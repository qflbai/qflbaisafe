package com.kongweijun.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.kongweijun.mobilesafe.R;
import com.kongweijun.util.ConstantValue;
import com.kongweijun.util.SpUtil;
import com.kongweijun.view.SettingItemView;

public class SettingActivity extends Activity {
    private Context mContext;
    private SettingItemView mSiv_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;
        initUpdate();
    }
    /**
     * 初始化settingview条目的更新控件
     */
    private void initUpdate() {
        //从SpUtil中获取存储的开关节点状态，用做显示
        mSiv_update = (SettingItemView) findViewById(R.id.siv_update);
        boolean defIsChack = SpUtil.getBoolean(mContext, ConstantValue.OPEN_UPDATE,false);
        mSiv_update.setCheck(defIsChack);
        mSiv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * 如果之前是选中状态的，点击过后是未选中，反之则选中
             */
            public void onClick(View view) {
                boolean isCheck = mSiv_update.isCheck();
                mSiv_update.setCheck(!isCheck);
                //存储当前开关状态，为了下次方便显示
               SpUtil.putBoolean(mContext,ConstantValue.OPEN_UPDATE,!isCheck);
            }
        });
    }
}
