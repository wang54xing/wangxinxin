package com.fulaan.new33.dto.isolate;

import com.pojo.new33.isolate.ZouBanTimeEntry;
import org.bson.types.ObjectId;

public class ZouBanTimeDTO {
	private String schoolId;
	private String xqid;
	private String gid;
	private Integer type;
	private Integer x;
	private Integer y;

	public ZouBanTimeDTO() {
	}

	public ZouBanTimeDTO(String schoolId, String xqid, String gid, Integer type, Integer x, Integer y) {
		this.schoolId = schoolId;
		this.xqid = xqid;
		this.gid = gid;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public ZouBanTimeDTO(ZouBanTimeEntry entry) {
		this.schoolId = entry.getSid().toString();
		this.xqid = entry.getXqid().toString();
		this.gid = entry.getGradeId().toString();
		this.type = entry.getType();
		this.x = entry.getX();
		this.y = entry.getY();
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

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	private ZouBanTimeEntry getEntry(ZouBanTimeDTO dto){
		ZouBanTimeEntry entry = new ZouBanTimeEntry(new ObjectId(dto.getSchoolId()),new ObjectId(dto.getXqid()),dto.getType(),dto.getX(),dto.getY(),new ObjectId(dto.getGid()));
		return entry;
	}
}
