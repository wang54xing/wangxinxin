package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

public class N33_RemovedXY extends BaseDBObject {
	private static final long serialVersionUID = -4589046154540581032L;

	public N33_RemovedXY(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public N33_RemovedXY() {}

	public N33_RemovedXY(ObjectId classRoomId, Integer x, Integer y,
	                      ObjectId termId, ObjectId sid) {
		super();
		BasicDBObject dbo =new BasicDBObject()
				.append("sid",sid)
				.append("termId",termId)
				.append("classRoomId",classRoomId)
				.append("x",x)
				.append("y",y);
		setBaseEntry(dbo);
	}

	public Integer getX() {
		return getSimpleIntegerValue("x");
	}
	public void setX(Integer x) {
		setSimpleValue("x",x);
	}

	public Integer getY() {
		return getSimpleIntegerValue("y");
	}
	public void setY(Integer y) {
		setSimpleValue("y",y);
	}

	public ObjectId getClassRoomId() {
		return getSimpleObjecIDValue("classRoomId");
	}
	public void setClassRoomId(ObjectId classRoomId) {
		setSimpleValue("classRoomId", classRoomId);
	}
	public ObjectId getTermId() {
		return getSimpleObjecIDValue("termId");
	}
	public void setTermId(ObjectId termId) {
		setSimpleValue("termId", termId);
	}
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}
	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("sid", schoolId);
	}
}
