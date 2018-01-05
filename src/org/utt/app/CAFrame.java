package org.utt.app;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;

public class CAFrame extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CAFrame frame = new CAFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CAFrame() {
		setBounds(100, 100, 450, 300);

	}

}
