package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_RemovedXY;
import com.pojo.new33.paike.N33_ZhuanXiangEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_RemovedXYDao extends BaseDao {

	public void addEntry(N33_RemovedXY entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_REMOVEDXY, entry.getBaseEntry());
	}

	public N33_RemovedXY findEntryByXYAndClassRoom(ObjectId sid,ObjectId termId,ObjectId classRoomId,Integer x,Integer y) {
		DBObject query = new BasicDBObject("sid", sid).append("termId",termId).append("classRoomId",classRoomId).append("x",x).append("y",y);
		DBObject dbObject = findOne(MongoFacroty.getAppDB(),
				Constant.COLLECTION_NEW33_REMOVEDXY, query, Constant.FIELDS);
		if (dbObject != null) {
			return new N33_RemovedXY((BasicDBObject) dbObject);
		} else {
			return null;
		}
	}

	public List<N33_RemovedXY> findEntryByTermId(ObjectId termId, ObjectId sid) {
		List<N33_RemovedXY> teaEntries = new ArrayList<N33_RemovedXY>();
		BasicDBObject query = new BasicDBObject("termId", termId).append("sid", sid);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_REMOVEDXY, query, Constant.FIELDS);
		if (dbObject != null && dbObject.size() != 0) {
			for (DBObject dbo : dbObject) {
				teaEntries.add(new N33_RemovedXY((BasicDBObject) dbo));
			}
		}
		return teaEntries;
	}

	public void deleteByTermId(ObjectId sid,ObjectId termId) {
		DBObject query = new BasicDBObject("sid", sid).append("termId",termId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_REMOVEDXY, query);
	}
}
