package com.fulaan.new33.service;
import com.db.new33.*;
import com.db.new33.isolate.*;
import com.db.new33.paike.*;
import com.db.user.UserDao;
import com.fulaan.new33.dto.isolate.*;
import com.fulaan.new33.dto.paike.*;
import com.fulaan.new33.service.isolate.*;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.TermEntry;
import com.pojo.new33.isolate.ZouBanTimeEntry;
import com.pojo.new33.paike.*;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.*;

/**
 * Created by albin on 2018/3/20.
 */
@Service
public class N33_ZKB_TempService extends BaseService {
	private N33_ZKBDao zkbDao = new N33_ZKBDao();
	private N33_ZKB_TempDao zkb_tempDao = new N33_ZKB_TempDao();
	private N33_JXBDao jxbDao = new N33_JXBDao();
	private N33_TeaDao teaDao = new N33_TeaDao();
	private N33_SWService swService = new N33_SWService();
	private N33_StudentTagDao studentTagDao = new N33_StudentTagDao();
	private N33_GradeWeekRangeDao n33_gradeWeekRangeDao = new N33_GradeWeekRangeDao();
	N33_TurnOffDao turnOffDao = new N33_TurnOffDao();
	@Autowired
	private N33_ClassroomService classroomService;
	@Autowired
	private CourseRangeService courseRangeService;

	@Autowired
	private N33_GDSXService gdsxService;

	private N33_ZIXIKEDao zixikeDao = new N33_ZIXIKEDao();
	private N33_ZhuanXiangDao zhuanXiangDao = new N33_ZhuanXiangDao();
	private N33_ClassroomDao classroomDao = new N33_ClassroomDao();
	@Autowired
	private IsolateSubjectService subjectService;

	private N33_JXBKS_TempDao jxbksTempDao = new N33_JXBKS_TempDao();

	private TermDao termDao = new TermDao();

	private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();

	private ZouBanTimeDao timeDao = new ZouBanTimeDao();

	private N33_CountDao countDao = new N33_CountDao();
	private N33_RemovedXYDao removedXYDao = new N33_RemovedXYDao();
	private N33_TkLogDao tkLogDao = new N33_TkLogDao();

	/**
	 * 将周课表的数据初始化到周课表临时表中
	 * @param termId
	 * @param schoolId
	 * @param week
	 */
	public void initN33_ZKBTemp(ObjectId termId,ObjectId schoolId,Integer week){
		List<N33_ZKBEntry> zkbEntryList = zkbDao.getYKBsByclassRoomIds(termId, schoolId, week);
		List<N33_ZKBEntry_Temp> zkbEntry_tempList = zkb_tempDao.getN33_ZKBEntry_TempByclassRoomIds(termId, schoolId, week);
		if(zkbEntry_tempList.size() == 0){
			List<N33_ZKBEntry_Temp> zkbEntry_temps = new ArrayList<N33_ZKBEntry_Temp>();
			for (N33_ZKBEntry zkbEntry : zkbEntryList) {
				N33_ZKBEntry_Temp zkbEntry_temp = new N33_ZKBEntry_Temp(zkbEntry.getX(),zkbEntry.getY(),zkbEntry.getClassroomId(),zkbEntry.getJxbId(),zkbEntry.getIsUse(),zkbEntry.getSubjectId(),zkbEntry.getTeacherId(),zkbEntry.getGradeId(),zkbEntry.getSchoolId(),zkbEntry.getTermId(),zkbEntry.getWeek(),zkbEntry.getUserId(),zkbEntry.getTime(),zkbEntry.getNJxbId(),zkbEntry.getNSubjectId(),zkbEntry.getNTeacherId(),zkbEntry.getCId());
				zkbEntry_temp.setType(zkbEntry.getType());
				zkbEntry_temp.setID(zkbEntry.getID());
				zkbEntry_temps.add(zkbEntry_temp);
			}
			zkb_tempDao.addN33_ZKBEntry_TempList(zkbEntry_temps);
		}
	}

	public Map<String, Object> getStatus(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map<String, Object> status = (Map<String, Object>) session.getAttribute("saveStatus");
		if (status != null && status.get("st") != null && (Integer) status.get("st") == -1) {
			session.removeAttribute("status");
		}
		return status;
	}

	/**
	 * 多步调课页面保存并提交课表
	 * @param termId
	 * @param schoolId
	 * @param week
	 */
	public Map<String,Object> saveKeBiao(ObjectId termId,ObjectId schoolId,Integer week,Integer tkType,ObjectId userId,Integer eWeek,HttpServletRequest request){
		HttpSession session = request.getSession();
		Map<String, Object> status = new HashMap<String, Object>();
		status.put("st", 1);
		session.setAttribute("saveStatus", status);
		Map<String,Object> retMap = new HashMap<String, Object>();
		Integer count = week % 2;
		if(tkType == 1){
			//短期调课
			List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeek(schoolId, week, termId);
			Map<ObjectId,N33_ZKBEntry> zkbEntryMap = new HashMap<ObjectId, N33_ZKBEntry>();
			for (N33_ZKBEntry zkbEntry : zkbEntryList) {
				zkbEntryMap.put(zkbEntry.getID(),zkbEntry);
			}

			List<N33_ZKBEntry_Temp> zkbEntry_tempList = zkb_tempDao.getN33_ZKBEntry_TempByclassRoomIds(termId, schoolId, week);
			for (N33_ZKBEntry_Temp zkbEntry_temp : zkbEntry_tempList) {
				N33_ZKBEntry zkbEntry = zkbEntryMap.get(zkbEntry_temp.getID());
 				if(((zkbEntry.getType() == zkbEntry_temp.getType())
						&& zkbEntry.getType() == 6
						&& zkbEntry.getJxbId().equals(zkbEntry_temp.getJxbId()) && zkbEntry.getNJxbId().equals(zkbEntry_temp.getNJxbId()))
						|| (zkbEntry.getType() == zkbEntry_temp.getType() && zkbEntry.getType() != 6 && zkbEntry.getJxbId() != null && zkbEntry.getJxbId().equals(zkbEntry_temp.getJxbId()))
					    || zkbEntry.getType() == zkbEntry_temp.getType() && zkbEntry.getType() == 0){
					continue;
				}else{
				    Map<String,ObjectId> map = getData(zkbEntry,zkbEntry_temp,count);
				    ObjectId subId = map.get("subId");
				    ObjectId gid = map.get("gid");
				    ObjectId oSubId = map.get("oSubId");
				    ObjectId tid = map.get("tid");
				    ObjectId oTid = map.get("oTid");
				    ObjectId jxbId = map.get("jxbId");
				    ObjectId oJxbId = map.get("oJxbId");
					tkLogDao.addN33_TkLogEntry(new N33_TkLogEntry(zkbEntry.getID(), zkbEntry.getID(), zkbEntry.getSchoolId(), gid, zkbEntry.getTermId(), userId,
							subId, oSubId, tid, oTid, 0, String.valueOf(week), 1, 0, zkbEntry.getX() + "," + zkbEntry.getY(), zkbEntry.getX() + "," + zkbEntry.getY(),jxbId,oJxbId));
				}
			}
			if(zkbEntry_tempList.size() > 0){
				zkbDao.removeKBByWeek(schoolId, termId, week);
				List<N33_ZKBEntry> zkbEntry_temps = new ArrayList<N33_ZKBEntry>();
				for (N33_ZKBEntry_Temp zkbEntry_temp : zkbEntry_tempList) {
					N33_ZKBDTO dto = new N33_ZKBDTO(zkbEntry_temp, week, zkbEntry_temp.getUserId(), zkbEntry_temp.getTime(), zkbEntry_temp.getCId(), zkbEntry_temp.getTermId());
					N33_ZKBEntry entry1 = dto.buildEntry();
					entry1.setType(zkbEntry_temp.getType());
					entry1.setID(zkbEntry_temp.getID());
					zkbEntry_temps.add(entry1);
				}
				zkbDao.addN33_ZKBEntryList(zkbEntry_temps);
				retMap.put("msg","提交成功");
			}else{
				retMap.put("msg","无可提交数据");
			}
		}else{
			//長期調課
			//长期调课第一周的周课表（用于判断是否可以调课以及完成长期调课的第一周调课）
			List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeek(schoolId, week, termId);
			Map<String,N33_ZKBEntry> zkbEntryMap = new HashMap<String, N33_ZKBEntry>();
			for (N33_ZKBEntry zkbEntry : zkbEntryList) {
				zkbEntryMap.put(zkbEntry.getX() + "" + zkbEntry.getY() + "" + zkbEntry.getClassroomId().toString(),zkbEntry);
			}

			List<N33_ZKBEntry> zkbEntryList2 = zkbDao.getN33_ZKBByWeek(schoolId, week, termId);
			Map<String,N33_ZKBEntry> zkbEntryMap2 = new HashMap<String, N33_ZKBEntry>();
			for (N33_ZKBEntry zkbEntry : zkbEntryList2) {
				zkbEntryMap2.put(zkbEntry.getX() + "" + zkbEntry.getY() + "" + zkbEntry.getClassroomId().toString(),zkbEntry);
			}

			List<N33_ZKBEntry_Temp> zkbEntry_temps = zkb_tempDao.getYKBEntrysList(termId, schoolId, week);
			Map<String,N33_ZKBEntry_Temp> zkbEntry_tempMap = new HashMap<String, N33_ZKBEntry_Temp>();
			for (N33_ZKBEntry_Temp zkbEntry_temp : zkbEntry_temps) {
				zkbEntry_tempMap.put(zkbEntry_temp.getX() + "" + zkbEntry_temp.getY() + "" + zkbEntry_temp.getClassroomId().toString(),zkbEntry_temp);
			}

			List<N33_RemovedXY> removedXYList = removedXYDao.findEntryByTermId(termId,schoolId);

			List<Map<String,Object>> canNotTKList = new ArrayList<Map<String, Object>>();
			//记录循环的第几周，如果是第一周，则直接调课，否则需要判断是否可以调课
			Integer continueCount = 0;
			List<N33_TkLogEntry> tkLogEntries = new ArrayList<N33_TkLogEntry>();
			TermEntry termEntry = termDao.findIsolateTermEntryEntryById(termId);
			List<ObjectId> ciIds = new ArrayList<ObjectId>();
			for (TermEntry.PaiKeTimes paiKeTimes : termEntry.getPaiKeTimes()) {
				if(paiKeTimes.getIr() == 0){
					ciIds.add(paiKeTimes.getID());
				}
			}
			List<Integer> weekList = new ArrayList<Integer>();
			List<Integer> canNotTKWeekList = new ArrayList<Integer>();
			// jxb add 
//	        ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
//	        int acClassType = 0;
//	        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId,entry.getGradeId(),ciId,1);
//	        if(null!=turnOff){
//	        	acClassType = turnOff.getAcClass();
//	        }
	        // jxb add
			Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapsByCiIds(ciIds);

			Map<ObjectId, N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomEntryListByXqGradeForMap(ciIds, schoolId);
			for (int i = week;i <= eWeek; i ++) {
				//判断该周是否可以调课，有一节课不一样就不允许调课
				List<N33_ZKBEntry> zkbEntryList1 = zkbDao.getN33_ZKBByWeek(schoolId, i, termId);
				Map<String,N33_ZKBEntry> zkbEntryMap1 = new HashMap<String, N33_ZKBEntry>();
				for (N33_ZKBEntry zkbEntry : zkbEntryList1) {
					zkbEntryMap1.put(zkbEntry.getX() + "" + zkbEntry.getY() + "" + zkbEntry.getClassroomId().toString(),zkbEntry);
				}
				if(continueCount > 0){
					boolean tkFlag = true;
					for (N33_ZKBEntry zkbEntry : zkbEntryList1) {
						N33_ZKBEntry zkbEntry1 = zkbEntryMap2.get(zkbEntry.getX() + "" + zkbEntry.getY() + "" + zkbEntry.getClassroomId().toString());
						//判断两周的课表有没有不同，如果有，记录哪些不同，如果没有，调课，并记录
						if(((zkbEntry1.getType() == zkbEntry.getType())
								&& zkbEntry1.getType() == 6
								&& zkbEntry1.getJxbId().equals(zkbEntry.getJxbId()) && zkbEntry1.getNJxbId().equals(zkbEntry.getNJxbId()))
								|| (zkbEntry1.getType() == zkbEntry.getType() && zkbEntry1.getType() != 6 && zkbEntry1.getJxbId() != null && zkbEntry1.getJxbId().equals(zkbEntry.getJxbId()))
								|| zkbEntry1.getType() == zkbEntry.getType() && zkbEntry1.getType() == 0){
							continue;
						}else{
							tkFlag = false;
							Map<String,Object> jxbXYMap = new HashMap<String, Object>();
							jxbXYMap.put("week","第" + i + "周");
							jxbXYMap.put("xy","周" + (zkbEntry1.getX() + 1) + "第" + (zkbEntry1.getY() + 1) + "节");
							if(zkbEntry1.getType() == 6){
								if(count == 0){
									jxbXYMap.put("jxbName",("".equals(jxbEntryMap.get(zkbEntry1.getNJxbId()).getNickName()) || jxbEntryMap.get(zkbEntry1.getNJxbId()).getNickName() == null) ? jxbEntryMap.get(zkbEntry1.getNJxbId()).getName() + "(双)" : jxbEntryMap.get(zkbEntry1.getNJxbId()).getNickName()+ "(双)");
								}else{
									jxbXYMap.put("jxbName",("".equals(jxbEntryMap.get(zkbEntry1.getJxbId()).getNickName()) || jxbEntryMap.get(zkbEntry1.getJxbId()).getNickName() == null) ? jxbEntryMap.get(zkbEntry1.getJxbId()).getName()+ "(单)" : jxbEntryMap.get(zkbEntry1.getJxbId()).getNickName()+ "(单)");
								}
							}else if((zkbEntry1.getType() == 0) || zkbEntry1.getJxbId() == null || "".equals(zkbEntry1.getJxbId().toString())){
									jxbXYMap.put("jxbName","");
							}else{
								jxbXYMap.put("jxbName",("".equals(jxbEntryMap.get(zkbEntry1.getJxbId()).getNickName()) || jxbEntryMap.get(zkbEntry1.getJxbId()).getNickName() == null) ? jxbEntryMap.get(zkbEntry1.getJxbId()).getName() : jxbEntryMap.get(zkbEntry1.getJxbId()).getNickName());
							}
							jxbXYMap.put("classRoomName",classroomEntryMap.get(zkbEntry1.getClassroomId()).getRoomName());
							//一周中导致不能排课的格子中的课
							canNotTKList.add(jxbXYMap);
						}
					}
					if(tkFlag){
						for (N33_RemovedXY removedXY : removedXYList) {
							N33_ZKBEntry zkbEntry = zkbEntryMap1.get(removedXY.getX() + "" + removedXY.getY() + "" + removedXY.getClassRoomId().toString());
							N33_ZKBEntry_Temp zkbEntry_temp = zkbEntry_tempMap.get(removedXY.getX() + "" + removedXY.getY() + "" + removedXY.getClassRoomId().toString());
							if((zkbEntry.getType() == zkbEntry_temp.getType() && zkbEntry.getType() == 0)
									|| (zkbEntry.getType() == zkbEntry_temp.getType() && zkbEntry.getType() != 6 && zkbEntry.getJxbId() != null && zkbEntry.getJxbId().equals(zkbEntry_temp.getJxbId()))
									|| (zkbEntry.getType() == zkbEntry_temp.getType() && zkbEntry.getType() == 6 && zkbEntry.getJxbId().equals(zkbEntry_temp.getJxbId()) && zkbEntry.getNJxbId().equals(zkbEntry_temp.getNJxbId()))){
								continue;
							}else{
								updateZKB(zkbEntry,zkbEntry_temp);
							}
						}
						weekList.add(i);
					}else{
						//不能调课的周
						canNotTKWeekList.add(i);
					}
				}else{
					weekList.add(i);
					//直接调长期调课中第一周的课
					for (N33_RemovedXY removedXY : removedXYList) {
						N33_ZKBEntry zkbEntry = zkbEntryMap.get(removedXY.getX() + "" + removedXY.getY() + "" + removedXY.getClassRoomId().toString());
						N33_ZKBEntry_Temp zkbEntry_temp = zkbEntry_tempMap.get(removedXY.getX() + "" + removedXY.getY() + "" + removedXY.getClassRoomId().toString());
						if((zkbEntry.getType() == zkbEntry_temp.getType() && zkbEntry.getType() == 0)
								|| (zkbEntry.getType() == zkbEntry_temp.getType() && zkbEntry.getType() != 6 && zkbEntry.getJxbId() != null && zkbEntry.getJxbId().equals(zkbEntry_temp.getJxbId()))
								|| (zkbEntry.getType() == zkbEntry_temp.getType() && zkbEntry.getType() == 6 && zkbEntry.getJxbId().equals(zkbEntry_temp.getJxbId()) && zkbEntry.getNJxbId().equals(zkbEntry_temp.getNJxbId()))){
							continue;
						}else{
							Map<String,ObjectId> map = getData(zkbEntry,zkbEntry_temp,count);
							ObjectId subId = map.get("subId");
							ObjectId gid = map.get("gid");
							ObjectId oSubId = map.get("oSubId");
							ObjectId tid = map.get("tid");
							ObjectId oTid = map.get("oTid");
							ObjectId jxbId = map.get("jxbId");
							ObjectId oJxbId = map.get("oJxbId");
							N33_TkLogEntry tkLogEntry = new N33_TkLogEntry(zkbEntry.getID(), zkbEntry.getID(), zkbEntry.getSchoolId(), gid, zkbEntry.getTermId(), userId,
									subId, oSubId, tid, oTid, 0, "", 1, 1, zkbEntry.getX() + "," + zkbEntry.getY(), zkbEntry.getX() + "," + zkbEntry.getY(),jxbId,oJxbId);
							tkLogEntries.add(tkLogEntry);
							updateZKB(zkbEntry,zkbEntry_temp);
						}
					}
				}
				continueCount++;
			}

			String s = getWeekString(weekList);
			for (N33_TkLogEntry tkLogEntry : tkLogEntries) {
				tkLogEntry.setWeek(s);
			}
			if(tkLogEntries.size() > 0){
				tkLogDao.addN33_TkLogEntry(tkLogEntries);
			}
			removedXYDao.deleteByTermId(schoolId,termId);
			retMap.put("msg","提交成功");
			retMap.put("list",canNotTKList);
			String canNotTKString = "";
			for (int i = 0;i < canNotTKWeekList.size();i ++) {
				if(i == canNotTKWeekList.size() - 1){
					canNotTKString += canNotTKWeekList.get(canNotTKWeekList.size() - 1);
				}else{
					canNotTKString += canNotTKWeekList.get(i);
					canNotTKString += "、";
				}
			}
			retMap.put("canNotTKString",canNotTKString);
		}
		status.put("st", -1);
		return retMap;
	}

	private String getWeekString(List<Integer> weekList){
		String s = "";
		Integer week0 = weekList.get(0);
		int a = 0;
		if(weekList.size() == 1){
			s = week0 + "";
		}else{
			for (Integer weeks : weekList) {
				if(a > 0){
					if(weekList.get(a) == (weekList.get(a - 1) + 1)){
						if(a == weekList.size() - 1){
							week0 = weekList.get(a);
							if("".equals(s)){
								if(week0 == weekList.get(a - 1)){
									s += week0;
								}else{
									s += weekList.get(a - 1);
									s += "-";
									s += week0;
								}
							}else{
								if(week0 == weekList.get(a - 1)){
									s += ",";
									s += week0;
								}else{
									s += ",";
									s += weekList.get(a - 1);
									s += "-";
									s += week0;
								}
							}
						}
					}else{
						if("".equals(s)){
							if(week0 == weekList.get(a - 1)){
								s += week0;
							}else{
								s += week0;
								s += "-";
								s += weekList.get(a - 1);
							}
						}else{
							if(week0 == weekList.get(a - 1)){
								s += ",";
								s += week0;
							}else{
								s += ",";
								s += week0;
								s += "-";
								s += weekList.get(a - 1);
							}
						}
						if(weekList.size() > a){
							week0 = weekList.get(a);
						}else{
							break;
						}
						if((weekList.size() - 1) == a){
							s += ",";
							s += week0;
						}
					}
				}
				a ++;
			}
		}
		return s;
	}

	private void updateZKB(N33_ZKBEntry zkbEntry,N33_ZKBEntry_Temp zkbEntry_temp){
		zkbEntry.setIsUse(zkbEntry_temp.getIsUse());
		zkbEntry.setJxbId(zkbEntry_temp.getJxbId());
		zkbEntry.setTeacherId(zkbEntry_temp.getTeacherId());
		zkbEntry.setSubjectId(zkbEntry_temp.getSubjectId());
		zkbEntry.setNJxbId(zkbEntry_temp.getNJxbId());
		zkbEntry.setNSubjectId(zkbEntry_temp.getNSubjectId());
		zkbEntry.setNTeacherId(zkbEntry_temp.getNTeacherId());
		zkbEntry.setType(zkbEntry_temp.getType());
		zkbDao.updateN33_ZKB(zkbEntry);
	}

	private Map<String,ObjectId> getData(N33_ZKBEntry zkbEntry,N33_ZKBEntry_Temp zkbEntry_temp,Integer count){
		ObjectId subId = null;
		ObjectId oSubId = null;
		ObjectId jxbId = null;
		ObjectId oJxbId = null;
		ObjectId tid = null;
		ObjectId oTid = null;
		ObjectId gid = null;
		if(zkbEntry.getType() == 6){
			if(count == 0){
				oSubId = zkbEntry.getNSubjectId();
				oJxbId = zkbEntry.getNJxbId();
				oTid = zkbEntry.getNTeacherId();
			}else{
				oSubId = zkbEntry.getSubjectId();
				oJxbId = zkbEntry.getJxbId();
				oTid = zkbEntry.getTeacherId();
			}
		}else if(zkbEntry.getType() == 4){
			oSubId = zkbEntry.getSubjectId();
			oJxbId = zkbEntry.getJxbId();
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(zkbEntry.getJxbId(),zkbEntry.getCId());
			for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
				if(zkbEntry.getClassroomId().equals(zhuanXiangEntry.getRoomId())){
					oTid = zhuanXiangEntry.getTeaId();
				}
			}
		}else{
			oSubId = zkbEntry.getSubjectId();
			oJxbId = zkbEntry.getJxbId();
			oTid = zkbEntry.getTeacherId();
		}

		if(zkbEntry_temp.getType() == 6){
			if(count == 0){
				subId = zkbEntry_temp.getNSubjectId();
				jxbId = zkbEntry_temp.getNJxbId();
			}else{
				subId = zkbEntry_temp.getSubjectId();
				jxbId = zkbEntry_temp.getJxbId();
			}
		}else if(zkbEntry_temp.getType() == 4){
			subId = zkbEntry_temp.getSubjectId();
			jxbId = zkbEntry_temp.getJxbId();
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(zkbEntry_temp.getJxbId(),zkbEntry_temp.getCId());
			for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
				if(zkbEntry_temp.getClassroomId().equals(zhuanXiangEntry.getRoomId())){
					tid = zhuanXiangEntry.getTeaId();
				}
			}
		}else{
			subId = zkbEntry_temp.getSubjectId();
			jxbId = zkbEntry_temp.getJxbId();
			tid = zkbEntry_temp.getTeacherId();
		}
		if(zkbEntry.getGradeId() == null || "".equals(zkbEntry.getGradeId())){
			gid = zkbEntry_temp.getGradeId();
		}else{
			gid = zkbEntry.getGradeId();
		}

		Map<String,ObjectId> map = new HashMap<String, ObjectId>();
		map.put("subId",subId);
		map.put("oSubId",oSubId);
		map.put("jxbId",jxbId);
		map.put("oJxbId",oJxbId);
		map.put("tid",tid);
		map.put("oTid",oTid);
		map.put("gid",gid);
		return map;
	}


	/**
	 * 加载课表list
	 *
	 * @param gradeId
	 * @param classRoomId
	 * @param schoolId
	 */
	public Map getKeBiaoList(ObjectId termId, String gradeId, String classRoomId, ObjectId schoolId,Integer week) {
		Map retMap = new HashMap();
		Integer count = week % 2;
		List<N33_ZKBEntry_Temp> zkbtempEntryList = zkb_tempDao.getN33_ZKBEntry_TempByclassRoomIds(termId, schoolId, week, new ObjectId(gradeId));
		List<ObjectId> zxk = new ArrayList<ObjectId>();
		for (N33_ZKBEntry_Temp entry : zkbtempEntryList) {
			if (entry.getType() == 5) {
				zxk.add(entry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxk);
		if(zkbtempEntryList.size() > 0){
			ObjectId cid = zkbtempEntryList.get(0).getCId();
			Map map = new HashMap();
			List<N33_ClassroomDTO> classroomDTOs = classroomService.getRoomEntryListByXqGradeAndCiId(cid, schoolId, new ObjectId(gradeId), 1);

			List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), cid);
			List<ObjectId> classroomIds = new ArrayList<ObjectId>();classroomIds.add(new ObjectId(classRoomId));
			Map<String, List<N33_ZKBDTO_Temp>> ykbDTOMap = new HashMap<String, List<N33_ZKBDTO_Temp>>();
			Map<String, List<N33_ZKBDTO_Temp>> ykbDTOMap2 = new HashMap<String, List<N33_ZKBDTO_Temp>>();
			List<String> swlist = new ArrayList<String>();
			List<N33_SWDTO> swdtos = swService.getGuDingShiWuByXqid(termId);
			Map<String, N33_SWDTO> swdtoMap = new HashMap<String, N33_SWDTO>();
			for (N33_SWDTO swdto : swdtos) {
				swdtoMap.put((swdto.getY() - 1) + "," + (swdto.getX() - 1), swdto);
			}
			if (swdtos != null && swdtos.size() != 0) {
				for (N33_SWDTO swdto : swdtos) {
					swlist.add(swdto.getXy());
				}
			}
			List<String> gdsxList = new ArrayList<String>();
			List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(termId, schoolId, new ObjectId(gradeId));
			Map<String, N33_GDSXDTO> gdsxdtoMap = new HashMap<String, N33_GDSXDTO>();
			if (gdsxdtos != null && gdsxdtos.size() != 0) {
				for (N33_GDSXDTO gdsxdto : gdsxdtos) {
					gdsxdtoMap.put(gdsxdto.getX() + "," + gdsxdto.getY(), gdsxdto);
				}
			}

			if (gdsxdtos != null && gdsxdtos.size() != 0) {
				for (N33_GDSXDTO gdsxdto : gdsxdtos) {
					gdsxList.add(String.valueOf(gdsxdto.getX()) + gdsxdto.getY());
				}
			}

			N33_GradeWeekRangeEntry gradeWeekRangeEntry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(cid, schoolId, new ObjectId(gradeId));
			List<IdNameValuePairDTO> weekList = new ArrayList<IdNameValuePairDTO>();
			String[] weekArgs = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
			if (gradeWeekRangeEntry != null) {
				for (int i = gradeWeekRangeEntry.getStart(); i <= gradeWeekRangeEntry.getEnd(); i++) {
					IdNameValuePairDTO dto = new IdNameValuePairDTO();
					dto.setName(weekArgs[i - 1]);
					dto.setType(i);
					weekList.add(dto);
				}
			}

			List<N33_ZKBDTO_Temp> n33_zkbdto_temps = new ArrayList<N33_ZKBDTO_Temp>();
			List<N33_ZKBEntry_Temp> zkbEntry_temps = zkb_tempDao.getYKBsByclassRoomIds(termId, schoolId, classroomIds, week);
			if (zkbEntry_temps != null && zkbEntry_temps.size() != 0) {
				List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(zkbEntry_temps, "tid");
				Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, cid);
				List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(zkbEntry_temps, "jxbId");
				jxbIds.addAll(MongoUtils.getFieldObjectIDs(zkbEntry_temps, "nJxbId"));
				Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapsByIds(jxbIds);
				Map<ObjectId, String> tagMaps = new HashMap<ObjectId, String>();
				List<ObjectId> tagIds = new ArrayList<ObjectId>();
				if (jxbEntryMap != null && jxbEntryMap.size() != 0) {
					for (Map.Entry<ObjectId, N33_JXBEntry> entry : jxbEntryMap.entrySet()) {
						tagIds.addAll(entry.getValue().getTagIds());
					}
					Map<ObjectId, N33_StudentTagEntry> studentTagEntryMap = studentTagDao.getTagListByIdsMap(tagIds);
					for (Map.Entry<ObjectId, N33_JXBEntry> entry : jxbEntryMap.entrySet()) {
						List<ObjectId> tags = entry.getValue().getTagIds();
						String tag = "";
						if (tags != null && tags.size() != 0) {
							for (ObjectId id : tags) {
								tag += studentTagEntryMap.get(id) != null ? studentTagEntryMap.get(id).getName() : "" + ",";
							}
						}
						tagMaps.put(entry.getKey(), tag);
					}
				}

				for (N33_ZKBEntry_Temp ykbEntry : zkbEntry_temps) {

					n33_zkbdto_temps.add(new N33_ZKBDTO_Temp(ykbEntry));
					List<N33_ZKBDTO_Temp> n33_ykbdtos1 = ykbDTOMap.get(ykbEntry.getClassroomId().toString());
					if (n33_ykbdtos1 == null || n33_ykbdtos1.size() == 0) {
						n33_ykbdtos1 = new ArrayList<N33_ZKBDTO_Temp>();
					}
					N33_ZKBDTO_Temp ykbdto = new N33_ZKBDTO_Temp(ykbEntry);
					if (ykbdto.getNtid() != null) {
						if (count == 0) {
							if (StringUtils.isNotEmpty(ykbdto.getNtid())) {
								ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getNtid())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getNtid())).getUserName() : "");
							}
							if (StringUtils.isNotEmpty(ykbdto.getnJxbId())) {
								ykbdto.setJxbType(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getType() : 1);
								if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getNickName())) {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getNickName());
								} else {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getName());
								}
								ykbdto.setStudentCount(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getStudentIds().size() : 0);
								ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getnJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getnJxbId())) : "");
							}
						} else {
							if (StringUtils.isNotEmpty(ykbdto.getTeacherId())) {
								ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())).getUserName() : "");
							}
							if (StringUtils.isNotEmpty(ykbdto.getJxbId())) {
								if (ykbdto.getType() == 5) {
									ykbdto.setJxbType(5);
									N33_ZIXIKEEntry zixikeEntry = zixikeDao.getJXBById(new ObjectId(ykbdto.getJxbId()));
									if (zixikeEntry != null) {
										ykbdto.setJxbName(zixikeEntry.getName());
										ykbdto.setStudentCount(zixikeEntry.getStudentIds().size());
									}
								} else {
									ykbdto.setJxbType(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getType() : 1);
									if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName())) {
										ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName());
									} else {
										ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getName());
									}
									ykbdto.setStudentCount(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getStudentIds().size() : 0);
									ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getJxbId())) : "");
								}
							}
						}
					} else {
						if (StringUtils.isNotEmpty(ykbdto.getTeacherId())) {
							ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())).getUserName() : "");
						}
						if (StringUtils.isNotEmpty(ykbdto.getJxbId())) {
							if (ykbdto.getType() != 5) {
								ykbdto.setJxbType(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getType() : 1);
								if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName())) {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName());
								} else {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getName());
								}
								ykbdto.setStudentCount(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getStudentIds().size() : 0);
								ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getJxbId())) : "");
							} else {
								N33_ZIXIKEEntry entry = zixikeEntries.get(new ObjectId(ykbdto.getJxbId()));
								if (entry == null) {
									continue;
								}
								ykbdto.setJxbName(entry.getName() + "(自习课)");
								ykbdto.setStudentCount(entry.getStudentIds().size());
								ykbdto.setRemarks("");
								ykbdto.setType(5);
							}
						}
					}
					ykbdto.setSwType(0);
					ykbdto.setIsSWAndCourse(0);
					if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY())) {
						ykbdto.setSwType(1);
						if (ykbdto.getIsUse() != 0 || (ykbdto.getJxbId() != null && ykbdto.getJxbId() != "")) {
							ykbdto.setIsSWAndCourse(1);
						}
						ykbdto.setRemarks(swdtoMap.get(ykbEntry.getX() + "," + ykbEntry.getY()).getDesc());
					}
					if (gdsxList != null && gdsxList.size() != 0 && gdsxList.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY())) {
						ykbdto.setSwType(2);
						if (ykbdto.getIsUse() != 0 || (ykbdto.getJxbId() != null && ykbdto.getJxbId() != "")) {
							ykbdto.setIsSWAndCourse(1);
						}
						ykbdto.setRemarks(gdsxdtoMap.get(ykbEntry.getX() + "," + ykbEntry.getY()).getDesc());
					}
					n33_ykbdtos1.add(ykbdto);
					ykbDTOMap.put(ykbEntry.getClassroomId().toString(), n33_ykbdtos1);
				}
			}
			for (Map.Entry<String, List<N33_ZKBDTO_Temp>> entry : ykbDTOMap.entrySet()) {
				List<N33_ZKBDTO_Temp> ykbdtos = entry.getValue();
				Collections.sort(ykbdtos, new Comparator<N33_ZKBDTO_Temp>() {
					@Override
					public int compare(N33_ZKBDTO_Temp o1, N33_ZKBDTO_Temp o2) {
						return o1.getY() - o2.getY();
					}
				});
				ykbDTOMap.put(entry.getKey(), ykbdtos);
			}

			List<N33_ClassroomDTO> classroomDTOs2 = new ArrayList<N33_ClassroomDTO>();
			if (classroomDTOs != null && classroomDTOs.size() != 0) {
				for (N33_ClassroomDTO dto : classroomDTOs) {
					if(classRoomId.equals(dto.getRoomId())){
						classroomDTOs2.add(dto);
					}
					dto.setWeekList(weekList);
					if (ykbDTOMap.get(dto.getRoomId()) != null) {
						ykbDTOMap2.put(dto.getRoomId(), ykbDTOMap.get(dto.getRoomId()));
					}
				}
			}
			map.put("courseRangeDTOs", courseRangeDTOs);
			map.put("ykbdto", ykbDTOMap2);
			map.put("classrooms", classroomDTOs2);
			return map;
		}
		return retMap;
	}

	/**
	 * 获取教学班
	 *
	 * @param zkbId
	 * @param gradeId
	 * @param schoolId
	 * @return
	 */
	public Map getJXBById(String zkbId, String gradeId, ObjectId schoolId,Integer week) {
		List<N33_XKBDTO> xkbdtos = new ArrayList<N33_XKBDTO>();
		Map map = new HashMap();
		Map<String, List<N33_ZKBDTO_Temp>> ykbdtoMaps = new HashMap<String, List<N33_ZKBDTO_Temp>>();
		N33_ZKBEntry_Temp zkbEntry_temp = zkb_tempDao.getN33_ZKB_TempById(new ObjectId(zkbId));
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		Map<ObjectId, ObjectId> jxbClrIdMap = new HashMap<ObjectId, ObjectId>();
		List<N33_ZKBEntry_Temp> zkbEntry_temps = zkb_tempDao.getZKB_TempEntrysList(zkbEntry_temp.getCId(), zkbEntry_temp.getSchoolId(), new ObjectId(gradeId),week);
		if (zkbEntry_temps != null && zkbEntry_temps.size() != 0) {
			for (N33_ZKBEntry_Temp entry : zkbEntry_temps) {
				if (zkbEntry_temp.getX() == entry.getX() && zkbEntry_temp.getY() == entry.getY()) {
					if (entry.getJxbId() != null) {
						jxbIds.add(entry.getJxbId());
						jxbClrIdMap.put(entry.getJxbId(), entry.getClassroomId());
					}
					if (entry.getNJxbId() != null) {
						jxbIds.add(entry.getNJxbId());
						jxbClrIdMap.put(entry.getNJxbId(), entry.getClassroomId());
					}
				}
			}
		}
		List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
		List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBListByIds(jxbIds);
		List<ObjectId> tagIds = new ArrayList<ObjectId>();
		List<ObjectId> teacherIds = new LinkedList<ObjectId>();
		List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(zkbEntry_temp.getCId());
		Map<ObjectId, N33_ClassroomEntry> classroomMap = classroomService.getRoomListMapBySchoolId(schoolId, zkbEntry_temp.getCId());
		if (jxbEntryList != null && jxbEntryList.size() != 0) {
			for (N33_JXBEntry entry : jxbEntryList) {
				if (entry.getType() == 4) {
					for (N33_ZhuanXiangEntry zxentry : zhuanXiangEntryList) {
						if (entry.getID().equals(zxentry.getJxbId()) && zxentry.getTeaId() != null) {
							if (zkbEntry_temp.getNTeacherId() != null && !zkbEntry_temp.getNTeacherId().toString().equals(zxentry.getTeaId().toString())) {
								teacherIds.add(zxentry.getTeaId());
							} else if (zkbEntry_temp.getTeacherId() != null && !zkbEntry_temp.getTeacherId().toString().equals(zxentry.getTeaId().toString())) {
								teacherIds.add(zxentry.getTeaId());
							}else if(zkbEntry_temp.getTeacherId() == null){
								teacherIds.add(zxentry.getTeaId());
							}
						}
					}
				} else {
					tagIds.addAll(entry.getTagIds());
					if (entry.getTercherId() != null) {
						if (zkbEntry_temp.getNTeacherId() != null && !zkbEntry_temp.getNTeacherId().toString().equals(entry.getTercherId().toString())) {
							teacherIds.add(entry.getTercherId());
						} else if (zkbEntry_temp.getTeacherId() != null && !zkbEntry_temp.getTeacherId().toString().equals(entry.getTercherId().toString())) {
							teacherIds.add(entry.getTercherId());
						}else if(zkbEntry_temp.getTeacherId() == null){
							teacherIds.add(entry.getTercherId());
						}
					}
				}
			}
			if (zkbEntry_temp.getNTeacherId() != null) {
				((LinkedList<ObjectId>) teacherIds).addFirst(zkbEntry_temp.getNTeacherId());
			}
			if (zkbEntry_temp.getTeacherId() != null) {
				((LinkedList<ObjectId>) teacherIds).addFirst(zkbEntry_temp.getTeacherId());
			}
			Map<ObjectId, N33_StudentTagEntry> studentTagEntryMap = studentTagDao.getTagListByIdsMap(tagIds);
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, zkbEntry_temp.getCId());
			for (N33_JXBEntry entry : jxbEntryList) {
				N33_JXBDTO jxbdto = new N33_JXBDTO(entry);
				List<ObjectId> tags = entry.getTagIds();
				String tag = "";
				if (tags != null && tags.size() != 0) {
					for (ObjectId id : tags) {
						tag += studentTagEntryMap.get(id) != null ? studentTagEntryMap.get(id).getName() : "" + ",";
					}
				}
				jxbdto.setClassroomName("");
				if (jxbClrIdMap.get(entry.getID()) != null) {
					jxbdto.setClassroomName(classroomMap.get(jxbClrIdMap.get(entry.getID())).getRoomName());
				}
				jxbdto.setTeacherName(teaEntryMap.get(entry.getTercherId()) != null ? teaEntryMap.get(entry.getTercherId()).getUserName() : "");
				jxbdto.setRemarks(tag);
				jxbdtos.add(jxbdto);
			}
			ykbdtoMaps = getYKBbyTeacherIds(teacherIds, zkbEntry_temp.getCId(), zkbEntry_temp.getSchoolId(),week);

			if (teacherIds != null && teacherIds.size() != 0) {
				List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), zkbEntry_temp.getCId());
				for (ObjectId tid : teacherIds) {
					boolean flag = true;
					for (N33_XKBDTO xkbdto : xkbdtos){
						if(tid.toString().equals(xkbdto.getTeacherId())){
							flag = false;
						}
					}
					if(flag){
						N33_XKBDTO dto = new N33_XKBDTO();
						dto.setTeacherName(teaEntryMap.get(tid).getUserName());
						dto.setTeacherId(tid.toString());
						dto.setCourseRangeDTOList(courseRangeDTOs);
						xkbdtos.add(dto);
					}
				}
			}
		}

		map.put("jxbdtos", jxbdtos);
		map.put("teakbs", ykbdtoMaps);
		map.put("teas", xkbdtos);
		return map;
	}


	/**
	 * 生成老师小课表
	 *
	 * @param teacherIds
	 * @param ciId
	 * @param schoolId
	 * @return
	 */
	public Map<String, List<N33_ZKBDTO_Temp>> getYKBbyTeacherIds(List<ObjectId> teacherIds, ObjectId ciId, ObjectId schoolId,Integer week) {
		Map<ObjectId,N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomEntryListByXqGradeForMap(ciId,schoolId);
		Map<String, String> subMap = subjectService.getSubjectMapById(schoolId, ciId);
		List<N33_ZKBEntry_Temp> zkbEntry_temps = zkb_tempDao.getZKB_TempEntrysList(ciId, schoolId,week);
		
		List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(schoolId, ciId);
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(schoolId, ciId);
		Map<String, List<N33_ZKBDTO_Temp>> ykbMaps = new HashMap<String, List<N33_ZKBDTO_Temp>>();
		Map<ObjectId, List<N33_ZKBEntry_Temp>> ykbMapByJXBID = new HashMap<ObjectId, List<N33_ZKBEntry_Temp>>();
		if (zkbEntry_temps != null && zkbEntry_temps.size() != 0) {
			for (N33_ZKBEntry_Temp entry : zkbEntry_temps) {
				List<N33_ZKBEntry_Temp> ykbs = ykbMapByJXBID.get(entry.getJxbId());
				if (ykbs == null || ykbs.size() == 0) {
					ykbs = new ArrayList<N33_ZKBEntry_Temp>();
				}
				ykbs.add(entry);
				ykbMapByJXBID.put(entry.getJxbId(), ykbs);
				if (entry.getNJxbId() != null) {
					List<N33_ZKBEntry_Temp> ykbs2 = ykbMapByJXBID.get(entry.getNJxbId());
					if (ykbs2 == null || ykbs2.size() == 0) {
						ykbs2 = new ArrayList<N33_ZKBEntry_Temp>();
					}
					ykbs2.add(entry);
					ykbMapByJXBID.put(entry.getNJxbId(), ykbs2);
				}
			}
		}
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntryByTeaIds(ciId, teacherIds);
		Map<ObjectId, List<ObjectId>> teacherJXBByIdMap = new HashMap<ObjectId, List<ObjectId>>();
		if (zhuanXiangEntries != null && zhuanXiangEntries.size() != 0) {
			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
				List<ObjectId> jxbIds = teacherJXBByIdMap.get(entry.getTeaId());
				if (jxbIds == null || jxbIds.size() == 0) {
					jxbIds = new ArrayList<ObjectId>();
				}
				jxbIds.add(entry.getJxbId());
				teacherJXBByIdMap.put(entry.getTeaId(), jxbIds);
			}
		}
		if (jxbEntryList != null && jxbEntryList.size() != 0) {
			for (N33_JXBEntry entry : jxbEntryList) {
				List<ObjectId> jxbIds = teacherJXBByIdMap.get(entry.getTercherId());
				if (jxbIds == null || jxbIds.size() == 0) {
					jxbIds = new ArrayList<ObjectId>();
				}
				jxbIds.add(entry.getID());
				teacherJXBByIdMap.put(entry.getTercherId(), jxbIds);
			}
		}
		for (ObjectId tid : teacherIds) {
			List<N33_ZKBDTO_Temp> ykbdtos = new ArrayList<N33_ZKBDTO_Temp>();
			List<ObjectId> jxbIds = teacherJXBByIdMap.get(tid);
			if (jxbIds != null && jxbIds.size() != 0) {
				for (ObjectId id : jxbIds) {
					List<N33_ZKBEntry_Temp> n33YkbEntries = ykbMapByJXBID.get(id);
					if (n33YkbEntries != null && n33YkbEntries.size() != 0) {
						for (N33_ZKBEntry_Temp entry : n33YkbEntries) {
							N33_ZKBDTO_Temp dto = new N33_ZKBDTO_Temp(entry);
							if(entry.getType() == 4){
								for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntries) {
									if(entry.getJxbId() != null && entry.getJxbId().toString().equals(zhuanXiangEntry.getJxbId().toString())){
										dto.setJxbName(zhuanXiangEntry.getName());
										break;
									}
								}
							}else{
								if (entry.getNJxbId() != null && entry.getNJxbId().equals(id)) {
									if(jxbEntryMap.get(entry.getNJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(entry.getNJxbId()).getNickName())){
										dto.setJxbName(jxbEntryMap.get(entry.getNJxbId()).getNickName() + "(双)");
									}else{
										dto.setJxbName(jxbEntryMap.get(entry.getNJxbId()).getName() + "(双)");
									}
								} else {
									if(entry.getType() == 6){
										if(jxbEntryMap.get(entry.getJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(entry.getJxbId()).getNickName())){
											dto.setJxbName(jxbEntryMap.get(entry.getJxbId()).getNickName() + "(单)");
										}else{
											dto.setJxbName(jxbEntryMap.get(entry.getJxbId()).getName() + "(单)");
										}
									}else{
										if(jxbEntryMap.get(entry.getJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(entry.getJxbId()).getNickName())){
											dto.setJxbName(jxbEntryMap.get(entry.getJxbId()).getNickName());
										}else{
											dto.setJxbName(jxbEntryMap.get(entry.getJxbId()).getName());
										}
									}

								}
							}
							if (entry.getNJxbId() != null && entry.getNJxbId().equals(id)) {
								dto.setSubjectName(entry.getNSubjectId() != null ? subMap.get(entry.getNSubjectId().toString()) : "");
							} else {
								dto.setSubjectName(entry.getSubjectId() != null ? subMap.get(entry.getSubjectId().toString()) : "");
							}
							dto.setRoomName(classroomEntryMap.get(entry.getClassroomId()) != null ? classroomEntryMap.get(entry.getClassroomId()).getRoomName() : "");
							ykbdtos.add(dto);
						}
					}
				}
			}
			ykbMaps.put(tid.toString(), ykbdtos);
		}
		return ykbMaps;
	}

	public Map getJXBByXY(int x, int y, ObjectId gradeId, ObjectId xqid, ObjectId schoolId,Integer week) {
		List<N33_XKBDTO> xkbdtos = new ArrayList<N33_XKBDTO>();
		Map map = new HashMap();
		Map<String, List<N33_ZKBDTO_Temp>> ykbdtoMaps = new HashMap<String, List<N33_ZKBDTO_Temp>>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		Map<ObjectId, ObjectId> jxbClrIdMap = new HashMap<ObjectId, ObjectId>();
		List<N33_ZKBEntry_Temp> ykbEntryList = zkb_tempDao.getYKBEntrysList(xqid, schoolId, gradeId,week);
		List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
		if(ykbEntryList.size() == 0){
			map.put("jxbdtos", jxbdtos);
			map.put("teakbs", ykbdtoMaps);
			map.put("teas", xkbdtos);
			return map;
		}
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_ZKBEntry_Temp entry : ykbEntryList) {
				if (x == entry.getX() && y == entry.getY()) {
					if (entry.getJxbId() != null) {
						jxbIds.add(entry.getJxbId());
						jxbClrIdMap.put(entry.getJxbId(), entry.getClassroomId());
					}
					if (entry.getNJxbId() != null) {
						jxbIds.add(entry.getNJxbId());
						jxbClrIdMap.put(entry.getNJxbId(), entry.getClassroomId());
					}
				}
			}
		}

		List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBListByIds(jxbIds);
		List<ObjectId> tagIds = new ArrayList<ObjectId>();
		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
		List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(ykbEntryList.get(0).getCId());
		Map<ObjectId, N33_ClassroomEntry> classroomMap = classroomService.getRoomListMapBySchoolId(schoolId, ykbEntryList.get(0).getCId());
		if (jxbEntryList != null && jxbEntryList.size() != 0) {
			for (N33_JXBEntry entry : jxbEntryList) {
				if (entry.getType() == 4) {
					for (N33_ZhuanXiangEntry zxentry : zhuanXiangEntryList) {
						if (entry.getID().equals(zxentry.getJxbId()) && zxentry.getTeaId() != null) {
							teacherIds.add(zxentry.getTeaId());
						}
					}
				} else {
					tagIds.addAll(entry.getTagIds());
					if (entry.getTercherId() != null) {
						teacherIds.add(entry.getTercherId());
					}
				}

			}
			Map<ObjectId, N33_StudentTagEntry> studentTagEntryMap = studentTagDao.getTagListByIdsMap(tagIds);
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, ykbEntryList.get(0).getCId());
			for (N33_JXBEntry entry : jxbEntryList) {
				N33_JXBDTO jxbdto = new N33_JXBDTO(entry);
				List<ObjectId> tags = entry.getTagIds();
				String tag = "";
				if (tags != null && tags.size() != 0) {
					for (ObjectId id : tags) {
						tag += studentTagEntryMap.get(id) != null ? studentTagEntryMap.get(id).getName() : "" + ",";
					}
				}
				jxbdto.setClassroomName("");
				if (jxbClrIdMap.get(entry.getID()) != null) {
					jxbdto.setClassroomName(classroomMap.get(jxbClrIdMap.get(entry.getID())).getRoomName());
				}
				jxbdto.setTeacherName(teaEntryMap.get(entry.getTercherId()) != null ? teaEntryMap.get(entry.getTercherId()).getUserName() : "");
				jxbdto.setRemarks(tag);
				jxbdtos.add(jxbdto);
			}
			ykbdtoMaps = getYKBbyTeacherIds(teacherIds, ykbEntryList.get(0).getCId(), schoolId,week);

			if (teacherIds != null && teacherIds.size() != 0) {
				List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), ykbEntryList.get(0).getCId());
				for (ObjectId tid : teacherIds) {
					boolean flag = true;
					for (N33_XKBDTO xkbdto : xkbdtos) {
						if(tid.toString().equals(xkbdto.getTeacherId())){
							flag = false;
							break;
						}
					}
					if(flag){
						N33_XKBDTO dto = new N33_XKBDTO();
						dto.setTeacherName(teaEntryMap.get(tid).getUserName());
						dto.setTeacherId(tid.toString());
						dto.setCourseRangeDTOList(courseRangeDTOs);
						xkbdtos.add(dto);
					}
				}
			}
		}

		map.put("jxbdtos", jxbdtos);
		map.put("teakbs", ykbdtoMaps);
		map.put("teas", xkbdtos);
		return map;
	}

	/**撤销周课表(临时)
	 * @param zkbId
	 */
	public void undoZKB_Temp(String zkbId,Integer type) {
		N33_ZKBEntry_Temp zkbEntry_temp = zkb_tempDao.getN33_ZKB_TempById(new ObjectId(zkbId));
		if(type == 2){
			N33_RemovedXY removedXY = removedXYDao.findEntryByXYAndClassRoom(zkbEntry_temp.getSchoolId(),zkbEntry_temp.getTermId(),zkbEntry_temp.getClassroomId(),zkbEntry_temp.getX(), zkbEntry_temp.getY());
			if(removedXY == null){
				removedXY = new N33_RemovedXY(zkbEntry_temp.getClassroomId(), zkbEntry_temp.getX(), zkbEntry_temp.getY(),zkbEntry_temp.getTermId(),zkbEntry_temp.getSchoolId());
				removedXYDao.addEntry(removedXY);
			}
		}
		Integer week = zkbEntry_temp.getWeek();
		Integer count = week % 2;
		if (zkbEntry_temp.getType() == 4) {
			List<N33_ZKBEntry_Temp> zkbEntry_temps = zkb_tempDao.getZKB_TempByjxbId(zkbEntry_temp.getJxbId(), zkbEntry_temp.getX(), zkbEntry_temp.getY());
			if (zkbEntry_temps != null && zkbEntry_temps.size() != 0) {
				for (N33_ZKBEntry_Temp zkbEntry_temp1 : zkbEntry_temps) {
					N33_ZKBEntry_Temp zkbEntry_temp2 = new N33_ZKBEntry_Temp(zkbEntry_temp1.getX(), zkbEntry_temp1.getY(), zkbEntry_temp1.getClassroomId(), zkbEntry_temp1.getSchoolId(), zkbEntry_temp1.getTermId(),0,zkbEntry_temp1.getCId(), zkbEntry_temp1.getWeek(),zkbEntry_temp.getUserId(),zkbEntry_temp1.getTime());
					zkbEntry_temp2.setID(zkbEntry_temp1.getID());
					zkb_tempDao.updateN33_ZKB_Temp(zkbEntry_temp2);
				}
				N33_JXBKS_TempEntry jxbks_tempEntry = jxbksTempDao.getN33_JXBKS_TempByjxbId(zkbEntry_temp.getJxbId(),zkbEntry_temp.getWeek(),zkbEntry_temp.getTermId(),type);
				if(jxbks_tempEntry == null){
					N33_JXBEntry jxbEntry = jxbDao.getJXBById(zkbEntry_temp.getJxbId());
					N33_JXBKS_TempEntry jxbks_tempEntry1 = new N33_JXBKS_TempEntry(jxbEntry.getID(),jxbEntry.getSubjectId(),jxbEntry.getGradeId(),jxbEntry.getSchoolId(),zkbEntry_temp.getTermId(),zkbEntry_temp.getWeek(),1,type);
					jxbksTempDao.addN33_JXBKS_TempEntry(jxbks_tempEntry1);
				}else{
					jxbks_tempEntry.setKs(jxbks_tempEntry.getKs() + 1);
					jxbksTempDao.updateN33_JXBks_Temp(jxbks_tempEntry);
				}
			}
		} else {
			N33_ZKBEntry_Temp zkbEntry_temp2 = new N33_ZKBEntry_Temp(zkbEntry_temp.getX(), zkbEntry_temp.getY(), zkbEntry_temp.getClassroomId(), zkbEntry_temp.getSchoolId(), zkbEntry_temp.getTermId(),0,zkbEntry_temp.getCId(), zkbEntry_temp.getWeek(),zkbEntry_temp.getUserId(),zkbEntry_temp.getTime());
			zkbEntry_temp2.setID(zkbEntry_temp.getID());
			zkb_tempDao.updateN33_ZKB_Temp(zkbEntry_temp2);
			if(zkbEntry_temp.getType() == 6){
				N33_JXBKS_TempEntry jxbks_tempEntry = null;
				N33_JXBEntry jxbEntry = null;
				if(count == 0){
					jxbks_tempEntry = jxbksTempDao.getN33_JXBKS_TempByjxbId(zkbEntry_temp.getNJxbId(),zkbEntry_temp.getWeek(),zkbEntry_temp.getTermId(),type);
					jxbEntry = jxbDao.getJXBById(zkbEntry_temp.getNJxbId());
				}else{
					jxbks_tempEntry = jxbksTempDao.getN33_JXBKS_TempByjxbId(zkbEntry_temp.getJxbId(),zkbEntry_temp.getWeek(),zkbEntry_temp.getTermId(),type);
					jxbEntry = jxbDao.getJXBById(zkbEntry_temp.getJxbId());
				}
				if(jxbks_tempEntry == null){
					N33_JXBKS_TempEntry jxbks_tempEntry1 = new N33_JXBKS_TempEntry(jxbEntry.getID(),jxbEntry.getSubjectId(),jxbEntry.getGradeId(),jxbEntry.getSchoolId(),zkbEntry_temp.getTermId(),zkbEntry_temp.getWeek(),1,type);
					jxbksTempDao.addN33_JXBKS_TempEntry(jxbks_tempEntry1);
				}else{
					jxbks_tempEntry.setKs(jxbks_tempEntry.getKs() + 1);
					jxbksTempDao.updateN33_JXBks_Temp(jxbks_tempEntry);
				}
			}else{
				N33_JXBKS_TempEntry jxbks_tempEntry = jxbksTempDao.getN33_JXBKS_TempByjxbId(zkbEntry_temp.getJxbId(),zkbEntry_temp.getWeek(),zkbEntry_temp.getTermId(),type);
				N33_JXBEntry jxbEntry = jxbDao.getJXBById(zkbEntry_temp.getJxbId());
				if(jxbks_tempEntry == null){
					N33_JXBKS_TempEntry jxbks_tempEntry1 = new N33_JXBKS_TempEntry(jxbEntry.getID(),jxbEntry.getSubjectId(),jxbEntry.getGradeId(),jxbEntry.getSchoolId(),zkbEntry_temp.getTermId(),zkbEntry_temp.getWeek(),1,type);
					jxbksTempDao.addN33_JXBKS_TempEntry(jxbks_tempEntry1);
				}else{
					jxbks_tempEntry.setKs(jxbks_tempEntry.getKs() + 1);
					jxbksTempDao.updateN33_JXBks_Temp(jxbks_tempEntry);
				}
			}
		}
	}

	/**
	 * 查询待调课的教学班
	 * @param week
	 * @param classRoomId
	 * @param gradeId
	 * @param sid
	 * @return
	 */
	public List<N33_JXBDTO> getJXBList(Integer week,ObjectId classRoomId,ObjectId gradeId,ObjectId sid,ObjectId zhenXqid,Integer type){
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(zhenXqid);
		List<ObjectId> ciIds = new ArrayList<ObjectId>();
		for (TermEntry.PaiKeTimes paiKeTimes : termEntry.getPaiKeTimes()) {
			if(paiKeTimes.getIr() == 0){
				ciIds.add(paiKeTimes.getID());
			}
		}
		List<N33_JXBDTO> jxbdtoList = new ArrayList<N33_JXBDTO>();
		Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeacherMapBySid(sid);
		List<N33_JXBKS_TempEntry> jxbks_tempEntries = jxbksTempDao.getN33_JXBks_TempByWeek(gradeId,sid,week,zhenXqid,type);
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapsByCiIds(ciIds);
		for (N33_JXBKS_TempEntry jxbks_tempEntry : jxbks_tempEntries) {
			if(jxbEntryMap.get(jxbks_tempEntry.getJxbId()).getType() == 4){
				List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbks_tempEntry.getJxbId(),jxbEntryMap.get(jxbks_tempEntry.getJxbId()).getTermId());
				for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
					if(zhuanXiangEntry.getRoomId().equals(classRoomId)){
						N33_JXBDTO jxbdto = new N33_JXBDTO(jxbEntryMap.get(jxbks_tempEntry.getJxbId()));
						jxbdto.setTkKs(jxbks_tempEntry.getKs());
						jxbdto.setStudentCount(jxbEntryMap.get(jxbks_tempEntry.getJxbId()).getStudentIds().size());
						jxbdto.setTeacherName(teaEntryMap.get(zhuanXiangEntry.getTeaId()).getUserName());
						jxbdtoList.add(jxbdto);
						break;
					}
				}
			}else{
				if(jxbEntryMap.get(jxbks_tempEntry.getJxbId()) != null && jxbEntryMap.get(jxbks_tempEntry.getJxbId()).getClassroomId().equals(classRoomId)){
					N33_JXBDTO jxbdto = new N33_JXBDTO(jxbEntryMap.get(jxbks_tempEntry.getJxbId()));
					jxbdto.setTkKs(jxbks_tempEntry.getKs());
					jxbdto.setStudentCount(jxbEntryMap.get(jxbks_tempEntry.getJxbId()).getStudentIds().size());
					jxbdto.setTeacherName(teaEntryMap.get(jxbEntryMap.get(jxbks_tempEntry.getJxbId()).getTercherId()).getUserName());
					jxbdtoList.add(jxbdto);
				}
			}

		}
		return jxbdtoList;
	}

	public boolean judgeIsCanSave(ObjectId termId,ObjectId sid,Integer week,Integer type){
		List<N33_JXBKS_TempEntry> jxbks_tempEntries = jxbksTempDao.getN33_JXBks_TempByWeek(sid,week,termId,type);
		if(jxbks_tempEntries != null && jxbks_tempEntries.size() > 0){
			return false;
		}else{
			return true;
		}
	}



	/**
	 * @param zhenXqid
	 * @param jxbId
	 * @param classroomId
	 * @param schoolId
	 * @return
	 */
	public Map getConflictedSettledJXBByRoomId(ObjectId zhenXqid, String jxbId, String classroomId, ObjectId schoolId,Integer week) {
		//用于查询老师事务
		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
		Map map = new HashMap();
		List<String> xys = new ArrayList<String>();
		Map<String, List<String>> xyMesg = new HashMap<String, List<String>>();
		//查询该老师所带其他年级的固定事项
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
		ObjectId xqId = jxbEntry.getTermId();
		int acClassType = 0;
		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId,jxbEntry.getGradeId(),xqId,1);
		if(null!=turnOff){
			acClassType = turnOff.getAcClass();
		}
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapByTermId(xqId, acClassType);
		List<N33_GDSXDTO> gdsxdto = new ArrayList<N33_GDSXDTO>();
		if (jxbEntry.getType() == 4) {
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(new ObjectId(jxbId), xqId);
			//List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, jxbEntry.getGradeId());
			for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
				ObjectId teaId = zhuanXiangEntry.getTeaId();
				teacherIds.add(teaId);
				N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(teaId, xqId);
				if (teaEntry != null) {
					List<ObjectId> teaGradeIds = teaEntry.getGradeList();
					if (teaGradeIds != null && teaGradeIds.size() > 0) {
						for (ObjectId ids : teaGradeIds) {
							List<N33_GDSXDTO> n33_gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, ids);
							if (!ids.toString().equals(jxbEntry.getGradeId().toString())) {
								for (N33_GDSXDTO gdsxdto2 : n33_gdsxdtos) {
									boolean flag = true;
									for (N33_GDSXDTO gdsxdto1 : gdsxdto) {
										if (gdsxdto1.getX().intValue() == gdsxdto2.getX().intValue() && gdsxdto1.getY().intValue() == gdsxdto2.getY().intValue() && gdsxdto1.getGradeId().equals(gdsxdto2.getGradeId()) && gdsxdto1.getTermId().equals(gdsxdto2.getTermId())) {
											flag = false;
										}
									}
									if(flag){
										gdsxdto.add(gdsxdto2);
									}
								}
							}
						}
					}
				}
			}
		} else if (jxbEntry.getType() == 6) {
			//List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, jxbEntry.getGradeId());
			List<ObjectId> teaIds = new ArrayList<ObjectId>();
			if (jxbEntry.getTercherId() != null) {
				teaIds.add(jxbEntry.getTercherId());
				teacherIds.add(jxbEntry.getTercherId());
			}
			N33_JXBEntry NjxbEntry = jxbDao.getJXBById(jxbEntry.getRelativeId());
			if (NjxbEntry.getTercherId() != null) {
				teaIds.add(NjxbEntry.getTercherId());
				teacherIds.add(NjxbEntry.getTercherId());
			}
			for (ObjectId teaId : teaIds) {
				N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(teaId, xqId);
				if (teaEntry != null) {
					List<ObjectId> teaGradeIds = teaEntry.getGradeList();
					if (teaGradeIds != null && teaGradeIds.size() > 0) {
						for (ObjectId ids : teaGradeIds) {
							List<N33_GDSXDTO> n33_gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, ids);
							if (!ids.toString().equals(jxbEntry.getGradeId().toString())) {
								for (N33_GDSXDTO gdsxdto2 : n33_gdsxdtos) {
									boolean flag = true;
									for (N33_GDSXDTO gdsxdto1 : gdsxdto) {
										if (gdsxdto1.getX().intValue() == gdsxdto2.getX().intValue() && gdsxdto1.getY().intValue() == gdsxdto2.getY().intValue() && gdsxdto1.getGradeId().equals(gdsxdto2.getGradeId()) && gdsxdto1.getTermId().equals(gdsxdto2.getTermId())) {
											flag = false;
										}
									}
									if(flag){
										gdsxdto.add(gdsxdto2);
									}
								}
							}
						}
					}
				}
			}
		} else {
			N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(jxbEntry.getTercherId(), xqId);
			teacherIds.add(jxbEntry.getTercherId());
			if (teaEntry != null) {
				List<ObjectId> teaGradeIds = teaEntry.getGradeList();
				if (teaGradeIds != null && teaGradeIds.size() > 0) {
					for (ObjectId ids : teaGradeIds) {
						List<N33_GDSXDTO> n33_gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, ids);
						if (!ids.toString().equals(jxbEntry.getGradeId().toString())) {
							for (N33_GDSXDTO gdsxdto2 : n33_gdsxdtos) {
								boolean flag = true;
								for (N33_GDSXDTO gdsxdto1 : gdsxdto) {
									if (gdsxdto1.getX().intValue() == gdsxdto2.getX().intValue() && gdsxdto1.getY().intValue() == gdsxdto2.getY().intValue() && gdsxdto1.getGradeId().equals(gdsxdto2.getGradeId()) && gdsxdto1.getTermId().equals(gdsxdto2.getTermId())) {
										flag = false;
									}
								}
								if(flag){
									gdsxdto.add(gdsxdto2);
								}
							}
						}

					}
				}
			}
		}

		if (jxbEntry.getType() != 1 && jxbEntry.getType() != 2) {
			List<ZouBanTimeDTO> zouBanTimeDTOs = null;
			if((turnOff != null && turnOff.getStatus() != 0) || turnOff == null){
				zouBanTimeDTOs = courseRangeService.getZouBanTimeByType(schoolId, xqId, jxbEntry.getGradeId(), 0);
			}else{
				zouBanTimeDTOs = new ArrayList<ZouBanTimeDTO>();
			}
			if (zouBanTimeDTOs != null && zouBanTimeDTOs.size() != 0) {
				for (ZouBanTimeDTO dto : zouBanTimeDTOs) {
					xys.add((dto.getX() - 1) + "" + (dto.getY() - 1));
					if (xyMesg.get((dto.getX() - 1) + "" + (dto.getY() - 1)) != null) {
						List<String> stringList = xyMesg.get((dto.getX() - 1) + "" + (dto.getY() - 1));
						boolean flag = true;
						for (String s : stringList) {
							if ("固定走班格".equals(s)) {
								flag = false;
							}
						}
						if (flag) {
							stringList.add("固定走班格");
							xyMesg.put((dto.getX() - 1) + "" + (dto.getY() - 1), stringList);
						}
					} else {
						List<String> stringList = new ArrayList<String>();
						stringList.add("固定走班格");
						xyMesg.put((dto.getX() - 1) + "" + (dto.getY() - 1), stringList);
					}
					//xyMesg.put((dto.getX() - 1) + "" + (dto.getY() - 1), "固定走班格");
				}
			}
		}
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		jxbIds.add(jxbEntry.getID());
		if (jxbEntry.getDanOrShuang() != 0) {
			jxbIds.add(jxbEntry.getRelativeId());
		}
		Map<ObjectId, String> jxbCTMsg = new HashMap<ObjectId, String>();
		//教学班教学班之间的冲突
		List<N33_JXBCTEntry> jxbctEntrys = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, xqId);
		List<N33_ZKBDTO_Temp> ykbdtos = new ArrayList<N33_ZKBDTO_Temp>();
		if (jxbctEntrys != null && jxbctEntrys.size() != 0) {
			jxbIds = MongoUtils.getFieldObjectIDs(jxbctEntrys, "ojxbId");
			for (N33_JXBCTEntry jxbctEntry : jxbctEntrys) {
				String msg = "";
				if (jxbctEntry.getCtType() == 1) {
					msg = "学生冲突";
				} else if (jxbctEntry.getCtType() == 2) {
					msg = "教师冲突";
				} else if (jxbctEntry.getCtType() == 3) {
					msg = "场地冲突";
				}
				jxbCTMsg.put(jxbctEntry.getOjxbId(), msg + ":" + jxbEntryMap.get(jxbctEntry.getOjxbId()).getName());
			}
		}
		List<N33_ZKBEntry_Temp> ykbEntryList = zkb_tempDao.getZKB_TempEntrysByJXBIdsOrNJxbIds(xqId, jxbIds, schoolId,week);
		List<N33_ZKBEntry_Temp> ykbEntries = zkb_tempDao.getZKB_TempEntrysList(xqId, jxbEntry.getSchoolId(),week);

		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_ZKBEntry_Temp ykbEntry : ykbEntryList) {
				if(ykbEntry.getType() != 6){
					xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
					if (xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()) != null && xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()).size() != 0) {
						List<String> stringList = xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY());
						boolean flag = true;
						for (String s : stringList) {
							if (s.equals(jxbCTMsg.get(ykbEntry.getJxbId()))) {
								flag = false;
							}
						}
						if (flag) {
							if(jxbCTMsg.get(ykbEntry.getJxbId()) != null){
								stringList.add(jxbCTMsg.get(ykbEntry.getJxbId()));
							}
							xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
						}
						if(ykbEntry.getNJxbId() != null){
							boolean flag1 = true;
							for (String s : stringList) {
								if (s.equals(jxbCTMsg.get(ykbEntry.getNJxbId()))) {
									flag1 = false;
								}
							}
							if (flag1) {
								if(jxbCTMsg.get(ykbEntry.getNJxbId()) != null){
									stringList.add(jxbCTMsg.get(ykbEntry.getNJxbId()));
								}
								xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
							}
						}

					} else {
						List<String> stringList = new ArrayList<String>();
						if(jxbCTMsg.get(ykbEntry.getJxbId()) != null){
							stringList.add(jxbCTMsg.get(ykbEntry.getJxbId()));
							xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
						}
						if(jxbCTMsg.get(ykbEntry.getNJxbId()) != null){
							if((stringList.size() > 0 && !stringList.get(0).equals(jxbCTMsg.get(ykbEntry.getNJxbId()))) || stringList.size() == 0){
								stringList.add(jxbCTMsg.get(ykbEntry.getNJxbId()));
								xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
							}
						}
					}
				}
				//xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), jxbCTMsg.get(ykbEntry.getJxbId()));
			}
		}

		List<ObjectId> allJxbIds = new ArrayList<ObjectId>();
		if (ykbEntries != null && ykbEntries.size() != 0) {
			for (N33_ZKBEntry_Temp ykbEntry : ykbEntries) {
				if(jxbEntry.getType() == 6){
					N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(jxbEntry.getRelativeId());
					if ((ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry.getTercherId()) && jxbEntry.getDanOrShuang() == 1)
							|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry.getTercherId()) && jxbEntry.getDanOrShuang() == 2)
							|| (ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 1)
							|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 2)) {
						xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
						if (xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()) != null) {
							List<String> stringList = xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY());
							boolean flag = true;
							for (String s : stringList) {
								int a = s.indexOf(":");
								String str = s.substring(0,a);
								if (str.equals("教师冲突")) {
									flag = false;
								}
							}
							if (flag) {
								stringList.add("教师冲突" + ":" + jxbEntryMap.get(ykbEntry.getJxbId()).getName());
								xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
							}
						} else {
							List<String> stringList = new ArrayList<String>();
							stringList.add("教师冲突" + ":" + jxbEntryMap.get(ykbEntry.getJxbId()).getName());
							xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
						}
						//xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "教师冲突");
					}
				}

				if (jxbEntry.getGradeId() != null && jxbEntry.getGradeId().equals(ykbEntry.getGradeId()) && ykbEntry.getJxbId() != null) {
					allJxbIds.add(ykbEntry.getJxbId());
				}
			}
		}

		//如果某一个格子放了等级考的学科，那么这个格子同时上课的其他教室必须放等级考的学科
		List<ObjectId> njxbs = new ArrayList<ObjectId>();
		List<ObjectId> njxbs2 = new ArrayList<ObjectId>();
		if((turnOff != null && turnOff.getStatus() != 0) || turnOff == null){
			if (allJxbIds != null && allJxbIds.size() != 0) {
				List<N33_JXBEntry> jxbEntries = jxbDao.getJXBListByIds(allJxbIds);
				for (N33_JXBEntry entry : jxbEntries) {
					if ((jxbEntry.getType() == 1 || jxbEntry.getType() == 2) && (entry.getType() != 1 && entry.getType() != 2)) {
						njxbs.add(entry.getID());
					} else if ((jxbEntry.getType() == 3 || jxbEntry.getType() == 4 || jxbEntry.getType() == 6) && (entry.getType() != 3 && entry.getType() != 4 && entry.getType() != 6)) {
						njxbs2.add(entry.getID());
					}
				}
				if (ykbEntries != null && ykbEntries.size() != 0 && ((njxbs != null && njxbs.size() != 0) || (njxbs2 != null && njxbs2.size() != 0))) {
					for (N33_ZKBEntry_Temp ykbEntry : ykbEntries) {
						if (jxbEntry.getGradeId() != null && jxbEntry.getGradeId().equals(ykbEntry.getGradeId()) && njxbs.contains(ykbEntry.getJxbId())) {
							xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
							if (xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()) != null) {
								List<String> stringList = xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY());
								boolean flag = true;
								for (String s : stringList) {
									if (s.equals("非走班格")) {
										flag = false;
									}
								}
								if (flag) {
									stringList.add("非走班格");
									xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
								}
							} else {
								List<String> stringList = new ArrayList<String>();
								stringList.add("非走班格");
								xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
							}

//						if (StringUtils.isEmpty(xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()))) {
//							xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "非走班格");
//						}
						}
						if (jxbEntry.getGradeId() != null && jxbEntry.getGradeId().equals(ykbEntry.getGradeId()) && njxbs2.contains(ykbEntry.getJxbId())) {
							xys.add(ykbEntry.getX() + "" + ykbEntry.getY());

							if (xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()) != null) {
								List<String> stringList = xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY());
								boolean flag = true;
								for (String s : stringList) {
									if (s.equals("走班格")) {
										flag = false;
									}
								}
								if (flag) {
									stringList.add("走班格");
									xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
								}
							} else {
								List<String> stringList = new ArrayList<String>();
								stringList.add("走班格");
								xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
							}
//						if (StringUtils.isEmpty(xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()))) {
//							xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "走班格");
//						}
						}
					}
				}
			}
		}
		if (ykbEntries != null && ykbEntries.size() != 0) {
			for (N33_ZKBEntry_Temp entry : ykbEntries) {
				N33_ZKBDTO_Temp dto = new N33_ZKBDTO_Temp(entry);
				dto.setCtString(xyMesg.get(entry.getX() + "" + entry.getY()));
				//dto.setRemarks(xyMesg.get(entry.getX() + "" + entry.getY()));
				if (entry.getClassroomId() != null && entry.getClassroomId().toString().equals(classroomId)) {
					if (xys.contains(entry.getX() + "" + entry.getY())) {
						ykbdtos.add(dto);
					} else if (entry.getJxbId() != null) {
						if(new ObjectId(jxbId).equals(entry.getJxbId())){
							List<String> stringList = new ArrayList<String>();
							stringList.add("同一个教学班");
							dto.setCtString(stringList);
						}
						ykbdtos.add(dto);
					}
				}
			}
		}
		TermEntry termEntry = termDao.getTermByTimeId(xqId);
		//事务
		List<N33_SWDTO> swdtos = swService.getSwByXqidAndUserIds(termEntry.getID(), teacherIds);
		map.put("jxbcts", ykbdtos);
		map.put("swcts", swdtos);
		map.put("gdsxs", gdsxdto);
		return map;
	}

	/**
	 * 查询所有走班时间  0或1
	 */
	public List<ZouBanTimeDTO> getZouBanTime(ObjectId sid, ObjectId jxbId, ObjectId gid){
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		List<ZouBanTimeDTO> dtoList = new ArrayList<ZouBanTimeDTO>();
		List<ZouBanTimeEntry> list = timeDao.getZouBanTimeList(sid,jxbEntry.getTermId(),gid);
		for (ZouBanTimeEntry entry:list) {
			ZouBanTimeDTO dto = new ZouBanTimeDTO(entry);
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * 课表插课
	 *
	 * @param zkbId
	 * @param jxbId
	 */
	public void saveZKB_Temp(String zkbId, String jxbId,Integer week,Integer type) {
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
		N33_ZKBEntry_Temp zkbEntry_temp = zkb_tempDao.getN33_ZKBEntry_TempById(new ObjectId(zkbId));
		if(type == 2){
			N33_RemovedXY removedXY = removedXYDao.findEntryByXYAndClassRoom(zkbEntry_temp.getSchoolId(),zkbEntry_temp.getTermId(),zkbEntry_temp.getClassroomId(),zkbEntry_temp.getX(), zkbEntry_temp.getY());
			if(removedXY == null){
				removedXY = new N33_RemovedXY(zkbEntry_temp.getClassroomId(), zkbEntry_temp.getX(), zkbEntry_temp.getY(),zkbEntry_temp.getTermId(),zkbEntry_temp.getSchoolId());
				removedXYDao.addEntry(removedXY);
			}
		}
		if (jxbEntry.getType() != 4) {
			if (jxbEntry.getDanOrShuang() != 0) {
				N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
				//ykbEntry.setJxbId(new ObjectId(jxbId));
				zkbEntry_temp.setNSubjectId(jxbEntry2.getSubjectId());
				zkbEntry_temp.setNTeacherId(jxbEntry2.getTercherId());
				zkbEntry_temp.setType(jxbEntry2.getType());
				zkbEntry_temp.setNJxbId(jxbEntry2.getID());
				if(jxbEntry.getDanOrShuang() == 1 && jxbEntry2.getDanOrShuang() == 2){
					zkbEntry_temp.setJxbId(new ObjectId(jxbId));
					zkbEntry_temp.setIsUse(Constant.ONE);
					zkbEntry_temp.setGradeId(jxbEntry.getGradeId());
					zkbEntry_temp.setSubjectId(jxbEntry.getSubjectId());
					zkbEntry_temp.setType(jxbEntry.getType());
					zkbEntry_temp.setTeacherId(jxbEntry.getTercherId());
					zkbEntry_temp.setNSubjectId(jxbEntry2.getSubjectId());
					zkbEntry_temp.setNTeacherId(jxbEntry2.getTercherId());
					zkbEntry_temp.setNJxbId(jxbEntry2.getID());
				}else if(jxbEntry.getDanOrShuang() == 2 && jxbEntry2.getDanOrShuang() == 1){
					zkbEntry_temp.setJxbId(jxbEntry2.getID());
					zkbEntry_temp.setIsUse(Constant.ONE);
					zkbEntry_temp.setGradeId(jxbEntry2.getGradeId());
					zkbEntry_temp.setSubjectId(jxbEntry2.getSubjectId());
					zkbEntry_temp.setType(jxbEntry2.getType());
					zkbEntry_temp.setTeacherId(jxbEntry2.getTercherId());
					zkbEntry_temp.setNSubjectId(jxbEntry.getSubjectId());
					zkbEntry_temp.setNTeacherId(jxbEntry.getTercherId());
					zkbEntry_temp.setNJxbId(jxbEntry.getID());
				}
			}
			N33_JXBKS_TempEntry jxbks_tempEntry = jxbksTempDao.getN33_JXBKS_TempByjxbId(new ObjectId(jxbId),week,zkbEntry_temp.getTermId(),type);
			Integer ksCount = jxbks_tempEntry.getKs();
			if(ksCount > 1){
				jxbks_tempEntry.setKs(ksCount - 1);
				jxbksTempDao.updateN33_JXBks_Temp(jxbks_tempEntry);
			}else{
				jxbksTempDao.removeZKBEntry_TempById(jxbks_tempEntry.getID());
			}


			if(jxbEntry.getDanOrShuang() == 0){
				zkbEntry_temp.setJxbId(new ObjectId(jxbId));
				zkbEntry_temp.setIsUse(Constant.ONE);
				zkbEntry_temp.setGradeId(jxbEntry.getGradeId());
				zkbEntry_temp.setSubjectId(jxbEntry.getSubjectId());
				zkbEntry_temp.setType(jxbEntry.getType());
				zkbEntry_temp.setTeacherId(jxbEntry.getTercherId());
			}
			zkb_tempDao.updateN33_ZKB_Temp(zkbEntry_temp);
		} else {
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(new ObjectId(jxbId), jxbEntry.getTermId());
			if (zhuanXiangEntryList != null && zhuanXiangEntryList.size() != 0) {
				List<ObjectId> classroomIds = MongoUtils.getFieldObjectIDs(zhuanXiangEntryList, "rid");
				Map<ObjectId, N33_ZKBEntry_Temp> zkbEntry_tempMap = zkb_tempDao.getJXB(jxbEntry.getTermId(), classroomIds, zkbEntry_temp.getX(), zkbEntry_temp.getY(), jxbEntry.getSchoolId(),week);
				for (N33_ZhuanXiangEntry e : zhuanXiangEntryList) {
					N33_ZKBEntry_Temp zkbEntryTemp = zkbEntry_tempMap.get(e.getRoomId());
					zkbEntryTemp.setJxbId(new ObjectId(jxbId));
					zkbEntryTemp.setIsUse(Constant.ONE);
					zkbEntryTemp.setGradeId(jxbEntry.getGradeId());
					zkbEntryTemp.setSubjectId(jxbEntry.getSubjectId());
					//ykb.setTeacherId(e.getTeaId());
					zkbEntryTemp.setType(jxbEntry.getType());
					zkb_tempDao.updateN33_ZKB_Temp(zkbEntryTemp);
				}
			}
			N33_JXBKS_TempEntry jxbks_tempEntry = jxbksTempDao.getN33_JXBKS_TempByjxbId(new ObjectId(jxbId),week,zkbEntry_temp.getTermId(),type);
			Integer ksCount = jxbks_tempEntry.getKs();
			if(ksCount > 1){
				jxbks_tempEntry.setKs(ksCount - 1);
				jxbksTempDao.updateN33_JXBks_Temp(jxbks_tempEntry);
			}else{
				jxbksTempDao.removeZKBEntry_TempById(jxbks_tempEntry.getID());
			}
		}
	}

	public void reloadKB_Temp(String termId,ObjectId sid,Integer week,Integer type){
		zkb_tempDao.removeZKBEntry_TempById(new ObjectId(termId),sid,week);
		if(type == 1){
			jxbksTempDao.removeZKBEntry_TempById(new ObjectId(termId),sid,week,type);
		}else{
			jxbksTempDao.removeZKBEntry_TempById(new ObjectId(termId),sid,week,type);
			removedXYDao.deleteByTermId(sid,new ObjectId(termId));
		}
	}

	public void initIsSubmit(ObjectId termId,ObjectId sid,Integer week,Integer tk){
		N33_Count count = countDao.findCountEntryByWeek(sid,termId,week,tk);
		if(count == null){
			count = new N33_Count(true,sid,termId,week,tk);
			countDao.addCountEntry(count);
		}
	}

	public Boolean getCount(ObjectId termId,ObjectId sid,Integer week,Integer tk){
		N33_Count count = countDao.findCountEntryByWeek(sid,termId,week,tk);
		if(count != null){
			return count.getSubmit();
		}else{
			return true;
		}
	}

	public void updateCount(ObjectId termId,ObjectId sid,Integer week,Integer tk,boolean submit){
		N33_Count count = countDao.findCountEntryByWeek(sid,termId,week,tk);
		if(count != null){
			count.setSubmit(submit);
			countDao.updateCountEntry(count);
		}
	}
}