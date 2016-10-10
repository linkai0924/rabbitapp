package com.means.rabbit.activity.finance;

import java.util.Date;

import org.json.JSONObject;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;

import com.means.rabbit.R;
import com.means.rabbit.R.layout;
import com.means.rabbit.activity.travel.TravelDetailActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.DateUtils;
import com.means.rabbit.views.RefreshListViewAndMore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 
 * 财务明细
 * @author Administrator
 *
 */
public class FinanceDetailActivity extends RabbitBaseActivity {
	
	LayoutInflater mLayoutInflater;

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_detail);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.finance_detail));
		
		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().accountlog, self,
				R.layout.item_finance_detail);
		adapter.fromWhat("list");
		adapter.addField("note", R.id.note);
		adapter.addField(new FieldMap("balance", R.id.balance) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				
				JSONObject data = (JSONObject) jo;
				
				TextView  typeT = (TextView) itemV.findViewById(R.id.type);
				if(JSONUtil.getString(data, "method").equals("payout")) {
					typeT.setText(getString(R.string.expend));
					typeT.setBackgroundResource(R.drawable.fillet_10_pink_bg);
				} else {
					typeT.setText(getString(R.string.income));
					typeT.setBackgroundResource(R.drawable.fillet_10_green_bg);
				}
				

				return getString(R.string.money_symbol) + o;
			}
		});
		adapter.addField(new FieldMap("dateline", R.id.dateline) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				return DateUtils.dateToStrLong(new Date(
						Long.parseLong(o.toString()) * 1000));
			}
		});
		listV.setAdapter(adapter);
		contentListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
//				Intent it = new Intent(self, TravelDetailActivity.class);
//				JSONObject jo = adapter.getTItem(position);
//				it.putExtra("id", JSONUtil.getInt(jo, "id"));
//				startActivity(it);
			}
		});
	}
}
