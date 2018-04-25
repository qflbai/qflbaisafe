package com.qflbai.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.SpUtil;
import com.qflbai.mobilesafe.util.ToastUtil;

public class Setup3Activity extends BaseSetupActivity {

    private Context mContext;
    private EditText et_numbre;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        mContext = this;
        initUI();
        backDisplay();
    }

    /**
     * 回显过程
     */
    private void backDisplay() {

        String phone = SpUtil.getString(mContext, ConstantValue.CONACT_PHONE,
                "");
        et_numbre.setText(phone);
    }

    /**
     * 初始化控件UI的id值
     */
    private void initUI() {

        et_numbre = (EditText) findViewById(R.id.et_setup3activity);
        btn = (Button) findViewById(R.id.btn_setup3activity);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContactListActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (intent != null) {
            String phoneString = intent.getStringExtra("phone");
            // 将特殊字符过滤掉
            String phone = phoneString.replace("-", "").replace("(", "")
                    .replace(")", "").replace(" ", "").trim();
            et_numbre.setText(phone);
            // 存储联系人至sp中
            SpUtil.putString(mContext, ConstantValue.CONACT_PHONE, phone);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void next_Activity() {
        String phone = et_numbre.getText().toString().trim();

        if (!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(mContext, Setup4Activity.class);
            startActivity(intent);
            finish();
            // 开启平移动画
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
            SpUtil.putString(mContext, ConstantValue.CONACT_PHONE, phone);
        } else {
            ToastUtil.show(mContext, "请输入联系人电话号码", 0);
        }
    }

    @Override
    public void pre_Activity() {
        Intent intent = new Intent(mContext, Setup2Activity.class);
        startActivity(intent);
        finish();
        // 开启平移动画
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}