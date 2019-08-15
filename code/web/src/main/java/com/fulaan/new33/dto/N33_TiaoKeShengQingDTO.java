package com.fulaan.new33.dto;

import com.pojo.new33.N33_TiaoKeShengQingEntry;
import org.bson.types.ObjectId;

/**
 * Created by albin on 2018/8/3.
 */
public class N33_TiaoKeShengQingDTO {
    private String sid;
    private String xqid;
    private String jxbId;
    private String jxbName;
    private String njxbName;
    private String njxbId;
    private Integer week;
    private Integer x;
    private Integer y;
    private Integer nx;
    private Integer ny;
    private Integer sta;
    private String teaId;
    private String st;
    private String id;
    private String teaName;

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getJxbName() {
        return jxbName;
    }

    public void setJxbName(String jxbName) {
        this.jxbName = jxbName;
    }

    public String getNjxbName() {
        return njxbName;
    }

    public void setNjxbName(String njxbName) {
        this.njxbName = njxbName;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public N33_TiaoKeShengQingDTO() {
    }

    public N33_TiaoKeShengQingDTO(String sid, String xqid, String jxbId, String jxbName, String njxbName, String njxbId, Integer x, Integer y, Integer nx, Integer ny, Integer sta, String teaId, String st, String id,Integer week) {
        this.sid = sid;
        this.xqid = xqid;
        this.jxbId = jxbId;
        this.jxbName = jxbName;
        this.njxbName = njxbName;
        this.njxbId = njxbId;
        this.x = x;
        this.y = y;
        this.nx = nx;
        this.ny = ny;
        this.sta = sta;
        this.teaId = teaId;
        this.st = st;
        this.id = id;
        this.week=week;
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getXqid() {
        return xqid;
    }

    public void setXqid(String xqid) {
        this.xqid = xqid;
    }

    public String getJxbId() {
        return jxbId;
    }

    public void setJxbId(String jxbId) {
        this.jxbId = jxbId;
    }

    public String getNjxbId() {
        return njxbId;
    }

    public void setNjxbId(String njxbId) {
        this.njxbId = njxbId;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getNx() {
        return nx;
    }

    public void setNx(Integer nx) {
        this.nx = nx;
    }

    public Integer getNy() {
        return ny;
    }

    public void setNy(Integer ny) {
        this.ny = ny;
    }

    public Integer getSta() {
        return sta;
    }

    public void setSta(Integer sta) {
        this.sta = sta;
    }

    public N33_TiaoKeShengQingEntry BuildEntry() {
        N33_TiaoKeShengQingEntry tiaoKeShengQingEntry = new N33_TiaoKeShengQingEntry(new ObjectId(sid), new ObjectId(xqid), x, y, new ObjectId(jxbId), njxbId == null ? null : new ObjectId(njxbId), sta, nx, ny, new ObjectId(teaId),week);
        if (!id.equals("*")) {
            tiaoKeShengQingEntry.setID(new ObjectId(id));
        }
        return tiaoKeShengQingEntry;
    }

    public N33_TiaoKeShengQingDTO(N33_TiaoKeShengQingEntry entry) {
        this.sid = entry.getSchoolId().toString();
        this.xqid = entry.getXqId().toHexString();
        this.jxbId = entry.getJxbId().toHexString();
        this.njxbId = entry.getNJxbId() == null ? "" : entry.getNJxbId().toHexString();
        this.x = entry.getX();
        this.y = entry.getY();
        this.nx = entry.getNX();
        this.ny = entry.getNY();
        this.sta = entry.getSta();
        this.teaId = entry.getTeaId().toHexString();
        this.id = entry.getID().toString();
        this.week=entry.getWeek();
        this.jxbName = "";
        this.njxbName = "";
        this.st = "";
    }
}
