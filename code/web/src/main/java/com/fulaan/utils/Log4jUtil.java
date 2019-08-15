package com.fulaan.utils;

import org.apache.log4j.Logger;

public class Log4jUtil {

	private static Logger cLogger = Logger.getLogger("CommonLog");
	private static Logger eLogger = Logger.getLogger("ErrorLog");

	public static void error(String message) {
		eLogger.error(message);
	}

	public static void info(String message) {
		cLogger.warn(message);
	}

	public static void printStackTrace(Exception e) {
		e.printStackTrace();
		error(e.getMessage());
	}
}
