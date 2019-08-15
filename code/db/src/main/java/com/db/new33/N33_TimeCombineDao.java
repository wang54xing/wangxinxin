package com.db.new33;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.paike.N33_TimeCombineEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

public class N33_TimeCombineDao extends BaseDao {

    public void addTimeCombine(N33_TimeCombineEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, entry.getBaseEntry());
    }

    public void addTimeCombineList(Collection<N33_TimeCombineEntry> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, MongoUtils.fetchDBObjectList(list));
    }

    public void update(N33_TimeCombineEntry timeCombineEntry) {
        DBObject query = new BasicDBObject(Constant.ID, timeCombineEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, timeCombineEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, query, updateValue);
    }

    public List<N33_TimeCombineEntry> getTimeCombine(ObjectId ciId, ObjectId sid,ObjectId gradeId) {
        List<N33_TimeCombineEntry> result = new ArrayList<N33_TimeCombineEntry>();
        BasicDBObject query = new BasicDBObject("ciId", ciId)
                .append("sid", sid)
                .append("gradeId",gradeId);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_TimeCombineEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_TimeCombineEntry> getTimeCombineByCiId(ObjectId ciId, ObjectId sid) {
        List<N33_TimeCombineEntry> result = new ArrayList<N33_TimeCombineEntry>();
        BasicDBObject query = new BasicDBObject("ciId", ciId).append("sid", sid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_TimeCombineEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_TimeCombineEntry> getTimeCombineByType(ObjectId ciId, ObjectId sid, ObjectId gradeId, Integer type) {
        List<N33_TimeCombineEntry> result = new ArrayList<N33_TimeCombineEntry>();
        BasicDBObject query = new BasicDBObject("ciId", ciId)
                .append("sid", sid)
                .append("gradeId",gradeId)
                .append("type",type);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            result.add(new N33_TimeCombineEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public void removeByType(ObjectId ciId,ObjectId sid,ObjectId gradeId,Integer type) {
        BasicDBObject query = new BasicDBObject("ciId", ciId).append("sid",sid).append("gradeId",gradeId).append("type",type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, query);
    }

    public N33_TimeCombineEntry getTimeCombineByTypeAndTagId(ObjectId ciId, ObjectId sid, ObjectId gid,Integer type,ObjectId tagId) {
        BasicDBObject query = new BasicDBObject("ciId", ciId).append("sid", sid).append("gradeId", gid).append("type",type).append("tagId",tagId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, query, Constant.FIELDS);
        if (null != dbObject) {
            return new N33_TimeCombineEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /* *
     *  删除
     *   * @param id
     * @return void
     */
    public void removeById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, query);
    }

    /* *
     *  删除
     *   * @param id
     * @return void
     */
    public void removeByCiId(ObjectId ciId,ObjectId sid) {
        BasicDBObject query = new BasicDBObject("ciId", ciId).append("sid",sid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, query);
    }

    public void removeByTagId(ObjectId tagId,ObjectId sid) {
        BasicDBObject query = new BasicDBObject("tagId", tagId).append("sid",sid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIMECOMBINE, query);
    }
}
