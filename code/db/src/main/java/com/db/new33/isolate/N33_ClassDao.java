package com.db.new33.isolate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pojo.new33.isolate.N33_ClassroomEntry;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

public class N33_ClassDao extends BaseDao {

    /**
     * 增加班级
     *
     * @param entry
     */
    public void addClassEntry(ClassEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class,
                entry.getBaseEntry());
    }

    /**
     * 删除班级
     *
     * @param id
     */
    public void DelClass(ObjectId id) {
        DBObject query = new BasicDBObject().append(Constant.ID, id);
        remove(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query);
    }

    /**
     * 根据次id删除班级
     *
     * @param cid
     */
    public void removeByCiId(ObjectId sid,ObjectId cid) {
        DBObject query = new BasicDBObject().append("xqid", cid).append("sid",sid);
        remove(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query);
    }

    /**
     * 修改班级
     *
     * @param entry
     */
    public void updateIsolateClassEntry(ClassEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                entry.getBaseEntry());
        update(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, updateValue);
    }

    /**
     * 查询班级
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<ClassEntry> findClassEntryBySchoolIdAndGradeId(ObjectId xqid,
                                                               ObjectId sid, ObjectId gid) {
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("sid", sid).append("gid", gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("xh", Constant.ASC));
        for (DBObject dbo : dbObject) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            classEntryList.add(entry);
        }
        return classEntryList;
    }
    public List<ClassEntry> findClassEntryBySchoolIdAndGradeId(ObjectId xqid,
                                                               ObjectId sid, ObjectId gid,ObjectId classId) {
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("sid", sid).append("gid", gid);
        if(classId!=null){
            query.append("cid",classId);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("xh", Constant.ASC));
        for (DBObject dbo : dbObject) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            classEntryList.add(entry);
        }
        return classEntryList;
    }
    public List<ClassEntry> getClassEntryByCiId(ObjectId sid, ObjectId xqid) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }

    public List<ClassEntry> findClassEntryBySchoolIdAndGradeId(List<ObjectId> xqid,
                                                               ObjectId sid, ObjectId gid) {
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid))
                .append("sid", sid).append("gid", gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("xh", Constant.ASC));
        for (DBObject dbo : dbObject) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            classEntryList.add(entry);
        }
        return classEntryList;
    }
    public List<ClassEntry> getClassEnties(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, List<ObjectId> stuIds) {
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId)
                .append("gid", gradeId)
                .append("xqid", ciId)
                .append("uis", new BasicDBObject(Constant.MONGO_IN, stuIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS, new BasicDBObject("xh", Constant.ASC));
        for (DBObject dbo : dbObject) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            classEntryList.add(entry);
        }
        return classEntryList;
    }

    /**
     * 查询班级
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<ClassEntry> findClassEntryBySchoolIdAndGradeId(ObjectId xqid,
                                                                 ObjectId sid, ObjectId gid, BasicDBObject field) {
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("sid", sid).append("gid", gid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, field);
        for (DBObject dbo : dbObject) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            classEntryList.add(entry);
        }
        return classEntryList;
    }
    /**
     * 根据学校id查询班级
     *
     * @return
     */
    public Map<ObjectId, ClassEntry> findClassEntry(ObjectId sid) {
        Map<ObjectId, ClassEntry> entries = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            entries.put(entry.getClassId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, ClassEntry> findClassEntryMap(ObjectId gid, ObjectId xqid) {
        Map<ObjectId, ClassEntry> entries = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            entries.put(entry.getClassId(), entry);
        }
        return entries;
    }
    public Map<ObjectId, ClassEntry> findClassEntryMap(ObjectId xqid) {
        Map<ObjectId, ClassEntry> entries = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            entries.put(entry.getClassId(), entry);
        }
        return entries;
    }

    public Map<String, ObjectId> findClassEntryMapForClassNameAndID(ObjectId sid, ObjectId xqid) {
        Map<String, ObjectId> entries = new HashMap<String, ObjectId>();
        BasicDBObject query = new BasicDBObject("sid", sid).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            entries.put(entry.getClassName(), entry.getClassId());
        }
        return entries;
    }

    /**
     * 查询班级存在的数量
     *
     * @param schoolId
     * @param xqid
     * @return
     */
    public Integer getClassCount(ObjectId schoolId, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid",
                schoolId);
        return count(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query);
    }

    public void add(Collection<ClassEntry> list) {
        if (list.size() > 0) {
            save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Class,
                    MongoUtils.fetchDBObjectList(list));
        }
    }

    /**
     * 根据id查找班级
     *
     * @param id
     * @return
     */
    public ClassEntry findClassEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_ISOLATE_Class, query, Constant.FIELDS);
        if (dbObject != null) {
            return new ClassEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
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
