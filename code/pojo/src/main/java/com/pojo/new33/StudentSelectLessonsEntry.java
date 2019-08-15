package com.pojo.new33;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 学生选课结果表
 * {
 *  t:学期
 *  si:学校id
 *  ci：班级id
 *  sti:学生id
 *  ses：选课结果
 * }
 * @author fourer
 *
 */
public class StudentSelectLessonsEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6550101574132063922L;
	
	
	public StudentSelectLessonsEntry(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}
	public StudentSelectLessonsEntry(ObjectId term,ObjectId schoolId, ObjectId classId,
			ObjectId studentId, List<ObjectId> selects) {
         super();
		 
		 BasicDBObject dbo =new BasicDBObject("t",term)
				 .append("si", schoolId)
		                    .append("ci", classId)
		                    .append("sti", studentId)
		                    .append("ses", MongoUtils.convert(selects))
		                    ;
		 setBaseEntry(dbo);
	}
	
	
	public ObjectId getTerm() {
		return getSimpleObjecIDValue("t");
	}
	public void setTerm(ObjectId term) {
		setSimpleValue("t", term);
	}
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("si");
	}
	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("si", schoolId);
	}
	
	public ObjectId getClassId() {
		return getSimpleObjecIDValue("ci");
	}
	public void setClassId(ObjectId classId) {
		setSimpleValue("ci", classId);
	}
	
	public ObjectId getStudentId() {
		return getSimpleObjecIDValue("sti");
	}
	public void setStudentId(ObjectId studentId) {
		setSimpleValue("sti", studentId);
	}
	
	public List<ObjectId> getSelects() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ses");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(((ObjectId)o));
			}
		}
		return retList;
	}
	public void setSelects(List<ObjectId> selects) {
		setSimpleValue("ses", MongoUtils.convert(selects));
	}
	
	
	
}
