package com.minfo.quanmei.entity;

/**
 * Created by liujing on 15/8/28.
 */
public class Hospital {

    private String name;
    private String imgUrl;
    private String imgList;
    private String caseList;
    private int createtime;
    private int intime;

    public Hospital(){};

    public Hospital(String caseList, int createtime, String imgList, String imgUrl, int intime, String name) {
        this.caseList = caseList;
        this.createtime = createtime;
        this.imgList = imgList;
        this.imgUrl = imgUrl;
        this.intime = intime;
        this.name = name;
    }

    public String getCaseList() {
        return caseList;
    }

    public void setCaseList(String caseList) {
        this.caseList = caseList;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public String getImgList() {
        return imgList;
    }

    public void setImgList(String imgList) {
        this.imgList = imgList;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIntime() {
        return intime;
    }

    public void setIntime(int intime) {
        this.intime = intime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
