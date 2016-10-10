package com.means.rabbit.views.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.means.rabbit.R;

public class DelectDialog extends BaseAlertDialog{

	Context mContext;

	OnDelectResultListener delectResultListener;

	public DelectDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_delect);
		initView();
	}

	private void initView() {

		findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delectResultListener.onResult();
				dismiss();
			}
		});

	}

	public interface OnDelectResultListener {
		void onResult();
	}

	public OnDelectResultListener getOnDelectResultListener() {
		return delectResultListener;
	}

	public void setOnDelectResultListener(
			OnDelectResultListener delectResultListener) {
		this.delectResultListener = delectResultListener;
	}

}
