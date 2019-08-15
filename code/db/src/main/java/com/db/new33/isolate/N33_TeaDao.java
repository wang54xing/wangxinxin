package com.db.new33.isolate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

public class N33_TeaDao extends BaseDao {

    public void addIsolateN33_TeaEntry(N33_TeaEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, entry.getBaseEntry());
    }

    public void addIsolateN33_TeaEntryEntrys(List<DBObject> entrys) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, entrys);
    }

    public void add(Collection<N33_TeaEntry> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, MongoUtils.fetchDBObjectList(list));
    }

    public void updateIsolateN33_TeaEntry(N33_TeaEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, updateValue);
    }

    /**
     * 根据用户姓名对用户进行模糊查询
     *
     * @param sid
     * @param userName
     * @return
     */
    public List<N33_TeaEntry> findTeaListByName(ObjectId sid, String userName) {
        BasicDBObject query = new BasicDBObject().append("unm", MongoUtils.buildRegex(userName)).append("sid", sid);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        List<N33_TeaEntry> teaEntryList = new ArrayList<N33_TeaEntry>();
        for (DBObject dbObject : dbObjectList) {
            N33_TeaEntry teaEntry = new N33_TeaEntry((BasicDBObject) dbObject);
            teaEntryList.add(teaEntry);
        }
        return teaEntryList;
    }

    public N33_TeaEntry findIsolateN33_TeaEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_TeaEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public N33_TeaEntry findIsolateN33_TeaEntryById(ObjectId xqid, ObjectId subjectId,ObjectId gradeId,String teaName) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("unm", teaName).append("gid", gradeId).append("sub",subjectId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_TeaEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    /**
     * @param ids
     * @return
     */
    public List<N33_TeaEntry> findIsolateN33_TeaEntryByuIds(ObjectId termId, List<ObjectId> ids) {
        List<N33_TeaEntry> teaEntries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, ids)).append("xqid", termId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                teaEntries.add(new N33_TeaEntry((BasicDBObject) dbo));
            }
        }
        return teaEntries;
    }

    public List<N33_TeaEntry> findIsolateN33_TeaEntryByuIds(ObjectId termId, List<ObjectId> ids, ObjectId gid) {
        List<N33_TeaEntry> teaEntries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, ids)).append("xqid", termId).append("gid", gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        if (dbObject != null && dbObject.size() != 0) {
            for (DBObject dbo : dbObject) {
                teaEntries.add(new N33_TeaEntry((BasicDBObject) dbo));
            }
        }
        return teaEntries;
    }

    public N33_TeaEntry findIsolateN33_TeaEntryById(ObjectId uid, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("uid", uid).append("xqid", xqid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_TeaEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }


    public List<N33_TeaEntry> findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(ObjectId xqid, ObjectId sid, ObjectId gid, ObjectId subjectId, String name) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        if (subjectId != null) {
            query.append("sub", subjectId);
        }
        if(gid != null){
            query.append("gid", gid);
        }
        if (!name.equals("*")) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<N33_TeaEntry> findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectIdByCiIds(List<ObjectId> xqids, ObjectId sid, ObjectId gid, ObjectId subjectId, String name) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqids)).append("sid", sid);
        if (subjectId != null) {
            query.append("sub", subjectId);
        }
        if(gid != null){
            query.append("gid", gid);
        }
        if (!name.equals("*")) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<N33_TeaEntry> findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(ObjectId xqid, ObjectId sid, ObjectId gid, List<ObjectId> subjectId) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid);
        if (subjectId != null && subjectId.size() > 0) {
            query.append("sub", new BasicDBObject(Constant.MONGO_IN, subjectId));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<N33_TeaEntry> findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(List<ObjectId> xqid, List<ObjectId> userId) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("uid", new BasicDBObject(Constant.MONGO_IN, userId));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<N33_TeaEntry> findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(List<ObjectId> xqid, ObjectId sid, ObjectId gid, ObjectId subjectId, String name) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("sid", sid);
        if (subjectId != null) {
            query.append("sub", subjectId);
        }
        if (!name.equals("*") && gid != null) {
            query.append("unm", MongoUtils.buildRegex(name));
            query.append("gid", gid);
        } else if(!name.equals("*")){
            query.append("unm", MongoUtils.buildRegex(name));
        }else{
            query.append("gid", gid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }


    public List<N33_TeaEntry> findN33_TeaEntryBySubjectId(ObjectId xqid, ObjectId subjectId, String name) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid);
        if (subjectId != null) {
            query.append("sub", subjectId);
        }
        if (!name.equals("*")) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    /**
     * 查询学期对应所有次的老师并去重
     *
     * @param xqid
     * @param subjectId
     * @param name
     * @return
     */
    public List<N33_TeaEntry> findN33_TeaEntryBySubjectIdByCiId(List<ObjectId> xqid, String subjectId, String name, String gradeId) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        //用于去重
        List<ObjectId> tids = new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));
        if (!"*".equals(subjectId)) {
            query.append("sub", new ObjectId(subjectId));
        }
        if (!name.equals("*")) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        if (!"*".equals(gradeId)) {
            query.append("gid", new ObjectId(gradeId));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            if (!tids.contains(entry.getUserId())) {
                entries.add(entry);
                tids.add(entry.getUserId());
            }
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> findN33_TeaEntryBySubjectId(List<ObjectId> xqid, ObjectId subjectId, String name) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));
        if (subjectId != null) {
            query.append("sub", subjectId);
        }
        if (!name.equals("*")) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> findN33_TeaEntryBySubjectId(List<ObjectId> xqid, ObjectId subjectId, String name,ObjectId gid) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));
        if (subjectId != null) {
            query.append("sub", subjectId);
        }
        if (!name.equals("*")) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        query.append("gid",gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> findN33_TeaEntryBySubjectIdG(List<ObjectId> xqid, ObjectId subjectId, String name,String gid) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));
        if (subjectId != null) {
            query.append("sub", subjectId);
        }
        if (!name.equals("*")) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        if(!gid.equals("*")) {
            query.append("gid", new ObjectId(gid));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> findN33_TeaEntryBySubjectIdG(List<ObjectId> xqid, String name,String gid) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));

        if (!name.equals("*")) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        if(!gid.equals("*")) {
            query.append("gid", new ObjectId(gid));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    /**
     * 返回教师Map
     *
     * @param xqid
     * @param subjectId
     * @param name
     * @param gradeId
     * @return
     */
    public Map<ObjectId, N33_TeaEntry> findN33_TeaEntryBySubjectId(List<ObjectId> xqid, String subjectId, String name, String gradeId) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));
        if (!"*".equals(subjectId)) {
            query.append("sub", new ObjectId(subjectId));
        }
        if (!name.equals("*")) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        if (!"*".equals(gradeId)) {
            query.append("gid", new ObjectId(gradeId));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> getTeaMap(ObjectId xqid, ObjectId sid, ObjectId gid, ObjectId subjectId) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid);
        if (subjectId != null) {
            query.append("sub", subjectId);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getID(), entry);
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> getTeaMap(ObjectId xqid, ObjectId sid, ObjectId gid, List<ObjectId> subjectId) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid);
        if (subjectId != null&&subjectId.size()>0) {
            query.append("sub", new BasicDBObject(Constant.MONGO_IN,subjectId));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getID(), entry);
        }
        return entries;
    }

    public Map<String, String> getTeacherMap(ObjectId sid) {
        Map<String, String> map = new HashMap<String, String>();
        BasicDBObject query = new BasicDBObject("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            map.put(entry.getUserName(), entry.getUserId().toString());
        }
        return map;
    }

    public Map<ObjectId, N33_TeaEntry> getTeacherMapBySid(ObjectId sid) {
        Map<ObjectId, N33_TeaEntry> map = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            map.put(entry.getUserId(), entry);
        }
        return map;
    }

    public Map<ObjectId, N33_TeaEntry> getTeaMap(ObjectId xqid, ObjectId sid, ObjectId gid) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }


    public Map<ObjectId, N33_TeaEntry> getTeaMap(ObjectId xqid, List<ObjectId> userId) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("uid", new BasicDBObject(Constant.MONGO_IN,userId));

        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> getTeaMap(ObjectId xqid, ObjectId sid) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    /**
     * 查询排课次所有的老师
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<N33_TeaEntry> getTeaListByGradeId(ObjectId xqid, ObjectId sid, ObjectId gid, ObjectId subId) {
        List<N33_TeaEntry> teaList = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid).append("sub", subId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            teaList.add(entry);
        }
        return teaList;
    }

    /**
     * 查询排课次所有的老师
     *
     * @param sid
     * @param gid
     * @param xqid
     * @return
     */
    public List<N33_TeaEntry> getTeaList(ObjectId sid, ObjectId gid, ObjectId xqid) {
        List<N33_TeaEntry> teaList = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            teaList.add(entry);
        }
        return teaList;
    }

    public List<N33_TeaEntry> getTeaListByUserId(ObjectId userId) {
        List<N33_TeaEntry> teaList = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("uid", userId);

        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            teaList.add(entry);
        }
        return teaList;
    }


    public Map<ObjectId, N33_TeaEntry> getTeaMap(ObjectId xqid, ObjectId sid, BasicDBObject field) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);

        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, field);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> getTeaMap(List<ObjectId> userIds, ObjectId xqid) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> getTeaMapByXQID(List<ObjectId> xqid, ObjectId sid) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, N33_TeaEntry> getTeaMap(List<ObjectId> userIds) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }


    public Map<ObjectId, N33_TeaEntry> getTeaMap(List<ObjectId> userIds,List<ObjectId> cids) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds)).append("xqid", new BasicDBObject(Constant.MONGO_IN, cids));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }
    /**
     * @param userName
     * @param schoolId
     * @param termId
     * @return
     */
    public List<N33_TeaEntry> getTeaList(String userName, ObjectId schoolId, ObjectId termId) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("unm", MongoUtils.buildRegex(userName)).append("xqid", termId).append("sid", schoolId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<N33_TeaEntry> getTeaList(List<ObjectId> userIds, List<ObjectId> xqid) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds)).append("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public void addIsolateTeaEntrys(Collection<N33_TeaEntry> entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, MongoUtils.fetchDBObjectList(entry));
    }

    public void DelList(ObjectId xqid, List<ObjectId> gids, List<ObjectId> subs, List<ObjectId> ids, ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", xqid)
                .append("sid", sid)
                .append("gid", new BasicDBObject(Constant.MONGO_IN, gids))
                .append("sub", new BasicDBObject(Constant.MONGO_IN, subs))
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, ids));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query);
    }

    public void DelList(ObjectId xqid, ObjectId gid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("gid",gid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query);
    }
    public void DelTea(ObjectId id) {
        DBObject query = new BasicDBObject().append(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query);
    }


    public void DelTea(List<ObjectId> id) {
        DBObject query = new BasicDBObject().append(Constant.ID, new BasicDBObject(Constant.MONGO_IN,id));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query);
    }

    public List<N33_TeaEntry> IsolateTeaListByNewCi(ObjectId xqid) {
        List<N33_TeaEntry> entries = new ArrayList<N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public void delTeaByCid(ObjectId xqid) {
        DBObject query = new BasicDBObject("xqid", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query);
    }
    
    //oz
    public Map<ObjectId, N33_TeaEntry> findTeasByST(List<ObjectId> xqid, ObjectId schoolId) {
        Map<ObjectId, N33_TeaEntry> entries = new HashMap<ObjectId, N33_TeaEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));
        query.append("sid", schoolId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Tea, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TeaEntry entry = new N33_TeaEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }
}
