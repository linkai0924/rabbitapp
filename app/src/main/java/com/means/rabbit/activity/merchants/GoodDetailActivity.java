package com.means.rabbit.activity.merchants;

import org.json.JSONArray;
import org.json.JSONObject;

import net.duohuo.dhroid.adapter.PSAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;
import android.widget.TextView;

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

/**
 * 
 * 代购详情
 * 
 * @author Administrator
 * 
 */

public class GoodDetailActivity extends RabbitBaseActivity {
	KeyVauleView keyValueView;

	CommentView commentView;

	String daigouId;

	NomalGallery gallery;

	PSAdapter galleryAdapter;

	RatingBar ratingBar;

	String score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gooddetail);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.good_detail));
		daigouId = getIntent().getStringExtra("daigouId");
		keyValueView = (KeyVauleView) findViewById(R.id.keyvule_view);
		commentView = (CommentView) findViewById(R.id.comment_view);
		gallery = (NomalGallery) findViewById(R.id.gallery);
		ratingBar = (RatingBar) findViewById(R.id.ratingbar);
		findViewById(R.id.buy).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UserInfoManage.getInstance().checkLogin(self,
						new LoginCallBack() {

							@Override
							public void onisLogin() {
								Intent it = new Intent(self,
										InsteadShoppingActivity.class);
								it.putExtra("daigouId", daigouId);
								startActivity(it);

							}

							@Override
							public void onLoginFail() {
								// TODO Auto-generated method stub

							}
						});

			}
		});

		getData();
		getCommentList();
	}

	private void getData() {
		DhNet net = new DhNet(new API().daigouDetail);
		net.addParam("key", getIntent().getStringExtra("key"));
		net.addParam("id", daigouId);
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
							JSONUtil.getString(detailJo, "id"), "5");

					JSONArray image_data = JSONUtil.getJSONArray(detailJo,
							"image_data");
					galleryAdapter = new PSAdapter(self, R.layout.item_gallery);
					galleryAdapter.addField("img_m", R.id.pic, "default");
					galleryAdapter.addAll(image_data);
					gallery.setAdapter(galleryAdapter);
					ratingBar.setRating(JSONUtil.getFloat(detailJo, "score"));
					JSONArray field_dataJSA = JSONUtil.getJSONArray(detailJo,
							"field_data");
					keyValueView.setData(field_dataJSA,
							JSONUtil.getString(detailJo, "content"));

					ViewUtil.bindView(findViewById(R.id.good_title),
							JSONUtil.getString(detailJo, "title"));

					ViewUtil.bindView(findViewById(R.id.address),
							JSONUtil.getString(detailJo, "address"));
					ViewUtil.bindView(findViewById(R.id.name),
							JSONUtil.getString(detailJo, "shoptitle"));

					ViewUtil.bindView(findViewById(R.id.comment_des),
							JSONUtil.getString(detailJo, "score") + "/"
									+ JSONUtil.getString(detailJo, "comment"));

					score = JSONUtil.getString(detailJo, "score");
					ViewUtil.bindView(
							findViewById(R.id.price),
							getString(R.string.money_symbol)
									+ JSONUtil.getString(detailJo, "price"));
					TextView goumaidesT = (TextView) findViewById(R.id.goumaides);
					goumaidesT.setText(Html.fromHtml(JSONUtil.getString(
							detailJo, "goumaides")));

					ViewUtil.bindView(
							findViewById(R.id.old_price),
							getString(R.string.money_symbol)
									+ JSONUtil.getString(detailJo, "oldprice"));

					findViewById(R.id.address_layout).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									// Intent it = new Intent(self,
									// MapActivity.class);
									// it.putExtra("url", JSONUtil.getString(
									// detailJo, "map_url"));
									// it.putExtra("tolat",
									// JSONUtil.getFloat(detailJo, "lat"));
									// it.putExtra("tolng",
									// JSONUtil.getFloat(detailJo, "lng"));
									// startActivity(it);

								}
							});

				}

			}
		});
	}

	private void getCommentList() {
		DhNet net = new DhNet(new API().commentlist);
		net.addParam("contentid", daigouId);
		net.addParam("type", 3);
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
