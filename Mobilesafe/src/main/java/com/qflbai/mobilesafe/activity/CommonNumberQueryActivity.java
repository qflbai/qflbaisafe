package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.qflbai.mobilesafe.been.Child;
import com.qflbai.mobilesafe.been.Group;
import com.qflbai.mobilesafe.engine.CommonNumberDao;
import com.qflbai.mobilesafe.R;

import java.util.List;

public class CommonNumberQueryActivity extends Activity {

    private List<Group> mList;
    private ExpandableListView elv_expandable;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_number_query);
        initView();
        initData();
    }

    /**
     * 初始化数据，填充适配器
     */
    private void initData() {
        mList = CommonNumberDao.getGroup();
        mAdapter = new MyAdapter();
        elv_expandable.setAdapter(mAdapter);
        //给可扩展listview注册点击事件
       elv_expandable.setOnChildClickListener(new ExpandableListView.
               OnChildClickListener() {
           @Override
           public boolean onChildClick(ExpandableListView parent, View v,
                           int groupPosition, int childPosition, long id) {
               startCall(mAdapter.getChild(groupPosition,childPosition)
                       .getNumber());
               return false;
           }
       });
    }

    private void startCall(String number) {
        //ToastUtil.show(getApplicationContext(),"我被点击了",0);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+ number));
        startActivity(intent);
    }

    /**
     * 初始化UI
     */
    private void initView() {
        elv_expandable = (ExpandableListView) findViewById(R.
                id.elv_expandable);
    }

    /**
     *
     */
    class MyAdapter extends BaseExpandableListAdapter{

        private TextView tv_name;
        private TextView tv_number;

        @Override
        public int getGroupCount() {
            return mList.size();
        }

        /**
         * 获取每一组中孩子节点的总数
         * @param groupPosition
         * @return
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return mList.get(groupPosition).getChildList().size();
        }

        /**
         * 获取组的对象
         * @param groupPosition
         * @return
         */
        @Override
        public Group getGroup(int groupPosition) {
            return mList.get(groupPosition);
        }

        /**
         * 获取对应组中的孩子节点对象
         * @param groupPosition 组中的位置
         * @param childPosition 孩子节点的位置
         * @return
         */
        @Override
        public Child getChild(int groupPosition, int childPosition) {
            Group group = mList.get(groupPosition);
            List<Child> childList = group.getChildList();
            Child child = childList.get(childPosition);
            return child;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        /**
         * 获取孩子节点的id
         * @param groupPosition
         * @param childPosition
         * @return
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText(getGroup(groupPosition).getName());
            textView.setTextColor(getResources().getColor(R.color.action_purple));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
            textView.setPadding(90,10,0,10);
            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            View view = null;
            if(view == null){
                view = View.inflate(getApplicationContext(), R.layout.
                        elv_child_item, null);
            }else{
                view = convertView;
            }
            tv_name = (TextView) view.findViewById(R.id.tv_name_text);
            tv_number = (TextView) view.findViewById(R.id.tv_number_text);
            tv_name.setText(getChild(groupPosition,childPosition).getName());
            tv_number.setText(getChild(groupPosition,childPosition).getNumber());
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;//返回true代表孩子节点能被点击
        }
    }
}
