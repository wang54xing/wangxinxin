package com.fulaan.new33.service.isolate;

import java.text.SimpleDateFormat;
import java.util.*;

import com.db.new33.N33_JXZDao;
import com.fulaan.new33.dto.isolate.GradeDTO;
import com.pojo.new33.isolate.N33_JXZEntry;
import com.pojo.utils.MongoUtils;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.db.new33.isolate.TermDao;
import com.fulaan.new33.dto.isolate.TermDTO;
import com.fulaan.utils.RESTAPI.bo.new33.isolate.isolateAPI;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.TermEntry;
import com.sys.utils.JsonUtil;

@Service
public class IsolateTermService extends BaseService{

	private TermDao dao = new TermDao();

    private com.db.new33.N33_JXZDao N33_JXZDao = new N33_JXZDao();

    private IsolateGradeService isolateGradeService = new IsolateGradeService();

    /**
     * 批量新增学期
     *
     * @param sid
     * @throws org.json.JSONException
     */
    public void addIsolateTermEntrys(ObjectId sid) throws JSONException {
        String result = isolateAPI.getIsolateTerm(sid.toString());
        JSONObject resultJson = new JSONObject(result);
        Map map1 = (Map) JsonUtil.JSONToObj(resultJson.toString(), Map.class);
        List<Map<String, Object>> dtoList1 = (List<Map<String, Object>>) map1.get("message");
        List<DBObject> entries = new ArrayList<DBObject>();
        for (Map<String, Object> dto : dtoList1) {
            TermDTO dto1 = new TermDTO(
                    (String) dto.get("tid"),
                    Integer.parseInt(dto.get("sy").toString().substring(0, 4)),
                    (String) dto.get("xqnm"),
                    (String) dto.get("sid"),
                    (Long) dto.get("st"),
                    (Long) dto.get("et"),
                    (Integer) dto.get("ir"),
                    (String) dto.get("id"),
                    (String) dto.get("sy"));
            TermEntry entry = dto1.getEntry();
            TermEntry.PaiKeTimes paiKeTime = new TermEntry.PaiKeTimes();
            paiKeTime.setID(new ObjectId());
            paiKeTime.setSerialNumber(1);// 设置第一次派克
            List<TermEntry.PaiKeTimes> paiKeTimesList = new ArrayList<TermEntry.PaiKeTimes>();
            paiKeTimesList.add(paiKeTime);
            entry.setPaiKeTimes(paiKeTimesList);
            entry.setID(new ObjectId());
            entries.add(entry.getBaseEntry());
        }
        dao.addIsolateTermEntrys(entries);
        List<TermEntry> waitForJXZ = dao.getIsolateTermEntrysBySid(sid);
        for(TermEntry term:waitForJXZ){
            generateJXZ(sid,term.getID().toString());// 生成教学周
        }
    }


    public int getCount(ObjectId sid) {
        return dao.count(sid);
    }


    /**
     * 返回当前学校的所有学期的所有次
     *
     * @return
     */
    public List<Map<String,Object>> getAllTermList(ObjectId sid) {
        List<TermEntry> entries = dao.getIsolateTermListByYear(sid);
        List<Map<String,Object>> retList = new ArrayList<Map<String, Object>>();

        for (TermEntry termEntry : entries) {
            for (TermEntry.PaiKeTimes paiKeTimes : termEntry.getPaiKeTimes()) {
                if(paiKeTimes.getIr() == 0){
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("ciId",paiKeTimes.getID().toString());
                    map.put("ciName",termEntry.getXqName() + "第" + paiKeTimes.getSerialNumber() + "次");
                    retList.add(map);
                }
            }
        }
        return retList;
    }

    /**
     * 返回小于当前时间的学期
     *
     * @return
     */
    public List<TermDTO> getTermList(ObjectId sid) {
        //当前时间
        Long time = System.currentTimeMillis();
        //返回时间小于当前时间的学期
        getDefauleTermId(sid);
        List<TermEntry> entries = dao.getIsolateTermListByTime(time, sid);
        List<TermDTO> dtos = new ArrayList<TermDTO>();
        for (TermEntry entry : entries) {
            dtos.add(new TermDTO(entry));
        }
        return dtos;
    }
    public List<Map<String,Object>> getYearList(ObjectId sid) {
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        Map<Integer,List<TermEntry>> map = new HashMap<Integer, List<TermEntry>>();
        //当前时间
        Long time = System.currentTimeMillis();
        //返回时间小于当前时间的学期
        List<TermEntry> entries = dao.getIsolateTermListByTime(time, sid);
        for (TermEntry entry : entries) {
            List<TermEntry> list = map.get(entry.getYear())==null?new ArrayList<TermEntry>(): map.get(entry.getYear());
            list.add(entry);
            map.put(entry.getYear(),list);
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        for(List<TermEntry> entries1:map.values()){
            Map<String,Object> temp = new HashMap<String,Object>();
            temp.put("year",entries1.get(0).getYear());
            temp.put("sy",entries1.get(0).getSy());
            Boolean flag =false;
            for(TermEntry e:entries1){
                if(e.getIr()==0){
                    temp.put("term1",sf.format(new Date(e.getStartTime()))+"至"+sf.format(new Date(e.getEndTime())));
                }
                else {
                    temp.put("term2",sf.format(new Date(e.getStartTime()))+"至"+sf.format(new Date(e.getEndTime())));
                }
                Boolean flag1 = getXqIdIsFaBu(e.getID());
                if(flag||flag1==true){
                    flag = true;

                }
            }
            if(flag){
                temp.put("isfabu",1);
            }
            result.add(temp);

        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (Integer)o2.get("year")-(Integer)o1.get("year");
            }
        });
        return result;
    }
    public List<Map<String,Object>> getTermPaikeTimes(ObjectId sid) {
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        //当前时间
        Long time = System.currentTimeMillis();
        //返回时间小于当前时间的学期
        getDefauleTermId(sid);
        List<TermEntry> entries = dao.getIsolateTermListByTime(time, sid);
        List<TermDTO> dtos = new ArrayList<TermDTO>();
        for (TermEntry entry : entries) {
            dtos.add(new TermDTO(entry));
        }
        for(TermDTO dto:dtos){
            String termId = dto.getId();
            String termName = dto.getXqnm();
            Integer term = dto.getIr();
            for(TermDTO.PaiKeTimeDTO timeDTO:dto.getTimeDTOS()){
                if(timeDTO.getIr()==0){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("termId",termId);
                    map.put("term",term);
                    map.put("ciname",termName+"第"+timeDTO.getSerialNumber()+"次");
                    map.put("ciId",timeDTO.getId());
                    map.put("gradeIds", timeDTO.getGradeIds());
                    map.put("serial",timeDTO.getSerialNumber());
                    result.add(map);
                }
            }
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return new ObjectId(o2.get("ciId").toString()).getTimestamp()-new ObjectId(o1.get("ciId").toString()).getTimestamp();
            }
        });
        return result;
    }
    public TermEntry getTermEntry(ObjectId xqid) {
        return dao.findIsolateTermEntryEntryById(xqid);
    }

    /**
     * 根据学期获取次
     * @param termId
     * @return
     */
    public List<Map<String,Object>> getPaikeTimesByTermId(ObjectId termId){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        TermEntry termEntry = dao.findIsolateTermEntryEntryById(termId);
        List<TermEntry.PaiKeTimes> paiKeTimesList = termEntry.getPaiKeTimes();
        Collections.sort(paiKeTimesList, new Comparator<TermEntry.PaiKeTimes>() {
            @Override
            public int compare(TermEntry.PaiKeTimes o1, TermEntry.PaiKeTimes o2) {
                return o1.getSerialNumber()-o2.getSerialNumber();
            }
        });
        for(TermEntry.PaiKeTimes time:paiKeTimesList){
            if(time.getIr()==0){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("termId",termEntry.getID().toString());
                map.put("ciId",time.getID().toString());
                map.put("gradeIds", MongoUtils.convertToStringList(time.getGradeIds()));
                map.put("serial",time.getSerialNumber());
                map.put("desc",time.getDescription()==null?"":time.getDescription());
                map.put("date",sf.format(time.getID().getDate()));
                result.add(map);
            }

        }
        return result;
    }
    public List<Map<String,Object>> getPaikeTimesByTermId(ObjectId termId, ObjectId sid){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        TermEntry termEntry = dao.findIsolateTermEntryEntryById(termId);
        Map<String, Map<String, GradeDTO>> ciGradeMMap = isolateGradeService.getCiGradeMMap(sid);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        List<TermEntry.PaiKeTimes> paiKeTimesList = termEntry.getPaiKeTimes();
        Collections.sort(paiKeTimesList, new Comparator<TermEntry.PaiKeTimes>() {
            @Override
            public int compare(TermEntry.PaiKeTimes o1, TermEntry.PaiKeTimes o2) {
                return o1.getSerialNumber()-o2.getSerialNumber();
            }
        });
        for(TermEntry.PaiKeTimes time:paiKeTimesList){
            if(time.getIr()==0){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("termId",termEntry.getID().toString());
                String ciId = time.getID().toString();
                map.put("ciId", ciId);
                map.put("isfabu",getCiIdIsFaBu(time.getID()));
                map.put("gradeIds", MongoUtils.convertToStringList(time.getGradeIds()));
                String gradeStr = "";
                Map<String, GradeDTO> gradeMap = ciGradeMMap.get(ciId);
                for(ObjectId gid:time.getGradeIds()){
                    if(null!=gradeMap){
                        GradeDTO gDto = gradeMap.get(gid.toString());
                        if(null!=gDto){
                            gradeStr+= gDto.getGnm()+"("+gDto.getJie()+")<br/>";
                        }
                    }
                }
                map.put("gradeStr",gradeStr);
                map.put("serial",time.getSerialNumber());
                map.put("desc",time.getDescription()==null?"":time.getDescription());
                map.put("date",sf.format(time.getID().getDate()));
                result.add(map);
            }

        }
        return result;
    }
    public void removePaiKeTime(ObjectId termId,ObjectId timeId){
        dao.removePaiKeTime(termId,timeId);
    }

    public void addPaikeTime(ObjectId schoolId, ObjectId termId, TermDTO.PaiKeTimeDTO dto){
        TermEntry.PaiKeTimes paiKeTime = dto.buildEntry();
        dao.addPaikeTime(termId, paiKeTime);
        TermEntry termEntry = dao.findIsolateTermEntryEntryById(termId);
        long startTime = termEntry.getStartTime();
        long endTime = termEntry.getEndTime();
        long curTIme = new Date().getTime();
        if(startTime<curTIme&&curTIme<endTime){
            isolateGradeService.editGradeInfo(schoolId, paiKeTime.getID());
        }
    }

    public void updatePaikeTime(ObjectId termId,ObjectId timeId,TermDTO.PaiKeTimeDTO dto){
        dao.updatePaikeTime(termId,timeId, dto.buildEntry());
    }
    
    public List<TermDTO> getTermByYear(ObjectId schoolId,Integer year) {
    	List<TermDTO> dtos = new ArrayList<TermDTO>();
    	List<TermEntry> list =  dao.getTermByYear(schoolId, year);
    	for(TermEntry entry:list) {
    		dtos.add(new TermDTO(entry));
    	}
    	return dtos;
    }

    public void addTerm(TermDTO dto) {
    	ObjectId xqid = dao.addIsolateTermEntry(dto.getEntry());
        generateJXZ(new ObjectId(dto.getSid()),xqid.toString());// 生成教学周
    }

    public TermDTO getCurrentTerm(ObjectId schoolId) {
    	TermEntry entry = dao.getTermByTime(schoolId, new Date().getTime());
    	if(entry!=null) {
    		return new TermDTO(entry);
    	}
    	return null;
    }

    public void generateJXZ(ObjectId schoolId,String xqid) {
        if(N33_JXZDao.getListByXq(schoolId, new ObjectId(xqid)).size()!=0) {
            N33_JXZDao.removeEntryList(schoolId, new ObjectId(xqid));
        }
        TermEntry termEntry = dao.findIsolateTermEntryEntryById(new ObjectId(xqid));
        Date termStart = new Date(termEntry.getStartTime());
        Date termEnd = new Date(termEntry.getEndTime());

        int week_range = 0;
        long tempstart;
        long tempend;
        if(DateTimeUtils.somedayIsWeekDay(termStart)!=1) {
            week_range+=1;
            tempstart = getLastDayOfWeek(termStart.getTime()).getTime();
        }
        else {
            tempstart = termStart.getTime();
        }
        if(DateTimeUtils.somedayIsWeekDay(termEnd)!=0) {
            week_range+=1;
            tempend = getFirstDayOfWeek(termEnd.getTime()).getTime();
        }
        else {
            tempend = termEnd.getTime()+1000*60*60*24;
        }
        week_range += (int)((tempend-tempstart)/(1000*60*60*24*7));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(termStart);

        List<N33_JXZEntry> entryList = new ArrayList<N33_JXZEntry>();
        for(int i=1;i<=week_range;i++) {
            calendar.add(Calendar.DATE, i==1?0:7);
            Date date = calendar.getTime();// 一周中的某天

            N33_JXZEntry entry = new N33_JXZEntry();
            entry.setSchoolId(schoolId);
            entry.setXQId(new ObjectId(xqid));
            entry.setSerial(i);
            if(i==1) {
                entry.setStart(termEntry.getStartTime());
                entry.setEnd(getLastDayOfWeek(date.getTime()).getTime()+1000*60*60*24-1);
            }
            else if(i==week_range) {
                entry.setStart(getFirstDayOfWeek(date.getTime()).getTime());
                entry.setEnd(termEntry.getEndTime()+1000*60*60*24-1);
            }
            else {
                entry.setStart(getFirstDayOfWeek(date.getTime()).getTime());
                entry.setEnd(getLastDayOfWeek(date.getTime()).getTime()+1000*60*60*24-1);
            }
            entryList.add(entry);
        }
        N33_JXZDao.add(entryList);

    }

    /**
     * 取得指定日期所在周的第一天
     */
    private  Date getFirstDayOfWeek(long date) {
        try {
            java.util.Date time = new Date(date);
            Calendar c = new GregorianCalendar();
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.setTime(time);
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * 取得指定日期所在周的最后一天
     */
    private  Date getLastDayOfWeek(long date) {
        try {
            java.util.Date time = new Date(date);
            Calendar c = new GregorianCalendar();
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.setTime(time);
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int getDateByOr(String sid) {
        N33_JXZEntry entry= N33_JXZDao.getJXZByDate(new ObjectId(sid),getDefauleTermId(new ObjectId(sid)), System.currentTimeMillis());
        if(entry!=null){
            return entry.getSerial();
        }
        return 1;
    }


}
