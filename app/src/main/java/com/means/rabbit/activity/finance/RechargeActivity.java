package com.means.rabbit.activity.finance;

import java.math.BigDecimal;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import com.means.rabbit.R;
import com.means.rabbit.RabbitApplication;
import com.means.rabbit.R.layout;
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
import android.widget.EditText;

/**
 * 
 * 账户充值
 * 
 * @author Administrator
 * 
 */
public class RechargeActivity extends RabbitBaseActivity {

	EditText moneyE;

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
		setContentView(R.layout.activity_recharge);
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.recharge));

		if (RabbitApplication.getInstance().getLangType() == 1) {
			findViewById(R.id.paypal).setVisibility(View.GONE);
			findViewById(R.id.zhifubao).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.paypal).setVisibility(View.VISIBLE);
			findViewById(R.id.zhifubao).setVisibility(View.GONE);
		}
		moneyE = (EditText) findViewById(R.id.money);

		findViewById(R.id.paypal).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(moneyE.getText().toString())) {
					showToast(getString(R.string.recharge_des));
					return;
				}

				if (Integer.parseInt(moneyE.getText().toString()) == 0) {
					showToast(getString(R.string.recharge_des1));
					return;
				}
				payByPaypal();
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

		PayPalPayment payment = new PayPalPayment(new BigDecimal(moneyE
				.getText().toString()), huobi, getString(R.string.recharge),
				PayPalPayment.PAYMENT_INTENT_SALE);
		Intent intent = new Intent(this, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
		startActivityForResult(intent, 0);
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
		net.addParam("payprice", moneyE.getText().toString());
		net.addParam("type", 3);

		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					showToast(getString(R.string.recharge_success));
					Intent it = getIntent();
					setResult(Activity.RESULT_OK, it);
					finish();
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}
}
