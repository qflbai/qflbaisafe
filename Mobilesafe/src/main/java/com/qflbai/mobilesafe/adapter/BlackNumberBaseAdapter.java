package com.qflbai.mobilesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qflbai.mobilesafe.db.dao.BlackNumberDao;
import com.qflbai.mobilesafe.domain.BlackNumberInfo;
import com.qflbai.mobilesafe.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */
public class BlackNumberBaseAdapter extends BaseAdapter{

    private List<BlackNumberInfo> mLists;
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;
    private BlackNumberInfo mInfo;


    public BlackNumberBaseAdapter(Context context, List<BlackNumberInfo> lists,
                               int resource) {
        this.mContext = context;
        this.mLists = lists;
        this.mResource = resource;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mLists.size();
    }
    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Item mItem = null;
        if(convertView == null){
            convertView =mInflater.inflate(mResource,parent,false);
            mItem = new Item();
            mItem.iv_delete = (ImageView)convertView.findViewById(R.id.
                    iv_delete);
            mItem.tv_phone = (TextView)convertView.findViewById(R.id
                    .tv_phone);
            mItem.tv_mode = (TextView)convertView.findViewById(R.id.
                    tv_mode);
            convertView.setTag(mItem);
        }else{
            mItem = (Item) convertView.getTag();
        }
        mInfo = mLists.get(position);
        mItem.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlackNumberDao mDao = BlackNumberDao.getInstance(mContext);
                mDao.delete(mLists.get(position).getPhone());
                mLists.remove(position);
                notifyDataSetChanged();
            }
        });
        int mode = Integer.parseInt(mInfo.getMode());
        switch (mode) {
            case 1:
                mItem.tv_mode.setText("拦截短信");
                break;
            case 2:
                mItem.tv_mode.setText("拦截电话");
                break;
            case 3:
                mItem.tv_mode.setText("拦截所有");
                break;
            default:
                break;
        }
        mItem.tv_phone.setText(mInfo.getPhone());
        return convertView;
    }
    //将item类定义为静态，不会去创建多个对象
    private static class Item {
        private ImageView iv_delete;
        private TextView tv_mode;
        private TextView tv_phone;
    }
    /**
     *  listview如果有个多个条目的时候，我们可以分页操作，我们可以约定查询数据的一次加载20
     *条，逆序输出
     *按照_id的逆序排序，查询后20条数据(limit 中第一位代表逆序的索引值，第二位代表查询到的条目个数);
     *select * from blacknumber order by _id desc limit 0,20;
     */
}
