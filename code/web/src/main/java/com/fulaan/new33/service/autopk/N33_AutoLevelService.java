package com.fulaan.new33.service.autopk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.new33.autopk.N33_AutoTeaSetDao;
import com.db.new33.autopk.N33_SubjectSetDao;
import com.db.new33.autopk.N33_TeaMutexDao;
import com.db.new33.autopk.N33_ZouBanSetDao;
import com.fulaan.new33.service.N33_AutoFenBanService;
import com.fulaan.new33.service.isolate.BaseService;
import com.pojo.autoPK.N33_SubjectSetEntry;
import com.pojo.autoPK.N33_ZouBanSetEntry;
import com.pojo.autoPK.TeaMutexEntry;
import com.pojo.autoPK.N33_SubjectSetEntry.MutexSubject;

/**
*自动排课-排课规则-教师设置
*/
@Service
public class N33_AutoLevelService extends BaseService {

	private N33_AutoTeaSetDao autoTeaSetDao = new N33_AutoTeaSetDao();
	
	@Autowired
    private N33_AutoFenBanService fenBanService;
	
	private N33_TeaMutexDao teaMutexDao = new N33_TeaMutexDao();
	
	private N33_ZouBanSetDao zouBanSetDao = new N33_ZouBanSetDao();
	
	private N33_SubjectSetDao subjectSetDao = new N33_SubjectSetDao();
	
	/**
     * 设置自动标签
     */
    public void autoLabelLevel(Map repMap) {
    	ObjectId schoolId = new ObjectId(repMap.get("schoolId").toString());
    	ObjectId gradeId = new ObjectId(repMap.get("gradeId").toString());
    	ObjectId ciId = new ObjectId(repMap.get("ciId").toString());
    	//首先判断此次排课是否已经发布，已经发布则不允许修改数据
    	//创建自动标签
    	fenBanService.autoFenBanPingTag(schoolId,ciId, gradeId);
    	//查询排课规则
    	//学科互斥
    	List<MutexSubject> mutexSubList =new ArrayList<MutexSubject>();
    	N33_SubjectSetEntry subjectSetEntry = subjectSetDao.findN33_SubjectSetEntry(schoolId,ciId,gradeId,4);
    	if(subjectSetEntry != null && subjectSetEntry.getFlag())
    		mutexSubList = subjectSetEntry.getMutexSubjects();
    	//教师互斥
    	List<TeaMutexEntry> teaMutexs = new ArrayList<TeaMutexEntry>();
    	teaMutexs = teaMutexDao.getEntrys(schoolId, gradeId, ciId);
    	//走班教室数量
    	int roomNum = -1;
    	N33_ZouBanSetEntry zouBanSetEntry = zouBanSetDao.findN33_ZouBanSetEntry(schoolId,ciId,gradeId,2);
    	if(zouBanSetEntry != null && zouBanSetEntry.getFlag())
    		roomNum = zouBanSetEntry.getCount();
    	//教学班人数上限
    	int jxbLimit = -1;
    	zouBanSetEntry = zouBanSetDao.findN33_ZouBanSetEntry(schoolId,ciId,gradeId,3);
    	if(zouBanSetEntry != null && zouBanSetEntry.getFlag())
    		jxbLimit = zouBanSetEntry.getCount();
    	//走班教室上下浮动人数上限 
    	int roomLimit = -1;
    	zouBanSetEntry = zouBanSetDao.findN33_ZouBanSetEntry(schoolId,ciId,gradeId,4);
    	if(zouBanSetEntry != null && zouBanSetEntry.getFlag())
    		roomLimit = zouBanSetEntry.getCount();
    	//等级-时段组合 to do
    	
    	//合格-时段组合 to do
    	
    }
	
    
}
