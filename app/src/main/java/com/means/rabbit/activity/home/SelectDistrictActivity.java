package com.means.rabbit.activity.home;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.means.rabbit.R;
import com.means.rabbit.adapter.CityAdapter;
import com.means.rabbit.adapter.CountryAdapter;
import com.means.rabbit.adapter.DistrictlistAdapter;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.bean.CityEB;
import com.means.rabbit.bean.DistrictEB;
import com.means.rabbit.utils.RabbitPerference;

import de.greenrobot.event.EventBus;

public class SelectDistrictActivity extends RabbitBaseActivity {

	ListView countrylist, citylist,districtlist;

	JSONArray jsa;

	CountryAdapter countryAdapter;
	
	DistrictlistAdapter districtlistAdapter;

	CityAdapter cityAdapter;
	
	DistrictEB districtBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_district);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.select_city));
		districtBean = new DistrictEB();

		countrylist = (ListView) findViewById(R.id.countrylist);
		citylist = (ListView) findViewById(R.id.citylist);
		districtlist = (ListView) findViewById(R.id.district);
		
		countryAdapter = new CountryAdapter(self);
		countrylist.setAdapter(countryAdapter);
		countrylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				countryAdapter.setCurrentPosition(position);
				JSONObject jo = countryAdapter.getItem(position);
				
				districtBean.setCountryid(JSONUtil.getString(jo, "id"));
				districtBean.setCountryName(JSONUtil.getString(jo, "name"));
				try {
					JSONArray jsa = jo.getJSONArray("_child");
					cityAdapter.setData(jsa);
				} catch (JSONException e) {
					cityAdapter.setData(null);
					EventBus.getDefault().post(districtBean);
					finish();
					e.printStackTrace();
				}
			}
		});
		cityAdapter = new CityAdapter(self);
		citylist.setAdapter(cityAdapter);
		citylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject jo = cityAdapter.getItem(position);
				
				districtBean.setCityid(JSONUtil.getString(jo, "id"));
				districtBean.setCityName(JSONUtil.getString(jo, "name"));
				
				getDistrict(JSONUtil.getInt(jo, "id"));
			}
		});
		
		districtlistAdapter = new DistrictlistAdapter(self);
		districtlist.setAdapter(districtlistAdapter);
		districtlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject jo = districtlistAdapter.getItem(position);
				districtBean.setDistrictid(JSONUtil.getString(jo, "id"));
				districtBean.setDistrictName(JSONUtil.getString(jo, "name"));
				
				EventBus.getDefault().post(districtBean);
				finish();
			}
		});
		getData();
	}

	private void getData() {
		DhNet net = new DhNet(new API().citylist);
		net.doGetInDialog(getString(R.string.progress_doing), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					jsa = response.jSONArrayFromData();
					countryAdapter.setData(jsa);
				}

			}
		});
	}
	
	private void getDistrict(int cityid) {
		DhNet net = new DhNet(new API().arealist);
		net.addParam("cityid", cityid);
		net.doGetInDialog(getString(R.string.progress_doing), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					jsa = response.jSONArrayFromData();
					districtlistAdapter.setData(jsa);
					if (jsa == null || jsa.length() == 0) {
						EventBus.getDefault().post(districtBean);
						finish();
					}
				}

			}
		});
	}

}
