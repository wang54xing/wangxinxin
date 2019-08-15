package com.fulaan.new33.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;









import com.db.new33.*;
import com.db.new33.isolate.*;
import com.db.new33.paike.*;
import com.db.user.UserDao;
import com.fulaan.new33.dto.isolate.*;
import com.fulaan.new33.dto.paike.*;
import com.fulaan.new33.service.isolate.*;
import com.fulaan.utils.LocalIp;
import com.fulaan.utils.RESTAPI.ta.notice.NoticeAPI;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.new33.*;
import com.pojo.new33.isolate.*;
import com.pojo.new33.paike.*;
import com.pojo.user.UserEntry;
import com.qiniu.api.net.Http;
import com.sys.constants.Constant;


import org.apache.axis2.databinding.types.xsd.DateTime;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pojo.utils.MongoUtils;
import com.sys.utils.DateTimeUtils;

import redis.clients.jedis.BinaryClient;

/**
 * Created by albin on 2018/3/20.
 */
@Service
public class N33_ZKBService extends BaseService {
	private N33_JXZDao jxzDao = new N33_JXZDao();
	private N33_YKBDao ykbDao = new N33_YKBDao();
	private N33_JXBDao jxbDao = new N33_JXBDao();
	private N33_ZKBDao zkbDao = new N33_ZKBDao();
	private N33_TeaDao teaDao = new N33_TeaDao();
	private N33_SWService swService = new N33_SWService();
	private N33_ClassroomDao classroomDao = new N33_ClassroomDao();
	private N33_TiaoKeShengQingDao tiaoKeShengQingDao = new N33_TiaoKeShengQingDao();
	private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();
	private TermDao termDao = new TermDao();
	private N33_DefaultTermDao defaultTermDao = new N33_DefaultTermDao();
	private N33_StudentTagDao studentTagDao = new N33_StudentTagDao();
	private N33_GradeWeekRangeDao n33_gradeWeekRangeDao = new N33_GradeWeekRangeDao();
	N33_TurnOffDao turnOffDao = new N33_TurnOffDao();
	@Autowired
	private N33_DefaultTermService defaultTermService;
	@Autowired
	private IsolateTermService termService;
	@Autowired
	private IsolateSubjectService subjectService;
	@Autowired
	private N33_ClassroomService classroomService;
	@Autowired
	private CourseRangeService courseRangeService;
	@Autowired
	private N33_NameKBService n33_nameKBService;

	@Autowired
    private N33_TurnOffService turnOffService;
	
	@Autowired
	private N33_GDSXService gdsxService;

	private N33_GDSXDao gdsxDao = new N33_GDSXDao();

	@Autowired
	private N33_FenBanInfoSetService n33_fenBanInfoSetService;

	private N33_FaBuLogDao faBuLogDao = new N33_FaBuLogDao();


	private N33_StudentDao studentDao = new N33_StudentDao();
	private N33_ZhuanXiangDao zhuanXiangDao = new N33_ZhuanXiangDao();
	private N33_ZIXIKEDao zixikeDao = new N33_ZIXIKEDao();
	private GradeDao gradeDao = new GradeDao();
	private N33_FaBuDao faBuDao = new N33_FaBuDao();
	@Autowired
	private IsolateClassService classService;
	@Autowired
	FaTongZhiService faTongZhiService;

	private N33_TkLogDao tkLogDao = new N33_TkLogDao();
	private SubjectDao subjectDao = new SubjectDao();

	private N33_SWDao swDao = new N33_SWDao();

	private UserDao userDao = new UserDao();

	@Autowired
	private PaiKeService paiKeService;

	private N33_DaKaDao daKaDao = new N33_DaKaDao();

	private CourseRangeDao dao= new CourseRangeDao();



	/**
	 * 获得当前时间处于某个周
	 *
	 * @param xqid
	 * @param sid
	 * @param time
	 * @return
	 */
	public Integer getWeekByTime(ObjectId xqid, ObjectId sid, long time) {
		N33_JXZEntry entry = jxzDao.getJXZByDate(xqid, sid, time);
		if (entry != null) {
			return entry.getSerial();
		}
		return 0;
	}

	public Map<String, Object> getStatus(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map<String, Object> status = (Map<String, Object>) session.getAttribute("fabuStas");
		if (status != null && status.get("st") != null && (Integer) status.get("st") == -1) {
			session.removeAttribute("status");
		}
		return status;
	}

	public Map<String, Object> getStatusDC(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map<String, Object> status = (Map<String, Object>) session.getAttribute("dckb");
		if (status != null && status.get("st") != null && (Integer) status.get("st") == -1) {
			session.removeAttribute("status");
		}
		return status;
	}

	/**
	 * 发布课表
	 *
	 * @param xqid
	 * @param sid
	 * @return
	 */
	public void CopySourceKB2WeekKB(ObjectId xqid, ObjectId sid, ObjectId userId, Integer staWeek, Integer endWeek, ObjectId gid, ObjectId cid, HttpServletRequest request) {
		Integer staWeek1 = staWeek;
		Integer endWeek1 = endWeek;
		HttpSession session = request.getSession();
		Map<String, Object> status = new HashMap<String, Object>();
		status.put("st", 999);
		session.setAttribute("fabuStas", status);
		List<N33_ClassroomDTO> classroomDTOs = classroomService.getRoomEntryListByXq(cid, sid,1);
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(sid.toString(), cid);
		List<ObjectId> classroomIds = new ArrayList<ObjectId>();
		if (classroomIds == null || classroomIds.size() == 0) {
			if (classroomDTOs != null && classroomDTOs.size() != 0) {
				for (N33_ClassroomDTO dto : classroomDTOs) {
					classroomIds.add(new ObjectId(dto.getRoomId()));
				}
			}
		}
		status.put("st", 999);
		String KBName = n33_nameKBService.getTypeName("*") + "发布";
		n33_nameKBService.addNameKB(cid, sid, KBName, gid, userId);
		//获得原课表
		List<N33_YKBEntry> ykbEntries = ykbDao.getYKBEntrysListOrGidNull(cid, sid, gid);
		//封装Dto
		List<N33_ZKBEntry> dtos = getEntrys(ykbEntries, staWeek, endWeek, userId, cid, xqid);

		Collections.sort(dtos, new Comparator<N33_ZKBEntry>() {
			@Override
			public int compare(N33_ZKBEntry o1, N33_ZKBEntry o2) {
				return o1.getWeek() - o2.getWeek();
			}
		});
		N33_GradeWeekRangeEntry gradeWeekRangeEntry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(cid,sid,gid);
		N33_FaBuEntry faBuEntry = faBuDao.getN33_FaBuEntryByCiId(cid);
		for (; staWeek <= endWeek; staWeek++) {
			if (zkbDao.getN33_ZKBByWeekGrade(cid, xqid, gid, staWeek).size() > 0) {
				status.put("st", staWeek);
				zkbDao.deleteKBByXqidByXueQiAndGidNull(sid,xqid,staWeek,gid);
//				zkbDao.updateN33_ZKBC(gid, xqid, staWeek);
				List<N33_ZKBEntry> zkbEntryList = new ArrayList<N33_ZKBEntry>();
				for (N33_ZKBEntry entry : dtos) {
					if (entry.getWeek() == staWeek) {
						status.put("st", entry.getWeek());
						zkbEntryList.add(entry);
//						zkbDao.updateN33_ZKBC(entry);
					}
				}
				zkbDao.addN33_ZKBEntryList(zkbEntryList);
			} else {
				List<N33_ZKBEntry> zkbEntryList = new ArrayList<N33_ZKBEntry>();
				status.put("st", staWeek);
				zkbDao.deleteKBByXqidByXueQiAndGidNull(sid,xqid,staWeek,gid);
//				zkbDao.updateN33_ZKBC(gid, xqid, staWeek);
				for (N33_ZKBEntry entry : dtos) {
					if (entry.getWeek() == staWeek) {
						status.put("st", entry.getWeek());
						zkbEntryList.add(entry);
//						zkbDao.updateN33_ZKBC(entry);
					}
				}
				zkbDao.addN33_ZKBEntryList(zkbEntryList);
//				checkKS(userId, sid, xqid, classroomIds, courseRangeDTOs.size(), staWeek, staWeek, cid,gid);
			}
		}
		checkKS(userId, sid, xqid, classroomIds, courseRangeDTOs.size(), staWeek1, endWeek1, cid,gid);
//        if (zkbDao.getN33_ZKBByWeekGrade(cid, xqid, gid).size() > 0) {
//            status.put("st", 1);
//            for (; staWeek <= endWeek; staWeek++) {
//                zkbDao.updateN33_ZKBC(gid, xqid, staWeek);
//            }
//            for (N33_ZKBEntry entry : dtos) {
//                status.put("st", entry.getWeek());
//                zkbDao.updateN33_ZKBC(entry);
//            }
//        } else {
//            status.put("st", 1);
//            checkKS(userId, sid, xqid, classroomIds, courseRangeDTOs.size(), staWeek, endWeek, cid);
//            for (; staWeek <= endWeek; staWeek++) {
//                zkbDao.updateN33_ZKBC(gid, xqid, staWeek);
//            }
//            for (N33_ZKBEntry entry : dtos) {
//                status.put("st", entry.getWeek());
//                zkbDao.updateN33_ZKBC(entry);
//            }
//        }
		if (faBuEntry == null) {
			N33_FaBuEntry faBuEntry1 = new N33_FaBuEntry(cid, sid, xqid);
			faBuDao.addN33_FaBuEntry(faBuEntry1);
		}
		status.put("st", -1);
		faBuLogDao.addN33_FaBuLogEntry(new N33_FaBuLogEntry(sid, userId, gid, xqid, cid, staWeek1, endWeek1, 1));



		//发通知
		Grade grade = gradeDao.getIsolateGrADEEntrysByGid(cid, sid, gid);
		List<String> userIds = new ArrayList<String>();
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(cid, sid, gid);
		List<StudentEntry> studentEntryList = studentDao.getStudentByXqidAndGradeId(gid, cid);
		for (StudentEntry entry : studentEntryList) {
			userIds.add(entry.getUserId().toString());
		}
		for (N33_TeaEntry entry : teaEntryMap.values()) {
			userIds.add(entry.getUserId().toString());
		}
		faTongZhiService.faTongZhi(grade.getName() + "课表发布", sid.toString(), userId.toString(), userIds);
	}

	private void checkKS(ObjectId userId, ObjectId schoolId, ObjectId termId, List<ObjectId> classroomIds, int count, Integer staWeek, Integer endWeek, ObjectId cid,ObjectId gradeId) {
		N33_GradeWeekRangeEntry gradeWeekRangeEntry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(cid, schoolId, gradeId);
		for (; staWeek <= endWeek; staWeek++) {
			List<N33_ZKBEntry> zkbEntries = new ArrayList<N33_ZKBEntry>();
			Map<ObjectId,List<N33_ZKBEntry>> zkbMap = zkbDao.getN33_ZKBByWeekAndX(staWeek,6,termId,classroomIds);
			List<ObjectId> weekendZKB = new ArrayList<ObjectId>();
			for (ObjectId id : classroomIds) {
				if(!weekendZKB.contains(id)){
					for (int i = gradeWeekRangeEntry.getEnd(); i < 7; i++) {
						if(zkbMap.get(id).size() == 0){
							for (int j = 0; j < count; j++) {
								zkbEntries.add(new N33_ZKBEntry(i, j, id, schoolId, termId, staWeek, userId, null, null, null, cid));
							}
						}
					}
					weekendZKB.add(id);
				}
			}
			if (zkbEntries != null && zkbEntries.size() != 0) {
				zkbDao.addN33_ZKBEntryList(zkbEntries);
			}
		}
	}

//	private void checkKSSunDay(N33_GradeWeekRangeEntry gradeWeekRangeEntry,ObjectId userId, ObjectId schoolId, ObjectId termId, List<ObjectId> classroomIds, int count, Integer staWeek, Integer endWeek, ObjectId cid) {
//		Map<String, N33_ZKBEntry> ykbEntryHashMap = new HashMap<String, N33_ZKBEntry>();
//		for (; staWeek <= endWeek; staWeek++) {
//			List<N33_ZKBEntry> ykbEntryList = zkbDao.getYKBsByclassRoomIds(termId, schoolId, classroomIds, staWeek);
//			if (ykbEntryList != null && ykbEntryList.size() != 0) {
//				for (N33_ZKBEntry entry : ykbEntryList) {
//					ykbEntryHashMap.put(entry.getClassroomId().toString() + entry.getX() + entry.getY(), entry);
//				}
//			}
//			List<N33_ZKBEntry> ykbEntries = new ArrayList<N33_ZKBEntry>();
//			for (ObjectId id : classroomIds) {
//				for (int i = 5; i < gradeWeekRangeEntry.getEnd(); i++) {
//					for (int j = 0; j < count; j++) {
//						if (ykbEntryHashMap.get(id.toString() + i + j) == null) {
//							ykbEntries.add(new N33_ZKBEntry(i, j, id, schoolId, termId, staWeek, userId, null, null, null, cid));
//						}
//					}
//				}
//			}
//			if (ykbEntries != null && ykbEntries.size() != 0) {
//				zkbDao.addN33_ZKBEntryList(ykbEntries);
//			}
//		}
//	}

	public void removeKBByXqid(ObjectId xqid, ObjectId sid, Integer staWeek, Integer endWeek, ObjectId gid, ObjectId cid) {
		for (; staWeek <= endWeek; staWeek++) {
			zkbDao.deleteKBByXqidByXueQi(sid, xqid, staWeek, gid);
			faBuDao.removeN33_FaBuEntry(cid);
		}
	}

	public List<String> getCiIds(Integer staWeek, Integer endWeek, ObjectId gid, ObjectId xqid) {
		List<String> ciIds = new ArrayList<String>();
		for (; staWeek <= endWeek; staWeek++) {
			List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeek(xqid,gid,staWeek);
			if(zkbEntryList != null && zkbEntryList.size() > 0){
				if(!ciIds.contains(zkbEntryList.get(0).getCId().toString())){
					ciIds.add(zkbEntryList.get(0).getCId().toString());
				}
			}
		}
		return ciIds;
	}

	/**
	 * 封装DTO
	 *
	 * @param entry
	 * @param userId
	 * @return
	 */
	public List<N33_ZKBEntry> getEntrys(List<N33_YKBEntry> entry, Integer staWeek, Integer endWeek, ObjectId userId, ObjectId cid, ObjectId xqid) {
		//开始结束时间
		Integer st = staWeek;
		//发布时间
		Long time = System.currentTimeMillis();
		//封装Entry
		List<N33_ZKBEntry> zbkEntry = new ArrayList<N33_ZKBEntry>();
		for (N33_YKBEntry en : entry) {
			for (; st <= endWeek; st++) {
				N33_ZKBDTO dto = new N33_ZKBDTO(en, st, userId, time, cid, xqid);
				N33_ZKBEntry entry1 = dto.buildEntry();
				entry1.setType(en.getType());
				zbkEntry.add(entry1);
			}
			st = staWeek;
		}
		return zbkEntry;
	}

	public void daochu(HttpServletResponse response) {
		ObjectId schoolId = new ObjectId("5b9732ef48c5025c78ba1406");
		ObjectId xqid = new ObjectId("5b97516246d697466adcca6c");
		ObjectId ciId = new ObjectId("5b97516246d697466adcca6b");
		Integer week = 5;
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<N33_TeaEntry> teaEntries = teaDao.IsolateTeaListByNewCi(ciId);
		List<N33_TeaEntry> teaEntries1 = new ArrayList<N33_TeaEntry>();
		List<ObjectId> teaIds = new ArrayList<ObjectId>();
		for (N33_TeaEntry teaEntry : teaEntries) {
			if (!teaIds.contains(teaEntry.getUserId())) {
				teaIds.add(teaEntry.getUserId());
				teaEntries1.add(teaEntry);
			}
		}
		List<String> teaName = new ArrayList<String>();
		HSSFWorkbook wb = new HSSFWorkbook();
		int count10 = 0;
		for (N33_TeaEntry teacherE : teaEntries1) {
			count10++;
			System.out.println(count10);
			ObjectId teacherId = teacherE.getUserId();
			List<ObjectId> uids = new ArrayList<ObjectId>();
			uids.add(teacherE.getUserId());
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(uids);
			String userName1 = null;
			for (N33_TeaEntry teaEntry : teaEntryMap.values()) {
				userName1 = teaEntry.getUserName();
				if (teaName.contains(userName1)) {
					userName1 += "1";
					teaName.add(userName1);
				} else {
					teaName.add(userName1);
				}
			}
			List<Map<String, Object>> lists = termService.getPaikeTimesByTermId(xqid);
			List<ObjectId> cids = new ArrayList<ObjectId>();
			for (Map<String, Object> lt : lists) {
				String cid = (String) lt.get("ciId");
				cids.add(new ObjectId(cid));
			}

			//该次排课的所有年级
			Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(cids, schoolId);

			//周课表
			List<N33_ZKBEntry> entries = zkbDao.getJXB(xqid, teacherId, week);
			entries.addAll(zkbDao.getJXB(xqid, week));
			//学科列表 所有次的
			List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(cids, schoolId);
			//教学班列表 所有次的
			List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(cids, teacherId);
			jxbEntries.addAll(jxbDao.getZXJXBList(cids));
			//教室列表 所有次的
			List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, cids);

			//专项课Ids
			List<ObjectId> zxkIds = new ArrayList<ObjectId>();
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				zxkIds.add(jxbEntry.getID());
			}
			List<ObjectId> ods = new ArrayList<ObjectId>();
			for (N33_ZKBEntry jxbEntry : entries) {
				if (jxbEntry.getType() == 5) {
					ods.add(jxbEntry.getJxbId());
				}
			}
			Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(ods);
			List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
			Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
			List<ObjectId> jxbIds = new ArrayList<ObjectId>();
			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
				if (!jxbIds.contains(entry.getJxbId())) {
					jxbIds.add(entry.getJxbId());
				}
			}
			for (ObjectId objectId : jxbIds) {
				List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
				for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
					if (entry.getJxbId().toString().equals(objectId.toString())) {
						jis.add(entry);
					}
				}
				listMap.put(objectId, jis);
			}

			//课节
			List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(schoolId.toString(), xqid);

			Map<ObjectId, List<N33_ClassroomEntry>> classRoomList = new HashMap<ObjectId, List<N33_ClassroomEntry>>();
			Map<ObjectId, List<N33_KSDTO>> gradeSubjectCidMap = new HashMap<ObjectId, List<N33_KSDTO>>();
			Map<ObjectId, Map<ObjectId, N33_JXBEntry>> jxbEntryMap = new HashMap<ObjectId, Map<ObjectId, N33_JXBEntry>>();

			for (Map<String, Object> lt : lists) {
				String cid = (String) lt.get("ciId");
//        String cid="5ad859cf8fb25af5a4779221";
				//学科
				List<N33_KSDTO> subjectCid = new ArrayList<N33_KSDTO>();
				for (N33_KSDTO ksdto : gradeSubject) {
					if (ksdto.getXqid().equals(cid)) {
						subjectCid.add(ksdto);
					}
				}
				gradeSubjectCidMap.put(new ObjectId(cid), subjectCid);
				// 教学班
				Map<ObjectId, N33_JXBEntry> jxbEntryMap1 = new HashMap<ObjectId, N33_JXBEntry>();
				for (N33_JXBEntry jxbEntry : jxbEntries) {
					if (jxbEntry.getTermId().toString().equals(cid)) {
						jxbEntryMap1.put(jxbEntry.getID(), jxbEntry);
					}
				}
				jxbEntryMap.put(new ObjectId(cid), jxbEntryMap1);
				//教室
				List<N33_ClassroomEntry> jsList = new ArrayList<N33_ClassroomEntry>();
				for (N33_ClassroomEntry js : classRoomDTO) {
					if (js.getXQId().toString().equals(cid)) {
						jsList.add(js);
					}
				}
				classRoomList.put(new ObjectId(cid), jsList);
			}

			//TermEntry termEntry = termDao.getTermByTimeId(xqid);
			//教师事务
			List<N33_SWDTO> teaSwdtos = swService.getSwByXqidAndUserId(xqid, teacherId);

			//固定事务
			List<N33_SWDTO> gdSwdtos = swService.getGuDingShiWuBySchoolId(xqid, schoolId);

			Map<String, N33_ZKBDTO> zkbMap = new HashMap<String, N33_ZKBDTO>();
			for (N33_ZKBEntry entry : entries) {
				Integer count = week % 2;
				N33_ZKBDTO dto = new N33_ZKBDTO(entry);
				dto.setSwType(0);
				List<N33_KSDTO> ksDtos = gradeSubjectCidMap.get(entry.getCId());
				if (entry.getType() != 5) {
					for (N33_KSDTO ksdto : ksDtos) {
						//获取学科
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							dto.setSubjectName(ksdto.getSnm());
							break;
						}
					}
				}

				if (entry.getType() != 4 && entry.getType() != 5) {
					//教学班
					Map<ObjectId, N33_JXBEntry> jxbEntryMap2 = jxbEntryMap.get(entry.getCId());
					N33_JXBEntry entry1 = jxbEntryMap2.get(entry.getJxbId());
					if (entry1 != null) {
						if (StringUtils.isNotEmpty(entry1.getNickName())) {
							dto.setJxbName(entry1.getNickName());
						} else {
							dto.setJxbName(entry1.getName());
						}
						dto.setStudentCount(entry1.getStudentIds().size());
					}
					//教室
					List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
					for (N33_ClassroomEntry room : classRoomDTOList) {
						//获取教室
						if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
							dto.setRoomName(room.getRoomName());
							break;
						}
					}
					if (entry.getNJxbId() != null) {
						if (count == 0) {
							if (entry.getNTeacherId().toString().equals(teacherId.toString())) {
								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
							}
						} else {
							if (entry.getTeacherId().toString().equals(teacherId.toString())) {
								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
							}
						}
					} else {
						zkbMap.put(dto.getX() + "" + dto.getY(), dto);
					}
				} else {
					if (entry.getType() == 4) {
						List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
						for (N33_ZhuanXiangEntry entry1 : zhuanList) {
							if (entry1.getTeaId().toString().equals(teacherId.toString())) {
								dto.setJxbName(entry1.getName());
								dto.setStudentCount(entry1.getStudentId().size());
								List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
								for (N33_ClassroomEntry room : classRoomDTOList) {
									//获取教室
									if (room.getRoomId().toString().equals(entry1.getRoomId().toString())) {
										dto.setRoomName(room.getRoomName());
										break;
									}
								}
								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
							}
						}
					} else {
						N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
						dto.setJxbName(entry1.getName());
						dto.setStudentCount(entry1.getStudentIds().size());
						dto.setSubjectName("自习课");
						List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
						for (N33_ClassroomEntry room : classRoomDTOList) {
							//获取教室
							if (room.getRoomId().toString().equals(entry1.getClassroomId().toString())) {
								dto.setRoomName(room.getRoomName());
								break;
							}
						}
						zkbMap.put(dto.getX() + "" + dto.getY(), dto);
					}
				}
			}

			for (N33_SWDTO dto : teaSwdtos) {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setSwType(1);
				zkbdto.setSwDesc(dto.getDesc());
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			}

			for (N33_SWDTO dto : gdSwdtos) {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setSwType(1);
				zkbdto.setSwDesc(dto.getDesc());
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			}

			HSSFSheet sheet = wb.createSheet(userName1 + "课表");
			sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 7));
			sheet.setVerticallyCenter(true);
			sheet.setDisplayGridlines(false);

			HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
			cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

			//设置列宽
			sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
			sheet.setColumnWidth((short) 1, (short) 5500);
			sheet.setColumnWidth((short) 2, (short) 5500);
			sheet.setColumnWidth((short) 3, (short) 5500);
			sheet.setColumnWidth((short) 4, (short) 5500);
			sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
			sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
			sheet.setColumnWidth((short) 7, (short) 5500);

			HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
			headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
			headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
			headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
			headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
			headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			headerStyle1.setWrapText(true);

			HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
			headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
			headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
			headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
			headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
			headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
			headerStyle2.setWrapText(true);


			HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
			headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
			headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
			headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
			headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			headerStyle3.setWrapText(true);

			//为sheet1生成第一行，用于放表头信息
			HSSFRow row1 = sheet.createRow(0);
			row1.setHeightInPoints(40);
			HSSFCell cell1 = row1.createCell(0);
			cell1.setCellStyle(headerStyle1);
			cell1.setCellValue(termEntry.getXqName() + "  " + userName1 + "  课表");

			//为sheet1生成第二行，用于放表头信息
			HSSFRow row = sheet.createRow(1);
			row.setHeightInPoints(35);
			HSSFCell cell = row.createCell(0);
			cell.setCellStyle(headerStyle2);
			cell.setCellValue("课节 / 日");
			List<String> weekLists = new ArrayList<String>();
			weekLists.add("周一");
			weekLists.add("周二");
			weekLists.add("周三");
			weekLists.add("周四");
			weekLists.add("周五");
			weekLists.add("周六");
			weekLists.add("周日");
			for (int j = 0; j < weekLists.size(); j++) {
				cell = row.createCell(j + 1);
				cell.setCellStyle(headerStyle2);
				cell.setCellValue(weekLists.get(j));
			}

			int page = 1;
			if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
				for (int i = 0; i < courseRangeDTOs.size(); i++) {
					page++;
					row = sheet.createRow(page);
					row.setHeightInPoints(50);
					cell = row.createCell(0);
					cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
					cell.setCellStyle(headerStyle2);
					for (int j = 0; j < weekLists.size(); j++) {
						cell = row.createCell(j + 1);
						cell.setCellStyle(headerStyle3);
						N33_ZKBDTO dto = zkbMap.get(j + "" + i);
						if (dto != null) {
							if (dto.getSwType() == 0) {
								StringBuffer sb = new StringBuffer();
								if (dto.getJxbName() != null) {
									sb.append(dto.getJxbName()).append("（" + dto.getStudentCount() + "）\r\n");
								}
								if (dto.getSubjectName() != null) {
									sb.append(dto.getSubjectName() + "\r\n");
								}
								if (dto.getGradeId() != null) {
									sb.append(gradeMap.get(new ObjectId(dto.getGradeId())).getName() + "（" + gradeMap.get(new ObjectId(dto.getGradeId())).getJie() + "）\r\n");
								}
								if (dto.getRoomName() != null) {
									sb.append(dto.getRoomName());
								}
								String con = sb.toString();
								cell.setCellValue(con);
							} else {
								cell.setCellValue(dto.getSwDesc());
							}
						}
					}
				}
			}

		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 导出一组老师的课表
	 *
	 * @param map
	 * @param sid
	 * @param response
	 */
	public void exportTeaKBByGroup(Map map, ObjectId sid, HttpServletResponse response,HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map<String, Object> status = new HashMap<String, Object>();
		status.put("st", 1);
		session.setAttribute("dckb", status);
		Integer week = Integer.parseInt((String) map.get("week"));
		List<String> tids = (List<String>) map.get("teaIds");
		List<ObjectId> teacherIds = MongoUtils.convertToObjectIdList(tids);
		ObjectId xqid = new ObjectId((String) map.get("xqid"));
		String gradeId = (String) map.get("gradeId");
		String subId = (String) map.get("subId");
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		for (TermEntry.PaiKeTimes times : termEntry.getPaiKeTimes()) {
			cids.add(times.getID());
		}
		//课节
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(sid.toString(), xqid);
		Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(cids, sid);
		Map<ObjectId, Map<String,N33_ZKBDTO>> teacherKBMap = getAllTeacherKB(teacherIds, week, sid, xqid);
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMapByXQID(cids, sid);
		exportTeaKBAll(teacherIds,teacherKBMap,termEntry,response,gradeId,subId,courseRangeDTOs,gradeMap,teaEntryMap,week);
		status.put("st", -1);
		session.setAttribute("dckb", status);
	}

	public void exportTeaKBAll(List<ObjectId> teaIds, Map<ObjectId, Map<String, N33_ZKBDTO>> teacherKBMap, TermEntry termEntry, HttpServletResponse response, String gradeId,String subjectId, List<CourseRangeDTO> courseRangeDTOs, Map<ObjectId, Grade> gradeMap, Map<ObjectId, N33_TeaEntry> teaEntryMap,Integer week) {
		List<ObjectId> subIds = new ArrayList<ObjectId>();
		if("*".equals(subjectId)){
			for (ObjectId teaId : teaIds) {
				N33_TeaEntry teaEntry = teaEntryMap.get(teaId);
				for (ObjectId id : teaEntry.getSubjectList()) {
					if (!subIds.contains(id)) {
						subIds.add(id);
					}
				}
			}
		}else{
			subIds.add(new ObjectId(subjectId));
			subIds.add(new ObjectId(subjectId));
		}

		Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.getIsolateSubjectEntryBySubs(subIds);
		HSSFWorkbook wb = new HSSFWorkbook();
		if (!"*".equals(gradeId)) {
			HSSFSheet sheet = wb.createSheet(gradeMap.get(new ObjectId(gradeId)).getName() + "年级课表");
			sheet.setVerticallyCenter(true);
			sheet.setDisplayGridlines(false);

			HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
			cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中
			//设置列宽
			sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
			sheet.setColumnWidth((short) 1, (short) 5500);
			sheet.setColumnWidth((short) 2, (short) 5500);
			sheet.setColumnWidth((short) 3, (short) 5500);
			sheet.setColumnWidth((short) 4, (short) 5500);
			sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
			sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
			sheet.setColumnWidth((short) 7, (short) 5500);

			HSSFCellStyle headerStyle0 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
			headerStyle0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headerStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont headerFont0 = (HSSFFont) wb.createFont();    //创建字体样式
			headerFont0.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
			headerFont0.setFontHeightInPoints((short) 16);    //设置字体大小
			headerStyle0.setFont(headerFont0);    //为标题样式设置字体样式
			headerStyle0.setBorderBottom(HSSFCellStyle.BORDER_NONE); // 下边框
			headerStyle0.setBorderLeft(HSSFCellStyle.BORDER_NONE);// 左边框
			headerStyle0.setBorderTop(HSSFCellStyle.BORDER_NONE);// 上边框
			headerStyle0.setBorderRight(HSSFCellStyle.BORDER_NONE);// 右边框
			headerStyle0.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
			headerStyle0.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
			headerStyle0.setWrapText(true);

			HSSFCellStyle headerStyle4 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
			headerStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headerStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont headerFont4 = (HSSFFont) wb.createFont();    //创建字体样式
			headerFont4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
			headerFont4.setFontHeightInPoints((short) 16);    //设置字体大小
			headerStyle4.setFont(headerFont0);    //为标题样式设置字体样式
			headerStyle4.setBorderBottom(HSSFCellStyle.BORDER_NONE); // 下边框
			headerStyle4.setBorderLeft(HSSFCellStyle.BORDER_NONE);// 左边框
			headerStyle4.setBorderTop(HSSFCellStyle.BORDER_NONE);// 上边框
			headerStyle4.setBorderRight(HSSFCellStyle.BORDER_NONE);// 右边框
			//headerStyle4.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
			headerStyle4.setWrapText(true);

			HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
			headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
			headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
			headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
			headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
			headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			headerStyle1.setWrapText(true);

			HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
			headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
			headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
			headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
			headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
			headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
			headerStyle2.setWrapText(true);

			HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
			headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
			headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
			headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
			headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			headerStyle3.setWrapText(true);
			//在某个学科已经导出的老师课表，后续的老师课表中不再导出
			List<ObjectId> ydcTeaIds = new ArrayList<ObjectId>();
			//记录老师循环的次数，用于记录哪些行是跨列的
			Integer count = 0;
			//记录学科循环的次数，用于记录哪些行是跨列的
			Integer count1 = 0;
			for (ObjectId subId : subIds) {
				//判断该学科下是否有未导出的老师
				boolean flag1 = false;
				for (ObjectId teaId : teaIds) {
					if(!ydcTeaIds.contains(teaId) && teaEntryMap.get(teaId).getSubjectList().contains(subId)){
						flag1 = true;
					}
				}
				if(!flag1){
					continue;
				}
				//记录是否遍历到另一个学科
				boolean flag = true;
				count1 ++;
				if(count == 0 && count1 == 1){
					sheet.addMergedRegion(new Region(0, (short)0, 0, (short) 7));
					sheet.addMergedRegion(new Region(1, (short)0, 1, (short) 7));
				}else if(flag && count1 != 1){
					sheet.addMergedRegion(new Region(ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1), (short)0, ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1), (short) 7));
					sheet.addMergedRegion(new Region(ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1) + 1, (short)0, ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1-1) + 1, (short) 7));
				}
				//为sheet1生成第二行，用于放表头信息
				HSSFRow row1 =  null;
				if(count1 == 1 && count == 0){
					row1 = sheet.createRow(0);
				}else{
					row1 = sheet.createRow((count1 - 1) + (ydcTeaIds.size() * (courseRangeDTOs.size() + 2)));
				}
				row1.setHeightInPoints(40);
				HSSFCell cell1 = row1.createCell(0);
				cell1.setCellStyle(headerStyle0);
				cell1.setCellValue(termEntry.getXqName() + "   " + gradeMap.get(new ObjectId(gradeId)).getName() + "   " + ksEntryMap.get(subId).getSubjectName());

				//设置打印对象
				HSSFPrintSetup printSetup = sheet.getPrintSetup();
				printSetup.setPaperSize(HSSFPrintSetup.A3_PAPERSIZE);

				//设置打印方向，横向就是true
				printSetup.setLandscape(true);
				sheet.setDisplayGridlines(false);
				sheet.setPrintGridlines(false);
				sheet.setHorizontallyCenter(true);//设置打印页面为水平居中
				sheet.setVerticallyCenter(true);
				for (ObjectId teaId : teaIds) {
					if(!ydcTeaIds.contains(teaId) && teaEntryMap.get(teaId).getSubjectList().contains(subId)){
						if(!flag){
							sheet.addMergedRegion(new Region(ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1) + 1, (short)0, ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1) + 1, (short) 7));
						}
						//为sheet1生成第二行，用于放表头信息
						HSSFRow row2 = null;
						if(count1 == 1 && count == 0){
							row2 = sheet.createRow(1);
						}else{
							row2 = sheet.createRow((count1 - 1) + (ydcTeaIds.size() * (courseRangeDTOs.size() + 2)) + 1);
						}
						row2.setHeightInPoints(40);
						HSSFCell cell2 = row2.createCell(0);
						cell2.setCellStyle(headerStyle4);
						cell2.setCellValue(termEntry.getXqName() + "  " + teaEntryMap.get(teaId).getUserName() + "  第（" + week + "）周  课表");
						sheet.setRowBreak((count1 - 1) + ((ydcTeaIds.size() + 1) * (courseRangeDTOs.size() + 2)));
						sheet.setColumnBreak(8);
						flag = false;

						//为sheet1生成第三行，用于放表头信息
						HSSFRow row = null;
						if(count == 0 && count1 == 1){
							row = sheet.createRow(2);
						}else{
							row = sheet.createRow((count1 - 1) + (ydcTeaIds.size() * (courseRangeDTOs.size() + 2)) + 2);
						}

						row.setHeightInPoints(35);
						HSSFCell cell = row.createCell(0);
						cell.setCellStyle(headerStyle2);
						cell.setCellValue("课节 / 日");
						List<String> weekLists = new ArrayList<String>();
						weekLists.add("周一");
						weekLists.add("周二");
						weekLists.add("周三");
						weekLists.add("周四");
						weekLists.add("周五");
//						weekLists.add("周六");
//						weekLists.add("周日");
						for (int j = 0; j < weekLists.size(); j++) {
							cell = row.createCell(j + 1);
							cell.setCellStyle(headerStyle2);
							cell.setCellValue(weekLists.get(j));
						}
						Map<String,N33_ZKBDTO> zkbMap = teacherKBMap.get(teaId);
						int page = 1;
						if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
							for (int i = 0; i < courseRangeDTOs.size(); i++) {
								page++;
								if(count == 0 && count1 == 1){
									row = sheet.createRow(page + 1);
								}else{
									row = sheet.createRow((count1 - 1) + (ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + 1) +page);
								}
								row.setHeightInPoints(50);
								cell = row.createCell(0);
								cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
								cell.setCellStyle(headerStyle2);
								for (int j = 0; j < weekLists.size(); j++) {
									cell = row.createCell(j + 1);
									cell.setCellStyle(headerStyle3);
									N33_ZKBDTO dto = zkbMap.get(j + "" + i);
									if (dto != null) {
										if (dto.getIsSWAndCourse() == 0) {
											if (dto.getSwType() == 0) {
												StringBuffer sb = new StringBuffer();
												if (dto.getJxbName() != null) {
													sb.append(dto.getJxbName());
//															.append("（" + dto.getStudentCount() + "人）\r\n");
												}
//												if(dto.getnJxbName() != null && !"".equals(dto.getnJxbName())){
//													if(dto.getJxbName() == null){
//														sb.append(dto.getnJxbName()).append("（" + dto.getnStudentCount() + "人）\r\n");
//													}else{
//														sb.append("/" + dto.getnJxbName()).append("（" + dto.getnStudentCount() + "人）\r\n");
//													}
//
//												}
												if (dto.getSubjectName() != null) {
//													sb.append(dto.getSubjectName() + "\r\n");
												}
//												if(dto.getnSubjectName() != null && !"".equals(dto.getnSubjectName())){
//													if(dto.getSubjectName() == null){
//														sb.append(dto.getnSubjectName() + "\r\n");
//													}else{
//														sb.append("/" + dto.getnSubjectName() + "\r\n");
//													}
//
//												}
												if (dto.getGradeId() != null) {
//													sb.append(gradeMap.get(new ObjectId(dto.getGradeId())).getName() + "（" + gradeMap.get(new ObjectId(dto.getGradeId())).getJie() + "）\r\n");
												}
//												if (dto.getGradeName() != null) {
//													sb.append(dto.getGradeName());
//												}
//												if(dto.getnGradeName() != null && !"".equals(dto.getnGradeName())){
//													if(dto.getGradeName() == null){
//														sb.append(dto.getnGradeName() + "\r\n");
//													}else{
//														sb.append("/" + dto.getnGradeName() + "\r\n");
//													}
//												}
												if (dto.getRoomName() != null) {
//													sb.append(dto.getRoomName());
												}
//												if(dto.getnRoomName() != null && !"".equals(dto.getnRoomName())){
//													if(dto.getRoomName() == null){
//														sb.append(dto.getnRoomName() + "\r\n");
//													}else{
//														sb.append("/" + dto.getnRoomName() + "\r\n");
//													}
//												}
												String con = sb.toString();
												cell.setCellValue(con);
											} else {
												cell.setCellValue(dto.getSwDesc());
											}
										} else {
											StringBuffer sb = new StringBuffer();
											if (dto.getJxbName() != null) {
												sb.append(dto.getJxbName());
//														.append("（" + dto.getStudentCount() + "）\r\n");
											}
//											if(dto.getnJxbName() != null && !"".equals(dto.getnJxbName())){
//												if(dto.getJxbName() == null){
//													sb.append(dto.getnJxbName()).append("（" + dto.getnStudentCount() + "人）\r\n");
//												}else{
//													sb.append("/" + dto.getnJxbName()).append("（" + dto.getnStudentCount() + "人）\r\n");
//												}
//
//											}
											if (dto.getSubjectName() != null) {
//												sb.append(dto.getSubjectName() + "\r\n");
											}
//											if(dto.getnSubjectName() != null && !"".equals(dto.getnSubjectName())){
//												if(dto.getSubjectName() == null){
//													sb.append(dto.getnSubjectName() + "\r\n");
//												}else{
//													sb.append("/" + dto.getnSubjectName() + "\r\n");
//												}
//
//											}
											if (dto.getGradeId() != null) {
//												sb.append(gradeMap.get(new ObjectId(dto.getGradeId())).getName() + "（" + gradeMap.get(new ObjectId(dto.getGradeId())).getJie() + "）\r\n");
											}
//											if (dto.getGradeName() != null) {
//												sb.append(dto.getGradeName());
//											}
//											if(dto.getnGradeName() != null && !"".equals(dto.getnGradeName())){
//												if(dto.getGradeName() == null){
//													sb.append(dto.getnGradeName() + "\r\n");
//												}else{
//													sb.append("/" + dto.getnGradeName() + "\r\n");
//												}
//											}
											if (dto.getRoomName() != null) {
//												sb.append(dto.getRoomName() + "\r\n");
											}
//											if(dto.getnRoomName() != null && !"".equals(dto.getnRoomName())){
//												if(dto.getRoomName() == null){
//													sb.append(dto.getnRoomName() + "\r\n");
//												}else{
//													sb.append("/" + dto.getnRoomName() + "\r\n");
//												}
//											}
											sb.append("事务：" + dto.getSwDesc());
											String con = sb.toString();
											cell.setCellValue(con);
										}
									}
								}
							}
						}
						count ++;
						ydcTeaIds.add(teaId);
					}
				}
			}
		} else {
			Collection<Grade> gradeList = (Collection<Grade>) gradeMap.values();
			for (Grade grade : gradeList) {
				gradeId = grade.getGradeId().toString();
				HSSFSheet sheet = wb.createSheet(gradeMap.get(new ObjectId(gradeId)).getName() + "年级课表");
				sheet.setVerticallyCenter(true);
				sheet.setDisplayGridlines(false);

				HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
				cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

				//设置列宽
				sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
				sheet.setColumnWidth((short) 1, (short) 5500);
				sheet.setColumnWidth((short) 2, (short) 5500);
				sheet.setColumnWidth((short) 3, (short) 5500);
				sheet.setColumnWidth((short) 4, (short) 5500);
				sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
				sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
				sheet.setColumnWidth((short) 7, (short) 5500);

				HSSFCellStyle headerStyle0 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
				headerStyle0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				headerStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				HSSFFont headerFont0 = (HSSFFont) wb.createFont();    //创建字体样式
				headerFont0.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
				headerFont0.setFontHeightInPoints((short) 16);    //设置字体大小
				headerStyle0.setFont(headerFont0);    //为标题样式设置字体样式
				headerStyle0.setBorderBottom(HSSFCellStyle.BORDER_NONE); // 下边框
				headerStyle0.setBorderLeft(HSSFCellStyle.BORDER_NONE);// 左边框
				headerStyle0.setBorderTop(HSSFCellStyle.BORDER_NONE);// 上边框
				headerStyle0.setBorderRight(HSSFCellStyle.BORDER_NONE);// 右边框
				headerStyle0.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
				headerStyle0.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
				headerStyle0.setWrapText(true);

				HSSFCellStyle headerStyle4 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
				headerStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				headerStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				HSSFFont headerFont4 = (HSSFFont) wb.createFont();    //创建字体样式
				headerFont4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
				headerFont4.setFontHeightInPoints((short) 16);    //设置字体大小
				headerStyle4.setFont(headerFont4);    //为标题样式设置字体样式
				headerStyle4.setBorderBottom(HSSFCellStyle.BORDER_NONE); // 下边框
				headerStyle4.setBorderLeft(HSSFCellStyle.BORDER_NONE);// 左边框
				headerStyle4.setBorderTop(HSSFCellStyle.BORDER_NONE);// 上边框
				headerStyle4.setBorderRight(HSSFCellStyle.BORDER_NONE);// 右边框
				//headerStyle4.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
				headerStyle4.setWrapText(true);

				HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
				headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
				headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
				headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
				headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
				headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_NONE); // 下边框
				headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_NONE);// 左边框
				headerStyle1.setBorderTop(HSSFCellStyle.BORDER_NONE);// 上边框
				headerStyle1.setBorderRight(HSSFCellStyle.BORDER_NONE);// 右边框
//				headerStyle1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//				headerStyle1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
				headerStyle1.setWrapText(true);

				HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
				headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
				headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
				headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
				headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
				headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
				headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
				headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
				headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
				headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
				headerStyle2.setWrapText(true);

				HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
				headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
				headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
				headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
				headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
				headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
				headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
				headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
				headerStyle3.setWrapText(true);
				//在某个学科已经导出的老师课表，后续的老师课表中不再导出
				List<ObjectId> ydcTeaIds = new ArrayList<ObjectId>();
				//记录老师循环的次数，用于记录哪些行是跨列的
				Integer count = 0;
				//记录学科循环的次数，用于记录哪些行是跨列的
				Integer count1 = 0;
				for (ObjectId subId : subIds) {
					//判断该学科下是否有未导出的老师
					boolean flag1 = false;
					for (ObjectId teaId : teaIds) {
						if(!ydcTeaIds.contains(teaId) && teaEntryMap.get(teaId).getSubjectList().contains(subId)){
							flag1 = true;
						}
					}
					if(!flag1){
						continue;
					}
					//记录是否遍历到另一个学科
					boolean flag = true;
					count1 ++;
					if(count == 0 && count1 == 1){
						sheet.addMergedRegion(new Region(0, (short)0, 0, (short) 7));
						sheet.addMergedRegion(new Region(1, (short)0, 1, (short) 7));
					}else if(flag && count1 != 1){
						sheet.addMergedRegion(new Region(ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1), (short)0, ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1), (short) 7));
						sheet.addMergedRegion(new Region(ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1) + 1, (short)0, ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1-1) + 1, (short) 7));
					}

					//设置打印对象
					HSSFPrintSetup printSetup = sheet.getPrintSetup();
					printSetup.setPaperSize(HSSFPrintSetup.A3_PAPERSIZE);

					//设置打印方向，横向就是true
					printSetup.setLandscape(true);
					sheet.setDisplayGridlines(false);
					sheet.setPrintGridlines(false);
					sheet.setHorizontallyCenter(true);//设置打印页面为水平居中
					sheet.setVerticallyCenter(true);

					//为sheet1生成第二行，用于放表头信息
					HSSFRow row1 =  null;
					if(count1 == 1 && count == 0){
						row1 = sheet.createRow(0);
					}else{
						row1 = sheet.createRow((count1 - 1) + (ydcTeaIds.size() * (courseRangeDTOs.size() + 2)));
					}
					row1.setHeightInPoints(40);
					HSSFCell cell1 = row1.createCell(0);
					cell1.setCellStyle(headerStyle0);
					cell1.setCellValue(termEntry.getXqName() + "   " + gradeMap.get(new ObjectId(gradeId)).getName() + "   " + ksEntryMap.get(subId).getSubjectName());
					for (ObjectId teaId : teaIds) {
						if(!ydcTeaIds.contains(teaId) && teaEntryMap.get(teaId).getSubjectList().contains(subId)){
							if(!flag){
								sheet.addMergedRegion(new Region(ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1) + 1, (short)0, ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + (count1 - 1) + 1, (short) 7));
							}
							sheet.setRowBreak((count1 - 1) + ((ydcTeaIds.size() + 1) * (courseRangeDTOs.size() + 2)));
							sheet.setColumnBreak(8);
							flag = false;
							//为sheet1生成第二行，用于放表头信息
							HSSFRow row2 = null;
							if(count1 == 1 && count == 0){
								row2 = sheet.createRow(1);
							}else{
								row2 = sheet.createRow((count1 - 1) + (ydcTeaIds.size() * (courseRangeDTOs.size() + 2)) + 1);
							}
							row2.setHeightInPoints(40);
							HSSFCell cell2 = row2.createCell(0);
							cell2.setCellStyle(headerStyle1);
							cell2.setCellValue(termEntry.getXqName() + "  " + teaEntryMap.get(teaId).getUserName() + "  第（" + week + "）周  课表");
							//为sheet1生成第三行，用于放表头信息
							HSSFRow row = null;
							if(count == 0 && count1 == 1){
								row = sheet.createRow(2);
							}else{
								row = sheet.createRow((count1 - 1) + (ydcTeaIds.size() * (courseRangeDTOs.size() + 2)) + 2);
							}

							row.setHeightInPoints(35);
							HSSFCell cell = row.createCell(0);
							cell.setCellStyle(headerStyle4);
							cell.setCellValue("课节 / 日");
							List<String> weekLists = new ArrayList<String>();
							weekLists.add("周一");
							weekLists.add("周二");
							weekLists.add("周三");
							weekLists.add("周四");
							weekLists.add("周五");
							weekLists.add("周六");
							weekLists.add("周日");
							for (int j = 0; j < weekLists.size(); j++) {
								cell = row.createCell(j + 1);
								cell.setCellStyle(headerStyle2);
								cell.setCellValue(weekLists.get(j));
							}
							Map<String,N33_ZKBDTO> zkbMap = teacherKBMap.get(teaId);
							int page = 1;
							if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
								for (int i = 0; i < courseRangeDTOs.size(); i++) {
									page++;
									if(count == 0 && count1 == 1){
										row = sheet.createRow(page + 1);
									}else{
										row = sheet.createRow((count1 - 1) + (ydcTeaIds.size() * (courseRangeDTOs.size() + 2) + 1) +page);
									}
									row.setHeightInPoints(50);
									cell = row.createCell(0);
									cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
									cell.setCellStyle(headerStyle2);
									for (int j = 0; j < weekLists.size(); j++) {
										cell = row.createCell(j + 1);
										cell.setCellStyle(headerStyle3);
										N33_ZKBDTO dto = zkbMap.get(j + "" + i);
										if (dto != null) {
											if (dto.getIsSWAndCourse() == 0) {
												if (dto.getSwType() == 0) {
													StringBuffer sb = new StringBuffer();
													if (dto.getJxbName() != null) {
														sb.append(dto.getJxbName()).append("（" + dto.getStudentCount() + "人）\r\n");
													}
//													if(dto.getnJxbName() != null && !"".equals(dto.getnJxbName())){
//														if(dto.getJxbName() == null){
//															sb.append(dto.getnJxbName()).append("（" + dto.getnStudentCount() + "人）\r\n");
//														}else{
//															sb.append("/" + dto.getnJxbName()).append("（" + dto.getnStudentCount() + "人）\r\n");
//														}
//
//													}
													if (dto.getSubjectName() != null) {
														sb.append(dto.getSubjectName() + "\r\n");
													}
//													if(dto.getnSubjectName() != null && !"".equals(dto.getnSubjectName())){
//														if(dto.getSubjectName() == null){
//															sb.append(dto.getnSubjectName() + "\r\n");
//														}else{
//															sb.append("/" + dto.getnSubjectName() + "\r\n");
//														}
//
//													}
													if (dto.getGradeId() != null) {
														sb.append(gradeMap.get(new ObjectId(dto.getGradeId())).getName() + "（" + gradeMap.get(new ObjectId(dto.getGradeId())).getJie() + "）\r\n");
													}
//													if (dto.getGradeName() != null) {
//														sb.append(dto.getGradeName());
//													}
//													if(dto.getnGradeName() != null && !"".equals(dto.getnGradeName())){
//														if(dto.getGradeName() == null){
//															sb.append(dto.getnGradeName() + "\r\n");
//														}else{
//															sb.append("/" + dto.getnGradeName() + "\r\n");
//														}
//													}
													if (dto.getRoomName() != null) {
														sb.append(dto.getRoomName());
													}
//													if(dto.getnRoomName() != null && !"".equals(dto.getnRoomName())){
//														if(dto.getRoomName() == null){
//															sb.append(dto.getnRoomName() + "\r\n");
//														}else{
//															sb.append("/" + dto.getnRoomName() + "\r\n");
//														}
//													}
													String con = sb.toString();
													cell.setCellValue(con);
												} else {
													cell.setCellValue(dto.getSwDesc());
												}
											} else {
												StringBuffer sb = new StringBuffer();
												if (dto.getJxbName() != null) {
													sb.append(dto.getJxbName()).append("（" + dto.getStudentCount() + "）\r\n");
												}
//												if(dto.getnJxbName() != null && !"".equals(dto.getnJxbName())){
//													if(dto.getJxbName() == null){
//														sb.append(dto.getnJxbName()).append("（" + dto.getnStudentCount() + "人）\r\n");
//													}else{
//														sb.append("/" + dto.getnJxbName()).append("（" + dto.getnStudentCount() + "人）\r\n");
//													}
//
//												}
												if (dto.getSubjectName() != null) {
													sb.append(dto.getSubjectName() + "\r\n");
												}
//												if(dto.getnSubjectName() != null && !"".equals(dto.getnSubjectName())){
//													if(dto.getSubjectName() == null){
//														sb.append(dto.getnSubjectName() + "\r\n");
//													}else{
//														sb.append("/" + dto.getnSubjectName() + "\r\n");
//													}
//
//												}
												if (dto.getGradeId() != null) {
													sb.append(gradeMap.get(new ObjectId(dto.getGradeId())).getName() + "（" + gradeMap.get(new ObjectId(dto.getGradeId())).getJie() + "）\r\n");
												}
//												if (dto.getGradeName() != null) {
//													sb.append(dto.getGradeName());
//												}
//												if(dto.getnGradeName() != null && !"".equals(dto.getnGradeName())){
//													if(dto.getGradeName() == null){
//														sb.append(dto.getnGradeName() + "\r\n");
//													}else{
//														sb.append("/" + dto.getnGradeName() + "\r\n");
//													}
//												}
												if (dto.getRoomName() != null) {
													sb.append(dto.getRoomName() + "\r\n");
												}
//												if(dto.getnRoomName() != null && !"".equals(dto.getnRoomName())){
//													if(dto.getRoomName() == null){
//														sb.append(dto.getnRoomName() + "\r\n");
//													}else{
//														sb.append("/" + dto.getnRoomName() + "\r\n");
//													}
//												}
												sb.append("事务：" + dto.getSwDesc());
												String con = sb.toString();
												cell.setCellValue(con);
											}
										}
									}
								}
							}
							count ++;
							ydcTeaIds.add(teaId);
						}
					}
				}
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("教师课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 查询传入的所有老师的课表
	 * @param teaIds
	 * @param week
	 * @param schoolId
	 * @param xqid
	 * @return
	 */
	public Map<ObjectId,Map<String,N33_ZKBDTO>> getAllTeacherKB(List<ObjectId> teaIds, Integer week, ObjectId schoolId, ObjectId xqid) {
		Map<ObjectId,Map<String,N33_ZKBDTO>> retMap = new HashMap<ObjectId, Map<String, N33_ZKBDTO>>();
		//专项课的周课表
		List<N33_ZKBEntry> zkbEntryList = zkbDao.getJXB(xqid, week);
		List<Map<String, Object>> lists = termService.getPaikeTimesByTermId(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		for (Map<String, Object> lt : lists) {
			String cid = (String) lt.get("ciId");
			cids.add(new ObjectId(cid));
		}
		//学科列表 所有次的
		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(cids, schoolId);
		//教室列表 所有次的
		List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, cids);
		//固定事务
		List<N33_SWDTO> gdSwdtos = swService.getGuDingShiWuBySchoolId(xqid, schoolId);
		for (ObjectId teaId : teaIds) {
			List<ObjectId> uids = new ArrayList<ObjectId>();
			uids.add(teaId);
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(uids);
			List<ObjectId> teaGradeIds = new ArrayList<ObjectId>();
			String userName1 = null;
			for (N33_TeaEntry teaEntry : teaEntryMap.values()) {
				userName1 = teaEntry.getUserName();
				for (ObjectId gradeId : teaEntry.getGradeList()) {
					if (!teaGradeIds.contains(gradeId)) {
						teaGradeIds.add(gradeId);
					}
				}
			}

			//周课表
			List<N33_ZKBEntry> entries = zkbDao.getJXB(xqid, teaId, week);
			entries.addAll(zkbEntryList);
			entries.addAll(zkbDao.getJXBN(xqid, teaId, week));
			//教学班列表 所有次的
			List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(cids, teaId);

			jxbEntries.addAll(jxbDao.getZXJXBList(cids));


			//专项课Ids
			List<ObjectId> zxkIds = new ArrayList<ObjectId>();
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				zxkIds.add(jxbEntry.getID());
			}
			List<ObjectId> ods = new ArrayList<ObjectId>();
			for (N33_ZKBEntry jxbEntry : entries) {
				if (jxbEntry.getType() == 5) {
					ods.add(jxbEntry.getJxbId());
				}
			}
			Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(ods);
			List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
			Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
			List<ObjectId> jxbIds = new ArrayList<ObjectId>();
			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
				if (!jxbIds.contains(entry.getJxbId())) {
					jxbIds.add(entry.getJxbId());
				}
			}
			for (ObjectId objectId : jxbIds) {
				List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
				for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
					if (entry.getJxbId().toString().equals(objectId.toString())) {
						jis.add(entry);
					}
				}
				listMap.put(objectId, jis);
			}

			Map<ObjectId, List<N33_ClassroomEntry>> classRoomList = new HashMap<ObjectId, List<N33_ClassroomEntry>>();
			Map<ObjectId, List<N33_KSDTO>> gradeSubjectCidMap = new HashMap<ObjectId, List<N33_KSDTO>>();
			Map<ObjectId, Map<ObjectId, N33_JXBEntry>> jxbEntryMap = new HashMap<ObjectId, Map<ObjectId, N33_JXBEntry>>();

			for (Map<String, Object> lt : lists) {
				String cid = (String) lt.get("ciId");
				//学科
				List<N33_KSDTO> subjectCid = new ArrayList<N33_KSDTO>();
				for (N33_KSDTO ksdto : gradeSubject) {
					if (ksdto.getXqid().equals(cid)) {
						subjectCid.add(ksdto);
					}
				}
				gradeSubjectCidMap.put(new ObjectId(cid), subjectCid);
				// 教学班
				Map<ObjectId, N33_JXBEntry> jxbEntryMap1 = new HashMap<ObjectId, N33_JXBEntry>();
				for (N33_JXBEntry jxbEntry : jxbEntries) {
					if (jxbEntry.getTermId().toString().equals(cid)) {
						jxbEntryMap1.put(jxbEntry.getID(), jxbEntry);
					}
				}
				jxbEntryMap.put(new ObjectId(cid), jxbEntryMap1);
				//教室
				List<N33_ClassroomEntry> jsList = new ArrayList<N33_ClassroomEntry>();
				for (N33_ClassroomEntry js : classRoomDTO) {
					if (js.getXQId().toString().equals(cid)) {
						jsList.add(js);
					}
				}
				classRoomList.put(new ObjectId(cid), jsList);
			}

			//TermEntry termEntry = termDao.getTermByTimeId(xqid);
			//教师事务
			List<N33_SWDTO> teaSwdtos = swService.getSwByXqidAndUserId(xqid, teaId);


			Map<String, N33_ZKBDTO> zkbMap = new HashMap<String, N33_ZKBDTO>();

			for (N33_ZKBEntry entry : entries) {
				Integer count = week % 2;
				N33_ZKBDTO dto = new N33_ZKBDTO(entry);
				dto.setSwType(0);
				dto.setIsSWAndCourse(0);
				List<N33_KSDTO> ksDtos = gradeSubjectCidMap.get(entry.getCId());
				if (entry.getType() != 5) {
					for (N33_KSDTO ksdto : ksDtos) {
						//获取学科
						if(count == 0 && entry.getType() == 6){
							if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								dto.setSubjectName(ksdto.getSnm());
								break;
							}
						}else{
							if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								dto.setSubjectName(ksdto.getSnm());
								break;
							}
						}

					}
				}

				if (entry.getType() != 4 && entry.getType() != 5) {
					//教学班
					Map<ObjectId, N33_JXBEntry> jxbEntryMap2 = jxbEntryMap.get(entry.getCId());
					N33_JXBEntry entry1 = jxbEntryMap2.get(entry.getJxbId());
					if (entry1 != null) {
						if (StringUtils.isNotEmpty(entry1.getNickName())) {
							dto.setJxbName(entry1.getNickName());
						} else {
							dto.setJxbName(entry1.getName());
						}
						dto.setStudentCount(entry1.getStudentIds().size());
					}
					//教室
					List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
					for (N33_ClassroomEntry room : classRoomDTOList) {
						//获取教室
						if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
							dto.setRoomName(room.getRoomName());
							break;
						}
					}
					if (entry.getNJxbId() != null) {
						if (count == 0) {
							if (entry.getNTeacherId().toString().equals(teaId.toString())) {
								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
							}
						} else {
							if (entry.getTeacherId().toString().equals(teaId.toString())) {
								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
							}
						}
					} else {
						zkbMap.put(dto.getX() + "" + dto.getY(), dto);
					}
				} else {
					if (entry.getType() == 4) {
						List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
						for (N33_ZhuanXiangEntry entry1 : zhuanList) {
							if (entry1.getTeaId().toString().equals(teaId.toString())) {
								dto.setJxbName(entry1.getName());
								dto.setStudentCount(entry1.getStudentId().size());
								List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
								for (N33_ClassroomEntry room : classRoomDTOList) {
									//获取教室
									if (room.getRoomId().toString().equals(entry1.getRoomId().toString())) {
										dto.setRoomName(room.getRoomName());
										break;
									}
								}
								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
							}
						}
					} else {
						N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
						dto.setJxbName(entry1.getName());
						dto.setStudentCount(entry1.getStudentIds().size());
						dto.setSubjectName("自习课");
						List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
						for (N33_ClassroomEntry room : classRoomDTOList) {
							//获取教室
							if (room.getRoomId().toString().equals(entry1.getClassroomId().toString())) {
								dto.setRoomName(room.getRoomName());
								break;
							}
						}
						zkbMap.put(dto.getX() + "" + dto.getY(), dto);
					}
				}
			}

			List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, schoolId, teaGradeIds);
			for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
				if (zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY()) != null) {
					N33_ZKBDTO zkbdto = zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY());
					zkbdto.setIsSWAndCourse(1);
					zkbdto.setSwType(1);
					if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
						String s = zkbdto.getSwDesc();
						s += "\r\n";
						s += gdsxEntry.getDesc();
						zkbdto.setSwDesc(s);
					} else {
						zkbdto.setSwDesc(gdsxEntry.getDesc());
					}
					zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
				} else {
					N33_ZKBDTO zkbdto = new N33_ZKBDTO();
					zkbdto.setSwType(1);
					zkbdto.setIsSWAndCourse(0);
					zkbdto.setSwDesc(gdsxEntry.getDesc());
					zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
				}
			}

			for (N33_SWDTO dto : teaSwdtos) {
				if (zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1)) != null) {
					N33_ZKBDTO zkbdto = zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1));
					zkbdto.setIsSWAndCourse(1);
					zkbdto.setSwType(1);
					if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
						String s = zkbdto.getSwDesc();
						s += "\r\n";
						s += dto.getDesc();
						zkbdto.setSwDesc(s);
					} else {
						zkbdto.setSwDesc(dto.getDesc());
					}
					zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
				} else {
					N33_ZKBDTO zkbdto = new N33_ZKBDTO();
					zkbdto.setIsSWAndCourse(0);
					zkbdto.setSwType(1);
					zkbdto.setSwDesc(dto.getDesc());
					zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
				}
			}

			for (N33_SWDTO dto : gdSwdtos) {
				if (zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1)) != null) {
					N33_ZKBDTO zkbdto = zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1));
					zkbdto.setIsSWAndCourse(1);
					zkbdto.setSwType(1);
					if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
						String s = zkbdto.getSwDesc();
						s += "\r\n";
						s += dto.getDesc();
						zkbdto.setSwDesc(s);
					} else {
						zkbdto.setSwDesc(dto.getDesc());
					}
					zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
				} else {
					N33_ZKBDTO zkbdto = new N33_ZKBDTO();
					zkbdto.setIsSWAndCourse(0);
					zkbdto.setSwType(1);
					zkbdto.setSwDesc(dto.getDesc());
					zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
				}
			}
			retMap.put(teaId,zkbMap);
		}
		return retMap;
	}

//	public Map<ObjectId,Map<String,N33_ZKBDTO>> getAllTeacherKB(List<ObjectId> teaIds, Integer week, ObjectId schoolId, ObjectId xqid,Map<ObjectId, Grade> gradeMap) {
//		Map<ObjectId,Map<String,N33_ZKBDTO>> retMap = new HashMap<ObjectId, Map<String, N33_ZKBDTO>>();
//		//专项课的周课表
//		List<N33_ZKBEntry> zkbEntryList = zkbDao.getJXB(xqid, week);
//		List<Map<String, Object>> lists = termService.getPaikeTimesByTermId(xqid);
//		List<ObjectId> cids = new ArrayList<ObjectId>();
//		for (Map<String, Object> lt : lists) {
//			String cid = (String) lt.get("ciId");
//			cids.add(new ObjectId(cid));
//		}
//		//学科列表 所有次的
//		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(cids, schoolId);
//		//教室列表 所有次的
//		List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, cids);
//		//固定事务
//		List<N33_SWDTO> gdSwdtos = swService.getGuDingShiWuBySchoolId(xqid, schoolId);
//		for (ObjectId teaId : teaIds) {
//			List<ObjectId> uids = new ArrayList<ObjectId>();
//			uids.add(teaId);
//			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(uids);
//			List<ObjectId> teaGradeIds = new ArrayList<ObjectId>();
//			String userName1 = null;
//			for (N33_TeaEntry teaEntry : teaEntryMap.values()) {
//				userName1 = teaEntry.getUserName();
//				for (ObjectId gradeId : teaEntry.getGradeList()) {
//					if (!teaGradeIds.contains(gradeId)) {
//						teaGradeIds.add(gradeId);
//					}
//				}
//			}
//
//			//周课表
//			List<N33_ZKBEntry> entries = zkbDao.getJXB(xqid, teaId, week);
//			entries.addAll(zkbEntryList);
//			entries.addAll(zkbDao.getJXBN(xqid, teaId, week));
//			//教学班列表 所有次的
//			List<N33_JXBEntry> jxbEntries = jxbDao.getJXBsBySubId(cids, teaId);
//			jxbEntries.addAll(jxbDao.getJXBsBySubIds(cids));
//
//
//			//专项课Ids
//			List<ObjectId> zxkIds = new ArrayList<ObjectId>();
//			for (N33_JXBEntry jxbEntry : jxbEntries) {
//				zxkIds.add(jxbEntry.getID());
//			}
//			List<ObjectId> ods = new ArrayList<ObjectId>();
//			for (N33_ZKBEntry jxbEntry : entries) {
//				if (jxbEntry.getType() == 5) {
//					ods.add(jxbEntry.getJxbId());
//				}
//			}
//			Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(ods);
//			List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
//			Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
//			List<ObjectId> jxbIds = new ArrayList<ObjectId>();
//			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
//				if (!jxbIds.contains(entry.getJxbId())) {
//					jxbIds.add(entry.getJxbId());
//				}
//			}
//			for (ObjectId objectId : jxbIds) {
//				List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
//				for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
//					if (entry.getJxbId().toString().equals(objectId.toString())) {
//						jis.add(entry);
//					}
//				}
//				listMap.put(objectId, jis);
//			}
//
//			Map<ObjectId, List<N33_ClassroomEntry>> classRoomList = new HashMap<ObjectId, List<N33_ClassroomEntry>>();
//			Map<ObjectId, List<N33_KSDTO>> gradeSubjectCidMap = new HashMap<ObjectId, List<N33_KSDTO>>();
//			Map<ObjectId, Map<ObjectId, N33_JXBEntry>> jxbEntryMap = new HashMap<ObjectId, Map<ObjectId, N33_JXBEntry>>();
//
//			for (Map<String, Object> lt : lists) {
//				String cid = (String) lt.get("ciId");
//				//学科
//				List<N33_KSDTO> subjectCid = new ArrayList<N33_KSDTO>();
//				for (N33_KSDTO ksdto : gradeSubject) {
//					if (ksdto.getXqid().equals(cid)) {
//						subjectCid.add(ksdto);
//					}
//				}
//				gradeSubjectCidMap.put(new ObjectId(cid), subjectCid);
//				// 教学班
//				Map<ObjectId, N33_JXBEntry> jxbEntryMap1 = new HashMap<ObjectId, N33_JXBEntry>();
//				for (N33_JXBEntry jxbEntry : jxbEntries) {
//					if (jxbEntry.getTermId().toString().equals(cid)) {
//						jxbEntryMap1.put(jxbEntry.getID(), jxbEntry);
//					}
//				}
//				jxbEntryMap.put(new ObjectId(cid), jxbEntryMap1);
//				//教室
//				List<N33_ClassroomEntry> jsList = new ArrayList<N33_ClassroomEntry>();
//				for (N33_ClassroomEntry js : classRoomDTO) {
//					if (js.getXQId().toString().equals(cid)) {
//						jsList.add(js);
//					}
//				}
//				classRoomList.put(new ObjectId(cid), jsList);
//			}
//
//			//TermEntry termEntry = termDao.getTermByTimeId(xqid);
//			//教师事务
//			List<N33_SWDTO> teaSwdtos = swService.getSwByXqidAndUserId(xqid, teaId);
//
//			Map<String, N33_ZKBDTO> zkbMap = new HashMap<String, N33_ZKBDTO>();
//
//			for (N33_ZKBEntry entry : entries) {
//				Integer count = week % 2;
//				N33_ZKBDTO dto = null;
//				if(zkbMap.get(entry.getX() + "" + entry.getY()) == null){
//					dto = new N33_ZKBDTO(entry);
//				}else{
//					dto = zkbMap.get(entry.getX() + "" + entry.getY());
//				}
//
//				if(entry.getType() == 4){
//					List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
//					List<ObjectId> tids = MongoUtils.getFieldObjectIDs(zhuanList,"tid");
//					if(!tids.contains(teaId)){
//						continue;
//					}
//				}
//
//				if(entry.getType() == 6){
//					if(teaId.equals(entry.getNTeacherId())){
//						dto.setnGradeName(gradeMap.get(entry.getGradeId()).getName() + "（" + gradeMap.get(entry.getGradeId()).getJie() + "）（双）");
//					}
//					if(teaId.equals(entry.getTeacherId())){
//						dto.setGradeName(gradeMap.get(entry.getGradeId()).getName() + "（" + gradeMap.get(entry.getGradeId()).getJie() + "）（单）");
//					}
//				}else{
//					dto.setGradeName(gradeMap.get(entry.getGradeId()).getName() + "（" + gradeMap.get(entry.getGradeId()).getJie());
//				}
//				dto.setSwType(0);
//				dto.setIsSWAndCourse(0);
//				List<N33_KSDTO> ksDtos = gradeSubjectCidMap.get(entry.getCId());
//				if (entry.getType() != 5) {
//					for (N33_KSDTO ksdto : ksDtos) {
//						//获取学科
//						if(entry.getType() == 6){
//							if (teaId.equals(entry.getNTeacherId()) && ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
//								dto.setnSubjectName(ksdto.getSnm() + "（双）");
//							}
//							if (teaId.equals(entry.getTeacherId()) && ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
//								dto.setSubjectName(ksdto.getSnm() + "（单）");
//							}
//						}else{
//							if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
//								dto.setSubjectName(ksdto.getSnm());
//								break;
//							}
//						}
//
//					}
//				}
//				if (entry.getType() != 4 && entry.getType() != 5) {
//					//教学班
//					Map<ObjectId, N33_JXBEntry> jxbEntryMap2 = jxbEntryMap.get(entry.getCId());
//					N33_JXBEntry entry1 = null;
//					if(teaId.equals(entry.getTeacherId())){
//						entry1 = jxbEntryMap2.get(entry.getJxbId());
//						if (entry1 != null) {
//							if (StringUtils.isNotEmpty(entry1.getNickName())) {
//								if(entry.getType() == 6){
//									dto.setJxbName(entry1.getNickName() + "（单）");
//								}else{
//									dto.setJxbName(entry1.getNickName());
//								}
//							} else {
//								if(entry.getType() == 6){
//									dto.setJxbName(entry1.getName() + "（单）");
//								}else{
//									dto.setJxbName(entry1.getName());
//								}
////								dto.setJxbName(entry1.getName());
//							}
//							dto.setStudentCount(entry1.getStudentIds().size());
////							dto.setStudentCount(entry1.getStudentIds().size());
//						}
//					}
//					if(teaId.equals(entry.getNTeacherId())){
//						entry1 = jxbEntryMap2.get(entry.getNJxbId());
//						if (entry1 != null) {
//							if (StringUtils.isNotEmpty(entry1.getNickName())) {
//								if(entry.getType() == 6){
//									dto.setnJxbName(entry1.getNickName() + "（双）");
//								}else{
//									dto.setnJxbName(entry1.getNickName());
//								}
////								dto.setnJxbName(entry1.getNickName());
//							} else {
//								if(entry.getType() == 6){
//									dto.setnJxbName(entry1.getName() + "（双）");
//								}else{
//									dto.setnJxbName(entry1.getName());
//								}
////								dto.setnJxbName(entry1.getName());
//							}
//							dto.setnStudentCount(entry1.getStudentIds().size());
////							dto.setStudentCount(entry1.getStudentIds().size());
//						}
//					}
//
//					//教室
//					List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
//					for (N33_ClassroomEntry room : classRoomDTOList) {
//						if(entry.getType() == 6){
//							if(teaId.equals(entry.getTeacherId())){
//								if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
//									dto.setRoomName(room.getRoomName() + "（单）");
//								}
//							}else if(teaId.equals(entry.getNTeacherId())){
//								if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
//									dto.setnRoomName(room.getRoomName() + "（双）");
//								}
//							}
//						}else{
//							//获取教室
//							if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
//								dto.setRoomName(room.getRoomName());
//								break;
//							}
//						}
//
//					}
//					if(entry.getTeacherId().toString().equals(teaId.toString())||entry.getNTeacherId().toString().equals(teaId.toString())){
//						zkbMap.put(dto.getX() + "" + dto.getY(), dto);
//					}
////					if ( entry.getNJxbId() != null) {
////						if (count == 0) {
////							if (entry.getNTeacherId().toString().equals(teaId.toString())) {
////								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
////							}
////						} else {
////							if (entry.getTeacherId().toString().equals(teaId.toString())) {
////								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
////							}
////						}
////					} else {
////						zkbMap.put(dto.getX() + "" + dto.getY(), dto);
////					}
//				} else {
//					if (entry.getType() == 4) {
//						List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
//						for (N33_ZhuanXiangEntry entry1 : zhuanList) {
//							if (entry1.getTeaId().toString().equals(teaId.toString())) {
//								dto.setJxbName(entry1.getName());
//								dto.setStudentCount(entry1.getStudentId().size());
//								List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
//								for (N33_ClassroomEntry room : classRoomDTOList) {
//									//获取教室
//									if (room.getRoomId().toString().equals(entry1.getRoomId().toString())) {
//										dto.setRoomName(room.getRoomName());
//										break;
//									}
//								}
//								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
//							}
//						}
//					} else {
//						N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
//						dto.setJxbName(entry1.getName());
//						dto.setStudentCount(entry1.getStudentIds().size());
//						dto.setSubjectName("自习课");
//						List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
//						for (N33_ClassroomEntry room : classRoomDTOList) {
//							//获取教室
//							if (room.getRoomId().toString().equals(entry1.getClassroomId().toString())) {
//								dto.setRoomName(room.getRoomName());
//								break;
//							}
//						}
//						zkbMap.put(dto.getX() + "" + dto.getY(), dto);
//					}
//				}
//			}
//
//			List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, schoolId, teaGradeIds);
//			for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
//				if (zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY()) != null) {
//					N33_ZKBDTO zkbdto = zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY());
//					zkbdto.setIsSWAndCourse(1);
//					zkbdto.setSwType(1);
//					if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
//						String s = zkbdto.getSwDesc();
//						s += "\r\n";
//						s += gdsxEntry.getDesc();
//						zkbdto.setSwDesc(s);
//					} else {
//						zkbdto.setSwDesc(gdsxEntry.getDesc());
//					}
//					zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
//				} else {
//					N33_ZKBDTO zkbdto = new N33_ZKBDTO();
//					zkbdto.setSwType(1);
//					zkbdto.setIsSWAndCourse(0);
//					zkbdto.setSwDesc(gdsxEntry.getDesc());
//					zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
//				}
//			}
//
//			for (N33_SWDTO dto : teaSwdtos) {
//				if (zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1)) != null) {
//					N33_ZKBDTO zkbdto = zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1));
//					zkbdto.setIsSWAndCourse(1);
//					zkbdto.setSwType(1);
//					if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
//						String s = zkbdto.getSwDesc();
//						s += "\r\n";
//						s += dto.getDesc();
//						zkbdto.setSwDesc(s);
//					} else {
//						zkbdto.setSwDesc(dto.getDesc());
//					}
//					zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
//				} else {
//					N33_ZKBDTO zkbdto = new N33_ZKBDTO();
//					zkbdto.setIsSWAndCourse(0);
//					zkbdto.setSwType(1);
//					zkbdto.setSwDesc(dto.getDesc());
//					zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
//				}
//			}
//
//			for (N33_SWDTO dto : gdSwdtos) {
//				if (zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1)) != null) {
//					N33_ZKBDTO zkbdto = zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1));
//					zkbdto.setIsSWAndCourse(1);
//					zkbdto.setSwType(1);
//					if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
//						String s = zkbdto.getSwDesc();
//						s += "\r\n";
//						s += dto.getDesc();
//						zkbdto.setSwDesc(s);
//					} else {
//						zkbdto.setSwDesc(dto.getDesc());
//					}
//					zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
//				} else {
//					N33_ZKBDTO zkbdto = new N33_ZKBDTO();
//					zkbdto.setIsSWAndCourse(0);
//					zkbdto.setSwType(1);
//					zkbdto.setSwDesc(dto.getDesc());
//					zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
//				}
//			}
//			retMap.put(teaId,zkbMap);
//		}
//		return retMap;
//	}

	/**
	 * 导出教师课表
	 */
	public void exportTeaKB(String userName, ObjectId teacherId, ObjectId schoolId, Integer week, HttpServletResponse response) {
		ObjectId xqid = getDefauleTermId(schoolId);

		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> uids = new ArrayList<ObjectId>();
		uids.add(teacherId);
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(uids);
		String userName1 = null;
		List<ObjectId> teaGradeIds = new ArrayList<ObjectId>();
		for (N33_TeaEntry teaEntry : teaEntryMap.values()) {
			userName1 = teaEntry.getUserName();
			for (ObjectId gradeId : teaEntry.getGradeList()) {
				if (!teaGradeIds.contains(gradeId)) {
					teaGradeIds.add(gradeId);
				}
			}
		}

		List<Map<String, Object>> lists = termService.getPaikeTimesByTermId(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		for (Map<String, Object> lt : lists) {
			String cid = (String) lt.get("ciId");
			cids.add(new ObjectId(cid));
		}

		//该次排课的所有年级
		Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(cids, schoolId);

		//周课表
		List<N33_ZKBEntry> entries = zkbDao.getJXB(xqid, teacherId, week);
		entries.addAll(zkbDao.getJXB(xqid, week));
		entries.addAll(zkbDao.getJXBN(xqid, teacherId, week));
		//学科列表 所有次的
		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(cids, schoolId);
		//教学班列表 所有次的
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(cids, teacherId);

		jxbEntries.addAll(jxbDao.getZXJXBList(cids));
		//教室列表 所有次的
		List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, cids);

		//专项课Ids
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			zxkIds.add(jxbEntry.getID());
		}
		List<ObjectId> ods = new ArrayList<ObjectId>();
		for (N33_ZKBEntry jxbEntry : entries) {
			if (jxbEntry.getType() == 5) {
				ods.add(jxbEntry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(ods);
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
			if (!jxbIds.contains(entry.getJxbId())) {
				jxbIds.add(entry.getJxbId());
			}
		}
		for (ObjectId objectId : jxbIds) {
			List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
				if (entry.getJxbId().toString().equals(objectId.toString())) {
					jis.add(entry);
				}
			}
			listMap.put(objectId, jis);
		}

		//课节
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(schoolId.toString(), xqid);

		Map<ObjectId, List<N33_ClassroomEntry>> classRoomList = new HashMap<ObjectId, List<N33_ClassroomEntry>>();
		Map<ObjectId, List<N33_KSDTO>> gradeSubjectCidMap = new HashMap<ObjectId, List<N33_KSDTO>>();
		Map<ObjectId, Map<ObjectId, N33_JXBEntry>> jxbEntryMap = new HashMap<ObjectId, Map<ObjectId, N33_JXBEntry>>();

		for (Map<String, Object> lt : lists) {
			String cid = (String) lt.get("ciId");
//        String cid="5ad859cf8fb25af5a4779221";
			//学科
			List<N33_KSDTO> subjectCid = new ArrayList<N33_KSDTO>();
			for (N33_KSDTO ksdto : gradeSubject) {
				if (ksdto.getXqid().equals(cid)) {
					subjectCid.add(ksdto);
				}
			}
			gradeSubjectCidMap.put(new ObjectId(cid), subjectCid);
			// 教学班
			Map<ObjectId, N33_JXBEntry> jxbEntryMap1 = new HashMap<ObjectId, N33_JXBEntry>();
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				if (jxbEntry.getTermId().toString().equals(cid)) {
					jxbEntryMap1.put(jxbEntry.getID(), jxbEntry);
				}
			}
			jxbEntryMap.put(new ObjectId(cid), jxbEntryMap1);
			//教室
			List<N33_ClassroomEntry> jsList = new ArrayList<N33_ClassroomEntry>();
			for (N33_ClassroomEntry js : classRoomDTO) {
				if (js.getXQId().toString().equals(cid)) {
					jsList.add(js);
				}
			}
			classRoomList.put(new ObjectId(cid), jsList);
		}

		//TermEntry termEntry = termDao.getTermByTimeId(xqid);
		//教师事务
		List<N33_SWDTO> teaSwdtos = swService.getSwByXqidAndUserId(xqid, teacherId);

		//固定事务
		List<N33_SWDTO> gdSwdtos = swService.getGuDingShiWuBySchoolId(xqid, schoolId);

		Map<String, N33_ZKBDTO> zkbMap = new HashMap<String, N33_ZKBDTO>();

		for (N33_ZKBEntry entry : entries) {
			Integer count = week % 2;
			N33_ZKBDTO dto = new N33_ZKBDTO(entry);
			dto.setSwType(0);
			dto.setIsSWAndCourse(0);
			List<N33_KSDTO> ksDtos = gradeSubjectCidMap.get(entry.getCId());
			if (entry.getType() != 5) {
				for (N33_KSDTO ksdto : ksDtos) {
					//获取学科
					if(count == 0 && entry.getType() == 6){
						if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							dto.setSubjectName(ksdto.getSnm());
							break;
						}
					}else{
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							dto.setSubjectName(ksdto.getSnm());
							break;
						}
					}

				}
			}

			if (entry.getType() != 4 && entry.getType() != 5) {
				//教学班
				Map<ObjectId, N33_JXBEntry> jxbEntryMap2 = jxbEntryMap.get(entry.getCId());
				N33_JXBEntry entry1 = jxbEntryMap2.get(entry.getJxbId());
				if (entry1 != null) {
					if (StringUtils.isNotEmpty(entry1.getNickName())) {
						dto.setJxbName(entry1.getNickName());
					} else {
						dto.setJxbName(entry1.getName());
					}
					dto.setStudentCount(entry1.getStudentIds().size());
				}
				//教室
				List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
				for (N33_ClassroomEntry room : classRoomDTOList) {
					//获取教室
					if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
						dto.setRoomName(room.getRoomName());
						break;
					}
				}
				if (entry.getNJxbId() != null) {
					if (count == 0) {
						if (entry.getNTeacherId().toString().equals(teacherId.toString())) {
							zkbMap.put(dto.getX() + "" + dto.getY(), dto);
						}
					} else {
						if (entry.getTeacherId().toString().equals(teacherId.toString())) {
							zkbMap.put(dto.getX() + "" + dto.getY(), dto);
						}
					}
				} else {
					zkbMap.put(dto.getX() + "" + dto.getY(), dto);
				}
			} else {
				if (entry.getType() == 4) {
					List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
					for (N33_ZhuanXiangEntry entry1 : zhuanList) {
						if (entry1.getTeaId().toString().equals(teacherId.toString())) {
							dto.setJxbName(entry1.getName());
							dto.setStudentCount(entry1.getStudentId().size());
							List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
							for (N33_ClassroomEntry room : classRoomDTOList) {
								//获取教室
								if (room.getRoomId().toString().equals(entry1.getRoomId().toString())) {
									dto.setRoomName(room.getRoomName());
									break;
								}
							}
							zkbMap.put(dto.getX() + "" + dto.getY(), dto);
						}
					}
				} else {
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					dto.setJxbName(entry1.getName());
					dto.setStudentCount(entry1.getStudentIds().size());
					dto.setSubjectName("自习课");
					List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
					for (N33_ClassroomEntry room : classRoomDTOList) {
						//获取教室
						if (room.getRoomId().toString().equals(entry1.getClassroomId().toString())) {
							dto.setRoomName(room.getRoomName());
							break;
						}
					}
					zkbMap.put(dto.getX() + "" + dto.getY(), dto);
				}
			}
		}

		List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, schoolId, teaGradeIds);
		for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
			if (zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY()) != null) {
				N33_ZKBDTO zkbdto = zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY());
				zkbdto.setIsSWAndCourse(1);
				zkbdto.setSwType(1);
				if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
					String s = zkbdto.getSwDesc();
					s += "\r\n";
					s += gdsxEntry.getDesc();
					zkbdto.setSwDesc(s);
				} else {
					zkbdto.setSwDesc(gdsxEntry.getDesc());
				}
				zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
			} else {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setSwType(1);
				zkbdto.setIsSWAndCourse(0);
				zkbdto.setSwDesc(gdsxEntry.getDesc());
				zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
			}
		}

		for (N33_SWDTO dto : teaSwdtos) {
			if (zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1)) != null) {
				N33_ZKBDTO zkbdto = zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1));
				zkbdto.setIsSWAndCourse(1);
				zkbdto.setSwType(1);
				if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
					String s = zkbdto.getSwDesc();
					s += "\r\n";
					s += dto.getDesc();
					zkbdto.setSwDesc(s);
				} else {
					zkbdto.setSwDesc(dto.getDesc());
				}
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			} else {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setIsSWAndCourse(0);
				zkbdto.setSwType(1);
				zkbdto.setSwDesc(dto.getDesc());
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			}
		}

		for (N33_SWDTO dto : gdSwdtos) {
			if (zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1)) != null) {
				N33_ZKBDTO zkbdto = zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1));
				zkbdto.setIsSWAndCourse(1);
				zkbdto.setSwType(1);
				if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
					String s = zkbdto.getSwDesc();
					s += "\r\n";
					s += dto.getDesc();
					zkbdto.setSwDesc(s);
				} else {
					zkbdto.setSwDesc(dto.getDesc());
				}
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			} else {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setIsSWAndCourse(0);
				zkbdto.setSwType(1);
				zkbdto.setSwDesc(dto.getDesc());
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			}
		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(userName1 + "课表");
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 7));
		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
		sheet.setColumnWidth((short) 1, (short) 5500);
		sheet.setColumnWidth((short) 2, (short) 5500);
		sheet.setColumnWidth((short) 3, (short) 5500);
		sheet.setColumnWidth((short) 4, (short) 5500);
		sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
		sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
		sheet.setColumnWidth((short) 7, (short) 5500);

		HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
		headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
		headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
		headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
		headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle1.setWrapText(true);

		HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
		headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
		headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		headerStyle2.setWrapText(true);


		HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
		headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
		headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle3.setWrapText(true);

		//为sheet1生成第一行，用于放表头信息
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeightInPoints(40);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(headerStyle1);
		cell1.setCellValue(termEntry.getXqName() + "  " + userName + "  课表");

		//为sheet1生成第二行，用于放表头信息
		HSSFRow row = sheet.createRow(1);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("课节 / 日");
		List<String> weekLists = new ArrayList<String>();
		weekLists.add("周一");
		weekLists.add("周二");
		weekLists.add("周三");
		weekLists.add("周四");
		weekLists.add("周五");
		weekLists.add("周六");
		weekLists.add("周日");
		for (int j = 0; j < weekLists.size(); j++) {
			cell = row.createCell(j + 1);
			cell.setCellStyle(headerStyle2);
			cell.setCellValue(weekLists.get(j));
		}

		int page = 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < courseRangeDTOs.size(); i++) {
				page++;
				row = sheet.createRow(page);
				row.setHeightInPoints(50);
				cell = row.createCell(0);
				cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
				cell.setCellStyle(headerStyle2);
				for (int j = 0; j < weekLists.size(); j++) {
					cell = row.createCell(j + 1);
					cell.setCellStyle(headerStyle3);
					N33_ZKBDTO dto = zkbMap.get(j + "" + i);
					if (dto != null) {
						if (dto.getIsSWAndCourse() == 0) {
							if (dto.getSwType() == 0) {
								StringBuffer sb = new StringBuffer();
								if (dto.getJxbName() != null) {
									sb.append(dto.getJxbName()).append("（" + dto.getStudentCount() + "）\r\n");
								}
								if (dto.getSubjectName() != null) {
									sb.append(dto.getSubjectName() + "\r\n");
								}
								if (dto.getGradeId() != null) {
									sb.append(gradeMap.get(new ObjectId(dto.getGradeId())).getName() + "（" + gradeMap.get(new ObjectId(dto.getGradeId())).getJie() + "）\r\n");
								}
								if (dto.getRoomName() != null) {
									sb.append(dto.getRoomName());
								}
								String con = sb.toString();
								cell.setCellValue(con);
							} else {
								cell.setCellValue(dto.getSwDesc());
							}
						} else {
							StringBuffer sb = new StringBuffer();
							if (dto.getJxbName() != null) {
								sb.append(dto.getJxbName()).append("（" + dto.getStudentCount() + "）\r\n");
							}
							if (dto.getSubjectName() != null) {
								sb.append(dto.getSubjectName() + "\r\n");
							}
							if (dto.getGradeId() != null) {
								sb.append(gradeMap.get(new ObjectId(dto.getGradeId())).getName() + "（" + gradeMap.get(new ObjectId(dto.getGradeId())).getJie() + "）\r\n");
							}
							if (dto.getRoomName() != null) {
								sb.append(dto.getRoomName() + "\r\n");
							}
							sb.append("事务：" + dto.getSwDesc());
							String con = sb.toString();
							cell.setCellValue(con);
						}
					}
				}
			}
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(userName1 + "课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	//oz 查找学科课表
	public List<Map<String, Object>> getGradeSubTeaKb(ObjectId xqid, String gradeId, ObjectId subjectId, Integer week,ObjectId schoolId) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();//返回数据
		//学期发布次
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cids.add(paiKeTimes.getID());
			}
		}
		//查询老师
		Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findTeasByST(cids, schoolId);
		List<ObjectId> userIds = new ArrayList<ObjectId>();
		for (N33_TeaEntry teaEntry : teaEntries.values()) {
			userIds.add(teaEntry.getUserId());
		}
		Map<ObjectId, N33_TeaEntry> userMap = teaDao.getTeaMap(userIds, cids);
		//此年级此学科所有的教学班
		List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
		//jxb add
		 ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
		int acClassType = turnOffService.getAcClassType(schoolId, new ObjectId(gradeId), ciId,1);
		//jxb add			
		jxbEntries = jxbDao.getJXBList(new ObjectId(gradeId), subjectId, cids,acClassType);
		//查询此年级此学科第多少周所有的周课表
		List<N33_ZKBEntry> zkbEntries = new ArrayList<N33_ZKBEntry>(); 
		zkbEntries = zkbDao.getZKBsBySW(schoolId, gradeId, xqid, subjectId,week);
		//遍历周课表查找其中所有的自习课
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_ZKBEntry zkbEntry : zkbEntries) {
			if (zkbEntry.getType() == 5) {
				zxkIds.add(zkbEntry.getJxbId());
			}
		}
		
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			jxbEntryMap.put(jxbEntry.getID(), jxbEntry);
			jxbIds.add(jxbEntry.getID());
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxkIds);//查询自习课
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbIds);//查询专项课
		//封装数据
		Integer count = week % 2;
		for (N33_ZKBEntry entry : zkbEntries) {
			Boolean bf = true;
			if (dataList.size() > 0) {
				Integer x = Integer.parseInt(String.valueOf(entry.getX() + 1));
				Integer y = Integer.parseInt(String.valueOf(entry.getY() + 1));
				//判断坐标是否存在值
				for (Map<String, Object> lt : dataList) {
					//同一个坐标
					Integer x1 = (Integer) lt.get("x");
					Integer y1 = (Integer) lt.get("y");
					if (x1 == x && y1 == y) {//同时上课
						bf = false;
						List<String> tsName = (List<String>) lt.get("tsName");
						String name = "";
						if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}
						if (entry.getType() == 5) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}
						if (entry.getType() == 6) {
							if (count == 0) {
								if(jxbEntryMap.containsKey(entry.getNJxbId())){
									name = userMap.get(entry.getNTeacherId()).getUserName();
									N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
									name += "-" + entry1.getName();
									tsName.add(name);
								}else{
									continue;
								}
							} else {
								if(jxbEntryMap.containsKey(entry.getJxbId())){
									name = userMap.get(entry.getTeacherId()).getUserName();
									N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
									name += "-" + entry1.getName();
									tsName.add(name);
								}else{
									continue;
								}
							}	
						}
						if (entry.getType() == 4) {
							for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
								if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
									String nm = userMap.get(entry1.getTeaId()).getUserName();
									N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
									nm += "-" + entryd.getName();
									tsName.add(nm);
								}
							}
						}
						lt.put("tsName", tsName);
					}
				}
			}
			if (bf) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", entry.getX() + 1);
				map.put("y", entry.getY() + 1);
				map.put("cid",entry.getCId().toString());
				map.put("title", "查看授课老师");
				List<String> tsName = new ArrayList<String>();
				String name = "";
				if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
					name += "-" + entry1.getName();
					tsName.add(name);
				}
				if (entry.getType() == 5) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					name += "-" + entry1.getName();
					tsName.add(name);
				}
				if (entry.getType() == 6) {
					if (count == 0) {
						if(jxbEntryMap.containsKey(entry.getNJxbId())){
							name = userMap.get(entry.getNTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}else{
							continue;
						}
					} else {
						if(jxbEntryMap.containsKey(entry.getJxbId())){
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}else{
							continue;
						}
					}	
				}
				if (entry.getType() == 4) {
					for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
						if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
							String nm = userMap.get(entry1.getTeaId()).getUserName();
							N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
							nm += "-" + entryd.getName();
							tsName.add(nm);
						}
					}
				}
				map.put("tsName", tsName);
				dataList.add(map);
			}
		}
		return dataList;
	}
	
	//zj
	public List<Map<String, Object>> getGradeSubjectTeaTable(ObjectId xqid, String gradeId, ObjectId subjectId, Integer week) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cids.add(paiKeTimes.getID());
			}
		}
		//学科老师
		Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySubjectIdG(cids, subjectId, "*", gradeId);
		List<ObjectId> userIds = new ArrayList<ObjectId>();
		for (N33_TeaEntry teaEntry : teaEntries.values()) {
			userIds.add(teaEntry.getUserId());
		}
		Map<ObjectId, N33_TeaEntry> userMap = teaDao.getTeaMap(userIds, cids);
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeekTT(userIds, week, xqid, gradeId);
		zkbEntries.addAll(zkbDao.getN33_ZKBByWeekNTT(userIds, week, xqid, gradeId));
		//教学班列表 所有次的
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(termEntry.getSid()).getPaikeci();
		int acClassType = turnOffService.getAcClassType(termEntry.getSid(), new ObjectId(gradeId), ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(gradeId, subjectId, cids, userIds,acClassType);
		jxbEntries.addAll(jxbDao.getZXJXBList(gradeId, subjectId, cids,acClassType));
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
		List<ObjectId> ods = new ArrayList<ObjectId>();
		for (N33_ZKBEntry jxbEntry : zkbEntries) {
			if (jxbEntry.getType() == 5) {
				ods.add(jxbEntry.getJxbId());
			}
		}
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			jxbEntryMap.put(jxbEntry.getID(), jxbEntry);
			zxkIds.add(jxbEntry.getID());
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(ods);
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		Integer count = week % 2;
		for (N33_ZKBEntry entry : zkbEntries) {
			Boolean bf = true;
			if (list.size() > 0) {
				Integer x = Integer.parseInt(String.valueOf(entry.getX() + 1));
				Integer y = Integer.parseInt(String.valueOf(entry.getY() + 1));
				//判断坐标是否存在值
				for (Map<String, Object> lt : list) {
					//同一个坐标
					Integer x1 = (Integer) lt.get("x");
					Integer y1 = (Integer) lt.get("y");
					if (x1 == x && y1 == y) {
						bf = false;
						List<String> tsName = (List<String>) lt.get("tsName");
						String name = "";
						if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
							name += "-" + entry1.getName();
						}
						if (entry.getType() == 5) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
							name += "-" + entry1.getName();
						}
						if (entry.getType() == 6) {
							if (count == 0) {
								name = userMap.get(entry.getNTeacherId()).getUserName();
								N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
								name += "-" + entry1.getName();
							} else {
								name = userMap.get(entry.getTeacherId()).getUserName();
								N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
								name += "-" + entry1.getName();
							}
						}
						if (entry.getType() == 4) {
							for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
								if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
									String nm = userMap.get(entry1.getTeaId()).getUserName();
									N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
									nm += "-" + entryd.getName();
									tsName.add(nm);
								}
							}
						}
						if (entry.getType() != 4) {
							tsName.add(name);
						}
						lt.put("tsName", tsName);
					}
				}
			}
			if (bf) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", entry.getX() + 1);
				map.put("y", entry.getY() + 1);
				map.put("cid",entry.getCId().toString());
				map.put("title", "查看授课老师");
				List<String> tsName = new ArrayList<String>();
				String name = "";
				if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
					name += "-" + entry1.getName();
				}
				if (entry.getType() == 5) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					name += "-" + entry1.getName();
				}
				if (entry.getType() == 6) {
					if (count == 0) {
						name = userMap.get(entry.getNTeacherId()).getUserName();
						N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
						name += "-" + entry1.getName();
					} else {
						name = userMap.get(entry.getTeacherId()).getUserName();
						N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
						name += "-" + entry1.getName();
					}
				}
				if (entry.getType() == 4) {
					for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
						if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
							String nm = userMap.get(entry1.getTeaId()).getUserName();
							N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
							nm += "-" + entryd.getName();
							tsName.add(nm);
						}
					}
				}
				if (entry.getType() != 4) {
					tsName.add(name);
				}
				map.put("tsName", tsName);
				list.add(map);
			}
		}
		return list;
	}


	public void getGradeSubjectTeaTableImp(ObjectId xqid, String gradeId, ObjectId subjectId, Integer week, ObjectId sid, HttpServletResponse response) {
		Map<String, Map<String, Object>> list = new ConcurrentHashMap<String, Map<String, Object>>();
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cids.add(paiKeTimes.getID());
			}
		}
		//学科老师
		Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySubjectIdG(cids, subjectId, "*", gradeId);
		List<ObjectId> userIds = new ArrayList<ObjectId>();
		for (N33_TeaEntry teaEntry : teaEntries.values()) {
			userIds.add(teaEntry.getUserId());
		}
		Map<ObjectId, N33_TeaEntry> userMap = teaDao.getTeaMap(userIds, cids);
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeekTT(userIds, week, xqid, gradeId);
		zkbEntries.addAll(zkbDao.getN33_ZKBByWeekNTT(userIds, week, xqid, gradeId));
		//教学班列表 所有次的
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(termEntry.getSid()).getPaikeci();
		int acClassType = turnOffService.getAcClassType(termEntry.getSid(), new ObjectId(gradeId), ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(gradeId, subjectId, cids, userIds,acClassType);
		jxbEntries.addAll(jxbDao.getZXJXBList(gradeId, subjectId, cids,acClassType));
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
		List<ObjectId> ods = new ArrayList<ObjectId>();
		for (N33_ZKBEntry jxbEntry : zkbEntries) {
			if (jxbEntry.getType() == 5) {
				ods.add(jxbEntry.getJxbId());
			}
		}
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			jxbEntryMap.put(jxbEntry.getID(), jxbEntry);
			zxkIds.add(jxbEntry.getID());
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(ods);
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		Integer count = week % 2;
		for (N33_ZKBEntry entry : zkbEntries) {
			if(!subjectId.equals(entry.getSubjectId())){
				continue;
			}
			Boolean bf = true;
			if (list.size() > 0) {
				Integer x = Integer.parseInt(String.valueOf(entry.getX() + 1));
				Integer y = Integer.parseInt(String.valueOf(entry.getY() + 1));
				//判断坐标是否存在值
				for (Map<String, Object> lt : list.values()) {
					//同一个坐标
					Integer x1 = (Integer) lt.get("x");
					Integer y1 = (Integer) lt.get("y");
					if (x1 == x && y1 == y) {
						bf = false;
						List<String> tsName = (List<String>) lt.get("tsName");
						if (tsName == null) {
							tsName = new ArrayList<String>();
						}
						String name = "";
						if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
							name += "-" + entry1.getName();
						}
						if (entry.getType() == 5) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
							name += "-" + entry1.getName();
						}
						if (entry.getType() == 6) {
							if (count == 0) {
								name = userMap.get(entry.getNTeacherId()).getUserName();
								N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
								name += "-" + entry1.getName();
							} else {
								name = userMap.get(entry.getTeacherId()).getUserName();
								N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
								name += "-" + entry1.getName();
							}
						}
						if (entry.getType() == 4) {
							for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
								if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
									String nm = userMap.get(entry1.getTeaId()).getUserName();
									N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
									nm += "-" + entryd.getName();
									tsName.add(nm);
								}
							}
						}
						if (entry.getType() != 4) {
							tsName.add(name);
						}
						lt.put("tsName", tsName);
					}
				}
			}
			if (bf) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", entry.getX() + 1);
				map.put("y", entry.getY() + 1);
				map.put("title", "查看授课老师");
				List<String> tsName = new ArrayList<String>();
				String name = "";
				if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
					name += "-" + entry1.getName();
				}
				if (entry.getType() == 5) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					name += "-" + entry1.getName();
				}
				if (entry.getType() == 6) {
					if (count == 0) {
						name = userMap.get(entry.getNTeacherId()).getUserName();
						N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
						name += "-" + entry1.getName();
					} else {
						name = userMap.get(entry.getTeacherId()).getUserName();
						N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
						name += "-" + entry1.getName();
					}
				}
				if (entry.getType() == 4) {
					for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
						if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
							String nm = userMap.get(entry1.getTeaId()).getUserName();
							N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
							nm += "-" + entryd.getName();
							tsName.add(nm);
						}
					}
				}
				if (entry.getType() != 4) {
					tsName.add(name);
				}
				map.put("tsName", tsName);
				map.put("isSwAndCourse", 0);
				map.put("swType", 0);
				list.put(entry.getX() + "" + entry.getY(), map);
			}
		}
		List<N33_SWDTO> swdtos = swService.getGuDingShiWuByXqid(xqid);
		List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(xqid, sid, new ObjectId(gradeId));
		if (list != null) {
			for (N33_SWDTO swdto : swdtos) {
				boolean flag = true;
				for (Map.Entry<String, Map<String, Object>> entry1 : list.entrySet()) {
					Map<String, Object> map = entry1.getValue();
					List<String> stringList = (List<String>) map.get("tsName");
					if (new Integer((Integer) map.get("x")).intValue() == swdto.getY().intValue() && new Integer((Integer) map.get("y")).intValue() == swdto.getX().intValue() && stringList != null && stringList.size() > 0) {
						flag = false;
						map.put("isSwAndCourse", 1);
						map.put("swType", 1);
						if (map.get("SwDesc") != null) {
							String s = (String) map.get("SwDesc");
							s += "\r\n";
							s += swdto.getDesc();
							map.put("SwDesc", s);
							list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
						} else {
							map.put("SwDesc", swdto.getDesc());
							list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
						}
					} else if (new Integer((Integer) map.get("x")).intValue() == swdto.getY().intValue() && new Integer((Integer) map.get("y")).intValue() == swdto.getX().intValue() && (stringList == null || stringList.size() == 0)) {
						flag = false;
						map.put("swType", 1);
						map.put("SwDesc", swdto.getDesc());
						list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
					}
				}
				if (flag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("x", swdto.getY().intValue());
					map.put("y", swdto.getX().intValue());
					map.put("SwDesc", swdto.getDesc());
					map.put("isSwAndCourse", 0);
					map.put("swType", 1);
					list.put((swdto.getY() - 1) + "" + (swdto.getX() - 1), map);
				}
			}
		}

		if (list != null) {
			for (N33_GDSXDTO gdsxdto : gdsxdtos) {
				boolean flag = true;  
				for (Map.Entry<String, Map<String, Object>> entry1 : list.entrySet()) {
					Map<String, Object> map = entry1.getValue();
					List<String> stringList = (List<String>) map.get("tsName");
					if ((new Integer((Integer) map.get("x")).intValue() - 1) == gdsxdto.getX().intValue() && (new Integer((Integer) map.get("y") ).intValue()- 1) == gdsxdto.getY().intValue() && stringList != null && stringList.size() > 0) {
					//if ((new Integer((Integer) map.get("x")).intValue() - 1) == gdsxdto.getX().intValue() && (new Integer((Integer) map.get("y") - 1).intValue()) == gdsxdto.getY().intValue() && stringList != null && stringList.size() > 0) {
						flag = false;
						map.put("isSwAndCourse", 1);
						map.put("swType", 1);
						if (map.get("SwDesc") != null) {
							String s = (String) map.get("SwDesc");
							s += "\r\n";
							s += gdsxdto.getDesc();
							map.put("SwDesc", s);
							list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
						} else {
							map.put("SwDesc", gdsxdto.getDesc());
							list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
						}
					} else if ((new Integer((Integer) map.get("x")).intValue() - 1) == gdsxdto.getX().intValue() && (new Integer((Integer) map.get("y")).intValue() - 1) == gdsxdto.getY().intValue() && (stringList == null || stringList.size() == 0)) {
						flag = false;
						map.put("swType", 1);
						map.put("SwDesc", gdsxdto.getDesc());
						list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
					}
				}
				if (flag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("x", gdsxdto.getX() + 1);
					map.put("y", gdsxdto.getY() + 1);
					map.put("SwDesc", gdsxdto.getDesc());
					map.put("isSwAndCourse", 0);
					map.put("swType", 1);
					list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
				}
			}
		}
		//课节
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(sid.toString(), xqid);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("学科课表");
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 7));
		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
		sheet.setColumnWidth((short) 1, (short) 5500);
		sheet.setColumnWidth((short) 2, (short) 5500);
		sheet.setColumnWidth((short) 3, (short) 5500);
		sheet.setColumnWidth((short) 4, (short) 5500);
		sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
		sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
		sheet.setColumnWidth((short) 7, (short) 5500);

		HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
		headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
		headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
		headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
		headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle1.setWrapText(true);

		HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
		headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
		headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		headerStyle2.setWrapText(true);


		HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
		headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
		headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle3.setWrapText(true);

		//为sheet1生成第一行，用于放表头信息
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeightInPoints(40);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(headerStyle1);
		cell1.setCellValue(termEntry.getXqName() + "学科课表");

		//为sheet1生成第二行，用于放表头信息
		HSSFRow row = sheet.createRow(1);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("课节 / 日");
		List<String> weekLists = new ArrayList<String>();
		weekLists.add("周一");
		weekLists.add("周二");
		weekLists.add("周三");
		weekLists.add("周四");
		weekLists.add("周五");
		weekLists.add("周六");
		weekLists.add("周日");
		for (int j = 0; j < weekLists.size(); j++) {
			cell = row.createCell(j + 1);
			cell.setCellStyle(headerStyle2);
			cell.setCellValue(weekLists.get(j));
		}

		int page = 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < courseRangeDTOs.size(); i++) {
				page++;
				row = sheet.createRow(page);
				row.setHeightInPoints(50);
				cell = row.createCell(0);
				cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
				cell.setCellStyle(headerStyle2);
				for (int j = 0; j < weekLists.size(); j++) {
					cell = row.createCell(j + 1);
					cell.setCellStyle(headerStyle3);
					Map<String, Object> dto = list.get(j + "" + i);
					if (dto != null) {
						String str = "";
						List<String> list1 = (List<String>) dto.get("tsName");
						if (list1 != null && list1.size() > 0) {
							for (String s : list1) {
								str += s + ",\r\n";
							}
						}
						if (dto.get("SwDesc") != null && dto.get("SwDesc") != "") {
							if (str == "") {
								str += "事务：";
							} else {
								str += "\r\n事务：";
							}
							str += (String) dto.get("SwDesc");
						}
						cell.setCellValue(str);
					}
				}
			}
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("学科课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}


	/**
	 * 老师课表
	 *
	 * @param teacherId
	 * @param schoolId
	 * @return
	 */
	public List<Map<String, Object>> GetTeachersSettledPositionsByWeek(ObjectId teacherId, ObjectId schoolId, Integer week) {
		ObjectId xqid = getDefauleTermId(schoolId);
		List<Map<String, Object>> lists = termService.getPaikeTimesByTermId(xqid);

		List<ObjectId> cids = new ArrayList<ObjectId>();
		for (Map<String, Object> lt : lists) {
			String cid = (String) lt.get("ciId");
			cids.add(new ObjectId(cid));
		}
//      cids.add(new ObjectId("5ad859cf8fb25af5a4779221"));
		//原课表
		List<N33_ZKBEntry> entries = zkbDao.getJXB(xqid, teacherId, week);
		entries.addAll(zkbDao.getJXB(xqid, week));
		entries.addAll(zkbDao.getJXBN(xqid, teacherId, week));
		//学科列表 所有次的
		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(cids, schoolId);

		//该次排课的所有年级
		Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(cids, schoolId);

		//教学班列表 所有次的
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(cids, teacherId);
		jxbEntries.addAll(jxbDao.getZXJXBList(cids));
		//教室列表 所有次的
		List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, cids);
		//专项课Ids
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			zxkIds.add(jxbEntry.getID());
		}
		List<ObjectId> ods = new ArrayList<ObjectId>();
		for (N33_ZKBEntry jxbEntry : entries) {
			if (jxbEntry.getType() == 5) {
				ods.add(jxbEntry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(ods);
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
			if (!jxbIds.contains(entry.getJxbId())) {
				jxbIds.add(entry.getJxbId());
			}
		}
		for (ObjectId objectId : jxbIds) {
			List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
				if (entry.getJxbId().toString().equals(objectId.toString())) {
					jis.add(entry);
				}
			}
			listMap.put(objectId, jis);
		}
		//封装后的
		Map<ObjectId, List<N33_ClassroomEntry>> classRoomList = new HashMap<ObjectId, List<N33_ClassroomEntry>>();
		Map<ObjectId, List<N33_KSDTO>> gradeSubjectCidMap = new HashMap<ObjectId, List<N33_KSDTO>>();
		Map<ObjectId, Map<ObjectId, N33_JXBEntry>> jxbEntryMap = new HashMap<ObjectId, Map<ObjectId, N33_JXBEntry>>();
		for (Map<String, Object> lt : lists) {
			String cid = (String) lt.get("ciId");
//        String cid="5ad859cf8fb25af5a4779221";
			//学科
			List<N33_KSDTO> subjectCid = new ArrayList<N33_KSDTO>();
			for (N33_KSDTO ksdto : gradeSubject) {
				if (ksdto.getXqid().equals(cid)) {
					subjectCid.add(ksdto);
				}
			}
			gradeSubjectCidMap.put(new ObjectId(cid), subjectCid);
			// 教学班
			Map<ObjectId, N33_JXBEntry> jxbEntryMap1 = new HashMap<ObjectId, N33_JXBEntry>();
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				if (jxbEntry.getTermId().toString().equals(cid)) {
					jxbEntryMap1.put(jxbEntry.getID(), jxbEntry);
				}
			}
			jxbEntryMap.put(new ObjectId(cid), jxbEntryMap1);
			//教室
			List<N33_ClassroomEntry> jsList = new ArrayList<N33_ClassroomEntry>();
			for (N33_ClassroomEntry js : classRoomDTO) {
				if (js.getXQId().toString().equals(cid)) {
					jsList.add(js);
				}
			}
			classRoomList.put(new ObjectId(cid), jsList);
		}
		//返回数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ObjectId ciId = null;
		if(entries.size() > 0){
			 ciId = entries.get(0).getCId();
		}else{
			return list;
		}
		List<CourseRangeEntry> courseRangeEntryList = dao.getEntryListBySchoolId(schoolId, ciId);
		for (N33_ZKBEntry entry : entries) {
			Integer count = week % 2;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("x", entry.getX() + 1);
			map.put("y", entry.getY() + 1);
			map.put("cid",entry.getCId().toString());
			for (CourseRangeEntry courseRangeEntry : courseRangeEntryList) {
				if(courseRangeEntry.getSerial() == (entry.getY() + 1)){
					map.put("start",courseRangeEntry.getStart());
					map.put("end",courseRangeEntry.getEnd());
				}
			}
			String gradeName = "";
			if (entry.getGradeId() != null) {
				gradeName = gradeMap.get(entry.getGradeId()).getName() + "（" + gradeMap.get(entry.getGradeId()).getJie() + "）";
			}
			map.put("grade", gradeName);
			List<N33_KSDTO> dto = gradeSubjectCidMap.get(entry.getCId());
			if(dto == null){
				return list;
			}
			if (entry.getType() != 5) {
				for (N33_KSDTO ksdto : dto) {
					//获取学科
					if(count == 0 && entry.getType() == 6){
						if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							map.put("subName", ksdto.getSnm());
							break;
						}
					}else{
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							map.put("subName", ksdto.getSnm());
							break;
						}
					}
				}
			}
			if (entry.getType() != 4 && entry.getType() != 5) {
				//教学班
				Map<ObjectId, N33_JXBEntry> jxbEntryMap2 = jxbEntryMap.get(entry.getCId());
				N33_JXBEntry entry1 = null;
				if (count == 0) {
					entry1 = jxbEntryMap2.get(entry.getNJxbId());
				} else {
					entry1 = jxbEntryMap2.get(entry.getJxbId());
				}
				if (entry1 == null) {
					entry1 = jxbDao.getJXBById(entry.getJxbId());
				}
				if(entry1 == null){
					continue;
				}
				if (StringUtils.isNotEmpty(entry1.getNickName())) {
					map.put("JxbName", entry1.getNickName());
				} else {
					map.put("JxbName", entry1.getName());
				}
				map.put("count", entry1.getStudentIds().size());
				//教室
				List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
				for (N33_ClassroomEntry room : classRoomDTOList) {
					//获取学科
					if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
						map.put("roomName", room.getRoomName());
						break;
					}
				}
				if (entry.getNTeacherId() != null) {
					if (count == 0) {
						map.put("jxbId", entry.getNJxbId().toString());
						if (entry.getNTeacherId().toString().equals(teacherId.toString())) {
							list.add(map);
						}
					} else {
						if (entry.getTeacherId().toString().equals(teacherId.toString())) {
							list.add(map);
						}
					}
				} else {
					map.put("jxbId", entry.getJxbId().toString());
					list.add(map);
				}
			} else {
				if (entry.getType() == 4) {
					List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
					if (zhuanList != null) {
						for (N33_ZhuanXiangEntry entry1 : zhuanList) {
							if (entry1.getTeaId().toString().equals(teacherId.toString())) {
								map.put("jxbId", entry1.getJxbId().toString());
								map.put("JxbName", entry1.getName());
								map.put("count", entry1.getStudentId().size());
								List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
								for (N33_ClassroomEntry room : classRoomDTOList) {
									//获取学科
									if (room.getRoomId().toString().equals(entry1.getRoomId().toString())) {
										map.put("roomName", room.getRoomName());
										break;
									}
								}
								list.add(map);
							}
						}
					}
				} else {
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					if (entry1 != null) {
						map.put("JxbName", entry1.getName());
						map.put("count", entry1.getStudentIds().size());
						map.put("subName", "自习课");
						List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
						for (N33_ClassroomEntry room : classRoomDTOList) {
							//获取学科
							if (room.getRoomId().toString().equals(entry1.getClassroomId().toString())) {
								map.put("roomName", room.getRoomName());
								break;
							}
						}
						list.add(map);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 导出学生课表
	 *
	 * @param studentId
	 * @param schoolId
	 * @param week
	 * @return
	 */
	public void exportStuKB(String userName, ObjectId studentId, ObjectId schoolId,  String className,Integer week, ObjectId gid,HttpServletResponse response) {
		Integer count = week % 2;
		ObjectId xqid = getDefauleTermId(schoolId);
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);

		List<N33_ZKBEntry> zkbEntryList1 = zkbDao.getYKBsByclassRoomIds(xqid, schoolId, week, gid);
		ObjectId cid = null;
		if (zkbEntryList1.size() > 0) {
			cid = zkbEntryList1.get(0).getCId();
		} else {
			return;
		}
		//查学生教学班列表
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		studentIds.add(studentId);
		
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gid, cid,1);
		//jxb add
		
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds,acClassType);

		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry entry : jxbEntries.values()) {
			zxkIds.add(entry.getID());
		}
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
			if (!jxbIds.contains(entry.getJxbId())) {
				jxbIds.add(entry.getJxbId());
			}
		}
		for (ObjectId objectId : jxbIds) {
			List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
				if (entry.getJxbId().toString().equals(objectId.toString())) {
					jis.add(entry);
				}
			}
			listMap.put(objectId, jis);
		}

		List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries, xqid);

		for (N33_ZKBEntry entry : zkbEntryList1) {
			if (entry.getType() == 5) {
				jiaoXueBanList.add(entry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(jiaoXueBanList);
		//学科列表
		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(cid, schoolId);
		//查询教学班所在的所有周课表
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeek(jiaoXueBanList, week, xqid);
		List<N33_ZKBEntry> zkbEntries1 = zkbDao.getN33_ZKBByWeekForNJxbId(jiaoXueBanList, week, xqid);
		zkbEntries.addAll(zkbEntries1);

		//课节
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(schoolId.toString(), xqid);
		//教室列表
		List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, cid);
		//老师列表
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(cid, schoolId, gid);


		Map<String, N33_ZKBDTO> zkbMap = new HashMap<String, N33_ZKBDTO>();
		//TermEntry termEntry = termDao.getTermByTimeId(xqid);
		//固定事务
		List<N33_SWDTO> gdSwdtos = swService.getGuDingShiWuBySchoolId(xqid, schoolId);

		for (N33_ZKBEntry entry : zkbEntries) {
			N33_ZKBDTO dto = new N33_ZKBDTO(entry);
			dto.setIsSWAndCourse(0);

			if (entry.getType() != 4 && entry.getType() != 5) {
				dto.setSwType(0);
				if (entry.getNTeacherId() != null) {
					
					//双周导出双周，单周导出单周
//					if (count == 0) {
//						dto.setTeacherName(teaEntryMap.get(entry.getNTeacherId()).getUserName());
//					} else {
//						dto.setTeacherName(teaEntryMap.get(entry.getTeacherId()).getUserName());
//					}
					
					//同时导出单双周
					dto.setTeacherName(teaEntryMap.get(entry.getTeacherId()).getUserName());
					dto.setnTeacherName(teaEntryMap.get(entry.getTeacherId()).getUserName());
					
					
				} else {
					if(teaEntryMap.get(entry.getTeacherId()) != null){
						dto.setTeacherName(teaEntryMap.get(entry.getTeacherId()).getUserName());
					}else{
						dto.setTeacherName("");
					}
				}

				//教学班
				if (entry.getNTeacherId() != null) {
					
					//同时导出单双周
					N33_JXBEntry entry1 = jxbEntries.get(entry.getNJxbId());
					if (StringUtils.isNotEmpty(entry1.getNickName())) {
						dto.setnJxbName(entry1.getNickName());
					}else{
						dto.setnJxbName(entry1.getName());
					}
					N33_JXBEntry entry2 = jxbEntries.get(entry.getJxbId());
					if (StringUtils.isNotEmpty(entry2.getNickName())) {
						dto.setJxbName(entry2.getNickName());
					}else{
						dto.setJxbName(entry2.getName());
					}
					//双周导出双周，单周导出单周
//					if (count == 0) {
//						N33_JXBEntry entry1 = jxbEntries.get(entry.getNJxbId());
//						if (StringUtils.isNotEmpty(entry1.getNickName())) {
//							dto.setJxbName(entry1.getNickName());
//						} else {
//							dto.setJxbName(entry1.getName());
//						}
//					} else {
//						N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
//						if (StringUtils.isNotEmpty(entry1.getNickName())) {
//							dto.setJxbName(entry1.getNickName());
//						} else {
//							dto.setJxbName(entry1.getName());
//						}
//					}
				} else {
					N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
					if (StringUtils.isNotEmpty(entry1.getNickName())) {
						dto.setJxbName(entry1.getNickName());
					} else {
						dto.setJxbName(entry1.getNickName());
					}
				}
			} else {
				if (entry.getType() == 4) {
					List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
					for (N33_ZhuanXiangEntry entry1 : zhuanList) {
						if (entry1.getStudentId().contains(studentId)) {
							dto.setJxbName(entry1.getName());
							dto.setTeacherName(teaEntryMap.get(entry1.getTeaId()).getUserName());
							for (N33_ClassroomEntry room : classRoomDTO) {
								//获取学科
								if (room.getRoomId().toString().equals(entry1.getRoomId().toString())) {
									dto.setRoomName(room.getRoomName());
									break;
								}
							}

						}
					}
				} else {
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					if(!entry1.getStudentIds().contains(studentId)){
						continue;
					}
					if (entry1.getStudentIds().contains(studentId)) {
						dto.setJxbName(entry1.getName());
						dto.setTeacherName("");
						N33_TeaEntry teaEntry = teaEntryMap.get(entry1.getTeacherId());
						if (teaEntry != null) {
							dto.setTeacherName(teaEntry.getUserName());
						}
						dto.setSubjectName("自习课");
						for (N33_ClassroomEntry room : classRoomDTO) {
							//获取学科
							if (room.getRoomId().toString().equals(entry1.getClassroomId().toString())) {
								dto.setRoomName(room.getRoomName());
								break;
							}
						}
					}
				}
			}
			if (entry.getType() != 5) {
				for (N33_KSDTO ksdto : gradeSubject) {
					//获取学科
					if (entry.getNTeacherId() != null) {
						if (count == 0) {
							if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								dto.setSubjectName(ksdto.getSnm());
								break;
							}
						} else {
							if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								dto.setSubjectName(ksdto.getSnm());
								break;
							}
						}
					} else {
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							dto.setSubjectName(ksdto.getSnm());
							break;
						}
					}
				}
			}
			//教室
			if (entry.getType() != 4 && entry.getType() != 5) {
				for (N33_ClassroomEntry room : classRoomDTO) {
					//获取学科
					if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
						dto.setRoomName(room.getRoomName());
						break;
					}
				}
			}
			zkbMap.put(dto.getX() + "" + dto.getY(), dto);
		}

		List<ObjectId> gradeIds = new ArrayList<ObjectId>();
		gradeIds.add(gid);
		List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, schoolId, gradeIds);
		for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
			if (zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY()) != null) {
				N33_ZKBDTO zkbdto = zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY());
				zkbdto.setIsSWAndCourse(1);
				zkbdto.setSwType(1);
				if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
					String s = zkbdto.getSwDesc();
					s += "\r\n";
					s += gdsxEntry.getDesc();
					zkbdto.setSwDesc(s);
				} else {
					zkbdto.setSwDesc(gdsxEntry.getDesc());
				}
				zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
			} else {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setSwType(1);
				zkbdto.setIsSWAndCourse(0);
				zkbdto.setSwDesc(gdsxEntry.getDesc());
				zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
			}
		}

		for (N33_SWDTO dto : gdSwdtos) {
			if (zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1)) != null) {
				N33_ZKBDTO zkbdto = zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1));
				zkbdto.setIsSWAndCourse(1);
				zkbdto.setSwType(1);
				if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
					String s = zkbdto.getSwDesc();
					s += "\r\n";
					if (dto.getSk() == 1) {
						s += "自习课";
					} else {
						s += dto.getDesc();
					}
					zkbdto.setSwDesc(s);
				} else {
					if (dto.getSk() == 1) {
						zkbdto.setSwDesc("自习课");
					} else {
						zkbdto.setSwDesc(dto.getDesc());
					}
				}
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			} else {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setIsSWAndCourse(0);
				zkbdto.setSwType(1);
				if (dto.getSk() == 1) {
					zkbdto.setSwDesc("自习课");
				} else {
					zkbdto.setSwDesc(dto.getDesc());
				}
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			}
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(userName + "课表");

		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 7));
		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
		sheet.setColumnWidth((short) 1, (short) 5500);
		sheet.setColumnWidth((short) 2, (short) 5500);
		sheet.setColumnWidth((short) 3, (short) 5500);
		sheet.setColumnWidth((short) 4, (short) 5500);
		sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
		sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
		sheet.setColumnWidth((short) 7, (short) 5500);

		HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
		headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
		headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
		headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
		headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle1.setWrapText(true);

		HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
		headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
		headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		headerStyle2.setWrapText(true);


		HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
		headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
		headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle3.setWrapText(true);

		//为sheet1生成第一行，用于放表头信息
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeightInPoints(40);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(headerStyle1);
		cell1.setCellValue(termEntry.getXqName() + "  "  + className + "   " + userName + "   " + "第(" + week + ")周"  +"  课表");


		//为sheet1生成第一行，用于放表头信息
		HSSFRow row = sheet.createRow(1);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("课节 / 日");
		List<String> weekLists = new ArrayList<String>();
		weekLists.add("周一");
		weekLists.add("周二");
		weekLists.add("周三");
		weekLists.add("周四");
		weekLists.add("周五");
		weekLists.add("周六");
		weekLists.add("周日");
		for (int j = 0; j < weekLists.size(); j++) {
			cell = row.createCell(j + 1);
			cell.setCellStyle(headerStyle2);
			cell.setCellValue(weekLists.get(j));
		}

		int page = 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < courseRangeDTOs.size(); i++) {
				page++;
				row = sheet.createRow(page);
				row.setHeightInPoints(50);
				cell = row.createCell(0);
				cell.setCellStyle(headerStyle2);
				cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
				for (int j = 0; j < weekLists.size(); j++) {
					cell = row.createCell(j + 1);
					cell.setCellStyle(headerStyle3);
					N33_ZKBDTO dto = zkbMap.get(j + "" + i);
					if (dto != null) {
						if (dto.getIsSWAndCourse() == 0) {
							if (dto.getSwType() == 0) {
								StringBuffer sb = new StringBuffer();
								if (dto.getJxbName() != null) {
									if(!"".equals(dto.getnJxbName()) && dto.getnJxbName() != null){
										sb.append(dto.getJxbName() + "（单）/" + dto.getnJxbName() + "(双)" + "\r\n");
									}else{
										sb.append(dto.getJxbName() + "\r\n");
				
									}
									//sb.append(dto.getJxbName() + "\r\n");
								}
								if (dto.getTeacherName() != null) {
									//同时导出单双周
									if(!"".equals(dto.getnTeacherName()) && dto.getnTeacherName() != null){
										sb.append(dto.getTeacherName() + "（单）/" + dto.getnTeacherName() + "(双)" + "\r\n");
									}else{
										sb.append(dto.getTeacherName() + "\r\n");
									}
									//双周导出双周，单周导出单周
									//sb.append(dto.getTeacherName() + "\r\n");
								}
								if (dto.getSubjectName() != null) {
									sb.append(dto.getSubjectName() + "\r\n");
								}
								if (dto.getRoomName() != null) {
									sb.append(dto.getRoomName());
								}
								String con = sb.toString();
								cell.setCellValue(con);
							} else {
								cell.setCellValue(dto.getSwDesc());
							}
						} else {
							StringBuffer sb = new StringBuffer();
							if (dto.getJxbName() != null) {
								sb.append(dto.getJxbName() + "\r\n");
							}
							if (dto.getTeacherName() != null) {
								sb.append(dto.getTeacherName() + "\r\n");
							}
							if (dto.getSubjectName() != null) {
								sb.append(dto.getSubjectName() + "\r\n");
							}
							if (dto.getRoomName() != null) {
								sb.append(dto.getRoomName() + "\r\n");
							}
							sb.append("事务：" + dto.getSwDesc());
							String con = sb.toString();
							cell.setCellValue(con);
						}
					}
				}
			}
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(userName + "课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}



	/**
	 * 学生课表
	 *
	 * @param studentId
	 * @param schoolId
	 * @return
	 */
	public List<Map<String, Object>> GetStudentSettledPositionsByWeek(ObjectId studentId, ObjectId schoolId, Integer week, ObjectId gradeId) {
		Integer count = week % 2;
		ObjectId xqid = getDefauleTermId(schoolId);
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds(xqid, schoolId, week, gradeId);
		if (ykbEntryList1 == null || ykbEntryList1.size() == 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("message", "该周课表未发布");
			List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
			retList.add(retMap);
			return retList;
		}
		ObjectId cid = ykbEntryList1.get(0).getCId();
		//查学生教学班列表
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		studentIds.add(studentId);
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, cid,1);
		//jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds,acClassType);
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry entry : jxbEntries.values()) {
			zxkIds.add(entry.getID());
		}
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
			if (!jxbIds.contains(entry.getJxbId())) {
				jxbIds.add(entry.getJxbId());
			}
		}
		for (ObjectId objectId : jxbIds) {
			List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
				if (entry.getJxbId().toString().equals(objectId.toString())) {
					jis.add(entry);
				}
			}
			listMap.put(objectId, jis);
		}
		List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries, xqid);
		for (N33_ZKBEntry entry : ykbEntryList1) {
			if (entry.getType() == 5) {
				jiaoXueBanList.add(entry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(jiaoXueBanList);
		//学科列表
		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(cid, schoolId);
		//查询教学班所在的所有周课表
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeek(jiaoXueBanList, week, xqid);
		List<N33_ZKBEntry> zkbEntries1 = zkbDao.getN33_ZKBByWeekForNJxbId(jiaoXueBanList, week, xqid);
		zkbEntries.addAll(zkbEntries1);
		if (zkbEntries.size() > 0) {
			//教室列表
			List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, cid);
			//老师列表
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(cid, schoolId, gradeId);
			//判定走班课和非走班课程
			//返回数据
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (N33_ZKBEntry entry : zkbEntries) {
				if (entry.getJxbId() == null) {
					continue;
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", entry.getX() + 1);
				map.put("y", entry.getY() + 1);
				map.put("cid",entry.getCId().toString());
				if (entry.getType() != 4 && entry.getType() != 5) {
					if (entry.getNTeacherId() != null) {
						if (count == 0) {
							if(teaEntryMap.get(entry.getNTeacherId())==null){
								map.put("teaName", "");
							}else {
								map.put("teaName", teaEntryMap.get(entry.getNTeacherId()).getUserName());
							}
						} else {
							if(teaEntryMap.get(entry.getTeacherId())==null){
								map.put("teaName", "");
							}else {
								map.put("teaName", teaEntryMap.get(entry.getTeacherId()).getUserName());
							}
						}
					} else {
						if(teaEntryMap.get(entry.getTeacherId())==null){
							map.put("teaName", "");
						}else {
							map.put("teaName", teaEntryMap.get(entry.getTeacherId()).getUserName());
						}
					}
					//教学班
					if (entry.getNTeacherId() != null) {
						if (count == 0) {
							N33_JXBEntry entry1 = jxbEntries.get(entry.getNJxbId());
							if (StringUtils.isNotEmpty(entry1.getNickName())) {
								map.put("JxbName", entry1.getNickName());
							} else {
								map.put("JxbName", entry1.getName());
							}
						} else {
							N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
							if (StringUtils.isNotEmpty(entry1.getNickName())) {
								map.put("JxbName", entry1.getNickName());
							} else {
								map.put("JxbName", entry1.getName());
							}
						}
					} else {
						N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
						if (StringUtils.isNotEmpty(entry1.getNickName())) {
							map.put("JxbName", entry1.getNickName());
						} else {
							map.put("JxbName", entry1.getName());
						}
					}

				} else {
					if (entry.getType() == 4) {
						List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
						for (N33_ZhuanXiangEntry entry1 : zhuanList) {
							if (entry1.getStudentId().contains(studentId)) {
								map.put("JxbName", entry1.getName());
								map.put("teaName", teaEntryMap.get(entry1.getTeaId()).getUserName());
								for (N33_ClassroomEntry room : classRoomDTO) {
									//获取学科
									if (room.getRoomId().toString().equals(entry1.getRoomId().toString())) {
										map.put("roomName", room.getRoomName());
										break;
									}
								}
								list.add(map);
							}
						}
					} else {
						N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
						if(!entry1.getStudentIds().contains(studentId)){
							continue;
						}
						if (entry1.getStudentIds().contains(studentId)) {
							map.put("JxbName", entry1.getName());
							map.put("teaName", "");
							N33_TeaEntry teaEntry = teaEntryMap.get(entry1.getTeacherId());
							if (teaEntry != null) {
								map.put("teaName", teaEntry.getUserName());
							}
							map.put("subName", "自习课");
							for (N33_ClassroomEntry room : classRoomDTO) {
								//获取学科
								if (room.getRoomId().toString().equals(entry1.getClassroomId().toString())) {
									map.put("roomName", room.getRoomName());
									break;
								}
							}
							list.add(map);
						}
					}
				}
				if (entry.getType() != 5) {
					for (N33_KSDTO ksdto : gradeSubject) {
						//获取学科
						if (entry.getNTeacherId() != null) {
							if (count == 0) {
								if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
									map.put("subName", ksdto.getSnm());
									break;
								}
							} else {
								if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
									map.put("subName", ksdto.getSnm());
									break;
								}
							}
						} else {
							if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								map.put("subName", ksdto.getSnm());
								break;
							}
						}
					}
				}
				if (entry.getType() != 4 && entry.getType() != 5) {
					//教室
					for (N33_ClassroomEntry room : classRoomDTO) {
						//获取学科
						if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
							map.put("roomName", room.getRoomName());
							break;
						}
					}
					list.add(map);
				}
			}
			return list;
		}
		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * 判断是否可以导出课表
	 *
	 * @param xqid
	 * @param schoolId
	 * @param week
	 * @param gradeId
	 * @return
	 */
	public boolean isCanExport(ObjectId xqid, ObjectId schoolId, Integer week, ObjectId gradeId) {
		List<N33_ZKBEntry> ykbEntryList = zkbDao.getYKBsByclassRoomIds(xqid, schoolId, week, gradeId);
		if (ykbEntryList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 导出行政班课表
	 *
	 * @param classId
	 * @param xqid
	 * @param gradeId
	 * @param schoolId
	 * @param week
	 */
	public void exportXZBKB(String className, ObjectId classId, ObjectId xqid, ObjectId gradeId, ObjectId schoolId, Integer week,boolean isChecked, HttpServletResponse response) {
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		Integer count = week % 2;
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds(xqid, schoolId, week, gradeId);
		ObjectId cid = null;
		if (ykbEntryList1.size() > 0) {
			cid = ykbEntryList1.get(0).getCId();
		} else {
			return;
		}
		//查行政班学生
		List<ObjectId> studentIds = getStudentList(classId, cid);
		//查询老师Map
		Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(cid,schoolId);
		//查年级学科
		Map<String, N33_KSDTO> stringN33_ksdtoMap = subjectService.getIsolateSubjectByGradeIdMap(cid, schoolId, gradeId.toHexString());
		//查学生教学班列表
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, cid,1);
		//jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds, acClassType);
		List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries, xqid);
		//查询教学班所在的所有周课表
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeek(jiaoXueBanList, week, xqid);

		//课节
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(schoolId.toString(), xqid);

		Map<String, String> swMap = new HashMap<String, String>();

		Map<String, N33_ZKBDTO> zkbMap = new HashMap<String, N33_ZKBDTO>();
		//TermEntry termEntry = termDao.getTermByTimeId(xqid);
		//固定事务
		List<N33_SWDTO> gdSwdtos = swService.getGuDingShiWuBySchoolId(xqid, schoolId);

		//判断是否是走班
		List<N33_ZKBDTO> zkblist = new ArrayList<N33_ZKBDTO>();

		for (N33_ZKBEntry entry : zkbEntries) {


			if (zkblist.size() > 0) {
				Integer x = Integer.parseInt(String.valueOf(entry.getX()));
				Integer y = Integer.parseInt(String.valueOf(entry.getY()));
				//是否可以添加
				Boolean bf = true;
				//判断坐标是否存在值
				for (N33_ZKBDTO dto : zkblist) {
					//同一个坐标
					Integer x1 = (Integer) dto.getX();
					Integer y1 = (Integer) dto.getY();

					if (x1 == x && y1 == y) {
						String jList = dto.getJxbName();
						//单双周
						if (entry.getNTeacherId() == null) {
							if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
								jList += "\r\n" + jxbEntries.get(entry.getJxbId()).getNickName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName());
							} else {
								jList += "\r\n" + jxbEntries.get(entry.getJxbId()).getName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName());
							}
							if(jxbEntries.get(entry.getJxbId()).getType() == 1){
								List<String> zoubanType = dto.getZouBanType();
								if(!dto.getZouBanType().contains("等级走班")){
									zoubanType.add("等级走班");
									dto.setZouBanType(zoubanType);
								}
							}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
								List<String> zoubanType = dto.getZouBanType();
								if(!dto.getZouBanType().contains("合格走班")){
									zoubanType.add("合格走班");
									dto.setZouBanType(zoubanType);
								}
							}
							if(isChecked == false){
								List<String> zoubanType = dto.getZouBanType();
								String zoubanString = "";
								for (String s : zoubanType) {
									zoubanString += s;
									zoubanString += "\r\n";
								}
								dto.setJxbName(zoubanString);
							}else{
								dto.setJxbName(jList);
							}
							bf = false;
							break;
						} else {
							if (count == 0) {
								if (StringUtils.isNotEmpty(jxbEntries.get(entry.getNJxbId()).getNickName())) {
									jList += "\r\n" + jxbEntries.get(entry.getNJxbId()).getNickName() + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName());
								} else {
									jList += "\r\n" + jxbEntries.get(entry.getNJxbId()).getName() + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName());
								}
								if(jxbEntries.get(entry.getNJxbId()).getType() == 1){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("等级走班")){
										zoubanType.add("等级走班");
										dto.setZouBanType(zoubanType);
									}
								}else if(jxbEntries.get(entry.getNJxbId()).getType() == 2){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("合格走班")){
										zoubanType.add("合格走班");
										dto.setZouBanType(zoubanType);
									}
								}
							} else {
								if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
									jList += "\r\n" + jxbEntries.get(entry.getJxbId()).getNickName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName());
								} else {
									jList += "\r\n" + jxbEntries.get(entry.getJxbId()).getName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName());
								}
								if(jxbEntries.get(entry.getJxbId()).getType() == 1){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("等级走班")){
										zoubanType.add("等级走班");
										dto.setZouBanType(zoubanType);
									}
								}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("合格走班")){
										zoubanType.add("合格走班");
										dto.setZouBanType(zoubanType);
									}
								}
							}
							if(isChecked == false){
								List<String> zoubanType = dto.getZouBanType();
								String zoubanString = "";
								for (String s : zoubanType) {
									zoubanString += s;
									zoubanString += "\r\n";
								}
								dto.setJxbName(zoubanString);
							}else{
								dto.setJxbName(jList);
							}
							bf = false;
							break;
						}
					}
				}
				if (bf) {
					N33_ZKBDTO dto = new N33_ZKBDTO(entry);
					dto.setSwType(0);
					dto.setIsSWAndCourse(0);

					if (stringN33_ksdtoMap != null) {
						dto.setSubjectName(stringN33_ksdtoMap.get(entry.getSubjectId().toString()).getSnm());
					}

					if (entry.getNJxbId() != null) {
						if (count == 0) {
							if (jxbEntries.get(entry.getNJxbId()).getType() == 1 || jxbEntries.get(entry.getNJxbId()).getType() == 2) {
								dto.setType(2);
							} else {
								dto.setType(1);
							}
							if (StringUtils.isNotEmpty(jxbEntries.get(entry.getNJxbId()).getNickName())) {
								if(jxbEntries.get(entry.getNJxbId()).getType() == 1 || jxbEntries.get(entry.getNJxbId()).getType() == 2){
									if(isChecked == false){
										if(jxbEntries.get(entry.getNJxbId()).getType() == 1){
											dto.setJxbName("等级走班");
										}else if(jxbEntries.get(entry.getNJxbId()).getType() == 2){
											dto.setJxbName("合格走班");
										}
									}else{
										dto.setJxbName(jxbEntries.get(entry.getNJxbId()).getNickName() + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()));
									}
									if(jxbEntries.get(entry.getNJxbId()).getType() == 1){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("等级走班")){
											zoubanType.add("等级走班");
											dto.setZouBanType(zoubanType);
										}
									}else if(jxbEntries.get(entry.getNJxbId()).getType() == 2){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("合格走班")){
											zoubanType.add("合格走班");
											dto.setZouBanType(zoubanType);
										}
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getNJxbId()).getNickName() +"\r\n"+ (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()));
								}
							} else {
								if(jxbEntries.get(entry.getNJxbId()).getType() == 1 || jxbEntries.get(entry.getNJxbId()).getType() == 2){
									if(isChecked == false){
										if(jxbEntries.get(entry.getNJxbId()).getType() == 1){
											dto.setJxbName("等级走班");
										}else if(jxbEntries.get(entry.getNJxbId()).getType() == 2){
											dto.setJxbName("合格走班");
										}
									}else{
										dto.setJxbName(jxbEntries.get(entry.getNJxbId()).getName() + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()));
									}
									if(jxbEntries.get(entry.getNJxbId()).getType() == 1){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("等级走班")){
											zoubanType.add("等级走班");
											dto.setZouBanType(zoubanType);
										}
									}else if(jxbEntries.get(entry.getNJxbId()).getType() == 2){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("合格走班")){
											zoubanType.add("合格走班");
											dto.setZouBanType(zoubanType);
										}
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getNJxbId()).getName() +"\r\n"+ (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()));
								}
							}
							dto.setStudentCount(jxbEntries.get(entry.getNJxbId()).getStudentIds().size());
							if ((zkbMap.get(entry.getX() + "" + entry.getY()) != null)) {
								continue;
							} else {
								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
							}
						} else {
							if (jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2) {
								dto.setType(2);
							} else {
								dto.setType(1);
							}
							if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
								if(jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2){
									if(isChecked == false){
										if(jxbEntries.get(entry.getJxbId()).getType() == 1){
											dto.setJxbName("等级走班");
										}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
											dto.setJxbName("合格走班");
										}
									}else{
										dto.setJxbName(jxbEntries.get(entry.getJxbId()).getNickName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
									}
									if(jxbEntries.get(entry.getJxbId()).getType() == 1){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("等级走班")){
											zoubanType.add("等级走班");
											dto.setZouBanType(zoubanType);
										}
									}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("合格走班")){
											zoubanType.add("合格走班");
											dto.setZouBanType(zoubanType);
										}
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getJxbId()).getNickName() + "\r\n" + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
								}
							} else {
								if(jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2){
									if(isChecked == false){
										if(jxbEntries.get(entry.getJxbId()).getType() == 1){
											dto.setJxbName("等级走班");
										}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
											dto.setJxbName("合格走班");
										}
									}else{
										dto.setJxbName(jxbEntries.get(entry.getJxbId()).getName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
									}
									if(jxbEntries.get(entry.getJxbId()).getType() == 1){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("等级走班")){
											zoubanType.add("等级走班");
											dto.setZouBanType(zoubanType);
										}
									}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("合格走班")){
											zoubanType.add("合格走班");
											dto.setZouBanType(zoubanType);
										}
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getJxbId()).getName() + "\r\n" + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
								}
							}
							dto.setStudentCount(jxbEntries.get(entry.getJxbId()).getStudentIds().size());
							if ((zkbMap.get(entry.getX() + "" + entry.getY()) != null)) {
								continue;
							} else {
								zkbMap.put(dto.getX() + "" + dto.getY(), dto);
							}
						}
					} else {
						if (jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2) {
							dto.setType(2);
						} else {
							dto.setType(1);
						}
						if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
							if(jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2){
								if(isChecked == false){
									if(jxbEntries.get(entry.getJxbId()).getType() == 1){
										dto.setJxbName("等级走班");
									}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
										dto.setJxbName("合格走班");
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getJxbId()).getNickName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
								}
								if(jxbEntries.get(entry.getJxbId()).getType() == 1){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("等级走班")){
										zoubanType.add("等级走班");
										dto.setZouBanType(zoubanType);
									}
								}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("合格走班")){
										zoubanType.add("合格走班");
										dto.setZouBanType(zoubanType);
									}
								}
							}else{
								dto.setJxbName(jxbEntries.get(entry.getJxbId()).getNickName() + "\r\n" + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
							}
						} else {
							if(jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2){
								if(isChecked == false){
									if(jxbEntries.get(entry.getJxbId()).getType() == 1){
										dto.setJxbName("等级走班");
									}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
										dto.setJxbName("合格走班");
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getJxbId()).getName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
								}
								if(jxbEntries.get(entry.getJxbId()).getType() == 1){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("等级走班")){
										zoubanType.add("等级走班");
										dto.setZouBanType(zoubanType);
									}
								}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("合格走班")){
										zoubanType.add("合格走班");
										dto.setZouBanType(zoubanType);
									}
								}
							}else{
								dto.setJxbName(jxbEntries.get(entry.getJxbId()).getName() + "\r\n" + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
							}
						}
						dto.setStudentCount(jxbEntries.get(entry.getJxbId()).getStudentIds().size());
						if ((zkbMap.get(entry.getX() + "" + entry.getY()) != null)) {
							continue;
						} else {
							zkbMap.put(dto.getX() + "" + dto.getY(), dto);
						}
					}
					zkblist.add(dto);
				}
			} else {
				N33_ZKBDTO dto = new N33_ZKBDTO(entry);
				dto.setIsSWAndCourse(0);
				dto.setSwType(0);

				if (stringN33_ksdtoMap != null) {
					dto.setSubjectName(stringN33_ksdtoMap.get(entry.getSubjectId().toString()).getSnm());
				}

				if (entry.getNJxbId() != null) {
					if (count == 0) {
						if (jxbEntries.get(entry.getNJxbId()).getType() == 1 || jxbEntries.get(entry.getNJxbId()).getType() == 2) {
							dto.setType(2);
						} else {
							dto.setType(1);
						}
						if (StringUtils.isNotEmpty(jxbEntries.get(entry.getNJxbId()).getNickName())) {
							if(jxbEntries.get(entry.getNJxbId()).getType() == 1 || jxbEntries.get(entry.getNJxbId()).getType() == 2){
								if(isChecked == false){
									if(jxbEntries.get(entry.getNJxbId()).getType() == 1){
										dto.setJxbName("等级走班");
									}else if(jxbEntries.get(entry.getNJxbId()).getType() == 2){
										dto.setJxbName("合格走班");
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getNJxbId()).getNickName() + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()));
								}
								if(jxbEntries.get(entry.getNJxbId()).getType() == 1){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("等级走班")){
										zoubanType.add("等级走班");
										dto.setZouBanType(zoubanType);
									}
								}else if(jxbEntries.get(entry.getNJxbId()).getType() == 2){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("合格走班")){
										zoubanType.add("合格走班");
										dto.setZouBanType(zoubanType);
									}
								}
							}else{
								dto.setJxbName(jxbEntries.get(entry.getNJxbId()).getNickName() + "\r\n" + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()));
							}
						} else {
							if(jxbEntries.get(entry.getNJxbId()).getType() == 1 || jxbEntries.get(entry.getNJxbId()).getType() == 2){
								if(isChecked == false){
									if(jxbEntries.get(entry.getNJxbId()).getType() == 1){
										dto.setJxbName("等级走班");
									}else if(jxbEntries.get(entry.getNJxbId()).getType() == 2){
										dto.setJxbName("合格走班");
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getNJxbId()).getName() + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()));
								}
								if(jxbEntries.get(entry.getNJxbId()).getType() == 1){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("等级走班")){
										zoubanType.add("等级走班");
										dto.setZouBanType(zoubanType);
									}
								}else if(jxbEntries.get(entry.getNJxbId()).getType() == 2){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("合格走班")){
										zoubanType.add("合格走班");
										dto.setZouBanType(zoubanType);
									}
								}
							}else{
								dto.setJxbName(jxbEntries.get(entry.getNJxbId()).getName() + "\r\n" + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()) + "\r\n" + jxbEntries.get(entry.getJxbId()).getNickName() + "\r\n" + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
							}
						}
						dto.setStudentCount(jxbEntries.get(entry.getNJxbId()).getStudentIds().size());
						if ((zkbMap.get(entry.getX() + "" + entry.getY()) != null)) {
							continue;
						} else {
							zkbMap.put(dto.getX() + "" + dto.getY(), dto);
						}
					} else {
						if (jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2) {
							dto.setType(2);
						} else {
							dto.setType(1);
						}
						if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
							if(jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2){
								if(isChecked == false){
									if(jxbEntries.get(entry.getJxbId()).getType() == 1){
										dto.setJxbName("等级走班");
									}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
										dto.setJxbName("合格走班");
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getJxbId()).getNickName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
								}
								if(jxbEntries.get(entry.getJxbId()).getType() == 1){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("等级走班")){
										zoubanType.add("等级走班");
										dto.setZouBanType(zoubanType);
									}
								}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("合格走班")){
										zoubanType.add("合格走班");
										dto.setZouBanType(zoubanType);
									}
								}
							}else{
								dto.setJxbName(jxbEntries.get(entry.getNJxbId()).getName() + "\r\n" + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()) + "\r\n" + (teaEntryMap.get(entry.getNTeacherId()) == null ? "" : teaEntryMap.get(entry.getNTeacherId()).getUserName()) + "\r\n" +jxbEntries.get(entry.getJxbId()).getNickName() + "\r\n" + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
							}
						} else {
							if(jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2){
								if(isChecked == false){
									if(jxbEntries.get(entry.getJxbId()).getType() == 1){
										dto.setJxbName("等级走班");
									}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
										dto.setJxbName("合格走班");
									}
								}else{
									dto.setJxbName(jxbEntries.get(entry.getJxbId()).getName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
								}
								if(jxbEntries.get(entry.getJxbId()).getType() == 1){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("等级走班")){
										zoubanType.add("等级走班");
										dto.setZouBanType(zoubanType);
									}
								}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
									List<String> zoubanType = dto.getZouBanType();
									if(!dto.getZouBanType().contains("合格走班")){
										zoubanType.add("合格走班");
										dto.setZouBanType(zoubanType);
									}
								}
							}else{
								dto.setJxbName(jxbEntries.get(entry.getJxbId()).getName() + "\r\n" + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
							}
						}
						dto.setStudentCount(jxbEntries.get(entry.getJxbId()).getStudentIds().size());
						if ((zkbMap.get(entry.getX() + "" + entry.getY()) != null)) {
							continue;
						} else {
							zkbMap.put(dto.getX() + "" + dto.getY(), dto);
						}
					}
				} else {
					if (jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2) {
						dto.setType(2);
					} else {
						dto.setType(1);
					}
					if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
						if(jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2){
							if(isChecked == false){
								if(jxbEntries.get(entry.getJxbId()).getType() == 1){
									dto.setJxbName("等级走班");
								}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
									dto.setJxbName("合格走班");
								}
							}else{
								dto.setJxbName(jxbEntries.get(entry.getJxbId()).getNickName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
							}
							if(jxbEntries.get(entry.getJxbId()).getType() == 1){
								List<String> zoubanType = dto.getZouBanType();
								if(!dto.getZouBanType().contains("等级走班")){
									zoubanType.add("等级走班");
									dto.setZouBanType(zoubanType);
								}
							}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
								List<String> zoubanType = dto.getZouBanType();
								if(!dto.getZouBanType().contains("合格走班")){
									zoubanType.add("合格走班");
									dto.setZouBanType(zoubanType);
								}
							}
						}else{
							dto.setJxbName(jxbEntries.get(entry.getJxbId()).getNickName() + "\r\n" + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
						}
					} else {
						if(jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2){
							if(isChecked == false){
								if(jxbEntries.get(entry.getJxbId()).getType() == 1){
									dto.setJxbName("等级走班");
								}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
									dto.setJxbName("合格走班");
								}
							}else{
								dto.setJxbName(jxbEntries.get(entry.getJxbId()).getName() + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
							}
							if(jxbEntries.get(entry.getJxbId()).getType() == 1){
								List<String> zoubanType = dto.getZouBanType();
								if(!dto.getZouBanType().contains("等级走班")){
									zoubanType.add("等级走班");
									dto.setZouBanType(zoubanType);
								}
							}else if(jxbEntries.get(entry.getJxbId()).getType() == 2){
								List<String> zoubanType = dto.getZouBanType();
								if(!dto.getZouBanType().contains("合格走班")){
									zoubanType.add("合格走班");
									dto.setZouBanType(zoubanType);
								}
							}
						}else{
							dto.setJxbName(jxbEntries.get(entry.getJxbId()).getName() + "\r\n" + (teaEntryMap.get(entry.getTeacherId()) == null ? "" : teaEntryMap.get(entry.getTeacherId()).getUserName()));
						}
					}
					dto.setStudentCount(jxbEntries.get(entry.getJxbId()).getStudentIds().size());
					if ((zkbMap.get(entry.getX() + "" + entry.getY()) != null)) {
						continue;
					} else {
						zkbMap.put(dto.getX() + "" + dto.getY(), dto);
					}
				}
				zkblist.add(dto);
			}
		}

		List<ObjectId> gradeIds = new ArrayList<ObjectId>();
		gradeIds.add(gradeId);
		List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, schoolId, gradeIds);
		for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
			if (zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY()) != null) {
				N33_ZKBDTO zkbdto = zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY());
				zkbdto.setIsSWAndCourse(1);
				zkbdto.setSwType(1);
				if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
					String s = zkbdto.getSwDesc();
					s += "\r\n";
					s += gdsxEntry.getDesc();
					zkbdto.setSwDesc(s);
				} else {
					zkbdto.setSwDesc(gdsxEntry.getDesc());
				}
				zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
			} else {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setSwType(1);
				zkbdto.setIsSWAndCourse(0);
				zkbdto.setSwDesc(gdsxEntry.getDesc());
				zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
			}
		}


		for (N33_SWDTO dto : gdSwdtos) {
			if (zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1)) != null) {
				N33_ZKBDTO zkbdto = zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1));
				zkbdto.setIsSWAndCourse(1);
				zkbdto.setSwType(1);
				if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
					String s = zkbdto.getSwDesc();
					s += "\r\n";
					s += dto.getDesc();
					zkbdto.setSwDesc(s);
				} else {
					zkbdto.setSwDesc(dto.getDesc());
				}
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			} else {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setIsSWAndCourse(0);
				zkbdto.setSwType(1);
				zkbdto.setSwDesc(dto.getDesc());
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			}
		}


		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("课表");

		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 7));
		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
		sheet.setColumnWidth((short) 1, (short) 5500);
		sheet.setColumnWidth((short) 2, (short) 5500);
		sheet.setColumnWidth((short) 3, (short) 5500);
		sheet.setColumnWidth((short) 4, (short) 5500);
		sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
		sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
		sheet.setColumnWidth((short) 7, (short) 5500);

		HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
		headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
		headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
		headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
		headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle1.setWrapText(true);

		HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
		headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
		headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		headerStyle2.setWrapText(true);


		HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
		headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
		headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle3.setWrapText(true);

		//为sheet1生成第一行，用于放表头信息
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeightInPoints(40);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(headerStyle1);
		cell1.setCellValue(termEntry.getXqName() + "  " + className + "  课表");

		//为sheet1生成第一行，用于放表头信息
		HSSFRow row = sheet.createRow(1);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("课节 / 日");
		List<String> weekLists = new ArrayList<String>();
		weekLists.add("周一");
		weekLists.add("周二");
		weekLists.add("周三");
		weekLists.add("周四");
		weekLists.add("周五");
		weekLists.add("周六");
		weekLists.add("周日");
		for (int j = 0; j < weekLists.size(); j++) {
			cell = row.createCell(j + 1);
			cell.setCellStyle(headerStyle2);
			cell.setCellValue(weekLists.get(j));
		}

		int page = 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < courseRangeDTOs.size(); i++) {
				page++;
				row = sheet.createRow(page);
				row.setHeightInPoints(50);
				cell = row.createCell(0);
				cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
				cell.setCellStyle(headerStyle2);
				for (int j = 0; j < weekLists.size(); j++) {
					cell = row.createCell(j + 1);
					cell.setCellStyle(headerStyle3);
					N33_ZKBDTO dto = zkbMap.get(j + "" + i);
					if (dto != null) {
						StringBuffer sb = new StringBuffer();
						if (dto.getIsSWAndCourse() == 0) {
							if (dto.getSwType() == 0) {
								if (dto.getType() == 2) {
									sb.append(dto.getJxbName());
								} else {
									if (dto.getJxbName() != null) {
										sb.append(dto.getJxbName() + "\r\n").append("人数:" + dto.getStudentCount());
									}
								}
								String con = sb.toString();
								cell.setCellValue(con);
							} else {
								cell.setCellValue(dto.getSwDesc());
							}
						} else {
							if (dto.getType() == 2) {
								sb.append(dto.getJxbName());
							} else {
								if (dto.getJxbName() != null) {
									sb.append(dto.getJxbName() + "\r\n").append("人数:" + dto.getStudentCount() + "\r\n");
								}
							}
							sb.append("事务：" + dto.getSwDesc());
							String con = sb.toString();
							cell.setCellValue(con);
						}
					}
				}
			}
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(className + "课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 行政班课表
	 *
	 * @param classId
	 * @param xqid
	 * @param gradeId
	 * @param schoolId
	 * @param week
	 * @return
	 */
	public List<Map<String, Object>> GetClassSettledPositionsByWeek(ObjectId classId, ObjectId xqid, ObjectId gradeId, ObjectId schoolId, Integer week) {
		Integer count = week % 2;
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds(xqid, schoolId, week, gradeId);
		if (ykbEntryList1.size() == 0) {
			return new ArrayList<Map<String, Object>>();
		}
		ObjectId cid = ykbEntryList1.get(0).getCId();

		//查询老师Map
		Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(cid,schoolId);
		//查行政班学生
		List<ObjectId> studentIds = getStudentList(classId, cid);
		//返回数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<N33_ZKBEntry> zkbEntries = new ArrayList<N33_ZKBEntry>();
		Map<ObjectId, N33_JXBEntry> jxbEntries = new HashMap<ObjectId, N33_JXBEntry>();
		//查年级学科
		Map<String, N33_KSDTO> stringN33_ksdtoMap = subjectService.getIsolateSubjectByGradeIdMap(cid, schoolId, gradeId.toHexString());
		if (studentIds.size() > 0) {
			//查学生教学班列表
			//jxb add
			int acClassType = turnOffService.getAcClassType(schoolId, gradeId, cid,1);
			//jxb add
			jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds, acClassType);
			List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries, xqid);
			//查询教学班所在的所有周课表
			zkbEntries = zkbDao.getN33_ZKBByWeek(jiaoXueBanList, week, xqid);
		} else {
			N33_ClassroomEntry classroomEntry = classroomDao.getRoomEntryListByXqClass(cid, classId);
			if (classroomEntry != null) {
				List<ObjectId> jxbIds = new ArrayList<ObjectId>();
				zkbEntries = zkbDao.getN33_ZKBByWeekList(classroomEntry.getRoomId(), week, gradeId);
				for (N33_ZKBEntry entry : zkbEntries) {
					jxbIds.add(entry.getJxbId());
					if (entry.getNJxbId() != null) {
						jxbIds.add(entry.getNJxbId());
					}
				}
				jxbEntries = jxbDao.getJXBMapsByIdsV1(jxbIds);
			}
		}
		//判定走班课和非走班课程
		for (N33_ZKBEntry entry : zkbEntries) {
			if (list.size() > 0 && entry.getType() != 4) {
				Integer x = Integer.parseInt(String.valueOf(entry.getX() + 1));
				Integer y = Integer.parseInt(String.valueOf(entry.getY() + 1));
				//是否可以添加
				Boolean bf = true;
				//判断坐标是否存在值
				for (Map<String, Object> lt : list) {
					//同一个坐标
					Integer x1 = (Integer) lt.get("x");
					Integer y1 = (Integer) lt.get("y");
					if (x1 == x && y1 == y) {
						String jList = (String) lt.get("jList");
						if (entry.getType() == 1 || entry.getType() == 2) {
							//单双周
							if (entry.getNTeacherId() == null) {
								if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
									jList += "," + jxbEntries.get(entry.getJxbId()).getNickName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")";
								} else {
									jList += "," + jxbEntries.get(entry.getJxbId()).getName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")";
								}
								lt.put("jList", jList);
								bf = false;
								break;
							} else {
								if (count == 0) {
									if (StringUtils.isNotEmpty(jxbEntries.get(entry.getNJxbId()).getNickName())) {
										jList += "," + jxbEntries.get(entry.getNJxbId()).getNickName() + "(" + jxbEntries.get(entry.getNJxbId()).getStudentIds().size() + ")";
									} else {
										jList += "," + jxbEntries.get(entry.getNJxbId()).getName() + "(" + jxbEntries.get(entry.getNJxbId()).getStudentIds().size() + ")";
									}
								} else {
									if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
										jList += "," + jxbEntries.get(entry.getJxbId()).getNickName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")";
									} else {
										jList += "," + jxbEntries.get(entry.getJxbId()).getName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")";
									}
								}
								lt.put("jList", jList);
								bf = false;
								break;
							}
						}
					}
				}
				if (bf) {
					if (entry.getNJxbId() != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("x", entry.getX() + 1);
						map.put("y", entry.getY() + 1);
						map.put("cid",entry.getCId().toString());
						if (count == 0) {
							if (jxbEntries.get(entry.getNJxbId()).getType() == 1 || jxbEntries.get(entry.getNJxbId()).getType() == 2) {
								map.put("type", 2);
							} else {
								map.put("type", 1);
							}
							if (StringUtils.isNotEmpty(jxbEntries.get(entry.getNJxbId()).getNickName())) {
								map.put("jList", jxbEntries.get(entry.getNJxbId()).getNickName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
							} else {
								map.put("jList", jxbEntries.get(entry.getNJxbId()).getName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
							}
							if(entry.getNTeacherId() != null){
								if(StringUtils.isNotEmpty(teaEntryMap.get(entry.getNTeacherId()).getUserName())){
									map.put("teaName",teaEntryMap.get(entry.getNTeacherId()).getUserName());
								}
							}
							map.put("count", jxbEntries.get(entry.getNJxbId()).getStudentIds().size());
						} else {
							if (jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2) {
								map.put("type", 2);
							} else {
								map.put("type", 1);
							}
							if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
								map.put("jList", jxbEntries.get(entry.getJxbId()).getNickName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
							} else {
								map.put("jList", jxbEntries.get(entry.getJxbId()).getName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
							}
							if(entry.getTeacherId() != null){
								if(StringUtils.isNotEmpty(teaEntryMap.get(entry.getTeacherId()).getUserName())){
									map.put("teaName",teaEntryMap.get(entry.getTeacherId()).getUserName());
								}
							}
							map.put("count", jxbEntries.get(entry.getJxbId()).getStudentIds().size());
						}
						list.add(map);
					} else {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("x", entry.getX() + 1);
						map.put("y", entry.getY() + 1);
						map.put("cid",entry.getCId().toString());
						if (jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2) {
							map.put("type", 2);
						} else {
							map.put("type", 1);
						}
						if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
							map.put("jList", jxbEntries.get(entry.getJxbId()).getNickName());
							if ((Integer) map.get("type") == 2) {
								map.put("jList", jxbEntries.get(entry.getJxbId()).getNickName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
							}
						} else {
							map.put("jList", jxbEntries.get(entry.getJxbId()).getName());
							if ((Integer) map.get("type") == 2) {
								map.put("jList", jxbEntries.get(entry.getJxbId()).getName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
							}
						}
						if(entry.getTeacherId() != null){
							if(teaEntryMap.get(entry.getTeacherId()) == null){
								map.put("teaName","");
							}else{
								if(StringUtils.isNotEmpty(teaEntryMap.get(entry.getTeacherId()).getUserName())){
									map.put("teaName",teaEntryMap.get(entry.getTeacherId()).getUserName());
								}
							}
						}
						map.put("count", jxbEntries.get(entry.getJxbId()).getStudentIds().size());
						list.add(map);
					}
				}
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", entry.getX() + 1);
				map.put("y", entry.getY() + 1);
				map.put("cid",entry.getCId().toString());
				String subName = stringN33_ksdtoMap.get(entry.getSubjectId().toString()).getSnm();
				//单双周
				if (entry.getNJxbId() != null) {
					if (count == 0) {
						if (jxbEntries.get(entry.getNJxbId()).getType() == 1 || jxbEntries.get(entry.getNJxbId()).getType() == 2) {
							map.put("type", 2);
						} else {
							map.put("type", 1);
						}
						if (StringUtils.isNotEmpty(jxbEntries.get(entry.getNJxbId()).getNickName())) {
							map.put("jList", jxbEntries.get(entry.getNJxbId()).getNickName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
						} else {
							map.put("jList", jxbEntries.get(entry.getNJxbId()).getName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
						}
						if(entry.getNTeacherId() != null && entry.getType() != 4){
							if(StringUtils.isNotEmpty(teaEntryMap.get(entry.getNTeacherId()).getUserName())){
								map.put("teaName",teaEntryMap.get(entry.getNTeacherId()).getUserName());
							}
						}
					} else {
						if (jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2) {
							map.put("type", 2);
						} else {
							map.put("type", 1);
						}
						if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
							map.put("jList", jxbEntries.get(entry.getJxbId()).getNickName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
						} else {
							map.put("jList", jxbEntries.get(entry.getJxbId()).getName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
						}
						if(entry.getTeacherId() != null && entry.getType() != 4){
							if(StringUtils.isNotEmpty(teaEntryMap.get(entry.getTeacherId()).getUserName())){
								map.put("teaName",teaEntryMap.get(entry.getTeacherId()).getUserName());
							}
						}
					}
				} else {
					if (jxbEntries.get(entry.getJxbId()).getType() == 1 || jxbEntries.get(entry.getJxbId()).getType() == 2) {
						map.put("type", 2);
					} else {
						map.put("type", 1);
					}
					if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
						map.put("jList", jxbEntries.get(entry.getJxbId()).getNickName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
					} else {
						map.put("jList", jxbEntries.get(entry.getJxbId()).getName() + "(" + jxbEntries.get(entry.getJxbId()).getStudentIds().size() + ")");
					}
					if(entry.getTeacherId() != null && entry.getType() != 4){
						if(StringUtils.isNotEmpty(teaEntryMap.get(entry.getTeacherId()).getUserName())){
							map.put("teaName",teaEntryMap.get(entry.getTeacherId()).getUserName());
						}
					}
				}
				map.put("count", jxbEntries.get(entry.getJxbId()).getStudentIds().size());
				list.add(map);
			}
		}
		return list;
	}


	public List<N33_JXBDTO> exportJXBinfo(Integer week, Integer x, Integer y, ObjectId xqid, ObjectId gradeId, ObjectId classId, ObjectId schoolId) {
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds(xqid, schoolId, week, gradeId);
		ObjectId cid = ykbEntryList1.get(0).getCId();
		//查行政班学生
		List<ObjectId> studentIds = getStudentList(classId, cid);
		//查学生教学班列表
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, xqid,1);
		//jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds, acClassType);
		List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries, xqid);
		//查询教学班所在的所有周课表
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeek(jiaoXueBanList, week, xqid, x, y);
		//判定走班课和非走班课程
		//返回数据
		List<N33_JXBDTO> list = new ArrayList<N33_JXBDTO>();
		for (N33_ZKBEntry entry : zkbEntries) {
			N33_JXBEntry jxbEntry = jxbEntries.get(entry.getJxbId());
			list.add(new N33_JXBDTO(jxbEntry));
		}
		return list;
	}

	public void exportJXBinfo(String name, Integer week, Integer x, Integer y, ObjectId xqid, ObjectId gradeId, ObjectId classId, ObjectId schoolId, HttpServletResponse response) {
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds(xqid, schoolId, week, gradeId);
		ObjectId cid = ykbEntryList1.get(0).getCId();
		//查行政班学生
		List<ObjectId> studentIds = getStudentList(classId, cid);
		//查学生教学班列表
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, xqid,1);
		//jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds,acClassType);
		List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries, xqid);
		//查询教学班所在的所有周课表
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeek(jiaoXueBanList, week, xqid, x, y);
		Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidMap(cid, studentIds);
		//判定走班课和非走班课程
		//返回数据
		List<StudentDTO> list = new ArrayList<StudentDTO>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_ZKBEntry entry : zkbEntries) {
			N33_JXBEntry jxbEntry = jxbEntries.get(entry.getJxbId());
			if (!jxbIds.contains(entry.getJxbId())) {
				jxbIds.add(entry.getJxbId());
				N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
				List<ObjectId> jxbStuIds = entry1.getStudentIds();
				List<ObjectId> jxbClassStuIds = entry1.getStudentIds();
				jxbStuIds.removeAll(studentIds);
				jxbClassStuIds.removeAll(jxbStuIds);
				for (ObjectId stuId : jxbClassStuIds) {
					StudentEntry studentEntry = studentEntryMap.get(stuId);
					StudentDTO dto = new StudentDTO(studentEntry);
					dto.setJxbName(jxbEntry.getName());
					list.add(dto);
				}
			}
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		//生成一个sheet1
		HSSFSheet sheet = wb.createSheet(name);
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("姓名");
		cell = row.createCell(1);
		cell.setCellValue("学号");
		cell = row.createCell(2);
		cell.setCellValue("性别");
		cell = row.createCell(3);
		cell.setCellValue("教学班");
		cell = row.createCell(4);
		cell.setCellValue("层级");
		int count = 1;// 学生计数
		for (StudentDTO dto : list) {
			HSSFRow temprow = sheet.createRow(count);
			HSSFCell tempcell = temprow.createCell(0);
			tempcell.setCellValue(dto.getUserName());
			tempcell = temprow.createCell(1);
			tempcell.setCellValue(dto.getStudyNumber());
			tempcell = temprow.createCell(2);
			tempcell.setCellValue(dto.getSexStr());
			tempcell = temprow.createCell(3);
			tempcell.setCellValue(dto.getJxbName());
			tempcell = temprow.createCell(4);
			tempcell.setCellValue(dto.getLevel());
			count++;

		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(name + ".xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 行政班课表
	 *
	 * @param schoolId
	 * @return
	 */
	public List<StudentDTO> GetClassSettledPositionsByWeekXyInfo(Map<String, Object> map, ObjectId schoolId) {
		Integer week = Integer.parseInt((String) map.get("week"));
		ObjectId xqid = new ObjectId((String) map.get("xqid"));
		ObjectId gradeId = new ObjectId((String) map.get("gradeId"));
		ObjectId classId = new ObjectId((String) map.get("classId"));
		Integer y = Integer.parseInt((String) map.get("x")) - 1;
		Integer x = Integer.parseInt((String) map.get("y")) - 1;
		List<String> jxbMapIds = new ArrayList<String>();
		if (map.get("jxbIds") != null) {
			jxbMapIds = (List<String>) map.get("jxbIds");
		}
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds(xqid, schoolId, week, gradeId);
		ObjectId cid = ykbEntryList1.get(0).getCId();
		//查行政班学生
		List<ObjectId> studentIds = getStudentList(classId, cid);
		//查学生教学班列表
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, xqid,1);
		//jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds, acClassType);
		List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries, xqid);
		//查询教学班所在的所有周课表
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeek(jiaoXueBanList, week, xqid, x, y);
		if (jxbMapIds.size() > 0) {
			zkbEntries = zkbDao.getN33_ZKBByWeek(MongoUtils.convertToObjectIdList(jxbMapIds), week, xqid, x, y);
		}
		Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidMap(cid, studentIds);
		//判定走班课和非走班课程
		//返回数据
		List<StudentDTO> list = new ArrayList<StudentDTO>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_ZKBEntry entry : zkbEntries) {
			N33_JXBEntry jxbEntry = jxbEntries.get(entry.getJxbId());
			if (!jxbIds.contains(entry.getJxbId())) {
				jxbIds.add(entry.getJxbId());
				N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
				List<ObjectId> jxbStuIds = entry1.getStudentIds();
				List<ObjectId> jxbClassStuIds = entry1.getStudentIds();
				jxbStuIds.removeAll(studentIds);
				jxbClassStuIds.removeAll(jxbStuIds);
				for (ObjectId stuId : jxbClassStuIds) {
					StudentEntry studentEntry = studentEntryMap.get(stuId);
					StudentDTO dto = new StudentDTO(studentEntry);
					dto.setJxbName(jxbEntry.getName());
					list.add(dto);
				}
			}
		}
		return list;
	}

	public Integer getN33_ZKBByWeek(ObjectId xqid, ObjectId gid, Integer week) {
		//查询教学班所在的所有周课表
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeek(xqid, gid, week);
		if (zkbEntries == null || zkbEntries.size() == 0) {
			return 0;
		}
		return zkbEntries.size();
	}

	/**
	 * 查询行政班学生
	 *
	 * @param classId
	 * @param xqid
	 * @return
	 */
	public List<ObjectId> getStudentList(ObjectId classId, ObjectId xqid) {
		List<StudentEntry> studentEntryList = studentDao.getStudentByXqidAndClassId(classId, xqid);
		List<ObjectId> studentList = new ArrayList<ObjectId>();
		for (StudentEntry studentEntry : studentEntryList) {
			studentList.add(studentEntry.getUserId());
		}
		return studentList;
	}


	/**
	 * 查询教学班根据学生
	 *
	 * @param jxbEntries
	 * @param xqid
	 * @return
	 */
	public List<ObjectId> getJiaoXueBanList(Map<ObjectId, N33_JXBEntry> jxbEntries, ObjectId xqid) {

		List<ObjectId> jiaoxueList = new ArrayList<ObjectId>();
		for (N33_JXBEntry studentEntry : jxbEntries.values()) {
			jiaoxueList.add(studentEntry.getID());
		}
		return jiaoxueList;
	}


	public List<PaiKeXyDto> getTeaIdsKPXy(List<String> teaIds, ObjectId xqid, ObjectId sid, Integer week) {
		List<PaiKeXyDto> xyDtoList = new ArrayList<PaiKeXyDto>();

		List<N33_SWEntry> swEntryList = swDao.getSwBySchoolId(xqid, sid);
		List<N33_ZKBEntry> zkbEntryList = zkbDao.getZKBEntryByWeekAndCiId(xqid, sid, week);


		Map<ObjectId, List<N33_GDSXDTO>> gdsxGradeMap = new HashMap<ObjectId, List<N33_GDSXDTO>>();

		List<ObjectId> teacherIds = MongoUtils.convertToObjectIdList(teaIds);
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds);

		int count = week % 2;

		//对应学期下的所有次
		List<ObjectId> cid = new ArrayList<ObjectId>();
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cid.add(paiKeTimes.getID());
			}
		}

		//该学期所有次的专项课Map
		Map<ObjectId, List<N33_ZhuanXiangEntry>> zhuanXiangEntryMap = zhuanXiangDao.findN33_ZhuanXiangEntryByCiId(cid, sid);
		for (String teaId : teaIds) {
			N33_TeaEntry teaEntry = teaEntryMap.get(new ObjectId(teaId));
			List<N33_GDSXDTO> gdsxdtoList = new ArrayList<N33_GDSXDTO>();
			List<ObjectId> teaGradeIds = teaEntry.getGradeList();
			for (ObjectId ids : teaGradeIds) {
				if (gdsxGradeMap.get(ids) == null) {
					List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(xqid, sid, ids);
					gdsxdtoList.addAll(gdsxdtos);
					gdsxGradeMap.put(ids, gdsxdtos);
				} else {
					gdsxdtoList = gdsxGradeMap.get(ids);
				}
			}
			for (N33_ZKBEntry zkbEntry : zkbEntryList) {
				//该老师已经排课的点
				PaiKeXyDto ypkXY = new PaiKeXyDto();
				boolean flag = false;
				if (zkbEntry.getType() == 4) {
					List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangEntryMap.get(zkbEntry.getJxbId());
					for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
						if (teaId.equals(zhuanXiangEntry.getTeaId().toString())) {
							ypkXY.setX(zkbEntry.getX());
							ypkXY.setY(zkbEntry.getY());
							flag = true;
						}
					}
				} else if (zkbEntry.getNTeacherId() != null) {
					if (count == 0) {
						if (teaId.equals(zkbEntry.getNTeacherId().toString())) {
							ypkXY.setX(zkbEntry.getX());
							ypkXY.setY(zkbEntry.getY());
							flag = true;
						}
					} else {
						if (zkbEntry.getTeacherId() != null && teaId.equals(zkbEntry.getTeacherId().toString())) {
							ypkXY.setX(zkbEntry.getX());
							ypkXY.setY(zkbEntry.getY());
							flag = true;
						}
					}
				} else {
					if (zkbEntry.getTeacherId() != null && teaId.equals(zkbEntry.getTeacherId().toString())) {
						ypkXY.setX(zkbEntry.getX());
						ypkXY.setY(zkbEntry.getY());
						flag = true;
					}
				}

				if (!xyDtoList.contains(ypkXY) && flag) {
					xyDtoList.add(ypkXY);
				}
			}

			//该老师存在固定事项的点
			for (N33_GDSXDTO gdsxdto : gdsxdtoList) {
				PaiKeXyDto ypkXY = new PaiKeXyDto();
				ypkXY.setY(gdsxdto.getY());
				ypkXY.setX(gdsxdto.getX());
				if (!xyDtoList.contains(ypkXY)) {
					xyDtoList.add(ypkXY);
				}
			}

			//该老师存在事务的点
			for (N33_SWEntry swEntry : swEntryList) {
				boolean flag = false;
				PaiKeXyDto ypkXY = new PaiKeXyDto();
				if (swEntry.getTeacherIds().size() == 0) {
					ypkXY.setY(swEntry.getX() - 1);
					ypkXY.setX(swEntry.getY() - 1);
					flag = true;
				} else {
					for (ObjectId ids : swEntry.getTeacherIds()) {
						if (teaId.equals(ids.toString())) {
							ypkXY.setY(swEntry.getX() - 1);
							ypkXY.setX(swEntry.getY() - 1);
							flag = true;
						}
					}
				}

				if (!xyDtoList.contains(ypkXY) && flag) {
					xyDtoList.add(ypkXY);
				}
			}
		}
		return xyDtoList;
	}

	/**
	 * 查询某点所有的未排课的老师
	 *
	 * @param xqid
	 * @param sid
	 * @param week
	 * @param subId
	 * @return
	 */
	public List<N33_TeaDTO> getFreeTeacher(ObjectId xqid, List<Map<String, String>> xyList, ObjectId sid, Integer week, String subId, String gradeId) {
		//返回的List
		List<N33_TeaDTO> teaEntryList = new ArrayList<N33_TeaDTO>();

		//先根据xy查固定事务
		List<N33_SWEntry> swdtoList = new ArrayList<N33_SWEntry>();
		Map<String, List<N33_SWEntry>> swMap = swService.getGuDingShiWuBySchoolIdForMap(xqid, sid);

		Map<String, List<N33_ZKBEntry>> zkbEntryMap = zkbDao.getN33_ZKBByWeekAndXYForMap(xqid, sid, week, "*");

		ObjectId gid = null;
		if (!"*".equals(gradeId)) {
			gid = new ObjectId(gradeId);
		}
		List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(xqid, sid, gid);
		List<N33_GDSXDTO> gdsxdtoList = new ArrayList<N33_GDSXDTO>();

		List<N33_ZKBEntry> zkbEntryList = new ArrayList<N33_ZKBEntry>();
		for (Map<String, String> map : xyList) {
			if (zkbEntryMap.get(map.get("x") + "," + map.get("y")) != null) {
				zkbEntryList.addAll(zkbEntryMap.get(map.get("x") + "," + map.get("y")));
			}
			Integer swx = new Double((String) map.get("x")).intValue() + 1;
			Integer swy = new Double((String) map.get("y")).intValue() + 1;
			if (swMap.get(swx + "," + swy) != null) {
				swdtoList.addAll(swMap.get(swx + "," + swy));
			}
			for (N33_GDSXDTO dto : gdsxdtos) {
				if (dto.getX() == (swx - 1) && dto.getY() == (swy - 1)) {
					gdsxdtoList.add(dto);
				}
			}
		}

		int count = week % 2;

		//对应学期下的所有次
		List<ObjectId> cid = new ArrayList<ObjectId>();
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cid.add(paiKeTimes.getID());
			}
		}

		Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIdsMapSubIds(cid, gradeId, sid);

		//该学期所有次的专项课Map
		Map<ObjectId, List<N33_ZhuanXiangEntry>> zhuanXiangEntryMap = zhuanXiangDao.findN33_ZhuanXiangEntryByCiId(cid, sid);

		//该学期所有次的老师
		List<N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySubjectIdByCiId(cid, subId, "*", gradeId);
		List<ObjectId> teaIds = MongoUtils.getFieldObjectIDs(teaEntries, "uid");

		//该学期所有次的老师Map
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.findN33_TeaEntryBySubjectId(cid, subId, "*", gradeId);
		//查询对应点有课或者有事务的所有老师
		List<ObjectId> ypkTeaIds = new ArrayList<ObjectId>();
		for (N33_GDSXDTO gdsxdto : gdsxdtoList) {
			for (ObjectId teaId : teaIds) {
				if (teaEntryMap.get(teaId).getGradeList().contains(new ObjectId(gdsxdto.getGradeId()))) {
					if (!ypkTeaIds.contains(teaId)) {
						ypkTeaIds.add(teaId);
					}
				}
			}
		}


		if (swdtoList.size() > 0) {
			for (N33_SWEntry swEntry : swdtoList) {
				if (swEntry.getTeacherIds().size() == 0) {
					for (ObjectId id : teaIds) {
						if (!ypkTeaIds.contains(id)) {
							ypkTeaIds.add(id);
						}
					}
				} else {
					for (ObjectId ids : swEntry.getTeacherIds()) {
						if (!ypkTeaIds.contains(ids)) {
							ypkTeaIds.add(ids);
						}
					}
				}
			}
		}

		if (zkbEntryList.size() > 0) {
			for (N33_ZKBEntry zkbEntry : zkbEntryList) {
				if (zkbEntry.getType() == 4) {
					List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangEntryMap.get(zkbEntry.getJxbId());
					for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
						if (!ypkTeaIds.contains(zhuanXiangEntry.getTeaId()) && teaIds.contains(zhuanXiangEntry.getTeaId())) {
							ypkTeaIds.add(zhuanXiangEntry.getTeaId());
						}
					}
				} else if (zkbEntry.getNTeacherId() != null) {
					if (count == 0) {
						if (!ypkTeaIds.contains(zkbEntry.getNTeacherId()) && teaIds.contains(zkbEntry.getNTeacherId())) {
							ypkTeaIds.add(zkbEntry.getNTeacherId());
						}
					} else {
						if (!ypkTeaIds.contains(zkbEntry.getTeacherId()) && teaIds.contains(zkbEntry.getTeacherId())) {
							ypkTeaIds.add(zkbEntry.getTeacherId());
						}
					}
				} else {
					if (!ypkTeaIds.contains(zkbEntry.getTeacherId()) && teaIds.contains(zkbEntry.getTeacherId())) {
						ypkTeaIds.add(zkbEntry.getTeacherId());
					}
				}
			}
		}
		for (ObjectId ids : teaIds) {
			if (!ypkTeaIds.contains(ids)) {
				N33_TeaEntry teaEntry = teaEntryMap.get(ids);
				N33_TeaDTO dto = new N33_TeaDTO(teaEntry);
				for (ObjectId id : teaEntry.getSubjectList()) {
					if (!"*".equals(subId)) {
						if (id.toString().equals(subId)) {
							if (ksEntryMap.get(id) == null) {
								dto.setSubjectStr("");
							} else {
								dto.setSubjectStr(ksEntryMap.get(id).getSubjectName());
							}
						}
					} else {
						if (teaEntry.getSubjectList().size() > 0) {
							if (ksEntryMap.get(teaEntry.getSubjectList().get(0)) == null) {
								dto.setSubjectStr("");
							} else {
								dto.setSubjectStr(ksEntryMap.get(teaEntry.getSubjectList().get(0)).getSubjectName());
							}
						} else {
							dto.setSubjectStr("");
						}
					}
				}
				teaEntryList.add(dto);
			}
		}
		return teaEntryList;
	}

	public Map getKeBiaoListByClassRoomId(ObjectId termId, String classRoomIds, ObjectId classRoomId, String weeks, String indexs, ObjectId schoolId, Integer week) {
		boolean saflg = false;
		boolean suflg = false;
		Integer count = week % 2;
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds1(termId, schoolId, week, classRoomId);
		List<ObjectId> zxk = new ArrayList<ObjectId>();
		for (N33_ZKBEntry entry : ykbEntryList1) {
			if (entry.getType() == 5) {
				zxk.add(entry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxk);
		Map<ObjectId, UserEntry> userEntryMap = userDao.getUserMapBySchoolId(schoolId, new BasicDBObject("_id", 1).append("binds", 1).append("nm", 1));
		if (ykbEntryList1 != null && ykbEntryList1.size() > 0) {
			ObjectId cid = ykbEntryList1.get(0).getCId();
			Map map = new HashMap();
			List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(schoolId.toString(), termId);
			List<ObjectId> classroomIds = new ArrayList<ObjectId>();
			if (!StringUtils.isEmpty(classRoomIds)) {
				classroomIds = MongoUtils.convert(classRoomIds);
			} else {
				classroomIds.add(classRoomId);
			}
			if (!StringUtils.isEmpty(weeks)) {
				String[] weekArg = weeks.split(",");
			}
			if (!StringUtils.isEmpty(indexs)) {
				String[] indexArg = indexs.split(",");
			}

			Map<String, List<N33_ZKBDTO>> ykbDTOMap = new HashMap<String, List<N33_ZKBDTO>>();
			Map<String, List<N33_ZKBDTO>> ykbDTOMap2 = new HashMap<String, List<N33_ZKBDTO>>();
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

			List<IdNameValuePairDTO> weekList = new ArrayList<IdNameValuePairDTO>();
			String[] weekArgs = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
			//if (gradeWeekRangeEntry != null) {
			for (int i = 1; i <= 7; i++) {
				IdNameValuePairDTO dto = new IdNameValuePairDTO();
				dto.setName(weekArgs[i - 1]);
				dto.setType(i);
				weekList.add(dto);
			}
			//}
			List<N33_ZKBDTO> n33_ykbdtos = new ArrayList<N33_ZKBDTO>();
			List<N33_ZKBEntry> ykbEntryList = zkbDao.getYKBsByclassRoomIds(termId, schoolId, classroomIds, week);
			if (ykbEntryList != null && ykbEntryList.size() != 0) {
				List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "tid");
				Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, cid);
				List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "jxbId");
				jxbIds.addAll(MongoUtils.getFieldObjectIDs(ykbEntryList, "nJxbId"));
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

				for (N33_ZKBEntry ykbEntry : ykbEntryList) {
					if (ykbEntry.getX() == 5 && ykbEntry.getJxbId() != null) {
						saflg = true;
					}
					if (ykbEntry.getX() == 6 && ykbEntry.getJxbId() != null) {
						suflg = true;
					}

					n33_ykbdtos.add(new N33_ZKBDTO(ykbEntry));
					List<N33_ZKBDTO> n33_ykbdtos1 = ykbDTOMap.get(ykbEntry.getClassroomId().toString());
					if (n33_ykbdtos1 == null || n33_ykbdtos1.size() == 0) {
						n33_ykbdtos1 = new ArrayList<N33_ZKBDTO>();
					}
					N33_ZKBDTO ykbdto = new N33_ZKBDTO(ykbEntry);
					if (ykbdto.getNtid() != null) {
						if (count == 0) {
							if (StringUtils.isNotEmpty(ykbdto.getNtid())) {
								ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getNtid())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getNtid())).getUserName() : "");
							}
							if (StringUtils.isNotEmpty(ykbdto.getnJxbId())) {
								List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
								ykbdto.setStuIds(stuIds);
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
									List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
									ykbdto.setStuIds(stuIds);
									N33_ZIXIKEEntry zixikeEntry = zixikeDao.getJXBById(new ObjectId(ykbdto.getJxbId()));
									if (zixikeEntry != null) {
										ykbdto.setJxbName(zixikeEntry.getName());
										ykbdto.setStudentCount(zixikeEntry.getStudentIds().size());
									}
								} else {
									ykbdto.setJxbType(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getType() : 1);
									List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
									ykbdto.setStuIds(stuIds);
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
								List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
								ykbdto.setStuIds(stuIds);
								ykbdto.setJxbType(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getType() : 1);
								if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName())) {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName());
								} else {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getName());
								}
								ykbdto.setStudentCount(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getStudentIds().size() : 0);
								ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getJxbId())) : "");
							} else {
								List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
								ykbdto.setStuIds(stuIds);
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
					if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY()) && ykbdto.getIsUse() != 0) {
						ykbdto.setIsSWAndCourse(1);
					}
					if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY())) {
						ykbdto.setSwType(1);
						ykbdto.setRemarks(swdtoMap.get(ykbEntry.getX() + "," + ykbEntry.getY()).getDesc());
					}
					n33_ykbdtos1.add(ykbdto);
					ykbDTOMap.put(ykbEntry.getClassroomId().toString(), n33_ykbdtos1);
				}
			}
			for (Map.Entry<String, List<N33_ZKBDTO>> entry : ykbDTOMap.entrySet()) {
				List<N33_ZKBDTO> ykbdtos = entry.getValue();
				Collections.sort(ykbdtos, new Comparator<N33_ZKBDTO>() {
					@Override
					public int compare(N33_ZKBDTO o1, N33_ZKBDTO o2) {
						return o1.getY() - o2.getY();
					}
				});
				ykbDTOMap.put(entry.getKey(), ykbdtos);
			}

			ykbDTOMap2.put(classRoomId.toString(), ykbDTOMap.get(classRoomId.toString()));

			map.put("courseRangeDTOs", courseRangeDTOs);
			map.put("ykbdto", ykbDTOMap2);
			map.put("saflg", saflg);
			map.put("suflg", suflg);
			return map;
		} else {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("message", "该周课表未发布");
			return retMap;
		}
	}

	/**
	 * 获取教学班学生手环Code
	 * @return
	 */
	public List<Map<String,Object>> getJXBstudent(ObjectId zkbId,ObjectId sid){
		List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();

		Integer week = termService.getDateByOr(sid.toString());
		Integer count = week % 2;
		N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(zkbId);
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		ObjectId jxbId = null;
		if(zkbEntry.getType() == 6 && count == 0){
			jxbId = zkbEntry.getNJxbId();
		}else {
			jxbId = zkbEntry.getJxbId();
		}

		if(zkbEntry.getType() == 5){
			N33_ZIXIKEEntry zixikeEntry = zixikeDao.getJXBById(jxbId);
			studentIds.addAll(zixikeEntry.getStudentIds());
		}else if(zkbEntry.getType() == 4){
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbId,zkbEntry.getCId());
			for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
				if(zhuanXiangEntry.getRoomId().toString().equals(zkbEntry.getClassroomId().toString())){
					studentIds.addAll(zhuanXiangEntry.getStudentId());
				}
			}
		}else{
			N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
			studentIds.addAll(jxbEntry.getStudentIds());
		}

//		List<UserEntry> userEntries = userDao.getUserEntryList(studentIds,new BasicDBObject("nm",1).append("_id",1).append("binds",1));
		List<UserEntry> userEntries = userDao.getUserEntryList(studentIds,Constant.FIELDS);
		List<N33_DaKaEntry> daKaEntries = daKaDao.findDaKaEntries(zkbId);
		List<ObjectId> daKaStuIds = MongoUtils.getFieldObjectIDs(daKaEntries,"studentId");
		for (UserEntry userEntry : userEntries) {
			Map<String,Object> map = new HashMap<String, Object>();
			if(daKaStuIds.contains(userEntry.getID())){
				map.put("daKaFlag",true);
			}else{
				map.put("daKaFlag",false);
			}
			map.put("name",userEntry.getUserName());
			map.put("userId",userEntry.getID().toString());
			if(userEntry.getUserBind() != null && userEntry.getUserBind().getType() == 10000){
				map.put("code",userEntry.getUserBind().getBindValue());
			}else{
				map.put("code","");
			}
			retList.add(map);
		}
		return retList;
	}

	public void saveStudentDaKa(ObjectId zkbId,ObjectId studentId,int daKaFlag){
		N33_DaKaEntry daKaEntry = new N33_DaKaEntry(zkbId,studentId,daKaFlag);
		daKaDao.addDaKaEntry(daKaEntry);
	}

	public Map<String,Object> getKeBiaoListByClassRoomIdForFulaan(String classRoomIds, ObjectId classId, String weeks, String indexs, ObjectId schoolId) {

		Integer week = termService.getDateByOr(schoolId.toString());
		ObjectId termId = defaultTermService.getDefauleTermId(schoolId);
		Integer count = week % 2;
		List<N33_ZKBEntry> ykbEntryList2 = zkbDao.getZKBEntryByWeekAndCiId(termId, schoolId, week);
		ObjectId cid = null;
		if(ykbEntryList2 != null && ykbEntryList2.size() > 0){
			cid = ykbEntryList2.get(0).getCId();
		}else {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("message", "该周课表未发布");
			return retMap;
		}
		N33_ClassroomEntry classroomEntry = classroomDao.getRoomEntryListByXqClass(cid, classId);
		ObjectId classRoomId = null;
		if(classroomEntry != null && classroomEntry.getRoomId() != null && !"".equals(classroomEntry.getRoomId().toString())){
			classRoomId = classroomEntry.getRoomId();
		}else {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("message", "该行政班没有对应的教室");
			return retMap;
		}
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds1(termId, schoolId, week, classRoomId);
		List<ObjectId> zxk = new ArrayList<ObjectId>();
		for (N33_ZKBEntry entry : ykbEntryList1) {
			if (entry.getType() == 5) {
				zxk.add(entry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxk);
		//Map<ObjectId, UserEntry> userEntryMap = userDao.getUserMapBySchoolId(schoolId, new BasicDBObject("_id", 1).append("binds", 1).append("nm", 1));
		if (ykbEntryList1 != null && ykbEntryList1.size() > 0) {
			ObjectId cid1 = ykbEntryList1.get(0).getCId();
			Map map = new HashMap();
			List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), cid1);
			List<ObjectId> classroomIds = new ArrayList<ObjectId>();
			if (!StringUtils.isEmpty(classRoomIds)) {
				classroomIds = MongoUtils.convert(classRoomIds);
			} else {
				classroomIds.add(classRoomId);
			}
			if (!StringUtils.isEmpty(weeks)) {
				String[] weekArg = weeks.split(",");
			}
			if (!StringUtils.isEmpty(indexs)) {
				String[] indexArg = indexs.split(",");
			}

			Map<String, List<N33_ZKBDTO>> ykbDTOMap = new HashMap<String, List<N33_ZKBDTO>>();
			//Map<String, List<N33_ZKBDTO>> ykbDTOMap2 = new HashMap<String, List<N33_ZKBDTO>>();
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

			List<IdNameValuePairDTO> weekList = new ArrayList<IdNameValuePairDTO>();
			String[] weekArgs = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
			//if (gradeWeekRangeEntry != null) {
			for (int i = 1; i <= 7; i++) {
				IdNameValuePairDTO dto = new IdNameValuePairDTO();
				dto.setName(weekArgs[i - 1]);
				dto.setType(i);
				weekList.add(dto);
			}
			//}
			List<N33_ZKBDTO> n33_ykbdtos = new ArrayList<N33_ZKBDTO>();
			List<N33_ZKBEntry> ykbEntryList = zkbDao.getYKBsByclassRoomIds(termId, schoolId, classroomIds, week);
			if (ykbEntryList != null && ykbEntryList.size() != 0) {
				List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "tid");
				Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, cid);
				List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "jxbId");
				jxbIds.addAll(MongoUtils.getFieldObjectIDs(ykbEntryList, "nJxbId"));
				Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapsByIds(jxbIds);
				Map<String, String> ksName = subjectService.getSubjectMapById(schoolId, cid);

				for (N33_ZKBEntry ykbEntry : ykbEntryList) {
					n33_ykbdtos.add(new N33_ZKBDTO(ykbEntry));
					List<N33_ZKBDTO> n33_ykbdtos1 = ykbDTOMap.get(ykbEntry.getClassroomId().toString());
					if (n33_ykbdtos1 == null || n33_ykbdtos1.size() == 0) {
						n33_ykbdtos1 = new ArrayList<N33_ZKBDTO>();
					}
					N33_ZKBDTO ykbdto = new N33_ZKBDTO(ykbEntry);
					if (ykbdto.getNtid() != null) {
						if (count == 0) {
							if (StringUtils.isNotEmpty(ykbdto.getNsubId())) {
								ykbdto.setSubjectName(ksName.get(ykbdto.getNsubId()) != null ? ksName.get(ykbdto.getNsubId()) : "");
							}
							if (StringUtils.isNotEmpty(ykbdto.getNtid())) {
								ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getNtid())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getNtid())).getUserName() : "");
							}
							if (StringUtils.isNotEmpty(ykbdto.getnJxbId())) {
								//List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
								//ykbdto.setStuIds(stuIds);
								ykbdto.setJxbType(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getType() : 1);
								if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getNickName())) {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getNickName());
								} else {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getName());
								}
								ykbdto.setStudentCount(jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getnJxbId())).getStudentIds().size() : 0);
								//ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getnJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getnJxbId())) : "");
							}
						} else {
							if (StringUtils.isNotEmpty(ykbdto.getSubjectId())) {
								ykbdto.setSubjectName(ksName.get(ykbdto.getSubjectId()) != null ? ksName.get(ykbdto.getSubjectId()) : "");
							}
							if (StringUtils.isNotEmpty(ykbdto.getTeacherId())) {
								ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())).getUserName() : "");
							}
							if (StringUtils.isNotEmpty(ykbdto.getJxbId())) {
								if (ykbdto.getType() == 5) {
									ykbdto.setJxbType(5);
									//List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
									//ykbdto.setStuIds(stuIds);
									N33_ZIXIKEEntry zixikeEntry = zixikeDao.getJXBById(new ObjectId(ykbdto.getJxbId()));
									if (zixikeEntry != null) {
										ykbdto.setJxbName(zixikeEntry.getName());
										ykbdto.setStudentCount(zixikeEntry.getStudentIds().size());
									}
								} else {
									ykbdto.setJxbType(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getType() : 1);
									//List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
									//ykbdto.setStuIds(stuIds);
									if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName())) {
										ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName());
									} else {
										ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getName());
									}
									ykbdto.setStudentCount(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getStudentIds().size() : 0);
									//ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getJxbId())) : "");
								}

							}
						}
					} else {
						if (StringUtils.isNotEmpty(ykbdto.getTeacherId())) {
							ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())).getUserName() : "");
						}
						if (StringUtils.isNotEmpty(ykbdto.getJxbId())) {
							if (ykbdto.getType() != 5) {
								//List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
								//ykbdto.setStuIds(stuIds);
								if (StringUtils.isNotEmpty(ykbdto.getSubjectId())) {
									ykbdto.setSubjectName(ksName.get(ykbdto.getSubjectId()) != null ? ksName.get(ykbdto.getSubjectId()) : "");
								}
								ykbdto.setJxbType(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getType() : 1);
								if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName())) {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getNickName());
								} else {
									ykbdto.setJxbName(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getName());
								}
								ykbdto.setStudentCount(jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(ykbdto.getJxbId())).getStudentIds().size() : 0);
								//ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getJxbId())) : "");
							} else {
								//List<String> stuIds = getStudentIds(userEntryMap, ykbdto, jxbEntryMap, classRoomId, count, zixikeEntries);
								//ykbdto.setStuIds(stuIds);
								N33_ZIXIKEEntry entry = zixikeEntries.get(new ObjectId(ykbdto.getJxbId()));
								if (entry == null) {
									continue;
								}
								ykbdto.setSubjectName("自习课");
								ykbdto.setJxbName(entry.getName() + "(自习课)");
								ykbdto.setStudentCount(entry.getStudentIds().size());
								ykbdto.setRemarks("");
								ykbdto.setType(5);
							}
						}
					}
					ykbdto.setSwType(0);
					ykbdto.setIsSWAndCourse(0);
					if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY()) && ykbdto.getIsUse() != 0) {
						ykbdto.setIsSWAndCourse(1);
					}
					if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY())) {
						ykbdto.setSwType(1);
						ykbdto.setRemarks(swdtoMap.get(ykbEntry.getX() + "," + ykbEntry.getY()).getDesc());
					}
					n33_ykbdtos1.add(ykbdto);
					ykbDTOMap.put(ykbEntry.getClassroomId().toString(), n33_ykbdtos1);
				}
			}
			for (Map.Entry<String, List<N33_ZKBDTO>> entry : ykbDTOMap.entrySet()) {
				List<N33_ZKBDTO> ykbdtos = entry.getValue();
				Collections.sort(ykbdtos, new Comparator<N33_ZKBDTO>() {
					@Override
					public int compare(N33_ZKBDTO o1, N33_ZKBDTO o2) {
						return o1.getY() - o2.getY();
					}
				});
				ykbDTOMap.put(entry.getKey(), ykbdtos);
			}

			//ykbDTOMap2.put(classRoomId.toString(), ykbDTOMap.get(classRoomId.toString()));

			map.put("courseRangeDTOs", courseRangeDTOs);
			map.put("zkbdto", ykbDTOMap.get(classRoomId.toString()));
			return map;
		} else {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("message", "该周课表未发布");
			return retMap;
		}
	}

	private List<String> getStudentIds(Map<ObjectId, UserEntry> userEntryMap, N33_ZKBDTO zkbdto, Map<ObjectId, N33_JXBEntry> jxbEntryMap, ObjectId classRoomId, Integer count, Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries) {
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		N33_JXBEntry jxbEntry = null;
		if (zkbdto.getNtid() != null) {
			if (count == 0) {
				jxbEntry = jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId()));
			} else {
				jxbEntry = jxbEntryMap.get(new ObjectId(zkbdto.getJxbId()));
			}
			if (jxbEntry.getType() == 4) {
				List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbEntry.getID(), jxbEntry.getTermId());
				for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
					if (zhuanXiangEntry.getRoomId() != null && zhuanXiangEntry.getRoomId().toString().equals(classRoomId.toString())) {
						studentIds.addAll(zhuanXiangEntry.getStudentId());
						break;
					}
				}
			} else {
				studentIds = jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getStudentIds();
			}
		} else {
			if (zkbdto.getType() != 5) {
				studentIds = jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getStudentIds();
			} else {
				N33_ZIXIKEEntry entry = zixikeEntries.get(new ObjectId(zkbdto.getJxbId()));
				studentIds = entry.getStudentIds();
			}
		}
		List<String> stuIds = new ArrayList<String>();
		for (ObjectId ids : studentIds) {
			UserEntry userEntry = userEntryMap.get(ids);
			if (userEntryMap.get(ids) != null && userEntryMap.get(ids).getUserBind() != null && userEntryMap.get(ids).getUserBind().getBindValue() != null) {
				stuIds.add(userEntryMap.get(ids).getUserBind().getBindValue());
			} else {
				stuIds.add("");
			}
		}
		return stuIds;
	}

	public Map getKeBiaoList(ObjectId termId, String gradeId, String classRoomIds, String weeks, String indexs, ObjectId schoolId, Integer week) {
		boolean saflg = false;
		boolean suflg = false;
		Integer count = week % 2;
		List<N33_ZKBEntry> ykbEntryList1 = zkbDao.getYKBsByclassRoomIds(termId, schoolId, week, new ObjectId(gradeId));
		List<ObjectId> zxk = new ArrayList<ObjectId>();
		for (N33_ZKBEntry entry : ykbEntryList1) {
			if (entry.getType() == 5) {
				zxk.add(entry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxk);
		if (ykbEntryList1.size() > 0) {
			ObjectId cid = ykbEntryList1.get(0).getCId();
			Map map = new HashMap();
			List<N33_ClassroomDTO> classroomDTOs = classroomService.getRoomEntryListByXqGradeAndTerm(termId, schoolId, new ObjectId(gradeId), 1);
			List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), cid);
			List<ObjectId> classroomIds = new ArrayList<ObjectId>();
			if (!StringUtils.isEmpty(classRoomIds)) {
				classroomIds = MongoUtils.convert(classRoomIds);
			} else {
				if (classroomDTOs != null && classroomDTOs.size() != 0) {
					for (N33_ClassroomDTO dto : classroomDTOs) {
						classroomIds.add(new ObjectId(dto.getRoomId()));
					}
				}
			}
			if (!StringUtils.isEmpty(weeks)) {
				String[] weekArg = weeks.split(",");
			}
			if (!StringUtils.isEmpty(indexs)) {
				String[] indexArg = indexs.split(",");
			}

			Map<String, List<N33_ZKBDTO>> ykbDTOMap = new HashMap<String, List<N33_ZKBDTO>>();
			Map<String, List<N33_ZKBDTO>> ykbDTOMap2 = new HashMap<String, List<N33_ZKBDTO>>();
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
			List<N33_ZKBDTO> n33_ykbdtos = new ArrayList<N33_ZKBDTO>();
			List<N33_ZKBEntry> ykbEntryList = zkbDao.getYKBsByclassRoomIds(termId, schoolId, classroomIds, week);
			if (ykbEntryList != null && ykbEntryList.size() != 0) {
				List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "tid");
				Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, cid);
				List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "jxbId");
				jxbIds.addAll(MongoUtils.getFieldObjectIDs(ykbEntryList, "nJxbId"));
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

				for (N33_ZKBEntry ykbEntry : ykbEntryList) {
					if (ykbEntry.getX() == 5 && ykbEntry.getJxbId() != null) {
						saflg = true;
					}
					if (ykbEntry.getX() == 6 && ykbEntry.getJxbId() != null) {
						suflg = true;
					}

					n33_ykbdtos.add(new N33_ZKBDTO(ykbEntry));
					List<N33_ZKBDTO> n33_ykbdtos1 = ykbDTOMap.get(ykbEntry.getClassroomId().toString());
					if (n33_ykbdtos1 == null || n33_ykbdtos1.size() == 0) {
						n33_ykbdtos1 = new ArrayList<N33_ZKBDTO>();
					}
					N33_ZKBDTO ykbdto = new N33_ZKBDTO(ykbEntry);
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
			for (Map.Entry<String, List<N33_ZKBDTO>> entry : ykbDTOMap.entrySet()) {
				List<N33_ZKBDTO> ykbdtos = entry.getValue();
				Collections.sort(ykbdtos, new Comparator<N33_ZKBDTO>() {
					@Override
					public int compare(N33_ZKBDTO o1, N33_ZKBDTO o2) {
						return o1.getY() - o2.getY();
					}
				});
				ykbDTOMap.put(entry.getKey(), ykbdtos);
			}
			if (classroomDTOs != null && classroomDTOs.size() != 0) {
				for (N33_ClassroomDTO dto : classroomDTOs) {
					dto.setWeekList(weekList);
					if (ykbDTOMap.get(dto.getRoomId()) != null) {
						ykbDTOMap2.put(dto.getRoomId(), ykbDTOMap.get(dto.getRoomId()));
					}
				}
			}
			map.put("courseRangeDTOs", courseRangeDTOs);
			map.put("ykbdto", ykbDTOMap2);
			map.put("saflg", saflg);
			map.put("suflg", suflg);
			map.put("classrooms", classroomDTOs);
			return map;
		}
		return null;
	}


	public Integer getGradeWeek(ObjectId sid, ObjectId xqid) {
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		for (TermEntry.PaiKeTimes times : termEntry.getPaiKeTimes()) {
			if (times.getIr() == 0) {
				cids.add(times.getID());
			}
		}
		List<N33_GradeWeekRangeEntry> gradeWeekRangeEntryList = n33_gradeWeekRangeDao.getGradeWeekRangeByCids(cids, sid);
		if (gradeWeekRangeEntryList != null && gradeWeekRangeEntryList.size() > 0) {
			Integer end = 5;
			for (N33_GradeWeekRangeEntry entry : gradeWeekRangeEntryList) {
				if (entry.getEnd() > end) {
					end = entry.getEnd();
				}
			}
			return end;
		} else {
			return 5;
		}
	}


	/**
	 * 按照行政班导出总课表
	 *
	 * @param termId
	 * @param gradeId
	 * @param schoolId
	 * @param week
	 * @param response
	 */
	public void exportZKBDataByClass(ObjectId termId, ObjectId gradeId, ObjectId schoolId, Integer week, String gradeName,HttpServletResponse response,boolean isChecked) {
		Integer count = week % 2;
		List<N33_ZKBEntry> zkbEntryList1 = zkbDao.getYKBsByclassRoomIds(termId, schoolId, week, gradeId);
		ObjectId cid = null;
		if (zkbEntryList1.size() > 0) {
			cid = zkbEntryList1.get(0).getCId();
		} else {
			return;
		}
		//查询老师Map
		Map<ObjectId,N33_TeaEntry> teaEntryMap1 = teaDao.getTeaMap(cid,schoolId);
		//自习课Map
		List<ObjectId> zxk = new ArrayList<ObjectId>();
		for (N33_ZKBEntry entry : zkbEntryList1) {
			if (entry.getType() == 5) {
				zxk.add(entry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxk);

		//对应次的课节
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), cid);

		//获取该次的所有行政班
		List<ClassInfoDTO> classDTOs = classService.getClassByXqAndGrade(cid, schoolId, gradeId);
		//得到行政班的classId集合
		List<ObjectId> clasIds = new ArrayList<ObjectId>();
		for (ClassInfoDTO dto : classDTOs) {
			clasIds.add(new ObjectId(dto.getClassId()));
		}

		Map<String, List<N33_ZKBDTO>> zkbDTOMap = new HashMap<String, List<N33_ZKBDTO>>();
		Map<String, List<N33_ZKBDTO>> zkbDTOMap2 = new HashMap<String, List<N33_ZKBDTO>>();
		List<String> swlist = new ArrayList<String>();

		List<N33_SWDTO> swdtos = swService.getGuDingShiWuByXqid(termId);
		if (swdtos != null && swdtos.size() != 0) {
			for (N33_SWDTO swdto : swdtos) {
				swlist.add(swdto.getXy());
			}
		}

		for (ObjectId classId : clasIds) {
			//查行政班学生
			List<ObjectId> studentIds = getStudentList(classId, cid);
			//查年级学科
			Map<String, N33_KSDTO> stringN33_ksdtoMap = subjectService.getIsolateSubjectByGradeIdMap(cid, schoolId, gradeId.toString());
			//查学生教学班列表
			//jxb add
			int acClassType = turnOffService.getAcClassType(schoolId, gradeId, cid,1);
			//jxb add
			Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds,acClassType);
			List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries, termId);
			//查询教学班所在的所有周课表
			List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeek(jiaoXueBanList, week, termId);

			if (zkbEntries != null && zkbEntries.size() != 0) {
				List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(zkbEntries, "tid");
				Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, cid);
				List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(zkbEntries, "jxbId");
				jxbIds.addAll(MongoUtils.getFieldObjectIDs(zkbEntries, "nJxbId"));
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

				//判断是否是走班
				List<N33_ZKBDTO> zkblist = new ArrayList<N33_ZKBDTO>();

				for (N33_ZKBEntry zkbEntry : zkbEntries) {

					if (zkblist.size() > 0) {
						Integer x = Integer.parseInt(String.valueOf(zkbEntry.getX()));
						Integer y = Integer.parseInt(String.valueOf(zkbEntry.getY()));
						//是否可以添加
						Boolean bf = true;
						//判断坐标是否存在值
						for (N33_ZKBDTO dto : zkblist) {
							//同一个坐标
							Integer x1 = (Integer) dto.getX();
							Integer y1 = (Integer) dto.getY();
							if (x1 == x && y1 == y) {
								String jList = dto.getJxbName();
								//单双周
								if (zkbEntry.getNTeacherId() == null) {
									if (StringUtils.isNotEmpty(jxbEntries.get(zkbEntry.getJxbId()).getNickName())) {
										jList += "\r\n" + jxbEntries.get(zkbEntry.getJxbId()).getNickName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName());
									} else {
										jList += "\r\n" + jxbEntries.get(zkbEntry.getJxbId()).getName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName());
									}
									if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("等级走班")){
											zoubanType.add("等级走班");
											dto.setZouBanType(zoubanType);
										}
									}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
										List<String> zoubanType = dto.getZouBanType();
										if(!dto.getZouBanType().contains("合格走班")){
											zoubanType.add("合格走班");
											dto.setZouBanType(zoubanType);
										}
									}
									if(isChecked == false){
										List<String> zoubanType = dto.getZouBanType();
										String zoubanString = "";
										for (String s : zoubanType) {
											zoubanString += s;
											zoubanString += "\r\n";
										}
										dto.setJxbName(zoubanString);
									}else{
										dto.setJxbName(jList);
									}
									bf = false;
									break;
								} else {
									if (count == 0) {
										if (StringUtils.isNotEmpty(jxbEntries.get(zkbEntry.getNJxbId()).getNickName())) {
											jList += "\r\n" + jxbEntries.get(zkbEntry.getNJxbId()).getNickName() + (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName());
										} else {
											jList += "\r\n" + jxbEntries.get(zkbEntry.getNJxbId()).getName() + (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName());
										}
										if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1){
											List<String> zoubanType = dto.getZouBanType();
											if(!dto.getZouBanType().contains("等级走班")){
												zoubanType.add("等级走班");
												dto.setZouBanType(zoubanType);
											}
										}else if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
											List<String> zoubanType = dto.getZouBanType();
											if(!dto.getZouBanType().contains("合格走班")){
												zoubanType.add("合格走班");
												dto.setZouBanType(zoubanType);
											}
										}
									} else {
										if (StringUtils.isNotEmpty(jxbEntries.get(zkbEntry.getJxbId()).getNickName())) {
											jList += "\r\n" + jxbEntries.get(zkbEntry.getJxbId()).getNickName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName());
										} else {
											jList += "\r\n" + jxbEntries.get(zkbEntry.getJxbId()).getName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName());
										}
										if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
											List<String> zoubanType = dto.getZouBanType();
											if(!dto.getZouBanType().contains("等级走班")){
												zoubanType.add("等级走班");
												dto.setZouBanType(zoubanType);
											}
										}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
											List<String> zoubanType = dto.getZouBanType();
											if(!dto.getZouBanType().contains("合格走班")){
												zoubanType.add("合格走班");
												dto.setZouBanType(zoubanType);
											}
										}
									}
									if(isChecked == false){
										List<String> zoubanType = dto.getZouBanType();
										String zoubanString = "";
										for (String s : zoubanType) {
											zoubanString += s;
											zoubanString += "\r\n";
										}
										dto.setJxbName(zoubanString);
									}else{
										dto.setJxbName(jList);
									}
									bf = false;
									break;
								}
							}
						}
						if (bf) {
							List<N33_ZKBDTO> n33_zkbdtos1 = zkbDTOMap.get(classId.toString());
							if (n33_zkbdtos1 == null || n33_zkbdtos1.size() == 0) {
								n33_zkbdtos1 = new ArrayList<N33_ZKBDTO>();
							}
							N33_ZKBDTO zkbdto = new N33_ZKBDTO(zkbEntry);

							if (zkbdto.getNtid() != null) {
								if (count == 0) {
									if (jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2) {
										zkbdto.setType(2);
									} else {
										zkbdto.setType(1);
									}
//									if (StringUtils.isNotEmpty(zkbdto.getNtid())) {
//										zkbdto.setTeacherName(teaEntryMap.get(new ObjectId(zkbdto.getNtid())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getNtid())).getUserName() : "");
//									}
									if (StringUtils.isNotEmpty(zkbdto.getnJxbId())) {
										zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getType() : 1);
										if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getNickName())) {
											if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
												if(isChecked == false){
													if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1){
														zkbdto.setJxbName("等级走班");
													}else if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
														zkbdto.setJxbName("合格走班");
													}
												}else{
													zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getNJxbId()).getNickName() + (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName()));
												}
												if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("等级走班")){
														zoubanType.add("等级走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}else if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("合格走班")){
														zoubanType.add("合格走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getNJxbId()).getNickName() +"\r\n"+ (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName()));
											}
										} else {
											if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
												if(isChecked == false){
													if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1){
														zkbdto.setJxbName("等级走班");
													}else if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
														zkbdto.setJxbName("合格走班");
													}
												}else{
													zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getNJxbId()).getName() + (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName()));
												}
												if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("等级走班")){
														zoubanType.add("等级走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}else if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("合格走班")){
														zoubanType.add("合格走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getNJxbId()).getName() +"\r\n"+ (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName()));
											}
										}
//										zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getStudentIds().size() : 0);
										zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getnJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getnJxbId())) : "");
									}
								} else {
									if (jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2) {
										zkbdto.setType(2);
									} else {
										zkbdto.setType(1);
									}
//									if (StringUtils.isNotEmpty(zkbdto.getTeacherId())) {
//										zkbdto.setTeacherName(teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())).getUserName() : "");
//									}
									if (StringUtils.isNotEmpty(zkbdto.getJxbId())) {
										zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getType() : 1);
										if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName())) {
											if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
												if(isChecked == false){
													if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
														zkbdto.setJxbName("等级走班");
													}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
														zkbdto.setJxbName("合格走班");
													}
												}else{
													zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getNickName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
												}
												if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("等级走班")){
														zoubanType.add("等级走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("合格走班")){
														zoubanType.add("合格走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getNickName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
											}
										} else {
											if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
												if(isChecked == false){
													if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
														zkbdto.setJxbName("等级走班");
													}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
														zkbdto.setJxbName("合格走班");
													}
												}else{
													zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
												}
												if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("等级走班")){
														zoubanType.add("等级走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("合格走班")){
														zoubanType.add("合格走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
											}
										}
//										zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getStudentIds().size() : 0);
										zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getJxbId())) : "");
									}
								}
							} else {
								if (jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2) {
									zkbdto.setType(2);
								} else {
									zkbdto.setType(1);
								}
//								if (StringUtils.isNotEmpty(zkbdto.getTeacherId())) {
//									zkbdto.setTeacherName(teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())).getUserName() : "");
//								}
								if (StringUtils.isNotEmpty(zkbdto.getJxbId())) {
									zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getType() : 1);
									if (zkbdto.getType() != 5) {
										if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName())) {
											if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){

												if(isChecked == false){
													if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
														zkbdto.setJxbName("等级走班");
													}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
														zkbdto.setJxbName("合格走班");
													}
												}else{
													zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getNickName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
												}
												if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("等级走班")){
														zoubanType.add("等级走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("合格走班")){
														zoubanType.add("合格走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getNickName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
											}
										} else {
											if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){

												if(isChecked == false){
													if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
														zkbdto.setJxbName("等级走班");
													}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
														zkbdto.setJxbName("合格走班");
													}
												}else{
													zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
												}
												if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("等级走班")){
														zoubanType.add("等级走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
													List<String> zoubanType = zkbdto.getZouBanType();
													if(!zkbdto.getZouBanType().contains("合格走班")){
														zoubanType.add("合格走班");
														zkbdto.setZouBanType(zoubanType);
													}
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
											}
										}
										zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getStudentIds().size() : 0);
										zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getJxbId())) : "");
									} else {
										N33_ZIXIKEEntry entry = zixikeEntries.get(new ObjectId(zkbdto.getJxbId()));
										zkbdto.setJxbName(entry.getName() + "(自习课)");
										zkbdto.setStudentCount(entry.getStudentIds().size());
										zkbdto.setRemarks("");
									}
								}
							}
							zkbdto.setSwType(0);
							zkbdto.setIsSWAndCourse(0);
//							if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(zkbEntry.getX()) + zkbEntry.getY())) {
//								zkbdto.setSwType(1);
//							}
							n33_zkbdtos1.add(zkbdto);
							zkbDTOMap.put(classId.toString(), n33_zkbdtos1);
							zkblist.add(zkbdto);
						}
					} else {
						List<N33_ZKBDTO> n33_zkbdtos1 = zkbDTOMap.get(classId.toString());
						if (n33_zkbdtos1 == null || n33_zkbdtos1.size() == 0) {
							n33_zkbdtos1 = new ArrayList<N33_ZKBDTO>();
						}
						N33_ZKBDTO zkbdto = new N33_ZKBDTO(zkbEntry);

						if (zkbdto.getNtid() != null) {
							if (count == 0) {
								if (jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2) {
									zkbdto.setType(2);
								} else {
									zkbdto.setType(1);
								}
//								if (StringUtils.isNotEmpty(zkbdto.getNtid())) {
//									zkbdto.setTeacherName(teaEntryMap.get(new ObjectId(zkbdto.getNtid())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getNtid())).getUserName() : "");
//								}
								if (StringUtils.isNotEmpty(zkbdto.getnJxbId())) {
									zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getType() : 1);
									if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getNickName())) {
										if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
											if(isChecked == false){
												if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1){
													zkbdto.setJxbName("等级走班");
												}else if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
													zkbdto.setJxbName("合格走班");
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getNJxbId()).getNickName() + (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName()));
											}
											if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("等级走班")){
													zoubanType.add("等级走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}else if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("合格走班")){
													zoubanType.add("合格走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}
										}else{
											zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getNJxbId()).getNickName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName()));
										}
									} else {
										if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){

											if(isChecked == false){
												if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1){
													zkbdto.setJxbName("等级走班");
												}else if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
													zkbdto.setJxbName("合格走班");
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getNJxbId()).getName() + (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName()));
											}
											if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 1){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("等级走班")){
													zoubanType.add("等级走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}else if(jxbEntries.get(zkbEntry.getNJxbId()).getType() == 2){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("合格走班")){
													zoubanType.add("合格走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}
										}else{
											zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getNJxbId()).getName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getNTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getNTeacherId()).getUserName()));
										}
									}
									zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getStudentIds().size() : 0);
									zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getnJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getnJxbId())) : "");
								}
							} else {
								if (jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2) {
									zkbdto.setType(2);
								} else {
									zkbdto.setType(1);
								}
//								if (StringUtils.isNotEmpty(zkbdto.getTeacherId())) {
//									zkbdto.setTeacherName(teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())).getUserName() : "");
//								}
								if (StringUtils.isNotEmpty(zkbdto.getJxbId())) {
									zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getType() : 1);
									if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName())) {
										if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){

											if(isChecked == false){
												if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
													zkbdto.setJxbName("等级走班");
												}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
													zkbdto.setJxbName("合格走班");
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getNickName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
											}
											if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("等级走班")){
													zoubanType.add("等级走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("合格走班")){
													zoubanType.add("合格走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}
										}else{
											zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getNickName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
										}
									} else {
										if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
											if(isChecked == false){
												if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
													zkbdto.setJxbName("等级走班");
												}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
													zkbdto.setJxbName("合格走班");
												}
											}else{
												zkbdto.setJxbName(jxbEntries.get(zkbEntry.getJxbId()).getName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
											}
											if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("等级走班")){
													zoubanType.add("等级走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("合格走班")){
													zoubanType.add("合格走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}
										}else{
											zkbdto.setJxbName(jxbEntries.get(zkbEntry.getJxbId()).getName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
										}
									}
									zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getStudentIds().size() : 0);
									zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getJxbId())) : "");
								}
							}
						} else {
							if (jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2) {
								zkbdto.setType(2);
							} else {
								zkbdto.setType(1);
							}
//							if (StringUtils.isNotEmpty(zkbdto.getTeacherId())) {
//								zkbdto.setTeacherName(teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())).getUserName() : "");
//							}
							if (StringUtils.isNotEmpty(zkbdto.getJxbId())) {
								zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getType() : 1);
								if (zkbdto.getType() != 5) {
									if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName())) {
										if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
											if(isChecked == false){
												if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
													zkbdto.setJxbName("等级走班");
												}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
													zkbdto.setJxbName("合格走班");
												}
											}else{
												zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getNickName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
											}
											if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("等级走班")){
													zoubanType.add("等级走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("合格走班")){
													zoubanType.add("合格走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}
										}else{
											zkbdto.setJxbName(jxbEntryMap.get(zkbEntry.getJxbId()).getNickName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
										}
									} else {
										if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1 || jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){

											if(isChecked == false){
												if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
													zkbdto.setJxbName("等级走班");
												}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
													zkbdto.setJxbName("合格走班");
												}
											}else{
												zkbdto.setJxbName(jxbEntries.get(zkbEntry.getJxbId()).getName() + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
											}
											if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 1){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("等级走班")){
													zoubanType.add("等级走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}else if(jxbEntries.get(zkbEntry.getJxbId()).getType() == 2){
												List<String> zoubanType = zkbdto.getZouBanType();
												if(!zkbdto.getZouBanType().contains("合格走班")){
													zoubanType.add("合格走班");
													zkbdto.setZouBanType(zoubanType);
												}
											}
										}else{
											zkbdto.setJxbName(jxbEntries.get(zkbEntry.getJxbId()).getName() + "\r\n" + (teaEntryMap1.get(zkbEntry.getTeacherId()) == null ? "" : teaEntryMap1.get(zkbEntry.getTeacherId()).getUserName()));
										}
									}
									zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getStudentIds().size() : 0);
									zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getJxbId())) : "");
								} else {
									N33_ZIXIKEEntry entry = zixikeEntries.get(new ObjectId(zkbdto.getJxbId()));
									zkbdto.setJxbName(entry.getName() + "(自习课)");
									zkbdto.setStudentCount(entry.getStudentIds().size());
									zkbdto.setRemarks("");
								}
							}
						}
						zkbdto.setSwType(0);
						zkbdto.setIsSWAndCourse(0);

//						if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(zkbEntry.getX()) + zkbEntry.getY())) {
//							zkbdto.setSwType(1);
//						}
						n33_zkbdtos1.add(zkbdto);
						zkbDTOMap.put(classId.toString(), n33_zkbdtos1);
						zkblist.add(zkbdto);
					}
				}
			}
			List<N33_ZKBDTO> zkbdtos = zkbDTOMap.get(classId.toString());
			if (zkbdtos != null) {
				for (N33_SWDTO swdto : swdtos) {
					boolean flag = true;
					for (N33_ZKBDTO zkbdto1 : zkbdtos) {
						if (zkbdto1.getX() == (swdto.getY() - 1) && zkbdto1.getY() == (swdto.getX() - 1)) {
							flag = false;
							zkbdto1.setIsSWAndCourse(1);
							zkbdto1.setSwType(1);
							if (zkbdto1.getSwDesc() != null && zkbdto1.getSwDesc() != "") {
								String s = zkbdto1.getSwDesc();
								s += "\r\n";
								s += swdto.getDesc();
								zkbdto1.setSwDesc(s);
							} else {
								zkbdto1.setSwDesc(swdto.getDesc());
							}
						}
					}
					if (flag) {
						N33_ZKBDTO zkbdto1 = new N33_ZKBDTO();
						zkbdto1.setX(swdto.getY() - 1);
						zkbdto1.setY(swdto.getX() - 1);
						zkbdto1.setSwType(1);
						zkbdto1.setIsSWAndCourse(0);
						zkbdto1.setSwDesc(swdto.getDesc());
						zkbdtos.add(zkbdto1);
					}
				}
			}

			List<ObjectId> gradeIds = new ArrayList<ObjectId>();
			gradeIds.add(gradeId);
			List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(termId, schoolId, gradeIds);
			if (zkbdtos != null) {
				for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
					boolean flag = true;
					for (N33_ZKBDTO zkbdto1 : zkbdtos) {
						if (zkbdto1.getX() == gdsxEntry.getX() && zkbdto1.getY() == gdsxEntry.getY()) {
							flag = false;
							zkbdto1.setIsSWAndCourse(1);
							zkbdto1.setSwType(1);
							if (zkbdto1.getSwDesc() != null && zkbdto1.getSwDesc() != "") {
								String s = zkbdto1.getSwDesc();
								s += "\r\n";
								s += gdsxEntry.getDesc();
								zkbdto1.setSwDesc(s);
							} else {
								zkbdto1.setSwDesc(gdsxEntry.getDesc());
							}
						}
					}
					if (flag) {
						N33_ZKBDTO zkbdto1 = new N33_ZKBDTO();
						zkbdto1.setX(gdsxEntry.getX());
						zkbdto1.setY(gdsxEntry.getY());
						zkbdto1.setSwType(1);
						zkbdto1.setIsSWAndCourse(0);
						zkbdto1.setSwDesc(gdsxEntry.getDesc());
						zkbdtos.add(zkbdto1);
					}
				}
			}
			zkbDTOMap.put(classId.toString(), zkbdtos);
		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("课表");

		List<String> weekLists = new ArrayList<String>();
		weekLists.add("周一");
		weekLists.add("周二");
		weekLists.add("周三");
		weekLists.add("周四");
		weekLists.add("周五");
//		weekLists.add("周六");
//		weekLists.add("周日");

		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
		for (int i = 1; i <= classDTOs.size() * weekLists.size(); i++) {
			sheet.setColumnWidth((short) i, (short) 5500);
		}

		HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
		headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
		headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
		headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
		headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle1.setWrapText(true);

		HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
		headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
		headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		headerStyle2.setWrapText(true);


		HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
		headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
		headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle3.setWrapText(true);

		if (classDTOs != null && classDTOs.size() != 0) {
			for (int i = 0; i < classDTOs.size(); i++) {
				sheet.addMergedRegion(new Region(0, (short) (i * weekLists.size() + 1), 0, (short) (i * weekLists.size() + weekLists.size())));
			}
		}
		//为sheet1生成第一行，用于放表头信息
		HSSFRow row = sheet.createRow(0);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("班级");
		cell.setCellStyle(headerStyle2);
		if (classDTOs != null && classDTOs.size() != 0) {
			int it = 1;
			for (int i = 0; i < classDTOs.size(); i++) {
				cell = row.createCell(it);
				cell.setCellStyle(headerStyle2);
				cell.setCellValue(classDTOs.get(i).getClassName());
				it += weekLists.size();
			}
		}
		row = sheet.createRow(1);
		row.setHeightInPoints(35);
		cell = row.createCell(0);
		cell.setCellValue("课节 / 日");
		cell.setCellStyle(headerStyle2);
		if (classDTOs != null && classDTOs.size() != 0) {
			for (int i = 0; i < classDTOs.size(); i++) {
				for (int j = 0; j < weekLists.size(); j++) {
					cell = row.createCell(j + weekLists.size() * i + 1);
					cell.setCellStyle(headerStyle2);
					cell.setCellValue(weekLists.get(j));
				}
			}
		}

		int page = 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < courseRangeDTOs.size(); i++) {
				page++;
				row = sheet.createRow(page);
				row.setHeightInPoints(50);
				cell = row.createCell(0);
				cell.setCellStyle(headerStyle2);
				cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
				for (int z = 0; z < classDTOs.size(); z++) {
					List<N33_ZKBDTO> zkbdtos = zkbDTOMap.get(classDTOs.get(z).getClassId());
					Map<String, N33_ZKBDTO> zkbmap = new HashMap<String, N33_ZKBDTO>();
					if (zkbdtos != null && zkbdtos.size() != 0) {
						for (N33_ZKBDTO zkbdto : zkbdtos) {
							zkbmap.put(zkbdto.getX() + "" + zkbdto.getY(), zkbdto);
						}
					}
					for (int x = 0; x < weekLists.size(); x++) {
						int y = 0;
						if (weekLists.get(x).indexOf("一") != -1) {
							y = 0;
						} else if (weekLists.get(x).indexOf("二") != -1) {
							y = 1;
						} else if (weekLists.get(x).indexOf("三") != -1) {
							y = 2;
						} else if (weekLists.get(x).indexOf("四") != -1) {
							y = 3;
						} else if (weekLists.get(x).indexOf("五") != -1) {
							y = 4;
						} else if (weekLists.get(x).indexOf("六") != -1) {
							y = 5;
						} else if (weekLists.get(x).indexOf("日") != -1) {
							y = 6;
						}
						cell = row.createCell(z * weekLists.size() + x + 1);
						cell.setCellStyle(headerStyle3);
						N33_ZKBDTO dto = zkbmap.get(y + "" + i);
						if (dto != null) {
							StringBuffer sb = new StringBuffer();
							if (dto.getIsSWAndCourse() == 0) {
								if (dto.getSwType() == 0) {
									if (dto.getType() == 2) {
										sb.append(dto.getJxbName());
									} else {
										if (dto.getJxbName() != null) {
											sb.append(dto.getJxbName() + "\r\n");
										}
									}
									String con = sb.toString();
									cell.setCellValue(con);
								} else {
									cell.setCellValue(dto.getSwDesc());
								}
							} else {
								if(null!=dto.getType()) {
									if (dto.getType() == 2) {
										sb.append(dto.getJxbName());
									} else {
										if (dto.getJxbName() != null) {
											sb.append(dto.getJxbName() + "\r\n");
										}
									}
								}
								sb.append("事务：" + dto.getSwDesc());
								String con = sb.toString();
								cell.setCellValue(con);
							}

						}
					}
				}
			}
		}
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setWrapText(true);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);

		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(gradeName + "课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 导出总课表
	 *
	 * @param termId
	 * @param gradeId
	 * @param classRoomIds
	 * @param weeks
	 * @param indexs
	 * @param schoolId
	 * @param week
	 * @return
	 */
	public void exportZKBData(ObjectId termId, String gradeId, List<String> classRoomIds, List<String> weeks, List<String> indexs, ObjectId schoolId, Integer week, HttpServletResponse response) {
		Integer count = week % 2;
		List<N33_ZKBEntry> zkbEntryList1 = zkbDao.getYKBsByclassRoomIds(termId, schoolId, week, new ObjectId(gradeId));
		ObjectId cid = null;
		if (zkbEntryList1.size() > 0) {
			cid = zkbEntryList1.get(0).getCId();
		} else {
			return;
		}

		List<ObjectId> gradeIds = new ArrayList<ObjectId>();
		gradeIds.add(new ObjectId(gradeId));
		List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(termId, schoolId, gradeIds);

		List<ObjectId> zxk = new ArrayList<ObjectId>();
		for (N33_ZKBEntry entry : zkbEntryList1) {
			if (entry.getType() == 5) {
				zxk.add(entry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxk);

		List<ObjectId> clasIds = MongoUtils.convertToObjectIdList(classRoomIds);
		List<ObjectId> kjIds = MongoUtils.convertToObjectIdList(indexs);
		//所有的课节
		List<CourseRangeDTO> allCourseRangeDTOs = courseRangeService.getListBySchoolIdZKB(schoolId.toString(), termId);
		List<N33_ClassroomDTO> classroomDTOs = classroomService.getRoomEntryListByXqGradeAndRoomIds(clasIds, cid, schoolId, new ObjectId(gradeId), 1);
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdAndKJIdsList(kjIds, schoolId.toString());

		Map<String, List<N33_ZKBDTO>> zkbDTOMap = new HashMap<String, List<N33_ZKBDTO>>();
		Map<String, List<N33_ZKBDTO>> zkbDTOMap2 = new HashMap<String, List<N33_ZKBDTO>>();
		List<String> swlist = new ArrayList<String>();
		//TermEntry termEntry = termDao.getTermByTimeId(termId);
		List<N33_SWDTO> swdtos = swService.getGuDingShiWuByXqid(termId);
		if (swdtos != null && swdtos.size() != 0) {
			for (N33_SWDTO swdto : swdtos) {
				swlist.add(swdto.getXy());
			}
		}
		List<N33_ZKBEntry> zkbEntryList = zkbDao.getYKBsByclassRoomIds(termId, schoolId, clasIds, week);
		if (zkbEntryList != null && zkbEntryList.size() != 0) {
			List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(zkbEntryList, "tid");
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, cid);
			List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(zkbEntryList, "jxbId");
			jxbIds.addAll(MongoUtils.getFieldObjectIDs(zkbEntryList, "nJxbId"));
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

			for (N33_ZKBEntry zkbEntry : zkbEntryList) {
				List<N33_ZKBDTO> n33_zkbdtos1 = zkbDTOMap.get(zkbEntry.getClassroomId().toString());
				if (n33_zkbdtos1 == null || n33_zkbdtos1.size() == 0) {
					n33_zkbdtos1 = new ArrayList<N33_ZKBDTO>();
				}
				N33_ZKBDTO zkbdto = new N33_ZKBDTO(zkbEntry);

				if (zkbdto.getNtid() != null) {
//					if (count == 0) {
//						if (StringUtils.isNotEmpty(zkbdto.getNtid())) {
//							zkbdto.setTeacherName(teaEntryMap.get(new ObjectId(zkbdto.getNtid())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getNtid())).getUserName() : "");
//						}
//						if (StringUtils.isNotEmpty(zkbdto.getnJxbId())) {
//							zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getType() : 1);
//							if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getNickName())) {
//								zkbdto.setJxbName(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getNickName());
//							} else {
//								zkbdto.setJxbName(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getName());
//							}
//							zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getStudentIds().size() : 0);
//							zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getnJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getnJxbId())) : "");
//						}
//					} else {
//						if (StringUtils.isNotEmpty(zkbdto.getTeacherId())) {
//							zkbdto.setTeacherName(teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())).getUserName() : "");
//						}
//						if (StringUtils.isNotEmpty(zkbdto.getJxbId())) {
//							zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getType() : 1);
//							if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName())) {
//								zkbdto.setJxbName(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName());
//							} else {
//								zkbdto.setJxbName(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getName());
//							}
//							zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getStudentIds().size() : 0);
//							zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getJxbId())) : "");
//						}
//					}
					
						if (StringUtils.isNotEmpty(zkbdto.getNtid())) {
							String tm = teaEntryMap.get(new ObjectId(zkbdto.getNtid())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getNtid())).getUserName() : "";
							String ntm = teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())).getUserName() : "";
							zkbdto.setTeacherName(ntm + "/" + tm);
						
						}
						if (StringUtils.isNotEmpty(zkbdto.getnJxbId())) {
							zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getType() : 1);
							String ncm = "";
							String cm = "";
							if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getNickName())) {
								cm = jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getNickName();
							} else {
								cm = jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getName();
							}
							if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName())) {
								ncm = jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName();
							} else {
								ncm = jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getName();
								
							}
							zkbdto.setJxbName(ncm + "/" + cm);
							zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getnJxbId())).getStudentIds().size() : 0);
							zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getnJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getnJxbId())) : "");
						}
				} else {
					if (StringUtils.isNotEmpty(zkbdto.getTeacherId())) {
						zkbdto.setTeacherName(teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(zkbdto.getTeacherId())).getUserName() : "");
					}
					if (StringUtils.isNotEmpty(zkbdto.getJxbId())) {
						zkbdto.setJxbType(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getType() : 1);
						if (zkbdto.getType() != 5) {
							if (StringUtils.isNotEmpty(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName())) {
								zkbdto.setJxbName(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getNickName());
							} else {
								zkbdto.setJxbName(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getName());
							}
							zkbdto.setStudentCount(jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())) != null ? jxbEntryMap.get(new ObjectId(zkbdto.getJxbId())).getStudentIds().size() : 0);
							zkbdto.setRemarks(tagMaps.get(new ObjectId(zkbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(zkbdto.getJxbId())) : "");
						} else {
							N33_ZIXIKEEntry entry = zixikeEntries.get(new ObjectId(zkbdto.getJxbId()));
							zkbdto.setJxbName(entry.getName() + "(自习课)");
							zkbdto.setStudentCount(entry.getStudentIds().size());
							zkbdto.setRemarks("");
						}

					}
				}
				zkbdto.setSwType(0);
				zkbdto.setIsSWAndCourse(0);
//				if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(zkbEntry.getX()) + zkbEntry.getY())) {
//					zkbdto.setSwType(1);
//				}
				n33_zkbdtos1.add(zkbdto);
				zkbDTOMap.put(zkbEntry.getClassroomId().toString(), n33_zkbdtos1);
			}

			for (Map.Entry<String, List<N33_ZKBDTO>> entry : zkbDTOMap.entrySet()) {
				List<N33_ZKBDTO> zkbdtos = entry.getValue();
				if (zkbdtos != null) {
					for (N33_SWDTO swdto : swdtos) {
						boolean flag = true;
						for (N33_ZKBDTO zkbdto1 : zkbdtos) {
							if (zkbdto1.getX() == (swdto.getY() - 1) && zkbdto1.getY() == (swdto.getX() - 1)) {
								flag = false;
								zkbdto1.setIsSWAndCourse(1);
								zkbdto1.setSwType(1);
								if (zkbdto1.getSwDesc() != null && zkbdto1.getSwDesc() != "") {
									String s = zkbdto1.getSwDesc();
									s += "\r\n";
									s += swdto.getDesc();
									zkbdto1.setSwDesc(s);
								} else {
									zkbdto1.setSwDesc(swdto.getDesc());
								}
							}
						}
						if (flag) {
							N33_ZKBDTO zkbdto1 = new N33_ZKBDTO();
							zkbdto1.setX(swdto.getY() - 1);
							zkbdto1.setY(swdto.getX() - 1);
							zkbdto1.setSwType(1);
							zkbdto1.setIsSWAndCourse(0);
							zkbdto1.setSwDesc(swdto.getDesc());
							zkbdtos.add(zkbdto1);
						}
					}
				}

				if (zkbdtos != null) {
					for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
						boolean flag = true;
						for (N33_ZKBDTO zkbdto1 : zkbdtos) {
							if (zkbdto1.getX() == gdsxEntry.getX() && zkbdto1.getY() == gdsxEntry.getY()) {
								flag = false;
								zkbdto1.setIsSWAndCourse(1);
								zkbdto1.setSwType(1);
								if (zkbdto1.getSwDesc() != null && zkbdto1.getSwDesc() != "") {
									String s = zkbdto1.getSwDesc();
									s += "\r\n";
									s += gdsxEntry.getDesc();
									zkbdto1.setSwDesc(s);
								} else {
									zkbdto1.setSwDesc(gdsxEntry.getDesc());
								}
							}
						}
						if (flag) {
							N33_ZKBDTO zkbdto1 = new N33_ZKBDTO();
							zkbdto1.setX(gdsxEntry.getX());
							zkbdto1.setY(gdsxEntry.getY());
							zkbdto1.setSwType(1);
							zkbdto1.setIsSWAndCourse(0);
							zkbdto1.setSwDesc(gdsxEntry.getDesc());
							zkbdtos.add(zkbdto1);
						}
					}
				}
				zkbDTOMap.put(entry.getKey().toString(), zkbdtos);
			}
		}
		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			for (N33_ClassroomDTO dto : classroomDTOs) {
				zkbDTOMap2.put(dto.getRoomId(), zkbDTOMap.get(dto.getRoomId()));
			}
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("课表");

		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
		for (int i = 1; i <= classroomDTOs.size() * weeks.size(); i++) {
			sheet.setColumnWidth((short) i, (short) 5500);
		}

		HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
		headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
		headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
		headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
		headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle1.setWrapText(true);

		HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
		headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
		headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		headerStyle2.setWrapText(true);


		HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
		headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
		headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle3.setWrapText(true);

		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			for (int i = 0; i < classroomDTOs.size(); i++) {
				sheet.addMergedRegion(new Region(0, (short) (i * weeks.size() + 1), 0, (short) (i * weeks.size() + weeks.size())));
			}
		}
		//为sheet1生成第一行，用于放表头信息
		HSSFRow row = sheet.createRow(0);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("教室 / 班级");
		cell.setCellStyle(headerStyle2);
		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			int it = 1;
			for (int i = 0; i < classroomDTOs.size(); i++) {
				cell = row.createCell(it);
				cell.setCellStyle(headerStyle2);
				if(classroomDTOs.get(i).getClassName() != null && !"".equals(classroomDTOs.get(i).getClassName())){
					cell.setCellValue(classroomDTOs.get(i).getRoomName() + "（" + classroomDTOs.get(i).getClassName() + "）");
				}else{
					cell.setCellValue(classroomDTOs.get(i).getRoomName() + "");
				}
				it += weeks.size();
			}
		}
		row = sheet.createRow(1);
		row.setHeightInPoints(35);
		cell = row.createCell(0);
		cell.setCellValue("课节 / 日");
		cell.setCellStyle(headerStyle2);
		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			for (int i = 0; i < classroomDTOs.size(); i++) {
				for (int j = 0; j < weeks.size(); j++) {
					cell = row.createCell(j + weeks.size() * i + 1);
					cell.setCellStyle(headerStyle2);
					cell.setCellValue(weeks.get(j));
				}
			}
		}
		int page = 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < allCourseRangeDTOs.size(); i++) {
				if (kjIds.contains(new ObjectId(allCourseRangeDTOs.get(i).getId()))) {
					page++;
					row = sheet.createRow(page);
					row.setHeightInPoints(50);
					cell = row.createCell(0);
					cell.setCellStyle(headerStyle2);
					cell.setCellValue(allCourseRangeDTOs.get(i).getName() + "\r\n" + allCourseRangeDTOs.get(i).getStart() + "~" + allCourseRangeDTOs.get(i).getEnd());
					for (int z = 0; z < classroomDTOs.size(); z++) {
						List<N33_ZKBDTO> zkbdtos = zkbDTOMap.get(classroomDTOs.get(z).getRoomId());
						Map<String, N33_ZKBDTO> zkbmap = new HashMap<String, N33_ZKBDTO>();
						if (zkbdtos != null && zkbdtos.size() != 0) {
							for (N33_ZKBDTO zkbdto : zkbdtos) {
								zkbmap.put(zkbdto.getX() + "" + zkbdto.getY(), zkbdto);
							}
						}
						for (int x = 0; x < weeks.size(); x++) {
							int y = 0;
							if (weeks.get(x).indexOf("一") != -1) {
								y = 0;
							} else if (weeks.get(x).indexOf("二") != -1) {
								y = 1;
							} else if (weeks.get(x).indexOf("三") != -1) {
								y = 2;
							} else if (weeks.get(x).indexOf("四") != -1) {
								y = 3;
							} else if (weeks.get(x).indexOf("五") != -1) {
								y = 4;
							} else if (weeks.get(x).indexOf("六") != -1) {
								y = 5;
							} else if (weeks.get(x).indexOf("日") != -1) {
								y = 6;
							}
							cell = row.createCell(z * weeks.size() + x + 1);
							cell.setCellStyle(headerStyle3);
							N33_ZKBDTO dto = zkbmap.get(y + "" + i);
							if (dto != null) {
								if (dto.getIsSWAndCourse() == 0) {
									if (dto.getSwType() == 0) {
										StringBuffer sb = new StringBuffer();
										if (dto.getJxbName() != null) {
											sb.append(dto.getJxbName()).append("(" + dto.getStudentCount() + ")\r\n");
										}
										if (dto.getTeacherName() != null) {
											sb.append(dto.getTeacherName());
										}
										String con = sb.toString();
										cell.setCellValue(con);
									} else {
										cell.setCellValue("全校事务");
									}
								} else {
									StringBuffer sb = new StringBuffer();
									if (dto.getType() == 2) {
										sb.append(dto.getJxbName());
									} else {
										if (dto.getJxbName() != null) {
											sb.append(dto.getJxbName() + "\r\n").append("人数:" + dto.getStudentCount() + "\r\n");
										}
									}
									sb.append("事务：" + dto.getSwDesc());
									String con = sb.toString();
									cell.setCellValue(con);
								}
							}
						}
					}
				}
			}
		}
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setWrapText(true);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);

		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param ykbId
	 * @param orgYkbId
	 */
	public String exchangeJXBByZkb(String ykbId, String orgYkbId, int sweek, int eweek, int index, ObjectId userId) {
		int week = DateTimeUtils.getWeekByDate(new Date());
		N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(new ObjectId(ykbId));
		N33_ZKBEntry orgZkbEntry = zkbDao.getN33_ZKBById(new ObjectId(orgYkbId));
		N33_JXZEntry jxzEntry = jxzDao.getJXZByDate(zkbEntry.getSchoolId(), zkbEntry.getTermId(), System.currentTimeMillis());
		int x1 = zkbEntry.getX();
		int x2 = orgZkbEntry.getX();
		String error = "";
		if (eweek != 0) {
			if (!getConflicted(zkbEntry, orgZkbEntry)) {
				return "不能相互调课！";
			}
//            if (week > x1 || week > x2) {
//                sweek = sweek + 1;
//            }
			for (int i = sweek; i <= eweek; i++) {
				N33_ZKBEntry zkb = zkbDao.getN33_ZKBById(zkbEntry.getClassroomId(),zkbEntry.getJxbId(),zkbEntry.getX(),zkbEntry.getY(),i,zkbEntry.getTermId(),zkbEntry.getGradeId());
				N33_ZKBEntry zkb2 = zkbDao.getN33_ZKBById(orgZkbEntry.getClassroomId(),orgZkbEntry.getJxbId(),orgZkbEntry.getX(),orgZkbEntry.getY(),i,orgZkbEntry.getTermId(),orgZkbEntry.getGradeId());
				if (zkb!=null && zkb2!=null) {
					zkbDao.updateN33_ZKBByWeekXY(zkbEntry, i, orgZkbEntry);
					zkbDao.updateN33_ZKBByWeekXY(orgZkbEntry, i, zkbEntry);
					N33_TiaoKeShengQingEntry entry = new N33_TiaoKeShengQingEntry(zkbEntry.getSchoolId(), zkbEntry.getTermId(), zkbEntry.getX(), zkbEntry.getY(), zkbEntry.getJxbId(), orgZkbEntry.getJxbId(), 2, orgZkbEntry.getX(), orgZkbEntry.getY(), zkbEntry.getTeacherId(), sweek);
					N33_TiaoKeShengQingEntry entry1 = new N33_TiaoKeShengQingEntry(zkbEntry.getSchoolId(), zkbEntry.getTermId(), orgZkbEntry.getX(), orgZkbEntry.getY(), orgZkbEntry.getJxbId(), zkbEntry.getJxbId(), 2, zkbEntry.getX(), zkbEntry.getY(), orgZkbEntry.getTeacherId(), sweek);
					tiaoKeShengQingDao.addEntry(entry);
					tiaoKeShengQingDao.addEntry(entry1);
				} else {
					error += i + ",";
				}
			}
			tkLogDao.addN33_TkLogEntry(new N33_TkLogEntry(new ObjectId(ykbId), new ObjectId(orgYkbId), zkbEntry.getSchoolId(), zkbEntry.getGradeId(), zkbEntry.getTermId(), userId,
					zkbEntry.getSubjectId(), orgZkbEntry.getSubjectId(), zkbEntry.getTeacherId(), orgZkbEntry.getTeacherId(), 0, sweek + "-" + eweek, 1, 1, zkbEntry.getX() + "," + zkbEntry.getY(), orgZkbEntry.getX() + "," + orgZkbEntry.getY(),orgZkbEntry.getJxbId(),zkbEntry.getJxbId()));
		} else {
			if (jxzEntry != null) {
				if (jxzEntry.getSerial() >= index && (week > x1+1 || week > x2+1)) {
					return "只能调在当天之后的课！";
				}
			}
//			int status = 1;
//			N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(zkbEntry.getSchoolId(),zkbEntry.getGradeId(),zkbEntry.getCId(),2);
//			if (turnOff != null) {
//				status = turnOff.getStatus();
//			}
//			if (status==0) {
//				x1 = x1 + 1;
//				x2 = x2 + 1;
//			}

			if (!getConflicted(zkbEntry, orgZkbEntry)) {
				return "有冲突，不能相互调课！";
			}
			if (!getConflictedByTeacherIds(zkbEntry, orgZkbEntry)) {
				return "有冲突，不能相互调课！";
			}
			zkbDao.updateN33_ZKB(zkbEntry, orgZkbEntry);
			zkbDao.updateN33_ZKB(orgZkbEntry, zkbEntry);

			N33_TiaoKeShengQingEntry entry = new N33_TiaoKeShengQingEntry(zkbEntry.getSchoolId(), zkbEntry.getTermId(), zkbEntry.getX(), zkbEntry.getY(), zkbEntry.getJxbId(), orgZkbEntry.getJxbId(), 2, orgZkbEntry.getX(), orgZkbEntry.getY(), zkbEntry.getTeacherId(), sweek);
			N33_TiaoKeShengQingEntry entry1 = new N33_TiaoKeShengQingEntry(zkbEntry.getSchoolId(), zkbEntry.getTermId(), orgZkbEntry.getX(), orgZkbEntry.getY(), orgZkbEntry.getJxbId(), zkbEntry.getJxbId(), 2, zkbEntry.getX(), zkbEntry.getY(), orgZkbEntry.getTeacherId(), sweek);
			tiaoKeShengQingDao.addEntry(entry);
			tiaoKeShengQingDao.addEntry(entry1);
			tkLogDao.addN33_TkLogEntry(new N33_TkLogEntry(new ObjectId(ykbId), new ObjectId(orgYkbId), zkbEntry.getSchoolId(), zkbEntry.getGradeId(), zkbEntry.getTermId(), userId,
					zkbEntry.getSubjectId(), orgZkbEntry.getSubjectId(), zkbEntry.getTeacherId(), orgZkbEntry.getTeacherId(), 0, String.valueOf(sweek), 1, 0, zkbEntry.getX() + "," + zkbEntry.getY(), orgZkbEntry.getX() + "," + orgZkbEntry.getY(),orgZkbEntry.getJxbId(),zkbEntry.getJxbId()));
		}
		List<String> userIds = new ArrayList<String>();
		userIds.add(orgZkbEntry.getTeacherId().toString());
		userIds.add(zkbEntry.getTeacherId().toString());
		if (orgZkbEntry.getNTeacherId() != null) {
			userIds.add(orgZkbEntry.getNTeacherId().toString());
		}
		if (zkbEntry.getNTeacherId() != null) {
			userIds.add(zkbEntry.getTeacherId().toString());
		}
		faTongZhiService.faTongZhi("调课通知,请注意查看新的课表", zkbEntry.getSchoolId().toString(), userId.toString(), userIds);
		if (StringUtils.isNotEmpty(error)) {
			return "第"+error+"周未能调课，原因：课节不一致！";
		}
		return "交换成功！";
	}

	private boolean getConflictedByTeacherIds(N33_ZKBEntry zkbEntry, N33_ZKBEntry orgZkbEntry) {
		List<N33_ZKBEntry> zkbEntries1 = zkbDao.getN33_ZKBByWeekTea(zkbEntry.getTermId(), zkbEntry.getWeek(), zkbEntry.getTeacherId());
		List<N33_ZKBEntry> zkbEntries2 = zkbDao.getN33_ZKBByWeekTea(orgZkbEntry.getTermId(), orgZkbEntry.getWeek(), orgZkbEntry.getTeacherId());
		if (zkbEntries1 != null && zkbEntries1.size() != 0) {
			for (N33_ZKBEntry zkbEntry1 : zkbEntries1) {
				if (orgZkbEntry.getX() == zkbEntry1.getX() && orgZkbEntry.getY() == zkbEntry1.getY()) {
					return false;
				}
			}
		}

		if (zkbEntries2 != null && zkbEntries2.size() != 0) {
			for (N33_ZKBEntry zkbEntry2 : zkbEntries2) {
				if (zkbEntry.getX() == zkbEntry2.getX() && zkbEntry.getY() == zkbEntry2.getY()) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean getConflicted(N33_ZKBEntry zkbEntry, N33_ZKBEntry orgZkbEntry) {
		List<N33_JXBCTEntry> jxbctEntrys = jxbctDao.getJXBCTEntrysByJXB(zkbEntry.getSchoolId(), zkbEntry.getID(), orgZkbEntry.getID());
		if (jxbctEntrys != null && jxbctEntrys.size() != 0) {
			return false;
		}
		List<N33_SWDTO> swdtos = swService.getSwByXqidAndUserId(zkbEntry.getCId(), zkbEntry.getTeacherId());
		List<N33_SWDTO> swdtos2 = swService.getSwByXqidAndUserId(orgZkbEntry.getCId(), orgZkbEntry.getTeacherId());
		if (swdtos != null && swdtos.size() != 0) {
			for (N33_SWDTO swdto : swdtos) {
				if (swdto.getX() == orgZkbEntry.getX() && swdto.getY() == orgZkbEntry.getY()) {
					return false;
				}
			}
		}
		if (swdtos2 != null && swdtos2.size() != 0) {
			for (N33_SWDTO swdto : swdtos2) {
				if (swdto.getX() == zkbEntry.getX() && swdto.getY() == zkbEntry.getY()) {
					return false;
				}
			}
		}
		return true;
	}

	public List<Map<String, String>> getConflictedSettledJXBByZKBId(String zkbId) {
		N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(new ObjectId(zkbId));
		List<Map<String,String>> retList = new ArrayList<Map<String, String>>();
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(zkbEntry.getJxbId());
		Map zkbMap = getKeBiaoList(zkbEntry.getTermId(), zkbEntry.getGradeId().toString(), zkbEntry.getClassroomId().toString(), "", "", zkbEntry.getSchoolId(), zkbEntry.getWeek());
		Map<String,List<N33_ZKBDTO>> ykbdtosMap = (Map<String, List<N33_ZKBDTO>>) zkbMap.get("ykbdto");
		List<N33_ZKBDTO> ykbdtos = ykbdtosMap.get(zkbEntry.getClassroomId().toString());
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(jxbEntry.getSchoolId(), jxbEntry.getTermId());
		if(jxbEntry.getType() == 6){
			jxbIds.add(zkbEntry.getJxbId());
			jxbIds.add(jxbEntry.getRelativeId());

		}else{
			jxbIds.add(zkbEntry.getJxbId());
		}
		List<N33_JXBCTEntry> jxbctEntryList = jxbctDao.getJxbCTByjxbIds(jxbIds);
		Map<String,N33_JXBCTEntry> jxbctEntryMap = new HashMap<String, N33_JXBCTEntry>();
		List<ObjectId> ctJxbIds = new ArrayList<ObjectId>();
		for (N33_JXBCTEntry jxbctEntry : jxbctEntryList) {
			jxbctEntryMap.put(jxbctEntry.getJxbId().toString() + "" + jxbctEntry.getOjxbId().toString(),jxbctEntry);
			jxbctEntryMap.put(jxbctEntry.getOjxbId().toString() + "" + jxbctEntry.getJxbId().toString(),jxbctEntry);
			if(!ctJxbIds.contains(jxbctEntry.getOjxbId())){
				ctJxbIds.add(jxbctEntry.getOjxbId());
			}
		}
		List<ObjectId> roomIds = new ArrayList<ObjectId>();
		if(jxbEntry.getType() != 4 && jxbEntry.getClassroomId() != null){
			roomIds.add(jxbEntry.getClassroomId());
		}else{
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(zkbEntry.getJxbId(),jxbEntry.getTermId());
			for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
				if(zhuanXiangEntry.getRoomId() != null){
					roomIds.add(zhuanXiangEntry.getRoomId());
				}
			}
		}

		List<N33_ZKBEntry> roomYkbEntryList = zkbDao.getYKBsByclassRoomIds(zkbEntry.getTermId(), zkbEntry.getSchoolId(),roomIds,zkbEntry.getWeek());

		List<N33_ZKBEntry> ykbEntryList = zkbDao.getYKBEntrysByJXBIdsOrNJxbIds(zkbEntry.getTermId(),ctJxbIds,zkbEntry.getSchoolId(),zkbEntry.getWeek());

		if(jxbEntry.getType() != 0){
			for (N33_ZKBEntry ykbEntry : ykbEntryList) {
				if(!ykbEntry.getClassroomId().toString().equals(zkbEntry.getClassroomId().toString())){
					boolean flag = true;
					if(jxbEntry.getType() == 6){
						N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(jxbEntry.getRelativeId());
						if((ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry.getTercherId()) && jxbEntry.getDanOrShuang() == 1)
								|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry.getTercherId()) && jxbEntry.getDanOrShuang() == 2)
								|| (ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 1)
								|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 2)
								|| (jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1)
								|| (jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3)
								|| (jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1)
								|| jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3
								|| (ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1)
								|| (ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3)
								|| (ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId())!= null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId()).getCtType() == 1)
								|| (ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId()) != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId()).getCtType() == 3)){
							for (Map<String,String> map : retList) {
								if(Integer.valueOf(map.get("x")).intValue() == ykbEntry.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry.getY()){
									flag = false;
									break;
								}
							}
						}else{
							flag = false;
						}
					}else{
						for (Map<String,String> map : retList) {
							if(Integer.valueOf(map.get("x")).intValue() == ykbEntry.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry.getY()){
								flag = false;
								break;
							}
						}
					}
					if(flag){
						Map<String,String> map = getCTDESCMap3(jxbctEntryMap,jxbEntry,ykbEntry,jxbEntryMap);
						retList.add(map);
					}
				}
			}
		}

		if(jxbEntry.getType() != 0){
			for (N33_ZKBEntry ykbEntry1 : roomYkbEntryList) {
				if(ykbEntry1.getJxbId() != null && ykbEntry1.getJxbId().toString().equals(jxbEntry.getID().toString())){
					boolean flag = true;
					for (Map<String,String> map : retList) {
						if(Integer.valueOf(map.get("x")).intValue() == ykbEntry1.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry1.getY()){
							flag = false;
							break;
						}
					}
					if(flag){
						Map<String,String> map = new HashMap<String, String>();
						map.put("x",String.valueOf(ykbEntry1.getX()));
						map.put("y",String.valueOf(ykbEntry1.getY()));
						map.put("msg","同一个教学班");
						retList.add(map);
					}
				}
			}
		}

		List<N33_ZKBEntry> ykbEntrysList = zkbDao.getZKBbyXY(zkbEntry.getTermId(),null,zkbEntry.getX(),zkbEntry.getY(),jxbEntry.getSchoolId(),1);

		for (N33_ZKBDTO ykbDto : ykbdtos) {
			if(ykbDto.getIsUse() != 1){
				continue;
			}
			if(ykbDto.getJxbId() == null || "".equals(ykbDto.getJxbId())){
				continue;
			}
			N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(new ObjectId(ykbDto.getJxbId()));
			List<ObjectId> jxbIds1 = new ArrayList<ObjectId>();
			if(ykbDto.getType() == 6){
				jxbIds1.add(new ObjectId(ykbDto.getJxbId()));
				jxbIds1.add(new ObjectId(ykbDto.getnJxbId()));
			}else{
				jxbIds1.add(new ObjectId(ykbDto.getJxbId()));
			}

			List<N33_JXBCTEntry> jxbctEntryList1 = jxbctDao.getJxbCTByjxbIds(jxbIds1);
			Map<String,N33_JXBCTEntry> jxbctEntryMap2 = new HashMap<String, N33_JXBCTEntry>();
			List<ObjectId> jxbctids = new ArrayList<ObjectId>();
			for (N33_JXBCTEntry jxbctEntry : jxbctEntryList1) {
				jxbctEntryMap2.put(jxbctEntry.getJxbId().toString() + "" + jxbctEntry.getOjxbId().toString(),jxbctEntry);
				jxbctEntryMap2.put(jxbctEntry.getOjxbId().toString() + "" + jxbctEntry.getJxbId().toString(),jxbctEntry);
				jxbctids.add(jxbctEntry.getOjxbId());
			}
			for(N33_ZKBEntry ykbEntry : ykbEntrysList){
				boolean flag = false;
				if(ykbEntry.getType() == 6 && ykbDto.getType() == 6 && (jxbctids.contains(ykbEntry.getJxbId()) || jxbctids.contains(ykbEntry.getNJxbId()))){
					flag = true;
					N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(new ObjectId(ykbDto.getnJxbId()));
					if((ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 1)
							|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 2)
							|| (ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry2.getTercherId()) && jxbEntry2.getDanOrShuang() == 1)
							|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry2.getTercherId()) && jxbEntry2.getDanOrShuang() == 2)){
						for (Map<String,String> map : retList) {
							if(Integer.valueOf(map.get("x")).intValue() == ykbDto.getX() && Integer.valueOf(map.get("y")).intValue() == ykbDto.getY()){
								flag = false;
								break;
							}
						}
					}else{
						flag = false;
					}
				}else if(jxbctids.contains(ykbEntry.getJxbId()) || (ykbEntry.getNJxbId() != null && jxbctids.contains(ykbEntry.getNJxbId()))){
					flag = true;
					for (Map<String,String> map : retList) {
						if(Integer.valueOf(map.get("x")).intValue() == ykbDto.getX() && Integer.valueOf(map.get("y")).intValue() == ykbDto.getY()){
							flag = false;
							break;
						}
					}
				}
				if(zkbEntry.getClassroomId().toString().equals(ykbEntry.getClassroomId().toString())){
					flag = false;
				}
				if(flag){
					N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(ykbEntry.getJxbId());
					Map<String,String> map = getCTDESCMap2(jxbctEntryMap2,jxbEntry2,ykbDto.buildEntry(),jxbEntryMap,zkbEntry.getX(),zkbEntry.getY());
					retList.add(map);
				}
			}
		}
		return retList;
	}

	/**
	 * @param zkbId
	 * @return
	 */
//	public Map getConflictedSettledJXBByZKBId(String zkbId) {
//		Map map = new HashMap();
//		N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(new ObjectId(zkbId));
//		//教学班教学班之间的冲突
//		List<N33_JXBCTEntry> jxbctEntrys = jxbctDao.getJXBCTEntrysByJXB(zkbEntry.getSchoolId(), zkbEntry.getJxbId());
//		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
//		List<N33_ZKBDTO> zkbdtos = new ArrayList<N33_ZKBDTO>();
//		if (jxbctEntrys != null && jxbctEntrys.size() != 0) {
//			jxbIds = MongoUtils.getFieldObjectIDs(jxbctEntrys, "ojxbId");
//		}
//		jxbIds.add(zkbEntry.getJxbId());
//		List<N33_ZKBEntry> zkbEntryList = zkbDao.getZKBEntrysByJXBIds(zkbEntry.getTermId(), jxbIds, zkbEntry.getSchoolId(),zkbEntry.getClassroomId(),zkbEntry.getWeek());
//		if (zkbEntryList != null && zkbEntryList.size() != 0) {
//			for (N33_ZKBEntry entry : zkbEntryList) {
//				zkbdtos.add(new N33_ZKBDTO(entry));
//			}
//		}
//		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(zkbEntry.getTermId());
//		//事务
//		List<N33_SWDTO> swdtos = swService.getSwByXqidAndUserId(termEntry.getID(), zkbEntry.getTeacherId());
//		map.put("jxbcts", zkbdtos);
//		map.put("swcts", swdtos);
//		return map;
//	}

	/**
	 * @param ykbId
	 * @param schoolId
	 * @param teacherId
	 * @param sweek
	 * @param eweek
	 * @param index
	 * @return
	 */
//	public String dkJXB(String ykbId, ObjectId schoolId, String teacherId, int sweek, int eweek, int week2,String index, ObjectId userId) {
////		int week = DateTimeUtils.getWeekByDate(new Date());
////		N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(new ObjectId(ykbId));
////		N33_JXZEntry jxzEntry = jxzDao.getJXZByDate(zkbEntry.getSchoolId(), zkbEntry.getTermId(), System.currentTimeMillis());
////		ObjectId oteacherId = zkbEntry.getTeacherId();
////		int x1 = zkbEntry.getX();
////		if (eweek != 0) {
////			if (week > x1) {
////				sweek = sweek + 1;
////			}
////			String mesg = getConflicted(zkbEntry, new ObjectId(teacherId), 0);
////			if (!"true".equals(mesg)) {
////				return mesg;
////			}
////			for (int i = sweek; i <= eweek; i++) {
////				zkbDao.updateN33_ZKB(zkbEntry, i, new ObjectId(teacherId));
////			}
////			tkLogDao.addN33_TkLogEntry(new N33_TkLogEntry(new ObjectId(ykbId), null, zkbEntry.getSchoolId(), zkbEntry.getGradeId(), zkbEntry.getTermId(), userId,
////					zkbEntry.getSubjectId(), null, oteacherId, new ObjectId(teacherId), 1, sweek + "-" + eweek, 1, 1, zkbEntry.getX() + "," + zkbEntry.getY(), ""));
////		} else {
//////			N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(zkbEntry.getSchoolId(),zkbEntry.getGradeId(),zkbEntry.getCId(),2);
//////			int status = 1;
//////			if (turnOff != null) {
//////				status = turnOff.getStatus();
//////			}
//////			if (status==0) {
//////				x1 = x1 + 1;
//////			}
////			if (jxzEntry.getSerial() >= week2 && week > x1+1) {
////				return "只能调在当天之后的课！";
////			}
////			String mesg = getConflicted(zkbEntry, new ObjectId(teacherId), 0);
////			if (!"true".equals(mesg)) {
////				return mesg;
////			}
////			zkbEntry.setTeacherId(new ObjectId(teacherId));
////			zkbDao.updateN33_ZKB(zkbEntry);
////			tkLogDao.addN33_TkLogEntry(new N33_TkLogEntry(new ObjectId(ykbId), null, zkbEntry.getSchoolId(), zkbEntry.getGradeId(), zkbEntry.getTermId(), userId,
////					zkbEntry.getSubjectId(), null, oteacherId, new ObjectId(teacherId), 1, String.valueOf(sweek), 1, 0, zkbEntry.getX() + "," + zkbEntry.getY(), ""));
////		}
////		return "代课成功！";
////	}


	public String dkJXB(String ykbId,String orgTeacherId, ObjectId schoolId, String teacherId, int sweek, int eweek, int week2,String index, ObjectId userId) {
		N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(new ObjectId(ykbId));
		List<ObjectId> userIds = new ArrayList<ObjectId>();
		userIds.add(new ObjectId(orgTeacherId));
		List<Integer> weekList = new ArrayList<Integer>();
		for (int i = sweek; i <= eweek; i++) {
				weekList.add(i);
		}
		String[] args = index.split(",");
		Map<String,N33_ZKBEntry> zkbEntryMap = new HashMap<String, N33_ZKBEntry>();
		List<N33_ZKBEntry> zkbEntries = zkbDao.getZKBEntrysListByTeacherIds2(zkbEntry.getTermId(), zkbEntry.getSchoolId(),weekList,userIds);
		if (zkbEntries!=null && zkbEntries.size()!=0) {
			for (N33_ZKBEntry e : zkbEntries) {
				String s = ""+e.getX()+e.getY();
				if (e.getTeacherId().equals(new ObjectId(orgTeacherId))) {
					zkbEntryMap.put(e.getTeacherId().toString()+e.getWeek()+s+"1",e);
				}
				if (e.getNTeacherId()!=null && e.getNTeacherId().equals(new ObjectId(orgTeacherId))) {
					zkbEntryMap.put(e.getNTeacherId().toString()+e.getWeek()+s+"2",e);
				}
			}
		}
		String message = "";
		for (int i = sweek; i <= eweek; i++) {
			for (int j=0;j<args.length;j++) {
				if (StringUtils.isNotEmpty(args[j])) {
					String str1 = orgTeacherId + i+args[j]+"1";
					String str2 = orgTeacherId + i+args[j]+"2";
					N33_ZKBEntry zkb1 = zkbEntryMap.get(str1);
					N33_ZKBEntry zkb2 = zkbEntryMap.get(str2);
					if (zkb1!=null) {
						zkbDao.updateN33_ZKB(zkb1, new ObjectId(teacherId),1);
						tkLogDao.addN33_TkLogEntry(new N33_TkLogEntry(zkb1.getID(), null, zkb1.getSchoolId(), zkb1.getGradeId(), zkb1.getTermId(), userId,
					zkbEntry.getSubjectId(), null, new ObjectId(orgTeacherId), new ObjectId(teacherId), 1, String.valueOf(i), 1, 1, zkb1.getX() + "," + zkb1.getY(), "",null,zkb1.getJxbId()));
					} else if (zkb2!=null) {
						zkbDao.updateN33_ZKB(zkb2, new ObjectId(teacherId),2);
						tkLogDao.addN33_TkLogEntry(new N33_TkLogEntry(zkb2.getID(), null, zkb2.getSchoolId(), zkb2.getGradeId(), zkb2.getTermId(), userId,
								zkbEntry.getSubjectId(), null, new ObjectId(orgTeacherId), new ObjectId(teacherId), 1, String.valueOf(i), 1, 1, zkb2.getX() + "," + zkb2.getY(), "",null,zkb2.getJxbId()));
					} else {
						message += "第"+i+"周 周"+args[j].substring(0,1)+"第"+args[j].substring(1,args[j].length())+"节，";
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(message)) {
			message += "课表不相同！";
		} else {
			message = "代课成功！";
		}
		return message;
	}

	private String getConflicted(N33_ZKBEntry zkbEntry, ObjectId teacherId, int week) {
		List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeekTid(zkbEntry, teacherId, week);
//        TermEntry termEntry = termDao.getTermByTimeId(zkbEntry.getTermId());
		List<N33_ZKBEntry> zkbEntryList2 = zkbDao.getN33_ZKBByWeekTea(zkbEntry.getTermId(), zkbEntry.getWeek(), teacherId);
		if (zkbEntryList2 != null && zkbEntryList2.size() != 0) {
			for (N33_ZKBEntry entry : zkbEntryList2) {
				if (zkbEntry.getX() == entry.getX() && zkbEntry.getY() == entry.getY()) {
					return "该课节老师有课，不可代课！";
				}
			}
		}
		if (zkbEntryList != null && zkbEntryList.size() != 0) {
			return "该课节老师有课，不可代课！";
		} else {
			boolean flg = false;
			List<N33_SWDTO> swdtos = swService.getSwByXqidAndUserId(zkbEntry.getTermId(), teacherId);
			if (swdtos != null && swdtos.size() != 0) {
				for (N33_SWDTO dto : swdtos) {
					if (dto.getX() - 1 == zkbEntry.getX() && dto.getY() - 1 == zkbEntry.getY()) {
						flg = true;
					}
				}
			}
			if (flg) {
				return "该课节老师有事务，不可代课！";
			} else {
				return "true";
			}
		}
	}

	public Map getJXBByXY(int x, int y, ObjectId gradeId, ObjectId xqid, ObjectId schoolId) {
		List<N33_XKBDTO> xkbdtos = new ArrayList<N33_XKBDTO>();
		Map map = new HashMap();
		Map<String, List<N33_YKBDTO>> ykbdtoMaps = new HashMap<String, List<N33_YKBDTO>>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		Map<ObjectId, ObjectId> jxbClrIdMap = new HashMap<ObjectId, ObjectId>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(xqid, schoolId, gradeId);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
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
		List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
		List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBListByIds(jxbIds);
		List<ObjectId> tagIds = new ArrayList<ObjectId>();
		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
		List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(xqid);
		Map<ObjectId, N33_ClassroomEntry> classroomMap = classroomService.getRoomListMapBySchoolId(schoolId, xqid);
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
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, xqid);
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
			ykbdtoMaps = getYKBbyTeacherIds(teacherIds, xqid, schoolId);

			if (teacherIds != null && teacherIds.size() != 0) {
				List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), xqid);
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


	/**
	 * 获取教学班
	 *
	 * @param ykbId
	 * @param gradeId
	 * @param schoolId
	 * @return
	 */
	public Map getJXBById(String ykbId, String gradeId, ObjectId schoolId) {
		List<N33_XKBDTO> xkbdtos = new ArrayList<N33_XKBDTO>();
		Map map = new HashMap();
		Map<String, List<N33_YKBDTO>> ykbdtoMaps = new HashMap<String, List<N33_YKBDTO>>();
		N33_YKBEntry ykbEntry = ykbDao.getN33_YKBById(new ObjectId(ykbId));
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		Map<ObjectId, ObjectId> jxbClrIdMap = new HashMap<ObjectId, ObjectId>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(ykbEntry.getTermId(), ykbEntry.getSchoolId(), new ObjectId(gradeId));
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
				if (ykbEntry.getX() == entry.getX() && ykbEntry.getY() == entry.getY()) {
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
		List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(ykbEntry.getTermId());
		Map<ObjectId, N33_ClassroomEntry> classroomMap = classroomService.getRoomListMapBySchoolId(schoolId, ykbEntry.getTermId());
		if (jxbEntryList != null && jxbEntryList.size() != 0) {
			for (N33_JXBEntry entry : jxbEntryList) {
				if (entry.getType() == 4) {
					for (N33_ZhuanXiangEntry zxentry : zhuanXiangEntryList) {
						if (entry.getID().equals(zxentry.getJxbId()) && zxentry.getTeaId() != null) {
							if (ykbEntry.getNTeacherId() != null && !ykbEntry.getNTeacherId().toString().equals(zxentry.getTeaId().toString())) {
								teacherIds.add(zxentry.getTeaId());
							} else if (ykbEntry.getTeacherId() != null && !ykbEntry.getTeacherId().toString().equals(zxentry.getTeaId().toString())) {
								teacherIds.add(zxentry.getTeaId());
							}else if(ykbEntry.getTeacherId() == null){
								teacherIds.add(zxentry.getTeaId());
							}
						}
					}
				} else {
					tagIds.addAll(entry.getTagIds());
					if (entry.getTercherId() != null) {
						if (ykbEntry.getNTeacherId() != null && !ykbEntry.getNTeacherId().toString().equals(entry.getTercherId().toString())) {
							teacherIds.add(entry.getTercherId());
						} else if (ykbEntry.getTeacherId() != null && !ykbEntry.getTeacherId().toString().equals(entry.getTercherId().toString())) {
							teacherIds.add(entry.getTercherId());
						}else if(ykbEntry.getTeacherId() == null){
							teacherIds.add(entry.getTercherId());
						}
					}
				}
			}
			if (ykbEntry.getNTeacherId() != null) {
				((LinkedList<ObjectId>) teacherIds).addFirst(ykbEntry.getNTeacherId());
			}
			if (ykbEntry.getTeacherId() != null) {
				((LinkedList<ObjectId>) teacherIds).addFirst(ykbEntry.getTeacherId());
			}
			Map<ObjectId, N33_StudentTagEntry> studentTagEntryMap = studentTagDao.getTagListByIdsMap(tagIds);
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, ykbEntry.getTermId());
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
			ykbdtoMaps = getYKBbyTeacherIds(teacherIds, ykbEntry.getTermId(), ykbEntry.getSchoolId());

			if (teacherIds != null && teacherIds.size() != 0) {
				List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), ykbEntry.getTermId());
				for (ObjectId tid : teacherIds) {
					boolean flag = true;
					for (N33_XKBDTO xkbdto : xkbdtos){
						if(tid.toString().equals(xkbdto.getTeacherId())){
							flag = false;
						}
					}
					if(flag){
						N33_XKBDTO dto = new N33_XKBDTO();
						if(teaEntryMap.get(tid) == null){
							dto.setTeacherName("");
						}else{
							dto.setTeacherName(teaEntryMap.get(tid).getUserName());
						}
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
	public Map<String, List<N33_YKBDTO>> getYKBbyTeacherIds(List<ObjectId> teacherIds, ObjectId ciId, ObjectId schoolId) {
		Map<ObjectId,N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomEntryListByXqGradeForMap(ciId,schoolId);
		Map<String, String> subMap = subjectService.getSubjectMapById(schoolId, ciId);
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(ciId, schoolId);
		List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(schoolId, ciId);
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(schoolId, ciId);
		Map<String, List<N33_YKBDTO>> ykbMaps = new HashMap<String, List<N33_YKBDTO>>();
		Map<ObjectId, List<N33_YKBEntry>> ykbMapByJXBID = new HashMap<ObjectId, List<N33_YKBEntry>>();
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
				List<N33_YKBEntry> ykbs = ykbMapByJXBID.get(entry.getJxbId());
				if (ykbs == null || ykbs.size() == 0) {
					ykbs = new ArrayList<N33_YKBEntry>();
				}
				ykbs.add(entry);
				ykbMapByJXBID.put(entry.getJxbId(), ykbs);
				if (entry.getNJxbId() != null) {
					List<N33_YKBEntry> ykbs2 = ykbMapByJXBID.get(entry.getNJxbId());
					if (ykbs2 == null || ykbs2.size() == 0) {
						ykbs2 = new ArrayList<N33_YKBEntry>();
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
			List<N33_YKBDTO> ykbdtos = new ArrayList<N33_YKBDTO>();
			List<ObjectId> jxbIds = teacherJXBByIdMap.get(tid);
			if (jxbIds != null && jxbIds.size() != 0) {
				for (ObjectId id : jxbIds) {
					List<N33_YKBEntry> n33YkbEntries = ykbMapByJXBID.get(id);
					if (n33YkbEntries != null && n33YkbEntries.size() != 0) {
						for (N33_YKBEntry entry : n33YkbEntries) {
							N33_YKBDTO dto = new N33_YKBDTO(entry);
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
							dto.setClassRoomName(classroomEntryMap.get(entry.getClassroomId()) != null ? classroomEntryMap.get(entry.getClassroomId()).getRoomName() : "");
							ykbdtos.add(dto);
						}
					}
				}
			}
			ykbMaps.put(tid.toString(), ykbdtos);
		}
		return ykbMaps;
	}

	/**
	 * @param gradeId
	 * @param termId
	 * @param userId
	 * @param userRole
	 * @param schoolId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Map getTiaoKeLog(String gradeId, String termId, String ci, ObjectId userId, int userRole, ObjectId schoolId, String userName, int page, int pageSize) {
		Map map = new HashMap();
		List<ObjectId> uids = new ArrayList<ObjectId>();
		if (StringUtils.isNotEmpty(userName)) {
			List<N33_TeaEntry> teaEntries = teaDao.getTeaList(userName, schoolId, new ObjectId(ci));
			if (teaEntries != null && teaEntries.size() != 0) {
				uids = MongoUtils.getFieldObjectIDs(teaEntries, "uid");
			}
		}
		List<N33_TkLogDTO> tkLogDTOs = new ArrayList<N33_TkLogDTO>();
		int count = tkLogDao.getTkLogCountByGradeId(new ObjectId(termId), schoolId, new ObjectId(gradeId), uids);
		List<N33_TkLogEntry> tkLogEntries = tkLogDao.getTkLogByGradeId(new ObjectId(termId), schoolId, new ObjectId(gradeId), page, pageSize, uids);
		List<ObjectId> subjectIds = new ArrayList<ObjectId>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
		jxbIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "jxbId"));
		jxbIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "oJxbId"));
		subjectIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "osid"));
		subjectIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "nsid"));
		teacherIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "uid"));
		teacherIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "otid"));
		teacherIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "ntid"));
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, new ObjectId(ci));
		Map<ObjectId, N33_JXBEntry> jxbMaps = jxbDao.getJXBMapsByIds(jxbIds);
		List<ObjectId> ykbIds = new ArrayList<ObjectId>();
		ykbIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "oYkbId"));
		ykbIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "nYkbId"));
		Map<ObjectId, N33_ZKBEntry> ykbEntryMap = zkbDao.getZKBbyIds(ykbIds, schoolId);
//		Map<ObjectId, N33_KSEntry> subjectEntryMap = subjectDao.getIsolateSubjectEntryBySubs(subjectIds);
		if (tkLogEntries != null && tkLogEntries.size() != 0) {
			for (N33_TkLogEntry entry : tkLogEntries) {
				//调课的原来的位置
				String[] oXY = entry.getOlocal().split(",");
				//调课的现在的位置
				String[] nXY = entry.getNlocal().split(",");
				N33_TkLogDTO dto = new N33_TkLogDTO(entry);
				dto.setTeacherName(teaEntryMap.get(entry.getOTeacherId()) != null ? teaEntryMap.get(entry.getOTeacherId()).getUserName() : "");
				String[] weekArg = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
				if (ykbEntryMap.get(entry.getOYkbId()) != null) {
					String jxbName = "";
					if (entry.getoJxbId() != null && jxbMaps.get(entry.getoJxbId()) != null) {
						jxbName = jxbMaps.get(entry.getoJxbId()).getName();
						dto.setJxbName(jxbName);
						dto.setKeJie(weekArg[Integer.parseInt(oXY[0])] + "第" + (Integer.parseInt(oXY[1]) + 1) + "节课");
					} else {
						dto.setJxbName(jxbName);
						dto.setKeJie(weekArg[Integer.parseInt(oXY[0])] + "第" + (Integer.parseInt(oXY[1]) + 1) + "节课");
					}
				}
				dto.setNewTeacherName(teaEntryMap.get(entry.getNTeacherId()) != null ? teaEntryMap.get(entry.getNTeacherId()).getUserName() : "");
				if (entry.getType() == 0) {
					if (ykbEntryMap.get(entry.getNYkbId()) != null) {
						String jxbName = "";
						if (entry.getJxbId() != null && jxbMaps.get(entry.getJxbId()) != null) {
							jxbName = jxbMaps.get(entry.getJxbId()).getName();
							dto.setNewJxbName(jxbName);
							dto.setNewKeJie(weekArg[Integer.parseInt(nXY[0])] + "第" + (Integer.parseInt(nXY[1]) + 1) + "节课");
						} else {
							dto.setNewJxbName(jxbName);
							dto.setNewKeJie(weekArg[Integer.parseInt(nXY[0])] + "第" + (Integer.parseInt(nXY[1]) + 1) + "节课");
						}
					}
				}
				tkLogDTOs.add(dto);
			}
		}

		map.put("count", count);
		map.put("logs", tkLogDTOs);
		map.put("page", page);
		map.put("pageSize", pageSize);
		return map;
	}


	public List<Map<String, Object>> getZkbByJxbId(ObjectId jxbId,int x,int y, Integer week) {
		List<Map<String, Object>> reslist = new ArrayList<Map<String, Object>>();
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeekList(jxbEntry.getClassroomId(), week, jxbEntry.getGradeId(), getDefaultPaiKeTerm(jxbEntry.getSchoolId()).getPaikeXqid());
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_ZKBEntry zkbEntry : zkbEntryList) {
			if (zkbEntry.getJxbId() != null) {
				jxbIds.add(zkbEntry.getJxbId());
			}
			if (zkbEntry.getNJxbId() != null) {
				jxbIds.add(zkbEntry.getNJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntryMap = zixikeDao.getJXBMapsByIds(jxbIds);
		Map<ObjectId, N33_JXBEntry> n33_jxbEntryMap = jxbDao.getJXBMapsByIds(jxbIds);
		Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(zkbEntryList.get(0).getCId(), zkbEntryList.get(0).getSchoolId());
		Map<ObjectId, N33_ClassroomEntry> classroomEntryMap = classroomService.getRoomListMapBySchoolId(zkbEntryList.get(0).getSchoolId(), zkbEntryList.get(0).getCId());

		//jxb add
		int acClassType = turnOffService.getAcClassType(jxbEntry.getSchoolId(), jxbEntry.getGradeId(), zkbEntryList.get(0).getCId(),1);
		//jxb add
		List<N33_JXBEntry> ShengJxbList = jxbDao.getJXBListV1(jxbEntry.getSchoolId(), jxbEntry.getGradeId(), jxbEntry.getClassroomId(), zkbEntryList.get(0).getCId(),acClassType);
		List<ObjectId> sjxbIds = MongoUtils.getFieldObjectIDs(ShengJxbList, "_id");
		List<N33_TiaoKeShengQingEntry> tiaoKeShengQingEntries = tiaoKeShengQingDao.getEntryList(zkbEntryList.get(0).getTermId(), sjxbIds, week);
		for (N33_ZKBEntry zkbEntry : zkbEntryList) {
			Boolean isTrue = true;
			for (N33_TiaoKeShengQingEntry entry : tiaoKeShengQingEntries) {
				if (entry.getNY() == zkbEntry.getY() && entry.getNX() == zkbEntry.getX()) {
					isTrue = false;
					break;
				}
			}
			if (isTrue) {
				if (zkbEntry.getJxbId() != null && zkbEntry.getType() != 5) {
					Integer count = week % 2;
					Map<String, Object> map = new HashMap<String, Object>();
					Grade grade = gradeMap.get(zkbEntry.getGradeId());
					map.put("gradeName", grade.getName());
					N33_ClassroomEntry classroomEntry = classroomEntryMap.get(zkbEntry.getClassroomId());
					map.put("roomName", classroomEntry.getRoomName());
					map.put("x", zkbEntry.getX() + 1);
					map.put("y", zkbEntry.getY() + 1);
					map.put("sta", 0);
					if (count == 0 && zkbEntry.getNJxbId() != null) {
						map.put("jxbId", zkbEntry.getNJxbId().toString());
						N33_JXBEntry jxbEntry1 = n33_jxbEntryMap.get(zkbEntry.getNJxbId());
						map.put("jxbName", jxbEntry1.getName());
					} else {
						map.put("jxbId", zkbEntry.getJxbId().toString());
						N33_JXBEntry jxbEntry1 = n33_jxbEntryMap.get(zkbEntry.getJxbId());
						map.put("jxbName", jxbEntry1.getName());
					}
					boolean flag = getShiBuShiKeTiaoKe(jxbId,new ObjectId(map.get("jxbId").toString()),x,y,Integer.valueOf(map.get("x").toString()),Integer.valueOf(map.get("y").toString()),zkbEntryList.get(0).getTermId(),week);
					map.put("flag",flag);
					reslist.add(map);
				} else if (zkbEntry.getJxbId() != null && zkbEntry.getType() == 5) {
					Map<String, Object> map = new HashMap<String, Object>();
					Grade grade = gradeMap.get(zkbEntry.getGradeId());
					map.put("gradeName", grade.getName());
					N33_ClassroomEntry classroomEntry = classroomEntryMap.get(zkbEntry.getClassroomId());
					map.put("roomName", classroomEntry.getRoomName());
					map.put("jxbId", zkbEntry.getJxbId().toString());
					N33_ZIXIKEEntry zixikeEntry = zixikeEntryMap.get(zkbEntry.getJxbId());
					map.put("jxbName", zixikeEntry.getName());
					map.put("x", zkbEntry.getX() + 1);
					map.put("y", zkbEntry.getY() + 1);
					boolean flag = getShiBuShiKeTiaoKe(jxbId,zkbEntry.getJxbId(),x,y,zkbEntry.getX() + 1,zkbEntry.getY() + 1,zkbEntryList.get(0).getTermId(),week);
					map.put("flag",flag);
					reslist.add(map);
				}
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", zkbEntry.getX() + 1);
				map.put("y", zkbEntry.getY() + 1);
				map.put("sta", 1);
				reslist.add(map);
			}
		}
		return reslist;
	}

	/**
	 * 根据教学班查找和哪些格子有冲突不能调课
	 * @param jxbId
	 * @return
	 */
	public List<Map<String, String>> getAllCTXY(ObjectId jxbId,ObjectId classRoomId,Integer type,Integer x,Integer y,ObjectId tid){
		List<Map<String,String>> retList = new ArrayList<Map<String, String>>();
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		Map ykbMap = paiKeService.getKeBiaoList(jxbEntry.getTermId(),jxbEntry.getGradeId().toString(),classRoomId.toString(),jxbEntry.getSchoolId());
		Map<String,List<N33_YKBDTO>> ykbdtosMap = (Map<String, List<N33_YKBDTO>>) ykbMap.get("ykbdto");
		List<N33_YKBDTO> ykbdtos = ykbdtosMap.get(classRoomId.toString());

		List<ObjectId> teaIds = new ArrayList<ObjectId>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
//		N33_YKBEntry ykbEntry2 = ykbDao.getN33_YKBByClassRoom(x,y,classRoomId,jxbEntry.getTermId());
//		List<N33_YKBEntry> entries = ykbDao.getJXB(jxbEntry.getTermId(), tid);
//		entries.addAll(ykbDao.getJXBByTid(jxbEntry.getTermId(), tid));
//		entries.addAll(ykbDao.getYKBbyType(jxbEntry.getTermId(),jxbEntry.getSchoolId(),4)) ;
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(jxbEntry.getSchoolId(), jxbEntry.getTermId());
//		if(type == 0){
//			for (N33_YKBEntry ykbEntry : entries) {
//				boolean flag = false;
//				if(ykbEntry.getType() == 4){
//					List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(ykbEntry.getJxbId(),jxbEntry.getTermId());
//					for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
//						if(zhuanXiangEntry.getTeaId() != null && tid.toString().equals(zhuanXiangEntry.getTeaId().toString())){
//							flag = true;
//							break;
//						}
//					}
//				}else{
//					flag = true;
//				}
//				if(flag){
//					List<ObjectId> roomIds = new ArrayList<ObjectId>();
//					roomIds.add(ykbEntry.getClassroomId());
//					List<N33_YKBEntry> roomYkbEntryList = ykbDao.getYKBsByclassRoomIds(jxbEntry.getTermId(), jxbEntry.getSchoolId(),roomIds);
//					for (N33_YKBEntry ykbEntry3 : roomYkbEntryList) {
//						if(ykbEntry3.getIsUse() != 0 && ykbEntry3.getX() == x && ykbEntry3.getY() == y){
//							boolean flag1 = true;
//							for (Map<String,String> map : retList) {
//								if(Integer.valueOf(map.get("x")).intValue() == ykbEntry.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry.getY()){
//									flag1 = false;
//									break;
//								}
//							}
//							if(flag1){
//								Map<String,String> map = new HashMap<String, String>();
//								map.put("x",String.valueOf(ykbEntry.getX()));
//								map.put("y",String.valueOf(ykbEntry.getY()));
//								map.put("msg","教室已使用");
//								retList.add(map);
//							}
//						}
//					}
//				}
//			}
//		}
		if(jxbEntry.getType() == 6){
			jxbIds.add(jxbId);
			jxbIds.add(jxbEntry.getRelativeId());
//			N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(jxbEntry.getRelativeId());
//			teaIds.add(jxbEntry.getTercherId());
//			teaIds.add(jxbEntry1.getTercherId());
		}else{
//			teaIds.add(jxbEntry.getTercherId());
			jxbIds.add(jxbId);
		}
		List<N33_JXBCTEntry> jxbctEntryList = jxbctDao.getJxbCTByjxbIds(jxbIds);
		Map<String,N33_JXBCTEntry> jxbctEntryMap = new HashMap<String, N33_JXBCTEntry>();
		List<ObjectId> ctJxbIds = new ArrayList<ObjectId>();
		for (N33_JXBCTEntry jxbctEntry : jxbctEntryList) {
			jxbctEntryMap.put(jxbctEntry.getJxbId().toString() + "" + jxbctEntry.getOjxbId().toString(),jxbctEntry);
			jxbctEntryMap.put(jxbctEntry.getOjxbId().toString() + "" + jxbctEntry.getJxbId().toString(),jxbctEntry);
			if(!ctJxbIds.contains(jxbctEntry.getOjxbId())){
				ctJxbIds.add(jxbctEntry.getOjxbId());
			}
		}

//		List<N33_YKBEntry> teaYkbEntryList = null;
		List<ObjectId> roomIds = new ArrayList<ObjectId>();
		if(jxbEntry.getType() != 4 && jxbEntry.getClassroomId() != null){
			roomIds.add(jxbEntry.getClassroomId());
		}else{
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbId,jxbEntry.getTermId());
			for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
				if(zhuanXiangEntry.getRoomId() != null){
					roomIds.add(zhuanXiangEntry.getRoomId());
				}
			}
		}

		List<N33_YKBEntry> roomYkbEntryList = ykbDao.getYKBsByclassRoomIds(jxbEntry.getTermId(), jxbEntry.getSchoolId(),roomIds);
//		if(type == 0){
//			teaYkbEntryList = ykbDao.getJXBByTidsOrNtids(jxbEntry.getTermId(),teaIds);
//		}

		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysByJXBIdsOrNJxbIds(jxbEntry.getTermId(),ctJxbIds,jxbEntry.getSchoolId());

		if(type != 0){
			for (N33_YKBEntry ykbEntry : ykbEntryList) {
				if(!ykbEntry.getClassroomId().toString().equals(classRoomId.toString())){
					boolean flag = true;
					if(jxbEntry.getType() == 6){
						N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(jxbEntry.getRelativeId());
						if((ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry.getTercherId()) && jxbEntry.getDanOrShuang() == 1)
								|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry.getTercherId()) && jxbEntry.getDanOrShuang() == 2)
								|| (ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 1)
								|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 2)
								|| (jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1)
								|| (jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3)
								|| (jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1)
								|| jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3
								|| (ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1)
								|| (ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()) != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3)
								|| (ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId())!= null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId()).getCtType() == 1)
								|| (ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId()) != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId()).getCtType() == 3)){
							for (Map<String,String> map : retList) {
								if(Integer.valueOf(map.get("x")).intValue() == ykbEntry.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry.getY()){
									flag = false;
									break;
								}
							}
						}else{
							flag = false;
						}
					}else{
						for (Map<String,String> map : retList) {
							if(Integer.valueOf(map.get("x")).intValue() == ykbEntry.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry.getY()){
								flag = false;
								break;
							}
						}
					}
					if(flag){
						Map<String,String> map = getCTDESCMap(jxbctEntryMap,jxbEntry,ykbEntry,jxbEntryMap);
						retList.add(map);
					}
				}
			}
		}
//		else{
//			for (N33_YKBEntry ykbEntry : ykbEntryList) {
//				if(ykbEntry.getType() == 4 || jxbEntry.getType() == 4 || (ykbEntry.getTeacherId() != null && jxbEntry.getTercherId() != null && !ykbEntry.getTeacherId().toString().equals(jxbEntry.getTercherId().toString()))){
//					boolean flag = true;
//					for (Map<String,String> map : retList) {
//						if(Integer.valueOf(map.get("x")).intValue() == ykbEntry.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry.getY()){
//							flag = false;
//							break;
//						}
//					}
//					if(flag){
//						Map<String,String> map = getCTDESCMap(jxbctEntryMap,jxbEntry,ykbEntry,jxbEntryMap);
//						retList.add(map);
//					}
//				}
//			}
//
//			for (N33_YKBEntry ykbEntry : roomYkbEntryList) {
//				if(ykbEntry.getIsUse() != 0){
//					boolean flag = true;
//					for (Map<String,String> map : retList) {
//						if(Integer.valueOf(map.get("x")).intValue() == ykbEntry.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry.getY()){
//							flag = false;
//							break;
//						}
//					}
//					if(flag){
//						Map<String,String> map = new HashMap<String, String>();
//						map.put("x",String.valueOf(ykbEntry.getX()));
//						map.put("y",String.valueOf(ykbEntry.getY()));
//						map.put("msg","教室已使用");
//						retList.add(map);
//					}
//				}
//			}
//		}

		if(type != 0){
			for (N33_YKBEntry ykbEntry1 : roomYkbEntryList) {
				if(ykbEntry1.getJxbId() != null && ykbEntry1.getJxbId().toString().equals(jxbId.toString())){
					boolean flag = true;
					for (Map<String,String> map : retList) {
						if(Integer.valueOf(map.get("x")).intValue() == ykbEntry1.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry1.getY()){
							flag = false;
							break;
						}
					}
					if(flag){
						Map<String,String> map = new HashMap<String, String>();
						map.put("x",String.valueOf(ykbEntry1.getX()));
						map.put("y",String.valueOf(ykbEntry1.getY()));
						map.put("msg","同一个教学班");
						retList.add(map);
					}
				}
			}
		}

		List<N33_YKBEntry> ykbEntrysList = ykbDao.getYKBbyXY(jxbEntry.getTermId(),null,x,y,jxbEntry.getSchoolId(),1);

		for (N33_YKBDTO ykbDto : ykbdtos) {
			if(ykbDto.getIsUse() != 1){
				continue;
			}
			if(ykbDto.getJxbId() == null || "".equals(ykbDto.getJxbId())){
				continue;
			}
			N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(new ObjectId(ykbDto.getJxbId()));
			List<ObjectId> jxbIds1 = new ArrayList<ObjectId>();
			if(ykbDto.getKbType() == 6){
				jxbIds1.add(new ObjectId(ykbDto.getJxbId()));
				jxbIds1.add(new ObjectId(ykbDto.getNjxbId()));
			}else{
				jxbIds1.add(new ObjectId(ykbDto.getJxbId()));
			}

			List<N33_JXBCTEntry> jxbctEntryList1 = jxbctDao.getJxbCTByjxbIds(jxbIds1);
			Map<String,N33_JXBCTEntry> jxbctEntryMap2 = new HashMap<String, N33_JXBCTEntry>();
			List<ObjectId> jxbctids = new ArrayList<ObjectId>();
			for (N33_JXBCTEntry jxbctEntry : jxbctEntryList1) {
				jxbctEntryMap2.put(jxbctEntry.getJxbId().toString() + "" + jxbctEntry.getOjxbId().toString(),jxbctEntry);
				jxbctEntryMap2.put(jxbctEntry.getOjxbId().toString() + "" + jxbctEntry.getJxbId().toString(),jxbctEntry);
				jxbctids.add(jxbctEntry.getOjxbId());
			}
			for(N33_YKBEntry ykbEntry : ykbEntrysList){
				boolean flag = false;
				if(ykbEntry.getType() == 6 && ykbDto.getKbType() == 6 && (jxbctids.contains(ykbEntry.getJxbId()) || jxbctids.contains(ykbEntry.getNJxbId()))){
					flag = true;
					N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(new ObjectId(ykbDto.getNjxbId()));
					if((ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 1)
							|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry1.getTercherId()) && jxbEntry1.getDanOrShuang() == 2)
							|| (ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry2.getTercherId()) && jxbEntry2.getDanOrShuang() == 1)
							|| (ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().equals(jxbEntry2.getTercherId()) && jxbEntry2.getDanOrShuang() == 2)){
						for (Map<String,String> map : retList) {
							if(Integer.valueOf(map.get("x")).intValue() == ykbDto.getX() && Integer.valueOf(map.get("y")).intValue() == ykbDto.getY()){
								flag = false;
								break;
							}
						}
					}else{
						flag = false;
					}
				}else if(jxbctids.contains(ykbEntry.getJxbId()) || (ykbEntry.getNJxbId() != null && jxbctids.contains(ykbEntry.getNJxbId()))){
					flag = true;
					for (Map<String,String> map : retList) {
						if(Integer.valueOf(map.get("x")).intValue() == ykbDto.getX() && Integer.valueOf(map.get("y")).intValue() == ykbDto.getY()){
							flag = false;
							break;
						}
					}
				}
				if(classRoomId.toString().equals(ykbEntry.getClassroomId().toString())){
					flag = false;
				}
				if(flag){
					N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(ykbEntry.getJxbId());
					Map<String,String> map = getCTDESCMap1(jxbctEntryMap2,jxbEntry2,ykbDto.buildEntry(),jxbEntryMap,x,y);
					retList.add(map);
				}
			}
		}
// else{
//			for (N33_YKBEntry ykbEntry1 : teaYkbEntryList) {
//				if(ykbEntry1.getJxbId() != null && ykbEntry1.getJxbId().toString().equals(jxbId.toString())){
//					boolean flag = true;
//					for (Map<String,String> map : retList) {
//						if(Integer.valueOf(map.get("x")).intValue() == ykbEntry1.getX() && Integer.valueOf(map.get("y")).intValue() == ykbEntry1.getY()){
//							flag = false;
//							break;
//						}
//					}
//					if(flag){
//						Map<String,String> map = new HashMap<String, String>();
//						map.put("x",String.valueOf(ykbEntry1.getX()));
//						map.put("y",String.valueOf(ykbEntry1.getY()));
//						map.put("msg","同一个教学班");
//						retList.add(map);
//					}
//				}
//			}
//		}
		return retList;
	}

	private Map<String,String> getCTDESCMap1(Map<String,N33_JXBCTEntry> jxbctEntryMap,N33_JXBEntry jxbEntry,N33_YKBEntry ykbEntry,Map<ObjectId,N33_JXBEntry> jxbEntryMap,Integer x,Integer y){
		Map<String,String> map = new HashMap<String, String>();
		if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + "学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":场地冲突");
			}
		}else if(jxbEntry.getType() == 6 && (jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()) != null)){
			if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":场地冲突");
			}
		}else if(ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":场地冲突");
			}
		}else if(jxbEntry.getType() == 6 && ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":场地冲突");
			}
		}
		map.put("x",String.valueOf(ykbEntry.getX()));
		map.put("y",String.valueOf(ykbEntry.getY()));
		return map;
	}

	private Map<String,String> getCTDESCMap2(Map<String,N33_JXBCTEntry> jxbctEntryMap,N33_JXBEntry jxbEntry,N33_ZKBEntry ykbEntry,Map<ObjectId,N33_JXBEntry> jxbEntryMap,Integer x,Integer y){
		Map<String,String> map = new HashMap<String, String>();
		if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + "学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":场地冲突");
			}
		}else if(jxbEntry.getType() == 6 && (jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()) != null)){
			if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":场地冲突");
			}
		}else if(ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":场地冲突");
			}
		}else if(jxbEntry.getType() == 6 && ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "在周" + (x + 1) + "第" + (y + 1) + "节:" + jxbEntry.getName() + ":场地冲突");
			}
		}
		map.put("x",String.valueOf(ykbEntry.getX()));
		map.put("y",String.valueOf(ykbEntry.getY()));
		return map;
	}

	private Map<String,String> getCTDESCMap3(Map<String,N33_JXBCTEntry> jxbctEntryMap,N33_JXBEntry jxbEntry,N33_ZKBEntry ykbEntry,Map<ObjectId,N33_JXBEntry> jxbEntryMap){
		Map<String,String> map = new HashMap<String, String>();
		if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":场地冲突");
			}
		}else if(jxbEntry.getType() == 6 && (jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()) != null)){
			if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":场地冲突");
			}
		}else if(ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":场地冲突");
			}
		}else if(jxbEntry.getType() == 6 && ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":场地冲突");
			}
		}
		map.put("x",String.valueOf(ykbEntry.getX()));
		map.put("y",String.valueOf(ykbEntry.getY()));
		return map;
	}

	private Map<String,String> getCTDESCMap(Map<String,N33_JXBCTEntry> jxbctEntryMap,N33_JXBEntry jxbEntry,N33_YKBEntry ykbEntry,Map<ObjectId,N33_JXBEntry> jxbEntryMap){
		Map<String,String> map = new HashMap<String, String>();
		if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":场地冲突");
			}
		}else if(jxbEntry.getType() == 6 && (jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()) != null)){
			if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + ":场地冲突");
			}
		}else if(ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getID().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":场地冲突");
			}
		}else if(jxbEntry.getType() == 6 && ykbEntry.getNJxbId() != null && jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()) != null){
			if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 1){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":学生冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 2){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":教师冲突");
			}else if(jxbctEntryMap.get(jxbEntry.getRelativeId().toString() + "" + ykbEntry.getNJxbId().toString()).getCtType() == 3){
				map.put("msg",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + ":场地冲突");
			}
		}
		map.put("x",String.valueOf(ykbEntry.getX()));
		map.put("y",String.valueOf(ykbEntry.getY()));
		return map;
	}


	public List<Map<String, Object>> getZkbByJxbIdYKB(ObjectId jxbId) {
		List<Map<String, Object>> reslist = new ArrayList<Map<String, Object>>();
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		List<ZouBanTimeDTO> zouBanTimeDTOs = courseRangeService.getZouBanTime(jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getGradeId());
		List<N33_YKBEntry> zkbEntryList = ykbDao.getYKBEntrysListByClassRoomId(jxbEntry.getTermId(), jxbEntry.getSchoolId(), jxbEntry.getClassroomId());
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_YKBEntry zkbEntry : zkbEntryList) {
			if (zkbEntry.getJxbId() != null) {
				jxbIds.add(zkbEntry.getJxbId());
			}
			if (zkbEntry.getNJxbId() != null) {
				jxbIds.add(zkbEntry.getNJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntryMap = zixikeDao.getJXBMapsByIds(jxbIds);
		Map<ObjectId, N33_JXBEntry> n33_jxbEntryMap = jxbDao.getJXBMapsByIds(jxbIds);
		Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(jxbEntry.getTermId(), jxbEntry.getSchoolId());
		Map<ObjectId, N33_ClassroomEntry> classroomEntryMap = classroomService.getRoomListMapBySchoolId(jxbEntry.getSchoolId(), jxbEntry.getTermId());
		for (N33_YKBEntry zkbEntry : zkbEntryList) {
			Boolean isTrue = true;
			for (ZouBanTimeDTO dto : zouBanTimeDTOs) {
				if (dto.getX() - 1 == zkbEntry.getX() && dto.getY() - 1 == zkbEntry.getY()) {
					isTrue = false;
					break;
				}
			}
			if (isTrue) {
				if (zkbEntry.getJxbId() != null && zkbEntry.getType() != 5 && zkbEntry.getGradeId().toString().equals(jxbEntry.getGradeId().toString())) {
					Map<String, Object> map = new HashMap<String, Object>();
					Grade grade = gradeMap.get(zkbEntry.getGradeId());
					map.put("gradeName", grade.getName());
					N33_ClassroomEntry classroomEntry = classroomEntryMap.get(zkbEntry.getClassroomId());
					map.put("roomName", classroomEntry.getRoomName());
					map.put("x", zkbEntry.getX() + 1);
					map.put("y", zkbEntry.getY() + 1);
					map.put("sta", 0);
					map.put("jxbId", zkbEntry.getJxbId().toString());
					map.put("ykbId", zkbEntry.getID().toString());
					N33_JXBEntry jxbEntry1 = n33_jxbEntryMap.get(zkbEntry.getJxbId());
					map.put("jxbName", jxbEntry1.getName());
					reslist.add(map);
				} else if (zkbEntry.getJxbId() != null && zkbEntry.getType() == 5 && zkbEntry.getGradeId().toString().equals(jxbEntry.getGradeId().toString())) {
					Map<String, Object> map = new HashMap<String, Object>();
					Grade grade = gradeMap.get(zkbEntry.getGradeId());
					map.put("gradeName", grade.getName());
					map.put("ykbId", zkbEntry.getID().toString());
					N33_ClassroomEntry classroomEntry = classroomEntryMap.get(zkbEntry.getClassroomId());
					map.put("roomName", classroomEntry.getRoomName());
					map.put("jxbId", zkbEntry.getJxbId().toString());
					N33_ZIXIKEEntry zixikeEntry = zixikeEntryMap.get(zkbEntry.getJxbId());
					map.put("jxbName", zixikeEntry.getName());
					map.put("x", zkbEntry.getX() + 1);
					map.put("y", zkbEntry.getY() + 1);
					reslist.add(map);
				} else {
					if (zkbEntry.getGradeId() == null) {
						Map<String, Object> map = new HashMap<String, Object>();
						N33_ClassroomEntry classroomEntry = classroomEntryMap.get(zkbEntry.getClassroomId());
						map.put("roomName", classroomEntry.getRoomName());
						map.put("x", zkbEntry.getX() + 1);
						map.put("y", zkbEntry.getY() + 1);
						map.put("sta", 0);
						reslist.add(map);
					}
				}
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", zkbEntry.getX() + 1);
				map.put("y", zkbEntry.getY() + 1);
				map.put("sta", 1);
				reslist.add(map);
			}
		}
		return reslist;
	}

	public void tiaokeLuo(ObjectId ykbId, ObjectId ybkId2) {
		N33_YKBEntry ykbEntry = ykbDao.getN33_YKBById(ykbId);
		N33_YKBEntry ykbEntry1 = ykbDao.getN33_YKBById(ybkId2);
		N33_YKBEntry ykbEntry2 = ykbDao.getN33_YKBById(ybkId2);

		ykbEntry1.setTeacherId(ykbEntry.getTeacherId());
		ykbEntry1.setJxbId(ykbEntry.getJxbId());
		ykbEntry1.setSubjectId(ykbEntry.getSubjectId());
		ykbEntry1.setType(ykbEntry.getType());
		ykbEntry1.setIsUse(ykbEntry.getIsUse());
		ykbEntry1.setNTeacherId(ykbEntry.getNTeacherId());
		ykbEntry1.setNJxbId(ykbEntry.getNJxbId());
		ykbEntry1.setGradeId(ykbEntry.getGradeId());
		ykbEntry1.setNSubjectId(ykbEntry.getNSubjectId());
		ykbEntry.setTeacherId(ykbEntry2.getTeacherId());
		ykbEntry.setJxbId(ykbEntry2.getJxbId());
		ykbEntry.setSubjectId(ykbEntry2.getSubjectId());
		ykbEntry.setType(ykbEntry2.getType());
		ykbEntry.setIsUse(ykbEntry2.getIsUse());
		ykbEntry.setNTeacherId(ykbEntry2.getNTeacherId());
		ykbEntry.setNJxbId(ykbEntry2.getNJxbId());
		ykbEntry.setNSubjectId(ykbEntry2.getNSubjectId());
		ykbEntry.setGradeId(ykbEntry2.getGradeId());
		ykbDao.updateN33_YKB(ykbEntry);
		ykbDao.updateN33_YKB(ykbEntry1);
	}

	public Boolean getShiBuShiKeTiaoKeYKB(ObjectId ykbId, ObjectId ybkId2) {
		N33_YKBEntry ykbEntry = ykbDao.getN33_YKBById(ykbId);
		N33_YKBEntry ykbEntry1 = ykbDao.getN33_YKBById(ybkId2);
		if (ykbEntry.getType() == 0 || ykbEntry1.getType() == 0) {
			if (ykbEntry.getType() != 6 && ykbEntry.getType() != 4 && ykbEntry1.getType() != 6 && ykbEntry1.getType() != 4) {
				ObjectId jxbId = null;
				ObjectId teaId = null;
				ObjectId gradeId = null;
				Integer x = 0;
				Integer y = 0;
				if (ykbEntry.getType() != 0) {
					teaId = ykbEntry.getTeacherId();
					gradeId = ykbEntry.getGradeId();
					jxbId = ykbEntry.getJxbId();
					x = ykbEntry1.getX();
					y = ykbEntry1.getY();
				} else {
					teaId = ykbEntry1.getTeacherId();
					gradeId = ykbEntry1.getGradeId();
					jxbId = ykbEntry1.getJxbId();
					x = ykbEntry.getX();
					y = ykbEntry.getY();
				}
				//老师是不是上课
				List<N33_YKBEntry> pks1 = ykbDao.getN33_ZKBByWeek(x, y, ykbEntry.getTermId(), teaId);
				if (pks1.size() > 0) {
					return false;
				}
				List<N33_YKBEntry> jxb1 = ykbDao.getN33_ZKBByWeekG(x, y, ykbEntry.getTermId(), jxbId, gradeId);
				List<ObjectId> jxbIds1 = new ArrayList<ObjectId>();
				for (N33_YKBEntry zkbEntry : jxb1) {
					if (!zkbEntry.getJxbId().toString().equals(jxbId.toString())) {
						jxbIds1.add(zkbEntry.getJxbId());
					}
				}
				Integer size1 = jxbctDao.getJxbCTByjxbIds(jxbId, jxbIds1);
				if (size1 != 0) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			if (ykbEntry.getType() != 6 && ykbEntry.getType() != 4 && ykbEntry1.getType() != 6 && ykbEntry1.getType() != 4) {
				List<N33_YKBEntry> pks1 = ykbDao.getN33_ZKBByWeek(ykbEntry.getX(), ykbEntry.getY(), ykbEntry.getTermId(), ykbEntry1.getTeacherId());
				List<N33_YKBEntry> pksa = ykbDao.getN33_ZKBByWeek(ykbEntry1.getX(), ykbEntry1.getY(), ykbEntry.getTermId(), ykbEntry.getTeacherId());
				if (pks1.size() != 0 || pksa.size() != 0) {
					return false;
				}
				List<N33_YKBEntry> jxb1 = ykbDao.getN33_ZKBByWeekG(ykbEntry.getX(), ykbEntry.getY(), ykbEntry.getTermId(), ykbEntry1.getJxbId(), ykbEntry.getGradeId());
				List<N33_YKBEntry> jxb2 = ykbDao.getN33_ZKBByWeekG(ykbEntry1.getX(), ykbEntry1.getY(), ykbEntry.getTermId(), ykbEntry.getJxbId(), ykbEntry.getGradeId());
				List<ObjectId> jxbIds1 = new ArrayList<ObjectId>();
				for (N33_YKBEntry zkbEntry : jxb1) {
					if (!zkbEntry.getJxbId().toString().equals(ykbEntry1.toString())) {
						jxbIds1.add(zkbEntry.getJxbId());
					}
				}
				List<ObjectId> jxbIds2 = new ArrayList<ObjectId>();
				for (N33_YKBEntry zkbEntry : jxb2) {
					if (!zkbEntry.getJxbId().toString().equals(ykbEntry.toString())) {
						jxbIds2.add(zkbEntry.getJxbId());
					}
				}
				Integer size1 = jxbctDao.getJxbCTByjxbIds(ykbEntry.getJxbId(), jxbIds1, 1);
				Integer size2 = jxbctDao.getJxbCTByjxbIds(ykbEntry1.getJxbId(), jxbIds2, 1);
//                5b45a7a58fb25a6d70d344a6
				if (size1 != 0 || size2 != 0) {
					return false;
				}
			} else {
				return false;
			}
		}
		//事物
		return true;
	}

	public Boolean getShiBuShiKeTiaoKe(ObjectId jxbId1, ObjectId jxbId2, Integer X1, Integer X2, Integer Y1, Integer Y2, ObjectId xqid, Integer week) {
		if (jxbId1 != null && jxbId2 != null) {
			N33_JXBEntry entry = jxbDao.getJXBById(jxbId1);
			N33_JXBEntry entry1 = jxbDao.getJXBById(jxbId2);
			if (entry == null || entry1 == null) {
				return false;
			}
			if (entry.getType() == 1 || entry.getType() == 2) {
				if (entry1.getType() != 1 && entry1.getType() != 2) {
					return false;
				}
			} else if (entry1.getType() == 1 || entry1.getType() == 2) {
				if (entry.getType() != 1 && entry.getType() != 2) {
					return false;
				}
			} else if (entry.getType() != entry1.getType()) {
				return false;
			}
			List<N33_SWEntry> swEntries = swDao.getSwByXqid(xqid);
			Boolean b = true;
			for (N33_SWEntry swEntry : swEntries) {
				if (swEntry.getTeacherIds().contains(entry.getTercherId()) && swEntry.getX() == X1 && swEntry.getY() == Y1) {
					b = false;
					break;
				}
				if (swEntry.getTeacherIds().contains(entry.getTercherId()) && swEntry.getX() == X2 && swEntry.getY() == Y2) {
					b = false;
					break;
				}
				if (swEntry.getTeacherIds().contains(entry1.getTercherId()) && swEntry.getX() == X1 && swEntry.getY() == Y1) {
					b = false;
					break;
				}
				if (swEntry.getTeacherIds().contains(entry1.getTercherId()) && swEntry.getX() == X2 && swEntry.getY() == Y2) {
					b = false;
					break;
				}
				if (entry.getType() == 6) {
					N33_JXBEntry entry3 = jxbDao.getJXBById(entry.getRelativeId());
					if (swEntry.getTeacherIds().contains(entry3.getTercherId()) && swEntry.getX() == X1 && swEntry.getY() == Y1) {
						b = false;
						break;
					}
					if (swEntry.getTeacherIds().contains(entry3.getTercherId()) && swEntry.getX() == X2 && swEntry.getY() == Y2) {
						b = false;
						break;
					}
				}
				if (entry1.getType() == 6) {
					N33_JXBEntry entry3 = jxbDao.getJXBById(entry.getRelativeId());
					if (swEntry.getTeacherIds().contains(entry3.getTercherId()) && swEntry.getX() == X1 && swEntry.getY() == Y1) {
						b = false;
						break;
					}
					if (swEntry.getTeacherIds().contains(entry3.getTercherId()) && swEntry.getX() == X2 && swEntry.getY() == Y2) {
						b = false;
						break;
					}
				}
			}
			//老师是否上其他课程
			Integer count = week % 2;
			ObjectId tea1Id = entry.getTercherId();
			ObjectId jxb1Id = entry.getID();
			ObjectId tea2Id = entry1.getTercherId();
			ObjectId jxb2Id = entry1.getID();
			if (entry.getType() == 6) {
				if (count == 0) {
					N33_JXBEntry entry3 = jxbDao.getJXBById(entry.getRelativeId());
					if (entry3.getDanOrShuang() == 1) {
						tea1Id = entry3.getTercherId();
						jxb1Id = entry3.getID();
					}
				}
			}
			if (entry1.getType() == 6) {
				if (count == 0) {
					N33_JXBEntry entry3 = jxbDao.getJXBById(entry.getRelativeId());
					if (entry3.getDanOrShuang() == 1) {
						tea2Id = entry3.getTercherId();
						jxb2Id = entry3.getID();
					}
				}
			}
			List<N33_ZKBEntry> pks1 = zkbDao.getN33_ZKBByWeek(week, Y2 - 1, X2 - 1, xqid, tea1Id);
			List<N33_ZKBEntry> pksa = zkbDao.getN33_ZKBByWeek(week, Y1 - 1, X1 - 1, xqid, tea2Id);
			if (pks1.size() != 0 || pksa.size() != 0) {
				b = false;
			}
			//学生冲突
			if (entry.getType() != 3 && entry.getType() != 6) {
				List<N33_ZKBEntry> jxb1 = zkbDao.getN33_ZKBByWeekG(week, Y2 - 1, X2 - 1, xqid, jxb2Id, entry.getGradeId());
				List<N33_ZKBEntry> jxb2 = zkbDao.getN33_ZKBByWeekG(week, Y1 - 1, X1 - 1, xqid, jxb1Id, entry.getGradeId());
				List<ObjectId> jxbIds1 = new ArrayList<ObjectId>();
				for (N33_ZKBEntry zkbEntry : jxb1) {
					if (!zkbEntry.getJxbId().toString().equals(jxb2Id.toString())) {
						jxbIds1.add(zkbEntry.getJxbId());
					}
				}
				List<ObjectId> jxbIds2 = new ArrayList<ObjectId>();
				for (N33_ZKBEntry zkbEntry : jxb2) {
					if (!zkbEntry.getJxbId().toString().equals(jxb1Id.toString())) {
						jxbIds2.add(zkbEntry.getJxbId());
					}
				}
				Integer size1 = jxbctDao.getJxbCTByjxbIds(jxbId1, jxbIds1, 1);
				Integer size2 = jxbctDao.getJxbCTByjxbIds(jxbId2, jxbIds2, 1);
//                5b45a7a58fb25a6d70d344a6
				if (size1 != 0 || size2 != 0) {
					b = false;
				}
			}
			return b;
		} else {
			N33_JXBEntry entry = null;
			if (jxbId1 != null) {
				entry = jxbDao.getJXBById(jxbId1);
			} else {
				entry = jxbDao.getJXBById(jxbId2);
			}
			if (entry == null) {
				return false;
			}
			List<N33_SWEntry> swEntries = swDao.getSwByXqid(xqid);
			Boolean b = true;
			for (N33_SWEntry swEntry : swEntries) {
				if (swEntry.getTeacherIds().contains(entry.getTercherId()) && swEntry.getX() == X1 && swEntry.getY() == Y1) {
					b = false;
					break;
				}
				if (swEntry.getTeacherIds().contains(entry.getTercherId()) && swEntry.getX() == X2 && swEntry.getY() == Y2) {
					b = false;
					break;
				}
				if (entry.getType() == 6) {
					N33_JXBEntry entry3 = jxbDao.getJXBById(entry.getRelativeId());
					if (swEntry.getTeacherIds().contains(entry3.getTercherId()) && swEntry.getX() == X1 && swEntry.getY() == Y1) {
						b = false;
						break;
					}
					if (swEntry.getTeacherIds().contains(entry3.getTercherId()) && swEntry.getX() == X2 && swEntry.getY() == Y2) {
						b = false;
						break;
					}
				}
			}
			//老师是否上其他课程
			Integer count = week % 2;
			ObjectId tea1Id = entry.getTercherId();
			ObjectId jxb1Id = entry.getID();
			if (entry.getType() == 6) {
				if (count == 0) {
					N33_JXBEntry entry3 = jxbDao.getJXBById(entry.getRelativeId());
					if (entry3.getDanOrShuang() == 1) {
						tea1Id = entry3.getTercherId();
						jxb1Id = entry3.getID();
					}
				}
			}
			List<N33_ZKBEntry> pks1 = zkbDao.getN33_ZKBByWeek(week, Y2 - 1, X2 - 1, xqid, tea1Id);
			if (pks1.size() != 0) {
				b = false;
			}
			//学生冲突
			List<N33_ZKBEntry> jxb1 = zkbDao.getN33_ZKBByWeekG(week, Y2 - 1, X2 - 1, xqid, jxb1Id, entry.getGradeId());
			List<ObjectId> jxbIds1 = new ArrayList<ObjectId>();
			for (N33_ZKBEntry zkbEntry : jxb1) {
				jxbIds1.add(zkbEntry.getJxbId());
			}
			Integer size1 = jxbctDao.getJxbCTByjxbIds(jxbId1, jxbIds1);
			if (size1 != 0) {
				b = false;
			}
			return b;
		}
	}

	public void tkChangeType(ObjectId id, Integer type) {
		N33_TkLogEntry entry = tkLogDao.findN33_TkLogEntryById(id);
		if (entry != null) {
			if (type == 1) {
				List<String> userIds = new ArrayList<String>();
				if (entry.getType() == 0) {
					N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(entry.getOYkbId());
					N33_ZKBEntry orgZkbEntry = zkbDao.getN33_ZKBById(entry.getNYkbId());
					if (entry.getXcty() == 0) {
						zkbDao.updateN33_ZKB(zkbEntry, orgZkbEntry);
						zkbDao.updateN33_ZKB(orgZkbEntry, zkbEntry);
					} else {
						int sweek = Integer.valueOf(entry.getWeek().split("-")[0]);
						int eweek = Integer.valueOf(entry.getWeek().split("-")[1]);
						for (int i = sweek; i <= eweek; i++) {
							zkbDao.updateN33_ZKBByWeekXY(zkbEntry, i, orgZkbEntry);
							zkbDao.updateN33_ZKBByWeekXY(orgZkbEntry, i, zkbEntry);
						}
					}
					userIds.add(orgZkbEntry.getTeacherId().toString());
					userIds.add(zkbEntry.getTeacherId().toString());
					if (orgZkbEntry.getNTeacherId() != null) {
						userIds.add(orgZkbEntry.getNTeacherId().toString());
					}
					if (zkbEntry.getNTeacherId() != null) {
						userIds.add(zkbEntry.getTeacherId().toString());
					}
				} else if (entry.getType() == 1) {
					N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(entry.getOYkbId());
					if (entry.getXcty() == 0) {
						zkbEntry.setTeacherId(entry.getNTeacherId());
						zkbDao.updateN33_ZKB(zkbEntry);
					} else {
						int sweek = Integer.valueOf(entry.getWeek().split("-")[0]);
						int eweek = Integer.valueOf(entry.getWeek().split("-")[1]);
						for (int i = sweek; i <= eweek; i++) {
							zkbDao.updateN33_ZKB(zkbEntry, i, entry.getNTeacherId());
						}
					}
					userIds.add(zkbEntry.getTeacherId().toString());
					if (zkbEntry.getNTeacherId() != null) {
						userIds.add(zkbEntry.getTeacherId().toString());
					}
				}
				faTongZhiService.faTongZhi("调课通知,请注意查看新的课表", entry.getSchoolId().toString(), entry.getUserId().toString().toString(), userIds);
				tiaoKeShengQingDao.updateEntry(entry.getSqId(), 2);
			} else {
				tiaoKeShengQingDao.updateEntry(entry.getSqId(), 3);
			}
			entry.setCimType(type);
			tkLogDao.updateN33_TkLog(entry);

		}
	}

	/**
	 * @param gradeId
	 * @param termId
	 * @param ci
	 * @param schoolId
	 * @param userName
	 * @param response
	 */
	public void exportTiaoKeLog(String gradeId, String termId, String ci, ObjectId schoolId, String userName, HttpServletResponse response) {
		List<ObjectId> uids = new ArrayList<ObjectId>();
		if (StringUtils.isNotEmpty(userName)) {
			List<N33_TeaEntry> teaEntries = teaDao.getTeaList(userName, schoolId, new ObjectId(ci));
			if (teaEntries != null && teaEntries.size() != 0) {
				uids = MongoUtils.getFieldObjectIDs(teaEntries, "uid");
			}
		}
		List<N33_TkLogDTO> tkLogDTOs = new ArrayList<N33_TkLogDTO>();
		List<N33_TkLogEntry> tkLogEntries = tkLogDao.getTkLogByGradeId(new ObjectId(termId), schoolId, new ObjectId(gradeId), uids);
		List<ObjectId> subjectIds = new ArrayList<ObjectId>();
		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
		subjectIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "osid"));
		subjectIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "nsid"));
		teacherIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "uid"));
		teacherIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "otid"));
		teacherIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "ntid"));
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, new ObjectId(ci));
		List<ObjectId> ykbIds = new ArrayList<ObjectId>();
		ykbIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "oYkbId"));
		ykbIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "nYkbId"));
		Map<ObjectId, N33_ZKBEntry> ykbEntryMap = zkbDao.getZKBbyIds(ykbIds, schoolId);
		Map<ObjectId, N33_KSEntry> subjectEntryMap = subjectDao.getIsolateSubjectEntryBySubs(subjectIds);
		if (tkLogEntries != null && tkLogEntries.size() != 0) {
			for (N33_TkLogEntry entry : tkLogEntries) {
				N33_TkLogDTO dto = new N33_TkLogDTO(entry);
				dto.setTeacherName(teaEntryMap.get(entry.getOTeacherId()) != null ? teaEntryMap.get(entry.getOTeacherId()).getUserName() : "");
				String[] weekArg = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
				if (ykbEntryMap.get(entry.getOYkbId()) != null) {
					String subjectName = "";
					if (entry.getOSubjectId() != null && subjectEntryMap.get(entry.getOSubjectId()) != null) {
						subjectName = subjectEntryMap.get(entry.getOSubjectId()).getSubjectName();
						dto.setKeJie(subjectName + " " + weekArg[ykbEntryMap.get(entry.getOYkbId()).getX()] + "第" + (ykbEntryMap.get(entry.getOYkbId()).getY() + 1) + "节课");
					} else {
						dto.setKeJie(weekArg[ykbEntryMap.get(entry.getOYkbId()).getX()] + "第" + (ykbEntryMap.get(entry.getOYkbId()).getY() + 1) + "节课");
					}
				}
				dto.setNewTeacherName(teaEntryMap.get(entry.getNTeacherId()) != null ? teaEntryMap.get(entry.getNTeacherId()).getUserName() : "");
				if (entry.getType() == 0) {
					if (ykbEntryMap.get(entry.getNYkbId()) != null) {
						String subjectName = "";
						if (entry.getNSubjectId() != null && subjectEntryMap.get(entry.getNSubjectId()) != null) {
							subjectName = subjectEntryMap.get(entry.getNSubjectId()).getSubjectName();
							dto.setNewKeJie(subjectName + " " + weekArg[ykbEntryMap.get(entry.getNYkbId()).getX()] + "第" + (ykbEntryMap.get(entry.getNYkbId()).getY() + 1) + "节课");
						} else {
							dto.setNewKeJie(weekArg[ykbEntryMap.get(entry.getNYkbId()).getX()] + "第" + (ykbEntryMap.get(entry.getNYkbId()).getY() + 1) + "节课");
						}
					}
				}
				tkLogDTOs.add(dto);
			}
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		//生成一个sheet1
		HSSFSheet sheet = wb.createSheet("调代课记录表");
		HSSFRow row = sheet.createRow(0);

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("调/代课申请人");
		cell = row.createCell(1);
		cell.setCellValue("课节");
		cell = row.createCell(2);
		cell.setCellValue("调/代课交换教师");
		cell = row.createCell(3);
		cell.setCellValue("交换课节");
		cell = row.createCell(4);
		cell.setCellValue("处理日期");
		cell = row.createCell(5);
		cell.setCellValue("类型");
		cell = row.createCell(6);
		cell.setCellValue("作用期");
		cell = row.createCell(7);
		cell.setCellValue("作用周");
		cell = row.createCell(8);
		cell.setCellValue("状态");
		int page = 0;
		if (tkLogDTOs != null && tkLogDTOs.size() != 0) {
			for (N33_TkLogDTO dto : tkLogDTOs) {
				page++;
				row = sheet.createRow(page);
				cell = row.createCell(0);
				cell.setCellValue(dto.getTeacherName());
				cell = row.createCell(1);
				cell.setCellValue(dto.getKeJie());
				cell = row.createCell(2);
				cell.setCellValue(dto.getNewTeacherName());
				cell = row.createCell(3);
				cell.setCellValue(dto.getNewKeJie());
				cell = row.createCell(4);
				cell.setCellValue(dto.getTime());
				cell = row.createCell(5);
				if (dto.getType() == 0) {
					cell.setCellValue("调课");
				} else {
					cell.setCellValue("代课");
				}
				cell = row.createCell(6);
				if (dto.getXcty() == 0) {
					cell.setCellValue("短期");
				} else {
					cell.setCellValue("长期");
				}
				cell = row.createCell(7);
				cell.setCellValue(dto.getWeek());
				cell = row.createCell(8);
				if (dto.getCimType() == 0) {
					cell.setCellValue("未审核");
				} else if (dto.getCimType() == 1) {
					cell.setCellValue("已通过");
				} else {
					cell.setCellValue("已拒绝");
				}
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("调代课记录表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param gradeId
	 * @param termId
	 * @param ci
	 * @param schoolId
	 * @return
	 */
	public Map getTKTotal(String gradeId, String termId, String ci, ObjectId schoolId) {
		Map map = new HashMap();
		List<N33_TkLogEntry> tkLogEntries = tkLogDao.getTkLogByGradeId(new ObjectId(termId), schoolId, new ObjectId(gradeId));
		List<N33_KSDTO> ksdtos = n33_fenBanInfoSetService.getSubjectByType(new ObjectId(gradeId), schoolId, "0");
		List<N33_SubjectCntDTO> subjectCntDTOS = new ArrayList<N33_SubjectCntDTO>();
		if (ksdtos != null && ksdtos.size() != 0) {
			for (N33_KSDTO dto : ksdtos) {
				subjectCntDTOS.add(new N33_SubjectCntDTO(dto.getSubid(), dto.getSnm(), dto.getType()));
			}
		}
		int tkCnt = 0;
		int dkCnt = 0;
		int shortCnt = 0;
		int longCnt = 0;
		int hgCnt = 0;
		int djCnt = 0;
		int xzCnt = 0;
		List<N33_WeekCntDTO> weekCntDTOS = new ArrayList<N33_WeekCntDTO>();
		String[] args = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
		for (int i = 0; i <= 6; i++) {
			weekCntDTOS.add(new N33_WeekCntDTO(args[i], 0, i));
		}
		List<ObjectId> ykbIds = new ArrayList<ObjectId>();
		ykbIds.addAll(MongoUtils.getFieldObjectIDs(tkLogEntries, "oYkbId"));
		List<N33_ZKBEntry> zkbList = zkbDao.findZKBbyIds(ykbIds, schoolId);
		if (zkbList != null && zkbList.size() != 0) {
			for (N33_ZKBEntry entry : zkbList) {
				if (entry.getType() == 1) {
					djCnt++;
				} else if (entry.getType() == 2) {
					hgCnt++;
				} else if (entry.getType() == 3) {
					xzCnt++;
				}
				for (N33_WeekCntDTO dto : weekCntDTOS) {
					if (dto.getX() == entry.getX()) {
						dto.setCount(dto.getCount() + 1);
						break;
					}
				}
			}
		}
		if (tkLogEntries != null && tkLogEntries.size() != 0) {
			for (N33_TkLogEntry logEntry : tkLogEntries) {
				if (logEntry.getType() == 0) {
					tkCnt++;
				} else if (logEntry.getType() == 1) {
					dkCnt++;
				}
				if (logEntry.getXcty() == 0) {
					shortCnt++;
				} else if (logEntry.getXcty() == 1) {
					longCnt++;
				}
				if (subjectCntDTOS != null && subjectCntDTOS.size() != 0) {
					for (N33_SubjectCntDTO dto : subjectCntDTOS) {
						if (dto.getSubjectId().equals(logEntry.getOSubjectId().toString())) {
							if (logEntry.getXcty() == 0) {
								int cnt = dto.getShortCnt() + 1;
								dto.setShortCnt(cnt);
								dto.setAllCnt(cnt + dto.getLongCnt());
								break;
							} else if (logEntry.getXcty() == 1) {
								int cnt = dto.getLongCnt() + 1;
								dto.setLongCnt(cnt);
								dto.setAllCnt(cnt + dto.getShortCnt());
								break;
							}
						}
					}
				}
			}
		}

		map.put("tkCnt", tkCnt);
		map.put("dkCnt", dkCnt);
		map.put("shortCnt", shortCnt);
		map.put("longCnt", longCnt);
		map.put("hgCnt", hgCnt);
		map.put("djCnt", djCnt);
		map.put("xzCnt", xzCnt);
		map.put("weekCntDTOs", weekCntDTOS);
		map.put("subjectCntDTOs", subjectCntDTOS);
		return map;
	}

	public List<Map<String, Object>> getZhuanXiangZuHeXianQingAll(ObjectId xqid, String gradeId) {
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cids.add(paiKeTimes.getID());
			}
		}
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(termEntry.getSid()).getPaikeci();
		int acClassType = turnOffService.getAcClassType(termEntry.getSid(), new ObjectId(gradeId), ciId,1);
		//jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getZxJXBMap(cids, gradeId,acClassType);
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntryMap.values()) {
			zxkIds.add(jxbEntry.getID());
		}
		Map<ObjectId, N33_ClassroomEntry> classRoomDTO = classroomDao.getRoomEntryListByXqClassMapS(cids, new ObjectId(gradeId));
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		//学科老师
		Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySubjectIdG(cids, "*", gradeId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
			Map<String, Object> map = new HashMap<String, Object>();
			N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
			N33_TeaEntry teaEntry = teaEntries.get(entry.getTeaId());
			N33_ClassroomEntry entry2 = classRoomDTO.get(entry.getRoomId());
			map.put("name", entry.getName());
			map.put("zxname", entry1.getNickName());
			map.put("teaname", teaEntry.getUserName());
			map.put("roomname", entry2.getRoomName());
			map.put("stusize", entry.getStudentId().size());
			list.add(map);
		}
		return list;
	}
	public List<Map<String, Object>> getZhuanXiangZuHeXianQing(ObjectId xqid, String gradeId, ObjectId subjectId) {
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cids.add(paiKeTimes.getID());
			}
		}
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(termEntry.getSid()).getPaikeci();
		int acClassType = turnOffService.getAcClassType(termEntry.getSid(), new ObjectId(gradeId), ciId,1);
		//jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getZxJXBMapV1(cids, subjectId, gradeId,acClassType);
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntryMap.values()) {
			zxkIds.add(jxbEntry.getID());
		}
		Map<ObjectId, N33_ClassroomEntry> classRoomDTO = classroomDao.getRoomEntryListByXqClassMapS(cids, new ObjectId(gradeId));
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		//学科老师
		Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySubjectIdG(cids, subjectId, "*", gradeId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
			Map<String, Object> map = new HashMap<String, Object>();
			N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
			N33_TeaEntry teaEntry = teaEntries.get(entry.getTeaId());
			N33_ClassroomEntry entry2 = classRoomDTO.get(entry.getRoomId());
			map.put("name", entry.getName());
			map.put("zxname", entry1.getNickName());
			map.put("teaname", teaEntry.getUserName());
			map.put("roomname", entry2.getRoomName());
			map.put("stusize", entry.getStudentId().size());
			list.add(map);
		}
		return list;
	}
	
	
	/**
	 * 批量导出学生课表
	 *
	 * @param studentId
	 * @param schoolId
	 * @param week
	 * @return
	 */
	public Map getStusKB(String userName, ObjectId studentId, ObjectId schoolId, Integer week, ObjectId gid) {
		
		Integer count = week % 2;
		ObjectId xqid = getDefauleTermId(schoolId);
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);

		List<N33_ZKBEntry> zkbEntryList1 = zkbDao.getYKBsByclassRoomIds(xqid, schoolId, week, gid);
		ObjectId cid = null;
		if (zkbEntryList1.size() > 0) {
			cid = zkbEntryList1.get(0).getCId();
		} else {
			return null;
		}
		//查学生教学班列表
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		studentIds.add(studentId);
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gid, cid,1);
		//jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds,acClassType);

		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry entry : jxbEntries.values()) {
			zxkIds.add(entry.getID());
		}
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
			if (!jxbIds.contains(entry.getJxbId())) {
				jxbIds.add(entry.getJxbId());
			}
		}
		for (ObjectId objectId : jxbIds) {
			List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
				if (entry.getJxbId().toString().equals(objectId.toString())) {
					jis.add(entry);
				}
			}
			listMap.put(objectId, jis);
		}

		List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries, xqid);

		for (N33_ZKBEntry entry : zkbEntryList1) {
			if (entry.getType() == 5) {
				jiaoXueBanList.add(entry.getJxbId());
			}
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(jiaoXueBanList);
		//学科列表
		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(cid, schoolId);
		//查询教学班所在的所有周课表
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeek(jiaoXueBanList, week, xqid);
		List<N33_ZKBEntry> zkbEntries1 = zkbDao.getN33_ZKBByWeekForNJxbId(jiaoXueBanList, week, xqid);
		zkbEntries.addAll(zkbEntries1);
		//课节
//		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(schoolId.toString(), xqid);
		//教室列表
		List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, cid);
		//老师列表
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(cid, schoolId,gid);


		Map<String, N33_ZKBDTO> zkbMap = new HashMap<String, N33_ZKBDTO>();
		//TermEntry termEntry = termDao.getTermByTimeId(xqid);
		//固定事务
		List<N33_SWDTO> gdSwdtos = swService.getGuDingShiWuBySchoolId(xqid, schoolId);

		for (N33_ZKBEntry entry : zkbEntries) {
			N33_ZKBDTO dto = new N33_ZKBDTO(entry);
			dto.setIsSWAndCourse(0);

			if (entry.getType() != 4 && entry.getType() != 5) {
				dto.setSwType(0);
				if (entry.getNTeacherId() != null) {
					//双周导出双周，单周导出单周
//					if (count == 0) {
//						dto.setTeacherName(teaEntryMap.get(entry.getNTeacherId()).getUserName());
//					} else {
//						dto.setTeacherName(teaEntryMap.get(entry.getTeacherId()).getUserName());
//					}
					//同时导出单双周
					dto.setTeacherName(teaEntryMap.get(entry.getTeacherId()).getUserName());
					dto.setnTeacherName(teaEntryMap.get(entry.getNTeacherId()).getUserName());
				} else {
					if(teaEntryMap.get(entry.getTeacherId()) != null){
						dto.setTeacherName(teaEntryMap.get(entry.getTeacherId()).getUserName());
					}else{
						dto.setTeacherName("");
					}
				}

				//教学班
				if (entry.getNTeacherId() != null) {
					//同时导出单双周
					N33_JXBEntry entry1 = jxbEntries.get(entry.getNJxbId());
					if (StringUtils.isNotEmpty(entry1.getNickName())) {
						dto.setnJxbName(entry1.getNickName());
					}else{
						dto.setnJxbName(entry1.getName());
					}
					N33_JXBEntry entry2 = jxbEntries.get(entry.getJxbId());
					if (StringUtils.isNotEmpty(entry2.getNickName())) {
						dto.setJxbName(entry2.getNickName());
					}else{
						dto.setJxbName(entry2.getName());
					}
					//双周导出双周，单周导出单周
//					if (count == 0) {
//						N33_JXBEntry entry1 = jxbEntries.get(entry.getNJxbId());
//						if (StringUtils.isNotEmpty(entry1.getNickName())) {
//							dto.setJxbName(entry1.getNickName());
//						} else {
//							dto.setJxbName(entry1.getName());
//						}
//					} else {
//						N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
//						if (StringUtils.isNotEmpty(entry1.getNickName())) {
//							dto.setJxbName(entry1.getNickName());
//						} else {
//							dto.setJxbName(entry1.getName());
//						}
//					}
				} else {
					N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
					if (StringUtils.isNotEmpty(entry1.getNickName())) {
						dto.setJxbName(entry1.getNickName());
					} else {
						dto.setJxbName(entry1.getNickName());
					}
				}
			} else {
				if (entry.getType() == 4) {
					List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
					for (N33_ZhuanXiangEntry entry1 : zhuanList) {
						if (entry1.getStudentId().contains(studentId)) {
							dto.setJxbName(entry1.getName());
							dto.setTeacherName(teaEntryMap.get(entry1.getTeaId()).getUserName());
							for (N33_ClassroomEntry room : classRoomDTO) {
								//获取学科
								if (room.getRoomId().toString().equals(entry1.getRoomId().toString())) {
									dto.setRoomName(room.getRoomName());
									break;
								}
							}

						}
					}
				} else {
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					if(!entry1.getStudentIds().contains(studentId)){
						continue;
					}
					if (entry1.getStudentIds().contains(studentId)) {
						dto.setJxbName(entry1.getName());
						dto.setTeacherName("");
						N33_TeaEntry teaEntry = teaEntryMap.get(entry1.getTeacherId());
						if (teaEntry != null) {
							dto.setTeacherName(teaEntry.getUserName());
						}
						dto.setSubjectName("自习课");
						for (N33_ClassroomEntry room : classRoomDTO) {
							//获取学科
							if (room.getRoomId().toString().equals(entry1.getClassroomId().toString())) {
								dto.setRoomName(room.getRoomName());
								break;
							}
						}
					}
				}
			}
			if (entry.getType() != 5) {
				for (N33_KSDTO ksdto : gradeSubject) {
					//获取学科
					if (entry.getNTeacherId() != null) {
						//同时导出单双周
						if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							dto.setnSubjectName(ksdto.getSnm());
						}
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							dto.setSubjectName(ksdto.getSnm());
							break;
						}
						//双周导出双周，单周导出单周
//						if (count == 0) {
//							if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
//								dto.setSubjectName(ksdto.getSnm());
//								break;
//							}
//						} else {
//							if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
//								dto.setSubjectName(ksdto.getSnm());
//								break;
//							}
//						}
					} else {
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							dto.setSubjectName(ksdto.getSnm());
							break;
						}
					}
				}
			}
			//教室
			if (entry.getType() != 4 && entry.getType() != 5) {
				for (N33_ClassroomEntry room : classRoomDTO) {
					//获取学科
					if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
						dto.setRoomName(room.getRoomName());
						break;
					}
				}
			}
			zkbMap.put(dto.getX() + "" + dto.getY(), dto);
		}

		List<ObjectId> gradeIds = new ArrayList<ObjectId>();
		gradeIds.add(gid);
		List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, schoolId, gradeIds);
		for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
			if (zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY()) != null) {
				N33_ZKBDTO zkbdto = zkbMap.get(gdsxEntry.getX() + "" + gdsxEntry.getY());
				zkbdto.setIsSWAndCourse(1);
				zkbdto.setSwType(1);
				if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
					String s = zkbdto.getSwDesc();
					s += "\r\n";
					s += gdsxEntry.getDesc();
					zkbdto.setSwDesc(s);
				} else {
					zkbdto.setSwDesc(gdsxEntry.getDesc());
				}
				zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
			} else {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setSwType(1);
				zkbdto.setIsSWAndCourse(0);
				zkbdto.setSwDesc(gdsxEntry.getDesc());
				zkbMap.put(gdsxEntry.getX() + "" + gdsxEntry.getY(), zkbdto);
			}
		}

		for (N33_SWDTO dto : gdSwdtos) {
			if (zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1)) != null) {
				N33_ZKBDTO zkbdto = zkbMap.get((dto.getY() - 1) + "" + (dto.getX() - 1));
				zkbdto.setIsSWAndCourse(1);
				zkbdto.setSwType(1);
				if (zkbdto.getSwDesc() != null && zkbdto.getSwDesc() != "") {
					String s = zkbdto.getSwDesc();
					s += "\r\n";
					if (dto.getSk() == 1) {
						s += "自习课";
					} else {
						s += dto.getDesc();
					}
					zkbdto.setSwDesc(s);
				} else {
					if (dto.getSk() == 1) {
						zkbdto.setSwDesc("自习课");
					} else {
						zkbdto.setSwDesc(dto.getDesc());
					}
				}
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX( ) - 1), zkbdto);
			} else {
				N33_ZKBDTO zkbdto = new N33_ZKBDTO();
				zkbdto.setIsSWAndCourse(0);
				zkbdto.setSwType(1);
				if (dto.getSk() == 1) {
					zkbdto.setSwDesc("自习课");
				} else {
					zkbdto.setSwDesc(dto.getDesc());
				}
				zkbMap.put((dto.getY() - 1) + "" + (dto.getX() - 1), zkbdto);
			}
		}
		return zkbMap;
	}
	
	
	public void exportStusKB(HttpServletResponse response,List dataList, ObjectId schoolId) {//String userName, ObjectId studentId, ObjectId schoolId, Integer week, ObjectId gid, 

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("学生" + "课表");

		
		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
		sheet.setColumnWidth((short) 1, (short) 5500);
		sheet.setColumnWidth((short) 2, (short) 5500);
		sheet.setColumnWidth((short) 3, (short) 5500);
		sheet.setColumnWidth((short) 4, (short) 5500);
		sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
		sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
		sheet.setColumnWidth((short) 7, (short) 5500);

		HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
		headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
		headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
		headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
		headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle1.setWrapText(true);

		HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont1.setFontHeightInPoints((short) 12);    //设置字体大小
		headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
		headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		headerStyle2.setWrapText(true);


		HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont2.setFontHeightInPoints((short) 10);    //设置字体大小
		headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
		headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle3.setWrapText(true);

		ObjectId xqid = getDefauleTermId(schoolId);
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		//课节
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(schoolId.toString(), xqid);
		int page = 0;
	for(int k=0 ; k<dataList.size() ;k++ ){
		Map zkbMap = (Map) dataList.get(k);
		sheet.addMergedRegion(new Region(page, (short)0, page, (short)7 ));
		//为sheet1生成第一行，用于放表头信息
		HSSFRow row1 = sheet.createRow(page);
		row1.setHeightInPoints(40);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(headerStyle1);
		cell1.setCellValue(termEntry.getXqName() + "  " + zkbMap.get("userName") + "  课表");
		row1.createCell(1).setCellStyle(headerStyle1);
		row1.createCell(2).setCellStyle(headerStyle1);
		row1.createCell(3).setCellStyle(headerStyle1);
		row1.createCell(4).setCellStyle(headerStyle1);
		row1.createCell(5).setCellStyle(headerStyle1);
		row1.createCell(6).setCellStyle(headerStyle1);
		row1.createCell(7).setCellStyle(headerStyle1);

		//为sheet1生成第二行，用于放表头信息
		HSSFRow row = sheet.createRow(page + 1);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("课节 / 日");
		List<String> weekLists = new ArrayList<String>();
		weekLists.add("周一");
		weekLists.add("周二");
		weekLists.add("周三");
		weekLists.add("周四");
		weekLists.add("周五");
		weekLists.add("周六");
		weekLists.add("周日");
		for (int j = 0; j < weekLists.size(); j++) {
			cell = row.createCell(j + 1);
			cell.setCellStyle(headerStyle2);
			cell.setCellValue(weekLists.get(j));
		}
		page += 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < courseRangeDTOs.size(); i++) {
				page ++;
				row = sheet.createRow(page);
				row.setHeightInPoints(50);
				cell = row.createCell(0);
				cell.setCellStyle(headerStyle2);
				cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
				for (int j = 0; j < weekLists.size(); j++) {
					cell = row.createCell(j + 1);
					cell.setCellStyle(headerStyle3);
					N33_ZKBDTO dto = (N33_ZKBDTO) zkbMap.get(j + "" + i);
					if (dto != null) {
						if (dto.getIsSWAndCourse() == 0) {
							if (dto.getSwType() == 0) {
								StringBuffer sb = new StringBuffer();
								if (dto.getJxbName() != null) {
									//同时导出单双周
									if(!"".equals(dto.getnJxbName()) && dto.getnJxbName() != null){
										sb.append(dto.getJxbName() + "（单）/" + dto.getnJxbName() + "(双)" + "\r\n");
									}else{
										sb.append(dto.getJxbName() + "\r\n");
									}
									//双周导出双周，单周导出单周
//									sb.append(dto.getJxbName() + "\r\n");
								}
								if (dto.getTeacherName() != null) {
									//同时导出单双周
									if(!"".equals(dto.getnTeacherName()) && dto.getnTeacherName() != null){
										sb.append(dto.getTeacherName() + "（单）/" + dto.getnTeacherName() + "(双)" + "\r\n");
									}else{
										sb.append(dto.getTeacherName() + "\r\n");
									}
									//双周导出双周，单周导出单周
//									sb.append(dto.getTeacherName() + "\r\n");
								}
								if (dto.getRoomName() != null) {
									sb.append(dto.getRoomName());
								}
								String con = sb.toString();
								cell.setCellValue(con);
							} else {
								cell.setCellValue(dto.getSwDesc());
							}
						} else {
							StringBuffer sb = new StringBuffer();
							if (dto.getJxbName() != null) {
								//同时导出单双周
								if(!"".equals(dto.getnJxbName()) && dto.getnJxbName() != null){
									sb.append(dto.getJxbName() + "（单）/" + dto.getnJxbName() + "(双)" + "\r\n");
								}else{
									sb.append(dto.getJxbName() + "\r\n");
								}
								//双周导出双周，单周导出单周
//								sb.append(dto.getJxbName() + "\r\n");
							}
							if (dto.getTeacherName() != null) {
								//同时导出单双周
								if(!"".equals(dto.getnTeacherName()) && dto.getnTeacherName() != null){
									sb.append(dto.getTeacherName() + "（单）/" + dto.getnTeacherName() + "(双)" + "\r\n");
								}else{
									sb.append(dto.getTeacherName() + "\r\n");
								}
								//双周导出双周，单周导出单周
//								sb.append(dto.getTeacherName() + "\r\n");
							}
							if (dto.getRoomName() != null) {
								sb.append(dto.getRoomName() + "\r\n");
							}
							sb.append("事务：" + dto.getSwDesc());
							String con = sb.toString();
							cell.setCellValue(con);
						}
					}
				}
			}
			page = page + 2;
		}
	}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("学生" + "课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//获取单个学科的课表 oz
	public Map getSubKB(ObjectId schoolId, ObjectId xqid, String gradeId, ObjectId subjectId, Integer week, ObjectId sid) {
		Map<String, Map<String, Object>> list = new ConcurrentHashMap<String, Map<String, Object>>();
		//学期发布次
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cids.add(paiKeTimes.getID());
			}
		}
		//查询老师
		Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findTeasByST(cids, schoolId);
		List<ObjectId> userIds = new ArrayList<ObjectId>();
		for (N33_TeaEntry teaEntry : teaEntries.values()) {
			userIds.add(teaEntry.getUserId());
		}
		Map<ObjectId, N33_TeaEntry> userMap = teaDao.getTeaMap(userIds, cids);
		//此年级此学科所有的教学班
		List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
		int acClassType = turnOffService.getAcClassType(schoolId, new ObjectId(gradeId), ciId,1);
		//jxb add
		jxbEntries = jxbDao.getJXBList(new ObjectId(gradeId), subjectId, cids,acClassType);
		//查询此年级此学科第多少周所有的周课表
		List<N33_ZKBEntry> zkbEntries = new ArrayList<N33_ZKBEntry>(); 
		zkbEntries = zkbDao.getZKBsBySW(schoolId, gradeId, xqid, subjectId,week);
		//遍历周课表查找其中所有的自习课
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_ZKBEntry zkbEntry : zkbEntries) {
			//System.out.println("x:" + zkbEntry.getX() + "  " + "y:" + zkbEntry.getY());
			if (zkbEntry.getType() == 5) {
				zxkIds.add(zkbEntry.getJxbId());
			}
		}
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			jxbEntryMap.put(jxbEntry.getID(), jxbEntry);
			jxbIds.add(jxbEntry.getID());
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxkIds);//查询自习课
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbIds);//查询专项课
		Integer count = week % 2;
		//遍历周课表
		for (N33_ZKBEntry entry : zkbEntries) {
		
			Boolean bf = true;
			if (list.size() > 0) {
				Integer x = Integer.parseInt(String.valueOf(entry.getX() + 1));
				Integer y = Integer.parseInt(String.valueOf(entry.getY() + 1));
				//判断坐标是否存在值
				for (Map<String, Object> lt : list.values()) {
					//同一个坐标
					Integer x1 = (Integer) lt.get("x");
					Integer y1 = (Integer) lt.get("y");
					if (x1 == x && y1 == y) {
						bf = false;
						List<String> tsName = (List<String>) lt.get("tsName");
						if (tsName == null) {
							tsName = new ArrayList<String>();
						}
						String name = "";
						if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}
						if (entry.getType() == 5) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}
						if (entry.getType() == 6) {
							if (count == 0) {
								if(jxbEntryMap.containsKey(entry.getNJxbId())){
									name = userMap.get(entry.getNTeacherId()).getUserName();
									N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
									name += "-" + entry1.getName();
									tsName.add(name);
								}else{
									continue;
								}
							} else {
								if(jxbEntryMap.containsKey(entry.getJxbId())){
									name = userMap.get(entry.getTeacherId()).getUserName();
									N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
									name += "-" + entry1.getName();
									tsName.add(name);
								}else{
									continue;
								}
							}
						}
						if (entry.getType() == 4) {
							for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
								if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
									String nm = userMap.get(entry1.getTeaId()).getUserName();
									N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
									nm += "-" + entryd.getName();
									tsName.add(nm);
								}
							}
						}
						lt.put("tsName", tsName);
					}
				}
			}
			if (bf) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", entry.getX() + 1);
				map.put("y", entry.getY() + 1);
				map.put("title", "查看授课老师");
				List<String> tsName = new ArrayList<String>();
				String name = "";
				// 1:等级考 2:合格考  3:行政型  4:专项型  5:自习课  6:单双周
				if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
					name += "-" + entry1.getName();
					tsName.add(name);
				}
				if (entry.getType() == 5) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					name += "-" + entry1.getName();
					tsName.add(name);
				}
				if (entry.getType() == 6) {
					if (count == 0) {
						if(jxbEntryMap.containsKey(entry.getNJxbId())){
							name = userMap.get(entry.getNTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}else{
							continue;
						}
					} else {
						if(jxbEntryMap.containsKey(entry.getJxbId())){
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}else{
							continue;
						}
					}
				}
				if (entry.getType() == 4) {
					for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
						if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
							String nm = userMap.get(entry1.getTeaId()).getUserName();
							N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
							nm += "-" + entryd.getName();
							tsName.add(nm);
						}
					}
				}
				map.put("tsName", tsName);
				map.put("isSwAndCourse", 0);
				map.put("swType", 0);
				list.put(entry.getX() + "" + entry.getY(), map);
			}
		}
		List<N33_SWDTO> swdtos = swService.getGuDingShiWuByXqid(xqid);
		List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(xqid, sid, new ObjectId(gradeId));
		if (list != null) {
			for (N33_SWDTO swdto : swdtos) {
				boolean flag = true;
				for (Map.Entry<String, Map<String, Object>> entry1 : list.entrySet()) {
					Map<String, Object> map = entry1.getValue();
					List<String> stringList = (List<String>) map.get("tsName");
					if (new Integer((Integer) map.get("x")).intValue() == swdto.getY().intValue() && new Integer((Integer) map.get("y")).intValue() == swdto.getX().intValue() && stringList != null && stringList.size() > 0) {
						flag = false;
						map.put("isSwAndCourse", 1);
						map.put("swType", 1);
						if (map.get("SwDesc") != null) {
							String s = (String) map.get("SwDesc");
							s += "\r\n";
							s += swdto.getDesc();
							map.put("SwDesc", s);
							list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
						} else {
							map.put("SwDesc", swdto.getDesc());
							list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
						}
					} else if (new Integer((Integer) map.get("x")).intValue() == swdto.getY().intValue() && new Integer((Integer) map.get("y")).intValue() == swdto.getX().intValue() && (stringList == null || stringList.size() == 0)) {
						flag = false;
						map.put("swType", 1);
						map.put("SwDesc", swdto.getDesc());
						list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
					}
				}
				if (flag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("x", swdto.getY().intValue());
					map.put("y", swdto.getX().intValue());
					map.put("SwDesc", swdto.getDesc());
					map.put("isSwAndCourse", 0);
					map.put("swType", 1);
					list.put((swdto.getY() - 1) + "" + (swdto.getX() - 1), map);
				}
			}
		}

		if (list != null) {
			for (N33_GDSXDTO gdsxdto : gdsxdtos) {
				boolean flag = true;  
				for (Map.Entry<String, Map<String, Object>> entry1 : list.entrySet()) {
					Map<String, Object> map = entry1.getValue();
					List<String> stringList = (List<String>) map.get("tsName");
					if ((new Integer((Integer) map.get("x")).intValue() - 1) == (gdsxdto.getX().intValue()) && (new Integer((Integer) map.get("y")).intValue() - 1) == (gdsxdto.getY().intValue()) && stringList != null && stringList.size() > 0) {
						flag = false;
						map.put("isSwAndCourse", 1);
						map.put("swType", 1);
						if (map.get("SwDesc") != null) {
							String s = (String) map.get("SwDesc");
							s += "\r\n";
							s += gdsxdto.getDesc();
							map.put("SwDesc", s);
							list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
						} else {
							map.put("SwDesc", gdsxdto.getDesc());
							list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
						}
					} else if ((new Integer((Integer) map.get("x")).intValue() - 1) == (gdsxdto.getX().intValue()) && (new Integer((Integer) map.get("y")).intValue() - 1) == (gdsxdto.getY().intValue()) && (stringList == null || stringList.size() == 0)) {
						flag = false;
						map.put("swType", 1);
						map.put("SwDesc", gdsxdto.getDesc());
						list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
					}
				}
				if (flag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("x", gdsxdto.getX() + 1);
					map.put("y", gdsxdto.getY() + 1);
					map.put("SwDesc", gdsxdto.getDesc());
					map.put("isSwAndCourse", 0);
					map.put("swType", 1);
					list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
				}
			}
		}
		Map cMap = new HashMap();
		cMap.put("cid", zkbEntries.get(0).getCId().toString());
		list.put("cid",cMap);
		return list;
//		//课节
//		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(sid.toString(), xqid);
	}
	
	 //批量导出学科课表
	 public void expSubKBExcels(List<Map> dataList,ObjectId xqid, String gradeId, Integer week, ObjectId sid, HttpServletResponse response) {
		
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		//课节
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(sid.toString(), xqid);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("学科课表");
		
		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
		sheet.setColumnWidth((short) 1, (short) 5500);
		sheet.setColumnWidth((short) 2, (short) 5500);
		sheet.setColumnWidth((short) 3, (short) 5500);
		sheet.setColumnWidth((short) 4, (short) 5500);
		sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
		sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
		sheet.setColumnWidth((short) 7, (short) 5500);

		HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
		headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
		headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
		headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
		headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle1.setWrapText(true);

		HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
		headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
		headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		headerStyle2.setWrapText(true);


		HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
		headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
		headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle3.setWrapText(true);

		int rcow = 0;//渲染数据开始行
	for(Map list : dataList){
		sheet.addMergedRegion(new Region(rcow, (short)0, rcow, (short)7 ));
		//为sheet1生成第一行，用于放表头信息
		HSSFRow row1 = sheet.createRow(rcow);
		row1.setHeightInPoints(40);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(headerStyle1);
		cell1.setCellValue(termEntry.getXqName() + list.get("subjectName").toString() +"学科课表");
		row1.createCell(1).setCellStyle(headerStyle1);
		row1.createCell(2).setCellStyle(headerStyle1);
		row1.createCell(3).setCellStyle(headerStyle1);
		row1.createCell(4).setCellStyle(headerStyle1);
		row1.createCell(5).setCellStyle(headerStyle1);
		row1.createCell(6).setCellStyle(headerStyle1);
		row1.createCell(7).setCellStyle(headerStyle1);

		//为sheet1生成第二行，用于放表头信息
		HSSFRow row = sheet.createRow(rcow + 1);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("课节 / 日");
		List<String> weekLists = new ArrayList<String>();
		weekLists.add("周一");
		weekLists.add("周二");
		weekLists.add("周三");
		weekLists.add("周四");
		weekLists.add("周五");
		weekLists.add("周六");
		weekLists.add("周日");
		for (int j = 0; j < weekLists.size(); j++) {
			cell = row.createCell(j + 1);
			cell.setCellStyle(headerStyle2);
			cell.setCellValue(weekLists.get(j));
		}

		rcow = rcow + 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < courseRangeDTOs.size(); i++) {
				rcow++;
				row = sheet.createRow(rcow);
				row.setHeightInPoints(50);
				cell = row.createCell(0);
				cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
				cell.setCellStyle(headerStyle2);
				for (int j = 0; j < weekLists.size(); j++) {
					cell = row.createCell(j + 1);
					cell.setCellStyle(headerStyle3);
					Map<String, Object> dto = (Map<String, Object>) list.get(j + "" + i);
					if (dto != null) {
						String str = "";
						List<String> list1 = (List<String>) dto.get("tsName");
						if (list1 != null && list1.size() > 0) {
							for (String s : list1) {
								str += s + "\r\n";
							}
						}
						if (dto.get("SwDesc") != null && dto.get("SwDesc") != "") {
							if (str == "") {
								str += "事务：";
							} else {
								str += "\r\n事务：";
							}
							str += (String) dto.get("SwDesc");
						}
						cell.setCellValue(str);
					}
				}
			}
			rcow = rcow + 2;
		}
	}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("学科课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 }

	public Map getTeacherKbByZKB(ObjectId xqId,String zkbId, ObjectId schoolId,ObjectId teacherId,String pkci) {
		Map map = new HashMap();
		Map<String, List<N33_ZKBDTO>> ykbdtoMaps = new HashMap<String, List<N33_ZKBDTO>>();
		N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(new ObjectId(zkbId));
		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
		teacherIds.add(teacherId);
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds);
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(schoolId.toString(), xqId);
		N33_XKBDTO dto = new N33_XKBDTO();
		dto.setTeacherName(teaEntryMap.get(teacherId).getUserName());
		dto.setTeacherId(teacherId.toString());
		dto.setCourseRangeDTOList(courseRangeDTOs);
		ykbdtoMaps = getZKBbyTeacherIds(teacherIds, zkbEntry.getTermId(),new ObjectId(pkci), zkbEntry.getSchoolId(),zkbEntry.getWeek());
		map.put("teakbs", ykbdtoMaps);
		map.put("teas", dto);
		return map;
	}

	public Map<String, List<N33_ZKBDTO>> getZKBbyTeacherIds(List<ObjectId> teacherIds, ObjectId ciId,ObjectId termId, ObjectId schoolId,int week) {
		Map<ObjectId,N33_ClassroomEntry> classroomEntryMap = new HashMap<ObjectId, N33_ClassroomEntry>();
		Map<String, String> subMap = subjectService.getSubjectMapById(schoolId, termId);
		List<N33_ZKBEntry> ykbEntryList = zkbDao.getZKBEntrysListByTeacherIds(ciId, schoolId,week,teacherIds);
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
		Map<String, List<N33_ZKBDTO>> ykbMaps = new HashMap<String, List<N33_ZKBDTO>>();
		List<N33_ZKBDTO> ykbdtos = new ArrayList<N33_ZKBDTO>();
		if (ykbEntryList!=null && ykbEntryList.size()!=0) {
			List<ObjectId> classroomIds = MongoUtils.getFieldObjectIDs(ykbEntryList,"clsrmId");
			List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList,"jxbId");
			jxbIds.addAll(MongoUtils.getFieldObjectIDs(ykbEntryList,"nJxbId"));
			jxbEntryMap = jxbDao.getJXBMapsByIds(jxbIds);
			classroomEntryMap = classroomDao.getRoomEntryListByXqRoomForMap(termId,classroomIds);
			List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntryByTeaIds(ciId, teacherIds);
			for (N33_ZKBEntry entry : ykbEntryList) {
				N33_ZKBDTO dto = new N33_ZKBDTO(entry);
				ObjectId id = null;
				if (teacherIds.contains(entry.getTeacherId())) {
					id = entry.getJxbId();
				}
				if (teacherIds.contains(entry.getNTeacherId())) {
					id = entry.getNJxbId();
				}
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
		ykbMaps.put(teacherIds.get(0).toString(), ykbdtos);
		return ykbMaps;
	}
	
	
	/**
	 * 查看课表模块-学科课表某块-导出学科课表
	 * oz 2019-3-8
	 * @param
	 * @return
	 */
	public void expSubKB(ObjectId schoolId,ObjectId xqid, String gradeId, ObjectId subjectId, Integer week, ObjectId sid, HttpServletResponse response) {
		Map<String, Map<String, Object>> list = new ConcurrentHashMap<String, Map<String, Object>>();
		//学期发布次
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cids.add(paiKeTimes.getID());
			}
		}
		//查询老师
		Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findTeasByST(cids, schoolId);
		List<ObjectId> userIds = new ArrayList<ObjectId>();
		for (N33_TeaEntry teaEntry : teaEntries.values()) {
			userIds.add(teaEntry.getUserId());
		}
		Map<ObjectId, N33_TeaEntry> userMap = teaDao.getTeaMap(userIds, cids);
		//此年级此学科所有的教学班
		List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
		int acClassType = turnOffService.getAcClassType(schoolId, new ObjectId(gradeId), ciId,1);
		//jxb add
		jxbEntries = jxbDao.getJXBList(new ObjectId(gradeId), subjectId, cids,acClassType);
		//查询此年级此学科第多少周所有的周课表
		List<N33_ZKBEntry> zkbEntries = new ArrayList<N33_ZKBEntry>(); 
		zkbEntries = zkbDao.getZKBsBySW(schoolId, gradeId, xqid, subjectId,week);
		//遍历周课表查找其中所有的自习课
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_ZKBEntry zkbEntry : zkbEntries) {
			if (zkbEntry.getType() == 5) {
				zxkIds.add(zkbEntry.getJxbId());
			}
		}
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			jxbEntryMap.put(jxbEntry.getID(), jxbEntry);
			jxbIds.add(jxbEntry.getID());
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxkIds);//查询自习课
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbIds);//查询专项课
		Integer count = week % 2;
		//遍历周课表并且返回数据封装数据
		for (N33_ZKBEntry entry : zkbEntries) {
		
			Boolean bf = true;
			if (list.size() > 0) {
				Integer x = Integer.parseInt(String.valueOf(entry.getX() + 1));
				Integer y = Integer.parseInt(String.valueOf(entry.getY() + 1));
				//判断坐标是否存在值
				for (Map<String, Object> lt : list.values()) {
					//同一个坐标
					Integer x1 = (Integer) lt.get("x");
					Integer y1 = (Integer) lt.get("y");
					if (x1 == x && y1 == y) {
						bf = false;
						List<String> tsName = (List<String>) lt.get("tsName");
						if (tsName == null) {
							tsName = new ArrayList<String>();
						}
						String name = "";
						if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}
						if (entry.getType() == 5) {//单双周
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}
						if (entry.getType() == 6) {//自习课
							if (count == 0) {
								if(jxbEntryMap.containsKey(entry.getNJxbId())){
									name = userMap.get(entry.getNTeacherId()).getUserName();
									N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
									name += "-" + entry1.getName();
									tsName.add(name);
								}else{
									continue;
								}
							} else {
								if(jxbEntryMap.containsKey(entry.getJxbId())){
									name = userMap.get(entry.getTeacherId()).getUserName();
									N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
									name += "-" + entry1.getName();
									tsName.add(name);
								}else{
									continue;
								}
							}
						}
						if (entry.getType() == 4) {
							for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
								if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
									String nm = userMap.get(entry1.getTeaId()).getUserName();
									N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
									nm += "-" + entryd.getName();
									tsName.add(nm);
								}
							}
						}
						lt.put("tsName", tsName);
					}
				}
			}
			if (bf) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", entry.getX() + 1);
				map.put("y", entry.getY() + 1);
				map.put("title", "查看授课老师");
				List<String> tsName = new ArrayList<String>();
				String name = "";
				// type 1:等级考 2:合格考  3:行政型  4:专项型  5:自习课  6:单双周
				if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
					name += "-" + entry1.getName();
					tsName.add(name);
				}
				if (entry.getType() == 5) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					name += "-" + entry1.getName();
					tsName.add(name);
				}
				if (entry.getType() == 6) {
					if (count == 0) {
						if(jxbEntryMap.containsKey(entry.getNJxbId())){
							name = userMap.get(entry.getNTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}else{
							continue;
						}
					} else {
						if(jxbEntryMap.containsKey(entry.getJxbId())){
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
							name += "-" + entry1.getName();
							tsName.add(name);
						}else{
							continue;
						}
					}
				}
				if (entry.getType() == 4) {
					for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
						if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
							String nm = userMap.get(entry1.getTeaId()).getUserName();
							N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
							nm += "-" + entryd.getName();
							tsName.add(nm);
						}
					}
				}
				map.put("tsName", tsName);
				map.put("isSwAndCourse", 0);
				map.put("swType", 0);
				list.put(entry.getX() + "" + entry.getY(), map);
			}
		}
		List<N33_SWDTO> swdtos = swService.getGuDingShiWuByXqid(xqid);//固定事务
		List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(xqid, sid, new ObjectId(gradeId));//固定事项
		if (list != null) {
			for (N33_SWDTO swdto : swdtos) {
				boolean flag = true;
				for (Map.Entry<String, Map<String, Object>> entry1 : list.entrySet()) {
					Map<String, Object> map = entry1.getValue();
					List<String> stringList = (List<String>) map.get("tsName");
					if (new Integer((Integer) map.get("x")).intValue() == swdto.getY().intValue() && new Integer((Integer) map.get("y")).intValue() == swdto.getX().intValue() && stringList != null && stringList.size() > 0) {
						flag = false;
						map.put("isSwAndCourse", 1);
						map.put("swType", 1);
						if (map.get("SwDesc") != null) {
							String s = (String) map.get("SwDesc");
							s += "\r\n";
							s += swdto.getDesc();
							map.put("SwDesc", s);
							list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
						} else {
							map.put("SwDesc", swdto.getDesc());
							list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
						}
					} else if (new Integer((Integer) map.get("x")).intValue() == swdto.getY().intValue() && new Integer((Integer) map.get("y")).intValue() == swdto.getX().intValue() && (stringList == null || stringList.size() == 0)) {
						flag = false;
						map.put("swType", 1);
						map.put("SwDesc", swdto.getDesc());
						list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
					}
				}
				if (flag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("x", swdto.getY().intValue());
					map.put("y", swdto.getX().intValue());
					map.put("SwDesc", swdto.getDesc());
					map.put("isSwAndCourse", 0);
					map.put("swType", 1);
					list.put((swdto.getY() - 1) + "" + (swdto.getX() - 1), map);
				}
			}
		}

		if (list != null) {
			for (N33_GDSXDTO gdsxdto : gdsxdtos) {
				boolean flag = true;  
				for (Map.Entry<String, Map<String, Object>> entry1 : list.entrySet()) {
					Map<String, Object> map = entry1.getValue();
					List<String> stringList = (List<String>) map.get("tsName");
					if ((new Integer((Integer) map.get("x")).intValue() - 1) == gdsxdto.getX().intValue() && (new Integer((Integer) map.get("y") ).intValue()- 1) == gdsxdto.getY().intValue() && stringList != null && stringList.size() > 0) {
					//if ((new Integer((Integer) map.get("x")).intValue() - 1) == gdsxdto.getX().intValue() && (new Integer((Integer) map.get("y") - 1).intValue()) == gdsxdto.getY().intValue() && stringList != null && stringList.size() > 0) {
						flag = false;
						map.put("isSwAndCourse", 1);
						map.put("swType", 1);
						if (map.get("SwDesc") != null) {
							String s = (String) map.get("SwDesc");
							s += "\r\n";
							s += gdsxdto.getDesc();
							map.put("SwDesc", s);
							list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
						} else {
							map.put("SwDesc", gdsxdto.getDesc());
							list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
						}
					} else if ((new Integer((Integer) map.get("x")).intValue() - 1) == gdsxdto.getX().intValue() && (new Integer((Integer) map.get("y")).intValue() - 1) == gdsxdto.getY().intValue() && (stringList == null || stringList.size() == 0)) {
						flag = false;
						map.put("swType", 1);
						map.put("SwDesc", gdsxdto.getDesc());
						list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
					}
				}
				if (flag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("x", gdsxdto.getX() + 1);
					map.put("y", gdsxdto.getY() + 1);
					map.put("SwDesc", gdsxdto.getDesc());
					map.put("isSwAndCourse", 0);
					map.put("swType", 1);
					list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
				}
			}
		}
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(sid.toString(), xqid);//课节
		//生成学科课表
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("学科课表");
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 7));
		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		sheet.setColumnWidth((short) 0, (short) 4500);// 设置列宽
		sheet.setColumnWidth((short) 1, (short) 5500);
		sheet.setColumnWidth((short) 2, (short) 5500);
		sheet.setColumnWidth((short) 3, (short) 5500);
		sheet.setColumnWidth((short) 4, (short) 5500);
		sheet.setColumnWidth((short) 5, (short) 5500);// 空列设置小一些
		sheet.setColumnWidth((short) 6, (short) 5500);// 设置列宽
		sheet.setColumnWidth((short) 7, (short) 5500);

		HSSFCellStyle headerStyle1 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式1
		headerStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    // 字体加粗
		headerFont.setFontHeightInPoints((short) 16);    //设置字体大小
		headerStyle1.setFont(headerFont);    //为标题样式设置字体样式
		headerStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle1.setWrapText(true);

		HSSFCellStyle headerStyle2 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont1 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小
		headerStyle2.setFont(headerFont1);    //为标题样式设置字体样式
		headerStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//headerStyle2.setFillBackgroundColor(HSSFColor.PALE_BLUE.index);
		headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		headerStyle2.setWrapText(true);


		HSSFCellStyle headerStyle3 = (HSSFCellStyle) wb.createCellStyle();// 创建标题样式2
		headerStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headerFont2 = (HSSFFont) wb.createFont();    //创建字体样式
		headerFont2.setFontHeightInPoints((short) 8);    //设置字体大小
		headerStyle3.setFont(headerFont2);    //为标题样式设置字体样式
		headerStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		headerStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		headerStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		headerStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		headerStyle3.setWrapText(true);

		//为sheet1生成第一行，用于放表头信息
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeightInPoints(40);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(headerStyle1);
		cell1.setCellValue(termEntry.getXqName() + "学科课表");

		//为sheet1生成第二行，用于放表头信息
		HSSFRow row = sheet.createRow(1);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("课节 / 日");
		List<String> weekLists = new ArrayList<String>();
		weekLists.add("周一");
		weekLists.add("周二");
		weekLists.add("周三");
		weekLists.add("周四");
		weekLists.add("周五");
		weekLists.add("周六");
		weekLists.add("周日");
		for (int j = 0; j < weekLists.size(); j++) {
			cell = row.createCell(j + 1);
			cell.setCellStyle(headerStyle2);
			cell.setCellValue(weekLists.get(j));
		}

		int page = 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < courseRangeDTOs.size(); i++) {
				page++;
				row = sheet.createRow(page);
				row.setHeightInPoints(50);
				cell = row.createCell(0);
				cell.setCellValue(courseRangeDTOs.get(i).getName() + "\r\n" + courseRangeDTOs.get(i).getStart() + "~" + courseRangeDTOs.get(i).getEnd());
				cell.setCellStyle(headerStyle2);
				for (int j = 0; j < weekLists.size(); j++) {
					cell = row.createCell(j + 1);
					cell.setCellStyle(headerStyle3);
					Map<String, Object> dto = list.get(j + "" + i);
					if (dto != null) {
						String str = "";
						List<String> list1 = (List<String>) dto.get("tsName");
						if (list1 != null && list1.size() > 0) {
							for (String s : list1) {
								str += s + ",\r\n";
							}
						}
						if (dto.get("SwDesc") != null && dto.get("SwDesc") != "") {
							if (str == "") {
								str += "事务：";
							} else {
								str += "\r\n事务：";
							}
							str += (String) dto.get("SwDesc");
						}
						cell.setCellValue(str);
					}
				}
			}
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/force-download");
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("学科课表.xls", "UTF-8"));
			response.setContentLength(content.length);
			outputStream.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public Map getGradeSubjectsKB(ObjectId xqid, String gradeId, ObjectId subjectId, Integer week, ObjectId sid) {
		Map<String, Map<String, Object>> list = new ConcurrentHashMap<String, Map<String, Object>>();
		TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
		for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
			if (paiKeTimes.getIr() == 0) {
				cids.add(paiKeTimes.getID());
			}
		}
		//学科老师
		Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySubjectIdG(cids, subjectId, "*", gradeId);
		List<ObjectId> userIds = new ArrayList<ObjectId>();
		for (N33_TeaEntry teaEntry : teaEntries.values()) {
			userIds.add(teaEntry.getUserId());
		}
		Map<ObjectId, N33_TeaEntry> userMap = teaDao.getTeaMap(userIds, cids);
		List<N33_ZKBEntry> zkbEntries = zkbDao.getN33_ZKBByWeekTT(userIds, week, xqid, gradeId);
		zkbEntries.addAll(zkbDao.getN33_ZKBByWeekNTT(userIds, week, xqid, gradeId));
		//教学班列表 所有次的
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(termEntry.getSid()).getPaikeci();
		int acClassType = turnOffService.getAcClassType(termEntry.getSid(), new ObjectId(gradeId), ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(gradeId, subjectId, cids, userIds,acClassType);
		jxbEntries.addAll(jxbDao.getZXJXBList(gradeId, subjectId, cids,acClassType));
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
		List<ObjectId> ods = new ArrayList<ObjectId>();
		for (N33_ZKBEntry jxbEntry : zkbEntries) {
			if (jxbEntry.getType() == 5) {
				ods.add(jxbEntry.getJxbId());
			}
		}
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			jxbEntryMap.put(jxbEntry.getID(), jxbEntry);
			zxkIds.add(jxbEntry.getID());
		}
		Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(ods);
		List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
		Integer count = week % 2;
		for (N33_ZKBEntry entry : zkbEntries) {
			if(!subjectId.equals(entry.getSubjectId())){
				continue;
			}
			Boolean bf = true;
			if (list.size() > 0) {
				Integer x = Integer.parseInt(String.valueOf(entry.getX() + 1));
				Integer y = Integer.parseInt(String.valueOf(entry.getY() + 1));
				//判断坐标是否存在值
				for (Map<String, Object> lt : list.values()) {
					//同一个坐标
					Integer x1 = (Integer) lt.get("x");
					Integer y1 = (Integer) lt.get("y");
					if (x1 == x && y1 == y) {
						bf = false;
						List<String> tsName = (List<String>) lt.get("tsName");
						if (tsName == null) {
							tsName = new ArrayList<String>();
						}
						String name = "";
						if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
							name += "-" + entry1.getName();
						}
						if (entry.getType() == 5) {
							name = userMap.get(entry.getTeacherId()).getUserName();
							N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
							name += "-" + entry1.getName();
						}
						if (entry.getType() == 6) {
							if (count == 0) {
								name = userMap.get(entry.getNTeacherId()).getUserName();
								N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
								name += "-" + entry1.getName();
							} else {
								name = userMap.get(entry.getTeacherId()).getUserName();
								N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
								name += "-" + entry1.getName();
							}
						}
						if (entry.getType() == 4) {
							for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
								if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
									String nm = userMap.get(entry1.getTeaId()).getUserName();
									N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
									nm += "-" + entryd.getName();
									tsName.add(nm);
								}
							}
						}
						if (entry.getType() != 4) {
							tsName.add(name);
						}
						lt.put("tsName", tsName);
					}
				}
			}
			if (bf) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", entry.getX() + 1);
				map.put("y", entry.getY() + 1);
				map.put("title", "查看授课老师");
				List<String> tsName = new ArrayList<String>();
				String name = "";
				if (entry.getType() != 5 && entry.getType() != 4 && entry.getType() != 6) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
					name += "-" + entry1.getName();
				}
				if (entry.getType() == 5) {
					name = userMap.get(entry.getTeacherId()).getUserName();
					N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
					name += "-" + entry1.getName();
				}
				if (entry.getType() == 6) {
					if (count == 0) {
						name = userMap.get(entry.getNTeacherId()).getUserName();
						N33_JXBEntry entry1 = jxbEntryMap.get(entry.getNJxbId());
						name += "-" + entry1.getName();
					} else {
						name = userMap.get(entry.getTeacherId()).getUserName();
						N33_JXBEntry entry1 = jxbEntryMap.get(entry.getJxbId());
						name += "-" + entry1.getName();
					}
				}
				if (entry.getType() == 4) {
					for (N33_ZhuanXiangEntry entry1 : zhuanXiangEntries) {
						if (entry1.getJxbId().toString().equals(entry.getJxbId().toString())) {
							String nm = userMap.get(entry1.getTeaId()).getUserName();
							N33_JXBEntry entryd = jxbEntryMap.get(entry1.getJxbId());
							nm += "-" + entryd.getName();
							tsName.add(nm);
						}
					}
				}
				if (entry.getType() != 4) {
					tsName.add(name);
				}
				map.put("tsName", tsName);
				map.put("isSwAndCourse", 0);
				map.put("swType", 0);
				list.put(entry.getX() + "" + entry.getY(), map);
			}
		}
		List<N33_SWDTO> swdtos = swService.getGuDingShiWuByXqid(xqid);
		List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(xqid, sid, new ObjectId(gradeId));
		if (list != null) {
			for (N33_SWDTO swdto : swdtos) {
				boolean flag = true;
				for (Map.Entry<String, Map<String, Object>> entry1 : list.entrySet()) {
					Map<String, Object> map = entry1.getValue();
					List<String> stringList = (List<String>) map.get("tsName");
					if (new Integer((Integer) map.get("x")).intValue() == swdto.getY().intValue() && new Integer((Integer) map.get("y")).intValue() == swdto.getX().intValue() && stringList != null && stringList.size() > 0) {
						flag = false;
						map.put("isSwAndCourse", 1);
						map.put("swType", 1);
						if (map.get("SwDesc") != null) {
							String s = (String) map.get("SwDesc");
							s += "\r\n";
							s += swdto.getDesc();
							map.put("SwDesc", s);
							list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
						} else {
							map.put("SwDesc", swdto.getDesc());
							list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
						}
					} else if (new Integer((Integer) map.get("x")).intValue() == swdto.getY().intValue() && new Integer((Integer) map.get("y")).intValue() == swdto.getX().intValue() && (stringList == null || stringList.size() == 0)) {
						flag = false;
						map.put("swType", 1);
						map.put("SwDesc", swdto.getDesc());
						list.put((new Integer((Integer) map.get("x")).intValue() - 1) + "" + (new Integer((Integer) map.get("y")).intValue() - 1), map);
					}
				}
				if (flag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("x", swdto.getY().intValue());
					map.put("y", swdto.getX().intValue());
					map.put("SwDesc", swdto.getDesc());
					map.put("isSwAndCourse", 0);
					map.put("swType", 1);
					list.put((swdto.getY() - 1) + "" + (swdto.getX() - 1), map);
				}
			}
		}

		if (list != null) {
			for (N33_GDSXDTO gdsxdto : gdsxdtos) {
				boolean flag = true;  
				for (Map.Entry<String, Map<String, Object>> entry1 : list.entrySet()) {
					Map<String, Object> map = entry1.getValue();
					List<String> stringList = (List<String>) map.get("tsName");
					if ((new Integer((Integer) map.get("x")).intValue() - 1) == (gdsxdto.getX().intValue()) && (new Integer((Integer) map.get("y")).intValue() - 1) == (gdsxdto.getY().intValue()) && stringList != null && stringList.size() > 0) {
						flag = false;
						map.put("isSwAndCourse", 1);
						map.put("swType", 1);
						if (map.get("SwDesc") != null) {
							String s = (String) map.get("SwDesc");
							s += "\r\n";
							s += gdsxdto.getDesc();
							map.put("SwDesc", s);
							list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
						} else {
							map.put("SwDesc", gdsxdto.getDesc());
							list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
						}
					} else if ((new Integer((Integer) map.get("x")).intValue() - 1) == (gdsxdto.getX().intValue()) && (new Integer((Integer) map.get("y")).intValue() - 1) == (gdsxdto.getY().intValue()) && (stringList == null || stringList.size() == 0)) {
						flag = false;
						map.put("swType", 1);
						map.put("SwDesc", gdsxdto.getDesc());
						list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
					}
				}
				if (flag) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("x", gdsxdto.getX() + 1);
					map.put("y", gdsxdto.getY() + 1);
					map.put("SwDesc", gdsxdto.getDesc());
					map.put("isSwAndCourse", 0);
					map.put("swType", 1);
					list.put(gdsxdto.getX() + "" + gdsxdto.getY(), map);
				}
			}
		}
		Map cMap = new HashMap();
		cMap.put("cid", zkbEntries.get(0).getCId().toString());
		list.put("cid",cMap);
		return list;
//		//课节
//		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolIdZKB(sid.toString(), xqid);
	}

	/**
	 * 老师是否有课
	 * @param teaId
	 * @param sid
	 * @param time
	 * @return
	 */
	public Boolean checkTeachersByTime(ObjectId teaId, ObjectId sid,String date, String time) throws Exception {
		ObjectId xqId = null;
		N33_DefaultTermEntry termEntry = defaultTermDao.findDefaultTermEntryBySchoolId(sid);
		if(termEntry != null){
			xqId = termEntry.getPaikeXqid();
		}else{
			N33_DefaultTermEntry defaultTermEntry = getDefaultPaiKeTerm(sid);
			if(defaultTermEntry!=null){
				xqId = defaultTermEntry.getPaikeXqid();
			}
		}
		int index = 0;
		String start = time.split("-")[0];
		String end = time.split("-")[1];
		N33_JXZEntry entry = jxzDao.getJXZByDate(xqId, sid, DateTimeUtils.getStrToLongTime(date+" "+start,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_H));
		if (entry != null) {
			index = entry.getSerial();
		}
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		int week = DateTimeUtils.getWeekByDate(DateTimeUtils.stringToDate(date,DateTimeUtils.DATE_YYYY_MM_DD));
		long lstart = sf.parse(start).getTime();
		long lend = sf.parse(end).getTime();
		List<CourseRangeEntry> list = dao.getEntryListBySchoolId(sid, xqId);
		List<Integer> days = new ArrayList<Integer>();
		if (list!=null && list.size()!=0) {
			for (CourseRangeEntry cr : list) {
				if ((cr.getStart()<=lstart || lstart<=cr.getEnd()) || (cr.getStart()<=lend || lend<=cr.getEnd())) {
					days.add(cr.getSerial()-1);
				}
			}
		}
		boolean flag = false;
		List<N33_ZKBEntry> entries = zkbDao.getN33_ZKBByWeekAndXY(xqId,week-1,days,sid,index, teaId);
		if (entries!=null && entries.size()!=0) {
			flag = true;
		} else {
			List<N33_SWEntry> sws = swDao.getSwByXqidAndUserId(xqId, teaId);
			for (N33_SWEntry sw : sws) {
				if (sw.getY()==index-1 && days.contains(sw.getX())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}


	/**
	 * 老师课表
	 *
	 * @param teacherId
	 * @param schoolId
	 * @return
	 */
	public Map<String,List<Map<String, Object>>> GetTeachersSettledPositionsByWeek2(Map mapData) {
		Map<String,List<Map<String, Object>>> teaMaps = new HashMap<String, List<Map<String, Object>>>();
		ObjectId schoolId = new ObjectId(mapData.get("schoolId").toString());
		List<ObjectId> teacherIds =  MongoUtils.convertToObjectIdList((List<String>)mapData.get("teaIds"));
		int week = Integer.valueOf(mapData.get("week").toString());
		ObjectId xqid = getDefauleTermId(schoolId);
		List<Map<String, Object>> lists = termService.getPaikeTimesByTermId(xqid);

		List<ObjectId> cids = new ArrayList<ObjectId>();
		for (Map<String, Object> lt : lists) {
			String cid = (String) lt.get("ciId");
			cids.add(new ObjectId(cid));
		}
		for (ObjectId teacherId : teacherIds) {
			//原课表
			List<N33_ZKBEntry> entries = zkbDao.getJXB(xqid, teacherId, week);
			entries.addAll(zkbDao.getJXB(xqid, week));
			entries.addAll(zkbDao.getJXBN(xqid, teacherId, week));
			//学科列表 所有次的
			List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(cids, schoolId);

			//该次排课的所有年级
			Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(cids, schoolId);

			//教学班列表 所有次的
			List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(cids, teacherId);
			jxbEntries.addAll(jxbDao.getZXJXBList(cids));
			//教室列表 所有次的
			List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, cids);
			//专项课Ids
			List<ObjectId> zxkIds = new ArrayList<ObjectId>();
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				zxkIds.add(jxbEntry.getID());
			}
			List<ObjectId> ods = new ArrayList<ObjectId>();
			for (N33_ZKBEntry jxbEntry : entries) {
				if (jxbEntry.getType() == 5) {
					ods.add(jxbEntry.getJxbId());
				}
			}
			Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(ods);
			List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
			Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
			List<ObjectId> jxbIds = new ArrayList<ObjectId>();
			for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
				if (!jxbIds.contains(entry.getJxbId())) {
					jxbIds.add(entry.getJxbId());
				}
			}
			for (ObjectId objectId : jxbIds) {
				List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
				for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
					if (entry.getJxbId().toString().equals(objectId.toString())) {
						jis.add(entry);
					}
				}
				listMap.put(objectId, jis);
			}
			//封装后的
			Map<ObjectId, List<N33_ClassroomEntry>> classRoomList = new HashMap<ObjectId, List<N33_ClassroomEntry>>();
			Map<ObjectId, List<N33_KSDTO>> gradeSubjectCidMap = new HashMap<ObjectId, List<N33_KSDTO>>();
			Map<ObjectId, Map<ObjectId, N33_JXBEntry>> jxbEntryMap = new HashMap<ObjectId, Map<ObjectId, N33_JXBEntry>>();
			for (Map<String, Object> lt : lists) {
				String cid = (String) lt.get("ciId");
//        String cid="5ad859cf8fb25af5a4779221";
				//学科
				List<N33_KSDTO> subjectCid = new ArrayList<N33_KSDTO>();
				for (N33_KSDTO ksdto : gradeSubject) {
					if (ksdto.getXqid().equals(cid)) {
						subjectCid.add(ksdto);
					}
				}
				gradeSubjectCidMap.put(new ObjectId(cid), subjectCid);
				// 教学班
				Map<ObjectId, N33_JXBEntry> jxbEntryMap1 = new HashMap<ObjectId, N33_JXBEntry>();
				for (N33_JXBEntry jxbEntry : jxbEntries) {
					if (jxbEntry.getTermId().toString().equals(cid)) {
						jxbEntryMap1.put(jxbEntry.getID(), jxbEntry);
					}
				}
				jxbEntryMap.put(new ObjectId(cid), jxbEntryMap1);
				//教室
				List<N33_ClassroomEntry> jsList = new ArrayList<N33_ClassroomEntry>();
				for (N33_ClassroomEntry js : classRoomDTO) {
					if (js.getXQId().toString().equals(cid)) {
						jsList.add(js);
					}
				}
				classRoomList.put(new ObjectId(cid), jsList);
			}
			//返回数据
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ObjectId ciId = null;
			if(entries.size() > 0){
				ciId = entries.get(0).getCId();
			}else{
				teaMaps.put(teacherId.toString(),list);
				continue;
			}
			List<CourseRangeEntry> courseRangeEntryList = dao.getEntryListBySchoolId(schoolId, ciId);
			for (N33_ZKBEntry entry : entries) {
				Integer count = week % 2;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", entry.getX() + 1);
				map.put("y", entry.getY() + 1);
				map.put("cid",entry.getCId().toString());
				for (CourseRangeEntry courseRangeEntry : courseRangeEntryList) {
					if(courseRangeEntry.getSerial() == (entry.getY() + 1)){
						map.put("start",courseRangeEntry.getStart());
						map.put("end",courseRangeEntry.getEnd());
					}
				}
				String gradeName = "";
				if (entry.getGradeId() != null) {
					gradeName = gradeMap.get(entry.getGradeId()).getName() + "（" + gradeMap.get(entry.getGradeId()).getJie() + "）";
				}
				map.put("grade", gradeName);
				List<N33_KSDTO> dto = gradeSubjectCidMap.get(entry.getCId());
				if(dto == null){
					teaMaps.put(teacherId.toString(),list);
					continue;
				}
				if (entry.getType() != 5) {
					for (N33_KSDTO ksdto : dto) {
						//获取学科
						if(count == 0 && entry.getType() == 6){
							if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								map.put("subName", ksdto.getSnm());
								break;
							}
						}else{
							if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								map.put("subName", ksdto.getSnm());
								break;
							}
						}
					}
				}
				if (entry.getType() != 4 && entry.getType() != 5) {
					//教学班
					Map<ObjectId, N33_JXBEntry> jxbEntryMap2 = jxbEntryMap.get(entry.getCId());
					N33_JXBEntry entry1 = null;
					if (count == 0) {
						entry1 = jxbEntryMap2.get(entry.getNJxbId());
					} else {
						entry1 = jxbEntryMap2.get(entry.getJxbId());
					}
					if (entry1 == null) {
						entry1 = jxbDao.getJXBById(entry.getJxbId());
					}
					if(entry1 == null){
						continue;
					}
					if (StringUtils.isNotEmpty(entry1.getNickName())) {
						map.put("JxbName", entry1.getNickName());
					} else {
						map.put("JxbName", entry1.getName());
					}
					map.put("count", entry1.getStudentIds().size());
					//教室
					List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
					for (N33_ClassroomEntry room : classRoomDTOList) {
						//获取学科
						if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
							map.put("roomName", room.getRoomName());
							break;
						}
					}
					if (entry.getNTeacherId() != null) {
						if (count == 0) {
							map.put("jxbId", entry.getNJxbId().toString());
							if (entry.getNTeacherId().toString().equals(teacherId.toString())) {
								list.add(map);
							}
						} else {
							if (entry.getTeacherId().toString().equals(teacherId.toString())) {
								list.add(map);
							}
						}
					} else {
						map.put("jxbId", entry.getJxbId().toString());
						list.add(map);
					}
				} else {
					if (entry.getType() == 4) {
						List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
						if (zhuanList != null) {
							for (N33_ZhuanXiangEntry entry1 : zhuanList) {
								if (entry1.getTeaId().toString().equals(teacherId.toString())) {
									map.put("jxbId", entry1.getJxbId().toString());
									map.put("JxbName", entry1.getName());
									map.put("count", entry1.getStudentId().size());
									List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
									for (N33_ClassroomEntry room : classRoomDTOList) {
										//获取学科
										if (room.getRoomId().toString().equals(entry1.getRoomId().toString())) {
											map.put("roomName", room.getRoomName());
											break;
										}
									}
									list.add(map);
								}
							}
						}
					} else {
						N33_ZIXIKEEntry entry1 = zixikeEntries.get(entry.getJxbId());
						if (entry1 != null) {
							map.put("JxbName", entry1.getName());
							map.put("count", entry1.getStudentIds().size());
							map.put("subName", "自习课");
							List<N33_ClassroomEntry> classRoomDTOList = classRoomList.get(entry.getCId());
							for (N33_ClassroomEntry room : classRoomDTOList) {
								//获取学科
								if (room.getRoomId().toString().equals(entry1.getClassroomId().toString())) {
									map.put("roomName", room.getRoomName());
									break;
								}
							}
							list.add(map);
						}
					}
				}
			}
			teaMaps.put(teacherId.toString(),list);
		}

		return teaMaps;
	}
}
