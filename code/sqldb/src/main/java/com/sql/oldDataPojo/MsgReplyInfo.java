package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/7.
 */
public class MsgReplyInfo {

    private int id;

    private int mesgid;

    private String homeworkcontent;

    private int userid;

    private Date createtime;



    private int classid;
    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMesgid() {
        return mesgid;
    }

    public void setMesgid(int mesgid) {
        this.mesgid = mesgid;
    }

    public String getHomeworkcontent() {
        return homeworkcontent;
    }

    public void setHomeworkcontent(String homeworkcontent) {
        this.homeworkcontent = homeworkcontent;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
