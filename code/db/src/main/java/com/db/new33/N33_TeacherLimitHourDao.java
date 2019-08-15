package com.db.new33;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_TeacherLimitHourEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


public class N33_TeacherLimitHourDao extends BaseDao{

    public N33_TeacherLimitHourEntry getTeacherLimitEntryByXqid(ObjectId xqid, ObjectId sid, ObjectId gid) {

        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        if(gid!=null){
            query.append("gid",gid);
        }
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_LIMITHOUR, query, Constant.FIELDS);
        if (null != dbObject ) {
                return new N33_TeacherLimitHourEntry((BasicDBObject) dbObject);
        }
        return null;
    }
    public List<N33_TeacherLimitHourEntry> getTeacherLimitEntryByXqid(ObjectId xqid, ObjectId sid){
        List<N33_TeacherLimitHourEntry> result = new ArrayList<N33_TeacherLimitHourEntry>();
        BasicDBObject query = new BasicDBObject("xqid", xqid).append("sid", sid);
        List<DBObject> dbObjects =find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_LIMITHOUR, query, Constant.FIELDS,new BasicDBObject("serial", Constant.ASC));
        for (DBObject dbObject : dbObjects) {
            result.add(new N33_TeacherLimitHourEntry((BasicDBObject) dbObject));
        }
        return result;
    }
    public void addLimitEntry(N33_TeacherLimitHourEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_LIMITHOUR,entry.getBaseEntry());
    }

    public void updateHour(ObjectId id,int hour){
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject("hour", hour);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_LIMITHOUR, query,new BasicDBObject(Constant.MONGO_SET,update));
    }

}
