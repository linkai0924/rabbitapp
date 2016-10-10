package com.means.rabbit.views;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.means.rabbit.R;
import com.means.rabbit.adapter.CatLeftAdapter;
import com.means.rabbit.adapter.CatRightAdapter;
import com.means.rabbit.api.API;
import com.means.rabbit.utils.RabbitPerference;

public class AreaPop {
	Context context;

	View contentV;

	PopupWindow pop;

	ListView leftListV, rightListV;

	CatLeftAdapter leftAapter;

	CatRightAdapter rightAdapter;

	JSONArray jsa;

	OnReslutClickListener onReslutClickListener;

	public AreaPop(Context context) {
		this.context = context;
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
				try {
					JSONArray jsa = jo.getJSONArray("_child");
					rightAdapter.setData(jsa);
				} catch (JSONException e) {
					if (onReslutClickListener != null) {
						onReslutClickListener.result(
								JSONUtil.getString(jo, "name"),
								JSONUtil.getString(jo, "id"));
					}
					rightAdapter.setData(null);
					pop.dismiss();
					e.printStackTrace();
				}
			}
		});

		rightListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject jo = rightAdapter.getItem(position);
				if (onReslutClickListener != null) {
					onReslutClickListener.result(
							JSONUtil.getString(jo, "name"),
							JSONUtil.getString(jo, "id"));
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

		getdata();
	}

	private void getdata() {

		RabbitPerference per = IocContainer.getShare().get(
				RabbitPerference.class);
		per.load();
		DhNet net = new DhNet(new API().arealist);
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

}
