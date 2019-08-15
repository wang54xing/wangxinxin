package com.sql.oldDataPojo;

import java.util.Date;

public class ExperienceLogInfo {

    private int userid;

    private String experiencename;

    private int experience;

    private Date createtime;

    private String experiencetime;

    private int experiencetype;

    private String relateId;
  
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getExperiencename() {
        return experiencename;
    }

    public void setExperiencename(String experiencename) {
        this.experiencename = experiencename;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getExperiencetime() {
        return experiencetime;
    }

    public void setExperiencetime(String experiencetime) {
        this.experiencetime = experiencetime;
    }

    public int getExperiencetype() {
        return experiencetype;
    }

    public void setExperiencetype(int experiencetype) {
        this.experiencetype = experiencetype;
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }
}
