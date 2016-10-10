package com.means.rabbit.views;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera.Area;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;

import com.means.rabbit.R;
import com.means.rabbit.activity.merchants.DaiGouActivity;
import com.means.rabbit.activity.merchants.FoodListActivity;
import com.means.rabbit.activity.merchants.HotelListActivity;
import com.means.rabbit.activity.travel.TravelActivity;
import com.means.rabbit.adapter.CatLeftAdapter;
import com.means.rabbit.adapter.CatRightAdapter;
import com.means.rabbit.api.API;
import com.means.rabbit.utils.RabbitPerference;
import com.means.rabbit.views.AreaPop.OnReslutClickListener;

public class CatPop {

	Context context;

	View contentV;

	PopupWindow pop;

	ListView leftListV, rightListV;

	CatLeftAdapter leftAapter;

	CatRightAdapter rightAdapter;

	JSONArray jsa;

	OnReslutClickListener onReslutClickListener;

	public static int CAT = 1;
	public static int AREA = 2;
	public static int BRAND = 3;

	int type;

	int parentId;

	public CatPop(Context context, int type) {
		this.context = context;
		this.type = type;
		contentV = LayoutInflater.from(context).inflate(R.layout.pop_cat, null);
		pop = new PopupWindow(contentV, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT, true);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		// pop.setAnimationStyle(R.style.mystyle);
		pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		initView();

	}

	private void initView() {
		leftListV = (ListView) contentV.findViewById(R.id.lrft_list);
		rightListV = (ListView) contentV.findViewById(R.id.right_list);

		leftAapter = new CatLeftAdapter(context);
		leftListV.setAdapter(leftAapter);

		rightAdapter = new CatRightAdapter(context);
		rightListV.setAdapter(rightAdapter);

		leftListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				leftAapter.setCurrentPosition(position);
				JSONObject jo = leftAapter.getItem(position);
				parentId = JSONUtil.getInt(jo, "id");

				JSONArray childJsa;

				try {
					childJsa = jo.getJSONArray("_child");
					childJsa.put(
							0,
							new JSONObject()
									.put("name",
											context.getResources().getString(
													R.string.all))
									.put("title",
											JSONUtil.getString(jo, "name"))
									.put("id", JSONUtil.getString(jo, "id")));
					rightAdapter.setData(childJsa);
				} catch (JSONException e) {

					childJsa = new JSONArray();
					try {
						childJsa.put(0, new JSONObject().put("name", context
								.getResources().getString(R.string.all)));
					} catch (NotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					rightAdapter.setData(childJsa);

					// goNext(JSONUtil.getString(jo, "id"),
					// JSONUtil.getString(jo, "name"));
					// if (onReslutClickListener != null) {
					// onReslutClickListener.result(
					// JSONUtil.getString(jo, "name"),
					// JSONUtil.getString(jo, "id"));
					// }
					// rightAdapter.setData(null);
					// pop.dismiss();
					e.printStackTrace();
				}

			}
		});

		rightListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject jo = rightAdapter.getItem(position);
				if (TextUtils.isEmpty(JSONUtil.getString(jo, "title"))) {
					if (onReslutClickListener != null) {
						onReslutClickListener.result(
								JSONUtil.getString(jo, "name"),
								JSONUtil.getString(jo, "id"));
					}

					goNext(JSONUtil.getString(jo, "id"),
							JSONUtil.getString(jo, "name"));
				} else {
					if (onReslutClickListener != null) {
						onReslutClickListener.result(
								JSONUtil.getString(jo, "title"),
								JSONUtil.getString(jo, "id"));
					}

					goNext(JSONUtil.getString(jo, "id"),
							JSONUtil.getString(jo, "title"));
				}

				pop.dismiss();
			}
		});

		contentV.findViewById(R.id.bg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						pop.dismiss();
					}
				});

		getdata(type);
	}

	private void getdata(int type) {

		RabbitPerference per = IocContainer.getShare().get(
				RabbitPerference.class);
		per.load();
		String url = null;
		if (type == CAT) {
			url = new API().catlist;
		} else if (type == AREA) {
			url = new API().arealist;
		} else if (type == BRAND) {
			url = new API().brandlist;
		}
		DhNet net = new DhNet(url);
		net.addParam("cityid", per.catid);
		net.doGetInDialog(new NetTask(context) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					jsa = response.jSONArrayFromData();
					leftAapter.setData(jsa);
				}

			}
		});
	}

	public void show(View v) {
		pop.showAsDropDown(v);
	}

	public OnReslutClickListener getOnReslutClickListener() {
		return onReslutClickListener;
	}

	public void setOnReslutClickListener(
			OnReslutClickListener onReslutClickListener) {
		this.onReslutClickListener = onReslutClickListener;
	}

	public interface OnReslutClickListener {
		void result(String catname, String catid);
	}

	private void goNext(String catid, String name) {
		if (type == CAT) {
			Intent it = null;
			switch (parentId) {
			case 1:
				it = new Intent(context, FoodListActivity.class);
				it.putExtra("title", context.getString(R.string.meishi));
				break;

			case 2:
				it = new Intent(context, HotelListActivity.class);
				break;

			// 货币兑换
			case 3:
				it = new Intent(context, FoodListActivity.class);
				it.putExtra("title", context.getString(R.string.huobiduihuan));
				break;

			// 出行服务
			case 4:
				it = new Intent(context, FoodListActivity.class);
				it.putExtra("title", context.getString(R.string.chuxingfuwu));
				break;

			// 旅行小米
			case 5:
				it = new Intent(context, TravelActivity.class);
				it.putExtra("title", context.getString(R.string.lvxingxiaomi));
				break;

			// 休闲娱乐
			case 6:
				it = new Intent(context, FoodListActivity.class);
				it.putExtra("title", context.getString(R.string.xiuxianyule));
				break;

			// 专享特色
			case 7:
				it = new Intent(context, FoodListActivity.class);
				it.putExtra("title", context.getString(R.string.zhuanxiangtese));
				break;

			// 紧急求助
			case 8:
				it = new Intent(context, TravelActivity.class);
				it.putExtra("title", context.getString(R.string.jinjiqiuzhu));
				break;

			case 35:
				it = new Intent(context, DaiGouActivity.class);
				break;

			default:
				break;
			}
			it.putExtra("catid", catid);
			it.putExtra("name", name);
			context.startActivity(it);
		}
	}

}
