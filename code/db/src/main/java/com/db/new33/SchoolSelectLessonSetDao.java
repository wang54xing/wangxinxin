package com.db.new33;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.SchoolSelectLessonSetEntry;
import com.pojo.new33.SchoolSelectLessonSetEntry.SelectLessons;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 学校选课设置
 * @author fourer
 *
 */
public class SchoolSelectLessonSetDao extends BaseDao {

	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addSchoolSelectLessonSetEntry(SchoolSelectLessonSetEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SELECT_LESSONS_SET, e.getBaseEntry());
        return e.getID();
	}
	
	
	
	

	/**
	 * 查询
	 * @param term 学期
	 * @param schoolId
	 * @param gradeId
	 * @return
	 */
	public List<SchoolSelectLessonSetEntry> getSchoolSelectLessonSetEntrys(ObjectId term,ObjectId schoolId,ObjectId gradeId)
	{
		 List<SchoolSelectLessonSetEntry> retList = new ArrayList<SchoolSelectLessonSetEntry>();
	     BasicDBObject query = new BasicDBObject();
	     if(null!=term)
	     {
	    	 query.append("t", term);
	     }
	     if(null!=schoolId)
	     {
	    	 query.append("si", schoolId);
	     }
	     if(null!=gradeId)
	     {
	    	 query.append("gi", gradeId);
	     }
	        
	     List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SELECT_LESSONS_SET, query, Constant.FIELDS);
	     if (null != list && !list.isEmpty()) {
	            for (DBObject dbo : list) {
	                retList.add(new SchoolSelectLessonSetEntry((BasicDBObject) dbo));
	            }
	        }
	     return retList;
	}
	
	
	/**
	 * 得到选课设置
	 * @param term
	 * @param schoolId
	 * @param gradeId
	 * @return
	 */
	public Map<String, SelectLessons> getSelectLessonsMap(ObjectId term,ObjectId schoolId,ObjectId gradeId)
	{
		Map<String, SelectLessons> retMap =new HashMap<String, SchoolSelectLessonSetEntry.SelectLessons>();
		
		List<SchoolSelectLessonSetEntry> list =getSchoolSelectLessonSetEntrys(term, schoolId, gradeId);
		
		for(SchoolSelectLessonSetEntry sss:list)
		{
			for(SelectLessons sls:sss.getSets())
			{
				retMap.put(sls.getId(), sls);
			}
		}
		return retMap;
	}
	
	
	/**
	 * 开启或者关闭
	 * @param id
	 * @param selectId
	 * @param state
	 */
	public void update(ObjectId id,String selectId,int state)
	{
		 BasicDBObject query = new BasicDBObject(Constant.ID,id).append("sets.id", selectId);
		 BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sets.$.st",state));
		 update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SELECT_LESSONS_SET, query, updateValue);
	}
	public void updateNum(ObjectId id,String selectId,Integer num)
	{
		if(num < 0){
			num = null;
		}
		BasicDBObject query = new BasicDBObject(Constant.ID,id).append("sets.id", selectId);
		BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sets.$.num",num));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SELECT_LESSONS_SET, query, updateValue);
	}
	public void updateLessionSet(ObjectId id,List<SelectLessons> sets) {
		BasicDBObject query = new BasicDBObject(Constant.ID,id);
		BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sets",MongoUtils.fetchDBObjectList(sets)));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SELECT_LESSONS_SET, query, updateValue);
	}

	public void removeByTerm(ObjectId term,ObjectId schoolId,ObjectId gradeId) {
		 BasicDBObject query = new BasicDBObject();
	     if(null!=term)
	     {
	    	 query.append("t", term);
	     }
	     if(null!=schoolId)
	     {
	    	 query.append("si", schoolId);
	     }
	     if(null!=gradeId)
	     {
	    	 query.append("gi", gradeId);
	     }
	     remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SELECT_LESSONS_SET, query);
	}
	
	public void updateStartTime(ObjectId id,long start,long end) {
		 BasicDBObject query = new BasicDBObject(Constant.ID,id);
		 BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("start",start).append("end", end));
		 update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SELECT_LESSONS_SET, query, updateValue);
	}

	public void addList(List<SchoolSelectLessonSetEntry> list){
		if(list.size()!=0){
			save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SELECT_LESSONS_SET,MongoUtils.fetchDBObjectList(list));
		}
	}
}
