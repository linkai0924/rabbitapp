package com.means.rabbit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.duohuo.dhroid.adapter.ValueFix;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.means.rabbit.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class RabbitValueFix implements ValueFix {

	static Map<String, DisplayImageOptions> imageOptions;

	public static DisplayImageOptions optionsDefault, headOptions,
			carLogoOptions, carBigLogoOptions, bigPicOptions, backOptions;

	static {

		imageOptions = new HashMap<String, DisplayImageOptions>();
		optionsDefault = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.color.line_color)
				.showImageOnFail(R.color.line_color)
				.showImageForEmptyUri(R.color.line_color)
				.cacheInMemory(true).cacheOnDisk(true)
				.resetViewBeforeLoading(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageOptions.put("default", optionsDefault);

		headOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_face)
				.showImageForEmptyUri(R.drawable.icon_face)
				.showImageOnFail(R.drawable.icon_face).cacheInMemory(true)
				.cacheOnDisk(true).resetViewBeforeLoading(true).build();
		imageOptions.put("head", headOptions);

		bigPicOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).resetViewBeforeLoading(true)
				.considerExifParams(false).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		imageOptions.put("big_pic", bigPicOptions);

	}

	@Override
	public Object fix(Object o, String type) {
		if (o == null)
			return null;
		if ("time".equals(type)) {
			return getStandardTime(Long.parseLong(o.toString()),
					"yyyy-MM-dd");
		} else if ("neartime".equals(type)) {
			return converTime(Long.parseLong(o.toString()));
		}
		return o;
	}

	public Object imgDef(String type) {
		return null;
	}

	/**
	 * @param type
	 *            header琛ㄧず澶村儚 grid涓殑澶у浘,hd楂樻竻,saytop璇磋鎯呴〉椤堕儴
	 * @return
	 */

	public static String converTime(long timestamp) {
		long currentSeconds = System.currentTimeMillis();
		long timeGap = (currentSeconds - timestamp) / 1000;// 与现在时间相差秒�?
		Date currentDate = new Date(currentSeconds);
		Date agoDate = new Date(timestamp);
		String timeStr = null;

		Calendar aCalendar = Calendar.getInstance();

		aCalendar.setTime(currentDate);
		int currentDays = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(agoDate);
		int agoDays = aCalendar.get(Calendar.DAY_OF_YEAR);
		// if(currentYear>agoYear) {
		// if()
		// }

		if (currentDays - agoDays >= 2) {// 1天以�?
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(timestamp);
			timeStr = sdf.format(date);
		} else if (currentDays - agoDays == 1) {// 1小时-24小时
			timeStr = "昨天";
		} else if (timeGap > 60 * 60) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date date = new Date(timestamp);
			timeStr = sdf.format(date);
		} else if (timeGap > 60) {// //1分钟-59分钟
			timeStr = timeGap / 60 + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getStandardTime(long timestamp, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = new Date(timestamp*1000);
		sdf.format(date);
		return sdf.format(date);
	}

	public static String delHTMLTag(String htmlStr) {
		// String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>";
		// //瀹氫箟script鐨勬鍒欒〃杈惧紡
		// String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>";
		// //瀹氫箟style鐨勬鍒欒〃杈惧紡
		// String regEx_html="<[^>]+>"; //瀹氫箟HTML鏍囩鐨勬鍒欒〃杈惧紡
		//
		// Pattern
		// p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
		// Matcher m_script=p_script.matcher(htmlStr);
		// htmlStr=m_script.replaceAll(""); //杩囨护script鏍囩
		//
		// Pattern
		// p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
		// Matcher m_style=p_style.matcher(htmlStr);
		// htmlStr=m_style.replaceAll(""); //杩囨护style鏍囩
		//
		// Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
		// Matcher m_html=p_html.matcher(htmlStr);
		// htmlStr=m_html.replaceAll(""); //杩囨护html鏍囩

		if (htmlStr == null || htmlStr.trim().equals("")) {
			return "";
		}
		// 鍘绘帀锟�锟斤拷html鍏冪礌,
		String str = htmlStr.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		return str.replace("&nbsp", ""); // 杩斿洖鏂囨湰瀛楃锟�
	}

	@Override
	public DisplayImageOptions imageOptions(String type) {
		if (type != null) {
			return imageOptions.get(type);

		}
		return imageOptions.get("default");
	}

}
