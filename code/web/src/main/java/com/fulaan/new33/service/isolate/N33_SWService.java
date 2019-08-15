package com.fulaan.new33.service.isolate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.db.new33.isolate.*;
import com.pojo.new33.isolate.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.new33.paike.N33_SWDao;
import com.db.new33.paike.N33_SWLBDao;
import com.fulaan.new33.dto.isolate.N33_ManagerDTO;
import com.fulaan.new33.dto.isolate.N33_SWDTO;
import com.fulaan.new33.dto.isolate.N33_SWLBDTO;
import com.fulaan.new33.dto.isolate.N33_TeaDTO;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.new33.paike.N33_SWLBEntry;

/**
 * Created by albin on 2018/3/13.
 */
@Service
public class N33_SWService extends BaseService {
    private N33_SWDao swDao = new N33_SWDao();
    private N33_SWLBDao swlbDao = new N33_SWLBDao();
    private N33_TeaDao teaDao = new N33_TeaDao();
    private N33_ManagerDao managerDao = new N33_ManagerDao();

    private GradeDao gradeDao = new GradeDao();
    private SubjectDao subjectDao = new SubjectDao();

    private TermDao termDao = new TermDao();

    /**
     * 初始化事务的类型
     *
     * @param schoolId
     * @param xqid
     * @return
     */
    public void addN33_SwLbList(ObjectId schoolId, ObjectId xqid) {
        List<N33_SWLBEntry> lbEntrys = new ArrayList<N33_SWLBEntry>();
        lbEntrys.add(new N33_SWLBEntry("全校事务", schoolId, xqid));
        lbEntrys.add(new N33_SWLBEntry("年级事务", schoolId, xqid));
        lbEntrys.add(new N33_SWLBEntry("学科事务", schoolId, xqid));
        lbEntrys.add(new N33_SWLBEntry("个人事务", schoolId, xqid));
        swlbDao.addN33_SWLBEntrys(lbEntrys);
    }

    public List<N33_SWLBDTO> getSwLbDtoByXqid(ObjectId sid, ObjectId xqid) {
        List<N33_SWLBEntry> entries = swlbDao.getSwLbByXqid(xqid, sid);
        List<N33_SWLBDTO> dtos = new ArrayList<N33_SWLBDTO>();
        for (N33_SWLBEntry entry : entries) {
            dtos.add(new N33_SWLBDTO(entry));
        }
        return dtos;
    }

    //物理删除事务
    public void removeSw(ObjectId id) {
        swDao.removeN33_SWEntry(id);
    }

    public List<N33_SWDTO> getSw(ObjectId reId) {
        List<N33_SWDTO> swdtoList = new ArrayList<N33_SWDTO>();
        List<N33_SWEntry> swEntry = swDao.getSwByXqidAndTypereId(reId);
        for (N33_SWEntry Entry : swEntry) {
            N33_SWDTO dto = new N33_SWDTO(Entry);
            swdtoList.add(dto);
        }
        return swdtoList;
    }

    public N33_SWDTO getSwId(ObjectId Id) {
        N33_SWEntry swEntry = swDao.getSwByXqidAndType(Id);
        if (swEntry != null) {
            return new N33_SWDTO(swEntry);
        } else {
            return null;
        }
    }

    public List<N33_TeaDTO> getSwTeaList(ObjectId swId) {
        N33_SWEntry swEntry = swDao.getSwByXqidAndType(swId);
        List<ObjectId> cid = new ArrayList<ObjectId>();
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(swEntry.getTermId());
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if (paiKeTimes.getIr() == 0) {
                cid.add(paiKeTimes.getID());
            }
        }
        List<ObjectId> teaIds = swEntry.getTeacherIds();
        List<Grade> gradeEntry = gradeDao.getIsolateGrADEEntrys(cid);
        List<N33_KSEntry> subjectList = subjectDao.findSubjectsByIdsMapSubId(cid);
        List<N33_TeaEntry> teEntry = teaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(cid, teaIds);
        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
        List<ObjectId> grade = new ArrayList<ObjectId>();
        for (Grade entry : gradeEntry) {
            if (!grade.contains(entry.getGradeId())) {
                grade.add(entry.getGradeId());
                gradeMap.put(entry.getGradeId(), entry);
            }
        }
        Map<ObjectId, N33_KSEntry> subjectMap = new HashMap<ObjectId, N33_KSEntry>();
        List<ObjectId> subject = new ArrayList<ObjectId>();
        for (N33_KSEntry entry : subjectList) {
            if (!subject.contains(entry.getSubjectId())) {
                subject.add(entry.getSubjectId());
                subjectMap.put(entry.getSubjectId(), entry);
            }
        }
        //返回值
        List<N33_TeaDTO> mapList = new ArrayList<N33_TeaDTO>();
        for (N33_TeaEntry teaEntry : teEntry) {
            N33_TeaDTO dto = new N33_TeaDTO(teaEntry);
            String graList = "";
            String subList = "";
            if (teaEntry.getGradeList() != null && teaEntry.getGradeList().size() > 0) {
                for (int i = 0; i < teaEntry.getGradeList().size(); i++) {
                    Grade gra = gradeMap.get(teaEntry.getGradeList().get(i));
                    if (i == 0) {
                        if (gra != null) {
                            graList += gra.getName();
                        }
                    } else {
                        if (gra != null) {
                            graList += ("," + gra.getName());
                        }
                    }
                }
            }

            if (teaEntry.getSubjectList() != null && teaEntry.getSubjectList().size() > 0) {
                for (int i = 0; i < teaEntry.getSubjectList().size(); i++) {
                    N33_KSEntry gra = subjectMap.get(teaEntry.getSubjectList().get(i));
                    if (i == 0) {
                        if (gra != null) {
                            subList += gra.getSubjectName();
                        }
                    } else {
                        if (gra != null) {
                            subList += ("," + gra.getSubjectName());
                        }
                    }
                }
            }

            if (dto.getSex() == 0) {
                dto.setSexStr("女");
            } else {
                dto.setSexStr("男");
            }
            dto.setGradeStr(graList);
            dto.setSubjectStr(subList);
            mapList.add(dto);
        }
        return mapList;
    }

    public void addManagerDto(N33_ManagerDTO dto) {
        managerDao.addManager(dto.getEntry());
    }

    public void deleteManager(ObjectId id) {
        managerDao.deleteManager(id);
    }

    /**
     * 查询所有的管理员
     *
     * @param sid
     * @return
     */
    public List<N33_ManagerDTO> getAllManagerList(ObjectId sid) {
        List<N33_ManagerEntry> list = managerDao.findAllManagerList(sid);
        List<N33_ManagerDTO> dtoList = new ArrayList<N33_ManagerDTO>();
        for (N33_ManagerEntry n33_managerEntry : list) {
            N33_ManagerDTO dto = new N33_ManagerDTO(n33_managerEntry);
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 逻辑删除事务类别
     *
     * @param id
     */
    public void removeSwlbDto(ObjectId id) {
        swlbDao.deleteN33_SWLB(id);
        swDao.deleteN33_AllSwlbSW(id);
    }

    /**
     * 更新事务类别
     *
     * @param id
     */
    public void updateSwlbDto(ObjectId id, String desc) {
        N33_SWLBEntry swlbEntry = swlbDao.findSWLBById(id);
        if (swlbEntry != null) {
            swlbEntry.setDesc(desc);
            swlbDao.updateN33_SWLB(swlbEntry);
        }
    }

    public void addSwEntryList(List<N33_SWDTO> swdtoList) {
        List<N33_SWEntry> entries = new ArrayList<N33_SWEntry>();
        for (N33_SWDTO swdto : swdtoList) {
            entries.add(swdto.buildEntry());
        }
        swDao.addN33_SWEntrys(entries);
    }

    /**
     * 增加事务类型
     *
     * @param desc
     * @param sid
     */
    public void addSwlbDto(String desc, ObjectId sid, ObjectId xqid) {
        N33_SWLBEntry swlbEntry = new N33_SWLBEntry(desc, sid, xqid);
        swlbDao.addN33_SWLBEntry(swlbEntry);
    }

    public List<N33_SWDTO> getSwByXqidAndType(ObjectId xqid, ObjectId typeId, ObjectId sid) {
        List<N33_SWEntry> entries = swDao.getSwByXqidAndType(xqid, sid, typeId);

        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cid = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cid.add(paiKeTimes.getID());
            }
        }
        List<N33_SWDTO> dtoList = new ArrayList<N33_SWDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (N33_SWEntry entry : entries) {
            userIds.addAll(entry.getTeacherIds());
        }
        List<N33_TeaEntry> teaList = teaDao.getTeaList(userIds, cid);
        List<ObjectId> teaiId = new ArrayList<ObjectId>();
        Map<ObjectId, N33_TeaEntry> teaEntryMap = new HashMap<ObjectId, N33_TeaEntry>();
        for (N33_TeaEntry teaEntry : teaList) {
            if (!teaiId.contains(teaEntry.getUserId())) {
                teaiId.add(teaEntry.getUserId());
                teaEntryMap.put(teaEntry.getUserId(), teaEntry);
            }
        }
        for (N33_SWEntry entry : entries) {
            N33_SWDTO swdto = new N33_SWDTO(entry);
            swdto.setXy("周" + entry.getY() + "第" + entry.getX() + "节");
            dtoList.add(swdto);
            String teaName = "";
            String teName = "";
            List<String> names = new ArrayList<String>();
            Integer index = 0;
            for (ObjectId tid : entry.getTeacherIds()) {
                N33_TeaEntry tea = teaEntryMap.get(tid);
                if (tea != null) {
                    teaName += tea.getUserName() + ",";
                    names.add(tea.getUserName());
                    if (index < 3) {
                        teName += tea.getUserName() + ",";
                    }
                }
                index++;
            }
            teName += "等" + names.size() + "人";
            if (names.size() == 0) {
                teName = "全体人员事务";
            }
            swdto.setUserName(teaName);
            swdto.setNames(names);
            swdto.setTeaName(teName);
        }
        return dtoList;
    }


    public N33_SWDTO getSwById(ObjectId id, ObjectId cid) {
        N33_SWEntry entry = swDao.getSwByXqidAndType(id);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        userIds.addAll(entry.getTeacherIds());
        Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(userIds, cid);
        N33_SWDTO swdto = new N33_SWDTO(entry);
        swdto.setXy("周" + entry.getY() + "第" + entry.getX() + "节");
        String teaName = "";
        String teName = "";
        List<String> names = new ArrayList<String>();
        Integer index = 0;
        for (ObjectId tid : entry.getTeacherIds()) {
            N33_TeaEntry tea = teaEntryMap.get(tid);
            if (tea != null) {
                teaName += tea.getUserName() + ",";
                names.add(tea.getUserName());
                if (index < 3) {
                    teName += tea.getUserName() + ",";
                }
            }
            index++;
        }
        teName += "等" + names.size() + "人";
        if (names.size() == 0) {
            teName = "全体老师事务";
        }
        swdto.setUserName(teaName);
        swdto.setNames(names);
        swdto.setTeaName(teName);
        return swdto;
    }

    public void delSwById(ObjectId id) {
        swDao.deleteN33_SW(id);
    }

    /**
     * 根据学期ID和XY查找事务
     *
     * @param xqid
     * @param x
     * @param y
     * @param sid
     * @return
     */
    public List<N33_SWDTO> getSwByXqidAndXY(ObjectId xqid, Integer x, Integer y, ObjectId sid) {
        List<N33_SWDTO> swdtoList = new ArrayList<N33_SWDTO>();
        List<N33_SWEntry> swEntryList = swDao.getSwByXqidAndXY(xqid, x, y, sid);
        for (N33_SWEntry swEntry : swEntryList) {
            N33_SWDTO dto = new N33_SWDTO(swEntry);
            swdtoList.add(dto);
        }
        return swdtoList;
    }

    public void updateSw(N33_SWDTO n33_swdto) {
        N33_SWEntry entry = n33_swdto.buildEntry();
        swDao.updateN33_SW(entry);
    }

    public List<N33_SWDTO> getSwByXqidAndUserId(ObjectId xqid, ObjectId uid) {
        List<N33_SWEntry> entries = swDao.getSwByXqidAndUserId(xqid, uid);
        List<N33_SWDTO> dtoList = new ArrayList<N33_SWDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (N33_SWEntry entry : entries) {
            userIds.addAll(entry.getTeacherIds());
        }
        Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(userIds, xqid);
        for (N33_SWEntry entry : entries) {
            N33_SWDTO swdto = new N33_SWDTO(entry);
            swdto.setXy("周" + entry.getY() + "第" + entry.getX() + "节");
            dtoList.add(swdto);
            String teaName = "";
            String teName = "";
            List<String> names = new ArrayList<String>();
            Integer index = 0;
            for (ObjectId tid : entry.getTeacherIds()) {
                N33_TeaEntry tea = teaEntryMap.get(tid);
                if (tea != null) {
                    teaName += tea.getUserName() + ",";
                    names.add(tea.getUserName());
                    if (index < 3) {
                        teName += tea.getUserName() + ",";
                    }
                }
                index++;
            }
            teName += "等" + names.size() + "人";
            swdto.setUserName(teaName);
            swdto.setNames(names);
            swdto.setTeaName(teName);
        }
        return dtoList;
    }

    public List<Map<String,Object>> getSwByXqidAndUserIdReverseXY(ObjectId xqid, ObjectId uid) {
        List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();
        List<N33_SWEntry> entries = swDao.getSwByXqidAndUserId(xqid, uid);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        userIds.add(uid);
        Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(userIds);
        for (N33_SWEntry entry : entries) {
            Map<String,Object> map = new HashMap<String, Object>();
            Integer x = entry.getY() - 1;
            Integer y = entry.getX() - 1;
            map.put("swDesc",entry.getDesc());
            map.put("x",x);
            map.put("y",y);
            map.put("teaName",teaEntryMap.get(uid).getUserName());
            retList.add(map);
        }
        return retList;
    }

    public List<N33_SWDTO> getGuDingShiWuByXqid(ObjectId xqid) {
        List<N33_SWEntry> n33_swEntries = swDao.getSwByXqid(xqid);
        List<N33_SWDTO> swdtos = new ArrayList<N33_SWDTO>();
        for (N33_SWEntry entry : n33_swEntries) {
            if (entry.getTeacherIds().size() == 0) {
                swdtos.add(new N33_SWDTO(entry));
            }
        }
        return swdtos;
    }

    public List<N33_SWDTO> getGuDingShiWuBySchoolId(ObjectId xqid, ObjectId schoolId) {
        List<N33_SWEntry> n33_swEntries = swDao.getSwBySchoolId(xqid, schoolId);
        List<N33_SWDTO> swdtos = new ArrayList<N33_SWDTO>();
        for (N33_SWEntry entry : n33_swEntries) {
            if (entry.getTeacherIds().size() == 0) {
                swdtos.add(new N33_SWDTO(entry));
            }
        }
        return swdtos;
    }

    /**
     * 固定事务
     * @param xqid
     * @param schoolId
     * @return
     */
    public List<N33_SWDTO> getGuDingShiWuList(ObjectId xqid, ObjectId schoolId) {
        List<N33_SWEntry> n33_swEntries = getGuDingShiWuEntries(xqid, schoolId);
        List<N33_SWDTO> swdtos = new ArrayList<N33_SWDTO>();
        for (N33_SWEntry entry : n33_swEntries) {
            N33_SWDTO dto = new N33_SWDTO(entry);
            int x = dto.getY()-1;
            int y = dto.getX()-1;
            dto.setX(x);
            dto.setY(y);
            swdtos.add(dto);
        }
        return swdtos;
    }

    public List<N33_SWEntry> getGuDingShiWuEntries(ObjectId xqid, ObjectId schoolId) {
        List<N33_SWEntry> n33_swEntries = swDao.getSwBySchoolId(xqid, schoolId);
        return n33_swEntries;
    }

    public List<Map<String,Object>> getGuDingShiWuBySchoolIdReverseXY(ObjectId xqid, ObjectId schoolId) {
        List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();
        List<N33_SWEntry> n33_swEntries = swDao.getSwBySchoolId(xqid, schoolId);
        for (N33_SWEntry entry : n33_swEntries) {
            Map<String,Object> map = new HashMap<String, Object>();
            if (entry.getTeacherIds().size() == 0) {
                Integer x = entry.getY() - 1;
                Integer y = entry.getX() - 1;
                map.put("swDesc",entry.getDesc());
                map.put("x",x);
                map.put("y",y);
                retList.add(map);
            }
        }
        return retList;
    }

    public List<N33_SWDTO> getSwdtoListExceptGD(ObjectId xqid, ObjectId schoolId) {
        List<N33_SWEntry> n33_swEntries = swDao.getSwBySchoolId(xqid, schoolId);
        List<N33_SWDTO> swdtos = new ArrayList<N33_SWDTO>();
        for (N33_SWEntry entry : n33_swEntries) {
            if (entry.getTeacherIds().size() != 0) {
                swdtos.add(new N33_SWDTO(entry));
            }
        }
        return swdtos;
    }

    public Map<String, List<N33_SWEntry>> getGuDingShiWuBySchoolIdForMap(ObjectId xqid, ObjectId schoolId) {
        Map<String, List<N33_SWEntry>> swMap = new HashMap<String, List<N33_SWEntry>>();
        List<N33_SWEntry> n33_swEntries = swDao.getSwBySchoolId(xqid, schoolId);
        for (N33_SWEntry entry : n33_swEntries) {
            String xy = entry.getY() + "," + entry.getX();
            if (swMap.get(xy) != null) {
                List<N33_SWEntry> swEntryList = swMap.get(xy);
                swEntryList.add(entry);
                swMap.put(xy, swEntryList);
            } else {
                List<N33_SWEntry> swEntryList = new ArrayList<N33_SWEntry>();
                swEntryList.add(entry);
                swMap.put(xy, swEntryList);
            }
        }
        return swMap;
    }

    public Map<String, List<N33_SWEntry>> getGuDingShiWuBySchoolIdAndXqidForMap(ObjectId xqid, ObjectId schoolId) {
        Map<String, List<N33_SWEntry>> swMap = new HashMap<String, List<N33_SWEntry>>();
        List<N33_SWEntry> n33_swEntries = swDao.getSwBySchoolId(xqid, schoolId);
        for (N33_SWEntry entry : n33_swEntries) {
            if (entry.getTeacherIds().size() == 0) {
                String xy = entry.getY() + "," + entry.getX();
                if (swMap.get(xy) != null) {
                    List<N33_SWEntry> swEntryList = swMap.get(xy);
                    swEntryList.add(entry);
                    swMap.put(xy, swEntryList);
                } else {
                    List<N33_SWEntry> swEntryList = new ArrayList<N33_SWEntry>();
                    swEntryList.add(entry);
                    swMap.put(xy, swEntryList);
                }
            }
        }
        return swMap;
    }

    /**
     * @param xqid
     * @param uids
     * @return
     */
    public List<N33_SWDTO> getSwByXqidAndUserIds(ObjectId xqid, List<ObjectId> uids) {
        List<N33_SWEntry> entries = swDao.getSwByXqidAndUserIds(xqid, uids);
        List<N33_SWDTO> dtoList = new ArrayList<N33_SWDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (N33_SWEntry entry : entries) {
            userIds.addAll(entry.getTeacherIds());
        }
        Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(userIds, xqid);
        for (N33_SWEntry entry : entries) {
            N33_SWDTO swdto = new N33_SWDTO(entry);
            swdto.setXy("周" + entry.getY() + "第" + entry.getX() + "节");
            dtoList.add(swdto);
            String teaName = "";
            String teName = "";
            List<String> names = new ArrayList<String>();
            Integer index = 0;
            for (ObjectId tid : entry.getTeacherIds()) {
                N33_TeaEntry tea = teaEntryMap.get(tid);
                if (tea != null) {
                    teaName += tea.getUserName() + ",";
                    names.add(tea.getUserName());
                    if (index < 3) {
                        teName += tea.getUserName() + ",";
                    }
                }
                index++;
            }
            teName += "等" + names.size() + "人";
            swdto.setUserName(teaName);
            swdto.setNames(names);
            swdto.setTeaName(teName);
        }
        return dtoList;
    }
}
