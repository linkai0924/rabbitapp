package com.means.rabbit.activity.merchants;

import org.json.JSONArray;
import org.json.JSONObject;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.adapter.PSAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.means.rabbit.R;
import com.means.rabbit.activity.main.ErweimaActivity;
import com.means.rabbit.activity.main.MapActivity;
import com.means.rabbit.activity.order.GroupOrderActivity;
import com.means.rabbit.activity.order.InsteadShoppingActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.manage.UserInfoManage;
import com.means.rabbit.manage.UserInfoManage.LoginCallBack;
import com.means.rabbit.views.CommentView;
import com.means.rabbit.views.KeyVauleView;
import com.means.rabbit.views.NomalGallery;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.ShopDetailTuangouView;

public class TuangouDetailActivity extends RabbitBaseActivity {

	View headV;

	KeyVauleView keyValueView;

	CommentView commentView;

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;

	String tuangouId;

	RatingBar ratingBar;

	NomalGallery gallery;

	PSAdapter galleryAdapter;

	// 立即购买按钮
	TextView bugT;

	String score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuangou_detail);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.tuangou_detail));
		tuangouId = getIntent().getStringExtra("tuangouId");
		headV = LayoutInflater.from(self).inflate(R.layout.head_tuangou_detail,
				null);

		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		listV.addHeadView(headV);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().hotelDetailNearTuangou, self,
				R.layout.item_shop_detail_tuangou_near);
		adapter.fromWhat("list");
		adapter.addparam("contentid", tuangouId);
		adapter.addparam("type", 1);
		adapter.addField("pic", R.id.pic);
		adapter.addField("title", R.id.title);
		adapter.addField("oldprice", R.id.oldprice);
		adapter.addField("ordercount", R.id.sell_count);
		adapter.addField(new FieldMap("price", R.id.price) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				return getString(R.string.money_symbol) + o + "/人";
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

		keyValueView = (KeyVauleView) headV.findViewById(R.id.keyvule_view);

		commentView = (CommentView) headV.findViewById(R.id.comment_view);
		gallery = (NomalGallery) headV.findViewById(R.id.gallery);
		ratingBar = (RatingBar) headV.findViewById(R.id.ratingbar);
		bugT = (TextView) headV.findViewById(R.id.bug);
		bugT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				UserInfoManage.getInstance().checkLogin(self,
						new LoginCallBack() {

							@Override
							public void onisLogin() {
								Intent it = new Intent(self,
										GroupOrderActivity.class);
								it.putExtra("tuangouId", tuangouId);
								startActivity(it);

							}

							@Override
							public void onLoginFail() {
								// TODO Auto-generated method stub

							}
						});

			}
		});
		getShopDetalData();
		getCommentList();
	}

	private void getShopDetalData() {
		DhNet net = new DhNet(new API().tuangouDetail);
		net.addParam("key", getIntent().getStringExtra("key"));
		net.addParam("id", tuangouId);
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
							JSONUtil.getString(detailJo, "id"), "4");

					JSONArray image_data = JSONUtil.getJSONArray(detailJo,
							"image_data");
					galleryAdapter = new PSAdapter(self, R.layout.item_gallery);
					galleryAdapter.addField("img_m", R.id.pic, "default");
					galleryAdapter.addAll(image_data);

					gallery.setAdapter(galleryAdapter);

					ViewUtil.bindView(headV.findViewById(R.id.title),
							JSONUtil.getString(detailJo, "title"));

					ViewUtil.bindView(headV.findViewById(R.id.address),
							JSONUtil.getString(detailJo, "address"));
					ViewUtil.bindView(headV.findViewById(R.id.name),
							JSONUtil.getString(detailJo, "shoptitle"));

					ViewUtil.bindView(headV.findViewById(R.id.comment_des),
							JSONUtil.getString(detailJo, "score") + "/"
									+ JSONUtil.getString(detailJo, "comment"));

					score = JSONUtil.getString(detailJo, "score");
					ViewUtil.bindView(
							headV.findViewById(R.id.price),
							getString(R.string.money_symbol)
									+ JSONUtil.getString(detailJo, "price"));

					ViewUtil.bindView(
							headV.findViewById(R.id.old_price),
							getString(R.string.money_symbol)
									+ JSONUtil.getString(detailJo, "oldprice"));
					TextView goumaidesT = (TextView) findViewById(R.id.goumaides);
					goumaidesT.setText(Html.fromHtml(JSONUtil.getString(
							detailJo, "goumaides")));
					ratingBar.setRating(JSONUtil.getFloat(detailJo, "score"));

					JSONArray field_dataJSA = JSONUtil.getJSONArray(detailJo,
							"field_data");
					keyValueView.setData(field_dataJSA,
							JSONUtil.getString(detailJo, "content"));

					ViewUtil.bindView(headV.findViewById(R.id.count),
							JSONUtil.getString(detailJo, "ordercount")
									+ getString(R.string.buy));

					headV.findViewById(R.id.address_layout).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									Intent it = new Intent(self,
											MapActivity.class);
									it.putExtra("url", JSONUtil.getString(
											detailJo, "map_url"));
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

	private void getCommentList() {
		DhNet net = new DhNet(new API().commentlist);
		net.addParam("contentid", tuangouId);
		net.addParam("type", 1);
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
