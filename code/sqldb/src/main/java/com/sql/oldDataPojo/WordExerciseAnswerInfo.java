package com.sql.oldDataPojo;

/**
 * Created by qinbo on 15/3/31.
 */
public class WordExerciseAnswerInfo {

    private int id;
    private int wordExerciseId;
    private int questId;
    private int userId;
    private String answer;
    private String answerPic;
    private Integer score;
    private int isCorrect;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWordExerciseId() {
        return wordExerciseId;
    }

    public void setWordExerciseId(int wordExerciseId) {
        this.wordExerciseId = wordExerciseId;
    }

    public int getQuestId() {
        return questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerPic() {
        return answerPic;
    }

    public void setAnswerPic(String answerPic) {
        this.answerPic = answerPic;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public int getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }
}
