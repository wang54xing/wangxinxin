package com.db.new33.isolate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.ClassEntry;
import com.sys.constants.Constant;

public class ClassDao extends BaseDao {

    public void addIsolateClassEntry(List<DBObject> entrys) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, entrys);
    }

    public List<ClassEntry> findClassListByIds(Collection<ObjectId> classIds, ObjectId xqid) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("cid", new BasicDBObject(Constant.MONGO_IN, classIds)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<ClassEntry> getIsolateClassByGrades(List<ObjectId> gradeIds, ObjectId xqid) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("gid", new BasicDBObject(Constant.MONGO_IN, gradeIds)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public Map<ObjectId, ClassEntry> getClassEntryMapByGradeId(List<ObjectId> classIds, ObjectId xqid) {
        Map<ObjectId, ClassEntry> entries = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject("cid", new BasicDBObject(Constant.MONGO_IN, classIds)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
                entries.put(entry.getClassId(), entry);
            }
        }
        return entries;
    }

    public Map<ObjectId, ClassEntry> getClassEntryMapByGradeId(ObjectId gradeId, ObjectId xqid) {
        Map<ObjectId, ClassEntry> entries = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject("gid",gradeId).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
                entries.put(entry.getClassId(), entry);
            }
        }
        return entries;
    }

    public List<ClassEntry> findClassListByUserId(ObjectId userId, ObjectId xqid) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("uis", userId).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<ClassEntry> getClassIdByStuIds(List<ObjectId> classIds, ObjectId xqid, List<ObjectId> userIds) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("cid", new BasicDBObject(Constant.MONGO_IN, classIds)).append("xqid", xqid).append("uis", new BasicDBObject(Constant.MONGO_IN, userIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<ClassEntry> findClassListBySchoolId(ObjectId sid, ObjectId xqid) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<ClassEntry> findByGradeIdId(ObjectId sid, ObjectId gradeId, ObjectId xqid) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid)
                .append("gid", gradeId)
                .append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<ClassEntry> findByGradeIdId(ObjectId sid, ObjectId gradeId, ObjectId xqid,Integer xh) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid)
                .append("gid", gradeId)
                .append("xqid", xqid)
                .append("xh",xh);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<ClassEntry> findByGradeIdIdOrderByXH(ObjectId sid, ObjectId gradeId, ObjectId xqid) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid)
                .append("gid", gradeId)
                .append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS,new BasicDBObject("xh", Constant.ASC));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<ClassEntry> findByGradeIdIdOrderByXH(ObjectId sid, ObjectId gradeId, ObjectId xqid, List<ObjectId> classIds) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid)
                .append("gid", gradeId)
                .append("xqid", xqid)
                .append("cid", new BasicDBObject(Constant.MONGO_IN, classIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS,new BasicDBObject("xh", Constant.ASC));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public Map<ObjectId, ClassEntry> findClassEntryMapBySchoolId(ObjectId sid, ObjectId xqid) {
        Map<ObjectId, ClassEntry> entries = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
                entries.put(entry.getClassId(), entry);
            }
        }
        return entries;
    }

    public Map<Integer, ClassEntry> findClassEntryMapBySchoolIdKeyName(ObjectId sid, ObjectId xqid) {
        Map<Integer, ClassEntry> entries = new HashMap<Integer, ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
                entries.put(entry.getXh(), entry);
            }
        }
        return entries;
    }

    public Map<ObjectId, ClassEntry> getIsolateClassEntry(List<ObjectId> classIds, ObjectId xqid) {
        Map<ObjectId, ClassEntry> entries = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject("cid", new BasicDBObject(Constant.MONGO_IN, classIds)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
                entries.put(entry.getClassId(), entry);
            }
        }
        return entries;
    }

    public Map<ObjectId, ClassEntry> getClassEntryMapByUserIds(List<ObjectId> userIds, ObjectId xqid) {
        Map<ObjectId, ClassEntry> entries = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject("uis", new BasicDBObject(Constant.MONGO_IN, userIds)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
                entries.put(entry.getClassId(), entry);
            }
        }
        return entries;
    }

    public ClassEntry findIsolateClassEntryByRoomId(ObjectId roomId, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("jid", roomId).append("xqid", xqid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        if (dbObject != null) {
            return new ClassEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public ClassEntry findIsolateClassEntryByStuId(ObjectId uid, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("uis", uid).append("xqid", xqid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        if (dbObject != null) {
            return new ClassEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public ClassEntry findIsolateClassEntryByCId(ObjectId cid, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("cid", cid).append("xqid", xqid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        return dbObject == null ? null : new ClassEntry(dbObject);
    }

    public void deleteById(ObjectId id, ObjectId xqid) {
        DBObject query = new BasicDBObject(Constant.ID, id).append("xqid", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query);
    }

    public void deleteBycId(ObjectId cid, ObjectId xqid) {
        DBObject query = new BasicDBObject("cid", cid).append("xqid", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query);
    }

    public void updateIsolateClassEntry(ClassEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, updateValue);
    }

    public ClassEntry findIsolateUserClassByIds(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        if (dbObject != null) {
            return new ClassEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public void addIsolateClassEntry(ClassEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, entry.getBaseEntry());
    }

    /**
     * 情况当前学期数据
     *
     * @param xqid
     */
    public void DelAll(ObjectId xqid) {
        DBObject query = new BasicDBObject("xqid", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query);
    }

    /**
     * 情况当前学期数据
     *
     * @param xqid
     */
    public void DelAllBySID(ObjectId xqid,ObjectId sid) {
        DBObject query = new BasicDBObject("xqid", xqid).append("sid",sid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query);
    }

    /**
     * 情况当前学期数据
     *
     * @param xqid
     */
    public void DelAllByGID(ObjectId xqid,ObjectId sid,ObjectId gradeId) {
        DBObject query = new BasicDBObject("xqid", xqid).append("sid",sid).append("gid",gradeId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query);
    }

    public List<ClassEntry> findClassListByIds(List<ObjectId> classIds, ObjectId xqid) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("cid", new BasicDBObject(Constant.MONGO_IN, classIds)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }


    /**
     * 根据classid查找班级
     *
     * @param id
     * @return
     */
    public ClassEntry findClassEntryByClassId(ObjectId xqid, ObjectId id) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("cid", id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        if (dbObject != null) {
            return new ClassEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }
}
