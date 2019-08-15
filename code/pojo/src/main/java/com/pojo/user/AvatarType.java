package com.pojo.user;
/**
 * 头像类型
 * @author fourer
 *
 */
public enum AvatarType {

	MIN_AVATAR(1,"最小尺寸头像"),
	MIDDLE_AVATAR(2,"中间尺寸头像"),
	MAX_AVATAR(3,"最大尺寸头像"),
	;
	
	private int type;
	private String des;
	
	
	private AvatarType(int type, String des) {
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
	
	
}
