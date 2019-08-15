package com.fulaan.new33.dto.isolate;

import java.util.List;

import org.bson.types.ObjectId;

import com.pojo.new33.isolate.Grade;
import com.pojo.utils.MongoUtils;

public class GradeDTO {

	private String id;
    private String xqid;
    private String sid;
    private String gnm;
    private List<String> cids;
    private List<String> gtea;
    private List<String> gstu;
    private String gid;
    private Integer type;
    private String jie;


    public GradeDTO() {
    }

    public GradeDTO(String id, String xqid, String gnm, List<String> cids, List<String> gtea, List<String> gstu, String sid, String gid, Integer type, String jie) {
        this.id = id;
        this.xqid = xqid;
        this.gnm = gnm;
        this.cids = cids;
        this.gtea = gtea;
        this.gstu = gstu;
        this.sid = sid;
        this.gid = gid;
        this.type = type;
        this.jie = jie;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getJie() {
        return jie;
    }

    public void setJie(String jie) {
        this.jie = jie;
    }

    public String getGnm() {
        return gnm;
    }

    public void setGnm(String gnm) {
        this.gnm = gnm;
    }

    public List<String> getCids() {
        return cids;
    }

    public void setCids(List<String> cids) {
        this.cids = cids;
    }

    public List<String> getGtea() {
        return gtea;
    }

    public void setGtea(List<String> gtea) {
        this.gtea = gtea;
    }

    public List<String> getGstu() {
        return gstu;
    }

    public void setGstu(List<String> gstu) {
        this.gstu = gstu;
    }

    public GradeDTO(Grade entry) {
        if(entry.getID()!=null){
            this.id = entry.getID().toString();
        }
        if(entry.getXQId()!=null){
            this.xqid = entry.getXQId().toString();
        }
        this.gnm = entry.getName();
        this.cids = MongoUtils.convertToStringList(entry.getClassList());
        this.gtea = MongoUtils.convertToStringList(entry.getTeacherList());
        this.gstu = MongoUtils.convertToStringList(entry.getStudentList());
        this.sid = entry.getSchoolId().toString();
        this.gid = entry.getGradeId().toHexString();
        this.type = entry.getType();
        this.jie=entry.getJie();
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public Grade buildEntry() {
        Grade entry = new Grade(new ObjectId(xqid), new ObjectId(sid), gnm, MongoUtils.convertToObjectIdList(cids), MongoUtils.convertToObjectIdList(gtea), MongoUtils.convertToObjectIdList(gstu), sy, new ObjectId(gid), type,jie);
        if (id.equals("*")) {
            entry.setID(new ObjectId());
        } else {
            entry.setID(new ObjectId(id));
        }
        return entry;
    }

    private String sy;

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
    }
}
