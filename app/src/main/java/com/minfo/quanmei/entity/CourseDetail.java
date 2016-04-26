package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liujing on 16/1/24.
 */
public class CourseDetail implements Serializable {
    private String teacher_name;
    private String teacher_intro;
    private String teacher_img;
    private String course_name;
    private String course_intro;
    private String course_banner;
    private String course_price;
    private List<String> course_imgs;
    private String course_id;
    public CourseDetail(){}


    public String getCourse_banner() {
        return course_banner;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setCourse_banner(String course_banner) {
        this.course_banner = course_banner;
    }

    public List<String> getCourse_imgs() {
        return course_imgs;
    }

    public void setCourse_imgs(List<String> course_imgs) {
        this.course_imgs = course_imgs;
    }

    public String getCourse_intro() {
        return course_intro;
    }

    public void setCourse_intro(String course_intro) {
        this.course_intro = course_intro;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_price() {
        return course_price;
    }

    public void setCourse_price(String course_price) {
        this.course_price = course_price;
    }

    public String getTeacher_img() {
        return teacher_img;
    }

    public void setTeacher_img(String teacher_img) {
        this.teacher_img = teacher_img;
    }

    public String getTeacher_intro() {
        return teacher_intro;
    }

    public void setTeacher_intro(String teacher_intro) {
        this.teacher_intro = teacher_intro;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    @Override
    public String toString() {
        return "CourseDetail{" +
                "course_banner='" + course_banner + '\'' +
                ", teacher_name='" + teacher_name + '\'' +
                ", teacher_intro='" + teacher_intro + '\'' +
                ", teacher_img='" + teacher_img + '\'' +
                ", course_name='" + course_name + '\'' +
                ", course_intro='" + course_intro + '\'' +
                ", course_price='" + course_price + '\'' +
                ", course_imgs=" + course_imgs +
                ", course_id='" + course_id + '\'' +
                '}';
    }
}
