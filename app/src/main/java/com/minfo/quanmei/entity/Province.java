package com.minfo.quanmei.entity;

import java.util.List;

/**
 * Created by liujing on 15/10/12.
 */
public class Province {
    private int id;
    private String name;
    private List<City> cities;

    public  Province(){

    }
    public Province(int id,String name){
        this.id = id;
        this.name= name;
    }

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

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
