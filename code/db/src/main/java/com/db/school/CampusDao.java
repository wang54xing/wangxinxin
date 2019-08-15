package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.CampusEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/7/23.
 */
public class CampusDao extends BaseDao{

    /**
     * 添加校区
     * @param campusEntry
     * @return
     */
    public ObjectId add(CampusEntry campusEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CAMPUS_NAME, campusEntry.getBaseEntry());
        return campusEntry.getID();
    }

    /**
     * 删除校区
     * @param campusId
     */
    public void delete(ObjectId campusId) {
        DBObject query = new BasicDBObject(Constant.ID, campusId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_CAMPUS_NAME, query);
    }

    /**
     * 更新校区
     * @param campusId
     * @param campusEntry
     */
    public void edit(ObjectId campusId, CampusEntry campusEntry) {
        DBObject query = new BasicDBObject(Constant.ID, campusId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,campusEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CAMPUS_NAME, query, updateValue);
    }

    /**
     * 列表
     * @return
     */
    public List<CampusEntry> list() {
        List<CampusEntry> campusEntryList = new ArrayList<CampusEntry>();
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CAMPUS_NAME, Constant.QUERY, Constant.FIELDS);
        if(null!=list && !list.isEmpty()) {
            for(DBObject dbo:list)
            {
                CampusEntry campusEntry = new CampusEntry((BasicDBObject)dbo);
                campusEntryList.add(campusEntry);
            }
            return campusEntryList;
        }
        return campusEntryList;
    }

    public CampusEntry getCampusInfo(ObjectId campusId) {
        DBObject query = new BasicDBObject(Constant.ID, campusId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CAMPUS_NAME, query, Constant.FIELDS);
        if(dbo != null) {
            return (new CampusEntry((BasicDBObject)dbo));
        }
        return new CampusEntry();
    }
}
