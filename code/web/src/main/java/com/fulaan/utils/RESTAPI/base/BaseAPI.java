package com.fulaan.utils.RESTAPI.base;

import com.sys.props.Resources;
import org.springframework.web.client.RestTemplate;

/**
 * Created by guojing on 2017/1/5.
 */
public class BaseAPI {
    public static final String BO_URL = Resources.getProperty("bo.server.name");
    public static final String TA_URL = Resources.getProperty("ta.server.name");
    public static final RestTemplate restTemplate = new RestTemplate();

    /**
     * get方法
     * @param resoureUrl
     * @return
     */
    public static String getForObject(String resoureUrl) {
        resoureUrl = BO_URL + resoureUrl;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class);
        return resultStr;
    }

    /**
     * post方法
     * @param resoureUrl
     * @return
     */
    public static String postForObject(String resoureUrl, Object obj) {
        resoureUrl = BO_URL + resoureUrl;
        String resultStr = restTemplate.postForObject(resoureUrl, obj, String.class);
        return resultStr;
    }

    /**
     * put方法
     * @param resoureUrl
     * @return
     */
    public static void put(String resoureUrl, Object obj) {
        resoureUrl = BO_URL + resoureUrl;
        restTemplate.put(resoureUrl, obj, String.class);
    }

    /**
     * delete方法
     * @param resoureUrl
     * @return
     */
    public static void delete(String resoureUrl) {
        resoureUrl = BO_URL + resoureUrl;
        restTemplate.delete(resoureUrl);
    }

    /**
     * get方法
     * @param resoureUrl
     * @return
     */
    public static String getTaForObject(String resoureUrl) {
        resoureUrl = TA_URL + resoureUrl;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class);
        return resultStr;
    }

    /**
     * post方法
     * @param resoureUrl
     * @return
     */
    public static String postTaForObject(String resoureUrl, Object obj) {
        resoureUrl = TA_URL + resoureUrl;
        String resultStr = restTemplate.postForObject(resoureUrl, obj, String.class);
        return resultStr;
    }

    /**
     * post方法
     * @param resoureUrl
     * @return
     */
    public static void taPut(String resoureUrl, Object obj) {
        resoureUrl = TA_URL + resoureUrl;
        restTemplate.put(resoureUrl, obj, String.class);
    }

    /**
     * delete方法
     * @param resoureUrl
     * @return
     */
    public static void taDelete(String resoureUrl) {
        resoureUrl = TA_URL + resoureUrl;
        restTemplate.delete(resoureUrl);
    }
}
