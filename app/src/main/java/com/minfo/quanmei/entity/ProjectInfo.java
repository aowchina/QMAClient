package com.minfo.quanmei.entity;

import java.util.List;

/**
 * Created by liujing on 15/8/27.
 */
public class ProjectInfo {
    /**
     * {
     "baseinfo": {
     "intro": "",
     "jgfw": "",
     "fxzs": "",
     "cxsj": "",
     "cxsj2": "",
     "yd": "",
     "qd": "",
     "syrq": "",
     "zlsc": "",
     "zlcs": "",
     "mzff": "",
     "sfzy": "",
     "hfsj": "",
     "zysx": "",
     "fxts": "",
     "jjrq": "",
     "id": ""
     },
     "goods": [{},{}]
     }
     */

    private Baseinfo baseinfo;
    private List<Product> goods;

    public Baseinfo getBaseinfo() {
        return baseinfo;
    }

    public void setBaseinfo(Baseinfo baseinfo) {
        this.baseinfo = baseinfo;
    }

    public List<Product> getGoods() {
        return goods;
    }

    public void setGoods(List<Product> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
                "baseinfo=" + baseinfo +
                ", goods=" + goods +
                '}';
    }
}
