package com.means.rabbit.base;

import net.duohuo.dhroid.ioc.IocContainer;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.means.rabbit.R;
import com.means.rabbit.activity.home.SelectCityActivity;
import com.means.rabbit.activity.home.TranslateActivity;
import com.means.rabbit.activity.main.MainActivity;
import com.means.rabbit.activity.main.SearchActivity;
import com.means.rabbit.utils.RabbitPerference;

public class RabbitBaseFragment extends Fragment {

	View city_layoutV;

	View search_layoutV;

	ImageView translateI;

	public void initTitleBar(View mainV) {
		TextView cityT = (TextView) mainV.findViewById(R.id.city);

		RabbitPerference per = IocContainer.getShare().get(
				RabbitPerference.class);
		per.load();

		cityT.setText(per.cityname);

		city_layoutV = (View) mainV.findViewById(R.id.city_layout);
		city_layoutV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(getActivity(), SelectCityActivity.class);
				startActivity(it);
			}
		});

		search_layoutV = mainV.findViewById(R.id.search_layout);
		search_layoutV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(getActivity(), SearchActivity.class);
				startActivity(it);
			}
		});
		translateI = (ImageView) mainV.findViewById(R.id.pic_translate);
		translateI.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(getActivity(), TranslateActivity.class);
				startActivity(it);
			}
		});

		mainV.findViewById(R.id.top_erweima).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setAction(Intents.Scan.ACTION);
						// intent.putExtra(Intents.Scan.MODE,
						// Intents.Scan.QR_CODE_MODE);
						intent.putExtra(Intents.Scan.CHARACTER_SET, "UTF-8");
						intent.putExtra(Intents.Scan.WIDTH, 600);
						intent.putExtra(Intents.Scan.HEIGHT, 600);
						// intent.putExtra(Intents.Scan.PROMPT_MESSAGE,
						// "type your prompt message");
						intent.setClass(getActivity(), CaptureActivity.class);
						getActivity().startActivityForResult(intent,
								MainActivity.REQUEST_CODE);
					}
				});
	}

}
