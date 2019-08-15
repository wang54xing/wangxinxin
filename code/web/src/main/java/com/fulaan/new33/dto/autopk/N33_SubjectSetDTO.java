package com.fulaan.new33.dto.autopk;

import com.pojo.autoPK.N33_SubjectSetEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_SubjectSetDTO {
	private String id;

	private String schoolId;

	private String ciId;

	private Integer type;

	private String gradeId;

	private Integer AMcount;

	private Integer PMcount;

	private boolean flag;

	private List<String> subIds;

	private List<MutexSubjectDTO> mutexSubjectDTOS;

	public N33_SubjectSetDTO() {
	}

	public N33_SubjectSetDTO(String schoolId, String ciId, Integer type, String gradeId, Integer AMcount, Integer PMcount, boolean flag, List<String> subIds, List<MutexSubjectDTO> mutexSubjectDTOS) {
		this.schoolId = schoolId;
		this.ciId = ciId;
		this.type = type;
		this.gradeId = gradeId;
		this.AMcount = AMcount;
		this.PMcount = PMcount;
		this.flag = flag;
		this.subIds = subIds;
		this.mutexSubjectDTOS = mutexSubjectDTOS;
	}

	public N33_SubjectSetDTO(N33_SubjectSetEntry subjectSetEntry) {
		this.id = subjectSetEntry.getID().toString();
		this.schoolId = subjectSetEntry.getSchoolId().toString();
		this.ciId = subjectSetEntry.getCiId().toString();
		this.type = subjectSetEntry.getType();
		this.gradeId = subjectSetEntry.getGradeId().toString();
		this.AMcount = subjectSetEntry.getAMCount();
		this.PMcount = subjectSetEntry.getPMCount();
		this.flag = subjectSetEntry.getFlag();
		this.subIds = MongoUtils.convertToStringList(subjectSetEntry.getSubIds());
		if(subjectSetEntry.getMutexSubjects() != null){
			List<MutexSubjectDTO> list = new ArrayList<MutexSubjectDTO>();
			for (N33_SubjectSetEntry.MutexSubject mutexSubject : subjectSetEntry.getMutexSubjects()) {
				list.add(new MutexSubjectDTO(mutexSubject));
			}
			this.mutexSubjectDTOS = list;
		}
	}

	public N33_SubjectSetEntry buildEntry(){
		N33_SubjectSetEntry subjectSetEntry = new N33_SubjectSetEntry();
		if("*".equals(this.id)){
			subjectSetEntry.setID(new ObjectId());
		}
		if(schoolId != null){
			subjectSetEntry.setSchoolId(new ObjectId(schoolId));
		}
		if(ciId != null){
			subjectSetEntry.setCiId(new ObjectId(ciId));
		}
		if(gradeId != null){
			subjectSetEntry.setGradeId(new ObjectId(gradeId));
		}
		if(type != null){
			subjectSetEntry.setType(type);
		}
		subjectSetEntry.setFlag(flag);
		if(AMcount != null){
			subjectSetEntry.setAMCount(AMcount);
		}
		if(PMcount != null){
			subjectSetEntry.setPMCount(PMcount);
		}
		if(subIds != null && subIds.size() > 0){
			subjectSetEntry.setSubIds(MongoUtils.convertToObjectIdList(subIds));
		}
		if(mutexSubjectDTOS != null && mutexSubjectDTOS.size() > 0){
			List<N33_SubjectSetEntry.MutexSubject> studentTagList = new ArrayList<N33_SubjectSetEntry.MutexSubject>();
			for (MutexSubjectDTO mutexSubjectDTO : mutexSubjectDTOS) {
				studentTagList.add(mutexSubjectDTO.buildMutexSubject());
			}
			subjectSetEntry.setMutexSubjects(studentTagList);
		}
		return subjectSetEntry;
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

	public Integer getAMcount() {
		return AMcount;
	}

	public void setAMcount(Integer AMcount) {
		this.AMcount = AMcount;
	}

	public Integer getPMcount() {
		return PMcount;
	}

	public void setPMcount(Integer PMcount) {
		this.PMcount = PMcount;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public List<String> getSubIds() {
		return subIds;
	}

	public void setSubIds(List<String> subIds) {
		this.subIds = subIds;
	}

	public List<MutexSubjectDTO> getMutexSubjectDTOS() {
		return mutexSubjectDTOS;
	}

	public void setMutexSubjectDTOS(List<MutexSubjectDTO> mutexSubjectDTOS) {
		this.mutexSubjectDTOS = mutexSubjectDTOS;
	}

	public static class MutexSubjectDTO {
		private String id;

		private String subjectId1;

		private String subjectId2;

		private String subName1;

		private String subName2;

		public MutexSubjectDTO() {
		}

		public MutexSubjectDTO(N33_SubjectSetEntry.MutexSubject mutexSubject) {
			this.subjectId1 = mutexSubject.getSubjectId1().toString();
			this.subjectId2 = mutexSubject.getSubjectId2().toString();
			this.id = mutexSubject.getID().toString();
		}

		public MutexSubjectDTO(String id,String subjectId1,String subjectId2){
			this.id = id;
			this.subjectId1 = subjectId1;
			this.subjectId2 = subjectId2;
		}

		public N33_SubjectSetEntry.MutexSubject buildMutexSubject(){
			N33_SubjectSetEntry.MutexSubject mutexSubject = new N33_SubjectSetEntry.MutexSubject(new ObjectId(id),new ObjectId(this.subjectId1),new ObjectId(subjectId2));
			return mutexSubject;
		}

		public String getSubjectId1() {
			return subjectId1;
		}

		public void setSubjectId1(String subjectId1) {
			this.subjectId1 = subjectId1;
		}

		public String getSubjectId2() {
			return subjectId2;
		}

		public void setSubjectId2(String subjectId2) {
			this.subjectId2 = subjectId2;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSubName1() {
			return subName1;
		}

		public void setSubName1(String subName1) {
			this.subName1 = subName1;
		}

		public String getSubName2() {
			return subName2;
		}

		public void setSubName2(String subName2) {
			this.subName2 = subName2;
		}
	}
}
