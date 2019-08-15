package com.pojo.autoPK;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

public class N33_ZouBanSetEntry extends BaseDBObject {

	private static final long serialVersionUID = 7954857132233359199L;

	public N33_ZouBanSetEntry() {}

	public N33_ZouBanSetEntry(DBObject dbo) {
		this((BasicDBObject) dbo);
	}

	public N33_ZouBanSetEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public N33_ZouBanSetEntry(ObjectId ciId, ObjectId schoolId, ObjectId gradeId, boolean flag, Integer count, Integer type) {
		super();
		BasicDBObject dbo = new BasicDBObject()
				.append("sid", schoolId)
				.append("ciId", ciId)
				.append("gradeId", gradeId)
				.append("type", type)  //规则类型（1.走班学科轮次一致     2.走班教室数量    3.教学班人数上限   4.走班教室上下浮动人数上限）
				.append("flag", flag) // true  启动规则     false   关闭规则
				.append("count", count);   //数量上限  如果类型是1，没有该字段
		setBaseEntry(dbo);
	}

	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}

	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("sid", schoolId);
	}

	public ObjectId getCiId() {
		return getSimpleObjecIDValue("ciId");
	}

	public void setCiId(ObjectId ciId) {
		setSimpleValue("ciId", ciId);
	}

	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gradeId");
	}

	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gradeId", gradeId);
	}

	public Integer getType() {
		return getSimpleIntegerValue("type");
	}

	public void setType(Integer type) {
		setSimpleValue("type", type);
	}

	public Boolean getFlag() {
		return getSimpleBoolean("flag");
	}

	public void setFlag(Boolean flag) {
		setSimpleValue("flag", flag);
	}

	public Integer getCount() {
		return getSimpleIntegerValue("count");
	}

	public void setCount(Integer count) {
		setSimpleValue("count", count);
	}

}
