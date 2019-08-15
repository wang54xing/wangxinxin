package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_PaiKeZuHeEntry;
import com.pojo.new33.paike.N33_TimeCombKeBiaoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hero on 2019/5/20.
 */
public class N33_TimeCombKeBiaoDao extends BaseDao {


    public void batchAddEntris(List<N33_TimeCombKeBiaoEntry> addEntries) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, MongoUtils.fetchDBObjectList(addEntries));
    }

    public void addEntry(N33_TimeCombKeBiaoEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, entry.getBaseEntry());
    }

    public N33_TimeCombKeBiaoEntry findEntryById(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_TimeCombKeBiaoEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public void update(N33_TimeCombKeBiaoEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, query, updateValue);
    }

    public void delete(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, query);
    }

    public N33_TimeCombKeBiaoEntry getTimeCombKeBiaoEntry(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int x, int y) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gradeId", gradeId);
        query.append("ciId", ciId);
        query.append("x", x);
        query.append("y", y);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_TimeCombKeBiaoEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public List<N33_TimeCombKeBiaoEntry> getTimeCombKeBiaoEntries(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        List<N33_TimeCombKeBiaoEntry> reList = new ArrayList<N33_TimeCombKeBiaoEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gradeId", gradeId);
        query.append("ciId", ciId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_TimeCombKeBiaoEntry entry = new N33_TimeCombKeBiaoEntry((BasicDBObject) dbo);
            reList.add(entry);
        }
        return reList;
    }

    public void cancelTimeCombKeBiao(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int x, int y) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gradeId", gradeId);
        query.append("ciId", ciId);
        query.append("x", x);
        query.append("y", y);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, query);
    }

    public int getTimeCombFinishKeShiCount(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int type, int serial) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gradeId", gradeId);
        query.append("ciId", ciId);
        query.append("type", type);
        query.append("serial", serial);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, query);
    }

    public void deleteEntris(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        query.append("gradeId", gradeId);
        query.append("ciId", ciId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TIEM_COMB_KEBIAO, query);
    }
}
