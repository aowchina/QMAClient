package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 16/1/24.
 */
public class Course  implements Serializable{

    /**
     * name : 全美课程二
     * teacherid : 2
     * courseid : 2
     * amount : 1
     * orderid : quanmei_course20160124151108
     */

    private String name;
    private String teacherid;
    private String courseid;
    private String amount;
    private String orderid;

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getName() {
        return name;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public String getCourseid() {
        return courseid;
    }

    public String getAmount() {
        return amount;
    }

    public String getOrderid() {
        return orderid;
    }

    @Override
    public String toString() {
        return "Course{" +
                "amount='" + amount + '\'' +
                ", name='" + name + '\'' +
                ", teacherid='" + teacherid + '\'' +
                ", courseid='" + courseid + '\'' +
                ", orderid='" + orderid + '\'' +
                '}';
    }
}
