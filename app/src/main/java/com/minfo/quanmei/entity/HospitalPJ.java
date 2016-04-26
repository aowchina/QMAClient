package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 15/12/12.
 */
public class HospitalPJ implements Serializable {

    /**
     * id : 4
     * pj : [{"id":"1","userid":"599677187","text":"这是一家神奇的医院","hua":"3","sm":"5","hj":"5","fw":"5","hid":"4","intime":"1449909807","orderid":"quanmei20151211173048","username":"刘静哈哈","userimg":"http://quanmei.min-fo.com/http://quanmei.min-fo.com/files/user/43/3a/7d/22/599677187/1449452714.jpg","pubtime":"2015-12-12","stars":"5.0","simg":"http://quanmei.min-fo.com/images/bimg01.png","name":"纹绣特惠一","fname":"纹绣前缀一"}]
     */


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
     * username : 刘静哈哈
     * userimg : http://quanmei.min-fo.com/http://quanmei.min-fo.com/files/user/43/3a/7d/22/599677187/1449452714.jpg
     * pubtime : 2015-12-12
     * stars : 5.0
     * simg : http://quanmei.min-fo.com/images/bimg01.png
     * name : 纹绣特惠一
     * fname : 纹绣前缀一
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
    private String userimg;
    private String pubtime;
    private String stars;
    private String simg;
    private String name;
    private String fname;

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

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFname(String fname) {
        this.fname = fname;
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

    public String getUserimg() {
        return userimg;
    }

    public String getPubtime() {
        return pubtime;
    }

    public String getStars() {
        return stars;
    }

    public String getSimg() {
        return simg;
    }

    public String getName() {
        return name;
    }

    public String getFname() {
        return fname;
    }

}
