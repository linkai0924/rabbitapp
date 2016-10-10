package com.means.rabbit.views.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;

import com.means.rabbit.R;


public class BaseAlertDialog extends AlertDialog {
    long animduring = 250;
    int direction = 1;

    public BaseAlertDialog(Context context, int theme) {
        super(context, theme);
        Window window = getWindow();
        window.setWindowAnimations(R.style.mystyle);
    }

    public BaseAlertDialog(Context context) {
        super(context);
        Window window = getWindow();
        window.setWindowAnimations(R.style.mystyle);
    }

}
