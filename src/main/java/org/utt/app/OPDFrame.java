package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JInternalFrame;

import org.utt.app.dao.DBmanager;
import org.utt.app.opd.MainHDFSOPDPanel;
import org.utt.app.opd.OPDPanel;
import org.utt.app.opd.ObjectOPD;
import org.utt.app.util.I18n;

public class OPDFrame extends JInternalFrame {

	Dimension screen;
	OPDPanel opdPanel;

	ObjectOPD objectData;
	public  static String [] hn_z515=null ;
	public  static String [][] clinicname=null ;
	public  static String [][] clinicno=null ;
	

	/**
	 * 
	 */
	public OPDFrame() {
		//init_z515();
		screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screen.width-5, screen.height-100);
        setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		setTitle(I18n.lang("opd.title"));
	    setLocation(0, 0);

	    setClosable(true);
	    setIconifiable(true);
	    setMaximizable(true);
	    
	    setResizable(false);
	    setDefaultCloseOperation(HIDE_ON_CLOSE);
	    
	    getContentPane().setLayout(new BorderLayout(0, 0));
	    
	    objectData = new ObjectOPD();
	    opdPanel = new OPDPanel(objectData);
	    objectData.addObserver(opdPanel);
	    getContentPane().add(opdPanel, BorderLayout.CENTER);


	    setVisible(false);
	     
	 
	    
    	
	}
	public void init_z515(){
		int p=0;
		Connection conn1,conn2;
		PreparedStatement stmt1,stmt,stmt2;
		ResultSet rs1,rs,rs2;
		
		try {
			conn1 = new DBmanager().getConnMySql();	
			String q1="select hn from utth_patient_memo where memo_type='0001' and  memo='Z515'";		
			stmt = conn1.prepareStatement(q1); 
			rs = stmt.executeQuery();
			while (rs.next()) {
				p++;
			}
			rs.close();
			hn_z515 = new String [p];
			stmt1 = conn1.prepareStatement(q1);		 
			rs1 = stmt1.executeQuery();
			int pp=0;
			while (rs1.next()) {
				String hn="";
				if(rs1.getString(1) !=null){
					hn=rs1.getString(1);
				}				 
				if(hn.equals("")){
					
				}else{
					//add
					hn_z515[pp]=hn;				 
				}
				pp++;
			}
			stmt1.close();
			stmt.close();
			conn1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 		
	}

}
