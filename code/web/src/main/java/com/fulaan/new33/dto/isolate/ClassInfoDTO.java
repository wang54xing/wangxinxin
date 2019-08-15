package com.fulaan.new33.dto.isolate;

import com.pojo.new33.isolate.ClassEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

public class ClassInfoDTO {

		private String id;
	    private String schoolId;
	    private String classId;
	    private String className;
	    private String xqid;
	    private String buid;
	    private String bunm;
	    private List<String> stus;
	    private String gid;
	    private String bz;
	    private Integer type;
	    private Integer xh;
	    
	   
	    private Integer stuCount;
	    private String gname;
	    public String getGname() {
			return gname;
		}

		public void setGname(String gname) {
			this.gname = gname;
		}
		
		public Integer getStuCount() {
			return stuCount;
		}

		public void setStuCount(Integer stuCount) {
			this.stuCount = stuCount;
		}

		public ClassInfoDTO() {
	    
	    }

	    public ClassInfoDTO(String id, String schoolId, String classId,
				String className, String xqid, String buid, String bunm,
				List<String> stus, String gid, String bz, Integer type,
				Integer xh) {
			super();
			this.id = id;
			this.schoolId = schoolId;
			this.classId = classId;
			this.className = className;
			this.xqid = xqid;
			this.buid = buid;
			this.bunm = bunm;
			this.stus = stus;
			this.gid = gid;
			this.bz = bz;
			this.type = type;
			this.xh = xh;
		}



		public ClassInfoDTO(ClassEntry entry) {
	        this.id = entry.getID().toString();
	        this.schoolId = entry.getSchoolId().toString();
	        this.className = entry.getClassName();
	        this.xqid = entry.getXQId().toHexString();
	        this.buid = entry.getMentorId() == null ? null : entry.getMentorId().toHexString();
	        this.bunm = entry.getMentorName();
	        this.stus = MongoUtils.convertToStringList(entry.getStudentList());
	        this.type = entry.getType();
	        this.classId =entry.getClassId().toHexString();
	        this.gid=entry.getGradeId().toHexString();
	        this.xh = entry.getXh();
	        this.bz = entry.getBeiZhu();
	    }

	
		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public ClassEntry getEntry() {
	        ClassEntry entry = new ClassEntry(new ObjectId(xqid), new ObjectId(schoolId), className, MongoUtils.convertToObjectIdList(stus), bunm, new ObjectId(classId), type, new ObjectId(gid), bz,xh);
	        if (buid!=null && buid.equals("*")) {
	            entry.setBuId(new ObjectId());
	        } else {
				if(buid==null){
					entry.setBuId(new ObjectId());
				}else{
					entry.setBuId(new ObjectId(buid));
				}
	        }
	        if (id.equals("*")) {
	            entry.setID(new ObjectId());
	        } else {
	            entry.setID(new ObjectId(id));
	        }
	        return entry;
	    }


	    public String getGid() {
	        return gid;
	    }

	    public void setGid(String gid) {
	        this.gid = gid;
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

	    public String getClassId() {
	        return classId;
	    }

	    public void setClassId(String classId) {
	        this.classId = classId;
	    }

	    public String getClassName() {
	        return className;
	    }

	    public void setClassName(String className) {
	        this.className = className;
	    }

	    public String getXqid() {
	        return xqid;
	    }

	    public void setXqid(String xqid) {
	        this.xqid = xqid;
	    }

	    public String getBuid() {
	        return buid;
	    }

	    public void setBuid(String buid) {
	        this.buid = buid;
	    }

	    public String getBunm() {
	        return bunm;
	    }

	    public void setBunm(String bunm) {
	        this.bunm = bunm;
	    }

	    public List<String> getStus() {
	        return stus;
	    }

	    public void setStus(List<String> stus) {
	        this.stus = stus;
	    }

		public String getBz() {
			return bz;
		}

		public void setBz(String bz) {
			this.bz = bz;
		}
	    
		public Integer getXh() {
			return xh;
		}

		public void setXh(Integer xh) {
			this.xh = xh;
		}
}
