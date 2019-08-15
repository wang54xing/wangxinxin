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
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by albin on 2018/3/20.
 */
public class N33_ZKBDao extends BaseDao {

    /**
     * 保存课表
     *
     * @param entry
     */
    public void addN33_YKBEntry(N33_ZKBEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, entry.getBaseEntry());
    }

    /**
     * 批量保存
     *
     * @param entry
     */
    public void addN33_ZKBEntryList(Collection<N33_ZKBEntry> entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, MongoUtils.fetchDBObjectList(entry));
    }


    /**
     * 更新一条课表
     *
     * @param YKBEntry
     */
    public void updateN33_ZKB(N33_ZKBEntry YKBEntry) {
        DBObject query = new BasicDBObject(Constant.ID, YKBEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, YKBEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);
    }

    public void updateN33_ZKBC(N33_ZKBEntry YKBEntry) {
        DBObject query = new BasicDBObject("clsrmId", YKBEntry.getClassroomId()).append("x", YKBEntry.getX()).append("y", YKBEntry.getY()).append("week", YKBEntry.getWeek());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, YKBEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);
    }


    public void updateN33_ZKBC(ObjectId gid, ObjectId xqid, Integer week) {
        DBObject query = new BasicDBObject("gid", gid).append("week", week).append("termId", xqid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("gid", null).append("tid", null).append("jxbId", null).append("subId", null).append("nsubId", null).append("nJxbId", null).append("ntid", null).append("isUse", 0).append("type", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);
    }

    /**
     * 删除一条课表
     *
     * @param id
     */
    public void deleteN33_ZKB(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);
    }

    public void deleteN33_ZKB(ObjectId gid, ObjectId xqid, Integer week) {
        DBObject query = new BasicDBObject("gid", gid).append("week", week).append("termId", xqid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("gid", null).append("tid", null).append("jxbId", null).append("subId", null).append("nsubId", null).append("nJxbId", null).append("ntid", null).append("isUse", 0).append("type", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);
    }

    /**
     * 物理删除课表
     *
     * @param id
     */
    public void removeN33_ZKBEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query);
    }


    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public N33_ZKBEntry getN33_ZKBById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_ZKBEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public N33_ZKBEntry getN33_ZKBById(ObjectId roomId, ObjectId jxbId, Integer x, Integer y, Integer week) {
        BasicDBObject query = new BasicDBObject("x", x).append("y", y).append("jxbId", jxbId).append("clsrmId", roomId).append("week", week);
        query.append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_ZKBEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public N33_ZKBEntry getN33_ZKBById(ObjectId roomId, ObjectId jxbId, Integer x, Integer y, Integer week,ObjectId termId,ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("x", x).append("y", y).append("jxbId", jxbId).append("clsrmId", roomId).append("week", week).append("termId",termId).append("gid",gradeId);
        query.append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_ZKBEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public N33_ZKBEntry getN33_ZKBById(ObjectId roomId, Integer week, Integer x, Integer y) {
        BasicDBObject query = new BasicDBObject("x", x).append("y", y).append("week", week).append("clsrmId", roomId);
        query.append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_ZKBEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 查询某一周的全校课表
     *
     * @param sid
     * @param week
     * @param xqid
     * @return
     */
    public List<N33_ZKBEntry> getN33_ZKBByWeek(ObjectId sid, Integer week, ObjectId xqid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("sid", sid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekTea(ObjectId xqid, Integer week, ObjectId teaId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("tid", teaId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekTeaCount(ObjectId xqid, Integer week, List<ObjectId> teaList) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("tid", new BasicDBObject(Constant.MONGO_IN, teaList));
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekTeaCount(ObjectId xqid, Integer week, Integer type,ObjectId subId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("type",type).append("subId",subId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekNTeaCount(ObjectId xqid, Integer week, List<ObjectId> teaId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("ntid", new BasicDBObject(Constant.MONGO_IN, teaId));
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekNTeaCount(ObjectId xqid, Integer week, Integer type) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("type",type);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekNTea(ObjectId xqid, Integer week, ObjectId teaId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("ntid", teaId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeek(ObjectId cid, ObjectId xqid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("cid", cid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekList(ObjectId classRoomId, Integer week, ObjectId gradeId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("gid", gradeId).append("week", week).append("clsrmId", classRoomId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekList(ObjectId classRoomId, Integer week, ObjectId gradeId,ObjectId xqid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("gid", gradeId).append("week", week).append("clsrmId", classRoomId).append("termId",xqid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeek(Integer week, Integer x, Integer y, ObjectId xqid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("x", x).append("y", y);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeek(Integer week, Integer x, Integer y, ObjectId xqid, ObjectId teaId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("x", x).append("y", y).append("tid", teaId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeek(Integer week, Integer x, Integer y, ObjectId xqid, ObjectId teaId,ObjectId ciId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("x", x).append("y", y).append("tid", teaId).append("cid",ciId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public Map<ObjectId,List<N33_ZKBEntry>> getN33_ZKBByWeekAndX(Integer week, Integer x, ObjectId xqid, List<ObjectId> classRoomIds) {
        Map<ObjectId,List<N33_ZKBEntry>> zkbMap = new HashMap<ObjectId, List<N33_ZKBEntry>>();

        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("x", x).append("clsrmId", new BasicDBObject(Constant.MONGO_NE, classRoomIds));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (ObjectId classRoomId : classRoomIds) {
            List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
            for (DBObject dbo : dblist) {
                N33_ZKBEntry zkbEntry = new N33_ZKBEntry((BasicDBObject) dbo);
                if(zkbEntry.getClassroomId().toString().equals(classRoomId.toString())){
                    result.add(zkbEntry);
                }
            }
            zkbMap.put(classRoomId,result);
        }

        return zkbMap;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekG(Integer week, Integer x, Integer y, ObjectId xqid, ObjectId jxbId, ObjectId gradeId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("x", x).append("y", y).append("jxbId", new BasicDBObject(Constant.MONGO_NE, jxbId)).append("gid", gradeId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekGrade(ObjectId cid, ObjectId xqid, ObjectId gid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("gid", gid).append("cid", cid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekGrade(ObjectId cid, ObjectId xqid, ObjectId gid, Integer week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("gid", gid).append("cid", cid).append("week", week);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeek(List<ObjectId> jiaoList, Integer week, ObjectId xqid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("jxbId", new BasicDBObject(Constant.MONGO_IN, jiaoList)).append("week", week).append("termId", xqid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekForNJxbId(List<ObjectId> jiaoList, Integer week, ObjectId xqid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("nJxbId", new BasicDBObject(Constant.MONGO_IN, jiaoList)).append("week", week).append("termId", xqid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekTT(List<ObjectId> teaList, Integer week, ObjectId xqid, String gradeId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("tid", new BasicDBObject(Constant.MONGO_IN, teaList)).append("week", week).append("termId", xqid);
        if (!gradeId.equals("*")) {
            query.append("gid", new ObjectId(gradeId));
        }
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekNTT(List<ObjectId> teaList, Integer week, ObjectId xqid, String gradeId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("ntid", new BasicDBObject(Constant.MONGO_IN, teaList)).append("week", week).append("termId", xqid);
        if (!gradeId.equals("*")) {
            query.append("gid", new ObjectId(gradeId));
        }
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeek(List<ObjectId> jiaoList, Integer week, ObjectId xqid, Integer x, Integer y) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("jxbId", new BasicDBObject(Constant.MONGO_IN, jiaoList)).append("week", week).append("termId", xqid).append("x", x).append("y", y);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeek(ObjectId xqid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }


    public List<N33_ZKBEntry> getN33_ZKBByWeek(ObjectId xqid, ObjectId gid, Integer week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("gid", gid).append("week", week);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getJXB(ObjectId xqId, ObjectId TeaId, Integer week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqId).append("tid", TeaId).append("ir", Constant.ZERO).append("week", week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getJXBN(ObjectId xqId, ObjectId TeaId, Integer week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqId).append("ntid", TeaId).append("ir", Constant.ZERO).append("week", week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getJXB(ObjectId xqId, Integer week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqId).append("type", 4).append("ir", Constant.ZERO).append("week", week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public Integer getZKBCount(ObjectId xqId, Integer week) {
        BasicDBObject query = new BasicDBObject("termId", xqId).append("ir", Constant.ZERO).append("week", week);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query);
    }

    public List<N33_ZKBEntry> getYKBsByclassRoomIds(ObjectId termId, ObjectId schoolId, List<ObjectId> classRoomIds, Integer week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO).append("week", week);
        if (classRoomIds != null && classRoomIds.size() != 0) {
            query.append("clsrmId", new BasicDBObject(Constant.MONGO_IN, classRoomIds));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getYKBsByclassRoomIds(ObjectId termId, ObjectId schoolId, List<ObjectId> classRoomIds, Integer week, ObjectId gid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO).append("week", week).append("gid", gid);
        if (classRoomIds != null && classRoomIds.size() != 0) {
            query.append("clsrmId", new BasicDBObject(Constant.MONGO_IN, classRoomIds));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * 查询对应学校对应周对应年级对应点上的周课表
     *
     * @param xqid
     * @param x
     * @param y
     * @param sid
     * @param week
     * @return
     */
    public List<N33_ZKBEntry> getN33_ZKBByWeekAndXY(ObjectId xqid, Integer x, Integer y, ObjectId sid, Integer week, String gradeId, String subId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid).append("ir", Constant.ZERO).append("week", week).append("x", x).append("y", y);
        if (!"*".equals(gradeId)) {
            query.append("gid", new ObjectId(gradeId));
        }
        if (!"*".equals(subId)) {
            query.append("subId", new ObjectId(subId));
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * 查询对应学校对应周对应年级所有点的周课表Map
     *
     * @param xqid
     * @param sid
     * @param week
     * @return
     */
    public Map<String, List<N33_ZKBEntry>> getN33_ZKBByWeekAndXYForMap(ObjectId xqid, ObjectId sid, Integer week, String gradeId) {
        Map<String, List<N33_ZKBEntry>> result = new HashMap<String, List<N33_ZKBEntry>>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid).append("ir", Constant.ZERO).append("week", week);
        if (!"*".equals(gradeId)) {
            query.append("gid", new ObjectId(gradeId));
        }
        /*if(!"*".equals(subId)){
            query.append("subId",new ObjectId(subId));
        }*/
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_ZKBEntry zkbEntry = new N33_ZKBEntry((BasicDBObject) dbo);
            String xy = zkbEntry.getX() + "," + zkbEntry.getY();
            if (result.get(xy) != null) {
                List<N33_ZKBEntry> zkbEntryList = result.get(xy);
                zkbEntryList.add(zkbEntry);
                result.put(xy, zkbEntryList);
            } else {
                List<N33_ZKBEntry> zkbEntryList = new ArrayList<N33_ZKBEntry>();
                zkbEntryList.add(zkbEntry);
                result.put(xy, zkbEntryList);
            }
        }
        return result;
    }

    /**
     * 根据次和周查询周课表
     *
     * @param xqid
     * @param sid
     * @param week
     * @return
     */
    public List<N33_ZKBEntry> getZKBEntryByWeekAndCiId(ObjectId xqid, ObjectId sid, Integer week) {
        List<N33_ZKBEntry> zkbEntryList = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid).append("ir", Constant.ZERO).append("week", week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_ZKBEntry zkbEntry = new N33_ZKBEntry((BasicDBObject) dbo);
            zkbEntryList.add(zkbEntry);
        }
        return zkbEntryList;
    }


    public List<N33_ZKBEntry> getYKBsByclassRoomIds(ObjectId termId, ObjectId schoolId, Integer week, ObjectId gid) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO).append("week", week).append("gid", gid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getYKBsByclassRoomIds(ObjectId termId, ObjectId schoolId, Integer week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO).append("week", week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public void removeKBByWeek(ObjectId sid, ObjectId xqid, Integer week) {
        BasicDBObject query = new BasicDBObject("sid", sid).append("termId", xqid).append("week", week);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query);
    }

    public List<N33_ZKBEntry> getYKBsByclassRoomIds1(ObjectId termId, ObjectId schoolId, Integer week,ObjectId classRoomId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("ir", Constant.ZERO).append("week", week).append("clsrmId",classRoomId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * 查询某一周的年级课表
     *
     * @param sid
     * @param week
     * @param xqid
     * @return
     */
    public List<N33_ZKBEntry> getN33_ZKBByWeekAndGradeId(ObjectId sid, Integer week, ObjectId xqid, ObjectId gradeId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("week", week).append("sid", sid).append("gid", gradeId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekAndGradeIdAndWeek(ObjectId sid, Integer week,ObjectId gradeId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("week", week).append("sid", sid).append("gid", gradeId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekAndGradeIdAndWeekAndTermId(ObjectId sid, Integer week,ObjectId gradeId,ObjectId termId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("week", week).append("sid", sid).append("gid", gradeId).append("termId",termId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * 取消发布
     *
     * @param sid
     * @param xqid
     */
    public void removeKBByXqid(ObjectId sid, ObjectId xqid, Integer week, ObjectId gid) {
        BasicDBObject query = new BasicDBObject("sid", sid).append("termId", xqid).append("week", week).append("gid", gid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query);
    }

    public void removeKBByXqidByXueQi(ObjectId sid, ObjectId xqid, Integer week, ObjectId gid) {
        BasicDBObject query = new BasicDBObject("sid", sid).append("termId", xqid).append("week", week).append("gid", gid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("gid", null).append("tid", null).append("jxbId", null).append("subId", null).append("nsubId", null).append("nJxbId", null).append("ntid", null).append("isUse", 0).append("type", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);
    }

    public void deleteKBByXqidByXueQi(ObjectId sid, ObjectId xqid, Integer week, ObjectId gid) {
        BasicDBObject query = new BasicDBObject("sid", sid).append("termId", xqid).append("week", week).append("gid", gid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query);
    }

    public void deleteKBByXqidByXueQiAndGidNull(ObjectId sid, ObjectId xqid, Integer week, ObjectId gid) {
        BasicDBObject query = new BasicDBObject("sid", sid).append("termId", xqid).append("week", week);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("gid", gid));
        values.add(new BasicDBObject("gid", null));
        values.add(new BasicDBObject("gid", ""));
        query.put(Constant.MONGO_OR, values);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query);
    }




    /**
     * @param zkbEntry
     * @param orgZkbEntry
     */
    public void updateN33_ZKB(N33_ZKBEntry zkbEntry, N33_ZKBEntry orgZkbEntry) {
        DBObject query = new BasicDBObject(Constant.ID, zkbEntry.getID());
        BasicDBObject value = new BasicDBObject("subId", orgZkbEntry.getSubjectId()).append("tid", orgZkbEntry.getTeacherId()).append("gid", orgZkbEntry.getGradeId()).append("jxbId", orgZkbEntry.getJxbId()).append("type",orgZkbEntry.getType());
        if (orgZkbEntry.getJxbId() == null) {
            value.append("isUse", 0);
        }
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);
    }

    public void updateN33_ZKBByWeekXY(N33_ZKBEntry zkbEntry, int week, N33_ZKBEntry orgZkbEntry) {
        DBObject query = new BasicDBObject("x", zkbEntry.getX()).append("y", zkbEntry.getY()).append("week", week).append("termId", zkbEntry.getTermId()).append("clsrmId",zkbEntry.getClassroomId());
        BasicDBObject value = new BasicDBObject("subId", orgZkbEntry.getSubjectId()).append("tid", orgZkbEntry.getTeacherId()).append("gid", orgZkbEntry.getGradeId())
                .append("jxbId", orgZkbEntry.getJxbId()).append("type",orgZkbEntry.getType()).append("cid",orgZkbEntry.getCId()).
                        append("nJxbId",orgZkbEntry.getNJxbId()).append("nsubId",orgZkbEntry.getNSubjectId())
                .append("ntid",orgZkbEntry.getNTeacherId()).append("us",orgZkbEntry.getUserId()).append("time",orgZkbEntry.getTime());
        if (orgZkbEntry.getJxbId() == null) {
            value.append("isUse", 0);
        }
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);

    }

    /**
     * @param termId
     * @param jxbIds
     * @param schoolId
     * @return
     */
    public List<N33_ZKBEntry> getZKBEntrysByJXBIds(ObjectId termId, List<ObjectId> jxbIds, ObjectId schoolId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("jxbId", new BasicDBObject(Constant.MONGO_IN, jxbIds));
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * @param termId
     * @param jxbIds
     * @param schoolId
     * @return
     */
    public List<N33_ZKBEntry> getZKBEntrysByJXBIds(ObjectId termId, List<ObjectId> jxbIds, ObjectId schoolId,ObjectId classroomID,int week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("jxbId", new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("clsrmId",classroomID).append("week",week);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * @param schoolId
     * @param week
     * @param termId
     * @param teacherId
     * @param x
     * @param y
     * @return
     */
    public List<N33_ZKBEntry> getN33_ZKBByWeekTid(ObjectId schoolId, int week, ObjectId termId, ObjectId teacherId, int x, int y) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("week", week).append("sid", schoolId).append("tid", teacherId).append("x", x).append("y", y);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * @param zkbEntry
     * @param week
     * @param teacherId
     */
    public void updateN33_ZKB(N33_ZKBEntry zkbEntry, int week, ObjectId teacherId) {
        DBObject query = new BasicDBObject("x", zkbEntry.getX()).append("y", zkbEntry.getY()).append("week", week).append("termId", zkbEntry.getTermId()).append("clsrmId", zkbEntry.getClassroomId());
        BasicDBObject value = new BasicDBObject("tid", teacherId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);
    }

    public void updateN33_ZKB(N33_ZKBEntry zkbEntry, ObjectId teacherId,int i) {
        DBObject query = new BasicDBObject(Constant.ID, zkbEntry.getID());
        BasicDBObject value = new BasicDBObject(i==1?"tid":"ntid", teacherId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, updateValue);
    }

    /**
     * @param zkbEntry
     * @param teacherId
     * @param week
     * @return
     */
    public List<N33_ZKBEntry> getN33_ZKBByWeekTid(N33_ZKBEntry zkbEntry, ObjectId teacherId, int week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("clsrmid", zkbEntry.getClassroomId()).append("sid", zkbEntry.getSchoolId()).append("tid", teacherId).append("x", zkbEntry.getX()).append("y", zkbEntry.getY());
        if (week == 0) {
            query.append("week", zkbEntry.getWeek());
        } else {
            query.append("week", new BasicDBObject(Constant.MONGO_GTE, week));
        }
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     * @param ykbIds
     * @param schoolId
     * @return
     */
    public Map<ObjectId, N33_ZKBEntry> getZKBbyIds(List<ObjectId> ykbIds, ObjectId schoolId) {
        Map<ObjectId, N33_ZKBEntry> ykbEntryMap = new HashMap<ObjectId, N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ykbIds)).append("sid", schoolId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_ZKBEntry entry = new N33_ZKBEntry((BasicDBObject) dbo);
            ykbEntryMap.put(entry.getID(), entry);
        }
        return ykbEntryMap;
    }

    /**
     * @param ykbIds
     * @param schoolId
     * @return
     */
    public List<N33_ZKBEntry> findZKBbyIds(List<ObjectId> ykbIds, ObjectId schoolId) {
        List<N33_ZKBEntry> zkbEntries = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ykbIds)).append("sid", schoolId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            N33_ZKBEntry entry = new N33_ZKBEntry((BasicDBObject) dbo);
            zkbEntries.add(entry);
        }
        return zkbEntries;
    }

    public List<N33_ZKBEntry> getZKBEntrysByJXBIds(ObjectId termId, ObjectId schoolId, ObjectId classroomId, int week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", termId).append("sid", schoolId).append("clsrmId",classroomId).append("week",week);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param xqId
     * @param gradeId
     * @param x
     * @param y
     * @param schoolId
     * @param isUse
     * @return
     */
    public List<N33_ZKBEntry> getZKBbyXY(ObjectId xqId, ObjectId gradeId, int x, int y, ObjectId schoolId,Integer isUse) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid",schoolId).append("x",x).append("y",y).append("ir",Constant.ZERO)
                .append("isUse",isUse);
        if(gradeId!=null){
            query.append("gid",gradeId);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;

    }

    public List<N33_ZKBEntry> getYKBEntrysByJXBIdsOrNJxbIds(ObjectId termId, List<ObjectId> ctJxbIds, ObjectId schoolId,int week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("ir",Constant.ZERO).append("week",week);;
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("jxbId",new BasicDBObject(Constant.MONGO_IN,ctJxbIds)));
        values.add(new BasicDBObject("nJxbId",new BasicDBObject(Constant.MONGO_IN,ctJxbIds)));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param xqId
     * @param schoolId
     * @param week
     * @return
     */
    public List<N33_ZKBEntry> getZKBEntrysList(ObjectId xqId, ObjectId schoolId,int week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid", schoolId).append("ir",Constant.ZERO).append("week",week);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
    /**
     *
     * @param xqId
     * @param schoolId
     * @param week
     * @return
     */
    public List<N33_ZKBEntry> getZKBEntrysListByTeacherIds(ObjectId xqId, ObjectId schoolId,int week,List<ObjectId> teacherIds) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid", schoolId).append("ir",Constant.ZERO).append("week",week);
        if (teacherIds!=null && teacherIds.size()!=0) {
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("tid", new BasicDBObject(Constant.MONGO_IN,teacherIds)));
            values.add(new BasicDBObject("ntid", new BasicDBObject(Constant.MONGO_IN,teacherIds)));
            query.put(Constant.MONGO_OR, values);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getZKBEntrysListByTeacherIds2(ObjectId xqId, ObjectId schoolId,List<Integer> weeks,List<ObjectId> teacherIds) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query =new BasicDBObject("termId",xqId).append("sid", schoolId).append("ir",Constant.ZERO).append("week",new BasicDBObject(Constant.MONGO_IN,weeks));
        if (teacherIds!=null && teacherIds.size()!=0) {
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("tid", new BasicDBObject(Constant.MONGO_IN,teacherIds)));
            values.add(new BasicDBObject("ntid", new BasicDBObject(Constant.MONGO_IN,teacherIds)));
            query.put(Constant.MONGO_OR, values);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
    
    //查询某个学科的周课表 oz
    public List<N33_ZKBEntry> getZKBsBySW(ObjectId schoolId, String gradeId, ObjectId xqid, ObjectId subjectId, Integer week) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gid", new ObjectId(gradeId))
        	 .append("termId", xqid)
        	 .append("week", week);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("subId", subjectId));
        values.add(new BasicDBObject("nsubId", subjectId));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_ZKBEntry> getN33_ZKBByWeekAndXY(ObjectId xqid, Integer x, List<Integer> ys, ObjectId sid, Integer week, ObjectId teacherId) {
        List<N33_ZKBEntry> result = new ArrayList<N33_ZKBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid).append("tid", teacherId).append("ir", Constant.ZERO).append("week", week).append("x", x).append("y", new BasicDBObject(Constant.MONGO_IN,ys));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZKB, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_ZKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
}
