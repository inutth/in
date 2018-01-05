package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.utt.app.opd.ObjectOPD;

public class EMRPanel extends JPanel{

	JTabbedPane tabbedPane,tabbedPane_in;
	Dimension screen;
	JPanel midPanel;

	AdminEMRPanel adminEMRPanel;
	PDFAdmin pdfAdmin;
	
	
	public EMRPanel() {
		super();

		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		adminEMRPanel = new AdminEMRPanel();
		pdfAdmin = new PDFAdmin();

		//tabbedPane.addTab("<html>C<br>O<br>N<br>T<br>R<br>O<br>L </html>", null, user, "control");
		add(tabbedPane, BorderLayout.CENTER);
		
		midPanel = new JPanel();
		midPanel.setLayout(new BorderLayout(0, 0));
		midPanel.setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		tabbedPane_in = new JTabbedPane();
        midPanel.add(tabbedPane_in, BorderLayout.CENTER);
        
        tabbedPane_in.addTab("<html><b>Admin EMR</b></html> " ,null,pdfAdmin);
        
		tabbedPane.addTab("<html>E<br>M<br>R </html>", null,midPanel, "Admin");

	}


}
