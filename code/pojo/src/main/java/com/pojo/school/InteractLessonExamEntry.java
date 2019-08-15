package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
 * 互动课堂
 * <pre>
 * collectionName:interactLessonExam
 * </pre>
 * <pre>
 * {
 *  lid:课堂id
 *  ui:用户ID
 *  ty:1:老师考试试题，2：学生考试答案
 *  ts:第几次考试
 *  nm:试卷名称
 *  cr:正确率（学生专用）
 *  ut:答题时间（学生专用）
 *  st:是否删除
 * }
 * </pre>
 * @author guojing
 * Created on 2015/11/24.
 */
public class InteractLessonExamEntry extends BaseDBObject {
    /**
     *
     */
    private static final long serialVersionUID = -6860926589325206344L;

    public InteractLessonExamEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public InteractLessonExamEntry(
            ObjectId lessonId,
            ObjectId userId,
            int type,
            int times,
            String examName
    ) {
        this(
                lessonId,
                userId,
                type,
                times,
                examName,
                0,
                "",
                DeleteState.NORMAL
        );
    }

    public InteractLessonExamEntry(
            ObjectId lessonId,
            ObjectId userId,
            int type,
            int times,
            String examName,
            int correctRate,
            String useTime
    ) {
        this(
                lessonId,
                userId,
                type,
                times,
                examName,
                correctRate,
                useTime,
                DeleteState.NORMAL
        );
    }

    public InteractLessonExamEntry(ObjectId lessonId,
                                   ObjectId userId,
                                   int type,
                                   int times,
                                   String examName,
                                   int correctRate,
                                   String useTime,
                                   DeleteState ds) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("lid", lessonId)
                .append("ui", userId)
                .append("ty", type)
                .append("ts", times)
                .append("nm", examName)
                .append("cr", correctRate)
                .append("ut", useTime)
                .append("st", ds.getState());
        setBaseEntry(dbo);
    }

    public ObjectId getLessonId() {
        return getSimpleObjecIDValue("lid");
    }
    public void setLessonId(ObjectId lessonId) {
        setSimpleValue("lid", lessonId);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }
    public void setType(int type) {
        setSimpleValue("ty", type);
    }

    public int getTimes() {
        return getSimpleIntegerValue("ts");
    }
    public void setTimes(int times) {
        setSimpleValue("ts", times);
    }

    public String getExamName() {
        return getSimpleStringValue("nm");
    }
    public void setExamName(String examName) {
        setSimpleValue("nm", examName);
    }

    public int getCorrectRate() {
        return getSimpleIntegerValue("cr");
    }
    public void setCorrectRate(int correctRate) {
        setSimpleValue("cr", correctRate);
    }

    public String getUseTime() {
        return getSimpleStringValue("ut");
    }
    public void setUseTime(String useTime) {
        setSimpleValue("ut", useTime);
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }
    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
