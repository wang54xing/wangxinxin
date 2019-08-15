package com.fulaan.new33.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.N33_StudentTagDao;
import com.db.new33.N33_TimeCombineDao;
import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.N33_ClassroomDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.SubjectDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.fulaan.new33.StopMsgException;
import com.fulaan.new33.dto.autopk.N33_ZouBanSetDTO;
import com.fulaan.new33.dto.isolate.StudentDTO;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.autopk.N33_TimeCombKeBiaoService;
import com.fulaan.new33.service.autopk.N33_ZouBanSetService;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_TimeCombineEntry;
import com.pojo.new33.paike.N33_TimeCombineEntry.ZuHeList;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.utils.MongoUtils;

@Service
public class N33_TimeCombineService extends BaseService {
	private N33_StudentTagDao studentTagDao = new N33_StudentTagDao();
	private N33_TimeCombineDao combineDao = new N33_TimeCombineDao();
	private N33_StudentDao studentDao = new N33_StudentDao();
	private SubjectDao subjectDao = new SubjectDao();
	private N33_JXBDao jxbDao = new N33_JXBDao();
	private N33_TeaDao teaDao = new N33_TeaDao();
	private N33_ClassroomDao classroomDao = new N33_ClassroomDao();
	private ClassDao classDao = new ClassDao();
	private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

	@Autowired
	private N33_ZouBanSetService zouBanSetService;

	@Autowired
	private N33_TimeCombKeBiaoService timeCombKeBiaoService;
	
	@Autowired
    private N33_TurnOffService turnOffService;

	/**
	 * 初始化时间段组合数据
	 * @param gradeId
	 * @param ciId
	 * @param sid
	 */
	public void initTimeCombine(ObjectId gradeId,ObjectId ciId,ObjectId sid){
		//查询没被创建虚拟班的学生标签
		List<N33_StudentTagEntry> tagEntries = studentTagDao.getTagListByxqid(ciId, gradeId);
		List<N33_TimeCombineEntry> timeCombineEntries = combineDao.getTimeCombine(ciId,sid,gradeId);
		List<ObjectId> combineTagIds = MongoUtils.getFieldObjectIDs(timeCombineEntries,"tagId");

		//标签中学生
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		for (N33_StudentTagEntry tagEntry : tagEntries) {
			for (N33_StudentTagEntry.StudentInfoEntry student : tagEntry.getStudents()) {
				if (!studentIds.contains(student.getStuId())) {
					studentIds.add(student.getStuId());
				}
			}
		}
		Map<ObjectId, StudentEntry> entryMap = studentDao.getStuMap(studentIds, ciId);

		//对应年级对应次的学科
		Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIds(ciId, gradeId, sid);

		List<N33_TimeCombineEntry> timeCombineEntryList = new ArrayList<N33_TimeCombineEntry>();

		for (N33_StudentTagEntry studentTagEntry : tagEntries) {
			if(!combineTagIds.contains(studentTagEntry.getID())){
				N33_TimeCombineEntry timeCombineEntryDJ = new N33_TimeCombineEntry();
				//等级
				timeCombineEntryDJ.setType(1);
				List<N33_TimeCombineEntry.ZuHeList> zuHeLists = new ArrayList<N33_TimeCombineEntry.ZuHeList>();
				for (int serial = 1;serial <= 3;serial ++) {
					N33_TimeCombineEntry.ZuHeList zuHeList = new N33_TimeCombineEntry.ZuHeList();
					zuHeList.setSerial(serial);
					zuHeLists.add(zuHeList);
				}
				timeCombineEntryDJ.setZuHeList(zuHeLists);
				timeCombineEntryDJ.setSchoolId(sid);
				timeCombineEntryDJ.setGradeId(gradeId);
				timeCombineEntryDJ.setCiId(ciId);
				timeCombineEntryDJ.setTagName(studentTagEntry.getName());
				timeCombineEntryDJ.setTagId(studentTagEntry.getID());
				if (studentTagEntry.getStudents().size() > 0) {
					N33_StudentTagEntry.StudentInfoEntry infoEntry = studentTagEntry.getStudents().get(0);
					if (infoEntry != null) {
						StudentEntry studentEntry = entryMap.get(infoEntry.getStuId());
						//学科标签
						ObjectId sub1 = studentEntry.getSubjectId1();
						ObjectId sub2 = studentEntry.getSubjectId2();
						ObjectId sub3 = studentEntry.getSubjectId3();
						timeCombineEntryDJ.setXkName(studentEntry.getCombiname());
						timeCombineEntryList.add(timeCombineEntryDJ);
						N33_TimeCombineEntry timeCombineEntryHG = new N33_TimeCombineEntry();
						timeCombineEntryHG.setType(2);
						timeCombineEntryHG.setZuHeList(zuHeLists);
						timeCombineEntryHG.setSchoolId(sid);
						timeCombineEntryHG.setGradeId(gradeId);
						timeCombineEntryHG.setCiId(ciId);
						timeCombineEntryHG.setTagName(studentTagEntry.getName());
						timeCombineEntryHG.setTagId(studentTagEntry.getID());
						List<ObjectId> subIds = new ArrayList<ObjectId>();
						subIds.add(sub1);
						subIds.add(sub2);
						subIds.add(sub3);
						String name = "";
						for (N33_KSEntry ksEntry : ksEntryMap.values()) {
							if (ksEntry.getIsZouBan() == 1) {
								if (!subIds.contains(ksEntry.getSubjectId())) {
									name += ksEntry.getSubjectName().substring(0, 1);
									subIds.add(ksEntry.getSubjectId());
								}
							}
						}
						timeCombineEntryHG.setXkName(name);
						timeCombineEntryList.add(timeCombineEntryHG);
					}
				}
			}
		}
		if(timeCombineEntryList.size() > 0){
			combineDao.addTimeCombineList(timeCombineEntryList);
		}
	}

	/**
	 * 查询分时间段组合的头部名称
	 * @param gradeId
	 * @param ciId
	 * @param schoolId
	 * @param type
	 * @return
	 */
	public List<Map<String,Object>> getTimeCombineHeadNameList(ObjectId gradeId,ObjectId ciId,ObjectId schoolId,Integer type){
		List<Map<String,Object>> headNameList = new ArrayList<Map<String,Object>>();
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId,schoolId,gradeId,type);
		if(timeCombineEntryList.size() > 0){
			N33_TimeCombineEntry timeCombineEntry = timeCombineEntryList.get(0);
			List<N33_TimeCombineEntry.ZuHeList> zuHeLists = timeCombineEntry.getZuHeList();
			Collections.sort(zuHeLists, new Comparator<N33_TimeCombineEntry.ZuHeList>() {
				@Override
				public int compare(N33_TimeCombineEntry.ZuHeList o1, N33_TimeCombineEntry.ZuHeList o2) {
					return o1.getSerial() - o2.getSerial();
				}
			});
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("name","标签名");
			headNameList.add(map);
			map = new HashMap<String, Object>();
			map.put("name","选科组合");
			headNameList.add(map);

			if(type == 1){
				for (N33_TimeCombineEntry.ZuHeList zuHeList : zuHeLists) {
					map = new HashMap<String, Object>();
					map.put("name","等级-时段组合" + zuHeList.getSerial());
					map.put("serial",zuHeList.getSerial());
					headNameList.add(map);
				}
			}else{
				for (N33_TimeCombineEntry.ZuHeList zuHeList : zuHeLists) {
					map = new HashMap<String, Object>();
					map.put("name","合格-时段组合" + zuHeList.getSerial());
					map.put("serial",zuHeList.getSerial());
					headNameList.add(map);
				}
			}
		}
		return headNameList;
	}

	/**
	 * 查询分时间段组合
	 * @param gradeId
	 * @param ciId
	 * @param schoolId
	 * @param type
	 * @return
	 */
	public Map<String,Object> getTimeCombine(ObjectId gradeId,ObjectId ciId,ObjectId schoolId,Integer type){
		
		Map<String,Object> returnMap = new HashMap<String, Object>();
		Map<ObjectId,N33_StudentTagEntry> studentTagEntryMap = studentTagDao.getTagListByIdsMapByCiId(ciId,gradeId);
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId,schoolId,gradeId,type);
		// jxb add 
        //ObjectId ciId = getDefaultPaiKeTerm(entry.getSchoolId()).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId,gradeId,ciId,1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
        // jxb add
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(schoolId, gradeId, ciId,acClassType);
		Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(ciId,schoolId,gradeId);
		Map<ObjectId,N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomMapByXqGradeMap(ciId,gradeId);
		
		List<List<Map<String, Object>>> retJXBList = new ArrayList<List<Map<String, Object>>>();
		List<List<Map<String, Object>>> retNameList = new ArrayList<List<Map<String, Object>>>();
		for (N33_TimeCombineEntry timeCombineEntry : timeCombineEntryList) {
			List<Map<String,Object>> listName = new ArrayList<Map<String, Object>>();
			List<N33_TimeCombineEntry.ZuHeList> zuHeLists = timeCombineEntry.getZuHeList();
			Collections.sort(zuHeLists, new Comparator<N33_TimeCombineEntry.ZuHeList>() {
				@Override
				public int compare(N33_TimeCombineEntry.ZuHeList o1, N33_TimeCombineEntry.ZuHeList o2) {
					return o1.getSerial() - o2.getSerial();
				}
			});
			Map<String,Object> retMap = new HashMap<String, Object>();
			retMap.put("name",timeCombineEntry.getTagName());
			retMap.put("bqid",timeCombineEntry.getTagId().toString());
			listName.add(retMap);
			retMap = new HashMap<String, Object>();
			String xkName = timeCombineEntry.getXkName();
			if(studentTagEntryMap.get(timeCombineEntry.getTagId()) != null){
				xkName += ("(" + studentTagEntryMap.get(timeCombineEntry.getTagId()).getStudents().size() + "人)");
			}else{
				xkName += "(0人)";
			}
			retMap.put("name",xkName);
			listName.add(retMap);
			retNameList.add(listName);
			List<Map<String,Object>> listJXB = new ArrayList<Map<String, Object>>();
			for (N33_TimeCombineEntry.ZuHeList zuHeList : zuHeLists) {
				Map<String,Object> jxbMap = new HashMap<String, Object>();
				if(zuHeList.getJxbId() == null || "".equals(zuHeList.getJxbId())){
					jxbMap.put("jxbName","选择教学班");
					jxbMap.put("teaName","选择老师");
					jxbMap.put("roomName","选择教室");
				}else{
					N33_JXBEntry jxbEntry = jxbEntryMap.get(zuHeList.getJxbId());
					if(jxbEntry != null){
						jxbMap.put("subId",jxbEntry.getSubjectId().toString());
						jxbMap.put("jxbName",(jxbEntry.getNickName() == null || "".equals(jxbEntry.getNickName())) ? jxbEntry.getName() + "(人数：" + jxbEntry.getStudentIds().size() + ")" : jxbEntry.getNickName() + "(人数：" + jxbEntry.getStudentIds().size() + ")");
					}else{
						jxbMap.put("jxbName","选择教学班");
					}
					if(jxbEntry == null || jxbEntry.getTercherId() == null || "".equals(jxbEntry.getTercherId().toString())){
						jxbMap.put("teaName","选择老师");
					}else{
						jxbMap.put("teaId",jxbEntry.getTercherId().toString());
						jxbMap.put("teaName",teaEntryMap.get(jxbEntry.getTercherId()) == null ? "" : teaEntryMap.get(jxbEntry.getTercherId()).getUserName());
					}
					if(jxbEntry == null || jxbEntry.getClassroomId() == null || "".equals(jxbEntry.getClassroomId().toString())){
						jxbMap.put("roomName","选择教室");
					}else{
						jxbMap.put("rid",jxbEntry.getClassroomId().toString());
						jxbMap.put("roomName",classroomEntryMap.get(jxbEntry.getClassroomId()) == null ? "" : classroomEntryMap.get(jxbEntry.getClassroomId()).getRoomName());
					}
					jxbMap.put("jxbId",zuHeList.getJxbId().toString());
				}
				jxbMap.put("serial",zuHeList.getSerial());
				jxbMap.put("tagId",timeCombineEntry.getTagId().toString());
				listJXB.add(jxbMap);
			}
			retJXBList.add(listJXB);
		}
		returnMap.put("listName",retNameList);
		returnMap.put("jxbList",retJXBList);
		return returnMap;
	}

	/**
	 * 查询分时间段组合
	 * @param schoolId
	 * @param gradeId
	 * @param ciId
	 * @return
	 */
	public Map<String, Object> getTimeCombineInfos(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		List<Map<String,Object>> timeCombList = new ArrayList<Map<String,Object>>();
		List<N33_TimeCombineEntry> entries = combineDao.getTimeCombine(ciId, schoolId, gradeId);
		// jxb add 
        //ObjectId ciId = getDefaultPaiKeTerm(entry.getSchoolId()).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId,gradeId,ciId,1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
        // jxb add

		Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);

		Map<Integer, Map<Integer, Integer>> typeSerailCountMap = timeCombKeBiaoService.getTimeCombKeShiMap(schoolId, gradeId, ciId);

		List<Integer> djSerials = new ArrayList<Integer>();
		Map<Integer, Map<String, Object>> djMap = new HashMap<Integer, Map<String, Object>>();

		List<Integer> hgSerials = new ArrayList<Integer>();
		Map<Integer, Map<String, Object>> hgMap = new HashMap<Integer, Map<String, Object>>();

		for(N33_TimeCombineEntry entry:entries){
			List<N33_TimeCombineEntry.ZuHeList> zuHeLists = entry.getZuHeList();
			Collections.sort(zuHeLists, new Comparator<N33_TimeCombineEntry.ZuHeList>() {
				@Override
				public int compare(N33_TimeCombineEntry.ZuHeList o1, N33_TimeCombineEntry.ZuHeList o2) {
					return o1.getSerial() - o2.getSerial();
				}
			});

			for (N33_TimeCombineEntry.ZuHeList zuHeList : zuHeLists) {
				int jxbKeShi = 0;
				if(null!=zuHeList.getJxbId()){
					N33_JXBEntry jxb = jxbMap.get(zuHeList.getJxbId());
					if(null!=jxb){
						jxbKeShi = jxb.getJXBKS();
					}
				}
				String zuHeName = "";
				Map<String, Object> zhMap = null;
				if(entry.getType() == 1){
					if(!djSerials.contains(zuHeList.getSerial())){
						djSerials.add(zuHeList.getSerial());
					}
					zuHeName = "等级-时段组合" + zuHeList.getSerial();
					zhMap = djMap.get(zuHeList.getSerial());
				}else{
					if(!hgSerials.contains(zuHeList.getSerial())){
						hgSerials.add(zuHeList.getSerial());
					}
					zuHeName = "合格-时段组合" + zuHeList.getSerial();
					zhMap = hgMap.get(zuHeList.getSerial());
				}

				if(null==zhMap){
					zhMap = new HashMap<String, Object>();
					zhMap.put("name", zuHeName);
					zhMap.put("serial", zuHeList.getSerial());
					zhMap.put("maxKeShi", jxbKeShi);
				}else{
					int maxKeShi = zhMap.get("maxKeShi")==null?0:(Integer) zhMap.get("maxKeShi");
					if(jxbKeShi>maxKeShi){
						zhMap.put("maxKeShi", jxbKeShi);
					}
				}
				zhMap.put("type", entry.getType());
				if(entry.getType() == 1) {
					djMap.put(zuHeList.getSerial(), zhMap);
				}else{
					hgMap.put(zuHeList.getSerial(), zhMap);
				}
			}
		}
		for(Integer djSerial:djSerials){
			Map<String, Object> dj = djMap.get(djSerial);
			if(null!=dj){
				timeCombList.add(dj);
			}
		}

		for(Integer hgSerial:hgSerials){
			Map<String, Object> hg = hgMap.get(hgSerial);
			if(null!=hg){
				timeCombList.add(hg);
			}
		}
		int finishTimeComb = 0;
		for(Map<String, Object> timeComb:timeCombList){
			int type = timeComb.get("type")==null?0:(Integer)timeComb.get("type");
			Map<Integer, Integer> serailCountMap = typeSerailCountMap.get(type);
			int finishKeShi = 0;
			if(null!=serailCountMap){
				int serial = timeComb.get("serial")==null?0:(Integer)timeComb.get("serial");
				int serailCount = serailCountMap.get(serial)==null?0:serailCountMap.get(serial);
				finishKeShi = serailCount;
			}
			int maxKeShi = timeComb.get("maxKeShi")==null?0:(Integer)timeComb.get("maxKeShi");
			timeComb.put("finishKeShi", finishKeShi);
			int isFinish = 0;
			if(finishKeShi==maxKeShi){
				isFinish = 1;
				finishTimeComb++;
			}
			timeComb.put("isFinish", isFinish);
		}
		reMap.put("totalTimeComb", timeCombList.size());
		reMap.put("finishTimeComb", finishTimeComb);
		reMap.put("timeCombList", timeCombList);
		return reMap;
	}


	public Map<Integer, List<Map<String, Object>>> getTypeTimeCombineMap(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		Map<Integer, List<Map<String, Object>>> reMap = new HashMap<Integer, List<Map<String, Object>>>();
		// jxb add 
        //ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId, gradeId, ciId, 1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
        // jxb add
		Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);
		List<N33_TimeCombineEntry> entries = combineDao.getTimeCombine(ciId, schoolId, gradeId);
		Map<ObjectId, Integer> jxbKSMap = new HashMap<ObjectId, Integer>();
		for(Map.Entry<ObjectId, N33_JXBEntry> jxb:jxbMap.entrySet()){
			jxbKSMap.put(jxb.getKey(), jxb.getValue().getJXBKS());
		}
		List<Integer> types = new ArrayList<Integer>();
		List<Integer> serials = new ArrayList<Integer>();
		Map<Integer, Map<Integer, Map<String, Object>>> typeSerialMap = new HashMap<Integer, Map<Integer, Map<String, Object>>>();

		for(N33_TimeCombineEntry entry:entries){
			if(!types.contains(entry.getType())){
				types.add(entry.getType());
			}
			Map<Integer, Map<String, Object>> serialMap = typeSerialMap.get(entry.getType());
			if(null==serialMap){
				serialMap = new HashMap<Integer, Map<String, Object>>();
			}
			List<N33_TimeCombineEntry.ZuHeList> zuHeLists = entry.getZuHeList();
			for (N33_TimeCombineEntry.ZuHeList zuHeList : zuHeLists) {
				if(!serials.contains(zuHeList.getSerial())){
					serials.add(zuHeList.getSerial());
				}
				Map<String, Object> serialObj = serialMap.get(zuHeList.getSerial());
				if(null==serialObj){
					serialObj = new HashMap<String, Object>();
				}
				serialObj.put("type", entry.getType());
				serialObj.put("serial", zuHeList.getSerial());
				String zuHeName = "";
				if(entry.getType() == 1){
					zuHeName = "等级-时段组合" + zuHeList.getSerial();
				}else{
					zuHeName = "合格-时段组合" + zuHeList.getSerial();
				}
				serialObj.put("name", zuHeName);
				Map<ObjectId, Integer> serialJxbKSMap = serialObj.get("jxbKSMap")==null?new HashMap<ObjectId, Integer>():(Map<ObjectId, Integer>)serialObj.get("jxbKSMap");
				int maxKeShi = serialObj.get("maxKeShi")==null?0:(Integer) serialObj.get("maxKeShi");
				int jxbKeShi = 0;
				if(null!=zuHeList.getJxbId()){
					jxbKeShi = jxbKSMap.get(zuHeList.getJxbId());
					serialJxbKSMap.put(zuHeList.getJxbId(), jxbKeShi);
					serialObj.put("jxbKSMap", serialJxbKSMap);
				}
				if(jxbKeShi>maxKeShi){
					serialObj.put("maxKeShi", jxbKeShi);
				}
				serialMap.put(zuHeList.getSerial(), serialObj);
			}
			typeSerialMap.put(entry.getType(), serialMap);
		}
		Collections.sort(types);
		Collections.sort(serials);

		for(Integer type:types){
			List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
			Map<Integer, Map<String, Object>> serialMap = typeSerialMap.get(type);
			for(Integer serial:serials){
				Map<String, Object> serialObj = serialMap.get(serial);
				if(null!=serialObj){
					reList.add(serialObj);
				}
			}
			reMap.put(type, reList);
		}
		return reMap;
	}

	/**
	 *
	 * @param schoolId
	 * @param gradeId
	 * @param ciId
	 * @param type
	 * @param serial
	 * @return
	 */
	public Map<String, Object> getTimeCombineInfo(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int type, int serial) {
		Map<String, Object> timeCombInfoMap = new HashMap<String, Object>();
		// jxb add 
        //ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId,gradeId,ciId,1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
        // jxb add
		Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);
		List<N33_TimeCombineEntry> entries = combineDao.getTimeCombineByType(ciId, schoolId, gradeId, type);

		Map<ObjectId, Integer> jxbKSMap = new HashMap<ObjectId, Integer>();
		for(N33_TimeCombineEntry entry:entries) {
			List<N33_TimeCombineEntry.ZuHeList> zuHeList = entry.getZuHeList();
			for (N33_TimeCombineEntry.ZuHeList zuHe : zuHeList) {
				if(serial == zuHe.getSerial()){
					int jxbKeShi = 0;
					if(null!=zuHe.getJxbId()){
						N33_JXBEntry jxb = jxbMap.get(zuHe.getJxbId());
						if(null!=jxb){
							jxbKeShi = jxb.getJXBKS();
							jxbKSMap.put(zuHe.getJxbId(), jxbKeShi);
						}
					}
					String zuHeName = "";
					if(entry.getType() == 1){
						zuHeName = "等级-时段组合" + zuHe.getSerial();
					}else{
						zuHeName = "合格-时段组合" + zuHe.getSerial();
					}
					timeCombInfoMap.put("name", zuHeName);
					int maxKeShi = timeCombInfoMap.get("maxKeShi")==null?0:(Integer) timeCombInfoMap.get("maxKeShi");
					if(jxbKeShi>maxKeShi){
						timeCombInfoMap.put("maxKeShi", jxbKeShi);
					}
				}
			}
		}
		timeCombInfoMap.put("jxbKSMap", jxbKSMap);
		return timeCombInfoMap;
	}


	/**
	 * 查询可以选择的教学班
	 * @param ciId
	 * @param gradeId
	 * @param tagId
	 */
	public List<N33_JXBDTO> querySelectJXB(ObjectId ciId,ObjectId gradeId,ObjectId tagId,Integer type,ObjectId schoolId,Integer serial){
		N33_StudentTagEntry studentTagEntry = studentTagDao.getTagById(tagId);
		// jxb add 
        //ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId,gradeId,ciId,1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
        // jxb add
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(schoolId, gradeId, ciId,acClassType);
		//对应年级对应次的学科
		Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIds(ciId, gradeId, schoolId);
		//标签中学生
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		for (N33_StudentTagEntry.StudentInfoEntry student : studentTagEntry.getStudents()) {
			if (!studentIds.contains(student.getStuId())) {
				studentIds.add(student.getStuId());
			}
		}
		List<ObjectId> subId = new ArrayList<ObjectId>();
		Map<ObjectId, StudentEntry> entryMap = studentDao.getStuMap(studentIds, ciId);
		if (studentTagEntry.getStudents().size() > 0) {
			N33_StudentTagEntry.StudentInfoEntry infoEntry = studentTagEntry.getStudents().get(0);
			if (infoEntry != null) {
				StudentEntry studentEntry = entryMap.get(infoEntry.getStuId());
				subId.add(studentEntry.getSubjectId1());
				subId.add(studentEntry.getSubjectId2());
				subId.add(studentEntry.getSubjectId3());
				if(type == 2){
					List<ObjectId> hgSubIds = new ArrayList<ObjectId>();
					for (N33_KSEntry ksEntry : ksEntryMap.values()) {
						if(ksEntry.getIsZouBan() == 1 && !subId.contains(ksEntry.getSubjectId())){
							hgSubIds.add(ksEntry.getSubjectId());
						}
					}
					subId = hgSubIds;
				}
			}
		}
		List<ObjectId> removeJxbIds = new ArrayList<ObjectId>();
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId,schoolId,gradeId,type);
		for (N33_TimeCombineEntry combineEntry : timeCombineEntryList) {
			for (N33_TimeCombineEntry.ZuHeList zuHeList : combineEntry.getZuHeList()) {
				if(!removeJxbIds.contains(zuHeList.getJxbId()) && zuHeList.getSerial() != serial){
					removeJxbIds.add(zuHeList.getJxbId());
				}
			}
		}
		N33_TimeCombineEntry timeCombineEntry = combineDao.getTimeCombineByTypeAndTagId(ciId, schoolId,gradeId,type,tagId);
		for (N33_TimeCombineEntry.ZuHeList zuHeList : timeCombineEntry.getZuHeList()) {
			if(zuHeList.getJxbId() != null && !"".equals(zuHeList.getJxbId().toString())){
				N33_JXBEntry jxbEntry = jxbEntryMap.get(zuHeList.getJxbId());
				if(zuHeList.getSerial() != serial){
					subId.remove(jxbEntry.getSubjectId());
				}
			}
		}
		List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(schoolId, gradeId, subId, ciId, type,acClassType);
		List<N33_JXBDTO> jxbdtoList = new ArrayList<N33_JXBDTO>();
		for (N33_JXBEntry jxbEntry : jxbEntryList) {
			if(!removeJxbIds.contains(jxbEntry.getID())){
				jxbdtoList.add(new N33_JXBDTO(jxbEntry));
			}
		}
		return jxbdtoList;
	}

	/**
	 * 存储教学班
	 * @param ciId
	 * @param tagId
	 * @param type
	 * @param serial
	 * @param jxbId
	 * @param gradeId
	 * @param sid
	 */
	public void saveJXB(ObjectId ciId,ObjectId tagId,Integer type,Integer serial,ObjectId jxbId,ObjectId gradeId,ObjectId sid){
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		N33_StudentTagEntry tagEntry = studentTagDao.getTagById(tagId);
		List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = tagEntry.getStudents();
		List<ObjectId> addStuIds = new ArrayList<ObjectId>();
		for (N33_StudentTagEntry.StudentInfoEntry infoEntry : studentInfoEntries) {
			if(!jxbEntry.getStudentIds().contains(infoEntry.getStuId())){
				addStuIds.add(infoEntry.getStuId());
			}
		}
		List<ObjectId> stuIds = new ArrayList<ObjectId>();
		stuIds.addAll(jxbEntry.getStudentIds());
		stuIds.addAll(addStuIds);
		jxbEntry.setStudentIds(stuIds);
		jxbDao.updateN33_JXB(jxbEntry);
		N33_TimeCombineEntry timeCombineEntry = combineDao.getTimeCombineByTypeAndTagId(ciId, sid,gradeId,type,tagId);
		for (N33_TimeCombineEntry.ZuHeList zuHeList : timeCombineEntry.getZuHeList()){
			if(serial == zuHeList.getSerial()){
				zuHeList.setJxbId(jxbId);
			}
		}
		combineDao.update(timeCombineEntry);
	}

	/**
	 * 存储教学班
	 * @param schoolId
	 * @param gradeId
	 * @param ciId
	 */
	public String autoTimeCombSaveJXB(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		//教学班
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
		//jxb add			
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(schoolId, gradeId, ciId, acClassType);
		if (jxbEntries.size() == 0) {
			return "请先生成走班教学班之后重试!";
		}
		Map<Integer, N33_ZouBanSetDTO> zouBanSetMap = zouBanSetService.getZouBanSetMap(gradeId, schoolId, ciId);
		int g_volume = 0;
		if(null!=zouBanSetMap.get(3)){
			N33_ZouBanSetDTO volumeSetDto = zouBanSetMap.get(3);
			g_volume = volumeSetDto.getCount();
		}
		if (g_volume == 0) {
			return "请先设置教学班人数上限之后重试!";
		}

		int g_chaZhi=0;
		if(null!=zouBanSetMap.get(4)){
			N33_ZouBanSetDTO chaZhiSetDto = zouBanSetMap.get(4);
			g_chaZhi = chaZhiSetDto.getCount();
		}
		//组合标签集合
		List<Map<String,Object>> tcList = getTimeCombList(schoolId, gradeId, ciId);

		//教学班type 教学班map集合
		Map<Integer, Map<ObjectId, List<N33_JXBEntry>>> typeSubJxbMap = getGradeCiJXB(schoolId, gradeId, ciId);

		//教学班type列表
		List<Integer> typeList = new ArrayList<Integer>();

		//组合标签Map
		Map<String, Map<String, Object>> tcMap = new HashMap<String, Map<String, Object>>();

		//分段列表
		List<Integer> serialList = new ArrayList<Integer>();

		//教学班type 学科 对应的学生数量
		Map<Integer, Map<ObjectId, Integer>> typeSubStuCountMap = new HashMap<Integer, Map<ObjectId, Integer>>();

		//教学班type 分时段 学科 标签集合 Map
		Map<Integer, Map<Integer, Map<ObjectId, List<String>>>> typeSerialSubTcIdsMap = new HashMap<Integer, Map<Integer, Map<ObjectId, List<String>>>>();

		for(Map<String, Object> tc:tcList){
			String tcId = tc.get("id").toString();
			tcMap.put(tcId, tc);
			Integer type = tc.get("type")==null?0:(Integer)tc.get("type");
			//统计 教学班type
			if(!typeList.contains(type)){
				typeList.add(type);
			}
			//统计 学科 对应的学生数量
			Map<ObjectId, Integer> subStuCountMap = typeSubStuCountMap.get(type)==null?new HashMap<ObjectId, Integer>():typeSubStuCountMap.get(type);
			if(null==subStuCountMap){
				subStuCountMap = new HashMap<ObjectId, Integer>();
			}
			Integer stuCount = tc.get("stuCount")==null?0:(Integer)tc.get("stuCount");
			List<ObjectId> subIds = (List<ObjectId>)tc.get("subIds");
			for(ObjectId subId:subIds) {
				Integer subStuCount = subStuCountMap.get(subId)==null?0:subStuCountMap.get(subId);
				subStuCount+=stuCount;
				subStuCountMap.put(subId, subStuCount);
			}
			//统计 教学班type 学科 对应的学生数量
			typeSubStuCountMap.put(type, subStuCountMap);

			//统计 分时段 学科 标签集合 Map
			Map<Integer, Map<ObjectId, List<String>>> serialSubTcIdsMap = typeSerialSubTcIdsMap.get(type);
			if(null==serialSubTcIdsMap){
				serialSubTcIdsMap = new HashMap<Integer, Map<ObjectId, List<String>>>();
			}
			List<Integer> serials = (List<Integer>)tc.get("serialList");
			for(Integer serial:serials){
				if(!serialList.contains(serial)){
					serialList.add(serial);
				}
				//统计 学科 标签集合 Map
				Map<ObjectId, List<String>> subTcIdsMap = serialSubTcIdsMap.get(serial);
				if(null==subTcIdsMap){
					subTcIdsMap = new HashMap<ObjectId, List<String>>();
				}
				for(ObjectId subId:subIds) {
					//统计 标签集合 Map
					List<String> tcIds = subTcIdsMap.get(subId);
					if(null==tcIds){
						tcIds = new ArrayList<String>();
					}
					if(!tcIds.contains(tcId)){
						tcIds.add(tcId);
					}
					subTcIdsMap.put(subId, tcIds);
				}
				serialSubTcIdsMap.put(serial, subTcIdsMap);
			}
			typeSerialSubTcIdsMap.put(type, serialSubTcIdsMap);
		}

		Collections.sort(typeList);
		for(Integer type:typeList){
			//取得教学班类型下 学科 对应的学生数量Map
			Map<ObjectId, Integer> subStuCountMap = typeSubStuCountMap.get(type);
			if(null==subStuCountMap){
				continue;
			}
			//取得教学班类型下 教学班Map
			Map<ObjectId, List<N33_JXBEntry>> subJxbsMap = typeSubJxbMap.get(type);
			if(null==subJxbsMap){
				continue;
			}

			//学科教学班学生密度统计列表
			List<Map<String, Object>> subMiDuList = new ArrayList<Map<String, Object>>();
			for (Map.Entry<ObjectId, Integer> entry : subStuCountMap.entrySet()) {
				//学科下 学生数量
				Integer subStuCount = entry.getValue()==null?0:entry.getValue();
				List<N33_JXBEntry> jxbList = subJxbsMap.get(entry.getKey());
				if (null == jxbList||jxbList.size()==0) {
					continue;
				}
				//教学班数量
				int jxbCount = jxbList.size();
				//教学班密度
				double miDu = subStuCount*1.0/jxbCount;
				Map<String, Object> subMiDu = new HashMap<String, Object>();
				subMiDu.put("subId", entry.getKey());
				subMiDu.put("miDu", miDu);
				subMiDu.put("jxbCount", jxbCount);
				subMiDu.put("serialCount", serialList.size());
				subMiDuList.add(subMiDu);
			}
			//根据教学班学生密度排序 高->低
			subMiDuListSort(subMiDuList);

			//分时段 学科 标签集合 Map
			Map<Integer, Map<ObjectId, List<String>>> serialSubTcIdsMap = typeSerialSubTcIdsMap.get(type);
			if(null==serialSubTcIdsMap){
				continue;
			}

			//组合标签使用过的分时段列统计
			Map<String, List<Integer>> tcSerialsMap = new HashMap<String, List<Integer>>();

			//根据教学班学生密度排列分时段组合
			for(Map<String, Object> subMiDu:subMiDuList){
				//取得学科
				ObjectId subId = (ObjectId)subMiDu.get("subId");
				//取得学科 教学班
				List<N33_JXBEntry> jxbList = subJxbsMap.get(subId);
				if (null == jxbList) {
					continue;
				}
				//取得学科 教学班数量
				int jxbCount = jxbList.size();

				//教学班map
				Map<ObjectId, N33_JXBEntry> jxbMap = new HashMap<ObjectId, N33_JXBEntry>();
				for(N33_JXBEntry jxb:jxbList){
					jxbMap.put(jxb.getID(), jxb);
				}

				//分时段组合列数
				int loopNum = serialList.size();

				//完成的教学班id集合
				List<ObjectId> finshJxbIds = new ArrayList<ObjectId>();

				//完成的组合标签id集合
				List<String> finshTcIds = new ArrayList<String>();

				//分时段组合列 使用过的教学班 Map
				Map<Integer, List<ObjectId>> serialJxbIdsMap = new HashMap<Integer, List<ObjectId>>();

				//教学班需要循环的次数
				int jxbLoopCount = jxbCount % loopNum == 0 ? jxbCount / loopNum : jxbCount / loopNum + 1;

				Double miDu = subMiDu.get("miDu") == null ? 0.0 : (Double) subMiDu.get("miDu");

				int i_miDu = (int)Math.ceil(miDu);

				int volume = g_volume;
				int chaZhi = g_chaZhi;

				if(i_miDu>=volume+chaZhi) {
					volume = i_miDu;
					chaZhi = 0;
				}

				//初始化分时段组合列索引
				int index = 0;

				int maxSerialStuCount = 0;
				Map<Integer, List<Integer>> serialListMap = new HashMap<Integer, List<Integer>>();
				for (int serial:serialList) {
					int serialStuCount = 0;
					//获取当前分时段组合列下 学科 标签集合 Map
					Map<ObjectId, List<String>> subTcIdsMap = serialSubTcIdsMap.get(serial);
					if (null != subTcIdsMap) {
						//获取当前分时段组合列下 所有的组合标签id
						List<String> tcIds = subTcIdsMap.get(subId);
						if (null != tcIds) {
							for (String hbTcId : tcIds) {
								//获取该组合标签 已经使用的 分时段组合列
								List<Integer> tcSerials = tcSerialsMap.get(hbTcId) == null ? new ArrayList<Integer>() : tcSerialsMap.get(hbTcId);
								//判段 当前分时段组合列 没有被占用; 该组合标签是未完成的
								if (!tcSerials.contains(serial) && !finshTcIds.contains(hbTcId)) {
									Map<String, Object> tc = tcMap.get(hbTcId);
									if (null != tc) {
										List<ObjectId> stuIds = (List<ObjectId>) tc.get("stuIds");
										serialStuCount+=stuIds.size();
									}
								}
							}
						}
					}
					if(serialStuCount > maxSerialStuCount){
						maxSerialStuCount=serialStuCount;
						serialListMap.remove(maxSerialStuCount);
						List<Integer> serials = new ArrayList<Integer>();
						serials.add(serial);
						serialListMap.put(maxSerialStuCount, serials);
					}else if(serialStuCount == maxSerialStuCount){
						List<Integer> serials = serialListMap.get(maxSerialStuCount)==null?new ArrayList<Integer>():serialListMap.get(maxSerialStuCount);
						serials.add(serial);
						serialListMap.put(maxSerialStuCount, serials);
					}
				}

				List<Integer> serials = serialListMap.get(maxSerialStuCount);
				if(null==serials){
					continue;
				}else{
					//随机取得一列分时段的索引
					Random random = new Random();
					int tempIndex = random.nextInt(serials.size());
					int selSerial = serials.get(tempIndex);
					index = serialList.indexOf(selSerial);

				}

				//循环 教学班数量循环
				for(int jxbLoop = 0; jxbLoop < jxbCount; jxbLoop++) {
					//分时段组合列数
					for (int loop = 0; loop < loopNum; loop++) {
						//获取当前分时段组合列
						int currSerial = serialList.get(index);

						//获取当前分时段组合列下 学科 标签集合 Map
						Map<ObjectId, List<String>> subTcIdsMap = serialSubTcIdsMap.get(currSerial);

						//获取当前分时段组合列下 可以使用的组合标签id集合
						List<String> kyTcIds = new ArrayList<String>();
						if (null != subTcIdsMap) {
							//获取当前分时段组合列下 所有的组合标签id
							List<String> tcIds = subTcIdsMap.get(subId);
							if (null != tcIds) {
								for (String hbTcId : tcIds) {
									//获取该组合标签 已经使用的 分时段组合列
									List<Integer> tcSerials = tcSerialsMap.get(hbTcId) == null ? new ArrayList<Integer>() : tcSerialsMap.get(hbTcId);

									//判段 当前分时段组合列 没有被占用; 该组合标签是未完成的
									if (!tcSerials.contains(currSerial) && !finshTcIds.contains(hbTcId)) {
										kyTcIds.add(hbTcId);
									}
								}
							}
						}
						//判段 当前分时段组合列 可以使用的组合标签id数量是否为零
						if (kyTcIds.size()==0) {
							//为零 到下一分时段组合列
							index++;
							//判断分时段组合列索引是否 到底
							if (index > serialList.size() - 1) {
								// 到底 置为零
								index = 0;
							}
							continue;
						}
						//可以合为一个标签的班级id集合
						List<String> hbTcIdList = new ArrayList<String>();
						heBingTimeCombIds(tcMap, kyTcIds, volume, chaZhi, hbTcIdList);

						//判断是否存在可以合为一个标签的班级id集合
						if (hbTcIdList.size() > 0) {  //存在可以合为一个标签的班级id集合

							//获取组合标签下的 学生id集合
							List<ObjectId> addStuIds = new ArrayList<ObjectId>();
							for (String hbTcId : hbTcIdList) {
								Map<String, Object> tc = tcMap.get(hbTcId);
								if (null == tc) {
									continue;
								}
								List<ObjectId> stuIds = (List<ObjectId>) tc.get("stuIds");
								addStuIds.addAll(stuIds);
							}
							//获取符合条件的教学班id
							ObjectId jxbId = null;
							for (N33_JXBEntry jxb : jxbList) {
								if (finshJxbIds.contains(jxb.getID())) {
									continue;
								}
								List<ObjectId> jxbStuIds = jxb.getStudentIds();
								for (ObjectId stuId : addStuIds) {
									if (!jxbStuIds.contains(stuId)) {
										jxbStuIds.add(stuId);
									}
								}
								int jxbStuCount = jxbStuIds.size();
								Boolean chuangJian = false;
								if (jxbStuCount > volume) {
									chuangJian = jxbStuCount - volume <= chaZhi;
								} else if (jxbStuCount == volume) {
									chuangJian = true;
								} else if (jxbStuCount < volume) {
									chuangJian = volume - jxbStuCount <= chaZhi;
								}
								if (chuangJian) {
									jxb.setStudentIds(jxbStuIds);
									jxbDao.updateN33_JXB(jxb);
									jxbId = jxb.getID();
									jxbMap.put(jxb.getID(), jxb);
									break;
								}
							}

							//完成教学班的分时段列对应关系
							if (null != jxbId) {
								if (!finshJxbIds.contains(jxbId)) {
									finshJxbIds.add(jxbId);
								}
								for (String hbTcId : hbTcIdList) {
									Map<String, Object> tc = tcMap.get(hbTcId);
									if (null != tc) {
										String tcId = tc.get("id").toString();
										N33_TimeCombineEntry entry = (N33_TimeCombineEntry) tc.get("entry");
										List<N33_TimeCombineEntry.ZuHeList> zuHeList = entry.getZuHeList();
										for (N33_TimeCombineEntry.ZuHeList zuHe : zuHeList) {
											if (currSerial == zuHe.getSerial()) {
												zuHe.setJxbId(jxbId);
												List<Integer> tcSerials = tcSerialsMap.get(tcId) == null ? new ArrayList<Integer>() : tcSerialsMap.get(tcId);
												if (!tcSerials.contains(currSerial)) {
													tcSerials.add(currSerial);
													tcSerialsMap.put(tcId, tcSerials);
												}
												List<ObjectId> serialJxbIds = serialJxbIdsMap.get(currSerial) == null ? new ArrayList<ObjectId>() : serialJxbIdsMap.get(currSerial);
												if (!serialJxbIds.contains(jxbId)) {
													serialJxbIds.add(jxbId);
													serialJxbIdsMap.put(currSerial, serialJxbIds);
												}
												break;
											}
										}
										entry.setZuHeList(zuHeList);
										combineDao.update(entry);
										finshTcIds.add(hbTcId);
									}
								}

								if(jxbList.size()==1){
									List<ObjectId> serialJxbIds = serialJxbIdsMap.get(currSerial);
									for (String kyTcId : kyTcIds) {
										if (!finshTcIds.contains(kyTcId)) {
											List<Integer> tcSerials = tcSerialsMap.get(kyTcId) == null ? new ArrayList<Integer>() : tcSerialsMap.get(kyTcId);
											if (!tcSerials.contains(currSerial)){
												Map<String, Object> tc = tcMap.get(kyTcId);
												if (null != tc) {
													N33_JXBEntry jxb = jxbMap.get(jxbId);
													List<ObjectId> newStuIds = new ArrayList<ObjectId>();
													List<ObjectId> stuIds = (List<ObjectId>) tc.get("stuIds");
													newStuIds.addAll(stuIds);
													List<ObjectId> jxbStuIds = jxb.getStudentIds() == null ? new ArrayList<ObjectId>() : jxb.getStudentIds();
													for (ObjectId stuId : newStuIds) {
														if (!jxbStuIds.contains(stuId)) {
															jxbStuIds.add(stuId);
														}
													}
													jxb.setStudentIds(jxbStuIds);
													jxbDao.updateN33_JXB(jxb);
													jxbMap.put(jxb.getID(), jxb);
													N33_TimeCombineEntry entry = (N33_TimeCombineEntry) tc.get("entry");
													List<N33_TimeCombineEntry.ZuHeList> zuHeList = entry.getZuHeList();
													for (N33_TimeCombineEntry.ZuHeList zuHe : zuHeList) {
														if (currSerial == zuHe.getSerial()) {
															zuHe.setJxbId(jxbId);
															if (!tcSerials.contains(currSerial)) {
																tcSerials.add(currSerial);
																tcSerialsMap.put(kyTcId, tcSerials);
															}
															if (!serialJxbIds.contains(jxbId)) {
																serialJxbIds.add(jxbId);
																serialJxbIdsMap.put(currSerial, serialJxbIds);
															}
															break;
														}
													}
													entry.setZuHeList(zuHeList);
													combineDao.update(entry);
													finshTcIds.add(kyTcId);
												}
											}
										}
									}
								}
							}
						}
						//完成当前分时段列, 转的下一列
						index++;
						//判断分时段组合列索引是否 到底
						if (index > serialList.size() - 1) {
							// 到底 置为零
							index = 0;
						}
					}
				}

				List<Map<String, Integer>> serialStuCountList = new ArrayList<Map<String, Integer>>();
				for (int serial:serialList) {
					int stuCount = 0;
					if (null != serialSubTcIdsMap.get(serial)) {
						//获取当前分时段组合列下 学科 标签集合 Map
						Map<ObjectId, List<String>> tcIdsMap = serialSubTcIdsMap.get(serial)==null?new HashMap<ObjectId, List<String>>():serialSubTcIdsMap.get(serial);
						//获取当前分时段组合列下 所有的组合标签id
						List<String> tcIds = tcIdsMap.get(subId)==null?new ArrayList<String>():tcIdsMap.get(subId);
						for (String tcId : tcIds) {
							//获取该组合标签 已经使用的 分时段组合列
							List<Integer> tcSerials = tcSerialsMap.get(tcId) == null ? new ArrayList<Integer>() : tcSerialsMap.get(tcId);
							//判段 当前分时段组合列 没有被占用; 该组合标签是未完成的
							if (!tcSerials.contains(serial) && !finshTcIds.contains(tcId)) {
								Map<String, Object> tc = tcMap.get(tcId);
								if (null != tc) {
									if(null!=tc.get("stuIds")){
										List<ObjectId> stuIds = (List<ObjectId>) tc.get("stuIds");
										stuCount+=stuIds.size();
									}
								}
							}
						}
					}
					Map<String, Integer> serialStuCount = new HashMap<String, Integer>();
					serialStuCount.put("serial", serial);
					serialStuCount.put("stuCount", stuCount);
					serialStuCountList.add(serialStuCount);
				}
				serialStuCountListSort(serialStuCountList);
				for(Map<String, Integer> serialStuCount: serialStuCountList){
					int currSerial = serialStuCount.get("serial");
					//获取当前分时段组合列下 学科 标签集合 Map
					Map<ObjectId, List<String>> subTcIdsMap = serialSubTcIdsMap.get(currSerial)==null?new HashMap<ObjectId, List<String>>():serialSubTcIdsMap.get(currSerial);

					//获取当前分时段组合列下 可以使用的组合标签id集合
					List<String> syTcIds = new ArrayList<String>();
					if (null != subTcIdsMap) {
						//获取当前分时段组合列下 所有的组合标签id
						if(null!=subTcIdsMap.get(subId)) {
							for (String tcId : subTcIdsMap.get(subId)) {
								//获取该组合标签 已经使用的 分时段组合列
								List<Integer> tcSerials = tcSerialsMap.get(tcId) == null ? new ArrayList<Integer>() : tcSerialsMap.get(tcId);

								//判段 当前分时段组合列 没有被占用; 该组合标签是未完成的
								if (!tcSerials.contains(currSerial) && !finshTcIds.contains(tcId)) {
									syTcIds.add(tcId);
								}
							}
						}
					}
					//判段 当前分时段组合列 可以使用的组合标签id数量是否为零
					if (syTcIds.size()==0) {
						//为零 到下一分时段组合列
						continue;
					}

					for (N33_JXBEntry jxb : jxbList) {
						if (finshJxbIds.contains(jxb.getID())) {
							continue;
						}
						for (String tcId : syTcIds) {
							List<Integer> tcSerials = tcSerialsMap.get(tcId) == null ? new ArrayList<Integer>() : tcSerialsMap.get(tcId);
							List<ObjectId> serialJxbIds = serialJxbIdsMap.get(currSerial) == null ? new ArrayList<ObjectId>() : serialJxbIdsMap.get(currSerial);
							if (!tcSerials.contains(currSerial) && !finshTcIds.contains(tcId)) {
								Map<String, Object> nHbTc = tcMap.get(tcId);
								if (null != nHbTc) {
									List<ObjectId> newStuIds = new ArrayList<ObjectId>();
									if(null!=nHbTc.get("stuIds")) {
										newStuIds.addAll((List<ObjectId>) nHbTc.get("stuIds"));
									}
									List<ObjectId> jxbStuIds = jxb.getStudentIds() == null ? new ArrayList<ObjectId>() : jxb.getStudentIds();
									for (ObjectId stuId : newStuIds) {
										if (!jxbStuIds.contains(stuId)) {
											jxbStuIds.add(stuId);
										}
									}
									int jxbStuCount = jxbStuIds.size();
									boolean chuangJian = false;
									if (jxbStuCount >= volume) {
										chuangJian = jxbStuCount - volume <= chaZhi;
									} else if (jxbStuCount < volume) {
										chuangJian = true;
									}
									if (chuangJian) {
										jxb.setStudentIds(jxbStuIds);
										jxbDao.updateN33_JXB(jxb);
										jxbMap.put(jxb.getID(), jxb);
										if (!finshJxbIds.contains(jxb.getID())) {
											finshJxbIds.add(jxb.getID());
										}
										N33_TimeCombineEntry entry = (N33_TimeCombineEntry) nHbTc.get("entry");
										List<N33_TimeCombineEntry.ZuHeList> zuHeList = entry.getZuHeList();
										for (N33_TimeCombineEntry.ZuHeList zuHe : zuHeList) {
											if (currSerial == zuHe.getSerial()) {
												zuHe.setJxbId(jxb.getID());
												if (!tcSerials.contains(currSerial)) {
													tcSerials.add(currSerial);
													tcSerialsMap.put(tcId, tcSerials);
												}
												if (!serialJxbIds.contains(jxb.getID())) {
													serialJxbIds.add(jxb.getID());
													serialJxbIdsMap.put(currSerial, serialJxbIds);
												}
												break;
											}
										}
										entry.setZuHeList(zuHeList);
										combineDao.update(entry);
										finshTcIds.add(tcId);
									}
								}
							}
						}
						break;
					}

					List<ObjectId> serialJxbIds = serialJxbIdsMap.get(currSerial);
					//处理零散组合标签
					if (null != serialJxbIds) {
						for (ObjectId serialJxbId : serialJxbIds) {
							N33_JXBEntry jxb = jxbMap.get(serialJxbId);
							if (jxb.getStudentIds().size() < volume + chaZhi) {
								for (String hbTcId : syTcIds) {
									List<Integer> tcSerials = tcSerialsMap.get(hbTcId) == null ? new ArrayList<Integer>() : tcSerialsMap.get(hbTcId);
									if (!tcSerials.contains(currSerial) && !finshTcIds.contains(hbTcId)) {
										Map<String, Object> nHbTc = tcMap.get(hbTcId);
										if (null != nHbTc) {
											List<ObjectId> newStuIds = new ArrayList<ObjectId>();
											List<ObjectId> stuIds = (List<ObjectId>) nHbTc.get("stuIds");
											newStuIds.addAll(stuIds);
											List<ObjectId> jxbStuIds = jxb.getStudentIds() == null ? new ArrayList<ObjectId>() : jxb.getStudentIds();
											for (ObjectId stuId : newStuIds) {
												if (!jxbStuIds.contains(stuId)) {
													jxbStuIds.add(stuId);
												}
											}
											int jxbStuCount = jxbStuIds.size();
											boolean chuangJian = false;
											if (jxbStuCount >= volume) {
												chuangJian = jxbStuCount - volume <= chaZhi;
											} else if (jxbStuCount < volume) {
												chuangJian = true;
											}
											if (chuangJian) {
												jxb.setStudentIds(jxbStuIds);
												jxbDao.updateN33_JXB(jxb);
												jxbMap.put(jxb.getID(), jxb);
												N33_TimeCombineEntry entry = (N33_TimeCombineEntry) nHbTc.get("entry");
												List<N33_TimeCombineEntry.ZuHeList> zuHeList = entry.getZuHeList();
												for (N33_TimeCombineEntry.ZuHeList zuHe : zuHeList) {
													if (currSerial == zuHe.getSerial()) {
														zuHe.setJxbId(serialJxbId);
														if (!tcSerials.contains(currSerial)) {
															tcSerials.add(currSerial);
															tcSerialsMap.put(hbTcId, tcSerials);
														}
														if (!serialJxbIds.contains(serialJxbId)) {
															serialJxbIds.add(serialJxbId);
															serialJxbIdsMap.put(currSerial, serialJxbIds);
														}
														break;
													}
												}
												entry.setZuHeList(zuHeList);
												combineDao.update(entry);
												finshTcIds.add(hbTcId);
											}
										}
									}
								}
							}
						}
					}
				}
			}


			//============================ conn add 19-06-05=========================================
			List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId,schoolId,gradeId,type);
			// jxb add 
	        //ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
	       
			Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);
			//Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(ciId,schoolId,gradeId);

			List ejxbList = new ArrayList();//等级时段组合下的所有教学班
			List allTeaList = new ArrayList();//等级时段组合下的所有教师

			List eTea1List = new ArrayList();//等级时段组合1下的所有教师
			List eRoom1List = new ArrayList();//等级时段组合1中已经选中的教室

			List eTea2List = new ArrayList();//等级时段组合2下的所有所有教师
			List eRoom2List = new ArrayList();//等级时段组合2中已经选中的教室

			List eTea3List = new ArrayList();//等级时段组合3下的所有所有教师
			List eRoom3List = new ArrayList();//等级时段组合3中已经选中的教室

			for(N33_TimeCombineEntry timeComEntry : timeCombineEntryList){
				List<ZuHeList> zuheList = timeComEntry.getZuHeList();
				for(ZuHeList zuheEntry : zuheList){
					N33_JXBEntry jxbEntry = jxbEntryMap.get(zuheEntry.getJxbId());
					if(zuheEntry.getSerial() == 1){//等级时段1
						if(jxbEntry != null && !ejxbList.contains(jxbEntry.getID())){
							ejxbList.add(jxbEntry.getID());
							//添加教师
							ObjectId subjectId = jxbEntry.getSubjectId();
							List<N33_TeaEntry> teaList = teaDao.getTeaListByGradeId(ciId, schoolId, gradeId, subjectId);
							boolean flag = false;
							for(N33_TeaEntry teaEntry : teaList){
								if(!eTea1List.contains(teaEntry.getUserId())){//保证老师不会在同一个时间点同时上多个班的课
									if(!allTeaList.contains(teaEntry.getUserId())){//教师平均 不然一个科目的的课就有可能是一个教师全部都上了
										//教师的数量大于教学班的数量  教师平均分配教学班
										eTea1List.add(teaEntry.getUserId());
										allTeaList.add(teaEntry.getUserId());
										jxbEntry.setTercherId(teaEntry.getUserId());
										break;
									}else{
										//教师的数量小于教学班的数量 教师平均分配教学班  本来教师可以教此教学班 但是放到下面就行分配
										flag = true;
										jxbEntry.setTercherId(null);
									}
								}else{
									jxbEntry.setTercherId(null);
								}
							}
							//教师的数量小于教学班的数量 教师平均分配教学班
							if(jxbEntry.getTercherId() == null && flag){
								for(N33_TeaEntry teaEntry : teaList){
									if(!eTea1List.contains(teaEntry.getUserId())){//保证老师不会在同一个时间点同时上多个班的课
										//教师的数量大于教学班的数量  教师平均分配教学班
										eTea1List.add(teaEntry.getUserId());
										jxbEntry.setTercherId(teaEntry.getUserId());
										break;
									}
								}
							}
							//添加教室
							List<N33_ClassroomEntry> classroomList = classroomDao.getRoomEntryListByXqGradeIso(ciId,schoolId,gradeId);
							for(N33_ClassroomEntry clasRomEntry : classroomList){
								if(clasRomEntry.getRoomName().indexOf("操场") == -1 && !eRoom1List.contains(clasRomEntry.getRoomId()) ){
									eRoom1List.add(clasRomEntry.getRoomId());
									jxbEntry.setClassroomId(clasRomEntry.getRoomId());
									break;
								}else{
									jxbEntry.setClassroomId(null);
								}
							}
							jxbDao.updateN33_JXB(jxbEntry);
						}else{
							if(jxbEntry != null && jxbEntry.getTercherId() != null)
								eTea1List.add(jxbEntry.getTercherId());
							if(jxbEntry != null && jxbEntry.getClassroomId() != null)
								eRoom1List.add(jxbEntry.getClassroomId());
						}
					}
					if(zuheEntry.getSerial() == 2){//等级时段2
						if(jxbEntry != null && !ejxbList.contains(jxbEntry.getID())){
							ejxbList.add(jxbEntry.getID());
							//添加教师
							ObjectId subjectId = jxbEntry.getSubjectId();
							List<N33_TeaEntry> teaList = teaDao.getTeaListByGradeId(ciId,schoolId,gradeId, subjectId);
							boolean flag = false;
							for(N33_TeaEntry teaEntry : teaList){
								if(!eTea2List.contains(teaEntry.getUserId())){//保证老师不会在同一个时间点同时上多个班的课
									if(!allTeaList.contains(teaEntry.getUserId())){//教师平均 不然一个科目的的课就有可能是一个教师全部都上了
										//教师的数量大于教学班的数量  教师平均分配教学班
										eTea2List.add(teaEntry.getUserId());
										allTeaList.add(teaEntry.getUserId());
										jxbEntry.setTercherId(teaEntry.getUserId());
										break;
									}else{
										//教师的数量小于教学班的数量 教师平均分配教学班  本来教师可以教此教学班 但是放到下面就行分配
										flag = true;
										jxbEntry.setTercherId(null);
									}
								}else{
									jxbEntry.setTercherId(null);
								}
							}
							//教师的数量小于教学班的数量 教师平均分配教学班
							if(jxbEntry.getTercherId() == null && flag){
								for(N33_TeaEntry teaEntry : teaList){
									if(!eTea2List.contains(teaEntry.getUserId())){//保证老师不会在同一个时间点同时上多个班的课
										//教师的数量大于教学班的数量  教师平均分配教学班
										eTea2List.add(teaEntry.getUserId());
										jxbEntry.setTercherId(teaEntry.getUserId());
										break;
									}
								}
							}
							//添加教室
							List<N33_ClassroomEntry> classroomList = classroomDao.getRoomEntryListByXqGradeIso(ciId,schoolId,gradeId);
							for(N33_ClassroomEntry clasRomEntry : classroomList){
								if(clasRomEntry.getRoomName().indexOf("操场") == -1 && !eRoom2List.contains(clasRomEntry.getRoomId()) ){
									eRoom2List.add(clasRomEntry.getRoomId());
									jxbEntry.setClassroomId(clasRomEntry.getRoomId());
									break;
								}else{
									jxbEntry.setClassroomId(null);
								}
							}
							jxbDao.updateN33_JXB(jxbEntry);
						}else{
							if(jxbEntry != null && jxbEntry.getTercherId() != null)
								eTea2List.add(jxbEntry.getTercherId());
							if(jxbEntry != null && jxbEntry.getClassroomId() != null)
								eRoom2List.add(jxbEntry.getClassroomId());
						}
					}
					if(zuheEntry.getSerial() == 3){//等级时段3
						if(jxbEntry != null&& !ejxbList.contains(jxbEntry.getID())){
							ejxbList.add(jxbEntry.getID());
							//添加教师
							ObjectId subjectId = jxbEntry.getSubjectId();
							List<N33_TeaEntry> teaList = teaDao.getTeaListByGradeId(ciId,schoolId,gradeId, subjectId);
							boolean flag = false;
							for(N33_TeaEntry teaEntry : teaList){
								if(!eTea3List.contains(teaEntry.getUserId())){//保证老师不会在同一个时间点同时上多个班的课
									if(!allTeaList.contains(teaEntry.getUserId())){//教师平均 不然一个科目的的课就有可能是一个教师全部都上了
										//教师的数量大于教学班的数量  教师平均分配教学班
										eTea3List.add(teaEntry.getUserId());
										allTeaList.add(teaEntry.getUserId());
										jxbEntry.setTercherId(teaEntry.getUserId());
										break;
									}else{
										//教师的数量小于教学班的数量 教师平均分配教学班  本来教师可以教此教学班 但是放到下面就行分配
										flag = true;
										jxbEntry.setTercherId(null);
									}
								}else{
									jxbEntry.setTercherId(null);
								}
							}
							//教师的数量小于教学班的数量 教师平均分配教学班
							if(jxbEntry.getTercherId() == null && flag){
								for(N33_TeaEntry teaEntry : teaList){
									if(!eTea3List.contains(teaEntry.getUserId())){//保证老师不会在同一个时间点同时上多个班的课
										//教师的数量大于教学班的数量  教师平均分配教学班
										eTea3List.add(teaEntry.getUserId());
										jxbEntry.setTercherId(teaEntry.getUserId());
										break;
									}
								}
							}
							//添加教室
							List<N33_ClassroomEntry> classroomList = classroomDao.getRoomEntryListByXqGradeIso(ciId,schoolId,gradeId);
							for(N33_ClassroomEntry clasRomEntry : classroomList){
								if(clasRomEntry.getRoomName().indexOf("操场") == -1 && !eRoom3List.contains(clasRomEntry.getRoomId()) ){
									eRoom3List.add(clasRomEntry.getRoomId());
									jxbEntry.setClassroomId(clasRomEntry.getRoomId());
									break;
								}else{
									jxbEntry.setClassroomId(null);
								}
							}
							jxbDao.updateN33_JXB(jxbEntry);
						}else{
							if(jxbEntry != null && jxbEntry.getTercherId() != null)
								eTea3List.add(jxbEntry.getTercherId());
							if(jxbEntry != null && jxbEntry.getClassroomId() != null)
								eRoom3List.add(jxbEntry.getClassroomId());
						}
					}
				}
			}
			//============================ conn add 19-06-05=========================================
		}
		return "ok";
	}

	/**
	 * 根据教学班学生密度排序 高->低
	 * @param subMiDuList
	 */
	public void subMiDuListSort(List<Map<String, Object>> subMiDuList) {
		Collections.sort(subMiDuList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
				int jxbCount0 = arg0.get("jxbCount")==null?0:(Integer) arg0.get("jxbCount");
				int serialCount0 = arg0.get("serialCount")==null?0:(Integer) arg0.get("serialCount");

				int jxbCount1 = arg1.get("jxbCount")==null?0:(Integer)arg1.get("jxbCount");
				int serialCount1 = arg1.get("serialCount")==null?0:(Integer)arg1.get("serialCount");

				Double miDu0 = arg0.get("miDu") == null ? 0.0 : (Double) arg0.get("miDu");
				Double miDu1 = arg1.get("miDu") == null ? 0.0 : (Double) arg1.get("miDu");
				if(serialCount0>jxbCount0||serialCount1>jxbCount1){
					if(jxbCount0==jxbCount1){
						return miDu1.compareTo(miDu0);
					}else {
						return jxbCount0 - jxbCount1;
					}
				}else {
					return miDu1.compareTo(miDu0);
				}
			}
		});
	}

	/**
	 * 根据教学班学生密度排序 高->低
	 * @param serialStuCountList
	 */
	public void serialStuCountListSort(List<Map<String, Integer>> serialStuCountList) {
		Collections.sort(serialStuCountList, new Comparator<Map<String, Integer>>() {
			@Override
			public int compare(Map<String, Integer> arg0, Map<String, Integer> arg1) {
				int stuCount0 = arg0.get("stuCount")==null?0:(Integer) arg0.get("stuCount");
				int stuCount1 = arg1.get("stuCount")==null?0:(Integer)arg1.get("stuCount");
				return stuCount1 - stuCount0;
			}
		});
	}

	/**
	 * 合为一个标签的班级id集合
	 * @param tcIds 分段班级集合
	 * @param volume 容量
	 * @param chaZhi 差值
	 * @return
	 */
	public void heBingTimeCombIds(Map<String, Map<String, Object>> tcMap, List<String> tcIds, Integer volume, Integer chaZhi, List<String> hbTcIdList) {
		if(chaZhi>1) {
			//取符合容量+浮动的组合标签id
			for (int cursor = chaZhi; cursor >= chaZhi*-1; cursor--) {
				try {
					heBingTimeHandle(tcMap, tcIds, tcIds.size(), volume, cursor, hbTcIdList, new ArrayList<String>());
				} catch (StopMsgException e) {
				}finally {
					if(hbTcIdList.size()>0){
						break;
					}
				}
			}
		}else {
			try {
				//取等于容量
				heBingTimeHandle(tcMap, tcIds, tcIds.size(), volume, chaZhi, hbTcIdList, new ArrayList<String>());
			} catch (StopMsgException e) {
			}

		}
	}

	/**
	 * 合为一个标签的班级id集合 处理逻辑
	 * @param tcMap
	 * @param tcIds
	 * @param tcsSize
	 * @param volume
	 * @param chaZhi
	 * @param hbTcIdList
	 * @param hbTcIds
	 * @return
	 */
	public List<String> heBingTimeHandle(Map<String, Map<String, Object>> tcMap, List<String> tcIds, int tcsSize, Integer volume, Integer chaZhi, List<String> hbTcIdList, List<String> hbTcIds) {
		boolean isEnd = true;
		for(String tcId:tcIds){
			if(hbTcIdList.contains(tcId)){
				continue;
			}
			if(!hbTcIds.contains(tcId)) {
				isEnd = false;
				//对应选课的学生人数
				int stuCount = 0;
				//取的班级信息
				Map<String, Object> tc = tcMap.get(tcId);
				if(null!=tc) {
					//对应选课的学生人数
					stuCount = (Integer)tc.get("stuCount");
				}

				//上一次递归的班级学生总数
				int prveClsCount = 0;
				for(String hbTcId:hbTcIds){
					//取的班级信息
					Map<String, Object> ptc = tcMap.get(hbTcId);
					if(null!=ptc) {
						//对应选课的学生人数
						int pstuCount = (Integer)ptc.get("stuCount");
						prveClsCount += pstuCount;
					}
				}

				boolean chuangJian = false;
				int stuAllCount = stuCount + prveClsCount;

				if (stuAllCount >= volume) {
					chuangJian = stuAllCount - volume == chaZhi;
					if(!chuangJian){
						isEnd = true;
						continue;
					}
				}
				/*else if (stuAllCount == volume) {
					chuangJian = true;
				}else if (stuAllCount < volume) {
					chuangJian = volume - stuAllCount <= chaZhi;
				}*/
				if (stuAllCount < volume) {
					if (!chuangJian) {
						hbTcIds.add(tcId);
					}
				}
				if (chuangJian) {
					hbTcIds.add(tcId);
					for(String hbClassId:hbTcIds){
						hbTcIdList.add(hbClassId);
					}
					//找到结果 提前结束递归
					throw new StopMsgException();
				}else{
					if(tcIds.size()>1) {
						hbTcIds = heBingTimeHandle(tcMap, tcIds.subList(1, tcIds.size()), tcsSize, volume, chaZhi, hbTcIdList, hbTcIds);
						if(hbTcIds.size()==0){
							if(tcsSize>tcIds.size()){
								return hbTcIds;
							}
						}
					}else{
						hbTcIds = new ArrayList<String>();
					}
				}
			}
		}
		if(isEnd){
			hbTcIds = new ArrayList<String>();
		}
		return hbTcIds;
	}

	/**
	 * 自动排课专用，其他功能勿用！！！！！
	 * 查询可以选择的教学班
	 * @param schoolId
	 * @param gradeId
	 * @param ciId
	 */
	public Map<Integer, Map<ObjectId, List<N33_JXBEntry>>> getGradeCiJXB(ObjectId schoolId, ObjectId gradeId, ObjectId ciId){
        Map<Integer, Map<ObjectId, List<N33_JXBEntry>>> reMap = new HashMap<Integer, java.util.Map<ObjectId, List<N33_JXBEntry>>>();
      //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add			
        List<N33_JXBEntry> entries = jxbDao.getJXBList(schoolId, gradeId, ciId,acClassType);
		for(N33_JXBEntry entry:entries){
			if(entry.getType()==1||entry.getType()==2) {
				if (entry.getStudentIds().size() > 0) {
					entry.setStudentIds(new ArrayList<ObjectId>());
					jxbDao.updateN33_JXB(entry);
				}
			}
            Map<ObjectId, List<N33_JXBEntry>> subMap = reMap.get(entry.getType());
            if(null==subMap){
                subMap = new HashMap<ObjectId, List<N33_JXBEntry>>();
            }
            List<N33_JXBEntry> jxbs = subMap.get(entry.getSubjectId());
            if(null==jxbs){
                jxbs = new ArrayList<N33_JXBEntry>();
            }
            jxbs.add(entry);
            subMap.put(entry.getSubjectId(), jxbs);
            reMap.put(entry.getType(), subMap);
        }
		return reMap;
	}

	/**
	 * 查询分时间段组合的头部名称
	 * @param gradeId
	 * @param ciId
	 * @param schoolId
	 * @return
	 */
	public List<Map<String,Object>> getTimeCombList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId){

		//对应年级对应次的学科
		Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIds(ciId, gradeId, schoolId);

        Map<ObjectId, List<ObjectId>> stuTagStuIdsMap = new HashMap<ObjectId, List<ObjectId>>();

		Map<ObjectId, Map<Integer, List<ObjectId>>> stuTagSubIdsMap = new HashMap<ObjectId, Map<Integer, List<ObjectId>>>();

		List<N33_StudentTagEntry> stuTagEntries = studentTagDao.getTagList(ciId, gradeId);

		//标签中学生
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		for(N33_StudentTagEntry entry:stuTagEntries){
            List<ObjectId> stuIds = new ArrayList<ObjectId>();
			for (N33_StudentTagEntry.StudentInfoEntry student : entry.getStudents()) {
				if (!studentIds.contains(student.getStuId())) {
					studentIds.add(student.getStuId());
				}
                if (!stuIds.contains(student.getStuId())) {
                    stuIds.add(student.getStuId());
                }
			}
            stuTagStuIdsMap.put(entry.getID(), stuIds);
		}

		Map<ObjectId, StudentEntry> entryMap = studentDao.getStuMap(studentIds, ciId);
		for(N33_StudentTagEntry entry:stuTagEntries) {
			List<ObjectId> djSubIds = new ArrayList<ObjectId>();
			List<ObjectId> hgSubIds = new ArrayList<ObjectId>();
			if (studentIds.size() > 0) {
				N33_StudentTagEntry.StudentInfoEntry infoEntry = entry.getStudents().get(0);
				if (infoEntry != null) {
					StudentEntry studentEntry = entryMap.get(infoEntry.getStuId());
					djSubIds.add(studentEntry.getSubjectId1());
					djSubIds.add(studentEntry.getSubjectId2());
					djSubIds.add(studentEntry.getSubjectId3());
					for (N33_KSEntry ksEntry : ksEntryMap.values()) {
						if (ksEntry.getIsZouBan() == 1 && !djSubIds.contains(ksEntry.getSubjectId())) {
							hgSubIds.add(ksEntry.getSubjectId());
						}
					}
				}
			}
			Map<Integer, List<ObjectId>> subIdsMap = new HashMap<Integer, List<ObjectId>>();
			subIdsMap.put(1, djSubIds);
			subIdsMap.put(2, hgSubIds);
			stuTagSubIdsMap.put(entry.getID(), subIdsMap);
		}

        List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombine(ciId, schoolId, gradeId);
		for (N33_TimeCombineEntry entry : timeCombineEntryList) {
            List<ObjectId> subIds = new ArrayList<ObjectId>();
            Map<Integer, List<ObjectId>> subIdsMap = stuTagSubIdsMap.get(entry.getTagId());
            if(null!=subIdsMap){
                subIds = subIdsMap.get(entry.getType())==null?new ArrayList<ObjectId>():subIdsMap.get(entry.getType());
            }
            List<ObjectId> stuIds = stuTagStuIdsMap.get(entry.getTagId())==null?new ArrayList<ObjectId>():stuTagStuIdsMap.get(entry.getTagId());

			List<N33_TimeCombineEntry.ZuHeList> zuHeLists = entry.getZuHeList();
			Collections.sort(zuHeLists, new Comparator<N33_TimeCombineEntry.ZuHeList>() {
				@Override
				public int compare(N33_TimeCombineEntry.ZuHeList o1, N33_TimeCombineEntry.ZuHeList o2) {
					return o1.getSerial() - o2.getSerial();
				}
			});
            entry.setZuHeList(zuHeLists);
            List<Integer> serialList = new ArrayList<Integer>();
			for (N33_TimeCombineEntry.ZuHeList zuHeList : zuHeLists) {
				zuHeList.setJxbId(null);
				serialList.add(zuHeList.getSerial());
			}
			Map<String,Object> retMap = new HashMap<String, Object>();
            retMap.put("id", entry.getID().toString());
			retMap.put("tagId", entry.getTagId().toString());
            retMap.put("type", entry.getType());
            retMap.put("subIds", subIds);
            retMap.put("stuIds", stuIds);
            retMap.put("stuCount", stuIds.size());
			retMap.put("serialList", serialList);
            retMap.put("entry", entry);
            reList.add(retMap);
		}
		return reList;
	}

	/**
	 * 存储教学班
	 * @param ciId
	 * @param tagId
	 * @param type
	 * @param serial
	 * @param gradeId
	 * @param sid
	 */
	public void delJXB(ObjectId ciId,ObjectId tagId,Integer type,Integer serial,ObjectId gradeId,ObjectId sid){
		N33_TimeCombineEntry timeCombineEntry = combineDao.getTimeCombineByTypeAndTagId(ciId, sid,gradeId,type,tagId);
		for (N33_TimeCombineEntry.ZuHeList zuHeList : timeCombineEntry.getZuHeList()){
			if(serial == zuHeList.getSerial()){
				if(zuHeList.getJxbId() != null && !"".equals(zuHeList.getJxbId().toString())){
					N33_JXBEntry jxbEntry = jxbDao.getJXBById(zuHeList.getJxbId());
					if(null!=jxbEntry) {
						List<ObjectId> jxbStuIds = jxbEntry.getStudentIds();
						N33_StudentTagEntry tagEntry = studentTagDao.getTagById(tagId);
						if(null!=tagEntry) {
							List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = tagEntry.getStudents();
							List<ObjectId> removeStuIds = new ArrayList<ObjectId>();
							for (N33_StudentTagEntry.StudentInfoEntry infoEntry : studentInfoEntries) {
								removeStuIds.add(infoEntry.getStuId());
							}
							jxbStuIds.removeAll(removeStuIds);
						}else{
							jxbStuIds = new ArrayList<ObjectId>();
						}
						jxbEntry.setStudentIds(jxbStuIds);
						jxbDao.updateN33_JXB(jxbEntry);
					}
				}
				zuHeList.setJxbId(null);
			}
		}
		combineDao.update(timeCombineEntry);
	}

	/**
	 * 查询供选择的老师
	 * @param sid
	 * @param gradeId
	 * @param subjectId
	 * @param ciId
	 * @param serial
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> getTeacherList(ObjectId sid, ObjectId gradeId, ObjectId subjectId,ObjectId ciId,Integer serial,Integer type) {
		// jxb add 
        //ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,ciId,1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
        // jxb add
		List<String> teaList = new ArrayList<String>();

		Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(sid, gradeId, ciId,acClassType);
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId, sid, gradeId, type);
		for (N33_TimeCombineEntry timeCombineEntry : timeCombineEntryList) {
			for (N33_TimeCombineEntry.ZuHeList zuHeList : timeCombineEntry.getZuHeList()) {
				if(serial == zuHeList.getSerial()){
					if(zuHeList.getJxbId() != null && !"".equals(zuHeList.getJxbId().toString()) ){
						N33_JXBEntry jxbEntry = jxbEntryMap.get(zuHeList.getJxbId());
						if(jxbEntry != null && jxbEntry.getTercherId() != null && !"".equals(jxbEntry.getTercherId().toString())){
							teaList.add(jxbEntry.getTercherId().toString());
						}
					}
				}
			}
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<N33_TeaEntry> teaEntries = teaDao.getTeaListByGradeId(ciId, sid, gradeId, subjectId);
		for (N33_TeaEntry tea : teaEntries) {
			if(!teaList.contains(tea.getUserId().toString())){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", tea.getUserName());
				map.put("id", tea.getUserId().toString());
				result.add(map);
			}
		}
		return result;
	}

	public List<Map<String, Object>> getClassRoomList(ObjectId sid, ObjectId gradeId,Integer serial,Integer type) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
		List<N33_ClassroomEntry> list = classroomDao.getRoomEntryListByXqGrade(ciId, sid, gradeId);
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId,sid,gradeId,type);
		// jxb add 
        //ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,ciId,1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
        // jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(sid, gradeId, ciId, acClassType);
		List<String> roomList = new ArrayList<String>();
		for (N33_TimeCombineEntry timeCombineEntry : timeCombineEntryList) {
			for (N33_TimeCombineEntry.ZuHeList zuHeList : timeCombineEntry.getZuHeList()) {
				if(serial == zuHeList.getSerial()){
					if(zuHeList.getJxbId() != null && !"".equals(zuHeList.getJxbId().toString()) ){
						N33_JXBEntry jxbEntry = jxbEntryMap.get(zuHeList.getJxbId());
						if(jxbEntry != null && jxbEntry.getClassroomId() != null && !"".equals(jxbEntry.getClassroomId().toString())){
							roomList.add(jxbEntry.getClassroomId().toString());
						}
					}
				}
			}
		}
		for (N33_ClassroomEntry classroomEntry : list) {
			if(!roomList.contains(classroomEntry.getRoomId().toString())){
				Map<String, Object> map = new HashMap<String, Object>();
				String className = "";
				if (classroomEntry.getClassName() != null && !classroomEntry.getClassName().equals("null")) {
					className = "(" + classroomEntry.getClassName() + ")";
				}
				map.put("name", classroomEntry.getRoomName() + className);
				map.put("id", classroomEntry.getRoomId().toString());
				result.add(map);
			}
		}
		return result;
	}

	public void delJxbTeacher(ObjectId jxbId){
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		jxbEntry.setTercherId(null);
		jxbDao.updateN33_JXB(jxbEntry);
	}

	public void delJxbRoom(ObjectId jxbId){
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		jxbEntry.setClassroomId(null);
		jxbDao.updateN33_JXB(jxbEntry);
	}

	/**
	 * 清空一列的老师和教室
	 * @param serial
	 * @param type
	 * @param gradeId
	 * @param ciId
	 * @param sid
	 */
	public void clearColumn(Integer serial,Integer type,ObjectId gradeId,ObjectId ciId,ObjectId sid){
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId,sid,gradeId,type);
		for (N33_TimeCombineEntry entry : timeCombineEntryList) {
			for (N33_TimeCombineEntry.ZuHeList zuHeList : entry.getZuHeList()) {
				if(zuHeList.getJxbId() != null && !"".equals(zuHeList.getJxbId().toString()) && serial == zuHeList.getSerial()){
					N33_JXBEntry jxbEntry = jxbDao.getJXBById(zuHeList.getJxbId());
					if(null!=jxbEntry) {
						List<ObjectId> jxbStuIds = jxbEntry.getStudentIds();
						N33_StudentTagEntry tagEntry = studentTagDao.getTagById(entry.getTagId());
						if(null!=tagEntry) {
							List<ObjectId> removeStuIds = new ArrayList<ObjectId>();
							List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = tagEntry.getStudents();
							for (N33_StudentTagEntry.StudentInfoEntry infoEntry : studentInfoEntries) {
								removeStuIds.add(infoEntry.getStuId());
							}
							jxbStuIds.removeAll(removeStuIds);
						} else {
							jxbStuIds = new ArrayList<ObjectId>();
						}
						jxbEntry.setStudentIds(jxbStuIds);
						jxbDao.updateN33_JXB(jxbEntry);
					}
					zuHeList.setJxbId(null);
				}
			}
		}
		combineDao.removeByType(ciId,sid,gradeId,type);
		combineDao.addTimeCombineList(timeCombineEntryList);
	}

	/**
	 * 删除组合
	 * @param serial
	 * @param type
	 * @param gradeId
	 * @param ciId
	 * @param sid
	 */
	public void delZuHe(Integer serial,Integer type,ObjectId gradeId,ObjectId ciId,ObjectId sid){
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId,sid,gradeId,type);
		for (N33_TimeCombineEntry entry : timeCombineEntryList) {
			List<N33_TimeCombineEntry.ZuHeList> newZuHeList = new ArrayList<N33_TimeCombineEntry.ZuHeList>();
			for (N33_TimeCombineEntry.ZuHeList zuHeList : entry.getZuHeList()) {
				if(zuHeList.getSerial() != serial){
					newZuHeList.add(zuHeList);
				}else{
					if(zuHeList.getJxbId() != null) {
						N33_JXBEntry jxbEntry = jxbDao.getJXBById(zuHeList.getJxbId());
						if(null!=jxbEntry) {
							List<ObjectId> jxbStuIds = jxbEntry.getStudentIds();
							N33_StudentTagEntry tagEntry = studentTagDao.getTagById(entry.getTagId());
							if (null != tagEntry) {
								List<ObjectId> removeStuIds = new ArrayList<ObjectId>();
								List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = tagEntry.getStudents();
								for (N33_StudentTagEntry.StudentInfoEntry infoEntry : studentInfoEntries) {
									removeStuIds.add(infoEntry.getStuId());
								}
								jxbStuIds.removeAll(removeStuIds);
							} else {
								jxbStuIds = new ArrayList<ObjectId>();
							}
							jxbEntry.setStudentIds(jxbStuIds);
							jxbDao.updateN33_JXB(jxbEntry);
						}
					}
				}
			}
			entry.setZuHeList(newZuHeList);
		}
		combineDao.removeByType(ciId,sid,gradeId,type);
		combineDao.addTimeCombineList(timeCombineEntryList);
	}

	/**
	 * 增加一个组合
	 * @param type
	 * @param gradeId 
	 * @param ciId
	 * @param sid
	 */
	public void addZuHe(Integer type,ObjectId gradeId,ObjectId ciId,ObjectId sid){
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId,sid,gradeId,type);
		for (N33_TimeCombineEntry timeCombineEntry : timeCombineEntryList) {
			List<N33_TimeCombineEntry.ZuHeList> zuHeLists = timeCombineEntry.getZuHeList();
			List<N33_TimeCombineEntry.ZuHeList> newZuHeList = new ArrayList<N33_TimeCombineEntry.ZuHeList>();
			if(zuHeLists.size() == 0){
				N33_TimeCombineEntry.ZuHeList zuHeList1 = new N33_TimeCombineEntry.ZuHeList();
				zuHeList1.setSerial(1);
				newZuHeList.add(zuHeList1);
				timeCombineEntry.setZuHeList(newZuHeList);
				continue;
			}
			Collections.sort(zuHeLists, new Comparator<N33_TimeCombineEntry.ZuHeList>() {
				@Override
				public int compare(N33_TimeCombineEntry.ZuHeList o1, N33_TimeCombineEntry.ZuHeList o2) {
					return o1.getSerial() - o2.getSerial();
				}
			});
			//记录循环第几次
			Integer count = 0;
			//标记是否已经加入过一个组合，如果加入过则不加了
			boolean flag = true;
			for (N33_TimeCombineEntry.ZuHeList zuHeList : zuHeLists) {
				count ++;
				if((zuHeList.getSerial() != count) && flag){
					newZuHeList.add(zuHeList);
					N33_TimeCombineEntry.ZuHeList zuHeList1 = new N33_TimeCombineEntry.ZuHeList();
					zuHeList1.setSerial(count);
					newZuHeList.add(zuHeList1);
					flag = false;
				}else{
					newZuHeList.add(zuHeList);
				}
			}
			if(flag){
				count = 0;
				newZuHeList = new ArrayList<N33_TimeCombineEntry.ZuHeList>();
				for (N33_TimeCombineEntry.ZuHeList zuHeList : zuHeLists) {
					count ++;
					if(count == zuHeList.getSerial() && count < zuHeLists.size()){
						newZuHeList.add(zuHeList);
					}else{
						newZuHeList.add(zuHeList);
						N33_TimeCombineEntry.ZuHeList zuHeList1 = new N33_TimeCombineEntry.ZuHeList();
						zuHeList1.setSerial(zuHeLists.size() + 1);
						newZuHeList.add(zuHeList1);
					}
				}
			}
			timeCombineEntry.setZuHeList(newZuHeList);
		}
		combineDao.removeByType(ciId,sid,gradeId,type);
		combineDao.addTimeCombineList(timeCombineEntryList);
	}

	/**
	 * 檢查數據完整性
	 * @param gradeId
	 * @param ciId
	 * @param schoolId
	 * @return
	 */
	public Map<String,Object> checkZuHeData(ObjectId gradeId,ObjectId ciId,ObjectId schoolId){
		// jxb add 
        //ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId,gradeId,ciId,1);
        if(null!=turnOff){
        	acClassType = turnOff.getAcClass();
        }
        // jxb add
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(schoolId, gradeId, ciId,acClassType);
		List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombine(ciId,schoolId,gradeId);
		Map<String,Object> retMap = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		boolean flag = true;
		for (N33_TimeCombineEntry timeCombineEntry : timeCombineEntryList) {
			for (N33_TimeCombineEntry.ZuHeList zuHeList : timeCombineEntry.getZuHeList()) {
				if(zuHeList.getJxbId() != null && !"".equals(zuHeList.getJxbId().toString())){
					N33_JXBEntry jxbEntry = jxbEntryMap.get(zuHeList.getJxbId());
					if(jxbEntry != null){
						if(jxbEntry.getTercherId() == null || "".equals(jxbEntry.getTercherId().toString()) || jxbEntry.getClassroomId() == null || "".equals(jxbEntry.getClassroomId().toString())){
							flag = false;
							if(timeCombineEntry.getType() == 1){
								String name = "等级-时段组合" + zuHeList.getSerial();
								if(!list.contains("name")){
									list.add(name);
								}
							}else{
								String name = "合格-时段组合" + zuHeList.getSerial();
								if(!list.contains("name")){
									list.add(name);
								}
							}
						}
					}
				}
			}
		}
		retMap.put("flag",flag);
		retMap.put("list",list);
		return retMap;
	}

	/**
	 * 查找标签
	 * @param ciId
	 * @param gradeId
	 * @return
	 */
	public List<Map<String,Object>> getTagName(ObjectId ciId,ObjectId gradeId){
		List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();
		List<N33_StudentTagEntry> tagEntries = studentTagDao.getTagListByxqid(ciId, gradeId);
		for (N33_StudentTagEntry tagEntry : tagEntries) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("tagId",tagEntry.getID().toString());
			map.put("tagName",tagEntry.getName());
			retList.add(map);
		}
		return retList;
	}

	/**
	 * 查找教学班的学生数量
	 * @param jxbId
	 * @return
	 */
	public Integer getJxbStudentCount(ObjectId jxbId){
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		if(jxbEntry != null){
			return jxbEntry.getStudentIds().size();
		}else{
			return 0;
		}
	}

	/**
	 * 根据学生所在的班级，选科和所在的标签名查找教学班的学生信息
	 * @param jxbId
	 * @param classId
	 * @param combineName
	 * @param tagId
	 * @return
	 */
	public List<StudentDTO> getJxbStudent(ObjectId jxbId,String classId,String combineName,String tagId){
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
		if(jxbEntry != null){
			List<StudentDTO> studentDTOList = new ArrayList<StudentDTO>();
			List<ObjectId> stuIds = jxbEntry.getStudentIds();
			ObjectId gradeId = jxbEntry.getGradeId();
			ObjectId ciId = jxbEntry.getTermId();
			Map<ObjectId,StudentEntry> studentEntryMap = studentDao.getStudentByXqidMap(ciId,stuIds);
			List<N33_StudentTagEntry> studentTagEntryList = studentTagDao.getTagList(ciId,gradeId);
			Map<ObjectId,ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(gradeId,ciId);
			for (ObjectId stuId : stuIds) {
				StudentEntry studentEntry = studentEntryMap.get(stuId);
				if(!"*".equals(classId) && !classId.equals(studentEntry.getClassId().toString())){
					continue;
				}
				if(!"*".equals(combineName) && !combineName.equals(studentEntry.getCombiname())){
					continue;
				}
				ObjectId stuTagId = null;
				String tagName = "";
				for (N33_StudentTagEntry tagEntry : studentTagEntryList) {
					//标签中的学生
					List<ObjectId> tagStuIds = new ArrayList<ObjectId>();
					for (N33_StudentTagEntry.StudentInfoEntry infoEntry : tagEntry.getStudents()) {
						if(!tagStuIds.contains(infoEntry.getStuId())){
							tagStuIds.add(infoEntry.getStuId());
						}
					}
					if(tagStuIds.contains(stuId)){
						stuTagId = tagEntry.getID();
						tagName = tagEntry.getName();
						break;
					}
				}
				if((!"*".equals(tagId) && !tagId.equals(stuTagId.toString())) || (!"*".equals(tagId) && stuTagId == null)){
					continue;
				}
				StudentDTO studentDTO = new StudentDTO(studentEntry);
				studentDTO.setTagName(tagName);
				studentDTO.setClassXH(classEntryMap.get(studentEntry.getClassId()).getXh());
				studentDTOList.add(studentDTO);
			}

			return studentDTOList;
		}else{
			List<StudentDTO> studentDTOList = new ArrayList<StudentDTO>();
			return studentDTOList;
		}
	}
}
