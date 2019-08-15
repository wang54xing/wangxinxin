package com.pojo.new33.afresh;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**成绩信息，依附于PerformanceEntry
 * Created by fl on 2015/6/12.
 *
 * {
 *       subjectId:考试科目
 *       subjectName:科目名称
 *       subjectScore:考试成绩
 *       fullScore：满分
 *       failScore：及格分
 *       absence:缺考
 *       exemption:免考
 *   }
 */
public class Score extends BaseDBObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4765889781170832217L;

	/**
     * 构造函数
     * @param baseEntry
     */
    public Score(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public Score(ObjectId subjectId, String subjectName, Double subjectScore, Integer absence, Integer exemption, Integer fullScore, Integer failScore) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("subId", subjectId)
                .append("subNm", subjectName)
                .append("subS", subjectScore)
                .append("full", fullScore)
                .append("fail", failScore)
                .append("abs", absence)
                .append("exemp", exemption);
        setBaseEntry(dbo);
    }

    //getters and setters
    public ObjectId getSubjectId() {
        return  getSimpleObjecIDValue("subId");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subId", subjectId);
    }

    public String getSubjectName() {return getSimpleStringValue("subNm");}

    public void setSubjectName(String subjectName) { setSimpleValue("subNm", subjectName);}

    public Double getObjSubjectScore() {
        try {
            return getSimpleDoubleValue("subS");
        } catch (NullPointerException e) {
            return  null;
        }

    }

    public double getSubjectScore() {
        try {
            return getSimpleDoubleValue("subS");
        } catch (NullPointerException e) {
            return 0.0;
        }

    }
    public Double getSubjectScoreIncludeNull() {
        try {
            return getSimpleDoubleValue("subS");
        } catch (NullPointerException e) {
            return null;
        }

    }
    public void setSubjectScore(Double subjectScore) {
        setSimpleValue("subS", subjectScore);
    }

    public Integer getAbsence() {
        return  getSimpleIntegerValue("abs");
    }

    public void setAbsence(Integer absence) {
        setSimpleValue("abs", absence);
    }

    public Integer getExemption() {
        return getSimpleIntegerValue("exemp");
    }

    public void setExemption(Integer exemption) {
        setSimpleValue("exemp", exemption);
    }

    public Integer getFullScore() {
        double full = getSimpleDoubleValue("full");
        return (int)full;
    }

    public void setFullScore(Integer fullScore) {
        setSimpleValue("full", fullScore);
    }

    public Integer getFailScore() {
        double fail = getSimpleDoubleValue("fail");
        return (int)fail;
    }

    public void setFailScore(Integer failScore) {
        setSimpleValue("fail", failScore);
    }

}
