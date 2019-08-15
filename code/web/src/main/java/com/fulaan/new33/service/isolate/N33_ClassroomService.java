package com.fulaan.new33.service.isolate;

import java.util.*;

import com.db.new33.isolate.*;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZhuanXiangDao;
import com.pojo.new33.isolate.*;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.new33.paike.N33_ZhuanXiangEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fulaan.new33.dto.isolate.N33_ClassroomDTO;
import com.fulaan.schoolbase.dto.ClassRoomDTO;
import com.fulaan.schoolbase.dto.SchoolLoopDTO;
import com.fulaan.schoolbase.service.ClassRoomService;

@Service
public class N33_ClassroomService extends BaseService {

    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();

    private N33_JXBDao n33_jxbDao = new N33_JXBDao();
    private SubjectDao subjectDao = new SubjectDao();
    private N33_YKBDao n33_ykbDao = new N33_YKBDao();
    private GradeDao gradeDao = new GradeDao();
    private N33_ClassDao n33_classDao = new N33_ClassDao();
    public N33_ZhuanXiangDao n33_ZhuanXiangDao = new N33_ZhuanXiangDao();

    private TermDao termDao = new TermDao();

    private ClassRoomService classRoomService = new ClassRoomService();

    private N33_TurnOffService turnOffService = new N33_TurnOffService();

    public List<N33_ClassroomDTO> getListByXqGrade(ObjectId schoolId, String xqid, String gradeId) {
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(new ObjectId(gradeId), new ObjectId(xqid));
        Map<ObjectId, ClassEntry> classId_class = n33_classDao.findClassEntryMap(new ObjectId(gradeId), new ObjectId(xqid));
        List<N33_ClassroomDTO> result = new ArrayList<N33_ClassroomDTO>();
        Map<String, List<N33_YKBEntry>> roomId_ykb = n33_ykbDao.getYKBMapByRoomId(new ObjectId(xqid), schoolId, new ObjectId(gradeId));
        List<N33_ClassroomEntry> entryList = classroomDao.getRoomEntryListByXqGradeIso(new ObjectId(xqid), schoolId, new ObjectId(gradeId));
        Map<ObjectId,N33_ClassroomEntry> classroomEntryMap = new HashMap<ObjectId, N33_ClassroomEntry>();
        if (entryList!=null && entryList.size()!=0) {
            for (N33_ClassroomEntry entry : entryList) {
                classroomEntryMap.put(entry.getRoomId(),entry);
            }
        }
        List<ClassRoomDTO> classRoomDTOS = getRoomListBySchoolId2(schoolId);
        if (classRoomDTOS!=null && classRoomDTOS.size()!=0) {
            for (ClassRoomDTO dto : classRoomDTOS) {
                N33_ClassroomEntry entry = classroomEntryMap.get(new ObjectId(dto.getId()));
                if (entry!=null) {
                    if (!entry.getRoomName().equals(dto.getNumber())) {
                        classroomDao.updateRoomNameId(entry.getID(),dto.getNumber());
                    }
                }
            }
        }
        entryList = classroomDao.getRoomEntryListByXqGradeIso(new ObjectId(xqid), schoolId, new ObjectId(gradeId));
        int acClassType = turnOffService.getAcClassType(schoolId, new ObjectId(gradeId), new ObjectId(xqid),1);

        for (N33_ClassroomEntry entry : entryList) {
            Integer ksNum = 0;
            List<N33_JXBEntry> jxbEntryList = n33_jxbDao.getJxbListByRoomId(new ObjectId(gradeId), entry.getRoomId(), new ObjectId(xqid), acClassType);
            Integer danshuangNum = 0;
            for (N33_JXBEntry jxbEntry : jxbEntryList) {
                if (jxbEntry.getType() == 6) {
                    danshuangNum += jxbEntry.getJXBKS();
                } else {
                    ksNum += jxbEntry.getJXBKS();
                }
            }
            ksNum += danshuangNum / 2;
            List<N33_ZhuanXiangEntry> zhuanxiangke = n33_ZhuanXiangDao.findN33_ZhuanXiangEntryByRoomId(entry.getRoomId(), new ObjectId(xqid));
            Set<ObjectId> zhuanxiangJxbIds = new HashSet<ObjectId>();
            for (N33_ZhuanXiangEntry zhuanxiang : zhuanxiangke) {
                ObjectId jxbid = zhuanxiang.getJxbId();
                zhuanxiangJxbIds.add(jxbid);
            }
            for (ObjectId zhuanxiangJxbId : zhuanxiangJxbIds) {
                N33_JXBEntry jxbEntry = n33_jxbDao.getJXBById(zhuanxiangJxbId);
                if (jxbEntry != null) {
                    ksNum += jxbEntry.getJXBKS();
                }
            }
            N33_ClassroomDTO dto = new N33_ClassroomDTO(entry);
            if (entry.getClassId() != null) {
                if (classId_class.get(entry.getClassId()) != null) {
                    dto.setClassNumber(classId_class.get(entry.getClassId()).getXh().toString());
                    if (grade != null) {
                        dto.setGradeName(grade.getName());
                    }
                } else {
                    dto.setClassNumber("");
                }
            }
            dto.setClassTime(ksNum);
            List<N33_YKBEntry> ykbEntryList = roomId_ykb.get(entry.getRoomId().toString());
            if (ykbEntryList != null) {
                Integer size = 0;
                for (N33_YKBEntry ykbEntry : ykbEntryList) {
                    if (ykbEntry.getType() != 5 && ykbEntry.getJxbId() != null) {
                        size++;
                    }
                }
                dto.setArrangedTime(size);
            } else {
                dto.setArrangedTime(0);
            }
            result.add(dto);
        }
        Collections.sort(result, new Comparator<N33_ClassroomDTO>() {
            @Override
            public int compare(N33_ClassroomDTO o1, N33_ClassroomDTO o2) {
                return o1.getRoomName().hashCode() - o2.getRoomName().hashCode();
            }
        });
        return result;
    }

    public void saveClassRoom(N33_ClassroomDTO dto) {
        classroomDao.save(dto.buildEntry());
    }

    public void removeClassRoom(String id) {
        classroomDao.remove(new ObjectId(id));
    }

    /**
     * @param schoolId
     * @param xqid
     * @param gradeId
     * @param dtos
     */
    public void updateClassRoomList(ObjectId schoolId, String xqid, String gradeId, @RequestBody List<N33_ClassroomDTO> dtos) {
        List<N33_ClassroomEntry> entryList = classroomDao.getRoomEntryListByXqGradeIso(new ObjectId(xqid), schoolId, new ObjectId(gradeId));
        if (entryList != null && !entryList.isEmpty()) {
            List<String> ids = new ArrayList<String>();
            Map<String, N33_ClassroomDTO> roomid_dto = new HashMap<String, N33_ClassroomDTO>();
            for (N33_ClassroomDTO dto : dtos) {
                ids.add(dto.getRoomId());
                dto.setSchoolId(schoolId.toString());
                roomid_dto.put(dto.getRoomId(), dto);
            }
            Map<String, N33_ClassroomEntry> roomid_entry = new HashMap<String, N33_ClassroomEntry>();
            List<String> roomIds = new ArrayList<String>();
            for (N33_ClassroomEntry entry : entryList) {
                roomid_entry.put(entry.getRoomId().toString(), entry);
                roomIds.add(entry.getRoomId().toString());
            }
            List<String> differnce1 = new ArrayList<String>();
            differnce1.addAll(ids);
            differnce1.removeAll(roomIds);// 待添加的差集
            if (!differnce1.isEmpty()) {
                List<N33_ClassroomEntry> waitAddlist = new ArrayList<N33_ClassroomEntry>();
                for (String id : differnce1) {
                    waitAddlist.add(roomid_dto.get(id).buildEntry());
                }
                classroomDao.add(waitAddlist);
            }


            List<String> differnce2 = new ArrayList<String>();
            differnce2.addAll(roomIds);
            differnce2.removeAll(ids);// 待删除的差集
            if (!differnce2.isEmpty()) {
                for (String id : differnce2) {
                    classroomDao.remove(roomid_entry.get(id).getID());
                }

            }

        } else {// 纯添加
            List<N33_ClassroomEntry> waitAdd = new ArrayList<N33_ClassroomEntry>();
            for (N33_ClassroomDTO dto : dtos) {
                dto.setSchoolId(schoolId.toString());
                N33_ClassroomEntry entry = dto.buildEntry();
                entry.setArrange(1);
                waitAdd.add(entry);
            }
            classroomDao.add(waitAdd);
        }
    }

    public Map<String, List<ClassRoomDTO>> getRoomListBySchoolId(ObjectId schoolId) {
        Map<String, List<ClassRoomDTO>> result = new HashMap<String, List<ClassRoomDTO>>();
        List<SchoolLoopDTO> schoolLoopDTOs = classRoomService.getSchoolLoopList(schoolId);
        Collection<ClassRoomDTO> list = classRoomService.getClassRoomListByID(schoolId).values();
        for (ClassRoomDTO dto : list) {
            List<ClassRoomDTO> tempList = result.get(dto.getLoopId()) == null ? new ArrayList<ClassRoomDTO>() : result.get(dto.getLoopId());
            tempList.add(dto);
            result.put(dto.getLoopId(), tempList);
        }
        return result;
    }

    public List<ClassRoomDTO> getRoomListBySchoolId2(ObjectId schoolId) {
        List<ClassRoomDTO> result = new ArrayList<ClassRoomDTO>();
        Collection<ClassRoomDTO> list = classRoomService.getClassRoomListByID(schoolId).values();
        for (ClassRoomDTO dto : list) {
            result.add(dto);
        }
        return result;
    }


    public List<N33_ClassroomEntry> getRoomListBySchoolId(ObjectId schoolId, ObjectId xqid) {
        List<N33_ClassroomEntry> entryList = classroomDao.getRoomEntryListByXqGrade(xqid, schoolId);
        return entryList;
    }

    public List<N33_ClassroomEntry> getRoomListBySchoolId(ObjectId schoolId, List<ObjectId> xqid) {
        List<N33_ClassroomEntry> entryList = classroomDao.getRoomEntryListByXqGrade(xqid, schoolId);
        return entryList;
    }

    public void updateClassNameId(String id, String className, String classId) {
        if("".equals(classId)){
            classroomDao.updateClassNameId(new ObjectId(id), null, null);
        }else{
            classroomDao.updateClassNameId(new ObjectId(id), className, new ObjectId(classId));
        }
    }

    public void updateDesc(String id, String description, Integer cap, Integer type) {
        classroomDao.updateDesc(new ObjectId(id), description, cap, type);
    }

    public void updateArrange(String id, Integer arranged) {
        classroomDao.updateArrange(new ObjectId(id), arranged);
    }

    /**
     * 查询教室
     *
     * @param xqid
     * @param sid
     * @param gradeId
     * @param arrange 1可以排课的，2未安排的
     * @return
     */
    public List<N33_ClassroomDTO> getRoomEntryListByXqGrade(ObjectId xqid, ObjectId sid, ObjectId gradeId, Integer arrange) {
        List<N33_ClassroomDTO> result = new ArrayList<N33_ClassroomDTO>();
        List<N33_ClassroomEntry> entryList = classroomDao.getRoomEntryListByXqGrade(xqid, sid, gradeId, arrange);
        if (entryList != null) {
            for (N33_ClassroomEntry entry : entryList) {
                result.add(new N33_ClassroomDTO(entry));
            }
        }
        Collections.sort(result, new Comparator<N33_ClassroomDTO>() {
            @Override
            public int compare(N33_ClassroomDTO o1, N33_ClassroomDTO o2) {
                return o1.getRoomName().hashCode() - o2.getRoomName().hashCode();
            }
        });
        return result;
    }

    public List<N33_ClassroomDTO> getRoomEntryListByXq(ObjectId xqid, ObjectId sid, Integer arrange) {
        List<N33_ClassroomDTO> result = new ArrayList<N33_ClassroomDTO>();
        List<N33_ClassroomEntry> entryList = classroomDao.getRoomEntryListByXq(xqid, sid, arrange);
        if (entryList != null) {
            for (N33_ClassroomEntry entry : entryList) {
                result.add(new N33_ClassroomDTO(entry));
            }
        }
        Collections.sort(result, new Comparator<N33_ClassroomDTO>() {
            @Override
            public int compare(N33_ClassroomDTO o1, N33_ClassroomDTO o2) {
                return o1.getRoomName().hashCode() - o2.getRoomName().hashCode();
            }
        });
        return result;
    }

    /**
     * 查询所有次的排课教室（供查看课表使用）
     *
     * @param xqid
     * @param sid
     * @param gradeId
     * @param arrange 1可以排课的，2未安排的
     * @return
     */
    public List<N33_ClassroomDTO> getRoomEntryListByXqGradeAndTerm(ObjectId xqid, ObjectId sid, ObjectId gradeId, Integer arrange) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> ciIds = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                ciIds.add(paiKeTimes.getID());
            }
        }
        List<N33_ClassroomEntry> entryList = classroomDao.getIsolateClassRoomEntrys(ciIds, sid, gradeId, arrange);
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<N33_ClassroomDTO> result = new ArrayList<N33_ClassroomDTO>();
        if (entryList != null) {
            for (N33_ClassroomEntry entry : entryList) {
                if (!ids.contains(entry.getRoomId())) {
                    ids.add(entry.getRoomId());
                    result.add(new N33_ClassroomDTO(entry));
                }
            }
        }
        Collections.sort(result, new Comparator<N33_ClassroomDTO>() {
            @Override
            public int compare(N33_ClassroomDTO o1, N33_ClassroomDTO o2) {
                return o1.getRoomName().hashCode() - o2.getRoomName().hashCode();
            }
        });
        return result;
    }

    public List<N33_ClassroomDTO> getRoomEntryListByXqGradeAndCiId(ObjectId xqid, ObjectId sid, ObjectId gradeId, Integer arrange) {
        List<ObjectId> ciIds = new ArrayList<ObjectId>();
        ciIds.add(xqid);
        List<N33_ClassroomEntry> entryList = classroomDao.getIsolateClassRoomEntrys(ciIds, sid, gradeId, arrange);
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<N33_ClassroomDTO> result = new ArrayList<N33_ClassroomDTO>();
        if (entryList != null) {
            for (N33_ClassroomEntry entry : entryList) {
                if (!ids.contains(entry.getRoomId())) {
                    ids.add(entry.getRoomId());
                    result.add(new N33_ClassroomDTO(entry));
                }
            }
        }
        return result;
    }

    /**
     * 查询所有次的排课教室（供查看课表使用）
     *
     * @param xqid
     * @param sid
     * @param arrange 1可以排课的，2未安排的
     * @return
     */
    public List<N33_ClassroomDTO> getRoomEntryListByXqGradeAndTermAllGrade(ObjectId xqid, ObjectId sid, Integer arrange) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> ciIds = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                ciIds.add(paiKeTimes.getID());
            }
        }
        List<N33_ClassroomEntry> entryList = classroomDao.getIsolateClassRoomEntrysAllGrade(ciIds, sid, arrange);
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<N33_ClassroomDTO> result = new ArrayList<N33_ClassroomDTO>();
        if (entryList != null) {
            for (N33_ClassroomEntry entry : entryList) {
                if (!ids.contains(entry.getRoomId())) {
                    ids.add(entry.getRoomId());
                    result.add(new N33_ClassroomDTO(entry));
                }
            }
        }
        return result;
    }

    /**
     * 根据roomId集合查询教室
     *
     * @param xqid
     * @param sid
     * @param gradeId
     * @param arrange 1可以排课的，2未安排的
     * @return
     */
    public List<N33_ClassroomDTO> getRoomEntryListByXqGradeAndRoomIds(List<ObjectId> roomIds, ObjectId xqid, ObjectId sid, ObjectId gradeId, Integer arrange) {
        List<N33_ClassroomDTO> result = new ArrayList<N33_ClassroomDTO>();
        List<N33_ClassroomEntry> entryList = classroomDao.getRoomEntryListByXqGradeAndRoomIds(roomIds, xqid, sid, gradeId, arrange);
        if (entryList != null) {
            for (N33_ClassroomEntry entry : entryList) {
                result.add(new N33_ClassroomDTO(entry));
            }
        }
        Collections.sort(result, new Comparator<N33_ClassroomDTO>() {
            @Override
            public int compare(N33_ClassroomDTO o1, N33_ClassroomDTO o2) {
                return o1.getRoomName().hashCode() - o2.getRoomName().hashCode();
            }
        });
        return result;
    }

    /**
     * @param schoolId
     * @param xqid
     * @return
     */
    public Map<ObjectId, N33_ClassroomEntry> getRoomListMapBySchoolId(ObjectId schoolId, ObjectId xqid) {
        Map<ObjectId, N33_ClassroomEntry> classroomEntryMap = new HashMap<ObjectId, N33_ClassroomEntry>();
        List<N33_ClassroomEntry> entryList = classroomDao.getRoomEntryListByXqGrade(xqid, schoolId);
        if (entryList != null && entryList.size() != 0) {
            for (N33_ClassroomEntry entry : entryList) {
                classroomEntryMap.put(entry.getRoomId(), entry);
            }
        }
        return classroomEntryMap;
    }
}
