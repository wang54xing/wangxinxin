package com.db.new33.afresh;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.afresh.AfreshTypeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class AfreshTypeDao extends BaseDao {

    public void addAfreshTypeEntry(AfreshTypeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_TYPE, entry.getBaseEntry());
    }

    public void updAfreshTypeUseIsNotUse(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gid", gradeId);
        query.append("ciId", ciId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("use", Constant.ZERO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_TYPE, query, updateValue);
    }

    public void updAfreshTypeEntry(AfreshTypeEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_TYPE, query, updateValue);
    }

    public AfreshTypeEntry getUseAfreshTypeEntry(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gid", gradeId);
        query.append("ciId", ciId);
        query.append("use", Constant.ONE);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_TYPE, query, Constant.FIELDS);
        if (dbObject != null) {
            return new AfreshTypeEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public AfreshTypeEntry getAfreshTypeEntry(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int afreshType) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gid", gradeId);
        query.append("ciId", ciId);
        query.append("aty", afreshType);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_TYPE, query, Constant.FIELDS);
        if (dbObject != null) {
            return new AfreshTypeEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public List<AfreshTypeEntry> getAfreshTypeEntries(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        List<AfreshTypeEntry> entries = new ArrayList<AfreshTypeEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gid", gradeId);
        query.append("ciId", ciId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_TYPE, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new AfreshTypeEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }
}
