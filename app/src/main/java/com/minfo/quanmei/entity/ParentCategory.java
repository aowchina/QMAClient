package com.minfo.quanmei.entity;

import java.util.List;

/**
 * Created by liujing on 15/8/25.
 * 产品一级分类
 */
public class ParentCategory {
    private String icon;
    private String name;
    private List<ChildCategory> list;
    private boolean isExpanded = false;//是否已全部显示

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildCategory> getList() {
        return list;
    }

    public void setList(List<ChildCategory> list) {
        this.list = list;
    }
}
