package com.pojo.log;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.Set;

/**
 * 微博
 * <pre>
 * collectionName:userActionLog
 * </pre>
 * <pre>
 * {
 ui:用户ID
 ur:用户角色
 pf：平台;参见Platform
 ty:日志类型
 at:日志时间,long
 an:操作名称
 sid:学校id
 gids:年级id集合
 gtys:年级类型
 cids；班级id集合
 * @author guo.jing
 */
public class LogEntry extends BaseDBObject {

    private static final long serialVersionUID = 7933555018492747487L;

    public LogEntry(BasicDBObject dbo){setBaseEntry(dbo);}

    /**
     * 构造器
     *
     */
    public LogEntry(ObjectId userId,
                    int userRole,
                    int platformType,
                    int actionType,
                    long actionTime,
                    String actionName,
                    ObjectId schoolId,
                    Set<ObjectId> gradeIds,
                    Set<Integer> gradeTys,
                    Set<ObjectId> classIds
    )
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("ui", userId)
                .append("ur", userRole)
                .append("pf", platformType)
                .append("ty", actionType)
                .append("at", actionTime)
                .append("an", actionName)
                .append("sid", schoolId)
                .append("gids", gradeIds)
                .append("gtys", gradeTys)
                .append("cids", classIds)
                ;
        setBaseEntry(dbo);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }

    public int getUserRole() {
        return getSimpleIntegerValue("ur");
    }

    public void setUserRole(int userRole) {
        setSimpleValue("ur", userRole);
    }


    public int getPlatformType() {
        return getSimpleIntegerValue("pf");
    }

    public void setPlatformType(int platformType) {
        setSimpleValue("pf", platformType);
    }

	public int getActionType() {
        return getSimpleIntegerValue("ty");
	}

	public void setActionType(int actionType) {
        setSimpleValue("ty", actionType);
	}

	public Long getActionTime() {
        return getSimpleLongValue("at");
	}

	public void setActionTime(Long actionTime) {
        setSimpleValue("at",actionTime);
	}

	public String getActionName() {
        return getSimpleStringValue("an");
	}

	public void setActionName(String actionName) {
        setSimpleValue("an", actionName);
	}

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public Set<ObjectId> getGradeIds() {
        Set<ObjectId> retList =new HashSet<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("gids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }

    public void setGradeIds(Set<ObjectId> gradeIds) {
        setSimpleValue("gids", MongoUtils.convert(gradeIds));
    }

    public Set<Integer> getGradeTys() {
        Set<Integer> retList =new HashSet<Integer>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("gtys");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((Integer)o);
            }
        }
        return retList;
    }

    public void setGradeTys(Set<Integer> gradeTys) {
        setSimpleValue("gtys", MongoUtils.convert(gradeTys));
    }

    public Set<ObjectId> getClassIds() {
        Set<ObjectId> retList =new HashSet<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("cids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }

    public void setClassIds(Set<ObjectId> classIds) {
        setSimpleValue("cids", MongoUtils.convert(classIds));
    }
}
