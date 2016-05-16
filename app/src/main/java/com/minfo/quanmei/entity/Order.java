package com.minfo.quanmei.entity;

/**
 * Created by liujing on 16/4/25.
 */
public class Order {

    /**
     * id : 620
     * orderid : quanmei20160514140257
     * userid : 594111915
     * tid : 34
     * status : 1
     * dsf_orderid : null
     * dsf_type : 0
     * dj_money : 0.00
     * wk_money : 0.00
     * wk_type : 0
     * server_id : 0
     * get_point : 0.00
     * hf_point : 0.00
     * point_money : 0.00
     * detail_img : null
     * create_time : 1463205777
     * intime : 1463205777
     * simg : http://quanmei.min-fo.com/images/tehui/xiaotu10.png
     * dj : 0.01
     * newval : 0.02
     * name : 丰胸特惠7
     * fname : 自体脂肪隆胸
     * hname : 北京医院一
     * hid : 4
     * tel : 15301319969
     */

    private String id;
    private String orderid;
    private String userid;
    private String tid;
    private String status;
    private Object dsf_orderid;
    private String dsf_type;
    private String dj_money;
    private String wk_money;
    private String wk_type;
    private String server_id;
    private String get_point;
    private String hf_point;
    private String point_money;
    private Object detail_img;
    private String create_time;
    private String intime;
    private String simg;
    private String dj;
    private String newval;
    private String name;
    private String fname;
    private String hname;
    private int hid;
    private String tel;

    private double true_pay;
    private double have_pay;
    private String ctime;

    private boolean isCheck;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDsf_orderid(Object dsf_orderid) {
        this.dsf_orderid = dsf_orderid;
    }

    public void setDsf_type(String dsf_type) {
        this.dsf_type = dsf_type;
    }

    public void setDj_money(String dj_money) {
        this.dj_money = dj_money;
    }

    public void setWk_money(String wk_money) {
        this.wk_money = wk_money;
    }

    public void setWk_type(String wk_type) {
        this.wk_type = wk_type;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public void setGet_point(String get_point) {
        this.get_point = get_point;
    }

    public void setHf_point(String hf_point) {
        this.hf_point = hf_point;
    }

    public void setPoint_money(String point_money) {
        this.point_money = point_money;
    }

    public void setDetail_img(Object detail_img) {
        this.detail_img = detail_img;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public void setDj(String dj) {
        this.dj = dj;
    }

    public void setNewval(String newval) {
        this.newval = newval;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getUserid() {
        return userid;
    }

    public String getTid() {
        return tid;
    }

    public String getStatus() {
        return status;
    }

    public Object getDsf_orderid() {
        return dsf_orderid;
    }

    public String getDsf_type() {
        return dsf_type;
    }

    public String getDj_money() {
        return dj_money;
    }

    public String getWk_money() {
        return wk_money;
    }

    public String getWk_type() {
        return wk_type;
    }

    public String getServer_id() {
        return server_id;
    }

    public String getGet_point() {
        return get_point;
    }

    public String getHf_point() {
        return hf_point;
    }

    public String getPoint_money() {
        return point_money;
    }

    public Object getDetail_img() {
        return detail_img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getIntime() {
        return intime;
    }

    public String getSimg() {
        return simg;
    }

    public String getDj() {
        return dj;
    }

    public String getNewval() {
        return newval;
    }

    public String getName() {
        return name;
    }

    public String getFname() {
        return fname;
    }

    public String getHname() {
        return hname;
    }

    public int getHid() {
        return hid;
    }

    public String getTel() {
        return tel;
    }

    public double getHave_pay() {
        return have_pay;
    }

    public void setHave_pay(double have_pay) {
        this.have_pay = have_pay;
    }

    public double getTrue_pay() {
        return true_pay;
    }

    public void setTrue_pay(double true_pay) {
        this.true_pay = true_pay;
    }

    @Override
    public String toString() {
        return "Order{" +
                "create_time='" + create_time + '\'' +
                ", id='" + id + '\'' +
                ", orderid='" + orderid + '\'' +
                ", userid='" + userid + '\'' +
                ", tid='" + tid + '\'' +
                ", status='" + status + '\'' +
                ", dsf_orderid=" + dsf_orderid +
                ", dsf_type='" + dsf_type + '\'' +
                ", dj_money='" + dj_money + '\'' +
                ", wk_money='" + wk_money + '\'' +
                ", wk_type='" + wk_type + '\'' +
                ", server_id='" + server_id + '\'' +
                ", get_point='" + get_point + '\'' +
                ", hf_point='" + hf_point + '\'' +
                ", point_money='" + point_money + '\'' +
                ", detail_img=" + detail_img +
                ", intime='" + intime + '\'' +
                ", simg='" + simg + '\'' +
                ", dj='" + dj + '\'' +
                ", newval='" + newval + '\'' +
                ", name='" + name + '\'' +
                ", fname='" + fname + '\'' +
                ", hname='" + hname + '\'' +
                ", hid=" + hid +
                ", tel='" + tel + '\'' +
                '}';
    }
}
