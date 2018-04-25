package com.qflbai.mobilesafe.been;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/22.
 */

public class GridViewDataBeen implements Serializable {

    private String sec;
    private int image;
    public GridViewDataBeen() {
        super();
    }
    public GridViewDataBeen(String sec, int image) {
        super();
        this.sec = sec;
        this.image = image;
    }
    public String getSec() {
        return sec;
    }
    public void setSec(String sec) {
        this.sec = sec;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "GridViewDataBeen{" +
                "sec='" + sec + '\'' +
                ", image=" + image +
                '}';
    }
}