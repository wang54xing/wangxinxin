package com.fulaan.new33.service.isolate;

import com.db.new33.N33_StudentTagDao;
import com.db.new33.N33_VirtualClassDao;
import com.db.new33.isolate.*;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.service.N33_AutoFenBanService;
import com.fulaan.new33.service.N33_StudentTagService;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.N33_XuNiBanEntry;
import com.pojo.new33.isolate.*;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.sys.utils.CombineUtils;
import org.bson.types.ObjectId;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HEAD;
import java.math.BigDecimal;
import java.util.*;

@Service
public class N33_VirtualClassService extends BaseService {

    public static final Map<String, Integer> subject_sort = new HashMap<String, Integer>();
    private N33_VirtualClassDao virtualClassDao = new N33_VirtualClassDao();
    private N33_StudentTagDao studentTagDao = new N33_StudentTagDao();
    private N33_StudentDao studentDao = new N33_StudentDao();
    private N33_TeaDao teaDao = new N33_TeaDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private SubjectDao subjectDao = new SubjectDao();
    private ClassDao classDao = new ClassDao();
    private GradeDao gradeDao = new GradeDao();
    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();
    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();
    @Autowired
    private N33_TurnOffService turnOffService;
    private IsolateSubjectService subjectService = new IsolateSubjectService();
    @Autowired
    private N33_StudentTagService tagService;
    @Autowired
    private N33_AutoFenBanService autoFenBanService;

    public Map<String, Object> getStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> status = (Map<String, Object>) session.getAttribute("autoSetJXB");
        if (status != null && status.get("st") != null && (Integer) status.get("st") == -1) {
            session.removeAttribute("autoSetJXB");
        }
        return status;
    }

    /**
     * 初始化虚拟班
     */
    public void initVirtualClass(ObjectId gradeId, ObjectId xqid, ObjectId sid) {
        //查询没被创建虚拟班的学生标签
        List<N33_StudentTagEntry> tagEntries = studentTagDao.getTagListByxqid(xqid, gradeId, 1);
        //标签中学生
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        for (N33_StudentTagEntry tagEntry : tagEntries) {
            for (N33_StudentTagEntry.StudentInfoEntry student : tagEntry.getStudents()) {
                if (!studentIds.contains(student.getStuId())) {
                    studentIds.add(student.getStuId());
                }
            }
        }
        Map<ObjectId, StudentEntry> entryMap = studentDao.getStuMap(studentIds, xqid);
        List<N33_XuNiBanEntry> xiNiEntry = new ArrayList<N33_XuNiBanEntry>();
        //对应年级对应次的学科
        Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIds(xqid, gradeId, sid);
        for (N33_StudentTagEntry studentTagEntry : tagEntries) {
            //等级
            N33_XuNiBanEntry xuNiBanEntry = new N33_XuNiBanEntry(new ArrayList<N33_XuNiBanEntry.StudentTag>(), sid, xqid, gradeId, new ArrayList<ObjectId>());
            xuNiBanEntry.setType(0);
            //默认一个标签一个
            List<N33_XuNiBanEntry.StudentTag> tags = new ArrayList<N33_XuNiBanEntry.StudentTag>();
            N33_XuNiBanEntry.StudentTag tag = new N33_XuNiBanEntry.StudentTag();
            //标签id
            tag.setTagId(studentTagEntry.getID());
            //标签名
            if (studentTagEntry.getStudents().size() > 0) {
                N33_StudentTagEntry.StudentInfoEntry infoEntry = studentTagEntry.getStudents().get(0);
                if (infoEntry != null) {
                    StudentEntry studentEntry = entryMap.get(infoEntry.getStuId());
                    tag.setTagName(studentEntry.getCombiname());
                    tag.setTagCount(studentTagEntry.getStudents().size());
                    tag.setBqName(studentTagEntry.getName());
                    //学科标签
                    ObjectId sub1 = studentEntry.getSubjectId1();
                    ObjectId sub2 = studentEntry.getSubjectId2();
                    ObjectId sub3 = studentEntry.getSubjectId3();
                    //学科
                    List<N33_XuNiBanEntry.SubjectInfo> infos = new ArrayList<N33_XuNiBanEntry.SubjectInfo>();
                    infos.add(getSubjectInfoByStudentList(sub1, studentTagEntry.getStudents(), entryMap));
                    infos.add(getSubjectInfoByStudentList(sub2, studentTagEntry.getStudents(), entryMap));
                    infos.add(getSubjectInfoByStudentList(sub3, studentTagEntry.getStudents(), entryMap));
                    tag.setSubjectList(infos);
                    //班级
                    List<ObjectId> cids = new ArrayList<ObjectId>();
                    for (N33_StudentTagEntry.StudentInfoEntry student : studentTagEntry.getStudents()) {
                        if (!cids.contains(student.getClassId())) {
                            cids.add(student.getClassId());
                        }
                    }
                    tag.setClsIds(cids);
                    tags.add(tag);
                    xuNiBanEntry.setTagList(tags);
                    xiNiEntry.add(xuNiBanEntry);
                    //合格
                    studentTagEntry.setXuNi(1);
                    N33_XuNiBanEntry xuNiBanEntry1 = new N33_XuNiBanEntry();
                    xuNiBanEntry1.setTagList(xuNiBanEntry.getTagList());
                    xuNiBanEntry1.setGradeId(xuNiBanEntry.getGradeId());
                    xuNiBanEntry1.setJxbIds(xuNiBanEntry.getJxbIds());
                    xuNiBanEntry1.setXqId(xuNiBanEntry.getXqId());
                    xuNiBanEntry1.setType(1);
                    xuNiBanEntry1.setSchoolId(xuNiBanEntry.getSchoolId());
                    //合格学科
                    List<ObjectId> subIds = new ArrayList<ObjectId>();
                    List<ObjectId> subIds2 = new ArrayList<ObjectId>();
                    subIds.add(sub1);
                    subIds.add(sub2);
                    subIds.add(sub3);
                    String name = "";
                    for (N33_KSEntry ksEntry : ksEntryMap.values()) {
                        if (ksEntry.getIsZouBan() == 1) {
                            if (!subIds.contains(ksEntry.getSubjectId())) {
                                name += ksEntry.getSubjectName().substring(0, 1);
                                subIds.add(ksEntry.getSubjectId());
                                subIds2.add(ksEntry.getSubjectId());
                            }
                        }
                    }
                    List<N33_XuNiBanEntry.StudentTag> studentTags = new ArrayList<N33_XuNiBanEntry.StudentTag>();
                    for (N33_XuNiBanEntry.StudentTag xuNiBanEntry2 : xuNiBanEntry1.getTagList()) {
                        N33_XuNiBanEntry.StudentTag tag1 = new N33_XuNiBanEntry.StudentTag();
                        tag1.setTagName(name);
                        tag1.setTagCount(studentTagEntry.getStudents().size());
                        tag1.setTagId(studentTagEntry.getID());
                        List<N33_XuNiBanEntry.SubjectInfo> infos1 = new ArrayList<N33_XuNiBanEntry.SubjectInfo>();
                        for (Integer count = 0; count < xuNiBanEntry2.getSubjectList().size(); count++) {
                            N33_XuNiBanEntry.SubjectInfo sub = new N33_XuNiBanEntry.SubjectInfo();
                            sub.setSubId(subIds2.get(count));
                            sub.setIsFinish(xuNiBanEntry2.getSubjectList().get(count).getIsFinish());
                            sub.setStuCount(xuNiBanEntry2.getSubjectList().get(count).getStuCount());
                            sub.setStuIds(xuNiBanEntry2.getSubjectList().get(count).getStuIds());
                            infos1.add(sub);
                        }
                        tag1.setBqName(studentTagEntry.getName());
                        tag1.setSubjectList(infos1);
                        tag1.setClsIds(cids);
                        studentTags.add(tag1);
                    }
                    xuNiBanEntry1.setTagList(studentTags);
                    xuNiBanEntry1.setGradeId(gradeId);
                    xiNiEntry.add(xuNiBanEntry1);
                    studentTagDao.update(studentTagEntry);
                }
            }
        }
        if (xiNiEntry.size() > 0) {
            virtualClassDao.addN33_VirtualClassEntry(xiNiEntry);
        }
    }

    /**
     * 获得虚拟班标签的学科人数
     *
     * @param subId
     * @param infoEntries
     * @param map
     * @return
     */
    public N33_XuNiBanEntry.SubjectInfo getSubjectInfoByStudentList(ObjectId subId, List<N33_StudentTagEntry.StudentInfoEntry> infoEntries, Map<ObjectId, StudentEntry> map) {
        N33_XuNiBanEntry.SubjectInfo info = new N33_XuNiBanEntry.SubjectInfo(subId, 0, new ArrayList<ObjectId>(), 0);
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        for (N33_StudentTagEntry.StudentInfoEntry infoEntry : infoEntries) {
            if (!stuIds.contains(infoEntry.getStuId())) {
                StudentEntry entry = map.get(infoEntry.getStuId());
                if (entry != null) {
                    if (entry.getSubjectId1().toString().equals(subId.toString())) {
                        stuIds.add(infoEntry.getStuId());
                    }
                    if (entry.getSubjectId2().toString().equals(subId.toString())) {
                        stuIds.add(infoEntry.getStuId());
                    }
                    if (entry.getSubjectId3().toString().equals(subId.toString())) {
                        stuIds.add(infoEntry.getStuId());
                    }
                }
            }
        }
        info.setStuIds(stuIds);
        info.setStuCount(stuIds.size());
        return info;
    }

    /**
     * 查询本次的所有虚拟班
     *
     * @return
     */
    public List<Map<String, Object>> getVirtualClassByXqidAndGradeId(ObjectId xqid, ObjectId sid, ObjectId gid, Integer type, Integer isHide) {
        //返回数据
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        //对应次对应年级的学生
        Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidAndGradeIdMap(gid, xqid);
        //对应次对应年级所有的行政班
        Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(gid, xqid);
        //对应年级对应次的学科
        Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIdsForSubId(xqid, gid, sid);
        //对应次的老师
        Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(xqid, sid);
        //对应次的教室
        Map<ObjectId, N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomEntryListByXqGradeForMap(xqid, sid);
        if (type != 2) {
            //对应次的所有虚拟班
            List<N33_XuNiBanEntry> virtualClassList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, sid, gid, type);
            //年级名称
            String gradeName = gradeDao.findIsolateGradeEntryByGradeId(gid, xqid).getName();
            Integer combineName = 1;
            for (N33_XuNiBanEntry xuNiBanEntry : virtualClassList) {
                List<ObjectId> jxbIds = xuNiBanEntry.getJxbIds();
                //对应虚拟班关联的教学班
                List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBListByIds(jxbIds, xqid);
                //返回的学科的List
                List<Map<String, Object>> subjectList = new ArrayList<Map<String, Object>>();
                //取出虚拟班学生所在的行政班
                Map<ObjectId, Integer> clsIdsMap = new HashMap<ObjectId, Integer>();
                List<Map<String, Object>> stuSource = new ArrayList<Map<String, Object>>();
                //返回的选课名称
                List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
                //标签名
                List<String> bqList = new ArrayList<String>();
                List<N33_XuNiBanEntry.StudentTag> studentTags = xuNiBanEntry.getTagList();
                for (N33_XuNiBanEntry.StudentTag studentTag : studentTags) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", studentTag.getTagName());
                    map.put("count", studentTag.getTagCount());
                    tagList.add(map);
                    bqList.add(studentTag.getBqName());
                    List<ObjectId> clsIdsList = studentTag.getClsIds();
                    for (ObjectId ids : clsIdsList) {
                        if (clsIdsMap.get(ids) == null) {
                            clsIdsMap.put(ids, 0);
                        }
                    }
                    List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
                    //定义变量记录生成同一个标签中返回学科记录的次数，如果是第一次，则记录学生来源
                    Integer loopCount = 0;
                    for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                        loopCount++;
                        if (loopCount == 1) {
                            List<ObjectId> stuIds = subjectInfo.getStuIds();
                            for (ObjectId ids : stuIds) {
                                Integer count = clsIdsMap.get(studentEntryMap.get(ids).getClassId());
                                count++;
                                clsIdsMap.put(studentEntryMap.get(ids).getClassId(), count);
                            }
                        }
                        //返回的对应虚拟班的学科Map
                        if (isHide == 1) {
                            if (subjectInfo.getIsFinish() == 0) {
                                addSubjectList(subjectInfo, studentEntryMap, subjectList, ksEntryMap, jxbEntryList, classroomEntryMap, teaEntryMap, loopCount);
                            }
                        } else {
                            addSubjectList(subjectInfo, studentEntryMap, subjectList, ksEntryMap, jxbEntryList, classroomEntryMap, teaEntryMap, loopCount);
                        }
                    }
                }
                for (Map.Entry<ObjectId, Integer> entry : clsIdsMap.entrySet()) {
                    if (entry.getValue() == 0) {
                        continue;
                    }
                    //返回虚拟班的学生来源
                    Map<String, Object> classMap = new HashMap<String, Object>();
                    classMap.put("sourceClass", gradeName + "（" + classEntryMap.get(entry.getKey()).getXh() + "）班");
                    classMap.put("sourceStuCount", entry.getValue());
                    stuSource.add(classMap);
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", xuNiBanEntry.getID().toString());
                map.put("tagName", tagList);
                map.put("bqName", bqList);
                map.put("combineName", "组合" + combineName);
                map.put("stuSource", stuSource);
                map.put("subjectList", subjectList);
                map.put("size", subjectList.size());
                result.add(map);
                combineName++;
            }
        } else {
            List<ClassEntry> classEntries = classDao.findByGradeIdId(sid, gid, xqid);
            List<N33_KSEntry> ksEn = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
            List<ObjectId> subIds = new ArrayList<ObjectId>();
            for (N33_KSEntry entry : ksEn) {
                if (entry.getIsZouBan() == 0) {
                    subIds.add(entry.getSubjectId());
                }
            }
            //jxb add
            ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
            int acClassType = turnOffService.getAcClassType(sid, gid, ciId,1);
            //jxb add
            List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gid, subIds, xqid, 3, acClassType);
            if (type == 3) {
                jxbEntries.addAll(jxbDao.getJXBList(sid, gid, subIds, xqid, 6));
            }
            for (ClassEntry classEntry : classEntries) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("combineName", classEntry.getName());
                List<Map<String, Object>> subjectList = new ArrayList<Map<String, Object>>();
                for (N33_JXBEntry entry : jxbEntries) {
                    List<ObjectId> userIds = classEntry.getStudentList();
                    userIds.removeAll(entry.getStudentIds());
                    if(userIds.size()==0) {
                        Map<String, Object> subjectMap = new HashMap<String, Object>();
                        subjectMap.put("subjectId", entry.getSubjectId().toString());
                        if (ksEntryMap.get(entry.getSubjectId()) == null) {
                            subjectMap.put("subName", "");
                        } else {
                            subjectMap.put("subName", ksEntryMap.get(entry.getSubjectId()).getSubjectName());
                        }
                        subjectMap.put("jxbName", entry.getName());
                        subjectMap.put("jxbId", entry.getID().toString());
                        subjectMap.put("jxbStuCount", entry.getStudentIds().size());
                        subjectMap.put("classRoom", entry.getClassroomId() == null ? "" : classroomEntryMap.get(entry.getClassroomId()).getRoomName());
                        subjectMap.put("teaName", entry.getTercherId() == null ? "" : teaEntryMap.get(entry.getTercherId()).getUserName());
                        subjectMap.put("teaId", entry.getTercherId() == null ? "" : entry.getTercherId().toString());
                        subjectMap.put("classRoomId", entry.getClassroomId() == null ? "" : entry.getClassroomId().toString());
                        subjectList.add(subjectMap);
                    }
                }
                map.put("subjectList", subjectList);
                result.add(map);
            }
        }
        return result;
    }

    private void addSubjectList(N33_XuNiBanEntry.SubjectInfo subjectInfo, Map<ObjectId, StudentEntry> studentEntryMap, List<Map<String, Object>> subjectList, Map<ObjectId, N33_KSEntry> ksEntryMap, List<N33_JXBEntry> jxbEntryList, Map<ObjectId, N33_ClassroomEntry> classroomEntryMap, Map<ObjectId, N33_TeaEntry> teaEntryMap, Integer loopCount) {
        Map<String, Object> subjectMap = new HashMap<String, Object>();
        boolean flag = true;
        for (Map<String, Object> map : subjectList) {
            if (subjectInfo.getSubId().toString().equals((String) map.get("subjectId"))) {
                flag = false;
                Integer count = (Integer) map.get("stuCount");
                count += subjectInfo.getStuCount();
                map.put("stuCount", count);
                break;
            }
        }
        if (flag) {
            //判断该学科是否已经有教学班，若没有，值放空
            boolean isHaveJxb = true;
            subjectMap.put("subjectId", subjectInfo.getSubId().toString());
            if (ksEntryMap.get(subjectInfo.getSubId()) == null) {
                subjectMap.put("subName", "");
            } else {
                subjectMap.put("subName", ksEntryMap.get(subjectInfo.getSubId()).getSubjectName());
            }
            subjectMap.put("stuCount", subjectInfo.getStuCount());
            if (jxbEntryList != null && jxbEntryList.size() > 0) {
                for (N33_JXBEntry entry : jxbEntryList) {
                    if (entry.getSubjectId().toString().equals(subjectInfo.getSubId().toString())) {
                        isHaveJxb = false;
                        subjectMap.put("jxbName", entry.getName());
                        subjectMap.put("jxbId", entry.getID().toString());
                        subjectMap.put("jxbStuCount", entry.getStudentIds().size());
                        subjectMap.put("classRoom", entry.getClassroomId() == null ? "" : classroomEntryMap.get(entry.getClassroomId()).getRoomName());
                        subjectMap.put("teaName", entry.getTercherId() == null ? "" : teaEntryMap.get(entry.getTercherId()).getUserName());
                        subjectMap.put("teaId", entry.getTercherId() == null ? "" : entry.getTercherId().toString());
                        subjectMap.put("classRoomId", entry.getClassroomId() == null ? "" : entry.getClassroomId().toString());
                        break;
                    }
                }
            }
            if (isHaveJxb) {
                subjectMap.put("jxbName", "");
                subjectMap.put("jxbId", "");
                subjectMap.put("classRoom", "");
                subjectMap.put("teaName", "");
                subjectMap.put("teaId", "");
                subjectMap.put("classRoomId", "");
                subjectMap.put("jxbStuCount", "");
            }
            subjectMap.put("isFinish", subjectInfo.getIsFinish());
            subjectList.add(subjectMap);
        }
    }

    /**
     * 清空给虚拟班已分配的所有教学班
     *
     * @param sid
     * @param xqid
     */
    public void clearAllJxb(ObjectId sid, ObjectId xqid, ObjectId gradeId, Integer type) {
        //查询所有对应类型的虚拟班
        List<N33_XuNiBanEntry> xuNiBanEntryList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, sid, gradeId, type);

        //查询对应年级对应次所有的教学班(Map)
        // jxb add 
        ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        // jxb add
        Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(sid, gradeId, xqid, acClassType);

        for (N33_XuNiBanEntry xuNiBanEntry : xuNiBanEntryList) {
            List<N33_XuNiBanEntry.StudentTag> studentTags = xuNiBanEntry.getTagList();
            List<ObjectId> jxbIds = xuNiBanEntry.getJxbIds();
            if (jxbIds.size() > 0) {
                for (ObjectId ids : jxbIds) {
                    //得到需要更改学生的教学班
                    N33_JXBEntry jxbEntry = jxbEntryMap.get(ids);
                    //教学班要移除的学生
                    List<ObjectId> stuIds = new ArrayList<ObjectId>();
                    for (N33_XuNiBanEntry.StudentTag studentTag : studentTags) {
                        List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
                        for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                            if (subjectInfo.getSubId().toString().equals(jxbEntry.getSubjectId().toString())) {
                                stuIds.addAll(subjectInfo.getStuIds());
                            }
                        }
                    }
                    //教学班的学生
                    List<ObjectId> jxbStuIds = jxbEntry.getStudentIds();
                    jxbStuIds.removeAll(stuIds);
                    jxbEntry.setStudentIds(jxbStuIds);
                    jxbDao.updateN33_JXB(jxbEntry);
                }
                xuNiBanEntry.setJxbIds(new ArrayList<ObjectId>());
                virtualClassDao.updateN33_VirtualClassEntry(xuNiBanEntry);
            }
        }


    }

    /**
     * 清空教学班
     *
     * @param xuNiBanId
     * @param jxbId
     */
    public void clearJXB(ObjectId xuNiBanId, ObjectId jxbId, ObjectId subId, ObjectId sid) {
        N33_XuNiBanEntry xuNiBanEntry = virtualClassDao.findN33_VirtualClassEntry(xuNiBanId);
        List<ObjectId> jxbIds = xuNiBanEntry.getJxbIds();
        jxbIds.remove(jxbId);
        xuNiBanEntry.setJxbIds(jxbIds);
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        List<N33_XuNiBanEntry.StudentTag> studentTags = xuNiBanEntry.getTagList();
        for (N33_XuNiBanEntry.StudentTag studentTag : studentTags) {
            List<N33_XuNiBanEntry.SubjectInfo> subjectInfos = studentTag.getSubjectList();
            for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfos) {
                if (subjectInfo.getSubId().toString().equals(subId.toString())) {
                    stuIds.addAll(subjectInfo.getStuIds());
                }
            }
        }

        //为设置的教学班添加该虚拟班的学生
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
        List<ObjectId> jxbEntryStudents = jxbEntry.getStudentIds();
        jxbEntryStudents.removeAll(stuIds);
        jxbEntry.setStudentIds(jxbEntryStudents);

        jxbDao.updateN33_JXB(jxbEntry);
        virtualClassDao.updateN33_VirtualClassEntry(xuNiBanEntry);
    }

    /**
     * 选择教学班
     *
     * @param xuNiBanId
     * @param jxbId
     * @param oldJxbId  !*  更改教学班    *    第一次选择教学班
     */
    public String selectJXB(ObjectId xuNiBanId, ObjectId jxbId, ObjectId subId, String oldJxbId, ObjectId sid) {
        ObjectId oJxbId = null;
        if (!"*".equals(oldJxbId)) {
            oJxbId = new ObjectId(oldJxbId);
        }
        N33_XuNiBanEntry xuNiBanEntry = virtualClassDao.findN33_VirtualClassEntry(xuNiBanId);
        List<ObjectId> jxbIds = xuNiBanEntry.getJxbIds();
        if (jxbIds != null) {
            if (!"*".equals(oldJxbId)) {
                jxbIds.remove(oJxbId);
            }
            jxbIds.add(jxbId);
        } else {
            jxbIds = new ArrayList<ObjectId>();
            if (!"*".equals(oldJxbId)) {
                jxbIds.remove(oJxbId);
            }
            jxbIds.add(jxbId);
        }
        xuNiBanEntry.setJxbIds(jxbIds);
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        List<N33_XuNiBanEntry.StudentTag> studentTags = xuNiBanEntry.getTagList();
        for (N33_XuNiBanEntry.StudentTag studentTag : studentTags) {
            List<N33_XuNiBanEntry.SubjectInfo> subjectInfos = studentTag.getSubjectList();
            for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfos) {
                if (subjectInfo.getSubId().toString().equals(subId.toString())) {
                    stuIds.addAll(subjectInfo.getStuIds());
                }
            }
        }

        //新增教学班的时候才检测冲突，因为修改教学班的时候学生是不可能存在其他的相同走班类型的教学班中
        if ("*".equals(oldJxbId)) {
            //检测冲突
            //判断是否是相同的学科
            if (tagService.getStuExistOtherJxb(subId, stuIds, jxbId, getDefaultPaiKeTerm(sid).getPaikeci())) {
                return "对应虚拟班中的学生已经存在于某个同学科的教学班中，选择教学班失败!";
            }
        }

        //判断虚拟班中的学生是否存在教学班中  true存在  false不存在
        if (tagService.getStuExistJxb(jxbId, stuIds, getDefaultPaiKeTerm(sid).getPaikeci())) {
            return "该教学班已经存在对应虚拟班中的学生，选择教学班失败!";
        }

        //为更改的教学班移出该虚拟班的学生
        if (!"*".equals(oldJxbId)) {
            N33_JXBEntry oJxbEntry = jxbDao.getJXBById(oJxbId);
            List<ObjectId> oJxbEntryStudents = oJxbEntry.getStudentIds();
            oJxbEntryStudents.removeAll(stuIds);
            oJxbEntry.setStudentIds(oJxbEntryStudents);
            jxbDao.updateN33_JXB(oJxbEntry);
        }

        //为设置的教学班添加该虚拟班的学生
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
        List<ObjectId> jxbEntryStudents = jxbEntry.getStudentIds();
        jxbEntryStudents.addAll(stuIds);
        jxbEntry.setStudentIds(jxbEntryStudents);
        //jxbEntry.setStudentIds(stuIds);


        //重置冲突关系
        //在页面上点击生成冲突
        /*tagService.conflictDetection(jxbEntry.getTermId(), jxbEntry.getSchoolId(), jxbEntry, getDefaultPaiKeTerm(sid).getPaikeci());*/

        jxbDao.updateN33_JXB(jxbEntry);
        virtualClassDao.updateN33_VirtualClassEntry(xuNiBanEntry);
        return "成功选择教学班";
    }

    /**
     * 释放虚拟班
     *
     * @param xuNiBanId
     * @param xqid
     */
    public void shiFangXuNiBan(ObjectId xuNiBanId, ObjectId xqid) {


        N33_XuNiBanEntry xuNiBanEntry = virtualClassDao.findN33_VirtualClassEntry(xuNiBanId);
        List<N33_XuNiBanEntry> virtualClassList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, xuNiBanEntry.getSchoolId(), xuNiBanEntry.getGradeId(), 1);
        if (xuNiBanEntry.getType() == 1) {
            virtualClassList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, xuNiBanEntry.getSchoolId(), xuNiBanEntry.getGradeId(), 0);
        }
        //将要拼成的合格虚拟班
        N33_XuNiBanEntry HGpinXuNiBanEntry = null;
        for (N33_XuNiBanEntry xuNiBanEntry1 : virtualClassList) {

            List<ObjectId> tagIdsList = new ArrayList<ObjectId>();
            for (N33_XuNiBanEntry.StudentTag studentTag : xuNiBanEntry1.getTagList()) {
                tagIdsList.add(studentTag.getTagId());
            }
            //根据该标签集合查找对应的将要拼成的合格虚拟班
            List<ObjectId> tagIds = new ArrayList<ObjectId>();
            for (N33_XuNiBanEntry.StudentTag studentTag : xuNiBanEntry.getTagList()) {
                tagIds.add(studentTag.getTagId());
            }
            if (tagIds.size() == tagIdsList.size()) {
                tagIds.retainAll(tagIdsList);
                if (tagIds.size() == tagIdsList.size()) {
                    HGpinXuNiBanEntry = xuNiBanEntry1;
                }
            }
            if (HGpinXuNiBanEntry != null) {
                break;
            }
        }
        //学生
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        for (N33_XuNiBanEntry.StudentTag tag : xuNiBanEntry.getTagList()) {
            for (N33_XuNiBanEntry.SubjectInfo subjectInfo : tag.getSubjectList()) {
                subjectInfo.setIsFinish(0);
                stuIds.addAll(subjectInfo.getStuIds());
            }
        }
       /* for (N33_XuNiBanEntry.StudentTag tag : HGpinXuNiBanEntry.getTagList()) {
            for (N33_XuNiBanEntry.SubjectInfo subjectInfo : tag.getSubjectList()) {
                subjectInfo.setIsFinish(0);
            }
        }*/
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        jxbIds.addAll(xuNiBanEntry.getJxbIds());
        jxbIds.addAll(HGpinXuNiBanEntry.getJxbIds());
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBListByIds(jxbIds, xqid);
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            List<ObjectId> stuId = jxbEntry.getStudentIds();
            stuId.removeAll(stuIds);
            jxbEntry.setStudentIds(stuId);
            jxbDao.updateN33_JXB(jxbEntry);
        }
        //删除虚拟班
        virtualClassDao.delN33_VirtualClassEntry(xuNiBanId);
        virtualClassDao.delN33_VirtualClassEntry(HGpinXuNiBanEntry.getID());
        //创建新虚拟班
        List<N33_XuNiBanEntry> xuNiBanEntries = new ArrayList<N33_XuNiBanEntry>();
        for (N33_XuNiBanEntry.StudentTag tag : xuNiBanEntry.getTagList()) {
            N33_XuNiBanEntry xuNiBanEntry1 = new N33_XuNiBanEntry();
            List<N33_XuNiBanEntry.StudentTag> tags = new ArrayList<N33_XuNiBanEntry.StudentTag>();
            tags.add(tag);
            xuNiBanEntry1.setTagList(tags);
            xuNiBanEntry1.setType(xuNiBanEntry.getType());
            xuNiBanEntry1.setGradeId(xuNiBanEntry.getGradeId());
            xuNiBanEntry1.setSchoolId(xuNiBanEntry.getSchoolId());
            xuNiBanEntry1.setXqId(xuNiBanEntry.getXqId());
            xuNiBanEntry1.setJxbIds(new ArrayList<ObjectId>());
            xuNiBanEntries.add(xuNiBanEntry1);
        }
        //合格型
        for (N33_XuNiBanEntry.StudentTag tag : HGpinXuNiBanEntry.getTagList()) {
            N33_XuNiBanEntry xuNiBanEntry1 = new N33_XuNiBanEntry();
            List<N33_XuNiBanEntry.StudentTag> tags = new ArrayList<N33_XuNiBanEntry.StudentTag>();
            tags.add(tag);
            xuNiBanEntry1.setTagList(tags);
            xuNiBanEntry1.setType(HGpinXuNiBanEntry.getType());
            xuNiBanEntry1.setGradeId(HGpinXuNiBanEntry.getGradeId());
            xuNiBanEntry1.setSchoolId(HGpinXuNiBanEntry.getSchoolId());
            xuNiBanEntry1.setXqId(HGpinXuNiBanEntry.getXqId());
            xuNiBanEntry1.setJxbIds(new ArrayList<ObjectId>());
            xuNiBanEntries.add(xuNiBanEntry1);
        }
        virtualClassDao.addN33_VirtualClassEntry(xuNiBanEntries);
        //if(xuNiBanEntry.getJxbIds().size()>0) {
        //重置冲突关系
        //tagService.conflictDetection(getDefauleTermId(xuNiBanEntry.getSchoolId()), xuNiBanEntry.getSchoolId(), xuNiBanEntry.getGradeId(), getDefaultPaiKeTerm(xuNiBanEntry.getSchoolId()).getPaikeci());
        //}
    }

    /**
     * 查询组合分班
     *
     * @param virtualClassId
     * @return
     */
    public Map<String, Object> getVirtualClassForCombineClass(ObjectId virtualClassId, Integer type, ObjectId xqid, ObjectId schoolId, ObjectId gradeId) {
        List<Map<String, Object>> calcList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();

        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId, gradeId, xqid, 1);
        if(null!=turnOff){
            acClassType = turnOff.getAcClass();
        }

        //对应次的所有虚拟班
        List<N33_XuNiBanEntry> virtualClassList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, schoolId, gradeId, type);
        //要拼班的虚拟班
        N33_XuNiBanEntry xuNiBanEntry = virtualClassDao.findN33_VirtualClassEntry(virtualClassId);
        List<N33_XuNiBanEntry.StudentTag> studentTagList = xuNiBanEntry.getTagList();
        List<N33_KSDTO> N33_KSDTOList = subjectService.getIsolateSubjectByGradeIdIncludeMain(xqid, schoolId, gradeId.toString(), 1);
        N33_KSDTO ksDto = null;
        if (N33_KSDTOList != null && N33_KSDTOList.size() > 0) {
            ksDto = N33_KSDTOList.get(0);
        }
        List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
        if (ksDto != null) {
            jxbEntries = jxbDao.getJXBList(schoolId, gradeId, new ObjectId(ksDto.getSubid()), xqid, acClassType);
        }

        Integer rl = null;
        if (jxbEntries.size() > 0) {
            rl = jxbEntries.get(0).getRongLiang();
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("aware", "请创建走班课教学班");
            return map;
        }
        //如果是第一次拼班，要找到所有可能的两科组合
        if (studentTagList.size() == 1) {
            List<ObjectId> subIds = new ArrayList<ObjectId>();
            N33_XuNiBanEntry.StudentTag studentTag = studentTagList.get(0);
            List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
            Map<ObjectId, Integer> stuCountMap = new HashMap<ObjectId, Integer>();
            for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                subIds.add(subjectInfo.getSubId());
                stuCountMap.put(subjectInfo.getSubId(), subjectInfo.getStuCount());
            }
            /*final Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
            for(N33_KSDTO dto:N33_KSDTOList) {
                if(subIds.contains(new ObjectId(dto.getId())))
                {
                    subjectMap.put(new ObjectId(dto.getSubid()),dto.getSnm());
                }
            }*/
            //List<String> combineNameList = new ArrayList<String>();
            List<Map<String, Object>> xuNiBanEntryList = autoFenBanService.getCanPinBanListByThreeSubId(false, rl, virtualClassId, subIds, virtualClassList, stuCountMap);
            //随机取两科
            List<CombineUtils.CombineResult<ObjectId>> ll = new ArrayList<CombineUtils.CombineResult<ObjectId>>();
            CombineUtils.combinerSelect(subIds, 2, ll); // 对现有学科进行组合与保存的进行比较
            List<Map<String, Object>> returnXuNiBanEntryList = new ArrayList<Map<String, Object>>();
            for (CombineUtils.CombineResult cr : ll) {
                //遍历每一个两科，查询所有的虚拟班中包含这两科的组合
                //String combineName=getCombineName(subjectMap,cr.getList());
                returnXuNiBanEntryList = judgeSelect(false, virtualClassId, virtualClassList, stuCountMap, rl, cr.getList());
                retList.addAll(returnXuNiBanEntryList);
            }
            retList.addAll(xuNiBanEntryList);

            if (xuNiBanEntryList != null && xuNiBanEntryList.size() > 0) {
                calcList.addAll(xuNiBanEntryList);
            } else {
                calcList.addAll(retList);
            }
        } else if (studentTagList.size() > 1) {
            List<ObjectId> list = autoFenBanService.isDingErZouYi(studentTagList);
            if (list.size() != 0) {
                Map<ObjectId, Integer> stuCountMap = new HashMap<ObjectId, Integer>();
                stuCountMap = autoFenBanService.getStudentCountMap(studentTagList);
                List<Map<String, Object>> xuNiBanEntryList = judgeSelect(false, virtualClassId, virtualClassList, stuCountMap, rl, list);
                retList.addAll(xuNiBanEntryList);
            } else {
                List<ObjectId> subIds = new ArrayList<ObjectId>();
                N33_XuNiBanEntry.StudentTag studentTag = studentTagList.get(0);
                List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
                for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                    subIds.add(subjectInfo.getSubId());
                }
                Map<ObjectId, Integer> stuCountMap = new HashMap<ObjectId, Integer>();
                stuCountMap = autoFenBanService.getStudentCountMap(studentTagList);

                List<Map<String, Object>> xuNiBanEntryList = autoFenBanService.getCanPinBanListByThreeSubId(false, rl, virtualClassId, subIds, virtualClassList, stuCountMap);
                //随机取两科
                List<CombineUtils.CombineResult<ObjectId>> ll = new ArrayList<CombineUtils.CombineResult<ObjectId>>();
                CombineUtils.combinerSelect(subIds, 2, ll); // 对现有学科进行组合与保存的进行比较
                List<Map<String, Object>> returnXuNiBanEntryList = new ArrayList<Map<String, Object>>();
                for (CombineUtils.CombineResult cr : ll) {
                    //遍历每一个两科，查询所有的虚拟班中包含这两科的组合
                    //String combineName=getCombineName(subjectMap,cr.getList());
                    returnXuNiBanEntryList = judgeSelect(false, virtualClassId, virtualClassList, stuCountMap, rl, cr.getList());
                    retList.addAll(returnXuNiBanEntryList);
                }
                retList.addAll(xuNiBanEntryList);
                if (xuNiBanEntryList != null && xuNiBanEntryList.size() > 0) {
                    calcList.addAll(xuNiBanEntryList);
                } else {
                    calcList.addAll(retList);
                }
            }
        }
        double variance = 0.00;
        String id = "";
        if (calcList.size() > 0) {
            for (Map<String, Object> map : calcList) {
                if ((Integer) map.get("isBigger") == 0) {
                    variance = (Double) map.get("variance");
                    id = (String) map.get("id");
                    break;
                }
            }
        }

        for (Map<String, Object> map : calcList) {
            if ((Integer) map.get("isBigger") == 0) {
                if ((Double) map.get("variance") <= variance) {
                    variance = (Double) map.get("variance");
                    id = (String) map.get("id");
                }
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("retList", retList);
        return map;
    }

    /**
     * 符合返回条件的虚拟班
     *
     * @param xuNiBanEntries
     * @return
     */
    public List<Map<String, Object>> judgeSelect(boolean isAuto, ObjectId virtualClassId, List<N33_XuNiBanEntry> xuNiBanEntries, Map<ObjectId, Integer> stuCountMap, Integer rl, List<ObjectId> crList) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        //已经加入返回的List中，用于在再次遍历的时候去除已经返回的记录
        List<N33_XuNiBanEntry> ycXuNiBanList = new ArrayList<N33_XuNiBanEntry>();
        for (N33_XuNiBanEntry xuNiBanEntry : xuNiBanEntries) {
            if (xuNiBanEntry.getTagList().size() == 1 && xuNiBanEntry.getJxbIds().size() == 0 && !xuNiBanEntry.getID().toString().equals(virtualClassId.toString())) {
                Map<ObjectId, Integer> replaceMap = new HashMap<ObjectId, Integer>();
                for (Map.Entry<ObjectId, Integer> entry : stuCountMap.entrySet()) {
                    replaceMap.put(entry.getKey(), entry.getValue());
                }
                Map<String, Object> map = new HashMap<String, Object>();
                List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = xuNiBanEntry.getTagList().get(0).getSubjectList();
                List<ObjectId> subIds = new ArrayList<ObjectId>();
                for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                    subIds.add(subjectInfo.getSubId());
                }
                List<ObjectId> calcList = new ArrayList<ObjectId>();
                //方差
                double variance = 0.00;
                double sum = 0.00;
                for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                    if ((subIds.contains(crList.get(0))) && (subIds.contains(crList.get(1)))) {
                        ycXuNiBanList.add(xuNiBanEntry);
                        for (ObjectId ids : crList) {
                            if (ids.toString().equals(subjectInfo.getSubId().toString())) {
                                Integer stuCount = replaceMap.get(ids);
                                stuCount += subjectInfo.getStuCount();
                                replaceMap.put(ids, stuCount);
                                calcList.add(ids);
                            }
                        }
                    }
                }

                BigDecimal bd = new BigDecimal(rl * 0.05).setScale(0, BigDecimal.ROUND_HALF_UP);
                Integer maxCount = Integer.parseInt(bd.toString()) + rl;
                if (calcList.size() > 0) {
                    for (ObjectId id : calcList) {
                        sum += Math.pow((replaceMap.get(id) - rl), 2);
                    }
                    if ((Integer) replaceMap.get(calcList.get(0)) > maxCount) {
                        if (isAuto == true) {
                            continue;
                        } else {
                            variance = sum / calcList.size();
                            map.put("variance", variance);
                            map.put("id", xuNiBanEntry.getID().toString());
                            map.put("name", xuNiBanEntry.getTagList().get(0).getTagName());
                            map.put("bqName", xuNiBanEntry.getTagList().get(0).getBqName());
                            map.put("stuCount", xuNiBanEntry.getTagList().get(0).getTagCount());
                            map.put("isBigger", 1);
                            result.add(map);
                        }
                    } else {
                        variance = sum / calcList.size();
                        map.put("variance", variance);
                        map.put("id", xuNiBanEntry.getID().toString());
                        map.put("name", xuNiBanEntry.getTagList().get(0).getTagName());
                        map.put("bqName", xuNiBanEntry.getTagList().get(0).getBqName());
                        map.put("stuCount", xuNiBanEntry.getTagList().get(0).getTagCount());
                        map.put("isBigger", 0);
                        //map.put("stuCount",xuNiBanEntry.getTagList().get(0).getSubjectList().get(0).getStuCount());
                        result.add(map);
                    }
                }
            }
        }
        xuNiBanEntries.removeAll(ycXuNiBanList);
        return result;
    }

    /**
     * 拼班操作
     *
     * @param pinId   将要拼成的虚拟班Id
     * @param pinById 被拼班的虚拟班Id
     * @param xqid
     * @param gradeId
     * @param sid
     */
    public String puzzleClass(ObjectId pinId, ObjectId pinById, ObjectId xqid, ObjectId gradeId, ObjectId sid) {

        //将要拼成的等级虚拟班
        N33_XuNiBanEntry pinXuNiBanEntry = virtualClassDao.findN33_VirtualClassEntry(pinId);
        //被拼班的等级虚拟班
        N33_XuNiBanEntry pinByXuNiBanEntry = virtualClassDao.findN33_VirtualClassEntry(pinById);
        //将要拼成的等级虚拟班的标签
        List<N33_XuNiBanEntry.StudentTag> pinStudentTags = pinXuNiBanEntry.getTagList();
        //被拼班的等级虚拟班的标签
        List<N33_XuNiBanEntry.StudentTag> pinByStudentTags = pinByXuNiBanEntry.getTagList();

        //对应次的所有合格虚拟班
        List<N33_XuNiBanEntry> virtualClassList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, sid, gradeId, 1);
        if (pinXuNiBanEntry.getType() == 1) {
            virtualClassList = virtualClassDao.getN33_VirtualClassEntryByXqidAndGrade(xqid, sid, gradeId, 0);
        }
        //将要拼成的合格虚拟班
        N33_XuNiBanEntry HGpinXuNiBanEntry = null;
        //被拼班的合格虚拟班
        N33_XuNiBanEntry HGpinByXuNiBanEntry = null;
        for (N33_XuNiBanEntry xuNiBanEntry : virtualClassList) {

            List<ObjectId> tagIdsList = new ArrayList<ObjectId>();
            for (N33_XuNiBanEntry.StudentTag studentTag : xuNiBanEntry.getTagList()) {
                tagIdsList.add(studentTag.getTagId());
            }

            //根据该标签集合查找对应的将要拼成的合格虚拟班
            List<ObjectId> tagIds = new ArrayList<ObjectId>();
            for (N33_XuNiBanEntry.StudentTag studentTag : pinXuNiBanEntry.getTagList()) {
                tagIds.add(studentTag.getTagId());
            }
            if (tagIds.size() == tagIdsList.size()) {
                tagIds.retainAll(tagIdsList);
                if (tagIds.size() == tagIdsList.size()) {
                    HGpinXuNiBanEntry = xuNiBanEntry;
                }
            }
            //根据该标签集合查找对应的被拼班的合格虚拟班
            List<ObjectId> tagIdsByPin = new ArrayList<ObjectId>();
            for (N33_XuNiBanEntry.StudentTag studentTag : pinByXuNiBanEntry.getTagList()) {
                tagIdsByPin.add(studentTag.getTagId());
            }
            if (tagIdsByPin.size() == tagIdsList.size()) {
                tagIdsByPin.retainAll(tagIdsList);
                if (tagIdsByPin.size() == tagIdsList.size()) {
                    HGpinByXuNiBanEntry = xuNiBanEntry;
                }
            }
            if (HGpinXuNiBanEntry != null && HGpinByXuNiBanEntry != null) {
                break;
            }
        }

        pinStudentTags.addAll(pinByStudentTags);
        pinXuNiBanEntry.setTagList(pinStudentTags);

        if (HGpinXuNiBanEntry != null && HGpinByXuNiBanEntry != null) {
            //将要拼成的合格虚拟班的标签
            List<N33_XuNiBanEntry.StudentTag> HGpinStudentTags = HGpinXuNiBanEntry.getTagList();
            //被拼班的合格虚拟班的标签
            List<N33_XuNiBanEntry.StudentTag> HGpinByStudentTags = HGpinByXuNiBanEntry.getTagList();
            HGpinStudentTags.addAll(HGpinByStudentTags);
            HGpinXuNiBanEntry.setTagList(HGpinStudentTags);
            virtualClassDao.updateN33_VirtualClassEntry(HGpinXuNiBanEntry);
            virtualClassDao.delN33_VirtualClassEntry(HGpinByXuNiBanEntry.getID());
        } else {
            return "不存在对应的合格类型的虚拟班拼班数据";
        }

        virtualClassDao.updateN33_VirtualClassEntry(pinXuNiBanEntry);
        virtualClassDao.delN33_VirtualClassEntry(pinByXuNiBanEntry.getID());
        return "拼班成功";
    }

    public void finishFenBan(ObjectId xqid, ObjectId subId, ObjectId id, Integer isFinish) {
        N33_XuNiBanEntry xuNiBanEntry = virtualClassDao.findN33_VirtualClassEntry(id);
        List<N33_XuNiBanEntry.StudentTag> studentTags = xuNiBanEntry.getTagList();
        for (N33_XuNiBanEntry.StudentTag studentTag : studentTags) {
            List<N33_XuNiBanEntry.SubjectInfo> subjectInfoList = studentTag.getSubjectList();
            for (N33_XuNiBanEntry.SubjectInfo subjectInfo : subjectInfoList) {
                if (subId.toString().equals(subjectInfo.getSubId().toString())) {
                    subjectInfo.setIsFinish(isFinish);
                }
            }
        }
        virtualClassDao.updateN33_VirtualClassEntry(xuNiBanEntry);
    }

    private String getCombineName(Map<ObjectId, String> subjectMap, List<ObjectId> values) {
        List<String> templist = new ArrayList<String>();
        for (ObjectId id : values) {
            templist.add(subjectMap.get(id));
        }

        Collections.sort(templist, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return subject_sort.get(o1) - subject_sort.get(o2);
            }

        });
        String result = "";
        for (String s : templist) {
            result += s.substring(0, 1);
        }
        return result;
    }

}
