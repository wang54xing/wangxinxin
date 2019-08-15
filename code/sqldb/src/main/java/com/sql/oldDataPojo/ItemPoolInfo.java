package com.sql.oldDataPojo;

/**
 * Created by qinbo on 15/5/8.
 */
public class ItemPoolInfo {

    private Integer Tid;
    private String booknode;
    private String period;
    private String subject;
    private String bookedition;
    private String grade;
    private String bookgrade;
    private String unit;
    private String lesson;
    private String difficulty;
    private String itemtype;
    private String knowledgePointId;
    private String knowledgePointName;
    private String knowledgeAreaId;
    private String knowledgeAreaName;
    private Integer point;
    private Integer objective;
    private String objectiveanswer;
    private Integer answercount;
    private Integer selectcount;
    private String itemcontent;
    private String answer;
    
    
    //
    private String danyuan; //
    private String singleKw;
    
   

    public Integer getTid() {
        return Tid;
    }

    public void setTid(Integer tid) {
        Tid = tid;
    }

    public String getBooknode() {
        return booknode;
    }

    public void setBooknode(String booknode) {
        this.booknode = booknode;
    }



    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBookedition() {
        return bookedition;
    }

    public void setBookedition(String bookedition) {
        this.bookedition = bookedition;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getBookgrade() {
        return bookgrade;
    }

    public void setBookgrade(String bookgrade) {
        this.bookgrade = bookgrade;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getKnowledgePointId() {
        return knowledgePointId;
    }

    public void setKnowledgePointId(String knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }

    public String getKnowledgePointName() {
        return knowledgePointName;
    }

    public void setKnowledgePointName(String knowledgePointName) {
        this.knowledgePointName = knowledgePointName;
    }

    public String getKnowledgeAreaId() {
        return knowledgeAreaId;
    }

    public void setKnowledgeAreaId(String knowledgeAreaId) {
        this.knowledgeAreaId = knowledgeAreaId;
    }

    public String getKnowledgeAreadName() {
        return knowledgeAreaName;
    }

    public void setKnowledgeAreadName(String knowledgeAreadName) {
        this.knowledgeAreaName = knowledgeAreadName;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getObjective() {
        return objective;
    }

    public void setObjective(Integer objective) {
        this.objective = objective;
    }

    public String getObjectiveanswer() {
        return objectiveanswer;
    }

    public void setObjectiveanswer(String objectiveanswer) {
        this.objectiveanswer = objectiveanswer;
    }

    public Integer getAnswercount() {
        return answercount;
    }

    public void setAnswercount(Integer answercount) {
        this.answercount = answercount;
    }

    public Integer getSelectcount() {
        return selectcount;
    }

    public void setSelectcount(Integer selectcount) {
        this.selectcount = selectcount;
    }

    public String getItemcontent() {
        return itemcontent;
    }

    public void setItemcontent(String itemcontent) {
        this.itemcontent = itemcontent;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

	public String getDanyuan() {
		return danyuan;
	}

	public void setDanyuan(String danyuan) {
		this.danyuan = danyuan;
	}

	public String getSingleKw() {
		return singleKw;
	}

	public void setSingleKw(String singleKw) {
		this.singleKw = singleKw;
	}

	@Override
	public String toString() {
		return "ItemPoolInfo [Tid=" + Tid + ", booknode=" + booknode
				+ ", period=" + period + ", subject=" + subject
				+ ", bookedition=" + bookedition + ", grade=" + grade
				+ ", bookgrade=" + bookgrade + ", unit=" + unit + ", lesson="
				+ lesson + ", difficulty=" + difficulty + ", itemtype="
				+ itemtype + ", knowledgePointId=" + knowledgePointId
				+ ", knowledgePointName=" + knowledgePointName
				+ ", knowledgeAreaId=" + knowledgeAreaId
				+ ", knowledgeAreaName=" + knowledgeAreaName + ", point="
				+ point + ", objective=" + objective + ", objectiveanswer="
				+ objectiveanswer + ", answercount=" + answercount
				+ ", selectcount=" + selectcount + ", itemcontent="
				+ itemcontent + ", answer=" + answer + ", danyuan=" + danyuan
				+ ", singleKw=" + singleKw + "]";
	}

	
    
    
}
