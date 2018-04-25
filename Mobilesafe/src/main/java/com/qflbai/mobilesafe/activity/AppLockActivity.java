package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qflbai.mobilesafe.been.AppInfo;
import com.qflbai.mobilesafe.db.dao.AppLockDao;
import com.qflbai.mobilesafe.engine.AppInfoProvider;
import com.qflbai.mobilesafe.R;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends Activity implements View.OnClickListener {

    private Button bt_unlock;
    private ProgressBar mProgressBar;
    private TextView mTv_loading;
    private Button bt_lock;
    private LinearLayout ll_unlock;
    private LinearLayout ll_lock;
    private TextView tv_unlock;
    private TextView tv_lock;
    private ListView lv_unlock;
    private ListView lv_lock;
    private List<AppInfo> mAppInfoList;
    private ArrayList<AppInfo> mLockList;
    private ArrayList<AppInfo> mUnLockList;
    private AppLockDao mDao;
    private MyAdapter mAdapterLock;
    private MyAdapter mAdapterUnLock;
    private Animation mAnimation;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            //接到已经加锁和未枷锁的信息后，可以填充数据适配器
            mAdapterLock = new MyAdapter(true);
            mAdapterUnLock = new MyAdapter(false);
            lv_lock.setAdapter(mAdapterLock);
            lv_unlock.setAdapter(mAdapterUnLock);
            mProgressBar.setVisibility(View.INVISIBLE);
            mTv_loading.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_unlock:
                ll_unlock.setVisibility(View.VISIBLE);
                ll_lock.setVisibility(View.INVISIBLE);
                bt_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                bt_lock.setBackgroundResource(R.drawable.tab_right_default);
                break;
            case R.id.bt_lock:
                //如果已经选择了枷锁按钮，则隐藏未枷锁，显示已枷锁
                //并且按钮要变色，切换图片，未枷锁图片变为浅色，已经枷锁变成身深色
                ll_unlock.setVisibility(View.INVISIBLE);
                ll_lock.setVisibility(View.VISIBLE);
                bt_unlock.setBackgroundResource(R.drawable.tab_left_default);
                bt_lock.setBackgroundResource(R.drawable.tab_right_pressed);
                break;
        }
    }

    class MyAdapter extends BaseAdapter{
        private  boolean isLock;

        /**
         * isLock为true代表是加锁，fasle是未加锁
         * @param isLock 这个标识用来区分是加锁还是没有加锁
         */
        public MyAdapter(boolean isLock){
            this.isLock = isLock;
        }

        @Override
        public int getCount() {
            if(isLock){
                tv_lock.setText("已加锁的应用:"+mLockList.size());
                return mLockList.size();
            }else {
                tv_unlock.setText("未加锁的应用:"+ mUnLockList.size());
                return mUnLockList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if(isLock){
                return mLockList.get(position);
            }else {
                return mUnLockList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = View.inflate(getApplicationContext(), R.layout
                        .listview_islock_item, null);
                holder = new ViewHolder();
                holder.iv_icon = ((ImageView)convertView.findViewById(R.
                        id.iv_icon));
                holder.iv_lock = ((ImageView)convertView.findViewById(R.
                        id.iv_lock));
                holder.tv_name = (TextView)convertView.findViewById(R.
                        id.tv_name);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            final AppInfo appInfo = (AppInfo) getItem(position);
            // TODO: 2017/3/20 appInfo 变成成员变量会出错为什么还没解决
            final View viewAnimation = convertView;
            holder.iv_icon.setBackground(appInfo.getIcon());
            holder.tv_name.setText(appInfo.getName());
            if(isLock){
                holder.iv_lock.setImageResource(R.drawable.lock);
            }else {
                holder.iv_lock.setImageResource(R.drawable.unlock);
            }
            holder.iv_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加动画效果,动画默认是非阻塞的,所以执行动画的同时,
                    // 动画以下的代码也会执行
                    viewAnimation.startAnimation(mAnimation);//500毫秒
                    //对动画执行过程做事件监听,监听到动画执行完成后,
                    // 再去移除集合中的数据,操作数据库,刷新界面
                    mAnimation.setAnimationListener(new Animation
                            .AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            //动画开始的是调用方法
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            //动画重复时候调用方法
                        }
                        //动画执行结束后调用方法
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if(isLock){
                                //已加锁------>未加锁过程
                                //1.已加锁集合删除一个,未加锁集合添加一个,对象就是getItem方法获取的对象
                                mLockList.remove(appInfo);
                                mUnLockList.add(appInfo);
                                //2.从已加锁的数据库中删除一条数据
                                mDao.delete(appInfo.getPackageName());
                                //3.刷新数据适配器
                                mAdapterLock.notifyDataSetChanged();
                            }else{
                                //未加锁------>已加锁过程
                                //1.已加锁集合添加一个,未加锁集合移除一个
                                // ,对象就是getItem方法获取的对象
                                mLockList.add(appInfo);
                                mUnLockList.remove(appInfo);
                                //2.从已加锁的数据库中插入一条数据
                                mDao.insert(appInfo.getPackageName());
                                //3.刷新数据适配器
                                mAdapterUnLock.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_icon,iv_lock;
        TextView tv_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        initView();
        initData();
        initAnimation();
    }

    /**
     * 初始化动画
     * 平移自身的一个宽度的大小
     */
    private void initAnimation() {
        mAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
       0,Animation.RELATIVE_TO_SELF ,1,Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0);
        mAnimation.setDuration(500);
        /*animation.setFillAfter(true);*/
    }

    /**
     * 区分已经枷锁和未枷锁的应用
     * 初始化数据
     */
    private void initData() {

        mProgressBar.setVisibility(View.VISIBLE);
        mTv_loading.setVisibility(View.VISIBLE);

        new Thread(){
            public void run() {
                //1.获取所有手机中的应用
                mAppInfoList = AppInfoProvider.getAppInfoList(
                        getApplicationContext());
                //2.区分已加锁应用和未加锁应用
                mLockList = new ArrayList<AppInfo>();
                mUnLockList = new ArrayList<AppInfo>();

                //3.获取数据库中已加锁应用包名的的结合
                mDao = AppLockDao.getInstance(getApplicationContext());
                List<String> lockPackageList = mDao.findAll();
                for (AppInfo appInfo : mAppInfoList) {
                    //4,如果循环到的应用的包名,在数据库中,则说明是已加锁应用
                    if(lockPackageList.contains(appInfo.packageName)){
                        mLockList.add(appInfo);
                    }else{
                        mUnLockList.add(appInfo);
                    }
                }
                //5.告知主线程,可以使用维护的数据
                mHandler.sendEmptyMessage(0);
            };
        }.start();
    }

    /**
     * 初始化控件的ID值
     */
    private void initView() {
        bt_unlock = (Button) findViewById(R.id.bt_unlock);
        bt_lock = (Button) findViewById(R.id.bt_lock);
        ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
        ll_lock = (LinearLayout) findViewById(R.id.ll_lock);
        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        tv_lock = (TextView) findViewById(R.id.tv_lock);
        lv_unlock = (ListView) findViewById(R.id.lv_unlock);
        lv_lock = (ListView) findViewById(R.id.lv_lock);

        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        mTv_loading = (TextView) findViewById(R.id.tv_loading);
        bt_unlock.setOnClickListener(this);
        bt_lock.setOnClickListener(this);
    }
}
