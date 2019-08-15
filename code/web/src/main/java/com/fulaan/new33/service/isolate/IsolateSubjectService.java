package com.fulaan.new33.service.isolate;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.isolate.ClassDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_ClassroomDao;
import com.db.new33.isolate.SubjectDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.TermEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.utils.MongoUtils;

@Service
public class IsolateSubjectService extends BaseService {
    private SubjectDao subjectDao = new SubjectDao();

    private TermDao termDao = new TermDao();
    private GradeDao gradeDao = new GradeDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private ClassDao classDao = new ClassDao();
    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();
    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();
    @Autowired
    private N33_TurnOffService turnOffService;

    /**
     * 批量新增课时
     *
     * @param dtos
     */
    public void addIsolateSubjectList(List<N33_KSDTO> dtos) {
        List<ObjectId> ids = new ArrayList<ObjectId>();
        ObjectId xqid = new ObjectId(dtos.get(0).getXqid());
        ObjectId gid = new ObjectId(dtos.get(0).getGid());
        ObjectId sid = new ObjectId(dtos.get(0).getSid());
        //对应年级的学科
        Map<ObjectId, N33_KSEntry> map = subjectDao.findSubjectsByIds(xqid, gid, sid);
        List<ObjectId> subjectIds = new ArrayList<ObjectId>();
        Collection<N33_KSEntry> ksEntries = map.values();
        for (N33_KSEntry ksEntry : ksEntries) {
            subjectIds.add(ksEntry.getSubjectId());
        }

        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        List<ObjectId> subId=new ArrayList<ObjectId>();
        for (N33_KSDTO dto : dtos) {
            N33_KSEntry subject = map.get(new ObjectId(dto.getId()));
            if (subject != null) {
                subjectDao.updateIsolateSubjectEntry(dto.getEntry());
            } else {
                entries.add(dto.getEntry());
            }
            ids.add(new ObjectId(dto.getId()));
            subId.add(new ObjectId(dto.getSubid()));
        }

        List<ObjectId> deleteJXB = new ArrayList<ObjectId>();
        for (ObjectId subjectId : subjectIds) {
            if(!subId.contains(subjectId)){
                deleteJXB.add(subjectId);
            }
        }
        //add 06-14 jxb
        ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = turnOffService.getAcClassType(sid, gid, ciId,1);
        //add 06-14 jxb
        jxbDao.delN33_JXBEntryCiIdIn(xqid, gid,deleteJXB,acClassType);
        if (entries.size() > 0) {
            subjectDao.addIsolateSubjectEntrys(entries);
        }
        subjectDao.DelSubjectList(xqid, gid, ids, sid);
        jxbDao.delN33_JXBEntryCiId(xqid, gid,subId, acClassType);
    }

    public void delSubject(ObjectId sid, ObjectId xqid, ObjectId gradeId) {
        subjectDao.DelSubjectList(xqid, gradeId);
        //add 06-14 jxb
        ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        //add 06-14 jxb
        jxbDao.delN33_JXBEntryCiId(xqid, gradeId,acClassType);
    }


    public void updateJXBKsList(ObjectId xqid,ObjectId gradeId,ObjectId sid){
        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid, gradeId, xqid,1);
        if(null!=turnOff){
            acClassType = turnOff.getAcClass();
        }
        Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIdsForSubId(xqid, gradeId, sid);
        List<N33_JXBEntry> jxbList = jxbDao.getJXBList(sid, gradeId, xqid, acClassType);
        List<ObjectId> ids=new ArrayList<ObjectId>();
        for (N33_JXBEntry jxbEntry:jxbList){
            ids.add(jxbEntry.getID());
            N33_KSEntry ksEntry=  ksEntryMap.get(jxbEntry.getSubjectId());
            if(jxbEntry.getType()==1){
                jxbEntry.setJXBKS(ksEntry.getDTime());
            }else{
                jxbEntry.setJXBKS(ksEntry.getTime());
            }
        }
        jxbDao.removeN33_JXBEntry(ids);
        jxbDao.addN33_JXBEntrys(jxbList);
    }

    public void reXingZengBan(ObjectId xqid, ObjectId gradeId, ObjectId sid) {
        Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIds(xqid, gradeId, sid);
        Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(gradeId, xqid);
        List<ClassEntry> classEntryList = new ArrayList<ClassEntry>();
        Collection<ClassEntry> classEntries = classEntryMap.values();
        for (ClassEntry classEntry : classEntries) {
            classEntryList.add(classEntry);
        }
        Collections.sort(classEntryList, new Comparator<ClassEntry>() {
            @Override
            public int compare(ClassEntry o1, ClassEntry o2) {
                return o1.getXh() - o2.getXh();
            }
        });
        Grade grade1 = gradeDao.findIsolateGradeByGradeId(xqid, sid, gradeId);
        List<N33_JXBEntry> jiaoXueBanList = new ArrayList<N33_JXBEntry>();
        //全部班级教室
        Map<ObjectId, N33_ClassroomEntry> classRoom = classroomDao.getRoomEntryListByXqGradeMap(xqid, gradeId);

        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,xqid,1);
        int acClassType = 0;
        if(null!=turnOff){
            acClassType = turnOff.getAcClass();
        }

        for (N33_KSEntry ksEntry : ksEntryMap.values()) {
            if (ksEntry.getDan() == 0 && ksEntry.getIsZouBan() == 0) {
                List<N33_JXBEntry> jxbList = jxbDao.getJXBList(sid, gradeId, ksEntry.getSubjectId(), xqid, acClassType);
                if (jxbList.size() == 0) {
                    for (ClassEntry entry : classEntryList) {
                        //学生列表
                        List<ObjectId> studentIds = entry.getStudentList();
                        N33_ClassroomEntry classroomEntry = classRoom.get(entry.getClassId());
                        //班级教室
                        ObjectId roomId = null;
                        if (classroomEntry != null) {
                            roomId = classroomEntry.getRoomId();
                        }
                        Integer type = 3;
                        String jxbName = ksEntry.getSubjectName() + grade1.getName() + "(" + entry.getXh() + ")";
                        N33_JXBEntry jxbEntry = new N33_JXBEntry(
                                jxbName,
                                jxbName,
                                ksEntry.getSubjectId(),
                                acClassType,
                                roomId,
                                null,
                                studentIds,
                                sid,
                                xqid,
                                0,
                                gradeId,
                                type,
                                new ArrayList<ObjectId>(),
                                studentIds.size(),
                                0
                        );
                        jxbEntry.setJXBKS(ksEntry.getTime());
                        jiaoXueBanList.add(jxbEntry);
                    }
                }
            }
        }
        if(jiaoXueBanList.size()>0){
            jxbDao.addN33_JXBEntrys(jiaoXueBanList);
        }
    }

    /*public static void main(String[] args){
        SubjectDao subjectDao = new SubjectDao();
        ClassDao classDao = new ClassDao();
        GradeDao gradeDao = new GradeDao();
        N33_JXBDao jxbDao = new N33_JXBDao();
        ObjectId gradeId = new ObjectId("5b7fb6ad46d697288dbd0282");
        ObjectId sid = new ObjectId("5b7e87d246d697559c3cb19d");
        ObjectId xqid = new ObjectId("5b80df9e46d6977b4d552bfa");
        Map<ObjectId, N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIds(xqid, gradeId, sid);
        Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(gradeId, xqid);
        Grade grade1 = gradeDao.findIsolateGradeByGradeId(xqid, sid, gradeId);
        for (N33_KSEntry ksEntry : ksEntryMap.values()) {

                List<N33_JXBEntry> jxbList = jxbDao.getJXBsBySubId(xqid, sid, gradeId, ksEntry.getSubjectId());
                for (N33_JXBEntry jxbEntry : jxbList) {
                    for (ClassEntry entry : classEntryMap.values()) {
                        if ((jxbEntry.getStudentIds().size() == 0 || jxbEntry.getStudentIds().size() == 319) && (ksEntry.getSubjectName() + grade1.getName() + "(" + entry.getXh() + ")").equals(jxbEntry.getName())) {
                            jxbEntry.setStudentIds(entry.getStudentList());
                            jxbEntry.setRongLiang(entry.getStudentList().size());
                            jxbDao.updateN33_JXB(jxbEntry);
                        }
                    }
                }

        }
    }*/

    /**
     * 查询年级对应的所有学科
     *
     * @param xqid
     * @param sid
     * @return
     */
    public List<N33_KSDTO> getSubjectByType(ObjectId xqid, ObjectId gid, ObjectId sid, String subType) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        if ("0".equals(subType)) {
            entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
        } else if ("1".equals(subType) || "2".equals(subType)) {
            entries = subjectDao.getIsolateSubjectEntryByZouBan(xqid, sid, gid, 1);
        } else if ("3".equals(subType)) {
            entries = subjectDao.getIsolateSubjectEntryByZouBan(xqid, sid, gid, 0);
        } else if ("4".equals(subType)) {
            List<N33_KSEntry> temp = new ArrayList<N33_KSEntry>();
            temp = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
            for (N33_KSEntry entry : temp) {
                if (entry.getDan() == 1) {
                    entries.add(entry);
                }
            }
        }
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            dots.add(new N33_KSDTO(entry));
        }
        return dots;
    }

    /**
     * 查询年级对应的所有学科
     *
     * @param xqid
     * @param sid
     * @return
     */
    public List<N33_KSDTO> getSubjectByTypeExitZX(ObjectId xqid, ObjectId gid, ObjectId sid, String subType) {
        List<N33_KSEntry> entries = new ArrayList<N33_KSEntry>();
        if ("0".equals(subType)) {
            entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
        } else if ("1".equals(subType) || "2".equals(subType)) {
            entries = subjectDao.getIsolateSubjectEntryByZouBan(xqid, sid, gid, 1);
        } else if ("3".equals(subType)) {
            entries = subjectDao.getIsolateSubjectEntryByZouBan(xqid, sid, gid, 0);
        } else if ("4".equals(subType)) {
            List<N33_KSEntry> temp = new ArrayList<N33_KSEntry>();
            temp = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
            for (N33_KSEntry entry : temp) {
                if (entry.getDan() == 1) {
                    entries.add(entry);
                }
            }
        }

        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            dots.add(new N33_KSDTO(entry));
        }
        return dots;
    }


    /**
     * 查询对应年级对应学校对应学期下的非以下九门学科课时
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<N33_KSEntry> getIsolateSubjectEntryExitNine(ObjectId xqid, ObjectId sid, ObjectId gid) {
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
        List<N33_KSEntry> returnEntry = new ArrayList<N33_KSEntry>();
        for (N33_KSEntry n33_KSEntry : entries) {
            if (!"语文".equals(n33_KSEntry.getSubjectName()) && !"数学".equals(n33_KSEntry.getSubjectName()) && !"英语".equals(n33_KSEntry.getSubjectName())
                    && !"物理".equals(n33_KSEntry.getSubjectName()) && !"化学".equals(n33_KSEntry.getSubjectName()) && !"生物".equals(n33_KSEntry.getSubjectName())
                    && !"政治".equals(n33_KSEntry.getSubjectName()) && !"历史".equals(n33_KSEntry.getSubjectName()) && !"地理".equals(n33_KSEntry.getSubjectName())) {
                returnEntry.add(n33_KSEntry);
            }
        }
        return returnEntry;
    }


    /**
     * 设置走班学科
     *
     * @param subId
     * @param gid
     * @param isZouBan
     * @param sid
     */
    public void setIsZouBan(ObjectId subId, ObjectId gid, Integer isZouBan, ObjectId sid, ObjectId xqid) {
        N33_KSEntry entry = subjectDao.findIsolateSubjectEntryById(xqid, sid, subId, gid);
        entry.setIsZouBan(isZouBan);
        subjectDao.setIsZouBan(entry);
    }

    /**
     * 设置走班学科
     *
     * @param sid
     */
    public void setIsZouBanList(List<Map> maps, ObjectId sid) {
        for (Map map : maps) {
            ObjectId xqid = new ObjectId((String) map.get("xqid"));
            ObjectId subId = new ObjectId((String) map.get("subId"));
            ObjectId gid = new ObjectId((String) map.get("gid"));
            Integer isZouBan = new Integer((String) map.get("isZouBan"));
            N33_KSEntry entry = subjectDao.findIsolateSubjectEntryById(xqid, sid, subId, gid);
            entry.setIsZouBan(isZouBan);
            subjectDao.setIsZouBan(entry);
        }

    }

    /**
     * 查询可以设置走班的学科
     *
     * @param sid
     * @param xqid
     * @param gid
     * @return
     */
    public List<N33_KSDTO> getSubjectCanZouBan(ObjectId sid, ObjectId xqid, ObjectId gid) {
        List<N33_KSDTO> ksList = getIsolateSubjectListBySchoolId(xqid, sid, gid);
        return ksList;
    }

    /**
     * 查询走班的学科
     *
     * @param sid
     * @param xqid
     * @param gid
     * @return
     */
    public List<String> getSubjectZouBan(ObjectId sid, ObjectId xqid, ObjectId gid) {
        List<String> retList = new ArrayList<String>();
        List<N33_KSDTO> ksList = getIsolateSubjectListBySchoolIdZouBan(xqid, sid, gid);
        for (N33_KSDTO ksdto : ksList) {
            retList.add(ksdto.getSnm());
        }
        Collections.sort(retList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.hashCode() - o2.hashCode();
            }
        });
        List<String> retList1 = new ArrayList<String>();
        retList1.add("学生姓名");
        retList1.add("性别");
        retList1.add("学籍号");
        for (String subName : retList) {
            retList1.add(subName);
        }
        return retList1;
    }

    /**
     * 查询可能走班的学科
     *
     * @param sid
     * @return
     */
    public List<Map<String, Object>> getIsolateSubjectBySchoolId(ObjectId sid, ObjectId xqid) {
        List<N33_KSDTO> ksList = getIsolateSubjectListBySchoolId(xqid, sid);
        Map<ObjectId, Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(xqid, sid);
        List<Grade> listGrade = gradeDao.findGradeListBySchoolId(xqid, sid);
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        for (N33_KSDTO n33_KSDTO : ksList) {
            Map<String, Object> map = new HashMap<String, Object>();
            //在所有返回的map中加入年级id
            for (Grade grade : listGrade) {
                if (grade.getName().indexOf('高') != -1) {
                    if (grade.getName().indexOf('1') != -1 || grade.getName().indexOf('一') != -1) {
                        map.put("gidOne", grade.getGradeId().toString());
                    } else if (grade.getName().indexOf('2') != -1 || grade.getName().indexOf('二') != -1) {
                        map.put("gidTwo", grade.getGradeId().toString());
                    } else if (grade.getName().indexOf('3') != -1 || grade.getName().indexOf('三') != -1) {
                        map.put("gidThree", grade.getGradeId().toString());
                    }
                }
            }
            //语数英直接返回
            if ("语文".equals(n33_KSDTO.getSnm()) || "数学".equals(n33_KSDTO.getSnm()) || "英语".equals(n33_KSDTO.getSnm())) {
                map.put("subjectName", n33_KSDTO.getSnm());
                map.put("subjectId", n33_KSDTO.getSubid());
            }
            boolean flag = true;
            //如果是物化生政史地中的一科，则存入map是否走班
            if ("物理".equals(n33_KSDTO.getSnm()) || "化学".equals(n33_KSDTO.getSnm()) || "生物".equals(n33_KSDTO.getSnm()) || "政治".equals(n33_KSDTO.getSnm())
                    || "历史".equals(n33_KSDTO.getSnm()) || "地理".equals(n33_KSDTO.getSnm())) {
                map.put("subjectName", n33_KSDTO.getSnm());
                map.put("subjectId", n33_KSDTO.getSubid());
                if (gradeMap.get(new ObjectId(n33_KSDTO.getGid())) != null && gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('高') != -1) {
                    if (gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('1') != -1 || gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('一') != -1) {
                        map.put("gradeOne", n33_KSDTO.getIsZouBan());
                    } else if (gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('2') != -1 || gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('二') != -1) {
                        map.put("gradeTwo", n33_KSDTO.getIsZouBan());
                    } else if ((gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('3') != -1 || gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('三') != -1)
                            && !"地理".equals(n33_KSDTO.getSnm()) && !"生物".equals(n33_KSDTO.getSnm())) {
                        map.put("gradeThree", n33_KSDTO.getIsZouBan());
                    }
                } else {
                    flag = false;
                }
            }
            //如果返回的list中存在该学科，则只要存入map中该年级是否走班(因为returnList中只可能存在九门课，所以以下判断只需要判断非语数英即可)
            for (Map<String, Object> map1 : returnList) {
                if (map1.get("subjectId").equals(n33_KSDTO.getSubid())) {
                    if (!"语文".equals(n33_KSDTO.getSnm()) && !"数学".equals(n33_KSDTO.getSnm()) && !"英语".equals(n33_KSDTO.getSnm()) && gradeMap.get(new ObjectId(n33_KSDTO.getGid())) != null
                            && gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('高') != -1) {
                        if (gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('1') != -1 || gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('一') != -1) {
                            map1.put("gradeOne", n33_KSDTO.getIsZouBan());
                        } else if (gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('2') != -1 || gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('二') != -1) {
                            map1.put("gradeTwo", n33_KSDTO.getIsZouBan());
                        } else if ((gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('3') != -1 || gradeMap.get(new ObjectId(n33_KSDTO.getGid())).getName().indexOf('三') != -1)
                                && !"地理".equals(n33_KSDTO.getSnm()) && !"生物".equals(n33_KSDTO.getSnm())) {
                            map1.put("gradeThree", n33_KSDTO.getIsZouBan());
                        }
                    }
                    flag = false;
                }
            }
            if (flag) {
                returnList.add(map);
            }
        }
        return returnList;
    }

    /**
     * 查询学校对应年级对应以下九门学科的课时
     *
     * @param xqid
     * @param sid
     * @return
     */
    public List<N33_KSDTO> getIsolateSubjectListBySchoolId(ObjectId xqid, ObjectId sid, ObjectId gid) {
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if (entry.getSubjectName().equals("化学") || entry.getSubjectName().equals("物理") || entry.getSubjectName().equals("地理") ||
                    entry.getSubjectName().equals("历史") || entry.getSubjectName().equals("政治") || entry.getSubjectName().equals("生物")) {
                dots.add(new N33_KSDTO(entry));
            }
        }
        return dots;
    }

    /**
     * 查询走班学科
     *
     * @param xqid
     * @param sid
     * @return
     */
    public List<N33_KSDTO> getIsolateSubjectListBySchoolIdZouBan(ObjectId xqid, ObjectId sid, ObjectId gid) {
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if ((entry.getSubjectName().equals("化学") || entry.getSubjectName().equals("物理") || entry.getSubjectName().equals("地理") ||
                    entry.getSubjectName().equals("历史") || entry.getSubjectName().equals("政治") || entry.getSubjectName().equals("生物")) && entry.getIsZouBan() == 1) {
                dots.add(new N33_KSDTO(entry));
            }
        }
        return dots;
    }

    /**
     * 查询学校对应以下九门学科的课时
     *
     * @param xqid
     * @param sid
     * @return
     */
    public List<N33_KSDTO> getIsolateSubjectListBySchoolId(ObjectId xqid, ObjectId sid) {
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if (entry.getSubjectName().equals("语文") || entry.getSubjectName().equals("数学") || entry.getSubjectName().equals("英语") ||
                    entry.getSubjectName().equals("化学") || entry.getSubjectName().equals("物理") || entry.getSubjectName().equals("地理") ||
                    entry.getSubjectName().equals("历史") || entry.getSubjectName().equals("政治") || entry.getSubjectName().equals("生物")) {
                dots.add(new N33_KSDTO(entry));
            }
        }
        return dots;
    }


    /**
     * 查询对应年级、学期下面的所有学科课时
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<N33_KSDTO> getIsolateSubjectByGradeId(ObjectId xqid, ObjectId sid, String gid) {
        ObjectId gradeId = null;
        if (!gid.equals("*")) {
            gradeId = new ObjectId(gid);
        }
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gradeId);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        List<N33_KSDTO> dotd = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if (entry.getSubjectName().equals("化学") || entry.getSubjectName().equals("物理") || entry.getSubjectName().equals("地理") || entry.getSubjectName().equals("历史") || entry.getSubjectName().equals("政治") || entry.getSubjectName().equals("生物")) {
                dotd.add(new N33_KSDTO(entry));
            } else {
                dots.add(new N33_KSDTO(entry));
            }
        }
        dots.addAll(dotd);
        return dots;
    }

    /**
     *
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public List<N33_KSDTO> getIsolateSubjectList(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(ciId, schoolId, gradeId);
        List<N33_KSDTO> allDtos = new ArrayList<N33_KSDTO>();
        List<N33_KSDTO> zbDtos = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if (entry.getSubjectName().equals("化学")
                    || entry.getSubjectName().equals("物理")
                    || entry.getSubjectName().equals("地理")
                    || entry.getSubjectName().equals("历史")
                    || entry.getSubjectName().equals("政治")
                    || entry.getSubjectName().equals("生物")) {
                zbDtos.add(new N33_KSDTO(entry));
            } else {
                allDtos.add(new N33_KSDTO(entry));
            }
        }
        allDtos.addAll(zbDtos);
        return allDtos;
    }

    /**
     * 查询对应年级、学期下面的所有学科课时
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<N33_KSDTO> getIsolateSubjectByGradeId(ObjectId xqid, ObjectId sid, String gid, int type) {
        ObjectId gradeId = null;
        if (!gid.equals("*")) {
            gradeId = new ObjectId(gid);
        }
        List<ObjectId> subjectIds = new ArrayList<ObjectId>();
        if (type == 4) {
        	//jxb add
        	ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
        	int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        	//jxb add	
            List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gradeId, xqid, type,acClassType);
            subjectIds = MongoUtils.getFieldObjectIDs(jxbEntries, "subId");
        }
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gradeId, type);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        List<N33_KSDTO> dotd = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if ((type == 4 && subjectIds != null && subjectIds.size() != 0 && subjectIds.contains(entry.getSubjectId())) || type != 4) {
                if (entry.getSubjectName().equals("化学") || entry.getSubjectName().equals("物理") || entry.getSubjectName().equals("地理") || entry.getSubjectName().equals("历史") || entry.getSubjectName().equals("政治") || entry.getSubjectName().equals("生物")) {
                    dotd.add(new N33_KSDTO(entry));
                } else {
                    dots.add(new N33_KSDTO(entry));
                }
            }
        }
        dots.addAll(dotd);
        return dots;
    }

    /**
     * 学校学科
     *
     * @param schoolId
     * @return
     */
    public Map<String, String> getSubjectMap(ObjectId schoolId, ObjectId xqid) {
        Map<String, String> map = new HashMap<String, String>();
        List<N33_KSEntry> ksEntryList = subjectDao.getSubjectEntryList(schoolId, xqid);
        for (N33_KSEntry ksEntry : ksEntryList) {
            N33_KSDTO dto = new N33_KSDTO(ksEntry);
            map.put(dto.getSnm(), dto.getSubid());
        }
        return map;
    }

    /**
     * 学校学科
     *
     * @param schoolId
     * @return
     */
    public Map<String, String> getSubjectMapById(ObjectId schoolId, ObjectId xqid) {
        Map<String, String> map = new HashMap<String, String>();
        List<N33_KSEntry> ksEntryList = subjectDao.getSubjectEntryList(schoolId, xqid);
        for (N33_KSEntry ksEntry : ksEntryList) {
            N33_KSDTO dto = new N33_KSDTO(ksEntry);
            map.put(dto.getSubid(), dto.getSnm());
        }
        return map;
    }

    /**
     * 查询对应年级、学期下面的所有学科课时(包含3门)
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public List<N33_KSDTO> getIsolateSubjectByGradeIdIncludeMain(ObjectId xqid, ObjectId sid, String gid, Integer isZouBan) {
        ObjectId gradeId = null;
        if (!gid.equals("*")) {
            gradeId = new ObjectId(gid);
        }
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gradeId, isZouBan);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        List<N33_KSDTO> dotd = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if (entry.getSubjectName().equals("语文") || entry.getSubjectName().equals("数学") || entry.getSubjectName().equals("英语") ||
                    entry.getSubjectName().equals("化学") || entry.getSubjectName().equals("物理") || entry.getSubjectName().equals("地理") ||
                    entry.getSubjectName().equals("历史") || entry.getSubjectName().equals("政治") || entry.getSubjectName().equals("生物")) {
                dotd.add(new N33_KSDTO(entry));
            } else {
                dots.add(new N33_KSDTO(entry));
            }
        }
        dots.addAll(dotd);
        return dots;
    }

    public Map<String, N33_KSDTO> getIsolateSubjectByGradeIdMap(ObjectId xqid, ObjectId sid, String gid) {
        ObjectId gradeId = null;
        if (!gid.equals("*")) {
            gradeId = new ObjectId(gid);
        }
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gradeId);
        Map<String, N33_KSDTO> dots = new HashMap<String, N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            dots.put(entry.getSubjectId().toString(), new N33_KSDTO(entry));
        }
        return dots;
    }


    public List<N33_KSDTO> getIsolateSubjectByGradeIdMap(ObjectId xqid, ObjectId sid) {
        ObjectId gradeId = null;
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gradeId);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            dots.add(new N33_KSDTO(entry));
        }
        return dots;
    }


    public List<N33_KSDTO> getIsolateSubjectByGradeIdMap(List<ObjectId> xqid, ObjectId sid) {
        ObjectId gradeId = null;
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gradeId);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            dots.add(new N33_KSDTO(entry));
        }
        return dots;
    }

    public Map<ObjectId, List<N33_KSEntry>> getsubjectMap(ObjectId xqid, ObjectId sid) {
        Map<ObjectId, List<N33_KSEntry>> result = new HashMap<ObjectId, List<N33_KSEntry>>();
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid);
        for (N33_KSEntry entry : entries) {
            List<N33_KSEntry> list = result.get(entry.getGradeId()) == null ? new ArrayList<N33_KSEntry>() : result.get(entry.getGradeId());
            list.add(entry);
            result.put(entry.getGradeId(), list);
        }
        return result;
    }

    public List<N33_KSDTO> getIsolateSubjectListByXqid(ObjectId xqid, ObjectId sid) {
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            dots.add(new N33_KSDTO(entry));
        }
        return dots;
    }

    public List<N33_KSDTO> getIsolateSubjectListByList(ObjectId xqid, ObjectId sid) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cid = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cid.add(paiKeTimes.getID());
            }
        }
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntry(cid, sid);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        List<N33_KSEntry> entries1 = new ArrayList<N33_KSEntry>();
        for (N33_KSEntry entry : entries) {
            Boolean bf = true;
            for (N33_KSEntry entry1 : entries1) {
                if (entry1.getGradeId().toString().equals(entry.getGradeId().toString()) && entry1.getSubjectId().toString().equals(entry.getSubjectId().toString())) {
                    bf = false;
                }
            }
            if (bf) {
                entries1.add(entry);
            }
        }
        for (N33_KSEntry entry : entries1) {
            dots.add(new N33_KSDTO(entry));
        }
        return dots;
    }


    public List<N33_KSDTO> getIsolateSubjectListByCid(ObjectId cid, ObjectId sid) {
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(cid, sid);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            dots.add(new N33_KSDTO(entry));
        }
        return dots;
    }

    public N33_KSDTO getIsolateSubjectByGradeId(ObjectId xqid, ObjectId sid, ObjectId gid, ObjectId subId) {
        N33_KSEntry entry = subjectDao.findIsolateSubjectEntryById(xqid, sid, subId, gid);
        N33_KSDTO ksdto = new N33_KSDTO(entry);
        return ksdto;
    }


    /**
     * 加载某个学校某个年级所有学科去重复
     *
     * @param xqid
     * @param sid
     * @return
     */
    public List<N33_KSDTO> getIsolateSubjectListByXqAndGrade(ObjectId xqid, ObjectId sid, ObjectId gid) {
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, gid);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if (dots.size() > 0) {
                Boolean bf = true;
                for (N33_KSDTO dto : dots) {
                    if (dto.getSubid().equals(entry.getSubjectId().toString())) {
                        bf = false;
                    }
                }
                if (bf) {
                    dots.add(new N33_KSDTO(entry));
                }
            } else {
                dots.add(new N33_KSDTO(entry));
            }
        }
        return dots;
    }

    /**
     * 加载某个学校所有学科去重复
     *
     * @param xqid
     * @param sid
     * @return
     */
    public List<N33_KSDTO> getIsolateSubjectListByXq(ObjectId xqid, ObjectId sid) {
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if (dots.size() > 0) {
                Boolean bf = true;
                for (N33_KSDTO dto : dots) {
                    if (dto.getSubid().equals(entry.getSubjectId().toString())) {
                        bf = false;
                    }
                }
                if (bf) {
                    dots.add(new N33_KSDTO(entry));
                }
            } else {
                dots.add(new N33_KSDTO(entry));
            }
        }
        return dots;
    }

    public List<N33_KSDTO> getIsolateSubjectListByGradeIdAndXqid(ObjectId xqid, ObjectId sid,ObjectId gid) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cids.add(paiKeTimes.getID());
            }
        }
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        Map<ObjectId,N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIdsMapSubIds(cids, gid.toString(), sid);
        if(ksEntryMap != null){
            for (N33_KSEntry ksEntry : ksEntryMap.values()) {
                dots.add(new N33_KSDTO(ksEntry));
            }
        }
        return dots;
    }


    public List<N33_KSDTO> getIsolateSubjectListByZKB(ObjectId xqid, ObjectId sid) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cids.add(paiKeTimes.getID());
            }
        }
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntry(cids, sid);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if (dots.size() > 0) {
                Boolean bf = true;
                for (N33_KSDTO dto : dots) {
                    if (dto.getSubid().equals(entry.getSubjectId().toString())) {
                        bf = false;
                    }
                }
                if (bf) {
                    dots.add(new N33_KSDTO(entry));
                }
            } else {
                dots.add(new N33_KSDTO(entry));
            }
        }
        return dots;
    }

    public List<N33_KSDTO> getIsolateSubjectListByZKB(ObjectId xqid, ObjectId sid,String gradeId) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cids.add(paiKeTimes.getID());
            }
        }
        List<N33_KSEntry> entries = subjectDao.getIsolateSubjectEntry(cids, sid,gradeId);
        List<N33_KSDTO> dots = new ArrayList<N33_KSDTO>();
        for (N33_KSEntry entry : entries) {
            if (dots.size() > 0) {
                Boolean bf = true;
                for (N33_KSDTO dto : dots) {
                    if (dto.getSubid().equals(entry.getSubjectId().toString())) {
                        bf = false;
                    }
                }
                if (bf) {
                    dots.add(new N33_KSDTO(entry));
                }
            } else {
                dots.add(new N33_KSDTO(entry));
            }
        }
        return dots;
    }


    public void updateZhuanDan(String id, Integer ty) {
        subjectDao.updateDanShuangZhou("dan", id, ty);
    }

    public void updateDanShuangZhou(String id, Integer ty, Integer type) {
        //非等级考
        if (ty != 1) {
            subjectDao.updateDanShuangZhou("type", id, type);
        } else {
            subjectDao.updateDanShuangZhou("type1", id, type);
        }
    }

    /**
     * @param cid  次id
     * @param ncid 同步的次的id
     */
    public void IsolateSubListByNewCi(ObjectId cid, ObjectId ncid) {
        //指定的次的id
        List<N33_KSEntry> teaEntries = subjectDao.IsolateSubListByNewCi(cid);
        //清楚同步次的老师数据
        subjectDao.delSubByCid(ncid);
        for (N33_KSEntry ksEntry : teaEntries) {
            ksEntry.setID(new ObjectId());
            ksEntry.setXQId(ncid);
        }
        if (teaEntries.size() > 0) {
            subjectDao.add(teaEntries);
        }

    }
}
