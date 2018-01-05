package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.utt.app.opd.CalendarPanel;
import org.utt.app.opd.ObjectOPD;

public class AppMntPanel extends JPanel implements  Observer{
	ObjectOPD oUserInfo;
	Dimension screen;
	JPanel topPanel, navigationButtonPanel;
	JButton prevMonthButton, nextMonthButton, currentMonthButton;
	JTextField dateField;
	JLabel sundayLabel, mondayLabel, tuesdayLabel, wednesdayLabel, thursdayLabel, fridayLabel, saturdayLabel, monthYearLabel;
	int topPanelWidth;
	int TOP_PANEL_HEIGHT = 75;
	CalendarPanel calendarPanel;
	
	public AppMntPanel(ObjectOPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(null);
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		//setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		topPanelWidth=screen.width-50;
		
		drawPanels();
	}
	public void update(Observable oObservable, Object oObject) {
		oUserInfo = ((ObjectOPD)oObservable); // cast
	}
	private void drawPanels() {
        drawTopPanel();
        //drawDayDetailPanel();
        drawCalendarPanel();
    }
	private void drawTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.decode("#E2E2E2")));

        // buttons
        prevMonthButton = new JButton("<");
        //prevMonthButton.addActionListener(new prevMonthButtonHandler());
        currentMonthButton = new JButton("Today");
        //currentMonthButton.addActionListener(new currentMonthButtonHandler());
        nextMonthButton = new JButton(">");
        //nextMonthButton.addActionListener(new nextMonthButtonHandler());
        dateField = new JTextField();
        dateField.setHorizontalAlignment(JTextField.CENTER);
        setDateFieldText();
        //dateField.addActionListener(new dateFieldHandler());

        navigationButtonPanel = new JPanel();
        navigationButtonPanel.setBackground(Color.WHITE);
        navigationButtonPanel.setLayout(new GridLayout());

        // weekday labels
        sundayLabel = new JLabel("Sun", SwingConstants.RIGHT);
        sundayLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mondayLabel = new JLabel("Mon", SwingConstants.RIGHT);
        mondayLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        tuesdayLabel = new JLabel("Tue", SwingConstants.RIGHT);
        tuesdayLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        wednesdayLabel = new JLabel("Wed", SwingConstants.RIGHT);
        wednesdayLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        thursdayLabel = new JLabel("Thu", SwingConstants.RIGHT);
        thursdayLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        fridayLabel = new JLabel("Fri", SwingConstants.RIGHT);
        fridayLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        saturdayLabel = new JLabel("Sat", SwingConstants.RIGHT);
        saturdayLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // draw the active month and year label
        monthYearLabel = new JLabel();
        monthYearLabel.setForeground(Color.decode("#333333"));
        monthYearLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        setMonthYearLabelText();

        topPanel.setBackground(Color.WHITE);
        setTopPanelBounds();

        // add components
        navigationButtonPanel.add(dateField);
        navigationButtonPanel.add(prevMonthButton);
        navigationButtonPanel.add(currentMonthButton);
        navigationButtonPanel.add(nextMonthButton);
        topPanel.add(navigationButtonPanel);

        topPanel.add(sundayLabel);
        topPanel.add(mondayLabel);
        topPanel.add(tuesdayLabel);
        topPanel.add(wednesdayLabel);
        topPanel.add(thursdayLabel);
        topPanel.add(fridayLabel);
        topPanel.add(saturdayLabel);

        topPanel.add(monthYearLabel);
        add(topPanel);
    }
	public void drawCalendarPanel() {
        calendarPanel = new CalendarPanel();
        add(calendarPanel);
    }
	public void setMonthYearLabelText() {
    }
	public void setDateFieldText() {
    }
	public void setTopPanelBounds() {
        topPanel.setBounds(0, 0, topPanelWidth, TOP_PANEL_HEIGHT);
        int dayLabelWidth = topPanelWidth / 7;
        sundayLabel.setBounds(-5, 50, dayLabelWidth, 25);
        sundayLabel.setForeground(Color.decode("#BBBBBB"));
        mondayLabel.setBounds(dayLabelWidth-5, 50, dayLabelWidth, 25);
        tuesdayLabel.setBounds(2*dayLabelWidth-5, 50, dayLabelWidth, 25);
        wednesdayLabel.setBounds(3*dayLabelWidth-5, 50, dayLabelWidth, 25);
        thursdayLabel.setBounds(4*dayLabelWidth-5, 50, dayLabelWidth, 25);
        fridayLabel.setBounds(5*dayLabelWidth-5, 50, dayLabelWidth, 25);
        saturdayLabel.setBounds(6*dayLabelWidth-5, 50, dayLabelWidth, 25);
        saturdayLabel.setForeground(Color.decode("#BBBBBB"));
        monthYearLabel.setBounds(15,10,topPanelWidth/2,50);
        navigationButtonPanel.setBounds(topPanelWidth - 455, 12, 450, 30);
    }



}
