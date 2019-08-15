package com.db.app;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.RegionEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 地区dao
 * @author fourer
 *
 */
public class RegionDao extends BaseDao {
  
	/**
	 * 增加一个地区
	 * @param e
	 * @return
	 */
	public ObjectId addRegionEntry(RegionEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 增加多个地区
	 * @param rs
	 */
	public void addRegionEntrys(Collection<RegionEntry> rs)
	{
		 List<DBObject> list=MongoUtils.fetchDBObjectList(rs);
		 if(null!=list && !list.isEmpty())
		 {
			 save(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME, list);
		 }
	}
	
	/**
	 * 根据ID查询
	 * @param objectId
	 * @return
	 */
	  public RegionEntry getRegionById(ObjectId objectId){
	        BasicDBObject query =new BasicDBObject(Constant.ID,objectId);
	        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME, query, Constant.FIELDS);
	        if(null!=dbo)
	        {
	            return new RegionEntry((BasicDBObject)dbo);
	        }
	        return null;
	    }
	
	
	/**
	 * 按照级别和父code查询
	 * @param level level大于0时生效
	 * @param parentCode parentCode不为“”时生效
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<RegionEntry> getRegionEntryList(int level,ObjectId parentId) throws ResultTooManyException
	{
		List<RegionEntry> retList =new ArrayList<RegionEntry>();
		BasicDBObject query =new BasicDBObject();
		if(level>0)
		{
			query.append("lel", level);
		}
		if(null!=parentId)
		{
			query.append("pid", parentId);
		}
		
		if(query.isEmpty())
			throw new ResultTooManyException("find too many result by this condition");
		
		List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME, query, Constant.FIELDS, new BasicDBObject("so",Constant.DESC));
		
		if(null!=dboList && !dboList.isEmpty())
		{
			for(DBObject dbo:dboList)
			{
				retList.add(new RegionEntry((BasicDBObject)dbo));
			}
		}
		return retList;
	}
	
	
	/**
	 * 查询所有记录，用于缓存地区数据查询
	 * @return
	 */
	public List<RegionEntry> getAllRegionEntry() 
	{
		List<RegionEntry> retList =new ArrayList<RegionEntry>();
		BasicDBObject query =new BasicDBObject();
		List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME, query, Constant.FIELDS, new BasicDBObject("so",Constant.DESC));
		
		if(null!=dboList && !dboList.isEmpty())
		{
			for(DBObject dbo:dboList)
			{
				retList.add(new RegionEntry((BasicDBObject)dbo));
			}
		}
		return retList;
	}
	
	
	
	/**
	 * 根据name精确匹配数据
	 * 该方法不建议使用，仅仅用于导数据
	 * @param name
	 * @return
	 */
	@Deprecated
	public RegionEntry getRegionEntryByName(String name)
	{
		BasicDBObject query =new BasicDBObject("nm",name);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new RegionEntry((BasicDBObject)dbo);
		}
		return null;
	}

	/**
	 * 根据ID集合查找region列表
	 * @param regionIds
	 * @return
	 */
	public List<RegionEntry> getRegionEntryList(List<ObjectId> regionIds) {
		List<RegionEntry> retList =new ArrayList<RegionEntry>();
		BasicDBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,regionIds));
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME, query, Constant.FIELDS);
		if(null!=list && !list.isEmpty())
		{
			for(DBObject dbo:list)
			{
				retList.add(new RegionEntry((BasicDBObject)dbo));
			}
		}
		return retList;
	}

	public Map<String, RegionEntry> getRegionEntryMap(ObjectId regionId) {
		Map<String, RegionEntry> map =new HashMap<String, RegionEntry>();
		BasicDBObject query =new BasicDBObject();
		if(regionId!=null) {
			query.append(Constant.ID, regionId);
		}
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME, query, Constant.FIELDS);
		if(null!=list && !list.isEmpty())
		{
			RegionEntry e;
			for(DBObject dbo:list)
			{
				e=new RegionEntry((BasicDBObject)dbo);
				map.put(e.getID().toString(), e);
			}
		}
		return map;
	}

	/**
	 * 修改数据
	 * @param id
	 * @param level
	 * @param pid
	 */
	public void update(ObjectId id,int level,ObjectId pid)
	{
		BasicDBObject query=new BasicDBObject(Constant.ID,id);
		BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("lel",level)
		.append("pid",pid));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME, query,updateValue);
	}

	/**
	 * 根据ID集合查找region列表
	 * @param regionIds
	 * @return
	 */
	public List<RegionEntry> getRegionEntryListByPid(List<ObjectId> regionIds) {
		List<RegionEntry> retList =new ArrayList<RegionEntry>();
		/*BasicDBList basicDBList=new BasicDBList();
		basicDBList.add(new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,regionIds)));
		basicDBList.add(new BasicDBObject("pid",new BasicDBObject(Constant.MONGO_IN,regionIds)));*/
		BasicDBObject query =new BasicDBObject("pid",new BasicDBObject(Constant.MONGO_IN,regionIds));
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REGION_NAME,query, Constant.FIELDS);
		if(null!=list && !list.isEmpty())
		{
			for(DBObject dbo:list)
			{
				retList.add(new RegionEntry((BasicDBObject)dbo));
			}
		}
		return retList;
	}
}
