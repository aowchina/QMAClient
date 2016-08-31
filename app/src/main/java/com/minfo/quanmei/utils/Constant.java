package com.minfo.quanmei.utils;

import com.minfo.quanmei.config.ImageSelConfig;
import com.minfo.quanmei.entity.Group;
import com.minfo.quanmei.entity.GroupTag;
import com.minfo.quanmei.entity.Province;
import com.minfo.quanmei.entity.Special;
import com.minfo.quanmei.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liujing on 15/8/20.
 */
public class Constant {

    //版本信息的HashMap
    public static HashMap<String, String> updateMap = null;

    //版本是否需要更新的标识
    public static boolean isUpdate = false;

    public static boolean isLogin = false;

    public static ImageSelConfig imageSelConfig;



    /**
     * 当前登录用户
     */
    public static User user;

    /**
     * 特惠类别集合
     */
    public static List<Special> specialList;

    /**
     * 省数据结合
     */
    public static List<Province> provinces = new ArrayList<>();

    /**
     * 临时小组信息，发帖写日记
     */
    public static Group groupDetail;

    /**
     * 已选择小组标签临时保存
     */
    public static List<GroupTag> selectGroupTags;


    public static int currentGroupIndex = 0;



}
