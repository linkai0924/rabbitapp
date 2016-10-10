package com.means.rabbit.activity.order;

import java.text.ParseException;

import org.json.JSONObject;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import com.means.rabbit.R;
import com.means.rabbit.R.layout;
import com.means.rabbit.activity.order.pay.HotelOrderDetailActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitUtils;
import com.means.rabbit.views.CartView;
import com.means.rabbit.views.CartView.OnCartViewClickListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * 酒店订单
 * 
 * @author Administrator
 * 
 */
public class AddShopOrderActivity extends RabbitBaseActivity {

	String startDate, endDate;

	EditText msgE, telE, usernameE;

	CartView cartView;

	String id;

	TextView totalPriceT;

	Double price;

	int credit;

	TextView shifuT;

	EditText jifenE;

	TextView daikouT;

	// 积分比例
	float creditY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grogshop_order);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.grogshop_pay_order));
		Intent it = getIntent();
		cartView = (CartView) findViewById(R.id.cartView);
		msgE = (EditText) findViewById(R.id.msg);
		telE = (EditText) findViewById(R.id.tel);
		usernameE = (EditText) findViewById(R.id.username);
		totalPriceT = (TextView) findViewById(R.id.total_price);
		jifenE = (EditText) findViewById(R.id.jifen);
		daikouT = (TextView) findViewById(R.id.dikou);
		shifuT = (TextView) findViewById(R.id.shifu);
		jifenE.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (creditY != 0) {
					if (!TextUtils.isEmpty(jifenE.getText().toString())) {
						int jifen = Integer.parseInt(jifenE.getText()
								.toString());
						if (jifen > credit) {
							showToast(getString(R.string.favorable_des1)
									+ credit
									+ getString(R.string.favorable_num));
							jifenE.setText(0 + "");
						} else {
							float daikou = jifen / creditY;
							if (daikou > price) {
								showToast(getString(R.string.favorable_des)
										+ price * creditY
										+ getString(R.string.favorable_credit));
								jifenE.setText(0 + "");
								shifuT.setText(price + "");
							} else {
								daikouT.setText(getString(R.string.money_symbol)
										+ daikou);
								shifuT.setText(price - daikou + "");
							}
						}
					} else {

						jifenE.setText(0 + "");
						shifuT.setText(price + "");
						// jifenE.setText(0 + "");
					}

				}
			}
		});
		startDate = it.getStringExtra("startDate");
		endDate = it.getStringExtra("endDate");

		try {
			int days = RabbitUtils.daysBetween(startDate, endDate);
			ViewUtil.bindView(findViewById(R.id.date),
					getString(R.string.hotel_ruzhu) + startDate
							+ getString(R.string.hotel_likai) + endDate + " ["
							+ days + "]" + getString(R.string.hotel_des));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		id = it.getStringExtra("id");

		findViewById(R.id.submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				submit();

			}
		});
		getData();
	}

	private void getData() {
		DhNet net = new DhNet(new API().preHotelOrder);
		net.addParam("itemid", id);
		net.addParam("startdate", startDate);
		net.addParam("enddate", endDate);
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					ViewUtil.bindView(findViewById(R.id.name),
							JSONUtil.getString(jo, "title"));
					ViewUtil.bindView(findViewById(R.id.fangxing),
							JSONUtil.getString(jo, "name"));

					JSONObject user_dataJo = JSONUtil.getJSONObject(jo,
							"user_data");

					credit = JSONUtil.getInt(user_dataJo, "credit");
					int credit_s = JSONUtil.getInt(user_dataJo, "credit_s");
					if (credit_s != 0) {
						creditY = credit / (float) credit_s;

					} else {
						jifenE.setText(0 + "");
					}

					jifenE.setEnabled(credit_s == 0 ? false : true);

					ViewUtil.bindView(findViewById(R.id.tel),
							JSONUtil.getString(user_dataJo, "phone"));

					ViewUtil.bindView(findViewById(R.id.username),
							JSONUtil.getString(user_dataJo, "nickname"));
					price = JSONUtil.getDouble(jo, "dayprice");

					totalPriceT.setText(getString(R.string.money_symbol)
							+ JSONUtil.getString(jo, "totalprice"));
					shifuT.setText(JSONUtil.getString(jo, "totalprice"));

					ViewUtil.bindView(
							findViewById(R.id.price),
							getString(R.string.money_symbol)
									+ JSONUtil.getString(jo, "totalprice"));


					ViewUtil.bindView(
							findViewById(R.id.old_price),
							getString(R.string.money_symbol)
									+ JSONUtil.getString(jo, "dayprice") + "/"
									+ getString(R.string.hotel_des));
					cartView.setCarNum(JSONUtil.getInt(jo, "daycount"));
					cartView.setOnCartViewClickListener(new OnCartViewClickListener() {

						@Override
						public void onClick() {

							totalPriceT
									.setText(getString(R.string.money_symbol)
											+ cartView.getCartNum() * price);

							ViewUtil.bindView(
									findViewById(R.id.price),
									getString(R.string.money_symbol)
											+ cartView.getCartNum() * price);

							if (Integer.parseInt(jifenE.getText().toString()) == 0) {
								shifuT.setText(cartView.getCartNum() * price
										+ "");
							} else {
								shifuT.setText(cartView.getCartNum()
										* price
										- Integer.parseInt(jifenE.getText()
												.toString()) / creditY + "");
							}
						}
					});
					cartView.setMaxNum(JSONUtil.getInt(jo, "mincount"));
				}

			}
		});
	}

	private void submit() {

		DhNet net = new DhNet(new API().addHotelOrder);
		net.addParam("itemid", id);

		net.addParam("itemid", id);
		net.addParam("startdate", startDate);
		net.addParam("enddate", endDate);
		net.addParam("buyernote", msgE.getText().toString());
		net.addParam("buyername", usernameE.getText().toString());
		net.addParam("buyerphone", telE.getText().toString());
		net.addParam("ordercount", cartView.getCartNum());
		net.addParam("credit", jifenE.getText().toString());
		net.doPostInDialog(getString(R.string.submiting), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast(getString(R.string.submit_success));
					JSONObject jo = response.jSON();
					Intent it = new Intent(self, HotelOrderDetailActivity.class);
					it.putExtra("orderid", JSONUtil.getString(jo, "id"));
					startActivity(it);
					finishWithoutAnim();
				}

			}
		});

	}

}
