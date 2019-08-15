package com.pojo.new33.isolate;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

public class N33_DefaultTermEntry extends BaseDBObject {

	private static final long serialVersionUID = 940401643554531909L;

	public N33_DefaultTermEntry() {

	}

	public N33_DefaultTermEntry(BasicDBObject baseEntry) {
		setBaseEntry(baseEntry);
	}
	public N33_DefaultTermEntry(ObjectId xqid,ObjectId schoolId) {
		BasicDBObject dbObject = new BasicDBObject()
				.append("xqid",xqid)
				.append("sid", schoolId)
				;// 排课次
		setBaseEntry(dbObject);
	}
	public N33_DefaultTermEntry(ObjectId xqid,ObjectId schoolId,ObjectId paikeXqId,ObjectId paikeCiId) {
		BasicDBObject dbObject = new BasicDBObject()
				.append("xqid",xqid)
				.append("sid", schoolId)
				.append("paikexq",paikeXqId) // 排课学期
				.append("paikeci",paikeCiId);// 排课次
		setBaseEntry(dbObject);
	}

	public ObjectId getSid() {
		return getSimpleObjecIDValue("sid");
	}

	public void setSid(ObjectId schoolId) {
		setSimpleValue("sid", schoolId);
	}
	
	public ObjectId getXqid() {
		return getSimpleObjecIDValue("xqid");
	}

	public void setXqid(ObjectId xqid) {
		setSimpleValue("xqid", xqid);
	}

	public ObjectId getPaikeXqid() {
		return getSimpleObjecIDValue("paikexq");
	}

	public void setPaikeXqid(ObjectId xqid) {
		setSimpleValue("paikexq", xqid);
	}

	public ObjectId getPaikeci(){
		return getSimpleObjecIDValue("paikeci");
	}

	public void setPaikeci(ObjectId paikeCiId){
		setSimpleValue("paikeci",paikeCiId);
	}
}
