package com.sql.oldDataPojo;

import java.io.Serializable;

/**
 * Created by qinbo on 15/3/19.
 */
public class SubjectInfo implements Serializable{


    private static final long serialVersionUID = -2972920692957379654L;

    private String name;
    private int gradeId;
    private int schoolId;
    private int subjectId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
