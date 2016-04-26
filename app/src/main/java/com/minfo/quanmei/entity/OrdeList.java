package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 15/9/29.
 */
public class OrdeList implements Serializable{

    /**
     * id : 84
     * hid : 5
     * status : 1
     * simg : http://quanmei.min-fo.com/images/tehui/simg04.png
     * name : 眼部特惠1
     * dj : 1000
     * userid : 599677187
     * hname : 北京医院二
     * tid : 12
     * orderid : quanmei20151211185006
     * intime : 1449831006
     * fname : 眼部前缀1
     */
    private String id;
    private int hid;
    private String status;
    private String simg;
    private String name;
    private String dj;
    private String userid;
    private String hname;
    private String tid;
    private String orderid;
    private String intime;
    private String fname;
    private String tel;
    private String newval;

    public String getNewval() {
        return newval;
    }

    public void setNewval(String newval) {
        this.newval = newval;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDj(String dj) {
        this.dj = dj;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getId() {
        return id;
    }

    public int getHid() {
        return hid;
    }

    public String getStatus() {
        return status;
    }

    public String getSimg() {
        return simg;
    }

    public String getName() {
        return name;
    }

    public String getDj() {
        return dj;
    }

    public String getUserid() {
        return userid;
    }

    public String getHname() {
        return hname;
    }

    public String getTid() {
        return tid;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getIntime() {
        return intime;
    }

    public String getFname() {
        return fname;
    }

    @Override
    public String toString() {
        return "OrdeList{" +
                "id='" + id + '\'' +
                ", hid=" + hid +
                ", status='" + status + '\'' +
                ", simg='" + simg + '\'' +
                ", name='" + name + '\'' +
                ", dj='" + dj + '\'' +
                ", userid='" + userid + '\'' +
                ", hname='" + hname + '\'' +
                ", tid='" + tid + '\'' +
                ", orderid='" + orderid + '\'' +
                ", intime='" + intime + '\'' +
                ", fname='" + fname + '\'' +
                '}';
    }
}
