package com.pojo.new33.paike;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


public class N33_TimeCombineEntry extends BaseDBObject {

	private static final long serialVersionUID = 9151831693306819019L;

	public N33_TimeCombineEntry(BasicDBObject baseEntry) {
		setBaseEntry(baseEntry);
	}

	public N33_TimeCombineEntry() {
	}

	public N33_TimeCombineEntry(ObjectId schoolId, ObjectId ciId, ObjectId tagId, ObjectId gradeId,
	                    String tagName, String xkName, Integer type,List<ZuHeList> zuHeLists) {
		BasicDBObject dbObject = new BasicDBObject()
				.append("sid", schoolId)
				.append("ciId", ciId)
				.append("tagId", tagId)     //标签ID
				.append("gradeId", gradeId)
				.append("tagName", tagName) //标签名
				.append("xkName", xkName)   //标签选科名
				.append("type", type)          //类型   0  等级   1 合格
				.append("zuHeList", MongoUtils.convert(MongoUtils.fetchDBObjectList(zuHeLists)));
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
	public ObjectId getCiId() {
		return getSimpleObjecIDValue("ciId");
	}
	public void setCiId(ObjectId ciId) {
		setSimpleValue("ciId", ciId);
	}

	public ObjectId getTagId() {
		return getSimpleObjecIDValue("tagId");
	}
	public void setTagId(ObjectId tagId) {
		setSimpleValue("tagId", tagId);
	}
	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gradeId");
	}
	public void setGradeId(ObjectId gid) {
		setSimpleValue("gradeId", gid);
	}
	public String getTagName(){
		return getSimpleStringValue("tagName");
	}
	public void setTagName(String tagName){
		setSimpleValue("tagName",tagName);
	}


	public String getXkName(){
		return getSimpleStringValueDef("xkName","");
	}
	public void setXkName(String xkName){
		setSimpleValue("xkName",xkName);
	}

	public void setZuHeList(List<ZuHeList> list){
		setSimpleValue("zuHeList",MongoUtils.convert(MongoUtils.fetchDBObjectList(list)));
	}

	public List<ZuHeList> getZuHeList(){
		List<ZuHeList> retList =new ArrayList<ZuHeList>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("zuHeList");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new ZuHeList((BasicDBObject)o));
			}
		}
		return retList;
	}


	/**
	 * 内部类
	 * 虚拟班对应的标签类
	 */
	public static class ZuHeList extends BaseDBObject{
		public ZuHeList() {
		}

		public ZuHeList(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public ZuHeList(Integer serial,ObjectId jxbId,String name) {
			super();
			BasicDBObject dbo =new BasicDBObject()
					.append("serial", serial)       //组合的序号
					.append("jxbId",jxbId)
					.append("headName",name);         //组合选择的教学班
			setBaseEntry(dbo);
		}

		public String getHeadName(){
			return getSimpleStringValue("headName");
		}
		public void setHeadName(String headName){
			setSimpleValue("headName",headName);
		}

		public Integer getSerial(){
			return getSimpleIntegerValue("serial");
		}
		public void setSerial(Integer serial){
			setSimpleValue("serial",serial);
		}

		public ObjectId getJxbId(){
			return getSimpleObjecIDValue("jxbId");
		}
		public void setJxbId(ObjectId jxbId){
			setSimpleValue("jxbId",jxbId);
		}
	}
}
