package com.db.new33.isolate;

import java.util.*;

import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.utils.MongoUtils;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sys.constants.Constant;

public class SubjectDao extends BaseDao {

    public void addIsolateSubjectEntry(List<DBObject> entrys) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, entrys);
    }

    /**
     * 设置走班学科
     *
     * @param entry
     */
    public void setIsZouBan(N33_KSEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, updateValue);
    }

    /**
     * 查询对应年级的走班学科或者非走班学科
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<N33_KSEntry> getIsolateSubjectEntryByZouBan(ObjectId xqid, ObjectId sid, ObjectId gid, Integer isZouBan) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid).append("isZouBan", isZouBan);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<N33_KSEntry> getIsolateSubjectEntryByZouBan(ObjectId xqid, ObjectId gid, Integer isZouBan,List<ObjectId> subIds) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("subid", new BasicDBObject(Constant.MONGO_NOTIN,subIds)).append("gid", gid).append("isZouBan", isZouBan);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<N33_KSEntry> getIsolateSubjectEntryByXqid(ObjectId xqid, ObjectId gid, List<ObjectId> sub) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid);
        if (gid != null) {
            query.append("gid", gid);
        }
        if (sub.size() > 0) {
            query.append("subid", new BasicDBObject(Constant.MONGO_IN, sub));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<N33_KSEntry> getIsolateSubjectEntryByXqid(ObjectId xqid, ObjectId sid, ObjectId gid) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        if (gid != null) {
            query.append("gid", gid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<N33_KSEntry> getIsolateSubjectEntryByXqid(List<ObjectId> xqid, ObjectId sid, ObjectId gid) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("sid", sid);
        if (gid != null) {
            query.append("gid", gid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<N33_KSEntry> getIsolateSubjectEntryByXqid(ObjectId xqid, ObjectId sid, ObjectId gid, Integer isZouBan) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("isZouBan", isZouBan);
        if (gid != null) {
            query.append("gid", gid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<N33_KSEntry> getIsolateSubjectEntryByXqid(ObjectId xqid, ObjectId sid) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<N33_KSEntry> getIsolateSubjectEntryByXqid(List<ObjectId> xqid, ObjectId sid) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN,xqid)).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<N33_KSEntry> getIsolateSubjectEntry(List<ObjectId> xqid, ObjectId sid) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<N33_KSEntry> getIsolateSubjectEntry(List<ObjectId> xqid, ObjectId sid,String gid) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("sid", sid);
        if(!gid.equals("*")){
            query.append("gid",new ObjectId(gid));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public N33_KSEntry findIsolateSubjectEntryById(ObjectId xqid, ObjectId sid, ObjectId subid, ObjectId gid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("subid", subid).append("gid", gid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_KSEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public List<N33_KSEntry> findSubjectsByIds(ObjectId xqid, ObjectId sid, Collection<ObjectId> subjectIds) {
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("sid", sid)
                .append("subid", new BasicDBObject(Constant.MONGO_IN, subjectIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);

        List<N33_KSEntry> subjects = new ArrayList<N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.add(new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }

    public List<N33_KSEntry> getSubjectEntryList(ObjectId sid, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);

        List<N33_KSEntry> subjects = new ArrayList<N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.add(new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }

    public void updateIsolateSubjectEntry(N33_KSEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, updateValue);
    }

    public void updateDanShuangZhou(String type, String id, Integer val) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(type, val));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, updateValue);
    }

    public N33_KSEntry findIsolateSubjectEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_KSEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public void addIsolateSubjectEntry(N33_KSEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, entry.getBaseEntry());
    }


    public void addIsolateSubjectEntrys(Collection<N33_KSEntry> entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, MongoUtils.fetchDBObjectList(entry));
    }

    public void DelAll(ObjectId xqid) {
        DBObject query = new BasicDBObject("xqid", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query);
    }

    public void DelSubject(ObjectId xqid, ObjectId subid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("subid", subid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query);
    }

    public void DelSubjectList(ObjectId xqid, ObjectId gid, List<ObjectId> ids, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid).append(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, ids));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query);
    }


    public void DelSubjectList(ObjectId xqid, ObjectId gid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("gid", gid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query);
    }
    public Map<ObjectId, N33_KSEntry> findSubjectsByIds(ObjectId xqid, ObjectId gid, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        Map<ObjectId, N33_KSEntry> subjects = new HashMap<ObjectId, N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.put(new N33_KSEntry((BasicDBObject) dbo).getID(), new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }

    public Map<ObjectId, N33_KSEntry> findSubjectsByIds(ObjectId xqid, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        Map<ObjectId, N33_KSEntry> subjects = new HashMap<ObjectId, N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.put(new N33_KSEntry((BasicDBObject) dbo).getID(), new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }

    public Map<ObjectId, N33_KSEntry> findSubjectsByForMap(ObjectId xqid, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        Map<ObjectId, N33_KSEntry> subjects = new HashMap<ObjectId, N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.put(new N33_KSEntry((BasicDBObject) dbo).getSubjectId(), new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }

    public Map<ObjectId, N33_KSEntry> findSubjectsByIdsForSubId(ObjectId xqid, ObjectId gid, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        Map<ObjectId, N33_KSEntry> subjects = new HashMap<ObjectId, N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.put(new N33_KSEntry((BasicDBObject) dbo).getSubjectId(), new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }

    public Map<ObjectId, N33_KSEntry> findSubjectsByIdsMapSubId(ObjectId xqid, ObjectId gid, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        if(gid != null){
            ((BasicDBObject) query).append("gid",gid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        Map<ObjectId, N33_KSEntry> subjects = new HashMap<ObjectId, N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.put(new N33_KSEntry((BasicDBObject) dbo).getSubjectId(), new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }

    public Map<ObjectId, N33_KSEntry> findSubjectsByIdsMapSubIdByCids(List<ObjectId> xqid, ObjectId gid, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN,xqid)).append("sid", sid);
        if(gid != null){
            ((BasicDBObject) query).append("gid",gid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        Map<ObjectId, N33_KSEntry> subjects = new HashMap<ObjectId, N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.put(new N33_KSEntry((BasicDBObject) dbo).getSubjectId(), new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }

    public Map<ObjectId, N33_KSEntry> findSubjectsByIdsMapSubIds(List<ObjectId> xqid, String gid, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN,xqid)).append("sid", sid);
        if(!"*".equals(gid)){
            ((BasicDBObject) query).append("gid",new ObjectId(gid));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        Map<ObjectId, N33_KSEntry> subjects = new HashMap<ObjectId, N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.put(new N33_KSEntry((BasicDBObject) dbo).getSubjectId(), new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }


    public List<N33_KSEntry> findSubjectsByIdsMapSubId(List<ObjectId> xqid, ObjectId gid, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("sid", sid).append("gid", gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        List<N33_KSEntry> subjects = new ArrayList<N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.add(new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }

    public List<N33_KSEntry> findSubjectsByIdsMapSubId(List<ObjectId> xqid) {
        DBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        List<N33_KSEntry> subjects = new ArrayList<N33_KSEntry>();
        for (DBObject dbo : dbObject) {
            subjects.add(new N33_KSEntry((BasicDBObject) dbo));
        }
        return subjects;
    }
    public List<N33_KSEntry> IsolateSubListByNewCi(ObjectId xqid) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_KSEntry entry = new N33_KSEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }


    public void add(Collection<N33_KSEntry> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, MongoUtils.fetchDBObjectList(list));
    }
    public void delSubByCid(ObjectId xqid) {
        DBObject query = new BasicDBObject("xqid", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query);
    }
    public List<N33_KSEntry> getIsolateSubjectEntryByXqid(ObjectId xqid, ObjectId sid, ObjectId gid, int type) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        if (gid != null) {
            query.append("gid", gid);
        }
        if (type == 3) {
            query.append("isZouBan", 0);
        } else if (type == 2 || type == 1) {
            query.append("isZouBan", 1);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new N33_KSEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    /**
     *
     * @param subs
     * @return
     */
    public Map<ObjectId,N33_KSEntry> getIsolateSubjectEntryBySubs(List<ObjectId> subs) {
        Map<ObjectId,N33_KSEntry> map = new HashMap<ObjectId, N33_KSEntry>();
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        BasicDBObject query = new BasicDBObject();
        if (subs.size() > 0) {
            query.append("subid", new BasicDBObject(Constant.MONGO_IN, subs));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_SUBJECT, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                N33_KSEntry entry = new N33_KSEntry((BasicDBObject) dbo);
                map.put(entry.getSubjectId(),entry);
            }
        }
        return map;
    }
}
