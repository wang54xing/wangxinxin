package com.fulaan.new33.service;

import com.db.new33.*;
import com.db.new33.isolate.*;
import com.db.new33.paike.*;
import com.fulaan.new33.dto.isolate.CourseRangeDTO;
import com.fulaan.new33.dto.isolate.GradeDTO;
import com.fulaan.new33.service.isolate.*;
import com.pojo.new33.CourseRangeEntry;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.N33_StudentTagEntry;
import com.pojo.new33.N33_XuNiBanEntry;
import com.pojo.new33.isolate.*;
import com.pojo.new33.paike.*;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HEAD;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步排课数据service
 */
@Service
public class N33_SyncPaikeDataService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(N33_SyncPaikeDataService.class);

    @Autowired
    private N33_StudentTagService tagService;
    @Autowired
    private IsolateUserService userService;
    @Autowired
    private IsolateSubjectService subjectService;
    @Autowired
    private N33_ClassroomService n33_classroomService;
    private N33_ClassroomDao n33_classroomDao = new N33_ClassroomDao();
    private CourseRangeDao courseRangeDao = new CourseRangeDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private TermDao termDao = new TermDao();
    private N33_StudentTagDao tagDao = new N33_StudentTagDao();
    private N33_FenBanInfoSetService n33_fenBanInfoSetService = new N33_FenBanInfoSetService();
    private N33_ZhuanXiangDao zhuanXiangDao = new N33_ZhuanXiangDao();
    @Autowired
    private IsolateClassService classService;
    private N33_ClassDao classDao = new N33_ClassDao();
    @Autowired
    private IsolateGradeService gradeService;
    private GradeDao gradeDao = new GradeDao();
    @Autowired
    private N33_StudentService studentService;
    private N33_StudentDao studentDao = new N33_StudentDao();
    @Autowired
    private CourseRangeService courseRangeService;
    private ZouBanTimeDao timeDao = new ZouBanTimeDao();

    private N33_SynRecordDao n33_synRecordDao = new N33_SynRecordDao();
    private N33_SysnEntryDao sysnDao = new N33_SysnEntryDao();

    private N33_YKBDao ykbDao = new N33_YKBDao();

    private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();

    private N33_GradeWeekRangeDao n33_gradeWeekRangeDao = new N33_GradeWeekRangeDao();

    private SchoolSelectLessonSetService schoolSelectLessonSetService = new SchoolSelectLessonSetService();

    private N33_ZIXIKEDao n33_zixikeDao = new N33_ZIXIKEDao();

    private N33_VirtualClassDao virtualClassDao = new N33_VirtualClassDao();

    private N33_TimeCombineDao combineDao = new N33_TimeCombineDao();


    public void synAllData(List<Integer> types, ObjectId schoolId, ObjectId oldCiId, ObjectId newCiId, HttpServletRequest request) throws InterruptedException {
        HttpSession session = request.getSession();
        Map<String, Object> status = new HashMap<String, Object>();
        session.setAttribute("status", status);
        try {
            Integer xuNiSyn = 1;
            for (int i = 0; i < types.size(); i++) {
                Integer type = types.get(i);
                if (type == 14) {
                    xuNiSyn = 0;
                }
            }
            for (int i = 0; i < types.size(); i++) {
                Integer type = types.get(i);
                if (type == 1) {
                    status.put("text", "正在同步教室数据...");
                    syncClassRommData(oldCiId, newCiId, schoolId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 2) {
                    status.put("text", "正在同步课表结构...");
                    syncCourseRangeData(oldCiId, newCiId, schoolId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 3) {
                    status.put("text", "正在同步教师数据...");
                    syncTeaData(oldCiId, newCiId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 4) {
                    status.put("text", "正在同步课时数据...");
                    syncSubData(oldCiId, newCiId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 5) {
                    status.put("text", "同步自习课数据...");
                    syncZiXiKe(schoolId, oldCiId, newCiId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 6) {
                    status.put("text", "正在同步教学班数据...");
                    syncJXBData(oldCiId, newCiId, schoolId, xuNiSyn);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 7) {
                    status.put("text", "同步班级数据...");
                    syncClassData(oldCiId, newCiId, schoolId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 8) {
                    status.put("text", "同步走班时间数据...");
                    syncZouBanTimeData(oldCiId, newCiId, schoolId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 9) {
                    status.put("text", "同步年级数据...");
                    syncGradeData(oldCiId, newCiId, schoolId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 10) {
                    status.put("text", "同步学生数据...");
                    syncStudentData(oldCiId, newCiId, schoolId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 11) {
                    status.put("text", "同步排课数据...");
                    syncPaiKeData(oldCiId, newCiId, schoolId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 12) {
                    status.put("text", "同步教学日数据...");
                    syncGradeWeekData(oldCiId, newCiId, schoolId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 13) {
                    status.put("text", "同步选科组合数据...");
                    syncXuanKeZuHe(schoolId, oldCiId, newCiId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 14) {
                    status.put("text", "同步虚拟班数据...");
                    syncVirtualClassData(schoolId, oldCiId, newCiId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }
                if (type == 15) {
                    status.put("text", "同步时段组合数据...");
                    syncTimeCombineData(schoolId, oldCiId, newCiId);
                    status.put("progress", i * 1.0 / types.size());
                    status.put("end", 0);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            status.put("text", "同步出错");
            status.put("progress", 1);
            status.put("end", 1);
        }
        status.put("text", "同步完成");
        status.put("progress", 1);
        status.put("end", 1);
        N33_SynRecordEntry recordEntry = n33_synRecordDao.getRecord(newCiId);
        if (recordEntry == null) {
            recordEntry = new N33_SynRecordEntry(schoolId, newCiId, oldCiId, types);
        } else {
            recordEntry.setOldCiId(oldCiId);
            recordEntry.setRecords(types);
        }
        n33_synRecordDao.add(recordEntry);
    }

    /**
     * type=9
     * 同步时段组合数据
     *
     * @param oldCiId  被同步的
     * @param newCiId  待同步的
     * @param schoolId
     */
    public void syncTimeCombineData(ObjectId schoolId, ObjectId oldCiId, ObjectId newCiId) throws Exception {
        List<N33_TimeCombineEntry> result = new ArrayList<N33_TimeCombineEntry>();
        try {
            logger.info("同步时段组合数据-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            List<N33_TimeCombineEntry> combineEntries = combineDao.getTimeCombineByCiId(oldCiId,schoolId);
            Map<ObjectId, ObjectId> sysnMap = sysnDao.getSysnEntry(newCiId, schoolId);
            for (N33_TimeCombineEntry combineEntry : combineEntries) {
                List<N33_TimeCombineEntry.ZuHeList> zuHeLists = new ArrayList<N33_TimeCombineEntry.ZuHeList>();
                for (N33_TimeCombineEntry.ZuHeList zuHeList : combineEntry.getZuHeList()) {
                    N33_TimeCombineEntry.ZuHeList newZuHeList = new N33_TimeCombineEntry.ZuHeList();
                    newZuHeList.setSerial(zuHeList.getSerial());
                    newZuHeList.setJxbId(sysnMap.get(zuHeList.getJxbId()));
                    newZuHeList.setHeadName(zuHeList.getHeadName());
                    zuHeLists.add(newZuHeList);
                }
                combineEntry.setTagId(sysnMap.get(combineEntry.getTagId()));
                combineEntry.setZuHeList(zuHeLists);
                combineEntry.setCiId(newCiId);
                combineEntry.setID(new ObjectId());
                result.add(combineEntry);
            }
            combineDao.removeByCiId(newCiId, schoolId);
            if (result.size() > 0) {
                combineDao.addTimeCombineList(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步时段组合数据出错", e);
            throw e;
        }
    }

    public Map<String, Object> getStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> status = (Map<String, Object>) session.getAttribute("status");
        if (status != null && status.get("end") != null && (Integer) status.get("end") == 1) {
            session.removeAttribute("status");
        }
        return status;
    }

    public Map<String, Object> getSynRecord(ObjectId ciId) {
        Map<String, Object> result = new HashMap<String, Object>();
        N33_SynRecordEntry recordEntry = n33_synRecordDao.getRecord(ciId);
        result.put("list", recordEntry != null ? recordEntry.getRecords() : null);
        result.put("old", recordEntry != null ? recordEntry.getOldCiId().toString() : null);
        return result;
    }

    /**
     * 同步教室数据 type=1
     *
     * @param oldCiId  被同步的
     * @param newCiId  待同步的
     * @param schoolId
     */
    public void syncClassRommData(ObjectId oldCiId, ObjectId newCiId, ObjectId schoolId) throws Exception {
        List<N33_ClassroomEntry> result = new ArrayList<N33_ClassroomEntry>();
        try {
            logger.info("同步教室数据-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            List<N33_ClassroomEntry> classroomEntryList = n33_classroomService.getRoomListBySchoolId(schoolId, oldCiId);
            for (N33_ClassroomEntry classroomEntry : classroomEntryList) {
                classroomEntry.setXQId(newCiId);
                classroomEntry.setID(new ObjectId());
                result.add(classroomEntry);
            }
            n33_classroomDao.removeByCiId(newCiId);
            n33_classroomDao.add(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步教室数据出错", e);
            throw e;
        }
    }

    /**
     * type=2
     *
     * @param oldCiId
     * @param newCiId
     * @param schoolId
     */
    public void syncCourseRangeData(ObjectId oldCiId, ObjectId newCiId, ObjectId schoolId) throws Exception {
        List<CourseRangeEntry> result = new ArrayList<CourseRangeEntry>();
        try {
            logger.info("同步课表结构-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            List<CourseRangeEntry> entryList = courseRangeDao.getEntryListBySchoolId(schoolId, oldCiId);
            for (CourseRangeEntry entry : entryList) {
                entry.setXqId(newCiId);
                entry.setID(new ObjectId());
                result.add(entry);
            }
            courseRangeDao.removeEntryByXq(schoolId, newCiId);
            if (result.size() > 0) {
                courseRangeDao.add(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步课表结构数据出错", e);
            throw e;
        }

    }

    /**
     * type=3
     *
     * @param oldCiId
     * @param newCiId
     */
    public void syncTeaData(ObjectId oldCiId, ObjectId newCiId) throws Exception {
        try {
            logger.info("同步老师表结构-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            userService.IsolateTeaListByNewCi(oldCiId, newCiId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步老师表结构数据出错", e);
            throw e;
        }

    }

    /**
     * type=4
     *
     * @param oldCiId
     * @param newCiId
     */
    public void syncSubData(ObjectId oldCiId, ObjectId newCiId) throws Exception {
        try {
            logger.info("同步课时结构-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            subjectService.IsolateSubListByNewCi(oldCiId, newCiId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步课时结构数据出错", e);
            throw e;
        }

    }


    /**
     * type=6
     * 同步班级数据
     *
     * @param oldCiId  被同步的
     * @param newCiId  待同步的
     * @param schoolId
     */
    public void syncClassData(ObjectId oldCiId, ObjectId newCiId, ObjectId schoolId) throws Exception {
        List<ClassEntry> result = new ArrayList<ClassEntry>();
        try {
            logger.info("同步班级数据-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            List<ClassEntry> classEntryList = classService.getClassEntryByCiId(schoolId, oldCiId);
            for (ClassEntry classEntry : classEntryList) {
                classEntry.setXQId(newCiId);
                classEntry.setID(new ObjectId());
                result.add(classEntry);
            }
            classDao.removeByCiId(schoolId, newCiId);
            if (result.size() > 0) {
                classDao.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步班级数据出错", e);
            throw e;
        }
    }

    /**
     * type=7
     * 同步走班时间数据
     *
     * @param oldCiId  被同步的
     * @param newCiId  待同步的
     * @param schoolId
     */
    public void syncZouBanTimeData(ObjectId oldCiId, ObjectId newCiId, ObjectId schoolId) throws Exception {
        List<ZouBanTimeEntry> result = new ArrayList<ZouBanTimeEntry>();
        try {
            logger.info("同步走班时间数据-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            List<ZouBanTimeEntry> zouBanTimeEntryList = courseRangeService.getZouBanTimeForEntry(schoolId, oldCiId);
            for (ZouBanTimeEntry zouBanTimeEntry : zouBanTimeEntryList) {
                zouBanTimeEntry.setXqid(newCiId);
                zouBanTimeEntry.setID(new ObjectId());
                result.add(zouBanTimeEntry);
            }
            timeDao.removeByCiId(schoolId, newCiId);
            if (result.size() > 0) {
                timeDao.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步走班时间数据出错", e);
            throw e;
        }
    }

    /**
     * type=8
     * 同步年级数据
     *
     * @param oldCiId  被同步的
     * @param newCiId  待同步的
     * @param schoolId
     */
    public void syncGradeData(ObjectId oldCiId, ObjectId newCiId, ObjectId schoolId) throws Exception {
        List<Grade> result = new ArrayList<Grade>();
        try {
            logger.info("同步年级数据-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            List<Grade> gradeEntryList = gradeService.getGradeListByCidForEntryS(oldCiId, schoolId);
            for (Grade grade : gradeEntryList) {
                grade.setXQId(newCiId);
                grade.setID(new ObjectId());
                result.add(grade);
            }
            gradeDao.removeByCiId(schoolId, newCiId);
            if (result.size() > 0) {
                gradeDao.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步年级数据出错", e);
            throw e;
        }
    }

    /**
     * type=9
     * 同步学生数据
     *
     * @param oldCiId  被同步的
     * @param newCiId  待同步的
     * @param schoolId
     */
    public void syncStudentData(ObjectId oldCiId, ObjectId newCiId, ObjectId schoolId) throws Exception {
        List<StudentEntry> result = new ArrayList<StudentEntry>();
        try {
            logger.info("同步学生数据-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            List<StudentEntry> studentEntryList = studentService.getStudentListByCidForEntry(oldCiId, schoolId);
            for (StudentEntry student : studentEntryList) {
                student.setXqid(newCiId);
                student.setID(new ObjectId());
                result.add(student);
            }
            studentDao.removeByCiId(schoolId, newCiId);
            if (result.size() > 0) {
                studentDao.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步学生数据出错", e);
            throw e;
        }
    }

    /**
     * type=10
     * 同步排课数据
     *
     * @param oldCiId
     * @param newCiId
     * @param schoolId
     */
    private void syncPaiKeData(ObjectId oldCiId, ObjectId newCiId, ObjectId schoolId) {
        List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(newCiId, schoolId);
        if (ykbEntryList != null && ykbEntryList.size() != 0) {
            ykbDao.deleteN33_YKB(newCiId, schoolId);
        }
        List<N33_JXBKSEntry> jxbksEntries = jxbksDao.getJXBKSsByCiId(newCiId, schoolId);
        if (jxbksEntries != null && jxbksEntries.size() != 0) {
            jxbksDao.deleteN33_JXBKS(newCiId, schoolId);
        }
        Map<ObjectId, ObjectId> sysnMap = sysnDao.getSysnEntry(newCiId, schoolId);
        List<N33_YKBEntry> ykbEntryList3 = new ArrayList<N33_YKBEntry>();
        List<N33_YKBEntry> ykbEntryList2 = ykbDao.getYKBEntrysList(oldCiId, schoolId);
        if (ykbEntryList2 != null && ykbEntryList2.size() != 0) {
            for (N33_YKBEntry entry : ykbEntryList2) {
                entry.setJxbId(sysnMap.get(entry.getJxbId()));
                entry.setNJxbId(sysnMap.get(entry.getNJxbId()));
                entry.setTermId(newCiId);
                entry.setID(new ObjectId());
                ykbEntryList3.add(entry);
            }
            ykbDao.addN33_YKBEntrys(ykbEntryList3);
        }

        List<N33_JXBKSEntry> jxbksEntries3 = new ArrayList<N33_JXBKSEntry>();
        List<N33_JXBKSEntry> jxbksEntries2 = jxbksDao.getJXBKSsByCiId(oldCiId, schoolId);
        if (jxbksEntries2 != null && jxbksEntries2.size() != 0) {
            for (N33_JXBKSEntry entry : jxbksEntries2) {
                entry.setJxbId(sysnMap.get(entry.getJxbId()));
                entry.setTermId(newCiId);
                entry.setID(new ObjectId());
                jxbksEntries3.add(entry);
            }
            jxbksDao.addN33_JXBKSEntrys(jxbksEntries3);
        }
    }

    public void syncJXBData(ObjectId oldCiId, ObjectId newCiId, ObjectId sid, Integer xuNiSys) {
        logger.info("同步教学班结构-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
        //指定的次的id
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(oldCiId);
        //清楚同步的数据
        jxbDao.delN33_JXBEntryCiId(newCiId);
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            jxbEntry.setID(judgeId(newCiId, jxbEntry.getID(), sid));
            jxbEntry.setTermId(newCiId);
        }
        if (jxbEntries.size() > 0) {
            jxbDao.addN33_JXBEntrys(jxbEntries);
        }
        n33_fenBanInfoSetService.detactConfliction(sid, newCiId);// 生成教师教室冲突
        //学生标签
        //指定的次的id
        List<N33_StudentTagEntry> tagEntries = tagDao.IsolateN33_JXBEntryByNewCi(oldCiId);
        //清楚同步的数据
        tagDao.delN33_JXBEntryCid(newCiId);
        for (N33_StudentTagEntry tagEntry : tagEntries) {
            tagEntry.setID(judgeId(newCiId, tagEntry.getID(), sid));
            tagEntry.setXqId(newCiId);
            if (xuNiSys == 1) {
                tagEntry.setXuNi(0);
            }
        }
        if (tagEntries.size() > 0) {
            tagDao.add(tagEntries);
        }
        zhuanXiangDao.del(newCiId);
        List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(oldCiId);
        for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntries) {
            zhuanXiangEntry.setID(judgeId(newCiId, zhuanXiangEntry.getID(), sid));
            zhuanXiangEntry.setCiId(newCiId);
            zhuanXiangEntry.setJxbId(judgeId(newCiId, zhuanXiangEntry.getJxbId(), sid));
        }
        if (zhuanXiangEntries.size() > 0) {
            zhuanXiangDao.add(zhuanXiangEntries);
        }
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            //单双周
            ObjectId relativeId = jxbEntry.getRelativeId();
            if (relativeId != null) {
                jxbEntry.setRelativeId(judgeId(newCiId, relativeId, sid));
            }
            List<ObjectId> tagIds = new ArrayList<ObjectId>();
            for (ObjectId tagId : jxbEntry.getTagIds()) {
                tagIds.add(judgeId(newCiId, tagId, sid));
            }
            jxbEntry.setTagIds(tagIds);
            jxbDao.updateN33_JXB(jxbEntry);
        }
        for (N33_StudentTagEntry tagEntry : tagEntries) {
            List<ObjectId> jxbIds = new ArrayList<ObjectId>();
            for (ObjectId jxbId : tagEntry.getJxbIds()) {
                jxbIds.add(judgeId(newCiId, jxbId, sid));
            }
            tagEntry.setJxbIds(jxbIds);
            tagDao.update(tagEntry);
        }
        //排课的年级
        //学生冲突
        TermEntry termEntry1 = termDao.getTermByTimeId(newCiId);
        for (TermEntry.PaiKeTimes paiKeTimes : termEntry1.getPaiKeTimes()) {
            if (paiKeTimes.getID().toString().equals(newCiId.toString())) {
                //创建年级
                for (ObjectId grade : paiKeTimes.getGradeIds()) {
                    tagService.conflictDetection(sid, grade, newCiId);
                }
            }
        }
    }

    public void syncVirtualClassData(ObjectId schoolId, ObjectId oldCiId, ObjectId newCiId) throws Exception{
        List<N33_XuNiBanEntry> result = new ArrayList<N33_XuNiBanEntry>();
        try {
            logger.info("同步虚拟班数据-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            virtualClassDao.delN33_VirtualClassEntryByXqid(newCiId,schoolId);
            List<N33_XuNiBanEntry> list = virtualClassDao.getN33_VirtualClassEntryByXqid(oldCiId,schoolId);

            for (N33_XuNiBanEntry xuNiBanEntry:list) {
                List<ObjectId> jxbIds = xuNiBanEntry.getJxbIds();
                List<ObjectId> newJxbIds = new ArrayList<ObjectId>();
                if(jxbIds != null && jxbIds.size() > 0){
                    for (ObjectId ids : jxbIds) {
                        newJxbIds.add(judgeId(newCiId,ids,schoolId));
                    }
                }
                xuNiBanEntry.setJxbIds(newJxbIds);
                xuNiBanEntry.setID(new ObjectId());
                List<N33_XuNiBanEntry.StudentTag> studentTags = xuNiBanEntry.getTagList();
                for (N33_XuNiBanEntry.StudentTag studentTag : studentTags) {
                    studentTag.setTagId(judgeId(newCiId,studentTag.getTagId(),schoolId));
                }
                xuNiBanEntry.setTagList(studentTags);
                xuNiBanEntry.setXqId(newCiId);
                result.add(xuNiBanEntry);
            }
            if (result.size() > 0) {
                virtualClassDao.addN33_VirtualClassEntry(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步虚拟班数据出错", e);
            throw e;
        }
    }

    /**
     * 同步教学日数据
     *
     * @param oldCiId  被同步的
     * @param newCiId  待同步的
     * @param schoolId
     */
    public void syncGradeWeekData(ObjectId oldCiId, ObjectId newCiId, ObjectId schoolId) throws Exception {
        List<N33_GradeWeekRangeEntry> result = new ArrayList<N33_GradeWeekRangeEntry>();
        try {
            logger.info("同步教学日数据-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            List<N33_GradeWeekRangeEntry> list = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(oldCiId, schoolId);
            for (N33_GradeWeekRangeEntry gradeWeekRangeEntry : list) {
                gradeWeekRangeEntry.setXQId(newCiId);
                gradeWeekRangeEntry.setID(new ObjectId());
                result.add(gradeWeekRangeEntry);
            }
            n33_gradeWeekRangeDao.removeByCiId(schoolId, newCiId);
            if (result.size() > 0) {
                n33_gradeWeekRangeDao.addGradeWeekRangeEntryList(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步教学日数据出错", e);
            throw e;
        }
    }

    /**
     * 保存同步教学班时候的对应关系
     *
     * @param ciId 同步时传入的新的次id
     * @param oId  同步时传入的被同步的_id字段
     * @param sid
     */
    public ObjectId judgeId(ObjectId ciId, ObjectId oId, ObjectId sid) {
        N33_SysnEntry entry = sysnDao.getSysnEntry(ciId, oId, sid);
        if (entry != null) {
            return entry.getNId();
        } else {
            N33_SysnEntry entry1 = new N33_SysnEntry();
            entry1.setCiId(ciId);
            entry1.setOId(oId);
            entry1.setSid(sid);
            entry1.setNId(new ObjectId());
            sysnDao.saveSysnEntry(entry1);
            return entry1.getNId();
        }
    }

    /**
     * 同步选科组合
     * type=12
     *
     * @param oldCiId
     * @param newCiId
     */
    public void syncXuanKeZuHe(ObjectId sid, ObjectId oldCiId, ObjectId newCiId) throws Exception {
        try {
            logger.info("同步选科组合-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            schoolSelectLessonSetService.syncXuanKeZuHe(sid, oldCiId, newCiId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步选科组合数据出错", e);
            throw e;
        }

    }

    /**
     * 同步自习班
     * type=13
     *
     * @param oldCiId
     * @param newCiId
     */
    public void syncZiXiKe(ObjectId sid, ObjectId oldCiId, ObjectId newCiId) throws Exception {
        try {
            logger.info("同步自习班-oldCiId:" + oldCiId + "-newCiId:" + newCiId);
            List<N33_ZIXIKEEntry> list = n33_zixikeDao.getJXBsBySubId(oldCiId);
            List<N33_ZIXIKEEntry> waitForAdd = new ArrayList<N33_ZIXIKEEntry>();
            n33_zixikeDao.delN33_ZIXIKEEntryCid(newCiId);
            for (N33_ZIXIKEEntry zixikeEntry : list) {
                ObjectId oldId = zixikeEntry.getID();
                ObjectId newId = judgeId(newCiId, oldId, sid);
                zixikeEntry.setID(newId);
                zixikeEntry.setTermId(newCiId);
                waitForAdd.add(zixikeEntry);
            }
            if (waitForAdd.size() != 0) {
                n33_zixikeDao.addN33_ZIXIKEEntrys(waitForAdd);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步自习班数据出错", e);
            throw e;
        }

    }
}
