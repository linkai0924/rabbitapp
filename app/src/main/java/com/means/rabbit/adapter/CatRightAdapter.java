package com.means.rabbit.adapter;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.means.rabbit.R;

public class CatRightAdapter extends BaseAdapter {
	Context mContex;
	LayoutInflater inflater;

	JSONArray jsa;

	public CatRightAdapter(Context mContex) {
		this.mContex = mContex;
		inflater = LayoutInflater.from(mContex);
	}

	public void setData(JSONArray jsa) {
		this.jsa = jsa;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (jsa == null) {
			return 0;
		}
		return jsa.length();
	}

	@Override
	public JSONObject getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return jsa.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.item_city_list, null);
		JSONObject jo = getItem(position);
		ViewUtil.bindView(convertView.findViewById(R.id.name),
				JSONUtil.getString(jo, "name"));
		// TODO Auto-generated method stub
		return convertView;
	}
}
