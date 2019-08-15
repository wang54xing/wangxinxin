package com.fulaan.new33.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.N33_GradeWeekRangeDao;
import com.db.new33.N33_TimeCombineDao;
import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_ClassroomDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.SubjectDao;
import com.db.new33.paike.N33_JXBCTDao;
import com.db.new33.paike.N33_JXBCombineDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_JXBKSDao;
import com.db.new33.paike.N33_PaiKeZuHeDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.db.new33.paike.N33_YKBDao;
import com.fulaan.new33.dto.N33_PaiKeZuHeDTO;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.new33.paike.N33_JXBCTEntry;
import com.pojo.new33.paike.N33_JXBCombineEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_JXBKSEntry;
import com.pojo.new33.paike.N33_PaiKeZuHeEntry;
import com.pojo.new33.paike.N33_TimeCombineEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * Created by albin on 2018/7/11.
 */
@Service
public class N33_PaiKeZuHeService extends BaseService{
	private N33_PaiKeZuHeDao zuHeDao = new N33_PaiKeZuHeDao();
	private N33_JXBDao jxbDao = new N33_JXBDao();
	private N33_TeaDao teaDao = new N33_TeaDao();
	private N33_AutoPkService autoPkService = new N33_AutoPkService();
	private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();
	private SubjectDao subjectDao = new SubjectDao();
	private ClassDao classDao = new ClassDao();
	private N33_StudentDao studentDao = new N33_StudentDao();
	private GradeDao gradeDao = new GradeDao();
	private N33_ClassroomDao classroomDao = new N33_ClassroomDao();
	private N33_JXBCombineDao jxbCombineDao = new N33_JXBCombineDao();
	private N33_GradeWeekRangeDao n33_gradeWeekRangeDao = new N33_GradeWeekRangeDao();
	private IsolateSubjectService subjectService = new IsolateSubjectService();
	private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();
	private N33_YKBDao ykbDao = new N33_YKBDao();

	private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();
	@Autowired
	private PaiKeService paiKeService;

	private N33_TimeCombineDao timeCombineDao = new N33_TimeCombineDao();
	
	@Autowired
    private N33_TurnOffService turnOffService;

	public void addEntry(N33_PaiKeZuHeDTO dto) {
		N33_PaiKeZuHeEntry entry = dto.buildEntry();
		zuHeDao.addEntry(entry);
	}

	public void updateEntry(Map<String, Object> map) {
		String id = (String) map.get("id");
		List<String> jxbIds = (List<String>) map.get("jxbIds");
		N33_PaiKeZuHeEntry entry = zuHeDao.findEntryById(new ObjectId(id));
		List<ObjectId> jxbList = new ArrayList<ObjectId>();
		for (ObjectId jxbId : entry.getJxbIds()) {
			if (!jxbList.contains(jxbId)) {
				jxbList.add(jxbId);
			}
		}
		for (String jxbId : jxbIds) {
			if (!jxbList.contains(new ObjectId(jxbId))) {
				jxbList.add(new ObjectId(jxbId));
			}
		}
		entry.setJxbIds(jxbList);
		zuHeDao.update(entry);
	}

	public void delEntry(ObjectId id) {
		zuHeDao.delete(id);
	}

	public void delJxbById(ObjectId id, ObjectId jxbId) {
		N33_PaiKeZuHeEntry entry = zuHeDao.findEntryById(id);
		List<ObjectId> jxbList = entry.getJxbIds();
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		if (jxbEntry.getType() == 6) {
			jxbList.remove(jxbEntry.getRelativeId());
		}
		jxbList.remove(jxbId);
		entry.setJxbIds(jxbList);
		zuHeDao.update(entry);
	}

	/**
	 * 初始化等级和合格类型的排课组合
	 */
	public void initZuHeList(ObjectId ciId,ObjectId sid,ObjectId gradeId,Integer type)  {
		List<N33_TimeCombineEntry> timeCombineEntryList = timeCombineDao.getTimeCombineByType(ciId,sid,gradeId,type);
		List<N33_PaiKeZuHeEntry> entryList = zuHeDao.getEntryList(gradeId, type, ciId);
		List<N33_PaiKeZuHeEntry> addPaiKeZuHeEntry = new ArrayList<N33_PaiKeZuHeEntry>();
		if(timeCombineEntryList.size() > 0){
			N33_TimeCombineEntry timeCombineEntry = timeCombineEntryList.get(0);
			List<N33_TimeCombineEntry.ZuHeList> zuHeLists = timeCombineEntry.getZuHeList();
			Collections.sort(zuHeLists, new Comparator<N33_TimeCombineEntry.ZuHeList>() {
				@Override
				public int compare(N33_TimeCombineEntry.ZuHeList o1, N33_TimeCombineEntry.ZuHeList o2) {
					return o1.getSerial() - o2.getSerial();
				}
			});
			for (N33_TimeCombineEntry.ZuHeList zuHeList : zuHeLists) {
				String name = "";
				if(type == 1){
					name += ("等级-时段组合" + zuHeList.getSerial());
				}else{
					name += ("合格-时段组合" + zuHeList.getSerial());
				}
				//标识这个时段组合是否已经存在组合表中
				boolean flag = true;
				for (N33_PaiKeZuHeEntry zuHeEntry : entryList) {
					if(name.equals(zuHeEntry.getName())){
						List<ObjectId> jxbIds = new ArrayList<ObjectId>();
						for (N33_TimeCombineEntry timeCombineEntry1 : timeCombineEntryList) {
							for (N33_TimeCombineEntry.ZuHeList zuHeList1 : timeCombineEntry1.getZuHeList()) {
								if(zuHeList1.getSerial() == zuHeList.getSerial() && zuHeList1.getJxbId() != null && !"".equals(zuHeList1.getJxbId().toString()) && !jxbIds.contains(zuHeList1.getJxbId())){
									jxbIds.add(zuHeList1.getJxbId());
								}
							}
						}
						zuHeEntry.setJxbIds(jxbIds);
						zuHeDao.update(zuHeEntry);
						flag = false;
					}
				}
				if(flag){
					List<ObjectId> jxbIds = new ArrayList<ObjectId>();
					for (N33_TimeCombineEntry timeCombineEntry1 : timeCombineEntryList) {
						for (N33_TimeCombineEntry.ZuHeList zuHeList1 : timeCombineEntry1.getZuHeList()) {
							if(zuHeList1.getSerial() == zuHeList.getSerial() && zuHeList1.getJxbId() != null && !"".equals(zuHeList1.getJxbId().toString()) && !jxbIds.contains(zuHeList1.getJxbId())){
								jxbIds.add(zuHeList1.getJxbId());
							}
						}
					}
					N33_PaiKeZuHeEntry entry = new N33_PaiKeZuHeEntry(name,sid,ciId,jxbIds,type,gradeId);
					addPaiKeZuHeEntry.add(entry);
				}
			}
			if(addPaiKeZuHeEntry.size() > 0){
				zuHeDao.addEntrys(addPaiKeZuHeEntry);
			}
		}else{
			zuHeDao.delete(gradeId,type,ciId);
		}

	}

	public List<N33_PaiKeZuHeDTO> getPaiKeZuHeList(ObjectId sid, ObjectId gradeId, ObjectId ciId, Integer type) {
		List<N33_PaiKeZuHeEntry> entryList = zuHeDao.getEntryList(gradeId, type, ciId);
		List<N33_PaiKeZuHeDTO> dtos = new ArrayList<N33_PaiKeZuHeDTO>();
		// jxb add 
       // ObjectId cisId = getDefaultPaiKeTerm(sid).getPaikeci();
		int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        // jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntryMap1 = jxbDao.getJXBMap(sid, gradeId, ciId, acClassType);

		List<ObjectId> zuHeJxbIds = new ArrayList<ObjectId>();
		for (N33_PaiKeZuHeEntry entry : entryList) {
			List<ObjectId> jxbIds = new ArrayList<ObjectId>();
			List<ObjectId> danshuangJxbIds = new ArrayList<ObjectId>();
			jxbIds.addAll(entry.getJxbIds());
			for (ObjectId jxbIds1 : jxbIds) {
				if (jxbEntryMap1.get(jxbIds1).getType() == 6) {
					danshuangJxbIds.add(jxbEntryMap1.get(jxbIds1).getRelativeId());
				}
			}
			jxbIds.addAll(danshuangJxbIds);
			zuHeJxbIds.addAll(jxbIds);
		}
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapsByIds(zuHeJxbIds);
		List<ObjectId> teaIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntryMap.values()) {
			teaIds.add(jxbEntry.getTercherId());
		}
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teaIds, ciId);
		for (N33_PaiKeZuHeEntry entry : entryList) {
			N33_PaiKeZuHeDTO dto = new N33_PaiKeZuHeDTO(entry);
			List<Map<String, Object>> resulte = new ArrayList<Map<String, Object>>();
			List<ObjectId> zuHeJXBIds = entry.getJxbIds();
			//将对应的单双周的课加入到组合教学班中
			List<ObjectId> addJxbList = new ArrayList<ObjectId>();
			for (ObjectId ids : zuHeJXBIds) {
				if (jxbEntryMap1.get(ids).getType() == 6) {
					addJxbList.add(jxbEntryMap1.get(ids).getRelativeId());
				}
			}
			zuHeJXBIds.addAll(addJxbList);
			dto.setJxbIds(MongoUtils.convertToStringList(zuHeJXBIds));

			Collections.sort(zuHeJXBIds, new Comparator<ObjectId>() {
				@Override
				public int compare(ObjectId o1, ObjectId o2) {
					return o2.hashCode() - o1.hashCode();
				}
			});

			for (ObjectId jxbId : zuHeJXBIds) {
				Map<String, Object> dt = new HashMap<String, Object>();
				dt.put("id", jxbId.toString());
				dt.put("zuHeId", entry.getID().toString());
				N33_JXBEntry jxbEntry = jxbEntryMap.get(jxbId);
				dt.put("teaId", jxbEntry.getTercherId() == null ? "" : jxbEntry.getTercherId().toString());
				dt.put("jxbName", jxbEntry.getName());
				N33_TeaEntry teaEntry = teaEntryMap.get(jxbEntry.getTercherId());
				dt.put("TeaName", teaEntry == null ? "" : teaEntry.getUserName());
				resulte.add(dt);
			}
			dto.setJxbList(resulte);
			dtos.add(dto);
		}
		return dtos;
	}

	public List<N33_JXBDTO> getBuFenCTJXB(ObjectId jxbId, ObjectId ciId, ObjectId sid, ObjectId gradeId) {
		List<N33_JXBDTO> retList = new ArrayList<N33_JXBDTO>();
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		Map<ObjectId,N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomMapByXqGradeMap(ciId,gradeId);
		Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(ciId,sid,gradeId);
		//jxb add
		int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(sid, gradeId, ciId, acClassType);
		for (N33_JXBEntry n33_jxbEntry : jxbEntryList) {
			List<ObjectId> replaceStuIds1 = new ArrayList<ObjectId>();
			List<ObjectId> replaceStuIds2 = new ArrayList<ObjectId>();
			List<ObjectId> stuIds = n33_jxbEntry.getStudentIds();
			for (ObjectId stuId : stuIds) {
				replaceStuIds1.add(stuId);
			}
			replaceStuIds1.retainAll(jxbEntry.getStudentIds());
			for (ObjectId stuId : jxbEntry.getStudentIds()) {
				replaceStuIds2.add(stuId);
			}
			replaceStuIds2.retainAll(n33_jxbEntry.getStudentIds());
			if ((replaceStuIds1.size() != n33_jxbEntry.getStudentIds().size() && replaceStuIds1.size() > 0) || (replaceStuIds2.size() > 0 && replaceStuIds2.size() != jxbEntry.getStudentIds().size())) {
				N33_JXBDTO dto = new N33_JXBDTO(n33_jxbEntry);
				dto.setClassroomName(classroomEntryMap.get(n33_jxbEntry.getClassroomId()) == null ? "" : classroomEntryMap.get(n33_jxbEntry.getClassroomId()).getRoomName());
				dto.setTeacherName(teaEntryMap.get(n33_jxbEntry.getTercherId()) == null ? "" : teaEntryMap.get(n33_jxbEntry.getTercherId()).getUserName());
				retList.add(dto);
			}
		}
		return retList;
	}


	/**
	 * 查找存在传入教学班的组合并且在组合中相交教学班找出是否存在无冲突的教学班
	 *
	 * @param jxbId
	 * @param name
	 * @param ciId
	 * @param sid
	 * @param gradeId
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> getZuHeNoCTJXB(ObjectId jxbId, String name, ObjectId ciId, ObjectId
			sid, ObjectId gradeId, Integer type) {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();

		List<N33_JXBCombineEntry> jxbCombineEntries = jxbCombineDao.getN33_JXBCombineEntry(ciId, sid, gradeId, type, jxbId);
		flag:
		for (N33_JXBCombineEntry jxbCombineEntry : jxbCombineEntries) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			List<N33_JXBCombineEntry.JxbList> jxbLists = jxbCombineEntry.getJxbList();
			for (N33_JXBCombineEntry.JxbList jxbList : jxbLists) {
				List<N33_JXBCombineEntry.IntersectJXB> intersectJXBS = jxbList.getIntersectJXB();
				for (N33_JXBCombineEntry.IntersectJXB intersectJXB : intersectJXBS) {
					if (intersectJXB.getIntersectJxbId().toString().equals(jxbId.toString())) {
						List<N33_JXBCombineEntry.IsNoCTJXB> isNoCTJXBS = intersectJXB.getIsNoCTJXB();
						List<String> wuCTJXBIds = new ArrayList<String>();
						if (isNoCTJXBS == null || isNoCTJXBS.size() == 0) {
							continue flag;
						} else {
							for (N33_JXBCombineEntry.IsNoCTJXB isNoCTJXB : isNoCTJXBS) {
								wuCTJXBIds.add(isNoCTJXB.getoJxbId().toString());
							}
							retMap.put("name", jxbCombineEntry.getName());
							retMap.put("jxbList", wuCTJXBIds);
							retList.add(retMap);
							continue flag;
						}
					}
				}
			}
		}
		return retList;
	}

	/**
	 * 查找和传入教学班均无冲突的教学班
	 *
	 * @param
	 * @return
	 */
	public List<String> getNoCTJXB(Map map) {

		List<String> jxbId = (List<String>) map.get("jxbIds");
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (String id : jxbId) {
			jxbIds.add(new ObjectId(id));
		}

		ObjectId schoolId = new ObjectId((String) map.get("schoolId"));
		ObjectId ciId = new ObjectId((String) map.get("ciId"));
		ObjectId gradeId = new ObjectId((String) map.get("gradeId"));
		String types = (String) map.get("type");
		Integer type = Integer.parseInt(types);
		List<String> retList = new ArrayList<String>();
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(schoolId, gradeId, ciId, type,acClassType);
		if (type == 3) {
			jxbEntries.addAll(jxbDao.getJXBList(schoolId, gradeId, ciId, 6,acClassType));
		}
		List<ObjectId> allJXBIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			allJXBIds.add(jxbEntry.getID());
		}

		//教学班冲突
		List<N33_JXBCTEntry> jxbctEntryList = jxbctDao.getJXBCTEntrysByJXBs(schoolId, allJXBIds, ciId);

		Map<ObjectId, List<ObjectId>> jxbCTMap = autoPkService.getCTJXBIds(jxbEntries, jxbctEntryList);

		//所有与传入教学班冲突的教学班
		List<ObjectId> allCTJXBIds = new ArrayList<ObjectId>();
		for (ObjectId id : jxbIds) {
			if (!allCTJXBIds.contains(id)) {
				allCTJXBIds.add(id);
			}
			List<ObjectId> ctJXBIds = jxbCTMap.get(id);
			for (ObjectId ids : allJXBIds) {
				if (ctJXBIds.contains(ids) && !allCTJXBIds.contains(ids)) {
					allCTJXBIds.add(ids);
				}
			}
		}
		for (ObjectId id : allJXBIds) {
			if (!allCTJXBIds.contains(id)) {
				retList.add(id.toString());
			}
		}
		return retList;
	}

	/**
	 * 查询教学班组合
	 *
	 * @param ciId
	 * @param sid
	 * @param gradeId
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> getJXBList(ObjectId ciId, ObjectId sid, ObjectId gradeId, Integer type) {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		//jxb add
		int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
		//jxb add
		//学科
		List<N33_KSEntry> ksEn = subjectDao.getIsolateSubjectEntryByXqid(ciId, sid, gradeId);
		List<ObjectId> subIds = new ArrayList<ObjectId>();
		for (N33_KSEntry entry : ksEn) {
			subIds.add(entry.getSubjectId());
		}
		//所有对应走班类型的教学班
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gradeId, subIds, ciId, type,acClassType);
		if (type == 3) {
			jxbEntries.addAll(jxbDao.getJXBList(sid, gradeId, subIds, ciId, 6,acClassType));
		}

		List<ObjectId> allJXBIds = new ArrayList<ObjectId>();
		List<ObjectId> JXBIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			allJXBIds.add(jxbEntry.getID());
			JXBIds.add(jxbEntry.getID());
		}

		//对应次的老师
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(ciId, sid);

		Map<ObjectId, N33_JXBEntry> jxbEntryMap = getJXBMap(jxbEntries);
		Map<ObjectId, N33_JXBDTO> jxbdtoMap = getJXBDTOMap(jxbEntries, teaEntryMap);

		//对应次对应年级所有的行政班
		Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(gradeId, ciId);

		//对应次对应年级的学生
		Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidAndGradeIdMap(gradeId, ciId);

		//年级名称
		String gradeName = gradeDao.findIsolateGradeEntryByGradeId(gradeId, ciId).getName();

		Integer count = 0;
		//如果是找到单独的一个教学班没有完全冲突，但是与这个单独的教学班的所有部分冲突教学班中又存在完全冲突的教学班，则先找完全冲突的教学班
		ObjectId jxbId = null;
		bufenFlag:
		while (true) {
			Map<String, Object> zuHeMap = new HashMap<String, Object>();
			List<N33_JXBDTO> jxbList = new ArrayList<N33_JXBDTO>();
			if (allJXBIds.size() > 0) {
				if (jxbId == null) {
					jxbId = allJXBIds.get(0);
				}
				//完全冲突的教学班List放在一个组合中
				List<ObjectId> completeCTJXBIds = new ArrayList<ObjectId>();
				//等待第一次循环移除的教学班集合
				List<ObjectId> waitRemoveJXBIds = new ArrayList<ObjectId>();
				jxbList.add(jxbdtoMap.get(jxbId));
				completeCTJXBIds.add(jxbId);
				allJXBIds.remove(jxbId);
				waitRemoveJXBIds.add(jxbId);
				for (ObjectId id : allJXBIds) {
					List<ObjectId> stuIds = new ArrayList<ObjectId>();
					for (ObjectId stuId : jxbEntryMap.get(id).getStudentIds()) {
						stuIds.add(stuId);
					}

					if (jxbEntryMap.get(jxbId).getStudentIds().size() == jxbEntryMap.get(id).getStudentIds().size()) {
						stuIds.retainAll(jxbEntryMap.get(jxbId).getStudentIds());
						if (stuIds.size() == jxbEntryMap.get(jxbId).getStudentIds().size()) {
							jxbList.add(jxbdtoMap.get(id));
							completeCTJXBIds.add(id);
							waitRemoveJXBIds.add(id);
						}
					}
				}
				jxbId = null;
				for (ObjectId completeCTJXBId : completeCTJXBIds) {
					List<ObjectId> buFenCTJXBIds = new ArrayList<ObjectId>();
					for (ObjectId ids : JXBIds) {
						List<ObjectId> stuIds = new ArrayList<ObjectId>();
						for (ObjectId stuId : jxbEntryMap.get(ids).getStudentIds()) {
							stuIds.add(stuId);
						}

						stuIds.retainAll(jxbEntryMap.get(completeCTJXBId).getStudentIds());

						List<ObjectId> stuIds2 = new ArrayList<ObjectId>();
						for (ObjectId id : jxbEntryMap.get(completeCTJXBId).getStudentIds()) {
							stuIds2.add(id);
						}
						stuIds2.retainAll(jxbEntryMap.get(ids).getStudentIds());

						if ((stuIds.size() != jxbEntryMap.get(ids).getStudentIds().size() && stuIds.size() > 0) || (stuIds2.size() > 0 && stuIds2.size() != jxbEntryMap.get(completeCTJXBId).getStudentIds().size())) {
							//去重
							boolean flag = true;
							for (N33_JXBDTO dto : jxbList) {
								if (dto.getId().equals(ids.toString())) {
									flag = false;
								}
							}
							if (flag) {
								jxbList.add(jxbdtoMap.get(ids));
							}
							buFenCTJXBIds.add(ids);
							waitRemoveJXBIds.add(ids);
						}
					}
					//如果一个组合中完全冲突的教学班数量为1，部分冲突的教学班中存在完全冲突的教学班
					if (completeCTJXBIds.size() == 1) {
						for (ObjectId ids1 : buFenCTJXBIds) {
							for (ObjectId ids2 : buFenCTJXBIds) {
								List<ObjectId> stuIds = new ArrayList<ObjectId>();
								for (ObjectId stuId : jxbEntryMap.get(ids1).getStudentIds()) {
									stuIds.add(stuId);
								}
								if (!ids1.toString().equals(ids2.toString()) && jxbEntryMap.get(ids1).getStudentIds().size() == jxbEntryMap.get(ids2).getStudentIds().size()) {
									stuIds.retainAll(jxbEntryMap.get(ids2).getStudentIds());
									if (stuIds.size() == jxbEntryMap.get(ids2).getStudentIds().size()) {
										jxbId = ids1;
										continue bufenFlag;
									}
								}
							}
						}
					}
				}
				count++;
				if (type == 1) {
					zuHeMap.put("name", "组合D" + count);
				} else if (type == 2) {
					zuHeMap.put("name", "组合H" + count);
				} else {
					if (jxbList.get(0).getStudentIds() != null && jxbList.get(0).getStudentIds().size() > 0) {
						if (studentEntryMap.get(new ObjectId(jxbList.get(0).getStudentIds().get(0))) == null) {
							continue;
						}
						zuHeMap.put("name", gradeName + "（" + classEntryMap.get(studentEntryMap.get(new ObjectId(jxbList.get(0).getStudentIds().get(0))).getClassId()).getXh() + "）班");
					} else {
						zuHeMap.put("name", "组合");
					}
				}
				allJXBIds.removeAll(waitRemoveJXBIds);
				zuHeMap.put("jxbList", jxbList);
			} else {
				break;
			}
			retList.add(zuHeMap);
		}

		for (Map<String, Object> map : retList) {
			List<N33_JXBDTO> jxbdtoList = (List<N33_JXBDTO>) map.get("jxbList");
			for (N33_JXBDTO dto : jxbdtoList) {
				for (Map<String, Object> map1 : retList) {
					if (map.get("name") != null) {
						if (((String) map.get("name")).equals(((String) map1.get("name")))) {
							continue;
						}
						List<N33_JXBDTO> jxbdtoList1 = (List<N33_JXBDTO>) map1.get("jxbList");
						for (N33_JXBDTO dto1 : jxbdtoList1) {
							if (dto.getId().equals(dto1.getId())) {
								dto.setZuHeRepeatFlag(true);
							}
						}
					}

				}
			}
		}
		return retList;
	}

	/**
	 * 导出组合
	 *
	 * @param ciId
	 * @param sid
	 * @param gradeId
	 * @param type
	 * @param response
	 */
	public void exportZuHe(ObjectId ciId, ObjectId sid, ObjectId gradeId, Integer type, String count2, String
			count1, HttpServletResponse response) {
		List<List<Map<String, Object>>> retList = new ArrayList<List<Map<String, Object>>>();
		retList = getJXBZuHeList(ciId, sid, gradeId, type, 1);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("组合");
		sheet.setVerticallyCenter(true);
		sheet.setDisplayGridlines(false);

		HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中

		//设置列宽
		for (int i = 0; i < 8; i++) {
			if (i == 2) {
				sheet.setColumnWidth((short) i, (short) 6500);// 设置列宽
			} else {
				sheet.setColumnWidth((short) i, (short) 5500);// 设置列宽
			}
		}

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


		HSSFRow row = sheet.createRow(0);
		row.setHeightInPoints(35);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("教学班组合");
		sheet.addMergedRegion(new Region(0, (short) 0, 1, (short) 0));
		cell = row.createCell(1);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("学生来源");
		sheet.addMergedRegion(new Region(0, (short) 1, 1, (short) 1));
		cell = row.createCell(2);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("教学班信息");
		sheet.addMergedRegion(new Region(0, (short) 2, 0, (short) 4));
		cell = row.createCell(5);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("相交教学班信息");
		sheet.addMergedRegion(new Region(0, (short) 5, 0, (short) 7));
		row = sheet.createRow(1);
		cell = row.createCell(1);
		cell.setCellStyle(headerStyle2);
		row.setHeightInPoints(35);
		cell = row.createCell(2);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("教学班" + count2);
		cell = row.createCell(3);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("教室");
		cell = row.createCell(4);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("教师");
		cell = row.createCell(5);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("教学班" + count1);
		cell = row.createCell(6);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("教室");
		cell = row.createCell(7);
		cell.setCellStyle(headerStyle2);
		cell.setCellValue("教师");
		Integer row1 = 2;
		for (List<Map<String, Object>> list : retList) {
			int count = 8 - list.size();
			row = sheet.createRow(row1);
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					cell = row.createCell(i);
					cell.setCellStyle(headerStyle3);
				}
			}
			row.setHeightInPoints(25);
			for (Map<String, Object> map : list) {
				cell = row.createCell(count);
				if ((Integer) map.get("rowspan") != null) {
					sheet.addMergedRegion(new Region(row1, (short) count, row1 + ((Integer) map.get("rowspan") - 1), (short) count));
				}
				if (count == 1) {
					String name = map.get("name").toString();
				}
				cell.setCellValue(map.get("name").toString());
				cell.setCellStyle(headerStyle3);
				count++;
			}
			row1++;
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
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("教学班组合.xls", "UTF-8"));
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
	 * 保存教学班组合
	 *
	 * @param ciId
	 * @param sid
	 * @param gradeId
	 * @param type
	 */
	public void saveJXBZuHeList(ObjectId ciId, ObjectId sid, ObjectId gradeId, Integer type) {
		jxbCombineDao.removeN33_JXBCombine(ciId, sid, gradeId, type);


		List<N33_JXBCombineEntry> saveList = new ArrayList<N33_JXBCombineEntry>();
		//学科
		List<N33_KSEntry> ksEn = subjectDao.getIsolateSubjectEntryByXqid(ciId, sid, gradeId);
		List<ObjectId> subIds = new ArrayList<ObjectId>();
		for (N33_KSEntry entry : ksEn) {
			subIds.add(entry.getSubjectId());
		}
		//jxb add
		int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
		//jxb add
		//所有对应走班类型的教学班
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gradeId, subIds, ciId, type,acClassType);
		if (type == 3) {
			jxbEntries.addAll(jxbDao.getJXBList(sid, gradeId, subIds, ciId, 6,acClassType));
		}

		List<ObjectId> allJXBIds = new ArrayList<ObjectId>();
		List<ObjectId> JXBIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			allJXBIds.add(jxbEntry.getID());
			JXBIds.add(jxbEntry.getID());
		}
		//对应次的老师
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(ciId, sid);
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = getJXBMap(jxbEntries);

		//对应次对应年级的学生
		Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidAndGradeIdMap(gradeId, ciId);

		//对应次对应年级所有的行政班
		Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(gradeId, ciId);

		//年级名称
		String gradeName = gradeDao.findIsolateGradeEntryByGradeId(gradeId, ciId).getName();
		//对应次的教室
		Map<ObjectId, N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomEntryListByXqGradeForMap(ciId, sid);


		Integer count = 0;
		//如果是找到单独的一个教学班没有完全冲突，但是与这个单独的教学班的所有部分冲突教学班中又存在完全冲突的教学班，则先找完全冲突的教学班
		ObjectId jxbId = null;
		bufenFlag:
		while (true) {
			N33_JXBCombineEntry jxbCombineEntry = new N33_JXBCombineEntry();
			jxbCombineEntry.setGradeId(gradeId);
			jxbCombineEntry.setSchoolId(sid);
			jxbCombineEntry.setXqId(ciId);
			jxbCombineEntry.setType(type);
			if (allJXBIds.size() > 0) {
				if (jxbId == null) {
					jxbId = allJXBIds.get(0);
				}
				//完全冲突的教学班List放在一个组合中
				List<ObjectId> completeCTJXBIds = new ArrayList<ObjectId>();
				//等待第一次循环移除的教学班集合
				List<ObjectId> waitRemoveJXBIds = new ArrayList<ObjectId>();
				completeCTJXBIds.add(jxbId);
				allJXBIds.remove(jxbId);
				waitRemoveJXBIds.add(jxbId);
				for (ObjectId id : allJXBIds) {
					List<ObjectId> stuIds = new ArrayList<ObjectId>();
					for (ObjectId stuId : jxbEntryMap.get(id).getStudentIds()) {
						stuIds.add(stuId);
					}

					if (jxbEntryMap.get(jxbId).getStudentIds().size() == jxbEntryMap.get(id).getStudentIds().size()) {
						stuIds.retainAll(jxbEntryMap.get(jxbId).getStudentIds());
						if (stuIds.size() == jxbEntryMap.get(jxbId).getStudentIds().size()) {
							completeCTJXBIds.add(id);
							waitRemoveJXBIds.add(id);
						}
					}
				}
				String XZBName = "";
				if (jxbEntryMap.get(jxbId).getStudentIds().size() > 0) {
					XZBName = gradeName + "(" + classEntryMap.get(studentEntryMap.get(jxbEntryMap.get(jxbId).getStudentIds().get(0)).getClassId()).getXh() + ")班";
				} else {
					XZBName = "组合";
				}

				jxbId = null;
				List<N33_JXBCombineEntry.JxbList> jxbLists = new ArrayList<N33_JXBCombineEntry.JxbList>();
				for (ObjectId completeCTJXBId : completeCTJXBIds) {
					N33_JXBCombineEntry.JxbList jxbList = new N33_JXBCombineEntry.JxbList();
					jxbList.setJxbId(completeCTJXBId);
					jxbList.setRoomName(classroomEntryMap.get(jxbEntryMap.get(completeCTJXBId).getClassroomId()).getRoomName());
					jxbList.setTeaName(teaEntryMap.get(jxbEntryMap.get(completeCTJXBId).getTercherId()).getUserName());
					List<ObjectId> buFenCTJXBIds = new ArrayList<ObjectId>();
					List<N33_JXBCombineEntry.IntersectJXB> intersectJXBList = new ArrayList<N33_JXBCombineEntry.IntersectJXB>();
					for (ObjectId ids : JXBIds) {
						N33_JXBCombineEntry.IntersectJXB intersectJXB = new N33_JXBCombineEntry.IntersectJXB();
						List<ObjectId> stuIds = new ArrayList<ObjectId>();
						for (ObjectId stuId : jxbEntryMap.get(ids).getStudentIds()) {
							stuIds.add(stuId);
						}

						stuIds.retainAll(jxbEntryMap.get(completeCTJXBId).getStudentIds());

						List<ObjectId> stuIds2 = new ArrayList<ObjectId>();
						for (ObjectId id : jxbEntryMap.get(completeCTJXBId).getStudentIds()) {
							stuIds2.add(id);
						}
						stuIds2.retainAll(jxbEntryMap.get(ids).getStudentIds());

						if ((stuIds.size() != jxbEntryMap.get(ids).getStudentIds().size() && stuIds.size() > 0) || (stuIds2.size() > 0 && stuIds2.size() != jxbEntryMap.get(completeCTJXBId).getStudentIds().size())) {
							intersectJXB.setIntersectJxbId(ids);
							intersectJXB.setIntersectRoomName(classroomEntryMap.get(jxbEntryMap.get(ids).getClassroomId()).getRoomName());
							intersectJXB.setIntersectTeaName(teaEntryMap.get(jxbEntryMap.get(ids).getTercherId()).getUserName());
							intersectJXBList.add(intersectJXB);
							buFenCTJXBIds.add(ids);
							waitRemoveJXBIds.add(ids);
						}
					}

					//如果一个组合中完全冲突的教学班数量为1，部分冲突的教学班中存在完全冲突的教学班
					for (ObjectId ids1 : buFenCTJXBIds) {
						for (ObjectId ids2 : buFenCTJXBIds) {
							List<ObjectId> stuIds = new ArrayList<ObjectId>();
							for (ObjectId stuId : jxbEntryMap.get(ids1).getStudentIds()) {
								stuIds.add(stuId);
							}
							if (!ids1.toString().equals(ids2.toString()) && jxbEntryMap.get(ids1).getStudentIds().size() == jxbEntryMap.get(ids2).getStudentIds().size()) {
								stuIds.retainAll(jxbEntryMap.get(ids2).getStudentIds());
								if (stuIds.size() == jxbEntryMap.get(ids2).getStudentIds().size()) {
									jxbId = ids1;
									continue bufenFlag;
								}
							}
						}
					}
					jxbList.setIntersectJXB(intersectJXBList);
					jxbLists.add(jxbList);
					jxbCombineEntry.setJxbList(jxbLists);
				}
				//判断同一个组合中的相交教学班是否存在没有学生冲突的两个教学班
				for (N33_JXBCombineEntry.JxbList jxbList : jxbLists) {
					List<N33_JXBCombineEntry.IntersectJXB> intersectJXBS = jxbList.getIntersectJXB();
					for (N33_JXBCombineEntry.IntersectJXB intersectJXB : intersectJXBS) {
						//和intersectJXB教学班已经无冲突的教学班，防止重复存储
						List<ObjectId> jxbIds = new ArrayList<ObjectId>();
						List<N33_JXBCombineEntry.IsNoCTJXB> isNoCTJXBS = new ArrayList<N33_JXBCombineEntry.IsNoCTJXB>();
						N33_JXBCombineEntry.IsNoCTJXB isNoCTJXB = new N33_JXBCombineEntry.IsNoCTJXB();
						for (N33_JXBCombineEntry.JxbList jxbList1 : jxbLists) {
							List<N33_JXBCombineEntry.IntersectJXB> intersectJXBS1 = jxbList1.getIntersectJXB();
							for (N33_JXBCombineEntry.IntersectJXB intersectJXB1 : intersectJXBS1) {
								if (!intersectJXB.getIntersectJxbId().toString().equals(intersectJXB1.getIntersectJxbId().toString())) {
									N33_JXBEntry jxbEntry = jxbEntryMap.get(intersectJXB.getIntersectJxbId());
									//用来做交集操作，防止引用传递
									List<ObjectId> stuIds = new ArrayList<ObjectId>();
									for (ObjectId ids : jxbEntry.getStudentIds()) {
										stuIds.add(ids);
									}
									stuIds.retainAll(jxbEntryMap.get(intersectJXB1.getIntersectJxbId()).getStudentIds());
									if (stuIds.size() == 0 && !jxbIds.contains(intersectJXB1.getIntersectJxbId())) {
										isNoCTJXB.setJxbId(intersectJXB.getIntersectJxbId());
										isNoCTJXB.setoJxbId(intersectJXB1.getIntersectJxbId());
										isNoCTJXB.setFlag(false);
										isNoCTJXBS.add(isNoCTJXB);
										jxbIds.add(intersectJXB1.getIntersectJxbId());
									}
								}
							}
						}
						intersectJXB.setIsNoCTJXB(isNoCTJXBS);
					}
				}
				count++;
				if (type == 1) {
					jxbCombineEntry.setName("组合D" + count);
				} else if (type == 2) {
					jxbCombineEntry.setName("组合H" + count);
				} else {
					jxbCombineEntry.setName(XZBName);
				}
				allJXBIds.removeAll(waitRemoveJXBIds);
				saveList.add(jxbCombineEntry);
			} else {
				break;
			}
		}
		if (saveList.size() > 0) {
			jxbCombineDao.addN33_JXBCombine(saveList);
		}
	}

	/**
	 * 查询等级的教学班所有完全冲突的教学班和完全无冲突的教学班
	 *
	 * @param ciId
	 * @param sid
	 * @param gradeId
	 * @param type
	 * @param isExport 1表示导出
	 * @return
	 */
	public List<List<Map<String, Object>>> getJXBZuHeList(ObjectId ciId, ObjectId sid, ObjectId gradeId, Integer
			type, Integer isExport) {
		List<List<Map<String, Object>>> retList = new ArrayList<List<Map<String, Object>>>();

		//学科
		List<N33_KSEntry> ksEn = subjectDao.getIsolateSubjectEntryByXqid(ciId, sid, gradeId);
		List<ObjectId> subId = new ArrayList<ObjectId>();
		for (N33_KSEntry entry : ksEn) {
			subId.add(entry.getSubjectId());
		}
		//jxb add
		int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
		//jxb add
		//所有对应走班类型的教学班
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gradeId, subId, ciId, type,acClassType);
		if (type == 3) {
			jxbEntries.addAll(jxbDao.getJXBList(sid, gradeId, subId, ciId, 6,acClassType));
		}

		List<ObjectId> allJXBIds = new ArrayList<ObjectId>();
		List<ObjectId> JXBIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			allJXBIds.add(jxbEntry.getID());
			JXBIds.add(jxbEntry.getID());
		}
		//对应次的老师
		Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(ciId, sid);
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = getJXBMap(jxbEntries);
		Map<ObjectId, N33_JXBDTO> jxbdtoMap = getJXBDTOMap(jxbEntries, teaEntryMap);

		//对应次对应年级的学生
		Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidAndGradeIdMap(gradeId, ciId);

		//对应次对应年级所有的行政班
		Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(gradeId, ciId);

		//年级名称
		String gradeName = gradeDao.findIsolateGradeEntryByGradeId(gradeId, ciId).getName();
		//对应次的教室
		Map<ObjectId, N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomEntryListByXqGradeForMap(ciId, sid);

		Integer count = 0;
		//如果是找到单独的一个教学班没有完全冲突，但是与这个单独的教学班的所有部分冲突教学班中又存在完全冲突的教学班，则先找完全冲突的教学班
		ObjectId jxbId = null;
		int i = 1;
		bufenFlag:
		while (true) {
			//count++;
			Integer count1 = 0;
			if (allJXBIds.size() > 0) {
				if (jxbId == null) {
					jxbId = allJXBIds.get(0);
				}
				//完全冲突的教学班List放在一个组合中
				List<ObjectId> completeCTJXBIds = new ArrayList<ObjectId>();
				//等待第一次循环移除的教学班集合
				List<ObjectId> waitRemoveJXBIds = new ArrayList<ObjectId>();
				completeCTJXBIds.add(jxbId);
				allJXBIds.remove(jxbId);
				waitRemoveJXBIds.add(jxbId);
				Map<String, Object> retMap1 = new HashMap<String, Object>();
				Map<String, Object> retMap2 = new HashMap<String, Object>();
				List<ObjectId> studentIds = jxbEntryMap.get(jxbId).getStudentIds();

				//取出虚拟班学生所在的行政班
				Map<ObjectId, Integer> clsIdsMap = new HashMap<ObjectId, Integer>();
				List<ObjectId> addedStuIds = new ArrayList<ObjectId>();
				for (ObjectId stuId : studentIds) {
					if (!addedStuIds.contains(stuId)) {
						if (studentEntryMap.get(stuId) == null) {
							continue;
						}
						if (clsIdsMap.get(studentEntryMap.get(stuId).getClassId()) == null) {
							clsIdsMap.put(studentEntryMap.get(stuId).getClassId(), 1);
							addedStuIds.add(stuId);
						} else {
							Integer stuCount = clsIdsMap.get(studentEntryMap.get(stuId).getClassId());
							stuCount++;
							clsIdsMap.put(studentEntryMap.get(stuId).getClassId(), stuCount);
							addedStuIds.add(stuId);
						}
					}
				}
				Integer rowSpanCount = 0;
				//检测完全冲突的教学班
				for (ObjectId id : allJXBIds) {

					List<ObjectId> stuIds = new ArrayList<ObjectId>();
					for (ObjectId stuId : jxbEntryMap.get(id).getStudentIds()) {
						stuIds.add(stuId);
					}

					if (jxbEntryMap.get(jxbId).getStudentIds().size() == jxbEntryMap.get(id).getStudentIds().size()) {
						stuIds.retainAll(jxbEntryMap.get(jxbId).getStudentIds());
						if (stuIds.size() == jxbEntryMap.get(jxbId).getStudentIds().size()) {
							List<ObjectId> studentIds1 = jxbEntryMap.get(jxbId).getStudentIds();
							for (ObjectId stuId : studentIds1) {
								if (!addedStuIds.contains(stuId)) {
									if (studentEntryMap.get(stuId) == null) {
										continue;
									}
									if (clsIdsMap.get(studentEntryMap.get(stuId).getClassId()) == null) {
										clsIdsMap.put(studentEntryMap.get(stuId).getClassId(), 0);
										addedStuIds.add(stuId);
									} else {
										Integer stuCount = clsIdsMap.get(studentEntryMap.get(stuId).getClassId());
										stuCount++;
										clsIdsMap.put(studentEntryMap.get(stuId).getClassId(), stuCount);
										addedStuIds.add(stuId);
									}
								}
							}
							completeCTJXBIds.add(id);
							waitRemoveJXBIds.add(id);
						}
					}
				}
				jxbId = null;
				//学生来源
				//如果是行政班组合名就叫行政班名
				String XZBName = "";
				StringBuffer name = new StringBuffer();
				if (clsIdsMap.entrySet() == null || clsIdsMap.entrySet().size() == 0) {
					XZBName = "组合";
				}
				for (Map.Entry<ObjectId, Integer> entry : clsIdsMap.entrySet()) {
					if ("".equals(XZBName)) {
						XZBName = gradeName + "(" + classEntryMap.get(entry.getKey()).getXh() + ")班";
					}
					if (isExport == null) {
						name.append(gradeName + "(" + classEntryMap.get(entry.getKey()).getXh() + ")班 (" + entry.getValue() + ")<br>");
					} else {
						name.append(gradeName + "(" + classEntryMap.get(entry.getKey()).getXh() + ")班 (" + entry.getValue() + ")\r\n");
					}
				}
				retMap2.put("name", name);
				for (ObjectId completeCTJXBId : completeCTJXBIds) {
					Map<String, Object> retMap3 = new HashMap<String, Object>();
					List<ObjectId> buFenCTJXBIds = new ArrayList<ObjectId>();
					for (ObjectId ids : JXBIds) {
						List<ObjectId> stuIds = new ArrayList<ObjectId>();
						for (ObjectId stuId : jxbEntryMap.get(ids).getStudentIds()) {
							stuIds.add(stuId);
						}

						stuIds.retainAll(jxbEntryMap.get(completeCTJXBId).getStudentIds());

						List<ObjectId> stuIds2 = new ArrayList<ObjectId>();
						for (ObjectId id : jxbEntryMap.get(completeCTJXBId).getStudentIds()) {
							stuIds2.add(id);
						}
						stuIds2.retainAll(jxbEntryMap.get(ids).getStudentIds());

						if ((stuIds.size() != jxbEntryMap.get(ids).getStudentIds().size() && stuIds.size() > 0) || (stuIds2.size() > 0 && stuIds2.size() != jxbEntryMap.get(completeCTJXBId).getStudentIds().size())) {
							buFenCTJXBIds.add(ids);
							waitRemoveJXBIds.add(ids);
						}
					}

					//如果一个组合中完全冲突的教学班数量为1，部分冲突的教学班中存在完全冲突的教学班
					if (completeCTJXBIds.size() == 1) {
						for (ObjectId ids1 : buFenCTJXBIds) {
							for (ObjectId ids2 : buFenCTJXBIds) {
								List<ObjectId> stuIds = new ArrayList<ObjectId>();
								if (jxbEntryMap.get(ids1).getStudentIds() != null) {
									for (ObjectId stuId : jxbEntryMap.get(ids1).getStudentIds()) {
										stuIds.add(stuId);
									}
								}
								if (!ids1.toString().equals(ids2.toString()) && jxbEntryMap.get(ids1).getStudentIds() != null && jxbEntryMap.get(ids2).getStudentIds() != null && jxbEntryMap.get(ids1).getStudentIds().size() == jxbEntryMap.get(ids2).getStudentIds().size() && jxbEntryMap.get(ids2).getStudentIds().size() != 0 && jxbEntryMap.get(ids1).getStudentIds().size() != 0) {
									stuIds.retainAll(jxbEntryMap.get(ids2).getStudentIds());
									if (stuIds.size() == jxbEntryMap.get(ids2).getStudentIds().size()) {
										jxbId = ids1;
										continue bufenFlag;
									}
								}
							}
						}
					}


					retMap3.put("name", jxbdtoMap.get(completeCTJXBId).getName());
					retMap3.put("stuCount", jxbdtoMap.get(completeCTJXBId).getStudentCount());
					retMap3.put("id", completeCTJXBId.toString());
					Map<String, Object> retMap4 = new HashMap<String, Object>();
					retMap4.put("name", jxbEntryMap.get(completeCTJXBId).getClassroomId() == null ? "" : classroomEntryMap.get(jxbEntryMap.get(completeCTJXBId).getClassroomId()).getRoomName());
					retMap4.put("roomId", jxbEntryMap.get(completeCTJXBId).getClassroomId() == null ? "" : jxbEntryMap.get(completeCTJXBId).getClassroomId().toString());
					retMap4.put("jxbId", completeCTJXBId.toString());
					Map<String, Object> retMap5 = new HashMap<String, Object>();
					retMap5.put("name", jxbEntryMap.get(completeCTJXBId).getTercherId() == null ? "" : teaEntryMap.get(jxbEntryMap.get(completeCTJXBId).getTercherId()).getUserName());
					retMap5.put("teaId", jxbEntryMap.get(completeCTJXBId).getTercherId() == null ? "" : jxbEntryMap.get(completeCTJXBId).getTercherId().toString());
					retMap5.put("subId", jxbEntryMap.get(completeCTJXBId).getSubjectId().toString());
					retMap5.put("jxbId", completeCTJXBId.toString());
					if (buFenCTJXBIds.size() > 0) {
						retMap3.put("rowspan", buFenCTJXBIds.size());
						retMap4.put("rowspan", buFenCTJXBIds.size());
						retMap5.put("rowspan", buFenCTJXBIds.size());
						rowSpanCount += buFenCTJXBIds.size();
					} else {
						retMap3.put("rowspan", 1);
						retMap4.put("rowspan", 1);
						retMap5.put("rowspan", 1);
						rowSpanCount += 1;
					}
					if (buFenCTJXBIds.size() > 0) {
						Integer count2 = 0;
						for (ObjectId bufenCTId : buFenCTJXBIds) {
							count1++;
							count2++;
							List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
							Map<String, Object> retMap6 = new HashMap<String, Object>();
							retMap6.put("name", jxbdtoMap.get(bufenCTId).getName());
							if (type == 1) {
								retMap6.put("zuHeName", "组合D" + (count + 1));
							} else if (type == 1) {
								retMap6.put("zuHeName", "组合H" + (count + 1));
							} else {
								retMap6.put("zuHeName", XZBName);
							}
							retMap6.put("count", i);
							i++;
							retMap6.put("stuCount", jxbdtoMap.get(bufenCTId).getStudentCount());
							retMap6.put("id", bufenCTId.toString());
							Map<String, Object> retMap7 = new HashMap<String, Object>();
							retMap7.put("name", jxbEntryMap.get(bufenCTId).getClassroomId() == null ? "" : classroomEntryMap.get(jxbEntryMap.get(bufenCTId).getClassroomId()).getRoomName());
							retMap7.put("roomId", jxbEntryMap.get(bufenCTId).getClassroomId() == null ? "" : jxbEntryMap.get(bufenCTId).getClassroomId().toString());
							retMap7.put("jxbId", bufenCTId.toString());
							Map<String, Object> retMap8 = new HashMap<String, Object>();
							retMap8.put("name", jxbEntryMap.get(bufenCTId).getTercherId() == null ? "" : teaEntryMap.get(jxbEntryMap.get(bufenCTId).getTercherId()).getUserName());
							retMap8.put("teaId", jxbEntryMap.get(bufenCTId).getTercherId() == null ? "" : jxbEntryMap.get(bufenCTId).getTercherId().toString());
							retMap8.put("subId", jxbEntryMap.get(bufenCTId).getSubjectId().toString());
							retMap8.put("jxbId", bufenCTId.toString());
							if (count1 == 1) {
								list.add(retMap1);
								list.add(retMap2);
							}
							if (count2 == 1) {
								list.add(retMap3);
								list.add(retMap4);
								list.add(retMap5);
							}
							list.add(retMap6);
							list.add(retMap7);
							list.add(retMap8);
							retList.add(list);
						}
					} else {
						count1++;
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						Map<String, Object> retMap6 = new HashMap<String, Object>();
						retMap6.put("name", "-");
						Map<String, Object> retMap7 = new HashMap<String, Object>();
						retMap7.put("name", "-");
						Map<String, Object> retMap8 = new HashMap<String, Object>();
						retMap8.put("name", "-");
						if (count1 == 1) {
							list.add(retMap1);
							list.add(retMap2);
						}
						list.add(retMap3);
						list.add(retMap4);
						list.add(retMap5);
						list.add(retMap6);
						list.add(retMap7);
						list.add(retMap8);
						retList.add(list);
					}
				}
				count++;
				if (type == 1) {
					retMap1.put("name", "组合D" + count);
				} else if (type == 2) {
					retMap1.put("name", "组合H" + count);
				} else {
					retMap1.put("name", XZBName);
				}
				if (rowSpanCount > completeCTJXBIds.size()) {
					retMap1.put("rowspan", rowSpanCount);
					retMap2.put("rowspan", rowSpanCount);
				} else {
					retMap1.put("rowspan", completeCTJXBIds.size());
					retMap2.put("rowspan", rowSpanCount);
				}
				allJXBIds.removeAll(waitRemoveJXBIds);
			} else {
				break;
			}
		}
		return retList;
	}

	//得到教学班的Map
	private Map<ObjectId, N33_JXBEntry> getJXBMap(List<N33_JXBEntry> jxbEntryList) {
		Map<ObjectId, N33_JXBEntry> retMap = new HashMap<ObjectId, N33_JXBEntry>();

		for (N33_JXBEntry jxbEntry : jxbEntryList) {
			retMap.put(jxbEntry.getID(), jxbEntry);
		}
		return retMap;
	}

	private Map<ObjectId, N33_JXBDTO> getJXBDTOMap
			(List<N33_JXBEntry> jxbEntryList, Map<ObjectId, N33_TeaEntry> teaEntryMap) {
		Map<ObjectId, N33_JXBDTO> retMap = new HashMap<ObjectId, N33_JXBDTO>();

		for (N33_JXBEntry jxbEntry : jxbEntryList) {
			N33_JXBDTO dto = new N33_JXBDTO(jxbEntry);
			if (teaEntryMap.get(jxbEntry.getTercherId()) != null) {
				dto.setTeacherName(teaEntryMap.get(jxbEntry.getTercherId()).getUserName());
			} else {
				dto.setTeacherName("");
			}
			List<String> stuIds = new ArrayList<String>();
			for (ObjectId id : jxbEntry.getStudentIds()) {
				stuIds.add(id.toString());
			}
			dto.setStudentIds(stuIds);
			retMap.put(jxbEntry.getID(), dto);
		}
		return retMap;
	}

	public List<IdNameValuePairDTO> getGradeWeek(ObjectId gradeId, ObjectId ciId, ObjectId sid) {
		List<IdNameValuePairDTO> retList = new ArrayList<IdNameValuePairDTO>();
		String[] weekArgs = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
		N33_GradeWeekRangeEntry gradeWeekRangeEntry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(ciId, sid, gradeId);
		if (gradeWeekRangeEntry != null) {
			for (int i = gradeWeekRangeEntry.getStart(); i <= gradeWeekRangeEntry.getEnd(); i++) {
				IdNameValuePairDTO dto = new IdNameValuePairDTO();
				dto.setName(weekArgs[i - 1]);
				dto.setType(i);
				retList.add(dto);
			}
		}
		return retList;
	}

	public Map<String, Object> getZuHeConflictedSettledJXB(ObjectId zuHeId, ObjectId xqid, ObjectId sid) {
		N33_PaiKeZuHeEntry zuHeEntry = zuHeDao.findEntryById(zuHeId);
		List<ObjectId> jxbIds = zuHeEntry.getJxbIds();
		List<String> jxbStringIds = new ArrayList<String>();
		for (ObjectId jxbId : jxbIds) {
			jxbStringIds.add(jxbId.toString());
		}
		Map<String, Object> retMap = paiKeService.getZuHeConflictedSettled(jxbStringIds, xqid, sid);
		return retMap;
	}

	public List<N33_JXBDTO> getZuHeJXBList(ObjectId zuHeId, ObjectId xqid, ObjectId sid, ObjectId gradeId, Integer
			type) {
		List<N33_JXBDTO> retList = new ArrayList<N33_JXBDTO>();
		N33_PaiKeZuHeEntry zuHeEntry = zuHeDao.findEntryById(zuHeId);
		List<ObjectId> jxbIds = zuHeEntry.getJxbIds();
		// jxb add 
        ObjectId cisId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,cisId,1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
	    // jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(sid, gradeId, xqid, acClassType);
		List<ObjectId> danshuangJxbIds = new ArrayList<ObjectId>();
		for (ObjectId jxbId : jxbIds) {
			if (jxbEntryMap.get(jxbId).getType() == 6) {
				danshuangJxbIds.add(jxbEntryMap.get(jxbId).getRelativeId());
			}
		}
		jxbIds.addAll(danshuangJxbIds);
		Map<ObjectId, N33_KSDTO> ksdtoMap = new HashMap<ObjectId, N33_KSDTO>();
		List<ObjectId> subjectIds = new ArrayList<ObjectId>();
		List<N33_KSDTO> ksdtos = subjectService.getIsolateSubjectByGradeId(xqid, sid, gradeId.toString(), type);
		if (ksdtos != null && ksdtos.size() != 0) {
			for (N33_KSDTO dto : ksdtos) {
				subjectIds.add(new ObjectId(dto.getSubid()));
				ksdtoMap.put(new ObjectId(dto.getSubid()), dto);
			}
		}

		Map<ObjectId, Integer> ypCountMap = new HashMap<ObjectId, Integer>();
		List<N33_JXBKSEntry> jxbksEntries = jxbksDao.getJXBKSsBySubjectIds(xqid, sid, gradeId, subjectIds);
		if (jxbksEntries != null && jxbksEntries.size() != 0) {
			for (N33_JXBKSEntry jxbksEntry : jxbksEntries) {
				ypCountMap.put(jxbksEntry.getJxbId(), jxbksEntry.getYpCount());
			}
		}

		//该组合的所有教学班
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBListByIds(jxbIds, xqid);
		if (jxbEntries != null && jxbEntries.size() != 0) {
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(MongoUtils.getFieldObjectIDs(jxbEntries, "tid"), xqid);
			Map<ObjectId, N33_ClassroomEntry> roomEntryMap = classroomDao.getRoomEntryListByXqRoomForMap(xqid, MongoUtils.getFieldObjectIDs(jxbEntries, "clsrmId"));
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				N33_JXBDTO jxbdto = new N33_JXBDTO(jxbEntry);
				jxbdto.setClassroomName(roomEntryMap.get(jxbEntry.getClassroomId()) != null ? roomEntryMap.get(jxbEntry.getClassroomId()).getRoomName() : "");
				jxbdto.setTeacherName(teaEntryMap.get(jxbEntry.getTercherId()) != null ? teaEntryMap.get(jxbEntry.getTercherId()).getUserName() : "");
				if (jxbEntry.getType() == 1) {
					jxbdto.setZksCount(jxbEntry.getJXBKS());
				} else {
					jxbdto.setZksCount(jxbEntry.getJXBKS());
				}
				if (ypCountMap.get(jxbEntry.getID()) != null) {
					jxbdto.setYpksCount(ypCountMap.get(jxbEntry.getID()));
				} else {
					jxbdto.setYpksCount(0);
				}
				retList.add(jxbdto);
			}
		}
		return retList;
	}

	public void saveKeBiaoByGroup(ObjectId zuHeId, ObjectId xqid, Integer x, Integer y, ObjectId sid, ObjectId
			gradeId, Integer type) {
		N33_PaiKeZuHeEntry zuHeEntry = zuHeDao.findEntryById(zuHeId);
		List<ObjectId> jxbIds = zuHeEntry.getJxbIds();
		Map<ObjectId, N33_KSDTO> ksdtoMap = new HashMap<ObjectId, N33_KSDTO>();
		List<ObjectId> subjectIds = new ArrayList<ObjectId>();
		List<N33_KSDTO> ksdtos = subjectService.getIsolateSubjectByGradeId(xqid, sid, gradeId.toString(), type);
		if (ksdtos != null && ksdtos.size() != 0) {
			for (N33_KSDTO dto : ksdtos) {
				subjectIds.add(new ObjectId(dto.getSubid()));
				ksdtoMap.put(new ObjectId(dto.getSubid()), dto);
			}
		}

		Map<ObjectId, Integer> ypCountMap = new HashMap<ObjectId, Integer>();
		List<N33_JXBKSEntry> jxbksEntries = jxbksDao.getJXBKSsBySubjectIds(xqid, sid, gradeId, subjectIds);
		if (jxbksEntries != null && jxbksEntries.size() != 0) {
			for (N33_JXBKSEntry jxbksEntry : jxbksEntries) {
				ypCountMap.put(jxbksEntry.getJxbId(), jxbksEntry.getYpCount());
			}
		}

		//该组合的所有教学班
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBListByIds(jxbIds, xqid);

		for (N33_JXBEntry jxbEntry : jxbEntries) {
			ObjectId classRoomId = jxbEntry.getClassroomId();
			N33_YKBEntry ykbEntry = ykbDao.getN33_YKBByClassRoom(x, y, classRoomId, xqid);
			if (ykbEntry.getJxbId() != null) {
				jxbksDao.updateN33_JXBKSMinusCountByJxbId(ykbEntry.getJxbId());
			}
			if (ykbEntry.getNJxbId() != null) {
				jxbksDao.updateN33_JXBKSMinusCountByJxbId(ykbEntry.getNJxbId());
			}
			if (jxbEntry.getDanOrShuang() != 0) {
				N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
				ykbEntry.setNSubjectId(jxbEntry2.getSubjectId());
				ykbEntry.setNTeacherId(jxbEntry2.getTercherId());
				ykbEntry.setNJxbId(jxbEntry2.getID());
			}
			if (ypCountMap.get(jxbEntry.getID()) == null) {
				ypCountMap.put(jxbEntry.getID(), 0);
			}
			if (jxbEntry.getJXBKS() != 0 && (ypCountMap.get(jxbEntry.getID()) >= jxbEntry.getJXBKS()) || jxbEntry.getJXBKS() == 0) {
				continue;
			}

			if (jxbEntry.getDanOrShuang() != 0) {
				N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
				N33_JXBKSEntry entry = jxbksDao.getJXBKSsByJXBId(jxbEntry.getRelativeId());
				if (entry == null) {
					jxbksDao.addN33_JXBKSEntry(new N33_JXBKSEntry(jxbEntry.getRelativeId(), jxbEntry2.getSubjectId(), 1, jxbEntry2.getGradeId(), jxbEntry2.getSchoolId(), jxbEntry2.getTermId(), jxbEntry2.getDanOrShuang()));
				} else {
					jxbksDao.updateN33_JXBKSCountByJxbId(jxbEntry.getRelativeId());
				}
			}
			N33_JXBKSEntry entry = jxbksDao.getJXBKSsByJXBId(jxbEntry.getID());
			if (entry == null) {
				jxbksDao.addN33_JXBKSEntry(new N33_JXBKSEntry(jxbEntry.getID(), jxbEntry.getSubjectId(), 1, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang()));
			} else {
				jxbksDao.updateN33_JXBKSCountByJxbId(jxbEntry.getID());
			}

			ykbEntry.setJxbId(jxbEntry.getID());
			ykbEntry.setIsUse(Constant.ONE);
			ykbEntry.setGradeId(jxbEntry.getGradeId());
			ykbEntry.setSubjectId(jxbEntry.getSubjectId());
			ykbEntry.setTeacherId(jxbEntry.getTercherId());
			ykbEntry.setType(jxbEntry.getType());
			ykbEntry.setZuHeId(zuHeId);
			ykbDao.updateN33_YKB(ykbEntry);
		}
	}

	public void clearKeBiaoByGroup(ObjectId zuHeId, ObjectId xqid, Integer x, Integer y, ObjectId sid, ObjectId
			gradeId) {
		List<N33_YKBEntry> ykbEntryList = ykbDao.clearKeBiaoByGroup(xqid, sid, gradeId, x, y, zuHeId);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
				N33_YKBEntry n33_ykbEntry = new N33_YKBEntry(entry.getX(), entry.getY(), entry.getClassroomId(), entry.getSchoolId(), entry.getTermId(), 0);
				n33_ykbEntry.setID(entry.getID());
				n33_ykbEntry.setNJxbId(null);
				n33_ykbEntry.setNSubjectId(null);
				n33_ykbEntry.setNTeacherId(null);
				n33_ykbEntry.setZuHeId(null);
				ykbDao.updateN33_YKB(n33_ykbEntry);
				if (entry.getNJxbId() != null) {
					N33_JXBKSEntry jxbksEntry2 = jxbksDao.getJXBKSsByJXBId(entry.getNJxbId());
					if (jxbksEntry2 != null) {
						jxbksEntry2.setYpCount(jxbksEntry2.getYpCount() - 1 >= 0 ? jxbksEntry2.getYpCount() - 1 : 0);
					}
					jxbksDao.updateN33_JXBKS(jxbksEntry2);
				}
				N33_JXBKSEntry jxbksEntry = jxbksDao.getJXBKSsByJXBId(entry.getJxbId());
				if (jxbksEntry != null) {
					jxbksEntry.setYpCount(jxbksEntry.getYpCount() - 1 >= 0 ? jxbksEntry.getYpCount() - 1 : 0);
				}
				jxbksDao.updateN33_JXBKS(jxbksEntry);
			}
		}
	}

	public List<Map<String, Object>> getKbByGroup(ObjectId zuHeId, ObjectId xqid, ObjectId sid, ObjectId gradeId) {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.findKeBiaoByGroup(xqid, sid, gradeId, zuHeId);
		for (N33_YKBEntry entry : ykbEntryList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("x", entry.getX());
			map.put("y", entry.getY());
			map.put("zuHeId", zuHeId.toString());
			retList.add(map);
		}
		return retList;
	}

	public List<String> getXYJXBList(ObjectId zuHeId, ObjectId ciId, ObjectId sid, ObjectId gradeId, Integer
			x, Integer y) {
		List<String> jxbList = new ArrayList<String>();
		N33_PaiKeZuHeEntry zuHeEntry = zuHeDao.findEntryById(zuHeId);
		List<ObjectId> jxbIds = zuHeEntry.getJxbIds();
		// jxb add 
        //ObjectId cisId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,ciId,1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
	    // jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(sid, gradeId, ciId, acClassType);
		List<ObjectId> danshuangJxbIds = new ArrayList<ObjectId>();
		for (ObjectId ids : jxbIds) {
			if (jxbEntryMap.get(ids).getType() == 6) {
				danshuangJxbIds.add(jxbEntryMap.get(ids).getRelativeId());
			}
		}
		jxbIds.addAll(danshuangJxbIds);
		List<N33_YKBEntry> ykbEntryList = ykbDao.clearKeBiaoByGroup(ciId, sid, gradeId, x, y, zuHeId);
		List<ObjectId> ypJXBIds = new ArrayList<ObjectId>();
		for (N33_YKBEntry ykbEntry : ykbEntryList) {
			if (ykbEntry.getNJxbId() != null) {
				ypJXBIds.add(ykbEntry.getNJxbId());
			}
			ypJXBIds.add(ykbEntry.getJxbId());
		}

		for (ObjectId jxbId : jxbIds) {
			if (!ypJXBIds.contains(jxbId)) {
				jxbList.add(jxbId.toString());
			}
		}
		return jxbList;
	}

	public void clearZuHeJXBByJXBID(ObjectId zuHeId, ObjectId xqid, ObjectId sid, ObjectId gradeId, Integer
			x, Integer y, ObjectId jxbId) {
		List<N33_YKBEntry> ykbEntryList = ykbDao.clearKeBiaoByGroup(xqid, sid, gradeId, x, y, zuHeId);
		for (N33_YKBEntry entry : ykbEntryList) {
			if (jxbId.toString().equals(entry.getJxbId().toString()) || (entry.getNJxbId() != null && jxbId.toString().equals(entry.getNJxbId().toString()))) {
				N33_YKBEntry n33_ykbEntry = new N33_YKBEntry(entry.getX(), entry.getY(), entry.getClassroomId(), entry.getSchoolId(), entry.getTermId(), 0);
				n33_ykbEntry.setID(entry.getID());
				n33_ykbEntry.setNJxbId(null);
				n33_ykbEntry.setNSubjectId(null);
				n33_ykbEntry.setNTeacherId(null);
				n33_ykbEntry.setZuHeId(null);
				ykbDao.updateN33_YKB(n33_ykbEntry);
				if (entry.getNJxbId() != null) {
					N33_JXBKSEntry jxbksEntry2 = jxbksDao.getJXBKSsByJXBId(entry.getNJxbId());
					if (jxbksEntry2 != null) {
						jxbksEntry2.setYpCount(jxbksEntry2.getYpCount() - 1 >= 0 ? jxbksEntry2.getYpCount() - 1 : 0);
					}
					jxbksDao.updateN33_JXBKS(jxbksEntry2);
				}
				N33_JXBKSEntry jxbksEntry = jxbksDao.getJXBKSsByJXBId(entry.getJxbId());
				if (jxbksEntry != null) {
					jxbksEntry.setYpCount(jxbksEntry.getYpCount() - 1 >= 0 ? jxbksEntry.getYpCount() - 1 : 0);
				}
				jxbksDao.updateN33_JXBKS(jxbksEntry);
			}
		}
	}

	public void saveZuHeJXBByJXBID(ObjectId zuHeId, ObjectId xqid, ObjectId sid, ObjectId gradeId, Integer
			x, Integer y, ObjectId jxbId) {
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		ObjectId classRoomId = jxbEntry.getClassroomId();
		N33_YKBEntry ykbEntry = ykbDao.getN33_YKBByClassRoom(x, y, classRoomId, xqid);
		if (ykbEntry.getJxbId() != null) {
			jxbksDao.updateN33_JXBKSMinusCountByJxbId(ykbEntry.getJxbId());
		}
		if (ykbEntry.getNJxbId() != null) {
			jxbksDao.updateN33_JXBKSMinusCountByJxbId(ykbEntry.getNJxbId());
		}
		if (jxbEntry.getDanOrShuang() != 0) {
			N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
			ykbEntry.setNSubjectId(jxbEntry2.getSubjectId());
			ykbEntry.setNTeacherId(jxbEntry2.getTercherId());
			ykbEntry.setNJxbId(jxbEntry2.getID());
		}

		if (jxbEntry.getDanOrShuang() != 0) {
			N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
			N33_JXBKSEntry entry = jxbksDao.getJXBKSsByJXBId(jxbEntry.getRelativeId());
			if (entry == null) {
				jxbksDao.addN33_JXBKSEntry(new N33_JXBKSEntry(jxbEntry.getRelativeId(), jxbEntry2.getSubjectId(), 1, jxbEntry2.getGradeId(), jxbEntry2.getSchoolId(), jxbEntry2.getTermId(), jxbEntry2.getDanOrShuang()));
			} else {
				jxbksDao.updateN33_JXBKSCountByJxbId(jxbEntry.getRelativeId());
			}
		}
		N33_JXBKSEntry entry = jxbksDao.getJXBKSsByJXBId(jxbEntry.getID());
		if (entry == null) {
			jxbksDao.addN33_JXBKSEntry(new N33_JXBKSEntry(jxbEntry.getID(), jxbEntry.getSubjectId(), 1, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang()));
		} else {
			jxbksDao.updateN33_JXBKSCountByJxbId(jxbEntry.getID());
		}

		ykbEntry.setJxbId(jxbEntry.getID());
		ykbEntry.setIsUse(Constant.ONE);
		ykbEntry.setGradeId(jxbEntry.getGradeId());
		ykbEntry.setSubjectId(jxbEntry.getSubjectId());
		ykbEntry.setTeacherId(jxbEntry.getTercherId());
		ykbEntry.setType(jxbEntry.getType());
		ykbEntry.setZuHeId(zuHeId);
		ykbDao.updateN33_YKB(ykbEntry);
	}


	public List<N33_YKBEntry> getYkbEntries(ObjectId zuHeId, ObjectId xqid, ObjectId sid, ObjectId gradeId) {
		List<N33_YKBEntry> ykbEntryList = ykbDao.findKeBiaoByGroup(xqid, sid, gradeId, zuHeId);
		return ykbEntryList;
	}

}
