package com.fulaan.new33.service.isolate;

import java.util.HashMap;
import java.util.Map;

import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.new33.isolate.N33_DefaultTermDao;
import com.db.new33.isolate.TermDao;
import com.fulaan.new33.dto.isolate.N33_DefaultTermDTO;
import com.pojo.new33.isolate.N33_DefaultTermEntry;
import com.pojo.new33.isolate.TermEntry;

@Service
public class N33_DefaultTermService extends BaseService{
	
	private N33_DefaultTermDao termDao = new N33_DefaultTermDao();
	private TermDao dao = new TermDao();

	/**
     * 查询默认学期
     * @return
     */
    public Map<String,String> getDefaultTerm(ObjectId sid){
    	N33_DefaultTermEntry termEntry = termDao.findDefaultTermEntryBySchoolId(sid);
    	Map<String, String> map = new HashMap<String, String>();
    	if(termEntry != null){
    		map.put("xqid", termEntry.getXqid().toString());
			map.put("paikexq",termEntry.getPaikeXqid().toString());
			map.put("paikeci",termEntry.getPaikeci().toString());
    	}else{
			N33_DefaultTermEntry defaultTermEntry = getDefaultPaiKeTerm(sid);
			if(defaultTermEntry!=null){
				map.put("xqid",defaultTermEntry.getXqid().toString());
				map.put("paikexq",defaultTermEntry.getPaikeXqid().toString());
				map.put("paikeci",defaultTermEntry.getPaikeci().toString());
			}
    	}
    	TermEntry term = dao.findIsolateTermEntryEntryById(new ObjectId((String)map.get("xqid")));
		TermEntry paikexq = dao.findIsolateTermEntryEntryById(new ObjectId((String)map.get("paikexq")));
		map.put("paikexqname", paikexq.getXqName());
		for(TermEntry.PaiKeTimes paiKeTime:paikexq.getPaiKeTimes()){
			if(map.get("paikeci").equals(paiKeTime.getID().toString())){
				map.put("paikeciname", paikexq.getXqName()+"第"+paiKeTime.getSerialNumber()+"次");
			}
		}
    	if(term != null){
    		map.put("xqnm", term.getXqName());
    	}
    	return map;
    }

	/**
	 * 查询默认学期(携带起始时间)
	 * @return
	 */
	public Map<String,String> getDefaultTermAndTime(ObjectId sid){
		N33_DefaultTermEntry termEntry = termDao.findDefaultTermEntryBySchoolId(sid);
		Map<String, String> map = new HashMap<String, String>();
		if(termEntry != null){
			map.put("xqid", termEntry.getXqid().toString());
			map.put("paikexq",termEntry.getPaikeXqid().toString());
			map.put("paikeci",termEntry.getPaikeci().toString());
		}else{
			N33_DefaultTermEntry defaultTermEntry = getDefaultPaiKeTerm(sid);
			if(defaultTermEntry!=null){
				map.put("xqid",defaultTermEntry.getXqid().toString());
				map.put("paikexq",defaultTermEntry.getPaikeXqid().toString());
				map.put("paikeci",defaultTermEntry.getPaikeci().toString());
			}
		}
		TermEntry term = dao.findIsolateTermEntryEntryById(new ObjectId((String)map.get("xqid")));
		TermEntry paikexq = dao.findIsolateTermEntryEntryById(new ObjectId((String)map.get("paikexq")));
		map.put("paikexqname", paikexq.getXqName());
		for(TermEntry.PaiKeTimes paiKeTime:paikexq.getPaiKeTimes()){
			if(map.get("paikeci").equals(paiKeTime.getID().toString())){
				map.put("paikeciname", paikexq.getXqName()+"第"+paiKeTime.getSerialNumber()+"次");
			}
		}
		if(term != null){
			map.put("xqnm", term.getXqName());
			String stm = "";
			if(term.getStartTime()!=null && term.getStartTime()!=0){
				stm = DateTimeUtils.getLongToStrTime(term.getStartTime(), "yyyy-MM-dd");
			}
			String etm = "";
			if(term.getEndTime()!=null && term.getEndTime()!=0){
				etm = DateTimeUtils.getLongToStrTime(term.getEndTime(), "yyyy-MM-dd");
			}
			map.put("startTime",stm);
			map.put("endTime",etm);
		}
		return map;
	}
    
    /**
     * 更改默认学期
     * @param xqid
     * @param sid
     */
    public void updateDefaultTerm(N33_DefaultTermDTO dto){
    	N33_DefaultTermEntry defaultTerm = termDao.findDefaultTermEntryBySchoolId(new ObjectId(dto.getSchoolId()));
    	if(defaultTerm == null){
    		termDao.addDefaultTerm(dto.getEntry());
    	}else{
    		termDao.updateDefaultTerm(dto.getEntry());
    	}
    }

    public void updatePaiKeTerm(ObjectId schoolId,ObjectId paikeTermId,ObjectId paikeciId){
		N33_DefaultTermEntry defaultTerm = termDao.findDefaultTermEntryBySchoolId(schoolId);
		if(defaultTerm == null){
			N33_DefaultTermEntry entry = new N33_DefaultTermEntry();
			entry.setSid(schoolId);
			entry.setPaikeXqid(paikeTermId);
			entry.setPaikeci(paikeciId);
			termDao.addDefaultTerm(entry);
		}else{
			termDao.updatePaikeTerm(defaultTerm.getID(),paikeTermId,paikeciId);
		}
	}
}
