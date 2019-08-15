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
 * 教学班
 * {
 * nm: name 系统分配的教学班名称
 * nnm: nickName 用户自定义教学班名称
 * subId : subjectId 教学班关联的学科
 * clsrmId : classRoomId预定的教学班教学的执行教师/场地，教学实际发生地
 * tid : teacherId  预定的教学班的授课教师
 * stuIds : studentIds 教学班包含的一组学生
 * sid : schoolId 学校Id
 * termId : termId 学期ID
 * gid : gradeId 年级ID
 * tp : type 等级考 1 合格考 2 行政型 3 4 专项型 6单双周 5自习课
 * tags:标签list
 * rl
 * view
 * ir :是否删除
 * 单双周教学班使用
 * dos 单双周  0不是  1单周 2双周
 * rjid:关联的教学班id
 * jxbks
 * 专项课使用
 * cisList 班級ids
 * }
 * Created by wang_xinxin on 2018/3/7.
 */
public class N33_JXB_TempEntry extends BaseDBObject {

    private static final long serialVersionUID = -1747560486287585910L;

    public N33_JXB_TempEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_JXB_TempEntry() {
    }

    public N33_JXB_TempEntry(String name, String nickName, ObjectId subjectId, ObjectId classroomId, ObjectId tercherId, List<ObjectId> studentIds, ObjectId schoolId,
                             ObjectId termId, int index, ObjectId gradeId, int type, List<ObjectId> tagIds, Integer rl, Integer view) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("nm", name)
                .append("nnm", nickName)
                .append("subId", subjectId)
                .append("clsrmId", classroomId)
                .append("tid", tercherId)
                .append("stuIds", MongoUtils.convert(studentIds))
                .append("sid", schoolId)
                .append("termId", termId)
                .append("gid", gradeId)
                .append("idx", index)
                .append("tp", type)
                .append("jxbks", 0)
                .append("tags", MongoUtils.convert(tagIds))
                .append("rl", rl)
                .append("ir", Constant.ZERO)
                .append("view", view);
        setBaseEntry(dbo);
    }

    public N33_JXB_TempEntry(String name, ObjectId classroomId, ObjectId tercherId, List<ObjectId> studentIds, ObjectId schoolId,
                             ObjectId termId, ObjectId gradeId, int type) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("nm", name)
                .append("clsrmId", classroomId)
                .append("tid", tercherId)
                .append("stuIds", MongoUtils.convert(studentIds))
                .append("sid", schoolId)
                .append("termId", termId)
                .append("gid", gradeId)
                .append("tp", type)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public int getView() {
        return getSimpleIntegerValueDef("view", 0);
    }

    public void setView(int view) {
        setSimpleValue("view", view);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }


    public ObjectId getParentId() {
        return getSimpleObjecIDValue("parentId");
    }

    public void setParentId(ObjectId parentId) {
        setSimpleValue("parentId", parentId);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getNickName() {
        return getSimpleStringValue("nnm");
    }

    public void setNickName(String nickName) {
        setSimpleValue("nnm", nickName);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subId");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subId", subjectId);
    }

    public ObjectId getClassroomId() {
        return getSimpleObjecIDValue("clsrmId");
    }

    public void setClassroomId(ObjectId classroomId) {
        setSimpleValue("clsrmId", classroomId);
    }

    public ObjectId getTercherId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTercherId(ObjectId tercherId) {
        setSimpleValue("tid", tercherId);
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

    public int getIndex() {
        return getSimpleIntegerValue("idx");
    }

    public void setIndex(int index) {
        setSimpleValue("idx", index);
    }

    public int getRongLiang() {
        return getSimpleIntegerValueDef("rl", 0);
    }

    public void setRongLiang(int rl) {
        setSimpleValue("rl", rl);
    }

    public int getType() {
        return getSimpleIntegerValue("tp");
    }

    public void setType(int type) {
        setSimpleValue("tp", type);
    }

    public List<ObjectId> getStudentIds() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("stuIds");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setStudentIds(List<ObjectId> studentIds) {
        setSimpleValue("stuIds", MongoUtils.convert(studentIds));
    }

    public List<ObjectId> getTagIds() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("tags");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setIr(Integer ir) {
        setSimpleValue("ir", ir);
    }

    public int getIr() {
        return getSimpleIntegerValueDef("ir", 0);
    }


    public void setJXBKS(Integer ir) {
        setSimpleValue("jxbks", ir);
    }

    public int getJXBKS() {
        return getSimpleIntegerValueDef("jxbks", 0);
    }

    public void setTagIds(List<ObjectId> tagIds) {
        setSimpleValue("tags", MongoUtils.convert(tagIds));
    }

    public int getDanOrShuang() {
        return getSimpleIntegerValueDef("dos", 0);
    }

    public void setDanOrShuang(Integer danOrShuang) {
        setSimpleValue("dos", danOrShuang);
    }

    public ObjectId getRelativeId() {
        return getSimpleObjecIDValue("rjid");
    }

    public void setRelativeId(ObjectId relativeId) {
        setSimpleValue("rjid", relativeId);
    }


    public List<ObjectId> getClassIds() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("cls");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setClassIds(List<ObjectId> classIds) {
        setSimpleValue("cls", MongoUtils.convert(classIds));
    }

    @Override
    public int hashCode() {
        return getID().toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj.getClass().equals(getClass()))) {
            N33_JXB_TempEntry n33_jxbEntry = (N33_JXB_TempEntry) obj;
            if (getID().toString().equals(n33_jxbEntry.getID().toString())) {
                return true;
            }
        }
        return false;
    }
}
