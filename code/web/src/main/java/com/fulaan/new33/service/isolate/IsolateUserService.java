package com.fulaan.new33.service.isolate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.db.new33.isolate.*;
import com.db.new33.paike.*;
import com.pojo.new33.isolate.*;
import com.pojo.new33.paike.*;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fulaan.new33.dto.isolate.N33_ManagerDTO;
import com.fulaan.new33.dto.isolate.N33_TeaDTO;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.PaiKeService;
import com.fulaan.utils.RESTAPI.bo.new33.isolate.isolateAPI;
import com.pojo.utils.MongoUtils;
import com.sys.utils.JsonUtil;


@Service
public class IsolateUserService extends BaseService {
    //老师
    private N33_TeaDao teaDao = new N33_TeaDao();
    private N33_ClassDao classDao = new N33_ClassDao();
    private GradeDao gradeDao = new GradeDao();
    private SubjectDao subjectDao = new SubjectDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();
    private N33_ZKBDao zkbDao = new N33_ZKBDao();
    @Autowired
    private PaiKeService paiKeService;

    private N33_ManagerDao managerDao = new N33_ManagerDao();

    private TermDao termDao = new TermDao();
    private N33_ZhuanXiangDao zhuanXiangDao = new N33_ZhuanXiangDao();
    private N33_ZIXIKEDao zixikeDao = new N33_ZIXIKEDao();

    /**
     * 批量新增老师
     *
     * @param dtos
     */
    public void addIsolateTeaList(List<N33_TeaDTO> dtos, ObjectId gradeId, ObjectId sid, ObjectId xqid) {
        if (dtos.size() == 0) {
            Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(xqid, sid, gradeId);
            List<ObjectId> userIds = new ArrayList<ObjectId>();
            for (N33_TeaEntry entry : teaEntryMap.values()) {
                List<ObjectId> gradeIds = entry.getGradeList();
                gradeIds.remove(gradeId);
                if (gradeIds.size() == 0) {
                    userIds.add(entry.getID());
                } else {
                    entry.setGradeList(gradeIds);
                    teaDao.updateIsolateN33_TeaEntry(entry);
                }
            }
            teaDao.DelTea(userIds);
        } else {
            Map<ObjectId, N33_TeaEntry> teaEntryGradeMap = teaDao.getTeaMap(xqid, sid, gradeId);
            for (N33_TeaEntry entry : teaEntryGradeMap.values()) {
                List<ObjectId> gradeIds = entry.getGradeList();
                gradeIds.remove(gradeId);
                if (gradeIds.size() == 0) {
                    teaDao.DelTea(entry.getID());
                } else {
                    entry.setGradeList(gradeIds);
                    teaDao.updateIsolateN33_TeaEntry(entry);
                }
            }
            List<ObjectId> uis = new ArrayList<ObjectId>();
            for (N33_TeaDTO dto : dtos) {
                uis.add(new ObjectId(dto.getUid()));
            }
            Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(xqid, uis);
            List<ObjectId> userIds = new ArrayList<ObjectId>();
            Map<ObjectId, Integer> map = new HashMap<ObjectId, Integer>();
            for (N33_TeaEntry entry : teaEntryMap.values()) {
                List<ObjectId> gradeIds = entry.getGradeList();
                if (gradeIds.contains(gradeId)) {
                    map.put(entry.getUserId(), 0);
                } else {
                    map.put(entry.getUserId(), 1);
                }
                gradeIds.remove(gradeId);
                if (gradeIds.size() == 0) {
                    userIds.add(entry.getID());
                } else {
                    entry.setGradeList(gradeIds);
                    teaDao.updateIsolateN33_TeaEntry(entry);
                }
            }
            teaDao.DelTea(userIds);
            teaEntryMap = teaDao.getTeaMap(xqid, uis);
            List<N33_TeaEntry> list = new ArrayList<N33_TeaEntry>();
            for (N33_TeaDTO dto : dtos) {
                Boolean bf = true;
                for (N33_TeaEntry entry : teaEntryMap.values()) {
                    if (dto.getUid().equals(entry.getUserId().toString())) {
                        List<ObjectId> gradeIds = entry.getGradeList();
                        gradeIds.add(gradeId);
                        entry.setGradeList(gradeIds);
                        if (map.get(entry.getUserId()) == 0) {
                            entry.setSubjectList(MongoUtils.convertToObjectIdList(dto.getSubjectIds()));
                        }
                        if (map.get(entry.getUserId()) == 1 && dto.getGradeIds().contains(gradeId.toString())) {
                            entry.setSubjectList(MongoUtils.convertToObjectIdList(dto.getSubjectIds()));
                        }
                        teaDao.updateIsolateN33_TeaEntry(entry);
                        bf = false;
                    }
                }
                if (bf) {
                    list.add(dto.getEntry());
                }
            }
            if (list.size() > 0) {
                teaDao.addIsolateTeaEntrys(list);
            }
        }
    }

    /**
     * 新增修改
     *
     * @param dto
     */
    public void addIsolateTeaOrUpdate(N33_TeaDTO dto) {
        N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(new ObjectId(dto.getId()));
        if (teaEntry != null) {
            teaDao.updateIsolateN33_TeaEntry(dto.getEntry());
        } else {
            teaDao.addIsolateN33_TeaEntry(dto.getEntry());
        }
    }

    /**
     * 根据姓名模糊查询教师
     *
     * @param sid
     * @param userName
     * @return
     * @throws JSONException
     */
    public List<N33_ManagerDTO> getTeaListByName(ObjectId sid, String userName) throws JSONException {
        String result = isolateAPI.getTeaListByName(userName, sid);
        JSONObject resultJson = new JSONObject(result);
        Map map1 = (Map) JsonUtil.JSONToObj(resultJson.toString(), Map.class);
        List<Map<String, Object>> dtoList1 = (List<Map<String, Object>>) map1.get("message");
        List<N33_ManagerDTO> managerList = new ArrayList<N33_ManagerDTO>();
        List<N33_ManagerEntry> listManager = managerDao.findAllManagerList(sid);
        for (Map<String, Object> dto : dtoList1) {
            String name = (String) dto.get("name");
            String contact = (String) dto.get("contact");
            String uid = (String) dto.get("uid");
            boolean flag = true;
            for (N33_ManagerEntry n33_ManagerEntry : listManager) {
                if (n33_ManagerEntry.getUserId().toString().equals(uid)) {
                    flag = false;
                }
            }
            if (flag) {
                N33_ManagerDTO managerDto = new N33_ManagerDTO(new ObjectId().toString(), sid.toString(), uid, name, contact);
                managerList.add(managerDto);
            }
        }
        return managerList;
    }

    public N33_TeaDTO getTea(ObjectId id, ObjectId gid) {
        N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(id);
        Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(teaEntry.getXqid(), teaEntry.getSchoolId());
        Map<ObjectId, N33_KSEntry> subjectMap = subjectDao.findSubjectsByIdsMapSubId(teaEntry.getXqid(), gid, teaEntry.getSchoolId());
        N33_TeaDTO dto = new N33_TeaDTO(teaEntry);
        String graList = "";
        String subList = "";
        for (ObjectId grade : teaEntry.getGradeList()) {
            Grade gra = gradeMap.get(grade);
            if (gra != null) {
                graList += gra.getName() + ",";
            }
        }
        for (ObjectId grade : teaEntry.getSubjectList()) {
            N33_KSEntry gra = subjectMap.get(grade);
            if (gra != null) {
                subList += gra.getSubjectName() + ",";
            }
        }
        if (dto.getSex() == 0) {
            dto.setSexStr("女");
        } else {
            dto.setSexStr("男");
        }
        dto.setGradeStr(graList);
        dto.setSubjectStr(subList);
        return dto;
    }

    public List<N33_TeaDTO> getTeaAndSubjectByGradeId(ObjectId xqid, ObjectId sid, ObjectId gid, ObjectId subId) {
        List<N33_TeaEntry> teaEntries = teaDao.getTeaListByGradeId(xqid, sid, gid, subId);

        //教学班
        Map<ObjectId, List<N33_JXBDTO>> jxbMap = paiKeService.getUserJiaoXueBanByXqid(xqid);

        //教学班
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(xqid);
        List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(jxbEntries, "_id");
        Map<ObjectId, N33_JXBKSEntry> jxbKSMap = jxbksDao.getJXBKSsBySubjectId(jxbIds, xqid);

        //全校课时
        List<N33_KSEntry> subjectTime = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid);

        List<N33_TeaDTO> returnList = new ArrayList<N33_TeaDTO>();
        for (N33_TeaEntry entry : teaEntries) {
            N33_TeaDTO dto = new N33_TeaDTO(entry);
            //课时
            Integer ksCount = 0;
            //已排课时
            Integer pksCount = 0;

            //教学班list
            List<N33_JXBDTO> jxbdtos = jxbMap.get(entry.getUserId());

            if (jxbdtos != null && jxbdtos.size() > 0) {
                ksCount = getTeaKeShi(jxbdtos, subjectTime);
                for (N33_JXBDTO jxbdto : jxbdtos) {
                    N33_JXBKSEntry KsEntry = jxbKSMap.get(new ObjectId(jxbdto.getId()));
                    pksCount += KsEntry != null ? KsEntry.getYpCount() : 0;
                }
            }

            int count = 0;
            if (jxbdtos != null && jxbdtos.size() > 0) {
                for (N33_JXBDTO jxbdto : jxbdtos) {
                    if (entry.getUserId().toString().equals(jxbdto.getTeacherId())) {
                        count++;
                    }
                }
            }
            dto.setKs(ksCount);
            dto.setYpks(pksCount);
            dto.setJxbCount(count);
            returnList.add(dto);
        }
        return returnList;
    }

    public List<N33_TeaDTO> getTeaAndSubjectByGradeId1(ObjectId xqid, ObjectId sid, ObjectId gid, ObjectId subId) {
        List<N33_TeaEntry> teaEntries = teaDao.getTeaListByGradeId(xqid, sid, gid, subId);

        List<N33_TeaDTO> returnList = new ArrayList<N33_TeaDTO>();
        for (N33_TeaEntry entry : teaEntries) {
            N33_TeaDTO dto = new N33_TeaDTO(entry);
            returnList.add(dto);
        }
        return returnList;
    }

    public List<N33_TeaDTO> getTeaByGradeAndSubject(Map map, ObjectId sid) {
        String xqid = (String) map.get("xqid");
        String gradeId = (String) map.get("gradeId");
        List<String> subList = new ArrayList<String>();
        if (map.get("subList") != null) {
            subList = (List<String>) map.get("subList");
        }
        List<N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(new ObjectId(xqid), sid, new ObjectId(gradeId), MongoUtils.convertToObjectIdList(subList));
        List<N33_TeaDTO> dtos = new ArrayList<N33_TeaDTO>();
        for (N33_TeaEntry teaEntry : teaEntries) {
            dtos.add(new N33_TeaDTO(teaEntry));
        }
        return dtos;
    }

    public List<N33_TeaDTO> getTeaByGradeAndSubject(ObjectId xqid, ObjectId sid, String gradeId, String subid, String name) {
        ObjectId subjectId = null;
        ObjectId gid = null;
        if (!subid.equals("*")) {
            subjectId = new ObjectId(subid);
        }
        if (!"*".equals(gradeId)) {
            gid = new ObjectId(gradeId);
        }
        List<N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(xqid, sid, gid, subjectId, name);
        Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(xqid, sid);
        Map<ObjectId, N33_KSEntry> subjectMap = subjectDao.findSubjectsByIdsMapSubId(xqid, gid, sid);

        List<N33_TeaDTO> mapList = new ArrayList<N33_TeaDTO>();
        for (N33_TeaEntry teaEntry : teaEntries) {
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
                    if (gra != null) {
                        if (!gra.getSubjectName().equals("")) {
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

    public List<N33_TeaDTO> getTeaByGradeAndSubjectCount(ObjectId xqid, ObjectId sid, String gradeId, String subid, String name) {
        ObjectId subjectId = null;
        ObjectId gid = null;
        if (!subid.equals("*")) {
            subjectId = new ObjectId(subid);
        }
        if (!"*".equals(gradeId)) {
            gid = new ObjectId(gradeId);
        }
        List<N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(xqid, sid, gid, subjectId, name);
        Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(xqid, sid);
        Map<ObjectId, N33_KSEntry> subjectMap = subjectDao.findSubjectsByIdsMapSubId(xqid, gid, sid);
        //教学班
        Map<ObjectId, List<N33_JXBDTO>> teaList = paiKeService.getUserJiaoXueBanByXqid(xqid);
        //全校课时
        //List<N33_KSEntry> SubjectTime = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid);
        //返回值
        List<com.fulaan.new33.dto.isolate.N33_TeaDTO> mapList = new ArrayList<com.fulaan.new33.dto.isolate.N33_TeaDTO>();
        for (N33_TeaEntry teaEntry : teaEntries) {
            com.fulaan.new33.dto.isolate.N33_TeaDTO dto = new com.fulaan.new33.dto.isolate.N33_TeaDTO(teaEntry);
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
                    if (gra != null) {
                        if (!gra.getSubjectName().equals("")) {
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
                }
            }

            if (dto.getSex() == 0) {
                dto.setSexStr("女");
            } else if(dto.getSex() == 1){
                dto.setSexStr("男");
            }else{
                dto.setSexStr("未知");
            }
            dto.setGradeStr(graList);
            dto.setSubjectStr(subList);
//            Integer count = 0;
            //教学班list
            List<N33_JXBDTO> jxbdtos = teaList.get(teaEntry.getUserId());
//            if (jxbdtos != null && jxbdtos.size() > 0) {
//                for (N33_JXBDTO jxbdto : jxbdtos) {
//                    if (jxbdto.getGradeId().equals(gradeId)) {
//                        count++;
//                    }
//                }
//            }
//            dto.setGradeJXBCount(count);
//            if (jxbdtos == null) {
//                dto.setJxbCount(0);
//            } else {
//                dto.setJxbCount(jxbdtos.size());
//            }
            if (jxbdtos != null) {
                Integer ksCount = getTeaKeShi(jxbdtos, new ArrayList<N33_KSEntry>());
                dto.setKs(ksCount);
            } else {
                dto.setKs(0);
            }
            mapList.add(dto);
        }
        return mapList;
    }

    public List<N33_TeaDTO> getTeaByGradeAndSubjectByXQID(ObjectId xqid, ObjectId sid, String gradeId, String subid, String name,Integer week) {
        ObjectId subjectId = null;
        ObjectId gid = null;
        if (!subid.equals("*")) {
            subjectId = new ObjectId(subid);
        }
        if (!"*".equals(gradeId)) {
            gid = new ObjectId(gradeId);
        }
        List<N33_ZKBEntry> zkbEntryList = zkbDao.getZKBEntryByWeekAndCiId(xqid,sid,week);
        //专项课
        List<ObjectId> zxIds = new ArrayList<ObjectId>();
        //自习课
        List<ObjectId> zxkIds = new ArrayList<ObjectId>();
        for (N33_ZKBEntry zkbEntry : zkbEntryList){
            if(zkbEntry.getType() == 4){
                zxIds.add(zkbEntry.getJxbId());
            }
            if(zkbEntry.getType() == 5){
                zxkIds.add(zkbEntry.getJxbId());
            }
        }
        Map<ObjectId, N33_ZIXIKEEntry> zixikeEntries = zixikeDao.getJXBMapsByIds(zxkIds);
        List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxIds);
        Map<ObjectId, List<N33_ZhuanXiangEntry>> listMap = new HashMap<ObjectId, List<N33_ZhuanXiangEntry>>();
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
            if (!jxbIds.contains(entry.getJxbId())) {
                jxbIds.add(entry.getJxbId());
            }
        }
        for (ObjectId objectId : jxbIds) {
            List<N33_ZhuanXiangEntry> jis = new ArrayList<N33_ZhuanXiangEntry>();
            for (N33_ZhuanXiangEntry entry : zhuanXiangEntries) {
                if (entry.getJxbId().toString().equals(objectId.toString())) {
                    jis.add(entry);
                }
            }
            listMap.put(objectId, jis);
        }
        List<ObjectId> ypkTeaIds = new ArrayList<ObjectId>();
        for (N33_ZKBEntry zkbEntry : zkbEntryList) {
            if(zkbEntry.getType() != 4 && zkbEntry.getType() != 5){
                if(zkbEntry.getNTeacherId() != null && zkbEntry.getNTeacherId().toString() != ""){
                    if(!ypkTeaIds.contains(zkbEntry.getNTeacherId())){
                        ypkTeaIds.add(zkbEntry.getNTeacherId());
                    }
                }
                if(zkbEntry.getTeacherId() != null && zkbEntry.getTeacherId().toString() != ""){
                    if(!ypkTeaIds.contains(zkbEntry.getTeacherId())){
                        ypkTeaIds.add(zkbEntry.getTeacherId());
                    }
                }
            }else {
                if(zkbEntry.getType() == 4){
                    List<N33_ZhuanXiangEntry> zhuanList = listMap.get(zkbEntry.getJxbId());
                    if (zhuanList != null) {
                        for (N33_ZhuanXiangEntry entry1 : zhuanList) {
                            if(!ypkTeaIds.contains(entry1.getTeaId())){
                                ypkTeaIds.add(entry1.getTeaId());
                            }
                        }
                    }
                }else{
                    N33_ZIXIKEEntry entry1 = zixikeEntries.get(zkbEntry.getJxbId());
                    if(!ypkTeaIds.contains(entry1.getTeacherId())){
                        ypkTeaIds.add(entry1.getTeacherId());
                    }
                }
            }
        }

        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        for (TermEntry.PaiKeTimes times : termEntry.getPaiKeTimes()) {
            cids.add(times.getID());
        }
        List<N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectIdByCiIds(cids, sid, gid, subjectId, name);
        Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(cids, sid);
        Map<ObjectId, N33_KSEntry> subjectMap = subjectDao.findSubjectsByIdsMapSubIdByCids(cids, gid, sid);

        List<N33_TeaDTO> mapList = new ArrayList<N33_TeaDTO>();
        List<ObjectId> teaIds = new ArrayList<ObjectId>();
        for (N33_TeaEntry teaEntry : teaEntries) {
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
                    if (gra != null) {
                        if (!gra.getSubjectName().equals("")) {
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
                }
            }

            if (dto.getSex() == 0) {
                dto.setSexStr("女");
            } else {
                dto.setSexStr("男");
            }
            dto.setGradeStr(graList);
            dto.setSubjectStr(subList);
            if(!teaIds.contains(teaEntry.getUserId()) && ypkTeaIds.contains(teaEntry.getUserId())){
                teaIds.add(teaEntry.getUserId());
                mapList.add(dto);
            }
        }
        return mapList;
    }


    public List<N33_TeaDTO> getTeaListByXqid(ObjectId xqid, ObjectId sid, String gid, String subid, String name) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cid = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cid.add(paiKeTimes.getID());
            }
        }
        ObjectId subjectId = null;
        if (!subid.equals("*")) {
            subjectId = new ObjectId(subid);
        }
        ObjectId gradeId = null;
        if (!gid.equals("*")) {
            gradeId = new ObjectId(gid);
        }
        //封装前的
        List<N33_TeaEntry> teEntry = teaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(cid, sid, gradeId, subjectId, name);
        List<Grade> gradeEntry = gradeDao.getIsolateGrADEEntrys(cid, sid);
        List<N33_KSEntry> subjectList = subjectDao.findSubjectsByIdsMapSubId(cid, gradeId, sid);
        //封装后的
        List<N33_TeaEntry> teaEntries = new ArrayList<N33_TeaEntry>();
        List<ObjectId> tea = new ArrayList<ObjectId>();
        for (N33_TeaEntry entry : teEntry) {
            if (!tea.contains(entry.getUserId())) {
                tea.add(entry.getUserId());
                teaEntries.add(entry);
            }
        }
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
        for (N33_TeaEntry teaEntry : teaEntries) {
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

    public void deleteTea(ObjectId id) {
        teaDao.DelTea(id);
    }

    /**
     * 得到老师根据学科（教师课表使用）
     *
     * @param subid
     * @param sid
     * @return
     */
    public List<Map<String, Object>> getTeaListBySubjectIdIsTeaTable(ObjectId subid, ObjectId sid, String name, Integer week) {
        Integer ct = week % 2;
        ObjectId xqid = getDefaultPaiKeTerm(sid).getPaikeci();
        //学科老师
        List<N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySubjectId(xqid, subid, name);
        //老师教学班
        Map<ObjectId, List<N33_JXBDTO>> teaList = paiKeService.getUserJiaoXueBanByXqid(xqid);
        //全校课时
        List<N33_KSEntry> SubjectTime = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid);
        //教学班
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(xqid);
        List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(jxbEntries, "_id");
        Map<ObjectId, N33_JXBKSEntry> entries = jxbksDao.getJXBKSsBySubjectId(jxbIds, xqid);
        Integer count = zkbDao.getZKBCount(xqid, week);
        //返回数据
        List<Map<String, Object>> DtoList = new ArrayList<Map<String, Object>>();
        for (N33_TeaEntry teaEntry : teaEntries) {
            Map<String, Object> dto = new HashMap<String, Object>();
            //课时
            Integer ksCount = 0;
            //已排课时
            Integer pksCount = 0;
            dto.put("id", teaEntry.getUserId().toString());
            dto.put("name", teaEntry.getUserName());
            //教学班list
            List<N33_JXBDTO> jxbdtos = teaList.get(teaEntry.getUserId());
            if (jxbdtos != null && jxbdtos.size() > 0) {
                ksCount = getTeaKeShi(jxbdtos, SubjectTime);
                for (N33_JXBDTO jxbdto : jxbdtos) {
                    N33_JXBKSEntry entry = entries.get(new ObjectId(jxbdto.getId()));
                    if (week != 0) {
                        if (jxbdto.getDanOrShuang() == 0) {
                            pksCount += entry != null ? entry.getYpCount() : 0;
                        } else {
                            if (ct == 0) {
                                if (jxbdto.getDanOrShuang() == 2) {
                                    pksCount += entry != null ? entry.getYpCount() : 0;
                                }
                            } else {
                                if (jxbdto.getDanOrShuang() == 1) {
                                    pksCount += entry != null ? entry.getYpCount() : 0;
                                }
                            }
                        }
                    } else {
                        pksCount += entry != null ? entry.getYpCount() : 0;
                    }
                }
            }
            if (week != 0) {
                if (count > 0) {
                    dto.put("ksCount", ksCount);
                    dto.put("pksCount", pksCount);
                } else {
                    dto.put("ksCount", 0);
                    dto.put("pksCount", 0);
                }
            }
            if (week == 0) {
                dto.put("ksCount", ksCount);
                dto.put("pksCount", pksCount);
            }
            DtoList.add(dto);
        }
        return DtoList;
    }

    public List<Map<String, Object>> getTeaListBySubjectIdIsTeaTableWeek(ObjectId subid, ObjectId sid, String name, Integer week) {
        ObjectId xqid = getDefauleTermId(sid);
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cids.add(paiKeTimes.getID());
            }
        }
        //学科老师
        Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySubjectId(cids, subid, name);
        //返回数据
        List<Map<String, Object>> DtoList = new ArrayList<Map<String, Object>>();
        List<ObjectId> teaIdsList = MongoUtils.getFieldObjectIDs(teaEntries.values(), "uid");

        List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeekTeaCount(xqid, week, teaIdsList);
        zkbEntryList.addAll(zkbDao.getN33_ZKBByWeekNTeaCount(xqid, week, teaIdsList));
        for (N33_TeaEntry teaEntry : teaEntries.values()) {
            Map<String, Object> dto = new HashMap<String, Object>();
            dto.put("id", teaEntry.getUserId().toString());
            dto.put("name", teaEntry.getUserName());
            Integer count = null;
            if(zkbEntryList.size() > 0){
                count = getTeaKS(teaEntry.getUserId(), zkbEntryList);
            }else{
                count = 0;
            }
            dto.put("count", count);
            DtoList.add(dto);
        }
        return DtoList;
    }

    public List<Map<String, Object>> getTeaListBySubjectIdIsTeaTableWeek(ObjectId subid, ObjectId sid, String name, Integer week, ObjectId gid) {
        ObjectId xqid = getDefauleTermId(sid);
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cids.add(paiKeTimes.getID());
            }
        }
        //学科老师
        Map<ObjectId, N33_TeaEntry> teaEntries = teaDao.findN33_TeaEntryBySubjectId(cids, subid, name, gid);
        //返回数据
        List<Map<String, Object>> DtoList = new ArrayList<Map<String, Object>>();
        List<ObjectId> teaIdsList = MongoUtils.getFieldObjectIDs(teaEntries.values(), "uid");

        List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeekTeaCount(xqid, week, teaIdsList);
        zkbEntryList.addAll(zkbDao.getN33_ZKBByWeekNTeaCount(xqid, week, teaIdsList));
        zkbEntryList.addAll(zkbDao.getN33_ZKBByWeekNTeaCount(xqid,week,4));
        for (N33_TeaEntry teaEntry : teaEntries.values()) {
            Map<String, Object> dto = new HashMap<String, Object>();
            dto.put("id", teaEntry.getUserId().toString());
            dto.put("name", teaEntry.getUserName());
            Integer count = getTeaKS(teaEntry.getUserId(), zkbEntryList);
            dto.put("count", count);
            DtoList.add(dto);
        }
        return DtoList;
    }

    public Integer getTeaKS(ObjectId userId, List<N33_ZKBEntry> zkbEntries) {
        ObjectId ciId = null;
        if(zkbEntries != null && zkbEntries.size() > 0){
            ciId = zkbEntries.get(0).getCId();
        }else {
            ciId = new ObjectId();
        }
//        ObjectId ciId = zkbEntries.get(0).getCId();
        Map<ObjectId,List<N33_ZhuanXiangEntry>> zhuangXiangMap = zhuanXiangDao.getZhuanXiangMap(ciId);
        List<ObjectId> zkbIds = new ArrayList<ObjectId>();
        Integer count = 0;
        for (N33_ZKBEntry zkbEntry : zkbEntries) {
            if(zkbEntry.getType() != 4){
                if (!zkbIds.contains(zkbEntry.getID())) {
                    if (userId.toString().equals(zkbEntry.getTeacherId().toString())) {
                        zkbIds.add(zkbEntry.getID());
                        count++;
                    }
                    if (zkbEntry.getNTeacherId() != null) {
                        if (userId.toString().equals(zkbEntry.getNTeacherId().toString())) {
                            zkbIds.add(zkbEntry.getID());
                            count++;
                        }
                    }
                }
            }else{
                List<N33_ZhuanXiangEntry> zhuanXiangEntryList = zhuangXiangMap.get(zkbEntry.getJxbId());
                if(zhuanXiangEntryList != null){
                    for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntryList) {
                        if(userId.toString().equals(zhuanXiangEntry.getTeaId().toString())){
                            count ++ ;
                            break;
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * 统计某个老师的教学班总课时数
     *
     * @param jxbdtos
     * @param SubjectTime
     * @return
     */
    public Integer getTeaKeShi(List<N33_JXBDTO> jxbdtos, List<N33_KSEntry> SubjectTime) {
        Integer count = 0;
        for (N33_JXBDTO jxbdto : jxbdtos) {
            count += jxbdto.getJxbks();
        }
        return count;
    }

    /**
     * @param cid  次id
     * @param ncid 同步的次的id
     */
    public void IsolateTeaListByNewCi(ObjectId cid, ObjectId ncid) {
        //指定的次的id
        List<N33_TeaEntry> teaEntries = teaDao.IsolateTeaListByNewCi(cid);
        //清楚同步次的老师数据
        teaDao.delTeaByCid(ncid);
        for (N33_TeaEntry teaEntry : teaEntries) {
            teaEntry.setID(new ObjectId());
            teaEntry.setXqid(ncid);
        }
        if (teaEntries.size() != 0) {
            teaDao.add(teaEntries);
        }
    }

    public List<String> getTeaGradeList(ObjectId userId,ObjectId xqid){
        List<String> retList = new ArrayList<String>();
        TermEntry entry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        for (TermEntry.PaiKeTimes paiKeTimes : entry.getPaiKeTimes()) {
            cids.add(paiKeTimes.getID());
        }
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        userIds.add(userId);
        List<N33_TeaEntry> entries = teaDao.getTeaList(userIds, cids);
        if(entries != null && entries.size() > 0){
            List<ObjectId> gradeList = entries.get(0).getGradeList();
            retList = MongoUtils.convertToStringList(gradeList);
        }
        return retList;
    }
}
