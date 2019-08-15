package com.fulaan.new33.dto.paike;

import com.pojo.new33.paike.N33_NameTypeEntry;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2018/3/20.
 */
public class N33_NameTypeDTO {
    private String termId;
    private String gradeId;
    private String userId;
    private String sid;
    private String name;
    private String nid;
    private Long time;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public N33_NameTypeDTO() {
    }

    public N33_NameTypeDTO(N33_NameTypeEntry typeEntry) {
        this.sid = typeEntry.getSchoolId().toString();
        this.termId = typeEntry.getTermId().toString();
        this.gradeId = typeEntry.getGradeId().toString();
        this.name = typeEntry.getName();
        this.nid = typeEntry.getNameId().toHexString();
        this.time = typeEntry.getTime();
        this.userId=typeEntry.getUserId().toString();
    }

    public N33_NameTypeDTO(String termId, String gradeId, String userId, String sid, String name, String nid, Long time) {
        this.termId = termId;
        this.gradeId = gradeId;
        this.userId = userId;
        this.sid = sid;
        this.name = name;
        this.nid = nid;
        this.time = time;
    }

    public N33_NameTypeEntry getEntry() {
        N33_NameTypeEntry zkbEntry = new N33_NameTypeEntry(new ObjectId(gradeId), new ObjectId(sid), new ObjectId(termId), new ObjectId(nid), name, new ObjectId(userId), time);
        return zkbEntry;
    }
}
