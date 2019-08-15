package com.sql.oldDataPojo;

import java.io.Serializable;

/**
 * Created by qinbo on 15/3/19.
 */
public class GradeInfo implements Serializable{

    private static final long serialVersionUID = 6352235122964948888L;
    private int id;
    private String gradeName;

    private int schoolId;
    private int typeId;
    private int userid;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getGradeName() {
        return gradeName;
    }
    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
    public int getSchoolId() {
        return schoolId;
    }
    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
