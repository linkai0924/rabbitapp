package com.means.rabbit.activity.order.pay;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import com.means.rabbit.R;
import com.means.rabbit.R.layout;
import com.means.rabbit.RabbitApplication;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.constans.Const;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * 支付订单
 * 
 * @author Administrator
 * 
 */
public class PayOrderActivity extends RabbitBaseActivity {

	String payprice;

	String orderid;

	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

	// note that these credentials will differ between live & sandbox
	// environments.

	private static final int REQUEST_CODE_PAYMENT = 1;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	private static final int REQUEST_CODE_PROFILE_SHARING = 3;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(Const.CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("王骁")
			.merchantPrivacyPolicyUri(
					Uri.parse("http://cn.lazybunny.c.wanruankeji.com/home/account/paypalnotify"))
			.merchantUserAgreementUri(
					Uri.parse("http://cn.lazybunny.c.wanruankeji.com/home/account/paypalnotify"));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_order);
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.pay_order));
		Intent it = getIntent();
		payprice = it.getStringExtra("payprice");
		orderid = it.getStringExtra("orderid");
		ViewUtil.bindView(findViewById(R.id.name), it.getStringExtra("name"));
		ViewUtil.bindView(findViewById(R.id.payprice),
				getString(R.string.money_symbol) + payprice);

		if (RabbitApplication.getInstance().getLangType() == 1) {
			findViewById(R.id.paypal).setVisibility(View.GONE);
			findViewById(R.id.zhifubao).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.paypal).setVisibility(View.VISIBLE);
			findViewById(R.id.zhifubao).setVisibility(View.GONE);
		}

		findViewById(R.id.paybymoney).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!TextUtils.isEmpty(orderid)) {
					payByMoney();
				} else {
					payByMoneyYH();
				}

			}
		});

		findViewById(R.id.paypal).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payByPaypal();
			}
		});

		getMyMoney();
	}

	private void payByMoney() {
		DhNet net = new DhNet(new API().pay);
		net.addParam("orderid", orderid);
		net.addParam("payprice", payprice);
		net.doPostInDialog(getString(R.string.pay_paying), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				if (response.isSuccess()) {
					showToast(getString(R.string.pay_success));
					Intent it = getIntent();
					setResult(Activity.RESULT_OK, it);
					finish();
				}

			}
		});
	}

	// 优惠买单使用余额支付
	private void payByMoneyYH() {
		DhNet net = new DhNet(new API().youhuibuy);
		net.addParam("contentid", getIntent().getStringExtra("contentid"));
		net.addParam("payprice", payprice);
		net.addParam("credit", getIntent().getStringExtra("credit"));
		net.doPostInDialog(getString(R.string.pay_paying), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					showToast(getString(R.string.pay_success));
					Intent it = getIntent();
					setResult(Activity.RESULT_OK, it);
					finish();
				}

			}
		});
	}

	private void getMyMoney() {
		DhNet net = new DhNet(new API().accountview);
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					ViewUtil.bindView(
							findViewById(R.id.money),
							getString(R.string.money_symbol)
									+ JSONUtil.getString(jo, "balance"));
				}
			}
		});
	}

	private void payByPaypal() {

		String huobi = "";
		if (RabbitApplication.getInstance().getLangType() == 1) {

		} else if (RabbitApplication.getInstance().getLangType() == 2) {
			huobi = "USD";
		} else {
			huobi = "MYR";
		}

		PayPalPayment payment = new PayPalPayment(new BigDecimal(payprice),
				huobi, getIntent().getStringExtra("name"),
				PayPalPayment.PAYMENT_INTENT_SALE);
		Intent intent = new Intent(this, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			PaymentConfirmation confirm = data
					.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if (confirm != null) {
				JSONObject jo = confirm.toJSONObject();

				System.out.println("paymentExample" + jo.toString());

				JSONObject response = JSONUtil.getJSONObject(jo, "response");

				String paymentid = JSONUtil.getString(response, "id");
				paypalVerification(paymentid);
				// TODO: 发送支付ID到你的服务器进行验证
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			// Log.i("paymentExample", "The user canceled.");
		} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
			// Log.i("paymentExample",
			// "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
		}
	}

	private void paypalVerification(String id) {
		DhNet net = new DhNet(new API().paypal);
		net.addParam("paymentid", id);
		net.addParam("payprice", payprice);
		net.addParam("orderid", orderid);
		net.addParam("contentid", getIntent().getStringExtra("contentid"));
		if (!TextUtils.isEmpty(getIntent().getStringExtra("contentid"))) {
			net.addParam("type", 2);
		} else {
			net.addParam("type", 1);
		}
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					showToast(getString(R.string.pay_success));
					Intent it = getIntent();
					setResult(Activity.RESULT_OK, it);
					finish();
				}

			}
		});
	}

}
