package com.db.new33.isolate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by albin on 2018/3/16.
 */
public class N33_StudentDao extends BaseDao {

    public void addStudentEntry(StudentEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, entry.getBaseEntry());
    }


    public void add(Collection<StudentEntry> list) {
        if (list.size() > 0) {
            save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, MongoUtils.fetchDBObjectList(list));
        }
    }

    public void removeByCiId(ObjectId sid, ObjectId ciId) {
        DBObject query = new BasicDBObject().append("xqid", ciId).append("sid", sid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query);
    }

    public void removeByCiId(ObjectId sid, ObjectId ciId, ObjectId gid) {
        DBObject query = new BasicDBObject().append("xqid", ciId).append("sid", sid).append("gid",gid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query);
    }


    public StudentEntry findStudent(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        if (dbObject != null) {
            return new StudentEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public StudentEntry findStudent(ObjectId xqid,ObjectId gradeId,String stuName,String stuNo,ObjectId classId) {
        BasicDBObject query = new BasicDBObject().append("gid", gradeId).append("xqid", xqid).append("cid",classId);
        if (!stuNo.trim().equals("")) {
            query.append("sn",stuNo);
        }else{
            query.append("unm", MongoUtils.buildRegex(stuName));
        }
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        if (dbObject != null) {
            return new StudentEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public List<StudentEntry> findStudentExact(ObjectId xqid,ObjectId gradeId,String stuName,String stuNo,ObjectId classId) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("gid", gradeId).append("xqid", xqid).append("cid",classId);
        if (!stuNo.trim().equals("")) {
            query.append("sn",stuNo);
        }else{
            query.append("unm",MongoUtils.buildRegex(stuName));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public StudentEntry findStudent(ObjectId xqid, ObjectId uid) {
        BasicDBObject query = new BasicDBObject("uid", uid).append("xqid", xqid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        if (dbObject != null) {
            return new StudentEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public void updateStudentEntry(StudentEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, updateValue);
    }


    public void updateStudentEntryByClassName(ObjectId xqid, ObjectId cid, String name) {
        DBObject query = new BasicDBObject("cid", cid).append("xqid", xqid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cnm", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, updateValue);
    }

    public void DelStudent(ObjectId id) {
        DBObject query = new BasicDBObject().append(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query);
    }

    public List<StudentEntry> getStudentByXqidAndGradeId(ObjectId gid, ObjectId xqid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStudentByXqidAndStuname(ObjectId xqid, ObjectId gradeId, String username, String combineName) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("unm", MongoUtils.buildRegex(username)).append("xqid", xqid).append("gid", gradeId).append("combiname", combineName);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStudentByXqidAndGradeIdAndCombine(ObjectId gid, ObjectId xqid, String combineName) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("combiname",null));
        values.add(new BasicDBObject("combiname",""));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStudentByXqidAndClassIdAndCombine(ObjectId classId, ObjectId xqid, String combineName) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("cid", classId).append("xqid", xqid);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("combiname",null));
        values.add(new BasicDBObject("combiname",""));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public Integer countStudentByXqidAndGradeId(ObjectId gid, ObjectId xqid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid);
        Integer count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query);
        return count;
    }

    public Integer countStudentByXqidAndGradeIdxuanke(ObjectId gid, ObjectId xqid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("combiname",null));
        values.add(new BasicDBObject("combiname",""));
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid);
        query.put(Constant.MONGO_OR, values);
        Integer count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query);
        return count;
    }

    public Integer countStudentByXqidAndGradeIdxuanke(ObjectId gid, ObjectId xqid,String name) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid).append("combiname",name);
        Integer count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query);
        return count;
    }

    public Integer countStudentByXqidAndClassId(ObjectId classId, ObjectId xqid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("cid", classId).append("xqid", xqid);
        Integer count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query);
        return count;
    }

    public Integer countStudentByXqidAndClassId(ObjectId classId, ObjectId xqid, String combineName) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("cid", classId).append("xqid", xqid);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("combiname",combineName));
        values.add(new BasicDBObject("combiname",""));
        query.put(Constant.MONGO_OR, values);
        Integer count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query);
        return count;
    }

    /**
     * 查找学生（根据姓名模糊查询）
     *
     * @param
     * @param xqid
     * @return
     */
    public List<StudentEntry> getStudentByXqidAndName(ObjectId xqid, ObjectId cid, ObjectId sid, String name) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("cid", cid).append("sid", sid);
        if (name != null && name != "") {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    /**
     * 对应年级某个组合学生
     *
     * @param gid
     * @param xqid
     * @return
     */
    public Map<ObjectId, StudentEntry> getStudentByXqidAndGradeIdMap(ObjectId gid, ObjectId xqid, Integer lev) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid).append("level", lev);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    /**
     * 对应年级某个组合学生
     *
     * @param gid
     * @param xqid
     * @return
     */
    public Map<ObjectId, StudentEntry> getStudentByXqidAndGradeIdMapAll(ObjectId gid, ObjectId xqid, Integer lev) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("level",null));
        values.add(new BasicDBObject("level",lev));
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid);
        if (lev != -1 && lev != 0) {
            query.append("level", lev);
        }else if(lev == 0) {
            query.append(Constant.MONGO_OR,values);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, StudentEntry> getStudentByXqidAndGradeIdMap(ObjectId gid, ObjectId xqid) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    /**
     * 对应年级某个组合学生
     *
     * @param
     * @return
     */
    public Map<String, String> getStudentMap(ObjectId sid) {
        Map<String, String> map = new HashMap<String, String>();
        BasicDBObject query = new BasicDBObject("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            map.put(entry.getStudyNum(), entry.getUserId().toString());
        }
        return map;
    }

    public List<StudentEntry> getStudentByXqidAndGradeId(ObjectId gid, ObjectId xqid, BasicDBObject field) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, field);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStudentByXqidAndClassId(ObjectId classId, ObjectId xqid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("cid", classId).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public Map<ObjectId, StudentEntry> getStudentDtoByClassIdZKBForExport(List<ObjectId> xqid, String classId,ObjectId gid) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = null;
        if(!"*".equals(classId)){
            query = new BasicDBObject("cid", new ObjectId(classId)).append("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("gid", gid);
        }else{
            query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, xqid)).append("gid", gid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, StudentEntry> getStudentByXqidAndClassId(List<ObjectId> xqid, ObjectId classId) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = new BasicDBObject("cid", classId).append("xqid", new BasicDBObject(Constant.MONGO_IN, xqid));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public List<StudentEntry> getStudentByXqidAndClassId(ObjectId classId, ObjectId xqid, List<ObjectId> studentIds) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("cid", classId).append("xqid", xqid);
        if (studentIds.size() > 0) {
            query.append("uid", new BasicDBObject(Constant.MONGO_NOTIN, studentIds));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStudentByXqid(ObjectId classId, ObjectId xqid, List<ObjectId> studentIds) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("cid", classId).append("xqid", xqid).append("uid", new BasicDBObject(Constant.MONGO_IN, studentIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStudentByXqid(ObjectId xqid, List<ObjectId> studentIds) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("xqid", xqid).append("uid", new BasicDBObject(Constant.MONGO_IN, studentIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public Map<ObjectId, StudentEntry> getStudentByXqidMap(ObjectId xqid, List<ObjectId> studentIds) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("xqid", xqid).append("uid", new BasicDBObject(Constant.MONGO_IN, studentIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, StudentEntry> getStudentByXqidMap(ObjectId xqid, List<ObjectId> studentIds, BasicDBObject field) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("xqid", xqid).append("uid", new BasicDBObject(Constant.MONGO_IN, studentIds));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, field);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<String, StudentEntry> getStuMapStuNum(ObjectId xqid) {
        Map<String, StudentEntry> entries = new HashMap<String, StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getStudyNum(), entry);
        }
        return entries;
    }

    public Map<String, StudentEntry> getStuMapStuName(ObjectId xqid) {
        Map<String, StudentEntry> entries = new HashMap<String, StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserName(), entry);
        }
        return entries;
    }

    public Map<ObjectId, StudentEntry> getStuMapIdEntry(ObjectId xqid, BasicDBObject field) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, field);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, StudentEntry> getStuMapIdEntry(ObjectId xqid) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, StudentEntry> getStuMapKeyIsUidByClass(ObjectId xqid, ObjectId classId) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("xqid", xqid).append("cid", classId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public Map<ObjectId, StudentEntry> getStuMap(List<ObjectId> userIds, ObjectId xqid) {
        Map<ObjectId, StudentEntry> entries = new HashMap<ObjectId, StudentEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.put(entry.getUserId(), entry);
        }
        return entries;
    }

    public List<StudentEntry> getStuList(List<ObjectId> userIds, ObjectId xqid, String name) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds)).append("xqid", xqid);
        if (!"*".equals(name)) {
            query.append("unm", MongoUtils.buildRegex(name));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public void updateStuClassId(List<ObjectId> userIds, ObjectId xqid, ObjectId cid) {
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds)).append("xqid", xqid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cid", cid));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, updateValue);
    }

    public List<StudentEntry> getStuList(List<ObjectId> userIds, ObjectId xqid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStuListByUserIds(List<ObjectId> userIds, ObjectId xqid,ObjectId sid) {
        List<com.pojo.new33.isolate.StudentEntry> entries = new ArrayList<com.pojo.new33.isolate.StudentEntry>();
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds)).append("xqid", xqid).append("sid",sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            com.pojo.new33.isolate.StudentEntry entry = new com.pojo.new33.isolate.StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    /**
     * 同步数据调用
     *
     * @param ciId
     * @param sid
     * @return
     */
    public List<StudentEntry> getStuListBySidAndCiId(ObjectId ciId, ObjectId sid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject("xqid", ciId).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStuListByCombineName(ObjectId ciId, ObjectId classId,String combineName) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = null;
        if(classId != null){
            query = new BasicDBObject("xqid", ciId).append("cid", classId).append("combiname",combineName);
        }else{
            query = new BasicDBObject("xqid", ciId).append("combiname",combineName);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public Integer getStudentCount(ObjectId schoolId, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("sid", schoolId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query);
    }

    public void updateStudentSelectSub(ObjectId xqid, ObjectId schoolId, ObjectId userId, ObjectId sub1, ObjectId sub2, ObjectId sub3, String name) {
        BasicDBObject query = new BasicDBObject("sid", schoolId)
                .append("xqid", xqid)
                .append("uid", userId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("subid1", sub1)
                .append("subid2", sub2)
                .append("subid3", sub3)
                .append("combiname", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, updateValue);
    }

    public List<StudentEntry> queryList(String sn, String username, ObjectId classId, ObjectId gradeId, ObjectId xqid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject();
        if (xqid != null) {
            query.append("xqid", xqid);
        }
        if (sn != null) {
            query.append("sn", sn);
        }
        if (username != null && !username.trim().equals("")) {
            query.append("unm", MongoUtils.buildRegex(username));
        }
        if (classId != null) {
            query.append("cid", classId);
        }
        if (gradeId != null) {
            query.append("gid", gradeId);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStu(List<String> username, List<String> userNo, ObjectId xqid, ObjectId gid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("gid", gid).append("xqid", xqid);
        if (userNo.size() > 0) {
            query.append("sn", new BasicDBObject(Constant.MONGO_IN, userNo));
        }
        if(username.size()>0){
            query.append("unm", new BasicDBObject(Constant.MONGO_IN, username));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStu(String username, ObjectId xqid, ObjectId gid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("gid", gid);
        if (xqid != null) {
            query.append("xqid", xqid);
        }
        if (!username.trim().equals("")) {
            query.append("unm", MongoUtils.buildRegex(username));
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStuByCombiname(String combiname, ObjectId xqid, ObjectId gid, ObjectId cid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("gid", gid).append("combiname", combiname);
        if (xqid != null) {
            query.append("xqid", xqid);
        }
        if (cid != null) {
            query.append("cid", cid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStuByGid(ObjectId xqid, ObjectId gid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject().append("gid", gid);
        if (xqid != null) {
            query.append("xqid", xqid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStuListByStuNumAndStuNameAndClass(String sn, String username, ObjectId sid, ObjectId xqid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject();
        if (xqid != null) {
            query.append("xqid", xqid);
        }
        if (!"".equals(sn)) {
            query.append("sn", sn);
        }
        if (!username.trim().equals("")) {
            query.append("unm", MongoUtils.buildRegex(username));
        }
        if (sid != null) {
            query.append("sid", sid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<StudentEntry> getStuListByStuNumAndStuNameAndClassExact(String sn, String username, ObjectId sid, ObjectId xqid) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        BasicDBObject query = new BasicDBObject();
        if (xqid != null) {
            query.append("xqid", xqid);
        }
        if (!"".equals(sn)) {
            query.append("sn", sn);
        }
        if ("".equals(sn)) {
            if (!username.trim().equals("")) {
                query.append("unm", username);
            }
        }
        if (sid != null) {
            query.append("sid", sid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            StudentEntry entry = new StudentEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public void updateStudentSelectById(ObjectId id, Integer level, ObjectId sub1, ObjectId sub2, ObjectId sub3, String name) {
        BasicDBObject query = new BasicDBObject("_id", id);

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("subid1", sub1)
                .append("subid2", sub2)
                .append("subid3", sub3)
                .append("level", level)
                .append("combiname", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_Stu, query, updateValue);
    }

    public StudentEntry getStudentEntry(ObjectId gradeId, ObjectId xqId, ObjectId oldClassId, ObjectId userId) {
        return null;
    }
}
