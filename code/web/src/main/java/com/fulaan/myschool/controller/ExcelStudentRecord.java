package com.fulaan.myschool.controller;

/**
 * Created by Hao on 2015/6/2.
 */
public class ExcelStudentRecord {
    private String xuejiNumber;//学籍号
    private int gradeCode;//年级代码
    private String className;//班级名称
    private String studyNumber;//学号
    private String studentName;//学生姓名
    private int  sex;//行呗  1表示男 0 表示女
   


    public String getXuejiNumber() {
        return xuejiNumber;
    }

    public void setXuejiNumber(String xuejiNumber) {
        this.xuejiNumber = xuejiNumber;
    }

    public int getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(int gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudyNumber() {
        return studyNumber;
    }

    public void setStudyNumber(String studyNumber) {
        this.studyNumber = studyNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
    

	@Override
	public String toString() {
		return "ExcelStudentRecord [xuejiNumber=" + xuejiNumber
				+ ", gradeCode=" + gradeCode + ", className=" + className
				+ ", studyNumber=" + studyNumber + ", studentName="
				+ studentName + ", sex=" + sex + "]";
	}
    
    
    
    
}
