package com.db.new33.afresh;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.afresh.PerformanceXYZLEntry;
import com.pojo.new33.afresh.Score;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;


/**
 * Created by fl on 2015/6/15.
 */


public class PerformanceXYZLDao extends BaseDao {

//==================================================================增==============================================================

    /**
     * 添加一条成绩记录
     *
     * @param performanceEntry
     * @return
     */
    public ObjectId save(PerformanceXYZLEntry performanceEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, performanceEntry.getBaseEntry());
        return performanceEntry.getID();
    }

    public void update(PerformanceXYZLEntry performanceEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, performanceEntry.getBaseEntry());
    }
    /**
     * 添加一批成绩记录
     *
     * @param pList
     * @return
     */
    public List<ObjectId> save(List<PerformanceXYZLEntry> pList) {
        List<DBObject> dbObjectList = new ArrayList<DBObject>();
        List<ObjectId> performanceList = new ArrayList<ObjectId>();
        for (PerformanceXYZLEntry p : pList) {
            dbObjectList.add(p.getBaseEntry());
        }
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, dbObjectList);
        for (PerformanceXYZLEntry p : pList) {
            performanceList.add(p.getId());
        }
        return performanceList;
    }

    //=====================================================删================================================================

    /**
     * 删除某次考试某个班级某个学科的记录
     *
     * @param performanceId
     * @param subjectId
     * @return
     */
    public boolean deleteSubject(ObjectId performanceId, ObjectId subjectId) {
        DBObject query = new BasicDBObject(Constant.ID, performanceId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("sList", new BasicDBObject("subId", subjectId)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
        return true;
    }

    /**
     * 删除
     *
     * @param pId
     */
    public void delete(ObjectId pId) {
        DBObject query = new BasicDBObject(Constant.ID, pId);
        MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_PERFORMANCE_XYZL).remove(query);
    }


    /**
     * 删除
     *
     * @param examId
     */
    public void deleteByExamId(ObjectId examId) {
        DBObject query = new BasicDBObject("exId", examId);
        MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_PERFORMANCE_XYZL).remove(query);
    }
    //=====================================================改================================================================

    /**
     * 打分,用于页面
     *
     * @param subjectId
     * @param score
     */
    public void updateScore(ObjectId performanceId, ObjectId subjectId, Double score, Integer absence, Integer exemption) {
        DBObject query = new BasicDBObject(Constant.ID, performanceId).append("sList.subId", subjectId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sList.$.subS", score).append("sList.$.abs", absence).append("sList.$.exemp", exemption));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }

    public void updateScore(ObjectId exId, ObjectId subjectId, Double score, ObjectId uid) {
        DBObject query = new BasicDBObject("exId", exId).append("sList.subId", subjectId).append("stuId", uid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sList.$.subS", score).append("st", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }
    public void updateScoreByExamId(ObjectId exId, ObjectId subjectId, Double score) {
        DBObject query = new BasicDBObject("exId", exId).append("sList.subId", subjectId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sList.$.subS", score).append("st", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }
    public void updateScore(ObjectId exId, ObjectId subjectId, Integer quekao, Integer miankao, ObjectId uid) {
        DBObject query = new BasicDBObject("exId", exId).append("sList.subId", subjectId).append("stuId", uid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sList.$.abs", quekao).append("sList.$.exemp", miankao));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }

    public void updateStuNo(ObjectId userid, ObjectId examid, String stuno) {
        DBObject query = new BasicDBObject("exId", examid).append("stuId", userid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("stuno", stuno));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }

    /**
     * 打分，用于excel导入
     *
     * @param subjectId
     * @param score
     */
    public void updateScore(ObjectId examId, ObjectId subjectId, Double score, Integer absence, Integer exemption, String stuName) {
        DBObject query = new BasicDBObject("stuNm", stuName).append("sList.subId", subjectId).append("exId", examId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sList.$.subS", score).append("sList.$.abs", absence).append("sList.$.exemp", exemption));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }

    public void updateScore(ObjectId examId, ObjectId subjectId, Double score, Integer absence, Integer exemption, String stuName, String stun) {

        BasicDBObject query = new BasicDBObject().append("sList.subId", subjectId).append("exId", examId);
        if (!stun.equals("")) {
            query.append("stuno", stun);
        } else {
            query.append("stuNm", stuName);
        }
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sList.$.subS", score).append("st", 1).append("sList.$.abs", absence).append("sList.$.exemp", exemption));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }

    /**
     * 更新满分及格分，用于老师编辑考试信息
     *
     * @param examId
     * @param subjectId
     * @param fullScore
     * @param failScore
     */
    public void updateFullFailScoreByExamId(ObjectId examId, ObjectId subjectId, Integer fullScore, Integer failScore) {
        DBObject query = new BasicDBObject("exId", examId).append("sList.subId", subjectId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sList.$.full", fullScore).append("sList.$.fail", failScore));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }

    /**
     * 整合考试，更新成绩
     *
     * @param examId
     * @param stuId
     * @param score
     */
    public void updateScore(ObjectId examId, ObjectId stuId, Double score) {
        DBObject query = new BasicDBObject("exId", examId).append("stuId", stuId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sList.0.subS", score));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }

    public void updateAreaRanking(ObjectId performanceId, int ranking) {
        DBObject query = new BasicDBObject(Constant.ID, performanceId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ar", ranking));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, updateValue);
    }
    //=====================================================查================================================================

    /**
     * 查询某次考试最高或者最低分
     *
     * @param exid
     * @param order
     * @return
     */
    public Double getMaxOrMinSco(ObjectId exid, String order) {
        BasicDBObject query = new BasicDBObject();
        if (exid != null) {
            query.put("exId", exid);
        }
        BasicDBObject or = new BasicDBObject("suc", order);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, or);
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
        if (null != list && !list.isEmpty()) {
            PerformanceXYZLEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
        }
        return performanceEntryList.get(0).getScoreSum();
    }

    /**
     * 根据成绩记录的id查询整条成绩记录
     *
     * @param id
     * @return
     */
    public PerformanceXYZLEntry getPerformanceById(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
        if (null != dbo) {
            return new PerformanceXYZLEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public PerformanceXYZLEntry getPerformanceById(ObjectId userId,ObjectId examId) {
        DBObject query = new BasicDBObject().append("exId", examId).append("stuId", userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
        if (null != dbo) {
            return new PerformanceXYZLEntry((BasicDBObject) dbo);
        }
        return null;
    }
    /**
     * 查找考试成绩,用于新表结构
     *
     * @param examId
     * @param classId
     * @return
     */

    public List<PerformanceXYZLEntry> getPerformanceEntryList(ObjectId examId, ObjectId classId, ObjectId stuId, Collection<ObjectId> examList, Collection<ObjectId> classIds, Collection<ObjectId> studentIds, DBObject fields) {
        BasicDBObject query = new BasicDBObject();
        if (examId != null) {
            query.put("exId", examId);
        }
        if (classId != null) {
            query.put("cId", classId);
        }
        if (stuId != null) {
            query.put("stuId", stuId);
        }
        if (examList != null) {
            query.put("exId", new BasicDBObject(Constant.MONGO_IN, examList));
        }
        if (classIds != null) {
            query.put("cId", new BasicDBObject(Constant.MONGO_IN, classIds));
        }
        if (null != studentIds && studentIds.size() > 0) {
            query.put("stuId", new BasicDBObject(Constant.MONGO_IN, studentIds));
        }
//        query.append("st", 1);
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();

        {
            List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, fields);
            if (null != list && !list.isEmpty()) {
                PerformanceXYZLEntry p;
                for (DBObject dbo : list) {
                    p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                    performanceEntryList.add(p);
                }

            }
        }
        return performanceEntryList;
    }

    public List<PerformanceXYZLEntry> getPerformanceEntryList(ObjectId examId,DBObject fields) {
        BasicDBObject query = new BasicDBObject();
        if (examId != null) {
            query.put("exId", examId);
        }
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();

        {
            List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, fields);
            if (null != list && !list.isEmpty()) {
                PerformanceXYZLEntry p;
                for (DBObject dbo : list) {
                    p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                    performanceEntryList.add(p);
                }

            }
        }
        return performanceEntryList;
    }
    public List<PerformanceXYZLEntry> getPerformanceEntryLists(ObjectId examId, ObjectId classId, ObjectId stuId, Collection<ObjectId> examList, Collection<ObjectId> classIds, Collection<ObjectId> studentIds, DBObject fields) {
        BasicDBObject query = new BasicDBObject();
        if (examId != null) {
            query.put("exId", examId);
        }
        if (classId != null) {
            query.put("cId", classId);
        }
        if (stuId != null) {
            query.put("stuId", stuId);
        }
        if (examList != null) {
            query.put("exId", new BasicDBObject(Constant.MONGO_IN, examList));
        }
        if (classIds != null) {
            query.put("cId", new BasicDBObject(Constant.MONGO_IN, classIds));
        }
        if (null != studentIds && studentIds.size() > 0) {
            query.put("stuId", new BasicDBObject(Constant.MONGO_IN, studentIds));
        }
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();

        {
            List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, fields);
            if (null != list && !list.isEmpty()) {
                PerformanceXYZLEntry p;
                for (DBObject dbo : list) {
                    p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                    performanceEntryList.add(p);
                }

            }
        }
        return performanceEntryList;
    }

    public Map<ObjectId, PerformanceXYZLEntry> getPerformanceEntryListz(ObjectId examId, ObjectId classId, ObjectId stuId, Collection<ObjectId> examList, Collection<ObjectId> classIds, Collection<ObjectId> studentIds, DBObject fields) {
        BasicDBObject query = new BasicDBObject();
        Map<ObjectId, PerformanceXYZLEntry> map = new HashMap<ObjectId, PerformanceXYZLEntry>();
        if (examId != null) {
            query.put("exId", examId);
        }
        if (classId != null) {
            query.put("cId", classId);
        }
        if (stuId != null) {
            query.put("stuId", stuId);
        }
        if (examList != null) {
            query.put("exId", new BasicDBObject(Constant.MONGO_IN, examList));
        }
        if (classIds != null) {
            query.put("cId", new BasicDBObject(Constant.MONGO_IN, classIds));
        }
        if (null != studentIds && studentIds.size() > 0) {
            query.put("stuId", new BasicDBObject(Constant.MONGO_IN, studentIds));
        }
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();

        {
            List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, fields);
            if (null != list && !list.isEmpty()) {
                PerformanceXYZLEntry p;
                for (DBObject dbo : list) {
                    p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                    map.put(p.getStudentId(), p);
                }
                return map;
            }
        }
        return null;
    }

    /**
     * 查找考试成绩，用于旧表结构
     *
     * @param
     * @return
     */
    public List<PerformanceXYZLEntry> getPerformanceEntryList(List<ObjectId> performanceList, ObjectId classId, ObjectId stuId) {
        BasicDBObject query = new BasicDBObject();
        if (performanceList != null) {
            query.put(Constant.ID, new BasicDBObject(Constant.MONGO_IN, performanceList));
        }
        if (classId != null) {
            query.put("cId", classId);
        }
        if (stuId != null) {
            query.put("stuId", stuId);
        }
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
        if (query != null) {
            List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
            if (null != list && !list.isEmpty()) {

                PerformanceXYZLEntry p;
                for (DBObject dbo : list) {
                    p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                    performanceEntryList.add(p);
                }

            }
        }
        return performanceEntryList;
    }

    public Map<ObjectId, PerformanceXYZLEntry> getPerformanceEntryListz(List<ObjectId> performanceList, ObjectId classId, ObjectId stuId) {
        BasicDBObject query = new BasicDBObject();
        if (performanceList != null) {
            query.put(Constant.ID, new BasicDBObject(Constant.MONGO_IN, performanceList));
        }
        if (classId != null) {
            query.put("cId", classId);
        }
        if (stuId != null) {
            query.put("stuId", stuId);
        }
        Map<ObjectId, PerformanceXYZLEntry> performanceEntryList = new HashMap<ObjectId, PerformanceXYZLEntry>();
        if (query != null) {
            List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
            if (null != list && !list.isEmpty()) {

                PerformanceXYZLEntry p;
                for (DBObject dbo : list) {
                    p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                    performanceEntryList.put(p.getID(), p);
                }

            }
        }
        return performanceEntryList;
    }

    /**
     * 查询某科目满分
     *
     * @param performanceId
     * @param subjectId
     * @return
     */
    public Integer getFullScore(ObjectId performanceId, ObjectId subjectId) {
        DBObject query = new BasicDBObject(Constant.ID, performanceId);
        BasicDBObject dbo = (BasicDBObject) findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
        if (null != dbo) {
            PerformanceXYZLEntry performanceEntry = new PerformanceXYZLEntry(dbo);
            for (Score score : performanceEntry.getScoreList()) {
                if (score.getSubjectId().equals(subjectId)) {
                    return score.getFullScore();
                }
            }
        }
        return null;
    }

    /**
     * 查询某科目及格分
     *
     * @param performanceId
     * @param subjectId
     * @return
     */
    public Integer getFailScore(ObjectId performanceId, ObjectId subjectId) {
        DBObject query = new BasicDBObject(Constant.ID, performanceId);
        BasicDBObject dbo = (BasicDBObject) findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
        if (null != dbo) {
            PerformanceXYZLEntry performanceEntry = new PerformanceXYZLEntry(dbo);
            for (Score score : performanceEntry.getScoreList()) {
                if (score.getSubjectId().equals(subjectId)) {
                    return score.getFailScore();
                }
            }
        }
        return null;
    }

    /**
     * 查找考试超过某个分数的学生数
     *
     * @param examId
     * @param subjectId
     * @param score
     * @return
     */
    public int findPassCount(ObjectId examId, ObjectId subjectId, int score) {
        DBObject query = new BasicDBObject("exId", examId)
                .append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH,
                        new BasicDBObject("subId", subjectId).append("subS",
                                new BasicDBObject(Constant.MONGO_GTE, score))));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query);
    }

    /**
     * 查找考试超过某个分数的学生数
     *
     * @param examList
     * @param subjectId
     * @param score
     * @return
     */
    public int findPassCount(List<ObjectId> examList, ObjectId subjectId, int score) {
        DBObject query = new BasicDBObject("exId", new BasicDBObject(Constant.MONGO_IN, examList))
                .append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH,
                        new BasicDBObject("subId", subjectId).append("subS",
                                new BasicDBObject(Constant.MONGO_GTE, score))));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query);
    }

    /**
     * 成绩列表
     *
     * @param examId
     * @return
     */
    public List<PerformanceXYZLEntry> findPerformanceList(ObjectId examId) {
        DBObject query = new BasicDBObject("exId", examId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
            PerformanceXYZLEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return null;
    }

    public Integer findPerformanceListByCount(ObjectId examId) {
        DBObject query = new BasicDBObject("exId", examId).append("sList.abs", 1);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query);
    }

    public List<PerformanceXYZLEntry> findPerformanceList(ObjectId examId, ObjectId cid) {
        DBObject query = new BasicDBObject("exId", examId).append("cId", cid);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
        if (null != list && !list.isEmpty()) {
            PerformanceXYZLEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return performanceEntryList;
    }

    public List<PerformanceXYZLEntry> findPerformanceList(ObjectId examId, List<ObjectId> studeIds) {
        DBObject query = new BasicDBObject("exId", examId).append("stuId", new BasicDBObject(Constant.MONGO_IN,studeIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
        if (null != list && !list.isEmpty()) {
            PerformanceXYZLEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return performanceEntryList;
    }

    /***
     * @Description 查找成绩
     * @Param examId 考试id
     * @Param studeIds 考生id List
     * @Param subjectId  科目id
     * @Param noScore 无成绩标识
     * @return java.util.List<com.pojo.xyzl.PerformanceXYZLEntry>
     * @Author houdongdong
     * @Date  2019/2/12 9:47
     **/
    public List<PerformanceXYZLEntry> findPerformanceList(ObjectId examId, List<ObjectId> studeIds ,ObjectId subjectId  ,boolean noScore ) {
        BasicDBObject query = new BasicDBObject("exId", examId).append("stuId", new BasicDBObject(Constant.MONGO_IN,studeIds));
        if (subjectId !=null ){
            query.append("sList.subId",  subjectId);
        }
        if (noScore){
            query.append("sList.subS",  null);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
        if (null != list && !list.isEmpty()) {
            PerformanceXYZLEntry performanceXYZLEntry;
            for (DBObject dbo : list) {
                performanceXYZLEntry = new PerformanceXYZLEntry((BasicDBObject) dbo);
                if (subjectId !=null && noScore){
                    //在这里处理 科目和无成绩同时选中的情况
                    List<Score> scoreList = performanceXYZLEntry.getScoreList();
                    Iterator<Score> iterator = scoreList.iterator();
                    while (iterator.hasNext()){
                        Score score = iterator.next();
                        if (subjectId.equals(score.getSubjectId())  && score.getSubjectScore()==0.0){
                            performanceEntryList.add(performanceXYZLEntry);
                        }
                    }
                }else{
                    performanceEntryList.add(performanceXYZLEntry);
                }
            }
            return performanceEntryList;
        }


        return performanceEntryList;
    }

    /**
     * 区域联考学生成绩排名表
     *
     * @param areaId
     * @param skip
     * @param pageSize
     * @return
     */
    public List<PerformanceXYZLEntry> findPerformanceListByAreaId(ObjectId areaId, int skip, int pageSize) {
        DBObject matchDBO = new BasicDBObject("aid", areaId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, matchDBO, Constant.FIELDS, new BasicDBObject("ar", Constant.ASC), skip, pageSize);
        if (null != list && !list.isEmpty()) {
            List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
            PerformanceXYZLEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return null;
    }

    public List<PerformanceXYZLEntry> findPerformanceListByAreaId(ObjectId areaId, List<ObjectId> examIds, DBObject fields) {
        BasicDBObject query = new BasicDBObject("aid", areaId);
        if (examIds != null) {
            query.append("exId", new BasicDBObject(Constant.MONGO_IN, examIds));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, fields);
        if (null != list && !list.isEmpty()) {
            List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
            PerformanceXYZLEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return null;
    }

    public int findPerformanceCountByAreaId(ObjectId areaExamId) {
        DBObject query = new BasicDBObject("aid", areaExamId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query);
    }
    
    /**
     * 查询和某学生相关的考试成绩
     * @param studentId
     * @param fields
     * @return
     */
    public List<PerformanceXYZLEntry> getPerformanceByStuId(ObjectId studentId,DBObject fields){
    	 BasicDBObject query = new BasicDBObject("stuId", studentId);
    	 List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, fields);
         if (null != list && !list.isEmpty()) {
             List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
             PerformanceXYZLEntry p;
             for (DBObject dbo : list) {
                 p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                 performanceEntryList.add(p);
             }
             return performanceEntryList;
         }
         return null;
    }

    public List<PerformanceXYZLEntry> getListByAbs(ObjectId subjectId,List<ObjectId> examIds,DBObject fields){
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
        BasicDBObject query = new BasicDBObject("sList.subId", subjectId);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("sList.abs", 1));
        values.add(new BasicDBObject("sList.exemp", 1));
        query.put(Constant.MONGO_OR, values);
        query.append("exId", new BasicDBObject(Constant.MONGO_IN,examIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, fields);
        if (null != list && !list.isEmpty()) {
            PerformanceXYZLEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return performanceEntryList;
    }

    /***
     * @Description 查询考试的某些学科的成绩
     * @Param [examIds, subjectIdList, fields]
     * @return java.util.List<com.pojo.xyzl.PerformanceXYZLEntry>
     * @Author houdongdong
     * @Date  2019/1/31 14:02
     **/
    public List<PerformanceXYZLEntry> getPerformanceXYZLEntryListByAbs(ObjectId examIds,List<ObjectId> subjectIdList,DBObject fields){
        List<PerformanceXYZLEntry> performanceEntryList = new ArrayList<PerformanceXYZLEntry>();
        BasicDBObject query = new BasicDBObject().append("sList.subId",  new BasicDBObject(Constant.MONGO_IN,subjectIdList));
        BasicDBList values = new BasicDBList();
//        values.add(new BasicDBObject("sList.abs", 1));
//        values.add(new BasicDBObject("sList.exemp", 1));
//        query.put(Constant.MONGO_OR, values);
        query.append("exId", examIds);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, fields);
        if (null != list && !list.isEmpty()) {
            PerformanceXYZLEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return performanceEntryList;
    }

    public List<PerformanceXYZLEntry> getPerformanceEntryList(List<ObjectId> examIds) {
        BasicDBObject query = new BasicDBObject();
        query.put("exId", new BasicDBObject(Constant.MONGO_IN, examIds));
        List<PerformanceXYZLEntry> reList = new ArrayList<PerformanceXYZLEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_XYZL, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            PerformanceXYZLEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceXYZLEntry((BasicDBObject) dbo);
                reList.add(p);
            }

        }
        return reList;
    }
    /**
     * @Description 获取学生考试成绩map
     * 格式：{
     *      studentId :{
     *          examId: {
     *              subject1Id:  scoreValue1
     *              subject2Id:  scoreValue2
     *              subject3Id:  scoreValue3
     *          }
     *      }
     * }
     * @Param
     * @return
     * @Author houdongdong
     * @Date  2018/12/17 10:23
     **/
    public static Map<String ,Map<String ,Map<String ,Double >>> getStudentEaxmSubjectScoreMap ( List<PerformanceXYZLEntry> performanceXYZLEntryList){
        Map<String ,Map<String ,Map<String ,Double >>> studentEaxmScoreMap = new HashMap<String, Map<String, Map<String ,Double >>>();
            for ( PerformanceXYZLEntry performanceXYZLEntry : performanceXYZLEntryList) {
                String studentId = performanceXYZLEntry.getStudentId().toString();
                Map<String, Map<String ,Double > > examPerformanceXYZLEntryMap = studentEaxmScoreMap.get(studentId);
                if (examPerformanceXYZLEntryMap !=null){
                    examPerformanceXYZLEntryMap.put(performanceXYZLEntry.getExamId().toString(),getSubjectScoreMap(performanceXYZLEntry));
                }else{
                    examPerformanceXYZLEntryMap = new HashMap<String, Map<String ,Double > >();
                    examPerformanceXYZLEntryMap.put(performanceXYZLEntry.getExamId().toString(),getSubjectScoreMap(performanceXYZLEntry));
                    studentEaxmScoreMap.put(studentId,examPerformanceXYZLEntryMap);
                }
            }

        return studentEaxmScoreMap;
    }

    /**
     * @Description performanceXYZLEntry转为学科：成绩 map   格式：{学科id为key, 成绩为value}
     * @Param
     * @return
     * @Author houdongdong
     * @Date  2018/12/17 13:52
     **/
    public static Map<String ,Double > getSubjectScoreMap(PerformanceXYZLEntry performanceXYZLEntry){
        Map<String ,Double > result = new HashMap<String, Double>();
        List<Score> scoreList = performanceXYZLEntry.getScoreList();
        for (Score score  :scoreList ) {
            result.put(score.getSubjectId().toString() ,score.getSubjectScore());
        }
        return result;
    }
}
