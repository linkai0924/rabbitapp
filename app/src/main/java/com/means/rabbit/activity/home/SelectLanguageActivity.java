package com.means.rabbit.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.means.rabbit.R;
import com.means.rabbit.base.RabbitBaseActivity;

public class SelectLanguageActivity extends RabbitBaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_language);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.select_language));

	}

	public void setLanguage(View v) {
		Intent it = new Intent(self , TranslateActivity.class);
		switch (v.getId()) {
		//中语
		case R.id.chinese:
			it.putExtra("language","zh");
			it.putExtra("label",getString(R.string.translate_chinese));
			setResult(RESULT_OK, it);
			break;
		//英语
		case R.id.english:
			it.putExtra("language","en");
			it.putExtra("label",getString(R.string.translate_english));
			setResult(RESULT_OK, it);
			break;
		//日语
		case R.id.japanese:
			it.putExtra("language","jp");
			it.putExtra("label",getString(R.string.translate_japanese));
			setResult(RESULT_OK, it);
			break;
		//阿拉伯语
		case R.id.arab:
			it.putExtra("language","ara");
			it.putExtra("label",getString(R.string.translate_arab));
			setResult(RESULT_OK, it);
			break;
		//朝鲜语
		case R.id.korea:
			it.putExtra("language","kor");
			it.putExtra("label",getString(R.string.translate_korea));
			setResult(RESULT_OK, it);
			break;
		//菲律宾语
		case R.id.philippines:
			it.putExtra("language","ph");
			it.putExtra("label",getString(R.string.translate_philippines));
			setResult(RESULT_OK, it);
			break;
		//印尼语
		case R.id.indonesia:
			it.putExtra("language","ind");
			it.putExtra("label",getString(R.string.translate_indonesia));
			setResult(RESULT_OK, it);
			break;
		//西班牙语
		case R.id.spain:
			it.putExtra("language","spa");
			it.putExtra("label",getString(R.string.translate_spain));
			setResult(RESULT_OK, it);
			break;
		//意大利语
		case R.id.italy:
			it.putExtra("language","it");
			it.putExtra("label",getString(R.string.translate_italy));
			setResult(RESULT_OK, it);
			break;

		default:
			break;
		}
		finish();
	}
}
