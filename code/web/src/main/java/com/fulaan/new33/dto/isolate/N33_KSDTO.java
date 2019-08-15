package com.fulaan.new33.dto.isolate;

import com.pojo.new33.isolate.N33_KSEntry;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2018/3/8.
 */
public class N33_KSDTO {
    private String id;
    private String subid;
    private String gid;
    private String snm;
    private String xqid;
    private Integer type;
    private Integer time;
    private String sid;
    private Integer dTime;
    private Integer isZouBan;
    private Integer type1;
    private Integer dan;

    public Integer getDan() {
        return dan;
    }

    public void setDan(Integer dan) {
        this.dan = dan;
    }

    public Integer getType1() {
        return type1;
    }

    public void setType1(Integer type1) {
        this.type1 = type1;
    }

    public Integer getIsZouBan() {
        return isZouBan;
    }

    public void setIsZouBan(Integer isZouBan) {
        this.isZouBan = isZouBan;
    }

    public Integer getdTime() {
        return dTime;
    }

    public void setdTime(Integer dTime) {
        this.dTime = dTime;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getSnm() {
        return snm;
    }

    public void setSnm(String snm) {
        this.snm = snm;
    }

    public String getXqid() {
        return xqid;
    }

    public void setXqid(String xqid) {
        this.xqid = xqid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public N33_KSDTO(String id, String subid, String gid, String snm, String xqid, Integer type, Integer time, String sid, Integer dTime) {
        this.id = id;
        this.subid = subid;
        this.gid = gid;
        this.snm = snm;
        this.xqid = xqid;
        this.type = type;
        this.time = time;
        this.sid = sid;
        this.dTime = dTime;
    }

    public N33_KSDTO(String id, String subid, String gid, String snm, String xqid, Integer type, Integer time, String sid, Integer dTime, Integer isZouBan) {
        this.id = id;
        this.subid = subid;
        this.gid = gid;
        this.snm = snm;
        this.xqid = xqid;
        this.type = type;
        this.time = time;
        this.sid = sid;
        this.dTime = dTime;
        this.isZouBan = isZouBan;
    }

    public N33_KSDTO() {
    }

    public N33_KSDTO(N33_KSEntry entry) {
        this.id = entry.getID().toString();
        this.sid = entry.getSchoolId().toString();
        this.subid = entry.getSubjectId().toHexString();
        this.snm = entry.getSubjectName();
        this.time = entry.getTime();
        this.type = entry.getType();
        this.type1 = entry.getType1();
        this.gid = entry.getGradeId().toHexString();
        this.xqid = entry.getXQId().toHexString();
        this.dTime = entry.getDTime();
        this.isZouBan = entry.getIsZouBan();
        this.dan=entry.getDan();
    }


    public N33_KSEntry getEntry() {
        N33_KSEntry entry = new N33_KSEntry(new ObjectId(xqid), new ObjectId(sid), snm, new ObjectId(subid), time,  dTime, type,type1, new ObjectId(gid), isZouBan,dan);
        entry.setID(new ObjectId(id));
        return entry;
    }
}
