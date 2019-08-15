package com.pojo.autoPK;



import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
*自动排课-排课规则-其他设置
*
*	entry								type								annotation
*	
*	schoolId							ObjectId							学校id
*	gradeId								ObjectId							学校id	
*	cId									ObjectId							次id																	年级id
*	subCheck							String								选择可排学科(默认系统识别)(0:否 1:是)
*	boxcheck							String								选择可排格子(默认系统识别)(0:否 1:是)
*	ltCheck								String								连堂处理(0:否 1:是)
*	classCheck							String								多功能教室数量设置 (0:否 1:是)
*	periodCheck							String								分段设置 (0:否 1:是)
*
*/


public class AutoOtherSetEntry extends BaseDBObject {
    
    private static final long serialVersionUID = -6860924985625206344L;
    
    public AutoOtherSetEntry() {
    	
    }

    public AutoOtherSetEntry(BasicDBObject baseEntry) {
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
    
    public String getSubCheck() {
        return getSimpleStringValueDef("subCheck", "");
    }

    public void setSubCheck(String subCheck) {
        setSimpleValue("subCheck", subCheck);
    }
    
    public String getBoxcheck() {
        return getSimpleStringValueDef("boxcheck", "");
    }

    public void setBoxcheck(String boxcheck) {
        setSimpleValue("boxcheck", boxcheck);
    }
    
    public String getLtCheck() {
        return getSimpleStringValueDef("ltCheck", "");
    }

    public void setLtCheck(String ltCheck) {
        setSimpleValue("ltCheck", ltCheck);
    }
    
    public String getClassCheck() {
        return getSimpleStringValueDef("classCheck", "");
    }

    public void setClassCheck(String classCheck) {
        setSimpleValue("classCheck", classCheck);
    }
    
    public String getPeriodCheck() {
        return getSimpleStringValueDef("periodCheck", "");
    }

    public void setPeriodCheck(String periodCheck) {
        setSimpleValue("periodCheck", periodCheck);
    }
    
}
