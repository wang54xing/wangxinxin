package com.fulaan.new33.dto.isolate;

import org.bson.types.ObjectId;

import com.pojo.new33.isolate.N33_DefaultTermEntry;

public class N33_DefaultTermDTO {

	private String id;
	private String xqid;
	private String schoolId;
	private String paikexqId;
	private String paikeciId;
	
	public N33_DefaultTermDTO(){
		
	}
	
	public N33_DefaultTermDTO(String id,String xqid,String schoolId) {
		super();
		this.id = id;
		this.xqid = xqid;
		this.schoolId = schoolId;
	}

	public N33_DefaultTermDTO(N33_DefaultTermEntry entry) {
		super();
		this.id = entry.getID().toString();
		this.xqid = entry.getXqid().toString();
		this.schoolId = entry.getSid().toString();
		if(entry.getPaikeXqid()!=null){
			this.paikexqId = entry.getPaikeXqid().toString();
		}
		if(entry.getPaikeci()!=null){
			this.paikeciId = entry.getPaikeci().toString();
		}

	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getPaikexqId() {
		return paikexqId;
	}

	public void setPaikexqId(String paikexqId) {
		this.paikexqId = paikexqId;
	}

	public String getPaikeciId() {
		return paikeciId;
	}

	public void setPaikeciId(String paikeciId) {
		this.paikeciId = paikeciId;
	}

	public String getXqid() {
		return xqid;
	}

	public void setXqid(String xqid) {
		this.xqid = xqid;
	}

	public N33_DefaultTermEntry getEntry() {
		N33_DefaultTermEntry entry = new N33_DefaultTermEntry();
        if(paikexqId!=null){
			entry.setPaikeXqid(new ObjectId(paikexqId));
		}
		if(paikeciId!=null){
        	entry.setPaikeci(new ObjectId(paikeciId));
		}
		if(id!=null){
			entry.setID(new ObjectId(id));
		}
		if(schoolId!=null){
			entry.setSid(new ObjectId(schoolId));
		}
		if(xqid!=null){
			entry.setXqid(new ObjectId(xqid));
		}
		return entry;
    }
	
}
