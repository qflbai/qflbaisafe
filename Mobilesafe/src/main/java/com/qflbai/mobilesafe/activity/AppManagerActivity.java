package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.been.AppInfo;
import com.qflbai.mobilesafe.engine.AppInfoProvider;
import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends Activity
        implements View.OnClickListener{
    private List<AppInfo> mAppInfoList;
    private AppInfo mAppInfo;
    private ListView lv_app_list;
    private MyAdapter mAdapter;
    private List<AppInfo> mSystemList;
    private List<AppInfo> mCustomerList;
    private TextView tv_des;
    private PopupWindow popupWindow;
    private Context mContext;


    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mAdapter = new MyAdapter();
            lv_app_list.setAdapter(mAdapter);
            if(tv_des!=null && mCustomerList!=null){
                tv_des.setText("用户应用("+mCustomerList.size()+")");
            }
        };
    };

    @Override
    public void onClick(View v) {
        //判断点击是按个按钮
        //getId() : 获取点击按钮的id
        switch (v.getId()) {
            case R.id.ll_popuwindow_uninstall:
                System.out.println("卸载");
                uninstall();
                break;
            case R.id.ll_popuwindow_start:
                System.out.println("启动");
                start();
                break;
            case R.id.ll_popuwindow_share:
                System.out.println("分享");
                share();
                break;
            case R.id.ll_popuwindow_detail:
                System.out.println("详情");
                detail();
                break;
        }
        hidePopuwindow();
    }

    class MyAdapter extends BaseAdapter {

        //获取数据适配器中条目类型的总数,修改成两种(纯文本,图片+文字)
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        //指定索引指向的条目类型,条目类型状态码指定(0(复用系统),1)
        @Override
        public int getItemViewType(int position) {
            if(position == 0 || position == mCustomerList.size()+1){
                //返回0,代表纯文本条目的状态码
                return 0;
            }else{
                //返回1,代表图片+文本条目状态码
                return 1;
            }
        }

        //listView中添加两个描述条目
        @Override
        public int getCount() {
            return mCustomerList.size()+
                    mSystemList.size()+2;
        }

        @Override
        public AppInfo getItem(int position) {
            if(position == 0 || position == mCustomerList.size()+1){
                return null;
            }else{
                if(position<mCustomerList.size()+1){
                    return mCustomerList.get(position-1);
                }else{
                    //返回系统应用对应条目的对象
                    return mSystemList.get(position - mCustomerList.size()-2);
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if(type == 0){
                //展示灰色纯文本条目
                ViewTitleHolder holder = null;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(),
                            R.layout.listview_app_item_title, null);
                    holder = new ViewTitleHolder();
                    holder.tv_title = (TextView)convertView.
                            findViewById(R.id.tv_title);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewTitleHolder) convertView.getTag();
                }
                if(position == 0){
                    holder.tv_title.setText("用户应用("+mCustomerList.size()+")");
                }else{
                    holder.tv_title.setText("系统应用("+mSystemList.size()+")");
                }
                return convertView;
            }else{
                //展示图片+文字条目
                ViewHolder holder = null;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(),
                            R.layout.listview_app_item, null);
                    holder = new ViewHolder();
                    holder.iv_icon = (ImageView)convertView.
                            findViewById(R.id.iv_icon);
                    holder.tv_name = (TextView)convertView.
                            findViewById(R.id.tv_name);
                    holder.tv_path = (TextView) convertView.
                            findViewById(R.id.tv_path);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
                holder.tv_name.setText(getItem(position).name);
                if(getItem(position).isSdCard){
                    holder.tv_path.setText("sd卡应用");
                }else{
                    holder.tv_path.setText("手机应用");
                }
                return convertView;
            }
        }
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_path;
    }
    static class ViewTitleHolder{
        TextView tv_title;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manger);
        mContext = this;
        initTitle();
        initList();
    }

    /**
     * Activity在一次获取焦点调用OnResume
     */
    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initList() {
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        tv_des = (TextView) findViewById(R.id.tv_des);
        initData();
        lv_app_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //滚动过程中调用方法
                //AbsListView中view就是listView对象
                //firstVisibleItem第一个可见条目索引值
                //visibleItemCount当前一个屏幕的可见条目数
                //总共条目总数
                hidePopuwindow();
                if(mCustomerList!=null && mSystemList!=null){
                    if(firstVisibleItem>=mCustomerList.size()+1){
                        //滚动到了系统条目
                        tv_des.setText("系统应用("+mSystemList.size()+")");
                    }else{
                        //滚动到了用户应用条目
                        tv_des.setText("用户应用("+mCustomerList.size()+")");
                    }
                }

            }
        });

        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position == 0 || position == mCustomerList.size()+1){
                    return;
                }else{
                    if(position<mCustomerList.size()+1){
                        mAppInfo = mCustomerList.get(position - 1);
                    }else{
                        //返回系统应用对应条目的对象
                        mAppInfo = mSystemList.get(position - mCustomerList.size
                                ()-2);
                    }
                    showPopupWindow(parent,view);
                }
            }
        });
    }

    /**
     * 弹出PopupWindow对话框
     * @param parent
     * @param view
     */
    private void showPopupWindow(AdapterView<?> parent, View view) {
        //3.弹出气泡
        /*TextView contentView = new TextView(getApplicationContext());
        contentView.setText("我是popuwindow的textview控件");
        contentView.setBackgroundColor(Color.RED);*/
        hidePopuwindow();
        View contentView = View.inflate(getApplicationContext(), R.layout.
                popu_window, null);

        //初始化控件
        LinearLayout ll_popuwindow_uninstall = (LinearLayout) contentView.
                findViewById(R.id.ll_popuwindow_uninstall);
        LinearLayout ll_popuwindow_start = (LinearLayout) contentView.
                findViewById(R.id.ll_popuwindow_start);
        LinearLayout ll_popuwindow_share = (LinearLayout) contentView.
                findViewById(R.id.ll_popuwindow_share);
        LinearLayout ll_popuwindow_detail = (LinearLayout) contentView.
                findViewById(R.id.ll_popuwindow_detail);
        //给控件设置点击事件
        //给控件设置点击事件
        ll_popuwindow_uninstall.setOnClickListener(this);
        ll_popuwindow_start.setOnClickListener(this);
        ll_popuwindow_share.setOnClickListener(this);
        ll_popuwindow_detail.setOnClickListener(this);
        //contentView : 显示view对象
        //width,height : view宽高
        popupWindow = new PopupWindow(contentView, LinearLayout.
                LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //4.获取条目的位置,让气泡显示在相应的条目
        int[] location = new int[2];//保存x和y坐标的数组
        view.getLocationInWindow(location);//获取条目x和y的坐标,同时保存到int[]
        //获取x和y的坐标
        int x = location[0];
        int y = location[1];
        //parent : 要挂载在那个控件上
        //gravity,x,y : 控制popuwindow显示的位置
        //popupWindow.showAsDropDown(view ,50,-view.getHight());
        popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, x+100,
                y-30);
        //6.设置动画
        //缩放动画
        //前四个 :　控制控件由没有变到有   动画 0:没有    1:整个控件
        //后四个:控制控件是按照自身还是父控件进行变化
        //RELATIVE_TO_SELF : 以自身变化
        //RELATIVE_TO_PARENT : 以父控件变化
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        //由半透明变成不透明
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        //组合动画
        //shareInterpolator : 是否使用相同的动画插补器  true:共享
        // false:各自使用各自的
        AnimationSet animationSet = new AnimationSet(true);
        //添加动画
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        //执行动画
        contentView.startAnimation(animationSet);
    }

    private void initTitle() {
        //1,获取磁盘(内存,区分于手机运行内存)可用大小,磁盘路径
        String path = Environment.getDataDirectory().getAbsolutePath();
        //2,获取sd卡可用大小,sd卡路径
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //3,获取以上两个路径下文件夹的可用大小
        String memoryAvailSpace = Formatter.formatFileSize(this, getAvailSpace(path));
        String sdMemoryAvailSpace = Formatter.formatFileSize(this,getAvailSpace(sdPath));

        TextView tv_memory = (TextView) findViewById(R.id.tv_memory);
        TextView tv_sd_memory = (TextView) findViewById(R.id.tv_sd_memory);

        tv_memory.setText("磁盘可用:"+memoryAvailSpace);
        tv_sd_memory.setText("sd卡可用:"+sdMemoryAvailSpace);
    }

    //int代表多少个G
    /**
     * 返回值结果单位为byte = 8bit,最大结果为2147483647 bytes
     * @param path
     * @return 返回指定路径可用区域的byte类型值
     */
    private long getAvailSpace(String path) {
        //获取可用磁盘大小类
        StatFs statFs = new StatFs(path);
        //获取可用区块的个数
        long count = statFs.getAvailableBlocks();
        //获取区块的大小
        long size = statFs.getBlockSize();
        //区块大小*可用区块个数 == 可用空间大小
        return count*size;
    }
    /**
     * 分享
     */
    private void share() {
        /**
         *  Intent
         {
         act=android.intent.action.SEND
         typ=text/plain
         flg=0x3000000
         cmp=com.android.mms/.ui.ComposeMessageActivity (has extras)   intent中包含信息
         } from pid 228
         */
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "发现一个很牛x软件"+mAppInfo.getName()+",下载地址:www.baidu.com,自己去搜");
        startActivity(intent);
    }

    /**
     * 隐藏气泡
     */
    private void hidePopuwindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();//隐藏气泡
            popupWindow = null;
        }
    }

    /**
     *详情
     */
    private void detail() {
        /**
         *  Intent
         {
         act=android.settings.APPLICATION_DETAILS_SETTINGS    action
         dat=package:com.example.android.apis   data
         cmp=com.android.settings/.applications.InstalledAppDetails
         } from pid 228
         */
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:"+mAppInfo.getPackageName()));
        startActivity(intent);
    }
    /**
     * 启动
     */
    private void start() {
        PackageManager pm = getPackageManager();
        //获取应用程序的启动意图
        Intent intent = pm.getLaunchIntentForPackage(mAppInfo.getPackageName());
        if (intent!=null) {
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "系统核心程序,无法启动",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 卸载
     */
    private void uninstall() {
        /**
         * <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <action android:name="android.intent.action.DELETE" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="package" />
         </intent-filter>
         */
        //判断是否是系统程序
        if (!mAppInfo.isSystem) {
            //判断是否是我们自己的应用,是不能卸载
            if (!mAppInfo.getPackageName().equals(getPackageName())) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:"+mAppInfo.getPackageName()));
                //tel:110
                startActivityForResult(intent,0);
            }else{
                Toast.makeText(mContext, "文明社会,杜绝自杀",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(mContext, "要想卸载系统程序,请root先",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread(){
            public void run() {
                mAppInfoList = AppInfoProvider.getAppInfoList
                        (getApplicationContext());
                mSystemList = new ArrayList<AppInfo>();
                mCustomerList = new ArrayList<AppInfo>();
                for (AppInfo appInfo : mAppInfoList) {
                    if(appInfo.isSystem){
                        //系统应用
                        mSystemList.add(appInfo);
                    }else{
                        //用户应用
                        mCustomerList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            };
        }.start();
    }

    @Override
    protected void onDestroy() {
        //隐藏气泡
        hidePopuwindow();
        super.onDestroy();
    }
}
