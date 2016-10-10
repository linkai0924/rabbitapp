package com.means.rabbit.views;

import net.duohuo.dhroid.util.DhUtil;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.means.rabbit.R;

public class MyToast {
	
	private static MyToast toastCommom;  
    
    private Toast toast;  
      
    private MyToast(){  
    }  
      
    public static MyToast createToastConfig(){  
        if (toastCommom==null) {  
            toastCommom = new MyToast();  
        }  
        return toastCommom;  
    }  
      
    /**  
     * 显示Toast  
     * @param context  
     * @param root  
     * @param tvString  
     */  
      
    public void ToastShow(Context context,ViewGroup root,String tvString){  
        View layout = LayoutInflater.from(context).inflate(R.layout.my_toast,root);  
        TextView text = (TextView) layout.findViewById(R.id.text);  
        text.setText(tvString);  
        toast = new Toast(context);  
        toast.setGravity(Gravity.TOP, 0,DhUtil.dip2px(context, 100));  
        toast.setDuration(Toast.LENGTH_SHORT);  
        toast.setView(layout);  
        toast.show();  
    }  

}
