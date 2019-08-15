package com.fulaan.new33.dto;


import com.pojo.new33.afresh.AfreshTypeEntry;
import com.sys.utils.StringUtil;
import org.bson.types.ObjectId;

public class AfreshTypeDTO {

    private String id;
    private String schoolId;
    private String gradeId;
    private String ciId;
    private int afreshType;
    private int use;

    public AfreshTypeDTO(){

    }

    public AfreshTypeDTO(AfreshTypeEntry e){
        this.id = e.getID().toString();
        this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
        this.gradeId = e.getGradeId()==null?"":e.getGradeId().toString();
        this.ciId = e.getCiId()==null?"":e.getCiId().toString();
        this.afreshType = e.getAfreshType();
        this.use = e.getUse();
    }

    public AfreshTypeEntry buildEntry(){
        ObjectId schId=null;
        if(StringUtil.isEmpty(this.getSchoolId())){
            schId=new ObjectId(this.getSchoolId());
        }
        ObjectId gId=null;
        if(StringUtil.isEmpty(this.getGradeId())){
            gId=new ObjectId(this.getGradeId());
        }
        ObjectId cId=null;
        if(StringUtil.isEmpty(this.getCiId())){
            cId=new ObjectId(this.getCiId());
        }
        AfreshTypeEntry entry = new AfreshTypeEntry(
                schId,
                gId,
                cId,
                this.getAfreshType(),
                this.getUse()

        );
        return entry;
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

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getCiId() {
        return ciId;
    }

    public void setCiId(String ciId) {
        this.ciId = ciId;
    }

    public int getAfreshType() {
        return afreshType;
    }

    public void setAfreshType(int afreshType) {
        this.afreshType = afreshType;
    }

    public int getUse() {
        return use;
    }

    public void setUse(int use) {
        this.use = use;
    }
}
