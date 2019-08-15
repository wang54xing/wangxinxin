package com.db.new33.isolate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.CourseRangeEntry;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.new33.isolate.ZouBanTimeEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ZouBanTimeDao extends BaseDao {

	public void setZouBanTime(ZouBanTimeEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SETZOUBANTIME, entry.getBaseEntry());
	}

	public ZouBanTimeEntry getZouBanTime(ObjectId sid,ObjectId xqid,ObjectId gid,Integer x,Integer y){
		BasicDBObject query = new BasicDBObject("sid", sid).append("xqid", xqid).append("gid",gid).append("x",x).append("y",y);
		DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SETZOUBANTIME, query, Constant.FIELDS);
		if (dbObject != null) {
			return new ZouBanTimeEntry((BasicDBObject) dbObject);
		} else {
			return null;
		}
	}

	public List<ZouBanTimeEntry> getZouBanTimeList(ObjectId sid,ObjectId xqid,ObjectId gid){
		List<ZouBanTimeEntry> list = new ArrayList<ZouBanTimeEntry>();
		BasicDBObject query = new BasicDBObject("sid", sid).append("xqid", xqid).append("gid",gid);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SETZOUBANTIME, query, Constant.FIELDS);
		if (null != dbObject && !dbObject.isEmpty()) {
			for (DBObject dbo : dbObject) {
				list.add(new ZouBanTimeEntry((BasicDBObject) dbo));
			}
		}
		return list;
	}

	/**
	 * 根据学校和次id查询走班时间（用于同步上一次数据）
	 * @param sid
	 * @param cid
	 * @return
	 */
	public List<ZouBanTimeEntry> getZouBanTimeListForEntry(ObjectId sid,ObjectId cid){
		List<ZouBanTimeEntry> list = new ArrayList<ZouBanTimeEntry>();
		BasicDBObject query = new BasicDBObject("sid", sid).append("xqid", cid);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SETZOUBANTIME, query, Constant.FIELDS);
		if (null != dbObject && !dbObject.isEmpty()) {
			for (DBObject dbo : dbObject) {
				list.add(new ZouBanTimeEntry((BasicDBObject) dbo));
			}
		}
		return list;
	}

	public List<ZouBanTimeEntry> getZouBanTimeListByType(ObjectId sid,ObjectId xqid,ObjectId gid,Integer type){
		List<ZouBanTimeEntry> list = new ArrayList<ZouBanTimeEntry>();
		BasicDBObject query = new BasicDBObject("sid", sid).append("xqid", xqid).append("gid",gid).append("type",type);
		List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SETZOUBANTIME, query, Constant.FIELDS);
		if (null != dbObject && !dbObject.isEmpty()) {
			for (DBObject dbo : dbObject) {
				list.add(new ZouBanTimeEntry((BasicDBObject) dbo));
			}
		}
		return list;
	}

	public void updateZouBanTimeEntry(ZouBanTimeEntry entry) {
		DBObject query = new BasicDBObject("sid", entry.getSid()).append("xqid", entry.getXqid()).append("gid",entry.getGradeId()).append("x",entry.getX()).append("y",entry.getY());
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SETZOUBANTIME, query, updateValue);
	}

	public void add(List<ZouBanTimeEntry> list) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SETZOUBANTIME, MongoUtils.fetchDBObjectList(list));
	}

	/**
	 * 根据次id删除走班时间
	 *
	 * @param cid
	 */
	public void removeByCiId(ObjectId sid,ObjectId cid) {
		DBObject query = new BasicDBObject().append("xqid", cid).append("sid",sid);
		remove(MongoFacroty.getAppDB(),
				Constant.COLLECTION_NEW33_SETZOUBANTIME, query);
	}

}
