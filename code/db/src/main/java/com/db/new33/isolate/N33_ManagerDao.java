package com.db.new33.isolate;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.N33_ManagerEntry;
import com.sys.constants.Constant;

public class N33_ManagerDao extends BaseDao{

	public void addManager(N33_ManagerEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Manager, entry.getBaseEntry());
    }
	
	/**
	 * 删除管理员
	 * @param id
	 */
	public void deleteManager(ObjectId id) {
		DBObject query = new BasicDBObject().append(Constant.ID, id);
		remove(MongoFacroty.getAppDB(),
				Constant.COLLECTION_NEW33_ISOLATE_Manager, query);
	}

	public N33_ManagerEntry getEntry(ObjectId sid,ObjectId userId) {
		BasicDBObject query = new BasicDBObject("userId", userId).append("sid",sid);
		DBObject dbObject = findOne(MongoFacroty.getAppDB(),
				Constant.COLLECTION_NEW33_ISOLATE_Manager, query, Constant.FIELDS);
		if (dbObject != null) {
			return new N33_ManagerEntry((BasicDBObject) dbObject);
		} else {
			return null;
		}
	}
	/**
	 * 查询管理员
	 * @param sid
	 * @return
	 */
	public List<N33_ManagerEntry> findAllManagerList(ObjectId sid) {
		List<N33_ManagerEntry> entries = new ArrayList<N33_ManagerEntry>();
		BasicDBObject query = new BasicDBObject("sid", sid);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
				Constant.COLLECTION_NEW33_ISOLATE_Manager, query, Constant.FIELDS);
		for (DBObject dbo : dbObject) {
			N33_ManagerEntry entry = new N33_ManagerEntry((BasicDBObject) dbo);
			entries.add(entry);
		}
		return entries;
	}

}
