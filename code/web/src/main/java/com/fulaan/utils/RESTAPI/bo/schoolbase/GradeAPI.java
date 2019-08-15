package com.fulaan.utils.RESTAPI.bo.schoolbase;

import com.fulaan.utils.RESTAPI.base.BaseAPI;

/**
 * Created by guojing on 2017/3/22.
 */
public class GradeAPI extends BaseAPI{

    public static String getGradeInfo(String schoolId, int gradeNumber, String gradeType) {
        String reUrl = "/grade/getGradeInfo/"+schoolId+"/"+gradeNumber+"/"+gradeType;
        return getForObject(reUrl);
    }

    public static String getGradeInfo(String schoolId) {
        String reUrl = "/grade/get/1"+schoolId.toString();
        return getForObject(reUrl);
    }
    public static String getGradeInfoG(String gid) {
        String reUrl = "/grade/get/"+gid;
        return getForObject(reUrl);
    }

    public static String getClassInfo(String userId,String sid) {
        String reUrl = "/class/get/selClassInfoByGradeIdS/"+userId+"/"+sid;
        return getForObject(reUrl);
    }
    public static String getClassInfo(String sid) {
        String reUrl = "/class/get/selClassInfoByGradeIdSt/"+sid;
        return getForObject(reUrl);
    }
}
