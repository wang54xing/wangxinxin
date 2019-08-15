package com.db.new33.afresh;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.afresh.AfreshClassEntry;
import com.pojo.new33.afresh.ChooseSetEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2019-05-13.
 *
 *
 */
public class AfreshClassDao extends BaseDao {

    public List<AfreshClassEntry> findByGradeIdId(ObjectId sid, ObjectId gradeId, ObjectId xqid) {
        List<AfreshClassEntry> entries = new ArrayList<AfreshClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid)
                .append("gid", gradeId)
                .append("xqid", xqid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new AfreshClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public List<AfreshClassEntry> findByGradeIdId(ObjectId rid) {
        List<AfreshClassEntry> entries = new ArrayList<AfreshClassEntry>();
        BasicDBObject query = new BasicDBObject("rid", rid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new AfreshClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public void addAfreshClassEntry(List<DBObject> entrys) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, entrys);
    }

    public AfreshClassEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, query, Constant.FIELDS);
        if(dbo!=null) {
            return new AfreshClassEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public void addEntry(AfreshClassEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, entry.getBaseEntry());
    }

    public List<AfreshClassEntry> getEntries(ObjectId schoolId, ObjectId gradeId, ObjectId termId, ObjectId ruleId, int className) {
        List<AfreshClassEntry> entries = new ArrayList<AfreshClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId)
                .append("gid", gradeId)
                .append("xqid", termId)
                .append("rid", ruleId)
                .append("xh", className);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new AfreshClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }

    public void updateClassEntry(AfreshClassEntry e) {
        BasicDBObject query = new BasicDBObject(Constant.ID, e.getID());
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET, e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, query, updateValue);
    }


    public void updateClassName(ObjectId id, String className) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nm", className));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, query, updateValue);
    }
    
    public void delClassById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, query);
    }
    
    public List<AfreshClassEntry> getClassEntries(ObjectId schoolId, ObjectId gradeId, ObjectId termId) {
        List<AfreshClassEntry> entries = new ArrayList<AfreshClassEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId)
                .append("gid", gradeId)
                .append("xqid", termId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_AFRESH_CLASS, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new AfreshClassEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }
    
    
    
    //================================================设置分班规则表==========================================================
    
    public void addChooseEntry(ChooseSetEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_CHOOSESET, entry.getBaseEntry());
    }
    
    public List<ChooseSetEntry> getChooseSetEntries(ObjectId schoolId, ObjectId gradeId, ObjectId termId) {
        List<ChooseSetEntry> entries = new ArrayList<ChooseSetEntry>();
        BasicDBObject query = new BasicDBObject("sid", schoolId)
                .append("gid", gradeId)
                .append("ciId", termId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_CHOOSESET, query, Constant.FIELDS);
        if (null != dbObject && !dbObject.isEmpty()) {
            for (DBObject dbo : dbObject) {
                entries.add(new ChooseSetEntry((BasicDBObject) dbo));
            }
        }
        return entries;
    }
    
    public void updSubList(ObjectId id, int classContain, int classNum, int floatNum) {
  	  List<ChooseSetEntry> entries = new ArrayList<ChooseSetEntry>();
      BasicDBObject query = new BasicDBObject("_id", id);
      DBObject updateValue =new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("classContain", classContain).append("classNum", classNum).append("floatNum", floatNum));
      update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_CHOOSESET, query, updateValue);
  }
    
    public void delChooseSetById(ObjectId schoolId, ObjectId gradeId, ObjectId termId) {
    	  List<ChooseSetEntry> entries = new ArrayList<ChooseSetEntry>();
          BasicDBObject query = new BasicDBObject("schoolId", schoolId)
                  .append("gradeId", gradeId)
                  .append("ciId", termId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_CHOOSESET, query);
    }
}
