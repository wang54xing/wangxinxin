package com.pojo.new33;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2018/5/29.
 * 虚拟班
 * tagList
 * [
 *   tagId:标签id
 *   tagName:标签组合名+（人数）
 *   subjectList[
 *         subjectId:学科ID
 *         stuCount:学生人数
 *         stuIds:学生列表
 *         isFinish:0   未完成     1   已完成
 *   ]
 *   clsIds:标签关联的行政班列表
 * ]
 * sid:学校id
 * xqid:排课次id
 * gid:年级id
 * jxbIds:虚拟班关联的教学班
 * type 0等级    1合格
 */
public class N33_XuNiBanEntry extends BaseDBObject {

	private static final long serialVersionUID = -7761877097089584856L;

	public N33_XuNiBanEntry(BasicDBObject baseEntry) {
		setBaseEntry(baseEntry);
	}

	public N33_XuNiBanEntry() {
	}

	public N33_XuNiBanEntry(List<StudentTag> tagList, ObjectId sid, ObjectId xqid, ObjectId gid, List<ObjectId> jxbIds) {
		BasicDBObject dbObject = new BasicDBObject()
				.append("sid", sid)
				.append("xqid", xqid)
				.append("gradeId", gid)
				.append("tagList", MongoUtils.convert(MongoUtils.fetchDBObjectList(tagList)))
				.append("jxbIds",MongoUtils.convert(jxbIds))
				.append("type",0);
		setBaseEntry(dbObject);
	}


	public void setType(Integer type){
		setSimpleValue("type", type);
	}
	public Integer getType(){
		return getSimpleIntegerValue("type");
	}
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}
	public void setSchoolId(ObjectId sid) {
		setSimpleValue("sid", sid);
	}
	public ObjectId getXqId() {
		return getSimpleObjecIDValue("xqid");
	}
	public void setXqId(ObjectId xqid) {
		setSimpleValue("xqid", xqid);
	}

	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gradeId");
	}
	public void setGradeId(ObjectId gid) {
		setSimpleValue("gradeId", gid);
	}

	public void setTagList(List<StudentTag> tagList){
		List<DBObject> list = MongoUtils.fetchDBObjectList(tagList);
		setSimpleValue("tagList",  MongoUtils.convert(list));
	}

	public List<StudentTag> getTagList(){
		List<StudentTag> retList =new ArrayList<StudentTag>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("tagList");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new StudentTag((BasicDBObject)o));
			}
		}
		return retList;
	}

	public void setJxbIds(List<ObjectId> jxbIds){
		setSimpleValue("jxbIds",MongoUtils.convert(jxbIds));
	}
	public List<ObjectId> getJxbIds(){
		List<ObjectId> result = new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("jxbIds");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				result.add((ObjectId)o);
			}
		}
		return result;
	}

	/**
	 * 内部类
	 * 虚拟班对应的标签类
	 */
	public static class StudentTag extends BaseDBObject{
		public StudentTag() {
		}

		public StudentTag(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public StudentTag(ObjectId tagId,String tagName,String bqName,List<SubjectInfo> subjectInfos,List<ObjectId> clsIds,Integer count) {
			super();
			BasicDBObject dbo =new BasicDBObject()
					.append("tagId", tagId)
					.append("tagCount",count)
					.append("tagName", tagName)
					.append("bqName",bqName)
					.append("subjectList", MongoUtils.convert(MongoUtils.fetchDBObjectList(subjectInfos)))
					.append("clsIds",MongoUtils.convert(clsIds));
			setBaseEntry(dbo);
		}

		public Integer getTagCount(){
			return getSimpleIntegerValueDef("tagCount",0);
		}
		public void setTagCount(Integer tagCount){
			setSimpleValue("tagCount",tagCount);
		}

		public ObjectId getTagId(){
			return getSimpleObjecIDValue("tagId");
		}
		public void setTagId(ObjectId tagId){
			setSimpleValue("tagId",tagId);
		}

		public String getTagName(){
			return getSimpleStringValue("tagName");
		}
		public void setTagName(String tagName){
			setSimpleValue("tagName",tagName);
		}


		public String getBqName(){
			return getSimpleStringValueDef("bqName","");
		}
		public void setBqName(String tagName){
			setSimpleValue("bqName",tagName);
		}
		public void setSubjectList(List<SubjectInfo> list){
			setSimpleValue("subjectList",MongoUtils.convert(MongoUtils.fetchDBObjectList(list)));
		}

		public List<SubjectInfo> getSubjectList(){
			List<SubjectInfo> retList =new ArrayList<SubjectInfo>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("subjectList");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					retList.add(new SubjectInfo((BasicDBObject)o));
				}
			}
			return retList;
		}

		public void setClsIds(List<ObjectId> clsIds){
			setSimpleValue("clsIds",MongoUtils.convert(clsIds));
		}
		public List<ObjectId> getClsIds(){
			List<ObjectId> result = new ArrayList<ObjectId>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("clsIds");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					result.add((ObjectId)o);
				}
			}
			return result;
		}
	}

	/**
	 *  内部类
	 *  虚拟班对应标签类下的学科信息类
	 */
	public static class SubjectInfo extends BaseDBObject{
		public SubjectInfo() {
		}

		public SubjectInfo(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public SubjectInfo(ObjectId subId,Integer stuCount,List<ObjectId> stuIds,Integer isFinish) {
			BasicDBObject dbObject = new BasicDBObject()
					.append("subId", subId)
					.append("stuCount", stuCount)
					.append("stuIds", MongoUtils.convert(stuIds))
					.append("isFinish",isFinish);
			setBaseEntry(dbObject);
		}

		public Integer getIsFinish(){
			return getSimpleIntegerValue("isFinish");
		}
		public void setIsFinish(Integer isFinish){
			setSimpleValue("isFinish",isFinish);
		}
		public ObjectId getSubId() {
			return getSimpleObjecIDValue("subId");
		}
		public void setSubId(ObjectId subId) {
			setSimpleValue("subId", subId);
		}
		public void setStuCount(Integer stuCount){
			setSimpleValue("stuCount", stuCount);
		}
		public Integer getStuCount(){
			return getSimpleIntegerValue("stuCount");
		}
		public void setStuIds(List<ObjectId> stuIds){
			setSimpleValue("stuIds",MongoUtils.convert(stuIds));
		}
		public List<ObjectId> getStuIds(){
			List<ObjectId> result = new ArrayList<ObjectId>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("stuIds");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					result.add((ObjectId)o);
				}
			}
			return result;
		}
	}
}
