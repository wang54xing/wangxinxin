package com.db.new33;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_GDSXEntry;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class N33_GDSXDao extends BaseDao {

	/**
	 * 保存固定事项
	 *
	 * @param entry
	 */
	public void addGDSXEntry(N33_GDSXEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GDSX, entry.getBaseEntry());
	}

	/**
	 * 批量保存固定事项
	 *
	 * @param entry
	 */
	public void addGDSXEntry(Collection<N33_GDSXEntry> entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GDSX, MongoUtils.fetchDBObjectList(entry));
	}

	/**
	 * 根据id查询固定事项
	 *
	 * @param id
	 */
	public N33_GDSXEntry findGDSXEntry(ObjectId id) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GDSX, query, Constant.FIELDS);
		if (null != dbo) {
			return new N33_GDSXEntry((BasicDBObject) dbo);
		}
		return null;
	}

	/**
	 * 更新一条固定事项
	 *
	 * @param gdsxEntry
	 */
	public void updateN33_GDSXEntry(N33_GDSXEntry gdsxEntry) {
		DBObject query = new BasicDBObject(Constant.ID, gdsxEntry.getID());
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, gdsxEntry.getBaseEntry());
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GDSX, query, updateValue);
	}

	/**
	 * 物理删除固定事项
	 *
	 * @param id
	 */
	public void removeN33_GDSXEntry(ObjectId id) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GDSX, query);
	}

	/**
	 * 根据学校，年级，次，删除固定事项
	 * @param sid
	 * @param xqid
	 * @param gid
	 */
	public void removeN33_GDSXEntry(ObjectId sid,ObjectId xqid,ObjectId gid) {
		BasicDBObject query = new BasicDBObject("sid", sid).append("termId",xqid).append("gid",gid);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GDSX, query);
	}

	public List<N33_GDSXEntry> getGDSXBySidAndXqid(ObjectId xqid, ObjectId sid, ObjectId gid) {
		List<N33_GDSXEntry> entries = new ArrayList<N33_GDSXEntry>();
		BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid);
		if(gid != null){
			query.append("gid", gid);
		}
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GDSX, query, Constant.FIELDS);
		for (DBObject dbo : dbObject) {
			N33_GDSXEntry entry = new N33_GDSXEntry((BasicDBObject) dbo);
			entries.add(entry);
		}
		return entries;
	}

	public List<N33_GDSXEntry> getGDSXEntries(ObjectId xqid, ObjectId sid) {
		List<N33_GDSXEntry> entries = new ArrayList<N33_GDSXEntry>();
		BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GDSX, query, Constant.FIELDS);
		for (DBObject dbo : dbObject) {
			N33_GDSXEntry entry = new N33_GDSXEntry((BasicDBObject) dbo);
			entries.add(entry);
		}
		return entries;
	}

	public List<N33_GDSXEntry> getGDSXBySidAndXqid(ObjectId xqid, ObjectId sid, List<ObjectId> gid) {
		List<N33_GDSXEntry> entries = new ArrayList<N33_GDSXEntry>();
		BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid).append("gid", new BasicDBObject(Constant.MONGO_IN,gid));
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GDSX, query, Constant.FIELDS);
		for (DBObject dbo : dbObject) {
			N33_GDSXEntry entry = new N33_GDSXEntry((BasicDBObject) dbo);
			entries.add(entry);
		}
		return entries;
	}
}
