package com.qflbai.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.SpUtil;
import com.qflbai.mobilesafe.util.ToastUtil;

public class Setup4Activity extends BaseSetupActivity {

    private Context mContext;
    private CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        mContext = this;

        initView();

    }

    /**
     * 找到我们关心的控件
     */
    private void initView() {

        cb = (CheckBox) findViewById(R.id.cb_setup4Activity);

        // 是否选中状态的过程

        boolean bl = SpUtil.getBoolean(mContext, ConstantValue.OPEN_SECURITY,
                false);
        // 根据状态，修改CheckBox后续的文字显示
        cb.setChecked(bl);
        if (bl) {
            cb.setText("你已经开启防盗保护");
        } else {
            cb.setText("你没有开启防盗保护");
        }
        // 点击过程中，checkbox状态的切换，以及切换后状态的存储
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                SpUtil.putBoolean(mContext, ConstantValue.OPEN_SECURITY,
                        isChecked);
                if (isChecked) {
                    cb.setText("你已经开启防盗保护");
                } else {
                    cb.setText("你没有开启防盗保护");
                }
            }
        });
    }

    @Override
    public void next_Activity() {
        boolean bl = SpUtil.getBoolean(mContext, ConstantValue.OPEN_SECURITY,
                false);
        if (bl) {
            Intent intent = new Intent(mContext, SetupOverActivity.class);
            startActivity(intent);
            finish();
            // 开启平移动画
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
            SpUtil.putBoolean(mContext, ConstantValue.SETUP_OVER, true);
        } else {
            ToastUtil.show(mContext, "请开启防盗保护", 0);
        }
    }

    @Override
    public void pre_Activity() {
        Intent intent = new Intent(mContext, Setup3Activity.class);
        startActivity(intent);
        finish();
        // 开启平移动画
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
