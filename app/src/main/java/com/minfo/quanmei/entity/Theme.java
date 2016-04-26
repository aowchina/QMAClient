package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liujing on 15/10/13.
 */
public class Theme implements Serializable{

    /**
     * id : 2
     * name : 上海玫瑰医院专场
     * fname : 高端医美体验
     * simg : http://quanmei.min-fo.com/images/tehui/simg02.png
     * bimg : http://quanmei.min-fo.com/images/bimg02.png
     * hid : 4
     * pid : 0
     * intime : 1444726706
     * amount : null
     */

    private String id;
    private String name;
    private String fname;
    private String simg;
    private String bimg;
    private String hid;
    private String pid;
    private String intime;
    private Object amount;
    private List<PartUser> yyuser;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public void setBimg(String bimg) {
        this.bimg = bimg;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFname() {
        return fname;
    }

    public String getSimg() {
        return simg;
    }

    public String getBimg() {
        return bimg;
    }

    public String getHid() {
        return hid;
    }

    public String getPid() {
        return pid;
    }

    public String getIntime() {
        return intime;
    }

    public Object getAmount() {
        return amount;
    }

    public List<PartUser> getYyuser() {
        return yyuser;
    }

    public void setYyuser(List<PartUser> yyuser) {
        this.yyuser = yyuser;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "amount=" + amount +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fname='" + fname + '\'' +
                ", simg='" + simg + '\'' +
                ", bimg='" + bimg + '\'' +
                ", hid='" + hid + '\'' +
                ", pid='" + pid + '\'' +
                ", intime='" + intime + '\'' +
                ", yyuser=" + yyuser +
                '}';
    }
}
