package com.fulaan.new33.dto.isolate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import com.pojo.new33.CourseRangeEntry;

public class CourseRangeDTO {
	
	private String id;
	private String schoolId;
	private String xqid;
	private Integer serial;
	private String name;
	private long lstart;
	private long lend;
	private String start;
	private String end;
	private String range;
	
	public CourseRangeDTO() {}
	
	
	public CourseRangeDTO(CourseRangeEntry entry) {
		id = entry.getID().toString();
		schoolId = entry.getSchoolId().toString();
		serial = entry.getSerial();
		name = entry.getName();
		lstart = entry.getStart();
		lend = entry.getEnd();
		start = DateTimeUtils.getLongToStrTime(lstart, "HH:mm");
		end = DateTimeUtils.getLongToStrTime(lend, "HH:mm");
	}

	public static void main(String[] args){

	}
	
	public CourseRangeEntry buildEntry() throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		lstart = sf.parse(start).getTime();
		lend = sf.parse(end).getTime();
		CourseRangeEntry entry = new CourseRangeEntry(new ObjectId(schoolId), new ObjectId(xqid), serial, name, lstart, lend);
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
	
	public String getXqid() {
		return xqid;
	}


	public void setXqid(String xqid) {
		this.xqid = xqid;
	}


	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getSerial() {
		return serial;
	}
	public void setSerial(Integer serial) {
		this.serial = serial;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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


	public String getRange() {
		return range;
	}


	public void setRange(String range) {
		this.range = range;
	}
	
}
