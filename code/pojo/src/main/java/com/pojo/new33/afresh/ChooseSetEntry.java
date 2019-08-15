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
 * Created by James on 2019-05-13.
 *
 *  *  设置分班规则表
 *
 * schoolId 	学校id
 * gradeId		年级id
 * ciId			次Id
 * type			组合
 * subComName	组合名称
 * classNum		插尖班数量
 * classContain	插尖班容量
 * floatNum		插尖班浮动人数
 *
 */
public class ChooseSetEntry extends BaseDBObject {

    private static final long serialVersionUID = 1242991562318562073L;

    public ChooseSetEntry() {

    }
    
    public ChooseSetEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gid) {
        setSimpleValue("gid", gid);
    }

    public ObjectId getCiId() {
        return getSimpleObjecIDValue("ciId");
    }
    
    public void setCiId(ObjectId ciId) {
        setSimpleValue("ciId", ciId);
    }
    
    public String getType() {
        return getSimpleStringValue("type");
    }

    public void setType(String type) {
        setSimpleValue("type", type);
    }
    
    public String getSubComName() {
        return getSimpleStringValue("subComName");
    }

    public void setSubComName(String subComName) {
        setSimpleValue("subComName", subComName);
    }
    
    public Integer getClassNum() {
        return getSimpleIntegerValueDef("classNum",0);

    }

    public void setClassNum(Integer classNum) {
        setSimpleValue("classNum", classNum);
    }
    
    public Integer getClassContain() {
        return getSimpleIntegerValueDef("classContain",0);

    }

    public void setClassContain(Integer classContain) {
        setSimpleValue("classContain", classContain);
    }

    public Integer getFloatNum() {
        return getSimpleIntegerValueDef("floatNum",0);

    }

    public void setFloatNum(Integer floatNum) {
        setSimpleValue("floatNum", floatNum);
    }
}
