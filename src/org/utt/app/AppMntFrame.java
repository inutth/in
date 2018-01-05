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

import org.utt.app.dao.DBmanager;
import org.utt.app.opd.ObjectOPD;
import org.utt.app.util.I18n;

public class AppMntFrame extends JInternalFrame {

	Dimension screen;
	AppMntPanel appMntPanel;
	ObjectOPD objectData;
	

	/**
	 * 
	 */
	public AppMntFrame() {
		screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screen.width-5, screen.height-100);
        setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		setTitle(I18n.lang("dental.title"));
	    setLocation(0, 0);

	    setClosable(true);
	    setIconifiable(true);
	    setMaximizable(true);
	    
	    setResizable(false);
	    setDefaultCloseOperation(HIDE_ON_CLOSE);
	    
	    getContentPane().setLayout(new BorderLayout(0, 0));
	    
	    objectData = new ObjectOPD();
	    appMntPanel = new AppMntPanel(objectData);
	    objectData.addObserver(appMntPanel);
	    getContentPane().add(appMntPanel, BorderLayout.CENTER);


	    setVisible(false);
	     
	 
	    
    	
	}


}
