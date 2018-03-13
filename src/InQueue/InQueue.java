package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbToggleButton;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.notification.NotificationManager;

public class InQueue  extends JDialog implements  KeyListener{
	Dimension screen;
	JPanel contentPanel = new JPanel();
	WebPanel lowPanel;
	WebBreadcrumb locationBreadcrumb,locationBreadcrumb1;
	WebBreadcrumbToggleButton userButton;
	JButton userButton1;
	int tick = 0;
	WebLabel[] seperator = new WebLabel[]{new WebLabel(":"), new WebLabel(":")};
	WebLabel time_Label;
	public String [][] initname_indb=null ;
	DateTime now = new DateTime();
	String date_in=now.toLocalDate().toString();
	public InQueue() {
		init2();
		Locale.setDefault(new Locale("th", "TH"));        
		screen = Toolkit.getDefaultToolkit().getScreenSize();	 
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        //int taskBarsize = scnMax.bottom;
        setPreferredSize(new Dimension(screen.width, screen.height));
        //setPreferredSize(new Dimension(screen.width, screen.height-taskBarsize));
		//setBounds(0, 0, screen.width, (screen.height-taskBarsize));
		setBounds(0, 0, screen.width, (screen.height));
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.setBackground(new Color(0, 0, 0));
		 
		addText();
		getContentPane().add(createStatusBar (), BorderLayout.SOUTH);
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		 
	
	}
	public static void main(String[] args) {
		 try {
				//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");			
			} catch (Exception e) { }
		try {
			InQueue dialog = new InQueue();
			//dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public WebStatusBar createStatusBar () {
		final WebStatusBar statusBar = new WebStatusBar ();		
		statusBar.add ( getLocationBreadcrumb () );		
		statusBar.addSpacing ();	
		statusBar.add ( getNext ());
		statusBar.addSpacing ();
		statusBar.addToEnd( getClock ());		
        NotificationManager.setMargin ( 0, 0, statusBar.getPreferredSize ().height, 0 );
		return statusBar;
	}
	WebBreadcrumb getLocationBreadcrumb () {
		locationBreadcrumb = new WebBreadcrumb ( true );
		final ButtonGroup locationGroup = new ButtonGroup ();
		userButton = new WebBreadcrumbToggleButton ();
        userButton.setText ( " In Application : โรงพยาบาล อุตรดิตถ์   " );
        userButton.setSelected ( true );
        userButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
            	System.exit(0);
            }
            
        } );
        locationBreadcrumb.add ( userButton );
        locationGroup.add ( userButton );
		
		return locationBreadcrumb;
	}
	
	WebBreadcrumb getNext () {
		locationBreadcrumb1 = new WebBreadcrumb ( true );
		final ButtonGroup locationGroup1 = new ButtonGroup ();
		userButton1 = new JButton ();
        userButton1.setText ( "Next" );
        userButton1.setSelected ( true );
        getRootPane().setDefaultButton(userButton1);
        userButton1.addActionListener ( new ActionListener (){
            @Override
            public void actionPerformed ( final ActionEvent e ){
            	try {
        			Connection conn = new DBmanager().getConnMySql();
        			String sql = "select visit_clinic.id from visit,visit_clinic where visit.id=visit_clinic.id "
        					+ "and visit.visitdate='"+ date_in+"' and visit_clinic.servicestatus='0'  and visit_clinic.clinic='0203' and visit_clinic.roomno='2' order by visit_clinic.queue limit 1" ;
        		
        			PreparedStatement stmt = conn.prepareStatement(sql);
        			ResultSet rs = stmt.executeQuery();
        			
        			while (rs.next()) {
        				int id=rs.getInt(1);
        				String sql_next = "update visit_clinic set servicestatus='1' where id='"+id+"'";
        				PreparedStatement stmt6 = conn.prepareStatement(sql_next);
						int rs_save = stmt6.executeUpdate();
						stmt6.close();
        			}

        			stmt.close();
        			conn.close();
        		} catch (SQLException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
            	
            	addText();
            	//System.out.println("Hello112");
            }
             
        } );
          
        locationBreadcrumb1.add ( userButton1 );
        locationGroup1.add ( userButton1 );
		
		return locationBreadcrumb1;
	}
	 
	public WebPanel getClock() {
		lowPanel = new WebPanel();
		lowPanel.setPreferredSize(new Dimension(300, 20));
		lowPanel.setLayout(null);
		
		
		
		time_Label = new WebLabel("");
		time_Label.setHorizontalAlignment(SwingConstants.CENTER);
		time_Label.setFont(new Font("Tahoma", Font.BOLD, 13));
		time_Label.setBounds(1, 1, 300, 20);
		lowPanel.add(time_Label);
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Locale.setDefault(new Locale("th", "TH"));
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = 
                        new SimpleDateFormat("วันที่  d MMM yyyy  เวลา   hh:mm:ss");
                Date currentTime = cal.getTime( );
                time_Label.setText(dateFormat.format(currentTime));

                tick++;
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();
		 
		return lowPanel;
	}
	public void addText() {
		contentPanel.removeAll();
		WebLabel room_Label = new WebLabel("ห้องตรวจที่ 3");
		room_Label.setHorizontalAlignment(SwingConstants.CENTER);
		room_Label.setFont(new Font("Tahoma", Font.BOLD, 120));
		room_Label.setBounds( 0, 20,screen.width, 150);
		room_Label.setForeground(Color.WHITE);
		contentPanel.add(room_Label);
		 
		try {
			Connection conn = new DBmanager().getConnMySql();
			String sql = "select visit_clinic.queue,visit.visitno,visit.hn from visit,visit_clinic where visit.id=visit_clinic.id "
					+ "and visit.visitdate='"+ date_in+"' and visit_clinic.servicestatus='0'  and visit_clinic.clinic='0203' and visit_clinic.roomno='2' order by visit_clinic.queue limit 1" ;
		
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				System.out.println(rs.getString(3));
				WebLabel q1 = new WebLabel("ลำดับที่  "+rs.getString(1).trim());
				q1.setHorizontalAlignment(SwingConstants.CENTER);
				q1.setFont(new Font("Tahoma", Font.BOLD, 120));
				q1.setBounds(20, 200,screen.width, 150);
				q1.setForeground(Color.WHITE);
				contentPanel.add(q1);
				
				WebLabel q2 = new WebLabel(getName(rs.getString(3).trim()));
				q2.setHorizontalAlignment(SwingConstants.LEFT);
				q2.setFont(new Font("Tahoma", Font.BOLD, 120));
				q2.setBounds(20, 380,screen.width, 150);
				q2.setForeground(Color.WHITE);
				contentPanel.add(q2);

			}

			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPanel.revalidate();
		contentPanel.repaint();
		
	}
	public String getName(String hn){
		String name_to="",initname="";
		Connection conn;
		PreparedStatement stmt,stmt1,stmt2,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		
		String query_db="select  BirthDateTime,sex,maritalstatus,initialnamecode from patient_info where hn='"+hn.trim()+"'" ; 
		String query_name="select firstname, lastname from patient_name where  suffix='0'  and hn='"+hn.trim()+"'" ; 
		
		conn=new DBmanager().getConnMSSql();
		 
		try {
			stmt = conn.prepareStatement(query_db);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String sex_pt="",mar_pt="";
				Date birthday=rs.getDate(1);
				String age_pt=AgeInAll(birthday.toString()).trim();
				if(rs.getString(4)==null || rs.getString(4).equals("1") || rs.getString(4).equals("2") || rs.getString(4).equals("3") || rs.getString(4).equals("127") || rs.getString(4).equals("128")){
					sex_pt=rs.getString(2).trim();
					mar_pt=rs.getString(3).trim();				 
					int age_y_int= Integer.parseInt(AgeInYear(birthday.toString()).trim());
					if(age_y_int<15){
						if(sex_pt.equals("1")){
							initname="ด.ช.";
						}else if(sex_pt.equals("2")){
							initname="ด.ญ.";
						}
					}else if(age_y_int>=15){
						if(sex_pt.equals("1")){
							initname="นาย";
						}else if(sex_pt.equals("2")){
							if(mar_pt.equals("1")){
								initname="นางสาว";
							}else {
								initname="นาง";
							}
							
						}
					}					 
					
				}else{

					for(int i=0;i<initname_indb.length;i++){
						if(rs.getString(4).trim().equals(initname_indb[i][0])){
							initname=initname_indb[i][1];
							break;
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					//initname=rs.getString(10).trim();
				}	
				
			}
			stmt.close();
			//

			stmt2 = conn.prepareStatement(query_name);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {			
				if(rs2.getString(1) !=null){
					name_to=" "+initname+" "+rs2.getString(1).substring(1).trim()+" "+rs2.getString(2).substring(1).trim();
				}
							
			}
			stmt2.close();
			//
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name_to;
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
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String AgeInAll(String birthday){
		String age="";
		LocalDate birthdate = new LocalDate (birthday);          //Birth date
        LocalDate now = new LocalDate();                    //Today's date
        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        int age_year=period.getYears();
        int age_month=period.getMonths();
        int age_day=period.getDays();
		return age=age_year+" ปี "+age_month+" เดือน "+age_day+" วัน";
	}
	public static String AgeInYear(String birthday){
		String age="";
		LocalDate birthdate = new LocalDate (birthday);          //Birth date
        LocalDate now = new LocalDate();                    //Today's date
        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        int age_year=period.getYears();
		return age=age_year+"";
	}
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
			try {
    			Connection conn = new DBmanager().getConnMySql();
    			String sql = "select visit_clinic.id from visit,visit_clinic where visit.id=visit_clinic.id "
    					+ "and visit.visitdate='"+ date_in+"' and visit_clinic.servicestatus='0'  and visit_clinic.clinic='0203' and visit_clinic.roomno='2' order by visit_clinic.queue limit 1" ;
    		
    			PreparedStatement stmt = conn.prepareStatement(sql);
    			ResultSet rs = stmt.executeQuery();
    			
    			while (rs.next()) {
    				int id=rs.getInt(1);
    				String sql_next = "update visit_clinic set servicestatus='1' where id='"+id+"'";
    				PreparedStatement stmt6 = conn.prepareStatement(sql_next);
					int rs_save = stmt6.executeUpdate();
					stmt6.close();
    			}

    			stmt.close();
    			conn.close();
    		} catch (SQLException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        	
        	addText();
		}
	}
	public void keyReleased(KeyEvent arg0) {
		 
	}
	public void keyTyped(KeyEvent arg0) {
		
	}
}
