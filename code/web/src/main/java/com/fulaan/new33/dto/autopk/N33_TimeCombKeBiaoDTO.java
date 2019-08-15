package com.fulaan.new33.dto.autopk;

import com.fulaan.utils.StringUtil;
import com.pojo.new33.paike.N33_TimeCombKeBiaoEntry;
import org.bson.types.ObjectId;

public class N33_TimeCombKeBiaoDTO {
    private String id;
    private String schoolId;
    private String gradeId;
    private String ciId;
    private int type;
    private String serialName;
    private int serial;
    private int x;
    private int y;

    public N33_TimeCombKeBiaoDTO() {
    }

    public N33_TimeCombKeBiaoDTO(N33_TimeCombKeBiaoEntry e) {
        this.id = e.getID().toString();
        this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
        this.gradeId = e.getGradeId()==null?"":e.getGradeId().toString();
        this.ciId = e.getCiId()==null?"":e.getCiId().toString();
        this.type = e.getType();
        this.serialName = e.getSerialName();
        this.serial = e.getSerial();
        this.x = e.getX();
        this.y = e.getY();
    }

    public N33_TimeCombKeBiaoEntry buildEntry(){
        ObjectId sId=null;
        if(StringUtil.isEmpty(this.getSchoolId())){
            sId=new ObjectId(this.getSchoolId());
        }
        ObjectId gid=null;
        if(StringUtil.isEmpty(this.getGradeId())){
            gid=new ObjectId(this.getGradeId());
        }
        ObjectId cid=null;
        if(StringUtil.isEmpty(this.getCiId())){
            cid=new ObjectId(this.getCiId());
        }
        N33_TimeCombKeBiaoEntry entry = new N33_TimeCombKeBiaoEntry(
                sId,
                gid,
                cid,
                this.getType(),
                this.getSerialName(),
                this.getSerial(),
                this.getX(),
                this.getY()
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSerialName() {
        return serialName;
    }

    public void setSerialName(String serialName) {
        this.serialName = serialName;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
