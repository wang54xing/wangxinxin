package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.school.Subject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 学校年级dao
 *
 * @author fourer
 */
public class SchoolDao extends BaseDao {

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addSchoolEntry(SchoolEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public SchoolEntry getSchoolEntry(ObjectId id, DBObject fields) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, fields);
        if (null != dbo) {
            return new SchoolEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public SchoolEntry getOldSchoolEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new SchoolEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 添加一个年级
     *
     * @param id
     * @param grade
     */
    public void addGrade(ObjectId id, Grade grade) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("grs", grade.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    /**
     * 修改字段值
     *
     * @param id
     * @param field
     * @param value
     */
    public void update(ObjectId id, String field, Object value) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(field, value));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    /**
     * 修改一个科目
     * add by miaoqiang
     *
     * @param id
     * @param subject
     */
    public void updateSubject(ObjectId id, Subject subject) {
        DBObject query = new BasicDBObject(Constant.ID, id).append("subs.si", subject.getSubjectId());
        BasicDBObject update = new BasicDBObject("subs.$.nm", subject.getName()).append("subs.$.gis", subject.getGradeIds());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    /**
     * 添加一个科目
     *
     * @param id
     * @param subject
     */
    public void addSubject(ObjectId id, Subject subject) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("subs", subject.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    /**
     * 修改某一个年级的班级
     *
     * @param id
     * @param subjectId
     * @param gradeids
     */
    public void updateGradeForSubject(ObjectId id, ObjectId subjectId, List<ObjectId> gradeids) {
        DBObject query = new BasicDBObject(Constant.ID, id).append("subs.si", subjectId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("subs.$.gis", MongoUtils.convert(gradeids)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }


    /**
     * 修改某一个学校的bind,用于第三方登录
     *
     * @param id
     * @param subjectId
     * @param gradeids
     */
    public void updateBind(ObjectId id, int type, String value) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject baseEntry = new BasicDBObject()
                .append("ty", type)
                .append("bv", value);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("binds", baseEntry));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }


    /**
     * 删除一个科目
     *
     * @param id
     * @param subject
     */
    public void deleteSubject(ObjectId id, Subject subject) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("subs", subject.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    /**
     * 根据学校ID查询学校信息，返回map
     *
     * @param schoolIds
     * @param fields
     * @return
     */
    public Map<ObjectId, SchoolEntry> getSchoolMap(Collection<ObjectId> schoolIds, DBObject fields) {
        Map<ObjectId, SchoolEntry> retMap = new HashMap<ObjectId, SchoolEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, schoolIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            SchoolEntry e = null;
            for (DBObject dbo : list) {
                e = new SchoolEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 给一个年级增加组长
     *
     * @param schoolId 学校ID
     * @param gradeId  年级ID
     * @param leaderId 组长ID
     */
    public void addGradeLeader(ObjectId schoolId, ObjectId gradeId, ObjectId leaderId) {
        DBObject query = new BasicDBObject(Constant.ID, schoolId).append("grs.gid", gradeId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("grs.$.ld", leaderId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    public ObjectId getGradeByZu(ObjectId sid, ObjectId uid) {
        BasicDBObject query = new BasicDBObject(Constant.ID, sid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        ObjectId oid=null;
        if (null != dbo) {
            SchoolEntry schoolEntry= new SchoolEntry((BasicDBObject) dbo);
            for (Grade grade: schoolEntry.getGradeList()){
                if(grade.getLeader()!=null) {
                    if (grade.getLeader().toString().equals(uid.toString())) {
                        oid = grade.getGradeId();
                    }
                }
            }
        }
        return oid;
    }


    /*
    * update subject name
    * */
    public boolean updateSubjectName(ObjectId subjectId, ObjectId schoolId, String newSubjectName) {
        DBObject query = new BasicDBObject(Constant.ID, schoolId).append("sub.si", subjectId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sub.$.nm", newSubjectName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
        return true;
    }

    public void updateById(ObjectId schoolId, Grade grade) {

        DBObject query = new BasicDBObject(Constant.ID, schoolId).append("grs.gid", grade.getGradeId());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("grs.$.nm", grade.getName()).
                        append("grs.$.ty", grade.getGradeType()).
                        append("grs.$.ld", grade.getLeader()).
                        append("grs.$.cld", grade.getCleader()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    /**
     * 临时修改
     *
     * @param schoolId
     * @param gid
     * @param gty
     */
    @Deprecated
    public void updateGradTypeById(ObjectId schoolId, ObjectId gid, int gty) {

        DBObject query = new BasicDBObject(Constant.ID, schoolId).append("grs.gid", gid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("grs.$.ty", gty));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    public void deleteGradeById(ObjectId schoolId, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, schoolId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("grs", new BasicDBObject("gid", gradeId)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, update);
    }

    public void updateSchoolNameAndLevel(ObjectId objectId, String schoolName, int num) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("nm", schoolName).append("sty", num));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, update);
    }

    /*
    *
    * 更新学期
    *
    * */
    public void updateTermType(ObjectId schoolId, int termType) {
        BasicDBObject query = new BasicDBObject(Constant.ID, schoolId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("tt", termType));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, update);
    }

    public List<Grade> findSchoolInfoBySchoolId(ObjectId schoolId) {
        DBObject matchDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject(Constant.ID, schoolId));
        DBObject projectDBO = new BasicDBObject(Constant.MONGO_PROJECT, new BasicDBObject("grs", 1));
        DBObject unbindDBO = new BasicDBObject(Constant.MONGO_UNWIND, "$grs");
        DBObject sortDBO = new BasicDBObject(Constant.MONGO_SORT, new BasicDBObject("grs.ty", Constant.ASC));
        List<Grade> gradeList = new ArrayList<Grade>();
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, matchDBO, projectDBO, unbindDBO, sortDBO);
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject schoolInfo;
            BasicDBObject grade;
            while (iter.hasNext()) {
                schoolInfo = (BasicDBObject) iter.next();
                grade = (BasicDBObject) schoolInfo.get("grs");
                gradeList.add(new Grade(grade));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gradeList;
    }

    public List<SchoolEntry> getSchoolEntryList(List<ObjectId> schoolIds) {
        List<SchoolEntry> retList = new ArrayList<SchoolEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, schoolIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new SchoolEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<SchoolEntry> getSchoolEntryByRegionIdList(List<ObjectId> regionIds) {
        List<SchoolEntry> retList = new ArrayList<SchoolEntry>();
        BasicDBObject query = new BasicDBObject("ir", new BasicDBObject(Constant.MONGO_IN, regionIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new SchoolEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 根据学校名获取学校Id
     *
     * @param name
     * @return
     */
    public ObjectId getSchoolIdByName(String name) {
        DBObject query = new BasicDBObject("nm", name);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new SchoolEntry((BasicDBObject) dbo).getID();
        }
        return null;
    }


    /**
     * 得到学校，用户用户年级升级
     *
     * @param skip
     * @param limit
     * @return
     */
    @Deprecated
    public List<SchoolEntry> getSchoolEntry(int skip, int limit) {
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, new BasicDBObject(), Constant.FIELDS, Constant.MONGO_SORTBY_ASC, skip, limit);
        List<SchoolEntry> schoolEntryList = new ArrayList<SchoolEntry>();
        for (DBObject dbObject : list) {
            SchoolEntry schoolEntry = new SchoolEntry((BasicDBObject) dbObject);
            schoolEntryList.add(schoolEntry);
        }
        return schoolEntryList;
    }


    @Deprecated
    public List<SchoolEntry> getSchoolEntry(Collection<ObjectId> regIds, int skip, int limit) {
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, new BasicDBObject("ir", new BasicDBObject(Constant.MONGO_IN, regIds)), Constant.FIELDS, Constant.MONGO_SORTBY_ASC, skip, limit);
        List<SchoolEntry> schoolEntryList = new ArrayList<SchoolEntry>();
        for (DBObject dbObject : list) {
            SchoolEntry schoolEntry = new SchoolEntry((BasicDBObject) dbObject);
            schoolEntryList.add(schoolEntry);
        }
        return schoolEntryList;
    }

    public List<Grade> findSchoolInfoByParams(List<ObjectId> schoolIds, int gradeType) {
        DBObject matchDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, schoolIds)));
        DBObject projectDBO = new BasicDBObject(Constant.MONGO_PROJECT, new BasicDBObject("grs", 1));
        DBObject unbindDBO = new BasicDBObject(Constant.MONGO_UNWIND, "$grs");
        DBObject sortDBO = new BasicDBObject(Constant.MONGO_SORT, new BasicDBObject("grs.ty", Constant.ASC));
        List<Grade> gradeList = new ArrayList<Grade>();
        AggregationOutput output;
        try {
            if (gradeType == 0) {
                output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, matchDBO, projectDBO, unbindDBO, sortDBO);
            } else {
                DBObject matchDBO1 = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject("grs.ty", gradeType));
                output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, matchDBO, projectDBO, unbindDBO, matchDBO1, sortDBO);
            }
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject schoolInfo;
            BasicDBObject grade;
            while (iter.hasNext()) {
                schoolInfo = (BasicDBObject) iter.next();
                grade = (BasicDBObject) schoolInfo.get("grs");
                gradeList.add(new Grade(grade));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gradeList;
    }


    /**
     * 根据区域获取所有区域内的学校(用于学籍管理教育局角色，由于区域和学校未关联，故不做处理直接查询全部学校)
     *
     * @return
     */
    public List<SchoolEntry> getSchoolEntryListByRegionForEdu() {
        List<SchoolEntry> retList = new ArrayList<SchoolEntry>();
        BasicDBObject query = new BasicDBObject();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new SchoolEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 根据学校名获取学校Ids
     *
     * @param name
     * @return
     */
    public List<ObjectId> getSchoolIdByNames(String name) {
        List<ObjectId> schoolIds = new ArrayList<ObjectId>();
        DBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(name));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            SchoolEntry e = null;
            for (DBObject dbo : list) {
                schoolIds.add(new SchoolEntry((BasicDBObject) dbo).getID());
            }
        }
        return schoolIds;
    }


    /**
     * 根据学校名和初始密码获取学校，用于导出学校成员列表
     *
     * @param name
     * @return
     */
    public SchoolEntry getSchoolEntry(String name, String initPwd) {
        DBObject query = new BasicDBObject("nm", MongoUtils.buildRegex(name));

        if (StringUtils.isNotBlank(initPwd)) {
            query.put("inp", initPwd);
        }
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new SchoolEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据一个绑定查找学校
     *
     * @param id
     * @param type
     * @param value
     * @return
     */
    public SchoolEntry getSchoolEntryByBind(int type, String value) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("ty", type)
                .append("bv", value);
        DBObject query = new BasicDBObject("binds", baseEntry);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new SchoolEntry((BasicDBObject) dbo);
        }
        return null;
    }


    public List<SchoolEntry> getSchoolEntryByRegion(Set<ObjectId> regionIds, Set<ObjectId> noschoolIds, String schoolName, DBObject fields) {
        List<SchoolEntry> retList = new ArrayList<SchoolEntry>();
        BasicDBObject query = new BasicDBObject();
        if (regionIds.size() > 0) {
            query.append("ir", new BasicDBObject(Constant.MONGO_IN, regionIds));
        }
        if (noschoolIds.size() > 0) {
            query.append(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, noschoolIds));
        }
        if (StringUtils.isNotBlank(schoolName)) {
            query.append("nm", MongoUtils.buildRegex(schoolName));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, fields);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new SchoolEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<SchoolEntry> getAllSchoolEntryList(DBObject fields) {
        List<SchoolEntry> retList = new ArrayList<SchoolEntry>();
        BasicDBObject sort = new BasicDBObject("ir", Constant.DESC);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, new BasicDBObject(), fields, sort);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new SchoolEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 仅仅应用于更新学校模块
     *
     * @param id
     * @param field
     * @param value
     */
    @Deprecated
    public void updateMods(int nv, String mods) {
        DBObject query = new BasicDBObject("nv", nv);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("mods", mods));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    @Deprecated
    public void updateModsNoNVValue(String mods) {
        DBObject query = new BasicDBObject("nv", new BasicDBObject("$exists", false));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("mods", mods));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, query, updateValue);
    }

    @Deprecated
    public List<SchoolEntry> getSchoolEntryListBYNv(int nv) {
        List<SchoolEntry> retList = new ArrayList<SchoolEntry>();
        BasicDBObject sort = new BasicDBObject("ir", Constant.DESC);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_NAME, new BasicDBObject("nv", nv), Constant.FIELDS, sort);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new SchoolEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }
}
