package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liujing on 15/10/2.
 */
public class NoteFirstReply extends NoteReply implements Serializable {

    /**
     * id : 12
     * userid : 14999534
     * userid2 : 0
     * wid : 93
     * intime : 1445930331
     * userimg : http://ww1.sinaimg.cn/crop.0.0.610.610.1024/b1e069d1gw1ehiibnhw44j20h00h00wi.jpg
     * username : 客棹孤舟
     * pubtime : 一个月前
     * text :
     */

    private String id;
    private String userid;
    private int userid2;
    private String wid;
    private String intime;
    private String userimg;
    private String username;
    private String pubtime;
    private String text;
    private int status;

    private int more_hf;

    private List<SecondReply> son;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SecondReply> getSon() {
        return son;
    }

    public void setSon(List<SecondReply> son) {
        this.son = son;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUserid2(int userid2) {
        this.userid2 = userid2;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public int getUserid2() {
        return userid2;
    }

    public String getWid() {
        return wid;
    }

    public String getIntime() {
        return intime;
    }

    public String getUserimg() {
        return userimg;
    }

    public String getUsername() {
        return username;
    }

    public String getPubtime() {
        return pubtime;
    }

    public String getText() {
        return text;
    }

    public int getMore_hf() {
        return more_hf;
    }

    public void setMore_hf(int more_hf) {
        this.more_hf = more_hf;
    }

    @Override
    public String toString() {
        return "NoteFirstReply{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", userid2=" + userid2 +
                ", wid='" + wid + '\'' +
                ", intime='" + intime + '\'' +
                ", userimg='" + userimg + '\'' +
                ", username='" + username + '\'' +
                ", pubtime='" + pubtime + '\'' +
                ", text='" + text + '\'' +
                ", status=" + status +
                ", more_hf=" + more_hf +
                ", son=" + son +
                '}';
    }
}


