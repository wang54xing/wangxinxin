package com.fulaan.schoolbase.dto;

import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2017/1/19.
 */
public class TimeTableDTO {

    private String id;

    private String schoolId;

    private String name;

    private String startTime;

    private String endTime;

    private Integer type;
    
    private String typeName;

    private String remark;
    
    private Integer sjd;
    
    private String shiduan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getTypeName() {
		if(this.type==0) {
			return "课间";
		}
		else if(this.type==1){
			return "课节";
		}
		return "";
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

	public Integer getSjd() {
		return sjd;
	}

	public void setSjd(Integer sjd) {
		this.sjd = sjd;
	}

	public String getShiduan() {
		return shiduan;
	}

	
    

}
