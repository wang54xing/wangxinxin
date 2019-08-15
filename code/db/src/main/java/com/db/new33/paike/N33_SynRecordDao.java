package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_SynRecordEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

public class N33_SynRecordDao extends BaseDao {

    public void add(N33_SynRecordEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SYNRECORD, entry.getBaseEntry());
    }

    public N33_SynRecordEntry getRecord(ObjectId ciId){
        BasicDBObject query = new BasicDBObject("ciId",ciId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SYNRECORD, query,Constant.FIELDS);
        if(dbo!=null){
            return new N33_SynRecordEntry((BasicDBObject)dbo);
        }
        return null;
    }


}
