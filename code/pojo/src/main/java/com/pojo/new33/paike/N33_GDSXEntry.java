package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

public class N33_GDSXEntry extends BaseDBObject {

	private static final long serialVersionUID = -2081374655241301577L;

	public N33_GDSXEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public N33_GDSXEntry() {}

	public N33_GDSXEntry(int x, int y, ObjectId gradeId, String desc, ObjectId schoolId, ObjectId termId) {
		super();
		BasicDBObject dbo = new BasicDBObject()
				.append("x",x)
				.append("y",y)
				.append("desc",desc)
				.append("sid",schoolId)
				.append("termId",termId)
				.append("gid",gradeId)
				.append("ir", Constant.ZERO);
		setBaseEntry(dbo);
	}

	public int getX() {
		return getSimpleIntegerValue("x");
	}
	public void setX(int x) {
		setSimpleValue("x",x);
	}
	public int getY() {
		return getSimpleIntegerValue("y");
	}
	public void setY(int y) {
		setSimpleValue("y",y);
	}
	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gid");
	}
	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gid",gradeId);
	}
	public String getDesc() {
		return getSimpleStringValue("desc");
	}
	public void setDesc(String desc) {
		setSimpleValue("desc",desc);
	}
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}
	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("sid", schoolId);
	}
	public ObjectId getTermId() {
		return getSimpleObjecIDValue("termId");
	}
	public void setTermId(ObjectId termId) {
		setSimpleValue("termId", termId);
	}
}
