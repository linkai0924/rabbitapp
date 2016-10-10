package com.means.rabbit.activity.merchants;

import org.json.JSONArray;
import org.json.JSONObject;

import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.means.rabbit.R;
import com.means.rabbit.activity.main.SearchActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitUtils;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.TabView;
import com.means.rabbit.views.TabView.OnTabSelectListener;

/**
 * 
 * 代购列表
 * 
 * @author Administrator
 * 
 */
public class DaiGouActivity extends RabbitBaseActivity {

	LayoutInflater mLayoutInflater;

	View headV;

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;

	TabView tabV;

	String keywords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daigou_list);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.Hotboom));
		setRightAction2(R.drawable.icon_green_search,
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent it = new Intent(self, SearchActivity.class);
						startActivity(it);
					}
				});

		keywords = getIntent().getStringExtra("keywords");

		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		mLayoutInflater = LayoutInflater.from(self);
		headV = mLayoutInflater.inflate(R.layout.head_food_list, null);
		listV.addHeadView(headV);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().dgcontentlist, self,
				R.layout.item_daigou_list);
		adapter.addparam("keywords", keywords);
		adapter.fromWhat("list");
		adapter.addField("pic", R.id.pic);
		adapter.addField("price", R.id.price);
		adapter.addField("title", R.id.title);
		adapter.addField("score", R.id.score);
		adapter.addField("catname", R.id.catname);
		adapter.addField("des", R.id.des);
		listV.setAdapter(adapter);
		contentListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				JSONObject jo = adapter.getTItem(position
						- contentListV.getHeaderViewsCount());
				Intent it = new Intent(self, GoodDetailActivity.class);
				it.putExtra("daigouId", JSONUtil.getString(jo, "id"));
				startActivity(it);
			}
		});

		tabV = (TabView) findViewById(R.id.tab);
		tabV.setLeftText(getString(R.string.Hotboom));
		tabV.setCentertText(getString(R.string.Hotboom_brand), "品牌");
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
		DhNet net = new DhNet(new  API().listad);
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
