package com.fulaan.new33.service;

import com.db.new33.N33_GDSXDao;
import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.paike.N33_ExClassRecordDao;
import com.fulaan.new33.dto.isolate.N33_GDSXDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.TermEntry;
import com.pojo.new33.paike.N33_ExClassRecordEntry;
import com.pojo.new33.paike.N33_GDSXEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class N33_ExClassRecordService extends BaseService {

    private N33_ExClassRecordDao recordDao = new N33_ExClassRecordDao();
    private GradeDao gradeDao = new GradeDao();

    @Autowired
    private N33_ZKBService zkbService;
    public List<Map<String,Object>> getN33_ExClassRecord(ObjectId xqid,ObjectId sid){
        List<Map<String,Object>> recordList = new ArrayList<Map<String, Object>>();
        List<N33_ExClassRecordEntry> recordEntries = recordDao.getRecordOrderByTime(xqid,sid);
        Map<ObjectId,Grade> gradeMap = gradeDao.findGradeListBySchoolIdMap(xqid,sid);
        for (N33_ExClassRecordEntry recordEntry : recordEntries) {
            Grade grade = gradeMap.get(recordEntry.getGradeId());
            Map<String,Object> map = new HashMap<String, Object>();
            if(grade != null){
                map.put("gradeName",grade.getName());
            }else{
                map.put("gradeName","");
            }
            long time = recordEntry.getTime();
            Date date = new Date(time);
            SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            map.put("time",sd.format(date));
            map.put("ox",recordEntry.getoX());
            map.put("oy",recordEntry.getoY());
            map.put("x",recordEntry.getX());
            map.put("y",recordEntry.getY());
            map.put("teaName",recordEntry.getJXBANDTeaName());
            map.put("oTeaName",recordEntry.getoJXBANDTeaName());
            recordList.add(map);
        }
        return recordList;
    }

    public void cancelRecord(ObjectId xqid,ObjectId sid){
        List<N33_ExClassRecordEntry> recordEntries = recordDao.getRecordOrderByTime(xqid,sid);
        if(recordEntries != null && recordEntries.size() > 0){
            N33_ExClassRecordEntry entry = recordEntries.get(0);
            ObjectId ykbId = entry.getYkbId();
            ObjectId ykbId1 = entry.getoYkbId();
            zkbService.tiaokeLuo(ykbId,ykbId1);
            recordDao.removeN33_ExClassRecordEntry(entry.getID());
        }
    }

    public void deleteRecord(ObjectId xqid,ObjectId sid){
        recordDao.removeN33_ExClassRecordEntryBySid(xqid,sid);
    }
}
