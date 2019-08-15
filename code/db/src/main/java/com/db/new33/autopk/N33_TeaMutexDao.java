package com.db.new33.autopk;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.TeaMutexEntry;
import com.pojo.autoPK.TeaMutexEntry;
import com.pojo.new33.CourseRangeEntry;
import com.sys.constants.Constant;

public class N33_TeaMutexDao extends BaseDao {
	
	public void addEntry(TeaMutexEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAMUTEX, entry.getBaseEntry());
	}
	
	public TeaMutexEntry getEntryById(ObjectId schoolId,ObjectId id) {
		TeaMutexEntry TeaMutexEntry = new TeaMutexEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append(Constant.ID, id);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAMUTEX, query, Constant.FIELDS);
		if(dbo!=null) {
			TeaMutexEntry = new TeaMutexEntry((BasicDBObject)dbo);
		}
		return TeaMutexEntry;
	}
	
	public void updateEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updVal = new BasicDBObject("ir", "1");
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updVal);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAMUTEX, query, updateValue);
    }
	
	public TeaMutexEntry getEntryByGId(ObjectId schoolId,ObjectId gradeId) {
		TeaMutexEntry TeaMutexEntry = new TeaMutexEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append(Constant.ID, gradeId);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAMUTEX, query, Constant.FIELDS);
		if(dbo!=null) {
			TeaMutexEntry = new TeaMutexEntry((BasicDBObject)dbo);
		}
		return TeaMutexEntry;
	}
	
	public int getEntryList(ObjectId schoolId,ObjectId gradeId, ObjectId ciId){
		List<TeaMutexEntry> result = new ArrayList<TeaMutexEntry>();
		BasicDBObject query = new BasicDBObject("schoolId",schoolId).append("gradeId", gradeId).append("ciId", ciId).append("ir", "0");
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAMUTEX, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
		for (DBObject dbObject : dbObjects) {
			result.add(new TeaMutexEntry((BasicDBObject) dbObject));
        }
        return result.size();
	}
	
	public List<TeaMutexEntry> getEntrys(ObjectId schoolId, ObjectId gradeId, ObjectId ciId){
		List<TeaMutexEntry> result = new ArrayList<TeaMutexEntry>();
		BasicDBObject query = new BasicDBObject("schoolId",schoolId).append("gradeId", gradeId).append("ciId", ciId).append("ir", "0");
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAMUTEX, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
		for (DBObject dbObject : dbObjects) {
			result.add(new TeaMutexEntry((BasicDBObject) dbObject));
        }
        return result;
	}
}
