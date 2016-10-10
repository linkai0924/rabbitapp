package com.means.rabbit.activity.my;

import org.json.JSONException;
import org.json.JSONObject;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.activity.home.SelectDistrictActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.bean.DistrictEB;

import de.greenrobot.event.EventBus;

/**
 * 新增配送地址
 * 
 * @author dell
 * 
 */
public class AddShippingAddressActivity extends RabbitBaseActivity {

	LinearLayout region_layout;
	TextView regionT;
	EditText addressEt, phoneEt, nameEt, postcodeEt;
	CheckBox checkbox;
	Button saveBtn;

	DistrictEB city;

	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_shipping_address);

		EventBus.getDefault().register(this);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.shippingaddressactivity));

		region_layout = (LinearLayout) findViewById(R.id.region_layout);
		regionT = (TextView) findViewById(R.id.region);
		addressEt = (EditText) findViewById(R.id.address);
		phoneEt = (EditText) findViewById(R.id.phone);
		nameEt = (EditText) findViewById(R.id.name);
		postcodeEt = (EditText) findViewById(R.id.postcode);
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		saveBtn = (Button) findViewById(R.id.save);

		region_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(self, SelectDistrictActivity.class);
				startActivity(it);
			}
		});

		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String data = getIntent().getStringExtra("data");
				if (!TextUtils.isEmpty(data)) {
					editAddress();
				} else {
					addAddress();
				}
			}
		});

		String data = getIntent().getStringExtra("data");
		if (!TextUtils.isEmpty(data)) {
			try {
				JSONObject jo = new JSONObject(data);
				initData(jo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void initData(JSONObject jo) {
		ViewUtil.bindView(regionT, JSONUtil.getString(jo, "areaname"));
		ViewUtil.bindView(addressEt, JSONUtil.getString(jo, "lxaddress"));
		ViewUtil.bindView(phoneEt, JSONUtil.getString(jo, "lxphone"));
		ViewUtil.bindView(nameEt, JSONUtil.getString(jo, "lxname"));
		ViewUtil.bindView(postcodeEt, JSONUtil.getString(jo, "zipcode"));

		if (JSONUtil.getInt(jo, "dft") == 1) {
			checkbox.setChecked(true);
		} else {
			checkbox.setChecked(false);
		}

		city = new DistrictEB();
		city.setCityid(JSONUtil.getString(jo, "cityid"));
		city.setDistrictid(JSONUtil.getString(jo, "areaid"));
		id = JSONUtil.getString(jo, "id");
		// ViewUtil.bindView(addressEt, JSONUtil.getString(jo, "lxaddress"));
		// ViewUtil.bindView(addressEt, JSONUtil.getString(jo, "lxaddress"));

		// it.putExtra("id", JSONUtil.getString(jo, "id"));
		// it.putExtra("lxname", JSONUtil.getString(jo, "lxname"));
		// it.putExtra("lxphone", JSONUtil.getString(jo, "lxphone"));
		// it.putExtra("lxaddress", JSONUtil.getString(jo, "lxaddress"));
	}

	public void onEventMainThread(DistrictEB city) {
		regionT.setText(city.territoryName());
		this.city = city;
	}

	private void addAddress() {
		String region = regionT.getText().toString().trim();
		String address = addressEt.getText().toString().trim();
		String phone = phoneEt.getText().toString().trim();
		String name = nameEt.getText().toString().trim();
		String postcode = postcodeEt.getText().toString().trim();

		if (TextUtils.isEmpty(region)) {
			showToast(getString(R.string.addshippingaddress_input_region));// 请输入地区
			return;
		}
		if (TextUtils.isEmpty(address)) {
			showToast(getString(R.string.addshippingaddress_input_address));// 请输入地址
			return;
		}
		if (TextUtils.isEmpty(phone)) {
			showToast(getString(R.string.addshippingaddress_input_phone));// 请输入手机
			return;
		}
		if (TextUtils.isEmpty(name)) {
			showToast(getString(R.string.addshippingaddress_input_name));// 请输入姓名
			return;
		}
		if (TextUtils.isEmpty(postcode)) {
			showToast(getString(R.string.addshippingaddress_input_postcode));// 请输入邮编
			return;
		}

		DhNet net = new DhNet(new API().addaddress);
		net.addParam("lxname", name);
		net.addParam("lxphone", phone);
		net.addParam("lxaddress", address);
		net.addParam("zipcode", postcode);
		net.addParam("cityid", city.getCityid());
		net.addParam("areaid", city.getDistrictid());
		net.addParam("dft", checkbox.isChecked() ? 1 : 0);
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					Intent it = getIntent();
					setResult(Activity.RESULT_OK, it);
					finish();
				}
			}
		});

	}

	private void editAddress() {
		String region = regionT.getText().toString().trim();
		String address = addressEt.getText().toString().trim();
		String phone = phoneEt.getText().toString().trim();
		String name = nameEt.getText().toString().trim();
		String postcode = postcodeEt.getText().toString().trim();

		if (TextUtils.isEmpty(region)) {
			showToast(getString(R.string.addshippingaddress_input_region));// 请输入地区
			return;
		}
		if (TextUtils.isEmpty(address)) {
			showToast(getString(R.string.addshippingaddress_input_address));// 请输入地址
			return;
		}
		if (TextUtils.isEmpty(phone)) {
			showToast(getString(R.string.addshippingaddress_input_phone));// 请输入手机
			return;
		}
		if (TextUtils.isEmpty(name)) {
			showToast(getString(R.string.addshippingaddress_input_name));// 请输入姓名
			return;
		}
		if (TextUtils.isEmpty(postcode)) {
			showToast(getString(R.string.addshippingaddress_input_postcode));// 请输入邮编
			return;
		}

		DhNet net = new DhNet(new API().edit_addaddress);
		net.addParam("lxname", name);
		net.addParam("lxphone", phone);
		net.addParam("lxaddress", address);
		net.addParam("zipcode", postcode);
		net.addParam("cityid", city.getCityid());
		net.addParam("areaid", city.getDistrictid());
		net.addParam("dft", checkbox.isChecked() ? 1 : 0);
		net.addParam("id", id);
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					Intent it = getIntent();
					setResult(Activity.RESULT_OK, it);
					finish();
				}
			}
		});
	}
}
