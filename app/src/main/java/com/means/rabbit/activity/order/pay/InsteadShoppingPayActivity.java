package com.means.rabbit.activity.order.pay;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import com.means.rabbit.R;
import com.means.rabbit.RabbitValueFix;
import com.means.rabbit.R.layout;
import com.means.rabbit.activity.comment.PostCommentMainActivity;
import com.means.rabbit.activity.main.ErweimaActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * 代购支付
 * 
 * @author Administrator
 * 
 */
public class InsteadShoppingPayActivity extends RabbitBaseActivity {
	String daigouId;

	Button payB;

	Double credit_s;
	public int pay = 1003;
	TextView shifuT;
	public int comment = 1004;
	int servicestatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instead_shopping_pay);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.insteadshopping));
		daigouId = getIntent().getStringExtra("orderid");
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
		DhNet net = new DhNet(new API().daigouOrderDetail);
		net.addParam("orderid", daigouId);
		net.doGetInDialog(new NetTask(self) {

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
									+ JSONUtil.getDouble(jo, "orderprice"));
					ViewUtil.bindView(findViewById(R.id.count),
							JSONUtil.getString(jo, "count"));

					String orderTime = RabbitValueFix.getStandardTime(
							JSONUtil.getLong(jo, "adddateline"), "yyyy-MM-dd");

					String paytime = RabbitValueFix.getStandardTime(
							JSONUtil.getLong(jo, "actdateline"),
							"yyyy-MM-dd HH:mm");

					ViewUtil.bindView(findViewById(R.id.adddateline), orderTime);
					ViewUtil.bindView(findViewById(R.id.pay_time), paytime);
					ViewUtil.bindView(findViewById(R.id.use_time), paytime);

					ViewUtil.bindView(findViewById(R.id.buyerphone),
							JSONUtil.getString(jo, "buyerphone"));

					ViewUtil.bindView(findViewById(R.id.buyername),
							JSONUtil.getString(jo, "buyername"));

					ViewUtil.bindView(findViewById(R.id.buyernote),
							JSONUtil.getString(jo, "buyernote"));

					JSONObject credit_dataJo = JSONUtil.getJSONObject(jo,
							"user_data");

					ViewUtil.bindView(findViewById(R.id.credit),
							JSONUtil.getString(credit_dataJo, "credit"));

					credit_s = JSONUtil.getDouble(credit_dataJo, "credit_s");
					ViewUtil.bindView(findViewById(R.id.credit_s),
							getString(R.string.money_symbol) + credit_s);
					ViewUtil.bindView(findViewById(R.id.ercode),
							JSONUtil.getString(jo, "ercode"));

					final int paystatus = JSONUtil.getInt(jo, "paystatus");
					servicestatus = JSONUtil.getInt(jo, "servicestatus");
					payB.setTag(paystatus);
					payB.setBackgroundResource(paystatus == 1 ? R.drawable.fillet_10_pink_bg
							: R.drawable.fillet_10_green_bg);
					// payB.setText(paystatus == 1 ? "支付订单" : "已支付");

					if (paystatus == 2 && servicestatus == 2
							&& JSONUtil.getInt(jo, "orderstatus") == 2) {
						payB.setText(getString(R.string.order_status_complete));
						payB.setBackgroundResource(R.drawable.fillet_10_green_bg);
						findViewById(R.id.cancle).setVisibility(View.GONE);
					} else if (JSONUtil.getInt(jo, "orderstatus") == 3) {
						payB.setText(getString(R.string.order_status_cancle));
						payB.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.GONE);
					} else if (servicestatus == 1
							&& JSONUtil.getInt(jo, "orderstatus") == 2) {
						payB.setText(getString(R.string.order_status_release_comment));
						payB.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.GONE);
					} else if (paystatus == 1) {
						payB.setText(getString(R.string.order_status_pay_order));
						payB.setBackgroundResource(R.drawable.fillet_10_pink_bg);
						findViewById(R.id.cancle).setVisibility(View.VISIBLE);
					} else if (paystatus == 2) {
						payB.setText(getString(R.string.order_status_payed));
						payB.setBackgroundResource(R.drawable.fillet_10_green_bg);
						findViewById(R.id.cancle).setVisibility(View.VISIBLE);
					}
					payB.setVisibility(View.VISIBLE);
					payB.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it;
							if (payB.getTag().equals(1)) {
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
									it.putExtra("type", "3");
									startActivityForResult(it, comment);
								}
							}

						}
					});

					JSONObject user_addressJo = JSONUtil.getJSONObject(jo,
							"user_address");

					ViewUtil.bindView(findViewById(R.id.add_username),
							JSONUtil.getString(user_addressJo, "lxname"));
					ViewUtil.bindView(findViewById(R.id.add_tel),
							JSONUtil.getString(user_addressJo, "lxphone"));
					ViewUtil.bindView(
							findViewById(R.id.add_address),
							JSONUtil.getString(user_addressJo, "areaname")
									+ " "
									+ JSONUtil.getString(user_addressJo,
											"zipcode"));

					ViewUtil.bindView(findViewById(R.id.shifu),
							JSONUtil.getString(jo, "payprice"));

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

					// reality_moneyT.setText((JSONUtil.getDouble(jo,
					// "totalprice")-JSONUtil.getDouble(user_data, "credit_s"))
					// + "");
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == pay && resultCode == Activity.RESULT_OK) {
			payB.setText("已支付");
			payB.setBackgroundResource(R.drawable.fillet_10_green_bg);
			payB.setTag(2);
		}

		if (requestCode == comment && resultCode == Activity.RESULT_OK) {
			payB.setText("已评论");
			payB.setBackgroundResource(R.drawable.fillet_10_green_bg);
			payB.setTag(2);
			servicestatus = 2;
		}
	}

	private void cancleOrder() {
		DhNet net = new DhNet(new API().cancelOrder);
		net.addParam("orderid", daigouId);
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
