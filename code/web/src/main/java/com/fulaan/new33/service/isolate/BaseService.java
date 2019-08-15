package com.fulaan.new33.service.isolate;

import java.util.List;

import com.db.new33.paike.N33_FaBuDao;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.db.new33.isolate.N33_DefaultTermDao;
import com.db.new33.isolate.TermDao;
import com.db.new33.paike.N33_SWDao;
import com.pojo.new33.isolate.N33_DefaultTermEntry;
import com.pojo.new33.isolate.TermEntry;
import org.springframework.stereotype.Service;

public class BaseService {

	private TermDao termDao = new TermDao();
	private N33_DefaultTermDao defaultTermDao = new N33_DefaultTermDao();
	private N33_FaBuDao faBuDao=new N33_FaBuDao();
	
	/**
	 * 获取默认的学期id
	 * @param schoolId
	 * @return
	 */
	public final ObjectId getDefauleTermId(ObjectId schoolId){
		N33_DefaultTermEntry defaultTerm = defaultTermDao.findDefaultTermEntryBySchoolId(schoolId);
		if(defaultTerm == null){
			long current = System.currentTimeMillis();
			TermEntry entry = termDao.getTermByTime(schoolId, current);
			if(entry == null) {
				List<TermEntry> list = termDao.getIsolateTermEntrysBySid(schoolId);
				if(list.size() > 1){
					long min = Math.min(Math.abs(list.get(0).getStartTime()-current), Math.abs(list.get(0).getEndTime()-current));
					for (TermEntry termEntry : list) {
						if(min > Math.min(Math.abs(termEntry.getStartTime()-current), Math.abs(termEntry.getEndTime()-current))){
							min = Math.min(Math.abs(termEntry.getStartTime()-current), Math.abs(termEntry.getEndTime()-current));
							entry = termEntry;
						}
					}
				}else if (list.size() == 1){
					entry = list.get(0);
				}
			}
			if(entry != null){
				N33_DefaultTermEntry defaultTermEntry = new N33_DefaultTermEntry();
				defaultTermEntry.setXqid(entry.getID());
				defaultTermEntry.setSid(schoolId);
				defaultTermEntry.setPaikeXqid(entry.getID());
				defaultTermEntry.setPaikeci(entry.getPaiKeTimes().get(0).getID());
				defaultTerm = defaultTermEntry;
				defaultTermDao.addDefaultTerm(defaultTerm);
			}
		}
		if(defaultTerm!= null){
			return defaultTerm.getXqid();
		}else{
			return null;
		}
	}

	public final N33_DefaultTermEntry getDefaultPaiKeTerm(ObjectId schoolId){
		N33_DefaultTermEntry defaultTerm = defaultTermDao.findDefaultTermEntryBySchoolId(schoolId);
		if(defaultTerm == null){
			long current = System.currentTimeMillis();
			TermEntry entry = termDao.getTermByTime(schoolId, current);
			if(entry == null) {
				List<TermEntry> list = termDao.getIsolateTermEntrysBySid(schoolId);
				if(list.size() > 1){
					long min = Math.min(Math.abs(list.get(0).getStartTime()-current), Math.abs(list.get(0).getEndTime()-current));
					entry = list.get(0);
					for (TermEntry termEntry : list) {
						if(min > Math.min(Math.abs(termEntry.getStartTime()-current), Math.abs(termEntry.getEndTime()-current))){
							min = Math.min(Math.abs(termEntry.getStartTime()-current), Math.abs(termEntry.getEndTime()-current));
							entry = termEntry;
						}
					}
				}else if (list.size() == 1){
					entry = list.get(0);
				}
			}
			if(entry != null){
				N33_DefaultTermEntry defaultTermEntry = new N33_DefaultTermEntry();
				defaultTermEntry.setXqid(entry.getID());
				defaultTermEntry.setSid(schoolId);
				defaultTermEntry.setPaikeXqid(entry.getID());
				defaultTermEntry.setPaikeci(entry.getPaiKeTimes().get(0).getID());
				defaultTerm = defaultTermEntry;
				defaultTermDao.addDefaultTerm(defaultTerm);
			}
		}
		return defaultTerm;
	}

	public Boolean getCiIdIsFaBu(ObjectId ciId){
		return faBuDao.getN33_FaBuEntryByCiId(ciId)!=null;
	}

	public Boolean getXqIdIsFaBu(ObjectId xqid){
		return faBuDao.getN33_FaBuEntryByXqId(xqid)!=null;
	}
}
