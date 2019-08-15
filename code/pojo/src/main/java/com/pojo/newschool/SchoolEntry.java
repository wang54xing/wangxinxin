package com.pojo.newschool;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学校信息
 * <pre>
 * collectionName:newschool
 * </pre>
 * <pre>
 * {
 *  nm:学校名称 name
 *  int:基本介绍 introduce
 *  url:官网 url
 *  smo;校训 schoolMotto
 *  slo:学校logo schoolLogo
 *  pes:{
 *       primary: year 小学学段:学制
 *      junior: year 初中学段:学制
 *      senior:year 高中学段:学制
 *      ...
 *  } 学段 period
 *  pr:省份 province
 *  ci:城市 city
 *  co:区县 county
 *  ad:入学时间 admissionDate
 *  pid:父id parentId
 *  crid:创建人id createrId
 *  crt:创建时间 createDate
 *  ir:是否删除 0没有删除 1已经删除
 * }
 * </pre>
 * Created by guojing on 2016/12/29.
 */
public class SchoolEntry extends BaseDBObject {

    private static final long serialVersionUID = 6485398291488814841L;

    public SchoolEntry() {

    }

    public SchoolEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public SchoolEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public SchoolEntry(
            String name,
            String url,
            String introduce,
            String schoolMotto,
            String schoolLogo,
            List<Period> periodList,
            ObjectId province,
            ObjectId city,
            ObjectId county,
            String detailedAddress,
            ObjectId parentId,
            ObjectId createrId
    ) {
        super();
        List<DBObject> periods = MongoUtils.fetchDBObjectList(periodList);
        BasicDBObject dbo =new BasicDBObject();
        dbo.append("nm", name);
        dbo.append("url",url);
        dbo.append("int", introduce);
        dbo.append("smo", schoolMotto);
        dbo.append("slo", schoolLogo);
        dbo.append("pes", MongoUtils.convert(periods));
        dbo.append("pr", province);
        dbo.append("ci", city);
        dbo.append("co", county);
        dbo.append("dad", detailedAddress);
        dbo.append("pid", parentId);
        dbo.append("crid", createrId);
        dbo.append("crt", new Date().getTime());
        dbo.append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }

    public String getUrl() {
        return getSimpleStringValueDef("url","");
    }

    public void setUrl(String url) {
        setSimpleValue("url",url);
    }

    public String getName() {
        return getSimpleStringValueDef("nm","");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getIntroduce() {
        return getSimpleStringValueDef("int","");
    }

    public void setIntroduce(String introduce) {
        setSimpleValue("int", introduce);
    }

    public String getSchoolMotto() {
        return getSimpleStringValueDef("smo","");
    }

    public void setSchoolMotto(String schoolMotto) {
        setSimpleValue("smo", schoolMotto);
    }

    public String getSchoolLogo() {
        return getSimpleStringValueDef("slo","");
    }

    public void setSchoolLogo(String schoolLogo) {
        setSimpleValue("slo", schoolLogo);
    }

    public List<Period> getPeriods() {
        List<Period> retList =new ArrayList<Period>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("pes");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new Period((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setPeriods(List<Period> periodList) {
        List<DBObject> periods = MongoUtils.fetchDBObjectList(periodList);
        setSimpleValue("pes", MongoUtils.convert(periods));
    }

    public ObjectId getProvince() {
        return getSimpleObjecIDValue("pr");
    }

    public void setProvince(ObjectId province) {
        setSimpleValue("pr", province);
    }

    public ObjectId getCity() {
        return getSimpleObjecIDValue("ci");
    }

    public void setCity(ObjectId city) {
        setSimpleValue("ci", city);
    }

    public ObjectId getCounty() {
        return getSimpleObjecIDValue("co");
    }

    public void setCounty(ObjectId county) {
        setSimpleValue("co", county);
    }

    public String getDetailedAddress() {
        return getSimpleStringValueDef("dad","");
    }

    public void setDetailedAddress(String detailedAddress) {
        setSimpleValue("dad", detailedAddress);
    }

    public ObjectId getParentId() {
        return getSimpleObjecIDValue("pid");
    }

    public void setParentId(ObjectId parentId) {
        setSimpleValue("pid", parentId);
    }

    public ObjectId getCreaterId() {
        return getSimpleObjecIDValue("crid");
    }

    public void setCreaterId(ObjectId createrId) {
        setSimpleValue("crid",createrId);
    }

    public long getCreateDate() {
        return getSimpleLongValueDef("crt",0l);
    }

    public void setCreateDate(long createDate) {
        setSimpleValue("crt", createDate);
    }
    //默认没有删除
    public int getIsRemove() {
        return getSimpleIntegerValueDef("ir", Constant.ZERO);
    }

    public void setIsRemove(int isRemove) {
        setSimpleValue("ir", isRemove);
    }
}
