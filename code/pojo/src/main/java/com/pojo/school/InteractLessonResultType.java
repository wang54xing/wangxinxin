package com.pojo.school;

/**
 * Created by guojing on 2015/11/25.
 */
public enum InteractLessonResultType {
    UNANSWERED(0, "未做"), //未做
    CORRECT(1, "正确"),  //回答正确
    INCORRECT(2,"错误"),  //回答错误
    ;
    private InteractLessonResultType(int result, String des) {
        this.result = result;
        this.des = des;
    }

    private int result;
    private String des;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
