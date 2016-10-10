package com.means.rabbit.activity.my.edit;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import com.means.rabbit.R;
import com.means.rabbit.R.layout;
import com.means.rabbit.activity.my.edit.ForgetPswdActivity.TimeCount;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitPerference;
import com.means.rabbit.utils.RabbitUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * 修改手机
 * 
 * @author Administrator
 * 
 */
public class EditPhoneActivity extends RabbitBaseActivity implements
		OnClickListener {

	EditText newphoneEt, verificationEt;
	// EditText phoneEt,passwordEt;
	TextView getverificationT;
	Button submitBtn;

	private TimeCount time = null;

	RabbitPerference per;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_phone);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.editinfo));
		time = new TimeCount(60000, 1000);
		per = IocContainer.getShare().get(RabbitPerference.class);
		per.load();

		// phoneEt = (EditText) findViewById(R.id.phone);
		newphoneEt = (EditText) findViewById(R.id.newphone);
		// passwordEt = (EditText) findViewById(R.id.password);
		verificationEt = (EditText) findViewById(R.id.verification);
		getverificationT = (TextView) findViewById(R.id.getverification);
		submitBtn = (Button) findViewById(R.id.submit);

		getverificationT.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
	}

	private void submit() {
		// String tel = phoneEt.getText().toString();
		final String newtel = newphoneEt.getText().toString();
		// final String password = passwordEt.getText().toString();
		String code = verificationEt.getText().toString();
		// if (TextUtils.isEmpty(tel)) {
		// showToast("请输入旧手机号");
		// return;
		// }
		if (TextUtils.isEmpty(newtel)) {
			showToast(getString(R.string.edit_phone_hint_new_phone));
			return;
		}
		// if (tel.length() != 11) {
		// showToast("手机号格式不正确");
		// return;
		// }
		if (newtel.length() != 11) {
			showToast(getString(R.string.editinfo_tel_des));
			return;
		}
		if (TextUtils.isEmpty(code)) {
			showToast(getString(R.string.editinfo_code_des));
			return;
		}
		// if (TextUtils.isEmpty(password)) {
		// showToast("请输入密码");
		// return;
		// }

		DhNet smsNet = new DhNet(new API().changephone);
		smsNet.addParam("oldphone", per.getPhone());
		smsNet.addParam("pswd", per.getPassword());
		smsNet.addParam("phone", newtel);
		smsNet.addParam("mobilecode", code);
		smsNet.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					showToast(getString(R.string.editinfo_success));
					Intent it = new Intent(self, EditInfoActivity.class);
					it.putExtra("phone", newtel);
					setResult(RESULT_OK, it);
					finish();
				}
			}
		});

	}

	// 获取验证码
	private void getMobileCode() {
		String tel = newphoneEt.getText().toString();
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
		smsNet.addParam("type", "4"); // 4为改手机
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
			getverificationT.setText(getString(R.string.editinfo_code_release));
			getverificationT.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			getverificationT.setEnabled(false);
			getverificationT.setText(millisUntilFinished / 1000 + "s");

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.getverification:
			getMobileCode();
			break;
		case R.id.submit:
			submit();
			break;

		default:
			break;
		}
	}
}
