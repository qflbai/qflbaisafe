package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private Context mContext;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        //实际上就是标题栏，在Android4.0之后，谷歌推出新的ActionBar
        getActionBar().setDisplayHomeAsUpEnabled(true);//显示(ActionBar)标题栏
        //后两个参数是给盲人用的,不是 盲人就传递0
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,0,0);
        //设置抽屉监听
        mDrawerLayout.setDrawerListener(mToggle);

        //动态添加到主界面中
       getFragmentManager().beginTransaction().replace(R.id.fl_main,new WidgetFragment()).commit();
    }

    /**
     * 初始化布局和控件的id值
     */
    private void init() {

        setContentView(R.layout.activity_main);
        mContext = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);

    }

    /**
     * 在Activity創建之後執行
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();//同步状态
    }

    /**
     * 选项菜单同步
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mToggle.onOptionsItemSelected(item );
        return super.onOptionsItemSelected(item);
    }

    /**
     * 同步配置
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
}
