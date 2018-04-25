package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.SpUtil;
import com.qflbai.mobilesafe.util.StreamUtil;
import com.qflbai.mobilesafe.util.ThemeUtil;
import com.qflbai.mobilesafe.util.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * 这个activity是spalshActivity ,主要包括展示logo等功能
 * @author Administrator
 */
public class SpalshActivity extends Activity {

    private static final String tag = "SpalshActivity";
    private static final int UPDATE_VERSION = 1;// 更新新版本的消息
    private static final int ENTER_HOME = 2;// 进入主界面
    private static final int JSON_EXCEPTION = 3;// json解析错误
    protected static final int URL_EXCEPTION = 4;// url地址错误
    private RelativeLayout rl_root;
    protected static final int IO_EXCEPTION = 5;// io错误
    private static final int ENTERHOME = 6;
    private TextView mShowVersionName;
    private PackageManager mPackageManagerName;
    private PackageManager mPackageManagerCode;
    private PackageInfo mPackageInfoCode;
    private PackageInfo mPackageInfoName;
    private String mVersionName;
    private int mLocalVersionCode;
    private Message mMessage;
    private Context mContext;
    private FileOutputStream mFos;
    private InputStream mIs;
    private String mVersionDes;
    private String mDownloadUrl;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    // 弹出对话框提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    // 进入应用程序主界面
                    enterHome();
                    break;
                case IO_EXCEPTION:
                    ToastUtil.show(getApplicationContext(), "读取数据失败", 0);
                    enterHome();
                    break;
                case JSON_EXCEPTION:
                    ToastUtil.show(getApplicationContext(), "json解析错误", 0);
                    enterHome();
                    break;
                case URL_EXCEPTION:
                    ToastUtil.show(getApplicationContext(), "请求网络失败", 0);
                    enterHome();
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(SpUtil.getInt(this, ConstantValue.CHANGE_TO_THEME,13)!=-1){
            ThemeUtil.setActivityTheme(this);
        }
        super.onCreate(savedInstanceState);
        // 去除当前activity的头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_spalsh);
        mContext = this;
        // 初始化UI
        initUI();
        // 初始化数据
        initData();
        // 初始化动画
        initAnimation();
        initDb();
        if(!SpUtil.getBoolean(mContext,ConstantValue.HAS_SHORTCUT,false)){
            initShortCut();
        }
    }

    /**
     * 生成快捷方式
     */
    private void initShortCut() {
        //给intent维护图标，名称，数据
        Intent intent = new Intent("com.android.launcher.action.INSTALL_" +
                "SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                "360手机卫士");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                BitmapFactory.decodeResource(getResources(),R.mipmap.mobilesafe));
        //点击快捷键跳转到Activity
        //维护开启的意图(隐式意图)
        Intent intent1 = new Intent("android.intent.action.Home");
        intent1.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                intent1);
        //发送广播携带数据
        sendBroadcast(intent);
        //告知sp已经生成了快捷方式
        SpUtil.putBoolean(mContext,ConstantValue.HAS_SHORTCUT,true);
    }

    /**
     * 给SpalshActivity设置透明动画
     */
    private void initAnimation() {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(3000);
        rl_root.startAnimation(aa);
    }

    /**
     * 初始化数据库的数据
     */
    private void initDb() {
        initCopyDb("commonnum.db");
        initCopyDb("address.db");
        initCopyDb("antivirus.db");
    }
    /**
     * 初始化归属地数据拷贝过程
     * @param dbName 数据库名称
     *  拷贝数据库值fukes文件加下
     */
    private void initCopyDb(String dbName) {
        File filesDir = mContext.getFilesDir();//存储到files路径
        //mContext.getCacheDir();//存储到case文件夹
        File file = new File(filesDir,dbName);
        if(file.exists()){//判断文件存在
            return;
        }
        //读取第三方资产目录下的文件
        try {
            mIs = mContext.getAssets().open(dbName);
            //将读取到的内容写入到指定的文件下的文件中
            mFos = new FileOutputStream(file);
            int len = -1;
            byte bys [] = new byte[1024];
            while ((len = mIs.read(bys))!=-1){
                mFos.write(bys,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(mFos != null&& mIs != null){
                    mFos.close();
                    mIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 弹出对话框提示用户更新
     */
    private void showUpdateDialog() {

        // TODO: 2017/2/22 后续还需要改进对话框的形状
        // [1]创建创建AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setCancelable(false);
        // [2]设置左上角图标
        builder.setIcon(R.drawable.update_downloading_1);
        // [3]设置标题
        builder.setTitle("软件版本更新");
        // [4]设置更新内容
        builder.setMessage(mVersionDes);
        // [5]设置下载按钮
        builder.setPositiveButton("立即体验",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击立即体验，就下载最新Apk
                        updateDownLoadingApk();
                    }
                });

        // [6]点击取消键也让其进入主界面
        builder.setNegativeButton("以后再说",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        enterHome();
                    }
                });

        // [7]点击取消键也让其进入主界面
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                enterHome();
            }
        });

        //[8]点击不在提醒按钮后不在弹出更新对话框
        builder.setNeutralButton("不再提醒!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: 2017/2/22  点击不在提醒按钮后不在弹出更新对话框
                SpUtil.putBoolean(mContext,ConstantValue.OPEN_UPDATE,false);
                enterHome();
            }
        });
        builder.show();
    }
    /**
     * 从服务器连接网络去更新Apk下载 在这里使用第三方框架Xutils框架 在使用SDK之前要判断SDK是否挂载上
     */
    protected void updateDownLoadingApk() {

        // [1]判断SDK是否挂载上

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            // [2]获取Sdk路径
            String path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "updateDownLoad.apk";
            // String paht1 =
            // Environment.getExternalStorageDirectory().getPath()+File.separator+"updateDownLoad.apk";

            // [3]发送请求，获取apk，并且放置到指定路径

            HttpUtils hu = new HttpUtils();

            /**
             * url:从服务器获取下载的路径 target：下载存储文件位置 autoResume:是否支持断点续传
             * true, 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
             *true, 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
             * RequestCallBack:回调函数
             */

            hu.download(mDownloadUrl, path, true,true,
                    new RequestCallBack<File>() {

                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {

                            // [4]下载成功会执行该方法
                            File file = responseInfo.result;

                            Log.i(tag, "下载成功");

                            // [4.1]下载成功后要进行安装下载下来的apk
                            installApk(file);

                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {

                            // [5]下载成功会执行该方法
                            Log.i(tag, "下载失败");
                        }

                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {

                            // [6]当前进度 的回调---下载过程中加载进度条

                            Log.i(tag, "刚刚下载");

                            showProgressDialog(total, current);

                            super.onLoading(total, current, isUploading);
                        }

                        // [7]刚刚开始下载的回调

                        @Override
                        public void onStart() {
                            Log.i(tag, "开始下载");
                            super.onStart();
                        }
                    });
        }
    }
    /**
     * 下载时弹出下载对话框
     *
     * @param total
     *            文件的总大小
     * @param current
     *            当前下载的进度
     */
    protected void showProgressDialog(long total, long current) {

        ProgressDialog pd = new ProgressDialog(mContext);

        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        pd.setTitle("Apk正在下载中");

        pd.setMessage("数据正在下载中，请耐心等待!");

        pd.show();

        pd.setMax((int) total);

        pd.setProgress((int) current);

        if (pd.getProgress() == pd.getMax()) {

            pd.dismiss();
            Log.i(tag, "对话框关闭了！");
        }
    }
    /**
     * 提示用户安装apk
     *
     * @param file
     *            隐式意图安装
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        // startActivity(intent);
        // 在当前activity(PackageInstallerActivity)退出的时候，会调用之前的activity(SpalshActivity)的onActivityResult()方法；
        startActivityForResult(intent, ENTERHOME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ENTERHOME) {
            enterHome();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 进入应用程序主界面
     */
    protected void enterHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 获取数据的方法
     */
    private void initData() {
        // [1]获取应用版本的版本名称
        mVersionName = getVersionName();
        mShowVersionName.setText("版本名称:" + mVersionName);
        // [2]检测(本地版本号和服务器版本号做比较)是否有更新，如果有更新，提示用户下载
        mLocalVersionCode = getVersionCode();
        // [3]获取服务器的版本号(客户端发请求，服务端给响应，(json,xml))

        // http://localhost:10086/update1.json
        // http://www.oxxx.com/update1.json(?key=value)括号内容代表是带返回值得json,返回200请求成功，则以流的形式返回
        /**
         * 服务端json中内容包括版本名称 服务端json中内容包括版本号 服务端json中内容包括版本描述
         * 服务端json中内容包括新版本apk下载地址
         */
        if (SpUtil.getBoolean(mContext, ConstantValue.OPEN_UPDATE, false)) {
            checkVersion();
        } else {
            // mHandler.sendMessageDelayed("",4000);
            // 这个方法是用来发送消息后，4000秒中在处理当前的状态码指定的消息
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
        }
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread() {
            @Override
            public void run() {
                //发送请求获取的数据
                mMessage = Message.obtain();
                //获取当前的时间
                long stratTime = System.currentTimeMillis();
                try {
                    //[3.1]封装URL路径
                    String path1 = "http://192.168.1.104:10086/updateApk.json";
                    String path = "http://10.0.2.2:10086/updateApk.json";
                    //请求json的路径
                    URL url = new URL(path);
                    //[3.2]开启一个连接
                    HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                    //[3.3]设置请求头
                    huc.setConnectTimeout(2000);//请求超时
                    huc.setReadTimeout(2000);//读取超时
                    huc.setRequestMethod("GET");//请求方式模认识get方式，还有POST方式
                    //[3.4]获取响应码
                    int code = huc.getResponseCode();
                    if (code == 200) {//请求成功

                        //[3.5]如果请求成功，则以流的方式返回
                        InputStream is = huc.getInputStream();
                        //[3.6]将流转化成字符串
                        String json = StreamUtil.streamToSting(is);
                        Log.i(TAG, "run:" + json);
                        //[3.7]JSON的解析，看见什么就解析什么
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        Log.i(TAG, "run: " + versionName);
                        Log.i(TAG, "run: " + mVersionDes);
                        Log.i(TAG, "run: " + versionCode);
                        Log.i(TAG, "run: " + mDownloadUrl);
                        //[4]比对版本号(服务端和客户端相比较，如果服务端大于本地版本号，提示用户更新)
                        if(mLocalVersionCode < Integer.parseInt(versionCode)){
                            //提示用户更新(弹出对话框(UI))
                            mMessage.what = UPDATE_VERSION;
                        }else {
                            mMessage.what = ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mMessage.what = URL_EXCEPTION;
                } catch (IOException e) {
                    e.printStackTrace();
                    mMessage.what = IO_EXCEPTION;
                } catch (JSONException e) {
                    e.printStackTrace();
                    mMessage.what = JSON_EXCEPTION;
                }finally {
                    //[5]指定睡眠时间，请求网络的时间的时长超过四秒则不做处理
                    //如果请求网络的时间小于小于4秒则强制睡满4秒
                    long endTime = System.currentTimeMillis();
                    if ((endTime -stratTime) <4000) {
                        SystemClock.sleep(4000-(endTime -stratTime));
                    }
                    mHandler.sendMessage(mMessage);
                }
            }
        }.start();
    }
    /**
     * 获取当前应用的本地版本号
     * @return 返回版本号，如果返回0则有异常
     */
    private int getVersionCode() {
        // [1]获取包管理者对象packageManage
        mPackageManagerCode = getPackageManager();
        // [2]从包的管理者对象中获取指定的基本信息(版本名称、版本号)

        // 第一个参数代表当前应用的包名，第二个参数传0代表获取基本信息

        // 获取当前应用的包名用getPackageName();
        try {
            mPackageInfoCode = mPackageManagerCode.getPackageInfo(
                    getPackageName(), 0);
            // [3]获取版本号
            return (mPackageInfoCode.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            // 当前应用的包名没有找到
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 获取当前应用的版本名称，这个名称在清单文件中
     *
     * @return 返回应用版本名称，返回null代表异常
     */
    private String getVersionName() {
        // [1]获取包管理者对象packageManage
        mPackageManagerName = getPackageManager();
        // [2]从包的管理者对象中获取指定的基本信息(版本名称、版本号)

        // 第一个参数代表当前应用的包名，第二个参数传0代表获取基本信息

        // 获取当前应用的包名用getPackageName();
        try {
            mPackageInfoName = mPackageManagerName.getPackageInfo(
                    getPackageName(), 0);
            // [3]获取版本名称
            return (mPackageInfoName.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // 当前应用的报名没有找到
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 这个方法主要是初始化控件的id值
     */
    private void initUI() {
        mShowVersionName = (TextView) findViewById(R.id.tv_SpalshActivity_versionName);
        rl_root = (RelativeLayout) findViewById(R.id.rl_SpalshActivity_root);
    }
}
