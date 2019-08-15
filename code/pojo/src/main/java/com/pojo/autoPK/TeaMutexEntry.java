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
*自动排课-排课规则-教师设置-教师互斥（不在同一时间上课）
*
*	entry								type								annotation
*
*	schoolId							ObjectId							学校id
*	gradeId								ObjectId							年级id
*	cId									ObjectId							次id			
*	mutexNum							int									互斥组序号
*	teaSubList							List<TeaSubEntry>					学科教师	
*	ir									String								删除标记(0:否 1:是)
*/


public class TeaMutexEntry extends BaseDBObject {
    
    private static final long serialVersionUID = -6860924985625206344L;

    public TeaMutexEntry() {
    	
    }
    
    public TeaMutexEntry(BasicDBObject baseEntry) {
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
    
    public void setMutexNum(int mutexNum) {
        setSimpleValue("mutexNum", mutexNum);
    }
    
    public int getMutexNum() {
        return getSimpleIntegerValue("mutexNum");
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

	public String getIsRemove() {
		return getSimpleStringValue("ir");
	}


	public void setIsRemove(String ir) {
		setSimpleValue("ir", ir);
	}
}
