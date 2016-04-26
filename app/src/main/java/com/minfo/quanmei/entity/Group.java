package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liujing on 15/9/29.
 */
public class Group implements Serializable{


    /**
     * amount : 0
     * id : 6
     * icon : http://192.168.1.101/a_servers/company/qm/images/yanjing.png
     * name : 眼部
     * intime : 1443522838
     * intro : 双眼皮，啦啦
     */

    private String id;
    private String icon;
    private String name;
    private String intime;
    private String amount;
    private String group;
    private String intro;
    private int isin;

    private List<User> admin;
    private List<GroupTag> tag;
    private List<String> ht;

    public List<String> getHt() {
        return ht;
    }

    public void setHt(List<String> ht) {
        this.ht = ht;
    }

    public List<User> getAdmin() {
        return admin;
    }

    public void setAdmin(List<User> admin) {
        this.admin = admin;
    }

    public List<GroupTag> getTag() {
        return tag;
    }

    public void setTag(List<GroupTag> tag) {
        this.tag = tag;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getIntime() {
        return intime;
    }

    public String getIntro() {
        return intro;
    }
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getIsin() {
        return isin;
    }

    public void setIsin(int isin) {
        this.isin = isin;
    }


    @Override
    public String toString() {
        return "Group{" +
                "admin=" + admin +
                ", id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", intime='" + intime + '\'' +
                ", amount='" + amount + '\'' +
                ", group='" + group + '\'' +
                ", intro='" + intro + '\'' +
                ", isin=" + isin +
                ", tag=" + tag +
                ", ht=" + ht +
                '}';
    }
}
