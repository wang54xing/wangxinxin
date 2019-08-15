package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_PaiKeZuHeEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by albin on 2018/7/10.
 */
public class N33_PaiKeZuHeDao extends BaseDao {

    public void addEntry(N33_PaiKeZuHeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKEZUHE, entry.getBaseEntry());
    }

    public N33_PaiKeZuHeEntry findEntryById(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_PAIKEZUHE, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_PaiKeZuHeEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public void update(N33_PaiKeZuHeEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKEZUHE, query, updateValue);
    }

    public void delete(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKEZUHE, query);
    }
    public void addEntrys(Collection<N33_PaiKeZuHeEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKEZUHE, dbObjects);
    }

    public void delete(ObjectId gid, Integer type, ObjectId xqid) {
        BasicDBObject query = new BasicDBObject("termId", xqid).append("gid", gid).append("type", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKEZUHE, query);
    }

    public List<N33_PaiKeZuHeEntry> getEntryList(ObjectId gid, Integer type, ObjectId xqid) {
        List<N33_PaiKeZuHeEntry> entries = new ArrayList<N33_PaiKeZuHeEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("gid", gid);
        if (type > 0) {
            query.append("type", type);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PAIKEZUHE, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_PaiKeZuHeEntry entry = new N33_PaiKeZuHeEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }
}
