package com.fulaan.schoolbase.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fulaan.utils.StringUtil;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.school.SchoolEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/12/29.
 */
public class SchoolDTO {
    private String id;
    private String name;
    private String introduce;
    private String message;
    private String url;
    private String schoolMotto;
    private String schoolLogo;
    private List<SchoolPeriod> periods = new ArrayList<SchoolPeriod>();
    private String province;
    private String city;
    private String county;
    private String detailedAddress;
    private String parentId;
    private String createrId;
    private String createDate;
    private int primary;
    private int middle;
    private int high;

    private List<IdNameValuePairDTO> idNameValuePairDTOs;

    public SchoolDTO(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getSchoolMotto() {
        return schoolMotto;
    }

    public void setSchoolMotto(String schoolMotto) {
        this.schoolMotto = schoolMotto;
    }

    public String getSchoolLogo() {
        return schoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<SchoolPeriod> getPeriods() {
        return periods;
    }

    public void setPeriods(List<SchoolPeriod> periods) {
        this.periods = periods;
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    public int getMiddle() {
        return middle;
    }

    public void setMiddle(int middle) {
        this.middle = middle;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<IdNameValuePairDTO> getIdNameValuePairDTOs() {
        return idNameValuePairDTOs;
    }

    public void setIdNameValuePairDTOs(List<IdNameValuePairDTO> idNameValuePairDTOs) {
        this.idNameValuePairDTOs = idNameValuePairDTOs;
    }
}
