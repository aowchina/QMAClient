package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 15/10/27.
 */
public class HospitalData implements Serializable {
    /**
     * {
     "id": 5,
     "name": "",
     "logo": "http://quanmei.min-fo.com/",
     "stars": 3,
     "sm": "9.8",
     "hj": "10.0",
     "fw": "9.7",
     "hp": "98%",
     "plamount": 0,
     "pl": [],
     "doctor": [
     {
     "id": "11",
     "name": "敖英芳",
     "direction": "为运动损伤性伤病的治疗",
     "img": "http://quanmei.min-fo.com/images/lian.png"
     },
     {
     "id": "12",
     "name": "曹晓光",
     "direction": "白内障手术、视光学、屈光手术、近视眼防治、眼前节疾病",
     "img": "http://quanmei.min-fo.com/images/lian.png"
     }
     ]
     }
     */

    private int id;
    private String name;
    private String logo;
    private int stars;
    private String sm;
    private String hj;
    private String fw;
    private String hp;
    private int plamount;
    private List<HospitalPJ> pl;
    private List<DoctorData>doctor;

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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public String getHj() {
        return hj;
    }

    public void setHj(String hj) {
        this.hj = hj;
    }

    public String getFw() {
        return fw;
    }

    public void setFw(String fw) {
        this.fw = fw;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public int getPlamount() {
        return plamount;
    }

    public void setPlamount(int plamount) {
        this.plamount = plamount;
    }

    public List<HospitalPJ> getPl() {
        return pl;
    }

    public void setPl(List<HospitalPJ> pl) {
        this.pl = pl;
    }

    public List<DoctorData> getDoctor() {
        return doctor;
    }

    public void setDoctor(List<DoctorData> doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "HospitalData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", stars=" + stars +
                ", sm='" + sm + '\'' +
                ", hj='" + hj + '\'' +
                ", fw='" + fw + '\'' +
                ", hp='" + hp + '\'' +
                ", plamount=" + plamount +
                ", pl=" + pl +
                ", doctor=" + doctor +
                '}';
    }
}
