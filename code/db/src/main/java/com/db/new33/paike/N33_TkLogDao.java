package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.paike.N33_TkLogEntry;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wang_xinxin on 2018/6/25.
 */
public class N33_TkLogDao extends BaseDao {
    /**
     * 保存调课记录
     * @param entry
     */
    public void addN33_TkLogEntry(N33_TkLogEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TKLOG, entry.getBaseEntry());
    }

    /**
     * 保存调课记录
     * @param list
     */
    public void addN33_TkLogEntry(Collection<N33_TkLogEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TKLOG, dbObjects);
    }

    /**
     * 更新调课记录
     * @param tkLogEntry
     */
    public void updateN33_TkLog(N33_TkLogEntry tkLogEntry){
        DBObject query =new BasicDBObject(Constant.ID,tkLogEntry.getID());
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,tkLogEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TKLOG, query, updateValue);
    }

    public N33_TkLogEntry findN33_TkLogEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id).append("ir",0);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TKLOG, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_TkLogEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    /**
     *
     * @param termId
     * @param schoolId
     * @param gradeId
     * @return
     */
    public List<N33_TkLogEntry> getTkLogByGradeId(ObjectId termId, ObjectId schoolId, ObjectId gradeId,int page,int pageSize,List<ObjectId> uids) {
        List<N33_TkLogEntry> result = new ArrayList<N33_TkLogEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("gid",gradeId).append("ir",Constant.ZERO);
        if (uids!=null &&uids.size()!=0) {
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("otid",new BasicDBObject(Constant.MONGO_IN,uids)));
            values.add(new BasicDBObject("ntid",new BasicDBObject(Constant.MONGO_IN,uids)));
            query.put(Constant.MONGO_OR, values);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TKLOG, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        for(DBObject dbo:dblist) {
            result.add(new N33_TkLogEntry((BasicDBObject) dbo));
        }
        return result;
    }

    /**
     *
     * @param termId
     * @param schoolId
     * @param gradeId
     * @return
     */
    public int getTkLogCountByGradeId(ObjectId termId, ObjectId schoolId, ObjectId gradeId,List<ObjectId> uids) {
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("gid",gradeId).append("ir",Constant.ZERO);
        if (uids!=null &&uids.size()!=0) {
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("oYkbId",new BasicDBObject(Constant.MONGO_IN,uids)));
            values.add(new BasicDBObject("nYkbId",new BasicDBObject(Constant.MONGO_IN,uids)));
            query.put(Constant.MONGO_OR, values);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TKLOG, query);
    }


    public void remove(ObjectId sqId) {
        BasicDBObject query = new BasicDBObject("sqId", sqId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TKLOG, query);
    }

    public List<N33_TkLogEntry> getTkLogByGradeId(ObjectId termId, ObjectId schoolId, ObjectId gradeId, List<ObjectId> uids) {
        List<N33_TkLogEntry> result = new ArrayList<N33_TkLogEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("gid",gradeId).append("ir",Constant.ZERO);
        if (uids!=null &&uids.size()!=0) {
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("otid",new BasicDBObject(Constant.MONGO_IN,uids)));
            values.add(new BasicDBObject("ntid",new BasicDBObject(Constant.MONGO_IN,uids)));
            query.put(Constant.MONGO_OR, values);
        }
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TKLOG, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:dblist) {
            result.add(new N33_TkLogEntry((BasicDBObject) dbo));
        }
        return result;
    }

    public List<N33_TkLogEntry> getTkLogByGradeId(ObjectId termId, ObjectId schoolId, ObjectId gradeId) {
        List<N33_TkLogEntry> result = new ArrayList<N33_TkLogEntry>();
        BasicDBObject query =new BasicDBObject("termId",termId).append("sid",schoolId).append("gid",gradeId).append("ir",Constant.ZERO);
        List<DBObject> dblist = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TKLOG, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:dblist) {
            result.add(new N33_TkLogEntry((BasicDBObject) dbo));
        }
        return result;
    }
}
