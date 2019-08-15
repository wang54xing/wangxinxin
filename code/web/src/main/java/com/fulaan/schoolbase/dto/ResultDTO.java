package com.fulaan.schoolbase.dto;

import java.util.ArrayList;
import java.util.List;

import com.pojo.app.IdNameValuePairDTO;

public class ResultDTO {

	private String id;
	
	private String userId;
	
	private String schoolId;
	
	private String introduce;
	
	private String level;
	
	private String time;
	
	private List<IdNameValuePairDTO> files = new ArrayList<IdNameValuePairDTO>();

	public ResultDTO() {}
	
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

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<IdNameValuePairDTO> getFiles() {
		return files;
	}

	public void setFiles(List<IdNameValuePairDTO> files) {
		this.files = files;
	}
	
}
