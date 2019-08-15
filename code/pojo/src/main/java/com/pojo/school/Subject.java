package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 科目信息，依附于SchoolEntry
 * <pre>
 * {
         si:科目ID
 *       nm:名字
 *       ty:类型
 *       gis:年级ID
 *       [
 *       
 *       ]
 *       uis:学科组长
 *       [
 *       ]
 * }
 * </pre>
 * @author fourer
 */
public class Subject extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8034598588438206559L;

	
	
	
	public Subject(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}
	public Subject(String name, ObjectId gradeId) {
		super();
		BasicDBList list =new BasicDBList();
		list.add(gradeId);
		BasicDBList list2 =new BasicDBList();
		BasicDBObject dbo =new BasicDBObject()
		.append("si", new ObjectId())
		.append("nm", name)
		.append("gis", list)
		.append("uis", list2);
		setBaseEntry(dbo);
	}
	public ObjectId getSubjectId() {
		return getSimpleObjecIDValue("si");
	}
	public void setSubjectId(ObjectId subjectId) {
		setSimpleValue("si", subjectId);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	
	
	public List<ObjectId> getGradeIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
	    BasicDBList list =(BasicDBList)getSimpleObjectValue("gis");
	    if(null!=list && !list.isEmpty())
	    {
	        for(Object o:list)
	        {
	             retList.add((ObjectId)o);
	        }
	     }
	    return retList;
	}
	public void setGradeIds(List<ObjectId> gradeIds) {
		setSimpleValue("gis", MongoUtils.convert(gradeIds));
	}

	public int getSubjectType() {
		return getSimpleIntegerValueDef("ty",-1);
	}
	public void setSubjectType(int gradeType) {
		setSimpleValue("ty", gradeType);
	}
	public List<ObjectId> getUserIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("uis");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setUserIds(List<ObjectId> userIds) {
		setSimpleValue("uis", MongoUtils.convert(userIds));
	}


}
