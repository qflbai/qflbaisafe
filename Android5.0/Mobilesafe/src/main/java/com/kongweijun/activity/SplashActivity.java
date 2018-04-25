package com.kongweijun.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongweijun.mobilesafe.R;
import com.kongweijun.util.ConstantValue;
import com.kongweijun.util.SpUtil;
import com.kongweijun.util.StreamUtil;
import com.kongweijun.util.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/

public class SplashActivity extends Activity {

    public static final String TAG = "SplashActivity";
    public static final int UPDATE_VERSION =0;//更新版本，弹出对话框
    private static final int ENTER_HOME = 1;//进入主界面
    private static final int URL_EXCEPTION = 2;
    private static final int IO_EXCEPTION = 3;
    private static final int JSON_EXCEPTION = 4;
    private TextView tv_versionName;
    private Context mContext;
    private int mVersionCode;
    private Message mMessage;
    private String mVersionDes;
    private RelativeLayout mRl;
    private String mDownloadUrl;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    //弹出对话框，提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入主界面
                    enterHome();
                    break;
                case IO_EXCEPTION:
                    //进入主界面
                    enterHome();
                    ToastUtil.show(mContext,"IO_EXCEPTION",0);
                    break;
                case JSON_EXCEPTION:
                    //进入主界面
                    ToastUtil.show(mContext,"JSON_EXCEPTION",0);
                    enterHome();
                    break;
                case URL_EXCEPTION:
                    //进入主界面
                    ToastUtil.show(mContext,"URL_EXCEPTION",0);
                    enterHome();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 查查当前方法被其他方法调用的快捷键是alt+ctrl+h
     * 查看当前位置要返回前一个的位置快捷键是Ctrl+alt + ->
     * 显示更新对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.update_downloading_1);
        builder.setTitle("360手机卫士版本更新");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 下载apk，apk的连接地址downLoadUrl
               downloadApk();
            }
        });
        builder.setNeutralButton("不再提醒",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //如果点击了不在提醒对话框，下次进来就不在提醒对话框的更新提示
                // TODO: 2017/2/19
                ToastUtil.show(mContext,"不再提醒",0);
                SpUtil.putBoolean(mContext,ConstantValue.OPEN_UPDATE,false);
                enterHome();
            }
        });
        builder.setNegativeButton("稍后再说",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                enterHome();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                enterHome();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    /**
     * 使用xutils下载文件
     * apk 下载链接地址，放置apk所在的路径
     * 需要判断SD卡是否可以用，是否挂载上
     *  true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
     *  true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
     */
    private void downloadApk() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //获取SD卡的路径
            String path = Environment.getExternalStorageDirectory().getAbsoluteFile()
                    + File.separator + "updateApk.apk";
            HttpUtils hu = new HttpUtils();//发送请求，下载相应的apk
            hu.download(mDownloadUrl, path, true, true, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功的时候调用(下载成功后，放置在result中)
                    File result = responseInfo.result;
                    Log.i(TAG, "onSuccess:" + result);
                    installApk(result);
                }
                @Override
                public void onFailure(HttpException error, String msg) {
                    //下载失败的时候调用
                    Log.i(TAG, "onFailure:" + msg);
                }

                @Override
                public void onStart() {
                    //刚刚下载的时候
                    Log.i(TAG, "onStart:");
                }
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    //下载过程中的时候调用
                    // TODO: 2017/2/20  在这里要添加进条来显示下载文件的大小
                    Log.i(TAG, "onLoading:" +current/total);
                    showProgressDialog(total, current);
                }
            });
        }
    }
    /**
     * 隐式意图安装apk，调用系统安装API
     * @param file     安装apk对应的路径
     */
    private void installApk(File file) {
        //调用系统应用界面，
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivityForResult(intent,0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**

     * @param total  精度条的总大小
     * @param current 当前文件的大小
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
            Log.i(TAG, "对话框关闭了！");
        }
    }
    /**
     * 进入应用程序的主界面中
     */
    private void enterHome() {
        startActivity(new Intent(mContext,HomeActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化上下文环境
        mContext = this;
        //初始化UI
        init();
        //初始化数据
        initData();

        //初始化动画
        initAnimation();
    }
    /**
     * 初始化动画
     */
    private void initAnimation() {

        Animation ah = new AlphaAnimation(0,1);
        ah.setDuration(3000);
        mRl.startAnimation(ah);
    }
    /**
     * 获取数据方法
     */
    private void initData() {
        //[1]获取应用版本名称
        tv_versionName.setText("版本名称:" + getVersionName());
        //[2]获取本地版本号
        mVersionCode = getVersionCode();
        //[3]获取服务端版本号(客户端发送请求，服务端给响应，(json,xml))
        /**
         * json中的内容包括:
         * 更新版本的版本名称
         * 新版本的描述信息
         * 服务器版本号
         * 新版本apk下载地址
         * http://www.oxxx.com/updateApk.json?key = value;
         */

        //在检测新版本之前，应该判断一下是否开启检测更新状态的开关
        if(SpUtil.getBoolean(mContext, ConstantValue.OPEN_UPDATE,false)){
            checkVersion();
        }else {
            //直接进入主界面
            //mHandler.sendMessageDelayed(msg,4000);
            //在发送消息4000秒后去处理，ENTER_HOME,状态码的功能
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,4000);
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
                    String path = "http://192.168.1.103:10086/updateApk.json";//请求json的路径
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
                        String json = StreamUtil.streaToSting(is);
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
                        if(mVersionCode < Integer.parseInt(versionCode)){
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
     * 获取版本号：相应的参数在清单文件中
     * @return 返回0 代表有异常
     */
    private int getVersionCode() {
        //[２.1]实例化包管理者对象
        PackageManager pm = this.getPackageManager();
        //[２.2]从包管理者对象中获取指定包名的基本信息(包括版本名称和版本号)
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //[２.3]获取对于的版本号
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 获取版本名称：相应的参数在清单文件中
     * @return 返回应用版本名称，返回null,代表有异常
     */
    private String getVersionName() {
        //[1.1]实例化包管理者对象
        PackageManager pm = this.getPackageManager();
        //[1.2]从包管理者对象中获取指定包名的基本信息(包括版本名称和版本号)
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //[1.3]获取对于的版本名称
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 快速初始化init()的快捷键是alt+Enter
     * 快速注释是alt+shift+k
     * 查查当前方法被其他方法调用的快捷键是alt+ctrl+h
     * 查看当前位置要返回前一个的位置快捷键是Ctrl+alt + ->
     * 重命名快捷键是shift+N或者是shift+F6
     * 初始化UI
     */
    private void init() {
        tv_versionName = (TextView) findViewById(R.id.tv_versionName);
        mRl = (RelativeLayout)findViewById(R.id.activity_splash);
    }
}
