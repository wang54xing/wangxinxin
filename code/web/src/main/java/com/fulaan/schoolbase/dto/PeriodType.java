package com.fulaan.schoolbase.dto;

/**
 * Created by guojing on 2017/1/10.
 */
public enum PeriodType {

    PRIMARY("primary","小学"),
    JUNIOR("junior","初中"),
    SENIOR("senior","高中");

    private String type;
    private String name;

    private PeriodType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static PeriodType getPeriodType(String type)
    {
        for(PeriodType sjt:PeriodType.values())
        {
            if(sjt.getType().equals(type))
            {
                return sjt;
            }
        }
        return null;
    }
}
