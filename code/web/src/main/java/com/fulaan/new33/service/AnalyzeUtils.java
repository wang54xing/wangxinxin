package com.fulaan.new33.service;

import com.pojo.new33.ExamXYZLEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author rick
 *
 */
public class AnalyzeUtils {
	public void sortExamByDate2(List<ExamXYZLEntry> list) {
		if(list==null) {
			return;
		}
		Collections.sort(list, new Comparator<ExamXYZLEntry>() {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

			public int compare(ExamXYZLEntry o1, ExamXYZLEntry o2) {
				String d1 = o1.getDate();
				String d2 = o2.getDate();
				if (d1 != null && !"".equals(d1) && d2 != null && !"".equals(d2)) {
					try {
						if (sf.parse(d1).getTime() > sf.parse(d2).getTime()) {
							return -1;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if ((d1 == null || d1.equals("")) && d2 != null) {
					return 1;
				} else if ((d2 == null || d2.equals("")) && d1 != null) {
					return -1;
				}
				return 0;
			}
		});
	}
}
