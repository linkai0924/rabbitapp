package com.means.rabbit.activity.order;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.means.rabbit.R;
import com.means.rabbit.RabbitValueFix;
import com.means.rabbit.activity.comment.PostCommentMainActivity;
import com.means.rabbit.activity.main.ErweimaActivity;
import com.means.rabbit.activity.order.pay.PayOrderActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitUtils;

public class BusinessHotelOrderDetailActivity extends RabbitBaseActivity {

	TextView titleT, nameT, dateT, signlpirceT, countT, totalpriceT, idT,
			buyernoteT, buyerphoneT, buyernameT, creditT, credit_sT,
			reality_moneyT;

	String itemid;

	Button grogshop_btn;

	public int pay = 1003;

	public int comment = 1004;

	int servicestatus;

	EditText errcodeE;

	public final int REQUEST_CODE = 10086;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_b_hotel_order_detail);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.grogshop_pay_order));
		errcodeE = (EditText) findViewById(R.id.erweima_code);
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

					// final int paystatus = JSONUtil.getInt(jo, "paystatus");
					// grogshop_btn.setText(paystatus == 1 ? "支付订单" : "已支付");
					
					
					

					String orderTime = RabbitValueFix.getStandardTime(
							JSONUtil.getLong(jo, "adddateline"),
							"yyyy-MM-dd HH:mm");

					String paytime = RabbitValueFix.getStandardTime(
							JSONUtil.getLong(jo, "actdateline"),
							"yyyy-MM-dd HH:mm");

					ViewUtil.bindView(findViewById(R.id.adddateline), orderTime);
					ViewUtil.bindView(findViewById(R.id.pay_time), paytime);
					ViewUtil.bindView(findViewById(R.id.use_time), paytime);
					

					final int paystatus = JSONUtil.getInt(jo, "paystatus");
					servicestatus = JSONUtil.getInt(jo, "servicestatus");
					grogshop_btn.setTag(paystatus);
					grogshop_btn
							.setBackgroundResource(paystatus == 1 ? R.drawable.fillet_10_pink_bg
									: R.drawable.fillet_10_green_bg);

					if (paystatus == 2 && servicestatus == 2
							&& JSONUtil.getInt(jo, "orderstatus") == 2) {
						grogshop_btn
								.setText(getString(R.string.order_status_complete));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_green_bg);
						findViewById(R.id.cancle).setVisibility(View.GONE);
						errcodeE.setEnabled(false);
						errcodeE.setHint("");

					} else if (JSONUtil.getInt(jo, "orderstatus") == 3) {
						grogshop_btn
								.setText(getString(R.string.order_status_cancle));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.GONE);
						errcodeE.setEnabled(false);
						errcodeE.setHint("");
					} else if (servicestatus == 1
							&& JSONUtil.getInt(jo, "orderstatus") == 2) {
						grogshop_btn
								.setText(getString(R.string.order_status_release_comment_des));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.GONE);
						errcodeE.setEnabled(false);
						errcodeE.setHint("");
					} else if (paystatus == 1) {
						grogshop_btn
								.setText(getString(R.string.order_status_pay_des));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.VISIBLE);
						errcodeE.setEnabled(false);
						errcodeE.setHint("");
					} else if (paystatus == 2) {
						grogshop_btn
								.setText(getString(R.string.business_order_des));
						grogshop_btn
								.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.VISIBLE);
						errcodeE.setEnabled(true);
					}
					grogshop_btn.setVisibility(View.VISIBLE);
					grogshop_btn.setTag(paystatus);
					grogshop_btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (grogshop_btn
									.getText()
									.toString()
									.equals(getString(R.string.business_order_des))) {
								usecode();
							}

						}
					});

					reality_moneyT.setText(JSONUtil.getString(jo, "payprice"));

					findViewById(R.id.erweima).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {

									callCapture();
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

	private void usecode() {
		if (TextUtils.isEmpty(errcodeE.getText().toString())) {
			showToast(getString(R.string.business_order_des3));
			return;
		}
		DhNet net = new DhNet(new API().usecode);
		net.addParam("orderid", itemid);
		net.addParam("ercode", errcodeE.getText().toString());
		net.doPostInDialog(getString(R.string.submiting), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {

					grogshop_btn
							.setText(getString(R.string.order_status_release_comment_des));
					grogshop_btn
							.setBackgroundResource(R.drawable.fillet_10_pink_bg);
					grogshop_btn.setTag(2);
					findViewById(R.id.cancle).setVisibility(View.GONE);
					errcodeE.setEnabled(false);
					errcodeE.setHint("");
					// JSONObject jo = response.jSON();
					// Intent it;
					// int type = JSONUtil.getInt(jo, "type");
					// if (type == 2) {
					// it = new Intent(self, GroupPayActivity.class);
					//
					// } else if (type == 1) {
					// it = new Intent(self, HotelOrderDetailActivity.class);
					// } else {
					// it = new Intent(self, InsteadShoppingPayActivity.class);
					// }
					// it.putExtra("orderid", JSONUtil.getString(jo, "id"));
					// self.startActivity(it);

				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != data && requestCode == REQUEST_CODE) {
			switch (resultCode) {
			case Activity.RESULT_OK:

				String result = data.getStringExtra(Intents.Scan.RESULT);
				try {
					JSONObject jo = new JSONObject(result);
					// if (JSONUtil.getString(jo, "type").equals("order")) {
					// errcodeE.setText(JSONUtil.getString(jo, "key"));
					// usecode();
					// } else {
					RabbitUtils.erweimaIntent(self,
							JSONUtil.getString(jo, "type"),
							JSONUtil.getString(jo, "id"),
							JSONUtil.getString(jo, "key"));
					// }

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			default:
				break;
			}
		}
	}

	private void callCapture() {
		Intent intent = new Intent();
		intent.setAction(Intents.Scan.ACTION);
		// intent.putExtra(Intents.Scan.MODE, Intents.Scan.QR_CODE_MODE);
		intent.putExtra(Intents.Scan.CHARACTER_SET, "UTF-8");
		intent.putExtra(Intents.Scan.WIDTH, 600);
		intent.putExtra(Intents.Scan.HEIGHT, 600);
		// intent.putExtra(Intents.Scan.PROMPT_MESSAGE,
		// "type your prompt message");
		intent.setClass(this, CaptureActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}

}
