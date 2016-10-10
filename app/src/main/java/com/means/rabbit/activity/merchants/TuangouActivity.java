package com.means.rabbit.activity.merchants;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.views.RefreshListViewAndMore;

public class TuangouActivity extends RabbitBaseActivity {
	
	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;
	
	String shopId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuangou);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.tuangou));
		shopId = getIntent().getStringExtra("shopId");
		
		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().hotelDetailNearTuangou, self,
				R.layout.item_shop_detail_tuangou_near);
		adapter.fromWhat("list");
		adapter.addparam("contentid", shopId);
		adapter.addparam("type", 1);
		adapter.addField("pic", R.id.pic);
		adapter.addField("title", R.id.title);
		adapter.addField("oldprice", R.id.oldprice);
		adapter.addField("ordercount", R.id.sell_count);
		adapter.addField(new FieldMap("price", R.id.price) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				return getString(R.string.money_symbol) + o + getString(R.string.hotel_people);
			}
		});

		listV.setAdapter(adapter);
		contentListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				JSONObject jo = adapter.getTItem(position
						- contentListV.getHeaderViewsCount());
				Intent it = new Intent(self, TuangouDetailActivity.class);
				it.putExtra("tuangouId", JSONUtil.getString(jo, "id"));
				startActivity(it);
			}
		});
	}
}
