package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 15/10/15.
 */
public class StartDiary implements Serializable{
    private String id;
    private String title;
    private String gid;
    private String userid;
    private String ctime;
    private String type;
    private String tid;
    private String intime;
    private String zan;
    private String pl;
    private String pluserid;
    private String plusername;
    private String pluserimg;
    private String pltime;
    private String gname;
    private String text;
    private List<String> img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getPluserid() {
        return pluserid;
    }

    public void setPluserid(String pluserid) {
        this.pluserid = pluserid;
    }

    public String getPlusername() {
        return plusername;
    }

    public void setPlusername(String plusername) {
        this.plusername = plusername;
    }

    public String getPluserimg() {
        return pluserimg;
    }

    public void setPluserimg(String pluserimg) {
        this.pluserimg = pluserimg;
    }

    public String getPltime() {
        return pltime;
    }

    public void setPltime(String pltime) {
        this.pltime = pltime;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String>getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", gid='" + gid + '\'' +
                ", userid='" + userid + '\'' +
                ", ctime='" + ctime + '\'' +
                ", type='" + type + '\'' +
                ", tid='" + tid + '\'' +
                ", intime='" + intime + '\'' +
                ", zan='" + zan + '\'' +
                ", pl='" + pl + '\'' +
                ", pluserid='" + pluserid + '\'' +
                ", plusername='" + plusername + '\'' +
                ", pluserimg='" + pluserimg + '\'' +
                ", pltime='" + pltime + '\'' +
                ", gname='" + gname + '\'' +
                ", text='" + text + '\'' +
                ", img=" + img +
                '}';
    }
}
