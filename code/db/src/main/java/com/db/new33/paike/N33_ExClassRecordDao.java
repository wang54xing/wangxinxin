package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_ExClassRecordEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by wang_xinxin on 2018/3/8.
 */
public class N33_ExClassRecordDao extends BaseDao {


    /**
     * 保存换课记录
     *
     * @param entry
     */
    public void addN33_ExClassRecordEntry(N33_ExClassRecordEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_EXCLASSRECORD, entry.getBaseEntry());
    }

    /**
     * 物理删除换课记录
     *
     * @param id
     */
    public void removeN33_ExClassRecordEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_EXCLASSRECORD, query);
    }

    public void removeN33_ExClassRecordEntryBySid(ObjectId xqid,ObjectId sid) {
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid",sid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_EXCLASSRECORD, query);
    }

    public List<N33_ExClassRecordEntry> getRecordOrderByTime(ObjectId xqid,ObjectId sid){
        List<N33_ExClassRecordEntry> result = new ArrayList<N33_ExClassRecordEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_EXCLASSRECORD, query, Constant.FIELDS,new BasicDBObject("time",-1));
        for (DBObject dbo : dblist) {
            result.add(new N33_ExClassRecordEntry((BasicDBObject) dbo));
        }
        return result;
    }
}
