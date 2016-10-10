package com.means.rabbit.manage;

import android.app.Activity;
import android.content.Intent;

import com.means.rabbit.activity.my.LoginActivity;
import com.means.rabbit.bean.User;

public class UserInfoManage {

	static UserInfoManage instance;

	public static UserInfoManage getInstance() {
		if (instance == null) {
			instance = new UserInfoManage();
		}
		return instance;
	}

	public boolean checkLogin(final Activity context,
			final LoginCallBack loginCallBack) {
		boolean islogin = User.getInstance().isLogin();
		if (!islogin) {

			Intent it = new Intent(context, LoginActivity.class);
			context.startActivity(it);

		} else {
			if (loginCallBack != null) {
				loginCallBack.onisLogin();
			}
		}
		return islogin;
	}

	public boolean checkLogin2(final Activity context,
			final LoginCallBack loginCallBack) {
		boolean islogin = User.getInstance().isLogin();
		if (!islogin) {
			LoginActivity.loginCall = loginCallBack;
			Intent it = new Intent(context, LoginActivity.class);
			context.startActivity(it);
			return false;

		}

		if (loginCallBack != null) {
			if (islogin) {
				loginCallBack.onisLogin();
			}
		}

		return islogin;
	}

	public interface LoginCallBack {
		public void onisLogin();

		public void onLoginFail();
	}
}
