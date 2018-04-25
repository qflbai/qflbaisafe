package com.qflbai.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.SpUtil;
import com.qflbai.mobilesafe.util.ToastUtil;
import com.qflbai.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

    private Context mContext;
    private SettingItemView siv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        mContext = this;
        initUI();
        // 读取已有的绑定状态，用做显示sp中是否存储了sim卡的系列号
        backDisplay();
    }

    /**
     * 改方法主要是回显保存在sp中的内容。
     */
    private void backDisplay() {
        String sim_number = SpUtil.getString(mContext,
                ConstantValue.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)) {
            siv.setCheck(false);
        } else {
            siv.setCheck(true);
        }
        siv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [1]获取原有的状态
                boolean ischeck = siv.isCheck();
                // [2]将原有的状态取反，设置给当前的条目
                siv.setCheck(!ischeck);

                if (!ischeck) {
                    // [3]存储系列卡号，获取系列卡号(telephoneManager
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    // [3.1]获取系列卡号
                    String simSerialNumber = manager.getSimSerialNumber();
                    SpUtil.putString(mContext, ConstantValue.SIM_NUMBER,
                            simSerialNumber);
                } else {
                    // [4]将存储系列卡号的节点，从sp中删除
                    SpUtil.remove(mContext, ConstantValue.SIM_NUMBER);
                }
            }
        });

    }

    /**
     * 初始化UI的控件
     */
    private void initUI() {
        siv = (SettingItemView) findViewById(R.id.siv);
    }

    @Override
    public void next_Activity() {
        String sim_number = SpUtil.getString(mContext,
                ConstantValue.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)) {

            ToastUtil.show(mContext, "请绑定Sim卡后在点击下一步", 0);

        } else {
            Intent intent = new Intent(mContext, Setup3Activity.class);
            startActivity(intent);
            finish();
            // 开启平移动画
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        }
    }

    @Override
    public void pre_Activity() {

        Intent intent = new Intent(mContext, Setup1Activity.class);
        startActivity(intent);
        finish();
        // 开启平移动画
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }
}