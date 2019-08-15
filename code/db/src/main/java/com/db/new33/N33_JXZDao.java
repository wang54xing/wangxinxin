package com.db.new33;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.N33_JXZEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

public class N33_JXZDao extends BaseDao {

	public List<N33_JXZEntry> getListByXq(ObjectId schoolId,ObjectId xqid){
		List<N33_JXZEntry> result = new ArrayList<N33_JXZEntry>();
		BasicDBObject query = new BasicDBObject("sid", schoolId).append("xqid", xqid);
		List<DBObject> dbObjects =find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_WEEK, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
		for (DBObject dbObject : dbObjects) {
			result.add(new N33_JXZEntry((BasicDBObject) dbObject));
        }
        return result;
	}
	
	/**
	 * 批量保存
	 * @param rs
	 */
	public void add(List<N33_JXZEntry> rs) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(rs);
		 if(null!=list && !list.isEmpty())
		 {
			 save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_WEEK, list);
		 }
	}
	
	public N33_JXZEntry getJXZByDate(ObjectId schoolId,ObjectId xqid,long date) {
		BasicDBObject query = new BasicDBObject("sid", schoolId).append("xqid", xqid)
				.append("start", new BasicDBObject(Constant.MONGO_LTE,date))
				.append("end", new BasicDBObject(Constant.MONGO_GTE,date));
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_WEEK, query, Constant.FIELDS);
		if(dbo!=null) {
			return new N33_JXZEntry((BasicDBObject)dbo);
		}
		return null;
	}
	public void removeEntryList(ObjectId schoolId,ObjectId xqid) {
		BasicDBObject query = new BasicDBObject("sid", schoolId).append("xqid", xqid);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_WEEK, query);
	}

	public N33_JXZEntry getJXZBySerial(ObjectId schoolId,ObjectId xqid,int index) {
		BasicDBObject query = new BasicDBObject("sid", schoolId).append("xqid", xqid)
				.append("serial", index);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_WEEK, query, Constant.FIELDS);
		if(dbo!=null) {
			return new N33_JXZEntry((BasicDBObject)dbo);
		}
		return null;
	}
}
