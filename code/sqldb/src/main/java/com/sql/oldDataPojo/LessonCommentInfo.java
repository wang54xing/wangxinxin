package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/5/5.
 */
public class LessonCommentInfo {

    private int userId;
    private int outId;
    private Date lastModified;
    private String content;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOutId() {
        return outId;
    }

    public void setOutId(int outId) {
        this.outId = outId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
