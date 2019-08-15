package com.fulaan.utils;

import java.util.Calendar;

/**
 * Created by fl on 2016/8/22.
 */
public class TermUtil {

    public static String getSchoolYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String schoolYear;
        if(month >= 9){
            schoolYear = year + "-" + (year + 1) + "学年";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年";
        }
        return schoolYear;
    }

    public static String getTerm(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String term;
        if (month < 9 && month >= 2) {
            term = (year - 1) + "-" + year + "学年第二学期";
        } else if(month >= 9){
            term = year + "-" + (year + 1) + "学年第一学期";
        } else {
            term = (year - 1) + "-" + year + "学年第一学期";
        }
        return term;
    }
}
