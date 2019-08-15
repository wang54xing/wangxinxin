package com.pojo.log;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.pojo.app.IdNameValuePairDTO;

/**
 * 登录统计DTO
 * @author fourer
 *
 */
public class SimpleLogLoginStatDTO {

   static	NumberFormat numberFormat = NumberFormat.getInstance();
   
   static
   {
	   numberFormat.setMaximumFractionDigits(0);
   }
	
	private int teacherCount;
	private int studentCount;
	private int parentCount;
    private List<IdNameValuePairDTO> statList =new ArrayList<IdNameValuePairDTO>();
    
    
    
    
	public SimpleLogLoginStatDTO(int teacherCount, int studentCount,
			int parentCount, List<IdNameValuePairDTO> statList) {
		super();
		this.teacherCount = teacherCount;
		this.studentCount = studentCount;
		this.parentCount = parentCount;
		this.statList = statList;
	}
	
	
	public int getTeacherCount() {
		return teacherCount;
	}
	public void setTeacherCount(int teacherCount) {
		this.teacherCount = teacherCount;
	}
	public int getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}
	public int getParentCount() {
		return parentCount;
	}
	public void setParentCount(int parentCount) {
		this.parentCount = parentCount;
	}
	public List<IdNameValuePairDTO> getStatList() {
		return statList;
	}
	public void setStatList(List<IdNameValuePairDTO> statList) {
		this.statList = statList;
	}
    
    
    
	private String teacherPercent;
	private String studentPercent;
	private String parentPercnt;




	public String getTeacherPercent() {
		try
		{
			int total=this.teacherCount+this.parentCount+this.studentCount;
			double p=this.teacherCount/Double.parseDouble(String.valueOf(total));
			return numberFormat.format(p* 100);
		}catch(Exception ex)
		{
		}
		return "#";
	}


	public void setTeacherPercent(String teacherPercent) {
		
	}


	public String getStudentPercent() {
		try
		{
		int total=this.teacherCount+this.parentCount+this.studentCount;
		double p=this.studentCount/Double.parseDouble(String.valueOf(total));
		return numberFormat.format(p* 100);
		}catch(Exception ex)
		{
			
		}
		return "#";
		
	}


	public void setStudentPercent(String studentPercent) {
		
	
	}


	public String getParentPercnt() {
		try
		{
			int teacherP=Integer.valueOf(getTeacherPercent());
			int studentP=Integer.valueOf(getStudentPercent());
			int parentP=100-teacherP-studentP;
			return String.valueOf(parentP);
		}catch(Exception ex)
		{
			
		}
		return "#";
		
	}


	public void setParentPercnt(String parentPercnt) {
		
	}
	
	
    
    
}
