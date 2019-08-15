package com.db.new33;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_XuNiBanEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

public class N33_VirtualClassDao extends BaseDao {
	/**
	 * 保存虚拟班
	 *
	 * @param entry
	 */
	public void addN33_VirtualClassEntry(N33_XuNiBanEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, entry.getBaseEntry());
	}


	/**
	 * 批量保存
	 *
	 * @param entry
	 */
	public void addN33_VirtualClassEntry(Collection<N33_XuNiBanEntry> entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, MongoUtils.fetchDBObjectList(entry));
	}

	/**
	 * 根据id查询虚拟班
	 *
	 * @param id
	 */
	public N33_XuNiBanEntry findN33_VirtualClassEntry(ObjectId id) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query, Constant.FIELDS);
		if (null != dbo) {
			return new N33_XuNiBanEntry((BasicDBObject) dbo);
		}
		return null;
	}

	/**
	 * 删除虚拟班
	 *
	 * @param xqid
	 */
	public void delN33_VirtualClassEntryByXqid(ObjectId xqid,ObjectId sid) {
		DBObject query = new BasicDBObject("xqid", xqid).append("sid",sid);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query);
	}

	/**
	 * 删除虚拟班
	 *
	 * @param id
	 */
	public void delN33_VirtualClassEntry(ObjectId id) {
		DBObject query = new BasicDBObject(Constant.ID, id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query);
	}
	public void delN33_VirtualClassEntryTag(ObjectId id) {
		DBObject query = new BasicDBObject("tagList.tagId", id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query);
	}

	/**
	 * 删除等待删除的
	 */
	public void delN33_VirtualClassEntryTag() {
		DBObject query = new BasicDBObject("type", 1000);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query);
	}
	public List<N33_XuNiBanEntry> getN33_VirtualClassEntryTag(ObjectId tagId){
		List<N33_XuNiBanEntry> returnList = new ArrayList<N33_XuNiBanEntry>();
		DBObject query = new BasicDBObject("tagList.tagId", tagId);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				returnList.add(new N33_XuNiBanEntry((BasicDBObject) dbo));
			}
		}
		return returnList;
	}
	/**
	 * 修改虚拟班
	 * @param entry
	 */
	public void updateN33_VirtualClassEntry(N33_XuNiBanEntry entry) {
		DBObject query = new BasicDBObject(Constant.ID, entry.getID());
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query, updateValue);
	}

	/**
	 * 根据次和年级查询虚拟班
	 * @param xqid
	 * @param sid
	 * @param gradeId
	 * @return
	 */
	public List<N33_XuNiBanEntry> getN33_VirtualClassEntryByXqidAndGrade(ObjectId xqid,ObjectId sid,ObjectId gradeId,Integer type){
		List<N33_XuNiBanEntry> returnList = new ArrayList<N33_XuNiBanEntry>();
		BasicDBObject query = new BasicDBObject("xqid",xqid).append("sid",sid).append("gradeId",gradeId).append("type",type);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				returnList.add(new N33_XuNiBanEntry((BasicDBObject) dbo));
			}
		}
		return returnList;
	}

	/**
	 * 根据次和年级查询虚拟班
	 * @param xqid
	 * @param sid
	 * @param gradeId
	 * @return
	 */
	public List<N33_XuNiBanEntry> getAllN33_VirtualClassEntryByXqidAndGrade(ObjectId xqid,ObjectId sid,ObjectId gradeId){
		List<N33_XuNiBanEntry> returnList = new ArrayList<N33_XuNiBanEntry>();
		BasicDBObject query = new BasicDBObject("xqid",xqid).append("sid",sid).append("gradeId",gradeId);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				returnList.add(new N33_XuNiBanEntry((BasicDBObject) dbo));
			}
		}
		return returnList;
	}

	/**
	 * 根据次和年级查询虚拟班
	 * @param xqid
	 * @param sid
	 * @param gradeId
	 * @return
	 */
	public Map<ObjectId,N33_XuNiBanEntry> getN33_VirtualClassEntryByXqidAndGradeForMap(ObjectId xqid,ObjectId sid,ObjectId gradeId,Integer type){
		Map<ObjectId,N33_XuNiBanEntry> map = new HashMap<ObjectId, N33_XuNiBanEntry>();
		BasicDBObject query = new BasicDBObject("xqid",xqid).append("sid",sid).append("gradeId",gradeId).append("type",type);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				N33_XuNiBanEntry xuNiBanEntry = new N33_XuNiBanEntry((BasicDBObject) dbo);
				map.put(xuNiBanEntry.getID(),xuNiBanEntry);
			}
		}
		return map;
	}

	public List<N33_XuNiBanEntry> getN33_VirtualClassEntryByXqid(ObjectId xqid,ObjectId sid){
		List<N33_XuNiBanEntry> returnList = new ArrayList<N33_XuNiBanEntry>();
		BasicDBObject query = new BasicDBObject("xqid",xqid).append("sid",sid);
		List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_VIRTUALCLASS, query, Constant.FIELDS);
		if(dblist != null){
			for (DBObject dbo : dblist) {
				returnList.add(new N33_XuNiBanEntry((BasicDBObject) dbo));
			}
		}
		return returnList;
	}
}
