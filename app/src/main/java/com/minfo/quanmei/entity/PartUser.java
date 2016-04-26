package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 15/10/13.
 */
public class PartUser implements Serializable{

    private int userid;
    private String userimg;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
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
        return "PartUser{" +
                "userid=" + userid +
                ", userimg='" + userimg + '\'' +
                '}';
    }
}
