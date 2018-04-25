package com.qflbai.mobilesafe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qflbai.mobilesafe.been.GridViewDataBeen;
import com.qflbai.mobilesafe.R;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<GridViewDataBeen> mLists;
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;

    /**
     * 定义一个回调接口作为RecyclerView的点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        //void onItemLongClick(View view, int position);
    }
    private OnItemClickListener mListener;

    public RecyclerViewAdapter(Context context, List<GridViewDataBeen> lists,
                               int resource) {
        this.mContext = context;
        this.mLists = lists;
        this.mResource = resource;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * 为接口暴露一个set方法，方便给用户调用
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 一个是创建viewHolder,相当于listView中的的getView中复用那部分
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mResource, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * //将getView方法变为两个方法：一个是创建viewHolder,
     * 另外一个是绑定视图
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setData(position);//绑定数据
        setOnItemEvent(holder);
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    /**
     * 创建RecyclerView.ViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_activityhome_gridview_item);
            textView = (TextView) itemView.findViewById(R.id.tv_activityhome_gridview_item);
        }

        public void setData(int position) {
            GridViewDataBeen gridViewDataBeen = mLists.get(position);
            imageView.setImageResource(gridViewDataBeen.getImage());
            textView.setText(gridViewDataBeen.getSec());
        }
    }
    protected void setOnItemEvent(final MyViewHolder holder) {
        //设置点击事件回调
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPostion = holder.getLayoutPosition();
                    //mListener其实就是子类对象，调用了该方法
                    mListener.onItemClick(holder.itemView, layoutPostion);
                }
            });
         /*   holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
