package com.fulaan.new33.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.CourseRangeDao;
import com.db.new33.DayRangeDao;
import com.db.new33.isolate.N33_ClassroomDao;
import com.db.new33.isolate.N33_StudentDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.SubjectDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_SWDao;
import com.db.new33.paike.N33_SWLBDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.db.new33.paike.N33_YKBDao;
import com.db.new33.paike.N33_ZIXIKEDao;
import com.fulaan.new33.dto.isolate.N33_ClassroomDTO;
import com.fulaan.new33.service.isolate.N33_ClassroomService;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.new33.CourseRangeEntry;
import com.pojo.new33.DayRangeEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.TermEntry;
import com.pojo.new33.isolate.TermEntry.PaiKeTimes;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_SWEntry;
import com.pojo.new33.paike.N33_SWLBEntry;
import com.pojo.new33.paike.N33_TurnOff;
import com.pojo.new33.paike.N33_YKBEntry;
import com.pojo.new33.paike.N33_ZIXIKEEntry;

@Service
public class PaikeReportService {

    private SubjectDao subjectDao = new SubjectDao();

    private N33_JXBDao jxbDao = new N33_JXBDao();

    private N33_ZIXIKEDao zixikeDao = new N33_ZIXIKEDao();

    private N33_SWDao swDao = new N33_SWDao();

    private N33_SWLBDao swlbDao = new N33_SWLBDao();

    private TermDao termDao = new TermDao();

    private N33_TeaDao teaDao = new N33_TeaDao();

    private N33_YKBDao ykbDao = new N33_YKBDao();

    private N33_ClassroomDao classroomDao = new N33_ClassroomDao();

    private CourseRangeDao courseRangeDao = new CourseRangeDao();

    private DayRangeDao dayRangeDao = new DayRangeDao();

    private DecimalFormat df = new DecimalFormat("######0.00");

    private N33_StudentDao n33_studentDao = new N33_StudentDao();

    private N33_JXBService n33_jxbService = new N33_JXBService();

    private SchoolSelectLessonSetService schoolSelectLessonSetService = new SchoolSelectLessonSetService();

    private N33_ClassroomService n33_classroomService = new N33_ClassroomService();

    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

    @Autowired
    private PaiKeService paiKeService;
    
    @Autowired
    private N33_TurnOffService turnOffService;

    public Map<String,Object> getReport(ObjectId ciId,ObjectId sid,ObjectId gid){
        Map<String,Object> result = new HashMap<String,Object>();

        TermEntry termEntry = termDao.getTermByTimeId(ciId);
        String paikeciname ="";
        for(PaiKeTimes paiKeTime:termEntry.getPaiKeTimes()){
            if(paiKeTime.getID().toString().equals(ciId.toString())){
                paikeciname = termDao.getTermByTimeId(ciId).getXqName()+"第"+paiKeTime.getSerialNumber()+"次";
            }
        }
        List<Map<String,Object>> zhengti1 = zhengti1(ciId,sid,gid);
        Map<String,Object> zhengti2 = getZhengti2(ciId,sid,gid);
        Map<String,Object> zouban1 = zouban1(ciId,sid,gid);
        List<Map<String,Object>> zouban2 = zouban2(ciId,sid,gid);
        List<Map<String,Object>> zouban3 = zouban3(ciId,sid,gid);
        List<Map<String,Object>> shiwu = shiwu(ciId,sid,gid);
        Map<String,Object> kebiao1 = kebiao1(ciId,sid,gid);
        List<Map<String,Object>> selectresult = selectResult(ciId,sid,gid);
        List<Map<String,Object>> selectresult2 = selectResult2(ciId,sid,gid);
        List<Map<String,Object>> roomkebiao = roomKebiao(ciId,sid,gid);
        result.put("paikeciname",paikeciname);
        result.put("zhengti1",zhengti1);
        result.put("zhengti2",zhengti2);
        result.put("zouban1",zouban1);
        result.put("zouban2",zouban2);
        result.put("zouban3",zouban3);
        result.put("shiwu",shiwu);
        result.put("kebiao1",kebiao1);
        result.put("selectresult",selectresult);
        result.put("selectresult2",selectresult2);
        result.put("roomkebiao",roomkebiao);
        return result;
    }
    public List<Map<String,Object>> zhengti1(ObjectId ciId,ObjectId sid,ObjectId gid){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        Map<ObjectId,N33_KSEntry> subId_ks = subjectDao.findSubjectsByIdsMapSubId(ciId,gid,sid);
        //jxb add
        int acClassType = turnOffService.getAcClassType(sid, gid, ciId,1);
        //jxb add
        List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(sid, gid, ciId, acClassType);
        Map<String,List<N33_JXBEntry>> sub_jxbs = new HashMap<String, List<N33_JXBEntry>>();
        for(N33_JXBEntry jxbEntry:jxbEntryList){
            List<N33_JXBEntry> list = sub_jxbs.get(jxbEntry.getSubjectId().toString()+jxbEntry.getType())==null?new ArrayList<N33_JXBEntry>():sub_jxbs.get(jxbEntry.getSubjectId().toString()+jxbEntry.getType());
            list.add(jxbEntry);
            sub_jxbs.put(jxbEntry.getSubjectId().toString()+jxbEntry.getType(),list);
        }
        for(List<N33_JXBEntry> list:sub_jxbs.values()){
            N33_JXBEntry jxb = list.get(0);
            ObjectId subId = jxb.getSubjectId();
            Integer type = jxb.getType();
            Map<String,Object> map = new HashMap<String,Object>();
            if(subId_ks.get(subId)==null){
                continue;
            }
            map.put("subname",subId_ks.get(subId).getSubjectName());
            map.put("ksnum",jxb.getJXBKS());
            Integer ks=jxb.getJXBKS();
            if(type==1){
                map.put("type","等级考");
                map.put("ksnum",jxb.getJXBKS());
                ks = jxb.getJXBKS();
            }
            else if(type==2){
                map.put("type","合格考");
            }
            else if(type==3){
                map.put("type","行政型");
            }
            else if(type==4){
                map.put("type","专项型");
            }
            else if(type==6){
                map.put("type","单双周");
            }
            Integer stunum = 0;

            for(N33_JXBEntry jxbEntry:list){
                stunum+=jxbEntry.getStudentIds().size();
            }
            map.put("stunum",stunum);
            map.put("jxbnum",list.size());
            map.put("zongks",list.size()*ks);
            if(list.size()!=0){
                map.put("stunumjxbnum",df.format(stunum*1.0/list.size()));
            }
            else{
                map.put("stunumjxbnum","");
            }
            result.add(map);
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return o1.get("subname").toString().compareTo(o2.get("subname").toString());
            }
        });
        return result;
    }
    /**
     * 走班，非走班，自习班课时
     * @param ciId
     * @param sid
     * @param gid
     * @return
     */
    public Map<String,Object> getZhengti2(ObjectId ciId,ObjectId sid,ObjectId gid){
        Map<String,Object> result = new HashMap<String,Object>();
        // 走班
        Map<ObjectId,N33_KSEntry> subId_ks = subjectDao.findSubjectsByIdsMapSubId(ciId,gid,sid);
        List<N33_KSEntry> zoubanks = subjectDao.getIsolateSubjectEntryByZouBan(ciId,sid,gid,1);
        List<ObjectId> zoubanSubIds = new ArrayList<ObjectId>();
        for(N33_KSEntry ks:zoubanks){
            zoubanSubIds.add(ks.getSubjectId());
        }
      //jxb add
        int acClassType = turnOffService.getAcClassType(sid, gid, ciId,1);
        //jxb add			

        List<N33_JXBEntry> zoubanlist = jxbDao.getJXBList(sid, gid, zoubanSubIds, ciId,acClassType);
        result.put("zoubannum",zoubanlist.size());
        Integer zoubanhour = 0;
        for(N33_JXBEntry jxb:zoubanlist){
                zoubanhour+=jxb.getJXBKS();
        }
        result.put("zoubanhour",zoubanhour);
        // 非走班
        List<N33_KSEntry> feizoubanks = subjectDao.getIsolateSubjectEntryByZouBan(ciId,sid,gid,0);
        List<ObjectId> feizoubanSubIds = new ArrayList<ObjectId>();
        for(N33_KSEntry ks:feizoubanks){
            feizoubanSubIds.add(ks.getSubjectId());
        }
        List<N33_JXBEntry> feizoubanlist = jxbDao.getJXBList(sid, gid, feizoubanSubIds, ciId,acClassType);
        result.put("feizoubannum",feizoubanlist.size());
        Integer feizoubanhour = 0;
        for(N33_JXBEntry jxb:feizoubanlist){
            feizoubanhour+=jxb.getJXBKS();
        }
        result.put("feizoubanhour",feizoubanhour);

        // 自习
        List<N33_ZIXIKEEntry> zixikelist = zixikeDao.getJXBsByXqidAndGid(ciId,sid,gid);
        result.put("zixinum",zixikelist.size());
        result.put("zixihour",zixikelist.size());
        return result;
    }

    /**
     * 走班整体1
     * @param ciId
     * @param sid
     * @param gid
     * @return
     */
    public Map<String,Object> zouban1(ObjectId ciId,ObjectId sid,ObjectId gid){
        Map<String,Object> result = new HashMap<String,Object>();
        Map<ObjectId,N33_KSEntry> subId_ks = subjectDao.findSubjectsByIdsMapSubId(ciId,gid,sid);
        List<N33_KSEntry> zoubanks = subjectDao.getIsolateSubjectEntryByZouBan(ciId,sid,gid,1);
        List<ObjectId> zoubanSubIds = new ArrayList<ObjectId>();
        for(N33_KSEntry ks:zoubanks){
            zoubanSubIds.add(ks.getSubjectId());
        }
      //jxb add
        int acClassType = turnOffService.getAcClassType(sid, gid, ciId,1);
        //jxb add			

        List<N33_JXBEntry> zoubanlist = jxbDao.getJXBList(sid, gid, zoubanSubIds, ciId,acClassType);
        result.put("zoubannum",zoubanlist.size());
        HashSet<ObjectId> teacherIds = new HashSet<ObjectId>();
        for(N33_JXBEntry jxbEntry:zoubanlist){
            teacherIds.add(jxbEntry.getTercherId());
        }
        result.put("teanum",teacherIds.size());
        if(teacherIds.size()==0){
            result.put("ratio","");
        }
        else{
            result.put("ratio",df.format(zoubanlist.size()*1.0/teacherIds.size()));
        }
        Integer zoubanhour = 0;
        for(N33_JXBEntry jxb:zoubanlist){
            zoubanhour+=jxb.getJXBKS();
        }
        result.put("zoubanhour",zoubanhour);
        return result;
    }

    public List<Map<String,Object>> zouban2(ObjectId ciId,ObjectId sid,ObjectId gid){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        List<N33_KSEntry> zoubanks = subjectDao.getIsolateSubjectEntryByZouBan(ciId,sid,gid,1);

        int acClassType = 0;
        N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid, gid, ciId,1);
        if(null!=turnOff){
            acClassType = turnOff.getAcClass();
        }

        Integer allzoubannum = 0;
        Integer allteanum = 0;
        Integer allzoubanhour = 0;
        Integer allkuatou = 0;
        for(N33_KSEntry ksEntry:zoubanks){
            Map<String,Object> map = new HashMap<String,Object>();
            ObjectId subjectId = ksEntry.getSubjectId();
            List<N33_JXBEntry> jxbEntryList = jxbDao.getJXBList(sid, gid, subjectId, ciId, acClassType);
            map.put("subname",ksEntry.getSubjectName());
            map.put("zoubannum",jxbEntryList.size());

            HashSet<ObjectId> teacherIds = new HashSet<ObjectId>();
            Integer zoubanhour = 0;
            for(N33_JXBEntry jxbEntry:jxbEntryList){
                teacherIds.add(jxbEntry.getTercherId());
                zoubanhour+=jxbEntry.getJXBKS();
            }
            Integer kuatou = n33_jxbService.get跨头老师Num(ciId, new ArrayList<ObjectId>(teacherIds));
            map.put("kuatou",kuatou);
            map.put("teanum",teacherIds.size());
            if(teacherIds.size()!=0){
                map.put("zoubannumteanum",df.format(jxbEntryList.size()*1.0/teacherIds.size()));
            }
            else{
                map.put("zoubannumteanum","");
            }
            map.put("zoubanhour",zoubanhour);
            if(teacherIds.size()!=0){
                map.put("zoubanhourteanum",df.format(zoubanhour*1.0/teacherIds.size()));
            }
            else{
                map.put("zoubanhourteanum","");
            }
            result.add(map);

            allzoubannum+=jxbEntryList.size();
            allteanum+=teacherIds.size();
            allzoubanhour+=zoubanhour;
            allkuatou+=kuatou;
        }
        Map<String,Object> total = new HashMap<String,Object>();
        total.put("subname","总计");
        total.put("zoubannum",allzoubannum);
        total.put("teanum",allteanum);
        total.put("zoubanhour",allzoubanhour);
        total.put("kuatou",allkuatou);
        if(allteanum!=0){
            total.put("zoubannumteanum",df.format(allzoubannum*1.0/allteanum));
            total.put("zoubanhourteanum",df.format(allzoubanhour*1.0/allteanum));
        }
        else{
            total.put("zoubannumteanum","");
            total.put("zoubanhourteanum","");
        }
        result.add(total);
        return result;
    }

    public List<Map<String,Object>> zouban3(ObjectId ciId,ObjectId sid,ObjectId gid){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        Map<ObjectId,N33_KSEntry> subId_ks = subjectDao.findSubjectsByIdsMapSubId(ciId,gid,sid);
        List<N33_KSEntry> zoubanks = subjectDao.getIsolateSubjectEntryByZouBan(ciId,sid,gid,1);
        List<ObjectId> zoubanSubIds = new ArrayList<ObjectId>();
        for(N33_KSEntry ks:zoubanks){
            zoubanSubIds.add(ks.getSubjectId());
        }
      //jxb add
        int acClassType = turnOffService.getAcClassType(sid, gid, ciId,1);
        //jxb add			

        List<N33_JXBEntry> zoubanlist = jxbDao.getJXBList(sid, gid, zoubanSubIds, ciId,acClassType);
        Map<String,List<N33_JXBEntry>> sub_jxbs = new HashMap<String, List<N33_JXBEntry>>();
        for(N33_JXBEntry jxbEntry:zoubanlist){
            List<N33_JXBEntry> list = sub_jxbs.get(jxbEntry.getSubjectId().toString()+jxbEntry.getType())==null?new ArrayList<N33_JXBEntry>():sub_jxbs.get(jxbEntry.getSubjectId().toString()+jxbEntry.getType());
            list.add(jxbEntry);
            sub_jxbs.put(jxbEntry.getSubjectId().toString()+jxbEntry.getType(),list);
        }
        for(List<N33_JXBEntry> list:sub_jxbs.values()){
            N33_JXBEntry jxb = list.get(0);
            ObjectId subId = jxb.getSubjectId();
            Integer type = jxb.getType();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("subname",subId_ks.get(subId).getSubjectName());
            if(type==1){
                map.put("type","等级考");
            }
            else if(type==2){
                map.put("type","合格考");
            }
            else if(type==3){
                map.put("type","行政型");
            }
            else if(type==4){
                map.put("type","专项型");
            }
            else if(type==6){
                map.put("type","单双周");
            }
            Integer stunum = 0;
            Integer maxnum = 0;
            Integer minnum = 9999;
            for(N33_JXBEntry jxbEntry:list){
                stunum+=jxbEntry.getStudentIds().size();
                if(jxbEntry.getStudentIds().size()>maxnum){
                    maxnum = jxbEntry.getStudentIds().size();
                }
                if(jxbEntry.getStudentIds().size()<minnum){
                    minnum = jxbEntry.getStudentIds().size();
                }
            }
            map.put("stunum",stunum);
            map.put("maxnum",maxnum);
            map.put("minnum",minnum);
            if(list.size()!=0){
                map.put("avgnum",df.format(stunum*1.0/list.size()));
            }
            else{
                map.put("avgnum","");
            }
            result.add(map);
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return o1.get("subname").toString().compareTo(o2.get("subname").toString());
            }
        });
        return result;
    }

    public List<Map<String,Object>> shiwu(ObjectId ciId,ObjectId sid,ObjectId gid){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        Map<ObjectId,N33_KSEntry> subId_ks = subjectDao.findSubjectsByIdsMapSubId(ciId,gid,sid);
        List<N33_KSEntry> zoubanks = subjectDao.getIsolateSubjectEntryByZouBan(ciId,sid,gid,1);
        ObjectId xqId = termDao.getTermByTimeId(ciId).getID();
        List<N33_SWLBEntry> swlbList = swlbDao.getSwLbByXqid(xqId,sid);
        ObjectId personalSWId = null;
//        for(N33_SWLBEntry lb:swlbList){
//            if(lb.getDesc().equals("个人事务")){
//                personalSWId = lb.getID();
//            }
//        }
        List<N33_SWEntry> swEntryList = swDao.getSwByXqid(xqId);
        if(swEntryList.size()!=0){
            List<ObjectId> swteas = new ArrayList<ObjectId>();
            for(N33_SWEntry swEntry:swEntryList){
                swteas.addAll(swEntry.getTeacherIds());
            }
            for(N33_KSEntry ksEntry:zoubanks){
                Map<String,Object> map = new HashMap<String,Object>();
                List<N33_TeaEntry> teacherList = teaDao.findN33_TeaEntryBySchoolIdAndGradeIdOrSubjectId(ciId,sid,gid,ksEntry.getSubjectId(),"*");
                List<ObjectId> teacherIds = new ArrayList<ObjectId>();
                for(N33_TeaEntry teaEntry:teacherList){
                    teacherIds.add(teaEntry.getUserId());
                }
                Integer swnum = 0;
                for(N33_SWEntry swEntry:swEntryList){
                    if(swEntry.getTeacherIds().containsAll(teacherIds)){
                        swnum++;
                    }
                }
                map.put("swnum",swnum);
                teacherIds.retainAll(swteas);
                map.put("teanum",teacherIds.size());
                if(teacherIds.size()!=0){
                    map.put("swnumteanum",df.format(swnum*1.0/teacherIds.size()));
                }
                else{
                    map.put("swnumteanum","");
                }
                map.put("subname",ksEntry.getSubjectName());
                result.add(map);
            }
        }
        else{
            for(N33_KSEntry ksEntry:zoubanks){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("subname",ksEntry.getSubjectName());
                map.put("swnum",0);
                map.put("teanum",0);
                map.put("swnumteanum","");
                result.add(map);
            }
        }
        return result;
    }

    public Map<String,Object> kebiao1(ObjectId ciId,ObjectId sid,ObjectId gid){
        Map<String,Object> result = new HashMap<String,Object>();
        List<ObjectId> roomIds = new ArrayList<ObjectId>();
        List<N33_YKBEntry> ykbEntryList = ykbDao.getYKBsByclassRoomIds(ciId,sid,roomIds);
        List<N33_YKBEntry> usedYkbList = ykbDao.getSettledJXBList(ciId,gid,sid);
        ObjectId xqId = termDao.getTermByTimeId(ciId).getID();
        List<CourseRangeEntry> list = courseRangeDao.getEntryListBySchoolId(sid, ciId);
        DayRangeEntry dayRangeEntry = dayRangeDao.getEntryBySchoolId(sid);
        List<Integer> shangwuX = new ArrayList<Integer>();
        List<Integer> xiawuX = new ArrayList<Integer>();
        List<Integer> wanshangX = new ArrayList<Integer>();
        for(CourseRangeEntry entry:list) {
            if(dayRangeEntry!=null) {
                if(entry.getStart()>=dayRangeEntry.getStart1()&&entry.getEnd()<=dayRangeEntry.getEnd1()) {
                    shangwuX.add(entry.getSerial()-1);
                }
                else if(entry.getStart()>=dayRangeEntry.getStart2()&&entry.getEnd()<=dayRangeEntry.getEnd2()){
                    xiawuX.add(entry.getSerial()-1);
                }
                else if(entry.getStart()>=dayRangeEntry.getStart3()&&entry.getEnd()<=dayRangeEntry.getEnd3()){
                    wanshangX.add(entry.getSerial()-1);
                }
            }
        }
        Integer shangwuSum = 0;
        Integer xiawuSum = 0;
        Integer wanshangSum = 0;
        for(N33_YKBEntry ykbEntry:ykbEntryList){
            if(shangwuX.contains(ykbEntry.getX())){
                shangwuSum++;
            }
            if(xiawuX.contains(ykbEntry.getX())){
                xiawuSum++;
            }
            if(wanshangX.contains(ykbEntry.getX())){
                wanshangSum++;
            }
        }
        Integer shangwuUsed = 0;
        Integer xiawuUsed = 0;
        Integer wanshangUsed = 0;
        for(N33_YKBEntry ykbEntry:usedYkbList){
            if(shangwuX.contains(ykbEntry.getX())){
                shangwuUsed++;
            }
            if(xiawuX.contains(ykbEntry.getX())){
                xiawuUsed++;
            }
            if(wanshangX.contains(ykbEntry.getX())){
                wanshangUsed++;
            }
        }

        result.put("keyong",ykbEntryList.size()-usedYkbList.size());
        result.put("swkeyong",shangwuSum-shangwuUsed);
        result.put("xwkeyong",xiawuSum-xiawuUsed);
        result.put("wskeyong",wanshangSum-wanshangUsed);
        result.put("used",usedYkbList.size());
        result.put("swused",shangwuUsed);
        result.put("xwused",xiawuUsed);
        result.put("wsused",wanshangUsed);
        Map<String,Object> keshimap = getZhengti2(ciId,sid,gid);
        Integer zoubanhour = (Integer) keshimap.get("zoubanhour");
        Integer feizoubanhour = (Integer) keshimap.get("feizoubanhour");
        if(shangwuSum!=0){
            result.put("zoubanshangwu",df.format(zoubanhour*1.0/shangwuSum));
            result.put("feizoubanshangwu",df.format(feizoubanhour*1.0/shangwuSum));
        }
        else{
            result.put("zoubanshangwu","");
            result.put("feizoubanshangwu","");
        }
        if(xiawuSum!=0){
            result.put("zoubanxiawu",df.format(zoubanhour*1.0/xiawuSum));
            result.put("feizoubanxiawu",df.format(feizoubanhour*1.0/xiawuSum));
        }
        else{
            result.put("zoubanxiawu","");
            result.put("feizoubanxiawu","");
        }
        if(wanshangSum!=0){
            result.put("zoubanwanshang",df.format(zoubanhour*1.0/wanshangSum));
            result.put("feizoubanwanshang",df.format(feizoubanhour*1.0/wanshangSum));
        }
        else {
            result.put("zoubanwanshang","");
            result.put("feizoubanwanshang","");
        }
        return result;
    }

    public List<Map<String,Object>> selectResult(ObjectId ciId,ObjectId sid,ObjectId gid){
        Map<String,Object> selectResult = schoolSelectLessonSetService.getStuSelectResultByClass(ciId,sid,gid,3);
        List<Map<String,Object>> content = (List<Map<String, Object>>) selectResult.get("content");
        return content;
    }
    public List<Map<String,Object>> selectResult2(ObjectId ciId,ObjectId sid,ObjectId gid){
        Map<String,Object> selectResult = schoolSelectLessonSetService.getStuSelectResultByClass(ciId,sid,gid,1);
        List<Map<String,Object>> content = (List<Map<String, Object>>) selectResult.get("content");
        Integer gradeStudentNum = n33_studentDao.countStudentByXqidAndGradeId(gid,ciId);
        for(Map<String,Object> map:content){
            if(map.get("name").toString().contains("物")){
                map.put("subname","物理");
            }
            if(map.get("name").toString().contains("化")){
                map.put("subname","化学");
            }
            if(map.get("name").toString().contains("生")){
                map.put("subname","生物");
            }
            if(map.get("name").toString().contains("政")){
                map.put("subname","政治");
            }
            if(map.get("name").toString().contains("历")){
                map.put("subname","历史");
            }
            if(map.get("name").toString().contains("地")){
                map.put("subname","地理");
            }
            Integer dengjinum = (Integer)map.get("sum");
            map.put("hegenum",gradeStudentNum-dengjinum);
        }
        return content;
    }

    public List<Map<String,Object>> roomKebiao(ObjectId ciId,ObjectId sid,ObjectId gid){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        List<N33_ClassroomDTO> classList = n33_classroomService.getListByXqGrade(sid,ciId.toString(),gid.toString());
        for(N33_ClassroomDTO classroomDTO:classList){
            Map<String,Object> map = new HashMap<String,Object>();
            String name = classroomDTO.getRoomName();
            String classname = classroomDTO.getClassName();
            Integer arrangedTime = classroomDTO.getArrangedTime();
            String ratio = "";
            Map kebiao = paiKeService.getKeBiaoList(ciId,gid.toString(), classroomDTO.getRoomId(), sid);
            Integer allCnt = (Integer) kebiao.get("allCnt");
            if(allCnt!=0){
                ratio = df.format(arrangedTime*1.0/allCnt);
            }
            Set<String> source = n33_jxbService.getClassName(sid,ciId,new ObjectId(classroomDTO.getRoomId()));
            map.put("name",name);
            map.put("classname",classname);
            map.put("arrangedTime",arrangedTime);
            map.put("ratio",ratio);
            map.put("source",source);
            result.add(map);
        }
        return result;
    }
}
