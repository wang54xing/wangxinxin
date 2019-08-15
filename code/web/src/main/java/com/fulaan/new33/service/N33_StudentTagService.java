package com.fulaan.new33.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.N33_StudentTagDao;
import com.db.new33.N33_TimeCombineDao;
import com.db.new33.N33_VirtualClassDao;
import com.db.new33.autopk.N33_PartClassSetDao;
import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.new33.isolate.SubjectDao;
import com.db.new33.paike.N33_JXBCTDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_JXBKSDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.fulaan.new33.dto.N33_StudentTagDTO;
import com.fulaan.new33.dto.isolate.StudentDTO;
import com.fulaan.new33.service.autopk.N33_AutoTeaSetService;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.autoPK.N33_PartClassSetEntry;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.N33_XuNiBanEntry;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.new33.paike.N33_JXBCTEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_JXBKSEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.utils.MongoUtils;

@Service
public class N33_StudentTagService extends BaseService {

    private N33_StudentTagDao n33_studentTagDao = new N33_StudentTagDao();
    private N33_JXBDao n33_jxbDao = new N33_JXBDao();
    private ClassDao classDao = new ClassDao();
    private N33_StudentDao studentDao = new N33_StudentDao();
    private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();
    private SubjectDao subjectDao = new SubjectDao();
    private GradeDao gradeDao = new GradeDao();
    private N33_VirtualClassDao virtualClassDao = new N33_VirtualClassDao();

    private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();

    private N33_JXBDao jxbDao = new N33_JXBDao();

    private N33_TimeCombineDao combineDao = new N33_TimeCombineDao();

    private N33_PartClassSetDao partClassSetDao = new N33_PartClassSetDao();
    
    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

    @Autowired
    private N33_AutoTeaSetService autoTeaSetService;

    @Autowired
    private N33_FenBanInfoSetService infoSetService;

    @Autowired
    private N33_TurnOffService turnOffService;

    public List<N33_StudentTagDTO> getTagListByxqid(ObjectId xqid, ObjectId gradeId) {
        List<N33_StudentTagDTO> result = new ArrayList<N33_StudentTagDTO>();
        List<N33_StudentTagEntry> entryList = n33_studentTagDao.getTagListByxqid(xqid, gradeId);
        for (N33_StudentTagEntry entry : entryList) {
            result.add(new N33_StudentTagDTO(entry));
        }
        return result;
    }

    public void addStudentTag(N33_StudentTagDTO dto) {
        n33_studentTagDao.addStudentTag(dto.buildEntry());
    }


    public void addStuJinTag(Map<String, Object> map) {
        ObjectId tagId = new ObjectId((String) map.get("tagId"));
        ObjectId xqid = new ObjectId((String) map.get("xqid"));
        List<String> stuIds = (List<String>) map.get("stuList");
        List<ObjectId> studentIds = MongoUtils.convertToObjectIdList(stuIds);
        N33_StudentTagEntry tagEntry = n33_studentTagDao.getTagById(tagId);
        List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = tagEntry.getStudents();
        //学生
        Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidMap(xqid, studentIds);
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        for (StudentEntry studentEntry : studentEntryMap.values()) {
            if (classIds.add(studentEntry.getClassId())) ;
        }
        Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(classIds, xqid);
        for (ObjectId stuId : studentIds) {
            StudentEntry studentEntry = studentEntryMap.get(stuId);
            ClassEntry classEntry = classEntryMap.get(studentEntry.getClassId());
            N33_StudentTagEntry.StudentInfoEntry studentInfoEntry = new N33_StudentTagEntry.StudentInfoEntry(stuId, classEntry.getClassId(), classEntry.getName());
            studentInfoEntries.add(studentInfoEntry);
        }
        tagEntry.setStudents(studentInfoEntries);
        n33_studentTagDao.update(tagEntry);
    }

    public void delStudentByTagId(ObjectId tagId, ObjectId stuId) {
        List<N33_XuNiBanEntry> xuNiBanEntries = virtualClassDao.getN33_VirtualClassEntryTag(tagId);
        for (N33_XuNiBanEntry entry : xuNiBanEntries) {
            List<N33_XuNiBanEntry.StudentTag> tags = new ArrayList<N33_XuNiBanEntry.StudentTag>();
            for (N33_XuNiBanEntry.StudentTag tag : entry.getTagList()) {
                List<N33_XuNiBanEntry.SubjectInfo> infos = new ArrayList<N33_XuNiBanEntry.SubjectInfo>();
                Integer stuCount = 0;
                Integer a = 0;
                for (N33_XuNiBanEntry.SubjectInfo info : tag.getSubjectList()) {
                    List<ObjectId> oid = info.getStuIds();
                    oid.remove(stuId);
                    info.setStuIds(oid);
                    if (a == 0) {
                        stuCount += oid.size();
                    }
                    info.setStuCount(oid.size());
                    if (oid.size() > 0) {
                        infos.add(info);
                    }
                    a++;
                }
                tag.setSubjectList(infos);
                tag.setTagCount(stuCount);
                if (infos.size() > 0) {
                    tags.add(tag);
                }
            }
            entry.setTagList(tags);
            //tag等于0改为等待删除数据
            if (tags.size() == 0) {
                entry.setType(1000);
            }
            virtualClassDao.updateN33_VirtualClassEntry(entry);
        }
        virtualClassDao.delN33_VirtualClassEntryTag();
        N33_StudentTagEntry tagEntry = n33_studentTagDao.getTagById(tagId);
        List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
        for (N33_StudentTagEntry.StudentInfoEntry entry : tagEntry.getStudents()) {
            if (!entry.getStuId().toString().equals(stuId.toString())) {
                studentInfoEntries.add(entry);
            }
        }
        if (studentInfoEntries.size() > 0) {
            tagEntry.setStudents(studentInfoEntries);
            n33_studentTagDao.update(tagEntry);
        } else {
            n33_studentTagDao.removeById(tagEntry.getID());
        }
        List<ObjectId> jxbIds = tagEntry.getJxbIds();
        for (N33_XuNiBanEntry entry : xuNiBanEntries) {
            jxbIds.addAll(entry.getJxbIds());
        }
        //教学班
        List<N33_JXBEntry> jxbctEntries = n33_jxbDao.getJXBListByIds(jxbIds);
        for (N33_JXBEntry jxbEntry : jxbctEntries) {
            List<ObjectId> stuIds = jxbEntry.getStudentIds();
            stuIds.remove(stuId);
            jxbEntry.setStudentIds(stuIds);
            n33_jxbDao.updateN33_JXB(jxbEntry);
        }
    }

    public void removeTag(ObjectId id,ObjectId sid) {
        combineDao.removeByTagId(id,sid);
        N33_StudentTagEntry studentTagEntry = n33_studentTagDao.getTagById(id);
        List<ObjectId> objectIds = studentTagEntry.getJxbIds();
        //虚拟班
        List<N33_XuNiBanEntry> xuNiBanEntries = virtualClassDao.getN33_VirtualClassEntryTag(id);
        for (N33_XuNiBanEntry entry : xuNiBanEntries) {
            objectIds.addAll(entry.getJxbIds());
            List<N33_XuNiBanEntry.StudentTag> stuTag = new ArrayList<N33_XuNiBanEntry.StudentTag>();
            for (N33_XuNiBanEntry.StudentTag tag : entry.getTagList()) {
                if (!tag.getTagId().toString().equals(id.toString())) {
                    stuTag.add(tag);
                }
            }
            entry.setTagList(stuTag);
            //tag等于0改为等待删除数据
            if (stuTag.size() == 0) {
                entry.setType(1000);
            }
            virtualClassDao.updateN33_VirtualClassEntry(entry);
        }
        virtualClassDao.delN33_VirtualClassEntryTag();
        List<N33_JXBEntry> n33_jxbEntries = n33_jxbDao.getJXBListByIds(objectIds, studentTagEntry.getXqId());
        List<N33_StudentTagEntry.StudentInfoEntry> studentInfoEntries = studentTagEntry.getStudents();
        for (N33_JXBEntry jxbEntry : n33_jxbEntries) {
            List<ObjectId> stuIds = jxbEntry.getStudentIds();
            for (N33_StudentTagEntry.StudentInfoEntry infoEntry : studentInfoEntries) {
                if (stuIds.contains(infoEntry.getStuId())) {
                    stuIds.remove(infoEntry.getStuId());
                }
            }
            List<ObjectId> tagIds = jxbEntry.getTagIds();
            tagIds.remove(studentTagEntry.getID());
            jxbEntry.setStudentIds(stuIds);
            jxbEntry.setTagIds(tagIds);
            n33_jxbDao.updateN33_JXB(jxbEntry);
        }
        n33_studentTagDao.removeById(id);
    }

    public void updateName(ObjectId id, String name) {
        n33_studentTagDao.updateName(id, name);
    }

    public void updateJxb(ObjectId id, List<String> jxbids) {
        n33_studentTagDao.updateJxb(id, MongoUtils.convertToObjectIdList(jxbids));
    }

    public void updateStudent(ObjectId id, List<N33_StudentTagDTO.StudentInfo> studentInfos) {
        List<N33_StudentTagEntry.StudentInfoEntry> entryList = new ArrayList<N33_StudentTagEntry.StudentInfoEntry>();
        for (N33_StudentTagDTO.StudentInfo dto : studentInfos) {
            entryList.add(dto.buildEntry());
        }
        n33_studentTagDao.updateStudent(id, entryList);
    }

    /**
     * 查询年级学生标记列表
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getStudentList(Map map, ObjectId sid) {
        //学期id
        ObjectId xqid = new ObjectId((String) map.get("xqid"));
        //年级id
        ObjectId gradeId = new ObjectId((String) map.get("gradeId"));
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId, xqid);
        //学科是几科的组合
        Integer subjectType = Integer.parseInt((String) map.get("sbt"));
        //学生组合（包含人数）
        String sbujectName = (String) map.get("sbn");
        String subjectsName = sbujectName.substring(0, subjectType);
        //等级
        Integer lev = Integer.parseInt((String) map.get("lev"));

        //分段类型
        Integer fenDuan = Integer.parseInt((String) map.get("fenDuan"));

        //查询类型
        Integer type = (Integer) map.get("type");
        //是否非0
        Integer fei = (Integer) map.get("fei");

        Map autoOtherSet = autoTeaSetService.getAutoOtherSet(sid, gradeId, xqid);
        String periodCheck = autoOtherSet.get("periodCheck")==null?"":autoOtherSet.get("periodCheck").toString();

        List<ClassEntry> classEntries = null;
        if("1".equals(periodCheck)&&fenDuan!=-1) {
            List<N33_PartClassSetEntry> partClassSetEntries = partClassSetDao.getPartClassEntries(sid, gradeId, xqid, fenDuan);
            List<ObjectId> ptClsIds = MongoUtils.getFieldObjectIDs(partClassSetEntries, "classId");

            //年级下面的班级
            classEntries = classDao.findByGradeIdIdOrderByXH(sid, gradeId, xqid, ptClsIds);
        }else{
            fenDuan=-1;
            //年级下面的班级
            classEntries = classDao.findByGradeIdIdOrderByXH(sid, gradeId, xqid);
        }

        //查询学生是否被标记
        //List<N33_StudentTagEntry> studentTagEntries = n33_studentTagDao.getTagListByxqid(xqid, gradeId);
        List<N33_StudentTagEntry> studentTagEntries = n33_studentTagDao.getFenDuanTagList(xqid, gradeId, fenDuan);

        //对应年级某个组合学生
        Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidAndGradeIdMapAll(gradeId, xqid, lev);

        studentEntryMap = getStudentBySubjectName(studentEntryMap, subjectsName, subjectType);
        return getStuList(type, classEntries, studentEntryMap, subjectsName, lev, studentTagEntries, grade, fei);
    }

    public List<Map<String, Object>> getNotTagStudentBySubject(String subName, ObjectId gradeId, ObjectId xqid) {
        List<Map<String, Object>> studentDTOs = new ArrayList<Map<String, Object>>();
        //查询学生是否被标记
        List<N33_StudentTagEntry> studentTagEntries = n33_studentTagDao.getTagListByxqid(xqid, gradeId);
        //对应年级某个组合学生
        Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidAndGradeIdMapAll(gradeId, xqid, -1);
        studentEntryMap = getStudentBySubjectName(studentEntryMap, subName, 3);
        for (StudentEntry entry : studentEntryMap.values()) {
            Boolean stuTag = true;
            for (N33_StudentTagEntry tagEntry : studentTagEntries) {
                for (N33_StudentTagEntry.StudentInfoEntry infoEntry : tagEntry.getStudents()) {
                    if (infoEntry.getStuId().toString().equals(entry.getUserId().toString())) {
                        stuTag = false;
                    }
                }
            }
            if (stuTag) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", entry.getUserId().toString());
                map.put("lev", entry.getLevel() == 0 ? "无" : entry.getLevel().toString());
                map.put("name", entry.getUserName());
                map.put("sn", entry.getStudyNum());
                map.put("sex", entry.getSex() == 0 ? "男" : "女");
                map.put("clsName", entry.getClassName());
                map.put("com", entry.getCombiname());
                studentDTOs.add(map);
            }
        }
        return studentDTOs;
    }


    public List<Map<String, Object>> getStudentListByDingErZouYi(Map map, ObjectId sid) {
        //学期id
        ObjectId xqid = new ObjectId((String) map.get("xqid"));
        //年级id
        ObjectId gradeId = new ObjectId((String) map.get("gradeId"));
        //是否非0
        Integer fei = (Integer) map.get("fei");
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(gradeId, xqid);
        //学科是几科的组合
        Integer subjectType = Integer.parseInt((String) map.get("sbt"));
        //学生组合（包含人数）
        String sbujectName = (String) map.get("sbn");
        String subjectsName = sbujectName.substring(0, subjectType);
        //等级
        Integer lev = Integer.parseInt((String) map.get("lev"));
        //查询类型
        Integer type = (Integer) map.get("type");
        //年级下面的班级
        List<ClassEntry> classEntries = classDao.findByGradeIdIdOrderByXH(sid, gradeId, xqid);
        //查询学生是否被标记
        List<N33_StudentTagEntry> studentTagEntries = n33_studentTagDao.getTagListByxqid(xqid, gradeId);
        //对应年级某个组合学生
        Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidAndGradeIdMapAll(gradeId, xqid, lev);
        List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gradeId);
        Map<String, List<StudentEntry>> studentEntryMap1 = getStudentBySubjectNameZouYi(studentEntryMap, subjectsName, subjectType, ksEntries);
        return getStuListZou(type, classEntries, studentEntryMap1, subjectsName, lev, studentTagEntries, ksEntries, subjectType, grade, fei);
    }

    public Map<ObjectId, StudentEntry> getStudentBySubjectName(Map<ObjectId, StudentEntry> studentEntryMap, String subjectsName, Integer subjectType) {
        List<String> subNames = new ArrayList<String>();
        Integer index = 1;
        for (int count = 0; count < subjectType; count++) {
            subNames.add(subjectsName.substring(count, index));
            index++;
        }
        Map<ObjectId, StudentEntry> studentEntryMap1 = new HashMap<ObjectId, StudentEntry>();
        for (StudentEntry studentEntry : studentEntryMap.values()) {
            Integer zuHeCount = 0;
            for (String sub : subNames) {
                if (studentEntry.getCombiname().indexOf(sub) != -1) {
                    zuHeCount++;
                }
            }
            if (zuHeCount == subNames.size()) {
                studentEntryMap1.put(studentEntry.getUserId(), studentEntry);
            }
        }
        return studentEntryMap1;
    }

    public Map<String, List<StudentEntry>> getStudentBySubjectNameZouYi(Map<ObjectId, StudentEntry> studentEntryMap, String subjectsName, Integer subjectType, List<N33_KSEntry> ksEntries) {
        List<String> subNames = new ArrayList<String>();
        Integer index = 1;
        for (int count = 0; count < subjectType; count++) {
            subNames.add(subjectsName.substring(count, index));
            index++;
        }
        //其他的走班Name
        List<String> qiTaNames = new ArrayList<String>();
        for (N33_KSEntry ksEntry : ksEntries) {
            if (ksEntry.getIsZouBan() == 1) {
                String name = ksEntry.getSubjectName().substring(0, 1);
                if (!subNames.contains(name)) {
                    qiTaNames.add(name);
                }
            }
        }
        //组合新的数据
        List<List<String>> fuHeSubjectList = new ArrayList<List<String>>();
        for (String qiTaSubName : qiTaNames) {
            List<String> sub = new ArrayList<String>();
            for (String s : subNames) {
                sub.add(s);
            }
            sub.add(qiTaSubName);
            fuHeSubjectList.add(sub);
        }
        Map<String, List<StudentEntry>> studentEntryMap1 = new HashMap<String, List<StudentEntry>>();
        for (List<String> subject : fuHeSubjectList) {
            String subName = "";
            for (String sub : subject) {
                subName += sub;
            }
            List<StudentEntry> studentEntryList = new ArrayList<StudentEntry>();
            for (StudentEntry studentEntry : studentEntryMap.values()) {
                Integer zuHeCount = 0;
                for (String sub : subject) {
                    if (studentEntry.getCombiname().indexOf(sub) != -1) {
                        zuHeCount++;
                    }
                }
                if (zuHeCount == subject.size()) {
                    studentEntryList.add(studentEntry);
                }
            }
            studentEntryMap1.put(subName, studentEntryList);
        }
        return studentEntryMap1;
    }

    /**
     * 封装返回数据
     *
     * @param type
     * @param classEntries
     * @param studentEntryMap
     * @return
     */
    public List<Map<String, Object>> getStuList(Integer type, List<ClassEntry> classEntries, Map<ObjectId, StudentEntry> studentEntryMap, String subjectName, Integer lev, List<N33_StudentTagEntry> studentTagEntries, Grade grade, Integer fei) {
        //返回数据
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (ClassEntry entry : classEntries) {
            Integer biaoJiStu = 0;
            Integer leve = lev;
            Map<String, Object> map = new HashMap<String, Object>();
            if (grade != null) {
                map.put("grade", grade.getName());
            } else {
                map.put("grade", "");
            }
            map.put("xh", entry.getXh());
            map.put("name", entry.getName());
            map.put("classCount", entry.getStudentList().size());
            map.put("classId", entry.getClassId().toString());
            map.put("subjectName", subjectName);
            List<Map<String, Object>> studentList = new ArrayList<Map<String, Object>>();
            List<ObjectId> tagIds = new ArrayList<ObjectId>();
            List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
            for (ObjectId stuId : entry.getStudentList()) {
                StudentEntry studentEntry = studentEntryMap.get(stuId);
                if (studentEntry != null) {
                    Integer tag = 0;
                    leve = studentEntry.getLevel();
                    Map<String, Object> student = new HashMap<String, Object>();
                    student.put("name", studentEntry.getUserName());
                    student.put("id", studentEntry.getUserId().toString());
                    for (N33_StudentTagEntry tagEntry : studentTagEntries) {
                        for (N33_StudentTagEntry.StudentInfoEntry infos : tagEntry.getStudents()) {
                            if (infos.getStuId().toString().equals(studentEntry.getUserId().toString())) {
                                tag = 1;
                                if (type != 1) {
                                    if (!tagIds.contains(tagEntry.getID())) {
                                        tagIds.add(tagEntry.getID());
                                        Map<String, Object> tagPut = new HashMap<String, Object>();
                                        tagPut.put("name", tagEntry.getName() + "(" + tagEntry.getStudents().size() + ")");
                                        tagPut.put("id", tagEntry.getID().toString());
                                        tagList.add(tagPut);
                                    }
                                }
                            }
                        }
                    }
                    if (tag == 1) {
                        biaoJiStu++;
                    }
                    student.put("bj", tag);
                    if (type == 0) {
                        studentList.add(student);
                    }
                    if (type == 1 && tag == 0) {
                        studentList.add(student);
                    }
                    if (type == 2 && tag == 1) {
                        studentList.add(student);
                    }
                }
            }
            if (lev != -1) {
                map.put("lev", leve);
            } else {
                map.put("lev", "全部");
            }
            map.put("student", studentList);
            map.put("tagList", tagList);
            map.put("subjectStuCount", studentList.size());
            map.put("subjectStuBiaoCount", biaoJiStu);
            map.put("subjectWeiBiaoCount", studentList.size() - biaoJiStu);
            if (fei == 0) {
                if (studentList.size() > 0) {
                    result.add(map);
                }
            } else {
                result.add(map);
            }
        }
        return result;
    }


    public List<Map<String, Object>> getStuListZou(Integer type, List<ClassEntry> classEntries, Map<String, List<StudentEntry>> studentEntryMap, String subjectName, Integer lev, List<N33_StudentTagEntry> studentTagEntries, List<N33_KSEntry> ksEntries, Integer subjectType, Grade grade, Integer fei) {
        //返回数据
        List<String> subNames = new ArrayList<String>();
        Integer index = 1;
        for (int count = 0; count < subjectType; count++) {
            subNames.add(subjectName.substring(count, index));
            index++;
        }
        //其他的走班Name
        List<String> qiTaNames = new ArrayList<String>();
        for (N33_KSEntry ksEntry : ksEntries) {
            if (ksEntry.getIsZouBan() == 1) {
                String name = ksEntry.getSubjectName().substring(0, 1);
                if (!subNames.contains(name)) {
                    qiTaNames.add(name);
                }
            }
        }
        //组合新的数据
        List<List<String>> fuHeSubjectList = new ArrayList<List<String>>();
        for (String qiTaSubName : qiTaNames) {
            List<String> sub = new ArrayList<String>();
            for (String s : subNames) {
                sub.add(s);
            }
            sub.add(qiTaSubName);
            fuHeSubjectList.add(sub);
        }
        //数据封装
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (ClassEntry entry : classEntries) {
            Integer leve = lev;
            Map<String, Object> map = new HashMap<String, Object>();
            if (grade != null) {
                map.put("grade", grade.getName());
            } else {
                map.put("grade", "");
            }
            map.put("xh", entry.getXh());
            map.put("name", entry.getName());
            map.put("classCount", entry.getStudentList().size());
            map.put("classId", entry.getClassId().toString());
            List<Map<String, Object>> subjectNameList = new ArrayList<Map<String, Object>>();
            for (List<String> subList : fuHeSubjectList) {
                List<ObjectId> tagIds = new ArrayList<ObjectId>();
                List<Map<String, Object>> studentList = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
                Map<String, Object> map1 = new HashMap<String, Object>();
                String name = "";
                for (String subName : subList) {
                    name += subName;
                }
                map1.put("subName", name);
                //组合学生
                List<StudentEntry> studentEntryList = studentEntryMap.get(name);
                //班级学生
                List<StudentEntry> clsStudentList = new ArrayList<StudentEntry>();
                for (StudentEntry studentEntry : studentEntryList) {
                    if (studentEntry.getClassId().toString().equals(entry.getClassId().toString())) {
                        clsStudentList.add(studentEntry);
                    }
                }
                Integer biaoJiStu = 0;
                for (StudentEntry studentEntry : clsStudentList) {
                    if (studentEntry != null) {
                        Integer tag = 0;
                        leve = studentEntry.getLevel();
                        Map<String, Object> student = new HashMap<String, Object>();
                        student.put("name", studentEntry.getUserName());
                        student.put("id", studentEntry.getUserId().toString());
                        for (N33_StudentTagEntry tagEntry : studentTagEntries) {
                            for (N33_StudentTagEntry.StudentInfoEntry infos : tagEntry.getStudents()) {
                                if (infos.getStuId().toString().equals(studentEntry.getUserId().toString())) {
                                    tag = 1;
                                    if (type != 1) {
                                        if (!tagIds.contains(tagEntry.getID())) {
                                            tagIds.add(tagEntry.getID());
                                            Map<String, Object> tagPut = new HashMap<String, Object>();
                                            tagPut.put("name", tagEntry.getName() + "(" + tagEntry.getStudents().size() + ")");
                                            tagPut.put("id", tagEntry.getID().toString());
                                            tagList.add(tagPut);
                                        }
                                    }
                                }
                            }
                        }
                        if (tag == 1) {
                            biaoJiStu++;
                        }
                        student.put("bj", tag);
                        if (type == 0) {
                            studentList.add(student);
                        }
                        if (type == 1 && tag == 0) {
                            studentList.add(student);
                        }
                        if (type == 2 && tag == 1) {
                            studentList.add(student);
                        }
                    }
                }
                if (lev != -1) {
                    map1.put("lev", leve);
                } else {
                    map1.put("lev", "全部");
                }
                map1.put("student", studentList);
                map1.put("tagList", tagList);
                map1.put("subjectStuCount", studentList.size());
                map1.put("subjectStuBiaoCount", biaoJiStu);
                map1.put("subjectWeiBiaoCount", studentList.size() - biaoJiStu);
                if (fei == 0) {
                    if (studentList.size() > 0) {
                        subjectNameList.add(map1);
                    }
                } else {
                    subjectNameList.add(map1);
                }
            }
            map.put("subjectNameList", subjectNameList);
            map.put("size", subjectNameList.size());
            if (subjectNameList.size() > 0) {
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 查询教学班
     *
     * @param map
     */
    public List<Map<String, Object>> getJiaoXueBanList(Map map, ObjectId sid) {
        //学期id
        ObjectId xqid = new ObjectId((String) map.get("xqid"));
        //年级id
        ObjectId gradeId = new ObjectId((String) map.get("gid"));
        //学科list
        List<ObjectId> subIds = new ArrayList<ObjectId>();
        List<String> sub = (List<String>) map.get("subid");
        if (sub.size() > 0) {
            subIds = MongoUtils.convertToObjectIdList(sub);
        }
        //教学班类型
        Integer subType = Integer.parseInt((String) map.get("subType"));
        //教学班名字
        String jxbName = (String) map.get("jxbName");
        //所有教学班

        //jxb add
        ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntries = n33_jxbDao.getJXBList(sid, gradeId, subIds, xqid, subType, jxbName,acClassType);
        //标签list
        List<ObjectId> tags = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            if (jxbEntry.getTagIds() != null) {
                tags.addAll(jxbEntry.getTagIds());
            }
        }
        Map<ObjectId, N33_StudentTagEntry> studentTagEntries = n33_studentTagDao.getTagListByIdsMap(tags);
        return getDtoListByJiaoXueBan(jxbEntries, studentTagEntries);
    }

    public List<Map<String, Object>> getDtoListByJiaoXueBan(List<N33_JXBEntry> entries, Map<ObjectId, N33_StudentTagEntry> studentTagEntryMap) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (N33_JXBEntry entry : entries) {
            Map<String, Object> map = new HashMap<String, Object>();
            List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
            for (ObjectId tagid : entry.getTagIds()) {
                N33_StudentTagEntry tagEntry = studentTagEntryMap.get(tagid);
                if (tagEntry != null) {
                    Map<String, Object> tagPut = new HashMap<String, Object>();
                    tagPut.put("name", tagEntry.getName());
                    tagPut.put("id", tagEntry.getID().toString());
                    tagPut.put("count", tagEntry.getStudents().size());
                    tagList.add(tagPut);
                }
            }
            map.put("tagList", tagList);
            if (StringUtils.isNotEmpty(entry.getNickName())) {
                map.put("name", entry.getNickName());
            } else {
                map.put("name", entry.getName());
            }
            map.put("id", entry.getID().toString());
            map.put("stuCount", entry.getStudentIds().size());
            String name = "";
            if (entry.getType() == 1) {
                name = "等级型";
            }
            if (entry.getType() == 2) {
                name = "合格型";
            }
            if (entry.getType() == 3) {
                name = "行政型";
            }
            if (entry.getType() == 4) {
                name = "专项型";
            }
            map.put("typeName", name);
            result.add(map);
        }
        return result;
    }


    public List<Map<String, Object>> getStudentViewTagByGradeId(ObjectId xqid, ObjectId gradeId) {
        //标签
        List<N33_StudentTagEntry> studentTagEntry = n33_studentTagDao.getStudentViewTagByGradeId(xqid, gradeId);
        List<ObjectId> studentTagIds = MongoUtils.getFieldObjectIDs(studentTagEntry, "_id");
        //关联的教学班
        List<N33_JXBEntry> tagList = n33_jxbDao.getJxbListByTagIds(xqid, studentTagIds);
        return getStudentViewDto(studentTagEntry, tagList);
    }

    
    public void TongBuStuByClass(ObjectId xqid, ObjectId gradeId,ObjectId sid) {
        //全部年级的班级
        List<ClassEntry> gradeClass = classDao.findByGradeIdId(sid, gradeId, xqid);
        //全部课时
        List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gradeId);
        //学生列表
        List<StudentEntry> studentList = studentDao.getStudentByXqidAndGradeId(gradeId, xqid);
        Grade grade1 = gradeDao.findIsolateGradeByGradeId(xqid, sid, gradeId);

        for (ClassEntry classEntry : gradeClass) {
            //学生列表
            List<ObjectId> studentIds = new ArrayList<ObjectId>();
            for (StudentEntry student : studentList) {
                if (student.getClassId().toString().equals(classEntry.getClassId().toString())) {
                    studentIds.add(student.getUserId());
                }
            }
            for (N33_KSEntry ksEntry : ksEntries) {
                //行政班
                if (ksEntry.getIsZouBan() == 0 && ksEntry.getDan() == 0) {
                    String name = ksEntry.getSubjectName() + grade1.getName() + "(" + classEntry.getXh() + ")";
                    // jxb add 06-14
                    ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
                    int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
                    // jxb add 06-14
                    N33_JXBEntry jxbEntry1 = jxbDao.getJXBEntry(name, xqid, acClassType);
                    if(jxbEntry1 != null){
                        jxbEntry1.setStudentIds(studentIds);
                        jxbDao.updateN33_JXB(jxbEntry1);
                    }
                }
            }
        }
    }

    public List<Map<String, Object>> getTagList(ObjectId xqid, ObjectId gradeId) {
        //标签
        List<N33_StudentTagEntry> studentTagEntry = n33_studentTagDao.getTagList(xqid, gradeId);
        List<ObjectId> studentTagIds = MongoUtils.getFieldObjectIDs(studentTagEntry, "_id");
        //关联的教学班
        List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJxbListByTagIds(xqid, studentTagIds);
        return getStudentViewDto(studentTagEntry, jxbEntryList);
    }

    public List<Map<String, Object>> getTagListKS(ObjectId xqid, ObjectId gradeId, ObjectId sid) {
        //标签
        List<N33_StudentTagEntry> studentTagEntry = n33_studentTagDao.getTagList(xqid, gradeId);
        List<ObjectId> studentTagIds = MongoUtils.getFieldObjectIDs(studentTagEntry, "_id");
        //关联的教学班
        List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJxbListByTagIds(xqid, studentTagIds);
        return getStudentViewDto(studentTagEntry, jxbEntryList, xqid, sid);
    }

    public List<Map<String, Object>> getStuSign(ObjectId id) {
        N33_StudentTagEntry studentTagEntry = n33_studentTagDao.getTagById(id);
        if (studentTagEntry == null) {
            return new ArrayList<Map<String, Object>>();
        }
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        for (N33_StudentTagEntry.StudentInfoEntry infoEntry : studentTagEntry.getStudents()) {
            stuIds.add(infoEntry.getStuId());
        }
        Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidMap(studentTagEntry.getXqId(), stuIds);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ObjectId stu : stuIds) {
            StudentEntry studentEntry = studentEntryMap.get(stu);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tgid", id.toString());
            map.put("id", studentEntry.getUserId().toString());
            map.put("name", studentEntry.getUserName());
            map.put("lev", studentEntry.getLevel() == 0 ? "无" : studentEntry.getLevel().toString());
            map.put("sn", studentEntry.getStudyNum());
            map.put("sex", studentEntry.getSex() == 0 ? "男" : "女");
            map.put("clsName", studentEntry.getClassName());
            map.put("com", studentEntry.getCombiname());
            list.add(map);
        }
        return list;
    }

    /**
     * 组装显示的标签的DTO
     *
     * @param studentTagEntry
     * @param jxbEntriesList
     * @return
     */
    public List<Map<String, Object>> getStudentViewDto(List<N33_StudentTagEntry> studentTagEntry, List<N33_JXBEntry> jxbEntriesList, ObjectId xqid, ObjectId sid) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<ObjectId, N33_JXBKSEntry> jxbKSMap = jxbksDao.getJXBKSsBySubjectId(MongoUtils.getFieldObjectIDs(jxbEntriesList, "_id"), xqid);
        //全校课时
        List<N33_KSEntry> subjectTime = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid);
        for (N33_StudentTagEntry entry : studentTagEntry) {
            Map<String, Object> res = new HashMap<String, Object>();
            res.put("id", entry.getID().toString());
            res.put("name", entry.getName());
            res.put("view", entry.getView());
            res.put("stuCount", entry.getStudents().size());
            //行政班
            String classListStr = "";
            List<ObjectId> clsList = new ArrayList<ObjectId>();
            for (N33_StudentTagEntry.StudentInfoEntry entry1 : entry.getStudents()) {
                //行政班
                if (!clsList.contains(entry1.getClassId())) {
                    clsList.add(entry1.getClassId());
                    classListStr += entry1.getClassName() + ",";
                }
            }
            res.put("classListStr", classListStr);
            //教学班
            String jiaoXueBanStr = "";
            List<ObjectId> jxbList = new ArrayList<ObjectId>();

            //课时
            Integer ksCount = 0;
            //已排课时
            Integer pksCount = 0;

            for (ObjectId ids : entry.getJxbIds()) {
                for (N33_JXBEntry entry1 : jxbEntriesList) {
                    if (entry1.getID().toString().equals(ids.toString())) {
                        //教学班
                        if (!jxbList.contains(entry1.getID())) {
                            N33_JXBKSEntry KsEntry = jxbKSMap.get(entry1.getID());
                            pksCount += KsEntry != null ? KsEntry.getYpCount() : 0;
                            jxbList.add(entry1.getID());
                            if (StringUtils.isNotEmpty(entry1.getNickName())) {
                                jiaoXueBanStr += entry1.getNickName() + ",";
                            } else {
                                jiaoXueBanStr += entry1.getName() + ",";
                            }
                        }
                    }
                }
            }

            if (jxbList.size() > 0) {
                List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJXBListByIds(jxbList, xqid);
                ksCount = getTeaKeShi(jxbEntryList, subjectTime);
            }
            res.put("allks", ksCount);
            res.put("ypks", pksCount);
            res.put("jiaoXueBanStr", jiaoXueBanStr);
            result.add(res);
        }
        return result;
    }

    /**
     * 统计某个标签的教学班总课时数
     *
     * @param jxbdtos
     * @param SubjectTime
     * @return
     */
    public Integer getTeaKeShi(List<N33_JXBEntry> jxbdtos, List<N33_KSEntry> SubjectTime) {
        Integer count = 0;
        for (N33_JXBEntry jxbdto : jxbdtos) {
           count+=jxbdto.getJXBKS();
        }
        return count;
    }

    /**
     * 组装显示的标签的DTO
     *
     * @param studentTagEntry
     * @param jxbEntriesList
     * @return
     */
    public List<Map<String, Object>> getStudentViewDto(List<N33_StudentTagEntry> studentTagEntry, List<N33_JXBEntry> jxbEntriesList) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (N33_StudentTagEntry entry : studentTagEntry) {
            Map<String, Object> res = new HashMap<String, Object>();
            res.put("id", entry.getID().toString());
            res.put("name", entry.getName());
            res.put("view", entry.getView());
            res.put("stuCount", entry.getStudents().size());
            //行政班
            String classListStr = "";
            List<ObjectId> clsList = new ArrayList<ObjectId>();
            for (N33_StudentTagEntry.StudentInfoEntry entry1 : entry.getStudents()) {
                //行政班
                if (!clsList.contains(entry1.getClassId())) {
                    clsList.add(entry1.getClassId());
                    classListStr += entry1.getClassName() + ",";
                }
            }
            res.put("classListStr", classListStr);
            //教学班
            String jiaoXueBanStr = "";
            List<ObjectId> jxbList = new ArrayList<ObjectId>();

            for (ObjectId ids : entry.getJxbIds()) {
                for (N33_JXBEntry entry1 : jxbEntriesList) {
                    if (entry1.getID().toString().equals(ids.toString())) {
                        //教学班
                        if (!jxbList.contains(entry1.getID())) {
                            jxbList.add(entry1.getID());
                            if (StringUtils.isNotEmpty(entry1.getNickName())) {
                                jiaoXueBanStr += entry1.getNickName() + ",";
                            } else {
                                jiaoXueBanStr += entry1.getName() + ",";
                            }
                        }
                    }
                }
            }
            res.put("jiaoXueBanStr", jiaoXueBanStr);
            result.add(res);
        }
        return result;
    }

    /**
     * 新增标签学生到某个教学班
     *
     * @param jxbId
     * @param tagid
     * @return
     */
    public String addJxbStudentByTagId(ObjectId jxbId, ObjectId tagid, ObjectId cid) {
        //教学班列表
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        //查询标签学生
        N33_StudentTagEntry tagEntry = n33_studentTagDao.getTagById(tagid);
        //学生列表
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        for (N33_StudentTagEntry.StudentInfoEntry studentInfoEntry : tagEntry.getStudents()) {
            studentIds.add(studentInfoEntry.getStuId());
        }
//        if (getStudentSubjectIsJiaoXueBan(jxbEntry.getSubjectId(), jxbEntry.getTermId(), studentIds)) {
//            return "对应的标签中存在和教学班学科不符合的学生，新增失败!";
//        }
        //判断标签中的学生是否存在教学班中  true存在  false不存在
        if (getStuExistJxb(jxbId, studentIds, cid)) {
            return "该教学班已经存在对应标签中的学生，新增失败!";
        }
        //判断是否是相同的学科
        if (getStuExistOtherJxb(jxbEntry.getSubjectId(), studentIds, jxbId, cid)) {
            return "对应标签中的学生已经存在于某个同学科的教学班中，新增失败!";
        }
        //教学班学生
        List<ObjectId> jxbStudentList = jxbEntry.getStudentIds();
        jxbStudentList.addAll(studentIds);
        jxbEntry.setStudentIds(jxbStudentList);
        //教学班标签
        List<ObjectId> tagList = jxbEntry.getTagIds();
        tagList.add(tagid);
        jxbEntry.setTagIds(tagList);
        //标签教学班
        List<ObjectId> jxbIds = tagEntry.getJxbIds();
        jxbIds.add(jxbId);
        tagEntry.setJxbIds(jxbIds);
        n33_jxbDao.updateN33_JXB(jxbEntry);
        n33_studentTagDao.addStudentTag(tagEntry);
        //重置冲突关系
        conflictDetection(jxbEntry.getTermId(), jxbEntry.getSchoolId(), jxbEntry, cid);
        return "添加成功";
    }

    /**
     * 人员检测
     *
     * @param jxbId
     * @param objectIds
     * @return
     */
    public Boolean getStuExistJxb(ObjectId jxbId, List<ObjectId> objectIds, ObjectId cid) {
        return n33_jxbDao.getJXBEntry(jxbId, objectIds, cid) != null;
    }


    /**
     * 选课检测
     *
     * @param objectIds
     * @return
     */
    /*public Boolean getStudentSubjectIsJiaoXueBan(ObjectId subjectId, ObjectId xqid, List<ObjectId> objectIds) {
        List<StudentEntry> studentEntryList = studentDao.getStudentByXqid(xqid, objectIds);
        for (StudentEntry entry : studentEntryList) {
            Boolean bf1 = entry.getSubjectId1().toString().equals(subjectId.toString());
            Boolean bf2 = entry.getSubjectId2().toString().equals(subjectId.toString());
            Boolean bf3 = entry.getSubjectId3().toString().equals(subjectId.toString());
            if (!bf1 && !bf2 && !bf3) {
                return true;
            }
        }
        return false;
    }*/

    /**
     * 学科检测
     *
     * @param subjectId
     * @param objectIds
     * @param jxbId
     * @return
     */
    public Boolean getStuExistOtherJxb(ObjectId subjectId, List<ObjectId> objectIds, ObjectId jxbId, ObjectId cid) {
    	// jxb add 
    	N33_JXBEntry entry = jxbDao.getJXBById(jxbId);
        ObjectId ciId = getDefaultPaiKeTerm(entry.getSchoolId()).getPaikeci();
        int acClassType = turnOffService.getAcClassType(entry.getSchoolId(), entry.getGradeId(), ciId,1);
        // jxb add
    	return n33_jxbDao.getJXBEntry(jxbId, objectIds, subjectId, cid, acClassType) != null;
    }

    /**
     * 增加冲突检测
     *
     * @param xqid
     * @param sid
     */
    public void conflictDetection(ObjectId xqid, ObjectId sid, N33_JXBEntry jxbEntry, ObjectId cid) {
        //查询全年级教学班
    	//jxb add
    	int acClassType = turnOffService.getAcClassType(sid, jxbEntry.getGradeId(), cid,1);
    	//jxb add	
        List<N33_JXBEntry> jxbEntries = n33_jxbDao.getJXBList(jxbEntry.getSchoolId(), jxbEntry.getGradeId(), cid,acClassType);
        List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(jxbEntries, "_id");
        //删除对应教学班的学生冲突
        jxbctDao.removeByJxbId(sid, 1, cid);
        jxbctDao.removeByJxbIdo(sid, 1, cid);
        //获得对应教学班的冲突
        List<N33_JXBCTEntry> jxbctEntries = new ArrayList<N33_JXBCTEntry>();
        for (N33_JXBEntry nianJi : jxbEntries) {
            for (N33_JXBEntry nianJi1 : jxbEntries) {
                if (!nianJi.getID().toString().equals(nianJi1.getID().toString())) {
                    //教学班学生
                    List<ObjectId> jxbStudent = nianJi.getStudentIds();
                    //对应的学生
                    List<ObjectId> student = nianJi1.getStudentIds();
                    //冲突的学生
                    jxbStudent.retainAll(student);
                    //存在冲突的人
                    if (jxbStudent.size() > 0) {
                        N33_JXBCTEntry jxbctEntry = new N33_JXBCTEntry(cid, sid, nianJi.getID(), nianJi1.getID(), 1, jxbStudent.size(), jxbStudent);
                        jxbctEntries.add(jxbctEntry);
                    }
                }
            }
        }
        if (jxbctEntries.size() > 0) {
            jxbctDao.addN33_JXBCTEntrys(jxbctEntries);
        }
    }

    public void conflictDetection(ObjectId sid, ObjectId gradeId, ObjectId cid) {
        //查询全年级教学班
    	//jxb add
    	int acClassType = turnOffService.getAcClassType(sid,gradeId, cid,1);
    	//jxb add
        List<N33_JXBEntry> jxbEntries = n33_jxbDao.getJXBList(sid, gradeId, cid, acClassType);
        List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(jxbEntries, "_id");
        Map<ObjectId, N33_JXBEntry> jxbMap = new HashMap<ObjectId, N33_JXBEntry>();
        for (N33_JXBEntry jxb : jxbEntries) {
            jxbMap.put(jxb.getID(), jxb);
        }
        //删除对应教学班的学生冲突
        jxbctDao.removeByJxbId(sid, jxbIds, 1, cid);
        jxbctDao.removeByJxbIdo(sid, jxbIds, 1, cid);
        //获得对应教学班的冲突
        List<N33_JXBCTEntry> jxbctEntries = new ArrayList<N33_JXBCTEntry>();
        for (N33_JXBEntry jxb1 : jxbEntries) {
            jxbIds.remove(jxb1.getID());
            for(ObjectId jxbId:jxbIds){
                if(jxbId.equals(jxb1.getID())){
                    continue;
                }
                N33_JXBEntry jxb2 = jxbMap.get(jxbId);

                //教学班学生
                List<ObjectId> jxbStudent = jxb1.getStudentIds();
                //对应的学生
                List<ObjectId> student = jxb2.getStudentIds();
                //冲突的学生
                List<ObjectId> tempStudents = new ArrayList<ObjectId>(jxbStudent);
                tempStudents.retainAll(student);
                //存在冲突的人
                if (tempStudents.size() > 0) {
                    if (tempStudents.size() == jxbStudent.size() && jxbStudent.size() == student.size()) {
                        N33_JXBCTEntry jxbct1 = new N33_JXBCTEntry(cid, sid, jxb1.getID(), jxb2.getID(), 1, tempStudents.size(), tempStudents, 1);
                        jxbctEntries.add(jxbct1);
                        N33_JXBCTEntry jxbct2 = new N33_JXBCTEntry(cid, sid, jxb2.getID(), jxb1.getID(), 1, tempStudents.size(), tempStudents, 1);
                        jxbctEntries.add(jxbct2);
                    } else {
                        N33_JXBCTEntry jxbct1 = new N33_JXBCTEntry(cid, sid, jxb1.getID(), jxb2.getID(), 1, tempStudents.size(), tempStudents);
                        jxbctEntries.add(jxbct1);
                        N33_JXBCTEntry jxbct2 = new N33_JXBCTEntry(cid, sid, jxb2.getID(), jxb1.getID(), 1, tempStudents.size(), tempStudents);
                        jxbctEntries.add(jxbct2);
                    }

                }
            }
        }
        if (jxbctEntries.size() > 0) {
            jxbctDao.addN33_JXBCTEntrys(jxbctEntries);
        }
    }

    /**
     * 不是教学班里面的班级学生
     *
     * @param xqid
     * @param classId
     * @param jxbid
     * @return
     */
    public List<StudentDTO> getStudentListByNoAdd(ObjectId xqid, ObjectId classId, ObjectId jxbid) {
        //教学班列表
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbid);
        List<ObjectId> studentIds = jxbEntry.getStudentIds();
        List<StudentEntry> studentEntryList = studentDao.getStudentByXqidAndClassId(classId, xqid, studentIds);
        List<StudentDTO> dtos = new ArrayList<StudentDTO>();
        for (StudentEntry entry : studentEntryList) {
            dtos.add(new StudentDTO(entry));
        }
        return dtos;
    }

    /**
     * 不是tag的班级学生
     *
     * @param xqid
     * @param classId
     * @param jxbid
     * @return
     */
    public List<StudentDTO> getStudentListByIsNotTag(ObjectId xqid, ObjectId classId, ObjectId jxbid) {
        //教学班列表
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbid);
        //标签列表
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        if (jxbEntry.getTagIds() != null) {
            List<N33_StudentTagEntry> tagEntries = n33_studentTagDao.getTagListByIds(jxbEntry.getTagIds());
            for (N33_StudentTagEntry tagEntry : tagEntries) {
                for (N33_StudentTagEntry.StudentInfoEntry studentInfoEntry : tagEntry.getStudents()) {
                    studentIds.add(studentInfoEntry.getStuId());
                }
            }
        }
        //不是 标记的学生
        List<ObjectId> users = new ArrayList<ObjectId>();
        for (ObjectId userIds : jxbEntry.getStudentIds()) {
            if (!studentIds.contains(userIds)) {
                users.add(userIds);
            }
        }
        //多的学生
        List<StudentEntry> studentEntryList = studentDao.getStudentByXqid(classId, xqid, users);
        List<StudentDTO> dtos = new ArrayList<StudentDTO>();
        for (StudentEntry entry : studentEntryList) {
            dtos.add(new StudentDTO(entry));
        }
        return dtos;
    }

    /**
     * 新增学生
     *
     * @param jxbId
     * @param uid
     * @return
     */
    public String addStu(ObjectId jxbId, ObjectId uid, ObjectId cid) {
        //教学班列表
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        //学生列表
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        studentIds.add(uid);
//        //判断标签中的学生是否存在教学班中  true不存在  false存在
//        if (getStudentSubjectIsJiaoXueBan(jxbEntry.getSubjectId(), jxbEntry.getTermId(), studentIds)) {
//            return "对应的学生选课和教学班学科不符合，新增失败!";
//        }
        //判断标签中的学生是否存在教学班中  true存在  false不存在
        if (getStuExistJxb(jxbId, studentIds, cid)) {
            return "该教学班已经存在对应的学生，新增失败!";
        }
        //判断是否是相同的学科
        if (getStuExistOtherJxb(jxbEntry.getSubjectId(), studentIds, jxbId, cid)) {
            return "对应的学生已经存在于某个同学科的教学班中，新增失败!";
        }
        //教学班学生
        List<ObjectId> jxbStudentList = jxbEntry.getStudentIds();
        jxbStudentList.add(uid);
        jxbEntry.setStudentIds(jxbStudentList);
        //重置冲突关系
        n33_jxbDao.updateN33_JXB(jxbEntry);
        conflictDetection(jxbEntry.getTermId(), jxbEntry.getSchoolId(), jxbEntry, cid);
        return "添加成功";
    }

    /**
     * 删除某个学生
     *
     * @param jxbId
     * @param uid
     * @return
     */
    public String delStu(ObjectId jxbId, ObjectId uid, ObjectId cid) {
        //教学班列表
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        //教学班学生
        List<ObjectId> jxbStudentList = new ArrayList<ObjectId>();
        for (ObjectId id : jxbEntry.getStudentIds()) {
            if (!id.toString().equals(uid.toString())) {
                jxbStudentList.add(id);
            }
        }
        jxbEntry.setStudentIds(jxbStudentList);
        //重置冲突关系.
        n33_jxbDao.updateN33_JXB(jxbEntry);
        conflictDetection(jxbEntry.getTermId(), jxbEntry.getSchoolId(), jxbEntry, cid);
        return "删除成功";
    }

    /**
     * 根据tag删除学生
     *
     * @param jxbId
     * @param tagid
     * @return
     */
    public String removeTagByBan(ObjectId jxbId, ObjectId tagid, ObjectId cid) {
        //教学班列表
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        //查询标签学生
        N33_StudentTagEntry tagEntry = n33_studentTagDao.getTagById(tagid);
        //学生列表
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        for (N33_StudentTagEntry.StudentInfoEntry studentInfoEntry : tagEntry.getStudents()) {
            studentIds.add(studentInfoEntry.getStuId());
        }
        //教学班学生
        List<ObjectId> jxbStudentList = jxbEntry.getStudentIds();
        jxbStudentList.removeAll(studentIds);
        jxbEntry.setStudentIds(jxbStudentList);
        //教学班标签
        List<ObjectId> tagList = jxbEntry.getTagIds();
        tagList.remove(tagid);
        jxbEntry.setTagIds(tagList);
        //标签教学班
        List<ObjectId> jxbIds = tagEntry.getJxbIds();
        jxbIds.remove(jxbId);
        tagEntry.setJxbIds(jxbIds);
        //重置冲突关系
        n33_jxbDao.updateN33_JXB(jxbEntry);
        n33_studentTagDao.addStudentTag(tagEntry);
        conflictDetection(jxbEntry.getTermId(), jxbEntry.getSchoolId(), jxbEntry, cid);

        return "删除成功";
    }

    public void updateTagView(Map map) {
        List<ObjectId> tagIdsList = new ArrayList<ObjectId>();
        List<String> ids = (List<String>) map.get("ids");
        if (ids.size() > 0) {
            tagIdsList = MongoUtils.convertToObjectIdList(ids);
        }
        n33_studentTagDao.updateView(tagIdsList, 0);
        n33_studentTagDao.updateNoView(tagIdsList, 1);
    }

    public void updateView(String id) {
        List<ObjectId> tagIdsList = new ArrayList<ObjectId>();
        tagIdsList.add(new ObjectId(id));
        n33_studentTagDao.updateView(tagIdsList, 1);
    }

    public void updateView(ObjectId xqid, ObjectId gradeId) {
        n33_studentTagDao.updateView(xqid, gradeId, 0);
    }

    public void updateJxb(ObjectId studentId, ObjectId jxbId) {
        N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(jxbId);
        List<ObjectId> StuIds = jxbEntry.getStudentIds();
        StuIds.remove(studentId);
        jxbEntry.setStudentIds(StuIds);
        n33_jxbDao.updateN33_JXB(jxbEntry);
    }

    public void detactConfliction(ObjectId schoolId, ObjectId gradeId, ObjectId cid) {
        conflictDetection(schoolId, gradeId, cid);
        infoSetService.detactConfliction(schoolId, cid);
    }

//    public static void main(String[] args){
//        N33_JXBDao jxbDao = new N33_JXBDao();
//        ObjectId sid = new ObjectId("5b9732ef48c5025c78ba1406");
//        ObjectId termId = new ObjectId("5b97516246d697466adcca6b");
//        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBsBySchoolId(termId,sid);
//        int cpunt = 0;
//        for (N33_JXBEntry jxbEntry1 : jxbEntryList) {
//            List<ObjectId> stuIds = new ArrayList<ObjectId>();
//            jxbEntry1.setStudentIds(stuIds);
//            jxbDao.updateN33_JXB(jxbEntry1);
//            cpunt ++;
//            System.out.println(cpunt);
//        }
//    }
}
