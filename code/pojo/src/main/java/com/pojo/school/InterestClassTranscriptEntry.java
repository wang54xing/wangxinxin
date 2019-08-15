package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/** 期末总评
 * Created by Hao on 2015/4/17.
 */
public class InterestClassTranscriptEntry extends BaseDBObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8256966727074284867L;
	/*
    *兴趣班学生成绩单记录表
    *
    * classId  -->cid   班级id
    * userId  --->uid   学生id
    * resultPictureUrl --->rpu  学生成果展示(图片)  路径
    * teacherComment -->tc 老师评语
    * totalLessonScore -->tls  课时总分
    * finalResult --> fr 学期总评分
    *termType -->tt
    *
    * */
    public InterestClassTranscriptEntry(){
        BasicDBObject entry=new BasicDBObject();
        entry.append("cid",null).
                append("uid", null).
                append("rpu", "").
                append("tc", "").
                append("tls", 0).
                append("fr", 0).
                append("tt", 1);
    }

    public InterestClassTranscriptEntry(BasicDBObject basicDBObject){
        setBaseEntry(basicDBObject);
    }
    public ObjectId getClassId(){
        return  getSimpleObjecIDValue("cid");
    }
    public ObjectId getUserId(){
        return  getSimpleObjecIDValue("uid");
    }
    public String  getResultPicUrl(){
        return  getSimpleStringValue("rpu");
    }
    public String getTeacherComment(){
        return  getSimpleStringValue("tc");
    }
    public int getTotalLessonScore(){
        return  getSimpleIntegerValue("tls");
    }
    public int getFinalResult(){
        return  getSimpleIntegerValue("fr");
    }
    public void setClassId(ObjectId classId){
        setSimpleValue("cid",classId);
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public void  setResultPictureUrl(String pictureUrl){
        setSimpleValue("rpu",pictureUrl);
    }
    public void setTeacherComment(String teacherComment){
        setSimpleValue("tc",teacherComment);
    }
    public void setTotalLessonScore(int totalLessonScore){
        setSimpleValue("tls",totalLessonScore);
    }
    public void setFinalResult(int score){
        setSimpleValue("fr",score);
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
}
