package com.means.rabbit.activity.my.edit;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitPerference;

/**
 * 修改昵称
 * 
 * @author Administrator
 * 
 */
public class EditNickNameActivity extends RabbitBaseActivity {

	EditText nicknameEt;
	Button saveBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_nick_name);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.editinfo));

		nicknameEt = (EditText) findViewById(R.id.nickname);
		saveBtn = (Button) findViewById(R.id.save);

		nicknameEt.setText(getIntent().getStringExtra("nickname"));

		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				editNickName();
			}
		});
	}

	public void editNickName() {
		final String name = nicknameEt.getText().toString();
		if (TextUtils.isEmpty(name)) {
			showToast(getString(R.string.editinfo_name_des));
			return;
		}
		if (name.length() > 14) {
			showToast(getString(R.string.editinfo_name_des1));
			return;
		}

		DhNet net = new DhNet(new API().editaction);
		net.addParam("name", name);
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					showToast(getString(R.string.editinfo_success));
					RabbitPerference per = IocContainer.getShare().get(
							RabbitPerference.class);
					per.load();
					per.name = name;
					per.commit();
					Intent it = new Intent(self, EditInfoActivity.class);
					it.putExtra("nickname", name);
					setResult(RESULT_OK, it);
					finish();
				}
			}
		});
	}
}
