package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/7.
 */
public class MesgInfo {
    private int id;
    private int userid;
    private String mesgname;

    private String mesgcontent;


    private Date createtime;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getMesgname() {
        return mesgname;
    }

    public void setMesgname(String mesgname) {
        this.mesgname = mesgname;
    }

    public String getMesgcontent() {
        return mesgcontent;
    }

    public void setMesgcontent(String mesgcontent) {
        this.mesgcontent = mesgcontent;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
