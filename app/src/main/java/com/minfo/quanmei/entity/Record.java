package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by liujing on 16/2/20.
 */
public class Record implements Serializable {

    /**
     * id : 26
     * type : 2
     * status : 1
     * order_num : quanmei20160220133347
     * order_fee : 1800.00
     * point : 18.00
     * ctime : 1455946440
     * intime : 1455946440
     * userid : 594111915
     * week : 星期六
     * year : 2016
     * month : 02
     * day : 20
     */

    private String id;
    private String type;
    private String status;
    private String order_num;
    private String order_fee;
    private String point;
    private String ctime;
    private String intime;
    private String userid;
    private String week;
    private String year;
    private String month;
    private String day;

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public void setOrder_fee(String order_fee) {
        this.order_fee = order_fee;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getOrder_num() {
        return order_num;
    }

    public String getOrder_fee() {
        return order_fee;
    }

    public String getPoint() {
        return point;
    }

    public String getCtime() {
        return ctime;
    }

    public String getIntime() {
        return intime;
    }

    public String getUserid() {
        return userid;
    }

    public String getWeek() {
        return week;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "Record{" +
                "ctime='" + ctime + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", order_num='" + order_num + '\'' +
                ", order_fee='" + order_fee + '\'' +
                ", point='" + point + '\'' +
                ", intime='" + intime + '\'' +
                ", userid='" + userid + '\'' +
                ", week='" + week + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
