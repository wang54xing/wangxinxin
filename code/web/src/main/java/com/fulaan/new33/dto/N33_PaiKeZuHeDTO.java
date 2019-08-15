package com.fulaan.new33.dto;

import com.pojo.new33.paike.N33_PaiKeZuHeEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by albin on 2018/7/11.
 */
public class N33_PaiKeZuHeDTO {

    private String id;

    private String name;

    private String schoolId;

    private String termId;

    private List<String> jxbIds;

    private Integer type;

    private String gradeId;

    private List<Map<String, Object>> jxbList;

    private Integer pkCount;

    public N33_PaiKeZuHeDTO() {
    }

    public N33_PaiKeZuHeDTO(String name, String schoolId, String termId, List<String> jxbIds, Integer type, String gradeId) {
        this.name = name;
        this.schoolId = schoolId;
        this.termId = termId;
        this.jxbIds = jxbIds;
        this.type = type;
        this.gradeId = gradeId;
    }

    public N33_PaiKeZuHeDTO(N33_PaiKeZuHeEntry entry) {
        this.id = entry.getID().toString();
        this.gradeId = entry.getGId().toString();
        this.name = entry.getName();
        this.schoolId = entry.getSchoolId().toString();
        this.termId = entry.getTermId().toString();
        this.jxbIds = MongoUtils.convertToStringList(entry.getJxbIds());
        this.type = entry.getType();
        this.pkCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Map<String, Object>> getJxbList() {
        return jxbList;
    }

    public void setJxbList(List<Map<String, Object>> jxbList) {
        this.jxbList = jxbList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public Integer getPkCount() {
        return pkCount;
    }

    public void setPkCount(Integer pkCount) {
        this.pkCount = pkCount;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public List<String> getJxbIds() {
        return jxbIds;
    }

    public void setJxbIds(List<String> jxbIds) {
        this.jxbIds = jxbIds;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public N33_PaiKeZuHeEntry buildEntry() {
        if (jxbIds.size() <= 0 || jxbIds == null) {
            jxbIds = new ArrayList<String>();
        }
        N33_PaiKeZuHeEntry entry = new N33_PaiKeZuHeEntry(name, new ObjectId(schoolId), new ObjectId(termId), MongoUtils.convertToObjectIdList(jxbIds), type, new ObjectId(gradeId));
        return entry;
    }
}
