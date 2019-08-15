package com.fulaan.myschool.service;

import com.db.school.CampusDao;
import com.pojo.school.CampusEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fl on 2015/7/23.
 */
@Service
public class CampusService {
    private CampusDao campusDao = new CampusDao();

    public void add(CampusEntry campusEntry){
        campusDao.add(campusEntry);
    }

    public void delete(ObjectId campusId) {
        campusDao.delete(campusId);
    }

    public void edit(ObjectId campusId, CampusEntry campusEntry) {
        campusDao.edit(campusId, campusEntry);
    }

    public List<CampusEntry> list() {
        return campusDao.list();
    }

    public CampusEntry getCampusInfo(ObjectId campusId) {
        return campusDao.getCampusInfo(campusId);
    }
}
