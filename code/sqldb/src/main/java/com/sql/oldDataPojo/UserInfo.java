package com.sql.oldDataPojo;
import java.io.Serializable;
import java.util.Date;
/**
 * Created by qinbo on 15/3/17.
 */
public class UserInfo implements Serializable{
    private static final long serialVersionUID = 3848141038233363069L;
    private int id;
    private String UserName;
    private int Sex;
    private String NickName;
    private String IDCard;
    private int Role;
    private String MobileNumber;
    private String PhoneNumber;
    private String Email;
    private String QQ;
    private Long WeiXin;
    private Long WeiBo;
    private String Address;
    private String PostCode;
    private int BloodType;
    private String Password;
    private Date RegisterTime;
    private int Level;
    private int Credit;
    private String maxImageURL;
    private String middleImageURL;
    private String minImageURL;
    private String Signature;
    private Long RegisterIP;
    private int CURRENTSCHOOLID;
    private Date CURRENTENROLLDATE;
    private String LEARNINGHISTORY;
    private int SchoolID;
    private String SchoolName;
    private String JobTitle;
    private Date OnBoardDate;
    private String Profile;
    private String Detail;
    /**
     * 地区ID
     */
    private int GeoId;
    private double Balance;
    private String CurrentSchoolName;
    private String DreamSchoolName;
    private int GradeId;
    private int DreamSchoolId;
    private String studentid;
    private boolean initialized;
    private String pwdProtectA;
    private int pwdProtectQ;
    private String birthDate;
    private int payType;
    private String activeCardNumber;
    private double giftBalance;
    private int experiencevalue;
    private Date lastActiveDate;
    private int registered;
    private String sql;
    private Date qustionMonthPay_endTime;
    private int systemId;
    private int watchVideoTime;
    private String subjectName;
    private int teacherId;
    private Date bloglogintime;
    private String petname;
    private String studentNum;
    private int ismanage;
    private String psdQuestion1;
    private String psdQuestion2;
    private String psdQuestion3;
    private String psdAnswer1;
    private String psdAnswer2;
    private String psdAnswer3;
    private String mainClassName;
    private String cityName;
    private boolean isFriend;
    private int isk6kt;



    //
    private String schoolName4WB;

    public String getSchoolName4WB() {
        return schoolName4WB;
    }

    public void setSchoolName4WB(String schoolName4WB) {
        this.schoolName4WB = schoolName4WB;
    }

    public boolean getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    public Date getQustionMonthPay_endTime() {
        return qustionMonthPay_endTime;
    }

    public void setQustionMonthPay_endTime(Date qustionMonthPay_endTime) {
        this.qustionMonthPay_endTime = qustionMonthPay_endTime;
    }

    public int getWatchVideoTime() {
        return watchVideoTime;
    }

    public void setWatchVideoTime(int watchVideoTime) {
        this.watchVideoTime = watchVideoTime;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public int getRegistered() {
        return registered;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    public String getActiveCardNumber() {
        return activeCardNumber;
    }

    public void setActiveCardNumber(String activeCardNumber) {
        this.activeCardNumber = activeCardNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String iDCard) {
        IDCard = iDCard;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int role) {
        Role = role;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String qQ) {
        QQ = qQ;
    }

    public Long getWeiXin() {
        return WeiXin;
    }

    public void setWeiXin(Long weiXin) {
        WeiXin = weiXin;
    }

    public Long getWeiBo() {
        return WeiBo;
    }

    public void setWeiBo(Long weiBo) {
        WeiBo = weiBo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Date getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(Date lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public int getBloodType() {
        return BloodType;
    }

    public void setBloodType(int bloodType) {
        BloodType = bloodType;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Date getRegisterTime() {
        return RegisterTime;
    }

    public void setRegisterTime(Date registerTime) {
        RegisterTime = registerTime;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getCredit() {
        return Credit;
    }

    public void setCredit(int credit) {
        Credit = credit;
    }


    public String getMaxImageURL() {
        return maxImageURL;
    }

    public void setMaxImageURL(String maxImageURL) {
        this.maxImageURL = maxImageURL;
    }

    public String getMiddleImageURL() {
        return middleImageURL;
    }

    public void setMiddleImageURL(String middleImageURL) {
        this.middleImageURL = middleImageURL;
    }

    public String getMinImageURL() {
        return minImageURL;
    }

    public void setMinImageURL(String minImageURL) {
        this.minImageURL = minImageURL;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public Long getRegisterIP() {
        return RegisterIP;
    }

    public void setRegisterIP(Long registerIP) {
        RegisterIP = registerIP;
    }

    public int getCURRENTSCHOOLID() {
        return CURRENTSCHOOLID;
    }

    public void setCURRENTSCHOOLID(int cURRENTSCHOOLID) {
        CURRENTSCHOOLID = cURRENTSCHOOLID;
    }

    public Date getCURRENTENROLLDATE() {
        return CURRENTENROLLDATE;
    }

    public void setCURRENTENROLLDATE(Date cURRENTENROLLDATE) {
        CURRENTENROLLDATE = cURRENTENROLLDATE;
    }

    public String getLEARNINGHISTORY() {
        return LEARNINGHISTORY;
    }

    public void setLEARNINGHISTORY(String lEARNINGHISTORY) {
        LEARNINGHISTORY = lEARNINGHISTORY;
    }

    public int getSchoolID() {
        return SchoolID;
    }

    public void setSchoolID(int schoolID) {
        SchoolID = schoolID;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String schoolName) {
        SchoolName = schoolName;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public Date getOnBoardDate() {
        return OnBoardDate;
    }

    public void setOnBoardDate(Date onBoardDate) {
        OnBoardDate = onBoardDate;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public int getGeoId() {
        return GeoId;
    }

    public void setGeoId(int geoId) {
        GeoId = geoId;
    }

    public String getCurrentSchoolName() {
        return CurrentSchoolName;
    }

    public void setCurrentSchoolName(String currentSchoolName) {
        CurrentSchoolName = currentSchoolName;
    }

    public String getDreamSchoolName() {
        return DreamSchoolName;
    }

    public void setDreamSchoolName(String dreamSchoolName) {
        DreamSchoolName = dreamSchoolName;
    }

    public int getGradeId() {
        return GradeId;
    }

    public void setGradeId(int gradeId) {
        GradeId = gradeId;
    }

    public int getDreamSchoolId() {
        return DreamSchoolId;
    }

    public void setDreamSchoolId(int dreamSchoolId) {
        DreamSchoolId = dreamSchoolId;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String getPwdProtectA() {
        return pwdProtectA;
    }

    public void setPwdProtectA(String pwdProtectA) {
        this.pwdProtectA = pwdProtectA;
    }

    public int getPwdProtectQ() {
        return pwdProtectQ;
    }

    public void setPwdProtectQ(int pwdProtectQ) {
        this.pwdProtectQ = pwdProtectQ;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public double getGiftBalance() {
        return giftBalance;
    }

    public void setGiftBalance(double giftBalance) {
        this.giftBalance = giftBalance;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public int getExperiencevalue() {
        return experiencevalue;
    }

    public void setExperiencevalue(int experiencevalue) {
        this.experiencevalue = experiencevalue;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Date getBloglogintime() {
        return bloglogintime;
    }

    public void setBloglogintime(Date bloglogintime) {
        this.bloglogintime = bloglogintime;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public int getIsmanage() {
        return ismanage;
    }

    public void setIsmanage(int ismanage) {
        this.ismanage = ismanage;
    }

    public String getPsdQuestion1() {
        return psdQuestion1;
    }

    public void setPsdQuestion1(String psdQuestion1) {
        this.psdQuestion1 = psdQuestion1;
    }

    public String getPsdQuestion2() {
        return psdQuestion2;
    }

    public void setPsdQuestion2(String psdQuestion2) {
        this.psdQuestion2 = psdQuestion2;
    }

    public String getPsdQuestion3() {
        return psdQuestion3;
    }

    public void setPsdQuestion3(String psdQuestion3) {
        this.psdQuestion3 = psdQuestion3;
    }

    public String getPsdAnswer1() {
        return psdAnswer1;
    }

    public void setPsdAnswer1(String psdAnswer1) {
        this.psdAnswer1 = psdAnswer1;
    }

    public String getPsdAnswer2() {
        return psdAnswer2;
    }

    public void setPsdAnswer2(String psdAnswer2) {
        this.psdAnswer2 = psdAnswer2;
    }

    public String getPsdAnswer3() {
        return psdAnswer3;
    }

    public void setPsdAnswer3(String psdAnswer3) {
        this.psdAnswer3 = psdAnswer3;
    }

    public int getIsk6kt() {
        return isk6kt;
    }

    public void setIsk6kt(int isk6kt) {
        this.isk6kt = isk6kt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((NickName == null) ? 0 : NickName.hashCode());
        result = prime * result
                + ((UserName == null) ? 0 : UserName.hashCode());
        result = prime * result + id;
        result = prime * result
                + ((maxImageURL == null) ? 0 : maxImageURL.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserInfo other = (UserInfo) obj;
        if (NickName == null) {
            if (other.NickName != null)
                return false;
        } else if (!NickName.equals(other.NickName))
            return false;
        if (UserName == null) {
            if (other.UserName != null)
                return false;
        } else if (!UserName.equals(other.UserName))
            return false;
        if (id != other.id)
            return false;
        if (maxImageURL == null) {
            if (other.maxImageURL != null)
                return false;
        } else if (!maxImageURL.equals(other.maxImageURL))
            return false;
        return true;
    }


}
