package com.fulaan.new33.dto.isolate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.pojo.new33.isolate.N33_JXZEntry;

public class N33_JXZDTO {
	
	private String id;
	private String schoolId;
	private String xqid;
	private Integer serial;
	private String start;
	private String end;
	private long lstart;
	private long lend;
	private String numberWeek;
	// 显示，每周每天
	private List<Map<String,String>> weekdays = new ArrayList<Map<String,String>>();
	
	public N33_JXZDTO() {}
	
	
	public N33_JXZDTO(N33_JXZEntry entry) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		if(entry.getID()!=null) {
			id = entry.getID().toString();
		}
		schoolId = entry.getSchoolId().toString();
		xqid = entry.getXQId().toString();
		serial = entry.getSerial();
		lstart = entry.getStart();
		start = sf.format(new Date(lstart));
		lend = entry.getEnd();
		end = sf.format(new Date(lend));
		
	}
	public N33_JXZEntry buildEntry() throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		lstart = sf.parse(start).getTime();
		lend = sf.parse(end).getTime();
		N33_JXZEntry entry = new N33_JXZEntry(new ObjectId(schoolId), new ObjectId(xqid), serial, lstart, lend);
		if(id!=null) {
			entry.setID(new ObjectId(id));
		}
		return entry;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getXqid() {
		return xqid;
	}
	public void setXqid(String xqid) {
		this.xqid = xqid;
	}
	public Integer getSerial() {
		return serial;
	}
	public void setSerial(Integer serial) {
		this.serial = serial;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}


	public long getLstart() {
		return lstart;
	}


	public void setLstart(long lstart) {
		this.lstart = lstart;
	}


	public long getLend() {
		return lend;
	}


	public void setLend(long lend) {
		this.lend = lend;
	}


	public List<Map<String, String>> getWeekdays() {
		return weekdays;
	}


	public void setWeekdays(List<Map<String, String>> weekdays) {
		this.weekdays = weekdays;
	}


	public String getNumberWeek() {
		return numberWeek;
	}


	public void setNumberWeek(String numberWeek) {
		this.numberWeek = numberWeek;
	}
	
	
}
