package com.means.rabbit.activity.main;

import net.duohuo.dhroid.util.UserLocation;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.means.rabbit.R;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.bean.User;

public class MapActivity extends RabbitBaseActivity {

	WebView webV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}

	@Override
	public void initView() {

		setTitle(getString(R.string.map_title));
		webV = (WebView) findViewById(R.id.content);
		WebSettings webSettings = webV.getSettings();
		webSettings.setJavaScriptEnabled(true);
		String url = getIntent().getStringExtra("url");
		Intent it = getIntent();
		float tolat = it.getFloatExtra("tolat", 0);
		float tolng = it.getFloatExtra("tolng", 0);
		String webUrl = url + "&fromlat="
				+ UserLocation.getInstance().getLatitude() + "&fromlng="
				+ UserLocation.getInstance().getLongitude();
		System.out.println("weburl:  " + webUrl);
		webV.loadUrl(webUrl);

	}

}
