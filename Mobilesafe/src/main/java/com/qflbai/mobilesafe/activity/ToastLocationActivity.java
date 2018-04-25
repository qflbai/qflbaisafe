package com.qflbai.mobilesafe.activity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.SpUtil;

public class ToastLocationActivity extends Activity {

    private Context mContext;
    private ImageView mIv_drag;
    private Button mBtn_bottom_drag;
    private Button mBtn_top_drag;
    private WindowManager mWM;
    private int mScreenHeight;
    private int mScreenWidth;
    private long[] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);
        mContext = this;
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenHeight = mWM.getDefaultDisplay().getHeight();
        mScreenWidth = mWM.getDefaultDisplay().getWidth();
        int locationX = SpUtil.getInt(mContext, ConstantValue.LOCATION_X, 0);
        int locationY = SpUtil.getInt(mContext, ConstantValue.LOCATION_Y, 0);
        mIv_drag = (ImageView) findViewById(R.id.iv_drag);
        mBtn_bottom_drag = (Button) findViewById(R.id.btn_bottom_drag);
        mBtn_top_drag = (Button) findViewById(R.id.btn_top_drag);
        //左上角坐标作用在iv_drag上
        //iv_drag在相对布局中,所以其所在位置的规则需要由相对布局提供
        //指定宽高都为WRAP_CONTENT
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //将左上角的坐标作用在iv_drag对应规则参数上
        layoutParams.leftMargin = locationX;
        layoutParams.topMargin = locationY;
        //将以上规则作用在mIv_drag上
        mIv_drag.setLayoutParams(layoutParams);

        if(locationY>mScreenHeight/2){
            mBtn_bottom_drag.setVisibility(View.INVISIBLE);
            mBtn_top_drag.setVisibility(View.VISIBLE);
        }else{
            mBtn_bottom_drag.setVisibility(View.VISIBLE);
            mBtn_top_drag.setVisibility(View.INVISIBLE);
        }

        /**
         *多次点击事件
         */
        mIv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if(mHits[mHits.length-1]-mHits[0]<500){
                    //满足双击事件后,调用代码
                    int left = mScreenWidth/2 - mIv_drag.getWidth()/2;
                    int top = mScreenHeight/2 - mIv_drag.getHeight()/2;
                    int right = mScreenWidth/2+mIv_drag.getWidth()/2;
                    int bottom = mScreenHeight/2+mIv_drag.getHeight()/2;
                    //控件按以上规则显示
                    mIv_drag.layout(left, top, right, bottom);
                    //存储最终位置
                    SpUtil.putInt(getApplicationContext(), ConstantValue
                            .LOCATION_X, mIv_drag.getLeft());
                    SpUtil.putInt(getApplicationContext(), ConstantValue.
                            LOCATION_Y, mIv_drag.getTop());
                }
            }
        });

        //监听响应的触摸事件(监听某一个控件的拖拽过程(按下(1),移动(多次),抬起(1)))
        mIv_drag.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;
            //对不同的事件做不同的逻辑处理
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();//相对于原点距离的 X轴距离
                        //event.getX();//相对于自己的X轴的距离
                        //event.getY();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int disX = moveX-startX;
                        int disY = moveY-startY;
                        //1,当前控件所在屏幕的(左,上)角的位置
                        int left = mIv_drag.getLeft()+disX;//左侧坐标
                        int top = mIv_drag.getTop()+disY;//顶端坐标
                        int right = mIv_drag.getRight()+disX;//右侧坐标
                        int bottom = mIv_drag.getBottom()+disY;//底部坐标

                        //容错处理(mIv_drag不能拖拽出手机屏幕)
                        //左边缘不能超出屏幕
                        //容错处理(iv_drag不能拖拽出手机屏幕)
                        //左边缘不能超出屏幕
                        if(left<0){
                            return true;
                        }

                        //右边边缘不能超出屏幕
                        if(right>mScreenWidth){
                            return true;
                        }
                        if(top>mScreenHeight/2){
                            mBtn_bottom_drag.setVisibility(View.INVISIBLE);
                            mBtn_top_drag.setVisibility(View.VISIBLE);
                        }else{
                            mBtn_bottom_drag.setVisibility(View.VISIBLE);
                            mBtn_top_drag.setVisibility(View.INVISIBLE);
                        }
                        //上边缘不能超出屏幕可现实区域
                        if(top<0){
                            return true;
                        }
                        //下边缘(屏幕的高度-22 = 底边缘显示最大值)
                        if(bottom>mScreenHeight - 22){
                            return true;
                        }
                        //2,告知移动的控件,按计算出来的坐标去做展示
                        mIv_drag.layout(left, top, right, bottom);
                        //3,重置一次其实坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //4,存储移动到的位置
                        SpUtil.putInt(mContext, ConstantValue.LOCATION_X, mIv_drag
                                .getLeft());
                        SpUtil.putInt(mContext, ConstantValue.LOCATION_Y, mIv_drag
                                .getTop());
                        break;
                    default:
                        break;
                }
                //要想控件具有点击事件又要有触摸事件，那么要把返回值改为false
                return  false;//在当前的情况下返回false不响应事件,返回true才会响应事件
            }
        });
    }
}
