package com.qflbai.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.qflbai.mobilesafe.activity.BackgroundActivity;
import com.qflbai.mobilesafe.R;

public class RocketService extends Service {
    private WindowManager mWM;
    private int mScreenHeight;
    private int mScreenWidth;
    private  WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private View mRocketView;
    private ImageView mImageView;
    private WindowManager.LayoutParams params;

    public RocketService() {
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int hight = (int) msg.obj;
            params.y = hight;
            //告知窗体更新火箭view的所在 位置
            mWM.updateViewLayout(mRocketView,params);
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }

    @Override
    public void onCreate() {
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenHeight = mWM.getDefaultDisplay().getHeight();
        mScreenWidth = mWM.getDefaultDisplay().getWidth();
        showRocket();
    }

    /**
     * 显示小火箭在窗体上
     */
    private void showRocket() {
        params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	默认能够被触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //在响铃的时候显示吐司,和电话类型一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        //指定吐司的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT+Gravity.TOP;
        mRocketView = View.inflate(this, R.layout.rocket_view, null);
        mImageView = (ImageView) mRocketView.findViewById(R.id
                .iv_rocket_view);
        AnimationDrawable mAnimationImageView = (AnimationDrawable) mImageView.getBackground();
        mAnimationImageView.start();
        //读取sp中存储吐司位置的x,y坐标值
        // params.x为吐司左上角的x的坐标
        //params.x = SpUtil.getInt(getApplicationContext(), LOCATION_X, 0);
        // params.y为吐司左上角的y的坐标
        //params.y = SpUtil.getInt(getApplicationContext(), LOCATION_Y, 0);

        mWM.addView(mRocketView, params);
        mRocketView.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int disX = moveX-startX;
                        int disY = moveY-startY;
                        params.x = params.x+disX;
                        params.y = params.y+disY;
                        //容错处理
                        if(params.x<0){
                            params.x = 0;
                        }
                        if(params.y<0){
                            params.y=0;
                        }
                        if(params.x>mScreenWidth-mRocketView.getWidth()){
                            params.x = mScreenWidth-mRocketView.getWidth();
                        }
                        if(params.y>mScreenHeight-mRocketView.getHeight()-22){
                            params.y = mScreenHeight-mRocketView.getHeight()-22;
                        }
                        //告知窗体吐司需要按照手势的移动,去做位置的更新
                        mWM.updateViewLayout(mRocketView, params);
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //SpUtil.putInt(getApplicationContext(), ConstantValue
                            //.LOCATION_X, params.x);
                        //SpUtil.putInt(getApplicationContext(),ConstantValue
                           // .LOCATION_Y, params.y);
                        if((params.x>0)||(params.x<1000)&&(params.y>800)){
                            //发送火箭
                            sendRocket();
                            //开启产生烟雾的动画(尾气的activity)
                            //在开启火箭过程中,去开启一个新的activity,activity透明,在此activity中放置两张图片(淡入淡出效果)
                            Intent intent = new Intent(getApplicationContext(),BackgroundActivity.class);
                            //指定开启新的activity任务栈
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        break;
                }
                //true 响应拖拽触发的事件
                return true;
            }
        });

    }

    /**
     * 发送火箭
     */
    private void sendRocket() {
        //在向上的移动过程中，一直去减速y轴的大小，直到减少为0
       new Thread(){
           @Override
           public void run() {
               for(int i = 0;i<11;i++){
                   int height = 1000 - i * 100;
                   try {
                       Thread.sleep(50);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }finally {
                       Message msg = Message.obtain();
                       msg.obj = height;
                       mHandler.sendMessage(msg);
                   }
               }
           }
       }.start();
    }

    @Override
    public void onDestroy() {
        //取消对电话状态的监听(开启服务的时候监听电话的对象)
        if(mWM!=null && mRocketView!=null){
            mWM.removeView(mRocketView);
        }
        super.onDestroy();
    }
}
