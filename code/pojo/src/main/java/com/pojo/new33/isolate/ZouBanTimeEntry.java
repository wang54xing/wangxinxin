package com.pojo.new33.isolate;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

public class ZouBanTimeEntry extends BaseDBObject{

	private static final long serialVersionUID = -7066657935530132482L;

	public ZouBanTimeEntry(BasicDBObject baseEntry) {
		setBaseEntry(baseEntry);
	}

	public ZouBanTimeEntry() {

	}

	public ZouBanTimeEntry(ObjectId schoolId, ObjectId xqid, Integer type, Integer x, Integer y,ObjectId gid) {
		BasicDBObject dbObject = new BasicDBObject()
				.append("sid", schoolId)
				.append("xqid", xqid)
				.append("type", type)       //0 是走班时间       1 非走班时间
				.append("gid", gid)
				.append("x", x)
				.append("y", y)
				.append("ir", Constant.ZERO);
		setBaseEntry(dbObject);
	}

	public ObjectId getSid() {
		return getSimpleObjecIDValue("sid");
	}
	public void setSid(ObjectId sid) {
		setSimpleValue("sid", sid);
	}

	public ObjectId getXqid() {
		return getSimpleObjecIDValue("xqid");
	}
	public void setXqid(ObjectId xqid) {
		setSimpleValue("xqid", xqid);
	}

	public void setType(Integer type){
		setSimpleValue("type",type);
	}
	public Integer getType(){
		return getSimpleIntegerValue("type");
	}

	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gid");
	}
	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gid", gradeId);
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
}
