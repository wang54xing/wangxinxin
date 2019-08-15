package com.pojo.autoPK;



import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
*自动排课-排课规则-其他设置(不可排课的格子)
*
*	entry								type								annotation
*	
*	schoolId							ObjectId							学校id
*	gradeId								ObjectId							学校id	
*	cId									ObjectId							次id																	年级id
*	x									String								周几
*	Y									String								第几节课
*
*/


public class AutoPkGzSetEntry extends BaseDBObject {
    
    private static final long serialVersionUID = -6860924985625206344L;
    
    public AutoPkGzSetEntry() {
    	
    }

    public AutoPkGzSetEntry(BasicDBObject baseEntry) {
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
    
    public String getX() {
    	
        return getSimpleStringValue("x");
    }

    public void setX(String x) {
        setSimpleValue("x", x);
    }
    
    public String getY() {
        return getSimpleStringValue("y");
    }

    public void setY(String y) {
        setSimpleValue("y", y);
    }
    
    
   
}
