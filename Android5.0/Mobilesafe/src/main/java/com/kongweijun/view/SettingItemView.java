package com.kongweijun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongweijun.mobilesafe.R;

/**
 * Created by Administrator on 2017/2/21.
 */

public class SettingItemView extends RelativeLayout{

    private static final String NAME_SPACE = "http://schemas.android.com/apk/res-auto";
    private CheckBox cb;
    private TextView tv_sec;
    private static final String TAG = "SettingItemView";
    private TextView tv_title;
    private String mDesOff;
    private String mDesOn;
    private String mDestitle;

    public SettingItemView(Context context) {
        super(context);
        //this(context,null);
        init(context);
    }
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // this(context,null,0);
        init(context);
        /**
         * 获取自定义以及原生属性的操作，写在此处，在attrs对象中获取
         */
        initAttrs(attrs);
    }

    /**
     * @param context
     *            上下文环境
     * @param attrs
     *            保存有控件的所有属性
     * @param defStyleAttr
     */
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttrs(attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        initAttrs(attrs);
    }

    /**
     *初始化构造方法，无论属性都能够调用这四个构造方法
     * 将XML转化成view，将设置界面的一个条目转换成view对象
     */
    private void init(Context context) {

        //将布局文件转化成view对象
        View view = View.inflate(context, R.layout.setting_item_view,this);
        /**
         * View view1 = View.inflate(context, R.layout.setting_item_view,null);
         * this.addView(view1);
         * 将xml----view将设置界面的一个item转换为view对象,this代表直接把当前对象view添加到SettingItemView中
         */
        tv_sec = (TextView) this.findViewById(R.id.tv_settingActivit_sec);
        cb = (CheckBox) this.findViewById(R.id.cb_settingActivit);
        tv_title = (TextView) this.findViewById(R.id.tv_settingActivit_title);
    }
    /**
     * @param attrs
     *  构造方法中维护好的属性集合 返回属性集合中自定义属性的属性值 通过命名空间获取属性名称和属性值
     */
    private void initAttrs(AttributeSet attrs) {
        mDesOff = attrs.getAttributeValue(NAME_SPACE, "desOff");
        mDesOn = attrs.getAttributeValue(NAME_SPACE, "desOn");
        mDestitle = attrs.getAttributeValue(NAME_SPACE, "destitle");
        tv_title.setText(mDestitle);
    }
    /**
     * 判断是否开启的状态
     * @return 返回当前SettingItemView是否选中状态。true代表选中，返回false则代表未选中。 返回true则整个条目选中
     */
    public boolean isCheck() {

        return cb.isChecked();// 当CheckBox选中返回true,否则返回false。
    }

    /**
     * 切换状态的方法
     * @param isCheck
     * 是否作为开启的变量，由点击过程决定
     */
    public void setCheck(boolean isCheck) {

        cb.setChecked(isCheck);

        if (isCheck) {

            tv_sec.setText(mDesOn);

        } else {
            tv_sec.setText(mDesOff);
        }
    }
}
