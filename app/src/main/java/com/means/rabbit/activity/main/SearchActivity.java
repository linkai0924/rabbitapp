package com.means.rabbit.activity.main;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.means.rabbit.R;
import com.means.rabbit.activity.merchants.DaiGouActivity;
import com.means.rabbit.activity.merchants.FoodListActivity;
import com.means.rabbit.activity.merchants.HotelListActivity;
import com.means.rabbit.activity.travel.TravelActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;

/**
 * 
 * 搜索
 * 
 * @author Administrator
 * 
 */
public class SearchActivity extends RabbitBaseActivity {

	ListView listView;

	NetJSONAdapter adapter;

	String keywords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.search));

		listView = (ListView) findViewById(R.id.listview_normal);
		adapter = new NetJSONAdapter(new API().search, self,
				R.layout.item_search_list);
//		String keywords = ((EditText) findViewById(R.id.content)).getText()
//				.toString().trim();
//		adapter.addparam("keywords", keywords);
		adapter.fromWhat("data");
		adapter.addField("name", R.id.name);
		adapter.addField(new FieldMap("count", R.id.count) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				if (o.toString().equals("0")) {
					itemV.findViewById(R.id.count)
							.setVisibility(View.INVISIBLE);
				} else {
					itemV.findViewById(R.id.count).setVisibility(View.VISIBLE);
				}

				return o + getString(R.string.search_text);
			}
		});
		adapter.addField(new FieldMap("type", R.id.type) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {

				return getTypeContent(o.toString());
			}
		});
		listView.setAdapter(adapter);

		findViewById(R.id.search).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						search();
					}
				});
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject jo = (JSONObject) adapter.getItem(position);
				String type = JSONUtil.getString(jo, "type");
				
				Intent it = getTypeIntent(type);
				startActivity(it);
			}
		});

	}

	private void search() {
		keywords = ((EditText) findViewById(R.id.content)).getText().toString()
				.trim();
		if (TextUtils.isEmpty(keywords)) {
			showToast(getString(R.string.search_content));
			return;
		}
		adapter.addparam("keywords", keywords);
		adapter.refresh();
	}

	private String getTypeContent(String type) {
		if (type.equals("1")) {
			return getString(R.string.search_type1);
		} else if (type.equals("2")) {
			return getString(R.string.search_type2);
		} else if (type.equals("3")) {
			return getString(R.string.search_type3);
		} else if (type.equals("4")) {
			return getString(R.string.search_type4);
		} else if (type.equals("5")) {
			return getString(R.string.search_type5);
		}
		return "";
	}

	private Intent getTypeIntent(String type) {
		Intent it;
		if (type.equals("1")) { // 资讯
			it = new Intent(self, TravelActivity.class);
			it.putExtra("title", getString(R.string.lvxingxiaomi));
			it.putExtra("keywords", keywords);
			return it;
		} else if (type.equals("2")) { // 商家
			it = new Intent(self, FoodListActivity.class);
			it.putExtra("title", getString(R.string.meishi));
			it.putExtra("keywords", keywords);
			return it;
		} else if (type.equals("3")) { // 团购
			it = new Intent(self, FoodListActivity.class);
			it.putExtra("title", getString(R.string.meishi));
			it.putExtra("keywords", keywords);
			return it;
		} else if (type.equals("4")) { // 代购
			it = new Intent(self, DaiGouActivity.class);
			it.putExtra("keywords", keywords);
			return it;
		} else { // 酒店
			it = new Intent(self, HotelListActivity.class);
			it.putExtra("keywords", keywords);
		}
		return it;
	}

}
