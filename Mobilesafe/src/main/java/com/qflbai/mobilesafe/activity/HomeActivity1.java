package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.qflbai.mobilesafe.adapter.GridViewBaseAdapter;
import com.qflbai.mobilesafe.adapter.RecyclerViewAdapter;
import com.qflbai.mobilesafe.been.GridViewDataBeen;
import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.MD5Util;
import com.qflbai.mobilesafe.util.SpUtil;
import com.qflbai.mobilesafe.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.qflbai.mobilesafe.util.ToastUtil.show;
/**
 * Created by Administrator on 2017/2/19.
 */
public class HomeActivity1 extends Activity{
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
        setContentView(R.layout.activity_home1);
        mContext = this;
        initView();
        initData();
        initAdapter();

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
     * 初始化自定义GridView的适配器
     */
    private void initAdapter() {

        List<GridViewDataBeen> lists = initData();
        mAdapter = new GridViewBaseAdapter(mContext, lists,
                R.layout.activity_home1_gridview_item);
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
        gv = (GridView) findViewById(R.id.gv_homeActivity);
    }
}
