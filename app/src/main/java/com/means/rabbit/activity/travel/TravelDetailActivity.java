package com.means.rabbit.activity.travel;

import java.util.Date;

import org.json.JSONObject;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.activity.main.ErweimaActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.DateUtils;

/**
 * 
 * 旅游详情
 * 
 * @author Administrator
 * 
 */
public class TravelDetailActivity extends RabbitBaseActivity {

	private ImageView picI;
	private TextView titleT, adddatelineT, viewsT;

	private int id;

	WebView contentT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_detail);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.travel_details));
		id = getIntent().getIntExtra("id", 0);

		picI = (ImageView) findViewById(R.id.pic);
		titleT = (TextView) findViewById(R.id.ttitle);
		adddatelineT = (TextView) findViewById(R.id.adddateline);
		viewsT = (TextView) findViewById(R.id.views);
		contentT = (WebView) findViewById(R.id.content);
		getTravelDetail();

	}

	private void getTravelDetail() {
		DhNet net = new DhNet(new API().contentview);
		net.addParam("key", getIntent().getStringExtra("key"));
		net.addParam("id", id);
		net.doGetInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					final JSONObject jo = response.jSONFromData();
					setRightAction(R.drawable.erweima, new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent it = new Intent(self, ErweimaActivity.class);
							it.putExtra("url", JSONUtil.getString(jo, "pic_qr"));
							startActivity(it);
						}
					});

					setRightAction2(JSONUtil.getBoolean(jo, "is_collect"),
							JSONUtil.getString(jo, "id"), "1");

					ViewUtil.bindNetImage(picI, JSONUtil.getString(jo, "pic"),
							"default");
					ViewUtil.bindView(titleT, JSONUtil.getString(jo, "title"));
					contentT.loadDataWithBaseURL(null,
							JSONUtil.getString(jo, "content"), "text/html",
							"utf-8", null);
					ViewUtil.bindView(
							adddatelineT,
							getString(R.string.travel_time)
									+ DateUtils.dateToStr(new Date(Long
											.parseLong(JSONUtil.getString(jo,
													"adddateline")) * 1000)));
					ViewUtil.bindView(viewsT,
							getString(R.string.travel_des) + JSONUtil.getString(jo, "views"));
				}
			}
		});
	}

}
