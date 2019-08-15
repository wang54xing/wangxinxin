package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 班级信息
 * <pre>
 * collectionName:classes
 * </pre>
 * <pre>
 * {
 *  sid:学校ID
 *  gid:年级ID
 *  nm:名字
 *  int:简介
 *  ts:总的学生数目
 *  ma:班主任
 *  teas[]:班级老师
 *  stus[]:班级学生 
 *  sys:是否需要同步
 *  ir:是否删除 0没有删除 1已经删除
 *  lt:最后更新时间
 *  by:毕业年份
 * }
 * </pre>
 * @author fourer
 */
public class ClassEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6485398291488814841L;
	
	public ClassEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public ClassEntry(ObjectId schoolId, ObjectId gradeId, String name,
		 ObjectId masterID,int totalStudent) {
		this(schoolId,
				gradeId,
				name,
				Constant.EMPTY,
                masterID,
				new ArrayList<ObjectId>(), //teachers
				new ArrayList<ObjectId>(),  //students
				totalStudent
			);
	}
	
	
	public ClassEntry(ObjectId schoolId, ObjectId gradeId, String name,
			String introduce, ObjectId masterID, List<ObjectId> teachers,
			List<ObjectId> students,int totalStudent) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("sid", schoolId)
		.append("gid", gradeId)
		.append("nm", name)
		.append("int", introduce)
		.append("ma", masterID)
		.append("teas", MongoUtils.convert(teachers))
		.append("stus", MongoUtils.convert(students))
		.append(Constant.FIELD_SYN, Constant.SYN_YES_NEED)
		.append("ts", totalStudent)
		.append("ir", Constant.ZERO);
		;
		setBaseEntry(dbo);
	}
	//默认没有删除
	public int getIsRemove() {
		if(getBaseEntry().containsField("ir"))
		{
		  return getSimpleIntegerValue("ir");
		}
		return Constant.ZERO;
	}
	public void setIsRemove(int isRemove) {
		setSimpleValue("ir", isRemove);
	}
	
	
	public int getTotalStudent() {
		return getSimpleIntegerValue("ts");
	}

	public void setTotalStudent(int totalStudent) {
		setSimpleValue("ts", totalStudent);
	}

	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
				
	}
	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("sid", schoolId);
	}
	
	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gid");
	}
	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gid", gradeId);
	}
	
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	
	
	
	
	
	public String getYear() {
		return getSimpleStringValue("by");
	}
	public void setYear(String name) {
		setSimpleValue("by", name);
	}
	
	
	
	
	public String getIntroduce() {
		return getSimpleStringValue("int");
	}
	public void setIntroduce(String introduce) {
		setSimpleValue("int", introduce);
	}

	public ObjectId getMaster() {
		return getSimpleObjecIDValue("ma");
	}
	public void setMaster(ObjectId masterId) {
		setSimpleValue("ma", masterId);
	}
	public List<ObjectId> getTeachers() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("teas");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setTeachers(List<ObjectId> teachers) {
		setSimpleValue("teas", MongoUtils.convert(teachers));
	}
	public List<ObjectId> getStudents() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("stus");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	
	public void setStudents(List<ObjectId> students) {
		setSimpleValue("stus", MongoUtils.convert(students));
	}
}
