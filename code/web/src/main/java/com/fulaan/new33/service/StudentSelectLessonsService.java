package com.fulaan.new33.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fulaan.annotation.ObjectIdType;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_ClassDao;
import com.db.new33.isolate.N33_StudentDao;
import com.fulaan.new33.service.SchoolSelectLessonSetService.SchoolLesson33DTO;
import com.fulaan.new33.service.SchoolSelectLessonSetService.lesson33DTO;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.StudentEntry;
import com.sys.constants.Constant;

/**
 * 学生选课service
 * @author fourer
 *
 */
@Service
public class StudentSelectLessonsService {
	private GradeDao gradeDao = new GradeDao();
    private ClassDao classDao = new ClassDao();
    private N33_ClassDao N33_ClassDao = new N33_ClassDao();
	private N33_StudentDao N33_StudentDao = new N33_StudentDao();
	@Autowired
	private SchoolSelectLessonSetService SchoolSelectLessonSetService;
	
	public List<Map<String,String>> importUser(InputStream inputStream, ObjectId xqid, ObjectId sid) throws IOException {
        List<Map<String,String>> result = new ArrayList<Map<String, String>>();
		//年级
        Map<String, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMapKeyIsName(xqid, sid);
        //班级
        List<ClassEntry> classList = classDao.findClassListBySchoolId(sid, xqid);
        //学科
        Map<ObjectId,SchoolLesson33DTO> gradeId_lesson = new HashMap<ObjectId,SchoolLesson33DTO>();
        for(Grade grade:gradeMap.values()) {
        	gradeId_lesson.put(grade.getGradeId(), SchoolSelectLessonSetService.getSchoolSelects(xqid, sid, grade.getGradeId()));
        	
        }
		
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet UserSheet = workbook.getSheet("导入");
        int rowNum = UserSheet.getLastRowNum();
        for (int i = 1; i <= rowNum; i++) {
            String username = null;
            String sn = null;
            ObjectId gradeId = null;
            ObjectId classId = null;
            Integer classXh = null;
            String gradeName = "";
            Integer level = null;
            //姓名
            HSSFCell cell = UserSheet.getRow(i).getCell(0);
            if (!getStringCellValue(cell).equals("")) {
                //根据学生取姓名
            	username = getStringCellValue(cell);
            } 
            //学号
            cell = UserSheet.getRow(i).getCell(1);
            if (!getStringCellValue(cell).equals("")) {
                //根据学生取学号
            	sn = getStringCellValue(cell);
            } 
          //年级
            cell = UserSheet.getRow(i).getCell(2);
            if (!getStringCellValue(cell).equals("")) {
                //取年级
                Grade grade = gradeMap.get(getStringCellValue(cell));
                gradeName = getStringCellValue(cell);
                if (grade != null) {
                	gradeId = grade.getGradeId();
                }
            }
            //班级
            cell = UserSheet.getRow(i).getCell(3);
            if (!getStringCellValue(cell).equals("")) {
                //取班级
//                Integer xh = Integer.parseInt(getStringCellValue(cell).split(".")[0]);
            	Integer xh = new Double(cell.getNumericCellValue()).intValue();
                classXh = xh;
                ClassEntry classEntry = null;
                for (ClassEntry classEntry1 : classList) {
               	 if (classEntry1.getXh() == xh &&gradeId!=null&& classEntry1.getGradeId().toString().equals(gradeId.toString())) {
                        classEntry = classEntry1;
                        break;
                    }
				}
                if (classEntry != null) {
                    classId = classEntry.getClassId();
                } 
            }
            //层级
            cell = UserSheet.getRow(i).getCell(4);
	        if (!getStringCellValue(cell).equals("") && !getStringCellValue(cell).equals("无")) {
                level = new Double(cell.getNumericCellValue()).intValue();
            }
            List<StudentEntry> list = N33_StudentDao.queryList(sn, username, classId, gradeId, xqid);
            if(list.size()==1) {
            	ObjectId id = list.get(0).getID();
            	gradeId = list.get(0).getGradeId();
            	String combiname = null;

                // 组合
                cell = UserSheet.getRow(i).getCell(5);
                if (!getStringCellValue(cell).equals("")) {
                	combiname = getStringCellValue(cell);
                } 
                SchoolLesson33DTO lesson = gradeId_lesson.get(gradeId);
                if(lesson!=null&&combiname!=null) {
                	String name = transformName(combiname);
                	for(lesson33DTO lessondto:lesson.getList()) {
                		if(name!=null&&lessondto.getName().equals(name)) {
                			N33_StudentDao.updateStudentSelectById(id, level, lessondto.getSubjectId1(), lessondto.getSubjectId2(), lessondto.getSubjectId3(), name);
                		}
                	}
                	
                }
            }
            else {// 根据条件查到多个则跳过
                Map<String,String> errorStu = new HashMap<String, String>();
                errorStu.put("userName",username);
                errorStu.put("stuNum",sn == null ? "" : sn);
                errorStu.put("classXH",String.valueOf(classXh) == null ? "" : String.valueOf(classXh));
                errorStu.put("grade",gradeName);
                errorStu.put("level",String.valueOf(level) == null ? "" : String.valueOf(level));
                result.add(errorStu);
            	continue;
            }
        }
        return result;
	}
	private String transformName(String name) {
		if(name.length()!=3) {
			return null;
		}
		List<String> arr = Arrays.asList(name.substring(0, 1),name.substring(1, 2),name.substring(2, 3));
		Collections.sort(arr, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
			    if(SchoolSelectLessonSetService.subjectHead_sort.get(o1)!=null&&SchoolSelectLessonSetService.subjectHead_sort.get(o2)!=null)
				    return SchoolSelectLessonSetService.subjectHead_sort.get(o1)-SchoolSelectLessonSetService.subjectHead_sort.get(o2);
			    else{
			        return 0;
                }
			}
		});
		return arr.get(0)+arr.get(1)+arr.get(2);
	}
	public void exportTemplate(HttpServletResponse response) {
		HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("导入");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell = row.createCell(1);
        cell.setCellValue("学籍号");
        cell = row.createCell(2);
        cell.setCellValue("年级");
        cell = row.createCell(3);
        cell.setCellValue("班级序号");
        cell = row.createCell(4);
        cell.setCellValue("层级");
        cell = row.createCell(5);
        cell.setCellValue("选科组合");
        
        HSSFRow row2  = sheet.createRow(1);
        HSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("爱德华威滕");
        cell2 = row2.createCell(1);
        cell2.setCellValue("2018020305");
        cell2 = row2.createCell(2);
        cell2.setCellValue("高一");
        cell2 = row2.createCell(3);
        cell2.setCellValue(1);
        cell2 = row2.createCell(4);
        cell2.setCellValue(1);
        cell2 = row2.createCell(5);
        cell2.setCellValue("物化生");

        String[] list = new String[5];
        list[0] = "1";
        list[1] = "2";
        list[2] = "3";
        list[3] = "4";
        list[4] = "5";
        //性别
        CellRangeAddressList regions = new CellRangeAddressList(1, 1000, 4, 4);
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(list);
        HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("导入选科.xls", "UTF-8"));
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
	
	private String getStringCellValue(HSSFCell cell) {
        if (cell == null) return Constant.EMPTY;
        String strCell;

        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                strCell = Constant.EMPTY;
                break;
        }

        return StringUtils.isBlank(strCell) ? Constant.EMPTY : strCell;
    }
	
	public List<Map<String,String>> getStuSelectByClass(ObjectId xqid,ObjectId gradeId,ObjectId id,String name){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		ClassEntry classEntry = N33_ClassDao.findClassEntryByClassId(xqid,id);
		Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId,xqid);
		String className = classEntry.getClassName();
		List<ObjectId> studentIds = classEntry.getStudentList();
		List<StudentEntry> studentList = N33_StudentDao.getStuList(studentIds, xqid,name);
		for(StudentEntry entry:studentList) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("name", entry.getUserName());
			map.put("class", className);
			map.put("xh",classEntry.getXh().toString());
            map.put("lev",entry.getLevel()==0?"无":entry.getLevel().toString());
			if(grade != null){
				map.put("grade",grade.getName());
			}else{
				map.put("grade","");
			}
			map.put("sn", entry.getStudyNum()==null?"":String.valueOf(entry.getStudyNum()));
			if(entry.getSex()==0) {
				map.put("sex", "女");
			}
			else if(entry.getSex()==1) {
				map.put("sex", "男");
			}
			map.put("group", entry.getCombiname()==null?"":entry.getCombiname());
			result.add(map);
		}
		return result;
	}

	public void exportStuSelectResult(ObjectId xqid, ObjectId gradeId, HttpServletResponse response){
        List<StudentEntry> studentEntryList = N33_StudentDao.getStudentByXqidAndGradeId(gradeId,xqid);

        Map<ObjectId, ClassEntry> classEntryMap = N33_ClassDao.findClassEntryMap(gradeId, xqid);
        Map<ObjectId, ClassEntry> classMap = classDao.getClassEntryMapByGradeId(gradeId, xqid);

        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("导入");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell = row.createCell(1);
        cell.setCellValue("学籍号");
		cell = row.createCell(2);
		cell.setCellValue("年级");
		cell = row.createCell(3);
		cell.setCellValue("班级序号");
        cell = row.createCell(4);
        cell.setCellValue("层级(非必填)");
        cell = row.createCell(5);
        cell.setCellValue("选科组合");

        for(int i=0;i<studentEntryList.size();i++) {
            StudentEntry entry = studentEntryList.get(i);
            HSSFRow row1 = sheet.createRow(i + 1);
            HSSFCell cell1 = row1.createCell(0);
            cell1.setCellValue(entry.getUserName2());
            cell1 = row1.createCell(1);
            cell1.setCellValue(entry.getStudyNum());
            cell1 = row1.createCell(2);
            cell1.setCellValue(entry.getGradeName());
            cell1 = row1.createCell(3);
            Integer xh = 0;
            if(null!=classMap.get(entry.getClassId())){
                xh = classMap.get(entry.getClassId()).getXh();
            }else{
                if(null!=classEntryMap.get(entry.getClassId())){
                    xh = classEntryMap.get(entry.getClassId()).getXh();
                }
            }
            cell1.setCellValue(xh);
	        cell1 = row1.createCell(4);
	        if(entry.getLevel() == 0 || entry.getLevel() == null){
		        cell1.setCellValue("无");
	        }else{
		        cell1.setCellValue(entry.getLevel());
	        }

	        cell1 = row1.createCell(5);
	        cell1.setCellValue(entry.getCombiname());
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
                response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("导出选科结果.xls", "UTF-8"));
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
}
