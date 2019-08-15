package com.fulaan.new33.service.isolate;

import com.db.new33.isolate.GradeDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.fulaan.new33.dto.isolate.GradeDTO;
import com.fulaan.utils.RESTAPI.bo.new33.isolate.isolateAPI;
import com.fulaan.utils.StringUtil;
import com.mongodb.DBObject;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.isolate.TermEntry;
import com.pojo.utils.MongoUtils;
import com.sys.utils.JsonUtil;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IsolateGradeService extends BaseService {

    private GradeDao dao = new GradeDao();
    private TermDao termDao = new TermDao();
    private GradeDao gradeDao = new GradeDao();

    private N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

    public void getIsolateGradeBySid(ObjectId sid, ObjectId xqid){
        List<Grade> gradeList = gradeDao.getIsolateGrADEEntrysByXqid(xqid, sid);
        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        for (Grade grade : gradeList) {
            gradeIds.add(grade.getGradeId());
            gradeMap.put(grade.getGradeId(), grade);
        }
        List<Map<String, Object>> dtoList1 = isolateAPI.getIsolateGradeList(sid.toString());
        List<DBObject> entries = new ArrayList<DBObject>();
        for (Map<String, Object> dto : dtoList1) {
            GradeDTO dto1 = new GradeDTO(
                    "*",
                    "*",
                    (String) dto.get("gnm"),
                    (List<String>) dto.get("cids"),
                    (List<String>) dto.get("gtea"),
                    (List<String>) dto.get("gstu"),
                    (String) dto.get("sid"),
                    (String) dto.get("id"),
                    (Integer) dto.get("type"),
                    (String) dto.get("jie"));
            dto1.setXqid(xqid.toHexString());
            Grade entry = dto1.buildEntry();
            entry.setXQId(xqid);

            if(!gradeIds.contains(entry.getGradeId())){
                entries.add(entry.getBaseEntry());
            }else{
                Grade oldGrade = gradeMap.get(entry.getGradeId());
                if(!oldGrade.getJie().equals(entry.getJie())){
                    Grade grade = gradeMap.get(entry.getGradeId());
                    grade.setJie(entry.getJie());
                    gradeDao.updateIsolateUserEntry(grade);
                }
            }

            TermEntry termEntry = termDao.getTermByTimeId(xqid);
            for (TermEntry.PaiKeTimes paiKeTimes : termEntry.getPaiKeTimes()) {
                if(paiKeTimes.getIr() == 0){
                    List<Grade> gradeList1 = gradeDao.getIsolateGrADEEntrysByXqid(paiKeTimes.getID(),sid);
                    Map<ObjectId,Grade> gradeMap1 = new HashMap<ObjectId, Grade>();
                    for (Grade grade : gradeList1) {
                        gradeMap1.put(grade.getGradeId(), grade);
                    }
                    Grade grade = gradeMap1.get(entry.getGradeId());
                    if(grade != null && !grade.getJie().equals(entry.getJie())){
                        grade.setJie(entry.getJie());
                        gradeDao.updateIsolateUserEntry(grade);
                    }
                }
            }
        }
        if(entries.size() > 0){
            dao.addIsolateGradeEntry(entries);
        }
    }

    public int getCountByXqid(ObjectId xqid, ObjectId sid) {
        return dao.getCountByXqid(xqid, sid);
    }

    public List<Map<String, Object>> getGradeList(ObjectId sid) throws JSONException {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            String result = isolateAPI.getIsolateGrade(sid.toString());
            JSONObject resultJson = new JSONObject(result);
            Map map1 = (Map) JsonUtil.JSONToObj(resultJson.toString(), Map.class);
            List<Map<String, Object>> dtoList1 = (List<Map<String, Object>>) map1.get("message");
            for (Map<String, Object> dto : dtoList1) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("gnm",dto.get("gnm"));
//                if(dto.get("gnm").toString().indexOf("高")==-1){
//                    continue;
//                }
                map.put("sid",dto.get("sid").toString());
                map.put("gid",dto.get("id").toString());
                map.put("jie",dto.get("jie").toString());
                list.add(map);
            }
            return list;
    }
    /**
     * 根据学期id查询
     *
     * @param xqid
     * @return
     */
    public List<GradeDTO> getGradeListByXqid(ObjectId xqid, ObjectId sid) {
        List<Grade> entries = dao.getIsolateGrADEEntrysByXqid(xqid, sid);
        TermEntry termEntry = termDao.getTermByTimeId(xqid);
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if (paiKeTimes.getID().toString().equals(xqid.toString())) {
                gradeIds = paiKeTimes.getGradeIds();
            }
        }
        List<GradeDTO> dtos = new ArrayList<GradeDTO>();
        for (Grade entry : entries) {
            if (gradeIds.contains(entry.getGradeId())) {
                dtos.add(new GradeDTO(entry));
            }
        }
        return dtos;
    }

    /**
     * 根据学期id查询
     *
     * @param xqid
     * @return
     */
    public List<GradeDTO> getNewGradeListByXqid(ObjectId xqid, ObjectId sid) {
        List<Grade> entries = dao.getIsolateGrADEEntrysByXqid(xqid, sid);
        TermEntry termEntry = termDao.getTermByTimeId(xqid);

        List<ObjectId> openIds = turnOffDao.getAllSet(sid, xqid, 1);

        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if (paiKeTimes.getID().toString().equals(xqid.toString())) {
                gradeIds = paiKeTimes.getGradeIds();
            }
        }
        List<GradeDTO> dtos = new ArrayList<GradeDTO>();
        for (Grade entry : entries) {
            if (gradeIds.contains(entry.getGradeId()) && openIds.contains(entry.getGradeId())) {
                dtos.add(new GradeDTO(entry));
            }
        }
        return dtos;
    }


    public List<GradeDTO> getGradeListByXqidList(ObjectId xqid, ObjectId sid) {
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cid = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        //所有次选中的年级
        List<ObjectId> ciGradeIds = new ArrayList<ObjectId>();
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                for (ObjectId gradeId : paiKeTimes.getGradeIds()) {
                    if(!ciGradeIds.contains(gradeId)){
                        ciGradeIds.add(gradeId);
                    }
                }
                cid.add(paiKeTimes.getID());
            }
        }
        List<Grade> entries = dao.getIsolateGrADEEntrys(cid, sid);
        List<GradeDTO> dtos = new ArrayList<GradeDTO>();
        for (Grade entry : entries) {
            if (!gradeIds.contains(entry.getGradeId()) && ciGradeIds.contains(entry.getGradeId())) {
                gradeIds.add(entry.getGradeId());
                dtos.add(new GradeDTO(entry));
            }
        }
        return dtos;
    }

    public List<GradeDTO> getGradeListByCid(ObjectId cid, ObjectId sid) {
        List<Grade> entries = dao.getIsolateGrADEEntrysByXqid(cid, sid);
        TermEntry termEntry = termDao.getTermByTimeId(cid);
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if (paiKeTimes.getID().toString().equals(cid.toString())) {
                gradeIds = paiKeTimes.getGradeIds();
            }
        }
        List<GradeDTO> dtos = new ArrayList<GradeDTO>();
        for (Grade entry : entries) {
            if (gradeIds.contains(entry.getGradeId())) {
                dtos.add(new GradeDTO(entry));
            }
        }
        return dtos;
    }

    public List<Grade> getGradeListByCidForEntryS(ObjectId cid, ObjectId sid) {
        List<Grade> entries = dao.getIsolateGrADEEntrysByXqid(cid, sid);
        return entries;
    }

    public List<Grade> getGradeListByCidForEntry(ObjectId cid, ObjectId sid) {
        List<Grade> entries = dao.getIsolateGrADEEntrysByXqid(cid, sid);
        TermEntry termEntry = termDao.getTermByTimeId(cid);
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if (paiKeTimes.getID().toString().equals(cid.toString())) {
                gradeIds = paiKeTimes.getGradeIds();
            }
        }
        List<Grade> returnEntries = new ArrayList<Grade>();
        for (Grade entry : entries) {
            if (gradeIds.contains(entry.getGradeId())) {
                returnEntries.add(entry);
            }
        }
        return returnEntries;
    }


    /*public List<GradeDTO> getGradeListBySid(ObjectId sid) {
        List<GradeDTO> result = new ArrayList<GradeDTO>();
        Map<String, GradeDTO> map = new HashMap<String, GradeDTO>();
        List<Grade> entries = dao.getGradeBySid(sid);
        for (Grade grade : entries) {
            map.put(grade.getGradeId().toString(), new GradeDTO(grade));
        }
        result.addAll(map.values());
        return result;
    }*/


    public Map<String, Map<String, GradeDTO>> getCiGradeMMap(ObjectId schoolId) {
        Map<String, Map<String, GradeDTO>> ciGradeMMap = new HashMap<String, Map<String, GradeDTO>>();
        List<Grade> entries = dao.getGradeBySid(schoolId);
        for(Grade grade:entries){
            GradeDTO gDto = new GradeDTO(grade);
            Map<String, GradeDTO> gradeMap = ciGradeMMap.get(gDto.getXqid());
            if(null==gradeMap){
                gradeMap = new HashMap<String, GradeDTO>();
            }
            if(null==gradeMap.get(gDto.getGid())) {
                gradeMap.put(gDto.getGid(), gDto);
                ciGradeMMap.put(gDto.getXqid(), gradeMap);
            }
        }
        return ciGradeMMap;
    }

    public void editGradeInfo(ObjectId schoolId, ObjectId ciId) {
        List<Grade> gradeList = dao.getIsolateGrADEEntrysByXqid(ciId, schoolId);
        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        for (Grade grade : gradeList) {
            gradeIds.add(grade.getGradeId());
            gradeMap.put(grade.getGradeId(), grade);
        }
        List<Map<String, Object>> gradeMList = isolateAPI.getIsolateGradeList(schoolId.toString());
        List<Grade> addList = new ArrayList<Grade>();
        for (Map<String, Object> dto : gradeMList) {
            String gradeId = dto.get("id")==null?"":(String) dto.get("id");
            String gradeName = dto.get("gnm")==null?"":(String) dto.get("gnm");
            List<String> classIds =dto.get("cids")==null?new ArrayList<String>():(List<String>) dto.get("cids");
            List<String> teaIds =dto.get("gtea")==null?new ArrayList<String>():(List<String>) dto.get("gtea");
            List<String> stuIds =dto.get("gstu")==null?new ArrayList<String>():(List<String>) dto.get("gstu");
            Integer type = dto.get("type")==null?0:(Integer) dto.get("type");
            String jie = dto.get("jie")==null?"":(String) dto.get("jie");
            Grade gEntry = new Grade(
                    ciId,
                    schoolId,
                    gradeName,
                    MongoUtils.convertToObjectIdList(classIds),
                    MongoUtils.convertToObjectIdList(teaIds),
                    MongoUtils.convertToObjectIdList(stuIds),
                    null,
                    new ObjectId(gradeId),
                    type,
                    jie
            );
            if(!gradeIds.contains(gEntry.getGradeId())){
                addList.add(gEntry);
            }else{
                Grade oldGrade = gradeMap.get(gEntry.getGradeId());
                if(!oldGrade.getJie().equals(gEntry.getJie())||!gradeName.equals(oldGrade.getName())){
                    oldGrade.setJie(gEntry.getJie());
                    oldGrade.setName(gEntry.getName());
                    gradeDao.updateIsolateUserEntry(oldGrade);
                }
            }
        }
        if(addList.size() > 0){
            dao.add(addList);
        }
    }

    public List<GradeDTO> getGradeListByXqid(ObjectId sid) {
        ObjectId xqid = getDefauleTermId(sid);
        List<Grade> entries = dao.getIsolateGrADEEntrysByXqid(xqid, sid);
        List<GradeDTO> dtos = new ArrayList<GradeDTO>();
        for (Grade entry : entries) {
            dtos.add(new GradeDTO(entry));
        }
        return dtos;
    }

    public List<GradeDTO> getGradeListByXqidAndSid(ObjectId sid, ObjectId xqid) {
        List<Grade> entries = dao.getIsolateGrADEEntrysByXqid(xqid, sid);
        List<GradeDTO> dtos = new ArrayList<GradeDTO>();
        for (Grade entry : entries) {
            dtos.add(new GradeDTO(entry));
        }
        return dtos;
    }

    public Grade getGrade(ObjectId xqid, ObjectId gid) {
        return dao.findIsolateGradeEntryByGradeId(gid, xqid);
    }

    public Map<String, String> getCurrGradeMap(ObjectId xqid, ObjectId schoolId) {
        Map<String, String> reMap = new HashMap<String, String>();
        List<Grade> list = dao.getIsolateGrADEEntrysByXqid(schoolId, xqid);
        for (Grade entry : list) {
            GradeDTO dto = new GradeDTO(entry);
            reMap.put(dto.getGnm(), dto.getGid());
        }
        return reMap;
    }
}
