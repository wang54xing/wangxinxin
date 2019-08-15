package com.pojo.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

/**
 * mongo的辅助类
 * @author fourer
 *
 */
public class MongoUtils {

	private static final Logger logger =Logger.getLogger(MongoUtils.class);
	

	/**
	 * 创建正则查询，不区分大小写
	 * @param queryValue
	 * @return
	 */
	public static DBObject buildRegex(String queryValue)
	{
		return new BasicDBObject().append(Constant.MONGO_REGEX, queryValue).append("$options", "$i");
	}
	
	
	/**
	 * 抽取DBObject
	 * @param col
	 * @return
	 */
	public static  List<DBObject> fetchDBObjectList(Collection<? extends BaseDBObject> col)
	{
		List<DBObject> retList =new ArrayList<DBObject>();
		if(null!=col)
		{
			for(BaseDBObject t:col)
			{
			  retList.add(t.getBaseEntry());
			}
		}
		return retList;
	}
	/**
	 * 得到ID
	 * @param col
	 * @return
	 */
	public static  List<ObjectId> getFieldObjectIDs(Collection<? extends BaseDBObject> col)
	{
		List<ObjectId> retList =new ArrayList<ObjectId>();
		if(null!=col)
		{
			for(BaseDBObject t:col)
			{
			  retList.add(t.getID());
			}
		}
		return retList;
	}
	/**
	 * 得到ObjectId的list;注意：该field为ObjectId
	 * @param col
	 * @param field
	 * @return
	 */
	public static  List<ObjectId> getFieldObjectIDs(Collection<? extends BaseDBObject> col,String field)
	{
		List<ObjectId> retList =new ArrayList<ObjectId>();
		if(null!=col)
		{
			for(BaseDBObject t:col)
			{
					try
					{
					  retList.add(t.getBaseEntry().getObjectId(field));
					}catch(Exception ex)
					{
						logger.error("", ex);
					}
			}
		}
		return retList;
	}
	
	
	
	/**
	 * 得到ObjectId的list;注意：该field为ObjectId
	 * @param col
	 * @param field
	 * @return
	 */
	public static  List<ObjectId> getFieldObjectIDs(BaseDBObject dbo,String field)
	{
		List<ObjectId> retList =new ArrayList<ObjectId>();
		if(null!=dbo)
		{
			BasicDBList list =(BasicDBList)dbo.getBaseEntry().get(field);
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					retList.add((ObjectId)o);
				}
			}
			return retList;
		}
		return retList;
	}
	
	
	/**
	 * 将集合转变成BasicDBList
	 * @param ids
	 * @return
	 */
	public static <T> BasicDBList convert (Collection<T> ids)
	{
		BasicDBList retList=new BasicDBList();
		
		if(null!=ids && ids.size()>0)
		{
			for(T id:ids)
			{
				retList.add(id);
			}
		}
		return retList;
	}
	
	
	/**
	 * 比较两个ObjectId，并且返回一个顺序的ObjectId[] 数组
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static ObjectId[] compare(ObjectId o1,ObjectId o2)
	{
		int a=o1.compareTo(o2);
		
		if(a>Constant.ZERO)
			return new ObjectId[]{o1,o2};
		if(a<Constant.ZERO)
			return new ObjectId[]{o2,o1};
		
		int h1=o1.hashCode();
		int h2=o2.hashCode();
		
		a=h1-h2;
		if(a>Constant.ZERO)
			return new ObjectId[]{o1,o2};
		else
			return new ObjectId[]{o2,o1};
	}

	
	/**
	 * 将id,id 类型的字符串转变成  List<ObjectId>
	 * 如有非ObjectId字符，则抛出异常
	 * @param ids
	 * @return
	 */
	public static List<ObjectId> convert(String ids) 
	{
		List<ObjectId> retList =new ArrayList<ObjectId>();
		if(StringUtils.isNotBlank(ids))
		{
			String[] arr=StringUtils.split(ids, Constant.COMMA);
			for(String id:arr)
			{
				if(StringUtils.isNotBlank(id))
				{
					try
					{
					retList.add(new ObjectId(id));
					}catch(Exception ex)
					{
						
					}
				}
			}
		}
		return retList;
	}
	
	/**
	 * 将多个ObjectId 对象转变成字符串，并且用“,”连接
	 * @param ids
	 * @return
	 * @throws IllegalParamException
	 */
	public static String convertToStr(Collection<ObjectId> ids) 
	{
		StringBuilder builder =new StringBuilder();
		if(null!=ids)
		{
			for(ObjectId id:ids)
			{
				if(null!=id)
				{
				  builder.append(id.toString()).append(Constant.COMMA);
				}
			}
		}
		return builder.toString();
	}
	
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public static List<String> convertToStringList(Collection<ObjectId> ids) 
	{
		List<String> retList =new ArrayList<String>();
		if(null!=ids)
		{
			for(ObjectId id:ids)
			{
				if(null!=id)
				{
					retList.add(id.toString());
				}
			}
		}
		return retList;
	}

	public static List<ObjectId> convertToObjectIdList(Collection<String> ids){
		List<ObjectId> retList = new ArrayList<ObjectId>();
		if(null != ids){
			for(String id : ids){
				retList.add(new ObjectId(id));
			}
		}
		return retList;
	}
	
	
}

