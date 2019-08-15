package com.fulaan.schoolbase.service;

import com.db.classroom.NotClassroomReserveDao;
import com.pojo.classroom.NotClassroomReserveEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by albin on 2017/12/14.
 */
public class NotClassroomReserveService {
    private NotClassroomReserveDao reserveDao = new NotClassroomReserveDao();

    public void delete(ObjectId id) {
        reserveDao.delete(id);
    }

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addNotClassroomReserveEntry(NotClassroomReserveEntry e) {
        if (reserveDao.getNotClassroomReserveEntry(e.getClassRoomID()) == null) {
            return reserveDao.addNotClassroomReserveEntry(e);
        } else {
            reserveDao.updateNotClassroomReserveEntry(e);
            return e.getID();
        }
    }

    /**
     * @param sid
     * @param sta 0全校 1免预约  2不能预约
     * @return
     */
    public List<Map<String, Object>> getNotClassroomReserveEntry(ObjectId sid, Integer sta) {
        List<NotClassroomReserveEntry> notEntry = reserveDao.getNotClassroomReserveEntrys(sid, sta);
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (NotClassroomReserveEntry entry : notEntry) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rnm", entry.getClassRoomName());
            map.put("rid", entry.getClassRoomID().toString());
            map.put("id", entry.getID().toString());
            maps.add(map);
        }
        return maps;
    }

    public Map<ObjectId, NotClassroomReserveEntry> getNotClassroomReserveEntry(ObjectId sid, List<ObjectId> rid) {
        Map<ObjectId, NotClassroomReserveEntry> notEntry = reserveDao.getNotClassroomReserveEntrys(sid, rid);
        return notEntry;
    }

    /**
     * true 需要审核  false不用审核
     *
     * @param rmid
     * @return
     */
    public Boolean getMianClassroomReserveEntry(ObjectId rmid) {
        return reserveDao.getMianClassroomReserveEntry(rmid) == null;
    }

    public Map<ObjectId,NotClassroomReserveEntry> getUserRoom(ObjectId uid) {
        return reserveDao.getUserRoom(uid);
    }

}
