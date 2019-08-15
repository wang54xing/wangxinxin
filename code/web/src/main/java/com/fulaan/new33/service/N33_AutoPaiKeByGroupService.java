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
import com.db.new33.isolate.SubjectDao;
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
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.ZouBanTimeEntry;
import com.pojo.new33.paike.N33_AutoPaiKeEntry;
import com.pojo.new33.paike.N33_JXBCTEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_JXBKSEntry;
import com.pojo.new33.paike.N33_PaiKeZuHeEntry;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.new33.paike.N33_YKBEntry;

@Service
public class N33_AutoPaiKeByGroupService {
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();
    private N33_AutoPkService autoPkService = new N33_AutoPkService();
    private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();
    private SubjectDao subjectDao = new SubjectDao();
    private N33_SWDao swDao = new N33_SWDao();
    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();
    private N33_PaiKeZuHeDao paiKeZuHeDao = new N33_PaiKeZuHeDao();

    private N33_YKBDao ykbDao = new N33_YKBDao();

    private ZouBanTimeDao zouBanTimeDao = new ZouBanTimeDao();
    private N33_ZIXIKEDao n33_zixikeDao = new N33_ZIXIKEDao();
    private N33_GradeWeekRangeDao gradeWeekRangeDao = new N33_GradeWeekRangeDao();
    private N33_AutoPaiKeDao autoPaiKeDao = new N33_AutoPaiKeDao();
    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();
    
    @Autowired
    private PaiKeService paiKeService;

    @Autowired
    private N33_AutoPkService pkService;
    
    @Autowired
    private N33_TurnOffService turnOffService;

    //是否周六日排课
    private N33_GradeWeekRangeEntry gradeWeekRangeEntry = null;
    //所有的排课规则
    private List<N33_AutoPaiKeEntry> autoPaiKeEntryList = null;

    //走班格
    private List<ZouBanTimeEntry> zouBanTimeEntries = null;
    //教学班冲突
    private List<N33_JXBCTEntry> jxbctEntries = null;

    //教学班
    private List<N33_JXBEntry> jxbEntries = null;
    private Map<ObjectId, N33_JXBEntry> jxbEntryMap = null;
    //查询原课表
    private List<N33_YKBEntry> ykbEntries = null;
    //事务
    private List<N33_SWEntry> swEntries = null;
    //初始化总课时
    private Map<ObjectId, Integer> ZongSubjectKs = null;
    //初始化排课课时
    private Map<ObjectId, Integer> PaiKeSubjectKs = null;

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
        //课时
        List<N33_KSEntry> ksEn = subjectDao.getIsolateSubjectEntryByXqid(ciId, schoolId, gradeId);
        List<ObjectId> subIds = new ArrayList<ObjectId>();
        for (N33_KSEntry entry : ksEn) {
            subIds.add(entry.getSubjectId());
        }
        // jxb add 
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add
        jxbEntryMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, subIds,acClassType);
        //事务
        swEntries = swDao.getSwByXqid(xqid);
        //走班格
        zouBanTimeEntries = zouBanTimeDao.getZouBanTimeListByType(schoolId, ciId, gradeId, 0);
        gradeWeekRangeEntry = gradeWeekRangeDao.getGradeWeekRangeByXqid(ciId, schoolId, gradeId);
        autoPaiKeEntryList = autoPaiKeDao.getEntryList(schoolId, ciId, gradeId);
        jxbEntries = jxbDao.getJXBList(schoolId, gradeId, subIds, ciId,acClassType);
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        for (N33_JXBEntry entry : jxbEntries) {
            jxbIds.add(entry.getID());
        }
        //教学班冲突
        jxbctEntries = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, ciId);
        for (int i = 1; i <= 2; i++) {
            //无冲突教学班组合的所有情况
            List<List<List<ObjectId>>> zuHe = getAllNoCTJXB(schoolId, ciId, gradeId, i);
            jxbEntries = jxbDao.getJXBList(schoolId, gradeId, ciId, i,acClassType);
            ZongSubjectKs = autoPkService.initSubjectKS(jxbEntries, ksEntries);
            PaiKeSubjectKs = autoPkService.initJxbPaiKs(jxbEntries);
            //获取一种排课使用的情况组合进行排课
            List<List<ObjectId>> paiKeZuHe = getMinSWCTList(zuHe);
            Map<ObjectId, List<ObjectId>> zuHeMap = new HashMap<ObjectId, List<ObjectId>>();
            //用于整理每一种组合能排的点
            //List<Map<ObjectId,List<ObjectId>>> argsList = new ArrayList<Map<ObjectId, List<ObjectId>>>();
            //为排课情况的每一种教学班组合创建Id，为排序使用
            List<ObjectId> zuHeIds = new ArrayList<ObjectId>();
            for (List<ObjectId> jxbList : paiKeZuHe) {
                ObjectId id = new ObjectId();
                zuHeIds.add(id);
                zuHeMap.put(id, jxbList);
                //argsList.add(zuHeMap);
            }

            for (ObjectId id : zuHeIds) {
                for (; ; ) {
                    if (AutoPK(zuHeMap, zuHeIds, id)) {
                        break;
                    }
                }
            }
            for (N33_YKBEntry entry : ykbEntries) {
                if (entry.getIsUse() == 1 && entry.getGradeId().toString().equals(gradeId.toString())) {
                    ykbDao.updateN33_YKB(entry);
                }
            }
        }
        List<N33_JXBKSEntry> entries = new ArrayList<N33_JXBKSEntry>();
        for (N33_JXBEntry jxb : jxbEntries) {
            Integer jxbCount = PaiKeSubjectKs.get(jxb.getID());
//            if(jxb.getID().toString().equals("5b3b24898fb25af3aa90e826")){
                /*System.out.println("教学班最终课时");
                System.out.println(jxb.getName()+":"+ZongSubjectKs.get(jxb.getID()));
				System.out.println(jxb.getName()+":"+jxbCount);*/
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

    /**
     * 排课
     *
     * @param zuHe
     * @return
     */
    public boolean AutoPK(Map<ObjectId, List<ObjectId>> zuHe, List<ObjectId> zuHeIds, ObjectId zuHeId) {
        //获得一种情况每种组合应该排的点
        Map<ObjectId, List<PaiKeXyDto>> xyListMap = getZuHeKPXY(zuHe);
        //获得每一个组合中的点可排组合的数量
        Map<ObjectId, List<PaiKeXyDto>> zuHeDianCountMap = getZuHeDianCountList(xyListMap, zuHeIds, zuHe);
        //排序
        Map<ObjectId, List<PaiKeXyDto>> zuHeDianCountPaiXuMap = getSortZuHeDian(zuHeDianCountMap, zuHeIds);

        //老师教室排课点
        List<PaiKeXyDto> xyDtoList = zuHeDianCountPaiXuMap.get(zuHeId);

        for (PaiKeXyDto xyDto : xyDtoList) {
            //某个组合所在教室排课教学班
            List<N33_JXBEntry> zuHeJXBList = new ArrayList<N33_JXBEntry>();
            for (ObjectId jxbIds : zuHe.get(zuHeId)) {
                zuHeJXBList.add(jxbEntryMap.get(jxbIds));
            }
            //课时已经排满的教学班
            List<N33_JXBEntry> jxbEntryList = new ArrayList<N33_JXBEntry>();
            //List<ObjectId> waitRemoveJXBIds = new ArrayList<ObjectId>();
            for (N33_JXBEntry jxbEntry : zuHeJXBList) {
                Integer ZongCount = ZongSubjectKs.get(jxbEntry.getID());
                Integer PaiCount = PaiKeSubjectKs.get(jxbEntry.getID());
                if (ZongCount == PaiCount) {
                    //waitRemoveJXBIds.add(jxbEntry.getID());
                    jxbEntryList.add(jxbEntry);
                }
            }
            zuHeJXBList.removeAll(jxbEntryList);
            //zuHe.get(zuHeId).removeAll(waitRemoveJXBIds);

            //当前点是否可以被排课
            Integer x = xyDto.getX();
            Integer y = xyDto.getY();
            if (isConflictByGroup(x, y, zuHe.get(zuHeId)) && zuHeJXBList.size() > 0) {
                for (N33_JXBEntry jxbEntry : zuHeJXBList) {
                    updateYkbEntry(jxbEntry, x, y);
                }
                return false;
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

    public Map<ObjectId, List<PaiKeXyDto>> getSortZuHeDian(Map<ObjectId, List<PaiKeXyDto>> zuHeDianCountMap, List<ObjectId> zuHeIds) {
        Map<ObjectId, List<PaiKeXyDto>> idListMap = new HashMap<ObjectId, List<PaiKeXyDto>>();
        for (ObjectId zuHeId : zuHeIds) {
            //教师在教室能排课的点
            List<PaiKeXyDto> zuHeDian = zuHeDianCountMap.get(zuHeId);
            //排序
            Collections.sort(zuHeDian, new Comparator<PaiKeXyDto>() {
                @Override
                public int compare(PaiKeXyDto o1, PaiKeXyDto o2) {
                    return o1.getCount() - o2.getCount();
                }
            });
            idListMap.put(zuHeId, zuHeDian);
        }
        return idListMap;
    }

    /**
     * @param xyListMap
     * @param zuHeIds
     * @param zuHe
     * @return
     */
    public Map<ObjectId, List<PaiKeXyDto>> getZuHeDianCountList(Map<ObjectId, List<PaiKeXyDto>> xyListMap, List<ObjectId> zuHeIds, Map<ObjectId, List<ObjectId>> zuHe) {
        Map<ObjectId, List<PaiKeXyDto>> retMap = new HashMap<ObjectId, List<PaiKeXyDto>>();

        for (ObjectId zuHeId : zuHeIds) {
            List<ObjectId> zuHeJXBIds = zuHe.get(zuHeId);
            //组合能排课的点
            List<PaiKeXyDto> xyDtoList = xyListMap.get(zuHeId);
            for (PaiKeXyDto xyDto : xyDtoList) {
                //计算当前点在教师的老师可用数量
                Integer count = 0;
                for (List<PaiKeXyDto> xyDtos : xyListMap.values()) {
                    for (PaiKeXyDto xyDto1 : xyDtos) {
                        if (xyDto.equals(xyDto1)) {
                            count++;
                        }
                    }
                }
                xyDto.setCount(count);

                for (ZouBanTimeEntry zouBanTimeEntry : zouBanTimeEntries) {
                    if ((zouBanTimeEntry.getX() - 1) == xyDto.getX() && (zouBanTimeEntry.getY() - 1) == xyDto.getY()) {
                        xyDto.setCount(-998);
                    }
                }

                //过滤配置
                for (N33_AutoPaiKeEntry autoPaiKeEntry : autoPaiKeEntryList) {
                    if (autoPaiKeEntry.getType() != 4) {
                        for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                            if (paiKeXy.getX() == xyDto.getX() && paiKeXy.getY() == xyDto.getY()) {
                                xyDto.setCount(-99);
                                break;
                            }
                        }
                    } else {
                        for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                            if (paiKeXy.getX() == xyDto.getX() && paiKeXy.getY() == xyDto.getY()) {
                                xyDto.setCount(9999);
                                break;
                            }
                        }
                    }

                    for (ObjectId jxbId : zuHeJXBIds) {
                        if (autoPaiKeEntry.getTeaIds().contains(jxbEntryMap.get(jxbId).getTercherId())) {
                            //处理优先的
                            if (autoPaiKeEntry.getStatus() == 2) {
                                for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                                    if (paiKeXy.getX() == xyDto.getX() && paiKeXy.getY() == xyDto.getY()) {
                                        xyDto.setCount(-997);
                                        break;
                                    }
                                }
                            }
                            if (autoPaiKeEntry.getStatus() == 1) {
                                for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                                    if (paiKeXy.getX() == xyDto.getX() && paiKeXy.getY() == xyDto.getY()) {
                                        xyDto.setCount(-999);
                                        break;
                                    }
                                }
                            }
                        }

                        if (autoPaiKeEntry.getSubIds().contains(jxbEntryMap.get(jxbId).getSubjectId())) {
                            //处理优先的
                            if (autoPaiKeEntry.getStatus() == 2) {
                                for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                                    if (paiKeXy.getX() == xyDto.getX() && paiKeXy.getY() == xyDto.getY()) {
                                        xyDto.setCount(-997);
                                        break;
                                    }
                                }
                            }
                            if (autoPaiKeEntry.getStatus() == 1) {
                                for (N33_AutoPaiKeEntry.PaiKeXy paiKeXy : autoPaiKeEntry.getXYList()) {
                                    if (paiKeXy.getX() == xyDto.getX() && paiKeXy.getY() == xyDto.getY()) {
                                        xyDto.setCount(-999);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            retMap.put(zuHeId, xyDtoList);
        }
        return retMap;
    }

    public Map<String, Object> getStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> status = (Map<String, Object>) session.getAttribute("zuhe");
        if (status != null && status.get("st") != null && (Integer) status.get("st") == -1) {
            session.removeAttribute("status");
        }
        return status;
    }

    public void getAllNoCTJXBZuHe(ObjectId sid, ObjectId ciId, ObjectId gradeId, int type, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> status = new HashMap<String, Object>();
        status.put("st", 1);
        session.setAttribute("zuhe", status);
        List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByXqid(ciId, sid, gradeId, type);
        List<ObjectId> subId = new ArrayList<ObjectId>();
        for (N33_KSEntry ksEntry : ksEntries) {
            subId.add(ksEntry.getSubjectId());
        }
        //所有对应走班类型的教学班
        //jxb add
        int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gradeId, subId, ciId, type,acClassType);
        if (type == 3) {
            jxbEntries.addAll(jxbDao.getJXBList(sid, gradeId, subId, ciId, 6,acClassType));
        }
        //教室容量
        Integer roomSize = classroomDao.getRoomEntryListByXqGrade(ciId, sid, gradeId).size();
        //创建的组合
        List<N33_PaiKeZuHeEntry> paiKeZuHeEntries = new ArrayList<N33_PaiKeZuHeEntry>();
        while (true) {
            if (jxbEntries.size() == 0) {
                break;
            }
            //随机一个教学班
            N33_JXBEntry jxbEntry = getAnyJXB(jxbEntries);
            //删除随机的
            if (jxbEntry.getDanOrShuang() != 0) {
                N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(jxbEntry.getRelativeId());
                jxbEntries.remove(jxbEntry1);
            }
            jxbEntries.remove(jxbEntry);
            if (paiKeZuHeEntries.size() == 0) {
                List<ObjectId> jxbIds = new ArrayList<ObjectId>();
                jxbIds.add(jxbEntry.getID());
                N33_PaiKeZuHeEntry zuHeEntry = new N33_PaiKeZuHeEntry("组合" + (paiKeZuHeEntries.size() + 1), sid, ciId, jxbIds, type, gradeId);
                paiKeZuHeEntries.add(zuHeEntry);
            } else {
                //能放入第几个组合中
                Integer count = -1;
                for (Integer size = 0; size < paiKeZuHeEntries.size(); size++) {
                    N33_PaiKeZuHeEntry zuHeEntry = paiKeZuHeEntries.get(size);
                    //达到最高容量上限
                    if (zuHeEntry.getJxbIds().size() == roomSize) {
                        continue;
                    }
                    //true 可以 false不可以
                    Boolean isZuHe = pkService.get教学班列表是否存在冲突(zuHeEntry.getJxbIds(), jxbEntry.getID());
                    if (isZuHe) {
                        count = size;
                    }
                }
                if (count != -1) {
                    N33_PaiKeZuHeEntry zuHeEntry = paiKeZuHeEntries.get(count);
                    List<ObjectId> jxbIds = zuHeEntry.getJxbIds();
                    jxbIds.add(jxbEntry.getID());
                    zuHeEntry.setJxbIds(jxbIds);
                } else {
                    List<ObjectId> jxbIds = new ArrayList<ObjectId>();
                    jxbIds.add(jxbEntry.getID());
                    N33_PaiKeZuHeEntry zuHeEntry = new N33_PaiKeZuHeEntry("组合" + (paiKeZuHeEntries.size() + 1), sid, ciId, jxbIds, type, gradeId);
                    paiKeZuHeEntries.add(zuHeEntry);
                }
            }
        }
        paiKeZuHeDao.delete(gradeId, type, ciId);
        if (paiKeZuHeEntries.size() > 0) {
            paiKeZuHeDao.addEntrys(paiKeZuHeEntries);
        }
        status.put("st", -1);
    }

    private N33_JXBEntry getAnyJXB(List<N33_JXBEntry> xyList) {
        Integer size = xyList.size();
        Random random = new Random();
        Integer count = random.nextInt(size);
        N33_JXBEntry map = xyList.get(count);
        return map;
    }

    /**
     * 将所有的组合写入文件
     *
     * @param sid
     * @param ciId
     * @param gradeId
     */
    public List<List<List<ObjectId>>> getAllNoCTJXB(ObjectId sid, ObjectId ciId, ObjectId gradeId, Integer type) {
        List<List<List<ObjectId>>> returnList = new ArrayList<List<List<ObjectId>>>();
        //所有的等级教学班
      //jxb add
        int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(sid, gradeId, ciId, type,acClassType);
        if (type == 3) {
            jxbEntryList.addAll(jxbDao.getJXBList(sid, gradeId, ciId, 6, acClassType));
        }

        List<ObjectId> jxbIdList = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxbEntry : jxbEntryList) {
            jxbIdList.add(jxbEntry.getID());
        }

        //教学班冲突
        List<N33_JXBCTEntry> jxbctEntryList = jxbctDao.getJXBCTEntrysByJXBs(sid, jxbIdList, ciId);

        Map<ObjectId, List<ObjectId>> jxbCTMap = autoPkService.getCTJXBIds(jxbEntryList, jxbctEntryList);

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
        List<ObjectId> noCTJXBList1 = autoPkService.getNoCTJXB(jxbIds, jxbCTMap, jxbIdList);
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

                List<ObjectId> noCTJXBList = autoPkService.getNoCTJXB(jxbList, jxbCTMap, replaceJXBList);

                List<ObjectId> waitRemoveList = new ArrayList<ObjectId>();
                waitRemoveList.addAll(jxbList);
                List<ObjectId> list = new ArrayList<ObjectId>();
                list.addAll(jxbList);
                Integer count1 = 1;
                while (true) {
                    if (noCTJXBList.size() > 0) {
                        noCTJXBList = autoPkService.递归(noCTJXBList, list, jxbCTMap, replaceJXBList, map, count1, waitRemoveList, a, situation, map1);
                        count1++;
                    } else {
                        retList.add(list);
                        replaceJXBList.removeAll(waitRemoveList);
                        count1--;
                        //循环到map1有count1的值或者map1的count1的值已经循环到最大
                        if (a == 0) {
                            while (true) {
                                if (map1.get(count1) == null) {
                                    count1--;
                                } else {
                                    count1--;
                                    autoPkService.getMap(map, map1, count1);
                                    break;
                                }
                            }
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
            if (map.get(1) == breakFlag) {
                break;
            }
            System.out.println(map.values());
        }
        return returnList;
    }

    /**
     * 返回每组事务重合度最高的一种以组排课的情况
     *
     * @param returnList
     * @return
     */
    private List<List<ObjectId>> getMinSWCTList(List<List<List<ObjectId>>> returnList) {
        List<List<ObjectId>> retList = null;
        Integer min = null;
        //循环每一种情况找出最佳情况用于排课
        continueFlag:
        for (List<List<ObjectId>> list : returnList) {
            //记录一种情况中一种组合的可排点的累加
            Integer sum = 0;

            for (List<ObjectId> jxbList : list) {
                //获取一个组合里面的老师
                List<ObjectId> teaIds = new ArrayList<ObjectId>();
                //获取一个组合中的学科
                List<ObjectId> subIds = new ArrayList<ObjectId>();
                for (ObjectId id : jxbList) {
                    if (!teaIds.contains(jxbEntryMap.get(id).getTercherId())) {
                        teaIds.add(jxbEntryMap.get(id).getTercherId());
                    }
                    if (!subIds.contains(jxbEntryMap.get(id).getSubjectId())) {
                        subIds.add(jxbEntryMap.get(id).getSubjectId());
                    }
                }

                //将所有老师和学科必须排的点与拒绝排的点分别放在两个Map里面
                Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> teaMustMap = new HashMap<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>>();
                Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> teaRefuseMap = new HashMap<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>>();
                Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> subMustMap = new HashMap<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>>();
                Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> subRefuseMap = new HashMap<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>>();
                getMap(teaMustMap, teaRefuseMap, subMustMap, subRefuseMap);
                //然后遍历所有的老师和学科判断点是否一致或者冲突，决定是否返回这个List
                List<N33_AutoPaiKeEntry.PaiKeXy> compareTeaMustList = null;
                List<N33_AutoPaiKeEntry.PaiKeXy> compareTeaRefuseList = null;
                for (ObjectId id : teaIds) {
                    //如果老师不存在必须排课的点，循环下一个老师
                    if (teaMustMap.get(id) != null) {
                        if (compareTeaMustList == null) {
                            compareTeaMustList = teaMustMap.get(id);
                        } else {
                            //老师之间必须排课的点如果不一致则放弃该种情况
                            if (teaMustMap.get(id).size() == compareTeaMustList.size() && teaMustMap.get(id).retainAll(compareTeaMustList) && teaMustMap.get(id).size() == compareTeaMustList.size()) {
                                for (ObjectId ids : teaIds) {
                                    //如果一个组合中老师必须排的点和拒绝排的点存在冲突，则舍弃该种情况
                                    if (teaRefuseMap.get(ids) != null) {
                                        if (compareTeaMustList.retainAll(teaRefuseMap.get(ids))) {
                                            continue continueFlag;
                                        }
                                    }
                                }
                                for (ObjectId subId : subIds) {
                                    if (subMustMap.get(subId) != null) {
                                        //老师必须排课的点和学科必须排课的点如果不一致，则放弃该种情况
                                        if (compareTeaMustList.size() == subMustMap.get(subId).size() && subMustMap.get(subId).retainAll(compareTeaMustList) && subMustMap.get(subId).size() == compareTeaMustList.size()) {
                                            //如果一个组合中的老师必排的点和学科拒绝排的点存在冲突，则舍弃这种情况
                                            for (ObjectId subId1 : subIds) {
                                                if (subRefuseMap.get(subId1) != null) {
                                                    if (compareTeaMustList.retainAll(subRefuseMap.get(subId1))) {
                                                        continue continueFlag;
                                                    }
                                                }
                                            }
                                        } else {
                                            continue continueFlag;
                                        }
                                    }
                                }
                            } else {
                                //如果一个组合中每个老师必须排课的点存在不一样的点则该种情况舍弃
                                continue continueFlag;
                            }
                        }
                    }

                    //如果老师不存在拒绝排课的点，则循环下一个老师
                    if (teaRefuseMap.get(id) != null) {
                        compareTeaRefuseList = teaRefuseMap.get(id);

                        //如果组合中老师拒绝排课的点和必须排课的点存在冲突则舍弃该种情况
                        for (ObjectId teaId : teaIds) {
                            if (teaMustMap.get(teaId) != null) {
                                if (compareTeaRefuseList.retainAll(teaMustMap.get(teaId))) {
                                    continue continueFlag;
                                }
                            }
                        }

                        //如果一个组合中的老师拒绝排课的点和学科必须排的点存在冲突，则舍弃这种情况
                        for (ObjectId subId : subIds) {
                            if (subMustMap.get(subId) != null) {
                                if (compareTeaRefuseList.retainAll(subMustMap.get(subId))) {
                                    continue continueFlag;
                                }
                            }
                        }
                    }
                }

                List<N33_AutoPaiKeEntry.PaiKeXy> compareSubMustList = null;
                List<N33_AutoPaiKeEntry.PaiKeXy> compareSubRefuseList = null;
                for (ObjectId subId : subIds) {
                    //如果学科不存在必须排课的点，循环下一个学科
                    if (subMustMap.get(subId) != null) {
                        if (compareSubMustList == null) {
                            compareSubMustList = subMustMap.get(subId);
                        } else {
                            //学科之间必须排课的点如果不一致则放弃该种情况
                            if (subMustMap.get(subId).size() == compareSubMustList.size() && subMustMap.get(subId).retainAll(compareSubMustList) && subMustMap.get(subId).size() == compareSubMustList.size()) {
                                for (ObjectId ids : subIds) {
                                    //如果一个组合中学科必须排的点和拒绝排的点存在冲突，则舍弃该种情况
                                    if (subRefuseMap.get(ids) != null) {
                                        if (compareSubMustList.retainAll(subRefuseMap.get(ids))) {
                                            continue continueFlag;
                                        }
                                    }
                                }
                                for (ObjectId teaId : teaIds) {
                                    if (teaMustMap.get(teaId) != null) {
                                        //学科必须排课的点和老师必须排课的点如果不一致，则放弃该种情况
                                        if (compareSubMustList.size() == teaMustMap.get(teaId).size() && teaMustMap.get(teaId).retainAll(compareSubMustList) && teaMustMap.get(teaId).size() == compareSubMustList.size()) {
                                            //如果一个组合中的学科必排的点和老师拒绝排的点存在冲突，则舍弃这种情况
                                            for (ObjectId teaId1 : teaIds) {
                                                if (teaRefuseMap.get(teaId1) != null) {
                                                    if (compareSubMustList.retainAll(teaRefuseMap.get(teaId1))) {
                                                        continue continueFlag;
                                                    }
                                                }
                                            }
                                        } else {
                                            continue continueFlag;
                                        }
                                    }
                                }
                            } else {
                                //如果一个组合中每个老师必须排课的点存在不一样的点则该种情况舍弃
                                continue continueFlag;
                            }
                        }
                    }

                    //如果学科不存在拒绝排课的点，则循环下一个学科
                    if (subRefuseMap.get(subId) != null) {

                        compareSubRefuseList = subRefuseMap.get(subId);

                        //如果组合中学科拒绝排课的点和必须排课的点存在冲突则舍弃该种情况
                        for (ObjectId id : subIds) {
                            if (subMustMap.get(id) != null) {
                                if (compareSubRefuseList.retainAll(subMustMap.get(id))) {
                                    continue continueFlag;
                                }
                            }
                        }

                        //如果一个组合中的学科拒绝排课的点和老师必须排的点存在冲突，则舍弃这种情况
                        for (ObjectId teaId : teaIds) {
                            if (teaMustMap.get(teaId) != null) {
                                if (compareSubRefuseList.retainAll(teaMustMap.get(teaId))) {
                                    continue continueFlag;
                                }
                            }
                        }
                    }
                }
                //每一种情况算出每一个组合的点
                sum += caclKPKCount(jxbList, teaMustMap, teaRefuseMap, subMustMap, subRefuseMap);
            }
            if (min == null) {
                min = sum;
                retList = list;
            } else {
                if (sum < min) {
                    min = sum;
                    retList = list;
                }
            }
        }
        return retList;
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
     * 查询所有组合可以排课的点
     *
     * @param zuHeMap
     * @return
     */
    public Map<ObjectId, List<PaiKeXyDto>> getZuHeKPXY(Map<ObjectId, List<ObjectId>> zuHeMap) {
        Map<ObjectId, List<PaiKeXyDto>> retMap = new HashMap<ObjectId, List<PaiKeXyDto>>();
        for (Map.Entry<ObjectId, List<ObjectId>> entry : zuHeMap.entrySet()) {
            List<ObjectId> jxbIds = entry.getValue();
            List<PaiKeXyDto> zuHeKePaiXYList = null;
            for (ObjectId id : jxbIds) {
                N33_JXBEntry jxbEntry = jxbEntryMap.get(id);
                ObjectId teaId = jxbEntry.getTercherId();
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
            retMap.put(entry.getKey(), zuHeKePaiXYList);
        }
        return retMap;
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
            ObjectId teaId = jxbEntry.getTercherId();
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
     * 检测每种情况的所有教学班集合不能排的点
     *
     * @param teaMustMap
     * @param teaRefuseMap
     * @param subMustMap
     * @param subRefuseMap
     * @return
     */
    private Integer caclKPKCount(List<ObjectId> jxbList, Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> teaMustMap, Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> teaRefuseMap, Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> subMustMap, Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> subRefuseMap) {
        //获取一个组合里面的老师
        List<ObjectId> teaIds = new ArrayList<ObjectId>();
        //获取一个组合中的学科
        List<ObjectId> subIds = new ArrayList<ObjectId>();
        for (ObjectId id : jxbList) {
            if (!teaIds.contains(jxbEntryMap.get(id).getTercherId())) {
                teaIds.add(jxbEntryMap.get(id).getTercherId());
            }
            if (!subIds.contains(jxbEntryMap.get(id).getSubjectId())) {
                subIds.add(jxbEntryMap.get(id).getSubjectId());
            }
        }

        List<N33_AutoPaiKeEntry.PaiKeXy> xyList = new ArrayList<N33_AutoPaiKeEntry.PaiKeXy>();
        for (N33_SWEntry swEntry : swEntries) {
            for (ObjectId id : teaIds) {
                if (swEntry.getTeacherIds().contains(id)) {
                    N33_AutoPaiKeEntry.PaiKeXy xy = new N33_AutoPaiKeEntry.PaiKeXy();
                    xy.setX(swEntry.getY() - 1);
                    xy.setY(swEntry.getX() - 1);
                    if (!xyList.contains(xy)) {
                        xyList.add(xy);
                    }
                }
            }
        }

        for (N33_AutoPaiKeEntry autoPaiKeEntry : autoPaiKeEntryList) {
            if (autoPaiKeEntry.getStatus() == 3) {
                for (ObjectId teaId : teaIds) {
                    if (autoPaiKeEntry.getTeaIds().contains(teaId)) {
                        for (N33_AutoPaiKeEntry.PaiKeXy xy : autoPaiKeEntry.getXYList()) {
                            if (!xyList.contains(xy)) {
                                xyList.add(xy);
                            }
                        }
                    }
                }

                for (ObjectId subId : subIds) {
                    if (autoPaiKeEntry.getSubIds().contains(subId)) {
                        for (N33_AutoPaiKeEntry.PaiKeXy xy : autoPaiKeEntry.getXYList()) {
                            if (!xyList.contains(xy)) {
                                xyList.add(xy);
                            }
                        }
                    }
                }
            }
        }
        return xyList.size();
    }

    /**
     * 将所有老师和学科必须排的点与拒绝排的点分别放在两个Map里面
     *
     * @param teaMustMap
     * @param teaRefuseMap
     * @param subMustMap
     * @param subRefuseMap
     */
    private void getMap(Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> teaMustMap, Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> teaRefuseMap, Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> subMustMap, Map<ObjectId, List<N33_AutoPaiKeEntry.PaiKeXy>> subRefuseMap) {
        for (N33_AutoPaiKeEntry autoPaiKeEntry : autoPaiKeEntryList) {
            if (autoPaiKeEntry.getStatus() == 1) {
                if (autoPaiKeEntry.getSubIds() != null && autoPaiKeEntry.getSubIds().size() > 0) {
                    for (ObjectId subId : autoPaiKeEntry.getSubIds()) {
                        if (subMustMap.get(subId) != null) {
                            List<N33_AutoPaiKeEntry.PaiKeXy> xyList = subMustMap.get(subId);
                            for (N33_AutoPaiKeEntry.PaiKeXy xy : autoPaiKeEntry.getXYList()) {
                                if (!xyList.contains(xy)) {
                                    xyList.add(xy);
                                }
                            }
                            subMustMap.put(subId, xyList);
                        } else {
                            subMustMap.put(subId, autoPaiKeEntry.getXYList());
                        }
                    }
                }
                if (autoPaiKeEntry.getTeaIds() != null && autoPaiKeEntry.getTeaIds().size() > 0) {
                    for (ObjectId teaId : autoPaiKeEntry.getTeaIds()) {
                        if (teaMustMap.get(teaId) != null) {
                            List<N33_AutoPaiKeEntry.PaiKeXy> xyList = teaMustMap.get(teaId);
                            for (N33_AutoPaiKeEntry.PaiKeXy xy : autoPaiKeEntry.getXYList()) {
                                if (!xyList.contains(xy)) {
                                    xyList.add(xy);
                                }
                            }
                            teaMustMap.put(teaId, xyList);
                        } else {
                            teaMustMap.put(teaId, autoPaiKeEntry.getXYList());
                        }
                    }
                }
            } else if (autoPaiKeEntry.getStatus() == 3) {
                if (autoPaiKeEntry.getSubIds() != null && autoPaiKeEntry.getSubIds().size() > 0) {
                    for (ObjectId subId : autoPaiKeEntry.getSubIds()) {
                        if (subRefuseMap.get(subId) != null) {
                            List<N33_AutoPaiKeEntry.PaiKeXy> xyList = subRefuseMap.get(subId);
                            for (N33_AutoPaiKeEntry.PaiKeXy xy : autoPaiKeEntry.getXYList()) {
                                if (!xyList.contains(xy)) {
                                    xyList.add(xy);
                                }
                            }
                            subRefuseMap.put(subId, xyList);
                        } else {
                            subRefuseMap.put(subId, autoPaiKeEntry.getXYList());
                        }
                    }
                }
                if (autoPaiKeEntry.getTeaIds() != null && autoPaiKeEntry.getTeaIds().size() > 0) {
                    for (ObjectId teaId : autoPaiKeEntry.getTeaIds()) {
                        if (teaRefuseMap.get(teaId) != null) {
                            List<N33_AutoPaiKeEntry.PaiKeXy> xyList = teaRefuseMap.get(teaId);
                            for (N33_AutoPaiKeEntry.PaiKeXy xy : autoPaiKeEntry.getXYList()) {
                                if (!xyList.contains(xy)) {
                                    xyList.add(xy);
                                }
                            }
                            teaRefuseMap.put(teaId, xyList);
                        } else {
                            teaRefuseMap.put(teaId, autoPaiKeEntry.getXYList());
                        }
                    }
                }
            }
        }
    }
}
