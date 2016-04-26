package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 15/9/30.
 * 帖子二级回复Model
 */
public class SecondReply extends NoteReply implements Serializable{

    /**
     * id : 20
     * userid : 14999534
     * userid2 : 14999534
     * wid : 93
     * username : 客棹孤舟
     * username2 : 客棹孤舟
     * pubtime : 16分钟前
     * text : 第一条二级回复10271749。
     */

    private String id;
    private String userid;
    private String userid2;
    private String wid;
    private String username;
    private String username2;
    private String pubtime;
    private String text;

    private String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUserid2(String userid2) {
        this.userid2 = userid2;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
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

    public String getUserid2() {
        return userid2;
    }

    public String getWid() {
        return wid;
    }

    public String getUsername() {
        return username;
    }

    public String getUsername2() {
        return username2;
    }

    public String getPubtime() {
        return pubtime;
    }

    public String getText() {
        return text;
    }


    @Override
    public String toString() {
        return "SecondReply{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", userid2='" + userid2 + '\'' +
                ", wid='" + wid + '\'' +
                ", username='" + username + '\'' +
                ", username2='" + username2 + '\'' +
                ", pubtime='" + pubtime + '\'' +
                ", text='" + text + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
