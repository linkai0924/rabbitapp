package com.means.rabbit.activity.travel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseFragment;
import com.means.rabbit.bean.CityEB;
import com.means.rabbit.utils.DateUtils;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.TabView;
import com.means.rabbit.views.TabView.OnTabSelectListener;

import de.greenrobot.event.EventBus;

public class TravelFragment extends RabbitBaseFragment implements
		OnClickListener {
	static TravelFragment instance;

	View mainV;

	LayoutInflater mLayoutInflater;

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;

	TabView tabV;

	public static TravelFragment getInstance() {
		if (instance == null) {
			instance = new TravelFragment();
		}

		return instance;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		mainV = inflater.inflate(R.layout.fragment_travel, null);
		mLayoutInflater = inflater;
		initView();
		// TODO Auto-generated method stub
		return mainV;
	}

	private void initView() {
		View backV = mainV.findViewById(R.id.backLayout);
		backV.setVisibility(View.GONE);

		TextView titleT = (TextView) mainV.findViewById(R.id.title);
		if (titleT != null) {
			titleT.setText(getString(R.string.lvxingxiaomi));
		}
		listV = (RefreshListViewAndMore) mainV.findViewById(R.id.my_listview);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().contentlist, getActivity(),
				R.layout.item_travel_list);
		adapter.addparam("catid", "5");
		adapter.fromWhat("list");
		adapter.addField("title", R.id.title);
		adapter.addField(new FieldMap("views", R.id.views) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {

				return getString(R.string.travel_des) + o;
			}
		});
		adapter.addField("des", R.id.des);
		adapter.addField(new FieldMap("adddateline", R.id.adddateline) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				return DateUtils.dateToStr(new Date(
						Long.parseLong(o.toString()) * 1000));
			}
		});
		adapter.addField("pic", R.id.pic);

		listV.setAdapter(adapter);
		contentListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent it = new Intent(getActivity(),
						TravelDetailActivity.class);
				JSONObject jo = adapter.getTItem(position);
				it.putExtra("id", JSONUtil.getInt(jo, "id"));
				startActivity(it);
			}
		});

		tabV = (TabView) mainV.findViewById(R.id.tab);
		tabV.setLeftText(getString(R.string.lvxingxiaomi));
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

	@Override
	public void onClick(View v) {
	}
}
