package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/4/15.
 */
public class GroupChatInfo {
    private int id;
    private String roomid;
    private int groupUserid;
    private String chatContent;
    private Date sendDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public int getGroupUserid() {
        return groupUserid;
    }

    public void setGroupUserid(int groupUserid) {
        this.groupUserid = groupUserid;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}
