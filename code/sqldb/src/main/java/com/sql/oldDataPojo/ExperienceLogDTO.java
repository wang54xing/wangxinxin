package com.sql.oldDataPojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pojo.user.UserExperienceLogEntry.ExperienceLog;
import com.sys.utils.CustomDateSerializer;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by guojing on 2015/3/31.
 */
public class ExperienceLogDTO {
    private String experiencename;

    private int experience;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createtime;

    private int scoreType;

    private String relateId;

    public ExperienceLogDTO(){

    }
    public ExperienceLogDTO(ExperienceLog experienceLog) {
        this.experiencename = experienceLog.getDes();
        this.experience = experienceLog.getExperience();
        this.createtime = new Date(experienceLog.getTime());
        this.scoreType=experienceLog.getExpLogTypeOrdinal();
        this.relateId = experienceLog.getRelariveId().toString();

    }

    public String getExperiencename() {
        return experiencename;
    }

    public void setExperiencename(String experiencename) {
        this.experiencename = experiencename;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public int getScoreType() {
        return scoreType;
    }

    public void setScoreType(int scoreType) {
        this.scoreType = scoreType;
    }

    public ExperienceLog buildExperienceLog()
    {
        long createtime=this.getCreatetime()==null?0:this.getCreatetime().getTime();
        //ObjectId relateId=this.getRelateId()==null?null:new ObjectId(this.getRelateId());
        ObjectId relateId=null;
        ExperienceLog scoreLog=new ExperienceLog(
                this.getExperiencename(),
                this.getExperience(),
                createtime,
                this.getScoreType(),
                relateId
        );
        return scoreLog;
    }
}
