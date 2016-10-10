package com.means.rabbit.activity.my;

import org.json.JSONObject;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import com.means.rabbit.R;
import com.means.rabbit.R.layout;
import com.means.rabbit.activity.merchants.DaiGouActivity;
import com.means.rabbit.activity.merchants.GoodDetailActivity;
import com.means.rabbit.activity.merchants.HotelDetailActivity;
import com.means.rabbit.activity.merchants.ShopDetailActivity;
import com.means.rabbit.activity.merchants.TuangouDetailActivity;
import com.means.rabbit.activity.travel.TravelActivity;
import com.means.rabbit.activity.travel.TravelDetailActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.views.RefreshListViewAndMore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 收藏列表
 * 
 * @author dell
 * 
 */
public class MyCollectActivity extends RabbitBaseActivity {
	RefreshListViewAndMore listV;
	ListView contentListV;

	NetJSONAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collect);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.my_collect));

		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		contentListV = listV.getListView();

		adapter = new NetJSONAdapter(new API().collectlist, self,
				R.layout.item_my_collect);
		adapter.fromWhat("list");
		adapter.addField(new FieldMap("title", R.id.title) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {

				JSONObject data = (JSONObject) jo;
				int type = JSONUtil.getInt(data, "type");
				String typename = "";
				switch (type) {
				case 1:
					typename = getString(R.string.search_type1);
					break;

				case 2:
					typename = getString(R.string.search_type2);
					break;

				case 3:
					typename = getString(R.string.search_type5);
					break;

				case 4:
					typename = getString(R.string.search_type3);
					break;

				case 5:
					typename = getString(R.string.search_type4);
					break;

				default:
					break;
				}

				ViewUtil.bindView(itemV.findViewById(R.id.type), typename);
				// TODO Auto-generated method stub
				return o;
			}
		});
		listV.setAdapter(adapter);
		contentListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				JSONObject jo = adapter.getTItem(position
						- contentListV.getHeaderViewsCount());
				Intent it = null;
				int type = JSONUtil.getInt(jo, "type");
				switch (type) {
				case 1:
					it = new Intent(self, TravelDetailActivity.class);
					it.putExtra("id", JSONUtil.getInt(jo, "contentid"));
					break;

				case 2:
					it = new Intent(self, ShopDetailActivity.class);
					it.putExtra("shopId", JSONUtil.getString(jo, "contentid"));
					break;

				case 3:
					it = new Intent(self, HotelDetailActivity.class);
					it.putExtra("hotelId", JSONUtil.getString(jo, "contentid"));
					break;

				case 4:
					it = new Intent(self, TuangouDetailActivity.class);
					it.putExtra("tuangouId",
							JSONUtil.getString(jo, "contentid"));
					break;

				case 5:
					it = new Intent(self, GoodDetailActivity.class);
					it.putExtra("daigouId", JSONUtil.getString(jo, "contentid"));
					break;

				default:
					break;
				}

				startActivity(it);

			}
		});
	}
}
