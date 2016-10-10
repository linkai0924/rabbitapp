package com.means.rabbit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.means.rabbit.R;
import com.means.rabbit.activity.merchants.GoodDetailActivity;
import com.means.rabbit.activity.merchants.HotelDetailActivity;
import com.means.rabbit.activity.merchants.ShopDetailActivity;
import com.means.rabbit.activity.merchants.TuangouDetailActivity;
import com.means.rabbit.activity.order.pay.GroupPayActivity;
import com.means.rabbit.activity.order.pay.HotelOrderDetailActivity;
import com.means.rabbit.activity.order.pay.InsteadShoppingPayActivity;
import com.means.rabbit.activity.travel.TravelDetailActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.manage.UserInfoManage;

public class RabbitUtils {
	// 是否包含字母
	public static boolean isLetter(String str) {
		for (int i = 0; i < str.length(); i++) { // 循环遍历字符串
			if (Character.isLetter(str.charAt(i))) { // 用char包装类中的判断字母的方法判断每一个字符
				return true;
			}
		}
		return false;
	}

	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static void call(Context context, String number) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ number));
		context.startActivity(intent);
	}

	public static void ImgIntent(Context context, int type, String id) {

		Intent it = null;

		switch (type) {
		case 1:
			it = new Intent(context, TravelDetailActivity.class);
			it.putExtra("id", Integer.parseInt(id));
			break;

		case 2:
			it = new Intent(context, HotelDetailActivity.class);
			it.putExtra("hotelId", id);
			break;

		case 3:
			it = new Intent(context, ShopDetailActivity.class);
			it.putExtra("shopId", id);
			break;

		case 4:
			it = new Intent(context, TuangouDetailActivity.class);
			it.putExtra("tuangouId", id);
			break;

		case 5:
			it = new Intent(context, GoodDetailActivity.class);
			it.putExtra("daigouId", id);
			break;

		default:
			break;
		}

		context.startActivity(it);

	}

	public static void erweimaIntent(final Context context, String type,
			final String id, final String key) {

		Intent it = null;

		if (type.equals("shop")) {

			it = new Intent(context, ShopDetailActivity.class);
			it.putExtra("shopId", id);
			it.putExtra("key", key);
		} else if (type.equals("hotel")) {

			it = new Intent(context, HotelDetailActivity.class);
			it.putExtra("hotelId", id);
			it.putExtra("key", key);
		} else if (type.equals("tuangou")) {

			it = new Intent(context, TuangouDetailActivity.class);
			it.putExtra("tuangouId", id);
			it.putExtra("key", key);
		} else if (type.equals("daigou")) {

			it = new Intent(context, GoodDetailActivity.class);
			it.putExtra("daigouId", id);
			it.putExtra("key", key);
		} else if (type.equals("article")) {

			it = new Intent(context, TravelDetailActivity.class);
			it.putExtra("key", key);
			it.putExtra("id", Integer.parseInt(id));
		} else if (type.equals("order")) {
			UserInfoManage.getInstance().checkLogin2((Activity) context,
					new UserInfoManage.LoginCallBack() {

						@Override
						public void onisLogin() {
							usecode(context, id, key);
						}

						@Override
						public void onLoginFail() {
							// TODO Auto-generated method stub

						}
					});
		}

		if (it != null) {
			context.startActivity(it);
		}

	}

	private static void usecode(final Context context, String orderid,
			String code) {
		DhNet net = new DhNet(new API().usecode);
		net.addParam("orderid", orderid);
		net.addParam("ercode", code);
		net.doPostInDialog("提交中...", new NetTask(context) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {

					IocContainer.getShare().get(IDialog.class)
							.showToastShort(context, "消费成功!");
					JSONObject jo = response.jSON();
					Intent it;
					int type = JSONUtil.getInt(jo, "type");
					if (type == 2) {
						it = new Intent(context, GroupPayActivity.class);

					} else if (type == 1) {
						it = new Intent(context, HotelOrderDetailActivity.class);
					} else {
						it = new Intent(context,
								InsteadShoppingPayActivity.class);
					}
					it.putExtra("orderid", JSONUtil.getString(jo, "id"));
					context.startActivity(it);
				}

			}
		});
	}
	
	public static boolean checkEmail(String email){
        boolean flag = false;
        try{
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
}
