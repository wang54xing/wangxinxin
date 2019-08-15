package com.fulaan.new33.service.isolate;

import com.db.new33.isolate.GradeDao;
import com.db.new33.paike.N33_TurnOffDao;
import com.pojo.new33.isolate.Grade;
import com.pojo.new33.paike.N33_TurnOff;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class N33_TurnOffService extends BaseService{
	N33_TurnOffDao turnOffDao = new N33_TurnOffDao();

	private GradeDao gradeDao = new GradeDao();

	public void updateTurnOff(ObjectId gradeId,ObjectId ciId,ObjectId sid,Integer status,Integer type){
		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,ciId,type);
		if(turnOff != null){
			turnOff.setStatus(status);
			turnOff.setType(type);
			turnOffDao.updateTurnOff(turnOff);
		}else{
			N33_TurnOff turnOff1 = new N33_TurnOff(ciId, sid, gradeId, status, type);
			turnOffDao.addTurnOff(turnOff1);
		}
	}

	//修改 加入是否开启重分班状态
	public Map<String, Object> getTurnOff(ObjectId gradeId,ObjectId ciId,ObjectId sid,Integer type){
		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,ciId,type);
		Map<String,Object> retMap = new HashMap<String, Object>();
		if(turnOff != null){
			retMap.put("status",turnOff.getStatus());
			//新增
			retMap.put("acClass",turnOff.getAcClass());
		}else{
			retMap.put("status",-1);
			//新增
			retMap.put("acClass",0);
		}
		return retMap;
	}

	/**
	 *
	 * @param schoolId
	 * @param gradeId
	 * @param ciId
	 * @param type
	 * @return
	 */
	public int getAcClassType(ObjectId schoolId, ObjectId gradeId, ObjectId ciId, Integer type){
		int acClassType = 0;
		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(schoolId, gradeId, ciId, type);
		if(null!=turnOff){
			acClassType = turnOff.getAcClass();
		}
		return acClassType;
	}

	/**
	 *
	 * @param schoolId
	 * @return
	 */
	public Map<ObjectId, Integer> getGradeAcClassTypeMap(ObjectId schoolId) {
		Map<ObjectId, Integer> reMap = new HashMap<ObjectId, Integer>();
		ObjectId ciId = getDefaultPaiKeTerm(schoolId).getPaikeci();
		int type = Constant.ONE;
		List<Grade> grades = gradeDao.getIsolateGrADEEntrysByXqid(ciId, schoolId);
		Map<ObjectId, N33_TurnOff> turnOffMap = turnOffDao.getGradeTurnOffMap(schoolId, ciId, type);
		for(Grade grade:grades){
			N33_TurnOff turnOff = turnOffMap.get(grade.getGradeId());
			int acClassType = 0;
			if(null!=turnOff){
				acClassType = turnOff.getAcClass();
			}
			reMap.put(grade.getGradeId(), acClassType);
		}
		return reMap;
	}


	//新增 修改开启重分班状态（0 关闭   1 开启）
	public void updateAcClassToTurnOff(ObjectId gradeId,ObjectId ciId,ObjectId sid,Integer type,Integer acClass){
		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,ciId,type);
		if(turnOff != null){
			turnOff.setAcClass(acClass);
			turnOffDao.updateTurnOff(turnOff);
		}else{
			N33_TurnOff turnOff1 = new N33_TurnOff(ciId, sid, gradeId, -1, type);
			turnOff1.setAcClass(acClass);
			turnOffDao.addTurnOff(turnOff1);
		}
	}

	public void saveDESCForNoCourse(ObjectId gradeId,ObjectId ciId,ObjectId sid,Integer type,String desc,Integer kbType){
		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,ciId,type,kbType);
		if(turnOff != null){
			turnOff.setDesc(desc);
			turnOff.setType(type);
			turnOffDao.updateTurnOff(turnOff);
		}else{
			N33_TurnOff turnOff1 = new N33_TurnOff(ciId, sid, gradeId, type, desc,kbType);
			turnOffDao.addTurnOff(turnOff1);
		}
	}

	public String getDESCForNoCourse(ObjectId gradeId,ObjectId ciId,ObjectId sid,Integer type,Integer kbType){
		N33_TurnOff turnOff = turnOffDao.getTurnOffBySidAndCiIdAndGradeId(sid,gradeId,ciId,type,kbType);
		if(turnOff == null){
			return "";
		}else{
			return turnOff.getDesc();
		}
	}
}
