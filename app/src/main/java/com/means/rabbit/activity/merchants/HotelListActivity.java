package com.means.rabbit.activity.merchants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.view.DateDialog;
import net.duohuo.dhroid.view.DateDialog.OnDateResultListener;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.activity.home.SelectCityActivity;
import com.means.rabbit.activity.main.MainActivity;
import com.means.rabbit.activity.main.SearchActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.bean.BackHomeEB;
import com.means.rabbit.bean.HotelCityEB;
import com.means.rabbit.utils.DateUtils;
import com.means.rabbit.utils.RabbitPerference;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.TabView;
import com.means.rabbit.views.TabView.OnTabSelectListener;

import de.greenrobot.event.EventBus;

public class HotelListActivity extends RabbitBaseActivity {

	LayoutInflater mLayoutInflater;

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;

	TabView tabV;

	TextView startDateT, endDateT;

	DateDialog timeDialog;

	String catid;

	String keywords;

	TextView cityT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_hotel_list);

	}

	@Override
	public void initView() {
		setTitle(getString(R.string.hotel_list));
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
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().hotelList, self,
				R.layout.item_hotel_list);

		RabbitPerference per = IocContainer.getShare().get(
				RabbitPerference.class);
		per.load();
		adapter.addparam("catid", catid);
		adapter.addparam("keywords", keywords);
		adapter.fromWhat("list");

		adapter.addField("title", R.id.title);
		adapter.addField("pic", R.id.pic);
		adapter.addField(new FieldMap("price", R.id.price) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				TextView comment_desT = (TextView) itemV

				.findViewById(R.id.comment_des);
				JSONObject data = (JSONObject) jo;

				comment_desT.setText(getString(R.string.hotel_comment_des)
						+ JSONUtil.getString(data, "score") + "/"
						+ JSONUtil.getString(data, "comment"));

				return getString(R.string.money_symbol) + o
						+ getString(R.string.hotel_price_des);
			}
		});
		adapter.addField("tuidingdes", R.id.price_des);
		adapter.addField("tuangoudes", R.id.order_des);
		adapter.addField("address", R.id.address);
		// adapter.addField("title", R.id.title);
		// adapter.addField("title", R.id.title);
		// adapter.addField("title", R.id.title);
		listV.setAdapter(adapter);
		contentListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				JSONObject jo = adapter.getTItem(position);
				Intent it = new Intent(self, HotelDetailActivity.class);
				it.putExtra("hotelId", JSONUtil.getString(jo, "id"));
				it.putExtra("startDate", startDateT.getText().toString());
				it.putExtra("endDate", endDateT.getText().toString());
				startActivity(it);

			}
		});

		tabV = (TabView) findViewById(R.id.tab);
		String name = getIntent().getStringExtra("name");
		if (TextUtils.isEmpty(name)) {
			tabV.setLeftText(getString(R.string.hotel_list));
		} else {
			tabV.setLeftText(name);
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

		startDateT = (TextView) findViewById(R.id.start_date);
		endDateT = (TextView) findViewById(R.id.end_date);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		startDateT.setText(df.format(new Date()));

		endDateT.setText(df.format(getNextDay(new Date())));

		timeDialog = new DateDialog();
		startDateT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timeDialog.setOnDateResultListener(new OnDateResultListener() {

					@Override
					public void result(String date, long datetime) {
						startDateT.setText(date);
						startDateT.setTag(datetime);

						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date date1 = df.parse(date);
							String endDate = df.format(getNextDay(date1));
							endDateT.setText(endDate);
							adapter.addparam("enddate", endDate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						adapter.addparam("startdate", date);
						adapter.refreshDialog();
					}
				});
				timeDialog.show(self, "-", -1);
			}
		});

		endDateT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				timeDialog.setOnDateResultListener(new OnDateResultListener() {

					@Override
					public void result(String date, long datetime) {
						endDateT.setText(date);
						adapter.addparam("enddate", date);
						adapter.refreshDialog();
					}
				});
				// Calendar calendar = Calendar.getInstance();
				// calendar.setTimeInMillis(DateUtils.getStringToDate2(startDateT.getText().toString()));
				// calendar.add(Calendar.DAY_OF_MONTH, +1);
				// Log.d("------calendar.getTimeInMillis()-----",calendar.getTimeInMillis()+"");

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = formatter
							.parse(startDateT.getText().toString());
					timeDialog.show(self, "-", date.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		cityT = (TextView) findViewById(R.id.hotel_city);
		cityT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(self, SelectCityActivity.class);
				it.putExtra("from", "hotellist");
				startActivity(it);
			}
		});
	}

	public Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		date = calendar.getTime();
		return date;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(HotelCityEB city) {
		adapter.addparam("cityid", city.getCatid());
		adapter.refresh();
		cityT.setText(city.getCityname());
	}
}
