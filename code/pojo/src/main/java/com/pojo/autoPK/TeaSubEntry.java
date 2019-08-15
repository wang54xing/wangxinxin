package com.pojo.autoPK;



import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
*自动排课-排课规则-教师设置-教师周任课天数
*
*	entry								type								annotation
*
*	schoolId							ObjectId							学校id
*	subjectId							ObjectId							学科id
*	userId								ObjectId							教师id
*				
*/


public class TeaSubEntry extends BaseDBObject {
    
    private static final long serialVersionUID = -6860924985625206344L;
    
    public TeaSubEntry() {
        super();
    }

    public TeaSubEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
   
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("schoolId");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("schoolId", schoolId);
    }
    
    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subjectId");
    }
    
    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subjectId", subjectId);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("userId");
    }
    
    public void setUserId(ObjectId userId) {
        setSimpleValue("userId", userId);
    }

    
}
