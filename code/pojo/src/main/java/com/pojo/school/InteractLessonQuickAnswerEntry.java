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
 *  ts:第几次
 *  ft:考题类型
 *  ftd:考题类型描述
 *  ar:考题答案type
 *  ard:考题答案
 *  ut:答题用时
 *  st:是否删除
 * }
 * </pre>
 * @author guojing
 * Created on 2015/11/26.
 */
public class InteractLessonQuickAnswerEntry extends BaseDBObject {
    /**
     *
     */
    private static final long serialVersionUID = -6860963589325206344L;

    public InteractLessonQuickAnswerEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public InteractLessonQuickAnswerEntry(
            ObjectId lessonId,
            ObjectId userId,
            int type,
            int times,
            String format,
            String formatDes,
            int answer,
            String answerDes,
            String useTime
    ) {
        this(
                lessonId,
                userId,
                type,
                times,
                format,
                formatDes,
                answer,
                answerDes,
                useTime,
                DeleteState.NORMAL
        );
    }

    public InteractLessonQuickAnswerEntry(ObjectId lessonId,
                                   ObjectId userId,
                                   int type,
                                   int times,
                                   String format,
                                   String formatDes,
                                   int answer,
                                   String answerDes,
                                   String useTime,
                                   DeleteState ds) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("lid", lessonId)
                .append("ui", userId)
                .append("ty", type)
                .append("ts", times)
                .append("ft", format)
                .append("ftd", formatDes)
                .append("ar", answer)
                .append("ard", answerDes)
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

    public String getFormat() {
        return getSimpleStringValue("ft");
    }
    public void setFormat(String format) {
        setSimpleValue("ft", format);
    }

    public String getFormatDes() {
        return getSimpleStringValue("ftd");
    }
    public void setFormatDes(String formatDes) {
        setSimpleValue("ftd", formatDes);
    }

    public int getAnswer() {
        return getSimpleIntegerValue("ar");
    }
    public void setAnswer(int answer) {
        setSimpleValue("ar", answer);
    }

    public String getAnswerDes() {
        return getSimpleStringValue("ard");
    }
    public void setAnswerDes(String answerDes) {
        setSimpleValue("ard", answerDes);
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
