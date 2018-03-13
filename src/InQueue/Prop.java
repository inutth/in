package org.utt.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Prop {
	static Properties properties;
	public static Properties getInstance() {
		InputStream input = null;
		if (properties == null) {
			 properties = new Properties();
			 try {
				 input=Prop.class.getClassLoader().getResourceAsStream("app.properties");
			 	 properties.load(input);
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
	public static void setProperty(String key,String value) {
		Prop.getInstance().setProperty(key,value);
	}
	public static void init() {
		Prop.getInstance();
	}
	
}
