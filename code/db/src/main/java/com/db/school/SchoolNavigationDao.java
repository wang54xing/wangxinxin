package com.db.school;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.SchoolNavigationEntry;
import com.sys.constants.Constant;

/**
 * 学校导航操作类
 * @author fourer
 *
 */
public class SchoolNavigationDao extends BaseDao {

	/**
	 * 添加记录
	 * @param e
	 * @return
	 */
	public ObjectId addSchoolNavigationEntry(SchoolNavigationEntry e)
	{
		 save(MongoFacroty.getAppDB(), Constant.COLLECTION_NAV, e.getBaseEntry());
	     return e.getID();
	}
	
	/**
	 * 查询
	 * @param sid 学校id
	 * @param types 类型
	 * @param modleIds 模块id
	 * @param role 用户role
	 * @return
	 */
	public List<SchoolNavigationEntry> getSchoolNavigationEntryList(ObjectId sid,List<Integer> types,List<Integer> modleIds)
	{
		List<SchoolNavigationEntry> retList =new ArrayList<SchoolNavigationEntry>();
		
		BasicDBObject query =new BasicDBObject();
		
		if(null!=sid)
		{
			query.append("rsids",new BasicDBObject(Constant.MONGO_NOTIN,Arrays.asList(sid)));
		}
		if(null!=types && types.size()>0)
		{
			query.append("ty", new BasicDBObject(Constant.MONGO_IN,types));
		}
		if(null!=modleIds && modleIds.size()>0)
		{
			query.append("mid", new BasicDBObject(Constant.MONGO_IN,modleIds));
		}
		
		
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NAV,query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new SchoolNavigationEntry((BasicDBObject)dbo));
            }
        }
        
        
		return retList;
	}
	
	
	
	/**
	 * 要修改的item
	 * @param id
	 * @param schools 学校ID
	 */
	public void removeSchools(ObjectId id,List<ObjectId> schools)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSHALL, new BasicDBObject("rsids", schools));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NAV, query, updateValue);
	}
}
