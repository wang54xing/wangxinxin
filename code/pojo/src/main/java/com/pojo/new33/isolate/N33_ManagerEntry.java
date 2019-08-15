package com.pojo.new33.isolate;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

public class N33_ManagerEntry extends BaseDBObject {

	private static final long serialVersionUID = 8744017310949496476L;

	public N33_ManagerEntry() {

	}
	
	public N33_ManagerEntry(BasicDBObject baseEntry) {
		setBaseEntry(baseEntry);
	}
	
	public N33_ManagerEntry(ObjectId sid,ObjectId userId,String userName,String concatWay) {
		BasicDBObject dbObject = new BasicDBObject().append("sid",sid).append("userId", userId)
				.append("userName", userName).append("concatWay", concatWay);
		setBaseEntry(dbObject);
	}
	
	public ObjectId getSid() {
		return getSimpleObjecIDValue("sid");
	}

	public void setSid(ObjectId schoolId) {
		setSimpleValue("sid", schoolId);
	}
	
	public ObjectId getUserId(){
		return getSimpleObjecIDValue("userId");
	}
	
	public void setUserId(ObjectId userId){
		setSimpleValue("userId",userId);
	}
	
	public void setUserName(String userName){
		setSimpleValue("userName", userName);
	}
	
	public String getUserName(){
		return getSimpleStringValueDef("userName", "");
	}

	public void setConcatWay(String concatWay){
		setSimpleValue("concatWay", concatWay);
	}
	
	public String getConcatWay(){
		return getSimpleStringValueDef("concatWay", "");
	}
}
