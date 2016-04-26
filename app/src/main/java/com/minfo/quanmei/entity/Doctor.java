package com.minfo.quanmei.entity;

/**
 * Created by liujing on 15/8/28.
 */
public class Doctor {
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像url
     */
    private String imgUrl;
    /**
     * 职位
     */
    private String position;
    /**
     * 临床方向
     */
    private String direction;
    /**
     * 权重，影响医生列表排序
     */
    private String weight;
    /**
     * 记录创建时间
     */
    private int createtime;
    /**
     * 记录写入时间
     */
    private int intime;

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
