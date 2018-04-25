package com.kongweijun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongweijun.been.GridViewDataBeen;
import com.kongweijun.mobilesafe.R;

import java.util.List;

/**
 * 继承RecyclerView的适配器
 * Created by Administrator on 2017/2/20.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private OnItemClickListener mListener;
    private List<GridViewDataBeen> lists;
    private Context context;
    private int resource;

    private LayoutInflater inflater ;

    public RecyclerViewAdapter(Context context, List<GridViewDataBeen> lists, int resource) {
        this.context = context;
        this.lists = lists;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, null);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setData(position);
        holder.setOnItemEvent(holder);
    }
    @Override
    public int getItemCount() {
        return lists.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView textview;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_activityhome_gridview_item);
            textview = (TextView) itemView.findViewById(R.id.tv_activityhome_gridview_item);
        }
        public void setData(int position) {
            GridViewDataBeen gd = lists.get(position);
            image.setImageResource(gd.getImage());
            textview.setText(gd.getSec());
        }

        public void setOnItemEvent(final MyViewHolder holder) {
            //设置点击事件回调
            if (mListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int layoutPostion = holder.getLayoutPosition();
                        mListener.onItemClick(holder.itemView,layoutPostion);
                    }
                });
            /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPostion = holder.getLayoutPosition();
                    mListener.onItemLongClick(holder.itemView, layoutPostion);
                    return false;
                }
            });*/
            }
        }
    }
    public interface OnItemClickListener {

        void onItemClick(View view, int position);
        //void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
