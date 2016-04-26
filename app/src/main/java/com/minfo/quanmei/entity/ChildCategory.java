package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 15/8/25.
 * 产品二级分类
 */
public class ChildCategory implements Serializable{

    private int id;
    private String name;
    private int createtime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }
}
