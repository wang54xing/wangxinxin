package com.fulaan.new33.dto;

import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_StudentTagDTO {

    private String id;

    private String xqid;

    private String schoolId;

    private String gradeId;

    private String name;

    private Integer view;

    private List<String> jxbIds = new ArrayList<String>();

    private List<StudentInfo> studentInfos = new ArrayList<StudentInfo>();

    public N33_StudentTagDTO() {
    }

    public N33_StudentTagDTO(N33_StudentTagEntry entry) {
        id = entry.getID().toString();
        xqid = entry.getXqId().toString();
        schoolId = entry.getSchoolId().toString();
        gradeId = entry.getGradeId().toString();
        name = entry.getName();
        if (entry.getJxbIds() != null) {
            jxbIds = MongoUtils.convertToStringList(entry.getJxbIds());
        }
        if (entry.getStudents() != null) {
            List<StudentInfo> list = new ArrayList<StudentInfo>();
            for (N33_StudentTagEntry.StudentInfoEntry studentInfoEntry : entry.getStudents()) {
                list.add(new StudentInfo(studentInfoEntry));
            }
            studentInfos = list;
        }
        this.view = entry.getView();
    }

    public N33_StudentTagEntry buildEntry() {
        N33_StudentTagEntry entry = new N33_StudentTagEntry();
        if (view != null) {
            entry.setView(view);
        }
        if (id != null) {
            entry.setID(new ObjectId(id));
        }
        if (xqid != null) {
            entry.setXqId(new ObjectId(xqid));
        }
        if (schoolId != null) {
            entry.setSchoolId(new ObjectId(schoolId));
        }
        if (gradeId != null) {
            entry.setGradeId(new ObjectId(gradeId));
        }
        entry.setName(name);
        if (jxbIds != null && jxbIds.size() != 0) {
            entry.setJxbIds(MongoUtils.convertToObjectIdList(jxbIds));
        }
        if (studentInfos != null && studentInfos.size() != 0) {
            List<N33_StudentTagEntry.StudentInfoEntry> list = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
            for (StudentInfo info : studentInfos) {
                list.add(info.buildEntry());
            }
            entry.setStudents(list);
        }
        entry.setXuNi(0);
        return entry;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXqid() {
        return xqid;
    }

    public void setXqid(String xqid) {
        this.xqid = xqid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getJxbIds() {
        return jxbIds;
    }

    public void setJxbIds(List<String> jxbIds) {
        this.jxbIds = jxbIds;
    }

    public List<StudentInfo> getStudentInfos() {
        return studentInfos;
    }

    public void setStudentInfos(List<StudentInfo> studentInfos) {
        this.studentInfos = studentInfos;
    }

    public static class StudentInfo {
        private String stuid;
        private String classId;
        private String className;

        public StudentInfo() {
        }

        public StudentInfo(String stuid, String classId, String className) {
            this.stuid = stuid;
            this.classId = classId;
            this.className = className;
        }

        public N33_StudentTagEntry.StudentInfoEntry buildEntry() {
            return new N33_StudentTagEntry.StudentInfoEntry(new ObjectId(stuid), new ObjectId(classId), className);
        }

        public StudentInfo(N33_StudentTagEntry.StudentInfoEntry entry) {
            stuid = entry.getStuId().toString();
            classId = entry.getClassId().toString();
            className = entry.getClassName();
        }

        public String getStuid() {
            return stuid;
        }

        public void setStuid(String stuid) {
            this.stuid = stuid;
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
    }
}
