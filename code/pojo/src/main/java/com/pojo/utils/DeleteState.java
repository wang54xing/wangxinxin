package com.pojo.utils;

/**
 * 微博状态
 * @author fourer
 *
 */
public enum DeleteState {

	NORMAL(0,"正常"),
	DELETED(1,"已经删除"),
	;
	private int state;
	private String des;
	
	
	private DeleteState(int state, String des) {
		this.state = state;
		this.des = des;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
}
