package com.fulaan.schoolbase.service;

import com.fulaan.schoolbase.dto.*;
import com.fulaan.utils.RESTAPI.bo.group.GroupAPI;
import com.fulaan.utils.RESTAPI.bo.notice.NoticeBaseAPI;
import com.fulaan.utils.RESTAPI.ta.notice.NoticeAPI;
import com.pojo.utils.MongoUtils;
import com.sys.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2017/1/25.
 */

@Service
public class GradeService {

    /**
     * 添加班级
     * @param grade
     * @return
     */
    public String addGrade(GradeDTO grade) {
        String result = NoticeBaseAPI.addGrade(grade);
        if (!StringUtils.isEmpty(grade.getGradeUserId())) {
            try {
                JSONObject dataJson = new JSONObject(result);
                JSONObject rows = dataJson.getJSONObject("message");
                GradeDTO dto = (GradeDTO) JsonUtil.JSONToObj(rows.get("dto").toString(), GradeDTO.class);
                if (dto!=null && !StringUtils.isEmpty(dto.getId())) {
                    Map map = new HashMap();
                    map.put("groupId",dto.getId());
                    map.put("roleName","年级组长");
                    map.put("userId",grade.getGradeUserId());
                    result = NoticeAPI.addOrEditUserRoleInGroup(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 更新年级名称
     * @param gradeIds
     * @param gradeNames
     * @return
     */
    public String updateGrades(String gradeIds, String gradeNames) {
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setGradeIds(gradeIds);
        gradeDTO.setGradeNames(gradeNames);
        return NoticeBaseAPI.updateGrades(gradeDTO);
    }

    /**
     * 查询年级
     * @param schoolId
     * @return
     */
    public List<GradeDTO> selGradeList(ObjectId schoolId) {
        String result = NoticeBaseAPI.selGradeList(schoolId);
        List<GradeDTO> dtos = new ArrayList<GradeDTO>();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONArray rows = dataJson.getJSONArray("message");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    GradeDTO dto = (GradeDTO) JsonUtil.JSONToObj(info.toString(), GradeDTO.class);
                    dtos.add(dto);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return dtos;
    }


    public String getGradeInfoList(String schoolId, String startYear) {
        String result = NoticeBaseAPI.getGradeInfoList(schoolId, startYear);
        return result;
    }

    /**
     * 查询历史年级
     * @param schoolId
     * @return
     */
    public List<GradeDTO> selHistroyGradeList(ObjectId schoolId) {
        String result = NoticeBaseAPI.selHistroyGradeList(schoolId);
        List<GradeDTO> dtos = new ArrayList<GradeDTO>();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONArray rows = dataJson.getJSONArray("message");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    GradeDTO dto = (GradeDTO) JsonUtil.JSONToObj(info.toString(), GradeDTO.class);
                    dtos.add(dto);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return dtos;
    }
    /**
     * 查询年级
     * @param id
     * @return
     */
    public GradeDTO selGradeInfo(ObjectId id) {
        String result = NoticeBaseAPI.selGradeInfo(id);
        List<String> gradeIds = new ArrayList<String>();
        gradeIds.add(id.toString());
        String result2 = GroupAPI.getGroupRoleUserIds(gradeIds, "年级组长");
        GradeDTO dto = new GradeDTO();
        try {
            JSONObject usersJson2 = new JSONObject(result2);
            JSONObject rows2 = usersJson2.getJSONObject("message");
            JSONObject dataJson = new JSONObject(result);
            JSONObject rows = dataJson.getJSONObject("message");
            dto = (GradeDTO) JsonUtil.JSONToObj(rows.toString(), GradeDTO.class);
            dto.setGradeUserId(rows2.getString(id.toString()));
            dto.setType(PeriodType.getPeriodType(dto.getType()).getName());
        } catch (Exception e) {
            e.getMessage();
        }
        return dto;
    }

    /**
     * 创建年级
     * @param grade
     */
    public void updateGrade(GradeDTO grade) {
        NoticeBaseAPI.updateGrade(grade);
        Map map = new HashMap();
        map.put("groupId",grade.getId());
        map.put("roleName","年级组长");
        map.put("userId",grade.getGradeUserId());
        NoticeAPI.addOrEditUserRoleInGroup(map);
    }

    /**
     * 年级老师
     * @param id
     * @param schoolId
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public Map getGradeTeacher(ObjectId id, ObjectId schoolId, String keyword, int page, int pageSize) {
        Map map = new HashMap();
        String result = NoticeBaseAPI.getGradeTeacher(id, schoolId, keyword, page, pageSize);
        List<ResumeDTO> dtos = new ArrayList<ResumeDTO>();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONObject reMap = dataJson.getJSONObject("message");
            JSONArray rows = reMap.getJSONArray("rows");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    ResumeDTO dto = (ResumeDTO) JsonUtil.JSONToObj(info.toString(), ResumeDTO.class);
                    dtos.add(dto);
                }
            }
            map.put("rows",dtos);
            map.put("count",reMap.getInt("count"));
            map.put("page", page);
            map.put("pageSize", pageSize);
        } catch (Exception e) {
            e.getMessage();
        }

        return map;
    }

    /**
     * 查询老师通过年级学科
     * @param schoolId
     * @param keyword
     * @param gradeId
     * @param subjectId
     * @return
     */
    public List<ResumeDTO> selTeachersByGradeSubject(ObjectId schoolId, String keyword, String gradeId, String subjectId) {
        String result = NoticeBaseAPI.selTeachersByGradeSubject(schoolId, keyword, new ObjectId(gradeId), subjectId);
        List<ResumeDTO> dtos = new ArrayList<ResumeDTO>();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONArray rows = dataJson.getJSONArray("message");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    ResumeDTO dto = (ResumeDTO) JsonUtil.JSONToObj(info.toString(), ResumeDTO.class);
                    dtos.add(dto);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return dtos;
    }

    /**
     * 删除年级老师
     * @param id
     */
    public void delGradeTeacher(String id) {
//        RestAPIUtil.delGradeTeacher(new ObjectId(id));
        GroupAPI.deleteTeaGroupSubject(id);
    }

    /**
     * 添加年级老师
     * @param grade
     */
    public void addGradeTeacher(GradeDTO grade) {
//        RestAPIUtil.addGradeTeacher(grade);
        TeacherGroupSubjectDTO dto = new TeacherGroupSubjectDTO();
        dto.setSubjectId(grade.getSubjectIds());
        dto.setTeacherId(grade.getUserIds());
        dto.setGroupId(grade.getId());
        GroupAPI.addTeacherGroupSubject(dto);
        if (StringUtils.isNotEmpty(grade.getUserIds())) {
            GroupUsersDTO groupUsersDTO = new GroupUsersDTO();
            groupUsersDTO.setGroupId(grade.getId());
            List<String> teachers = new ArrayList<String>();
            teachers.addAll(MongoUtils.convertToStringList(MongoUtils.convert(grade.getUserIds())));
            groupUsersDTO.setTeacherIds(teachers);
            GroupAPI.addUsersToGroup(groupUsersDTO);
        }


    }

    /**
     * 设置年级组长
     * @param dto
     */
    public void setGradeLeader(GradeDTO dto) {
        Map map = new HashMap();
        map.put("groupId",dto.getId());
        map.put("roleName","年级组长");
        map.put("userId",dto.getGradeUserId());
        NoticeAPI.addOrEditUserRoleInGroup(map);
    }

    public String selStuGradeList(ObjectId schoolId, ObjectId userId) {
        return GroupAPI.getGradeGroupInfos(schoolId.toString(), userId.toString());
    }
    
    public String upgrade(ObjectId schoolId) {
    	
    	    return NoticeBaseAPI.upgrade(schoolId);
    }
}
