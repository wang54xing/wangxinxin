package com.db.school;

import com.db.base.SynDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.ClassEntry;
import com.pojo.school.InterestClassEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import com.sys.utils.StringUtil;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 班级dao
 * index:sid_gid_ir
 * {"sid":1,"gid":1,"ir":1}
 *
 * @author fourer
 */
public class ClassDao extends SynDao {

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addClassEntry(ClassEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 更新介绍
     *
     * @param id
     * @param introduce
     */
    public void updateIntroduce(ObjectId id, String introduce) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("int", introduce));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }


    /**
     * 更新班主任
     *
     * @param id
     * @param masterid
     */
    public void updateMaster(ObjectId id, ObjectId masterid) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ma", masterid));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }


    /**
     * 更新班级学生数量
     *
     * @param id
     * @param masterid
     */
    public void updateStudentCount(ObjectId id, int count) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ts", count));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }


    /**
     * 更新班级名称
     *
     * @param id
     * @param
     */
    public void updateName(ObjectId id, String name) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nm", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }


    /**
     * 添加一个老师，同时同步到搜索引擎
     *
     * @param id
     * @param teacherId
     */
    public void addTeacher(ObjectId id, ObjectId teacherId) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("teas", teacherId)).append(Constant.MONGO_SET, new BasicDBObject(Constant.FIELD_SYN, Constant.SYN_YES_NEED));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }

    /**
     * 添加一个学生，同时同步到搜索引擎
     *
     * @param id
     * @param studentId
     */
    public void addStudent(ObjectId id, ObjectId studentId) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("stus", studentId))
                .append(Constant.MONGO_SET, new BasicDBObject(Constant.FIELD_SYN, Constant.SYN_YES_NEED))
                .append(Constant.MONGO_INC, new BasicDBObject("ts", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }


    /**
     * 经确定，学生学校班级只有一个
     * 根据学生ID查询
     *
     * @param studentId
     * @param fields
     * @return
     */
    public List<ClassEntry> getClassEntryListByStudentId(ObjectId studentId, DBObject fields) {
        List<ClassEntry> retList = new ArrayList<ClassEntry>();
        DBObject query = new BasicDBObject("stus", studentId).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            ClassEntry e;
            for (DBObject dbo : list) {
                e = new ClassEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 根据id查询，返回map形式
     *
     * @param cos
     * @param fields
     * @return
     */
    public Map<ObjectId, ClassEntry> getClassEntryMap(Collection<ObjectId> cos, DBObject fields) {
        Map<ObjectId, ClassEntry> retMap = new HashMap<ObjectId, ClassEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, cos));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            ClassEntry e;
            for (DBObject dbo : list) {
                e = new ClassEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 详情
     *
     * @param objectId
     * @return
     */
    public ClassEntry getClassEntryById(ObjectId objectId, DBObject fields) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields);
        if (null != dbo) {
            return new ClassEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * classids详情
     *
     * @param objectId
     * @return
     */
    public List<ClassEntry> getClassEntryByIds(Collection<ObjectId> objectId, DBObject fields) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, objectId));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields);
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : list) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }

    /**
     * 通过学生ID查找所在主班级
     *
     * @param studentId
     * @return
     */
    public ClassEntry getClassEntryByStuId(ObjectId studentId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("stus", studentId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields);
        if (null != dbo) {
            return new ClassEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据学校ID查询
     *
     * @param schoolID
     * @return
     */
    public List<ClassEntry> findClassInfoBySchoolId(ObjectId schoolID, DBObject field) {
        BasicDBObject query = new BasicDBObject("sid", schoolID).append("ir", Constant.ZERO);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, field);
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : dbObjects) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }


    public void updateClassNameAndTeacherIdById(ObjectId classId, String classname, ObjectId teacherId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, classId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("nm", classname).append("ma", teacherId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }


    public void deleteById(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }

    public void deleteStuById(ObjectId classId, ObjectId studentId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, classId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("stus", studentId))
                .append(Constant.MONGO_INC, new BasicDBObject("ts", Constant.NEGATIVE_ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, update);
    }


    public void deleteTeacherByIdAndTeacherId(ObjectId classId, ObjectId teacherId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, classId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("teas", teacherId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, update);
    }

    /**
     * 更新年级
     *
     * @param cid
     */
    public void updateGrade(ObjectId cid, ObjectId gid) {
        BasicDBObject query = new BasicDBObject(Constant.ID, cid);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("gid", gid));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }


    /**
     * 根据教师Id找到年级，返回年级列表
     *
     * @param teacherId
     * @param fields
     * @return
     */
    public List<ClassEntry> getClassEntryByTeacherId(ObjectId teacherId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("teas", teacherId));
        values.add(new BasicDBObject("ma", teacherId));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields);
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbo : dbos) {
            classEntryList.add(new ClassEntry((BasicDBObject) dbo));
        }
        return classEntryList;
    }

    /**
     * 通过学生ID查找所在主班级
     *
     * @param stuIds
     * @return
     */
    public List<ClassEntry> getClassEntryByStuIds(List<ObjectId> stuIds, DBObject fields) {
        BasicDBObject query = new BasicDBObject("stus", new BasicDBObject(Constant.MONGO_IN, stuIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields);
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : list) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }
//    public Map<ObjectId,ClassEntry>getClassEntryByStuIdsList(List<ObjectId> stuIds, DBObject fields) {
//        BasicDBObject query = new BasicDBObject("stus", new BasicDBObject(Constant.MONGO_IN, stuIds));
//        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields);
//        Map<ObjectId,ClassEntry> entryMap=new HashMap<ObjectId, ClassEntry>();
//        for (DBObject dbObject : list) {
//            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
//            entryMap.put(classEntry.getID(), classEntry);
//        }
//        return entryMap;
//    }

    public List<ClassEntry> findClassEntryByStuId(ObjectId stuId) {
        BasicDBObject query = new BasicDBObject("stus", stuId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS);
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : list) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;

    }

    /**
     * 根据班主任查找整个年级的班级
     *
     * @param masterId
     * @return
     */
    public List<ClassEntry> findClassEntryByMasterId(ObjectId masterId) {
        BasicDBObject query = new BasicDBObject("ma", masterId).append("ir", 0);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS);
        ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
        List<ClassEntry> classEntryList = findClassEntryByGradeId(classEntry.getGradeId());
        return classEntryList;
    }

    public List<ClassEntry> findClassEntryByGradeId(ObjectId gradeid) {
        BasicDBObject query = new BasicDBObject("gid", gradeid).append("ir", Constant.ZERO);
        BasicDBObject sort = new BasicDBObject("nm", Constant.ASC);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS, sort);

        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : dbObjectList) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }


    public List<ClassEntry> findClassEntryByGradeId(String gradeId, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO);
        if (StringUtil.isEmpty(gradeId)) {
            query.append("gid", new ObjectId(gradeId));
        }
        BasicDBObject sort = new BasicDBObject("nm", Constant.ASC);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS, sort);

        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : dbObjectList) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }

    public List<ClassEntry> getGradeClassesInfo(ObjectId gradeId, ObjectId teacherId) {
        BasicDBObject query = new BasicDBObject("gid", gradeId).append("ir", Constant.ZERO);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("teas", teacherId));
        values.add(new BasicDBObject("ma", teacherId));
        query.put(Constant.MONGO_OR, values);
        BasicDBObject sort = new BasicDBObject("nm", Constant.ASC);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS, sort);
        if (dbObjectList == null || dbObjectList.isEmpty()) return null;
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : dbObjectList) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }


    /**
     * 根据多个年级查询
     *
     * @param gradeid
     * @return
     */
    public List<ClassEntry> findClassEntryByGradeIds(List<ObjectId> gradeid) {

        BasicDBObject query = new BasicDBObject("gid", new BasicDBObject(Constant.MONGO_IN, gradeid)).append("ir", Constant.ZERO);
        BasicDBObject sort = new BasicDBObject("nm", Constant.ASC);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS, sort);
        if (dbObjectList == null || dbObjectList.isEmpty()) return null;
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : dbObjectList) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }

    public List<ClassEntry> findClassEntryByGradeIds(List<ObjectId> gradeid,String name) {

        BasicDBObject query = new BasicDBObject("gid", new BasicDBObject(Constant.MONGO_IN, gradeid)).append("ir", Constant.ZERO);
        if(!name.equals("*")){
            query.append("nm", MongoUtils.buildRegex(name));
        }
        BasicDBObject sort = new BasicDBObject("nm", Constant.ASC);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS, sort);
        if (dbObjectList == null || dbObjectList.isEmpty()) return null;
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : dbObjectList) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }

    /**
     * 根据班级id集合查询年级下的班级
     *
     * @param classids
     * @return
     */
    public List<ClassEntry> findClassByClassIdList(List<ObjectId> classids) {

        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, classids)).append("ir", Constant.ZERO);
        BasicDBObject sort = new BasicDBObject("nm", Constant.ASC);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS, sort);
        if (dbObjectList == null || dbObjectList.isEmpty()) return null;
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : dbObjectList) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }

    /**
     * 查找班主任所在的行政班
     *
     * @param masterId
     * @return
     * @author shanchao
     */
    public List<ClassEntry> findClassByMasterId(ObjectId masterId) {
        BasicDBObject query = new BasicDBObject("ma", masterId).append("ir", 0);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS);
        List<ClassEntry> classEntries = new ArrayList<ClassEntry>();
        if (null != dbObjects) {
            for (DBObject dbObject : dbObjects) {
                classEntries.add(new ClassEntry((BasicDBObject) dbObject));
            }
        }
        return classEntries;
    }

    /**
     * 删除学生数据
     *
     * @param studentId
     */
    @Deprecated
    public void deleteStu(ObjectId studentId) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("stus", studentId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, update);
    }

    /**
     * 删除老师
     *
     * @param teacherId
     */
    @Deprecated
    public void deleteTeacher(ObjectId teacherId) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("teas", teacherId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, update);
    }


    /**
     * 改方法只用于统计使用；
     *
     * @param skip
     * @param limit
     * @return
     */
    @Deprecated
    public List<ClassEntry> getAllEntrys(int skip, int limit) {
        List<ClassEntry> retList = new ArrayList<ClassEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, new BasicDBObject(), new BasicDBObject(), Constant.MONGO_SORTBY_ASC, skip, limit);
        if (dbObjectList != null && dbObjectList.size() > 0) {

            for (DBObject dbo : dbObjectList) {
                ClassEntry classEntry = new ClassEntry((BasicDBObject) dbo);
                retList.add(classEntry);
            }
        }

        return retList;
    }


    public Map<ObjectId, ClassEntry> getClassEntryMapByGradeId(ObjectId gradeId, DBObject fields) {
        Map<ObjectId, ClassEntry> retMap = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject("gid", gradeId).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            ClassEntry e;
            for (DBObject dbo : list) {
                e = new ClassEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    public List<ClassEntry> getClassEntryByParam(ObjectId gradeId, List<ObjectId> noIds, DBObject fields) {
        BasicDBObject query = new BasicDBObject("gid", gradeId).append("ir", Constant.ZERO);
        if (noIds != null && noIds.size() > 0) {
            query.append(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, noIds));
        }
        BasicDBObject sort = new BasicDBObject("nm", Constant.ASC);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, fields, sort);
        if (dbObjectList == null || dbObjectList.isEmpty()) return null;
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        for (DBObject dbObject : dbObjectList) {
            ClassEntry classEntry = new ClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }

    /**
     * 更新学校id
     *
     * @param id
     * @param schoolId
     */
    @Deprecated
    public void updateSchoolGrade(ObjectId id, ObjectId schoolId, ObjectId gradeID) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sid", schoolId).append("gid", gradeID));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, updateValue);
    }

    public List<ClassEntry> getClssByRole(ObjectId schoolId, ObjectId bzj, ObjectId gid, ObjectId te,String name,String nian) {
        List<ClassEntry> retList = new ArrayList<ClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO);
        if(!name.equals("*")){
            query.append("nm", MongoUtils.buildRegex(name));
        }
        if(!nian.equals("*")){
            query.append("gid", new ObjectId(nian));
        }
        if (bzj != null) {
            List<ObjectId> bzrs = new ArrayList<ObjectId>();
            bzrs.add(bzj);
            query.append("ma", new BasicDBObject(Constant.MONGO_IN, bzrs));
        }
        if (gid != null) {
            List<ObjectId> gids = new ArrayList<ObjectId>();
            gids.add(bzj);
            query.append("gid",gid);
        }
        if (te != null) {
            List<ObjectId> tes = new ArrayList<ObjectId>();
            tes.add(te);
            query.append("teas", new BasicDBObject(Constant.MONGO_IN, tes));
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS);
        if (dbObjectList != null && dbObjectList.size() > 0) {

            for (DBObject dbo : dbObjectList) {
                ClassEntry classEntry = new ClassEntry((BasicDBObject) dbo);
                retList.add(classEntry);
            }
        }

        return retList;
    }
}
