package com.db.new33;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_ZjkjEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_ZjkjDao extends BaseDao {

    /**
     *
     * @param entry
     */
    public void addZjkjEntry(N33_ZjkjEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZJTK, entry.getBaseEntry());
    }

    public void updateN33_ZjkjEntry(N33_ZjkjEntry gdsxEntry) {
        DBObject query = new BasicDBObject(Constant.ID, gdsxEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, gdsxEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZJTK, query, updateValue);
    }

    /**
     *
     * @param xqid
     * @param sid
     * @param gid
     * @param jxbId
     * @return
     */
    public List<N33_ZjkjEntry> getZjkjByJXBId(ObjectId xqid, ObjectId sid, ObjectId gid,ObjectId jxbId,ObjectId ciId) {
        List<N33_ZjkjEntry> entries = new ArrayList<N33_ZjkjEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid).append("isUse",0).append("gid", gid).append("jxbId",jxbId).append("ir",0).append("cid",ciId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZJTK, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_ZjkjEntry entry = new N33_ZjkjEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    /**
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<N33_ZjkjEntry> getZjkjBySidAndXqid(ObjectId xqid, ObjectId sid, ObjectId gid) {
        List<N33_ZjkjEntry> entries = new ArrayList<N33_ZjkjEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqid).append("sid", sid).append("isUse",0);
        if(gid != null){
            query.append("gid", gid);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZJTK, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_ZjkjEntry entry = new N33_ZjkjEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }

    public List<N33_ZjkjEntry> getZjkjList(ObjectId xqId, ObjectId gradeId,ObjectId schoolId) {
        List<N33_ZjkjEntry> entries = new ArrayList<N33_ZjkjEntry>();
        BasicDBObject query = new BasicDBObject("termId", xqId).append("sid", schoolId).append("isUse",0).append("ir",0);
        if(gradeId != null){
            query.append("gid", gradeId);
        }
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ZJTK, query, Constant.FIELDS);
        for (DBObject dbo : dbObject) {
            N33_ZjkjEntry entry = new N33_ZjkjEntry((BasicDBObject) dbo);
            entries.add(entry);
        }
        return entries;
    }
}
