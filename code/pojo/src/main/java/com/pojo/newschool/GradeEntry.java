package com.pojo.newschool;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * 年级信息
 * <pre>
 * collectionName:newgrades
 * </pre>
 * <pre>
 * {
 *  sid:学校ID schoolId
 *  dgnm:默认年级名称 defaultGradeName
 *  gnm:年级名称 gradeName
 *  ty:类型 type
 *  clscnt:班级数 classCount
 *  int:简介 introduce
 *  ad:入学时间 admissionDate
 *  crid:创建人id createrId
 *  crt:创建时间 createDate
 *  ir:是否删除 0没有删除 1已经删除
 * }
 * </pre>
 * Created by guojing on 2016/12/29.
 */
public class GradeEntry extends BaseDBObject {
    private static final long serialVersionUID = 6485398291488814841L;

    public GradeEntry() {

    }

    public GradeEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public GradeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public GradeEntry(
            ObjectId schoolId,
            String defaultGradeName,
            String gradeName,
            int gradeNumber,
            String type,
            String introduce,
            long admissionDate,
            ObjectId createrId,
            int classCount
    ) {
        super();
        BasicDBObject dbo =new BasicDBObject();
        dbo.append("sid", schoolId)
        .append("dgnm", defaultGradeName)
        .append("gnm", gradeName)
        .append("ty", type)
        .append("clscnt",classCount)
        .append("int", introduce)
        .append("ad", admissionDate);
        dbo.append("gnr", gradeNumber)
        .append("crid", createrId)
        .append("crt", new Date().getTime())
        .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public GradeEntry(
            ObjectId schoolId,
            String defaultGradeName,
            int gradeNumber,
            String type,
            ObjectId createrId
    ) {
        super();
        BasicDBObject dbo =new BasicDBObject();
                dbo.append("sid", schoolId)
                .append("dgnm", defaultGradeName)
                .append("gnm", "")
                .append("ty", type)
                .append("int", "")
                .append("ad", 0l)
                .append("gnr", gradeNumber)
                .append("crid", createrId)
                .append("crt", new Date().getTime())
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");

    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public String getDefaultGradeName() {
        return getSimpleStringValueDef("dgnm","");
    }

    public void setDefaultGradeName(String gradeName) {
        setSimpleValue("dgnm", gradeName);
    }

    public String getGradeName() {
        return getSimpleStringValueDef("gnm","");
    }

    public void setGradeName(String gradeName) {
        setSimpleValue("gnm", gradeName);
    }

    public int getGradeNumber() {
        return getSimpleIntegerValue("gnr");
    }

    public void setGradeNumber(int gradeNumber) {
        setSimpleValue("gnr", gradeNumber);
    }

    public String getType() {
        return getSimpleStringValue("ty");
    }

    public void setType(String type) {
        setSimpleValue("ty", type);
    }

    public String getIntroduce() {
        return getSimpleStringValueDef("int","");
    }

    public void setIntroduce(String introduce) {
        setSimpleValue("int", introduce);
    }

    public long getAdmissionDate() {
        return getSimpleLongValueDef("ad",0l);
    }

    public void setAdmissionDate(long admissionDate) {
        setSimpleValue("ad", admissionDate);
    }

    public ObjectId getCreaterId() {
        return getSimpleObjecIDValue("crid");
    }

    public void setCreaterId(ObjectId createrId) {
        setSimpleValue("crid",createrId);
    }

    public long getCreateDate() {
        return getSimpleLongValueDef("crt",0l);
    }

    public void setCreateDate(long createDate) {
        setSimpleValue("crt", createDate);
    }

    //默认没有删除
    public int getIsRemove() {
        return getSimpleIntegerValueDef("ir", Constant.ZERO);
    }

    public void setIsRemove(int isRemove) {
        setSimpleValue("ir", isRemove);
    }

    public int getClassCount() {
        return getSimpleIntegerValue("clscnt");
    }

    public void setClassCount(int classCount) {
        setSimpleValue("clscnt",classCount);
    }

}
