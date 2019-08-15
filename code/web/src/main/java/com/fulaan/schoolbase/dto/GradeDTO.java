package com.fulaan.schoolbase.dto;

import com.fulaan.utils.StringUtil;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * 班级信息
 * @author fourer
 *
 */
public class GradeDTO implements Serializable {
	private static final long serialVersionUID = -5197596091397995948L;
    private String id;
    private String schoolId;
    private String defaultGradeName;
	private String gradeName;
    private int gradeNumber;
	private String type;
	private String admissionDate;
	private String graduateDate;
    private String introduce;
    private String createrId;
    private int classCount;
    private String gradeUserId;
    private String gradeUserName;
    private String gradeIds;
    private String gradeNames;
    private String userIds;
    private String subjectIds;
    private String gradeUserNames;
    private String gradeUserIds;
    private int graduate;
    public GradeDTO()
	{

    }


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public String getDefaultGradeName() {
        return defaultGradeName;
    }

    public void setDefaultGradeName(String defaultGradeName) {
        this.defaultGradeName = defaultGradeName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getGradeNumber() {
        return gradeNumber;
    }

    public void setGradeNumber(int gradeNumber) {
        this.gradeNumber = gradeNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public String getGradeUserId() {
        return gradeUserId;
    }

    public void setGradeUserId(String gradeUserId) {
        this.gradeUserId = gradeUserId;
    }

    public String getGradeUserName() {
        return gradeUserName;
    }

    public void setGradeUserName(String gradeUserName) {
        this.gradeUserName = gradeUserName;
    }

    public String getGradeIds() {
        return gradeIds;
    }

    public void setGradeIds(String gradeIds) {
        this.gradeIds = gradeIds;
    }

    public String getGradeNames() {
        return gradeNames;
    }

    public void setGradeNames(String gradeNames) {
        this.gradeNames = gradeNames;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getGradeUserNames() {
        return gradeUserNames;
    }

    public void setGradeUserNames(String gradeUserNames) {
        this.gradeUserNames = gradeUserNames;
    }
    
    public int getGraduate() {
		return graduate;
	}

	public void setGraduate(int graduate) {
		this.graduate = graduate;
	}


	public String getGraduateDate() {
		return graduateDate;
	}


	public void setGraduateDate(String graduateDate) {
		this.graduateDate = graduateDate;
	}


	public String getGradeUserIds() {
		return gradeUserIds;
	}


	public void setGradeUserIds(String gradeUserIds) {
		this.gradeUserIds = gradeUserIds;
	}
	
}
