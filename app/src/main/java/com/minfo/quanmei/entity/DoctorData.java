package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 15/10/27.
 */
public class DoctorData implements Serializable {
    /**
     * "id": "11",
     "name": "敖英芳",
     "direction": "为运动损伤性伤病的治疗",
     "img": "http://quanmei.min-fo.com/images/lian.png"
     */
    private String id;
    private String name;
    private String direction;
    private String img;
    private String pos;
    private String intro;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "DoctorData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", direction='" + direction + '\'' +
                ", img='" + img + '\'' +
                ", pos='" + pos + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }
}
