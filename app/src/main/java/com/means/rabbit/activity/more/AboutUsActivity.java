package com.means.rabbit.activity.more;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

public class AboutUsActivity extends RabbitBaseActivity {

    TextView titleT, contentT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        setTitle(getString(R.string.about_us));

        titleT = (TextView) findViewById(R.id.title_txt);
        contentT = (TextView) findViewById(R.id.content);

        DhNet net = new DhNet(new API().aboutdetail);
        net.doGetInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                // TODO Auto-generated method stub
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    titleT.setText(JSONUtil.getString(jo, "title"));
                    contentT.setText(Html.fromHtml(JSONUtil.getString(jo, "content")));
                }
            }
        });
    }
}
