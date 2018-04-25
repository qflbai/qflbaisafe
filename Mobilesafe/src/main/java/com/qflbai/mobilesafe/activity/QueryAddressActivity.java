package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qflbai.mobilesafe.engine.AddressDao;
import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ToastUtil;

public class QueryAddressActivity extends Activity {
    private static final String TAG ="QueryAddressActivity";
    private EditText et_InputNumber;
    private Button btn_Address;
    private TextView tv_Result;
    private String mQueryAddress;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(mQueryAddress != null){
                tv_Result.setText(mQueryAddress);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        initView();
    }
    /**
     * 初始化控件的ID值
     */
    private void initView() {
        et_InputNumber = (EditText) findViewById(R.id.et_input_phoneNumber);
        btn_Address = (Button) findViewById(R.id.btn_query_Address);
        tv_Result = (TextView) findViewById(R.id.tv_query_result);

        //实时查询方法
        et_InputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String inputPhome = et_InputNumber.getText().toString().trim();
                query(inputPhome);
            }
        });
        btn_Address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String inputPhome = et_InputNumber.getText().toString().trim();
                if(TextUtils.isEmpty(inputPhome)){
                    //输入框抖动
                    //抖动
                    Animation shake = AnimationUtils.loadAnimation(
                            getApplicationContext(), R.anim.shake);
                    //interpolator插补器,数学函数
                    //自定义插补器
                   /* shake.setInterpolator(new Interpolator() {
                        @Override
                        public float getInterpolation(float v) {
                            return 0;
                        }
                    });*/
                    et_InputNumber.startAnimation(shake);
                    Log.i(TAG, "onClick:"+inputPhome+"------");
                    ToastUtil.show(getApplicationContext(),"亲!请输入你要查询的号码再查询",0);
                    //手机震动效果,获取相应的对象
                    Vibrator systemService = (Vibrator) getSystemService(Context
                            .VIBRATOR_SERVICE);
                    //震动的毫秒值
                    systemService.vibrate(3000);
                    //有规律的震动
                    systemService.vibrate(new long[]{2000,5000,2000,5000},-1);
                }else{
                    Log.i(TAG, "onClick:"+inputPhome+"------");
                    query(inputPhome);
                }
            }
        });
    }
    /**
     * @param inputPhome 要查询的号码
     *                   因为查询是一个耗时的操作，所以要开子线程
     */
    private void query(final String inputPhome) {
        new Thread(){
            @Override
            public void run() {
                mQueryAddress = AddressDao.getAddress(inputPhome);
                //应用消息机制告知主线程看可以使用了
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
