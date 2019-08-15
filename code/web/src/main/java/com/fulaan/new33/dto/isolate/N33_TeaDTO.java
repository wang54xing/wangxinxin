package com.fulaan.new33.dto.isolate;

import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by albin on 2018/3/8.
 */
public class N33_TeaDTO {
    private String id;
    private String uid;
    private String unm;
    private Integer sex;
    private String sid;
    private String xqid;
    private Integer isBanZhuRen;
    private String isBanName;
    private Integer classNum;
    private List<String> gradeIds;
    private List<String> subjectIds;

    private Integer ks;

    private String gradeStr;
    private String sexStr;
    private String subjectStr;
    private Integer jxbCount;

    private Integer gradeJXBCount;

    private Integer ypks;//教师的已排课时

    public Integer getGradeJXBCount() {
        return gradeJXBCount;
    }

    public void setGradeJXBCount(Integer gradeJXBCount) {
        this.gradeJXBCount = gradeJXBCount;
    }

    public Integer getYpks() {
        return ypks;
    }

    public void setYpks(Integer ypks) {
        this.ypks = ypks;
    }

    public Integer getKs() {
        return ks;
    }

    public void setKs(Integer ks) {
        this.ks = ks;
    }

    public Integer getJxbCount() {
        return jxbCount;
    }

    public void setJxbCount(Integer jxbCount) {
        this.jxbCount = jxbCount;
    }

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    public String getSubjectStr() {
        return subjectStr;
    }

    public void setSubjectStr(String subjectStr) {
        this.subjectStr = subjectStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUnm() {
        return unm;
    }

    public void setUnm(String unm) {
        this.unm = unm;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
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

    public List<String> getGradeIds() {
        return gradeIds;
    }

    public void setGradeIds(List<String> gradeIds) {
        this.gradeIds = gradeIds;
    }

    public List<String> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<String> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getGradeStr() {
        return gradeStr;
    }

    public void setGradeStr(String gradeStr) {
        this.gradeStr = gradeStr;
    }

    public String getIsBanName() {
        return isBanName;
    }

    public void setIsBanName(String isBanName) {
        this.isBanName = isBanName;
    }

    public N33_TeaDTO(String id, String uid, String unm, Integer sex, String sid, String xqid, List<String> gradeIds, List<String> subjectIds) {
        this.id = id;
        this.uid = uid;
        this.unm = unm;
        this.sex = sex;
        this.sid = sid;
        this.xqid = xqid;
        this.gradeIds = gradeIds;
        this.subjectIds = subjectIds;
    }

    public N33_TeaDTO(String id, String uid, String unm, Integer sex, String sid, String xqid, Integer isBanZhuRen, Integer classNum, List<String> gradeIds, List<String> subjectIds, String gradeStr, String sexStr, String subjectStr, Integer jxbCount) {
        this.id = id;
        this.uid = uid;
        this.unm = unm;
        this.sex = sex;
        this.sid = sid;
        this.xqid = xqid;
        this.isBanZhuRen = isBanZhuRen;
        this.classNum = classNum;
        this.gradeIds = gradeIds;
        this.subjectIds = subjectIds;
        this.gradeStr = gradeStr;
        this.sexStr = sexStr;
        this.subjectStr = subjectStr;
        this.jxbCount = jxbCount;
    }

    public Integer getIsBanZhuRen() {
        return isBanZhuRen;
    }

    public void setIsBanZhuRen(Integer isBanZhuRen) {
        this.isBanZhuRen = isBanZhuRen;
    }

    public Integer getClassNum() {
        return classNum;
    }

    public void setClassNum(Integer classNum) {
        this.classNum = classNum;
    }

    public N33_TeaDTO() {
    }

    public N33_TeaDTO(N33_TeaEntry teaEntry) {
        this.id = teaEntry.getID().toString();
        this.uid = teaEntry.getUserId().toString();
        this.unm = teaEntry.getUserName();
        this.sex = teaEntry.getSex();
        this.xqid = teaEntry.getXqid().toHexString();
        this.sid = teaEntry.getSchoolId().toString();
        this.gradeIds = MongoUtils.convertToStringList(teaEntry.getGradeList());
        this.subjectIds = MongoUtils.convertToStringList(teaEntry.getSubjectList());
        this.isBanZhuRen = teaEntry.getIsBanZhuRen();
        this.classNum = teaEntry.getClassNum();
        if (this.isBanZhuRen == 0) {
            this.isBanName = "";
        } else {
            this.isBanName = "是";
        }
    }

    public N33_TeaEntry getEntry() {
        N33_TeaEntry entry = new N33_TeaEntry(
                new ObjectId(sid),
                new ObjectId(xqid),
                new ObjectId(uid),
                unm,
                MongoUtils.convertToObjectIdList(gradeIds),
                sex,
                MongoUtils.convertToObjectIdList(subjectIds), classNum, isBanZhuRen);
        entry.setID(new ObjectId(id));
        return entry;
    }
}
