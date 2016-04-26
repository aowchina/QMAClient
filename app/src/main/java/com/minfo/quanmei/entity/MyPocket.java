package com.minfo.quanmei.entity;

import java.util.List;

/**
 * Created by liujing on 16/2/20.
 */
public class MyPocket {
    private String point;
    private List<Record> list;

    public List<Record> getList() {
        return list;
    }

    public void setList(List<Record> list) {
        this.list = list;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "MyPocket{" +
                "list=" + list +
                ", point='" + point + '\'' +
                '}';
    }
}
