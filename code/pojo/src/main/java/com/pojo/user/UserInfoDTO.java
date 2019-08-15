package com.pojo.user;

import com.pojo.app.IdValuePairDTO;

import java.io.Serializable;

/**
 * 用户信息
 * 
 * @author fourer
 *
 */
public class UserInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7434875365191972837L;

	
	private String id;
	private int sex;
	private int role;
	private int permission;
	private String name;
	private String realName;
	private String nickName;
	private String avt;
	private String passWord;
	private IdValuePairDTO schoolInfo;
	private IdValuePairDTO geoInfo;
	private IdValuePairDTO gradeInfo;
	private double balance;
	private int experienceValue;
	private String className;


	public UserInfoDTO()
	{
		
	}
	
	
	
	public UserInfoDTO(UserEntry e)
	{
		this.id =e.getID().toString();
		this.sex=e.getSex();
		this.role=e.getRole();
		this.name=e.getUserName();
		this.realName=e.getRealUserName();
		this.nickName=e.getNickName();
		this.passWord=e.getPassword();
		this.avt=e.getAvatar();
	}
	

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getPermission() {
		return permission;
	}

	public IdValuePairDTO getSchoolInfo() {
		return schoolInfo;
	}

	public void setSchoolInfo(IdValuePairDTO schoolInfo) {
		this.schoolInfo = schoolInfo;
	}

	public IdValuePairDTO getGeoInfo() {
		return geoInfo;
	}

	public void setGeoInfo(IdValuePairDTO geoInfo) {
		this.geoInfo = geoInfo;
	}

	public IdValuePairDTO getGradeInfo() {
		return gradeInfo;
	}

	public void setGradeInfo(IdValuePairDTO gradeInfo) {
		this.gradeInfo = gradeInfo;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public int getExperienceValue() {
		return experienceValue;
	}

	public void setExperienceValue(int experienceValue) {
		this.experienceValue = experienceValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getAvt() {
		return avt;
	}
	public void setAvt(String avt) {
		this.avt = avt;
	}



	public String getNickName() {
		return nickName;
	}



	public void setNickName(String nickName) {
		this.nickName = nickName;
	}



	public String getPassWord() {
		return passWord;
	}



	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
