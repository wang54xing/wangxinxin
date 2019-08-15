package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/9.
 */
public class FriendApplyInfo {
    private int id;
    private int userId;
    private int respondent;
    private Date applyDate;
    private Date respondDate;
    private int accepted;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRespondent() {
        return respondent;
    }

    public void setRespondent(int respondent) {
        this.respondent = respondent;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getRespondDate() {
        return respondDate;
    }

    public void setRespondDate(Date respondDate) {
        this.respondDate = respondDate;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
