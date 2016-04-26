package com.minfo.quanmei.utils;

/*
 * 验证类
 * @author wangrui@min-fo.com
 * @date: 2014-05-26
 */
public class MyCheck {

    //private static String number_exp = "^[0-9]+$";
    private static String money_exp = "^[0-9]+$";
    private static String eventid_exp = "^(1)[0-9]{3}$";
    private static String eventname_exp = "^[(\u4e00-\u9fa5)|(a-zA-Z0-9)]{2,10}$";
    private static String teamname_exp = "^[(\u4e00-\u9fa5)|(a-zA-Z0-9)]{2,12}$";
    private static String nicheng_exp = "^[(\u4e00-\u9fa5)|(a-zA-Z)]{2,10}$";
    private static String mail_exp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private static String psw_exp = "^[0-9a-zA-Z]{6,15}$";
    //	private static String tel_exp = "[1][358]\\d{9}";
    private static String tel_exp = "^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";

    private static String date = "^((((19|20)\\d{2})-(0?(1|[3-9])|1[012])-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)" +
            "([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$";

    private static String noteTitle_exp = "^[(\u4e00-\u9fa5)|(a-zA-Z0-9_)|(\\+,]{2,}$";
    private static String hosName_exp = "^[(\u4e00-\u9fa5)|(a-zA-Z0-9)]{2,}$";

    public static boolean isNoteTitle(String str){
        if(str.matches(noteTitle_exp)){
            return true;
        }
        return false;
    }
    public static boolean isHosName(String str){
        if(str.matches(hosName_exp)){
            return true;
        }
        return false;
    }

    //验证是否符合手机号要求
    public static boolean isTel(String str) {
        if (str.matches(tel_exp)) {
            return true;
        }
        return false;
    }

    //验证是否符合昵称要求
    public static boolean isNiCheng(String str) {
        if (str.matches(nicheng_exp)) {
            return true;
        }
        return false;
    }

    //验证是否符合球队名称求要
    public static boolean isTeamName(String str) {
        if (str.matches(teamname_exp)) {
            return true;
        }
        return false;
    }

    //验证是否符合充值要求
    public static boolean isMoney(String str) {
        if (str.matches(money_exp) || !str.substring(0, 1).equals("0")) {
            return true;
        }
        return false;
    }

    //验证是否符合赛事名称要求
    public static boolean isEventName(String str) {
        if (str.matches(eventname_exp)) {
            return true;
        }
        return false;
    }

    //验证是否符合赛事编号要求
    public static boolean isEventId(String str) {
        if (str.matches(eventid_exp)) {
            return true;
        }
        return false;
    }

    //验证是否符合邮箱要求
    public static boolean isEmail(String str) {
        if (str.matches(mail_exp)) {
            return true;
        }
        return false;
    }

    //验证密码是否要求
    public static boolean isPsw(String str) {
        if (str.matches(psw_exp)) {
            return true;
        }
        return false;
    }

    /**
     *验证日期
     * @param str
     * @return
     */
    public static boolean isDate(String str){
        if(str.matches(date)){
            return true;
        }
        return false;
    }
}
