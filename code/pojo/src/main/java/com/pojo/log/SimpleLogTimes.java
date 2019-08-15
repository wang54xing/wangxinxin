package com.pojo.log;

import java.util.Date;

/**
 * 简单日志统计时间段
 * @author fourer
 *
 */
public class SimpleLogTimes {

	private long beginTime;
	private long endTime;
	private String name;
	
	public SimpleLogTimes(long beginTime, long endTime, String name) {
		super();
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.name = name;
	}
	
	
	
	public long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}



	@Override
	public String toString() {
		return "SimpleLogTimes [beginTime=" + new Date(this.beginTime).toLocaleString() + ", endTime="
				+ new Date(this.endTime).toLocaleString() + ", name=" + name + "]";
	}
	
	
	
	
}
