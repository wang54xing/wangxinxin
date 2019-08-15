package com.db.new33;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.DayRangeEntry;
import com.sys.constants.Constant;

/**
 * @author rick
 *
 */
public class DayRangeDao extends BaseDao {
	
	public DayRangeEntry getEntryBySchoolId(ObjectId schoolId) {
		 BasicDBObject query = new BasicDBObject("sid",schoolId);
		 DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_DAY_RANGE, query, Constant.FIELDS);
		 if(dbo!=null) {
			 return new DayRangeEntry((BasicDBObject) dbo);
		 }
		 return null;
	}
	public void removeEntry(ObjectId id) {
		BasicDBObject query = new BasicDBObject(Constant.ID,id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_DAY_RANGE, query);
	}
	public void addEntry(DayRangeEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_DAY_RANGE, entry.getBaseEntry());
	}
	
}
