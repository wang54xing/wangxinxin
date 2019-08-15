package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/11/25.
 */
public enum InteractLessonAnswerType {

    CHOICE_A(1, "choice", "A"), //1
    CHOICE_B(1<<1, "choice", "B"), //2
    CHOICE_C(1<<2, "choice", "C"),  //4
    CHOICE_D(1<<3, "choice", "D"), //8
    CHOICE_TRUE(1<<4, "bool","正确"), //16
    CHOICE_FALSE(1<<5, "bool","错误"), //32
    NOT_CHOICE(0, "notChoice","未选"), //0
    ;
    private InteractLessonAnswerType(int answer, String format, String des) {
        this.answer = answer;
        this.format = format;
        this.des = des;
    }

    private int answer;
    private String format;
    private String des;

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }


    /**
     * 根据answer得到InteractLessonAnswerType
     * @param answer
     * @return
     */
    public static List<InteractLessonAnswerType> getInteractLessonAnswerType(int answer)
    {
        List<InteractLessonAnswerType> retList =new ArrayList<InteractLessonAnswerType>();

        for(InteractLessonAnswerType ilat:InteractLessonAnswerType.values())
        {
            if((answer&ilat.getAnswer())==ilat.getAnswer())
            {
                retList.add(ilat);
            }
        }
        return retList;
    }

    /**
     * 根据answer得到InteractLessonAnswerType描述
     * @param answer
     * @return
     */
    public static String getInteractLessonAnswerTypeDes(int answer)
    {
        String answerDes="";
        List<InteractLessonAnswerType> answerTypes=getInteractLessonAnswerType(answer);
        for (InteractLessonAnswerType answerType: answerTypes) {
            if(answer!=0) {
                if(answerType.getAnswer()!=0) {
                    if ("".equals(answerDes)) {
                        answerDes = answerType.getDes();
                    } else {
                        answerDes += "," + answerType.getDes();
                    }
                }
            }else{
                answerDes = answerType.getDes();
            }
        }
        return answerDes;
    }

    /**
     * 根据format得到InteractLessonAnswerType
     * @param format
     * @return
     */
    public static List<InteractLessonAnswerType> getInteractLessonAnswerType(String format)
    {
        List<InteractLessonAnswerType> retList =new ArrayList<InteractLessonAnswerType>();

        for(InteractLessonAnswerType ilat:InteractLessonAnswerType.values())
        {
            if(format!=null&&ilat.getFormat().equals(format))
            {
                retList.add(ilat);
            }
        }

        retList.add(NOT_CHOICE);
        return retList;
    }
}
