package com.kongweijun.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.kongweijun.adapter.GridViewBaseAdapter;
import com.kongweijun.adapter.RecyclerViewAdapter;
import com.kongweijun.been.GridViewDataBeen;
import com.kongweijun.mobilesafe.R;
import com.kongweijun.util.ConstantValue;
import com.kongweijun.util.MD5Util;
import com.kongweijun.util.SpUtil;
import com.kongweijun.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.kongweijun.util.ToastUtil.show;
/**
 * Created by Administrator on 2017/2/19.
 */
public class HomeActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private GridView gv;
    private List<GridViewDataBeen> mList;
    private GridViewDataBeen mGridViewDataBeen;
    private GridViewBaseAdapter mAdapter;
    private String savePwd;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        initView();
        refresh();
        initData();
        //initAdapter();
        initRecyclerView();
    }
    /**
     * 初始化RecyclerView控件和适配器
     */
    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerViewAdapter = new RecyclerViewAdapter(mContext,initData(), R.layout.grid_item);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        showDialog();
                        break;
                    case 8:
                        Toast.makeText(mContext, "position:"+position, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(mContext,SettingActivity.class));
                        //finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }
    /**
     * 显示两种对话框，初始设置密码对话框 确认密码对话框
     */
    private void showDialog() {

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
        dialog.setView(view,0,0,0,0);
        dialog.show();
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String oncePwd = confirm_pwd.getText().toString().trim();
                if (!(TextUtils.isEmpty(oncePwd))) {
                    if (savePwd.equals(MD5Util.encrypt(oncePwd))) {
                        // 进入手机防盗界面
                        Intent intent = new Intent(mContext,SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        ToastUtil.show(mContext, "确认密码有误", 0);
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
            public void onClick(View v) {
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
                                ConstantValue.MOBILE_SAFE_PSD, MD5Util.encrypt(pwdString));
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
     * 下拉刷新
     */
    private void refresh() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mRefreshLayout.setProgressBackgroundColor(R.color.refresh_bg);
    }
    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        },3000);
    }
    /**
     * 初始化自定义GridView的适配器
     */
    private void initAdapter() {

        List<GridViewDataBeen> lists = initData();
        mAdapter = new GridViewBaseAdapter(mContext, lists,
                R.layout.activity_home_gridview_item);
        gv.setAdapter(mAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 8:
                        show(mContext,"position:"+position,0);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    /**
     * 初始化gridView的数据
     *
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
        //gv = (GridView) findViewById(R.id.gv_homeActivity);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
    }
}
