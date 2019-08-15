package com.pojo.video;

/**
 * 视频类型
 * @author fourer
 *
 */
public enum VideoSourceType {

	SWF_CLOUD_CLASS(0,"FLASH云课程"),
	VIDEO_CLOUD_CLASS(1,"视频云课程"),
	USER_VIDEO(2,"用户视频"),
	VOTE_VIDEO(3,"投票视频"),
	OTHER(8,"其他"),
	DISCARD_VIDEO(9,"废弃视频"),
	;
	
	private int type;
	private String des;
	
	
	private VideoSourceType(int type, String des) {
		this.type = type;
		this.des = des;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getDes() {
		return des;
	}


	public void setDes(String des) {
		this.des = des;
	}
	
	
	
	public static VideoSourceType getVideoSourceType(int type)
	{
		for(VideoSourceType vst:VideoSourceType.values())
		{
			if(vst.getType()==type)
			{
				return vst;
			}
		}
		
		return null;
	}
}
