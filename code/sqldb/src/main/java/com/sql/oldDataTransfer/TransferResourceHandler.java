package com.sql.oldDataTransfer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.resources.ResourceDao;
import com.pojo.resources.ResourceEntry;


public class TransferResourceHandler {
	
	public static void main(String[] args) throws IOException {
		File f= new File("D:\\res1215\\TransferResource0523.txt");
		List<String> list=FileUtils.readLines(f, "utf-8");
		
		Map<ObjectId, Integer> map =new HashMap<ObjectId, Integer>();
		for(String s:list)
		{
			if(StringUtils.isNotBlank(s))
			{
				int index=s.indexOf("=");
				if(index>0)
				{
					try
					{
						  String oidStr=s.substring(index+1);
						  String intStr=s.substring(0, index);
						  map.put(new ObjectId(oidStr), Integer.valueOf(intStr));
					}catch(Exception ex)
					{
						
					}
				}
			}
		}
		
		ResourceDao resourceDao =new ResourceDao();
		List<ResourceEntry> dbolist=resourceDao.getResourceEntryList(1,1000);
		
		
		Set<Integer> successList =new HashSet<Integer>();
		
		for(ResourceEntry re:dbolist)
		{
			if(map.containsKey(re.getID()))
			{
				successList.add(map.get(re.getID()));
			}
			else
			{
				System.out.println("没有找到对应的编号"+re.getID());
			}
		}
		
		
		for(int i=0;i<882;i++)
		{
			if(!successList.contains(i))
			{
				System.out.println(i);
			}
		}
		
		
	}
	
	
	
	
	public static void main1(String[] args)  {
		
		ResourceDao resourceDao =new ResourceDao();
		List<ResourceEntry> list=resourceDao.getResourceEntryList(1,1000);
		List<ObjectId> delList =new ArrayList<ObjectId>();
		for(ResourceEntry re:list)
		{
			ObjectId id =re.getID();
			
			
			boolean pptExists=false;
			boolean pdfExists=false;
			boolean swfExists=false;
			
			
			
			//ppt
			try
			{
				File docx =new File("D:\\resourceSwf0523\\"+id.toString()+".ppt");
				
				if(docx.exists() && docx.length()>1024)
				{
				  File todocx =new File("D:\\resourceSwf0527\\"+id.toString()+".ppt");
				  FileUtils.copyFile(docx, todocx);
				  pptExists=true;
				}
			}catch(Exception ex)
			{
				
			}
			
			
			
			//pdf
			try
			{
				File docx =new File("D:\\resourceSwf0523\\"+id.toString()+".pdf");
				if(docx.exists() && docx.length()>1024)
				{
				  File todocx =new File("D:\\resourceSwf0527\\"+id.toString()+".pdf");
				  FileUtils.copyFile(docx, todocx);
				  pdfExists=true;
				}
			}catch(Exception ex)
			{
				
			}
			
			
			
			//swf
			try
			{
				File docx =new File("D:\\resourceSwf0523\\"+id.toString()+".swf");
				if(docx.exists() && docx.length()>1024)
				{
				File todocx =new File("D:\\resourceSwf0527\\"+id.toString()+".swf");
				FileUtils.copyFile(docx, todocx);
				swfExists=true;
				}
			}catch(Exception ex)
			{
				
			}
			
			
			
			if(!pptExists || !pdfExists || !swfExists)
			{
				delList.add(id);
				
				File docx =new File("D:\\resourceSwf0527\\"+id.toString()+".ppt");
				File docx1 =new File("D:\\resourceSwf0527\\"+id.toString()+".pdf");
				File docx2 =new File("D:\\resourceSwf0527\\"+id.toString()+".swf");
				
				docx.delete();
				docx1.delete();
				docx2.delete();
				
			}
			
		}
		
		System.out.println(delList.size());
		
		for(ObjectId id:delList)
		{
			System.out.println(id);
			resourceDao.remove(id);
		}
	}
}
