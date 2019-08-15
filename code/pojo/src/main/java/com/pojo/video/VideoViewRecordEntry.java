package com.pojo.video;

import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;

/**
 * 视频观看记录表
 * <pre>
 * collectionName:videorecords
 * </pre>
 * <pre>
 * {
	     vd{
	         id:视频ID
	         v:视频名称
	       }
	     ui{
	        id:
	        v:用户名字
	       }
	     cl{,非学生用户无
	         id:
	         v:主班级名称
	        } 
	     st:状态 0:已点击 1：已经看完
	     vi:语音地址,通常为null
	     {
	          id:
	          v：地址
	     }
         cou:1 保留
         ti:观看时间
 *   }
 * </pre>
 * @author fourer
 */
public class VideoViewRecordEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1093254010528207003L;
	
	
	public VideoViewRecordEntry(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}


	public VideoViewRecordEntry(IdValuePair videoInfo, IdValuePair userInfo,
			IdValuePair classInfo, int state, IdValuePair voiceInfo,int count,long lastTime) {
		super();
        BasicDBObject vi = null;
        if(voiceInfo != null){
            vi = voiceInfo.getBaseEntry();
        }
        BasicDBObject ci = null;
        if(classInfo!=null){
            ci = classInfo.getBaseEntry();
        }
		BasicDBObject baseEntry =new BasicDBObject().append("vd", videoInfo.getBaseEntry())
				.append("ui", userInfo.getBaseEntry())
				.append("cl", ci)
				.append("st", state)
				.append("vi", vi)
				.append("cou", count)
                .append("ti",lastTime);
				;
		setBaseEntry(baseEntry);
	}
    public long getLastViewTime() {return getSimpleLongValue("ti");}
    public void setLastViewTime(long time){setSimpleValue("ti",time);}
	
	public int getCount() {
		return getSimpleIntegerValue("cou");
	}

	public void setCount(int count) {
		setSimpleValue("cou", count);
	}

	public IdValuePair getVideoInfo() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("vd");
		if(null!=dbo)
			return new IdValuePair(dbo);
		return null;
	}
	public void setVideoInfo(IdValuePair videoInfo) {
		setSimpleValue("vd", videoInfo.getBaseEntry());
	}
	public IdValuePair getUserInfo() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("ui");
		if(null!=dbo)
			return new IdValuePair(dbo);
		return null;
	}
	public void setUserInfo(IdValuePair studentInfo) {
		setSimpleValue("ui", studentInfo.getBaseEntry());
	}
	public IdValuePair getClassInfo() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("cl");
		if(null!=dbo)
			return new IdValuePair(dbo);
		return null;
	}
	public void setClassInfo(IdValuePair classInfo) {
		setSimpleValue("cl", classInfo.getBaseEntry());
	}
	public int getState() {
		return getSimpleIntegerValue("st");
	}
	public void setState(int state) {
		setSimpleValue("st", state);
	}
	public IdValuePair getVoiceInfo() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("vi");
		if(null!=dbo)
			return new IdValuePair(dbo);
		return null;
	}
	public void setVoiceInfo(IdValuePair voiceInfo) {
		setSimpleValue("vi", voiceInfo.getBaseEntry());
	}
	
}
