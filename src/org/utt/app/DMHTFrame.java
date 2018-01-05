package org.utt.app;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;

public class DMHTFrame extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DMHTFrame frame = new DMHTFrame();
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
	public DMHTFrame() {
		setBounds(100, 100, 450, 300);

	}

}
