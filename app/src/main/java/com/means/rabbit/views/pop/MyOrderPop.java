package com.means.rabbit.views.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.views.SortPop.OnReslutClickListener;

public class MyOrderPop {
	Context context;

	View contentV;

	PopupWindow pop;

	OnReslutClickListener onReslutClickListener;

	LinearLayout classify_layout,state_layout,layoutV;
	
	int type;

	public MyOrderPop(Context context,int type) {
		this.context = context;
		this.type = type;
		contentV = LayoutInflater.from(context)
				.inflate(R.layout.pop_my_order, null);
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
		
			
		setPopType();
		for (int i = 0; i < layoutV.getChildCount(); i+=2) {
			System.out.println("i:" + i);
			RelativeLayout childV = (RelativeLayout) layoutV.getChildAt(i);
			final TextView textT = (TextView) childV.getChildAt(0);
			childV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (onReslutClickListener != null) {
						onReslutClickListener.result(type,
								textT.getText().toString(), v.getTag()
										.toString());
					}
					pop.dismiss();

				}
			});

		}
	}
	
	public void setPopType(){
		classify_layout = (LinearLayout) contentV.findViewById(R.id.classify_layout);
		state_layout = (LinearLayout) contentV.findViewById(R.id.state_layout);
		classify_layout.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
		state_layout.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
		layoutV = type == 0 ? classify_layout : state_layout;
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
		void result(int type,String catname, String tag);
	}
}
