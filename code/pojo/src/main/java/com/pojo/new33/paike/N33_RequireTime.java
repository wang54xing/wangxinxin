package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

public class N33_RequireTime extends BaseDBObject {

	private static final long serialVersionUID = -9043556208462714907L;
	public N33_RequireTime(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public N33_RequireTime() {}

	public N33_RequireTime(ObjectId schoolId, ObjectId termId, long start, long end) {
		super();
		BasicDBObject dbo =new BasicDBObject()
				.append("sid",schoolId)
				.append("termId",termId)
				.append("ir", Constant.ZERO)
				.append("start", start)
				.append("end",end);
		setBaseEntry(dbo);
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
