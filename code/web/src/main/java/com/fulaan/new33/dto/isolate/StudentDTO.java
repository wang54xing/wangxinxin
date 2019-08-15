package com.fulaan.new33.dto.isolate;

import org.bson.types.ObjectId;

import com.pojo.new33.isolate.StudentEntry;

public class StudentDTO {
    private String id;

    private String schoolId;

    private String xqid;

    private String userId;

    private String userName;

    private String classId;

    private String className;

    private String gradeId;

    private String gradeName;

    private String studyNumber;

    private Integer type;

    private Integer level;
    private Integer sex;
    private String combiname;
    private String subjectId1;
    private String subjectId2;
    private String subjectId3;

    private String sexStr;

    private String jxbName;

    private String tagName;
    private Integer classXH;

    public Integer getClassXH() {
        return classXH;
    }

    public void setClassXH(Integer classXH) {
        this.classXH = classXH;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public StudentDTO() {
    }

    public StudentDTO(StudentEntry entry) {
        id = entry.getID().toString();
        schoolId = entry.getSid().toString();
        xqid = entry.getXqid().toString();
        userId = entry.getUserId().toString();
        userName = entry.getUserName();
        classId = entry.getClassId().toString();
        className = entry.getClassName();
        gradeId = entry.getGradeId().toString();
        gradeName = entry.getGradeName();
        studyNumber = entry.getStudyNum();
        type = entry.getType();
        sex = entry.getSex();
        level = entry.getLevel();
        subjectId1 = entry.getSubjectId1() == null ? null : entry.getSubjectId1().toString();
        subjectId2 = entry.getSubjectId2() == null ? null : entry.getSubjectId2().toString();
        subjectId3 = entry.getSubjectId3() == null ? null : entry.getSubjectId3().toString();
        combiname = entry.getCombiname();
        if(sex == 1){
            this.sexStr = "男";
        }else if(sex == 0){
            this.sexStr = "女";
        }else{
            this.sexStr = "";
        }
    }

    public StudentEntry buildEntry() {
        StudentEntry entry = new StudentEntry(new ObjectId(schoolId), new ObjectId(xqid), new ObjectId(userId), userName,
                new ObjectId(classId), className,
                new ObjectId(gradeId), gradeName, sex, studyNumber, type,
                level);
        if (id != null) {
            entry.setID(new ObjectId(id));
        }
        return entry;
    }

//	public StudentEntry getEntry() {
//		StudentEntry entry = new StudentEntry(new ObjectId(schoolId), new ObjectId(xqid), new ObjectId(userId), userName,
//				new ObjectId(classId), className,
//				new ObjectId(gradeId), gradeName,  sex, studyNumber, type,
//				level,combiname,subjectId1==null?null:new ObjectId(subjectId1),
//				new ObjectId(subjectId2)==null?null:new ObjectId(subjectId2),
//				new ObjectId(subjectId3)==null?null:new ObjectId(subjectId3));
//		if(id!=null) {
//			entry.setID(new ObjectId(id));
//		}
//		return entry;
//	}


    public String getJxbName() {
        return jxbName;
    }

    public void setJxbName(String jxbName) {
        this.jxbName = jxbName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getXqid() {
        return xqid;
    }

    public void setXqid(String xqid) {
        this.xqid = xqid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getStudyNumber() {
        return studyNumber;
    }

    public void setStudyNumber(String studyNumber) {
        this.studyNumber = studyNumber;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSex() {
        return sex;
    }

    public String getCombiname() {
        return combiname;
    }

    public void setCombiname(String combiname) {
        this.combiname = combiname;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSubjectId1() {
        return subjectId1;
    }

    public void setSubjectId1(String subjectId1) {
        this.subjectId1 = subjectId1;
    }

    public String getSubjectId2() {
        return subjectId2;
    }

    public void setSubjectId2(String subjectId2) {
        this.subjectId2 = subjectId2;
    }

    public String getSubjectId3() {
        return subjectId3;
    }

    public void setSubjectId3(String subjectId3) {
        this.subjectId3 = subjectId3;
    }

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    public StudentDTO(String id, String schoolId, String xqid, String userId, String userName, String classId, String className, String gradeId, String gradeName, String studyNumber, Integer type, Integer level, Integer sex, String subjectId1, String subjectId2, String subjectId3) {
        this.id = id;
        this.schoolId = schoolId;
        this.xqid = xqid;
        this.userId = userId;
        this.userName = userName;
        this.classId = classId;
        this.className = className;
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.studyNumber = studyNumber;
        this.type = type;
        this.level = level;
        this.sex = sex;
        this.subjectId1 = subjectId1;
        this.subjectId2 = subjectId2;
        this.subjectId3 = subjectId3;
    }

    public StudentDTO(String id, String schoolId, String xqid, String userId,
                      String userName, String classId, String className, String gradeId,
                      String gradeName, String studyNumber, Integer type, Integer level,
                      Integer sex, String combiname, String subjectId1,
                      String subjectId2, String subjectId3) {
        super();
        this.id = id;
        this.schoolId = schoolId;
        this.xqid = xqid;
        this.userId = userId;
        this.userName = userName;
        this.classId = classId;
        this.className = className;
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.studyNumber = studyNumber;
        this.type = type;
        this.level = level;
        this.sex = sex;
        this.combiname = combiname;
        this.subjectId1 = subjectId1;
        this.subjectId2 = subjectId2;
        this.subjectId3 = subjectId3;
    }

}
