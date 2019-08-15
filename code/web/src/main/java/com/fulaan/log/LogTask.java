package com.fulaan.log;

import com.fulaan.log.dto.LogDTO;
import com.fulaan.log.service.LogService;
import com.pojo.log.LogType;

import java.util.concurrent.LinkedBlockingQueue;

public class LogTask implements Runnable {

	private static LinkedBlockingQueue<LogDTO> logs = new LinkedBlockingQueue<LogDTO>();
	private  LogService logService=new LogService();
	public static void put(LogDTO log) {
		try {
            logs.put(log);
		} catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	public static void put(LogType type, String userId, String actionName) {
        LogDTO le = new LogDTO();
		le.setActionType(type.getCode());
		le.setUserId(userId);
		le.setActionName(actionName);
		put(le);
	}
	

	@Override
	public void run() {
		while (true) {
			try {
                LogDTO logDto = logs.take();
                logService.insert(logDto);
			} catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
	}
}
