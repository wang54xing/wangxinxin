package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/9.
 */
public class ActivityDiscussInfo {

    private int id;
    private int actId;
    private int userId;
    private String content;
    private String image;
    private Date t;
    private int repId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActId() {
        return actId;
    }

    public void setActId(int actId) {
        this.actId = actId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

    public int getRepId() {
        return repId;
    }

    public void setRepId(int repId) {
        this.repId = repId;
    }
}
