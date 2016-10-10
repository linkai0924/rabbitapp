package com.means.rabbit.activity.order.pay;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.RabbitValueFix;
import com.means.rabbit.activity.comment.PostCommentMainActivity;
import com.means.rabbit.activity.main.ErweimaActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;

/**
 * 
 * 酒店订单详情
 * 
 * @author Administrator
 * 
 */
public class HotelOrderDetailActivity extends RabbitBaseActivity {

	TextView titleT, nameT, dateT, signlpirceT, countT, totalpriceT, idT,
			buyernoteT, buyerphoneT, buyernameT, creditT, credit_sT,
			reality_moneyT;

	String itemid;

	Button grogshop_btn;

	public int pay = 1003;

	public int comment = 1004;

	int servicestatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grogshop_pay);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.grogshop_pay_order));

		itemid = getIntent().getStringExtra("orderid");

		titleT = (TextView) findViewById(R.id.title_name);
		nameT = (TextView) findViewById(R.id.name);
		dateT = (TextView) findViewById(R.id.date);
		signlpirceT = (TextView) findViewById(R.id.signlpirce);
		countT = (TextView) findViewById(R.id.count);
		totalpriceT = (TextView) findViewById(R.id.totalprice);
		idT = (TextView) findViewById(R.id.id);
		buyerphoneT = (TextView) findViewById(R.id.buyerphone);
		buyernameT = (TextView) findViewById(R.id.buyername);
		buyernoteT = (TextView) findViewById(R.id.buyernote);
		creditT = (TextView) findViewById(R.id.credit);
		credit_sT = (TextView) findViewById(R.id.credit_s);
		reality_moneyT = (TextView) findViewById(R.id.reality_money);

		grogshop_btn = (Button) findViewById(R.id.grogshop_btn);

		findViewById(R.id.cancle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancleOrder();
			}
		});

		getGrogshopData();
	}

	private void getGrogshopData() {
		DhNet net = new DhNet(new API().hotelOrderDetail);
		net.addParam("orderid", itemid);
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					final JSONObject jo = response.jSONFromData();
					titleT.setText(JSONUtil.getString(jo, "title"));
					nameT.setText(JSONUtil.getString(jo, "buyername"));

					String startdate = RabbitValueFix.getStandardTime(
							JSONUtil.getLong(jo, "startdate"), "yyyy-MM-dd");
					String enddate = RabbitValueFix.getStandardTime(
							JSONUtil.getLong(jo, "enddate"), "yyyy-MM-dd");

					dateT.setText("入住" + startdate + " 离开" + enddate);
					
					
					
					String orderTime = RabbitValueFix.getStandardTime(
							JSONUtil.getLong(jo, "adddateline"),
							"yyyy-MM-dd HH:mm");

					String paytime = RabbitValueFix.getStandardTime(
							JSONUtil.getLong(jo, "actdateline"),
							"yyyy-MM-dd HH:mm");

					ViewUtil.bindView(findViewById(R.id.adddateline), orderTime);
					ViewUtil.bindView(findViewById(R.id.pay_time), paytime);
					ViewUtil.bindView(findViewById(R.id.use_time), paytime);

					signlpirceT.setText(getString(R.string.money_symbol)
							+ JSONUtil.getString(jo, "singleprice"));

					ViewUtil.bindView(
							findViewById(R.id.yingfu),
							getString(R.string.money_symbol)
									+ JSONUtil.getDouble(jo, "orderprice"));

					countT.setText(JSONUtil.getString(jo, "daycount"));
					totalpriceT.setText(getString(R.string.money_symbol)
							+ JSONUtil.getInt(jo, "count")
							* JSONUtil.getDouble(jo, "singleprice"));
					idT.setText(JSONUtil.getString(jo, "code"));
					buyerphoneT.setText(JSONUtil.getString(jo, "buyerphone"));
					buyernameT.setText(JSONUtil.getString(jo, "buyername"));
					buyernoteT.setText(JSONUtil.getString(jo, "buyernote"));
					JSONObject credit_data = JSONUtil.getJSONObject(jo,
							"user_data");
					creditT.setText(JSONUtil.getString(credit_data, "credit"));
					credit_sT.setText(JSONUtil.getString(credit_data,
							"credit_s"));
					ViewUtil.bindView(findViewById(R.id.ercode),
							JSONUtil.getString(jo, "ercode"));

					ViewUtil.bindView(findViewById(R.id.count),
							JSONUtil.getString(jo, "count"));
					ViewUtil.bindView(findViewById(R.id.name),
							JSONUtil.getString(jo, "name"));

					final int paystatus = JSONUtil.getInt(jo, "paystatus");
					servicestatus = JSONUtil.getInt(jo, "servicestatus");
					// grogshop_btn.setText(paystatus == 1 ? "支付订单" : "已支付");

					if (paystatus == 2 && servicestatus == 2
							&& JSONUtil.getInt(jo, "orderstatus") == 2) {
						grogshop_btn.setText(getString(R.string.order_status_complete));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_green_bg);
						findViewById(R.id.cancle).setVisibility(View.GONE);
					} else if (JSONUtil.getInt(jo, "orderstatus") == 3) {
						grogshop_btn.setText(getString(R.string.order_status_cancle));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.GONE);
					} else if (servicestatus == 1
							&& JSONUtil.getInt(jo, "orderstatus") == 2) {
						grogshop_btn.setText(getString(R.string.order_status_release_comment));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.GONE);
					} else if (paystatus == 1) {
						grogshop_btn.setText(getString(R.string.order_status_pay_order));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.VISIBLE);
					} else if (paystatus == 2) {
						grogshop_btn.setText(getString(R.string.order_status_payed));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_green_bg);
						findViewById(R.id.cancle).setVisibility(View.VISIBLE);
					}
					
					

					// if (paystatus == 1) {
					// grogshop_btn.setText("支付订单");
					// } else {
					// if(servicestatus==1&& JSONUtil.getInt(jo, "orderstatus")
					// == 2){
					// grogshop_btn.setText("发布评论");
					// }else if(JSONUtil.getInt(jo, "orderstatus")){
					//
					// }
					//
					// }

					grogshop_btn.setTag(paystatus);
					grogshop_btn
							.setBackgroundResource(paystatus == 1 ? R.drawable.fillet_10_pink_bg
									: R.drawable.fillet_10_green_bg);
					grogshop_btn.setVisibility(View.VISIBLE);
					grogshop_btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							Intent it;

							if (grogshop_btn.getTag().equals(1)) {
								it = new Intent(self, PayOrderActivity.class);
								it.putExtra("payprice",
										JSONUtil.getString(jo, "payprice"));
								it.putExtra("orderid",
										JSONUtil.getString(jo, "id"));
								it.putExtra("name",
										JSONUtil.getString(jo, "title"));
								startActivityForResult(it, pay);
							} else {
								if (servicestatus == 1
										&& JSONUtil.getInt(jo, "orderstatus") == 2) {
									it = new Intent(self,
											PostCommentMainActivity.class);
									it.putExtra("contentid",
											JSONUtil.getString(jo, "contentid"));
									it.putExtra("type", "2");
									startActivityForResult(it, comment);
								}
							}

						}
					});
					reality_moneyT.setText(JSONUtil.getString(jo, "payprice"));

					findViewById(R.id.erweima).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									if (TextUtils.isEmpty(JSONUtil.getString(
											jo, "ercode_img"))) {
										return;
									}

									Intent it = new Intent(self,
											ErweimaActivity.class);
									it.putExtra("url", JSONUtil.getString(jo,
											"ercode_img"));
									startActivity(it);
								}
							});

				}
			}
		});
	}

	private void cancleOrder() {
		DhNet net = new DhNet(new API().cancelOrder);
		net.addParam("orderid", itemid);
		net.doPostInDialog("取消中...", new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					showToast("取消成功!");
					finish();
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == pay && resultCode == Activity.RESULT_OK) {
			grogshop_btn.setText("已支付");
			grogshop_btn.setBackgroundResource(R.drawable.fillet_10_green_bg);
			grogshop_btn.setTag(2);
		}

		if (requestCode == comment && resultCode == Activity.RESULT_OK) {
			grogshop_btn.setText("已评论");
			grogshop_btn.setBackgroundResource(R.drawable.fillet_10_green_bg);
			grogshop_btn.setTag(2);
			servicestatus = 2;
		}

	}

}
