package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import org.utt.app.dao.DBmanager;
import org.utt.app.ipd.FullIPDPanel;
import org.utt.app.util.I18n;
import org.utt.app.util.Setup;

public class InApp extends JFrame {
	public  static String [][] initname_indb=null ;
	public  static String [][] rightname=null ;
	
	JDesktopPane desktopPane;
	JToolBar toolBar;
	Dimension screen;
	JDialog dialogLogin;
	JTextField TextField_Longin;
	JPasswordField TextField_PassWord;
	WelcomePanel welcomePanel;
	DentalFrame dentalFrame;
	DentalCommFrame dentalCommFrame;
	DentalReportFrame dentalReportFrame;
	IPDFrame ipdFrame;
	OPDFrame opdFrame;
	PharFrame pharFrame;
	ImgFrame imgFrame;
	AdminFrame adminFrame;
	ReportFrame reportFrame;
	EMRFrame emrFrame;
	AppMntFrame appMntFrame;
	JButton ButtonHome,ButtonDental,ButtonIPD,ButtonOPD,ButtonExit,ButtonImg,ButtonControl,ButtonReport,ButtonEMR,ButtonDentalReport,ButtonDentalComm,ButtonDentalAppMnt;
	
	public  static String username = "";
	public  static String name = "";
	public  static String usertype = "";
	public  static String userappcode = "";
	public  static String userposition = "";
	public  static String cliniccode = "";
	public  static String reportcode = "";

	/**
	 * 
	 */
	public InApp() {
		init2();
		desktopPane = new JDesktopPane();
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width, screen.height-100));
		setBounds(0, 0, screen.width, screen.height-100);
		setTitle(I18n.lang("desktop.title"));
		getContentPane().add(desktopPane, BorderLayout.CENTER);
		desktopPane.setBackground(Setup.getColor());
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBackground(Setup.getColor());
		toolBar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(toolBar, BorderLayout.NORTH);
		ButtonHome = new JButton(" ");
		ButtonHome.setBackground(Setup.getColor());
		
		ButtonHome.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/home.png")));
		ButtonHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(usertype.trim().equals("111")){
					emrFrame.setVisible(false);
					reportFrame.setVisible(false);
					imgFrame.setVisible(false);
					adminFrame.setVisible(false);
				}
				else if(usertype.trim().equals("6")){
					ipdFrame.setVisible(false);
				}
			
			}
		});
		toolBar.add(ButtonHome);
		//welcome panel
		welcomePanel =new WelcomePanel();
		welcomePanel.setBounds(0, 0, screen.width, screen.height-175);
		welcomePanel.setVisible(true);
		desktopPane.add(welcomePanel);
		
		 
		//desktopPane.add(opdFrame);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent ev){
                if (confirmBeforeExit()){
                    System.exit(0);
                }
            }
        });
		setVisible(true);
		createDialog(this,I18n.lang("label.dialog.title"));
		
	}
	public void createDialog(JFrame f,String title){
		dialogLogin = new JDialog(f,title,true);
		JPanel p =new JPanel(null);
		JLabel LabelLongin = new JLabel(I18n.lang("label.username"),JLabel.RIGHT);
		JLabel LabelPassword =new JLabel(I18n.lang("label.password"),JLabel.RIGHT);		
		JLabel LabelVersion = new JLabel(I18n.lang("label.version"),JLabel.RIGHT);
		LabelVersion.setFont(new Font("Tahoma", Font.PLAIN, 11));
		JLabel LabelHospital = new JLabel(I18n.lang("label.hosp.name"),JLabel.RIGHT);
		LabelHospital.setFont(new Font("Tahoma", Font.PLAIN, 11));
		TextField_Longin =  new JTextField(10);
		TextField_PassWord = new JPasswordField(10);
		TextField_Longin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckUser();
			}
		});
		TextField_PassWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckUser();
			}
		});
		JButton ButtonOK = new JButton(I18n.lang("label.button.login"));
		JButton ButtonCancel = new JButton(I18n.lang("label.button.cancel"));
		
		p.add(LabelLongin);
		p.add(LabelPassword);
		p.add(TextField_Longin);
		p.add(TextField_PassWord);
		p.add(ButtonOK);
		p.add(ButtonCancel);
		p.add(LabelVersion);
		p.add(LabelHospital);

		LabelLongin.setBounds(10,20,80,25);
		LabelPassword.setBounds(10,50,80,25);
		TextField_Longin.setBounds(100,21,80,20);
		TextField_PassWord.setBounds(100,51,80,20);
		ButtonOK.setBounds(30,85,80,25);
		ButtonCancel.setBounds(130,85,100,25);
		LabelVersion.setBounds(30,125,200,25);
		LabelHospital.setBounds(30,140,200,25);
		
		Border textfield_b = new SoftBevelBorder(BevelBorder.LOWERED);
		TextField_Longin.setBorder(textfield_b);
		TextField_PassWord.setBorder(textfield_b);	 
		
		ButtonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		ButtonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckUser();
			}
		});
		dialogLogin.setContentPane(p);
		dialogLogin.setBounds(100,200,250,200);
		dialogLogin.setVisible(true);
		if(dialogLogin != null){
			int ans = dialogLogin.getDefaultCloseOperation() ;
			if(ans == 1)
				System.exit(0);
		}
	}
	public void CheckUser(){

		char []pass =TextField_PassWord.getPassword();
		if(TextField_Longin.getText().equals("") || TextField_Longin.getText().equals("=") || TextField_Longin.getText().length()>9 ){
			JOptionPane.showMessageDialog(dialogLogin,I18n.lang("dialoglogin.label.username.error.title"),I18n.lang("dialoglogin.label.username.error.text"),JOptionPane.WARNING_MESSAGE); 
		}else if(pass.length == 0){
			JOptionPane.showMessageDialog(dialogLogin,I18n.lang("dialoglogin.label.passwpord.error.title"),I18n.lang("dialoglogin.label.passwpord.error.text"),JOptionPane.WARNING_MESSAGE); 
		}else{
			String password ="";
			for(int i=0 ;i<pass.length;i++){
				password = password + pass[i];
			 }
			String sql_check_user="SELECT name,position,type,clinic_code,app_code,username,certifyno,report_code from inaccount WHERE username = ? AND password = ? and status='1'";
			
			Connection con=new DBmanager().getConnMySql();
			PreparedStatement stmt;
			try {
				stmt = con.prepareStatement(sql_check_user);
				stmt.setString(1, TextField_Longin.getText().trim());  
				stmt.setString(2, password);
				ResultSet rs=stmt.executeQuery();
				while( rs.next() ){
					if(rs.getString(1) !=null){
						name=rs.getString(1).trim();					
					}
					if(rs.getString(2) !=null){
						userposition=rs.getString(2).trim();						
					}
					if(rs.getString(3) !=null){
						usertype=rs.getString(3).trim();						
					}
					if(rs.getString(4) !=null){
						cliniccode=rs.getString(4).trim();						
					}
					if(rs.getString(5) !=null){
						userappcode=rs.getString(5).trim();						
					}
					if(rs.getString(6) !=null){
						username=rs.getString(6).trim();						
					}
					if(rs.getString(8) !=null){
						reportcode=rs.getString(8).trim();						
					}
				}
				
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
			if(username.equals("")){
				JOptionPane.showMessageDialog(dialogLogin,"Please check UserName or Password","Password Error",JOptionPane.ERROR_MESSAGE);
			 }
			else{
				dialogLogin.dispose();
		 		dialogLogin =null;	
		 		//welcomePanel.setVisible(false);
		 		welcomePanel.removeAll();
		 		welcomePanel.revalidate();
		 		welcomePanel.repaint();
		 		
			}
			 		
			
		}
		if(usertype.trim().equals("111")){
			adminFrame = new AdminFrame();
			desktopPane.add(adminFrame);
			reportFrame = new ReportFrame();
			desktopPane.add(reportFrame);
			emrFrame = new EMRFrame();
			desktopPane.add(emrFrame);
			
			
			imgFrame = new ImgFrame();
			desktopPane.add(imgFrame);
			ButtonImg = new JButton(" Photo ");
			ButtonImg.setBackground(Setup.getColor());
			
			ButtonImg.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonImg.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					imgFrame.setVisible(true);
					imgFrame.initRight();
					reportFrame.setVisible(false);
					adminFrame.setVisible(false);
					emrFrame.setVisible(false);
				}
			});
			toolBar.add(ButtonImg);
			ButtonControl = new JButton(" Control ");
			ButtonControl.setBackground(new Color(191, 207, 245));
			
			ButtonControl.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonControl.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					adminFrame.setVisible(true);
					reportFrame.setVisible(false);
					imgFrame.setVisible(false);
					emrFrame.setVisible(false);
					
				}
			});
			toolBar.add(ButtonControl);
			ButtonReport = new JButton(" Report ");
			ButtonReport.setBackground(new Color(191, 207, 245));
			
			ButtonReport.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonReport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					reportFrame.setVisible(true);
					adminFrame.setVisible(false);
					imgFrame.setVisible(false);
					emrFrame.setVisible(false);
				}
			});
			toolBar.add(ButtonReport);
			ButtonEMR = new JButton(" EMR Manage ");
			ButtonEMR.setBackground(new Color(191, 207, 245));
			
			ButtonEMR.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonEMR.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					emrFrame.setVisible(true);
					reportFrame.setVisible(false);
					imgFrame.setVisible(false);
					adminFrame.setVisible(false);
				}
			});
			toolBar.add(ButtonEMR);
			setButtonExit();
		}
		else if(usertype.trim().equals("6")){
			ipdFrame = new IPDFrame();
			desktopPane.add(ipdFrame);
			
			welcomePanel.add(new FullIPDPanel(ipdFrame.getObject()),BorderLayout.CENTER);
			welcomePanel.revalidate();
	 		welcomePanel.repaint();
			ButtonIPD = new JButton("IPD ");
			ButtonIPD.setBackground(Setup.getColor());
			
			ButtonIPD.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonIPD.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ipdFrame.setVisible(true);
					ipdFrame.initIPDPanel();
					 
				}
			});
			toolBar.add(ButtonIPD);
			
			setButtonExit();
			
			
		}
		else if(usertype.trim().equals("8")){
			opdFrame = new OPDFrame();
			desktopPane.add(opdFrame);
			ButtonOPD = new JButton("OPD ");
			ButtonOPD.setBackground(Setup.getColor());
			
			ButtonOPD.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonOPD.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					opdFrame.setVisible(true);
				}
			});
			toolBar.add(ButtonOPD);
			setButtonExit();
			
		}
		else if(usertype.trim().equals("81")){
			pharFrame = new PharFrame();
			desktopPane.add(pharFrame);
			ButtonOPD = new JButton("Phar ");
			ButtonOPD.setBackground(Setup.getColor());
			
			ButtonOPD.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonOPD.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					pharFrame.setVisible(true);
				}
			});
			toolBar.add(ButtonOPD);
			setButtonExit();
			
		}
		else if(usertype.trim().equals("83")){
			opdFrame = new OPDFrame();
			desktopPane.add(opdFrame);
			ButtonOPD = new JButton("OPD ");
			ButtonOPD.setBackground(Setup.getColor());
			
			ButtonOPD.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonOPD.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					opdFrame.setVisible(true);
				}
			});
			toolBar.add(ButtonOPD);
			setButtonExit();
			
		}
		else if(usertype.trim().equals("10")){
			dentalFrame = new DentalFrame();
			desktopPane.add(dentalFrame);
			dentalCommFrame = new DentalCommFrame();
			desktopPane.add(dentalCommFrame);
			dentalReportFrame = new DentalReportFrame();
			desktopPane.add(dentalReportFrame);
			appMntFrame = new AppMntFrame();
			desktopPane.add(appMntFrame);
			 
			ButtonDental = new JButton(" Dental ");
			ButtonDental.setBackground(Setup.getColor());
			
			ButtonDental.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonDental.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dentalFrame.setVisible(true);
					dentalFrame.initDetalPanel();
					dentalReportFrame.setVisible(false);
					dentalCommFrame.setVisible(false);
					appMntFrame.setVisible(false);
				}
			});
			toolBar.add(ButtonDental);
			
			 
			ButtonDentalComm = new JButton(" Dental Comm ");
			ButtonDentalComm.setBackground(Setup.getColor());
			
			ButtonDentalComm.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonDentalComm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dentalFrame.setVisible(false);
					dentalReportFrame.setVisible(false);
					dentalCommFrame.setVisible(true);
					appMntFrame.setVisible(false);
				}
			});
			toolBar.add(ButtonDentalComm);
			
			ButtonDentalReport = new JButton(" Dental Report ");
			ButtonDentalReport.setBackground(Setup.getColor());
			
			ButtonDentalReport.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonDentalReport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dentalFrame.setVisible(false);
					dentalReportFrame.setVisible(true);
					dentalCommFrame.setVisible(false);
					appMntFrame.setVisible(false);
				}
			});
			toolBar.add(ButtonDentalReport);
			
			ButtonDentalAppMnt = new JButton(" Dental AppMnt ");
			ButtonDentalAppMnt.setBackground(Setup.getColor());
			
			ButtonDentalAppMnt.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")));
			ButtonDentalAppMnt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					appMntFrame.setVisible(true);
					dentalFrame.setVisible(false);
					dentalReportFrame.setVisible(false);
					dentalCommFrame.setVisible(false);
				}
			});
			toolBar.add(ButtonDentalAppMnt);
			
			
			setButtonExit();
			 
		}
		 
		
	}
	 
	public boolean confirmBeforeExit(){

        if (JOptionPane.showConfirmDialog(this, I18n.lang("desktop.confirmbeforeexitdialog.title"), I18n.lang("desktop.confirmbeforeexitdialog.text"), JOptionPane.YES_NO_OPTION) == 0){
            return true;
        }
        return false;
    }
	public void setButtonExit() {
		toolBar.add(Box.createHorizontalGlue());
		//
		ButtonExit = new JButton(" ");
		ButtonExit.setBackground(Setup.getColor());
		ButtonExit.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/exit.gif")));
		ButtonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (confirmBeforeExit()){
                    System.exit(0);
                }
            
			}
		});
		toolBar.add(ButtonExit);
	}
	public void init2(){
		int p=0,p1=0;
		PreparedStatement stmt31,stmt32;
		ResultSet rs31,rs32,rs33,rs34;
		String query="select code,englishname from sysconfig where ctrlcode=?";
		try {
			Connection conn = new DBmanager().getConnMSSql();
			stmt31 = conn.prepareStatement(query);
			stmt31.setString(1, "10121"); 
			rs31 = stmt31.executeQuery();
			while (rs31.next()) {
				p++;
			}
			rs31.close();
			initname_indb = new String [p][2];
			rs32 = stmt31.executeQuery();
			int pp=0;
			while (rs32.next()) {
				initname_indb[pp][0]=rs32.getString(1);
				initname_indb[pp][1]=rs32.getString(2).substring(1);
				pp++;
			}
			rs32.close();
			stmt31.close();
			//right name
			
			stmt32 = conn.prepareStatement(query);
			stmt32.setString(1, "20019"); 
			rs33 = stmt32.executeQuery();
			while (rs33.next()) {
				p1++;
			}
			rs33.close();
			rightname = new String [p1][2];
			rs34 = stmt32.executeQuery();
			int pp1=0;
			while (rs34.next()) {
				rightname[pp1][0]=rs34.getString(1);
				rightname[pp1][1]=rs34.getString(2).substring(1);
				pp1++;
			}
			rs34.close();
			stmt32.close();
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 


}
