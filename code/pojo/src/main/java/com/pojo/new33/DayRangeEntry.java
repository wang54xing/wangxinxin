package com.pojo.new33;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;


/**
 * 课表区间设置
 * {
 * 		sid
 * 		start1:上午开始时间
 * 		end1:上午结束时间
 * 		start2:下午开始时间
 * 		end2:下午结束时间
 * 		start3:晚上开始时间
 * 		end3:晚上结束时间
 * }
 * 
 * @author rick
 *
 */
public class DayRangeEntry extends BaseDBObject {

	private static final long serialVersionUID = 5523894644360446610L;
	
	public DayRangeEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	public DayRangeEntry() {}
	
	public DayRangeEntry(ObjectId schoolId,long start1,long end1
			,long start2,long end2,long start3,long end3) {
		super();
		 BasicDBObject dbo =new BasicDBObject()
				 .append("sid", schoolId)
				 .append("start1", start1)
				 .append("end1", end1)
				 .append("start2", start2)
				 .append("end2", end2)
				 .append("start3", start3)
				 .append("end3", end3);
		 setBaseEntry(dbo);
	}
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}
	public void setSchoolId(ObjectId sid) {
		setSimpleValue("sid", sid);
	}
	
	public long getStart1() {
		return getSimpleLongValue("start1");
	}
	public void setStart1(long start) {
		setSimpleValue("start1", start);
	}
	public long getEnd1() {
		return getSimpleLongValue("end1");
	}
	public void setEnd1(long end) {
		setSimpleValue("end1", end);
	}
	
	public long getStart2() {
		return getSimpleLongValue("start2");
	}
	public void setStart2(long start) {
		setSimpleValue("start2", start);
	}
	public long getEnd2() {
		return getSimpleLongValue("end2");
	}
	public void setEnd2(long end) {
		setSimpleValue("end2", end);
	}
	
	public long getStart3() {
		return getSimpleLongValue("start3");
	}
	public void setStart3(long start) {
		setSimpleValue("start3", start);
	}
	public long getEnd3() {
		return getSimpleLongValue("end3");
	}
	public void setEnd3(long end) {
		setSimpleValue("end3", end);
	}
	
}
