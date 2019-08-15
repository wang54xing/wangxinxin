package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_NameKBEntry;
import com.pojo.new33.paike.N33_NameTypeEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by albin on 2018/3/20.
 */
public class N33_NameTypeDao extends BaseDao {

    /**
     * 保存课表
     *
     * @param entry
     */
    public void addN33_Name(N33_NameTypeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKBTYPE, entry.getBaseEntry());
    }
    /**
     * 更新一条课表
     *
     * @param YKBEntry
     */
    public void updateN33_NameKBEntry(N33_NameKBEntry YKBEntry) {
        DBObject query = new BasicDBObject(Constant.ID, YKBEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, YKBEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKBTYPE, query, updateValue);
    }

    /**
     * 删除一条课表
     *
     * @param id
     */
    public void deleteN33_NameKBEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKBTYPE, query, updateValue);
    }

    /**
     * 物理删除课表
     *
     * @param id
     */
    public void removeN33_NameKBEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKBTYPE, query);
    }


    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public N33_NameTypeEntry getN33_NameKBEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKBTYPE, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_NameTypeEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 查询全校课表
     *
     * @param sid
     * @param gid
     * @param xqid
     * @return
     */
    public List<N33_NameTypeEntry> getN33_NameKBEntryBynId(ObjectId sid, ObjectId gid, ObjectId xqid) {
        List<N33_NameTypeEntry> result = new ArrayList<N33_NameTypeEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid);
        if(gid!=null){
            query.append("gid",gid);
        }
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKBTYPE, query, Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:dblist) {
            result.add(new N33_NameTypeEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_NameTypeEntry> getN33_NameKBEntryBynId(ObjectId sid,ObjectId xqid) {
        List<N33_NameTypeEntry> result = new ArrayList<N33_NameTypeEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKBTYPE, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_NameTypeEntry((BasicDBObject) dbo));
        }
        return result;
    }
}
