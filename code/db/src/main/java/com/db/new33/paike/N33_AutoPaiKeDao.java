package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_AutoPaiKeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2018/6/29.
 */
public class N33_AutoPaiKeDao extends BaseDao {
    public void addEntry(N33_AutoPaiKeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKESHEZHI, entry.getBaseEntry());
    }

    public void updateN33_AutoPaiKeEntry(N33_AutoPaiKeEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKESHEZHI, query, updateValue);
    }

    public void removeEntry(ObjectId id) {
        DBObject query = new BasicDBObject("_id", id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKESHEZHI, query);
    }

    public List<N33_AutoPaiKeEntry> getEntryList(ObjectId schoolId, ObjectId xqid, ObjectId gid) {
        List<N33_AutoPaiKeEntry> result = new ArrayList<N33_AutoPaiKeEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId).append("xqid", xqid).append("gid", gid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKESHEZHI, query, Constant.FIELDS, new BasicDBObject("status", -1));
        for (DBObject dbo : dblist) {
            result.add(new N33_AutoPaiKeEntry((BasicDBObject) dbo));
        }
        return result;
    }


    public List<N33_AutoPaiKeEntry> getEntryList(ObjectId xqid, ObjectId gid, Integer type) {
        List<N33_AutoPaiKeEntry> result = new ArrayList<N33_AutoPaiKeEntry>();
        BasicDBObject query = new BasicDBObject().append("xqid", xqid).append("gid", gid);
        if(type>0){
            query.append("type",type);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKESHEZHI, query, Constant.FIELDS, new BasicDBObject("status", -1));
        for (DBObject dbo : dblist) {
            result.add(new N33_AutoPaiKeEntry((BasicDBObject) dbo));
        }
        return result;
    }
    public N33_AutoPaiKeEntry findEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKESHEZHI, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_AutoPaiKeEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }
}
