package com.pojo.school;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yan on 2015/3/13.
 */
public class SchoolDTO {

    private String schoolId;
    private SchoolType schoolType;
    private String schoolName;
    private String englishName;
    private String introduction;
    private String detail;
    private String homePage;
    private String postCode;//邮编
    private String telephone;//电话号码
    private String initPwd;
    private String regionId;//地区id
    private String address;
    private List<Grade>  gradeList=new ArrayList<Grade>();
    private List<Subject> subjectList=new ArrayList<Subject>();
    private String schoolYear;
    private int schoolTypeInt;
    private int schoolTermType;
    private String logo;
    public SchoolDTO(){}

    public SchoolDTO(SchoolEntry schoolEntry){
        Map<Integer,SchoolType> map=new HashMap<Integer,SchoolType>();
        map.put(1,SchoolType.KINDERGARENER);
        map.put(2,SchoolType.PRIMARY);
        map.put(4,SchoolType.JUNIOR);
        map.put(8,SchoolType.SENIOR);
        map.put(16,SchoolType.UNIVERSITY);
        this.schoolId=schoolEntry.getID().toString();
        this.address=schoolEntry.getAddress();
        this.detail=schoolEntry.getDetail();
        this.englishName=schoolEntry.getEnglishName();
        this.gradeList=schoolEntry.getGradeList();
        this.homePage=schoolEntry.getDomain();
        this.initPwd=schoolEntry.getInitialPassword();
        this.introduction=schoolEntry.getIntroduce();
        this.postCode=schoolEntry.getPostCode();
        try
        {
          this.regionId=schoolEntry.getRegionId().toString();
          this.schoolType=map.get(schoolEntry.getSchoolType());
          this.schoolTypeInt=schoolEntry.getSchoolType();
          this.schoolTermType=schoolEntry.getTermType();
        }catch(Exception ex)
        {
        	
        }
        this.schoolName=schoolEntry.getName();
        this.telephone=schoolEntry.getTelephone();
        this.subjectList=schoolEntry.getSubjects();


    }
    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public SchoolType getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(SchoolType schoolType) {
        this.schoolType = schoolType;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getInitPwd() {
        return initPwd;
    }

    public void setInitPwd(String initPwd) {
        this.initPwd = initPwd;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public int getSchoolTypeInt() {
        return schoolTypeInt;
    }

    public void setSchoolTypeInt(int schoolTypeInt) {
        this.schoolTypeInt = schoolTypeInt;
    }

    public int getSchoolTermType() {
        return schoolTermType;
    }

    public void setSchoolTermType(int schoolTermType) {
        this.schoolTermType = schoolTermType;
    }


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
