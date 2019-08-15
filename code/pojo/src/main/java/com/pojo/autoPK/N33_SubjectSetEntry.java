package com.pojo.autoPK;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_SubjectSetEntry extends BaseDBObject {
	private static final long serialVersionUID = 6896593420512225891L;

	public N33_SubjectSetEntry() {}

	public N33_SubjectSetEntry(DBObject dbo) {
		this((BasicDBObject) dbo);
	}

	public N33_SubjectSetEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public N33_SubjectSetEntry(ObjectId ciId, ObjectId schoolId, ObjectId gradeId, boolean flag, Integer AMCount,Integer PMCount, Integer type, List<ObjectId> subIds,List<MutexSubject> mutexSubjects) {
		super();
		BasicDBObject dbo = new BasicDBObject()
				.append("sid", schoolId)
				.append("ciId", ciId)
				.append("gradeId", gradeId)
				.append("type", type)  //规则类型（1.学科进度一致    2.周课时平均,日课时集中    3.上下午课时分配   4.学科互斥（不在同一天上课）
				.append("flag", flag) // true  启动规则     false   关闭规则
				.append("amcount", AMCount)    //数量上限  如果类型是3，才有该字段
				.append("pmcount",PMCount)     //数量上限  如果类型是3，才有该字段
				.append("subIds",MongoUtils.convert(subIds))
				.append("mutexSubjects",MongoUtils.convert(MongoUtils.fetchDBObjectList(mutexSubjects)));
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

	public Integer getAMCount() {
		return getSimpleIntegerValueDef("amcount",0);
	}

	public void setAMCount(Integer amcount) {
		setSimpleValue("amcount", amcount);
	}

	public Integer getPMCount() {
		return getSimpleIntegerValueDef("pmcount",0);
	}

	public void setPMCount(Integer pmcount) {
		setSimpleValue("pmcount", pmcount);
	}
	public void setMutexSubjects(List<MutexSubject> mutexSubjects){
		List<DBObject> list = MongoUtils.fetchDBObjectList(mutexSubjects);
		setSimpleValue("mutexSubjects",  MongoUtils.convert(list));
	}

	public List<MutexSubject> getMutexSubjects(){
		List<MutexSubject> retList =new ArrayList<MutexSubject>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("mutexSubjects");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new MutexSubject((BasicDBObject)o));
			}
		}
		return retList;
	}

	public List<ObjectId> getSubIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("subIds");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(((ObjectId)o));
			}
		}
		return retList;
	}

	public void setSubIds(List<ObjectId> subIds) {
		setSimpleValue("subIds", MongoUtils.convert(subIds));
	}


	/**
	 * 互斥学科
	 */
	public static class MutexSubject extends BaseDBObject{
		public MutexSubject() {
		}

		public MutexSubject(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public MutexSubject(ObjectId id,ObjectId subjectId1,ObjectId subjectId2) {
			BasicDBObject dbObject = new BasicDBObject()
					.append("_id",id)
					.append("subjectId1", subjectId1)
					.append("subjectId2", subjectId2);
			setBaseEntry(dbObject);
		}

		public ObjectId getId() {
			return getSimpleObjecIDValue("_id");
		}

		public void setId(ObjectId _id) {
			setSimpleValue("_id", _id);
		}

		public ObjectId getSubjectId1() {
			return getSimpleObjecIDValue("subjectId1");
		}

		public void setSubjectId1(ObjectId subjectId1) {
			setSimpleValue("subjectId1", subjectId1);
		}

		public ObjectId getSubjectId2() {
			return getSimpleObjecIDValue("subjectId2");
		}

		public void setSubjectId2(ObjectId subjectId2) {
			setSimpleValue("subjectId2", subjectId2);
		}
	}
}
