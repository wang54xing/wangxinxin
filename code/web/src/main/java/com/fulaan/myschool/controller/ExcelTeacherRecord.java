package com.fulaan.myschool.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2015/6/2.
 */
public class ExcelTeacherRecord {
	private String teacherName;
	private String jobNumber;// 职工号
	private int sex;// 1 男 0 女
	private int role;// 用户角色
	private String postionDesc;//职务
	
	public ExcelTeacherRecord(String teacherName, String jobNumber, int sex,
			int role,String postionDesc) {
		super();
		this.teacherName = teacherName;
		this.jobNumber = jobNumber;
		this.sex = sex;
		this.role = role;
		this.postionDesc = postionDesc;
	}


	private List<GradeAndClass> GradeAndClassList = new ArrayList<ExcelTeacherRecord.GradeAndClass>();

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getPostionDesc() {
		return postionDesc;
	}

	public void setPostionDesc(String postionDesc) {
		this.postionDesc = postionDesc;
	}

	public List<GradeAndClass> getGradeAndClassList() {
		return GradeAndClassList;
	}

	public void setGradeAndClassList(List<GradeAndClass> gradeAndClassList) {
		GradeAndClassList = gradeAndClassList;
	}
	
	public void addGradeAndClass(GradeAndClass gac)
	{
		GradeAndClassList.add(gac);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((teacherName == null) ? 0 : teacherName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExcelTeacherRecord other = (ExcelTeacherRecord) obj;
		if (teacherName == null) {
			if (other.teacherName != null)
				return false;
		} else if (!teacherName.equals(other.teacherName))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ExcelTeacherRecord [teacherName=" + teacherName
				+ ", jobNumber=" + jobNumber + ", sex=" + sex + ", role="
				+ role + ", GradeAndClassList=" + GradeAndClassList + "]";
	}



	public static class GradeAndClass {
		private String subjectName;// 学科名称
		private String gradeName;// 学科名称
		private int gradeCode;// 年级代码
		private String className;// 年级代码
		private boolean isMaster; // 班主任
		private boolean isLeaderSubject; // 班主任
		private boolean isLeaderGrade; // 年级组长
		private boolean isPrepareLeader; // 备课组长
		public GradeAndClass(String subjectName, String gradeName,
				int gradeCode, String className, boolean isMaster,
				boolean isLeaderSubject, boolean isLeaderGrade,
				boolean isPrepareLeader) {
			super();
			this.subjectName = subjectName;
			this.gradeName = gradeName;
			this.gradeCode = gradeCode;
			this.className = className;
			this.isMaster = isMaster;
			this.isLeaderSubject = isLeaderSubject;
			this.isLeaderGrade = isLeaderGrade;
			this.isPrepareLeader = isPrepareLeader;
		}

		public String getSubjectName() {
			return subjectName;
		}

		public void setSubjectName(String subjectName) {
			this.subjectName = subjectName;
		}

		public String getGradeName() {
			return gradeName;
		}

		public void setGradeName(String gradeName) {
			this.gradeName = gradeName;
		}

		public int getGradeCode() {
			return gradeCode;
		}

		public void setGradeCode(int gradeCode) {
			this.gradeCode = gradeCode;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public boolean isMaster() {
			return isMaster;
		}

		public void setMaster(boolean isMaster) {
			this.isMaster = isMaster;
		}

		public boolean isLeaderSubject() {
			return isLeaderSubject;
		}

		public void setLeaderSubject(boolean isLeaderSubject) {
			this.isLeaderSubject = isLeaderSubject;
		}

		public boolean isLeaderGrade() {
			return isLeaderGrade;
		}

		public void setLeaderGrade(boolean isLeaderGrade) {
			this.isLeaderGrade = isLeaderGrade;
		}

		public boolean isPrepareLeader() {
			return isPrepareLeader;
		}

		public void setPrepareLeader(boolean isPrepareLeader) {
			this.isPrepareLeader = isPrepareLeader;
		}

		@Override
		public String toString() {
			return "GradeAndClass [subjectName=" + subjectName + ", gradeName="
					+ gradeName + ", gradeCode=" + gradeCode + ", className="
					+ className + ", isMaster=" + isMaster
					+ ", isLeaderSubject=" + isLeaderSubject
					+ ", isLeaderGrade=" + isLeaderGrade + ", isPrepareLeader="
					+ isPrepareLeader + "]";
		}

	}

}
