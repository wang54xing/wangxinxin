package com.db.new33.autopk;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.AutoOtherSetEntry;
import com.pojo.new33.N33_ZjkjEntry;
import com.pojo.new33.isolate.N33_JXZEntry;
import com.sys.constants.Constant;

public class N33_AutoOtherSetDao extends BaseDao {
	
	public void addEntry(AutoOtherSetEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AUTOOTHERSET, entry.getBaseEntry());
	}
	
	public AutoOtherSetEntry getEntryById(ObjectId schoolId,ObjectId id) {
		AutoOtherSetEntry AutoOtherSetEntry = new AutoOtherSetEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append(Constant.ID, id);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AUTOOTHERSET, query, Constant.FIELDS);
		if(dbo!=null) {
			AutoOtherSetEntry = new AutoOtherSetEntry((BasicDBObject)dbo);
		}
		return AutoOtherSetEntry;
	}
	
	public void updateEntry(AutoOtherSetEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AUTOOTHERSET, query, updateValue);
    }
	
	public AutoOtherSetEntry getEntryByGId(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		AutoOtherSetEntry AutoOtherSetEntry = new AutoOtherSetEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append("gradeId", gradeId).append("ciId", ciId);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AUTOOTHERSET, query, Constant.FIELDS);
		if(dbo!=null) {
			AutoOtherSetEntry = new AutoOtherSetEntry((BasicDBObject)dbo);
		}
		return AutoOtherSetEntry;
	}
}
