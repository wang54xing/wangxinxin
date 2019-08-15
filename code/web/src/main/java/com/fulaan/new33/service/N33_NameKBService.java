package com.fulaan.new33.service;

import com.db.new33.paike.*;
import com.fulaan.new33.dto.paike.N33_NameKBDTO;
import com.fulaan.new33.dto.paike.N33_NameTypeDTO;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_NameKBEntry;
import com.pojo.new33.paike.N33_NameTypeEntry;
import com.pojo.new33.paike.N33_YKBEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by albin on 2018/3/20.
 */
@Service
public class N33_NameKBService {
    private N33_NameTypeDao typeDao = new N33_NameTypeDao();
    private N33_NameKBDao kbDao = new N33_NameKBDao();
    private N33_YKBDao ykbDao = new N33_YKBDao();
    private N33_JXBKSDao jxbksDao = new N33_JXBKSDao();
    private N33_JXBDao jxbDao = new N33_JXBDao();
    private N33_ZIXIKEDao zixikeDao = new N33_ZIXIKEDao();


    /**
     * 创建命名课表
     *
     * @param xqid
     * @param sid
     * @param name
     */
    public void addNameKB(ObjectId xqid, ObjectId sid, String name, ObjectId gid, ObjectId uid) {
        String KBName = getTypeName(name);
        //获得原课表
        List<N33_YKBEntry> ykbEntries = ykbDao.getYKBEntrysList(xqid, sid, gid);
        //创建新的课表
        if (ykbEntries.size() > 0) {
            //创建新的命名
            ObjectId nbid = addNameType(KBName, xqid, sid, gid, uid);
            List<N33_NameKBEntry> entries = getEntrys(ykbEntries, nbid, KBName);
            kbDao.addN33_NameKBEntryList(entries);
        }
    }

    /**
     * 新增新的命名
     *
     * @param name
     * @param xqid
     * @param sid
     * @param gid
     * @param uid
     * @return
     */
    public ObjectId addNameType(String name, ObjectId xqid, ObjectId sid, ObjectId gid, ObjectId uid) {
        ObjectId nbid = new ObjectId();
        N33_NameTypeEntry typeEntry = new N33_NameTypeEntry(gid, sid, xqid, nbid, name, uid, System.currentTimeMillis());
        typeDao.addN33_Name(typeEntry);
        return nbid;
    }

    /**
     * 命名课表初始化
     *
     * @param name
     * @return
     */
    public String getTypeName(String name) {
        if (name.equals("*")) {
            //时间
            Long time = System.currentTimeMillis();
            String date = DateTimeUtils.convert(time, "yyyy-MM-dd hh:mm:ss");
            return date + "课表";
        }
        return name;
    }

    /**
     * 封装DTO
     *
     * @param entry
     * @return
     */
    public List<N33_NameKBEntry> getEntrys(List<N33_YKBEntry> entry, ObjectId nid, String name) {
        //封装Entry
        List<N33_NameKBEntry> zbkEntry = new ArrayList<N33_NameKBEntry>();
        for (N33_YKBEntry en : entry) {
            N33_NameKBDTO dto = new N33_NameKBDTO(en, name, nid.toHexString());
            N33_NameKBEntry entry1 = dto.getEntry();
            entry1.setType(en.getType());
            zbkEntry.add(entry1);
        }
        return zbkEntry;
    }

    /**
     * 加载某个命名课表
     *
     * @param nid
     * @return
     */
    public void getDtoByNid(ObjectId nid, ObjectId xqid, ObjectId gid) {
        List<N33_NameKBEntry> entries = kbDao.getN33_NameKBEntryBynId(nid, gid);
        List<N33_YKBEntry> entries1 = new ArrayList<N33_YKBEntry>();
        for (N33_NameKBEntry entry : entries) {
            N33_YKBEntry ykbEntry = new N33_YKBEntry(entry);
            ykbEntry.setNTeacherId(entry.getNTeacherId());
            ykbEntry.setNJxbId(entry.getNJxbId());
            ykbEntry.setNSubjectId(entry.getNSubjectId());
            ykbEntry.setType(entry.getType());
            entries1.add(ykbEntry);
        }
        //情空原课表
        if (entries.size() > 0) {
            //还原原课表
            List<ObjectId> clsId = new ArrayList<ObjectId>();
            for (N33_YKBEntry entry : entries1) {
                clsId.add(entry.getClassroomId());
            }
            List<N33_YKBEntry> ykbEntries = ykbDao.getYKBsByclassRoomIds(xqid, entries.get(0).getSchoolId(), clsId);
            for (N33_YKBEntry entry : ykbEntries) {
                if (entry.getGradeId() != null) {
                    if (entry.getGradeId().toString().equals(gid.toString())) {
                        entry.setGradeId(null);
                        entry.setTeacherId(null);
                        entry.setJxbId(null);
                        entry.setSubjectId(null);
                        entry.setNSubjectId(null);
                        entry.setNJxbId(null);
                        entry.setNTeacherId(null);
                        entry.setIsUse(0);
                        entry.setType(0);
                        ykbDao.updateN33_YKB(entry);
                    }
                }
            }
            zixikeDao.removeN33_ZIXIKEEntryByXqGiD(xqid, gid);
//            zixikeDao.delN33_ZIXIKEEntryCid(xqid);
            for (N33_YKBEntry entry : entries1) {
                for (N33_YKBEntry en : ykbEntries) {
                    if (entry.getClassroomId().toString().equals(en.getClassroomId().toString()) && entry.getX() == en.getX() && en.getY() == entry.getY()) {
                        entry.setID(en.getID());
                        ykbDao.updateN33_YKB(entry);
                    }
                }
            }
            //清空教学班课时
            jxbksDao.updateN33_JXBKS(xqid, gid);
            for (N33_NameKBEntry n33_nameKBEntry : entries) {
                jxbksDao.updateN33_JXBKSCountByJxbId(n33_nameKBEntry.getJxbId());
                if (n33_nameKBEntry.getType() == 5) {
                    zixikeDao.update(n33_nameKBEntry.getJxbId(), 0);
                }
                if (n33_nameKBEntry.getType() == 4) {
                    Integer count = 0;
                    for (N33_NameKBEntry kbEntry : entries) {
                        if (n33_nameKBEntry.getJxbId().toString().equals(kbEntry.getJxbId().toString()) && n33_nameKBEntry.getClassroomId().toString().equals(kbEntry.getClassroomId().toString())) {
                            count++;
                        }
                    }
                    jxbksDao.updateN33_JXBKSCountByJxbId(n33_nameKBEntry.getJxbId(), count);
                }
                if(n33_nameKBEntry.getType()==6){
                    jxbksDao.updateN33_JXBKSCountByJxbId(n33_nameKBEntry.getNJxbId());
                }
            }
        }
    }

    /**
     * 加载命名课表list
     *
     * @param xqid
     * @param sid
     * @return
     */
    public List<Map> getDtoType(ObjectId xqid, ObjectId sid, ObjectId gid) {
        List<N33_NameTypeEntry> entries = typeDao.getN33_NameKBEntryBynId(sid, gid, xqid);
        List<Map> dtos = new ArrayList<Map>();
        for (N33_NameTypeEntry entry : entries) {
            Map dto = new HashMap();
            dto.put("name", entry.getName());
            dto.put("id", entry.getNameId().toString());
            dtos.add(dto);
        }
        return dtos;
    }
}
