package com.db.new33.isolate;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.N33_DefaultTermEntry;
import com.sys.constants.Constant;


public class N33_DefaultTermDao extends BaseDao{

	/**
     * 更新默认学期
     *
     * @param id
     */
    public void updateDefaultTerm(N33_DefaultTermEntry entry) {
    	DBObject query = new BasicDBObject("sid", entry.getSid());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_MRXQ, query, updateValue);
    }
    public void updatePaikeTerm(ObjectId id,ObjectId paikeTermId,ObjectId paikeciId) {
        DBObject query = new BasicDBObject("_id", id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("paikexq",paikeTermId).append("paikeci",paikeciId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_MRXQ, query, updateValue);
    }
    /**
     * 设置默认学期
     *
     * @param id
     */
    public void addDefaultTerm(N33_DefaultTermEntry entry) {
    	save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_MRXQ, entry.getBaseEntry());
    }
    
    /**
	 * 查找当前学校的默认学期
	 */
	public N33_DefaultTermEntry findDefaultTermEntryBySchoolId(ObjectId sid){
		BasicDBObject query = new BasicDBObject("sid", sid);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW33_MRXQ, query, Constant.FIELDS);
        if(dbObject != null){
        	return new N33_DefaultTermEntry((BasicDBObject) dbObject);
        }else{
        	return null;
        }
	}
    
}
