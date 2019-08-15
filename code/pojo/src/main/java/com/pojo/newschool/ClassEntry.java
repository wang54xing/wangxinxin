package com.pojo.newschool;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 班级信息
 * <pre>
 * collectionName: newclasses
 * </pre>
 * <pre>
 * {
 *  nm:名称 name
 *  mid:班主任ID masterId
 *  sid:学校ID schoolId
 *  gid:年级ID gradeId
 *  cs:班级人数 classSize
 *  int:班级口号 introduce
 *  ad:入学时间 admissionDate
 *  cnr:班级序号 classNumber
 *  crid:创建人id createrId
 *  crt:创建时间 createDate
 *  suids:学生列表
 *  ir:是否删除 0没有删除 1已经删除
 * }
 * </pre>
 * Created by guojing on 2016/12/29.
 */
public class ClassEntry extends BaseDBObject {
    private static final long serialVersionUID = 6485398291488814841L;

    public ClassEntry() {

    }

    public ClassEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public ClassEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ClassEntry(
            String name,
            ObjectId masterId,
            ObjectId schoolId,
            ObjectId gradeId,
            int classSize,
            String introduce,
            long admissionDate,
            int classNumber,
            ObjectId createrId
            , List<ObjectId> userList
    ) {
        super();
        BasicDBObject dbo =new BasicDBObject();
        dbo.append("nm", name)
        .append("mid", masterId)
        .append("sid", schoolId)
        .append("gid", gradeId)
        .append("cs", classSize)
        .append("int", introduce)
        .append("ad", admissionDate)
        .append("suids", MongoUtils.convert(userList))
        .append("cnr", classNumber);
            dbo.append("crid", createrId)
            .append("crt", new Date().getTime())
            .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public String getName() {
        return getSimpleStringValueDef("nm","");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public ObjectId getMasterId() {
        return getSimpleObjecIDValue("mid");
    }

    public void setMasterId(ObjectId masterId) {
        setSimpleValue("mid", masterId);
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

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public int getClassSize() {
        return getSimpleIntegerValueDef("cs",0);
    }

    public void setClassSize(int classSize) {
        setSimpleValue("cs", classSize);
    }

    public String getIntroduce() {
        return getSimpleStringValueDef("int","");
    }

    public void setIntroduce(String introduce) {
        setSimpleValue("int", introduce);
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

    public long getAdmissionDate() {
        return getSimpleLongValueDef("ad",0l);
    }

    public void setAdmissionDate(long admissionDate) {
        setSimpleValue("ad", admissionDate);
    }

    public int getClassNumber() {
        return getSimpleIntegerValue("cnr");
    }

    public void setClassNumber(int classNumber) {
        setSimpleValue("cnr", classNumber);
    }
    //默认没有删除
    public int getIsRemove() {
        return getSimpleIntegerValueDef("ir", Constant.ZERO);
    }

    public void setIsRemove(int isRemove) {
        setSimpleValue("ir", isRemove);
    }

    public List<ObjectId> getUserList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("suids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(((ObjectId)o));
            }
        }
        return retList;
    }

    public void setUserList(List<ObjectId> userList) {
        setSimpleValue("suids", MongoUtils.convert(userList));
    }
}
