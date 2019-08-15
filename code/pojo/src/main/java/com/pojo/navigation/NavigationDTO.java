package com.pojo.navigation;

import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.NameValuePairDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2017/1/10.
 */
public class NavigationDTO implements Serializable {
    private static final long serialVersionUID = -6450286636417115416L;
    private String navId;
    private String acId;
    private int type;
    private String name;
    private String cname;
    private String appClass;
    private String roleNames;
    private String acIds;
    private int isShow;
    private int sort;
    private String parentId;
    private String link;
    private String image;
    private List<NavigationDTO> navigationDTOs;
    private List<NameValueDTO> appClassList;
    private List<NameValueDTO> roleList;
    private String schoolId;
    private String userId;
    private double price;
    private String version;
    private List<String> acIdList;
    private String fileId;
    private String packageName;
    private String versionCode;
    private String versionName;
    private String path;
    private String size;
    private String time;
    private String fileDesc;
    public NavigationDTO() {

    }

    public NavigationDTO (NavigationEntry e)
    {
        this.type=e.getType();
        this.name=e.getName();
        this.sort=e.getSort();
        this.parentId=e.getParentId()!=null ?e.getParentId().toString():"";
        this.image=e.getImage();
        if(e.getList().size()==1)
        {
            this.link=e.getList().get(0).getLink();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<NavigationDTO> getNavigationDTOs() {
        return navigationDTOs;
    }

    public void setNavigationDTOs(List<NavigationDTO> navigationDTOs) {
        this.navigationDTOs = navigationDTOs;
    }

    public List<NameValueDTO> getAppClassList() {
        return appClassList;
    }

    public void setAppClassList(List<NameValueDTO> appClassList) {
        this.appClassList = appClassList;
    }

    public List<NameValueDTO> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<NameValueDTO> roleList) {
        this.roleList = roleList;
    }

    public String getNavId() {
        return navId;
    }

    public void setNavId(String navId) {
        this.navId = navId;
    }

    public String getAcId() {
        return acId;
    }

    public void setAcId(String acId) {
        this.acId = acId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getAppClass() {
        return appClass;
    }

    public void setAppClass(String appClass) {
        this.appClass = appClass;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAcIds() {
        return acIds;
    }

    public void setAcIds(String acIds) {
        this.acIds = acIds;
    }

    public List<String> getAcIdList() {
        return acIdList;
    }

    public void setAcIdList(List<String> acIdList) {
        this.acIdList = acIdList;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NavigationDTO)) return false;

        NavigationDTO that = (NavigationDTO) o;

        if (sort != that.sort) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return sort;
    }

    @Override
    public String toString() {
        return "NavigationDTO{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", parentId='" + parentId + '\'' +
                ", link='" + link + '\'' +
                ", image='" + image + '\'' +
                ", navigationDTOs=" + navigationDTOs +
                '}';
    }
}
