package com.pojo.user;

import com.pojo.app.IdValuePairDTO;

import java.util.List;

/**
 * Created by Hao on 2015/3/31.
 *
 * 学生统计信息
 */
public class StudentStat {
    private String studentId;
    private int role;
    private String studentNum;
    private String studentJob;//职务
    private String userName;
    private String imageURL;//头像
    private int endViewNum;//视频观看数量endViewNum
    private int endQuestionNum;//习题做完数量
    private int experienceValue;
    private String className;
    private String gradeName;
    private int sex;
    private String interestClassInfo;
    private String attendance;
    private List<IdValuePairDTO> interestClassList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getEndViewNum() {
        return endViewNum;
    }

    public void setEndViewNum(int endViewNum) {
        this.endViewNum = endViewNum;
    }

    public int getEndQuestionNum() {
        return endQuestionNum;
    }

    public void setEndQuestionNum(int endQuestionNum) {
        this.endQuestionNum = endQuestionNum;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getExperienceValue() {
        return experienceValue;
    }

    public void setExperienceValue(int experienceValue) {
        this.experienceValue = experienceValue;
    }
    public String getStudentJob() {
        return studentJob;
    }

    public void setStudentJob(String studentJob) {
        this.studentJob = studentJob;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSexStr(){
        if(getSex()==0){
            return "女";
        }
        if(getSex()==1){
            return "男";
        }
        if(getSex()==-1){
            return "未知";
        }
        return "";
    }

    public String getInterestClassInfo() {
        return interestClassInfo;
    }

    public void setInterestClassInfo(String interestClassInfo) {
        this.interestClassInfo = interestClassInfo;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public List<IdValuePairDTO> getInterestClassList() {
        return interestClassList;
    }

    public void setInterestClassList(List<IdValuePairDTO> interestClassList) {
        this.interestClassList = interestClassList;
    }
}
