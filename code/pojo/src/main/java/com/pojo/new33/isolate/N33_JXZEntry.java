package com.pojo.new33.isolate;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 教学周
 * @author rick
 *
 */
public class N33_JXZEntry extends BaseDBObject {

	public N33_JXZEntry() {}
	public N33_JXZEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
	public N33_JXZEntry(ObjectId schoolId,ObjectId xqid,Integer serial,long start,long end) {
		super();
        BasicDBObject dbo = new BasicDBObject()
                .append("sid", schoolId)
                .append("xqid", xqid)
                .append("serial", serial)// 第几周
                .append("start", start)
                .append("end", end);
        setBaseEntry(dbo);
	}
	 public ObjectId getSchoolId() {
	        return getSimpleObjecIDValue("sid");
	    }

	    public void setSchoolId(ObjectId schoolId) {
	        setSimpleValue("sid", schoolId);
	    }

	    public void setXQId(ObjectId xqid) {
	        setSimpleValue("xqid", xqid);
	    }
	    public ObjectId getXQId() {
	        return getSimpleObjecIDValue("xqid");
	    }
	    public Integer getSerial() {
	    	return getSimpleIntegerValue("serial");
	    }
	    public void setSerial(Integer serial) {
	    	setSimpleValue("serial", serial);
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
