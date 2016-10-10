package com.means.rabbit.activity.my;

import java.util.Date;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RatingBar;

import com.means.rabbit.R;
import com.means.rabbit.activity.merchants.ShopDetailActivity;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.DateUtils;
import com.means.rabbit.views.RefreshListViewAndMore;
import com.means.rabbit.views.RoundImageView;

public class CommentListActivity extends RabbitBaseActivity {

	RefreshListViewAndMore listV;

	ListView contentListV;

	NetJSONAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.comment_list));
		listV = (RefreshListViewAndMore) findViewById(R.id.my_listview);
		contentListV = listV.getListView();
		adapter = new NetJSONAdapter(new API().commentuserlist, self,
				R.layout.item_comment);
		adapter.fromWhat("list");
		adapter.addField("username", R.id.name);
		adapter.addField("content", R.id.content);
		adapter.addField(new FieldMap("dateline", R.id.time) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				JSONObject jso = (JSONObject) jo;
				ViewUtil.bindNetImage((RoundImageView) itemV.findViewById(R.id.head), JSONUtil.getString(jso, "faceimg_s"), "head");
				((RatingBar)itemV.findViewById(R.id.ratingbar)).setRating(JSONUtil.getFloat(jso,"score"));
				return DateUtils.dateToStr(new Date(
						Long.parseLong(o.toString()) * 1000));
			}
		});
		listV.setAdapter(adapter);
//		contentListV.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				JSONObject jso = (JSONObject) adapter.getItem(position);
//				String shopId = JSONUtil.getString(jso, "id");
//				Intent it = new Intent(self, ShopDetailActivity.class);
//				it.putExtra("shopId",shopId );
//				startActivity(it);
//
//			}
//		});

	}

}
