package com.fulaan.new33.dto.isolate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;

import com.pojo.new33.DayRangeEntry;

public class DayRangeDTO {
	private String id;
	private String schoolId;
	private long lstart1;
	private long lend1;
	private long lstart2;
	private long lend2;
	private long lstart3;
	private long lend3;
	private String start1;
	private String end1;
	private String start2;
	private String end2;
	private String start3;
	private String end3;
	private transient SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
	
	public DayRangeDTO() {}
	
	public DayRangeDTO(DayRangeEntry entry) {
		id = entry.getID().toString();
		schoolId = entry.getSchoolId().toString();
		lstart1 = entry.getStart1();
		lend1 = entry.getEnd1();
		lstart2 = entry.getStart2();
		lend2 = entry.getEnd2();
		lstart3 = entry.getStart3();
		lend3 = entry.getEnd3();
		start1 = sf.format(new Date(lstart1));
		end1 = sf.format(new Date(lend1));
		start2 = sf.format(new Date(lstart2));
		end2 = sf.format(new Date(lend2));
		start3 = sf.format(new Date(lstart3));
		end3 = sf.format(new Date(lend3));
	}
	public DayRangeEntry buildEntry() throws ParseException {
		DayRangeEntry entry =  new DayRangeEntry(new ObjectId(schoolId), 
				sf.parse(start1).getTime(), sf.parse(end1).getTime(), 
				sf.parse(start2).getTime(), sf.parse(end2).getTime(), 
				sf.parse(start3).getTime(), sf.parse(end3).getTime());
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

	public long getLstart1() {
		return lstart1;
	}

	public void setLstart1(long lstart1) {
		this.lstart1 = lstart1;
	}

	public long getLend1() {
		return lend1;
	}

	public void setLend1(long lend1) {
		this.lend1 = lend1;
	}

	public long getLstart2() {
		return lstart2;
	}

	public void setLstart2(long lstart2) {
		this.lstart2 = lstart2;
	}

	public long getLend2() {
		return lend2;
	}

	public void setLend2(long lend2) {
		this.lend2 = lend2;
	}

	public long getLstart3() {
		return lstart3;
	}

	public void setLstart3(long lstart3) {
		this.lstart3 = lstart3;
	}

	public long getLend3() {
		return lend3;
	}

	public void setLend3(long lend3) {
		this.lend3 = lend3;
	}

	public String getStart1() {
		return start1;
	}

	public void setStart1(String start1) {
		this.start1 = start1;
	}

	public String getEnd1() {
		return end1;
	}

	public void setEnd1(String end1) {
		this.end1 = end1;
	}

	public String getStart2() {
		return start2;
	}

	public void setStart2(String start2) {
		this.start2 = start2;
	}

	public String getEnd2() {
		return end2;
	}

	public void setEnd2(String end2) {
		this.end2 = end2;
	}

	public String getStart3() {
		return start3;
	}

	public void setStart3(String start3) {
		this.start3 = start3;
	}

	public String getEnd3() {
		return end3;
	}

	public void setEnd3(String end3) {
		this.end3 = end3;
	}

	public SimpleDateFormat getSf() {
		return sf;
	}

	public void setSf(SimpleDateFormat sf) {
		this.sf = sf;
	}
	
	
}
