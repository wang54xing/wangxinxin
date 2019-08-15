package com.db.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.log.SimpleLogEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 简单日志
 * @author fourer
 *
 */
public class SimpleLogDao extends BaseDao {

	/**
	 * 添加
	 * @param list
	 */
	public void addSimpleLogs(List<SimpleLogEntry> list)
	{
		if(null!=list && list.size()>0)
		{
			List<DBObject> insertList =MongoUtils.fetchDBObjectList(list);
		    save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_SIMPLELOG, insertList);
		}
	}
	
	
	/**
	 * 查询记录
	 * @param beginTime 查询开始时间
	 * @param endTime 查询结束时间
	 * @param userIds 用户
	 * @param schoolIds 学校
	 * @param pfs 平台
	 * @param apps 应用
	 * @return
	 */
	public List<SimpleLogEntry> getSimpleLogEntry(Long beginTime,Long endTime, Collection<ObjectId> userIds,Collection<ObjectId> schoolIds, Collection<Integer> pfs, Collection<Integer> apps,DBObject fields)
	{
		List<SimpleLogEntry> retList =new ArrayList<SimpleLogEntry>();
		BasicDBObject queryDBO=new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_GTE,new ObjectId(new Date(beginTime))).append(Constant.MONGO_LTE,new ObjectId(new Date(endTime))));
		
		if(null!=userIds && userIds.size()>0)
		{
			queryDBO.append("ui", new BasicDBObject(Constant.MONGO_IN,userIds));
		}
		if(null!=schoolIds && schoolIds.size()>0)
		{
			queryDBO.append("si", new BasicDBObject(Constant.MONGO_IN,schoolIds));
		}
		if(null!=pfs && pfs.size()>0)
		{
			queryDBO.append("pf", new BasicDBObject(Constant.MONGO_IN,pfs));
		}
		if(null!=apps && apps.size()>0)
		{
			queryDBO.append("app", new BasicDBObject(Constant.MONGO_IN,apps));
		}
		
		List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_SIMPLELOG,queryDBO,fields);
	    for(DBObject dbObject:dbObjectList){
	    	SimpleLogEntry logEntry=new SimpleLogEntry((BasicDBObject)dbObject);
	    	retList.add(logEntry);
	    }
		return retList;
	}
	
}
