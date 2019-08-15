package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
 * 互动课堂
 * <pre>
 * collectionName:interactLessonScoreClassify
 * </pre>
 * <pre>
 * {
 *  lid:课堂id
 *  ts:第几次考试
 *  en:优秀人数
 *  er:优秀率
 *  gn:良好人数
 *  gr:良好率
 *  fn:不及格人数
 *  fr:不及格率
 *  st:是否删除
 * }
 * </pre>
 * @author guojing
 * Created on 2015/11/24.
 */
public class InteractLessonScoreClassifyEntry extends BaseDBObject {
	/**
	 *
	 */
	private static final long serialVersionUID = -6860926532425206344L;

	public InteractLessonScoreClassifyEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public InteractLessonScoreClassifyEntry(
			ObjectId lessonId,
			int times,
			int excellentNum,
			int excellentRate,
			int goodNum,
			int goodRate,
			int failureNum,
			int failureRate
	) {
		this(
				lessonId,
				times,
				excellentNum,
				excellentRate,
				goodNum,
		        goodRate,
				failureNum,
				failureRate,
				DeleteState.NORMAL
		);
	}

	public InteractLessonScoreClassifyEntry(
			ObjectId lessonId,
			int times,
			int excellentNum,
			int excellentRate,
			int goodNum,
			int goodRate,
			int failureNum,
			int failureRate,
			DeleteState ds) {
		super();
		BasicDBObject dbo =new BasicDBObject()
				.append("lid", lessonId)
				.append("ts", times)
				.append("en", excellentNum)
				.append("er", excellentRate)
				.append("gn", goodNum)
				.append("gr", goodRate)
				.append("fn", failureNum)
				.append("fr", failureRate)
				.append("st", ds.getState());
		setBaseEntry(dbo);
	}

	public ObjectId getLessonId() {
		return getSimpleObjecIDValue("lid");
	}
	public void setLessonId(ObjectId lessonId) {
		setSimpleValue("lid", lessonId);
	}

	public int getTimes() {
		return getSimpleIntegerValue("ts");
	}
	public void setTimes(int times) {
		setSimpleValue("ts", times);
	}

	public int getExcellentNum() {
		return getSimpleIntegerValue("en");
	}
	public void setExcellentNum(int excellentNum) {
		setSimpleValue("en", excellentNum);
	}

	public int getExcellentRate() {
		return getSimpleIntegerValue("er");
	}
	public void setExcellentRate(int excellentRate) {
		setSimpleValue("er", excellentRate);
	}

	public int getGoodNum() {
		return getSimpleIntegerValue("gn");
	}
	public void setGoodNum(int goodNum) {
		setSimpleValue("gn", goodNum);
	}

	public int getGoodRate() {
		return getSimpleIntegerValue("gr");
	}
	public void setGoodRate(int goodRate) {
		setSimpleValue("gr", goodRate);
	}

	public int getFailureNum() {
		return getSimpleIntegerValue("fn");
	}
	public void setFailureNum(int failureNum) {
		setSimpleValue("fn", failureNum);
	}

	public int getFailureRate() {
		return getSimpleIntegerValue("fr");
	}
	public void setFailureRate(int failureRate) {
		setSimpleValue("fr", failureRate);
	}

	public int getDeleteState() {
		return getSimpleIntegerValue("st");
	}
	public void setDeleteState(int deleteState) {
		setSimpleValue("st", deleteState);
	}
}