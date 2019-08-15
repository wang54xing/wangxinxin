package com.fulaan.new33.dto.isolate;

import com.pojo.new33.isolate.N33_TeaRules;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class N33_TeaRulesDTO {

    private String id;
    private String xqid;
    private String sid;
    private String teaId;
    private List<TeaRulesListDTO> teaRulesList;
    private Integer lev;

    private String teaName;
    private String sexStr;

    private Integer size;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public N33_TeaRulesDTO() {
    }

    public N33_TeaRulesDTO(String id, String xqid, String sid, String teaId, List<TeaRulesListDTO> teaRulesList,Integer lev) {
        this.id = id;
        this.xqid = xqid;
        this.sid = sid;
        this.teaId = teaId;
        this.teaRulesList = teaRulesList;
        this.lev = lev;
    }

    public N33_TeaRulesDTO(String xqid, List<TeaRulesListDTO> teaRulesList) {
        this.xqid = xqid;
        this.teaRulesList = teaRulesList;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
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

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }

    public List<TeaRulesListDTO> getTeaRulesList() {
        return teaRulesList;
    }

    public void setTeaRulesList(List<TeaRulesListDTO> teaRulesList) {
        this.teaRulesList = teaRulesList;
    }

    public N33_TeaRulesDTO(N33_TeaRules entry) {
        this.id = entry.getID().toString();
        this.xqid = entry.getXqid().toString();
        this.sid = entry.getSchoolId().toString();
        this.teaId = entry.getTeaId().toString();
        List<N33_TeaRules.TeaRulesList> teaRulesLists = entry.getTeaRulesList();
        List<TeaRulesListDTO> retList = new ArrayList<TeaRulesListDTO>();
        for (N33_TeaRules.TeaRulesList teaRulesList:teaRulesLists) {
            retList.add(new TeaRulesListDTO(teaRulesList));
        }
        this.teaRulesList = retList;
        this.lev=entry.getLev();
    }

    public N33_TeaRules buildEntry() {
        List<N33_TeaRules.TeaRulesList> teaRulesLists = new ArrayList<N33_TeaRules.TeaRulesList>();
        if(teaRulesList != null){
            for (TeaRulesListDTO teaRulesListDTO : teaRulesList) {
                teaRulesLists.add(teaRulesListDTO.buildEntry());
            }
        }
        N33_TeaRules entry = new N33_TeaRules(new ObjectId(sid), new ObjectId(xqid), new ObjectId(teaId),teaRulesLists, lev);
        return entry;
    }

    public static class TeaRulesListDTO{
        private String id;
        private Integer x;
        private Integer y;
        private String desc;
        private Integer require;
        private Integer status;
        private List<String> gid;

        private String statusDesc;

        private String color;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        public TeaRulesListDTO() {
        }

        public TeaRulesListDTO(String id,Integer x, Integer y,String desc,Integer require,Integer status,List<String> gid) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.desc = desc;
            this.require = require;
            this.status = status;
            this.gid = gid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getRequire() {
            return require;
        }

        public void setRequire(Integer require) {
            this.require = require;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<String> getGid() {
            return gid;
        }

        public void setGid(List<String> gid) {
            this.gid = gid;
        }

        public TeaRulesListDTO(N33_TeaRules.TeaRulesList entry) {
            this.id = entry.getID().toString();
            this.x = entry.getX();
            this.y = entry.getY();
            this.desc = entry.getDesc();
            this.require = entry.getRequire();
            this.gid = MongoUtils.convertToStringList(entry.getGids());
            this.status = entry.getStatus();
        }

        public N33_TeaRules.TeaRulesList buildEntry() {
            N33_TeaRules.TeaRulesList entry = new N33_TeaRules.TeaRulesList(x,y,desc,require,status,MongoUtils.convertToObjectIdList(gid));
            entry.setID(new ObjectId());
            return entry;
        }
    }
}
