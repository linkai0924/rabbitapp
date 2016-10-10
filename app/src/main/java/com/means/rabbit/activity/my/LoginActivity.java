package com.means.rabbit.activity.my;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.activity.my.edit.ForgetPswdActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.bean.User;
import com.means.rabbit.manage.UserInfoManage.LoginCallBack;
import com.means.rabbit.utils.RabbitPerference;

public class LoginActivity extends RabbitBaseActivity implements
		OnClickListener {

	private EditText nicknameEt, passwordEt;
	private Button loginBtn;
	private TextView forgetpswdT, registerT;

	RabbitPerference per;
	public static LoginCallBack loginCall;
	boolean isLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.login));

		nicknameEt = (EditText) findViewById(R.id.nickname);
		passwordEt = (EditText) findViewById(R.id.password);
		loginBtn = (Button) findViewById(R.id.login);
		forgetpswdT = (TextView) findViewById(R.id.forgetpswd);
		registerT = (TextView) findViewById(R.id.register);

		loginBtn.setOnClickListener(this);
		forgetpswdT.setOnClickListener(this);
		registerT.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent it;
		switch (v.getId()) {
		// 登录
		case R.id.login:
			login();
			break;
		// 忘记密码
		case R.id.forgetpswd:
			it = new Intent(self, ForgetPswdActivity.class);
			startActivity(it);
			break;
		// 注册
		case R.id.register:
			it = new Intent(self, RegisterActivity.class);
			startActivity(it);
			break;
		}

	}

	private void login() {
		final String nickname = nicknameEt.getText().toString();
		final String password = passwordEt.getText().toString();

		// final String nickname = "18151906146";
		// final String password = "123456 a";
		if (TextUtils.isEmpty(nickname)) {
			showToast(getString(R.string.login_name_des));
			return;
		}
		if (TextUtils.isEmpty(password)) {
			showToast(getString(R.string.login_pswd_hint));
			return;
		}
		if (password.length() < 6 || password.length() > 15) {
			showToast(getString(R.string.editinfo_pswd_des));
			return;
		}
		DhNet smsNet = new DhNet(new API().login);
		smsNet.addParam("pswd", password);
		smsNet.addParam("name", nickname);

		smsNet.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					User.getInstance().setLogin(true);
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
					per.setPassword(password);
					per.commit();
					isLogin = true;
					finish();
				}
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		if (loginCall != null) {
			if (isLogin) {
				loginCall.onisLogin();
			} else {
				loginCall.onLoginFail();
			}
		}
		loginCall = null;
	}

}
