package com.fulaan.new33.service.isolate;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.new33.isolate.ClassDao;
import com.fulaan.new33.dto.isolate.ClassInfoDTO;
import com.pojo.new33.isolate.ClassEntry;

public class N33_ClassService {

	private ClassDao classDao = new ClassDao();
	
	public List<ClassInfoDTO> findByGradeIdSid(ObjectId schoolId,ObjectId xqid,ObjectId gradeId){
		List<ClassInfoDTO> result = new ArrayList<ClassInfoDTO>();
		List<ClassEntry> entrylist = classDao.findByGradeIdId(schoolId, gradeId, xqid);
		for(ClassEntry entry:entrylist) {
			result.add(new ClassInfoDTO(entry));
		}
		return result;
	}
}
