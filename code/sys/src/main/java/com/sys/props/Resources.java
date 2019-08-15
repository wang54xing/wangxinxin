package com.sys.props;

public class Resources {

	private static final ConfigProperties config = new ConfigProperties(
			new String[] { "/app_conf.properties"});

	public static String getProperty(String key, String defaultValue) {
		return config.getProperty(key, defaultValue);
	}

	public static String getProperty(String key) {
		return config.getProperty(key);
	}
	
	public static int getIntProperty(String key) {
		
	return 	config.getIntProperty(key);
	}
	
	public static int getIntProperty(String key,int def) {
		return 	config.getIntProperty(key,def);
	}

	
}
