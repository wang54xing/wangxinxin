package com.fulaan.new33.service;

import com.db.new33.N33_GDSXDao;
import com.db.new33.N33_GradeWeekRangeDao;
import com.db.new33.N33_StudentTagDao;
import com.db.new33.N33_TiaoKeShengQingDao;
import com.db.new33.isolate.*;
import com.db.new33.paike.*;
import com.fulaan.new33.dto.N33_TiaoKeShengQingDTO;
import com.fulaan.new33.dto.autopk.N33_SubjectSetDTO;
import com.fulaan.new33.dto.autopk.N33_ZouBanSetDTO;
import com.fulaan.new33.dto.isolate.*;
import com.fulaan.new33.dto.paike.N33_FaBuLogDTO;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.dto.paike.N33_YKBDTO;
import com.fulaan.new33.service.autopk.N33_AutoTeaSetService;
import com.fulaan.new33.service.autopk.N33_SubjectSetService;
import com.fulaan.new33.service.autopk.N33_ZouBanSetService;
import com.fulaan.new33.service.isolate.*;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.N33_TiaoKeShengQingEntry;
import com.pojo.new33.isolate.*;
import com.pojo.new33.paike.*;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.LabelUI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by wang_xinxin on 2018/3/12.
 */
@Service
public class PaiKeService extends BaseService {

	private N33_ZKBDao zkbDao = new N33_ZKBDao();

	private N33_TiaoKeShengQingDao tiaoKeShengQingDao = new N33_TiaoKeShengQingDao();

	private N33_JXBDao jxbDao = new N33_JXBDao();

	private N33_YKBDao ykbDao = new N33_YKBDao();

	private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();

	private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();

	private N33_TkLogDao tkLogDao = new N33_TkLogDao();

	private N33_SWDao swDao = new N33_SWDao();

	private N33_TeaDao teaDao = new N33_TeaDao();

	private N33_StudentTagDao studentTagDao = new N33_StudentTagDao();

    private N33_StudentDao studentDao = new N33_StudentDao();

    private N33_ClassDao classDao = new N33_ClassDao();

    private TermDao termDao = new TermDao();

    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();

    private N33_ZhuanXiangDao zhuanXiangDao = new N33_ZhuanXiangDao();

    private N33_ZIXIKEDao zixikeDao = new N33_ZIXIKEDao();

    private GradeDao gradeDao = new GradeDao();

    private N33_FaBuLogDao faBuLogDao = new N33_FaBuLogDao();

    private N33_GradeWeekRangeDao n33_gradeWeekRangeDao = new N33_GradeWeekRangeDao();

    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();
    
    @Autowired
    private N33_TurnOffService turnOffService;

    @Autowired
	private N33_SWService swService;

    @Autowired
	private CourseRangeService courseRangeService;

    @Autowired
	private IsolateSubjectService subjectService;

    @Autowired
	private N33_ClassroomService classroomService;

    @Autowired
    private IsolateGradeService gradeService ;

	@Autowired
	private N33_StudentTagService tagService;

	@Autowired
	private IsolateTermService termService;

	@Autowired
	private N33_GDSXService gdsxService;

	@Autowired
	private N33_PaiKeZuHeService paiKeZuHeService;

	@Autowired
	private N33_AutoTeaSetService autoTeaSetService;

	@Autowired
	private N33_ZouBanSetService zouBanSetService;

	@Autowired
	private N33_SubjectSetService subjectSetService;

	/**
	 * 生成导入教学班的模板
	 *
	 * @param response
	 */
	public void exportTemplate(HttpServletResponse response) {
		HSSFWorkbook wb = new HSSFWorkbook();
		//生成二个sheet
		for (int i = 0; i < 2; i++) {
			HSSFSheet sheet = wb.createSheet("化学D" + (i + 1));
			//为sheet1生成第一行，用于放表头信息
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("学号");
			cell = row.createCell(1);
			cell.setCellValue("学生姓名");
			cell = row.createCell(2);
			cell.setCellValue("教学班名");
			cell = row.createCell(3);
			cell.setCellValue("行政班名");
			cell = row.createCell(4);
			cell.setCellValue("年级");
			cell = row.createCell(5);
			cell.setCellValue("学科");
			cell = row.createCell(6);
			cell.setCellValue("授课老师");
			cell = row.createCell(7);
			cell.setCellValue("教学班类型(D:等级考,H:合格考)");

			HSSFRow row2 = sheet.createRow(1);
			HSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue(String.valueOf(2018020305 + i));
			cell2 = row2.createCell(1);
			if (i == 0) {
				cell2.setCellValue("张三");
			} else {
				cell2.setCellValue("李四");
			}
			cell2 = row2.createCell(2);
			cell2.setCellValue("化学D" + (i + 1));
			cell2 = row2.createCell(3);
			cell2.setCellValue("高一（" + (i + 1) + "）班");
			cell2 = row2.createCell(4);
			cell2.setCellValue("高一");
			cell2 = row2.createCell(5);
			cell2.setCellValue("化学");
			cell2 = row2.createCell(6);
			cell2.setCellValue("化学老师一");
			cell2 = row2.createCell(7);
			cell2.setCellValue("D");

			String[] list = new String[2];
			list[0] = "D";
			list[1] = "H";
			CellRangeAddressList regions = new CellRangeAddressList(1, 1000, 7, 7);
			DVConstraint constraint = DVConstraint.createExplicitListConstraint(list);
			HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
			sheet.addValidationData(data_validation);
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
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("导入教学班.xls", "UTF-8"));
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
	 * 导入教学班
	 *
	 * @param schoolId
	 * @param inputStream
	 */
	public List<Map<String, Object>> importJXBExcel(String cid, String schoolId, InputStream inputStream) throws Exception {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		try {
			int sheetCount = workbook.getNumberOfSheets();
			List<Map> datasList = new ArrayList<Map>();
			for (int i = 0; i < sheetCount; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				Map classData = new HashMap();
				List<Map> datas = new ArrayList<Map>();
				for (int j = 1; j <= sheet.getLastRowNum(); j++) {
					Map data = new HashMap();
					HSSFRow row = sheet.getRow(j);
					String jxClassName = row.getCell(2).getStringCellValue().trim();
					classData.put("className", jxClassName);
					String jxbType = null;
					if (row.getCell(7) != null) {
						jxbType = row.getCell(7).getStringCellValue().trim();
					}
					if (classData.get("type") == null) {
						Integer type = null;
						if ("D".equals(jxbType)) {
							type = 1;
						} else if ("H".equals(jxbType)) {
							type = 2;
						}
						classData.put("type", type);
					}
					String gradeName = row.getCell(4) == null ? "" : row.getCell(4).getStringCellValue().trim();
					classData.put("gradeName", gradeName);
					String subjectName = row.getCell(5) == null ? "" : row.getCell(5).getStringCellValue().trim();
					classData.put("subjectName", subjectName);
					String teaName = row.getCell(6) == null ? "" : row.getCell(6).getStringCellValue().trim();
					classData.put("teaName", teaName);
					String stuNum = row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue().trim();
					data.put("stuNum", stuNum);
					String stuName = row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue().trim();
					data.put("stuName", stuName);
					String className = row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue().trim();
					data.put("className", className);
					datas.add(data);
				}
				classData.put("datas", datas);
				datasList.add(classData);
			}
			Map<String, String> grdMap = gradeService.getCurrGradeMap(new ObjectId(schoolId), new ObjectId(cid));
			Map<String, String> subMap = subjectService.getSubjectMap(new ObjectId(schoolId), new ObjectId(cid));
			Map<String, String> teaMap = teaDao.getTeacherMap(new ObjectId(schoolId));
			List<N33_JXBDTO> dtos = new ArrayList<N33_JXBDTO>();

			Map<ObjectId, N33_TurnOff> gradeTurnOffMap = turnOffDao.getGradeTurnOffMap(new ObjectId(schoolId), new ObjectId(cid),1);

			for (Map classData : datasList) {
				List<Map> datas = classData.get("datas") == null ? new ArrayList<Map>() : (List<Map>) classData.get("datas");
				N33_JXBDTO dto = new N33_JXBDTO();
				dto.setSchoolId(schoolId);
				dto.setTermId(cid);
				dto.setIndex(0);
				if (classData.get("type") != null) {
					Integer type = new Double(classData.get("type").toString()).intValue();
					dto.setType(type);
				} else {
					for (Map data : datas) {
						Map map = new HashMap();
						map.put("stuNum", data.get("stuNum"));
						map.put("stuName", data.get("stuName"));
						map.put("jxbName", classData.get("className"));
						map.put("className", data.get("className"));
						map.put("grade", classData.get("gradeName"));
						map.put("subject", classData.get("subjectName"));
						map.put("teaName", classData.get("teaName"));
						map.put("errorMessage", "未设置教学班类型");
						//map.put("type","");
						returnList.add(map);
					}
					continue;
				}
				String className = classData.get("className") == null ? "" : classData.get("className").toString();
				String gradeName = classData.get("gradeName") == null ? "" : classData.get("gradeName").toString();
				String gradeId = grdMap.get(gradeName) == null ? null : grdMap.get(gradeName).toString();

				int acClassType = 0;
				N33_TurnOff turnOff = gradeTurnOffMap.get(gradeId);
				if(null!=turnOff){
					acClassType = turnOff.getAcClass();
				}
				dto.setAcClassType(acClassType);
				N33_JXBEntry jxbEntry = jxbDao.getJXBEntry(className, new ObjectId(cid),new ObjectId(gradeId), acClassType);
				if (className == "" || jxbEntry != null) {
					for (Map data : datas) {
						Map map = new HashMap();
						map.put("stuNum", data.get("stuNum"));
						map.put("stuName", data.get("stuName"));
						map.put("jxbName", classData.get("className"));
						map.put("className", data.get("className"));
						map.put("grade", classData.get("gradeName"));
						map.put("subject", classData.get("subjectName"));
						map.put("teaName", classData.get("teaName"));
						map.put("errorMessage", "教学班重名或未设置教学班名");
						//map.put("type","");
						returnList.add(map);
					}
					continue;
				}
				dto.setName(className);
				dto.setNickName(className);
				dto.setGradeId(gradeId);
				String subjectName = classData.get("subjectName") == null ? "" : classData.get("subjectName").toString();
				String subjectId = subMap.get(subjectName) == null ? "" : subMap.get(subjectName).toString();
				dto.setSubjectId(subjectId);
				String teaName = classData.get("teaName") == null ? "" : classData.get("teaName").toString();
				String teaId = teaMap.get(teaName) == null ? null : teaMap.get(teaName).toString();
				dto.setTeacherId(teaId);
				List<String> stuIds = new ArrayList<String>();
				for (Map data : datas) {
					String stuNum = data.get("stuNum") == null ? "" : data.get("stuNum").toString();
					String stuName = data.get("stuName") == null ? "" : data.get("stuName").toString();
					List<StudentEntry> stuList = studentDao.getStuListByStuNumAndStuNameAndClassExact(stuNum, stuName, new ObjectId(schoolId), new ObjectId(cid));
					if (stuList.size() > 1 || stuList.size() == 0) {
						Map map = new HashMap();
						map.put("stuNum", data.get("stuNum"));
						map.put("stuName", data.get("stuName"));
						map.put("jxbName", classData.get("className"));
						map.put("className", data.get("className"));
						map.put("grade", classData.get("gradeName"));
						map.put("subject", classData.get("subjectName"));
						map.put("teaName", classData.get("teaName"));
						map.put("errorMessage", "暂无该学生或存在多个相同名字的学生");
						//map.put("type","");
						returnList.add(map);
						continue;
					} else if (stuList.size() == 1) {
						String stuId = stuList.get(0).getUserId().toString();
						if (!"".equals(stuId)) {
							stuIds.add(stuId);
						}
					}
				}
				dto.setStudentIds(stuIds);
				dto.setRl(stuIds.size());

				dtos.add(dto);
			}
			if (dtos.size() > 0) {
				addJXBDTOs(dtos);
			}

			//生成教学班冲突
			TermEntry termEntry = termDao.getTermByTimeId(new ObjectId(cid));
			for (TermEntry.PaiKeTimes paiKeTimes : termEntry.getPaiKeTimes()) {
				if (paiKeTimes.getID().toString().equals(cid)) {
					//创建年级
					for (ObjectId grade : paiKeTimes.getGradeIds()) {
						tagService.conflictDetection(new ObjectId(schoolId), grade, new ObjectId(cid));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("导入失败！\n" + e.getMessage());
		} finally {
			inputStream.close();
		}

		return returnList;
	}

	public void addJXBDTOs(List<N33_JXBDTO> dtos) {
		for (N33_JXBDTO dto : dtos) {
			N33_JXBEntry entry = new N33_JXBEntry();
			if (dto.getName() != null) {
				entry.setName(dto.getName());
			}
			entry.setNickName("");
			if (dto.getSubjectId() != null) {
				entry.setSubjectId(new ObjectId(dto.getSubjectId()));
			}
			entry.setAcClassType(dto.getAcClassType());
			entry.setClassroomId(null);
			if (dto.getTeacherId() != null) {
				entry.setTercherId(new ObjectId(dto.getTeacherId()));
			}
			if (dto.getStudentIds() != null) {
				List<String> list = dto.getStudentIds();
				List<ObjectId> addList = new ArrayList<ObjectId>();
				for (String s : list) {
					addList.add(new ObjectId(s));
				}
				entry.setStudentIds(addList);
			}
			if (dto.getSchoolId() != null) {
				entry.setSchoolId(new ObjectId(dto.getSchoolId()));
			}
			if (dto.getTermId() != null) {
				entry.setTermId(new ObjectId(dto.getTermId()));
			}
			if (dto.getGradeId() != null) {
				entry.setGradeId(new ObjectId(dto.getGradeId()));
			}
			entry.setIndex(dto.getIndex());
			entry.setType(dto.getType());
			entry.setRongLiang(dto.getRl());
			entry.setIr(0);
			entry.setDanOrShuang(0);
			jxbDao.addN33_JXBEntry(entry);
		}
	}


	/**
	 * 加载课表list
	 *
	 * @param gradeId
	 * @param classRoomIds
	 * @param weeks
	 * @param indexs
	 * @param schoolId
	 */
	public Map getKeBiaoList(ObjectId termId, String gradeId, String classRoomIds, String weeks, String indexs, ObjectId schoolId) {
		Map map = new HashMap();
		List<N33_ClassroomDTO> classroomDTOs = classroomService.getRoomEntryListByXqGrade(termId, schoolId, new ObjectId(gradeId), 1);
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), termId);
		List<ObjectId> classroomIds = new ArrayList<ObjectId>();
		if (!StringUtils.isEmpty(classRoomIds)) {
			classroomIds = MongoUtils.convert(classRoomIds);
		}
		if (classroomIds == null || classroomIds.size() == 0) {
			if (classroomDTOs != null && classroomDTOs.size() != 0) {
				for (N33_ClassroomDTO dto : classroomDTOs) {
					classroomIds.add(new ObjectId(dto.getRoomId()));
				}
			}
		}
		N33_GradeWeekRangeEntry gradeWeekRangeEntry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(termId, schoolId, new ObjectId(gradeId));
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
		checkKS(schoolId, termId, classroomIds, courseRangeDTOs.size(), gradeWeekRangeEntry);
		List<Integer> weekIds = new ArrayList<Integer>();
		int weekCount = gradeWeekRangeEntry.getEnd() - gradeWeekRangeEntry.getStart() + 1;
		if (!StringUtils.isEmpty(weeks)) {
			String[] weekArg = weeks.split(",");
			if (weekArg != null && weekArg.length == 1 && weekArg[0].equals("0")) {
				for (int i = 0; i < weekCount; i++) {
					weekIds.add(i);
				}
			} else {
				for (String week : weekArg) {
					weekIds.add(Integer.valueOf(week) - 1);
				}
				weekCount = weekIds.size();
			}
		}
		List<Integer> indexIds = new ArrayList<Integer>();
		if (!StringUtils.isEmpty(indexs)) {
			String[] indexArg = indexs.split(",");
			if (indexArg != null && indexArg.length == 1 && indexArg[0].equals("0")) {

			} else {
				for (String index : indexArg) {
					indexIds.add(Integer.valueOf(index) - 1);
				}
			}
		}
		Map<String, List<N33_YKBDTO>> ykbDTOMap = new HashMap<String, List<N33_YKBDTO>>();
		Map<String, List<N33_YKBDTO>> ykbDTOMap2 = new HashMap<String, List<N33_YKBDTO>>();
		List<String> swlist = new ArrayList<String>();
		TermEntry termEntry = termDao.getTermByTimeId(termId);

		List<N33_SWDTO> swdtos = swService.getGuDingShiWuBySchoolId(termEntry.getID(), schoolId);

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
		List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(termEntry.getID(), schoolId, new ObjectId(gradeId));
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

		List<N33_YKBDTO> n33_ykbdtos = new ArrayList<N33_YKBDTO>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBsByclassRoomIds(termId, schoolId, classroomIds, weekIds, indexIds);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "tid");
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, termId);
			List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "jxbId");
			if (MongoUtils.getFieldObjectIDs(ykbEntryList, "nJxbId") != null && MongoUtils.getFieldObjectIDs(ykbEntryList, "nJxbId").size() != 0) {
				jxbIds.addAll(MongoUtils.getFieldObjectIDs(ykbEntryList, "nJxbId"));
			}
			Map<ObjectId, N33_ZIXIKEEntry> zixikeEntryMap = zixikeDao.getJXBsBySubIdsByMap(termId);
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

			for (N33_YKBEntry ykbEntry : ykbEntryList) {
				n33_ykbdtos.add(new N33_YKBDTO(ykbEntry));
				List<N33_YKBDTO> n33_ykbdtos1 = ykbDTOMap.get(ykbEntry.getClassroomId().toString());
				if (n33_ykbdtos1 == null || n33_ykbdtos1.size() == 0) {
					n33_ykbdtos1 = new ArrayList<N33_YKBDTO>();
				}
				N33_YKBDTO ykbdto = new N33_YKBDTO(ykbEntry);
				if (StringUtils.isNotEmpty(ykbdto.getTeacherId())) {
					ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())).getUserName() : "");
				} else {
					ykbdto.setTeacherName("");
				}
				if (StringUtils.isNotEmpty(ykbdto.getNteacherId())) {
					ykbdto.setNteacherName(teaEntryMap.get(new ObjectId(ykbdto.getNteacherId())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getNteacherId())).getUserName() : "");
				} else {
					ykbdto.setNteacherName("");
				}
				if (StringUtils.isNotEmpty(ykbdto.getJxbId())) {
					N33_JXBEntry jxbEntry = jxbEntryMap.get(new ObjectId(ykbdto.getJxbId()));
					String jxbNm = "";
					if (jxbEntry != null) {
						if (StringUtils.isNotEmpty(jxbEntry.getNickName())) {
							jxbNm = jxbEntry.getNickName();
						} else {
							jxbNm = jxbEntry.getName();
						}
					}
					ykbdto.setJxbName(jxbNm);
					ykbdto.setStudentCount(jxbEntry != null ? jxbEntry.getStudentIds().size() : 0);
					ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getJxbId())) : "");
				}
				if (ykbEntry.getType() == 5 && ykbEntry.getJxbId() != null) { // 设置自习班信息
					N33_ZIXIKEEntry zixikeEntry = zixikeEntryMap.get(new ObjectId(ykbdto.getJxbId()));
					if (zixikeEntry != null) {
						ykbdto.setJxbName(zixikeEntry.getName());
						ykbdto.setStudentCount(zixikeEntry.getStudentIds().size());
					}
				}
				if (StringUtils.isNotEmpty(ykbdto.getNjxbId())) {
					N33_JXBEntry jxbEntry2 = jxbEntryMap.get(new ObjectId(ykbdto.getNjxbId()));
					String jxbNm2 = "";
					if (jxbEntry2 != null) {
						if (StringUtils.isNotEmpty(jxbEntry2.getNickName())) {
							jxbNm2 = jxbEntry2.getNickName();
						} else {
							jxbNm2 = jxbEntry2.getName();
						}
					}
					ykbdto.setNjxbName(jxbNm2);
					ykbdto.setNstudentCount(jxbEntry2 != null ? jxbEntry2.getStudentIds().size() : 0);
					ykbdto.setNremarks(tagMaps.get(new ObjectId(ykbdto.getNjxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getNjxbId())) : "");
				}
				ykbdto.setSwType(0);
				if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY())) {
					ykbdto.setSwType(1);
					ykbdto.setRemarks(swdtoMap.get(ykbEntry.getX() + "," + ykbEntry.getY()).getDesc());
				}
				if (gdsxList != null && gdsxList.size() != 0 && gdsxList.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY())) {
					ykbdto.setSwType(2);
					ykbdto.setRemarks(gdsxdtoMap.get(ykbEntry.getX() + "," + ykbEntry.getY()).getDesc());
				}
				ykbdto.setWeekCount(weekCount);
				n33_ykbdtos1.add(ykbdto);
				ykbDTOMap.put(ykbEntry.getClassroomId().toString(), n33_ykbdtos1);
			}
		}
		for (Map.Entry<String, List<N33_YKBDTO>> entry : ykbDTOMap.entrySet()) {
			List<N33_YKBDTO> ykbdtos = entry.getValue();
			Collections.sort(ykbdtos, new Comparator<N33_YKBDTO>() {
				@Override
				public int compare(N33_YKBDTO o1, N33_YKBDTO o2) {
					return o1.getY() - o2.getY();
				}
			});
			ykbDTOMap.put(entry.getKey(), ykbdtos);
		}
		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			for (N33_ClassroomDTO dto : classroomDTOs) {
				dto.setWeekList(weekList);
				ykbDTOMap2.put(dto.getRoomId(), ykbDTOMap.get(dto.getRoomId()));
			}
		}
		map.put("courseRangeDTOs", courseRangeDTOs);
		map.put("ykbdto", ykbDTOMap2);
		map.put("classrooms", classroomDTOs);
		return map;
	}

	/**
	 * @param schoolId
	 * @param termId
	 * @param classroomIds
	 * @param count        课节数
	 */
	private void checkKS(ObjectId schoolId, ObjectId termId, List<ObjectId> classroomIds, int count, N33_GradeWeekRangeEntry gradeWeekRangeEntry) {
		Map<String, N33_YKBEntry> ykbEntryHashMap = new HashMap<String, N33_YKBEntry>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBsByclassRoomIds(termId, schoolId, classroomIds);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
				ykbEntryHashMap.put(entry.getClassroomId().toString() + entry.getX() + entry.getY(), entry);
			}
		}
		List<N33_YKBEntry> ykbEntries = new ArrayList<N33_YKBEntry>();
		for (ObjectId id : classroomIds) {
			for (int i = gradeWeekRangeEntry.getStart() - 1; i < gradeWeekRangeEntry.getEnd(); i++) {
				for (int j = 0; j < count; j++) {
					if (ykbEntryHashMap.get(id.toString() + i + j) == null) {
						ykbEntries.add(new N33_YKBEntry(i, j, id, schoolId, termId));
					}
				}
			}
		}
		if (ykbEntries != null && ykbEntries.size() != 0) {
			ykbDao.addN33_YKBEntrys(ykbEntries);
		}
	}


	/**
	 * 根据老师排课
	 *
	 * @param x
	 * @param y
	 * @param jxbId
	 */
	public void saveKeBiaoInfoByTeacher(String ciId, String x, String y, String jxbId) {
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
		if (jxbEntry.getType() != 4) {
			ObjectId classRoomId = jxbEntry.getClassroomId();
			N33_YKBEntry ykbEntry = ykbDao.getN33_YKBByClassRoom(Integer.valueOf(x), Integer.valueOf(y), classRoomId, new ObjectId(ciId));
			if (ykbEntry.getJxbId() != null) {
				jxbksDao.updateN33_JXBKSMinusCountByJxbId(ykbEntry.getJxbId());
			}
			if (ykbEntry.getNJxbId() != null) {
				jxbksDao.updateN33_JXBKSMinusCountByJxbId(ykbEntry.getNJxbId());
			}
			if (jxbEntry.getDanOrShuang() != 0) {
				N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
				if(jxbEntry.getDanOrShuang() == 1 && jxbEntry2.getDanOrShuang() == 2){
					ykbEntry.setJxbId(new ObjectId(jxbId));
					ykbEntry.setIsUse(Constant.ONE);
					ykbEntry.setGradeId(jxbEntry.getGradeId());
					ykbEntry.setSubjectId(jxbEntry.getSubjectId());
					ykbEntry.setTeacherId(jxbEntry.getTercherId());
					ykbEntry.setType(jxbEntry.getType());
					ykbEntry.setNSubjectId(jxbEntry2.getSubjectId());
					ykbEntry.setNTeacherId(jxbEntry2.getTercherId());
					ykbEntry.setNJxbId(jxbEntry2.getID());
				}else if (jxbEntry.getDanOrShuang() == 2 && jxbEntry2.getDanOrShuang() == 1){
					ykbEntry.setJxbId(jxbEntry2.getID());
					ykbEntry.setIsUse(Constant.ONE);
					ykbEntry.setGradeId(jxbEntry2.getGradeId());
					ykbEntry.setSubjectId(jxbEntry2.getSubjectId());
					ykbEntry.setTeacherId(jxbEntry2.getTercherId());
					ykbEntry.setType(jxbEntry2.getType());
					ykbEntry.setNSubjectId(jxbEntry.getSubjectId());
					ykbEntry.setNTeacherId(jxbEntry.getTercherId());
					ykbEntry.setNJxbId(new ObjectId(jxbId));
				}
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
			N33_JXBKSEntry entry = jxbksDao.getJXBKSsByJXBId(new ObjectId(jxbId));
			if (entry == null) {
				jxbksDao.addN33_JXBKSEntry(new N33_JXBKSEntry(new ObjectId(jxbId), jxbEntry.getSubjectId(), 1, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang()));
			} else {
				jxbksDao.updateN33_JXBKSCountByJxbId(new ObjectId(jxbId));
			}
			if(jxbEntry.getDanOrShuang() == 0){
				ykbEntry.setJxbId(new ObjectId(jxbId));
				ykbEntry.setIsUse(Constant.ONE);
				ykbEntry.setGradeId(jxbEntry.getGradeId());
				ykbEntry.setSubjectId(jxbEntry.getSubjectId());
				ykbEntry.setTeacherId(jxbEntry.getTercherId());
				ykbEntry.setType(jxbEntry.getType());
			}
			ykbDao.updateN33_YKB(ykbEntry);
		} else {
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(new ObjectId(jxbId), jxbEntry.getTermId());
			if (zhuanXiangEntryList != null && zhuanXiangEntryList.size() != 0) {
				List<ObjectId> classroomIds = MongoUtils.getFieldObjectIDs(zhuanXiangEntryList, "rid");
				Map<ObjectId, N33_YKBEntry> ykbEntryMap = ykbDao.getJXB(jxbEntry.getTermId(), classroomIds, Integer.valueOf(x), Integer.valueOf(y), jxbEntry.getSchoolId());
				for (N33_ZhuanXiangEntry e : zhuanXiangEntryList) {
					N33_YKBEntry ykb = ykbEntryMap.get(e.getRoomId());
					ykb.setJxbId(new ObjectId(jxbId));
					ykb.setIsUse(Constant.ONE);
					ykb.setGradeId(jxbEntry.getGradeId());
					ykb.setSubjectId(jxbEntry.getSubjectId());
					//ykb.setTeacherId(e.getTeaId());
					ykb.setType(jxbEntry.getType());
					ykbDao.updateN33_YKB(ykb);
				}
			}
			N33_JXBKSEntry entry = jxbksDao.getJXBKSsByJXBId(new ObjectId(jxbId));
			if (entry == null) {
				jxbksDao.addN33_JXBKSEntry(new N33_JXBKSEntry(new ObjectId(jxbId), jxbEntry.getSubjectId(), 1, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang()));
			} else {
				jxbksDao.updateN33_JXBKSCountByJxbId(new ObjectId(jxbId));
			}
		}
	}

	/**
	 * 课表插课
	 *
	 * @param ykbId
	 * @param jxbId
	 */
	public void saveKeBiaoInfo(String ykbId, String jxbId, String oykbId) {
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
		N33_YKBEntry ykbEntry = ykbDao.getN33_YKBById(new ObjectId(ykbId));
		if (jxbEntry.getType() != 4) {
			if (jxbEntry.getDanOrShuang() != 0) {
				N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
				//ykbEntry.setJxbId(new ObjectId(jxbId));
				ykbEntry.setNSubjectId(jxbEntry2.getSubjectId());
				ykbEntry.setNTeacherId(jxbEntry2.getTercherId());
				ykbEntry.setType(jxbEntry2.getType());
				ykbEntry.setNJxbId(jxbEntry2.getID());
				if(jxbEntry.getDanOrShuang() == 1 && jxbEntry2.getDanOrShuang() == 2){
					ykbEntry.setJxbId(new ObjectId(jxbId));
					ykbEntry.setIsUse(Constant.ONE);
					ykbEntry.setGradeId(jxbEntry.getGradeId());
					ykbEntry.setSubjectId(jxbEntry.getSubjectId());
					ykbEntry.setType(jxbEntry.getType());
					ykbEntry.setTeacherId(jxbEntry.getTercherId());
					ykbEntry.setNSubjectId(jxbEntry2.getSubjectId());
					ykbEntry.setNTeacherId(jxbEntry2.getTercherId());
					ykbEntry.setNJxbId(jxbEntry2.getID());
				}else if(jxbEntry.getDanOrShuang() == 2 && jxbEntry2.getDanOrShuang() == 1){
					ykbEntry.setJxbId(jxbEntry2.getID());
					ykbEntry.setIsUse(Constant.ONE);
					ykbEntry.setGradeId(jxbEntry2.getGradeId());
					ykbEntry.setSubjectId(jxbEntry2.getSubjectId());
					ykbEntry.setType(jxbEntry2.getType());
					ykbEntry.setTeacherId(jxbEntry2.getTercherId());
					ykbEntry.setNSubjectId(jxbEntry.getSubjectId());
					ykbEntry.setNTeacherId(jxbEntry.getTercherId());
					ykbEntry.setNJxbId(jxbEntry.getID());
				}
			}
			if (StringUtils.isNotEmpty(oykbId)) {
				N33_YKBEntry oYkbEntry = ykbDao.getN33_YKBById(new ObjectId(oykbId));
				oYkbEntry.setJxbId(ykbEntry.getJxbId());
				oYkbEntry.setGradeId(ykbEntry.getGradeId());
				oYkbEntry.setSubjectId(ykbEntry.getSubjectId());
				oYkbEntry.setType(ykbEntry.getType());
				oYkbEntry.setTeacherId(ykbEntry.getTeacherId());
				if (ykbEntry.getNJxbId() != null) {
					oYkbEntry.setNJxbId(ykbEntry.getNJxbId());
					oYkbEntry.setNSubjectId(ykbEntry.getNSubjectId());
					oYkbEntry.setNTeacherId(ykbEntry.getNTeacherId());
				}
				ykbDao.updateN33_YKB(oYkbEntry);
			} else {
//                if (ykbEntry.getJxbId() != null) {
//                    jxbksDao.updateN33_JXBKSMinusCountByJxbId(ykbEntry.getJxbId());
//                }
//                if (ykbEntry.getNJxbId() != null) {
//                    jxbksDao.updateN33_JXBKSMinusCountByJxbId(ykbEntry.getNJxbId());
//                }
				if (jxbEntry.getDanOrShuang() != 0) {
					N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
					N33_JXBKSEntry entry = jxbksDao.getJXBKSsByJXBId(jxbEntry.getRelativeId());
					if (entry == null) {
						jxbksDao.addN33_JXBKSEntry(new N33_JXBKSEntry(jxbEntry.getRelativeId(), jxbEntry2.getSubjectId(), 1, jxbEntry2.getGradeId(), jxbEntry2.getSchoolId(), jxbEntry2.getTermId(), jxbEntry2.getDanOrShuang()));
					} else {
						jxbksDao.updateN33_JXBKSCountByJxbId(jxbEntry.getRelativeId());
					}
				}
				N33_JXBKSEntry entry = jxbksDao.getJXBKSsByJXBId(new ObjectId(jxbId));
				if (entry == null) {
					jxbksDao.addN33_JXBKSEntry(new N33_JXBKSEntry(new ObjectId(jxbId), jxbEntry.getSubjectId(), 1, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang()));
				} else {
					jxbksDao.updateN33_JXBKSCountByJxbId(new ObjectId(jxbId));
				}
			}
			if(jxbEntry.getDanOrShuang() == 0){
				ykbEntry.setJxbId(new ObjectId(jxbId));
				ykbEntry.setIsUse(Constant.ONE);
				ykbEntry.setGradeId(jxbEntry.getGradeId());
				ykbEntry.setSubjectId(jxbEntry.getSubjectId());
				ykbEntry.setType(jxbEntry.getType());
				ykbEntry.setTeacherId(jxbEntry.getTercherId());
			}
			ykbDao.updateN33_YKB(ykbEntry);
		} else {
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(new ObjectId(jxbId), jxbEntry.getTermId());
			if (zhuanXiangEntryList != null && zhuanXiangEntryList.size() != 0) {
				List<ObjectId> classroomIds = MongoUtils.getFieldObjectIDs(zhuanXiangEntryList, "rid");
				Map<ObjectId, N33_YKBEntry> ykbEntryMap = ykbDao.getJXB(jxbEntry.getTermId(), classroomIds, ykbEntry.getX(), ykbEntry.getY(), jxbEntry.getSchoolId());
				for (N33_ZhuanXiangEntry e : zhuanXiangEntryList) {
					N33_YKBEntry ykb = ykbEntryMap.get(e.getRoomId());
					ykb.setJxbId(new ObjectId(jxbId));
					ykb.setIsUse(Constant.ONE);
					ykb.setGradeId(jxbEntry.getGradeId());
					ykb.setSubjectId(jxbEntry.getSubjectId());
					//ykb.setTeacherId(e.getTeaId());
					ykb.setType(jxbEntry.getType());
					ykbDao.updateN33_YKB(ykb);
				}
			}
			N33_JXBKSEntry entry = jxbksDao.getJXBKSsByJXBId(new ObjectId(jxbId));
			if (entry == null) {
				jxbksDao.addN33_JXBKSEntry(new N33_JXBKSEntry(new ObjectId(jxbId), jxbEntry.getSubjectId(), 1, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang()));
			} else {
				jxbksDao.updateN33_JXBKSCountByJxbId(new ObjectId(jxbId));
			}
		}
	}

	/**
	 * 获得某个教学班的已排课时以及计划课时
	 *
	 * @param jxbId
	 * @return
	 */
	public N33_JXBDTO getjxbks(String jxbId) {
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
		N33_JXBKSEntry jxbksEntry = jxbksDao.getJXBKSsByJXBId(jxbEntry.getID());
		N33_JXBDTO jxbdto = new N33_JXBDTO(jxbEntry);
		jxbdto.setZksCount(jxbEntry.getJXBKS());
		jxbdto.setYpksCount(jxbksEntry.getYpCount());
		return jxbdto;
	}

	/**
	 * 更新某个教学班的已排课时
	 *
	 * @param jxbId
	 * @param type  1添加 2移除
	 */
	public void updatejxbks(String jxbId, int type) {
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
		N33_JXBKSEntry jxbksEntry = jxbksDao.getJXBKSsByJXBId(jxbEntry.getID());
		if (type == 1) {
			jxbksEntry.setYpCount(jxbksEntry.getYpCount() + 1);
		} else {
			if (jxbksEntry.getYpCount() - 1 >= 0) {
				jxbksEntry.setYpCount(jxbksEntry.getYpCount() - 1);
			}
		}
		jxbksDao.updateN33_JXBKS(jxbksEntry);
	}

	/**
	 * 删除教学班课时信息
	 *
	 * @param jxbId
	 */
	public void deljxbks(String jxbId) {
		jxbksDao.deleteN33_JXBKSByJXBId(new ObjectId(jxbId));
	}

	/**
	 * @param xqId
	 * @param gradeId
	 * @param subjectId
	 * @param schoolId
	 * @return
	 */
	public int getypksbysuid(ObjectId xqId, String gradeId, String subjectId, ObjectId schoolId) {
		int count = 0;
		List<N33_JXBKSEntry> jxbksEntries = jxbksDao.getJXBKSsBySubjectId(xqId, schoolId, new ObjectId(gradeId), new ObjectId(subjectId));
		if (jxbksEntries != null && jxbksEntries.size() != 0) {
			for (N33_JXBKSEntry jxbksEntry : jxbksEntries) {
				count += jxbksEntry.getYpCount();
			}
		}
		return count;
	}

	/**
	 * @param xqId
	 * @param gradeId
	 */
	public void clearKB(ObjectId xqId, String gradeId, ObjectId schoolId) {
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(xqId, schoolId, new ObjectId(gradeId));
		zixikeDao.removeN33_ZIXIKEEntryByXqGiD(xqId, new ObjectId(gradeId));
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
//                N33_YKBEntry n33_ykbEntry = new N33_YKBEntry(entry.getX(), entry.getY(), entry.getClassroomId(), entry.getSchoolId(), entry.getTermId());
//                n33_ykbEntry.setID(entry.getID());
//                ykbDao.updateN33_YKB(n33_ykbEntry);
				ykbDao.deleteN33_YKB(entry.getID());
//                N33_JXBKSEntry jxbksEntry = jxbksDao.getJXBKSsByJXBId(entry.getJxbId());
//                if (jxbksEntry != null) {
//                    jxbksEntry.setYpCount(jxbksEntry.getYpCount() - 1 >= 0 ? jxbksEntry.getYpCount() - 1 : 0);
//                }
				jxbksDao.updateCount(entry.getTermId(), entry.getJxbId());
				if (entry.getNJxbId() != null) {
					jxbksDao.updateCount(entry.getTermId(), entry.getNJxbId());
				}
			}
		}
	}

	/**
	 * 根据教室来清空课表
	 * @param xqId
	 * @param gradeId
	 */
	public void clearKBByClassRoom(ObjectId xqId, String gradeId, ObjectId schoolId,ObjectId classRoomId) {
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysListByClassRoomId(xqId, schoolId, new ObjectId(gradeId),classRoomId);
		zixikeDao.removeN33_ZIXIKEEntryByXqGiDByClassRoom(xqId, new ObjectId(gradeId),classRoomId);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
//                N33_YKBEntry n33_ykbEntry = new N33_YKBEntry(entry.getX(), entry.getY(), entry.getClassroomId(), entry.getSchoolId(), entry.getTermId());
//                n33_ykbEntry.setID(entry.getID());
//                ykbDao.updateN33_YKB(n33_ykbEntry);
				ykbDao.deleteN33_YKB(entry.getID());
//                N33_JXBKSEntry jxbksEntry = jxbksDao.getJXBKSsByJXBId(entry.getJxbId());
//                if (jxbksEntry != null) {
//                    jxbksEntry.setYpCount(jxbksEntry.getYpCount() - 1 >= 0 ? jxbksEntry.getYpCount() - 1 : 0);
//                }
				jxbksDao.updateCount(entry.getTermId(), entry.getJxbId());
				if (entry.getNJxbId() != null) {
					jxbksDao.updateCount(entry.getTermId(), entry.getNJxbId());
				}
			}
		}
	}

	public List<N33_SWDTO> getTeaShiWu(String ykbId,String classRoomId,int x,int y){
		List<N33_SWDTO> swdtoList = new ArrayList<N33_SWDTO>();
		List<ObjectId> swdtoIds = new ArrayList<ObjectId>();
		N33_YKBEntry ykbEntry = ykbDao.getN33_YKBById(new ObjectId(ykbId));
		List<ObjectId> teaIds = new ArrayList<ObjectId>();
		if(ykbEntry.getType() == 1 || ykbEntry.getType() == 2 || ykbEntry.getType() == 3 || ykbEntry.getType() == 5){
			teaIds.add(ykbEntry.getTeacherId());
		}else if(ykbEntry.getType() == 6){
			teaIds.add(ykbEntry.getTeacherId());
			teaIds.add(ykbEntry.getNTeacherId());
		}else if(ykbEntry.getType() == 4){
			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(ykbEntry.getJxbId(),ykbEntry.getTermId());
			for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
				if(zhuanXiangEntry.getRoomId() != null && classRoomId.equals(zhuanXiangEntry.getRoomId().toString()) && zhuanXiangEntry.getTeaId() != null && zhuanXiangEntry.getTeaId().toString() != ""){
					teaIds.add(zhuanXiangEntry.getTeaId());
				}
			}
		}
		ObjectId ciId = ykbEntry.getTermId();
		ObjectId xqid = termDao.getTermByTimeId(ciId).getID();
		for (ObjectId teaId : teaIds) {
			List<N33_SWDTO> swdtos = swService.getSwByXqidAndUserId(xqid,teaId);
			for (N33_SWDTO swdto : swdtos) {
				if(!swdtoIds.contains(new ObjectId(swdto.getId())) && (swdto.getY().intValue() - 1) == x && (swdto.getX().intValue() - 1) == y){
					swdtoIds.add(new ObjectId(swdto.getId()));
					swdtoList.add(swdto);
				}
			}
		}
		return swdtoList;
	}

	/**
	 * 根据教室来清空课表
	 * @param xqId
	 * @param gradeId
	 */
	public void clearKBByTea(ObjectId xqId, String gradeId, ObjectId schoolId,ObjectId teaId) {
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(xqId, schoolId, new ObjectId(gradeId));
		zixikeDao.removeN33_ZIXIKEEntryByXqGiDByTeaId(xqId, new ObjectId(gradeId),teaId);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
				if((entry.getNTeacherId() != null && entry.getNTeacherId().toString().equals(teaId.toString())) || (entry.getTeacherId() != null && entry.getTeacherId().toString().equals(teaId.toString()))){
					ykbDao.deleteN33_YKB(entry.getID());
					jxbksDao.updateCount(entry.getTermId(), entry.getJxbId());
					if (entry.getNJxbId() != null) {
						jxbksDao.updateCount(entry.getTermId(), entry.getNJxbId());
					}
				}
//                N33_YKBEntry n33_ykbEntry = new N33_YKBEntry(entry.getX(), entry.getY(), entry.getClassroomId(), entry.getSchoolId(), entry.getTermId());
//                n33_ykbEntry.setID(entry.getID());
//                ykbDao.updateN33_YKB(n33_ykbEntry);

//                N33_JXBKSEntry jxbksEntry = jxbksDao.getJXBKSsByJXBId(entry.getJxbId());
//                if (jxbksEntry != null) {
//                    jxbksEntry.setYpCount(jxbksEntry.getYpCount() - 1 >= 0 ? jxbksEntry.getYpCount() - 1 : 0);
//                }
			}
		}
	}

	/**
	 * @param xqId
	 * @param gradeId
	 * @param classroomId
	 * @param x
	 * @param y
	 * @param schoolId
	 * @return
	 */
	public List<N33_JXBDTO> getJXB(ObjectId xqId, String gradeId, String classroomId, int x, int y, ObjectId schoolId) {
		List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.getJXB(xqId, new ObjectId(gradeId), classroomId, x, y, schoolId);
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "jxbId");
			List<N33_JXBEntry> jxbEntries = jxbDao.getJXBListByIds(jxbIds);
			if (jxbEntries != null && jxbEntries.size() != 0) {
				for (N33_JXBEntry entry : jxbEntries) {
					jxbdtos.add(new N33_JXBDTO(entry));
				}
			}
		}

		return jxbdtos;
	}

	/**
	 * 获得当前已排的教学班列表
	 *
	 * @param xqId
	 * @param gradeId
	 * @return
	 */
	public List<N33_JXBDTO> GetSettledJXBinSourceKB(ObjectId xqId, String gradeId, ObjectId schoolId) {
		List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.getSettledJXBList(xqId, new ObjectId(gradeId), schoolId);
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "jxbId");
			List<N33_JXBEntry> jxbEntries = jxbDao.getJXBListByIds(jxbIds);
			if (jxbEntries != null && jxbEntries.size() != 0) {
				for (N33_JXBEntry entry : jxbEntries) {
					jxbdtos.add(new N33_JXBDTO(entry));
				}
			}
		}

		return jxbdtos;
	}

	/**
	 * 获得所有已排教学班的占位
	 *
	 * @param xqId
	 * @param gradeId
	 * @param schoolId
	 * @return
	 */
	public List<N33_YKBDTO> getPositionofSettledJXB(ObjectId xqId, String gradeId, ObjectId schoolId) {
		List<N33_YKBDTO> ykbdtos = new ArrayList<N33_YKBDTO>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.getSettledJXBList(xqId, new ObjectId(gradeId), schoolId);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
				ykbdtos.add(new N33_YKBDTO(entry));
			}
		}
		return ykbdtos;
	}

	/**
	 * 检测组合冲突
	 *
	 * @param jxbIds
	 * @param xqid
	 * @param sid
	 * @return
	 */
	public Map<String, Object> getZuHeConflictedSettled(List<String> jxbIds, ObjectId xqid, ObjectId sid) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		for (String jxbId : jxbIds) {
			Map<String, Object> jxbConflictedSettled = getConflictedSettledJXB(xqid, jxbId, sid);
			if (retMap.get("jxbcts") != null) {
				List<N33_YKBDTO> ykbdtos = (List<N33_YKBDTO>) retMap.get("jxbcts");
				ykbdtos.addAll((List<N33_YKBDTO>) jxbConflictedSettled.get("jxbcts"));
				retMap.put("jxbcts", ykbdtos);
			} else {
				retMap.put("jxbcts", (List<N33_YKBDTO>) jxbConflictedSettled.get("jxbcts"));
			}
			if (retMap.get("swcts") != null) {
				List<N33_SWDTO> swcts = (List<N33_SWDTO>) retMap.get("swcts");
				swcts.addAll((List<N33_SWDTO>) jxbConflictedSettled.get("swcts"));
				retMap.put("swcts", swcts);
			} else {
				retMap.put("swcts", (List<N33_SWDTO>) jxbConflictedSettled.get("swcts"));
			}
			if (retMap.get("gdsws") != null) {
				List<N33_SWDTO> gdsws = (List<N33_SWDTO>) retMap.get("gdsws");
				gdsws.addAll((List<N33_SWDTO>) jxbConflictedSettled.get("gdsws"));
				retMap.put("gdsws", gdsws);
			} else {
				retMap.put("gdsws", (List<N33_SWDTO>) jxbConflictedSettled.get("gdsws"));
			}
		}
		return retMap;
	}

	/**
	 * 获得与待排教学班有冲突的所有已排教学班的位置
	 * a)检查学生冲突
	 * b)检查教师冲突
	 * c)检查事务冲突
	 * d)是否有班级限制
	 *
	 * @param xqId
	 * @param jxbId
	 * @param schoolId
	 * @return
	 */
	public Map getConflictedSettledJXB(ObjectId xqId, String jxbId, ObjectId schoolId) {
		Map map = new HashMap();
		List<String> xys = new ArrayList<String>();
		Map<String, List<String>> xyMesg = new HashMap<String, List<String>>();
		//查询该老师所带其他年级的固定事项
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));

		int acClassType = 0;
		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId,jxbEntry.getGradeId(),xqId,1);
		if(null!=turnOff){
			acClassType = turnOff.getAcClass();
		}
		
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapByTermId(xqId, acClassType);
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
		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
		if (jxbEntry.getDanOrShuang() != 0) {
			jxbIds.add(jxbEntry.getRelativeId());
			N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
			teacherIds.add(jxbEntry2.getTercherId());
		}
		Map<ObjectId, String> jxbCTMsg = new HashMap<ObjectId, String>();
		//教学班教学班之间的冲突
		List<N33_JXBCTEntry> jxbctEntrys = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, xqId);
		List<N33_YKBDTO> ykbdtos = new ArrayList<N33_YKBDTO>();
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
				N33_JXBEntry jxb =jxbEntryMap.get(jxbctEntry.getOjxbId());
				if(null!=jxb){
					jxbCTMsg.put(jxbctEntry.getOjxbId(), msg + ":" + jxb.getName());
				}
			}
		}
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysByJXBIdsOrNJxbIds(xqId, jxbIds, schoolId);
		List<N33_YKBEntry> ykbEntries = ykbDao.getYKBEntrysList(xqId, jxbEntry.getSchoolId());

		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry ykbEntry : ykbEntryList) {
				xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
				if (xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()) != null) {
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
				} else {
					List<String> stringList = new ArrayList<String>();
					if(jxbCTMsg.get(ykbEntry.getJxbId()) != null){
						stringList.add(jxbCTMsg.get(ykbEntry.getJxbId()));
					}
					xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
				}
				//xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), jxbCTMsg.get(ykbEntry.getJxbId()));
			}
		}

		List<ObjectId> allJxbIds = new ArrayList<ObjectId>();
		if (ykbEntries != null && ykbEntries.size() != 0) {
			for (N33_YKBEntry ykbEntry : ykbEntries) {
				if (ykbEntry.getClassroomId() != null && jxbEntry.getClassroomId() != null && (ykbEntry.getIsUse() == 1 || ykbEntry.getNTeacherId() != null || ykbEntry.getTeacherId() != null) && ykbEntry.getClassroomId().toString().equals(jxbEntry.getClassroomId().toString())) {
					xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
					if (xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()) != null) {
						List<String> stringList = xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY());
						boolean flag = true;
						for (String s : stringList) {
							int a = s.indexOf(":");
							if(a != -1){
								String str = s.substring(0,a);
								if (str.equals("教室冲突")) {
									flag = false;
								}
							}
						}
						if (flag) {
							if(jxbEntryMap.get(ykbEntry.getJxbId()) != null){
								stringList.add("教室冲突" + ":" + jxbEntryMap.get(ykbEntry.getJxbId()).getName());
								xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
							}
						}
					} else {
						List<String> stringList = new ArrayList<String>();
						if(jxbEntryMap.get(ykbEntry.getJxbId()) != null){
							stringList.add("教室冲突" + ":" + jxbEntryMap.get(ykbEntry.getJxbId()).getName());
							xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
						}
					}
					//xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "教室冲突");
				}
				if ((ykbEntry.getTeacherId() != null && jxbEntry.getTercherId() != null && ykbEntry.getTeacherId().toString().equals(jxbEntry.getTercherId().toString()) && jxbEntry.getDanOrShuang() != 2) || (ykbEntry.getNTeacherId() != null && jxbEntry.getTercherId() != null && ykbEntry.getNTeacherId().toString().equals(jxbEntry.getTercherId().toString()) && jxbEntry.getDanOrShuang() == 2)) {
					xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
					if (xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()) != null) {
						List<String> stringList = xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY());
						boolean flag = true;
						for (String s : stringList) {
							int a = s.indexOf(":");
							if(a != -1){
								String str = s.substring(0,a);
								if (str.equals("教师冲突")) {
									flag = false;
								}
							}
						}
						if (flag) {
							if(jxbEntryMap.get(ykbEntry.getJxbId()) != null){
								stringList.add("教师冲突" + ":" + jxbEntryMap.get(ykbEntry.getJxbId()).getName());
								xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
							}
						}
					} else {
						List<String> stringList = new ArrayList<String>();
						if(jxbEntryMap.get(ykbEntry.getJxbId()) != null){
							stringList.add("教师冲突" + ":" + jxbEntryMap.get(ykbEntry.getJxbId()).getName());
							xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
						}
					}
					//xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "教师冲突");
				}
				if (jxbEntry.getGradeId() != null && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && ykbEntry.getJxbId() != null) {
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
					for (N33_YKBEntry ykbEntry : ykbEntries) {
						if (jxbEntry.getGradeId() != null && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && njxbs.contains(ykbEntry.getJxbId())) {
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
							//xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "非走班格");
						}
						if (jxbEntry.getGradeId() != null && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && njxbs2.contains(ykbEntry.getJxbId())) {
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
							//xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "走班格");
						}
					}
				}
			}
		}

		if (ykbEntries != null && ykbEntries.size() != 0) {
			for (N33_YKBEntry entry : ykbEntries) {
				N33_YKBDTO dto = new N33_YKBDTO(entry);
				dto.setCtString(xyMesg.get(entry.getX() + "" + entry.getY()));

				if (jxbEntry.getType() != 4) {
					if (jxbEntry.getClassroomId() != null && entry.getClassroomId() != null && entry.getClassroomId().toString().equals(jxbEntry.getClassroomId().toString())) {
						if (xys.contains(entry.getX() + "" + entry.getY())) {
							ykbdtos.add(dto);
						} else if (entry.getJxbId() != null) {
							ykbdtos.add(dto);
						}
					}
				} else {
					List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbEntry.getID(), xqId);
					//某一个专项课所对应的教室id
					List<ObjectId> claId = MongoUtils.getFieldObjectIDs(zhuanXiangEntries, "rid");
					if (claId != null && claId.size() > 0 && entry.getClassroomId() != null && claId.contains(entry.getClassroomId())) {
						if (xys.contains(entry.getX() + "" + entry.getY())) {
							ykbdtos.add(dto);
						} else if (entry.getJxbId() != null) {
							ykbdtos.add(dto);
						}
					}
				}
			}
		}
		teacherIds.add(jxbEntry.getTercherId());
		TermEntry termEntry = termDao.getTermByTimeId(xqId);
		//事务
		List<N33_SWDTO> swdtos = swService.getSwByXqidAndUserIds(termEntry.getID(), teacherIds);
		List<N33_SWDTO> guDingSWs = swService.getGuDingShiWuByXqid(termEntry.getID());
		map.put("jxbcts", ykbdtos);
		map.put("swcts", swdtos);
		map.put("gdsws", guDingSWs);
		return map;
	}

	/**
	 * 获得与待排教学班有冲突的所有已排教学班的位置
	 * a)检查学生冲突
	 * b)检查教师冲突
	 * c)检查事务冲突
	 * d)是否有班级限制
	 *
	 * @param xqId
	 * @param jxbId
	 * @param schoolId
	 * @return
	 */
//	public Map getConflictedSettledJXB(ObjectId xqId, String jxbId, ObjectId schoolId) {
//		Map map = new HashMap();
//		List<String> xys = new ArrayList<String>();
//		ObjectId zhenXqid = termDao.getTermByTimeId(xqId).getID();
//		Map<String, String> xyMesg = new HashMap<String, String>();
//		//查询该老师所带其他年级的固定事项
//		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
//		if (jxbEntry.getType() == 4) {
//			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(new ObjectId(jxbId), xqId);
//			List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, jxbEntry.getGradeId());
//			for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
//				ObjectId teaId = zhuanXiangEntry.getTeaId();
//				N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(teaId, xqId);
//				if (teaEntry != null) {
//					List<ObjectId> teaGradeIds = teaEntry.getGradeList();
//					if (teaGradeIds != null && teaGradeIds.size() > 0) {
//						for (ObjectId ids : teaGradeIds) {
//							if (!ids.toString().equals(jxbEntry.getGradeId().toString())) {
//								gdsxdtos.addAll(gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, ids));
//							}
//							if (gdsxdtos != null && gdsxdtos.size() > 0) {
//								for (N33_GDSXDTO dto : gdsxdtos) {
//									xys.add(dto.getX() + "" + dto.getY());
//									xyMesg.put(dto.getX() + "" + dto.getY(), "固定事项格");
//								}
//							}
//						}
//					}
//				}
//			}
//		} else if (jxbEntry.getType() == 6) {
//			List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, jxbEntry.getGradeId());
//			List<ObjectId> teaIds = new ArrayList<ObjectId>();
//			if (jxbEntry.getTercherId() != null) {
//				teaIds.add(jxbEntry.getTercherId());
//			}
//			N33_JXBEntry NjxbEntry = jxbDao.getJXBById(jxbEntry.getRelativeId());
//			if (NjxbEntry.getTercherId() != null) {
//				teaIds.add(jxbEntry.getTercherId());
//			}
//			for (ObjectId teaId : teaIds) {
//				N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(teaId, xqId);
//				if (teaEntry != null) {
//					List<ObjectId> teaGradeIds = teaEntry.getGradeList();
//					if (teaGradeIds != null && teaGradeIds.size() > 0) {
//						for (ObjectId ids : teaGradeIds) {
//							if (!ids.toString().equals(jxbEntry.getGradeId().toString())) {
//								gdsxdtos.addAll(gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, ids));
//							}
//							if (gdsxdtos != null && gdsxdtos.size() > 0) {
//								for (N33_GDSXDTO dto : gdsxdtos) {
//									xys.add(dto.getX() + "" + dto.getY());
//									xyMesg.put(dto.getX() + "" + dto.getY(), "固定事项格");
//								}
//							}
//						}
//					}
//				}
//			}
//		} else {
//			List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, jxbEntry.getGradeId());
//			N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(jxbEntry.getTercherId(), xqId);
//			if (teaEntry != null) {
//				List<ObjectId> teaGradeIds = teaEntry.getGradeList();
//				if (teaGradeIds != null && teaGradeIds.size() > 0) {
//					for (ObjectId ids : teaGradeIds) {
//						if (!ids.toString().equals(jxbEntry.getGradeId().toString())) {
//							gdsxdtos.addAll(gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, ids));
//						}
//						if (gdsxdtos != null && gdsxdtos.size() > 0) {
//							for (N33_GDSXDTO dto : gdsxdtos) {
//								xys.add(dto.getX() + "" + dto.getY());
//								xyMesg.put(dto.getX() + "" + dto.getY(), "固定事项格");
//							}
//						}
//					}
//				}
//			}
//		}
//
//		if (jxbEntry.getType() != 1 && jxbEntry.getType() != 2) {
//			List<ZouBanTimeDTO> zouBanTimeDTOs = courseRangeService.getZouBanTimeByType(schoolId, xqId, jxbEntry.getGradeId(), 0);
//			if (zouBanTimeDTOs != null && zouBanTimeDTOs.size() != 0) {
//				for (ZouBanTimeDTO dto : zouBanTimeDTOs) {
//					xys.add((dto.getX() - 1) + "" + (dto.getY() - 1));
//					xyMesg.put((dto.getX() - 1) + "" + (dto.getY() - 1), "固定走班格");
//				}
//			}
//		}
//		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
//		jxbIds.add(jxbEntry.getID());
//		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
//		if (jxbEntry.getDanOrShuang() != 0) {
//			jxbIds.add(jxbEntry.getRelativeId());
//			N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
//			teacherIds.add(jxbEntry2.getTercherId());
//		}
//		Map<ObjectId, String> jxbCTMsg = new HashMap<ObjectId, String>();
//		//教学班教学班之间的冲突
//		List<N33_JXBCTEntry> jxbctEntrys = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, xqId);
//		List<N33_YKBDTO> ykbdtos = new ArrayList<N33_YKBDTO>();
//		if (jxbctEntrys != null && jxbctEntrys.size() != 0) {
//			jxbIds = MongoUtils.getFieldObjectIDs(jxbctEntrys, "ojxbId");
//			for (N33_JXBCTEntry jxbctEntry : jxbctEntrys) {
//				String msg = "";
//				if (jxbctEntry.getCtType() == 1) {
//					msg = "学生冲突";
//				} else if (jxbctEntry.getCtType() == 2) {
//					msg = "教师冲突";
//				} else if (jxbctEntry.getCtType() == 3) {
//					msg = "场地冲突";
//				}
//				jxbCTMsg.put(jxbctEntry.getOjxbId(), msg);
//			}
//		}
//		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysByJXBIdsOrNJxbIds(xqId, jxbIds, schoolId);
//		List<N33_YKBEntry> ykbEntries = ykbDao.getYKBEntrysList(xqId, jxbEntry.getSchoolId());
//
//		if (ykbEntryList != null && ykbEntryList.size() != 0) {
//			for (N33_YKBEntry ykbEntry : ykbEntryList) {
//				xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
//				xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), jxbCTMsg.get(ykbEntry.getJxbId()));
//			}
//		}
//
//		List<ObjectId> allJxbIds = new ArrayList<ObjectId>();
//		if (ykbEntries != null && ykbEntries.size() != 0) {
//			for (N33_YKBEntry ykbEntry : ykbEntries) {
//				if (ykbEntry.getClassroomId() != null && jxbEntry.getClassroomId() != null && (ykbEntry.getIsUse() == 1 || ykbEntry.getNTeacherId() != null || ykbEntry.getTeacherId() != null) && ykbEntry.getClassroomId().toString().equals(jxbEntry.getClassroomId().toString())) {
//					xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
//					xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "教室冲突");
//				}
//				if (ykbEntry.getTeacherId() != null && jxbEntry.getTercherId() != null && ykbEntry.getTeacherId().toString().equals(jxbEntry.getTercherId().toString())) {
//					xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
//					xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "教师冲突");
//				}
//				if (jxbEntry.getGradeId() != null && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && ykbEntry.getJxbId() != null) {
//					allJxbIds.add(ykbEntry.getJxbId());
//				}
//			}
//		}
//
//		//如果某一个格子放了等级考的学科，那么这个格子同时上课的其他教室必须放等级考的学科
//		List<ObjectId> njxbs = new ArrayList<ObjectId>();
//		List<ObjectId> njxbs2 = new ArrayList<ObjectId>();
//		if (allJxbIds != null && allJxbIds.size() != 0) {
//			List<N33_JXBEntry> jxbEntries = jxbDao.getJXBByIds(allJxbIds);
//			for (N33_JXBEntry entry : jxbEntries) {
//				if ((jxbEntry.getType() == 1 || jxbEntry.getType() == 2) && (entry.getType() != 1 && entry.getType() != 2)) {
//					njxbs.add(entry.getID());
//				} else if ((jxbEntry.getType() == 3 || jxbEntry.getType() == 4 || jxbEntry.getType() == 6) && (entry.getType() != 3 && entry.getType() != 4 && entry.getType() != 6)) {
//					njxbs2.add(entry.getID());
//				}
//			}
//			if (ykbEntries != null && ykbEntries.size() != 0 && ((njxbs != null && njxbs.size() != 0) || (njxbs2 != null && njxbs2.size() != 0))) {
//				for (N33_YKBEntry ykbEntry : ykbEntries) {
//					if (jxbEntry.getGradeId() != null && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && njxbs.contains(ykbEntry.getJxbId())) {
//						xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
//						xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "非走班格");
//					}
//					if (jxbEntry.getGradeId() != null && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && njxbs2.contains(ykbEntry.getJxbId())) {
//						xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
//						xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "走班格");
//					}
//				}
//			}
//		}
//		if (ykbEntries != null && ykbEntries.size() != 0) {
//			for (N33_YKBEntry entry : ykbEntries) {
//				N33_YKBDTO dto = new N33_YKBDTO(entry);
//				dto.setRemarks(xyMesg.get(entry.getX() + "" + entry.getY()));
//
//				if (jxbEntry.getType() != 4) {
//					if (jxbEntry.getClassroomId() != null && entry.getClassroomId() != null && entry.getClassroomId().toString().equals(jxbEntry.getClassroomId().toString())) {
//						if (xys.contains(entry.getX() + "" + entry.getY())) {
//							ykbdtos.add(dto);
//						} else if (entry.getJxbId() != null) {
//							ykbdtos.add(dto);
//						}
//					}
//				} else {
//					List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbEntry.getID(), xqId);
//					//某一个专项课所对应的教室id
//					List<ObjectId> claId = MongoUtils.getFieldObjectIDs(zhuanXiangEntries, "rid");
//					if (claId != null && claId.size() > 0 && entry.getClassroomId() != null && claId.contains(entry.getClassroomId())) {
//						if (xys.contains(entry.getX() + "" + entry.getY())) {
//							ykbdtos.add(dto);
//						} else if (entry.getJxbId() != null) {
//							ykbdtos.add(dto);
//						}
//					}
//				}
//			}
//		}
//		teacherIds.add(jxbEntry.getTercherId());
//		TermEntry termEntry = termDao.getTermByTimeId(xqId);
//		//事务
//		List<N33_SWDTO> swdtos = swService.getSwByXqidAndUserIds(termEntry.getID(), teacherIds);
//		List<N33_SWDTO> guDingSWs = swService.getGuDingShiWuByXqid(termEntry.getID());
//		map.put("jxbcts", ykbdtos);
//		map.put("swcts", swdtos);
//		map.put("gdsws", guDingSWs);
//		return map;
//	}

	/**
	 * @param xqId
	 * @param jxbId
	 * @param classroomId
	 * @param schoolId
	 * @return
	 */
//	public Map getConflictedSettledJXBByRoomId(ObjectId xqId, String jxbId, String classroomId, ObjectId schoolId) {
//		Map map = new HashMap();
//		List<String> xys = new ArrayList<String>();
//		Map<String, String> xyMesg = new HashMap<String, String>();
//		ObjectId zhenXqid = termDao.getTermByTimeId(xqId).getID();
//		//查询该老师所带其他年级的固定事项
//		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
//		if (jxbEntry.getType() == 4) {
//			List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(new ObjectId(jxbId), xqId);
//			List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, jxbEntry.getGradeId());
//			for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
//				ObjectId teaId = zhuanXiangEntry.getTeaId();
//				N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(teaId, xqId);
//				if (teaEntry != null) {
//					List<ObjectId> teaGradeIds = teaEntry.getGradeList();
//					if (teaGradeIds != null && teaGradeIds.size() > 0) {
//						for (ObjectId ids : teaGradeIds) {
//							if (!ids.toString().equals(jxbEntry.getGradeId().toString())) {
//								gdsxdtos.addAll(gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, ids));
//							}
//							if (gdsxdtos != null && gdsxdtos.size() > 0) {
//								for (N33_GDSXDTO dto : gdsxdtos) {
//									xys.add(dto.getX() + "" + dto.getY());
//									xyMesg.put(dto.getX() + "" + dto.getY(), "固定事项格");
//								}
//							}
//						}
//					}
//				}
//			}
//		} else if (jxbEntry.getType() == 6) {
//			List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, jxbEntry.getGradeId());
//			List<ObjectId> teaIds = new ArrayList<ObjectId>();
//			if (jxbEntry.getTercherId() != null) {
//				teaIds.add(jxbEntry.getTercherId());
//			}
//			N33_JXBEntry NjxbEntry = jxbDao.getJXBById(jxbEntry.getRelativeId());
//			if (NjxbEntry.getTercherId() != null) {
//				teaIds.add(jxbEntry.getTercherId());
//			}
//			for (ObjectId teaId : teaIds) {
//				N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(teaId, xqId);
//				if (teaEntry != null) {
//					List<ObjectId> teaGradeIds = teaEntry.getGradeList();
//					if (teaGradeIds != null && teaGradeIds.size() > 0) {
//						for (ObjectId ids : teaGradeIds) {
//							if (!ids.toString().equals(jxbEntry.getGradeId().toString())) {
//								gdsxdtos.addAll(gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, ids));
//							}
//							if (gdsxdtos != null && gdsxdtos.size() > 0) {
//								for (N33_GDSXDTO dto : gdsxdtos) {
//									xys.add(dto.getX() + "" + dto.getY());
//									xyMesg.put(dto.getX() + "" + dto.getY(), "固定事项格");
//								}
//							}
//						}
//					}
//				}
//			}
//		} else {
//			List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, jxbEntry.getGradeId());
//			N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(jxbEntry.getTercherId(), xqId);
//			if (teaEntry != null) {
//				List<ObjectId> teaGradeIds = teaEntry.getGradeList();
//				if (teaGradeIds != null && teaGradeIds.size() > 0) {
//					for (ObjectId ids : teaGradeIds) {
//						if (!ids.toString().equals(jxbEntry.getGradeId().toString())) {
//							gdsxdtos.addAll(gdsxService.getGDSXBySidAndXqid(zhenXqid, schoolId, ids));
//						}
//						if (gdsxdtos != null && gdsxdtos.size() > 0) {
//							for (N33_GDSXDTO dto : gdsxdtos) {
//								xys.add(dto.getX() + "" + dto.getY());
//								xyMesg.put(dto.getX() + "" + dto.getY(), "固定事项格");
//							}
//						}
//					}
//				}
//			}
//		}
//
//
//		if (jxbEntry.getType() != 1 && jxbEntry.getType() != 2) {
//			List<ZouBanTimeDTO> zouBanTimeDTOs = courseRangeService.getZouBanTimeByType(schoolId, xqId, jxbEntry.getGradeId(), 0);
//			if (zouBanTimeDTOs != null && zouBanTimeDTOs.size() != 0) {
//				for (ZouBanTimeDTO dto : zouBanTimeDTOs) {
//					xys.add((dto.getX() - 1) + "" + (dto.getY() - 1));
//					xyMesg.put((dto.getX() - 1) + "" + (dto.getY() - 1), "固定走班格");
//				}
//			}
//		}
//		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
//		jxbIds.add(jxbEntry.getID());
//		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
//		if (jxbEntry.getDanOrShuang() != 0) {
//			jxbIds.add(jxbEntry.getRelativeId());
//			N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
//			teacherIds.add(jxbEntry2.getTercherId());
//		}
//		Map<ObjectId, String> jxbCTMsg = new HashMap<ObjectId, String>();
//		//教学班教学班之间的冲突
//		List<N33_JXBCTEntry> jxbctEntrys = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, xqId);
//		List<N33_YKBDTO> ykbdtos = new ArrayList<N33_YKBDTO>();
//		if (jxbctEntrys != null && jxbctEntrys.size() != 0) {
//			jxbIds = MongoUtils.getFieldObjectIDs(jxbctEntrys, "ojxbId");
//			for (N33_JXBCTEntry jxbctEntry : jxbctEntrys) {
//				String msg = "";
//				if (jxbctEntry.getCtType() == 1) {
//					msg = "学生冲突";
//				} else if (jxbctEntry.getCtType() == 2) {
//					msg = "教师冲突";
//				} else if (jxbctEntry.getCtType() == 3) {
//					msg = "场地冲突";
//				}
//				jxbCTMsg.put(jxbctEntry.getOjxbId(), msg);
//			}
//		}
//		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysByJXBIdsOrNJxbIds(xqId, jxbIds, schoolId);
//		List<N33_YKBEntry> ykbEntries = ykbDao.getYKBEntrysList(xqId, jxbEntry.getSchoolId());
//
//		if (ykbEntryList != null && ykbEntryList.size() != 0) {
//			for (N33_YKBEntry ykbEntry : ykbEntryList) {
//				xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
//				xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), jxbCTMsg.get(ykbEntry.getJxbId()));
//			}
//		}
//
//		List<ObjectId> allJxbIds = new ArrayList<ObjectId>();
//		if (ykbEntries != null && ykbEntries.size() != 0) {
//			for (N33_YKBEntry ykbEntry : ykbEntries) {
//				if (ykbEntry.getTeacherId() != null && ykbEntry.getTeacherId().equals(jxbEntry.getTercherId())) {
//					xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
//					xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "教师冲突");
//				}
//				if (jxbEntry.getGradeId() != null && jxbEntry.getGradeId().equals(ykbEntry.getGradeId()) && ykbEntry.getJxbId() != null) {
//					allJxbIds.add(ykbEntry.getJxbId());
//				}
//			}
//		}
//
//		//如果某一个格子放了等级考的学科，那么这个格子同时上课的其他教室必须放等级考的学科
//		List<ObjectId> njxbs = new ArrayList<ObjectId>();
//		List<ObjectId> njxbs2 = new ArrayList<ObjectId>();
//		if (allJxbIds != null && allJxbIds.size() != 0) {
//			List<N33_JXBEntry> jxbEntries = jxbDao.getJXBByIds(allJxbIds);
//			for (N33_JXBEntry entry : jxbEntries) {
//				if ((jxbEntry.getType() == 1 || jxbEntry.getType() == 2) && (entry.getType() != 1 && entry.getType() != 2)) {
//					njxbs.add(entry.getID());
//				} else if ((jxbEntry.getType() == 3 || jxbEntry.getType() == 4 || jxbEntry.getType() == 6) && (entry.getType() != 3 && entry.getType() != 4 && entry.getType() != 6)) {
//					njxbs2.add(entry.getID());
//				}
//			}
//			if (ykbEntries != null && ykbEntries.size() != 0 && ((njxbs != null && njxbs.size() != 0) || (njxbs2 != null && njxbs2.size() != 0))) {
//				for (N33_YKBEntry ykbEntry : ykbEntries) {
//					if (jxbEntry.getGradeId() != null && jxbEntry.getGradeId().equals(ykbEntry.getGradeId()) && njxbs.contains(ykbEntry.getJxbId())) {
//						xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
//						if (StringUtils.isEmpty(xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()))) {
//							xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "非走班格");
//						}
//					}
//					if (jxbEntry.getGradeId() != null && jxbEntry.getGradeId().equals(ykbEntry.getGradeId()) && njxbs2.contains(ykbEntry.getJxbId())) {
//						xys.add(ykbEntry.getX() + "" + ykbEntry.getY());
//						if (StringUtils.isEmpty(xyMesg.get(ykbEntry.getX() + "" + ykbEntry.getY()))) {
//							xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), "走班格");
//						}
//					}
//				}
//			}
//		}
//		if (ykbEntries != null && ykbEntries.size() != 0) {
//			for (N33_YKBEntry entry : ykbEntries) {
//				N33_YKBDTO dto = new N33_YKBDTO(entry);
//				dto.setRemarks(xyMesg.get(entry.getX() + "" + entry.getY()));
//				if (entry.getClassroomId() != null && entry.getClassroomId().toString().equals(classroomId)) {
//					if (xys.contains(entry.getX() + "" + entry.getY())) {
//						ykbdtos.add(dto);
//					} else if (entry.getJxbId() != null) {
//						ykbdtos.add(dto);
//					}
//				}
////                else {
////                    if (xys.contains(entry.getX() + "" + entry.getY())) {
////                        ykbdtos.add(dto);
////                    } else if (entry.getJxbId() != null) {
////                        ykbdtos.add(dto);
////                    }
////                }
//
//			}
//		}
//		teacherIds.add(jxbEntry.getTercherId());
//		TermEntry termEntry = termDao.getTermByTimeId(xqId);
//		//事务
//		List<N33_SWDTO> swdtos = swService.getSwByXqidAndUserIds(termEntry.getID(), teacherIds);
//		map.put("jxbcts", ykbdtos);
//		map.put("swcts", swdtos);
//		return map;
//	}


	/**
	 * @param xqId
	 * @param jxbId
	 * @param classroomId
	 * @param schoolId
	 * @return
	 */
	public Map getConflictedSettledJXBByRoomId(ObjectId xqId, String jxbId, String classroomId, ObjectId schoolId) {
		//用于查询老师事务
		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
		Map map = new HashMap();
		List<String> xys = new ArrayList<String>();
		Map<String, List<String>> xyMesg = new HashMap<String, List<String>>();
		ObjectId zhenXqid = termDao.getTermByTimeId(xqId).getID();
		//查询该老师所带其他年级的固定事项
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));

		int acClassType = 0;
		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId, jxbEntry.getGradeId(), xqId, 1);
		if(null!=turnOff){
			acClassType = turnOff.getAcClass();
		}

		Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapByTermId(xqId, acClassType);

		List<N33_GDSXDTO> gdsxdto = new ArrayList<N33_GDSXDTO>();
		List<N33_GDSXDTO> gdsxdtos = new ArrayList<N33_GDSXDTO>();
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
		List<N33_YKBDTO> ykbdtos = new ArrayList<N33_YKBDTO>();
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
				N33_JXBEntry jxb =jxbEntryMap.get(jxbctEntry.getOjxbId());
				if(null!=jxb){
					jxbCTMsg.put(jxbctEntry.getOjxbId(), msg + ":" + jxb.getName());
				}
			}
		}
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysByJXBIdsOrNJxbIds(xqId, jxbIds, schoolId);
		List<N33_YKBEntry> ykbEntries = ykbDao.getYKBEntrysList(xqId, jxbEntry.getSchoolId());

		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry ykbEntry : ykbEntryList) {
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
			N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(jxbEntry.getRelativeId());
			for (N33_YKBEntry ykbEntry : ykbEntries) {
				if(jxbEntry.getType() == 6){
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
								if(a != -1){
									String str = s.substring(0,a);
									if (str.equals("教师冲突")) {
										flag = false;
									}
								}
							}
							if (flag) {
								N33_JXBEntry jxb =jxbEntryMap.get(ykbEntry.getJxbId());
								if(null!=jxb){
									stringList.add("教师冲突" + ":" + jxb.getName());
									xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
								}
							}
						} else {
							List<String> stringList = new ArrayList<String>();
							N33_JXBEntry jxb =jxbEntryMap.get(ykbEntry.getJxbId());
							if(null!=jxb){
								stringList.add("教师冲突" + ":" + jxb.getName());
								xyMesg.put(ykbEntry.getX() + "" + ykbEntry.getY(), stringList);
							}
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
					for (N33_YKBEntry ykbEntry : ykbEntries) {
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
			for (N33_YKBEntry entry : ykbEntries) {
				N33_YKBDTO dto = new N33_YKBDTO(entry);
				dto.setCtString(xyMesg.get(entry.getX() + "" + entry.getY()));
				//dto.setRemarks(xyMesg.get(entry.getX() + "" + entry.getY()));
				if (entry.getClassroomId() != null && entry.getClassroomId().toString().equals(classroomId)) {
					if (xys.contains(entry.getX() + "" + entry.getY())) {
						ykbdtos.add(dto);
					} else if (entry.getJxbId() != null) {
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
		//map.put("gdsxs", gdsxdto); 不在查询老师所带其他年级的固定事项
		map.put("gdsxs", gdsxdto); 
		return map;
	}

	public List<Map<String, Object>> GetTeachersSettledPositionsTag(ObjectId tagId, ObjectId schoolId) {
		ObjectId xqid = getDefaultPaiKeTerm(schoolId).getPaikeci();
		N33_StudentTagEntry tagEntry = studentTagDao.getTagById(tagId);
		List<ObjectId> jxbIds = tagEntry.getJxbIds();
		//原课表
		List<N33_YKBEntry> entries = ykbDao.getJXB(xqid, jxbIds);
		//学科列表
		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(xqid, schoolId);
		//教学班列表
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapsByIds(jxbIds);
		//教室列表
		List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, xqid);
		//返回数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (N33_YKBEntry entry : entries) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("x", entry.getX() + 1);
			map.put("y", entry.getY() + 1);
			map.put("ykbId", entry.getID().toString());
			map.put("type", entry.getType());
			for (N33_KSDTO ksdto : gradeSubject) {
				//获取学科
				if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
					map.put("subName", ksdto.getSnm());
					break;
				}
			}
			//教学班
			N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
			if (entry1 != null) {
				List<ObjectId> objectIds = entry1.getStudentIds();
				map.put("count", objectIds != null ? objectIds.size() : 0);
				map.put("JxbName", entry1.getName());
				//教室
				for (N33_ClassroomEntry room : classRoomDTO) {
					//获取学科
					if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
						map.put("roomName", room.getRoomName());
						break;
					} else {
						map.put("roomName", "");
					}
				}
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 老师课表
	 *
	 * @param teacherId
	 * @param schoolId
	 * @return
	 */
	public List<Map<String, Object>> GetTeachersSettledPositions(ObjectId teacherId, ObjectId schoolId) {
		ObjectId xqid = getDefaultPaiKeTerm(schoolId).getPaikeci();
		ObjectId pkXq = getDefaultPaiKeTerm(schoolId).getPaikeXqid();

		List<Map<String, Object>> lists = termService.getPaikeTimesByTermId(pkXq);
		List<ObjectId> cids = new ArrayList<ObjectId>();
		for (Map<String, Object> lt : lists) {
			String cid = (String) lt.get("ciId");
			cids.add(new ObjectId(cid));
		}

		//该次排课的所有年级
		Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(cids, schoolId);

		//原课表
		List<N33_YKBEntry> entries = ykbDao.getJXB(xqid, teacherId);
		entries.addAll(ykbDao.getJXBByTid(xqid, teacherId));
		entries.addAll(ykbDao.getYKBbyType(xqid,schoolId,4));
		//学科列表
		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(xqid, schoolId);
		//教学班列表
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndTeaId(teacherId, xqid);
		List<ObjectId> xqids = new ArrayList<ObjectId>();
		xqids.add(xqid);
		List<N33_JXBEntry> jxbEntriesList = jxbDao.getJXBList(xqids, teacherId);
		jxbEntriesList.addAll(jxbDao.getZXJXBList(xqids));
		//专项课Ids
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntriesList) {
			zxkIds.add(jxbEntry.getID());
		}
		List<ObjectId> ods = new ArrayList<ObjectId>();
		for (N33_YKBEntry ykbEntry : entries) {
			if (ykbEntry.getType() == 5) {
				ods.add(ykbEntry.getJxbId());
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


		//教室列表
		List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, xqid);
		//返回数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (N33_YKBEntry entry : entries) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (entry.getJxbId() == null) {
				continue;
			}
			map.put("x", entry.getX() + 1);
			map.put("y", entry.getY() + 1);
			map.put("ykbId", entry.getID().toString());
			map.put("jxbId", entry.getJxbId().toString());
			map.put("type", entry.getType());
			map.put("gradeName", gradeMap.get(entry.getGradeId()).getName() + "（" + gradeMap.get(entry.getGradeId()).getJie() + "）");
			if (entry.getType() != 4 && entry.getType() != 5) {
				if (entry.getNTeacherId() != null) {
					if (entry.getNTeacherId().toString().equals(teacherId.toString())) {
						for (N33_KSDTO ksdto : gradeSubject) {
							//获取学科
							if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								map.put("subName", ksdto.getSnm());
								break;
							}
						}
						//教学班
						N33_JXBEntry entry1 = jxbEntries.get(entry.getNJxbId());
						if (entry1 != null) {
							List<ObjectId> objectIds = entry1.getStudentIds();
							map.put("count", objectIds != null ? objectIds.size() : 0);
							map.put("JxbName", entry1.getName() + "(双)");
							//教室
							for (N33_ClassroomEntry room : classRoomDTO) {
								//获取学科
								if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
									map.put("roomName", room.getRoomName());
									break;
								} else {
									map.put("roomName", "");
								}
							}
							list.add(map);
						}
					} else {
						for (N33_KSDTO ksdto : gradeSubject) {
							//获取学科
							if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								map.put("subName", ksdto.getSnm());
								break;
							}
						}
						//教学班
						N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
						if (entry1 != null) {
							List<ObjectId> objectIds = entry1.getStudentIds();
							map.put("count", objectIds != null ? objectIds.size() : 0);
							if(entry.getType() == 6){
								map.put("JxbName", entry1.getName() + "(单)");
							}else{
								map.put("JxbName", entry1.getName());
							}
							//教室
							for (N33_ClassroomEntry room : classRoomDTO) {
								//获取学科
								if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
									map.put("roomName", room.getRoomName());
									break;
								} else {
									map.put("roomName", "");
								}
							}
							list.add(map);
						}
					}
				} else {
					for (N33_KSDTO ksdto : gradeSubject) {
						//获取学科
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							map.put("subName", ksdto.getSnm());
							break;
						}
					}
					//教学班
					N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
					if (entry1 != null) {
						List<ObjectId> objectIds = entry1.getStudentIds();
						map.put("count", objectIds != null ? objectIds.size() : 0);
						map.put("JxbName", entry1.getName());
						//教室
						for (N33_ClassroomEntry room : classRoomDTO) {
							//获取学科
							if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
								map.put("roomName", room.getRoomName());
								break;
							} else {
								map.put("roomName", "");
							}
						}
						list.add(map);
					}
				}
			} else {
				if (entry.getType() == 4) {
					for (N33_KSDTO ksdto : gradeSubject) {
						//获取学科
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							map.put("subName", ksdto.getSnm());
							break;
						}
					}
					List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
					for (N33_ZhuanXiangEntry entry1 : zhuanList) {
						if (entry1.getTeaId().toString().equals(teacherId.toString())) {
							map.put("JxbName", entry1.getName());
							map.put("count", entry1.getStudentId().size());
							for (N33_ClassroomEntry room : classRoomDTO) {
								//获取教室
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
					map.put("JxbName", entry1.getName());
					map.put("count", entry1.getStudentIds().size());
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
		return list;
	}

	/**
	 * 获取某老师某年级课表
	 *
	 * @param teacherId
	 * @param schoolId
	 * @return
	 */
	public List<Map<String, Object>> GetTeachersSettledPositionsByGrade(ObjectId gid, ObjectId teacherId, ObjectId schoolId) {
		ObjectId xqid = getDefaultPaiKeTerm(schoolId).getPaikeci();
		//原课表
		List<N33_YKBEntry> entries = ykbDao.getJXB(xqid, teacherId);
		entries.addAll(ykbDao.getJXBByTid(xqid, teacherId));
		entries.addAll(ykbDao.getYKBbyType(xqid,schoolId,4));
		//学科列表
		List<N33_KSDTO> gradeSubject = subjectService.getIsolateSubjectByGradeIdMap(xqid, schoolId);
		//教学班列表
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndTeaId(teacherId, xqid);
		List<ObjectId> xqids = new ArrayList<ObjectId>();
		xqids.add(xqid);
		List<N33_JXBEntry> jxbEntriesList = jxbDao.getJXBList(xqids, teacherId);
		jxbEntriesList.addAll(jxbDao.getZXJXBList(xqids));
		//专项课Ids
		List<ObjectId> zxkIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntriesList) {
			zxkIds.add(jxbEntry.getID());
		}
		List<ObjectId> ods = new ArrayList<ObjectId>();
		for (N33_YKBEntry ykbEntry : entries) {
			if (ykbEntry.getType() == 5) {
				ods.add(ykbEntry.getJxbId());
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

		//教室列表
		List<N33_ClassroomEntry> classRoomDTO = classroomService.getRoomListBySchoolId(schoolId, xqid);
		//返回数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (N33_YKBEntry entry : entries) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (entry.getJxbId() == null) {
				continue;
			}
			map.put("x", entry.getX() + 1);
			map.put("y", entry.getY() + 1);
			map.put("ykbId", entry.getID().toString());
			if(entry.getType() != 4){
				if(entry.getTeacherId().toString().equals(teacherId.toString())) {
					map.put("jxbId", entry.getJxbId().toString());
				}else{
					map.put("jxbId", entry.getNJxbId().toString());
				}
			}
			map.put("type", entry.getType());
			if (entry.getType() != 4 && entry.getType() != 5) {
				if (entry.getNTeacherId() != null) {
					if (entry.getNTeacherId().toString().equals(teacherId.toString())) {
						for (N33_KSDTO ksdto : gradeSubject) {
							//获取学科
							if (ksdto.getSubid().equals(entry.getNSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								map.put("subName", ksdto.getSnm());
								break;
							}
						}
						//教学班
						N33_JXBEntry entry1 = jxbEntries.get(entry.getNJxbId());
						if (entry1 != null) {
							List<ObjectId> objectIds = entry1.getStudentIds();
							map.put("count", objectIds != null ? objectIds.size() : 0);
							if(entry1.getNickName() != null && !"".equals(entry1.getNickName())){
								map.put("JxbName", entry1.getNickName());
							}else{
								map.put("JxbName", entry1.getName());
							}
							//教室
							for (N33_ClassroomEntry room : classRoomDTO) {
								//获取学科
								if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
									map.put("roomName", room.getRoomName());
									break;
								} else {
									map.put("roomName", "");
								}
							}
							list.add(map);
						}
					} else {
						for (N33_KSDTO ksdto : gradeSubject) {
							//获取学科
							if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
								map.put("subName", ksdto.getSnm());
								break;
							}
						}
						//教学班
						N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
						if (entry1 != null) {
							List<ObjectId> objectIds = entry1.getStudentIds();
							map.put("count", objectIds != null ? objectIds.size() : 0);
							if(entry1.getNickName() != null && !"".equals(entry1.getNickName())){
								map.put("JxbName", entry1.getNickName());
							}else{
								map.put("JxbName", entry1.getName());
							}
							//教室
							for (N33_ClassroomEntry room : classRoomDTO) {
								//获取学科
								if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
									map.put("roomName", room.getRoomName());
									break;
								} else {
									map.put("roomName", "");
								}
							}
							list.add(map);
						}
					}
				} else {
					for (N33_KSDTO ksdto : gradeSubject) {
						//获取学科
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							map.put("subName", ksdto.getSnm());
							break;
						}
					}
					//教学班
					N33_JXBEntry entry1 = jxbEntries.get(entry.getJxbId());
					if (entry1 != null) {
						List<ObjectId> objectIds = entry1.getStudentIds();
						map.put("count", objectIds != null ? objectIds.size() : 0);
						if(entry1.getNickName() != null && !"".equals(entry1.getNickName())){
							map.put("JxbName", entry1.getNickName());
						}else{
							map.put("JxbName", entry1.getName());
						}
						//教室
						for (N33_ClassroomEntry room : classRoomDTO) {
							//获取学科
							if (room.getRoomId().toString().equals(entry.getClassroomId().toString())) {
								map.put("roomName", room.getRoomName());
								break;
							} else {
								map.put("roomName", "");
							}
						}
						list.add(map);
					}
				}
			} else {
				if (entry.getType() == 4) {
					for (N33_KSDTO ksdto : gradeSubject) {
						//获取学科
						if (ksdto.getSubid().equals(entry.getSubjectId().toString()) && ksdto.getGid().equals(entry.getGradeId().toString())) {
							map.put("subName", ksdto.getSnm());
							break;
						}
					}
					List<N33_ZhuanXiangEntry> zhuanList = listMap.get(entry.getJxbId());
					for (N33_ZhuanXiangEntry entry1 : zhuanList) {
						if (entry1.getTeaId().toString().equals(teacherId.toString())) {
							map.put("JxbName", entry1.getName());
							map.put("count", entry1.getStudentId().size());
							for (N33_ClassroomEntry room : classRoomDTO) {
								//获取教室
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
					map.put("JxbName", entry1.getName());
					map.put("count", entry1.getStudentIds().size());
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
		return list;
	}

	/**
	 * 获得老师教学班列表
	 *
	 * @return
	 */
	public Map<ObjectId, List<N33_JXBDTO>> getUserJiaoXueBanByXqid(ObjectId xqid) {
		//全校教学班
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(xqid);
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = new HashMap<ObjectId, N33_JXBEntry>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			jxbEntryMap.put(jxbEntry.getID(),jxbEntry);
		}
		//返回数据
		Map<ObjectId, List<N33_JXBDTO>> TeaJiaoMap = new HashMap<ObjectId, List<N33_JXBDTO>>();
		//老师List
		List<ObjectId> TeaList = new ArrayList<ObjectId>();
		//加载老师
		for (N33_JXBEntry entry : jxbEntries) {
			if (entry.getType() == 4) {
				List<N33_ZhuanXiangEntry> zxList = zhuanXiangDao.findN33_ZhuanXiangEntry(entry.getID(), entry.getTermId());
				for (N33_ZhuanXiangEntry entry1 : zxList) {
					if (!TeaList.contains(entry1.getTeaId())) {
						if (entry1.getTeaId() != null) {
							TeaList.add(entry1.getTeaId());
						}
					}
				}
			} else if (entry.getType() == 6){
				N33_JXBEntry relaJXB = jxbEntryMap.get(entry.getRelativeId());
				if(null!=relaJXB) {
					if (!TeaList.contains(entry.getTercherId())) {
						TeaList.add(entry.getTercherId());
					}
					if (!TeaList.contains(relaJXB.getTercherId())) {
						TeaList.add(relaJXB.getTercherId());
					}
				}
			}else{
				if (!TeaList.contains(entry.getTercherId())) {
					if (entry.getTercherId() != null) {
						TeaList.add(entry.getTercherId());
					}
				}
			}

		}
		Map<ObjectId, List<N33_ZhuanXiangEntry>> zlEntryMap = zhuanXiangDao.getZhuanXiangMap(xqid);
		//创建老师对应的教学班数据
		for (ObjectId teaId : TeaList) {
			//老师教学班
			List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
			for (N33_JXBEntry entry : jxbEntries) {
				List<N33_ZhuanXiangEntry> zxList = zlEntryMap.get(entry.getID());
				List<ObjectId> zxTeaIds = MongoUtils.getFieldObjectIDs(zxList, "tid");
				if (entry.getTercherId() != null) {
					try {
						if (teaId.toString().equals(entry.getTercherId().toString())) {
							jxbdtos.add(new N33_JXBDTO(entry));
						}
					} catch (Exception e) {
						continue;
					}
				} else {
					if (zxTeaIds.contains(teaId)) {
						jxbdtos.add(new N33_JXBDTO(entry));
					}
				}
			}
			TeaJiaoMap.put(teaId, jxbdtos);
		}
		return TeaJiaoMap;
	}

	/**
	 * @param ykbIds
	 * @param schoolId
	 */
	public void undoYKB(String ykbIds, ObjectId schoolId) {
		List<ObjectId> ykbs = MongoUtils.convert(ykbIds);
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysByIds(ykbs, schoolId);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry entry : ykbEntryList) {
				if (entry.getType() == 4) {
					List<N33_YKBEntry> ykbEntryList2 = ykbDao.getYKBByjxbId(entry.getJxbId(), entry.getX(), entry.getY());
					if (ykbEntryList2 != null && ykbEntryList2.size() != 0) {
						for (N33_YKBEntry ykb : ykbEntryList2) {
							N33_YKBEntry n33_ykbEntry = new N33_YKBEntry(ykb.getX(), ykb.getY(), ykb.getClassroomId(), ykb.getSchoolId(), ykb.getTermId(), 0);
							n33_ykbEntry.setID(ykb.getID());
							n33_ykbEntry.setNJxbId(null);
							n33_ykbEntry.setNSubjectId(null);
							n33_ykbEntry.setNTeacherId(null);
							n33_ykbEntry.setZuHeId(null);
							ykbDao.updateN33_YKB(n33_ykbEntry);
						}
						N33_JXBKSEntry jxbksEntry = jxbksDao.getJXBKSsByJXBId(entry.getJxbId());
						if (jxbksEntry != null) {
							jxbksEntry.setYpCount(jxbksEntry.getYpCount() - 1 >= 0 ? jxbksEntry.getYpCount() - 1 : 0);
						}
						jxbksDao.updateN33_JXBKS(jxbksEntry);
					}
				} else {
					if (entry.getType() == 5) {
						zixikeDao.removeN33_ZIXIKEEntry(entry.getJxbId());
						N33_YKBEntry n33_ykbEntry = new N33_YKBEntry(entry.getX(), entry.getY(), entry.getClassroomId(), entry.getSchoolId(), entry.getTermId(), 0);
						n33_ykbEntry.setID(entry.getID());
						n33_ykbEntry.setNJxbId(null);
						n33_ykbEntry.setNSubjectId(null);
						n33_ykbEntry.setNTeacherId(null);
						ykbDao.updateN33_YKB(n33_ykbEntry);
					} else {
						N33_YKBEntry n33_ykbEntry = new N33_YKBEntry(entry.getX(), entry.getY(), entry.getClassroomId(), entry.getSchoolId(), entry.getTermId(), 0);
						n33_ykbEntry.setID(entry.getID());
						n33_ykbEntry.setNJxbId(null);
						n33_ykbEntry.setNSubjectId(null);
						n33_ykbEntry.setNTeacherId(null);
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
		}

	}

	/**
	 * @param gradeId
	 * @param classRoomIds
	 * @param weeks
	 * @param indexs
	 * @param response
	 */
	public void exportPKData(ObjectId schoolId, ObjectId termId, String gradeId, String classRoomIds, String weeks, String indexs, HttpServletResponse response) {
		Map map = new HashMap();
		List<N33_ClassroomDTO> classroomDTOs = classroomService.getRoomEntryListByXqGrade(termId, schoolId, new ObjectId(gradeId), 1);
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), termId);
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
		N33_GradeWeekRangeEntry gradeWeekRangeEntry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(termId, schoolId, new ObjectId(gradeId));
		checkKS(schoolId, termId, classroomIds, courseRangeDTOs.size(), gradeWeekRangeEntry);
		if (!StringUtils.isEmpty(weeks)) {
			String[] weekArg = weeks.split(",");
		}
		if (!StringUtils.isEmpty(indexs)) {
			String[] indexArg = indexs.split(",");
		}

		Map<String, List<N33_YKBDTO>> ykbDTOMap = new HashMap<String, List<N33_YKBDTO>>();
		Map<String, List<N33_YKBDTO>> ykbDTOMap2 = new HashMap<String, List<N33_YKBDTO>>();
		List<String> swlist = new ArrayList<String>();
		TermEntry termEntry = termDao.getTermByTimeId(termId);
		List<N33_SWDTO> swdtos = swService.getGuDingShiWuBySchoolId(termEntry.getID(), schoolId);
		if (swdtos != null && swdtos.size() != 0) {
			for (N33_SWDTO swdto : swdtos) {
				swlist.add(swdto.getXy());
			}
		}
		List<N33_YKBDTO> n33_ykbdtos = new ArrayList<N33_YKBDTO>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBsByclassRoomIds(termId, schoolId, classroomIds);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "tid");
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, termId);
			List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "jxbId");
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

			for (N33_YKBEntry ykbEntry : ykbEntryList) {
				n33_ykbdtos.add(new N33_YKBDTO(ykbEntry));
				List<N33_YKBDTO> n33_ykbdtos1 = ykbDTOMap.get(ykbEntry.getClassroomId().toString());
				if (n33_ykbdtos1 == null || n33_ykbdtos1.size() == 0) {
					n33_ykbdtos1 = new ArrayList<N33_YKBDTO>();
				}
				N33_YKBDTO ykbdto = new N33_YKBDTO(ykbEntry);
				if (StringUtils.isNotEmpty(ykbdto.getTeacherId())) {
					ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())).getUserName() : "");
				}
				if (StringUtils.isNotEmpty(ykbdto.getJxbId())) {
					N33_JXBEntry jxbEntry = jxbEntryMap.get(new ObjectId(ykbdto.getJxbId()));
					String jxbNm = "";
					if (jxbEntry != null) {
						if (StringUtils.isNotEmpty(jxbEntry.getNickName())) {
							jxbNm = jxbEntry.getNickName();
						} else {
							jxbNm = jxbEntry.getName();
						}
					}
					ykbdto.setJxbName(jxbNm);
					ykbdto.setStudentCount(jxbEntry != null ? jxbEntry.getStudentIds().size() : 0);
					ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getJxbId())) : "");
				}
				ykbdto.setSwType(0);
				if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY())) {
					ykbdto.setSwType(1);
				}
				n33_ykbdtos1.add(ykbdto);
				ykbDTOMap.put(ykbEntry.getClassroomId().toString(), n33_ykbdtos1);
			}
		}
		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			for (N33_ClassroomDTO dto : classroomDTOs) {
				ykbDTOMap2.put(dto.getRoomId(), ykbDTOMap.get(dto.getRoomId()));
			}
		}
		map.put("courseRangeDTOs", courseRangeDTOs);
		map.put("ykbdto", ykbDTOMap2);
		map.put("classrooms", classroomDTOs);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("课表");
		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			for (int i = 0; i < classroomDTOs.size(); i++) {
				sheet.addMergedRegion(new Region(0, (short) (i * 5 + 1), 0, (short) (i * 5 + 5)));
			}
		}
		//为sheet1生成第一行，用于放表头信息
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("班级 / 教室");

		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			int it = 1;
			for (int i = 0; i < classroomDTOs.size(); i++) {
				cell = row.createCell(it);
				cell.setCellValue(classroomDTOs.get(i).getRoomName());
				it += 5;
			}
		}

		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue("课节 / 日");
		List<String> weekLists = new ArrayList<String>();
		weekLists.add("周一");
		weekLists.add("周二");
		weekLists.add("周三");
		weekLists.add("周四");
		weekLists.add("周五");
		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			for (int i = 0; i < classroomDTOs.size(); i++) {
				for (int j = 0; j < weekLists.size(); j++) {
					cell = row.createCell(j + 5 * i + 1);
					cell.setCellValue(weekLists.get(j));
				}
			}
		}
		int page = 1;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (int i = 0; i < courseRangeDTOs.size(); i++) {
				page++;
				row = sheet.createRow(page);
				cell = row.createCell(0);
				cell.setCellValue(courseRangeDTOs.get(i).getName());
				for (int z = 0; z < classroomDTOs.size(); z++) {
					List<N33_YKBDTO> ykbdtos = ykbDTOMap.get(classroomDTOs.get(z).getRoomId());
					Map<String, N33_YKBDTO> ykbmap = new HashMap<String, N33_YKBDTO>();
					if (ykbdtos != null && ykbdtos.size() != 0) {
						for (N33_YKBDTO ykbdto : ykbdtos) {
							ykbmap.put(ykbdto.getX() + "" + ykbdto.getY(), ykbdto);
						}
					}
					for (int j = 0; j < 5; j++) {
						cell = row.createCell(z * 5 + j + 1);
						N33_YKBDTO dto = ykbmap.get(j + "" + i);
						if (dto != null) {
							if (dto.getSwType() == 0) {
								StringBuffer sb = new StringBuffer();
								if (dto.getJxbName() != null) {
									sb.append(dto.getJxbName()).append(dto.getStudentCount());
								}
								if (dto.getTeacherName() != null) {
									sb.append(dto.getTeacherName());
								}
								if (dto.getRemarks() != null) {
									sb.append(dto.getRemarks());
								}
								String con = sb.toString();
								cell.setCellValue(con);
							} else {
								cell.setCellValue("全校事务");
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
	 * 可排教学班
	 *
	 * @param gradeId
	 * @param subjectId
	 * @return
	 */
	public List<N33_JXBDTO> getkpjxb(ObjectId xqId, String gradeId, String subjectId, ObjectId schoolId, int type) {
		List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
		N33_KSDTO ksdto = subjectService.getIsolateSubjectByGradeId(xqId, schoolId, new ObjectId(gradeId), new ObjectId(subjectId));
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
		int acClassType = turnOffService.getAcClassType(schoolId, new ObjectId(gradeId), ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(schoolId, new ObjectId(gradeId), new ObjectId(subjectId), xqId, type, acClassType);

		Map<ObjectId, N33_StudentTagEntry> studentTagEntryMap = new HashMap<ObjectId, N33_StudentTagEntry>();
		if (jxbEntries != null && jxbEntries.size() != 0) {
			List<ObjectId> tagIds = MongoUtils.getFieldObjectIDs(jxbEntries, "tags");
			studentTagEntryMap = studentTagDao.getTagListByIdsMap(tagIds);
		}
		Map<ObjectId, Integer> ypCountMap = new HashMap<ObjectId, Integer>();
		List<N33_JXBKSEntry> jxbksEntries = jxbksDao.getJXBKSsBySubjectId(xqId, schoolId, new ObjectId(gradeId), new ObjectId(subjectId));

		if (jxbksEntries != null && jxbksEntries.size() != 0) {
			for (N33_JXBKSEntry jxbksEntry : jxbksEntries) {
				ypCountMap.put(jxbksEntry.getJxbId(), jxbksEntry.getYpCount());
			}
		}
		if (jxbEntries != null && jxbEntries.size() != 0) {
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(MongoUtils.getFieldObjectIDs(jxbEntries, "tid"), xqId);
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				String tag = "";
				List<ObjectId> tags = jxbEntry.getTagIds();
				if (tags != null && tags.size() != 0) {
					for (ObjectId id : tags) {
						tag += studentTagEntryMap.get(id) != null ? studentTagEntryMap.get(id).getName() : "" + ",";
					}
				}
				N33_JXBDTO jxbdto = new N33_JXBDTO(jxbEntry);
				jxbdto.setTeacherName(teaEntryMap.get(jxbEntry.getTercherId()) != null ? teaEntryMap.get(jxbEntry.getTercherId()).getUserName() : "");
				if (jxbEntry.getType() == 1) {
					jxbdto.setZksCount(jxbdto.getJxbks());
				} else {
					jxbdto.setZksCount(jxbdto.getJxbks());
				}

				jxbdto.setRemarks(tag);
				if (ypCountMap.get(jxbEntry.getID()) != null) {
					jxbdto.setYpksCount(ypCountMap.get(jxbEntry.getID()));
				} else {
					jxbdto.setYpksCount(0);
				}

				jxbdtos.add(jxbdto);
			}
		}
		return jxbdtos;
	}

	public List<Map<String,Object>> getZhuanXiangYKBIds(List<String> ykbIds,ObjectId sid,ObjectId ciId){
		List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();
		List<ObjectId> ykbIds1 = MongoUtils.convertToObjectIdList(ykbIds);
		Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(ciId,sid);
		Map<ObjectId,N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomEntryListByXqGradeForMap(ciId,sid);
		List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysByIds(ykbIds1,sid);
		for (N33_YKBEntry ykbEntry : ykbEntryList) {
			if(ykbEntry.getNJxbId() != null && ykbEntry.getNJxbId().toString() != ""){
				N33_JXBEntry jxbEntry = jxbDao.getJXBById(ykbEntry.getNJxbId());
				List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(ykbEntry.getNJxbId(),ciId);
				for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("roomName",classroomEntryMap.get(zhuanXiangEntry.getRoomId()).getRoomName());
					map.put("teaName",teaEntryMap.get(zhuanXiangEntry.getTeaId()).getUserName());
					map.put("stuCount",zhuanXiangEntry.getStudentId().size());
					map.put("jxbName",zhuanXiangEntry.getName());
					if(jxbEntry.getNickName() != null && jxbEntry.getNickName() != ""){
						map.put("zuHeName",jxbEntry.getNickName());
					}else{
						map.put("zuHeName",jxbEntry.getName());
					}
					retList.add(map);
				}
			}
			if(ykbEntry.getJxbId() != null && ykbEntry.getJxbId().toString() != ""){
				List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(ykbEntry.getJxbId(),ciId);
				N33_JXBEntry jxbEntry = jxbDao.getJXBById(ykbEntry.getJxbId());
				for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("roomName",classroomEntryMap.get(zhuanXiangEntry.getRoomId()).getRoomName());
					map.put("teaName",teaEntryMap.get(zhuanXiangEntry.getTeaId()).getUserName());
					map.put("stuCount",zhuanXiangEntry.getStudentId().size());
					map.put("jxbName",zhuanXiangEntry.getName());
					if(jxbEntry.getNickName() != null && jxbEntry.getNickName() != ""){
						map.put("zuHeName",jxbEntry.getNickName());
					}else{
						map.put("zuHeName",jxbEntry.getName());
					}
					retList.add(map);
				}
			}
		}
		return retList;
	}

	/**
	 * 加载课表list
	 *
	 * @param gradeId
	 * @param classRoomId
	 * @param schoolId
	 */
	public Map getKeBiaoList(ObjectId termId, String gradeId, String classRoomId, ObjectId schoolId) {
		Map map = new HashMap();
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), termId);
		List<ObjectId> classroomIds = new ArrayList<ObjectId>();
		classroomIds.add(new ObjectId(classRoomId));
		N33_GradeWeekRangeEntry gradeWeekRangeEntry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(termId, schoolId, new ObjectId(gradeId));
		List<IdNameValuePairDTO> weekList = new ArrayList<IdNameValuePairDTO>();
		String[] weekArgs = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
		int ypcnt = 0;
		if (gradeWeekRangeEntry != null) {
			for (int i = gradeWeekRangeEntry.getStart(); i <= gradeWeekRangeEntry.getEnd(); i++) {
				IdNameValuePairDTO dto = new IdNameValuePairDTO();
				dto.setName(weekArgs[i - 1]);
				dto.setType(i);
				weekList.add(dto);
			}
		}
		checkKS(schoolId, termId, classroomIds, courseRangeDTOs.size(), gradeWeekRangeEntry);
		List<Integer> weekIds = new ArrayList<Integer>();
		int weekCount = gradeWeekRangeEntry.getEnd() - gradeWeekRangeEntry.getStart() + 1;
		for (int i = 0; i < weekCount; i++) {
			weekIds.add(i);
		}
		List<Integer> indexIds = new ArrayList<Integer>();

		Map<String, List<N33_YKBDTO>> ykbDTOMap = new HashMap<String, List<N33_YKBDTO>>();
		Map<String, List<N33_YKBDTO>> ykbDTOMap2 = new HashMap<String, List<N33_YKBDTO>>();
		List<String> swlist = new ArrayList<String>();
		TermEntry termEntry = termDao.getTermByTimeId(termId);
		List<N33_SWDTO> swdtoListExceptGD = swService.getSwdtoListExceptGD(termEntry.getID(),schoolId);
		List<N33_SWDTO> swdtos = swService.getGuDingShiWuBySchoolId(termEntry.getID(), schoolId);
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
		List<N33_GDSXDTO> gdsxdtos = gdsxService.getGDSXBySidAndXqid(termEntry.getID(), schoolId, new ObjectId(gradeId));
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

		List<N33_YKBDTO> n33_ykbdtos = new ArrayList<N33_YKBDTO>();
		List<N33_YKBEntry> ykbglEntryList = ykbDao.getYKBsByclassRoomIds(termId, schoolId, classroomIds, weekIds, indexIds);
		
		
		if (ykbglEntryList != null && ykbglEntryList.size() != 0) {
			
			//过滤重复的坐标
			Map glMap = new HashMap();
			Map ykbIVMap = new HashMap();
			for (N33_YKBEntry ykbEntry : ykbglEntryList) {
				if(glMap.containsKey(ykbEntry.getX() + "" + ykbEntry.getY())){
					if(ykbEntry.getJxbId() != null){
						ykbIVMap.put(ykbEntry.getX() + "" + ykbEntry.getY(), ykbEntry);
					}
				}else{
					ykbIVMap.put(ykbEntry.getX() + "" + ykbEntry.getY(), ykbEntry);
				}
			}
			
			Collection valueCollection = ykbIVMap.values();
		    final int size = valueCollection.size();

		    List<N33_YKBEntry> ykbEntryList = new ArrayList<N33_YKBEntry>(valueCollection);
			
			List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "tid");
			for (N33_YKBEntry ykbEntry:ykbEntryList) {
				if(ykbEntry.getNTeacherId() != null && ykbEntry.getNTeacherId().toString() != "" && !teacherIds.contains(ykbEntry.getNTeacherId())){
					teacherIds.add(ykbEntry.getNTeacherId());
				}
			}
			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, termId);
			List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList, "jxbId");
			if (MongoUtils.getFieldObjectIDs(ykbEntryList, "nJxbId") != null && MongoUtils.getFieldObjectIDs(ykbEntryList, "nJxbId").size() != 0) {
				jxbIds.addAll(MongoUtils.getFieldObjectIDs(ykbEntryList, "nJxbId"));
			}

			Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapsByIds(jxbIds);
			Map<ObjectId, N33_ZIXIKEEntry> zixikeEntryMap = zixikeDao.getJXBsBySubIdsByMap(termId);
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

			Map<ObjectId,List<N33_ZhuanXiangEntry>> zhuanXiangMap = zhuanXiangDao.getZhuanXiangMap(termId);

			
			for (N33_YKBEntry ykbEntry : ykbEntryList) {
				n33_ykbdtos.add(new N33_YKBDTO(ykbEntry));
				List<N33_YKBDTO> n33_ykbdtos1 = ykbDTOMap.get(ykbEntry.getClassroomId().toString());
				if (n33_ykbdtos1 == null || n33_ykbdtos1.size() == 0) {
					n33_ykbdtos1 = new ArrayList<N33_YKBDTO>();
				}
				N33_YKBDTO ykbdto = new N33_YKBDTO(ykbEntry);
				if (StringUtils.isNotEmpty(ykbdto.getTeacherId())) {
					ykbdto.setTeacherName(teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getTeacherId())).getUserName() : "");
				} else {
					ykbdto.setTeacherName("");
				}
				if (StringUtils.isNotEmpty(ykbdto.getNteacherId())) {
					ykbdto.setNteacherName(teaEntryMap.get(new ObjectId(ykbdto.getNteacherId())) != null ? teaEntryMap.get(new ObjectId(ykbdto.getNteacherId())).getUserName() : "");
				} else {
					ykbdto.setNteacherName("");
				}
				if (StringUtils.isNotEmpty(ykbdto.getJxbId())) {
					ypcnt++;
					N33_JXBEntry jxbEntry = jxbEntryMap.get(new ObjectId(ykbdto.getJxbId()));
					String jxbNm = "";
					if (jxbEntry != null) {
						if (StringUtils.isNotEmpty(jxbEntry.getNickName())) {
							jxbNm = jxbEntry.getNickName();
						} else {
							jxbNm = jxbEntry.getName();
						}
					}

					ykbdto.setJxbName(jxbNm);
					ykbdto.setStudentCount(jxbEntry != null ? jxbEntry.getStudentIds().size() : 0);
					ykbdto.setRemarks(tagMaps.get(new ObjectId(ykbdto.getJxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getJxbId())) : "");
					if (ykbEntry.getType() == 5) {
						N33_ZIXIKEEntry zixikeEntry = zixikeEntryMap.get(new ObjectId(ykbdto.getJxbId()));
						if (zixikeEntry != null) {
							ykbdto.setJxbName(zixikeEntry.getName());
							ykbdto.setStudentCount(zixikeEntry.getStudentIds().size());
						}
					}
				}
				if (StringUtils.isNotEmpty(ykbdto.getNjxbId())) {
					N33_JXBEntry jxbEntry2 = jxbEntryMap.get(new ObjectId(ykbdto.getNjxbId()));
					String jxbNm2 = "";
					if (jxbEntry2 != null) {
						if (StringUtils.isNotEmpty(jxbEntry2.getNickName())) {
							jxbNm2 = jxbEntry2.getNickName();
						} else {
							jxbNm2 = jxbEntry2.getName();
						}
					}
					ykbdto.setNjxbName(jxbNm2);
					ykbdto.setNstudentCount(jxbEntry2 != null ? jxbEntry2.getStudentIds().size() : 0);
					ykbdto.setNremarks(tagMaps.get(new ObjectId(ykbdto.getNjxbId())) != null ? tagMaps.get(new ObjectId(ykbdto.getNjxbId())) : "");
				}
				ykbdto.setSwType(0);
				ykbdto.setIsSWAndCourse(0);
				if (swlist != null && swlist.size() != 0 && swlist.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY())) {
					ykbdto.setSwType(1);
					if((ykbdto.getJxbId() != null && ykbdto.getJxbId() != "") || (ykbdto.getNjxbId() != null && ykbdto.getNjxbId() != "")){
						ykbdto.setIsSWAndCourse(1);
					}
					ykbdto.setRemarks(swdtoMap.get(ykbEntry.getX() + "," + ykbEntry.getY()).getDesc());
				}
				if(swdtoListExceptGD != null && swdtoListExceptGD.size() != 0){
					for (N33_SWDTO swdto : swdtoListExceptGD) {
						if(ykbdto.getKbType() != 4){
							if(((ykbdto.getJxbId() != null && ykbdto.getJxbId() != "") || (ykbdto.getNjxbId() != null && ykbdto.getNjxbId() != "")) && ykbdto.getX() == (swdto.getY() - 1) && ykbdto.getY() == (swdto.getX() - 1) && (swdto.getTids().contains(ykbdto.getTeacherId()) || swdto.getTids().contains(ykbdto.getNteacherId()))){
								ykbdto.setSwType(3);
								ykbdto.setIsSWAndCourse(1);
							}
						}else{
							List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangMap.get(new ObjectId(ykbdto.getJxbId()));
							if(zhuanXiangEntryList != null){
								for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
									if(ykbdto.getX() == (swdto.getY() - 1) && ykbdto.getY() == (swdto.getX() - 1) && (swdto.getTids().contains(zhuanXiangEntry.getTeaId().toString()))){
										ykbdto.setSwType(3);
										ykbdto.setIsSWAndCourse(1);
										break;
									}
								}
							}
						}
					}
				}
				if (gdsxList != null && gdsxList.size() != 0 && gdsxList.contains(String.valueOf(ykbEntry.getX()) + ykbEntry.getY())) {
					ykbdto.setSwType(2);
					if((ykbdto.getJxbId() != null && ykbdto.getJxbId() != "") || (ykbdto.getNjxbId() != null && ykbdto.getNjxbId() != "")){
						ykbdto.setIsSWAndCourse(1);
					}
					ykbdto.setRemarks(gdsxdtoMap.get(ykbEntry.getX() + "," + ykbEntry.getY()).getDesc());
				}
				ykbdto.setWeekCount(weekCount);
				n33_ykbdtos1.add(ykbdto);
				ykbDTOMap.put(ykbEntry.getClassroomId().toString(), n33_ykbdtos1);
			}
		}
		for (Map.Entry<String, List<N33_YKBDTO>> entry : ykbDTOMap.entrySet()) {
			List<N33_YKBDTO> ykbdtos = entry.getValue();
			Collections.sort(ykbdtos, new Comparator<N33_YKBDTO>() {
				@Override
				public int compare(N33_YKBDTO o1, N33_YKBDTO o2) {
					return o1.getY() - o2.getY();
				}
			});
			ykbDTOMap.put(entry.getKey(), ykbdtos);
		}
		List<N33_ClassroomDTO> classroomDTOs2 = new ArrayList<N33_ClassroomDTO>();
		List<N33_ClassroomDTO> classroomDTOs = classroomService.getRoomEntryListByXqGrade(termId, schoolId, new ObjectId(gradeId), 1);
		if (classroomDTOs != null && classroomDTOs.size() != 0) {
			for (N33_ClassroomDTO dto : classroomDTOs) {
				if (dto.getRoomId().equals(classRoomId)) {
					dto.setWeekList(weekList);
					classroomDTOs2.add(dto);
					ykbDTOMap2.put(dto.getRoomId(), ykbDTOMap.get(dto.getRoomId()));
					/*for(N33_YKBDTO ykbdto : ykbDTOMap.get(dto.getRoomId())){
						System.out.println(ykbdto.getX() + "   " + ykbdto.getY());
					}*/
				}

			}
		}
		map.put("courseRangeDTOs", courseRangeDTOs);
		map.put("ykbdto", ykbDTOMap2);
		map.put("classrooms", classroomDTOs2);
		map.put("ypCnt", ypcnt);
		map.put("allCnt", weekCount * courseRangeDTOs.size() - swdtos.size());
		return map;
	}

	/**
	 * 查询老师的教学班数量
	 *
	 * @param ciId
	 * @param sid
	 * @param teaId
	 * @param gradeId
	 * @return
	 */
	public Integer getTeacherJXBCount(ObjectId ciId, ObjectId sid, ObjectId teaId, ObjectId gradeId, Integer type) {
		List<N33_KSDTO> ksdtos = subjectService.getIsolateSubjectByGradeId(ciId, sid, gradeId.toString(), type);
		Map<String, N33_KSDTO> ksdtoMap = subjectService.getIsolateSubjectByGradeIdMap(ciId, sid, gradeId.toString());
		List<ObjectId> subjectIds = new ArrayList<ObjectId>();
		if (ksdtos != null && ksdtos.size() != 0) {
			for (N33_KSDTO dto : ksdtos) {
				subjectIds.add(new ObjectId(dto.getSubid()));
				//ksdtoMap.put(new ObjectId(dto.getSubid()),dto);
			}
		}
		//jxb add
		int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
		//jxb add			

		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBListV1(sid, gradeId, subjectIds, teaId, ciId, type,acClassType);
        /*Integer count = jxbDao.countTeacherCarryJxbNUM(ciId,teaId,gradeId);
        if(count == null){
            count = 0;
        }*/
		Integer count = jxbEntries.size();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			if ((jxbEntry.getType() == 1 &&jxbEntry.getJXBKS() == 0) || (jxbEntry.getType() != 1 && jxbEntry.getJXBKS() == 0) || jxbEntry.getType() == 4) {
				count--;
			}
		}
		if (type == 0 || type == 4) {
			List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(sid, gradeId, ciId,4,acClassType);
			if (jxbEntryList != null && jxbEntryList.size() > 0) {
				List<ObjectId> jxbIds = new ArrayList<ObjectId>();
				for (N33_JXBEntry n33_jxbEntry : jxbEntryList) {
					if (n33_jxbEntry.getType() == 1 && n33_jxbEntry.getJXBKS() != 0) {
						jxbIds.add(n33_jxbEntry.getID());
					} else if (n33_jxbEntry.getType() != 1 && n33_jxbEntry.getJXBKS() != 0) {
						jxbIds.add(n33_jxbEntry.getID());
					} else {
						continue;
					}
				}
				//List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(jxbEntryList,"_id");
				List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbIds);
				for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
					if (teaId.toString().equals(zhuanXiangEntry.getTeaId().toString())) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 查询教室的教学班数量
	 *
	 * @param ciId
	 * @param sid
	 * @param roomId
	 * @param gradeId
	 * @return
	 */
	public Integer getRoomJXBCount(ObjectId ciId, ObjectId sid, ObjectId roomId, ObjectId gradeId, Integer type) {
		List<N33_KSDTO> ksdtos = subjectService.getIsolateSubjectByGradeId(ciId, sid, gradeId.toString(), type);
		Map<String, N33_KSDTO> ksdtoMap = subjectService.getIsolateSubjectByGradeIdMap(ciId, sid, gradeId.toString());
		List<ObjectId> subjectIds = new ArrayList<ObjectId>();
		if (ksdtos != null && ksdtos.size() != 0) {
			for (N33_KSDTO dto : ksdtos) {
				subjectIds.add(new ObjectId(dto.getSubid()));
				//ksdtoMap.put(new ObjectId(dto.getSubid()),dto);
			}
		}
		//jxb add
		int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gradeId, subjectIds, roomId, ciId, type,acClassType);

		Integer count = jxbEntries.size();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			if ((jxbEntry.getType() == 1 && jxbEntry.getJXBKS() == 0) || (jxbEntry.getType() != 1 && jxbEntry.getJXBKS() == 0) || jxbEntry.getType() == 4) {
				count--;
			}
		}
		if (type == 0 || type == 4) {
			List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(sid, gradeId, ciId,4,acClassType);
			if (jxbEntryList != null && jxbEntryList.size() > 0) {
				List<ObjectId> jxbIds = new ArrayList<ObjectId>();
				for (N33_JXBEntry n33_jxbEntry : jxbEntryList) {
					if (n33_jxbEntry.getType() == 1 && n33_jxbEntry.getJXBKS() != 0) {
						jxbIds.add(n33_jxbEntry.getID());
					} else if (n33_jxbEntry.getType() != 1 && n33_jxbEntry.getJXBKS() != 0) {
						jxbIds.add(n33_jxbEntry.getID());
					} else {
						continue;
					}
				}
				//List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(jxbEntryList,"_id");
				List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbIds);
				for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
					if (roomId.toString().equals(zhuanXiangEntry.getRoomId().toString())) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 按照教师排课获取教师所带的教学班
	 *
	 * @param xqId
	 * @param gradeId
	 * @param subjectId
	 * @param schoolId
	 * @param type
	 * @param teaId
	 * @return
	 */
	public List<N33_JXBDTO> getkpjxbByTeaId(ObjectId xqId, ObjectId gradeId, ObjectId subjectId, ObjectId schoolId, int type, ObjectId teaId) {
		Map<ObjectId, List<N33_ZhuanXiangEntry>> zxMap = zhuanXiangDao.getZhuanXiangMap(xqId);
		List<N33_JXBDTO> returnList = new ArrayList<N33_JXBDTO>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		if (type == 4) {
			List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntryByTeaId(xqId, teaId);
			jxbIds = MongoUtils.getFieldObjectIDs(zhuanXiangEntries, "jxbId");
		}
		N33_KSDTO ksdto = subjectService.getIsolateSubjectByGradeId(xqId, schoolId, gradeId, subjectId);
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(schoolId, gradeId, subjectId, xqId, type,acClassType);
		Map<ObjectId, N33_StudentTagEntry> studentTagEntryMap = new HashMap<ObjectId, N33_StudentTagEntry>();
		if (jxbEntries != null && jxbEntries.size() != 0) {
			List<ObjectId> tagIds = MongoUtils.getFieldObjectIDs(jxbEntries, "tags");
			studentTagEntryMap = studentTagDao.getTagListByIdsMap(tagIds);
		}
		Map<ObjectId, Integer> ypCountMap = new HashMap<ObjectId, Integer>();
		List<N33_JXBKSEntry> jxbksEntries = jxbksDao.getJXBKSsBySubjectId(xqId, schoolId, gradeId, subjectId);
		if (jxbksEntries != null && jxbksEntries.size() != 0) {
			for (N33_JXBKSEntry jxbksEntry : jxbksEntries) {
				ypCountMap.put(jxbksEntry.getJxbId(), jxbksEntry.getYpCount());
			}
		}

		if (jxbEntries != null && jxbEntries.size() != 0) {
			Map<ObjectId, N33_ClassroomEntry> roomEntryMap = classroomDao.getRoomEntryListByXqRoomForMap(xqId, MongoUtils.getFieldObjectIDs(jxbEntries, "clsrmId"));
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				List<ObjectId> teaList = new ArrayList<ObjectId>();
				if (jxbEntry.getType() == 4) {
					List<N33_ZhuanXiangEntry> zxList = zxMap.get(jxbEntry.getID());
					teaList = MongoUtils.getFieldObjectIDs(zxList, "tid");
				}
				if ((type == 4 && jxbIds != null && jxbIds.size() != 0 && jxbIds.contains(jxbEntry.getID())) || type != 4) {
					if ((jxbEntry.getTercherId() == null && teaList != null && teaList.size() > 0 && teaList.contains(teaId) && jxbEntry.getStudentIds().size() > 0) || (jxbEntry.getTercherId() != null && jxbEntry.getTercherId().toString().equals(teaId.toString()))) {
						String tag = "";
						List<ObjectId> tags = jxbEntry.getTagIds();
						if (tags != null && tags.size() != 0) {
							for (ObjectId id : tags) {
								tag += studentTagEntryMap.get(id) != null ? studentTagEntryMap.get(id).getName() : "" + ",";
							}
						}
						N33_JXBDTO jxbdto = new N33_JXBDTO(jxbEntry);
						jxbdto.setClassroomName(roomEntryMap.get(jxbEntry.getClassroomId()) != null ? roomEntryMap.get(jxbEntry.getClassroomId()).getRoomName() : "");
						if (jxbEntry.getType() == 1) {
							jxbdto.setZksCount(jxbdto.getJxbks());
						} else {
							jxbdto.setZksCount(jxbdto.getJxbks());
						}

						jxbdto.setRemarks(tag);
						if (ypCountMap.get(jxbEntry.getID()) != null) {
							jxbdto.setYpksCount(ypCountMap.get(jxbEntry.getID()));
						} else {
							jxbdto.setYpksCount(0);
						}
						returnList.add(jxbdto);
					}
				}
			}
		}
		return returnList;
	}


	public List<N33_JXBDTO> getkpjxbByTag(String type, ObjectId tagId, ObjectId sid, ObjectId gradeId) {
		List<N33_JXBDTO> returnList = new ArrayList<N33_JXBDTO>();
		N33_StudentTagEntry tagEntry = studentTagDao.getTagById(tagId);
		List<ObjectId> jxbIds = tagEntry.getJxbIds();
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBListByIds(jxbIds, type);
		ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
		List<N33_KSDTO> ksdto = subjectService.getIsolateSubjectByGradeId(xqid, sid, gradeId.toHexString());
		Map<ObjectId, N33_StudentTagEntry> studentTagEntryMap = new HashMap<ObjectId, N33_StudentTagEntry>();
		if (jxbEntries != null && jxbEntries.size() != 0) {
			List<ObjectId> tagIds = MongoUtils.getFieldObjectIDs(jxbEntries, "tags");
			studentTagEntryMap = studentTagDao.getTagListByIdsMap(tagIds);
		}

		Map<ObjectId, Integer> ypCountMap = new HashMap<ObjectId, Integer>();

		List<N33_JXBKSEntry> jxbksEntries = jxbksDao.getJXBKSsBySubjectIds(jxbIds, xqid);
		if (jxbksEntries != null && jxbksEntries.size() != 0) {
			for (N33_JXBKSEntry jxbksEntry : jxbksEntries) {
				ypCountMap.put(jxbksEntry.getJxbId(), jxbksEntry.getYpCount());
			}
		}

		if (jxbEntries != null && jxbEntries.size() != 0) {
			Map<ObjectId, N33_ClassroomEntry> roomEntryMap = classroomDao.getRoomEntryListByXqRoomForMap(xqid, MongoUtils.getFieldObjectIDs(jxbEntries, "clsrmId"));
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				String tag = "";
				List<ObjectId> tags = jxbEntry.getTagIds();
				if (tags != null && tags.size() != 0) {
					for (ObjectId id : tags) {
						tag += studentTagEntryMap.get(id) != null ? studentTagEntryMap.get(id).getName() : "" + ",";
					}
				}
				N33_JXBDTO jxbdto = new N33_JXBDTO(jxbEntry);
				jxbdto.setClassroomName(roomEntryMap.get(jxbEntry.getClassroomId()) != null ? roomEntryMap.get(jxbEntry.getClassroomId()).getRoomName() : "");
				if (jxbEntry.getType() == 1) {
					for (N33_KSDTO ksdto1 : ksdto) {
						if (ksdto1.getSubid().toString().equals(jxbEntry.getSubjectId().toString())) {
							jxbdto.setZksCount(jxbEntry.getJXBKS());
						}
					}
				} else {
					for (N33_KSDTO ksdto1 : ksdto) {
						if (ksdto1.getSubid().toString().equals(jxbEntry.getSubjectId().toString())) {
							jxbdto.setZksCount(jxbEntry.getJXBKS());
						}
					}
				}
				jxbdto.setRemarks(tag);
				if (ypCountMap.get(jxbEntry.getID()) != null) {
					jxbdto.setYpksCount(ypCountMap.get(jxbEntry.getID()));
				} else {
					jxbdto.setYpksCount(0);
				}
				returnList.add(jxbdto);
			}
		}
		return returnList;
	}

	/**
	 * @param xqId
	 * @param gradeId
	 * @param schoolId
	 * @param type
	 * @param classroomId
	 * @return
	 */
	public List<N33_JXBDTO> getkpjxbByCrmid(ObjectId xqId, String gradeId, ObjectId schoolId, int type, String classroomId) {
		Map<ObjectId, N33_KSDTO> ksdtoMap = new HashMap<ObjectId, N33_KSDTO>();
		List<ObjectId> subjectIds = new ArrayList<ObjectId>();
		List<N33_KSDTO> ksdtos = subjectService.getIsolateSubjectByGradeId(xqId, schoolId, gradeId, type);
		if (ksdtos != null && ksdtos.size() != 0) {
			for (N33_KSDTO dto : ksdtos) {
				subjectIds.add(new ObjectId(dto.getSubid()));
				ksdtoMap.put(new ObjectId(dto.getSubid()), dto);
			}
		}
		Map<ObjectId, List<N33_ZhuanXiangEntry>> zxMap = zhuanXiangDao.getZhuanXiangMap(xqId);
		List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		if (type == 4) {
			List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntryByRoomId(new ObjectId(classroomId), xqId);
			jxbIds = MongoUtils.getFieldObjectIDs(zhuanXiangEntries, "jxbId");
		}
		//jxb add
		ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
		int acClassType = turnOffService.getAcClassType(schoolId,  new ObjectId(gradeId), ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(schoolId, new ObjectId(gradeId), subjectIds, xqId, type,acClassType );
		Map<ObjectId, N33_StudentTagEntry> studentTagEntryMap = new HashMap<ObjectId, N33_StudentTagEntry>();
		if (jxbEntries != null && jxbEntries.size() != 0) {
			List<ObjectId> tagIds = MongoUtils.getFieldObjectIDs(jxbEntries, "tags");
			studentTagEntryMap = studentTagDao.getTagListByIdsMap(tagIds);
		}
		Map<ObjectId, Integer> ypCountMap = new HashMap<ObjectId, Integer>();
		List<N33_JXBKSEntry> jxbksEntries = jxbksDao.getJXBKSsBySubjectIds(xqId, schoolId, new ObjectId(gradeId), subjectIds);

		if (jxbksEntries != null && jxbksEntries.size() != 0) {
			for (N33_JXBKSEntry jxbksEntry : jxbksEntries) {
				ypCountMap.put(jxbksEntry.getJxbId(), jxbksEntry.getYpCount());
			}
		}
		if (jxbEntries != null && jxbEntries.size() != 0) {

			Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(MongoUtils.getFieldObjectIDs(jxbEntries, "tid"), xqId);
			for (N33_JXBEntry jxbEntry : jxbEntries) {
				List<ObjectId> classRoomList = new ArrayList<ObjectId>();
				if (jxbEntry.getType() == 4) {
					List<N33_ZhuanXiangEntry> zxList = zxMap.get(jxbEntry.getID());
					if (zxList == null || zxList.size() == 0 || jxbEntry.getStudentIds().size() == 0) {
						continue;
					} else {
						classRoomList = MongoUtils.getFieldObjectIDs(zxList, "rid");
					}
				}
				if ((type == 4 && jxbIds != null && jxbIds.size() != 0 && jxbIds.contains(jxbEntry.getID())) || type != 4) {
					if ((jxbEntry.getClassroomId() == null && classRoomList != null && classRoomList.size() > 0 && classRoomList.contains(new ObjectId(classroomId)) && jxbEntry.getStudentIds().size() > 0) || jxbEntry.getClassroomId() != null && jxbEntry.getClassroomId().toString().equals(classroomId)) {
						String tag = "";
						List<ObjectId> tags = jxbEntry.getTagIds();
						if (tags != null && tags.size() != 0) {
							for (ObjectId id : tags) {
								tag += studentTagEntryMap.get(id) != null ? studentTagEntryMap.get(id).getName() : "" + ",";
							}
						}
						N33_JXBDTO jxbdto = new N33_JXBDTO(jxbEntry);
						jxbdto.setTeacherName(teaEntryMap.get(jxbEntry.getTercherId()) != null ? teaEntryMap.get(jxbEntry.getTercherId()).getUserName() : "");
						if (jxbEntry.getType() == 1) {
							jxbdto.setZksCount(jxbEntry.getJXBKS());
						} else {
							jxbdto.setZksCount(jxbEntry.getJXBKS());
						}

						jxbdto.setRemarks(tag);
						if (ypCountMap.get(jxbEntry.getID()) != null) {
							jxbdto.setYpksCount(ypCountMap.get(jxbEntry.getID()));
						} else {
							jxbdto.setYpksCount(0);
						}
						jxbdtos.add(jxbdto);
					}
				}
			}
		}
		return jxbdtos;
	}

	/**
	 * @param termId
	 * @param gradeId
	 * @param schoolId
	 */
	public void checkYKB(ObjectId termId, String gradeId, ObjectId schoolId) {
		List<N33_ClassroomDTO> classroomDTOs = classroomService.getRoomEntryListByXqGrade(termId, schoolId, new ObjectId(gradeId), 1);
		List<ObjectId> classroomIds = new ArrayList<ObjectId>();
		if (classroomIds == null || classroomIds.size() == 0) {
			if (classroomDTOs != null && classroomDTOs.size() != 0) {
				for (N33_ClassroomDTO dto : classroomDTOs) {
					classroomIds.add(new ObjectId(dto.getRoomId()));
				}
			}
		}
		N33_GradeWeekRangeEntry gradeWeekRangeEntry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(termId, schoolId, new ObjectId(gradeId));
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), termId);
		checkKS(schoolId, termId, classroomIds, courseRangeDTOs.size(), gradeWeekRangeEntry);
	}

	/**
	 * 连堂课
	 *
	 * @param jxbId
	 * @param schoolId
	 * @return
	 */
	public Map lianTangs(String jxbId, ObjectId schoolId) {
		Map map = new HashMap();
		N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
		List<ObjectId> teacherIds = new ArrayList<ObjectId>();
		if (jxbEntry.getType() == 1 || jxbEntry.getType() == 2 || jxbEntry.getType() == 3) {
			teacherIds.add(jxbEntry.getTercherId());
		} else if (jxbEntry.getType() == 4) {
			List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbEntry.getID(), jxbEntry.getTermId());
			teacherIds.addAll(MongoUtils.getFieldObjectIDs(zhuanXiangEntries, "tid"));
		} else if (jxbEntry.getType() == 6) {
			N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(jxbEntry.getRelativeId());
			teacherIds.add(jxbEntry.getTercherId());
			teacherIds.add(jxbEntry2.getTercherId());
		}
		List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), jxbEntry.getTermId());
		List<Integer> xs = new ArrayList<Integer>();
		List<Integer> ys = new ArrayList<Integer>();
		int count = 0;
		if (courseRangeDTOs != null && courseRangeDTOs.size() != 0) {
			for (CourseRangeDTO dto : courseRangeDTOs) {
				String time = "2018-1-1 " + dto.getEnd() + ":00";
				String compareTime = "2018-1-1 12:30:00";
				if (DateTimeUtils.getStrToLongTime(compareTime) >= DateTimeUtils.getStrToLongTime(time)) {
					xs.add(count);
				} else {
					ys.add(count);
				}
				count++;
			}
		}
		Map<ObjectId, List<String>> teacherMaps = new HashMap<ObjectId, List<String>>();
		List<N33_YKBEntry> ykbEntryList = ykbDao.getJXBByTidsOrNtids(jxbEntry.getTermId(), teacherIds);
		List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(ykbEntryList,"jxbId");
		jxbIds.addAll(MongoUtils.getFieldObjectIDs(ykbEntryList,"nJxbId"));
		Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapsByIds(jxbIds);
		if (ykbEntryList != null && ykbEntryList.size() != 0) {
			for (N33_YKBEntry ykbEntry : ykbEntryList) {
				List<String> tys = teacherMaps.get(ykbEntry.getTeacherId());
				if (tys == null || tys.size() == 0) {
					tys = new ArrayList<String>();
				}
				N33_JXBEntry jxb = jxbEntryMap.get(ykbEntry.getJxbId());
				if(null==jxb){
					continue;
				}
				tys.add(ykbEntry.getX() + "" + ykbEntry.getY()+ "," + jxb.getName());
				teacherMaps.put(ykbEntry.getTeacherId(), tys);
				if (ykbEntry.getType() == 6) {
					tys = teacherMaps.get(ykbEntry.getNTeacherId());
					if (tys == null || tys.size() == 0) {
						tys = new ArrayList<String>();
					}
					tys.add(ykbEntry.getX() + "" + ykbEntry.getY() + "," + jxb.getName());
					teacherMaps.put(ykbEntry.getNTeacherId(), tys);
				}
			}
		}
		List<Map<String,String>> threeList = new ArrayList<Map<String, String>>();
		List<Map<String,String>> fourList = new ArrayList<Map<String, String>>();
//		List<String> three = new ArrayList<String>();
//		List<String> four = new ArrayList<String>();
		if (teacherMaps != null && teacherMaps.size() != 0) {
			for (Map.Entry<ObjectId, List<String>> entry : teacherMaps.entrySet()) {
				List<String> xys = entry.getValue();
				if (xys != null && xys.size() > 1) {
					for (int i = 0; i < 7; i++) {
						List<Integer> kejie = new ArrayList<Integer>();
						String jxbName = "";
						for (String xy : xys) {
							if (xy.substring(0, 1).equals(String.valueOf(i))) {
								int a = xy.indexOf(",");
								kejie.add(Integer.valueOf(xy.substring(1, a)));
								jxbName = xy.substring(a+1,xy.length());
							}
						}
						if (kejie != null && kejie.size() > 0) {
							getXYS(kejie, threeList, fourList, String.valueOf(i), courseRangeDTOs.size(),jxbName);
						}
					}
				}
			}
		}

		map.put("three", threeList);
		map.put("four", fourList);
		return map;
	}

	/**
	 * @param kejie
	 * @param threeList
	 * @param fourList
	 * @param k
	 */
	private void getXYS(List<Integer> kejie, List<Map<String,String>> threeList, List<Map<String,String>> fourList, String k, int count,String jxbName) {
		List<Integer> xys = new ArrayList<Integer>();
		List<Integer> xys2 = new ArrayList<Integer>();
		List<Integer> xys3 = new ArrayList<Integer>();
		Collections.sort(kejie);
		xys.addAll(kejie);
		xys2.addAll(kejie);
		xys3.addAll(kejie);
		isSequence(kejie, 2);
		isSequence(xys3, 3);
		xys.removeAll(kejie);
		xys2.removeAll(xys3);
		if (xys2 != null && xys2.size() != 0) {
			for (int i = 0; i < xys2.size(); i++) {
				Map<String,String> retMap = new HashMap<String, String>();
				retMap.put("fourJxbName",jxbName);
				if (i == 0) {
					if (xys2.get(i) != 0) {
						retMap.put("four",k + (xys2.get(i) - 1));
						//four.add(k + (xys2.get(i) - 1));
					}
				} else if (i % 3 == 0) {
					if (xys2.get(i) != 0) {
						retMap.put("four",k + (xys2.get(i) - 1));
						//four.add(k + (xys2.get(i) - 1));
					}
				} else if (i % 3 == 2) {
					if (xys2.get(i) != (count - 1)) {
						retMap.put("four",k + (xys2.get(i) + 1));
						//four.add(k + (xys2.get(i) + 1));
					}
				}
				fourList.add(retMap);
			}
		}
		if (xys != null && xys.size() != 0) {
			for (int i = 0; i < xys.size(); i++) {
				Map<String,String> retMap = new HashMap<String, String>();
				retMap.put("threeJxbName",jxbName);
				if (i % 2 != 0) {
					if (xys.get(i) != 0 && xys.get(i) != (count - 1) && !xys2.contains(xys.get(i))) {
						boolean flag = true;
						for (Map<String,String> map : fourList) {
							if((k + (xys.get(i) - 1)).equals(map.get("four"))){
								flag = false;
							}
						}
						if(flag){
							retMap.put("three",k + (xys.get(i) - 2));
						}

						//three.add(k + (xys.get(i) - 2));
					}
				} else {
					if (xys.get(i) != (count - 1) && !xys2.contains(xys.get(i))) {
						boolean flag = true;
						for (Map<String,String> map : fourList) {
							if((k + xys.get(i) + 1).equals(map.get("four"))){
								flag = false;
							}
						}
						if(flag){
							retMap.put("three",k + (xys.get(i) + 2));
						}

						//three.add(k + (xys.get(i) + 2));
					}
				}
				threeList.add(retMap);
			}
		}
	}


	/**
	 * @param pokers
	 * @param max
	 * @return
	 */
	private List<Integer> isSequence(List<Integer> pokers, int max) {
		Collections.sort(pokers); // 先将集合中的元素排序
		if (!CollectionUtils.isEmpty(pokers) && pokers.size() >= max) {
			for (int index = 0; index < pokers.size(); index++) {
				List<Integer> seq = new ArrayList<Integer>(max);
				int currValue = pokers.get(index);
				for (int j = currValue; j < currValue + max; j++) {
					if (pokers.contains(j)) { //  判断当前元素的下一个是否在list集合里面如果存在将 为true存在map里
						seq.add(j); // 将连续的存在seq中
					}
				}
				if (seq.size() == max) {
					for (Integer num : seq) {
						pokers.remove(pokers.indexOf(num)); // 根据元素的索引位置删除元素 ， 只想删除一个元素seq对应的一个元素，并且不删除重复元素的时候，

// 我使用的是poker.remove(int index) 根据索引删除（

//                        注释：1，不可以直接使用pokers.remove(Object o) 这个方法，会发生数组越界，
//
//                        2，也不可以使用removeAll(Collenction<T> c)
//
//                        举例:List<Integer> a = new ArrayList<>(Array.asList(new Integer(1,2,3,4,5,6,7,1,1,1)));
//
//                        List<Integer> b = new ArrayList<>(Array.asList(new Integer(1)));
//
//                        用a.removeAll(b),就会把集合a里面的所有的元素1，都会删除。

					}
				}
			}
		}
		return pokers;
	}

	/**
	 * @param classId
	 * @param xqid
	 * @param gradeId
	 * @param schoolId
	 * @return
	 */
	public List<Map<String, Object>> GetClassSettledPositions(ObjectId classId, ObjectId xqid, ObjectId gradeId, ObjectId schoolId) {
//        List<N33_YKBEntry> ykbEntryList1 = ykbDao.getYKBsByclassRoomIds(xqid, schoolId, gradeId);
		ObjectId cid = xqid;
		//查行政班学生
		List<ObjectId> studentIds = getStudentList(classId, cid);

		//查询老师Map
		Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(xqid,schoolId);
		//查年级学科
		Map<String, N33_KSDTO> stringN33_ksdtoMap = subjectService.getIsolateSubjectByGradeIdMap(cid, schoolId, gradeId.toHexString());
		//查学生教学班列表
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, cid,1);
		//jxb add
		Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getJXBMapByCiIdAndStuIds(cid, studentIds, acClassType);
		List<ObjectId> jiaoXueBanList = getJiaoXueBanList(jxbEntries);
		//查询教学班所在的所有周课表
		List<N33_YKBEntry> ykbEntries = ykbDao.getN33_YKBByWeek(jiaoXueBanList, xqid);
		//判定走班课和非走班课程
		//返回数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (N33_YKBEntry entry : ykbEntries) {
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
						//单双周
						if (entry.getNTeacherId() == null) {
							if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
								jList += ("," + jxbEntries.get(entry.getJxbId()).getNickName());
							} else {
								jList += ("," + jxbEntries.get(entry.getJxbId()).getName());
							}
							lt.put("jList", jList);
							bf = false;
							break;
						} else {
							if (StringUtils.isNotEmpty(jxbEntries.get(entry.getNJxbId()).getNickName())) {
								jList += ("," + jxbEntries.get(entry.getNJxbId()).getNickName());
							} else {
								jList += ("," + jxbEntries.get(entry.getNJxbId()).getName());
							}
							if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
								jList += ("," + jxbEntries.get(entry.getJxbId()).getNickName());
							} else {
								jList += ("," + jxbEntries.get(entry.getJxbId()).getName());
							}
							lt.put("jList", jList);
							bf = false;
							break;

						}
					}
				}
				if (bf) {
					if (entry.getNJxbId() != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("x", entry.getX() + 1);
						map.put("y", entry.getY() + 1);
						if (stringN33_ksdtoMap.get(entry.getNSubjectId().toString()).getIsZouBan() == 1) {
							map.put("type", 2);
						} else {
							map.put("type", 1);
						}
						if (StringUtils.isNotEmpty(jxbEntries.get(entry.getNJxbId()).getNickName())) {
							map.put("jList", jxbEntries.get(entry.getNJxbId()).getNickName());
							//map.put("teaName",teaEntryMap.get(entry.))
						} else {
							map.put("jList", jxbEntries.get(entry.getNJxbId()).getName());
						}
						map.put("count", jxbEntries.get(entry.getNJxbId()).getStudentIds().size());
						if (stringN33_ksdtoMap.get(entry.getSubjectId().toString()).getIsZouBan() == 1) {
							map.put("type", 2);
						} else {
							map.put("type", 1);
						}
						if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
							map.put("jList", jxbEntries.get(entry.getJxbId()).getNickName());
						} else {
							map.put("jList", jxbEntries.get(entry.getJxbId()).getName());
						}
						if(entry.getTeacherId() != null){
							if(StringUtils.isNotEmpty(teaEntryMap.get(entry.getTeacherId()).getUserName())){
								map.put("teaName",teaEntryMap.get(entry.getTeacherId()).getUserName());
							}
						}

						map.put("count", jxbEntries.get(entry.getJxbId()).getStudentIds().size());
						list.add(map);
					} else {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("x", entry.getX() + 1);
						map.put("y", entry.getY() + 1);
						if (stringN33_ksdtoMap.get(entry.getSubjectId().toString()).getIsZouBan() == 1) {
							map.put("type", 2);
						} else {
							map.put("type", 1);
						}
						if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
							map.put("jList", jxbEntries.get(entry.getJxbId()).getNickName());
						} else {
							map.put("jList", jxbEntries.get(entry.getJxbId()).getName());
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
				String subName = stringN33_ksdtoMap.get(entry.getSubjectId().toString()).getSnm();
				//单双周
				if (entry.getNJxbId() != null) {
					if (stringN33_ksdtoMap.get(entry.getNSubjectId().toString()).getIsZouBan() == 1) {
						map.put("type", 2);
					} else {
						map.put("type", 1);
					}
					if (StringUtils.isNotEmpty(jxbEntries.get(entry.getNJxbId()).getNickName())) {
						map.put("jList", jxbEntries.get(entry.getNJxbId()).getNickName());
					} else {
						map.put("jList", jxbEntries.get(entry.getNJxbId()).getName());
					}
					if (stringN33_ksdtoMap.get(entry.getSubjectId().toString()).getIsZouBan() == 1) {
						map.put("type", 2);
					} else {
						map.put("type", 1);
					}
					if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
						map.put("jList", jxbEntries.get(entry.getJxbId()).getNickName());
					} else {
						map.put("jList", jxbEntries.get(entry.getJxbId()).getName());
					}
					if(entry.getTeacherId() != null && entry.getType() != 4){
						if(StringUtils.isNotEmpty(teaEntryMap.get(entry.getTeacherId()).getUserName())){
							map.put("teaName",teaEntryMap.get(entry.getTeacherId()).getUserName());
						}
					}
				} else {
					if (stringN33_ksdtoMap.get(entry.getSubjectId().toString()).getIsZouBan() == 1) {
						map.put("type", 2);
					} else {
						map.put("type", 1);
					}
					if (StringUtils.isNotEmpty(jxbEntries.get(entry.getJxbId()).getNickName())) {
						map.put("jList", jxbEntries.get(entry.getJxbId()).getNickName());
					} else {
						map.put("jList", jxbEntries.get(entry.getJxbId()).getName() + ",");
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

	/**
	 * 查询教学班根据学生
	 *
	 * @param jxbEntries
	 * @return
	 */
	public List<ObjectId> getJiaoXueBanList(Map<ObjectId, N33_JXBEntry> jxbEntries) {

		List<ObjectId> jiaoxueList = new ArrayList<ObjectId>();
		for (N33_JXBEntry studentEntry : jxbEntries.values()) {
			jiaoxueList.add(studentEntry.getID());
		}
		return jiaoxueList;
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
	 * @param gradeId
	 * @param termId
	 * @param ci
	 * @param schoolId
	 * @return
	 */
	public List<N33_FaBuLogDTO> getFaBuLogs(String gradeId, String termId, String ci, ObjectId schoolId) {
		List<N33_FaBuLogDTO> faBuLogDTOs = new ArrayList<N33_FaBuLogDTO>();
		List<N33_FaBuLogEntry> faBuLogEntries = faBuLogDao.getFaBuLogs(new ObjectId(termId), schoolId, new ObjectId(gradeId), new ObjectId(ci));
		if (faBuLogEntries != null && faBuLogEntries.size() != 0) {
			for (N33_FaBuLogEntry entry : faBuLogEntries) {
				faBuLogDTOs.add(new N33_FaBuLogDTO(entry));
			}
		}
		return faBuLogDTOs;
	}

	/**
	 * 新增调课申请
	 */
	public void addOrUpdate(N33_TiaoKeShengQingDTO dto) {
		N33_TiaoKeShengQingEntry tiaoKeShengQingEntry = dto.BuildEntry();
		tiaoKeShengQingEntry = tiaoKeShengQingDao.getEntry(tiaoKeShengQingEntry.getID());
		N33_JXBEntry entry = jxbDao.getJXBById(new ObjectId(dto.getJxbId()));
		N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(entry.getClassroomId(), dto.getWeek(), dto.getX(), dto.getY());
		N33_ZKBEntry zkbEntry1 = zkbDao.getN33_ZKBById(entry.getClassroomId(), dto.getWeek(), dto.getNx(), dto.getNy());
		if (tiaoKeShengQingEntry != null) {
			tiaoKeShengQingDao.updateEntry(dto.BuildEntry());
		} else {
			tiaoKeShengQingEntry = dto.BuildEntry();
			tiaoKeShengQingEntry.setID(new ObjectId());
			N33_TkLogEntry tkLogEntry = new N33_TkLogEntry(
					zkbEntry.getID(),
					zkbEntry1.getID(),
					zkbEntry.getSchoolId(),
					zkbEntry.getGradeId(),
					zkbEntry.getTermId(),
					new ObjectId(dto.getTeaId()),
					zkbEntry.getSubjectId(),
					zkbEntry1.getNSubjectId(),
					zkbEntry.getTeacherId(),
					zkbEntry1.getTeacherId(),
					0,
					dto.getWeek().toString(),
					0,
					0,zkbEntry.getX()+","+zkbEntry.getY(),zkbEntry1.getX()+","+zkbEntry1.getY());
			tkLogEntry.setSqId(tiaoKeShengQingEntry.getID());
			tiaoKeShengQingDao.addEntry(tiaoKeShengQingEntry);
			tkLogDao.addN33_TkLogEntry(tkLogEntry);
		}
	}


	public List<N33_TiaoKeShengQingDTO> getDtoList(ObjectId xqid, ObjectId teaId, Integer week) {
		List<N33_TiaoKeShengQingEntry> teaList = tiaoKeShengQingDao.getEntryList(xqid, teaId, week);
		List<N33_TiaoKeShengQingDTO> dtos = new ArrayList<N33_TiaoKeShengQingDTO>();
		for (N33_TiaoKeShengQingEntry entry : teaList) {
			N33_TiaoKeShengQingDTO dto = new N33_TiaoKeShengQingDTO(entry);
			dto.setX(dto.getX() + 1);
			dto.setY(dto.getY() + 1);
			dto.setNy(dto.getNy() + 1);
			dto.setNx(dto.getNx() + 1);
			dtos.add(dto);
		}
		return dtos;
	}

	public List<N33_TiaoKeShengQingDTO> getShengQingListALL(ObjectId xqid, ObjectId teaId) {
		List<N33_TiaoKeShengQingEntry> teaList = tiaoKeShengQingDao.getEntryList(xqid, teaId);
		List<N33_TiaoKeShengQingDTO> dtos = new ArrayList<N33_TiaoKeShengQingDTO>();
		List<ObjectId> jxbId = new ArrayList<ObjectId>();
		for (N33_TiaoKeShengQingEntry entry : teaList) {
			jxbId.add(entry.getJxbId());
			jxbId.add(entry.getNJxbId());
		}
		Map<ObjectId, N33_JXBEntry> map = jxbDao.getJXBMapsByIds(jxbId);
		for (N33_TiaoKeShengQingEntry entry : teaList) {
			N33_TiaoKeShengQingDTO dto = new N33_TiaoKeShengQingDTO(entry);
			dto.setX(dto.getX() + 1);
			dto.setY(dto.getY() + 1);
			dto.setNy(dto.getNy() + 1);
			dto.setNx(dto.getNx() + 1);
			N33_JXBEntry jxbEntry = map.get(entry.getJxbId());
			N33_JXBEntry NjxbEntry = map.get(entry.getNJxbId());
			if (dto.getSta() == 1) {
				dto.setSt("已申请");
			}
			if (dto.getSta() == 2) {
				dto.setSt("已调课");
			}
			if (dto.getSta() == 3) {
				dto.setSt("已拒绝");
			}
			dto.setTeaName("");
			dto.setJxbName(jxbEntry.getName());
			if (NjxbEntry != null) {
				dto.setTeaName(teaDao.findIsolateN33_TeaEntryById(NjxbEntry.getTercherId(), NjxbEntry.getTermId()).getUserName());
				dto.setNjxbName(NjxbEntry.getName());
			} else {
				dto.setNjxbName("");
			}
			dtos.add(dto);
		}
		return dtos;
	}

	public void updateSta(ObjectId id, Integer sta) {
		tiaoKeShengQingDao.updateEntry(id, sta);
	}

	public void delShengQing(ObjectId id) {
		tiaoKeShengQingDao.remove(id);
		tkLogDao.remove(id);
	}

	/**
	 * 自动排走班课
	 * @param schoolId
	 * @param gradeId
	 * @param xqId
	 * @param ciId
	 */
	public void autoPaiKeZouBan(ObjectId schoolId, ObjectId gradeId, ObjectId xqId, ObjectId ciId){
		//课表矩阵
		List<IdNameValuePairDTO> weekDays = paiKeZuHeService.getGradeWeek(gradeId, ciId, schoolId);
		List<CourseRangeDTO> courseRanges = courseRangeService.getListBySchoolId(schoolId.toString(), ciId);
		List<Integer> xList = new ArrayList<Integer>();
		Map<Integer, List<Integer>> xyListMap = new HashMap<Integer, List<Integer>>();
		for(int x = 0; x<weekDays.size(); x++){
			xList.add(x);
			List<Integer> yList = xyListMap.get(x);
			if(null==yList){
				yList = new ArrayList<Integer>();
			}
			for(int y = 0; y<courseRanges.size(); y++){
				yList.add(y);
			}
			xyListMap.put(x, yList);
		}

		//获取非走班教学班
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getZBJXBList(schoolId, gradeId, ciId, acClassType);

		//教学班ids
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		//清除历史数据
		List<ObjectId> clearJxbIds = new ArrayList<ObjectId>();

		List<Integer> types = new ArrayList<Integer>();

		List<Integer> serials = new ArrayList<Integer>();

		Map<Integer, Map<Integer, Map<String, Object>>> typeSerialMap = new HashMap<Integer, Map<Integer, Map<String, Object>>>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			if(!jxbIds.contains(jxbEntry.getID())) {
				jxbIds.add(jxbEntry.getID());
			}
			if(null!=jxbEntry.getID()){
				clearJxbIds.add(jxbEntry.getID());
			}
			if(null!=jxbEntry.getRelativeId()){
				clearJxbIds.add(jxbEntry.getRelativeId());
			}

			int jxbSerial = jxbEntry.getTimeCombSerial();
			int jxbType = jxbEntry.getType();

			if(!serials.contains(jxbSerial)){
				serials.add(jxbSerial);
			}
			if(!types.contains(jxbType)){
				types.add(jxbType);
			}
			Map<Integer, Map<String, Object>> serialMap = typeSerialMap.get(jxbType);
			if(null==serialMap){
				serialMap = new HashMap<Integer, Map<String, Object>>();
			}

			Map<String, Object> serialObj = serialMap.get(jxbSerial);
			if(null==serialObj){
				serialObj = new HashMap<String, Object>();
			}
			serialObj.put("type", jxbType);
			serialObj.put("serial", jxbSerial);
			String zuHeName = "";
			if(jxbType == 1){
				zuHeName = "等级-时段组合" + jxbSerial;
			}else{
				zuHeName = "合格-时段组合" + jxbSerial;
			}
			serialObj.put("name", zuHeName);
			Map<ObjectId, Integer> serialJxbKSMap = serialObj.get("jxbKSMap")==null?new HashMap<ObjectId, Integer>():(Map<ObjectId, Integer>)serialObj.get("jxbKSMap");
			int maxKeShi = serialObj.get("maxKeShi")==null?0:(Integer) serialObj.get("maxKeShi");
			int jxbKeShi = jxbEntry.getJXBKS();
			serialJxbKSMap.put(jxbEntry.getID(), jxbKeShi);
			serialObj.put("jxbKSMap", serialJxbKSMap);

			if(jxbKeShi>maxKeShi){
				serialObj.put("maxKeShi", jxbKeShi);
			}
			serialMap.put(jxbSerial, serialObj);

			typeSerialMap.put(jxbType, serialMap);
		}

		Map<Integer, List<Map<String, Object>>> typeTimeCombMap = new HashMap<Integer, List<Map<String, Object>>>();
		for(Integer type:types){
			List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
			Map<Integer, Map<String, Object>> serialMap = typeSerialMap.get(type);
			for(Integer serial:serials){
				Map<String, Object> serialObj = serialMap.get(serial);
				if(null!=serialObj){
					reList.add(serialObj);
				}
			}
			typeTimeCombMap.put(type, reList);
		}

		jxbksDao.clearJxbKsByJxbIds(clearJxbIds, ciId);
		ykbDao.clearYuanKeBiaoKeShi(ciId, schoolId, gradeId, types);

		//教学班map
		Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);

		//源课表集合
		List<N33_YKBEntry> ykbList = ykbDao.getYKBEntrysList(ciId, schoolId);
		//专项map
		Map<ObjectId, List<N33_ZhuanXiangEntry>> zxMap = zhuanXiangDao.getZhuanXiangMap(ciId);

		List<N33_JXBCTEntry> jxbctEntries = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, ciId);

		//教学班冲突格
		Map<ObjectId, Map<Integer, List<Integer>>> jxbXyNotUseMap = getJxbCtXysMMap(schoolId, gradeId, xqId, ciId, jxbEntries, jxbMap, zxMap, ykbList, jxbctEntries, "zouBan");

		Map<String, Map<ObjectId, N33_YKBEntry>> xyClsRoomMMap = new HashMap<String, Map<ObjectId, N33_YKBEntry>>();
		for(N33_YKBEntry ykbEntry:ykbList){
			String key = ykbEntry.getX()+","+ykbEntry.getY();
			Map<ObjectId, N33_YKBEntry> clsRoomMMap = xyClsRoomMMap.get(key);
			if(null==clsRoomMMap){
				clsRoomMMap = new HashMap<ObjectId, N33_YKBEntry>();
			}
			clsRoomMMap.put(ykbEntry.getClassroomId(), ykbEntry);
			xyClsRoomMMap.put(key, clsRoomMMap);
		}

		List<N33_YKBEntry> ykbUpdList = new ArrayList<N33_YKBEntry>();

		List<N33_TimeCombKeBiaoEntry> addEntries = new ArrayList<N33_TimeCombKeBiaoEntry>();

		Map<Integer, List<Integer>> usedXyListMap = new HashMap<Integer, List<Integer>>();

		Collections.sort(types);
		for(Integer type:types){
			List<Map<String, Object>> timeCombs = typeTimeCombMap.get(type);
			if(null==timeCombs){
				continue;
			}

			Map<Integer, Map<String, Object>> serialTimeCombMap = new HashMap<Integer, Map<String, Object>>();
			Map<Integer, Integer> serialNotUseCountMap = new HashMap<Integer, Integer>();
			Map<Integer, Map<Integer, List<Integer>>> serialXyNotUseMap = new HashMap<Integer, Map<Integer, List<Integer>>>();
			int allTimeCombMaxKeShi = 0;
			for(Map<String, Object> timeComb:timeCombs){
				int serial = (Integer)timeComb.get("serial");
				int serialNotUseCount = serialNotUseCountMap.get(serial)==null?0:serialNotUseCountMap.get(serial);
				Map<Integer, List<Integer>> xyNotUseMap = serialXyNotUseMap.get(serial);
				if(null==xyNotUseMap){
					xyNotUseMap = new HashMap<Integer, List<Integer>>();
				}
				Map<ObjectId, Integer> jxbKSMap = timeComb.get("jxbKSMap")==null?new HashMap<ObjectId, Integer>():(Map<ObjectId, Integer>)timeComb.get("jxbKSMap");
				for(ObjectId jxbId:jxbKSMap.keySet()){
					Map<Integer, List<Integer>> serialJxbXyNotUseMap = jxbXyNotUseMap.get(jxbId);
					if(null==serialJxbXyNotUseMap){
						continue;
					}
					for(Map.Entry<Integer, List<Integer>> serialJxbXyNotUse:serialJxbXyNotUseMap.entrySet()){
						List<Integer> yNots = xyNotUseMap.get(serialJxbXyNotUse.getKey());
						if(null==yNots){
							yNots = new ArrayList<Integer>();
						}
						for(Integer notUseY:serialJxbXyNotUse.getValue()){
							if(!yNots.contains(notUseY)){
								serialNotUseCount++;
								yNots.add(notUseY);
							}
						}
						xyNotUseMap.put(serialJxbXyNotUse.getKey(), yNots);
					}

				}
				serialTimeCombMap.put(serial, timeComb);
				serialXyNotUseMap.put(serial, xyNotUseMap);
				serialNotUseCountMap.put(serial, serialNotUseCount);

				int maxKeShi = timeComb.get("maxKeShi")==null?0:(Integer)timeComb.get("maxKeShi");
				if(maxKeShi>allTimeCombMaxKeShi){
					allTimeCombMaxKeShi = maxKeShi;
				}
			}

			List<Map<String, Integer>> serialNotUseCountList = new ArrayList<Map<String, Integer>>();

			for(Map.Entry<Integer, Integer> entry:serialNotUseCountMap.entrySet()){
				Map<String, Integer> serialMap = new HashMap<String, Integer>();
				serialMap.put("serial", entry.getKey());
				serialMap.put("count", entry.getValue());
				serialNotUseCountList.add(serialMap);
			}
			serialNotUseCountListSort(serialNotUseCountList);


			//随机排序
			//Collections.shuffle(serialNotUseCountList);
			Map<Integer, Integer> serialFinishCountMap = new HashMap<Integer, Integer>();

			int index = 0;

			boolean changeCol = false;
			int loopFinishCount = 0;
			int cursor = 0;
			int initMaxKeShi = allTimeCombMaxKeShi;
			for(int loop = 0; loop<allTimeCombMaxKeShi; loop++){
				boolean serialSuccessState = true;
				Map<Integer, Integer> serialTempFinishCountMap = new HashMap<Integer, Integer>(serialFinishCountMap);
				List<N33_TimeCombKeBiaoEntry> serialFininshEntries = new ArrayList<N33_TimeCombKeBiaoEntry>();
				List<N33_YKBEntry> serialYkbUpdList = new ArrayList<N33_YKBEntry>();
				Map<Integer, List<Integer>> usedTempXyListMap = new HashMap<Integer, List<Integer>>(usedXyListMap);
				for(Map<String, Integer> serialMap:serialNotUseCountList){
					int serial = serialMap.get("serial");
					Map<String, Object> timeComb = serialTimeCombMap.get(serial);
					if(null==timeComb){
						continue;
					}
					String serialName = timeComb.get("name")==null?"":(String)timeComb.get("name");
					int maxKeShi = timeComb.get("maxKeShi")==null?0:(Integer)timeComb.get("maxKeShi");

					Map<ObjectId, Integer> jxbKSMap = timeComb.get("jxbKSMap")==null?new HashMap<ObjectId, Integer>():(Map<ObjectId, Integer>)timeComb.get("jxbKSMap");

					int serialFinishCount = serialTempFinishCountMap.get(serial)==null?0:serialTempFinishCountMap.get(serial);
					if(serialFinishCount>=maxKeShi){
						continue;
					}
					Map<Integer, List<Integer>> xyNotUseMap = serialXyNotUseMap.get(serial);
					for(int i=0;i<xList.size();i++){
						int x = xList.get(index);
						List<Integer> yList = xyListMap.get(x);
						List<Integer> usedYList = usedTempXyListMap.get(x)==null?new ArrayList<Integer>():usedTempXyListMap.get(x);
						List<Integer> yNotUse = new ArrayList<Integer>();
						if(null!=xyNotUseMap){
							if(null!=xyNotUseMap.get(x)){
								yNotUse = xyNotUseMap.get(x);
							}
						}
						int keYongY = -1;
						for(Integer y:yList){
							int xyValue = x*10+y;
							if(!yNotUse.contains(y)&&!usedYList.contains(y)&&(xyValue>cursor||cursor==0)){
								keYongY = y;
								cursor = xyValue;
								break;
							}
						}
						if(-1==keYongY){
							if(i==xList.size()-1){
								serialSuccessState = false;
							}
							index++;
							if(index>xList.size()-1){
								changeCol = false;
								break;
							}
							loopFinishCount = 0;
							cursor = 0;
							changeCol = true;
							if(2*initMaxKeShi>allTimeCombMaxKeShi) {
								allTimeCombMaxKeShi++;
							}
							continue;
						}

						N33_TimeCombKeBiaoEntry addEntry = new N33_TimeCombKeBiaoEntry();
						addEntry.setSchoolId(schoolId);
						addEntry.setGradeId(gradeId);
						addEntry.setCiId(ciId);
						addEntry.setType(type);
						addEntry.setSerialName(serialName);
						addEntry.setSerial(serial);
						addEntry.setX(x);
						addEntry.setY(keYongY);
						serialFininshEntries.add(addEntry);

						String key = x+","+keYongY;
						Map<ObjectId, N33_YKBEntry> clsRoomMMap = xyClsRoomMMap.get(key);
						if(null!=clsRoomMMap) {
							for(Map.Entry<ObjectId, Integer> jxbKS:jxbKSMap.entrySet()){
								int jxbKeShi = jxbKS.getValue();
								if(jxbKeShi>serialFinishCount) {
									N33_JXBEntry jxbEntry = jxbMap.get(jxbKS.getKey());
									if(null!=jxbEntry){
										ObjectId classRoomId = jxbEntry.getClassroomId();
										N33_YKBEntry ykbEntry = clsRoomMMap.get(classRoomId);
										if(null!=ykbEntry) {
											ykbEntry.setJxbId(jxbEntry.getID());
											ykbEntry.setIsUse(Constant.ONE);
											ykbEntry.setGradeId(jxbEntry.getGradeId());
											ykbEntry.setSubjectId(jxbEntry.getSubjectId());
											ykbEntry.setTeacherId(jxbEntry.getTercherId());
											ykbEntry.setType(jxbEntry.getType());
											ykbEntry.setTimeCombSerial(serial);
											serialYkbUpdList.add(ykbEntry);
										}
									}
								}
							}
						}
						usedYList.add(keYongY);
						usedTempXyListMap.put(x, usedYList);
						loopFinishCount++;
						break;
					}
					if(!serialSuccessState){
						changeCol=false;
						break;
					}
					serialFinishCount++;
					serialTempFinishCountMap.put(serial, serialFinishCount);
					if(changeCol){
						if(loopFinishCount==serialNotUseCountList.size()){
							changeCol=false;
							loopFinishCount = 0;
							cursor = 0;
							break;
						}
					}
				}
				if(serialSuccessState){
					serialFinishCountMap= new HashMap<Integer, Integer>(serialTempFinishCountMap);
					usedXyListMap = new HashMap<Integer, List<Integer>>(usedTempXyListMap);
					addEntries.addAll(serialFininshEntries);
					ykbUpdList.addAll(serialYkbUpdList);
				}
				if(!changeCol){
					index++;
					if (index > xList.size() - 1) {
						index = 0;
						loopFinishCount = 0;
						cursor = 0;
					}
				}
			}
		}

		if(addEntries.size()>0){
			List<ObjectId> finishJxbIds = MongoUtils.getFieldObjectIDs(ykbUpdList, "jxbId");
			List<ObjectId> addKSJxbIds = new ArrayList<ObjectId>();
			List<ObjectId> updKSJxbIds = new ArrayList<ObjectId>();
			Map<ObjectId, N33_JXBKSEntry> jxbksMap = jxbksDao.getJXBKSMap(ciId, finishJxbIds);
			for(N33_YKBEntry ykbEntry:ykbUpdList){
				if(null==ykbEntry){
					continue;
				}
				N33_JXBEntry jxbEntry = jxbMap.get(ykbEntry.getJxbId());
				if(null==jxbEntry) {
					continue;
				}
				int jxbKeShi = jxbEntry.getJXBKS();
				N33_JXBKSEntry jxbksEntry = jxbksMap.get(jxbEntry.getID());
				int ypCount = 0;
				if(null!=jxbksEntry){
					ypCount = jxbksEntry.getYpCount();
				}
				if ((jxbKeShi!=0&&ypCount >= jxbKeShi)||jxbKeShi==0) {
					continue;
				}
				if (jxbksEntry == null) {
					jxbksEntry = new N33_JXBKSEntry(jxbEntry.getID(), jxbEntry.getSubjectId(), 1, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang());
					jxbksEntry.setID(new ObjectId());
					jxbksMap.put(jxbEntry.getID(), jxbksEntry);
					if(!addKSJxbIds.contains(jxbEntry.getID())) {
						addKSJxbIds.add(jxbEntry.getID());
					}
				} else {
					jxbksEntry.setYpCount(ypCount+1);
					jxbksMap.put(jxbEntry.getID(), jxbksEntry);
					if(!addKSJxbIds.contains(jxbEntry.getID())){
						if(!updKSJxbIds.contains(jxbEntry.getID())) {
							updKSJxbIds.add(jxbEntry.getID());
						}
					}
				}
				ykbDao.updateN33_YKB(ykbEntry);
			}
			if(addKSJxbIds.size()>0){
				List<N33_JXBKSEntry> addJXBKSList = new ArrayList<N33_JXBKSEntry>();
				for(ObjectId addId:addKSJxbIds){
					N33_JXBKSEntry jxbksEntry = jxbksMap.get(addId);
					if(null!=jxbksEntry){
						addJXBKSList.add(jxbksEntry);
					}
				}
				if(addJXBKSList.size()>0) {
					jxbksDao.addN33_JXBKSEntries(addJXBKSList);
				}
			}
			if(updKSJxbIds.size()>0){
				for(ObjectId updId:updKSJxbIds){
					N33_JXBKSEntry jxbksEntry = jxbksMap.get(updId);
					if(null!=jxbksEntry){
						jxbksDao.updateN33_JXBKS(jxbksEntry);
					}
				}
			}
		}
	}


	/**
	 * 根据时段组合不能排课坐标统计排序 高->低
	 * @param serialNotUseCountList
	 */
	public void serialNotUseCountListSort(List<Map<String, Integer>> serialNotUseCountList) {
		Collections.sort(serialNotUseCountList, new Comparator<Map<String, Integer>>() {
			@Override
			public int compare(Map<String, Integer> arg0, Map<String, Integer> arg1) {
				int count0 = arg0.get("count")==null?0:(Integer) arg0.get("count");
				int count1 = arg1.get("count")==null?0:(Integer)arg1.get("count");
				if(count1==count0){
					//随机取得一列分时段的索引
					Random random = new Random();
					int flag = random.nextInt(2);
					if(flag==1){
						return -1;
					}else{
						return 0;
					}
				}else {
					return count1 - count0;
				}
			}
		});
	}

	/**
	 * 自动排非走班课
	 *
	 * @param schoolId
	 * @param gradeId
	 * @param xqId
	 * @param ciId
	 */
    public void autoPaiKeFeiZouBan(ObjectId schoolId, ObjectId gradeId, ObjectId xqId, ObjectId ciId) {

		//getPaiKeRules(schoolId, gradeId, ciId);

		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);

        int delHisYKB = 1;
        //非走班类型
        List<Integer> fzbTypes = new ArrayList<Integer>();
        fzbTypes.add(3);
        fzbTypes.add(4);
        //fzbTypes.add(5);
        fzbTypes.add(6);
        //获取非走班教学班
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(schoolId, gradeId, ciId, fzbTypes, acClassType);

        //清除历史数据
        if(delHisYKB==1){
            List<ObjectId> clearJxbIds = new ArrayList<ObjectId>();
            for (N33_JXBEntry jxbEntry : jxbEntries) {
                if(null!=jxbEntry.getID()){
                    clearJxbIds.add(jxbEntry.getID());
                }
                if(null!=jxbEntry.getRelativeId()){
                    clearJxbIds.add(jxbEntry.getRelativeId());
                }
            }
            jxbksDao.clearJxbKsByJxbIds(clearJxbIds, ciId);
            ykbDao.clearYuanKeBiaoKeShi(ciId, schoolId, gradeId, fzbTypes);
        }

		//jxb add
		if(acClassType==1){
			autoPaiKeZouBan(schoolId, gradeId, xqId, ciId);
		}

		//专项map
		Map<ObjectId, List<N33_ZhuanXiangEntry>> zxMap = zhuanXiangDao.getZhuanXiangMap(ciId);

		//教师->教学班集合
		Map<ObjectId, List<ObjectId>> teaJxbIdsMap = new HashMap<ObjectId, List<ObjectId>>();

		//没有设置老师的教学班
		List<ObjectId> noTeaJxbIds = new ArrayList<ObjectId>();

		//教学班ids
		List<ObjectId> jxbIds = new ArrayList<ObjectId>();

		//专项教学班->教师集合
		Map<ObjectId, List<ObjectId>> zxJxbTeaIdsMap = new HashMap<ObjectId, List<ObjectId>>();
		for(N33_JXBEntry jxbEntry:jxbEntries){
			if(!jxbIds.contains(jxbEntry.getID())) {
				jxbIds.add(jxbEntry.getID());
			}
			if (null!=jxbEntry.getRelativeId()) {
				if(!jxbIds.contains(jxbEntry.getRelativeId())) {
					jxbIds.add(jxbEntry.getRelativeId());
				}
			}
			if (jxbEntry.getType() == 4) {
				List<ObjectId> teaIds = new ArrayList<ObjectId>();
				List<N33_ZhuanXiangEntry> zxList = zxMap.get(jxbEntry.getID());
				if (zxList != null) {
					for(N33_ZhuanXiangEntry zxEntry:zxList){
						ObjectId teaId = zxEntry.getTeaId();
						if(null!=teaId) {
							if(!teaIds.contains(teaId)) {
								teaIds.add(teaId);
							}
						}
					}
				}
				zxJxbTeaIdsMap.put(jxbEntry.getID(), teaIds);
				if(teaIds.size()>0){
					for(ObjectId teaId:teaIds) {
						List<ObjectId> teaJxbIds = teaJxbIdsMap.get(teaId);
						if (null == teaJxbIds) {
							teaJxbIds = new ArrayList<ObjectId>();
						}
						if (!teaJxbIds.contains(jxbEntry.getID())) {
							teaJxbIds.add(jxbEntry.getID());
						}
						if (null != jxbEntry.getRelativeId()) {
							if (!teaJxbIds.contains(jxbEntry.getRelativeId())) {
								teaJxbIds.add(jxbEntry.getRelativeId());
							}
						}
						teaJxbIdsMap.put(teaId, teaJxbIds);
					}
				}else{
					if (!noTeaJxbIds.contains(jxbEntry.getID())) {
						noTeaJxbIds.add(jxbEntry.getID());
					}
					if (null != jxbEntry.getRelativeId()) {
						if (!noTeaJxbIds.contains(jxbEntry.getRelativeId())) {
							noTeaJxbIds.add(jxbEntry.getRelativeId());
						}
					}
				}
			}else {
				if (null != jxbEntry.getTercherId()) {
					List<ObjectId> teaJxbIds = teaJxbIdsMap.get(jxbEntry.getTercherId());
					if (null == teaJxbIds) {
						teaJxbIds = new ArrayList<ObjectId>();
					}
					if (!teaJxbIds.contains(jxbEntry.getID())) {
						teaJxbIds.add(jxbEntry.getID());
					}
					if (null != jxbEntry.getRelativeId()) {
						if (!teaJxbIds.contains(jxbEntry.getRelativeId())) {
							teaJxbIds.add(jxbEntry.getRelativeId());
						}
					}
					teaJxbIdsMap.put(jxbEntry.getTercherId(), teaJxbIds);
				} else {
					if (!noTeaJxbIds.contains(jxbEntry.getID())) {
						noTeaJxbIds.add(jxbEntry.getID());
					}
					if (null != jxbEntry.getRelativeId()) {
						if (!noTeaJxbIds.contains(jxbEntry.getRelativeId())) {
							noTeaJxbIds.add(jxbEntry.getRelativeId());
						}
					}
				}
			}
		}

		//教学班课时map
		Map<ObjectId, N33_JXBKSEntry> jxbksMap = jxbksDao.getJXBKSMap(ciId, jxbIds);

		//获取 类型->教学班集合
		Map<Integer, List<ObjectId>> typeJxbsMap = getTypeJxbIdsMap(jxbEntries);

		//获取 教室->上课教学班集合
		Map<ObjectId, List<ObjectId>> rmJxbsMap = getRmJxbsMap(jxbEntries, zxMap);

		//获取 教学班->教室集合
		Map<ObjectId, List<ObjectId>> jxbRmsMap = getJxbRmsMap(jxbEntries, zxMap);

		//非专项课的非走班教学班类型
		List<Integer> types = new ArrayList<Integer>();
		for(Map.Entry<Integer, List<ObjectId>> entry:typeJxbsMap.entrySet()){
			if(entry.getKey()!=4){
				types.add(entry.getKey());
			}
		}

		//教学班教学班之间的冲突
		Map<ObjectId, List<ObjectId>> jxbCTJxbsMap = new HashMap<ObjectId, List<ObjectId>>();
		List<N33_JXBCTEntry> jxbctEntries = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, ciId);
		for (N33_JXBCTEntry jxbctEntry : jxbctEntries) {
			List<ObjectId> jxbCTJxbIds = jxbCTJxbsMap.get(jxbctEntry.getJxbId());
			if(null==jxbCTJxbIds){
				jxbCTJxbIds = new ArrayList<ObjectId>();
			}
			jxbCTJxbIds.add(jxbctEntry.getOjxbId());
			jxbCTJxbsMap.put(jxbctEntry.getJxbId(), jxbCTJxbIds);
		}

		//教学班map
		Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);

		//源课表集合
		List<N33_YKBEntry> ykbList = ykbDao.getYKBEntrysList(ciId, schoolId);

		//教学班冲突格
		Map<ObjectId, Map<Integer, List<Integer>>> jxbCtXysMMap = getJxbCtXysMMap(schoolId, gradeId, xqId, ciId, jxbEntries, jxbMap, zxMap, ykbList, jxbctEntries, "feiZouBan");

		Map<String, Map<ObjectId, N33_YKBEntry>> xyClsRoomMMap = new HashMap<String, Map<ObjectId, N33_YKBEntry>>();

		//教学班在源课表使用的xy坐标
		Map<ObjectId, Map<Integer, List<Integer>>> jxbYkbUseXysMMap = new HashMap<ObjectId, Map<Integer, List<Integer>>>();
		for(N33_YKBEntry ykb:ykbList){
			if(null!=ykb.getJxbId()){
				Map<Integer, List<Integer>> jxbYkbUseXysMap = jxbYkbUseXysMMap.get(ykb.getJxbId());
				if(null==jxbYkbUseXysMap){
					jxbYkbUseXysMap = new HashMap<Integer, List<Integer>>();
				}
				mapAddXys(jxbYkbUseXysMap, ykb.getX(), ykb.getX());
				if(null!=ykb.getNJxbId()){
					Map<Integer, List<Integer>> nJxbYkbUseXysMap = jxbYkbUseXysMMap.get(ykb.getNJxbId());
					if(null!=nJxbYkbUseXysMap){
						heBingMapToTagMap(nJxbYkbUseXysMap, jxbYkbUseXysMap);
					}
					jxbYkbUseXysMMap.put(ykb.getNJxbId(), jxbYkbUseXysMap);
				}
				jxbYkbUseXysMMap.put(ykb.getJxbId(), jxbYkbUseXysMap);
			}
			String key = ykb.getX()+","+ykb.getY();
			Map<ObjectId, N33_YKBEntry> clsRoomMMap = xyClsRoomMMap.get(key);
			if(null==clsRoomMMap){
				clsRoomMMap = new HashMap<ObjectId, N33_YKBEntry>();
			}
			clsRoomMMap.put(ykb.getClassroomId(), ykb);
			xyClsRoomMMap.put(key, clsRoomMMap);
		}

		Map<ObjectId, Map<String, Object>> teaCountMMap = new HashMap<ObjectId, Map<String, Object>>();
		for(Map.Entry<ObjectId, List<ObjectId>> teaJxbIdsEntry:teaJxbIdsMap.entrySet()){
			ObjectId teaId = teaJxbIdsEntry.getKey();
			Map<String, Object> teaCountMap = teaCountMMap.get(teaId);
			if(null==teaCountMap){
				teaCountMap = new HashMap<String, Object>();
			}
			List<ObjectId> teaJxbIds = teaJxbIdsEntry.getValue();
			int teaJxbCount = teaJxbIds.size();
			int totalKeShi = 0;
			for(ObjectId jxbId:teaJxbIds){
				N33_JXBEntry jxbEntry = jxbMap.get(jxbId);
				if(null!=jxbEntry) {
					totalKeShi += jxbEntry.getJXBKS();
				}
			}
			teaCountMap.put("teaId", teaId);
			teaCountMap.put("totalKeShi", totalKeShi);
			teaCountMap.put("teaJxbCount", teaJxbCount);
			teaCountMMap.put(teaId, teaCountMap);
		}

		//课表矩阵
		List<IdNameValuePairDTO> weekDays = paiKeZuHeService.getGradeWeek(gradeId, ciId, schoolId);
		List<CourseRangeDTO> courseRanges = courseRangeService.getListBySchoolId(schoolId.toString(), ciId);
		List<Integer> xList = new ArrayList<Integer>();
		Map<Integer, List<Integer>> xyListMap = new HashMap<Integer, List<Integer>>();
		for(int x = 0; x<weekDays.size(); x++){
			xList.add(x);
			List<Integer> yList = xyListMap.get(x);
			if(null==yList){
				yList = new ArrayList<Integer>();
			}
			for(int y = 0; y<courseRanges.size(); y++){
				yList.add(y);
			}
			xyListMap.put(x, yList);
		}

		//教学班教室
		/*List<N33_ClassroomDTO> clsRmList = classroomService.getRoomEntryListByXqGrade(ciId, schoolId, gradeId,1);
        List<String> clsRmIds = new ArrayList<String>();
        Map<String, N33_ClassroomDTO> clsRmMap = new HashMap<String, N33_ClassroomDTO>();
		for(N33_ClassroomDTO clsRm:clsRmList){
			clsRmIds.add(clsRm.getRoomId());
			clsRmMap.put(clsRm.getRoomId(), clsRm);
		}*/

		//优先处理专项课
		List<ObjectId> typeJxbIds = typeJxbsMap.get(4);
		if(null!=typeJxbIds){
			List<Map<String, Object>> zxJxbCountMaps = new ArrayList<Map<String, Object>>();
			for(ObjectId zxJxbId:typeJxbIds){
				List<ObjectId> zxJxbTeaIds = zxJxbTeaIdsMap.get(zxJxbId);
				if(null!=zxJxbTeaIds&&zxJxbTeaIds.size()>0){
					List<Map<String, Object>> teaCountMaps = new ArrayList<Map<String, Object>>();
					for(ObjectId zxJxbTeaId:zxJxbTeaIds){
						if(null!=teaCountMMap.get(zxJxbTeaId)){
							Map<String, Object> teaCountMap = new HashMap<String, Object>(teaCountMMap.get(zxJxbTeaId));
							teaCountMaps.add(teaCountMap);
						}
					}
					if(teaCountMaps.size()>0) {
						listSortOne(teaCountMaps);
						Map<String, Object> zxJxbCountMap = new HashMap<String, Object>(teaCountMaps.get(0));
						zxJxbCountMap.put("jxbId", zxJxbId);
						zxJxbCountMaps.add(zxJxbCountMap);
					}
				}
			}
			if(zxJxbCountMaps.size()>0) {
				listSortOne(zxJxbCountMaps);
				buildPaiKe(xList, xyListMap, zxJxbCountMaps, jxbCtXysMMap, jxbCTJxbsMap, jxbYkbUseXysMMap, jxbMap, jxbksMap, xyClsRoomMMap, jxbRmsMap);
			}
		}

		List<Map<String, Object>> teaCountMaps = new ArrayList<Map<String, Object>>();
		for(ObjectId teaId:teaJxbIdsMap.keySet()){
			if(null!=teaCountMMap.get(teaId)){
				Map<String, Object> teaCountMap = new HashMap<String, Object>(teaCountMMap.get(teaId));
				teaCountMaps.add(teaCountMap);
			}
		}
		listSortOne(teaCountMaps);

		for(Map<String, Object> teaCountMap:teaCountMaps){
			if(null!=teaCountMap.get("teaId")) {
				ObjectId teaId = (ObjectId) teaCountMap.get("teaId");
				List<ObjectId> teaJxbIds = teaJxbIdsMap.get(teaId);
				if(null!=teaJxbIds){
					List<Map<String, Object>> jxbCtCountMaps = new ArrayList<Map<String, Object>>();
					for(ObjectId teaJxbId:teaJxbIds){
						Map<String, Object> jxbCtCountMap = new HashMap<String, Object>();
						int ctXyCount = getJxbCtXyCount(teaJxbId, jxbCtXysMMap, jxbYkbUseXysMMap);
						jxbCtCountMap.put("jxbId", teaJxbId);
						jxbCtCountMap.put("ctXyCount", ctXyCount);
						jxbCtCountMaps.add(jxbCtCountMap);
					}
					if(jxbCtCountMaps.size()>0) {
						listSortTwo(jxbCtCountMaps);
						buildPaiKe(xList, xyListMap, jxbCtCountMaps, jxbCtXysMMap, jxbCTJxbsMap, jxbYkbUseXysMMap, jxbMap, jxbksMap, xyClsRoomMMap, jxbRmsMap);
					}
				}
			}
		}

		List<Map<String, Object>> noTeajxbMaps = new ArrayList<Map<String, Object>>();
		for(ObjectId jxbId:noTeaJxbIds){
			Map<String, Object> jxbCtCountMap = new HashMap<String, Object>();
			int ctXyCount = getJxbCtXyCount(jxbId, jxbCtXysMMap, jxbYkbUseXysMMap);
			jxbCtCountMap.put("jxbId", jxbId);
			jxbCtCountMap.put("ctXyCount", ctXyCount);
			noTeajxbMaps.add(jxbCtCountMap);
		}
		if(noTeajxbMaps.size()>0) {
			listSortTwo(noTeajxbMaps);
			buildPaiKe(xList, xyListMap, noTeajxbMaps, jxbCtXysMMap, jxbCTJxbsMap, jxbYkbUseXysMMap, jxbMap, jxbksMap, xyClsRoomMMap, jxbRmsMap);
		}
    }

	public Map getPaiKeRules(ObjectId schoolId, ObjectId gradeId, ObjectId ciId){

		List<N33_SubjectSetDTO> subSetList= subjectSetService.getSubjectSetDTOByCiIdAndGradeId(schoolId, gradeId, ciId);

		List<N33_ZouBanSetDTO> zbSetList = zouBanSetService.getZouBanSet(gradeId, schoolId, ciId);

		//TODO 添加排课规则2019-7-31
		Map teaSetMap = autoTeaSetService.getAutoTeaSet(schoolId, gradeId, ciId);
		Map otherSetMap = autoTeaSetService.getAutoOtherSet(schoolId, gradeId, ciId);

		String teaWeek = teaSetMap.get("teaWeek").toString();
		String teaDay = teaSetMap.get("teaDay").toString();
		String teaOverClass = teaSetMap.get("teaOverClass").toString();
		String teaStep = teaSetMap.get("teaStep").toString();
		String teaRule = teaSetMap.get("teaRule").toString();
		String teaMutex = teaSetMap.get("teaMutex").toString();

		String subCheck = otherSetMap.get("subCheck").toString();
		String boxcheck = otherSetMap.get("boxcheck").toString();
		String ltCheck = otherSetMap.get("ltCheck").toString();
		String classCheck = otherSetMap.get("classCheck").toString();
		String periodCheck = otherSetMap.get("periodCheck").toString();

		if("1".equals(teaWeek)){

		}



    	return null;
	}

	/**
	 * 获得冲突坐标数量
	 * @param teaJxbId
	 * @param jxbCtXysMMap
	 * @param jxbYkbUseXysMMap
	 * @return
	 */
	public int getJxbCtXyCount(
			ObjectId teaJxbId,
			Map<ObjectId, Map<Integer, List<Integer>>> jxbCtXysMMap,
			Map<ObjectId, Map<Integer, List<Integer>>> jxbYkbUseXysMMap
	) {
		Map<Integer, List<Integer>> ctXysMap = new HashMap<Integer, List<Integer>>();
		Map<Integer, List<Integer>> jxbCtXysMap = jxbCtXysMMap.get(teaJxbId);
		if(null==jxbCtXysMap){
			jxbCtXysMap = new HashMap<Integer, List<Integer>>();
		}
		heBingMapToTagMap(jxbCtXysMap, ctXysMap);
		Map<Integer, List<Integer>> jxbYkbUseXysMap = jxbYkbUseXysMMap.get(teaJxbId);
		if(null==jxbYkbUseXysMap){
			jxbYkbUseXysMap = new HashMap<Integer, List<Integer>>();
		}
		heBingMapToTagMap(jxbYkbUseXysMap, ctXysMap);
		int ctXyCount = 0;
		for(List<Integer> value:ctXysMap.values()){
			ctXyCount +=value.size();
		}
		return ctXyCount;
	}

	/**
	 * 创建课表
	 * @param xList
	 * @param xyListMap
	 * @param jxbCountMaps
	 * @param jxbCtXysMMap
	 * @param jxbCTJxbsMap
	 * @param jxbYkbUseXysMMap
	 * @param jxbMap
	 * @param jxbksMap
	 * @param xyClsRoomMMap
	 * @param jxbRmsMap
	 */
	public void buildPaiKe(
			List<Integer> xList,
			Map<Integer, List<Integer>> xyListMap,
			List<Map<String, Object>> jxbCountMaps,
			Map<ObjectId, Map<Integer, List<Integer>>> jxbCtXysMMap,
			Map<ObjectId, List<ObjectId>> jxbCTJxbsMap,
			Map<ObjectId, Map<Integer, List<Integer>>> jxbYkbUseXysMMap,
			Map<ObjectId, N33_JXBEntry> jxbMap,
			Map<ObjectId, N33_JXBKSEntry> jxbksMap,
			Map<String, Map<ObjectId, N33_YKBEntry>> xyClsRoomMMap,
			Map<ObjectId, List<ObjectId>> jxbRmsMap
	){
		List<N33_YKBEntry> ykbUpdList = new ArrayList<N33_YKBEntry>();
		for(Map<String, Object> jxbCountMap:jxbCountMaps){
			if(null!=jxbCountMap.get("jxbId")) {
				ObjectId zxJxbId = (ObjectId)jxbCountMap.get("jxbId");

				Map<Integer, List<Integer>> jxbCtXysMap = jxbCtXysMMap.get(zxJxbId);
				if(null==jxbCtXysMap){
					jxbCtXysMap = new HashMap<Integer, List<Integer>>();
				}
				Map<Integer, List<Integer>> jxbYkbUseXysMap = jxbYkbUseXysMMap.get(zxJxbId);
				if(null==jxbYkbUseXysMap){
					jxbYkbUseXysMap = new HashMap<Integer, List<Integer>>();
				}
				N33_JXBEntry jxbEntry = jxbMap.get(zxJxbId);
				N33_JXBKSEntry jxbksEntry = jxbksMap.get(zxJxbId);
				int jxbKeiShi = jxbEntry.getJXBKS();
				int ypKeiShi = 0;
				if(null!=jxbksEntry){
					ypKeiShi = jxbksEntry.getYpCount();
				}else{
					jxbksEntry = new N33_JXBKSEntry(jxbEntry.getID(), jxbEntry.getSubjectId(), ypKeiShi, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang());
					jxbksMap.put(jxbEntry.getID(), jxbksEntry);
					jxbksDao.addN33_JXBKSEntry(jxbksEntry);
					if (null!=jxbEntry.getRelativeId()&&jxbEntry.getDanOrShuang() != 0) {
						N33_JXBKSEntry relaJxbksEntry = jxbksMap.get(jxbEntry.getRelativeId());
						if (relaJxbksEntry == null) {
							N33_JXBEntry relaJxbEntry = jxbMap.get(jxbEntry.getRelativeId());
							relaJxbksEntry = new N33_JXBKSEntry(relaJxbEntry.getID(), relaJxbEntry.getSubjectId(), ypKeiShi, relaJxbEntry.getGradeId(), relaJxbEntry.getSchoolId(), relaJxbEntry.getTermId(), relaJxbEntry.getDanOrShuang());
							jxbksMap.put(relaJxbEntry.getID(), relaJxbksEntry);
							jxbksDao.addN33_JXBKSEntry(relaJxbksEntry);
						}
					}
				}

				int initYpKeiShi = ypKeiShi;

				int xCount = xList.size();
				//教学班需要循环的次数
				int loopCount = jxbKeiShi % xCount == 0 ? jxbKeiShi / xCount : jxbKeiShi / xCount + 1;

				//初始化天索引 随机取得一列分时段的索引
				Random random = new Random();
				int xIndex = random.nextInt(xList.size());
				//int xIndex = 0;
				int initXIndex = xIndex;
				for(int loop = 0; loop<loopCount; loop++) {
					boolean isFinish = false;
					for(int i=0;i<xList.size();i++){
						int x = xList.get(xIndex);
						if(i>0){
							if(initXIndex == xIndex){
								break;
							}
						}
						if(jxbKeiShi>ypKeiShi) {
							List<Integer> yList = xyListMap.get(x);
							List<Integer> jxbYkbUseYs = jxbYkbUseXysMap.get(x) == null ? new ArrayList<Integer>() : jxbYkbUseXysMap.get(x);
							if(loop == 0) {
								boolean xIsUse = false;
								for(Integer y:yList){
									if(jxbYkbUseYs.contains(y)){
										xIsUse = true;
										break;
									}
								}
								if (xIsUse) {
									//判断分时段组合列索引是否 到底
									xIndex = getNewXIndex(xIndex, xList.size());
									continue;
								}
							}
							List<Integer> jxbCtYs = jxbCtXysMap.get(x) == null ? new ArrayList<Integer>() : jxbCtXysMap.get(x);
							List<Integer> keYongYs = new ArrayList<Integer>();
							for(Integer y:yList){
								if(!jxbCtYs.contains(y)&&!jxbYkbUseYs.contains(y)){
									keYongYs.add(y);
								}
							}
							int keYongY = 0;
							if(keYongYs.size()>0){
								int yIndex = random.nextInt(keYongYs.size());
								keYongY = keYongYs.get(yIndex);
							}else{
								//判断分时段组合列索引是否 到底
								xIndex = getNewXIndex(xIndex, xList.size());
								continue;
							}
							List<ObjectId> jxbRmIds = jxbRmsMap.get(zxJxbId);
							if(null==jxbRmIds){
								continue;
							}
							String key = x+","+keYongY;
							Map<ObjectId, N33_YKBEntry> clsRoomMMap = xyClsRoomMMap.get(key);
							for(ObjectId classRoomId:jxbRmIds) {
								N33_YKBEntry ykbEntry = clsRoomMMap.get(classRoomId);
								if (null != ykbEntry) {
									ykbEntryCopyJxbInfo(ykbEntry, jxbEntry, jxbMap);
									ykbUpdList.add(ykbEntry);
									clsRoomMMap.put(classRoomId, ykbEntry);
									xyClsRoomMMap.put(key, clsRoomMMap);
								}
							}
							ypKeiShi++;
							if(!jxbYkbUseYs.contains(keYongY)) {
								jxbYkbUseYs.add(keYongY);
								jxbYkbUseXysMap.put(x, jxbYkbUseYs);
							}
							if(!jxbCtYs.contains(keYongY)) {
								jxbCtYs.add(keYongY);
								jxbCtXysMap.put(x, jxbCtYs);
							}
							List<ObjectId> ctJxbs = jxbCTJxbsMap.get(zxJxbId);
							if (null != ctJxbs) {
								for (ObjectId ctJxbId : ctJxbs) {
									Map<Integer, List<Integer>> ctJxbCtXysMap = jxbCtXysMMap.get(ctJxbId);
									if (null == ctJxbCtXysMap) {
										ctJxbCtXysMap = new HashMap<Integer, List<Integer>>();
									}
									List<Integer> ctJxbCtXys = ctJxbCtXysMap.get(x);
									if(null==ctJxbCtXys){
										ctJxbCtXys = new ArrayList<Integer>();
									}
									if(!ctJxbCtXys.contains(keYongY)) {
										ctJxbCtXys.add(keYongY);
									}
									ctJxbCtXysMap.put(x, ctJxbCtXys);
									jxbCtXysMMap.put(ctJxbId, ctJxbCtXysMap);
								}
							}
						}else{
							isFinish = true;
							break;
						}
						//判断分时段组合列索引是否 到底
						xIndex = getNewXIndex(xIndex, xList.size());
					}
					if(isFinish){
						break;
					}else{
						if(loop==loopCount-1&&4>loopCount){
							loopCount++;
						}
					}
				}
				if(ypKeiShi>initYpKeiShi) {
					jxbksEntry.setYpCount(ypKeiShi);
					jxbksMap.put(jxbEntry.getID(), jxbksEntry);
					if (null!=jxbEntry.getRelativeId()&&jxbEntry.getDanOrShuang() != 0) {
						N33_JXBKSEntry relaJxbksEntry = jxbksMap.get(jxbEntry.getRelativeId());
						relaJxbksEntry.setYpCount(ypKeiShi);
						jxbksMap.put(jxbEntry.getRelativeId(), relaJxbksEntry);
					}
					jxbYkbUseXysMMap.put(zxJxbId, jxbYkbUseXysMap);
				}
			}
		}
		if(ykbUpdList.size()>0){
			List<ObjectId> upFinishKSJxbIds = new ArrayList<ObjectId>();
			for(N33_YKBEntry ykbEntry:ykbUpdList){
				if(!upFinishKSJxbIds.contains(ykbEntry.getJxbId())) {
					N33_JXBKSEntry jxbksEntry = jxbksMap.get(ykbEntry.getJxbId());
					jxbksDao.updateN33_JXBKS(jxbksEntry);
					upFinishKSJxbIds.add(ykbEntry.getJxbId());
				}
				if (ykbEntry.getType() == 6) {
					if(null!=ykbEntry.getNJxbId()) {
						if(!upFinishKSJxbIds.contains(ykbEntry.getNJxbId())) {
							N33_JXBKSEntry relaJxbksEntry = jxbksMap.get(ykbEntry.getNJxbId());
							jxbksDao.updateN33_JXBKS(relaJxbksEntry);
							upFinishKSJxbIds.add(ykbEntry.getNJxbId());
						}
					}
				}
				ykbDao.updateN33_YKB(ykbEntry);
			}
		}
	}

	/**
	 * 判断分时段组合列索引是否 到底
	 * @param xIndex
	 * @param size
	 * @return
	 */
	public int getNewXIndex(int xIndex, int size){
		int newXIndex = 0;
		newXIndex = xIndex+1;
		//判断分时段组合列索引是否 到底
		if (newXIndex > size - 1) {
			// 到底 置为零
			newXIndex = 0;
		}
		return newXIndex;
	}

	/**
	 * 更新源课表entry
	 * @param ykbEntry
	 * @param jxbEntry
	 * @param jxbMap
	 */
	public void ykbEntryCopyJxbInfo(N33_YKBEntry ykbEntry, N33_JXBEntry jxbEntry, Map<ObjectId, N33_JXBEntry> jxbMap){
		if (null != ykbEntry) {
			ykbEntry.setJxbId(jxbEntry.getID());
			ykbEntry.setIsUse(Constant.ONE);
			ykbEntry.setGradeId(jxbEntry.getGradeId());
			ykbEntry.setSubjectId(jxbEntry.getSubjectId());
			ykbEntry.setTeacherId(jxbEntry.getTercherId());
			ykbEntry.setType(jxbEntry.getType());
			if (jxbEntry.getDanOrShuang() != 0) {
				N33_JXBEntry ralejxb = jxbMap.get(jxbEntry.getRelativeId());
				ykbEntry.setNSubjectId(ralejxb.getSubjectId());
				ykbEntry.setNTeacherId(ralejxb.getTercherId());
				ykbEntry.setNJxbId(ralejxb.getID());
			}
		}
	}

	/**
	 * 排序1
	 * @param list
	 */
    public void listSortOne(List<Map<String, Object>> list){
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
				int totalKeShi0 = arg0.get("totalKeShi")==null?0:(Integer) arg0.get("totalKeShi");
				int totalKeShi1 = arg1.get("totalKeShi")==null?0:(Integer)arg1.get("totalKeShi");
				if(totalKeShi0==totalKeShi1){
					int teaJxbCount0 = arg0.get("teaJxbCount")==null?0:(Integer) arg0.get("teaJxbCount");
					int teaJxbCount1 = arg1.get("teaJxbCount")==null?0:(Integer)arg1.get("teaJxbCount");
					if(teaJxbCount1 == teaJxbCount0) {
						//随机取得索引
						Random random = new Random();
						int flag = random.nextInt(2);
						if (flag == 1) {
							return -1;
						} else {
							return 0;
						}
					}else{
						return teaJxbCount1 - teaJxbCount0;
					}
				}else{
					return totalKeShi1 - totalKeShi0;
				}
			}
		});
	}

	/**
	 * 排序2
	 * @param list
	 */
	public void listSortTwo(List<Map<String, Object>> list) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
				int ctXyCount0 = arg0.get("ctXyCount")==null?0:(Integer) arg0.get("ctXyCount");
				int ctXyCount1 = arg1.get("ctXyCount")==null?0:(Integer)arg1.get("ctXyCount");
				if(ctXyCount1 == ctXyCount0) {
					//随机取得索引
					Random random = new Random();
					int flag = random.nextInt(2);
					if (flag == 1) {
						return -1;
					} else {
						return 0;
					}
				}else{
					return ctXyCount1 - ctXyCount0;
				}
			}
		});
	}

	/**
	 *
	 * @param jxbEntries
	 * @param zxMap
	 * @return
	 */
	public Map<ObjectId, List<ObjectId>> getJxbRmsMap(List<N33_JXBEntry> jxbEntries, Map<ObjectId, List<N33_ZhuanXiangEntry>> zxMap) {
		Map<ObjectId, List<ObjectId>> reMap = new HashMap<ObjectId, List<ObjectId>>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			if (jxbEntry.getStudentIds().size() == 0) {
				continue;
			}
			List<ObjectId> rmIds = reMap.get(jxbEntry.getID());
			if(null==rmIds){
				rmIds = new ArrayList<ObjectId>();
			}
			if (jxbEntry.getType() == 4) {
				List<N33_ZhuanXiangEntry> zxList = zxMap.get(jxbEntry.getID());
				if (zxList != null) {
					for(N33_ZhuanXiangEntry zxEntry:zxList){
						if(null!=zxEntry.getRoomId()) {
							if(!rmIds.contains(zxEntry.getRoomId())){
								rmIds.add(zxEntry.getRoomId());
							}
						}
					}
				}
			}else{
				if(null!=jxbEntry.getClassroomId()) {
					if(!rmIds.contains(jxbEntry.getClassroomId())){
						rmIds.add(jxbEntry.getClassroomId());
					}
				}
			}
			reMap.put(jxbEntry.getID(), rmIds);
			if(null!=jxbEntry.getRelativeId()){
				reMap.put(jxbEntry.getRelativeId(), rmIds);
			}
		}
		return reMap;

	}

	/**
	 * 获取 教室->上课教学班集合
	 * @param jxbEntries
	 * @param zxMap
	 * @return
	 */
	public Map<ObjectId, List<ObjectId>> getRmJxbsMap(List<N33_JXBEntry> jxbEntries, Map<ObjectId, List<N33_ZhuanXiangEntry>> zxMap) {
		Map<ObjectId, List<ObjectId>> reMap = new HashMap<ObjectId, List<ObjectId>>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			if (jxbEntry.getStudentIds().size() == 0) {
				continue;
			}
			List<ObjectId> rmIds = new ArrayList<ObjectId>();
			if (jxbEntry.getType() == 4) {
				List<N33_ZhuanXiangEntry> zxList = zxMap.get(jxbEntry.getID());
				if (zxList != null) {
					for(N33_ZhuanXiangEntry zxEntry:zxList){
						if(null!=zxEntry.getRoomId()) {
							if(!rmIds.contains(zxEntry.getRoomId())){
								rmIds.add(zxEntry.getRoomId());
							}
						}
					}
				}
			}else{
				if(null!=jxbEntry.getClassroomId()) {
					if(!rmIds.contains(jxbEntry.getClassroomId())){
						rmIds.add(jxbEntry.getClassroomId());
					}
				}
			}
			for(ObjectId rmId : rmIds){
				List<ObjectId> jxbIds = reMap.get(rmId);
				if(null==jxbIds){
					jxbIds = new ArrayList<ObjectId>();
				}
				jxbIds.add(jxbEntry.getID());
				if(null!=jxbEntry.getRelativeId()){
					jxbIds.add(jxbEntry.getRelativeId());
				}
				reMap.put(rmId, jxbIds);
			}
		}
		return reMap;
	}

    /**
	 * 获取 类型->教室->教学班集合
	 * @param jxbEntries
     * @return
     */
    public Map<Integer, List<ObjectId>> getTypeJxbIdsMap(
			List<N33_JXBEntry> jxbEntries
	) {
		Map<Integer, List<ObjectId>> reMap = new HashMap<Integer, List<ObjectId>>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			if (jxbEntry.getStudentIds().size() == 0) {
				continue;
			}
			List<ObjectId> jxbIds = reMap.get(jxbEntry.getType());
			if(null==jxbIds){
				jxbIds = new ArrayList<ObjectId>();
			}
			if(!jxbIds.contains(jxbEntry.getID())) {
				jxbIds.add(jxbEntry.getID());
			}
			if(null!=jxbEntry.getRelativeId()){
				if(!jxbIds.contains(jxbEntry.getRelativeId())) {
					jxbIds.add(jxbEntry.getRelativeId());
				}
			}

			reMap.put(jxbEntry.getType(), jxbIds);
		}
        return reMap;
    }

	/**
	 * 教室教学班冲突格
	 * @param schoolId
	 * @param gradeId
	 * @param ciId
	 * @param jxbEntries
	 * @param jxbMap
	 * @param zxMap
	 * @return
	 */
	public Map<ObjectId, Map<Integer, List<Integer>>> getJxbCtXysMMap(
			ObjectId schoolId,
			ObjectId gradeId,
			ObjectId xqId,
			ObjectId ciId,
			List<N33_JXBEntry> jxbEntries,
			Map<ObjectId, N33_JXBEntry> jxbMap,
			Map<ObjectId, List<N33_ZhuanXiangEntry>> zxMap,
			List<N33_YKBEntry> ykbEntries,
			List<N33_JXBCTEntry> jxbctEntries,
			String jxbType
	) {
		//教室类型map
		Map<ObjectId, Integer> roomTypeMap = new HashMap<ObjectId, Integer>();
		for(N33_JXBEntry jxb:jxbMap.values()){
			if(jxb.getAcClassType()==1) {
				Integer roomType = roomTypeMap.get(jxb.getClassroomId())==null?0:roomTypeMap.get(jxb.getClassroomId());
				if(roomType!=1){
					if(jxb.getTimeCombSerial()>=1){
						roomType=1;
						roomTypeMap.put(jxb.getClassroomId(), roomType);
					}
				}
			}
		}

		//事务
		Map<Integer, List<Integer>> allTeaXyNotUseMap = new HashMap<Integer, List<Integer>>();
		Map<ObjectId, Map<Integer, List<Integer>>> teaXyNotUseMMap = new HashMap<ObjectId, Map<Integer, List<Integer>>>();
		List<N33_SWDTO> swDtos = swService.getGuDingShiWuList(xqId, schoolId);
		for(N33_SWDTO swDto:swDtos){
			int x = swDto.getX();
			int y = swDto.getY();
			if(swDto.getTids().size()>0) {
				for (String teaId : swDto.getTids()) {
					Map<Integer, List<Integer>> teaXyNotUseMap = teaXyNotUseMMap.get(new ObjectId(teaId));
					if (null == teaXyNotUseMap) {
						teaXyNotUseMap = new HashMap<Integer, List<Integer>>();
					}
					mapAddXys(teaXyNotUseMap, x, y);
					teaXyNotUseMMap.put(new ObjectId(teaId), teaXyNotUseMap);
				}
			}else{
				mapAddXys(allTeaXyNotUseMap, x, y);
			}
		}

		//单双周教学班
		List<N33_JXBEntry> dszJxbs = new ArrayList<N33_JXBEntry>();
		//非单双周教学班
		List<N33_JXBEntry> ndszJxbs = new ArrayList<N33_JXBEntry>();

		//教师map
		Map<ObjectId, N33_TeaEntry> teaMap = teaDao.getTeaMap(ciId, schoolId);

		//固定事项
		Map<ObjectId, List<N33_GDSXEntry>> gradeGDSXsMap = gdsxService.getGradeGDSXEntriesMap(xqId, schoolId);
		Map<ObjectId, Map<Integer, List<Integer>>> jxbXyNotUseMMap = new HashMap<ObjectId, Map<Integer, List<Integer>>>();
		for(N33_JXBEntry jxbEntry:jxbEntries){
			if(jxbEntry.getType() == 6){
				dszJxbs.add(jxbEntry);
			}else{
				ndszJxbs.add(jxbEntry);
			}
			Map<Integer, List<Integer>> jxbXyNotUseMap = jxbXyNotUseMMap.get(jxbEntry.getID());
			if(null==jxbXyNotUseMap){
				jxbXyNotUseMap = new HashMap<Integer, List<Integer>>();
			}
			List<ObjectId> teaIds = new ArrayList<ObjectId>();
			if (jxbEntry.getType() == 4) {
				List<N33_ZhuanXiangEntry> zxEntries = zxMap.get(jxbEntry.getID());
				for(N33_ZhuanXiangEntry zxEntry:zxEntries){
					ObjectId teaId = zxEntry.getTeaId();
					if(null!=teaId) {
						if(!teaIds.contains(teaId)) {
							teaIds.add(teaId);
						}
					}
				}
			}else if (jxbEntry.getType() == 6) {
				if (jxbEntry.getTercherId() != null) {
					if(!teaIds.contains(jxbEntry.getTercherId())) {
						teaIds.add(jxbEntry.getTercherId());
					}
				}
				N33_JXBEntry relaJxbEntry = jxbMap.get(jxbEntry.getRelativeId());
				if(null!=relaJxbEntry){
					if (relaJxbEntry.getTercherId() != null) {
						if(!teaIds.contains(relaJxbEntry.getTercherId())) {
							teaIds.add(relaJxbEntry.getTercherId());
						}
					}
				}
			}else{
				if (jxbEntry.getTercherId() != null) {
					if(!teaIds.contains(jxbEntry.getTercherId())) {
						teaIds.add(jxbEntry.getTercherId());
					}
				}
			}

			if(teaIds.size()>0) {
				for (ObjectId teaId : teaIds) {
					N33_TeaEntry teaEntry = teaMap.get(teaId);
					if (teaEntry != null) {
						List<ObjectId> teaGradeIds = teaEntry.getGradeList();
						for (ObjectId teaGradeId : teaGradeIds) {
							List<N33_GDSXEntry> n33_gdsxs = gradeGDSXsMap.get(teaGradeId);
							if (null == n33_gdsxs) {
								continue;
							}
							for (N33_GDSXEntry gdsxEntry : n33_gdsxs) {
								mapAddXys(jxbXyNotUseMap, gdsxEntry.getX(), gdsxEntry.getY());
							}
						}
					}
					Map<Integer, List<Integer>> teaXyNotUseMap = teaXyNotUseMMap.get(teaId);
					if (null != teaXyNotUseMap) {
						heBingMapToTagMap(teaXyNotUseMap, jxbXyNotUseMap);
					}
					heBingMapToTagMap(allTeaXyNotUseMap, jxbXyNotUseMap);
				}
			}else{
				List<N33_GDSXEntry> n33_gdsxs = gradeGDSXsMap.get(gradeId);
				if (null == n33_gdsxs) {
					continue;
				}
				for (N33_GDSXEntry gdsxEntry : n33_gdsxs) {
					mapAddXys(jxbXyNotUseMap, gdsxEntry.getX(), gdsxEntry.getY());
				}
			}
			jxbXyNotUseMMap.put(jxbEntry.getID(), jxbXyNotUseMap);
		}

		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId, gradeId, ciId, 1);
		//固定走班格
		Map<Integer, List<Integer>> gdZbXysMap = new HashMap<Integer, List<Integer>>();
		if("feiZouBan".equals(jxbType)) {
			List<ZouBanTimeDTO> zouBanTimeDTOs = null;
			if ((turnOff != null && turnOff.getStatus() != 0) || turnOff == null) {
				zouBanTimeDTOs = courseRangeService.getZouBanTimeByType(schoolId, ciId, gradeId, 0);
			} else {
				zouBanTimeDTOs = new ArrayList<ZouBanTimeDTO>();
			}
			if (zouBanTimeDTOs != null && zouBanTimeDTOs.size() != 0) {
				for (ZouBanTimeDTO dto : zouBanTimeDTOs) {
					int x = dto.getX() - 1;
					int y = dto.getY() - 1;
					mapAddXys(gdZbXysMap, x, y);
				}
			}
		}

		//教学班教学班之间的冲突
		List<ObjectId> allCtJxbIds = new ArrayList<ObjectId>();
		Map<ObjectId, List<ObjectId>> jxbCTJxbsMap = new HashMap<ObjectId, List<ObjectId>>();
		//List<N33_JXBCTEntry> jxbctEntrys = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, ciId);
		for (N33_JXBCTEntry jxbctEntry : jxbctEntries) {
			N33_JXBEntry jxbEntry = jxbMap.get(jxbctEntry.getOjxbId());
			if(null!=jxbEntry) {
				List<ObjectId> jxbCTJxbs = jxbCTJxbsMap.get(jxbctEntry.getJxbId());
				if (null == jxbCTJxbs) {
					jxbCTJxbs = new ArrayList<ObjectId>();
				}

				jxbCTJxbs.add(jxbctEntry.getOjxbId());

				jxbCTJxbsMap.put(jxbctEntry.getJxbId(), jxbCTJxbs);

				if (!allCtJxbIds.contains(jxbctEntry.getOjxbId())) {
					allCtJxbIds.add(jxbctEntry.getOjxbId());
				}

				if (!allCtJxbIds.contains(jxbctEntry.getJxbId())) {
					allCtJxbIds.add(jxbctEntry.getJxbId());
				}
			}
		}

		//冲突的教学班源课表
		List<N33_YKBEntry> ctYkbEntries = ykbDao.getYKBEntrysByJXBIdsOrNJxbIds(ciId, allCtJxbIds, schoolId);
		Map<ObjectId, List<N33_YKBEntry>> jxbYkbsMap = new HashMap<ObjectId, List<N33_YKBEntry>>();
		for (N33_YKBEntry ykbEntry : ctYkbEntries) {
			if(ykbEntry.getType() != 6){
				List<N33_YKBEntry> jxbYkbs = jxbYkbsMap.get(ykbEntry.getJxbId());
				if(null==jxbYkbs){
					jxbYkbs = new ArrayList<N33_YKBEntry>();
				}
				jxbYkbs.add(ykbEntry);
				jxbYkbsMap.put(ykbEntry.getJxbId(), jxbYkbs);
				if(null!=ykbEntry.getNJxbId()){
					List<N33_YKBEntry> njxbYkbs = jxbYkbsMap.get(ykbEntry.getNJxbId());
					if(null==njxbYkbs){
						njxbYkbs = new ArrayList<N33_YKBEntry>();
					}
					njxbYkbs.add(ykbEntry);
					jxbYkbsMap.put(ykbEntry.getNJxbId(), njxbYkbs);
				}
			}
		}

		//教学班冲突格
		Map<ObjectId, Map<Integer, List<Integer>>> jxbXysMMap = new HashMap<ObjectId, Map<Integer, List<Integer>>>();
		for(N33_JXBEntry jxbEntry:ndszJxbs) {
			List<ObjectId> jxbCTJxbs = jxbCTJxbsMap.get(jxbEntry.getID());
			if(null==jxbCTJxbs){
				continue;
			}
			Map<Integer, List<Integer>> jxbXysMap = jxbXysMMap.get(jxbEntry.getID());
			if(null==jxbXysMap){
				jxbXysMap = new HashMap<Integer, List<Integer>>();
			}
			for(ObjectId cTJxbId:jxbCTJxbs){
				List<N33_YKBEntry> jxbYkbs = jxbYkbsMap.get(cTJxbId);
				if(null!=jxbYkbs){
					for (N33_YKBEntry ykbEntry : jxbYkbs) {
						int x = ykbEntry.getX();
						int y = ykbEntry.getY();
						mapAddXys(jxbXysMap, x, y);
					}
				}
				N33_JXBEntry ctJxbEntry = jxbMap.get(cTJxbId);
				if(null!=ctJxbEntry){
					if(null!=ctJxbEntry.getRelativeId()){
						List<N33_YKBEntry> njxbYkbs = jxbYkbsMap.get(ctJxbEntry.getRelativeId());
						if(null!=njxbYkbs){
							for (N33_YKBEntry ykbEntry : njxbYkbs) {
								int x = ykbEntry.getX();
								int y = ykbEntry.getY();
								mapAddXys(jxbXysMap, x, y);
							}
						}
					}
				}
			}
			jxbXysMMap.put(jxbEntry.getID(), jxbXysMap);
		}

		//本次排课的源课表
		//List<N33_YKBEntry> ykbEntries = ykbDao.getYKBEntrysList(ciId, schoolId);
		for(N33_JXBEntry jxbEntry:dszJxbs){
			N33_JXBEntry raleJxbEntry = jxbMap.get(jxbEntry.getRelativeId());
			if(null==raleJxbEntry){
				continue;
			}
			ObjectId jxbTeaId = jxbEntry.getTercherId();
			ObjectId raleJxbTeaId = raleJxbEntry.getTercherId();
			Map<Integer, List<Integer>> jxbXysMap = jxbXysMMap.get(jxbEntry.getID());
			if(null==jxbXysMap){
				jxbXysMap = new HashMap<Integer, List<Integer>>();
			}

			Map<Integer, List<Integer>> raleJxbXysMap = jxbXysMMap.get(raleJxbEntry.getID());
			if(null!=raleJxbXysMap){
				heBingMapToTagMap(raleJxbXysMap, jxbXysMap);
			}
			for (N33_YKBEntry ykbEntry : ykbEntries) {
				ObjectId teacherId = ykbEntry.getTeacherId();
				ObjectId nTeacherId = ykbEntry.getNTeacherId();
				if ((teacherId!=null&&(teacherId.equals(jxbTeaId)||teacherId.equals(raleJxbTeaId)))
						|| (nTeacherId!=null&&(nTeacherId.equals(jxbTeaId)|| nTeacherId.equals(raleJxbTeaId)))) {
					int x = ykbEntry.getX();
					int y = ykbEntry.getY();
					mapAddXys(jxbXysMap, x, y);
				}
			}
			jxbXysMMap.put(jxbEntry.getID(), jxbXysMap);
			jxbXysMMap.put(raleJxbEntry.getID(), jxbXysMap);
		}

		//非走班教学班格Map
		Map<Integer, List<Integer>> njxbXysMap = new HashMap<Integer, List<Integer>>();
		//走班教学班格Map
		Map<Integer, List<Integer>> zbjxbXysMap = new HashMap<Integer, List<Integer>>();
		if((turnOff != null && turnOff.getStatus() != 0) || turnOff == null) {
			//如果某一个格子放了等级考的学科，那么这个格子同时上课的其他教室必须放等级考的学科
			for (N33_YKBEntry ykbEntry : ykbEntries) {
				if (ykbEntry.getGradeId() != null && gradeId.equals(ykbEntry.getGradeId()) && ykbEntry.getJxbId() != null) {
					N33_JXBEntry useJxbEntry = jxbMap.get(ykbEntry.getJxbId());
					if(null==useJxbEntry){
						continue;
					}
					if (useJxbEntry.getType() != 1 && useJxbEntry.getType() != 2) {
						//非走班格
						int x = ykbEntry.getX();
						int y = ykbEntry.getY();
						mapAddXys(njxbXysMap, x, y);
					} else if (useJxbEntry.getType() != 3 && useJxbEntry.getType() != 4 && useJxbEntry.getType() != 6) {
						//走班格
						int x = ykbEntry.getX();
						int y = ykbEntry.getY();
						mapAddXys(zbjxbXysMap, x, y);
					}
				}
			}
		}
		//教室教学班冲突格
		Map<ObjectId, Map<Integer, List<Integer>>> jxbCtXysMMap = new HashMap<ObjectId, Map<Integer, List<Integer>>>();
		for(N33_JXBEntry jxbEntry:jxbEntries){
			int roomType = 1;
			if(jxbEntry.getAcClassType()==1) {
				roomType = roomTypeMap.get(jxbEntry.getClassroomId()) == null ? 0 : roomTypeMap.get(jxbEntry.getClassroomId());
			}
			Map<Integer, List<Integer>> jxbCtXysMap = jxbCtXysMMap.get(jxbEntry.getID());
			if (null == jxbCtXysMap) {
				jxbCtXysMap = new HashMap<Integer, List<Integer>>();
			}
			if (jxbEntry.getType() == 1 || jxbEntry.getType() == 2) {
				heBingMapToTagMap(njxbXysMap, jxbCtXysMap);
			} else if (jxbEntry.getType() == 3 ||jxbEntry.getType() == 6) {
				if(roomType==1) {
					heBingMapToTagMap(gdZbXysMap, jxbCtXysMap);
					heBingMapToTagMap(zbjxbXysMap, jxbCtXysMap);
				}
			}else if(jxbEntry.getType() == 4){
				heBingMapToTagMap(gdZbXysMap, jxbCtXysMap);
				heBingMapToTagMap(zbjxbXysMap, jxbCtXysMap);
			}

			Map<Integer, List<Integer>> jxbXysMap = jxbXysMMap.get(jxbEntry.getID());
			if (null != jxbXysMap) {
				heBingMapToTagMap(jxbXysMap, jxbCtXysMap);
			}
			Map<Integer, List<Integer>> jxbXyNotUseMap = jxbXyNotUseMMap.get(jxbEntry.getID());
			if (null != jxbXyNotUseMap) {
				heBingMapToTagMap(jxbXyNotUseMap, jxbCtXysMap);
			}
			if (null != jxbEntry.getRelativeId()) {
				Map<Integer, List<Integer>> raleJxbXysMap = jxbXysMMap.get(jxbEntry.getRelativeId());
				if (null != raleJxbXysMap) {
					heBingMapToTagMap(raleJxbXysMap, jxbCtXysMap);
				}
				Map<Integer, List<Integer>> raleJxbXyNotUseMap = jxbXyNotUseMMap.get(jxbEntry.getRelativeId());
				if (null != raleJxbXyNotUseMap) {
					heBingMapToTagMap(raleJxbXyNotUseMap, jxbCtXysMap);
				}
			}
			jxbCtXysMMap.put(jxbEntry.getID(), jxbCtXysMap);
		}
		return jxbCtXysMMap;
	}

	/**
	 * 添加坐标
	 * @param map
	 * @param x
	 * @param y
	 */
	public void mapAddXys(Map<Integer, List<Integer>> map, int x, int y){
		List<Integer> ys = map.get(x);
		if(null==ys){
			ys = new ArrayList<Integer>();
		}
		if(!ys.contains(y)){
			ys.add(y);
		}
		map.put(x, ys);
	}

	/**
	 * 合并map
	 * @param map
	 * @param tagMap
	 */
	public void heBingMapToTagMap(Map<Integer, List<Integer>> map, Map<Integer, List<Integer>> tagMap){
		for (Map.Entry<Integer, List<Integer>> jxbXys : map.entrySet()) {
			List<Integer> ctYs = tagMap.get(jxbXys.getKey());
			if(null==ctYs){
				ctYs = new ArrayList<Integer>();
			}
			for(Integer jxbY:jxbXys.getValue()){
				if(!ctYs.contains(jxbY)){
					ctYs.add(jxbY);
				}
			}
			tagMap.put(jxbXys.getKey(), ctYs);
		}
	}

	/**
	 *
	 * @param schoolId
	 * @param gradeId
	 * @param ciId
	 */
	public void clearData(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
		//非走班类型
		List<Integer> fzbTypes = new ArrayList<Integer>();
		fzbTypes.add(3);
		fzbTypes.add(4);
		//fzbTypes.add(5);
		fzbTypes.add(6);

		//获取非走班教学班
		//jxb add
		int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
		//jxb add
		List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(schoolId, gradeId, ciId, fzbTypes,acClassType);

		List<ObjectId> jxbIds = new ArrayList<ObjectId>();
		for (N33_JXBEntry jxbEntry : jxbEntries) {
			if(null!=jxbEntry.getID()){
				jxbIds.add(jxbEntry.getID());
			}
			if(null!=jxbEntry.getRelativeId()){
				jxbIds.add(jxbEntry.getRelativeId());
			}
		}
		jxbksDao.clearJxbKsByJxbIds(jxbIds, ciId);
		ykbDao.clearYuanKeBiaoKeShi(ciId, schoolId, gradeId, fzbTypes);
	}
}
