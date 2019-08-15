package com.db.base;

import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.AggregationOutput;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;
import com.mongodb.WriteResult;

public class BaseDao {

	
	private static final Logger logger =Logger.getLogger("SlowSQL");
	
	private static final long time=2000;
	/**
	 * save a DBObject
	 * @param db
	 * @param name
	 * @param dbo
	 */
	protected WriteResult save(DB db,String name,DBObject dbo)
	{
		long a=System.currentTimeMillis();
		WriteResult res= db.getCollection(name).save(dbo);
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info(" save time="+(b-a)+";name="+name+";dbo:"+dbo);
		}
		return res;
	}

	/**
	 * save a DBObject list
	 * @param db
	 * @param name
	 * @param list
	 */
	protected WriteResult save(DB db,String name, List<DBObject> list)
	{
		long a=System.currentTimeMillis();
		WriteResult res= db.getCollection(name).insert(list);
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info(" save time="+(b-a)+";name="+name);
		}
		return res;
	}
	
	/**
	 * remove something
	 * @param db
	 * @param name
	 * @param query
	 */
	protected WriteResult remove(DB db,String name,DBObject query)
	{
		long a=System.currentTimeMillis();
		WriteResult res= db.getCollection(name).remove(query);
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info(" save time="+(b-a)+";name="+name+";query:"+query.toString());
		}
		return res;
	}

	/**
	 * update
	 * @param db
	 * @param name
	 * @param query
	 * @param updateValue
	 */
	protected WriteResult update(DB db,String name,DBObject query,DBObject updateValue)
	{
		long a=System.currentTimeMillis();
		WriteResult res= db.getCollection(name).update(query, updateValue,false,true);
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info(" update time="+(b-a)+";name="+name+";query:"+query.toString()+";updateValue:"+updateValue);
		}
		return res;
	}
	
	/**
	 * 查询数量
	 * @param db
	 * @param name
	 * @param query
	 * @return
	 */
	protected int count(DB db,String name,DBObject query)
	{
		return db.getCollection(name).find(query).count();
	}
	
	/**
	 * find 查询
	 * @param db
	 * @param name
	 * @param query
	 * @return
	 */
	protected List<DBObject>   find(DB db,String name,DBObject query,DBObject fields)
	{
		long a=System.currentTimeMillis();
		List<DBObject> list=db.getCollection(name).find(query,fields).toArray();
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info("time="+(b-a)+";name="+name+";query:"+query.toString()+";fields:"+fields+"size:"+(null==list?"0":list.size()));
		}
		return list;
	}
	/**
	 * 查询单个记录
	 * @param db
	 * @param name
	 * @param query
	 * @return
	 */
	protected DBObject findOne(DB db,String name,DBObject query,DBObject fields)
	{
		return db.getCollection(name).findOne(query,fields);
	}
	/**
	 * 
	 * @param db
	 * @param name
	 * @param query
	 * @param fields
	 * @param orderBy
	 * @return
	 */
	protected List<DBObject> find(DB db,String name,DBObject query,DBObject fields,DBObject orderBy)
	{
		long a=System.currentTimeMillis();
		List<DBObject> list= db.getCollection(name).find(query,fields).sort(orderBy).toArray();
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info("time="+(b-a)+";name="+name+";query:"+query.toString()+";fields:"+fields+"size:"+(null==list?"0":list.size()));
		}
		return list;
	} 
	
	/**
	 * 分页查询
	 * @param db
	 * @param name
	 * @param query
	 * @param fields
	 * @param orderBy
	 * @param skip
	 * @param limit
	 * @return
	 */
	protected List<DBObject> find(DB db,String name,DBObject query,DBObject fields,DBObject orderBy,int skip,int limit)
	{
		long a=System.currentTimeMillis();
		List<DBObject> list= db.getCollection(name).find(query,fields).sort(orderBy).skip(skip).limit(limit).toArray();
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info("time="+(b-a)+";name="+name+";query:"+query.toString()+";fields:"+fields+"size:"+(null==list?"0":list.size()));
		}
		return list;
	}

	/**
	 *
	 * @param db
	 * @param name
	 * @param query
	 * @param fields
	 * @param orderBy
	 * @param skip
	 * @return
	 */
	protected List<DBObject> find(DB db,String name,DBObject query,DBObject fields,DBObject orderBy,int skip)
	{
		long a=System.currentTimeMillis();
		List<DBObject> list= db.getCollection(name).find(query,fields).sort(orderBy).skip(skip).toArray();
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info("time="+(b-a)+";name="+name+";query:"+query.toString()+";fields:"+fields+"size:"+(null==list?"0":list.size()));
		}
		return list;
	}


	/**
	 * 集合，分组
	 * @param db
	 * @param name
	 * @param firstOp
	 * @param additionalOps
	 * @return
	 */
	protected AggregationOutput aggregate(DB db,String name,DBObject firstOp, DBObject ... additionalOps) throws Exception
	{
		long a=System.currentTimeMillis();
		AggregationOutput output= db.getCollection(name).aggregate(firstOp, additionalOps);
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info("time="+(b-a)+";name="+name+";firstOp:"+firstOp+";additionalOps:"+additionalOps);
		}
		return output;
	}
	
	
	/**
	 * 
	 * @param db
	 * @param name
	 * @param query
	 * @param map map函数
	 * @param reduce reduce函数
	 * @param outputTarget 结果
	 * @return
	 * @throws Exception
	 */
	protected MapReduceOutput mapReduce(DB db,String name,DBObject query,String map,String reduce,String outputTarget) throws Exception
	{
		long a=System.currentTimeMillis();
		MapReduceOutput output= db.getCollection(name).mapReduce(map, reduce, outputTarget, query);
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info("time="+(b-a)+";name="+name+";query:"+query+";map:"+map);
		}
		return output;
	}
	
	
	
	/**
	 * mapreduce 操作
	 * @param db
	 * @param mapReduce
	 * @return
	 * @throws Exception
	 */
	protected CommandResult mapReduce(DB db,DBObject mapReduce) throws Exception
	{
		long a=System.currentTimeMillis();
		CommandResult output= db.command(mapReduce);
		long b=System.currentTimeMillis();
		if(b-a>time)
		{
		  logger.info("time="+(b-a)+";name="+db.getName()+";mapReduce:"+mapReduce);
		}
		return output;
	}
	
	
}
