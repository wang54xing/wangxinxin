package com.db.new33.autopk;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.TeaStepEntry;
import com.pojo.autoPK.TeaStepEntry;
import com.sys.constants.Constant;

public class N33_TeaStepDao extends BaseDao {
	
	public void addEntry(TeaStepEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEASTEP, entry.getBaseEntry());
	}
	
	public TeaStepEntry getEntryById(ObjectId schoolId,ObjectId id) {
		TeaStepEntry TeaStepEntry = new TeaStepEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append(Constant.ID, id);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEASTEP, query, Constant.FIELDS);
		if(dbo!=null) {
			TeaStepEntry = new TeaStepEntry((BasicDBObject)dbo);
		}
		return TeaStepEntry;
	}
	
	public void updateEntry(TeaStepEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEASTEP, query, updateValue);
    }
	
	public TeaStepEntry getEntryByGId(ObjectId schoolId,ObjectId gradeId, ObjectId ciId) {
		TeaStepEntry TeaStepEntry = new TeaStepEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append("gradeId", gradeId).append("ciId", ciId);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEASTEP, query, Constant.FIELDS);
		if(dbo!=null) {
			TeaStepEntry = new TeaStepEntry((BasicDBObject)dbo);
		}
		return TeaStepEntry;
	}
}
