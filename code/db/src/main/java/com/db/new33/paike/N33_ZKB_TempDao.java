package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.new33.paike.N33_ZKBEntry;
import com.pojo.new33.paike.N33_ZKBEntry_Temp;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by albin on 2018/3/20.
 */
public class N33_ZKB_TempDao extends BaseDao {

    /**
     * 保存周课表临时表
     *
     * @param entry
     */
    public void addN33_ZKBEntry_Temp(N33_ZKBEntry_Temp entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, entry.getBaseEntry());
    }

    /**
     * 批量保存
     *
     * @param entry
     */
    public void addN33_ZKBEntry_TempList(Collection<N33_ZKBEntry_Temp> entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, MongoUtils.fetchDBObjectList(entry));
    }

    /**
     * 更新一条课表
     *
     * @param entry_temp
     */
    public void updateN33_ZKBEntry_Temp(N33_ZKBEntry_Temp entry_temp) {
        DBObject query = new BasicDBObject(Constant.ID, entry_temp.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry_temp.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, updateValue);
    }

    public List<N33_ZKBEntry_Temp> getN33_ZKBEntry_TempByclassRoomIds(ObjectId termId, ObjectId schoolId, Integer week, ObjectId gid) {
        List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO).append("week", week).append("gid", gid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry_Temp((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry_Temp> getN33_ZKBEntry_TempByclassRoomIds(ObjectId termId, ObjectId schoolId, Integer week) {
        List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO).append("week", week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry_Temp((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry_Temp> getYKBsByclassRoomIds(ObjectId termId, ObjectId schoolId, List<ObjectId> classRoomIds, Integer week) {
        List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO).append("week", week);
        if (classRoomIds != null && classRoomIds.size() != 0) {
            query.append("clsrmId", new BasicDBObject(Constant.MONGO_IN, classRoomIds));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry_Temp((BasicDBObject) dbo));
        }
        return result;
    }

    public Map<ObjectId,List<N33_ZKBEntry_Temp>> getN33_ZKB_TempByWeekAndX(Integer week, Integer x, ObjectId xqid, List<ObjectId> classRoomIds) {
        Map<ObjectId,List<N33_ZKBEntry_Temp>> zkbMap = new HashMap<ObjectId, List<N33_ZKBEntry_Temp>>();

        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("x", x).append("clsrmId", new BasicDBObject(Constant.MONGO_NE, classRoomIds));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for (ObjectId classRoomId : classRoomIds) {
            List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
            for (DBObject dbo : dblist) {
                N33_ZKBEntry_Temp zkbEntry = new N33_ZKBEntry_Temp((BasicDBObject) dbo);
                if(zkbEntry.getClassroomId().toString().equals(classRoomId.toString())){
                    result.add(zkbEntry);
                }
            }
            zkbMap.put(classRoomId,result);
        }

        return zkbMap;
    }

    public N33_ZKBEntry_Temp getN33_ZKB_TempById(ObjectId id){
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        query.append("ir", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new N33_ZKBEntry_Temp((BasicDBObject)dbo);
        }
        return null;
    }

    /***
     * 获得某个学期某一周的课表
     * @param xqId
     * @param schoolId
     * @return
     */
    public List<N33_ZKBEntry_Temp> getZKB_TempEntrysList(ObjectId xqId, ObjectId schoolId,ObjectId gid,Integer week) {
        List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
        BasicDBObject query =new BasicDBObject("cid",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("gid",gid).append("week",week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry_Temp((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry_Temp> getZKB_TempEntrysList(ObjectId xqId, ObjectId schoolId,Integer week) {
        List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
        BasicDBObject query =new BasicDBObject("cid",xqId).append("sid", schoolId).append("ir",Constant.ZERO).append("week",week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry_Temp((BasicDBObject) dbo));
        }
        return result;
    }

    /***
     * 获得某个学期的课表
     * @param xqId
     * @param schoolId
     * @return
     */
    public List<N33_ZKBEntry_Temp> getYKBEntrysList(ObjectId xqId, ObjectId schoolId,ObjectId gid,Integer week) {
        List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("gid",gid).append("week",week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry_Temp((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry_Temp> getYKBEntrysList(ObjectId xqId, ObjectId schoolId,Integer week) {
        List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("week",week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry_Temp((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param jxbId
     * @param x
     * @param y
     * @return
     */
    public List<N33_ZKBEntry_Temp> getZKB_TempByjxbId(ObjectId jxbId, int x, int y) {
        List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
        BasicDBObject query =new BasicDBObject("jxbId",jxbId).append("x",x).append("y",y).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry_Temp((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * 更新一条源课表
     * @param zkbEntry_temp
     */
    public void updateN33_ZKB_Temp(N33_ZKBEntry_Temp zkbEntry_temp){
        DBObject query =new BasicDBObject(Constant.ID,zkbEntry_temp.getID());
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,zkbEntry_temp.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, updateValue);
    }

    public List<N33_ZKBEntry_Temp> getZKB_TempEntrysByJXBIdsOrNJxbIds(ObjectId xqId, List<ObjectId> jxbIds, ObjectId schoolId,Integer week) {
        List<N33_ZKBEntry_Temp> result = new ArrayList<N33_ZKBEntry_Temp>();
        BasicDBObject query =new BasicDBObject("cid",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("week",week);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("jxbId",new BasicDBObject(Constant.MONGO_IN,jxbIds)));
        values.add(new BasicDBObject("nJxbId",new BasicDBObject(Constant.MONGO_IN,jxbIds)));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry_Temp((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public N33_ZKBEntry_Temp getN33_ZKBEntry_TempById(ObjectId id){
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        query.append("ir", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new N33_ZKBEntry_Temp((BasicDBObject)dbo);
        }
        return null;
    }

    public Map<ObjectId,N33_ZKBEntry_Temp> getJXB(ObjectId xqId, List<ObjectId> classroomIds, int x, int y, ObjectId schoolId,Integer week) {
        Map<ObjectId,N33_ZKBEntry_Temp> clsYKBMap = new HashMap<ObjectId, N33_ZKBEntry_Temp>();
        BasicDBObject query =new BasicDBObject("cid",xqId).append("sid",schoolId).append("x",x).append("y",y).append("ir",Constant.ZERO).append("week",week);
        query.append("clsrmId",new BasicDBObject(Constant.MONGO_IN,classroomIds));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            N33_ZKBEntry_Temp entry = new N33_ZKBEntry_Temp((BasicDBObject) dbo);
            clsYKBMap.put(entry.getClassroomId(),entry);
        }
        return clsYKBMap;
    }

    public void removeZKBEntry_TempById(ObjectId termId,ObjectId sid,Integer week) {
        BasicDBObject query = new BasicDBObject("week",week).append("sid",sid).append("termId",termId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB_TEMP, query);
    }
}
