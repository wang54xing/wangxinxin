package com.db.new33.autopk;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.AutoTeaSetEntry;
import com.pojo.new33.N33_ZjkjEntry;
import com.pojo.new33.isolate.N33_JXZEntry;
import com.sys.constants.Constant;

public class N33_AutoTeaSetDao extends BaseDao {
	
	public void addEntry(AutoTeaSetEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AUTOTEASET, entry.getBaseEntry());
	}
	
	public AutoTeaSetEntry getEntryById(ObjectId schoolId,ObjectId id) {
		AutoTeaSetEntry autoTeaSetEntry = new AutoTeaSetEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append(Constant.ID, id);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AUTOTEASET, query, Constant.FIELDS);
		if(dbo!=null) {
			autoTeaSetEntry = new AutoTeaSetEntry((BasicDBObject)dbo);
		}
		return autoTeaSetEntry;
	}
	
	public void updateEntry(AutoTeaSetEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AUTOTEASET, query, updateValue);
    }
	
	public AutoTeaSetEntry getEntryByGId(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		AutoTeaSetEntry autoTeaSetEntry = new AutoTeaSetEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append("gradeId", gradeId).append("ciId", ciId);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AUTOTEASET, query, Constant.FIELDS);
		if(dbo!=null) {
			autoTeaSetEntry = new AutoTeaSetEntry((BasicDBObject)dbo);
		}
		return autoTeaSetEntry;
	}
}
