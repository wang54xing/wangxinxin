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
 * 自习课
 * {
 *
 *     clsrmid : classroomId 相对 教学班的执行教室而言
 *     tid : teacherId 教师
 *     gid : gradeId 年级
 *     sid : schoolId 学校ID
 *     termId : termId 学期
 *     ir
 * }
 * Created by wang_xinxin on 2018/3/7.
 */
public class N33_ZIXIKEEntry extends BaseDBObject {

    public N33_ZIXIKEEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_ZIXIKEEntry() {}


    public N33_ZIXIKEEntry(String name,ObjectId classroomId,ObjectId teacherId,ObjectId gradeId,ObjectId schoolId,ObjectId termId, List<ObjectId> studentIds) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("name",name)
                .append("clsrmId", classroomId)
                .append("tid",teacherId)
                .append("sid",schoolId)
                .append("termId",termId)
                .append("gid",gradeId)
                .append("stuIds", MongoUtils.convert(studentIds))
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }


    public String getName() {
        return getSimpleStringValue("name");
    }
    public void setName(String name) {
        setSimpleValue("name",name);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
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

    public List<ObjectId> getStudentIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("stuIds");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(((ObjectId)o));
            }
        }
        return retList;
    }

    public void setStudentIds(List<ObjectId> studentIds) {
        setSimpleValue("stuIds", MongoUtils.convert(studentIds));
    }

}
