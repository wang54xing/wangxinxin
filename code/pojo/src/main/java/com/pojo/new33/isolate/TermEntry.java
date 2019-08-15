package com.pojo.new33.isolate;

import com.mongodb.BasicDBList;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rick
 * termEntry 3+3隔离层（学期）
 * Tid 学年id
 * xqnm:学期名
 * sid 学校id
 * st:开始时间
 * et:结束时间
 * ir:上学期还是下学期(0=上 1=下)
 */
public class TermEntry extends BaseDBObject {
	private static final long serialVersionUID = -5327902228794346271L;
	
	public TermEntry(BasicDBObject baseEntry) {
        setBaseEntry(baseEntry);
    }

    public TermEntry() {
    }

    public TermEntry(ObjectId schoolId, String xqnm, ObjectId tid,Integer year, Long sTime, Long eTime, Integer ir, String sy) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("tid", tid)
                .append("year", year)
                .append("xqnm",xqnm)
                .append("st", sTime)
                .append("et", eTime)
                .append("ir", ir)
                .append("sy",sy);
        setBaseEntry(dbObject);
    }


    public TermEntry(ObjectId schoolId, String xqnm,Integer year, Long sTime, Long eTime, Integer ir, String sy,List<PaiKeTimes> times) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("year", year)
                .append("xqnm",xqnm)
                .append("st", sTime)
                .append("et", eTime)
                .append("ir", ir)
                .append("sy",sy)
                .append("times", MongoUtils.convert(MongoUtils.fetchDBObjectList(times)));
        setBaseEntry(dbObject);
    }
    public TermEntry(ObjectId schoolId, String xqnm,Integer year, Long sTime, Long eTime, Integer ir, String sy) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("year", year)
                .append("xqnm",xqnm)
                .append("st", sTime)
                .append("et", eTime)
                .append("ir", ir)
                .append("sy",sy);
        setBaseEntry(dbObject);
    }
    public ObjectId getSid() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSid(ObjectId sid) {
        setSimpleValue("sid", sid);
    }


    public String getXqName() {
        return getSimpleStringValue("xqnm");
    }

    public void setXqName(String xqnm) {
        setSimpleValue("xqnm", xqnm);
    }


    public void setYear(Integer year) {
    	setSimpleValue("year", year);
    }
    public Integer getYear() {
    	return getSimpleIntegerValue("year");
    }
    public ObjectId getTid() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTid(ObjectId tid) {
        setSimpleValue("tid", tid);
    }


    public ObjectId getTimeId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTimeId(ObjectId tid) {
        setSimpleValue("tid", tid);
    }


    public int getIr() {
        return getSimpleIntegerValue("ir");
    }

    public void setIr(int ir) {
        setSimpleValue("ir", ir);
    }


    public Long getSTime() {
        return getSimpleLongValue("st");
    }

    public void setSTime(Long sTime) {
        setSimpleValue("st", sTime);
    }


    public Long getStartTime() {
        return getSimpleLongValue("st");
    }

    public void setStartTime(Long sTime) {
        setSimpleValue("st", sTime);
    }


    public Long getETime() {
        return getSimpleLongValue("et");
    }

    public void setETime(Long eTime) {
        setSimpleValue("et", eTime);
    }



    public Long getEndTime() {
        return getSimpleLongValue("et");
    }

    public void setEndTime(Long eTime) {
        setSimpleValue("et", eTime);
    }

    public String getSy(){
	    return getSimpleStringValue("sy");
    }
    public void setSy(String sy){
        setSimpleValue("sy",sy);
    }
    public List<PaiKeTimes> getPaiKeTimes(){
        List<PaiKeTimes> result = new ArrayList<PaiKeTimes>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("times");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                result.add(new PaiKeTimes((BasicDBObject)o));
            }
        }
        return result;
    }
    public void setPaiKeTimes(List<PaiKeTimes> times){
        setSimpleValue("times", MongoUtils.convert(MongoUtils.fetchDBObjectList(times)));
    }
    public static class PaiKeTimes extends BaseDBObject{
        public PaiKeTimes(){}
        public PaiKeTimes(Integer serialNumber, String description,List<ObjectId> gradeIds) {
            super();
            BasicDBObject dbObject = new BasicDBObject()
                    .append("serial", serialNumber)
                    .append("desc",description)
                    .append("gradeids",MongoUtils.convert(gradeIds));
            setBaseEntry(dbObject);
        }
        public PaiKeTimes(Integer serialNumber, String description,List<ObjectId> gradeIds,Integer ir) {
            super();
            BasicDBObject dbObject = new BasicDBObject()
                    .append("serial", serialNumber)
                    .append("desc",description)
                    .append("gradeids",MongoUtils.convert(gradeIds))
                    .append("ir",ir);
            setBaseEntry(dbObject);
        }

        public PaiKeTimes(BasicDBObject baseEntry) {
            super(baseEntry);
        }

        public Integer getSerialNumber() {
            return getSimpleIntegerValue("serial");
        }
        public void setSerialNumber(Integer serialNumber){
            setSimpleValue("serial", serialNumber);
        }

        public String getDescription() {
            return getSimpleStringValue("desc");
        }

        public void setDescription(String description) {
            setSimpleValue("desc",description);
        }
        public Integer getIr() {
            return getSimpleIntegerValueDef("ir",0);
        }
        public void setIr(Integer ir){
            setSimpleValue("ir", ir);
        }
        public List<ObjectId> getGradeIds(){
            List<ObjectId> result = new ArrayList<ObjectId>();
            BasicDBList list = (BasicDBList) getSimpleObjectValue("gradeids");
            if (null != list && !list.isEmpty()) {
                for (Object o : list) {
                    result.add((ObjectId) o);
                }
            }
            return result;
        }

        public void setGradeIds(List<ObjectId> gradeIds){
            setSimpleValue("gradeids",MongoUtils.convert(gradeIds));
        }
    }
}
