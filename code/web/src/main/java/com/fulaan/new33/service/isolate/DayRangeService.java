package com.fulaan.new33.service.isolate;

import java.text.ParseException;

import org.bson.types.ObjectId;

import com.db.new33.DayRangeDao;
import com.fulaan.new33.dto.isolate.DayRangeDTO;
import com.pojo.new33.DayRangeEntry;

public class DayRangeService {
	
	private DayRangeDao dao = new DayRangeDao();
	
	public DayRangeDTO getBySchoolId(String schoolId) {
		DayRangeEntry entry = dao.getEntryBySchoolId(new ObjectId(schoolId));
		if(null!=entry) {
			return new DayRangeDTO(entry);
		}
		return null;
	}
	
	public void save(DayRangeDTO dto) throws ParseException {
		dao.addEntry(dto.buildEntry());
	}
	

}
