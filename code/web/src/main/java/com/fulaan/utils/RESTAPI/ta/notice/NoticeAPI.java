package com.fulaan.utils.RESTAPI.ta.notice;

import com.fulaan.schoolbase.dto.SubjectDTO;
import com.fulaan.utils.RESTAPI.base.BaseAPI;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2017/3/2.
 */
public class NoticeAPI extends BaseAPI {




    /**
     * 查询通知
     * @param userId
     * @param type
     * @return
     */
    public static String getNoticeReceiverList(String schoolId,String userId, int type,int platform,String keyword,int page,int pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "-1";
        }
        Map map = new HashMap();
        map.put("schoolId",schoolId);
        map.put("userId",userId);
        map.put("type",type+"");
        map.put("platform",platform+"");
        map.put("keyword",keyword);
        map.put("page",page+"");
        map.put("pageSize",pageSize+"");
        String resoureUrl = "/relation/getNotices";
        String resultStr = postTaForObject(resoureUrl, map);
        return resultStr;
    }




    /**
     * 通知信息
     * @param id
     */
    public static String getNoticeInfo(String id,ObjectId userId) {
        String resoureUrl = "/relation/getNotice/"+id+"/"+userId;
        String resultStr = getTaForObject(resoureUrl);
        return resultStr;
    }



    /**
     *
     * @param id
     */
    public static String noticeInvalid(String id) {
        String resoureUrl = "/relation/post/"+id;
        String resultStr = getTaForObject(resoureUrl);
        return resultStr;
    }

    /**
     * 设置学科组长
     * @param dto
     */
    public static String setSubjectLeader(SubjectDTO dto) {
        String resoureUrl = "/subject/post/set";
        return postTaForObject(resoureUrl, dto);
    }

    /**
     *
     * @param userId
     * @param schoolId
     * @return
     */
    public static String getGroupsStr(ObjectId userId,ObjectId schoolId) {
        String resoureUrl = "/groupUser/getGroupsByUserId/"+userId+"/"+schoolId;
        String resultStr=getTaForObject(resoureUrl);
        return resultStr;

    }

    /**
     * 添加年级组长学科组长
     * @param map
     * @return
     */
    public static String addOrEditUserRoleInGroup(Map map) {
        String resoureUrl = "/urig/addOrEditUserRoleInGroup";
        String resultStr=postTaForObject(resoureUrl, map);
        return resultStr;
    }


    /**
     * 学生调班
     * @param orgClassId
     * @param classId
     * @param userIds
     */
    public static String moveStudents(String orgClassId, String classId, String userIds) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("orgClassId",orgClassId);
        map.put("classId",classId);
        map.put("userIds",userIds);
        String resoureUrl = "/groupUser/moveUsersFromGroup";
        String resultStr=postTaForObject(resoureUrl, map);
        return resultStr;
    }

    /**
     * 查询通知
     * @param userId
     * @return
     */
    public static String getAppOneNotice(String schoolId,String userId,int platform) {
        Map map = new HashMap();
        map.put("schoolId",schoolId);
        map.put("userId",userId);
        map.put("platform",platform+"");
        String resoureUrl = "/relation/getAppOneNotice";
        String resultStr = postTaForObject(resoureUrl, map);
        return resultStr;
    }

    public static String addRelation(Map map) {
        String resoureUrl = "/relation/post";
        String resultStr = postTaForObject(resoureUrl, map);
        return resultStr;
    }
    public static String addBoRelation(Map map) {
        String resoureUrl = "/notices/postNotice";
        String resultStr = postForObject(resoureUrl, map);
        return resultStr;
    }

    /**
     *
     * @param schoolId
     * @param classId
     * @param page
     * @param pageSize
     * @return
     */
    public static String getNoticeList(String schoolId, String classId, int page, int pageSize) {

        Map map = new HashMap();
        map.put("schoolId",schoolId);
        map.put("classId",classId);
        map.put("page",page+"");
        map.put("pageSize",pageSize+"");
        String resoureUrl = "/relation/getBPNotices";
        String resultStr = postTaForObject(resoureUrl, map);
        return resultStr;
    }
}
