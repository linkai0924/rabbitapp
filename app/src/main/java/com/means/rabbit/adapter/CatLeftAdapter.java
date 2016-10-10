package com.means.rabbit.adapter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.means.rabbit.R;

public class CatLeftAdapter extends BaseAdapter {
	Context mContex;
	LayoutInflater inflater;

	JSONArray jsa;

	int currentPosition = -1;

	public CatLeftAdapter(Context mContex) {
		this.mContex = mContex;
		inflater = LayoutInflater.from(mContex);
	}

	public void setData(JSONArray jsa) {
		this.jsa = jsa;
		notifyDataSetChanged();
	}

	public void setCurrentPosition(int position) {
		this.currentPosition = position;
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

		convertView = inflater.inflate(R.layout.item_country_list, null);
		if (currentPosition == position) {
			convertView.setBackgroundColor(mContex.getResources().getColor(
					R.color.white));
		} else {
			convertView.setBackgroundColor(mContex.getResources().getColor(
					R.color.app_bg_color));
		}
		JSONObject jo = getItem(position);
		ViewUtil.bindView(convertView.findViewById(R.id.name),
				JSONUtil.getString(jo, "name"));
		// TODO Auto-generated method stub
		return convertView;
	}

}
