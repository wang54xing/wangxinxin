package com.pojo.newschool;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 老师简历信息
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 *  uid:
 *  sid 学校id
 *  nm:姓名
 *  sex:性别
 *  birth:出生日期
 *  bplace:籍贯birthplace
 *  nation:民族nation
 *  cdnum:身份证号cardnum
 *  maritalst:婚姻状况marital status
 *  residence:户口所在地residence
 *  address:现住地址address
 *  phone:联系电话phone
 *  edu:学历education
 *  major:专业major
 *  snm:毕业院校schoolName
 *  stus:状态status
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/26.
 */
public class ResumeEntry extends BaseDBObject {

    private static final long serialVersionUID = -770716000755469730L;

    public ResumeEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public ResumeEntry(ObjectId userId, String userName, int sex, ObjectId schoolId, List<ObjectId> subjectIds, int status) {
        this(userId,userName,sex,schoolId,"","","","",0,"","","","","","",subjectIds,status);
    }

    public ResumeEntry(ObjectId userId, String userName, int sex, ObjectId schoolId, String birth, String birthPlace, String nation, String cardNum,
                       int maritalStatus, String residence, String address, String phone,
                       String education, String major, String schoolName, List<ObjectId> subjectIds, int status) {
        BasicDBObject dbo =new BasicDBObject()
                .append("uid", userId)
                .append("nm",userName)
                .append("sex",sex)
                .append("sid", schoolId)
                .append("birth",birth)
                .append("bplace",birthPlace)
                .append("nation", nation)
                .append("cdnum", cardNum)
                .append("maritalst", maritalStatus)
                .append("residence", residence)
                .append("address", address)
                .append("phone", phone)
                .append("edu", education)
                .append("major", major)
                .append("snm", schoolName)
                .append("suids", MongoUtils.convert(subjectIds))
                .append("stus",status)
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid",schoolId);
    }
    public String getUserName(){
        return getSimpleStringValue("nm");
    }
    public void setUserName(String userName) {
        setSimpleValue("nm",userName);
    }
    public int getSex(){
        return getSimpleIntegerValueDef("sex",1);
    }
    public void setSex(int sex) {
        setSimpleValue("sex",sex);
    }
    public String getCardNum(){
        return getSimpleStringValue("cdnum");
    }
    public void setCardNum(String cardNum) {
        setSimpleValue("cdnum",cardNum);
    }

    public String getBirth() {
        return getSimpleStringValue("birth");
    }
    public void setBirth(String birth) {
        setSimpleValue("birth",birth);
    }
    public  int getMaritalStatus() {
        return getSimpleIntegerValue("maritalst");
    }
    public void setMaritalStatus(int maritalStatus) {
        setSimpleValue("maritalst",maritalStatus);
    }
    public String getResidence() {
        return getSimpleStringValue("residence");
    }
    public void setResidence(String residence) {
        setSimpleValue("residence",residence);
    }
    public String getAddress() {
        return getSimpleStringValue("address");
    }
    public void setAddress(String address) {
        setSimpleValue("address",address);
    }
    public String getPhone() {
        return getSimpleStringValue("phone");
    }
    public void setPhone(String phone) {
        setSimpleValue("phone",phone);
    }

    public String getEducation() {
        return getSimpleStringValue("edu");
    }

    public void setEducation(String education) {
        setSimpleValue("edu",education);
    }
    public String getMajor() {
        return getSimpleStringValue("major");
    }
    public void setMajor(String major) {
        setSimpleValue("major",major);
    }
    public String getSchoolName() {
        return getSimpleStringValue("snm");
    }
    public void setSchoolName(String schoolName) {
        setSimpleValue("snm",schoolName);
    }

   public String getBirthPlace() {
       return getSimpleStringValue("bplace");
   }
    public void setBirthPlace(String birthPlace) {
        setSimpleValue("bplace",birthPlace);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("uid",userId);
    }


    public String getNation() {
        return getSimpleStringValue("nation");
    }

    public void setNation(String nation) {
        setSimpleValue("nation",nation);
    }

    public List<ObjectId> getSubjectIds() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("suids");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(((ObjectId)o));
            }
        }
        return retList;
    }

    public void setSubjectIds(List<ObjectId> subjectIds) {
        setSimpleValue("suids", MongoUtils.convert(subjectIds));
    }

    public int getStatus() {
        return getSimpleIntegerValueDef("stus",1);
    }

    public void setStatus(int status) {
        setSimpleValue("stus",status);
    }
}
