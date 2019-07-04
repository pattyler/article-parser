package io.github.patfromthe90s.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for loading a properties file and accessing them.
 * 
 * @author Patrick
 *
 */
public class PropertiesUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
	
	private static final Properties properties;
	
	static {
		properties = new Properties();
	}
	
	public static void load(String filename) {
		try {
			properties.load(new FileReader(filename));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public static String get(String key) {
		return properties.getProperty(key);
	}

}
