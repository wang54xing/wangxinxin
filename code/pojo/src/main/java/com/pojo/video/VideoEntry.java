package com.pojo.video;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 视频信息
 * <pre>
 * collectionName:videos
 * </pre>
 * <pre>
 * {
 *  nm:名字
 *  ud:更新日期
 *  lng:长度
 *  iurl:图片地址
 *  us:上传状态 0:正在上传 1：上传成功 ,2:上传失败 , 实际对应转码状态，0为未转码，1为转码成功
 *  df：删除标记
 *  tn：临时名字
 *  bk:七牛存储Key
 *  
 *  
 *  pid:七牛persistentId
 *  vn:观看次数
 *  vsty:视频来源类型; 详见VideoSourceType
 *  vt：视频type
 *  
 *
 * }
 * </pre>
 * @author fourer
 */
public class VideoEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5910922876897656754L;

	
	
	public VideoEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public VideoEntry(String name, long length, int videoSourceType,String bk) {
		this(name, //name
				System.currentTimeMillis(),//updateDate
				length,//length
				Constant.EMPTY, //imgUrl
				0, //viewNumber
				1,//updateState todo 测试需要，设为上传成功，正式应设为0
				Constant.NEGATIVE_ONE,//videoType
				Constant.ZERO, //deleteFlag
				Constant.EMPTY, //temporaryName
				bk,//bucketkey
				Constant.EMPTY,//persistentId
				videoSourceType//videoSourceType
				);
		
	}
	
	public VideoEntry(String name, long updateDate, long length, String imgUrl,int viewNumber, int updateState, int videoType,
			int deleteFlag, String temporaryName,
			String bucketkey, String persistentId, int videoSourceType) {
		super();
		

		BasicDBObject baseEntry =new BasicDBObject()
		.append("nm", name)
		.append("ud", updateDate)
		.append("lng", length)
		.append("iurl", imgUrl)
		.append("vn", viewNumber)
		.append("us", updateState)
		.append("vt", videoType)
		.append("df", deleteFlag)
		.append("tn", temporaryName)
		.append("bk", bucketkey)
		.append("pid", persistentId)
		.append("vsty", videoSourceType);//详见VideoSourceType

		setBaseEntry(baseEntry);

	}

	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public long getUpdateDate() {
		return getSimpleLongValue("ud");
	}
	public void setUpdateDate(long updateDate) {
		setSimpleValue("ud", updateDate);
	}


	public long getVideLong() {
		return getSimpleLongValueDef("sc",10000L);
	}
	public void setVideLong(long sc) {
		setSimpleValue("sc", sc);
	}
	public int getThumbUp() {
		return getSimpleIntegerValue("thu");
	}
	public void setThumbUp(int thumbUp) {
		setSimpleValue("thu", thumbUp);
	}
	public String getThumbDown() {
		return getSimpleStringValue("thd");
	}
	public void setThumbDown(String thumbDown) {
		setSimpleValue("thd", thumbDown);
	}
	
	public long getLength() {
		return getSimpleLongValue("lng");
	}
	public void setLength(long length) {
		setSimpleValue("lng", length);
	}
	public String getImgUrl() {
		return getSimpleStringValue("iurl");
	}
	public void setImgUrl(String imgUrl) {
		setSimpleValue("iurl", imgUrl);
	}
	public int getViewNumber() {
		return getSimpleIntegerValue("vn");
	}
	public void setViewNumber(int viewNumber) {
		setSimpleValue("vn", viewNumber);
	}
	public int getUpdateState() {
		return getSimpleIntegerValue("us");
	}
	public void setUpdateState(int updateState) {
		setSimpleValue("us", updateState);
	}
	

	public int getVideoType() {
		return getSimpleIntegerValue("vt");
	}
	public void setVideoType(int videoType) {
		setSimpleValue("vt", videoType);
	}
	
	
	public int getDeleteFlag() {
		return getSimpleIntegerValue("df");
	}
	public void setDeleteFlag(int deleteFlag) {
		setSimpleValue("df", deleteFlag);
	}
	public String getTemporaryName() {
		return getSimpleStringValue("tn");
	}
	public void setTemporaryName(String temporaryName) {
		setSimpleValue("tn", temporaryName);
	}

	public String getBucketkey() {
		return getSimpleStringValue("bk");
	}
	public void setBucketkey(String bucketkey) {
		setSimpleValue("bk", bucketkey);
	}
	public String getPersistentId() {
		return getSimpleStringValue("pid");
	}
	public void setPersistentId(String persistentId) {
		setSimpleValue("pid", persistentId);
	}
	public int getVideoSourceType() {
		return getSimpleIntegerValue("vsty");
	}
	public void setVideoSourceType(int videoSourceType) {
		setSimpleValue("vsty", videoSourceType);
	}

	
}
