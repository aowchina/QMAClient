package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 15/12/4.
 */
public class SearchData implements Serializable {
    private List<Product>th;
    private String th_amount;

    private List<GroupArticle>rj;
    private String rj_amount;

    private List<GroupArticle>tz;
    private String tz_amount;

    public List<Product> getTh() {
        return th;
    }

    public void setTh(List<Product> th) {
        this.th = th;
    }

    public String getTh_amount() {
        return th_amount;
    }

    public void setTh_amount(String th_amount) {
        this.th_amount = th_amount;
    }

    public List<GroupArticle> getRj() {
        return rj;
    }

    public void setRj(List<GroupArticle> rj) {
        this.rj = rj;
    }

    public String getRj_amount() {
        return rj_amount;
    }

    public void setRj_amount(String rj_amount) {
        this.rj_amount = rj_amount;
    }

    public List<GroupArticle> getTz() {
        return tz;
    }

    public void setTz(List<GroupArticle> tz) {
        this.tz = tz;
    }

    public String getTz_amount() {
        return tz_amount;
    }

    public void setTz_amount(String tz_amount) {
        this.tz_amount = tz_amount;
    }

    @Override
    public String toString() {
        return "SearchData{" +
                "th=" + th +
                ", th_amount='" + th_amount + '\'' +
                ", rj=" + rj +
                ", rj_amount='" + rj_amount + '\'' +
                ", tz=" + tz +
                ", tz_amount='" + tz_amount + '\'' +
                '}';
    }
}
