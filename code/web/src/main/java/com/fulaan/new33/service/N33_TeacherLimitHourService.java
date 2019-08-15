package com.fulaan.new33.service;

import com.db.factory.MongoFacroty;
import com.db.new33.N33_TeacherLimitHourDao;
import com.fulaan.schoolbase.dto.GradeDTO;
import com.fulaan.schoolbase.service.GradeService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.new33.N33_TeacherLimitHourEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N33_TeacherLimitHourService {

    private N33_TeacherLimitHourDao n33_teacherLimitHourDao = new N33_TeacherLimitHourDao();

    private GradeService gradeService = new GradeService();

    public Map<String,Object> getTeacherLimitEntryByXqid(ObjectId xqid, ObjectId sid, ObjectId gid) {
        Map<String,Object> result = new HashMap<String,Object>();
        N33_TeacherLimitHourEntry entry = n33_teacherLimitHourDao.getTeacherLimitEntryByXqid(xqid,sid,gid);
        if(entry!=null){
            result.put("id",entry.getID().toString());
            result.put("hour",entry.getHour());
        }
        else {
            result.put("id","");
            result.put("hour","");
        }
        return result;
    }
    public List<Map<String,Object>> getTeacherLimitEntryByXqid(ObjectId xqid, ObjectId sid){
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        List<N33_TeacherLimitHourEntry> list = n33_teacherLimitHourDao.getTeacherLimitEntryByXqid(xqid,sid);
        List<GradeDTO> gradeList = gradeService.selGradeList(sid);
        Map<String,GradeDTO> gradeDTOMap = new HashMap<String, GradeDTO>();
        for(GradeDTO dto:gradeList){
            gradeDTOMap.put(dto.getId(),dto);
        }
        for(N33_TeacherLimitHourEntry entry:list){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",entry.getID().toString());
            map.put("gid",entry.getGradeId().toString());
            map.put("gnm",gradeDTOMap.get(entry.getGradeId().toString()).getGradeName());
            map.put("hour",entry.getHour());
            result.add(map);
        }
        if(result.size()==0){

            for(GradeDTO gradeDTO:gradeList){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("gid",gradeDTO.getId());
                map.put("gnm",gradeDTO.getGradeName());
                map.put("hour","");
                result.add(map);
            }
        }
        return result;
    }
    public void addLimitEntry(Map<String,Object> param,ObjectId sid){
        N33_TeacherLimitHourEntry entry = new N33_TeacherLimitHourEntry();
        if(param.get("id")!=null){
            entry.setID(new ObjectId(param.get("id").toString()));
        }
        entry.setXQId(new ObjectId(param.get("xqid").toString()));
        entry.setSchoolId(sid);
        entry.setGradeId(new ObjectId(param.get("gid").toString()));
        entry.setHour(new Double(param.get("hour").toString()).intValue());
        n33_teacherLimitHourDao.addLimitEntry(entry);

    }
//    public void saveList(List<Map<String,Object>> paramList){
//        for(Map<String,Object> param:paramList){
//            addLimitEntry(param);
//        }
//    }
    public void updateHour(ObjectId id,int hour){
        n33_teacherLimitHourDao.updateHour(id,hour);
    }
}
