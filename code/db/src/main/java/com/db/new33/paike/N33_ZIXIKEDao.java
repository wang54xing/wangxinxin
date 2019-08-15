package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_ZIXIKEEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

public class N33_ZIXIKEDao extends BaseDao {

    /**
     * 保存教学班
     *
     * @param entry
     */
    public ObjectId addN33_ZIXIKEEntry(N33_ZIXIKEEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 增加多个教学班
     *  @param list
     */
    public void addN33_ZIXIKEEntrys(Collection<N33_ZIXIKEEntry> list) {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        if (dbObjects.size() > 0) {
            save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, dbObjects);
        }
    }

    /**
     * 更新一条教学班
     *
     * @param jxbEntry
     */
    public void updateN33_JXB(N33_ZIXIKEEntry jxbEntry) {
        DBObject query = new BasicDBObject(Constant.ID, jxbEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, jxbEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }

    /**
     * 删除一条教学班
     *
     * @param id
     */
    public void removeN33_ZIXIKEEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }

    /**
     * 删除一条教学班
     *
     * @param
     */
    public void removeN33_ZIXIKEEntryByXqGiD(ObjectId ciId,ObjectId gid) {
        DBObject query = new BasicDBObject("termId",ciId).append("gid",gid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }

    /**
     * 删除一条教学班
     *
     * @param
     */
    public void removeN33_ZIXIKEEntryByXqGiDByClassRoom(ObjectId ciId,ObjectId gid,ObjectId classRoomId) {
        DBObject query = new BasicDBObject("termId",ciId).append("gid",gid).append("clsrmId",classRoomId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }

    /**
     * 删除一条教学班
     *
     * @param
     */
    public void removeN33_ZIXIKEEntryByXqGiDByTeaId(ObjectId ciId,ObjectId gid,ObjectId teaId) {
        DBObject query = new BasicDBObject("termId",ciId).append("gid",gid).append("tid",teaId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }



    /**
     * 物理删除教学班
     *
     * @param id
     */
    public void removeN33_ZIXIKEEntryPhysically(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query);
    }

    /**
     * @param id
     * @return
     */
    public N33_ZIXIKEEntry getJXBById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_ZIXIKEEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public N33_ZIXIKEEntry getJXBByIdAndStudentIds(ObjectId id, List<ObjectId> studentIds,ObjectId cid) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        if (studentIds.size() > 0) {
            query.append("stuIds", new BasicDBObject(Constant.MONGO_IN, studentIds));
        }
        query.append("ir", Constant.ZERO).append("termId",cid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_ZIXIKEEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public N33_ZIXIKEEntry getStudentIsJiaoXueBanByTongXueKe(ObjectId id, List<ObjectId> studentIds, ObjectId subjectId,ObjectId cid) {
        BasicDBObject query = new BasicDBObject("subId", subjectId).append(Constant.ID, new BasicDBObject(Constant.MONGO_NE, id));
        if (studentIds.size() > 0) {
            query.append("stuIds", new BasicDBObject(Constant.MONGO_IN, studentIds));
        }
        query.append("ir", Constant.ZERO).append("termId",cid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_ZIXIKEEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * @param termId
     * @param schoolId
     * @param gradeId
     * @param subjectId
     * @return
     */
    public List<N33_ZIXIKEEntry> getJXBsBySubId(ObjectId termId, ObjectId schoolId, ObjectId gradeId, ObjectId subjectId) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("gid", gradeId).
                append("subId", subjectId).append("ir", Constant.ZERO);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * @param termId
     * @param schoolId
     * @param gradeId
     * @param subjectIds
     * @return
     */
    public List<N33_ZIXIKEEntry> getJXBsBySubIds(ObjectId termId, ObjectId schoolId, ObjectId gradeId, List<ObjectId> subjectIds) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("gid", gradeId).
                append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds)).append("ir", Constant.ZERO);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * @param termId
     * @param schoolId
     * @param gradeId
     * @param subjectIds
     * @return
     */
    public List<N33_ZIXIKEEntry> getJXBsBySubIds(ObjectId termId, ObjectId schoolId, ObjectId gradeId, List<ObjectId> subjectIds, String type) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("gid", gradeId).
                append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds)).append("ir", Constant.ZERO);
        if (!"0".equals(type)) {
            query.append("tp", Integer.valueOf(type));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }


    public List<N33_ZIXIKEEntry> getJXBsBySubIds(ObjectId termId, List<ObjectId> subjectIds) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).
                append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds)).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }



    public Map<ObjectId, N33_ZIXIKEEntry> getJXBsBySubIdsByMap(ObjectId termId) {
        Map<ObjectId, N33_ZIXIKEEntry> result = new HashMap<ObjectId, N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_ZIXIKEEntry((BasicDBObject) dbo).getID(), new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * 根据学期id和年级查询教学班
     *
     * @param termId
     * @param schoolId
     * @param gradeId
     * @return
     */
    public List<N33_ZIXIKEEntry> getJXBsByXqidAndGid(ObjectId termId, ObjectId schoolId, ObjectId gradeId) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("gid", gradeId).append("ir", Constant.ZERO);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * @param termId
     * @param schoolId
     * @param gradeId
     * @param subjectId
     * @return
     */
    public List<N33_ZIXIKEEntry> getJXBsBySubId(ObjectId termId, ObjectId schoolId, ObjectId gradeId, ObjectId subjectId, Integer type) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("gid", gradeId).
                append("subId", subjectId).append("ir", Constant.ZERO);
        if (type != 0) {
            query.append("tp", type);
        }

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }



    public List<N33_ZIXIKEEntry> getJXBsBygid(ObjectId termId, ObjectId schoolId, ObjectId gid) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO).append("gid", gid);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }
    public List<N33_ZIXIKEEntry> getJXBsBySubIdS(ObjectId termId, ObjectId schoolId, ObjectId gid,ObjectId cid) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", cid).append("sid", schoolId).append("ir", Constant.ZERO).append("gid", gid);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZIXIKEEntry> getJXBsBySubId(ObjectId termId) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("ir", Constant.ZERO);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS, new BasicDBObject("gid", 1));
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }


    public Map<ObjectId, N33_ZIXIKEEntry> getJXBsBySubId(List<ObjectId> termId) {
        Map<ObjectId, N33_ZIXIKEEntry> result = new HashMap<ObjectId, N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, termId)).append("ir", Constant.ZERO);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS, new BasicDBObject("gid", 1));
        for (DBObject dbo : dblist) {
            result.put(new N33_ZIXIKEEntry((BasicDBObject) dbo).getID(),new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public Map<ObjectId, N33_ZIXIKEEntry> getJXBsByXqidAndSid(ObjectId xqid, ObjectId sid, ObjectId gid) {
        Map<ObjectId, N33_ZIXIKEEntry> result = new HashMap<ObjectId, N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("ir", Constant.ZERO).append("sid", sid).append("gid", gid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_ZIXIKEEntry((BasicDBObject) dbo).getID(), new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }


    public Map<ObjectId, N33_ZIXIKEEntry> getJXBsBySubId(ObjectId termId, ObjectId userId) {
        Map<ObjectId, N33_ZIXIKEEntry> result = new HashMap<ObjectId, N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("ir", Constant.ZERO).append("tid", userId);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_ZIXIKEEntry((BasicDBObject) dbo).getID(), new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }


    public List<N33_ZIXIKEEntry> getJXBsBySubId(List<ObjectId> termId, ObjectId userId) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, termId)).append("ir", Constant.ZERO).append("tid", userId);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public Map<ObjectId, N33_ZIXIKEEntry> getJXBsByRoomId(ObjectId termId, ObjectId roomId) {
        Map<ObjectId, N33_ZIXIKEEntry> result = new HashMap<ObjectId, N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("ir", Constant.ZERO).append("clsrmId", roomId);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_ZIXIKEEntry((BasicDBObject) dbo).getID(), new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }
    public List<N33_ZIXIKEEntry> getJxbListByRoomId(ObjectId termId, ObjectId roomId){
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("ir", Constant.ZERO).append("clsrmId", roomId);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }
    /**
     * @param jxbIds
     * @return
     */
    public List<N33_ZIXIKEEntry> getJXBByIds(List<ObjectId> jxbIds) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }


    public List<N33_ZIXIKEEntry> getJXBByIds(List<ObjectId> jxbIds, ObjectId xqid) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO).append("termId", xqid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }



    public void updateTeacherId(ObjectId id, ObjectId tid) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("tid", tid));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }

    public void updateClassId(ObjectId id, ObjectId clsrmId) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("clsrmId", clsrmId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }

    public void updateNickName(ObjectId id, String name) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("name", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }

    public void update(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }

    public void update(ObjectId id,Integer ir) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", ir));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, updateValue);
    }
    public Map<ObjectId, N33_ZIXIKEEntry> getJXBByStudetnIds(List<ObjectId> userId, ObjectId xqid) {
        Map<ObjectId, N33_ZIXIKEEntry> result = new HashMap<ObjectId, N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("stuIds", new BasicDBObject(Constant.MONGO_IN, userId)).append("ir", Constant.ZERO).append("termId", xqid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_ZIXIKEEntry((BasicDBObject) dbo).getID(), new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * @param jxbIds
     * @return
     */
    public Map<ObjectId, N33_ZIXIKEEntry> getJXBMapsByIds(List<ObjectId> jxbIds) {
        Map<ObjectId, N33_ZIXIKEEntry> jxbEntryMap = new HashMap<ObjectId, N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_ZIXIKEEntry jxbEntry = new N33_ZIXIKEEntry((BasicDBObject) dbo);
            jxbEntryMap.put(jxbEntry.getID(), jxbEntry);
        }
        return jxbEntryMap;
    }

    /**
     * @param termId
     * @param schoolId
     * @return
     */
    public List<N33_ZIXIKEEntry> getJXBsBySchoolId(ObjectId termId, ObjectId schoolId) {
        List<N33_ZIXIKEEntry> result = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZIXIKEEntry((BasicDBObject) dbo));
        }
        return result;
    }


    public void add(Collection<N33_ZIXIKEEntry> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, MongoUtils.fetchDBObjectList(list));
    }
    public List<N33_ZIXIKEEntry> IsolateN33_ZIXIKEEntryByNewCi(ObjectId xqid) {
        List<N33_ZIXIKEEntry> entries = new ArrayList<N33_ZIXIKEEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_ZIXIKEEntry entry = new N33_ZIXIKEEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public void delN33_ZIXIKEEntryCid(ObjectId xqid) {
        DBObject query = new BasicDBObject("termId", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZIXIBAN, query);
    }
}
