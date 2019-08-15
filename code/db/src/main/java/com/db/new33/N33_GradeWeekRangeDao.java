package com.db.new33;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

public class N33_GradeWeekRangeDao extends BaseDao{

    public N33_GradeWeekRangeEntry getGradeWeekRangeByXqid(ObjectId xqid, ObjectId sid, ObjectId gid) {

        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        if(gid!=null){
            query.append("gid",gid);
        }
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK, query, Constant.FIELDS);
        if (null != dbObject ) {
            return new N33_GradeWeekRangeEntry((BasicDBObject) dbObject);
        }
        return null;
    }
    public List<N33_GradeWeekRangeEntry> getGradeWeekRangeByXqid(ObjectId xqid, ObjectId sid){
        List<N33_GradeWeekRangeEntry> result = new ArrayList<N33_GradeWeekRangeEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObjects =find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
        for (DBObject dbObject : dbObjects) {
            result.add(new N33_GradeWeekRangeEntry((BasicDBObject) dbObject));
        }
        return result;
    }

    public List<N33_GradeWeekRangeEntry> getGradeWeekRangeByCids(List<ObjectId> cids, ObjectId sid){
        List<N33_GradeWeekRangeEntry> result = new ArrayList<N33_GradeWeekRangeEntry>();
        BasicDBObject query = new BasicDBObject("xqid", new BasicDBObject(Constant.MONGO_IN, cids));
        query.append("sid", sid);
        List<DBObject> dbObjects =find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK, query, Constant.FIELDS,new BasicDBObject("end", Constant.ASC));
        for (DBObject dbObject : dbObjects) {
            result.add(new N33_GradeWeekRangeEntry((BasicDBObject) dbObject));
        }
        return result;
    }

    public void removeByCiId(ObjectId sid,ObjectId xqid){
        DBObject query = new BasicDBObject("xqid", xqid).append("sid",sid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK, query);
    }

    public void addGradeWeekRangeEntry(N33_GradeWeekRangeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK,entry.getBaseEntry());
    }

    public void addGradeWeekRangeEntryList(List<N33_GradeWeekRangeEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK,MongoUtils.fetchDBObjectList(entries));
    }

    public void updateRange(ObjectId id,int start,Integer end){
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject("start", start).append("end",end);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK, query,new BasicDBObject(Constant.MONGO_SET,update));
    }

    public void updateRangeEnd(ObjectId id,Integer end){
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject("end", end);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK, query,new BasicDBObject(Constant.MONGO_SET,update));
    }

    public void updateRangeSetEnd(ObjectId gid,ObjectId sid,ObjectId xqid,int end){
        BasicDBObject query = new BasicDBObject("sid", sid).append("xqid",xqid).append("gid",gid);
        BasicDBObject update = new BasicDBObject("end", end);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK, query,new BasicDBObject(Constant.MONGO_SET,update));
    }

    public Map<String,N33_GradeWeekRangeEntry> getGradeWeekRangeByXqidForMap(ObjectId xqid, ObjectId sid){
        Map<String,N33_GradeWeekRangeEntry> resultMap = new HashMap<String, N33_GradeWeekRangeEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObjects =find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_GRADEWEEK, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
        for (DBObject dbObject : dbObjects) {
            N33_GradeWeekRangeEntry entry = new N33_GradeWeekRangeEntry((BasicDBObject) dbObject);
            resultMap.put(entry.getGradeId().toString(),entry);
        }
        return resultMap;
    }
}
