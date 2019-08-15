package com.fulaan.new33.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.isolate.N33_ClassroomDao;
import com.db.new33.paike.N33_JXBCTDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_JXBKSDao;
import com.db.new33.paike.N33_SWDao;
import com.fulaan.new33.dto.isolate.CourseRangeDTO;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.CourseRangeService;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.new33.paike.N33_JXBCTEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.utils.MongoUtils;

/**
 * Created by wang_xinxin on 2018/4/24.
 */
@Service
@SuppressWarnings("all")
public class N33_AutoPaiKeService extends BaseService{

    private N33_JXBDao jxbDao = new N33_JXBDao();

    private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();

    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();

    private IsolateSubjectService subjectService = new IsolateSubjectService();

    private CourseRangeService courseRangeService = new CourseRangeService();

    private N33_SWDao swDao = new N33_SWDao();

    private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();
    
    @Autowired
    private N33_TurnOffService turnOffService;


    /**
     * 教学班信息完备性检查
     *
     * @param schoolId
     * @param defauleTermId
     * @param gradeId
     * @return
     */
    public int checkJXBInfo(ObjectId schoolId, ObjectId defauleTermId, String gradeId) {
        int resultCode = 0;
        //jxb add
        ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
        int acClassType = turnOffService.getAcClassType(schoolId, new ObjectId(gradeId), ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(schoolId, new ObjectId(gradeId), defauleTermId, acClassType);
        if (jxbEntryList != null && jxbEntryList.size() != 0) {
            for (N33_JXBEntry jxbEntry : jxbEntryList) {
                if (jxbEntry.getClassroomId() == null) {
                    resultCode = 1;
                    break;
                }
                if (jxbEntry.getStudentIds() == null || jxbEntry.getStudentIds().size() == 0) {
                    resultCode = 2;
                    break;
                }
                if (jxbEntry.getSubjectId() == null) {
                    resultCode = 3;
                    break;
                }
                if (jxbEntry.getTercherId() == null) {
                    resultCode = 4;
                    break;
                }
            }
        } else {
            resultCode = 5;
        }
        return resultCode;
    }

    /**
     * 教室/教师的课时合理性
     *
     * @param jxbEntryList
     * @param schoolId
     * @param termId
     * @param gradeId
     * @return
     */
    public Map<Boolean, String> rationJXBs(List<N33_JXBEntry> jxbEntryList, ObjectId schoolId, ObjectId termId, ObjectId gradeId) {
        Map<Boolean, String> resultMap = new HashMap<Boolean, String>();
        List<N33_ClassroomEntry> classroomEntryList = classroomDao.getRoomEntryListByXqGrade(termId, schoolId, gradeId);
        List<N33_KSDTO> ksdtos = subjectService.getIsolateSubjectByGradeId(termId, schoolId, gradeId.toString());
        Map<String, N33_KSDTO> ksdtoMap = new HashMap<String, N33_KSDTO>();
        if (ksdtos != null && ksdtos.size() != 0) {
            for (N33_KSDTO dto : ksdtos) {
                ksdtoMap.put(dto.getSubid(), dto);
            }
        }
        List<CourseRangeDTO> courseRangeDTOs = courseRangeService.getListBySchoolId(schoolId.toString(), termId);
        if (courseRangeDTOs == null || courseRangeDTOs.size() == 0) {
            resultMap.put(false, "需提前设置课节数!");
            return resultMap;
        }
        if (classroomEntryList != null && classroomEntryList.size() != 0) {
            for (N33_ClassroomEntry entry : classroomEntryList) {
                int count = 0;
                for (N33_JXBEntry jxbEntry : jxbEntryList) {
                    if (entry.getID().equals(jxbEntry.getClassroomId())) {
                        if (ksdtoMap.get(jxbEntry.getSubjectId().toString()) == null) {
                            resultMap.put(false, "需提前设置学科课时数!");
                            return resultMap;
                        } else {
                            count += jxbEntry.getJXBKS();
                        }
                    }
                }
                if (count > courseRangeDTOs.size() * 5) {
                    resultMap.put(false, "教室课时数超过正常课时!");
                    return resultMap;
                }
            }
        }
        resultMap.put(true, "检查合格!");
        return resultMap;
    }

    /**
     * 获取跨头老师列表
     *
     * @param schoolId
     * @param termId
     * @param gradeId
     * @return
     */
    public List<ObjectId> getKuaTouTeacherIds(ObjectId schoolId, ObjectId termId, ObjectId gradeId) {
    	//jxb add
    	ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
    	int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
    	//jxb add
        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(schoolId, termId);
        List<ObjectId> teacherIds = new ArrayList<ObjectId>();
        List<ObjectId> newTeacherIds = new ArrayList<ObjectId>();
        if (jxbEntryList != null && jxbEntryList.size() != 0) {
            for (N33_JXBEntry entry : jxbEntryList) {
                if (entry.getGradeId().equals(gradeId)) {
                    teacherIds.add(entry.getTercherId());
                }
            }
        }
        Map<ObjectId, Integer> teacherGradeMaps = new HashMap<ObjectId, Integer>();
        if (jxbEntryList != null && jxbEntryList.size() != 0) {
            for (ObjectId tid : teacherIds) {
                List<ObjectId> gradeIds = new ArrayList<ObjectId>();
                for (N33_JXBEntry entry : jxbEntryList) {
                    if (tid.equals(entry.getTercherId())) {
                        gradeIds.add(entry.getGradeId());
                    }
                }
                if (gradeIds.size() > 1) {
                    List newList = new ArrayList(new HashSet(gradeIds));
                    teacherGradeMaps.put(tid, newList.size());
                }
            }
            newTeacherIds.addAll(sortMap(teacherGradeMaps));
        }
        return newTeacherIds;
    }

    /**
     * 老师所带教学班
     *
     * @param teacherId
     * @return
     */
    public List<N33_JXBEntry> getJXBsByTeacherId(List<N33_JXBEntry> jxbEntryList, ObjectId teacherId) {
        List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
        if (jxbEntryList != null && jxbEntryList.size() != 0) {
            for (N33_JXBEntry entry : jxbEntryList) {
                if (entry.getTercherId().equals(teacherId)) {
                    jxbEntries.add(entry);
                }
            }
        }
        return jxbEntries;
    }

    /**
     * 按照总课时数排序
     *
     * @param jxbEntryList
     * @param ksdtoMap
     * @param teacherIds
     * @return
     */
    public List<ObjectId> sortTeacherIdsByKS(List<N33_JXBEntry> jxbEntryList, Map<String, N33_KSDTO> ksdtoMap, List<ObjectId> teacherIds) {
        List<ObjectId> newTeacherIds = new ArrayList<ObjectId>();
        Map<ObjectId, Integer> teacherKSMap = new HashMap<ObjectId, Integer>();
        if (jxbEntryList != null && jxbEntryList.size() != 0) {
            for (ObjectId tid : teacherIds) {
                int count = 0;
                for (N33_JXBEntry entry : jxbEntryList) {
                    if (tid.equals(entry.getTercherId())) {
                        count += entry.getJXBKS();
                    }
                }
                teacherKSMap.put(tid, count);
            }
            newTeacherIds.addAll(sortMap(teacherKSMap));
        }
        return newTeacherIds;
    }

    /**
     * 按照总事务数排序
     *
     * @param teacherIds
     * @return
     */
    public List<ObjectId> sortTeacherIdsBySW(ObjectId termId, ObjectId schoolId, List<ObjectId> teacherIds) {
        List<ObjectId> newTeacherIds = new ArrayList<ObjectId>();
        Map<ObjectId, Integer> teacherSWMap = new HashMap<ObjectId, Integer>();
        List<N33_SWEntry> swEntries = swDao.getSwBySchoolId(termId, schoolId);
        if (swEntries != null && swEntries.size() != 0) {
            for (ObjectId tid : teacherIds) {
                int count = 0;
                for (N33_SWEntry entry : swEntries) {
                    if (entry.getTeacherIds().size() == 0) {
                        count++;
                    } else if (entry.getTeacherIds().contains(tid)) {
                        count++;
                    }
                }
                teacherSWMap.put(tid, count);
            }
            newTeacherIds.addAll(sortMap(teacherSWMap));
        }
        return newTeacherIds;
    }

    /**
     * 教学班排序（冲突）
     *
     * @param jxbEntryList
     * @return
     */
    public List<ObjectId> sortJXBConflict(List<N33_JXBEntry> jxbEntryList) {
        List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(jxbEntryList);
        List<N33_JXBCTEntry> jxbctEntries = jxbctDao.getJxbCTByjxbIds(jxbIds);
        Map<ObjectId, Integer> jxbIdMap = new HashMap<ObjectId, Integer>();
        List<ObjectId> newJxbIds = new ArrayList<ObjectId>();
        if (jxbctEntries != null && jxbctEntries.size() != 0) {
            for (ObjectId id : jxbIds) {
                int count = 0;
                for (N33_JXBCTEntry entry : jxbctEntries) {
                    if (id.equals(entry.getJxbId())) {
                        count++;
                    }
                }
                jxbIdMap.put(id, count);
            }
            newJxbIds = sortMap(jxbIdMap);
        }
        return newJxbIds;
    }

    /**
     * map value值排序
     *
     * @param teacherSWMap
     * @return
     */
    private List<ObjectId> sortMap(Map<ObjectId, Integer> teacherSWMap) {
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<Map.Entry<ObjectId, Integer>> list = new ArrayList<Map.Entry<ObjectId, Integer>>(teacherSWMap.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<ObjectId, Integer>>() {
            //降序排序
            @Override
            public int compare(Map.Entry<ObjectId, Integer> o1,
                               Map.Entry<ObjectId, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for (Map.Entry<ObjectId, Integer> mapping : list) {
            ids.add(mapping.getKey());
        }
        return ids;
    }

    /**
     * 教室教学班
     *
     * @param jxbEntryList
     * @param classroomId
     * @return
     */
    public List<N33_JXBEntry> getJXBsByClassRoomId(List<N33_JXBEntry> jxbEntryList, ObjectId classroomId) {
        List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
        if (jxbEntryList != null && jxbEntryList.size() != 0) {
            for (N33_JXBEntry entry : jxbEntryList) {
                if (entry.getClassroomId().equals(classroomId)) {
                    jxbEntries.add(entry);
                }
            }
        }
        return jxbEntries;
    }

}
