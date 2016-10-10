package com.means.rabbit.activity.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.means.rabbit.R;
import com.means.rabbit.base.RabbitBaseActivity;

/**
 * 
 * 外卖订单
 * 
 * @author Administrator
 *
 */
public class TakeAwayOrderActivity extends RabbitBaseActivity {

	ListView mList;
	
	LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_away_order);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.takeawayorder));
		
		inflater = LayoutInflater.from(self);
		View footView = inflater.inflate(R.layout.take_away_order_footview, null);
//		R.layout.item_take_away_order_list
		
		mList = (ListView) findViewById(R.id.listview_normal);
		mList.addFooterView(footView);
	}
}
