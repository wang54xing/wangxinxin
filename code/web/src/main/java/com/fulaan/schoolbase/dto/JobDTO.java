package com.fulaan.schoolbase.dto;

import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2017/1/22.
 */
public class JobDTO {

    private String id;

    private String organization;

    private String startTime;

    private String endTime;

    private String userId;

    private String detail;

    public JobDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
