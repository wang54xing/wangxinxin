package com.fulaan.new33.service.isolate;

import com.db.new33.afresh.AfreshClassDao;
import com.db.new33.afresh.AfreshClassSetDao;
import com.db.new33.isolate.*;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.fulaan.new33.dto.isolate.ClassInfoDTO;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.dto.isolate.StudentDTO;
import com.fulaan.new33.service.SchoolSelectLessonSetService;
import com.fulaan.new33.service.SchoolSelectLessonSetService.SchoolLesson33DTO;
import com.fulaan.new33.service.SchoolSelectLessonSetService.lesson33DTO;
import com.fulaan.utils.RESTAPI.bo.new33.isolate.isolateAPI;
import com.hankcs.hanlp.seg.common.Term;
import com.pojo.new33.afresh.AfreshClassEntry;
import com.pojo.new33.afresh.AfreshClassSetEntry;
import com.pojo.new33.isolate.*;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.JsonUtil;

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
import org.zefer.html.doc.s;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by albin on 2018/3/16.
 */
@Service
public class N33_StudentService extends BaseService{

    private N33_StudentDao studentDao = new N33_StudentDao();
    private GradeDao gradeDao = new GradeDao();

    private TermDao termDao = new TermDao();
    private ClassDao classDao = new ClassDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private AfreshClassSetDao afreshClassSetDao = new AfreshClassSetDao();
    @Autowired
    private IsolateClassService classService;

    private SubjectDao subjectDao = new SubjectDao();

    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

    private AfreshClassDao afreshClassDao = new AfreshClassDao();

    private N33_ClassDao n33ClassDao = new N33_ClassDao();
    
    @Autowired
    private N33_TurnOffService turnOffService;

    @Autowired
    private SchoolSelectLessonSetService schoolSelectLessonSetService;
    


    public List<Map<String,Object>> getDJStudent(List<String> stuIds,ObjectId xqid,ObjectId gradeId){
        List<Map<String,Object>> studentDTOList = new ArrayList<Map<String,Object>>();
        List<StudentEntry> studentEntryList = studentDao.getStuList(MongoUtils.convertToObjectIdList(stuIds),xqid);
        Map<ObjectId,ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(gradeId,xqid);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId,xqid);

        for (StudentEntry studentEntry : studentEntryList) {
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
            studentDTOList.add(map);
        }
        return studentDTOList;
    }


    public List<lesson33DTO> getSelectLessons(ObjectId termId, ObjectId sid, ObjectId gradeId) {
        SchoolLesson33DTO schoolLesson33DTO = schoolSelectLessonSetService.getSchoolSelects(termId, sid, gradeId);
        List<lesson33DTO> listLesson33DTO = schoolLesson33DTO.getList();
        List<lesson33DTO> list = new ArrayList<lesson33DTO>();
        long time = System.currentTimeMillis();
        if (schoolLesson33DTO.getStartTime() != null && schoolLesson33DTO.getEndTime() != null) {
            if (listLesson33DTO.size() > 0) {
                for (lesson33DTO lesson33dto : listLesson33DTO) {
                    if (schoolLesson33DTO.getStartTime() <= time && schoolLesson33DTO.getEndTime() >= time) {
                        list.add(lesson33dto);
                    }
                }
            }
        }
        return list;
    }


    public List<lesson33DTO> getAllSelectLessons(ObjectId termId, ObjectId sid, ObjectId gradeId) {
        SchoolLesson33DTO schoolLesson33DTO = schoolSelectLessonSetService.getSchoolSelects(termId, sid, gradeId);
        List<lesson33DTO> listLesson33DTO = schoolLesson33DTO.getList();
        List<lesson33DTO> list = new ArrayList<lesson33DTO>();
        if (listLesson33DTO.size() > 0) {
            for (lesson33DTO lesson33dto : listLesson33DTO) {
                list.add(lesson33dto);
            }
        }
        return list;
    }

    /**
     * 同步学生和班级
     * @param sid
     * @param xqid
     */
    public void synchronizedStudent(ObjectId sid,ObjectId xqid,String gradeId) throws JSONException{
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,new ObjectId(gradeId),xqid,1);
        if (turnOff==null || turnOff.getAcClass()==0) {
            classDao.DelAllByGID(xqid,sid,new ObjectId(gradeId));
            classService.getClassByGid(sid, xqid,gradeId);
            Map<ObjectId,StudentEntry> studentEntryMap = studentDao.getStuMapIdEntry(xqid);
            studentDao.removeByCiId(sid,xqid,new ObjectId(gradeId));
            String result = isolateAPI.getIsolateUserByGid(sid.toString(),gradeId);
            JSONObject resultJson = new JSONObject(result);
            Map map1 = (Map) JsonUtil.JSONToObj(resultJson.toString(), Map.class);
            List<Map<String, Object>> dtoList1 = (List<Map<String, Object>>) map1.get("message");
            List<StudentEntry> entries = new ArrayList<StudentEntry>();
            for (Map<String, Object> dto : dtoList1) {
                if ((Integer) dto.get("tos") == 1) {
                    List<String> cls = (List<String>) dto.get("cid");
                    List<String> cln = (List<String>) dto.get("cnm");
                    List<String> gls = (List<String>) dto.get("gid");
                    List<String> gln = (List<String>) dto.get("gnm");
                    if (cls.size() > 0 && gls.size() > 0) {
                        StudentDTO dto1 = new StudentDTO(new ObjectId().toString(),
                                (String) dto.get("sid"),
                                xqid.toHexString(),
                                (String) dto.get("uid"),
                                (String) dto.get("unm"),
                                cls.get(0),
                                cln.get(0),
                                gls.get(0),
                                gln.get(0),
                                (String) dto.get("sn"),
                                0,
                                0,
                                (Integer) dto.get("sex"),
                                null,
                                null,
                                null,
                                null);
                        StudentEntry entry = dto1.buildEntry();
                        if(studentEntryMap.get(entry.getUserId()) != null){
                            entry.setCombiname(studentEntryMap.get(entry.getUserId()).getCombiname());
                            entry.setSubjectId1(studentEntryMap.get(entry.getUserId()).getSubjectId1());
                            entry.setSubjectId2(studentEntryMap.get(entry.getUserId()).getSubjectId2());
                            entry.setSubjectId3(studentEntryMap.get(entry.getUserId()).getSubjectId3());
                            entry.setLevel(studentEntryMap.get(entry.getUserId()).getLevel());
                        }
                        entries.add(entry);
                    }
                }
            }
            studentDao.add(entries);
        } else {
            classDao.DelAllByGID(xqid,sid,new ObjectId(gradeId));
            AfreshClassSetEntry afreshClassSetEntry = afreshClassSetDao.getEntryBySchoolId(xqid,sid,new ObjectId(gradeId));
            List<AfreshClassEntry> afreshClassEntryList = new ArrayList<AfreshClassEntry>();

            if(afreshClassSetEntry!=null){
                afreshClassEntryList = afreshClassDao.findByGradeIdId(afreshClassSetEntry.getID());
            }
            Map<ObjectId,StudentEntry> stuMap = new HashMap<ObjectId, StudentEntry>();
            List<StudentEntry> studentEntries = studentDao.getStuByGid(xqid,new ObjectId(gradeId));
            if (studentEntries!=null && studentEntries.size()!=0) {
                for (StudentEntry entry : studentEntries) {
                    stuMap.put(entry.getUserId(),entry);
                }
            }
            List<ClassEntry> classEntries = new ArrayList<ClassEntry>();
            if (afreshClassEntryList!=null && afreshClassEntryList.size()!=0) {
                for (AfreshClassEntry entry : afreshClassEntryList) {
                    ClassEntry ce = new ClassEntry(entry.getXQId(),sid,entry.getName(),entry.getStudentList(),
                            entry.getMentorName(),entry.getID(),entry.getType(),entry.getGradeId(),entry.getBeiZhu(),entry.getXh());
                    ce.setID(entry.getID());
                    ce.setMentorId(entry.getMentorId());
                    List<ObjectId> userIds = entry.getStudentList();
                    if (userIds!=null && userIds.size()!=0) {
                        for (ObjectId uid : userIds) {
                            StudentEntry st = stuMap.get(uid);
                            st.setClassId(entry.getID());
                            st.setClassName(entry.getClassName());
                            studentDao.updateStudentEntry(st);
                        }
                    }
//                    ce.setUserList(new ArrayList<ObjectId>());
                    classEntries.add(ce);
                }
                n33ClassDao.add(classEntries);
            }
        }
    }

    /**
     * 批量从2.0导入学生
     *
     * @param sid
     * @param xqid
     * @throws org.json.JSONException
     */
    public void getStudentBySid(ObjectId sid, ObjectId xqid) throws JSONException {
        String result = isolateAPI.getIsolateUserBySid(sid.toString());
        JSONObject resultJson = new JSONObject(result);
        Map map1 = (Map) JsonUtil.JSONToObj(resultJson.toString(), Map.class);
        List<Map<String, Object>> dtoList1 = (List<Map<String, Object>>) map1.get("message");
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        for (Map<String, Object> dto : dtoList1) {
            if ((Integer) dto.get("tos") == 1) {
                List<String> cls = (List<String>) dto.get("cid");
                List<String> cln = (List<String>) dto.get("cnm");
                List<String> gls = (List<String>) dto.get("gid");
                List<String> gln = (List<String>) dto.get("gnm");
                if (cls.size() > 0 && gls.size() > 0) {
                    StudentDTO dto1 = new StudentDTO(new ObjectId().toString(),
                            (String) dto.get("sid"),
                            xqid.toHexString(),
                            (String) dto.get("uid"),
                            (String) dto.get("unm"),
                            cls.get(0),
                            cln.get(0),
                            gls.get(0),
                            gln.get(0),
                            (String) dto.get("sn"),
                            0,
                            0,
                            (Integer) dto.get("sex"),
                            null,
                            null,
                            null,
                            null);
                    StudentEntry entry = dto1.buildEntry();

                    entries.add(entry);
                }
            }
        }
        studentDao.add(entries);
    }

    /**
     * 根据学生的姓名或者学籍号查询2.0学生
     *
     * @return
     * @throws JSONException
     */
    public List<StudentDTO> getIsolateStudentByNameOrCn(String userName, ObjectId sid, ObjectId xqid, ObjectId gradeId, ObjectId classId) throws JSONException {
        String result = isolateAPI.getIsolateStudentByNameOrCn(userName, sid, gradeId, classId);
        JSONObject resultJson = new JSONObject(result);
        Map map1 = (Map) JsonUtil.JSONToObj(resultJson.toString(), Map.class);
        List<Map<String, Object>> dtoList1 = (List<Map<String, Object>>) map1.get("message");
        List<StudentDTO> entries = new ArrayList<StudentDTO>();
        Map<ObjectId, StudentEntry> map = studentDao.getStuMapKeyIsUidByClass(xqid, classId);

        for (Map<String, Object> dto : dtoList1) {
            String cid = (String) dto.get("cid");
            String cnm = (String) dto.get("cnm");
            String gid = (String) dto.get("gid");
            String gnm = (String) dto.get("gnm");
            if (cid != "" && cid != null && gid != "" && gid != null) {
                StudentDTO dto1 = new StudentDTO(new ObjectId().toString(),
                        (String) dto.get("sid"),
                        xqid.toHexString(),
                        (String) dto.get("uid"),
                        (String) dto.get("unm"),
                        cid,
                        cnm,
                        gid,
                        gnm,
                        (String) dto.get("sn"),
                        0,
                        0,
                        (Integer) dto.get("sex"),
                        null,
                        null,
                        null,
                        null);
                if (dto1.getSex() == 0) {
                    dto1.setSexStr("女");
                } else {
                    dto1.setSexStr("男");
                }
                if (map.get(new ObjectId(dto1.getUserId())) == null) {
                    entries.add(dto1);
                }
            }
        }

        return entries;
    }

    /**
     * 批量增加学生
     *
     * @param studentList
     */
    public void addStudentList(List<StudentDTO> studentList) {
        List<StudentEntry> entries = new ArrayList<StudentEntry>();
        List<ObjectId> list = new ArrayList<ObjectId>();
        String cid = studentList.get(0).getClassId();
        String xqid = studentList.get(0).getXqid();
        for (StudentDTO studentDTO : studentList) {
            StudentEntry entry = studentDTO.buildEntry();
            entries.add(entry);
            list.add(new ObjectId(studentDTO.getUserId()));
        }
        ClassEntry classEntry = null;
        if (cid != null && cid != "" && xqid != null && xqid != "") {
            classEntry = classDao.findIsolateClassEntryByCId(new ObjectId(cid), new ObjectId(xqid));
        }
        if (classEntry != null) {
            List<ObjectId> stuList = classEntry.getStudentList();
            list.addAll(stuList);
            classEntry.setUserList(list);
            classDao.updateIsolateClassEntry(classEntry);
        }
        studentDao.add(entries);
    }


    public void addStudent(StudentDTO dto) {
        StudentEntry entry = studentDao.findStudent(new ObjectId(dto.getId()));
        if (entry != null) {
            studentDao.updateStudentEntry(dto.buildEntry());
        } else {
            studentDao.addStudentEntry(dto.buildEntry());
        }
    }

    public void updateLevel(Integer level, String id) {
        StudentEntry entry = studentDao.findStudent(new ObjectId(id));
        entry.setLevel(level);
        studentDao.updateStudentEntry(entry);

    }


    /**
     * 修改学生选课
     *
     * @param id
     * @param subjectId1
     * @param subjectId2
     * @param subjectId3
     * @param name
     */
    public void updateStudent(ObjectId id, ObjectId subjectId1, ObjectId subjectId2, ObjectId subjectId3, String name) {
        StudentEntry entry = studentDao.findStudent(id);
        entry.setSubjectId1(subjectId1);
        entry.setSubjectId2(subjectId2);
        entry.setSubjectId3(subjectId3);
        entry.setCombiname(name);
        studentDao.updateStudentEntry(entry);
    }

    public Map<String,Object> updateStudentAndJXB(ObjectId id, ObjectId subjectId1, ObjectId subjectId2, ObjectId subjectId3, String name) {
        Map<String,Object> retMap = new HashMap<String, Object>();
        StudentEntry entry = studentDao.findStudent(id);
        //学生原来的教学班
        //jxb add
        ObjectId ciId = getDefaultPaiKeTerm(entry.getSchoolId()).getPaikeci();
        int acClassType = turnOffService.getAcClassType(entry.getSchoolId(), entry.getGradeId(), ciId,1);
        //jxb add
        List<N33_JXBEntry> oJxbEntryList = jxbDao.getStuJXBList(entry.getGradeId(), entry.getXqid(), entry.getUserId(),acClassType);
        //原来的教学班名字
        String oJxbName = "";
        for (N33_JXBEntry jxbEntry : oJxbEntryList) {
            if(jxbEntry.getType() == 1 || jxbEntry.getType() == 2){
                jxbEntry.getStudentIds().remove(entry.getUserId());
                jxbDao.updateN33_JXB(jxbEntry);
                if(jxbEntry.getNickName() == null || "".equals(jxbEntry.getNickName())){
                    if("".equals(oJxbName)){
                        oJxbName += jxbEntry.getName();
                    }else{
                        oJxbName += "  ";
                        oJxbName += jxbEntry.getName();
                    }
                }else{
                    if("".equals(oJxbName)){
                        oJxbName += jxbEntry.getNickName();
                    }else{
                        oJxbName += "  ";
                        oJxbName += jxbEntry.getNickName();
                    }
                }
            }
        }
        //同一个行政班中选科一样的学生
        List<StudentEntry> sameClassStuList = studentDao.getStuListByCombineName(entry.getXqid(),entry.getClassId(),name);
        //现在的教学班名字
        String nJxbName = "";
        if(sameClassStuList.size() > 0){
            //在同一个行政班中找选科一样的模板
            StudentEntry studentEntry = null;
            while (true) {
                int count = (int)(Math.random() * sameClassStuList.size());
                studentEntry = sameClassStuList.get(count);
                if(!entry.getUserId().toString().equals(studentEntry.getUserId())){
                    break;
                }
            }

            //和更改学生同选科的教学班
            List<N33_JXBEntry> nJxbEntryList = jxbDao.getStuJXBList(entry.getGradeId(), entry.getXqid(), studentEntry.getUserId(),acClassType);

            for (N33_JXBEntry jxbEntry : nJxbEntryList) {
                if(jxbEntry.getType() == 1 || jxbEntry.getType() == 2){
                    jxbEntry.getStudentIds().add(entry.getUserId());
                    jxbDao.updateN33_JXB(jxbEntry);
                    if(jxbEntry.getNickName() == null || "".equals(jxbEntry.getNickName())){
                        if("".equals(nJxbName)){
                            nJxbName += jxbEntry.getName();
                        }else{
                            nJxbName += "  ";
                            nJxbName += jxbEntry.getName();
                        }
                    }else{
                        if("".equals(nJxbName)){
                            nJxbName += jxbEntry.getNickName();
                        }else{
                            nJxbName += "  ";
                            nJxbName += jxbEntry.getNickName();
                        }
                    }
                }
            }
        }else{
            List<StudentEntry> classStuList = studentDao.getStuListByCombineName(entry.getXqid(),null,name);
            //不同行政班中选科一样的学生
            List<StudentEntry> diffClassStuList = studentDao.getStuListByCombineName(entry.getXqid(),null,name);
            for (StudentEntry studentEntry : classStuList) {
                if(!studentEntry.getClassId().equals(entry.getClassId().toString())){
                    diffClassStuList.add(studentEntry);
                }
            }
            if(diffClassStuList.size() > 0){
                //在不同行政班中找选科一样的模板
                StudentEntry studentEntry = null;
                while (true) {
                    int count = (int)(Math.random() * diffClassStuList.size());
                    studentEntry = diffClassStuList.get(count);
                    if(!entry.getUserId().toString().equals(studentEntry.getUserId())){
                        break;
                    }
                }
                //和更改学生同选科的教学班
                List<N33_JXBEntry> nJxbEntryList = jxbDao.getStuJXBList(entry.getGradeId(), entry.getXqid(), studentEntry.getUserId(),acClassType);

                for (N33_JXBEntry jxbEntry : nJxbEntryList) {
                    if(jxbEntry.getType() == 1 || jxbEntry.getType() == 2){
                        jxbEntry.getStudentIds().add(entry.getUserId());
                        jxbDao.updateN33_JXB(jxbEntry);
                        if(jxbEntry.getNickName() == null || "".equals(jxbEntry.getNickName())){
                            if("".equals(nJxbName)){
                                nJxbName += jxbEntry.getName();
                            }else{
                                nJxbName += "  ";
                                nJxbName += jxbEntry.getName();
                            }
                        }else{
                            if("".equals(nJxbName)){
                                nJxbName += jxbEntry.getNickName();
                            }else{
                                nJxbName += "  ";
                                nJxbName += jxbEntry.getNickName();
                            }
                        }
                    }
                }
            }
        }

        String oCombineName = entry.getCombiname();
        entry.setSubjectId1(subjectId1);
        entry.setSubjectId2(subjectId2);
        entry.setSubjectId3(subjectId3);
        entry.setCombiname(name);
        studentDao.updateStudentEntry(entry);
        retMap.put("oJxbName",oJxbName);
        retMap.put("nJxbName",nJxbName);
        retMap.put("stuName",entry.getUserName());
        retMap.put("combine",oCombineName);
        return retMap;
    }



    public void deleteStudent(ObjectId id) {
        StudentEntry entry = studentDao.findStudent(id);
        ClassEntry classEntry = classDao.findIsolateClassEntryByCId(entry.getClassId(), entry.getXqid());
        List<ObjectId> clsIds = new ArrayList<ObjectId>();
        for (ObjectId ids : classEntry.getStudentList()) {
            if (!ids.toString().equals(entry.getUserId().toString())) {
                clsIds.add(ids);
            }
        }
        classEntry.setUserList(clsIds);
        classDao.updateIsolateClassEntry(classEntry);
        studentDao.DelStudent(id);
    }

    public List<StudentDTO> getStudentDTO(List<String> stuIds,ObjectId sid){
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        if(xqid == null){
            List<StudentDTO> dtos = new ArrayList<StudentDTO>();
            return dtos;
        }
        List<ObjectId> studentIds = MongoUtils.convertToObjectIdList(stuIds);
        List<StudentEntry> studentEntryList = studentDao.getStuListByUserIds(studentIds,xqid,sid);
        List<StudentDTO> dtos = new ArrayList<StudentDTO>();
        for (StudentEntry entry : studentEntryList) {
            StudentDTO studentDTO = new StudentDTO(entry);
            if (studentDTO.getSex() == 0) {
                studentDTO.setSexStr("女");
            } else if(studentDTO.getSex() == 1){
                studentDTO.setSexStr("男");
            }else{
                studentDTO.setSexStr("");
            }
            dtos.add(studentDTO);
        }
        return dtos;
    }

    public List<StudentDTO> getStudentDtoByClassId(ObjectId xqid, ObjectId classId) {
        List<StudentEntry> studentEntryList = studentDao.getStudentByXqidAndClassId(classId, xqid);
        List<StudentDTO> dtos = new ArrayList<StudentDTO>();
        for (StudentEntry entry : studentEntryList) {
            StudentDTO studentDTO = new StudentDTO(entry);
            if (studentDTO.getSex() == 0) {
                studentDTO.setSexStr("女");
            } else if(studentDTO.getSex() == 1){
                studentDTO.setSexStr("男");
            }else{
                studentDTO.setSexStr("");
            }
            dtos.add(studentDTO);
        }
        return dtos;
    }

    public List<StudentDTO> getStudentDtoByClassIdForZhuanXiang(ObjectId xqid, ObjectId classId,Integer sex,Integer level,String combine) {
        List<StudentEntry> studentEntryList = studentDao.getStudentByXqidAndClassId(classId, xqid);
        List<StudentDTO> dtos = new ArrayList<StudentDTO>();
        for (StudentEntry entry : studentEntryList) {
            StudentDTO studentDTO = new StudentDTO(entry);
            if (studentDTO.getSex() == 0) {
                studentDTO.setSexStr("女");
            } else if(studentDTO.getSex() == 1){
                studentDTO.setSexStr("男");
            }else{
                studentDTO.setSexStr("");
            }
            if(sex != -1 && sex != entry.getSex()){
                continue;
            }
            if(level != -1 && level != 0 && level != entry.getLevel()){
                continue;
            }else if(level == 0 && entry.getLevel() != null && entry.getLevel() != 0){
                continue;
            }
            if(!"*".equals(combine) && !combine.equals(entry.getCombiname())){
                continue;
            }
            dtos.add(studentDTO);
        }
        return dtos;
    }

    public List<StudentEntry> getStudentListByCidForEntry(ObjectId cid, ObjectId sid) {
        List<StudentEntry> list = new ArrayList<StudentEntry>();
        list = studentDao.getStuListBySidAndCiId(cid, sid);
        return list;
    }


    public List<StudentDTO> getStudentDtoByClassIdZKB(ObjectId xqid, ObjectId classId) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cid = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cid.add(paiKeTimes.getID());
            }
        }
        Map<ObjectId, StudentEntry> studentEntryList = studentDao.getStudentByXqidAndClassId(cid, classId);
        List<StudentDTO> dtos = new ArrayList<StudentDTO>();
        for (StudentEntry entry : studentEntryList.values()) {
            StudentDTO studentDTO = new StudentDTO(entry);
            if (studentDTO.getSex() == 0) {
                studentDTO.setSexStr("女");
            } else {
                studentDTO.setSexStr("男");
            }
            dtos.add(studentDTO);
        }
        return dtos;
    }

    public List<StudentDTO> getStudentDtoByClassIdZKBForExport(ObjectId xqid, String classId,ObjectId gid) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cid = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cid.add(paiKeTimes.getID());
            }
        }
        Map<ObjectId, StudentEntry> studentEntryList = studentDao.getStudentDtoByClassIdZKBForExport(cid, classId,gid);
        List<StudentDTO> dtos = new ArrayList<StudentDTO>();
        for (StudentEntry entry : studentEntryList.values()) {
            StudentDTO studentDTO = new StudentDTO(entry);
            if (studentDTO.getSex() == 0) {
                studentDTO.setSexStr("女");
            } else {
                studentDTO.setSexStr("男");
            }
            dtos.add(studentDTO);
        }
        return dtos;
    }

    public List<StudentDTO> getStudentDtoByGradeId(ObjectId xqid, ObjectId gradeId) {
        List<StudentEntry> studentEntryList = studentDao.getStudentByXqidAndGradeId(gradeId, xqid);
        List<StudentDTO> dtos = new ArrayList<StudentDTO>();
        for (StudentEntry entry : studentEntryList) {
            dtos.add(new StudentDTO(entry));
        }
        return dtos;
    }

    public Map<String,Map<String,Object>> getDifferenceXQ(ObjectId ciId1,ObjectId ciId2,ObjectId sid){
        Map<String,Map<String,Object>> retMap = new HashMap<String, Map<String,Object>>();
        List<TermEntry> termEntries = termDao.getIsolateTermEntrysBySid(sid);
        for (TermEntry termEntry : termEntries) {
            for (TermEntry.PaiKeTimes paiKeTimes : termEntry.getPaiKeTimes()) {
                if(ciId1.toString().equals(paiKeTimes.getID().toString()) && paiKeTimes.getIr() == 0){
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("xqname",termEntry.getXqName());
                    map.put("ciName","第" + paiKeTimes.getSerialNumber() + "次选科");
                    retMap.put("ciId1Name",map);
                }
                if(ciId2.toString().equals(paiKeTimes.getID().toString()) && paiKeTimes.getIr() == 0){
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("xqname",termEntry.getXqName());
                    map.put("ciName","第" + paiKeTimes.getSerialNumber() + "次选科");
                    retMap.put("ciId2Name",map);
                }
            }
        }
        return retMap;
    }

    public List<Map<String,Object>> getDifference(ObjectId ciId1,ObjectId ciId2,ObjectId classId,ObjectId sid){
        List<TermEntry> termEntries = termDao.getIsolateTermEntrysBySid(sid);
        String ciId1Name = "";
        String ciId2Name = "";
        for (TermEntry termEntry : termEntries) {
            for (TermEntry.PaiKeTimes paiKeTimes : termEntry.getPaiKeTimes()) {
                if(ciId1.toString().equals(paiKeTimes.getID().toString()) && paiKeTimes.getIr() == 0){
                    ciId1Name = termEntry.getXqName() + "\r\n第" + paiKeTimes.getSerialNumber() + "次选科";
                }
                if(ciId2.toString().equals(paiKeTimes.getID().toString()) && paiKeTimes.getIr() == 0){
                    ciId2Name = termEntry.getXqName() + "\r\n第" + paiKeTimes.getSerialNumber() + "次选科";
                }
            }
        }

        List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();
        ClassEntry classEntry1 = classDao.findIsolateClassEntryByCId(classId,ciId1);
        ClassEntry classEntry2 = classDao.findIsolateClassEntryByCId(classId,ciId2);
        List<ObjectId> stuIds1 = classEntry1.getStudentList();
        List<ObjectId> stuIds2 = null;
        if(classEntry2 == null){
            stuIds2 = new ArrayList<ObjectId>();
        }else{
            stuIds2 = classEntry2.getStudentList();
        }

        Map<ObjectId,ClassEntry> classEntryMap1 = classDao.findClassEntryMapBySchoolId(sid,ciId1);
        Map<ObjectId,ClassEntry> classEntryMap2 = classDao.findClassEntryMapBySchoolId(sid,ciId2);

        List<StudentEntry> studentEntryList1 = studentDao.getStuList(stuIds1,ciId1);
        List<StudentEntry> studentEntryList2 = studentDao.getStuList(stuIds2,ciId2);

        List<ObjectId> xqids = new ArrayList<ObjectId>();
        xqids.add(ciId1);
        xqids.add(ciId2);
        Map<ObjectId,N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIdsMapSubIdByCids(xqids,null,sid);
        List<ObjectId> alreadyStudent = new ArrayList<ObjectId>();
        Map<ObjectId,Grade> gradeMap1 = gradeDao.findGradeListBySchoolIdMap(ciId1,sid);
        Map<ObjectId,Grade> gradeMap2 = gradeDao.findGradeListBySchoolIdMap(ciId2,sid);
        for (StudentEntry studentEntry1 : studentEntryList1) {
            alreadyStudent.add(studentEntry1.getUserId());
            boolean flag = false;
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("name",studentEntry1.getUserName());
            map.put("className",gradeMap1.get(studentEntry1.getGradeId()).getName() + "(" + classEntryMap1.get(studentEntry1.getClassId()).getXh() + ")班");
            map.put("studentNumber",studentEntry1.getStudyNum());
            if(studentEntry1.getSex() == 0){
                map.put("sexStr","女");
            }else if(studentEntry1.getSex() == 1){
                map.put("sexStr","男");
            }else{
                map.put("sexStr","");
            }
            map.put("combineName1",studentEntry1.getCombiname());
            map.put("userId",studentEntry1.getUserId().toString());
            for (StudentEntry studentEntry2 : studentEntryList2) {
                if(studentEntry1.getUserId().toString().equals(studentEntry2.getUserId().toString())){
                    flag = true;
                    map.put("combineName2",studentEntry2.getCombiname());
                    if(studentEntry1.getCombiname().equals(studentEntry2.getCombiname())){
                        map.put("different","无");
                        map.put("flag",1);
                    }else{
                        String different = getDifferentSub(studentEntry1,studentEntry2,ksEntryMap);
                        map.put("different",different);
                        map.put("flag",2);
                    }
                    map.put("remark","");
                }
            }
            if(!flag){
                map.put("flag",2);
                map.put("combineName2","");
                map.put("different","");
                map.put("remark", studentEntry1.getUserName() + "在" + ciId2Name + "中不存在");
            }
            retList.add(map);
        }

        for (StudentEntry studentEntry2 : studentEntryList2) {
            if(!alreadyStudent.contains(studentEntry2.getUserId())){
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("name",studentEntry2.getUserName());
                map.put("className",gradeMap2.get(studentEntry2.getGradeId()).getName() + "(" + classEntryMap2.get(studentEntry2.getClassId()).getXh() + ")班");
                map.put("studentNumber",studentEntry2.getStudyNum());
                if(studentEntry2.getSex() == 0){
                    map.put("sexStr","女");
                }else if(studentEntry2.getSex() == 1){
                    map.put("sexStr","男");
                }else{
                    map.put("sexStr","");
                }
                map.put("combineName2",studentEntry2.getCombiname());
                map.put("userId",studentEntry2.getUserId().toString());
                map.put("combineName1","");
                map.put("different","");
                map.put("remark", studentEntry2.getUserName() + "在" + ciId1Name + "中不存在");
                map.put("flag",2);
                retList.add(map);
            }
        }
        return retList;
    }

    /**
     * 查询不同的选科
     * @param studentEntry1
     * @param studentEntry2
     * @param ksEntryMap
     * @return
     */
    private String getDifferentSub(StudentEntry studentEntry1,StudentEntry studentEntry2,Map<ObjectId,N33_KSEntry> ksEntryMap){
        List<ObjectId> subIds1 = new ArrayList<ObjectId>();
        subIds1.add(studentEntry1.getSubjectId1());
        subIds1.add(studentEntry1.getSubjectId2());
        subIds1.add(studentEntry1.getSubjectId3());
        List<ObjectId> subIds2 = new ArrayList<ObjectId>();
        subIds2.add(studentEntry2.getSubjectId1());
        subIds2.add(studentEntry2.getSubjectId2());
        subIds2.add(studentEntry2.getSubjectId3());
        String different = "";
        for (ObjectId subId : subIds1) {
            if(!subIds2.contains(subId) && subId != null && !"".equals(subId.toString())){
                if("".equals(different)){
                    different += ksEntryMap.get(subId).getSubjectName();
                }else{
                    different += ",";
                    different += ksEntryMap.get(subId).getSubjectName();
                }
            }
        }

        for (ObjectId subId : subIds2) {
            if(!subIds1.contains(subId) && subId != null && !"".equals(subId.toString())){
                if("".equals(different)){
                    different += ksEntryMap.get(subId).getSubjectName();
                }else{
                    different += ",";
                    different += ksEntryMap.get(subId).getSubjectName();
                }
            }
        }
        return different;
    }

    /**
     * 根据学生姓名查找学生（模糊查询）
     *
     * @param xqid
     * @param name
     * @return
     */
    public List<StudentDTO> getStudentByName(ObjectId xqid, ObjectId cid, ObjectId sid, String name) {
        List<StudentDTO> dtos = new ArrayList<StudentDTO>();
        ClassEntry classEntry = classDao.findClassEntryByClassId(xqid, cid);
        if(null!=classEntry) {
            List<ObjectId> studentIds = classEntry.getStudentList();
            List<StudentEntry> studentEntryList = studentDao.getStuList(studentIds, xqid, name);
            //List<StudentEntry> studentEntryList = studentDao.getStudentByXqidAndName(xqid, cid, sid, name);
            List<ObjectId> updUserIds = new ArrayList<ObjectId>();
            for (StudentEntry entry : studentEntryList) {
                dtos.add(new StudentDTO(entry));
                if (!cid.equals(entry.getClassId())) {
                    updUserIds.add(entry.getUserId());
                }
            }
            if (updUserIds.size() > 0) {
                studentDao.updateStuClassId(updUserIds, xqid, cid);
            }
        }
        return dtos;
    }

    public StudentDTO getStudentDto(ObjectId xqid, ObjectId uid) {
        StudentEntry entry = studentDao.findStudent(xqid, uid);
        return new StudentDTO(entry);
    }

    public StudentDTO getStudentDto(ObjectId id) {
        StudentEntry entry = studentDao.findStudent(id);
        return new StudentDTO(entry);
    }


    public Integer getCountByXqId(ObjectId xqid, ObjectId sid) {
        return studentDao.getStudentCount(sid, xqid);
    }

    /**
     * 导入学生
     *
     * @param inputStream
     * @param xqid
     */
    public List<Map<String, Object>> importUser(InputStream inputStream, ObjectId xqid, ObjectId sid) throws IOException {
        //返回的List
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        //年级
        Map<String, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMapKeyIsName(xqid, sid);
        //班级
        List<ClassEntry> classList = classDao.findClassListBySchoolId(sid, xqid);
        //学科
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet UserSheet = workbook.getSheet("用户列表");
        int rowNum = UserSheet.getLastRowNum();
        List<StudentEntry> studentEntry = new ArrayList<StudentEntry>();
        for (int i = 1; i <= rowNum; i++) {
            StudentEntry entry = new StudentEntry();
            entry.setSubjectId1(null);
            entry.setSubjectId2(null);
            entry.setSubjectId3(null);
            entry.setUserid(new ObjectId());
            entry.setSid(sid);
            entry.setXqid(xqid);
            entry.setID(new ObjectId());
            entry.setType(1);
            //姓名
            HSSFCell cell = UserSheet.getRow(i).getCell(0);
            if (!getStringCellValue(cell).equals("")) {
                //根据学生取姓名
                entry.setUserName(getStringCellValue(cell));
            } else {
                Map map = getErrorMap(UserSheet, i);
                returnList.add(map);
                continue;
            }
            //学号
            cell = UserSheet.getRow(i).getCell(1);
            if (!getStringCellValue(cell).equals("")) {
                //根据学生取学号
                entry.setStudyNum(getStringCellValue(cell));
            } else {
                entry.setStudyNum("");
            }
            //学籍类型
            cell = UserSheet.getRow(i).getCell(2);
            //性别类型
            cell = UserSheet.getRow(i).getCell(3);
            if (!getStringCellValue(cell).equals("")) {
                if (getStringCellValue(cell).equals("男")) {
                    entry.setSex(1);
                } else {
                    entry.setSex(0);
                }
            } else {
                Map map = getErrorMap(UserSheet, i);
                returnList.add(map);
                continue;
            }
            //年级
            cell = UserSheet.getRow(i).getCell(4);
            if (!getStringCellValue(cell).equals("")) {
                //取年级
                Grade grade = gradeMap.get(getStringCellValue(cell));
                if (grade != null) {
                    entry.setGradeId(grade.getGradeId());
                    entry.setGradeName(grade.getName());
                } else {
                    Map map = getErrorMap(UserSheet, i);
                    returnList.add(map);
                    continue;
                }
            }
            //班级
            cell = UserSheet.getRow(i).getCell(5);
            if (!getStringCellValue(cell).equals("")) {
                //取班级
                Integer xh = new Double(getStringCellValue(cell)).intValue();
                ClassEntry classEntry = null;
                for (ClassEntry classEntry1 : classList) {
                    if (classEntry1.getXh() == xh && classEntry1.getGradeId().toString().equals(entry.getGradeId().toString())) {
                        classEntry = classEntry1;
                        break;
                    }
                }
                if (classEntry != null) {
                    entry.setClassId(classEntry.getClassId());
                    entry.setClassName(classEntry.getName());
                    List<ObjectId> userIds = classEntry.getStudentList();
                    userIds.add(entry.getUserId());
                    classEntry.setUserList(userIds);
                    classDao.updateIsolateClassEntry(classEntry);
                } else {
                    Map map = getErrorMap(UserSheet, i);
                    returnList.add(map);
                    continue;
                }
            } else {
                Map map = getErrorMap(UserSheet, i);
                returnList.add(map);
                continue;
            }
            //层级
            cell = UserSheet.getRow(i).getCell(6);
            if (!getStringCellValue(cell).equals("")) {
                entry.setLevel(new Double(getStringCellValue(cell)).intValue());
            } else {
                entry.setLevel(0);
            }
            studentEntry.add(entry);
        }
        studentDao.add(studentEntry);
        return returnList;
    }

    /**
     * 将导入不成功的学生返回用户
     *
     * @param userSheet
     * @param rowNum
     * @return
     */
    private Map<String, Object> getErrorMap(HSSFSheet userSheet, Integer rowNum) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", getStringCellValue(userSheet.getRow(rowNum).getCell(0)));
        map.put("stuNum", getStringCellValue(userSheet.getRow(rowNum).getCell(1)));
        map.put("stuRegisterType", getStringCellValue(userSheet.getRow(rowNum).getCell(2)));
        map.put("sex", getStringCellValue(userSheet.getRow(rowNum).getCell(3)));
        map.put("grade", getStringCellValue(userSheet.getRow(rowNum).getCell(4)));
        if("".equals(getStringCellValue(userSheet.getRow(rowNum).getCell(5)))){
            map.put("classXH","");
        }else{
            map.put("classXH", new Double(getStringCellValue(userSheet.getRow(rowNum).getCell(5))).intValue());
        }
        if("".equals(getStringCellValue(userSheet.getRow(rowNum).getCell(6)))){
            map.put("level","");
        }else{
            map.put("level", new Double(getStringCellValue(userSheet.getRow(rowNum).getCell(6))).intValue());
        }
        return map;
    }

    /**
     * 生成导入学生的模板
     *
     * @param response
     */
    public void exportTemplate(HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("用户列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell = row.createCell(1);
        cell.setCellValue("学号");
        cell = row.createCell(2);
        cell.setCellValue("学籍类型");
        cell = row.createCell(3);
        cell.setCellValue("性别");
        cell = row.createCell(4);
        cell.setCellValue("年级");
        cell = row.createCell(5);
        cell.setCellValue("班级序号");
        cell = row.createCell(6);
        cell.setCellValue("层级");

        HSSFRow row2 = sheet.createRow(1);
        HSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("张三");
        cell2 = row2.createCell(1);
        cell2.setCellValue("2018020305");
        cell2 = row2.createCell(2);
        cell2.setCellValue("正式生");
        cell2 = row2.createCell(3);
        cell2.setCellValue("男");
        cell2 = row2.createCell(4);
        cell2.setCellValue("高一");
        cell2 = row2.createCell(5);
        cell2.setCellValue(1);
        cell2 = row2.createCell(6);
        cell2.setCellValue(1);


        String[] list = new String[6];
        list[0] = "";
        list[1] = "1";
        list[2] = "2";
        list[3] = "3";
        list[4] = "4";
        list[5] = "5";
        //性别
        CellRangeAddressList regions = new CellRangeAddressList(1, 1000, 6, 6);
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("导入用户.xls", "UTF-8"));
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


    public void getStudentListBySubjectNameIsNotTag() {
    }

    /**
     * 查询这个学生是否存在
     *
     * @param ciId
     * @param studentName
     */
    public StudentDTO getStudentByStudentName(ObjectId ciId, ObjectId gid, String studentName, ObjectId sid) {
        //姓名查询
        List<StudentEntry> studentEntryList = studentDao.getStu(studentName, ciId, gid);
        //不存在教学班中的学生
        //jxb add
        int acClassType = turnOffService.getAcClassType(sid, gid, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gid, ciId, acClassType);
        StudentEntry stuId = null;
        for (StudentEntry entry : studentEntryList) {
            Boolean bf = true;
            for (N33_JXBEntry jxbEntry : jxbEntries) {
                if (jxbEntry.getStudentIds().contains(entry.getUserId())) {
                    bf = false;
                }
            }
            if (bf) {
                stuId = entry;
                break;
            }
        }
        if (stuId != null) {
            return new StudentDTO(stuId);
        }
        return new StudentDTO();
    }
}
