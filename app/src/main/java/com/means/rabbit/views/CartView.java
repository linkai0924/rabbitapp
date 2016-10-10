package com.means.rabbit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.means.rabbit.R;

public class CartView extends LinearLayout {
	Context mContext;

	TextView cartNumT;

	OnCartViewClickListener onCartViewClickListener;

	ImageView minusI;

	ImageView addI;

	int maxNum;

	public CartView(Context context) {
		super(context);
	}

	public CartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.include_cart_view, this);
		cartNumT = (TextView) findViewById(R.id.cart_num);
		minusI = (ImageView) findViewById(R.id.minus);
		minusI.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				minusNum();
				if (onCartViewClickListener != null) {
					onCartViewClickListener.onClick();
				}
			}
		});

		addI = (ImageView) findViewById(R.id.add);
		addI.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addNum();
				if (onCartViewClickListener != null) {
					onCartViewClickListener.onClick();
				}
			}
		});
	}

	public void addNum() {
		int num = getCartNum();
		if (num < maxNum) {
			num++;
		}
		cartNumT.setText(num + "");
	}

	public void minusNum() {
		int num = getCartNum();
		if (num <= 0) {
			num = 0;
		} else {
			num--;
		}
		cartNumT.setText(num + "");
	}

	public int getCartNum() {
		int num = 0;
		try {
			num = Integer.valueOf(cartNumT.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		return num;
	}

	// public void setCartNumTextView() {
	// if (mGood != null) {
	// cartNumT.setVisibility(mGood.getCount() >= 1 ? View.VISIBLE
	// : View.GONE);
	// minusI.setVisibility(mGood.getCount() >= 1 ? View.VISIBLE
	// : View.INVISIBLE);
	// cartNumT.setText(mGood.getCount() + "");
	// } else {
	// cartNumT.setVisibility(View.INVISIBLE);
	// minusI.setVisibility(View.INVISIBLE);
	// }
	// }

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public void setCarNum(int num) {
		cartNumT.setText(num + "");
	}

	public OnCartViewClickListener getOnCartViewClickListener() {
		return onCartViewClickListener;
	}

	public void setOnCartViewClickListener(
			OnCartViewClickListener onCartViewClickListener) {
		this.onCartViewClickListener = onCartViewClickListener;
	}

	public interface OnCartViewClickListener {

		void onClick();

	}

}
