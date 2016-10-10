package com.means.rabbit.views;

import com.means.rabbit.R;
import com.means.rabbit.views.CatPop.OnReslutClickListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabView extends LinearLayout {
	Context mContext;
	View contentV;

	TextView leftT, centerT, rightT;

	CatPop pop;

	AreaPop areaPop;

	SortPop sortPop;

	OnTabSelectListener onTabSelectListener;

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	private void initView() {
		contentV = LayoutInflater.from(mContext).inflate(R.layout.tab_view,
				this);
		leftT = (TextView) contentV.findViewById(R.id.left);
		centerT = (TextView) contentV.findViewById(R.id.center);
		rightT = (TextView) contentV.findViewById(R.id.right);
		leftT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop = new CatPop(mContext, CatPop.CAT);
				pop.show(TabView.this);
			}
		});

		centerT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (centerT.getTag().toString().equals("品牌")) {
					pop = new CatPop(mContext, CatPop.BRAND);
				} else {
					pop = new CatPop(mContext, CatPop.AREA);
				}
				pop.setOnReslutClickListener(new OnReslutClickListener() {

					@Override
					public void result(String catname, String catid) {
						centerT.setText(catname);
						if (onTabSelectListener != null) {
							onTabSelectListener.onCenterSelect(catid);
						}
					}
				});
				pop.show(TabView.this);
			}
		});

		rightT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sortPop = new SortPop(mContext);
				sortPop.setOnReslutClickListener(new SortPop.OnReslutClickListener() {

					@Override
					public void result(String catname, String tag) {
						rightT.setText(catname);
						if (onTabSelectListener != null) {
							onTabSelectListener.onRightSelect(tag);
						}
					}
				});
				sortPop.show(TabView.this);
			}
		});
	}

	public void setLeftText(String text) {
		leftT.setText(text);
	}

	public void setCentertText(String text, String tag) {
		centerT.setText(text);
		centerT.setTag(tag);
		centerT.setVisibility(View.VISIBLE);
	}

	public void setRightText(String text) {
		rightT.setText(text);
	}

	public OnTabSelectListener getOnTabSelectListener() {
		return onTabSelectListener;
	}

	public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
		this.onTabSelectListener = onTabSelectListener;
	}

	public interface OnTabSelectListener {
		void onCenterSelect(String result);

		void onRightSelect(String result);
	}

}
