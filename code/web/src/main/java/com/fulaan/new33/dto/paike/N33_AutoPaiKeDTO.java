package com.fulaan.new33.dto.paike;

import com.db.new33.paike.PaiKeXyDto;
import com.pojo.new33.paike.N33_AutoPaiKeEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2018/7/9.
 */
public class N33_AutoPaiKeDTO {
    private String id;
    private String xqid;
    private String sid;
    private Integer type;
    private Integer lev;
    private Integer status;
    private String gid;
    private List<String> tea;
    private List<String> sub;
    private List<PaiKeXyDto> xyList;
    private String teaStr;

    public String getTeaStr() {
        return teaStr;
    }

    public void setTeaStr(String teaStr) {
        this.teaStr = teaStr;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public List<String> getTea() {
        return tea;
    }

    public void setTea(List<String> tea) {
        this.tea = tea;
    }

    public List<String> getSub() {
        return sub;
    }

    public void setSub(List<String> sub) {
        this.sub = sub;
    }

    public List<PaiKeXyDto> getXyList() {
        return xyList;
    }

    public void setXyList(List<PaiKeXyDto> xyList) {
        this.xyList = xyList;
    }

    public N33_AutoPaiKeDTO() {
    }

    public N33_AutoPaiKeDTO(N33_AutoPaiKeEntry paiKeEntry) {
        this.id = paiKeEntry.getID().toString();
        this.sid = paiKeEntry.getSId().toString();
        this.xqid = paiKeEntry.getXqId().toHexString();
        this.gid = paiKeEntry.getGradeId().toHexString();
        this.type = paiKeEntry.getType();
        this.lev = paiKeEntry.getLev();
        this.status = paiKeEntry.getStatus();
        this.tea = MongoUtils.convertToStringList(paiKeEntry.getTeaIds());
        this.sub = MongoUtils.convertToStringList(paiKeEntry.getSubIds());
        List<PaiKeXyDto> xyDtoList = new ArrayList<PaiKeXyDto>();
        for (N33_AutoPaiKeEntry.PaiKeXy xy : paiKeEntry.getXYList()) {
            PaiKeXyDto xyDto = new PaiKeXyDto();
            xyDto.setX(xy.getX());
            xyDto.setY(xy.getY());
            xyDtoList.add(xyDto);
        }
        this.xyList = xyDtoList;
    }

    public N33_AutoPaiKeEntry getEntry() {
        if (id.equals("*")) {
            id = new ObjectId().toString();
        }
        List<N33_AutoPaiKeEntry.PaiKeXy> xyDtoList = new ArrayList<N33_AutoPaiKeEntry.PaiKeXy>();
        for (PaiKeXyDto xy : xyList) {
            N33_AutoPaiKeEntry.PaiKeXy xyDto = new N33_AutoPaiKeEntry.PaiKeXy();
            xyDto.setX(xy.getX());
            xyDto.setY(xy.getY());
            xyDtoList.add(xyDto);
        }
        N33_AutoPaiKeEntry paiKeEntry = new N33_AutoPaiKeEntry(new ObjectId(sid), new ObjectId(xqid), type, MongoUtils.convertToObjectIdList(tea), MongoUtils.convertToObjectIdList(sub), status, xyDtoList, lev, new ObjectId(gid));
        paiKeEntry.setID(new ObjectId(id));
        return paiKeEntry;
    }
}
