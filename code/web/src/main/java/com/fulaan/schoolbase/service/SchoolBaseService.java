package com.fulaan.schoolbase.service;

import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.db.school.TeacherClassSubjectDao;
import com.db.user.UserDao;
import com.fulaan.schoolbase.dto.ClassInfoDTO;
import com.fulaan.schoolbase.dto.*;
import com.fulaan.schoolbase.dto.SchoolDTO;
import com.fulaan.utils.RESTAPI.bo.group.GroupAPI;
import com.fulaan.utils.RESTAPI.bo.notice.NoticeBaseAPI;
import com.pojo.school.*;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.JsonUtil;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wang_xinxin on 2017/1/18.
 */
@Service
public class SchoolBaseService {

    UserDao userDao = new UserDao();
    SchoolDao schoolDao = new SchoolDao();
    TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();
    ClassDao classDao = new ClassDao();
    /**
     * 添加学校
     * @param schoolDTO
     * @return
     */
    public String addSchool(SchoolDTO schoolDTO) {
        setPeriods(schoolDTO);
        return NoticeBaseAPI.addSchool(schoolDTO);
    }

    /**
     *
     * @param schoolDTO
     */
    private void setPeriods(SchoolDTO schoolDTO) {
        List<SchoolPeriod> periods = new ArrayList<SchoolPeriod>();
        SchoolPeriod schoolPeriod = null;
//        if (schoolDTO.getPrimary()!=0) {
            schoolPeriod = new SchoolPeriod();
            schoolPeriod.setPeriod(PeriodType.PRIMARY.getType());
            schoolPeriod.setYear(schoolDTO.getPrimary());
            periods.add(schoolPeriod);
//        }
//        if (schoolDTO.getMiddle()!=0) {
            schoolPeriod = new SchoolPeriod();
            schoolPeriod.setPeriod(PeriodType.JUNIOR.getType());
            schoolPeriod.setYear(schoolDTO.getMiddle());
            periods.add(schoolPeriod);
//        }
//        if (schoolDTO.getHigh()!=0) {
            schoolPeriod = new SchoolPeriod();
            schoolPeriod.setPeriod(PeriodType.SENIOR.getType());
            schoolPeriod.setYear(schoolDTO.getHigh());
            periods.add(schoolPeriod);
//        }
        schoolDTO.setPeriods(periods);
    }

    /**
     * 查询一个学校
     * @param id
     * @return
     */
    public String getSchool(ObjectId id) {
        String result = NoticeBaseAPI.getSchool(id);
        return result;
    }

    /**
     * 删除学校
     * @param id
     */
    public void delSchool(ObjectId id) {
        NoticeBaseAPI.delSchool(id);
    }

    /**
     * 跟新学校
     * @param schoolDTO
     */
    public void updateSchool(SchoolDTO schoolDTO) {
        setPeriods(schoolDTO);
        NoticeBaseAPI.updateSchool(schoolDTO);
    }

    /**
     * 查询学校及所有分校
     * @param schoolId
     * @return
     */
    public String selSchoolList(ObjectId schoolId,String keyword) {
        return NoticeBaseAPI.selSchoolList(schoolId,keyword);
    }

    /**
     * 检查用户名
     * @param userName
     * @param schoolId
     * @return
     */
    public String checkUserName(String userName, ObjectId schoolId) {
        String result = NoticeBaseAPI.checkUserName(schoolId,userName);
        String userId = null;
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONArray rows = dataJson.getJSONArray("message");
            if(rows!=null&&rows.length()>0) {
                userId = rows.getString(0);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return userId;
    }


}
