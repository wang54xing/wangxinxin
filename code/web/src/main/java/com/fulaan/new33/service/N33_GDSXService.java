package com.fulaan.new33.service;

import com.db.new33.N33_GDSXDao;
import com.db.new33.isolate.N33_ClassroomDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_SWDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZhuanXiangDao;
import com.fulaan.new33.dto.isolate.N33_GDSXDTO;
import com.fulaan.new33.dto.paike.N33_YKBDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.fulaan.new33.service.isolate.N33_SWService;
import com.fulaan.schoolbase.service.ClassRoomService;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.TermEntry;
import com.pojo.new33.paike.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class N33_GDSXService extends BaseService {
    private N33_GDSXDao gdsxDao = new N33_GDSXDao();
    private N33_TeaDao teaDao = new N33_TeaDao();
    private TermDao termDao = new TermDao();
    private N33_SWDao swDao = new N33_SWDao();
    private N33_YKBDao ykbDao = new N33_YKBDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();
    private N33_ZhuanXiangDao zhuanXiangDao = new N33_ZhuanXiangDao();

    public List<N33_GDSXDTO> getGDSXBySidAndXqid(ObjectId xqid, ObjectId sid, ObjectId gid) {
        List<N33_GDSXDTO> retList = new ArrayList<N33_GDSXDTO>();
        List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, sid, gid);
        for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
            N33_GDSXDTO gdsxdto = new N33_GDSXDTO(gdsxEntry);
            retList.add(gdsxdto);
        }
        return retList;
    }

    public Map<ObjectId, List<N33_GDSXEntry>> getGradeGDSXEntriesMap(ObjectId xqid, ObjectId schoolId){
        Map<ObjectId, List<N33_GDSXEntry>> reMap = new HashMap<ObjectId, List<N33_GDSXEntry>>();
        List<N33_GDSXEntry> entries = gdsxDao.getGDSXEntries(xqid, schoolId);
        for (N33_GDSXEntry entry : entries) {
            List<N33_GDSXEntry> gradeEntries = reMap.get(entry.getGradeId());
            if(null==gradeEntries){
                gradeEntries = new ArrayList<N33_GDSXEntry>();
            }
            gradeEntries.add(entry);
            reMap.put(entry.getGradeId(), gradeEntries);
        }
        return reMap;
    }


    public String updateGDSX(List<N33_GDSXDTO> gdsxdtos, ObjectId sid) {
        N33_GDSXDTO dto = gdsxdtos.get(0);
        ObjectId gid = new ObjectId(dto.getGradeId());
        ObjectId xqid = new ObjectId(dto.getTermId());
        gdsxDao.removeN33_GDSXEntry(sid, xqid, gid);
        List<N33_GDSXEntry> gdsxEntries = new ArrayList<N33_GDSXEntry>();
        for (N33_GDSXDTO gdsxdto : gdsxdtos) {
            gdsxdto.setSid(sid.toString());
            gdsxdto.setId("*");
            gdsxEntries.add(gdsxdto.buildEntry());
        }

        gdsxDao.addGDSXEntry(gdsxEntries);
        return "保存成功";
    }

    public List<N33_GDSXDTO> getGDSXByXqid(ObjectId xqid, ObjectId sid, ObjectId gid) {
        List<N33_GDSXDTO> retList = new ArrayList<N33_GDSXDTO>();
        List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, sid, gid);
        for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
            N33_GDSXDTO gdsxdto = new N33_GDSXDTO(gdsxEntry);
            retList.add(gdsxdto);
        }
        return retList;
    }

    public List<N33_GDSXDTO> getGDSXBySidAndXqidAndTeaId(ObjectId xqid, ObjectId sid, ObjectId gid, ObjectId teaId, ObjectId ciId) {
        List<N33_GDSXDTO> retList = new ArrayList<N33_GDSXDTO>();
        List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, sid, gid);

        N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(teaId, ciId);
        List<ObjectId> teaGradeIds = teaEntry.getGradeList();
        if (teaGradeIds != null && teaGradeIds.size() > 0) {
            for (ObjectId gradeId : teaGradeIds) {
                if (!gradeId.toString().equals(gid.toString())) {
                    gdsxEntries.addAll(gdsxDao.getGDSXBySidAndXqid(xqid, sid, gradeId));
                }
            }
        }

        for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
            N33_GDSXDTO gdsxdto = new N33_GDSXDTO(gdsxEntry);
            retList.add(gdsxdto);
        }
        return retList;
    }

    public List<N33_GDSXDTO> getGDSXBySidAndXqidTea(ObjectId xqid, ObjectId sid, ObjectId teaId) {
        TermEntry entry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        for (TermEntry.PaiKeTimes paiKeTimes : entry.getPaiKeTimes()) {
            cids.add(paiKeTimes.getID());
        }
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        userIds.add(teaId);
        List<N33_TeaEntry> entries = teaDao.getTeaList(userIds, cids);
        List<N33_GDSXDTO> retList = new ArrayList<N33_GDSXDTO>();
        if (entries.size() == 0) {
            return new ArrayList<N33_GDSXDTO>();
        }
        List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(xqid, sid, entries.get(0).getGradeList());
        for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
            N33_GDSXDTO gdsxdto = new N33_GDSXDTO(gdsxEntry);
            retList.add(gdsxdto);
        }
        return retList;
    }

    public void deleteGDSX(ObjectId xqid,ObjectId sid,ObjectId gradeId){
        gdsxDao.removeN33_GDSXEntry(sid,xqid,gradeId);
    }

    /**
     * 事务冲突列表
     * @param paikeci
     * @param gradeId
     * @param sid
     * @return
     */
    public List<Map<String,Object>> getShiWuConflict(ObjectId paikeci,ObjectId gradeId,ObjectId sid){
        List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();
        Map<ObjectId,N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMap(sid, paikeci);
        Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(paikeci,sid);
        Map<ObjectId,N33_ClassroomEntry> classRoomEntryMap = classroomDao.getRoomEntryListByXqGradeForMap(paikeci,sid);
        TermEntry termEntry = termDao.getTermByTimeId(paikeci);
        List<N33_GDSXEntry> gdsxEntries = gdsxDao.getGDSXBySidAndXqid(termEntry.getID(), sid, gradeId);

        List<N33_SWEntry> swEntryList = swDao.getSwBySchoolId(termEntry.getID(),sid);
        List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(paikeci,sid,gradeId);
        Map<String,List<N33_YKBEntry>> map = new HashMap<String, List<N33_YKBEntry>>();
        for (N33_YKBEntry ykbEntry : ykbEntryList) {
            List<N33_YKBEntry> ykbEntryList1 = map.get(ykbEntry.getX() + "" + ykbEntry.getY());
            if(ykbEntryList1 == null){
                ykbEntryList1 = new ArrayList<N33_YKBEntry>();
            }
            ykbEntryList1.add(ykbEntry);
            map.put(ykbEntry.getX() + "" + ykbEntry.getY(),ykbEntryList1);
        }

        //算出固定事项和课程同时存在的记录
        getGDSXAndCourses(retList,gdsxEntries,map,jxbEntryMap,teaEntryMap,classRoomEntryMap);
        getGDSWAndCourses(retList,swEntryList,map,jxbEntryMap,teaEntryMap,classRoomEntryMap);
        return retList;
    }

    private void getGDSWAndCourses(List<Map<String,Object>> retList,List<N33_SWEntry> swEntryList,Map<String,List<N33_YKBEntry>> ykbListMap,Map<ObjectId,N33_JXBEntry> jxbEntryMap,Map<ObjectId,N33_TeaEntry> teaEntryMap,Map<ObjectId,N33_ClassroomEntry> classroomEntryMap){
        for (N33_SWEntry swEntry : swEntryList) {
            String xy = (swEntry.getY() - 1) + "" + (swEntry.getX() - 1);
            List<N33_YKBEntry> xyYKBEntryList = ykbListMap.get(xy);
            if(xyYKBEntryList != null){
                for (N33_YKBEntry ykbEntry : xyYKBEntryList) {
                    if(swEntry.getTeacherIds().size() == 0){
                        if(ykbEntry.getType() == 6){
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("desc",swEntry.getDesc());
                            map.put("xy","周" + swEntry.getY() + "第" + swEntry.getX() + "节");
                            map.put("classRoomName",classroomEntryMap.get(ykbEntry.getClassroomId()).getRoomName());
                            for (int i = 1; i <= 2; i ++ ) {
                                if(i == 1){
                                    if(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName())){
                                        map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getNickName() + "单");
                                    }else{
                                        map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "单");
                                    }
                                    if(ykbEntry.getTeacherId() == null || "".equals(ykbEntry.getTeacherId().toString())){
                                        map.put("teaName","");
                                    }else{
                                        map.put("teaName",teaEntryMap.get(ykbEntry.getTeacherId()).getUserName() + "单");
                                    }
                                    retList.add(map);
                                }else{
                                    if(jxbEntryMap.get(ykbEntry.getNJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(ykbEntry.getNJxbId()).getNickName())){
                                        map.put("jxbName",jxbEntryMap.get(ykbEntry.getNJxbId()).getNickName() + "双");
                                    }else{
                                        map.put("jxbName",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "双");
                                    }
                                    if(ykbEntry.getNTeacherId() == null || "".equals(ykbEntry.getNTeacherId().toString())){
                                        map.put("teaName","");
                                    }else{
                                        map.put("teaName",teaEntryMap.get(ykbEntry.getNTeacherId()).getUserName() + "双");
                                    }
                                    retList.add(map);
                                }
                            }
                        }else if(ykbEntry.getType() == 4){
                            List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(ykbEntry.getJxbId(),ykbEntry.getTermId());
                            for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
                                Map<String,Object> map = new HashMap<String, Object>();
                                map.put("desc",swEntry.getDesc());
                                map.put("xy","周" + swEntry.getY() + "第" + swEntry.getX() + "节");
                                map.put("classRoomName",classroomEntryMap.get(zhuanXiangEntry.getRoomId()).getRoomName());
                                if(zhuanXiangEntry.getTeaId() == null || "".equals(zhuanXiangEntry.getTeaId().toString())){
                                    map.put("teaName","");
                                }else{
                                    map.put("teaName",teaEntryMap.get(zhuanXiangEntry.getTeaId()).getUserName());
                                }
                                map.put("jxbName",zhuanXiangEntry.getName());
                                retList.add(map);
                            }
                        }else{
                            if(ykbEntry.getType() != 0){
                                Map<String,Object> map = new HashMap<String, Object>();
                                map.put("desc",swEntry.getDesc());
                                map.put("xy","周" + swEntry.getY() + "第" + swEntry.getX() + "节");
                                map.put("classRoomName",classroomEntryMap.get(ykbEntry.getClassroomId()).getRoomName());
                                if(ykbEntry.getTeacherId() == null || "".equals(ykbEntry.getTeacherId().toString())){
                                    map.put("teaName","");
                                }else{
                                    map.put("teaName",teaEntryMap.get(ykbEntry.getTeacherId()).getUserName());
                                }
                                if(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName())){
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getNickName());
                                }else{
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getName());
                                }
                                retList.add(map);
                            }
                        }
                    }else{
                        if(ykbEntry.getType() == 4){
                            List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(ykbEntry.getJxbId(),ykbEntry.getTermId());
                            for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
                                if(swEntry.getTeacherIds().contains(zhuanXiangEntry.getTeaId())){
                                    Map<String,Object> map = new HashMap<String, Object>();
                                    map.put("desc",swEntry.getDesc());
                                    map.put("xy","周" + swEntry.getY() + "第" + swEntry.getX() + "节");
                                    map.put("classRoomName",classroomEntryMap.get(zhuanXiangEntry.getRoomId()).getRoomName());
                                    if(zhuanXiangEntry.getTeaId() == null || "".equals(zhuanXiangEntry.getTeaId().toString())){
                                        map.put("teaName","");
                                    }else{
                                        map.put("teaName",teaEntryMap.get(zhuanXiangEntry.getTeaId()).getUserName());
                                    }
                                    map.put("jxbName",zhuanXiangEntry.getName());
                                    retList.add(map);
                                }
                            }
                        }else if (ykbEntry.getType() == 6){
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("desc",swEntry.getDesc());
                            map.put("xy","周" + swEntry.getY() + "第" + swEntry.getX() + "节");
                            map.put("classRoomName",classroomEntryMap.get(ykbEntry.getClassroomId()).getRoomName());
                            if(swEntry.getTeacherIds().contains(ykbEntry.getNTeacherId())){
                                if(jxbEntryMap.get(ykbEntry.getNJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(ykbEntry.getNJxbId()).getNickName())){
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getNJxbId()).getNickName() + "双");
                                }else{
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "双");
                                }
                                if(ykbEntry.getNTeacherId() == null || "".equals(ykbEntry.getNTeacherId().toString())){
                                    map.put("teaName","");
                                }else{
                                    map.put("teaName",teaEntryMap.get(ykbEntry.getNTeacherId()).getUserName() + "双");
                                }
                                retList.add(map);
                            }
                            if(swEntry.getTeacherIds().contains(ykbEntry.getTeacherId())){
                                if(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName())){
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getNickName() + "单");
                                }else{
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "单");
                                }
                                if(ykbEntry.getTeacherId() == null || "".equals(ykbEntry.getTeacherId().toString())){
                                    map.put("teaName","");
                                }else{
                                    map.put("teaName",teaEntryMap.get(ykbEntry.getTeacherId()).getUserName() + "单");
                                }
                                retList.add(map);
                            }
                        }else{
                            if(swEntry.getTeacherIds().contains(ykbEntry.getTeacherId()) && ykbEntry.getType() != 0 && ykbEntry.getJxbId() != null && !"".equals(ykbEntry.getJxbId().toString())){
                                Map<String,Object> map = new HashMap<String, Object>();
                                map.put("desc",swEntry.getDesc());
                                map.put("xy","周" + swEntry.getY() + "第" + swEntry.getX() + "节");
                                map.put("classRoomName",classroomEntryMap.get(ykbEntry.getClassroomId()).getRoomName());
                                if(ykbEntry.getTeacherId() == null || "".equals(ykbEntry.getTeacherId().toString())){
                                    map.put("teaName","");
                                }else{
                                    map.put("teaName",teaEntryMap.get(ykbEntry.getTeacherId()).getUserName());
                                }
                                if(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName())){
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getNickName());
                                }else{
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getName());
                                }
                                retList.add(map);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 算出固定事项和课程同时存在的记录
     * @param retList
     * @param gdsxEntries
     * @param ykbListMap
     * @param jxbEntryMap
     * @param teaEntryMap
     * @param classroomEntryMap
     */
    private void getGDSXAndCourses(List<Map<String,Object>> retList,List<N33_GDSXEntry> gdsxEntries,Map<String,List<N33_YKBEntry>> ykbListMap,Map<ObjectId,N33_JXBEntry> jxbEntryMap,Map<ObjectId,N33_TeaEntry> teaEntryMap,Map<ObjectId,N33_ClassroomEntry> classroomEntryMap){
        for (N33_GDSXEntry gdsxEntry : gdsxEntries) {
            String xy = gdsxEntry.getX() + "" + gdsxEntry.getY();
            List<N33_YKBEntry> xyYKBEntryList = ykbListMap.get(xy);
            if(xyYKBEntryList != null){
                for (N33_YKBEntry ykbEntry : xyYKBEntryList) {
                    if(ykbEntry.getType() == 6){
                        Map<String,Object> map = new HashMap<String, Object>();
                        map.put("desc",gdsxEntry.getDesc());
                        map.put("xy","周" + (gdsxEntry.getX() + 1) + "第" + (gdsxEntry.getY() + 1) + "节");
                        map.put("classRoomName",classroomEntryMap.get(ykbEntry.getClassroomId()).getRoomName());
                        for (int i = 1; i <= 2; i ++ ) {
                            if(i == 1){
                                if(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName())){
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getNickName() + "单");
                                }else{
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getName() + "单");
                                }
                                if(ykbEntry.getTeacherId() == null || "".equals(ykbEntry.getTeacherId().toString())){
                                    map.put("teaName","");
                                }else{
                                    map.put("teaName",teaEntryMap.get(ykbEntry.getTeacherId()).getUserName() + "单");
                                }
                                retList.add(map);
                            }else{
                                if(jxbEntryMap.get(ykbEntry.getNJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(ykbEntry.getNJxbId()).getNickName())){
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getNJxbId()).getNickName() + "双");
                                }else{
                                    map.put("jxbName",jxbEntryMap.get(ykbEntry.getNJxbId()).getName() + "双");
                                }
                                if(ykbEntry.getNTeacherId() == null || "".equals(ykbEntry.getNTeacherId().toString())){
                                    map.put("teaName","");
                                }else{
                                    map.put("teaName",teaEntryMap.get(ykbEntry.getNTeacherId()).getUserName() + "双");
                                }
                                retList.add(map);
                            }
                        }
                    }else if(ykbEntry.getType() == 4){
                        List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuanXiangDao.findN33_ZhuanXiangEntry(ykbEntry.getJxbId(),ykbEntry.getTermId());
                        for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("desc",gdsxEntry.getDesc());
                            map.put("xy","周" + (gdsxEntry.getX() + 1) + "第" + (gdsxEntry.getY() + 1) + "节");
                            map.put("classRoomName",classroomEntryMap.get(zhuanXiangEntry.getRoomId()).getRoomName());
                            if(zhuanXiangEntry.getTeaId() == null || "".equals(zhuanXiangEntry.getTeaId().toString())){
                                map.put("teaName","");
                            }else{
                                map.put("teaName",teaEntryMap.get(zhuanXiangEntry.getTeaId()).getUserName());
                            }
                            map.put("jxbName",zhuanXiangEntry.getName());
                            retList.add(map);
                        }
                    }else{
                        if(ykbEntry.getType() != 0){
                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put("desc",gdsxEntry.getDesc());
                            map.put("xy","周" + (gdsxEntry.getX() + 1) + "第" + (gdsxEntry.getY() + 1) + "节");
                            map.put("classRoomName",classroomEntryMap.get(ykbEntry.getClassroomId()).getRoomName());
                            if(ykbEntry.getTeacherId() == null || "".equals(ykbEntry.getTeacherId().toString())){
                                map.put("teaName","");
                            }else{
                                map.put("teaName",teaEntryMap.get(ykbEntry.getTeacherId()).getUserName());
                            }
                            if(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName() != null && !"".equals(jxbEntryMap.get(ykbEntry.getJxbId()).getNickName())){
                                map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getNickName());
                            }else{
                                map.put("jxbName",jxbEntryMap.get(ykbEntry.getJxbId()).getName());
                            }
                            retList.add(map);
                        }
                    }
                }
            }
        }
    }

}
