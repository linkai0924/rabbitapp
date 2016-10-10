package com.means.rabbit.activity.home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.means.rabbit.R;
import com.means.rabbit.activity.merchants.DaiGouActivity;
import com.means.rabbit.activity.merchants.FoodListActivity;
import com.means.rabbit.activity.merchants.HotelListActivity;
import com.means.rabbit.activity.travel.TravelActivity;
import com.means.rabbit.adapter.AllItemAdapter;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;

/**
 * 全部分类
 * 
 * @author Administrator
 * 
 */
public class AllItemActivity extends RabbitBaseActivity {

	ExpandableListView listView;

	AllItemAdapter adapter;

	int parentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_item);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.all_item));

		listView = (ExpandableListView) findViewById(R.id.expandableListView);

		adapter = new AllItemAdapter(self);
		listView.setAdapter(adapter);
		listView.setGroupIndicator(null);
		listView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				// TODO Auto-generated method stub
				return true;
			}
		});

		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				JSONObject groupJo = adapter.getGroup(groupPosition);
				parentId = JSONUtil.getInt(groupJo, "id");

				JSONObject jo = adapter.getChild(groupPosition, childPosition);
				if (TextUtils.isEmpty(JSONUtil.getString(jo, "title"))) {

					goNext(JSONUtil.getString(jo, "id"),
							JSONUtil.getString(jo, "name"));
				} else {

					goNext(JSONUtil.getString(jo, "id"),
							JSONUtil.getString(jo, "title"));
				}

				// TODO Auto-generated method stub
				return false;
			}
		});

		// listView.setOnChildClickListener(new
		// ExpandableListView.OnChildClickListener() {
		//
		// @Override
		// public boolean onChildClick(ExpandableListView parent, View v,
		// int groupPosition, int childPosition, long id) {
		// // TODO Auto-generated method stub
		// return false;
		// }
		// });

		getData();

	}

	private void getData() {
		DhNet net = new DhNet(new API().catlist);
		net.doGet(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					JSONArray jsa = response.jSONArrayFromData();
					adapter.setData(jsa);
					for (int i = 0; i < adapter.getGroupCount(); i++) {

						listView.expandGroup(i);

					}
				}
			}
		});
	}

	private void goNext(String catid, String name) {
		Intent it = null;
		switch (parentId) {
		case 1:
			it = new Intent(self, FoodListActivity.class);
			it.putExtra("title", self.getString(R.string.meishi));
			break;

		case 2:
			it = new Intent(self, HotelListActivity.class);
			break;

		// 货币兑换
		case 3:
			it = new Intent(self, FoodListActivity.class);
			it.putExtra("title", self.getString(R.string.huobiduihuan));
			break;

		// 出行服务
		case 4:
			it = new Intent(self, FoodListActivity.class);
			it.putExtra("title", self.getString(R.string.chuxingfuwu));
			break;

		// 旅行小米
		case 5:
			it = new Intent(self, TravelActivity.class);
			it.putExtra("title", self.getString(R.string.lvxingxiaomi));
			break;

		// 休闲娱乐
		case 6:
			it = new Intent(self, FoodListActivity.class);
			it.putExtra("title", self.getString(R.string.xiuxianyule));
			break;

		// 专享特色
		case 7:
			it = new Intent(self, FoodListActivity.class);
			it.putExtra("title", self.getString(R.string.zhuanxiangtese));
			break;

		// 紧急求助
		case 8:
			it = new Intent(self, TravelActivity.class);
			it.putExtra("title", self.getString(R.string.jinjiqiuzhu));
			break;

		case 35:
			it = new Intent(self, DaiGouActivity.class);
			break;

		default:
			break;
		}
		it.putExtra("catid", catid);
		it.putExtra("name", name);
		self.startActivity(it);
	}

}
