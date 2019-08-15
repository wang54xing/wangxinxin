package com.fulaan.schoolbase.dto;

import java.util.List;

/**
 * Created by wang_xinxin on 2017/1/15.
 */
public class SubjectDTO {

    private String id;

    private String schoolId;

    private String subjectName;

    private String leadUser;

    private String leadUserName;

    private List<String> userIds;

    private String userNames;

    private int subjectType;
    
    private String subjectTypeName;

    //附属学科
    private String attachedSubjectId;

    private int index;

    public SubjectDTO() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getLeadUser() {
        return leadUser;
    }

    public void setLeadUser(String leadUser) {
        this.leadUser = leadUser;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getLeadUserName() {
        return leadUserName;
    }

    public void setLeadUserName(String leadUserName) {
        this.leadUserName = leadUserName;
    }

    public int getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(int subjectType) {
        this.subjectType = subjectType;
    }

    public String getAttachedSubjectId() {
        return attachedSubjectId;
    }

    public void setAttachedSubjectId(String attachedSubjectId) {
        this.attachedSubjectId = attachedSubjectId;
    }


	public String getSubjectTypeName() {
		if(this.subjectType==0) {
			return "主学科";
		}
		else {
			return "附属学科";
		}
		
	}

    public void setSubjectTypeName(String subjectTypeName) {
        this.subjectTypeName = subjectTypeName;
    }

    public int getIndex() {

        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
