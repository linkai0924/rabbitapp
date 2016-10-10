package com.means.rabbit.activity.more;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;
import com.means.rabbit.views.MyToast;

/**
 * 
 * 意见反馈
 * 
 * @author Administrator
 * 
 */
public class FeedbackActivity extends RabbitBaseActivity {

	private MyToast toastCommom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.feedback));

		toastCommom = MyToast.createToastConfig();

		findViewById(R.id.submit).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						submit();
					}

				});
	}

	private void submit() {
		String content = ((EditText) findViewById(R.id.content)).getText()
				.toString();
		if (TextUtils.isEmpty(content)) {
			toastCommom.ToastShow(self,
					(ViewGroup) findViewById(R.id.toast_layout_root),
					getString(R.string.post_comment_txt));
			return;
		}
		DhNet net = new DhNet(new API().feedback);
		net.addParam("content", content);
		net.doPostInDialog(new NetTask(self) {

			@Override
			public void doInUI(Response response, Integer transfer) {

				if (response.isSuccess()) {
					showToast(getString(R.string.submit_success));
					finish();
				}

			}
		});

	}
}
