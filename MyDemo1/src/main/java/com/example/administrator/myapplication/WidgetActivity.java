package com.example.administrator.myapplication;


import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/2/16.
 */
public class WidgetActivity extends Activity {
    private RecyclerView mRly;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_widget);
        mRly = (RecyclerView) findViewById(R.id.rlv);
        //RecyclerView 可以实现水平垂直listview和gridView,瀑布流
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRly.setLayoutManager(llm);
        mRly.setAdapter(new RecyclerViewAdapter());
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private MyViewHolder mViewHolder;
        private MyViewHolder mHolder;

        //将getView方法变为两个方法：一个是创建viewHolder,
        //另外一个是绑定视图

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View inflate = View.inflate(parent.getContext(), R.layout.widget_item, null);
            mViewHolder = new MyViewHolder(inflate);

            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mImageView.setImageResource(R.drawable.g1);
        }


        /**
         * 和getCount方法一样
         * @return
         */
        @Override
        public int getItemCount() {
            return 20;
        }
    }

    /**
     * 创建RecyclerView.ViewHolder
     */
   class MyViewHolder extends RecyclerView.ViewHolder{

     ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}
