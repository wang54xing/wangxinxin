package com.pojo.user;

/**
 * 打分事项
 * Created by Tony on 2014/9/24.
 */

public enum ExpLogType {

    CLOUD(1, 0, "观看云课程"),
    LESSON(1, 1, "观看作业视频"),
    FINISHED_ADVANCED_PRACTICE(1, 2, "完成进阶练习"),
    RATE(0, 3, "老师打分"),
    QUIZ(2, 4, "完成试卷"),
    AVATAR(5, 5, "上传头像"),
    PASSWORD(5, 6,"修改密码"),
    MICROBLOG(1, 7, "发表微校园"),
    PUSH(1, 8, "推送课程"),
    UPLOADQUIZ(3, 9, "上传试卷"),
    NOTIFY(2, 10, "发布通知"),
    //DELETEPUSH(-1, 11, "删除推送课程"),
    //DELETEQUIZ(-5, 12, "删除试卷"),
    //DELETENOTIFY(-1, 13, "删除通知/作业"),
    PUBLISH_ELECT(1, 14, "发布投票"),
    RUN_FOR_ELECT(2, 15, "报名投票"),
    VOTE_FOR_ELECT(1, 16, "投出一票"),
    //DELETE_ELECT(-1, 17, "删除发布的投票"),
    //ABSTAIN_FROM_ELECT(-1, 18, "放弃竞选"),
    //VOTER_QUIT(-1,19, "被投票者退出"),
    SCHOOL_SECURITY(1, 20, "校园安全"),
    //DELETE_SCHOOL_SECURITY(-1, 21, "删除校园安全"),
    DOC_FLOW(2, 22, "公文流转"),
    MICRO_HOME_BLOG(1, 23, "发表微家园"),
    MICRO_BLOG_REVIEW(1, 24, "微校园评论/点赞"),
    MICRO_HOME_BLOG_REVIEW(1, 25, "微家园评论/点赞"),
    UPLOAD_LESSON(1, 26, "上传备课"),
    SEARCH_SCHOOL_NOTICE(1, 27, "查看学校通知"),
    LAUNCH_FRIEND_SCIRCLE_ACTIVITY(1, 28, "发起好友圈活动"),
    JOIN_FRIEND_SCIRCLE_ACTIVITY(1, 29,"参与好友圈活动"),
    LAUNCH_QUESTIONNAIRE(2, 30, "发起问卷调查"),
    JOIN_QUESTIONNAIRE(1, 31,"参与问卷调查"),
    PUBLISH_HOMEWORK(3, 32, "发布作业"),
    SUBMIT_HOMEWORK(1, 33, "提交作业"),
    REPLY_HOMEWORK(1, 34, "回复作业"),
    FINISHED_PRACTICE(5, 35,"完成练习"),
    LAUNCH_GROUP_CHAT(1, 36, "发起群组交流"),
    PERFORMANCE_ANALYSIS(5, 37, "成绩分析录入"),
    INTERACT_LESSON(5, 38, "互动课堂"),
    MALL_USED(0, 39, "商城购物抵用")
    ;
    private final String desc;
    private final int type;
    private final int exp;

    /**
     * @param exp 分值
     * @param type 类型
     * @param desc  描述
     */
    ExpLogType(int exp,int type, String desc) {
        this.exp = exp;
        this.type=type;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getType() {
        return type;
    }

    public int getExp() {
        return exp;
    }
}
