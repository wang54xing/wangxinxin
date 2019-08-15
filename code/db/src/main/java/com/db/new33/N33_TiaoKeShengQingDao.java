package com.db.new33;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_TiaoKeShengQingEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2018/8/3.
 */
public class N33_TiaoKeShengQingDao extends BaseDao {

    public void remove(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TiaoKeShengQing, query);
    }



    public void updateEntry(N33_TiaoKeShengQingEntry entry) {
        BasicDBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TiaoKeShengQing, query, updateValue);
    }

    public void updateEntry(ObjectId id,Integer sta) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sta",sta));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TiaoKeShengQing, query, updateValue);
    }
    public N33_TiaoKeShengQingEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TiaoKeShengQing, query, Constant.FIELDS);
        if(dbo!=null) {
            return new N33_TiaoKeShengQingEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public void addEntry(N33_TiaoKeShengQingEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TiaoKeShengQing, entry.getBaseEntry());
    }

    public List<N33_TiaoKeShengQingEntry> getEntryList(ObjectId xqid, List<ObjectId> jxbIds,Integer week) {
        List<N33_TiaoKeShengQingEntry> result = new ArrayList<N33_TiaoKeShengQingEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("week",week).append("jxbId", new BasicDBObject(Constant.MONGO_IN, jxbIds)).append("sta",1);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TiaoKeShengQing, query, Constant.FIELDS);
        for (DBObject dbObject : dbObjects) {
            result.add(new N33_TiaoKeShengQingEntry((BasicDBObject) dbObject));
        }
        return result;
    }

    public List<N33_TiaoKeShengQingEntry> getEntryList(ObjectId xqid,ObjectId teaId) {
        List<N33_TiaoKeShengQingEntry> result = new ArrayList<N33_TiaoKeShengQingEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("teaId", teaId);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TiaoKeShengQing, query, Constant.FIELDS);
        for (DBObject dbObject : dbObjects) {
            result.add(new N33_TiaoKeShengQingEntry((BasicDBObject) dbObject));
        }
        return result;
    }

    public List<N33_TiaoKeShengQingEntry> getEntryList(ObjectId xqid,ObjectId teaId,Integer week) {
        List<N33_TiaoKeShengQingEntry> result = new ArrayList<N33_TiaoKeShengQingEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("teaId", teaId).append("week",week);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TiaoKeShengQing, query, Constant.FIELDS);
        for (DBObject dbObject : dbObjects) {
            result.add(new N33_TiaoKeShengQingEntry((BasicDBObject) dbObject));
        }
        return result;
    }
}
