package com.kongweijun.adapter;

/**
 * Created by Administrator on 2017/2/20.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.kongweijun.been.GridViewDataBeen;
import com.kongweijun.mobilesafe.R;
import java.util.List;

public class GridViewBaseAdapter extends BaseAdapter {

    private List<GridViewDataBeen> lists;
    private Context context;
    private int resource;
    private LayoutInflater inflater ;

    public GridViewBaseAdapter(Context context, List<GridViewDataBeen> lists,
                               int resource) {
        this.context = context;
        this.lists = lists;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item mItem = null;
        if(convertView == null){

            convertView = inflater.inflate(resource, null);
            mItem = new Item();
            mItem.image = (ImageView) convertView.findViewById(R.id.iv_activityhome_gridview_item);
            mItem.textView = (TextView) convertView.findViewById(R.id.tv_activityhome_gridview_item);
            convertView.setTag(mItem);

        }else{
            mItem= (Item) convertView.getTag();
        }

        GridViewDataBeen gd = lists.get(position);
        mItem.image.setImageResource(gd.getImage());
        mItem.textView.setText(gd.getSec());
        return convertView;
    }

    private class Item {
        private ImageView image;
        private TextView textView;
    }
}
