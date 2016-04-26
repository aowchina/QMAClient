package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 15/10/6.
 */
public class GroupTag implements Serializable{
    private String id;
    private String gid;
    private String name;
    private String intime;

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GroupTag{" +
                "id='" + id + '\'' +
                ", gid='" + gid + '\'' +
                ", name='" + name + '\'' +
                ", intime='" + intime + '\'' +
                '}';
    }
}
