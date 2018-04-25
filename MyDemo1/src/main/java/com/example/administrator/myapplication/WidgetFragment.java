package com.example.administrator.myapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Administrator on 2017/2/16.
 */

public class WidgetFragment extends Fragment implements View.OnClickListener {

    private Button mBtn;
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private Button mBtn4;
    private Button mBtn5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_widget,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtn = (Button)view.findViewById(R.id.btn);
        mBtn1 = (Button) view.findViewById(R.id.btn1);
        mBtn2 = (Button) view.findViewById(R.id.btn2);
        mBtn3 = (Button) view.findViewById(R.id.btn3);
        mBtn4 = (Button) view.findViewById(R.id.btn4);
        mBtn5 = (Button) view.findViewById(R.id.btn5);

        //事件的监听事件
        mBtn.setOnClickListener(this);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn:
                Intent intent = new Intent(getActivity(), WidgetActivity.class);
                startActivity(intent);
                break;
            case R.id.btn1:
                break;
            case R.id.btn2:
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
            case R.id.btn5:
                break;
            default:
                break;
        }
    }
}
