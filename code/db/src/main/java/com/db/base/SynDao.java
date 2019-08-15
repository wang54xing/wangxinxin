package com.db.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sys.constants.Constant;

/**
 * 同步数据库操作类，需要同步的dao应该继承此类
 * @author fourer
 *
 */
public class SynDao extends BaseDao {

	private static final BasicDBObject SYN_QUERY =new BasicDBObject(Constant.FIELD_SYN,Constant.SYN_YES_NEED);
	
	/**
	 * 总数
	 * @param collectionName
	 * @return
	 */
	public int getSynCount(String collectionName)
	{
		return count(MongoFacroty.getAppDB(), collectionName, SYN_QUERY);
	}
	
	/**
	 * 分页查询需要同步的实体
	 * @param collectionName
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<DBObject> getSynDBObjectList(String collectionName,int skip,int limit)
	{
		return find(MongoFacroty.getAppDB(), collectionName, SYN_QUERY, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
	}
	
	/**
	 * 分页查询需要同步的实体,map形式
	 * @param collectionName
	 * @param skip
	 * @param limit
	 * @return
	 */
	public Map<ObjectId,DBObject> getSynDBObjectMap(String collectionName,int skip,int limit)
	{
		Map<ObjectId,DBObject> retMap =new HashMap<ObjectId, DBObject>();
		List<DBObject> list= find(MongoFacroty.getAppDB(), collectionName, SYN_QUERY, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
		if(null!=list && list.size()>0)
		{
			ObjectId id;
			for(DBObject dbo:list)
			{
				id=(ObjectId)dbo.get(Constant.ID);
				retMap.put(id, dbo);
			}
		}
		return retMap;
	}
	
	
	/**
	 * 更新syn字段,由搜索调用
	 * @param collectionName
	 * @param ids
	 */
	public void updateSyn(String collectionName,Collection<ObjectId> ids)
	{
		BasicDBObject dboQuery =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
		BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new  BasicDBObject(Constant.FIELD_SYN,Constant.SYN_NOT_NEED));
		update(MongoFacroty.getAppDB(), collectionName, dboQuery, updateValue);
	}
	
	
	
	
	
}
