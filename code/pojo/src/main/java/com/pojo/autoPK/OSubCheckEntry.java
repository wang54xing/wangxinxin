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
*自动排课-排课规则-其他设置-选择可排学科
*
*	entry								type								annotation
*
*	schoolId							ObjectId							学校id
*	gradeId								ObjectId							年级id
*	cId									ObjectId							次id				
*	subList								List<ObjectId>						学科	
*
*/


public class OSubCheckEntry extends BaseDBObject {
    
    private static final long serialVersionUID = -6860924985625206344L;
    
    public OSubCheckEntry() {
    	
    }

    public OSubCheckEntry(BasicDBObject baseEntry) {
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
    
    public void setCourseNum(int courseNum) {
        setSimpleValue("courseNum", courseNum);
    }
    
    public int getCourseNum() {
        return getSimpleIntegerValue("courseNum");
    }

    public List<ObjectId> getSubList() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("subList");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(((ObjectId)o));
			}
		}
		return retList;
	}

	public void setSubList(List<ObjectId> subList) {
		setSimpleValue("subList", MongoUtils.convert(subList));
	}

   
}
