package com.pojo.newschool;

/**
 * Created by guojing on 2017/1/10.
 */
public enum GradeType {
    ONE(1,"一年级"),
    TWO(2,"二年级"),
    THREE(3,"三年级"),
    FOUR(4,"四年级"),
    FIVE(5,"五年级"),
    SIX(6,"六年级"),
    SEVEN(7,"七年级"),
    EIGHT(8,"八年级"),
    NINE(9,"九年级"),
    TEN(10,"十年级"),
    ELEVEN(11,"十一年级"),
    TWELVE(12,"十二年级"),
    ;

    private int type;
    private String des;

    private GradeType(int type, String des) {
        this.type = type;
        this.des = des;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }
    public void setDes(String des) {
        this.des = des;
    }

    public static GradeType getGradeType(int type)
    {
        for(GradeType item:GradeType.values())
        {
            if(item.getType()==type)
            {
                return item;
            }
        }
        return null;
    }
}
