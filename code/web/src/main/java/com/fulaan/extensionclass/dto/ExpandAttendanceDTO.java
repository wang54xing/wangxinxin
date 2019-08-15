package com.fulaan.extensionclass.dto;

public class ExpandAttendanceDTO {

	private String id;
	
	private String expandOpenId;
	
	private String userId;
	
	private String name;
	
	private String clazzName;
	
	private int status;
	
	private long time;
	
	private String date;
	
	public ExpandAttendanceDTO() {}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExpandOpenId() {
		return expandOpenId;
	}

	public void setExpandOpenId(String expandOpenId) {
		this.expandOpenId = expandOpenId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
