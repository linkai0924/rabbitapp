package com.means.rabbit.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.adapter.CatLeftAdapter;
import com.means.rabbit.adapter.CatRightAdapter;
import com.means.rabbit.views.CatPop.OnReslutClickListener;

public class SortPop {
	Context context;

	View contentV;

	PopupWindow pop;

	OnReslutClickListener onReslutClickListener;

	LinearLayout layoutV;

	public SortPop(Context context) {
		this.context = context;
		contentV = LayoutInflater.from(context)
				.inflate(R.layout.pop_sort, null);
		pop = new PopupWindow(contentV, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT, true);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		// pop.setAnimationStyle(R.style.mystyle);
		pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		initView();

	}

	private void initView() {
		contentV.findViewById(R.id.bg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						pop.dismiss();
					}
				});

		layoutV = (LinearLayout) contentV.findViewById(R.id.layout);
		System.out
				.println("layoutV.getChildCount():" + layoutV.getChildCount());
		for (int i = 0; i < layoutV.getChildCount(); i+=2) {
			System.out.println("i:" + i);
			RelativeLayout childV = (RelativeLayout) layoutV.getChildAt(i);
			final TextView textT = (TextView) childV.getChildAt(0);
			childV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (onReslutClickListener != null) {
						onReslutClickListener.result(
								textT.getText().toString(), v.getTag()
										.toString());
					}
					pop.dismiss();

				}
			});

		}
	}

	public void show(View v) {
		pop.showAsDropDown(v);
	}

	public OnReslutClickListener getOnReslutClickListener() {
		return onReslutClickListener;
	}

	public void setOnReslutClickListener(
			OnReslutClickListener onReslutClickListener) {
		this.onReslutClickListener = onReslutClickListener;
	}

	public interface OnReslutClickListener {
		void result(String catname, String tag);
	}

}
