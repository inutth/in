package org.utt.app.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
	public static Locale locale;
	public static ResourceBundle messages;
	public I18n() {
		
	}
	 public static ResourceBundle getInstance() {
		 if (messages == null) {
			 locale = new Locale("th", "TH");;
			 messages = ResourceBundle.getBundle("messages",locale);
		 }
		 return messages;
	 }
	 public static String lang(String key) {
		 String value = "KEY_NOT_FOUND";
		 value = I18n.getInstance().getString(key);
		 return value;
	 }
	  

}
