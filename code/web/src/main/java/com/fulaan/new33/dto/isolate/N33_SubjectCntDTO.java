package com.fulaan.new33.dto.isolate;

public class N33_SubjectCntDTO {

    private String subjectId;

    private String subjectName;

    private int longCnt;

    private int shortCnt;

    private int allCnt;

    private int type;

    public N33_SubjectCntDTO(String subid, String snm,int type) {
        this.subjectId = subid;
        this.subjectName = snm;
        this.shortCnt = 0;
        this.longCnt = 0;
        this.allCnt = 0;
        this.type = type;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getLongCnt() {
        return longCnt;
    }

    public void setLongCnt(int longCnt) {
        this.longCnt = longCnt;
    }

    public int getShortCnt() {
        return shortCnt;
    }

    public void setShortCnt(int shortCnt) {
        this.shortCnt = shortCnt;
    }

    public int getAllCnt() {
        return allCnt;
    }

    public void setAllCnt(int allCnt) {
        this.allCnt = allCnt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
