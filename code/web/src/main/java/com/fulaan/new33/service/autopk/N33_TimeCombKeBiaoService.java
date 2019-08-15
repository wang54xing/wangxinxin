package com.fulaan.new33.service.autopk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.paike.N33_JXBCTDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_JXBKSDao;
import com.db.new33.paike.N33_TimeCombKeBiaoDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZhuanXiangDao;
import com.fulaan.new33.dto.autopk.N33_TimeCombKeBiaoDTO;
import com.fulaan.new33.dto.isolate.CourseRangeDTO;
import com.fulaan.new33.dto.isolate.N33_SWDTO;
import com.fulaan.new33.service.N33_GDSXService;
import com.fulaan.new33.service.N33_PaiKeZuHeService;
import com.fulaan.new33.service.N33_TimeCombineService;
import com.fulaan.new33.service.PaiKeService;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.CourseRangeService;
import com.fulaan.new33.service.isolate.N33_SWService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.fulaan.utils.StringUtil;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.paike.N33_GDSXEntry;
import com.pojo.new33.paike.N33_JXBCTEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_JXBKSEntry;
import com.pojo.new33.paike.N33_TimeCombKeBiaoEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.new33.paike.N33_ZhuanXiangEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

@Service
public class N33_TimeCombKeBiaoService extends BaseService{

    private N33_TimeCombKeBiaoDao timeCombKeBiaoDao = new N33_TimeCombKeBiaoDao();

    private N33_JXBDao jxbDao = new N33_JXBDao();

    private N33_YKBDao ykbDao = new N33_YKBDao();

    private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();

    private N33_JXBCTDao jxbctDao = new N33_JXBCTDao();

    private N33_ZhuanXiangDao zhuanXiangDao = new N33_ZhuanXiangDao();

    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

    private N33_TeaDao teaDao = new N33_TeaDao();

    @Autowired
    private N33_TimeCombineService timeCombineService;

    @Autowired
    private CourseRangeService courseRangeService;

    @Autowired
    private N33_PaiKeZuHeService paiKeZuHeService;

    @Autowired
    private N33_GDSXService gdsxService;

    @Autowired
    private N33_SWService swService;

    @Autowired
    private PaiKeService paiKeService;
    
    @Autowired
    private N33_TurnOffService turnOffService;

    public String saveTimeCombKeBiao(N33_TimeCombKeBiaoDTO dto) {
        String state = "";
        N33_TimeCombKeBiaoEntry entry = dto.buildEntry();
        // jxb add 
	     ObjectId ciId = getDefaultPaiKeTerm(entry.getSchoolId()).getPaikeci();
//	     int acClassType = 0;
//	     N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(entry.getSchoolId(),entry.getGradeId(),ciId,1);
//	     if(null!=turnOff){
//	     	acClassType = turnOff.getAcClass();
//	     }
	     
	     int acClassType = turnOffService.getAcClassType(entry.getSchoolId(), entry.getGradeId(), ciId,1);
         // jxb add

        Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(entry.getSchoolId(), entry.getGradeId(), entry.getCiId(),acClassType);
        Map<String, Object> timeCombMap = timeCombineService.getTimeCombineInfo(entry.getSchoolId(), entry.getGradeId(), entry.getCiId(),
                entry.getType(), entry.getSerial());
        int maxKeShi = timeCombMap.get("maxKeShi")==null?0:(Integer) timeCombMap.get("maxKeShi");
        int finishKs = timeCombKeBiaoDao.getTimeCombFinishKeShiCount(entry.getSchoolId(), entry.getGradeId(), entry.getCiId(), entry.getType(), entry.getSerial());
        if(maxKeShi>finishKs) {
            N33_TimeCombKeBiaoEntry oldEntry = timeCombKeBiaoDao.getTimeCombKeBiaoEntry(entry.getSchoolId(),
                    entry.getGradeId(),
                    entry.getCiId(),
                    entry.getX(),
                    entry.getY());
            Map<ObjectId, N33_YKBEntry> ykbMap = ykbDao.getYKBMap(entry.getX(), entry.getY(), entry.getCiId());
            if (null == oldEntry) {
                Map<ObjectId, Integer> jxbKSMap = timeCombMap.get("jxbKSMap")==null?new HashMap<ObjectId, Integer>():(Map<ObjectId, Integer>)timeCombMap.get("jxbKSMap");
                List<N33_YKBEntry> ykbUpdList = new ArrayList<N33_YKBEntry>();
                Set<ObjectId> jxbIds = jxbKSMap.keySet();
                Map<ObjectId, N33_JXBKSEntry> jxbksMap = jxbksDao.getJXBKSMap(entry.getCiId(), jxbIds);
                List<ObjectId> addKSJxbIds = new ArrayList<ObjectId>();
                List<ObjectId> updKSJxbIds = new ArrayList<ObjectId>();
                for(Map.Entry<ObjectId, Integer> jxbKS:jxbKSMap.entrySet()){
                    int jxbKeShi = jxbKS.getValue();
                    if(jxbKeShi>finishKs) {
                        N33_JXBEntry jxbEntry = jxbMap.get(jxbKS.getKey());
                        if(null!=jxbEntry){
                            ObjectId classRoomId = jxbEntry.getClassroomId();
                            N33_YKBEntry ykbEntry = ykbMap.get(classRoomId);
                            if(null!=ykbEntry) {
                                N33_JXBKSEntry jxbksEntry = jxbksMap.get(jxbEntry.getID());
                                int ypCount = 0;
                                if(null!=jxbksEntry){
                                    ypCount = jxbksEntry.getYpCount();
                                }
                                if ((jxbKeShi!=0&&ypCount >= jxbKeShi)||jxbKeShi==0) {
                                    continue;
                                }
                                if (jxbksEntry == null) {
                                    jxbksEntry = new N33_JXBKSEntry(jxbEntry.getID(), jxbEntry.getSubjectId(), 1, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang());
                                    jxbksEntry.setID(new ObjectId());
                                    jxbksMap.put(jxbEntry.getID(), jxbksEntry);
                                    if(!addKSJxbIds.contains(jxbEntry.getID())) {
                                        addKSJxbIds.add(jxbEntry.getID());
                                    }
                                } else {
                                    jxbksEntry.setYpCount(ypCount+1);
                                    jxbksMap.put(jxbEntry.getID(), jxbksEntry);
                                    if(!addKSJxbIds.contains(jxbEntry.getID())){
                                        if(!updKSJxbIds.contains(jxbEntry.getID())) {
                                            updKSJxbIds.add(jxbEntry.getID());
                                        }
                                    }
                                }
                                ykbEntry.setJxbId(jxbEntry.getID());
                                ykbEntry.setIsUse(Constant.ONE);
                                ykbEntry.setGradeId(jxbEntry.getGradeId());
                                ykbEntry.setSubjectId(jxbEntry.getSubjectId());
                                ykbEntry.setTeacherId(jxbEntry.getTercherId());
                                ykbEntry.setType(jxbEntry.getType());
                                ykbEntry.setTimeCombSerial(entry.getSerial());
                                ykbUpdList.add(ykbEntry);
                            }
                        }
                    }
                }

                if(addKSJxbIds.size()>0){
                    List<N33_JXBKSEntry> addJXBKSList = new ArrayList<N33_JXBKSEntry>();
                    for(ObjectId addId:addKSJxbIds){
                        N33_JXBKSEntry jxbksEntry = jxbksMap.get(addId);
                        if(null!=jxbksEntry){
                            addJXBKSList.add(jxbksEntry);
                        }
                    }
                    if(addJXBKSList.size()>0) {
                        jxbksDao.addN33_JXBKSEntries(addJXBKSList);
                    }
                }
                if(updKSJxbIds.size()>0){
                    for(ObjectId updId:updKSJxbIds){
                        N33_JXBKSEntry jxbksEntry = jxbksMap.get(updId);
                        if(null!=jxbksEntry){
                            jxbksDao.updateN33_JXBKS(jxbksEntry);
                        }
                    }
                }
                if(ykbUpdList.size()>0){
                    for(N33_YKBEntry ykbEntry:ykbUpdList){
                        ykbDao.updateN33_YKB(ykbEntry);
                    }
                }
                timeCombKeBiaoDao.addEntry(entry);
                //添加完成
                state = "0";
            } else {
                //课节已被占用
                state = "1";
            }
        }else{
            //课时已排满
            state = "2";
        }
        return state;
    }

    /**
     * 查询分段组合下固定事务
     * @param schoolId
     * @param gradeId
     * @param xqId
     * @param ciId
     * @param type
     * @param serial
     * @return
     */
    public List<Map<String, Object>> getTimeCombJxbCtList(ObjectId schoolId, ObjectId gradeId, ObjectId xqId, ObjectId ciId, int type, int serial) {
        List<String> xys = new ArrayList<String>();
        Map<String, List<String>> xyMesg = new HashMap<String, List<String>>();
        Map<String, Object> timeCombMap = timeCombineService.getTimeCombineInfo(schoolId, gradeId, ciId, type, serial);
        Map<ObjectId, Integer> jxbKSMap = timeCombMap.get("jxbKSMap")==null?new HashMap<ObjectId, Integer>():(Map<ObjectId, Integer>)timeCombMap.get("jxbKSMap");
        // jxb add 
         //ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
         int acClassType = 0;
         N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId,gradeId,ciId,1);
         if(null!=turnOff){
         	acClassType = turnOff.getAcClass();
         }
         // jxb add
        Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);
        List<ObjectId> jxbTeaIds = new ArrayList<ObjectId>();
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        for(ObjectId jxbId:jxbKSMap.keySet()){
            if(!jxbIds.contains(jxbId)){
                jxbIds.add(jxbId);
            }
            N33_JXBEntry jxbEntry = jxbMap.get(jxbId);
            if(null!=jxbEntry){
                if(null!=jxbEntry.getTercherId()){
                    jxbTeaIds.add(jxbEntry.getTercherId());
                }
            }
        }

        List<N33_SWDTO> swDtos = swService.getGuDingShiWuList(xqId, schoolId);
        for(N33_SWDTO swDto:swDtos){
            String xy = swDto.getX()+";"+swDto.getY();
            if(!xys.contains(xy)){
                xys.add(xy);
            }
            if(swDto.getTids().size()>0) {
                for (String teaId : swDto.getTids()) {
                    if (StringUtil.isEmpty(teaId)) {
                        if(jxbTeaIds.contains(new ObjectId(teaId))){
                            //reList.add(swDto);
                            mapAddXyCtInfos(xyMesg, xy, swDto.getDesc());
                            break;
                        }
                    }
                }
            }else{
                mapAddXyCtInfos(xyMesg, xy, swDto.getDesc());
                //reList.add(swDto);
            }
        }

        //教师map
        Map<ObjectId, N33_TeaEntry> teaMap = teaDao.getTeaMap(ciId, schoolId);

        //固定事项
        Map<ObjectId, List<N33_GDSXEntry>> gradeGDSXsMap = gdsxService.getGradeGDSXEntriesMap(xqId, schoolId);

        for (ObjectId teaId : jxbTeaIds) {
            N33_TeaEntry teaEntry = teaMap.get(teaId);
            if (teaEntry != null) {
                List<ObjectId> teaGradeIds = teaEntry.getGradeList();
                for (ObjectId teaGradeId : teaGradeIds) {
                    List<N33_GDSXEntry> n33_gdsxs = gradeGDSXsMap.get(teaGradeId);
                    if (null == n33_gdsxs) {
                        continue;
                    }
                    for (N33_GDSXEntry gdsxEntry : n33_gdsxs) {
                        String xy = gdsxEntry.getX()+";"+gdsxEntry.getY();
                        if(!xys.contains(xy)){
                            xys.add(xy);
                        }
                        String info = teaEntry.getUserName()+":"+gdsxEntry.getDesc();
                        mapAddXyCtInfos(xyMesg, xy, info);
                    }
                }
            }
        }

        //教学班教学班之间的冲突
        List<ObjectId> allCtJxbIds = new ArrayList<ObjectId>();
        List<N33_JXBCTEntry> jxbctEntries = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, ciId);
        Map<ObjectId, String> jxbCTMsg = new HashMap<ObjectId, String>();
        for (N33_JXBCTEntry jxbctEntry : jxbctEntries) {
            if(!allCtJxbIds.contains(jxbctEntry.getOjxbId())) {
                allCtJxbIds.add(jxbctEntry.getOjxbId());
            }
            String msg = "";
            if (jxbctEntry.getCtType() == 1) {
                msg = "学生冲突";
            } else if (jxbctEntry.getCtType() == 2) {
                msg = "教师冲突";
            } else if (jxbctEntry.getCtType() == 3) {
                msg = "场地冲突";
            }
            N33_JXBEntry jxbEntry = jxbMap.get(jxbctEntry.getOjxbId());
            if(null!=jxbEntry) {
                jxbCTMsg.put(jxbctEntry.getOjxbId(), msg + ":" +jxbEntry.getName());
            }
        }

        //冲突的教学班源课表
        List<N33_YKBEntry> ctYkbEntries = ykbDao.getYKBEntrysByJXBIdsOrNJxbIds(ciId, allCtJxbIds, schoolId);
        for (N33_YKBEntry ykbEntry : ctYkbEntries) {
            if(null!=ykbEntry.getJxbId()&&ykbEntry.getType() != 6){
                String xy = ykbEntry.getX()+";"+ykbEntry.getY();
                if(!xys.contains(xy)){
                    xys.add(xy);
                }
                String info = jxbCTMsg.get(ykbEntry.getJxbId());
                mapAddXyCtInfos(xyMesg, xy, info);
                if(ykbEntry.getNJxbId() != null){
                    String njxbInfo = jxbCTMsg.get(ykbEntry.getNJxbId());
                    mapAddXyCtInfos(xyMesg, xy, njxbInfo);
                }
            }
        }

       // N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId, gradeId, ciId, 1); 06-14 zhushi
        if((turnOff != null && turnOff.getStatus() != 0) || turnOff == null) {
            //本次排课的源课表
            List<N33_YKBEntry> ykbEntries = ykbDao.getYKBEntrysList(ciId, schoolId);
            //如果某一个格子放了等级考的学科，那么这个格子同时上课的其他教室必须放等级考的学科
            for (N33_YKBEntry ykbEntry : ykbEntries) {
                if (ykbEntry.getGradeId() != null && gradeId.equals(ykbEntry.getGradeId()) && ykbEntry.getJxbId() != null) {
                    N33_JXBEntry useJxbEntry = jxbMap.get(ykbEntry.getJxbId());
                    if (useJxbEntry.getType() != 1 && useJxbEntry.getType() != 2) {
                        //非走班格
                        String xy = ykbEntry.getX()+";"+ykbEntry.getY();
                        if(!xys.contains(xy)){
                            xys.add(xy);
                        }
                        mapAddXyCtInfos(xyMesg, xy, "非走班格");
                    }
                }
            }
        }

        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        for(String xy:xys){
            List<String> msgs = xyMesg.get(xy);
            if(null!=msgs){
                String[] arr = xy.split(";");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("x", arr[0]);
                map.put("y", arr[1]);
                map.put("msgs", msgs);
                reList.add(map);
            }
        }
        return reList;
    }

    /**
     * 添加坐标
     * @param map
     * @param xy
     * @param info
     */
    public void mapAddXyCtInfos(Map<String, List<String>> map, String xy, String info){
        if(StringUtil.isEmpty(info)) {
            List<String> stringList = map.get(xy);
            if (null == stringList) {
                stringList = new ArrayList<String>();
            }
            if (!stringList.contains(info)) {
                stringList.add(info);
            }
            map.put(xy, stringList);
        }
    }

    /**
     * 自动分时段排课
     * @param schoolId
     * @param gradeId
     * @param ciId
     */
    public void autoBuildTimeCombKeBiao(ObjectId schoolId, ObjectId gradeId, ObjectId xqId, ObjectId ciId) {
        List<IdNameValuePairDTO> weekDays = paiKeZuHeService.getGradeWeek(gradeId, ciId, schoolId);
        List<CourseRangeDTO> courseRanges = courseRangeService.getListBySchoolId(schoolId.toString(), ciId);

        List<Integer> xList = new ArrayList<Integer>();
        Map<Integer, List<Integer>> xyListMap = new HashMap<Integer, List<Integer>>();
        for(int x = 0; x<weekDays.size(); x++){
            xList.add(x);
            List<Integer> yList = xyListMap.get(x);
            if(null==yList){
                yList = new ArrayList<Integer>();
            }
            for(int y = 0; y<courseRanges.size(); y++){
                yList.add(y);
            }
            xyListMap.put(x, yList);
        }

        Map<Integer, List<Map<String, Object>>> typeTimeCombMap = timeCombineService.getTypeTimeCombineMap(schoolId, gradeId, ciId);
        List<Integer> types = new ArrayList<Integer>();
        for(Map.Entry<Integer, List<Map<String, Object>>> typeTimeComb:typeTimeCombMap.entrySet()){
            types.add(typeTimeComb.getKey());
        }

        //获取非走班教学班
        //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntries = jxbDao.getZBJXBList(schoolId, gradeId, ciId, acClassType);

        //教学班ids
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        //清除历史数据
        List<ObjectId> clearJxbIds = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            if(!jxbIds.contains(jxbEntry.getID())) {
                jxbIds.add(jxbEntry.getID());
            }
            if(null!=jxbEntry.getID()){
                clearJxbIds.add(jxbEntry.getID());
            }
            if(null!=jxbEntry.getRelativeId()){
                clearJxbIds.add(jxbEntry.getRelativeId());
            }
        }
        jxbksDao.clearJxbKsByJxbIds(clearJxbIds, ciId);
        ykbDao.clearYuanKeBiaoKeShi(ciId, schoolId, gradeId, types);
        timeCombKeBiaoDao.deleteEntris(schoolId, gradeId, ciId);

        //教学班map
        Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId, acClassType);

        //源课表集合
        List<N33_YKBEntry> ykbList = ykbDao.getYKBEntrysList(ciId, schoolId);
        //专项map
        Map<ObjectId, List<N33_ZhuanXiangEntry>> zxMap = zhuanXiangDao.getZhuanXiangMap(ciId);

        List<N33_JXBCTEntry> jxbctEntries = jxbctDao.getJXBCTEntrysByJXBs(schoolId, jxbIds, ciId);

        //教学班冲突格
        Map<ObjectId, Map<Integer, List<Integer>>> jxbXyNotUseMap = paiKeService.getJxbCtXysMMap(schoolId, gradeId, xqId, ciId, jxbEntries, jxbMap, zxMap, ykbList, jxbctEntries, "zouBan");

        Map<String, Map<ObjectId, N33_YKBEntry>> xyClsRoomMMap = new HashMap<String, Map<ObjectId, N33_YKBEntry>>();
        for(N33_YKBEntry ykbEntry:ykbList){
            String key = ykbEntry.getX()+","+ykbEntry.getY();
            Map<ObjectId, N33_YKBEntry> clsRoomMMap = xyClsRoomMMap.get(key);
            if(null==clsRoomMMap){
                clsRoomMMap = new HashMap<ObjectId, N33_YKBEntry>();
            }
            clsRoomMMap.put(ykbEntry.getClassroomId(), ykbEntry);
            xyClsRoomMMap.put(key, clsRoomMMap);
        }

        List<N33_YKBEntry> ykbUpdList = new ArrayList<N33_YKBEntry>();

        List<N33_TimeCombKeBiaoEntry> addEntries = new ArrayList<N33_TimeCombKeBiaoEntry>();

        Map<Integer, List<Integer>> usedXyListMap = new HashMap<Integer, List<Integer>>();

        //随机排序
        //Collections.shuffle(types);
        //降序排序
        //Collections.reverse(types);
        //升序序排序
        Collections.sort(types);
        for(Integer type:types){
            List<Map<String, Object>> timeCombs = typeTimeCombMap.get(type);
            if(null==timeCombs){
                continue;
            }

            Map<Integer, Map<String, Object>> serialTimeCombMap = new HashMap<Integer, Map<String, Object>>();
            Map<Integer, Integer> serialNotUseCountMap = new HashMap<Integer, Integer>();
            Map<Integer, Map<Integer, List<Integer>>> serialXyNotUseMap = new HashMap<Integer, Map<Integer, List<Integer>>>();
            int allTimeCombMaxKeShi = 0;
            for(Map<String, Object> timeComb:timeCombs){
                int serial = (Integer)timeComb.get("serial");
                int serialNotUseCount = serialNotUseCountMap.get(serial)==null?0:serialNotUseCountMap.get(serial);
                Map<Integer, List<Integer>> xyNotUseMap = serialXyNotUseMap.get(serial);
                if(null==xyNotUseMap){
                    xyNotUseMap = new HashMap<Integer, List<Integer>>();
                }
                Map<ObjectId, Integer> jxbKSMap = timeComb.get("jxbKSMap")==null?new HashMap<ObjectId, Integer>():(Map<ObjectId, Integer>)timeComb.get("jxbKSMap");
                for(ObjectId jxbId:jxbKSMap.keySet()){
                    Map<Integer, List<Integer>> serialJxbXyNotUseMap = jxbXyNotUseMap.get(jxbId);
                    if(null==serialJxbXyNotUseMap){
                        continue;
                    }
                    for(Map.Entry<Integer, List<Integer>> serialJxbXyNotUse:serialJxbXyNotUseMap.entrySet()){
                        List<Integer> yNots = xyNotUseMap.get(serialJxbXyNotUse.getKey());
                        if(null==yNots){
                            yNots = new ArrayList<Integer>();
                        }
                        for(Integer notUseY:serialJxbXyNotUse.getValue()){
                            if(!yNots.contains(notUseY)){
                                serialNotUseCount++;
                                yNots.add(notUseY);
                            }
                        }
                        xyNotUseMap.put(serialJxbXyNotUse.getKey(), yNots);
                    }

                }
                serialTimeCombMap.put(serial, timeComb);
                serialXyNotUseMap.put(serial, xyNotUseMap);
                serialNotUseCountMap.put(serial, serialNotUseCount);

                int maxKeShi = timeComb.get("maxKeShi")==null?0:(Integer)timeComb.get("maxKeShi");
                if(maxKeShi>allTimeCombMaxKeShi){
                    allTimeCombMaxKeShi = maxKeShi;
                }
            }

            List<Map<String, Integer>> serialNotUseCountList = new ArrayList<Map<String, Integer>>();

            for(Map.Entry<Integer, Integer> entry:serialNotUseCountMap.entrySet()){
                Map<String, Integer> serialMap = new HashMap<String, Integer>();
                serialMap.put("serial", entry.getKey());
                serialMap.put("count", entry.getValue());
                serialNotUseCountList.add(serialMap);
            }
            serialNotUseCountListSort(serialNotUseCountList);


            //随机排序
            //Collections.shuffle(serialNotUseCountList);
            Map<Integer, Integer> serialFinishCountMap = new HashMap<Integer, Integer>();

            int index = 0;

            boolean changeCol = false;
            int loopFinishCount = 0;
            int cursor = 0;
            int initMaxKeShi = allTimeCombMaxKeShi;
            for(int loop = 0; loop<allTimeCombMaxKeShi; loop++){
                boolean serialSuccessState = true;
                Map<Integer, Integer> serialTempFinishCountMap = new HashMap<Integer, Integer>(serialFinishCountMap);
                List<N33_TimeCombKeBiaoEntry> serialFininshEntries = new ArrayList<N33_TimeCombKeBiaoEntry>();
                List<N33_YKBEntry> serialYkbUpdList = new ArrayList<N33_YKBEntry>();
                Map<Integer, List<Integer>> usedTempXyListMap = new HashMap<Integer, List<Integer>>(usedXyListMap);
                for(Map<String, Integer> serialMap:serialNotUseCountList){
                    int serial = serialMap.get("serial");
                    Map<String, Object> timeComb = serialTimeCombMap.get(serial);
                    if(null==timeComb){
                        continue;
                    }
                    String serialName = timeComb.get("name")==null?"":(String)timeComb.get("name");
                    int maxKeShi = timeComb.get("maxKeShi")==null?0:(Integer)timeComb.get("maxKeShi");

                    Map<ObjectId, Integer> jxbKSMap = timeComb.get("jxbKSMap")==null?new HashMap<ObjectId, Integer>():(Map<ObjectId, Integer>)timeComb.get("jxbKSMap");

                    int serialFinishCount = serialTempFinishCountMap.get(serial)==null?0:serialTempFinishCountMap.get(serial);
                    if(serialFinishCount>=maxKeShi){
                        continue;
                    }
                    Map<Integer, List<Integer>> xyNotUseMap = serialXyNotUseMap.get(serial);
                    for(int i=0;i<xList.size();i++){
                        int x = xList.get(index);
                        List<Integer> yList = xyListMap.get(x);
                        List<Integer> usedYList = usedTempXyListMap.get(x)==null?new ArrayList<Integer>():usedTempXyListMap.get(x);
                        List<Integer> yNotUse = new ArrayList<Integer>();
                        if(null!=xyNotUseMap){
                            if(null!=xyNotUseMap.get(x)){
                                yNotUse = xyNotUseMap.get(x);
                            }
                        }
                        int keYongY = -1;
                        for(Integer y:yList){
                            int xyValue = x*10+y;
                            if(!yNotUse.contains(y)&&!usedYList.contains(y)&&(xyValue>cursor||cursor==0)){
                                keYongY = y;
                                cursor = xyValue;
                                break;
                            }
                        }
                        if(-1==keYongY){
                            if(i==xList.size()-1){
                                serialSuccessState = false;
                            }
                            index++;
                            if(index>xList.size()-1){
                                changeCol = false;
                                break;
                            }
                            loopFinishCount = 0;
                            cursor = 0;
                            changeCol = true;
                            if(2*initMaxKeShi>allTimeCombMaxKeShi) {
                                allTimeCombMaxKeShi++;
                            }
                            continue;
                        }

                        N33_TimeCombKeBiaoEntry addEntry = new N33_TimeCombKeBiaoEntry();
                        addEntry.setSchoolId(schoolId);
                        addEntry.setGradeId(gradeId);
                        addEntry.setCiId(ciId);
                        addEntry.setType(type);
                        addEntry.setSerialName(serialName);
                        addEntry.setSerial(serial);
                        addEntry.setX(x);
                        addEntry.setY(keYongY);
                        serialFininshEntries.add(addEntry);

                        String key = x+","+keYongY;
                        Map<ObjectId, N33_YKBEntry> clsRoomMMap = xyClsRoomMMap.get(key);
                        if(null!=clsRoomMMap) {
                            for(Map.Entry<ObjectId, Integer> jxbKS:jxbKSMap.entrySet()){
                                int jxbKeShi = jxbKS.getValue();
                                if(jxbKeShi>serialFinishCount) {
                                    N33_JXBEntry jxbEntry = jxbMap.get(jxbKS.getKey());
                                    if(null!=jxbEntry){
                                        ObjectId classRoomId = jxbEntry.getClassroomId();
                                        N33_YKBEntry ykbEntry = clsRoomMMap.get(classRoomId);
                                        if(null!=ykbEntry) {
                                            ykbEntry.setJxbId(jxbEntry.getID());
                                            ykbEntry.setIsUse(Constant.ONE);
                                            ykbEntry.setGradeId(jxbEntry.getGradeId());
                                            ykbEntry.setSubjectId(jxbEntry.getSubjectId());
                                            ykbEntry.setTeacherId(jxbEntry.getTercherId());
                                            ykbEntry.setType(jxbEntry.getType());
                                            ykbEntry.setTimeCombSerial(serial);
                                            serialYkbUpdList.add(ykbEntry);
                                        }
                                    }
                                }
                            }
                        }
                        usedYList.add(keYongY);
                        usedTempXyListMap.put(x, usedYList);
                        loopFinishCount++;
                        break;
                    }
                    if(!serialSuccessState){
                        changeCol=false;
                        break;
                    }
                    serialFinishCount++;
                    serialTempFinishCountMap.put(serial, serialFinishCount);
                    if(changeCol){
                        if(loopFinishCount==serialNotUseCountList.size()){
                            changeCol=false;
                            loopFinishCount = 0;
                            cursor = 0;
                            break;
                        }
                    }
                }
                if(serialSuccessState){
                    serialFinishCountMap= new HashMap<Integer, Integer>(serialTempFinishCountMap);
                    usedXyListMap = new HashMap<Integer, List<Integer>>(usedTempXyListMap);
                    addEntries.addAll(serialFininshEntries);
                    ykbUpdList.addAll(serialYkbUpdList);
                }
                if(!changeCol){
                    index++;
                    if (index > xList.size() - 1) {
                        index = 0;
                        loopFinishCount = 0;
                        cursor = 0;
                    }
                }
            }

            //int xCount = xList.size();
            //教学班需要循环的次数
            /*int loopCount = allTimeCombMaxKeShi % xCount == 0 ? allTimeCombMaxKeShi / xCount : allTimeCombMaxKeShi / xCount + 1;
            for(int loop = 0; loop<loopCount; loop++){
                int cursor = 0;
                for(int x:xList){
                    List<Integer> yList = xyListMap.get(x);
                    List<Integer> usedYList = usedXyListMap.get(x)==null?new ArrayList<Integer>():usedXyListMap.get(x);

                    boolean serialSuccessState = true;
                    Map<Integer, Integer> serialTempFinishCountMap = new HashMap<Integer, Integer>(serialFinishCountMap);
                    List<N33_TimeCombKeBiaoEntry> serialFininshEntries = new ArrayList<N33_TimeCombKeBiaoEntry>();
                    List<N33_YKBEntry> serialYkbUpdList = new ArrayList<N33_YKBEntry>();

                    for(Map<String, Integer> serialMap:serialNotUseCountList){
                        int serial = serialMap.get("serial");
                        Map<String, Object> timeComb = serialTimeCombMap.get(serial);
                        if(null==timeComb){
                            continue;
                        }
                        String serialName = timeComb.get("name")==null?"":(String)timeComb.get("name");
                        int maxKeShi = timeComb.get("maxKeShi")==null?0:(Integer)timeComb.get("maxKeShi");

                        Map<ObjectId, Integer> jxbKSMap = timeComb.get("jxbKSMap")==null?new HashMap<ObjectId, Integer>():(Map<ObjectId, Integer>)timeComb.get("jxbKSMap");

                        int serialFinishCount = serialTempFinishCountMap.get(serial)==null?0:serialTempFinishCountMap.get(serial);
                        if(serialFinishCount>=maxKeShi){
                            continue;
                        }
                        Map<Integer, List<Integer>> xyNotUseMap = serialXyNotUseMap.get(serial);


                        List<Integer> yNotUse = new ArrayList<Integer>();
                        if(null!=xyNotUseMap){
                            if(null!=xyNotUseMap.get(x)){
                                yNotUse = xyNotUseMap.get(x);
                            }
                        }
                        int keYongY = -1;
                        for(Integer y:yList){
                            int xyValue = x*10+keYongY;
                            if(!yNotUse.contains(y)&&!usedYList.contains(y)&&(xyValue>cursor||cursor==0)){
                                keYongY = y;
                                cursor = xyValue;
                                break;
                            }
                        }
                        if(-1==keYongY){
                            serialSuccessState = false;
                            continue;
                        }

                        N33_TimeCombKeBiaoEntry addEntry = new N33_TimeCombKeBiaoEntry();
                        addEntry.setSchoolId(schoolId);
                        addEntry.setGradeId(gradeId);
                        addEntry.setCiId(ciId);
                        addEntry.setType(type);
                        addEntry.setSerialName(serialName);
                        addEntry.setSerial(serial);
                        addEntry.setX(x);
                        addEntry.setY(keYongY);
                        serialFininshEntries.add(addEntry);

                        String key = x+","+keYongY;
                        Map<ObjectId, N33_YKBEntry> clsRoomMMap = xyClsRoomMMap.get(key);
                        if(null!=clsRoomMMap) {
                            for(Map.Entry<ObjectId, Integer> jxbKS:jxbKSMap.entrySet()){
                                int jxbKeShi = jxbKS.getValue();
                                if(jxbKeShi>serialFinishCount) {
                                    N33_JXBEntry jxbEntry = jxbMap.get(jxbKS.getKey());
                                    if(null!=jxbEntry){
                                        ObjectId classRoomId = jxbEntry.getClassroomId();
                                        N33_YKBEntry ykbEntry = clsRoomMMap.get(classRoomId);
                                        if(null!=ykbEntry) {
                                            ykbEntry.setJxbId(jxbEntry.getID());
                                            ykbEntry.setIsUse(Constant.ONE);
                                            ykbEntry.setGradeId(jxbEntry.getGradeId());
                                            ykbEntry.setSubjectId(jxbEntry.getSubjectId());
                                            ykbEntry.setTeacherId(jxbEntry.getTercherId());
                                            ykbEntry.setType(jxbEntry.getType());
                                            ykbEntry.setTimeCombSerial(serial);
                                            serialYkbUpdList.add(ykbEntry);
                                        }
                                    }
                                }
                            }
                        }

                        usedYList.add(keYongY);

                        serialFinishCount++;
                        serialTempFinishCountMap.put(serial, serialFinishCount);
                    }

                    if(serialSuccessState){
                        serialFinishCountMap= new HashMap<Integer, Integer>(serialTempFinishCountMap);
                        usedXyListMap.put(x, usedYList);
                        addEntries.addAll(serialFininshEntries);
                        ykbUpdList.addAll(serialYkbUpdList);
                    }

                }
            }*/
        }

        if(addEntries.size()>0){
            timeCombKeBiaoDao.batchAddEntris(addEntries);
            List<ObjectId> finishJxbIds = MongoUtils.getFieldObjectIDs(ykbUpdList, "jxbId");
            List<ObjectId> addKSJxbIds = new ArrayList<ObjectId>();
            List<ObjectId> updKSJxbIds = new ArrayList<ObjectId>();
            Map<ObjectId, N33_JXBKSEntry> jxbksMap = jxbksDao.getJXBKSMap(ciId, finishJxbIds);
            for(N33_YKBEntry ykbEntry:ykbUpdList){
                if(null==ykbEntry){
                    continue;
                }
                N33_JXBEntry jxbEntry = jxbMap.get(ykbEntry.getJxbId());
                if(null==jxbEntry) {
                    continue;
                }
                int jxbKeShi = jxbEntry.getJXBKS();
                N33_JXBKSEntry jxbksEntry = jxbksMap.get(jxbEntry.getID());
                int ypCount = 0;
                if(null!=jxbksEntry){
                    ypCount = jxbksEntry.getYpCount();
                }
                if ((jxbKeShi!=0&&ypCount >= jxbKeShi)||jxbKeShi==0) {
                    continue;
                }
                if (jxbksEntry == null) {
                    jxbksEntry = new N33_JXBKSEntry(jxbEntry.getID(), jxbEntry.getSubjectId(), 1, jxbEntry.getGradeId(), jxbEntry.getSchoolId(), jxbEntry.getTermId(), jxbEntry.getDanOrShuang());
                    jxbksEntry.setID(new ObjectId());
                    jxbksMap.put(jxbEntry.getID(), jxbksEntry);
                    if(!addKSJxbIds.contains(jxbEntry.getID())) {
                        addKSJxbIds.add(jxbEntry.getID());
                    }
                } else {
                    jxbksEntry.setYpCount(ypCount+1);
                    jxbksMap.put(jxbEntry.getID(), jxbksEntry);
                    if(!addKSJxbIds.contains(jxbEntry.getID())){
                        if(!updKSJxbIds.contains(jxbEntry.getID())) {
                            updKSJxbIds.add(jxbEntry.getID());
                        }
                    }
                }
                ykbDao.updateN33_YKB(ykbEntry);
            }
            if(addKSJxbIds.size()>0){
                List<N33_JXBKSEntry> addJXBKSList = new ArrayList<N33_JXBKSEntry>();
                for(ObjectId addId:addKSJxbIds){
                    N33_JXBKSEntry jxbksEntry = jxbksMap.get(addId);
                    if(null!=jxbksEntry){
                        addJXBKSList.add(jxbksEntry);
                    }
                }
                if(addJXBKSList.size()>0) {
                    jxbksDao.addN33_JXBKSEntries(addJXBKSList);
                }
            }
            if(updKSJxbIds.size()>0){
                for(ObjectId updId:updKSJxbIds){
                    N33_JXBKSEntry jxbksEntry = jxbksMap.get(updId);
                    if(null!=jxbksEntry){
                        jxbksDao.updateN33_JXBKS(jxbksEntry);
                    }
                }
            }
        }
    }

    /**
     * 根据时段组合不能排课坐标统计排序 高->低
     * @param serialNotUseCountList
     */
    public void serialNotUseCountListSort(List<Map<String, Integer>> serialNotUseCountList) {
        Collections.sort(serialNotUseCountList, new Comparator<Map<String, Integer>>() {
            @Override
            public int compare(Map<String, Integer> arg0, Map<String, Integer> arg1) {
                int count0 = arg0.get("count")==null?0:(Integer) arg0.get("count");
                int count1 = arg1.get("count")==null?0:(Integer)arg1.get("count");
                if(count1==count0){
                    //随机取得一列分时段的索引
                    Random random = new Random();
                    int flag = random.nextInt(2);
                    if(flag==1){
                        return -1;
                    }else{
                        return 0;
                    }
                }else {
                    return count1 - count0;
                }
            }
        });
    }

    public List<N33_TimeCombKeBiaoDTO> getTimeCombKeBiaoList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        List<N33_TimeCombKeBiaoDTO> reList = new ArrayList<N33_TimeCombKeBiaoDTO>();
        List<N33_TimeCombKeBiaoEntry> entries = timeCombKeBiaoDao.getTimeCombKeBiaoEntries(schoolId, gradeId, ciId);
        for(N33_TimeCombKeBiaoEntry entry:entries){
            N33_TimeCombKeBiaoDTO dto = new N33_TimeCombKeBiaoDTO(entry);
            reList.add(dto);
        }
        return reList;
    }

    public void cancelTimeCombKeBiao(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, int x, int y) {
        N33_TimeCombKeBiaoEntry entry = timeCombKeBiaoDao.getTimeCombKeBiaoEntry(schoolId, gradeId, ciId, x, y);
        List<N33_YKBEntry> ykbEntryList = ykbDao.clearTimeCombKeBiao(ciId, schoolId, gradeId, x, y, entry.getSerial());
        for (N33_YKBEntry ykbEntry : ykbEntryList) {
            N33_YKBEntry n33_ykbEntry = new N33_YKBEntry(ykbEntry.getX(), ykbEntry.getY(), ykbEntry.getClassroomId(), ykbEntry.getSchoolId(), ykbEntry.getTermId(), 0);
            n33_ykbEntry.setID(ykbEntry.getID());
            n33_ykbEntry.setNJxbId(null);
            n33_ykbEntry.setNSubjectId(null);
            n33_ykbEntry.setNTeacherId(null);
            n33_ykbEntry.setZuHeId(null);
            ykbDao.updateN33_YKB(n33_ykbEntry);
            if (ykbEntry.getNJxbId() != null) {
                N33_JXBKSEntry jxbksEntry2 = jxbksDao.getJXBKSsByJXBId(ykbEntry.getNJxbId());
                if (jxbksEntry2 != null) {
                    jxbksEntry2.setYpCount(jxbksEntry2.getYpCount() - 1 >= 0 ? jxbksEntry2.getYpCount() - 1 : 0);
                }
                jxbksDao.updateN33_JXBKS(jxbksEntry2);
            }
            N33_JXBKSEntry jxbksEntry = jxbksDao.getJXBKSsByJXBId(ykbEntry.getJxbId());
            if (jxbksEntry != null) {
                jxbksEntry.setYpCount(jxbksEntry.getYpCount() - 1 >= 0 ? jxbksEntry.getYpCount() - 1 : 0);
                jxbksDao.updateN33_JXBKS(jxbksEntry);
            }
        }
        timeCombKeBiaoDao.cancelTimeCombKeBiao(schoolId, gradeId, ciId, x, y);
    }

    public Map<Integer, Map<Integer, Integer>> getTimeCombKeShiMap(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        Map<Integer, Map<Integer, Integer>> reMap = new HashMap<Integer, Map<Integer, Integer>>();
        List<N33_TimeCombKeBiaoEntry> entries = timeCombKeBiaoDao.getTimeCombKeBiaoEntries(schoolId, gradeId, ciId);
        for(N33_TimeCombKeBiaoEntry entry:entries){
            Map<Integer, Integer> subMap = reMap.get(entry.getType());
            if(null==subMap){
                subMap = new HashMap<Integer, Integer>();
            }
            int keShi = subMap.get(entry.getSerial())==null?0:subMap.get(entry.getSerial());
            keShi++;
            subMap.put(entry.getSerial(), keShi);
            reMap.put(entry.getType(), subMap);
        }
        return reMap;
    }
}
