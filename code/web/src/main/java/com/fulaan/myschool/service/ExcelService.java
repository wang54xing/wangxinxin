package com.fulaan.myschool.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.fulaan.myschool.controller.ExcelStudentRecord;
import com.fulaan.myschool.controller.ExcelTeacherRecord;
import com.fulaan.myschool.controller.ExcelTeacherRecord.GradeAndClass;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;


/**
 * Created by Hao on 2015/5/6.
 */
public class ExcelService {

	private final static Logger logger =Logger.getLogger("IMPORTLOG");
	
	
    public static Map<String,Object> findTeacherAndStudentRecord(InputStream inputStream){
        Map<String,Object> returnMap=new HashMap<String, Object>();
        Map<String,ExcelTeacherRecord> teacherMap =new HashMap<String, ExcelTeacherRecord>();
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
            HSSFSheet teacherSheet = workbook.getSheet("教师信息");
            HSSFSheet studentSheet = workbook.getSheet("学生信息");
            int rowNum = teacherSheet.getLastRowNum();
            logger.info("Teacher rows number="+rowNum);
            
            for (int i = 1; i <= rowNum; i++) {
            	
            	try
            	{
		            	String userName  = getStringCellValue(teacherSheet.getRow(i).getCell(0));
		                String jobNum = getStringCellValue(teacherSheet.getRow(i).getCell(1));
		                String sex = getStringCellValue(teacherSheet.getRow(i).getCell(2));
		                String role = getStringCellValue(teacherSheet.getRow(i).getCell(3));
						String postionDesc = getStringCellValue(teacherSheet.getRow(i).getCell(4));
		                String subject = getStringCellValue(teacherSheet.getRow(i).getCell(5));
		                String gradeName = getStringCellValue(teacherSheet.getRow(i).getCell(6));
		                String gradeLevel = getStringCellValue(teacherSheet.getRow(i).getCell(7));
		                String className = getStringCellValue(teacherSheet.getRow(i).getCell(8));
		                String classLeader = getStringCellValue(teacherSheet.getRow(i).getCell(9));//是 或者 空
		                String studyLeader = getStringCellValue(teacherSheet.getRow(i).getCell(10));//学科名称 或者空
		                String gradeLeader = getStringCellValue(teacherSheet.getRow(i).getCell(11));//年级名称 或者空
		                String prepareLeader = getStringCellValue(teacherSheet.getRow(i).getCell(12));//是  或者空
		                
		                logger.info("Teacher Row index:"+i);
		                logger.info("[userName=" + userName + ", jobNum=" + jobNum + ", sex="
		    					+ sex + ", role=" + role+ ", postionDesc="+ postionDesc + ", subject=" + subject
		    					+ ", gradeName=" + gradeName + ", gradeLevel=" + gradeLevel
		    					+ ", className=" + className + ", classLeader="
		    					+ classLeader + ", studyLeader=" + studyLeader
		    					+ ", gradeLeader=" + gradeLeader + ", prepareLeader="
		    					+ prepareLeader + "]");
		                
		                
		                ExcelTeacherRecord excelTeacherRecord=new ExcelTeacherRecord(
		                		userName,
		                		jobNum,
		                		"男".equals(sex.trim())? 1 : 0,
		                		"老师".equals(role)?	UserRole.TEACHER.getRole():UserRole.HEADMASTER.getRole(),
								StringUtils.isEmpty(postionDesc)?role:postionDesc
		                		);
		               if(!teacherMap.containsKey(userName))
		               {
		            	   teacherMap.put(userName, excelTeacherRecord);
		               }
		               int gradeCode=0;
		               try
		               {
		            	   gradeCode=Integer.parseInt(gradeLevel.trim());
		               }catch(Exception ex)
		               {}
		               
		               if(StringUtils.isNotBlank(className))
		               {
		               GradeAndClass gradeAndClass=new GradeAndClass(subject, 
		            		   gradeName,
		            		   gradeCode,
		            		   className,
		            		   StringUtils.isNotBlank(classLeader), 
		            		   StringUtils.isNotBlank(studyLeader), 
		            		   StringUtils.isNotBlank(gradeLeader), 
		            		   StringUtils.isNotBlank(prepareLeader)
		            		   );
		               
		               teacherMap.get(userName).addGradeAndClass(gradeAndClass);
		               }
            	}catch(Exception ex)
            	{
            		logger.error("", ex);
            	}
            }
            rowNum=studentSheet.getLastRowNum();
            logger.info("Student rows number="+rowNum);
            List<ExcelStudentRecord> studentRecords=new ArrayList<ExcelStudentRecord>();
            for (int i = 1; i <= rowNum; i++){
            	
            	try
            	{
		                String xuejihao = getStringCellValue(studentSheet.getRow(i).getCell(0));
		                String gradeCode = getStringCellValue(studentSheet.getRow(i).getCell(1));
		                String className = getStringCellValue(studentSheet.getRow(i).getCell(2));
		                String studentNumber = getStringCellValue(studentSheet.getRow(i).getCell(3));
		                String userName = getStringCellValue(studentSheet.getRow(i).getCell(4));
		                String sex = getStringCellValue(studentSheet.getRow(i).getCell(5));
		                
		                
		                logger.info("Student Row index:"+i);
		                logger.info("[xuejihao=" + xuejihao + ", gradeCode=" + gradeCode + ", className="
		    					+ className + ", studentNumber=" + studentNumber + ", userName=" + userName
		    					+ ", sex=" + sex + "]");
		                
		                ExcelStudentRecord excelStudentRecord=new ExcelStudentRecord();
		                excelStudentRecord.setClassName(className);
		                excelStudentRecord.setGradeCode(Integer.parseInt(gradeCode.trim()));
		                excelStudentRecord.setSex("男".equals(sex.trim())? 1 : 0);
		                excelStudentRecord.setXuejiNumber(xuejihao);
		                excelStudentRecord.setStudentName(userName);
		                excelStudentRecord.setStudyNumber(studentNumber);
		
		                studentRecords.add(excelStudentRecord);
            	}catch(Exception ex)
            	{
            		logger.error("", ex);
            	}

            }
            returnMap.put("teacherList",new ArrayList<ExcelTeacherRecord>(teacherMap.values()));
            returnMap.put("studentList",studentRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnMap;
    }
    
    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private static String getStringCellValue(HSSFCell cell) {
    	 if(cell==null) return Constant.EMPTY;
        String strCell = Constant.EMPTY;
       
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
       
        return StringUtils.isBlank(strCell)?Constant.EMPTY:strCell;
    }
    
    
    public static void main(String []dsa) throws IOException {
    	

        
    	
//    	Map<String,Object> map=  findTeacherAndStudentRecord(new FileInputStream("d:\\123.xls"));
//    	
//    	List<ExcelTeacherRecord> list=(List<ExcelTeacherRecord>)map.get("teacherList");
//    	
//    	for(ExcelTeacherRecord etr:list)
//    	{
//    		logger.info(etr);
//    	}
//    	
//         List<ExcelStudentRecord> list1=(List<ExcelStudentRecord>)map.get("studentList");
//    	
//    	for(ExcelStudentRecord etr:list1)
//    	{
//    		logger.info(etr);
//    	}
    }
    
   
           
 
}
