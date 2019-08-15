package com.fulaan.utils.RESTAPI.bo.user;

import com.fulaan.utils.RESTAPI.base.BaseAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2017/1/5.
 */
public class UserAPI extends BaseAPI {

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public static String getUserInfoById(String userId) {
        String reUrl = "/users/infos/" + userId;
        return getForObject(reUrl);
    }

    public static String getUserRole(String userId) {
        String reUrl = "/users/userRole/" + userId;
        return getForObject(reUrl);
    }

    public static String getUserRole(String schoolId, String userName) {
        String reUrl = "/users/getTeachersByUserName/" + schoolId + "/" + userName;
        return getForObject(reUrl);
    }

    public static String getUserMapInfosByIds(List<String> userIds) {
        Map map = new HashMap();
        map.put("userIds",userIds);
        String reUrl = "/users/getUserMapInfosByIds";
        return postForObject(reUrl,map);
    }

    public static String getUserInfos(String schoolId, int identity, String userName) {
        String reUrl = "/users/getUserInfos/2/" + schoolId + "/" + identity + "/" + userName;
        return getForObject(reUrl);
    }
    /**
     * 教职工
     * @param schoolId
     * @return
     */
    public static String getStaffUsers(String schoolId) {
        String reUrl = "/users/getTeachers/"+schoolId;
        return getForObject(reUrl);
    }

    /**
     *
     * @param userId
     * @param userName
     * @param telphone
     * @param email
     * @return
     */
    public static String updateUserInfo(String userId, String userName, String telphone, String email,int sex) {
        Map map = new HashMap();
        map.put("userId",userId);
        map.put("userName",userName);
        map.put("phone",telphone);
        map.put("email",email);
        map.put("sex",String.valueOf(sex));
        String reUrl = "/users/updateUserInfo";
        return postForObject(reUrl,map);
    }

    /**
     *
     * @param userId
     * @param orgpwd
     * @param pwd
     * @return
     */
    public static String updatePassword(String userId, String orgpwd, String pwd) {
        Map map = new HashMap();
        map.put("userId",userId);
        map.put("orgpwd",orgpwd);
        map.put("pwd",pwd);
        String reUrl = "/users/updatePassword";
        return postForObject(reUrl,map);
    }

    /**
     *
     * @param schoolId
     * @param userName
     * @return
     */
    public static String getUsersByUserName(String schoolId,String userName) {
        String reUrl = "/users/getUsersByUserName/"+schoolId+"/"+userName;
        return getForObject(reUrl);
    }
}
