package com.fulaan.log.dto;

import com.pojo.log.LogEntry;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Set;

public class LogDTO {

    private String id;

	private String userId;

    private int platformType;

	private int actionType;

	private Date actionTime = new Date();

	private String actionName;

    private ObjectId schoolId;

    private Set<ObjectId> gradeIds;

    private Set<Integer> gradeTys;

    private Set<ObjectId> classIds;

    private int userRole;

    public LogDTO(){

    }

    public LogDTO(LogEntry logEntry) {
        this.id= logEntry.getID().toString();
        this.userId = logEntry.getUserId().toString();
        this.platformType=logEntry.getPlatformType();
        this.actionType = logEntry.getActionType();
        this.actionTime = new Date(logEntry.getActionTime());
        this.actionName = logEntry.getActionName();
        this.schoolId = logEntry.getSchoolId();
        this.gradeIds = logEntry.getGradeIds();
        this.gradeTys = logEntry.getGradeTys();
        this.classIds = logEntry.getClassIds();
        this.userRole=logEntry.getUserRole();
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

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

    public ObjectId getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(ObjectId schoolId) {
        this.schoolId = schoolId;
    }

    public Set<ObjectId> getGradeIds() {
        return gradeIds;
    }

    public void setGradeIds(Set<ObjectId> gradeIds) {
        this.gradeIds = gradeIds;
    }

    public Set<ObjectId> getClassIds() {
        return classIds;
    }

    public void setClassIds(Set<ObjectId> classIds) {
        this.classIds = classIds;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public Set<Integer> getGradeTys() {
        return gradeTys;
    }

    public void setGradeTys(Set<Integer> gradeTys) {
        this.gradeTys = gradeTys;
    }

    public LogEntry buildLogEntry()
    {
        LogEntry logEntry =new LogEntry(
                new ObjectId(this.getUserId()),
                this.getUserRole(),
                this.getPlatformType(),
                this.getActionType(),
                this.getActionTime().getTime(),
                this.getActionName(),
                this.getSchoolId(),
                this.getGradeIds(),
                this.getGradeTys(),
                this.getClassIds()
        );
        return logEntry;
    }

}
