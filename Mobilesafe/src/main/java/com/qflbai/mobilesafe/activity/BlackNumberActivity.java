package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.qflbai.mobilesafe.adapter.BlackNumberBaseAdapter;
import com.qflbai.mobilesafe.db.dao.BlackNumberDao;
import com.qflbai.mobilesafe.domain.BlackNumberInfo;
import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ThemeUtil;
import com.qflbai.mobilesafe.util.ToastUtil;

import java.util.ArrayList;

public class BlackNumberActivity extends Activity {
    private int mCount;
    private BlackNumberDao mDao;
    private ListView mListView;
    private Button mBtn_add;
    private Context mContext;
    private ArrayList<BlackNumberInfo> mList;
    private BlackNumberBaseAdapter mAdapter;
    private int mode = 1;
    private boolean mIsLoad = false;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            initAdapter();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.setActivityTheme(this);
        super.onCreate(savedInstanceState);
        // 去除当前activity的头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_black_number);
        mContext = this;
        initView();
        initData();
    }
    /**
     * 初始化适配器
     */
    private void initAdapter() {
        if(mAdapter == null){
            mAdapter = new BlackNumberBaseAdapter(mContext,
                    mList, R.layout.listview_blacknumber_item);
            mListView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 从数据库中获取的数据
     * 初始化listview需要的数据
     */
    private ArrayList<BlackNumberInfo> initData() {
        new Thread(){
            @Override
            public void run() {
                isLoadData(0,15);
            }
        }.start();
        return mList;
    }

    /**
     * @param index 要加载的索引值
     * @param count   一次要加载的总条目数
     */
    private void isLoadData(int index,int count) {
        mDao = BlackNumberDao.getInstance(mContext);
        //mList = mDao.queryAll();
        mList = mDao.query(index,count);
        mCount = mDao.getCount();
        mHandler.sendEmptyMessage(0);
    }

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialog_add_blacknumber, null);
        dialog.setView(view);
        dialog.show();
        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button)view.findViewById(R.id.bt_cancel);

        //监听其选中条目的切换过程
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                group.getId();
                switch (checkedId) {
                    case R.id.rb_sms:
                        //拦截短信
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        //拦截电话
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        //拦截所有
                        mode = 3;
                        break;
                }
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1,获取输入框中的电话号码
                String phone = et_phone.getText().toString().trim();
                if(!TextUtils.isEmpty(phone)){
                    //2,数据库插入当前输入的拦截电话号码
                    mDao.insert(phone, mode+"");
                    //3,让数据库和集合保持同步(1.数据库中数据重新读一遍,2.手动向集合中添加一个对象(插入数据构建的对象))
                    BlackNumberInfo info= new BlackNumberInfo(phone,
                            mode+"");
                    //4,将对象插入到集合的最顶部
                    mList.add(0,info);
                    //5,通知数据适配器刷新(数据适配器中的数据有改变了)
                    if(mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }
                    //6,隐藏对话框
                    dialog.dismiss();
                }else{
                    ToastUtil.show(mContext, "请输入拦截号码",0);
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 初始化控件的id值
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_blackNumber);
        mBtn_add = (Button) findViewById(R.id.btn_add);
        mBtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //监听listview的滚动状态
        initListViewScroll();
    }
    /**
     * 加载更多的触发条件
     * (1)滚动到最底部，最后一个listViewde 条目可以看见
     * (2)滚动状态发生改变，滚动----停止(空闲)
     * (3)监听状态改变
     */
    private void initListViewScroll() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            /**
             * 当滚动过程中状态发生改变调用方法
             * OnScrollListener.SCROLL_STATE_FLING飞速滚动
             * OnScrollListener.SCROLL_STATE_IDLE空闲状态
             * OnScrollListener.SCROLL_STATE_TOUCH_SCROLL拿手触摸着去滚动状态
             * @param view
             * @param scrollState
             * 条件一:滚动到停止状态
             * 条件二:最后一个条目可见(最后一个条目的索引值>=数据适配器中集合的总
             * 条目个数-1)
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if(mList != null) {
                    if ((AbsListView.OnScrollListener.SCROLL_STATE_IDLE ==
                            scrollState) && (mListView.getLastVisiblePosition()
                            >= mList.size() - 1)&&(!mIsLoad)) {
                        /**
                         * mIsLoad防止重复加载的变量
                         *如果当前正在加载mIsLoad就会为true,本次加载完毕后,
                         * 再将mIsLoad改为false
                         *如果下一次加载需要去做执行的时候,会判断上诉mIsLoad变量,
                         * 是否为false,如果为true,
                         * 就需要等待上一次加载完成,将其值
                         *改为false后再去加载
                         *如果条目总数大于集合大小的时,才可以去继续加载更多
                         */
                        if (mCount>mList.size()){
                            new Thread(){
                                @Override
                                public void run() {
                                    mDao = BlackNumberDao.getInstance(mContext);
                                    //mList = mDao.queryAll();
                                    //获取下页的数据
                                    ArrayList<BlackNumberInfo> moreData =
                                            mDao.query(mList.size(), 15);
                                    mList.addAll(moreData);
                                    //把下一页的数据加载上一页的数据后面
                                    mHandler.sendEmptyMessage(0);
                                }
                            }.start();
                        }
                    }
                }
            }

            //滚动过程中调用方法
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int
                    visibleItemCount, int totalItemCount) {

            }
        });
    }
}
