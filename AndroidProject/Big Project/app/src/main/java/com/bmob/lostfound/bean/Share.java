package com.bmob.lostfound.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/9/13.
 */
public class Share extends BmobObject{

    private String content;
    private User author;
    private Integer answerNum;

    public Share() {
        answerNum = 0;
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

    public Integer getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(Integer answerNum) {
        this.answerNum = answerNum;
    }
}
