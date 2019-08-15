package com.db.video;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.video.VideoEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

import org.bson.types.ObjectId;

import java.util.*;

/**
 * 视频操作表
 * index:pid
 *     {"pid":1}
 * @author fourer
 *
 */
public class VideoDao extends BaseDao {

	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addVideoEntry(VideoEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_NAME, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 更改字段值
	 * @param id
	 * @param field
	 * @param value
	 * @throws IllegalParamException
	 */
	public void update(ObjectId id,String field,Object value) throws IllegalParamException
	{
		if(field.equalsIgnoreCase("nm") || field.equalsIgnoreCase("lng") || field.equalsIgnoreCase("vsty"))
		{
			throw new IllegalParamException();
		}
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject(field,value));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_NAME, query, updateValue);
	}
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public VideoEntry getVideoEntryById(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_NAME, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new VideoEntry((BasicDBObject)dbo);
		}
		return null;
	}

    /**
     * 根据persistentid查询
     * @param persistentid
     * @return
     */
    public VideoEntry getVideoEntryByPersistentId(String persistentid)
    {
        DBObject query =new BasicDBObject("pid",persistentid);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_NAME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new VideoEntry((BasicDBObject)dbo);
        }
        return null;
    }
	
	/**
	 * 根据ID集合查询，并且返回map形式
	 * @param col
	 * @param fields
	 * @return
	 */
	public Map<ObjectId, VideoEntry> getVideoEntryMap(Collection<ObjectId> col,DBObject fields)
	{
		Map<ObjectId, VideoEntry> map =new HashMap<ObjectId, VideoEntry>();
		DBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,col));
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_NAME, query, fields);
		if(null!=list && !list.isEmpty())
		{
			VideoEntry e;
			for(DBObject dbo:list)
			{
				e=new VideoEntry((BasicDBObject)dbo);
				map.put(e.getID(), e);
			}
		}
		return map;
	}
	
	
	
	 /**
     * 根据persistentid查询
     * @param persistentid
     * @return
     */
	@Deprecated
    public VideoEntry getVideoEntryByBK(String bk)
    {
        DBObject query =new BasicDBObject("bk",bk);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_NAME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new VideoEntry((BasicDBObject)dbo);
        }
        return null;
    }
	
	
	@Deprecated
    public List<VideoEntry> getVideoEntrys(int skip,int limit)
    {
		List<VideoEntry> list =new ArrayList<VideoEntry>();
        DBObject query =new BasicDBObject();
        List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_NAME, query, Constant.FIELDS,Constant.MONGO_SORTBY_ASC,skip,limit);
        if(null!=dbos && dbos.size()>0)
        {
        	for(DBObject dbo:dbos)
        	{
        		list.add( new VideoEntry((BasicDBObject)dbo));
        	}
        }
        return list;
    }

	public void deleteVideoEntry(ObjectId videoId) {
		DBObject query =new BasicDBObject(Constant.ID,videoId);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("df", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_NAME, query, updateValue);
	}
}
