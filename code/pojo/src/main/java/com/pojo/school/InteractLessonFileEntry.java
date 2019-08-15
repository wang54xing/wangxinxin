package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * 互动课堂
 * <pre>
 * collectionName:interactLessonFile
 * </pre>
 * <pre>
 * {
 *  lid:课堂id
 *  ui:用户ID
 *  ur:用户角色
 *  ty:1:老师上传课件，2：老师上传考试试题，3：学生上传课件，4：学生上传考试答案
 *  ts:第几次上传
 *  fn:文件名称
 *  isv:是否是视频 Y:是，N：否
 *  vi:视频ID(isv=Y)
 *  ph:文件路径(isv=N)
 *  ct:创建时间,long
 *  st:是否删除
 * }
 * </pre>
 * @author guojing
 * Created on 2015/11/23.
 */

public class InteractLessonFileEntry extends BaseDBObject {

    /**
     *
     */
    private static final long serialVersionUID = -6860929654325206344L;

    public InteractLessonFileEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public InteractLessonFileEntry(
            ObjectId lessonId,
            ObjectId userId,
            int role,
            int type,
            int times,
            String fileName,
            String isVideo,
            ObjectId videoId,
            String filePath
    ) {
        this(
                lessonId,
                userId,
                role,
                type,
                times,
                fileName,
                isVideo,
                videoId,
                filePath,
                new Date().getTime(),
                DeleteState.NORMAL
        );
    }

    public InteractLessonFileEntry(ObjectId lessonId,
                                   ObjectId userId,
                                   int role,
                                   int type,
                                   int times,
                                   String fileName,
                                   String isVideo,
                                   ObjectId videoId,
                                   String filePath,
                                   long createTime,
                                   DeleteState ds) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("lid", lessonId)
                .append("ui", userId)
                .append("ur", role)
                .append("ty", type)
                .append("ts", times)
                .append("fn", fileName)
                .append("isv", isVideo)
                .append("vi", videoId)
                .append("ph", filePath)
                .append("ct", createTime)
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

    public int getUserRole() {
        return getSimpleIntegerValue("ur");
    }
    public void setUserRole(int userRole) {
        setSimpleValue("ur", userRole);
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

    public String getFileName() {
        return getSimpleStringValue("fn");
    }
    public void setFileName(String fileName) {
        setSimpleValue("fn", fileName);
    }

    public String getIsVideo() {
        return getSimpleStringValue("isv");
    }
    public void setIsVideo(String isVideo) {
        setSimpleValue("isv", isVideo);
    }

    public ObjectId getVideoId() {
        return getSimpleObjecIDValue("vi");
    }
    public void setVideoId(ObjectId videoId) {
        setSimpleValue("vi", videoId);
    }

    public String getFilePath() {
        return getSimpleStringValue("ph");
    }
    public void setFilePath(String filePath) {
        setSimpleValue("ph", filePath);
    }

    public long getCreateTime() {
        return getSimpleLongValue("ct");
    }
    public void setCreateTime(long createTime) {
        setSimpleValue("ct",createTime);
    }

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }
    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
