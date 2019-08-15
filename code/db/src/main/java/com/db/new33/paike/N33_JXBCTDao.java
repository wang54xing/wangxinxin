package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_JXBCTEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wang_xinxin on 2018/3/8.
 */
public class N33_JXBCTDao extends BaseDao {

    /**
     * 保存
     *
     * @param entry
     */
    public void addN33_JXBCTEntry(N33_JXBCTEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, entry.getBaseEntry());
    }

    /**
     * 增加多个冲突
     *
     * @param list
     */
    public void addN33_JXBCTEntrys(Collection<N33_JXBCTEntry> list) {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, dbObjects);
    }

    /**
     * 更新一条冲突
     *
     * @param jxbctEntry
     */
    public void updateN33_JXBCT(N33_JXBCTEntry jxbctEntry) {
        DBObject query = new BasicDBObject(Constant.ID, jxbctEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, jxbctEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, updateValue);
    }

    /**
     * 删除一条冲突
     *
     * @param id
     */
    public void deleteN33_JXBCT(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, updateValue);
    }

    /**
     * 物理删除
     *
     * @param id
     */
    public void removeN33_JXBCTEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public Integer getJXBCt(ObjectId jxbId, ObjectId ojxbId) {
        BasicDBObject query = new BasicDBObject("jxbId", jxbId).append("ojxbId", ojxbId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    /**
     * @param schoolId
     * @param jxbIds
     * @return
     */
    public List<N33_JXBCTEntry> getJXBCTEntrysByJXBs(ObjectId schoolId, List<ObjectId> jxbIds, ObjectId termId) {
        List<N33_JXBCTEntry> result = new ArrayList<N33_JXBCTEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId).append("ciId", termId).append("jxbId", new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBCTEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_JXBCTEntry> getJXBCTEntrysByJXB(ObjectId schoolId, ObjectId jxbId) {
        List<N33_JXBCTEntry> result = new ArrayList<N33_JXBCTEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId).append("jxbId", jxbId).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBCTEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_JXBCTEntry> getJXBCTEntrysByJXB(ObjectId schoolId, ObjectId jxbId, ObjectId orgJxbId) {
        List<N33_JXBCTEntry> result = new ArrayList<N33_JXBCTEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId).append("jxbId", jxbId).append("ojxbId", orgJxbId).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBCTEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public void removeByJxbId(ObjectId schoolId, ObjectId jxbId, Integer type) {
        DBObject query = new BasicDBObject("sid", schoolId).append("jxbId", jxbId).append("ir", Constant.ZERO).append("ctType", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbIdciId(ObjectId schoolId, ObjectId ciId, Integer type) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ciId", ciId).append("ir", Constant.ZERO).append("ctType", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByOJxbId(ObjectId schoolId, ObjectId jxbId, Integer type) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ojxbId", jxbId).append("ir", Constant.ZERO).append("ctType", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbId(ObjectId schoolId, List<ObjectId> jxbId, Integer type, ObjectId cid) {
        DBObject query = new BasicDBObject("sid", schoolId).append("jxbId", new BasicDBObject(Constant.MONGO_IN, jxbId)).append("ir", Constant.ZERO).append("ctType", type).append("ciId", cid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbId(ObjectId schoolId, Integer type, ObjectId cid) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO).append("ctType", type).append("ciId", cid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }



    public void removeByJxbId(ObjectId schoolId, List<ObjectId> jxbId, Integer type) {
        DBObject query = new BasicDBObject("sid", schoolId).append("jxbId", new BasicDBObject(Constant.MONGO_IN, jxbId)).append("ir", Constant.ZERO).append("ctType", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbIdAll(ObjectId schoolId,Integer type) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO).append("ctType", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbIdAll(ObjectId schoolId,Integer type,ObjectId ciId) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO).append("ctType", type).append("ciId",ciId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbIdo(ObjectId schoolId, List<ObjectId> jxbId, Integer type, ObjectId cid) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ojxbId", new BasicDBObject(Constant.MONGO_IN, jxbId)).append("ir", Constant.ZERO).append("ctType", type).append("ciId", cid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbIdo(ObjectId schoolId, Integer type, ObjectId cid) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO).append("ctType", type).append("ciId", cid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbIdo(ObjectId schoolId, List<ObjectId> jxbId, Integer type) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ojxbId", new BasicDBObject(Constant.MONGO_IN, jxbId)).append("ir", Constant.ZERO).append("ctType", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbIdoAll(ObjectId schoolId, Integer type) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO).append("ctType", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public void removeByJxbIdoAll(ObjectId schoolId, Integer type,ObjectId ciId) {
        DBObject query = new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO).append("ctType", type).append("ciId",ciId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public List<N33_JXBCTEntry> getJxbCTBySid(ObjectId schoolId, Integer type) {
        List<N33_JXBCTEntry> result = new ArrayList<N33_JXBCTEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId).append("ctType", type).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBCTEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_JXBCTEntry> getJxbCTByjxbIds(List<ObjectId> jxbIds) {
        List<N33_JXBCTEntry> result = new ArrayList<N33_JXBCTEntry>();
        BasicDBObject query = new BasicDBObject("jxbId", new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBCTEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public Integer getJxbCTByjxbIds(ObjectId jxbId, List<ObjectId> jxbIds) {
        BasicDBObject query = new BasicDBObject("ojxbId", new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO).append("jxbId", jxbId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }

    public Integer getJxbCTByjxbIds(ObjectId jxbId, List<ObjectId> jxbIds,Integer type) {
        BasicDBObject query = new BasicDBObject("ojxbId", new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO).append("jxbId", jxbId).append("type",type);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query);
    }
    /**
     * @param schoolId
     * @param jxbIds
     * @return
     */
    public List<N33_JXBCTEntry> getJXBCTEntrysByJXBs(ObjectId schoolId, List<ObjectId> jxbIds, Integer type) {
        List<N33_JXBCTEntry> result = new ArrayList<N33_JXBCTEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId).append("jxbId", new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ctType", type).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBCTEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_JXBCTEntry> getJXBCTEntrysByCiId(ObjectId ciId, ObjectId schoolId, Integer type) {
        List<N33_JXBCTEntry> result = new ArrayList<N33_JXBCTEntry>();
        BasicDBObject query = new BasicDBObject().append("ciId", ciId).append("ctType", type).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBCTEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * @param schoolId
     * @param
     * @return
     */
    public List<N33_JXBCTEntry> getJXBCTEntrysByOJXBs(ObjectId schoolId, List<ObjectId> ojxbIds, Integer type) {
        List<N33_JXBCTEntry> result = new ArrayList<N33_JXBCTEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId).append("ojxbId", new BasicDBObject(Constant.MONGO_IN, ojxbIds)).append("ctType", type).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBCT, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBCTEntry((BasicDBObject) dbo));
        }
        return result;
    }
}
