package com.means.rabbit.activity.home;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.adapter.PSAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.NormalGallery;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.activity.merchants.DaiGouActivity;
import com.means.rabbit.activity.merchants.FoodListActivity;
import com.means.rabbit.activity.merchants.HotelListActivity;
import com.means.rabbit.activity.merchants.ShopDetailActivity;
import com.means.rabbit.activity.travel.TravelActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseFragment;
import com.means.rabbit.bean.CityEB;
import com.means.rabbit.utils.RabbitUtils;
import com.means.rabbit.views.RefreshListViewAndMore;

import de.greenrobot.event.EventBus;

public class HomePageFragment extends RabbitBaseFragment implements
		OnClickListener {
	static HomePageFragment instance;

	View mainV;

	LayoutInflater mLayoutInflater;

	View headV;

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;

	NormalGallery gallery;
	PSAdapter galleryAdapter;
	// 中间部分控件id
	int[] pic_ids = { R.id.pic_1, R.id.pic_2, R.id.pic_3, R.id.pic_4 };

	// 美食点击按钮
	View foodLayoutV;

	// 酒店点击按钮
	View hotelLayoutV;

	// 随身翻译点击按钮
	View redpacketLayoutV;

	// 旅行小秘按钮
	View travel_layoutV;

	// 紧急求助
	View help_layoutV;

	// 全部分类
	View all_catV;

	// 出行服务
	View travel_servicesV;

	// 休闲娱乐
	View entertainmentV;

	// 专享特色
	View exclusive_characteristicsV;

	// 代购
	View daigouV;

	public static HomePageFragment getInstance() {
		if (instance == null) {
			instance = new HomePageFragment();
		}

		return instance;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainV = inflater.inflate(R.layout.fragment_home_page, null);
		mLayoutInflater = inflater;
		System.out.println("homefragment");
		EventBus.getDefault().register(this);
		initView();
		// TODO Auto-generated method stub
		return mainV;
	}

	private void initView() {
		initTitleBar(mainV);
		listV = (RefreshListViewAndMore) mainV.findViewById(R.id.my_listview);
		headV = mLayoutInflater.inflate(R.layout.head_home_index, null);
		gallery = (NormalGallery) headV.findViewById(R.id.gallery);
		getConfiglist();

		listV.addHeadView(headV);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().likelist, getActivity(),
				R.layout.item_home_like_list);
		adapter.fromWhat("list");
		adapter.addField("title", R.id.title);
		adapter.addField("tuangoudes", R.id.tuangoudes);
		adapter.addField("pic", R.id.pic);
		adapter.addField(new FieldMap("price", R.id.price) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				JSONObject data = (JSONObject) jo;
				TextView comment_desT = (TextView) itemV
						.findViewById(R.id.comment_des);
				comment_desT.setText(getString(R.string.food_comment_des)
						+ JSONUtil.getString(data, "score") + "/"
						+ JSONUtil.getString(data, "comment"));

				return getString(R.string.money_symbol) + o.toString();
			}
		});

		adapter.addField(new FieldMap("ordercount", R.id.ordercount) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				return getString(R.string.home_selled) + o.toString();
			}
		});

		// adapter.addField(new FieldMap("lng", R.id.distance) {
		//
		// @Override
		// public Object fix(View itemV, Integer position, Object o, Object jo)
		// {
		// return "距离" + o.toString();
		// }
		// });

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

		foodLayoutV = headV.findViewById(R.id.food_layout);
		hotelLayoutV = headV.findViewById(R.id.hotel_layout);
		redpacketLayoutV = headV.findViewById(R.id.redpacket);
		travel_layoutV = headV.findViewById(R.id.travel_layout);
		help_layoutV = headV.findViewById(R.id.help_layout);
		all_catV = headV.findViewById(R.id.all_cat);
		travel_servicesV = headV.findViewById(R.id.travel_services);
		entertainmentV = headV.findViewById(R.id.entertainment);
		exclusive_characteristicsV = headV
				.findViewById(R.id.exclusive_characteristics);
		daigouV = headV.findViewById(R.id.daigou);
		foodLayoutV.setOnClickListener(this);
		hotelLayoutV.setOnClickListener(this);
		redpacketLayoutV.setOnClickListener(this);
		travel_layoutV.setOnClickListener(this);
		help_layoutV.setOnClickListener(this);
		all_catV.setOnClickListener(this);
		travel_servicesV.setOnClickListener(this);
		entertainmentV.setOnClickListener(this);
		exclusive_characteristicsV.setOnClickListener(this);
		daigouV.setOnClickListener(this);
	}

	// 获取首页幻灯+中间内容列表
	private void getConfiglist() {
		DhNet net = new DhNet(new API().configlist);
		net.doGet(new NetTask(getActivity()) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					// 幻灯
					JSONArray jsa1 = response.jSONArrayFromData();
					galleryAdapter = new PSAdapter(getActivity(),
							R.layout.item_gallery);
					galleryAdapter.addField("pic", R.id.pic, "default");
					galleryAdapter.addAll(jsa1);
					gallery.setAdapter(galleryAdapter);
					gallery.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							JSONObject data = (JSONObject) galleryAdapter
									.getTItem(position);
							RabbitUtils.ImgIntent(getActivity(),
									JSONUtil.getInt(data, "type"),
									JSONUtil.getString(data, "id"));

						}
					});

					// 中间部分
					JSONArray jsa2 = response.jSONArrayFrom("data1");
					if (jsa2 != null) {
						ViewUtil.bindNetImage((ImageView) headV
								.findViewById(R.id.pic_0), JSONUtil.getString(
								JSONUtil.getJSONObjectAt(jsa2, 0), "pic"),
								"default");
						((TextView) headV.findViewById(R.id.pic_0_title))
								.setText(JSONUtil.getString(
										JSONUtil.getJSONObjectAt(jsa2, 0),
										"title"));
						for (int i = 0; i < pic_ids.length; i++) {
							ImageView view = (ImageView) headV
									.findViewById(pic_ids[i]);

							final JSONObject jo = JSONUtil.getJSONObjectAt(
									jsa2, i + 1);
							view.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									RabbitUtils.ImgIntent(getActivity(),
											JSONUtil.getInt(jo, "type"),
											JSONUtil.getString(jo, "id"));
								}
							});

							ViewUtil.bindNetImage(view, JSONUtil.getString(
									JSONUtil.getJSONObjectAt(jsa2, i + 1),
									"pic"), "default");
						}
					}
				}
			}
		});
	}

	public void onEventMainThread(CityEB city) {
		TextView cityT = (TextView) mainV.findViewById(R.id.city);
		cityT.setText(city.getCityname());
		adapter.addparam("cityid", city.getCatid());
		adapter.refresh();
	}

	@Override
	public void onClick(View v) {

		Intent it;

		switch (v.getId()) {
		// 美食
		case R.id.food_layout:
			it = new Intent(getActivity(), FoodListActivity.class);
			it.putExtra("title", getString(R.string.meishi));
			it.putExtra("catid", "1");
			startActivity(it);
			break;

		// 出行服务
		case R.id.travel_services:
			it = new Intent(getActivity(), FoodListActivity.class);
			it.putExtra("title", getString(R.string.chuxingfuwu));
			it.putExtra("catid", "4");
			startActivity(it);
			break;

		// 休闲娱乐
		case R.id.entertainment:
			it = new Intent(getActivity(), FoodListActivity.class);
			it.putExtra("title", getString(R.string.xiuxianyule));
			it.putExtra("catid", "6");
			startActivity(it);
			break;

		// 专享特色
		case R.id.exclusive_characteristics:
			it = new Intent(getActivity(), FoodListActivity.class);
			it.putExtra("title", getString(R.string.zhuanxiangtese));
			it.putExtra("catid", "7");
			startActivity(it);
			break;
		// 代购
		case R.id.daigou:
			it = new Intent(getActivity(), DaiGouActivity.class);
			// it.putExtra("title", getString(R.string.huobiduihuan));
			// it.putExtra("catid", "3");
			startActivity(it);
			break;

		case R.id.hotel_layout:
			it = new Intent(getActivity(), HotelListActivity.class);
			startActivity(it);
			break;

		// 随身翻译
		case R.id.redpacket:
			it = new Intent(getActivity(), TranslateActivity.class);
			startActivity(it);
			break;

		// 旅行小秘
		case R.id.travel_layout:
			it = new Intent(getActivity(), TravelActivity.class);
			it.putExtra("title", getString(R.string.lvxingxiaomi));
			it.putExtra("catid", "5");
			startActivity(it);
			break;

		case R.id.help_layout:
			it = new Intent(getActivity(), TravelActivity.class);
			it.putExtra("title", getString(R.string.jinjiqiuzhu));
			it.putExtra("catid", "8");
			startActivity(it);
			break;

		case R.id.all_cat:
			it = new Intent(getActivity(), AllItemActivity.class);
			startActivity(it);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
