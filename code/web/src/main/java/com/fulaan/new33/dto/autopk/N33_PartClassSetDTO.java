package com.fulaan.new33.dto.autopk;

import com.fulaan.utils.StringUtil;
import com.pojo.autoPK.N33_PartClassSetEntry;
import org.bson.types.ObjectId;

public class N33_PartClassSetDTO {

    private String id;

    private String schoolId;

    private String ciId;

    private String gradeId;

    private int fenDuan;

    private String classId;

    private String className;

    private String showSlassName;

    private Integer classSort;

    private String gradeName;

    public N33_PartClassSetDTO(){

    }

    public N33_PartClassSetDTO(N33_PartClassSetEntry e){
        this.id = e.getID()==null?"":e.getID().toString();
        this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
        this.ciId = e.getCiId()==null?"":e.getCiId().toString();
        this.gradeId = e.getGradeId()==null?"":e.getGradeId().toString();
        this.fenDuan = e.getFenDuan();
        this.classId = e.getClassId()==null?"":e.getClassId().toString();
    }

    public N33_PartClassSetEntry buildEntry(){
        N33_PartClassSetEntry setEntry = new N33_PartClassSetEntry();
        if("*".equals(this.id)){
            setEntry.setID(new ObjectId());
        }else{
            if(StringUtil.isEmpty(this.id)) {
                setEntry.setID(new ObjectId(this.id));
            }
        }
        if(this.schoolId != null){
            setEntry.setSchoolId(new ObjectId(this.schoolId));
        }
        if(this.ciId != null){
            setEntry.setCiId(new ObjectId(this.ciId));
        }
        if(this.gradeId != null){
            setEntry.setGradeId(new ObjectId(this.gradeId));
        }
        setEntry.setFenDuan(this.fenDuan);
        if(this.classId != null){
            setEntry.setClassId(new ObjectId(this.classId));
        }
        return setEntry;
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

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public int getFenDuan() {
        return fenDuan;
    }

    public void setFenDuan(int fenDuan) {
        this.fenDuan = fenDuan;
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

    public String getShowSlassName() {
        return showSlassName;
    }

    public void setShowSlassName(String showSlassName) {
        this.showSlassName = showSlassName;
    }

    public Integer getClassSort() {
        return classSort;
    }

    public void setClassSort(Integer classSort) {
        this.classSort = classSort;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
