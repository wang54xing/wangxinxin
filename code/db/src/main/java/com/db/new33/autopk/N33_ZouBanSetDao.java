package com.db.new33.autopk;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.N33_SubjectSetEntry;
import com.pojo.autoPK.N33_ZouBanSetEntry;
import com.pojo.new33.N33_XuNiBanEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_ZouBanSetDao extends BaseDao {
	/**
	 * 保存自动排课的走班设置
	 *
	 * @param entry
	 */
	public void addN33_ZouBanSetEntry(N33_ZouBanSetEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZOUBANSET, entry.getBaseEntry());
	}

	/**
	 * 查詢自动排课规则走班设置（根据类型，年级和次）
	 * @param sid
	 * @param ciId
	 * @param gradeId
	 * @param type
	 * @return
	 */
	public N33_ZouBanSetEntry findN33_ZouBanSetEntry(ObjectId sid,ObjectId ciId,ObjectId gradeId,Integer type) {
		BasicDBObject query = new BasicDBObject("sid", sid).append("ciId",ciId).append("gradeId",gradeId).append("type",type);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZOUBANSET, query, Constant.FIELDS);
		if (null != dbo) {
			return new N33_ZouBanSetEntry((BasicDBObject) dbo);
		}
		return null;
	}

	/**
	 * 查詢自动排课规则走班设置（根据年级和次）
	 * @param sid
	 * @param ciId
	 * @param gradeId
	 * @return
	 */
	public List<N33_ZouBanSetEntry> findN33_ZouBanSetEntryByGradeId(ObjectId sid,ObjectId ciId,ObjectId gradeId) {
		List<N33_ZouBanSetEntry> list = new ArrayList<N33_ZouBanSetEntry>();
		BasicDBObject query = new BasicDBObject("sid", sid).append("ciId",ciId).append("gradeId",gradeId);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZOUBANSET, query, Constant.FIELDS);
		for (DBObject dbo : dbObject) {
			N33_ZouBanSetEntry entry = new N33_ZouBanSetEntry((BasicDBObject) dbo);
			list.add(entry);
		}
		return list;
	}

	/**
	 * 修改自动排课规则走班设置
	 * @param entry
	 */
	public void updateN33_ZouBanSetEntry(N33_ZouBanSetEntry entry) {
		DBObject query = new BasicDBObject(Constant.ID, entry.getID());
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZOUBANSET, query, updateValue);
	}

}
