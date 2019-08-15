package com.db.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.school.LeagueEnrty;
import com.sys.constants.Constant;

/**
 * 联盟资源
 * @author fourer
 *
 */
public class LeagueDao extends BaseDao {

	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId add(LeagueEnrty e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_LEAGUE_NAME, e.getBaseEntry());
		return e.getID();
	}
	
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public LeagueEnrty getDetail(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LEAGUE_NAME, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new LeagueEnrty((BasicDBObject)dbo);
		}
		return null;
	}
	
	
	/**
	 * 添加一个学校
	 * @param id
	 * @param pair
	 * @param type 0： 删除 1：添加
	 */
	public void operSchool(ObjectId id,IdValuePair pair,int type)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		String oper=Constant.ZERO==type?Constant.MONGO_PULL:Constant.MONGO_PUSH;
		DBObject updateValue =new BasicDBObject(oper,new BasicDBObject("sch",pair.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_LEAGUE_NAME, query, updateValue);
	}
	
	
	/**
	 * 按照学校ID查询
	 * @param schoolId
	 * @return
	 */
	public List<LeagueEnrty> getLeagueEnrtys(ObjectId schoolId,DBObject fields)
	{
		List<LeagueEnrty> retList =new ArrayList<LeagueEnrty>();
		DBObject query =new BasicDBObject("sch.id",schoolId);
		List<DBObject> list =find(MongoFacroty.getAppDB(), Constant.COLLECTION_LEAGUE_NAME, query, fields);
		if(null!=list && list.size()>Constant.ZERO)
		{
			for(DBObject dbo:list)
			{
				retList.add(new LeagueEnrty((BasicDBObject)dbo) ) ;
			}
		}
		
		return retList;
	}
	
}
