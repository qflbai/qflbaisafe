package com.qflbai.mobilesafe.been;

/**
 * Created by Administrator on 2017/3/18.
 */

public class Child {

    private String id;
    private String number;
    private String name;

    public Child() {
    }

    public Child(String id, String number, String name) {
        this.id = id;
        this.number = number;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Child{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
