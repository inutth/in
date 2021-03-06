package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.utt.app.opd.ObjectOPD;

public class AdminPanel extends JPanel {

	JTabbedPane tabbedPane,tabbedPane_in;
	Dimension screen;
	UserControl user;
	CompControl comp;
	DoctorControl doctor;
	JPanel midPanel;
	 
	
	public AdminPanel( ) {
 
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		
		user = new UserControl();
		comp = new CompControl();
		doctor = new DoctorControl();
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		//tabbedPane.addTab("<html>C<br>O<br>N<br>T<br>R<br>O<br>L </html>", null, user, "control");
		add(tabbedPane, BorderLayout.CENTER);
		
		midPanel = new JPanel();
		midPanel.setLayout(new BorderLayout(0, 0));
		midPanel.setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		//mainPanel.add(midPanel, BorderLayout.CENTER);
		
		 
        //tabbedPane.setBackground(new Color(232, 248, 245  ));
		tabbedPane_in = new JTabbedPane();
        midPanel.add(tabbedPane_in, BorderLayout.CENTER);
        tabbedPane_in.addTab("<html><b>User Management</b></html> " ,null,user);
        tabbedPane_in.addTab("<html><b>Computer Management</b></html> " ,null,comp);
        tabbedPane_in.addTab("<html><b>Doctor Management</b></html> " ,null,doctor);
        //tabbedPane.addTab("<html><b>EMR</b></html> ",new ImageIcon(getClass().getClassLoader().getResource("images/running.png")) ,mainHDFSOPDPanel);
        //tabbedPane.addTab("<html><b>Old EMR</b></html> ",new ImageIcon(InApp.class.getResource("/org/utt/app/gfx/box.png")) ,mainOldHDFSOPDPanel);
        //tabbedPane.addTab("<html><b>SCAN</b></html> " ,new ImageIcon(InApp.class.getResource("/org/utt/app/gfx/add.png")),scanOPDCardUser);
        tabbedPane.addTab("<html>C<br>O<br>N<br>T<br>R<br>O<br>L </html>", null, midPanel, "control");
	}
 

}
