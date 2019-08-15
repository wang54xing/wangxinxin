package com.db.classroom;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.classroom.NotClassroomReserveEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by albin on 2017/12/14.
 */
public class NotClassroomReserveDao extends BaseDao {

    /**
     * 添加一个非预定教室
     *
     * @param e
     * @return
     */
    public ObjectId addNotClassroomReserveEntry(NotClassroomReserveEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTCLASSROOMRESERVE, e.getBaseEntry());
        return e.getID();
    }

    public void updateNotClassroomReserveEntry(NotClassroomReserveEntry entry) {
        BasicDBObject query = new BasicDBObject("crid", entry.getClassRoomID());
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTCLASSROOMRESERVE, query, update);
    }

    public NotClassroomReserveEntry getNotClassroomReserveEntry(ObjectId rmid) {
        NotClassroomReserveEntry retList = null;
        BasicDBObject query = new BasicDBObject("crid", rmid);
        DBObject list = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTCLASSROOMRESERVE, query, Constant.FIELDS);
        if (null != list) {
            retList = new NotClassroomReserveEntry((BasicDBObject) list);
        }
        return retList;
    }

    public NotClassroomReserveEntry getMianClassroomReserveEntry(ObjectId rmid) {
        NotClassroomReserveEntry retList = null;
        BasicDBObject query = new BasicDBObject("crid", rmid).append("st", 1);
        DBObject list = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTCLASSROOMRESERVE, query, Constant.FIELDS);
        if (null != list) {
            retList = new NotClassroomReserveEntry((BasicDBObject) list);
        }
        return retList;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(ObjectId id) {
        BasicDBObject query = new BasicDBObject("crid", id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTCLASSROOMRESERVE, query);
    }

    public List<NotClassroomReserveEntry> getNotClassroomReserveEntrys(ObjectId sid, Integer sta) {
        List<NotClassroomReserveEntry> retList = new ArrayList<NotClassroomReserveEntry>();
        BasicDBObject query = new BasicDBObject("si", sid);
        if (sta > 0) {
            query.append("st", sta);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTCLASSROOMRESERVE, query, Constant.FIELDS, new BasicDBObject(Constant.ID, -1));
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new NotClassroomReserveEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }
    public Map<ObjectId, NotClassroomReserveEntry> getUserRoom(ObjectId uid) {
        Map<ObjectId, NotClassroomReserveEntry> retList = new HashMap<ObjectId, NotClassroomReserveEntry>();
        BasicDBObject query = new BasicDBObject("fid", uid);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTCLASSROOMRESERVE, query, Constant.FIELDS, new BasicDBObject(Constant.ID, -1));
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                NotClassroomReserveEntry entry = new NotClassroomReserveEntry((BasicDBObject) dbo);
                retList.put(entry.getClassRoomID(), entry);
            }
        }
        return retList;
    }


    public Map<ObjectId, NotClassroomReserveEntry> getNotClassroomReserveEntrys(ObjectId sid, List<ObjectId> roomIds) {
        Map<ObjectId, NotClassroomReserveEntry> retList = new HashMap<ObjectId, NotClassroomReserveEntry>();
        BasicDBObject query = new BasicDBObject("si", sid);
        if (roomIds.size() > 0) {
            query.append("crid", new BasicDBObject(Constant.MONGO_IN, roomIds));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTCLASSROOMRESERVE, query, Constant.FIELDS, new BasicDBObject(Constant.ID, -1));
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                NotClassroomReserveEntry entry = new NotClassroomReserveEntry((BasicDBObject) dbo);
                retList.put(entry.getClassRoomID(), entry);
            }
        }
        return retList;
    }
}
