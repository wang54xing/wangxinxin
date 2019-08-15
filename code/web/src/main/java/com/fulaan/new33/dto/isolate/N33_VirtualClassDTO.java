package com.fulaan.new33.dto.isolate;

import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.N33_XuNiBanEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_VirtualClassDTO {

	private String id;

	private String schoolId;

	private String gradeId;

	private String xqid;

	private List<String> jxbIds;

	private List<StudentTagDTO> tagList;

	public N33_VirtualClassDTO() {
	}

	public N33_VirtualClassDTO(N33_XuNiBanEntry entry) {
		id = entry.getID().toString();
		xqid = entry.getXqId().toString();
		schoolId = entry.getSchoolId().toString();
		gradeId = entry.getGradeId().toString();
		if (entry.getJxbIds() != null) {
			jxbIds = MongoUtils.convertToStringList(entry.getJxbIds());
		}
		if (entry.getTagList() != null) {
			List<StudentTagDTO> list = new ArrayList<StudentTagDTO>();
			for (N33_XuNiBanEntry.StudentTag studentTag : entry.getTagList()) {
				list.add(new StudentTagDTO(studentTag));
			}
			tagList = list;
		}
	}
	
	public N33_XuNiBanEntry buildEntry(){
		N33_XuNiBanEntry xuNiBanEntry = new N33_XuNiBanEntry();
		if(id != null){
			xuNiBanEntry.setID(new ObjectId(id));
		}
		if(schoolId != null){
			xuNiBanEntry.setSchoolId(new ObjectId(schoolId));
		}
		if(xqid != null){
			xuNiBanEntry.setXqId(new ObjectId(xqid));
		}
		if(gradeId != null){
			xuNiBanEntry.setGradeId(new ObjectId(gradeId));
		}
		if(jxbIds != null && jxbIds.size() > 0){
			xuNiBanEntry.setJxbIds(MongoUtils.convertToObjectIdList(jxbIds));
		}
		if(tagList != null && tagList.size() > 0){
			List<N33_XuNiBanEntry.StudentTag> studentTagList = new ArrayList<N33_XuNiBanEntry.StudentTag>();
			for (StudentTagDTO studentTagDTO : tagList) {
				studentTagList.add(studentTagDTO.buildStudentTag());
			}
			xuNiBanEntry.setTagList(studentTagList);
		}
		return xuNiBanEntry;
	}


	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getXqid() {
		return xqid;
	}

	public void setXqid(String xqid) {
		this.xqid = xqid;
	}

	public List<String> getJxbIds() {
		return jxbIds;
	}

	public void setJxbIds(List<String> jxbIds) {
		this.jxbIds = jxbIds;
	}

	public static class StudentTagDTO{

		private String tagId;

		private String tagName;

		private List<SubjectListDTO> subjectList;

		private List<String> clsIds;

		public StudentTagDTO(){

		}

		public StudentTagDTO(N33_XuNiBanEntry.StudentTag studentTag){
			this.tagId = studentTag.getTagId().toString();
			this.tagName = studentTag.getTagName();
			if(studentTag.getSubjectList() != null){
				List<SubjectListDTO> subjectInfoList = new ArrayList<SubjectListDTO>();
				for (N33_XuNiBanEntry.SubjectInfo subjectInfo:studentTag.getSubjectList()) {
					subjectInfoList.add(new SubjectListDTO(subjectInfo));
				}
				this.subjectList = subjectInfoList;
			}
			if(studentTag.getClsIds() != null){
				this.clsIds = MongoUtils.convertToStringList(studentTag.getClsIds());
			}
		}

		public StudentTagDTO(String tagId,String tagName,List<SubjectListDTO> subjectInfoList,List<String> clsIds){
			this.tagId = tagId;
			this.tagName = tagName;
			this.subjectList = subjectInfoList;
			this.clsIds = clsIds;
		}

		public N33_XuNiBanEntry.StudentTag buildStudentTag(){
			N33_XuNiBanEntry.StudentTag studentTag = new N33_XuNiBanEntry.StudentTag();
			if(tagId != null){
				studentTag.setTagId(new ObjectId(tagId));
			}
			if(tagName != null){
				studentTag.setTagName(tagName);
			}
			if(clsIds != null && clsIds.size() > 0){
				studentTag.setClsIds(MongoUtils.convertToObjectIdList(clsIds));
			}
			if(subjectList != null && subjectList.size() != 0){
				List<N33_XuNiBanEntry.SubjectInfo> infoList = new ArrayList<N33_XuNiBanEntry.SubjectInfo>();
				for (SubjectListDTO subjectListDTO:subjectList) {
					infoList.add(subjectListDTO.buildSubjectInfo());
				}
				studentTag.setSubjectList(infoList);
			}
			return studentTag;
		}

		public String getTagId() {
			return tagId;
		}

		public void setTagId(String tagId) {
			this.tagId = tagId;
		}

		public String getTagName() {
			return tagName;
		}

		public void setTagName(String tagName) {
			this.tagName = tagName;
		}

		public List<SubjectListDTO> getSubjectList() {
			return subjectList;
		}

		public void setSubjectList(List<SubjectListDTO> subjectList) {
			this.subjectList = subjectList;
		}

		public List<String> getClsIds() {
			return clsIds;
		}

		public void setClsIds(List<String> clsIds) {
			this.clsIds = clsIds;
		}
	}

	public static class SubjectListDTO{
		private String subjectId;

		private Integer stuCount;

		private Integer isFinish;

		private List<String> stuIds;

		public SubjectListDTO(){

		}

		public SubjectListDTO(String subjectId,Integer stuCount, Integer isFinish,List<String> stuIds){
			this.subjectId = subjectId;
			this.stuCount = stuCount;
			this.isFinish = isFinish;
			this.stuIds = stuIds;
		}

		public N33_XuNiBanEntry.SubjectInfo buildSubjectInfo(){
			N33_XuNiBanEntry.SubjectInfo subjectInfo = new N33_XuNiBanEntry.SubjectInfo();
			if(subjectId != null){
				subjectInfo.setSubId(new ObjectId(subjectId));
			}
			if(stuCount != null){
				subjectInfo.setStuCount(stuCount);
			}
			if(isFinish != null){
				subjectInfo.setIsFinish(isFinish);
			}
			if(stuIds != null && stuIds.size() > 0){
				subjectInfo.setStuIds(MongoUtils.convertToObjectIdList(stuIds));
			}
			return subjectInfo;
		}

		public SubjectListDTO(N33_XuNiBanEntry.SubjectInfo subjectInfo){
			this.subjectId = subjectInfo.getSubId().toString();

			this.stuCount = subjectInfo.getStuCount();

			this.isFinish = subjectInfo.getIsFinish();

			this.stuIds = MongoUtils.convertToStringList(subjectInfo.getStuIds());
		}

		public String getSubjectId() {
			return subjectId;
		}

		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}

		public Integer getStuCount() {
			return stuCount;
		}

		public void setStuCount(Integer stuCount) {
			this.stuCount = stuCount;
		}

		public Integer getIsFinish() {
			return isFinish;
		}

		public void setIsFinish(Integer isFinish) {
			this.isFinish = isFinish;
		}

		public List<String> getStuIds() {
			return stuIds;
		}

		public void setStuIds(List<String> stuIds) {
			this.stuIds = stuIds;
		}
	}
}
