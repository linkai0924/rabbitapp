package com.means.rabbit.activity.finance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.means.rabbit.R;
import com.means.rabbit.api.API;
import com.means.rabbit.base.RabbitBaseActivity;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

/**
 * 财务管理
 *
 * @author Administrator
 */
public class FinancialManagementActivity extends RabbitBaseActivity implements
        OnClickListener {

    final int Recharge = 101;
    TextView balanceT;
    LinearLayout rechargeV, finance_detailV, withdrawV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_management);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.financial_management));

        balanceT = (TextView) findViewById(R.id.balance);
        rechargeV = (LinearLayout) findViewById(R.id.recharge);
        finance_detailV = (LinearLayout) findViewById(R.id.finance_detail);
        withdrawV = (LinearLayout) findViewById(R.id.withdraw);

        rechargeV.setOnClickListener(this);
        finance_detailV.setOnClickListener(this);
        withdrawV.setOnClickListener(this);

        myMoney();
    }

    private void myMoney() {
        DhNet net = new DhNet(new API().accountview);
        net.doGetInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                // TODO Auto-generated method stub
                if (response.isSuccess()) {
                    JSONObject jo = response.jSONFromData();
                    balanceT.setText(getString(R.string.money_symbol)
                            + JSONUtil.getString(jo, "balance"));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            // 充值
            case R.id.recharge:
                it = new Intent(self, RechargeActivity.class);
                startActivityForResult(it, Recharge);
                break;
            // 财务明细
            case R.id.finance_detail:
                it = new Intent(self, FinanceDetailActivity.class);
                startActivity(it);
                break;
            // 提现到银行卡
            case R.id.withdraw:
                it = new Intent(self, WithdrawCardActivity.class);
                startActivity(it);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, arg2);
        if (requestCode == Recharge && resultCode == Activity.RESULT_OK) {
            myMoney();
        }
    }
}
