package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 16/1/14.
 */
public class Teacher implements Serializable {
    private String id;
    private String name;
    private String logo;

    public Teacher(String id, String logo, String name) {
        this.id = id;
        this.logo = logo;
        this.name = name;
    }

    public Teacher() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
