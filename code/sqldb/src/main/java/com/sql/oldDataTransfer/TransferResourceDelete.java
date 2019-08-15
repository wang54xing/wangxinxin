package com.sql.oldDataTransfer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.factory.MongoFacroty;
import com.db.resources.ResourceDao;
import com.mongodb.BasicDBObject;
import com.pojo.resources.ResourceEntry;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.QiniuFileUtils;
//1246
public class TransferResourceDelete {

	
	
	
	
	public static void main2(String[] args) throws ResultTooManyException, IllegalParamException, InterruptedException {
		
		ObjectId id =new ObjectId("5641a7c363e763b4cd043401");
		
		
		ResourceDao resourceDao =new ResourceDao();
		List<ResourceEntry> list=new ArrayList<ResourceEntry>();
		System.out.println(list.size());
		for(ResourceEntry e:list)
		{
			
			
			if(e.getID().getTime()>id.getTime())
			{
				
				
				File f=new File("D:\\resourceSwf\\"+e.getID()+".doc");
				File f1=new File("D:\\resourceSwf\\"+e.getID()+".ppt");
				File f2=new File("D:\\resourceSwf\\"+e.getID()+".pptx");
				File f3=new File("D:\\resourceSwf\\"+e.getID()+".swf");
				
				f.delete();
				f1.delete();
				f2.delete();
				f3.delete();
				
				resourceDao.remove(e.getID());
				
				try
				{
				  QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getID()+".doc");
				  
				}catch(Exception ex)
				{
					
				}
				
				
				try
				{
				  QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getID()+".docx");
				}catch(Exception ex)
				{
					
				}
				
				
				try
				{
				  QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getID()+".ppt");
				}catch(Exception ex)
				{
					
				}
				
				try
				{
				  QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getID()+".pptx");
				}catch(Exception ex)
				{
					
				}
				
				
				
				try
				{
				  QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getID()+".pdf");
				}catch(Exception ex)
				{
					
				}
			}
			
			Thread.sleep(500);
			
		}
	}
	
}
