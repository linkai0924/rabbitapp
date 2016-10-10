package com.means.rabbit.views;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.means.rabbit.R;

public class TranslatePop implements View.OnClickListener {

    Activity context;


    View contentV;

    PopupWindow pop;

    

    public TranslatePop(Activity context) {
        this.context = context;
        contentV = LayoutInflater.from(context).inflate(R.layout.pop_tarnslate, null);
        pop = new PopupWindow(contentV, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.mystyle);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();

    }

    private void initView() {

    }

    public void show(View v) {
        pop.showAsDropDown(v);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
    

}

