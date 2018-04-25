package com.kongweijun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 在说后两个构造函数之前，先说说View的属性，在View中有不同的属性，比如layout_width等，TextView还有textColor这些特有的属性，
 * 我们可以对这些属性进行不同的配置进而实现不同的效果。而且属性也可以在不同的位置进行配置。以TextView为例，android:textColor这个属性可以在多个地方配置，
 * 可以直接写在xml中，可以在xml中以style的形式定义，这两种是我们平时见得较多的，其实还有一种背后的力量可以给属性赋值，那就是主题。
 *我们在android中可以配置一个主题，从而使得一些View即使你不对其进行任何配置，它都会有一些已经默认赋值的属性，这就是主题的功劳。
 *View类的后两个构造函数都是与主题相关的，也就是说，在你自定义View时，如果不需要你的View随着主题变化而变化，有前两个构造函数就OK了，
 * 但是如果你想你的View随着主题变化而变化，就需要利用后两个构造函数了。
 * Created by Administrator on 2017/2/20.
 */

public class FocusTextView extends TextView{

    /**
     * 优先级xml定义的属性>style定义的属性>theme定义的属性
     * 主要是在Java代码中声明一个View时所用，不过如果只用第一个构造函数，
     * 声明的View并没有任何的参数，基本是个空的View对象。
     * @param context
     */
    public FocusTextView(Context context) {
        super(context);
    }

    /**
     * 当我们自定义一个View，且在布局文件中引用时，在系统初始化该View时，
     * 调用的是第二个构造函数，而且我把第二个构造函数中的attrs参数打了出来，
     * 其实，其中的参数attrs是我们在xml中配置的参数。
     * @param context
     * @param attrs
     */
    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 能够获取焦点的的自定义TextView
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
