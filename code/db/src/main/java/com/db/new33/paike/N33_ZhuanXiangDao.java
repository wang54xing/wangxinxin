package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_XuNiBanEntry;
import com.pojo.new33.paike.N33_ZhuanXiangEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.omg.CORBA.ARG_IN;

import java.util.*;

/**
 * Created by albin on 2018/5/14.
 */
public class N33_ZhuanXiangDao extends BaseDao {


    public void addBEntry(N33_ZhuanXiangEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, entry.getBaseEntry());
    }

    public void add(Collection<N33_ZhuanXiangEntry> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, MongoUtils.fetchDBObjectList(list));
    }

    public void updateN33_ZhuanXiangEntry(N33_ZhuanXiangEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, updateValue);
    }

    public void del(ObjectId xqid) {
        DBObject query = new BasicDBObject("ciId", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query);
    }
    public void delete(ObjectId id){
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, updateValue);
    }

    public void deleteByJxb(ObjectId id){
        DBObject query =new BasicDBObject("jxbId",id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, updateValue);
    }
    public N33_ZhuanXiangEntry findN33_ZhuanXiangEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id).append("ir",0);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_ZhuanXiangEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public List<N33_ZhuanXiangEntry> findN33_ZhuanXiangEntry(ObjectId jxbId, ObjectId ciid) {
        List<N33_ZhuanXiangEntry> teaEntries = new ArrayList<N33_ZhuanXiangEntry>();
        BasicDBObject query = new BasicDBObject("jxbId", jxbId).append("ciId", ciid).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                teaEntries.add(new N33_ZhuanXiangEntry((BasicDBObject) dbo));
            }
        }
        return teaEntries;
    }

    public List<N33_ZhuanXiangEntry> findN33_ZhuanXiangEntry(List<ObjectId> jxbIds) {
        List<N33_ZhuanXiangEntry> teaEntries = new ArrayList<N33_ZhuanXiangEntry>();
        BasicDBObject query = new BasicDBObject("jxbId", new BasicDBObject(Constant.MONGO_IN,jxbIds)).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                teaEntries.add(new N33_ZhuanXiangEntry((BasicDBObject) dbo));
            }
        }
        return teaEntries;
    }

    public List<N33_ZhuanXiangEntry> findN33_ZhuanXiangEntry(ObjectId ciid) {
        List<N33_ZhuanXiangEntry> teaEntries = new ArrayList<N33_ZhuanXiangEntry>();
        BasicDBObject query = new BasicDBObject().append("ciId", ciid).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                teaEntries.add(new N33_ZhuanXiangEntry((BasicDBObject) dbo));
            }
        }
        return teaEntries;
    }

    public List<N33_ZhuanXiangEntry> findN33_ZhuanXiangEntryByTeaId(ObjectId ciid,ObjectId teaId) {
        List<N33_ZhuanXiangEntry> teaEntries = new ArrayList<N33_ZhuanXiangEntry>();
        BasicDBObject query = new BasicDBObject().append("ciId", ciid).append("tid",teaId).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                teaEntries.add(new N33_ZhuanXiangEntry((BasicDBObject) dbo));
            }
        }
        return teaEntries;
    }

    public Map<ObjectId,List<N33_ZhuanXiangEntry>> getZhuanXiangMap(ObjectId ciid){
        Map<ObjectId,List<N33_ZhuanXiangEntry>> result = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
        BasicDBObject query = new BasicDBObject("ciId", ciid).append("ir",0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                N33_ZhuanXiangEntry zhuanXiangEntry = new N33_ZhuanXiangEntry((BasicDBObject) dbo);
                List<N33_ZhuanXiangEntry> list = result.get(zhuanXiangEntry.getJxbId())==null?new ArrayList<N33_ZhuanXiangEntry>():result.get(zhuanXiangEntry.getJxbId());
                list.add(zhuanXiangEntry);
                result.put(zhuanXiangEntry.getJxbId(),list);
            }
        }
        return result;
    }

    public List<N33_ZhuanXiangEntry> findN33_ZhuanXiangEntryByRoomId(ObjectId classroomId, ObjectId ciid) {
        List<N33_ZhuanXiangEntry> teaEntries = new ArrayList<N33_ZhuanXiangEntry>();
        BasicDBObject query = new BasicDBObject("rid", classroomId).append("ciId", ciid).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                teaEntries.add(new N33_ZhuanXiangEntry((BasicDBObject) dbo));
            }
        }
        return teaEntries;
    }

    public List<N33_ZhuanXiangEntry> findN33_ZhuanXiangEntryByTeaIds(ObjectId ciid,List<ObjectId> teaIds) {
        List<N33_ZhuanXiangEntry> teaEntries = new ArrayList<N33_ZhuanXiangEntry>();
        BasicDBObject query = new BasicDBObject().append("ciId", ciid).append("tid",new BasicDBObject(Constant.MONGO_IN,teaIds)).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                teaEntries.add(new N33_ZhuanXiangEntry((BasicDBObject) dbo));
            }
        }
        return teaEntries;
    }

    /**
     * 对应jxbId的所有专项课Map
     * @param ciid
     * @param sid
     * @return
     */
    public Map<ObjectId,List<N33_ZhuanXiangEntry>> findN33_ZhuanXiangEntryByCiId(List<ObjectId> ciid,ObjectId sid) {
        Map<ObjectId,List<N33_ZhuanXiangEntry>> zhuanXiangEntryMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
        BasicDBObject query = new BasicDBObject().append("sid", sid).append("ciId",new BasicDBObject(Constant.MONGO_IN,ciid)).append("ir", 0);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZHUANG_XIANG, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                List<N33_ZhuanXiangEntry> zhuanXiangEntryList = new ArrayList<N33_ZhuanXiangEntry>();
                N33_ZhuanXiangEntry zhuanXiangEntry = new N33_ZhuanXiangEntry((BasicDBObject)dbo);
                if(zhuanXiangEntryMap.get(zhuanXiangEntry.getJxbId()) != null){
                    zhuanXiangEntryList = zhuanXiangEntryMap.get(zhuanXiangEntry.getJxbId());
                    zhuanXiangEntryList.add(zhuanXiangEntry);
                }else{
                    zhuanXiangEntryList.add(zhuanXiangEntry);
                    zhuanXiangEntryMap.put(zhuanXiangEntry.getJxbId(),zhuanXiangEntryList);
                }

            }
        }
        return zhuanXiangEntryMap;
    }
}
