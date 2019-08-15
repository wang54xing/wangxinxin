package com.pojo.school;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;

/**
 * 老师班级课程表，主要服务于学习中心.班级课程
 * <pre>
 * collectionName:tcsubjects
 * </pre>
 * <pre>
 * {
    ti:老师ID
    cli:
    {
     id:objectid 班级ID
     v:班级名字
    }
    sui：对应 SchoolEntry.Subject
    {
     id:objectID
     v:名字
    }
 * }
 * </pre>
 * @author fourer
 */
public class TeacherClassSubjectEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1633480490994548066L;

	
	public TeacherClassSubjectEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public TeacherClassSubjectEntry(ObjectId teacherId, IdValuePair classInfo,
			IdValuePair subjectInfo) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("ti", teacherId)
		.append("cli", classInfo.getBaseEntry())
		.append("sui", subjectInfo.getBaseEntry());
		setBaseEntry(dbo);
	}

	
	public ObjectId getTeacherId() {
		return getSimpleObjecIDValue("ti");
	}
	public void setTeacherId(ObjectId teacherId) {
		setSimpleValue("ti", teacherId);
	}
	
	

	public IdValuePair getClassInfo() {
		
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("cli");
		return new  IdValuePair(dbo);
	}
	public void setClassInfo(IdValuePair classInfo) {
		setSimpleValue("cli", classInfo.getBaseEntry());
	}
	public IdValuePair getSubjectInfo() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("sui");
		return new  IdValuePair(dbo);
	}
	public void setSubjectInfo(IdValuePair subjectInfo) {
		setSimpleValue("sui", subjectInfo.getBaseEntry());
	}
	
	
}
