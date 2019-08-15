package com.fulaan.new33.dto.isolate;

import com.pojo.new33.paike.N33_SWLBEntry;

/**
 * Created by albin on 2018/3/13.
 */
public class N33_SWLBDTO {
    private String id;
    private String sid;
    private String desc;
    private String xqid;

    public N33_SWLBDTO(String id, String sid, String desc, String xqid) {
        this.id = id;
        this.sid = sid;
        this.desc = desc;
        this.xqid = xqid;
    }

    public N33_SWLBDTO() {
    }

    public N33_SWLBDTO(N33_SWLBEntry swlbEntry) {
        this.id = swlbEntry.getID().toString();
        this.desc = swlbEntry.getDesc();
        this.sid = swlbEntry.getSchoolId().toString();
        this.xqid = swlbEntry.getTermId().toHexString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getXqid() {
        return xqid;
    }

    public void setXqid(String xqid) {
        this.xqid = xqid;
    }
}
