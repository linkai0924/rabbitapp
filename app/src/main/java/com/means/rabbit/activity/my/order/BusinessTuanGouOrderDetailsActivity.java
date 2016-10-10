package com.means.rabbit.activity.my.order;

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
import com.means.rabbit.activity.main.MainActivity;
import com.means.rabbit.activity.order.pay.GroupPayActivity;
import com.means.rabbit.activity.order.pay.HotelOrderDetailActivity;
import com.means.rabbit.activity.order.pay.InsteadShoppingPayActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitUtils;

/**
 * 商家订单详情
 * 
 * @author dell
 * 
 */
public class BusinessTuanGouOrderDetailsActivity extends RabbitBaseActivity {

	String orderid;

	Double credit_s;

	TextView shifuT;

	Button payB;

	EditText errcodeE;

	public final int REQUEST_CODE = 10086;
	int servicestatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_order_details);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.grouporder));
		orderid = getIntent().getStringExtra("orderid");
		errcodeE = (EditText) findViewById(R.id.erweima_code);
		payB = (Button) findViewById(R.id.pay);
		findViewById(R.id.cancle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancleOrder();
			}
		});
		getData();
	}

	private void getData() {
		DhNet net = new DhNet(new API().orderbusinessdetail);
		net.addParam("orderid", orderid);
		net.doGetInDialog(getString(R.string.progress_doing),
				new NetTask(self) {

					@Override
					public void doInUI(Response response, Integer transfer) {

						if (response.isSuccess()) {

							final JSONObject jo = response.jSONFromData();
							ViewUtil.bindView(findViewById(R.id.name),
									JSONUtil.getString(jo, "title"));

							ViewUtil.bindView(findViewById(R.id.price),
									JSONUtil.getString(jo, "singleprice"));
							ViewUtil.bindView(findViewById(R.id.code),
									JSONUtil.getString(jo, "code"));

							ViewUtil.bindView(
									findViewById(R.id.total_price),
									getString(R.string.money_symbol)
											+ JSONUtil.getDouble(jo,
													"orderprice"));
							ViewUtil.bindView(findViewById(R.id.count),
									JSONUtil.getString(jo, "count"));

							String orderTime = RabbitValueFix.getStandardTime(
									JSONUtil.getLong(jo, "adddateline"),
									"yyyy-MM-dd");
							String paytime = RabbitValueFix.getStandardTime(
									JSONUtil.getLong(jo, "actdateline"),
									"yyyy-MM-dd HH:mm");
							ViewUtil.bindView(findViewById(R.id.order_time),
									orderTime);


							ViewUtil.bindView(findViewById(R.id.pay_time), paytime);
							ViewUtil.bindView(findViewById(R.id.use_time), paytime);
							
							
							
							ViewUtil.bindView(findViewById(R.id.tel),
									JSONUtil.getString(jo, "buyerphone"));

							JSONObject credit_dataJo = JSONUtil.getJSONObject(
									jo, "user_data");

							ViewUtil.bindView(findViewById(R.id.credit),
									JSONUtil.getString(credit_dataJo, "credit"));

							credit_s = JSONUtil.getDouble(credit_dataJo,
									"credit_s");
							ViewUtil.bindView(findViewById(R.id.credit_s),
									getString(R.string.money_symbol) + credit_s);
							// ViewUtil.bindView(findViewById(R.id.ercode),
							// JSONUtil.getString(jo, "ercode"));

							ViewUtil.bindView(findViewById(R.id.shifu),
									JSONUtil.getDouble(jo, "payprice") + "");

							final int paystatus = JSONUtil.getInt(jo,
									"paystatus");
							servicestatus = JSONUtil
									.getInt(jo, "servicestatus");
							payB.setTag(paystatus);
							payB.setBackgroundResource(paystatus == 1 ? R.drawable.fillet_10_pink_bg
									: R.drawable.fillet_10_green_bg);

							if (paystatus == 2 && servicestatus == 2
									&& JSONUtil.getInt(jo, "orderstatus") == 2) {
								payB.setText(getString(R.string.order_status_complete));
								payB.setBackgroundResource(R.drawable.fillet_10_green_bg);
								findViewById(R.id.cancle).setVisibility(
										View.GONE);
								errcodeE.setEnabled(false);
								errcodeE.setHint("");
							} else if (JSONUtil.getInt(jo, "orderstatus") == 3) {
								payB.setText(getString(R.string.order_status_cancle));
								payB.setBackgroundResource(R.drawable.fillet_10_pink_bg);
								findViewById(R.id.cancle).setVisibility(
										View.GONE);
								errcodeE.setEnabled(false);
								errcodeE.setHint("");
							} else if (servicestatus == 1
									&& JSONUtil.getInt(jo, "orderstatus") == 2) {
								payB.setText(getString(R.string.order_status_release_comment_des));
								payB.setBackgroundResource(R.drawable.fillet_10_pink_bg);
								findViewById(R.id.cancle).setVisibility(
										View.GONE);
								errcodeE.setEnabled(false);
								errcodeE.setHint("");
							} else if (paystatus == 1) {
								payB.setText(getString(R.string.order_status_pay_des));
								payB.setBackgroundResource(R.drawable.fillet_10_pink_bg);
								findViewById(R.id.cancle).setVisibility(
										View.VISIBLE);
								errcodeE.setEnabled(false);
								errcodeE.setHint("");
							} else if (paystatus == 2) {
								payB.setText(getString(R.string.business_order_des));
								payB.setBackgroundResource(R.drawable.fillet_10_pink_bg);
								findViewById(R.id.cancle).setVisibility(
										View.VISIBLE);
								errcodeE.setEnabled(true);
							}

							// payB.setBackgroundResource(paystatus == 1 ?
							// R.drawable.fillet_10_pink_bg
							// : R.drawable.fillet_10_green_bg);
							// payB.setText(paystatus == 1 ?
							// getString(R.string.business_order_des)
							// : getString(R.string.business_order_des1));
							payB.setVisibility(View.VISIBLE);
							payB.setTag(paystatus);
							payB.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (payB.getText()
											.toString()
											.equals(getString(R.string.business_order_des))) {
										usecode();
									}

								}
							});

							findViewById(R.id.erweima).setOnClickListener(
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											callCapture();
											// if
											// (TextUtils.isEmpty(JSONUtil.getString(
											// jo, "ercode_img"))) {
											// return;
											// }
											//
											// Intent it = new Intent(self,
											// ErweimaActivity.class);
											// it.putExtra("url",
											// JSONUtil.getString(jo,
											// "ercode_img"));
											// startActivity(it);
										}
									});

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
		net.addParam("orderid", orderid);
		net.addParam("ercode", errcodeE.getText().toString());
		net.doPostInDialog(getString(R.string.submiting), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					payB.setText(getString(R.string.order_status_release_comment_des));
					payB.setBackgroundResource(R.drawable.fillet_10_pink_bg);
					payB.setTag(2);
					findViewById(R.id.cancle).setVisibility(View.GONE);
					errcodeE.setEnabled(false);
					errcodeE.setHint("");

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

	private void cancleOrder() {
		DhNet net = new DhNet(new API().cancelOrder);
		net.addParam("orderid", orderid);
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
}
