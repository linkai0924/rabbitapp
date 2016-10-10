package com.means.rabbit.activity.my;

import java.util.Date;

import org.json.JSONObject;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;

import com.means.rabbit.R;
import com.means.rabbit.R.id;
import com.means.rabbit.R.layout;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.DateUtils;
import com.means.rabbit.views.RefreshListViewAndMore;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 积分明细
 * @author dell
 *
 */
public class MyIntegralActivity extends RabbitBaseActivity {
	RefreshListViewAndMore listV;
	ListView contentListV;

	NetJSONAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_integral);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.my_integral));
		
		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		String url = new API().creditloglist;
		contentListV = listV.getListView();
		
		adapter = new NetJSONAdapter(url, self,
				R.layout.item_my_integral);
		adapter.fromWhat("list");
		adapter.addField("note", R.id.note);
		adapter.addField(new FieldMap("dateline", R.id.dateline) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				JSONObject data = (JSONObject) jo;
				TextView  statusT = (TextView) itemV.findViewById(R.id.status);
				if (JSONUtil.getFloat(data, "value")>0) {
					statusT.setText(getString(R.string.income));
					statusT.setBackgroundResource(R.drawable.fillet_10_green_bg);
				}else {
					statusT.setText(getString(R.string.expend));
					statusT.setBackgroundResource(R.drawable.fillet_10_pink_bg);
				}
				// TODO Auto-generated method stub
				return DateUtils.dateToStrLong(new Date(Long.parseLong(o
						.toString()) * 1000));
			}
		});
		// setUrl("http://cwapi.gongpingjia.com:8080/v2/activity/list?latitude=32&longitude=118&maxDistance=5000000&token="+user.getToken()+"&userId="+user.getUserId());
//		adapter.addField("area_name", R.id.name);
//		adapter.addField("image", R.id.pic, "default");
		listV.setAdapter(adapter);
	}
}
