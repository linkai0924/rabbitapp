package com.means.rabbit.activity.merchants;

import org.json.JSONObject;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;

import com.means.rabbit.R;
import com.means.rabbit.activity.home.HomePageFragment;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseFragment;
import com.means.rabbit.bean.CityEB;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.TabView;
import com.means.rabbit.views.TabView.OnTabSelectListener;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MerchatsFragment extends RabbitBaseFragment {

	static MerchatsFragment instance;

	View mainV;

	LayoutInflater mLayoutInflater;

	View headV;

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;

	TabView tabV;

	public static MerchatsFragment getInstance() {
		if (instance == null) {
			instance = new MerchatsFragment();
		}

		return instance;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainV = inflater.inflate(R.layout.fragment_travel, null);
		mLayoutInflater = inflater;
		EventBus.getDefault().register(this);
		initView();
		// TODO Auto-generated method stub
		return mainV;
	}

	private void initView() {

		View backV = mainV.findViewById(R.id.backLayout);
		backV.setVisibility(View.GONE);

		TextView titleT = (TextView) mainV.findViewById(R.id.title);
		if (titleT != null) {
			titleT.setText(getString(R.string.search_type2));
		}

		listV = (RefreshListViewAndMore) mainV.findViewById(R.id.my_listview);
		mLayoutInflater = LayoutInflater.from(getActivity());
		headV = mLayoutInflater.inflate(R.layout.head_food_list, null);
		headV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(getActivity(), GuessLikeActivity.class);
				startActivity(it);
			}
		});
		listV.addHeadView(headV);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().foodList, getActivity(),
				R.layout.item_food_list);
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
				Intent it = new Intent(getActivity(), ShopDetailActivity.class);
				it.putExtra("shopId", JSONUtil.getString(jo, "id"));
				startActivity(it);

			}
		});

		tabV = (TabView) mainV.findViewById(R.id.tab);
		tabV.setLeftText(getString(R.string.meishi));
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
	}

	public void onEventMainThread(CityEB city) {
		// TextView cityT = (TextView) mainV.findViewById(R.id.city);
		// cityT.setText(city.getCityname());

		adapter.addparam("cityid", city.getCatid());
		adapter.refresh();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
