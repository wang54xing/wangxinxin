package com.fulaan.new33.dto.autopk;

import com.pojo.autoPK.N33_ZouBanSetEntry;
import org.bson.types.ObjectId;

public class N33_ZouBanSetDTO {
	private String id;

	private String schoolId;

	private String ciId;

	private Integer type;

	private String gradeId;

	private Integer count;

	private boolean flag;

	public N33_ZouBanSetDTO() {
	}

	public N33_ZouBanSetDTO(N33_ZouBanSetEntry entry) {
		this.id = entry.getID().toString();
		this.gradeId = entry.getGradeId().toString();
		this.schoolId = entry.getSchoolId().toString();
		this.ciId = entry.getCiId().toString();
		this.type = entry.getType();
		this.count = entry.getCount();
		this.flag = entry.getFlag();
	}

	public N33_ZouBanSetDTO(String gradeId, String schoolId, String ciId, Integer type, Integer count,boolean flag) {
		this.gradeId = gradeId;
		this.schoolId = schoolId;
		this.ciId = ciId;
		this.type = type;
		this.count = count;
		this.flag = flag;
	}

	public N33_ZouBanSetEntry buildEntry() {
		N33_ZouBanSetEntry entry = new N33_ZouBanSetEntry(new ObjectId(ciId), new ObjectId(schoolId), new ObjectId(gradeId),flag, count, type);
		if("*".equals(this.id)){
			entry.setID(new ObjectId());
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

	public String getCiId() {
		return ciId;
	}

	public void setCiId(String ciId) {
		this.ciId = ciId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}


}
