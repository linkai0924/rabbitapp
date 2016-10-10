package com.means.rabbit.activity.more;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;

public class HelpDetailActivity extends RabbitBaseActivity {

	TextView titleT, contentT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_detail);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.help_detail));

		titleT = (TextView) findViewById(R.id.title_txt);
		contentT = (TextView) findViewById(R.id.content);

		DhNet net = new DhNet(new API().helpdetail);
		net.addParam("id", getIntent().getStringExtra("id"));
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					titleT.setText(JSONUtil.getString(jo, "title"));
					contentT.setText(Html.fromHtml(JSONUtil.getString(jo, "content")));
				}
			}
		});

	}

}
