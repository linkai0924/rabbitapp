package com.means.rabbit.activity.my.order;

import java.util.Date;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.activity.order.pay.GroupPayActivity;
import com.means.rabbit.activity.order.pay.HotelOrderDetailActivity;
import com.means.rabbit.activity.order.pay.InsteadShoppingPayActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.DateUtils;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.pop.MyOrderPop;
import com.means.rabbit.views.pop.MyOrderPop.OnReslutClickListener;

/**
 * 我的订单
 * 
 * @author dell
 * 
 */
public class MyOrderActivity extends RabbitBaseActivity implements
		OnClickListener {
	RefreshListViewAndMore listV;
	ListView contentListV;

	NetJSONAdapter adapter;

	LinearLayout classifyLl, typeLl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.my_order));

		classifyLl = (LinearLayout) findViewById(R.id.classify);
		typeLl = (LinearLayout) findViewById(R.id.type);

		classifyLl.setOnClickListener(this);
		typeLl.setOnClickListener(this);

		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		contentListV = listV.getListView();

		getDate();
	}

	private void showPop(int type, View v) {
		MyOrderPop pop;
		pop = new MyOrderPop(self, type);
		pop.setOnReslutClickListener(new OnReslutClickListener() {

			@Override
			public void result(int type, String catname, String tag) {
				if (type == 0) {
					adapter.addparam("type ", tag);
					adapter.refresh();
				} else {
					// showToast(tag);
					adapter.addparam("status ", tag);
					adapter.refresh();
				}
				// TODO Auto-generated method stub
			}
		});
		pop.show(v);
	}

	private void getDate() {
		adapter = new NetJSONAdapter(new API().orderlist, self,
				R.layout.item_order_list);
		adapter.fromWhat("list");
		adapter.addField("title", R.id.title);
		// adapter.addField("paystatus", R.id.paystatus);
		adapter.addField(new FieldMap("payprice", R.id.payprice) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {

				JSONObject data = (JSONObject) jo;

				// TODO Auto-generated method stub
				TextView paystatusT = (TextView) itemV
						.findViewById(R.id.paystatus);
				int paystatus = JSONUtil.getInt(data, "paystatus");

				int servicestatus = JSONUtil.getInt(data, "servicestatus");
				// grogshop_btn.setText(paystatus == 1 ? "支付订单" : "已支付");

				if (paystatus == 2 && servicestatus == 2
						&& JSONUtil.getInt(data, "orderstatus") == 2) {
					paystatusT
							.setText(getString(R.string.order_status_complete));
					paystatusT.setTextColor(getResources().getColor(R.color.text_dark_green));
					paystatusT
							.setBackgroundResource(R.drawable.fillet_10_frame_dark_green_bg);
				} else if (JSONUtil.getInt(data, "orderstatus") == 3) {
					paystatusT.setText(getString(R.string.order_status_cancle));
					paystatusT.setTextColor(getResources().getColor(R.color.text_99_grey));
					paystatusT.setBackgroundResource(R.drawable.btn_grey_bg);
				} else if (servicestatus == 1
						&& JSONUtil.getInt(data, "orderstatus") == 2) {
					paystatusT
							.setText(getString(R.string.order_status_comment));
					paystatusT
							.setBackgroundResource(R.drawable.fillet_10_frame_pink_bg);
					paystatusT.setTextColor(getResources().getColor(R.color.text_pink));
				} else if (paystatus == 1) {
					paystatusT.setText(getString(R.string.order_status_pay));
					paystatusT
							.setBackgroundResource(R.drawable.fillet_10_frame_pink_bg);
					paystatusT.setTextColor(getResources().getColor(R.color.text_pink));
				} else if (paystatus == 2) {
					paystatusT.setText(getString(R.string.order_status_use));
					paystatusT
							.setBackgroundResource(R.drawable.fillet_10_frame_pink_bg);
					paystatusT.setTextColor(getResources().getColor(R.color.text_pink));
				}

				return getString(R.string.money_symbol) + "  " + o.toString();
			}
		});
		adapter.addField(new FieldMap("adddateline", R.id.adddateline) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				return DateUtils.dateToStrLong(new Date(Long.parseLong(o
						.toString()) * 1000));
			}
		});
		adapter.addField("title", R.id.title);
		listV.setAdapter(adapter);

		contentListV
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						JSONObject jo = adapter.getTItem(position);
						Intent it;
						int type = JSONUtil.getInt(jo, "type");
						if (type == 2) {
							it = new Intent(self, GroupPayActivity.class);

						} else if (type == 1) {
							it = new Intent(self,
									HotelOrderDetailActivity.class);
						} else {
							it = new Intent(self,
									InsteadShoppingPayActivity.class);
						}
						it.putExtra("orderid", JSONUtil.getString(jo, "id"));
						startActivity(it);
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 分类
		case R.id.classify:
			showPop(0, v);
			break;
		// 状态
		case R.id.type:
			showPop(1, v);
			break;

		default:
			break;
		}
	}
}
