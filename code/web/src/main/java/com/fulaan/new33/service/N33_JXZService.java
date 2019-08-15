package com.fulaan.new33.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.axis2.databinding.types.soapencoding.DateTime;
import org.bson.types.ObjectId;

import com.db.new33.N33_JXZDao;
import com.db.new33.isolate.TermDao;
import com.fulaan.new33.dto.isolate.N33_JXZDTO;
import com.fulaan.new33.dto.isolate.TermDTO;
import com.fulaan.new33.service.isolate.IsolateTermService;
import com.pojo.new33.isolate.N33_JXZEntry;
import com.pojo.new33.isolate.TermEntry;
import com.sys.utils.DateTimeUtils;

public class N33_JXZService {
	
	private N33_JXZDao N33_JXZDao = new N33_JXZDao();
	private TermDao TermDao = new TermDao();
	private IsolateTermService isolateTermService = new IsolateTermService();

	
	public List<N33_JXZDTO> getListByXq(ObjectId schoolId,ObjectId xqid) throws ParseException{
		List<N33_JXZDTO> dtos = new ArrayList<N33_JXZDTO>();
		//List<N33_JXZEntry> entryList = N33_JXZDao.getListByXq(schoolId, new ObjectId(xqid));

//			generateJXZ(schoolId,xqid);
			List<N33_JXZEntry> entryList2 = N33_JXZDao.getListByXq(schoolId, xqid);
			for(N33_JXZEntry entry:entryList2) {
				dtos.add(new N33_JXZDTO(entry));
			}

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sf3 = new SimpleDateFormat(DateTimeUtils.DATE_MM);
		SimpleDateFormat sf4 = new SimpleDateFormat(DateTimeUtils.DATE_DD);
		if(dtos.size()!=0){
			Date startDate = sf.parse(dtos.get(0).getStart());
			Date endDate = sf.parse(dtos.get(dtos.size()-1).getEnd());
			int startWeekDay = DateTimeUtils.somedayIsWeekDay(startDate)==0?7:DateTimeUtils.somedayIsWeekDay(startDate);
			int endWeekDay = DateTimeUtils.somedayIsWeekDay(endDate)==0?7:DateTimeUtils.somedayIsWeekDay(endDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getFirstDayOfWeek(startDate.getTime()));
			for(int i=0;i<dtos.size();i++) {
				N33_JXZDTO jxz = dtos.get(i);
				jxz.setNumberWeek("第"+String.valueOf(jxz.getSerial())+"周");
				List<Map<String,String>> weekdays = new ArrayList<Map<String,String>>();
				if(i==0) {
					for(int j=1;j<=7;j++) {
						calendar.add(Calendar.DATE, j==1?0:1);
						Map<String,String> map= new HashMap<String, String>();
						if(j>=startWeekDay) {
							String date = "";
							String day = sf4.format(calendar.getTime());
							if (Integer.valueOf(day)==1) {
								String month = sf3.format(calendar.getTime());
								date = Integer.valueOf(month)+"月/"+Integer.valueOf(day);
							} else {
								date = Integer.valueOf(day)+"";
							}
							map.put("date", date);
							weekdays.add(map);
						}
						else {
							map.put("date", "");
							weekdays.add(map);
						}
					}

				}
				else if(i==dtos.size()-1){
					for(int j=1;j<=7;j++) {
						calendar.add(Calendar.DATE, j==1?0:1);
						Map<String,String> map= new HashMap<String, String>();
						if(j<=endWeekDay) {
							String date = "";
							String day = sf4.format(calendar.getTime());
							if (Integer.valueOf(day)==1) {
								String month = sf3.format(calendar.getTime());
								date = Integer.valueOf(month)+"月/"+Integer.valueOf(day);
							} else {
								date = Integer.valueOf(day)+"";
							}
							map.put("date", date);
							weekdays.add(map);
						}
						else {
							map.put("date", "");
							weekdays.add(map);
						}
					}
				}
				else {
					for(int j=1;j<=7;j++) {
						calendar.add(Calendar.DATE, j==1?0:1);
						Map<String,String> map= new HashMap<String, String>();
						String date = "";
						String day = sf4.format(calendar.getTime());
						if (Integer.valueOf(day)==1) {
							String month = sf3.format(calendar.getTime());
							date = Integer.valueOf(month)+"月/"+Integer.valueOf(day);
						} else {
							date = Integer.valueOf(day)+"";
						}
						map.put("date", date);
						weekdays.add(map);

					}
				}
				calendar.add(Calendar.DATE, 1);
				jxz.setWeekdays(weekdays);
			}
		}
		return dtos;
	}
	
	public void saveTerm(ObjectId schoolId,Integer year,String start,String end,String start2,String end2,String yearName) throws ParseException {
		List<TermDTO> termList = isolateTermService.getTermByYear(schoolId, year);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		if(termList!=null&&termList.size()!=0) {
			for(TermDTO dto:termList) {
				if(dto.getIr()==0) {
					dto.setXqnm(yearName+"第一学期");
					dto.setSt(sf.parse(start).getTime());
					dto.setEt(sf.parse(end).getTime());
				}
				else {
					dto.setXqnm(yearName+"第二学期");
					dto.setSt(sf.parse(start2).getTime());
					dto.setEt(sf.parse(end2).getTime());
				}
				dto.setSy(yearName);
			}
		} else {
			String tid = new ObjectId().toString();
			TermDTO term1 = new TermDTO(tid, year, yearName+"第一学期", schoolId.toString(), sf.parse(start).getTime(), sf.parse(end).getTime(), 0, "*", yearName);
			TermDTO term2 = new TermDTO(tid, year, yearName+"第二学期", schoolId.toString(), sf.parse(start2).getTime(), sf.parse(end2).getTime(), 1, "*", yearName);
			termList.add(term1);
			termList.add(term2);
			for(TermDTO dto:termList) {
				List<TermDTO.PaiKeTimeDTO> timeDTOList = new ArrayList<TermDTO.PaiKeTimeDTO>();
				TermDTO.PaiKeTimeDTO paiKeTimeDTO = new TermDTO.PaiKeTimeDTO();
				paiKeTimeDTO.setId(new ObjectId().toString());
				paiKeTimeDTO.setSerialNumber(1);
				timeDTOList.add(paiKeTimeDTO);
				dto.setTimeDTOS(timeDTOList);
			}
		}
		for(TermDTO dto:termList) {
				isolateTermService.addTerm(dto);
		}
	}
	
	
	
	/**  
     * 取得指定日期所在周的第一天  
     */   
     private  Date getFirstDayOfWeek(long date) {   
        try {  
        	java.util.Date time = new Date(date);
        	Calendar c = new GregorianCalendar();   
            c.setFirstDayOfWeek(Calendar.MONDAY);   
            c.setTime(time);   
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday   
            return c.getTime();   
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
          
          
     }  
  
     /**  
     * 取得指定日期所在周的最后一天  
     */   
     private  Date getLastDayOfWeek(long date) {   
        try {  
            java.util.Date time = new Date(date);
            Calendar c = new GregorianCalendar();   
            c.setFirstDayOfWeek(Calendar.MONDAY);   
            c.setTime(time);   
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday   
            return c.getTime();   
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
     }  
       
    private String numberToChinese(String string) {
    	String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };

        String result = "";

        int n = string.length();
        for (int i = 0; i < n; i++) {

            int num = string.charAt(i) - '0';

            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
           
        }
        return result;
    }

	/**
	 *
	 * @param schoolId
	 * @param xqid
     * @return
     */
	public List<N33_JXZDTO> getTermByXq(ObjectId schoolId, ObjectId xqid) {
		List<N33_JXZDTO> dtos = new ArrayList<N33_JXZDTO>();
		List<N33_JXZEntry> entryList = N33_JXZDao.getListByXq(schoolId, xqid);
		for(N33_JXZEntry entry:entryList) {
			N33_JXZDTO jxz = new N33_JXZDTO(entry);
			jxz.setNumberWeek("第"+String.valueOf(jxz.getSerial())+"周");
			dtos.add(jxz);
		}
		return dtos;
	}

	/**
	 *
	 * @param schoolId
	 * @param defauleTermId
     * @return
     */
	public N33_JXZDTO getCurrentJXZ(ObjectId schoolId, ObjectId defauleTermId) {
		N33_JXZDTO dto = new N33_JXZDTO();
		N33_JXZEntry entry = N33_JXZDao.getJXZByDate(schoolId,defauleTermId,System.currentTimeMillis());
		if (entry!=null) {
			dto = new N33_JXZDTO(entry);
		}
		return dto;
	}
}
