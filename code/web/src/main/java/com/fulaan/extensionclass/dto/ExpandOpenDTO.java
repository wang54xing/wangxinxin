package com.fulaan.extensionclass.dto;

import java.util.ArrayList;
import java.util.List;

public class ExpandOpenDTO {

	private String id;

	private String name;

	private String startTime;

	private String endTime;

	private String expandId;

	private List<String> teacherIdList = new ArrayList<String>();

	private List<String> StuList = new ArrayList<String>();

	private int studentLimitNum;

	private int genderType;

	private String schoolId;

	private List<String> targetIdList = new ArrayList<String>();

	private String asid;

	private String address;

	private String times;

	private String treeId;

	private String treeName;

	private double score;

	private int status;

	private int evalCount;
	private int ks;

	private Long qt;
	private String qtime;
	private String kuid;

	private Integer dqr;
	private Integer zrs;

	public Integer getDqr() {
		return dqr;
	}

	public void setDqr(Integer dqr) {
		this.dqr = dqr;
	}

	public Integer getZrs() {
		return zrs;
	}

	public void setZrs(Integer zrs) {
		this.zrs = zrs;
	}

	public String getKuid() {
		return kuid;
	}

	public void setKuid(String kuid) {
		this.kuid = kuid;
	}

	public ExpandOpenDTO() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getExpandId() {
		return expandId;
	}

	public void setExpandId(String expandId) {
		this.expandId = expandId;
	}

	public List<String> getTeacherIdList() {
		return teacherIdList;
	}

	public void setTeacherIdList(List<String> teacherIdList) {
		this.teacherIdList = teacherIdList;
	}

	public int getStudentLimitNum() {
		return studentLimitNum;
	}

	public void setStudentLimitNum(int studentLimitNum) {
		this.studentLimitNum = studentLimitNum;
	}

	public int getGenderType() {
		return genderType;
	}

	public void setGenderType(int genderType) {
		this.genderType = genderType;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public List<String> getTargetIdList() {
		return targetIdList;
	}

	public void setTargetIdList(List<String> targetIdList) {
		this.targetIdList = targetIdList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getTreeName() {
		return treeName;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getEvalCount() {
		return evalCount;
	}

	public void setEvalCount(int evalCount) {
		this.evalCount = evalCount;
	}

	public String getAsid() {
		return asid;
	}

	public void setAsid(String asid) {
		this.asid = asid;
	}

	public int getKs() {
		return ks;
	}

	public void setKs(int ks) {
		this.ks = ks;
	}

	public Long getQt() {
		return qt;
	}

	public void setQt(Long qt) {
		this.qt = qt;
	}

	public String getQtime() {
		return qtime;
	}

	public void setQtime(String qtime) {
		this.qtime = qtime;
	}

	public List<String> getStuList() {
		return StuList;
	}

	public void setStuList(List<String> stuList) {
		StuList = stuList;
	}
}
