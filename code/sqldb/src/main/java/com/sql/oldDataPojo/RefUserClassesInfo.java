package com.sql.oldDataPojo;

import java.io.Serializable;

/**
 * Created by guojing on 2015/3/23.
 */
public class RefUserClassesInfo implements Serializable {
    private static final long serialVersionUID = -2791829442094243367L;
    private int userId;
    private int classId;

    public int getTeamtype() {
        return teamtype;
    }

    public void setTeamtype(int teamtype) {
        this.teamtype = teamtype;
    }

    public int getSchoolYeartype() {
        return schoolYeartype;
    }

    public void setSchoolYeartype(int schoolYeartype) {
        this.schoolYeartype = schoolYeartype;
    }

    private int teamtype;
    private int schoolYeartype;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
