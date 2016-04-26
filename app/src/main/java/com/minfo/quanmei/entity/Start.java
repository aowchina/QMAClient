package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 15/10/13.
 */
public class Start implements Serializable {

    private List<CycleImg> lunbo;
    private List<Theme> act;
    private List<Product> tehui;
    private List<ProjectProduct> project;
    private List<Product> goods;
    private List<GroupArticle> diary;

    public List<CycleImg> getLunbo() {
        return lunbo;
    }

    public void setLunbo(List<CycleImg> lunbo) {
        this.lunbo = lunbo;
    }

    public List<Theme> getAct() {
        return act;
    }

    public void setAct(List<Theme> act) {
        this.act = act;
    }

    public List<Product> getTehui() {
        return tehui;
    }

    public void setTehui(List<Product> tehui) {
        this.tehui = tehui;
    }

    public List<GroupArticle> getDiary() {
        return diary;
    }

    public void setDiary(List<GroupArticle> diary) {
        this.diary = diary;
    }

    public List<ProjectProduct> getProject() {
        return project;
    }

    public void setProject(List<ProjectProduct> project) {
        this.project = project;
    }

    public List<Product> getGoods() {
        return goods;
    }

    public void setGoods(List<Product> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "Start{" +
                "act=" + act +
                ", lunbo=" + lunbo +
                ", tehui=" + tehui +
                ", project=" + project +
                ", goods=" + goods +
                ", diary=" + diary +
                '}';
    }
}
