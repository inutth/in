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

import org.utt.app.util.I18n;

public class DentalReportFrame extends JInternalFrame {

	Dimension screen;
	DentalReportPanel dentalReportPanel;
	

	/**
	 * 
	 */
	public DentalReportFrame() {
		//init_z515();
		screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screen.width-5, screen.height-100);
        setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		//setTitle(I18n.lang("opd.title"));
	    setLocation(0, 0);

	    setClosable(true);
	    setIconifiable(true);
	    setMaximizable(true);
	    
	    setResizable(false);
	    setDefaultCloseOperation(HIDE_ON_CLOSE);
	    
	    getContentPane().setLayout(new BorderLayout(0, 0));
	    
	    dentalReportPanel = new DentalReportPanel();
	    getContentPane().add(dentalReportPanel, BorderLayout.CENTER);


	    setVisible(false);
	     
	 
	    
    	
	}

}
