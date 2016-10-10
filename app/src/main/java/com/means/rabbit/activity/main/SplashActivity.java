package com.means.rabbit.activity.main;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.GlobalParams;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.UserLocation;
import net.duohuo.dhroid.util.UserLocation.OnLocationChanged;

import org.json.JSONObject;

import com.means.rabbit.R;
import com.means.rabbit.activity.home.SelectCityActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.bean.CityEB;
import com.means.rabbit.bean.User;
import com.means.rabbit.utils.RabbitPerference;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * 欢迎页
 * 
 * @author Administrator
 * 
 */
public class SplashActivity extends RabbitBaseActivity {

	private final Handler mHandler = new Handler();

	RabbitPerference per;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		EventBus.getDefault().register(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		per = IocContainer.getShare().get(RabbitPerference.class);
		per.load();

		if (per.isFirst == 0) {
			final UserLocation location = UserLocation.getInstance();
			location.setOnLocationChanged(new OnLocationChanged() {

				@Override
				public void change(double latitude, double longitude) {
					getCity(location.getCity());

				}
			});
			location.init(self);
		} else {
			if (!TextUtils.isEmpty(per.name)
					&& !TextUtils.isEmpty(per.password)) {
				login();
			} else {
				notFirst();
			}
		}

	}

	private void login() {

		DhNet net = new DhNet(new API().login);
		net.addParam("name", per.name);
		net.addParam("pswd", per.password);
		// net.addParam("phone", "13852286536");
		// net.addParam("password", "123");
		net.doPostInDialog(getString(R.string.login_des), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();

					if (TextUtils.isEmpty(JSONUtil.getString(jo, "type"))) {
						User.getInstance().setType(1);
					} else {
						User.getInstance().setType(JSONUtil.getInt(jo, "type"));
					}
					per = IocContainer.getShare().get(RabbitPerference.class);
					per.load();
					per.setName(JSONUtil.getString(jo, "name"));
					per.setNickname(JSONUtil.getString(jo, "nickname"));
					per.setPhone(JSONUtil.getString(jo, "phone"));
					per.setSex(JSONUtil.getString(jo, "sex"));
					per.setFaceimg_s(JSONUtil.getString(jo, "faceimg_s"));
					per.setMsgcount(JSONUtil.getString(jo, "msgcount"));
					per.setOrdercount(JSONUtil.getString(jo, "ordercount"));
					per.setGroupname(JSONUtil.getString(jo, "groupname"));
					per.setEmail(JSONUtil.getString(jo, "email"));
					per.commit();

					User user = User.getInstance();
					user.setLogin(true);

					// showToast("登录成功");

					Intent it = new Intent(self, MainActivity.class);
					startActivity(it);
					finishWithoutAnim();
					// 登录成功后发送事件,关闭之前的页面
				} else {
					notFirst();
				}

			}

			@Override
			public void onErray(Response response) {
				// TODO Auto-generated method stub
				super.onErray(response);
				notFirst();
			}
		});
	}

	private void notFirst() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(self, MainActivity.class);
				startActivity(intent);
				finishWithoutAnim();
			}
		}, 2000);
	}

	private void getCity(final String cityname) {
		DhNet net = new DhNet(new API().getcity);
		net.addParam("city", cityname);
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {

					JSONObject jo = response.jSON();
					String cityid = JSONUtil.getString(jo, "cityid");

					GlobalParams globalParams = IocContainer.getShare().get(
							GlobalParams.class);
					globalParams.setGlobalParam("lang", "cn");
					globalParams.setGlobalParam("cityid", cityid);
					per.catid = cityid;
					per.cityname = cityname;
					per.isFirst = 1;
					per.commit();
					notFirst();

				} else {
					Intent it = new Intent(self, SelectCityActivity.class);
					startActivity(it);
				}

			}
		});
	}

	public void onEventMainThread(CityEB city) {
		per.isFirst = 1;
		per.commit();
		Intent intent = new Intent(self, MainActivity.class);
		startActivity(intent);
		finishWithoutAnim();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
