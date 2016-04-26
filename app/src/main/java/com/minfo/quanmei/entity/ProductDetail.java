package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 15/10/22.
 */
public class ProductDetail implements Serializable {



    /**
     *  "id": "40",
     "bimg": "images/tehui/simg04.png",
     "oldval": "3000",
     "newval": "2000",
     "dj": "1000",
     "intro": "安师大发生的发撒旦法水电费安师大发安师大发安师大发生的f",
     "detail": "<p>发水电费爱的色放安师大发撒旦法水电费啊隧道股份电话费导航就换个羊肉汤一天热饮太热叶荣添叶荣添寡凫单鹄大发光火大发光火电饭锅</p>",
     "hid": 4,
     "lc": "",
     "lcnote": "",
     "hname": "",
     "himg": "",
     "users": []
     */
    private String id;
    private String bimg;
    private String oldval;
    private String newval;
    private String dj;
    private String intro;
    private String detail;
    private String hid;
    private String lc;
    private String lcnote;
    private String hname;
    private String himg;
    private List<String> users;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBimg() {
        return bimg;
    }

    public void setBimg(String bimg) {
        this.bimg = bimg;
    }

    public String getOldval() {
        return oldval;
    }

    public void setOldval(String oldval) {
        this.oldval = oldval;
    }

    public String getNewval() {
        return newval;
    }

    public void setNewval(String newval) {
        this.newval = newval;
    }

    public String getDj() {
        return dj;
    }

    public void setDj(String dj) {
        this.dj = dj;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
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

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getHimg() {
        return himg;
    }

    public void setHimg(String himg) {
        this.himg = himg;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
                "id='" + id + '\'' +
                ", bimg='" + bimg + '\'' +
                ", oldval='" + oldval + '\'' +
                ", newval='" + newval + '\'' +
                ", dj='" + dj + '\'' +
                ", intro='" + intro + '\'' +
                ", detail='" + detail + '\'' +
                ", hid='" + hid + '\'' +
                ", lc='" + lc + '\'' +
                ", lcnote='" + lcnote + '\'' +
                ", hname='" + hname + '\'' +
                ", himg='" + himg + '\'' +
                ", users=" + users +
                '}';
    }
}
