package com.pojo.school;

import java.io.Serializable;

import org.bson.types.ObjectId;

import com.pojo.app.IdValuePairDTO;

/**
 * 老师班级课程
 * 
 * @author fourer
 *
 */

public class TeacherClassSubjectDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 45910516265175633L;

	private String id;
	private String teacherId;
	private IdValuePairDTO classInfo;
	private IdValuePairDTO subjectInfo;

	public TeacherClassSubjectDTO(TeacherClassSubjectEntry e) {
		this.id = e.getID().toString();
		this.teacherId = e.getTeacherId().toString();
		this.classInfo = new IdValuePairDTO(e.getClassInfo());
		this.subjectInfo = new IdValuePairDTO(e.getSubjectInfo());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public IdValuePairDTO getClassInfo() {
		return classInfo;
	}

	public void setClassInfo(IdValuePairDTO classInfo) {
		this.classInfo = classInfo;
	}

	public IdValuePairDTO getSubjectInfo() {
		return subjectInfo;
	}

	public void setSubjectInfo(IdValuePairDTO subjectInfo) {
		this.subjectInfo = subjectInfo;
	}

	
	public ObjectId getClassId()
	{
		if(null!=this.classInfo)
			return this.classInfo.getId();
		return null;
	}
	public String getName() {
		//todo
		try
		{
		return classInfo.getValue().toString()
				+ subjectInfo.getValue().toString();
		}catch(Exception ex)
		{
			
		}
		return "测试1";
	}

	public void setName(String name) {
		//this.name = name;
	}

}
