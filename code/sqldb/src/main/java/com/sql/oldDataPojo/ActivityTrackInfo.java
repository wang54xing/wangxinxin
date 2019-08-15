package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/9.
 */
public class ActivityTrackInfo {

    private int id;
    private int relateId;
    private int type;
    private int userId;
    private Date createTime;
    private int fromDevice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRelateId() {
        return relateId;
    }

    public void setRelateId(int relateId) {
        this.relateId = relateId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getFromDevice() {
        return fromDevice;
    }

    public void setFromDevice(int fromDevice) {
        this.fromDevice = fromDevice;
    }
}
