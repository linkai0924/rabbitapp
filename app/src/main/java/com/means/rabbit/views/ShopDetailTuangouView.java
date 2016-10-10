package com.means.rabbit.views;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.means.rabbit.R;
import com.means.rabbit.activity.merchants.TuangouDetailActivity;

public class ShopDetailTuangouView extends LinearLayout {
	Context mContext;

	LayoutInflater mLayoutInflater;

	public ShopDetailTuangouView(Context context) {
		super(context);
	}

	public ShopDetailTuangouView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		this.mContext = context;
	}

	public void setData(JSONArray jsa) {
		mLayoutInflater = LayoutInflater.from(mContext);
		for (int i = 0; i < jsa.length(); i++) {
			final JSONObject jo = JSONUtil.getJSONObjectAt(jsa, i);
			if (i == 2) {
				View v = mLayoutInflater.inflate(R.layout.model_more, null);
				this.addView(v);
			} else {
				View v = mLayoutInflater.inflate(
						R.layout.item_shop_detail_tuangou, null);
				ViewUtil.bindView(v.findViewById(R.id.price),
						mContext.getString(R.string.money_symbol)+
						JSONUtil.getString(jo, "price"));
				ViewUtil.bindView(v.findViewById(R.id.old_price),
						mContext.getString(R.string.money_symbol)+JSONUtil.getString(jo, "oldprice"));
				ViewUtil.bindView(v.findViewById(R.id.title),
						JSONUtil.getString(jo, "title"));
				ViewUtil.bindView(v.findViewById(R.id.count),
						JSONUtil.getString(jo, "ordercount"));
				ViewUtil.bindNetImage((ImageView) v.findViewById(R.id.pic),
						JSONUtil.getString(jo, "pic"), "default");
				this.addView(v);
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(mContext,
								TuangouDetailActivity.class);
						it.putExtra("tuangouId", JSONUtil.getString(jo, "id"));
						mContext.startActivity(it);

					}
				});
			}
		}

	}
}
