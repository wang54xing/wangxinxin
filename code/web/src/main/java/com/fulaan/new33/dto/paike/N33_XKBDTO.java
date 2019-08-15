package com.fulaan.new33.dto.paike;

import com.fulaan.new33.dto.isolate.CourseRangeDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wang_xinxin on 2018/5/22.
 */
public class N33_XKBDTO {
    private String teacherId;

    private String teacherName;

    private List<CourseRangeDTO> courseRangeDTOList;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<CourseRangeDTO> getCourseRangeDTOList() {
        return courseRangeDTOList;
    }

    public void setCourseRangeDTOList(List<CourseRangeDTO> courseRangeDTOList) {
        this.courseRangeDTOList = courseRangeDTOList;
    }
}
