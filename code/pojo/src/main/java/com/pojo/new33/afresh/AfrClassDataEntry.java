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
 * Created by James on 2019-06-12.
 *
 *  *  学生选科数据统计表
 *
		db							annotion
		ciId						次id
		schoolId					学校id
		classId						班级id
		stuNum						班级学生人数
		commonSubList				共同课程
		zouSubList<SubNumEntry>		走班课程
		type						0等级 1合格			
		
		
						
 *
 */
public class AfrClassDataEntry extends BaseDBObject {

    private static final long serialVersionUID = 1242991562318562073L;

    public AfrClassDataEntry() {
    	
    }
    
    public ObjectId getCiId() {
        return getSimpleObjecIDValue("ciId");
    }

    public void setCiId(ObjectId ciId) {
        setSimpleValue("ciId", ciId);
    }
    
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("schoolId");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("schoolId", schoolId);
    }
    
    public void setClassId(ObjectId classId) {
        setSimpleValue("classId", classId);
    }
    
    public ObjectId getclassId() {
        return getSimpleObjecIDValue("classId");
    }
    
    public List<ObjectId> getCommonSubList() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("commonSubList");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setCommonSubList(List<ObjectId> commonSubList) {
        setSimpleValue("commonSubList", MongoUtils.convert(commonSubList));
    }
    
    public Integer getStuNum() {
        return getSimpleIntegerValueDef("stuNum",0);

    }

    public void setStuNum(Integer stuNum) {
        setSimpleValue("stuNum", stuNum);
    }
    
    public List<SubNumEntry> getZouSubList() {
        List<SubNumEntry> retList = new ArrayList<SubNumEntry>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("zouSubList");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((SubNumEntry) o));
            }
        }
        return retList;
    }

    public void setZouSubList(List<SubNumEntry> zouSubList) {
        setSimpleValue("zouSubList", MongoUtils.convert(zouSubList));
    }
    
    
    
    
    
    
    
    

    
}
