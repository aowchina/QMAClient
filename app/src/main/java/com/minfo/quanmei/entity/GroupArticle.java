package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fei on 15/10/15.
 * 小组文章实体类
 */
public class GroupArticle implements Serializable{

    private String text;
    private String text2;
    private String pluserid;
    private List<User> pluser;
    private List<String> imgs;
    private String groupname;
    private String userid;
    private int isjing;
    private String userimg;
    private String type;
    private String ctime;
    private String id;
    private String title;
    private String zan;
    private String pubtime;
    private String gid;
    private String pl;
    private String tid;
    private String intime;
    private String plusername;
    private String pluserimg;


    private String username2;

    private String pltime;
    private String gname;

    private String username;

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    private int iszan;

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getPltime() {
        return pltime;
    }

    public void setPltime(String pltime) {
        this.pltime = pltime;
    }

    public String getPluserimg() {
        return pluserimg;
    }

    public void setPluserimg(String pluserimg) {
        this.pluserimg = pluserimg;
    }

    public int getIszan() {
        return iszan;
    }

    public void setIszan(int iszan) {
        this.iszan = iszan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getPluser() {
        return pluser;
    }

    public void setPluser(List<User> pluser) {
        this.pluser = pluser;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public int getIsjing() {
        return isjing;
    }

    public void setIsjing(int isjing) {
        this.isjing = isjing;
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

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    @Override
    public String toString() {
        return "GroupArticle{" +
                "ctime='" + ctime + '\'' +
                ", text='" + text + '\'' +
                ", text2='" + text2 + '\'' +
                ", pluserid='" + pluserid + '\'' +
                ", pluser=" + pluser +
                ", imgs=" + imgs +
                ", groupname='" + groupname + '\'' +
                ", userid='" + userid + '\'' +
                ", isjing=" + isjing +
                ", userimg='" + userimg + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", zan='" + zan + '\'' +
                ", pubtime='" + pubtime + '\'' +
                ", gid='" + gid + '\'' +
                ", pl='" + pl + '\'' +
                ", tid='" + tid + '\'' +
                ", intime='" + intime + '\'' +
                ", plusername='" + plusername + '\'' +
                ", pluserimg='" + pluserimg + '\'' +
                ", username2='" + username2 + '\'' +
                ", pltime='" + pltime + '\'' +
                ", gname='" + gname + '\'' +
                ", username='" + username + '\'' +
                ", iszan=" + iszan +
                '}';
    }
}
