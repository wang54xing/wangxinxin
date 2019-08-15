package com.fulaan.cache;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sys.constants.Constant;
import com.sys.props.Resources;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 缓存操作类
 * @author fourer
 *
 */
public final class RedisUtils {

	private static final Logger logger = Logger.getLogger(RedisUtils.class);

	private static String REDIS_ADDRESS = Resources.getProperty("redis.address");

	private static int REDIS_PORT = Resources.getIntProperty("redis.port");

	private static int REDIS_MAX_ACTIVE = Resources.getIntProperty("redis.max.active");

	private static int REDIS_MAX_IDLE =  Resources.getIntProperty("redis.max.idle");

	private static int REDIS_MAX_WAIT =  Resources.getIntProperty("redis.mex.wait");

	private static int REDIS_TIMEOUT = Resources.getIntProperty("redis.timeout");
	
	private static String REDIS_PASSWORD=Resources.getProperty("redis.password");

	private static boolean REDIS_TEST_ON_BORROW = true;

	private static JedisPool JEDIS_POOL = null;

	
	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(REDIS_MAX_ACTIVE);
			config.setMaxIdle(REDIS_MAX_IDLE);
			config.setMaxWaitMillis(REDIS_MAX_WAIT);
			config.setTestOnBorrow(REDIS_TEST_ON_BORROW);
			
			if(StringUtils.isBlank(REDIS_PASSWORD))
			{
				JEDIS_POOL = new JedisPool(config, REDIS_ADDRESS, REDIS_PORT, REDIS_TIMEOUT);
			}
			else
			{
				JEDIS_POOL = new JedisPool(config, REDIS_ADDRESS, REDIS_PORT, REDIS_TIMEOUT,REDIS_PASSWORD);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	
	public synchronized static Jedis getJedis() {
		try {
			if (JEDIS_POOL != null) {
				Jedis resource = JEDIS_POOL.getResource();
				return resource;
			} 
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			JEDIS_POOL.returnResource(jedis);
		}
	}
	
	/**
	 * 缓存String
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public static synchronized void cacheString(String key,String value,int seconds)
	{
		Jedis jedis=getJedis();
		if(null!=jedis)
		{
		
			jedis.setex(key, seconds, value);
			returnResource(jedis);
		}
	}
	
	/**
	 * 从redis中取出String值
	 * @param key
	 * @return
	 */
	public static synchronized String getString(String key)
	{
		Jedis jedis=getJedis();
		String value =null;
		if(null!=jedis)
		{
			value= jedis.get(key);
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 缓存一个map<String,String>
	 * @param key
	 * @param value
	 * @param seconds过期时间 ,当大于0时生效
	 */
	public static void cacheMap(String key,Map<String,String> value,int seconds)
	{
		Jedis jedis=getJedis();
		if(null!=jedis)
		{
			 jedis.hmset(key, value);
			 if(seconds>Constant.ZERO)
			 {
				 jedis.expire(key, seconds);
			 }
			 returnResource(jedis);
		}
	}
	
	/**
	 * 缓存二进制数据
	 * @param key 
	 * @param value
	 * @param seconds
	 */
	public static void cacheBytes(String key,byte[] value,int seconds)
	{
		Jedis jedis=getJedis();
		if(null!=jedis)
		{
			 jedis.set(key.getBytes(), value);
			 if(seconds>Constant.ZERO)
			 {
				 jedis.expire(key.getBytes(), seconds);
			 }
			 returnResource(jedis);
		}
	}
	
	/**
	 * 从redis中取出二进制数据
	 * @param key
	 * @return
	 */
	public static  byte[] getBytes(String key)
	{
		Jedis jedis=getJedis();
		byte[] value =null;
		if(null!=jedis)
		{
			value= jedis.get(key.getBytes());
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 得到缓存的map
	 * @param key
	 * @return
	 */
	public static Map<String,String> getMap(String key)
	{
		Jedis jedis=getJedis();
		Map<String,String> value=null;
		if(null!=jedis)
		{
			value=jedis.hgetAll(key);
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 得到map中的某个键值
	 * @param key
	 * @param mapKey
	 * @return
	 */
	public static String getMapkeyValue(String key,String mapKey)
	{
		Jedis jedis=getJedis();
		String value=null;
		if(null!=jedis)
		{
			value=jedis.hget(key, mapKey);
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 根据key删除缓存
	 * @param key
	 */
	public static void deleteKey(String key)
	{
		Jedis jedis=getJedis();
		if(null!=jedis)
		{
			jedis.del(key);
			returnResource(jedis);
		}
	}
	
	/**
	 * 删除多个key
	 * @param keys
	 */
	public static void deleteKeys(String... keys)
	{
		Jedis jedis=getJedis();
		if(null!=jedis)
		{
			jedis.del(keys);
			returnResource(jedis);
		}
	}
	
	
}
