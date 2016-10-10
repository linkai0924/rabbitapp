package com.means.rabbit.adapter;

import java.io.File;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.means.rabbit.R;
import com.means.rabbit.activity.comment.PostCommentMainActivity;

public class PostCommentPicAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_FOOTER = 0;

	private static final int TYPE_ITEM = 1;

	private final Context mContext;

	private JSONArray data;

	public PostCommentPicAdapter(Context context) {
		mContext = context;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		// 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView

		if (viewType == TYPE_ITEM) {
			// inflate your layout and pass it to view holder
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.item_post_comment, parent, false);
			return new VHItem(view);
		} else if (viewType == TYPE_FOOTER) {
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.item_post_comment_footview, parent, false);
			// inflate your layout and pass it to view holder
			return new VHFooter(view);
		}
		throw new RuntimeException("there is no type that matches the type "
				+ viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

		if (holder instanceof VHItem) {
			final JSONObject jo = getItem(position);
			Bitmap bm = PhotoUtil.getLocalImage(new File(JSONUtil.getString(jo,
					"mPhotoPath")));
			((VHItem) holder).photo.setImageBitmap(bm);
			((VHItem) holder).photo
					.setOnLongClickListener(new View.OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							((PostCommentMainActivity) mContext)
									.delectPhoto(JSONUtil.getInt(jo, "id"));
							return false;
						}
					});
			// Log.d(">>>>>>>>>>>photioid:   ", JSONUtil.getInt(jo, "id")+"");
		} else if (holder instanceof VHFooter) {
			((VHFooter) holder).addphoto
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							((PostCommentMainActivity) mContext).showPop();
							// Toast.makeText(mContext, "添加", 0).show();
						}
					});
			// cast holder to VHHeader and set data for header.
		}

	}

	@Override
	public int getItemCount() {
		if (data == null) {
			return 1;
		} else if (data.length() < 4) {
			return data.length() + 1;
		} else {
			return 4;
		}
	}

	public void setData(JSONArray data) {
		this.data = data;
		notifyDataSetChanged();
	}

	public JSONObject getItem(int position) {
		if (data == null) {
			return null;
		}
		try {
			return (JSONObject) data.get(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionFooter(position))
			return TYPE_FOOTER;

		return TYPE_ITEM;
	}

	private boolean isPositionFooter(int position) {

		if (data != null && data.length() == 4) {
			return false;
		} else if (getItemCount() == position + 1) {
			return true;
		}

		return false;
	}

	class VHItem extends RecyclerView.ViewHolder {

		ImageView photo;

		public VHItem(View itemView) {

			super(itemView);
			photo = (ImageView) itemView.findViewById(R.id.photo);
		}

	}

	class VHFooter extends RecyclerView.ViewHolder {

		ImageView addphoto;

		public VHFooter(View itemView) {

			super(itemView);
			addphoto = (ImageView) itemView.findViewById(R.id.addphoto);
		}

	}

}
