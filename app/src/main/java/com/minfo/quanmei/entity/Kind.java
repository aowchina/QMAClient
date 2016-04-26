package com.minfo.quanmei.entity;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 15/10/14.
 */
public class Kind implements Serializable {
    private String id;
    private String name;

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

    @Override
    public String toString() {
        return "Kind{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
