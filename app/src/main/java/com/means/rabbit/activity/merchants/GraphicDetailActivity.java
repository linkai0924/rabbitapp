package com.means.rabbit.activity.merchants;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.base.RabbitBaseActivity;

public class GraphicDetailActivity extends RabbitBaseActivity {

	String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graphic_detail);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.graphic_detail));
		content = getIntent().getStringExtra("content");
		TextView textT = (TextView) findViewById(R.id.text);
		textT.setText(Html.fromHtml(content));
	}

}
