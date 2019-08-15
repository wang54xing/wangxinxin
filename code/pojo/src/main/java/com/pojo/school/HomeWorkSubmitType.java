package com.pojo.school;

/**
 * 作业提交方式
 * @author fourer
 *
 */
public enum HomeWorkSubmitType {

	ONLINE(1,"在线提交"),
	ONCLASS(2,"课堂提交"),
	;
	
	private int type;
	private String name;
	
	
	private HomeWorkSubmitType(int type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static HomeWorkSubmitType getHomeWorkSubmitType(int type)
	{
		for(HomeWorkSubmitType t:HomeWorkSubmitType.values())
		{
			if(t.getType()==type)
			{
				return t;
			}
		}
		
		return null;
	}
	
	
}
