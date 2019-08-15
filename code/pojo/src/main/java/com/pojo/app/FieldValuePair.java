package com.pojo.app;

import org.bson.types.ObjectId;

/**
 * 存储字段与字段值
 * @author fourer
 *
 */
public class FieldValuePair {
	private String field;
	private Object value;
	private String weekIndex;
	
	public FieldValuePair(String field, Object value) {
		super();
		this.field = field;
		this.value = value;
	}

	public FieldValuePair(String field, Object value, String weekIndex) {
		super();
		this.field = field;
		this.value = value;
		this.weekIndex = weekIndex;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getWeekIndex() {
		return weekIndex;
	}

	public void setWeekIndex(String weekIndex) {
		this.weekIndex = weekIndex;
	}
}
