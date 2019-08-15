package com.db.new33.paike;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * Created by wang_xinxin on 2018/3/8.
 */
public class N33_SWDao extends BaseDao {
	
    /**
     * 保存事务
     *
     * @param entry
     */
    public void addN33_SWEntry(N33_SWEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, entry.getBaseEntry());
    }

    /**
     * 增加多个事务
     *
     * @param list
     */
    public void addN33_SWEntrys(Collection<N33_SWEntry> list) {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, dbObjects);
    }

    /**
     * 更新一条事务
     *
     * @param swEntry
     */
    public void updateN33_SW(N33_SWEntry swEntry) {
        DBObject query = new BasicDBObject(Constant.ID, swEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, swEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, updateValue);
    }

    /**
     * 删除一条事务
     *
     * @param id
     */
    public void deleteN33_SW(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, updateValue);
    }
    
    /**
     *	物理删除对应事务类别下的所有事务
     *
     * @param id
     */
    public void deleteN33_AllSwlbSW(ObjectId id) {
    	BasicDBObject query = new BasicDBObject("swlbId", id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query);
    }

    /**
     * 物理删除事务
     *
     * @param id
     */
    public void removeN33_SWEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query);
    }
    
    public List<N33_SWEntry> getSwByXqidAndType(ObjectId xqid, ObjectId sid, ObjectId typeId) {
        List<N33_SWEntry> entries = new ArrayList<N33_SWEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid).append("swlbId", typeId).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_SWEntry entry = new N33_SWEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }


    public List<N33_SWEntry> getSwByXqidAndUserId(ObjectId xqid, ObjectId uid) {
        List<N33_SWEntry> entries = new ArrayList<N33_SWEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("tids", uid).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_SWEntry entry = new N33_SWEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<N33_SWEntry> getSwByXqid(ObjectId xqid) {
        List<N33_SWEntry> entries = new ArrayList<N33_SWEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_SWEntry entry = new N33_SWEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<N33_SWEntry> getSwByXqidAndXY(ObjectId xqid,Integer x,Integer y,ObjectId sid) {
        List<N33_SWEntry> entries = new ArrayList<N33_SWEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("y",x).append("x",y).append("sid",sid).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_SWEntry entry = new N33_SWEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }


    public List<N33_SWEntry> getSwBySchoolId(ObjectId xqid,ObjectId schoolId) {
        List<N33_SWEntry> entries = new ArrayList<N33_SWEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid",schoolId).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_SWEntry entry = new N33_SWEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    /**
     *
     * @param xqid
     * @param x  xy反了
     * @param y
     * @return
     */
    public List<N33_SWEntry> getSwByXY(ObjectId xqid,Integer x,Integer y) {
        List<N33_SWEntry> entries = new ArrayList<N33_SWEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("x",x).append("y",y).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_SWEntry entry = new N33_SWEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }
    public N33_SWEntry getSwByXqidAndType(ObjectId Id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, Id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, Constant.FIELDS);
        N33_SWEntry entry = new N33_SWEntry((BasicDBObject) dbObject);
        return entry;
    }

    public  List<N33_SWEntry> getSwByXqidAndTypereId(ObjectId reId) {
        List<N33_SWEntry> entries = new ArrayList<N33_SWEntry>();
        BasicDBObject query = new BasicDBObject("reId", reId).append("ir",0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_SWEntry entry = new N33_SWEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }
    public List<N33_SWEntry> getSwByXqidAndUserIds(ObjectId xqid, List<ObjectId> uids) {
        List<N33_SWEntry> entries = new ArrayList<N33_SWEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("tids", new BasicDBObject(Constant.MONGO_IN,uids)).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SW, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_SWEntry entry = new N33_SWEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }
}
