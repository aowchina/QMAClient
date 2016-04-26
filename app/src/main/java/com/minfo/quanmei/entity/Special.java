package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 15/10/12.
 */
public class Special implements Serializable{
    private int id;
    private String name;
    private String intime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Special{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", intime='" + intime + '\'' +
                '}';
    }
}
