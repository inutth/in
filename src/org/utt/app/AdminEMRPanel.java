package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class AdminEMRPanel extends JPanel {

	Dimension screen;
	
	public AdminEMRPanel() {
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width, screen.height-175));
		setLayout(new BorderLayout(0, 0));
		

	}

}
