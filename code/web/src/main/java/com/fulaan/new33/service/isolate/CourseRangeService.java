package com.fulaan.new33.service.isolate;

import java.text.ParseException;
import java.util.*;

import com.db.new33.isolate.ZouBanTimeDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZKBDao;
import com.fulaan.new33.dto.isolate.ZouBanTimeDTO;
import com.pojo.new33.isolate.ZouBanTimeEntry;
import com.pojo.new33.paike.N33_ZKBEntry;
import org.bson.types.ObjectId;

import com.db.new33.CourseRangeDao;
import com.db.new33.DayRangeDao;
import com.db.new33.isolate.TermDao;
import com.fulaan.new33.dto.isolate.CourseRangeDTO;
import com.pojo.new33.CourseRangeEntry;
import com.pojo.new33.DayRangeEntry;
import com.pojo.new33.isolate.TermEntry;
import org.springframework.stereotype.Service;

@Service
public class CourseRangeService {

	private CourseRangeDao dao= new CourseRangeDao();
	private DayRangeDao dayRangeDao = new DayRangeDao();
	private TermDao termDao = new TermDao();
	private ZouBanTimeDao timeDao = new ZouBanTimeDao();

	private N33_YKBDao ykbDao = new N33_YKBDao();
	private N33_ZKBDao zkbDao = new N33_ZKBDao();

	public List<CourseRangeDTO> getListBySchoolId(String schoolId,ObjectId xqid){
		List<CourseRangeDTO> result = new ArrayList<CourseRangeDTO>();
		List<CourseRangeEntry> list = dao.getEntryListBySchoolId(new ObjectId(schoolId), xqid);
		DayRangeEntry dayRangeEntry = dayRangeDao.getEntryBySchoolId(new ObjectId(schoolId));
		for(CourseRangeEntry entry:list) {
			CourseRangeDTO dto = new CourseRangeDTO(entry);
			dto.setRange("");
			if(dayRangeEntry!=null) {
				if(entry.getStart()>=dayRangeEntry.getStart1()&&entry.getEnd()<=dayRangeEntry.getEnd1()) {
					dto.setRange("上午");
				}
				else if(entry.getStart()>=dayRangeEntry.getStart2()&&entry.getEnd()<=dayRangeEntry.getEnd2()){
					dto.setRange("下午");
				}
				else if(entry.getStart()>=dayRangeEntry.getStart3()&&entry.getEnd()<=dayRangeEntry.getEnd3()){
					dto.setRange("晚上");
				}
			}
			result.add(dto);
		}
		return result;
	}

	public List<CourseRangeDTO> getListBySchoolIdForKB(String schoolId,ObjectId xqid,Integer week,ObjectId gradeId){
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds(xqid, new ObjectId(schoolId), week, gradeId);
		ObjectId ciId = null;
		if(ykbEntryList1.size() > 0){
			ciId = ykbEntryList1.get(0).getCId();
		}else{
			List<CourseRangeDTO> result = new ArrayList<CourseRangeDTO>();
			return result;
		}
		List<CourseRangeDTO> result = new ArrayList<CourseRangeDTO>();
		List<CourseRangeEntry> list = dao.getEntryListBySchoolId(new ObjectId(schoolId), ciId);
		DayRangeEntry dayRangeEntry = dayRangeDao.getEntryBySchoolId(new ObjectId(schoolId));
		for(CourseRangeEntry entry:list) {
			CourseRangeDTO dto = new CourseRangeDTO(entry);
			dto.setRange("");
			if(dayRangeEntry!=null) {
				if(entry.getStart()>=dayRangeEntry.getStart1()&&entry.getEnd()<=dayRangeEntry.getEnd1()) {
					dto.setRange("上午");
				}
				else if(entry.getStart()>=dayRangeEntry.getStart2()&&entry.getEnd()<=dayRangeEntry.getEnd2()){
					dto.setRange("下午");
				}
				else if(entry.getStart()>=dayRangeEntry.getStart3()&&entry.getEnd()<=dayRangeEntry.getEnd3()){
					dto.setRange("晚上");
				}
			}
			result.add(dto);
		}
		return result;
	}

	/**
	 * 查询学期中次的课节最多的记录
	 * @param schoolId
	 * @param xqid
	 * @return
	 */
	public List<CourseRangeDTO> getListBySchoolIdZKB(String schoolId,ObjectId xqid){
		List<CourseRangeDTO> result = new ArrayList<CourseRangeDTO>();
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cid = new ArrayList<ObjectId>();
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if(paiKeTimes.getIr()==0) {
				cid.add(paiKeTimes.getID());
			}
		}
		List<CourseRangeEntry> list = dao.getEntryListBySchoolId(new ObjectId(schoolId), cid);
		Map<ObjectId,List<CourseRangeDTO>> map = new HashMap<ObjectId, List<CourseRangeDTO>>();
		for (CourseRangeEntry entry:list){
			if(map.get(entry.getXqId()) != null){
				List<CourseRangeDTO> entryList = map.get(entry.getXqId());
				entryList.add(new CourseRangeDTO(entry));
			}else{
				List<CourseRangeDTO> addList = new ArrayList<CourseRangeDTO>();
				addList.add(new CourseRangeDTO(entry));
				map.put(entry.getXqId(),addList);
			}
		}
		int count = 0;
		Set<ObjectId> keySet = map.keySet();
		Iterator<ObjectId> it = keySet.iterator();
		while(it.hasNext()){
			ObjectId id = it.next();
			if (map.get(id).size() > count) {
				count = map.get(id).size();
				result = map.get(id);
			}
		}
		return result;
	}

	public List<CourseRangeDTO> getListByCiId(String schoolId,ObjectId xqid){
		List<CourseRangeDTO> result = new ArrayList<CourseRangeDTO>();
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cid = new ArrayList<ObjectId>();
		cid.add(xqid);
		List<CourseRangeEntry> list = dao.getEntryListBySchoolId(new ObjectId(schoolId), cid);
		Map<ObjectId,List<CourseRangeDTO>> map = new HashMap<ObjectId, List<CourseRangeDTO>>();
		for (CourseRangeEntry entry:list){
			if(map.get(entry.getXqId()) != null){
				List<CourseRangeDTO> entryList = map.get(entry.getXqId());
				entryList.add(new CourseRangeDTO(entry));
			}else{
				List<CourseRangeDTO> addList = new ArrayList<CourseRangeDTO>();
				addList.add(new CourseRangeDTO(entry));
				map.put(entry.getXqId(),addList);
			}
		}
		int count = 0;
		Set<ObjectId> keySet = map.keySet();
		Iterator<ObjectId> it = keySet.iterator();
		while(it.hasNext()){
			ObjectId id = it.next();
			if (map.get(id).size() > count) {
				count = map.get(id).size();
				result = map.get(id);
			}
		}
		return result;
	}

	public List<CourseRangeDTO> getListBySchoolIdAndKJIdsList(List<ObjectId> kjIds,String schoolId){
		List<CourseRangeDTO> result = new ArrayList<CourseRangeDTO>();
		List<CourseRangeEntry> list = dao.getEntryListBySchoolIdAndKJIdsList(kjIds,new ObjectId(schoolId));
		DayRangeEntry dayRangeEntry = dayRangeDao.getEntryBySchoolId(new ObjectId(schoolId));
		for(CourseRangeEntry entry:list) {
			CourseRangeDTO dto = new CourseRangeDTO(entry);
			dto.setRange("");
			if(dayRangeEntry!=null) {
				if(entry.getStart()>=dayRangeEntry.getStart1()&&entry.getEnd()<=dayRangeEntry.getEnd1()) {
					dto.setRange("上午");
				}
				else if(entry.getStart()>=dayRangeEntry.getStart2()&&entry.getEnd()<=dayRangeEntry.getEnd2()){
					dto.setRange("下午");
				}
				else if(entry.getStart()>=dayRangeEntry.getStart3()&&entry.getEnd()<=dayRangeEntry.getEnd3()){
					dto.setRange("晚上");
				}
			}
			result.add(dto);
		}
		return result;
	}

	public List<CourseRangeDTO> getListBySchoolIdAndKJIds(List<ObjectId> kjIds,String schoolId,ObjectId xqid){
		List<CourseRangeDTO> result = new ArrayList<CourseRangeDTO>();
		List<CourseRangeEntry> list = dao.getEntryListBySchoolIdAndKJIds(kjIds,new ObjectId(schoolId), xqid);
		DayRangeEntry dayRangeEntry = dayRangeDao.getEntryBySchoolId(new ObjectId(schoolId));
		for(CourseRangeEntry entry:list) {
			CourseRangeDTO dto = new CourseRangeDTO(entry);
			dto.setRange("");
			if(dayRangeEntry!=null) {
				if(entry.getStart()>=dayRangeEntry.getStart1()&&entry.getEnd()<=dayRangeEntry.getEnd1()) {
					dto.setRange("上午");
				}
				else if(entry.getStart()>=dayRangeEntry.getStart2()&&entry.getEnd()<=dayRangeEntry.getEnd2()){
					dto.setRange("下午");
				}
				else if(entry.getStart()>=dayRangeEntry.getStart3()&&entry.getEnd()<=dayRangeEntry.getEnd3()){
					dto.setRange("晚上");
				}
			}
			result.add(dto);
		}
		return result;
	}

	/**
	 * 手动同步上学期的课表结构
	 * @param schoolId
	 * @param xqid
	 */
	public void syncLastTermRange(String schoolId,String xqid) {
		dao.removeEntryByXq(new ObjectId(schoolId), new ObjectId(xqid));
		List<TermEntry> termList = termDao.getIsolateTermEntrysBySidOrder(new ObjectId(schoolId));
		String lastXqid = null;
		for(int i=termList.size()-1;i>=0;i--) {
			if(termList.get(i).getID().toString().equals(xqid)&&i!=0) {
				lastXqid = termList.get(i-1).getID().toString();
			}
		}
		if(lastXqid!=null) {
			List<CourseRangeEntry> lastList = dao.getEntryListBySchoolId(new ObjectId(schoolId), new ObjectId(lastXqid));
			for(CourseRangeEntry entry:lastList) {
				entry.setID(new ObjectId());
				entry.setXqId(new ObjectId(xqid));
				dao.saveEntry(entry);// 复制上学期的
			}
		}
	}
	public void save(CourseRangeDTO dto) throws ParseException {
		dao.saveEntry(dto.buildEntry());
	}

	public void removeById(String id) {
		CourseRangeEntry courseRangeEntry = dao.getEntryByID(new ObjectId(id));
		Integer y = courseRangeEntry.getSerial() - 1;
		ykbDao.removeN33_YKBEntryByY(courseRangeEntry.getXqId(),y);
		dao.removeEntry(new ObjectId(id));
	}

	/**
	 * 设置走班时间
	 */
	public void setZouBanTime(ObjectId sid,ObjectId xqid,ObjectId gid,Integer type,Integer x,Integer y){
		ZouBanTimeEntry entry = timeDao.getZouBanTime(sid,xqid,gid,x,y);
		if(entry != null){
			entry.setType(type);
			timeDao.updateZouBanTimeEntry(entry);
		}else{
			ZouBanTimeEntry entry1 = new ZouBanTimeEntry(sid,xqid,type,x,y,gid);
			timeDao.setZouBanTime(entry1);
		}
	}

	/**
	 * 根据学校和次id查询所有的走班时间(用于同步数据)
	 */
	public List<ZouBanTimeEntry> getZouBanTimeForEntry(ObjectId sid, ObjectId cid){
		List<ZouBanTimeEntry> list = timeDao.getZouBanTimeListForEntry(sid,cid);
		return list;
	}

	/**
	 * 查询所有走班时间  0或1
	 */
	public List<ZouBanTimeDTO> getZouBanTime(ObjectId sid, ObjectId xqid, ObjectId gid){
		List<ZouBanTimeDTO> dtoList = new ArrayList<ZouBanTimeDTO>();
		List<ZouBanTimeEntry> list = timeDao.getZouBanTimeList(sid,xqid,gid);
		for (ZouBanTimeEntry entry:list) {
			ZouBanTimeDTO dto = new ZouBanTimeDTO(entry);
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * 查询走班时间       0
	 */
	public List<ZouBanTimeDTO> getZouBanTimeByType(ObjectId sid, ObjectId xqid, ObjectId gid,Integer type){
		List<ZouBanTimeDTO> dtoList = new ArrayList<ZouBanTimeDTO>();
		List<ZouBanTimeEntry> list = timeDao.getZouBanTimeListByType(sid,xqid,gid,type);
		for (ZouBanTimeEntry entry:list) {
			ZouBanTimeDTO dto = new ZouBanTimeDTO(entry);
			dtoList.add(dto);
		}
		return dtoList;
	}

}
