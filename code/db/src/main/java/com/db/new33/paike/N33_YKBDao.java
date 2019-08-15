package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.new33.paike.N33_ZKBEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by wang_xinxin on 2018/3/8.
 */
public class N33_YKBDao extends BaseDao {
    /**
     * 保存源课表
     * @param entry
     */
    public void addN33_YKBEntry(N33_YKBEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, entry.getBaseEntry());
    }

    /**
     * 增加多个源课表
     * @param list
     */
    public void addN33_YKBEntrys(Collection<N33_YKBEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, dbObjects);
    }

    /**
     * 更新一条源课表
     * @param YKBEntry
     */
    public void updateN33_YKB(N33_YKBEntry YKBEntry){
        DBObject query =new BasicDBObject(Constant.ID,YKBEntry.getID());
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,YKBEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, updateValue);
    }

    /**
     * 删除一条源课表
     * @param id
     */
    public void deleteN33_YKB(ObjectId id){
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, updateValue);
    }

    /**
     * 物理删除源课表
     * @param id
     */
    public void removeN33_YKBEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query);
    }

    public void removeN33_YKBEntryByY(ObjectId ciId,Integer y) {
        BasicDBObject query = new BasicDBObject("termId",ciId).append("y",y);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query);
    }

    public void removeN33_YKBEntryByXqid(ObjectId xqid,ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("termId",xqid).append("gid",gradeId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query);
    }

    public void removeN33_YKBEntryByXqid(ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("termId",xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query);
    }
    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public N33_YKBEntry getN33_YKBById(ObjectId id){
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        query.append("ir", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new N33_YKBEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public List<N33_YKBEntry> getN33_ZKBByWeek( Integer x, Integer y, ObjectId xqid, ObjectId teaId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("x", x).append("y", y).append("tid", teaId).append("ir",0);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
    public List<N33_YKBEntry> getN33_ZKBByWeekG(Integer x, Integer y, ObjectId xqid, ObjectId jxbId, ObjectId gradeId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("x", x).append("y", y).append("jxbId", new BasicDBObject(Constant.MONGO_NE, jxbId)).append("gid", gradeId).append("ir",0);;
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
    /**
     * 根据ID查询
     * @param
     * @return
     */
    public N33_YKBEntry getN33_YKBByClassRoom(Integer x,Integer y,ObjectId classRoomId,ObjectId ciId){
        BasicDBObject query =new BasicDBObject("x",x).append("y",y).append("clsrmId",classRoomId).append("termId",ciId).append("ir",Constant.ZERO);
        query.append("ir", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new N33_YKBEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public Map<ObjectId, N33_YKBEntry> getYKBMap(Integer x, Integer y, ObjectId ciId) {
        Map<ObjectId,N33_YKBEntry> clsYKBMap = new HashMap<ObjectId, N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("x",x).append("y",y).append("termId",ciId).append("ir",Constant.ZERO);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            N33_YKBEntry entry = new N33_YKBEntry((BasicDBObject) dbo);
            clsYKBMap.put(entry.getClassroomId(),entry);
        }
        return clsYKBMap;
    }

    /**
     * 学历查询所有教室所有年级课表
     * @param termId
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getYKBsByclassRoomIds(ObjectId termId, ObjectId schoolId,List<ObjectId> classRoomIds) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("ir",Constant.ZERO);
        if (classRoomIds!=null && classRoomIds.size()!=0) {
            query.append("clsrmId",new BasicDBObject(Constant.MONGO_IN,classRoomIds));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param termId
     * @param schoolId
     * @param gid
     * @return
     */
    public List<N33_YKBEntry> getYKBsByclassRoomIds(ObjectId termId, ObjectId schoolId, ObjectId gid) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId)
                .append("sid", schoolId)
                .append("ir", Constant.ZERO)
                .append("gid",gid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }


    /**
     * 学历查询所有教室所有年级课表
     * @param termId
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getYKBsByclassRoomIds(ObjectId termId, ObjectId schoolId,List<ObjectId> classRoomIds,List<Integer> xs,List<Integer> ys) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("ir",Constant.ZERO);
        if (classRoomIds!=null && classRoomIds.size()!=0) {
            query.append("clsrmId",new BasicDBObject(Constant.MONGO_IN,classRoomIds));
        }
        if (xs!=null && xs.size()!=0) {
            query.append("x",new BasicDBObject(Constant.MONGO_IN,xs));
        }
        if (ys!=null && ys.size()!=0) {
            query.append("y",new BasicDBObject(Constant.MONGO_IN,ys));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param xqId
     * @param gradeId
     * @param classroomId
     * @param x
     * @param y
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getJXB(ObjectId xqId, ObjectId gradeId, String classroomId, int x, int y, ObjectId schoolId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("gid",gradeId).append("x",x).append("y",y).append("ir",Constant.ZERO);
        if (StringUtils.isNotEmpty(classroomId)) {
            query.append("clsrmId",new ObjectId(classroomId));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;


    }

    public List<N33_YKBEntry> getYKBbyXY(ObjectId xqId, ObjectId gradeId, int x, int y, ObjectId schoolId,Integer isUse) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("x",x).append("y",y).append("ir",Constant.ZERO)
                .append("isUse",isUse);
        if(gradeId!=null){
            query.append("gid",gradeId);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;

    }
    public List<N33_YKBEntry> getYKBbyXY(ObjectId xqId, ObjectId gradeId, int x, int y, ObjectId schoolId,Integer isUse,Integer type) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("x",x).append("y",y).append("ir",Constant.ZERO)
                .append("isUse",isUse).append("type",type);
        if(gradeId!=null){
            query.append("gid",gradeId);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> getYKBbyType(ObjectId xqId, ObjectId schoolId,Integer type) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("type",type);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> getJXB(ObjectId xqId, ObjectId TeaId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("tid",TeaId).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> getJXBByGrade(ObjectId xqId, ObjectId TeaId,ObjectId gid) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("tid",TeaId).append("gid",gid).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
    public List<N33_YKBEntry> getJXB(ObjectId xqId, List<ObjectId> jxbIds) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("jxbId",new BasicDBObject(Constant.MONGO_IN,jxbIds)).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
    public List<N33_YKBEntry> getJXBByTid(ObjectId xqId, ObjectId TeaId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("ntid",TeaId).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> getJXBByTids(ObjectId xqId, List<ObjectId> TeaIds) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("tid",new BasicDBObject(Constant.MONGO_IN,TeaIds)).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> getJXBByTidsOrNtids(ObjectId xqId, List<ObjectId> TeaIds) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("ir",Constant.ZERO);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("tid", new BasicDBObject(Constant.MONGO_IN,TeaIds)));
        values.add(new BasicDBObject("ntid", new BasicDBObject(Constant.MONGO_IN,TeaIds)));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param termId
     * @param gradeId
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getSettledJXBList(ObjectId termId, ObjectId gradeId, ObjectId schoolId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("gid",gradeId).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
    public List<N33_YKBEntry> getSettledJXBList(ObjectId termId, List<ObjectId> tids) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("tid",new BasicDBObject(Constant.MONGO_IN,tids)).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * 
     * @param xqId
     * @param jxbIds
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getYKBEntrysByJXBIds(ObjectId xqId, List<ObjectId> jxbIds, ObjectId schoolId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("jxbId",new BasicDBObject(Constant.MONGO_IN,jxbIds)).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param xqId
     * @param jxbIds
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getYKBEntrysByJXBIdsOrNJxbIds(ObjectId xqId, List<ObjectId> jxbIds, ObjectId schoolId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("jxbId",new BasicDBObject(Constant.MONGO_IN,jxbIds)));
        values.add(new BasicDBObject("nJxbId",new BasicDBObject(Constant.MONGO_IN,jxbIds)));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
    /***
     * 获得某个学期的课表
     * @param xqId
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getYKBEntrysList(ObjectId xqId, ObjectId schoolId, ObjectId gid) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("gid",gid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> getYKBEntrysListOrGidNull(ObjectId xqId, ObjectId schoolId,ObjectId gid) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("gid", gid));
        values.add(new BasicDBObject("gid", null));
        values.add(new BasicDBObject("gid", ""));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /***
     * 获得某个学期的课表
     * @param xqId
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getYKBEntrysListByClassRoomId(ObjectId xqId, ObjectId schoolId,ObjectId gid,ObjectId classRoomId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("gid",gid).append("clsrmId",classRoomId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> getYKBEntrysListByClassRoomId(ObjectId xqId, ObjectId schoolId,ObjectId classRoomId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId", xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("clsrmId",classRoomId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /***
     * 获得某个学期某个组合在某个点的课表
     * @param xqId
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> clearKeBiaoByGroup(ObjectId xqId, ObjectId schoolId,ObjectId gid,Integer x,Integer y,ObjectId zuHeId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("gid",gid).append("x",x).append("y",y).append("zuHeId",zuHeId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> clearTimeCombKeBiao(ObjectId xqId, ObjectId schoolId, ObjectId gid, Integer x, Integer y, int serial) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO)
                .append("gid",gid).append("x",x).append("y",y).append("serial",serial);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public void clearYuanKeBiaoKeShi(ObjectId xqId, ObjectId schoolId, ObjectId gid, List<Integer> types) {
        BasicDBObject query =new BasicDBObject("termId",xqId)
                .append("sid",schoolId)
                .append("ir",Constant.ZERO)
                .append("gid",gid)
                .append("type", new BasicDBObject(Constant.MONGO_IN, types));
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject()
                .append("jxbId", null)
                .append("isUse", 0)
                .append("nJxbId", null)
                .append("subId", null)
                .append("nsubId", null)
                .append("tid", null)
                .append("ntid", null)
                .append("type", 0)
                .append("zuHeId", null)
                .append("serial", 0)
        );
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, updateValue);
    }

    public List<N33_YKBEntry> getYuanKeBiaoEntries(ObjectId xqId, ObjectId schoolId, ObjectId gid, List<Integer> types) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO)
                .append("gid",gid).append("type", new BasicDBObject(Constant.MONGO_IN, types));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> getYuanKeBiaoEntries(ObjectId termId, ObjectId schoolId, ObjectId gid) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId)
                .append("sid", schoolId)
                .append("ir", Constant.ZERO)
                .append("gid",gid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /***
     * 获得某个学期某个组合的课表
     * @param xqId
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> findKeBiaoByGroup(ObjectId xqId, ObjectId schoolId,ObjectId gid,ObjectId zuHeId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("gid",gid).append("zuHeId",zuHeId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public Map<String,List<N33_YKBEntry>> getYKBMapByRoomId(ObjectId xqId, ObjectId schoolId,ObjectId gid){
        Map<String,List<N33_YKBEntry>> result = new HashMap<String, List<N33_YKBEntry>>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("ir",Constant.ZERO).append("gid",gid).append("isUse",1);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            N33_YKBEntry entry = new N33_YKBEntry((BasicDBObject) dbo);
            if(entry.getClassroomId()!=null){
                List<N33_YKBEntry> list = result.get(entry.getClassroomId().toString())==null?new ArrayList<N33_YKBEntry>():result.get(entry.getClassroomId().toString());
                list.add(entry);
                result.put(entry.getClassroomId().toString(),list);
            }
        }
        return result;
    }



    public List<N33_YKBEntry> getYKBEntrysList(ObjectId xqId, ObjectId schoolId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid", schoolId).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param ykbEntry
     * @param orgYkbEntry
     */
    public void updateN33_YKB(N33_YKBEntry ykbEntry, N33_YKBEntry orgYkbEntry) {
        DBObject query =new BasicDBObject(Constant.ID,ykbEntry.getID());
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("subId",orgYkbEntry.getSubjectId()).append("tid",orgYkbEntry.getTeacherId()).append("gid",orgYkbEntry.getGradeId()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, updateValue);
    }

    /**
     *
     * @param ids
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getYKBEntrysByIds(List<ObjectId> ids, ObjectId schoolId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("sid",schoolId).append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids)).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param tercherId
     * @param schoolId
     * @return
     */
    public List<N33_YKBEntry> getYKBEntrysByTids(ObjectId xqId,ObjectId tercherId,ObjectId schoolId) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("tid",tercherId).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param newCiId
     * @param schoolId
     */
    public void deleteN33_YKB(ObjectId newCiId, ObjectId schoolId) {
        DBObject query =new BasicDBObject("termId",newCiId).append("sid",schoolId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, updateValue);
    }

    /**
     *  教室对应源课表
     * @param xqId
     * @param
     * @param classroomIds
     * @param x
     * @param y
     * @param schoolId
     * @return
     */
    public Map<ObjectId,N33_YKBEntry> getJXB(ObjectId xqId, List<ObjectId> classroomIds, int x, int y, ObjectId schoolId) {
        Map<ObjectId,N33_YKBEntry> clsYKBMap = new HashMap<ObjectId, N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("x",x).append("y",y).append("ir",Constant.ZERO);
            query.append("clsrmId",new BasicDBObject(Constant.MONGO_IN,classroomIds));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            N33_YKBEntry entry = new N33_YKBEntry((BasicDBObject) dbo);
            clsYKBMap.put(entry.getClassroomId(),entry);
        }
        return clsYKBMap;
    }

    /**
     *
     * @param jxbId
     * @param x
     * @param y
     * @return
     */
    public List<N33_YKBEntry> getYKBByjxbId(ObjectId jxbId, int x, int y) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query =new BasicDBObject("jxbId",jxbId).append("x",x).append("y",y).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_YKBEntry> getN33_YKBByWeek(List<ObjectId> jiaoList, ObjectId xqid) {
        List<N33_YKBEntry> result = new ArrayList<N33_YKBEntry>();
        BasicDBObject query = new BasicDBObject("jxbId", new BasicDBObject(Constant.MONGO_IN, jiaoList)).append("termId", xqid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_YKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param ykbIds
     * @param schoolId
     * @return
     */
    public Map<ObjectId, N33_YKBEntry> getYKBbyIds(List<ObjectId> ykbIds, ObjectId schoolId) {
        Map<ObjectId, N33_YKBEntry> ykbEntryMap = new HashMap<ObjectId, N33_YKBEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ykbIds)).append("sid", schoolId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_YKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_YKBEntry entry = new N33_YKBEntry((BasicDBObject) dbo);
            ykbEntryMap.put(entry.getID(),entry);
        }
        return ykbEntryMap;
    }
}
