package com.pojo.school;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 互动课堂组内互评得分表
 * <pre>
 * collectionName:interactLessonGroupScore
 * </pre>
 * <pre>
 * {
 * 	lid : 课堂id
 *  uid : 学生id
 *  gpsc : 互评得分
 *  teasc : 老师打分
 *  tsc : 总分
 *  times : 次数
 *  ir : 是否删除
 * }
 * </pre>
 * added by xusy 2017年12月1日 下午1:55:45
 */
public class InteractLessonGroupScoreEntry extends BaseDBObject {

	private static final long serialVersionUID = -353837193097319635L;

	public InteractLessonGroupScoreEntry(BasicDBObject entry) {
		super(entry);
	}
	
	public InteractLessonGroupScoreEntry(
			ObjectId lessonId,
			ObjectId userId,
			double groupScore,
			double teacherScore,
			double totalScore,
			int times) {
		BasicDBObject entry = new BasicDBObject()
				.append("lid", lessonId)
				.append("uid", userId)
				.append("gpsc", groupScore)
				.append("teasc", teacherScore)
				.append("tsc", totalScore)
				.append("times", times)
				.append("ir", Constant.ZERO);
		setBaseEntry(entry);		
	}
	
	public ObjectId getLessonId() {
		return getSimpleObjecIDValue("lid");
	}
	
	public void setLessonId(ObjectId lessonId) {
		setSimpleValue("lid", lessonId);
	}
	
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("uid");
	}
	
	public void setUserId(ObjectId userId) {
		setSimpleValue("uid", userId);
	}
	
	public Double getGroupScore() {
		return getSimpleDoubleValueDef("gpsc", 0.0D);
	}
	
	public void setGroupScore(Double groupScore) {
		setSimpleValue("gpsc", groupScore);
	}
	
	public Double getTeacherScore() {
		return getSimpleDoubleValueDef("teasc", 0.0D);
	}
	
	public void setTeacherScore(Double teacherScore) {
		setSimpleValue("teasc", teacherScore);
	}
	
	public Double getTotalScore() {
		return getSimpleDoubleValueDef("tsc", 0.0D);
	}
	
	public void setTotalScore(Double totalScore) {
		setSimpleValue("tsc", totalScore);
	}
	
	public int getTimes() {
		return getSimpleIntegerValue("times");
	}
	
	public void setTimes(int times) {
		setSimpleValue("times", times);
	}
	
}
