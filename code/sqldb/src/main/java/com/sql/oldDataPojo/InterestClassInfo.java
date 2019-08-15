package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/3.
 */
public class InterestClassInfo {

    private int id;
    private int schoolid;

    private String classname;
    private String classcontent;


    private int teacherId;

    private int actualcount;

    private String classtime;

    private int subjectId;

    private Date opentime;

    private Date closetime;

    private int firstteam;

    private int secondteam;

    private String gradeArry;

    private int coursetype; //长课2，短课1

    private int isopen;


    private int termType;

    private int studentcount;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClasscontent() {
        return classcontent;
    }

    public void setClasscontent(String classcontent) {
        this.classcontent = classcontent;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getActualcount() {
        return actualcount;
    }

    public void setActualcount(int actualcount) {
        this.actualcount = actualcount;
    }

    public String getClasstime() {
        return classtime;
    }

    public void setClasstime(String classtime) {
        this.classtime = classtime;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public Date getOpentime() {
        return opentime;
    }

    public void setOpentime(Date opentime) {
        this.opentime = opentime;
    }

    public Date getClosetime() {
        return closetime;
    }

    public void setClosetime(Date closetime) {
        this.closetime = closetime;
    }

    public int getFirstteam() {
        return firstteam;
    }

    public void setFirstteam(int firstteam) {
        this.firstteam = firstteam;
    }

    public int getSecondteam() {
        return secondteam;
    }

    public void setSecondteam(int secondteam) {
        this.secondteam = secondteam;
    }

    public String getGradeArry() {
        return gradeArry;
    }

    public void setGradeArry(String gradeArry) {
        this.gradeArry = gradeArry;
    }

    public int getCoursetype() {
        return coursetype;
    }

    public void setCoursetype(int coursetype) {
        this.coursetype = coursetype;
    }

    public int getIsopen() {
        return isopen;
    }

    public void setIsopen(int isopen) {
        this.isopen = isopen;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public int getStudentcount() {
        return studentcount;
    }

    public void setStudentcount(int studentcount) {
        this.studentcount = studentcount;
    }

    public int getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(int schoolid) {
        this.schoolid = schoolid;
    }
}
