package com.pojo.user;


import com.mongodb.BasicDBList;
import com.pojo.app.IdValuePair;
import com.pojo.utils.MongoUtils;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import com.sys.utils.NameShowUtils;


import java.util.ArrayList;
import java.util.List;



/**
 * 用户信息表,包括以前系统学生和老师等信息
 * <pre>
 * collectionName:users
 * </pre>
 * <pre>
 * {
 *  nm:用户名称
 *  pw:密码
 *  sex: 0女 1 男  -1：未知
 *  nnm:昵称
 *  r:角色
 *  per:角色外具有的权限；用“,”分割
 *  rper:角色内去除的权限；用“,”分割
 *  mn:手机
 *  pn:电话
 *  e:邮箱
 *  qq:
 *  wx:微信
 *  wb:微博
 *  add:地址
 *  pc:邮编
 *  by:血型
 *  rt:注册时间,long
 *  ri:注册IP
 *  si:学校ID
 *  bal:余额;迁移至mysql
 *  bir:生日.long
 *  lad:最后登录日期，long
 *  exp:经验值
 *  cid:关联账号(如果是家长，此字段是子女ID；如果是子女，此字段是家长ID)
 *  [
 *   
 *  ]
 *  avt:头像
 *  gli:群组列表;IdValuePair
 *    [
 *        {
 *         id:String
 *         v:群名称
 *        }
 *    ]
 *
 *    kt:是否是k6kt用户  0不是  1是
 *  
 *  
 *  //----------学生字段begin----------
 *  sn:学号 
 *  rn:学籍号
 *  //----------学生字段end----------
 *  
 *  
 *  //----------老师字段begin----------
 *  int:简介
 *  jnb:工号
 *  //----------老师字段end----------
 *  
 *  syn：是否需要同步 0:需要 1已经完成
 *  ir：是否被删除 1已经被删除 0 没有
 *  chatid : 环信聊天id
 *
 *  shd: 微家园更新时间
 *  fhd: 微校园更新时间
 *  ijy:禁言
 *  jydt:禁言时间
 *  
 *  
 *  //----------学籍管理begin----------
 *  民族 : sra(studentRace) 
 *  健康状态 : she(studentHealth)
 *  户籍地址 : rad(residenceAddress)
 *  变动日期：cdt(changeDate)
 *  变动说明：cps(changePostscript)
 *  新学校     : nsc(newSchool)
 *  //----------学籍管理end----------
 *  
 *  
 *  uty:用户类型 0默认用户  1：有效时间用户
 *  vabt:此用户的有效开始时间
 *  vatl:此用户的有效时长，从第一次开始登录算起，单位s(秒)
 *  pos:编制
 *  posdec:职务
 *  sis:学校集合.用于学校分校区情况，每个校区都单独看作一个学校;用于大校长，分区校长不用；
 *  [
 *   
 *  ]
 *  logn:登录名
 *  bnm:绑定用户名 用于ahedu.cn的绑定
 *  
 *  
 *  yhbnm:有鸿用户绑定
 *  binds:
 *  {
 *    ty: 1安徽省平台绑定  2 有红用户绑定 3 高新一中用户  10000深圳中学
	  bv
 *  }
 *  coupon:優惠券
 *  sid:身份证
 *  lt:首字母
 * }
 * </pre>
 * @author fourer
 */
public class UserEntry extends BaseDBObject {

    /**
     *
     */
    private static final long serialVersionUID = 4002785167984267964L;
    
	public UserEntry(){}

	public UserEntry(DBObject dbo) {
		this((BasicDBObject)dbo);
	}
    
	
	public UserEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}


	public UserEntry(String userName, String password,
			String mobile,String email) {
		 this(userName,  //userName
			  password,  //password
				 Constant.NEGATIVE_ONE,    //sex
				 Constant.EMPTY, //nickName
				 Constant.DEFAULT_VALUE_INT,//role
				 Constant.EMPTY,//permission
				 Constant.EMPTY,//removePermission
				 mobile,//mobileNumber
				 Constant.EMPTY, //phoneNumber
				 email, //email
				 Constant.EMPTY, //qq
				 Constant.EMPTY, //weiXin
				 Constant.EMPTY, //weiBo
				 Constant.EMPTY, //address
				 Constant.EMPTY, //postCode
				 Constant.NEGATIVE_ONE, //bloodType
				 System.currentTimeMillis(), //registerTime
				 Constant.EMPTY, //registerIP
				 null, //schoolID
				 Constant.DEFAULT_VALUE_LONG, //birthDate
				 0L, //lastActiveDate
				 Constant.ZERO , //experiencevalue
				 Constant.EMPTY,//studyNum
				 Constant.EMPTY,//registerNum
				 Constant.EMPTY,//job
				 Constant.EMPTY,//introduce
				 Constant.EMPTY,//jobnumber
				 Constant.SYN_YES_NEED,//syn，
				 null, //connectId,
				 Constant.EMPTY, //avatar
				 null,
				 Constant.DEFAULT_VALUE_LONG,
				 Constant.DEFAULT_VALUE_LONG,
				 Constant.DEFAULT_VALUE_INT,
				 Constant.DEFAULT_VALUE_LONG,
				 1,
				 Constant.EMPTY,0,"","");
	}



	public UserEntry(String userName, String password,
			int sex,List<IdValuePair> groupInfoList) {
		 this(userName,  //userName
			  password,  //password
				 sex,    //sex
				 Constant.EMPTY, //nickName
				 Constant.DEFAULT_VALUE_INT,//role
				 Constant.EMPTY,//permission
				 Constant.EMPTY,//removePermission
				 Constant.EMPTY,//mobileNumber
				 Constant.EMPTY, //phoneNumber
				 Constant.EMPTY, //email
				 Constant.EMPTY, //qq
				 Constant.EMPTY, //weiXin
				 Constant.EMPTY, //weiBo
				 Constant.EMPTY, //address
				 Constant.EMPTY, //postCode
				 Constant.NEGATIVE_ONE, //bloodType
				 System.currentTimeMillis(), //registerTime
				 Constant.EMPTY, //registerIP
				 null, //schoolID
				 Constant.DEFAULT_VALUE_LONG, //birthDate
				 0L, //lastActiveDate
				 Constant.ZERO , //experiencevalue
				 Constant.EMPTY,//studyNum
				 Constant.EMPTY,//registerNum
				 Constant.EMPTY,//job
				 Constant.EMPTY,//introduce
				 Constant.EMPTY,//jobnumber
				 Constant.SYN_YES_NEED,//syn，
				 null, //connectId,
				 Constant.EMPTY, //avatar
				 groupInfoList,
				 Constant.DEFAULT_VALUE_LONG,
				 Constant.DEFAULT_VALUE_LONG,
				 Constant.DEFAULT_VALUE_INT,
				 Constant.DEFAULT_VALUE_LONG,
				 1,
				 Constant.EMPTY,0,"","");
	}

	public UserEntry(String userName, String password,
					 int sex,List<IdValuePair> groupInfoList,String postionDec) {
		this(userName,  //userName
				password,  //password
				sex,    //sex
				Constant.EMPTY, //nickName
				Constant.DEFAULT_VALUE_INT,//role
				Constant.EMPTY,//permission
				Constant.EMPTY,//removePermission
				Constant.EMPTY,//mobileNumber
				Constant.EMPTY, //phoneNumber
				Constant.EMPTY, //email
				Constant.EMPTY, //qq
				Constant.EMPTY, //weiXin
				Constant.EMPTY, //weiBo
				Constant.EMPTY, //address
				Constant.EMPTY, //postCode
				Constant.NEGATIVE_ONE, //bloodType
				System.currentTimeMillis(), //registerTime
				Constant.EMPTY, //registerIP
				null, //schoolID
				Constant.DEFAULT_VALUE_LONG, //birthDate
				0L, //lastActiveDate
				Constant.ZERO , //experiencevalue
				Constant.EMPTY,//studyNum
				Constant.EMPTY,//registerNum
				Constant.EMPTY,//job
				Constant.EMPTY,//introduce
				Constant.EMPTY,//jobnumber
				Constant.SYN_YES_NEED,//syn，
				null, //connectId,
				Constant.EMPTY, //avatar
				groupInfoList,
				Constant.DEFAULT_VALUE_LONG,
				Constant.DEFAULT_VALUE_LONG,
				Constant.DEFAULT_VALUE_INT,
				Constant.DEFAULT_VALUE_LONG,
				1,
				postionDec,0,"","");
	}
	
	


	public UserEntry(String userName, String password,
			int sex, String nickName, int role, String permission,String removePermission,
			String mobileNumber, String phoneNumber, String email, String qq,
			String weiXin, String weiBo, String address, String postCode,
			int bloodType, long registerTime, String registerIP, ObjectId schoolID,
			long birthDate, long lastActiveDate,
			int experiencevalue, String studyNum, String registerNum,String job,

			String introduce, String jobnumber,int syn,List<ObjectId> connectId,String avatar,List<IdValuePair> groupInfoList,long schoolHomeDate,long familyHomeDate,int jinyan,long jinyanDate,int postion,String postionDec,int chat,String sid,String letter) {

		 super();
		 
		 BasicDBObject dbo =new BasicDBObject()
				 .append("nm", userName.toLowerCase())
		                    .append("pw", password)
		                    .append("sex", sex)
		                    .append("nnm", nickName)
		                    .append("r", role)
		                    .append("per", permission)
		                    .append("rper", removePermission)
		                    .append("mn", mobileNumber)
		                    .append("pn", phoneNumber)
		                    .append("e", email)
		                    .append("qq", qq)
		                    .append("wx", weiXin)
		                    .append("wb", weiBo)
		                    .append("add", address)
		                    .append("pc", postCode)
		                    .append("by", bloodType)
		                    .append("rt", registerTime)
		                    .append("ri", registerIP)
		                    .append("si", schoolID)
		                    .append("bir", birthDate)
		                    .append("lad", lastActiveDate)
		                    .append("exp", experiencevalue)
		                    .append("sn", studyNum)
		                    .append("rn", registerNum)
				 .append("jo", job)
							.append("int", introduce)
				 .append("jnb", jobnumber)
		                    .append(Constant.FIELD_SYN, syn)
		                    .append("cid", MongoUtils.convert(connectId))
		                    .append("avt", avatar)
				 .append("gli", MongoUtils.convert(MongoUtils.fetchDBObjectList(groupInfoList)))
				 			.append("ir", Constant.ZERO)
				 .append("chatid", Constant.EMPTY)
				 .append("shd", schoolHomeDate)
				 .append("fhd", familyHomeDate)
				 .append("ijy", jinyan)
				 .append("jydt", jinyanDate)
				 .append("pos",postion)
				 .append("posdec",postionDec)
				 .append("ic",chat)
				 .append("coupon",Constant.ZERO)
				 .append("sid", sid)
				 .append("lt",letter)
		                    ;
		 
		 
		 setBaseEntry(dbo);
	}
	public int getPostion() {
		return getSimpleIntegerValueDef("pos", 1);
	}

	public void setPostion(int postion) {
		setSimpleValue("pos",postion);
	}

	public int getCoupon() {
		return getSimpleIntegerValueDef("coupon",0);
	}

	public void setCoupon(int coupon) {
		setSimpleValue("coupon",coupon);
	}

    public String getChatId() 
    {
    	String chatid= getSimpleStringValue("chatid");
    	if(StringUtils.isBlank(chatid))
    	{
    		ObjectId id=getID();
    		if(null!=id)
    		{
    	     	return id.toString();
    		}
    		return Constant.EMPTY;
    	}
    	return chatid;
    }
    
    public void setChatId(String chatId){setSimpleValue("chatid", chatId);}

	public String getUserName() {
		String nm= getSimpleStringValue("nm");
		return NameShowUtils.showName(nm);
	}
	
	public String getRealUserName() {
		String nm= getSimpleStringValue("nm");
		return nm;
	}
	public void setUserName(String userName) {
		setSimpleValue("nm", userName);
	}
	
	public String getPassword() {
		return getSimpleStringValue("pw");
	}
	public void setPassword(String password) {
		setSimpleValue("pw", password);
	}
	
	public int getSex() {
		return getSimpleIntegerValue("sex");
	}
	public void setSex(int sex) {
		setSimpleValue("sex", sex);
	}
	
	
	public String getNickName() {
		return getSimpleStringValue("nnm");
	}
	public void setNickName(String nickName) {
		setSimpleValue("nnm", nickName);
	}
	
	public int getRole() {
		return getSimpleIntegerValueDef("r",0);
	}
	public void setRole(int role) {
		setSimpleValue("r", role);
	}
	public String getPermission() {
		 return getSimpleStringValue("per");
	}
	public void setPermission(String permission) {
		setSimpleValue("per", permission);
	}
	
	public String getRemovePermission() {
		 return getSimpleStringValue("rper");
	}
	public void setRemovePermission(String removePermission) {
		setSimpleValue("rper", removePermission);
	}
	
	public String getMobileNumber() {
		String mob= getSimpleStringValue("mn");
		if(null==mob)
		{
			return Constant.EMPTY;
		}
		return mob;
	}
	public void setMobileNumber(String mobileNumber) {
		setSimpleValue("mn", mobileNumber);
	}
	public String getPhoneNumber() {
		return getSimpleStringValue("pn");
	}
	public void setPhoneNumber(String phoneNumber) {
		setSimpleValue("pn", phoneNumber);
	}
	
	public String getEmail() {
		String e= getSimpleStringValue("e");
		if(null==e)
		{
			return Constant.EMPTY;
		}
		return e;
	}
	public void setEmail(String email) {
		setSimpleValue("e", email);
	}
	
	public String getQq() {
		return getSimpleStringValue("qq");
	}
	public void setQq(String qq) {
		setSimpleValue("qq", qq);
	}
	public String getWeiXin() {
		return getSimpleStringValue("wx");
	}
	public void setWeiXin(String weiXin) {
		setSimpleValue("wx", weiXin);
	}
	
	public String getWeiBo() {
		return getSimpleStringValue("wb");
	}
	public void setWeiBo(String weiBo) {
		setSimpleValue("wb", weiBo);
	}
	
	public String getAddress() {
		return getSimpleStringValue("add");
	}
	public void setAddress(String address) {
		setSimpleValue("add", address);
	}
	public String getPostCode() {
		return getSimpleStringValue("pc");
	}
	public void setPostCode(String postCode) {
		setSimpleValue("pc", postCode);
	}
	
	public int getBloodType() {
		return getSimpleIntegerValue("by");
	}
	public void setBloodType(int bloodType) {
		setSimpleValue("by", bloodType);
	}
	
	
	public long getRegisterTime() {
		return getSimpleLongValue("rt");
	}
	public void setRegisterTime(long registerTime) {
		setSimpleValue("rt", registerTime);
	}
	
	public String getRegisterIP() {
		return getSimpleStringValue("ri");
	}
	public void setRegisterIP(String registerIP) {
		setSimpleValue("ri", registerIP);
	}
	

	public ObjectId getSchoolID() {
        return getSimpleObjecIDValue("si");
	}
	public void setSchoolID(ObjectId schoolID) {
		setSimpleValue("si", schoolID);
	}

//	public double getBalance() {
//		return getSimpleDoubleValue("bal");
//	}
//	public void setBalance(double balance) {
//		setSimpleValue("bal", balance);
//	}
	
	public long getBirthDate() {
		if(getBaseEntry().containsField("bir")){
			return getSimpleLongValue("bir");
		} else {
			return 0;
		}
	}
	public void setBirthDate(long birthDate) {
		setSimpleValue("bir", birthDate);
	}
	public long getLastActiveDate() {
		return getSimpleLongValueDef("lad",0L);
	}
	public void setLastActiveDate(long lastActiveDate) {
		setSimpleValue("lad", lastActiveDate);
	}
	public int getExperiencevalue() {
		return getSimpleIntegerValueDef("exp", 0);
	}
	public void setExperiencevalue(int experiencevalue) {
		setSimpleValue("exp", experiencevalue);
	}
	
	public String getStudyNum() {
		if(this.getBaseEntry().containsField("sn"))
		{
			return getSimpleStringValue("sn");
		}
		return "";
	}
	public void setStudyNum(String studyNum) {
		setSimpleValue("sn", studyNum);
	}
	public String getRegisterNum() {
		return getSimpleStringValue("rn");
	}
	public void setRegisterNum(String registerNum) {
		setSimpleValue("rn", registerNum);
	}
	
	public String getIntroduce() {
		return getSimpleStringValue("int");
	}
	public void setIntroduce(String introduce) {
		setSimpleValue("int", introduce);
	}
	
	public String getJobnumber() {
		return getSimpleStringValue("jnb");
	}
	
	public void setJobnumber(String jobnumber) {
		setSimpleValue("jnb", jobnumber);
	}

	public int getChat() {
		return getSimpleIntegerValue("ic");
	}

	public void setChat(int chat) {
		setSimpleValue("ic",chat);
	}

    //----------------兼容之前的版本-------------------//
//	public ObjectId getConnectId() {
//		return getSimpleObjecIDValue("cid");
//	}
//	public void setConnectId(ObjectId connectId) {
//		setSimpleValue("cid", connectId);
//	}
	//----------------兼容之前的版本-------------------//
	
	

	public List<ObjectId> getConnectIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cid");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(((ObjectId)o));
			}
		}
		return retList;
	}

	public void setConnectIds(List<ObjectId> connectId) {
		setSimpleValue("cid", MongoUtils.convert(connectId));
	}
	public String getAvatar() {
		return getSimpleStringValue("avt");
	}
	public void setAvatar(String avatar) {
		setSimpleValue("avt", avatar);
	}


	public long getSchoolHomeDate() {
		return getSimpleLongValue("shd");
	}
	public void setSchoolHomeDate(long schoolHomeDate) {
		setSimpleValue("shd", schoolHomeDate);
	}

	public long getFamilyHomeDate() {
		return getSimpleLongValue("fhd");
	}
	public void setFamilyHomeDate(long familyHomeDate) {
		setSimpleValue("fhd", familyHomeDate);
	}

	public List<IdValuePair> getGroupInfoList() {
		List<IdValuePair> retList =new ArrayList<IdValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("gli");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setGroupInfoList(List<IdValuePair> groupInfoList) {
		List<DBObject> list = MongoUtils.fetchDBObjectList(groupInfoList);
		setSimpleValue("gli",  MongoUtils.convert(list));
	}
	public void setJob(String job)
	{
		setSimpleValue("jo",job);
	}
	public String getJob()
	{
		if(this.getBaseEntry().containsField("jo"))
		{
			return getSimpleStringValue("jo");
		}
		return Constant.EMPTY;
	}

	public int getIsRemove() {
		if(this.getBaseEntry().containsField("ir"))
		{
		return getSimpleIntegerValue("ir");
		}
		return Constant.ZERO;
	}

	public void setIsRemove(int isRemove) {
		setSimpleValue("ir", isRemove);
	}
	
	//----------学籍管理begin----------
	
	public String getStudentRace() {
		return getSimpleStringValue("sra");
	}
	public void setStudentRace(String studentRace) {
		setSimpleValue("sra", studentRace);
	}
	
	public String getStudentHealth() {
		return getSimpleStringValue("she");
	}
	public void setStudentHealth(String studentHealth) {
		setSimpleValue("she", studentHealth);
	}
	
	
	public String getResidenceAddress() {
		return getSimpleStringValue("rad");
	}
	public void setResidenceAddress(String residenceAddress) {
		setSimpleValue("rad", residenceAddress);
	}
	
	public long getChangeDate() {
		return getSimpleLongValueDef("cdt",-1L);
	}
	public void setChangeDate(String changeDate) {
		setSimpleValue("cdt", changeDate);
	}
	
	public String getChagePostscript() {
		return getSimpleStringValue("cps");
	}
	public void setChagePostscript(String chagePostscript) {
		setSimpleValue("cps", chagePostscript);
	}
	
	public String getNewSchool() {
		return getSimpleStringValue("nsc");
	}
	public void setNewSchool(String newSchool) {
		setSimpleValue("nsc", newSchool);
	}
	//----------学籍管理end----------


	public int getK6KT(){
		return getSimpleIntegerValueDef("kt", 1);
	}

	public void setK6KT(int kt) {
		setSimpleValue("kt", kt);
	}
	public int getJinyan() {
		return getSimpleIntegerValueDef("ijy",0);
	}
	public void setJinyan(int jinyan) {
		setSimpleValue("ijy",jinyan);
	}
	public long getJinyanDate() {
		return getSimpleLongValue("jydt");
	}
	public void setJinyanDate(long jinyanDate) {
		setSimpleValue("jydt", jinyanDate);
	}

	public long getValidBeginTime() {
		return getSimpleLongValueDef("vabt", 0L);
	}

	public void setValidBeginTime(long validBeginTime) {
		setSimpleValue("vabt", validBeginTime);
	}

	public long getValidTime() {
		return getSimpleLongValueDef("vatl",Constant.SECONDS_IN_DAY*5);
	}

	public void setValidTime(long validTime) {
		setSimpleValue("vatl", validTime);
	}

	public int getUserType() {
		return getSimpleIntegerValueDef("uty", 0);
	}

	public void setUserType(int userType) {
		setSimpleValue("uty", userType);
	}

	public List<ObjectId> getSchoolIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sis");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(((ObjectId)o));
			}
		}
		return retList;
	}

	public void setSchoolIds(List<ObjectId> schoolIds) {
		setSimpleValue("sis",  MongoUtils.convert(schoolIds));
	}

	public String getLoginName() {
		String s= getSimpleStringValue("logn");
		if(null==s)
		{
			return Constant.EMPTY;
		}
		return s;
	}

	public void setLoginName(String loginName) {
		setSimpleValue("logn", loginName);
	}


	public String getBindName() {
		return getSimpleStringValue("bnm");
	}

	public void setBindName(String bindName) {
		setSimpleValue("bnm", bindName);
	}
	
	
	public String getYhBindName() {
		return getSimpleStringValue("yhbnm");
	}

	public void setYhBindName(String bindName) {
		setSimpleValue("yhbnm", bindName);
	}
	
	

	public String getPostionDec() {
		return getSimpleStringValue("posdec");
	}

	public void setPostionDec(String postionDec) {
		setSimpleValue("posdec",postionDec);
	}
	
	

	public String getSid() {
		return getSimpleStringValue("sid");
	}

	public void setSid(String sid) {
		setSimpleValue("sid",sid);
	}

	public String getLetter() {
		return getSimpleStringValue("lt");
	}

	public void setLetter(String letter) {
		setSimpleValue("lt",letter);
	}
	
	public Binds getUserBind() {
		BasicDBObject dbo=(BasicDBObject)getSimpleObjectValue("binds");
		
		if(null!=dbo)
		{
			return new Binds(dbo);
		}
		return null;
	}

	public void setUserBind(Binds userBind) {
		setSimpleValue("binds", userBind.getBaseEntry());
	}
	
	public int getMembership() {
		return getSimpleIntegerValueDef("msp", 1);
	}

	public void setMembership(int membership) {
		setSimpleValue("msp",membership);
	}

	/**
	 * 
	 * @author fourer
	 * {
	 *  ty:
	 *  bv
	 * }
	 */
	public static class Binds extends BaseDBObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7161013551691155805L;
		
		
		public Binds(BasicDBObject baseEntry) {
			super(baseEntry);
			setBaseEntry(baseEntry);
		}

		public Binds(int type, String bindValue) {
			BasicDBObject baseEntry =new BasicDBObject()
			.append("ty", type)
			.append("bv", bindValue);
			setBaseEntry(baseEntry);
		}
		
		public int getType() {
			return getSimpleIntegerValue("ty");
		}
		public void setType(int type) {
			setSimpleValue("ty", type);
		}
		public String getBindValue() {
			return getSimpleStringValue("bv");
		}
		public void setBindValue(String bindValue) {
			setSimpleValue("bv", bindValue);
		}
		
	}
	
}
