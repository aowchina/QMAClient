package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liujing on 15/10/12.
 */
public class SpecialPage implements Serializable{
    private List<Special> types;
    private Theme act_first;
    private List<Theme> act_list;

    public Theme getAct_first() {
        return act_first;
    }

    public void setAct_first(Theme act_first) {
        this.act_first = act_first;
    }

    public List<Theme> getAct_list() {
        return act_list;
    }

    public void setAct_list(List<Theme> act_list) {
        this.act_list = act_list;
    }

    public List<Special> getTypes() {
        return types;
    }

    public void setTypes(List<Special> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "SpecialPage{" +
                "act_first=" + act_first +
                ", types=" + types +
                ", act_list=" + act_list +
                '}';
    }
}
