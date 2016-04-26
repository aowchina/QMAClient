package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by min-fo-012 on 15/10/27.
 */
public class HospitalIntroduce implements Serializable {
    /**
     * {
     * "id": 5,
     * "name": "",
     * "logo": "http://quanmei.min-fo.com/",
     * "intro": "",
     * "xcimg": [
     * "http://quanmei.min-fo.com/images/hospital/yanling01.jpg",
     * "http://quanmei.min-fo.com/images/milan01.jpg",
     * "http://quanmei.min-fo.com/images/milan02.jpg"
     * ],
     * "alimg": [
     * "http://quanmei.min-fo.com/images/hospital/al02.jpg",
     * "http://quanmei.min-fo.com/images/hospital/al01.jpg",
     * "http://quanmei.min-fo.com/images/hospital/al03.jpg"
     * ]
     * }
     */
    private int id;
    private String name;
    private String logo;

    private String intro;
    private ArrayList<String> xcimg;
    private ArrayList<String> alimg;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public ArrayList<String> getXcimg() {
        return xcimg;
    }

    public void setXcimg(ArrayList<String> xcimg) {
        this.xcimg = xcimg;
    }

    public ArrayList<String> getAlimg() {
        return alimg;
    }

    public void setAlimg(ArrayList<String> alimg) {
        this.alimg = alimg;
    }

    @Override
    public String toString() {
        return "HospitalIntroduce{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", intro='" + intro + '\'' +
                ", xcimg=" + xcimg +
                ", alimg=" + alimg +
                '}';
    }
}
