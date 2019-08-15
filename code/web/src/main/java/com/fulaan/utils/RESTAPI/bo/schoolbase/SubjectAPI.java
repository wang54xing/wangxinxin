package com.fulaan.utils.RESTAPI.bo.schoolbase;

import com.fulaan.utils.RESTAPI.base.BaseAPI;

/**
 * Created by wang_xinxin on 2017/3/1.
 */
public class SubjectAPI extends BaseAPI {

    /**
     * 查询学科
     * @param id
     * @return
     */
    public static String getSubjectInfo(String id) {
        String resoureUrl = "/subject/id/"+id;
        return getForObject(resoureUrl);
    }
    public static String getSubjectListInfo(String sid) {
        String resoureUrl = "/subject/get/"+sid;
        return getForObject(resoureUrl);
    }
}
