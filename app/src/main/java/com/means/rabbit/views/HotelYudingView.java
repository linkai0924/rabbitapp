package com.means.rabbit.views;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.means.rabbit.R;
import com.means.rabbit.activity.order.AddShopOrderActivity;
import com.means.rabbit.activity.order.InsteadShoppingActivity;
import com.means.rabbit.manage.UserInfoManage;
import com.means.rabbit.manage.UserInfoManage.LoginCallBack;

public class HotelYudingView extends LinearLayout {
	Context mContext;

	LayoutInflater mLayoutInflater;

	String startDate;
	String endDate;

	public HotelYudingView(Context context) {
		super(context);
	}

	public HotelYudingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		this.mContext = context;
	}

	public void setData(JSONArray jsa, final String hotelname) {
		mLayoutInflater = LayoutInflater.from(mContext);
		if (jsa != null) {
			for (int i = 0; i < jsa.length(); i++) {
				final JSONObject jo = JSONUtil.getJSONObjectAt(jsa, i);
				View v = mLayoutInflater.inflate(R.layout.item_hotel_yuding,
						null);
				ViewUtil.bindView(v.findViewById(R.id.title),
						JSONUtil.getString(jo, "name"));
				ViewUtil.bindView(
						v.findViewById(R.id.price),
						mContext.getString(R.string.money_symbol)
								+ JSONUtil.getString(jo, "oldprice"));

				ViewUtil.bindView(
						v.findViewById(R.id.pre_price),
						mContext.getString(R.string.money_symbol)
								+ JSONUtil.getString(jo, "price"));
				ViewUtil.bindNetImage((ImageView) v.findViewById(R.id.pic),
						JSONUtil.getString(jo, "pic"), "default");
				ViewUtil.bindView(v.findViewById(R.id.des),
						JSONUtil.getString(jo, "des"));
				// ViewUtil.bindView(v.findViewById(R.id.title),
				// JSONUtil.getString(jo, "name"));

				v.findViewById(R.id.yuding).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {

								UserInfoManage.getInstance().checkLogin(
										(Activity) mContext,
										new LoginCallBack() {

											@Override
											public void onisLogin() {
												Intent it = new Intent(
														mContext,
														AddShopOrderActivity.class);

												it.putExtra("endDate", endDate);

												it.putExtra("startDate",
														startDate);
												it.putExtra("id", JSONUtil
														.getString(jo, "id"));
												mContext.startActivity(it);

											}

											@Override
											public void onLoginFail() {
												// TODO Auto-generated method
												// stub

											}
										});

							}
						});
				this.addView(v);
			}
		}

	}

	public void setDate(String startDate, String endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

}
