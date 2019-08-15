package com.fulaan.new33.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.jar.JarEntry;

import com.db.new33.N33_JXZDao;
import com.db.new33.isolate.*;
import com.db.new33.paike.*;
import com.fulaan.new33.dto.isolate.N33_KSDTO;
import com.fulaan.new33.dto.paike.N33_ZKBDTO;
import com.fulaan.new33.service.isolate.IsolateSubjectService;
import com.fulaan.new33.service.isolate.IsolateTermService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.mongodb.BasicDBObject;
import com.pojo.new33.isolate.*;
import com.pojo.new33.paike.*;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ClassInfo;
import org.springframework.stereotype.Service;

import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.isolate.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by albin on 2018/3/26.
 */
@Service
public class N33_JXBService extends BaseService {

    private ClassDao classDao = new ClassDao();
    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();
    private N33_StudentDao studentDao = new N33_StudentDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private N33_ZhuanXiangDao zhuanXiangDao = new N33_ZhuanXiangDao();
    private N33_TeaDao teaDao = new N33_TeaDao();
    private GradeDao gradeDao = new GradeDao();
    private SubjectDao subjectDao = new SubjectDao();
    private N33_ClassDao n33_classDao = new N33_ClassDao();
    private N33_JXZDao jxzDao = new N33_JXZDao();
    private N33_ZKBDao zkbDao = new N33_ZKBDao();
    private TermDao termDao = new TermDao();
    private N33_YKBDao ykbDao = new N33_YKBDao();

    @Autowired
    private N33_TurnOffService turnOffService;

    @Autowired
    private IsolateTermService termService;

    @Autowired
    private PaiKeService paiKeService;

    @Autowired
    private IsolateSubjectService subjectService;

    @Autowired
    private N33_AfreshService n33_afreshService;

    public void exportByGrade(ObjectId sid,ObjectId xqid,ObjectId gid,HttpServletResponse response,HttpServletRequest request) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Grade grade = gradeDao.getIsolateGrADEEntrysByGid(xqid,sid,gid);
        List<ClassEntry> classInfos = classDao.findByGradeIdIdOrderByXH(sid,gid,xqid);
        HttpSession session = request.getSession();
        Map<String, Object> status = new HashMap<String, Object>();
        status.put("st", 1);
        session.setAttribute("dcjxb", status);
        List<String> stringList = subjectService.getSubjectZouBan(sid,xqid,gid);
        for (ClassEntry classEntry : classInfos) {
            //生成一个sheet1
            HSSFSheet sheet = wb.createSheet(grade.getName() + "(" + classEntry.getXh() + ")班学生教学班列表");
            status.put("className", grade.getName() + "(" + classEntry.getXh() + ")班学生教学班列表");
            //为sheet1生成第一行，用于放表头信息
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = null;
            for (int i = 0; i < stringList.size();i ++) {
                cell = row.createCell(i);
                cell.setCellValue(stringList.get(i));
            }
            List<Map<String,Object>> retList = getStuJXB(xqid,classEntry.getClassId(),sid,gid);
            for (int i = 0; i < retList.size(); i ++) {
                row = sheet.createRow(i + 1);
                List<String> jxbNames = (List<String>) retList.get(i).get("jxbNames");
                cell = row.createCell(0);
                cell.setCellValue((String) retList.get(i).get("stuName"));
                cell = row.createCell(1);
                cell.setCellValue((String) retList.get(i).get("sexStr"));
                cell = row.createCell(2);
                cell.setCellValue((String) retList.get(i).get("stuNum"));
                for (int j = 3; j < 3 + jxbNames.size(); j ++) {
                    cell = row.createCell(j);
                    cell.setCellValue(jxbNames.get(j - 3));
                }
            }
        }
        status.put("st", -1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("学生教学班列表.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void exportByClass(ObjectId sid,ObjectId xqid,ObjectId gid,ObjectId classId,HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("学生教学班列表");
        List<String> stringList = subjectService.getSubjectZouBan(sid,xqid,gid);
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;
        for (int i = 0; i < stringList.size();i ++) {
            cell = row.createCell(i);
            cell.setCellValue(stringList.get(i));
        }
        List<Map<String,Object>> retList = getStuJXB(xqid,classId,sid,gid);
        for (int i = 0; i < retList.size(); i ++) {
            row = sheet.createRow(i + 1);
            List<String> jxbNames = (List<String>) retList.get(i).get("jxbNames");
            cell = row.createCell(0);
            cell.setCellValue((String) retList.get(i).get("stuName"));
            cell = row.createCell(1);
            cell.setCellValue((String) retList.get(i).get("sexStr"));
            cell = row.createCell(2);
            cell.setCellValue((String) retList.get(i).get("stuNum"));
            for (int j = 3; j < 3 + jxbNames.size(); j ++) {
                cell = row.createCell(j);
                cell.setCellValue(jxbNames.get(j - 3));
            }
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("学生教学班列表.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Map<String,Object>> getStuJXB(ObjectId ciId,ObjectId classId,ObjectId sid,ObjectId gradeId){
        List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();
        ClassEntry classEntry = classDao.findIsolateClassEntryByCId(classId,ciId);
        Map<ObjectId,N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByIdsForSubId(ciId,gradeId,sid);
        List<ObjectId> stuIds = classEntry.getStudentList();
        Map<ObjectId,StudentEntry> studentEntryMap = studentDao.getStuMapIdEntry(ciId);
        List<N33_KSDTO> ksdtos = subjectService.getIsolateSubjectListBySchoolIdZouBan(ciId,sid,gradeId);
	    Collections.sort(ksdtos, new Comparator<N33_KSDTO>() {
		    @Override
		    public int compare(N33_KSDTO o1, N33_KSDTO o2) {
			    return o1.getSnm().hashCode() - o2.getSnm().hashCode();
		    }
	    });
	    List<ObjectId> subIds = new ArrayList<ObjectId>();
	    for (N33_KSDTO ksdto : ksdtos) {
			subIds.add(new ObjectId(ksdto.getSubid()));
	    }
        Integer count = ksdtos.size();
        for (ObjectId stuId : stuIds) {
            Map<String,Object> map = new HashMap<String, Object>();

            //jxb add
            int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
          	//jxb add
            List<N33_JXBEntry> jxbEntries = jxbDao.getStuJXBList(gradeId, ciId, stuId,acClassType);
            List<N33_JXBEntry> jxbEntryList = new ArrayList<N33_JXBEntry>();
            for (N33_JXBEntry jxbEntry : jxbEntries) {
                if(jxbEntry.getType() == 1 || jxbEntry.getType() == 2){
                    jxbEntry.setSubName(ksEntryMap.get(jxbEntry.getSubjectId()).getSubjectName());
                    jxbEntryList.add(jxbEntry);
                }
            }

            Collections.sort(jxbEntryList, new Comparator<N33_JXBEntry>() {
                @Override
                public int compare(N33_JXBEntry o1, N33_JXBEntry o2) {
                    return o1.getSubName().hashCode() - o2.getSubName().hashCode();
                }
            });
            List<String> jxbNames = new ArrayList<String>();
            Integer continueCount = 0;
            for (N33_JXBEntry jxbEntry : jxbEntryList) {
				if(subIds.size() - 1 > continueCount){
					if(subIds.get(continueCount).equals(jxbEntry.getSubjectId())){
						if(jxbEntry.getNickName() == null || "".equals(jxbEntry.getNickName())){
							jxbNames.add(jxbEntry.getName());
						}else{
							jxbNames.add(jxbEntry.getNickName());
						}
					}else{
						jxbNames.add("");
						continueCount ++;
						while(true){
						    if(subIds.size() - 1 < continueCount){
                                break;
                            }
							if(subIds.get(continueCount).equals(jxbEntry.getSubjectId())){
								if(jxbEntry.getNickName() == null || "".equals(jxbEntry.getNickName())){
									jxbNames.add(jxbEntry.getName());
								}else{
									jxbNames.add(jxbEntry.getNickName());
								}
								break;
							}else{
								jxbNames.add("");
								continueCount ++;
							}
						}
					}
				}else{
					if(jxbEntry.getNickName() == null || "".equals(jxbEntry.getNickName())){
						jxbNames.add(jxbEntry.getName());
					}else{
						jxbNames.add(jxbEntry.getNickName());
					}
				}
				continueCount ++;
            }
            while(true){
                if(jxbNames.size() < count){
                    jxbNames.add("");
                }else{
                    break;
                }
            }
            map.put("jxbNames",jxbNames);
            map.put("stuName",studentEntryMap.get(stuId).getUserName());
            map.put("stuNum",studentEntryMap.get(stuId).getStudyNum());
            if(studentEntryMap.get(stuId).getSex() == 0){
                map.put("sexStr","女");
            }else if(studentEntryMap.get(stuId).getSex() == 1){
                map.put("sexStr","男");
            }else{
                map.put("sexStr","未知");
            }
            retList.add(map);
        }
        return retList;
    }

    public void delZhanXu(ObjectId id) {
        N33_ZhuanXiangEntry xiangEntry = zhuanXiangDao.findN33_ZhuanXiangEntryById(id);
        zhuanXiangDao.delete(id);
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(xiangEntry.getJxbId());
        List<ObjectId> stuList = jxbEntry.getStudentIds();
        stuList.removeAll(xiangEntry.getStudentId());
        jxbEntry.setStudentIds(stuList);
        jxbDao.updateN33_JXB(jxbEntry);
    }

    public void delZu(ObjectId id) {
        jxbDao.deleteN33_JXB(id);
        zhuanXiangDao.deleteByJxb(id);
    }

    public void addjxbStu(ObjectId jxbId, List<ObjectId> stuId) {
        N33_JXBEntry entry = jxbDao.getJXBById(jxbId);
        ObjectId reId = entry.getRelativeId();
        if (entry.getType() == 3 || entry.getType() == 6) {
            if (reId != null) {
                List<N33_JXBEntry> entryList = jxbDao.getRaleJXBList(entry.getTermId(), entry.getID(), reId);
                for (N33_JXBEntry entry1 : entryList) {
                    List<ObjectId> stIds = entry1.getStudentIds();
                    for (ObjectId oid : stuId) {
                        if (!stIds.contains(oid)) {
                            stIds.add(oid);
                        }
                    }
                    entry1.setStudentIds(stIds);
                    jxbDao.updateN33_JXB(entry1);
                }
            }
        }
        List<ObjectId> stuIds = entry.getStudentIds();
        stuIds.addAll(stuId);
        entry.setStudentIds(stuIds);
        jxbDao.updateN33_JXB(entry);
    }


    public void upZhuan(Map map) {
        ObjectId id = new ObjectId((String) map.get("id"));
        //班级列表
        List<String> stuIdsStr = (List<String>) map.get("stu");
        List<ObjectId> stuIds = MongoUtils.convertToObjectIdList(stuIdsStr);
        N33_ZhuanXiangEntry xiangEntry = zhuanXiangDao.findN33_ZhuanXiangEntryById(id);
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(xiangEntry.getJxbId());
        List<ObjectId> jxbStuIds = jxbEntry.getStudentIds();
        jxbStuIds.removeAll(xiangEntry.getStudentId());
        xiangEntry.setStudentId(stuIds);
        zhuanXiangDao.updateN33_ZhuanXiangEntry(xiangEntry);
        if (jxbStuIds.size() > 0) {
            for (ObjectId stuId : stuIds) {
                if (!jxbStuIds.contains(stuId)) {
                    jxbStuIds.add(stuId);
                }
            }
            jxbEntry.setStudentIds(jxbStuIds);
        } else {
            jxbEntry.setStudentIds(stuIds);
        }
        jxbDao.updateN33_JXB(jxbEntry);
    }

    public void addZhuanXiang(Map map, ObjectId sid) {
        //次id
        ObjectId xqid = new ObjectId((String) map.get("xqid"));
        //年级id
        ObjectId gradeId = new ObjectId((String) map.get("gradeId"));
        //班级列表
        List<String> classIdsStr = (List<String>) map.get("cls");
        List<ObjectId> classIds = MongoUtils.convertToObjectIdList(classIdsStr);
        //学科
        ObjectId subjectId = new ObjectId((String) map.get("subjectId"));

        int acClassType = turnOffService.getAcClassType(sid, gradeId, xqid,1);

        //组合名
        String name = (String) map.get("name");
        N33_KSEntry ksEntry=subjectDao.findIsolateSubjectEntryById(xqid,sid,subjectId,gradeId);
        N33_JXBEntry entry = new N33_JXBEntry(
                name,
                name,
                subjectId,
                acClassType,
                null,
                null,
                new ArrayList<ObjectId>(),
                sid,
                xqid,
                0,
                gradeId,
                4,
                new ArrayList<ObjectId>(),
                0,
                0
        );
        entry.setClassIds(classIds);
        entry.setJXBKS(ksEntry.getTime());
        jxbDao.addN33_JXBEntry(entry);
    }

    public List<Map<String, Object>> getZhuanXiangZuList(ObjectId xqid, ObjectId jxbId) {
        List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbId, xqid);
        List<ObjectId> roomIds = new ArrayList<ObjectId>();
        List<ObjectId> teaIds = new ArrayList<ObjectId>();
        for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntries) {
            roomIds.add(zhuanXiangEntry.getRoomId());
            teaIds.add(zhuanXiangEntry.getTeaId());
        }
        Map<ObjectId, N33_ClassroomEntry> classroomEntryMap = classroomDao.getRoomEntryListByXqRoomMapRoom(xqid, roomIds);
        Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teaIds, xqid);
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(jxbId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntries) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", zhuanXiangEntry.getName());
            map.put("room", classroomEntryMap.get(zhuanXiangEntry.getRoomId()).getRoomName());
            map.put("tea", teaEntryMap.get(zhuanXiangEntry.getTeaId()).getUserName());
            map.put("roomId", zhuanXiangEntry.getRoomId().toString());
            map.put("teaId", zhuanXiangEntry.getTeaId().toString());
            map.put("size", zhuanXiangEntry.getStudentId().size());
            map.put("zh", jxbEntry.getName());
            map.put("id", zhuanXiangEntry.getID().toString());
            list.add(map);
        }
        return list;
    }

    public Map<String, Object> getZhuan(ObjectId id) {
        N33_ZhuanXiangEntry zhuanXiangEntry = zhuanXiangDao.findN33_ZhuanXiangEntryById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", MongoUtils.convertToStringList(zhuanXiangEntry.getStudentId()));
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(zhuanXiangEntry.getJxbId());
        map.put("cids", MongoUtils.convertToStringList(jxbEntry.getClassIds()));
        return map;
    }

    public String[] getZhuanList(ObjectId id) {
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(id);
        List<ClassEntry> classEntryMap = classDao.findClassListByIds(jxbEntry.getClassIds(), jxbEntry.getTermId());
        String[] result = new String[classEntryMap.size()];
        for (Integer a = 0; a < classEntryMap.size(); a++) {
            result[a] = classEntryMap.get(a).getXh() + "";
        }
        return result;
    }

    public void updateZhuanName(ObjectId id, String name) {
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(id);
        jxbEntry.setName(name);
        jxbEntry.setNickName(name);
        jxbDao.updateN33_JXB(jxbEntry);
    }

    public void updateZhuanKS(ObjectId id, Integer ks) {
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(id);
        jxbEntry.setJXBKS(ks);
        jxbDao.updateN33_JXB(jxbEntry);
    }
    public List<Map<String, Object>> getZhuanXiangList(ObjectId xqid, ObjectId gradeId, String subjectId, ObjectId sid) {
        List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
      //jxb add
        ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        //jxb add
        if (subjectId.equals("*")) {
            jxbEntries = jxbDao.getJXBList(sid, gradeId, xqid,4, acClassType);
        } else {
            jxbEntries = jxbDao.getJXBList(sid, gradeId, new ObjectId(subjectId), xqid, 4, acClassType);
        }
        Grade grade = gradeDao.getIsolateGrADEEntrysByGid(xqid, sid, gradeId);
        Map<ObjectId, N33_KSEntry> sub = subjectDao.findSubjectsByIdsForSubId(xqid, gradeId, sid);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            classIds.addAll(jxbEntry.getClassIds());
        }
        Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMapByGradeId(classIds, xqid);
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            if (sub.get(jxbEntry.getSubjectId()) == null) {
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", jxbEntry.getID().toString());
            map.put("name", jxbEntry.getName());
            map.put("count", jxbEntry.getStudentIds().size());
            map.put("subName", sub.get(jxbEntry.getSubjectId()).getSubjectName());
            map.put("subId", jxbEntry.getSubjectId().toString());
            map.put("jxbks", jxbEntry.getJXBKS());
            String clsName = "";
            Integer count = 0;
            for (ObjectId cls : jxbEntry.getClassIds()) {
                ClassEntry classEntry = classEntryMap.get(cls);
                if (count == 0) {
                    clsName += grade.getName() + "(" + classEntry.getXh() + ")(" + classEntry.getStudentList().size() + ")";
                } else {
                    clsName += "," + grade.getName() + "(" + classEntry.getXh() + ")(" + classEntry.getStudentList().size() + ")";
                }
                count++;
            }
            map.put("clsName", clsName);
            list.add(map);
        }
        return list;
    }

    public void addZhuanXiangZu(ObjectId xqid, ObjectId jxbId, String name, ObjectId roomId, ObjectId teacherId, ObjectId sid) {
        N33_ZhuanXiangEntry zhuanXiangEntry = new N33_ZhuanXiangEntry(xqid, name, jxbId, sid, roomId, teacherId, new ArrayList<ObjectId>());
        List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(zhuanXiangEntry.getCiId(),sid);
        String ykbIds = "";
        for (N33_YKBEntry ykbEntry : ykbEntryList) {
            if(ykbEntry.getType() == 4 && (jxbId.toString().equals(ykbEntry.getJxbId().toString()) || (ykbEntry.getNJxbId() != null && jxbId.toString().equals(ykbEntry.getNJxbId().toString())))){
                ykbIds += ykbEntry.getID();
                ykbIds += ",";
            }
        }
        paiKeService.undoYKB(ykbIds,sid);
        zhuanXiangDao.addBEntry(zhuanXiangEntry);
    }

    public void upZhuanXiangZu(ObjectId xqid, ObjectId jxbId, String name, ObjectId roomId, ObjectId teacherId, ObjectId sid, ObjectId id) {
        N33_ZhuanXiangEntry zhuanXiangEntry = new N33_ZhuanXiangEntry(xqid, name, jxbId, sid, roomId, teacherId, new ArrayList<ObjectId>());
        N33_ZhuanXiangEntry xiangEntry = zhuanXiangDao.findN33_ZhuanXiangEntryById(id);
        if((xiangEntry.getRoomId() != null && !xiangEntry.getRoomId().toString().equals(roomId.toString())) || (xiangEntry.getTeaId() != null && !xiangEntry.getTeaId().toString().equals(teacherId.toString()))){
            List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysList(xiangEntry.getCiId(),sid);
            String ykbIds = "";
            for (N33_YKBEntry ykbEntry : ykbEntryList) {
                if(ykbEntry.getType() == 4 && (jxbId.toString().equals(ykbEntry.getJxbId().toString()) || (ykbEntry.getNJxbId() != null && jxbId.toString().equals(ykbEntry.getNJxbId().toString())))){
                    ykbIds += ykbEntry.getID();
                    ykbIds += ",";
                }
            }
            paiKeService.undoYKB(ykbIds,sid);
        }
        zhuanXiangEntry.setStudentId(xiangEntry.getStudentId());
        zhuanXiangEntry.setID(xiangEntry.getID());
        zhuanXiangDao.updateN33_ZhuanXiangEntry(zhuanXiangEntry);
    }

    /**
     * 查询对应学期，对应年级是否已经生成非走班教学班
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public boolean  IsCreateJXB(ObjectId xqid, ObjectId sid, ObjectId gid, int acClassType) {
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gid, xqid, acClassType);
        Map<ObjectId, N33_KSEntry> ksMap = subjectDao.findSubjectsByIdsMapSubId(xqid, gid, sid);
        for (N33_JXBEntry n33_JXBEntry : jxbEntries) {
            if(null!=ksMap.get(n33_JXBEntry.getSubjectId())) {
                if (acClassType == 0) {
                    if (ksMap.get(n33_JXBEntry.getSubjectId()).getIsZouBan() != 1) {
                        return false;
                    }
                } else {
                    if (n33_JXBEntry.getType() != 1 && n33_JXBEntry.getType() != 2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 查询对应学期，对应年级是否已经生成走班教学班
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public boolean IsCreateZouBanJXB(ObjectId xqid, ObjectId sid, ObjectId gid, int acClassType) {
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(sid, gid, xqid, acClassType);
        Map<ObjectId, N33_KSEntry> ksMap = subjectDao.findSubjectsByIdsMapSubId(xqid, gid, sid);
        for (N33_JXBEntry n33_JXBEntry : jxbEntries) {
            if(null!=ksMap.get(n33_JXBEntry.getSubjectId())) {
                if (acClassType == 0) {
                    if (ksMap.get(n33_JXBEntry.getSubjectId()).getIsZouBan() == 1) {
                        return false;
                    }
                } else {
                    if (n33_JXBEntry.getType() == 1 || n33_JXBEntry.getType() == 2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String createJXB(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, Integer rongLiang) {
        String reMsg = "";
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        if(acClassType==0){
            boolean isZouBanJXB = IsCreateZouBanJXB(ciId, schoolId, gradeId, acClassType);
            if (isZouBanJXB) {
                addJiaoXueBanIsZouBan(ciId, gradeId, schoolId, rongLiang);
                reMsg = "保存成功";
            } else {
                reMsg = "已经存在教学班";
            }
        }else{
            boolean isFeiZouBanJXB = IsCreateJXB(ciId, schoolId, gradeId, acClassType);
            boolean isZouBanJXB= IsCreateZouBanJXB(ciId, schoolId, gradeId, acClassType);
            if (isFeiZouBanJXB||isZouBanJXB) {
                //添加重新行政班教学班
                addCFXZBJiaoXueBan(schoolId, gradeId, ciId, isZouBanJXB, isFeiZouBanJXB, acClassType, rongLiang);
                reMsg = "保存成功";
            } else {
                reMsg = "已经存在教学班";
            }
        }
        return reMsg;
    }

    /**
     * 重行政 教学班
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @param isZouBanJXB
     * @param isFeiZouBanJXB
     * @param acClassType
     * @param rongLiang
     */
    public void addCFXZBJiaoXueBan(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId ciId,
            boolean isZouBanJXB,
            boolean isFeiZouBanJXB,
            int acClassType,
            int rongLiang
    ) {
        //全部班级教室
        Map<ObjectId, N33_ClassroomEntry> classRoomMap = classroomDao.getRoomEntryListByXqGradeMapOnlyArranged(ciId, gradeId);

        Map<String, Object> classSubsInfoMap = n33_afreshService.getClassSubsInfoMap(ciId, schoolId, gradeId);

        //基础学科
        List<ObjectId> baseSubIds = (List<ObjectId>)classSubsInfoMap.get("baseSubIds");

        //走班学科
        List<ObjectId> zbSubIds = (List<ObjectId>)classSubsInfoMap.get("zbSubIds");

        //学科map
        Map<String, N33_KSDTO> subMap = (Map<String, N33_KSDTO>)classSubsInfoMap.get("subMap");

        //班级信息
        List<Map> classList = (List<Map>)classSubsInfoMap.get("classList");

        Map<ObjectId, Map<ObjectId, List<ObjectId>>> clsDjSubStuIdsMMap = (Map<ObjectId, Map<ObjectId, List<ObjectId>>>)classSubsInfoMap.get("clsDjSubStuIdsMMap");

        Map<ObjectId, Map<ObjectId, List<ObjectId>>> clsHgSubStuIdsMMap = (Map<ObjectId, Map<ObjectId, List<ObjectId>>>)classSubsInfoMap.get("clsHgSubStuIdsMMap");

        Map<ObjectId, List<ObjectId>> djSubIdClsIdsMap = new HashMap<ObjectId, List<ObjectId>>();

        Map<ObjectId, List<ObjectId>> hgSubIdClsIdsMap = new HashMap<ObjectId, List<ObjectId>>();

        List<ObjectId> zouBanRoomIds = new ArrayList<ObjectId>();

        //重行政-行政教学班
        List<N33_JXBEntry> xzJXbList = new ArrayList<N33_JXBEntry>();
        for(Map cls:classList){
            ObjectId classId = (ObjectId) cls.get("classId");
            String className = (String) cls.get("className");

            N33_ClassroomEntry classroomEntry = classRoomMap.get(classId);
            //班级教室
            ObjectId roomId = null;
            if (classroomEntry != null) {
                roomId = classroomEntry.getRoomId();
            }
            if (isFeiZouBanJXB) {
                List<ObjectId> stuIds = cls.get("stuIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("stuIds");
                List<ObjectId> djXzSubIds = cls.get("djXzSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("djXzSubIds");
                List<ObjectId> hgXzSubIds = cls.get("hgXzSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("hgXzSubIds");
                List<N33_JXBEntry> baseSubJxbList = buildXzJxbList(baseSubIds, subMap, roomId, schoolId, gradeId, ciId, classId, className, stuIds, acClassType, "base");
                xzJXbList.addAll(baseSubJxbList);
                List<N33_JXBEntry> djXzsubJxbList = buildXzJxbList(djXzSubIds, subMap, roomId, schoolId, gradeId, ciId, classId, className, stuIds, acClassType, "dj");
                xzJXbList.addAll(djXzsubJxbList);
                List<N33_JXBEntry> hgXzsubJxbList = buildXzJxbList(hgXzSubIds, subMap, roomId, schoolId, gradeId, ciId, classId, className, stuIds, acClassType, "hg");
                xzJXbList.addAll(hgXzsubJxbList);
            }
            if (isZouBanJXB) {
                int compAdminClass = cls.get("compAdminClass") == null ? 0 : (Integer) cls.get("compAdminClass");
                if (compAdminClass == 0) {
                    //int stuNum = (Integer) cls.get("stuNum");
                    List<ObjectId> djZbSubIds = cls.get("djZbSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("djZbSubIds");
                    List<ObjectId> hgZbSubIds = cls.get("hgZbSubIds") == null ? new ArrayList<ObjectId>() : (List<ObjectId>) cls.get("hgZbSubIds");
                    //Map zouBan = new HashMap(cls);


                    addClsIdToSubIdClsIdsMap(classId, djSubIdClsIdsMap, djZbSubIds);
                    addClsIdToSubIdClsIdsMap(classId, hgSubIdClsIdsMap, hgZbSubIds);
                    if (null != roomId && !zouBanRoomIds.contains(roomId)) {
                        zouBanRoomIds.add(roomId);
                    }
                }
            }
        }
        Map<ObjectId, List<N33_TeaEntry>> subTeasMap = getSubTeasMap(schoolId, gradeId, ciId);
        if (isFeiZouBanJXB) {
            //为教学班设置老师
            setXzJxbTea(xzJXbList, subTeasMap);
            jxbDao.addN33_JXBEntrys(xzJXbList);
        }
        if (isZouBanJXB) {
            for(N33_ClassroomEntry clsRm:classRoomMap.values()){
                if(null==clsRm.getClassId()){
                    if(!zouBanRoomIds.contains(clsRm.getRoomId())){
                        zouBanRoomIds.add(clsRm.getRoomId());
                    }
                }
            }

            List<N33_JXBEntry> zbJXbList = new ArrayList<N33_JXBEntry>();

            List<N33_JXBEntry> djSubZbJxbList = buildZbJxbList(zbSubIds, djSubIdClsIdsMap, clsDjSubStuIdsMMap, subMap, subTeasMap, zouBanRoomIds, schoolId, gradeId, ciId, acClassType, rongLiang, "dj");
            zbJXbList.addAll(djSubZbJxbList);

            List<N33_JXBEntry> hgSubZbJxbList = buildZbJxbList(zbSubIds, hgSubIdClsIdsMap, clsHgSubStuIdsMMap, subMap, subTeasMap, zouBanRoomIds, schoolId, gradeId, ciId, acClassType, rongLiang, "hg");
            zbJXbList.addAll(hgSubZbJxbList);
            jxbDao.addN33_JXBEntrys(zbJXbList);
        }
    }

    /**
     * 为教学班设置老师
     * @param xzJXbList
     * @param subTeasMap
     */
    public void setXzJxbTea(List<N33_JXBEntry> xzJXbList, Map<ObjectId, List<N33_TeaEntry>> subTeasMap) {
        List<ObjectId> allTeaIds = new ArrayList<ObjectId>();
        Map<ObjectId, List<ObjectId>> subTeaIdsMap = new HashMap<ObjectId, List<ObjectId>>();
        for(N33_JXBEntry jxb:xzJXbList){
            List<N33_TeaEntry> subTeas = subTeasMap.get(jxb.getSubjectId());
            if(null==subTeas||subTeas.size()==0){
                continue;
            }
            List<ObjectId> subTeaIds = subTeaIdsMap.get(jxb.getSubjectId());
            if(null==subTeaIds){
                subTeaIds = new ArrayList<ObjectId>();
            }
            List<ObjectId> kyTeaIds = new ArrayList<ObjectId>();

            Map<ObjectId, List<ObjectId>> teaOtherSubsMap = new HashMap<ObjectId, List<ObjectId>>();
            for(N33_TeaEntry teaEntry : subTeas) {
                //教师的数量大于教学班的数量  教师平均分配教学班
                kyTeaIds.add(teaEntry.getUserId());
                List<ObjectId> teaOtherSubs = teaOtherSubsMap.get(teaEntry.getUserId());
                if(null==teaOtherSubs){
                    teaOtherSubs = new ArrayList<ObjectId>();
                }
                for(ObjectId teaSubId:teaEntry.getSubjectList()){
                    if(!teaSubId.equals(jxb.getSubjectId())){
                        teaOtherSubs.add(teaSubId);
                    }
                }
                teaOtherSubsMap.put(teaEntry.getUserId(), teaOtherSubs);
            }
            ObjectId teaId = null;
            boolean reUse = true;
            for(ObjectId kyTeaId:kyTeaIds) {
                if(!subTeaIds.contains(kyTeaId)) {
                    teaId = kyTeaId;
                    reUse = false;

                    break;
                }
            }

            if (reUse) {//教师平均 不然一个科目的的课就有可能是一个教师全部都上了
                if(kyTeaIds.size()>0) {
                    teaId = kyTeaIds.get(0);
                    subTeaIds = new ArrayList<ObjectId>();
                }
            }
            if(null!=teaId) {
                allTeaIds.add(teaId);
                subTeaIds.add(teaId);
                jxb.setTercherId(teaId);
                subTeaIdsMap.put(jxb.getSubjectId(), subTeaIds);
                List<ObjectId> teaOtherSubs = teaOtherSubsMap.get(teaId);
                if(null==teaOtherSubs){
                    teaOtherSubs = new ArrayList<ObjectId>();
                }
                for(ObjectId teaSubId:teaOtherSubs){
                    List<ObjectId> teaIds = subTeaIdsMap.get(teaSubId);
                    if(null==teaIds){
                        teaIds = new ArrayList<ObjectId>();
                    }
                    teaIds.add(teaId);
                    subTeaIdsMap.put(teaSubId, teaIds);
                }
            }
        }
    }

    /**
     * 构建重行政-走班教学班
     * @param zbSubIds
     * @param subIdClsIdsMap
     * @param clsSubStuIdsMMap
     * @param subMap
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @param acClassType
     * @param rongLiang
     * @param subType
     * @return
     */
    public List<N33_JXBEntry> buildZbJxbList(
            List<ObjectId> zbSubIds,
            Map<ObjectId, List<ObjectId>> subIdClsIdsMap,
            Map<ObjectId, Map<ObjectId, List<ObjectId>>> clsSubStuIdsMMap,
            Map<String, N33_KSDTO> subMap,
            Map<ObjectId, List<N33_TeaEntry>> subTeasMap,
            List<ObjectId> zouBanRoomIds,
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId ciId,
            int acClassType,
            int rongLiang,
            String subType

        ) {
        List<N33_JXBEntry> jxbList = new ArrayList<N33_JXBEntry>();
        boolean stuCt = false;
        List<ObjectId> allStuIds = new ArrayList<ObjectId>();
        List<Map<String, Object>> subStuInfoList = new ArrayList<Map<String, Object>>();
        for(ObjectId zbSubId:zbSubIds){
            List<ObjectId> clsIds = subIdClsIdsMap.get(zbSubId);
            Map<String, Object> subStuCount = new HashMap<String, Object>();
            List<ObjectId> subStuIds = new ArrayList<ObjectId>();
            List<List<ObjectId>> subStuIdsList = new ArrayList<List<ObjectId>>();
            int stuNum = 0;
            if(null!=clsIds) {
                for (ObjectId clsId : clsIds) {
                    Map<ObjectId, List<ObjectId>> subStuIdsMap = clsSubStuIdsMMap.get(clsId);
                    if(null==subStuIdsMap){
                        continue;
                    }
                    List<ObjectId> stuIds = getNewList(subStuIdsMap.get(zbSubId));
                    for(ObjectId stuId:stuIds){
                        if(allStuIds.contains(stuId)){
                            stuCt = true;
                        }else {
                            allStuIds.add(stuId);
                        }
                    }
                    int stuCount = stuIds.size();
                    if(stuCount>=rongLiang){
                        subStuIdsList.add(stuIds);
                    }else {
                        stuNum += stuCount;
                        subStuIds.addAll(stuIds);
                    }
                }
            }
            subStuCount.put("subId", zbSubId);
            subStuCount.put("stuNum", stuNum);
            subStuCount.put("subStuIds", subStuIds);
            subStuCount.put("subStuIdsList", subStuIdsList);
            subStuInfoList.add(subStuCount);
        }
        subStuCountListAscSort(subStuInfoList);
        int maxSerial = 1;
        if(stuCt){
            maxSerial = 2;
        }
        Map<Integer, List<ObjectId>> serialStuIdsMap = new HashMap<Integer, List<ObjectId>>();
        Map<ObjectId, List<ObjectId>> subUsedStuIdsMap = new HashMap<ObjectId, List<ObjectId>>();
        Map<ObjectId, Integer> subJxbCountMap = new HashMap<ObjectId, Integer>();
        for(int serial=maxSerial; serial>=1; serial--){
            for(Map<String, Object> subStuInfo:subStuInfoList){
                ObjectId zbSubId = (ObjectId)subStuInfo.get("subId");
                int jxbCount = 1;
                if(null!=subJxbCountMap.get(zbSubId)){
                    jxbCount = subJxbCountMap.get(zbSubId);
                }
                N33_KSDTO subDto = subMap.get(zbSubId.toString());
                if(null==subDto){
                    continue;
                }

                List<ObjectId> subStuIds = null;
                if(null!=subStuInfo.get("subStuIds")){
                    subStuIds = (List<ObjectId>)subStuInfo.get("subStuIds");
                }else{
                    subStuIds = new ArrayList<ObjectId>();
                }
                List<ObjectId> kyStuIds = new ArrayList<ObjectId>();
                List<ObjectId> subUsedStuIds = subUsedStuIdsMap.get(zbSubId);
                if(null==subUsedStuIds){
                    subUsedStuIds = new ArrayList<ObjectId>();
                }
                List<ObjectId> serialStuIds = serialStuIdsMap.get(serial);
                if(null==serialStuIds){
                    serialStuIds = new ArrayList<ObjectId>();
                }
                for(ObjectId subStuId:subStuIds){
                    if(!serialStuIds.contains(subStuId)&&!subUsedStuIds.contains(subStuId)){
                        kyStuIds.add(subStuId);
                    }
                }
                int stuCount = kyStuIds.size();
                int loopCount = stuCount % rongLiang == 0 ? stuCount / rongLiang : stuCount / rongLiang + 1;

                List<Map<String, Object>> clsStuInfoList = new ArrayList<Map<String, Object>>();
                List<ObjectId> clsIds = subIdClsIdsMap.get(zbSubId);
                if(null!=clsIds) {
                    for (ObjectId clsId : clsIds) {
                        Map<ObjectId, List<ObjectId>> subStuIdsMap = clsSubStuIdsMMap.get(clsId);
                        if(null==subStuIdsMap){
                            continue;
                        }
                        Map<String, Object> clsStuInfo = new HashMap<String, Object>();
                        List<ObjectId> clsStuIds = new ArrayList<ObjectId>();
                        List<ObjectId> stuIds = getNewList(subStuIdsMap.get(zbSubId));
                        for(ObjectId stuId:stuIds){
                            if(kyStuIds.contains(stuId)){
                                clsStuIds.add(stuId);
                            }
                        }
                        clsStuInfo.put("clsStuIds", clsStuIds);
                        clsStuInfo.put("stuNum", clsStuIds.size());
                        clsStuInfoList.add(clsStuInfo);
                    }
                }
                subStuCountListDescSort(clsStuInfoList);
                List<List<ObjectId>> clsStuIdsList = new ArrayList<List<ObjectId>>();
                for(Map<String, Object> clsStuInfo:clsStuInfoList){
                    List<ObjectId> clsStuIds = null;
                    if(null!=clsStuInfo.get("clsStuIds")) {
                        clsStuIds = (List<ObjectId>) clsStuInfo.get("clsStuIds");
                    }else{
                        continue;
                    }
                    if(clsStuIdsList.size()<loopCount){
                        if(clsStuIds.size()>0) {
                            clsStuIdsList.add(clsStuIds);
                            serialStuIds.addAll(clsStuIds);
                            subUsedStuIds.addAll(clsStuIds);
                        }
                    }else{
                        int clsStuCount = clsStuIds.size();
                        for(int i=clsStuIdsList.size()-1; i>=0;i--){
                            List<ObjectId> clsStuIdList = clsStuIdsList.get(i);
                            int clsStuListCount = clsStuIdList.size();
                            if(rongLiang>clsStuIdList.size()){
                                if(rongLiang>clsStuListCount){
                                    if(rongLiang>=clsStuListCount+clsStuCount){
                                        clsStuIdList.addAll(clsStuIds);
                                        serialStuIds.addAll(clsStuIds);
                                        subUsedStuIds.addAll(clsStuIds);
                                        break;
                                    }else{
                                        for(ObjectId clsStuId:clsStuIds){
                                            clsStuListCount = clsStuIdList.size();
                                            if(rongLiang>clsStuListCount){
                                                clsStuIdList.add(clsStuId);
                                                serialStuIds.add(clsStuId);
                                                subUsedStuIds.add(clsStuId);
                                            }else{
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(serial==1) {
                    List<List<ObjectId>> subStuIdsList = null;
                    if (null != subStuInfo.get("subStuIdsList")) {
                        subStuIdsList = (List<List<ObjectId>>) subStuInfo.get("subStuIdsList");
                    } else {
                        subStuIdsList = new ArrayList<List<ObjectId>>();
                    }
                    if(subStuIdsList.size()>0){
                        clsStuIdsList.addAll(subStuIdsList);
                        for(List<ObjectId> clsStuIds:clsStuIdsList){
                            serialStuIds.addAll(clsStuIds);
                            subUsedStuIds.addAll(clsStuIds);
                        }
                    }
                }

                serialStuIdsMap.put(serial, serialStuIds);
                subUsedStuIdsMap.put(zbSubId, subUsedStuIds);
                for(List<ObjectId> stuIds: clsStuIdsList){
                    String jxbName = subDto.getSnm();
                    int jxbType = 0;
                    int jxbKs = 0;
                    if("dj".equals(subType)){
                        jxbKs = subDto.getdTime();
                        jxbName+= "D" + jxbCount;
                        jxbType = 1;
                    }
                    if("hg".equals(subType)){
                        jxbKs = subDto.getTime();
                        jxbName+="H"+ jxbCount;
                        jxbType = 2;
                    }

                    N33_JXBEntry entry = new N33_JXBEntry(
                            jxbName,
                            jxbName,
                            zbSubId,
                            acClassType,
                            null,
                            null,
                            stuIds,
                            schoolId,
                            ciId,
                            0,
                            gradeId,
                            jxbType,
                            new ArrayList<ObjectId>(),
                            rongLiang,
                            0
                    );
                    entry.setParentId(null);
                    entry.setJXBKS(jxbKs);
                    entry.setTimeCombSerial(serial);
                    jxbList.add(entry);
                    jxbCount++;
                }
                subJxbCountMap.put(zbSubId, jxbCount);
            }
        }
        //Map<ObjectId, List<N33_TeaEntry>> subTeasMap = getSubTeasMap(schoolId, gradeId, ciId);
        List<ObjectId> allTeaIds = new ArrayList<ObjectId>();
        Map<Integer, List<ObjectId>> serialTeaIdsMap = new HashMap<Integer, List<ObjectId>>();
        Map<Integer, List<ObjectId>> serialRmIdsMap = new HashMap<Integer, List<ObjectId>>();
        for(N33_JXBEntry jxb:jxbList){
            List<N33_TeaEntry> subTeas = subTeasMap.get(jxb.getSubjectId());
            if(null==subTeas){
                subTeas = new ArrayList<N33_TeaEntry>();
            }
            List<ObjectId> serialTeaIds = serialTeaIdsMap.get(jxb.getTimeCombSerial())==null?new ArrayList<ObjectId>():serialTeaIdsMap.get(jxb.getTimeCombSerial());
            List<ObjectId> serialRmIds = serialRmIdsMap.get(jxb.getTimeCombSerial())==null?new ArrayList<ObjectId>():serialRmIdsMap.get(jxb.getTimeCombSerial());

            List<ObjectId> kyTeaIds = new ArrayList<ObjectId>();
            for(N33_TeaEntry teaEntry : subTeas) {
                if (!serialTeaIds.contains(teaEntry.getUserId())) {//保证老师不会在同一个时间点同时上多个班的课
                    //教师的数量大于教学班的数量  教师平均分配教学班
                    kyTeaIds.add(teaEntry.getUserId());
                }
            }

            ObjectId teaId = null;
            boolean reUse = true;
            for(ObjectId kyTeaId:kyTeaIds) {
                if(!allTeaIds.contains(kyTeaId)) {
                    teaId = kyTeaId;
                    reUse = false;
                    break;
                }
            }
            if (reUse) {//教师平均 不然一个科目的的课就有可能是一个教师全部都上了
                if(kyTeaIds.size()>0) {
                    teaId = kyTeaIds.get(0);
                }
            }

            if(null!=teaId) {
                allTeaIds.add(teaId);
                serialTeaIds.add(teaId);
                jxb.setTercherId(teaId);
            }

            ObjectId roomId = null;
            for(ObjectId zbRmId:zouBanRoomIds){
                if(!serialRmIds.contains(zbRmId)){
                    roomId = zbRmId;
                    break;
                }
            }
            if(null!=roomId) {
                serialRmIds.add(roomId);
                jxb.setClassroomId(roomId);
            }
            serialTeaIdsMap.put(jxb.getTimeCombSerial(), serialTeaIds);
            serialRmIdsMap.put(jxb.getTimeCombSerial(), serialRmIds);
        }
        return jxbList;
    }

    /**
     * 学科老师map
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @return
     */
    public Map<ObjectId, List<N33_TeaEntry>> getSubTeasMap(ObjectId schoolId, ObjectId gradeId, ObjectId ciId){
        Map<ObjectId, List<N33_TeaEntry>> reMap = new HashMap<ObjectId, List<N33_TeaEntry>>();
        List<N33_TeaEntry> teaList = teaDao.getTeaList(schoolId, gradeId, ciId);
        for(N33_TeaEntry teaEntry:teaList){
            for(ObjectId subId:teaEntry.getSubjectList()){
                List<N33_TeaEntry> teas = reMap.get(subId);
                if(null==teas){
                    teas = new ArrayList<N33_TeaEntry>();
                }
                teas.add(teaEntry);
                reMap.put(subId, teas);
            }
        }
        return reMap;
    }

    /**
     *
     * @param oldList
     * @return
     */
    public List<ObjectId> getNewList(List<ObjectId> oldList){
        List<ObjectId> newList = null;
        if(null!=oldList){
            newList = new ArrayList<ObjectId>(oldList);
        }else{
            newList = new ArrayList<ObjectId>();
        }
        return newList;
    }

    /**
     * 根据学科学生数量排序 低->高
     * @param subStuCounts
     */
    public void subStuCountListAscSort(List<Map<String, Object>> subStuCounts) {
        Collections.sort(subStuCounts, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
                int stuCount0 = arg0.get("stuNum")==null?0:(Integer) arg0.get("stuNum");
                int stuCount1 = arg1.get("stuNum")==null?0:(Integer)arg1.get("stuNum");
                return stuCount0-stuCount1;
            }
        });
    }

    /**
     * 根据学科学生数量排序 高->低
     * @param subStuCounts
     */
    public void subStuCountListDescSort(List<Map<String, Object>> subStuCounts) {
        Collections.sort(subStuCounts, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
                int stuCount0 = arg0.get("stuNum")==null?0:(Integer) arg0.get("stuNum");
                int stuCount1 = arg1.get("stuNum")==null?0:(Integer)arg1.get("stuNum");
                return stuCount1-stuCount0;
            }
        });
    }

    /**
     * 重行政 教学班
     * @param classId
     * @param subIdClsIdsMap
     * @param zbSubIds
     */
    public void addClsIdToSubIdClsIdsMap(
            ObjectId classId,
            Map<ObjectId, List<ObjectId>> subIdClsIdsMap,
            List<ObjectId> zbSubIds
    ) {
        for(ObjectId subId:zbSubIds){
            List<ObjectId> clsIds = subIdClsIdsMap.get(subId);
            if(null==clsIds){
                clsIds = new ArrayList<ObjectId>();
            }
            if(!clsIds.contains(classId)) {
                clsIds.add(classId);
                subIdClsIdsMap.put(subId, clsIds);
            }
        }
    }

    /**
     * 构建重行政-行政教学班
     * @param subIds
     * @param subMap
     * @param roomId
     * @param schoolId
     * @param gradeId
     * @param ciId
     * @param classId
     * @param className
     * @param stuIds
     * @param acClassType
     * @param subType
     * @return
     */
    public List<N33_JXBEntry> buildXzJxbList(
            List<ObjectId> subIds,
            Map<String, N33_KSDTO> subMap,
            ObjectId roomId,
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId ciId,
            ObjectId classId,
            String className,
            List<ObjectId> stuIds,
            int acClassType,
            String subType
    ) {
        List<N33_JXBEntry> jxbList = new ArrayList<N33_JXBEntry>();
        for(ObjectId subId:subIds){
            N33_KSDTO subDto = subMap.get(subId.toString());
            if(null==subDto){
                continue;
            }
            String jxbName = subDto.getSnm();
            int jxbKs = 0;
            if("base".equals(subType)){
                jxbKs = subDto.getTime();
            }
            if("dj".equals(subType)){
                jxbKs = subDto.getdTime();
                jxbName+="D";
            }
            if("hg".equals(subType)){
                jxbKs = subDto.getTime();
                jxbName+="H";
            }
            jxbName += className;
            N33_JXBEntry entry = new N33_JXBEntry(
                    jxbName,
                    jxbName,
                    subId,
                    acClassType,
                    roomId,
                    null,
                    stuIds,
                    schoolId,
                    ciId,
                    0,
                    gradeId,
                    3,
                    new ArrayList<ObjectId>(),
                    stuIds.size(),
                    0
            );
            entry.setParentId(classId);
            entry.setJXBKS(jxbKs);
            jxbList.add(entry);
        }
        return jxbList;
    }

    /**
     * *
     * 模式自动（走班）
     *
     * @param grade
     * @return
     */
    public void addJiaoXueBanIsZouBan(ObjectId xqid, ObjectId grade, ObjectId sid, Integer rongLiang) {
        //全部年级的班级
        //List<ClassEntry> gradeClass = classDao.findByGradeIdId(sid, grade, xqid);

        //全部班级教室
        //Map<ObjectId, N33_ClassroomEntry> classRoom = classroomDao.getRoomEntryListByXqGradeMap(xqid, grade);

        //非教学班
        //jxbEntries.addAll(addFeiZouBan(gradeClass, studentList, classRoom, ksEntries, sid, xqid, grade));

        //全部课时
        List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, grade);
        //学生列表
        List<StudentEntry> studentList = studentDao.getStudentByXqidAndGradeId(grade, xqid);
        //教学班list
        List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
        //教学班
        jxbEntries.addAll(addJiaoXueBanIsZouBan(ksEntries, studentList, rongLiang, sid, xqid, grade));
        jxbDao.addN33_JXBEntrys(jxbEntries);
    }

    public String createFeiZouBanJXB(ObjectId schoolId, ObjectId gradeId, ObjectId ciId) {
        String reMsg = "";
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        if(acClassType==0) {
            if (IsCreateJXB(ciId, schoolId, gradeId, acClassType)) {
                addJiaoXueBanIsFeiZouBan(ciId, gradeId, schoolId);
                reMsg = "保存成功";
            } else {
                reMsg = "已经存在教学班";
            }
        }
        return reMsg;
    }

    /**
     * *
     * 模式自动（语数英体育）
     *
     * @param grade
     * @return
     */
    public void addJiaoXueBanIsFeiZouBan(ObjectId xqid, ObjectId grade, ObjectId sid) {
        //全部年级的班级
        List<ClassEntry> gradeClass = classDao.findByGradeIdId(sid, grade, xqid);
        //全部课时
        List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByXqid(xqid, sid, grade);
        //全部班级教室
        Map<ObjectId, N33_ClassroomEntry> classRoom = classroomDao.getRoomEntryListByXqGradeMap(xqid, grade);
        //学生列表
        List<StudentEntry> studentList = studentDao.getStudentByXqidAndGradeId(grade, xqid);
        //教学班list
        List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
        //语数英和其他的
        jxbEntries.addAll(addFeiZouBanFei(gradeClass, studentList, classRoom, ksEntries, sid, xqid, grade));
        jxbDao.addN33_JXBEntrys(jxbEntries);
    }


    /**
     * 增加教学班
     *
     * @param dto
     */
    public String addJXBDto(N33_JXBDTO dto) {
        ObjectId schoolId = new ObjectId(dto.getSchoolId());
        ObjectId gradeId = new ObjectId(dto.getGradeId());
        ObjectId subjectId = new ObjectId(dto.getSubjectId());
        ObjectId ciId = new ObjectId(dto.getTermId());

        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);

        N33_JXBEntry jxbEntry = jxbDao.getJXBEntry(dto.getNickName(), ciId, gradeId, acClassType);
        if (jxbEntry != null) {
            return "该教学班自定义名称已存在";
        } else {
            N33_KSEntry subEntry = subjectDao.findIsolateSubjectEntryById(ciId, schoolId, subjectId, gradeId);
            N33_JXBEntry entry = new N33_JXBEntry(
                    dto.getName(),
                    dto.getNickName(),
                    new ObjectId(dto.getSubjectId()),
                    acClassType,
                    null,
                    null,
                    new ArrayList<ObjectId>(),
                    new ObjectId(dto.getSchoolId()),
                    new ObjectId(dto.getTermId()),
                    0,
                    new ObjectId(dto.getGradeId()),
                    dto.getType(),
                    new ArrayList<ObjectId>(),
                    dto.getRl(),
                    0
            );
            if (dto.getType() == 1) {
                entry.setJXBKS(subEntry.getDTime());
            } else {
                entry.setJXBKS(subEntry.getTime());
            }
            List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(schoolId, gradeId, subjectId, ciId, acClassType);
            Integer count = 1;
            for (N33_JXBEntry entry1 : jxbEntryList) {
                if (dto.getType() != 3) {
                    if (entry1.getType() == dto.getType()) {
                        count++;
                    }
                } else {
                    if (entry1.getType() == 3 || entry1.getType() == 6) {
                        count++;
                    }
                }
            }
            String name = "";
            if (dto.getType() == 1) {
                name= subEntry.getSubjectName() + "D" + count;
            }
            if (dto.getType() == 2) {
                name= subEntry.getSubjectName() + "H" + count;
            }
            if (dto.getType() == 3) {
                Grade grade= gradeDao.getIsolateGrADEEntrysByGid(ciId, schoolId, gradeId);
                name= subEntry.getSubjectName() +grade.getName()+ "(" + count+")";
            }
            entry.setName(name);
            jxbDao.addN33_JXBEntry(entry);
            return "增加成功";
        }
    }

    public Map<String, Object> getSubjectDAndH(ObjectId sid, ObjectId gid, long time, ObjectId subid) {
        //返回的map
        Map<String, Object> returnMap = new HashMap<String, Object>();
        N33_JXZEntry entry = jxzDao.getJXZByDate(sid,getDefauleTermId(sid),time);
        Integer week = entry.getSerial();
        List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeekAndGradeIdAndWeekAndTermId(sid,week,gid,getDefauleTermId(sid));
        List<String> hgStuIds = new ArrayList<String>();
        List<String> djStuIds = new ArrayList<String>();
        ObjectId xqid = null;
        if(zkbEntryList != null && zkbEntryList.size() != 0){
            xqid = zkbEntryList.get(0).getCId();
        }else{
            returnMap.put("hgStuIds",hgStuIds);
            returnMap.put("djStuIds",djStuIds);
            return returnMap;
        }

        //查找对应的学科
        N33_KSEntry ksEntry = subjectDao.findIsolateSubjectEntryById(xqid, sid, subid, gid);
        //学生列表
        List<StudentEntry> studentList = studentDao.getStudentByXqidAndGradeId(gid, xqid);
        //如果是走班学科
        if (ksEntry.getIsZouBan() == 1) {
            Integer stuCount = studentList.size();
            //组合中包含的名称
            String subjectName = ksEntry.getSubjectName().substring(0, 1);
            for (StudentEntry student : studentList) {
                if (student.getCombiname().indexOf(subjectName) != -1) {
                    djStuIds.add(student.getUserId().toString());
                }else{
                    hgStuIds.add(student.getUserId().toString());
                }
            }
            returnMap.put("hgStuIds", hgStuIds);
            returnMap.put("djStuIds", djStuIds);
        } else {
            returnMap.put("hgStuIds", hgStuIds);
            returnMap.put("djStuIds", djStuIds);
        }
        return returnMap;
    }

    /**
     * 查找教学班
     * @param tid
     * @param index
     * @param gradeId
     * @param week
     * @param schoolId
     * @return
     */
    public List<N33_JXBDTO> getGradeJXB(ObjectId tid,Integer index,ObjectId gradeId,Integer week,ObjectId schoolId){
        List<N33_JXBDTO> jxbdtoList = new ArrayList<N33_JXBDTO>();
        List<TermEntry> termEntryList = termDao.getIsolateTermEntrysByTid(tid);
        ObjectId termId = null;
        for (TermEntry termEntry : termEntryList) {
            if(index == 1){
                int i = termEntry.getXqName().indexOf("第一学期");
                if(i != -1){
                    termId = termEntry.getID();
                    break;
                }else{
                    continue;
                }
            }else{
                int i = termEntry.getXqName().indexOf("第二学期");
                if(i != -1){
                    termId = termEntry.getID();
                    break;
                }else{
                    continue;
                }
            }
        }
        ObjectId ciId = null;
        if(termId == null){
            return jxbdtoList;
        }else{
            List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeekAndGradeIdAndWeekAndTermId(schoolId,week,gradeId,termId);
			if(zkbEntryList.size() > 0){
				ciId = zkbEntryList.get(0).getCId();
				//jxb add
				int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
				//jxb add
				List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(schoolId, gradeId, ciId, acClassType);
				for (N33_JXBEntry jxbEntry : jxbEntryList) {
				    if(jxbEntry.getType() == 1 || jxbEntry.getType() == 2){
                        jxbdtoList.add(new N33_JXBDTO(jxbEntry));
                    }
				}
			}else{
				return jxbdtoList;
			}
        }
        return jxbdtoList;
    }


    /**
     * 如果是走班学科计算学生等级考和合格考人数，如果是专项或者行政班显示学生人数
     *
     * @param xqid
     * @param sid
     * @param gid
     * @return
     */
    public Map<String, Object> getCount(ObjectId xqid, ObjectId sid, ObjectId gid, ObjectId subid) {
        //返回的map
        Map<String, Object> returnMap = new HashMap<String, Object>();
        //查找对应的学科
        N33_KSEntry ksEntry = subjectDao.findIsolateSubjectEntryById(xqid, sid, subid, gid);
        //学生列表
        List<StudentEntry> studentList = studentDao.getStudentByXqidAndGradeId(gid, xqid);
        //如果是走班学科
        if (ksEntry.getIsZouBan() == 1) {
            Integer stuCount = studentList.size();
            //组合中包含的名称
            String subjectName = ksEntry.getSubjectName().substring(0, 1);
            //选择查询学科人数
            Integer studentSubjectCount = 0;
            for (StudentEntry student : studentList) {
                if (student.getCombiname().indexOf(subjectName) != -1) {
                    studentSubjectCount++;
                }
            }
            //未选查询学科人数
            Integer feiCount = stuCount - studentSubjectCount;
            returnMap.put("studentSubjectCount", studentSubjectCount);//等级考人数
            returnMap.put("feiCount", feiCount);//合格考人数
            returnMap.put("isZouBan", ksEntry.getIsZouBan());//该学科是否走班
            returnMap.put("subjectName", ksEntry.getSubjectName());//学科名
        } else {
            Integer stuCount = studentList.size();
            returnMap.put("isZouBan", ksEntry.getIsZouBan());//该学科是否走班
            returnMap.put("subjectName", ksEntry.getSubjectName());//学科名
            returnMap.put("stuCount", stuCount);//该学科人数
        }
        return returnMap;
    }


    /**
     * *
     * 非走班的教学班
     *
     * @param gradeClass
     * @param studentList
     * @param classRoom
     * @param ksEntries
     * @return
     */
    public List<N33_JXBEntry> addFeiZouBan(List<ClassEntry> gradeClass, List<StudentEntry> studentList, Map<ObjectId, N33_ClassroomEntry> classRoom, List<N33_KSEntry> ksEntries, ObjectId sid, ObjectId xqid, ObjectId grade) {
        List<N33_JXBEntry> jiaoXueBanList = new ArrayList<N33_JXBEntry>();
        Grade grade1 = gradeDao.findIsolateGradeByGradeId(xqid, sid, grade);
        for (ClassEntry classEntry : gradeClass) {
            //学生列表
            List<ObjectId> studentIds = new ArrayList<ObjectId>();
            for (StudentEntry student : studentList) {
                if (student.getClassId().toString().equals(classEntry.getClassId().toString())) {
                    studentIds.add(student.getUserId());
                }
            }
            N33_ClassroomEntry classroomEntry = classRoom.get(classEntry.getClassId());
            //班级教室
            ObjectId roomId = null;
            if (classroomEntry != null) {
                roomId = classroomEntry.getRoomId();
            }
            for (N33_KSEntry ksEntry : ksEntries) {
                //行政班
                if (ksEntry.getIsZouBan() == 0 && ksEntry.getDan() == 0) {
                    Integer type = 3;
                    String jxbName = ksEntry.getSubjectName() + grade1.getName() + "(" + classEntry.getXh() + ")";

                    N33_JXBEntry entry = new N33_JXBEntry(
                            jxbName,
                            jxbName,
                            ksEntry.getSubjectId(),
                            1,
                            roomId,
                            null,
                            studentIds,
                            sid,
                            xqid,
                            0,
                            grade,
                            type,
                            new ArrayList<ObjectId>(),
                            studentIds.size(),
                            0
                    );
                    jiaoXueBanList.add(entry);
                }
            }
        }
        return jiaoXueBanList;
    }

    /**
     * *
     * 添加走班的教学班
     *
     * @param ksEntries
     * @param studentList
     * @param rongLiang
     * @param sid
     * @param xqid
     * @param grade
     * @return
     */
    public List<N33_JXBEntry> addJiaoXueBanIsZouBan(
            List<N33_KSEntry> ksEntries,
            List<StudentEntry> studentList,
            Integer rongLiang,
            ObjectId sid,
            ObjectId xqid,
            ObjectId grade
    ) {
        int acClassType = turnOffService.getAcClassType(sid, grade, xqid,1);
        List<N33_JXBEntry> jxbEntries = new ArrayList<N33_JXBEntry>();
        for (N33_KSEntry ksEntry : ksEntries) {
            if (ksEntry.getIsZouBan() == 1) {
                //全年级人数
                Integer studentCount = studentList.size();
                //组合名（第一个）
                String subjectName = ksEntry.getSubjectName().substring(0, 1);
                //走班人数
                Integer studentSubjectCount = 0;
                for (StudentEntry student : studentList) {
                    if (student.getCombiname().indexOf(subjectName) != -1) {
                        studentSubjectCount++;
                    }
                }
                //非走班
                Integer feiCount = studentCount - studentSubjectCount;
                //非走班教室
                //合格考的教室
                Integer HG = feiCount / rongLiang;
                if (feiCount % rongLiang != 0) {
                    HG++;
                }
                for (Integer count = 1; count <= HG; count++) {
                    if(ksEntry.getTime() > 0){
                        String jxbName = ksEntry.getSubjectName() + "H" + count;
                        N33_JXBEntry entry = new N33_JXBEntry(
                                jxbName,
                                jxbName,
                                ksEntry.getSubjectId(),
                                acClassType,
                                null,
                                null,
                                new ArrayList<ObjectId>(),
                                sid,
                                xqid,
                                0,
                                grade,
                                2,
                                new ArrayList<ObjectId>(),
                                rongLiang,
                                0
                        );
                        entry.setParentId(null);
                        entry.setJXBKS(ksEntry.getTime());
                        jxbEntries.add(entry);
                    }
                }
                //等级考考的教室
                Integer DJ = studentSubjectCount / rongLiang;
                if (studentSubjectCount % rongLiang != 0) {
                    DJ++;
                }
                for (Integer count = 1; count <= DJ; count++) {
                    if(ksEntry.getDTime() > 0){
                        String jxbName = ksEntry.getSubjectName() + "D" + count;
                        N33_JXBEntry entry = new N33_JXBEntry(
                                jxbName,
                                jxbName,
                                ksEntry.getSubjectId(),
                                acClassType,
                                null,
                                null,
                                new ArrayList<ObjectId>(),
                                sid,
                                xqid,
                                0,
                                grade,
                                1,
                                new ArrayList<ObjectId>(),
                                rongLiang,
                                0
                        );
                        entry.setParentId(null);
                        entry.setJXBKS(ksEntry.getDTime());
                        jxbEntries.add(entry);
                    }
                }
            }
        }
        return jxbEntries;
    }


    /**
     * 创建语数英和体育等
     *
     * @param gradeClass
     * @param studentList
     * @param classRoom
     * @param ksEntries
     * @param sid
     * @param xqid
     * @param grade
     * @return
     */
    public List<N33_JXBEntry> addFeiZouBanFei(
            List<ClassEntry> gradeClass,
            List<StudentEntry> studentList,
            Map<ObjectId, N33_ClassroomEntry> classRoom,
            List<N33_KSEntry> ksEntries,
            ObjectId sid,
            ObjectId xqid,
            ObjectId grade
    ) {
        List<N33_JXBEntry> jiaoXueBanList = new ArrayList<N33_JXBEntry>();
        int acClassType = turnOffService.getAcClassType(sid, grade, xqid,1);
        Grade grade1 = gradeDao.findIsolateGradeByGradeId(xqid, sid, grade);
        for (ClassEntry classEntry : gradeClass) {
            //学生列表
            List<ObjectId> studentIds = new ArrayList<ObjectId>();
            for (StudentEntry student : studentList) {
                if (student.getClassId().toString().equals(classEntry.getClassId().toString())) {
                    studentIds.add(student.getUserId());
                }
            }
            N33_ClassroomEntry classroomEntry = classRoom.get(classEntry.getClassId());
            //班级教室
            ObjectId roomId = null;
            if (classroomEntry != null) {
                roomId = classroomEntry.getRoomId();
            }
            for (N33_KSEntry ksEntry : ksEntries) {
                //行政班
                if (ksEntry.getIsZouBan() == 0 && ksEntry.getDan() == 0) {
                    if(ksEntry.getTime() > 0){
                        Integer type = 3;
                        String jxbName = ksEntry.getSubjectName() + grade1.getName() + "(" + classEntry.getXh() + ")";
                        N33_JXBEntry entry = new N33_JXBEntry(
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
                                grade,
                                type,
                                new ArrayList<ObjectId>(),
                                studentIds.size(),
                                0
                        );
                        entry.setParentId(classEntry.getClassId());
                        entry.setJXBKS(ksEntry.getTime());
                        jiaoXueBanList.add(entry);
                    }
                }
            }
        }
        return jiaoXueBanList;
    }

    /**
     * 查询对应学期对应年级对应学科下的所有教学班
     *
     * @param xqid
     * @param gid
     * @param sid
     * @param subId
     * @return
     */
    public List<N33_JXBDTO> getAllJXBList(ObjectId xqid, ObjectId gid, ObjectId sid, ObjectId subId) {
        int acClassType = turnOffService.getAcClassType(sid, gid, xqid,1);
        List<N33_JXBDTO> jxbList = new ArrayList<N33_JXBDTO>();
        List<N33_JXBEntry> entries = jxbDao.getJXBList(sid, gid, subId, xqid, acClassType);
        for (N33_JXBEntry n33_JXBEntry : entries) {
            jxbList.add(new N33_JXBDTO(n33_JXBEntry));
        }
        return jxbList;
    }

    /**
     * 查询对应学期对应年级对应学科下的所有教学班
     *
     * @param xqid
     * @param gid
     * @param sid
     * @param subId
     * @return
     */
    public List<N33_JXBDTO> getAllJXBList(ObjectId xqid, ObjectId gid, ObjectId sid, ObjectId subId, Integer type) {
        List<N33_JXBDTO> jxbList = new ArrayList<N33_JXBDTO>();
        List<N33_JXBEntry> entries = getAllJXBListForEntry(xqid, gid, sid, subId, type);
        for (N33_JXBEntry n33_JXBEntry : entries) {
            jxbList.add(new N33_JXBDTO(n33_JXBEntry));
        }
        return jxbList;
    }

    /**
     * 查询对应学期对应年级对应学科下的所有教学班
     *
     * @param xqid
     * @param gid
     * @param sid
     * @param subId
     * @return
     */
    public List<N33_JXBEntry> getAllJXBListForEntry(ObjectId xqid, ObjectId gid, ObjectId sid, ObjectId subId, Integer type) {
    	List<N33_JXBEntry> entries = new ArrayList<N33_JXBEntry>();
    	//jxb add
    	ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
    	int acClassType = turnOffService.getAcClassType(sid, gid, ciId,1);
    	//jxb add
        if (type == 3) {
            entries = jxbDao.getJXBList(sid, gid, subId, xqid, 3, acClassType);
            entries.addAll(jxbDao.getJXBList(sid, gid, subId, xqid, 6, acClassType));
        } else {
            entries = jxbDao.getJXBList(sid, gid, subId, xqid, type, acClassType);
        }
        return entries;
    }

    /**
     * 批量修改教学班信息
     *
     * @param dtos
     */
    public void updateJXB(List<Map<String, String>> dtos, ObjectId xqid, ObjectId sid, ObjectId gid) {
    	// jxb add 
        ObjectId ciId = getDefaultPaiKeTerm(sid).getPaikeci();
        int acClassType = turnOffService.getAcClassType(sid, gid, xqid,1);
        // jxb add
        Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(sid, gid, xqid, acClassType);
        for (Map<String, String> map : dtos) {
            N33_JXBEntry entry = jxbMap.get(new ObjectId((String) map.get("id")));
            entry.setNickName((String) map.get("nnm"));
            entry.setRongLiang(Integer.parseInt((String) map.get("rl")));
            entry.setJXBKS(Integer.parseInt((String) map.get("jxbks")));
            if(entry.getType()==6){
                N33_JXBEntry entry1 =  jxbDao.getJXBById(entry.getRelativeId());
                entry.setJXBKS(Integer.parseInt((String) map.get("jxbks")));
                jxbDao.updateN33_JXB(entry1);
            }
            jxbDao.updateN33_JXB(entry);
        }
    }

    /**
     * 查询教学班学生数量
     *
     * @param id
     * @return
     */
    public Integer getStuCount(ObjectId id) {
        N33_JXBEntry entry = jxbDao.getJXBById(id);
        return entry.getStudentIds().size();
    }

    /**
     * 删除教学班
     *
     * @param id
     * @return
     */
    public void deleteJXB(ObjectId id) {
        N33_JXBEntry entry = jxbDao.getJXBById(id);
        jxbDao.removeN33_JXBEntry(id);
    }

    /**
     * 删除教学班
     *
     * @param id
     * @return
     */
    public String removeJXB(ObjectId id) {
        N33_JXBEntry entry = jxbDao.getJXBById(id);
        if (entry.getStudentIds().size() > 0) {
            return "该教学班存在学生！";
        } else {
            jxbDao.removeN33_JXBEntry(id);
            return null;
        }
    }

    public String setClassListDanShuangZhou1(Map map) {
        List<String> cidList = (List<String>) map.get("cidList");
        String xqid = (String) map.get("xqid");
        String subid1 = (String) map.get("subid1");
        String subid2 = (String) map.get("subid2");
        //全部年级的班级
        Map<ObjectId, ClassEntry> gradeClass = classDao.getIsolateClassEntry(MongoUtils.convertToObjectIdList(cidList), new ObjectId(xqid));
        //全部班级教室
        Map<ObjectId, N33_ClassroomEntry> classRoom = classroomDao.getRoomEntryListByXqClassMap(new ObjectId(xqid), MongoUtils.convertToObjectIdList(cidList));
        List<ObjectId> subList = new ArrayList<ObjectId>();
        subList.add(new ObjectId(subid1));
        subList.add(new ObjectId(subid2));
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBsBySubIds(subList, new ObjectId(xqid));
        for (ClassEntry classEntry : gradeClass.values()) {
            //学生
            List<ObjectId> studentList = classEntry.getStudentList();
            N33_JXBEntry jxb1 = null;
            N33_JXBEntry jxb2 = null;
            if (studentList.size() > 0) {
                //第一学科教学班
                jxb1 = getJxbStudentIsClassStudentAndSubject(studentList, new ObjectId(subid1), jxbEntries);
                //第二学科教学班
                jxb2 = getJxbStudentIsClassStudentAndSubject(studentList, new ObjectId(subid2), jxbEntries);
            } else {
                String name = "(" + classEntry.getXh() + ")";
                for (N33_JXBEntry entry : jxbEntries) {
                    if (entry.getName().indexOf(name) != -1 && entry.getSubjectId().toString().equals(subid1)) {
                        jxb1 = entry;
                    }
                }
                for (N33_JXBEntry entry : jxbEntries) {
                    if (entry.getName().indexOf(name) != -1 && entry.getSubjectId().toString().equals(subid2)) {
                        jxb2 = entry;
                    }
                }
            }
            if (jxb1 != null && jxb2 != null) {
                if (jxb1.getDanOrShuang() == 0 && jxb2.getDanOrShuang() == 0) {
                } else {
                    return "该班级已经存在所选的某个学科的单双周班级，创建失败";
                }
            } else {
                return "该班级没有对应学科教学班";
            }
        }
        return "ok";
    }

    public String setClassListDanShuangZhou(Map map) {
        List<String> cidList = (List<String>) map.get("cidList");
        String xqid = (String) map.get("xqid");
        String subid1 = (String) map.get("subid1");
        String subid2 = (String) map.get("subid2");
        List<ObjectId> subid = new ArrayList<ObjectId>();
        subid.add(new ObjectId(subid1));
        subid.add(new ObjectId(subid2));
        //全部年级的班级
        Map<ObjectId, ClassEntry> gradeClass = classDao.getIsolateClassEntry(MongoUtils.convertToObjectIdList(cidList), new ObjectId(xqid));
        ClassEntry cl = null;
        for (ClassEntry classEntry : gradeClass.values()) {
            cl = classEntry;
        }
//        List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByXqid(new ObjectId(xqid), cl.getGradeId(), subid);
            //全部班级教室
            Map<ObjectId, N33_ClassroomEntry> classRoom = classroomDao.getRoomEntryListByXqClassMap(new ObjectId(xqid), MongoUtils.convertToObjectIdList(cidList));
            List<ObjectId> subList = new ArrayList<ObjectId>();
            subList.add(new ObjectId(subid1));
            subList.add(new ObjectId(subid2));
            List<N33_JXBEntry> jxbEntries = jxbDao.getJXBsBySubIds(subList, new ObjectId(xqid));
            for (ClassEntry classEntry : gradeClass.values()) {
                Map map1 = new HashMap();
                List<String> cs = new ArrayList<String>();
                cs.add(classEntry.getID().toString());
                map1.put("cidList", cs);
                map1.put("xqid", xqid.toString());
                map1.put("subid1", subid1.toString());
                map1.put("subid2", subid2.toString());
                if (setClassListDanShuangZhou1(map1).equals("ok")) {
                    //学生
                    List<ObjectId> studentList = classEntry.getStudentList();
                    N33_JXBEntry jxb1 = null;
                    N33_JXBEntry jxb2 = null;
                    if (studentList.size() > 0) {
                        //第一学科教学班
                        jxb1 = getJxbStudentIsClassStudentAndSubject(studentList, new ObjectId(subid1), jxbEntries);
                        //第二学科教学班
                        jxb2 = getJxbStudentIsClassStudentAndSubject(studentList, new ObjectId(subid2), jxbEntries);
                    } else {
                        String name = "(" + classEntry.getXh() + ")";
                        for (N33_JXBEntry entry : jxbEntries) {
                            if (entry.getName().indexOf(name) != -1 && entry.getSubjectId().toString().equals(subid1)) {
                                jxb1 = entry;
                            }
                        }
                        for (N33_JXBEntry entry : jxbEntries) {
                            if (entry.getName().indexOf(name) != -1 && entry.getSubjectId().toString().equals(subid2)) {
                                jxb2 = entry;
                            }
                        }
                    }
                    if (jxb1 != null && jxb2 != null) {
                        if(jxb1.getJXBKS()==jxb2.getJXBKS()) {
                            if (jxb1.getDanOrShuang() == 0 && jxb2.getDanOrShuang() == 0) {
                                jxb1.setDanOrShuang(1);
                                jxb1.setRelativeId(jxb2.getID());
                                jxb1.setType(6);
                                jxb2.setDanOrShuang(2);
                                jxb2.setRelativeId(jxb1.getID());
                                jxb2.setType(6);
                                N33_ClassroomEntry classroomEntry = classRoom.get(classEntry.getClassId());
                                //班级教室
                                if (classroomEntry != null) {
                                    jxb2.setClassroomId(classroomEntry.getRoomId());
                                    jxb1.setClassroomId(classroomEntry.getRoomId());
                                }
                                jxbDao.updateN33_JXB(jxb1);
                                jxbDao.updateN33_JXB(jxb2);
                            }
                        }else {
                            return "课时不一致无法创建";
                        }
                    }
                }
            }
            return "ok";
    }

    //满足单数周条件的
    public N33_JXBEntry getJxbStudentIsClassStudentAndSubject(List<ObjectId> clsStudent, ObjectId subject, List<N33_JXBEntry> jxbEntries) {
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            //学科对应
            if (jxbEntry.getSubjectId().toString().equals(subject.toString())) {
                List<ObjectId> reStudentList = jxbEntry.getStudentIds();
                //初始人数一致
                if (reStudentList.size() == clsStudent.size()) {
                    //移除行政班学生之后的人数
                    reStudentList.removeAll(clsStudent);
                    //人数为0代表复合能够创建单数周的要求，也就是和行政班的学生人数一致
                    if (reStudentList.size() == 0) {
                        return jxbEntry;
                    }
                }
            }
        }
        return null;
    }

    public N33_JXBEntry getJxbStudentIsClassStudentAndSubject(List<ObjectId> clsStudent, ObjectId subject, Map<ObjectId, N33_JXBEntry> jxbEntries) {
        for (N33_JXBEntry jxbEntry : jxbEntries.values()) {
            //学科对应
            if (jxbEntry.getSubjectId().toString().equals(subject.toString())) {
                List<ObjectId> reStudentList = jxbEntry.getStudentIds();
                //初始人数一致
                if (reStudentList.size() == clsStudent.size()) {
                    //移除行政班学生之后的人数
                    reStudentList.removeAll(clsStudent);
                    //人数为0代表复合能够创建单数周的要求，也就是和行政班的学生人数一致
                    if (reStudentList.size() == 0) {
                        return jxbEntry;
                    }
                }
            }
        }
        return null;
    }

    public N33_JXBEntry getJxbStudentIsClassStudentAndSubject(List<ObjectId> clsStudent, ObjectId subject, N33_JXBEntry jxbEntrie) {
        //学科对应
        if (jxbEntrie.getSubjectId().toString().equals(subject.toString())) {
            List<ObjectId> reStudentList = jxbEntrie.getStudentIds();
            //初始人数一致
            if (reStudentList.size() == clsStudent.size()) {
                //移除行政班学生之后的人数
                reStudentList.removeAll(clsStudent);
                //人数为0代表复合能够创建单数周的要求，也就是和行政班的学生人数一致
                if (reStudentList.size() == 0) {
                    return jxbEntrie;
                }
            }
        }
        return null;
    }

    public List<Map<String, Object>> getClassListDanShuangZhou(Map map) {
        List<String> cidList = (List<String>) map.get("cidList");
        String xqid = (String) map.get("xqid");
        List<String> subList = (List<String>) map.get("subList");
        String gid = (String) map.get("gid");
        Grade grade = gradeDao.findIsolateGradeEntryByGradeId(new ObjectId(gid), new ObjectId(xqid));
        //全部年级的班级
        Map<ObjectId, ClassEntry> gradeClass = classDao.getIsolateClassEntry(MongoUtils.convertToObjectIdList(cidList), new ObjectId(xqid));

        //全部班级教室
//        Map<ObjectId, N33_ClassroomEntry> classRoom = classroomDao.getRoomEntryListByXqClassMap(new ObjectId(xqid), MongoUtils.convertToObjectIdList(cidList));
        // jxb add 
        ObjectId ciId = getDefaultPaiKeTerm(grade.getSchoolId()).getPaikeci();
        int acClassType = turnOffService.getAcClassType(grade.getSchoolId(), new ObjectId(gid), ciId,1);
        // jxb add
        Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getDanShuangJXBMap(new ObjectId(xqid), MongoUtils.convertToObjectIdList(subList), acClassType);
        List<ObjectId> roomId = new ArrayList<ObjectId>();
        for (N33_JXBEntry jxbEntry : jxbEntries.values()) {
            roomId.add(jxbEntry.getClassroomId());
        }
        Map<ObjectId, N33_ClassroomEntry> classRoom = classroomDao.getRoomEntryListByXqRoomForMap(new ObjectId(xqid), roomId);
        Map<ObjectId, N33_JXBEntry> orJxb = jxbDao.getJXBMapByTermId(new ObjectId(xqid), acClassType);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //去重复
        List<String> jxbList = new ArrayList<String>();
        for (N33_JXBEntry jxbEntry : jxbEntries.values()) {
            //教学班学生
            List<ObjectId> studentList = jxbEntry.getStudentIds();
            ClassEntry entry = getClassEntryByJxbStudentList(gradeClass, studentList);
            if (studentList.size() == 0) {
                Integer size = jxbEntry.getName().indexOf("(");
                Integer size1 = jxbEntry.getName().indexOf(")");
                String name = jxbEntry.getName().substring(size, size1 + 1);
                for (ClassEntry entry1 : gradeClass.values()) {
                    String kname = "(" + entry1.getXh() + ")";
                    if (kname.indexOf(name) != -1) {
                        entry = entry1;
                    }
                }
            }
            if (entry != null) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                if (grade != null) {
                    map1.put("grade", grade.getName());
                } else {
                    map1.put("grade", "");
                }
                map1.put("xh", entry.getXh());
                map1.put("cid", entry.getClassId().toString());
                map1.put("cName", entry.getClassName().toString());
                String danWeek = null;
                String shuangWeek = null;
                String danId = null;
                String shuangId = null;
                if (jxbEntry.getDanOrShuang() == 1) {
                    danWeek = jxbEntry.getSubjectId().toString();
                    shuangWeek = orJxb.get(jxbEntry.getRelativeId()).getSubjectId().toHexString();
                    danId = jxbEntry.getID().toHexString();
                    shuangId = orJxb.get(jxbEntry.getRelativeId()).getID().toHexString();
                } else {
                    shuangWeek = jxbEntry.getSubjectId().toString();
                    danWeek = orJxb.get(jxbEntry.getRelativeId()).getSubjectId().toHexString();
                    shuangId = jxbEntry.getID().toHexString();
                    danId = orJxb.get(jxbEntry.getRelativeId()).getID().toHexString();
                }
                map1.put("dan", danWeek);
                map1.put("shuang", shuangWeek);
                map1.put("danId", danId);
                map1.put("shuangId", shuangId);
                //班级教室
//
                N33_ClassroomEntry classroomEntry = classRoom.get(jxbEntry.getClassroomId());
                if (classroomEntry != null) {
                    map1.put("room", classroomEntry.getRoomName());
                } else {
                    map1.put("room", "");
                }
                if (jxbEntry.getClassroomId() != null) {
                    map1.put("roomId", jxbEntry.getClassroomId().toString());
                } else {
                    map1.put("roomId", "");
                }
                if (!jxbList.contains(jxbEntry.getID().toString())) {
                    list.add(map1);
                    jxbList.add(jxbEntry.getID().toString());
                    jxbList.add(orJxb.get(jxbEntry.getRelativeId()).getID().toString());
                }
            }
        }
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (Integer) o1.get("xh") - (Integer) o2.get("xh");
            }
        });
        return list;
    }

    public ClassEntry getClassEntryByJxbStudentList(Map<ObjectId, ClassEntry> objectIdClassEntryMap, List<ObjectId> studentList) {
        for (ClassEntry classEntry : objectIdClassEntryMap.values()) {
            List<ObjectId> reStudentList = classEntry.getStudentList();
            //初始人数一致
            if (reStudentList.size() == studentList.size()) {
                //移除行政班学生之后的人数
                reStudentList.removeAll(studentList);
                //人数为0代表复合能够创建单数周的要求，也就是和行政班的学生人数一致
                if (reStudentList.size() == 0) {
                    return classEntry;
                }
            }
        }
        return null;
    }

    public String updateDanShuang(ObjectId xqid, ObjectId dan, ObjectId shuang, ObjectId cid) {
        ClassEntry entry = classDao.findIsolateClassEntryByCId(cid, xqid);
        List<ObjectId> subList = new ArrayList<ObjectId>();
        subList.add(dan);
        subList.add(shuang);
        // jxb add 
        ObjectId ciId = getDefaultPaiKeTerm(entry.getSchoolId()).getPaikeci();
        int acClassType = turnOffService.getAcClassType(entry.getSchoolId(),entry.getGradeId(),ciId,1);
        // jxb add
        Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMapByTermId(xqid,acClassType);
        //Map<ObjectId, N33_JXBEntry> orJxb = jxbDao.getJXBMapByTermId(xqid);
//        List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByXqid(xqid, entry.getGradeId(), subList);
            N33_JXBEntry jxb1 = getJxbStudentIsClassStudentAndSubject(entry.getStudentList(), dan, jxbMap);
            N33_JXBEntry jxb2 = getJxbStudentIsClassStudentAndSubject(entry.getStudentList(), shuang, jxbMap);
            if (jxb1 != null && jxb2 != null) {
                N33_JXBEntry entry1 = null;
                N33_JXBEntry entry2 = null;
                if (jxb1.getDanOrShuang() == 0 || jxb2.getDanOrShuang() == 0) {
                    if (jxb1.getDanOrShuang() != 0) {
                        entry1 = jxb1;
                        entry2 = jxb2;
                    } else {
                        entry1 = jxb2;
                        entry2 = jxb1;
                    }
                }
                if(entry1.getJXBKS()==entry2.getJXBKS()) {
                    if (entry1 == null) {
                        return "该班级已经存在所选的某个学科的单双周班级，创建失败";
                    } else {
                        //管理的教学班
                        N33_JXBEntry jxb = jxbMap.get(entry1.getRelativeId());
                        jxb.setRelativeId(null);
                        jxb.setDanOrShuang(0);
                        jxbDao.updateN33_JXB(jxb);
                        if (dan.toString().equals(entry1.getSubjectId().toString())) {
                            entry1.setDanOrShuang(1);
                        } else {
                            entry1.setDanOrShuang(2);
                        }
                        entry1.setRelativeId(entry2.getID());
                        jxbDao.updateN33_JXB(entry1);
                        if (dan.toString().equals(entry2.getSubjectId().toString())) {
                            entry2.setDanOrShuang(1);
                        } else {
                            entry2.setDanOrShuang(2);
                        }
                        entry2.setRelativeId(entry1.getID());
                        jxbDao.updateN33_JXB(entry2);
                    }
                }else {
                    return "课时不一致无法创建";
                }
            }
            return "ok";
    }

    public void removeDanShuang(ObjectId xqid, ObjectId dan, ObjectId shuang, ObjectId cid) {
        ClassEntry entry = classDao.findIsolateClassEntryByCId(cid, xqid);
        // jxb add
        ObjectId ciId = getDefaultPaiKeTerm(entry.getSchoolId()).getPaikeci();
        int acClassType = turnOffService.getAcClassType(entry.getSchoolId(),entry.getGradeId(),ciId,1);
         // jxb add
        Map<ObjectId, N33_JXBEntry> jxbEntries = jxbDao.getDanShuangJXBMap(xqid, acClassType);
        N33_JXBEntry jxb1 = getJxbStudentIsClassStudentAndSubject(entry.getStudentList(), dan, jxbEntries);
        N33_JXBEntry jxb2 = getJxbStudentIsClassStudentAndSubject(entry.getStudentList(), shuang, jxbEntries);
        if (jxb1 != null && jxb2 != null) {
            jxb1.setRelativeId(null);
            jxb1.setDanOrShuang(0);
            jxb1.setType(3);
            jxb2.setRelativeId(null);
            jxb2.setType(3);
            jxb2.setDanOrShuang(0);
            jxbDao.updateN33_JXB(jxb1);
            jxbDao.updateN33_JXB(jxb2);
        }
    }

    public void HuanDanShuang(ObjectId dan, ObjectId shuang) {
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(dan);
        N33_JXBEntry jxbEntry1 = jxbDao.getJXBById(shuang);
        List<ObjectId> jxbIds = new ArrayList<ObjectId>();
        jxbIds.add(jxbEntry.getID());
        jxbIds.add(jxbEntry1.getID());
        if (jxbEntry.getDanOrShuang() == 1) {
            jxbEntry.setDanOrShuang(2);
            jxbEntry1.setDanOrShuang(1);
        } else {
            jxbEntry.setDanOrShuang(1);
            jxbEntry1.setDanOrShuang(2);
        }
        jxbDao.updateN33_JXB(jxbEntry);
        jxbDao.updateN33_JXB(jxbEntry1);
        List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBEntrysByJXBIdsOrNJxbIds(jxbEntry.getTermId(),jxbIds,jxbEntry.getSchoolId());
        for (N33_YKBEntry ykbEntry : ykbEntryList) {
            if(ykbEntry.getType() == 6){
                String ykbIds = ykbEntry.getID().toString();
                paiKeService.undoYKB(ykbIds,jxbEntry.getSchoolId());
            }
        }
    }

    public Integer get跨头老师Num(ObjectId ciId, List<ObjectId> teaIds) {
        Integer num = 0;
        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(teaIds, ciId);
        Map<ObjectId, List<N33_JXBEntry>> tea_jxbs = new HashMap<ObjectId, List<N33_JXBEntry>>();
        for (N33_JXBEntry jxb : jxbEntryList) {
            List<N33_JXBEntry> jxbs = tea_jxbs.get(jxb.getTercherId()) == null ? new ArrayList<N33_JXBEntry>() : tea_jxbs.get(jxb.getTercherId());
            jxbs.add(jxb);
            tea_jxbs.put(jxb.getTercherId(), jxbs);
        }
        for (List<N33_JXBEntry> jxbs : tea_jxbs.values()) {
            Set<ObjectId> gradeIds = new HashSet<ObjectId>();
            for (N33_JXBEntry jxb : jxbs) {
                gradeIds.add(jxb.getGradeId());
            }
            if (gradeIds.size() > 1) {
                num++;
            }
        }
        return num;
    }

    public Set<String> getClassName(ObjectId sid, ObjectId termId, ObjectId roomId) {
        Set<String> set = new HashSet<String>();
        List<N33_JXBEntry> list = new ArrayList<N33_JXBEntry>();
        Map<ObjectId, Integer> gACTMap = turnOffService.getGradeAcClassTypeMap(sid);
        for( Map.Entry<ObjectId, Integer> gACT: gACTMap.entrySet()){
            ObjectId gradeId = gACT.getKey();
            int acClassType = gACT.getValue();
            List<N33_JXBEntry> childs = jxbDao.getJxbListByRoomId(gradeId, roomId, termId, acClassType);
            if(childs.size()>0){
             list.addAll(childs);
            }
        }
        HashSet<ObjectId> stuIds = new HashSet<ObjectId>();
        for (N33_JXBEntry jxb : list) {
            stuIds.addAll(jxb.getStudentIds());
        }
        Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidMap(termId, new ArrayList<ObjectId>(stuIds), new BasicDBObject("uid", 1)
                .append("cid", 1));
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(termId);
        for (ObjectId stuId : stuIds) {
            StudentEntry studentEntry = studentEntryMap.get(stuId);
            if (studentEntry != null && studentEntry.getClassId() != null) {
                ClassEntry classEntry = classEntryMap.get(studentEntry.getClassId());
                if (classEntry != null) {
                    set.add(classEntry.getName());
                }
            }
        }
        return set;
    }

    /**
     * 推荐课程
     *
     * @param gradeId
     * @param ciId
     * @param sid
     * @param cid
     */
    public Map<String, Object> getJxbByStudentSubject(String combiname, ObjectId gradeId, ObjectId ciId, ObjectId sid, ObjectId cid, Integer isFaBu) {
        StudentEntry studentEntry = null;
        //查询对应班级有没有和他一样学科的
        List<StudentEntry> studentEntryList = studentDao.getStuByCombiname(combiname, ciId, gradeId, cid);
        if (studentEntryList.size() > 0) {
            studentEntry = studentEntryList.get(0);
        } else {
            //对应班级没有的话就从年级随便找一个和她一样选课组合的
            studentEntryList = studentDao.getStuByCombiname(combiname, ciId, gradeId, null);
            if (studentEntryList.size() > 0) {
                studentEntry = studentEntryList.get(0);
            }
        }
        if (studentEntry != null) {
            //存在和他一样选课的人
            //查询教学班

        	//jxb add
        	int acClassType = turnOffService.getAcClassType(sid, gradeId, ciId,1);
        	//jxb add	
            List<N33_JXBEntry> jxbEntries = jxbDao.getZBJXBList(sid, gradeId, ciId,acClassType);
            //查询课时
            List<ObjectId> subList = new ArrayList<ObjectId>();
            //等级1
            ObjectId subD1 = studentEntry.getSubjectId1();
            //等级1
            ObjectId subD2 = studentEntry.getSubjectId2();
            //等级1
            ObjectId subD3 = studentEntry.getSubjectId3();
            subList.add(subD1);
            subList.add(subD2);
            subList.add(subD3);
            //课时
            List<N33_KSEntry> ksEntries = subjectDao.getIsolateSubjectEntryByZouBan(ciId, gradeId, 1, subList);
            if (ksEntries.size() == 0) {
                return null;
            }
            //合格
            ObjectId subH1 = ksEntries.get(0).getSubjectId();
            ObjectId subH2 = ksEntries.get(1).getSubjectId();
            ObjectId subH3 = ksEntries.get(2).getSubjectId();
            //查询合他一样的人的教学班
            ObjectId userId = studentEntry.getUserId();
            List<N33_JXBEntry> jxbEntries1 = jxbDao.getStuZBJXBList(gradeId, ciId, userId,acClassType);
            //返回结果
            Map<String, List<Map<String, Object>>> subListMap = new HashMap<String, List<Map<String, Object>>>();
            //学科封装
            subListMap.put(subD1.toString(), getListMap(subD1, jxbEntries, isFaBu, 1, jxbEntries1));
            subListMap.put(subD2.toString(), getListMap(subD2, jxbEntries, isFaBu, 1, jxbEntries1));
            subListMap.put(subD3.toString(), getListMap(subD3, jxbEntries, isFaBu, 1, jxbEntries1));
            subListMap.put(subH1.toString(), getListMap(subH1, jxbEntries, isFaBu, 2, jxbEntries1));
            subListMap.put(subH2.toString(), getListMap(subH2, jxbEntries, isFaBu, 2, jxbEntries1));
            subListMap.put(subH3.toString(), getListMap(subH3, jxbEntries, isFaBu, 2, jxbEntries1));
            //返回值
            Map<String, Object> mapList = new HashMap<String, Object>();
            List<N33_KSDTO> subjectList = new ArrayList<N33_KSDTO>();
            subjectList.add(new N33_KSDTO(subjectDao.findIsolateSubjectEntryById(ciId, sid, subD1, gradeId)));
            subjectList.add(new N33_KSDTO(subjectDao.findIsolateSubjectEntryById(ciId, sid, subD2, gradeId)));
            subjectList.add(new N33_KSDTO(subjectDao.findIsolateSubjectEntryById(ciId, sid, subD3, gradeId)));
            subjectList.add(new N33_KSDTO(subjectDao.findIsolateSubjectEntryById(ciId, sid, subH1, gradeId)));
            subjectList.add(new N33_KSDTO(subjectDao.findIsolateSubjectEntryById(ciId, sid, subH2, gradeId)));
            subjectList.add(new N33_KSDTO(subjectDao.findIsolateSubjectEntryById(ciId, sid, subH3, gradeId)));
            mapList.put("subject", subjectList);
            mapList.put("subjectJxbList", subListMap);
            return mapList;
        } else {
            return null;
        }
    }

    /**
     *
     * @param subId
     * @param jxbEntries
     * @param isFaBu
     * @param DengHe
     * @param userEntry
     * @return
     */
    public List<Map<String, Object>> getListMap(ObjectId subId, List<N33_JXBEntry> jxbEntries, Integer isFaBu, Integer DengHe, List<N33_JXBEntry> userEntry) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            if (jxbEntry.getSubjectId().toString().equals(subId.toString()) && jxbEntry.getType() == DengHe) {
                //封装DTO
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("jxbId", jxbEntry.getID().toString());
                map.put("jxbName", jxbEntry.getName());
                map.put("jxbCount", jxbEntry.getStudentIds().size());
                map.put("subId", jxbEntry.getSubjectId().toString());
                if (isFaBu == 0) {
                    //发布了
                    map.put("isFaBu", 0);
                } else {
                    map.put("isFaBu", 1);
                }
                map.put("DengHe", DengHe);
                map.put("Tui", 0);
                for (N33_JXBEntry entry : userEntry) {
                    if (entry.getID().toString().equals(jxbEntry.getID().toString())) {
                        map.put("Tui", 1);
                    }
                }
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 行政班
     *
     * @param classId
     * @param ciId
     * @param userId
     * @param gradeId
     * @return
     */
    public List<Map<String, Object>> addStudentByXingZheng(ObjectId classId, ObjectId ciId, ObjectId userId, ObjectId gradeId) {
        ClassEntry classEntry = classDao.findIsolateClassEntryByCId(classId, ciId);
        //学生
        List<ObjectId> studentIds = classEntry.getStudentList();
        //移除当前学生
        studentIds.remove(userId);
        //教学班
        //jxb add
        int acClassType = turnOffService.getAcClassType(classEntry.getSchoolId(), gradeId, ciId,1);
        //jxb add			
        List<N33_JXBEntry> jxbEntries = jxbDao.getStusXZJXBList(gradeId, ciId, studentIds,acClassType);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            List<ObjectId> stids = jxbEntry.getStudentIds();
            stids.removeAll(studentIds);
            if (stids.size() == 0) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("jxbId", jxbEntry.getID().toString());
                map.put("jxbName", jxbEntry.getName());
                map.put("jxbCount", jxbEntry.getStudentIds().size());
                map.put("subId", jxbEntry.getSubjectId().toString());
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 新增专项
     *
     * @param ciId
     * @param gradeId
     * @return
     */
    public Map<String, Object> addStudentZhuanXiangKe(ObjectId schoolId,ObjectId ciId, ObjectId gradeId, ObjectId classId) {
    	//jxb add
    	int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
    	//jxb add
        List<N33_JXBEntry> jxbEntries = jxbDao.getZXJXBList(gradeId, classId, ciId,acClassType);
        //专项课Id
        List<ObjectId> zxkIds = new ArrayList<ObjectId>();
        List<Map<String, Object>> zhuanXiang = new ArrayList<Map<String, Object>>();
        for (N33_JXBEntry entry : jxbEntries) {
            zxkIds.add(entry.getID());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("zxId", entry.getID().toString());
            map.put("name", entry.getName());
            zhuanXiang.add(map);
        }
        //专项
        List<N33_ZhuanXiangEntry> zhuanXiangEntries = zhuanXiangDao.findN33_ZhuanXiangEntry(zxkIds);
        Map<String, Object> list = new HashMap<String, Object>();
        for (ObjectId zxId : zxkIds) {
            List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
            for (N33_ZhuanXiangEntry zhuanXiangEntry : zhuanXiangEntries) {
                if (zxId.toString().equals(zhuanXiangEntry.getJxbId().toString())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("jxbId", zhuanXiangEntry.getID().toString());
                    map.put("zxId", zhuanXiangEntry.getJxbId().toString());
                    map.put("jxbName", zhuanXiangEntry.getName());
                    map.put("jxbCount", zhuanXiangEntry.getStudentId().size());
                    list1.add(map);
                }
            }
            list.put(zxId.toString(), list1);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", list);
        resultMap.put("zhuan", zhuanXiang);
        return resultMap;
    }

    public void addStudentByJxb(ObjectId userId, List<ObjectId> jxbIds) {
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBListByIds(jxbIds);
        for (N33_JXBEntry n33_jxbEntry : jxbEntries) {
            List<ObjectId> stuIds = n33_jxbEntry.getStudentIds();
            stuIds.add(userId);
            n33_jxbEntry.setStudentIds(stuIds);
            jxbDao.updateN33_JXB(n33_jxbEntry);
        }
        //专项课
        List<N33_ZhuanXiangEntry> entries = zhuanXiangDao.findN33_ZhuanXiangEntry(jxbIds);
        for (N33_ZhuanXiangEntry n33_jxbEntry : entries) {
            List<ObjectId> stuIds = n33_jxbEntry.getStudentId();
            stuIds.add(userId);
            n33_jxbEntry.setStudentId(stuIds);
            zhuanXiangDao.updateN33_ZhuanXiangEntry(n33_jxbEntry);
        }
    }

    public List<N33_JXBDTO> getTeaJxbList(ObjectId xqid, ObjectId teaId) {
        List<Map<String, Object>> lists = termService.getPaikeTimesByTermId(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        for (Map<String, Object> lt : lists) {
            String cid = (String) lt.get("ciId");
            cids.add(new ObjectId(cid));
        }
        List<N33_JXBEntry> jxbEntries = jxbDao.getJXBList(cids, teaId);
        List<N33_JXBDTO> jxbdtos = new ArrayList<N33_JXBDTO>();
        for (N33_JXBEntry jxbEntry : jxbEntries) {
            jxbdtos.add(new N33_JXBDTO(jxbEntry));
        }
        return jxbdtos;
    }

    public N33_JXBDTO getJxbEntry(ObjectId jxbId) {
        N33_JXBEntry entry = jxbDao.getJXBById(jxbId);
        return new N33_JXBDTO(entry);
    }

    /**
     * 导出模板
     *
     * @param response
     */
    public void addUserImp(HttpServletResponse response, String[] teaList, String[] roomList, String[] classList) throws JSONException {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("专项课导入模板");
        //班级
        CellRangeAddressList regions = new CellRangeAddressList(1, 1000, 2, 2);
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(classList);
        HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
        //教室
        regions = new CellRangeAddressList(1, 1000, 3, 3);
        constraint = DVConstraint.createExplicitListConstraint(roomList);
        data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
        //老师
        regions = new CellRangeAddressList(1, 1000, 4, 4);
        constraint = DVConstraint.createExplicitListConstraint(teaList);
        data_validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation);
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("班级");
        cell = row.createCell(3);
        cell.setCellValue("教室");
        cell = row.createCell(4);
        cell.setCellValue("上课老师");
        cell = row.createCell(5);
        cell.setCellValue("专项班级名");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("专项课导入模板.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public Map<String, Object> getStatusDC(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> status = (Map<String, Object>) session.getAttribute("dcjxb");
        if (status != null && status.get("st") != null && (Integer) status.get("st") == -1) {
            session.removeAttribute("dcjxb");
        }
        return status;
    }

    public Map<String, Object> getStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> status = (Map<String, Object>) session.getAttribute("zhuangxiangStatus");
        if (status != null && status.get("st") != null && (Integer) status.get("st") == -1) {
            session.removeAttribute("zhuangxiangStatus");
        }
        return status;
    }

    public void importUser(HttpServletResponse response, InputStream inputStream, ObjectId xqid, ObjectId sid, ObjectId zuHeId, ObjectId gradeId, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        Map<String, Object> status = new HashMap<String, Object>();
        status.put("st", 1);
        session.setAttribute("zhuangxiangStatus", status);
        N33_JXBEntry zHentry = jxbDao.getJXBById(zuHeId);
        //学科
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook(inputStream);
        HSSFSheet UserSheet = workbook.getSheet("专项课导入模板");
        int rowNum = UserSheet.getLastRowNum();
        //学号
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<String> zhuanName = new ArrayList<String>();
        List<String> teaName = new ArrayList<String>();
        List<String> roomName = new ArrayList<String>();
        for (int i = 1; i <= rowNum; i++) {
            Map<String, Object> zhuanList = new HashMap<String, Object>();
            HSSFCell cell = UserSheet.getRow(i).getCell(0);
            if (!getStringCellValue(cell).equals("")) {
                //根据学生取姓名
                String stuNo = getStringCellValue(cell);
                if (stuNo.indexOf(".") != -1) {
                    stuNo = stuNo.substring(0, stuNo.indexOf("."));
                }
                zhuanList.put("studentNo", stuNo);
            } else {
                zhuanList.put("studentNo", "");
            }
            cell = UserSheet.getRow(i).getCell(1);
            if (!getStringCellValue(cell).equals("")) {
                //根据学生取学号
                String stuNo = getStringCellValue(cell);
                zhuanList.put("studentName", stuNo);
            } else {
                zhuanList.put("studentName", "");
            }
            cell = UserSheet.getRow(i).getCell(2);
            if (!getStringCellValue(cell).equals("")) {
                Integer xh = new Double(getStringCellValue(cell)).intValue();
                zhuanList.put("xh", xh);
            }
            cell = UserSheet.getRow(i).getCell(3);
            if (!getStringCellValue(cell).equals("")) {
                if (getStringCellValue(cell).indexOf(".") != -1) {
                    String s = getStringCellValue(cell).substring(0, getStringCellValue(cell).indexOf("."));
                    zhuanList.put("roomName", s);
                } else {
                    zhuanList.put("roomName", getStringCellValue(cell));
                }
                if (!roomName.contains(getStringCellValue(cell))) {
                    if (getStringCellValue(cell).indexOf(".") != -1) {
                        String s = getStringCellValue(cell).substring(0, getStringCellValue(cell).indexOf("."));
                        if (!roomName.contains(s)) {
                            roomName.add(s);
                        }
                    } else {
                        roomName.add(getStringCellValue(cell));
                    }
                }
            }
            cell = UserSheet.getRow(i).getCell(4);
            if (!getStringCellValue(cell).equals("")) {
                zhuanList.put("teaName", getStringCellValue(cell));
                if (!teaName.contains(getStringCellValue(cell))) {
                    teaName.add(getStringCellValue(cell));
                }
            }
            cell = UserSheet.getRow(i).getCell(5);
            if (!getStringCellValue(cell).equals("")) {
                String stuNo = getStringCellValue(cell);
                if (stuNo.indexOf(".") != -1) {
                    stuNo = stuNo.substring(0, stuNo.indexOf("."));
                }
                zhuanList.put("zhuanName", stuNo);
                if (!zhuanName.contains(stuNo)) {
                    zhuanName.add(stuNo);
                }
            }
            result.add(zhuanList);
        }
        Integer count = 0;
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        List<Map<String, Object>> excelList = new ArrayList<Map<String, Object>>();
        List<N33_ZhuanXiangEntry> entries = new ArrayList<N33_ZhuanXiangEntry>();
        for (Integer a = 0; a < zhuanName.size(); a++) {
            N33_ZhuanXiangEntry entry = new N33_ZhuanXiangEntry(xqid, zhuanName.get(a), zuHeId, sid, new ObjectId(), new ObjectId(), new ArrayList<ObjectId>());
            N33_ClassroomEntry entry1 = classroomDao.getRoomEntryListByXqGrade(xqid, sid, gradeId, roomName.get(a));
            if (entry1 == null) {
                count++;
                continue;
            } else {
                entry.setRoomId(entry1.getRoomId());
            }
            N33_TeaEntry teaEntry = teaDao.findIsolateN33_TeaEntryById(xqid, zHentry.getSubjectId(), gradeId, teaName.get(a));
            if (teaEntry == null) {
                count++;
                continue;
            } else {
                entry.setTeaId(teaEntry.getUserId());
            }
            List<StudentEntry> studentEntryList = new ArrayList<StudentEntry>();
            for (Map<String, Object> map : result) {
                String zName = (String) map.get("zhuanName");
                if (zName.equals(zhuanName.get(a))) {
                    Integer xh = (Integer) map.get("xh");
                    List<ClassEntry> classEntries = classDao.findByGradeIdId(sid, gradeId, xqid, xh);
                    //List<StudentEntry> entry2 = studentDao.findStudentExact(xqid, gradeId, (String) map.get("studentName"), (String) map.get("studentNo"), classEntries.get(0).getClassId());
                    //if(entry2 != null && entry2.size() == 1){
                    List<StudentEntry> studentEntries = studentDao.findStudentExact(xqid, gradeId, (String) map.get("studentName"), (String) map.get("studentNo"), classEntries.get(0).getClassId());
                    //StudentEntry entry2 = studentDao.findStudent(xqid, gradeId, (String) map.get("studentName"), (String) map.get("studentNo"), classEntries.get(0).getClassId());
                    if (studentEntries != null && studentEntries.size() == 1) {
                        studentEntryList.add(studentEntries.get(0));
                    } else {
                        excelList.add(map);
                        count++;
                    }
                }
            }
            stuIds.addAll(MongoUtils.getFieldObjectIDs(studentEntryList, "uid"));
            entry.setStudentId(MongoUtils.getFieldObjectIDs(studentEntryList, "uid"));
            entries.add(entry);
        }
        if (count == 0) {
            if (entries.size() > 0) {
                zhuanXiangDao.add(entries);
                List<ObjectId> stuId = zHentry.getStudentIds();
                for (ObjectId userID : stuIds) {
                    if (!stuId.contains(userID)) {
                        stuId.add(userID);
                    }
                }
                zHentry.setStudentIds(stuId);
                jxbDao.updateN33_JXB(zHentry);
            }
            status.put("st", -1);
            status.put("result", "导入成功");
        } else {
            if (entries.size() > 0) {
                zhuanXiangDao.add(entries);
                List<ObjectId> stuId = zHentry.getStudentIds();
                for (ObjectId userID : stuIds) {
                    if (!stuId.contains(userID)) {
                        stuId.add(userID);
                    }
                }
                zHentry.setStudentIds(stuId);
                jxbDao.updateN33_JXB(zHentry);
            }
            HSSFWorkbook wb = new HSSFWorkbook();
            //生成一个sheet1
            HSSFSheet sheet = wb.createSheet("重复学生");
            //为sheet1生成第一行，用于放表头信息
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("学号");
            cell = row.createCell(1);
            cell.setCellValue("姓名");
            cell = row.createCell(2);
            cell.setCellValue("班级");
            cell = row.createCell(3);
            cell.setCellValue("教室");
            cell = row.createCell(4);
            cell.setCellValue("上课老师");
            cell = row.createCell(5);
            cell.setCellValue("专项班级名");

            for (int i = 0; i < excelList.size(); i++) {
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue((String) excelList.get(i).get("studentNo"));
                cell = row.createCell(1);
                cell.setCellValue((String) excelList.get(i).get("studentName"));
                cell = row.createCell(2);
                cell.setCellValue((Integer) excelList.get(i).get("xh"));
                cell = row.createCell(3);
                cell.setCellValue((String) excelList.get(i).get("roomName"));
                cell = row.createCell(4);
                cell.setCellValue((String) excelList.get(i).get("teaName"));
                cell = row.createCell(5);
                cell.setCellValue((String) excelList.get(i).get("zhuanName"));
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                wb.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            OutputStream outputStream = null;
            try {
                outputStream = response.getOutputStream();
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("重复学生.xls", "UTF-8"));
                response.setContentLength(content.length);
                outputStream.write(content);
            } catch (Exception e) {
                throw new IOException();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                        outputStream = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            status.put("st", -1);
            status.put("result", "导入数据中存在未知的数据，请查看重复学生表格");
        }
    }

    private String getStringCellValue(HSSFCell cell) {
        if (cell == null) return Constant.EMPTY;
        String strCell;

        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                strCell = Constant.EMPTY;
                break;
        }

        return StringUtils.isBlank(strCell) ? Constant.EMPTY : strCell;
    }

    public void updateEntry(N33_JXBEntry n33_jxbEntry) {
        jxbDao.updateN33_JXB(n33_jxbEntry);
    }

    /**
     *
     * @param studentIds
     * @param orgJxbId
     * @param newJxbId
     */
    public void udpStudentByJxb(String studentIds, ObjectId orgJxbId, ObjectId newJxbId) {
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(orgJxbId);
        N33_JXBEntry jxbEntry2 = jxbDao.getJXBById(newJxbId);
        List<ObjectId> StuIds = jxbEntry.getStudentIds();
        List<ObjectId> StuIds2 = jxbEntry2.getStudentIds();
        if (StringUtils.isNotEmpty(studentIds)) {
            String[] stuArg = studentIds.split(",");
            for (int i=0;i<stuArg.length;i++) {
                if (StringUtils.isNotEmpty(stuArg[i])) {
                    StuIds.remove(new ObjectId(stuArg[i]));
                    StuIds2.add(new ObjectId(stuArg[i]));
                }
            }
        }
        jxbEntry.setStudentIds(StuIds);
        jxbEntry2.setStudentIds(StuIds2);
        jxbDao.updateN33_JXB(jxbEntry);
        jxbDao.updateN33_JXB(jxbEntry2);

    }
}
