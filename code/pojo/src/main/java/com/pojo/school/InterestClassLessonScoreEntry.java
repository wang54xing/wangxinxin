package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/** 课时得分
 * Created by Hao on 2015/4/17.
 */
public class InterestClassLessonScoreEntry extends BaseDBObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3121456935425658369L;
	/*
    *
    * 用于记录兴趣班学生得分记录
    *
    * classId  -->cid  班级id
    * userId--->uid   学生id
    * lessonIndex--> li 课时索引
    * studentScore --->ss 学生得分 （每个lesson的得分）
    * teacherComment--->tc 老师评语
    * attendance  -->at 考勤 1：到  0：缺席
    * lessonPictureUrl  --> lpu 课时成果
    * lessonName -->ln 课时名
    * termType -->tt
    * weekindex 教学周
    * */
    public InterestClassLessonScoreEntry(){
        BasicDBObject entry=new BasicDBObject()
                .append("cid",null)
                .append("uid",null)
                .append("li",1)
                .append("ss",0)
                .append("ln", "课时1")
                .append("at",1)
                .append("tc",null)
                .append("lpu", null)
                .append("tt", 1)
                .append("wi", 1)
                ;
        setBaseEntry(entry);
    }
    public InterestClassLessonScoreEntry(BasicDBObject basicDBObject){
        setBaseEntry(basicDBObject);
    }
    public ObjectId getClassId(){
        return  getSimpleObjecIDValue("cid");
    }
    public ObjectId getUserId(){
        return  getSimpleObjecIDValue("uid");
    }
    public int getLessonIndex(){
        return  getSimpleIntegerValue("li");
    }
    public Integer getStudentScore(){
        return  getSimpleIntegerValue("ss");
    }
    public String getTeacherComment(){
        if(getBaseEntry().containsField("tc")){
            return getSimpleStringValue("tc");
        }
        return "上课时十分认真";
    }
    public String getLessonPictureUrl(){
        if(getBaseEntry().containsField("lpu")){
            return getSimpleStringValue("lpu");
        }
        return "";

    }
    public String getLessonName(){
        if(getBaseEntry().containsField("ln")){
            return getSimpleStringValue("ln");
        }
        return "课程"+getLessonIndex();
    }
    public Integer getAttendance(){
        if(getBaseEntry().containsField("at")){
            return getSimpleIntegerValue("at");
        }
        return 1;
    }

    public void setClassId(ObjectId classId){
        setSimpleValue("cid",classId);
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public void setLessonIndex(int lessonIndex){
        setSimpleValue("li",lessonIndex);
    }
    public void setStudentScore(Integer studentScore){
        setSimpleValue("ss",studentScore);
    }
    public void setTeacherComment(String teacherComment){setSimpleValue("tc", teacherComment);}
    public void setLessonPictureUrl(String lessonPictureUrl){setSimpleValue("lpu",lessonPictureUrl);}
    public void setLessonName(String lessonName){setSimpleValue("ln", lessonName);}
    public void setAttendance(Integer attendance){setSimpleValue("at", attendance);}

    public int getTermType(){
        return getSimpleIntegerValueDef("tt", 1);
    }

    public void setTermType(int termType){
        setSimpleValue("tt", termType);
    }

    public int getWeekIndex(){
        return getSimpleIntegerValueDef("wi", 1);
    }

    public void setWeekIndex(int weekIndex){
        setSimpleValue("wi", weekIndex);
    }

 }
