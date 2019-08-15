package com.fulaan.new33.service.autopk;

import com.db.new33.autopk.N33_ZouBanSetDao;
import com.fulaan.new33.dto.autopk.N33_ZouBanSetDTO;
import com.fulaan.new33.service.isolate.BaseService;
import com.pojo.autoPK.N33_ZouBanSetEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class N33_ZouBanSetService extends BaseService {
	private N33_ZouBanSetDao zouBanSetDao = new N33_ZouBanSetDao();

	/**
	 * 勾选和修改自动排课规则走班设置
	 * @param flag
	 * @param gradeId
	 * @param sid
	 * @param ciId
	 * @param type
	 * @param count
	 */
	public void addZouBanSet(boolean flag, ObjectId gradeId,ObjectId sid,ObjectId ciId,Integer type,Integer count){
		N33_ZouBanSetEntry zouBanSetEntry = zouBanSetDao.findN33_ZouBanSetEntry(sid,ciId,gradeId,type);
		if(zouBanSetEntry != null){
			if(flag == true){
				zouBanSetEntry.setCount(count);
			}
			zouBanSetEntry.setFlag(flag);
			zouBanSetDao.updateN33_ZouBanSetEntry(zouBanSetEntry);
		}else{
			N33_ZouBanSetDTO zouBanSetDTO = new N33_ZouBanSetDTO(gradeId.toString(),sid.toString(), ciId.toString(),type, count,flag);
			zouBanSetEntry = zouBanSetDTO.buildEntry();
			zouBanSetDao.addN33_ZouBanSetEntry(zouBanSetEntry);
		}
	}

	/**
	 * 查询走班设置
	 * @param gradeId
	 * @param sid
	 * @param ciId
	 */
	public List<N33_ZouBanSetDTO> getZouBanSet(ObjectId gradeId,ObjectId sid,ObjectId ciId){
		List<N33_ZouBanSetDTO> zouBanSetDTOList = new ArrayList<N33_ZouBanSetDTO>();
		List<N33_ZouBanSetEntry> zouBanSetEntries = zouBanSetDao.findN33_ZouBanSetEntryByGradeId(sid,ciId,gradeId);
		for (N33_ZouBanSetEntry zouBanSetEntry : zouBanSetEntries) {
			zouBanSetDTOList.add(new N33_ZouBanSetDTO(zouBanSetEntry));
		}
		return zouBanSetDTOList;
	}

	/**
	 * 查询走班设置
	 * @param gradeId
	 * @param sid
	 * @param ciId
	 */
	public Map<Integer, N33_ZouBanSetDTO> getZouBanSetMap(ObjectId gradeId, ObjectId sid, ObjectId ciId){
		Map<Integer, N33_ZouBanSetDTO> reMap = new HashMap<Integer, N33_ZouBanSetDTO>();
		List<N33_ZouBanSetEntry> zouBanSetEntries = zouBanSetDao.findN33_ZouBanSetEntryByGradeId(sid,ciId,gradeId);
		for (N33_ZouBanSetEntry zouBanSetEntry : zouBanSetEntries) {
			N33_ZouBanSetDTO setDto = new N33_ZouBanSetDTO(zouBanSetEntry);
			reMap.put(setDto.getType(), setDto);
		}
		return reMap;
	}
}
