package com.fulaan.utils.RESTAPI.bo.new33.isolate;

import com.fulaan.utils.RESTAPI.base.BaseAPI;

import java.util.Map;

/**
 * Created by wang_xinxin on 2018/3/12.
 */
public class JXBAPI extends BaseAPI {

    /**
     *
     * @param map
     */
    public static void importData(Map map) {
        String reUrl = "/jxb/importData";
        postForObject(reUrl, map);
    }
}
