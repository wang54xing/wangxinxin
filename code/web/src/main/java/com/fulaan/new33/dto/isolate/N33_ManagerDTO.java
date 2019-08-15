package com.fulaan.new33.dto.isolate;

import org.bson.types.ObjectId;

import com.pojo.new33.isolate.N33_ManagerEntry;

public class N33_ManagerDTO {

	private String id;
    private String schoolId;
    private String userId;
    private String userName;
    private String contactWay;		//联系方式
    
    public N33_ManagerDTO() {
	    
    }
    
	public N33_ManagerDTO(String id, String schoolId, String userId,
			String userName, String contactWay) {
		super();
		this.id = id;
		this.schoolId = schoolId;
		this.userId = userId;
		this.userName = userName;
		this.contactWay = contactWay;
	}
	
	public N33_ManagerDTO(N33_ManagerEntry entry) {
		super();
		this.id = entry.getID().toString();
		this.schoolId = entry.getSid().toString();
		this.userId = entry.getUserId().toString();
		this.userName = entry.getUserName();
		this.contactWay = entry.getConcatWay();
	}
	
	public N33_ManagerEntry getEntry(){
		N33_ManagerEntry entry = new N33_ManagerEntry(new ObjectId(schoolId),new ObjectId(userId),userName,contactWay);
		return entry;
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}
    
}
