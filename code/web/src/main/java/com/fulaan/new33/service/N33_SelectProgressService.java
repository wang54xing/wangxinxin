package com.fulaan.new33.service;

import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.N33_ClassDao;
import com.db.new33.isolate.N33_StudentDao;
import com.fulaan.annotation.ObjectIdType;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.StudentEntry;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N33_SelectProgressService {
    private N33_StudentDao n33_studentDao = new N33_StudentDao();
    private ClassDao classDao = new ClassDao();

    public Map<String,Object> getGradeProgress(ObjectId sid,ObjectId ciId,ObjectId gradeId){
        Map<String,Object> result = new HashMap<String, Object>();
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        Map<String,Object> gradeInfo = new HashMap<String, Object>();
        DecimalFormat df = new DecimalFormat("#.00");
        Integer gradeStu = n33_studentDao.countStudentByXqidAndGradeId(gradeId,ciId);
        Integer 年级没选科 = n33_studentDao.countStudentByXqidAndGradeIdxuanke(gradeId,ciId);
        Integer 年级已选科 = gradeStu-年级没选科;
        if(gradeStu!=0){
            BigDecimal bg = new BigDecimal(年级已选科*1.0/gradeStu).setScale(2, RoundingMode.DOWN);
            double ratio = bg.doubleValue()*100;
            double width = bg.doubleValue()*1000;
            gradeInfo.put("ratio",ratio);
            gradeInfo.put("name","年级");
            gradeInfo.put("width",width);
            gradeInfo.put("color",ratio>70?"ruaBlue":(ratio>30)?"suYellow":"suRed");
        }
        else{
            gradeInfo.put("ratio",0);
            gradeInfo.put("name","年级");
            gradeInfo.put("width",0);
            gradeInfo.put("color","suRed");
        }
        list.add(gradeInfo);
        List<ClassEntry> classEntryList = classDao.findByGradeIdId(sid, gradeId, ciId);
        for(ClassEntry klass:classEntryList){
            Map<String,Object> klassInfo = new HashMap<String, Object>();
            Integer classStu = n33_studentDao.countStudentByXqidAndClassId(klass.getClassId(),ciId);
            Integer 班级没选科 = n33_studentDao.countStudentByXqidAndClassId(klass.getClassId(),ciId,null);
            Integer 班级已选科 = classStu-班级没选科;
            if(classStu!=0){
                BigDecimal bg = new BigDecimal(班级已选科*1.0/classStu).setScale(2, RoundingMode.DOWN);
                double ratio = bg.doubleValue()*100;
                double width =  bg.doubleValue()*1000;
                klassInfo.put("ratio",ratio);
                klassInfo.put("name",klass.getClassName());
                klassInfo.put("width",width);
                klassInfo.put("color",ratio>70?"ruaBlue":(ratio>30)?"suYellow":"suRed");
            }
            else{
                klassInfo.put("ratio",0);
                klassInfo.put("name",klass.getClassName());
                klassInfo.put("width",0);
                klassInfo.put("color","suRed");
            }
            list.add(klassInfo);
        }
        result.put("list",list);
        return result;
    }

    public List<Map<String,Object>> getStudentsByClassId(ObjectId ciId,String classId,ObjectId gradeId){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        List<StudentEntry> studentEntryList = null;
        if(classId.equals("grade")){
            studentEntryList = n33_studentDao.getStudentByXqidAndGradeIdAndCombine(gradeId,ciId,"");

        }
        else{
            studentEntryList = n33_studentDao.getStudentByXqidAndClassIdAndCombine(new ObjectId(classId),ciId,"");
        }
        for(StudentEntry studentEntry:studentEntryList){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id",studentEntry.getID().toString());
            map.put("nm",studentEntry.getUserName()); 
            map.put("sn",studentEntry.getStudyNum());
            ClassEntry classEntry = classDao.findIsolateClassEntryByCId(studentEntry.getClassId(),ciId);
            if(classEntry == null){
            	map.put("bj","");
            }else{
            	map.put("bj",studentEntry.getGradeName()+"("+classEntry.getXh()+")班");
            }
            
            map.put("cbn",studentEntry.getCombiname());
            if(studentEntry.getSex()==0) {
                map.put("sex", "女");
            }
            else if(studentEntry.getSex()==1) {
                map.put("sex", "男");
            }
            map.put("lev",studentEntry.getLevel()<=0?"无":studentEntry.getLevel());
            result.add(map);
        }
        return result;
    }
    public List<Map<String,Object>> getStudentsByName(ObjectId ciId,String name,ObjectId gradeId){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        List<StudentEntry> studentEntryList = n33_studentDao.getStudentByXqidAndStuname(ciId,gradeId,name,null);
        for(StudentEntry studentEntry:studentEntryList){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id",studentEntry.getID().toString());
            map.put("nm",studentEntry.getUserName());
            map.put("sn",studentEntry.getStudyNum());
            ClassEntry classEntry = classDao.findIsolateClassEntryByCId(studentEntry.getClassId(),ciId);
            map.put("bj",studentEntry.getGradeName()+"("+classEntry.getXh()+")班");
            map.put("cbn",studentEntry.getCombiname());
            if(studentEntry.getSex()==0) {
                map.put("sex", "女");
            }
            else if(studentEntry.getSex()==1) {
                map.put("sex", "男");
            }
            map.put("lev",studentEntry.getLevel()<=0?"无":studentEntry.getLevel());
            result.add(map);
        }
        return result;
    }

    public void export(HttpServletResponse response, @ObjectIdType ObjectId ciId, @ObjectIdType ObjectId gradeId){
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("未选名单");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell = row.createCell(1);
        cell.setCellValue("班级");
        cell = row.createCell(2);
        cell.setCellValue("学号");
        cell = row.createCell(3);
        cell.setCellValue("性别");
        cell = row.createCell(4);
        cell.setCellValue("层级");

        List<StudentEntry> studentEntryList = n33_studentDao.getStudentByXqidAndGradeIdAndCombine(gradeId,ciId,"");
        for(int i=0;i<studentEntryList.size();i++){
            StudentEntry studentEntry = studentEntryList.get(i);
            HSSFRow row2 = sheet.createRow(i+1);
            HSSFCell cell2 = row2.createCell(0);
            cell2.setCellValue(studentEntry.getUserName());
            cell2 = row2.createCell(1);
            ClassEntry classEntry = classDao.findIsolateClassEntryByCId(studentEntry.getClassId(),ciId);
            if(classEntry == null){
            	cell2.setCellValue("  ");
            }else{
            	cell2.setCellValue(studentEntry.getGradeName()+"("+classEntry.getXh()+")班");
            }
            cell2 = row2.createCell(2);
            cell2.setCellValue(studentEntry.getStudyNum());
            cell2 = row2.createCell(3);
            String sex = "";
            if(studentEntry.getSex()==0) {
                sex = "女";
            }
            else if(studentEntry.getSex()==1) {
                sex = "男";
            }
            cell2.setCellValue(sex);
            cell2 = row2.createCell(4);
            cell2.setCellValue(studentEntry.getLevel() <= 0 ? "无" : String.valueOf((Integer) studentEntry.getLevel()));
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("未选名单.xls", "UTF-8"));
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
}
