package com.means.rabbit.activity.comment;

import java.io.File;
import java.util.List;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ImageUtil;
import net.duohuo.dhroid.util.PhotoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.means.rabbit.R;
import com.means.rabbit.activity.main.PhotoSelectorActivity;
import com.means.rabbit.adapter.PostCommentPicAdapter;
import com.means.rabbit.api.API;
import com.means.rabbit.api.Constant;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.photo.model.PhotoModel;
import com.means.rabbit.views.MyToast;
import com.means.rabbit.views.dialog.DateTimerDialog;
import com.means.rabbit.views.dialog.DateTimerDialog.OnDateTimerResultListener;
import com.means.rabbit.views.pop.SelectPicturePop;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * 发布评论
 * 
 * @author Administrator
 * 
 */
public class PostCommentMainActivity extends RabbitBaseActivity {

	private RecyclerView recyclerView;
	private PostCommentPicAdapter mAdapter;
	private EditText contentEd;
	private RatingBar ratingBar;
	private Button submitBtn;

	private MyToast toastCommom;

	// 图片缓存根目录
	private File mCacheDir;
	private String mPhotoPath;

	// 上传图片总数
	private int uploadPhotoCount = 0;
	private int nowPhotoCount = 0;

	// 图片信息
	private JSONArray picArray;

	// 团购/代购ID , 类型
	private int contentid, type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_comment_main);

		mCacheDir = new File(getExternalCacheDir(), "Rabbit");
		mCacheDir.mkdirs();
	}

	@Override
	public void initView() {
		contentid = getIntent().getIntExtra("contentid", -1);
		type = getIntent().getIntExtra("type", -1);
		// TODO Auto-generated method stub
		setTitle(getString(R.string.post_comment));
		picArray = new JSONArray();

		toastCommom = MyToast.createToastConfig();
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		contentEd = (EditText) findViewById(R.id.content);
		ratingBar = (RatingBar) findViewById(R.id.ratingbar);
		submitBtn = (Button) findViewById(R.id.submit);

		submitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});

		LinearLayoutManager layoutManager = new LinearLayoutManager(self);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mAdapter = new PostCommentPicAdapter(self);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(mAdapter);
	}

	public void showPop() {
		mPhotoPath = new File(mCacheDir, System.currentTimeMillis() + ".jpg")
				.getAbsolutePath();
		final File tempFile = new File(mPhotoPath);
		final SelectPicturePop pop = SelectPicturePop.getInstance(self, -1);
		pop.setPhotoGraphListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent getImageByCamera = new Intent(
						"android.media.action.IMAGE_CAPTURE");
				getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(tempFile));
				startActivityForResult(getImageByCamera, Constant.TAKE_PHOTO);
				pop.dismiss();
			}
		});
		pop.setAlbumListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(self,
				// PhotoSelectorActivity.class);
				// intent.putExtra(PhotoSelectorActivity.KEY_MAX, 9);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				// startActivityForResult(intent, Constant.PICK_PHOTO);
				// pop.dismiss();

				Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
				getImage.addCategory(Intent.CATEGORY_OPENABLE);
				getImage.setType("image/jpeg");
				startActivityForResult(getImage, Constant.PICK_PHOTO);

			}
		});
		pop.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.PICK_PHOTO:
				PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mPhotoPath,
						data, 1, 1, 1000);
				break;
			case Constant.ZOOM_PIC:
				uploadPic(mPhotoPath);
				break;

			// case Constant.PICK_PHOTO:
			// if (data != null && data.getExtras() != null) {
			// List<PhotoModel> photos = (List<PhotoModel>) data
			// .getExtras().getSerializable("photos");
			// if (photos == null || photos.isEmpty()) {
			// showToast("没有选择图片!");
			// } else {
			// uploadPhotoCount += photos.size();
			// for (int i = 0; i < photos.size(); i++) {
			// String newPhotoPath = new File(mCacheDir,
			// System.currentTimeMillis() + ".jpg")
			// .getAbsolutePath();
			// Bitmap btp = PhotoUtil.getLocalImage(new File(
			// photos.get(i).getOriginalPath()));
			// PhotoUtil.saveLocalImageSquare(btp, new File(
			// newPhotoPath));
			// addPhoto(newPhotoPath);
			// }
			// }
			// }
			// break;
			case Constant.TAKE_PHOTO:

				String newPath = new File(mCacheDir, System.currentTimeMillis()
						+ ".jpg").getAbsolutePath();
				String path = PhotoUtil.onPhotoFromCamera(self,
						Constant.ZOOM_PIC, mPhotoPath, 1, 1, 1000, newPath);
				mPhotoPath = path;
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 添加图片
	private void addPhoto(String path) {
		nowPhotoCount++;

		try {
			JSONObject jo = new JSONObject();
			jo.put("id", nowPhotoCount);
			jo.put("path", path);
			picArray.put(jo);
			mAdapter.setData(picArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void uploadPic(String path) {
		showProgressDialog(getString(R.string.uploading));
		DhNet net = new DhNet(new API().uploadImg);
		net.upload("upfile", new File(path), new NetTask(self) {
			@Override
			public void doInUI(Response response, Integer transfer) {
				hidenProgressDialog();
				if (response.isSuccess()
						&& Integer.parseInt(response.getBundle("proccess")
								.toString()) == 100) {
					hidenProgressDialog();

					JSONObject jo1 = response.jSONFromData();
					JSONObject jo = new JSONObject();
					try {
						jo.put("id", JSONUtil.getString(jo1, "id"));
						jo.put("path", JSONUtil.getString(jo1, "path"));
						jo.put("mPhotoPath", mPhotoPath);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					picArray.put(jo);
					mAdapter.setData(picArray);

				}
			}

		});
	}

	// 删除图片
	@SuppressLint("NewApi")
	public void delectPhoto(int id) {
		for (int i = 0; i < picArray.length(); i++) {
			try {
				JSONObject jo = (JSONObject) picArray.get(i);
				if (id == JSONUtil.getInt(jo, "id")) {
					picArray.remove(i);
					mAdapter.setData(picArray);
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// 提交
	private void submit() {
		String content = contentEd.getText().toString();
		if (TextUtils.isEmpty(content)) {
			toastCommom.ToastShow(self,
					(ViewGroup) findViewById(R.id.toast_layout_root),
					getString(R.string.post_comment_txt));
			return;
		}

		DhNet net = new DhNet(new API().addcomment);
		net.addParam("contentid", getIntent().getStringExtra("contentid")); // 团购/代购ID
		net.addParam("content", content);
		net.addParam("score", ratingBar.getRating());
		net.addParam("type", getIntent().getStringExtra("type")); // 类型1是团购商家2是酒店3是代购
		net.addParam("slidepic", picArray.toString());
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if (response.isSuccess()) {
					finish();
					Intent it = getIntent();
					setResult(Activity.RESULT_OK, it);
					showToast(getString(R.string.release_success));
				}
			}
		});
	}

}
