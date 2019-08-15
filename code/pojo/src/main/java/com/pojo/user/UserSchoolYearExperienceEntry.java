package com.pojo.user;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * 用户积分日志
 *
 * <pre>
 * collectionName:uexperlogs
 * </pre>
 *
 * <pre>
 * {
 *  ui:用户ID
 *  si:学校ID
 *  gid:年级ID
 *  cid:班级ID
 *  sye:用户学年积分
 *  upt:修改时间
 * </pre>
 * Created by guojing on 2015/7/22.
 */
public class UserSchoolYearExperienceEntry extends BaseDBObject {
    /**
     *
     */
    private static final long serialVersionUID = 5323687213553943057L;
    public UserSchoolYearExperienceEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public UserSchoolYearExperienceEntry(ObjectId userId,
                                         ObjectId schoolId,
                                         ObjectId gradeId,
                                         ObjectId classId,
                                         int experience) {
        super();

        BasicDBObject baseEntry = new BasicDBObject()
                .append("ui", userId)
                .append("si", schoolId)
                .append("gid", gradeId)
                .append("cid", classId)
                .append("sye", experience)
                .append("upt", new Date().getTime());
        setBaseEntry(baseEntry);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("cid");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("cid", classId);
    }

    public int getSchoolYearExperience() {
        return getSimpleIntegerValue("sye");
    }

    public void setSchoolYearExperience(int schoolYearExperience) {
        setSimpleValue("sye", schoolYearExperience);
    }

    public long getUpdateTime() {
        return getSimpleLongValue("upt");
    }

    public void setUpdateTime(long updateTime) {
        setSimpleValue("upt",updateTime);
    }
}
