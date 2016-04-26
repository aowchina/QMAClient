package com.minfo.quanmei.entity;

/**
 * Created by min-fo-012 on 15/10/12.
 */
public class MyGrade {
    private String grade;
    private String numerical;

    public MyGrade(String grade, String numerical) {
        this.grade = grade;
        this.numerical = numerical;
    }
    public MyGrade() {

    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNumerical() {
        return numerical;
    }

    public void setNumerical(String numerical) {
        this.numerical = numerical;
    }
}
