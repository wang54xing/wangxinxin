package com.fulaan.utils.RESTAPI.bo.group;

import com.fulaan.schoolbase.dto.TeacherGroupSubjectDTO;
import com.fulaan.utils.RESTAPI.base.BaseAPI;
import com.sys.utils.JsonUtil;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2017/1/4.
 */
public class GroupAPI extends BaseAPI {


    /**
     * 查询群组类型
     * @param schoolId
     * @param lid
     * @param category
     * @return
     */
    public static String getGroupTypesByLid(String schoolId, long lid, int category) {
        String reUrl = "/groupType/getGroupTypesByLid/"+schoolId+"/"+lid+"/"+category;
        return getForObject(reUrl);
    }

    /**
     * 查询群组类型
     * @param lid
     * @return
     */
    public static String getGroupTypeByLid(long lid) {
        String reUrl = "/groupType/getGroupTypeByLid/"+lid;
        return getForObject(reUrl);
    }

    /**
     * 查询群组类型
     * @param schoolId
     * @param parentLid
     * @param category
     * @return
     */
    public static String getGroupTypesByParentLid(String schoolId, long parentLid, int category) {
        String reUrl = "/groupType/getGroupTypesByParentLid/"+schoolId+"/"+parentLid+"/"+category;
        return getForObject(reUrl);
    }

    /**
     * 查询群组类型
     * @param id
     * @return
     */
    public static String getGroupTypeById(String id) {
        String reUrl = "/groupType/get/"+id;
        return getForObject(reUrl);
    }

    /**
     * 查询群组
     * @param schoolId
     * @param category
     * @return
     */
    public static String getGroupTypes(String schoolId, int category) {
        String reUrl = "/groupType/getGroupTypes/"+schoolId+"/"+category;
        return getForObject(reUrl);
    }

    /**
     * 查询群组种类
     * @return
     */
    public static String getCategoryList() {
        String reUrl = "/groupType/getCategoryList";
        return getForObject(reUrl);
    }

    /**
     * 查询群组类型成员身份
     * @return
     */
    public static String getGroupTypeIdentities() {
        String reUrl = "/groupType/getGroupTypeIdentities";
        return getForObject(reUrl);
    }

    /**
     * 新增群组类型
     * @param dto
     * @return
     */
    public static String addGroupType(Object dto) {
        String reUrl = "/groupType/addGroupType";
        return postForObject(reUrl, dto);
    }

    /**
     * 修改群组类型
     * @param dto
     * @return
     */
    public static String editGroupType(Object dto) {
        String reUrl = "/groupType/editGroupType";
        return postForObject(reUrl, dto);
    }

    /**
     * 删除群组类型
     * @param id
     * @return
     */
    public static void delGroupType(String id) {
        String reUrl = "/groupType/delete/"+id;
        delete(reUrl);
    }

    /**
     * 获取群组信息
     * @param id
     * @return
     */
    public static String getGroupById(String id) {
        String reUrl = "/group/id/"+id;
        return getForObject(reUrl);
    }
    /**
     * 获取群组信息
     * @param parentId
     * @return
     */
    public static String getGroupsByPid(String parentId) {
        String reUrl = "/group//pid/1/"+parentId;
        return getForObject(reUrl);
    }



    /**
     * 查询群组
     * @param schoolId
     * @param category
     * @return
     */
    public static String getGroupsStr(String schoolId, int category) {
        String reUrl = "/group/getGroups/"+schoolId+"/"+category;
        return getForObject(reUrl);
    }

    /**
     * 查询群组
     * @param schoolId
     * @param category
     * @return
     */
    public static String getGroupListStr(String schoolId, int category) {
        String reUrl = "/group/getGroupList/"+schoolId+"/"+category;
        return getForObject(reUrl);
    }

    /**
     * 查询群组
     * @param groupId
     * @param schoolId
     * @param category
     * @return
     */
    public static String getRelationGroupsStr(String groupId, String schoolId, int category) {
        String reUrl = "/group/getRelationGroups/"+groupId+"/"+schoolId+"/"+category;
        //String reUrl = "/group/getGroups/"+schoolId+"/"+category;
        return getForObject(reUrl);
    }


    /**
     * 添加群组
     * @param dto
     * @return
     */
    public static String addGroup(Object dto) {
        String reUrl = "/group/addGroup";
        return postForObject(reUrl, dto);
    }

    /**
     * 修改群组
     * @param dto
     * @return
     */
    public static String editGroup(Object dto) {
        String reUrl = "/group/editGroup";
        return postForObject(reUrl, dto);
    }

    /**
     * 删除群组
     * @param id
     * @return
     */
    public static void delGroup(String id) {
        String reUrl = "/group/delete/"+id;
        delete(reUrl);
    }

    /**
     * 查询群组成员
      * @param schoolId
     * @param identName
     * @param userName
     * @return
     */
    public static String getGroupUsersByIdent(String schoolId, String identName, String userName) {
        String reUrl = "/groupUser/getGroupUsersByIdent/"+schoolId+"/"+identName+"/"+userName;
        return getTaForObject(reUrl);
    }

    /**
     * 查询群组成员
     * @param schoolId
     * @param identName
     * @param userName
     * @return
     */
    public static String getXunTangTeaUsersByIdent(String schoolId, String identName, String userName) {
        String reUrl = "/groupUser/getXunTangTeaUsersByIdent/"+schoolId+"/"+identName+"/"+userName;
        return getTaForObject(reUrl);
    }

    public static String getExpandRoleUsersByIdent(String schoolId, String identName, String userName) {
        String reUrl = "/groupUser/getExpandRoleUsersByIdent/"+schoolId+"/"+identName+"/"+userName;
        return getTaForObject(reUrl);
    }

    public static String getRoleUsersByIdent(String zid,String schoolId, String identName, String userName) {
        String reUrl = "/groupUser/getRoleUsersByIdent/"+schoolId+"/"+zid+"/"+identName+"/"+userName;
        return getTaForObject(reUrl);
    }

    public static String getRoleUsersByIdent(String zid) {
        String reUrl = "/groupUser/getUsersByGId" +
                "/"+zid;
        return getTaForObject(reUrl);
    }


    /**
     * 查询群组成员
     * @param groupId
     * @param userName
     * @param type （leader：领导、teacher：老师、student：学生、parent：学生家长、staff：校职工、other：其他、*：全部）
     * @return
     */
    public static String getGroupUserDetailInfos(String groupId, String userName, String type) {
        String reUrl = "/groupUser/getGroupUserDetailInfos/"+groupId+"/"+userName+"/"+type;
        return getTaForObject(reUrl);
    }

    public static String getGroupUsersByGroupId(String groupId) {
        String reUrl = "/groupUser/getGroupUsersByGroupId/"+groupId;
        return getTaForObject(reUrl);
    }

    public static String getGroupUsers(String tagGroupId, String groupId, long groupTypeId, int identity) {
        String reUrl = "/groupUser/getGroupUsers/"+tagGroupId+"/"+groupId+"/"+groupTypeId+"/"+identity;
        return getTaForObject(reUrl);
    }

    public static String getGroupUsers(String tagGroupId, long groupTypeId, String groupId, String roleId) {
        String reUrl = "/groupUser/getGroupUsers/1/"+tagGroupId+"/"+groupTypeId+"/"+groupId+"/"+roleId;
        return getTaForObject(reUrl);
    }


    public static String getGroupUsersByRoleId(String groupId, String roleId) {
        String reUrl = "/groupUser/getGroupUsers/2/"+groupId+"/"+roleId;
        return getTaForObject(reUrl);
    }

    /**
     * 年级列表
     * @return
     */
    public static String getGradeGroupInfos(String schoolId, String userId) {
        String reUrl = "/groupUser/getGradeGroupInfos/1/"+schoolId+"/"+userId;
        return getTaForObject(reUrl);
    }

    public static String addUsersToGroup(Object dto) {
        String reUrl = "/groupUser/addUsersToGroup";
        return postTaForObject(reUrl, dto);
    }

    public static void delUserFromGroup(String groupId, String userId) {
        String reUrl = "/groupUser/delUserFromGroup/"+groupId+"/"+userId;
        taDelete(reUrl);
    }

    public static String addOrEditUserRoleInGroup(Map map) {
        String reUrl = "/urig/addOrEdit";
        return postTaForObject(reUrl, map);
    }

    public static String getUserRolesByUserId(String groupId, String userId) {
        String reUrl = "/urig/getUserRolesByUserId/"+groupId+"/"+userId;
        return getTaForObject(reUrl);
    }


    public static String getUserRolesCountByGroupId(String groupId) {
        String reUrl = "/urig/getUserRolesCountByGroupId/"+groupId;
        return getTaForObject(reUrl);
    }

    public static String getUserRolesByGroupId(String groupId) {
        String reUrl = "/urig/get/roleUser/"+groupId;
        return getTaForObject(reUrl);
    }


    public static String getUserRoles(String schoolId, String groupId, String userId, long groupTypeId, int category, int userRole) {
        String reUrl = "/urig/getUserRoles/"+schoolId+"/"+groupId+"/"+userId+"/"+groupTypeId+"/"+category+"/"+userRole;
        return getTaForObject(reUrl);
    }

    public static String addUserRolesInGroup(Map map) {
        String reUrl = "/urig/addUserRolesInGroup";
        return postTaForObject(reUrl, map);
    }

    public static String getUserRoleInGroupId(String groupId, String roleId, String userId) {
        String reUrl = "/urig/getUserRoleInGroupId/"+groupId+"/"+roleId+"/"+userId;
        return getTaForObject(reUrl);
    }

    public static String getRoleUsers(String schoolId,String groupTypeEname) {
        String reUrl = "/urig/getRoleUsers"+"/"+schoolId+"/"+groupTypeEname;
        return getTaForObject(reUrl);
    }

    /**
     * 导入已有学校数据
     * @param map
     */
    public static String exportBaseGroupInfos(Map map) {
        String reUrl = "/group/exportBaseGroupInfos";
        return postForObject(reUrl, map);
    }

    /**
     * 学科列表
     * @return
     */
    public static String getSubjectInfos(String groupId) {
        String reUrl = "/teaGroupSubject/get/22/"+groupId;
        return getTaForObject(reUrl);
    }

    /**
     * 删除年级老师
     */
    public static void deleteTeaGroupSubject(String id) {
        String reUrl = "/teaGroupSubject/delete/"+id;
        taDelete(reUrl);
    }

    /**
     * 添加年级老师
     */
    public static String addTeacherGroupSubject(TeacherGroupSubjectDTO dto) {
        String reUrl = "/teaGroupSubject/addTeacherGroupSubject";
        return postTaForObject(reUrl,dto);
    }

    /**
     * 获取组长角色
     * @param groupIds
     * @return
     */
    public static String getGroupRoleUserIds(List<String> groupIds,String roleName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupIds",groupIds);
        map.put("roleName",roleName);
        String reUrl = "/groupUser/getGroupRoleUserIds";
        return postTaForObject(reUrl, map);
    }


    /**
     * 删除学生角色
     * @param id
     * @param userId
     */
    public static void delRoleUser(String id, String userId) {
        String reUrl = "/urig/delete/2/"+id+"/"+userId;
        taDelete(reUrl);
    }

    /**
     * 删除班级老师角色
     * @param map
     */
    public static void delTeacherRole(Map map) {
        String reUrl = "/teaGroupSubject/delTeacherRole";
        postTaForObject(reUrl,map);
    }

    /**
     *
     * @param groupIds
     * @return
     */
    public static String getGroupUsersByIds(String groupIds) {
        String reUrl = "/groupUser/getGroupUsersByIds/"+groupIds;
        return getTaForObject(reUrl);
    }

    /**
     * @param schoolId
     * @param groupIds
     * @return
     */
    public static String getGroupUserDetails(String schoolId, String groupIds) {
        String reUrl = "/groupUser/getGroupUserDetails/"+schoolId+"/"+groupIds;
        return getTaForObject(reUrl);
    }

    public static void delUserFromGroupByUserId(String userId) {
        String reUrl = "/groupUser/delUser/"+userId;
        taDelete(reUrl);
    }

    public static Map<String, String> getStuClassMap(String schoolId, List<String> stuIds) {
        Map<String, String> reMap = new HashMap<String, String>();
        String reStr =getStuClassMapStr(schoolId, stuIds);
        try {
            JSONObject reJson = new JSONObject(reStr);
            JSONObject objs = reJson.getJSONObject("message");
            reMap = (Map<String, String>) JsonUtil.JSONToObj(objs.toString(), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }

    public static String getStuClassMapStr(String schoolId, List<String> stuIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schoolId", schoolId);
        map.put("stuIds", stuIds);
        String reUrl = "/groupUser/get/stuClassMap";
        return postTaForObject(reUrl, map);
    }
}
