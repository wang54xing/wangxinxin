package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

public class N33_Count extends BaseDBObject {
	private static final long serialVersionUID = 7132915562893775449L;


	public N33_Count(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public N33_Count() {
	}

	public N33_Count(boolean submit, ObjectId sid, ObjectId xqid,Integer week,Integer tk) {
		super();
		BasicDBObject dbo = new BasicDBObject()
				.append("xqId", xqid)
				.append("sid", sid)
				.append("submit",submit)            //true 已经提交    false  未提交
				.append("week",week)
				.append("tk",tk);       //1    短期调课       2长期调课
		setBaseEntry(dbo);
	}

	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}

	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("sid", schoolId);
	}

	public boolean getSubmit() {
		return getSimpleBoolean("submit");
	}

	public void setSubmit(boolean submit) {
		setSimpleValue("submit", submit);
	}


	public ObjectId getXqId() {
		return getSimpleObjecIDValue("xqId");
	}

	public void setXqId(ObjectId xqId) {
		setSimpleValue("xqId", xqId);
	}

	public Integer getWeek() {
		return getSimpleIntegerValue("week");
	}

	public void setWeek(Integer week) {
		setSimpleValue("week", week);
	}

	public Integer getTk() {
		return getSimpleIntegerValue("tk");
	}

	public void setTk(Integer tk) {
		setSimpleValue("tk", tk);
	}
}
