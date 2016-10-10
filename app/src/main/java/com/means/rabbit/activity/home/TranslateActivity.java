package com.means.rabbit.activity.home;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.views.TranslatePop;

/**
 * 翻译助手
 * 
 * @author dell
 * 
 */
public class TranslateActivity extends RabbitBaseActivity implements
		OnClickListener {

	ImageView voiceI;

	View my_titlebarV;

	TranslatePop pop;

	String slang = "en";

	String lang = "zh";

	EditText contentE;

	TextView rightText, leftText;

	private final int LANGUAGECODELEFT = 1;

	private final int LANGUAGECODERIGHT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translate);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.translate));
		my_titlebarV = findViewById(R.id.my_titlebar);
		// TODO Auto-generated method stub
		rightText = (TextView) findViewById(R.id.right_text);
		leftText = (TextView) findViewById(R.id.left_lang);

		rightText.setOnClickListener(this);
		leftText.setOnClickListener(this);

		voiceI = (ImageView) findViewById(R.id.voice);
		voiceI.setOnClickListener(this);

		contentE = (EditText) findViewById(R.id.content);

		findViewById(R.id.translate).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				translate();
			}
		});

	}

	@Override
	public void onClick(View v) {
		Intent it;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_lang:
			it = new Intent(self, SelectLanguageActivity.class);
			startActivityForResult(it, LANGUAGECODELEFT);
			break;

		case R.id.right_text:
			it = new Intent(self, SelectLanguageActivity.class);
			startActivityForResult(it, LANGUAGECODERIGHT);
			break;

		case R.id.voice:
			pop = new TranslatePop(self);
			pop.show(my_titlebarV);
			break;

		default:
			break;
		}
	}

	private void translate() {
		if (TextUtils.isEmpty(contentE.getText().toString())) {
			showToast(getString(R.string.translate_content_hint));
			return;
		}

		DhNet net = new DhNet(new API().translation);
		net.addParam("slang", slang);
		net.addParam("lang", lang);
		net.addParam("q", contentE.getText().toString());
		net.doGetInDialog(getString(R.string.translate_ing), new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					JSONObject jo = response.jSON();
					ViewUtil.bindView(findViewById(R.id.result),
							JSONUtil.getString(jo, "data"));

				}

			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);

		if (arg1 == RESULT_OK) {
			if (arg0 == LANGUAGECODERIGHT) {
				slang = arg2.getStringExtra("language");
				rightText.setText(arg2.getStringExtra("label"));
			}

			if (arg0 == LANGUAGECODELEFT) {
				lang = arg2.getStringExtra("language");
				leftText.setText(arg2.getStringExtra("label"));
			}
		}
	}

}
