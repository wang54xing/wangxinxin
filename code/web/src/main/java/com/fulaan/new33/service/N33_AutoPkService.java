package com.fulaan.new33.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.N33_GradeWeekRangeDao;
import com.db.new33.isolate.N33_ClassroomDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.SubjectDao;
import com.db.new33.isolate.ZouBanTimeDao;
import com.db.new33.paike.N33_AutoPaiKeDao;
import com.db.new33.paike.N33_JXBCTDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_JXBKSDao;
import com.db.new33.paike.N33_SWDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZIXIKEDao;
import com.fulaan.new33.dto.isolate.N33_GDSXDTO;
import com.fulaan.new33.dto.isolate.N33_TeaDTO;
import com.fulaan.new33.dto.paike.N33_AutoPaiKeDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.ZouBanTimeEntry;
import com.pojo.new33.paike.N33_AutoPaiKeEntry;
import com.pojo.new33.paike.N33_JXBCTEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_JXBKSEntry;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.new33.paike.N33_YKBEntry;

/**
 * Created by albin on 2018/6/21.
 */
@Service
@SuppressWarnings("all")
public class N33_AutoPkService extends BaseService {
    private N33_JXBDao jxbDao = new N33_JXBDao();

    private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();

    private SubjectDao subjectDao = new SubjectDao();

    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();

    private N33_SWDao swDao = new N33_SWDao();

    private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();

    private N33_TeaDao teaDao = new N33_TeaDao();

    private N33_YKBDao ykbDao = new N33_YKBDao();

    private ZouBanTimeDao zouBanTimeDao = new ZouBanTimeDao();
    private N33_ZIXIKEDao n33_zixikeDao = new N33_ZIXIKEDao();
    private N33_GradeWeekRangeDao gradeWeekRangeDao = new N33_GradeWeekRangeDao();
    @Autowired
    private N33_GDSXService gdsxService;
    

@Autowired
    private N33_TurnOffService turnOffService;

    //初始化总课时
    private Map<ObjectId, Integer> ZongSubjectKs = null;
    //初始化排课课时
    private Map<ObjectId, Integer> PaiKeSubjectKs = null;
    //查询原课表
    private List<N33_YKBEntry> ykbEntries = null;
    //事务
    private List<N33_SWEntry> swEntries = null;
    //教学班
    private List<N33_JXBEntry> jxbEntries = null;
    private Map<ObjectId, N33_JXBEntry> jxbEntryMap = null;
    //走班格
    private List<ZouBanTimeEntry> zouBanTimeEntries = null;
    //教学班冲突
    private List<N33_JXBCTEntry> jxbctEntries = null;
    //是否周六日排课
    private N33_GradeWeekRangeEntry gradeWeekRangeEntry = null;
    //所有的排课规则
    private List<N33_AutoPaiKeEntry> autoPaiKeEntryList = null;

    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();
    @Autowired
    private PaiKeService paiKeService;
    private N33_AutoPaiKeDao autoPaiKeDao = new N33_AutoPaiKeDao();

    /**
     * 教学班信息完备性检查
     *
     * @param schoolId
     * @param ciId
     * @param gradeId
     * @return
     */
    public int checkJXBInfo(ObjectId schoolId, ObjectId ciId, String gradeId) {
        List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByXqid(ciId, schoolId, new ObjectId(gradeId));
        Map<ObjectId, N33_KSEntry> ksEntrieMap = subjectDao.findSubjectsByIdsForSubId(ciId, new ObjectId(gradeId), schoolId);
        List<ObjectId> subId = new ArrayList<ObjectId>();
        for (N33_KSEntry entry : ksEntries) {
            subId.add(entry.getSubjectId());
        }
        int resultCode = 0;
        //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, new ObjectId(gradeId), ciId,1);
        //jxb add			

        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(schoolId, new ObjectId(gradeId), subId, ciId,acClassType);
        if (jxbEntryList != null && jxbEntryList.size() != 0) {
            for (N33_JXBEntry jxbEntry : jxbEntryList) {
                if (jxbEntry.getType() != 1) {
                    if (jxbEntry.getJXBKS() == 0) {
                        continue;
                    }
                } else {
                    if (jxbEntry.getJXBKS() == 0) {
                        continue;
                    }
                }
                if (jxbEntry.getType() != 4) {
                    if (jxbEntry.getClassroomId() == null) {
                        resultCode = 1;//教室没完善
                        break;
                    }
                    if (jxbEntry.getStudentIds() == null || jxbEntry.getStudentIds().size() == 0) {
                        resultCode = 2;//学生没完善
                        break;
                    }
                    if (jxbEntry.getSubjectId() == null) {
                        resultCode = 3;//学科没完善
                        break;
                    }
                    if (jxbEntry.getTercherId() == null) {
                        resultCode = 4;//老师没完善
                        break;
                    }
                }
            }
        } else {
            resultCode = 5;
        }
        return resultCode;
    }

    /**
     * 初始化教学班已排课时
     *
     * @param jxbEntry
     * @return
     */
    public Map<ObjectId, Integer> initJxbPaiKs(List<N33_JXBEntry> jxbEntry) {
        Map<ObjectId, Integer> jxbYPKs = new HashMap<ObjectId, Integer>();
        for (N33_JXBEntry jxbEntry1 : jxbEntry) {
            jxbYPKs.put(jxbEntry1.getID(), 0);
        }
        return jxbYPKs;
    }


    public Map<String, Object> getStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> status = (Map<String, Object>) session.getAttribute("paike");
        if (status != null && status.get("st") != null && (Integer) status.get("st") == -1) {
            session.removeAttribute("status");
        }
        return status;
    }

    /**
     * 自动排课
     */
    public void autoPaiKe(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, ObjectId xqid, HttpServletRequest request) {
        //qingkong
        paiKeService.clearKB(ciId, gradeId.toString(), schoolId);
        paiKeService.checkYKB(ciId, gradeId.toString(), schoolId);
        n33_zixikeDao.removeN33_ZIXIKEEntryByXqGiD(ciId, gradeId);
        jxbksDao.remove(gradeId, ciId);
        HttpSession session = request.getSession();
        Map<String, Object> status = new HashMap<String, Object>();
        status.put("st", 1);
        status.put("renshu", 1);
        status.put("zrenshu", 0);
        session.setAttribute("paike", status);
        //原课表
        ykbEntries = ykbDao.getYKBsByclassRoomIds(ciId, schoolId, new ArrayList<ObjectId>());
        //学科课时
        Map<ObjectId, N33_KSEntry> ksEntries = subjectDao.findSubjectsByIdsMapSubId(ciId, gradeId, schoolId);
        //教学班
        List<N33_KSEntry> ksEn = subjectDao.getIsolateSubjectEntryByXqid(ciId, schoolId, gradeId);
        List<ObjectId> subIds = new ArrayList<ObjectId>();
        for (N33_KSEntry entry : ksEn) {
            subIds.add(entry.getSubjectId());
        }
        //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add			

        jxbEntries = jxbDao.getJXBList(schoolId, gradeId, subIds, ciId,acClassType);
        //教学班
//        jxbEntries = jxbDao.getJXBsByXqidAndGid(ciId, schoolId, gradeId);
       
        jxbEntryMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, subIds,acClassType);
//        jxbEntryMap = jxbDao.getJXBsByXqidAndSid(ciId, schoolId, gradeId);
        //初始化排课课时
        ZongSubjectKs = initSubjectKS(jxbEntries, ksEntries);
        PaiKeSubjectKs = initJxbPaiKs(jxbEntries);
        //事务
        swEntries = swDao.getSwByXqid(xqid);
        //走班格
        zouBanTimeEntries = zouBanTimeDao.getZouBanTimeListByType(schoolId, ciId, gradeId, 0);
        gradeWeekRangeEntry = gradeWeekRangeDao.getGradeWeekRangeByXqid(ciId, schoolId, gradeId);
        autoPaiKeEntryList = autoPaiKeDao.getEntryList(schoolId, ciId, gradeId);
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        for (N33_JXBEntry entry : jxbEntries) {
            jxbIds.add(entry.getID());
        }
        //教学班冲突
        jxbctEntries = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, ciId);
        //教室
        List<N33_ClassroomEntry> classroomEntries = classroomDao.getRoomEntryListByXqGrade(ciId, schoolId, gradeId);
        List<ObjectId> roomId = new ArrayList<ObjectId>();
        for (N33_ClassroomEntry classroomEntry : classroomEntries) {
            roomId.add(classroomEntry.getRoomId());
        }
        //需要上课的老师id
        List<ObjectId> teaIds = get需要自动排课的老师(jxbEntries);
        List<N33_TeaEntry> teaEntries = teaDao.findIsolateN33_TeaEntryByuIds(ciId, teaIds);
        status.put("zrenshu", teaEntries.size());
        //排序之后的老师
        List<N33_TeaEntry> teaEntries1 = teaSortByJXBCount(teaEntries, jxbEntries);
        //老师需要排课的教室
        Map<ObjectId, List<ObjectId>> teaRoomIds = get老师所需要排课的教室(teaIds, jxbEntries);
//        for (N33_TeaEntry teaEntry : teaEntries1) {
//            //老师所需要排课的教室
//            List<ObjectId> roomIds = teaRoomIds.get(teaEntry.getUserId());
//            for (ObjectId rId : roomIds) {
//                for (; ; ) {
//                    //自动排课
//                    if (PaiKe自动化(rId, teaEntry.getUserId(), xqid, ciId)) {
//                        break;
//                    }
//                }
//            }
//        }
        Integer count = 0;
        for (N33_TeaEntry teaEntry : teaEntries1) {
            count++;
            List<N33_JXBEntry> jxbEntryList = get老师所在的待排课教学班(teaEntry.getUserId(), jxbEntries);
            for (; jxbEntryList.size() != 0; ) {
                //随机获取一个教学班
                N33_JXBEntry jxbEntry = getAnyJXB(jxbEntryList);
                String name = "行政老师";
                if (jxbEntry.getType() == 1 || jxbEntry.getType() == 2) {
                    name = "走班老师";
                }
                status.put("renshu", name + count);
                jxbEntryList.remove(jxbEntry);
                PaiKe自动化多课时(jxbEntry, teaEntry.getUserId(), xqid, ciId);
            }
        }
        for (N33_YKBEntry entry : ykbEntries) {
            if (entry.getIsUse() == 1 && entry.getGradeId().toString().equals(gradeId.toString())) {
                ykbDao.updateN33_YKB(entry);
            }
        }
        //教学班课时计算
        List<N33_JXBKSEntry> entries = new ArrayList<N33_JXBKSEntry>();
        for (N33_JXBEntry jxb : jxbEntries) {
            Integer jxbCount = PaiKeSubjectKs.get(jxb.getID());
//            if(jxb.getID().toString().equals("5b3b24898fb25af3aa90e826")){
            System.out.println("教学班最终课时");
            System.out.println(jxb.getName() + ":" + ZongSubjectKs.get(jxb.getID()));
            System.out.println(jxb.getName() + ":" + jxbCount);
//            }
            if (jxbCount != 0) {
                N33_JXBKSEntry n33_jxbksEntry = new N33_JXBKSEntry(jxb.getID(), jxb.getSubjectId(), jxbCount, jxb.getGradeId(), jxb.getSchoolId(), jxb.getTermId(), jxb.getDanOrShuang());
                entries.add(n33_jxbksEntry);
            }
        }
        if (entries.size() != 0) {
            jxbksDao.addN33_JXBKSEntrys(entries);
        }
        status.put("st", -1);
    }

    public Boolean PaiKe自动化(ObjectId rid, ObjectId teaId, ObjectId xqid, ObjectId ciId) {
        //教室排课老师有哪些
        List<ObjectId> TeaRoomList = get教室排课老师有哪些(jxbEntries, rid);
        //对应教室的所有老师的点
        Map<ObjectId, List<Map<String, Object>>> TeaRoomDianList = getTeaKpkXY(TeaRoomList, swEntries, ykbEntries, rid, xqid, ciId);
        Map<ObjectId, List<Map<String, Object>>> TeaRoomDianCountList = get某个教室所有老师存在的可排课点与其他老师的计算(TeaRoomDianList, TeaRoomList);
        Map<ObjectId, List<Map<String, Object>>> TeaRoomDianCountPaiXuList = get排序后的老师所在教室的点(TeaRoomDianCountList, TeaRoomList);

        //老师教室排课点
        List<Map<String, Object>> maps = TeaRoomDianCountPaiXuList.get(teaId);
        //开始执行排课
        for (Map<String, Object> map : maps) {
            //某个老师所在教室排课教学班
            List<N33_JXBEntry> teaJxbList = get老师所在某个教室的待排课教学班(teaId, rid, jxbEntries);
            //情况课时已经排满的教学班
            List<N33_JXBEntry> jxbEntryList = new ArrayList<N33_JXBEntry>();
            for (N33_JXBEntry jxbEntry : teaJxbList) {
                Integer ZongCount = ZongSubjectKs.get(jxbEntry.getID());
                Integer PaiCount = PaiKeSubjectKs.get(jxbEntry.getID());
//                if (jxbEntry.getID().toString().equals("5b3b24808fb25af3aa90e812")) {
//                    System.out.println("开始打印点");
//                    for (Map<String, Object> map1 : maps) {
//                        System.out.println(map1.get("x")+","+map1.get("y"));
//                    }
//                }
//                if(jxbEntry.getID().toString().equals("5b3b24898fb25af3aa90e826")){
//                    System.out.println("开始打印点");
//                    System.out.println(PaiCount);
//                }
                if (ZongCount != PaiCount) {
                    jxbEntryList.add(jxbEntry);
                }
            }
            for (; ; ) {
                if (jxbEntryList.size() != 0) {
                    //随机获取一个教学班
                    N33_JXBEntry jxbEntry = getAnyJXB(jxbEntryList);
                    //当前点是否可以被排课
                    Integer x = (Integer) map.get("x");
                    Integer y = (Integer) map.get("y");
                    //是否可以排课
                    Boolean isPk = isConflict(x, y, rid, jxbEntry);
                    if (isPk) {
                        //修改原课表对应教室的X和Y上的值
                        updateYkbEntry(jxbEntry, x, y, rid);
                        return false;
                    }
                    //随机完成后移除教学班
                    jxbEntryList.remove(jxbEntry);
                } else {
                    break;
                }
            }
        }
        return true;
    }


    public List<Map<String, Object>> 重置排序(List<Map<String, Object>> maps, Integer x) {
        for (Map<String, Object> map : maps) {
            Integer count = (Integer) map.get("count");
            if (count != -999) {
                Integer yz = (Integer) map.get("x");
                if (yz == x) {
                    if (count < 0) {
                        map.put("count", 10000);
                    } else {
                        map.put("count", count + 10000);
                    }
                }
            }
        }
        List<Map<String, Object>> teaRoomDian = get排序(maps);
        return teaRoomDian;
    }

    public Boolean PaiKe自动化多课时(N33_JXBEntry jxbEntry, ObjectId teaId, ObjectId xqid, ObjectId ciId) {
        //教室排课老师有哪些
        List<ObjectId> TeaRoomList = get教室排课老师有哪些(jxbEntries, jxbEntry.getClassroomId());
        //对应教室的所有老师的点
        Map<ObjectId, List<Map<String, Object>>> TeaRoomDianList = getTeaKpkXY(TeaRoomList, swEntries, ykbEntries, jxbEntry.getClassroomId(), xqid, ciId);
        Map<ObjectId, List<Map<String, Object>>> TeaRoomDianCountList = get某个教室所有老师存在的可排课点与其他老师的计算(TeaRoomDianList, TeaRoomList);
        Map<ObjectId, List<Map<String, Object>>> TeaRoomDianCountPaiXuList = get排序后的老师所在教室的点(TeaRoomDianCountList, TeaRoomList);
        //老师教室排课点
        List<Map<String, Object>> maps = TeaRoomDianCountPaiXuList.get(teaId);
        for (; ; ) {
            //教学班待排课时
            Integer ZongCount = ZongSubjectKs.get(jxbEntry.getID());
            Integer PaiCount = PaiKeSubjectKs.get(jxbEntry.getID());
            if (maps.size() == 0) {
                return true;
            }
            if (PaiCount == ZongCount) {
                return true;
            }
            //总共产生多个个排课点
            Integer dianCount = ZongCount - PaiCount;
            if (dianCount > maps.size()) {
                dianCount = maps.size();
            }
            List<Map<String, Object>> dianList = new ArrayList<Map<String, Object>>();
            for (Integer count = 1; count <= dianCount; count++) {
                Map<String, Object> map = maps.get(0);
                if (map != null) {
                    Integer x = (Integer) map.get("x");
                    dianList.add(map);
                    //移除排课点
                    maps.remove(map);
                    //重置教室点
                    maps = 重置排序(maps, x);
                }
            }
//            //移除排课点
//            for (Map<String, Object> map : dianList) {
//                maps.remove(map);
//            }
            //点是否可以使用
            for (Map<String, Object> map : dianList) {
                Integer x = (Integer) map.get("x");
                Integer y = (Integer) map.get("y");
                //是否可以排课
                Boolean isPk = isConflict(x, y, jxbEntry.getClassroomId(), jxbEntry);
                if (isPk) {
                    //修改原课表对应教室的X和Y上的值
                    updateYkbEntry(jxbEntry, x, y, jxbEntry.getClassroomId());
                }
            }
        }
    }

    /**
     * 修改原课表
     *
     * @param jxbEntry
     * @param x
     * @param y
     * @param rid
     */
    public void updateYkbEntry(N33_JXBEntry jxbEntry, Integer x, Integer y, ObjectId rid) {
        for (N33_YKBEntry ykbEntry : ykbEntries) {
            if (ykbEntry.getClassroomId().toString().equals(rid.toString()) && x == ykbEntry.getX() && y == ykbEntry.getY()) {
                ykbEntry.setJxbId(jxbEntry.getID());
                ykbEntry.setGradeId(jxbEntry.getGradeId());
                ykbEntry.setTeacherId(jxbEntry.getTercherId());
                ykbEntry.setType(jxbEntry.getType());
                ykbEntry.setSubjectId(jxbEntry.getSubjectId());
                ykbEntry.setIsUse(1);
                Integer jxbCount = PaiKeSubjectKs.get(jxbEntry.getID()) + 1;
                PaiKeSubjectKs.put(jxbEntry.getID(), jxbCount);
                if (ykbEntry.getType() == 6) {
                    N33_JXBEntry n33_jxbEntry = jxbEntryMap.get(jxbEntry.getRelativeId());
                    ykbEntry.setNTeacherId(n33_jxbEntry.getTercherId());
                    ykbEntry.setNSubjectId(n33_jxbEntry.getSubjectId());
                    ykbEntry.setNJxbId(n33_jxbEntry.getID());
                    Integer njxbCount = PaiKeSubjectKs.get(n33_jxbEntry.getID()) + 1;
                    PaiKeSubjectKs.put(n33_jxbEntry.getID(), njxbCount);
                }
            }
        }
    }


    public List<ObjectId> get需要自动排课的老师(List<N33_JXBEntry> jxbEntries) {
        List<ObjectId> teaIds = new ArrayList<ObjectId>();
        for (N33_JXBEntry entry : jxbEntries) {
            if (entry.getType() != 4) {
                if (!teaIds.contains(entry.getTercherId())) {
                    teaIds.add(entry.getTercherId());
                }
            }
        }
        return teaIds;
    }

    public List<ObjectId> get教室排课老师有哪些(List<N33_JXBEntry> jxbEntries, ObjectId roomId) {
        List<ObjectId> roomTeaList = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            if (jxbEntry.getType() != 4) {
                if (jxbEntry.getClassroomId().toString().equals(roomId.toString())) {
                    if (!roomTeaList.contains(jxbEntry.getTercherId())) {
                        roomTeaList.add(jxbEntry.getTercherId());
                    }
                }
            }
        }
        return roomTeaList;
    }

//    public Map<ObjectId, List<Map<String, Object>>> get(Map<ObjectId, List<Map<String, Object>>> teaClassRoomDianList, List<ObjectId> roomTeaList) {
//        Map<ObjectId, List<Map<String, Object>>> idListMap = new HashMap<ObjectId, List<Map<String, Object>>>();
//        for (ObjectId tea : roomTeaList) {
//            //教师在教室能排课的点
//            List<Map<String, Object>> teaRoomDian = teaClassRoomDianList.get(tea);
//            for (Map<String, Object> dian : teaRoomDian) {
//                //计算当前点在教师的老师可用数量
//                Integer x = (Integer) dian.get("x");
//                Integer y = (Integer) dian.get("y");
//                for (ZouBanTimeEntry zouBanTimeEntry : zouBanTimeEntries) {
//                    if ((zouBanTimeEntry.getX() - 1) == x && (zouBanTimeEntry.getY() - 1) == y) {
//
//                    }
//                }
//                dian.put("count", -1);
//            }
//        }
//    }


    public Map<ObjectId, List<ObjectId>> get老师所需要排课的教室(List<ObjectId> teaIds, List<N33_JXBEntry> jxbEntries) {
        Map<ObjectId, List<ObjectId>> teaClassRoomIds = new HashMap<ObjectId, List<ObjectId>>();
        for (ObjectId teaId : teaIds) {
            List<ObjectId> classRoomIds = new ArrayList<ObjectId>();
            for (N33_JXBEntry jxbEntry : jxbEntries) {
                if (jxbEntry.getType() != 4) {
                    if (teaId.toString().equals(jxbEntry.getTercherId().toString())) {
                        if (!classRoomIds.contains(jxbEntry.getClassroomId())) {
                            classRoomIds.add(jxbEntry.getClassroomId());
                        }
                    }
                }
            }
            teaClassRoomIds.put(teaId, classRoomIds);
        }
        return teaClassRoomIds;
    }

    public List<N33_JXBEntry> get老师所在某个教室的待排课教学班(ObjectId teaId, ObjectId classRoomId, List<N33_JXBEntry> jxbEntries) {
        List<N33_JXBEntry> teaClassRoomJxbIds = new ArrayList<N33_JXBEntry>();
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            if (jxbEntry.getType() != 4) {
                if (teaId.toString().equals(jxbEntry.getTercherId().toString()) && classRoomId.toString().equals(jxbEntry.getClassroomId().toString())) {
                    if (!teaClassRoomJxbIds.contains(jxbEntry.getID())) {
                        teaClassRoomJxbIds.add(jxbEntry);
                    }
                }
            }
        }
        return teaClassRoomJxbIds;
    }

    public List<N33_JXBEntry> get老师所在的待排课教学班(ObjectId teaId, List<N33_JXBEntry> jxbEntries) {
        List<N33_JXBEntry> teaClassRoomJxbIds = new ArrayList<N33_JXBEntry>();
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            if (jxbEntry.getType() != 4) {
                if (teaId.toString().equals(jxbEntry.getTercherId().toString())) {
                    if (!teaClassRoomJxbIds.contains(jxbEntry.getID())) {
                        teaClassRoomJxbIds.add(jxbEntry);
                    }
                }
            }
        }
        return teaClassRoomJxbIds;
    }

    public List<Map<String, Object>> get排序(List<Map<String, Object>> teaClassRoomDianList) {
        //教师在教室能排课的点
        List<Map<String, Object>> teaRoomDian = teaClassRoomDianList;
        //排序
        Collections.sort(teaRoomDian, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (Integer) o1.get("count") - (Integer) o2.get("count");
            }
        });
        return teaRoomDian;
    }

    /**
     * @param teaClassRoomDianList 老师在教室所有能排课的点
     * @param roomTeaList
     * @return
     */
    public Map<ObjectId, List<Map<String, Object>>> get某个教室所有老师存在的可排课点与其他老师的计算(Map<ObjectId, List<Map<String, Object>>> teaClassRoomDianList, List<ObjectId> roomTeaList) {
        Map<ObjectId, List<Map<String, Object>>> idListMap = new HashMap<ObjectId, List<Map<String, Object>>>();
        for (ObjectId tea : roomTeaList) {
            //教师在教室能排课的点
            List<Map<String, Object>> teaRoomDian = teaClassRoomDianList.get(tea);
            for (Map<String, Object> dian : teaRoomDian) {
                Random random = new Random();
                Integer count = random.nextInt(100) + 1;
                dian.put("count", count);
            }
            teaRoomDian = get排序(teaRoomDian);
            for (Map<String, Object> dian : teaRoomDian) {
                //计算当前点在教师的老师可用数量
                Integer count = 0;
                Integer x = (Integer) dian.get("x");
                Integer y = (Integer) dian.get("y");
                //所有老师的所有点
                for (List<Map<String, Object>> RoomDian : teaClassRoomDianList.values()) {
                    for (Map<String, Object> map : RoomDian) {
                        Integer x1 = (Integer) map.get("x");
                        Integer y1 = (Integer) map.get("y");
                        if (x == x1 && y == y1) {
                            //没存在一个就+1
                            count++;
                        }
                    }
                }
                dian.put("count", count);
                for (ZouBanTimeEntry zouBanTimeEntry : zouBanTimeEntries) {
                    if ((zouBanTimeEntry.getX() - 1) == x && (zouBanTimeEntry.getY() - 1) == y) {
                        dian.put("count", -998);
                    }
                }
                //过滤配置
                for (N33_AutoPaiKeEntry autoPaiKeEntry : autoPaiKeEntryList) {
                    if (autoPaiKeEntry.getType() != 4) {
                        for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                            if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                                dian.put("count", -99);
                                break;
                            }
                        }
                    } else {
                        for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                            if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                                dian.put("count", 9999);
                                break;
                            }
                        }
                    }
                    if (autoPaiKeEntry.getTeaIds().contains(tea)) {
                        //处理优先的
                        if (autoPaiKeEntry.getStatus() == 2) {
                            for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                                if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                                    dian.put("count", -997);
                                    break;
                                }
                            }
                        }
                        if (autoPaiKeEntry.getStatus() == 1) {
                            for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                                if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                                    dian.put("count", -999);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            Integer min = 100;
            for (Map<String, Object> dian : teaRoomDian) {
                Integer minCount = (Integer) dian.get("count");
                if (minCount < min && minCount >= 0) {
                    min = minCount;
                }
            }
            if (min == 0) {
                min = 100;
            }
            for (Map<String, Object> dian : teaRoomDian) {
                Integer CountSize = (Integer) dian.get("count");
                if (CountSize == 0) {
                    Random random = new Random();
                    Integer count = random.nextInt(min) + 1;
                    dian.put("count", count);
                }
            }
            idListMap.put(tea, teaRoomDian);
        }
        return teaClassRoomDianList;
    }

    /**
     * 将点进行排序，优先选择某个点少的点
     *
     * @param teaClassRoomDianList
     * @param roomTeaList
     * @return
     */
    public Map<ObjectId, List<Map<String, Object>>> get排序后的老师所在教室的点(Map<ObjectId, List<Map<String, Object>>> teaClassRoomDianList, List<ObjectId> roomTeaList) {
        Map<ObjectId, List<Map<String, Object>>> idListMap = new HashMap<ObjectId, List<Map<String, Object>>>();
        for (ObjectId tea : roomTeaList) {
            //教师在教室能排课的点
            List<Map<String, Object>> teaRoomDian = teaClassRoomDianList.get(tea);
            //排序
            Collections.sort(teaRoomDian, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    return (Integer) o1.get("count") - (Integer) o2.get("count");
                }
            });
            idListMap.put(tea, teaRoomDian);
        }
        return idListMap;
    }

    /**
     * 查询所有老师可排课的点集合
     *
     * @param tids
     * @param swEntries
     * @param ykbEntries
     * @return
     */
    public Map<ObjectId, List<Map<String, Object>>> getTeaKpkXY(List<ObjectId> tids, List<N33_SWEntry> swEntries, List<N33_YKBEntry> ykbEntries, ObjectId roomId, ObjectId xqid, ObjectId ciId) {
        Map<ObjectId, List<Map<String, Object>>> retMap = new HashMap<ObjectId, List<Map<String, Object>>>();

        for (ObjectId teaId : tids) {

            //每一个老师可以排课的xy点List
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            //对应老师已经排过课的点
            List<Map<String, Object>> ypkList = new ArrayList<Map<String, Object>>();

            //对应教室源课表中的点
            List<Map<String, Object>> roomXY = new ArrayList<Map<String, Object>>();
            for (N33_YKBEntry ykbEntry : ykbEntries) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (ykbEntry.getClassroomId().toString().equals(roomId.toString()) && ykbEntry.getX() <= gradeWeekRangeEntry.getEnd()) {
                    map.put("x", ykbEntry.getX());
                    map.put("y", ykbEntry.getY());
                    roomXY.add(map);
                }
            }

            for (N33_YKBEntry ykbEntry : ykbEntries) {
                //该老师已经排课的点||该教室已经排课的点
                Map<String, Object> ypkMap = new HashMap<String, Object>();

                boolean flag = true;
                if ((ykbEntry.getTeacherId() != null && teaId.toString().equals(ykbEntry.getTeacherId().toString())) || (ykbEntry.getNTeacherId() != null && teaId.toString().equals(ykbEntry.getNTeacherId().toString())) || (ykbEntry.getClassroomId().toString().equals(roomId.toString()) && ykbEntry.getIsUse() == 1)) {
                    for (Map<String, Object> map : ypkList) {
                        if (ykbEntry.getX() == ((Integer) map.get("x")) && ykbEntry.getY() == ((Integer) map.get("y"))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        ypkMap.put("x", ykbEntry.getX());
                        ypkMap.put("y", ykbEntry.getY());
                        ypkList.add(ypkMap);
                    } else {
                        continue;
                    }
                }
            }

            List<N33_GDSXDTO> gdsxdtos = new ArrayList<N33_GDSXDTO>();
            N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(teaId, ciId);
            List<ObjectId> teaGradeIds = teaEntry.getGradeList();
            for (ObjectId gradeId : teaGradeIds) {
                gdsxdtos.addAll(gdsxService.getGDSXBySidAndXqid(xqid, teaEntry.getSchoolId(), gradeId));
            }

            for (N33_GDSXDTO gdsxdto : gdsxdtos) {
                //该老师已经排课的点||该教室已经排课的点
                Map<String, Object> ypkMap = new HashMap<String, Object>();
                boolean flag = true;
                for (Map<String, Object> map : ypkList) {
                    if (gdsxdto.getX() == ((Integer) map.get("x")) && gdsxdto.getY() == ((Integer) map.get("y"))) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    ypkMap.put("x", gdsxdto.getX());
                    ypkMap.put("y", gdsxdto.getY());
                    ypkList.add(ypkMap);
                } else {
                    continue;
                }
            }

            for (N33_SWEntry swEntry : swEntries) {
                //该老师已经排课的点||该教室已经排课的点
                Map<String, Object> ypkMap = new HashMap<String, Object>();

                boolean flag = true;
                if (swEntry.getTeacherIds().size() == 0 || swEntry.getTeacherIds().contains(teaId)) {
                    for (Map<String, Object> map : ypkList) {
                        if ((swEntry.getY() - 1) == ((Integer) map.get("x")) && (swEntry.getX() - 1) == ((Integer) map.get("y"))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        ypkMap.put("x", swEntry.getY() - 1);
                        ypkMap.put("y", swEntry.getX() - 1);
                        ypkList.add(ypkMap);
                    } else {
                        continue;
                    }
                }
            }

            //遍历教室源课表的点，添加该教室老师可排的点
            for (Map<String, Object> roomMap : roomXY) {
                boolean isAdd = true;
                for (Map<String, Object> ypkMap : ypkList) {
                    if (((Integer) roomMap.get("x") == ((Integer) ypkMap.get("x"))) && ((Integer) roomMap.get("y") == ((Integer) ypkMap.get("y")))) {
                        isAdd = false;
                        break;
                    }
                }

                if (isAdd) {
                    boolean flag = true;
                    for (Map<String, Object> map : list) {
                        if (((Integer) roomMap.get("x") == ((Integer) map.get("x"))) && ((Integer) roomMap.get("y") == ((Integer) map.get("y")))) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        list.add(roomMap);
                    }
                }
            }
                /*for (Map<String, Object> ypkMap : ypkList) {
                    if (((Integer)roomMap.get("x") == ((Integer)ypkMap.get("x"))) && ((Integer)roomMap.get("y") == ((Integer)ypkMap.get("y")))) {
                        continue wq;
                    } else {
                        Boolean flag=true;
                        for (Map<String,Object> map : list) {
                            if(((Integer)roomMap.get("x") == ((Integer)map.get("x"))) && ((Integer)roomMap.get("y") == ((Integer)map.get("y")))){
                                flag = false;
                            }
                        }
                        if(flag){
                            list.add(roomMap);
                        }
                    }
                }*/


            retMap.put(teaId, list);
        }
        return retMap;
    }

    /**
     * 判断教师是否跨头
     *
     * @param jxbEntryList
     * @return
     */
    private boolean checkKuaTou(List<N33_JXBEntry> jxbEntryList) {
        ObjectId jxbId = jxbEntryList.get(0).getGradeId();
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            if (!jxbEntry.getGradeId().toString().equals(jxbId.toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回所有的教学班总课时
     *
     * @param jxbEntryList
     * @param ksEntryMap
     * @return
     */
    public Map<ObjectId, Integer> initSubjectKS(List<N33_JXBEntry> jxbEntryList, Map<ObjectId, N33_KSEntry> ksEntryMap) {
        Map<ObjectId, Integer> retMap = new HashMap<ObjectId, Integer>();
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            retMap.put(jxbEntry.getID(), jxbEntry.getJXBKS());
        }
        return retMap;
    }

    /**
     * 随机获取一个XY
     *
     * @param xyList
     * @return
     */
    private Map<String, Object> getAnyXy(List<Map<String, Object>> xyList) {
        Integer size = xyList.size();
        Random random = new Random();
        Integer count = random.nextInt(size);
        Map<String, Object> map = xyList.get(count);
        return map;
    }


    /**
     * 随机获取一个JXB
     *
     * @param xyList
     * @return
     */
    private N33_JXBEntry getAnyJXB(List<N33_JXBEntry> xyList) {
        Integer size = xyList.size();
        Random random = new Random();
        Integer count = random.nextInt(size);
        N33_JXBEntry map = xyList.get(count);
        return map;
    }

    /**
     * 给老师排序
     *
     * @param teaEntryList
     * @param jxbEntryList
     * @return
     */
    private List<N33_TeaEntry> teaSortByJXBCount(List<N33_TeaEntry> teaEntryList, List<N33_JXBEntry> jxbEntryList) {
        List<N33_TeaEntry> list = new ArrayList<N33_TeaEntry>();

        List<N33_TeaDTO> teaDTOList = new ArrayList<N33_TeaDTO>();

        for (N33_TeaEntry teaEntry : teaEntryList) {
            Integer jxbCount = 0;
            for (N33_JXBEntry jxbEntry : jxbEntryList) {
                if ((jxbEntry.getTercherId() != null) && teaEntry.getUserId().toString().equals(jxbEntry.getTercherId().toString())) {
                    if (jxbEntry.getType() == 1 || jxbEntry.getType() == 2) {
                        jxbCount += 100;
                    } else {
                        jxbCount++;
                    }
                }
            }
            N33_TeaDTO teaDTO = new N33_TeaDTO(teaEntry);
            teaDTO.setJxbCount(jxbCount);
            if (jxbCount != 0) {
                teaDTOList.add(teaDTO);
            }
        }

        //排序
        Collections.sort(teaDTOList, new Comparator<N33_TeaDTO>() {
            @Override
            public int compare(N33_TeaDTO o1, N33_TeaDTO o2) {
                return o2.getJxbCount() - o1.getJxbCount();
            }
        });

        for (N33_TeaDTO dto : teaDTOList) {
            list.add(dto.getEntry());
        }
        return list;
    }

    public boolean isConflict(Integer x, Integer y, ObjectId roomId, N33_JXBEntry jxbEntry) {

        //如果非走班课不允许排走班格
        if (jxbEntry.getType() != 1 && jxbEntry.getType() != 2) {
            for (ZouBanTimeEntry zouBanTimeEntry : zouBanTimeEntries) {
                if ((zouBanTimeEntry.getX() - 1) == x && (zouBanTimeEntry.getY() - 1) == y) {
                    return false;
                }
            }
        }

        boolean flag = rulesXY(x, y, jxbEntry, autoPaiKeEntryList);

        if (!flag) {
            return flag;
        }

        //源课表
        for (N33_YKBEntry ykbEntry : ykbEntries) {
            if (ykbEntry.getX() == x && ykbEntry.getY() == y && ykbEntry.getClassroomId().toString().equals(roomId.toString())) {
                if (ykbEntry.getIsUse() == 1) {
                    return false;
                }
            }
        }
        //要排课的教学班，若为单双周课则查出单双周课对应的教学班一起检测冲突
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        jxbIds.add(jxbEntry.getID());
        if (jxbEntry.getType() == 6) {
            jxbIds.add(jxbEntry.getRelativeId());
        }
        //查找所有与对应教学班冲突教学班
        List<ObjectId> ctJxbIds = new ArrayList<ObjectId>();
        if (jxbctEntries != null && jxbctEntries.size() != 0) {
            for (N33_JXBCTEntry jxbctEntry : jxbctEntries) {
                if (jxbIds.contains(jxbctEntry.getJxbId())) {
                    ctJxbIds.add(jxbctEntry.getOjxbId());
                }
            }
        }

        //排课年级所有已排教学班
        List<ObjectId> allJxbIds = new ArrayList<ObjectId>();
        //对应格子的源课表某个教室排了与该教学班冲突的课，返回false
        for (N33_YKBEntry ykbEntry : ykbEntries) {
            if (jxbEntry.getGradeId() != null && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && ykbEntry.getJxbId() != null && ykbEntry.getX() == x && ykbEntry.getY() == y) {
                allJxbIds.add(ykbEntry.getJxbId());
            }
            if (ykbEntry.getX() == x && ykbEntry.getY() == y && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && (ctJxbIds.contains(ykbEntry.getJxbId()) || (ykbEntry.getNJxbId() != null && ctJxbIds.contains(ykbEntry.getNJxbId())))) {
                return false;
            }
        }

        //如果某一个格子放了等级考的学科，那么这个格子同时上课的其他教室必须放等级考的学科
        List<ObjectId> njxbs = new ArrayList<ObjectId>();
        List<ObjectId> njxbs2 = new ArrayList<ObjectId>();
        List<N33_JXBEntry> jxbEntryList = new ArrayList<N33_JXBEntry>();
        if (allJxbIds != null && allJxbIds.size() != 0) {
            for (ObjectId id : allJxbIds) {
                for (N33_JXBEntry jxbEntry1 : jxbEntries) {
                    if (jxbEntry1.getID().toString().equals(id.toString())) {
                        jxbEntryList.add(jxbEntry1);
                    }
                }
            }
            for (N33_JXBEntry entry : jxbEntryList) {
                if (((jxbEntry.getType() == 1) && (entry.getType() != 1)) || ((jxbEntry.getType() == 2) && (entry.getType() != 2))) {
                    njxbs.add(entry.getID());
                } else if ((jxbEntry.getType() == 3 || jxbEntry.getType() == 4 || jxbEntry.getType() == 6) && (entry.getType() != 3 && entry.getType() != 4 && entry.getType() != 6)) {
                    njxbs2.add(entry.getID());
                }
            }
            if (ykbEntries != null && ykbEntries.size() != 0 && ((njxbs != null && njxbs.size() != 0) || (njxbs2 != null && njxbs2.size() != 0))) {
                for (N33_YKBEntry ykbEntry : ykbEntries) {
                    if (jxbEntry.getGradeId() != null && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && njxbs.contains(ykbEntry.getJxbId()) && ykbEntry.getX() == x && ykbEntry.getY() == y) {
                        return false;
                    }
                    if (jxbEntry.getGradeId() != null && ykbEntry.getGradeId() != null && jxbEntry.getGradeId().toString().equals(ykbEntry.getGradeId().toString()) && njxbs2.contains(ykbEntry.getJxbId()) && ykbEntry.getX() == x && ykbEntry.getY() == y) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 检测自定义的自动排课规则
     *
     * @param x
     * @param y
     * @param jxbEntry
     * @param entryList
     * @return
     */
    public boolean rulesXY(Integer x, Integer y, N33_JXBEntry jxbEntry, List<N33_AutoPaiKeEntry> entryList) {
        boolean returnFlag = true;
//        if(x==4&&y==5&&jxbEntry.getType()==2){
//            System.out.println();
//        }
        for (N33_AutoPaiKeEntry autoPaiKeEntry : entryList) {
            List<N33_AutoPaiKeEntry.PaiKeXy> paiKeXyList = autoPaiKeEntry.getXYList();
            //必须排在的位置若不包含xy的位置，则返回false
            if (autoPaiKeEntry.getStatus() == 1) {
                //判断该规则是否包含传入的点
                boolean flag = false;
                for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : paiKeXyList) {
                    if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                        flag = true;
                    }
                }
                if (((jxbEntry.getType() == 1 || jxbEntry.getType() == 2) && autoPaiKeEntry.getType() == 1)
                        || ((jxbEntry.getType() == 3 || jxbEntry.getType() == 6) && autoPaiKeEntry.getType() == 3)
                        || ((jxbEntry.getType() == 4) && autoPaiKeEntry.getType() == 4)) {
                    if (autoPaiKeEntry.getSubIds().contains(jxbEntry.getSubjectId()) && autoPaiKeEntry.getTeaIds().contains(jxbEntry.getTercherId())) {
                        if (!flag) {
                            returnFlag = false;
                        }
                    } else if (autoPaiKeEntry.getSubIds().contains(jxbEntry.getSubjectId())) {
                        if (!flag) {
                            returnFlag = false;
                        }
                    } else if (autoPaiKeEntry.getTeaIds().contains(jxbEntry.getTercherId())) {
                        if (!flag) {
                            returnFlag = false;
                        }
                    } else {
                        if (autoPaiKeEntry.getTeaIds().size() == 0 && autoPaiKeEntry.getSubIds().size() == 0) {
                            if (!flag) {
                                returnFlag = false;
                            }
                        }
                    }
                }
                if (((jxbEntry.getType() == 1 || jxbEntry.getType() == 2) && autoPaiKeEntry.getType() != 1)
                        || ((jxbEntry.getType() == 3 || jxbEntry.getType() == 6) && autoPaiKeEntry.getType() != 3)
                        || ((jxbEntry.getType() == 4) && autoPaiKeEntry.getType() != 4)) {
                    if (flag) {
                        returnFlag = false;
                    }
                }
            } else if (autoPaiKeEntry.getStatus() == 3) {
                //判断该规则是否包含传入的点
                boolean flag = false;
                for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : paiKeXyList) {
                    if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                        flag = true;
                    }
                }
                if (((jxbEntry.getType() == 1 || jxbEntry.getType() == 2) && autoPaiKeEntry.getType() == 1)
                        || ((jxbEntry.getType() == 3 || jxbEntry.getType() == 6) && autoPaiKeEntry.getType() == 3)
                        || ((jxbEntry.getType() == 4) && autoPaiKeEntry.getType() == 4)) {
                    if (autoPaiKeEntry.getSubIds().contains(jxbEntry.getSubjectId()) && autoPaiKeEntry.getTeaIds().contains(jxbEntry.getTercherId())) {
                        if (flag) {
                            returnFlag = false;
                        }
                    } else if (autoPaiKeEntry.getTeaIds().contains(jxbEntry.getTercherId())) {
                        if (flag) {
                            returnFlag = false;
                        }
                    } else if (autoPaiKeEntry.getSubIds().contains(jxbEntry.getSubjectId())) {
                        if (flag) {
                            returnFlag = false;
                        }
                    } else {
                        if (autoPaiKeEntry.getTeaIds().size() == 0 && autoPaiKeEntry.getSubIds().size() == 0) {
                            if (flag) {
                                returnFlag = false;
                            }
                        }
                    }
                }
            }
            if (!returnFlag) {
                return returnFlag;
            }
            /*if((jxbEntry.getType() == 1 || jxbEntry.getType() == 2) && autoPaiKeEntry.getType() == 1){
                //必须排在的位置若不包含xy的位置，则返回false
                if(autoPaiKeEntry.getStatus() == 1){
                    boolean flag = false;
                    for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : paiKeXyList) {
                        if(paiKeXy.getX() == x && paiKeXy.getY() == y){
                            flag = true;
                        }
                    }
                    if(!flag){
                        return flag;
                    }
                }else if(autoPaiKeEntry.getStatus() == 3){
                    for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : paiKeXyList) {
                        if(paiKeXy.getX() == x && paiKeXy.getY() == y){
                            return false;
                        }
                    }
                }
            }*/
            /*if(autoPaiKeEntry.getSubIds().contains(jxbEntry.getSubjectId())){
                for () {

                }
            }*/
        }
        return true;
    }


    /**
     * 返回无冲突的教学班列表List
     *
     * @param sid
     * @param ciId
     * @param gradeId
     * @return
     */
    private List<List<N33_JXBEntry>> getNoCTJXBList(ObjectId sid, ObjectId ciId, ObjectId gradeId) {
        List<List<N33_JXBEntry>> retList = new ArrayList<List<N33_JXBEntry>>();

        //所有的走班教学班
        //jxb add
        int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> zouBanjxbEntryList = jxbDao.getZBJXBList(sid, gradeId, ciId,acClassType);
        List<ObjectId> jxbIdList = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxbEntry : zouBanjxbEntryList) {
            jxbIdList.add(jxbEntry.getID());
        }

        //将第一次循环的所有加入List的教学班移除
        List<N33_JXBEntry> waitRemoveList = new ArrayList<N33_JXBEntry>();

        //教学班冲突
        List<N33_JXBCTEntry> jxbctEntryList = jxbctDao.getJXBCTEntrysByJXBs(sid, jxbIdList, ciId);

        Map<ObjectId, List<ObjectId>> jxbCTMap = getCTJXBIds(zouBanjxbEntryList, jxbctEntryList);

        while (true) {
            //一组无冲突的教学班
            List<N33_JXBEntry> list = new ArrayList<N33_JXBEntry>();
            N33_JXBEntry jxbEntry1 = null;
            List<ObjectId> ctJXBIds = new ArrayList<ObjectId>();
            for (N33_JXBEntry jxbEntry : zouBanjxbEntryList) {
                //判断是第几次循环
                int count = 0;
                if (count == 0) {
                    jxbEntry1 = jxbEntry;
                    ctJXBIds = jxbCTMap.get(jxbEntry1.getID());
                    waitRemoveList.add(jxbEntry);
                    list.add(jxbEntry);
                } else {
                    if (!ctJXBIds.contains(jxbEntry.getID()) && jxbEntry.getType() == jxbEntry1.getType()) {
                        ctJXBIds = combineJXBList(ctJXBIds, jxbCTMap.get(jxbEntry.getID()));
                        waitRemoveList.add(jxbEntry);
                        list.add(jxbEntry);
                    }
                }
                count++;
            }
            retList.add(list);
            zouBanjxbEntryList.removeAll(waitRemoveList);
            if (zouBanjxbEntryList.size() == 0) {
                break;
            }
        }
        return retList;
    }

    public Map<Integer, Integer> getMap(Map<Integer, Integer> map, Map<Integer, Integer> map1, Integer count1) {
        Integer record = 0;
        if (map.get(count1) != null) {
            record = map.get(count1) + 1;
        } else {
            record++;
        }
        //若上一层循环增加一，所有下一层的循环全部置为第一次循环
        for (Map.Entry<Integer, Integer> keyMap : map1.entrySet()) {
            if (keyMap.getKey() > count1) {
                map.put(keyMap.getKey(), 1);
            }
        }

        if (map1.get(count1) < record) {
            count1--;
            getMap(map, map1, count1);
        } else {
            map.put(count1, record);
        }
        return map;
    }

    /**
     * 将所有的组合写入文件
     *
     * @param sid
     * @param ciId
     * @param gradeId
     */
    public void getAllNoCTJXB(ObjectId sid, ObjectId ciId, ObjectId gradeId, Integer type) {
        List<List<List<ObjectId>>> returnList = new ArrayList<List<List<ObjectId>>>();
        //所有的等级教学班
      //jxb add
        int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(sid, gradeId, ciId, type,acClassType);
        if (type == 3) {
            jxbEntryList.addAll(jxbDao.getJXBList(sid, gradeId, ciId,6,acClassType));
        }

        List<ObjectId> jxbIdList = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            jxbIdList.add(jxbEntry.getID());
        }

        //教学班冲突
        List<N33_JXBCTEntry> jxbctEntryList = jxbctDao.getJXBCTEntrysByJXBs(sid, jxbIdList, ciId);

        Map<ObjectId, List<ObjectId>> jxbCTMap = getCTJXBIds(jxbEntryList, jxbctEntryList);

        //记录某一层循环循环到第几次
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        LinkedHashMap<Integer, Integer> map1 = new LinkedHashMap<Integer, Integer>();

        //随机取一个教学班作为根节点
        Integer size = jxbIdList.size();
        Random random = new Random();
        Integer count = random.nextInt(size);
        ObjectId id = jxbIdList.get(count);
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        jxbIds.add(id);
        List<ObjectId> noCTJXBList1 = getNoCTJXB(jxbIds, jxbCTMap, jxbIdList);
        Integer breakFlag = noCTJXBList1.size();
        Integer situation = 0;
        while (true) {
            List<List<ObjectId>> retList = new ArrayList<List<ObjectId>>();

            List<ObjectId> replaceJXBList = new ArrayList<ObjectId>();
            for (ObjectId ids : jxbIdList) {
                replaceJXBList.add(ids);
            }
            //标记第一次进入该循环时才记录map
            Integer a = 0;
            while (true) {
                List<ObjectId> jxbList = new ArrayList<ObjectId>();
                if (a == 0) {
                    jxbList.add(id);
                } else {
                    Integer size1 = replaceJXBList.size();
                    Random random1 = new Random();
                    Integer count1 = random1.nextInt(size1);
                    ObjectId id1 = replaceJXBList.get(count1);
                    jxbList.add(id1);
                }

                List<ObjectId> noCTJXBList = getNoCTJXB(jxbList, jxbCTMap, replaceJXBList);

                List<ObjectId> waitRemoveList = new ArrayList<ObjectId>();
                waitRemoveList.addAll(jxbList);
                List<ObjectId> list = new ArrayList<ObjectId>();
                list.addAll(jxbList);
                Integer count1 = 1;
                while (true) {
                    if (noCTJXBList.size() > 0) {
                        noCTJXBList = 递归(noCTJXBList, list, jxbCTMap, replaceJXBList, map, count1, waitRemoveList, a, situation, map1);
                        count1++;
                    } else {
                        retList.add(list);
                        replaceJXBList.removeAll(waitRemoveList);
                        count1--;
                        /*if(map.get(count1) != null){
                            record = map.get(count1) + 1;
                        }else{
                            record ++;
                        }*/
                        //循环到map1有count1的值或者map1的count1的值已经循环到最大
                        if (a == 0) {
                            while (true) {
                                if (map1.get(count1) == null) {
                                    count1--;
                                } else {
                                    count1--;
                                    getMap(map, map1, count1);
                                    /*count1 --;
                                    //若上一层循环增加一，所有下一层的循环全部置为第一次循环
                                    for (Map.Entry<Integer,Integer> keyMap : map1.entrySet()) {
                                        if(keyMap.getKey() > count1){
                                            map.put(keyMap.getKey(),1);
                                        }
                                    }
                                    if(map.get(count1) != null){
                                        record = map.get(count1) + 1;
                                    }else{
                                        record = 1;
                                    }
                                    map.put(count1,record);*/
                                    break;
                                }
                            }
                            //map.put(count1,record);
                        }
                        break;
                    }
                }

                if (replaceJXBList.size() == 0) {
                    situation++;
                    break;
                }
                a++;
            }
            returnList.add(retList);

            /*String fileName = "G:\\log.txt";
            try {
                FileWriter writer = new FileWriter(fileName, true);
                writer.write("\r\n\r\n\r\n第" + situation + "种情况：" + "\r\n");
                //System.out.println(situation);
                int count2 = 1;
                for (List<ObjectId> list : retList) {
                    writer.write("\r\n第" + count2 + "种组合：");
                    for (ObjectId jxbId : list) {
                        writer.write("教学班ID：" + jxbId.toString());
                    }
                    count2++;
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            if (map.get(1) == breakFlag) {
                break;
            }
            System.out.println(map.values());
        }
    }

    /**
     * 递归加入list所有无冲突的教学班
     *
     * @return
     */
    public List<ObjectId> 递归(List<ObjectId> noCTJXBList, List<ObjectId> list, Map<ObjectId, List<ObjectId>> jxbCTMap, List<ObjectId> jxbIdList, Map<Integer, Integer> map, Integer count1, List<ObjectId> waitRemoveList, Integer a, Integer situation, Map<Integer, Integer> map1) {
        Integer count = 1;
        if (map.get(count1) != null) {
            count = map.get(count1);
        }
        if (count >= noCTJXBList.size()) {
            list.add(noCTJXBList.get(noCTJXBList.size() - 1));
            waitRemoveList.add(noCTJXBList.get(noCTJXBList.size() - 1));
        } else {
            list.add(noCTJXBList.get(count - 1));
            waitRemoveList.add(noCTJXBList.get(count - 1));
        }
        if (a == 0 && situation == 0) {
            map1.put(count1, noCTJXBList.size());
        }
        noCTJXBList = getNoCTJXB(list, jxbCTMap, jxbIdList);
        /*if(a == 0){
            count++;
			map.put(count1,count);
		}*/
        return noCTJXBList;
    }

    /**
     * 查找和传入教学班均无冲突的教学班
     *
     * @param jxbIds
     * @param jxbCTMap
     * @return
     */
    public List<ObjectId> getNoCTJXB(List<ObjectId> jxbIds, Map<ObjectId, List<ObjectId>> jxbCTMap, List<ObjectId> allJXBIds) {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        //所有与传入教学班冲突的教学班
        List<ObjectId> allCTJXBIds = new ArrayList<ObjectId>();
        for (ObjectId id : jxbIds) {
            if (!allCTJXBIds.contains(id)) {
                allCTJXBIds.add(id);
            }
            List<ObjectId> ctJXBIds = jxbCTMap.get(id);
            for (ObjectId ids : allJXBIds) {
                if (ctJXBIds.contains(ids) && !allCTJXBIds.contains(ids)) {
                    allCTJXBIds.add(ids);
                }
            }
        }
        for (ObjectId id : allJXBIds) {
            if (!allCTJXBIds.contains(id)) {
                retList.add(id);
            }
        }
        return retList;
    }

    /**
     * 获得每个教学班的冲突教学班
     *
     * @param jxbEntryList
     * @param jxbctEntryList
     * @return
     */
    public Map<ObjectId, List<ObjectId>> getCTJXBIds(List<N33_JXBEntry> jxbEntryList, List<N33_JXBCTEntry> jxbctEntryList) {
        Map<ObjectId, List<ObjectId>> retMap = new HashMap<ObjectId, List<ObjectId>>();
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            List<ObjectId> ctJXBIds = new ArrayList<ObjectId>();
            for (N33_JXBCTEntry n33_jxbctEntry : jxbctEntryList) {
                if (jxbEntry.getID().toString().equals(n33_jxbctEntry.getJxbId().toString())) {
                    ctJXBIds.add(n33_jxbctEntry.getOjxbId());
                }
            }
            retMap.put(jxbEntry.getID(), ctJXBIds);
        }
        return retMap;
    }

    /**
     * 递归加入list所有无冲突的教学班
     *
     * @return
     */
    private List<ObjectId> 递归(List<ObjectId> noCTJXBList, List<ObjectId> list, Map<ObjectId, List<ObjectId>> jxbCTMap, List<ObjectId> jxbIdList, Map<Integer, Integer> map, Integer count1, List<ObjectId> waitRemoveList, Integer a) {
        Integer count = 0;
        if (map.get(count1) != null) {
            count = map.get(count1);
        }
        if (count >= noCTJXBList.size()) {
            list.add(noCTJXBList.get(noCTJXBList.size() - 1));
            waitRemoveList.add(noCTJXBList.get(noCTJXBList.size() - 1));
        } else {
            list.add(noCTJXBList.get(count));
            waitRemoveList.add(noCTJXBList.get(count));
        }
        noCTJXBList = getNoCTJXB(list, jxbCTMap, jxbIdList);
        if (a == 0) {
            count++;
            map.put(count1, count);
        }
        return noCTJXBList;
    }

    /**
     * 检测传入的教学班集合是否互相存在冲突
     *
     * @param jxbIds
     * @param jxbCTMap
     * @return
     */
    private boolean isOrNotConflict(List<ObjectId> jxbIds, Map<ObjectId, List<ObjectId>> jxbCTMap) {
        for (ObjectId ids : jxbIds) {
            for (ObjectId ids1 : jxbIds) {
                if (!ids.toString().equals(ids1.toString()) && jxbCTMap.get(ids).contains(ids1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将加入第一个教学班的冲突教学班加入上一次的冲突教学班
     *
     * @param yCTJXBids   组合加入教学班之前的冲突教学班
     * @param addJXBCTIds 本次加入的教学班的冲突教学班
     * @return
     */
    private List<ObjectId> combineJXBList(List<ObjectId> yCTJXBids, List<ObjectId> addJXBCTIds) {
        for (ObjectId id : addJXBCTIds) {
            if (!yCTJXBids.contains(id)) {
                yCTJXBids.add(id);
            }
        }
        return yCTJXBids;
    }

    public void addOrUpdateGuiZe(N33_AutoPaiKeDTO dto) {
        if (dto.getId().equals("*")) {
            N33_AutoPaiKeEntry paiKeEntry = dto.getEntry();
            autoPaiKeDao.addEntry(paiKeEntry);
        } else {
            N33_AutoPaiKeEntry paiKeEntry = dto.getEntry();
            autoPaiKeDao.updateN33_AutoPaiKeEntry(paiKeEntry);
        }
    }

    public List<Map<String, Object>> getGuiZeList(ObjectId ciId, ObjectId gid, Integer type, ObjectId sid) {
        List<N33_AutoPaiKeEntry> paiKeEntries = autoPaiKeDao.getEntryList(ciId, gid, type);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIdsForSubId(ciId, gid, sid);
        for (N33_AutoPaiKeEntry paiKeEntry : paiKeEntries) {
            Map<String, Object> dto = new HashMap<String, Object>();
            dto.put("id", paiKeEntry.getID().toString());
            if (paiKeEntry.getType() == 3) {
                dto.put("type", "行政型");
            } else {
                dto.put("type", "走班型");
            }
            String str = "";
            if (paiKeEntry.getTeaIds().size() > 0) {
                List<N33_TeaEntry> teaList = teaDao.findIsolateN33_TeaEntryByuIds(ciId, paiKeEntry.getTeaIds(), gid);
                Integer count = 0;
                for (N33_TeaEntry teaEntry : teaList) {
                    if (count < 5) {
                        str += teaEntry.getUserName() + ",";
                    }
                    count++;
                }
                str += "等" + teaList.size() + "人所带的教学班";
            } else if (paiKeEntry.getSubIds().size() > 0) {
                for (ObjectId ksEntry : paiKeEntry.getSubIds()) {
                    if (ksEntryMap.get(ksEntry) != null) {
                        str += ksEntryMap.get(ksEntry).getSubjectName() + ",";
                    }
                }
                str += "等教学班";
            } else {
                str = dto.get("type") + "教学班";
            }
            dto.put("str", str);
            if (paiKeEntry.getStatus() == 1) {
                dto.put("sta", "必须");
            }
            if (paiKeEntry.getStatus() == 2) {
                dto.put("sta", "优先");
            }
            if (paiKeEntry.getStatus() == 3) {
                dto.put("sta", "拒绝");
            }
            if (paiKeEntry.getStatus() == 4) {
                dto.put("sta", "避免");
            }
            String xyStr = "";
            Integer ct = 0;
            for (N33_AutoPaiKeEntry.PaiKeXy xy : paiKeEntry.getXYList()) {
                if (ct < 5) {
                    xyStr += "周" + (xy.getX() + 1) + "第" + (xy.getY() + 1) + "节,";
                }
                ct++;
            }
            dto.put("day", xyStr + "等" + paiKeEntry.getXYList().size() + "个时间点");
            list.add(dto);
        }
        return list;
    }

    public void delGuiZe(ObjectId id) {
        autoPaiKeDao.removeEntry(id);
    }

    public N33_AutoPaiKeDTO getGuiZe(ObjectId id) {
        N33_AutoPaiKeEntry entry = autoPaiKeDao.findEntryById(id);
        List<N33_TeaEntry> teaList = teaDao.findIsolateN33_TeaEntryByuIds(entry.getXqId(), entry.getTeaIds(), entry.getGradeId());
        String str = "";
        Integer count = 0;
        for (N33_TeaEntry teaEntry : teaList) {
            if (count < 5) {
                str += teaEntry.getUserName() + ",";
            }
            count++;
        }
        str += "等" + teaList.size() + "人";
        N33_AutoPaiKeDTO dto = new N33_AutoPaiKeDTO(entry);
        dto.setTeaStr(str);
        return dto;
    }

    public Boolean get教学班列表是否存在冲突(List<ObjectId> jxbIds, ObjectId jxbId) {
        Boolean resulte = true;
        Integer count = jxbctDao.getJxbCTByjxbIds(jxbId, jxbIds);
        if (count > 0) {
            resulte = false;
        }
        /*for (ObjectId jxbId : jxbIds) {
            Integer count = jxbctDao.getJxbCTByjxbIds(jxbId, jxbIds);
            if (count > 0) {
                resulte = false;
            }
        }*/
        return resulte;
    }
}
