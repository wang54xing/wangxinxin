package com.db.new33.isolate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_DaKaEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_DaKaDao extends BaseDao {
	public void addDaKaEntry(N33_DaKaEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_DAKA, entry.getBaseEntry());
	}


	public List<N33_DaKaEntry> findDaKaEntries(ObjectId zkbId) {
		List<N33_DaKaEntry> entries = new ArrayList<N33_DaKaEntry>();
		BasicDBObject query = new BasicDBObject().append("zkbId", zkbId);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_DAKA, query, Constant.FIELDS);
		for (DBObject dbo : dbObject) {
			N33_DaKaEntry entry = new N33_DaKaEntry((BasicDBObject) dbo);
			entries.add(entry);
		}
		return entries;
	}
}
