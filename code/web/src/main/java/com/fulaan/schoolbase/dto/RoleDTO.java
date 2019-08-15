package com.fulaan.schoolbase.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/12/8.
 */
public class RoleDTO {

    private String id;
    private String name;
    private String desc;
    private String schoolId;
    private int maxNum;
    private int category;
    private long groupTypeId;
    private List<Integer> identities = new ArrayList<Integer>();
    private String createrId;
    private String createDate;
    private String endDate;
    private boolean checked;
    private String remark;
    private boolean disable;

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

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
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

    public List<Integer> getIdentities() {
        return identities;
    }

    public void setIdentities(List<Integer> identities) {
        this.identities = identities;
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean getDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
