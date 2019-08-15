package com.db.new33.isolate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_SysnEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N33_SysnEntryDao extends BaseDao {

	public N33_SysnEntry getSysnEntry(ObjectId ciId,ObjectId oId,ObjectId sid) {
		BasicDBObject query = new BasicDBObject("ciId",ciId).append("oId",oId).append("sid",sid);
		DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SYSN, query, Constant.FIELDS);
		if (dbObject != null) {
			return new N33_SysnEntry((BasicDBObject) dbObject);
		}
		return null;
	}

	public void saveSysnEntry(N33_SysnEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SYSN, entry.getBaseEntry());
	}

	/**
	 *
	 * @param ciId
	 * @param sid
     * @return
     */
	public Map<ObjectId,ObjectId> getSysnEntry(ObjectId ciId, ObjectId sid) {
		Map<ObjectId,ObjectId> idMaps = new HashMap<ObjectId, ObjectId>();
		BasicDBObject query = new BasicDBObject("ciId",ciId).append("sid",sid);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SYSN, query, Constant.FIELDS);
		for(DBObject dbo:dblist) {
			N33_SysnEntry entry = new N33_SysnEntry((BasicDBObject) dbo);
			idMaps.put(entry.getOId(),entry.getNId());
		}
		return idMaps;
	}

}
