package com.db.new33;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

public class N33_StudentTagDao extends BaseDao {

    public List<N33_StudentTagEntry> getTagListByxqid(ObjectId xqid, ObjectId gradeId) {
        List<N33_StudentTagEntry> retList = new ArrayList<N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("gradeId", gradeId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new N33_StudentTagEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<N33_StudentTagEntry> getFenDuanTagList(ObjectId xqid, ObjectId gradeId, int fenDuan) {
        List<N33_StudentTagEntry> retList = new ArrayList<N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("gradeId", gradeId);
        if(fenDuan!=-1){
            query.append("fenDuan", fenDuan);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new N33_StudentTagEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    public List<N33_StudentTagEntry> getTagListByxqid(ObjectId xqid, ObjectId gradeId, Integer XuNi) {
        List<N33_StudentTagEntry> retList = new ArrayList<N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("gradeId", gradeId).append("xuni", new BasicDBObject(Constant.MONGO_NE,XuNi));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new N33_StudentTagEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<N33_StudentTagEntry> getStudentViewTagByGradeId(ObjectId xqid, ObjectId gradeId) {
        List<N33_StudentTagEntry> retList = new ArrayList<N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("gradeId", gradeId).append("view", 0);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new N33_StudentTagEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<N33_StudentTagEntry> getTagListByIds(List<ObjectId> ids) {
        List<N33_StudentTagEntry> result = new ArrayList<N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_StudentTagEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public Map<ObjectId, N33_StudentTagEntry> getTagListByIdsMap(List<ObjectId> ids) {
        Map<ObjectId, N33_StudentTagEntry> result = new HashMap<ObjectId, N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_StudentTagEntry((BasicDBObject) dbo).getID(), new N33_StudentTagEntry((BasicDBObject) dbo));
        }
        return result;
    }


    public List<N33_StudentTagEntry> getTagList(ObjectId xqid, ObjectId gradeId) {
        List<N33_StudentTagEntry> retList = new ArrayList<N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("gradeId", gradeId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new N33_StudentTagEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public Map<ObjectId, N33_StudentTagEntry> getTagListByIdsMapByCiId(ObjectId ciId,ObjectId gradeId) {
        Map<ObjectId, N33_StudentTagEntry> result = new HashMap<ObjectId, N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject("xqid",ciId).append("gradeId",gradeId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_StudentTagEntry((BasicDBObject) dbo).getID(), new N33_StudentTagEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public N33_StudentTagEntry getTagById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_StudentTagEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /* *
     * 查找一个学生的标签
     * @param xqid
     * @param stuid
     * @return java.util.List<com.pojo.new33.N33_StudentTagEntry>
     */
    public List<N33_StudentTagEntry> getTagListByStuId(ObjectId xqid, ObjectId stuid) {
        List<N33_StudentTagEntry> result = new ArrayList<N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid)
                .append("students", stuid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_StudentTagEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public void addStudentTag(N33_StudentTagEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, entry.getBaseEntry());
    }

    public void update(N33_StudentTagEntry jxbEntry) {
        DBObject query = new BasicDBObject(Constant.ID, jxbEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, jxbEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, updateValue);
    }

    /* *
     *  删除
     *   * @param id
     * @return void
     */
    public void removeById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query);
    }

    /* *
     *  修改名称
     * @param id
     * @param name
     * @return void
     */
    public void updateName(ObjectId id, String name) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("name", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, updateValue);
    }

    public void updateView(List<ObjectId> ids, Integer view) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("view", view));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, updateValue);
    }


    public void updateView(ObjectId xqid, ObjectId gradeId, Integer view) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("gradeId", gradeId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("view", view));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, updateValue);
    }

    public void updateNoView(List<ObjectId> ids, Integer view) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, ids));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("view", view));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, updateValue);
    }

    /* *
     *
     * @param id
     * @param jxbids
     * @return void
     */
    public void updateJxb(ObjectId id, List<ObjectId> jxbids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("jxbIds", MongoUtils.convert(jxbids)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, updateValue);
    }

    public void updateStudent(ObjectId id, List<N33_StudentTagEntry.StudentInfoEntry> studentInfos) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("students", MongoUtils.convert(MongoUtils.fetchDBObjectList(studentInfos))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, updateValue);
    }

    public void add(Collection<N33_StudentTagEntry> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, MongoUtils.fetchDBObjectList(list));
    }

    public List<N33_StudentTagEntry> IsolateN33_JXBEntryByNewCi(ObjectId xqid) {
        List<N33_StudentTagEntry> entries = new ArrayList<N33_StudentTagEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_StudentTagEntry entry = new N33_StudentTagEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public void delN33_JXBEntryCid(ObjectId xqid) {
        DBObject query = new BasicDBObject("xqid", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query);
    }

    public void cleanUpTagEntries(ObjectId schoolId, ObjectId ciId, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gradeId", gradeId);
        query.append("xqid", ciId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_STUDENT_TAG, query);
    }
}
