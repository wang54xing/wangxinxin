package com.db.new33;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.ExamXYZLEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by fl on 2015/6/12.
 */

public class ExamXYZLDao extends BaseDao {

    //=====================================================增================================================================

    /**
     * 新建考试
     *
     * @param e
     * @return
     */
    public ObjectId add(ExamXYZLEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, e.getBaseEntry());
        return e.getID();
    }

    public List<ObjectId> save(List<ExamXYZLEntry> pList) {
        List<DBObject> dbObjectList = new ArrayList<DBObject>();
        List<ObjectId> performanceList = new ArrayList<ObjectId>();
        for (ExamXYZLEntry p : pList) {
            dbObjectList.add(p.getBaseEntry());
        }
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, dbObjectList);
        for (ExamXYZLEntry p : pList) {
            performanceList.add(p.getID());
        }
        return performanceList;
    }
    //=====================================================删================================================================

    /**
     * 删除考试, 逻辑删除
     *
     * @param examId
     */
    public void delete(ObjectId examId) {
        DBObject query = new BasicDBObject(Constant.ID, examId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    public ExamXYZLEntry getExamResultEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS);
        if (null != dbo) {
            return new ExamXYZLEntry((BasicDBObject) dbo);
        }
        return null;
    }
    public List<ExamXYZLEntry> getExamResultEntryByGrade(ObjectId gradeId,ObjectId sid) {
        BasicDBObject query = new BasicDBObject().append("sId", sid).append("gId",gradeId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS, new BasicDBObject("_id", -1));
        if (null != list && !list.isEmpty()) {
            List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }
    public List<ExamXYZLEntry> getExamResultEntryByGradeTime(ObjectId gradeId,ObjectId sid,long st,long ed) {
        BasicDBObject query = new BasicDBObject().append("sId", sid).append("gId",gradeId);
        query.append("ldate",new BasicDBObject(Constant.MONGO_GTE,st))
                .append("ldate",new BasicDBObject(Constant.MONGO_LTE,ed));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS, new BasicDBObject("_id", -1));
        if (null != list && !list.isEmpty()) {
            List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }
    public void updateExamName(ObjectId examId,String name,String time) {
        DBObject query = new BasicDBObject(Constant.ID, examId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("name", name).append("date",time).append("ldate", DateTimeUtils.getStrToLongTime(time, "yyyy-MM-dd")));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, updateValue);

    }
    public List<ExamXYZLEntry> getExamResultEntryByName(String name, ObjectId sid, int page, int pageSize) {
        BasicDBObject query = new BasicDBObject().append("sId", sid);
        if (!name.equals("*")) {
            query.append("name", MongoUtils.buildRegex(name));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS, new BasicDBObject("_id", -1));
        if (null != list && !list.isEmpty()) {
            List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }
    public List<ExamXYZLEntry> getExamResultEntryByName(String name, ObjectId sid, int page, int pageSize,ObjectId gid) {
        BasicDBObject query = new BasicDBObject().append("sId", sid);
        if (!name.equals("*")) {
            query.append("name", MongoUtils.buildRegex(name));
        }
        query.append("gId", gid);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS, new BasicDBObject("_id", -1));
        if (null != list && !list.isEmpty()) {
            List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }
    public Integer getExamResultEntryByName(String name, ObjectId sid, String term) {
        BasicDBObject query = new BasicDBObject().append("sId", sid).append("schY", term);
        if (!name.equals("*")) {
            query.append("name", MongoUtils.buildRegex(name));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query);
    }

    /**
     * 得到exercise关联的examResultEntry
     *
     * @param eid
     * @return
     */
    public ExamXYZLEntry getExamResultEntryByEid(ObjectId eid) {
        DBObject query = new BasicDBObject("eId", eid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS);
        if (null != dbo) {
            return new ExamXYZLEntry((BasicDBObject) dbo);
        }
        return null;
    }


    public ExamXYZLEntry findExamListL(ObjectId gradeId, List<String> schoolYear, List<String> type, Long time) {
        BasicDBObject query = new BasicDBObject();
        if (schoolYear.size() > 0) {
            query.append("schY", new BasicDBObject(Constant.MONGO_IN, schoolYear));
        }
        if (type.size() > 0) {
            query.append("type", new BasicDBObject(Constant.MONGO_IN, type));
        }
        query.append("gId", gradeId);
        query.append("ed", new BasicDBObject(Constant.MONGO_GTE, time));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries.get(0);
        }
        return null;
    }

    /**
     * 查询考试列表--成绩分析
     *
     * @param name       试卷名
     * @param gradeId    年级id
     * @param schoolYear 学期
     * @param type       考试类型
     * @param page
     * @return
     */
    public List<ExamXYZLEntry> findExamList(String name, ObjectId gradeId, List<String> schoolYear, List<String> type, Integer page, List<ObjectId> cList) {
        BasicDBObject query = new BasicDBObject("df", new BasicDBObject(Constant.MONGO_NE, 1));
        if (!name.equals("*")) {
            query.append("name", MongoUtils.buildRegex(name));
        }
        if (gradeId != null) {
            query.append("gId", gradeId);
        }
        if (schoolYear.size() > 0) {
            query.append("schY", new BasicDBObject(Constant.MONGO_IN, schoolYear));
        }
        if (type.size() > 0) {
            query.append("type", new BasicDBObject(Constant.MONGO_IN, type));
        }
        if (cList.size() > 0) {
            query.append("cList", new BasicDBObject(Constant.MONGO_IN, cList));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * 7, 7);
        if (null != list && !list.isEmpty()) {
            List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }

    public List<ObjectId> findExamList(List<ObjectId> cList) {
        BasicDBObject query = new BasicDBObject("df", new BasicDBObject(Constant.MONGO_NE, 1));
        if (cList.size() > 0) {
            query.append("cList", new BasicDBObject(Constant.MONGO_IN, cList));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (null != list && !list.isEmpty()) {
            List<ObjectId> examResultEntries = new ArrayList<ObjectId>();
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e.getID());
            }
            return examResultEntries;
        }
        return null;
    }

    public int findExamListCount(String name, ObjectId gradeId, List<String> schoolYear, List<String> type, List<ObjectId> cList) {
        BasicDBObject query = new BasicDBObject("df", new BasicDBObject(Constant.MONGO_NE, 1));
        if (!name.equals("*")) {
            query.append("name", MongoUtils.buildRegex(name));
        }
        if (gradeId != null) {
            query.append("gId", gradeId);
        }
        if (schoolYear.size() > 0) {
            query.append("schY", new BasicDBObject(Constant.MONGO_IN, schoolYear));
        }
        if (type.size() > 0) {
            query.append("type", new BasicDBObject(Constant.MONGO_IN, type));
        }
        if (cList.size() > 0) {
            query.append("cList", new BasicDBObject(Constant.MONGO_IN, cList));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query);
    }


    /**
     * 查询考试列表
     *
     * @param classId
     * @param subjectId
     * @param schoolYear
     * @param isGrade
     * @return
     */
    public List<ExamXYZLEntry> getExamList(ObjectId gradeId, ObjectId classId, ObjectId subjectId, String schoolYear, Integer isGrade) {
        DBObject query = new BasicDBObject("df", new BasicDBObject(Constant.MONGO_NE, 1));
        if (gradeId != null) {
            query.put("gId", gradeId);
        }
        if (classId != null) {
            query.put("cList", classId);
        }
        if (subjectId != null) {
            query.put("sList", subjectId);
        }
        if (schoolYear != null) {
            query.put("schY", schoolYear);
        }
        if (isGrade != null) {
            query.put("isGra", isGrade);
        }
        List<DBObject> list = new ArrayList<DBObject>();
        if (query != null) {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS);
        }
        if (null != list && !list.isEmpty()) {
            List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }

    /**
     * 根据id查询
     *
     * @param ids
     * @return
     */
    public List<ExamXYZLEntry> getExamList(Collection<ObjectId> ids, DBObject fields) {
        List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, fields, new BasicDBObject("date", -1));
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
        }
        return examResultEntries;
    }

    /**
     * 根据ids查询考试名称
     *
     * @param ids
     * @return
     */
    public Map<String, String> getExamName(Collection<ObjectId> ids) {
        Map<String, String> result = new HashMap<String, String>();
        List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, new BasicDBObject("name", 1).append("_id", 1), new BasicDBObject("date", -1));
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                result.put(e.getID().toString(), e.getName());
            }
        }
        return result;
    }

    public Map<String, ExamXYZLEntry> getExamMap(Collection<ObjectId> ids){
    	Map<String, ExamXYZLEntry> result = new HashMap<String, ExamXYZLEntry>();
    	 DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
         List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS, new BasicDBObject("date", -1));
         if (null != list && !list.isEmpty()) {
             for (DBObject dbo : list) {
                 ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                 result.put(e.getID().toString(), e);
             }
         }
         return result;
    }
    public List<ExamXYZLEntry> getExamByGradeId(List<ObjectId> gradeIds){
        BasicDBObject query = new BasicDBObject("gId", new BasicDBObject(Constant.MONGO_IN, gradeIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }

    //更新考试的学科信息 oz
    public void updateExamSubs(ObjectId examId,List<ObjectId> subList) {
        DBObject query = new BasicDBObject(Constant.ID, examId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("subList", subList));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, updateValue);

    }

    /***
     * @Description 获取考试列表
     * @Param schoolId
     * @Param  type
     * @return
     * @Author houdongdong
     * @Date  2019/1/29 16:03
     **/
    public List<ExamXYZLEntry> getExamXYZLEntryList(ObjectId schoolId , Integer type ,ObjectId gId){
        BasicDBObject query = new BasicDBObject("sId",schoolId);
        if (type != null){
            if (type == Constant.ZERO){
                //原始考试成绩,为兼容原有数据(原始数据没有type标识)
//                query.append("type",type);
                BasicDBObject typeExists = new BasicDBObject("type",type);
                BasicDBObject typeNoExists = new BasicDBObject("type",new BasicDBObject("$exists",false));
                BasicDBList dblist =new BasicDBList();
                dblist.add(typeExists);
                dblist.add(typeNoExists);
                query.append(Constant.MONGO_OR,dblist);
            }else{
                query.append("type",type);
            }
        }
        if (gId != null){
            query.append("gId",gId);
        }
        BasicDBObject sort = new BasicDBObject("date" , Constant.NEGATIVE_ONE);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS,sort);
        if (null != list && !list.isEmpty()) {
            List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
            for (DBObject dbo : list) {
                ExamXYZLEntry e = new ExamXYZLEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return Collections.emptyList();
    }
    
    
//    /**
//     * @Description 根据学年（termId） 查询考试
//     * @Param [gradeIds]
//     * @return java.util.List<com.pojo.xyzl.ExamXYZLEntry>
//     * @Author houdongdong
//     * @Date  2018/12/7 13:40
//     **/
//    public List<ExamXYZLEntry> getSchoolYearExamsByTerm(List<ObjectId> termIds){
//        BasicDBObject query = new BasicDBObject("yearId", new BasicDBObject(Constant.MONGO_IN, termIds));
//        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_XYZL, query, Constant.FIELDS);
//        if (null != list && !list.isEmpty()) {
//            List<ExamXYZLEntry> examResultEntries = new ArrayList<ExamXYZLEntry>();
//            for (DBObject dbo : list) {
//                examResultEntries.add(new ExamXYZLEntry((BasicDBObject) dbo));
//            }
//            return examResultEntries;
//        }
//        return Collections.EMPTY_LIST;
//    }
}
