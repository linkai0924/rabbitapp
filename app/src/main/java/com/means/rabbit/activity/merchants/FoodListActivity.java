package com.means.rabbit.activity.merchants;

import org.json.JSONArray;
import org.json.JSONObject;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.activity.main.SearchActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitUtils;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.TabView;
import com.means.rabbit.views.TabView.OnTabSelectListener;

public class FoodListActivity extends RabbitBaseActivity {

	LayoutInflater mLayoutInflater;

	View headV;

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;

	TabView tabV;

	String catid;

	String keywords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_list);
	}

	@Override
	public void initView() {
		setTitle(getIntent().getStringExtra("title"));
		setRightAction2(R.drawable.icon_green_search,
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent it = new Intent(self, SearchActivity.class);
						startActivity(it);
					}
				});
		catid = getIntent().getStringExtra("catid");
		keywords = getIntent().getStringExtra("keywords");
		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		mLayoutInflater = LayoutInflater.from(self);
		headV = mLayoutInflater.inflate(R.layout.head_food_list, null);
		headV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(self, GuessLikeActivity.class);
				startActivity(it);
			}
		});
		listV.addHeadView(headV);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().foodList, self,
				R.layout.item_food_list);
		adapter.addparam("catid", catid);
		adapter.addparam("keywords", keywords);
		adapter.fromWhat("list");
		adapter.addField("title", R.id.title);
		adapter.addField("price", R.id.price);
		adapter.addField("pic", R.id.pic);
		adapter.addField("tuangoudes", R.id.des);
		adapter.addField("tuidingdes", R.id.order_des);
		adapter.addField("areaname", R.id.area);
		adapter.addField(new FieldMap("price", R.id.price) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				JSONObject data = (JSONObject) jo;
				TextView comment_desT = (TextView) itemV
						.findViewById(R.id.comment_des);
				comment_desT.setText(getString(R.string.food_comment_des)
						+ JSONUtil.getString(data, "score") + "/"
						+ JSONUtil.getString(data, "comment"));
				// TODO Auto-generated method stub
				return getString(R.string.money_symbol) + o;
			}
		});

		listV.setAdapter(adapter);
		contentListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				JSONObject jo = adapter.getTItem(position
						- contentListV.getHeaderViewsCount());
				Intent it = new Intent(self, ShopDetailActivity.class);
				it.putExtra("shopId", JSONUtil.getString(jo, "id"));
				startActivity(it);

			}
		});

		tabV = (TabView) findViewById(R.id.tab);
		String tabName = getIntent().getStringExtra("name");
		if (!TextUtils.isEmpty(tabName)) {
			tabV.setLeftText(tabName);
		} else {
			tabV.setLeftText(getIntent().getStringExtra("title"));
		}
		tabV.setCentertText(getString(R.string.food_near), "");
		tabV.setOnTabSelectListener(new OnTabSelectListener() {

			@Override
			public void onRightSelect(String result) {
				adapter.addparam("order", result);
				adapter.refreshDialog();
			}

			@Override
			public void onCenterSelect(String result) {
				adapter.addparam("catid", result);
				adapter.refreshDialog();
			}
		});

		getAD();
	}

	private void getAD() {
		DhNet net = new DhNet(new API().listad);
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {

					JSONArray jsa = response.jSONArrayFrom("data");
					if (jsa != null) {
						if (jsa.length() == 2) {
							final JSONObject jo1 = JSONUtil.getJSONObjectAt(
									jsa, 0);
							ImageView pic1I = (ImageView) headV
									.findViewById(R.id.pic1);
							pic1I.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									RabbitUtils.ImgIntent(self,
											JSONUtil.getInt(jo1, "type"),
											JSONUtil.getString(jo1, "id"));
								}
							});
							ViewUtil.bindNetImage(pic1I,
									JSONUtil.getString(jo1, "pic"), "default");

							final JSONObject jo2 = JSONUtil.getJSONObjectAt(
									jsa, 1);
							ImageView pic2I = (ImageView) headV
									.findViewById(R.id.pic2);
							pic2I.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									RabbitUtils.ImgIntent(self,
											JSONUtil.getInt(jo2, "type"),
											JSONUtil.getString(jo2, "id"));
								}
							});
							ViewUtil.bindNetImage(pic2I,
									JSONUtil.getString(jo2, "pic"), "default");
						}
					}

				}

			}
		});
	}

}
