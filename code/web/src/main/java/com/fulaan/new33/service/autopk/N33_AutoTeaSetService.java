package com.fulaan.new33.service.autopk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.new33.autopk.N33_AutoOtherSetDao;
import com.db.new33.autopk.N33_AutoTeaSetDao;
import com.db.new33.autopk.N33_OSubCheckDao;
import com.db.new33.autopk.N33_PkGzDao;
import com.db.new33.autopk.N33_TeaDayDao;
import com.db.new33.autopk.N33_TeaMutexDao;
import com.db.new33.autopk.N33_TeaStepDao;
import com.db.new33.autopk.N33_TeaWeekCourseDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.SubjectDao;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.dto.isolate.N33_TeaDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.IsolateUserService;
import com.pojo.autoPK.AutoOtherSetEntry;
import com.pojo.autoPK.AutoPkGzSetEntry;
import com.pojo.autoPK.AutoTeaSetEntry;
import com.pojo.autoPK.OSubCheckEntry;
import com.pojo.autoPK.TeaDayEntry;
import com.pojo.autoPK.TeaMutexEntry;
import com.pojo.autoPK.TeaStepEntry;
import com.pojo.autoPK.TeaWeekCourseEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.sys.constants.Constant;

/**
*自动排课-排课规则-教师设置
*/
@Service
public class N33_AutoTeaSetService extends BaseService {

	private N33_AutoTeaSetDao autoTeaSetDao = new N33_AutoTeaSetDao();
	
	private N33_TeaWeekCourseDao teaWeekCourDao = new N33_TeaWeekCourseDao();
	
	private N33_TeaDayDao teaDayDao = new N33_TeaDayDao();
	
	private N33_TeaStepDao teaStepDao = new N33_TeaStepDao();
	
	private N33_TeaMutexDao teaMutexDao = new N33_TeaMutexDao();
	
	private SubjectDao subjectDao = new SubjectDao();
	
	private N33_TeaDao N33TeaDao = new N33_TeaDao();
	
	private N33_OSubCheckDao oSubCheckDao = new N33_OSubCheckDao();
	
	private N33_AutoOtherSetDao autoOtherSetDao = new N33_AutoOtherSetDao();
	
	private N33_PkGzDao pkGzDao = new N33_PkGzDao();
	
    private IsolateUserService isoUserService = new IsolateUserService();
	
	/**
     * 获取当前次下的所有教师
     */
    public List<Map> getAllTeachers(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId xqid = new ObjectId(repMap.get("xqid").toString());
    	String userName = repMap.get("userName").toString();
    	List<Map> dataList = new ArrayList<Map>();
		List<N33_TeaDTO> teaAllDtos = new ArrayList<N33_TeaDTO>();
		teaAllDtos = isoUserService.getTeaByGradeAndSubject(xqid, schoolId, gradeId.toString(), "*", userName);
    	List<ObjectId> cTeaList = new ArrayList<ObjectId>(); 
    	cTeaList = getWeekTeachers(repMap);//教师周任课天数已经选中的教师
    	Map userMap = new HashMap();
    	for(N33_TeaDTO teaDto : teaAllDtos){
    		if(!userMap.containsKey(teaDto.getUid()) && !cTeaList.contains(new ObjectId(teaDto.getUid()))){
    			Map dataMap = new HashMap();
    			dataMap.put("teaName", teaDto.getUnm());
    			dataMap.put("userId", teaDto.getUid());
    			userMap.put(teaDto.getUid(), teaDto.getUnm());
    			dataList.add(dataMap);
    		}
    	}
    	return dataList;
    }
	
    /**
     * 获取教师周任课天数 每天的教师
     */
    public List<ObjectId> getWeekTeachers(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	List<TeaWeekCourseEntry> teaWeekList = new ArrayList<TeaWeekCourseEntry>();
    	teaWeekList = teaWeekCourDao.getEntryByGId(schoolId, gradeId);
    	List<ObjectId> teaList = new ArrayList<ObjectId>();
    	for(TeaWeekCourseEntry tEntry : teaWeekList){
    		teaList.addAll(tEntry.getTeaList());
    	}
    	return teaList;
    }
	
	/**
     * 新增或更新教师设置
     */
    public void addOrupdAutoTeaSet(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	String num = repMap.get("num").toString();
    	String status = repMap.get("status").toString();
    	AutoTeaSetEntry autoTeaSetEntry = new AutoTeaSetEntry();
    	autoTeaSetEntry = autoTeaSetDao.getEntryByGId(schoolId, gradeId, ciId);
    	if(autoTeaSetEntry.getID() == null){//新增
    		autoTeaSetEntry.setSchoolId(schoolId);
    		autoTeaSetEntry.setGradeId(gradeId);
    		autoTeaSetEntry.setCiId(ciId);
    		if(num.equals("1")){
    			autoTeaSetEntry.setTeaWeek(status);
    		}else{
    			autoTeaSetEntry.setTeaWeek("");
    		}
    		if(num.equals("2")){
    			autoTeaSetEntry.setTeaDay(status);
    		}else{
    			autoTeaSetEntry.setTeaDay("");
    		}
    		if(num.equals("3")){
    			autoTeaSetEntry.setTeaOverClass(status);
    		}else{
    			autoTeaSetEntry.setTeaOverClass("");
    		}
    		if(num.equals("4")){
    			autoTeaSetEntry.setTeaStep(status);
    		}else{
    			autoTeaSetEntry.setTeaStep("");
    		}
    		if(num.equals("5")){
    			autoTeaSetEntry.setTeaRule(status);
    		}else{
    			autoTeaSetEntry.setTeaRule("");
    		}
    		if(num.equals("6")){
    			autoTeaSetEntry.setTeaMutex(status);
    		}else{
    			autoTeaSetEntry.setTeaMutex("");
    		}
    		autoTeaSetDao.addEntry(autoTeaSetEntry);
    	}else{
    		if(num.equals("1")){
    			autoTeaSetEntry.setTeaWeek(status);
    		}
    		if(num.equals("2")){
    			autoTeaSetEntry.setTeaDay(status);
    		}
    		if(num.equals("3")){
    			autoTeaSetEntry.setTeaOverClass(status);
    		}
    		if(num.equals("4")){
    			autoTeaSetEntry.setTeaStep(status);
    		}
    		if(num.equals("5")){
    			autoTeaSetEntry.setTeaRule(status);
    		}
    		if(num.equals("6")){
    			autoTeaSetEntry.setTeaMutex(status);
    		}
    		autoTeaSetDao.updateEntry(autoTeaSetEntry);
    	}
    }
    
    /**
     * 获取教师设置
     */
    public Map getAutoTeaSet(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
		Map dataMap = getAutoTeaSet(schoolId, gradeId, ciId);
		return dataMap;
    }

	/**
	 * 获取教师设置
	 * @param schoolId
	 * @param gradeId
	 * @param ciId
	 * @return
	 */
	public Map getAutoTeaSet(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		Map dataMap = new HashMap();
		AutoTeaSetEntry autoTeaSetEntry = autoTeaSetDao.getEntryByGId(schoolId, gradeId,ciId);
		if(autoTeaSetEntry != null ){
			dataMap.put("teaWeek", autoTeaSetEntry.getTeaWeek());
			dataMap.put("teaDay", autoTeaSetEntry.getTeaDay());
			dataMap.put("teaOverClass", autoTeaSetEntry.getTeaDay());
			dataMap.put("teaStep", autoTeaSetEntry.getTeaStep());
			dataMap.put("teaRule", autoTeaSetEntry.getTeaRule());
			dataMap.put("teaMutex", autoTeaSetEntry.getTeaMutex());
		}else{
			dataMap.put("teaWeek", "");
			dataMap.put("teaDay", "");
			dataMap.put("teaOverClass", "");
			dataMap.put("teaStep", "");
			dataMap.put("teaRule", "");
			dataMap.put("teaMutex", "");
		}
		return dataMap;
	}

    
    /**
     * 新增或更新周任课天数教师
     */
    public void addOrupdTeaWeek(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	int weekCourse = Integer.parseInt(repMap.get("weekCourse").toString());
    	String userIds = repMap.get("userIds").toString();
    	TeaWeekCourseEntry teaWeekCourseEntry = new TeaWeekCourseEntry();
    	teaWeekCourseEntry = teaWeekCourDao.getEntryByGWC(schoolId, gradeId, ciId, weekCourse);
    	List<ObjectId> teaList = new ArrayList<ObjectId>();
    	if(teaWeekCourseEntry.getID() == null ){//新增
    		teaWeekCourseEntry.setSchoolId(schoolId);
    		teaWeekCourseEntry.setGradeId(gradeId);
    		teaWeekCourseEntry.setCiId(ciId);
    		teaWeekCourseEntry.setWeekCourse(weekCourse);
    		//教师信息
    		if(StringUtils.isNotBlank(userIds)){
    			String[] arr = StringUtils.split(userIds, Constant.COMMA);
    			for(String teaSubId : arr){
    				teaList.add(new ObjectId(teaSubId));
    			}
    		}
    		removeWeekTeachers(schoolId, gradeId, teaList);//去除重复教师
    		teaWeekCourseEntry.setTeaList(teaList);
    		teaWeekCourDao.addEntry(teaWeekCourseEntry);
    	}else{
    		//教师信息
    		if(StringUtils.isNotBlank(userIds)){
    			String[] arr = StringUtils.split(userIds, Constant.COMMA);
    			for(String teaSubId : arr){
    				teaList.add(new ObjectId(teaSubId));
    				}
    		}
    		removeWeekTeachers(schoolId, gradeId, teaList);//去除重复教师
    		teaWeekCourseEntry.setTeaList(teaList);
    		teaWeekCourDao.updateEntry(teaWeekCourseEntry);
    	}
    }
    
    /**
     * 去除重复的 每天的教师
     */
    public void removeWeekTeachers(ObjectId schoolId, ObjectId gradeId, List<ObjectId> teaList) {
    	List<TeaWeekCourseEntry> teaWeekList = new ArrayList<TeaWeekCourseEntry>();
    	teaWeekList = teaWeekCourDao.getEntryByGId(schoolId, gradeId);
    	for(TeaWeekCourseEntry teaWEntry : teaWeekList){
    		List<ObjectId> addTeaList = new ArrayList<ObjectId>();
    		List<ObjectId> userList = new ArrayList<ObjectId>();
    		userList = teaWEntry.getTeaList();
    		for(ObjectId userId : userList){
    			if(!teaList.contains(userId)){
    				addTeaList.add(userId);
    			}
    		}
    		teaWEntry.setTeaList(addTeaList);
    		teaWeekCourDao.updateEntry(teaWEntry);
    	}
    }
    
    
    
    /**
     * 获取教师周任课天数详情
     */
    public Map getCTeaList(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	int weekCourse = Integer.parseInt(repMap.get("weekCourse").toString());
    	TeaWeekCourseEntry teaWeekCourseEntry = new TeaWeekCourseEntry();
    	teaWeekCourseEntry = teaWeekCourDao.getEntryByGWC(schoolId, gradeId, ciId, weekCourse);
    	Map dataMap = new HashMap();
    	List<Map> userList = new ArrayList<Map>();
    	if(teaWeekCourseEntry.getID() != null){
    		dataMap.put("weekCourse", teaWeekCourseEntry.getWeekCourse() + "");
    		//获取教师信息
    		List<N33_TeaEntry> teacherList = new ArrayList<N33_TeaEntry>();
    		teacherList = N33TeaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(ciId, schoolId, gradeId, null, "*");
    		Map teacherMap = new HashMap();
    		for(N33_TeaEntry teaEntry : teacherList){
    			if(!teacherMap.containsKey(teaEntry.getUserId()))
    				teacherMap.put(teaEntry.getUserId(), teaEntry.getUserName());
    		}
    		List<ObjectId> teaList = new ArrayList<ObjectId>();
    		teaList = teaWeekCourseEntry.getTeaList();
    		//封装教师信息
    		for(ObjectId teaId : teaList){
    			Map userMap = new HashMap();
    			userMap.put("userId", teaId.toString());
    			if(teacherMap.containsKey(teaId)){
    				userMap.put("teaName", teacherMap.get(teaId).toString());
    			}else{
    				userMap.put("teaName", "");
    			}
    			userList.add(userMap);
    		}
    		dataMap.put("userList", userList);
    	}else{
    		dataMap.put("weekCourse", "0");
    		dataMap.put("userList", userList);
    	}
    	return dataMap;
    }
    
    /**
     * 教师日最大连课节数-获取当前次下的所有教师
     */
    public List<Map> getDTeachers(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	String userName = repMap.get("userName").toString();
    	List<Map> dataList = new ArrayList<Map>();
		List<N33_TeaDTO> teaAllDtos = new ArrayList<N33_TeaDTO>();
		teaAllDtos = isoUserService.getTeaByGradeAndSubject(ciId, schoolId, gradeId.toString(), "*", userName);
    	List<ObjectId> cTeaList = new ArrayList<ObjectId>(); 
    	cTeaList = getDayTeachers(repMap);//教师日最大连课节数已经选中的教师
    	Map userMap = new HashMap();
    	for(N33_TeaDTO teaDto : teaAllDtos){
    		if(!userMap.containsKey(teaDto.getUid()) && ! cTeaList.contains(new ObjectId(teaDto.getUid()))){
    			Map dataMap = new HashMap();
    			dataMap.put("teaName", teaDto.getUnm());
    			dataMap.put("userId", teaDto.getUid());
    			userMap.put(teaDto.getUid(), teaDto.getUnm());
    			dataList.add(dataMap);
    		}
    	}
    	return dataList;
    }
    
    /**
     * 教师日最大连课节数已经选中的教师
     */
    public List<ObjectId> getDayTeachers(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	List<TeaDayEntry> teaDayList = new ArrayList<TeaDayEntry>();
    	teaDayList = teaDayDao.getEntryByGId(schoolId, gradeId, ciId);
    	List<ObjectId> teaList = new ArrayList<ObjectId>();
    	for(TeaDayEntry tEntry : teaDayList){
    		teaList.addAll(tEntry.getTeaList());
    	}
    	return teaList;
    }
    
    /**
     * 新增或更新教师日最大连课节数
     */
    public void addOrupdTeaDay(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	int courseNum = Integer.parseInt(repMap.get("courseNum").toString());
    	String userIds = repMap.get("userIds").toString();
    	TeaDayEntry teaDayEntry = new TeaDayEntry();
    	teaDayEntry = teaDayDao.getEntryByCId(schoolId, gradeId, ciId,courseNum);
    	if(teaDayEntry.getID() == null){//新增
    		teaDayEntry.setSchoolId(schoolId);
    		teaDayEntry.setGradeId(gradeId);
    		teaDayEntry.setCiId(ciId);
    		teaDayEntry.setCourseNum(courseNum);
    		List<ObjectId> teaList = new ArrayList<ObjectId>();
    		//教师信息
    		if(StringUtils.isNotBlank(userIds)){
    			String[] arr = StringUtils.split(userIds, Constant.COMMA);
    			for(String teaId : arr){
    				teaList.add(new ObjectId(teaId));
    			}
    		}
    		teaDayEntry.setTeaList(teaList);
    		teaDayDao.addEntry(teaDayEntry);
    	}else{
    		List<ObjectId> teaList = new ArrayList<ObjectId>();
    		//教师信息
    		if(StringUtils.isNotBlank(userIds)){
    			String[] arr = StringUtils.split(userIds, Constant.COMMA);
    			for(String teaId : arr){
    				teaList.add(new ObjectId(teaId));
    			}
    		}
    		teaDayEntry.setTeaList(teaList);
    		teaDayDao.updateEntry(teaDayEntry);
    	}
    }
    
    /**
     * 获取教师教师日最大连课节数详情
     */
    public List<Map> getDCTeaList(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	int courseNum = Integer.parseInt(repMap.get("courseNum").toString());
    	List<Map> userList = new ArrayList<Map>();
    	TeaDayEntry teaDayEntry = new TeaDayEntry();
    	teaDayEntry = teaDayDao.getEntryByCId(schoolId, gradeId, ciId,courseNum);
    	if(teaDayEntry.getID() != null){
    		//获取教师信息
    		List<N33_TeaEntry> teacherList = new ArrayList<N33_TeaEntry>();
    		teacherList = N33TeaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(ciId, schoolId, gradeId, null, "*");
    		Map teacherMap = new HashMap();
    		for(N33_TeaEntry teaEntry : teacherList){
    			if(!teacherMap.containsKey(teaEntry.getUserId()))
    				teacherMap.put(teaEntry.getUserId(), teaEntry.getUserName());
    		}
    		List<ObjectId> teaList = new ArrayList<ObjectId>();
    		teaList = teaDayEntry.getTeaList();
    		//封装教师信息
    		for(ObjectId teaId : teaList){
    			Map userMap = new HashMap();
    			if(teacherMap.containsKey(teaId)){
    				userMap.put("userId", teaId.toString());
    				userMap.put("teaName", teacherMap.get(teaId).toString());
    			}else{
    				userMap.put("userId", "");
    				userMap.put("teaName", "");
    			}
    			userList.add(userMap);
    		}
    	}
    	return userList;
    }
    
    /**
     * 教师跨班进度一致-获取当前次下的所有教师
     */
    public List<Map> getStepTeachers(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	String userName = repMap.get("userName").toString();
    	List<Map> dataList = new ArrayList<Map>();
		List<N33_TeaDTO> teaAllDtos = new ArrayList<N33_TeaDTO>();
		teaAllDtos = isoUserService.getTeaByGradeAndSubject(ciId, schoolId, gradeId.toString(), "*", userName);//当前年级次下的所有老师
    	List<ObjectId> cTeaList = new ArrayList<ObjectId>(); 
    	cTeaList = getStepCTeachers(repMap);//教师跨班进度一致已经选中的教师
    	Map userMap = new HashMap();
    	for(N33_TeaDTO teaDto : teaAllDtos){
    		if(!userMap.containsKey(teaDto.getUid()) && ! cTeaList.contains(new ObjectId(teaDto.getUid()))){
    			Map dataMap = new HashMap();
    			dataMap.put("teaName", teaDto.getUnm());
    			dataMap.put("userId", teaDto.getUid());
    			userMap.put(teaDto.getUid(), teaDto.getUnm());
    			dataList.add(dataMap);
    		}
    	}
    	return dataList;
    }
	
    
    /**
     * 教师跨班进度一致已经选中的教师
     */
    public List<ObjectId> getStepCTeachers(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	TeaStepEntry teaStepEntry = new TeaStepEntry();
    	teaStepEntry = teaStepDao.getEntryByGId(schoolId, gradeId,ciId);
    	List<ObjectId> teaList = new ArrayList<ObjectId>();
    	teaList = teaStepEntry.getTeaList();
    	return teaList;
    }
    
    /**
     * 新增或更新教师跨班进度一致 
     */
    public void addOrupdTeaStep(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	String userIds = repMap.get("userIds").toString();
    	TeaStepEntry teaStepEntry = new TeaStepEntry();
    	teaStepEntry = teaStepDao.getEntryByGId(schoolId, gradeId,ciId);
    	if(teaStepEntry.getID() == null){//新增
    		teaStepEntry.setSchoolId(schoolId);
    		teaStepEntry.setGradeId(gradeId);
    		teaStepEntry.setCiId(ciId);
    		List<ObjectId> teaList = new ArrayList<ObjectId>();
    		//教师信息
    		if(StringUtils.isNotBlank(userIds)){
    			String[] arr = StringUtils.split(userIds, Constant.COMMA);
    			for(String teaId : arr){
    				teaList.add(new ObjectId(teaId));
    			}
    		}
    		teaStepEntry.setTeaList(teaList);
    		teaStepDao.addEntry(teaStepEntry);
    	}else{
    		List<ObjectId> teaList = new ArrayList<ObjectId>();
    		//教师信息
    		if(StringUtils.isNotBlank(userIds)){
    			String[] arr = StringUtils.split(userIds, Constant.COMMA);
    			for(String teaId : arr){
    				teaList.add(new ObjectId(teaId));
    			}
    		}
    		teaStepEntry.setTeaList(teaList);
    		teaStepDao.updateEntry(teaStepEntry);
    	}
    }
    
    /**
     * 获取教师教师跨班进度一致 详情
     */
    public List<Map> getTeaStep(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	TeaStepEntry teaStepEntry = new TeaStepEntry();
    	teaStepEntry = teaStepDao.getEntryByGId(schoolId, gradeId,ciId);
    	List<Map> userList = new ArrayList<Map>();
    	if(teaStepEntry.getID() != null){
    		//获取教师信息
    		List<N33_TeaEntry> teacherList = new ArrayList<N33_TeaEntry>();
    		teacherList = N33TeaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(ciId, schoolId, gradeId, null, "*");
    		Map teacherMap = new HashMap();
    		for(N33_TeaEntry teaEntry : teacherList){
    			if(!teacherMap.containsKey(teaEntry.getUserId()))
    				teacherMap.put(teaEntry.getUserId(), teaEntry.getUserName());
    		}
    		List<ObjectId> teaList = new ArrayList<ObjectId>();
    		teaList = teaStepEntry.getTeaList();
    		//封装教师信息
    		for(ObjectId teaId : teaList){
    			Map userMap = new HashMap();
    			if(teacherMap.containsKey(teaId)){
    				userMap.put("teaId", teaId.toString());
    				userMap.put("teaName", teacherMap.get(teaId).toString());
    			}else{
    				userMap.put("teaId","");
    				userMap.put("teaName", "");
    			}
    			userList.add(userMap);
    		}
    		
    	}
    	return userList;
    }
    
    /**
     * 新增或更新教师互斥
     */
    public void addTeaMutex(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	String userIds = repMap.get("userIds").toString();
    	TeaMutexEntry teaMutexEntry = new TeaMutexEntry();
		teaMutexEntry.setSchoolId(schoolId);
		teaMutexEntry.setCiId(ciId);
		teaMutexEntry.setGradeId(gradeId);
		int teaMutexSize = teaMutexDao.getEntryList(schoolId, gradeId, ciId) + 1;
		teaMutexEntry.setMutexNum(teaMutexSize);
		teaMutexEntry.setIsRemove("0");
		//教师信息
		List<ObjectId> teaList = new ArrayList<ObjectId>();
		if(StringUtils.isNotBlank(userIds)){
			String[] arr = StringUtils.split(userIds, Constant.COMMA);
			for(String teaId : arr){
				teaList.add(new ObjectId(teaId));
			}
		}
		teaMutexEntry.setTeaList(teaList);
		teaMutexDao.addEntry(teaMutexEntry);
    }
    
    /**
     * 删除教师互斥
     */
    public void delTeaMutex(Map repMap) {
    	ObjectId id = new ObjectId(repMap.get("id").toString());
    	teaMutexDao.updateEntry(id);
    }
    
    /**
     * 获取教师教师互斥 
     */
    public List<Map> getTeaNutex(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	List<TeaMutexEntry> teaMutexs = new ArrayList<TeaMutexEntry>();
    	teaMutexs = teaMutexDao.getEntrys(schoolId, gradeId, ciId);
    	List<Map> dataList = new ArrayList<Map>();
    	if(teaMutexs.size() > 0){
    		//获取教师信息
    		List<N33_TeaEntry> teacherList = new ArrayList<N33_TeaEntry>();
    		teacherList = N33TeaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(ciId, schoolId, gradeId, null, "*");
    		Map teacherMap = new HashMap();
    		for(N33_TeaEntry teaEntry : teacherList){
    			if(!teacherMap.containsKey(teaEntry.getUserId()))
    				teacherMap.put(teaEntry.getUserId(), teaEntry.getUserName());
    		}
    		//封装教师信息
    		for(TeaMutexEntry teaMutexEntry : teaMutexs){
    			Map dataMap = new HashMap();//返回数据
    			dataMap.put("id", teaMutexEntry.getID().toString());
    			dataMap.put("mutexNum", teaMutexEntry.getMutexNum() + "");
    			List<ObjectId> teaList = new ArrayList<ObjectId>();
    			teaList = teaMutexEntry.getTeaList();
    			for(int i=0; i<teaList.size(); i++ ){
        			if(teacherMap.containsKey(teaList.get(i))){
        				dataMap.put("teaName" + i, teacherMap.get(teaList.get(i)).toString());
        			}else{
        				dataMap.put("teaName", "");
        			}
    			}
    			dataList.add(dataMap);
    		}
    	}
    	return dataList;
    }
    
    /**
     * 获取当前次下的所有教师
     */
    public List<Map> getAllTeas(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	List<Map> dataList = new ArrayList<Map>();
		List<N33_TeaDTO> teaAllDtos = new ArrayList<N33_TeaDTO>();
		teaAllDtos = isoUserService.getTeaByGradeAndSubject(ciId, schoolId, gradeId.toString(), "*", "*");
    	Map userMap = new HashMap();
    	for(N33_TeaDTO teaDto : teaAllDtos){
    		if(!userMap.containsKey(teaDto.getUid())){
    			Map dataMap = new HashMap();
    			dataMap.put("teaName", teaDto.getUnm());
    			dataMap.put("sex", teaDto.getSex() + "");
    			dataMap.put("userId", teaDto.getUid());
    			userMap.put(teaDto.getUid(), teaDto.getUnm());
    			dataList.add(dataMap);
    		}
    	}
    	return dataList;
    }
    
  //============================================== 其他设置 ==========================================
    
    /**
     * 新增或更新其他设置
     */
    public void addOtherSet(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	String num = repMap.get("number").toString();
    	String status = repMap.get("status").toString();
    	AutoOtherSetEntry autotherSetEntry = new AutoOtherSetEntry();
    	autotherSetEntry = autoOtherSetDao.getEntryByGId(schoolId, gradeId, ciId);
    	if(autotherSetEntry.getID() == null){//新增
    		autotherSetEntry.setSchoolId(schoolId);
    		autotherSetEntry.setGradeId(gradeId);
    		autotherSetEntry.setCiId(ciId);
    		if(num.equals("1")){
    			autotherSetEntry.setSubCheck(status);
    		}else{
    			autotherSetEntry.setSubCheck("");
    		}
    		if(num.equals("2")){
    			autotherSetEntry.setBoxcheck(status);
    		}else{
    			autotherSetEntry.setBoxcheck("");
    		}
    		if(num.equals("3")){
    			autotherSetEntry.setLtCheck(status);
    		}else{
    			autotherSetEntry.setLtCheck("");
    		}
    		if(num.equals("4")){
    			autotherSetEntry.setClassCheck(status);
    		}else{
    			autotherSetEntry.setClassCheck("");
    		}
    		if(num.equals("5")){
    			autotherSetEntry.setPeriodCheck(status);
    		}else{
    			autotherSetEntry.setPeriodCheck("");
    		}
    		autoOtherSetDao.addEntry(autotherSetEntry);
    	}else{
    		if(num.equals("1")){
    			autotherSetEntry.setSubCheck(status);
    		}
    		if(num.equals("2")){
    			autotherSetEntry.setBoxcheck(status);
    		}
    		if(num.equals("3")){
    			autotherSetEntry.setLtCheck(status);
    		}
    		if(num.equals("4")){
    			autotherSetEntry.setClassCheck(status);
    		}
    		if(num.equals("5")){
    			autotherSetEntry.setPeriodCheck(status);
    		}
    		autoOtherSetDao.updateEntry(autotherSetEntry);
    	}
    }
    
    /**
     * 获取其他设置
     */
    public Map getAutoOtherSet(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	//AutoOtherSetEntry autotherSetEntry = autoOtherSetDao.getEntryByGId(schoolId, gradeId, ciId);
    	Map dataMap = getAutoOtherSet(schoolId, gradeId, ciId);
    	return dataMap;
    }

	public Map getAutoOtherSet(ObjectId schoolId, ObjectId gradeId, ObjectId ciId){
		AutoOtherSetEntry autotherSetEntry = autoOtherSetDao.getEntryByGId(schoolId, gradeId, ciId);
		Map dataMap = new HashMap();
		if(autotherSetEntry!= null ){
			dataMap.put("subCheck", autotherSetEntry.getSubCheck());
			dataMap.put("boxcheck", autotherSetEntry.getBoxcheck());
			dataMap.put("ltCheck", autotherSetEntry.getLtCheck());
			dataMap.put("classCheck", autotherSetEntry.getClassCheck());
			dataMap.put("periodCheck", autotherSetEntry.getPeriodCheck());
		}else{
			dataMap.put("subCheck", "");
			dataMap.put("boxcheck", "");
			dataMap.put("ltCheck", "");
			dataMap.put("classCheck", "");
			dataMap.put("periodCheck", "");
		}
		return dataMap;
	}

    
    
    /**
     * 查询年级对应的所有学科
     *
     * @param xqid
     * @param sid
     * @return
     */
    public List<N33_KSDTO> getSubjectByType(ObjectId xqid, ObjectId gid, ObjectId sid, String subType) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        if ("0".equals(subType)) {
            entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
        } else if ("1".equals(subType) || "2".equals(subType)) {
            entries = subjectDao.getIsolateSubjectEntryByZouBan(xqid, sid, gid, 1);
        } else if ("3".equals(subType)) {
            entries = subjectDao.getIsolateSubjectEntryByZouBan(xqid, sid, gid, 0);
        } else if ("4".equals(subType)) {
            List<N33_KSEntry> temp = new ArrayList<N33_KSEntry>();
            temp = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
            for (N33_KSEntry entry : temp) {
                if (entry.getDan() == 1) {
                    entries.add(entry);
                }
            }
        }
        //获取已经添加的学科
        OSubCheckEntry oSubCheckEntry = new OSubCheckEntry();
    	oSubCheckEntry = oSubCheckDao.getEntryById(sid, gid, xqid);
    	List<ObjectId> subList = new ArrayList<ObjectId>();
    	subList = oSubCheckEntry.getSubList();
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
        	if(!subList.contains(entry.getSubjectId()))
        		dots.add(new N33_KSDTO(entry));
        }
        return dots;
    }
    
    
    /**
     * 获取已选择可排学科
     */
    public List<Map> getOsubCheks(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	List<Map> dataList = new ArrayList<Map>();
    	OSubCheckEntry oSubCheckEntry = new OSubCheckEntry();
    	oSubCheckEntry = oSubCheckDao.getEntryById(schoolId, gradeId, ciId);
    	List<ObjectId> subList = new ArrayList<ObjectId>();
    	subList = oSubCheckEntry.getSubList();
    	if(subList.size() > 0){
    		//获取学科信息
        	List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        	entries = subjectDao.getIsolateSubjectEntryByXqid(ciId,schoolId, gradeId);
        	Map subMap = new HashMap();
        	for(N33_KSEntry ksEntry : entries){
        		if(!subMap.containsKey(ksEntry.getSubjectId()))
        			subMap.put(ksEntry.getSubjectId(), ksEntry.getSubjectName());
        	}
        	for(ObjectId subId : subList){
        		Map dataMap = new HashMap();
        		if(subMap.containsKey(subId)){
        			dataMap.put("subId", subId.toString());
        			dataMap.put("subName", subMap.get(subId));
        		}
        		dataList.add(dataMap);
        	}
    	}
    	return dataList;
    }
    
    /**
     * 新增或更新选择可排学科
     */
    public void addOsubCheks(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	String subIds = repMap.get("subIds").toString();
    	OSubCheckEntry oSubCheckEntry = new OSubCheckEntry();
    	oSubCheckEntry = oSubCheckDao.getEntryById(schoolId, gradeId, ciId);
    	if(oSubCheckEntry.getID() == null){//新增
    		oSubCheckEntry.setSchoolId(schoolId);
    		oSubCheckEntry.setGradeId(gradeId);
    		oSubCheckEntry.setCiId(ciId);
    		List<ObjectId> subList = new ArrayList<ObjectId>();
    		if(StringUtils.isNotBlank(subIds)){
    			String[] arr = StringUtils.split(subIds, Constant.COMMA);
    			for(String subId : arr){
    				subList.add(new ObjectId(subId));
    			}
    		}
    		oSubCheckEntry.setSubList(subList);
    		oSubCheckDao.addEntry(oSubCheckEntry);
    	}else{
    		List<ObjectId> subList = new ArrayList<ObjectId>();
    		if(StringUtils.isNotBlank(subIds)){
    			String[] arr = StringUtils.split(subIds, Constant.COMMA);
    			for(String subId : arr){
    				subList.add(new ObjectId(subId));
    			}
    		}
    		oSubCheckEntry.setSubList(subList);
    		oSubCheckDao.updateEntry(oSubCheckEntry);
    	}
    }
    
    /**
     * 新增或更新不可排格子
     */
    public void addGz(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	String x =  repMap.get("x").toString();
    	String y =  repMap.get("y").toString();
    	String status =  repMap.get("status").toString();
    	AutoPkGzSetEntry autoPkGzSetEntry = new AutoPkGzSetEntry();
    	if(status.equals("0")){//物理删除此格子
    		autoPkGzSetEntry = pkGzDao.getEntryById(schoolId, gradeId, ciId, x, y);
    		pkGzDao.removeEntry(autoPkGzSetEntry);
    	}else{
    		autoPkGzSetEntry.setSchoolId(schoolId);
    		autoPkGzSetEntry.setGradeId(gradeId);
    		autoPkGzSetEntry.setCiId(ciId);
    		autoPkGzSetEntry.setX(x);
    		autoPkGzSetEntry.setY(y);
    		pkGzDao.addEntry(autoPkGzSetEntry);
    	}
    }
    
    /**
     * 新增或更新选择可排学科
     */
    public List<Map> getGzs(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	List<Map> dataList = new ArrayList<Map>();
    	List<AutoPkGzSetEntry> gzList = new ArrayList<AutoPkGzSetEntry>();
    	gzList = pkGzDao.getEntryByGId(schoolId, gradeId, ciId);
    	for(AutoPkGzSetEntry gzEntry : gzList){
    		Map dataMap = new HashMap();
    		dataMap.put("x", gzEntry.getX());
    		dataMap.put("y", gzEntry.getY());
    		dataList.add(dataMap);
    	}
    	return dataList;
    }
}
