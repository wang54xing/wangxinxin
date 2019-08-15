package com.sql.oldDataPojo;

/**
 * Created by qinbo on 15/3/31.
 */
public class WordExerciseSmallQuestInfo {

    private int id;
    private int questNum;
    private int type;
    private int score;
    private String answer;
    private int optionNum;
    private int bigQuestId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestNum() {
        return questNum;
    }

    public void setQuestNum(int questNum) {
        this.questNum = questNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getOptionNum() {
        return optionNum;
    }

    public void setOptionNum(int optionNum) {
        this.optionNum = optionNum;
    }

    public int getBigQuestId() {
        return bigQuestId;
    }

    public void setBigQuestId(int bigQuestId) {
        this.bigQuestId = bigQuestId;
    }
}
