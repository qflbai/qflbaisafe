package com.qflbai.mobilesafe.been;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/3/9.
 */
public class ProcessInfo{
    private String name;//应用名称
    private Drawable icon;//应用图标
    private long memSize;//应用已使用的内存数
    private boolean isCheck;//是否被选中
    private boolean isSystem;//是否为系统应用
    private String packageName;//如果进程没有名称,则将其所在应用的包名最为名称

    public ProcessInfo() {
    }

    public ProcessInfo(String name, Drawable icon, long memSize, boolean isCheck,
                       boolean isSystem, String packageName) {
        this.name = name;
        this.icon = icon;
        this.memSize = memSize;
        this.isCheck = isCheck;
        this.isSystem = isSystem;
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getMemSize() {
        return memSize;
    }

    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", memSize=" + memSize +
                ", isCheck=" + isCheck +
                ", isSystem=" + isSystem +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
