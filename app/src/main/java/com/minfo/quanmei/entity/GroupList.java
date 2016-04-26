package com.minfo.quanmei.entity;

import java.util.List;

/**
 * Created by liujing on 15/9/29.
 */
public class GroupList {

    private List<Group> group;
    private List<String> ht;

    public List<Group> getGroup() {
        return group;
    }

    public void setGroup(List<Group> group) {
        this.group = group;
    }

    public List<String> getHt() {
        return ht;
    }

    public void setHt(List<String> ht) {
        this.ht = ht;
    }

    @Override
    public String toString() {
        return "GroupList{" +
                "group=" + group +
                ", ht=" + ht +
                '}';
    }
}
