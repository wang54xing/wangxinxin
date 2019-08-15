package com.fulaan.new33.service;

import com.db.new33.isolate.N33_TeaDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.paike.N33_TeaRulesDao;
import com.fulaan.new33.dto.isolate.N33_TeaDTO;
import com.fulaan.new33.dto.isolate.N33_TeaRulesDTO;
import com.pojo.new33.CourseRangeEntry;
import com.pojo.new33.isolate.N33_TeaEntry;
import com.pojo.new33.isolate.N33_TeaRules;
import com.pojo.new33.isolate.TermEntry;
import com.pojo.new33.paike.N33_RequireTime;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class N33_TeaRulesService {
    private N33_TeaRulesDao teaRulesDao = new N33_TeaRulesDao();

    private N33_TeaDao teaDao = new N33_TeaDao();

    private TermDao termDao = new TermDao();

    /**
     * 查询教师规则
     * @param xqid
     * @param teaId
     * @return
     */
    public List<N33_TeaRulesDTO.TeaRulesListDTO> getTeaRulesDTOByXqidAndTeaId(List<String> gradeIds, ObjectId xqid, ObjectId teaId){
        List<N33_TeaRulesDTO.TeaRulesListDTO> retList = new ArrayList<N33_TeaRulesDTO.TeaRulesListDTO>();
        N33_TeaRules teaRules = teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,teaId);
        if(teaRules == null){
            return retList;
        }
        N33_TeaRulesDTO teaRulesDTO = new N33_TeaRulesDTO(teaRules);
        List<N33_TeaRulesDTO.TeaRulesListDTO> teaRulesListDTOList = teaRulesDTO.getTeaRulesList();
        if(teaRulesListDTOList != null && teaRulesListDTOList.size() > 0){
            for (N33_TeaRulesDTO.TeaRulesListDTO dto : teaRulesListDTOList) {
                List<String> dtoGradeList = dto.getGid();
                List<String> replaceGradeIds = new ArrayList<String>();
                for (String ids : dtoGradeList) {
                    replaceGradeIds.add(ids);
                }
                replaceGradeIds.retainAll(gradeIds);
                if(replaceGradeIds.size() > 0){
                    retList.add(dto);
                }
            }
        }
        return retList;
    }

    public List<N33_TeaRulesDTO> getTeaRulesByXy(List<String> teaList,ObjectId xqid,Integer x,Integer y){
        List<N33_TeaRulesDTO> retList = new ArrayList<N33_TeaRulesDTO>();
        Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(MongoUtils.convertToObjectIdList(teaList));
        for (String teaId : teaList) {
            N33_TeaRules teaRules = teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,new ObjectId(teaId));
            if(teaRules != null){
                N33_TeaRulesDTO teaRulesDTO = new N33_TeaRulesDTO(teaRules);
                List<N33_TeaRulesDTO.TeaRulesListDTO> newList = new ArrayList<N33_TeaRulesDTO.TeaRulesListDTO>();
                for (N33_TeaRulesDTO.TeaRulesListDTO dto : teaRulesDTO.getTeaRulesList()) {
                    if(dto.getX().intValue() == x.intValue() && dto.getY().intValue() == y.intValue()){
                        if(dto.getStatus() == 1){
                            dto.setStatusDesc("必须排课");
                            dto.setColor("c0a");
                        }else if(dto.getStatus() == 2){
                            dto.setStatusDesc("优先排课");
                            dto.setColor("cb5");
                        }else if(dto.getStatus() == 3){
                            dto.setStatusDesc("不排课");
                            dto.setColor("c27");
                        }else if(dto.getStatus() == 4){
                            dto.setStatusDesc("避免排课");
                            dto.setColor("cef");
                        }
                        newList.add(dto);
                    }
                }
                teaRulesDTO.setTeaRulesList(newList);
                teaRulesDTO.setTeaName(teaEntryMap.get(new ObjectId(teaId)).getUserName());
                retList.add(teaRulesDTO);
            }
        }
        return retList;
    }

    public List<N33_TeaRulesDTO> getTeaRulesDTOListByXqidAndTeaId(List<String> teaList,ObjectId xqid,Integer status) {
        List<N33_TeaRulesDTO> retList = new ArrayList<N33_TeaRulesDTO>();

        Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.getTeaMap(MongoUtils.convertToObjectIdList(teaList));
        for (String teaId : teaList) {
            N33_TeaRules teaRules = teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,new ObjectId(teaId));
            if(teaRules != null){
                N33_TeaRulesDTO teaRulesDTO = new N33_TeaRulesDTO(teaRules);
                List<N33_TeaRulesDTO.TeaRulesListDTO> newList = new ArrayList<N33_TeaRulesDTO.TeaRulesListDTO>();
                for (N33_TeaRulesDTO.TeaRulesListDTO dto : teaRulesDTO.getTeaRulesList()) {
                    if((dto.getStatus().intValue() == status.intValue()) || (status.intValue() == 0)){
                        if(dto.getStatus() == 1){
                            dto.setStatusDesc("必须排课");
                        }else if(dto.getStatus() == 2){
                            dto.setStatusDesc("优先排课");
                        }else if(dto.getStatus() == 3){
                            dto.setStatusDesc("不排课");
                        }else if(dto.getStatus() == 4){
                            dto.setStatusDesc("避免排课");
                        }
                        newList.add(dto);
                    }
                }
                teaRulesDTO.setTeaRulesList(newList);
                teaRulesDTO.setTeaName(teaEntryMap.get(new ObjectId(teaId)).getUserName());
                if(teaEntryMap.get(new ObjectId(teaId)).getSex() == 0){
                    teaRulesDTO.setSexStr("女");
                }else{
                    teaRulesDTO.setSexStr("男");
                }
                teaRulesDTO.setSize(teaRulesDTO.getTeaRulesList().size());
                retList.add(teaRulesDTO);
            }
        }

        return retList;
    }


    public void saveTeaRulesDTO(N33_TeaRulesDTO dto){
        ObjectId xqid = new ObjectId(dto.getXqid());
        ObjectId teaId = new ObjectId(dto.getTeaId());
        ObjectId sid = new ObjectId(dto.getSid());
        if(dto.getTeaRulesList() != null && dto.getTeaRulesList().size() == 0){
            teaRulesDao.removeTeaRulesByXqidAndSIDAndTeaId(xqid,sid,teaId);
            return;
        }
        if(teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,teaId) == null){
            N33_TeaRules teaRules = dto.buildEntry();
            teaRulesDao.addTeaRules(teaRules);
        }else {
            N33_TeaRules teaRules = dto.buildEntry();
            teaRules.setID(teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,teaId).getID());
            teaRulesDao.updateTeaRules(teaRules);
        }
    }

	public void addTeaRulesByTeaId(N33_TeaRulesDTO dto){
		ObjectId xqid = new ObjectId(dto.getXqid());
		ObjectId teaId = new ObjectId(dto.getTeaId());
		if(teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,teaId) == null){
			N33_TeaRules teaRules = dto.buildEntry();
			teaRulesDao.addTeaRules(teaRules);
		}else {
			N33_TeaRules teaRules = dto.buildEntry();
			N33_TeaRules addTeaRulesEntry = teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,teaId);
			List<N33_TeaRules.TeaRulesList> addTeaRulesList = addTeaRulesEntry.getTeaRulesList();
            addTeaRulesList.addAll(teaRules.getTeaRulesList());
            addTeaRulesEntry.setTeaRulesList(addTeaRulesList);
			teaRules.setID(teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,teaId).getID());
			teaRulesDao.updateTeaRules(addTeaRulesEntry);
		}
	}

    public void syncLastTermTeaRules(String schoolId,String xqid,ObjectId teaId) {
        teaRulesDao.removeTeaRulesByXqidAndSIDAndTeaId(new ObjectId(schoolId), new ObjectId(xqid),teaId);
        List<TermEntry> termList = termDao.getIsolateTermEntrysBySidOrder(new ObjectId(schoolId));
        String lastXqid = null;
        for(int i=termList.size()-1;i>=0;i--) {
            if(termList.get(i).getID().toString().equals(xqid)&&i!=0) {
                lastXqid = termList.get(i-1).getID().toString();
            }
        }
        if(lastXqid!=null) {
            N33_TeaRules teaRules = teaRulesDao.getTeaRulesByXqidAndTeaId(new ObjectId(lastXqid),teaId);
            teaRules.setID(new ObjectId());
            teaRules.setXqid(new ObjectId(xqid));
            teaRulesDao.addTeaRules(teaRules);
        }
    }

    public List<N33_TeaDTO> getTea(ObjectId xqid,ObjectId gid,ObjectId subId,String name){
        TermEntry termEntry = termDao.findIsolateTermEntryEntryById(xqid);
        List<ObjectId> cids = new ArrayList<ObjectId>();
        List<TermEntry.PaiKeTimes> paiKeTimeses = termEntry.getPaiKeTimes();
        for (TermEntry.PaiKeTimes paiKeTimes : paiKeTimeses) {
            if(paiKeTimes.getIr()==0) {
                cids.add(paiKeTimes.getID());
            }
        }
        Map<ObjectId,N33_TeaEntry> teaEntryMap = teaDao.findN33_TeaEntryBySubjectId(cids,subId.toString(),name,gid.toString());
        List<N33_TeaDTO> retList = new ArrayList<N33_TeaDTO>();
        if(teaEntryMap != null){
            for (N33_TeaEntry teaEntry : teaEntryMap.values()) {
                retList.add(new N33_TeaDTO(teaEntry));
            }
        }
        return retList;
    }

    /**
     * 同意教师规则
     */
    public void agreeRules(ObjectId ruleId,ObjectId eachId){
        N33_TeaRules teaRules = teaRulesDao.getTeaRulesById(ruleId);
        for (N33_TeaRules.TeaRulesList teaRulesList : teaRules.getTeaRulesList()) {
            if(eachId.toString().equals(teaRulesList.getID().toString())){
                teaRulesList.setRequire(1);
            }
        }
        teaRulesDao.updateTeaRules(teaRules);
    }

    /**
     * 同意教师规则
     */
    public void refuseRules(ObjectId ruleId,ObjectId eachId){
        N33_TeaRules teaRules = teaRulesDao.getTeaRulesById(ruleId);
        for (N33_TeaRules.TeaRulesList teaRulesList : teaRules.getTeaRulesList()) {
            if(eachId.toString().equals(teaRulesList.getID().toString())){
                teaRulesList.setRequire(2);
            }
        }
        teaRulesDao.updateTeaRules(teaRules);
    }

    /**
     * 删除老师的规则
     */
    /*public void deleteTeaRules(List<String> gradeList,ObjectId teaId,ObjectId xqid,ObjectId id){
        N33_TeaRules teaRules = teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,teaId);
        List<N33_TeaRules.TeaRulesList> teaRulesLists = new ArrayList<N33_TeaRules.TeaRulesList>();
        for (N33_TeaRules.TeaRulesList teaRulesList : teaRules.getTeaRulesList()) {
            if(!id.toString().equals(teaRulesList.getID().toString())){
                teaRulesLists.add(teaRulesList);
            }else{
                for (ObjectId ids : teaRulesList.getGids()) {
                    if(!gradeList.contains(ids.toString())){
                        teaRulesLists.add(teaRulesList);
                        break;
                    }
                }
            }
        }
        for (N33_TeaRules.TeaRulesList teaRulesList : teaRulesLists) {
            if(id.toString().equals(teaRulesList.getID().toString())){
                List<ObjectId> gradeIds = teaRulesList.getGids();
                gradeIds.removeAll(MongoUtils.convertToObjectIdList(gradeList));
                teaRulesList.setGids(gradeIds);
            }
        }
        teaRules.setTeaRulesList(teaRulesLists);
        teaRulesDao.updateTeaRules(teaRules);
    }*/

    /**
     * 删除老师的规则
     */
    public void deleteTeaRules(List<String> gradeList,ObjectId teaId,ObjectId xqid,ObjectId id){
        N33_TeaRules teaRules = teaRulesDao.getTeaRulesByXqidAndTeaId(xqid,teaId);
        List<N33_TeaRules.TeaRulesList> teaRulesLists = new ArrayList<N33_TeaRules.TeaRulesList>();
        for (N33_TeaRules.TeaRulesList teaRulesList : teaRules.getTeaRulesList()) {
            if(!id.toString().equals(teaRulesList.getID().toString())){
                teaRulesLists.add(teaRulesList);
            }else{
                List<ObjectId> gradeIds = teaRulesList.getGids();
                gradeIds.removeAll(MongoUtils.convertToObjectIdList(gradeList));
                if(gradeIds.size() == 0){
                    continue;
                }else{
                    teaRulesList.setGids(gradeIds);
                    teaRulesLists.add(teaRulesList);
                }

            }
        }
        teaRules.setTeaRulesList(teaRulesLists);
        teaRulesDao.updateTeaRules(teaRules);
    }

    public void updateRequireTime(ObjectId sid,ObjectId termId,String start,String end) throws ParseException {
        N33_RequireTime requireTime = teaRulesDao.getRequireTime(sid,termId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(requireTime != null){
            requireTime.setStart(sdf.parse(start).getTime());
            requireTime.setEnd(sdf.parse(end).getTime());
            teaRulesDao.updateRequireTime(requireTime);
        }else{
            N33_RequireTime requireTime1 = new N33_RequireTime(sid,termId,sdf.parse(start).getTime(),sdf.parse(end).getTime());
            teaRulesDao.addRequireTime(requireTime1);
        }
    }

    /**
     * 查询教师需求提交时间
     * @param sid
     * @param termId
     * @return
     */
    public Map<String,Object> getRequireTime(ObjectId sid,ObjectId termId){
        Map<String,Object> retMap = new HashMap<String, Object>();
        N33_RequireTime requireTime = teaRulesDao.getRequireTime(sid,termId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(requireTime != null){
            retMap.put("startLong",requireTime.getStart());
            retMap.put("endLong",requireTime.getEnd());
            retMap.put("start",sdf.format(new Date(requireTime.getStart())));
            retMap.put("end",sdf.format(new Date(requireTime.getEnd())));
        }
        return retMap;
    }

}
