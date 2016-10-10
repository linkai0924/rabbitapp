package com.means.rabbit.activity.main;

import android.os.Bundle;

import com.means.rabbit.R;
import com.means.rabbit.base.RabbitBaseActivity;

public class HelpActivity extends RabbitBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.jinjiqiuzhu));
	}

}
