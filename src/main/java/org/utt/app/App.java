package org.utt.app;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.UIManager;

import org.utt.app.dao.DAOInit;
import org.utt.app.util.Prop;



/**
 * 
 *
 */
public class App {
	
	public App() {
		new InApp();
	}
    public static void main( String[] args ){
        System.out.println( "In Starter.." );
        try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("org.utt.app.WindowsLookAndFeel");
			UIManager.put("PopupMenu.consumeEventOnClose", Boolean.TRUE);
			UIManager.put("OptionPane.background",new Color(243, 223, 208));
			UIManager.put("Dialog.background",new Color(243, 223, 208));
			//UIManager.put("DesktopPane.background",new Color(243, 223, 208));
			UIManager.put("InternalFrame.background",new Color(243, 223, 208));
			UIManager.put("InternalFrame.activeTitleBackground", Color.red);
	        UIManager.put("InternalFrame.activeTitleForeground", Color.blue);
	        UIManager.put("InternalFrame.inactiveTitleBackground", Color.black);
	        UIManager.put("InternalFrame.inactiveTitleForeground", Color.yellow);
			UIManager.put("Panel.background",new Color(243, 223, 208));
			//UIManager.put("TitledBorder.border", new LineBorder(new Color(200,200,200), 1));
			 
			//setUIFont (new javax.swing.plaf.FontUIResource(THSarabunNew));			
		} catch (Exception e) { }
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Prop.init();
					DAOInit.init();
					macosConfig();
			        new App();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
    public static void macosConfig() {
    	System.setProperty("apple.laf.useScreenMenuBar", "true");
    }
}

