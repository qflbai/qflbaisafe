package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.SpUtil;
import com.qflbai.mobilesafe.util.ThemeUtil;


public class SetupOverActivity extends Activity {
    private Context mContext;
    private TextView tv_phone;
    private TextView tv_open;
    private ImageView iv_lock;
    private TextView tv_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       if(SpUtil.getInt(this, ConstantValue.CHANGE_TO_THEME,0)!= -1){
            ThemeUtil.setActivityTheme(this);
       }
        // 去除当前activity的头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_over);

        mContext = this;
        boolean setup_over = SpUtil.getBoolean(mContext,
                ConstantValue.SETUP_OVER, false);

        if (setup_over) {
            // 密码输入成功后，并且四个导航界面设置完成，停留在设置完成功能列表界面
            setContentView(R.layout.activity_setup_over);
            initUI();
            backDisplay();
        } else {
            // 密码输入成功后，四个导航界面没有设置完成，跳转到第一个界面
            Intent intent = new Intent(mContext, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }
    /**
     * 初始化数据
     */
    private void backDisplay() {

        String phone = SpUtil.getString(mContext, ConstantValue.CONACT_PHONE,
                "");
        tv_phone.setText(phone);
        tv_open.setText("防盗保护已开启");
    }
    /**
     * 找到我们关心得控件
     */
    private void initUI() {
        tv_set = (TextView) findViewById(R.id.tv_setup_over_set);
        tv_phone = (TextView) findViewById(R.id.tv_setup_over_phone);
        tv_open = (TextView) findViewById(R.id.tv_setup_over_open);
        iv_lock = (ImageView) findViewById(R.id.iv_setup_over_lock);
        tv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Setup1Activity.class);
                startActivity(intent);
                finish();
                // 开启平移动画
                overridePendingTransition(R.anim.next_in_anim,
                        R.anim.next_out_anim);
            }
        });
    }
}
