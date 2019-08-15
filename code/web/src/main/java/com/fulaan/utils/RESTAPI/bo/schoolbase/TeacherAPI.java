package com.fulaan.utils.RESTAPI.bo.schoolbase;

import com.fulaan.utils.RESTAPI.base.BaseAPI;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2017/3/1.
 */
public class TeacherAPI extends BaseAPI {

    /**
     * 查询学校老师
     * @param schoolId
     * @param keyword
     * @return
     */
    public static String selTeachersBySchoolId(ObjectId schoolId, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            keyword = "keyword";
        }
        String resoureUrl = "/teacher/get/list/"+schoolId+"/"+keyword;
        return getForObject(resoureUrl);
    }

    /**
     *
     * @param schoolId
     * @param userId
     * @return
     */
    public static String selTeacherClassId(ObjectId schoolId, ObjectId userId) {
        String resoureUrl = "/groupUser/get/class/"+schoolId+"/"+userId;
        return getTaForObject(resoureUrl);
    }
}
