package com.pojo.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pojo.app.ModuleApps;
/**
 * 
 * @author fourer
 *
 */
public class SimpleLogStatDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8833102849038577674L;
	
	private int index;
	private String name;
	private List<TimeData> timeData=new ArrayList<SimpleLogStatDTO.TimeData>();
	
	
	public SimpleLogStatDTO(ModuleApps apps,List<SimpleLogTimes> ts)
	{
		this.index=apps.getIndex();
		this.name=apps.getName();
		for(SimpleLogTimes t:ts)
		{
			this.timeData.add(new TimeData(t.getBeginTime(), t.getEndTime(), t.getName()));
		}
	}
	
	
	
	public void increase(long time)
	{
		for(TimeData td:timeData)
		{
			if(time >td.getBeginTime() && time <= td.getEndTime())	
			{
				td.setData(td.getData()+1);
				break;
			}
		}
	}
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<TimeData> getTimeData() {
		return timeData;
	}

	public void setTimeData(List<TimeData> timeData) {
		this.timeData = timeData;
	}


	public static class TimeData extends SimpleLogTimes
	{
		private int data;
		public TimeData(long beginTime, long endTime, String name) {
			super(beginTime, endTime, name);
		}
		public int getData() {
			return data;
		}
		public void setData(int data) {
			this.data = data;
		}
	}
}
