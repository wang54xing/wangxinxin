package com.db.school;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.newschool.ClassEntry;
import com.sys.constants.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2017/2/11.
 */
public class NewClassDao extends BaseDao {

    /**
     * 添加
     * @param e
     * @return
     */
    public ClassEntry addClassEntry(ClassEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_CLASS_NAME, e.getBaseEntry());
        return e;
    }
    
    public ClassEntry findClassEntryByClassId(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_CLASS_NAME, query, Constant.FIELDS);
        if (dbObject != null) {
            return new ClassEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public Map<ObjectId, ClassEntry> getClassMapByIds(List<ObjectId> ids) {
        Map<ObjectId, ClassEntry> result = new HashMap<ObjectId, ClassEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_CLASS_NAME, query, Constant.FIELDS);
        for (DBObject dbo : dblist) {
            ClassEntry entry = new ClassEntry((BasicDBObject) dbo);
            result.put(entry.getID(), entry);
        }
        return result;
    }
}
