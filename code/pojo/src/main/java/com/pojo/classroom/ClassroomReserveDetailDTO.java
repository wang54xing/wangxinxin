package com.pojo.classroom;


import com.pojo.user.UserEntry;
import com.sys.utils.DateTimeUtils;

public class ClassroomReserveDetailDTO extends ClassroomReserveDTO {
	

	private String numberStr; //楼层编号
	private String applyTime;
	private String beizhu;
	private String jiaoshiCode;
    private String stateStr;
    private String result;
	public ClassroomReserveDetailDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
     // 0 没有审核 1审核通过 2拒绝 
	public ClassroomReserveDetailDTO(ClassroomReserveEntry cre,
									 String classRoom, String timeTableName, UserEntry user,
									 UserEntry checkUser, String classRoomId, String timeTableId) {
		super(cre, classRoom, timeTableName, user, checkUser, classRoomId, timeTableId);
		// TODO Auto-generated constructor stub
		this.applyTime= DateTimeUtils.convert(cre.getID().getDate().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A);
		
		if(cre.getState()==0)
		{
			this.stateStr="未审批";
		}
		if(cre.getState()==1)
		{
			this.stateStr="审核通过";
		}
		if(cre.getState()==2)
		{
			this.stateStr="拒绝";
		}
		this.result=cre.getResult();
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public String getJiaoshiCode() {
		return jiaoshiCode;
	}

	public void setJiaoshiCode(String jiaoshiCode) {
		this.jiaoshiCode = jiaoshiCode;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getNumberStr() {
		return numberStr;
	}

	public void setNumberStr(String numberStr) {
		this.numberStr = numberStr;
	}
	public String getStateStr() {
		return stateStr;
	}
	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
