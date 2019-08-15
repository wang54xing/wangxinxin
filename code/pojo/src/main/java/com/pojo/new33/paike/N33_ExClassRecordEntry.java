package com.pojo.new33.paike;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albin on 2018/6/29.
 */
public class N33_ExClassRecordEntry extends BaseDBObject {

    private static final long serialVersionUID = -4744310955303903715L;

    public N33_ExClassRecordEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public N33_ExClassRecordEntry() {
    }

    public N33_ExClassRecordEntry(ObjectId classRoomId,Long time,ObjectId sid, ObjectId xqid, Integer x,Integer y,Integer ox,Integer oy,ObjectId ykbId,ObjectId oYkbId, ObjectId gid) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("xqid", xqid) //次id
                .append("sid", sid)//学校id
                .append("gid",gid) //年级ID
                .append("time",time)    //记录时间
                .append("classRoomId",classRoomId) //调课教室
                .append("x",x)     //换课时鼠标第一次点击的源课表的x坐标
                .append("y",y)      //换课时鼠标第一次点击的源课表的y坐标
                .append("ox",ox)       //换课时鼠标第二次点击的源课表的x坐标
                .append("oy",oy)    //换课时鼠标第二次点击的源课表的y坐标
                .append("ykbId",ykbId)  //换课时鼠标第一次点击的源课表的Id
                .append("oYkbId",oYkbId); //换课时鼠标第二次点击的源课表的Id
        setBaseEntry(dbo);
    }

    public ObjectId getGradeId(){
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gid) {
        setSimpleValue("gid", gid);
    }

    public ObjectId getXqId() {
        return getSimpleObjecIDValue("xqid");
    }

    public void setXqId(ObjectId xqid) {
        setSimpleValue("xqid", xqid);
    }

    public ObjectId getSId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSId(ObjectId sid) {
        setSimpleValue("sid", sid);
    }

    public ObjectId getClassRoomId() {
        return getSimpleObjecIDValue("classRoomId");
    }

    public void setClassRoomId(ObjectId classRoomId) {
        setSimpleValue("classRoomId", classRoomId);
    }

    public Integer getX() {
        return getSimpleIntegerValue("x");
    }

    public void setX(Integer x) {
        setSimpleValue("x", x);
    }

    public Integer getY() {
        return getSimpleIntegerValue("y");
    }

    public void setY(Integer y) {
        setSimpleValue("y", y);
    }

    public Integer getoX() {
        return getSimpleIntegerValue("ox");
    }

    public void setoX(Integer ox) {
        setSimpleValue("ox", ox);
    }

    public Integer getoY() {
        return getSimpleIntegerValue("oy");
    }

    public void setoY(Integer oy) {
        setSimpleValue("oy", oy);
    }

    public ObjectId getYkbId() {
        return getSimpleObjecIDValue("ykbId");
    }

    public void setYkbId(ObjectId ykbId) {
        setSimpleValue("ykbId", ykbId);
    }

    public ObjectId getoYkbId() {
        return getSimpleObjecIDValue("oYkbId");
    }

    public void setoYkbId(ObjectId oYkbId) {
        setSimpleValue("oYkbId", oYkbId);
    }

    public long getTime() {
        return getSimpleLongValue("time");
    }

    public void setTime(ObjectId time) {
        setSimpleValue("time", time);
    }

    public String getJXBANDTeaName(){
        return getSimpleStringValue("jxbAndTeaName");
    }

    public void setJXBANDTeaName(String jxbAndTeaName){
        setSimpleValue("jxbAndTeaName",jxbAndTeaName);
    }

    public String getoJXBANDTeaName(){
        return getSimpleStringValue("oJxbAndTeaName");
    }

    public void setoJXBANDTeaName(String oJxbAndTeaName){
        setSimpleValue("oJxbAndTeaName",oJxbAndTeaName);
    }
}
