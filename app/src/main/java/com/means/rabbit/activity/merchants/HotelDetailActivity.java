package com.means.rabbit.activity.merchants;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.adapter.PSAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.activity.main.ErweimaActivity;
import com.means.rabbit.activity.main.MapActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitUtils;
import com.means.rabbit.views.CommentView;
import com.means.rabbit.views.HotelYudingView;
import com.means.rabbit.views.NomalGallery;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.ShopDetailTuangouView;

public class HotelDetailActivity extends RabbitBaseActivity {
	View headV;

	HotelYudingView hotelYudingView;

	ShopDetailTuangouView shopDetailTuangouView;

	CommentView commentView;

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;
	String hotelId;

	NomalGallery gallery;

	PSAdapter galleryAdapter;

	// 图片数量
	TextView pic_countT;

	TextView priceT;

	RatingBar ratingBar;

	TextView startDateT, endDateT;

	String score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_detail);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.hotel_detail));
		Intent lastIntent = getIntent();
		hotelId = lastIntent.getStringExtra("hotelId");
		headV = LayoutInflater.from(self).inflate(R.layout.head_hotel_detail,
				null);

		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		listV.addHeadView(headV);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().hotelDetailNearTuangou, self,
				R.layout.item_shop_detail_tuangou_near);
		adapter.fromWhat("list");
		adapter.addparam("contentid", hotelId);
		adapter.addparam("type", 2);
		adapter.addField("pic", R.id.pic);
		adapter.addField("title", R.id.title);
		adapter.addField("oldprice", R.id.oldprice);
		adapter.addField("ordercount", R.id.sell_count);
		adapter.addField(new FieldMap("price", R.id.price) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				return getString(R.string.money_symbol) + o
						+ getString(R.string.hotel_people);
			}
		});

		listV.setAdapter(adapter);
		contentListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				JSONObject jo = adapter.getTItem(position
						- contentListV.getHeaderViewsCount());
				Intent it = new Intent(self, TuangouDetailActivity.class);
				it.putExtra("tuangouId", JSONUtil.getString(jo, "id"));
				startActivity(it);

			}
		});

		hotelYudingView = (HotelYudingView) headV
				.findViewById(R.id.yuding_view);

		shopDetailTuangouView = (ShopDetailTuangouView) headV
				.findViewById(R.id.tuangou_view);

		commentView = (CommentView) headV.findViewById(R.id.comment_view);
		gallery = (NomalGallery) headV.findViewById(R.id.gallery);
		pic_countT = (TextView) headV.findViewById(R.id.pic_count);
		priceT = (TextView) headV.findViewById(R.id.price);
		ratingBar = (RatingBar) headV.findViewById(R.id.ratingbar);

		startDateT = (TextView) headV.findViewById(R.id.start_date);
		endDateT = (TextView) headV.findViewById(R.id.end_date);

		startDateT.setText(lastIntent.getStringExtra("startDate"));
		endDateT.setText(lastIntent.getStringExtra("endDate"));

		gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				pic_countT.setText(position + 1 + "/" + gallery.getCount());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		getHotelDetalData();
		getOrderList();
		getTuangouList();
		getCommentList();
	}

	// 获取详情数据
	private void getHotelDetalData() {
		DhNet net = new DhNet(new API().hoteldetail);
		net.addParam("key", getIntent().getStringExtra("key"));
		net.addParam("id", hotelId);
		net.doGet(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					final JSONObject detailJo = response.jSONFromData();

					setRightAction(R.drawable.erweima, new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent it = new Intent(self, ErweimaActivity.class);
							it.putExtra("url",
									JSONUtil.getString(detailJo, "pic_qr"));
							startActivity(it);
						}
					});

					setRightAction2(
							JSONUtil.getBoolean(detailJo, "is_collect"),
							JSONUtil.getString(detailJo, "id"), "3");

					JSONArray image_data = JSONUtil.getJSONArray(detailJo,
							"image_data");
					galleryAdapter = new PSAdapter(self, R.layout.item_gallery);
					galleryAdapter.addField("img_m", R.id.pic, "default");
					galleryAdapter.addAll(image_data);
					gallery.setAdapter(galleryAdapter);

					priceT.setText(JSONUtil.getString(detailJo, "price")
							+ getString(R.string.hotel_price_des));

					ViewUtil.bindView(headV.findViewById(R.id.title),
							JSONUtil.getString(detailJo, "title"));
					ViewUtil.bindView(headV.findViewById(R.id.address),
							JSONUtil.getString(detailJo, "address"));

					ViewUtil.bindView(headV.findViewById(R.id.tel),
							JSONUtil.getString(detailJo, "lxphone"));

					findViewById(R.id.tel_layout).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									RabbitUtils.call(self, JSONUtil.getString(
											detailJo, "lxphone"));
								}
							});

					// JSONArray label_inJsa = JSONUtil.getJSONArray(detailJo,
					// "label_in");
					// if (label_inJsa != null && label_inJsa.length() != 0) {
					// String labelS = "";
					//
					// for (int i = 0; i < label_inJsa.length(); i++) {
					// try {
					// JSONObject labeljo = label_inJsa
					// .getJSONObject(i);
					// labelS = labelS + " "
					//
					// + JSONUtil.getString(labeljo, "name");
					// ViewUtil.bindView(
					// headV.findViewById(R.id.label), labelS);
					// } catch (JSONException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
					//
					// }

					ViewUtil.bindView(headV.findViewById(R.id.label),
							JSONUtil.getString(detailJo, "stitle"));

					ViewUtil.bindView(headV.findViewById(R.id.comment_des),
							JSONUtil.getString(detailJo, "score") + "/"
									+ JSONUtil.getString(detailJo, "comment"));
					score = JSONUtil.getString(detailJo, "score");
					ratingBar.setRating(JSONUtil.getFloat(detailJo, "score"));

					headV.findViewById(R.id.address_layout).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									String url = "http://cn.lazybunny.c.wanruankeji.com/home/index/mapnav?tolng="
											+ JSONUtil
													.getFloat(detailJo, "lng")
											+ "&tolat="
											+ JSONUtil
													.getFloat(detailJo, "lat");

									Intent it = new Intent(self,
											MapActivity.class);
									it.putExtra("url", url);
									it.putExtra("tolat",
											JSONUtil.getFloat(detailJo, "lat"));
									it.putExtra("tolng",
											JSONUtil.getFloat(detailJo, "lng"));
									startActivity(it);

								}
							});

				}

			}
		});
	}

	// 获取预订列表
	private void getOrderList() {
		DhNet net = new DhNet(new API().hotelDetailOrderList);
		net.addParam("contentid", hotelId);
		net.doGet(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {

					TextView titleT = (TextView) headV.findViewById(R.id.title);
					hotelYudingView.setDate(startDateT.getText().toString(),
							endDateT.getText().toString());
					hotelYudingView.setData(response.jSONArrayFrom("list"),
							titleT.getText().toString());
				}

			}
		});
	}

	// 获取团购数据
	private void getTuangouList() {
		DhNet net = new DhNet(new API().hotelDetailNearTuangou);
		net.addParam("contentid", hotelId);
		net.doGet(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					shopDetailTuangouView.setData(response
							.jSONArrayFrom("list"));
				}

			}
		});
	}

	// 评论列表
	private void getCommentList() {
		DhNet net = new DhNet(new API().commentlist);
		net.addParam("contentid", hotelId);
		net.addParam("type", 2);
		net.addParam("step", 2);
		net.doGet(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					commentView.setData(response.jSONArrayFrom("list"), score);
				}

			}
		});
	}
}
