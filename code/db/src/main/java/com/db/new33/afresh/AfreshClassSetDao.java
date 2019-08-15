package com.db.new33.afresh;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.afresh.AfreshClassSetEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2019-05-13.
 */
public class AfreshClassSetDao extends BaseDao {
    public void addEntry(AfreshClassSetEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SET_CLASS, entry.getBaseEntry());
    }
/*ObjectId xqid,
                               ObjectId schoolId,
                               ObjectId gradeId,*/
    public void updateOld(ObjectId xqid,
                           ObjectId schoolId,
                           ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid",schoolId).append("gid", gradeId).append("isr",0);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("isr", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SET_CLASS, query, updateValue);
    }

    public void updateNew(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("isr", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SET_CLASS, query, updateValue);
    }

    public AfreshClassSetEntry getEntryBySchoolId(ObjectId xqid,
                                            ObjectId schoolId,
                                            ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid",schoolId).append("gid", gradeId).append("isr",0);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_SET_CLASS, query, Constant.FIELDS);
        if(dbo!=null) {
            return new AfreshClassSetEntry((BasicDBObject) dbo);
        }
        return null;
    }
}
