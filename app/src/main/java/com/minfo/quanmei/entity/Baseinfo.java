package com.minfo.quanmei.entity;


/**
 * Created by liujing on 15/8/27.
 */
public class Baseinfo {

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
    /**
     * id : 6
     * name : 埋线双眼皮
     * jgfw : 3000元-5000元
     * fxzs : 3
     * cxsj : 3-5年，因人而异
     * zlsc : 30分钟
     * zlcs : 1次
     * mzff : 局麻
     * hfsj : 完全恢复1-3个月
     * cxsj2 : 5-7天
     * sfzy : 1
     * ptid : 10
     * jj : 简介
     * yd : 优点
     * qd : 缺点
     * syrq : 适宜人群
     * zysx : <p>注意事项</p>
     * fxts : <p>风险提示</p>
     * jjrq : <p>禁忌人群</p>
     * intime : 1443548774
     */

    private String id;
    private String name;
    private String jgfw;
    private String fxzs;
    private String cxsj;
    private String zlsc;
    private String zlcs;
    private String mzff;
    private String hfsj;
    private String cxsj2;
    private String sfzy;
    private String ptid;
    private String jj;
    private String yd;
    private String qd;
    private String syrq;
    private String zysx;
    private String fxts;
    private String jjrq;
    private String intime;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJgfw(String jgfw) {
        this.jgfw = jgfw;
    }

    public void setFxzs(String fxzs) {
        this.fxzs = fxzs;
    }

    public void setCxsj(String cxsj) {
        this.cxsj = cxsj;
    }

    public void setZlsc(String zlsc) {
        this.zlsc = zlsc;
    }

    public void setZlcs(String zlcs) {
        this.zlcs = zlcs;
    }

    public void setMzff(String mzff) {
        this.mzff = mzff;
    }

    public void setHfsj(String hfsj) {
        this.hfsj = hfsj;
    }

    public void setCxsj2(String cxsj2) {
        this.cxsj2 = cxsj2;
    }

    public void setSfzy(String sfzy) {
        this.sfzy = sfzy;
    }

    public void setPtid(String ptid) {
        this.ptid = ptid;
    }

    public void setJj(String jj) {
        this.jj = jj;
    }

    public void setYd(String yd) {
        this.yd = yd;
    }

    public void setQd(String qd) {
        this.qd = qd;
    }

    public void setSyrq(String syrq) {
        this.syrq = syrq;
    }

    public void setZysx(String zysx) {
        this.zysx = zysx;
    }

    public void setFxts(String fxts) {
        this.fxts = fxts;
    }

    public void setJjrq(String jjrq) {
        this.jjrq = jjrq;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJgfw() {
        return jgfw;
    }

    public String getFxzs() {
        return fxzs;
    }

    public String getCxsj() {
        return cxsj;
    }

    public String getZlsc() {
        return zlsc;
    }

    public String getZlcs() {
        return zlcs;
    }

    public String getMzff() {
        return mzff;
    }

    public String getHfsj() {
        return hfsj;
    }

    public String getCxsj2() {
        return cxsj2;
    }

    public String getSfzy() {
        return sfzy;
    }

    public String getPtid() {
        return ptid;
    }

    public String getJj() {
        return jj;
    }

    public String getYd() {
        return yd;
    }

    public String getQd() {
        return qd;
    }

    public String getSyrq() {
        return syrq;
    }

    public String getZysx() {
        return zysx;
    }

    public String getFxts() {
        return fxts;
    }

    public String getJjrq() {
        return jjrq;
    }

    public String getIntime() {
        return intime;
    }

    @Override
    public String toString() {
        return "Baseinfo{" +
                "cxsj2='" + cxsj2 + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", jgfw='" + jgfw + '\'' +
                ", fxzs='" + fxzs + '\'' +
                ", cxsj='" + cxsj + '\'' +
                ", zlsc='" + zlsc + '\'' +
                ", zlcs='" + zlcs + '\'' +
                ", mzff='" + mzff + '\'' +
                ", hfsj='" + hfsj + '\'' +
                ", sfzy='" + sfzy + '\'' +
                ", ptid='" + ptid + '\'' +
                ", jj='" + jj + '\'' +
                ", yd='" + yd + '\'' +
                ", qd='" + qd + '\'' +
                ", syrq='" + syrq + '\'' +
                ", zysx='" + zysx + '\'' +
                ", fxts='" + fxts + '\'' +
                ", jjrq='" + jjrq + '\'' +
                ", intime=" + intime +
                '}';
    }
}
