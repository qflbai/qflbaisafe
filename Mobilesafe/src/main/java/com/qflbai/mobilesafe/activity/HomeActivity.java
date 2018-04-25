package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.qflbai.mobilesafe.adapter.GridViewBaseAdapter;
import com.qflbai.mobilesafe.adapter.RecyclerViewAdapter;
import com.qflbai.mobilesafe.been.GridViewDataBeen;
import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.MD5Util;
import com.qflbai.mobilesafe.util.SpUtil;
import com.qflbai.mobilesafe.util.ThemeUtil;
import com.qflbai.mobilesafe.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.qflbai.mobilesafe.util.ToastUtil.show;

public class HomeActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, MenuItem.OnMenuItemClickListener {

    private Context mContext;
    private List<GridViewDataBeen> mList;
    private GridViewDataBeen mGridViewDataBeen;
    private GridViewBaseAdapter mAdapter;
    private String savePwd;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar mToolbar;
    private String[] menuTitles;
    private GridLayoutManager mGridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       ThemeUtil.setActivityTheme(this);
        super.onCreate(savedInstanceState);
        // 去除当前activity的头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);
        mContext = this;
        initView();
        initToolBar();
        initData();
        initRefresh();
        initRecyclerViewAdapter();
    }
    /**
     * 初始化
     */
    private void initRecyclerViewAdapter() {
        mGridLayoutManager = new GridLayoutManager(mContext,3);
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        RecyclerViewAdapter mRecyclerViewAdapter = new RecyclerViewAdapter(mContext, initData(), R.layout.relativelayout_grid_item);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtil.show(mContext,"RecyclerView的第" + position + ":个条目被点击了",0);
                switch (position) {
                    case 0:
                        showDialog();
                        break;
                    case 1:
                        Intent intent = new Intent(mContext, BlackNumberActivity
                                .class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent2 = new Intent(mContext,AppManagerActivity
                                .class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(mContext,ProcessManagerActivity
                                .class);
                        startActivity(intent3);
                        break;
                    case 4:
                        //跳转到通信卫士模块
                        startActivity(new Intent(getApplicationContext(),
                                TrafficActivity.class));
                        break;
                    case 5:
                        Intent intent5 = new Intent(mContext,AnitVirusActivity
                                .class);
                        startActivity(intent5);
                        break;
                    case 6:
                        startActivity(new Intent(getApplicationContext(),
                                BaseCacheClearActivity.class));
                        //startActivity(new Intent(getApplicationContext(),
                        //CacheClearActivity.class));
                        break;
                    case 7:
                        Intent intent17 = new Intent(mContext,AToolActivity
                                .class);
                        startActivity(intent17);
                        break;
                    case 8:
                        startActivity(new Intent(mContext,SettingActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
    /**
     * 初始化下来刷新
     */
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setProgressBackgroundColor(R.color.refresh_bg);
    }
    /**
     * 初始化toolBar
     */
    private void
    initToolBar() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user);
        Bitmap b = createRoundConerImage(bitmap);
        mToolbar.setNavigationIcon(new BitmapDrawable(b));
        mToolbar.setLogo(R.drawable.ic_launcher);
        //mToolbar.setLogoDescription(R.string.title_activity_contact_list);
        mToolbar.setTitle("功能列表");
        //mToolbar.setSubtitle(R.string.setup2_text);
        setActionBar(mToolbar);
    }
    private Bitmap createRoundConerImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        int size = Math.min(source.getWidth(),source.getHeight());
        Bitmap target = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, size, size);
        canvas.drawRoundRect(rect, size /2, size/2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
    /**
     * 显示两种对话框，初始设置密码对话框 确认密码对话框
     */
    protected void showDialog() {
        savePwd = SpUtil
                .getString(mContext, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(savePwd)) {
            // 设置初始密码对话框
            showSetPwdDialog();
        } else {
            // 设置确认密码对话框
            showSetConfirmPwdDialog();
        }
    }
    /**
     * 设置确认密码对话框
     */
    private void showSetConfirmPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialot_setconfirm_pwd_item,
                null);
        final EditText confirm_pwd = (EditText) view
                .findViewById(R.id.et_confirm_dialot_set_pwd);
        Button cancel = (Button) view
                .findViewById(R.id.bt_confirm_dialot_set_cancel);
        Button submit = (Button) view
                .findViewById(R.id.bt_confirm_dialot_setconfirm_submit);
        /*dialog.setView(view,0,0,0);*/
        dialog.setView(view,0,0,0,0);
        dialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oncePwd = confirm_pwd.getText().toString().trim();
                if (!(TextUtils.isEmpty(oncePwd))) {
                    if (savePwd.equals(MD5Util.encrypt(oncePwd))) {
                        // 进入手机防盗界面
                        Intent intent = new Intent(mContext,SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        show(mContext, "确认密码有误", 0);
                    }
                } else {
                    ToastUtil.show(mContext, "你输入的确认密码为空", 0);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    /**
     * 设置初始密码对话框
     */
    private void showSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialot_set_pwd_item, null);
        //dialog.setView(view);
        dialog.setView(view,0,0,0,0);
        dialog.show();
        Button submit = (Button) view
                .findViewById(R.id.bt_dialot_setconfirm_submit);
        Button cancel = (Button) view.findViewById(R.id.bt_dialot_set_cancel);
        final EditText pwd = (EditText) view
                .findViewById(R.id.et_dialot_set_pwd);
        final EditText confirm_pwd = (EditText) view
                .findViewById(R.id.et_dialot_setconfirm_pwd);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confirm_pwdString = confirm_pwd.getText().toString()
                        .trim();
                String pwdString = pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwdString)
                        || TextUtils.isEmpty(confirm_pwdString)) {
                    // 密码为空
                    ToastUtil.show(mContext, "密码不能为空", 0);
                } else {
                    if (pwdString.equals(confirm_pwdString)) {
                        // 进入手机防盗界面
                        Intent intent = new Intent(mContext, SetupOverActivity.class);
                        startActivity(intent);
                        SpUtil.putString(mContext,
                                ConstantValue.MOBILE_SAFE_PSD,MD5Util.encrypt(pwdString));
                        dialog.dismiss();
                    } else {
                        ToastUtil.show(mContext, "两次输入的密码不一致", 0);
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    /**
     * 初始化gridView的数据
     * @return
     */
    private List<GridViewDataBeen> initData() {
        // 准备数据(文字(9组),图片(9张))
        String[] sec = { "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
                "缓存清理", "高级工具", "设置中心" };
        int[] image = { R.drawable.home_safe, R.drawable.home_callmsgsafe,
                R.drawable.home_apps, R.drawable.home_taskmanager,
                R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools,
                R.drawable.home_settings };
        mList = new ArrayList<GridViewDataBeen>();
        for (int i = 0; i < image.length; i++) {
            mGridViewDataBeen = new GridViewDataBeen(sec[i], image[i]);
            mList.add(mGridViewDataBeen);
        }
        return mList;
    }
    /**
     * 初始化控件(view)的id
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },3000);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SubMenu subMenu = menu.addSubMenu("设置布局");
        subMenu.add(0,1,0,"默认布局");
        subMenu.add(0,2,0,"传统布局");
        subMenu.add(0,3,0,"纵向列表");
        subMenu.add(0,4,0,"纵向九宫格");
        subMenu.add(0,5,0,"横向九宫格");
        subMenu.add(0,6,0,"纵向瀑布流");
        subMenu.add(0,7,0,"横向瀑布流");
        subMenu.add(0,3,0,"横向列表");
        if (menuTitles == null) {
            menuTitles = getResources().getStringArray(R.array.style_names);
        }
        for (String name : menuTitles){
            MenuItem menuItem = menu.add(name);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menuItem.setOnMenuItemClickListener(this);
        }
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                ToastUtil.show(mContext,"默认布局!",0);
                initRecyclerViewAdapter();
                break;
            case 2:
                ToastUtil.show(mContext,"GridView!",0);
                startActivity(new Intent(mContext,HomeActivity1.class));
                finish();
                break;
            case 3:
                ToastUtil.show(mContext,"ListView!",0);
                break;
            case 4:
                ToastUtil.show(mContext,"纵向九宫格!",0);
                break;
            case 5:
                mGridLayoutManager = new GridLayoutManager(mContext,2);
                mGridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
                mRecyclerView.setLayoutManager(mGridLayoutManager);
                mRecyclerView.setHasFixedSize(true);
                RecyclerViewAdapter mRecyclerViewAdapter = new RecyclerViewAdapter(mContext, initData(), R.layout.relativelayout_grid_item);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
                ToastUtil.show(mContext,"横向九宫格!",0);
                break;
            case 6:
                ToastUtil.show(mContext,"纵向瀑布流!",0);
                break;
            case 7:
                ToastUtil.show(mContext,"横向瀑布流!",0);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 点击了上面的改变按钮，则会改变当前页面的布局
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        for (int i = 0; i < menuTitles.length; i++) {

            if (item.getTitle().equals(menuTitles[i])) {
                Toast.makeText(this, item.getTitle()+"i"+ i, Toast.LENGTH_SHORT).show();
                ThemeUtil.changeToTheme(this,i);
                SpUtil.putInt(mContext,ConstantValue.CHANGE_TO_THEME,i);
                return true;
            }
        }
        return false;
    }
}
