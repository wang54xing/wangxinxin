package com.pojo.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户积分日志
 * 
 * <pre>
 * collectionName:uexperlogs
 * </pre>
 *
 * <pre>
 * {
 *  ui:用户ID
 *  con:
 *  sl:用户积分日志
 *  [
 *   {
 *    
 *   }
 *  ]
 * }
 * </pre>
 * 
 * @author fourer
 */
public class UserExperienceLogEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5323790813553943057L;
	public UserExperienceLogEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public UserExperienceLogEntry(ObjectId userId,  List<ExperienceLog> list) {
		super();
		int con=0;
		if(null!=list && list.size()>Constant.ZERO)
		{
			con=list.size();
		}
		BasicDBObject baseEntry =new BasicDBObject()
		.append("ui", userId)
		.append("sl", MongoUtils.convert(MongoUtils.fetchDBObjectList(list)))
		.append("con", con);
		setBaseEntry(baseEntry);
	}
	
	/**
	 * 此方法仅仅应用于查询分页情况
	 * @param userId
	 * @param list
	 * @param count
	 */
	public UserExperienceLogEntry(ObjectId userId,  List<ExperienceLog> list,int count) {
		super();
		
		BasicDBObject baseEntry =new BasicDBObject()
		.append("ui", userId)
		.append("sl", MongoUtils.fetchDBObjectList(list))
		.append("con", count);
		setBaseEntry(baseEntry);
	}
	
	

	public int getCount() {
		return getSimpleIntegerValue("con");
	}

	public void setCount(int count) {
		setSimpleValue("con", count);
	}

	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}

	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
	}

	public List<ExperienceLog> getList() {
		List<ExperienceLog> retList =new ArrayList<ExperienceLog>();
        List<DBObject> list =(List<DBObject>)getSimpleObjectValue("sl");

		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new ExperienceLog((BasicDBObject)o));
			}
		}
		return retList;
	}

	public void setList(List<ExperienceLog> list) {
		 List<DBObject> retList= MongoUtils.fetchDBObjectList(list);
		 setSimpleValue("sl", MongoUtils.convert(retList));
	}

	/**
	 * 积分日志 依附于userscore表
	 * 
	 * { ti:时间 des:描述 ex:积分 ri:关联ID }
	 * 
	 * @author fourer
	 * 
	 */
	public static class ExperienceLog extends BaseDBObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ExperienceLog(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public ExperienceLog(String des, int experience, ObjectId relariveId) {
			super();

			BasicDBObject baseEntry = new BasicDBObject()
					.append("ti", System.currentTimeMillis())
					.append("des", des).append("ex", experience)
					.append("ri", relariveId);
			setBaseEntry(baseEntry);
		}

        public ExperienceLog(String des, int experience, long time,int expLogTypeOrdinal, ObjectId relariveId) {
            super();

            BasicDBObject baseEntry = new BasicDBObject()
                    .append("ti",time)
                    .append("des", des)
                    .append("ex", experience)
                    .append("ty", expLogTypeOrdinal)
                    .append("ri", relariveId);
            setBaseEntry(baseEntry);
        }

		public long getTime() {
			return getSimpleLongValue("ti");
		}

		public void setTime(long time) {
			setSimpleValue("ti", time);
		}

		public String getDes() {
			return getSimpleStringValue("des");
		}

		public void setDes(String des) {
			setSimpleValue("des", des);
		}

		public int getExperience() {
			return getSimpleIntegerValue("ex");
		}

		public void setExperience(int experience) {
			setSimpleValue("ex", experience);
		}

        public int getExpLogTypeOrdinal() {
            return getSimpleIntegerValue("ty");
        }

        public void setExpLogTypeOrdinal(int expLogTypeOrdinal) {
            setSimpleValue("ty", expLogTypeOrdinal);
        }

		public ObjectId getRelariveId() {
			return getSimpleObjecIDValue("ri");
		}

		public void setRelariveId(ObjectId relariveId) {
			setSimpleValue("ri", relariveId);
		}

	}
}
