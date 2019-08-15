package com.db.new33;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.pojo.new33.DayRangeEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.CourseRangeEntry;
import com.sys.constants.Constant;

public class CourseRangeDao extends BaseDao {

	public CourseRangeEntry getEntryByID(ObjectId id) {
		BasicDBObject query = new BasicDBObject("_id",id);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_COURSE_RANGE, query, Constant.FIELDS);
		if(dbo!=null) {
			return new CourseRangeEntry((BasicDBObject) dbo);
		}
		return null;
	}
	
	public List<CourseRangeEntry> getEntryListBySchoolId(ObjectId schoolId,ObjectId xqid){
		List<CourseRangeEntry> result = new ArrayList<CourseRangeEntry>();
		BasicDBObject query = new BasicDBObject("sid",schoolId).append("xqid", xqid);
		
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_COURSE_RANGE, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
		for (DBObject dbObject : dbObjects) {
			result.add(new CourseRangeEntry((BasicDBObject) dbObject));
        }
        return result;
	}

	public List<CourseRangeEntry> getEntryListBySchoolId(ObjectId schoolId,List<ObjectId> xqid){
		List<CourseRangeEntry> result = new ArrayList<CourseRangeEntry>();
		BasicDBObject query = new BasicDBObject("sid",schoolId).append("xqid", new BasicDBObject(Constant.MONGO_IN,xqid));

		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_COURSE_RANGE, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
		for (DBObject dbObject : dbObjects) {
			result.add(new CourseRangeEntry((BasicDBObject) dbObject));
		}
		return result;
	}
	public List<CourseRangeEntry> getEntryListBySchoolIdAndKJIds(List<ObjectId> kjIds,ObjectId schoolId,ObjectId xqid){
		List<CourseRangeEntry> result = new ArrayList<CourseRangeEntry>();
		BasicDBObject query = new BasicDBObject("sid",schoolId).append("xqid", xqid).append("_id", new BasicDBObject(Constant.MONGO_IN,kjIds));
		
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_COURSE_RANGE, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
		for (DBObject dbObject : dbObjects) {
			result.add(new CourseRangeEntry((BasicDBObject) dbObject));
        }
        return result;
	}

	public List<CourseRangeEntry> getEntryListBySchoolIdAndKJIdsList(List<ObjectId> kjIds,ObjectId schoolId){
		List<CourseRangeEntry> result = new ArrayList<CourseRangeEntry>();
		BasicDBObject query = new BasicDBObject("sid",schoolId).append("_id", new BasicDBObject(Constant.MONGO_IN,kjIds));

		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_COURSE_RANGE, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
		for (DBObject dbObject : dbObjects) {
			result.add(new CourseRangeEntry((BasicDBObject) dbObject));
		}
		return result;
	}
	
	public void saveEntry(CourseRangeEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_COURSE_RANGE, entry.getBaseEntry());
	}
	public void add(Collection<CourseRangeEntry> list) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_COURSE_RANGE, MongoUtils.fetchDBObjectList(list));
	}
	public void removeEntry(ObjectId id) {
		BasicDBObject query = new BasicDBObject(Constant.ID,id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_COURSE_RANGE, query);
	}
	public void removeEntryByXq(ObjectId schoolId,ObjectId xqid) {
		BasicDBObject query = new BasicDBObject("sid",schoolId).append("xqid", xqid);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_COURSE_RANGE, query);
	}

}
