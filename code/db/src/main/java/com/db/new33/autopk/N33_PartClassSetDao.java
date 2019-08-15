package com.db.new33.autopk;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.autoPK.N33_PartClassSetEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_PartClassSetDao extends BaseDao {


    /**
     * 批量添加
     * @param entries
     */
    public void batchAddEntries(List<N33_PartClassSetEntry> entries) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PART_CLASS_SET, MongoUtils.fetchDBObjectList(entries));
    }

    /**
     * 查询
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public List<N33_PartClassSetEntry> getPartClassEntries(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        List<N33_PartClassSetEntry> list = new ArrayList<N33_PartClassSetEntry>();
        BasicDBObject query = new BasicDBObject("schoolId", schoolId)
                .append("gradeId", gradeId)
                .append("ciId", ciId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PART_CLASS_SET, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_PartClassSetEntry entry = new N33_PartClassSetEntry((BasicDBObject) dbo);
            list.add(entry);
        }
        return list;
    }

    /**
     * 修改分段
     * @param id
     * @param fenDuan
     */
    public void updatePartClassFenDuan(ObjectId id, int fenDuan) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("fenDuan", fenDuan));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PART_CLASS_SET, query, updateValue);
    }

    public void updatePartClassFenDuan(int maxFenDuan) {
        DBObject query = new BasicDBObject("fenDuan", new BasicDBObject(Constant.MONGO_GT, maxFenDuan));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("fenDuan", maxFenDuan));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PART_CLASS_SET, query, updateValue);
    }

    /**
     * 查询
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @param fenDuan
     * @return
     */
    public List<N33_PartClassSetEntry> getPartClassEntries(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int fenDuan) {
        List<N33_PartClassSetEntry> list = new ArrayList<N33_PartClassSetEntry>();
        BasicDBObject query = new BasicDBObject("schoolId", schoolId);
        query.append("gradeId", gradeId);
        query.append("ciId", ciId);;
        if(fenDuan!=-1) {
            query.append("fenDuan", fenDuan);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_PART_CLASS_SET, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_PartClassSetEntry entry = new N33_PartClassSetEntry((BasicDBObject) dbo);
            list.add(entry);
        }
        return list;
    }
}
