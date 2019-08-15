package com.fulaan.new33.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.db.new33.N33_TimeCombineDao;
import com.pojo.new33.paike.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.N33_StudentTagDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_ClassDao;
import com.db.new33.isolate.N33_ClassroomDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.SubjectDao;
import com.db.new33.paike.N33_JXBCTDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZKBDao;
import com.db.new33.paike.N33_ZhuanXiangDao;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.dto.isolate.N33_TeaDTO;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.fulaan.new33.service.isolate.IsolateUserService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.mongodb.BasicDBObject;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

@Service
public class N33_FenBanInfoSetService extends BaseService {

    private N33_JXBDao n33_jxbDao = new N33_JXBDao();

    private N33_StudentTagDao n33_studentTagDao = new N33_StudentTagDao();

    private N33_ClassDao n33_classDao = new N33_ClassDao();

    private N33_ClassroomDao n33_classroomDao = new N33_ClassroomDao();

    private N33_StudentDao n33_studentDao = new N33_StudentDao();

    private SubjectDao subjectDao = new SubjectDao();

    private N33_TeaDao n33_teaDao = new N33_TeaDao();

    private N33_JXBCTDao n33_jxbctDao = new N33_JXBCTDao();

    private GradeDao gradeDao = new GradeDao();

    private N33_ZhuanXiangDao n33_zhuanXiangDao = new N33_ZhuanXiangDao();

    private N33_ZKBDao zkbDao = new N33_ZKBDao();
    
    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

    private N33_YKBDao ykbDao = new N33_YKBDao();

    private N33_TimeCombineDao combineDao = new N33_TimeCombineDao();

    @Autowired
    private N33_TurnOffService turnOffService;

    private IsolateSubjectService subjectService = new IsolateSubjectService();

    @Autowired
    private IsolateUserService isolateUserService;

    @Autowired
    N33_JXBService jxbService;

    @Autowired
    private PaiKeService paiKeService;

    public List<N33_KSDTO> getSubjectByGradeId(ObjectId sid, ObjectId gid) {
        List<N33_KSDTO> result = new ArrayList<N33_KSDTO>();
        List<N33_KSEntry> list = subjectDao.getIsolateSubjectEntryByXqid(getDefaultPaiKeTerm(sid).getPaikeci(), sid, gid);
        for (N33_KSEntry entry : list) {
            result.add(new N33_KSDTO(entry));
        }
        return result;
    }

    public List<N33_KSDTO> getSubjectByType(ObjectId gid, ObjectId sid, String subType) {
        ObjectId paikeciId = getDefaultPaiKeTerm(sid).getPaikeci();
        return subjectService.getSubjectByType(paikeciId, gid, sid, subType);
    }

    public Map<String, Object> getJxbInfo(ObjectId gradeId, List<ObjectId> subjectIds, ObjectId schoolId, Integer type) {
        ObjectId xqId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        Map<String, Object> result = new HashMap<String, Object>();
        List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
        //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, xqId,1);
        //jxb add
        List<N33_JXBEntry> n33_jxbEntries = n33_jxbDao.getJXBList(schoolId, gradeId, subjectIds, xqId, type, acClassType);
        List<N33_StudentTagEntry> entryList = n33_studentTagDao.getTagListByxqid(xqId, gradeId);
        List<ClassEntry> classEntries = n33_classDao.findClassEntryBySchoolIdAndGradeId(xqId, schoolId, gradeId, new BasicDBObject("cid", 1).append("nm", 1));
        List<StudentEntry> studentEntries = n33_studentDao.getStudentByXqidAndGradeId(gradeId, xqId, new BasicDBObject("uid", 1).append("cid", 1).append("cnm", 1));
        Map<ObjectId, N33_ClassroomEntry> classId_room = n33_classroomDao.getRoomMapByXqGradeMap(xqId, gradeId);
        Map<ObjectId, N33_KSEntry> subjectId_subject = subjectDao.findSubjectsByIdsMapSubId(xqId, gradeId, schoolId);
        Map<ObjectId, N33_TeaEntry> tedId_tea = n33_teaDao.getTeaMap(xqId, schoolId, gradeId);
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(gradeId, xqId);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId, xqId);
        Map<ObjectId, StudentEntry> id_stu = new HashMap<ObjectId, StudentEntry>();
        for (StudentEntry studentEntry : studentEntries) {
            id_stu.put(studentEntry.getUserId(), studentEntry);
        }
//      for(ClassEntry classEntry:classEntries){
//         objectIdClassEntryMap.put(classEntry.getClassId(),classEntry);
//      }
        Map<ObjectId, N33_StudentTagEntry> objectIdN33_studentTagEntryMap = new HashMap<ObjectId, N33_StudentTagEntry>();
        for (N33_StudentTagEntry n33_studentTagEntry : entryList) {
            objectIdN33_studentTagEntryMap.put(n33_studentTagEntry.getID(), n33_studentTagEntry);
        }
        for (N33_JXBEntry n33_jxbEntry : n33_jxbEntries) {
            List<Map<String, Object>> class_count = new ArrayList<Map<String, Object>>();
            N33_JXBDTO n33_jxbdto = new N33_JXBDTO(n33_jxbEntry);
            // 设置教室名称
            if (classId_room.get(n33_jxbEntry.getClassroomId()) != null) {
                if (classId_room.get(n33_jxbEntry.getClassroomId()).getClassName() != null) {
                    n33_jxbdto.setClassroomName(classId_room.get(n33_jxbEntry.getClassroomId()).getRoomName() + "(" + classId_room.get(n33_jxbEntry.getClassroomId()).getClassName() + ")");
                } else {
                    n33_jxbdto.setClassroomName(classId_room.get(n33_jxbEntry.getClassroomId()).getRoomName());
                }
            } else {
                n33_jxbdto.setClassroomName("");
            }
            // 设置学科
            n33_jxbdto.setSubjectName(subjectId_subject.get(n33_jxbEntry.getSubjectId()).getSubjectName());
            if (tedId_tea.get(n33_jxbEntry.getTercherId()) != null) {
                n33_jxbdto.setTeacherName(tedId_tea.get(n33_jxbEntry.getTercherId()).getUserName());
            } else {
                n33_jxbdto.setTeacherName("");
            }
            List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
            for (ObjectId id : n33_jxbEntry.getTagIds()) {
                N33_StudentTagEntry tagEntry = objectIdN33_studentTagEntryMap.get(id);
                if (tagEntry != null) {
                    Map<String, Object> tag = new HashMap<String, Object>();
                    tag.put("name", tagEntry.getName());
                    tag.put("num", tagEntry.getStudents().size());
                    tags.add(tag);
                }
            }
            n33_jxbdto.setTags(tags);
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (ObjectId userId : n33_jxbEntry.getStudentIds()) {
                StudentEntry studentEntry = id_stu.get(userId);
                ClassEntry classEntry = null;
                if (studentEntry != null && studentEntry.getClassId() != null) {
                    classEntry = classEntryMap.get(studentEntry.getClassId());
                }
                if (classEntry != null && classEntry.getXh() != null) {
                    Integer i = map.get(classEntry.getXh()) == null ? 0 : map.get(classEntry.getXh());
                    map.put(classEntry.getXh(), i + 1);
                }
            }

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                Map<String, Object> tempMap = new HashMap<String, Object>();
                if (grade != null) {
                    tempMap.put("grade", grade.getName());
                } else {
                    tempMap.put("grade", "");
                }
                tempMap.put("xh", entry.getKey());
                tempMap.put("num", entry.getValue());
                class_count.add(tempMap);
            }

            n33_jxbdto.setClass_count(class_count);
            jxbdtos.add(n33_jxbdto);
        }
        result.put("list", jxbdtos);
        return result;
    }

    public Map<String, Object> getJxbInfoExcptZX(ObjectId gradeId, List<ObjectId> subjectIds, ObjectId schoolId, String type) {
        ObjectId xqId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        Map<String, Object> result = new HashMap<String, Object>();
        List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();

        List<N33_JXBEntry> n33_jxbEntries = jxbService.getAllJXBListForEntry(xqId, gradeId, schoolId, subjectIds.get(0), new Integer(type));

        List<N33_StudentTagEntry> entryList = n33_studentTagDao.getTagListByxqid(xqId, gradeId);
        List<ClassEntry> classEntries = n33_classDao.findClassEntryBySchoolIdAndGradeId(xqId, schoolId, gradeId, new BasicDBObject("cid", 1).append("nm", 1).append("uis", 1));

        List<ObjectId> allStuIds = new ArrayList<ObjectId>();
        for(ClassEntry classEntry:classEntries) {
            List<ObjectId> studentIds = classEntry.getStudentList();
            allStuIds.addAll(studentIds);
            List<StudentEntry> studentEntryList = n33_studentDao.getStuList(studentIds, xqId, "*");
            List<ObjectId> updUserIds = new ArrayList<ObjectId>();
            for (StudentEntry entry : studentEntryList) {
                if (!classEntry.getClassId().equals(entry.getClassId())) {
                    updUserIds.add(entry.getUserId());
                }
            }
            if (updUserIds.size() > 0) {
                n33_studentDao.updateStuClassId(updUserIds, xqId, classEntry.getClassId());
            }
        }

        List<StudentEntry> studentEntries = n33_studentDao.getStudentByXqidAndGradeId(gradeId, xqId, new BasicDBObject("uid", 1).append("cid", 1).append("cnm", 1));
        Map<ObjectId, N33_ClassroomEntry> classId_room = n33_classroomDao.getRoomMapByXqGradeMap(xqId, gradeId);
        Map<ObjectId, N33_KSEntry> subjectId_subject = subjectDao.findSubjectsByIdsMapSubId(xqId, gradeId, schoolId);
        Map<ObjectId, N33_TeaEntry> tedId_tea = n33_teaDao.getTeaMap(xqId, schoolId, gradeId);
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(gradeId, xqId);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId, xqId);
        Map<ObjectId, StudentEntry> id_stu = new HashMap<ObjectId, StudentEntry>();
        for (StudentEntry studentEntry : studentEntries) {
            id_stu.put(studentEntry.getUserId(), studentEntry);
        }
//        for(ClassEntry classEntry:classEntries){
//            objectIdClassEntryMap.put(classEntry.getClassId(),classEntry);
//        }
        Map<ObjectId, N33_StudentTagEntry> objectIdN33_studentTagEntryMap = new HashMap<ObjectId, N33_StudentTagEntry>();
        for (N33_StudentTagEntry n33_studentTagEntry : entryList) {
            List<N33_StudentTagEntry.StudentInfoEntry> stuInfoList = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
            for(N33_StudentTagEntry.StudentInfoEntry stuInfo: n33_studentTagEntry.getStudents()){
                if(allStuIds.contains(stuInfo.getStuId())){
                    stuInfoList.add(stuInfo);
                }
            }
            if(stuInfoList.size()>0&&n33_studentTagEntry.getStudents().size()>stuInfoList.size()){
                n33_studentTagEntry.setStudents(stuInfoList);
                n33_studentTagDao.update(n33_studentTagEntry);
            }
            objectIdN33_studentTagEntryMap.put(n33_studentTagEntry.getID(), n33_studentTagEntry);
        }

        for (N33_JXBEntry n33_jxbEntry : n33_jxbEntries) {
            List<ObjectId> stuIds = new ArrayList<ObjectId>();
            for(ObjectId stuId:n33_jxbEntry.getStudentIds()){
                if(allStuIds.contains(stuId)){
                    stuIds.add(stuId);
                }
            }
            if(stuIds.size()>0&&n33_jxbEntry.getStudentIds().size()>stuIds.size()){
                n33_jxbEntry.setStudentIds(stuIds);
                jxbService.updateEntry(n33_jxbEntry);
            }
            stuIds = new ArrayList<ObjectId>();
            for (ObjectId id : n33_jxbEntry.getTagIds()) {
                N33_StudentTagEntry tagEntry = objectIdN33_studentTagEntryMap.get(id);
                if (tagEntry != null) {
                    for(N33_StudentTagEntry.StudentInfoEntry stuInfo: tagEntry.getStudents()){
                        stuIds.add(stuInfo.getStuId());
                    }
                }
            }
            if(stuIds.size()>0&&n33_jxbEntry.getStudentIds().size()>stuIds.size()){
                n33_jxbEntry.setStudentIds(stuIds);
                jxbService.updateEntry(n33_jxbEntry);
            }

            stuIds = new ArrayList<ObjectId>();
            for (ObjectId userId : n33_jxbEntry.getStudentIds()) {
                StudentEntry studentEntry = id_stu.get(userId);
                ClassEntry classEntry = null;
                if (studentEntry != null && studentEntry.getClassId() != null) {
                    classEntry = classEntryMap.get(studentEntry.getClassId());
                }
                if (classEntry != null && classEntry.getXh() != null) {
                    stuIds.add(userId);
                }
            }

            if(stuIds.size()>0&&n33_jxbEntry.getStudentIds().size()>stuIds.size()){
                n33_jxbEntry.setStudentIds(stuIds);
                jxbService.updateEntry(n33_jxbEntry);
            }
        }
        //========================== 教学班更改 06-14 ==========================
        //jxb add
        Map<ObjectId, Integer> gACTMap = turnOffService.getGradeAcClassTypeMap(schoolId);

        int acClassType = gACTMap.get(gradeId)==null? 0 : gACTMap.get(gradeId);

        //jxb add
        //========================== 教学班更改 06-14 ==========================

        for (N33_JXBEntry n33_jxbEntry : n33_jxbEntries) {
            List<Map<String, Object>> class_count = new ArrayList<Map<String, Object>>();
            N33_JXBDTO n33_jxbdto = new N33_JXBDTO(n33_jxbEntry);
            if (n33_jxbEntry.getTercherId() != null) {
                n33_jxbdto.setThisGradeCarryNum(n33_jxbDao.countTeacherCarryJxbNUM(xqId, n33_jxbEntry.getTercherId(), gradeId, acClassType));
                n33_jxbdto.setCarryNum(n33_jxbDao.countTeacherCarryJxbNUM(xqId, n33_jxbEntry.getTercherId(), gACTMap));
            } else {
                n33_jxbdto.setThisGradeCarryNum(0);
                n33_jxbdto.setCarryNum(0);
            }
            // 设置教室名称
            if (classId_room.get(n33_jxbEntry.getClassroomId()) != null) {
                if (classId_room.get(n33_jxbEntry.getClassroomId()).getClassName() != null) {
                    n33_jxbdto.setClassroomName(classId_room.get(n33_jxbEntry.getClassroomId()).getRoomName() + "(" + classId_room.get(n33_jxbEntry.getClassroomId()).getClassName() + ")");
                } else {
                    n33_jxbdto.setClassroomName(classId_room.get(n33_jxbEntry.getClassroomId()).getRoomName());
                }
            } else {
                n33_jxbdto.setClassroomName("");
            }
            // 设置学科
            n33_jxbdto.setSubjectName(subjectId_subject.get(n33_jxbEntry.getSubjectId()).getSubjectName());
            if (tedId_tea.get(n33_jxbEntry.getTercherId()) != null) {
                n33_jxbdto.setTeacherName(tedId_tea.get(n33_jxbEntry.getTercherId()).getUserName());
            } else {
                n33_jxbdto.setTeacherName("");
            }
            List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
            for (ObjectId id : n33_jxbEntry.getTagIds()) {
                N33_StudentTagEntry tagEntry = objectIdN33_studentTagEntryMap.get(id);
                if (tagEntry != null) {
                    Map<String, Object> tag = new HashMap<String, Object>();
                    tag.put("name", tagEntry.getName());
                    tag.put("num", tagEntry.getStudents().size());
                    tags.add(tag);
                }
            }
            n33_jxbdto.setTags(tags);
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (ObjectId userId : n33_jxbEntry.getStudentIds()) {
                StudentEntry studentEntry = id_stu.get(userId);
                ClassEntry classEntry = null;
                if (studentEntry != null && studentEntry.getClassId() != null) {
                    classEntry = classEntryMap.get(studentEntry.getClassId());
                }
                if (classEntry != null && classEntry.getXh() != null) {
                    Integer i = map.get(classEntry.getXh()) == null ? 0 : map.get(classEntry.getXh());
                    map.put(classEntry.getXh(), i + 1);
                }
            }

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                Map<String, Object> tempMap = new HashMap<String, Object>();
                if (grade != null) {
                    tempMap.put("grade", grade.getName());
                } else {
                    tempMap.put("grade", "");
                }
                tempMap.put("xh", entry.getKey());
                tempMap.put("num", entry.getValue());
                class_count.add(tempMap);
            }

            n33_jxbdto.setClass_count(class_count);
            if (n33_jxbdto.getType() != 4) {
                jxbdtos.add(n33_jxbdto);
            }
        }
        result.put("list", jxbdtos);
        return result;
    }

    public List<Map<String, Object>> getTeacherList(ObjectId sid, ObjectId gradeId, ObjectId subjectId) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        Collection<N33_TeaEntry> collection = n33_teaDao.getTeaMap(xqid, sid, gradeId, subjectId).values();
        for (N33_TeaEntry tea : collection) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", tea.getUserName());
            map.put("id", tea.getUserId().toString());
            result.add(map);
        }
        return result;
    }

    public List<Map<String, Object>> getTeacherList2(ObjectId xqId,ObjectId sid, ObjectId gradeId, ObjectId subjectId,String index,int week) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        Collection<N33_TeaEntry> collection = n33_teaDao.getTeaMap(xqid, sid, gradeId, subjectId).values();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (N33_TeaEntry tea : collection) {
            userIds.add(tea.getUserId());
        }
        List<N33_ZKBEntry> zkbEntries = zkbDao.getZKBEntrysListByTeacherIds(xqId, sid,week,userIds);
        List<ObjectId> teacherIds = new ArrayList<ObjectId>();
        if (StringUtils.isNotEmpty(index)) {
            String[] args = index.split(",");
            if (zkbEntries!=null && zkbEntries.size()!=0) {
                for (N33_ZKBEntry entry : zkbEntries) {
                    for (int i=0;i<args.length;i++) {
                        String s = "" + entry.getX()+entry.getY();
                        if (StringUtils.isNotEmpty(args[i]) && args[i].equals(s)) {
                            teacherIds.add(entry.getTeacherId());
                            if (entry.getNTeacherId()!=null) {
                                teacherIds.add(entry.getNTeacherId());
                            }
                        }
                    }
                }
            }
        }
        for (N33_TeaEntry tea : collection) {
            if (!teacherIds.contains(tea.getUserId())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", tea.getUserName());
                map.put("id", tea.getUserId().toString());
                userIds.add(tea.getUserId());
                result.add(map);
            }
        }

        return result;
    }

    public String[] getTeacherLists(ObjectId sid, ObjectId gradeId, ObjectId id) {
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(id);
        List<N33_TeaEntry> collection = n33_teaDao.getTeaListByGradeId(xqid, sid, gradeId, jxbEntry.getSubjectId());
        String[] result=new String[collection.size()];
        for (Integer a=0;a<collection.size();a++){
            result[a]=collection.get(a).getUserName();
        }
        return result;
    }

    public List<Map<String, Object>> getClassRoomList(ObjectId sid, ObjectId gradeId) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        List<N33_ClassroomEntry> list = n33_classroomDao.getRoomEntryListByXqGrade(xqid, sid, gradeId);
        for (N33_ClassroomEntry classroomEntry : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            String className = "";
            if (classroomEntry.getClassName() != null && !classroomEntry.getClassName().equals("null")) {
                className = "(" + classroomEntry.getClassName() + ")";
            }
            map.put("name", classroomEntry.getRoomName() + className);
            map.put("id", classroomEntry.getRoomId().toString());
            result.add(map);
        }
        return result;
    }

    public String[] getClassRoomLists(ObjectId sid, ObjectId gradeId) {
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        List<N33_ClassroomEntry> list = n33_classroomDao.getRoomEntryListByXqGrade(xqid, sid, gradeId);
        String[] result=new String[list.size()];
        for (Integer a=0;a<list.size();a++){
            result[a]=list.get(a).getRoomName();
        }
        return result;
    }

    /**
     * 设置教师
     *
     * @param jxbId
     * @param sid
     * @param teacherId
     */
    public void setJxbTeacher(ObjectId jxbId, ObjectId sid, ObjectId teacherId) {
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(jxbEntry.getTermId(),sid);
        String ykbIds = "";
        for (N33_YKBEntry ykbEntry : ykbEntryList) {
            if((ykbEntry.getType() == 1 || ykbEntry.getType() == 2 || ykbEntry.getType() == 3 || ykbEntry.getType() == 6) && (jxbId.equals(ykbEntry.getJxbId()) || (ykbEntry.getNJxbId() != null && jxbId.equals(ykbEntry.getNJxbId())))){
                ykbIds += ykbEntry.getID();
                ykbIds += ",";
            }
        }
        paiKeService.undoYKB(ykbIds,sid);
        n33_jxbDao.updateTeacherId(jxbId, teacherId);// 先修改再检测冲突
    }

    public void setJxbRoom(ObjectId jxbId, ObjectId sid, ObjectId roomId) {
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(jxbEntry.getTermId(),sid);
        String ykbIds = "";
        for (N33_YKBEntry ykbEntry : ykbEntryList) {
            if((ykbEntry.getType() == 1 || ykbEntry.getType() == 2 || ykbEntry.getType() == 3 || ykbEntry.getType() == 6) && (jxbId.equals(ykbEntry.getJxbId()) || (ykbEntry.getNJxbId() != null && jxbId.toString().equals(ykbEntry.getNJxbId().toString())))){
                ykbIds += ykbEntry.getID();
                ykbIds += ",";
            }
        }
        paiKeService.undoYKB(ykbIds,sid);
        if (jxbEntry.getDanOrShuang() != 0) {
            ObjectId relativeId = jxbEntry.getRelativeId();
            n33_jxbDao.updateClassId(relativeId, roomId);
        }
        n33_jxbDao.updateClassId(jxbId, roomId);// 先修改在更新

    }

    public void detactConfliction(ObjectId sid) {
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        List<N33_JXBEntry> collection = n33_jxbDao.getJXBList(xqid);
        List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(collection,"_id");
        Map<ObjectId,N33_JXBEntry> jxbEntryMap = n33_jxbDao.getJXBMapsByIds(jxbIds);
        List<ObjectId> waitForDel = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxb : collection) {
            waitForDel.add(jxb.getID());
        }
        Map<ObjectId, List<N33_ZhuanXiangEntry>> zhuanxiangMap = n33_zhuanXiangDao.getZhuanXiangMap(xqid);
        // 教师冲突
        n33_jxbctDao.removeByJxbIdAll(sid, 2,xqid);
        n33_jxbctDao.removeByJxbIdoAll(sid, 2,xqid);
        List<N33_JXBCTEntry> list = new ArrayList<N33_JXBCTEntry>();
        for (N33_JXBEntry jxb1 : collection) {
            List<ObjectId> teaIds = new ArrayList<ObjectId>();
            if (jxb1.getType() == 4) {
                List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanxiangMap.get(jxb1.getID());
                if (zhuanXiangEntryList != null) {
                    for (N33_ZhuanXiangEntry entry : zhuanXiangEntryList) {
                        teaIds.add(entry.getTeaId());
                    }
                }
            } else {
                ObjectId teaId1 = jxb1.getTercherId();
                teaIds.add(teaId1);
            }

            for (N33_JXBEntry jxb2 : collection) {
                List<ObjectId> teaIds2 = new ArrayList<ObjectId>();
                if (jxb2.getType() == 4) {
                    List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanxiangMap.get(jxb2.getID());
                    if (zhuanXiangEntryList != null) {
                        for (N33_ZhuanXiangEntry entry : zhuanXiangEntryList) {
                            teaIds2.add(entry.getTeaId());
                        }
                    }

                } else {
                    ObjectId teaId2 = jxb2.getTercherId();
                    teaIds2.add(teaId2);
                }
                if (!jxb1.getID().toString().equals(jxb2.getID().toString())) {
                    List<ObjectId> tempTeaIds = new ArrayList<ObjectId>(teaIds);// 临时的
                    tempTeaIds.retainAll(teaIds2);
                    if (tempTeaIds.size() > 0 && tempTeaIds.get(0) != null && ((jxbEntryMap.get(jxb1).getDanOrShuang() == 1 && jxbEntryMap.get(jxb2).getDanOrShuang() != 2) || jxbEntryMap.get(jxb1).getDanOrShuang() == 0 || jxbEntryMap.get(jxb2).getDanOrShuang() == 0 || (jxbEntryMap.get(jxb1).getDanOrShuang() == 2 && jxbEntryMap.get(jxb2).getDanOrShuang() != 1))) {
                        list.add(new N33_JXBCTEntry(xqid, sid, jxb1.getID(), jxb2.getID(), 2, 0, null));
                    }
                }
            }
        }
        if (!list.isEmpty()) {
            n33_jxbctDao.addN33_JXBCTEntrys(list);
        }

        // 教室冲突
        n33_jxbctDao.removeByJxbIdAll(sid, 3,xqid);
        n33_jxbctDao.removeByJxbIdoAll(sid, 3,xqid);
        List<N33_JXBCTEntry> list2 = new ArrayList<N33_JXBCTEntry>();
        for (N33_JXBEntry jxb1 : collection) {
            List<ObjectId> roomIds = new ArrayList<ObjectId>();
            if (jxb1.getType() == 4) {
                List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanxiangMap.get(jxb1.getID());
                if (zhuanXiangEntryList != null) {
                    for (N33_ZhuanXiangEntry entry : zhuanXiangEntryList) {
                        roomIds.add(entry.getRoomId());
                    }
                }
            } else {
                ObjectId room1 = jxb1.getClassroomId();
                roomIds.add(room1);
            }

            for (N33_JXBEntry jxb2 : collection) {
                List<ObjectId> roomIds2 = new ArrayList<ObjectId>();
                if (jxb2.getType() == 4) {
                    List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanxiangMap.get(jxb2.getID());
                    if (zhuanXiangEntryList != null) {
                        for (N33_ZhuanXiangEntry entry : zhuanXiangEntryList) {
                            roomIds2.add(entry.getRoomId());
                        }
                    }
                } else {
                    ObjectId room2 = jxb2.getClassroomId();
                    roomIds2.add(room2);
                }
                if (!jxb1.getID().toString().equals(jxb2.getID().toString())) {
                    List<ObjectId> tempRoomIds = new ArrayList<ObjectId>(roomIds);// 临时的
                    tempRoomIds.retainAll(roomIds2);
                    if (tempRoomIds.size() > 0 && ((jxbEntryMap.get(jxb1).getDanOrShuang() == 1 && jxbEntryMap.get(jxb2).getDanOrShuang() != 2) || jxbEntryMap.get(jxb1).getDanOrShuang() == 0 || jxbEntryMap.get(jxb2).getDanOrShuang() == 0 || (jxbEntryMap.get(jxb1).getDanOrShuang() == 2 && jxbEntryMap.get(jxb2).getDanOrShuang() != 1))) {
                        list2.add(new N33_JXBCTEntry(xqid, sid, jxb1.getID(), jxb2.getID(), 3, 0, null));
                    }
                }
            }
        }
        if (!list2.isEmpty()) {
            n33_jxbctDao.addN33_JXBCTEntrys(list2);
        }

    }

    public void detactConfliction(ObjectId sid, ObjectId ciId) {
        List<N33_JXBEntry> jxbList = n33_jxbDao.getJXBList(ciId);
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        Map<ObjectId, N33_JXBEntry> jxbMap = new HashMap<ObjectId, N33_JXBEntry>();
        for (N33_JXBEntry jxb : jxbList) {
            if(!jxbIds.contains(jxb.getID())){
                jxbIds.add(jxb.getID());
                jxbMap.put(jxb.getID(), jxb);
            }
        }
        Map<ObjectId, List<N33_ZhuanXiangEntry>> zhuanxiangMap = n33_zhuanXiangDao.getZhuanXiangMap(ciId);
        // 教师冲突
        n33_jxbctDao.removeByJxbId(sid, jxbIds, 2);
        n33_jxbctDao.removeByJxbIdo(sid, jxbIds, 2);

        // 教室冲突
        n33_jxbctDao.removeByJxbId(sid, jxbIds, 3);
        n33_jxbctDao.removeByJxbIdo(sid, jxbIds, 3);

        List<N33_JXBCTEntry> list = new ArrayList<N33_JXBCTEntry>();

        for (N33_JXBEntry jxb1 : jxbList) {
            List<ObjectId> teaIds = new ArrayList<ObjectId>();
            List<ObjectId> roomIds = new ArrayList<ObjectId>();
            if (jxb1.getType() == 4) {
                List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanxiangMap.get(jxb1.getID());
                if (zhuanXiangEntryList != null) {
                    for (N33_ZhuanXiangEntry entry : zhuanXiangEntryList) {
                        ObjectId teaId1 = entry.getTeaId();
                        if(null!=teaId1&&!teaIds.contains(teaId1)) {
                            teaIds.add(teaId1);
                        }
                        ObjectId room1 = entry.getRoomId();
                        if(null!=room1&&!roomIds.contains(room1)) {
                            roomIds.add(room1);
                        }
                    }
                }
            } else {
                ObjectId teaId1 = jxb1.getTercherId();
                if(null!=teaId1&&!teaIds.contains(teaId1)) {
                    teaIds.add(teaId1);
                }
                ObjectId room1 = jxb1.getClassroomId();
                if(null!=room1&&!roomIds.contains(room1)) {
                    roomIds.add(room1);
                }
            }
            jxbIds.remove(jxb1.getID());
            for(ObjectId jxbId:jxbIds) {
                if (jxbId.equals(jxb1.getID())) {
                    continue;
                }
                N33_JXBEntry jxb2 = jxbMap.get(jxbId);
                List<ObjectId> teaIds2 = new ArrayList<ObjectId>();
                List<ObjectId> roomIds2 = new ArrayList<ObjectId>();
                if (jxb2.getType() == 4) {
                    List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanxiangMap.get(jxb2.getID());
                    if (zhuanXiangEntryList != null) {
                        for (N33_ZhuanXiangEntry entry : zhuanXiangEntryList) {
                            ObjectId teaId2 = entry.getTeaId();
                            if(null!=teaId2&&!teaIds2.contains(teaId2)) {
                                teaIds2.add(teaId2);
                            }
                            ObjectId room2 = entry.getRoomId();
                            if(null!=room2&&!roomIds2.contains(room2)) {
                                roomIds2.add(room2);
                            }
                        }
                    }
                } else {
                    ObjectId teaId2 = jxb2.getTercherId();
                    if(null!=teaId2&&!teaIds2.contains(teaId2)) {
                        teaIds2.add(teaId2);
                    }
                    ObjectId room2 = jxb2.getClassroomId();
                    if(null!=room2&&!roomIds2.contains(room2)) {
                        roomIds2.add(room2);
                    }
                }
                List<ObjectId> tempTeaIds = new ArrayList<ObjectId>(teaIds);// 临时的
                tempTeaIds.retainAll(teaIds2);
                if (tempTeaIds.size() > 0 && tempTeaIds.get(0) != null) {
                    N33_JXBCTEntry jxbct1 = new N33_JXBCTEntry(ciId, sid, jxb1.getID(), jxb2.getID(), 2, 0, null);
                    list.add(jxbct1);
                    N33_JXBCTEntry jxbct2 = new N33_JXBCTEntry(ciId, sid, jxb2.getID(), jxb1.getID(), 2, 0, null);
                    list.add(jxbct2);
                }
                List<ObjectId> tempRoomIds = new ArrayList<ObjectId>(roomIds);// 临时的
                tempRoomIds.retainAll(roomIds2);
                if (tempRoomIds.size() > 0) {
                    N33_JXBCTEntry jxbct1 = new N33_JXBCTEntry(ciId, sid, jxb1.getID(), jxb2.getID(), 3, 0, null);
                    list.add(jxbct1);
                    N33_JXBCTEntry jxbct2 = new N33_JXBCTEntry(ciId, sid, jxb2.getID(), jxb1.getID(), 3, 0, null);
                    list.add(jxbct2);
                }

            }
        }
        if (!list.isEmpty()) {
            n33_jxbctDao.addN33_JXBCTEntrys(list);
        }
    }

    public void setNickName(ObjectId jxbId, String nickname) {
        n33_jxbDao.updateNickName(jxbId, nickname);
    }

    /**
     * @param xqId
     * @param gradeId
     * @param subid1
     * @param subid2
     * @param type    1等级考  2合格考
     */
    public Map<String, Object> conflictDetection(ObjectId xqId, ObjectId schoolId, ObjectId gradeId, ObjectId subid1, ObjectId subid2, Integer type) {
        Map<String, Object> result = new HashMap<String, Object>();
        //jxb add
        ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntryList1 = n33_jxbDao.getJXBList(schoolId, gradeId, subid1, xqId, type, acClassType);
        List<N33_JXBEntry> jxbEntryList2 = n33_jxbDao.getJXBList(schoolId, gradeId, subid2, xqId, type, acClassType);
        List<List<Map<String, Object>>> content = new ArrayList<List<Map<String, Object>>>();
        for (N33_JXBEntry jxbEntry : jxbEntryList1) {
            List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
            List<ObjectId> stuList1 = jxbEntry.getStudentIds();
            for (N33_JXBEntry jxbEntry1 : jxbEntryList2) {
                Map<String, Object> map = new HashMap<String, Object>();
                List<ObjectId> stuList2 = jxbEntry.getStudentIds();
                stuList1.retainAll(stuList2);
                map.put("num", stuList1.size());
                map.put("stus", MongoUtils.convertToStringList(stuList1));
                row.add(map);
            }
            content.add(row);
        }
        List<String> head = new ArrayList<String>();
        for (N33_JXBEntry entry : jxbEntryList1) {
            if (StringUtils.isNotEmpty(entry.getNickName())) {
                head.add(entry.getNickName());
            } else {
                head.add(entry.getName());
            }
        }
        List<String> head2 = new ArrayList<String>();
        for (N33_JXBEntry entry : jxbEntryList2) {
            if (StringUtils.isNotEmpty(entry.getNickName())) {
                head.add(entry.getNickName());
            } else {
                head.add(entry.getName());
            }
        }
        result.put("content", content);
        result.put("head", head);
        result.put("head2", head2);
        return result;
    }

    public Map<String, Object> getConflictCollection(ObjectId schoolId, ObjectId gradeId, ObjectId subid1, ObjectId subid2, Integer type) {
        ObjectId xqId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        Map<String, Object> result = new HashMap<String, Object>();
        //jxb add
        ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBList(schoolId, gradeId, xqId,acClassType);
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxb : jxbEntryList) {
            jxbIds.add(jxb.getID());
        }
        List<N33_JXBCTEntry> ctList = n33_jxbctDao.getJXBCTEntrysByCiId(xqId, schoolId, 1);// 学生冲突
//        List<N33_JXBCTEntry> ctList = n33_jxbctDao.getJxbCTBySid(schoolId, 1);// 学生冲突
        Map<String, N33_JXBCTEntry> ctMap = new HashMap<String, N33_JXBCTEntry>();
        for (N33_JXBCTEntry entry : ctList) {
            ctMap.put(entry.getJxbId().toString() + "_" + entry.getOjxbId().toString(), entry);
        }
        List<N33_JXBEntry> jxbEntryList1 = jxbService.getAllJXBListForEntry(xqId, gradeId, schoolId, subid1, type);
        List<N33_JXBEntry> jxbEntryList2 = jxbService.getAllJXBListForEntry(xqId, gradeId, schoolId, subid2, type);
        //List<N33_JXBEntry> jxbEntryList1 = n33_jxbDao.getJXBsBySubId(xqId, schoolId, gradeId, subid1, type);
        //List<N33_JXBEntry> jxbEntryList2 = n33_jxbDao.getJXBsBySubId(xqId, schoolId, gradeId, subid2, type);
        List<List<Map<String, Object>>> content = new ArrayList<List<Map<String, Object>>>();
        Map<String, List<String>> jxbct_stus = new HashMap<String, List<String>>();
        for (N33_JXBEntry jxbEntry : jxbEntryList1) {
            List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
            String jxbid1 = jxbEntry.getID().toString();
            for (N33_JXBEntry jxbEntry1 : jxbEntryList2) {
                Map<String, Object> map = new HashMap<String, Object>();
                String jxbid2 = jxbEntry1.getID().toString();
                if (ctMap.get(jxbid1 + "_" + jxbid2) != null) {
                    N33_JXBCTEntry jxbctEntry = ctMap.get(jxbid1 + "_" + jxbid2);
                    map.put("num", jxbctEntry.getCtCount());
                    jxbct_stus.put(jxbid1 + "_" + jxbid2, MongoUtils.convertToStringList(jxbctEntry.getCtxsIds()));
                } else {
                    map.put("num", 0);
                }
                map.put("jxbs", jxbid1 + "_" + jxbid2);
                row.add(map);
            }
            content.add(row);
        }
        result.put("content", content);
        List<Map<String, Object>> vertical = new ArrayList<Map<String, Object>>();
        for (N33_JXBEntry jxb : jxbEntryList1) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(jxb.getNickName())) {
                map.put("name", jxb.getNickName());
            } else {
                map.put("name", jxb.getName());
            }
            vertical.add(map);
        }

        List<Map<String, Object>> horizon = new ArrayList<Map<String, Object>>();
        for (N33_JXBEntry jxb : jxbEntryList2) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(jxb.getNickName())) {
                map.put("name", jxb.getNickName());
            } else {
                map.put("name", jxb.getName());
            }
            horizon.add(map);
        }
        result.put("vertical", vertical);
        result.put("horizon", horizon);
        result.put("ctstus", jxbct_stus);
        return result;
    }

    public List<Map<String, Object>> getStudents(List<String> ids, ObjectId sid, ObjectId gradeId) {
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<ObjectId, ClassEntry> classId_class = n33_classDao.findClassEntryMap(gradeId, xqid);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId, xqid);
        List<StudentEntry> studentList = n33_studentDao.getStuList(MongoUtils.convertToObjectIdList(ids), xqid);
        for (StudentEntry entry : studentList) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (grade != null) {
                map.put("grade", grade.getName());
            } else {
                map.put("grade", "");
            }
            map.put("lev",entry.getLevel()==0?"无":entry.getLevel().toString());
            map.put("xh", classId_class.get(entry.getClassId()) == null ? "" : classId_class.get(entry.getClassId()).getXh());
            map.put("name", entry.getUserName());
            map.put("class", classId_class.get(entry.getClassId()) == null ? "" : classId_class.get(entry.getClassId()).getClassName());
            map.put("sn", entry.getStudyNum() == null ? "" : String.valueOf(entry.getStudyNum()));
            if (entry.getSex() == 0) {
                map.put("sex", "女");
            } else if (entry.getSex() == 1) {
                map.put("sex", "男");
            }
            map.put("group", entry.getCombiname() == null ? "" : entry.getCombiname());
            result.add(map);
        }
        return result;
    }

    public void exportJXBinfo(ObjectId sid, HttpServletResponse response) {
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBList(xqid);
        Map<ObjectId, StudentEntry> id_stu = n33_studentDao.getStuMapIdEntry(xqid, Constant.FIELDS);
        Map<ObjectId, N33_TeaEntry> id_tea = n33_teaDao.getTeaMap(xqid, sid, new BasicDBObject("uid", 1).append("unm", 1));
        Map<ObjectId, Grade> id_grade = gradeDao.findGradeListBySchoolIdMap(xqid, sid);
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(xqid);
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("教学班学生");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell = row.createCell(1);
        cell.setCellValue("学号");
        cell = row.createCell(2);
        cell.setCellValue("性别");
        cell = row.createCell(3);
        cell.setCellValue("层级");
        cell = row.createCell(4);
        cell.setCellValue("教师");
        cell = row.createCell(5);
        cell.setCellValue("教学班");
        cell = row.createCell(6);
        cell.setCellValue("年级");
        cell = row.createCell(7);
        cell.setCellValue("行政班");
        int count = 1;// 学生计数
        for (int i = 0; i < jxbEntryList.size(); i++) {
            String teacherName = "";
            String gradeName = "";
            if (id_tea.get(jxbEntryList.get(i).getTercherId()) != null) {
                teacherName = id_tea.get(jxbEntryList.get(i).getTercherId()).getUserName();
            }
            if (jxbEntryList.get(i).getGradeId() != null && id_grade.get(jxbEntryList.get(i).getGradeId()) != null) {
                gradeName = id_grade.get(jxbEntryList.get(i).getGradeId()).getName();
            }
            String jxbName = "";
            if (StringUtils.isNotEmpty(jxbEntryList.get(i).getNickName())) {
                jxbName = jxbEntryList.get(i).getNickName();
            } else {
                jxbName = jxbEntryList.get(i).getName();
            }
            for (int j = 0; j < jxbEntryList.get(i).getStudentIds().size(); j++) {
                ObjectId stuid = jxbEntryList.get(i).getStudentIds().get(j);
                String stuName = "";
                String sn = "";
                String sex = "";
                String level = "";
                String classname = "";
                HSSFRow temprow = sheet.createRow(count);

                if (id_stu.get(stuid) != null) {
                    stuName = id_stu.get(stuid).getUserName();
                    sn = id_stu.get(stuid).getStudyNum();
                    sex = id_stu.get(stuid).getSex() == 0 ? "女" : "男";
                    level = String.valueOf((Integer) id_stu.get(stuid).getLevel());
                    ClassEntry classEntry = classEntryMap.get(id_stu.get(stuid).getClassId());
                    if(classEntry!=null){
                        classname = classEntry.getClassName();
                    }
                }

                HSSFCell tempcell = temprow.createCell(0);
                tempcell.setCellValue(stuName);
                tempcell = temprow.createCell(1);
                tempcell.setCellValue(sn);
                tempcell = temprow.createCell(2);
                tempcell.setCellValue(sex);
                tempcell = temprow.createCell(3);
                tempcell.setCellValue(level);
                tempcell = temprow.createCell(4);
                tempcell.setCellValue(teacherName);
                tempcell = temprow.createCell(5);
                tempcell.setCellValue(jxbName);
                tempcell = temprow.createCell(6);
                tempcell.setCellValue(gradeName);
                tempcell = temprow.createCell(7);
                tempcell.setCellValue(classname);
                count++;
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("教学班学生.xls", "UTF-8"));
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
    
    //导出教学班  oz 
    public void exportJXBinfoTwo(ObjectId sid, String gradeId,HttpServletResponse response) {
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();//次id
        //jxb add
        ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = turnOffService.getAcClassType(sid, new ObjectId(gradeId), ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBList(sid, new ObjectId(gradeId), xqid,acClassType);//教学班
        Map<ObjectId, StudentEntry> id_stu = n33_studentDao.getStuMapIdEntry(xqid, Constant.FIELDS);//学生信息
        Map<ObjectId, N33_TeaEntry> id_tea = n33_teaDao.getTeaMap(xqid, sid, new BasicDBObject("uid", 1).append("unm", 1));//教师信息
        Map<ObjectId, Grade> id_grade = gradeDao.findGradeListBySchoolIdMap(xqid, sid);//年级
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(xqid);//班级
        HSSFWorkbook wb = new HSSFWorkbook();
        for (int i = 0; i < jxbEntryList.size(); i++) {//一个教学班生成一个sheet
	        //生成一个sheet1
        	HSSFSheet sheet = wb.createSheet(jxbEntryList.get(i).getName() + "学生名单");
	        HSSFRow row = sheet.createRow(0);
	        HSSFCell cell = row.createCell(0);
	        cell.setCellValue("姓名");
	        cell = row.createCell(1);
	        cell.setCellValue("学号");
	        cell = row.createCell(2);
	        cell.setCellValue("性别");
	        cell = row.createCell(3);
	        cell.setCellValue("层级");
	        cell = row.createCell(4);
	        cell.setCellValue("教师");
	        cell = row.createCell(5);
	        cell.setCellValue("教学班");
	        cell = row.createCell(6);
	        cell.setCellValue("年级");
	        cell = row.createCell(7);
	        cell.setCellValue("行政班");
	        int count = 1;// 学生计数
        
            String teacherName = "";
            String gradeName = "";
            if (id_tea.get(jxbEntryList.get(i).getTercherId()) != null) {
                teacherName = id_tea.get(jxbEntryList.get(i).getTercherId()).getUserName();
            }
            if (jxbEntryList.get(i).getGradeId() != null && id_grade.get(jxbEntryList.get(i).getGradeId()) != null) {
                gradeName = id_grade.get(jxbEntryList.get(i).getGradeId()).getName();
            }
            String jxbName = "";
            if (StringUtils.isNotEmpty(jxbEntryList.get(i).getNickName())) {
                jxbName = jxbEntryList.get(i).getNickName();
            } else {
                jxbName = jxbEntryList.get(i).getName();
            }
            for (int j = 0; j < jxbEntryList.get(i).getStudentIds().size(); j++) {
                ObjectId stuid = jxbEntryList.get(i).getStudentIds().get(j);
                String stuName = "";
                String sn = "";
                String sex = "";
                String level = "";
                String classname = "";
                HSSFRow temprow = sheet.createRow(count);

                if (id_stu.get(stuid) != null) {
                    stuName = id_stu.get(stuid).getUserName();
                    sn = id_stu.get(stuid).getStudyNum();
                    sex = id_stu.get(stuid).getSex() == 0 ? "女" : "男";
                    level = String.valueOf((Integer) id_stu.get(stuid).getLevel());
                    ClassEntry classEntry = classEntryMap.get(id_stu.get(stuid).getClassId());
                    if(classEntry!=null){
                        classname = classEntry.getClassName();
                    }
                }

                HSSFCell tempcell = temprow.createCell(0);
                tempcell.setCellValue(stuName);
                tempcell = temprow.createCell(1);
                tempcell.setCellValue(sn);
                tempcell = temprow.createCell(2);
                tempcell.setCellValue(sex);
                tempcell = temprow.createCell(3);
                tempcell.setCellValue(level);
                tempcell = temprow.createCell(4);
                tempcell.setCellValue(teacherName);
                tempcell = temprow.createCell(5);
                tempcell.setCellValue(jxbName);
                tempcell = temprow.createCell(6);
                tempcell.setCellValue(gradeName);
                tempcell = temprow.createCell(7);
                tempcell.setCellValue(classname);
                count++;
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("教学班学生.xls", "UTF-8"));
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
    

    //教學班學生列表
    public List<Map<String, Object>> getjxbStudentByJxbId(ObjectId jxbid, ObjectId sid, ObjectId gid) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<ObjectId, Grade> id_grade = gradeDao.findGradeListBySchoolIdMap(getDefaultPaiKeTerm(sid).getPaikeci(), sid);
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(gid, getDefaultPaiKeTerm(sid).getPaikeci());
        N33_JXBEntry entry = n33_jxbDao.getJXBById(jxbid);
        List<ObjectId> studentIds = entry.getStudentIds();
        Map<ObjectId, StudentEntry> studentEntryMap = n33_studentDao.getStudentByXqidMap(getDefaultPaiKeTerm(sid).getPaikeci(), studentIds);
        for (ObjectId student : studentIds) {
            StudentEntry entry1 = studentEntryMap.get(student);
            if (entry1 != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("grade", id_grade.get(entry1.getGradeId()) == null ? "" : id_grade.get(entry1.getGradeId()).getName());
                map.put("xh", classEntryMap.get(entry1.getClassId()) == null ? "" : classEntryMap.get(entry1.getClassId()).getXh());
                map.put("name", entry1.getUserName());
                map.put("lev",entry1.getLevel()==0?"无":entry1.getLevel().toString());
                map.put("sn", entry1.getStudyNum());
                if(entry1.getSex() == 0){
                    map.put("sex","女");
                }else if(entry1.getSex() == 1){
                    map.put("sex","男");
                }else {
                    map.put("sex","未知");
                }
                map.put("clsName", entry1.getClassName());
                map.put("userId", entry1.getUserId().toString());
                map.put("jxbid", jxbid.toString());
                map.put("conn", entry1.getCombiname());
                result.add(map);
            }
        }
        return result;
    }

    @Deprecated
    public Map<String, Object> autoSetTeacherAndClassRoom_bak(List<Integer> indexes, List<String> jxbIds, ObjectId ciId, ObjectId sid, ObjectId subId, ObjectId gid) {
        Map<String, Object> result = new HashMap<String, Object>();

        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid, gid, ciId, 1);
        int acClassType = 0;
        if(null!=turnOff){
            acClassType = turnOff.getAcClass();
        }

        List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBListByIds(MongoUtils.convertToObjectIdList(jxbIds));
        if (indexes.contains(1)) {
            List<N33_JXBEntry> subJxbs = n33_jxbDao.getJXBList(sid, gid, subId, ciId, acClassType);
            subJxbs.removeAll(jxbEntryList);// 非本次要排老师的教学班
            List<ObjectId> hasClassTeachers = new ArrayList<ObjectId>();
            for (N33_JXBEntry jxbEntry : subJxbs) {
                hasClassTeachers.add(jxbEntry.getTercherId());
            }

            List<N33_TeaDTO> gradeTeacher = isolateUserService.getTeaByGradeAndSubject(ciId, sid, gid.toString(), subId.toString(), "*");
            List<ObjectId> gradeTeacherIds = new ArrayList<ObjectId>();
            Map<ObjectId, N33_TeaDTO> teaDTOMap = new HashMap<ObjectId, N33_TeaDTO>();
            for (N33_TeaDTO teaDTO : gradeTeacher) {
                gradeTeacherIds.add(new ObjectId(teaDTO.getId()));
                teaDTOMap.put(new ObjectId(teaDTO.getId()), teaDTO);
            }
            gradeTeacherIds.removeAll(hasClassTeachers);
            Integer arrangedTeacherNum = jxbIds.size();
            List<ObjectId> arrangedTeacherList = new ArrayList<ObjectId>();
            if (arrangedTeacherNum <= gradeTeacherIds.size()) {
                int[] arr = new int[gradeTeacherIds.size()];
                for (int i = 0; i < arrangedTeacherNum; i++) {
                    while (true) {
                        int random = (int) (Math.random() * gradeTeacherIds.size());
                        if (arr[random] != 1) {//不重复
                            arrangedTeacherList.add(gradeTeacherIds.get(random));
                            arr[random] = 1;
                            break;
                        }
                    }

                }
            } else {// 该学科不在待排教学班的老师不够的情况下,
                int diff = arrangedTeacherNum - gradeTeacherIds.size();
                for (ObjectId id : gradeTeacherIds) {
                    arrangedTeacherList.add(id);
                }
                if (hasClassTeachers.size() != 0) {
                    for (int i = 0; i < arrangedTeacherNum; i++) {
                        int random = (int) (Math.random() * gradeTeacherIds.size());
                        arrangedTeacherList.add(hasClassTeachers.get(random));
                    }
                } else {
                    result.put("status1", 1);
                    result.put("reason", "没有可排教师");
                    return result;
                }

            }
            for (int i = 0; i < jxbEntryList.size(); i++) {
                N33_JXBEntry jxb = jxbEntryList.get(i);
                jxb.setTercherId(arrangedTeacherList.get(i));
            }
            n33_jxbDao.addN33_JXBEntrys(jxbEntryList);
            result.put("status1", 0);
        }
        return result;
    }

    public Map<String, Object> autoSetTeacherAndClassRoom(List<Integer> indexes, List<String> jxbIds, ObjectId ciId, ObjectId sid, ObjectId subId, ObjectId gid) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBListByIds(MongoUtils.convertToObjectIdList(jxbIds));
        if (indexes.contains(1)) {
            List<N33_TeaDTO> gradeTeacher = isolateUserService.getTeaByGradeAndSubject(ciId, sid, gid.toString(), subId.toString(), "*");
            List<ObjectId> gradeTeacherIds = new ArrayList<ObjectId>();
            Map<ObjectId, N33_TeaDTO> teaDTOMap = new HashMap<ObjectId, N33_TeaDTO>();
            for (N33_TeaDTO teaDTO : gradeTeacher) {
                gradeTeacherIds.add(new ObjectId(teaDTO.getUid()));
                teaDTOMap.put(new ObjectId(teaDTO.getUid()), teaDTO);
            }
            if (gradeTeacherIds.size() != 0) {
                Integer arrangedTeacherNum = jxbIds.size();
                List<ObjectId> arrangedTeacherList = new ArrayList<ObjectId>();

                for (int i = 0; i < arrangedTeacherNum; i++) {
                    int random = (int) (Math.random() * gradeTeacherIds.size());
                    arrangedTeacherList.add(gradeTeacherIds.get(random));
                }
                for (int i = 0; i < jxbEntryList.size(); i++) {
                    N33_JXBEntry jxb = jxbEntryList.get(i);
                    jxb.setTercherId(arrangedTeacherList.get(i));
                }
                n33_jxbDao.removeN33_JXBEntry(MongoUtils.convertToObjectIdList(jxbIds));
                n33_jxbDao.addN33_JXBEntrys(jxbEntryList);
                result.put("status1", 0);
            } else {
                result.put("status1", 1);
                result.put("reason1", "没有可排教师");
            }

        }
        if (indexes.contains(2)) {
            autoSetClassRoom(jxbEntryList, ciId, sid, subId, jxbIds, gid, result);
        }
        return result;
    }

    public void autoSetClassRoom(List<N33_JXBEntry> jxbEntryList, ObjectId ciId, ObjectId sid, ObjectId subId, List<String> jxbIds, ObjectId gid, Map<String, Object> result) {
        List<StudentEntry> studentEntries = n33_studentDao.getStudentByXqidAndGradeId(gid, ciId, new BasicDBObject("uid", 1).append("cid", 1).append("cnm", 1));
        Map<ObjectId, N33_ClassroomEntry> classId_room = n33_classroomDao.getRoomEntryListByXqGradeMapOnlyArranged(ciId, gid);
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(gid, ciId);
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gid, ciId);

        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid, gid, ciId, 1);
        int acClassType = 0;
        if(null!=turnOff){
            acClassType = turnOff.getAcClass();
        }

        List<N33_JXBEntry> subjectJxbs = n33_jxbDao.getJXBList(sid, gid, subId, ciId, acClassType);
        Map<ObjectId, Integer> roomId_count = new HashMap<ObjectId, Integer>();
        for (N33_JXBEntry jxbEntry : subjectJxbs) { // 计算该学科所有教学班使用的教室  使用次数
            if (jxbEntry.getClassroomId() != null) {
                int count = roomId_count.get(jxbEntry.getClassroomId()) == null ? 0 : roomId_count.get(jxbEntry.getClassroomId());
                count++;
                roomId_count.put(jxbEntry.getClassroomId(), count);
            }
        }
        Map<ObjectId, StudentEntry> id_stu = new HashMap<ObjectId, StudentEntry>();
        for (StudentEntry studentEntry : studentEntries) {
            id_stu.put(studentEntry.getUserId(), studentEntry);
        }
        if (classId_room.size() == 0) {
            result.put("status2", 1);
            result.put("reason2", "没有可排教室");
        } else {
            for (N33_JXBEntry jxbEntry : jxbEntryList) {
                Map<ObjectId, Integer> classId_num = new HashMap<ObjectId, Integer>();
                for (ObjectId userId : jxbEntry.getStudentIds()) {
                    StudentEntry studentEntry = id_stu.get(userId);
                    ClassEntry classEntry = null;
                    if (studentEntry != null && studentEntry.getClassId() != null) {
                        classEntry = classEntryMap.get(studentEntry.getClassId());
                    }
                    if (classEntry != null && classEntry.getXh() != null) {
                        Integer i = classId_num.get(classEntry.getClassId()) == null ? 0 : classId_num.get(classEntry.getClassId());
                        classId_num.put(classEntry.getClassId(), i + 1);
                    }
                }
                List<Map.Entry<ObjectId, Integer>> classId_numEntryList = new ArrayList<Map.Entry<ObjectId, Integer>>(classId_num.entrySet());
                Collections.sort(classId_numEntryList, new Comparator<Map.Entry<ObjectId, Integer>>() {
                    @Override
                    public int compare(Map.Entry<ObjectId, Integer> o1, Map.Entry<ObjectId, Integer> o2) {
                        return o2.getValue() - o1.getValue();
                    }
                });
                if (classId_numEntryList.size() > 0) {
                    for (Map.Entry<ObjectId, Integer> entry : classId_numEntryList) { // 从学生来源 最多到最少的教室依次往下
                        ObjectId classId = entry.getKey();
                        if (roomId_count.get(classId) != null && roomId_count.get(classId) > 0) { // 如果该教室已经被使用，则跳过
                            continue;
                        }
                        N33_ClassroomEntry classroomEntry = null;
                        classroomEntry = classId_room.get(classId);
                        if (classroomEntry != null) {
                            jxbEntry.setClassroomId(classroomEntry.getRoomId());
                        }
                    }

                }

            }
            // 第二轮补全没有设置教室的
            for (N33_JXBEntry jxbEntry : jxbEntryList) {
                if (jxbEntry.getClassroomId() == null) {
                    Map<ObjectId, Integer> classId_num = new HashMap<ObjectId, Integer>();
                    for (ObjectId userId : jxbEntry.getStudentIds()) {
                        StudentEntry studentEntry = id_stu.get(userId);
                        ClassEntry classEntry = null;
                        if (studentEntry != null && studentEntry.getClassId() != null) {
                            classEntry = classEntryMap.get(studentEntry.getClassId());
                        }
                        if (classEntry != null && classEntry.getXh() != null) {
                            Integer i = classId_num.get(classEntry.getClassId()) == null ? 0 : classId_num.get(classEntry.getClassId());
                            classId_num.put(classEntry.getClassId(), i + 1);
                        }
                    }
                    List<Map.Entry<ObjectId, Integer>> classId_numEntryList = new ArrayList<Map.Entry<ObjectId, Integer>>(classId_num.entrySet());
                    Collections.sort(classId_numEntryList, new Comparator<Map.Entry<ObjectId, Integer>>() {
                        @Override
                        public int compare(Map.Entry<ObjectId, Integer> o1, Map.Entry<ObjectId, Integer> o2) {
                            return o2.getValue() - o1.getValue();
                        }
                    });
                    if (classId_numEntryList.size() > 0) {
                        ObjectId classId = classId_numEntryList.get(0).getKey();
                        N33_ClassroomEntry classroomEntry = null;
                        classroomEntry = classId_room.get(classId);
                        if (classroomEntry != null) {
                            jxbEntry.setClassroomId(classroomEntry.getRoomId());
                        }
                    }
                }

            }
        }
        n33_jxbDao.removeN33_JXBEntry(MongoUtils.convertToObjectIdList(jxbIds));
        n33_jxbDao.addN33_JXBEntrys(jxbEntryList);
        result.put("status2", 0);
    }
    public void autoSetTeachersAndRoomAll(ObjectId ciId, ObjectId sid,  List<String> subjectIds, ObjectId gid, List<Integer> indexes){
        Map<String, Object> result = new HashMap<String, Object>();
        if (indexes.contains(1)) {
            for(String subjectId:subjectIds){
            	//jxb add
            	int acClassType = turnOffService.getAcClassType(sid, gid, ciId,1);
            	//jxb add
                List<N33_TeaDTO> gradeTeacher = isolateUserService.getTeaByGradeAndSubject(ciId, sid, gid.toString(), subjectId, "*");
                List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBList(sid, gid, MongoUtils.convertToObjectIdList(Arrays.asList(subjectId)), ciId, Arrays.asList(1,2),acClassType);
                //List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBList(sid, gid, MongoUtils.convertToObjectIdList(Arrays.asList(subjectId)), ciId, Arrays.asList(1,2), acClassType);
                List<ObjectId> jxbIds = new ArrayList<ObjectId>();
                for(N33_JXBEntry entry:jxbEntryList){
                    jxbIds.add(entry.getID());
                }
                List<ObjectId> gradeTeacherIds = new ArrayList<ObjectId>();
                Map<ObjectId, N33_TeaDTO> teaDTOMap = new HashMap<ObjectId, N33_TeaDTO>();
                for (N33_TeaDTO teaDTO : gradeTeacher) {
                    gradeTeacherIds.add(new ObjectId(teaDTO.getUid()));
                    teaDTOMap.put(new ObjectId(teaDTO.getUid()), teaDTO);
                }
                if (gradeTeacherIds.size() != 0) {
                    Integer arrangedTeacherNum = jxbIds.size();
                    List<ObjectId> arrangedTeacherList = new ArrayList<ObjectId>();

                    for (int i = 0; i < arrangedTeacherNum; i++) {
                        int random = (int) (Math.random() * gradeTeacherIds.size());
                        arrangedTeacherList.add(gradeTeacherIds.get(random));
                    }
                    for (int i = 0; i < jxbEntryList.size(); i++) {
                        N33_JXBEntry jxb = jxbEntryList.get(i);
                        jxb.setTercherId(arrangedTeacherList.get(i));
                    }
                    n33_jxbDao.removeN33_JXBEntry(jxbIds);
                    n33_jxbDao.addN33_JXBEntrys(jxbEntryList);
                    result.put("status1", 0);
                } else {
                    result.put("status1", 1);
                    result.put("reason1", "没有可排教师");
                }
            }


        }
        if (indexes.contains(2)) {
            autoSetClassRoomAll(ciId, sid,  subjectIds, gid, result);
        }
    }

    public void autoSetClassRoomAll(ObjectId ciId, ObjectId sid,  List<String> subjectIds, ObjectId gid, Map<String, Object> result) {
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid, gid, ciId, 1);
        int acClassType = 0;
        if(null!=turnOff){
            acClassType = turnOff.getAcClass();
        }
        for(String subjectId:subjectIds){
            List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBList(sid, gid, new ObjectId(subjectId), ciId, acClassType);
            autoSetClassRoom2(jxbEntryList,ciId,sid,new ObjectId(subjectId),gid,result);
        }
        List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBList(sid, gid, MongoUtils.convertToObjectIdList(subjectIds), ciId, Arrays.asList(1,2),acClassType);
        Map<ObjectId, Integer> roomId_count = new HashMap<ObjectId, Integer>();
        Map<ObjectId, LinkedList<N33_JXBEntry>> roomId_jxbs = new HashMap<ObjectId, LinkedList<N33_JXBEntry>>();
        Map<ObjectId, N33_ClassroomEntry> classId_room = n33_classroomDao.getRoomEntryListByXqGradeMapOnlyArranged(ciId, gid);
        for(N33_ClassroomEntry room:classId_room.values()){
            roomId_jxbs.put(room.getRoomId(),new LinkedList<N33_JXBEntry>());
            roomId_count.put(room.getRoomId(),0);
        }
        for (N33_JXBEntry jxbEntry : jxbEntryList) { // 计算所有教学班使用的教室  使用次数
            if (jxbEntry.getClassroomId() != null) {
                int count = roomId_count.get(jxbEntry.getClassroomId()) == null ? 0 : roomId_count.get(jxbEntry.getClassroomId());
                LinkedList<N33_JXBEntry> jxbs = roomId_jxbs.get(jxbEntry.getClassroomId()) == null ? new LinkedList<N33_JXBEntry>() : roomId_jxbs.get(jxbEntry.getClassroomId());
                count++;
                jxbs.add(jxbEntry);
                roomId_count.put(jxbEntry.getClassroomId(), count);
                roomId_jxbs.put(jxbEntry.getClassroomId(), jxbs);
            }
        }
        int average = 0;
        int sum = 0;
        for(int count:roomId_count.values()){
            sum+=count;
        }
        average = sum/roomId_count.size();
        LinkedList<N33_JXBEntry> surplusList = new LinkedList<N33_JXBEntry>();
        for(Map.Entry<ObjectId, LinkedList<N33_JXBEntry>> entry:roomId_jxbs.entrySet()){
            LinkedList<N33_JXBEntry> list = entry.getValue();
            if(list.size()>average){
                int diff = list.size()-average;
                while(diff!=0){
                    surplusList.add(list.pop());
                    diff--;
                }
            }
        }
        for(Map.Entry<ObjectId, LinkedList<N33_JXBEntry>> entry:roomId_jxbs.entrySet()){
            LinkedList<N33_JXBEntry> list = entry.getValue();
            ObjectId roomId = entry.getKey();
            if(list.size()<average){
                int diff = average-list.size();
                while(diff!=0){
                    if(!surplusList.isEmpty()){
                        N33_JXBEntry jxbEntry = surplusList.pop();
                        jxbEntry.setClassroomId(roomId);
                        list.add(jxbEntry);
                        diff--;
                    }
                }
            }
        }
        while(surplusList.size()>0){
            for(Map.Entry<ObjectId, LinkedList<N33_JXBEntry>> entry:roomId_jxbs.entrySet()){
                LinkedList<N33_JXBEntry> list = entry.getValue();
                ObjectId roomId = entry.getKey();
                if(!surplusList.isEmpty()){
                    N33_JXBEntry jxbEntry = surplusList.pop();
                    jxbEntry.setClassroomId(roomId);
                    list.add(jxbEntry);
                }
            }
        }
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        for(N33_JXBEntry entry:jxbEntryList){
            jxbIds.add(entry.getID());
        }
        n33_jxbDao.removeN33_JXBEntry(jxbIds);
        n33_jxbDao.addN33_JXBEntrys(jxbEntryList);
    }

    public void autoSetClassRoom2(List<N33_JXBEntry> jxbEntryList, ObjectId ciId, ObjectId sid, ObjectId subId,  ObjectId gid, Map<String, Object> result) {
        List<StudentEntry> studentEntries = n33_studentDao.getStudentByXqidAndGradeId(gid, ciId, new BasicDBObject("uid", 1).append("cid", 1).append("cnm", 1));
        Map<ObjectId, N33_ClassroomEntry> classId_room = n33_classroomDao.getRoomEntryListByXqGradeMapOnlyArranged(ciId, gid);
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(gid, ciId);
        Map<ObjectId, Integer> roomId_count = new HashMap<ObjectId, Integer>();
        for(N33_ClassroomEntry classroomEntry:classId_room.values()){
            roomId_count.put(classroomEntry.getRoomId(),0);
        }
//        for (N33_JXBEntry jxbEntry : subjectJxbs) { // 计算该学科所有教学班使用的教室  使用次数
//            if (jxbEntry.getClassroomId() != null) {
//                int count = roomId_count.get(jxbEntry.getClassroomId()) == null ? 0 : roomId_count.get(jxbEntry.getClassroomId());
//                count++;
//                roomId_count.put(jxbEntry.getClassroomId(), count);
//            }
//        }
        Map<ObjectId, StudentEntry> id_stu = new HashMap<ObjectId, StudentEntry>();
        for (StudentEntry studentEntry : studentEntries) {
            id_stu.put(studentEntry.getUserId(), studentEntry);
        }
        if (classId_room.size() == 0) {
            result.put("status2", 1);
            result.put("reason2", "没有可排教室");
        } else {
            for (N33_JXBEntry jxbEntry : jxbEntryList) {
                Map<ObjectId, Integer> classId_num = new HashMap<ObjectId, Integer>();
                for (ObjectId userId : jxbEntry.getStudentIds()) {
                    StudentEntry studentEntry = id_stu.get(userId);
                    ClassEntry classEntry = null;
                    if (studentEntry != null && studentEntry.getClassId() != null) {
                        classEntry = classEntryMap.get(studentEntry.getClassId());
                    }
                    if (classEntry != null && classEntry.getXh() != null) {
                        Integer i = classId_num.get(classEntry.getClassId()) == null ? 0 : classId_num.get(classEntry.getClassId());
                        classId_num.put(classEntry.getClassId(), i + 1);
                    }
                }
                List<Map.Entry<ObjectId, Integer>> classId_numEntryList = new ArrayList<Map.Entry<ObjectId, Integer>>(classId_num.entrySet());
                Collections.sort(classId_numEntryList, new Comparator<Map.Entry<ObjectId, Integer>>() {
                    @Override
                    public int compare(Map.Entry<ObjectId, Integer> o1, Map.Entry<ObjectId, Integer> o2) {
                        return o2.getValue() - o1.getValue();
                    }
                });
                if (classId_numEntryList.size() > 0) {
                    for (Map.Entry<ObjectId, Integer> entry : classId_numEntryList) { // 从学生来源 最多到最少的教室依次往下
                        ObjectId classId = entry.getKey();
                        if (roomId_count.get(classId) != null && roomId_count.get(classId) > 0) { // 如果该教室已经被使用，则跳过
                            continue;
                        }
                        N33_ClassroomEntry classroomEntry = null;
                        classroomEntry = classId_room.get(classId);
                        if (classroomEntry != null) {
                            jxbEntry.setClassroomId(classroomEntry.getRoomId());
                        }
                        roomId_count.put(classroomEntry.getRoomId(),roomId_count.get(classroomEntry.getRoomId())==null?1:
                                roomId_count.get(classroomEntry.getRoomId())+1);
                    }

                }

            }
//            // 第二轮补全没有设置教室的
//            for (N33_JXBEntry jxbEntry : jxbEntryList) {
//                if (jxbEntry.getClassroomId() == null) {
//                    Map<ObjectId, Integer> classId_num = new HashMap<ObjectId, Integer>();
//                    for (ObjectId userId : jxbEntry.getStudentIds()) {
//                        StudentEntry studentEntry = id_stu.get(userId);
//                        ClassEntry classEntry = null;
//                        if (studentEntry != null && studentEntry.getClassId() != null) {
//                            classEntry = classEntryMap.get(studentEntry.getClassId());
//                        }
//                        if (classEntry != null && classEntry.getXh() != null) {
//                            Integer i = classId_num.get(classEntry.getClassId()) == null ? 0 : classId_num.get(classEntry.getClassId());
//                            classId_num.put(classEntry.getClassId(), i + 1);
//                        }
//                    }
//                    List<Map.Entry<ObjectId, Integer>> classId_numEntryList = new ArrayList<Map.Entry<ObjectId, Integer>>(classId_num.entrySet());
//                    Collections.sort(classId_numEntryList, new Comparator<Map.Entry<ObjectId, Integer>>() {
//                        @Override
//                        public int compare(Map.Entry<ObjectId, Integer> o1, Map.Entry<ObjectId, Integer> o2) {
//                            return o2.getValue() - o1.getValue();
//                        }
//                    });
//                    if (classId_numEntryList.size() > 0) {
//                        ObjectId classId = classId_numEntryList.get(0).getKey();
//                        N33_ClassroomEntry classroomEntry = null;
//                        classroomEntry = classId_room.get(classId);
//                        if (classroomEntry != null) {
//                            jxbEntry.setClassroomId(classroomEntry.getRoomId());
//                        }
//                    }
//                }
//
//            }
        }
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        for(N33_JXBEntry entry:jxbEntryList){
            jxbIds.add(entry.getID());
        }
        n33_jxbDao.removeN33_JXBEntry(jxbIds);
        n33_jxbDao.addN33_JXBEntrys(jxbEntryList);
        result.put("status2", 0);
    }

    public void clearSet(List<String> jxbIds) {
        List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBListByIds(MongoUtils.convertToObjectIdList(jxbIds));
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            jxbEntry.setTercherId(null);
            jxbEntry.setClassroomId(null);
        }
        n33_jxbDao.removeN33_JXBEntry(MongoUtils.convertToObjectIdList(jxbIds));
        n33_jxbDao.addN33_JXBEntrys(jxbEntryList);
    }

    public Map<String, Object> getZuoBanJxbInfo(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int jxbType) {
        Map<String, Object> reMap = new HashMap<String, Object>();

        Grade grade = gradeDao.findIsolateGradeByGradeId(ciId, schoolId, gradeId);
        //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add

        int maxSerial = 1;
        Map<Integer, List<N33_JXBEntry>> serialJxbsMap = new HashMap<Integer, List<N33_JXBEntry>>();

        if(acClassType==1) {
            List<N33_JXBEntry> jxbEntries = n33_jxbDao.getJXBList(schoolId, gradeId, ciId, jxbType, acClassType);
            for (N33_JXBEntry jxb : jxbEntries) {
                if (jxb.getTimeCombSerial() > maxSerial) {
                    maxSerial = jxb.getTimeCombSerial();
                }
                List<N33_JXBEntry> serialJxbs = serialJxbsMap.get(jxb.getTimeCombSerial());
                if (null == serialJxbs) {
                    serialJxbs = new ArrayList<N33_JXBEntry>();
                }
                serialJxbs.add(jxb);
                serialJxbsMap.put(jxb.getTimeCombSerial(), serialJxbs);
            }
        }else{
            Map<ObjectId, N33_JXBEntry> jxbMap = n33_jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);
            List<N33_TimeCombineEntry> timeCombEntries = combineDao.getTimeCombineByType(ciId, schoolId, gradeId, jxbType);
            for (N33_TimeCombineEntry timeComb : timeCombEntries) {
                List<N33_TimeCombineEntry.ZuHeList> zuHeList = timeComb.getZuHeList();
                for(N33_TimeCombineEntry.ZuHeList zuHe:zuHeList){
                    if(null != zuHe.getJxbId()) {
                        N33_JXBEntry jxb = jxbMap.get(zuHe.getJxbId());
                        if(null!=jxb) {
                            if (zuHe.getSerial() > maxSerial) {
                                maxSerial = zuHe.getSerial();
                            }
                            List<N33_JXBEntry> serialJxbs = serialJxbsMap.get(zuHe.getSerial());
                            if (null == serialJxbs) {
                                serialJxbs = new ArrayList<N33_JXBEntry>();
                            }
                            serialJxbs.add(jxb);
                            serialJxbsMap.put(zuHe.getSerial(), serialJxbs);
                        }
                    }
                }
            }
        }
        List<StudentEntry> studentEntries = n33_studentDao.getStudentByXqidAndGradeId(gradeId, ciId, new BasicDBObject("uid", 1).append("cid", 1).append("cnm", 1));

        Map<ObjectId, N33_ClassroomEntry> classId_room = n33_classroomDao.getRoomMapByXqGradeMap(ciId, gradeId);

        Map<ObjectId, N33_TeaEntry> tedId_tea = n33_teaDao.getTeaMap(ciId, schoolId, gradeId);

        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(gradeId, ciId);

        Map<ObjectId, StudentEntry> id_stu = new HashMap<ObjectId, StudentEntry>();
        for (StudentEntry studentEntry : studentEntries) {
            id_stu.put(studentEntry.getUserId(), studentEntry);
        }

        List<Map<String, Object>> serailList = new ArrayList<Map<String, Object>>();
        List<Map<String, N33_JXBDTO>> dataList = new ArrayList<Map<String, N33_JXBDTO>>();
        for(int serail = 1; serail<=maxSerial; serail++){
            Map<String, Object> serailMap = new HashMap<String, Object>();
            serailMap.put("serail", serail);
            serailMap.put("acClassType", acClassType);
            serailList.add(serailMap);
            List<N33_JXBEntry> serialJxbs = serialJxbsMap.get(serail);
            if(null!=serialJxbs){
                boolean isStart = false;
                if(dataList.size()==0){
                    isStart = true;
                }
                int i = 0;
                for (N33_JXBEntry jxbEntry : serialJxbs) {
                    List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
                    N33_JXBDTO n33_jxbdto = new N33_JXBDTO(jxbEntry);
                    n33_jxbdto.setTimeCombSerial(serail);
                    N33_ClassroomEntry classRoom = classId_room.get(jxbEntry.getClassroomId());
                    String classRoomName = "";
                    // 设置教室名称
                    if (null!= classRoom) {
                        classRoomName =classRoom.getRoomName();
                        if (classRoom.getClassName() != null) {
                            classRoomName += "(" + classRoom.getClassName() + ")";
                        }
                    }
                    n33_jxbdto.setClassroomName(classRoomName);
                    N33_TeaEntry tea = tedId_tea.get(jxbEntry.getTercherId());
                    if (null!=tea) {
                        n33_jxbdto.setTeacherName(tea.getUserName());
                    } else {
                        n33_jxbdto.setTeacherName("");
                    }

                    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
                    for (ObjectId userId : jxbEntry.getStudentIds()) {
                        StudentEntry studentEntry = id_stu.get(userId);
                        ClassEntry classEntry = null;
                        if (studentEntry != null && studentEntry.getClassId() != null) {
                            classEntry = classEntryMap.get(studentEntry.getClassId());
                        }
                        if (classEntry != null && classEntry.getXh() != null) {
                            int stuCount = map.get(classEntry.getXh()) == null ? 0 : map.get(classEntry.getXh());
                            map.put(classEntry.getXh(), stuCount + 1);
                        }
                    }
                    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                        Map<String, Object> tempMap = new HashMap<String, Object>();
                        String className = grade.getName() + "（" + entry.getKey() + "）班";
                        tempMap.put("className", className);
                        tempMap.put("num", entry.getValue());
                        classList.add(tempMap);
                    }
                    n33_jxbdto.setClass_count(classList);
                    if(isStart){
                        Map<String, N33_JXBDTO> tamp = new HashMap<String, N33_JXBDTO>();
                        tamp.put("one", n33_jxbdto);
                        tamp.put("two", null);
                        tamp.put("three", null);
                        dataList.add(tamp);
                    }else{
                        if(serail==2) {
                            if (dataList.size() > i) {
                                dataList.get(i).put("two", n33_jxbdto);
                            } else {
                                Map<String, N33_JXBDTO> tamp = new HashMap<String, N33_JXBDTO>();
                                tamp.put("one", null);
                                tamp.put("two", n33_jxbdto);
                                tamp.put("three", null);
                                dataList.add(tamp);
                            }
                        }
                        if(serail==3) {
                            if (dataList.size() > i) {
                                dataList.get(i).put("three", n33_jxbdto);
                            } else {
                                Map<String, N33_JXBDTO> tamp = new HashMap<String, N33_JXBDTO>();
                                tamp.put("one", null);
                                tamp.put("two", null);
                                tamp.put("three", n33_jxbdto);
                                dataList.add(tamp);
                            }
                        }
                        i++;
                    }
                }
            }
        }
        reMap.put("serailList", serailList);
        reMap.put("dataList", dataList);
        reMap.put("maxSerial", maxSerial);
        return reMap;
    }

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param subId
     * @param ciId
     * @param serial
     * @param jxbType
     * @return
     */
    public List<Map<String, Object>> getZbJxbSerialTeaList(ObjectId schoolId, ObjectId gradeId, ObjectId subId, ObjectId ciId, ObjectId currJxbId, int serial, int jxbType) {
        //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add
        List<ObjectId> teaList = new ArrayList<ObjectId>();
        if(acClassType==1) {
            List<N33_JXBEntry> jxbEntries = n33_jxbDao.getJXBList(schoolId, gradeId, ciId, jxbType, acClassType, serial);
            for (N33_JXBEntry jxbEntry : jxbEntries) {
                if(jxbEntry.getTercherId() != null){
                    if(!teaList.contains(jxbEntry.getTercherId())){
                        teaList.add(jxbEntry.getTercherId());
                    }
                }
            }
        }else{
            Map<ObjectId, N33_JXBEntry> jxbEntryMap = n33_jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);
            List<N33_TimeCombineEntry> timeCombineEntryList = combineDao.getTimeCombineByType(ciId, schoolId, gradeId, jxbType);
            for (N33_TimeCombineEntry timeCombineEntry : timeCombineEntryList) {
                for (N33_TimeCombineEntry.ZuHeList zuHeList : timeCombineEntry.getZuHeList()) {
                    if(serial == zuHeList.getSerial()){
                        if(zuHeList.getJxbId() != null && !"".equals(zuHeList.getJxbId().toString()) ){
                            N33_JXBEntry jxbEntry = jxbEntryMap.get(zuHeList.getJxbId());
                            if(jxbEntry != null && jxbEntry.getTercherId() != null){
                                if(!teaList.contains(jxbEntry.getTercherId())){
                                    teaList.add(jxbEntry.getTercherId());
                                }
                            }
                        }
                    }
                }
            }
        }
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(currJxbId);
        if(null!=jxbEntry&&null!=jxbEntry.getTercherId()){
            teaList.remove(jxbEntry.getTercherId());
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<N33_TeaEntry> teaEntries = n33_teaDao.getTeaListByGradeId(ciId, schoolId, gradeId, subId);
        for (N33_TeaEntry tea : teaEntries) {
            if(!teaList.contains(tea.getUserId())){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", tea.getUserName());
                map.put("id", tea.getUserId().toString());
                result.add(map);
            }
        }
        return result;
    }

    public List<Map<String, Object>> getZbJxbSerialClsRmList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, ObjectId currJxbId, int serial, int jxbType) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add
        List<N33_ClassroomEntry> list = n33_classroomDao.getRoomEntryListByXqGrade(ciId, schoolId, gradeId);

        List<ObjectId> roomList = new ArrayList<ObjectId>();
        if(acClassType==1) {
            List<N33_JXBEntry> jxbEntries = n33_jxbDao.getJXBList(schoolId, gradeId, ciId, jxbType, acClassType, serial);
            for (N33_JXBEntry jxbEntry : jxbEntries) {
                if(jxbEntry.getClassroomId() != null){
                    if(!roomList.contains(jxbEntry.getClassroomId())){
                        roomList.add(jxbEntry.getClassroomId());
                    }
                }
            }
        }else {
            List<N33_TimeCombineEntry> timeCombEntries = combineDao.getTimeCombineByType(ciId, schoolId, gradeId, jxbType);
            Map<ObjectId, N33_JXBEntry> jxbEntryMap = n33_jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);
            for (N33_TimeCombineEntry timeCombineEntry : timeCombEntries) {
                for (N33_TimeCombineEntry.ZuHeList zuHeList : timeCombineEntry.getZuHeList()) {
                    if(serial == zuHeList.getSerial()){
                        if(zuHeList.getJxbId() != null && !"".equals(zuHeList.getJxbId().toString()) ){
                            N33_JXBEntry jxbEntry = jxbEntryMap.get(zuHeList.getJxbId());
                            if(jxbEntry != null && jxbEntry.getClassroomId() != null){
                                if(!roomList.contains(jxbEntry.getClassroomId())){
                                    roomList.add(jxbEntry.getClassroomId());
                                }
                            }
                        }
                    }
                }
            }
        }
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(currJxbId);
        if(null!=jxbEntry&&null!=jxbEntry.getClassroomId()){
            roomList.remove(jxbEntry.getClassroomId());
        }
        for (N33_ClassroomEntry classRoom : list) {
            if(!roomList.contains(classRoom.getRoomId())){
                Map<String, Object> map = new HashMap<String, Object>();
                String classRoomName = "";
                // 设置教室名称
                if (null!= classRoom) {
                    classRoomName =classRoom.getRoomName();
                    if (classRoom.getClassName() != null) {
                        classRoomName += "(" + classRoom.getClassName() + ")";
                    }
                }
                map.put("name", classRoomName);
                map.put("id", classRoom.getRoomId().toString());
                result.add(map);
            }
        }
        return result;
    }

    public List<N33_JXBDTO> getNoSerialZbJxbList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int jxbType) {
        //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntries = n33_jxbDao.getNoSerialZbJxbList(schoolId, gradeId, ciId, jxbType, acClassType);
        List<N33_JXBDTO> reList = new ArrayList<N33_JXBDTO>();
        for (N33_JXBEntry jxb : jxbEntries) {
            N33_JXBDTO jxbDto = new N33_JXBDTO(jxb);
            reList.add(jxbDto);
        }
        return reList;
    }

    public void setZbJxbSerial(ObjectId jxbId, int serial) {
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        jxbEntry.setTimeCombSerial(serial);
        jxbEntry.setSerialState(Constant.ONE);
        n33_jxbDao.updateN33_JXB(jxbEntry);
    }
}
