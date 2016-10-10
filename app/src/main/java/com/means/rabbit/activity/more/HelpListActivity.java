package com.means.rabbit.activity.more;

import org.json.JSONObject;

import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.views.RefreshListViewAndMore;

public class HelpListActivity extends RabbitBaseActivity {

	ListView listV;

	NetJSONAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_list);
	}

	@Override
	public void initView() {

		setTitle(getString(R.string.more_help));
		listV = (ListView) findViewById(R.id.listview_normal);
		adapter = new NetJSONAdapter(new API().helplist, self,
				R.layout.item_help_list);
		adapter.fromWhat("list");
		adapter.addField("title", R.id.name);
		listV.setAdapter(adapter);
		listV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				JSONObject jo = adapter.getTItem(position);
				Intent it = new Intent(self, HelpDetailActivity.class);
				it.putExtra("id", JSONUtil.getString(jo, "id"));
				startActivity(it);
			}
		});
		adapter.showNextInDialog();
	}

}
