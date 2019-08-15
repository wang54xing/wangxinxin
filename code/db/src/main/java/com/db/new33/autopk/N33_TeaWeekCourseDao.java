package com.db.new33.autopk;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.TeaWeekCourseEntry;
import com.pojo.autoPK.TeaWeekCourseEntry;
import com.pojo.new33.N33_XuNiBanEntry;
import com.sys.constants.Constant;

public class N33_TeaWeekCourseDao extends BaseDao {
	
	public void addEntry(TeaWeekCourseEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAWEEKCOURSE, entry.getBaseEntry());
	}
	
	public TeaWeekCourseEntry getEntryByGWC(ObjectId schoolId,ObjectId gradeId,ObjectId ciId,int weekCourse) {
		TeaWeekCourseEntry TeaWeekCourseEntry = new TeaWeekCourseEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append("gradeId", gradeId)
				.append("weekCourse", weekCourse).append("ciId", ciId);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAWEEKCOURSE, query, Constant.FIELDS);
		if(dbo!=null) {
			TeaWeekCourseEntry = new TeaWeekCourseEntry((BasicDBObject)dbo);
		}
		return TeaWeekCourseEntry;
	}
	
	public TeaWeekCourseEntry getEntryById(ObjectId schoolId,ObjectId id) {
		TeaWeekCourseEntry TeaWeekCourseEntry = new TeaWeekCourseEntry();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId).append(Constant.ID, id);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAWEEKCOURSE, query, Constant.FIELDS);
		if(dbo!=null) {
			TeaWeekCourseEntry = new TeaWeekCourseEntry((BasicDBObject)dbo);
		}
		return TeaWeekCourseEntry;
	}
	
	public void updateEntry(TeaWeekCourseEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAWEEKCOURSE, query, updateValue);
    }
	
	public List<TeaWeekCourseEntry> getEntryByGId(ObjectId schoolId,ObjectId gradeId) {
		List<TeaWeekCourseEntry> teaWeekList = new ArrayList<TeaWeekCourseEntry>();
		BasicDBObject query = new BasicDBObject("schoolId", schoolId);
		query.append("gradeId", gradeId);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEAWEEKCOURSE, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				teaWeekList.add(new TeaWeekCourseEntry((BasicDBObject) dbo));
			}
		}
		return teaWeekList;
	}
}
