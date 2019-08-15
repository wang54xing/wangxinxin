package com.db.new33.autopk;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.OSubCheckEntry;
import com.pojo.autoPK.OSubCheckEntry;
import com.pojo.autoPK.TeaWeekCourseEntry;
import com.sys.constants.Constant;

public class N33_OSubCheckDao extends BaseDao {
	
	public void addEntry(OSubCheckEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_OSUBCHECK, entry.getBaseEntry());
	}
	
	public OSubCheckEntry getEntryById(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		OSubCheckEntry OSubCheckEntry = new OSubCheckEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId);
		query.append("gradeId", gradeId);
		query.append("ciId", ciId);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_OSUBCHECK, query, Constant.FIELDS);
		if(dbo!=null) {
			OSubCheckEntry = new OSubCheckEntry((BasicDBObject)dbo);
		}
		return OSubCheckEntry;
	}
	
	public void updateEntry(OSubCheckEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_OSUBCHECK, query, updateValue);
    }
	
	public List<OSubCheckEntry> getEntryByGId(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		List<OSubCheckEntry> teaDayList = new ArrayList<OSubCheckEntry>();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId);
		query.append("gradeId", gradeId);
		query.append("ciId", ciId);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAWEEKCOURSE, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				teaDayList.add(new OSubCheckEntry((BasicDBObject) dbo));
			}
		}
		return teaDayList;
	}
	
	public OSubCheckEntry getEntryByCId(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int courseNum) {
		OSubCheckEntry OSubCheckEntry = new OSubCheckEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append("gradeId", gradeId).append("ciId", ciId);
		query.append("courseNum", courseNum);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_OSUBCHECK, query, Constant.FIELDS);
		if(dbo!=null) {
			OSubCheckEntry = new OSubCheckEntry((BasicDBObject)dbo);
		}
		return OSubCheckEntry;
	}
	
}
