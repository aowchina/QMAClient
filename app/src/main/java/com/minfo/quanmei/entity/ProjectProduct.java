package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 15/10/14.
 */
public class ProjectProduct implements Serializable{
    public  String id;


    private String name;
    private String icon;
    private String intime;
    private List<Kind> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Kind> getList() {
        return list;
    }

    public void setList(List<Kind> list) {
        this.list = list;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProjectProduct{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", intime='" + intime + '\'' +
                ", list=" + list +
                '}';
    }
}
