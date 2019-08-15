package com.fulaan.new33.dto.isolate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import com.pojo.new33.isolate.TermEntry;

/**
 * Tid 学年id
 * xqnm:学期名
 * sid 学校id
 * st:开始时间
 * et:结束时间
 * ir:上学期还是下学期(0=上 1=下)
 */
public class TermDTO {

	private String tid;
    private String xqnm;
    private String sid;
    private Integer year;
    private Long st;
    private Long et;
    private String start;
    private String end;
    private Integer ir;
    private String id;
    private String sy;

    private List<PaiKeTimeDTO> timeDTOS = new ArrayList<PaiKeTimeDTO>();


    public TermDTO(String tid, String xqnm, String sid, Long st, Long et, Integer ir) {
        this.tid = tid;
        this.xqnm = xqnm;
        this.sid = sid;
        this.st = st;
        this.et = et;
        this.ir = ir;
    }

    public TermDTO(String tid,Integer year, String xqnm, String sid, Long st, Long et, Integer ir, String id, String sy) {
        this.tid = tid;
        this.xqnm = xqnm;
        this.sid = sid;
        this.year = year;
        this.st = st;
        this.et = et;
        this.ir = ir;
        this.id = id;
        this.sy = sy;
    }
    
    public TermDTO() {
    }
    
    public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getXqnm() {
        return xqnm;
    }

    public void setXqnm(String xqnm) {
        this.xqnm = xqnm;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Long getSt() {
        return st;
    }

    public void setSt(Long st) {
        this.st = st;
    }

    public Long getEt() {
        return et;
    }

    public void setEt(Long et) {
        this.et = et;
    }

    public Integer getIr() {
        return ir;
    }

    public void setIr(Integer ir) {
        this.ir = ir;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
    }

    public List<PaiKeTimeDTO> getTimeDTOS() {
        return timeDTOS;
    }

    public void setTimeDTOS(List<PaiKeTimeDTO> timeDTOS) {
        this.timeDTOS = timeDTOS;
    }

    public static class PaiKeTimeDTO{
        private String id;
        private Integer serialNumber;
        private String description;
        private List<String> gradeIds = new ArrayList<String>();
        private Integer ir;
        public PaiKeTimeDTO(){}
        public PaiKeTimeDTO(TermEntry.PaiKeTimes time){
            id = time.getID().toString();
            serialNumber = time.getSerialNumber();
            description = time.getDescription();
            gradeIds = MongoUtils.convertToStringList(time.getGradeIds());
            ir = time.getIr();
        }
        public TermEntry.PaiKeTimes buildEntry(){
            TermEntry.PaiKeTimes time = new TermEntry.PaiKeTimes(serialNumber,description,MongoUtils.convertToObjectIdList(gradeIds));
            if(id!=null){
                time.setID(new ObjectId(id));
            }
            time.setIr(ir);
            return time;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(Integer serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getGradeIds() {
            return gradeIds;
        }

        public void setGradeIds(List<String> gradeIds) {
            this.gradeIds = gradeIds;
        }

        public Integer getIr() {
            return ir;
        }

        public void setIr(Integer ir) {
            this.ir = ir;
        }
    }
    public TermDTO(TermEntry entry) {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        this.xqnm = entry.getXqName();
        this.sid = entry.getSid().toString();
        if(entry.getTid()!=null) {
        	this.tid = entry.getTid().toString();
        }
        this.year = entry.getYear();
        this.id = entry.getID().toString();
        this.st = entry.getSTime();
        this.et = entry.getETime();
        this.start = sf.format(new Date(this.st));
        this.end = sf.format(new Date(this.et));
        this.sy = entry.getSy();
        if(entry.getPaiKeTimes()!=null){
            for(TermEntry.PaiKeTimes time:entry.getPaiKeTimes()){
                this.timeDTOS.add(new PaiKeTimeDTO(time));
            }
        }
        this.ir = entry.getIr();
        
    }

    public TermEntry getEntry() {
        TermEntry entry = new TermEntry(new ObjectId(sid), xqnm,year, st, et, ir, sy);
        if(tid!=null) {
        	entry.setTid(new ObjectId(tid));
        }
        if (id==null||id.equals("*")) {
            entry.setID(new ObjectId());
        } else {
            entry.setID(new ObjectId(id));
        }
        List<TermEntry.PaiKeTimes> timesList = new ArrayList<TermEntry.PaiKeTimes>();
        if(timeDTOS!=null){
            for(PaiKeTimeDTO dto:timeDTOS){
                timesList.add(dto.buildEntry());
            }
        }
        entry.setPaiKeTimes(timesList);
        return entry;
    }
}
