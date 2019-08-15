package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.paike.N33_SWLBEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wang_xinxin on 2018/3/8.
 */
public class N33_SWLBDao extends BaseDao {
    /**
     * 保存事务类别
     * @param entry
     */
    public void addN33_SWLBEntry(N33_SWLBEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SWLB, entry.getBaseEntry());
    }

    /**
     * 增加多个事务类别
     * @param list
     */
    public void addN33_SWLBEntrys(Collection<N33_SWLBEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SWLB, dbObjects);
    }

    /**
     * 更新一条事务类别
     * @param swlbEntry
     */
    public void updateN33_SWLB(N33_SWLBEntry swlbEntry){
        DBObject query =new BasicDBObject(Constant.ID,swlbEntry.getID());
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,swlbEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SWLB, query, updateValue);
    }
    
    /**
     * 根据id查找事务类别
     * @param swlbEntry
     */
    public N33_SWLBEntry findSWLBById(ObjectId id){
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(),
				Constant.COLLECTION_NEW33_SWLB, query, Constant.FIELDS);
		if (dbObject != null) {
			return new N33_SWLBEntry((BasicDBObject) dbObject);
		} else {
			return null;
		}
    }

    /**
     * 删除一条事务类别
     * @param id
     */
    public void deleteN33_SWLB(ObjectId id){
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SWLB, query, updateValue);
    }
    
    /**
     * 逻辑删除事务类别
     * @param id
     */
    public void removeN33_SwlbEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SWLB, query, updateValue);
    }

    /**
     * 物理删除事务类别
     * @param id
     */
    public void removeN33_SWLBEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SWLB, query);
    }

    public List<N33_SWLBEntry> getSwLbByXqid(ObjectId xqid, ObjectId sid) {
        List<N33_SWLBEntry> entries = new ArrayList<N33_SWLBEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid).append("ir",Constant.ZERO);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_SWLB, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_SWLBEntry entry = new N33_SWLBEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }
}
