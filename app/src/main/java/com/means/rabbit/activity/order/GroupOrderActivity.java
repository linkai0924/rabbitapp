package com.means.rabbit.activity.order;

import org.json.JSONObject;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import com.means.rabbit.R;
import com.means.rabbit.R.layout;
import com.means.rabbit.activity.order.pay.GroupPayActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
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
 * 团购订单
 * 
 * @author Administrator
 * 
 */
public class GroupOrderActivity extends RabbitBaseActivity {

	String tuangouId;

	CartView cartView;

	Double price;

	TextView totalPriceT;

	int credit;

	EditText telT;

	TextView shifuT;

	EditText jifenE;

	TextView daikouT;

	// 积分比例
	float creditY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_order);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.grouporder));
		tuangouId = getIntent().getStringExtra("tuangouId");
		cartView = (CartView) findViewById(R.id.cartView);
		totalPriceT = (TextView) findViewById(R.id.total_price);

		jifenE = (EditText) findViewById(R.id.credit);
		daikouT = (TextView) findViewById(R.id.credit_s);
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

		telT = (EditText) findViewById(R.id.tel);

		findViewById(R.id.submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				submit();

			}
		});
		getData();
	}

	private void getData() {
		DhNet net = new DhNet(new API().preTuangouOrder);
		net.addParam("itemid", tuangouId);
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {

					JSONObject jo = response.jSONFromData();
					ViewUtil.bindView(findViewById(R.id.name),
							JSONUtil.getString(jo, "title"));
					price = JSONUtil.getDouble(jo, "price");
					ViewUtil.bindView(findViewById(R.id.price),
							getString(R.string.money_symbol) + price);
					cartView.setMaxNum(100);
					cartView.setOnCartViewClickListener(new OnCartViewClickListener() {

						@Override
						public void onClick() {

							totalPriceT
									.setText(getString(R.string.money_symbol)
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
					totalPriceT.setText(getString(R.string.money_symbol)
							+ price);
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

					ViewUtil.bindView(telT,
							JSONUtil.getString(user_dataJo, "phone"));
					shifuT.setText(price + "");

				}

			}
		});
	}

	private void submit() {

		DhNet net = new DhNet(new API().addTuangouOrder);
		net.addParam("itemid", tuangouId);
		net.addParam("buyerphone", telT.getText().toString());
		net.addParam("ordercount", cartView.getCartNum());
		net.addParam("credit", jifenE.getText().toString());
		net.doPostInDialog(getString(R.string.submiting), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast(getString(R.string.submit_success));
					JSONObject jo = response.jSONFromData();
					Intent it = new Intent(self, GroupPayActivity.class);
					it.putExtra("orderid", JSONUtil.getString(jo, "id"));
					startActivity(it);
				}

			}
		});

	}
}
