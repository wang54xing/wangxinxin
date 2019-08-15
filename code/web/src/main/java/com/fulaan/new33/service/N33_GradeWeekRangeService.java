package com.fulaan.new33.service;

import com.db.factory.MongoFacroty;
import com.db.new33.N33_GradeWeekRangeDao;
import com.db.new33.isolate.GradeDao;
import com.fulaan.new33.service.isolate.IsolateGradeService;
import com.fulaan.schoolbase.dto.GradeDTO;
import com.fulaan.schoolbase.service.GradeService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_GradeWeekRangeEntry;
import com.pojo.new33.isolate.Grade;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

public class N33_GradeWeekRangeService {

    private N33_GradeWeekRangeDao n33_gradeWeekRangeDao = new N33_GradeWeekRangeDao();

    private GradeService gradeService = new GradeService();

    private IsolateGradeService n33_gradeService = new IsolateGradeService();

    private GradeDao gradeDao = new GradeDao();

    public Map<String,Object> getGradeWeekRangeByXqid(ObjectId xqid, ObjectId sid, ObjectId gid) {
        Map<String,Object> result = new HashMap<String,Object>();
        N33_GradeWeekRangeEntry entry = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(xqid,sid,gid);
        result.put("id",entry.getID().toString());
        result.put("start",entry.getStart());
        result.put("end",entry.getEnd());
        return result;
    }
    public List<Map<String,Object>> getGradeWeekRangeByXqid(ObjectId xqid, ObjectId sid){
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        List<N33_GradeWeekRangeEntry> list = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(xqid,sid);
        List<GradeDTO> gradeList = gradeService.selGradeList(sid);
        Map<String,GradeDTO> gradeDTOMap = new HashMap<String, GradeDTO>();
        for(GradeDTO dto:gradeList){
            gradeDTOMap.put(dto.getId(),dto);
        }
        for(N33_GradeWeekRangeEntry entry:list){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",entry.getID().toString());
            map.put("gid",entry.getGradeId().toString());
            map.put("gnm",gradeDTOMap.get(entry.getGradeId().toString()).getGradeName());
            map.put("start",entry.getStart());
            map.put("end",entry.getEnd());
            result.add(map);
        }
        if(result.size()==0){
            for(GradeDTO gradeDTO:gradeList){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("gid",gradeDTO.getId());
                map.put("gnm",gradeDTO.getGradeName());
                map.put("start","");
                map.put("end","");
                result.add(map);
            }
        }

        return result;
    }

    /**
     * 查询该次排课教学日（用于显示勾选的问题）
     * @param xqid
     * @param sid
     * @return
     */
    public List<Map<String,Object>> getGradeWeekRangeByXqidForView(ObjectId xqid, ObjectId sid){
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        List<N33_GradeWeekRangeEntry> list = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(xqid,sid);
        List<Grade> gradeList = gradeDao.getIsolateGrADEEntrysByXqid(xqid,sid);
        Map<String,Grade> gradeDTOMap = new HashMap<String, Grade>();
        for(Grade dto:gradeList){
            gradeDTOMap.put(dto.getGradeId().toString(),dto);
        }
        for(N33_GradeWeekRangeEntry entry:list){
            if(gradeDTOMap.get(entry.getGradeId().toString()) != null){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("id",entry.getID().toString());
                map.put("gid",entry.getGradeId().toString());
                map.put("gnm",gradeDTOMap.get(entry.getGradeId().toString()).getName());
                map.put("start",entry.getStart());
                map.put("end",entry.getEnd());
                result.add(map);
            }
        }
        if(result.size()==0){

            for(Grade gradeDTO:gradeList){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("gid",gradeDTO.getGradeId());
                map.put("gnm",gradeDTO.getName());
                map.put("start",1);
                map.put("end",5);
                result.add(map);
            }
        }

        return result;
    }

    public void addGradeWeekRangeEntry(Map<String,Object> param,ObjectId schoolId){

        N33_GradeWeekRangeEntry entry = new N33_GradeWeekRangeEntry();
        if(param.get("id")!=null){
            entry.setID(new ObjectId(param.get("id").toString()));
        }
        entry.setXQId(new ObjectId(param.get("xqid").toString()));
        entry.setSchoolId(schoolId);
        entry.setGradeId(new ObjectId(param.get("gid").toString()));
        if(param.get("start")!=null){
            entry.setStart(new Double(param.get("start").toString()).intValue());
        }
        if(param.get("end")!=null){
            entry.setEnd(new Double(param.get("end").toString()).intValue());
        }
        n33_gradeWeekRangeDao.addGradeWeekRangeEntry(entry);
    }

    public void saveList(List<Map<String,Object>> paramList,ObjectId schoolId){
        ObjectId xqid = new ObjectId(paramList.get(0).get("xqid").toString());
        Map<String,N33_GradeWeekRangeEntry> map = n33_gradeWeekRangeDao.getGradeWeekRangeByXqidForMap(xqid,schoolId);
        for(Map<String,Object> param:paramList){
            if(map.get(param.get("gid").toString()) != null){
                n33_gradeWeekRangeDao.updateRangeSetEnd(new ObjectId(param.get("gid").toString()),schoolId,new ObjectId(param.get("xqid").toString()),new Double(param.get("end").toString()).intValue());
            }else{
                N33_GradeWeekRangeEntry entry = new N33_GradeWeekRangeEntry();
                entry.setXQId(new ObjectId(param.get("xqid").toString()));
                entry.setSchoolId(schoolId);
                entry.setGradeId(new ObjectId(param.get("gid").toString()));
                if(param.get("start")!=null){
                    entry.setStart(new Double(param.get("start").toString()).intValue());
                }
                if(param.get("end")!=null){
                    entry.setEnd(new Double(param.get("end").toString()).intValue());
                }
                n33_gradeWeekRangeDao.addGradeWeekRangeEntry(entry);
            }
        }
    }

    public void saveListByXqid(Map dto,ObjectId schoolId){
        ObjectId xqid = new ObjectId(dto.get("xqid").toString());
        Map<String,N33_GradeWeekRangeEntry> map = n33_gradeWeekRangeDao.getGradeWeekRangeByXqidForMap(xqid,schoolId);
        List<N33_GradeWeekRangeEntry> updateList = n33_gradeWeekRangeDao.getGradeWeekRangeByXqid(xqid,schoolId);
        //首先将本次的所有年级的教学日全部设置为传进来的参数
        for (N33_GradeWeekRangeEntry entry:updateList) {
            n33_gradeWeekRangeDao.updateRangeEnd(entry.getID(),new Double(dto.get("end").toString()).intValue());
        }
        List<Grade> gradeList = gradeDao.getIsolateGrADEEntrysByXqid(xqid,schoolId);
        for(Grade grade:gradeList){
            if(map.get(grade.getGradeId().toString()) == null){
                N33_GradeWeekRangeEntry entry = new N33_GradeWeekRangeEntry();
                entry.setXQId(xqid);
                entry.setSchoolId(schoolId);
                entry.setGradeId(grade.getGradeId());
                if(dto.get("start")!=null){
                    entry.setStart(new Double(dto.get("start").toString()).intValue());
                }
                if(dto.get("end")!=null){
                    entry.setEnd(new Double(dto.get("end").toString()).intValue());
                }
                n33_gradeWeekRangeDao.addGradeWeekRangeEntry(entry);
            }
        }
    }

    public void updateRange(ObjectId id,int start,int end){
        n33_gradeWeekRangeDao.updateRange(id,start,end);
    }

    /**
     * 初始化教学日
     * @param sid
     * @param xqid
     */
    public void initGradeWeekRangeEntry(ObjectId sid,ObjectId xqid){

        Map<String,N33_GradeWeekRangeEntry> map = n33_gradeWeekRangeDao.getGradeWeekRangeByXqidForMap(xqid,sid);

        List<N33_GradeWeekRangeEntry> addList = new ArrayList<N33_GradeWeekRangeEntry>();

        List<com.fulaan.new33.dto.isolate.GradeDTO> gradeList = n33_gradeService.getGradeListByXqidAndSid(sid,xqid);
        Collection<N33_GradeWeekRangeEntry> gradeWeekRangeEntries = map.values();
        for (com.fulaan.new33.dto.isolate.GradeDTO dto:gradeList) {
            if(map.get(dto.getGid()) == null){
                N33_GradeWeekRangeEntry entry = new N33_GradeWeekRangeEntry();
                entry.setXQId(xqid);
                entry.setSchoolId(sid);
                entry.setGradeId(new ObjectId(dto.getGid()));
                if(map.values().size()>0) {
                    for (N33_GradeWeekRangeEntry entry1:gradeWeekRangeEntries){
                        entry.setStart(entry1.getStart());
                        entry.setEnd(entry1.getEnd());
                        break;
                    }
                } else{
                    entry.setStart(1);
                    entry.setEnd(5);
                }
                addList.add(entry);
            }
        }
        if(addList.size() > 0){
            n33_gradeWeekRangeDao.addGradeWeekRangeEntryList(addList);
        }
    }
}
