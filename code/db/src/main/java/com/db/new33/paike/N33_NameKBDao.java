package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_NameKBEntry;
import com.pojo.new33.paike.N33_ZKBEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by albin on 2018/3/20.
 */
public class N33_NameKBDao extends BaseDao {

    /**
     * 保存课表
     *
     * @param entry
     */
    public void addN33_NameKBEntry(N33_NameKBEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKB, entry.getBaseEntry());
    }

    /**
     * 批量保存
     *
     * @param entry
     */
    public void addN33_NameKBEntryList(Collection<N33_NameKBEntry> entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKB, MongoUtils.fetchDBObjectList(entry));
    }


    /**
     * 更新一条课表
     *
     * @param YKBEntry
     */
    public void updateN33_NameKBEntry(N33_NameKBEntry YKBEntry) {
        DBObject query = new BasicDBObject(Constant.ID, YKBEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, YKBEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKB, query, updateValue);
    }

    /**
     * 删除一条课表
     *
     * @param id
     */
    public void deleteN33_NameKBEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKB, query, updateValue);
    }

    /**
     * 物理删除课表
     *
     * @param id
     */
    public void removeN33_NameKBEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKB, query);
    }


    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public N33_NameKBEntry getN33_NameKBEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("ir", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKB, query, Constant.FIELDS);
        if (null != dbo) {
            return new N33_NameKBEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 查询某个命名课表
     *
     * @param nid
     * @return
     */
    public List<N33_NameKBEntry> getN33_NameKBEntryBynId(ObjectId nid) {
        List<N33_NameKBEntry> result = new ArrayList<N33_NameKBEntry>();
        BasicDBObject query = new BasicDBObject().append("nid", nid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_NameKBEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_NameKBEntry> getN33_NameKBEntryBynId(ObjectId nid,ObjectId gid) {
        List<N33_NameKBEntry> result = new ArrayList<N33_NameKBEntry>();
        BasicDBObject query = new BasicDBObject().append("nid", nid).append("gid",gid);
        query.append("ir", Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_NAMEKB, query, Constant.FIELDS);
        for(DBObject dbo:dblist) {
            result.add(new N33_NameKBEntry((BasicDBObject) dbo));
        }
        return result;
    }
}
