package com.means.rabbit.activity.finance;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;

/**
 * 
 * 提现到银行卡
 * 
 * @author Administrator
 * 
 */
public class WithdrawCardActivity extends RabbitBaseActivity {

	EditText moneyEt, cardidEt, nameEt;
	Button submitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_card);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.withdraw_card));

		moneyEt = (EditText) findViewById(R.id.money);
		cardidEt = (EditText) findViewById(R.id.cardid);
		nameEt = (EditText) findViewById(R.id.name);
		submitBtn = (Button) findViewById(R.id.submit);

		submitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submit();
			}
		});

	}

	private void submit() {
		String moneyStr = moneyEt.getText().toString();
		if (TextUtils.isEmpty(moneyStr)) {
			showToast(getString(R.string.withdrawcard_money_des));
			return;
		}
		int money = Integer.valueOf(moneyStr);
		String cardid = cardidEt.getText().toString();
		String name = nameEt.getText().toString();

		if (money <= 0) {
			showToast(getString(R.string.withdrawcard_money_des1));
			return;
		}

		if (TextUtils.isEmpty(cardid)) {
			showToast(getString(R.string.withdrawcard_code_des));
			return;
		}

		if (TextUtils.isEmpty(name)) {
			showToast(getString(R.string.withdrawcard_name_des));
			return;
		}

		DhNet net = new DhNet(new API().cashapply);
		net.addParam("name", name);
		net.addParam("num", cardid);
		net.addParam("amount", money);
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					finish();
					showToast(getString(R.string.withdrawcard_release_des));
				}
			}
		});

	}
}
