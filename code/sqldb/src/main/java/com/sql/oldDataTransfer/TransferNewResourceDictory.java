package com.sql.oldDataTransfer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.resources.ResourceDictionaryDao;
import com.pojo.app.IdValuePair;
import com.pojo.resources.ResourceDictionaryEntry;

/**
 * 导入新增加的目录
 * 
 * @author fourer
 *
 */
public class TransferNewResourceDictory {

	//private static IdValuePair xueduan = new IdValuePair(new ObjectId(
		//	"55d41e47e0b064452581269e"), "高中");
	private static Map<String, String> muluNameMap = new HashMap<String, String>();
	private static Map<String, ResourceDictionaryEntry> muluObjectMap = new HashMap<String, ResourceDictionaryEntry>();

	private static final ResourceDictionaryDao dao = new ResourceDictionaryDao();
	
	

	public static void main3(String[] args) throws Exception{
		
		File file =new File("E:\\newDict\\dict.txt");
		file.createNewFile();
		List<String> mulu = FileUtils.readLines(new File("E:\\newDict\\mulu.txt"),"UTF-8");
		List<String> name = FileUtils.readLines(new File("E:\\newDict\\name.txt"),"UTF-8");
		
		
		if (mulu.size() != name.size()) {
			throw new Exception("数目不对");
		}
		
		String muluStr="";
		String muluName="";
		
        //12
// 1学段 ->2学科-> 3版本 ->4年级-> 5单元-> 6课 
		for(int i=0;i<mulu.size();i++)
		{
			muluStr=mulu.get(i);
			muluName=name.get(i);
			if(muluStr.length()==9)
			{
				ResourceDictionaryEntry p=getParent(3,muluStr.substring(0, 6) );
				if(null==p)
				{
					throw new Exception("mei zhao dao  fulei ");
				}
				
				ResourceDictionaryEntry e =new ResourceDictionaryEntry(4, muluName, p.getID(), getParentList(3, muluStr.substring(0, 6)       ));
				e.setSort(Long.valueOf(muluStr));
				dao.addResourceDictionaryEntry(e);
			}
		}
		
		                         //12
	// 1学段 ->2学科-> 3版本 ->4年级-> 5单元-> 6课 
		for(int i=0;i<mulu.size();i++)
		{
			muluStr=mulu.get(i);
			muluName=name.get(i);
			if(muluStr.length()==12)
			{
			
				{
					ResourceDictionaryEntry p=getParent(4,muluStr.substring(0, 9) );
					if(null==p)
					{
						throw new Exception("mei zhao dao  fulei ");
					}
					ResourceDictionaryEntry e =new ResourceDictionaryEntry(5, muluName, p.getID(), getParentList(4,muluStr.substring(0, 9)));
					e.setSort(Long.valueOf(muluStr));
					dao.addResourceDictionaryEntry(e);
				}
			}
		}
		
		for(int i=0;i<mulu.size();i++)
		{
			muluStr=mulu.get(i);
			muluName=name.get(i);
			if(muluStr.length()==15)
			{
				{
					ResourceDictionaryEntry p=getParent(5,muluStr.substring(0, 12) );
					if(null==p)
					{
						System.out.println(muluStr);
						//continue;
						throw new Exception("mei zhao dao  fulei ");
					}
					ResourceDictionaryEntry e =new ResourceDictionaryEntry(6, muluName, p.getID(), getParentList(5,muluStr.substring(0, 12)));
					e.setSort(Long.valueOf(muluStr));
					dao.addResourceDictionaryEntry(e);
				}
			}
		}
		
	}
	
	
	
	
	public static void main(String[] args) throws Exception {

		File file =new File("E:\\newDict\\dict_0307.txt");
		file.createNewFile();
		List<String> mulu = FileUtils.readLines(new File("E:\\newDict\\mulu0307.txt"),"UTF-8");
		List<String> name = FileUtils.readLines(new File("E:\\newDict\\name0307.txt"),"UTF-8");
		ResourceDictionaryDao dao = new ResourceDictionaryDao();

		if (mulu.size() != name.size()) {
			throw new Exception("数目不对");
		}

		for (int i = 0; i < mulu.size(); i++) {
			
			if(!muluNameMap.containsKey(mulu.get(i).trim()))
			{
			  muluNameMap.put(mulu.get(i).trim(), name.get(i).trim());
			}
			else
			{
				System.out.println(mulu.get(i).trim());
			}
		}

		if (muluNameMap.size() != mulu.size()) {
			//throw new Exception("有重复");
		}
        //章节： 1学段 ->2学科-> 3版本 ->4年级-> 5单元-> 6课
		// 学科
		for (Map.Entry<String, String> entry : muluNameMap.entrySet()) {
			if (entry.getKey().length() == 3)// 学科
			{
				
				IdValuePair xueduan=getXueduan(entry.getKey());
				
				ObjectId id = new ObjectId();
				ResourceDictionaryEntry e = new ResourceDictionaryEntry(2,
						entry.getValue(), xueduan.getId(),
						getParentList(1, entry.getKey().substring(0, 1))
						);
				e.setSort(Long.valueOf(entry.getKey()));
				e.setID(id);
				dao.addResourceDictionaryEntry(e);
				muluObjectMap.put(entry.getKey(), e);
				
				FileUtils.write(file, "\r\n",true);
				FileUtils.write(file, entry.getKey()+","+id.toString(),true);
			}
		}
		 //章节： 1学段 ->2学科-> 3版本 ->4年级-> 5单元-> 6课
		// 版本
		for (Map.Entry<String, String> entry : muluNameMap.entrySet()) {
			if (entry.getKey().length() == 6)// 版本
			{
				ObjectId id = new ObjectId();
				ResourceDictionaryEntry e = new ResourceDictionaryEntry(3,
						entry.getValue(), getParentId(entry.getKey().substring(0, 3),2),
						getParentList(2, entry.getKey().substring(0, 3))
						
						
						
						);
				e.setSort(Long.valueOf(entry.getKey()));
				e.setID(id);
				dao.addResourceDictionaryEntry(e);
				muluObjectMap.put(entry.getKey(), e);
				
				
				FileUtils.write(file, "\r\n",true);
				FileUtils.write(file, entry.getKey()+","+id.toString(),true);
			}
		}
		 //章节： 1学段 ->2学科-> 3版本 ->4年级-> 5单元-> 6课
		// 年级
		for (Map.Entry<String, String> entry : muluNameMap.entrySet()) {
			if (entry.getKey().length() == 9)// 年级
			{
				ObjectId id = new ObjectId();
				ResourceDictionaryEntry e = new ResourceDictionaryEntry(4,
						entry.getValue(), getParentId(entry.getKey().substring(0, 6),3),
						getParentList(3, entry.getKey().substring(0, 6))
						
						);
				e.setSort(Long.valueOf(entry.getKey()));
				e.setID(id);
				dao.addResourceDictionaryEntry(e);
				muluObjectMap.put(entry.getKey(), e);
				
				
				FileUtils.write(file, "\r\n",true);
				FileUtils.write(file, entry.getKey()+","+id.toString(),true);
			}
		}
		//章节： 1学段 ->2学科-> 3版本 ->4年级-> 5单元-> 6课
		// 单元
		for (Map.Entry<String, String> entry : muluNameMap.entrySet()) {
			if (entry.getKey().length() == 12)// 单元
			{
				ObjectId id = new ObjectId();
				ResourceDictionaryEntry e = new ResourceDictionaryEntry(5,
						entry.getValue(), getParentId(entry.getKey().substring(0, 9),4),
						getParentList(4, entry.getKey().substring(0, 9))
						
						);
				e.setSort(Long.valueOf(entry.getKey()));
				e.setID(id);
				dao.addResourceDictionaryEntry(e);
				muluObjectMap.put(entry.getKey(), e);
				
				FileUtils.write(file, "\r\n",true);
				FileUtils.write(file, entry.getKey()+","+id.toString(),true);
			}
		}
		//章节： 1学段 ->2学科-> 3版本 ->4年级-> 5单元-> 6课
		// 课
		for (Map.Entry<String, String> entry : muluNameMap.entrySet()) {
			if (entry.getKey().length() == 15)// 课
			{
				ObjectId id = new ObjectId();
				ResourceDictionaryEntry e = new ResourceDictionaryEntry(6,
						entry.getValue(), getParentId(entry.getKey().substring(0, 12),5),
						getParentList(5, entry.getKey().substring(0, 12))
						);
				e.setSort(Long.valueOf(entry.getKey()));
				e.setID(id);
				dao.addResourceDictionaryEntry(e);
				muluObjectMap.put(entry.getKey(), e);
				
				FileUtils.write(file, "\r\n",true);
				FileUtils.write(file, entry.getKey()+","+id.toString(),true);
			}
		}

	}

	private static List<IdValuePair> getParentList(String mulu)
			throws Exception {
		
		IdValuePair xueduan=getXueduan(mulu);
		List<IdValuePair> list = new ArrayList<IdValuePair>();

		list.add(xueduan);

		ResourceDictionaryEntry e = null;
		IdValuePair ip = null;
		for (int i = 3; i < mulu.length(); i += 3) {
			String subMulu = mulu.substring(0, i);
			e = muluObjectMap.get(subMulu);
			if (null == e) {
				throw new Exception("目录错误");
			}

			ip = new IdValuePair(e.getID(), e.getName());
			list.add(ip);
		}
		System.out.println(list);
		return list;
	}

	private static ObjectId getParentId(String mulu,int type) throws Exception {
		
		
		ResourceDictionaryEntry e=dao.getResourceDictionaryEntryBySort(Long.valueOf(mulu), type);
		
		if(null!=e)
			return e.getID();
		
		throw new Exception("么有找到父目录");
	}
	
	
	private static List<IdValuePair> getParentList(int type,String sort)
			throws Exception {
		
		List<IdValuePair> list=null;
		ResourceDictionaryEntry e=dao.getResourceDictionaryEntryBySort(Long.valueOf(sort), type);
		if(null!=e)
		{
			list=e.getParentInfos();
			list.add(new IdValuePair(e.getID(), e.getName()));
			return list;
		}
	
		throw new Exception("没有找到父类");
	}
	
	
	private static ResourceDictionaryEntry getParent(int type,String sort)
			throws Exception {
		
		ResourceDictionaryEntry e=dao.getResourceDictionaryEntryBySort(Long.valueOf(sort), type);
		
		return e;
	}
	
	
	
	
	private static IdValuePair getXueduan(String code) throws Exception
	{
		String s=code.substring(0, 1);
		if(s.equalsIgnoreCase("1"))
			return  new IdValuePair(new ObjectId("55d41e47e0b064452581269c"), "初中");
		if(s.equalsIgnoreCase("2"))
			return  new IdValuePair(new ObjectId("55d41e47e0b064452581269e"), "高中");
		if(s.equalsIgnoreCase("3"))
			return  new IdValuePair(new ObjectId("55d41e47e0b064452581269a"), "小学");
		
		throw new Exception("Can not find xueduan");
					
	}
}
