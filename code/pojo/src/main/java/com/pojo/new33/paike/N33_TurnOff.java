package com.pojo.new33.paike;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 开关
 * {
 *  ciId: ciId 排课次
 * sid: sid 学校
 * gid : gid 年级
 * status : status    0 开   1 关（例如1不允许走班格放置非走班课）
 * type : type      1允许走班格放置非走班教学班 2.允许当日调课  3.设置查看课表时无课格子显示内容
 * kbType : kbType    1:教室课表    2：教师课表   3：学生课表  4：行政班课表  5：学科课表
 * acClass : 0 未开启   1 开启  （当前学校年级是否开启重分行政班按钮）
 * }
 * Created by wang_xinxin on 2018/3/7.
 */
public class N33_TurnOff extends BaseDBObject {

	private static final long serialVersionUID = -6044673618528311521L;

	public N33_TurnOff(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public N33_TurnOff() {}

	public N33_TurnOff(ObjectId ciId, ObjectId sid, ObjectId gradeId, Integer status,Integer type) {
		super();
		BasicDBObject dbo =new BasicDBObject()
				.append("ciId", ciId)
				.append("sid", sid)
				.append("gid", gradeId)
				.append("status",status)
				.append("type",type);
		setBaseEntry(dbo);
	}

	public N33_TurnOff(ObjectId ciId, ObjectId sid, ObjectId gradeId,Integer type,String desc,Integer kbType) {
		super();
		BasicDBObject dbo =new BasicDBObject()
				.append("ciId", ciId)
				.append("sid", sid)
				.append("gid", gradeId)
				.append("type",type)
				.append("desc",desc)
				.append("kbType",kbType);
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
		return getSimpleObjecIDValue("gid");
	}
	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gid", gradeId);
	}

	public Integer getStatus() {
		return getSimpleIntegerValue("status");
	}
	public void setStatus(Integer status) {
		setSimpleValue("status",status);
	}
	public Integer getType() {
		return getSimpleIntegerValue("type");
	}
	public void setType(Integer type) {
		setSimpleValue("type",type);
	}
	public String getDesc() {
		return getSimpleStringValue("desc");
	}
	public void setDesc(String desc) {
		setSimpleValue("desc",desc);
	}

	public Integer getKbType(){
		return getSimpleIntegerValue("kbType");
	}
	public void setKbType(Integer kbType) {
		setSimpleValue("kbType",kbType);
	}

	public Integer getAcClass(){
		return getSimpleIntegerValueDef("acClass", 0);
	}
	public void setAcClass(Integer acClass) {
		setSimpleValue("acClass",acClass);
	}
}
