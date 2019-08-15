package com.fulaan.schoolbase.service;

import com.fulaan.schoolbase.dto.ClassInfoDTO;
import com.fulaan.schoolbase.dto.GroupUsersDTO;
import com.fulaan.utils.RESTAPI.bo.group.GroupAPI;
import com.fulaan.utils.RESTAPI.bo.notice.NoticeBaseAPI;
import com.fulaan.utils.RESTAPI.ta.notice.NoticeAPI;
import com.pojo.utils.MongoUtils;
import com.sys.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
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
public class SchoolClassService {

    /**
     *增加班级
     * @param classInfoDTO
     * @return
     */
    public String addClass(ClassInfoDTO classInfoDTO) {
        return NoticeBaseAPI.addClass(classInfoDTO);
    }

    /**
     * 删除班级
     * @param id
     */
    public void delClass(ObjectId id) {
        NoticeBaseAPI.delClass(id);
    }

    /**
     * 更新班级
     * @param classInfoDTO
     */
    public void updateClass(ClassInfoDTO classInfoDTO) {
        NoticeBaseAPI.updateClass(classInfoDTO);

    }

    /**
     * 查询班级通过年级
     * @param gradeId
     */
    public List<ClassInfoDTO> selClassByGradeId(String gradeId) {
        String result = NoticeBaseAPI.selClassByGradeId(gradeId);
        List<ClassInfoDTO> dtos = new ArrayList<ClassInfoDTO>();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONArray rows = dataJson.getJSONArray("message");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    ClassInfoDTO dto = (ClassInfoDTO) JsonUtil.JSONToObj(info.toString(), ClassInfoDTO.class);
                    dtos.add(dto);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return dtos;
    }
    public List<ClassInfoDTO> getClassByGradeId(String gradeId){
    	String result = NoticeBaseAPI.getClassByGradeId(gradeId);
        List<ClassInfoDTO> dtos = new ArrayList<ClassInfoDTO>();
        try {
            JSONObject dataJson = new JSONObject(result);
            JSONArray rows = dataJson.getJSONArray("message");
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    ClassInfoDTO dto = (ClassInfoDTO) JsonUtil.JSONToObj(info.toString(), ClassInfoDTO.class);
                    dtos.add(dto);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return dtos;
    }
    /**
     * 查询班级所有信息通过年级
     * @param gradeId
     */
    public String selClassInfoByGradeId(String gradeId) {
        return NoticeBaseAPI.selClassInfoByGradeId(gradeId);
    }

    /**
     * 查詢班級
     * @param id
     * @return
     */
    public String selClass(ObjectId id) {
        return NoticeBaseAPI.selClass(id);
    }

    /**
     * 设置关联教室
     * 
     * @param id
     * @param roomId
     * @return
     */
    public String setClazzRoom(ObjectId id, ObjectId roomId) {
    	return NoticeBaseAPI.setClazzRoom(id, roomId);
    }
    
    /**
     * 删除班级学生
     * @param id
     * @param userIds
     */
    public void deleteStudents(String id, String userIds) {
        NoticeBaseAPI.delStudents(id, userIds);
    }

    /**
     * 调班
     * @param orgClassId
     * @param classId
     * @param userIds
     */
    public void moveStudents(String orgClassId, String classId, String userIds) {
        if (!orgClassId.equals(classId)) {
            NoticeAPI.moveStudents(orgClassId,classId,userIds);
        }
    }

    /**
     * 添加班委
     * @param classId
     * @param remark
     * @param userIds
     */
    public void addStudentRole(String classId,String roleId, String remark, String userIds) {
        Map map =  new HashMap();
        map.put("groupId",classId);
        map.put("roleId",roleId);
        map.put("userIds", MongoUtils.convertToStringList(MongoUtils.convert(userIds)));
        map.put("roleRemark",remark);
        GroupAPI.addUserRolesInGroup(map);
    }

    /**
     * 设置班主任
     * @param classId
     * @param teacherId
     */
    public void addMaster(String classId, String teacherId,String orgTeacherId) {
        if (StringUtils.isNotEmpty(teacherId)) {
            Map map = new HashMap();
            map.put("groupId",classId);
            map.put("roleName","班主任");
            map.put("userId",teacherId);
            NoticeAPI.addOrEditUserRoleInGroup(map);
            GroupUsersDTO dto = new GroupUsersDTO();
            dto.setGroupId(classId);
            List<String> teachers = new ArrayList<String>();
            teachers.add(teacherId);
            dto.setTeacherIds(teachers);
            GroupAPI.addUsersToGroup(dto);
        }
    }

    /**
     * 删除学生角色
     * @param id
     * @param userId
     */
    public void delStudentRole(String id, String userId) {
        GroupAPI.delRoleUser(id, userId);
    }

    /**
     *  删除老师角色
     * @param ids
     */
    public void delTeacher(String ids) {
        Map map = new HashMap();
        map.put("ids",ids);
        GroupAPI.delTeacherRole(map);
    }

    /**
     *
     * @param id
     * @param type
     */
    public void updateIndex(ObjectId schoolId,String id, int type) {
        NoticeBaseAPI.updateIndex(schoolId.toString(),id,type);
    }
}
