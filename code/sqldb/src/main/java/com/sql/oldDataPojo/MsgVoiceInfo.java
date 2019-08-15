package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/8.
 */
public class MsgVoiceInfo {

    private int id;
    private int courseid;
    private String voicename;
    private int userid;
    private Date createtime;
    private int mesgid;
    private int mesgreplyid;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseid() {
        return courseid;
    }

    public void setCouorseid(int couorseid) {
        this.courseid = couorseid;
    }

    public String getVoicename() {
        return voicename;
    }

    public void setVoicename(String voicename) {
        this.voicename = voicename;
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

    public int getMesgid() {
        return mesgid;
    }

    public void setMesgid(int mesgid) {
        this.mesgid = mesgid;
    }

    public int getMesgreplyid() {
        return mesgreplyid;
    }

    public void setMesgreplyid(int mesgreplyid) {
        this.mesgreplyid = mesgreplyid;
    }
}
