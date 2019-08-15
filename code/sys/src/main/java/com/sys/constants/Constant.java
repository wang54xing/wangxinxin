package com.sys.constants;
import com.mongodb.BasicDBObject;
public class Constant {
	/**
	 * chat 站点设置名称
	 */
	public static final  String  APP_CHAT_SET="cs";
	
	public static final  String  UTF_8="utf-8";
	
	public static final  String  GBK="GBK";
	
	public static final  String  ISO="ISO8859-1";
	
	
	public static final  String  SEPARATOR ="\\.";
	
	public static final  String  POINT =".";
	
	public static final  String  SMALL_POINT ="'";
	
	public static final  String  START="*";
	
	public static final  String  AT="@";
	//冒号
	public static final  String  COLON=":";
	//分号
	public static final  String  SEMICOLON=";";
	//下划线
	public static final  String  UNDERLINE="_";
	//分隔线
	public static final  String  SEPARATE_LINE="-";
	//问号
	public static final  String  ASK="?";
	//并且
	public static final  String  AND="&";
	//等于
	public static final  String  EQUALS="=";
	//逗号
	public static final  String  COMMA=",";
	//有效的
	public static final  int     VALID=1;
	//无效的
	public static final  int     INVALID=0;
	//char ','
	public static final  char    CHAR_COMMA=',';
	//换行符
	public static final  String  NEW_ROWS="\n"; 
	//空字符
	public static final  String  EMPTY="";
	//一个空格
	public static final  String  SPACE=" ";
	
	public static final  String BASE_PATH="/";
	public static final String   STRING_NEGATIVE_ONE="-1";

	//2秒
	public static final  int   SESSION_TWO_SECONDS=2;
	//1分钟
	public static final  int   SESSION_ONE_MINUTE=60;
	//2分钟
	public static final  int   SESSION_TWO_MINUTE=2*60;
	//5分钟
	public static final  int   SESSION_FIVE_MINUTE=5*60;
	//10分钟
	public static final  int   SESSION_TEN_MINUTE=10*60;
	//http session时长，30分钟
	public static final  long   SESSION_TIMEOUT_SECOND=30*60;
	public static final  int   SESSION_TIMEOUT_SECOND_INT=30*60;
	//一小时
	public static final  int    SECONDS_IN_HOUR=60*60;
	//一小时
	public static final  long    MS_SECONDS_IN_HOUR=60*60*1000L;
	//一天
	public static final  int    SECONDS_IN_DAY=60*60*24;
	//一天
	public static final  long    MS_SECONDS_IN_DAY=60*60*24*1000L;
	//一周
	public static final  int     SECONDS_IN_WEEK=60*60*24*7;
	//一周
	public static final  long     MS_SECONDS_IN_WEEK=60L*60L*24L*7L*1000L;
	//一月
	public static final  int    SECONDS_IN_MONTH=60*60*24*30;
	//一月
	public static final  long   MS_SECONDS_IN_MONTH=60L*60L*24L*30L*1000L;
	
	//2月
	public static final  long   MS_SECONDS_IN_TWO_MONTH=60L*60L*24L*30L*1000L*2L;
	//一年
	public static final  long   SECONDS_IN_YEAR=60L*60L*24L*365L*1000L;
	//一年
	public static final  long   MS_SECONDS_IN_YEAR=60L*60L*24L*365L*1000L;
	/**
	 * 每天的毫秒数
	 */
	public static final long     MS_IN_DAY=1000L*60*60*24;
	
	/**
	 * 500毫秒
	 */
	public static final  long   MS_ONE_THOUSAND=1000L;
	

	/**
	 * 每周的毫秒数
	 */
	public static final long     MS_IN_WEEK=MS_IN_DAY*7;
	
	/**
	 * 半小时
	 */
	public static final long     MS_IN_30_MINUTE=1000L*60*30;
	
	
	
	public static final int SYN_NOT_NEED=1;//已经同步完成
	public static final int SYN_YES_NEED=0;//需要同步
	
	public static final int  BRACELET_BINDS_TYPE=10000;
	
	/**
	 * 值
	 */
	public static final int      DEFAULT_VALUE_INT=0;
	public static final String   DEFAULT_VALUE_STRING="";
	public static final long     DEFAULT_VALUE_LONG=0l;
	public static final double   DEFAULT_VALUE_DOUBLE=0D;
	public static final Object   DEFAULT_VALUE_OBJECT=null;
	public static final int      MAN=1;
	public static final int      WOMEN=0;
	
	
	/**
	 * 用户微服务用户存储cookie key
	 */
	public static final String    MICRO_USER_COOKIE_USER_KEY="msui";
	public static final String    COOKIE_USER_KEY="ui";
	public static final String    COOKIE_USERNAME_KEY="momcallme";
	public static final String    COOKIE_SSO_USER_KEY="st";
	public static final String    COOKIE_VALIDATE_CODE="verifycode";
	public static final String    COOKIE_PASS_WORD="password";

	public static final String    USER_FIRST_LOGIN="firstlogin";

	public static final String    MD5="MD5";

	public static final String    COOKIE_PROVINCE_LOGIN="provincelogin";
	
	
	
	/**
	 * 最大的key.split 之后的长度，最大支持长度为3的key,也就是支持a.b.c,不支持a.b.c.d
	 */
	public static final int     ENTRY_MAX_FIELD_ARRAY_LENGTH=3;

	public static final int     NEGATIVE_ONE=-1;
	
	public static final int     NEGATIVE_SEVEN=-7;
	public static final int     NEGATIVE_TEN=-10;
	public static final int     NEGATIVE_HUNDRED=-100;
	public static final int     ZERO=0;
	public static final int     ONE=1;
	public static final int     TWO=2;
	public static final int     THREE=3;
	public static final int     FOUR=4;
	public static final int     FIVE=5;
	public static final int     SIX=6;
	public static final int     SEVEN=7;
	public static final int     EIGHT=8;
	public static final int     NINE=9;
	public static final int     TEN=10;
	public static final int     TWELVE=12;
	public static final int     FOURTEEN=14;
	public static final int     FIFTEEN=15;
	public static final int     SIXTEEN=16;
	public static final int     TWENTY=20;
	public static final int     TWENTY_FOUR=24;
	public static final int     TWENTY_FIVE=25;
	public static final int     FIVETY=50;
	public static final int     HUNDRED=100; 
	public static final int     HUNDRED_AND_FIVETY=150; 
	public static final int     FIVE_HUNDRED=500;
	public static final int     MIN_PASSWORD=100000; 
	public static final int     MAX_PASSWORD=99999999; 
	
	/**
	 * 字段
	 */
	public static final String ID="_id";
	//排序字段
	/**
	 * mongo 
	 */
	public static final String MONGO_NE="$ne";
	public static final String MONGO_SET="$set";
	public static final String MONGO_AND="$and";
	public static final String MONGO_OR="$or";
	public static final String MONGO_IN="$in";
	public static final String MONGO_ALL="$all";
	public static final String MONGO_NOTIN="$nin";
	public static final String MONGO_PUSH="$push";
	public static final String MONGO_PULL="$pull";
	public static final String MONGO_POP="$pop";
	public static final String MONGO_EACH="$each";
	public static final String MONGO_SORT="$sort";
	public static final String MONGO_SLICE	="$slice";
	public static final String MONGO_MATCH	="$match";
	public static final String MONGO_UNWIND	="$unwind";
	public static final String MONGO_SKIP	="$skip";
	public static final String MONGO_LIMIT	="$limit";
	public static final String MONGO_ADDTOSET	="$addToSet";
	public static final String MONGO_PROJECT	="$project";
	public static final String MONGO_ADD	="$add";
	public static final String MONGO_SUM	="$sum";
	public static final String MONGO_GROUP	="$group";
	public static final String MONGO_REGEX	="$regex";
	public static final String MONGO_OPTIONS	="$options";
	public static final String MONGO_INC	="$inc";
	public static final String MONGO_GEOWITHIN="$geoWithin";
	public static final String MONGO_CENTER="$center";
	public static final String MONGO_BOX	="$box";
	public static final String MONGO_GT	="$gt";
	public static final String MONGO_GTE="$gte";
	public static final String MONGO_LT	="$lt";
	public static final String MONGO_LTE="$lte";
	public static final String MONGO_ELEMATCH="$elemMatch";
	public static final String MONGO_PULLALL="$pullAll";
	public static final String MONGO_PUSHALL="$pushAll";
	public static final String MONGO_SIZE="$size";
	
	
	public static final BasicDBObject MONGO_SORTBY_ASC =new BasicDBObject("_id",1);
	public static final BasicDBObject MONGO_SORTBY_DESC =new BasicDBObject("_id",-1);
	
	
	public static final int DESC =-1;
	public static final int ASC	 =1;
	
	public static final BasicDBObject QUERY =new BasicDBObject();
	public static final BasicDBObject FIELDS =new BasicDBObject();
   
    

	
	/**
	 * 字段
	 */
	public static final String FIELD_SYN="syn";
    public static final String SUCCESS="SUCCESS";
    public static final String FAILD="FAILD";
    public static final String SUCCESS_CODE="200";
    public static final String FAILD_CODE="500";

    
    /**
     * 最多商品评论个数
     */
    public static final int MAX_GOODS_COMMENT_INT=500;

    /**
     * 最多课程评论个数
     */
    public static final int MAX_LESSON_COMMENT_INT=500;
    
    
    
	/**
	 * collection 
	 */
	//地区表
	public static final String COLLECTION_REGION_NAME="regions";
	//用户表
	public static final String COLLECTION_USER_NAME="uuser2";
    //单词，词汇，分词表
    public static final String COLLECTION_WORD_NAME = "words";
	//学校以及年级
	public static final String COLLECTION_SCHOOL_NAME="schools";

	public static final String COLLECTION_NEW_SCHOOL_NAME="newschools";

    //投票选举
    public static final String COLLECTION_ELECT_NAME="elects";
	//班级
	public static final String COLLECTION_CLASS_NAME="classes";

	//班级
	public static final String COLLECTION_NEW_CLASS_NAME="newclasses";
	//视频表
	public static final String COLLECTION_VIDEO_NAME="videos";
	//视频观看记录表
	public static final String COLLECTION_VIDEO_RECORD_NAME="videorecords";
	//信件表
	public static final String COLLECTION_LETTER_NAME="letters";
	//信件记录表
	public static final String COLLECTION_LETTER_RECORD_NAME="letterrecords";
	//博客
	public static final String COLLECTION_BLOG_NAME="blogs";
	//微课大赛
	public static final String COLLECTION_MATCH_NAME="match";

	//微课比赛分数
	public static final String COLLECTION_MATCH_LESSON_SCORE_NAME="lessonscore";
	//微课评分
	public static final String COLLECTION_MATCH_SCORE_NAME="matchscore";
	//云课程类别
	public static final String COLLECTION_CLOUDLESSONTYPE_NAME="cloudlessontypes";
    //云课程动作表，包括收藏，点赞等
    public static final String COLLECTION_CLOUDLESSON_ACTION_NAME = "cloudlessonactions";
    //云课程统计表
    public static final String COLLECTION_USER_CLOUDLESSON_STAT_NAME = "userclolessstat";
	//云课程
	public static final String COLLECTION_CLOUDLESSON_NAME="cloudlessons";
	//课程
	public static final String COLLECTION_LESSON_NAME="lessons";
	//微课创建记录表
	public static final String COLLECTION_LESSON_ADD="lessonadd";
	//学生观看记录表
	public static final String COLLECTION_LESSON_STU="lessonStuView";
	//目录
	public static final String COLLECTION_DIR_NAME="dirs";
	//老师班级课程
	public static final String COLLECTION_TEACHERCLASSSUBJECT_NAME="tcsubjects";
	//班级文档
	public static final String COLLECTION_EXERCISE_NAME ="exercises";
	//班级文档题目
	public static final String COLLECTION_EXERCISE_ITEM_NAME ="exerciseitems";
	//班级文档答案
	public static final String COLLECTION_EXERCISE_ANSWER_NAME ="exerciseanswers";
	//班级文档临时答案
	public static final String COLLECTION_EXERCISE_ANSWER_TEMP ="exerciseanswerstemp";
	//作业
	public static final String COLLECTION_HOMEWORK_NAME="homeworks";	
	//商品
	public static final String COLLECTION_GOODS_NAME="goods";
	//订单
	public static final String COLLECTION_ORDERS_NAME="orders";
    //提现
    public static final String COLLECTION_WITHDRAW_CASH_NAME="withdrawCash";
	//日历事件
	public static final String COLLECTION_EVENT_NAME="events";
	//日历循环事件
	public static final String COLLECTION_LOOPEVENT_NAME="loopevents";
	//用户积分
	public static final String COLLECTION_USER_EXPER_LOG_NAME="uexperlogs";
    //用户学年积分
    public static final String COLLECTION_USER_SCHOOL_YEAR_EXPER_NAME="uschoolyearexperlogs";
	//联盟资源
	public static final String COLLECTION_LEAGUE_NAME="leagues";
	//通知
	public static final String COLLECTION_NOTICE_NAME="notices";
	//通知阅读表
	public static final String COLLECTION_NOTICE_READ_NAME="noticesreads";
	//题库
	public static final String COLLECTION_ITEM_POOL="itempool";
	//用户题库
	public static final String COLLECTION_USER_ITEM_POOL="useritempool";
	//组卷
	public static final String COLLECTION_TEST_PAPER="testpapers";
	//学生练习
	public static final String COLLECTION_STUDENT_EXERCISE="studentexercise";	
	//学生错题库
	public static final String COLLECTION_STUDENT_ERROR_ITEM="erroritems";
    //系统版本
    public static final String COLLECTION_VERSION="versions";
    //资源字典表
    public static final String COLLECTION_RESOURCES_DICTIONARY="resdictionary";
   //资源字典表
    public static final String COLLECTION_RESOURCES="resources";
    //部门
    public static final String COLLECTION_DEPARTMENT="departments";
    //互动课堂
    public static final String COLLECTION_INTERACTLESSON="interactLesson";
	//互动课堂考试
	public static final String COLLECTION_INTERACTLESSON_EXAM="interactLessonExam";
	//互动课堂考试明细
	public static final String COLLECTION_INTERACTLESSON_EXAM_DETAIL="interactLessonExamDetail";
	//互动课堂考试成绩分布
	public static final String COLLECTION_INTERACTLESSON_SCORE_CLASSIFY="interactLessonScoreClassify";
	//互动课堂快速答题
	public static final String COLLECTION_INTERACTLESSON_QUICK_ANSWER="interactLessonQuickAnswer";
	//互动课堂文件
	public static final String COLLECTION_INTERACTLESSON_FILE="interactLessonFile";
	// 互动课堂组内互评得分
	public static final String COLLECTION_INTERACTLESSON_GROUP_SCORE = "interactLessonGroupScore";

	//互动课堂-活跃度
	public static final String COLLECTION_ACTIVINESS="activiness";
    
    //考试成绩
    public static final String COLLECTION_EXAMRESULT_NAME="examresult";
    //成绩列表
    public static final String COLLECTION_PERFORMANCE_NAME="performance";
	//成绩统计表
	public static final String COLLECTION_SCORE_SUMMARY="scoresummary";
	//新成绩表
	public static final String COLLECTION_SCORES="scores";
	//总分
	public static final String COLLECTION_ZONGFEN="zongfen";
	//校区管理
	public static final String COLLECTION_CAMPUS_NAME="campus";
	//提醒
	public static final String COLLECTION_REMIDNER_NAME="reminder";
	//兴趣班分类
	public static final String COLLECTION_INTERESTCATEGORY_NAME="interestcategory";
	//兴趣班分类
	public static final String COLLECTION_INTERESTTERMS_NAME="interestterms";





	//好友
    public static final String COLLECTION_FRIEND_NAME="friend";
	//添加好友的申请
    public static final String COLLECTION_FRIEND_APPLY_NAME="friendApply";
    //活动邀请
    public static final String COLLECTION_ACTIVITY_INVITATION_NAME="actInvitation";
    //活动
    public static final String COLLECTION_ACTIVITY_NAME="activity";
    //活动动态
    public static final String COLLECTION_ACTIVITY_TRACK_NAME="actTrack";

    //投票选举
    public static final String COLLECTION_QUESTIONNAIRE_NAME ="questionnaire";
	//新闻
    public static final String COLLECTION_NEWS_NAME = "news";
    //用户宠物
    public static final String COLLECTION_USER_PET="userPets";
    //宠物类型
    public static final String COLLECTION_PET_TYPE="petType";
    //兴趣班
    public static final String COLLECTION_INTEREST_CLASS_NAME="interestClass";
	//兴趣班课程 学生课时得分表
	public static final String COLLECTION_INTEREST_LESSON_SCORE_NAME="interestClassLessonScore";
	//兴趣班学生 期末成绩单
	public static final String COLLECTION_INTEREST_TRANSCRIPT="interestClassTranscript";
	//群组
	public static final String COLLECTION_GROUPS_NAME="groups";
	//群文件
	public static final String COLLECTION_GROUPFILE_NAME="groupfile";
	//群聊天记录
	public static final String COLLECTION_GROUPSCHAT_NAME="groupchat";
    //用户活动日志
    public static final String COLLECTION_USER_ACTION_LOG="userActionLog";
    //教育局管理
    public static final String COLLECTION_EDUCATION_BUREAU="educationBureau";
    //配置管理
    public static final String COLLECTION_SYSTEM_CONFIG="systemConfig";
    //校园安全
    public static final String COLLECTION_SCHOOL_SECURITY="schoolSecurity";
	//新闻栏目
	public static final String COLLECTION_NEWS_COLUMN="newscolumn";
    //德育项目管理
    public static final String COLLECTION_MORAL_CULTURE_MANAGE="moralCultureManage";
    //德育项目成绩
    public static final String COLLECTION_MORAL_CULTURE_SCORE="moralCultureScore";
	//考勤管理
	public static final String COLLECTION_ATTENDANCE_NAME="attendance";

	//运费模板
	public  static final String COLLECTION_EXPRESS_TEMPLATE_NAME="eexpresstemplate";

	//奖惩信息
	public static final String COLLECTION_REWARD = "reward";
	public static final String COLLECTION_REPAIR="repair";
	// 工资项目数据
	public static final String COLLECTION_SALARY_ITEM = "salaryItem";
	// 工资数据
	public static final String COLLECTION_SALARY = "salary";

	//考试信息
	public static final String COLLECTION_EXAM = "examresult";
	//考试成绩
	public static final String COLLECTION_SCORE = "performance";
	//区域联考
	public static final String COLLECTION_JOINTEXAM = "jointexam";
	//区域联考汇总
	public static final String COLLECTION_EXAMSUMMARY = "examsummary";
	//等级设置
	public static final String COLLECTION_LEVEL = "level";
	//考场资源
	public static final String COLLECTION_EXAM_ROOM = "examroom";
	//校园资产
	public static final String COLLECTION_PROPERTY = "property";
	//校园资产分类
	public static final String COLLECTION_PROPERTY_CLASSIFICATION = "propertyclassification";
	//器材管理
	public static final String COLLECTION_EQUIPMENT = "equipment";
	//器材管理分类
	public static final String COLLECTION_EQUIPMENT_CLASSIFICATION = "equipmentclassification";
		
	//评比
	public static final String COLLECTION_COMPETITION = "competition";
	//评比明细
	public static final String COLLECTION_COMPETITION_DETAIL = "competitiondetail";
	//评比分数
	public static final String COLLECTION_COMPETITION_SCORE = "competitionscore";
	
	//考试小分类型表
    public static final String COLLECTION_EXAM_ITEM = "examitem";
	//考试小分份数表
	public static final String COLLECTION_EXAM_ITEM_SCORE = "examitemscore";

	public static final String[] CHINESE_WEEK = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	// end ycode

    //火热活动
    public static final String COLLECTION_FIERY_ACTIVITY_NAME="fieryactivitys";

	//公文流转
	public static final String COLLECTION_DOC_FLOW_NAME="docflow";
	
	//评论
	public static final String COLLECTION_COMMENT = "comment"; 
	
	//用户基本信息
	public static final String COLLECTION_USER_BASE_INFO = "userbaseinfo"; 
	
	//学籍信息

	public static final String COLLECTION_REGISTRATION = "registration";










	//走班-教学周
	public static final String COLLECTION_ZOUBAN_TERMWEEK = "zb_term";
	//教室
	public static final String COLLECTION_CLASSROOM = "classroom";
	//课表配置
	public static final String COLLECTION_TIMETABLE_CONF = "zb_timetableconf";
	//走班模式配置
	public static final String COLLECTION_ZOUBAN_MODE = "zb_mode";
	//走班-进度
	public static final String COLLECTION_ZOUBAN_STATE = "zb_state";
	//选课配置
	public static final String COLLECTION_XUANKE_CONF = "zb_xuankeconf";
	//选课科目配置
	public static final String COLLECTION_SUBJECT_CONF = "zb_subjectconf";
	//走班-学科组合
	public static final String COLLECTION_ZOUBAN_SCHOOLSUBJECTGROUP = "zb_schoolSubjectGroup";
	//学生选课结果
	public static final String COLLECTION_STUDENT_CHOOSE = "zb_studentchoose";
	
	public static final String COLLECTION_STUDENT_COURSE_CHOOSE = "zb_stuCourseChoose";
	//分段
	public static final String COLLECTION_FENGDUAN = "zb_fengduan";
	//分层成绩
	public static final String COLLECTION_ZOUBAN_SCORE = "zb_score";
	//走班课程(教学班)
	public static final String COLLECTION_ZOUBAN_COURSE = "zb_zoubancourse";
	//走班-冲突记录
	public static final String COLLECTION_ZOUBAN_CONFLICT = "zb_conflict";
	//走班课表
	public static final String COLLECTION_COURSE_TABLE = "zb_coursetable";
	//走班调课通知
	public static final String COLLECTION_ZOUBAN_NOTICE = "zb_notice";
	//走班-虚拟班
	public static final String COLLECTION_ZOUBAN_VIRTUALCLASS = "zb_virtualclass";



	//走班-考勤（株洲）
	public static final String COLLECTION_ZOUBAN_ATTENDANCE = "zb_attendance";

	//课表配置（来凤&其他，和走班无关）
	public static final String COLLECTION_COURSE_CONF = "zb_courseconf";






	//论坛-帖子
	public static final String COLLECTION_FORUM_POST = "fpost";
	//论坛-回帖
	public static final String COLLECTION_FORUM_REPLY = "freply";
	//论坛-版块
	public static final String COLLECTION_FORUM_SECTION = "fsection";
	//论坛-收藏
	public static final String COLLECTION_FORUM_COLLECTION = "fcollection";
	//论坛-签到
	public static final String COLLECTION_FORUM_SIGN = "fsign";






	//资源-附件
	public static final String COLLECTION_COURSWARE = "coursware";
	public static final String COLLECTION_CLOUD_RESOURCES="resourcescloud";
	//评课议课
	public static final String COLLECTION_REVIEW = "review";
	//集体备课
	public static final String COLLECTION_PREPARATION = "preparation";
	//课题研究
	public static final String COLLECTION_PROJECT = "project";
	//课题研究子课题
	public static final String COLLECTION_SUB_PROJECT = "subproject";
	//科研成果
	public static final String COLLECTION_ACHIEVEMENT = "achievement";
	//教研
	public static final String COLLECTION_TEACHING_AND_RESEARCH="teachresearch";
	//成长档案
	public static final String COLLECTION_GROWTH_RECORD="growth";
	//题库
	public static final String COLLECTION_ITEM__STORE="itemstore";
	//复杂评论
	public static final String COLLECTION_COMPLEX_COMMENT = "complexcomment";

	
	//区域联考汇总表
	public static final String COLLECTION_EXAM_SUMMARY = "examsummary";
	//区域联考表
	public static final String COLLECTION_REGIONAL_EXAM = "jointexam";
	//家庭成员
	public static final String COLLECTION_FAMILY_MEMBER = "familymenmber"; 
	
	
	//学习简历
	public static final String COLLECTION_LEANING_RESUME = "learningresume"; 
	
	
	//评价项目(素质教育)
	public static final String COLLECTION_QUALITY = "quality"; 
	
	//评价子项目(素质教育)
	public static final String COLLECTION_SUB_QUALITY = "subquality"; 
	
	
	
	
	
	
	//门卫管理进校
		public static final String COLLECTION_STUDENT_ENTER="studententer";
		//门卫管理出校
		public static final String COLLECTION_STUDENT_OUT="studentout";
		//来访登记
		public static final String COLLECTION_VISIT="visit";
		//宿舍表（caotiecheng）
		public static final String COLLECTION_DORM="dorm";
		//宿舍区
		public static final String COLLECTION_DORM_AREA="dormarea";
		//宿舍楼
		public static final String COLLECTION_DORM_BUILDING="dormbuilding";
		//宿舍层
		public static final String COLLECTION_DORM_FLOOR="dormfloor";
		//宿舍学生表
		public static final String COLLECTION_DORM_STUDENT="dormstudent";
	    //微博话题
	    public static final String COLLECTION_BLOG_THEME_NAME="theme";
	   //功能教室
	    public static final String COLLECTION_FUNCTION_CLASSROOM="classroom";
	    //功能教室预约
	    public static final String COLLECTION_CLASSROOM_APPOINTMENT="classroomappointment";
	    //ebusiness
	    //电子商务商品
	    public static final String COLLECTION_EBUSINESS_GOODS="egoods";
	    //电子商务商品评论表
	    public static final String COLLECTION_EBUSINESS_GOODS_COMMENT="ecomment";
	    //电子商务订单
	    public static final String COLLECTION_EBUSINESS_ORDERS="eorder";
	    //电子商务订单地址
	    public static final String COLLECTION_EBUSINESS_ORDERSADDRESS="eoaddress";
		//电子商务商品日志
		public static final String COLLECTION_EBUSINESS_GOODSLOG="egoodslog";

	//电子商务商品分类
	public static final String COLLECTION_EBUSINESS_GOODSCATEGORY="egoodscategory";
	//电子商务年龄分类
	public static final String COLLECTION_EBUSINESS_AGECATEGORY="eagecategory";
	//电子商务年级分类
	public static final String COLLECTION_EBUSINESS_GRADECATEGORY="egradecategory";
	//电子商务抵用券
	public static final String COLLECTION_EBUSINESS_VOUCHER="evoucher";
	//商品分类简介视频
	public static final String COLLECTION_EBUSINESS_CATEGORYVIDEO="ecategoryVideo";


	//定时任务
	public static final String COLLECTION_CRONTAB="crontab";
	

	//来凤 教师请假
	public static final String COLLECTION_TEACHER_LEAVE="teacherLeave";
	//来凤  老师代课
	public static final String COLLECTION_REPLACE_COURSE="replaceCourse";
	//教师课程项目
	public static final String COLLECTION_COURSEPROJECT="courseproject";
	//教师简历
	public static final String COLLECTION_RESUME="resume";
	//教师职称信息
	public static final String COLLECTION_TITLE="title";
	//教师行政职务
	public static final String COLLECTION_POSTION="postion";
	//教师成果信息
	public static final String COLLECTION_RESULT="result";
	//教师学历信息
	public static final String COLLECTION_EDUCATION="education";
	//教师工作信息
	public static final String COLLECTION_JOB="job";
	//教师社会兼职信息
	public static final String COLLECTION_PARTTIME="parttime";
	//教师继续教育信息
	public static final String COLLECTION_CONTINUEEDUCATION="continueedu";
	//教师证书信息
	public static final String COLLECTION_CERTIFICATE="certificate";



	//记录录制视频内容
	public static final String COLLECTION_RECORD_VIDEO="recordvideo";
	    /**
		 * 部门常量
		 */
		//保卫科
		public static final String DEPARTMENT_GUARD="保卫科";
		//宿舍管理中心
		public static final String DEPARTMENT_DORM="宿舍管理中心";
		//功能教室管理中心
		public static final String DEPARTMENT_FUNCTION_CLASS="功能教室管理中心";
	//是否有走班配置
	public static final String COLLECTION_ZOUBAN_CONFIG ="zb_config";
	

	//导航
	public static final String COLLECTION_NAV="navs";
	//党建资源表
	public static final String COLLECTION_PARTYRESOURCE="partyres";
	//党建用户表
	public static final String COLLECTION_PARTYUSER="partyuser";
	//用户log
	public static final String COLLECTION_USER_LOG_NAME="ulog";
	
	//========教师评价=============
	//人员分组
	public static final String COLLECTION_TE_MEMBERGROUP = "te_memberGroup";
	//评分比重
	public static final String COLLECTION_TE_PROPORTION = "te_proportion";
	//考核要素+量化成绩
	public static final String COLLECTION_TE_ELEMENT = "te_element";
	//教师评价设置  包含  等第设置 评分时间 评比规则
	public static final String COLLECTION_TE_SETTING = "te_setting";
	//考核详情
	public static final String COLLECTION_TE_ITEM = "te_item";
	//考核老师
	public static final String COLLECTION_TE_TEACHER = "te_teacher";

	//日志报表邮箱管理
	public static final String COLLECTION_EMAIL_MANAGE = "emailmanage";
	//值班设定
	public static final String COLLECTION_DUTYSET_NAME="dutyset";
	//值班时间设定
	public static final String COLLECTION_DUTYSETTIME_NAME="dutyset_time";
	//值班项目设定
	public static final String COLLECTION_DUTYSETPROJECT_NAME="duty_project";
	//值班
	public static final String COLLECTION_DUTY_NAME="duty";
	//值班模板
	public static final String COLLECTION_DUTY_MODEL_NAME="duty_model";
	//值班模板
	public static final String COLLECTION_DUTY_USER_NAME="duty_user";
	//值班模板
	public static final String COLLECTION_DUTY_SHIFT_NAME="duty_shift";

	//一卡通账号信息
	public static final String COLLECTION_ACCOUNT_INFO = "accountinfo";

	//一卡通考勤信息
	public static final String COLLECTION_KAOQIN_INFO = "kaoqininfo";

	//一卡通考勤统计
	public static final String COLLECTION_KAOQIN_STATE = "kaoqinstate";

	//一卡通考勤时间设定
	public static final String COLLECTION_KAOQIN_TIME_SET = "kaoqintimeset";

	//一卡通交易信息
	public static final String COLLECTION_TRANS_INFO = "transinfo";

	//一卡通交易信息
	public static final String COLLECTION_DOOR_INFO = "doorinfo";

	//值班模板
	public static final String COLLECTION_OVERTIME_NAME="overtime";

	//值班模板
	public static final String COLLECTION_OVERTIME_MODEL_NAME="overtime_model";

	//值班模板
	public static final String COLLECTION_DUTY_MODEL_DETAIL_NAME="model_detail";

	//会议
	public static final String COLLECTION_MEETING_INFO = "meeting";

	//会议投票
	public static final String COLLECTION_MEETING_VOTE_INFO = "vote";

	//会议记录
	public static final String COLLECTION_MEETING_LOG_INFO = "meetlog";

	public static final String COLLECTION_MEETING_MESSAGE_INFO = "meetmsg";



	//杨浦小学 综合素质 分数设置信息
	public static final String COLLECTION_OVERALL_QUALITY_SCORE_SET = "overallQualityScoreSet";

	//杨浦小学 综合素质 项目设置信息
	public static final String COLLECTION_OVERALL_QUALITY_ITEM = "overallQualityItem";

	//杨浦小学 综合素质 班级
	public static final String COLLECTION_CLASS_OVERALL_QUALITY = "classOverallQuality";

	//杨浦小学  班级综合素质分数
	public static final String COLLECTION_CLASS_OVERALL_QUALITY_SCORE = "classOverallQualityScore";

	//杨浦小学 综合素质 学生
	public static final String COLLECTION_STU_OVERALL_QUALITY = "stuOverallQuality";

	//杨浦小学  学生形象币
	public static final String COLLECTION_STU_OVERALL_QUALITY_SCORE = "stuOverallQualityScore";

	//杨浦小学 综合素质 商品
	public static final String COLLECTION_OVERALL_QUALITY_GOODS = "overallQualityGoods";

	//杨浦小学 综合素质 商品兑换
	public static final String COLLECTION_STU_CHANGE_GOODS = "stuChangeGoods";

	//拓展课 学生评论
	public static final String COLLECTION_EVALUATE_RESULT = "evaluateResult";

	//指标副本的使用记录
	public static final String COLLECTION_INDICATOR_TREE_APPLIED = "indicatorTreeApplied";

	//拓展课 学生评论次数
	public static final String COLLECTION_EVALUATE_TIMES = "evaluateTime";
	
	//宿舍区
	public static final String COLLECTION_OA_DORM="darea";
	//宿舍楼
	public static final String COLLECTION_DORM_LOOP="dloop";
	//宿舍层
	public static final String COLLECTION_DORM_ROOM="droom";
	//迁出表
	public static final String COLLECTION_DORM_MOVE="dmove";

	//迁出表
	public static final String COLLECTION_QUALITY_INFO="quality";

	
	// 学生考勤信息表
	public static final String COLLECTION_STU_ATTENDANCE = "stu_attendance";

	//老师计划
	public static final String COLLECTION_PLAIN_INFO="plain";

	//老师检查统计
	public static final String COLLECTION_TEACH_CHECK_INFO="tcheck";


	//app图标导航
	public static final String COLLECTION_APPNAV="appnavs";

	//年级
	//一般评论指标
	public static final  String COLLECTION_CCOMMENTKPI="cckpis";
	//一般评论
	public static final  String COLLECTION_CCOMMENT="ccomments";
	//年级老师学科表
	public static final String COLLECTION_GRADE_USER="newgradeuser";

	//学科
	public static final String COLLECTION_SUBJECT = "newsubject";

	//教师简历
	public static final String COLLECTION_TEACHER_RESUME="newresume";
	
	
	public static final String COLLECTION_NEW_GRADE_NAME="newgrade";
	                                     
	//考试及第
	public static final String COLLECTION_PERFORMANCELEVEL_NAME="perlevels";
	
	//教材
	public static final String COLLECTION_TEACHERBOOK_NAME="teacherbooks";

	public static final String COLLECTION_JIAOANS_NAME="jiaoanjilu";
	//教材
	public static final String COLLECTION_TEACHERDICTIONARY_NAME="teacherDictionary";
	//学校教材
	public static final String COLLECTION_SchoolDictionary_NAME="SchoolDictionary";
	//教案
	public static final String COLLECTION_JIAOAN_NAME="jiaoans";
	//教案修改
	public static final String COLLECTION_JIAOAN_NAME_Up="jiaoanup";
	//微校园设置
	public static final String COLLECTION_HOMESCHOOL_SET="homeSchoolSet";



	//+++++++++++++++++++oa管理++++++++++++++++++
	//工资表
	public static final String COLLECTION_OA_SALARY = "oaSalary";
	//工资项目表
	public static final String COLLECTION_OA_SALARY_ITEM = "oaSalaryItem";
	//工资记录表
	public static final String COLLECTION_OA_MONEY = "oaSalaryMoney";

	  //用户简单日志表
    public static final String COLLECTION_USER_SIMPLELOG = "appsimlogs";

	//电子班牌对应关系
	public static final String COLLECTION_BANPAI_RELATION = "bprelation";
	
	
	//第三方手镯事件记录表
	public static final String COLLECTION_BRACELETEVENTLOG_NAME="braceletelog";
	//第三方手镯账户表
	public static final String COLLECTION_BRACELETACCOUNT_NAME="braceletelog";
	
	//教室预定
    public static final String COLLECTION_CLASSROOMRESERVE = "crreserve";
    //非预定教室
    public static final String COLLECTION_NOTCLASSROOMRESERVE = "cnotrreserve";

    //预定教室审核人
    public static final String COLLECTION_CLASSROOMRESERVEUSER = "crreserveadmin";


	//教室服务人员
	public static final String COLLECTION_CLASSROOMRESERVUSERS = "crreserveusers";


	//教室服务
	public static final String COLLECTION_ROOMRES = "roomres";
	
	// 成绩分析-参数缓存key
	public static final String COLLECTION_REFLECTPARM = "cjanalyzereflectparm";

	//电子班牌-班牌班级关系
	public static final String COLLECTION_DZBP_CLIENT_CLASS_RELA = "dzbp_ClientClassRela";
	
	//新3+3课表
	public static final String COLLECTION_STUDENT_SELECT_LESSONS = "n_stuselless";
	public static final String COLLECTION_SCHOOL_SELECT_LESSONS_SET = "n_schsellset";
	// 天区间
	public static final String COLLECTION_SCHOOL_DAY_RANGE = "n33_dayqj";
	// 课节区间
	public static final String COLLECTION_SCHOOL_COURSE_RANGE = "n33_kbjg";
	// 教学周
	public static final String COLLECTION_NEW33_WEEK = "n33_jxz";
	/** 新3+3隔离层**/
	public static final String COLLECTION_NEW33_ISOLATE_CLASSROOM = "n33_isolateclassroom";




	public static final String COLLECTION_NEW33_ISOLATE_GRADE = "n33_isolategrade";




	public static final String COLLECTION_NEW33_ISOLATE_SUBJECT = "n33_isolatesubject";
	public static final String COLLECTION_NEW33_ISOLATE_TERM = "n33_isolateterm";
	public static final String COLLECTION_NEW33_ISOLATE_Tea = "n33_isolateTea";
	public static final String COLLECTION_NEW33_ISOLATE_Class = "n33_isolateClass";
	public static final String COLLECTION_NEW33_ISOLATE_Stu = "n33_isolateStudent";
	public static final String COLLECTION_NEW33_ISOLATE_Manager = "n33_isolateManager";
	public static final String COLLECTION_NEW33_SETZOUBANTIME = "n33_setZouBanTime";
	public static final String COLLECTION_NEW33_CHOOSESET = "n33_chooseSet";

	public static final String COLLECTION_NEW33_DAKA = "n33_daka";  //记录周课表中的学生是否打卡
	/** -------------**/
	//教学班冲突
	public static final String COLLECTION_NEW33_JXBCT = "n33_jxbct";

	//教学班
	public static final String COLLECTION_NEW33_JXB = "n33_jxb";
	//教学班临时表
	public static final String COLLECTION_NEW33_JXB_TEMP = "n33_jxb_temp";

	//换课记录
	public static final String COLLECTION_NEW33_EXCLASSRECORD = "n33_exClassRecord";

	//教学班
	public static final String COLLECTION_NEW33_TURNOFF = "n33_turnoff";
	//考试
	public static final String COLLECTION_EXAMRESULT_XYZL="examxyzl";

	//重分行政班(班级)
	public static final String COLLECTION_NEW33_AFRESH_CLASS = "n33_afreshClass";
	//重分行政班(设置)
	public static final String COLLECTION_NEW33_AFRESH_SET_CLASS = "n33_afreshSetClass";
	//重分行政班(规则)
	public static final String COLLECTION_NEW33_AFRESH_RULE_CLASS = "n33_afreshRuleClass";

	//重分行政班(type)
	public static final String COLLECTION_NEW33_AFRESH_TYPE = "n33_afreshType";

	//重分行政班(学科规则)
	public static final String COLLECTION_NEW33_AFRESH_SUB_RULE = "n33_afreshSubRule";

	public static final String COLLECTION_PERFORMANCE_XYZL="performancexyzl";

	//发布
	public static final String COLLECTION_NEW33_FABU = "n33_fabu";

	//虚拟班
	public static final String COLLECTION_NEW33_VIRTUALCLASS = "n33_virtualclass";

	//自动排课规则走班设置
	public static final String COLLECTION_NEW33_ZOUBANSET = "n33_zoubanset";
	//自动排课规则学科设置
	public static final String COLLECTION_NEW33_SUBJECTSET = "n33_subjectset";

	public static final String COLLECTION_NEW33_GDSX = "n33_gdsx";

	//增减调课
	public static final String COLLECTION_NEW33_ZJTK = "n33_zjtk";

	//教学班组合
	public static final String COLLECTION_NEW33_JXBCombine = "n33_jxbcombine";

	//事务
	public static final String COLLECTION_NEW33_SW = "n33_sw";
	
	public static final String COLLECTION_NEW33_MRXQ = "n33_deaultTerm";

	//事务类别
	public static final String COLLECTION_NEW33_SWLB = "n33_swlb";

	//教师需求
	public static final String COLLECTION_NEW33_TEARULES = "n33_teaRules";
	//教师需求
	public static final String COLLECTION_NEW33_REQUIRETIME = "n33_requireTime";

	//源课表
	public static final String COLLECTION_NEW33_YKB = "n33_ykb";

	//教学班课时
	public static final String COLLECTION_NEW33_JXBKS = "n33_jxbks";

	//教学班课时临时表
	public static final String COLLECTION_NEW33_JXBKS_TEMP = "n33_jxbks_temp";

	//调课记录
	public static final String COLLECTION_NEW33_TKLOG = "n33_tklog";
	//发布记录
	public static final String COLLECTION_NEW33_FABULOG = "n33_fblog";


	//周课表
	public static final String COLLECTION_NEW33_ZKB = "n33_zkb";
	//周课表临时表
	public static final String COLLECTION_NEW33_ZKB_TEMP = "n33_zkb_temp";
	//命名课表类型
	public static final String COLLECTION_NEW33_NAMEKBTYPE = "n33_nameType";


	//命名课表类型
	public static final String COLLECTION_NEW33_NAMEKB = "n33_name";

	// 学生标签
	public static final String COLLECTION_NEW33_STUDENT_TAG = "n33_studenttag";

	//分时间段组合
	public static final String COLLECTION_NEW33_TIMECOMBINE = "n33_time_combine";


	public static final String COLLECTION_NEW33_LIMITHOUR = "n33_limithour";

	public static final String COLLECTION_NEW33_GRADEWEEK = "n33_gradeweek";

	//同步教学班中间表
	public static final String COLLECTION_NEW33_SYSN = "n33_sysn";

	public static final String COLLECTION_NEW33_ZHUANG_XIANG = "n33_ZhuanXiang";

	//记录多步调课是否保存提交
	public static final String COLLECTION_NEW33_COUNT = "n33_count";

	//同步指标记录
	public static final String COLLECTION_NEW33_SYNRECORD = "n33_synrecord";

	public static final String COLLECTION_NEW33_ZIXIBAN = "n33_zixiban";


	public static final String COLLECTION_NEW33_PAIKESHEZHI = "n33_PaiKeSheZhi";

	public static final String COLLECTION_NEW33_PAIKEZUHE = "n33_PaiKeZuHe";

	public static final String COLLECTION_NEW33_TIEM_COMB_KEBIAO = "n33_TimeCombKeBiao";

	public static final String COLLECTION_NEW33_REMOVEDXY = "n33_removedXY";
	public static final String COLLECTION_NEW33_TiaoKeShengQing = "n33_TiaoKeShengQing";
	
	
	//=====================================自动排课=========================================
	
	//自动排课-规则设置-教师设置
	public static final String COLLECTION_NEW33_AUTOTEASET = "n33_autoTeaSet";
	
	//自动排课-规则设置-教师周任课天数 
	public static final String COLLECTION_NEW33_TEAWEEKCOURSE = "n33_autoTeaWeekCourse";
	
	//自动排课-规则设置-教师周任课天数 
	public static final String COLLECTION_NEW33_TEADAY = "n33_autoTeaDay";
	
	//自动排课-规则设置-教师跨班进度一致
	public static final String COLLECTION_NEW33_TEASTEP = "n33_autoTeaStep";
	
	//自动排课-规则设置-教师跨班进度一致
	public static final String COLLECTION_NEW33_TEAMUTEX = "n33_autoTeaMutex";
	
	//自动排课-规则设置-其他设置
	public static final String COLLECTION_NEW33_AUTOOTHERSET = "n33_autoOtherSet";
	
	//自动排课-规则设置-其他设置-选择可排学科
	public static final String COLLECTION_NEW33_OSUBCHECK = "n33_autoSubChenk";
	
	//自动排课-规则设置-其他设置-不可排课格子
	public static final String COLLECTION_NEW33_PKGZ = "n33_autoPkGz";

	//自动排课-规则设置-其他设置-最大分段数量设置
	public static final String COLLECTION_NEW33_MAX_FEN_DUAN_SET = "n33_autoMaxFenDuanSet";

	//自动排课-规则设置-其他设置-分段设置
	public static final String COLLECTION_NEW33_PART_CLASS_SET = "n33_autoPartClassSet";
	//=====================================自动排课=========================================
}
