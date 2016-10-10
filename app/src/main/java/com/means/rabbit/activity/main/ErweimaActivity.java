package com.means.rabbit.activity.main;

import net.duohuo.dhroid.util.ViewUtil;
import android.os.Bundle;
import android.widget.ImageView;

import com.means.rabbit.R;
import com.means.rabbit.base.RabbitBaseActivity;

public class ErweimaActivity extends RabbitBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_erweima);

	}

	@Override
	public void initView() {
		setTitle(getString(R.string.erweima));
		ViewUtil.bindNetImage((ImageView) findViewById(R.id.pic), getIntent()
				.getStringExtra("url"), "default");

	}

}
