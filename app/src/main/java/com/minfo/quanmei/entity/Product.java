package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 15/8/24.
 * 产品信息
 */
public class Product implements Serializable{

    private int id;//id
    private String name;//名称
    private String fname;//副标题
    private String oldval;//原价
    private String newval;//新价
    private String simg;//小图
    private String bimg;//大图
    private int dj;//定金
    private String intro;//介绍
    private String lc;//流程说明
    private String lcnote;//流程备注
    private String detail;//详细说明
    private int hid;//医院id
    private int pid;//项目id
    private int tid;//类别id
    private int intime;//写入时间
    private String hname;//医院名称
    private String himg;//医院logo
    private int sellout;//已卖出个数

    public Product() {    }



    public String getBimg() {
        return bimg;
    }

    public void setBimg(String bimg) {
        this.bimg = bimg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getDj() {
        return dj;
    }

    public void setDj(int dj) {
        this.dj = dj;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public String getHimg() {
        return himg;
    }

    public void setHimg(String himg) {
        this.himg = himg;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIntime() {
        return intime;
    }

    public void setIntime(int intime) {
        this.intime = intime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getLcnote() {
        return lcnote;
    }

    public void setLcnote(String lcnote) {
        this.lcnote = lcnote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewval() {
        return newval;
    }

    public void setNewval(String newval) {
        this.newval = newval;
    }

    public String getOldval() {
        return oldval;
    }

    public void setOldval(String oldval) {
        this.oldval = oldval;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getSimg() {
        return simg;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getSellout() {
        return sellout;
    }

    public void setSellout(int sellout) {
        this.sellout = sellout;
    }



    @Override
    public String toString() {
        return "Product{" +
                "bimg='" + bimg + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", fname='" + fname + '\'' +
                ", oldval=" + oldval +
                ", newval=" + newval +
                ", simg='" + simg + '\'' +
                ", dj=" + dj +
                ", intro='" + intro + '\'' +
                ", lc='" + lc + '\'' +
                ", lcnote='" + lcnote + '\'' +
                ", detail='" + detail + '\'' +
                ", hid=" + hid +
                ", pid=" + pid +
                ", tid=" + tid +
                ", intime=" + intime +
                ", hname='" + hname + '\'' +
                ", himg='" + himg + '\'' +
                ", sellout=" + sellout +
                '}';
    }
}