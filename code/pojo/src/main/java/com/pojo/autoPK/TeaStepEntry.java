package com.pojo.autoPK;



import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;

import org.bson.types.ObjectId;

/**
*自动排课-排课规则-教师设置-教师跨班进度一致 
*
*	entry								type								annotation
*
*	schoolId							ObjectId							学校id
*	gradeId								ObjectId							年级id
*	cId									ObjectId							次id		
*	teaList								List<ObjectId>						教师信息	
*
*/


public class TeaStepEntry extends BaseDBObject {
    
    private static final long serialVersionUID = -6860924985625206344L;
    
    public TeaStepEntry() {
    	
    }

    public TeaStepEntry(BasicDBObject baseEntry) {
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
    
    public List<ObjectId> getTeaList() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("teaList");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(((ObjectId)o));
			}
		}
		return retList;
	}

	public void setTeaList(List<ObjectId> teaList) {
		setSimpleValue("teaList", MongoUtils.convert(teaList));
	}

   
}
