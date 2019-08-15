package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserExperienceLogEntry;
import com.pojo.user.UserExperienceLogEntry.ExperienceLog;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 用户积分日志操作
 * @author fourer
 *
 */
public class UserExperienceLogDao extends BaseDao {

	/**
	 * 添加用户
	 * @param e
	 * @return
	 */
	public ObjectId addUserExperienceLog(UserExperienceLogEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_EXPER_LOG_NAME, e.getBaseEntry());
		return e.getID();
	}
	
	
	/**
	 * 增加一个积分
	 * @param userId
	 * @param e
	 */
	public void addExperienceLogEntry(ObjectId userId,ExperienceLog e)
	{
		BasicDBObject query =new BasicDBObject("ui",userId);
		BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("sl",e.getBaseEntry()))
		.append(Constant.MONGO_INC, new BasicDBObject("con",Constant.ONE));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_EXPER_LOG_NAME, query, updateValue);
	}
	
	
	/**
	 * 积分日志
	 * @param userId
	 * @param skip
	 * @param limit
	 * @return
	 */
	public UserExperienceLogEntry getUserExperienceLogEntry(ObjectId userId,int skip,int limit)
	{
		DBObject matchDBO=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("ui",userId));
		DBObject projectDBO =new BasicDBObject(Constant.MONGO_PROJECT,new BasicDBObject("con",1).append("sl", 1));
		DBObject unbindDBO =new BasicDBObject(Constant.MONGO_UNWIND,"$sl");
		DBObject sortDBO =new BasicDBObject(Constant.MONGO_SORT,new BasicDBObject("sl.ti",Constant.DESC));
		DBObject skipDBO =new BasicDBObject(Constant.MONGO_SKIP,skip);
		DBObject limitDBO =new BasicDBObject(Constant.MONGO_LIMIT,limit);
		
		int count=0;
		List<ExperienceLog> experienceList =new ArrayList<ExperienceLog>();
		AggregationOutput output;
		try {
			output = aggregate(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_EXPER_LOG_NAME,matchDBO,projectDBO,unbindDBO,sortDBO,skipDBO,limitDBO);
			Iterator<DBObject>  iter=output.results().iterator();
			BasicDBObject userExperienceInfo;
			BasicDBObject experienceLog;
			while(iter.hasNext()) {
				userExperienceInfo=(BasicDBObject)iter.next();
				count= userExperienceInfo.getInt("con");
				experienceLog=(BasicDBObject)userExperienceInfo.get("sl");
				experienceList.add(new ExperienceLog(experienceLog));
			} 
		} catch (Exception e) {
		}
		return new UserExperienceLogEntry(userId, experienceList,count);
	}

    /**
     * 查询用户已添加的积分数
     *
     * @param userId 用户ID
     * @return
     */
    public int userExpCount(ObjectId userId) {
        BasicDBObject query =new BasicDBObject("ui",userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_EXPER_LOG_NAME, query);
    }

    /**
     * 查询用户已添加的积分数
     *
     * @param userId 用户ID
     * @param relateId 关联id
     * @param type
     * @return
     */
    public int countUserExp(ObjectId userId, String relateId,int type) {
        BasicDBObject query =new BasicDBObject("ui",userId);
        //检查关联id是否是null或是""
        if(null!=relateId&&!"".equals(relateId)&&!"null".equals(relateId)){
            query.append("sl.ri",new ObjectId(relateId));
        }
        query.append("sl.ty",type);

        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_EXPER_LOG_NAME, query, new BasicDBObject("sl.$",1).append("ui",1));
        if(null!=dbo)
        {
            return 1;
        }
        return 0;
    }
    /**
     * 积分日志增减明细
     * @param userId
     * @param currTime
     * @return
     */
    public List<ExperienceLog> getExperienceLogList(ObjectId userId, long currTime) {
        DBObject matchDBO=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("ui",userId));
        DBObject projectDBO =new BasicDBObject(Constant.MONGO_PROJECT,new BasicDBObject("con",1).append("sl", 1));
        DBObject unbindDBO =new BasicDBObject(Constant.MONGO_UNWIND,"$sl");
        DBObject matchDBO1=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("sl.ti", new BasicDBObject(Constant.MONGO_GTE, currTime)));
        List<ExperienceLog> experienceList =new ArrayList<ExperienceLog>();
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_EXPER_LOG_NAME,matchDBO,projectDBO,unbindDBO,matchDBO1);
            Iterator<DBObject>  iter=output.results().iterator();
            BasicDBObject userExperienceInfo;
            BasicDBObject experienceLog;
            while(iter.hasNext()) {
                userExperienceInfo=(BasicDBObject)iter.next();
                experienceLog=(BasicDBObject)userExperienceInfo.get("sl");
                experienceList.add(new ExperienceLog(experienceLog));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return experienceList;
    }

    /**
     * 积分日志增减明细
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<ExperienceLog> getExperienceLogList(ObjectId userId, long startTime,long endTime) {
        DBObject matchDBO=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("ui",userId));
        DBObject projectDBO =new BasicDBObject(Constant.MONGO_PROJECT,new BasicDBObject("con",1).append("sl", 1));
        DBObject unbindDBO =new BasicDBObject(Constant.MONGO_UNWIND,"$sl");
        BasicDBObject searchDBO=new BasicDBObject();

        BasicDBList dbList =new BasicDBList();

        if(startTime>0){
            dbList.add(new BasicDBObject("sl.ti",new BasicDBObject(Constant.MONGO_GTE,startTime)));
        }
        if(endTime>0) {
            dbList.add(new BasicDBObject("sl.ti", new BasicDBObject(Constant.MONGO_LTE, endTime)));
        }
        if(dbList.size()>0){
            searchDBO.append(Constant.MONGO_AND,dbList);
        }

        DBObject matchDBO1=new BasicDBObject(Constant.MONGO_MATCH,searchDBO);

        List<ExperienceLog> experienceList =new ArrayList<ExperienceLog>();
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_EXPER_LOG_NAME,matchDBO,projectDBO,unbindDBO,matchDBO1);
            Iterator<DBObject>  iter=output.results().iterator();
            BasicDBObject userExperienceInfo;
            BasicDBObject experienceLog;
            while(iter.hasNext()) {
                userExperienceInfo=(BasicDBObject)iter.next();
                experienceLog=(BasicDBObject)userExperienceInfo.get("sl");
                experienceList.add(new ExperienceLog(experienceLog));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return experienceList;
    }

    public List<ExperienceLog> getExperienceLogbyTypeList(ObjectId userId, int type) {
        DBObject matchDBO=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("ui",userId));
        DBObject projectDBO =new BasicDBObject(Constant.MONGO_PROJECT,new BasicDBObject("con",1).append("sl", 1));
        DBObject unbindDBO =new BasicDBObject(Constant.MONGO_UNWIND,"$sl");
        DBObject matchDBO1=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("sl.ty",type));
        List<ExperienceLog> experienceList =new ArrayList<ExperienceLog>();
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_EXPER_LOG_NAME,matchDBO,projectDBO,unbindDBO,matchDBO1);
            Iterator<DBObject>  iter=output.results().iterator();
            BasicDBObject userExperienceInfo;
            BasicDBObject experienceLog;
            while(iter.hasNext()) {
                userExperienceInfo=(BasicDBObject)iter.next();
                experienceLog=(BasicDBObject)userExperienceInfo.get("sl");
                experienceList.add(new ExperienceLog(experienceLog));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return experienceList;
    }



    public void updateExpLogTime(ObjectId userId,List<DBObject> list) {
        DBObject query =new BasicDBObject("ui",userId);
        DBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sl",list));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_EXPER_LOG_NAME,query,updateValue);
    }
    
   /**
    * 获取积分日志，用于统计
    * @param skip
    * @param limit
    * @return
    */
    public List<UserExperienceLogEntry> getAllUserExperienceLogEntry(int skip,int limit) {
        List<UserExperienceLogEntry> retList =new ArrayList<UserExperienceLogEntry>();
        BasicDBObject query =new BasicDBObject();
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_EXPER_LOG_NAME,query, Constant.FIELDS,Constant.MONGO_SORTBY_ASC,skip,limit);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new UserExperienceLogEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 按类型获取日经验值
     * @param userId
     * @param currTime
     * @param types
     * @return
     */
    public List<ExperienceLog> getExperienceLogListByTypes(ObjectId userId, long currTime, List<Integer> types) {
        DBObject matchDBO=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("ui",userId));
        DBObject projectDBO =new BasicDBObject(Constant.MONGO_PROJECT,new BasicDBObject("con",1).append("sl", 1));
        DBObject unbindDBO =new BasicDBObject(Constant.MONGO_UNWIND,"$sl");
        DBObject matchDBO1=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("sl.ti", new BasicDBObject(Constant.MONGO_GTE, currTime))
                .append("sl.ty", new BasicDBObject(Constant.MONGO_IN, types)));
        List<ExperienceLog> experienceList =new ArrayList<ExperienceLog>();
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_EXPER_LOG_NAME,matchDBO,projectDBO,unbindDBO,matchDBO1);
            Iterator<DBObject>  iter=output.results().iterator();
            BasicDBObject userExperienceInfo;
            BasicDBObject experienceLog;
            while(iter.hasNext()) {
                userExperienceInfo=(BasicDBObject)iter.next();
                experienceLog=(BasicDBObject)userExperienceInfo.get("sl");
                experienceList.add(new ExperienceLog(experienceLog));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return experienceList;
    }
}
