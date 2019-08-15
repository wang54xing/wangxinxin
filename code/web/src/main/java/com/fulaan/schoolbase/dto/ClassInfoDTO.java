package com.fulaan.schoolbase.dto;

import com.fulaan.utils.StringUtil;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 班级信息
 * @author fourer
 *
 */
public class ClassInfoDTO implements Serializable {
	private static final long serialVersionUID = -5197596091397995948L;
    private String id;
    private String className;
    private String masterId;
    private String schoolId;
    private String gradeId;
    private String gradeName;
    private int classSize;
    private String introduce;
    private String message;
    private String target;
    private String admissionDate;
    private int classNumber;
    private String createrId;
    private int classCount;//年级下班级数
    private int studentCount;
    private String masterName;
    private String masterImage;
    private List<String> studentList;
    private List<String> teacherList;
    private List<StudentDTO> studentDTOList;
    private List<ResumeDTO> resumeDTOs;
    private int maleCount;
    private int femaleCount;
    private String classImage;
    //班级昵称
    private String nickName;

    private String roomId;
    
    private String roomName; // 关联的教室名称
    
    public ClassInfoDTO()
	{

    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public int getClassSize() {
        return classSize;
    }

    public void setClassSize(int classSize) {
        this.classSize = classSize;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public List<StudentDTO> getStudentDTOList() {
        return studentDTOList;
    }

    public void setStudentDTOList(List<StudentDTO> studentDTOList) {
        this.studentDTOList = studentDTOList;
    }

    public String getMasterImage() {
        return masterImage;
    }

    public void setMasterImage(String masterImage) {
        this.masterImage = masterImage;
    }

    public List<String> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<String> studentList) {
        this.studentList = studentList;
    }

    public List<String> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<String> teacherList) {
        List<String> teacher = new ArrayList<String>();
        if (!StringUtils.isEmpty(masterId)) {
            teacher.add(masterId);
        }
        if(teacherList!=null&& teacherList.size()!=0) {
            teacher.addAll(teacherList);
        }
        this.teacherList = teacher;
    }

    public List<ResumeDTO> getResumeDTOs() {
        return resumeDTOs;
    }

    public void setResumeDTOs(List<ResumeDTO> resumeDTOs) {
        this.resumeDTOs = resumeDTOs;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public String getClassImage() {
        return classImage;
    }

    public void setClassImage(String classImage) {
        this.classImage = classImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
    
}
