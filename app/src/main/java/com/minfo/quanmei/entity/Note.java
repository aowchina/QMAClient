package com.minfo.quanmei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liujing on 15/9/25.
 * 帖子 Model
 */
public class Note implements Serializable{
    public int noteId;
    public String noteTitle;
    public boolean isHaveImg;
    public List<String> imgUrls;//图片url列表
    public String  groupName;
    public String noteContent;
    public String avatarUrl;//头像url
    public String nickName;//昵称
    public String time;//时间
    public int likeNum;//多少人觉得有用
    public int replyNum;//回复数
    public List<NoteFirstReply> firstReplies;

    public Note(){}

    public Note(String avatarUrl, List<NoteFirstReply> firstReplies, String groupName, List<String> imgUrls, boolean isHaveImg, int likeNum, String nickName, String noteContent, int noteId, String noteTitle, int replyNum, String time) {
        this.avatarUrl = avatarUrl;
        this.firstReplies = firstReplies;
        this.groupName = groupName;
        this.imgUrls = imgUrls;
        this.isHaveImg = isHaveImg;
        this.likeNum = likeNum;
        this.nickName = nickName;
        this.noteContent = noteContent;
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.replyNum = replyNum;
        this.time = time;
    }
}
