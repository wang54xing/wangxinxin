package com.fulaan.new33.service;

import com.db.new33.N33_ZjkjDao;
import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.paike.N33_JXBDao;
import com.db.new33.paike.N33_TkLogDao;
import com.db.new33.paike.N33_ZKBDao;
import com.fulaan.new33.dto.N33_ZjkjDTO;
import com.fulaan.new33.service.isolate.N33_TurnOffService;
import com.pojo.new33.N33_ZjkjEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.paike.N33_JXBEntry;
import com.pojo.new33.paike.N33_TkLogEntry;
import com.pojo.new33.paike.N33_ZKBEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class N33_ZjkjService {

    private N33_ZjkjDao zjkjDao = new N33_ZjkjDao();

    private N33_ZKBDao zkbDao = new N33_ZKBDao();

    private N33_JXBDao jxbDao = new N33_JXBDao();

    private N33_TeaDao teaDao = new N33_TeaDao();

    private N33_TkLogDao tkLogDao = new N33_TkLogDao();

    @Autowired
    private N33_TurnOffService turnOffService;

    /**
     *
     * @param zkbId
     * @return
     */
    public void setJkj(String zkbId,ObjectId userId) {
        N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(new ObjectId(zkbId));
        zjkjDao.addZjkjEntry(new N33_ZjkjEntry(zkbEntry.getX(),zkbEntry.getY(),zkbEntry.getClassroomId(),zkbEntry.getSchoolId(),zkbEntry.getTermId(),zkbEntry.getWeek(),userId,zkbEntry.getJxbId(),zkbEntry.getSubjectId(),
                zkbEntry.getTeacherId(),zkbEntry.getGradeId(),zkbEntry.getCId()));
        tkLogDao.addN33_TkLogEntry(new N33_TkLogEntry(new ObjectId(zkbId), null, zkbEntry.getSchoolId(), zkbEntry.getGradeId(), zkbEntry.getTermId(), userId,
                zkbEntry.getSubjectId(), null, zkbEntry.getTeacherId(), null, 3, String.valueOf(zkbEntry.getWeek()), 1, 0,
                zkbEntry.getX() + "," + zkbEntry.getY(), zkbEntry.getX() + "," + zkbEntry.getY(),null,zkbEntry.getJxbId()));
        zkbEntry.setIsUse(0);
        zkbEntry.setJxbId(null);
        zkbEntry.setTeacherId(null);
        zkbEntry.setSubjectId(null);
        zkbDao.updateN33_ZKB(zkbEntry);

    }

    /**
     *
     * @param zkbId
     * @param jxbId
     * @param userId
     */
    public void setZkj(String zkbId, String jxbId, ObjectId userId) {
        N33_JXBEntry jxbEntry = jxbDao.getJXBById(new ObjectId(jxbId));
        N33_ZKBEntry zkbEntry = zkbDao.getN33_ZKBById(new ObjectId(zkbId));
        zkbEntry.setJxbId(jxbEntry.getID());
        zkbEntry.setSubjectId(jxbEntry.getSubjectId());
        zkbEntry.setTeacherId(jxbEntry.getTercherId());
        zkbEntry.setGradeId(jxbEntry.getGradeId());
        zkbEntry.setIsUse(1);
        zkbDao.updateN33_ZKB(zkbEntry);
        tkLogDao.addN33_TkLogEntry(new N33_TkLogEntry(new ObjectId(zkbId), null, zkbEntry.getSchoolId(), zkbEntry.getGradeId(), zkbEntry.getTermId(), userId,
                zkbEntry.getSubjectId(), null, zkbEntry.getTeacherId(), null, 2, String.valueOf(zkbEntry.getWeek()), 1, 0,
                zkbEntry.getX() + "," + zkbEntry.getY(), zkbEntry.getX() + "," + zkbEntry.getY(),null,new ObjectId(jxbId)));
        List<N33_ZjkjEntry> zjkjEntrys = zjkjDao.getZjkjByJXBId(zkbEntry.getTermId(),zkbEntry.getSchoolId(),zkbEntry.getGradeId(),new ObjectId(jxbId),zkbEntry.getCId());
        if (zjkjEntrys!=null && zjkjEntrys.size()!=0) {
            N33_ZjkjEntry zjkjEntry = zjkjEntrys.get(0);
            zjkjEntry.setIsUse(1);
            zjkjDao.updateN33_ZjkjEntry(zjkjEntry);
        }

    }

    /**
     *
     * @param xqId
     * @param gradeId
     * @param schoolId
     * @return
     */
    public List<N33_ZjkjDTO> getZjkjList(String xqId,String ciId, String gradeId,ObjectId schoolId) {
        String[] weekArgs = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        List<N33_ZjkjDTO>  zjkjDTOS = new ArrayList<N33_ZjkjDTO>();
        List<N33_ZjkjEntry> zjkjEntryList = zjkjDao.getZjkjList(new ObjectId(xqId),new ObjectId(gradeId),schoolId);
        if (zjkjEntryList!=null && zjkjEntryList.size()!=0) {
            List<ObjectId> teacherIds = MongoUtils.getFieldObjectIDs(zjkjEntryList, "tid");
            List<ObjectId> jxbIds = MongoUtils.getFieldObjectIDs(zjkjEntryList, "jxbId");
            Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(teacherIds, new ObjectId(ciId));
            Map<ObjectId, N33_JXBEntry> jxbEntryMap = jxbDao.getJXBMapsByIds(jxbIds);
            for (N33_ZjkjEntry entry : zjkjEntryList) {
                N33_ZjkjDTO zjkjDTO = new N33_ZjkjDTO();
                zjkjDTO.setJxbName(jxbEntryMap.get(entry.getJxbId()).getName());
                zjkjDTO.setTeacherName(teaEntryMap.get(entry.getTeacherId()).getUserName());
                zjkjDTO.setKeji(weekArgs[entry.getX()]+"第"+(entry.getY()+1)+"节");
                zjkjDTO.setWeek("第"+entry.getWeek()+"周");
                zjkjDTOS.add(zjkjDTO);
            }
        }
        return zjkjDTOS;
    }

    public List<N33_ZjkjDTO> getJXBList(String zkbId, String ciId) {
        List<N33_ZjkjDTO> zjkjDTOS = new ArrayList<N33_ZjkjDTO>();
        N33_ZKBEntry entry = zkbDao.getN33_ZKBById(new ObjectId(zkbId));
        int acClassType = turnOffService.getAcClassType(entry.getSchoolId(), entry.getGradeId(), new ObjectId(ciId),1);
        List<N33_JXBEntry> jxbEntryList = jxbDao.getJxbListByRoomId(entry.getGradeId(), entry.getClassroomId(), new ObjectId(ciId), acClassType);
        List<N33_ZKBEntry> zkbEntryList = zkbDao.getN33_ZKBByWeek(entry.getWeek(),entry.getX(),entry.getY(),entry.getTermId());

        List<ObjectId> teas = new ArrayList<ObjectId>();
        if (zkbEntryList!=null && zkbEntryList.size()!=0) {
          teas.addAll(MongoUtils.getFieldObjectIDs(zkbEntryList,"tid"));
          teas.addAll(MongoUtils.getFieldObjectIDs(zkbEntryList,"ntid"));
        }

        if (jxbEntryList!=null && jxbEntryList.size()!=0) {
            Map<ObjectId, N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(MongoUtils.getFieldObjectIDs(jxbEntryList,"tid"), new ObjectId(ciId));
            for (N33_JXBEntry jxb : jxbEntryList) {
                if (teas==null || !teas.contains(jxb.getTercherId())) {
                    N33_ZjkjDTO dto = new N33_ZjkjDTO();
                    dto.setJxbId(jxb.getID().toString());
                    dto.setTeacherName(teaEntryMap.get(jxb.getTercherId()).getUserName());
                    dto.setJxbName(jxb.getName());
                    zjkjDTOS.add(dto);
                }
            }
        }
        return zjkjDTOS;
    }
}
