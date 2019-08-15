package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

import com.pojo.app.IdNameValuePair;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * Created by Hao on 2015/3/20.
 */
public class InterestClassEntry extends BaseDBObject {
    /*
    * schoolId --> sld  学校id
    * className -->cn   班级名称
    * classContent ->clc 班级介绍
    * teacherId-->tid   老师id
    * studentCount-->sc 当前学生数量
    * classTime-->ct    班级开课时段
    * subjectId-->sid   科目Id
    * openTime-->ot     开课时间
    * closeTime-->clt  课程关闭时间
    * firstTerm-->ft 上半学期
    * secondTerm-->st 下半学期
    * gradeList-->gl
    * isLongCourse-->ilc 1表示长课程  0表示短课  默认-1
    * stat-->  可用状态 1表示可用 0  表示不可用
    * stuList-->stl  学生记录  内嵌文档 InterestClassStudent
    * termType --> tt  学期  用于开始新学期
    * totalCount-->tc  可以报名人数
    * relationId-->rid  关系
    *term-->tm List<IdNameValuePair>学期 id保留 name学期名称 value对应的termType
    * typeId-->tyid 类型id
    * isRemove-->ir是否删除，0未删除   1已删除   默认0
    * room  rm 上课教室
    * sex --> sex 从属性别
    * */


    /**
	 * 
	 */
	private static final long serialVersionUID = 7135182651832897183L;
	public InterestClassEntry(){
        BasicDBObject entry=new BasicDBObject();
        entry.append("cn","").
                append("clc","").
                append("sld", null).
                append("tid", null).
                append("sc", 0).
                append("ct", null).
                append("sid", null).
                append("ot", 0).
                append("clt",0).
                append("ft", null).
                append("st", null).
                append("gl", null).
                append("ilc", -1).
                append("stat",1).
                append("stl", null).
                append("tt", 1).
                append("tc", 0).
                append("rid",null).
                append("tm",null).
                append("tyid", null).
                append("rm", "").
                append("sex",2)
                ;
        setBaseEntry(entry);
    }

    public InterestClassEntry(BasicDBObject dbObject) {
        setBaseEntry(dbObject);
    }

    public void setTotalCount(int totalCount){
        setSimpleValue("tc",totalCount);
    }
    public int getTotalCount(){
        return getSimpleIntegerValue("tc");
    }

    public void setTermType(int termType){
        setSimpleValue("tt",termType);
    }
    public int getTermType(){
        return getSimpleIntegerValue("tt");
    }
    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sld",schoolId);
    }
    public ObjectId getSchoolId(){
        return  getSimpleObjecIDValue("sld");
    }
    public void setStudentCount(int studentCount){
        setSimpleValue("sc",studentCount);
    }
    public int getStudentCount(){
        return getSimpleIntegerValue("sc");
    }
    public void setClassName(String className){
        setSimpleValue("cn",className);
    }
    public String getClassName(){
        return getSimpleStringValue("cn");
    }

    public void setTeacherId(ObjectId objectId){
        setSimpleValue("tid",objectId);
    }
    public ObjectId getTeacherId(){
        return getSimpleObjecIDValue("tid");
    }
    public void setClassTime(List<String> classTime){
        setSimpleValue("ct",classTime);
    }
    public List<String> getClassTime(){
        @SuppressWarnings("rawtypes")
		List stringList=(List)getSimpleObjectValue("ct");
        return stringList;
    }
   

    public void setSubjectId(ObjectId objectId){
        setSimpleValue("sid",objectId);
    }
    public ObjectId getSubjectId(){
        return  getSimpleObjecIDValue("sid");
    }

    public void setRelationId(ObjectId objectId){
        setSimpleValue("rid",objectId);
    }
    public ObjectId getRelationId(){
        return  getSimpleObjecIDValue("rid");
    }

    public void setOpenTime(long time){
        setSimpleValue("ot",time);
    }
    public long getOpenTime(){
        if(getSimpleObjectValue("ot")==null) return 0;
        return getSimpleLongValue("ot");
    }

    public void setCloseTime(long closeTime){
        setSimpleValue("clt",closeTime);
    }
    public long getCloseTime(){
        if(getSimpleObjectValue("clt")==null) return 0;
        return getSimpleLongValue("clt");
    }

    public void setFirstTerm(int firstTerm){
        setSimpleValue("ft",firstTerm);
    }
    public int getFirstTerm(){
        return  getSimpleIntegerValue("ft");
    }

    public void setSecondTerm(int secondTerm){
        setSimpleValue("st",secondTerm);
    }

    public int getSecondTerm(){
        return getSimpleIntegerValue("st");
    }

    public void setGradeIds(List<ObjectId> gradeIds){
        setSimpleValue("gl",gradeIds);
    }
    public List<ObjectId> getGradeIds(){
        List<ObjectId> stringList=(List)getSimpleObjectValue("gl");
        return stringList;
    }
    public void setIsLongCourse(int courseLength){
        setSimpleValue("ilc",courseLength);
    }
    public int getIsLongCourse(){
        return getSimpleIntegerValue("ilc");
    }

    public void setState(int state){
        setSimpleValue("stat",state);
    }
    public int getState(){
        return  getSimpleIntegerValue("stat");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("ir", isRemove);
    }

    public int getIsRemove(){
        return getSimpleIntegerValueDef("ir", 0);
    }

    public void setInterestClassStudents(List<InterestClassStudent> objectIdList){
//        List<DBObject> list = MongoUtils.convert(objectIdList);
        setSimpleValue("stl", MongoUtils.convert(MongoUtils.fetchDBObjectList(objectIdList)));
    }
    public List<InterestClassStudent> getInterestClassStudents(){
        List<InterestClassStudent> retList =new ArrayList<InterestClassStudent>();
//        return  (List)getSimpleObjectValue("stl");
        BasicDBList list =(BasicDBList)getSimpleObjectValue("stl");
        if(null!=list && !list.isEmpty()) {
            for(Object o:list) {
                retList.add(new InterestClassStudent((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setClassContent(String classContent){ setSimpleValue("clc",classContent); }
    public String getClassContent() {return getSimpleStringValue("clc");}

    public List<IdNameValuePair> getTerm(){
        List<IdNameValuePair> retList = new ArrayList<IdNameValuePair>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("tm");
        if(null != list && list.size()>0){
            for(Object o : list){
                retList.add(new IdNameValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setTerm(List<IdNameValuePair> term){
        setSimpleValue("tm", MongoUtils.convert(MongoUtils.fetchDBObjectList(term)));
    }

    public ObjectId getTypeId(){
        if(getBaseEntry().containsField("tyid")){
            return getSimpleObjecIDValue("tyid");
        }
        return null;
    }

    public void setTypeId(ObjectId typeId){
        setSimpleValue("tyid", typeId);
    }

    public List<InterestClassStudent> getCurrentInterestClassStudents(){
        List<InterestClassStudent> studentList = getInterestClassStudents();
        List<InterestClassStudent> currentStudentList = new ArrayList<InterestClassStudent>();
        if(studentList!=null && studentList.size()>0){
            for(InterestClassStudent student : studentList){
                if(student.getTermType() == getTermType()){
                    currentStudentList.add(student);
                }
            }
        }
        return currentStudentList;
    }

    public List<InterestClassStudent> getInterestClassStudentsByTermType(int termType){
        List<InterestClassStudent> studentList = getInterestClassStudents();
        List<InterestClassStudent> currentStudentList = new ArrayList<InterestClassStudent>();
        if(studentList!=null && studentList.size()>0){
            for(InterestClassStudent student : studentList){
                if(student.getTermType() == termType){
                    currentStudentList.add(student);
                }
            }
        }
        return currentStudentList;
    }

    public String getRoom(){
        if(getBaseEntry().containsField("rm")){
            return getSimpleStringValue("rm");
        } else {
            return "";
        }
    }

    public void setRoom(String room){
        setSimpleValue("rm", room);
    }
    public void setSex(int sex){
        setSimpleValue("sex", sex);
    }

    public int getSex(){
        return getSimpleIntegerValue("sex");
    }

 }
