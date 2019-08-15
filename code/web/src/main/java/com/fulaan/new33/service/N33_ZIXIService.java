package com.fulaan.new33.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.bson.types.ObjectId;


import com.db.new33.CourseRangeDao;
import com.db.new33.N33_GradeWeekRangeDao;
import com.db.new33.isolate.N33_ClassDao;
import com.db.new33.isolate.N33_ClassroomDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.isolate.ZouBanTimeDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_SWDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZIXIKEDao;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.new33.CourseRangeEntry;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.isolate.ClassEntry;
import com.pojo.new33.isolate.N33_ClassroomEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.StudentEntry;
import com.pojo.new33.isolate.TermEntry;
import com.pojo.new33.isolate.ZouBanTimeEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.new33.paike.N33_ZIXIKEEntry;
import com.pojo.utils.MongoUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class N33_ZIXIService {

    private N33_ZIXIKEDao n33_zixikeDao = new N33_ZIXIKEDao();
    private N33_YKBDao n33_ykbDao = new N33_YKBDao();
    private N33_GradeWeekRangeDao n33_gradeWeekRangeDao = new N33_GradeWeekRangeDao();
    private CourseRangeDao courseRangeDao= new CourseRangeDao();
    private N33_StudentDao studentDao = new N33_StudentDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private N33_ClassDao n33_classDao = new N33_ClassDao();
    private N33_ClassroomDao n33_classroomDao = new N33_ClassroomDao();
    private N33_SWDao n33_swDao = new N33_SWDao();
    private TermDao termDao = new TermDao();
    private N33_TeaDao n33_teaDao = new N33_TeaDao();
    private ZouBanTimeDao timeDao = new ZouBanTimeDao();
    private N33_ClassDao classDao = new N33_ClassDao();
    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

    private N33_TurnOffService turnOffService = new N33_TurnOffService();

    public Map<String,Object> getTable(ObjectId ciId,ObjectId gradeId,ObjectId schoolId){
        Map<String,Object> result = new HashMap<String,Object>();
        List<StudentEntry> studentEntryList = studentDao.getStudentByXqidAndGradeId(gradeId, ciId);
        TermEntry termEntry = termDao.getTermByTimeId(ciId);// 事务学期
        List<ZouBanTimeEntry> zouBanTimeList = timeDao.getZouBanTimeList(schoolId,ciId,gradeId);
        List<N33_SWEntry> swEntryList = n33_swDao.getSwByXqid(termEntry.getID());
        Map<String,List<N33_SWEntry>> swEntryMap = new HashMap<String, List<N33_SWEntry>>();
        for(N33_SWEntry entry:swEntryList){
            if(entry.getStudentKe()==1){// 事务是否自习
                List<N33_SWEntry> list = swEntryMap.get(entry.getY()+"_"+entry.getX())==null?new ArrayList<N33_SWEntry>():swEntryMap.get(entry.getY()+"_"+entry.getX());
                list.add(entry);
                swEntryMap.put(entry.getY()+"_"+entry.getX(),list);
            }
        }
        List<ObjectId> allStudentList = new ArrayList<ObjectId>();
        for(StudentEntry studentEntry:studentEntryList){
            allStudentList.add(studentEntry.getUserId());
        }
        // jxb add 
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        // jxb add
        Map<ObjectId, N33_JXBEntry> jxbMap = jxbDao.getJXBMap(schoolId, gradeId, ciId,acClassType);
        Map<ObjectId, N33_ZIXIKEEntry> zixikeMap = n33_zixikeDao.getJXBsByXqidAndSid(ciId, schoolId, gradeId);
        List<N33_YKBEntry> ykbEntryList =  n33_ykbDao.getYKBEntrysList(ciId,schoolId,gradeId);

        Map<String,List<N33_YKBEntry>> xy_ykb = new HashMap<String, List<N33_YKBEntry>>();
        for(N33_YKBEntry ykbEntry:ykbEntryList){
            List<N33_YKBEntry> ykbEntries = xy_ykb.get(ykbEntry.getX()+"_"+ykbEntry.getY())!=null?xy_ykb.get(ykbEntry.getX()+"_"+ykbEntry.getY()):
                    new ArrayList<N33_YKBEntry>();
            ykbEntries.add(ykbEntry);
            xy_ykb.put(ykbEntry.getX()+"_"+ykbEntry.getY(),ykbEntries);
        }
        N33_GradeWeekRangeEntry gradeWeekRangeEntry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(ciId,schoolId,gradeId);
        List<CourseRangeEntry> courseRangeEntries = courseRangeDao.getEntryListBySchoolId(schoolId, ciId);
        List<List<Map<String,Object>>> content = new ArrayList<List<Map<String, Object>>>();
        for(CourseRangeEntry courseRange:courseRangeEntries){
            List<Map<String,Object>> row = new ArrayList<Map<String, Object>>();
            int y = courseRange.getSerial()-1;
            for(int i = gradeWeekRangeEntry.getStart();i<=gradeWeekRangeEntry.getEnd();i++){
                int x = i-1;
                List<N33_YKBEntry> ykbEntries = xy_ykb.get(x+"_"+y);
                    Map<String,Object> cell = countNotInClass(x,y,ykbEntries,allStudentList,jxbMap,zixikeMap,swEntryMap,zouBanTimeList);
                    row.add(cell);
            }
            content.add(row);
        }
        List<Map<String,Object>> htitle = new ArrayList<Map<String, Object>>();
        String[] weekArgs = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for(int i = gradeWeekRangeEntry.getStart();i<=gradeWeekRangeEntry.getEnd();i++){
            Map<String,Object> cell = new HashMap<String,Object>();
            cell.put("value",i-1);
            cell.put("text",weekArgs[i-1]);
            htitle.add(cell);
        }
        List<Map<String,Object>> vtitle = new ArrayList<Map<String, Object>>();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        for(CourseRangeEntry courseRange:courseRangeEntries){
            Map<String,Object> cell = new HashMap<String,Object>();
            cell.put("text",courseRange.getName());
            cell.put("time",sf.format(new Date(courseRange.getStart()))+"-"+sf.format(new Date(courseRange.getEnd())));
            cell.put("value",courseRange.getSerial()-1);
            vtitle.add(cell);
        }
        result.put("content",content);
        result.put("vtitle",vtitle);
        result.put("htitle",htitle);
        result.put("gradenum",studentEntryList.size());
        return result;
    }

    private Map<String,Object> countNotInClass(int x,int y, List<N33_YKBEntry> ykbEntryList,List<ObjectId> oldAll,
                                               Map<ObjectId, N33_JXBEntry> jxbMap,Map<ObjectId, N33_ZIXIKEEntry> zixikeMap,
                                               Map<String,List<N33_SWEntry>>  swEntryMap,List<ZouBanTimeEntry> zouBanTimeList){
        List<N33_SWEntry> swEntryList = swEntryMap.get((x+1)+"_"+(y+1));// 从一开始
        List<ObjectId> allStudentList = new ArrayList<ObjectId>(oldAll);
        Map<String,Object> result = new HashMap<String,Object>();
        List<ObjectId> inClassStu = new ArrayList<ObjectId>();
        List<ObjectId> inZixiStu = new ArrayList<ObjectId>();
        if(ykbEntryList!=null){
            List<N33_YKBEntry> 非自习班ykb = new ArrayList<N33_YKBEntry>();
            List<N33_YKBEntry> 自习班ykb = new ArrayList<N33_YKBEntry>();
            for(N33_YKBEntry ykb:ykbEntryList){
                if(ykb.getType()!=5){
                    非自习班ykb.add(ykb);
                }
                else{
                    自习班ykb.add(ykb);
                }
            }
            if(非自习班ykb!=null){
                for(N33_YKBEntry ykbEntry:非自习班ykb){
                    if(ykbEntry.getJxbId()==null){
                        continue;
                    }
                    N33_JXBEntry jxbEntry = jxbMap.get(ykbEntry.getJxbId());
                    if(null!=jxbEntry) {
                        inClassStu.addAll(jxbEntry.getStudentIds());
                    }

                }
            }
            if(zixikeMap!=null){
                for(N33_YKBEntry ykbEntry:自习班ykb){
                    N33_ZIXIKEEntry zixikeEntry = zixikeMap.get(ykbEntry.getJxbId());
                    if(zixikeEntry!=null&&zixikeEntry.getStudentIds()!=null){
                        inZixiStu.addAll(zixikeEntry.getStudentIds());
                    }
                }
            }
            allStudentList.removeAll(inClassStu);
            List<ObjectId> notInlist = allStudentList;
            result.put("notin",notInlist.size());
            result.put("notinlist",MongoUtils.convertToStringList(notInlist));
            result.put("inzixi",inZixiStu.size());
            result.put("inzixilist",MongoUtils.convertToStringList(inZixiStu));
            notInlist.removeAll(inZixiStu);
            result.put("kepai",MongoUtils.convertToStringList(notInlist));
        }
        else{
            if(swEntryList!=null){
                allStudentList.removeAll(inClassStu);
                List<ObjectId> notInlist = allStudentList;
                result.put("notin",notInlist.size());
                result.put("notinlist",MongoUtils.convertToStringList(notInlist));
                result.put("inzixi",0);
                result.put("inzixilist",new ArrayList<String>());
                result.put("kepai",new ArrayList<String>());
                result.put("zixi",1);// 全体自习
            }
            else{
                allStudentList.removeAll(inClassStu);
                List<ObjectId> notInlist = allStudentList;
                result.put("notin",notInlist.size());
                result.put("notinlist",MongoUtils.convertToStringList(notInlist));
                result.put("inzixi",0);
                result.put("inzixilist",new ArrayList<String>());
                result.put("kepai",new ArrayList<String>());
            }
        }
        for(ZouBanTimeEntry time:zouBanTimeList){
            if(time.getX()==x+1&&time.getY()==y+1&&time.getType()==0){
                result.put("ztime",1);
            }
        }
        result.put("x",x);
        result.put("y",y);
        return result;
    }

    public List<Map<String,Object>> getStudents(ObjectId schoolId,ObjectId gradeId,ObjectId ciId,List<String> studentIds,Integer x,Integer y){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        Map<ObjectId,ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(gradeId,ciId);
        Map<ObjectId,StudentEntry> studentEntryMap= studentDao.getStudentByXqidMap(ciId,MongoUtils.convertToObjectIdList(studentIds));
        List<Map<String,Object>> list = getZiXiBanByXY(schoolId,ciId,gradeId,x,y);
        for (String student:studentIds){
            StudentEntry entry1=  studentEntryMap.get(new ObjectId(student));
            String uid = entry1.getUserId().toString();
            String zixiname = "";
            for(Map<String,Object> map:list){
                List<String> stus = (List<String>) map.get("stus");
                if(stus.contains(uid)){
                    zixiname = (String) map.get("name");
                }
            }
            if(entry1!=null){
                Map<String,Object> map=new HashMap<String, Object>();
                map.put("xh",classEntryMap.get(entry1.getClassId()) == null ? "" : classEntryMap.get(entry1.getClassId()).getXh());
                map.put("name",entry1.getUserName());
                map.put("lev",entry1.getLevel()==0?"无":entry1.getLevel().toString());
                map.put("sn",entry1.getStudyNum());
                map.put("sex",entry1.getSex()==0?"男":"女");
                map.put("clsName",entry1.getGradeName() + "(" + entry1.getClassName() + ")");
                map.put("conn",entry1.getCombiname());
                map.put("lev",entry1.getLevel()==0?"无":entry1.getLevel().toString());
                map.put("id",entry1.getUserId().toString());
                map.put("zixiname",zixiname);
                result.add(map);
            }
        }
        return result;
    }

    public void createZiXiBan(ObjectId sid,String name,ObjectId ykbId,ObjectId roomId,ObjectId ciId,ObjectId gid,ObjectId tid){
        N33_ZIXIKEEntry entry = new N33_ZIXIKEEntry(name,roomId,tid,gid,sid,ciId,null);

        ObjectId id = n33_zixikeDao.addN33_ZIXIKEEntry(entry);
        N33_YKBEntry ykbEntry = n33_ykbDao.getN33_YKBById(ykbId);
        ykbEntry.setJxbId(id);
        ykbEntry.setIsUse(1);
        ykbEntry.setTeacherId(tid);
        ykbEntry.setClassroomId(roomId);
        ykbEntry.setSchoolId(sid);
        ykbEntry.setGradeId(gid);
        ykbEntry.setTermId(ciId);
        ykbEntry.setType(5);// 自习
        n33_ykbDao.updateN33_YKB(ykbEntry);
        // 创建一个自习教学班
//        N33_JXBEntry jxbEntry = new N33_JXBEntry(name,roomId,tid,null,sid,ciId,gid,5);
//        jxbEntry.setID(id);
//        jxbDao.addN33_JXBEntry(jxbEntry);
    }

    public void createZiXiBan(ObjectId sid,String name,ObjectId ykbId,ObjectId roomId,ObjectId ciId,ObjectId gid,ObjectId tid,List<ObjectId> stuIds){
        N33_ZIXIKEEntry entry = new N33_ZIXIKEEntry(name,roomId,tid,gid,sid,ciId,stuIds);

        ObjectId id = n33_zixikeDao.addN33_ZIXIKEEntry(entry);
        N33_YKBEntry ykbEntry = n33_ykbDao.getN33_YKBById(ykbId);
        ykbEntry.setJxbId(id);
        ykbEntry.setIsUse(1);
        ykbEntry.setTeacherId(tid);
        ykbEntry.setClassroomId(roomId);
        ykbEntry.setSchoolId(sid);
        ykbEntry.setGradeId(gid);
        ykbEntry.setTermId(ciId);
        ykbEntry.setType(5);// 自习
        n33_ykbDao.updateN33_YKB(ykbEntry);
        // 创建一个自习教学班
//        N33_JXBEntry jxbEntry = new N33_JXBEntry(name,roomId,tid,null,sid,ciId,gid,5);
//        jxbEntry.setID(id);
//        jxbDao.addN33_JXBEntry(jxbEntry);
    }

    public List<Map<String,Object>> getRoomByXY(ObjectId sid,ObjectId ciId,ObjectId gid,Integer x,Integer y){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        List<N33_YKBEntry> ykbEntryList = n33_ykbDao.getYKBbyXY(ciId,null,x,y,sid,0);
        Map<ObjectId,N33_YKBEntry> roomId_ykb = new HashMap<ObjectId, N33_YKBEntry>();
        List<ObjectId> roomIds = new ArrayList<ObjectId>();
        for(N33_YKBEntry ykb:ykbEntryList){
            roomId_ykb.put(ykb.getClassroomId(),ykb);
            roomIds.add(ykb.getClassroomId());
        }
        Map<ObjectId,N33_ClassroomEntry> roomId_map = n33_classroomDao.getRoomEntryListByXqRoomMap(ciId,roomIds,gid);// 只查本年级的教室
        for(N33_ClassroomEntry entry:roomId_map.values()){
            Map<String,Object> map = new HashMap<String,Object>();
            if(roomId_ykb.get(entry.getRoomId())!=null){
                map.put("ykbid",roomId_ykb.get(entry.getRoomId()).getID().toString());
                map.put("roomid",entry.getRoomId().toString());
                map.put("roomname",entry.getRoomName());
                result.add(map);
            }
        }
        return result;
    }

    public List<Map<String,Object>> getTeachersByXY(ObjectId sid,ObjectId ciId,ObjectId gid,Integer x,Integer y){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        Map<ObjectId,N33_TeaEntry> tid_map = n33_teaDao.getTeaMap(ciId,sid,gid);
        List<ObjectId> ids = new ArrayList<ObjectId>();
        for(N33_TeaEntry entry:tid_map.values()){
            ids.add(entry.getUserId());
        }
        TermEntry termEntry = termDao.getTermByTimeId(ciId);
        List<N33_SWEntry> swEntryList = n33_swDao.getSwByXY(termEntry.getID(),y+1,x+1);// x,y在存的时候反了
        List<ObjectId> swteaIds = new ArrayList<ObjectId>();
        for(N33_SWEntry entry:swEntryList){
            swteaIds.addAll(entry.getTeacherIds());
        }
        ids.removeAll(swteaIds);// 有事务的排除
        List<N33_YKBEntry> ykbEntryList = n33_ykbDao.getYKBbyXY(ciId,gid,x,y,sid,1);
        List<ObjectId> ykTeaIds = new ArrayList<ObjectId>();
        for(N33_YKBEntry entry:ykbEntryList){
            ykTeaIds.add(entry.getTeacherId());
        }
        ids.removeAll(ykTeaIds);// 有课的排除
        for(ObjectId id:ids){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",id.toString());
            map.put("name",tid_map.get(id).getUserName());
            result.add(map);
        }
        return result;
    }

    public List<Map<String,Object>> getZiXiBanByXY(ObjectId sid,ObjectId ciId,ObjectId gid,Integer x,Integer y){

        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        List<N33_YKBEntry> ykbEntryList = n33_ykbDao.getYKBbyXY(ciId,gid,x,y,sid,1,5);
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<ObjectId> roomIds = new ArrayList<ObjectId>();
        Map<ObjectId,N33_YKBEntry> zixiId_ykb = new HashMap<ObjectId, N33_YKBEntry>();
        for(N33_YKBEntry ykbEntry:ykbEntryList){
            ids.add(ykbEntry.getJxbId());
            zixiId_ykb.put(ykbEntry.getJxbId(),ykbEntry);
            roomIds.add(ykbEntry.getClassroomId());
        }
        String[] arr = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        List<N33_ZIXIKEEntry> list = n33_zixikeDao.getJXBByIds(ids,ciId);
        Map<ObjectId,N33_TeaEntry> tid_map = n33_teaDao.getTeaMap(ciId,sid,gid);
        Map<ObjectId,N33_ClassroomEntry> roomId_map = n33_classroomDao.getRoomEntryListByXqRoomMap(ciId,roomIds,gid);// 只查本年级的教室
        for(N33_ZIXIKEEntry entry:list){
            Map<String,Object> map = new HashMap<String,Object>();
            N33_YKBEntry ykb = zixiId_ykb.get(entry.getID());
            if(ykb!=null){
                map.put("x",ykb.getX());
                map.put("y",ykb.getY());
                map.put("time",arr[ykb.getX()]+" 第"+(ykb.getY()+1)+"节课");
            }
            else{
                map.put("x","");
                map.put("y","");
                map.put("time","");
            }
            map.put("id",entry.getID().toString());
            map.put("name",entry.getName());
            map.put("num",entry.getStudentIds()==null?0:entry.getStudentIds().size());
            map.put("roomname",roomId_map.get(entry.getClassroomId()).getRoomName());
            map.put("roomid",entry.getClassroomId().toString());
            if(entry.getTeacherId()!=null){
                map.put("teaname",tid_map.get(entry.getTeacherId()).getUserName());
            }
            else{
                map.put("teaname","");
            }
            map.put("teaid",entry.getTeacherId().toString());
            map.put("stus",MongoUtils.convertToStringList(entry.getStudentIds()));
            result.add(map);
        }
        return result;
    }

    public List<Map<String,Object>> getZiXiBan(ObjectId sid,ObjectId ciId,ObjectId gid) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<ObjectId, N33_ZIXIKEEntry> zixikeMap = n33_zixikeDao.getJXBsByXqidAndSid(ciId,sid,gid);
        List<ObjectId> zixkeids = new ArrayList<ObjectId>();
        List<ObjectId> roomIds = new ArrayList<ObjectId>();
        for(N33_ZIXIKEEntry zixikeEntry:zixikeMap.values()){
            zixkeids.add(zixikeEntry.getID());
            roomIds.add(zixikeEntry.getClassroomId());
        }
        List<N33_YKBEntry> ykbEntryList = n33_ykbDao.getYKBEntrysByJXBIds(ciId,zixkeids,sid);
        Map<ObjectId,N33_YKBEntry> zixiId_ykb = new HashMap<ObjectId, N33_YKBEntry>();
        for(N33_YKBEntry ykb:ykbEntryList){
            zixiId_ykb.put(ykb.getJxbId(),ykb);
        }
        List<CourseRangeEntry> courseRangeEntries = courseRangeDao.getEntryListBySchoolId(sid, ciId);
        Collections.sort(courseRangeEntries,new Comparator<CourseRangeEntry>(){
            public int compare(CourseRangeEntry arg0,CourseRangeEntry arg1) {
                return arg0.getSerial() - arg1.getSerial();
            }
        });
        Map<ObjectId,N33_TeaEntry> tid_map = n33_teaDao.getTeaMap(ciId,sid,gid);
        Map<ObjectId,N33_ClassroomEntry> roomId_map = n33_classroomDao.getRoomEntryListByXqRoomMap(ciId,roomIds,gid);// 只查本年级的教室
        String[] arr = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for(N33_ZIXIKEEntry entry:zixikeMap.values()){
            Map<String,Object> map = new HashMap<String,Object>();
            N33_YKBEntry ykb = zixiId_ykb.get(entry.getID());
            if(ykb!=null){
                map.put("x",ykb.getX());
                map.put("y",ykb.getY());
                map.put("time",arr[ykb.getX()]+" " + courseRangeEntries.get(ykb.getY()).getName());
            }
            else{
                map.put("x","");
                map.put("y","");
                map.put("time","");
            }
            map.put("id",entry.getID().toString());
            map.put("name",entry.getName());
            map.put("num",entry.getStudentIds()==null?0:entry.getStudentIds().size());
            map.put("roomname",roomId_map.get(entry.getClassroomId()).getRoomName());
            if(entry.getTeacherId()!=null){
                map.put("teaname",tid_map.get(entry.getTeacherId()).getUserName());
            }
            else{
                map.put("teaname","");
            }
            map.put("teaid",entry.getTeacherId()==null?"":entry.getTeacherId().toString());
            map.put("roomid",entry.getClassroomId().toString());
            map.put("stus",MongoUtils.convertToStringList(entry.getStudentIds()));
            result.add(map);
        }
        return result;
    }

    /**
     * 从原有自习班中删除，添加到新的自习班
     * @param ciId
     * @param sid
     * @param id
     * @param students
     */
    public void addStudentsToZiXi(ObjectId ciId,ObjectId sid,ObjectId id,ObjectId gid,List<String> students,Integer x,Integer y){

        // 从原有删除
        List<Map<String,Object>> list = getZiXiBanByXY(sid,ciId,gid,x,y);
        for (String uid:students) {
            String zixiname = "";
            for (Map<String, Object> map : list) {
                List<String> stus = (List<String>) map.get("stus");
                if (stus.contains(uid)) {
                    String zixiId = map.get("id").toString();
                    N33_ZIXIKEEntry zixikeEntry1 = n33_zixikeDao.getJXBById(new ObjectId(zixiId));
                    if(zixikeEntry1!=null){
                        List<ObjectId> studentObjIds = zixikeEntry1.getStudentIds();
                        studentObjIds.remove(new ObjectId(uid));
                        zixikeEntry1.setStudentIds(studentObjIds);
                        n33_zixikeDao.updateN33_JXB(zixikeEntry1);
                    }
                }
            }
        }

        // 添加到指定的
        N33_ZIXIKEEntry zixikeEntry = n33_zixikeDao.getJXBById(id);
        HashSet<ObjectId> ids = new HashSet<ObjectId>(zixikeEntry.getStudentIds());
        ids.addAll(MongoUtils.convertToObjectIdList(students));
        zixikeEntry.setStudentIds(new ArrayList<ObjectId>(ids));
        n33_zixikeDao.updateN33_JXB(zixikeEntry);
    }

    public void removeZiXiBan(ObjectId ciId,ObjectId sid,ObjectId id){

        n33_zixikeDao.removeN33_ZIXIKEEntry(id);

        List<ObjectId> jxbIds = Arrays.asList(id);
        List<N33_YKBEntry> ykbEntryList = n33_ykbDao.getYKBEntrysByJXBIds(ciId,jxbIds,sid);// 仅有一个
        N33_YKBEntry ykbEntry = ykbEntryList.get(0);
        N33_YKBEntry newEntry = new N33_YKBEntry(ykbEntry.getX(),ykbEntry.getY(),ykbEntry.getClassroomId(),ykbEntry.getSchoolId(),ykbEntry.getTermId());
        newEntry.setType(null);
        newEntry.setIsUse(0);
        newEntry.setID(ykbEntry.getID());
        n33_ykbDao.updateN33_YKB(newEntry);

//        jxbDao.removeN33_JXBEntry(id);// 删除对应的教学班
    }
    public void removeZiXiBanByGid(ObjectId ciId,ObjectId sid,ObjectId gid){
        List<N33_ZIXIKEEntry> zixikeEntryList = n33_zixikeDao.getJXBsBygid(ciId,sid,gid);
        for(N33_ZIXIKEEntry zixikeEntry:zixikeEntryList){
            ObjectId id = zixikeEntry.getID();
            n33_zixikeDao.removeN33_ZIXIKEEntry(id);

            List<ObjectId> jxbIds = Arrays.asList(id);
            List<N33_YKBEntry> ykbEntryList = n33_ykbDao.getYKBEntrysByJXBIds(ciId,jxbIds,sid);// 仅有一个
            if(ykbEntryList != null && ykbEntryList.size() > 0){
                N33_YKBEntry ykbEntry = ykbEntryList.get(0);
                N33_YKBEntry newEntry = new N33_YKBEntry(ykbEntry.getX(),ykbEntry.getY(),ykbEntry.getClassroomId(),ykbEntry.getSchoolId(),ykbEntry.getTermId());
                newEntry.setType(null);
                newEntry.setIsUse(0);
                newEntry.setID(ykbEntry.getID());
                n33_ykbDao.updateN33_YKB(newEntry);
            }
        }


//        jxbDao.removeN33_JXBEntry(id);// 删除对应的教学班
    }
    public void updateRoomTeacher(ObjectId ciId,ObjectId sid,ObjectId id,ObjectId roomId,ObjectId teaId){
        N33_ZIXIKEEntry zixikeEntry = n33_zixikeDao.getJXBById(id);
        zixikeEntry.setClassroomId(roomId);
        zixikeEntry.setTeacherId(teaId);
        n33_zixikeDao.updateN33_JXB(zixikeEntry);

        List<ObjectId> jxbIds = Arrays.asList(id);
        List<N33_YKBEntry> ykbEntryList = n33_ykbDao.getYKBEntrysByJXBIds(ciId,jxbIds,sid);// 仅有一个
        N33_YKBEntry ykbEntry = ykbEntryList.get(0);
        ykbEntry.setTeacherId(teaId);
        ykbEntry.setClassroomId(roomId);
        n33_ykbDao.updateN33_YKB(ykbEntry);
    }

    public void exportZiXiBan(ObjectId sid,ObjectId ciId,ObjectId gid,HttpServletResponse response){
        List<Map<String,Object>> list = getZiXiBan(sid,ciId,gid);
        Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidAndGradeIdMap(gid,ciId);
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(gid,ciId);
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("教学班学生");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell = row.createCell(1);
        cell.setCellValue("班级");
        cell = row.createCell(2);
        cell.setCellValue("自习班");
        cell = row.createCell(3);
        cell.setCellValue("上课时间");
        cell = row.createCell(4);
        cell.setCellValue("教师");
        cell = row.createCell(5);
        cell.setCellValue("教室");
        int count = 1;
        for(int i=0;i<list.size();i++){
            Map<String,Object> map = list.get(i);
            String roomName = map.get("roomname").toString();
            String zixiBan = map.get("name").toString();
            String time = map.get("time").toString();
            String teaname = map.get("teaname").toString();
            List<String> stus = (List<String>) map.get("stus");
            for(int j=0;j<stus.size();j++){
                String stuId = stus.get(j);
                StudentEntry studentEntry = studentEntryMap.get(new ObjectId(stuId));
                HSSFRow temprow = sheet.createRow(count);
                HSSFCell tempcell = temprow.createCell(0);
                tempcell.setCellValue(studentEntry.getUserName());
                tempcell = temprow.createCell(1);
                tempcell.setCellValue(classEntryMap.get(studentEntry.getClassId()).getClassName());
                tempcell = temprow.createCell(2);
                tempcell.setCellValue(zixiBan);
                tempcell = temprow.createCell(3);
                tempcell.setCellValue(time);
                tempcell = temprow.createCell(4);
                tempcell.setCellValue(teaname);
                tempcell = temprow.createCell(5);
                tempcell.setCellValue(roomName);
                count++;
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("自习班名单.xls", "UTF-8"));
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
    
    //导出教学班的自习课 oz 2019-4-29
    public void exportZiXiBanTwo(ObjectId sid,ObjectId ciId,ObjectId gid,HttpServletResponse response){
        List<Map<String,Object>> zixibanList = getZiXiBan(sid,ciId,gid);
        Map<ObjectId, StudentEntry> studentEntryMap = studentDao.getStudentByXqidAndGradeIdMap(gid,ciId);
        Map<ObjectId, ClassEntry> classEntryMap = n33_classDao.findClassEntryMap(gid,ciId);
        
        Map<String,String> zixiTime = new HashMap<String, String>();
        for(int i=0;i<zixibanList.size();i++){//获取自习班上课的时间
        	String zxbName = "";//自习班名称
        	String keTime = "";//自习班上课时间
        	Map<String,Object> map = zixibanList.get(i);
        	zxbName = map.get("name").toString();
        	if(zixiTime.containsKey(zxbName)){
        		keTime = map.get("time").toString() + ","+zixiTime.get(zxbName).toString();
        	}else{
        		keTime = map.get("time").toString();
        	}
        	zixiTime.put(zxbName, keTime);
        }
        
        List<String> zxbList = new ArrayList<String>();
        HSSFWorkbook wb = new HSSFWorkbook();
        for(int i=0;i<zixibanList.size();i++){//一个自习班生成一个sheet
        	
        	Map<String,Object> map = zixibanList.get(i);
        	String roomName = map.get("roomname").toString();
            String zixiBan = map.get("name").toString();
            String time = map.get("time").toString();
            String teaname = map.get("teaname").toString();
            if(zxbList.contains(zixiBan)){//每个自习班只生成一个sheet
            	continue;
            }else{
            	zxbList.add(zixiBan);
            }
	        //生成一个sheet1
	        HSSFSheet sheet = wb.createSheet(zixiBan + "班_" + teaname);
	        sheet.setColumnWidth(1, 100 * 34);
	        
	        //第一行
	        sheet.addMergedRegion(new Region(0, (short) 0, 1, (short) 4));
			HSSFRow row = sheet.createRow(0);
	        HSSFCell cell = row.createCell(0);
	        cell.setCellValue(zixiBan + "_" + teaname + "   教室:" + roomName);
	        
	        //第二行
	        sheet.addMergedRegion(new Region(2, (short) 0, 6, (short) 4));
			HSSFRow row1 = sheet.createRow(2);
	        HSSFCell cell1 = row1.createCell(0);
	        HSSFCellStyle style = wb.createCellStyle();
	        // 设置自动换行
	        style .setWrapText(true);
	        cell1.setCellStyle(style);
	        String zixiTimeStr = zixiTime.get(zixiBan).toString();
	        String[] strTime = zixiTimeStr.split(",");
	        String cellValue = "";
	        for(int s=0;s<strTime.length;s++){
	        	if(s == 0){
	        		cellValue = "课程时间:" + strTime[s] + "";
	        	}else{
	        		if(s%3==0){
	        			cellValue = cellValue  + "\r\n" + strTime[s];
		        	}else{
		        		cellValue = cellValue + " " + strTime[s];
		        	}
	        	}
            }
	        cell1.setCellValue(new HSSFRichTextString(cellValue));
	        
			
			
	        HSSFRow row2 = sheet.createRow(7);
	        HSSFCell cell2 = row2.createCell(0);
	        cell2.setCellValue("序号");
	        cell2 = row2.createCell(1);
	        cell2.setCellValue("学号");
	        cell2 = row2.createCell(2);
	        cell2.setCellValue("姓名");
	        cell2 = row2.createCell(3);
	        cell2.setCellValue("班级");
	        cell2 = row2.createCell(4);
	        cell2.setCellValue("备注");
	        int count = 8;
            
            List<String> stus = (List<String>) map.get("stus");
            for(int j=0;j<stus.size();j++){
                String stuId = stus.get(j);
                StudentEntry studentEntry = studentEntryMap.get(new ObjectId(stuId));
                HSSFRow temprow = sheet.createRow(count);
                HSSFCell tempcell = temprow.createCell(0);
                tempcell.setCellValue((j+1) + "");
                tempcell = temprow.createCell(1);
                tempcell.setCellValue(studentEntry.getStudyNum());
                tempcell = temprow.createCell(2);
                tempcell.setCellValue(studentEntry.getUserName());
                tempcell = temprow.createCell(3);
                tempcell.setCellValue(classEntryMap.get(studentEntry.getClassId()).getClassName());
                tempcell = temprow.createCell(4);
                tempcell.setCellValue("");
                count++;
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("自习班名单.xls", "UTF-8"));
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

    public List<Map<String,Object>> autoArranage(ObjectId ciId,ObjectId gradeId,ObjectId schoolId,Map<String,Object> table, int cap,int type){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();

        List<List<Map<String,Object>>> content = (List<List<Map<String, Object>>>) table.get("content");
        Map<String,String> stu_jxbstr = null;
        if(type==1){
            stu_jxbstr = getStudentSelectJXB(ciId,gradeId,schoolId);
        }
        else{
            stu_jxbstr = getStudentSelectXZB(ciId,gradeId,schoolId);
        }
        for(List<Map<String,Object>> row:content){
            for(Map<String,Object> cell:row){
                Integer iszixi = (Integer) cell.get("zixi");
                if(iszixi!=null&&iszixi==1){
                    continue;// 全体自习，不排
                }
                else{
                    ArrayList<String> kepai = (ArrayList<String>) cell.get("kepai");
                    Integer x = (Integer) cell.get("x");
                    Integer y = (Integer) cell.get("y");
                    Map<String,Object> map = null;
                    if(type==1){
                        map = autoArrangeByJXB(ciId,gradeId,schoolId,kepai,x,y,cap,stu_jxbstr);
                    }
                    else if(type==2){
                        map = autoArrangeByXZB(ciId,gradeId,schoolId,kepai,x,y,cap,stu_jxbstr);
                    }
                    if(map!=null){
                        result.add(map);
                    }
                }
            }
        }
        return result;
    }
    public Map<String,String> getStudentSelectJXB(ObjectId ciId, ObjectId gradeId, ObjectId schoolId){
        Map<String,String> stu_jxbstr = new HashMap<String, String>();
        //jxb add
        int acClassType = turnOffService.getAcClassType(schoolId, gradeId, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(schoolId, gradeId, ciId,acClassType);
        Map<String,TreeSet<String>> stu_jxbs = new HashMap<String, TreeSet<String>>();
        for(N33_JXBEntry jxbEntry:jxbEntryList){
            String jxbId = jxbEntry.getID().toString();
            for(ObjectId stuId:jxbEntry.getStudentIds()){
                TreeSet<String> list = stu_jxbs.get(stuId.toString())==null?new TreeSet<String>(new StrComparator()):stu_jxbs.get(stuId.toString());
                list.add(jxbId);
                stu_jxbs.put(stuId.toString(),list);
            }
        }
        Map<String,List<String>> jxbs_stus = new HashMap<String, List<String>>();
        for(Map.Entry<String,TreeSet<String>> entry:stu_jxbs.entrySet()){
            String sutId = entry.getKey();
            StringBuffer jxbstr = new StringBuffer();
            for(String jxb:entry.getValue()){
                jxbstr.append(jxb);
            }
            stu_jxbstr.put(sutId,jxbstr.toString());

        }
        return stu_jxbstr;
    }
    public Map<String,Object> autoArrangeByJXB(ObjectId ciId,ObjectId gradeId,ObjectId schoolId,
                                 ArrayList<String> kepai,Integer x,Integer y,Integer cap,Map<String,String> stu_jxbstr){
        Map<String,List<String>> jxb_stus = new HashMap<String, List<String>>();
        List<Map<String,Object>> rooms = getRoomByXY(schoolId,ciId,gradeId,x,y);
        List<Map<String,Object>> teachers = getTeachersByXY(schoolId,ciId,gradeId,x,y);
        for(String stu:kepai){
            String jxbStr = stu_jxbstr.get(stu);
            List<String> stus = jxb_stus.get(jxbStr)==null?new ArrayList<String>():jxb_stus.get(jxbStr);
            stus.add(stu);
            jxb_stus.put(jxbStr,stus);
        }

        List<List<String>> capSizeStus = new ArrayList<List<String>>();
        List<String> allStus = new ArrayList<String>();
        for(Map.Entry<String,List<String>> entry:jxb_stus.entrySet()){
            allStus.addAll(entry.getValue());
        }
        int divide =allStus.size()/cap + allStus.size()%cap>0?1:0;
        List<String> fenduan = null;
        for(int i=0;i<allStus.size();i++){

            if(i%cap==0){
                fenduan = new ArrayList<String>();
                capSizeStus.add(fenduan);
            }
            fenduan.add(allStus.get(i));
        }
        if(capSizeStus.size()>rooms.size()){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("reason","教室不够");
            map.put("time","周"+(x+1)+"第"+(y+1)+"节课");
            return map;// 教室不够
        }
        if(capSizeStus.size()>teachers.size()){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("reason","教师不够");
            map.put("time","周"+(x+1)+"第"+(y+1)+"节课");
            return map;// 老师不够
        }
        for(int i=0;i<capSizeStus.size();i++){
            Map<String,Object> room = rooms.get(i);
            String ykbId = (String) room.get("ykbid");
            String roomId = (String) room.get("roomid");
            String roomName = (String) room.get("roomname");
            String name = "周"+(x+1)+"第"+(y+1)+"节课 自习课"+i;
            String tid = teachers.get(i).get("id").toString();
            createZiXiBan(schoolId,name,new ObjectId(ykbId),new ObjectId(roomId),ciId,gradeId,new ObjectId(tid),MongoUtils.convertToObjectIdList(capSizeStus.get(i)));
        }
        return null;
    }
    public Map<String,String> getStudentSelectXZB(ObjectId ciId,ObjectId gradeId,ObjectId schoolId){
        Map<String,String> stu_jxbstr = new HashMap<String, String>();
        List<ClassEntry> classEntries = classDao.findClassEntryBySchoolIdAndGradeId(ciId, schoolId, gradeId);
        Map<String,TreeSet<String>> stu_jxbs = new HashMap<String, TreeSet<String>>();
        for(ClassEntry classEntry:classEntries){
            String jxbId = classEntry.getClassId().toString();
            for(ObjectId stuId:classEntry.getStudentList()){
                TreeSet<String> list = stu_jxbs.get(stuId.toString())==null?new TreeSet<String>(new StrComparator()):stu_jxbs.get(stuId.toString());
                list.add(jxbId);
                stu_jxbs.put(stuId.toString(),list);
            }
        }
        Map<String,List<String>> jxbs_stus = new HashMap<String, List<String>>();
        for(Map.Entry<String,TreeSet<String>> entry:stu_jxbs.entrySet()){
            String sutId = entry.getKey();
            StringBuffer jxbstr = new StringBuffer();
            for(String jxb:entry.getValue()){
                jxbstr.append(jxb);
            }
            stu_jxbstr.put(sutId,jxbstr.toString());

        }
        return stu_jxbstr;
    }
    public Map<String,Object> autoArrangeByXZB(ObjectId ciId,ObjectId gradeId,ObjectId schoolId,
                                 ArrayList<String> kepai,Integer x,Integer y,Integer cap,Map<String,String> stu_jxbstr){
        Map<String,List<String>> jxb_stus = new HashMap<String, List<String>>();
        List<Map<String,Object>> rooms = getRoomByXY(schoolId,ciId,gradeId,x,y);
        List<Map<String,Object>> teachers = getTeachersByXY(schoolId,ciId,gradeId,x,y);
        for(String stu:kepai){
            String jxbStr = stu_jxbstr.get(stu);
            List<String> stus = jxb_stus.get(jxbStr)==null?new ArrayList<String>():jxb_stus.get(jxbStr);
            stus.add(stu);
            jxb_stus.put(jxbStr,stus);
        }

        List<List<String>> capSizeStus = new ArrayList<List<String>>();
        List<String> allStus = new ArrayList<String>();
        for(Map.Entry<String,List<String>> entry:jxb_stus.entrySet()){
            allStus.addAll(entry.getValue());
        }
        int divide =allStus.size()/cap + allStus.size()%cap>0?1:0;
        List<String> fenduan = null;
        for(int i=0;i<allStus.size();i++){

            if(i%cap==0){
                fenduan = new ArrayList<String>();
                capSizeStus.add(fenduan);
            }
            fenduan.add(allStus.get(i));
        }
        if(capSizeStus.size()>rooms.size()){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("reason","教室不够");
            map.put("time","周"+(x+1)+"第"+(y+1)+"节课");
            return map;// 教室不够
        }
        if(capSizeStus.size()>teachers.size()){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("reason","教师不够");
            map.put("time","周"+(x+1)+"第"+(y+1)+"节课");
            return map;// 老师不够
        }
        for(int i=0;i<capSizeStus.size();i++){
            Map<String,Object> room = rooms.get(i);
            String ykbId = (String) room.get("ykbid");
            String roomId = (String) room.get("roomid");
            String roomName = (String) room.get("roomname");
            String name = "周"+(x+1)+"第"+(y+1)+"节课 自习课"+i;
            String tid = teachers.get(i).get("id").toString();
            createZiXiBan(schoolId,name,new ObjectId(ykbId),new ObjectId(roomId),ciId,gradeId,new ObjectId(tid),MongoUtils.convertToObjectIdList(capSizeStus.get(i)));
        }
        return null;
    }
    class StrComparator implements Comparator{
        public int compare(Object obj1, Object obj2) {
            String s1 = (String) obj1;
            String s2 = (String) obj2;
            if(s1.compareTo(s2) <= 0)
                return -1;
            return 1;
        }
    }
}
