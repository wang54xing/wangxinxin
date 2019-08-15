package com.fulaan.schoolbase.dto;


import org.bson.types.ObjectId;



/**
 * @author rick
 * @time 2017年9月22日 下午3:30:09
 *
 */
public class JobLevelDTO {
private String id;
	
	private String schoolId;
	
	private String name;
	
	public JobLevelDTO() {}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
	


