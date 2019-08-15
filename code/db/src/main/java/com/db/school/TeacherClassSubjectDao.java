package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.school.TeacherClassSubjectEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 老师班级课程操作类
 * index:ti_cli-id_sui-id
 * {"ti":1,"cli.id":1,"sui.id":1}
 *
 * @author fourer
 */
public class TeacherClassSubjectDao extends BaseDao {

    public ObjectId addTeacherClassSubjectEntry(TeacherClassSubjectEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 通过老师查询
     *
     * @param teacherId 必须
     * @param classIds  班级ID集合
     * @return
     */
    public List<TeacherClassSubjectEntry> getTeacherClassSubjectEntryList(
            ObjectId teacherId, Collection<ObjectId> classIds, DBObject fields) {
        List<TeacherClassSubjectEntry> retList = new ArrayList<TeacherClassSubjectEntry>();
        DBObject query = new BasicDBObject("ti", teacherId);
        if (null != classIds) {
            query.put("cli.id", new BasicDBObject(Constant.MONGO_IN, classIds));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query,
                fields);
        if (null != list && !list.isEmpty()) {
            TeacherClassSubjectEntry e;
            for (DBObject dbo : list) {
                e = new TeacherClassSubjectEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }


    /**
     * 得到老师班级下的科目
     *
     * @param teacherId
     * @return
     */
    public Map<ObjectId, Set<ObjectId>> getClassLessonSet(ObjectId teacherId) {
        Map<ObjectId, Set<ObjectId>> retMap = new HashMap<ObjectId, Set<ObjectId>>();
        DBObject query = new BasicDBObject("ti", teacherId);

        List<DBObject> list = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query,
                Constant.FIELDS);

        if (null != list && !list.isEmpty()) {
            TeacherClassSubjectEntry e;
            for (DBObject dbo : list) {
                e = new TeacherClassSubjectEntry((BasicDBObject) dbo);
                if (!retMap.containsKey(e.getClassInfo().getId())) {
                    retMap.put(e.getClassInfo().getId(), new HashSet<ObjectId>());
                }

                retMap.get(e.getClassInfo().getId()).add(e.getSubjectInfo().getId());
            }
        }
        return retMap;
    }

    public Map<ObjectId, TeacherClassSubjectEntry> getClassLessonSet(List<ObjectId> teacherIds) {
        Map<ObjectId, TeacherClassSubjectEntry> retMap = new HashMap<ObjectId, TeacherClassSubjectEntry>();
        DBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_IN, teacherIds));

        List<DBObject> list = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query,
                Constant.FIELDS);

        if (null != list && !list.isEmpty()) {
            TeacherClassSubjectEntry e;
            for (DBObject dbo : list) {
                e = new TeacherClassSubjectEntry((BasicDBObject) dbo);
                retMap.put(e.getTeacherId(), e);
            }
        }
        return retMap;
    }


    /**
     * 详情
     *
     * @param id
     * @return
     */
    public TeacherClassSubjectEntry getTeacherClassSubjectEntry(ObjectId id) {

        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query,
                Constant.FIELDS);

        if (null != dbo) {
            return new TeacherClassSubjectEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /*
     * 根据老师idlist 和班级id 查找某班级下所有老师所教科目信息
     *
     * */
    public List<TeacherClassSubjectEntry> findSubjectByTeacherIdAndClassId(List<ObjectId> objectIdList, ObjectId objectId) {
        BasicDBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_IN, objectIdList)).append("cli.id", objectId);
        List<DBObject> dbObjectList =
                find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);

        List<TeacherClassSubjectEntry> teacherClassLessonEntryList = new ArrayList<TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.add(teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }

    /*
    *
    * 更新某一班级下老师科目关系
    * */
    public void updateTeacherAndSubject(ObjectId tclId, ObjectId teacherId, ObjectId subjectId, String subjectName) {
        BasicDBObject query = new BasicDBObject(Constant.ID, tclId);
        IdValuePair idValuePair = new IdValuePair(subjectId, subjectName);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ti", teacherId).append("sui", idValuePair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, update);
    }

    /**
     * 更新班级下的老师科目关系，兴趣班使用
     * author:qiangm
     *
     * @param tclId
     * @param teacherId
     * @param subjectId
     * @param subjectName
     * @param classId
     * @param className
     */
    public void updateTeacherAndSubject(ObjectId tclId, ObjectId teacherId, ObjectId subjectId, String subjectName, ObjectId classId, String className) {
        BasicDBObject query = new BasicDBObject(Constant.ID, tclId);
        IdValuePair idValuePair = new IdValuePair(subjectId, subjectName);
        IdValuePair classValuePair = new IdValuePair(classId, className);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ti", teacherId).
                append("sui", idValuePair.getBaseEntry()).
                append("cli", classValuePair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, update);
    }

    /**
     * 查询并返回tcsubject的id
     *
     * @param classId
     * @param teacherId
     * @param subjectId
     * @return
     */
    public ObjectId getTcsubjectId(ObjectId classId, ObjectId teacherId, ObjectId subjectId) {
        BasicDBObject query = new BasicDBObject();
        query.append("ti", teacherId)
                .append("sui.id", subjectId)
                .append("cli.id", classId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);
        if (dbObject != null)
            return new TeacherClassSubjectEntry((BasicDBObject) dbObject).getID();
        return null;
    }

    public void deleteById(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query);
    }

    /**
     * 通过班级ID删除
     *
     * @param classId
     */
    public void removeByClassId(ObjectId classId) {
        BasicDBObject query = new BasicDBObject("cli.id", classId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query);
    }


    /**
     * 通过老师ID查询
     *
     * @param userIds
     * @return
     */
    public List<TeacherClassSubjectEntry> findSubjectByTeacherIds(Collection<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_IN, userIds));
        List<DBObject> dbObjectList =
                find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);

        List<TeacherClassSubjectEntry> teacherClassLessonEntryList = new ArrayList<TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.add(teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }


    public Map<ObjectId,TeacherClassSubjectEntry> findSubjectByTeacherIdst(Collection<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_IN, userIds));
        List<DBObject> dbObjectList =
                find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);

        Map<ObjectId,TeacherClassSubjectEntry>  teacherClassLessonEntryList = new HashMap<ObjectId, TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.put(teacherClassLessonEntry.getTeacherId(),teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }

    public List<TeacherClassSubjectEntry> findEntryByClassId(ObjectId classId) {
        BasicDBObject query = new BasicDBObject("cli.id", classId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);
        List<TeacherClassSubjectEntry> teacherClassLessonEntryList = new ArrayList<TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.add(teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }

    public List<TeacherClassSubjectEntry> findEntryByClassIds(List<ObjectId> classIds) {
        BasicDBObject query = new BasicDBObject("cli.id", new BasicDBObject(Constant.MONGO_IN, classIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);
        List<TeacherClassSubjectEntry> teacherClassLessonEntryList = new ArrayList<TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.add(teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }

    public List<TeacherClassSubjectEntry> findTeacherClassSubjectBySubjectId(ObjectId subjectID) {
        BasicDBObject query = new BasicDBObject("sui.id", subjectID);
        List<DBObject> dbObjectList =
                find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);

        List<TeacherClassSubjectEntry> teacherClassLessonEntryList = new ArrayList<TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.add(teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }


    /**
     * 根据学科集合来查询
     *
     * @param subjectIDs
     * @return
     */
    public List<TeacherClassSubjectEntry> findTeacherClassSubjectBySubjectIds(Collection<ObjectId> subjectIDs, BasicDBObject fields) {
        BasicDBObject query = new BasicDBObject("sui.id", new BasicDBObject(Constant.MONGO_IN, subjectIDs));
        List<DBObject> dbObjectList =
                find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, fields);

        List<TeacherClassSubjectEntry> teacherClassLessonEntryList = new ArrayList<TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.add(teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }


    public void updateClassNameByClassId(ObjectId objectId, String className) {
        BasicDBObject query = new BasicDBObject("cli.id", objectId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cli.v", className));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, update);
    }

    public List<TeacherClassSubjectEntry> findEntryByClassIdAndTeacherId(ObjectId clazzId, ObjectId teacherId) {
        BasicDBObject query = new BasicDBObject("cli.id", clazzId).append("ti", teacherId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);
        List<TeacherClassSubjectEntry> teacherClassLessonEntryList = new ArrayList<TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.add(teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }

    public TeacherClassSubjectEntry findEntryByPrimaryKey(ObjectId teacherClassSubjectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, teacherClassSubjectId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);
        if (dbObject == null) return null;
        return new TeacherClassSubjectEntry((BasicDBObject) dbObject);
    }

    public List<TeacherClassSubjectEntry> findTeacherCLassSubjectByTeacherId(ObjectId teacherId) {
        BasicDBObject query = new BasicDBObject("ti", teacherId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);
        List<TeacherClassSubjectEntry> teacherClassLessonEntryList = new ArrayList<TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.add(teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }

    /**
     * 通过老师查询
     *
     * @param teacherIds 必须
     * @return
     */
    public List<TeacherClassSubjectEntry> getTeacherClassSubjectEntryByUidsList(List<ObjectId> teacherIds, BasicDBObject fields) {
        List<TeacherClassSubjectEntry> retList = new ArrayList<TeacherClassSubjectEntry>();
        DBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_IN, teacherIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query,
                fields);
        if (null != list && !list.isEmpty()) {
            TeacherClassSubjectEntry e;
            for (DBObject dbo : list) {
                e = new TeacherClassSubjectEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 更新班级名字
     *
     * @param classId
     * @param name
     */
    public void updateClassName(ObjectId classId, String name) {
        BasicDBObject query = new BasicDBObject("cli.id", classId);
        IdValuePair idValuePair = new IdValuePair(classId, name);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cli", idValuePair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, update);
    }

    @Deprecated
    /**
     *
     * @param skip
     * @param limit
     * @return
     */
    public List<TeacherClassSubjectEntry> getClassSubjectEntry(int skip, int limit) {
        List<TeacherClassSubjectEntry> retList = new ArrayList<TeacherClassSubjectEntry>();
        DBObject query = new BasicDBObject();
        List<DBObject> list = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_ASC, skip, limit);
        if (null != list && !list.isEmpty()) {
            TeacherClassSubjectEntry e;
            for (DBObject dbo : list) {
                e = new TeacherClassSubjectEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 获取老师代班的集合
     *
     * @param id
     * @return
     */
    public List<TeacherClassSubjectEntry> getTeacherClassSubjectEntry(Collection<ObjectId> id) {
        List<TeacherClassSubjectEntry> teacherClassSubjectEntries = new ArrayList<TeacherClassSubjectEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, id));
        List<DBObject> list = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query,
                Constant.FIELDS);

        if (null != list) {
            for (DBObject dbo : list) {
                teacherClassSubjectEntries.add(new TeacherClassSubjectEntry((BasicDBObject) dbo));
            }
            return teacherClassSubjectEntries;
        }
        return null;
    }

    /**
     * 根据班级和学科获取课
     *
     * @param classId
     * @param subjectId
     * @return
     */
    public TeacherClassSubjectEntry findTeacherClassSubjectByCIdSId(ObjectId classId, ObjectId subjectId) {
        BasicDBObject query = new BasicDBObject("sui.id", subjectId).append("cli.id", classId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new TeacherClassSubjectEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public List<TeacherClassSubjectEntry> findEntryByParams(List<ObjectId> classIds, ObjectId subjectId) {
        BasicDBObject query = new BasicDBObject("sui.id", subjectId);
        if (classIds.size() > 0) {
            query.append("cli.id", new BasicDBObject(Constant.MONGO_IN, classIds));
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);
        List<TeacherClassSubjectEntry> teacherClassLessonEntryList = new ArrayList<TeacherClassSubjectEntry>();
        for (DBObject dbObject : dbObjectList) {
            TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry((BasicDBObject) dbObject);
            teacherClassLessonEntryList.add(teacherClassLessonEntry);
        }
        return teacherClassLessonEntryList;
    }


    /**
     * 查找某一个老师的学科
     *
     * @param teacherID
     * @return
     */
    public TeacherClassSubjectEntry findTeacherClassSubjectByTeacherID(ObjectId teacherID) {
        BasicDBObject query = new BasicDBObject("ti", teacherID);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHERCLASSSUBJECT_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new TeacherClassSubjectEntry((BasicDBObject) dbo);
        }
        return null;
    }

}
