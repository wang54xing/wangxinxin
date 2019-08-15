package com.fulaan.schoolbase.dto;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by wang_xinxin on 2017/1/22.
 */
public class ResumeDTO {

    private String id;

    private String userId;

    private String schoolId;

    private String userName;

    private int sex;

    private String subjectIds;

    private String birth;

    private String birthPlace;

    private String nation;

    private String cardNum;

    private int maritalStatus;

    private String residence;

    private String address;

    private String phone;

    private String education;

    private String major;

    private String schoolName;

    private int type;

    private List<String> subjectIdList;

    private int status;
    
    private String userimg;

    private String subjectNames;

    private int isSubjectLeader;

    private int isGradeLeader;
    
    private String dutyId;
    private String jobLevelId;
    private String dutyName;
    private String jobLevelName;
    private int politicalLandscape;

    public ResumeDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public int getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(int maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getSubjectIdList() {
        return subjectIdList;
    }

    public void setSubjectIdList(List<String> subjectIdList) {
        this.subjectIdList = subjectIdList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

    public int getIsGradeLeader() {
        return isGradeLeader;
    }

    public void setIsGradeLeader(int isGradeLeader) {
        this.isGradeLeader = isGradeLeader;
    }

    public int getIsSubjectLeader() {
        return isSubjectLeader;
    }

    public void setIsSubjectLeader(int isSubjectLeader) {
        this.isSubjectLeader = isSubjectLeader;
    }

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public String getDutyId() {
		return dutyId;
	}

	public void setDutyId(String dutyId) {
		this.dutyId = dutyId;
	}

	public String getJobLevelId() {
		return jobLevelId;
	}

	public void setJobLevelId(String jobLevelId) {
		this.jobLevelId = jobLevelId;
	}

	public String getDutyName() {
		return dutyName;
	}

	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}

	public String getJobLevelName() {
		return jobLevelName;
	}

	public void setJobLevelName(String jobLevelName) {
		this.jobLevelName = jobLevelName;
	}

    public int getPoliticalLandscape() {
        return politicalLandscape;
    }

    public void setPoliticalLandscape(int politicalLandscape) {
        this.politicalLandscape = politicalLandscape;
    }
}
