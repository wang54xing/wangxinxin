package com.pojo.school;

import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

/**
 * 部门文件DTO
 * @author fourer
 *
 */
public class DepartmentFileDTO {

	private String id;
	private String name;
	private String size;
	private String type;
	private String time;
	private String user;
	
	
	public DepartmentFileDTO(DepartmentFile file)
	{
		this.id=file.getId().toString();
		this.name=file.getName();
		//this.size="5M";
		
	
		if(file.getSize()>1024*1024)
		{
			Long s=file.getSize()/1024*1024;
			this.size=String.valueOf(s.intValue())+"M";
		}
		else if(file.getSize()>1024)
		{
			Long s=file.getSize()/1024;
			this.size=String.valueOf(s.intValue())+"K";
		}
		else
		{
			this.size=String.valueOf(file.getSize())+"B";
		}
		
		
		
		int index=this.name.indexOf(Constant.POINT);
		this.type=file.getName().substring(index+1);
		this.time=DateTimeUtils.convert(file.getId().getTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_H);
	}
	
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


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	@Override
	public String toString() {
		return "DepartmentFileDTO [id=" + id + ", name=" + name + ", size="
				+ size + ", type=" + type + ", time=" + time + ", user=" + user
				+ "]";
	}
	
	
}
