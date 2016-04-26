package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 15/10/29.
 */
public class DoctorIntroduce implements Serializable {
    private int id;
    private List<DoctorData>doctor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DoctorData> getDoctor() {
        return doctor;
    }

    public void setDoctor(List<DoctorData> doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "DoctorIntroduce{" +
                "id=" + id +
                ", doctor=" + doctor +
                '}';
    }
}
