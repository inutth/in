package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.Toolkit;




import javax.swing.JInternalFrame;


import org.utt.app.util.I18n;

public class IPDFrame extends JInternalFrame {

	Dimension screen;

	/**
	 * 
	 */
	public IPDFrame() {
		screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screen.width-5, screen.height-100);
        setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		setTitle(I18n.lang("ipd.title"));
	    setLocation(0, 0);

	    setClosable(true);
	    setIconifiable(true);
	    setMaximizable(true);
	    
	    setResizable(false);
	    setDefaultCloseOperation(HIDE_ON_CLOSE);
	    
	    getContentPane().setLayout(new BorderLayout(0, 0));
	    
	    //northPanel = new NorthPanel();
	   // getContentPane().add(northPanel, BorderLayout.NORTH);


	    setVisible(false);
	     
	 
	    
    	
	}


}
