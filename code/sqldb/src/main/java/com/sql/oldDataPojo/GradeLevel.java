package com.sql.oldDataPojo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by qinbo on 15/3/24.
 */
public enum GradeLevel {
    Primary_1(11, "小一"), Primary_2(12, "小二"), Primary_3(13, "小三"), Primary_4(14, "小四"), Primary_5(15, "小五"), Primary_6(
            16, "小六（预备班） "), Middle_1(21, "初一"), Middle_2(22, "初二"), Middle_3(23, "初三"), High_1(31, "高一"), High_2(32, "高二"), High_3(
            33, "高三"),High_4(34,"高考总复习");

    private int status;
    private String description;

    private GradeLevel(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Map<String, String> getPrimarySchoolMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (GradeLevel thisEnum : GradeLevel.values()) {
            if (thisEnum.getStatus()<=16) {
                map.put(String.valueOf(thisEnum.getStatus()), thisEnum.getDescription());
            }
        }
        return map;
    }

    public static Map<String, String> getMiddleSchoolMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (GradeLevel thisEnum : GradeLevel.values()) {
            if (thisEnum.getStatus() < 31 && thisEnum.getStatus() > 16) {
                map.put(String.valueOf(thisEnum.getStatus()),
                        thisEnum.getDescription());
            }
        }
        return map;
    }

    public static Map<String, String> getHighSchoolMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (GradeLevel thisEnum : GradeLevel.values()) {
            if (thisEnum.getStatus()>=31 && thisEnum.getStatus()<34) {
                map.put(String.valueOf(thisEnum.getStatus()), thisEnum.getDescription());
            }
        }
        return map;
    }

    public static Map<String, String> getCloudHighSchoolMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (GradeLevel thisEnum : GradeLevel.values()) {
            if (thisEnum.getStatus()>=31) {
                map.put(String.valueOf(thisEnum.getStatus()), thisEnum.getDescription());
            }
        }
        return map;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (GradeLevel thisEnum : GradeLevel.values()) {
            map.put(String.valueOf(thisEnum.getStatus()), thisEnum.getDescription());
        }
        return map;
    }
    public static GradeLevel getGradeLevel(String description){
        for(GradeLevel thisEnum : GradeLevel.values()){
            if(thisEnum.getDescription().equals(description)){
                return thisEnum;
            }
        }
        return null;
    }

    public static String getGradeDescription(int status){
        for(GradeLevel thisEnum : GradeLevel.values()){
            if (thisEnum.getStatus() == status) {
                return thisEnum.getDescription();
            }
        }
        return null;
    }
}
