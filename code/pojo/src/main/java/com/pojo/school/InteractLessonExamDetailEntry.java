package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
 * 互动课堂
 * <pre>
 * collectionName:interactLessonExamDetail
 * </pre>
 * <pre>
 * {
 *  lid:课堂id
 *  eid：试卷id
 *  ui:用户ID
 *  ty:1:老师考试试题，2：学生考试答案
 *  ts:第几次考试
 *  nm:试卷名称
 *  nb:考题编号
 *  ft:考题类型
 *  ftd:考题类型描述
 *  ar:考题答案type
 *  ard:考题答案
 *  cot:正确答案type（学生专用）
 *  cotd:正确答案描述（学生专用）
 *  ret:答题结果（学生专用）0：未做，1：正确，2：错误
 *  qt:考题内容（老师专用）
 *  st:是否删除
 * }
 * </pre>
 * @author guojing
 * Created on 2015/11/24.
 */
public class InteractLessonExamDetailEntry extends BaseDBObject {
    /**
     *
     */
    private static final long serialVersionUID = -6860926589325206344L;

    public InteractLessonExamDetailEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public InteractLessonExamDetailEntry(
            ObjectId lessonId,
            ObjectId examId,
            ObjectId userId,
            int type,
            int times,
            String examName,
            int number,
            String format,
            String formatDes,
            int answer,
            String answerDes,
            int correct,
            String correctDes,
            int result,
            String question
    ) {
        this(
                lessonId,
                examId,
                userId,
                type,
                times,
                examName,
                number,
                format,
                formatDes,
                answer,
                answerDes,
                correct,
                correctDes,
                result,
                question,
                DeleteState.NORMAL
        );
    }

    public InteractLessonExamDetailEntry(ObjectId lessonId,
                                         ObjectId examId,
                                         ObjectId userId,
                                         int type,
                                         int times,
                                         String examName,
                                         int number,
                                         String format,
                                         String formatDes,
                                         int answer,
                                         String answerDes,
                                         int correct,
                                         String correctDes,
                                         int result,
                                         String question,
                                         DeleteState ds) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("lid", lessonId)
                .append("eid", examId)
                .append("ui", userId)
                .append("ty", type)
                .append("ts", times)
                .append("nm", examName)
                .append("nb", number)
                .append("ft", format)
                .append("ftd", formatDes)
                .append("ar", answer)
                .append("ard", answerDes)
                .append("cot", correct)
                .append("cotd", correctDes)
                .append("ret", result)
                .append("qt", question)
                .append("st", ds.getState());
        setBaseEntry(dbo);
    }

    public ObjectId getLessonId() {
        return getSimpleObjecIDValue("lid");
    }
    public void setLessonId(ObjectId lessonId) {
        setSimpleValue("lid", lessonId);
    }

    public ObjectId getExamId() {
        return getSimpleObjecIDValue("eid");
    }
    public void setExamId(ObjectId examId) {
        setSimpleValue("eid", examId);
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

    public int getNumber() {
        return getSimpleIntegerValue("nb");
    }
    public void setNumber(int number) {
        setSimpleValue("nb", number);
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

    public int getCorrect() {
        return getSimpleIntegerValue("cot");
    }
    public void setCorrect(int correct) {
        setSimpleValue("cot", correct);
    }

    public String getCorrectDes() {
        return getSimpleStringValue("cotd");
    }
    public void setCorrectDes(String correctDes) {
        setSimpleValue("cotd", correctDes);
    }

    public int getResult() {
        return getSimpleIntegerValue("ret");
    }
    public void setResult(int result) {
        setSimpleValue("ret", result);
    }

    public String getQuestion() {
        return getSimpleStringValue("qt");
    }
    public void setQuestion(String question) {
        setSimpleValue("qt", question);
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }
    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
