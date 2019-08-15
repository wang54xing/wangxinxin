package com.fulaan.new33.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import com.db.new33.isolate.GradeDao;
import com.fulaan.new33.dto.isolate.StudentDTO;
import com.fulaan.new33.service.isolate.*;

import com.pojo.new33.isolate.Grade;
import com.pojo.utils.MongoUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.SchoolSelectLessonSetDao;
import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.school.SchoolDao;
import com.fulaan.new33.controller.N33_StudentController;
import com.fulaan.new33.dto.isolate.ClassInfoDTO;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.dto.isolate.TermDTO;
import com.mongodb.BasicDBObject;
import com.pojo.new33.SchoolSelectLessonSetEntry;
import com.pojo.new33.SchoolSelectLessonSetEntry.SelectLessons;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.school.Subject;
import com.sys.utils.CombineUtils;
import com.sys.utils.CombineUtils.CombineResult;

/**
 * 学校选课设置
 * @author fourer
 *
 */
@Service
public class SchoolSelectLessonSetService extends BaseService{

	private SchoolSelectLessonSetDao schoolSelectLessonSetDao =new SchoolSelectLessonSetDao();
	private SchoolDao schoolDao =new SchoolDao();
	 private IsolateSubjectService subjectService = new IsolateSubjectService();
	public static final Map<String,Integer> subject_sort = new HashMap<String, Integer>();
	public static final Map<String,Integer> subjectHead_sort = new HashMap<String, Integer>();
	private IsolateTermService isolateTermService = new IsolateTermService();
	private N33_StudentDao N33_StudentDao = new N33_StudentDao();
	private N33_ClassService N33_ClassService = new N33_ClassService();
	private ClassDao classDao = new ClassDao();
	private GradeDao gradeDao = new GradeDao();
	@Autowired
	private N33_StudentService studentService;
	private N33_StudentDao studentDao = new N33_StudentDao();



	private IsolateClassService classService = new IsolateClassService();
	static {
		subject_sort.put("物理", 1);
		subject_sort.put("化学", 2);
		subject_sort.put("生物", 3);
		subject_sort.put("政治", 4);
		subject_sort.put("历史", 5);
		subject_sort.put("地理", 6);
		
		subjectHead_sort.put("物", 1);
		subjectHead_sort.put("化", 2);
		subjectHead_sort.put("生", 3);
		subjectHead_sort.put("政", 4);
		subjectHead_sort.put("历", 5);
		subjectHead_sort.put("地", 6);
	}

	/**
	 * 得到所有选课组合
	 * @param schoolId
	 * @return
	 */
	public SchoolLesson33DTO getSchoolSelects(ObjectId term,ObjectId schoolId,ObjectId gradeId)
	{
		List<lesson33DTO> retList =new ArrayList<SchoolSelectLessonSetService.lesson33DTO>();
		List<SchoolSelectLessonSetEntry> list=schoolSelectLessonSetDao.getSchoolSelectLessonSetEntrys(term, schoolId, gradeId);
		String SchoolLesson33DTOId="";
		Long st = null;
		Long ed = null;
		 List<N33_KSDTO> N33_KSDTOList = subjectService.getIsolateSubjectByGradeIdIncludeMain(term, schoolId, gradeId.toString(),1);
		List<String> subjectNames = new ArrayList<String>();
		Collections.addAll(subjectNames, "物理", "化学", "生物", "政治", "历史", "地理");
		    final Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
		    for(N33_KSDTO dto:N33_KSDTOList) {
		    	if(subjectNames.contains(dto.getSnm()))
				{
		    		subjectMap.put(new ObjectId(dto.getSubid()),dto.getSnm());
				}
		    }
		if(list.size()>=1)
		{
			SchoolSelectLessonSetEntry e=list.get(0);
			SchoolLesson33DTOId =e.getID().toString();
			st = e.getStart();
			ed = e.getEnd();
			List<ObjectId> subIds = new ArrayList<ObjectId>(subjectMap.keySet());
			List<CombineResult<ObjectId>>  ll=  new ArrayList<CombineResult<ObjectId>>();
		    CombineUtils.combinerSelect(subIds, 3,ll); // 对现有学科进行组合与保存的进行比较
		    List<SelectLessons> waitForAdd = new ArrayList<SchoolSelectLessonSetEntry.SelectLessons>();
			// 遍历新增的
		    for(CombineResult<ObjectId> cb:ll) {
		    	List<ObjectId> sList=cb.getList();
		    	Collections.sort(sList);
		    	String nid = sList.get(0).toString().substring(sList.get(0).toString().toString().length()-5)+"_"
		    			+sList.get(1).toString().substring(sList.get(1).toString().toString().length()-5)+"_"
		    			+sList.get(2).toString().substring(sList.get(2).toString().toString().length()-5);
		    	SelectLessons lessions = new SelectLessons(nid, getCombineName(sList.get(0),sList.get(1),sList.get(2), subjectMap), sList.get(0),sList.get(1),sList.get(2), 0);
		    	
		    	for(SelectLessons sls:e.getSets())
				{
		    		if(sls.getId().equals(nid)) {
		    			lessions.setState(sls.getState());
						lessions.setNum(sls.getNum());
		    			break;
		    		}
				}
		    	waitForAdd.add(lessions);
		    	
		    }
		    
			schoolSelectLessonSetDao.updateLessionSet(e.getID(), waitForAdd);
			for(SelectLessons lesson:waitForAdd) {
				retList.add(new lesson33DTO(lesson));
			}
		}
		else
		{
			    List<SelectLessons> sets =new ArrayList<SchoolSelectLessonSetEntry.SelectLessons>();
			   
				List<ObjectId> subjectList =new ArrayList<ObjectId>(subjectMap.keySet());
			    List<CombineResult<ObjectId>>  ll=  new ArrayList<CombineResult<ObjectId>>();
			    CombineUtils.combinerSelect(subjectList, 3,ll); 
			    
			    for(CombineResult<ObjectId> cb:ll)
			    {
			    	try
			    	{
				    	List<ObjectId> sList=cb.getList();
				    	Collections.sort(sList);// 保证顺序相同学科
				    	
				    	lesson33DTO dto =new lesson33DTO();
				    	dto.setSubjectId1(sList.get(0));
				    	dto.setSubjectId2(sList.get(1));
				    	dto.setSubjectId3(sList.get(2));
				    	
				    	String combineName=getCombineName(dto.subjectId1,dto.subjectId2,dto.subjectId3, subjectMap);
				    	String id=dto.getId();
				    	dto.setId(id);
				    	dto.setName(combineName);
				    	dto.setState(0);
				    	
				    	
				    	sets.add(dto.convert());
				    	retList.add(dto);
			    	}catch(Exception ex)
			    	{
			    	}
			    }
			    
			    
			    SchoolSelectLessonSetEntry e=new SchoolSelectLessonSetEntry(term, schoolId, gradeId, sets);
			    
			   ObjectId insertId= schoolSelectLessonSetDao.addSchoolSelectLessonSetEntry(e);
			   SchoolLesson33DTOId=insertId.toString();
				st = e.getStart();
				ed = e.getEnd();
		}
		Collections.sort(retList,new Comparator<lesson33DTO>() {
			@Override
			public int compare(lesson33DTO o1, lesson33DTO o2) {
				String o1a = o1.getName().substring(0, 1);
				String o1b = o1.getName().substring(1, 2);
				String o1c = o1.getName().substring(2, 3);
				String o2a = o2.getName().substring(0, 1);
				String o2b = o2.getName().substring(1, 2);
				String o2c = o2.getName().substring(2, 3);
				if(!o1a.equals(o2a)) {
					return subjectHead_sort.get(o1a)-subjectHead_sort.get(o2a);
				}
				if(!o1b.equals(o2b)) {
					return subjectHead_sort.get(o1b)-subjectHead_sort.get(o2b);
				}
				return subjectHead_sort.get(o1c)-subjectHead_sort.get(o2c);
			}
			
		});
		return new SchoolLesson33DTO(SchoolLesson33DTOId,st,ed, retList);
	}
	public void syncXuanKeZuHe(ObjectId sid,ObjectId oldCiId, ObjectId newCiId){
		schoolSelectLessonSetDao.removeByTerm(newCiId,sid,null);
		List<SchoolSelectLessonSetEntry> list=schoolSelectLessonSetDao.getSchoolSelectLessonSetEntrys(oldCiId, sid,null);
		List<SchoolSelectLessonSetEntry> newlist= new ArrayList<SchoolSelectLessonSetEntry>();
		for(SchoolSelectLessonSetEntry entry:list){
			entry.setID(new ObjectId());
			entry.setTerm(newCiId);
			newlist.add(entry);
		}
		schoolSelectLessonSetDao.addList(newlist);

	}
	public void updateStartTime(String id,String start,String end) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		schoolSelectLessonSetDao.updateStartTime(new ObjectId(id), sf.parse(start).getTime(), sf.parse(end).getTime());
	}
	
	/**
	 * 根据学生获取当前学期年级的学科组合
	 * @param userId
	 * @param schoolId
	 * @return
	 */
	public Map<String,Object> getCurrentSubjectGroup(ObjectId userId,ObjectId schoolId) throws JSONException {
		Map<String,Object> result = new HashMap<String, Object>();
		List<Map<String,Object>> relist = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> nolist = new ArrayList<Map<String,Object>>();// 不可选的选科
		ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
//		if (studentService.getCountByXqId(ciId, schoolId) == 0) {
//			studentService.getStudentBySid(new ObjectId(termDto.getSid()), new ObjectId(termDto.getId()));// TODO 学生同步
//		}
		StudentEntry studentEntry = N33_StudentDao.findStudent(ciId, userId);
		List<SchoolSelectLessonSetEntry> list = null;
		if(studentEntry!=null) {
			list=schoolSelectLessonSetDao.getSchoolSelectLessonSetEntrys(ciId, schoolId, studentEntry.getGradeId());
		}
		if(list!=null&&list.size()!=0) {
			SchoolSelectLessonSetEntry entry = list.get(0);
			List<SelectLessons> sets = entry.getSets();
			for(SelectLessons lessons:sets) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("id", lessons.getId());
				map.put("name", lessons.getName());
				map.put("sub1", lessons.getSubject1().toString());
				map.put("sub2", lessons.getSubject2().toString());
				map.put("sub3", lessons.getSubject3().toString());
				/*if((entry.getStart() == null || entry.getEnd() == null || entry.getStart() > System.currentTimeMillis()) || entry.getEnd() < System.currentTimeMillis()){
					nolist.add(map);
				}else{*/
				if(lessons.getState()==0) {
					nolist.add(map);
				}
				else {
					relist.add(map);
				}
				/*}*/
				if(entry.getStart() == null || entry.getEnd() == null || (entry.getStart() > System.currentTimeMillis() || entry.getEnd() < System.currentTimeMillis())){
					result.put("isOnTime", 1);
				}
			}
			result.put("relist", relist);
			result.put("nolist", nolist);
			result.put("xqid", ciId.toString());
			result.put("select",studentEntry.getCombiname());
			result.put("startTime", list.get(0).getStart());
			result.put("endTime", list.get(0).getEnd());
		}
		return result;
	}
	
	public String studentSelectSubjectGroup(ObjectId xqid,ObjectId userId,ObjectId schoolId,ObjectId sub1,ObjectId sub2,ObjectId sub3,String name) {
		StudentEntry studentEntry = N33_StudentDao.findStudent(xqid, userId);
		List<SchoolSelectLessonSetEntry> list = null;
		if(studentEntry!=null) {
			list=schoolSelectLessonSetDao.getSchoolSelectLessonSetEntrys(xqid, schoolId, studentEntry.getGradeId());
		}

		if(list!=null&&list.size()!=0) {
			SchoolSelectLessonSetEntry entry = list.get(0);
			Integer limit = 0;
			for(SelectLessons selectLessons:entry.getSets()){
				if(selectLessons.getName().equals(name)){
					limit = selectLessons.getNum();
				}
			}
			Integer already = N33_StudentDao.countStudentByXqidAndGradeIdxuanke(studentEntry.getGradeId(),xqid,name);
			if(entry.getStart() > System.currentTimeMillis() || entry.getEnd() < System.currentTimeMillis()){
				return "不在选科时间范围";
			}
			else if((limit != null) && (limit-already<=0)){
				return "该组合选科数量已满";
			}
			else{
				N33_StudentDao.updateStudentSelectSub(xqid, schoolId, userId, sub1, sub2, sub3,name);
				return "选科成功";
			}
		}
		return "未开启选科";
	}

	public List<Map<String,Object>> getHGOrDJStuIds(List<String> stuIds, String classId, String xqid,String gradeId){
		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
		List<ObjectId> studentIds = MongoUtils.convertToObjectIdList(stuIds);
		Map<ObjectId,ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(new ObjectId(gradeId),new ObjectId(xqid));
		Grade grade = gradeDao.findIsolateGradeEntryByGradeId(new ObjectId(gradeId),new ObjectId(xqid));
		if("".equals(classId)){
			List<StudentEntry> studentEntries = N33_StudentDao.getStudentByXqidAndGradeId(new ObjectId(gradeId),new ObjectId(xqid));
			for (StudentEntry studentEntry : studentEntries) {
				if(!studentIds.contains(studentEntry.getUserId()) && studentEntry.getCombiname() != null && !"".equals(studentEntry.getCombiname())){
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("stuId",studentEntry.getUserId().toString());
					map.put("clsXh",classEntryMap.get(studentEntry.getClassId()).getXh()+"");
					map.put("stuName",studentEntry.getUserName());
					map.put("stuNum",studentEntry.getStudyNum());
					map.put("className",grade.getName() + "(" + classEntryMap.get(studentEntry.getClassId()).getXh() + ")班");
					if(studentEntry.getSex() == 0){
						map.put("sex","女");
					}else if(studentEntry.getSex() == 1){
						map.put("sex","男");
					}else{
						map.put("sex","未知");
					}
					if(studentEntry.getLevel() == 0 || studentEntry.getLevel() == null){
						map.put("level","无");
					}else{
						map.put("level",studentEntry.getLevel());
					}
					if(studentEntry.getCombiname() != null){
						map.put("combineName",studentEntry.getCombiname());
					}else{
						map.put("combineName","");
					}
					retList.add(map);
				}
			}
		}else{
			List<StudentEntry> studentEntryList = studentDao.getStudentByXqidAndClassId(new ObjectId(classId),new ObjectId(xqid));
			List<ObjectId> studentIds1 = MongoUtils.getFieldObjectIDs(studentEntryList,"uid");
			List<StudentEntry> studentList = N33_StudentDao.getStuList(studentIds1, new ObjectId(xqid));
			Map<ObjectId,List<StudentEntry>> classId_stus = new HashMap<ObjectId, List<StudentEntry>>();
			if(studentList!=null) {
				for(StudentEntry entry:studentList) {
					List<StudentEntry> templist = classId_stus.get(entry.getClassId())==null?new ArrayList<StudentEntry>():classId_stus.get(entry.getClassId());
					templist.add(entry);
					classId_stus.put(entry.getClassId(), templist);
				}
			}
			for (StudentEntry studentEntry : classId_stus.get(new ObjectId(classId))) {
				if(!studentIds.contains(studentEntry.getUserId()) && studentEntry.getCombiname() != null && !"".equals(studentEntry.getCombiname())){
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("stuId",studentEntry.getUserId().toString());
					map.put("clsXh",classEntryMap.get(studentEntry.getClassId()).getXh()+"");
					map.put("stuName",studentEntry.getUserName());
					map.put("stuNum",studentEntry.getStudyNum());
					map.put("className",grade.getName() + "(" + classEntryMap.get(studentEntry.getClassId()).getXh() + ")班");
					if(studentEntry.getSex() == 0){
						map.put("sex","女");
					}else if(studentEntry.getSex() == 1){
						map.put("sex","男");
					}else{
						map.put("sex","未知");
					}
					if(studentEntry.getLevel() == 0 || studentEntry.getLevel() == null){
						map.put("level","无");
					}else{
						map.put("level",studentEntry.getLevel());
					}
					if(studentEntry.getCombiname() != null){
						map.put("combineName",studentEntry.getCombiname());
					}else{
						map.put("combineName","");
					}
					retList.add(map);
				}
			}

		}
		return retList;
	}
	
	public Map<String,Object> getStuSelectResultByClass(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,Integer type){

		SchoolLesson33DTO schoolLesson33DTO = getSchoolSelects(xqid,schoolId,gradeId);
		List<lesson33DTO> listLesson33DTO = schoolLesson33DTO.getList();

		Map<String,Object> result = new HashMap<String, Object>();
		//N33_ClassController.getIsolateClass(xqid.toString());// TODO
		List<ClassEntry> entrylist = classDao.findByGradeIdId(schoolId, gradeId, xqid);
		List<ClassInfoDTO> classInfoDTOList = classService.getClassByXqAndGrade(xqid,schoolId,gradeId);
		Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId,xqid);
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		List<Map<String,String>> head = new ArrayList<Map<String,String>>();// 标题list
		for(ClassInfoDTO classInfoDTO:classInfoDTOList){
			Map<String,String> map = new HashMap<String, String>();
			map.put("name", classInfoDTO.getGname()+"("+classInfoDTO.getXh()+")班");
			map.put("classId",classInfoDTO.getClassId());
			if(type == 1){
				map.put("dh","等级/合格");
				map.put("type",String.valueOf(1));
			}else{
				map.put("type",String.valueOf(2));
			}
			head.add(map);
			studentIds.addAll(MongoUtils.convertToObjectIdList(classInfoDTO.getStus()));
		}
		List<StudentEntry> studentEntries = N33_StudentDao.getStudentByXqidAndGradeId(gradeId,xqid);
		Integer count = 0;
		for (StudentEntry studentEntry : studentEntries) {
			if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
				count ++;
			}
		}
		List<StudentEntry> studentList = N33_StudentDao.getStuList(studentIds, xqid);// 查询各个班级的所有学生
		Map<ObjectId,List<StudentEntry>> classId_stus = new HashMap<ObjectId, List<StudentEntry>>();
		if(studentList!=null) {
			for(StudentEntry entry:studentList) {
				List<StudentEntry> templist = classId_stus.get(entry.getClassId())==null?new ArrayList<StudentEntry>():classId_stus.get(entry.getClassId());
				templist.add(entry);
				classId_stus.put(entry.getClassId(), templist);
			}
		}
		List<String> combinationNames = generateCombination(xqid, schoolId, gradeId, type);
		Map<String,Map<ObjectId,Integer>> name_class_num = new HashMap<String, Map<ObjectId,Integer>>();
		Map<String,List<Map<String,Object>>> name_StudentList=new HashMap<String, List<Map<String, Object>>>();
		for(String name:combinationNames) {
			List<Map<String,Object>> stuIds=new ArrayList<Map<String, Object>>();
			Map<ObjectId,Integer> class_num = name_class_num.get(name)==null?new HashMap<ObjectId, Integer>():name_class_num.get(name);
			for(ClassEntry klass:entrylist) {
				ObjectId klassId = klass.getClassId();
				Integer i = class_num.get(klassId)==null?0:class_num.get(klassId);
				List<StudentEntry> sutdentList = classId_stus.get(klass.getClassId());
				if(sutdentList!=null) {
					for(StudentEntry stu:sutdentList) {
						Map<String,Object> map=new HashMap<String, Object>();
						String combiname = stu.getCombiname();
						List<String> belongNames = judgeSelect(combinationNames, combiname, type);
						if(belongNames.contains(name)) {
							map.put("stuId",stu.getUserId().toString());
							map.put("clsXh",klass.getXh()+"");
							map.put("clsId",klass.getClassId().toString());
							map.put("stuName",stu.getUserName());
							map.put("stuNum",stu.getStudyNum());
							map.put("className",grade.getName() + "(" + klass.getXh() + ")班");
							if(stu.getSex() == 0){
								map.put("sex","女");
							}else if(stu.getSex() == 1){
								map.put("sex","男");
							}else{
								map.put("sex","未知");
							}
							if(stu.getLevel() == 0 || stu.getLevel() == null){
								map.put("level","无");
							}else{
								map.put("level",stu.getLevel());
							}
							if(stu.getCombiname() != null){
								map.put("combineName",stu.getCombiname());
							}else{
								map.put("combineName","");
							}
							stuIds.add(map);
							i++;
						}
					}
					
				}
				class_num.put(klassId, i);
				name_StudentList.put(name,stuIds);
			}
			name_class_num.put(name, class_num);
		}
		List<Map<String,Object>> content = new ArrayList<Map<String,Object>>();
		
		for(String name:combinationNames) {
			Map<String,Object> map = new HashMap<String, Object>();

			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			Map<ObjectId,Integer> class_num = name_class_num.get(name)==null?new HashMap<ObjectId, Integer>():name_class_num.get(name);
			Integer sum=0;
			for(ClassEntry klass:entrylist) {
				Map<String,String> newmap = new HashMap<String, String>();
				String i = class_num.get(klass.getClassId())==null?"":String.valueOf(class_num.get(klass.getClassId()));
				sum+=class_num.get(klass.getClassId())==null?0:class_num.get(klass.getClassId());
				newmap.put("num", i);
				if(type == 1){
					Integer xkStudent = 0;    //已经选科学生
					for (StudentEntry studentEntry : classId_stus.get(klass.getClassId())) {
						if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
							xkStudent ++;
						}
					}
					newmap.put("hgnum",String.valueOf(xkStudent - Integer.valueOf(i)));
					newmap.put("type",String.valueOf(1));
				}else{
					newmap.put("type",String.valueOf(2));
				}
				newmap.put("id", klass.getClassId().toString());
				list.add(newmap);
			}

			boolean flag = false;
			flag = judgeIsPrevious(type,sum,listLesson33DTO,name);

			if(flag){
				map.put("sum",sum);
				if(type == 1){
					map.put("name", name+"("+sum+"/" + (count - sum) + ")");
					map.put("type",String.valueOf(1));
				}else{
					map.put("name", name+"("+sum+")");
					map.put("type",String.valueOf(2));
				}
				map.put("subName", name);
				map.put("list", list);
				map.put("stuList",name_StudentList.get(name)==null?new ArrayList<Map<String,String>>():name_StudentList.get(name));
				content.add(map);
			}else{
				continue;
			}
		}
		result.put("head", head);
		result.put("content", content);
		Collections.sort(content, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return (Integer)o2.get("sum")-(Integer)o1.get("sum");
			}
		});
		return result;
	}

	public Map<String,Object> getStuSelectResultByClassForZhuanXiang(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,Integer type){

		SchoolLesson33DTO schoolLesson33DTO = getSchoolSelects(xqid,schoolId,gradeId);
		List<lesson33DTO> listLesson33DTO = schoolLesson33DTO.getList();

		Map<String,Object> result = new HashMap<String, Object>();
		//N33_ClassController.getIsolateClass(xqid.toString());// TODO
		List<ClassEntry> entrylist = classDao.findByGradeIdId(schoolId, gradeId, xqid);
		List<ClassInfoDTO> classInfoDTOList = classService.getClassByXqAndGrade(xqid,schoolId,gradeId);
		Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId,xqid);
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		List<Map<String,String>> head = new ArrayList<Map<String,String>>();// 标题list
		for(ClassInfoDTO classInfoDTO:classInfoDTOList){
			Map<String,String> map = new HashMap<String, String>();
			map.put("name", classInfoDTO.getGname()+"("+classInfoDTO.getXh()+")班");
			map.put("classId",classInfoDTO.getClassId());
			if(type == 1){
				map.put("dh","等级/合格");
				map.put("type",String.valueOf(1));
			}else{
				map.put("type",String.valueOf(2));
			}
			head.add(map);
			studentIds.addAll(MongoUtils.convertToObjectIdList(classInfoDTO.getStus()));
		}
		List<StudentEntry> studentEntries = N33_StudentDao.getStudentByXqidAndGradeId(gradeId,xqid);
		Integer count = 0;
		for (StudentEntry studentEntry : studentEntries) {
			if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
				count ++;
			}
		}
		List<StudentEntry> studentList = N33_StudentDao.getStuList(studentIds, xqid);// 查询各个班级的所有学生
		Map<ObjectId,List<StudentEntry>> classId_stus = new HashMap<ObjectId, List<StudentEntry>>();
		if(studentList!=null) {
			for(StudentEntry entry:studentList) {
				List<StudentEntry> templist = classId_stus.get(entry.getClassId())==null?new ArrayList<StudentEntry>():classId_stus.get(entry.getClassId());
				templist.add(entry);
				classId_stus.put(entry.getClassId(), templist);
			}
		}
		List<String> combinationNames = generateCombination(xqid, schoolId, gradeId, type);
		Map<String,Map<ObjectId,Integer>> name_class_num = new HashMap<String, Map<ObjectId,Integer>>();
		Map<String,List<Map<String,Object>>> name_StudentList=new HashMap<String, List<Map<String, Object>>>();
		for(String name:combinationNames) {
			List<Map<String,Object>> stuIds=new ArrayList<Map<String, Object>>();
			Map<ObjectId,Integer> class_num = name_class_num.get(name)==null?new HashMap<ObjectId, Integer>():name_class_num.get(name);
			for(ClassEntry klass:entrylist) {
				ObjectId klassId = klass.getClassId();
				Integer i = class_num.get(klassId)==null?0:class_num.get(klassId);
				List<StudentEntry> sutdentList = classId_stus.get(klass.getClassId());
				if(sutdentList!=null) {
					for(StudentEntry stu:sutdentList) {
						Map<String,Object> map=new HashMap<String, Object>();
						String combiname = stu.getCombiname();
						List<String> belongNames = judgeSelect(combinationNames, combiname, type);
						if(belongNames.contains(name)) {
							map.put("stuId",stu.getUserId().toString());
							map.put("clsXh",klass.getXh()+"");
							map.put("clsId",klass.getClassId().toString());
							map.put("stuName",stu.getUserName());
							map.put("stuNum",stu.getStudyNum());
							map.put("className",grade.getName() + "(" + klass.getXh() + ")班");
							if(stu.getSex() == 0){
								map.put("sex","女");
							}else if(stu.getSex() == 1){
								map.put("sex","男");
							}else{
								map.put("sex","未知");
							}
							if(stu.getLevel() == 0 || stu.getLevel() == null){
								map.put("level","无");
							}else{
								map.put("level",stu.getLevel());
							}
							if(stu.getCombiname() != null){
								map.put("combineName",stu.getCombiname());
							}else{
								map.put("combineName","");
							}
							stuIds.add(map);
							i++;
						}
					}

				}
				class_num.put(klassId, i);
				name_StudentList.put(name,stuIds);
			}
			name_class_num.put(name, class_num);
		}
		List<Map<String,Object>> content = new ArrayList<Map<String,Object>>();

		for(String name:combinationNames) {
			Map<String,Object> map = new HashMap<String, Object>();

			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			Map<ObjectId,Integer> class_num = name_class_num.get(name)==null?new HashMap<ObjectId, Integer>():name_class_num.get(name);
			Integer sum=0;
			for(ClassEntry klass:entrylist) {
				Map<String,String> newmap = new HashMap<String, String>();
				String i = class_num.get(klass.getClassId())==null?"":String.valueOf(class_num.get(klass.getClassId()));
				sum+=class_num.get(klass.getClassId())==null?0:class_num.get(klass.getClassId());
				newmap.put("num", i);
				if(type == 1){
					Integer xkStudent = 0;    //已经选科学生
					for (StudentEntry studentEntry : classId_stus.get(klass.getClassId())) {
						if(!"".equals(studentEntry.getCombiname()) && studentEntry.getCombiname() != null){
							xkStudent ++;
						}
					}
					newmap.put("hgnum",String.valueOf(xkStudent - Integer.valueOf(i)));
					newmap.put("type",String.valueOf(1));
				}else{
					newmap.put("type",String.valueOf(2));
				}
				newmap.put("id", klass.getClassId().toString());
				list.add(newmap);
			}

			boolean flag = false;
			flag = judgeIsPrevious(type,sum,listLesson33DTO,name);

			if(flag){
				map.put("sum",sum);
				if(type == 1){
					map.put("name", name);
					map.put("type",String.valueOf(1));
				}else{
					map.put("name", name);
					map.put("type",String.valueOf(2));
				}
				map.put("subName", name);
				map.put("list", list);
				map.put("stuList",name_StudentList.get(name)==null?new ArrayList<Map<String,String>>():name_StudentList.get(name));
				content.add(map);
			}else{
				continue;
			}
		}
		result.put("head", head);
		result.put("content", content);
		Collections.sort(content, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return (Integer)o2.get("sum")-(Integer)o1.get("sum");
			}
		});
		return result;
	}

	private boolean judgeIsPrevious(Integer type,Integer sum,List<lesson33DTO> list,String name){
		for (lesson33DTO dto:list) {
			if(type == 3){
				if(name.equals(dto.getName()) && (sum != 0 || dto.getState() != 0)){
					return true;
				}
			}
			if(type == 2){
				if(dto.getName().indexOf(name.substring(0, 1))>=0 && dto.getName().indexOf(name.substring(1, 2))>=0 && (sum != 0 || dto.getState() != 0)){
					return true;
				}
			}
			if(type == 1){
				if(dto.getName().indexOf(name)>=0 && (sum != 0 || dto.getState() != 0)) {
					return true;
				}
			}
		}
		return false;
	}

	private List<String> judgeSelect(List<String> combinationNames,String combiname,Integer type) {
		List<String> result = new ArrayList<String>();
		if(combiname!=null) {
			for(String name:combinationNames) {
				if(type==3&&name.equals(combiname)) {
					result.add(name);
				}
				if(type==2) {
					if(combiname.indexOf(name.substring(0, 1))>=0&&combiname.indexOf(name.substring(1, 2))>=0) {
						result.add(name);
					}
				}
				if(type==1&&combiname.indexOf(name)>=0) {
					result.add(name);
				}
			}
		}
		return result;
	}
	/**
	 * 生成学科组合
	 * @param xqid
	 * @param schoolId
	 * @param gradeId
	 * @param type
	 * @return
	 */
	private List<String> generateCombination(ObjectId xqid,ObjectId schoolId,ObjectId gradeId,Integer type){
		List<String> result = new ArrayList<String>();
		List<N33_KSDTO> N33_KSDTOList = subjectService.getIsolateSubjectByGradeIdIncludeMain(xqid, schoolId, gradeId.toString(),1);
		List<String> subjectNames = new ArrayList<String>();
		Collections.addAll(subjectNames, "物理", "化学", "生物", "政治", "历史", "地理");
		final Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
		for(N33_KSDTO dto:N33_KSDTOList) {
			if(subjectNames.contains(dto.getSnm()))
			{
			    subjectMap.put(new ObjectId(dto.getSubid()),dto.getSnm());
			}
		}
		List<ObjectId> subIds = new ArrayList<ObjectId>(subjectMap.keySet());
		List<CombineResult<ObjectId>>  ll=  new ArrayList<CombineResult<ObjectId>>();
	    CombineUtils.combinerSelect(subIds, type,ll); // 对现有学科进行组合与保存的进行比较
	    for(CombineResult<ObjectId> cr:ll) {
	    	String combineName=getCombineName(subjectMap,cr.getList());
	    	result.add(combineName);
	    }
	    return result;
	}
	/**
	 * 设置开启活着关闭
	 * @param id
	 * @param selectId
	 * @param state
	 */
	public void update(ObjectId id,String selectId,int state)
	{
		schoolSelectLessonSetDao.update(id, selectId, state);
	}

	public void updateNum(ObjectId id,String selectId,int num){
		schoolSelectLessonSetDao.updateNum(id, selectId, num);
	}
	
	private String getCombineName(ObjectId id1,ObjectId id2,ObjectId id3,Map<ObjectId, String> subjectMap)
	{
		
		String s1=subjectMap.get(id1);
		String s2=subjectMap.get(id2);
		String s3=subjectMap.get(id3);
		
		List<String> templist = Arrays.asList(s1,s2,s3);
		Collections.sort(templist,new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return subject_sort.get(o1)-subject_sort.get(o2);
			}
			
		});
		String result="";
		for(String s:templist) {
			result+=s.substring(0, 1);
		}
		return result;
	}
	
	private String getCombineName(Map<ObjectId, String> subjectMap,List<ObjectId> values)
	{
		List<String> templist = new ArrayList<String>();
		for(ObjectId id:values) {
			templist.add(subjectMap.get(id));
		}
		
		Collections.sort(templist,new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return subject_sort.get(o1)-subject_sort.get(o2);
			}
			
		});
		String result="";
		for(String s:templist) {
			result+=s.substring(0, 1);
		}
		return result;
	}
	
	public static class SchoolLesson33DTO
	{
		private String id;

		private String start;
		private Long startTime;
		private String end;
		private Long endTime;

		
		private List<lesson33DTO> list =new ArrayList<SchoolSelectLessonSetService.lesson33DTO>();
		
		public SchoolLesson33DTO(String id, List<lesson33DTO> list) {
			super();
			this.id = id;
			this.list = list;
		}
		public SchoolLesson33DTO(String id,Long st,Long ed, List<lesson33DTO> list) {
			super();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(st!=null){
				start = sf.format(new Date(st));
			}
			else{
				start = "";
			}
			if(ed!=null){
				end = sf.format(new Date(ed));
			}
			else{
				end = "";
			}
			this.id = id;
			this.list = list;
			this.startTime = st;
			this.endTime = ed;
		}
		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public List<lesson33DTO> getList() {
			return list;
		}
		public void setList(List<lesson33DTO> list) {
			this.list = list;
		}
		public Long getStartTime() {
			return startTime;
		}
		public void setStartTime(Long startTime) {
			this.startTime = startTime;
		}
		public Long getEndTime() {
			return endTime;
		}
		public void setEndTime(Long endTime) {
			this.endTime = endTime;
		}
		
	}
	
	
	/**
	 * 排列组合
	 * @author fourer
	 *
	 */
	public static class lesson33DTO
	{
		private String id;
		private String name;
		public ObjectId subjectId1;
		public ObjectId subjectId2;
		public ObjectId subjectId3;


		public String subjectId1Str;
		public String subjectId2Str;
		public String subjectId3Str;
		private int state;
		private Integer num;
		private String sub1Str;
		private String sub2Str;
		private String sub3Str;
		
		
		public lesson33DTO(){}
		
		public lesson33DTO(SelectLessons ses){
			this.id=ses.getId();
			this.name=ses.getName();
			this.subjectId1=ses.getSubject1();
			this.subjectId2=ses.getSubject2();
			this.subjectId3=ses.getSubject3();
			this.subjectId1Str=ses.getSubject1().toHexString();
			this.subjectId2Str=ses.getSubject2().toHexString();
			this.subjectId3Str=ses.getSubject3().toHexString();
			this.state=ses.getState();
			this.num = ses.getNum();
		}
		public SelectLessons convert()
		{
			SelectLessons ses=new SelectLessons(getId(), name, subjectId1, subjectId2, subjectId3, state,num);
			
			return ses;
		}
		
		
		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public ObjectId getSubjectId1() {
			return subjectId1;
		}


		public void setSubjectId1(ObjectId subjectId1) {
			this.subjectId1 = subjectId1;
		}


		public ObjectId getSubjectId2() {
			return subjectId2;
		}


		public void setSubjectId2(ObjectId subjectId2) {
			this.subjectId2 = subjectId2;
		}


		public ObjectId getSubjectId3() {
			return subjectId3;
		}


		public void setSubjectId3(ObjectId subjectId3) {
			this.subjectId3 = subjectId3;
		}



		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}



		public String getSubjectId1Str() {
			return subjectId1Str;
		}

		public void setSubjectId1Str(String subjectId1Str) {
			this.subjectId1Str = subjectId1Str;
		}

		public String getSubjectId2Str() {
			return subjectId2Str;
		}

		public void setSubjectId2Str(String subjectId2Str) {
			this.subjectId2Str = subjectId2Str;
		}

		public String getSubjectId3Str() {
			return subjectId3Str;
		}

		public void setSubjectId3Str(String subjectId3Str) {
			this.subjectId3Str = subjectId3Str;
		}

		public String getId() {
			
			if(StringUtils.isNotBlank(this.id))
				return this.id;
			String str=  String.valueOf(this.subjectId1.toString().substring(this.subjectId1.toString().length()-5));
			if(null!=this.subjectId2)
			{
				str+="_"+this.subjectId2.toString().substring(this.subjectId2.toString().length()-5);
			}
			if(null!=this.subjectId3)
			{
				str+="_"+this.subjectId3.toString().substring(this.subjectId3.toString().length()-5);
			}
			return str;
		}

		public void setId(String id) {
			this.id=id;
		}

		public Integer getNum() {
			return num;
		}

		public void setNum(Integer num) {
			this.num = num;
		}

		public String getSub1Str() {
			if(null!=this.subjectId1)
			{
				return this.subjectId1.toString();
			}
			return "";
		}

		public void setSub1Str(String sub1Str) {
		}

		public String getSub2Str() {
			if(null!=this.subjectId2)
			{
				return this.subjectId2.toString();
			}
			return "";
		}

		public void setSub2Str(String sub2Str) {
		}

		public String getSub3Str() {
			if(null!=this.subjectId3)
			{
				return this.subjectId3.toString();
			}
			return "";
		}

		public void setSub3Str(String sub3Str) {
			this.sub3Str = sub3Str;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((subjectId1 == null) ? 0 : subjectId1.hashCode());
			result = prime * result
					+ ((subjectId2 == null) ? 0 : subjectId2.hashCode());
			result = prime * result
					+ ((subjectId3 == null) ? 0 : subjectId3.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			lesson33DTO other = (lesson33DTO) obj;
			if (subjectId1 == null) {
				if (other.subjectId1 != null)
					return false;
			} else if (!subjectId1.equals(other.subjectId1))
				return false;
			if (subjectId2 == null) {
				if (other.subjectId2 != null)
					return false;
			} else if (!subjectId2.equals(other.subjectId2))
				return false;
			if (subjectId3 == null) {
				if (other.subjectId3 != null)
					return false;
			} else if (!subjectId3.equals(other.subjectId3))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "lesson33DTO [id=" + id + ", name=" + name + ", subjectId1="
					+ subjectId1 + ", subjectId2=" + subjectId2
					+ ", subjectId3=" + subjectId3 + ", state=" + state + "]";
		}
		
		
	}
}
