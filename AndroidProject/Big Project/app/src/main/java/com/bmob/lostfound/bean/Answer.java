package com.bmob.lostfound.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/9/12.
 */
public class Answer extends BmobObject {
    private String content;
    private User author;
    private Question question;
    private BmobRelation agreeUser;
    private Integer agreeNum;

    public Answer() {
        agreeNum = 0;
        content = "";
    }
    public Integer getAgreeNum() {
        return agreeNum;
    }

    public void setAgreeNum(Integer agreeNum) {
        this.agreeNum = agreeNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public BmobRelation getAgreeUser() {
        return agreeUser;
    }

    public void setAgreeUser(BmobRelation agreeUser) {
        this.agreeUser = agreeUser;
    }
}
