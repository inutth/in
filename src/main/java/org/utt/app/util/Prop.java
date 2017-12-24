package org.utt.app.util;

import java.io.IOException;
import java.util.Properties;

public class Prop {
	static Properties properties;
	public static Properties getInstance() {
		if (properties == null) {
			 properties = new Properties();
			 try {
				properties.load(Prop.class.getClassLoader().getResourceAsStream("app.properties"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.exit(0);
			}
		}
		return properties;
	}
	public static String getProperty(String key) {
		return Prop.getInstance().getProperty(key);
	}
	public static void init() {
		Prop.getInstance();
	}
	
}
