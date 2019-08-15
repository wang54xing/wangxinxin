package com.pojo.new33.paike;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 源课表
 * {
 *     x
 *     y
 *     clsrmid : classroomId 相对 教学班的执行教室而言
 *     jxbId : jxbId 教学班
 *     isUse : 是否被排课
 *     subId : subjectId 学科
 *     tid : teacherId 教师
 *     gid : gradeId 年级
 *     sid : schoolId 学校ID
 *     termId : termId 学期
 *     ty:type  等级考 1 合格考 2 行政型 3 4 专项型 6单双周 5自习课
 *     zuHeId   该源课表排课的组合ID
 *     ir
 * }
 * Created by wang_xinxin on 2018/3/7.
 */
public class N33_YKBEntry extends BaseDBObject {

    private static final long serialVersionUID = -8469434805717122190L;

    public N33_YKBEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_YKBEntry() {}

    public N33_YKBEntry(int x, int y, ObjectId classroomId,ObjectId schoolId,ObjectId termId) {
        this(x,y,classroomId,null,0,null,null,null,schoolId,termId);
    }

    public N33_YKBEntry(int x, int y, ObjectId classroomId,ObjectId schoolId,ObjectId termId,int type) {
        this(x,y,classroomId,null,0,null,null,null,schoolId,termId,type);
    }

    public N33_YKBEntry(N33_NameKBEntry entry) {
        this(entry.getX(),entry.getY(),entry.getClassroomId(),entry.getJxbId(),entry.getIsUse(),entry.getSubjectId(),entry.getTeacherId(),entry.getGradeId(),entry.getSchoolId(),entry.getTermId());
    }

    public N33_YKBEntry(int x, int y, ObjectId classroomId,ObjectId jxbId,int isUse,ObjectId subjectId,
                         ObjectId teacherId,ObjectId gradeId,ObjectId schoolId,ObjectId termId) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("x", x)
                .append("y", y)
                .append("clsrmId", classroomId)
                .append("jxbId",jxbId)
                .append("isUse",isUse)
                .append("tid",teacherId)
                .append("subId", subjectId)
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }
    public N33_YKBEntry(int x, int y, ObjectId classroomId, ObjectId jxbId, int isUse, ObjectId subjectId, ObjectId gradeId, ObjectId schoolId, ObjectId termId,int type) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("x", x)
                .append("y", y)
                .append("clsrmId", classroomId)
                .append("jxbId",jxbId)
                .append("isUse",isUse)
                .append("subId", subjectId)
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("type",type)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public N33_YKBEntry(int x, int y, ObjectId classroomId, ObjectId jxbId, int isUse, ObjectId subjectId,
                        ObjectId teacherId, ObjectId gradeId, ObjectId schoolId, ObjectId termId,int type) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("x", x)
                .append("y", y)
                .append("clsrmId", classroomId)
                .append("jxbId",jxbId)
                .append("isUse",isUse)
                .append("tid",teacherId)
                .append("subId", subjectId)
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("type",type)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public ObjectId getZuHeId() {
        return getSimpleObjecIDValue("zuHeId");
    }
    public void setZuHeId(ObjectId zuHeId) {
        setSimpleValue("zuHeId",zuHeId);
    }

    public int getTimeCombSerial() {
        return getSimpleIntegerValue("serial");
    }
    public void setTimeCombSerial(int timeCombSerial) {
        setSimpleValue("serial",timeCombSerial);
    }

    public int getX() {
        return getSimpleIntegerValue("x");
    }
    public void setX(int x) {
        setSimpleValue("x",x);
    }

    public int getY() {
        return getSimpleIntegerValue("y");
    }
    public void setY(int y) {
        setSimpleValue("y",y);
    }

    public int getIsUse() {
        return getSimpleIntegerValue("isUse");
    }
    public void setIsUse(int isUse) {
        setSimpleValue("isUse",isUse);
    }

    public ObjectId getJxbId() {
        return getSimpleObjecIDValue("jxbId");
    }
    public void setJxbId(ObjectId jxbId) {
        setSimpleValue("jxbId",jxbId);
    }

    public ObjectId getNJxbId() {
        return getSimpleObjecIDValue("nJxbId");
    }
    public void setNJxbId(ObjectId nJxbId) {
        setSimpleValue("nJxbId",nJxbId);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subId");
    }
    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subId", subjectId);
    }

    public ObjectId getNSubjectId() {
        return getSimpleObjecIDValue("nsubId");
    }
    public void setNSubjectId(ObjectId nSubjectId) {
        setSimpleValue("nsubId", nSubjectId);
    }

    public ObjectId getClassroomId() {
        return getSimpleObjecIDValue("clsrmId");
    }
    public void setClassroomId(ObjectId classroomId) {
        setSimpleValue("clsrmId", classroomId);
    }

    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }
    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid", teacherId);
    }

    public ObjectId getNTeacherId() {
        return getSimpleObjecIDValue("ntid");
    }
    public void setNTeacherId(ObjectId nTeacherId) {
        setSimpleValue("ntid", nTeacherId);
    }

    public ObjectId getTermId() {
        return getSimpleObjecIDValue("termId");
    }
    public void setTermId(ObjectId termId) {
        setSimpleValue("termId", termId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }
    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public Integer getType(){
        return getSimpleIntegerValueDef("type",0);
    }
    public void setType(Integer type){
        setSimpleValue("type",type);
    }

}
