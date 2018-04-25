package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.SpUtil;
import com.qflbai.mobilesafe.util.ThemeUtil;

public abstract class BaseSetupActivity extends Activity {
    private Context mContext;
    private GestureDetector gd;

    /**
     * 将每一个界面中的上一步，和下一步按钮操作看，抽取到父类只能中
     * @param savedInstanceState
     * 因为父类不知道子类上一步和下一步具体的执行操作代码，所欲要创建一个抽象方法，让子类实现这个方法，根据
     * 自己的特性去实现相应的操作，
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(SpUtil.getInt(this, ConstantValue.CHANGE_TO_THEME,0)!= -1){
            ThemeUtil.setActivityTheme(this);
        }
        // 去除当前activity的头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = this;
        // [2]通过手势处理类，接受多种类型的事件用做处理
        gd = new GestureDetector(mContext,
                new GestureDetector.SimpleOnGestureListener() {

                    /**
                     * 监听手势移动 e1:代表起始坐标， e2：代表终点坐标， velocityX：在x轴上移动的速度
                     */
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        float start = e1.getX();
                        float end = e2.getX();
                        if ((start - end) > 0) {
                            // 调用子类下一页方法
                            next_Activity();
                        }
                        if ((start - end) < 0) {
                            // 调用子类上一页方法
                            pre_Activity();
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }

                });
    }
    public void nextPage(View view) {
        // 调用一个抽象类，让子类其实现，点击下一页按钮的时候，根据子类的next_Activity()去判断响应的逻辑
        next_Activity();
    }
    public void prePage(View view) {
        // 调用一个抽象类，让子类其实现
        pre_Activity();
    }
    // 跳转到下一页的方法让其子类去实现
    public abstract void next_Activity();
    public abstract void pre_Activity();
    // [1]监听屏幕上响应的事件类型(抬起，按下，移动)在activity中重写onTouchEvent()
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // [3]创建手势管理的对象，用做管理在onTouchEvent(event)传递过来的手势动作
        gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //在父类中直接对子类中的返回键进行统一的处理

    /*@Override
    public void onBackPressed() {
        pre_Activity();
        super.onBackPressed();
    }*/
    //监听手机的物理按键按钮的点击事件

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        /**
         * keyCode :物理按键的标志
         * event:按键的处理事件
         */
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //true 是可以拦截按键的事件
                //在高版本中，home键是不可以拦截的
                pre_Activity();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
