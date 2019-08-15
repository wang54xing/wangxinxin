package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserLogEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/5/4.
 */
public class UserLogDao extends BaseDao {
    /**
     * 添加log
     * @param e
     * @return
     */
    public ObjectId addUserLogLog(UserLogEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     *
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    public List<UserLogEntry> getUserBalanceLogEntry(ObjectId id, int page, int pageSize) {
        BasicDBObject query=new BasicDBObject("ui",id).append("ty", 2);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, page, pageSize);

        List<UserLogEntry> userEntryList=new ArrayList<UserLogEntry>();
        for(DBObject dbObject:dbObjectList){
            UserLogEntry userEntry=new UserLogEntry((BasicDBObject)dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    /**
     *
     * @param id
     * @return
     */
    public int getUserBalanceLogCount(ObjectId id) {
        BasicDBObject query=new BasicDBObject("ui",id).append("ty", 2);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_NAME,query);
    }
}
