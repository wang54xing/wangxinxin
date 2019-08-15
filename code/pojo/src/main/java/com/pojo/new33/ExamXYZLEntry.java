package com.pojo.new33;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 考试信息表
 * Created by fl on 2015/6/12.
 * collectionName:examresult
 * {
 * id：考试编号
 * name:考试名称
 * sId:考试的学校
 * gId：考试年级
 * date：考试日期
 * schY:学年
 * subList:考试学科
 * yearId:
 * term:
 * type: （0：原始考试，1：组合考试）
 * }
 */
public class ExamXYZLEntry extends BaseDBObject {

    public final static  int TYPE_NOMRAL = 0;
    public final static  int TYPE_GROUP = 1;


    /**
     *
     */
    private static final long serialVersionUID = -9108279201992379305L;

    /**
     * 构造函数
     *
     * @param baseEntry
     */
    public ExamXYZLEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ExamXYZLEntry() {
        super();
    }

    /**
     * 构造函数
     *
     * @param name
     * @param schoolId
     * @param gradeId
     * @param date
     * @param schoolYear
     */
    public ExamXYZLEntry(String name, ObjectId schoolId, ObjectId gradeId, String date, String schoolYear, Long ldate, List<ObjectId> subList, ObjectId yearId, Integer term) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("name", name)
                .append("gId", gradeId)
                .append("sId", schoolId)
                .append("date", date)
                .append("ldate", ldate)
                .append("subList", MongoUtils.convert(subList))
                .append("yearId",yearId)
                .append("term",term)
                .append("schY", schoolYear);
        setBaseEntry(dbo);
    }

    public ExamXYZLEntry(String name, ObjectId schoolId, ObjectId gradeId, String date, String schoolYear, Long ldate, List<ObjectId> subList, ObjectId yearId, Integer term, Integer type) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("name", name)
                .append("gId", gradeId)
                .append("sId", schoolId)
                .append("date", date)
                .append("ldate", ldate)
                .append("subList", MongoUtils.convert(subList))
                .append("yearId",yearId)
                .append("term",term)
                .append("schY", schoolYear)
                .append("type", type);
        setBaseEntry(dbo);
    }

    public List<ObjectId> getSubjectList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("subList");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setSubjectList(List<ObjectId> subjectList) {
        setSimpleValue("subList", MongoUtils.convert(subjectList));
    }
    public String getName() {
        return getSimpleStringValue("name");
    }

    public void setName(String type) {
        setSimpleValue("name", type);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sId");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sId", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gId");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gId", gradeId);
    }


    public ObjectId getYearId() {
        return getSimpleObjecIDValue("yearId");
    }

    public void setYearIdYearId(ObjectId yearId) {
        setSimpleValue("yearId", yearId);
    }


    public Integer getTerm() {
        return getSimpleIntegerValue("term");
    }

    public void setTerm(Integer term) {
        setSimpleValue("term", term);
    }

    public String getDate() {
        return getSimpleStringValue("date");
    }

    public void setDate(String date) {
        setSimpleValue("date", date);
    }

    public String getSchoolYear() {
        return getSimpleStringValue("schY");
    }

    public void setSchoolYear(String schoolYear) {
        setSimpleValue("schY", schoolYear);
    }

    public Long getLDate() {
        return getSimpleLongValue("ldate");
    }
    public void setLDate(Long lDate) {
        setSimpleValue("ldate", lDate);
    }

    public Integer getType() {
        try {
            return getSimpleIntegerValue("type");
        }catch (Exception e){
            return null;
        }
    }
    public void setType(Integer type) {
        setSimpleValue("type", type);
    }
}
