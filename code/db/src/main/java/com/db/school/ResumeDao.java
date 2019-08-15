package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.newschool.ResumeEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2017/1/22.
 */
public class ResumeDao extends BaseDao {

    /**
     * 增加个人信息
     * @param e
     * @return
     */
    public ObjectId addResumeEntry(ResumeEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public ResumeEntry getResumeByUserId(ObjectId id){
        BasicDBObject query =new BasicDBObject("uid", id);
        query.append("ir", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ResumeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 更新个人信息
     * @param e
     */
    public void updateResumeEntry(ResumeEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query,updateValue);
    }

    /**
     * 删除个人信息
     * @param id
     */
    public void delResumeEntry(ObjectId id) {
        BasicDBObject query=new BasicDBObject("uid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query,updateValue);
    }

    /**
     * 查询学科老师列表
     * @param userList
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public List<ResumeEntry> getSubjectTeachers(List<ObjectId> userList, ObjectId schoolId, String keyword, int page, int pageSize) {
        BasicDBObject query=new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN,userList)).append("sid", schoolId).append("ir", Constant.ZERO);
        if (!keyword.equals("keyword")) {
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);

        List<ResumeEntry> resumeEntryList=new ArrayList<ResumeEntry>();
        for(DBObject dbObject:dbObjectList){
            ResumeEntry resumeEntry=new ResumeEntry((BasicDBObject)dbObject);
            resumeEntryList.add(resumeEntry);
        }
        return resumeEntryList;
    }

    /**
     * 查询学科老师数量
     * @param userList
     * @param schoolId
     * @param keyword
     * @return
     */
    public int getSubjectTeacherCount(List<ObjectId> userList, ObjectId schoolId, String keyword) {
        BasicDBObject query=new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN,userList)).append("sid",schoolId).append("ir", Constant.ZERO);
        if (!keyword.equals("keyword")) {
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query);
    }

    /**
     * 查询老师数量
     * @param schoolId
     * @param keyword
     * @return
     */
    public int getTeacherCount(ObjectId schoolId, String keyword) {
        BasicDBObject query=new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO);
        if (!keyword.equals("keyword")) {
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query);
    }

    /**
     * 查询老师
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public List<ResumeEntry> getTeachers(ObjectId schoolId, String keyword, int page, int pageSize) {
        BasicDBObject query=new BasicDBObject("sid",schoolId).append("ir", Constant.ZERO);
        if (!keyword.equals("keyword")) {
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<ResumeEntry> resumeEntryList=new ArrayList<ResumeEntry>();
        for(DBObject dbObject:dbObjectList){
            ResumeEntry resumeEntry=new ResumeEntry((BasicDBObject)dbObject);
            resumeEntryList.add(resumeEntry);
        }
        return resumeEntryList;
    }

    /**
     * 查询老师列表关键字
     * @param schoolId
     * @param keyword
     * @return
     */
    public List<ResumeEntry> getSubjectTeachersByKeyWord(ObjectId schoolId, String keyword) {
        BasicDBObject query=new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO);
        if (!keyword.equals("keyword")) {
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);

        List<ResumeEntry> resumeEntryList=new ArrayList<ResumeEntry>();
        for(DBObject dbObject:dbObjectList){
            ResumeEntry resumeEntry=new ResumeEntry((BasicDBObject)dbObject);
            resumeEntryList.add(resumeEntry);
        }
        return resumeEntryList;
    }

    /**
     * 查询老师Map
     * @param schoolId
     * @return
     */
    public Map<ObjectId, ResumeEntry> getResumeMapBySchoolId(ObjectId schoolId) {
        BasicDBObject query=new BasicDBObject("sid", schoolId).append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        Map<ObjectId, ResumeEntry> resumeEntryMap = new HashMap<ObjectId, ResumeEntry>();
        for(DBObject dbObject:dbObjectList){
            ResumeEntry resumeEntry=new ResumeEntry((BasicDBObject)dbObject);
            resumeEntryMap.put(resumeEntry.getUserId(),resumeEntry);
        }
        return resumeEntryMap;
    }

    /**
     * 删除老师学科
     * @param userId
     * @param subjectId
     */
    public void missSubjectIdByUserId(ObjectId userId, ObjectId subjectId) {
        DBObject query = new BasicDBObject("uid", userId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("suids", subjectId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_RESUME, query, updateValue);
    }
}
