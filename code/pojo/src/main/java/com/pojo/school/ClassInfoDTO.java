package com.pojo.school;

import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePairDTO;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

/**
 * 班级信息
 * @author fourer
 *
 */
public class ClassInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5197596091397995948L;

	private String id;
	private String className;
    private String gradeId;
    private String gradeName;
    private int gradeType;
    private String mainTeacherId;
    private String mainTeacher;
    private String teacherName;
    private String teacherAvatar;
	private String introduce;
    private String schoolId;
	private IdValuePairDTO geoInfo;
    private List<ObjectId> studentIds;
	private List<ObjectId> teacherIds;
    private int studentCount;
    private int classType; //0表示主板家 1 表示兴趣班
    private int lessonCount;
    private int homeworkCount;

    public int getCoursetype() {
        return coursetype;
    }

    public void setCoursetype(int coursetype) {
        this.coursetype = coursetype;
    }

    private int coursetype;

    public ClassInfoDTO()
	{}
	public ClassInfoDTO(ClassEntry e)
	{
		this.id=e.getID().toString();
		this.className=e.getName();
        this.studentIds=e.getStudents();
        this.teacherIds=e.getTeachers();
		if(null!=e.getGradeId())
		{
		  this.gradeId=e.getGradeId().toString();
		}
        this.classType = 0;
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
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public IdValuePairDTO getGeoInfo() {
		return geoInfo;
	}
	public void setGeoInfo(IdValuePairDTO geoInfo) {
		this.geoInfo = geoInfo;
	}
    public String getMainTeacher() {
        return mainTeacher;
    }

    public void setMainTeacher(String mainTeacher) {
        this.mainTeacher = mainTeacher;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }

    public String getMainTeacherId() {
        return mainTeacherId;
    }

    public void setMainTeacherId(String mainTeacherId) {
        this.mainTeacherId = mainTeacherId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public ClassEntry exportEntry() {
        ClassEntry classEntry=new ClassEntry(new BasicDBObject());
        if(gradeId!=null && !gradeId.trim().equals(""))
            classEntry.setGradeId(new ObjectId(gradeId));
        classEntry.setIntroduce(introduce);
        classEntry.setName(className);
        
        if(null!=mainTeacherId && ObjectId.isValid(mainTeacherId))
        {
         classEntry.setMaster(new ObjectId(mainTeacherId));
        }
        else
        {
        	classEntry.setMaster(null);
        }
        if(schoolId!=null && !schoolId.trim().equals(""))
            classEntry.setSchoolId(new ObjectId(schoolId));

        classEntry.setTeachers(teacherIds);
        classEntry.setStudents(studentIds);
        if(studentIds!=null)
        {
            classEntry.setTotalStudent(studentIds.size());
        }
        if(id!=null){
            classEntry.setID(new ObjectId(id));
        }
        classEntry.setName(this.className);
        classEntry.setTotalStudent(this.studentCount);
        classEntry.setIsRemove(Constant.ZERO);
        
        return classEntry;
    }
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public List<ObjectId> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<ObjectId> studentIds) {
        this.studentIds = studentIds;
    }

    public List<ObjectId> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(List<ObjectId> teacherIds) {
        this.teacherIds = teacherIds;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

    public int getGradeType() {
        return gradeType;
    }

    public void setGradeType(int gradeType) {
        this.gradeType = gradeType;
    }

    public int getHomeworkCount() {
        return homeworkCount;
    }

    public void setHomeworkCount(int homeworkCount) {
        this.homeworkCount = homeworkCount;
    }
}
