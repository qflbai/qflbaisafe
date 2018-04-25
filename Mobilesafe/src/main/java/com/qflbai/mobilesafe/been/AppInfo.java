package com.qflbai.mobilesafe.been;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/3/6.
 */

public class AppInfo {
    //名称,包名,图标,(内存,sd卡),(系统,用户)
    public String name;
    public String packageName;
    public Drawable icon;
    public boolean isSdCard;
    public boolean isSystem;
    public String versionName;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public boolean isSdCard() {
        return isSdCard;
    }
    public void setSdCard(boolean isSdCard) {
        this.isSdCard = isSdCard;
    }
    public boolean isSystem() {
        return isSystem;
    }
    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }
}
