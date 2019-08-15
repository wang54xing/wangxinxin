package com.sql.oldDataTransfer;

import com.db.resources.ResourceDao;
import com.db.resources.ResourceDictionaryDao;
import com.pojo.app.FileType;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.resources.ResourceEntry;
import com.sys.utils.HttpFileConvertUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 导入资源,只包括doc,docx,ppt,pdf类型
 * @author fourer
 *
 */
public class TransferResource {
	
	public static void main(String[] args) throws Exception {
		
		ResourceDao resourceDao =new ResourceDao();
		
		ResourceDictionaryDao resourceDictionaryDao =new ResourceDictionaryDao();
	
		File f= new File("D:\\res1215\\TransferResource0902.txt");
		f.createNewFile();
		
		File dir=new File("D:\\resourceSwf0902");
		//resourceSwf
		
		
		//0307
		List<String> names=FileUtils.readLines(new File("d:\\res1215\\name3.txt"),"UTF-8");
		List<String> mulus=FileUtils.readLines(new File("d:\\res1215\\mulu3.txt"),"UTF-8");
		List<String> addresss=FileUtils.readLines(new File("d:\\res1215\\address3.txt"),"UTF-8");
		List<String> kws=FileUtils.readLines(new File("d:\\res1215\\kwlist3.txt"));
		
		
		
		if(names.size()!=mulus.size() || mulus.size()!=addresss.size()  )
		{
			System.out.println("数目错误");
			throw new Exception("data size error!!");
		}
		
		
		for(int i=370;i<names.size();i++)
		{
			System.out.println("i="+i);
			
			String name =names.get(i).trim();
			String mulu =mulus.get(i).trim();
			String address1 =addresss.get(i).trim();
			String kw=kws.get(i).trim();
			System.out.println("addresss="+address1);
			try
			{
				boolean success=true;
				String address="";	
				File res=null;
				FileInputStream s=null;
				
				try
				{
			      address="E"+address1.substring(1);
			      res=new File(address);
			      s= new FileInputStream(res);
				}catch(Exception ex)
				{
					success=false;
				}
				
				
				if(!success)
				{
					try
					{
					 address="E"+address1.substring(2);
				     res=new File(address);
				     s= new FileInputStream(res);
					}catch(Exception ex)
					{
						System.out.println("数据错误;"+mulu+","+address1);
						FileUtils.write(f, "\r\n",true);
						FileUtils.write(f, "数据错误;"+mulu+","+address1,true);
						continue;
					}
				}
				
				
				
				
		    System.out.println("文件大小："+res.length());
			String extendName=FilenameUtils.getExtension(address);
			
			FileType ft=FileType.getFileType(extendName);
			
			

			
			if(!ft.equals(FileType.DOC) && !ft.equals(FileType.DOCX) && !ft.equals(FileType.PPT)  && !ft.equals(FileType.PPTX) && !ft.equals(FileType.PDF))
			{
				System.out.println("不支持的数据格式;"+mulu+","+address1);
				FileUtils.write(f, "\r\n",true);
				FileUtils.write(f, "不支持的数据格式;"+mulu+","+address1,true);
				continue;
			}
			
			
			
			//章节
			List<ObjectId> psbList =new ArrayList<ObjectId>();
			try
			{
				String[] muluArr =mulu.split("\\|");
				
				if(muluArr.length==2)
				{
					System.out.println(mulu);
				}
				
				for(String ml:muluArr)
				{
					
					Long a=null;
					try
					{
					a=Long.valueOf(ml);
					}catch(Exception ex)
					{
						
					}
					
					if(null==a)
					{
						a=Long.valueOf(ml.substring(1));
					}
					
					ResourceDictionaryEntry rde=resourceDictionaryDao.getResourceDictionaryEntryBySort(a,6);
					
					if(null==rde)
					{
						System.out.println("章节处理错误;"+mulu+","+address1);
						FileUtils.write(f, "\r\n",true);
						FileUtils.write(f, "章节处理错误;"+mulu+","+address1,true);
						continue;		
					}
					psbList.add(rde.getID());
				}
			}catch(Exception ex)
			{
				System.out.println("章节处理错误;"+mulu+","+address1);
				FileUtils.write(f, "\r\n",true);
				FileUtils.write(f, "章节处理错误;"+mulu+","+address1,true);
				continue;
			}
			
		
			//知识点
			List<ObjectId> kwList =new ArrayList<ObjectId>();
			if(StringUtils.isNotBlank(kw))
			{
				try
				{
					String[] kwArr =kw.split("\\|");
					
					if(kwArr.length==2)
					{
						System.out.println(kw);
					}
					
					for(String kwStr:kwArr)
					{
						ResourceDictionaryEntry rde=resourceDictionaryDao.getResourceDictionaryEntryBySort(Long.valueOf(kwStr),8);
						
						if(null==rde)
						{
							System.out.println("知识点处理错误;"+mulu+","+address1);
							FileUtils.write(f, "\r\n",true);
							FileUtils.write(f, "知识点处理错误;"+mulu+","+address1,true);
							continue;		
						}
						kwList.add(rde.getID());
					}
				}catch(Exception ex)
				{
					System.out.println("知识点处理错误;"+mulu+","+address1);
					FileUtils.write(f, "\r\n",true);
					FileUtils.write(f, "知识点处理错误;"+mulu+","+address1,true);
					continue;
				}
			}
			
			ObjectId fileKey =new ObjectId();
			
			
			//去除文件名的空格
			if(null!=res)
			{
				String name2=	fileKey+"."+FilenameUtils.getExtension(address);
				File newFile =new File(dir,name2);
				FileUtils.copyFile(res, newFile);
			}
			
			
			File swfFile=new File(dir, fileKey+".swf");
			swfFile.createNewFile();
			File pdfFile =new File(dir, fileKey.toString()+".pdf");
			//在本地生成swf
			if(ft.equals(FileType.DOC) || ft.equals(FileType.DOCX))
			{
		 	    HttpFileConvertUtils.word2PdfAndSwf(res, swfFile, pdfFile);
			}
			
			if(ft.equals(FileType.PDF) )
			{
				 HttpFileConvertUtils.convertPdfToSwf(res,swfFile.getAbsolutePath());
			}
			
			if(ft.equals(FileType.PPT) || ft.equals(FileType.PPTX))
		    {
				HttpFileConvertUtils.convertPPToPdf(res, pdfFile.getAbsolutePath());
				HttpFileConvertUtils.convertPdfToSwf(pdfFile,swfFile.getAbsolutePath());
		    }
			
			  ResourceEntry e =new ResourceEntry(ft.getType(), name, res.length(), "", fileKey+"."+extendName, null, null, kwList, psbList,0,1,0,null,new ArrayList<ObjectId>(),"资源板块后端");
			  e.setID(fileKey);
			  resourceDao.addResource(e);
			  FileUtils.write(f, "\r\n",true);
			  FileUtils.write(f, i+"="+fileKey.toString(),true);
			  
			  Thread.sleep(500);
			}catch(Exception ex)
			{
				FileUtils.write(f, "\r\n",true);
				System.out.println("导入出错;"+mulu+","+address1);
				FileUtils.write(f, mulu+","+address1,true);
			}
			
		}
	}
	
	
	
	
	

}
