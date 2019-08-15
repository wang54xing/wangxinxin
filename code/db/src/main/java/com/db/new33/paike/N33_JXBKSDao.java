package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_JXBKSEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 教学班课时
 * Created by wang_xinxin on 2018/3/14.
 */
public class N33_JXBKSDao extends BaseDao {
    /**
     * 保存教学班课时
     * @param entry
     */
    public void addN33_JXBKSEntry(N33_JXBKSEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, entry.getBaseEntry());
    }

    /**
     *
     * @param addEntries
     */
    public void addN33_JXBKSEntries(List<N33_JXBKSEntry> addEntries) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, MongoUtils.fetchDBObjectList(addEntries));
    }

    /**
     *
     * @param list
     */
    public void addN33_JXBKSEntrys(Collection<N33_JXBKSEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, dbObjects);
    }


    public void remove(ObjectId gradeId,ObjectId ciId) {
        BasicDBObject query = new BasicDBObject("gid",gradeId).append("termId",ciId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query);
    }

    /**
     *
     * @param jxbIds
     */
    public void clearJxbKsByJxbIds(List<ObjectId> jxbIds, ObjectId ciId) {
        BasicDBObject query =new BasicDBObject("jxbId", new BasicDBObject(Constant.MONGO_IN,jxbIds));
        query.append("termId", ciId);
        query.append("ir", Constant.ZERO);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ypcnt",0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }

    /**
     * 更新一条教学班课时
     * @param JXBKSEntry
     */
    public void updateN33_JXBKS(N33_JXBKSEntry JXBKSEntry){
        DBObject query =new BasicDBObject(Constant.ID,JXBKSEntry.getID());
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,JXBKSEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }

    public void updateN33_JXBKS(ObjectId xqid,ObjectId gradeId){
        DBObject query =new BasicDBObject("termId",xqid).append("gid",gradeId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ypcnt",0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }


    public void updateN33_JXBKS(ObjectId xqid){
        DBObject query =new BasicDBObject("termId",xqid);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ypcnt",0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }


    public void updateCount(ObjectId xqid,ObjectId jxbId){
        DBObject query =new BasicDBObject("termId",xqid).append("jxbId",jxbId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ypcnt",0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }
    /**
     * 删除一条教学班课时
     * @param id
     */
    public void deleteN33_JXBKS(ObjectId id){
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }

    /**
     * 删除一条教学班课时
     * @param xjbId
     */
    public void deleteN33_JXBKSByJXBId(ObjectId xjbId){
        DBObject query =new BasicDBObject("jxbId",xjbId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }

    /**
     *
     * @param termId
     * @param schoolId
     * @param gradeId
     * @param subjectId
     * @return
     */
    public List<N33_JXBKSEntry> getJXBKSsBySubjectId(ObjectId termId, ObjectId schoolId, ObjectId gradeId, ObjectId subjectId) {
        List<N33_JXBKSEntry> result = new ArrayList<N33_JXBKSEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("gid",gradeId).
                append("subId",subjectId).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_JXBKSEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_JXBKSEntry> getJXBKSsBySubjectIds(ObjectId termId, ObjectId schoolId, ObjectId gradeId, List<ObjectId> subjectIds) {
        List<N33_JXBKSEntry> result = new ArrayList<N33_JXBKSEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("gid",gradeId).
                append("subId",new BasicDBObject(Constant.MONGO_IN,subjectIds)).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_JXBKSEntry((BasicDBObject) dbo));
        }
        return result;
    }
    public List<N33_JXBKSEntry> getJXBKSsBySubjectIds(List<ObjectId> jxbIds,ObjectId xqid) {
        List<N33_JXBKSEntry> result = new ArrayList<N33_JXBKSEntry>();
        BasicDBObject query =new BasicDBObject("jxbId",new BasicDBObject(Constant.MONGO_IN,jxbIds)).append("termId", xqid).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_JXBKSEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public Map<ObjectId, N33_JXBKSEntry> getJXBKSsBySubjectId(List<ObjectId> jxbIds,ObjectId xqid) {
        Map<ObjectId,N33_JXBKSEntry>  result = new HashMap<ObjectId, N33_JXBKSEntry>();
        BasicDBObject query =new BasicDBObject("jxbId",new BasicDBObject(Constant.MONGO_IN,jxbIds)).append("termId", xqid).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            N33_JXBKSEntry entry=  new N33_JXBKSEntry((BasicDBObject) dbo);
            result.put(entry.getJxbId(),entry);
        }
        return result;
    }


    public Map<ObjectId, N33_JXBKSEntry> getJXBKSMap(ObjectId xqid, Collection<ObjectId> jxbIds) {
        Map<ObjectId,N33_JXBKSEntry> result = new HashMap<ObjectId, N33_JXBKSEntry>();
        BasicDBObject query =new BasicDBObject("jxbId",new BasicDBObject(Constant.MONGO_IN,jxbIds)).append("termId", xqid).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            N33_JXBKSEntry entry=  new N33_JXBKSEntry((BasicDBObject) dbo);
            result.put(entry.getJxbId(),entry);
        }
        return result;
    }

    /**
     *
     * @param jxbId
     * @return
     */
    public N33_JXBKSEntry getJXBKSsByJXBId(ObjectId jxbId) {
        BasicDBObject query =new BasicDBObject("jxbId", jxbId);
        query.append("ir", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new N33_JXBKSEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param jxbId
     */
    public void updateN33_JXBKSCountByJxbId(ObjectId jxbId) {
        DBObject query =new BasicDBObject("jxbId",jxbId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_INC,new BasicDBObject("ypcnt",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }

    public void updateN33_JXBKSCountByJxbId(ObjectId jxbId,Integer count) {
        DBObject query =new BasicDBObject("jxbId",jxbId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ypcnt",count));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }
    /**
     *
     * @param jxbId
     */
    public void updateN33_JXBKSMinusCountByJxbId(ObjectId jxbId) {
        DBObject query =new BasicDBObject("jxbId",jxbId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_INC,new BasicDBObject("ypcnt",-1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }

    /**
     *
     * @param newCiId
     * @param schoolId
     * @return
     */
    public List<N33_JXBKSEntry> getJXBKSsByCiId(ObjectId newCiId, ObjectId schoolId) {
        List<N33_JXBKSEntry> result = new ArrayList<N33_JXBKSEntry>();
        BasicDBObject query =new BasicDBObject("termId",newCiId).append("sid",schoolId).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_JXBKSEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param newCiId
     * @param schoolId
     */
    public void deleteN33_JXBKS(ObjectId newCiId, ObjectId schoolId) {
        DBObject query =new BasicDBObject("termId",newCiId).append("sid",schoolId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS, query, updateValue);
    }
}
