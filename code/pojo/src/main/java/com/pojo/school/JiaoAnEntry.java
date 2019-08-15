package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 教案
 * <pre>
 * collectionName:jiaoans
 * </pre>
 * <pre>
 * {
 * cid:
 * ui:用户
 * si:学校
 * nm:
 * mb:教学目标
 * nd:重点难点
 * zb:教学准备
 * docs：教案设计
 * [
 * {
 *    id:
 *    nm:
 *    v:
 *   }
 * ]
 * bs:板书设计
 * ktlx:课堂练习
 * zy:作业布置
 * fs:教学反思
 * ishw:是否推送作业 0没有
 * isxb:是否推送校本 0没有
 * islm:是否推送联盟资源 0没有
 * isjt:是否推送到集体教案 0没有
 * }
 * </pre>
 *
 * @author fourer
 */
public class JiaoAnEntry extends BaseDBObject {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JiaoAnEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public JiaoAnEntry(ObjectId connectId, ObjectId userId, ObjectId school, String name, String mubiao,
                       String nandian, String zhunbei, String sheji, String ktlx,
                       String zuoye, String fansi, List<IdNameValuePair> dosList) {
        super();

        List<DBObject> list = MongoUtils.fetchDBObjectList(dosList);
        BasicDBObject baseEntry = new BasicDBObject()
                .append("docs", MongoUtils.convert(list))
                .append("cid", connectId)
                .append("ui", userId)
                .append("si", school)
                .append("nm", name)
                .append("mb", mubiao)
                .append("nd", nandian)
                .append("zb", zhunbei)
                .append("bs", sheji)
                .append("ktlx", ktlx)
                .append("zy", zuoye)
                .append("fs", fansi)
                .append("ishw", 0)
                .append("isxb", 0)
                .append("islm", 0)
                .append("is", 0)
                .append("isjt",0);

        setBaseEntry(baseEntry);
    }

    public int getIsHowework() {
        return getSimpleIntegerValueDef("ishw", 0);
    }

    public void setIsHowework(int isHowework) {
        setSimpleValue("ishw", isHowework);
    }

    public int getIsXiaoben() {
        return getSimpleIntegerValueDef("isxb", 0);
    }

    public void setIsXiaoben(int isXiaoben) {
        setSimpleValue("isxb", isXiaoben);
    }

    public int getIsLianmeng() {
        return getSimpleIntegerValueDef("islm", 0);
    }

    public void setIsLianmeng(int isLianmeng) {
        setSimpleValue("islm", isLianmeng);
    }


    public int getIsJiti() {
        return getSimpleIntegerValueDef("isjt", 0);
    }

    public void setIsJiti(int isjt) {
        setSimpleValue("isjt", isjt);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public ObjectId getConnectId() {
        return getSimpleObjecIDValue("cid");
    }

    public void setConnectId(ObjectId connectId) {
        setSimpleValue("cid", connectId);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getMubiao() {
        return getSimpleStringValue("mb");
    }

    public void setMubiao(String mubiao) {
        setSimpleValue("mb", mubiao);
    }

    public String getNandian() {
        return getSimpleStringValue("nd");
    }

    public void setNandian(String nandian) {
        setSimpleValue("nd", nandian);
    }

    public String getZhunbei() {
        return getSimpleStringValue("zb");
    }

    public void setZhunbei(String zhunbei) {
        setSimpleValue("zb", zhunbei);
    }

    public String getSheji() {
        return getSimpleStringValue("bs");
    }

    public void setSheji(String sheji) {
        setSimpleValue("bs", sheji);
    }

    public String getKtlx() {
        return getSimpleStringValue("ktlx");
    }

    public void setKtlx(String ktlx) {
        setSimpleValue("ktlx", ktlx);
    }

    public String getZuoye() {
        return getSimpleStringValue("zy");
    }

    public void setZuoye(String zuoye) {
        setSimpleValue("zy", zuoye);
    }

    public String getFansi() {
        return getSimpleStringValue("fs");
    }

    public void setFansi(String fansi) {
        setSimpleValue("fs", fansi);
    }

    public List<IdNameValuePair> getDosList() {
        List<IdNameValuePair> retList = new ArrayList<IdNameValuePair>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("docs");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(new IdNameValuePair((BasicDBObject) o));
            }
        }
        return retList;
    }

    public void setDosList(List<IdNameValuePair> dosList) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(dosList);
        setSimpleValue("docs", MongoUtils.convert(list));
    }

}
