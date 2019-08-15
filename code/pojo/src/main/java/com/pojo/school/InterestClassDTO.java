package com.pojo.school;


import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hao on 2015/3/20.
 */
public class InterestClassDTO {
    /*
    * className -->cn
    * teacherId-->tid
    * studentCount-->st
    * classTime-->ct
    * subjectId-->sid
    * openTime-->ot
    * closeTime-->clt
    * firstTerm-->ft 上半学期
    * secondTerm-->st 下半学期
    * gradeList-->gl
    * termType-->tt
    * isLongCourse-->ilc
    * */
    private String id;
    private String schoolId;
    private String className;
    private String teacherId;
    private int studentCount;
    private int totalStudentCount;
    private List<String> classTime;
    private String subjectId;
    private Date openTime;
    private Date closeTime;
    private String firstTerm;
    private String secondTerm;
    private List<String> gradeList;
    private boolean isLongCourse;
    private int state;//1表示开放   0 表示关闭 可手动调节
    private List<InterestClassStudentDTO> studentList;
    private List<InterestClassStudentDTO> fstulist;
    private List<InterestClassStudentDTO> sstulist;
    private int termType;
    private String teacherName;
    private String classContent;
    private boolean isRequired;
    private String typeId;
    private String room;
    private int sex;

    //额外字段 用于页面展示或者判断
    private boolean isChoose;
    private int openState;//openTime未到达选课时间 1 可选 2   closeTime超过选课时间3
    private int chooseType;//1表示荀泽短课1  2表示选择短课2
    private boolean timeConflict;//上课时间冲突
    private int lessonCount;//课程数量*(页面使用)
    private int fchoose;
    private int schoose;
    private ObjectId relationId;
    private String typeName;//班级所属类别
    private String teacherAvatar;





    public InterestClassDTO(){}
    public InterestClassDTO(InterestClassEntry interestClassEntry) {
        this.id=interestClassEntry.getID().toString();
        this.schoolId=interestClassEntry.getSchoolId().toString();
        this.className=interestClassEntry.getClassName();
        if(null!=interestClassEntry.getTeacherId())
        {
          this.teacherId=interestClassEntry.getTeacherId().toString();
        }
        else
        {
        	this.teacherId=Constant.EMPTY;
        }
        this.studentCount=interestClassEntry.getStudentCount();
        this.classTime=interestClassEntry.getClassTime();
        this.subjectId=interestClassEntry.getSubjectId().toString();
        if(interestClassEntry.getOpenTime()!=0)
            this.openTime=new Date(interestClassEntry.getOpenTime());
        if(interestClassEntry.getCloseTime()!=0)
            this.closeTime=new Date(interestClassEntry.getCloseTime());
        this.firstTerm=interestClassEntry.getFirstTerm()+"";
        this.secondTerm=interestClassEntry.getSecondTerm()+"";
        this.isLongCourse=interestClassEntry.getIsLongCourse()==1?true:false;
        this.state=interestClassEntry.getState();
        this.termType=interestClassEntry.getTermType();
        this.totalStudentCount=interestClassEntry.getTotalCount();
        this.classContent=interestClassEntry.getClassContent();
        ObjectId typeId = interestClassEntry.getTypeId();
        if(null == typeId){
            this.typeId = "";
        } else {
            this.typeId=typeId.toString();
        }



        List<ObjectId> grades=interestClassEntry.getGradeIds();
        List<String>   gradIds=new ArrayList<String>();
        if(grades!=null){
            for(ObjectId objectId:grades){
                if(objectId!=null)
                    gradIds.add(objectId.toString());
            }
        }
        this.gradeList=gradIds;
        List<InterestClassStudent> studentIds=interestClassEntry.getInterestClassStudents();

        List<InterestClassStudentDTO> stringList = new ArrayList<InterestClassStudentDTO>();
        if(studentIds!=null){
                for(InterestClassStudent interestClassStudent :studentIds){
                    InterestClassStudentDTO interestClassStudentDTO =new InterestClassStudentDTO();
                    interestClassStudentDTO.setCourseType(interestClassStudent.getCourseType());
                    interestClassStudentDTO.setStudentId(interestClassStudent.getStudentId().toString());
                    interestClassStudentDTO.setTermType(interestClassStudent.getTermType());
                    stringList.add(interestClassStudentDTO);
                }
        }
        this.studentList=stringList;
        this.room = interestClassEntry.getRoom();
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public List<String> getClassTime() {
        return classTime;
    }

    public void setClassTime(List<String> classTime) {
        this.classTime = classTime;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public String getFirstTerm() {
        return firstTerm;
    }

    public void setFirstTerm(String firstTerm) {
        this.firstTerm = firstTerm;
    }

    public String getSecondTerm() {
        return secondTerm;
    }

    public void setSecondTerm(String secondTerm) {
        this.secondTerm = secondTerm;
    }

    public List<String> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<String> gradeList) {
        this.gradeList = gradeList;
    }

    public String getClassContent() {
        return classContent;
    }

    public void setClassContent(String classContent) {
        this.classContent = classContent;
    }

    public InterestClassEntry exportEntry() {
        InterestClassEntry expandClassEntry=new InterestClassEntry();

        if(id!=null && !"".equals(id.trim())){
            expandClassEntry.setID(new ObjectId(id));
        }
        expandClassEntry.setClassName(this.className);
        expandClassEntry.setClassTime(this.classTime);
        Date date = this.getCloseTime();
        if(date!=null){
            expandClassEntry.setCloseTime(this.closeTime.getTime());
        }
        if(firstTerm!=null)
        expandClassEntry.setFirstTerm(Integer.parseInt(firstTerm));
        if(secondTerm!=null)
        expandClassEntry.setSecondTerm(Integer.parseInt(secondTerm));

        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        if(gradeList!=null){
            for(String str:gradeList){
                objectIdList.add(new ObjectId(str));
            }
        }
        expandClassEntry.setGradeIds(objectIdList);
        expandClassEntry.setOpenTime(openTime.getTime());
        expandClassEntry.setStudentCount(studentCount);
        expandClassEntry.setSubjectId(new ObjectId(subjectId));
        expandClassEntry.setTeacherId(new ObjectId(teacherId));
        expandClassEntry.setState(state);
        expandClassEntry.setSchoolId(new ObjectId(schoolId));
        expandClassEntry.setTermType(termType);
        expandClassEntry.setTotalCount(totalStudentCount);
        expandClassEntry.setRelationId(this.relationId);
        expandClassEntry.setClassContent(this.classContent);
        expandClassEntry.setRoom(this.room);
        expandClassEntry.setSex(this.sex);
        if(null==typeId ||typeId.equals("")){
            expandClassEntry.setTypeId(null);
        } else {
            expandClassEntry.setTypeId(new ObjectId(typeId));
        }
        if(studentList==null){
            expandClassEntry.setInterestClassStudents(new ArrayList<InterestClassStudent>());
        }else{
            List<InterestClassStudent> students=new ArrayList<InterestClassStudent>();
            for(InterestClassStudentDTO studentDTO:studentList){
                InterestClassStudent interestClassStudent=new InterestClassStudent();
                interestClassStudent.setCourseType(studentDTO.getCourseType());
                interestClassStudent.setStudentId(new ObjectId(studentDTO.getStudentId()));
                students.add(interestClassStudent);
            }
            expandClassEntry.setInterestClassStudents(students);
        }
        if(isLongCourse) expandClassEntry.setIsLongCourse(1);
        else{ expandClassEntry.setIsLongCourse(0);}
        return expandClassEntry;
    }


    public void setIsLongCourse(boolean isLongCourse) {
        this.isLongCourse = isLongCourse;
    }
    public boolean getIsLongCourse() {
        return isLongCourse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<InterestClassStudentDTO> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<InterestClassStudentDTO> studentList) {
        this.studentList = studentList;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getTotalStudentCount() {
        return totalStudentCount;
    }

    public void setTotalStudentCount(int totalStudentCount) {
        this.totalStudentCount = totalStudentCount;
    }
    public boolean getIsChoose(){
        return this.isChoose;
    }
    public void setIsChoose(boolean k){
        this.isChoose=k;
    }

    public int getOpenState() {
        return openState;
    }

    public void setOpenState(int openState) {
        this.openState = openState;
    }

    public int getChooseType() {
        return chooseType;
    }

    public void setChooseType(int chooseType) {
        this.chooseType = chooseType;
    }

    public boolean isTimeConflict() {
        return timeConflict;
    }

    public void setTimeConflict(boolean timeConflict) {
        this.timeConflict = timeConflict;
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }

    public int getFchoose() {
        return fchoose;
    }

    public void setFchoose(int fchoose) {
        this.fchoose = fchoose;
    }

    public int getSchoose() {
        return schoose;
    }

    public void setSchoose(int schoose) {
        this.schoose = schoose;
    }

    public List<InterestClassStudentDTO> getFstulist() {
        return fstulist;
    }

    public void setFstulist(List<InterestClassStudentDTO> fstulist) {
        this.fstulist = fstulist;
    }

    public List<InterestClassStudentDTO> getSstulist() {
        return sstulist;
    }

    public void setSstulist(List<InterestClassStudentDTO> sstulist) {
        this.sstulist = sstulist;
    }

    public ObjectId getRelationId() {
        return relationId;
    }

    public void setRelationId(ObjectId relationId) {
        this.relationId = relationId;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<InterestClassStudentDTO> getCurrentStudentList(){
        List<InterestClassStudentDTO> list = new ArrayList<InterestClassStudentDTO>();
        if(null!=studentList && studentList.size()>0){
            for(InterestClassStudentDTO dto: studentList){
                if(dto.getTermType() == termType){
                    list.add(dto);
                }
            }
        }
        return list;
    }

    public List<InterestClassStudentDTO> getTermStudentList(int termType){
        List<InterestClassStudentDTO> list = new ArrayList<InterestClassStudentDTO>();
        if(null!=studentList && studentList.size()>0){
            for(InterestClassStudentDTO dto: studentList){
                if(dto.getTermType() == termType){
                    list.add(dto);
                }
            }
        }
        return list;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
