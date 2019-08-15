package com.fulaan.schoolbase.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/12/8.
 */
public class GroupDTO {

    private String id;
    private String name;
    private String desc;
    private String schoolId;
    private String owner;
    private int category;
    private long groupTypeId;
    private String groupTypeName;
    private String parentId;
    private List<String> parentIds = new ArrayList<String>();
    private String createrId;
    private String createDate;
    private String iconUrl;
    private String slogan;
    private String endDate;
    private int mark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public long getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(long groupTypeId) {
        this.groupTypeId = groupTypeId;
    }

    public String getGroupTypeName() {
        return groupTypeName;
    }

    public void setGroupTypeName(String groupTypeName) {
        this.groupTypeName = groupTypeName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
