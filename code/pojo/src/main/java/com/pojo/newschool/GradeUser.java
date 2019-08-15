package com.pojo.newschool;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2017/2/7.
 */
public class GradeUser extends BaseDBObject {

    private static final long serialVersionUID = 2663655553432092055L;

    public GradeUser() {

    }

    public GradeUser(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public GradeUser(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public GradeUser(ObjectId gradeId,ObjectId userId,ObjectId subjectId) {
        super();
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("gid",gradeId)
        .append("uid", userId)
        .append("suid", subjectId)
        .append("top", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }
    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid",gradeId);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("uid",userId);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("suid");
    }
    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("suid",subjectId);
    }

    public int getTop() {
        return getSimpleIntegerValue("top");
    }
    public void setTop(int top) {
        setSimpleValue("top",top);
    }
}
