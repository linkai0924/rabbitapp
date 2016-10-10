package com.means.rabbit.activity.my;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.RabbitApplication;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitUtils;

/**
 * 
 * 注册
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends RabbitBaseActivity implements
		OnClickListener {

	private EditText phoneEt, verificationEt, nicknameEt, passwordEt, emailE;
	private TextView getverificationBtn;
	private Button registerBtn;

	private TimeCount time = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.register));

		if (RabbitApplication.getInstance().getisPhone()) {
			findViewById(R.id.phone_layout).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.email_layout).setVisibility(View.VISIBLE);
		}

		time = new TimeCount(60000, 1000);
		emailE = (EditText) findViewById(R.id.email);
		phoneEt = (EditText) findViewById(R.id.phone);
		verificationEt = (EditText) findViewById(R.id.verification);
		nicknameEt = (EditText) findViewById(R.id.nickname);
		passwordEt = (EditText) findViewById(R.id.password);
		getverificationBtn = (TextView) findViewById(R.id.getverification);
		registerBtn = (Button) findViewById(R.id.register);

		getverificationBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);

	}

	private void register() {
		String tel = phoneEt.getText().toString();
		String email = emailE.getText().toString();
		String password = passwordEt.getText().toString();
		String code = verificationEt.getText().toString();
		String nickname = nicknameEt.getText().toString();

		if (RabbitApplication.getInstance().getisPhone()) {
			if (TextUtils.isEmpty(tel)) {
				showToast(getString(R.string.editinfo_tel_des1));
				return;
			}
			if (tel.length() != 11) {
				showToast(getString(R.string.editinfo_tel_des));
				return;
			}
		} else {
			if (TextUtils.isEmpty(email)) {
				showToast(getString(R.string.editinfo_email_des1));
				return;
			}
			if (!RabbitUtils.checkEmail(email)) {
				showToast(getString(R.string.editinfo_email_des2));
				return;
			}
		}
		if (TextUtils.isEmpty(code)) {
			showToast(getString(R.string.editinfo_code_des));
			return;
		}
		if (TextUtils.isEmpty(password)) {
			showToast(getString(R.string.edit_phone_hint_password));
			return;
		}
		if (password.length() < 6 || password.length() > 15) {
			showToast(getString(R.string.editinfo_pswd_des));
			return;
		}
		if (!RabbitUtils.isLetter(password)) {
			showToast(getString(R.string.editinfo_pswd_des1));
			return;
		}
		if (TextUtils.isEmpty(nickname)) {
			showToast(getString(R.string.edit_input_nickname));
			return;
		}
		if (nickname.length() > 8) {
			showToast(getString(R.string.register_pswd_des));
			return;
		}

		DhNet smsNet = new DhNet(new API().register);
		smsNet.addParam("name", nickname);
		smsNet.addParam("pswd", password);

		if (RabbitApplication.getInstance().getisPhone()) {
			smsNet.addParam("phone", tel);
			smsNet.addParam("mobilecode", code);
		} else {
			smsNet.addParam("email", email);
			smsNet.addParam("emailcode", code);
		}

		smsNet.addParam("type", 1);

		smsNet.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					showToast(getString(R.string.register_success));
					finish();
				}
			}
		});

	}

	// 获取验证码
	private void getMobileCode() {
		String tel = phoneEt.getText().toString();
		if (TextUtils.isEmpty(tel)) {
			showToast(getString(R.string.editinfo_tel_des1));
			return;
		}
		if (tel.length() != 11) {
			showToast(getString(R.string.editinfo_tel_des));
			return;
		}
		time.start();
		DhNet smsNet = new DhNet(new API().mobilecode);
		smsNet.addParam("phone", tel);
		smsNet.addParam("type", "1"); // 1为注册
		smsNet.doGet(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
				}
			}
		});
	}

	// 获取验证码
	private void getEmailCode() {
		String email = emailE.getText().toString();
		if (TextUtils.isEmpty(email)) {
			showToast(getString(R.string.editinfo_email_des1));
			return;
		}
		if (!RabbitUtils.checkEmail(email)) {
			showToast(getString(R.string.editinfo_email_des2));
			return;
		}
		time.start();
		DhNet smsNet = new DhNet(new API().emailcode);
		smsNet.addParam("email", email);
		smsNet.addParam("type", "1"); // 1为注册
		smsNet.doGet(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
				}
			}
		});
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			getverificationBtn
					.setText(getString(R.string.editinfo_code_release));
			getverificationBtn.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			getverificationBtn.setEnabled(false);
			getverificationBtn.setText(millisUntilFinished / 1000 + "s");

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.getverification:

			if (RabbitApplication.getInstance().getisPhone()) {
				getMobileCode();
			} else {
				getEmailCode();
			}
			break;
		case R.id.register:
			register();
			break;
		}
	}

}
