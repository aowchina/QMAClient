package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 15/12/12.
 */
public class Comments implements Serializable {

    /**
     * id : 1
     * userid : 599677187
     * text : 这是一家神奇的医院
     * hua : 3
     * sm : 5
     * hj : 5
     * fw : 5
     * hid : 4
     * intime : 1449909807
     * orderid : quanmei20151211173048
     * username : -27#-120#-104#-23#-99#-103#-27#-109#-120#-27#-109#-120#
     * pubtime : 2015-12-12
     */

    private String id;
    private String userid;
    private String text;
    private String hua;
    private String sm;
    private String hj;
    private String fw;
    private String hid;
    private String intime;
    private String orderid;
    private String username;
    private String pubtime;

    public void setId(String id) {
        this.id = id;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHua(String hua) {
        this.hua = hua;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public void setHj(String hj) {
        this.hj = hj;
    }

    public void setFw(String fw) {
        this.fw = fw;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public String getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getText() {
        return text;
    }

    public String getHua() {
        return hua;
    }

    public String getSm() {
        return sm;
    }

    public String getHj() {
        return hj;
    }

    public String getFw() {
        return fw;
    }

    public String getHid() {
        return hid;
    }

    public String getIntime() {
        return intime;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getUsername() {
        return username;
    }

    public String getPubtime() {
        return pubtime;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "fw='" + fw + '\'' +
                ", id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", text='" + text + '\'' +
                ", hua='" + hua + '\'' +
                ", sm='" + sm + '\'' +
                ", hj='" + hj + '\'' +
                ", hid='" + hid + '\'' +
                ", intime='" + intime + '\'' +
                ", orderid='" + orderid + '\'' +
                ", username='" + username + '\'' +
                ", pubtime='" + pubtime + '\'' +
                '}';
    }
}
