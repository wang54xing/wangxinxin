package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N33_TurnOffDao extends BaseDao {

	/**
	 * 保存开关
	 *
	 * @param entry
	 */
	public void addTurnOff(N33_TurnOff entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TURNOFF, entry.getBaseEntry());
	}

	/**
	 * 更新一条开关
	 *
	 * @param entry
	 */
	public void updateTurnOff(N33_TurnOff entry) {
		DBObject query = new BasicDBObject(Constant.ID, entry.getID());
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TURNOFF, query, updateValue);
	}

	/**
	 *
	 * @return
	 */
	public N33_TurnOff getTurnOffBySidAndCiIdAndGradeId(ObjectId sid, ObjectId gradeId, ObjectId ciId, Integer type) {
		BasicDBObject query = new BasicDBObject("sid", sid).append("gid",gradeId).append("ciId",ciId).append("type", type);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TURNOFF, query, Constant.FIELDS);
		if (null != dbo) {
			return new N33_TurnOff((BasicDBObject) dbo);
		}
		return null;
	}

	/**
	 *所有设置查询
	 * @return
	 */
	public List<ObjectId> getAllSet(ObjectId schoolId, ObjectId ciId, Integer type) {
		BasicDBObject query = new BasicDBObject("sid", schoolId).append("ciId",ciId).append("type",type);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TURNOFF, query, Constant.FIELDS);
		List<ObjectId> openIds = new ArrayList<ObjectId>();
		if (null != dbObject && !dbObject.isEmpty()) {
			for (DBObject dbo : dbObject) {
				N33_TurnOff ns = new N33_TurnOff((BasicDBObject) dbo);
				if(ns.getAcClass()==1){
					openIds.add(ns.getGradeId());
				}
			}
		}
		return openIds;
	}

	/**
	 *
	 * @return
	 */
	public N33_TurnOff getTurnOffBySidAndCiIdAndGradeId(ObjectId sid, ObjectId gradeId, ObjectId ciId, Integer type, Integer kbType) {
		BasicDBObject query = new BasicDBObject("sid", sid).append("gid",gradeId).append("ciId",ciId).append("type",type).append("kbType",kbType);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TURNOFF, query, Constant.FIELDS);
		if (null != dbo) {
			return new N33_TurnOff((BasicDBObject) dbo);
		}
		return null;
	}

	/**
	 *
	 * @param schoolId
	 * @param ciId
	 * @param type
	 * @return
	 */
    public Map<ObjectId, N33_TurnOff> getGradeTurnOffMap(ObjectId schoolId, ObjectId ciId, int type) {
		Map<ObjectId, N33_TurnOff> result = new HashMap<ObjectId, N33_TurnOff>();
		BasicDBObject query = new BasicDBObject("sid", schoolId).append("ciId",ciId).append("type",type);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TURNOFF, query, Constant.FIELDS);
		for (DBObject dbo : dblist) {
			N33_TurnOff turnOff = new N33_TurnOff((BasicDBObject) dbo);
			result.put(turnOff.getGradeId(), turnOff);
		}
		return result;
    }
}
