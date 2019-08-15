package com.fulaan.utils.RESTAPI.bo.new33.isolate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fulaan.new33.dto.isolate.GradeDTO;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.TermEntry;
import com.sys.utils.JsonUtil;
import org.bson.types.ObjectId;

import com.fulaan.utils.RESTAPI.base.BaseAPI;
import org.json.JSONObject;

public class isolateAPI extends BaseAPI{

	public static String getIsolateUserBySid(String schoolId) {
        String reUrl = "/users/getIsNotParentAndStaffBySchool/" + schoolId;
        return getForObject(reUrl);
    }

    public static String getIsolateUserByGid(String schoolId,String gradeId) {
        String reUrl = "/users/getIsNotParentAndStaffByGradeId/" + schoolId+"/"+gradeId;
        return getForObject(reUrl);
    }
	
	/**
	 * 根据学生的姓名或者学籍号查找学生
	 * @param userNameOrCn
	 * @param schoolId
	 * @return
	 */
	public static String getIsolateStudentByNameOrCn(String userNameOrCn,ObjectId schoolId,ObjectId gid,ObjectId classId) {
        String reUrl = "/users/getStudentByNameOrCn/" + userNameOrCn +"/" + schoolId + "/" + gid + "/" + classId;
        return getForObject(reUrl);
    }
	
	/**
	 * 查询2.0中除家长和学生以外的用户
	 * @param userNameOrCn
	 * @param schoolId
	 * @return
	 */
	public static String getTeaListByName(String userNameOrCn,ObjectId schoolId) {
		if(userNameOrCn == "" || userNameOrCn == null){
			userNameOrCn = "*";
		}
        String reUrl = "/users/getTeaListByName/" + userNameOrCn +"/" + schoolId;
        return getForObject(reUrl);
    }

    public static String getIsolateTerm(String schoolId) {
        String reUrl = "/term/getIsolateTerm/" + schoolId;
        return getForObject(reUrl);
    }

    public static String addNewIsolateTerm(Map map) {
        String reUrl = "/term/addNewIsolateTerm";
        return postForObject(reUrl,map);
    }

    public static String getIsolateSubject(String schoolId) {
        String reUrl = "/subject/getIsolate/" + schoolId;
        return getForObject(reUrl);
    }

    public static String getIsolateClass(String schoolId) {
        String reUrl = "/class/getIsolateClass/" + schoolId;
        return getForObject(reUrl);
    }

    public static String getIsolateClass(String schoolId,String gradeId) {
        String reUrl = "/class/getIsolateClass/" + schoolId+"/"+gradeId;
        return getForObject(reUrl);
    }

    public static String getIsolateRoom(String schoolId) {
        String reUrl = "/classroom/getIsolateRoom/" + schoolId;
        return getForObject(reUrl);
    }

    public static String getIsolateGrade(String schoolId) {
        String reUrl = "/grade/getIsolateGrade/" + schoolId;
        return getForObject(reUrl);
    }

    public static List<Map<String, Object>> getIsolateGradeList(String schoolId) {
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        try {
            String result = getIsolateGrade(schoolId);
            JSONObject resultJson = new JSONObject(result);
            Map map1 = (Map) JsonUtil.JSONToObj(resultJson.toString(), Map.class);
            reList = (List<Map<String, Object>>) map1.get("message");
        }catch (Exception e){
            e.printStackTrace();
        }
        return reList;
    }

    public static String getSchool(String schoolId) {
        String reUrl = "/school/id/" + schoolId;
        return getForObject(reUrl);
    }



    public static String addNewIsolateRoom(Map map) {
        String reUrl = "/classroom/addNewIsolateRoom";
        return postForObject(reUrl, map);
    }
    
    /***新3+3****/
    public static String selClassInfoByGradeId(String gradeId) {
    	String resoureUrl = "/class/get/3/" + gradeId;
        return getForObject(resoureUrl);
    }

    public static String selSubjectList(String sid){
        String resoureUrl = "/subject/get/" + sid;
        return getForObject(resoureUrl);
    }
    public static String getSubjectEntry(String id){
        String resoureUrl = "/subject/id/" + id;
        return getForObject(resoureUrl);
    }
}
