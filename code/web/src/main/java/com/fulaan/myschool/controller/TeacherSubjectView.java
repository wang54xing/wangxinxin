package com.fulaan.myschool.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2015/3/18.
 */
public class TeacherSubjectView {
    private String tclId;//teacherClassLessonId
    private String teacherId;//teacherId==userId
    private String userName;
    private String subjectId;
    private String subjectName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTclId() {
        return tclId;
    }

    public void setTclId(String tclId) {
        this.tclId = tclId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
