package com.fulaan.utils;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fulaan.new33.dto.isolate.DayRangeDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.util.*;

public class JsonUtil {
    private JsonUtil(){}
    public static String toJson(Object o){
        return null;
    }
    public static Object fromJson(String jsonText){
//    	return JSON.toJavaObject((JSON)(JSON.parse(jsonText)), Object.class);
    	return new Object();
    }
    
    /**
     * Convert Json to Map
     *
     * @throws java.io.IOException
     * @throws com.fasterxml.jackson.core.JsonParseException
     */
    public static Map<String, Object> Json2Map(String jsonStr) {

    	
    	Gson gson =new Gson();
    
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();

        try {

            // convert JSON string to Map
            map = mapper.readValue(jsonStr, Map.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
    public static void main(String[] args){
    	/*String str = "abcd";
    	str = toJson(str);
    	System.out.println(str);*/
//    	JSON json = (JSON) fromJson("abc");

    }

    public static <T>T jsonToObj(String json,TypeReference<T> typeReference){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (T) mapper.readValue(json, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String objToJson(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
