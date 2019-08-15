package com.pojo.app;

import com.pojo.school.DepartmentEntry;
import com.pojo.school.Grade;
import com.pojo.video.VideoEntry;

import java.io.Serializable;
/**
 * 简单DTO
 * @author fourer
 *
 */
public class SimpleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3941711049731896606L;
	public Object id;
	public Object type;
	public Object type1;
	public Object value;
	public Object value1;
	public Object value2;
	public Object value3;
	public Object value4;
	public Object value5;
	private Long size;
	
	
	
	
	public SimpleDTO(Object id, Object value, Object value1, Object value2) {
		super();
		this.id = id;
		this.value = value;
		this.value1 = value1;
		this.value2 = value2;
	}


	public SimpleDTO() {
		super();
	}
	
	
	public SimpleDTO(Object type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}
	public SimpleDTO(Object id, Object type, Object value) {
		super();
		this.id = id;
		this.type = type;
		this.value = value;
	}
	public SimpleDTO(VideoEntry e) {
		super();
		this.id = e.getID().toString();
		this.type = e.getName();
		this.value = e.getImgUrl();
	}


	public SimpleDTO(Grade e) {
		super();
		this.id = e.getGradeId().toString();
		this.type = e.getGradeType();
		this.value = e.getName();
	}

	
	public SimpleDTO(DepartmentEntry e)
	{
		this.id=e.getID().toString();
		this.value=e.getName();
	}


	public Object getType() {
		return type;
	}
	public void setType(Object type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public Object getValue1() {
		return value1;
	}
	public void setValue1(Object value1) {
		this.value1 = value1;
	}
	public Object getValue2() {
		return value2;
	}
	public void setValue2(Object value2) {
		this.value2 = value2;
	}


	public Object getValue3() {
		return value3;
	}


	public void setValue3(Object value3) {
		this.value3 = value3;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Object getType1() {
		return type1;
	}

	public void setType1(Object type1) {
		this.type1 = type1;
	}

	public Object getValue4() {
		return value4;
	}

	public void setValue4(Object value4) {
		this.value4 = value4;
	}

	public Object getValue5() {
		return value5;
	}

	public void setValue5(Object value5) {
		this.value5 = value5;
	}
}
