package com.pojo.new33;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 课节区间
 * {
 * 		sid:学校id
 * 		xqid 学期id
 * 		serial:序号
 * 		name:课节名
 * 		start:开始时间
 * 		end:结束时间
 * 		
 * }
 * @author rick
 *
 */
public class CourseRangeEntry extends BaseDBObject {

	private static final long serialVersionUID = 5523894644360446610L;
	
	public CourseRangeEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	public CourseRangeEntry() {}
	
	public CourseRangeEntry(ObjectId schoolId,ObjectId xqid,Integer serial,String name,long start,long end) {
		super();
		BasicDBObject dbo =new BasicDBObject()
				.append("sid", schoolId)
				.append("xqid", xqid)
				.append("serial", serial)
				.append("name", name)
				.append("start", start)
				.append("end", end);
		 setBaseEntry(dbo);
	}
	
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}
	public void setSchoolId(ObjectId sid) {
		setSimpleValue("sid", sid);
	}
	public ObjectId getXqId() {
		return getSimpleObjecIDValue("xqid");
	}
	public void setXqId(ObjectId xqid) {
		setSimpleValue("xqid", xqid);
	}
	public Integer getSerial() {
		return getSimpleIntegerValue("serial");
	}
	public void setSerial(Integer serial) {
		setSimpleValue("serial", serial);
	}
	public String getName() {
		return getSimpleStringValue("name");
	}
	public void setName(String name) {
		setSimpleValue("name", name);
	}
	public long getStart() {
		return getSimpleLongValue("start");
	}
	public void setStart(long start) {
		setSimpleValue("start", start);
	}
	public long getEnd() {
		return getSimpleLongValue("end");
	}
	public void setEnd(long end) {
		setSimpleValue("end", end);
	}
	
}
