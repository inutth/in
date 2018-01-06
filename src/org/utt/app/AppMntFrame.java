 package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JInternalFrame;

import org.utt.app.appmnt.CCalendar;
import org.utt.app.appmnt.CalendarManager;
import org.utt.app.appmnt.MainCalendarPanel;
import org.utt.app.dao.DBmanager;
import org.utt.app.opd.ObjectOPD;
import org.utt.app.util.I18n;

public class AppMntFrame extends JInternalFrame {

	
	ObjectOPD objectData;
	
	public Integer frameWidth, frameHeight;
	public MainCalendarPanel mainPanel;
	public CCalendar calendar;
    public CalendarManager manager;
	

	/**
	 * 
	 */
	public AppMntFrame() {
		calendar = new CCalendar();
        manager = new CalendarManager();
        setFrameDimension(false);
        setLayout(null);
		//screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, frameWidth, frameHeight);
        setPreferredSize(new Dimension(frameWidth, frameHeight));
		 
		setTitle(I18n.lang("dental.title"));
	    setLocation(0, 0);

	    setClosable(true);
	    setIconifiable(true);
	    setMaximizable(true);
	    
	    setResizable(false);
	    setDefaultCloseOperation(HIDE_ON_CLOSE);
	    
	     
	    
	    mainPanel = new MainCalendarPanel(AppMntFrame.this);
        setContentPane(mainPanel);
        //getContentPane().add(mainPanel);
	    
	    objectData = new ObjectOPD();
	    ///appMntPanel = new AppMntPanel(objectData);
	    //objectData.addObserver(appMntPanel);
	    //getContentPane().add(appMntPanel, BorderLayout.CENTER);
	   


	    setVisible(false);
	    
	     
	     
	 
	    
    	
	}
	public Integer getMainFrameHeight() {
        return frameHeight;
    }
	public Integer getMainFrameWidth() {
        return frameWidth;
    }
	public void setFrameDimension(boolean resized) {
        if (resized) {
            // window is being resized
            Dimension windowSize = getBounds().getSize();
            frameWidth = (int) windowSize.getWidth();
            frameHeight = (int) windowSize.getHeight();
        }
        else {
            // first time startup
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frameWidth = (int) screenSize.getWidth();
            frameHeight = (int) screenSize.getHeight();
        }
    }
	 


}
