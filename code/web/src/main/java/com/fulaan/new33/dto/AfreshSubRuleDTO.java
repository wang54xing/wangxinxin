package com.fulaan.new33.dto;

import com.pojo.new33.afresh.AfreshSubRuleEntry;
import com.sys.utils.StringUtil;
import org.bson.types.ObjectId;

public class AfreshSubRuleDTO {

    private String id;
    private String schoolId;
    private String gradeId;
    private String ciId;
    private String subId;
    private String subName;
    private int afreshType;
    private int number;
    private int volume;

    public AfreshSubRuleDTO(){

    }

    public AfreshSubRuleDTO(AfreshSubRuleEntry e){
        this.id = e.getID().toString();
        this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
        this.gradeId = e.getGradeId()==null?"":e.getGradeId().toString();
        this.ciId = e.getCiId()==null?"":e.getCiId().toString();
        this.subId = e.getSubId()==null?"":e.getSubId().toString();
        this.afreshType = e.getAfreshType();
        this.number = e.getNumber();
        this.volume = e.getVolume();
    }

    public AfreshSubRuleEntry buildEntry(){
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
        ObjectId suId=null;
        if(StringUtil.isEmpty(this.getSubId())){
            suId = new ObjectId(this.getSubId());
        }
        AfreshSubRuleEntry entry = new AfreshSubRuleEntry(
                schId,
                gId,
                cId,
                suId,
                this.getAfreshType(),
                this.getNumber(),
                this.getVolume()
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

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getAfreshType() {
        return afreshType;
    }

    public void setAfreshType(int afreshType) {
        this.afreshType = afreshType;
    }

    public int getNumber() {
        return number;
    }

    public String getNumberStr() {
        if(this.getNumber()==0){
            return "";
        }else{
            return this.getNumber()+"";
        }
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getVolume() {
        return volume;
    }

    public String getVolumeStr() {
        if(this.getVolume()==0){
            return "";
        }else{
            return this.getVolume()+"";
        }
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
