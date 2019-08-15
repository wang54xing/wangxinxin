package com.pojo.utils;

import java.io.Serializable;
/**
 * 用户登录日志
 * @author fourer
 *
 */
public class LoginLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String city;
	private String schoolId;
	private String schoolName;
	private String userId;
	private String userName;
	private String ipAddr;
	private int role;
	private String platform;

	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public String getPlatform() {
		return platform;
	}

	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	@Override
	public String toString() {
		return " [ipAddr=" + ipAddr+",city=" + city + ", schoolId=" + schoolId
				+ ", schoolName=" + schoolName +  ", userId="
				+ userId + ", userName=" + userName + ", role=" + role+", platform=" + platform+
				 "]";
	}
	
	
}
