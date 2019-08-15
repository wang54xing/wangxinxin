package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserSchoolYearExperienceEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guojing on 2015/7/22.
 */
public class UserSchoolYearExperienceDao extends BaseDao {
    /**
     * 添加用户学年积分
     * @param e
     * @return
     */
    public ObjectId addUserSchoolYearExperience(UserSchoolYearExperienceEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_SCHOOL_YEAR_EXPER_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查询单条用户学年积分
     * @param userId
     * @param fields
     * @return
     */
    public UserSchoolYearExperienceEntry getOneUserSchoolYearExperience(ObjectId userId,DBObject fields) {
        BasicDBObject query =new BasicDBObject("ui",userId);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_SCHOOL_YEAR_EXPER_NAME,query,fields);
        if(null!=dbo)
        {
            return new UserSchoolYearExperienceEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 查询
     * @param classId
     * @return
     * @throws com.sys.exceptions.ResultTooManyException
     */
    public List<UserSchoolYearExperienceEntry> getUserSchoolYearExperienceList(ObjectId classId) {
        List<UserSchoolYearExperienceEntry> retList =new ArrayList<UserSchoolYearExperienceEntry>();
        BasicDBObject query =new BasicDBObject("cid",classId);

        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_SCHOOL_YEAR_EXPER_NAME, query, Constant.FIELDS);

        for(DBObject dbo:list)
        {
            retList.add(new UserSchoolYearExperienceEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 修改用户学年积分
     * @param userId
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param schoolYearExp
     * @return
     */
    public void updateUserSchoolYearExperience(ObjectId userId, ObjectId schoolId, ObjectId gradeId, ObjectId classId, int schoolYearExp) {
        DBObject query =new BasicDBObject("ui",userId);
        DBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("si", schoolId)
                        .append("gid", gradeId)
                        .append("cid", classId)
                        .append("sye", schoolYearExp)
                        .append("upt", new Date().getTime()));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_SCHOOL_YEAR_EXPER_NAME,query,updateValue);
    }

    /**
     * 清空用户学年积分
     * @return
     */
    public void clearSchoolYearExp() {
        DBObject query =new BasicDBObject();
        DBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("si", "")
                        .append("gid", "")
                        .append("cid", "")
                        .append("sye", 0)
                        .append("upt", new Date().getTime()));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_SCHOOL_YEAR_EXPER_NAME,query,updateValue);
    }
}
