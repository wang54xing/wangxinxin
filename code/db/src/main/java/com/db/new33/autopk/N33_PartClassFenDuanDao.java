package com.db.new33.autopk;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.N33_PartClassFenDuanEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

public class N33_PartClassFenDuanDao extends BaseDao {


    public void addEntry(N33_PartClassFenDuanEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_MAX_FEN_DUAN_SET, e.getBaseEntry());
    }

    public void updEntry(N33_PartClassFenDuanEntry e) {
        DBObject query = new BasicDBObject(Constant.ID, e.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_MAX_FEN_DUAN_SET, query, updateValue);
    }

    public N33_PartClassFenDuanEntry getEntry(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        BasicDBObject query = new BasicDBObject("schoolId", schoolId)
                .append("gradeId", gradeId)
                .append("ciId", ciId);
        DBObject dbObj = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_MAX_FEN_DUAN_SET, query, Constant.FIELDS);
        if (null != dbObj) {
            return new N33_PartClassFenDuanEntry((BasicDBObject) dbObj);
        }
        return null;
    }
}
