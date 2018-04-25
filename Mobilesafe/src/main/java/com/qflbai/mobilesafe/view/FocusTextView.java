package com.qflbai.mobilesafe.view;

/**
 *
 * Created by Administrator on 2017/2/22.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *要为自定义View自定义属性，可以通过declare-styleable声明需要的属性
 *为了在Theme设置View的默认样式，可以同时定义一个format为reference的属性att_a，在定义Theme时为这个attribute指定一个Style，并且在View的第二个构造函数中将R.attr.attr_a 作为第三个参数调用第三个构造函数
 *可以定义一个Style作为Theme中没有定义attr_a时View属性的默认值。
 *可以在Theme中直接为属性赋值，但优先级最低
 *当defStyleAttr（即View的构造函数的第三个参数）不为0且在Theme中有为这个attr赋值时，defStyleRes（通过obtainStyledAttributes的第四个参数指定）不起作用
 *属性值定义的优先级：xml>style>Theme中的默认Sytle>默认Style（通过obtainStyledAttributes的第四个参数指定）>在Theme中直接指定属性值
 * @author Administrator
 *能够获取焦点的自定义TextView
 * 在View类中有四个构造函数，涉及到多个参数，
 *Context：上线文，这个不用多说
 *   AttributeSet attrs： 从xml中定义的参数
 *  int defStyleAttr ：主题中优先级最高的属性
 * int defStyleRes  ： 优先级次之的内置于View的style
 *在android中的属性可以在多个地方进行赋值，涉及到的优先级排序为：
 *Xml直接定义 > xml中style引用 > defStyleAttr > defStyleRes > theme直接定义
  */
public class FocusTextView extends TextView {

    //使用Java代码创建对象时可以就调用该构造方法
    public FocusTextView(Context context) {
        super(context);
    }
    //在XML布局文件中，使用控件的属性(带样式)，系统自动调用
    public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //在XML布局文件中，使用控件的属性，系统自动调用
    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public boolean isFocused() {
        return true;
    }
}
