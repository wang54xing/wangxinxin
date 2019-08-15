package com.pojo.school;
/**
 * 班级类型枚举
 * @author fourer
 *
 */
public enum ClassType {

	COMMON(0,"普通班级"),
	INTEREST_CLASS(1,"兴趣班"),
	;
	
	private int type;
	private String des;
	
	
	private ClassType(int type, String des) {
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
	
	
	public ClassType getClassType(int type)
	{
	  for(ClassType cty:ClassType.values())
	  {
		  if(cty.getType()==type)
		  {
			  return cty;
		  }
	  }
	  return null;
	}
	
}
