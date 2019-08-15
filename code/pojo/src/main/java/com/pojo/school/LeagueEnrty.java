package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 联盟资源
 * <pre>
 * collectionName:leagues
 * </pre>
 * <pre>
 * {
 *  nm:名字
 *  ad:管理员
 *  sch:学校集合;IdValuePair
 *   [
 *    {
 *      id:
 *      v
 *    }
 *   ]
 * }
 * </pre>
 * @author fourer
 */
public class LeagueEnrty extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2717641562290371343L;
	
	
	
	public LeagueEnrty(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}


	public LeagueEnrty(String name, ObjectId admin, List<IdValuePair> schools) {
		super();
		
		List<DBObject> list =MongoUtils.fetchDBObjectList(schools);
		BasicDBList dbList =MongoUtils.convert(list);
		BasicDBObject baseEntry =new BasicDBObject().append("nm", name)
				.append("ad", admin)
				.append("sch", dbList);
		setBaseEntry(baseEntry);
	}


	public String getName() {
		return getSimpleStringValue("nm");
	}


	public void setName(String name) {
		setSimpleValue("nm", name);
	}


	public ObjectId getAdmin() {
		return getSimpleObjecIDValue("ad");
	}


	public void setAdmin(ObjectId admin) {
		setSimpleValue("ad", admin);
	}


	public List<IdValuePair> getSchools() {
		List<IdValuePair> retList =new ArrayList<IdValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sch");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}


	public void setSchools(List<IdValuePair> schools) {
		List<DBObject> list =MongoUtils.fetchDBObjectList(schools);
		BasicDBList dbList =MongoUtils.convert(list);
		setSimpleValue("sch", dbList);
	}
	
	
}
