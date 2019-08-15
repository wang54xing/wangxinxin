package com.db.new33.autopk;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.TeaDayEntry;
import com.pojo.autoPK.TeaDayEntry;
import com.pojo.autoPK.TeaWeekCourseEntry;
import com.sys.constants.Constant;

public class N33_TeaDayDao extends BaseDao {
	
	public void addEntry(TeaDayEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEADAY, entry.getBaseEntry());
	}
	
	public TeaDayEntry getEntryById(ObjectId schoolId,ObjectId id) {
		TeaDayEntry TeaDayEntry = new TeaDayEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append(Constant.ID, id);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEADAY, query, Constant.FIELDS);
		if(dbo!=null) {
			TeaDayEntry = new TeaDayEntry((BasicDBObject)dbo);
		}
		return TeaDayEntry;
	}
	
	public void updateEntry(TeaDayEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEADAY, query, updateValue);
    }
	
	public List<TeaDayEntry> getEntryByGId(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		List<TeaDayEntry> teaDayList = new ArrayList<TeaDayEntry>();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId);
		query.append("gradeId", gradeId);
		query.append("ciId", ciId);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEADAY, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				teaDayList.add(new TeaDayEntry((BasicDBObject) dbo));
			}
		}
		return teaDayList;
	}
	
	public TeaDayEntry getEntryByCId(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int courseNum) {
		TeaDayEntry TeaDayEntry = new TeaDayEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append("gradeId", gradeId).append("ciId", ciId);
		query.append("courseNum", courseNum);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEADAY, query, Constant.FIELDS);
		if(dbo!=null) {
			TeaDayEntry = new TeaDayEntry((BasicDBObject)dbo);
		}
		return TeaDayEntry;
	}
	
}
