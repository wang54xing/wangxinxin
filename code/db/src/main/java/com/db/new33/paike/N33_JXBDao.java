package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.new33.isolate.SubjectDao;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by wang_xinxin on 2018/3/8.
 */
public class N33_JXBDao extends BaseDao {

    public Integer countTeacherCarryJxbNUM(ObjectId xqid, ObjectId tid, ObjectId gid, int acClassType) {
        BasicDBObject query = new BasicDBObject().append("termId", xqid)
                .append("tid", tid).append("acty", acClassType)
                .append("ir", Constant.ZERO);
                query.append("gid", gid);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query);
    }//done

    public Integer countTeacherCarryJxbNUM(ObjectId xqid, ObjectId tid, Map<ObjectId, Integer> gACTMap) {
        BasicDBObject query = new BasicDBObject().append("termId", xqid)
                .append("tid", tid)
                .append("ir", Constant.ZERO);
        BasicDBList queryList =new BasicDBList();
        for( Map.Entry<ObjectId, Integer> gACT: gACTMap.entrySet()){
            ObjectId gradeId = gACT.getKey();
            int acClassType = gACT.getValue();
            queryList.add(new BasicDBObject("gid", gradeId).append("acty", acClassType));
        }
        query.append(Constant.MONGO_OR, queryList);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query);
    }//done

    /**
     * 保存教学班
     *
     * @param entry
     */
    public void addN33_JXBEntry(N33_JXBEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, entry.getBaseEntry());
    }//done

    /**
     * 增加多个教学班
     *
     * @param list
     */
    public void addN33_JXBEntrys(Collection<N33_JXBEntry> list) {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        if (dbObjects.size() > 0) {
            save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, dbObjects);
        }
    }//done

    /**
     * 更新一条教学班
     *
     * @param jxbEntry
     */
    public void updateN33_JXB(N33_JXBEntry jxbEntry) {
        DBObject query = new BasicDBObject(Constant.ID, jxbEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, jxbEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, updateValue);
    }//done

    public void updateTagStus(ObjectId id, List<ObjectId> tagIds, List<ObjectId> stuIds) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("stuIds", MongoUtils.convert(stuIds))
                .append("tags", MongoUtils.convert(tagIds)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, updateValue);
    }//done

    public void updateTeacherId(ObjectId id, ObjectId tid) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("tid", tid));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, updateValue);
    }//done

    public void updateClassId(ObjectId id, ObjectId clsrmId) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("clsrmId", clsrmId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, updateValue);
    }//done

    public void updateNickName(ObjectId id, String nickname) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nnm", nickname));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, updateValue);
    }//done

    /**
     * 删除一条教学班
     *
     * @param id
     */
    public void deleteN33_JXB(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, updateValue);
    }//done

    /**
     * 物理删除教学班
     *
     * @param id
     */
    public void removeN33_JXBEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query);
    }//done

    public void removeN33_JXBEntry(List<ObjectId> jxbIds) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query);
    }//done

    /**
     * @param id
     * @return
     */
    public N33_JXBEntry getJXBById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_JXBEntry((BasicDBObject) dbo);
        }
        return null;
    }//done


    public void delN33_JXBEntryCiId(ObjectId ciId) {//, int acClassType
        DBObject query = new BasicDBObject("termId", ciId);//.append("acty", acClassType );
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query);
    }// to do

    public void delN33_JXBEntryCiId(ObjectId ciId, ObjectId gradeId, int acClassType) {
        DBObject query = new BasicDBObject("termId", ciId).append("gid",gradeId).append("acty ", acClassType );
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query);
    }//done

    public void delN33_JXBEntryCiId(ObjectId ciId, ObjectId gradeId, List<ObjectId> subId, int acClassType) {
        DBObject query = new BasicDBObject("termId", ciId)
                .append("gid",gradeId)
                .append("subId",new BasicDBObject(Constant.MONGO_NOTIN,subId))
                .append("acty ", acClassType );
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query);
    }//done

    public void delN33_JXBEntryCiIdIn(ObjectId ciId, ObjectId gradeId, List<ObjectId> subId, int acClassType) {
        DBObject query = new BasicDBObject("termId", ciId)
                .append("gid",gradeId)
                .append("subId",new BasicDBObject(Constant.MONGO_IN,subId))
                .append("acty ", acClassType );
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query);
    }//done

//    public Integer countTeacherCarryJxbNUM(ObjectId ciId, ObjectId tid, ObjectId gid) {
//        BasicDBObject query = new BasicDBObject().append("termId", ciId)
//                .append("tid", tid).append("ir", Constant.ZERO);
//        if (gid != null) {
//            query.append("gid", gid);
//        }
//        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query);
//    }

    public N33_JXBEntry getJXBEntry(ObjectId id, List<ObjectId> studentIds, ObjectId ciId) { //getJXBByIdAndStudentIds
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        if (studentIds.size() > 0) {
            query.append("stuIds", new BasicDBObject(Constant.MONGO_IN, studentIds));
        }
        query.append("ir", Constant.ZERO).append("termId", ciId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_JXBEntry((BasicDBObject) dbo);
        }
        return null;
    }//done

    /**
     * @param nickName
     * @return
     */
    public N33_JXBEntry getJXBEntry(String nickName, ObjectId ciId, ObjectId gradeId, int acClassType) {
        BasicDBObject query = new BasicDBObject("nnm", nickName);
        query.append("ir", Constant.ZERO)
                .append("termId", ciId)
                .append("gid",gradeId)
                .append("acty", acClassType);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_JXBEntry((BasicDBObject) dbo);
        }
        return null;
    }//done

    public N33_JXBEntry getJXBEntry(String name, ObjectId ciId, int acClassType) {
        BasicDBObject query = new BasicDBObject("nm", name).append("acty", acClassType);
        query.append("ir", Constant.ZERO).append("termId", ciId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_JXBEntry((BasicDBObject) dbo);
        }
        return null;
    }//done

    public N33_JXBEntry getJXBEntry(ObjectId id, List<ObjectId> studentIds, ObjectId subjectId, ObjectId ciId, int acClassType) {
        BasicDBObject query = new BasicDBObject("subId", subjectId)
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_NE, id));
        if (studentIds.size() > 0) {
            query.append("stuIds", new BasicDBObject(Constant.MONGO_IN, studentIds));
        }
        query.append("ir", Constant.ZERO).append("termId", ciId).append("acty", acClassType);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_JXBEntry((BasicDBObject) dbo);
        }
        return null;
    }//done


    public Map<ObjectId, N33_JXBEntry> getJXBMapByTermId(ObjectId ciId, int acClassType) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_JXBEntry((BasicDBObject) dbo).getID(), new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    public Map<ObjectId, N33_JXBEntry> getDanShuangJXBMap(ObjectId ciId, int acClassType) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        List<Integer> dos = new ArrayList<Integer>();
        dos.add(1);
        dos.add(2);
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("dos", new BasicDBObject(Constant.MONGO_IN, dos))
                .append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_JXBEntry((BasicDBObject) dbo).getID(), new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    public Map<ObjectId, N33_JXBEntry> getDanShuangJXBMap(ObjectId ciId, List<ObjectId> subjectIds, int acClassType) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        List<Integer> dos = new ArrayList<Integer>();
        dos.add(1);
        dos.add(2);
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("dos", new BasicDBObject(Constant.MONGO_IN, dos)).
                append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds))
                .append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_JXBEntry((BasicDBObject) dbo).getID(), new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     * 查詢沒有刪除的教学班
     * @param jxbIds
     * @return
     */
    public Map<ObjectId, N33_JXBEntry> getJXBMapsByIds(List<ObjectId> jxbIds) {
        Map<ObjectId, N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_JXBEntry jxbEntry = new N33_JXBEntry((BasicDBObject) dbo);
            jxbEntryMap.put(jxbEntry.getID(), jxbEntry);
        }
        return jxbEntryMap;
    }//done

    /**
     * 查询所有的教学班
     * @param jxbIds
     * @return
     */
    public Map<ObjectId, N33_JXBEntry> getJXBMapsByIdsV1(List<ObjectId> jxbIds) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, jxbIds));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.put(new N33_JXBEntry((BasicDBObject) dbo).getID(), new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     * @param ciIds
     * @return
     */
    public Map<ObjectId, N33_JXBEntry> getJXBMapsByCiIds(List<ObjectId> ciIds) {//, int acClassType .append("acty", acClassType)
        Map<ObjectId, N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, ciIds)).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_JXBEntry jxbEntry = new N33_JXBEntry((BasicDBObject) dbo);
            jxbEntryMap.put(jxbEntry.getID(), jxbEntry);
        }
        return jxbEntryMap;
    }// to do

    public Map<ObjectId, N33_JXBEntry> getJXBMap(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int acClassType) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();  
        BasicDBObject query = new BasicDBObject("termId", ciId).append("ir", Constant.ZERO).append("sid", schoolId).append("gid", gradeId).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_JXBEntry jxbEntry = new N33_JXBEntry((BasicDBObject) dbo);
            result.put(jxbEntry.getID(), jxbEntry);
        }
        return result;
    }// done

    public Map<ObjectId, N33_JXBEntry> getJXBMap(ObjectId schoolId, ObjectId ciId) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId).append("sid", schoolId).append("ir", Constant.ZERO);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_JXBEntry jxbEntry = new N33_JXBEntry((BasicDBObject) dbo);
            result.put(jxbEntry.getID(), jxbEntry);
        }
        return result;
    }// to do

    public Map<ObjectId, N33_JXBEntry> getJXBMap(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, List<ObjectId> subId, int acClassType) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("ir", Constant.ZERO)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("subId", new BasicDBObject(Constant.MONGO_IN, subId)).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_JXBEntry jxbEntry = new N33_JXBEntry((BasicDBObject) dbo);
            result.put(jxbEntry.getID(), jxbEntry);
        }
        return result;
    }//done

    /**
     * 本次排课 老师所带的教学班
     * @param ciId
     * @param teaId
     * @return
     */
    public Map<ObjectId, N33_JXBEntry> getJXBMapByCiIdAndTeaId(ObjectId ciId, ObjectId teaId) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("ir", Constant.ZERO)
                .append("tid", teaId);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_JXBEntry jxbEntry = new N33_JXBEntry((BasicDBObject) dbo);
            result.put(jxbEntry.getID(), jxbEntry);
        }
        return result;
    }//done

    /**
     * 专项教学班map
     * @param ciIds
     * @param gid
     * @return
     */
    public Map<ObjectId, N33_JXBEntry> getZxJXBMap(List<ObjectId> ciIds, String gid, int acClassType) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, ciIds))
                .append("ir", Constant.ZERO)
                .append("tp", 4).append("acty", acClassType);
        if (!gid.equals("*")) {
            query.append("gid", new ObjectId(gid));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_JXBEntry jxbEntry = new N33_JXBEntry((BasicDBObject) dbo);
            result.put(jxbEntry.getID(), jxbEntry);
        }
        return result;
    }//done

    /**
     * 专项教学班map
     * @param ciIds
     * @param subId
     * @param gid
     * @return
     */
    public Map<ObjectId, N33_JXBEntry> getZxJXBMapV1(List<ObjectId> ciIds, ObjectId subId, String gid, int acClassType) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, ciIds))
                .append("ir", Constant.ZERO)
                .append("tp", 4)
                .append("subId", subId).append("acty", acClassType);
        if (!gid.equals("*")) {
            query.append("gid", new ObjectId(gid));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_JXBEntry jxbEntry = new N33_JXBEntry((BasicDBObject) dbo);
            result.put(jxbEntry.getID(), jxbEntry);
        }
        return result;
    }//done

    /**
     * 本次排课 学生所在的教学班
     * @param ciId
     * @param stuIds
     * @return
     */
    public Map<ObjectId, N33_JXBEntry> getJXBMapByCiIdAndStuIds(ObjectId ciId, List<ObjectId> stuIds, int acClassType) {
        Map<ObjectId, N33_JXBEntry> result = new HashMap<ObjectId, N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("stuIds", new BasicDBObject(Constant.MONGO_IN, stuIds))
                .append("termId", ciId)
                .append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_JXBEntry jxbEntry = new N33_JXBEntry((BasicDBObject) dbo);
            result.put(jxbEntry.getID(), jxbEntry);
        }
        return result;
    }//done

    /**
     * @param jxbIds
     * @return
     */
    public List<N33_JXBEntry> getJXBListByIds(List<ObjectId> jxbIds) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     *
     * @param jxbIds
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBListByIds(List<ObjectId> jxbIds, ObjectId ciId) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, jxbIds))
                .append("termId", ciId)
                .append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     *
     * @param jxbIds
     * @param type
     * @return
     */
    public List<N33_JXBEntry> getJXBListByIds(List<ObjectId> jxbIds, String type) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject().
                append("_id", new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("ir", Constant.ZERO);
        if (!"0".equals(type)) {
            query.append("tp", Integer.valueOf(type));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     * 根据学期id和年级查询教学班
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int acClassType ) {//
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param subjectId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, ObjectId subjectId, ObjectId ciId, int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("subId", subjectId)
                
                .append("ir", Constant.ZERO).append("acty", acClassType);//
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     *
     * @param gradeId
     * @param classRoomId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBListV1(ObjectId schoolId, ObjectId gradeId, ObjectId classRoomId, ObjectId ciId, int acClassType) {//
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("clsrmId", classRoomId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO)
                .append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     * 老师 所带教学班
     * @param ciIds
     * @param teaId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(List<ObjectId> ciIds, ObjectId teaId) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, ciIds))
                .append("tid", teaId)
                .append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param gradeId
     * @param subId
     * @param ciIds
     * @param teaIds
     * @return
     */
    public List<N33_JXBEntry> getJXBList(String gradeId, ObjectId subId, List<ObjectId> ciIds, List<ObjectId> teaIds , int acClassType) {//
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, ciIds))
                .append("ir", Constant.ZERO)
                .append("tid", new BasicDBObject(Constant.MONGO_IN, teaIds))
                .append("subId", subId).append("acty", acClassType);
        if (!gradeId.equals("*")) {
            query.append("gid", new ObjectId(gradeId));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     *
     * @param ciId
     * @param userId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId ciId, List<ObjectId> userId , int acClassType) {//
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("tid", new BasicDBObject(Constant.MONGO_IN, userId))
                .append("termId", ciId)
                .append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done


    /**
     * 查询走班型教学班
     *
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getZBJXBList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        List<Integer> types = new ArrayList<Integer>();
        types.add(1);
        types.add(2);
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO)
                .append("tp", new BasicDBObject(Constant.MONGO_IN, types)).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     * 学生所在的所有教学班
     * @param gradeId
     * @param ciId
     * @param stuId
     * @return
     */
    public List<N33_JXBEntry> getStuJXBList(ObjectId gradeId, ObjectId ciId, ObjectId stuId, int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("stuIds", stuId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     * 学生所在的走班教学班
     * @param gradeId
     * @param ciId
     * @param stuId
     * @return
     */
    public List<N33_JXBEntry> getStuZBJXBList(ObjectId gradeId, ObjectId ciId, ObjectId stuId, int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        List<Integer> type = new ArrayList<Integer>();
        type.add(1);
        type.add(2);
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("stuIds", stuId)
                .append("gid", gradeId).
                append("tp", new BasicDBObject(Constant.MONGO_IN, type))
                .append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     * 查询学生行政和单双周教学班
     * @param gradeId
     * @param ciId
     * @param stuIds
     * @return
     */
    public List<N33_JXBEntry> getStusXZJXBList(ObjectId gradeId, ObjectId ciId, List<ObjectId> stuIds, int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        List<Integer> type = new ArrayList<Integer>();
        type.add(3);
        type.add(6);
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("stuIds", new BasicDBObject(Constant.MONGO_IN, stuIds))
                .append("gid", gradeId)
                .append("tp", new BasicDBObject(Constant.MONGO_IN, type))
                .append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     * 专项型教学班
     * @param ciIds
     * @return
     */
    public List<N33_JXBEntry> getZXJXBList(List<ObjectId> ciIds) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, ciIds))
                .append("tp", 4)
                .append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// to do

    /**
     *
     * @param gradeId
     * @param subId
     * @param ciIds
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId gradeId, ObjectId subId, List<ObjectId> ciIds,int acClassType) {//
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, ciIds));
        query.append("subId", subId);
        query.append("gid", gradeId);
        query.append("ir", Constant.ZERO).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     * 专项型教学班
     * @param gradeId
     * @param classId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getZXJXBList(ObjectId gradeId, ObjectId classId, ObjectId ciId,int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("gid", gradeId)
                .append("tp", 4)
                .append("ir", Constant.ZERO)
                .append("cls", classId).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param gradeId
     * @param subId
     * @param ciIds
     * @return
     */
    public List<N33_JXBEntry> getZXJXBList(String gradeId, ObjectId subId, List<ObjectId> ciIds,int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", new BasicDBObject(Constant.MONGO_IN, ciIds))
                .append("ir", Constant.ZERO)
                .append("tp", 4)
                .append("subId", subId).append("acty", acClassType);
        if (!gradeId.equals("*")) {
            query.append("gid", new ObjectId(gradeId));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param subjectIds
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, List<ObjectId> subjectIds, ObjectId ciId,int acClassType) {//
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId).
                append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds))
                .append("ir", Constant.ZERO)
                .append("acty", acClassType);

        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done


    /**
     *
     * @param schoolId
     * @param gradeId
     * @param subjectId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, ObjectId subjectId, ObjectId ciId, Integer type, int acClassType) {//
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("subId", subjectId)
                .append("ir", Constant.ZERO)
                .append("acty", acClassType);
        if (type != 0) {
            query.append("tp", type);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//done

    /**
     * 查询非走班型教学班
     *
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getFZBJXBList(ObjectId schoolId, ObjectId gradeId, ObjectId subjectId, ObjectId ciId, int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        List<Integer> types = new ArrayList<Integer>();
        types.add(1);
        types.add(2);
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("subId", subjectId)
                .append("tp", new BasicDBObject(Constant.MONGO_NOTIN, types))
                .append("acty", acClassType)
                .append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int type, int acClassType, int serail) {//
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO)
                .append("acty", acClassType);
        query.append("tp", type);
        if(1==acClassType) {
            query.append("serial", serail);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param subIds
     * @param ciId
     * @param type
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, List<ObjectId> subIds, ObjectId ciId, Integer type, int acClassType) {//, int acClassType
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO).append("acty", acClassType);
        if (subIds.size() > 0) {
            query.append("subId", new BasicDBObject(Constant.MONGO_IN, subIds));
        }
        if (type != 0) {
            query.append("tp", type);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param subjectIds
     * @param ciId
     * @param type
     * @param name
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, List<ObjectId> subjectIds, ObjectId ciId, Integer type, String name, int acClassType) {//, int acClassType
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO)
                .append("tp", type).append("acty", acClassType);
        if (subjectIds.size() > 0) {
            query.append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds));
        }
        if (!name.equals("*")) {
            query.append("nm", MongoUtils.buildRegex(name));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param subjectIds
     * @param roomId
     * @param ciId
     * @param type
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, List<ObjectId> subjectIds, ObjectId roomId, ObjectId ciId, Integer type, int acClassType) {//, int acClassType
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds))
                .append("ir", Constant.ZERO)
                .append("clsrmId", roomId).append("acty", acClassType);
        if (type != 0) {
            query.append("tp", type);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }//  done

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param subjectIds
     * @param teaId
     * @param ciId
     * @param type
     * @return
     */
    public List<N33_JXBEntry> getJXBListV1(ObjectId schoolId, ObjectId gradeId, List<ObjectId> subjectIds, ObjectId teaId, ObjectId ciId, Integer type, int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId).append("sid", schoolId).append("gid", gradeId).
                append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds)).append("ir", Constant.ZERO).append("tid", teaId).append("acty", acClassType);
        if (type != 0) {
            query.append("tp", type);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param subjectIds
     * @param types
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, List<ObjectId> subjectIds, ObjectId ciId, List<Integer> types, int acClassType) {//, int acClassType
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId).append("sid", schoolId).append("gid", gradeId).
                append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds)).append("ir", Constant.ZERO).append("acty", acClassType);
        query.append("tp", new BasicDBObject(Constant.MONGO_IN, types));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param subjectIds
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBsBySubIds(List<ObjectId> subjectIds, ObjectId ciId) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("subId", new BasicDBObject(Constant.MONGO_IN, subjectIds))
                .append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// to do

    /**
     * 根据学期id和年级查询教学班
     *
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, List<Integer> types, int acClassType) {//, int acClassType
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO)
                .append("tp", new BasicDBObject(Constant.MONGO_IN, types)).append("acty", acClassType);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @param type
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, Integer type, int acClassType) {//, int acClassType
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO).append("acty", acClassType);
        if (type != 0) {
            query.append("tp", type);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param schoolId
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId schoolId, ObjectId ciId) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// to do

    /**
     *
     * @param ciId
     * @return
     */
    public List<N33_JXBEntry> getJXBList(ObjectId ciId) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS, new BasicDBObject("gid", 1));
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param gradeId
     * @param roomId
     * @param ciId
     * @param acClassType
     * @return
     */
    public List<N33_JXBEntry> getJxbListByRoomId(ObjectId gradeId, ObjectId roomId, ObjectId ciId, int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("gid", gradeId)
                .append("clsrmId", roomId)
                .append("acty", acClassType)
                .append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param ciId
     * @param id
     * @param reId
     * @return
     */
    public List<N33_JXBEntry> getRaleJXBList(ObjectId ciId, ObjectId id, ObjectId reId) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("rjid", reId).append("termId", ciId).append("_id",new BasicDBObject(Constant.MONGO_NE,id));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// done

    /**
     *
     * @param ciId
     * @param tagList
     * @return
     */
    public List<N33_JXBEntry> getJxbListByTagIds(ObjectId ciId, List<ObjectId> tagList) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("tags", new BasicDBObject(Constant.MONGO_IN, tagList));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }// to do

    public List<N33_JXBEntry> getNoSerialZbJxbList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int type, int acClassType) {
        List<N33_JXBEntry> result = new ArrayList<N33_JXBEntry>();
        BasicDBObject query = new BasicDBObject("termId", ciId)
                .append("sid", schoolId)
                .append("gid", gradeId)
                .append("ir", Constant.ZERO)
                .append("acty", acClassType);
            query.append("tp", type);
        BasicDBList queryList =new BasicDBList();
        queryList.add(new BasicDBObject("serial", new BasicDBObject("$exists", false)));
        queryList.add(new BasicDBObject("serial", new BasicDBObject("$exists", true)).append("serial", Constant.ZERO));
        query.append(Constant.MONGO_OR, queryList);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBEntry((BasicDBObject) dbo));
        }
        return result;
    }



    /*public static void main(String[] args){
        N33_JXBDao jxbDao = new N33_JXBDao();
        ClassDao classDao = new ClassDao();
        SubjectDao subjectDao = new SubjectDao();
        N33_StudentDao studentDao = new N33_StudentDao();
        GradeDao gradeDao = new GradeDao();

        ObjectId sid = new ObjectId("5b9732ef48c5025c78ba1406");
        ObjectId termId = new ObjectId("5b97516246d697466adcca6b");
        ObjectId grade = new ObjectId("5b9752b246d697466ad8f3e7");
        //全部年级的班级
        List<ClassEntry> gradeClass = classDao.findByGradeIdId(sid, grade, termId);
        //全部课时
        List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByXqid(termId, sid, grade);
        //学生列表
        List<StudentEntry> studentList = studentDao.getStudentByXqidAndGradeId(grade, termId);
        Grade grade1 = gradeDao.findIsolateGradeByGradeId(termId, sid, grade);
        int count = 0;
        for (ClassEntry classEntry : gradeClass) {
        count++;
        System.out.println(count);
            //学生列表
            List<ObjectId> studentIds = new ArrayList<ObjectId>();
            for (StudentEntry student : studentList) {
                if (student.getClassId().toString().equals(classEntry.getClassId().toString())) {
                    studentIds.add(student.getUserId());
                }
            }
            for (N33_KSEntry ksEntry : ksEntries) {
                //行政班
                if (ksEntry.getIsZouBan() == 0 && ksEntry.getDan() == 0) {
                    String name = ksEntry.getSubjectName() + grade1.getName() + "(" + classEntry.getXh() + ")";
                    N33_JXBEntry jxbEntry1 = jxbDao.getJXBByName(name,termId);
                    if(jxbEntry1 != null){
                        jxbEntry1.setStudentIds(studentIds);
                        jxbDao.updateN33_JXB(jxbEntry1);
                    }
                }
            }
        }
    }*/
}
