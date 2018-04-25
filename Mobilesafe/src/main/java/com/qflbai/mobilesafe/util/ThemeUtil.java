package com.qflbai.mobilesafe.util;

import android.app.Activity;
import android.content.Intent;

import com.qflbai.mobilesafe.R;

/**
 * Created by Administrator on 2017/2/23.
 */

public class ThemeUtil {

    private static int mTheme;
    /**
     * @param activity 将要动态设置的activity
     * @param theme 要改变的主题
     */
    public static void changeToTheme(Activity activity, int theme){

        mTheme = theme;
        activity.overridePendingTransition(0, 0);
        Intent intent = new Intent(activity, activity.getClass());
        activity.finish();
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    /**
     * 动态设置activity的主题
     * @param activity
     */
    public static  void setActivityTheme(Activity activity) {

        switch (mTheme) {
            case ConstantValue.APP_THEME:
                activity.setTheme(R.style.AppTheme);
                break;
            case ConstantValue.DEFAULT_THEME:
                activity.setTheme(R.style.DefaultTheme);
                break;
            case ConstantValue.DEFAULT_LIGHT_THEME:
                activity.setTheme(R.style.DefaultLightTheme);
                break;
            case ConstantValue.DEFAULT_LIGH_DARK_THEME:
                activity.setTheme(R.style.DefaultLightDarkTheme);
                break;
            case ConstantValue.RED_THEME:
                activity.setTheme(R.style.RedTheme);
                break;
            case ConstantValue.PINK_THEME:
                activity.setTheme(R.style.PinkTheme);
                break;
            case ConstantValue.PURPLE_THEME:
                activity.setTheme(R.style.PurpleTheme);
                break;
            case ConstantValue.DEEP_PURPLE_THEME:
                activity.setTheme(R.style.DeepPurpleTheme);
                break;
            case ConstantValue.INDIGO_THEME:
                activity.setTheme(R.style.IndigoTheme);
                break;
            case ConstantValue.BLUE_THEME:
                activity.setTheme(R.style.BlueTheme);
                break;
            case ConstantValue.LIGHT_BLUE_THEME:
                activity.setTheme(R.style.LightBlueTheme);
                break;
            case ConstantValue.CYAN_THEME:
                activity.setTheme(R.style.CyanTheme);
                break;
            case ConstantValue.TEAL_THEME:
                activity.setTheme(R.style.TealTheme);
                break;
            case ConstantValue.GREEN_THEME:
                activity.setTheme(R.style.GreenTheme);
                break;
            case ConstantValue.LIGHT_THEME:
                activity.setTheme(R.style.LightGreenTheme);
                break;
            case ConstantValue.LIME_THEME:
                activity.setTheme( R.style.LimeTheme);
                break;
            case ConstantValue.YELLOW_THEME:
                activity.setTheme(R.style.YellowTheme);
                break;
            case ConstantValue.AMBER_THEME:
                activity.setTheme(R.style.AmberTheme);
                break;
            case ConstantValue.ORANGE_THEME:
                activity.setTheme(R.style.OrangeTheme);
                break;
            case ConstantValue.DEEP_ORANGE_THEME:
                activity.setTheme(R.style.DeepOrangeTheme);
                break;
            case ConstantValue.BROWN_THEME:
                activity.setTheme(R.style.BrownTheme);
                break;
            case ConstantValue.GREY_THEME:
                activity.setTheme(R.style.GreyTheme);
                break;
            case ConstantValue.BLUE_GREY_THEME:
                activity.setTheme(R.style.BlueGreyTheme);
                break;
            default:
                break;
        }
    }
}
