package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ReportPanel extends JPanel {
	JTabbedPane tabbedPane,tabbedPane_in;
	Dimension screen;
	JPanel midPanel;

	OPDReport opdReport;
	IPDReport ipdReport;
	
	public ReportPanel() {
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		opdReport = new OPDReport();
		ipdReport = new IPDReport();
		//tabbedPane.addTab("<html>C<br>O<br>N<br>T<br>R<br>O<br>L </html>", null, user, "control");
		add(tabbedPane, BorderLayout.CENTER);
		
		midPanel = new JPanel();
		midPanel.setLayout(new BorderLayout(0, 0));
		midPanel.setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		tabbedPane_in = new JTabbedPane();
        midPanel.add(tabbedPane_in, BorderLayout.CENTER);
		tabbedPane_in.addTab("<html><b>OPD Report</b></html> " ,null,opdReport);
		tabbedPane_in.addTab("<html><b>IPD Report</b></html> " ,null,ipdReport);
		
		tabbedPane.addTab("<html>C<br>O<br>N<br>T<br>R<br>O<br>L </html>", null, midPanel, "control");

	}

}
