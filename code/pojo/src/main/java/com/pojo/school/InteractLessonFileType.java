package com.pojo.school;

/**
 * Created by guojing on 2015/11/25.
 */
public enum InteractLessonFileType {
    CHOICE(1, "老师上传课件"),
    BOOL(2, "老师上传考试试题"),
    FILL(3, "学生上传课件"),
    SUBJECTIVE(4, "学生上传考试答案"),
    ;

    private InteractLessonFileType(int type, String des) {
        this.type = type;
        this.des = des;
    }
    private int type;
    private String des;

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
}
