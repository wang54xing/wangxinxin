package com.db.new33.afresh;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.afresh.AfreshSubRuleEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfreshSubRuleDao extends BaseDao {

    public void addEntries(List<AfreshSubRuleEntry> entries) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SUB_RULE, MongoUtils.fetchDBObjectList(entries));
    }

    public void addEntry(AfreshSubRuleEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SUB_RULE, entry.getBaseEntry());
    }

    public void updEntry(AfreshSubRuleEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SUB_RULE, query, updateValue);
    }

    public Map<ObjectId, AfreshSubRuleEntry> getAfreshSubRuleMap(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int afreshType) {
        Map<ObjectId, AfreshSubRuleEntry> reMap = new HashMap<ObjectId, AfreshSubRuleEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gid", gradeId);
        query.append("ciId", ciId);
        query.append("aty", afreshType);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SUB_RULE, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                AfreshSubRuleEntry entry = new AfreshSubRuleEntry((BasicDBObject) dbo);
                reMap.put(entry.getSubId(), entry);
            }
        }
        return reMap;
    }


    public List<AfreshSubRuleEntry> getEntries(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int afreshType) {
        List<AfreshSubRuleEntry> entries = new ArrayList<AfreshSubRuleEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gid", gradeId);
        query.append("ciId", ciId);
        query.append("aty", afreshType);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SUB_RULE, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new AfreshSubRuleEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }


    public AfreshSubRuleEntry getAfreshSubRuleEntry(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, ObjectId subId, int afreshType) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gid", gradeId);
        query.append("ciId", ciId);
        query.append("subId", subId);
        query.append("aty", afreshType);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SUB_RULE, query, Constant.FIELDS);
        if (dbObject != null) {
            return new AfreshSubRuleEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }
}
