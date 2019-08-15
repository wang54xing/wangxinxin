package com.pojo.autoPK;



import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
*自动排课-排课规则-教师设置
*
*	entry								type								annotation
*	
*	schoolId							ObjectId							学校id
*	gradeId								ObjectId							学校id	
*	cId									ObjectId							次id																	年级id
*	teaWeek								String								教师周任课天数(0:否 1:是)
*	teaDay								String								教师日最大连课节数(0:否 1:是)
*	teaOverClass						String								教师跨班连堂(0:否 1:是)
*	teaStep								String								教师跨班进度一致(0:否 1:是)
*	teaRule								String								教师规则(0:否 1:是)
*	teaMutex							String								教师互斥(0:否 1:是)
*
*/


public class AutoTeaSetEntry extends BaseDBObject {
    
    private static final long serialVersionUID = -6860924985625206344L;
    
    public AutoTeaSetEntry() {
    	
    }

    public AutoTeaSetEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("schoolId");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("schoolId", schoolId);
    }
    
    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gradeId");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gradeId", gradeId);
    }
    
    public ObjectId getCiId() {
        return getSimpleObjecIDValue("ciId");
    }

    public void setCiId(ObjectId ciId) {
        setSimpleValue("ciId", ciId);
    }
    
    public String getTeaWeek() {
        return getSimpleStringValueDef("teaWeek", "");
    }

    public void setTeaWeek(String teaWeek) {
        setSimpleValue("teaWeek", teaWeek);
    }
    
    public String getTeaDay() {
        return getSimpleStringValueDef("teaDay", "");
    }

    public void setTeaDay(String teaDay) {
        setSimpleValue("teaDay", teaDay);
    }
    
    public String getTeaOverClass() {
        return getSimpleStringValueDef("teaOverClass", "");
    }

    public void setTeaOverClass(String teaOverClass) {
        setSimpleValue("teaOverClass", teaOverClass);
    }
    
    public String getTeaStep() {
        return getSimpleStringValueDef("teaStep", "");
    }

    public void setTeaStep(String teaStep) {
        setSimpleValue("teaStep", teaStep);
    }
    
    public String getTeaRule() {
        return getSimpleStringValueDef("teaRule", "");
    }

    public void setTeaRule(String teaRule) {
        setSimpleValue("teaRule", teaRule);
    }
    
    public String getTeaMutex() {
        return getSimpleStringValueDef("teaMutex", "");
    }

    public void setTeaMutex(String teaMutex) {
        setSimpleValue("teaMutex", teaMutex);
    }
}
