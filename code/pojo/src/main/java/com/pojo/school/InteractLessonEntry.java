package com.pojo.school;

import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

import java.util.Date;

/**
 * 互动课堂
 * <pre>
 * collectionName:interactLesson
 * </pre>
 * <pre>
 * {
 *  ui:用户ID
 *  si:学校ID
 *  cid:班级ID
 *  tcsid:班级课程ID
 *  vn:视频名称
 *  vi:视频ID
 *  pu:是否推送  0么有 1已经推动
 *  lock:锁
 *  ct:创建时间,long
 *  st:是否删除
 * }
 * </pre>
 * @author fourer
 */
public class InteractLessonEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6860924909925206344L;

	public InteractLessonEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public InteractLessonEntry(
			ObjectId userId, ObjectId schoolId,
			ObjectId classId, ObjectId subjectId,
			ObjectId videoId,String lessonName
	) {
		this(
				userId,
				schoolId,
				classId,
				subjectId,
				videoId,
				new Date().getTime(),
				DeleteState.NORMAL,
				lessonName
		);
	}

	public InteractLessonEntry(ObjectId userId, ObjectId schoolId, ObjectId classId,
			   ObjectId subjectId, ObjectId videoId,long createTime, DeleteState ds,String lessonName) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("ui", userId)
		.append("si", schoolId)
		.append("cid", classId)
		.append("tcsid", subjectId)
		.append("vn", lessonName)
		.append("vi", videoId)
		.append("pu", Constant.ZERO)
		.append("lock", Constant.ZERO)
		.append("ct", createTime)
		.append("st", ds.getState());
		setBaseEntry(dbo);
	}
	
	
	public int getPush() {
		if(getBaseEntry().containsField("pu"))
		{
			return getSimpleIntegerValue("pu");
		}
		return Constant.ZERO;
	}

	public void setPush(int push) {
		setSimpleValue("pu", push);
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
	public ObjectId getClassId() {
		return getSimpleObjecIDValue("cid");
	}
	public void setClassId(ObjectId classId) {
		setSimpleValue("cid", classId);
	}

	public ObjectId getSubjectId() {
		return getSimpleObjecIDValue("tcsid");
	}
	public void setSubjectId(ObjectId subjectId) {
		setSimpleValue("tcsid", subjectId);
	}

	public ObjectId getVideoId() {
		return getSimpleObjecIDValue("vi");
	}
	public void setVideoId(ObjectId videoId) {
		setSimpleValue("vi", videoId);
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

	public int getLock() {
		return getSimpleIntegerValue("lock");
	}

	public void setLock(int lock) {
		setSimpleValue("lock",lock);
	}

	public String getLessonName() {
		return getSimpleStringValue("vn");
	}

	public void setLessonName(String lessonName) {
		setSimpleValue("vn",lessonName);
	}
}
