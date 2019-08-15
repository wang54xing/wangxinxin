package com.fulaan.new33.dto.isolate;

import com.pojo.new33.paike.N33_GDSXEntry;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

public class N33_GDSXDTO {
	private String id;
	private Integer x;
	private Integer y;
	private String desc;
	private String termId;
	private String sid;
	private String gradeId;

	public N33_GDSXDTO(){

	}

	public N33_GDSXDTO(Integer x, Integer y, String desc,  String termId, String gradeId) {
		this.x = x;
		this.y = y;
		this.desc = desc;
		this.termId = termId;
		this.gradeId = gradeId;
	}

	public N33_GDSXDTO(String id, Integer x, Integer y, String desc,  String termId, String sid, String gradeId) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.desc = desc;
		this.termId = termId;
		this.sid = sid;
		this.gradeId = gradeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public N33_GDSXDTO(N33_GDSXEntry entry) {
		this.id = entry.getID().toString();
		this.x = entry.getX();
		this.y = entry.getY();
		this.desc = entry.getDesc();
		this.sid = entry.getSchoolId().toString();
		this.termId = entry.getTermId().toHexString();
		this.gradeId = entry.getGradeId().toString();
	}

	public N33_GDSXEntry buildEntry() {
		N33_GDSXEntry entry = new N33_GDSXEntry(x, y, new ObjectId(gradeId), desc,new ObjectId(sid), new ObjectId(termId));
		if (id.equals("*")) {
			entry.setID(new ObjectId());
		} else {
			entry.setID(new ObjectId(id));
		}
		return entry;
	}
}
