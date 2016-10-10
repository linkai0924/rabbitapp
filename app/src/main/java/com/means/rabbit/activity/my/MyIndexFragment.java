package com.means.rabbit.activity.my;

import org.json.JSONObject;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import net.duohuo.dhroid.view.BadgeView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.activity.finance.FinancialManagementActivity;
import com.means.rabbit.activity.my.edit.EditInfoActivity;
import com.means.rabbit.activity.my.order.BusinessOrderActivity;
import com.means.rabbit.activity.my.order.MyOrderActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.bean.BackHomeEB;
import com.means.rabbit.bean.User;
import com.means.rabbit.utils.RabbitPerference;
import com.means.rabbit.views.CatPop;

import de.greenrobot.event.EventBus;

public class MyIndexFragment extends Fragment implements OnClickListener {
	static MyIndexFragment instance;

	View mainV;

	LayoutInflater mLayoutInflater;

	LinearLayout editinfoLl, systemmsgLl, business_orderLl, my_orderLl,
			collectLl, shipping_addressLl, integralLl, commentV,
			current_accountLl;

	Button logoutBtn;

	BadgeView msg_countT, order_countT;

	RabbitPerference per;

	public static MyIndexFragment getInstance() {
		if (instance == null) {
			instance = new MyIndexFragment();
		}

		return instance;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainV = inflater.inflate(R.layout.fragment_my, null);
		mLayoutInflater = inflater;
		initView();
		// TODO Auto-generated method stub
		return mainV;
	}

	private void initView() {

		View backV = mainV.findViewById(R.id.backLayout);
		backV.setVisibility(View.GONE);

		TextView titleT = (TextView) mainV.findViewById(R.id.title);
		if (titleT != null) {
			titleT.setText(getString(R.string.my_title));
		}

		editinfoLl = (LinearLayout) mainV.findViewById(R.id.editinfo);
		systemmsgLl = (LinearLayout) mainV.findViewById(R.id.systemmsg);
		business_orderLl = (LinearLayout) mainV
				.findViewById(R.id.business_order);
		my_orderLl = (LinearLayout) mainV.findViewById(R.id.my_order);
		shipping_addressLl = (LinearLayout) mainV
				.findViewById(R.id.shipping_address);
		collectLl = (LinearLayout) mainV.findViewById(R.id.collect);
		integralLl = (LinearLayout) mainV.findViewById(R.id.integral);
		commentV = (LinearLayout) mainV.findViewById(R.id.comment);
		current_accountLl = (LinearLayout) mainV
				.findViewById(R.id.current_account);
		logoutBtn = (Button) mainV.findViewById(R.id.logout);
		editinfoLl.setOnClickListener(this);
		systemmsgLl.setOnClickListener(this);
		business_orderLl.setOnClickListener(this);
		my_orderLl.setOnClickListener(this);
		shipping_addressLl.setOnClickListener(this);
		collectLl.setOnClickListener(this);
		integralLl.setOnClickListener(this);
		commentV.setOnClickListener(this);
		current_accountLl.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);

		msg_countT = (BadgeView) mainV.findViewById(R.id.msg_count);
		msg_countT.hide();
		order_countT = (BadgeView) mainV.findViewById(R.id.order_count);
		order_countT.hide();

		getUserType();

	}

	private void getUserType() {
		// User user = User.getInstance();
		// if (user.getType() == 1) {
		// business_orderLl.setVisibility(View.GONE);
		// mainV.findViewById(R.id.b_line).setVisibility(View.GONE);
		//
		// my_orderLl.setVisibility(View.VISIBLE);
		// mainV.findViewById(R.id.m_line).setVisibility(View.VISIBLE);
		// } else {
		// my_orderLl.setVisibility(View.GONE);
		// mainV.findViewById(R.id.m_line).setVisibility(View.GONE);
		//
		// business_orderLl.setVisibility(View.VISIBLE);
		// mainV.findViewById(R.id.b_line).setVisibility(View.VISIBLE);
		// }
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getinfo();
		getUserType();
	}

	@Override
	public void onClick(View v) {
		Intent it;
		switch (v.getId()) {
		// 编辑资料
		case R.id.editinfo:
			it = new Intent(getActivity(), EditInfoActivity.class);
			startActivity(it);
			break;
		// 系统消息
		case R.id.systemmsg:
			it = new Intent(getActivity(), SystemMsgActivity.class);
			startActivity(it);
			break;
		// 商家订单
		case R.id.business_order:
			it = new Intent(getActivity(), BusinessOrderActivity.class);
			startActivity(it);
			break;
		// 我的订单
		case R.id.my_order:
			it = new Intent(getActivity(), MyOrderActivity.class);
			startActivity(it);
			break;
		// 配送地址
		case R.id.shipping_address:
			it = new Intent(getActivity(), ShippingAddressActivity.class);
			it.putExtra("type", "edit");
			startActivity(it);
			break;
		// 收藏列表
		case R.id.collect:
			it = new Intent(getActivity(), MyCollectActivity.class);
			startActivity(it);
			break;
		// 积分明细
		case R.id.integral:
			it = new Intent(getActivity(), MyIntegralActivity.class);
			startActivity(it);
			break;

		// 我的评论
		case R.id.comment:
			it = new Intent(getActivity(), CommentListActivity.class);
			startActivity(it);
			break;
		// 现金账户
		case R.id.current_account:
			it = new Intent(getActivity(), FinancialManagementActivity.class);
			startActivity(it);
			break;
		// 退出当前用户
		case R.id.logout:
			logout();
			break;
		default:
			break;
		}
	}

	private void logout() {
		DhNet net = new DhNet(new API().logout);
		net.doPostInDialog(new NetTask(getActivity()) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					per = IocContainer.getShare().get(RabbitPerference.class);
					per.clear();
					User.getInstance().setLogin(false);
					EventBus.getDefault().post(new BackHomeEB());
					Intent it = new Intent(getActivity(), LoginActivity.class);
					startActivity(it);
				}
			}
		});
	}

	private void getinfo() {
		DhNet net = new DhNet(new API().userinfo);
		net.doGetInDialog(new NetTask(getActivity()) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					JSONObject jo = response.jSONFromData();
					ViewUtil.bindNetImage(
							(ImageView) mainV.findViewById(R.id.head),
							JSONUtil.getString(jo, "faceimg_s"), "head");
					ViewUtil.bindView(mainV.findViewById(R.id.level),
							JSONUtil.getString(jo, "rolename"));

					int msgcount = JSONUtil.getInt(jo, "msgcount");
					int ordercount = JSONUtil.getInt(jo, "ordercount");
					msg_countT.setVisibility(msgcount == 0 ? View.GONE
							: View.VISIBLE);
					msg_countT.setText(msgcount + "");
					order_countT.setVisibility(ordercount == 0 ? View.GONE
							: View.VISIBLE);
					order_countT.setText(ordercount + "");

				}

			}
		});
	}
}
