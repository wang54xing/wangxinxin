package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_Count;
import com.sys.constants.Constant;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

public class N33_CountDao extends BaseDao {

	public void addCountEntry(N33_Count entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_COUNT, entry.getBaseEntry());
	}

	public void updateCountEntry(N33_Count count) {
		DBObject query = new BasicDBObject("_id",count.getID());
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, count.getBaseEntry());
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_COUNT, query, updateValue);
	}

	public N33_Count findCountEntryByWeek(ObjectId sid,ObjectId xqid,Integer week,Integer tk) {
		BasicDBObject query = new BasicDBObject("sid", sid).append("xqId",xqid).append("week",week).append("tk",tk);
		DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_COUNT, query, Constant.FIELDS);
		if (dbObject != null) {
			return new N33_Count((BasicDBObject) dbObject);
		} else {
			return null;
		}
	}

}
