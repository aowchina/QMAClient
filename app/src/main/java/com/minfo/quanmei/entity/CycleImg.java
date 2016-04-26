package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 16/2/1.
 */
public class CycleImg implements Serializable {
    private int type;
    private String hid;
    private String pid;
    private String id;
    private String img;

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CycleImg{" +
                "hid='" + hid + '\'' +
                ", type=" + type +
                ", pid='" + pid + '\'' +
                ", id='" + id + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
