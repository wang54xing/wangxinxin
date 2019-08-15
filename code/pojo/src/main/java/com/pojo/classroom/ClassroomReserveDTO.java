package com.pojo.classroom;

import com.pojo.app.IdValuePairDTO;
import com.pojo.user.UserEntry;
import com.sys.utils.DateTimeUtils;

/**
 * 教室预定DTO
 * 
 * @author fourer
 *·
 */
public class ClassroomReserveDTO {

	private String id;
	private String classRoomId;
	private String classRoom;
	private String timeTableId;
	private String timeTableName;
	private String time;
	private IdValuePairDTO user;
	private String des;

	private IdValuePairDTO checkUser;
	private int state;
	private String result;
	
    private int week; //周几
    
	public ClassroomReserveDTO() {
		super();
	}
	
	public ClassroomReserveDTO(ClassroomReserveEntry cre, String classRoom , String timeTableName, UserEntry user, UserEntry checkUser, String classRoomId, String timeTableId) {
		super();
		this.id=cre.getID().toString();
		this.timeTableId=timeTableId;
		this.classRoomId=classRoomId;
		this.state=cre.getState();
		this.classRoom=classRoom;
		this.timeTableName=timeTableName;
		this.time=  DateTimeUtils.convert(cre.getTime(), DateTimeUtils.DATE_YYYY_MM_DD);
		
		if(null!=user)
		{
		  this.user=new IdValuePairDTO(user.getID(), user.getUserName());
		}
		this.des=cre.getDes();
		if(null!=checkUser)
		{
		  this.checkUser=new IdValuePairDTO(checkUser.getID(), checkUser.getUserName());
		}
		this.result=cre.getResult();
		
	}

	
	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}

	public String getTimeTableName() {
		return timeTableName;
	}

	public void setTimeTableName(String timeTableName) {
		this.timeTableName = timeTableName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public IdValuePairDTO getUser() {
		return user;
	}

	public void setUser(IdValuePairDTO user) {
		this.user = user;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public IdValuePairDTO getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(IdValuePairDTO checkUser) {
		this.checkUser = checkUser;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getClassRoomId() {
		return classRoomId;
	}

	public void setClassRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
	}

	public String getTimeTableId() {
		return timeTableId;
	}

	public void setTimeTableId(String timeTableId) {
		this.timeTableId = timeTableId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
