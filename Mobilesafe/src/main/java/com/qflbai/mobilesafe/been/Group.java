package com.qflbai.mobilesafe.been;

import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */

public class Group {

    private String name;
    private String idx;
    private List<Child> childList;

    public Group() {
    }

    public Group(String name, String idx) {
        this.name = name;
        this.idx = idx;
    }

    public Group(String name, String idx, List<Child> childList) {
        this.name = name;
        this.idx = idx;
        this.childList = childList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", idx='" + idx + '\'' +
                ", childList=" + childList +
                '}';
    }
}
