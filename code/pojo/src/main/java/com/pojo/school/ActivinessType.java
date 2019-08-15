package com.pojo.school;

/**
 * Created by guojing on 2015/11/20.
 */
public enum ActivinessType {
    LOGIN(1, 1, "学生登录"),
    UPLOAD(2, 1, "学生上传"),
    EXAM(3, 1, "学生考试"),
    ANSWER(4, 1, "学生抢答"),//举手
    RACE(5, 1, "快速答题"),//回答问题
    ANSWERED(6, 1, "学生抢答到"),//举手
    ;
    private int type;
    private int socre;
    private String des;

    private ActivinessType(int type, int socre, String des) {
        this.type = type;
        this.socre=socre;
        this.des = des;
    }


    public int getType() {
        return type;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getSocre() {
        return socre;
    }

    public void setSocre(int socre) {
        this.socre = socre;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static boolean isLoginOrUpload(int type) {
        return type==LOGIN.getType() || type==UPLOAD.getType() ;
    }

    public static boolean isNotLoginAndUpload(int type) {
        return type!=LOGIN.getType() && type!=UPLOAD.getType() ;
    }


    public static ActivinessType getActivinessType(int type)
    {
        for(ActivinessType aty:ActivinessType.values())
        {
            if(aty.getType()==type)
            {
                return aty;
            }
        }
        return null;
    }
}
