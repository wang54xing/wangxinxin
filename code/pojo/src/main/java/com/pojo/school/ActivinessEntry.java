package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
 * Created by guojing on 2015/11/19.
 */

/**互动课堂-活跃度
 * <pre>
 * collectionName:activiness
 * </pre>
 * lid:课堂id
 * stid:学生id
 * ty:活跃类型//1:学生登录，2：学生上传，3：学生考试，4：学生抢答（举手），5：快速答题（回答问题），6：学生抢答（抢到）
 * an:活跃积分
 * cn:活跃次数
 * st:是否删除
 * Created by fl on 2015/7/23.
 */
public class ActivinessEntry extends BaseDBObject {
    /**
     *
     */
    private static final long serialVersionUID = -6860924985625206344L;

    public ActivinessEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ActivinessEntry(ObjectId lessonId, ObjectId studentId,
                           int type, int activiness, int count
    ) {
        this(
                lessonId,
                studentId,
                type,
                activiness,
                count,
                DeleteState.NORMAL
        );
    }

    public ActivinessEntry(ObjectId lessonId, ObjectId studentId, int type, int activiness, int count, DeleteState ds) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("lid", lessonId)
                .append("stid", studentId)
                .append("ty", type)
                .append("an", activiness)
                .append("cn", count)
                .append("st", ds.getState());
        setBaseEntry(dbo);
    }

    public ObjectId getLessonId() {
        return getSimpleObjecIDValue("lid");
    }
    public void setLessonId(ObjectId lessonId) {
        setSimpleValue("lid", lessonId);
    }

    public ObjectId getStudentId() {
        return getSimpleObjecIDValue("stid");
    }
    public void setStudentId(ObjectId studentId) {
        setSimpleValue("stid", studentId);
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }
    public void setType(int type) {
        setSimpleValue("ty", type);
    }

    public int getActiviness() {
        return getSimpleIntegerValue("an");
    }
    public void setActiviness(int activiness) {
        setSimpleValue("an", activiness);
    }

    public int getCount() {
        return getSimpleIntegerValue("cn");
    }
    public void setCount(int count) {
        setSimpleValue("cn", count);
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }
    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
