package com.means.rabbit.activity.my.edit;

import java.io.File;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ImageUtil;
import net.duohuo.dhroid.util.PhotoUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.RabbitApplication;
import com.means.rabbit.api.API;
import com.means.rabbit.api.Constant;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.utils.RabbitPerference;
import com.means.rabbit.views.RoundImageView;
import com.means.rabbit.views.dialog.ReviseHeadDialog;
import com.means.rabbit.views.dialog.ReviseHeadDialog.OnHeadResultListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 编辑资料
 * 
 * @author dell
 * 
 */
public class EditInfoActivity extends RabbitBaseActivity implements
		OnClickListener {

	LinearLayout edit_nicknameV, edit_passwordV, edit_phoneV, edit_headV,
			edit_emailV;
	TextView nicknameT, passwordT, phoneT, emailT;
	RoundImageView headI;

	private final int NICKNAMECODE = 1;
	private final int PASSWORDECODE = 2;
	private final int PHONECODE = 3;
	private final int EMAILCODE = 4;

	RabbitPerference per;

	// 图片缓存根目录
	private File mCacheDir;
	private String mPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_info);
	}

	@Override
	public void initView() {

		if (RabbitApplication.getInstance().getisPhone()) {
			findViewById(R.id.edit_phone).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.edit_email).setVisibility(View.VISIBLE);
		}

		mCacheDir = new File(getExternalCacheDir(), "Rabbit");
		mCacheDir.mkdirs();
		// TODO Auto-generated method stub
		setTitle(getString(R.string.editinfo));
		per = IocContainer.getShare().get(RabbitPerference.class);
		per.load();

		nicknameT = (TextView) findViewById(R.id.nickname);
		passwordT = (TextView) findViewById(R.id.password);
		phoneT = (TextView) findViewById(R.id.phone);
		headI = (RoundImageView) findViewById(R.id.head);
		emailT = (TextView) findViewById(R.id.email);
		nicknameT.setText(per.getName());
		passwordT.setText(per.getPassword());
		phoneT.setText(per.getPhone());
		// boolean a = ImageLoader.getInstance().getDiskCache()
		// .remove(per.getFaceimg_s());
		// Bitmap b = ImageLoader.getInstance().getMemoryCache()
		// .remove(per.getFaceimg_s());
		ViewUtil.bindNetImage(headI, per.getFaceimg_s(), "head");

		edit_nicknameV = (LinearLayout) findViewById(R.id.edit_nickname);
		edit_passwordV = (LinearLayout) findViewById(R.id.edit_password);
		edit_phoneV = (LinearLayout) findViewById(R.id.edit_phone);
		edit_headV = (LinearLayout) findViewById(R.id.edit_head);
		edit_emailV = (LinearLayout) findViewById(R.id.edit_email);

		edit_nicknameV.setOnClickListener(this);
		edit_passwordV.setOnClickListener(this);
		edit_phoneV.setOnClickListener(this);
		edit_headV.setOnClickListener(this);
		edit_emailV.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent it;
		switch (v.getId()) {
		case R.id.edit_nickname:
			it = new Intent(self, EditNickNameActivity.class);
			it.putExtra("nickname", nicknameT.getText().toString());
			startActivityForResult(it, NICKNAMECODE);
			break;

		case R.id.edit_password:
			it = new Intent(self, ForgetPswdActivity.class);
			startActivityForResult(it, PASSWORDECODE);
			break;

		case R.id.edit_phone:
			it = new Intent(self, EditPhoneActivity.class);
			startActivityForResult(it, PHONECODE);
			break;

		case R.id.edit_email:
			it = new Intent(self, EditEmailActivity.class);
			startActivityForResult(it, EMAILCODE);
			break;
		case R.id.edit_head:
			ReviseHeadDialog dialog = new ReviseHeadDialog(self);
			dialog.setOnHeadResultListenerr(new OnHeadResultListener() {

				@Override
				public void onResult(int result) {
					mPhotoPath = new File(mCacheDir, System.currentTimeMillis()
							+ ".jpg").getAbsolutePath();
					System.out.println("mPhotoPath=============" + mPhotoPath);
					final File tempFile = new File(mPhotoPath);
					// 拍照
					if (0 == result) {
						Intent getImageByCamera = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(tempFile));
						startActivityForResult(getImageByCamera,
								Constant.TAKE_PHOTO);
						// 相册
					} else if (1 == result) {
						Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
						getImage.addCategory(Intent.CATEGORY_OPENABLE);
						getImage.setType("image/jpeg");
						startActivityForResult(getImage, Constant.PICK_PHOTO);
					}
				}
			});
			dialog.show();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK) {
			switch (arg0) {
			case Constant.TAKE_PHOTO:
				String newPath = new File(mCacheDir, System.currentTimeMillis()
						+ ".jpg").getAbsolutePath();
				String path = PhotoUtil.onPhotoFromCamera(self,
						Constant.ZOOM_PIC, mPhotoPath, 1, 1, 1000, newPath);
				mPhotoPath = path;
				break;
			case Constant.PICK_PHOTO:
				PhotoUtil.onPhotoFromPick(self, Constant.ZOOM_PIC, mPhotoPath,
						arg2, 1, 1, 1000);
				break;
			case Constant.ZOOM_PIC:
				uploadHead(mPhotoPath);
				break;
			case NICKNAMECODE:
				String nickname = arg2.getStringExtra("nickname");
				nicknameT.setText(nickname);
				per.setName(nickname);
				break;
			case PASSWORDECODE:
				String password = arg2.getStringExtra("password");
				nicknameT.setText(password);
				per.setPassword(password);
				break;
			case PHONECODE:
				String phone = arg2.getStringExtra("phone");
				phoneT.setText(phone);
				per.setPhone(phone);
				break;

			case EMAILCODE:
				String email = arg2.getStringExtra("email");
				emailT.setText(email);
				per.setEmail(email);
				break;
			default:
				break;
			}
			per.commit();
		}
	}

	private void uploadHead(String path) {

		// Bitmap bmp = PhotoUtil.getLocalImage(new File(path));
		DhNet net = new DhNet(new API().editfceimg);
		net.upload("upfile", new File(path), new NetTask(self) {
			@Override
			public void doInUI(Response response, Integer transfer) {
				hidenProgressDialog();
				if (response.isSuccess()
						&& Integer.parseInt(response.getBundle("proccess")
								.toString()) == 100) {
					System.out.println(response.toString());
					Bitmap bmp = PhotoUtil.getLocalImage(new File(mPhotoPath));
					headI.setImageBitmap(ImageUtil.toRoundCorner(bmp, 1000));
					showToast(getString(R.string.editinfo_head_success));
					JSONObject jo = response.jSONFromData();
					String head_url = per.getFaceimg_s();
					ImageLoader.getInstance().getMemoryCache().clear();
					ImageLoader.getInstance().getDiskCache().clear();
					per.commit();
				}
			}

		});
	}
}
