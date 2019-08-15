package com.db.new33.paike;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.N33_TeaRules;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.new33.paike.N33_RequireTime;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_TeaRulesDao extends BaseDao {

    public void removeTeaRules(ObjectId ids){
        DBObject query = new BasicDBObject("_id", ids);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEARULES, query);
    }

    public void removeTeaRulesByXqidAndSIDAndTeaId(ObjectId xqid,ObjectId sid,ObjectId teaId){
        DBObject query = new BasicDBObject("xqid", xqid).append("sid",sid).append("teaId",teaId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEARULES, query);
    }

    public void addTeaRules(N33_TeaRules entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEARULES, entry.getBaseEntry());
    }

    public void updateTeaRules(N33_TeaRules entry){
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEARULES, query, updateValue);
    }

    public N33_TeaRules getTeaRulesByXqidAndTeaId(ObjectId xqid,ObjectId teaId){
        BasicDBObject query = new BasicDBObject("xqid",xqid).append("teaId",teaId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEARULES, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_TeaRules((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    public N33_TeaRules getTeaRulesById(ObjectId ruleId){
        BasicDBObject query = new BasicDBObject("_id",ruleId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEARULES, query, Constant.FIELDS);
        if (dbObject != null) {
            return new N33_TeaRules((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    /**
     * 同意或拒绝某一条需求
     * @param ids
     * @param require
     */
    public void updateEachTeaRules(ObjectId ids,Integer require){
        DBObject query = new BasicDBObject("teaRulesList._id", ids);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("teaRulesList.$.require",require));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_TEARULES, query, updateValue);
    }

	/**
	 *
	 * @param sid
	 * @param termId
	 * @return
	 */
	public N33_RequireTime getRequireTime(ObjectId sid,ObjectId termId){
	    BasicDBObject query = new BasicDBObject("sid",sid).append("termId",termId);
	    DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_REQUIRETIME, query, Constant.FIELDS);
	    if (dbObject != null) {
		    return new N33_RequireTime((BasicDBObject) dbObject);
	    } else {
		    return null;
	    }
    }

	/**
	 * 修改教师提需求的时间
	 * @param requireTime
	 */
	public void updateRequireTime(N33_RequireTime requireTime){
	    DBObject query = new BasicDBObject(Constant.ID, requireTime.getID());
	    DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, requireTime.getBaseEntry());
	    update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_REQUIRETIME, query, updateValue);
    }

	public void addRequireTime(N33_RequireTime entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_REQUIRETIME, entry.getBaseEntry());
	}
}
