package com.minfo.quanmei.entity;

import java.util.List;

/**
 * Created by liujing on 15/9/25.
 * 日记 Model
 */
public class Diary {

    public int diaryId;
    public boolean isEssence;//是否为精华日记
    public String title;
    public String groupName;
    public String avatarUrl;//头像url
    public String nickName;//昵称
    public String time;//最新更新日期
    public int likeNum;//赞数
    public int replyNum;//回复数
    public String hosName;//医院名称
    public String price;//花费
    public List<DiaryUpdateItem> updateList;
}
