package com.db.new33.isolate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.Grade;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
public class GradeDao extends BaseDao {

	public void addIsolateGradeEntry(List<DBObject> entrys) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, entrys);
    }

    public void add(List<Grade> list) {
        if (list.size() > 0) {
            save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, MongoUtils.fetchDBObjectList(list));
        }
    }

    public void removeByCiId(ObjectId sid,ObjectId cid) {
        DBObject query = new BasicDBObject("xqid", cid).append("sid",sid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query);
    }

    public void addIsolateGradeEntry(Grade entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, entry.getBaseEntry());
    }

    public int getCountByXqid(ObjectId xqid, ObjectId sid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query);
    }

    public List<Grade> getIsolateGrADEEntrysByXqid(ObjectId xqid, ObjectId sid) {
        List<Grade> entries = new ArrayList<Grade>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS, new BasicDBObject("ty", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new Grade((BasicDBObject) dbo));
            }
        }
        return entries;
    }


    public List<Grade> getIsolateGrADEEntrys(List<ObjectId> cid, ObjectId sid) {
        List<Grade> entries = new ArrayList<Grade>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN,cid)).append("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS, new BasicDBObject("ty", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new Grade((BasicDBObject) dbo));
            }
        }
        return entries;
    }
    public List<Grade> getIsolateGrADEEntrys(List<ObjectId> cid) {
        List<Grade> entries = new ArrayList<Grade>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN,cid));
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS, new BasicDBObject("ty", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new Grade((BasicDBObject) dbo));
            }
        }
        return entries;
    }
    public List<Grade> getGradeBySid(ObjectId sid) {
        List<Grade> entries = new ArrayList<Grade>();
        BasicDBObject query = new BasicDBObject("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS, new BasicDBObject("ty", 1));
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new Grade((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public Grade getIsolateGrADEEntrysByGid(ObjectId xqid, ObjectId sid, ObjectId gid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS);
        if (null != dbObject) {
            return new Grade((BasicDBObject) dbObject);
        }
        return null;
    }

    public Map<ObjectId, Grade> getIsolateGrADEEntrysByXqid(List<ObjectId> gis, ObjectId xqid) {
        Map<ObjectId, Grade> entries = new HashMap<ObjectId, Grade>();
        BasicDBObject query = new BasicDBObject("gid", new BasicDBObject(Constant.MONGO_IN, gis)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                Grade entry = new Grade((BasicDBObject) dbo);
                entries.put(entry.getGradeId(), entry);
            }
        }
        return entries;
    }

    public List<Grade> getIsolateGrADEEntrysByXqids(List<ObjectId> gis, ObjectId xqid) {
        List<Grade> entries = new ArrayList<Grade>();
        BasicDBObject query = new BasicDBObject("gid", new BasicDBObject(Constant.MONGO_IN, gis)).append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                Grade entry = new Grade((BasicDBObject) dbo);
                entries.add(entry);
            }
        }
        return entries;
    }

    public void updateIsolateUserEntry(Grade entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, updateValue);
    }

    public List<Grade> findIsolateGradeListByIds(Collection<ObjectId> gradeIds, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("gid", new BasicDBObject(Constant.MONGO_IN,gradeIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS);
        List<Grade> gradeList = Lists.newArrayList();
        for (DBObject dbObject : dbObjectList) {
            gradeList.add(new Grade(dbObject));
        }
        return gradeList;
    }

    public Grade findIsolateGradeEntryByGradeId(ObjectId gid, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("gid", gid).append("xqid", xqid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS);
        if (dbObject != null) {
            return new Grade((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public Grade findIsolateGradeByGradeId(ObjectId xqid, ObjectId sid, ObjectId gid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid).append("gid", gid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS);
        if (dbObject != null) {
            return new Grade((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public Grade findClassInfoByClassId(ObjectId xqid, ObjectId cid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("cids", cid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS);
        if (dbObject != null) {
            return new Grade((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public Grade getGradeByUserId(ObjectId xqid, ObjectId uid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("gstu", uid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS);
        if (dbObject != null) {
            return new Grade((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }


    public List<Grade> findGradeListBySchoolId(List<ObjectId> xqid, ObjectId gid) {
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN,xqid)).append("gid", gid);
        BasicDBObject sort = new BasicDBObject("ty", 1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS, sort);
        List<Grade> gradeList = Lists.newArrayList();
        for (DBObject dbObject : dbObjectList) {
            gradeList.add(new Grade(dbObject));
        }
        return gradeList;
    }

    public List<Grade> findGradeListBySchoolId(ObjectId xqid, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", schoolId);
        BasicDBObject sort = new BasicDBObject("ty", 1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS, sort);
        List<Grade> gradeList = Lists.newArrayList();

        for (DBObject dbObject : dbObjectList) {
            gradeList.add(new Grade(dbObject));
        }
        return gradeList;
    }

    public Map<ObjectId,Grade> findGradeListBySchoolIdMap(ObjectId xqid, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", schoolId);
        BasicDBObject sort = new BasicDBObject("ty", 1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS, sort);
        Map<ObjectId,Grade> gradeList = new HashMap<ObjectId, Grade>();

        for (DBObject dbObject : dbObjectList) {
            gradeList.put(new Grade(dbObject).getGradeId(), new Grade(dbObject));
        }
        return gradeList;
    }

    public Map<ObjectId,Grade> findGradeListBySchoolIdMap(List<ObjectId> xqid, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN,xqid)).append("sid", schoolId);
        BasicDBObject sort = new BasicDBObject("ty", 1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS, sort);
        Map<ObjectId,Grade> gradeList = new HashMap<ObjectId, Grade>();

        for (DBObject dbObject : dbObjectList) {
            gradeList.put(new Grade(dbObject).getGradeId(), new Grade(dbObject));
        }
        return gradeList;
    }

    public Map<String,Grade> findGradeListBySchoolIdMapKeyIsName(ObjectId xqid, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", schoolId);
        BasicDBObject sort = new BasicDBObject("ty", 1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query, Constant.FIELDS, sort);
        Map<String,Grade> gradeList = new HashMap<String, Grade>();

        for (DBObject dbObject : dbObjectList) {
            gradeList.put(new Grade(dbObject).getName(), new Grade(dbObject));
        }
        return gradeList;
    }

    public void DelAll(ObjectId xqid) {
        DBObject query = new BasicDBObject("xqid", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_GRADE, query);
    }
}
