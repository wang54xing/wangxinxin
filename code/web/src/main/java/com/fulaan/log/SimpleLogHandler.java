package com.fulaan.log;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import com.fulaan.simplelog.service.SimpleLogService;
import com.pojo.log.SimpleLogEntry;
import com.sys.props.Resources;

/**
 * 简单日志处理程序
 * @author fourer
 *
 */
public class SimpleLogHandler {
	
	private static SimpleLogHandler handler =new SimpleLogHandler();
	
	private SimpleLogHandler(){
		
		Thread th=new Thread(new Runnable() {
			@Override
			public void run() {
				while(true)
				{
					insert();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		th.setName("Thread-logHandler");
		th.setDaemon(true);
		th.start();
	}
	
	public  static SimpleLogHandler getSimpleLogHandler()
	{
		return handler;
	}
	
	private SimpleLogService service =new SimpleLogService();
	
	
	private int length=Resources.getIntProperty("simplelog.max.length", 20);
	
	private volatile LinkedBlockingQueue<SimpleLogEntry> insertQueue = null;
	private  LinkedBlockingQueue<SimpleLogEntry> list = null;
	private  ReentrantLock lock =new ReentrantLock();
	
	
	public void put(SimpleLogEntry log)
	{
		try
		{
			lock.tryLock();
			if(null==list)
			{
				list =new LinkedBlockingQueue<SimpleLogEntry>();
			}
			
			try {
				list.put(log);
			} catch (InterruptedException e) {
		
			}
			
			if(list.size()>=length)
			{
				if(insertQueue==null)
				{
					insertQueue=list;
					list=null;
				}
			}
		}finally
		{
			lock.unlock();
		}
	}
	
	
	public void insert()
	{
		if(null!=insertQueue)
		{
			service.addSimpleLogs(new ArrayList<SimpleLogEntry>(this.insertQueue));
			insertQueue=null;
		}
	}
	
	
	public static void main(String[] args) {
		
	}
	
}
