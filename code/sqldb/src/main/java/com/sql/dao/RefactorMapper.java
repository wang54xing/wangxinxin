package com.sql.dao;

import com.sql.oldDataPojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RefactorMapper {

    public List<UserInfo> selUserInfoByUserId(@Param("offset") int offset,
                                               @Param("length") int length);

    public List<SchoolInfo> selSchool();

    public List<GeoInfo> selGeo();

    public List<GradeInfo> selGrade();

    public List<SubjectInfo> selSubject();

    public List<ClassesInfo> getClassInfo();

    public List<InterestClassInfo> getIntrestClassInfo();

    public List<RefUserClassesInfo> getRefTeacherClass();

    public List<RefUserClassesInfo> getRefUserClassesInfo();

    public List<RefUserClassesInfo> getRefClassMasterInfo();

    public List<CloudLessonInfo> getCloudLessonInfo();

    public List<RefParentStudent> getRefParentStudent();

    public List<CloudLessonTypeInfo> getCloudLessonTypeInfo();

    public List<VideoInfo> getVideoInfo();

    public List<LessonInfo> selLessonInfo();

    public List<DirInfo> selDir();
    public List<TeacherInfo> selTeacher();
    public List<TeacherClassLessonInfo> selTeacherClassSubject();


    public List<Integer> getCloudLessonGradeId(@Param("courseId")int courseId);

    public List<Integer> getCloudLessonTypeId(@Param("courseId")int courseId);

    public List<WordExerciseInfo> selWordExercise();

    public List<RefWordClassInfo> selRefWordlClassInfo();
    public List<WordExerciseSubmitInfo> selWordExerciseSubmit();


    public List<UserPetInfo> getUserPetInfo();

    public List<PetInfo> getPetTypeInfo();

    public List<RefLessonVideos> selRefLessonVideos();

    public List<LessonCourseWareInfo> seleLessonCourseWareInfo();

    public List<ExperienceLogInfo> getUserScoreLogsInfo();

    public List<LetterInfo> selLetterInfo();

    public List<WordExerciseBigQuestInfo> selExerciseBigQuest();
    public List<WordExerciseSmallQuestInfo> selExerciseSmallQuest();

    public List<WordExerciseAnswerInfo> selExerciseAnswer();

    public List<MicroBlogInfo> getMicroBlogInfo();

    public List<ReplyCommentInfo> getReplyCommentInfo();

    public List<MicroBlogZanLogsInfo> getMicroBlogZanLogsInfo();

    public List<BlogPicInfo> getBlogPicInfo();

    public List<Integer> getGradeMasterList();


    public List<MesgInfo> getHomeworkInfo();
    public List<MsgReplyInfo> getHomeworkSubmitInfo();
    public List<MsgVoiceInfo> getHomeworkVoiceInfo();
    //public List<MsgVoiceInfo> getHomeworkSubmitVoiceInfo();
    public List<MsgAttachInfo> getHomeworkAttachInfo();

    public List<RefMesgclass> getRefMesgclass();

    public List<MsgVoiceInfo> getHomeworkSubmitVoiceInfo();

    public List<MsgAttachInfo> getHomeworkSubmitAttachInfo();

    public List<MesgInfo> getNoticeInfo();


    public List<ActivityInfo> getActivityInfo();
    public List<ActivityDiscussInfo> getActivityDiscussInfo();

    public List<ActivityAttendInfo> getActivityAttendInfo();


    public List<ActivityImageCountInfo> getActivityImageCountInfo();


    public List<ActivityTrackInfo> getActivityTrackInfo();

    public List<ActivityInvitationInfo> getActivityInvitationInfo();

    public List<FriendInfo> getFriendInfo();
    public List<FriendApplyInfo> getFriendApplyInfo();

    public List<SchoolLeagueInfo> getSchoolLeagueInfo();

    public List<GroupInfo> getGroupInfo();
    public List<GroupChatInfo> getGroupChatInfo();
    public List<GroupFileInfo> getGroupFileInfo();
    public List<RefGroupUser> getGroupUserInfo();

    public List<InterestClassLessonScoreInfo> getInterestClassScoreInfo();
    public List<InterestClassTranscriptInfo> getInterestClassTranscriptInfo();

    public List<VideoViewInfo> getVideoViewInfo();

    public List<LessonCommentInfo> getLessonComment();

    public List<ItemPoolInfo> getItemPool1();
    public List<ItemPoolInfo> getItemPool2();
    public List<ItemPoolInfo> getItemPool3();

    public List<EmarketLessonInfo> getEmarketLesson();

    public List<RefLessonGradeInfo> getRefLessonGrade();
    public List<RefLessonSubjectInfo> getRefLessonSubject();

    public List<Integer> selHeadmasterWithClass();

    public void insertUserBalance(@Param("username") String username,
                                  @Param("password") String password,
                                  @Param("balance") Double balance,
                                  @Param("userId") String userId);


    public List<ResourceKPDicInfo> getResouceDicInfo();
    public List<ResourceKPDicInfo> getResouceDicInfo2();

}
