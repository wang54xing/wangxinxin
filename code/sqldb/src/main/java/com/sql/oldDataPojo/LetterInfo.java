package com.sql.oldDataPojo;

import java.util.Date;

/**
 * Created by qinbo on 15/3/31.
 */
public class LetterInfo {
    private int id;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    private String sender;
    private String recipient;
    private String message;
    private Date sendingtime;
    private int replyId;
    private int senderdelete;
    private int recipientdelete;
    private int isread;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendingtime() {
        return sendingtime;
    }

    public void setSendingtime(Date sendingtime) {
        this.sendingtime = sendingtime;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getSenderdelete() {
        return senderdelete;
    }

    public void setSenderdelete(int senderdelete) {
        this.senderdelete = senderdelete;
    }

    public int getRecipientdelete() {
        return recipientdelete;
    }

    public void setRecipientdelete(int recipientdelete) {
        this.recipientdelete = recipientdelete;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
