package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.newschool.GradeEntry;
import com.pojo.newschool.GradeUser;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/12/29.
 */
public class GradeDao extends BaseDao {
    /**
     * 添加
     * @param e
     * @return
     */
    public GradeEntry addGradeEntry(GradeEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, e.getBaseEntry());
        return e;
    }


    /**
     * 根据名称查询
     * @param schoolId
     * @param gradeName
     * @return
     */
    public List<GradeEntry> getGradeEntrysByName(ObjectId schoolId, String gradeName) {
        List<GradeEntry> retList =new ArrayList<GradeEntry>();
        BasicDBObject query =new BasicDBObject("gnm",gradeName);
        query.append("sid", schoolId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query, Constant.FIELDS);
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new GradeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public void deleteGrade(ObjectId id) {
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query,updateValue);
    }

    /**
     * 恢复
     * @param id
     * @return
     */
    public void recoveryGrade(ObjectId id) {
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir", Constant.ZERO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query,updateValue);
    }

    /**
     * 更新
     * @param e
     */
    public void updateGradeEntry(GradeEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query,updateValue);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public GradeEntry getGradeById(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        query.append("ir", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new GradeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public List<GradeEntry> getGradeById(List<ObjectId> id) {
        List<GradeEntry> retList =new ArrayList<GradeEntry>();
        BasicDBObject query =new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,id));
        query.append("ir", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query, Constant.FIELDS);
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new GradeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 根据schoolId查询
     * @param schoolId
     * @return
     */
    public List<GradeEntry> getGradesBySchoolId(ObjectId schoolId) {
        List<GradeEntry> retList =new ArrayList<GradeEntry>();
        BasicDBObject query =new BasicDBObject("sid",schoolId);
        query.append("ir", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query, Constant.FIELDS);
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new GradeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 根据schoolId查询
     * @param schoolId
     * @param type
     * @return
     */
    public List<GradeEntry> getAllGradesBySchoolId(ObjectId schoolId, String type) {
        List<GradeEntry> retList =new ArrayList<GradeEntry>();
        BasicDBObject query =new BasicDBObject("sid",schoolId);
        query.append("ty", type);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query, Constant.FIELDS);
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new GradeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     *
     * @param id
     * @param count
     */
    public void updateClassCnt(ObjectId id, int count) {
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("clscnt",count));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query,updateValue);
    }

    /**
     * 年级名称更新
     * @param id
     * @param gradeName
     */
    public void updateGradeName(String id, String gradeName) {
        BasicDBObject query=new BasicDBObject(Constant.ID,new ObjectId(id));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("gnm",gradeName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_GRADE_NAME, query,updateValue);
    }

    /**
     * 查询年级下的老师数量
     * @param gradeId
     * @return
     */
    public int getGradeUserCountByGradeId(ObjectId gradeId,List<ObjectId> userList) {
        BasicDBObject query=new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN,userList)).append("gid", gradeId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_GRADE_USER, query);
    }

    /**
     * 查询年级下的老师分页
     * @param id
     * @param userIds
     * @param page
     * @param pageSize
     * @return
     */
    public List<GradeUser> getGradeUsersByGradeId(ObjectId id, List<ObjectId> userIds, int page, int pageSize) {
        BasicDBObject query=new BasicDBObject("gid", id).append("uid", new BasicDBObject(Constant.MONGO_IN, userIds));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_GRADE_USER, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<GradeUser> gradeUserArrayList=new ArrayList<GradeUser>();
        for(DBObject dbObject:dbObjectList){
            GradeUser gradeUser=new GradeUser((BasicDBObject)dbObject);
            gradeUserArrayList.add(gradeUser);
        }
        return gradeUserArrayList;
    }

    /**
     * 查询年级组长
     * @param gradeIds
     * @return
     */
    public List<GradeUser> getGradeUserTopByGradeIds(List<ObjectId> gradeIds) {
        BasicDBObject query=new BasicDBObject("gid", new BasicDBObject(Constant.MONGO_IN,gradeIds)).append("top", 1);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_GRADE_USER, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        List<GradeUser> gradeUserArrayList=new ArrayList<GradeUser>();
        for(DBObject dbObject:dbObjectList){
            GradeUser gradeUser=new GradeUser((BasicDBObject)dbObject);
            gradeUserArrayList.add(gradeUser);
        }
        return gradeUserArrayList;
    }

    /**
     * 年级下的老师
     * @param gradeId
     * @return
     */
    public List<GradeUser> getGradeUserByGradeId(ObjectId gradeId) {
        BasicDBObject query=new BasicDBObject("gid", gradeId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_GRADE_USER, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        List<GradeUser> gradeUserArrayList=new ArrayList<GradeUser>();
        for(DBObject dbObject:dbObjectList){
            GradeUser gradeUser=new GradeUser((BasicDBObject)dbObject);
            gradeUserArrayList.add(gradeUser);
        }
        return gradeUserArrayList;
    }

    /**
     * 创建年级老师
     * @param e
     */
    public ObjectId addGradeUser(GradeUser e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_GRADE_USER, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除年级老师
     * @param id
     */
    public void deleteGradeTeacher(ObjectId id) {
        BasicDBObject query=new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_GRADE_USER,query);
    }

    /**
     * 添加年级组长
     * @param id
     * @param gradeUserId
     */
    public void addGradeUserLeader(ObjectId id, String gradeUserId) {
        BasicDBObject query=new BasicDBObject("gid",id).append("uid",new ObjectId(gradeUserId));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("top", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GRADE_USER, query,updateValue);
    }

    /**
     * 删除年级组长
     * @param id
     */
    public void removeGradeUserLeader(ObjectId id) {
        BasicDBObject query=new BasicDBObject("gid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("top", Constant.ZERO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GRADE_USER, query,updateValue);
    }
}
