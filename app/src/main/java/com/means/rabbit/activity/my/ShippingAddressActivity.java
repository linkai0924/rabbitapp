package com.means.rabbit.activity.my;

import org.json.JSONObject;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.dialog.DelectDialog;
import com.means.rabbit.views.dialog.DelectDialog.OnDelectResultListener;

/**
 * 配送地址
 * 
 * @author dell
 * 
 */
public class ShippingAddressActivity extends RabbitBaseActivity {
	RefreshListViewAndMore listV;
	ListView contentListV;

	NetJSONAdapter adapter;

	Button addaddressBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shipping_address);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.shippingaddressactivity));

		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		String url = new API().addressuserlist;
		contentListV = listV.getListView();

		adapter = new NetJSONAdapter(url, self, R.layout.item_shipping_address);
		adapter.fromWhat("list");
		adapter.addField("lxname", R.id.lxname);
		adapter.addField("lxphone", R.id.lxphone);
		adapter.addField("lxaddress", R.id.lxaddress);
		adapter.addField(new FieldMap("dft", R.id.dft) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				if (o.toString().equals("1")) {
					itemV.findViewById(R.id.dft).setVisibility(View.VISIBLE);
				} else {
					itemV.findViewById(R.id.dft).setVisibility(View.INVISIBLE);
				}

				return getString(R.string.item_shippingaddress_default);
			}
		});
		listV.setAdapter(adapter);
		contentListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (TextUtils.isEmpty(getIntent().getStringExtra("type"))) {
					JSONObject jo = adapter.getTItem(position);
					Intent it = getIntent();
					it.putExtra("areaname", JSONUtil.getString(jo, "areaname"));
					it.putExtra("id", JSONUtil.getString(jo, "id"));
					it.putExtra("lxname", JSONUtil.getString(jo, "lxname"));
					it.putExtra("lxphone", JSONUtil.getString(jo, "lxphone"));
					it.putExtra("lxaddress",
							JSONUtil.getString(jo, "lxaddress"));
					setResult(Activity.RESULT_OK, it);
					finish();
				} else {
					JSONObject jo = adapter.getTItem(position);
					Intent it = new Intent(self,
							AddShippingAddressActivity.class);
					it.putExtra("data", jo.toString());
					startActivityForResult(it, 1);
				}
			}

		});
		contentListV.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int index = position;
				final JSONObject jo = adapter.getTItem(position);
				DelectDialog dialog = new DelectDialog(self);
				dialog.show();
				dialog.setOnDelectResultListener(new OnDelectResultListener() {

					@Override
					public void onResult() {
						DhNet net = new DhNet(new API().address_del);
						net.addParam("id", JSONUtil.getString(jo, "id"));
						net.doPost(new NetTask(self) {

							@Override
							public void doInUI(Response response,
									Integer transfer) {
								// TODO Auto-generated method stub
								if (response.isSuccess()) {
									adapter.remove(index);
									adapter.notifyDataSetChanged();
								}
							}
						});
					}
				});
				return true;
			}
		});

		addaddressBtn = (Button) findViewById(R.id.addaddress);

		addaddressBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// 新增地址
				Intent it = new Intent(self, AddShippingAddressActivity.class);
				startActivityForResult(it, 1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);

		if (resultCode == Activity.RESULT_OK) {
			listV.refresh();
		}

	}
}
