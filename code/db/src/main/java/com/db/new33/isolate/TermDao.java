package com.db.new33.isolate;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.TermEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

public class TermDao extends BaseDao {
	
	public ObjectId addIsolateTermEntry(TermEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, entry.getBaseEntry());
        return entry.getID();
    }
    public void addIsolateTermEntrys(List<DBObject> entrys) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, entrys);
    }
    public void addList(List<TermEntry> entrys) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, MongoUtils.fetchDBObjectList(entrys));
    }
    
    public int count(ObjectId sid){
        BasicDBObject query =new BasicDBObject(Constant.ID,new BasicDBObject("$exists", true)).append("sid",sid);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM,query);
    }

    public void updateIsolateTermEntry(TermEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, updateValue);
    }

    public TermEntry findIsolateTermEntryEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS);
        if (dbObject != null) {
            return new TermEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }
    public List<TermEntry> getTermByYear(ObjectId sid,Integer year) {
    	List<TermEntry> entries = new ArrayList<TermEntry>();
    	BasicDBObject query = new BasicDBObject("sid", sid).append("year", year);
    	 List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS);
    	 if(null!=dbObject && !dbObject.isEmpty())
         {
             for(DBObject dbo:dbObject)
             {
                 entries.add(new TermEntry((BasicDBObject)dbo));
             }
         }
         return entries;
    }
    public List<TermEntry> getIsolateTermEntrysBySid(ObjectId sid) {
        List<TermEntry> entries = new ArrayList<TermEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS, new BasicDBObject("st", -1));
        if(null!=dbObject && !dbObject.isEmpty())
        {
            for(DBObject dbo:dbObject)
            {
                entries.add(new TermEntry((BasicDBObject)dbo));
            }
        }
        return entries;
    }

    public List<TermEntry> getIsolateTermEntrysByTid(ObjectId tid) {
        List<TermEntry> entries = new ArrayList<TermEntry>();
        BasicDBObject query = new BasicDBObject("tid", tid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS, new BasicDBObject("st", -1));
        if(null!=dbObject && !dbObject.isEmpty())
        {
            for(DBObject dbo:dbObject)
            {
                entries.add(new TermEntry((BasicDBObject)dbo));
            }
        }
        return entries;
    }
    
    /**
     * 正序
     * @param sid
     * @return
     */
    public List<TermEntry> getIsolateTermEntrysBySidOrder(ObjectId sid) {
        List<TermEntry> entries = new ArrayList<TermEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS, new BasicDBObject("st", 1));
        if(null!=dbObject && !dbObject.isEmpty())
        {
            for(DBObject dbo:dbObject)
            {
                entries.add(new TermEntry((BasicDBObject)dbo));
            }
        }
        return entries;
    }

    public void removeIsolateTermEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query);
    }

    public List<TermEntry> getIsolateTermListByTime(Long time, ObjectId sid) {
        List<TermEntry> entries = new ArrayList<TermEntry>();
//        "st", new BasicDBObject(Constant.MONGO_LTE,time)
        BasicDBObject query = new BasicDBObject()
                .append("sid",sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS,new BasicDBObject("st",-1));
        if(null!=dbObject && !dbObject.isEmpty())
        {
            for(DBObject dbo:dbObject)
            {
                entries.add(new TermEntry((BasicDBObject)dbo));
            }
        }
        return entries;
    }

    public List<TermEntry> getIsolateTermListByYear(ObjectId sid) {
        List<TermEntry> entries = new ArrayList<TermEntry>();
//        "st", new BasicDBObject(Constant.MONGO_LTE,time)
        BasicDBObject query = new BasicDBObject()
                .append("sid",sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS,new BasicDBObject("year",-1));
        if(null!=dbObject && !dbObject.isEmpty())
        {
            for(DBObject dbo:dbObject)
            {
                entries.add(new TermEntry((BasicDBObject)dbo));
            }
        }
        return entries;
    }



    public List<TermEntry> getIsolateTermListBySid(ObjectId sid) {
        List<TermEntry> entries = new ArrayList<TermEntry>();
        BasicDBObject query = new BasicDBObject("sid", sid);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS);
        if(null!=dbObject && !dbObject.isEmpty())
        {
            for(DBObject dbo:dbObject)
            {
                entries.add(new TermEntry((BasicDBObject)dbo));
            }
        }
        return entries;
    }
    public void DelAll(ObjectId xqid) {
        DBObject query = new BasicDBObject("xqid", xqid);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query);
    }
    
    public TermEntry getTermByTime(ObjectId schoolId,long date) {
    	BasicDBObject query = new BasicDBObject("st", new BasicDBObject(Constant.MONGO_LTE, date)).append("et", new BasicDBObject(Constant.MONGO_GTE, date))
    			.append("sid", schoolId);
    	DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS);
    	if(dbo!=null) {
    		return new TermEntry((BasicDBObject)dbo);
    	}
    	return null;
    }

    public void removePaiKeTime(ObjectId termId,ObjectId timeId){
        BasicDBObject query = new BasicDBObject(Constant.ID,termId).append("times._id",timeId);
        BasicDBObject updateval = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("times.$.ir",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM,query,updateval);
    }

    public void addPaikeTime(ObjectId termId,TermEntry.PaiKeTimes paiKeTime){
        paiKeTime.setID(new ObjectId());
        paiKeTime.setIr(0);
        BasicDBObject query = new BasicDBObject(Constant.ID,termId);
        BasicDBObject updateval = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("times", paiKeTime.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM,query,updateval);
    }

    public void updatePaikeTime(ObjectId termId,ObjectId timeId,TermEntry.PaiKeTimes paiKeTime){
        BasicDBObject query = new BasicDBObject(Constant.ID,termId).append("times._id",timeId);
        BasicDBObject updateval = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("times.$.desc",paiKeTime.getDescription())
                .append("times.$.gradeids",MongoUtils.convert(paiKeTime.getGradeIds())));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM,query,updateval);
    }

    public TermEntry getTermByTimeId(ObjectId timeId) {
        BasicDBObject query = new BasicDBObject("times._id", timeId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_ISOLATE_TERM, query, Constant.FIELDS);
        if(dbo!=null) {
            return new TermEntry((BasicDBObject)dbo);
        }
        return null;
    }
}
