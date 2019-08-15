package com.db.new33.autopk;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.AutoPkGzSetEntry;
import com.pojo.autoPK.AutoPkGzSetEntry;
import com.pojo.autoPK.TeaWeekCourseEntry;
import com.sys.constants.Constant;

public class N33_PkGzDao extends BaseDao {
	
	public void addEntry(AutoPkGzSetEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PKGZ, entry.getBaseEntry());
	}
	
	 public void removeById(ObjectId id) {
	        
	    }
	
	
	public void removeEntry(AutoPkGzSetEntry entry) {
		BasicDBObject query = new BasicDBObject(Constant.ID, entry.getID());
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PKGZ, query);
	}
	
	public AutoPkGzSetEntry getEntryById(ObjectId schoolId, ObjectId gradeId, ObjectId ciId,String x, String y) {
		AutoPkGzSetEntry AutoPkGzSetEntry = new AutoPkGzSetEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId);
		query.append("gradeId", gradeId).append("ciId", ciId).append("x", x).append("y", y);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PKGZ, query, Constant.FIELDS);
		if(dbo!=null) {
			AutoPkGzSetEntry = new AutoPkGzSetEntry((BasicDBObject)dbo);
		}
		return AutoPkGzSetEntry;
	}
	
	public void updateEntry(AutoPkGzSetEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PKGZ, query, updateValue);
    }
	
	public List<AutoPkGzSetEntry> getEntryByGId(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		List<AutoPkGzSetEntry> teaDayList = new ArrayList<AutoPkGzSetEntry>();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId);
		query.append("gradeId", gradeId);
		query.append("ciId", ciId);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PKGZ, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				teaDayList.add(new AutoPkGzSetEntry((BasicDBObject) dbo));
			}
		}
		return teaDayList;
	}
	
	public AutoPkGzSetEntry getEntryByCId(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int courseNum) {
		AutoPkGzSetEntry AutoPkGzSetEntry = new AutoPkGzSetEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append("gradeId", gradeId).append("ciId", ciId);
		query.append("courseNum", courseNum);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PKGZ, query, Constant.FIELDS);
		if(dbo!=null) {
			AutoPkGzSetEntry = new AutoPkGzSetEntry((BasicDBObject)dbo);
		}
		return AutoPkGzSetEntry;
	}
	
}
