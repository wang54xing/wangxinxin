package com.pojo.new33;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

public class N33_DaKaEntry extends BaseDBObject {
	private static final long serialVersionUID = -931725109620317584L;

	public N33_DaKaEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public N33_DaKaEntry() {
	}

	public N33_DaKaEntry(ObjectId zkbId, ObjectId studentId, int flag) {
		super();
		BasicDBObject dbo = new BasicDBObject()
				.append("zkbId", zkbId)
				.append("studentId", studentId)
				.append("flag", flag);
		setBaseEntry(dbo);
	}

	public ObjectId getZkbId() {
		return getSimpleObjecIDValue("zkbId");
	}

	public void setZkbId(ObjectId zkbId) {
		setSimpleValue("zkbId", zkbId);
	}

	public ObjectId getStudentId() {
		return getSimpleObjecIDValue("studentId");
	}

	public void setStudentId(ObjectId studentId) {
		setSimpleValue("studentId", studentId);
	}

	public int getDaKaFlag() {
		return getSimpleIntegerValue("flag");
	}

	public void setDaKaFlag(int flag) {
		setSimpleValue("flag", flag);
	}
}
