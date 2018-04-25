package com.qflbai.mobilesafe.domain;

/**
 * Created by Administrator on 2017/3/2.
 */

public class BlackNumberInfo {
    private String phone;
    private String mode;

    public BlackNumberInfo(String phone, String mode) {
        this.phone = phone;
        this.mode = mode;
    }

    public BlackNumberInfo() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "phone='" + phone + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }
}
