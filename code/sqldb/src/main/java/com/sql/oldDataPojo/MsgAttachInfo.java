package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/8.
 */
public class MsgAttachInfo {

    private int id;
    private String name;
    private Date uploaddate;
    private String pathname;
    private int userid;
    private int messageid;
    private int mesgreplyid;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(Date uploaddate) {
        this.uploaddate = uploaddate;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getMessageid() {
        return messageid;
    }

    public void setMessageid(int messageid) {
        this.messageid = messageid;
    }

    public int getMesgreplyid() {
        return mesgreplyid;
    }

    public void setMesgreplyid(int mesgreplyid) {
        this.mesgreplyid = mesgreplyid;
    }
}
