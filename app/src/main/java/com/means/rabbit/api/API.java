package com.means.rabbit.api;

import com.means.rabbit.RabbitApplication;

public class API {

	public String text = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/hotel/contentlist";

	public String hotelList = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/hotel/contentlist";

	/** 用户注册 */
	public String register = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/registaction";

	/** 获取验证码 */
	public String mobilecode = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/mobilecode";

	/** 用户登录 */
	public String login = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/login";

	/** 退出登录 */
	public String logout = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/logout";

	/** 修改密码 */
	public String resetpswdbyphone = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/user/resetpswdbyphone";

	/** 商家/酒店列表猜你喜欢列表 */
	public String guesslikelist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/contentlist";

	/** 修改昵称 */
	public String editaction = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/editaction";

	/** 旅行小蜜/紧急求助资讯列表分页 */
	public String contentlist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/cms/contentlist";

	/** 旅行小蜜/紧急求助资详情 */
	public String contentview = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/cms/contentview";

	/** 城市列表 */
	public String citylist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/citylist";

	/** 代购列表分页 */
	public String dgcontentlist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/dgcontentlist";

	/** 商家区域列表 */
	public String arealist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/arealist";

	/** 商家分类列表 */
	public String catlist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/catlist";

	/** 代购品牌分类 */
	public String brandlist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/labellist";

	/** 酒店详情 */
	public String hoteldetail = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/hotel/contentview";

	/** 酒店附近团购 */
	public String hotelDetailNearTuangou = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/shop/itemlist";

	/** 酒店预订列表 */
	public String hotelDetailOrderList = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/hotel/itemlist";

	/** 美食列表 */
	public String foodList = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/contentlist";

	/** 消息列表分页 */
	public String msglist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/msglist";

	/** 消息删除 */
	public String msgdelete = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/msgdelete";

	/** 关于我们 */
	public String aboutdetail = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/aboutdetail";

	/** 修改手机 */
	public String changephone = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/changephone";

	/** 修改头像 */
	public String editfceimg = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/editfceimg";

	/** 商家详情 */
	public String shopDetail = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/contentview";

	/** 团购/代购/酒店商家的发布评论 */
	public String addcomment = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/addcomment";

	/** 评论列表 */
	public String commentlist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/commentlist";

	/**
	 * 
	 * 
	 */
	public String tuangouDetail = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/itemview";

	/** 财务分页明细 */
	public String accountlog = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/accountlog";

	/** 申请提现 */
	public String cashapply = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/cashapply";

	/** 我的财务 */
	public String accountview = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/accountview";

	/** 我的订单列表分页 */
	public String orderlist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/hotel/orderlist";

	/** 酒店下单 */
	public String addHotelOrder = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/hotel/orderadd";

	/** 酒店订单预览 */
	public String preHotelOrder = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/hotel/orderpreview";
	public String addOrder = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/hotel/orderadd";

	/** 酒店订单预览 */

	/** 酒店订单详情 */
	public String hotelOrderDetail = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/hotel/orderview";

	/** 团购订单预览 */
	public String preTuangouOrder = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/shop/orderpreview";

	/** 团购订单下单 */
	public String addTuangouOrder = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/shop/orderadd";

	/** 团购订单详情 */
	public String tuangouOrderDetail = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/shop/orderview";

	/** 首页猜你喜欢分页列表 */
	public String likelist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/likelist";

	/** 首页幻灯+中间内容列表 */
	public String configlist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/configlist";

	/** 收获地址列表分页 */
	public String addressuserlist = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/cart/addressuserlist";

	/** 添加收获地址 */
	public String addaddress = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/cart/addaddress";

	/** 编辑收货地址 */
	public String edit_addaddress = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/cart/editaddress";

	/** 取消订单 */
	public String cancelOrder = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/ordercancel";

	/** 代购订单预览 */
	public String preDaigouOrder = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/dgorderpreview";

	/** 代购订单下单 */
	public String addDaigouOrder = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/dgorderadd";

	/** 代购订单预览 */
	public String daigouOrderDetail = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/shop/dgorderview";

	/** 代购详情 */
	public String daigouDetail = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/dgview";

	/** 支付 */
	public String pay = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/orderpay";

	/** 获取个人信息 */
	public String userinfo = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/info";

	/** 优惠买单 */
	public String youhuibuy = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/shoppay";

	/** 我的积分 */
	public String paycredit = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/paycredit";

	/** 翻译 */
	public String translation = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/fanyi";

	/** 首页搜索 */
	public String search = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/search";

	/** 首页搜索 */
	public String uploadImg = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/attachment/uploadimage";

	/** 商家订单列表 */
	public String orderbusinesslist = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/hotel/orderbusinesslist";

	/** 商家订单列详情 */
	public String orderbusinessdetail = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/shop/orderbusinessview";

	/** 使用消费码 */
	public String usecode = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/ercodepay";

	/** 积分记录 */
	public String creditloglist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/shop/creditloglist";

	/** 全部评论 */
	public String commentuserlist = RabbitApplication.getInstance()
			.getBaseUrl() + "/home/shop/commentuserlist";

	/** 获取城市 */
	public String getcity = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/getcityid";

	/** 列表广告位 */
	public String listad = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/adtoplist";

	/** 收藏 */
	public String collect = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/collect";

	/** 取消收藏 */
	public String collectdel = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/collectdel";

	/** 收藏列表 */
	public String collectlist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/collectlist";

	/** 版本更新 */
	public String update = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/updateconfig";

	/** 版本更新 */
	public String emailcode = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/emailcode";

	/** 修改邮箱 */
	public String changgeemail = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/changeemail";
	
	/** 重置密码*/
	public String resetpswdbyemail = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/user/resetpswdbyemail";
	
	/**删除地址*/
	public String address_del = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/cart/addressdelete";
	
	
	/**反馈*/
	public String feedback = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/feedback";
	
	/**帮助列表*/
	public String helplist = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/helplist";
	
	/**帮助详情*/
	public String helpdetail = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/index/helpview";
	
	/**paypal支付*/
	public String paypal = RabbitApplication.getInstance().getBaseUrl()
			+ "/home/account/paymentset";
}
