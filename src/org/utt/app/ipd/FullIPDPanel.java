package org.utt.app.ipd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.utt.app.InApp;
import org.utt.app.ipd.ObjectIPD;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.Setup;

public class FullIPDPanel extends JPanel implements  Observer{
	ObjectIPD oUserInfo;
	Dimension screen;
	JPanel [] bedPanel;
	JPanel mainPanel,main,topPanel;
	JButton ButtonRefresh;
	
	//int bedR=40;
	int bedC=0;
	
	String ptStatus=" OUTDATETIME  is null";
	 
	/**
	 * Create the panel.
	 */
	public FullIPDPanel(ObjectIPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width, screen.height-175));
		//System.out.println(screen.width);
		
		main = new JPanel();
		main.setPreferredSize(new Dimension(screen.width, screen.height-175));
		main.setBorder(new TitledBorder(null, "Bed View", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		main.setLayout(new BorderLayout(0, 0));
		main.setBackground(Setup.getColor()); 
		
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(screen.width, screen.height-210));
		  
		mainPanel.setLayout(null);
		mainPanel.setBackground(Setup.getColor()); 
		
		main.add(mainPanel,BorderLayout.CENTER);
		bedC=8;
		
		initPanel(getTotalIPD());
		getDataIPD();
		
		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(screen.width, 30));
		  
		topPanel.setLayout(null);
		topPanel.setBackground(Setup.getColor()); 
		
		ButtonRefresh = new JButton("Refresh");
		ButtonRefresh.setBackground(Setup.getColor());
		ButtonRefresh.setForeground(UIManager.getColor("Button.darkShadow"));

		ButtonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearpt();
				initPanel(getTotalIPD());
				getDataIPD();
				mainPanel.validate();
				mainPanel.repaint();
			}
		});
		ButtonRefresh.setBounds(30, 5, 90, 20);
		topPanel.add(ButtonRefresh);
		main.add(topPanel,BorderLayout.NORTH);
		
		
		add(main,BorderLayout.CENTER);

	}
	public void update(Observable oObservable, Object oObject) {
		oUserInfo = ((ObjectIPD)oObservable); // cast

	}
	public void initPanel(int ipd){
		 
		bedPanel = new JPanel [ipd];
		int j=0;
		int w=120,h=75;
		for(int i=0;i<bedPanel.length;i++){
			 
			if(i%bedC==0){
				bedPanel[i] = new JPanel();
				bedPanel[i].setPreferredSize(new Dimension(w, h));
				//bedPanel[i].setBorder(new TitledBorder(null, "Panel "+i, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				bedPanel[i].setBounds(10, 10+(j*h), w, h);
				bedPanel[i].setLayout(null);
				//bedPanel[i].setBackground(new Color(243, 223, 208)); 
				mainPanel.add(bedPanel[i]);
				
				 
			}
			else if(i%bedC==1){
				bedPanel[i] = new JPanel();
				bedPanel[i].setPreferredSize(new Dimension(w, h));
				//bedPanel[i].setBorder(new TitledBorder(null, "Panel "+i, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				bedPanel[i].setBounds(10+((i%bedC)*w), 10+(j*h), w, h);
				bedPanel[i].setLayout(null);
				//bedPanel[i].setBackground(new Color(243, 223, 208)); 
				mainPanel.add(bedPanel[i]);
				
				 
			}
			else if(i%bedC==2){
				bedPanel[i] = new JPanel();
				bedPanel[i].setPreferredSize(new Dimension(w, h));
				//bedPanel[i].setBorder(new TitledBorder(null, "Panel "+i, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				bedPanel[i].setBounds(10+((i%bedC)*w), 10+(j*h), w, h);
				bedPanel[i].setLayout(null);
				//bedPanel[i].setBackground(new Color(243, 223, 208)); 
				mainPanel.add(bedPanel[i]);
				
				 
			}
			else if(i%bedC==3){
				bedPanel[i] = new JPanel();
				bedPanel[i].setPreferredSize(new Dimension(w, h));
				//bedPanel[i].setBorder(new TitledBorder(null, "Panel "+i, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				bedPanel[i].setBounds(10+((i%bedC)*w), 10+(j*h), w, h);
				bedPanel[i].setLayout(null);
				//bedPanel[i].setBackground(new Color(243, 223, 208)); 
				mainPanel.add(bedPanel[i]);
				
				 
			}
			else if(i%bedC==4){
				bedPanel[i] = new JPanel();
				bedPanel[i].setPreferredSize(new Dimension(w, h));
				//bedPanel[i].setBorder(new TitledBorder(null, "Panel "+i, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				bedPanel[i].setBounds(10+((i%bedC)*w), 10+(j*h), w, h);
				bedPanel[i].setLayout(null);
				//bedPanel[i].setBackground(new Color(243, 223, 208)); 
				mainPanel.add(bedPanel[i]);
				
				 
			}
			else if(i%bedC==5){
				bedPanel[i] = new JPanel();
				bedPanel[i].setPreferredSize(new Dimension(w, h));
				//bedPanel[i].setBorder(new TitledBorder(null, "Panel "+i, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				bedPanel[i].setBounds(10+((i%bedC)*w), 10+(j*h), w, h);
				bedPanel[i].setLayout(null);
				//bedPanel[i].setBackground(new Color(243, 223, 208)); 
				mainPanel.add(bedPanel[i]);
				
				 
			}
			else if(i%bedC==6){
				bedPanel[i] = new JPanel();
				bedPanel[i].setPreferredSize(new Dimension(w, h));
				//bedPanel[i].setBorder(new TitledBorder(null, "Panel "+i, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				bedPanel[i].setBounds(10+((i%bedC)*w), 10+(j*h), w, h);
				bedPanel[i].setLayout(null);
				//bedPanel[i].setBackground(new Color(243, 223, 208)); 
				mainPanel.add(bedPanel[i]);
				
				 
			}
			else if(i%bedC==7){
				bedPanel[i] = new JPanel();
				bedPanel[i].setPreferredSize(new Dimension(w, h));
				//bedPanel[i].setBorder(new TitledBorder(null, "Panel "+i, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				bedPanel[i].setBounds(10+((i%bedC)*w), 10+(j*h), w, h);
				bedPanel[i].setLayout(null);
				//bedPanel[i].setBackground(new Color(243, 223, 208)); 
				mainPanel.add(bedPanel[i]);
				
				 j++;
			}
			 
			
			//System.out.println(j+"-Bed.."+i);
			
		}
		 
	}
	public  void getDataIPD(){
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and ward='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length>1){
			sql_user1="and ward in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
		}
		 
		
		Connection conn ;
		PreparedStatement stmt,stmt1,stmt2,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		String query="select an,firstname,lastname,hn,usedrightcode,outdatetime,BirthDateTime,sex,maritalstatus,initialnamecode,bedno,admdatetime,referfromhospital from in_view_ipd  where   "+ptStatus+"   and suffix='0' "+sql_user+" order by bedno ";				 
		//String query="select an,firstname,lastname,hn,usedrightcode,outdatetime,BirthDateTime,sex,maritalstatus,initialnamecode,ref,bedno from in_view_pt_ipd  where   "+ptStatus+"  and reftype='01'  and suffix='0' "+sql_user+" order by bedno ";			
		
		try {

			conn = new DBmanager().getConnMSSql();
			//conn1 = DriverManager.getConnection(InApp.UrlCust21);
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			int p=0;
			while (rs.next()) {
				String sex_pt="",mar_pt="";
				String initname="",right="";
				Date birthday=rs.getDate(7);
				String age_pt=Setup.AgeInAll(birthday.toString()).trim();
				if(rs.getString(10)==null || rs.getString(10).equals("1") || rs.getString(10).equals("2") || rs.getString(10).equals("3") || rs.getString(10).equals("127") || rs.getString(10).equals("128")){
					sex_pt=rs.getString(8).trim();
					mar_pt=rs.getString(9).trim();				 
					int age_y_int= Integer.parseInt(Setup.AgeInYear(birthday.toString()).trim());
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
					for(int i=0;i<InApp.initname_indb.length;i++){
						if(rs.getString(10).trim().equals(InApp.initname_indb[i][0])){
							initname=InApp.initname_indb[i][1];
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					//initname=rs.getString(10).trim();
				}
				if(rs.getString(5) !=null){
					for(int i=0;i<InApp.rightname.length;i++){
						if(rs.getString(5).trim().equals(InApp.rightname[i][0])){
							right=InApp.rightname[i][1];
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
				}
				
				
				//vstring.add(rs.getString(1)+": "+initname+" "+rs.getString(2).substring(1)+" "+rs.getString(3).substring(1));
				String bedno="";
				if(rs.getString(11)!=null){
					bedno=rs.getString(11);
				}
				else{
	            	bedno="0000";
	            }

				//System.out.println(" "+initname+" "+rs.getString(2).substring(1).trim()+" "+rs.getString(3).substring(1).trim());
				 
				//vstring.add(" ("+rs.getString(4)+"/"+rs.getString(5)+"*"+clinic_pt+"#"+pt_status+")");
				//System.out.println("AN: "+rs.getString(1)+" HN: "+rs.getString(4));
				//System.out.println("อายุ  : "+age_pt);
			 
				//bedPanel[p].setBorder(new TitledBorder(null, "Bed no. "+bedno, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				final JLabel Label_an = new JLabel("["+p+":"+bedno+"]"+"AN: "+rs.getString(1));
				Label_an.setFont(new Font("Tahoma", Font.PLAIN, 11));
				Label_an.setBounds(5,15,140,20);
				bedPanel[p].add(Label_an);
				
				 
				JLabel Label_hn = new JLabel("HN: "+rs.getString(4));
				Label_hn.setFont(new Font("Tahoma", Font.PLAIN, 10));
				Label_hn.setBounds(5,35,140,20);
				//bedPanel[p].add(Label_hn); 
				 
				JLabel Label_name = new JLabel(" "+initname+" "+rs.getString(2).substring(1).trim()+" "+rs.getString(3).substring(1).trim());
				Label_name.setFont(new Font("Tahoma", Font.PLAIN, 11));
				Label_name.setBounds(5,35,140,20);
				bedPanel[p].add(Label_name);
				
				JLabel Label_lname = new JLabel(" "+rs.getString(3).substring(1).trim());
				Label_lname.setFont(new Font("Tahoma", Font.PLAIN, 11));
				Label_lname.setBounds(5,75,140,20);
				//Label_lname.setBackground(new Color(202, 222, 238));
				//bedPanel[p].add(Label_lname);
				
				
				bedPanel[p].setBackground(new Color(250, 252, 253)); 
				bedPanel[p].setBorder(BorderFactory.createLineBorder(new Color(202, 222, 238))); 
				bedPanel[p].addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent arg0) {
						String p="";
						System.out.println(""+Label_an.getText());
						p=Label_an.getText().substring(1,Label_an.getText().indexOf(":"));
						//System.out.println("b "+p);
						for(int i=0;i<bedPanel.length;i++){
							if(i==Integer.parseInt(p)){
								bedPanel[i].setBorder(BorderFactory.createLineBorder(Color.RED));
							}else{
								bedPanel[i].setBorder(BorderFactory.createLineBorder(new Color(202, 222, 238))); 
							}
						}
						
						//mainPanel.removeAll();
						
						
						//mainPanel.validate();
						//mainPanel.repaint();
					}
					public void mouseReleased(MouseEvent arg0) {

					}
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					});
				
				p++;
			}
		
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  int getTotalIPD(){
		
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and ward='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length>1){
			sql_user1="and ward in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
		}
		 
		
		Connection conn ;
		PreparedStatement stmt,stmt1,stmt2,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		String query="select an,firstname,lastname,hn,usedrightcode,outdatetime,BirthDateTime,sex,maritalstatus,initialnamecode,bedno,admdatetime,referfromhospital from in_view_ipd  where   "+ptStatus+"   and suffix='0' "+sql_user+" order by bedno ";				 
		//String query="select an,firstname,lastname,hn,usedrightcode,outdatetime,BirthDateTime,sex,maritalstatus,initialnamecode,ref,bedno from in_view_pt_ipd  where   "+ptStatus+"  and reftype='01'  and suffix='0' "+sql_user+" order by bedno ";			
		int p=0;
		try {

			conn = new DBmanager().getConnMSSql();
			//conn1 = DriverManager.getConnection(InApp.UrlCust21);
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			 
			while (rs.next()) {
 
				p++;
			}
		
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return p;
	}
	public void clearpt(){
		for(int i=0;i<bedPanel.length;i++){
				bedPanel[i].setBorder(BorderFactory.createLineBorder(new Color(202, 222, 238)));		 
		}
	}


}
