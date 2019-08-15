package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/15.
 */
public class RefGroupUser {
    private String roomid;
    private int userid;
    private int isread;
    private Date updatetime;

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
