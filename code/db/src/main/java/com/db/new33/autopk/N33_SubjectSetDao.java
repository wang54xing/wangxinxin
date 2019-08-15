package com.db.new33.autopk;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.N33_SubjectSetEntry;
import com.pojo.autoPK.N33_ZouBanSetEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_SubjectSetDao extends BaseDao {
	/**
	 * 保存自动排课的学科设置
	 *
	 * @param entry
	 */
	public void addN33_SubjectSetEntry(N33_SubjectSetEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SUBJECTSET, entry.getBaseEntry());
	}

	/**
	 * 查詢自动排课规则学科设置（根据类型，年级和次）
	 * @param sid
	 * @param ciId
	 * @param gradeId
	 * @param type
	 * @return
	 */
	public N33_SubjectSetEntry findN33_SubjectSetEntry(ObjectId sid,ObjectId ciId,ObjectId gradeId,Integer type) {
		BasicDBObject query = new BasicDBObject("sid", sid).append("ciId",ciId).append("gradeId",gradeId).append("type",type);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SUBJECTSET, query, Constant.FIELDS);
		if (null != dbo) {
			return new N33_SubjectSetEntry((BasicDBObject) dbo);
		}
		return null;
	}

	public N33_SubjectSetEntry findN33_SubjectSetEntryByKS(ObjectId sid,ObjectId ciId,ObjectId gradeId,Integer type,Integer amCount,Integer pmCount) {
		BasicDBObject query = new BasicDBObject("sid", sid).append("ciId",ciId).append("gradeId",gradeId).append("type",type).append("amcount",amCount).append("pmcount",pmCount);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SUBJECTSET, query, Constant.FIELDS);
		if (null != dbo) {
			return new N33_SubjectSetEntry((BasicDBObject) dbo);
		}
		return null;
	}

	/**
	 * 查詢自动排课规则学科设置（根据年级和次）
	 * @param sid
	 * @param ciId
	 * @param gradeId
	 * @return
	 */
	public List<N33_SubjectSetEntry> findN33_SubjectSetEntryByGradeId(ObjectId sid, ObjectId ciId, ObjectId gradeId) {
		List<N33_SubjectSetEntry> list = new ArrayList<N33_SubjectSetEntry>();
		BasicDBObject query = new BasicDBObject("sid", sid).append("ciId",ciId).append("gradeId",gradeId);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SUBJECTSET, query, Constant.FIELDS);
		for (DBObject dbo : dbObject) {
			N33_SubjectSetEntry entry = new N33_SubjectSetEntry((BasicDBObject) dbo);
			list.add(entry);
		}
		return list;
	}

	/**
	 * 修改自动排课规则学科设置
	 * @param entry
	 */
	public void updateN33_SubjectSetEntry(N33_SubjectSetEntry entry) {
		DBObject query = new BasicDBObject(Constant.ID, entry.getID());
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SUBJECTSET, query, updateValue);
	}
}
