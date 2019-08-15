package com.sql.oldDataPojo;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guojing on 2015/3/23.
 */
public class ClassesInfo implements Serializable {
    private static final long serialVersionUID = -2791829442094243367L;
    private int id;
    private int schoolId;
    private int gradeId;
    private String className;
    private String classDetail;
    private int cty;
    private List<ObjectId> teacherId=null;
    private List<ObjectId> studentId=null;
    private int syn;

    private ObjectId masterId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDetail() {
        return classDetail;
    }

    public void setClassDetail(String classDetail) {
        this.classDetail = classDetail;
    }

    public int getCty() {
        return cty;
    }

    public void setCty(int cty) {
        this.cty = cty;
    }

    public List<ObjectId> getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(List<ObjectId> teacherId) {
        this.teacherId = teacherId;
    }

    public List<ObjectId> getStudentId() {
        return studentId;
    }

    public void setStudentId(List<ObjectId> studentId) {
        this.studentId = studentId;
    }

    public int getSyn() {
        return syn;
    }

    public void setSyn(int syn) {
        this.syn = syn;
    }

    public ObjectId getMasterId() {
        return masterId;
    }

    public void setMasterId(ObjectId masterId) {
        this.masterId = masterId;
    }
}
