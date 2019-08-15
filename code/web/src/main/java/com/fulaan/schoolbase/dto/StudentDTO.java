package com.fulaan.schoolbase.dto;

/**
 * Created by wang_xinxin on 2017/2/9.
 */
public class StudentDTO {
    private String classId;

    private String studentCode;

    private int membership;

    private String membershipName;

    private int sex;

    private String className;

    private String userName;

    private String nickName;

    private String userImage;

    private String userId;

    private String intake;//入学时间

    private String schoolId;

    private String orgClassId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public int getMembership() {
        return membership;
    }

    public void setMembership(int membership) {
        this.membership = membership;
    }

    public String getMembershipName() {
        return membershipName;
    }

    public void setMembershipName(String membershipName) {
        this.membershipName = membershipName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getOrgClassId() {
        return orgClassId;
    }

    public void setOrgClassId(String orgClassId) {
        this.orgClassId = orgClassId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
