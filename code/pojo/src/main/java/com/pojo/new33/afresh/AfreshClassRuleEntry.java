package com.pojo.new33.afresh;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2019-05-13.
 *
 *  重分行政班（规则表）---保存每次重分的设置属性
 *
 *  setId      pid           所属重分班
 *  order      od            规则序号    1
 *  number     fn            班级数量
 *  volume     fv            班级容量
 *  swim       fs            班级浮动
 *  compose    cp            组合 （学科组成）  默认为3个学科组合  （字符串：物化生）
 *
 */
public class AfreshClassRuleEntry extends BaseDBObject {

    public AfreshClassRuleEntry(){

    }

    public AfreshClassRuleEntry(DBObject dbObject){
        super((BasicDBObject) dbObject);
    }

    public AfreshClassRuleEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public AfreshClassRuleEntry(
            ObjectId setId,
            String order,
            String number,
            String volume,
            String swim,
            String compose
    ){

        BasicDBObject dbObject = new BasicDBObject()
                .append("sid",setId)
                .append("od", order)
                .append("fn", number)
                .append("fv", volume)
                .append("fs", swim)
                .append("cp", compose)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);

    }

    public ObjectId getSetId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSetId(ObjectId setId){
        setSimpleValue("sid",setId);
    }

    public String getOrder(){
        return getSimpleStringValue("od");
    }

    public  void setOrder(String order){
        setSimpleValue("od",order);
    }
    public String getNumber(){
        return getSimpleStringValue("fn");
    }

    public  void setNumber(String number){
        setSimpleValue("fn",number);
    }
    public String getVolume(){
        return getSimpleStringValue("fv");
    }

    public  void setVolume(String volume){
        setSimpleValue("fv",volume);
    }
    public String getSwim(){
        return getSimpleStringValue("fs");
    }

    public  void setSwim(String swim){
        setSimpleValue("fs",swim);
    }
    public String getCompose(){
        return getSimpleStringValue("cp");
    }

    public  void setCompose(String compose){
        setSimpleValue("cp",compose);
    }



}
