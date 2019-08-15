package com.pojo.new33.afresh;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 考试成绩表
 * Created by fl on 2015/6/12.
 * <p/>
 * {
 * id:成绩表ID
 * exId:考试id
 * stuId:学生的id
 * stuNm:学生的姓名
 * stuno:学号
 * cId:班级的Id
 * scoList：学生的成绩
 * [
 * {
 * subId:考试科目
 * subScore:考试成绩
 * }
 * ]
 * suc:总成绩
 * }
 */
public class PerformanceXYZLEntry extends BaseDBObject {

    /**
     *
     */
    private static final long serialVersionUID = -2689034026450549544L;

    /**
     * 构造函数
     *
     * @param baseEntry
     */
    public PerformanceXYZLEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public PerformanceXYZLEntry() {
        super();
    }
    /**
     * 构造函数
     *
     * @param studentId
     * @param studentName
     * @param classId
     * @param scoreList
     */
    public PerformanceXYZLEntry(ObjectId examId, ObjectId studentId, String studentName, String stuNo, ObjectId classId, List<Score> scoreList, Double suc) {
        super();
        Integer message = 0;
        if (suc == null) {
            message = 1;
        }
        BasicDBObject dbo = new BasicDBObject()
                .append("exId", examId)
                .append("stuId", studentId)
                .append("stuNm", studentName)
                .append("cId", classId)
                .append("stuno", stuNo)
                .append("st", 0)
                .append("suc", suc)
                .append("sucMessage", message)
                .append("sList", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoreList)));
        setBaseEntry(dbo);
    }

    //getters and setters
    public ObjectId getId() {
        return getID();
    }

    public ObjectId getExamId() {
        return getSimpleObjecIDValue("exId");
    }

    public void setExamId(ObjectId studentId) {
        setSimpleValue("exId", studentId);
    }

    public ObjectId getStudentId() {
        return getSimpleObjecIDValue("stuId");
    }

    public void setStudentId(ObjectId studentId) {
        setSimpleValue("stuId", studentId);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("cId");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("cId", classId);
    }

    public String getStudentName() {
        return getSimpleStringValue("stuNm");
    }

    public void setStudentName(String studentName) {
        setSimpleValue("stuNm", studentName);
    }


    public String getStudentNo() {
        return getSimpleStringValueDef("stuno", "");
    }

    public void setStudentNo(String stuno) {
        setSimpleValue("stuno", stuno);
    }

    public double getScoreSum() {
        return getSimpleDoubleValue("suc");
    }

    public void setScoreSum(double scoreSum) {
        setSimpleValue("suc", scoreSum);
    }


    public Integer getScoreMessage() {
        return getSimpleIntegerValue("sucMessage");
    }

    public void setScoreMessage(Integer scoreMessage) {
        setSimpleValue("sucMessage", scoreMessage);
    }
    public Integer getST() {
        return getSimpleIntegerValue("st");
    }

    public void setST(Integer scoreMessage) {
        setSimpleValue("st", scoreMessage);
    }
    public List<Score> getScoreList() {
        List<Score> scoreList = new ArrayList<Score>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("sList");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                scoreList.add(new Score((BasicDBObject) o));
            }
        }
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(scoreList);
        setSimpleValue("sList", MongoUtils.convert(list));
    }

}
