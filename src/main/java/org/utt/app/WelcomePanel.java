package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.utt.app.util.I18n;

public class WelcomePanel extends JPanel {

	Dimension screen;
	JLabel info=new JLabel(I18n.lang("welcome.info"));

	JTextPane tPane;
	 
	public WelcomePanel() {
		screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setPreferredSize(new Dimension(500, 400));
		setLayout(new BorderLayout());
		//add(info,BorderLayout.NORTH);
		
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
		tPane = new JTextPane();                
        tPane.setBorder(eb);
        tPane.setBackground(new Color(191, 207, 245));
        tPane.setMargin(new Insets(5, 5, 5, 5));
	
		add(tPane,BorderLayout.CENTER);
		appendToPane(tPane, I18n.lang("welcome.info.text"), Color.BLACK);

	}
	private void appendToPane(JTextPane tp, String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Tahoma");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
	}
	

}

