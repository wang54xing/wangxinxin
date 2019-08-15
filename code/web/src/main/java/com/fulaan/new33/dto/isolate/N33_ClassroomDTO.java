package com.fulaan.new33.dto.isolate;

import java.util.ArrayList;
import java.util.List;

import com.pojo.app.IdNameValuePairDTO;
import org.bson.types.ObjectId;

import com.pojo.new33.isolate.N33_ClassroomEntry;

public class N33_ClassroomDTO {

	private String id;
	private String schoolId;
	private String xqid;
	private String gradeId;
	private String roomId;
    private String roomName;
    private String description;
    private String classId;
    private String className;
    private Integer capacity;
    private Integer type;
    private Integer arrange;
    private Integer classTime;
    private Integer arrangedTime;
	private List<IdNameValuePairDTO> weekList;

	private String gradeName;
	private String classNumber;
    
    public N33_ClassroomDTO() {}
    
    public N33_ClassroomDTO(N33_ClassroomEntry entry) {
    	id = entry.getID().toString();
    	schoolId = entry.getSchoolId().toString();
    	xqid = entry.getXQId().toString();
    	gradeId = entry.getGradeId().toString();
    	roomId = entry.getRoomId().toString();
    	roomName = entry.getRoomName()==null?"":entry.getRoomName();
    	description = entry.getDescription()==null?"":entry.getDescription();
    	if(entry.getClassId()!=null) {
    		classId = entry.getClassId().toString();
    	}
    	className = entry.getClassName()==null?"":entry.getClassName();
    	capacity = entry.getCapacity();
    	type = entry.getType();
    	arrange = entry.getArrange();
    }
    
    public N33_ClassroomEntry buildEntry() {
    	N33_ClassroomEntry entry = new N33_ClassroomEntry();
    	if(id!=null) {
    		entry.setID(new ObjectId(id));
    	}
    	entry.setSchoolId(new ObjectId(schoolId));
    	entry.setXQId(new ObjectId(xqid));
    	entry.setGradeId(new ObjectId(gradeId));
    	entry.setRoomId(new ObjectId(roomId));
    	entry.setRoomName(roomName);
    	entry.setDescription(description);
    	if(classId!=null) {
    		entry.setClassId(new ObjectId(classId));
    	}
    	entry.setClassName(className);
    	if(capacity!=null) {
    		entry.setCapacity(capacity);
    	}
    	entry.setType(type);
    	if(arrange!=null) {
    		entry.setArrange(arrange);
    	}
    	return entry;
    }
    public static List<N33_ClassroomEntry> buildEntryList(List<N33_ClassroomDTO> list){
    	List<N33_ClassroomEntry> result = new ArrayList<N33_ClassroomEntry>();
    	for(N33_ClassroomDTO dto:list) {
    		result.add(dto.buildEntry());
    	}
    	return result;
    }
	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
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

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Integer getArrange() {
		return arrange;
	}

	public void setArrange(Integer arrange) {
		this.arrange = arrange;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<IdNameValuePairDTO> getWeekList() {
		return weekList;
	}

	public void setWeekList(List<IdNameValuePairDTO> weekList) {
		this.weekList = weekList;
	}

	public Integer getClassTime() {
		return classTime;
	}

	public void setClassTime(Integer classTime) {
		this.classTime = classTime;
	}

	public Integer getArrangedTime() {
		return arrangedTime;
	}

	public void setArrangedTime(Integer arrangedTime) {
		this.arrangedTime = arrangedTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
