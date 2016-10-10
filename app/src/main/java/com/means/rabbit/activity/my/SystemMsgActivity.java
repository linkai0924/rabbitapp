package com.means.rabbit.activity.my;

import java.util.Date;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.DateUtils;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.dialog.DelectDialog;
import com.means.rabbit.views.dialog.DelectDialog.OnDelectResultListener;

/**
 * 系统消息
 * 
 * @author dell
 * 
 */
public class SystemMsgActivity extends RabbitBaseActivity {
	RefreshListViewAndMore listV;
	ListView contentListV;

	NetJSONAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_msg);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.systemmsg));

		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		String url = new API().msglist;
		contentListV = listV.getListView();

		adapter = new NetJSONAdapter(url, self, R.layout.item_system_msg);
		adapter.fromWhat("list");
		adapter.addField("content", R.id.content);
		adapter.addField(new FieldMap("dateline", R.id.dateline) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {

				JSONObject data = (JSONObject) jo;

				ImageView img_isread = (ImageView) itemV
						.findViewById(R.id.isread);
				if (JSONUtil.getString(data, "isread").equals("1")) {
					img_isread.setVisibility(View.INVISIBLE);
				} else {
					img_isread.setVisibility(View.VISIBLE);
				}

				return DateUtils.dateToStr(new Date(
						Long.parseLong(o.toString()) * 1000));
			}
		});
		listV.setAdapter(adapter);

		contentListV
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						final int index = position;
						final JSONObject jo = (JSONObject) adapter
								.getItem(position);
						DelectDialog dialog = new DelectDialog(self);
						dialog.show();
						dialog.setOnDelectResultListener(new OnDelectResultListener() {

							@Override
							public void onResult() {
								DhNet net = new DhNet(new API().msgdelete);
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
						// TODO Auto-generated method stub
						return false;
					}
				});
	}
}
