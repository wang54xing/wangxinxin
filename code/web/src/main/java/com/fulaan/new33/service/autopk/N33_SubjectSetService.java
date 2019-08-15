package com.fulaan.new33.service.autopk;

import com.db.new33.autopk.N33_SubjectSetDao;
import com.db.new33.isolate.SubjectDao;
import com.fulaan.new33.dto.autopk.N33_SubjectSetDTO;
import com.fulaan.new33.dto.autopk.N33_ZouBanSetDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.pojo.autoPK.N33_SubjectSetEntry;
import com.pojo.autoPK.N33_ZouBanSetEntry;
import com.pojo.new33.isolate.N33_KSEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class N33_SubjectSetService extends BaseService {
	private N33_SubjectSetDao subjectSetDao = new N33_SubjectSetDao();

	private SubjectDao subjectDao = new SubjectDao();

	/**
	 * 勾选和修改自动排课规则中的学科设置
	 * @param map
	 */
	public void addSubjectSet(Map<String,Object> map){
		ObjectId sid = new ObjectId((String) map.get("sid"));
		ObjectId ciId = new ObjectId((String) map.get("ciId"));
		ObjectId gradeId = new ObjectId((String) map.get("gradeId"));
		Integer type = Integer.valueOf((String)map.get("type"));
		boolean flag = (Boolean) map.get("flag");
		List<String> subIds = (List<String>) map.get("subIds");

		//类型为4的时候给设置互斥学科的
		List<String> subId = (List<String>) map.get("mutexSubject");

		Integer amCount = null;
		Integer pmCount = null;
		if(type == 3){
			amCount = Integer.valueOf((String)map.get("amCount"));
			pmCount = Integer.valueOf((String)map.get("pmCount"));
		}
		ObjectId subId1 = null;
		ObjectId subId2 = null;
		//如果是删除某一个学科互斥选项，则存在deleteId
		ObjectId deleteId = null;
		if(type == 4 && flag == true && map.get("deleteId") == null){
			subId1 = new ObjectId(subId.get(0));
			subId2 = new ObjectId(subId.get(1));
		}
		if(map.get("deleteId") != null){
			deleteId = new ObjectId((String) map.get("deleteId"));
		}
		N33_SubjectSetEntry subjectSetEntry = null;
		if(type != 3){
			subjectSetEntry = subjectSetDao.findN33_SubjectSetEntry(sid,ciId,gradeId,type);
		}else{
			subjectSetEntry = subjectSetDao.findN33_SubjectSetEntryByKS(sid,ciId,gradeId,type,amCount,pmCount);
		}
		if(subjectSetEntry != null){
			subjectSetEntry.setFlag(flag);
			if(type == 3) {
				subjectSetEntry.setAMCount(amCount);
				subjectSetEntry.setPMCount(pmCount);
			}else if(type == 4){
				if(deleteId != null){
					List<N33_SubjectSetEntry.MutexSubject> list = subjectSetEntry.getMutexSubjects();
					List<N33_SubjectSetEntry.MutexSubject> newList = new ArrayList<N33_SubjectSetEntry.MutexSubject>();
					for (N33_SubjectSetEntry.MutexSubject mutexSubject : list) {
						if(!mutexSubject.getID().equals(deleteId)){
							newList.add(mutexSubject);
						}
					}
					subjectSetEntry.setMutexSubjects(newList);
				}else{
					if(flag == false){

					}else{
						List<N33_SubjectSetEntry.MutexSubject> list = subjectSetEntry.getMutexSubjects();
						N33_SubjectSetEntry.MutexSubject mutexSubject = new N33_SubjectSetEntry.MutexSubject(new ObjectId(),subId1,subId2);
						list.add(mutexSubject);
						subjectSetEntry.setMutexSubjects(list);
					}
				}
			}
			subjectSetEntry.setSubIds(MongoUtils.convertToObjectIdList(subIds));
			subjectSetDao.updateN33_SubjectSetEntry(subjectSetEntry);
		}else{
			List<N33_SubjectSetDTO.MutexSubjectDTO> newList = new ArrayList<N33_SubjectSetDTO.MutexSubjectDTO>();
			if(type == 4){
				N33_SubjectSetDTO.MutexSubjectDTO dto = new N33_SubjectSetDTO.MutexSubjectDTO();
				dto.setSubjectId1(subId1.toString());
				dto.setSubjectId2(subId2.toString());
				dto.setId(new ObjectId().toString());
				newList.add(dto);
			}
			N33_SubjectSetDTO subjectSetDTO = new N33_SubjectSetDTO(sid.toString(), ciId.toString(), type, gradeId.toString(),amCount, pmCount,flag,subIds,newList);
			subjectSetEntry = subjectSetDTO.buildEntry();
			subjectSetDao.addN33_SubjectSetEntry(subjectSetEntry);
		}
	}

	/**
	 * 查找年级中所有的学科设置规则
	 * @param sid
	 * @param gradeId
	 * @param ciId
	 * @return
	 */
	public List<N33_SubjectSetDTO> getSubjectSetDTOByCiIdAndGradeId(ObjectId sid,ObjectId gradeId,ObjectId ciId){
		List<N33_SubjectSetDTO> subjectSetDTOS = new ArrayList<N33_SubjectSetDTO>();
		List<N33_SubjectSetEntry> subjectSetEntries = subjectSetDao.findN33_SubjectSetEntryByGradeId(sid, ciId, gradeId);
		Map<ObjectId,N33_KSEntry> ksEntryMap = subjectDao.findSubjectsByForMap(ciId,sid);
		for (N33_SubjectSetEntry subjectSetEntry : subjectSetEntries) {
			if(subjectSetEntry.getType() == 4){
				N33_SubjectSetDTO dto = new N33_SubjectSetDTO(subjectSetEntry);
				List<N33_SubjectSetDTO.MutexSubjectDTO> mutexSubjectDTOS = dto.getMutexSubjectDTOS();
				for (N33_SubjectSetDTO.MutexSubjectDTO mutexSubjectDTO : mutexSubjectDTOS) {
					mutexSubjectDTO.setSubName1(ksEntryMap.get(new ObjectId(mutexSubjectDTO.getSubjectId1())).getSubjectName());
					mutexSubjectDTO.setSubName2(ksEntryMap.get(new ObjectId(mutexSubjectDTO.getSubjectId2())).getSubjectName());
				}
				subjectSetDTOS.add(dto);
			}else{
				subjectSetDTOS.add(new N33_SubjectSetDTO(subjectSetEntry));
			}

		}
		return subjectSetDTOS;
	}
}
