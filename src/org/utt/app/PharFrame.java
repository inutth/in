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
import org.utt.app.opd.PharOPDPanel;
import org.utt.app.util.I18n;

public class PharFrame extends JInternalFrame {

	Dimension screen;
	PharOPDPanel pharOPDPanel;

	ObjectOPD objectData;
	public  static String [] hn_z515=null ;
	public  static String [][] clinicname=null ;
	public  static String [][] clinicno=null ;
	public  static String [][] dosecode=null ;
	public  static String [][] dosetype=null ;
	public  static String [][] auxlabel1=null ;
	public  static String [][] doseunitcode=null ;
	

	/**
	 * 
	 */
	public PharFrame() {
		init_z515();
		initclinic();
		initDose();
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
	    pharOPDPanel = new PharOPDPanel(objectData);
	    objectData.addObserver(pharOPDPanel);
	    getContentPane().add(pharOPDPanel, BorderLayout.CENTER);


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
	public void initclinic(){
		String queryclinic="select app_code,clinic_code,clinic_name from clinic_name order by app_code";
		try {

			Connection conn = new DBmanager().getConnMySql();
			PreparedStatement stmt1 = conn.prepareStatement(queryclinic);
			ResultSet rs1 = stmt1.executeQuery();
		    int p1=0;
			while (rs1.next()) {	
				p1++;
			}
			clinicname = new String [p1][2];
			clinicno = new String [p1][2];
			ResultSet rs11 = stmt1.executeQuery();
			int pp1=0;
			while (rs11.next()) {
				clinicname[pp1][0]=rs11.getString(1).trim();
				clinicname[pp1][1]=rs11.getString(3).trim();
				clinicno[pp1][0]=rs11.getString(1).trim();
				clinicno[pp1][1]=rs11.getString(2).trim();
				pp1++;
			}
			rs11.close();
			stmt1.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void initDose(){
		int p=0,p1=0,p2=0,p3=0;
		PreparedStatement stmt31,stmt32,stmt33,stmt34;
		ResultSet rs31,rs32,rs33,rs34,rs35,rs36,rs37,rs38;
		String query30="select code,thainame from sysconfig where ctrlcode=?";
		try {
			Connection conn = new DBmanager().getConnMSSql();
			stmt31 = conn.prepareStatement(query30);
			stmt31.setString(1, "20030"); 
			rs31 = stmt31.executeQuery();
			while (rs31.next()) {
				if(rs31.getString(1) !=null){
					p++;
				}
				 
			}
			rs31.close();
			auxlabel1 = new String [p][2];
			rs32 = stmt31.executeQuery();
			int pp=0;
			while (rs32.next()) {
				String a="",b="";
				if(rs32.getString(1) !=null){
					a=rs32.getString(1).trim();
				}
				if(rs32.getString(2) !=null){
					b=rs32.getString(2).trim().substring(1);
				}
				auxlabel1[pp][0]=a;
				auxlabel1[pp][1]=b;
				pp++;
			}
			rs32.close();
			stmt31.close();
			//20031
			String query31="select code,englishname from sysconfig where ctrlcode=?";
			stmt32 = conn.prepareStatement(query31);
			stmt32.setString(1, "20031"); 
			rs33 = stmt32.executeQuery();
			while (rs33.next()) {
				if(rs33.getString(1) !=null){
					p1++;
				}
			}
			rs33.close();
			dosetype = new String [p1][2];
			rs34 = stmt32.executeQuery();
			int pp1=0;
			while (rs34.next()) {
				String a="",b="";
				if(rs34.getString(1) !=null){
					a=rs34.getString(1).trim();
				}
				if(rs34.getString(2) !=null){
					b=rs34.getString(2).trim().substring(1);
				}
				dosetype[pp1][0]=a;
				dosetype[pp1][1]=b;
				pp1++;
			}
			rs34.close();
			stmt32.close();
			
			//20032
			String query32="select code,englishname from sysconfig where ctrlcode=?";
			stmt33 = conn.prepareStatement(query32);
			stmt33.setString(1, "20032"); 
			rs35 = stmt33.executeQuery();
			while (rs35.next()) {
				if(rs35.getString(1) !=null){
					p2++;
				}
			}
			rs35.close();
			dosecode = new String [p2][2];
			rs36 = stmt33.executeQuery();
			int pp2=0;
			while (rs36.next()) {
				String a="",b="";
				if(rs36.getString(1) !=null){
					a=rs36.getString(1).trim();
				}
				if(rs36.getString(2) !=null){
					b=rs36.getString(2).trim().substring(1);
				}
				dosecode[pp2][0]=a;
				dosecode[pp2][1]=b;
				pp2++;
			}
			rs36.close();
			stmt33.close();
			
			//20034
			String query34="select code,englishname from sysconfig where ctrlcode=?";
			stmt34 = conn.prepareStatement(query34);
			stmt34.setString(1, "20034"); 
			rs37 = stmt34.executeQuery();
			while (rs37.next()) {
				if(rs37.getString(1) !=null){
					p3++;
				}
			}
			rs37.close();
			doseunitcode = new String [p3][2];
			rs38 = stmt34.executeQuery();
			int pp3=0;
			while (rs38.next()) {
				String a="",b="";
				if(rs38.getString(1) !=null){
					a=rs38.getString(1).trim();
				}
				if(rs38.getString(2) !=null){
					b=rs38.getString(2).trim().substring(1);
				}
				doseunitcode[pp3][0]=a;
				doseunitcode[pp3][1]=b;
				pp3++;
			}
			rs38.close();
			stmt34.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String [][] getDoseCode(){
		
		return dosecode;
	}
	public static String [][] getDoseType(){
		
		return dosetype;
	}
	public static String [][] getAuxLabel1(){
		
		return auxlabel1;
	}
	public static String GetAuxLabel1Name(String id){
		String auxlabel1name_to="";
		for(int i=0;i<auxlabel1.length;i++){
			if(id.trim().equals(auxlabel1[i][0].trim())){
				auxlabel1name_to=auxlabel1[i][1].trim();
			}
			//System.out.println(i+1+". "+depart[i][0]);
		}		
		return auxlabel1name_to;
	}
	public static String [][] getDoseUnitCode(){
		
		return doseunitcode;
	}

}
