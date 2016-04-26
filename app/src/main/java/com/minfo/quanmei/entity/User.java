package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liujing on 15/8/24.
 * 用户信息
 */
public class User implements Serializable {

    /**
     * sex : 暂未设置
     * username : 清粥小菜
     * level : 1
     * userimg :
     * age : 17
     * star : 0
     * userid : 785251622
     * city : 暂未设置
     */
    private String sex;
    private String username;
    private int level;
    private String userimg;
    private String age;
    private int star;
    private int userid;
    private String city;
    private List<Group> group;
    private String bgimg;


    public List<Group> getGroup() {
        return group;
    }

    public void setGroup(List<Group> group) {
        this.group = group;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public String getUsername() {
        return username;
    }

    public int getLevel() {
        return level;
    }

    public String getUserimg() {
        return userimg;
    }

    public String getAge() {
        return age;
    }

    public int getStar() {
        return star;
    }

    public int getUserid() {
        return userid;
    }

    public String getCity() {
        return city;
    }

    public String getBgimg() {
        return bgimg;
    }

    public void setBgimg(String bgimg) {
        this.bgimg = bgimg;
    }

    @Override
    public String toString() {
        return "User{" +
                "age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", username='" + username + '\'' +
                ", level=" + level +
                ", userimg='" + userimg + '\'' +
                ", star=" + star +
                ", userid=" + userid +
                ", city='" + city + '\'' +
                ", group=" + group +
                ", bgimg='" + bgimg + '\'' +
                '}';
    }
}
