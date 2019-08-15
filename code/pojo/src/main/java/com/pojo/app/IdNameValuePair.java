package com.pojo.app;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

public class IdNameValuePair extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6209553135316072632L;
	public IdNameValuePair() {
	}
	public IdNameValuePair(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	public IdNameValuePair(ObjectId id) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("id", id)
		.append("nm", null)
		.append("v", null);
		setBaseEntry(baseEntry);
	}

	public IdNameValuePair(ObjectId id,String name, Object value) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("id", id)
		.append("nm", name)
		.append("v", value);
		setBaseEntry(baseEntry);
	}
	
	
	public IdNameValuePair(Object id,String name, Object value) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("id", id)
		.append("nm", name)
		.append("v", value);
		setBaseEntry(baseEntry);
	}

	public IdNameValuePair(Object id,String name, ObjectId value) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
				.append("id", id)
				.append("nm", name)
				.append("v", value);
		setBaseEntry(baseEntry);
	}
	
	
	public ObjectId getId() {
		return getSimpleObjecIDValue("id");
	}
	public void setId(ObjectId id) {
		setSimpleValue("id", id);
	}
	
	public int getIntId() {
		return getSimpleIntegerValue("id");
	}
	public void setIntId(int id) {
		setSimpleValue("id", id);
	}
	
	
	
	public Object getValue() {
		return getSimpleObjectValue("v");
	}
	public void setValue(Object value) {
		setSimpleValue("v", value);
	}

	public String getName() {
		return getSimpleStringValue("nm");
	}

	public void setName(String name) {
		setSimpleValue("nm", name);
	}

}
