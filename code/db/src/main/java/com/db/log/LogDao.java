package com.db.log;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.log.LogEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2015/4/20.
 */
public class LogDao extends BaseDao {

    /**
     * 添加日志信息
     * @param e
     * @return
     */
    public ObjectId insert(LogEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, e.getBaseEntry());
        return e.getID();
    }

    public void editLog(ObjectId id, LogEntry e) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(e.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, query, update);
    }

    /**
     * 查询全部日志信息
     * @return
     */
    public List<LogEntry> findAll()
    {
        List<LogEntry> retList = new ArrayList<LogEntry>();
        List<DBObject>  list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, new BasicDBObject(), Constant.FIELDS);
        for(DBObject dbo:list)
        {
            retList.add(new LogEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 查询全部日志信息
     * @return
     */
    public List<LogEntry> findLogEntry(int skip,int limit)
    {
        List<LogEntry> retList = new ArrayList<LogEntry>();
        List<DBObject>  list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, new BasicDBObject(), Constant.FIELDS,Constant.MONGO_SORTBY_ASC,skip,limit);
        for(DBObject dbo:list)
        {
            retList.add(new LogEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 查询日志信息
     * @param uis 用户ID；不为null时生效
     * @param actionType 是微校园还是微家园
     * @param fields
     * @return
     * @throws ResultTooManyException
     */
    public List<LogEntry> getLogEntryByParamList(List<ObjectId> uis,int actionType,long dsl, long del,DBObject fields)  throws ResultTooManyException {
        List<LogEntry> retList =new ArrayList<LogEntry>();
        BasicDBObject query =new BasicDBObject("ui",new BasicDBObject(Constant.MONGO_IN,uis));

        BasicDBList dblist =new BasicDBList();
        if(dsl>0){
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if(del>0) {
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        if (actionType!=0) {
            query.append("ty",actionType);
        }

        BasicDBObject sort =new BasicDBObject("at",Constant.DESC);

        if(query.isEmpty())
            throw new ResultTooManyException();

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, query, fields, sort);

        for(DBObject dbo:list)
        {
            retList.add(new LogEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 查询日志数量
     * @param uis 用户ID；不为null时生效
     * @param actionType 是微校园还是微家园
     * @return
     * @throws ResultTooManyException
     */
    public int getLogEntryByParamCount(List<ObjectId> uis, int actionType, long dsl, long del) throws ResultTooManyException {
        BasicDBObject query =new BasicDBObject("ui",new BasicDBObject(Constant.MONGO_IN,uis));

        BasicDBList dblist =new BasicDBList();
        if(dsl>0){
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if(del>0) {
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        if (actionType!=0) {
            query.append("ty",actionType);
        }
        if(query.isEmpty())
            throw new ResultTooManyException();
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_ACTION_LOG,query);
    }

    /**
     * 查询日志信息
     * @param schoolId 学校Id；不为null时生效
     * @param gradeId 年级Id；不为null时生效
     * @param classId 班级Id；不为null时生效
     * @param actionType 是微校园还是微家园
     * @param fields
     * @return
     */
    public List<LogEntry> getLogEntryByParamList(ObjectId schoolId, ObjectId gradeId, ObjectId classId, int userRole, int noUserRole, int actionType,long dsl, long del,DBObject fields) {
        List<LogEntry> retList =new ArrayList<LogEntry>();
        BasicDBObject query =new BasicDBObject();
        if(schoolId!=null){
            query.append("sid",schoolId);
        }
        if(gradeId!=null){
            query.append("gids",gradeId);
        }
        if(classId!=null){
            query.append("cids",classId);
        }
        BasicDBList dblist =new BasicDBList();
        if(userRole!=0){
            dblist.add(new BasicDBObject("ur", userRole));
        }
        if(noUserRole!=0){
            dblist.add(new BasicDBObject("ur", new BasicDBObject(Constant.MONGO_NE, noUserRole)));
        }
        if(dsl>0){
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if(del>0) {
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        if (actionType!=0) {
            query.append("ty",actionType);
        }

        BasicDBObject sort =new BasicDBObject("at",Constant.DESC);

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, query, fields, sort);

        for(DBObject dbo:list)
        {
            retList.add(new LogEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 查询日志数量
     * @param schoolId 学校Id；不为null时生效
     * @param gradeId 年级Id；不为null时生效
     * @param classId 班级Id；不为null时生效
     * @param actionType 是微校园还是微家园
     * @return
     */
    public int getLogEntryByParamCount(ObjectId schoolId, ObjectId gradeId, ObjectId classId, int userRole, int noUserRole, int actionType, long dsl, long del){
        BasicDBObject query =new BasicDBObject();
        if(schoolId!=null){
            query.append("sid",schoolId);
        }
        if(gradeId!=null){
            query.append("gids",gradeId);
        }
        if(classId!=null){
            query.append("cids",classId);
        }
        BasicDBList dblist =new BasicDBList();
        if(userRole!=0){
            dblist.add(new BasicDBObject("ur", userRole));
        }
        if(noUserRole!=0){
            dblist.add(new BasicDBObject("ur", new BasicDBObject(Constant.MONGO_NE,noUserRole)));
        }
        if(dsl>0){
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if(del>0) {
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        if (actionType!=0) {
            query.append("ty",actionType);
        }
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_ACTION_LOG,query);
    }

    /**
     * 查询日志信息
     * @param schoolIds 学校Id；不为null时生效
     * @param gradeType 年级Id；不为null时生效
     * @param actionType 是微校园还是微家园
     * @param fields
     * @return
     */
    public List<LogEntry> getLogEntryByParamList(List<ObjectId> schoolIds, int gradeType, int userRole, int actionType,long dsl, long del,DBObject fields) {
        List<LogEntry> retList =new ArrayList<LogEntry>();
        BasicDBObject query =new BasicDBObject();
        if(schoolIds!=null&&schoolIds.size()>0){
            query.append("sid",new BasicDBObject(Constant.MONGO_IN,schoolIds));
        }
        if(gradeType!=0){
            query.append("gtys",gradeType);
        }
        if(userRole!=0){
            query.append("ur",userRole);
        }
        BasicDBList dblist =new BasicDBList();
        if(dsl>0){
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if(del>0) {
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        if (actionType!=0) {
            query.append("ty",actionType);
        }

        BasicDBObject sort =new BasicDBObject("at",Constant.DESC);

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, query, fields, sort);

        for(DBObject dbo:list)
        {
            retList.add(new LogEntry((BasicDBObject)dbo));
        }
        return retList;
    }


    /**
     * 查询日志数量
     * @param schoolIds 学校Id；不为null时生效
     * @param gradeType 年级Id；不为0时生效
     * @param actionType 是微校园还是微家园
     * @return
     */
    public int getLogEntryByParamCount(List<ObjectId> schoolIds, int gradeType, int userRole, int actionType, long dsl, long del){
        BasicDBObject query =new BasicDBObject();
        if(schoolIds!=null&&schoolIds.size()>0){
            query.append("sid",new BasicDBObject(Constant.MONGO_IN,schoolIds));
        }
        if(gradeType!=0){
            query.append("gtys",gradeType);
        }
        if(userRole!=0){
            query.append("ur",userRole);
        }
        BasicDBList dblist =new BasicDBList();
        if(dsl>0){
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if(del>0) {
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        if (actionType!=0) {
            query.append("ty",actionType);
        }

        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_ACTION_LOG,query);
    }
    /**
     * 查询全部日志数量
     * @return
     */
    public int findAllCount() {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, new BasicDBObject());
    }

    /**
     * 删除日志
     * @param schoolId 学校Id；不为null时生效
     * @param actionType 日志类型
     * @param dsl 开始时间
     * @param del 结束时间
     * @return
     */
    public void deleteLogEnrtys(ObjectId schoolId, int actionType, long dsl, long del) {
        BasicDBObject query =new BasicDBObject();
        if(schoolId!=null){
            query.append("sid",schoolId);
        }
        BasicDBList dblist =new BasicDBList();
        if(dsl>0){
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if(del>0) {
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        if (actionType!=0) {
            query.append("ty",actionType);
        }
        if(query.size()>0){
            remove(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, query);
        }
    }

    public Map<ObjectId,List<LogEntry>> getLogEntryMapByParam(int actionType, long dsl, long del, BasicDBObject fields) {
        Map<ObjectId, List<LogEntry>> map =new HashMap<ObjectId, List<LogEntry>>();
        BasicDBObject query =new BasicDBObject();
        BasicDBList dblist =new BasicDBList();
        if(dsl>0){
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if(del>0) {
            dblist.add(new BasicDBObject("at", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        if (actionType!=0) {
            query.append("ty",actionType);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTION_LOG, query, fields);
        List<LogEntry> logList=null;
        if(null!=list && !list.isEmpty())
        {
            LogEntry e;
            for(DBObject dbo:list)
            {
                e=new LogEntry((BasicDBObject)dbo);
                logList=map.get(e.getSchoolId());
                if(logList==null){
                    logList=new ArrayList<LogEntry>();
                }
                logList.add(e);
                map.put(e.getSchoolId(), logList);
            }
        }
        return map;
    }
}
