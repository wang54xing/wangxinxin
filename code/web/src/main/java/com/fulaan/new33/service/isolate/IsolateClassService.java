package com.fulaan.new33.service.isolate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.db.new33.N33_TimeCombineDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.paike.N33_JXBDao;
import com.fulaan.dzbp.service.ClientClassRelaService;
import com.fulaan.new33.dto.isolate.GradeDTO;
import com.fulaan.new33.dto.isolate.StudentDTO;
import com.fulaan.new33.service.N33_ZKBService;
import com.pojo.dzbp.ClientClassRelaEntry;
import com.pojo.new33.isolate.*;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_TimeCombineEntry;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_ClassDao;
import com.db.new33.isolate.N33_StudentDao;
import com.fulaan.new33.dto.isolate.ClassInfoDTO;
import com.fulaan.utils.RESTAPI.bo.new33.isolate.isolateAPI;
import com.sys.constants.Constant;
import com.sys.utils.JsonUtil;

@Service
public class IsolateClassService extends BaseService{

    private N33_ClassDao classDao = new N33_ClassDao();
    private N33_StudentDao studentDao = new N33_StudentDao();
    private GradeDao gradeDao = new GradeDao();

    private TermDao termDao = new TermDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private N33_TimeCombineDao combineDao = new N33_TimeCombineDao();

    private IsolateGradeService gradeService = new IsolateGradeService();

    @Autowired
    private ClientClassRelaService clientClassRelaService;

    @Autowired
    private N33_ZKBService zkbService;

    /**
     * 新增班级
     *
     * @param dto
     */
    public void addClassDto(ClassInfoDTO dto) {
        classDao.addClassEntry(dto.getEntry());
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(new ObjectId(dto.getGid()), new ObjectId(dto.getXqid()));
        List<ObjectId> list = grade.getClassList();
        list.add(new ObjectId(dto.getClassId()));
        grade.setClassList(list);
        gradeDao.updateIsolateUserEntry(grade);
    }

    /**
     * 删除班级
     */
    public void deleteClass(ObjectId id) {
        ClassEntry classEntry = classDao.findClassEntryById(id);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(classEntry.getGradeId(), classEntry.getXQId());
        List<ObjectId> list = new ArrayList<ObjectId>();
        for (ObjectId ids : grade.getClassList()) {
            if (!ids.toString().equals(classEntry.getClassId().toString())) {
                list.add(ids);
            }
        }
        grade.setClassList(list);
        gradeDao.updateIsolateUserEntry(grade);
        classDao.DelClass(id);
    }

    /**
     * 修改班级
     *
     * @param dto
     */
    public void updateClass(ClassInfoDTO dto) {
        classDao.updateIsolateClassEntry(dto.getEntry());
        studentDao.updateStudentEntryByClassName(new ObjectId(dto.getXqid()),
                new ObjectId(dto.getClassId()), dto.getClassName());
    }

    /**
     * 根据学期，年级，和学校查找班级
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<ClassInfoDTO> getClassByXqAndGrade(ObjectId xqid, ObjectId sid,
                                                   ObjectId gid) {
        List<ClassEntry> classEntries = classDao
                .findClassEntryBySchoolIdAndGradeId(xqid, sid, gid);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gid, xqid);
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        for (ClassEntry entry : classEntries) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO(entry);
            if (grade != null) {
                classInfoDTO.setGname(grade.getName());
            } else {
                classInfoDTO.setGname("");
            }
            classInfoDTO.setStuCount(classInfoDTO.getStus().size());
            classInfoDTOList.add(classInfoDTO);
        }
        return classInfoDTOList;
    }

    public List<StudentDTO> getClassStudentListByJxbId(ObjectId jxbId, ObjectId classId) {
        N33_JXBEntry entry1 = jxbDao.getJXBById(jxbId);
        ObjectId xqid = entry1.getTermId();
        ClassEntry entry = classDao.findClassEntryByClassId(xqid, classId);
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        for (ObjectId stuId : entry.getStudentList()) {
            if (!entry1.getStudentIds().contains(stuId)) {
                stuIds.add(stuId);
            }
        }
        List<StudentEntry> studentEntryList = studentDao.getStudentByXqid(classId, xqid, stuIds);
        List<StudentDTO> studentDTOs = new ArrayList<StudentDTO>();
        if (entry1.getType() == 1) {
            for (StudentEntry entry2 : studentEntryList) {
                if(entry2.getSubjectId1()==null||entry2.getSubjectId2()==null||entry2.getSubjectId3()==null){
                    continue;
                }
                boolean bf = true;
                if (!entry2.getSubjectId1().toString().equals(entry1.getSubjectId().toString()) && !entry2.getSubjectId2().toString().equals(entry1.getSubjectId().toString()) && !entry2.getSubjectId3().toString().equals(entry1.getSubjectId().toString())) {
                    bf = false;
                }
                if (bf) {
                    StudentDTO dto = new StudentDTO(entry2);
                    studentDTOs.add(dto);
                }
            }
        }
        if (entry1.getType() == 2) {
            for (StudentEntry entry2 : studentEntryList) {
                if(entry2.getSubjectId1()==null||entry2.getSubjectId2()==null||entry2.getSubjectId3()==null){
                    continue;
                }
                boolean bf = true;
                if (entry2.getSubjectId1().toString().equals(entry1.getSubjectId().toString()) || entry2.getSubjectId2().toString().equals(entry1.getSubjectId().toString()) || entry2.getSubjectId3().toString().equals(entry1.getSubjectId().toString())) {
                    bf = false;
                }
                if (bf) {
                    StudentDTO dto = new StudentDTO(entry2);
                    studentDTOs.add(dto);
                }
            }
        }
        if (entry1.getType() == 3 || entry1.getType() == 6) {
            for (StudentEntry entry2 : studentEntryList) {
                StudentDTO dto = new StudentDTO(entry2);
                studentDTOs.add(dto);
            }
        }
        return studentDTOs;
    }

    public List<ClassInfoDTO> getClassListByJxbId(ObjectId jxbId) {
        N33_JXBEntry entry1 = jxbDao.getJXBById(jxbId);
        ObjectId gradeId = entry1.getGradeId();
        ObjectId xqid = entry1.getTermId();
        ObjectId classId = null;
        ObjectId sid = entry1.getSchoolId();
        //此处有疑问
//        if (entry1.getType() == 3 || entry1.getType() == 6) {
//            classId = entry1.getRelativeId();
//        }
        List<ClassEntry> classEntries = classDao
                .findClassEntryBySchoolIdAndGradeId(xqid, sid, gradeId, classId);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId, xqid);
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        for (ClassEntry entry : classEntries) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO(entry);
            if (grade != null) {
                classInfoDTO.setGname(grade.getName());
            } else {
                classInfoDTO.setGname("");
            }
            classInfoDTO.setStuCount(classInfoDTO.getStus().size());
            classInfoDTOList.add(classInfoDTO);
        }
        return classInfoDTOList;
    }

    public List<ClassInfoDTO> getZbJxbClassList(ObjectId jxbId) {
        N33_JXBEntry jxb = jxbDao.getJXBById(jxbId);
        ObjectId schoolId = jxb.getSchoolId();
        ObjectId gradeId = jxb.getGradeId();
        ObjectId subId = jxb.getSubjectId();
        ObjectId ciId = jxb.getTermId();
        int acClassType = jxb.getAcClassType();
        int jxbType = jxb.getType();
        List<StudentEntry> stuList = studentDao.getStudentByXqidAndGradeId(gradeId, ciId);
        List<StudentEntry> subStuList = new ArrayList<StudentEntry>();
        for (StudentEntry stu : stuList) {
            List<ObjectId> stuSubIds = new ArrayList<ObjectId>();
            if(null!=stu.getSubjectId1()){
                stuSubIds.add(stu.getSubjectId1());
            }
            if(null!=stu.getSubjectId2()){
                stuSubIds.add(stu.getSubjectId2());
            }
            if(null!=stu.getSubjectId3()){
                stuSubIds.add(stu.getSubjectId3());
            }
            if(jxbType==1){
                if(stuSubIds.contains(subId)){
                    subStuList.add(stu);
                }
            }else{
                if(!stuSubIds.contains(subId)){
                    subStuList.add(stu);
                }
            }
        }
        List<ObjectId> noUseStuIds = new ArrayList<ObjectId>();
        List<ObjectId> useStuIds = new ArrayList<ObjectId>();
        if (1 == acClassType) {
            int serail = jxb.getTimeCombSerial();
            List<N33_JXBEntry> zbJxbList = jxbDao.getJXBList(schoolId, gradeId, ciId, jxbType, acClassType, serail);
            List<N33_JXBEntry> xzJxbList = jxbDao.getFZBJXBList(schoolId, gradeId, subId, ciId, acClassType);
            for (N33_JXBEntry zbJxb : zbJxbList) {
                for (ObjectId stuId : zbJxb.getStudentIds()) {
                    if (!useStuIds.contains(stuId)) {
                        useStuIds.add(stuId);
                    }
                }
            }
            for (N33_JXBEntry xzJxb : xzJxbList) {
                for (ObjectId stuId : xzJxb.getStudentIds()) {
                    if (!useStuIds.contains(stuId)) {
                        useStuIds.add(stuId);
                    }
                }
            }
        } else {
            Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);
            List<N33_TimeCombineEntry> timeCombEntries = combineDao.getTimeCombineByType(ciId, schoolId, gradeId, jxbType);
            int serail = 0;
            for (N33_TimeCombineEntry timeComb : timeCombEntries) {
                List<N33_TimeCombineEntry.ZuHeList> zuHeList = timeComb.getZuHeList();
                for (N33_TimeCombineEntry.ZuHeList zuHe : zuHeList) {
                    if (null != zuHe.getJxbId()&&jxbId.equals(zuHe.getJxbId())) {
                        serail = zuHe.getSerial();
                        break;
                    }
                }
                if(serail>0){
                    break;
                }
            }
            for (N33_TimeCombineEntry timeComb : timeCombEntries) {
                List<N33_TimeCombineEntry.ZuHeList> zuHeList = timeComb.getZuHeList();
                for(N33_TimeCombineEntry.ZuHeList zuHe:zuHeList){
                    if(null != zuHe.getJxbId()&&zuHe.getSerial()==serail) {
                        N33_JXBEntry jxbEntry = jxbMap.get(zuHe.getJxbId());
                        if(null!=jxbEntry) {
                            for (ObjectId stuId : jxbEntry.getStudentIds()) {
                                if (!useStuIds.contains(stuId)) {
                                    useStuIds.add(stuId);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (StudentEntry stu : subStuList) {
            if (!useStuIds.contains(stu.getUserId())) {
                noUseStuIds.add(stu.getUserId());
            }
        }
        List<ClassEntry> classEntries = classDao.getClassEnties(schoolId, gradeId, ciId, noUseStuIds);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId, ciId);
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        for (ClassEntry entry : classEntries) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO(entry);
            if (grade != null) {
                classInfoDTO.setGname(grade.getName());
            } else {
                classInfoDTO.setGname("");
            }
            classInfoDTO.setStuCount(classInfoDTO.getStus().size());
            classInfoDTOList.add(classInfoDTO);
        }
        return classInfoDTOList;
    }

    public Object getZbJxbClassStuList(ObjectId jxbId, ObjectId classId) {
        N33_JXBEntry jxb = jxbDao.getJXBById(jxbId);
        ObjectId schoolId = jxb.getSchoolId();
        ObjectId gradeId = jxb.getGradeId();
        ObjectId subId = jxb.getSubjectId();
        ObjectId ciId = jxb.getTermId();
        int acClassType = jxb.getAcClassType();
        int jxbType = jxb.getType();

        ClassEntry classEntry = classDao.findClassEntryByClassId(ciId, classId);

        List<ObjectId> noUseStuIds = new ArrayList<ObjectId>();
        List<ObjectId> useStuIds = new ArrayList<ObjectId>();
        if (1 == acClassType) {
            int serail = jxb.getTimeCombSerial();
            List<N33_JXBEntry> zbJxbList = jxbDao.getJXBList(schoolId, gradeId, ciId, jxbType, acClassType, serail);
            List<N33_JXBEntry> xzJxbList = jxbDao.getFZBJXBList(schoolId, gradeId, subId, ciId, acClassType);
            for (N33_JXBEntry zbJxb : zbJxbList) {
                for (ObjectId stuId : zbJxb.getStudentIds()) {
                    if (!useStuIds.contains(stuId)) {
                        useStuIds.add(stuId);
                    }
                }
            }
            for (N33_JXBEntry xzJxb : xzJxbList) {
                for (ObjectId stuId : xzJxb.getStudentIds()) {
                    if (!useStuIds.contains(stuId)) {
                        useStuIds.add(stuId);
                    }
                }
            }
        } else {
            Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);
            List<N33_TimeCombineEntry> timeCombEntries = combineDao.getTimeCombineByType(ciId, schoolId, gradeId, jxbType);
            int serail = 0;
            for (N33_TimeCombineEntry timeComb : timeCombEntries) {
                List<N33_TimeCombineEntry.ZuHeList> zuHeList = timeComb.getZuHeList();
                for (N33_TimeCombineEntry.ZuHeList zuHe : zuHeList) {
                    if (null != zuHe.getJxbId()&&jxbId.equals(zuHe.getJxbId())) {
                        serail = zuHe.getSerial();
                        break;
                    }
                }
                if(serail>0){
                    break;
                }
            }
            for (N33_TimeCombineEntry timeComb : timeCombEntries) {
                List<N33_TimeCombineEntry.ZuHeList> zuHeList = timeComb.getZuHeList();
                for(N33_TimeCombineEntry.ZuHeList zuHe:zuHeList){
                    if(null != zuHe.getJxbId()&&zuHe.getSerial()==serail) {
                        N33_JXBEntry jxbEntry = jxbMap.get(zuHe.getJxbId());
                        if(null!=jxbEntry) {
                            for (ObjectId stuId : jxbEntry.getStudentIds()) {
                                if (!useStuIds.contains(stuId)) {
                                    useStuIds.add(stuId);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (ObjectId stuId : classEntry.getStudentList()) {
            if (!useStuIds.contains(stuId)) {
                noUseStuIds.add(stuId);
            }
        }
        List<StudentEntry> studentEntries = studentDao.getStudentByXqid(classId, ciId, noUseStuIds);
        List<StudentDTO> studentDTOs = new ArrayList<StudentDTO>();
        for (StudentEntry stuEntry : studentEntries) {
            List<ObjectId> stuSubIds = new ArrayList<ObjectId>();
            if(null!=stuEntry.getSubjectId1()){
                stuSubIds.add(stuEntry.getSubjectId1());
            }
            if(null!=stuEntry.getSubjectId2()){
                stuSubIds.add(stuEntry.getSubjectId2());
            }
            if(null!=stuEntry.getSubjectId3()){
                stuSubIds.add(stuEntry.getSubjectId3());
            }
            if (jxbType == 1) {
                if(stuSubIds.contains(subId)){
                    StudentDTO dto = new StudentDTO(stuEntry);
                    studentDTOs.add(dto);
                }
            }else{
                if(!stuSubIds.contains(subId)){
                    StudentDTO dto = new StudentDTO(stuEntry);
                    studentDTOs.add(dto);
                }
            }
        }
        return studentDTOs;
    }


    /**
     * 根据次id查询所有的班级（用作同步使用）
     *
     * @param sid
     * @param cid
     * @return
     */
    public List<ClassEntry> getClassEntryByCiId(ObjectId sid, ObjectId cid) {
        List<ClassEntry> entryList = classDao.getClassEntryByCiId(sid, cid);
        return entryList;
    }


    public List<ClassInfoDTO> getClassByXqAndGradeZKB(ObjectId xqid, ObjectId sid,
                                                      ObjectId gid) {
//        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
//        List<ObjectId> cid = new ArrayList<ObjectId>();
//        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
//        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
//            if(paiKeTimes.getIr()==0) {
//                cid.add(paiKeTimes.getID());
//            }
//        }
    	List<ObjectId> cid = new ArrayList<ObjectId>();
    	ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
    	cid.add(ciId);
        List<GradeDTO> gradeList = gradeService.getGradeListByXqidList(xqid, sid);
        List<ClassEntry> classEntries = classDao
                .findClassEntryBySchoolIdAndGradeId(cid, sid, gid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        for (ClassEntry entry : classEntries) {
            if (!cids.contains(entry.getClassId())) {
                ClassInfoDTO classInfoDTO = new ClassInfoDTO(entry);
                for (GradeDTO dto : gradeList) {
                    if (entry.getGradeId().toString().equals(dto.getGid())) {
                        classInfoDTO.setGname(dto.getGnm());
                    }
                }
                if (classInfoDTO.getGname() == null) {
                    classInfoDTO.setGname("");
                }
                classInfoDTO.setStuCount(classInfoDTO.getStus().size());
                classInfoDTOList.add(classInfoDTO);
                cids.add(entry.getClassId());
            }
        }
        return classInfoDTOList;
    }

    /**
     * 查询是否存在班级
     *
     * @param xqid
     * @param sid
     * @return
     */
    public Integer getCountByXqId(ObjectId xqid, ObjectId sid) {
        Integer count = classDao.getClassCount(sid, xqid);
        return count;
    }

    /**
     * 批量从2.0导入班级
     *
     * @param sid
     * @param xqid
     * @throws org.json.JSONException
     */
    public void getClassBySid(ObjectId sid, ObjectId xqid) throws JSONException {
        String result = isolateAPI.getIsolateClass(sid.toString());
        JSONObject resultJson = new JSONObject(result);
        Map map = (Map) JsonUtil.JSONToObj(resultJson.toString(), Map.class);
        List<Map<String, Object>> dtoList = (List<Map<String, Object>>) map
                .get("message");
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        for (Map<String, Object> dto : dtoList) {
            ClassInfoDTO dto1 = new ClassInfoDTO(new ObjectId().toString(),
                    (String) dto.get("sid"),
                    (String) dto.get("id"),
                    (String) dto.get("nm"),
                    xqid.toString(),
                    (String) dto.get("buid"),
                    (String) dto.get("bunm"),
                    (List<String>) dto.get("stus"),
                    (String) dto.get("gid"),
                    null,
                    0,
                    (Integer) dto.get("xh"));
            ClassEntry entry = dto1.getEntry();
            entries.add(entry);
        }
        classDao.add(entries);
    }

    /**
     * 批量从2.0导入班级
     *
     * @param sid
     * @param xqid
     * @throws org.json.JSONException
     */
    public void getClassByGid(ObjectId sid, ObjectId xqid,String gradeId) throws JSONException {
        String result = isolateAPI.getIsolateClass(sid.toString(),gradeId);
        JSONObject resultJson = new JSONObject(result);
        Map map = (Map) JsonUtil.JSONToObj(resultJson.toString(), Map.class);
        List<Map<String, Object>> dtoList = (List<Map<String, Object>>) map
                .get("message");
        List<ClassEntry> entries = new ArrayList<ClassEntry>();
        for (Map<String, Object> dto : dtoList) {
            ClassInfoDTO dto1 = new ClassInfoDTO(new ObjectId().toString(),
                    (String) dto.get("sid"),
                    (String) dto.get("id"),
                    (String) dto.get("nm"),
                    xqid.toString(),
                    (String) dto.get("buid"),
                    (String) dto.get("bunm"),
                    (List<String>) dto.get("stus"),
                    (String) dto.get("gid"),
                    null,
                    0,
                    (Integer) dto.get("xh"));
            ClassEntry entry = dto1.getEntry();
            entries.add(entry);
        }
        classDao.add(entries);
    }

    /**
     * 根据班级id查找班级
     *
     * @param id
     * @return
     */
    public ClassInfoDTO getClassById(ObjectId id) {
        ClassEntry classEntry = classDao.findClassEntryById(id);
        ClassInfoDTO classInfoDto = new ClassInfoDTO(classEntry);
        return classInfoDto;
    }

    /**
     * 导入班级
     *
     * @param inputStream
     * @param xqid
     */
    public Integer importClass(InputStream inputStream, ObjectId xqid, ObjectId sid) throws IOException {
        //年级
        Map<String, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMapKeyIsName(xqid, sid);
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet UserSheet = workbook.getSheet("班级列表");
        int rowNum = UserSheet.getLastRowNum();
        List<ClassEntry> classEntry = new ArrayList<ClassEntry>();
        for (int i = 1; i <= rowNum; i++) {
            ClassEntry entry = new ClassEntry();
            entry.setBeiZhu(null);
            entry.setBuId(new ObjectId());
            entry.setBuName("");
            entry.setCId(new ObjectId());
            entry.setID(new ObjectId());
            entry.setSchoolId(sid);
            entry.setXQId(xqid);
            entry.setUserList(new ArrayList<ObjectId>());
            entry.setType(1);
            //班级名称
            HSSFCell cell = UserSheet.getRow(i).getCell(0);
            if (!getStringCellValue(cell).equals("")) {
                //根据班级取班级名称
                entry.setClassName(getStringCellValue(cell).trim());
            } else {
                continue;
            }
            //年级
            cell = UserSheet.getRow(i).getCell(1);
            if (!getStringCellValue(cell).equals("")) {
                //取年级
                Grade grade = gradeMap.get(getStringCellValue(cell).trim());
                if (grade != null) {
                    entry.setGradeId(grade.getGradeId());
                    List<ObjectId> gradeList = grade.getClassList();
                    gradeList.add(entry.getClassId());
                    grade.setClassList(gradeList);
                    gradeDao.updateIsolateUserEntry(grade);
                } else {
                    continue;
                }
            }
            //序号
            cell = UserSheet.getRow(i).getCell(2);
            if (!getStringCellValue(cell).equals("")) {
                entry.setXh(new Double(getStringCellValue(cell)).intValue());
            } else {
                continue;
            }
            classEntry.add(entry);
        }
        classDao.add(classEntry);
        return 0;
    }

    /**
     * 生成导入班级的模板
     *
     * @param response
     */
    public void exportTemplate(HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("班级列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("班级名称");
        cell = row.createCell(1);
        cell.setCellValue("年级");
        cell = row.createCell(2);
        cell.setCellValue("序号");

        HSSFRow row2 = sheet.createRow(1);
        HSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("高一(1)班");
        cell2 = row2.createCell(1);
        cell2.setCellValue("高一");
        cell2 = row2.createCell(2);
        cell2.setCellValue(1);

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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("导入班级.xls", "UTF-8"));
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

    public ClassInfoDTO getClassInfoByClientId(String clientId) {
        ClassInfoDTO classDto = null;
        ClientClassRelaEntry clientClassRela = clientClassRelaService.getClientClassRelaEntry(clientId);
        if(null!=clientClassRela) {
            ObjectId schoolId = clientClassRela.getSchoolId();
            ObjectId classId = clientClassRela.getClassId();
            classDto = getClassInfo(schoolId, classId);
        }
        return classDto;
    }

    public Map getZhouKeBiaoListByClientId(String clientId) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        ClientClassRelaEntry clientClassRela = clientClassRelaService.getClientClassRelaEntry(clientId);
        if(null!=clientClassRela) {
            ObjectId schoolId = clientClassRela.getSchoolId();
            ObjectId classId = clientClassRela.getClassId();
            retMap = zkbService.getKeBiaoListByClassRoomIdForFulaan("", classId,"", "", schoolId);
        }
        return retMap;
    }

    public ClassInfoDTO getClassInfo(ObjectId schoolId, ObjectId classId) {
        ClassInfoDTO classDto = null;
        N33_DefaultTermEntry termEntry = getDefaultPaiKeTerm(schoolId);
        ObjectId ciId = null;
        if (null != termEntry) {
            ciId = termEntry.getPaikeci();
        }
        if (null != ciId) {
            ClassEntry classEntry = classDao.findClassEntryByClassId(ciId, classId);
            if (null != classEntry) {
                Grade gradeEntry = gradeDao.findIsolateGradeEntryByGradeId(classEntry.getGradeId(), ciId);
                classDto = new ClassInfoDTO(classEntry);
                String className = gradeEntry.getName() + "(" + classDto.getXh() + ")" + "班";
                classDto.setClassName(className);
            }
        }
        return classDto;
    }

    public Map<String, Object> getZhouKeBiaoList(ObjectId schoolId, ObjectId classId) {
        Map<String, Object> retMap = zkbService.getKeBiaoListByClassRoomIdForFulaan("", classId,"", "", schoolId);
        return retMap;
    }
}

