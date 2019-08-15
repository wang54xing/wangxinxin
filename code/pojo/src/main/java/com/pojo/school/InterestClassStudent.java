package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by Hao on 2015/4/21.
 */
public class InterestClassStudent extends BaseDBObject  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6280678302566420981L;

	/*
    *
    *courseType-->ct 课程类型 0表示长课，1 表示短课1  2表示短课2
    * termType --> tt 学期类型
    *studentId -->sid 学生id
    *dropstate --> ds 退课状态 0：学生可以退课， 1：学生不可以退课   默认0
    *
    * */
    public InterestClassStudent(){
        BasicDBObject basicDBObject=new BasicDBObject();
        basicDBObject.append("ct",0).append("sid",null).append("ds", 0);
        setBaseEntry(basicDBObject);
    }

    public InterestClassStudent(BasicDBObject o) {
        setBaseEntry(o);
    }

    public int getCourseType() {
        return getSimpleIntegerValue("ct");
    }

    public void setCourseType(int courseType) {
        setSimpleValue("ct",courseType);
    }

    public ObjectId getStudentId() {
        return  getSimpleObjecIDValue("sid");
    }

    public void setStudentId(ObjectId studentId) {
        setSimpleValue("sid",studentId);
    }

    public int getTermType(){
        if(getBaseEntry().containsField("tt")){
            return getSimpleIntegerValue("tt");
        }
        return 1;
    }

    public void setTermType(int termType){
        setSimpleValue("tt", termType);
    }

    public int getDropState(){
        return getSimpleIntegerValueDef("ds", 0);
    }

    public void setDropState(int dropState){
        setSimpleValue("ds", dropState);
    }
}
