package com.fulaan.new33.dto.isolate;

import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by albin on 2018/3/14.
 */
public class N33_SWDTO {
    private String id;
    private Integer x;
    private Integer y;
    private String swlbId;
    private String desc;
    private int level;
    private long startTime;
    private long endTime;
    private String termId;
    private String sid;
    private List<String> tids;
    private String xy;
    private String userName;
    private List<String> names;
    private String TeaName;
    private Integer sk;
    private String reId;



    public N33_SWDTO(String id, Integer x, Integer y, String swlbId, String desc, int level, long startTime, long endTime, String termId, String sid, List<String> tids) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.swlbId = swlbId;
        this.desc = desc;
        this.level = level;
        this.startTime = startTime;
        this.endTime = endTime;
        this.termId = termId;
        this.sid = sid;
        this.tids = tids;
    }

    public N33_SWDTO(String id, Integer x, Integer y, String swlbId, String desc, int level, long startTime, long endTime, String termId, String sid, List<String> tids, String xy, String userName, List<String> names, String teaName, Integer sk) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.swlbId = swlbId;
        this.desc = desc;
        this.level = level;
        this.startTime = startTime;
        this.endTime = endTime;
        this.termId = termId;
        this.sid = sid;
        this.tids = tids;
        this.xy = xy;
        this.userName = userName;
        this.names = names;
        TeaName = teaName;
        this.sk = sk;
    }

    public Integer getSk() {
        return sk;
    }

    public void setSk(Integer sk) {
        this.sk = sk;
    }

    public N33_SWDTO() {
    }



    public N33_SWDTO(N33_SWEntry entry) {
        this.id = entry.getID().toString();
        this.x = entry.getX();
        this.y = entry.getY();
        this.tids = MongoUtils.convertToStringList(entry.getTeacherIds());
        this.swlbId = entry.getSwlbId().toHexString();
        this.desc = entry.getDesc();
        this.level = entry.getLevel();
        this.sid = entry.getSchoolId().toString();
        this.startTime = entry.getStartTime();
        this.endTime = entry.getEndTime();
        this.termId = entry.getTermId().toHexString();
        this.xy=(entry.getY()-1)+""+(entry.getX()-1);
        this.sk=entry.getStudentKe();
        this.reId=entry.getReId()==null?new ObjectId().toHexString():entry.getReId().toHexString();
    }

    public String getReId() {
        return reId;
    }

    public void setReId(String reId) {
        this.reId = reId;
    }

    public String getTeaName() {
        return TeaName;
    }

    public void setTeaName(String teaName) {
        TeaName = teaName;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getXy() {
        return xy;
    }

    public void setXy(String xy) {
        this.xy = xy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getSwlbId() {
        return swlbId;
    }

    public void setSwlbId(String swlbId) {
        this.swlbId = swlbId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<String> getTids() {
        return tids;
    }

    public void setTids(List<String> tids) {
        this.tids = tids;
    }

    public N33_SWEntry buildEntry() {
        N33_SWEntry entry = new N33_SWEntry(x, y, new ObjectId(swlbId), desc, MongoUtils.convertToObjectIdList(tids), level, startTime, endTime, new ObjectId(sid), new ObjectId(termId),sk);
        if (id.equals("*")) {
            entry.setID(new ObjectId());
        } else {
            entry.setID(new ObjectId(id));
        }
        entry.setReId(new ObjectId(reId));
        return entry;
    }
}
