package com.pojo.school;

/**
 * Created by guojing on 2015/11/25.
 */
public enum InteractLessonQuestionType {
    UNSET(0,"unset", "未设置"),
    CHOICE(1,"choice", "选择题"),
    BOOL(2,"bool", "判断题"),
    FILL(3,"fill", "填空题"),
    SUBJECTIVE(4,"subjective", "主观题"),
    MULTI(5,"multi", "多选题"),
    ;

    private InteractLessonQuestionType(int id, String type, String des) {
        this.id=id;
        this.type = type;
        this.des = des;
    }
    private int id;
    private String type;
    private String des;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }


    public static boolean isChoiceOrBoolOrMulti(int id) {
        return CHOICE.getId()==id || BOOL.getId()==id || MULTI.getId()==id;
    }

    public static boolean isNotChoiceAndBoolAndMulti(int id) {
        return CHOICE.getId()!=id && BOOL.getId()!=id && MULTI.getId()!=id;
    }

    public static InteractLessonQuestionType getInteractLessonQuestionType(int id)
    {
        for(InteractLessonQuestionType ilqType:InteractLessonQuestionType.values())
        {
            if(ilqType.getId()==id)
            {
                return ilqType;
            }
        }
        return null;
    }


    public static boolean isChoiceOrBoolOrMulti(String type) {
        if(type==null||"".equals(type)) {
           return false;
        }
        return CHOICE.getType().equals(type) || BOOL.getType().equals(type) || MULTI.getType().equals(type);
    }

    public static boolean isNotChoiceAndBool(String type) {
        if(type==null||"".equals(type)) {
            return false;
        }
        return !CHOICE.getType().equals(type) && !BOOL.getType().equals(type);
    }

    public static boolean isNotChoiceAndBoolAndMulti(String type) {
        if(type==null||"".equals(type)) {
            return false;
        }
        return !CHOICE.getType().equals(type) && !BOOL.getType().equals(type) && !MULTI.getType().equals(type);
    }

    public static InteractLessonQuestionType getInteractLessonQuestionType(String type)
    {
        if(type!=null&&!"".equals(type)){
            for(InteractLessonQuestionType ilqType:InteractLessonQuestionType.values())
            {
                if(ilqType.getType().equals(type))
                {
                    return ilqType;
                }
            }
        }
        return null;
    }
}
