package com.qflbai.mobilesafe.activity;

import com.qflbai.mobilesafe.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity extends BaseSetupActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        mContext = this;
    }


    @Override
    public void next_Activity() {

        Intent intent = new Intent(mContext,
                Setup2Activity.class);
        startActivity(intent);
        finish();
        // 开启平移动画
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);

    }

    @Override
    public void pre_Activity() {
    }
}
