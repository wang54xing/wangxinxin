package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_JXBKS_TempEntry;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 教学班课时
 * Created by wang_xinxin on 2018/3/14.
 */
public class N33_JXBKS_TempDao extends BaseDao {
    /**
     * 保存教学班课时
     * @param entry
     */
    public void addN33_JXBKS_TempEntry(N33_JXBKS_TempEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS_TEMP, entry.getBaseEntry());
    }

    /**
     *
     * @param list
     */
    public void addN33_JXBKS_TempEntrys(Collection<N33_JXBKS_TempEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS_TEMP, dbObjects);
    }

    public N33_JXBKS_TempEntry getN33_JXBKS_TempByjxbId(ObjectId jxbId,Integer week,ObjectId ciId,Integer type){
        BasicDBObject query =new BasicDBObject("jxbId", jxbId).append("week",week).append("termId",ciId).append("type",type);
        query.append("ir", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS_TEMP, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new N33_JXBKS_TempEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public void removeZKBEntry_TempById(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id",id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS_TEMP, query);
    }

    public void removeZKBEntry_TempById(ObjectId termId,ObjectId sid,Integer week,Integer type) {
        BasicDBObject query = new BasicDBObject("termId",termId).append("sid",sid).append("week",week).append("type",type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS_TEMP, query);
    }


    public void updateN33_JXBks_Temp(N33_JXBKS_TempEntry jxbks_tempEntry){
        DBObject query =new BasicDBObject(Constant.ID,jxbks_tempEntry.getID());
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,jxbks_tempEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS_TEMP, query, updateValue);
    }

    public List<N33_JXBKS_TempEntry> getN33_JXBks_TempByWeek(ObjectId gradeId,ObjectId sid,Integer week,ObjectId termId,Integer type) {
        List<N33_JXBKS_TempEntry> result = new ArrayList<N33_JXBKS_TempEntry>();
        BasicDBObject query = new BasicDBObject("gid", gradeId).append("sid", sid).append("week",week).append("termId",termId).append("type",type);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS_TEMP, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBKS_TempEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_JXBKS_TempEntry> getN33_JXBks_TempByWeek(ObjectId sid,Integer week,ObjectId termId,Integer type) {
        List<N33_JXBKS_TempEntry> result = new ArrayList<N33_JXBKS_TempEntry>();
        BasicDBObject query = new BasicDBObject().append("sid", sid).append("week",week).append("termId",termId).append("type",type);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_JXBKS_TEMP, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_JXBKS_TempEntry((BasicDBObject) dbo));
        }
        return result;
    }

}
