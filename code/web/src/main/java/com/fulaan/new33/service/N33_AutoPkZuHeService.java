package com.fulaan.new33.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.N33_GradeWeekRangeDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.SubjectDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.isolate.ZouBanTimeDao;
import com.db.new33.paike.N33_AutoPaiKeDao;
import com.db.new33.paike.N33_JXBCTDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_JXBKSDao;
import com.db.new33.paike.N33_PaiKeZuHeDao;
import com.db.new33.paike.N33_SWDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZIXIKEDao;
import com.db.new33.paike.PaiKeXyDto;
import com.fulaan.new33.dto.N33_PaiKeZuHeDTO;
import com.fulaan.new33.dto.isolate.N33_GDSXDTO;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.ZouBanTimeEntry;
import com.pojo.new33.paike.N33_AutoPaiKeEntry;
import com.pojo.new33.paike.N33_JXBCTEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_JXBKSEntry;
import com.pojo.new33.paike.N33_PaiKeZuHeEntry;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.utils.MongoUtils;

/**
 * Created by albin on 2018/7/17.
 */
@Service
@SuppressWarnings("all")
public class N33_AutoPkZuHeService {
    private N33_TeaDao teaDao = new N33_TeaDao();
    private TermDao termDao = new TermDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();
    private SubjectDao subjectDao = new SubjectDao();
    private N33_SWDao swDao = new N33_SWDao();
    private N33_YKBDao ykbDao = new N33_YKBDao();
    private N33_ZIXIKEDao n33_zixikeDao = new N33_ZIXIKEDao();
    private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();
    private N33_AutoPaiKeDao autoPaiKeDao = new N33_AutoPaiKeDao();
    private ZouBanTimeDao zouBanTimeDao = new ZouBanTimeDao();
    private N33_GradeWeekRangeDao gradeWeekRangeDao = new N33_GradeWeekRangeDao();
    private N33_PaiKeZuHeDao paiKeZuHeDao = new N33_PaiKeZuHeDao();
    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();
    @Autowired
    private N33_GDSXService gdsxService;
    @Autowired
    private PaiKeService paiKeService;
    @Autowired
    private N33_AutoPkService autoPkService;
    
    @Autowired
    private N33_TurnOffService turnOffService;
    
    //查询原课表
    private List<N33_YKBEntry> ykbEntries = null;
    //教学班
    private List<N33_JXBEntry> jxbEntries = null;
    private Map<ObjectId, N33_JXBEntry> jxbEntryMap = null;
    //初始化总课时
    private Map<ObjectId, Integer> ZongSubjectKs = null;
    //初始化排课课时
    private Map<ObjectId, Integer> PaiKeSubjectKs = null;
    //事务
    private List<N33_SWEntry> swEntries = null;
    //是否周六日排课
    private N33_GradeWeekRangeEntry gradeWeekRangeEntry = null;
    //所有的排课规则
    private List<N33_AutoPaiKeEntry> autoPaiKeEntryList = null;
    //走班格
    private List<ZouBanTimeEntry> zouBanTimeEntries = null;
    //组合
    private List<N33_PaiKeZuHeEntry> paiKeZuHeEntries = null;
    //已选组合组合
    private Map<ObjectId, N33_PaiKeZuHeDTO> paiKeZuHeYiXuanMaps = new HashMap<ObjectId, N33_PaiKeZuHeDTO>();
    //排课组合下的教学班
    private List<ObjectId> paiKeZuHeEntryJxbIds = new ArrayList<ObjectId>();
    //教学班冲突
    private List<N33_JXBCTEntry> jxbctEntries = null;
    //是否继续循环
    private Boolean isWhile = true;
    //以及排过不能排的点
    private List<PaiKeXyDto> xyDtosCountListBuNeng = new ArrayList<PaiKeXyDto>();
    //已经均匀过的坐标的y值列表
    private List<Integer> xList = new ArrayList<Integer>();
    //初始的排课点为1
    private Integer pkCount = 0;

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
        jxbEntryMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, subIds,acClassType);
        //初始化排课课时
        ZongSubjectKs = initSubjectKS(jxbEntries, ksEntries);
        PaiKeSubjectKs = initJxbPaiKs(jxbEntries);
        //事务
        swEntries = swDao.getSwByXqid(xqid);
        //走班格
        zouBanTimeEntries = zouBanTimeDao.getZouBanTimeListByType(schoolId, ciId, gradeId, 0);
        gradeWeekRangeEntry = gradeWeekRangeDao.getGradeWeekRangeByXqid(ciId, schoolId, gradeId);
        autoPaiKeEntryList = autoPaiKeDao.getEntryList(schoolId, ciId, gradeId);
        paiKeZuHeEntries = paiKeZuHeDao.getEntryList(gradeId, 0, ciId);
        if (paiKeZuHeEntries.size() > 0) {
            //教学班冲突
            List<ObjectId> jxbIds = new ArrayList<ObjectId>();
            for (N33_JXBEntry entry : jxbEntries) {
                jxbIds.add(entry.getID());
            }
            jxbctEntries = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, ciId);
            PaiKe课时均匀排方法();
            PaiKe非课时均匀排方法();
            for (N33_YKBEntry entry : ykbEntries) {
                if (entry.getGradeId() != null) {
                    if (entry.getIsUse() == 1 && entry.getGradeId().toString().equals(gradeId.toString())) {
                        ykbDao.updateN33_YKB(entry);
                    }
                }
                if (entry.getGradeId() == null && entry.getIsUse() == 1) {
                    entry.setIsUse(0);
                    entry.setJxbId(null);
                    entry.setNJxbId(null);
                    entry.setSubjectId(null);
                    entry.setTeacherId(null);
                    entry.setNTeacherId(null);
                    entry.setNSubjectId(null);
                    ykbDao.updateN33_YKB(entry);
                }
            }
            //教学班课时计算
            List<N33_JXBKSEntry> entries = new ArrayList<N33_JXBKSEntry>();
            for (N33_JXBEntry jxb : jxbEntries) {
                Integer jxbCount = PaiKeSubjectKs.get(jxb.getID());
////            if(jxb.getID().toString().equals("5b3b24898fb25af3aa90e826")){
//            System.out.println("教学班最终课时");
//            System.out.println(jxb.getName()+":"+ZongSubjectKs.get(jxb.getID()));
//            System.out.println(jxb.getName()+":"+jxbCount);
//            }
                if (jxbCount != 0) {
                    N33_JXBKSEntry n33_jxbksEntry = new N33_JXBKSEntry(jxb.getID(), jxb.getSubjectId(), jxbCount, jxb.getGradeId(), jxb.getSchoolId(), jxb.getTermId(), jxb.getDanOrShuang());
                    entries.add(n33_jxbksEntry);
                }
            }
            if (entries.size() != 0) {
                jxbksDao.addN33_JXBKSEntrys(entries);
            }
        } else {
            autoPkService.autoPaiKe(schoolId, gradeId, ciId, xqid, request);
        }
        status.put("st", -1);
    }

    public Boolean PaiKe自动化(N33_PaiKeZuHeEntry paiKeZuHeEntry) {
        Integer buChangInt = 0;
        //获得已经当前已经排课多少次
        N33_PaiKeZuHeDTO paiKeZuHeDTO = paiKeZuHeYiXuanMaps.get(paiKeZuHeEntry.getID());
        //计算组合中能够排课的点
        List<PaiKeXyDto> xyDtos = getKPXY(paiKeZuHeEntryJxbIds);
        //过滤能够排课的点
        List<PaiKeXyDto> xyDtosCountList = get组合中可排课的点增加需要排序的特殊字段(xyDtos, paiKeZuHeEntryJxbIds);
        //去除掉不能排课的点
        if (xyDtosCountListBuNeng.size() > 0) {
            List<PaiKeXyDto> paiKeXyDtos = new ArrayList<PaiKeXyDto>();
            for (PaiKeXyDto dto : xyDtosCountList) {
                Boolean isPaiKe = true;
                for (PaiKeXyDto nodto : xyDtosCountListBuNeng) {
                    if (nodto.getX() == dto.getX() && nodto.getY() == dto.getY()) {
                        isPaiKe = false;
                    }
                }
                if (isPaiKe) {
                    paiKeXyDtos.add(dto);
                }
            }
            xyDtosCountList = paiKeXyDtos;
        }
        if (xyDtosCountList.size() == 0) {
            return false;
        }
        //对组合中能够排课的点进行排序
        Collections.sort(xyDtosCountList, new Comparator<PaiKeXyDto>() {
            @Override
            public int compare(PaiKeXyDto o1, PaiKeXyDto o2) {
                return o1.getCount() - o2.getCount();
            }
        });
        for (PaiKeXyDto dto : xyDtosCountList) {
            //去除课时已经排满了的教学班
            List<ObjectId> jxbIds = new ArrayList<ObjectId>();
            for (ObjectId jxbId : paiKeZuHeEntryJxbIds) {
                Integer ZongCount = ZongSubjectKs.get(jxbId);
                Integer PaiCount = PaiKeSubjectKs.get(jxbId);
                if (ZongCount != PaiCount && ZongCount != 0) {
                    jxbIds.add(jxbId);
                }
            }
            paiKeZuHeEntryJxbIds = jxbIds;
            if (paiKeZuHeEntryJxbIds.size() == 0) {
                return false;
            }
            //判断这次排课总一周上多少天课
            Integer weekDay = gradeWeekRangeEntry.getEnd();
            //获得选中组合教学班课时最少的
            Integer keShiCount = getJxbListKeShiMinCount(paiKeZuHeEntryJxbIds);
            //计算步长
            Double buChang = weekDay.doubleValue() / keShiCount.doubleValue();
            //四舍五入取整
            String s = String.valueOf(Math.rint(buChang)).substring(0, 1);
            buChangInt = Integer.parseInt(s);
            //计算当前应该排课的天数是哪天
            if (pkCount >= weekDay) {
                return false;
            }
            //允许排课的点(满足课时均匀规则)
            if (dto.getX() == pkCount && !xList.contains(pkCount)) {
                //当前点是否可以被排课
                Integer x = dto.getX();
                Integer y = dto.getY();
                if (isConflictByGroup(x, y, paiKeZuHeEntryJxbIds)) {
                    for (ObjectId jxbId : paiKeZuHeEntryJxbIds) {
                        N33_JXBEntry jxbEntry = jxbEntryMap.get(jxbId);
                        updateYkbEntry(jxbEntry, x, y);
                    }
                    xList.add(pkCount);
                    paiKeZuHeDTO.setPkCount(paiKeZuHeDTO.getPkCount() + 1);
                    pkCount = pkCount + buChangInt;
                    return true;
                } else {
                    xyDtosCountListBuNeng.add(dto);
                }
            }
        }
        if (paiKeZuHeDTO.getPkCount() == 0) {
            pkCount = pkCount + 1;
        } else {
            pkCount = pkCount + buChangInt;
        }
        return true;
    }

    public void PaiKe自动化非均匀的(N33_PaiKeZuHeDTO paiKeZuHeEntry) {
        //计算组合中能够排课的点
        List<PaiKeXyDto> xyDtos = getKPXY(paiKeZuHeEntryJxbIds);
        //过滤能够排课的点
        List<PaiKeXyDto> xyDtosCountList = get组合中可排课的点增加需要排序的特殊字段(xyDtos, paiKeZuHeEntryJxbIds);
        //对组合中能够排课的点进行排序
        Collections.sort(xyDtosCountList, new Comparator<PaiKeXyDto>() {
            @Override
            public int compare(PaiKeXyDto o1, PaiKeXyDto o2) {
                return o1.getCount() - o2.getCount();
            }
        });
        for (PaiKeXyDto dto : xyDtosCountList) {
            //去除课时已经排满了的教学班
            List<ObjectId> jxbIds = new ArrayList<ObjectId>();
            for (ObjectId jxbId : paiKeZuHeEntryJxbIds) {
                Integer ZongCount = ZongSubjectKs.get(jxbId);
                Integer PaiCount = PaiKeSubjectKs.get(jxbId);
                if (ZongCount != PaiCount && ZongCount != 0) {
                    jxbIds.add(jxbId);
                }
            }
            paiKeZuHeEntryJxbIds = jxbIds;
            //当前点是否可以被排课
            Integer x = dto.getX();
            Integer y = dto.getY();
            if (isConflictByGroup(x, y, paiKeZuHeEntryJxbIds)) {
                for (ObjectId jxbId : paiKeZuHeEntryJxbIds) {
                    N33_JXBEntry jxbEntry = jxbEntryMap.get(jxbId);
                    updateYkbEntry(jxbEntry, x, y);
                }
//                //获得已经当前已经排课多少次
//                N33_PaiKeZuHeDTO paiKeZuHeDTO = paiKeZuHeYiXuanMaps.get(new ObjectId(paiKeZuHeEntry.getId()));
//                paiKeZuHeDTO.setPkCount(paiKeZuHeDTO.getPkCount()+1);
            }
        }
    }

    public void PaiKe非课时均匀排方法() {
        for (N33_PaiKeZuHeDTO dto : paiKeZuHeYiXuanMaps.values()) {
            paiKeZuHeEntryJxbIds = MongoUtils.convertToObjectIdList(dto.getJxbIds());
            PaiKe自动化非均匀的(dto);
        }
    }

    public void PaiKe课时均匀排方法() {
        paiKeZuHeYiXuanMaps = new HashMap<ObjectId, N33_PaiKeZuHeDTO>();
        //自动排课
        while (true) {
            //所有组合已经排课完成，跳出循环
            if (paiKeZuHeEntries.size() <= 0) {
                break;
            }
            //随机找到一个组合
            N33_PaiKeZuHeEntry paiKeZuHeEntry = getAnyZuHe();
            paiKeZuHeYiXuanMaps.put(paiKeZuHeEntry.getID(), new N33_PaiKeZuHeDTO(paiKeZuHeEntry));
            //去除其他组合中存在随机到的这个组合的教学班
            delZuHeByJxbIds(paiKeZuHeEntry.getJxbIds());
            //自动排课
            paiKeZuHeEntryJxbIds = paiKeZuHeEntry.getJxbIds();
            //重置排过课的点
            xyDtosCountListBuNeng = new ArrayList<PaiKeXyDto>();
            xList = new ArrayList<Integer>();
            isWhile = true;
            pkCount = 0;
            while (isWhile) {
                isWhile = PaiKe自动化(paiKeZuHeEntry);
            }
        }
    }

    /**
     * 检测这一组教学班是否存在不能排的教学班
     *
     * @param x
     * @param y
     * @param jxbIds
     * @return
     */
    public boolean isConflictByGroup(Integer x, Integer y, List<ObjectId> jxbIds) {
        for (ObjectId jxbId : jxbIds) {
            N33_JXBEntry jxbEntry = jxbEntryMap.get(jxbId);

            boolean flag = isConflict(x, y, jxbEntry.getClassroomId(), jxbEntry);
            if (!flag) {
                return flag;
            }
        }
        return true;
    }


    /**
     * 修改原课表
     *
     * @param jxbEntry
     * @param x
     * @param y
     */
    public void updateYkbEntry(N33_JXBEntry jxbEntry, Integer x, Integer y) {
        for (N33_YKBEntry ykbEntry : ykbEntries) {
            if (ykbEntry.getClassroomId().toString().equals(jxbEntry.getClassroomId().toString()) && x == ykbEntry.getX() && y == ykbEntry.getY()) {
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

    public boolean isConflict(Integer x, Integer y, ObjectId roomId, N33_JXBEntry jxbEntry) {
        //如果非走班课不允许排走班格
        if (jxbEntry.getType() != 1 && jxbEntry.getType() != 2) {
            for (ZouBanTimeEntry zouBanTimeEntry : zouBanTimeEntries) {
                if ((zouBanTimeEntry.getX() - 1) == x && (zouBanTimeEntry.getY() - 1) == y) {
                    return false;
                }
            }
        }
        boolean flag = autoPkService.rulesXY(x, y, jxbEntry, autoPaiKeEntryList);
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
     * 计算一组教学班中的最少课时
     *
     * @param jxbIds
     * @return
     */
    public Integer getJxbListKeShiMinCount(List<ObjectId> jxbIds) {
        List<Integer> keShiCounts = new ArrayList<Integer>();
        for (ObjectId jxbId : jxbIds) {
            Integer ZongCount = ZongSubjectKs.get(jxbId);
            keShiCounts.add(ZongCount);
        }
        Collections.sort(keShiCounts, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        return keShiCounts.size() > 0 ? keShiCounts.get(0) : 0;
    }

    public List<PaiKeXyDto> get组合中可排课的点增加需要排序的特殊字段(List<PaiKeXyDto> xyDtos, List<ObjectId> jxbIds) {
        List<PaiKeXyDto> dtos = new ArrayList<PaiKeXyDto>();
        if(xyDtos != null){
            for (PaiKeXyDto dto : xyDtos) {
                Integer x = dto.getX();
                Integer y = dto.getY();
                dto.setCount(0);
                for (ZouBanTimeEntry zouBanTimeEntry : zouBanTimeEntries) {
                    if ((zouBanTimeEntry.getX() - 1) == x && (zouBanTimeEntry.getY() - 1) == y) {
                        dto.setCount(-998);
                    }
                }
                for (N33_AutoPaiKeEntry autoPaiKeEntry : autoPaiKeEntryList) {
                    if (autoPaiKeEntry.getType() != 4) {
                        for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                            if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                                dto.setCount(-99);
                                break;
                            }
                        }
                    } else {
                        for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                            if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                                dto.setCount(9999);
                                break;
                            }
                        }
                    }
                    //获得老师列表
                    List<ObjectId> TeaIds = new ArrayList<ObjectId>();
                    for (ObjectId id : jxbIds) {
                        N33_JXBEntry jxbEntry = jxbEntryMap.get(id);
                        if (jxbEntry != null) {
                            if (!TeaIds.contains(jxbEntry.getTercherId())) {
                                TeaIds.add(jxbEntry.getTercherId());
                            }
                        }
                    }
                    List<ObjectId> autoPaiKeTeaIds = autoPaiKeEntry.getTeaIds();
                    autoPaiKeTeaIds.removeAll(TeaIds);
                    //存在教师的规则
                    if (autoPaiKeEntry.getTeaIds().size() != autoPaiKeTeaIds.size()) {
                        //处理优先的
                        if (autoPaiKeEntry.getStatus() == 2) {
                            for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                                if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                                    dto.setCount(-997);
                                    break;
                                }
                            }
                        }
                        if (autoPaiKeEntry.getStatus() == 1) {
                            for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                                if (paiKeXy.getX() == x && paiKeXy.getY() == y) {
                                    dto.setCount(-999);
                                    break;
                                }
                            }
                        }
                    }
                }
                dtos.add(dto);
            }
        }
        return dtos;
    }

    /**
     * 计算一组无冲突的教学班可排课的点
     *
     * @param jxbIds
     * @return
     */
    public List<PaiKeXyDto> getKPXY(List<ObjectId> jxbIds) {
        //List<N33_AutoPaiKeEntry.PaiKeXy> retXY = new ArrayList<N33_AutoPaiKeEntry.PaiKeXy>();
        List<PaiKeXyDto> zuHeKePaiXYList = null;
        for (ObjectId id : jxbIds) {
            N33_JXBEntry jxbEntry = jxbEntryMap.get(id);
            ObjectId xqid = termDao.getTermByTimeId(jxbEntry.getTermId()).getID();
            ObjectId teaId = jxbEntry.getTercherId();
            N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(teaId, jxbEntry.getTermId());
            //每一个老师可以排课的xy点List
            List<PaiKeXyDto> list = new ArrayList<PaiKeXyDto>();
            //对应老师已经排过课的点
            List<PaiKeXyDto> ypkList = new ArrayList<PaiKeXyDto>();
            //对应教室源课表中的点
            List<PaiKeXyDto> roomXY = new ArrayList<PaiKeXyDto>();
            for (N33_YKBEntry ykbEntry : ykbEntries) {
                PaiKeXyDto xy = new PaiKeXyDto();
                if (ykbEntry.getClassroomId().toString().equals(jxbEntry.getClassroomId().toString()) && ykbEntry.getX() <= gradeWeekRangeEntry.getEnd()) {
                    xy.setX(ykbEntry.getX());
                    xy.setY(ykbEntry.getY());
                    roomXY.add(xy);
                }
            }
            for (N33_YKBEntry ykbEntry : ykbEntries) {
                //该老师已经排课的点||该教室已经排课的点
                PaiKeXyDto ypkXY = new PaiKeXyDto();
                if ((ykbEntry.getTeacherId() != null && teaId.toString().equals(ykbEntry.getTeacherId().toString())) || (ykbEntry.getNTeacherId() != null && teaId.toString().equals(ykbEntry.getNTeacherId().toString())) || (ykbEntry.getClassroomId().toString().equals(jxbEntry.getClassroomId().toString()) && ykbEntry.getIsUse() == 1)) {
                    ypkXY.setX(ykbEntry.getX());
                    ypkXY.setY(ykbEntry.getY());
                    if (ypkList.contains(ypkXY)) {
                        continue;
                    } else {
                        ypkList.add(ypkXY);
                    }
                }
            }

            List<ObjectId> teaGradeIds = teaEntry.getGradeList();
            List<N33_GDSXDTO> gdsxdtos = new ArrayList<N33_GDSXDTO>();
            for (ObjectId gradeId : teaGradeIds) {
                gdsxdtos.addAll(gdsxService.getGDSXBySidAndXqid(xqid, jxbEntry.getSchoolId(), gradeId));
            }
            if (gdsxdtos != null && gdsxdtos.size() > 0) {
                for (N33_GDSXDTO gdsxdto : gdsxdtos) {
                    PaiKeXyDto ypkXY = new PaiKeXyDto();
                    ypkXY.setX(gdsxdto.getX());
                    ypkXY.setY(gdsxdto.getY());
                    if (ypkList.contains(ypkXY)) {
                        continue;
                    } else {
                        ypkList.add(ypkXY);
                    }
                }
            }

            for (N33_SWEntry swEntry : swEntries) {
                //事务占用的点
                PaiKeXyDto ypkXY = new PaiKeXyDto();
                if (swEntry.getTeacherIds().size() == 0 || swEntry.getTeacherIds().contains(teaId)) {
                    ypkXY.setX(swEntry.getY() - 1);
                    ypkXY.setY(swEntry.getX() - 1);
                    if (ypkList.contains(ypkXY)) {
                        continue;
                    } else {
                        ypkList.add(ypkXY);
                    }
                }
            }
            //遍历教室源课表的点，添加该教室老师可排的点
            for (PaiKeXyDto roomMap : roomXY) {
                if (!ypkList.contains(roomMap) && !list.contains(roomMap)) {
                    list.add(roomMap);
                }
            }
            if (zuHeKePaiXYList == null) {
                zuHeKePaiXYList = list;
            } else {
                zuHeKePaiXYList.retainAll(list);
            }
        }
        return zuHeKePaiXYList;
    }

    /**
     * *
     * 组合
     *
     * @param zuHeList
     * @return
     */
    private N33_PaiKeZuHeEntry getAnyZuHe() {
        Integer size = paiKeZuHeEntries.size();
        Random random = new Random();
        Integer count = random.nextInt(size);
        N33_PaiKeZuHeEntry map = paiKeZuHeEntries.get(count);
        return map;
    }

    /**
     * *
     * 重置组合,删除已经选中的组合中的教学班
     *
     * @param jxbList
     */
    private void delZuHeByJxbIds(List<ObjectId> jxbList) {
        List<N33_PaiKeZuHeEntry> zuHeEntryList = new ArrayList<N33_PaiKeZuHeEntry>();
        if(jxbList.size() != 0){
            for (N33_PaiKeZuHeEntry zuHeEntry : paiKeZuHeEntries) {
                Boolean fa = true;
                for (ObjectId jxbId : jxbList) {
                    if (zuHeEntry.getJxbIds().contains(jxbId)) {
                        fa = false;
                    }
                }
                if (fa) {
                    zuHeEntryList.add(zuHeEntry);
                }
            }
        }
        paiKeZuHeEntries = zuHeEntryList;
    }
}
