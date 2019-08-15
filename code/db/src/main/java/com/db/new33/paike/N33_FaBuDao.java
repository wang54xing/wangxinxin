package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_FaBuEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2018/6/19.
 */
public class N33_FaBuDao extends BaseDao {
    public void addN33_FaBuEntry(N33_FaBuEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_FABU, entry.getBaseEntry());
    }

    public void removeN33_FaBuEntry(ObjectId ciid) {
        DBObject query = new BasicDBObject("ciId", ciid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_FABU, query);
    }

    /***
     * 查询对应次是否被发布过课表
     * @param ciid
     * @return
     */
    public N33_FaBuEntry getN33_FaBuEntryByCiId(ObjectId ciid) {
        DBObject query = new BasicDBObject("ciId", ciid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_FABU, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_FaBuEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public N33_FaBuEntry getN33_FaBuEntryByXqId(ObjectId xqid) {
        DBObject query = new BasicDBObject("xqid", xqid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_FABU, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_FaBuEntry((BasicDBObject) dbo);
        }
        return null;
    }
}
