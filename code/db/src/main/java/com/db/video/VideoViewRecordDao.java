package com.db.video;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.video.VideoViewRecordEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 视频记录操作表
 * index:vd-id_st-id_cl-id-st
 *     {"vd.id":1,"st.id":1,"cl.id":1,"st":1}
 * @author fourer
 *
 */
public class VideoViewRecordDao extends BaseDao {

	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addVideoViewRecord(VideoViewRecordEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_RECORD_NAME, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 
	 * @param videoId 视频ID
	 * @param studentId 学生ID
	 * @param classId 班级ID
	 * @param state 状态
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<VideoViewRecordEntry> getVideoViewRecordEntrys(ObjectId videoId,ObjectId studentId,ObjectId classId,int state) throws ResultTooManyException
	{
		List<VideoViewRecordEntry> retList =new ArrayList<VideoViewRecordEntry>();
		BasicDBObject dbo =new BasicDBObject();
		if(null!=videoId)
		{
			dbo.append("vd.id", videoId);
		}
		if(null!=studentId)
		{
			dbo.append("st.id", videoId);
		}
		if(null!=classId)
		{
			dbo.append("cl.id", videoId);
		}
		if(dbo.isEmpty())
		{
			throw new ResultTooManyException();
		}
        if(state!=-1) {
            dbo.append("st", state);
        }
		List<DBObject> res=find(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_RECORD_NAME, dbo, Constant.FIELDS);
		if(null!=res)
		{
			for(DBObject dBObject:res)
			{
				retList.add(new VideoViewRecordEntry((BasicDBObject)dBObject) );
			}
		}
		
		return retList;
	}
	
	
	
	

	/**
	 * 
	 * @param videoIds 视频列表
	 * @param state
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<VideoViewRecordEntry> getVideoViewRecordEntrys(List<ObjectId> videoIds,int state)
	{
		List<VideoViewRecordEntry> retList =new ArrayList<VideoViewRecordEntry>();
		BasicDBObject dbo =new BasicDBObject("vd.id",new BasicDBObject(Constant.MONGO_IN,videoIds));
        if(state!=-1) {
            dbo.append("st", state);
        }
		List<DBObject> res=find(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_RECORD_NAME, dbo, Constant.FIELDS);
		if(null!=res)
		{
			for(DBObject dBObject:res)
			{
				retList.add(new VideoViewRecordEntry((BasicDBObject)dBObject) );
			}
		}
		
		return retList;
	}

    /**
     *
     * @param videoIds 视频列表
     * @param state
     * @return
     * @throws ResultTooManyException
     */
    public List<VideoViewRecordEntry> getVideoViewRecordEntrysPeriod(List<ObjectId> videoIds,List<ObjectId> userIds,
                                                                     int state,long startTime,long endTime)
    {
        List<VideoViewRecordEntry> retList =new ArrayList<VideoViewRecordEntry>();
        BasicDBObject dbo =new BasicDBObject("vd.id",new BasicDBObject(Constant.MONGO_IN,videoIds));
        if(state!=-1) {
            dbo.append("st", state);
        }
        if(userIds!=null){
            dbo.append("ui.id",new BasicDBObject(Constant.MONGO_IN,userIds));
        }
        dbo.append("ti",new BasicDBObject(Constant.MONGO_GTE,startTime));
        dbo.append("ti",new BasicDBObject(Constant.MONGO_LTE,endTime));

        List<DBObject> res=find(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_RECORD_NAME, dbo, Constant.FIELDS);
        if(null!=res)
        {
            for(DBObject dBObject:res)
            {
                retList.add(new VideoViewRecordEntry((BasicDBObject)dBObject) );
            }
        }

        return retList;
    }

    public List<VideoViewRecordEntry> getVideoViewRecordEntrysPeriod(List<ObjectId> videoIds)
    {
        List<VideoViewRecordEntry> retList =new ArrayList<VideoViewRecordEntry>();
        BasicDBObject dbo =new BasicDBObject("vd.id",new BasicDBObject(Constant.MONGO_IN,videoIds));
        List<DBObject> res=find(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_RECORD_NAME, dbo, Constant.FIELDS);
        if(null!=res)
        {
            for(DBObject dBObject:res)
            {
                retList.add(new VideoViewRecordEntry((BasicDBObject)dBObject) );
            }
        }

        return retList;
    }
    public List<VideoViewRecordEntry> getVideoViewRecordEntrysPeriod(List<ObjectId> videoIds,List<ObjectId> userids)
    {
        List<VideoViewRecordEntry> retList =new ArrayList<VideoViewRecordEntry>();
        BasicDBObject dbo =new BasicDBObject("vd.id",new BasicDBObject(Constant.MONGO_IN,videoIds));
        if(userids!=null&&userids.size()>0){
            dbo.append("ui.id",new BasicDBObject(Constant.MONGO_IN,userids));
        }
        List<DBObject> res=find(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_RECORD_NAME, dbo, Constant.FIELDS);
        if(null!=res)
        {
            for(DBObject dBObject:res)
            {
                retList.add(new VideoViewRecordEntry((BasicDBObject)dBObject) );
            }
        }

        return retList;
    }
    /*
    *
    * 依据学生id 查找记录
    *
    * */
    public List<VideoViewRecordEntry> findByStudentId(ObjectId studentId) {
        BasicDBObject query=new BasicDBObject("ui.id",studentId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_VIDEO_RECORD_NAME,query,Constant.FIELDS);

        List<VideoViewRecordEntry> videoViewRecordEntries=new ArrayList<VideoViewRecordEntry>();
        if(dbObjectList!=null){
            for(DBObject dbObject:dbObjectList){
                VideoViewRecordEntry videoViewRecordEntry=new VideoViewRecordEntry((BasicDBObject)dbObject);
                videoViewRecordEntries.add(videoViewRecordEntry);
            }
        }
        return videoViewRecordEntries;
    }

    public int showLessonCount(List<ObjectId> stuIds, Set<ObjectId> videoIds, ObjectId dslId, ObjectId delId) {
        BasicDBObject query =new BasicDBObject("ui.id",new BasicDBObject(Constant.MONGO_IN,stuIds)).append("vd.id",new BasicDBObject(Constant.MONGO_IN,videoIds));
        BasicDBList dblist =new BasicDBList();
        if(dslId!=null){
            dblist.add(new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_GTE,dslId)));
        }
        if(delId!=null){
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_RECORD_NAME, query);
    }


    /**
     * 根据查询条件取得观看的视频信息
     * @param usIds
     * @param dslId
     * @param delId
     * @param fields
     * @param orderBy
     * @return
     */
    public List<VideoViewRecordEntry> getVideoViewRecordByParamList(List<ObjectId> usIds, ObjectId dslId, ObjectId delId, BasicDBObject fields, String orderBy) {
        List<VideoViewRecordEntry> retList=new ArrayList<VideoViewRecordEntry>();
        BasicDBObject query =new BasicDBObject("ui.id",new BasicDBObject(Constant.MONGO_IN,usIds));
        BasicDBList dblist =new BasicDBList();
        if(dslId!=null){
            dblist.add(new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_GTE,dslId)));
        }
        if(delId!=null){
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        BasicDBObject sort =null;
        if (!"".equals(orderBy)){
            sort =new BasicDBObject(orderBy,Constant.DESC);
        }else{
            sort =new BasicDBObject(Constant.ID,Constant.DESC);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_VIDEO_RECORD_NAME, query, fields, sort);

        for(DBObject dbo:list)
        {
            retList.add(new VideoViewRecordEntry((BasicDBObject)dbo));
        }
        return retList;
    }
    
    
    
    /*
    *
    * 根据班级id查询视频观看记录记录
    *
    * */
    public List<VideoViewRecordEntry> findVideoViewRecordByClassId(ObjectId classId) {
        DBObject dbObject=new BasicDBObject("cl.id",classId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_VIDEO_RECORD_NAME,dbObject,Constant.FIELDS);
        List<VideoViewRecordEntry> videoViewRecordEntries=new ArrayList<VideoViewRecordEntry>();
        if(dbObjectList!=null){
            for(DBObject dbObject1:dbObjectList){
                VideoViewRecordEntry videoViewRecordEntry=new VideoViewRecordEntry((BasicDBObject)dbObject1);
                videoViewRecordEntries.add(videoViewRecordEntry);
            }
        }
        return videoViewRecordEntries;
    }
}
