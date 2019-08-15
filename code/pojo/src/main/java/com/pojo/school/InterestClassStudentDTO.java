package com.pojo.school;

/**
 * Created by Hao on 2015/4/21.
 */
public class InterestClassStudentDTO {
    private int courseType;
    private String studentId;
    private String studentName;
    private String className;//所在行政班级名称
    private int termType;
    private int dropState;

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public int getDropState() {
        return dropState;
    }

    public void setDropState(int dropState) {
        this.dropState = dropState;
    }
}
