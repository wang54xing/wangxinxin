package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/15.
 */
public class GroupInfo {
    private int id;
    private String groupname;
    private int maingroup;
    private Date createDate;
    private String roomid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getMaingroup() {
        return maingroup;
    }

    public void setMaingroup(int maingroup) {
        this.maingroup = maingroup;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }
}
