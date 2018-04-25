package com.qflbai.mobilesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qflbai.mobilesafe.been.GridViewDataBeen;
import com.qflbai.mobilesafe.R;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public class GridViewBaseAdapter extends BaseAdapter {
    private List<GridViewDataBeen> mLists;
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;

    public GridViewBaseAdapter(Context context, List<GridViewDataBeen> lists,
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Item mItem = null;
        if(convertView == null){
            convertView =mInflater.inflate(mResource,parent,false);
            mItem = new Item();
            mItem.image = (ImageView) convertView.findViewById(R.id.iv_activityhome_gridview_item);
            mItem.textView = (TextView) convertView.findViewById(R.id.tv_activityhome_gridview_item);
            convertView.setTag(mItem);
        }else{
            mItem= (Item) convertView.getTag();
        }
        GridViewDataBeen gd = mLists.get(position);
        mItem.image.setImageResource(gd.getImage());
        mItem.textView.setText(gd.getSec());
        return convertView;
    }
    private class Item {
        private ImageView image;
        private TextView textView;
    }
}
