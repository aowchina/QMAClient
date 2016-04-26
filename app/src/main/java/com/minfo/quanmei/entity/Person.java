package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 15/10/15.
 */
public class Person implements Serializable {
    private String userid;
    private String userimg;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    @Override
    public String toString() {
        return "Person{" +
                "userid='" + userid + '\'' +
                ", userimg='" + userimg + '\'' +
                '}';
    }
}
