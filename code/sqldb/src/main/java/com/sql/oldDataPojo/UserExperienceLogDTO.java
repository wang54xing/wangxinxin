package com.sql.oldDataPojo;

import com.pojo.user.UserExperienceLogEntry;
import com.pojo.user.UserExperienceLogEntry.ExperienceLog;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/3/31.
 */
public class UserExperienceLogDTO {

    private String userid;

    private List<ExperienceLogDTO> experienceLogs;
    public UserExperienceLogDTO(){

    }

    public UserExperienceLogDTO(UserExperienceLogEntry userExperienceLogEntry) {
        this.userid=userExperienceLogEntry.getUserId().toString();
        // pets
        if(userExperienceLogEntry.getList()!=null && !userExperienceLogEntry.getList().isEmpty())
        {
            this.experienceLogs = new ArrayList<ExperienceLogDTO>();
            for(ExperienceLog experienceLog: userExperienceLogEntry.getList())
            {
                this.experienceLogs.add(new ExperienceLogDTO(experienceLog));
            }
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<ExperienceLogDTO> getExperienceLogs() {
        return experienceLogs;
    }

    public void setExperienceLogs(List<ExperienceLogDTO> experienceLogs) {
        this.experienceLogs = experienceLogs;
    }

    /** 从当前传入的DTO产生Entry
     * @return
     */
    public UserExperienceLogEntry buildUserExperienceLogEntry(){
        List<ExperienceLog> experienceLog=null;
        if(getExperienceLogs()!=null && !getExperienceLogs().isEmpty())
        {
            experienceLog= new ArrayList<ExperienceLog>();
            for(ExperienceLogDTO experienceLogDTO: getExperienceLogs())
            {
                experienceLog.add(experienceLogDTO.buildExperienceLog());
            }
        }
        UserExperienceLogEntry userExperienceLogEntry = new UserExperienceLogEntry(
                new ObjectId(this.getUserid()),
                experienceLog
        );
        return userExperienceLogEntry;
    }
}
