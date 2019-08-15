package com.pojo.user;

/**
 * 申请用户信息
 * @author fourer
 *
 */
public class ApplicationUseInfo {

	 private String userName;
	    private String sex;
	    private String cellPhoneNumber;
	    private String email;
	    private String address;
	    private String schoolName;


	    private String grade;//年级
	    private String clazz;//班级
	    private String subject;//所带班级
	    private int stuNumber;//申请学生账号人数
	    private int teacherCount;//申请老师账号人数
	    private String knowFromWhere;//从何处了解k6kt


	    @Override
	    public String toString() {
	        return "ApplicationUseInfo{" +
	                "userName='" + userName + '\'' +
	                ", sex=" + sex +
	                ", cellPhoneNumber='" + cellPhoneNumber + '\'' +
	                ", email='" + email + '\'' +
	                ", address='" + address + '\'' +
	                ", schoolName='" + schoolName + '\'' +
	                ", grade='" + grade + '\'' +
	                ", clazz='" + clazz + '\'' +
	                ", subject='" + subject + '\'' +
	                ", stuNumber=" + stuNumber +
	                ", knowFromWhere='" + knowFromWhere + '\'' +
	                '}';
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        ApplicationUseInfo that = (ApplicationUseInfo) o;

	        if (sex != that.sex) return false;
	        if (cellPhoneNumber != null ? !cellPhoneNumber.equals(that.cellPhoneNumber) : that.cellPhoneNumber != null)
	            return false;
	        if (email != null ? !email.equals(that.email) : that.email != null) return false;
	        if (schoolName != null ? !schoolName.equals(that.schoolName) : that.schoolName != null) return false;
	        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

	        return true;
	    }



	    public String getUserName() {
	        return userName;
	    }

	    public void setUserName(String userName) {
	        this.userName = userName;
	    }

	    public String getSex() {
	        return sex;
	    }

	    public void setSex(String sex) {
	        this.sex = sex;
	    }

	    public String getCellPhoneNumber() {
	        return cellPhoneNumber;
	    }

	    public void setCellPhoneNumber(String cellPhoneNumber) {
	        this.cellPhoneNumber = cellPhoneNumber;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }

	    public String getSchoolName() {
	        return schoolName;
	    }

	    public void setSchoolName(String schoolName) {
	        this.schoolName = schoolName;
	    }

	    public String getGrade() {
	        return grade;
	    }

	    public void setGrade(String grade) {
	        this.grade = grade;
	    }

	    public String getClazz() {
	        return clazz;
	    }

	    public void setClazz(String clazz) {
	        this.clazz = clazz;
	    }

	    public String getSubject() {
	        return subject;
	    }

	    public void setSubject(String subject) {
	        this.subject = subject;
	    }

	    public int getStuNumber() {
	        return stuNumber;
	    }

	    public void setStuNumber(int stuNumber) {
	        this.stuNumber = stuNumber;
	    }

	    public String getKnowFromWhere() {
	        return knowFromWhere;
	    }

	    public void setKnowFromWhere(String knowFromWhere) {
	        this.knowFromWhere = knowFromWhere;
	    }

	    public int getTeacherCount() {
	        return teacherCount;
	    }

	    public void setTeacherCount(int teacherCount) {
	        this.teacherCount = teacherCount;
	    }
}
