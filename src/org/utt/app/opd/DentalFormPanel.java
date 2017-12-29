package org.utt.app.opd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.joda.time.DateTime;
import org.utt.app.InApp;
import org.utt.app.opd.ObjectOPD;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.Setup;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

public class DentalFormPanel extends JPanel implements Observer,ActionListener,MouseListener{
	ObjectOPD oUserInfo;
	Dimension screen;
	JPanel LeftSection;
	JTable tableOPDform,tableTx,tableExam1,tableExam2;;
	Vector<String> columnNamesOPDform,columnNamesTx;
	JScrollPane scrollPaneOPDform,scrollPaneTx,scrollPaneMid2,scrollPane43;
	JPanel MiddleSection,mid2,mid1,right1,RightSection,Panel43,txPanel;
	ButtonGroup groupTypeB,groupEduB,groupEdulevelB,groupFN,groupSN,groupProsN;
	JRadioButton ptTypeBButton[]=null,FNButton[]=null,SNButton[]=null,ProsNButton[]=null;
	JRadioButton ptEduBButton[]=null, ptEdulevelBButton[]=null;
	Vector<String> columnNamesExam1,columnNamesExam2;
	JTextField TextField_occ1,TextField_occ2,TextField_occ3,TextField_provider;
	
	JLabel l_provider,l_toothno,l_diag,l_tx,l_date,l_id;
	JTextField TextField_doctor,TextField_toothno,TextField_diag,TextField_tx,TextField_date,TextField_id;
	
	JTextField TextField_EdulevelB;
	
	JButton ButtonPrintExam1, ButtonSaveExam1,Button431,ButtonSaveExam3,Button432,ButtonTreatment,ButtonDelTx,ButtonSID,ButtonClearTx;
	//String year43="";
	int exam_status=0;
	GregorianCalendar day ;
	
	String servplace="1",dentaltype="5",schooltype="",classlevel="",gum="999999",provider="000",need_fluoride="0",need_scaling="0";
	int pteeth=0,pcaries=0,pfilling=0,pextract=0;
	int dteeth=0,dcaries=0,dfilling=0,dextract=0;
	int need_sealant=0,need_pfilling=0,need_pextract=0,need_dfilling=0,need_dextract=0,nprosthesis=0;
	int perm_perm=0,perm_pros=0,pros_pros=0;
	String [] u = new String[16];
	String [] l = new String[16];

	
	/**
	 * Create the panel.
	 */
	public DentalFormPanel(ObjectOPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-(screen.width*2)/10, screen.height-160));
		day = new GregorianCalendar();
		//year43=Setup.GetDateNow().trim().substring(0, 4);
		
		LeftSection = new JPanel();
		LeftSection.setPreferredSize(new Dimension(200, screen.height-160));
		add(LeftSection, BorderLayout.WEST);
		LeftSection.setLayout(new BorderLayout(0, 0));
		
		JLabel label1 = new JLabel("แบบฟอร์ม");
		label1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		LeftSection.add(label1, BorderLayout.NORTH);
		
		columnNamesOPDform = new Vector<String>();		
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		columnNamesOPDform.add("");
		
		columnNamesTx = new Vector<String>();
		columnNamesTx.add("");
		columnNamesTx.add("  วันที่ให้การรักษา");
		columnNamesTx.add("  ตำแหน่ง");
		columnNamesTx.add("  การวินิจฉัย");
		columnNamesTx.add("  การรักษา");
		columnNamesTx.add("  ผู้ให้การรักษา");
		
		Vector<Vector<String>> dataOPDform = new Vector<Vector<String>>();
		DefaultTableModel modelOPDform = new DefaultTableModel(dataOPDform, columnNamesOPDform){
			public Class getColumnClass(int column){
				return getValueAt(0, column).getClass();
			}
		};
		tableOPDform = new JTable(modelOPDform){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
				Component c = super.prepareRenderer(renderer, row, column);			
				if (!isRowSelected(row)){
					c.setBackground(getBackground());
				}
				return c;
			}
		};
		
	 	
		tableOPDform = new JTable();
		tableOPDform.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row_tableOPDform = tableOPDform.rowAtPoint(e.getPoint());
		        int col_tableOPDform = tableOPDform.columnAtPoint(e.getPoint());
		        String form_name_print=tableOPDform.getValueAt(row_tableOPDform,0).toString().trim();
				String form_name_print_show=tableOPDform.getValueAt(row_tableOPDform,2).toString().trim();
				String page=tableOPDform.getValueAt(row_tableOPDform,3).toString().trim();
				String form_code=tableOPDform.getValueAt(row_tableOPDform,1).toString().trim();
				//System.out.println("..."+form_code);
				String form_clinic_name=tableOPDform.getValueAt(row_tableOPDform,4).toString().trim();
				String typeofprint=tableOPDform.getValueAt(row_tableOPDform,5).toString().trim();
				//hn=oUserInfo.GetPtHN().trim();
				//age=oUserInfo.GetPtAge();
				//vn=oUserInfo.GetPtVN().trim();
				//visitdate=Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate());
				
				//System.out.println(oUserInfo.GetPtHN()+"***"+oUserInfo.GetPtVN());
				if(form_code.equals("00107")) {
					formPanel(form_code,"","");
				}
				else {
					formPanel(form_code,page,form_clinic_name);
				}
				
				 
				
			}
		});
		tableOPDform.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tableOPDform.setRowHeight(25);		
		tableOPDform.setFillsViewportHeight(true);
 
		scrollPaneOPDform = new JScrollPane(tableOPDform);
		getDataForm();
		LeftSection.add(scrollPaneOPDform, BorderLayout.CENTER);
		
		MiddleSection = new JPanel();
		MiddleSection.setBorder(new TitledBorder(null, "Dental", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		MiddleSection.setPreferredSize(new Dimension(((screen.width*2)/10)-300, screen.height-160));
		add(MiddleSection, BorderLayout.CENTER);
		MiddleSection.setLayout(new BorderLayout(0, 0));
		
		mid2 = new JPanel();
		//mid2.setBounds(5, 15, 665, 800);
		 
		mid2.setLayout(new BorderLayout(0, 0));
		
		scrollPaneMid2 = new JScrollPane(mid2);
		 
		MiddleSection.add(scrollPaneMid2,BorderLayout.CENTER);
		
		mid1 = new JPanel();
		mid1.setBounds(5, 15, 665, 30);
		//MiddleSection.add(mid1);
		mid1.setLayout(null);
		
		RightSection = new JPanel();
		RightSection.setPreferredSize(new Dimension(100, screen.height-160));
		add(RightSection, BorderLayout.EAST);
		RightSection.setLayout(null);
		
		right1 = new JPanel();
		right1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		right1.setBounds(5, 5, 90, 200);
		RightSection.add(right1);
		right1.setLayout(null);
		
	}
	public void update(Observable oObservable, Object oObject) {
		
		oUserInfo = ((ObjectOPD)oObservable); // cast
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == Button431){
			String [] input43 = new String [30];
			input43[0]="10673";
			input43[1]=oUserInfo.GetPtHN().trim();
			input43[2]="";input43[3]="";input43[4]="5";input43[5]="1";input43[6]="0";input43[7]="0";
			input43[8]="0";input43[9]="0";input43[10]="0";input43[11]="0";input43[12]="0";input43[13]="0";input43[14]="2";
			input43[15]="2";input43[16]="0";input43[17]="0";input43[18]="0";input43[19]="0";input43[20]="0";input43[21]="4";
			input43[22]="0";input43[23]="0";input43[24]="0";input43[25]="999999";input43[26]="";input43[27]="";input43[28]="000";
			input43[29]=Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate());
			
			String [] input43c = new String [30];
			input43c[0]="HOSPCODE";
			input43c[1]="PID";
			input43c[2]="SEQ";input43c[3]="DATE_SERV";input43c[4]="DENTTYPE";input43c[5]="SERVPLACE";input43c[6]="PTEETH";input43c[7]="PCARIES";
			input43c[8]="PFILLING";input43c[9]="PEXTRACT";input43c[10]="DTEETH";input43c[11]="DCARIES";input43c[12]="DFILLING";input43c[13]="DEXTRACT";input43c[14]="NEED_FLUORIDE";
			input43c[15]="NEED_SCALING";input43c[16]="NEED_SEALANT";input43c[17]="NEED_PFILLING";input43c[18]="NEED_DFILLING";input43c[19]="NEED_PEXTRACT";input43c[20]="NEED_DEXTRACT";input43c[21]="NPROSTHESIS";
			input43c[22]="PERM_PERM";input43c[23]="PERM_PROS";input43c[24]="PROS_PROS";input43c[25]="GUM";input43c[26]="SCHOOLTYPE";input43c[27]="CLASS";input43c[28]="PROVIDER";
			input43c[29]="D_UPDATE";
			
			
			int status43_insert=0;
			Connection con43;
			PreparedStatement stmt43,stmt431,stmt432,stmt44;
			ResultSet rs43,rs44 ;
			String sql_getHN43="select pid from dental43 where  pid='"+oUserInfo.GetPtHN().trim()+"' and date_serv='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
			try {
				con43 = new DBmanager().getConnMySql();				 
				String sql_getdhis="select * from dhis where  hn='"+oUserInfo.GetPtHN().trim()+"' and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
				stmt44 = con43.prepareStatement(sql_getdhis);
				rs44 = stmt44.executeQuery();
				while( rs44.next() ){
					input43[1]=rs44.getString(3).trim();
					
					String date=rs44.getString(1).trim();;
					String d= date.substring(8);
					String m= date.substring(5, 7);
					String y= date.substring(0, 4);
					
					input43[2]=rs44.getString(3).trim()+y+m+d;
					input43[3]=y+"-"+m+"-"+d;
					if(rs44.getString(6) !=null){
						input43[4]=rs44.getString(6).trim();
					}
					if(rs44.getString(5) !=null){
						input43[5]=rs44.getString(5).trim();
					}
					
					int pt=0,pc=0,pf=0,pe=0,npf=0,npe=0;
					int dt=0,dc=0,df=0,de=0,ndf=0,nde=0;
					int nsealant=0;
					for(int k=9;k<41;k++){
						if(rs44.getString(k) !=null){
						if(rs44.getString(k).trim().equals("0")){
							pt++;
						}
						else if(rs44.getString(k).trim().equals("6")){
							pc++;
							 
						}
						else if(rs44.getString(k).trim().equals("1") || rs44.getString(k).trim().equals("2")){
							npf++;
							 
						}
						else if(rs44.getString(k).trim().equals("3") || rs44.getString(k).trim().equals("5")){
							pf++;
							 
						}
						else if(rs44.getString(k).trim().equals("4") || rs44.getString(k).trim().equals("10")){
							pe++;
						}
						else if(rs44.getString(k).trim().equals("7")){
							npe++;
							 
						}
						
						else if(rs44.getString(k).trim().equals("A") || rs44.getString(k).trim().equals("a")){
							dt++;
						}
						else if(rs44.getString(k).trim().equals("G") || rs44.getString(k).trim().equals("g")){
							dc++;
							 
						}
						else if(rs44.getString(k).trim().equals("B") || rs44.getString(k).trim().equals("C") || rs44.getString(k).trim().equals("b") || rs44.getString(k).trim().equals("c")){
							ndf++;
							 
						}
						else if(rs44.getString(k).trim().equals("D") || rs44.getString(k).trim().equals("F") || rs44.getString(k).trim().equals("d") || rs44.getString(k).trim().equals("f")){
							df++;
							 
						}
						else if(rs44.getString(k).trim().equals("E") || rs44.getString(k).trim().equals("e")){
							de++;
						}
						else if(rs44.getString(k).trim().equals("H") || rs44.getString(k).trim().equals("h")){
							nde++;
							 
						}
						}
						if(k==9 || k==10 || k==11 || k==22 || k==23 || k==24 || k==25 || k==26 || k==27 || k==39 || k==40 || k==41){
							if(rs44.getString(k).trim().equals("0")){
								nsealant++;
							}
						}
					}
					 
					
					input43[6]=String.valueOf(pt+pc+pf+npf+npe);
					input43[7]=String.valueOf(pc+npf);
					input43[8]=String.valueOf(pf);
					input43[9]=String.valueOf(pe);
					
					input43[10]=String.valueOf(dt+dc+df+ndf+nde);			
					input43[11]=String.valueOf(dc+ndf);
					input43[12]=String.valueOf(df);
					input43[13]=String.valueOf(de);
					
					
					//need fluoride
					if(rs44.getString(45) !=null){
						input43[14]=rs44.getString(45).trim();
					}
					if(rs44.getString(46) !=null){
						input43[15]=rs44.getString(46).trim();	
					}
					
					String sealant="0";
					if(checkAge()==3){
						sealant=String.valueOf(nsealant);
					}
					 
					input43[16]=sealant;
					 
					input43[17]=String.valueOf(npf);
					input43[18]=String.valueOf(ndf);				
					input43[19]=String.valueOf(npe);
					input43[20]=String.valueOf(nde);
					//npros
					if(rs44.getString(47) !=null){
						input43[21]=rs44.getString(47).trim();
					}
					//occlusion
					if(rs44.getString(42) !=null){
						input43[22]=rs44.getString(42).trim();	
					}
					if(rs44.getString(43) !=null){
						input43[23]=rs44.getString(43).trim();		
					}
					if(rs44.getString(44) !=null){
						input43[24]=rs44.getString(44).trim();
					}
					//gum
					if(rs44.getString(41) !=null){
						input43[25]=rs44.getString(41).trim();
					}
					String st="",cl="";
					if(rs44.getString(7) !=null){
					if(rs44.getString(7).trim().equals("1")){
						st=rs44.getString(7).trim();
						if(rs44.getString(8) !=null){
						cl=rs44.getString(8).trim();
						}
					}
					else if(rs44.getString(7).trim().equals("2")){
						st=rs44.getString(7).trim();
						if(rs44.getString(8) !=null){
						cl=rs44.getString(8).trim();
						}
					}
					else{
						st="";
						cl="";
					}
					}
					//schooltype
					input43[26]=st;
					//class
					input43[27]=cl;
					if(rs44.getString(49) !=null){
					input43[28]=rs44.getString(49).trim();
					}
				}
				
				rs44.close();
				stmt44.close();
				
				stmt43 = con43.prepareStatement(sql_getHN43);
				//System.out.println(stmt6 );
				rs43 = stmt43.executeQuery();
				
				while( rs43.next() ){
					//System.out.println("***"+rs43.getString(1));
					//String visitdate43=rs43.getString(1).trim();
					String hn43=rs43.getString(1).trim();
					//if(visitdate43.equals(oUserInfo.GetPtVisitdate()) && hn43.equals(oUserInfo.GetPtHN().trim())){
					if(rs43.getString(1) !=null){
						//status43_insert=1;
						 
						 for(int h=0;h<input43c.length;h++){
							//System.out.println("Value input "+h+"-"+input43[h]);
							//String sql_update43 = "update dental43 set "+input43c[h]+"='"+input43[h]+"' where pid='"+oUserInfo.GetPtHN().trim()+"' ";
								
							//int rs_save431 = ((java.sql.Statement) stmt431).executeUpdate(sql_update43);
							String sql_del43 = "delete from dental43  where pid='"+oUserInfo.GetPtHN().trim()+"' and date_serv='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"' ";
							stmt431 = con43.prepareStatement(sql_del43);
							int rs_save431 =stmt431.executeUpdate();
							stmt431.close();
						}
						 
						 
					}
					 
				}
				if(status43_insert==0){

					String sql_insert43 = "insert into  dental43 ( hospcode,pid,seq,date_serv,denttype,servplace,pteeth"
							+ ",pcaries,pfilling,pextract,dteeth,dcaries,dfilling,dextract,need_fluoride,need_scaling"
							+ ",need_sealant,need_pfilling,need_dfilling,need_pextract,need_dextract,nprosthesis,perm_perm"
							+ ",perm_pros,pros_pros,gum,schooltype,class,provider,d_update ) values "
							+ "('"+input43[0]+"','"+input43[1]+"','"+input43[2]+"','"+input43[3].trim()+"','"+input43[4]+"','"+input43[5]+"',"+input43[6]+","+input43[7]+","
							+ input43[8]+","+input43[9]+","+input43[10]+","+input43[11]+","+input43[12]+","+input43[13]+",'"+input43[14]+"','"+input43[15]+"',"
							+ input43[16]+","+input43[17]+","+input43[18]+","+input43[19]+","+input43[20]+",'"+input43[21]+"',"+input43[22]+","+input43[23]+","
							+ input43[24]+",'"+input43[25]+"','"+input43[26]+"','"+input43[27]+"','"+input43[28]+"','"+Setup.GetDateNow()+"')";
								
					stmt432 = con43.prepareStatement(sql_insert43);
					int rs_save432 = stmt432.executeUpdate();
					stmt432.close();
				}
				
				stmt43.close();
				con43.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int h=0;h<input43.length;h++){
				//System.out.println("Value input "+h+"-"+input43[h]);
			}
			status43_insert=0;
		}
		else if(e.getSource() == Button432){
			String [] input43 = new String [30];
			input43[0]="10673";
			input43[1]=oUserInfo.GetPtHN().trim();
			input43[2]="";input43[3]="";input43[4]="5";input43[5]="2";input43[6]="0";input43[7]="0";
			input43[8]="0";input43[9]="0";input43[10]="0";input43[11]="0";input43[12]="0";input43[13]="0";input43[14]="2";
			input43[15]="2";input43[16]="0";input43[17]="0";input43[18]="0";input43[19]="0";input43[20]="0";input43[21]="4";
			input43[22]="0";input43[23]="0";input43[24]="0";input43[25]="999999";input43[26]="";input43[27]="";input43[28]="000";
			input43[29]=Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate());
			
			String [] input43c = new String [30];
			input43c[0]="HOSPCODE";
			input43c[1]="PID";
			input43c[2]="SEQ";input43c[3]="DATE_SERV";input43c[4]="DENTTYPE";input43c[5]="SERVPLACE";input43c[6]="PTEETH";input43c[7]="PCARIES";
			input43c[8]="PFILLING";input43c[9]="PEXTRACT";input43c[10]="DTEETH";input43c[11]="DCARIES";input43c[12]="DFILLING";input43c[13]="DEXTRACT";input43c[14]="NEED_FLUORIDE";
			input43c[15]="NEED_SCALING";input43c[16]="NEED_SEALANT";input43c[17]="NEED_PFILLING";input43c[18]="NEED_DFILLING";input43c[19]="NEED_PEXTRACT";input43c[20]="NEED_DEXTRACT";input43c[21]="NPROSTHESIS";
			input43c[22]="PERM_PERM";input43c[23]="PERM_PROS";input43c[24]="PROS_PROS";input43c[25]="GUM";input43c[26]="SCHOOLTYPE";input43c[27]="CLASS";input43c[28]="PROVIDER";
			input43c[29]="D_UPDATE";
			
			
			int status43_insert=0;
			Connection con43;
			PreparedStatement stmt43,stmt431,stmt432,stmt44;
			ResultSet rs43,rs44 ;
			String sql_getHN43="select pid from dental43 where  pid='"+oUserInfo.GetPtHN().trim()+"'  and date_serv='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
			//String sql_getHN43="select pid from dental43 where date_serv='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"' and pid='"+oUserInfo.GetPtHN().trim()+"'";
			
			try {
				con43 = new DBmanager().getConnMySql();			 
				String sql_getdhis="select * from dhis where hn='"+oUserInfo.GetPtHN().trim()+"'  and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
				//String sql_getdhis="select * from dhis where visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"' and hn='"+oUserInfo.GetPtHN().trim()+"'";
				stmt44 = con43.prepareStatement(sql_getdhis);
				rs44 = stmt44.executeQuery();
				while( rs44.next() ){
					input43[1]=rs44.getString(3).trim();
					
					String date=rs44.getString(1).trim();;
					String d= date.substring(8);
					String m= date.substring(5, 7);
					String y= date.substring(0, 4);
					
					input43[2]=rs44.getString(3).trim()+y+m+d;
					input43[3]=y+"-"+m+"-"+d;
					if(rs44.getString(6) !=null){
						input43[4]=rs44.getString(6).trim();
					}
					if(rs44.getString(5) !=null){
						input43[5]=rs44.getString(5).trim();
					}
					
					int pt=0,pc=0,pf=0,pe=0,npf=0,npe=0;
					int dt=0,dc=0,df=0,de=0,ndf=0,nde=0;
					int nsealant=0;
					for(int k=9;k<41;k++){
						if(rs44.getString(k) !=null){
						if(rs44.getString(k).trim().equals("0")){
							pt++;
						}
						else if(rs44.getString(k).trim().equals("6")){
							pc++;
						}
						else if(rs44.getString(k).trim().equals("1") || rs44.getString(k).trim().equals("2")){
							npf++;
						}
						else if(rs44.getString(k).trim().equals("3") || rs44.getString(k).trim().equals("5")){
							pf++;
						}
						else if(rs44.getString(k).trim().equals("4") || rs44.getString(k).trim().equals("10")){
							pe++;
						}
						else if(rs44.getString(k).trim().equals("7")){
							npe++;
						}
						
						else if(rs44.getString(k).trim().equals("A") || rs44.getString(k).trim().equals("a")){
							dt++;
						}
						else if(rs44.getString(k).trim().equals("G") || rs44.getString(k).trim().equals("g")){
							dc++;
						}
						else if(rs44.getString(k).trim().equals("B") || rs44.getString(k).trim().equals("C") || rs44.getString(k).trim().equals("b") || rs44.getString(k).trim().equals("c")){
							ndf++;
						}
						else if(rs44.getString(k).trim().equals("D") || rs44.getString(k).trim().equals("F") || rs44.getString(k).trim().equals("d") || rs44.getString(k).trim().equals("f")){
							df++;
						}
						else if(rs44.getString(k).trim().equals("E") || rs44.getString(k).trim().equals("e")){
							de++;
						}
						else if(rs44.getString(k).trim().equals("H") || rs44.getString(k).trim().equals("h")){
							nde++;
						}
						}
						if(k==9 || k==10 || k==11 || k==22 || k==23 || k==24 || k==25 || k==26 || k==27 || k==39 || k==40 || k==41){
							if(rs44.getString(k).trim().equals("0")){
								nsealant++;
							}
						}
					}
					 
					
					input43[6]=String.valueOf(pt+pc+pf+npf+npe);
					input43[7]=String.valueOf(pc+npf);
					input43[8]=String.valueOf(pf);
					input43[9]=String.valueOf(pe);
					
					input43[10]=String.valueOf(dt+dc+df+ndf+nde);			
					input43[11]=String.valueOf(dc+ndf);
					input43[12]=String.valueOf(df);
					input43[13]=String.valueOf(de);
					
					
					//need fluoride
					if(rs44.getString(45) !=null){
						input43[14]=rs44.getString(45).trim();
					}
					if(rs44.getString(46) !=null){
						input43[15]=rs44.getString(46).trim();	
					}
					
					String sealant="0";
					if(checkAge()==3){
						sealant=String.valueOf(nsealant);
					}
					 
					input43[16]=sealant;
					 
					input43[17]=String.valueOf(npf);
					input43[18]=String.valueOf(ndf);				
					input43[19]=String.valueOf(npe);
					input43[20]=String.valueOf(nde);
					//npros
					if(rs44.getString(47) !=null){
						input43[21]=rs44.getString(47).trim();
					}
					//occlusion
					if(rs44.getString(42) !=null){
						input43[22]=rs44.getString(42).trim();	
					}
					if(rs44.getString(43) !=null){
						input43[23]=rs44.getString(43).trim();		
					}
					if(rs44.getString(44) !=null){
						input43[24]=rs44.getString(44).trim();
					}
					//gum
					if(rs44.getString(41) !=null){
						input43[25]=rs44.getString(41).trim();
					}
					String st="",cl="";
					if(rs44.getString(7) !=null){
					if(rs44.getString(7).trim().equals("1")){
						st=rs44.getString(7).trim();
						if(rs44.getString(8) !=null){
						cl=rs44.getString(8).trim();
						}
					}
					else if(rs44.getString(7).trim().equals("2")){
						st=rs44.getString(7).trim();
						if(rs44.getString(8) !=null){
						cl=rs44.getString(8).trim();
						}
					}
					else{
						st="";
						cl="";
					}
					}
					//schooltype
					input43[26]=st;
					//class
					input43[27]=cl;
					if(rs44.getString(49) !=null){
					input43[28]=rs44.getString(49).trim();
					}
				}
				
				rs44.close();
				stmt44.close();
				
				stmt43 = con43.prepareStatement(sql_getHN43);
				//System.out.println(stmt6 );
				rs43 = stmt43.executeQuery();
				
				while( rs43.next() ){
					//String visitdate43=rs43.getString(1).trim();
					//String hn43=rs43.getString(2).trim();
					//if(visitdate43.equals(oUserInfo.GetPtVisitdate()) && hn43.equals(oUserInfo.GetPtHN().trim())){
					if(rs43.getString(1) !=null){
						//status43_insert=1;
						 
						 for(int h=0;h<input43c.length;h++){
							//System.out.println("Value input "+h+"-"+input43[h]);
							//String sql_update43 = "update dental43 set "+input43c[h]+"='"+input43[h]+"' where pid='"+oUserInfo.GetPtHN().trim()+"' ";
								
							//int rs_save431 = ((java.sql.Statement) stmt431).executeUpdate(sql_update43);
							String sql_del43 = "delete from dental43  where pid='"+oUserInfo.GetPtHN().trim()+"'  and date_serv='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
							stmt431 = con43.prepareStatement(sql_del43);
							int rs_save431 = stmt431.executeUpdate();
							stmt431.close();
						}
						 
						 
					}
					 
				}
				if(status43_insert==0){
					 
					String sql_insert43 = "insert into  dental43 ( hospcode,pid,seq,date_serv,denttype,servplace,pteeth"
							+ ",pcaries,pfilling,pextract,dteeth,dcaries,dfilling,dextract,need_fluoride,need_scaling"
							+ ",need_sealant,need_pfilling,need_dfilling,need_pextract,need_dextract,nprosthesis,perm_perm"
							+ ",perm_pros,pros_pros,gum,schooltype,class,provider,d_update ) values "
							+ "('"+input43[0]+"','"+input43[1]+"','"+input43[2]+"','"+input43[3].trim()+"','"+input43[4]+"','"+input43[5]+"',"+input43[6]+","+input43[7]+","
							+ input43[8]+","+input43[9]+","+input43[10]+","+input43[11]+","+input43[12]+","+input43[13]+",'"+input43[14]+"','"+input43[15]+"',"
							+ input43[16]+","+input43[17]+","+input43[18]+","+input43[19]+","+input43[20]+",'"+input43[21]+"',"+input43[22]+","+input43[23]+","
							+ input43[24]+",'"+input43[25]+"','"+input43[26]+"','"+input43[27]+"','"+input43[28]+"','"+Setup.GetDateNow()+"')";
								
					stmt432 = con43.prepareStatement(sql_insert43);
					int rs_save432 = stmt432.executeUpdate(sql_insert43);
					stmt432.close();
				}
				
				stmt43.close();
				con43.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int h=0;h<input43.length;h++){
				//System.out.println("Value input "+h+"-"+input43[h]);
			}
			status43_insert=0;
		}
		//end 431
		else if(e.getSource() == ButtonTreatment){
			String doctor="",toothno="",diag="",treatment="",date="";
			if(TextField_doctor.getText().trim().equals("")){
    			doctor="047";
    		}else{
    			 
    			doctor=TextField_doctor.getText().trim();
    		}
			if(TextField_diag.getText().trim().equals("")){
    			diag="";
    		}else{
    			 
    			diag=TextField_diag.getText().trim();
    		}
			if(TextField_toothno.getText().trim().equals("")){
    			toothno="01";
    		}else{
    			 
    			toothno=TextField_toothno.getText().trim();
    		}
			if(TextField_tx.getText().trim().equals("")){
    			treatment="";
    		}else{
    			 
    			treatment=TextField_tx.getText().trim();
    		}
			TextField_date.setText(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()));
    		date=TextField_date.getText().trim();
    		 
			//update data
    		Connection con6;
    		PreparedStatement stmt6,stmt2,stmt3;
    		ResultSet rs6,rs2;
    		int data_status=0;
    		try {
					con6 = new DBmanager().getConnMySql();
					 String sql_check_in_database="select hn from dental_activity where hn='"+oUserInfo.GetPtHN()+"' and toothno='"+toothno+"' and treatment='"+treatment+"' and diag='"+diag+"' and doctor='"+doctor+"' and visitdatetime='"+date+"'";
					 stmt2 = con6.prepareStatement(sql_check_in_database);			
					 rs2 = stmt2.executeQuery();
					 System.out.println("HN..--"+oUserInfo.GetPtHN()+" "+date+" "+toothno+" "+diag+" "+treatment+" "+doctor);
					 while( rs2.next() ){
						 //System.out.println("--"+rs2.getString(1));
						 if(rs2.getString(1) !=null){
							 //data_status=1;
							// String sql_update_data="update dental_activity set  toothno='"+toothno+"',treatment='"+treatment+"',diag='"+diag+"',doctor='"+doctor+"',visitdatetime='"+date+"' where hn='"+hn+"' ";
					    	 	
							// int rs_update =  stmt3.executeUpdate(sql_update_data);
					    	 
					    	 String sql_del_data="delete from dental_activity where hn='"+oUserInfo.GetPtHN()+"' and toothno='"+toothno+"' and treatment='"+treatment+"' and diag='"+diag+"' and doctor='"+doctor+"' and visitdatetime='"+date+"'";
					    	 stmt3 = con6.prepareStatement(sql_del_data);
					    	 int rs_update =  stmt3.executeUpdate();
							 stmt3.close();
							 
						 }
					 }
					 if(data_status==0){
						 String sql_insert_data="insert into dental_activity (visitdatetime,toothno,treatment,diag,doctor,amt,vn,typeplace,hn) values ('"+date+"','"+toothno+"','"+treatment+"','"+diag+"','"+doctor+"','','','2','"+oUserInfo.GetPtHN()+"')";
						 stmt6 = con6.prepareStatement(sql_insert_data);	
						 int rs_insert = stmt6.executeUpdate();
						 stmt6.close();
					 }
					 stmt2.close();
					 data_status=0;
	    			
	    		
	    			con6.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		
    		TextField_tx.setText("");
    		TextField_doctor.setText("");
    		TextField_diag.setText("");
    		TextField_toothno.setText("");
    		TextField_date.setText(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()));
    		tableTx.setModel(fetchDataTx());
			TableColumnModel columnModelTx = tableTx.getColumnModel();
			columnModelTx.getColumn(0).setPreferredWidth(30);
			columnModelTx.getColumn(1).setPreferredWidth(100);
			columnModelTx.getColumn(2).setPreferredWidth(80);
			columnModelTx.getColumn(3).setPreferredWidth(80);
			columnModelTx.getColumn(4).setPreferredWidth(80);
			columnModelTx.getColumn(5).setPreferredWidth(50);
			 
			((DefaultTableModel)tableTx.getModel()).fireTableDataChanged(); 
		}
		else if(e.getSource() == ButtonDelTx){
			//System.out.println(TextField_toothno.getText().trim()+"-"+TextField_tx.getText().trim()+"-"+TextField_diag.getText().trim()+"-"+TextField_doctor.getText().trim()+"-"+TextField_date.getText().trim());
			Connection con6;
    		PreparedStatement stmt6;   		
    		try {
				con6 = new DBmanager().getConnMySql();
				String sql_del_tx="delete from dental_activity where hn='"+oUserInfo.GetPtHN()+"' and toothno='"+TextField_toothno.getText().trim()+"' and treatment='"+TextField_tx.getText().trim()+"' and diag='"+TextField_diag.getText().trim()+"' and doctor='"+TextField_doctor.getText().trim()+"' and visitdatetime='"+TextField_date.getText().trim()+"'";
				 stmt6 = con6.prepareStatement(sql_del_tx);	
				 int rs_insert = stmt6.executeUpdate();
				 stmt6.close();
				 
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		TextField_tx.setText("");
    		TextField_doctor.setText("");
    		TextField_diag.setText("");
    		TextField_toothno.setText("");
    		TextField_date.setText(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()));
    		tableTx.setModel(fetchDataTx());
			TableColumnModel columnModelTx = tableTx.getColumnModel();
			columnModelTx.getColumn(0).setPreferredWidth(30);
			columnModelTx.getColumn(1).setPreferredWidth(100);
			columnModelTx.getColumn(2).setPreferredWidth(80);
			columnModelTx.getColumn(3).setPreferredWidth(80);
			columnModelTx.getColumn(4).setPreferredWidth(80);
			columnModelTx.getColumn(5).setPreferredWidth(50);
			 
			((DefaultTableModel)tableTx.getModel()).fireTableDataChanged(); 
		}
		else if(e.getSource() == ButtonClearTx){
			oUserInfo.setPtHN("");
			oUserInfo.setPtLabel("");
			oUserInfo.setPtCID("");
			TextField_tx.setText("");
			TextField_id.setText("");
    		TextField_doctor.setText("");
    		TextField_diag.setText("");
    		TextField_toothno.setText("");
    		TextField_date.setText(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()));
    		getDataClearTx();
		}
		else if(e.getSource() == ButtonSID){
			oUserInfo.setPtHN("");
			oUserInfo.setPtVN("");
			oUserInfo.setPtLabel("");
			oUserInfo.setPtCID("");
			String id_se=TextField_id.getText().trim();
			//System.out.println("..."+id_se);
			if(id_se.length() !=13){
				JOptionPane.showMessageDialog(null,"Incorrect ID"+id_se ,"CID",JOptionPane.WARNING_MESSAGE); 
				
			}else{
				Connection conn;
				PreparedStatement stmt;
				ResultSet rs;
				String query="select  firstname, lastname, hn,BirthDateTime,sex,maritalstatus,initialnamecode from in_view_pt where ref=?   and reftype='01' and suffix='0' "; 
				try {
					conn = new DBmanager().getConnMSSql();
					stmt = conn.prepareStatement(query);
					stmt.setString(1, id_se); 
					//stmt.setString(2, "1100");
					rs = stmt.executeQuery();
					int p=1;
					while (rs.next()) {
						String sex_pt="",mar_pt="";
						String initname="",right="";
						Date birthday=rs.getDate(4);
						String age_pt=Setup.AgeInAll(birthday.toString()).trim();
						if(rs.getString(7)==null || rs.getString(7).equals("1") || rs.getString(7).equals("2") || rs.getString(7).equals("3") || rs.getString(7).equals("127") || rs.getString(7).equals("128")){
							sex_pt=rs.getString(5).trim();
							mar_pt=rs.getString(6).trim();				 
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
								if(rs.getString(7).trim().equals(InApp.initname_indb[i][0])){
									initname=InApp.initname_indb[i][1];
								}
								//System.out.println(i+1+". "+labunitname[i][0]);
							}
							//initname=rs.getString(10).trim();
						}
						 
					
						final Vector<String> vstring = new Vector<String>();
						vstring.add(" "+Integer.toString(p) );
						//vstring.add(rs.getString(1)+": "+initname+" "+rs.getString(2).substring(1)+" "+rs.getString(3).substring(1));
						vstring.add(rs.getString(1));
						vstring.add(" "+initname+" "+rs.getString(2).substring(1).trim()+" "+rs.getString(3).substring(1).trim());
						String name=" "+initname+" "+rs.getString(1).substring(1).trim()+" "+rs.getString(2).substring(1).trim();
						 
						oUserInfo.setPtHN(rs.getString(3));
						oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+age_pt+"     :สิทธิ.. ");
						oUserInfo.setPtName(name);
						oUserInfo.setPtAge(age_pt);
						oUserInfo.setPtCID(id_se);

						p++;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				getDataTx();
			}
		}
		else if(e.getSource() == ButtonSaveExam1){
			if(TextField_EdulevelB.getText().trim().equals("")){
    			classlevel="";
    		}else{
    			 
    			classlevel=TextField_EdulevelB.getText().trim();
    		}
			//update data
    		Connection con611;
    		PreparedStatement stmt611,stmt612,stmt613;
    		try {
				con611 = new DBmanager().getConnMySql();			
    			String sql_updatepart1 = "update dhis set   vn='"+oUserInfo.GetPtVN()+"', cid='"+oUserInfo.GetPtCID()+"', typeserv='"+servplace+"', typept='"+dentaltype+"', edu='"+schooltype+"', edulevel='"+classlevel+"', d_update='"+Setup.GetDateNow()+"' where hn='"+oUserInfo.GetPtHN()+"' and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
    			stmt611 = con611.prepareStatement(sql_updatepart1);
    			int rs_update = stmt611.executeUpdate();
    			stmt611.close();
    			
    			if(!checkValueTable()){
        			JOptionPane.showMessageDialog(this,"ค่าที่กรอกไม่ถูกต้อง !!","Value Error",JOptionPane.ERROR_MESSAGE);
     
        		}else{
        			for(int m=0;m<16;m++){
            			u[(m)]=tableExam1.getValueAt(0,m).toString().trim();           			             			
            			String sql_updateU = "update dhis set  u"+m+"='"+u[m]+"' where hn='"+oUserInfo.GetPtHN()+"'  and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
            			stmt612 = con611.prepareStatement(sql_updateU);
            			int rs_updateU = stmt612.executeUpdate();
            			stmt612.close();
            			//System.out.println("u"+(m)+".."+u[(m)]);
            		}
            		for(int m=0;m<16;m++){
            			l[(m)]=tableExam1.getValueAt(1,m).toString().trim();           			
            			String sql_updateL = "update dhis set  l"+m+"='"+l[m]+"' where hn='"+oUserInfo.GetPtHN()+"' and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"' ";
            			stmt613 = con611.prepareStatement(sql_updateL);
            			int rs_updateU = stmt613.executeUpdate();
            			stmt613.close();
            		//	System.out.println("l"+(m)+".."+l[(m)]);
            		}
        		}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		clearDataPt43_1();   
    		///section2
    		if(TextField_occ1.getText().trim().equals("")){
    			perm_perm=0;
    		}else{
    			perm_perm= Integer.parseInt( TextField_occ1.getText().trim());
    		}
    		if(TextField_occ2.getText().trim().equals("")){
    			perm_pros=0;
    		}else{
    			perm_pros= Integer.parseInt( TextField_occ2.getText().trim());
    		}
    		if(TextField_occ3.getText().trim().equals("")){
    			pros_pros=0;
    		}else{
    			pros_pros= Integer.parseInt( TextField_occ3.getText().trim());
    		}
    		 
    		
    		if(TextField_provider.getText().trim().equals("")){
    			provider="047";
    		}else{
    			provider= TextField_provider.getText().trim();
    		}
    		
    		//perio
    		gum="999999";
    		if(!checkValueTableGum()){
    			JOptionPane.showMessageDialog(this,"ค่าที่กรอกไม่ถูกต้อง !!","Value Error",JOptionPane.ERROR_MESSAGE);
 
    		}else{
    			String ur="9",ua="9",ul="9",lr="9",la="9",ll="9";
    			ur=tableExam2.getValueAt(0,0).toString().trim();
    			ua=tableExam2.getValueAt(0,1).toString().trim();
    			ul=tableExam2.getValueAt(0,2).toString().trim();
    			lr=tableExam2.getValueAt(1,2).toString().trim();
    			la=tableExam2.getValueAt(1,1).toString().trim();
    			ll=tableExam2.getValueAt(1,0).toString().trim();
    			gum=ur+ua+ul+ll+la+lr;
    			
    			//update data
        		Connection con615;
        		PreparedStatement stmt615;
        		try {
					con615 = new DBmanager().getConnMySql();       			       			
        			String sql_updatepart2 = "update dhis set  perio='"+gum+"', occpetope='"+perm_perm+"', occpetopr='"+perm_pros+"', occprtopr='"+pros_pros+"', fluneed='"+need_fluoride+"', sneed='"+need_scaling+"', prosneed='"+nprosthesis+"', provider='"+provider+"' where hn='"+oUserInfo.GetPtHN()+"' and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"' ";
        			stmt615 = con615.prepareStatement(sql_updatepart2);
        			int rs_update = stmt615.executeUpdate();
        			stmt615.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
    		clearDataPt43_2();   

		}
		else if(e.getSource() == ButtonSaveExam3){
			if(TextField_EdulevelB.getText().trim().equals("")){
    			classlevel="";
    		}else{
    			 
    			classlevel=TextField_EdulevelB.getText().trim();
    		}
			//update data
    		Connection con611;
    		PreparedStatement stmt611,stmt612,stmt613;
    		try {
				con611 = new DBmanager().getConnMySql();   			   			
    			String sql_updatepart1 = "update dhis set   vn='0000', cid='"+oUserInfo.GetPtCID()+"', typeserv='2', typept='"+dentaltype+"', edu='"+schooltype+"', edulevel='"+classlevel+"', d_update='"+Setup.GetDateNow()+"' where hn='"+oUserInfo.GetPtHN()+"'  and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
    			stmt611 = con611.prepareStatement(sql_updatepart1);
    			int rs_update = stmt611.executeUpdate();
    			stmt611.close();
    			
    			if(!checkValueTable()){
        			JOptionPane.showMessageDialog(this,"ค่าที่กรอกไม่ถูกต้อง !!","Value Error",JOptionPane.ERROR_MESSAGE);
     
        		}else{
        			for(int m=0;m<16;m++){
            			u[(m)]=tableExam1.getValueAt(0,m).toString().trim();            			            			
            			String sql_updateU = "update dhis set  u"+m+"='"+u[m]+"' where hn='"+oUserInfo.GetPtHN()+"' and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"' ";
            			stmt612 = con611.prepareStatement(sql_updateU);
            			int rs_updateU = stmt612.executeUpdate();
            			stmt612.close();
            			//System.out.println("u"+(m)+".."+u[(m)]);
            		}
            		for(int m=0;m<16;m++){
            			l[(m)]=tableExam1.getValueAt(1,m).toString().trim();
            			 
            			
            			String sql_updateL = "update dhis set  l"+m+"='"+l[m]+"' where hn='"+oUserInfo.GetPtHN()+"'  and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
            			stmt613 = con611.prepareStatement(sql_updateL);
            			int rs_updateU = stmt613.executeUpdate();
            			stmt613.close();
            		//	System.out.println("l"+(m)+".."+l[(m)]);
            		}
        		}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		clearDataPt43_1();
			if(TextField_occ1.getText().trim().equals("")){
    			perm_perm=0;
    		}else{
    			perm_perm= Integer.parseInt( TextField_occ1.getText().trim());
    		}
    		if(TextField_occ2.getText().trim().equals("")){
    			perm_pros=0;
    		}else{
    			perm_pros= Integer.parseInt( TextField_occ2.getText().trim());
    		}
    		if(TextField_occ3.getText().trim().equals("")){
    			pros_pros=0;
    		}else{
    			pros_pros= Integer.parseInt( TextField_occ3.getText().trim());
    		}
    		 
    		
    		if(TextField_provider.getText().trim().equals("")){
    			provider="047";
    		}else{
    			provider= TextField_provider.getText().trim();
    		}
    		
    		//perio
    		gum="999999";
    		if(!checkValueTableGum()){
    			JOptionPane.showMessageDialog(this,"ค่าที่กรอกไม่ถูกต้อง !!","Value Error",JOptionPane.ERROR_MESSAGE);
 
    		}else{
    			String ur="9",ua="9",ul="9",lr="9",la="9",ll="9";
    			ur=tableExam2.getValueAt(0,0).toString().trim();
    			ua=tableExam2.getValueAt(0,1).toString().trim();
    			ul=tableExam2.getValueAt(0,2).toString().trim();
    			lr=tableExam2.getValueAt(1,2).toString().trim();
    			la=tableExam2.getValueAt(1,1).toString().trim();
    			ll=tableExam2.getValueAt(1,0).toString().trim();
    			gum=ur+ua+ul+ll+la+lr;
    			
    			//update data
        		Connection con615;
        		PreparedStatement stmt615;
        		try {
					con615 = new DBmanager().getConnMySql();       			         			
        			String sql_updatepart2 = "update dhis set  perio='"+gum+"', occpetope='"+perm_perm+"', occpetopr='"+perm_pros+"', occprtopr='"+pros_pros+"', fluneed='"+need_fluoride+"', sneed='"+need_scaling+"', prosneed='"+nprosthesis+"', provider='"+provider+"' where hn='"+oUserInfo.GetPtHN()+"' and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
        			stmt615 = con615.prepareStatement(sql_updatepart2);
        			int rs_update = stmt615.executeUpdate();
        			stmt615.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
    		clearDataPt43_2();
		}
		else if(e.getSource()instanceof JRadioButton){
			if(e.getActionCommand().trim().equals("ผู้สูงอายุ")){
	    		
    			dentaltype="4";		
    		}
			else if(e.getActionCommand().trim().equals("กลุ่มอื่นๆ")){
	    		
    			dentaltype="5";
    		}
    		else if(e.getActionCommand().trim().equals("เด็กวัยเรียน")){
        		
    			dentaltype="3";
    		}
    		else if(e.getActionCommand().trim().equals("เด็กก่อนวัยเรียน")){
        		
    			dentaltype="2";
    		}
    		else if(e.getActionCommand().trim().equals("หญิงตั้งครรภ์")){
        		
    			dentaltype="1";
    		}
    		else if(e.getActionCommand().trim().equals("ศพด.")){   	    		
    			schooltype="1";		
    		}
    		else if(e.getActionCommand().trim().equals("ประถม-รัฐบาล")){   	    		
				schooltype="2";		
    		}
			else if(e.getActionCommand().trim().equals("ประถม-เทศบาล")){   	    		
				schooltype="3";		
    		}
			else if(e.getActionCommand().trim().equals("ประถม-ท้องถิ่น")){   	    		
				schooltype="4";		
    		}
			else if(e.getActionCommand().trim().equals("ประถม-เอกชน")){   	    		
				schooltype="5";		
    		}
			else if(e.getActionCommand().trim().equals("มัธยม-รัฐบาล")){   	    		
				schooltype="6";		
    		}
			else if(e.getActionCommand().trim().equals("มัธยม-เทศบาล")){   	    		
				schooltype="7";		
    		}
			else if(e.getActionCommand().trim().equals("มัธยม-ท้องถิ่น")){   	    		
				schooltype="8";		
    		}
			else if(e.getActionCommand().trim().equals("มัธยม-เอกชน")){   	    		
				schooltype="9";		
    		}
			else if(e.getActionCommand().trim().equals("ไม่เลือก")){   	    		
				schooltype="0";		
    		}
			else if(e.getActionCommand().trim().equals("ต้องใส่ฟันบนและล่าง")){   
				nprosthesis=1;
			}
			else if(e.getActionCommand().trim().equals("ต้องใส่ฟันบน")){   
				nprosthesis=2;
			}
			else if(e.getActionCommand().trim().equals("ต้องใส่ฟันล่าง")){   
				nprosthesis=3;
			}
			else if(e.getActionCommand().trim().equals("ไม่ต้องใส่ฟันเทียม")){   
				nprosthesis=4;
			}
			else if(e.getActionCommand().trim().equals("จำเป็นs")){   
				need_scaling="1";
			}
			else if(e.getActionCommand().trim().equals("ไม่จำเป็นs")){   
				need_scaling="2";
			}
			else if(e.getActionCommand().trim().equals("จำเป็นf")){   
				need_fluoride="1";
			}
			else if(e.getActionCommand().trim().equals("ไม่จำเป็นf")){   
				need_fluoride="2";
			}
		}
	}
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() ==tableTx){

			int row_tableTx = tableTx.rowAtPoint(e.getPoint());
	        int col_tabletx = tableTx.columnAtPoint(e.getPoint());
	        String date=tableTx.getValueAt(row_tableTx,1).toString().trim();
	        String toothno=tableTx.getValueAt(row_tableTx,2).toString().trim();
	        String diag=tableTx.getValueAt(row_tableTx,3).toString().trim();
	        String treatment=tableTx.getValueAt(row_tableTx,4).toString().trim();
	        String doctor=tableTx.getValueAt(row_tableTx,5).toString().trim();
	        
	        TextField_date.setText(date);
	        TextField_toothno.setText(toothno);
	        TextField_diag.setText(diag);
	        TextField_tx.setText(treatment);
	        TextField_doctor.setText(doctor);
	       
	    	//System.out.println(TextField_toothno.getText().trim()+"-"+TextField_tx.getText().trim()+"-"+TextField_diag.getText().trim()+"-"+TextField_doctor.getText().trim()+"-"+TextField_date.getText().trim());
			
		}
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
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
			
 	}
	public void getDataForm(){
		tableOPDform.setModel(fetchDataForm());
		TableColumnModel columnModelOPDform = tableOPDform.getColumnModel();
		columnModelOPDform.getColumn(0).setPreferredWidth(30);
		columnModelOPDform.getColumn(1).setPreferredWidth(0);
		columnModelOPDform.getColumn(1).setMinWidth(0);
		columnModelOPDform.getColumn(1).setMaxWidth(0);
		columnModelOPDform.getColumn(2).setPreferredWidth(370);
		for (int n = 2; n < 5; n++) {	
			columnModelOPDform.getColumn(n+1).setPreferredWidth(0);
			columnModelOPDform.getColumn(n+1).setMinWidth(0);
			columnModelOPDform.getColumn(n+1).setMaxWidth(0);
		}

		((DefaultTableModel)tableOPDform.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataForm(){
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		
		Vector<Vector<String>> dataForm = new Vector<Vector<String>>();
		String sql_formopd = "select form_code,form_name,page,form_name_print,typeofprint  from formopd where type='1' and clinic= '1100'  order by orderpage";
		
		try {
			conn = new DBmanager().getConnMySql();
			stmt = conn.prepareStatement(sql_formopd);
			rs = stmt.executeQuery();
			int p=1;
			while (rs.next()) {
				final Vector<String> vstringForm = new Vector<String>();
				vstringForm.add(" "+Integer.toString(p) );
				vstringForm.add(rs.getString(1).trim());	           
	        	vstringForm.add(rs.getString(2));
	        	if(rs.getString(3) ==null){
	        		vstringForm.add("88");
	        	}else{
	        	vstringForm.add(rs.getString(3).trim());
	        	}
	        	if(rs.getString(4) ==null){
	        		vstringForm.add("");
	        	}else{
	        	vstringForm.add(rs.getString(4).trim());
	        	}
	        	if(rs.getString(5) ==null){
	        		vstringForm.add("1");
	        	}else{
	        	vstringForm.add(rs.getString(5).trim());
	        	}
				dataForm.add(vstringForm);
				p++;
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultTableModel(dataForm, columnNamesOPDform);
	}
	public void formPanel(String informcode,String page,String form_clinic_name){
		mid2.removeAll();
		String formcode=informcode.trim();
		if(formcode.equals("00115")){
			if(oUserInfo.GetPtHN().length() !=7){
				JOptionPane.showMessageDialog(this,"Please select Patient","Patient Error",JOptionPane.ERROR_MESSAGE);

			}else{
				checkExam(oUserInfo.GetPtHN());
				//right Section
				right1.removeAll();
				//Button Print
				ButtonPrintExam1 = new JButton("Print");		 
				ButtonPrintExam1.setBounds(5,10,80,30);		 
				ButtonPrintExam1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						PrintOPD(formcode,"2",page,form_clinic_name);
					}
				});
				
				right1.add(ButtonPrintExam1);
				
				right1.revalidate();
				right1.repaint();
				
				MiddleSection.revalidate();
				MiddleSection.repaint();
				RightSection.revalidate();
				RightSection.repaint();
			}
			 
			
			
		}
		/////////////////
		else if(formcode.equals("00116")){
			exam_status=0;
			System.out.println(oUserInfo.GetPtHN());
			if(oUserInfo.GetPtHN().length() !=7){
				JOptionPane.showMessageDialog(this,"Please select Patient","Patient Error",JOptionPane.ERROR_MESSAGE);

			}else{
				mid2.removeAll();
				checkExam(oUserInfo.GetPtHN());
				
				Panel43 = new JPanel();
				Panel43.setPreferredSize(new Dimension((screen.width*2)/10, 800));
				Panel43.setLayout(null);
				
				JLabel ptType,ptEdu,ptEdulevel,ptExam1,ptExam2,ptExam3,ptExam4;
				String [] ptTypeB={"หญิงตั้งครรภ์","เด็กก่อนวัยเรียน","เด็กวัยเรียน","ผู้สูงอายุ","กลุ่มอื่นๆ"};
				String [] ptEduB={"ศพด.","ประถม-รัฐบาล","ประถม-เทศบาล","ประถม-ท้องถิ่น","ประถม-เอกชน","มัธยม-รัฐบาล","มัธยม-เทศบาล","มัธยม-ท้องถิ่น","มัธยม-เอกชน","ไม่เลือก"};
				String [] ptEdulevelB={"ศพด.1-3","ประถม1-6","มัธยม1-6"};
				
				ptType=new JLabel("ประเภท ",JLabel.LEFT);
				ptType.setBounds(5,1,150,20);
				ptType.setFont(new Font("Tahoma", Font.PLAIN, 13));
				Panel43.add(ptType);
				int l_ptTypeB=ptTypeB.length;
				ptTypeBButton=new JRadioButton [l_ptTypeB];
				groupTypeB = new ButtonGroup();
				for(int row=0;row<l_ptTypeB;row++){
					int x=30;
					ptTypeBButton[row] = new JRadioButton(ptTypeB[row]);
					ptTypeBButton[row].setBounds(x+(row*130),20,120,20);
					ptTypeBButton[row].setBackground(new Color(207,229,233));
					ptTypeBButton[row].setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptTypeBButton[row].setActionCommand( ptTypeBButton[row].getText() );
					ptTypeBButton[row].addActionListener(this); 
					Panel43.add(ptTypeBButton[row]);
					groupTypeB.add(ptTypeBButton[row]);
				}
				//
				ptEdu=new JLabel("สถานศึกษา (กรณีเด็กวัยเรียน) ",JLabel.LEFT);
				ptEdu.setBounds(5,45,200,20);
				ptEdu.setFont(new Font("Tahoma", Font.PLAIN, 13));
				Panel43.add(ptEdu);
				
				int l_ptEduB=ptEduB.length;
				ptEduBButton=new JRadioButton [l_ptEduB];
				groupEduB = new ButtonGroup();
				for(int row=0;row<10;row++){
					if(row<5){
					int x=30;
					 
					ptEduBButton[row] = new JRadioButton(ptEduB[row]);
					ptEduBButton[row].setActionCommand( ptEduBButton[row].getText() );
					ptEduBButton[row].addActionListener(this);
					ptEduBButton[row].setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptEduBButton[row].setBounds(x+(row*130),70,120,20);
					ptEduBButton[row].setBackground(new Color(207,229,233));
					if(checkAge()==3){
						ptEduBButton[row].setEnabled(true);
					}else{
						ptEduBButton[row].setEnabled(false);
					}
					Panel43.add(ptEduBButton[row]);
					groupEduB.add(ptEduBButton[row]);
					}else if(row>4 && row<10){
						int x=30;
						 
						ptEduBButton[row] = new JRadioButton(ptEduB[row]);
						ptEduBButton[row].setActionCommand( ptEduBButton[row].getText() );
						ptEduBButton[row].addActionListener(this);
						ptEduBButton[row].setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptEduBButton[row].setBounds(x+((row-5)*130),100,120,20);
						ptEduBButton[row].setBackground(new Color(207,229,233));
						if(checkAge()==3){
							ptEduBButton[row].setEnabled(true);
						}else{
							ptEduBButton[row].setEnabled(false);
						}
						Panel43.add(ptEduBButton[row]);
						groupEduB.add(ptEduBButton[row]);
					}
				}
				//
				ptEdulevel=new JLabel("ระดับการศึกษา (กรณีเด็กวัยเรียน) ",JLabel.LEFT);
				ptEdulevel.setFont(new Font("Tahoma", Font.PLAIN, 13));
				ptEdulevel.setBounds(5,140,250,20);				
				Panel43.add(ptEdulevel);
								 
				Border textfield_b = new SoftBevelBorder(BevelBorder.LOWERED);
				TextField_EdulevelB = new JTextField();
				TextField_EdulevelB.setFont(new Font("Tahoma", Font.PLAIN, 13));
				TextField_EdulevelB.setBorder(textfield_b);
				TextField_EdulevelB.setBounds(200,140,50,20);
				TextField_EdulevelB.setText("");
				TextField_EdulevelB.addActionListener(this);
				Panel43.add(TextField_EdulevelB);
				//
				ptExam1=new JLabel("1.การตรวจในช่องปาก ",JLabel.LEFT);
				ptExam1.setFont(new Font("Tahoma", Font.PLAIN, 13));
				ptExam1.setBounds(5,180,200,20);
				Panel43.add(ptExam1);
				
				columnNamesExam1 = new Vector<String>();
				
				for(int c=0;c<16;c++){
					columnNamesExam1.add("");
				}
				
				Vector<Vector<String>> dataExam1 = new Vector<Vector<String>>();
				final Vector<String> dataExam = new Vector<String>();
				final Vector<String> dataExam2 = new Vector<String>();
				
				String sql_432 = "select * from dhis where  hn= '"+oUserInfo.GetPtHN()+"' and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
				//String sql_432 = "select * from dhis where  hn= '"+hn+"' order by visitdate desc limit 1";

				Connection conn43;
				PreparedStatement stmt43,stmt433;
				try {
					conn43 = new DBmanager().getConnMySql(); 
					stmt43 = conn43.prepareStatement(sql_432);
					 
					ResultSet rs43 = stmt43.executeQuery();
				 
					while (rs43.next()) {
						if(rs43.getString(6) ==null){
							//ptTypeBButton[4].setSelected(true);
						}
						else if(rs43.getString(6).trim().equals("1")){
							ptTypeBButton[0].setSelected(true);
						}
						else if(rs43.getString(6).trim().equals("2")){
							ptTypeBButton[1].setSelected(true);
						}
						else if(rs43.getString(6).trim().equals("3")){
							ptTypeBButton[2].setSelected(true);
						}
						else if(rs43.getString(6).trim().equals("4")){
							ptTypeBButton[3].setSelected(true);
						}
						else if(rs43.getString(6).trim().equals("5")){
							ptTypeBButton[4].setSelected(true);
						}
						if(rs43.getString(7) ==null || rs43.getString(7).trim().equals("0")){
							 
						}
						else if(rs43.getString(7).trim().equals("1")){
							ptEduBButton[0].setSelected(true);
						}
						else if(rs43.getString(7).trim().equals("2")){
							ptEduBButton[1].setSelected(true);
						}
						else if(rs43.getString(7).trim().equals("3")){
							ptEduBButton[2].setSelected(true);
						}
						else if(rs43.getString(7).trim().equals("4")){
							ptEduBButton[3].setSelected(true);
						}
						else if(rs43.getString(7).trim().equals("5")){
							ptEduBButton[4].setSelected(true);
						}
						else if(rs43.getString(7).trim().equals("6")){
							ptEduBButton[5].setSelected(true);
						}
						else if(rs43.getString(7).trim().equals("7")){
							ptEduBButton[6].setSelected(true);
						}
						else if(rs43.getString(7).trim().equals("8")){
							ptEduBButton[7].setSelected(true);
						}
						else if(rs43.getString(7).trim().equals("9")){
							ptEduBButton[8].setSelected(true);
						}
				
						if(rs43.getString(8) ==null || rs43.getString(8).trim().equals("0")){
							TextField_EdulevelB.setText("");
						}
						else  if(rs43.getString(8) !=null){
							TextField_EdulevelB.setText(rs43.getString(8).trim());
						}
							
							
						for(int r=9;r<25;r++){
							//System.out.println(rs43.getString(r));
							if(rs43.getString(r) ==null){
								dataExam.add("");
							}							
							else if(rs43.getString(r) !=null){
								dataExam.add(rs43.getString(r).trim());
								//System.out.println(rs43.getString(r).trim());
							}							 
						}
						dataExam1.add(dataExam); 					 					 
					}
					stmt43.close(); 
					stmt433 = conn43.prepareStatement(sql_432);
					ResultSet rs433 = stmt433.executeQuery();
					while (rs433.next()) {						
						for(int r=25;r<41;r++){
							if(rs433.getString(r) ==null){
								dataExam.add("");
							}
							else if(rs433.getString(r) !=null){
								dataExam2.add(rs433.getString(r).trim());
								//System.out.println(rs433.getString(r).trim());
							} 
						}
						dataExam1.add(dataExam2); 					 					 
					}
					stmt433.close();					 
					conn43.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DefaultTableModel modelExam1 = new DefaultTableModel(dataExam1, columnNamesExam1){
					public Class getColumnClass(int column){
						return getValueAt(0, column).getClass();
					}
				};
				tableExam1 = new JTable(modelExam1){
					public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
						JLabel c = (JLabel) super.prepareRenderer(renderer, row, column);
						c.setBackground(Color.WHITE);
						c.setForeground(Color.BLACK);
						c.setHorizontalAlignment(JLabel.CENTER);
				        c.setFont(new Font("Tahoma", Font.PLAIN, 13));
				        return c;
					}
				};
				TableColumnModel columnModelExam1 = tableExam1.getColumnModel();
			    for(int cl=0;cl<16;cl++){
			    	columnModelExam1.getColumn(cl).setPreferredWidth(35);
				}
			    tableExam1.setPreferredScrollableViewportSize(new Dimension(560, 70));
				tableExam1.setFillsViewportHeight(true);
				tableExam1.setEnabled(true);
				tableExam1.setRowHeight(35);
				tableExam1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				tableExam1.setBounds(40,260,560,70);
				tableExam1.addMouseListener(this);
				Panel43.add(tableExam1);
				//
				String [] tooth_dUP={"55","54","53","52","51","61","62","63","64","65"};
				String [] tooth_dLOW={"85","84","83","82","81","71","72","73","74","75"};
				int l_tooth_dUP=tooth_dUP.length;
				int l_tooth_dLOW=tooth_dLOW.length;
				
				 JLabel[] ltooth_dUP=new JLabel[l_tooth_dUP];
				 for(int row_tooth_dUP=0;row_tooth_dUP<l_tooth_dUP;row_tooth_dUP++){
					 int x_tooth_dUP=155;
					 ltooth_dUP[row_tooth_dUP] = new JLabel(tooth_dUP[row_tooth_dUP],JLabel.CENTER);		 
					 ltooth_dUP[row_tooth_dUP].setBounds(x_tooth_dUP+(row_tooth_dUP*35),225,20,20);
					 Panel43.add(ltooth_dUP[row_tooth_dUP]);
				 }
				  
				 JLabel[] ltooth_dLOW=new JLabel[l_tooth_dLOW];
				 for(int row_tooth_dLOW=0;row_tooth_dLOW<l_tooth_dLOW;row_tooth_dLOW++){
					 int x_tooth_dLOW=155;
					 ltooth_dLOW[row_tooth_dLOW] = new JLabel(tooth_dLOW[row_tooth_dLOW],JLabel.CENTER);		 
					 ltooth_dLOW[row_tooth_dLOW].setBounds(x_tooth_dLOW+(row_tooth_dLOW*35),350,20,20);
					 Panel43.add(ltooth_dLOW[row_tooth_dLOW]);
				 }
				 String [] tooth_pUP={"18","17","16","15","14","13","12","11","21","22","23","24","25","26","27","28"};
				 String [] tooth_pLOW={"48","47","46","45","44","43","42","41","31","32","33","34","35","36","37","38"};
				 int l_tooth_pUP=tooth_pUP.length;
				 int l_tooth_pLOW=tooth_pLOW.length;
				 
				 JLabel[] ltooth_pUP=new JLabel[l_tooth_pUP];
				 for(int row_tooth_pUP=0;row_tooth_pUP<l_tooth_pUP;row_tooth_pUP++){
					 int x_tooth_pUP=50;
					 ltooth_pUP[row_tooth_pUP] = new JLabel(tooth_pUP[row_tooth_pUP],JLabel.CENTER);		 
					 ltooth_pUP[row_tooth_pUP].setBounds(x_tooth_pUP+(row_tooth_pUP*35),240,20,20);
					 Panel43.add(ltooth_pUP[row_tooth_pUP]);
				 }
				 JLabel[] ltooth_pLOW=new JLabel[l_tooth_pLOW];
				 for(int row_tooth_pLOW=0;row_tooth_pLOW<l_tooth_pLOW;row_tooth_pLOW++){
					 int x_tooth_pLOW=50;
					 ltooth_pLOW[row_tooth_pLOW] = new JLabel(tooth_pLOW[row_tooth_pLOW],JLabel.CENTER);		 
					 ltooth_pLOW[row_tooth_pLOW].setBounds(x_tooth_pLOW+(row_tooth_pLOW*35),335,20,20);
					 Panel43.add(ltooth_pLOW[row_tooth_pLOW]);
					 
				 }
				//end sec1
				 
				 JLabel ptExam201,ptExam301,ptExam401,ptExam31,ptExam32,ptExam33,ptExam41,ptExam42,ptExam43,provider;
					ptExam201=new JLabel("2.สภาวะปริทันต์ ",JLabel.LEFT);
					ptExam201.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam201.setBounds(15,380,200,20);
					Panel43.add(ptExam201);
					
					columnNamesExam2 = new Vector<String>();			
					columnNamesExam2.add("");
					columnNamesExam2.add("");
					columnNamesExam2.add("");
					Vector<Vector<String>> dataExam201 = new Vector<Vector<String>>();
					final Vector<String> dataExam301 = new Vector<String>();
					final Vector<String> dataExam401 = new Vector<String>();
					String [] pLabel_UP={"ฟันหลังบนขวา","ฟันหน้าบน","ฟันหลังบนซ้าย"};
					String [] pLabel_LOW={"ฟันหลังล่างขวา","ฟันหน้าล่าง","ฟันหลังล่างซ้าย"};
					int l_pLabel_UP=pLabel_UP.length;
					int l_pLabel_LOW=pLabel_LOW.length;
					JLabel[] p_UP=new JLabel[l_pLabel_UP];
					 for(int row_p_UP=0;row_p_UP<l_pLabel_UP;row_p_UP++){
						 int x_UP=50;
						 p_UP[row_p_UP] = new JLabel(pLabel_UP[row_p_UP],JLabel.CENTER);		 
						 p_UP[row_p_UP].setBounds(x_UP+(row_p_UP*140),410,100,20);
						 Panel43.add(p_UP[row_p_UP]);
					 }
					  
					 JLabel[] p_LOW=new JLabel[l_pLabel_LOW];
					 for(int row_p_LOW=0;row_p_LOW<l_pLabel_LOW;row_p_LOW++){
						 int x_tooth_dLOW=50;
						 p_LOW[row_p_LOW] = new JLabel(pLabel_LOW[row_p_LOW],JLabel.CENTER);		 
						 p_LOW[row_p_LOW].setBounds(x_tooth_dLOW+(row_p_LOW*140),520,100,20);
						 Panel43.add(p_LOW[row_p_LOW]);
					 }
					
					
					ptExam3=new JLabel("3.การสบฟัน",JLabel.LEFT);
					ptExam3.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam3.setBounds(15,550,180,20);
					Panel43.add(ptExam3);
					
					ptExam31=new JLabel("1.จำนวนคู่สบฟันแท้กับฟันแท้ ",JLabel.LEFT);
					ptExam31.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam31.setBounds(25,570,350,20);				
					Panel43.add(ptExam31);
					
					TextField_occ1 = new JTextField();
					TextField_occ1.setBounds(400,570,50,20);
					TextField_occ1.setText("0");
					TextField_occ1.addActionListener(this);
					Panel43.add(TextField_occ1);
					
					ptExam32=new JLabel("2.จำนวนคู่สบฟันแท้กับฟันเทียม (เฉพาะกลุ่มที่มีอายุ >= 60 ปี)",JLabel.LEFT);
					ptExam32.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam32.setBounds(25,590,350,20);					
					Panel43.add(ptExam32);
					
					TextField_occ2 = new JTextField();
					TextField_occ2.setBounds(400,590,50,20);
					TextField_occ2.addActionListener(this);
					TextField_occ2.setEnabled(false);
					TextField_occ2.setText("0");
					if(checkAge()==4){
						TextField_occ2.setEnabled(true);
						TextField_occ2.setText("");					 
					}
					Panel43.add(TextField_occ2);
					
					ptExam33=new JLabel("3.จำนวนคู่สบฟันเทียมกับฟันเทียม (เฉพาะกลุ่มที่มีอายุ >= 60 ปี)",JLabel.LEFT);
					ptExam33.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam33.setBounds(25,610,350,20);					
					Panel43.add(ptExam33);
					
					TextField_occ3 = new JTextField();
					TextField_occ3.setBounds(400,610,50,20);
					TextField_occ3.addActionListener(this);
					TextField_occ3.setEnabled(false);
					TextField_occ3.setText("0");
					if(checkAge()==4){
						TextField_occ3.setEnabled(true);
						TextField_occ3.setText("");
					}
					Panel43.add(TextField_occ3);
					
					ptExam4=new JLabel("4.บริการที่ควรได้รับ ",JLabel.LEFT);
					ptExam4.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam4.setBounds(15,635,180,20);					
					Panel43.add(ptExam4);
					
					ptExam41=new JLabel("1.การทาฟูลออไรด์",JLabel.LEFT);
					ptExam41.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam41.setBounds(25,660,100,20);					
					Panel43.add(ptExam41);
					
					FNButton=new JRadioButton [2];
					groupFN = new ButtonGroup();
					for(int row=0;row<2;row++){
						int x=140;
						if(row==1){
							FNButton[row] = new JRadioButton("ไม่จำเป็นf");						  
						 }else{
							 FNButton[row] = new JRadioButton("จำเป็นf");
						 }				 
						FNButton[row].setActionCommand( FNButton[row].getText() );
						FNButton[row].addActionListener(this);				
						FNButton[row].setBounds(x+(row*100),660,100,20);
						FNButton[row].setBackground(new Color(207,229,233));					
						Panel43.add(FNButton[row]);
						groupFN.add(FNButton[row]);				 
					}
					
					ptExam42=new JLabel("2.การขูดหินน้ำลาย   ",JLabel.LEFT);
					ptExam42.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam42.setBounds(25,690,250,20);				
					Panel43.add(ptExam42);
					
					SNButton=new JRadioButton [2];
					groupSN = new ButtonGroup();
					for(int row=0;row<2;row++){
						int x=140;
						if(row==1){
							SNButton[row] = new JRadioButton("ไม่จำเป็นs");					  
						 }else{
							 SNButton[row] = new JRadioButton("จำเป็นs");
						 }			
						SNButton[row].setActionCommand( SNButton[row].getText() );
						SNButton[row].addActionListener(this);					 				
						SNButton[row].setBounds(x+(row*100),690,100,20);
						SNButton[row].setBackground(new Color(207,229,233));										
						Panel43.add(SNButton[row]);
						groupSN.add(SNButton[row]);				 
					}
					
					ptExam43=new JLabel("3.การใส่ฟันเทียม ",JLabel.LEFT);
					ptExam43.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam43.setBounds(25,720,250,20);					
					Panel43.add(ptExam43);
					
					String [] prosNeed={"ต้องใส่ฟันบนและล่าง","ต้องใส่ฟันบน","ต้องใส่ฟันล่าง","ไม่ต้องใส่ฟันเทียม"};
					int l_prosNeed = prosNeed.length;			
					ProsNButton=new JRadioButton [l_prosNeed];
					groupProsN = new ButtonGroup();
					for(int row=0;row<l_prosNeed;row++){
						int x=140;
						if(row==3){
							ProsNButton[row] = new JRadioButton(prosNeed[row]);						  
						 }else{
							ProsNButton[row] = new JRadioButton(prosNeed[row]);
						 }					 
						ProsNButton[row].setActionCommand( ProsNButton[row].getText() );
						ProsNButton[row].addActionListener(this);					 				
						ProsNButton[row].setBounds(x+(row*120),720,120,20);
						ProsNButton[row].setBackground(new Color(207,229,233));										
						Panel43.add(ProsNButton[row]);
						groupProsN.add(ProsNButton[row]);				 
					}
					provider=new JLabel("ผู้ตรวจ  ",JLabel.LEFT);
					provider.setFont(new Font("Tahoma", Font.PLAIN, 13));
					provider.setBounds(25,750,300,20);					
					Panel43.add(provider);
					
					TextField_provider = new JTextField();
					TextField_provider.setBounds(150,750,50,20);
					TextField_provider.setText("");
					TextField_provider.addActionListener(this);
					Panel43.add(TextField_provider);
					
					String sql_43 = "select perio,occpetope,occpetopr,occprtopr,fluneed,sneed,prosneed,provider  from dhis where  hn= '"+oUserInfo.GetPtHN()+"'  and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
					Connection conn431;
					PreparedStatement stmt431;
					try {
						conn431 = new DBmanager().getConnMySql();	 
						stmt431 = conn431.prepareStatement(sql_43);	
						ResultSet rs43 = stmt431.executeQuery();
						while (rs43.next()) {
							if(rs43.getString(1) ==null){
								String [] dex4={"","",""};
								for(int r=0;r<dex4.length;r++){
									dataExam301.add(dex4[r]);
								}			           
					            dataExam201.add(dataExam301); 
					            String [] dex5={"","",""};
								for(int r=0;r<dex5.length;r++){
									dataExam401.add(dex5[r]);
								}
					            dataExam201.add(dataExam401); 
							}
							else if(rs43.getString(1) !=null ){
								if( rs43.getString(1).equals("")) {
									String [] dex4={"","",""};
									for(int r=0;r<dex4.length;r++){
										dataExam301.add(dex4[r]);
									}			           
						            dataExam201.add(dataExam301); 
						            String [] dex5={"","",""};
									for(int r=0;r<dex5.length;r++){
										dataExam401.add(dex5[r]);
									}
						            dataExam201.add(dataExam401); 
								}else {
									String perio_text=rs43.getString(1).trim(); 	
									char[] perioArray = perio_text.toCharArray();
									for(int r=0;r<3;r++){
										dataExam301.add(String.valueOf(perioArray[r]));
									}
									dataExam201.add(dataExam301);
									for(int r=3;r<6;r++){
										dataExam401.add(String.valueOf(perioArray[r]));
									}
									dataExam201.add(dataExam401);
								}
								 
							}
							if(rs43.getString(2) ==null){							
							}
							else if(rs43.getString(2) !=null){
								TextField_occ1.setText(rs43.getString(2).trim());
							}						
							if(rs43.getString(3) ==null){							
							}
							else if(rs43.getString(3) !=null){
								TextField_occ2.setText(rs43.getString(3).trim());
							}
							if(rs43.getString(4) ==null){							
							}
							else if(rs43.getString(4) !=null){
								TextField_occ3.setText(rs43.getString(4).trim());
							}
							if(rs43.getString(5) ==null){						
							}
							else if(rs43.getString(5) !=null){
								if(rs43.getString(5).trim().equals("1")){
									FNButton[0].setSelected(true);
								}
								else if(rs43.getString(5).trim().equals("2")){
									FNButton[1].setSelected(true);
								}						 
							}						
							if(rs43.getString(6) ==null){							
							}
							else if(rs43.getString(6) !=null){
								if(rs43.getString(6).trim().equals("1")){
									SNButton[0].setSelected(true);
								}
								else if(rs43.getString(6).trim().equals("2")){
									SNButton[1].setSelected(true);
								}						 
							}						
							if(rs43.getString(7) ==null){						 
							}
							else if(rs43.getString(7) !=null){
								if(rs43.getString(7).trim().equals("1")){
									ProsNButton[0].setSelected(true);
								}
								else if(rs43.getString(7).trim().equals("2")){
									ProsNButton[1].setSelected(true);
								}
								else if(rs43.getString(7).trim().equals("3")){
									ProsNButton[2].setSelected(true);
								}
								else if(rs43.getString(7).trim().equals("4")){
									ProsNButton[3].setSelected(true);
								}							 
							}						
							if(rs43.getString(8) ==null){
								TextField_provider.setText("047");
							}
							else if(rs43.getString(8) !=null){
								TextField_provider.setText(rs43.getString(8).trim());
							}						 					 
						}		 
						stmt431.close(); 
						conn431.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					DefaultTableModel modelExam201 = new DefaultTableModel(dataExam201, columnNamesExam2){
						public Class getColumnClass(int column){
							return getValueAt(0, column).getClass();
						}
					};
					
					tableExam2 = new JTable(modelExam201){
						public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
							JLabel c = (JLabel) super.prepareRenderer(renderer, row, column);
							c.setBackground(Color.WHITE);
							c.setForeground(Color.BLACK);
							c.setHorizontalAlignment(JLabel.CENTER);
					        c.setFont(new Font("Tahoma", Font.PLAIN, 13));
					        return c;
						}
					};
					TableColumnModel columnModelExam2 = tableExam2.getColumnModel();
					for(int cl2=0;cl2<3;cl2++){
				    	columnModelExam2.getColumn(cl2).setPreferredWidth(35);
					}
				    tableExam2.setPreferredScrollableViewportSize(new Dimension(560, 70));
					tableExam2.setFillsViewportHeight(true);
					tableExam2.setEnabled(true);
					tableExam2.setRowHeight(35);
					tableExam2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					tableExam2.setBounds(40,440,420,70);
					tableExam2.addMouseListener(this);
					mid2.add(tableExam2);
					
				
				scrollPane43 = new JScrollPane(Panel43);
				//scrollPane43.setPreferredSize(new Dimension(500,500));
				//scrollPane43.setViewportView(Panel43);
				mid2.add(scrollPane43, BorderLayout.CENTER);
				
				mid2.revalidate();
				mid2.repaint();	
				MiddleSection.revalidate();
				MiddleSection.repaint();
				//right
				right1.removeAll();
				ButtonSaveExam1 = new JButton("Save");		 
				ButtonSaveExam1.setBounds(5,10,80,30);
				ButtonSaveExam1.addActionListener(this);
				ButtonPrintExam1 = new JButton("Print");		 
				ButtonPrintExam1.setBounds(5,50,80,30);
				ButtonPrintExam1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						PrintOPD(formcode,"2",page,form_clinic_name);
						
					}
				});
				
				Button431 = new JButton("To 43");		 
				Button431.setBounds(5,90,80,30);
				Button431.addActionListener(this);
				
				if(oUserInfo.GetPtVN().equals("")){
					 
				}else{
					right1.add(ButtonSaveExam1);
					right1.add(ButtonPrintExam1);
					right1.add(Button431); 
				}
				right1.revalidate();
				right1.repaint();
				RightSection.revalidate();
				RightSection.repaint();
			}
		}
		else if(formcode.equals("00107")) {
			mid2.removeAll();
			String [] Info43_1={"hospcode","pid","seq","date_serv","denttype","servplace","pteeth","pcaries","pfilling","pextract","dteeth","dcaries","dfilling","dextract"};
			String [] Info43_2={"need_fluoride","need_scaling","need_sealant","need_pfilling","need_dfilling","need_pextract","need_dextract","nprosthesis","permanent_permanent","permanent_prosthesis","prosthesis_prosthesis","gum","schooltype","class","provider","d_update"};
			int l_Info43_1=Info43_1.length;
			int l_Info43_2=Info43_2.length;
		 
			 JLabel[] Label43_1=new JLabel[l_Info43_1];
			 for(int r=0;r<l_Info43_1;r++){
				 int y=5;
				// Label43_1[r] = new JLabel(Info43_1[r],JLabel.LEFT);		 
				// Label43_1[r].setBounds(10,y+(r*30),100,20);
				// mid2.add(Label43_1[r]);
			 }
			 
			 JLabel[] Label43_2=new JLabel[l_Info43_2];
			 for(int r=0;r<l_Info43_2;r++){
				 int y=5;
				// Label43_2[r] = new JLabel(Info43_2[r],JLabel.LEFT);
				// Label43_2[r].setBounds(350,y+(r*30),150,20);
				// mid2.add(Label43_2[r]);
			 }
		 		String sql_43 = "select *  from dental43 where  pid= '"+oUserInfo.GetPtHN()+"' and date_serv='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
				Connection conn43;
				PreparedStatement stmt43,stmt431;
				try {
						conn43 = new DBmanager().getConnMySql();	 
						stmt43 = conn43.prepareStatement(sql_43);	
						stmt431 = conn43.prepareStatement(sql_43);	 
						ResultSet rs43 = stmt43.executeQuery();
						ResultSet rs431 = stmt431.executeQuery();
			        
						while (rs43.next()) {
							 
								for(int j=0;j<l_Info43_1;j++){
								int y=5;
								 Label43_1[j] = new JLabel(Info43_1[j]+" = "+rs43.getString(j+1),JLabel.LEFT);		 
								 Label43_1[j].setBounds(10,y+(j*30),150,20);
								 mid2.add(Label43_1[j]);
								//System.out.println("label 1 "+rs43.getString(j+1));
								}						 
						}
						while (rs431.next()) {
							 
							for(int j=0;j<l_Info43_2;j++){
								int y=5;
								Label43_2[j] = new JLabel(Info43_2[j]+" = "+rs431.getString(j+15),JLabel.LEFT);		 
							 	Label43_2[j].setBounds(350,y+(j*30),200,20);
							 	mid2.add(Label43_2[j]);
							//System.out.println("label 2 "+rs431.getString(j+15));
							}						 
					}
						stmt43.close();
						stmt431.close();
						conn43.close();
					} catch (SQLException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    
			mid2.revalidate();
			mid2.repaint();
			

			//right Section
			right1.removeAll();
			
			
			right1.revalidate();
			right1.repaint();
			MiddleSection.revalidate();
			MiddleSection.repaint();
			RightSection.revalidate();
			RightSection.repaint();
		}
		else if(formcode.equals("00110")){
			String hn_s="";
			clearValue();
			 
			mid2.removeAll();
			txPanel = new JPanel();
			txPanel.setLayout(null);
			
			l_date=new JLabel("วันที่ให้บริการ ",JLabel.RIGHT);
			l_date.setBounds(50,40,80,20);			
			txPanel.add(l_date);
			
			TextField_date= new JTextField();
			TextField_date.setBounds(140,40,80,20);
			TextField_date.setEditable(false);
			TextField_date.setText(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()));
			TextField_date.addActionListener(this);
			txPanel.add(TextField_date);
			 
			l_id=new JLabel("เลขที่บัตรประชาชน ",JLabel.RIGHT);
			l_id.setBounds(230,40,100,20);			
			txPanel.add(l_id);
			
			TextField_id= new JTextField();
			TextField_id.setBounds(340,40,150,20);
			TextField_id.setText("");
			TextField_id.addActionListener(this);
			txPanel.add(TextField_id);
			
			ButtonSID = new JButton("Search");		 
			ButtonSID.setBounds(520,40,80,20);		 
			ButtonSID.addActionListener(this);
			if(oUserInfo.GetPtVN().equals("")){
				txPanel.add(ButtonSID);
			}
			
			l_toothno=new JLabel("ตำแหน่งที่รักษา  ",JLabel.LEFT);
			l_toothno.setBounds(60,80,100,20);			
			txPanel.add(l_toothno);
			
			TextField_toothno= new JTextField();
			TextField_toothno.setBounds(70,100,50,20);
			TextField_toothno.setText("");
			TextField_toothno.addActionListener(this);
			txPanel.add(TextField_toothno);
			
			l_diag=new JLabel("การวินิจฉัย  ",JLabel.LEFT);
			l_diag.setBounds(170,80,150,20);			
			txPanel.add(l_diag);
			
			TextField_diag= new JTextField();
			TextField_diag.setBounds(150,100,100,20);
			TextField_diag.setText("");
			TextField_diag.addActionListener(this);
			txPanel.add(TextField_diag);
			
			l_tx=new JLabel("การรักษา  ",JLabel.LEFT);
			l_tx.setBounds(330,80,100,20);			
			txPanel.add(l_tx);
			
			TextField_tx= new JTextField();
			TextField_tx.setBounds(300,100,100,20);
			TextField_tx.setText("");
			TextField_tx.addActionListener(this);
			txPanel.add(TextField_tx);
			
			l_provider=new JLabel("ผู้ตรวจ  ",JLabel.LEFT);
			l_provider.setBounds(450,80,100,20);
				
			txPanel.add(l_provider);
			
			TextField_doctor = new JTextField();
			TextField_doctor.setBounds(440,100,50,20);
			TextField_doctor.setText("");
			TextField_doctor.addActionListener(this);
			txPanel.add(TextField_doctor);
			
			//database
			Vector<Vector<String>> dataTx = new Vector<Vector<String>>();
				
			DefaultTableModel modelTx = new DefaultTableModel(dataTx, columnNamesTx)
		    {
				public Class getColumnClass(int column)
				{
					return getValueAt(0, column).getClass();
				}
			};
			tableTx = new JTable(modelTx)
		    {
				public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
				{
					Component c = super.prepareRenderer(renderer, row, column);

		 
					return c;
				}
		    };
		    TableColumnModel columnModelTx = tableTx.getColumnModel();
			columnModelTx.getColumn(0).setPreferredWidth(30);
			columnModelTx.getColumn(1).setPreferredWidth(100);
			columnModelTx.getColumn(2).setPreferredWidth(80);
			columnModelTx.getColumn(3).setPreferredWidth(80);
			columnModelTx.getColumn(4).setPreferredWidth(80);
			columnModelTx.getColumn(5).setPreferredWidth(50);
			 
			tableTx.setPreferredScrollableViewportSize(new Dimension(600, 200));
			tableTx.setFillsViewportHeight(true);
			tableTx.setRowHeight(30);			 
			tableTx.addMouseListener(this);
			scrollPaneTx = new JScrollPane(tableTx);
			scrollPaneTx.setBounds(50, 150, 600, 200);
			txPanel.add(scrollPaneTx);
			
			mid2.add(txPanel, BorderLayout.CENTER);
			mid2.revalidate();
			mid2.repaint();
			
			right1.removeAll();
			/*
			ButtonPrintExam1 = new JButton("Print");		 
			ButtonPrintExam1.setBounds(5,40,80,20);		 
			ButtonPrintExam1.addActionListener(this);
			if(vn.equals("")){
				right1.add(ButtonPrintExam1);
			}
			*/
			ButtonTreatment = new JButton("Save");		 
			ButtonTreatment.setBounds(5,40,80,20);		 
			ButtonTreatment.addActionListener(this);
			if(oUserInfo.GetPtVN().equals("")){
				right1.add(ButtonTreatment);
			}
			ButtonClearTx = new JButton("Clear");		 
			ButtonClearTx.setBounds(5,70,80,20);		 
			ButtonClearTx.addActionListener(this);
			if(oUserInfo.GetPtVN().equals("")){
				right1.add(ButtonClearTx);
			}
			ButtonDelTx = new JButton("Delete");		 
			ButtonDelTx.setBounds(5,100,80,20);		 
			ButtonDelTx.addActionListener(this);
			if(oUserInfo.GetPtVN().equals("")){
				right1.add(ButtonDelTx);
			}
			
			right1.revalidate();
			right1.repaint();
			MiddleSection.revalidate();
			MiddleSection.repaint();
			RightSection.revalidate();
			RightSection.repaint();
		}
		else if(formcode.equals("00112")){
			if(oUserInfo.GetPtHN().length() !=7){
				JOptionPane.showMessageDialog(this,"Please select Patient","Patient Error",JOptionPane.ERROR_MESSAGE);

			}else{
				checkExam(oUserInfo.GetPtHN());
				//right Section
				right1.removeAll();
				
				
				if(oUserInfo.GetPtHN().length() !=7){
					JOptionPane.showMessageDialog(this,"Please select Patient","Patient Error",JOptionPane.ERROR_MESSAGE);

				}else{
					mid2.removeAll();
					checkExam(oUserInfo.GetPtHN());
					
					Panel43 = new JPanel();
					Panel43.setPreferredSize(new Dimension((screen.width*2)/10, 800));
					Panel43.setLayout(null);
					
					JLabel ptType,ptEdu,ptEdulevel,ptExam1,ptExam2,ptExam3,ptExam4;
					String [] ptTypeB={"หญิงตั้งครรภ์","เด็กก่อนวัยเรียน","เด็กวัยเรียน","ผู้สูงอายุ","กลุ่มอื่นๆ"};
					String [] ptEduB={"ศพด.","ประถม-รัฐบาล","ประถม-เทศบาล","ประถม-ท้องถิ่น","ประถม-เอกชน","มัธยม-รัฐบาล","มัธยม-เทศบาล","มัธยม-ท้องถิ่น","มัธยม-เอกชน","ไม่เลือก"};
					String [] ptEdulevelB={"ศพด.1-3","ประถม1-6","มัธยม1-6"};
					
					ptType=new JLabel("ประเภท ",JLabel.LEFT);
					ptType.setBounds(5,1,150,20);
					ptType.setFont(new Font("Tahoma", Font.PLAIN, 13));
					Panel43.add(ptType);
					int l_ptTypeB=ptTypeB.length;
					ptTypeBButton=new JRadioButton [l_ptTypeB];
					groupTypeB = new ButtonGroup();
					for(int row=0;row<l_ptTypeB;row++){
						int x=30;
						ptTypeBButton[row] = new JRadioButton(ptTypeB[row]);
						ptTypeBButton[row].setBounds(x+(row*130),20,120,20);
						ptTypeBButton[row].setBackground(new Color(207,229,233));
						ptTypeBButton[row].setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptTypeBButton[row].setActionCommand( ptTypeBButton[row].getText() );
						ptTypeBButton[row].addActionListener(this); 
						Panel43.add(ptTypeBButton[row]);
						groupTypeB.add(ptTypeBButton[row]);
					}
					//
					ptEdu=new JLabel("สถานศึกษา (กรณีเด็กวัยเรียน) ",JLabel.LEFT);
					ptEdu.setBounds(5,45,200,20);
					ptEdu.setFont(new Font("Tahoma", Font.PLAIN, 13));
					Panel43.add(ptEdu);
					
					int l_ptEduB=ptEduB.length;
					ptEduBButton=new JRadioButton [l_ptEduB];
					groupEduB = new ButtonGroup();
					for(int row=0;row<10;row++){
						if(row<5){
						int x=30;
						 
						ptEduBButton[row] = new JRadioButton(ptEduB[row]);
						ptEduBButton[row].setActionCommand( ptEduBButton[row].getText() );
						ptEduBButton[row].addActionListener(this);
						ptEduBButton[row].setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptEduBButton[row].setBounds(x+(row*130),70,120,20);
						ptEduBButton[row].setBackground(new Color(207,229,233));
						if(checkAge()==3){
							ptEduBButton[row].setEnabled(true);
						}else{
							ptEduBButton[row].setEnabled(false);
						}
						Panel43.add(ptEduBButton[row]);
						groupEduB.add(ptEduBButton[row]);
						}else if(row>4 && row<10){
							int x=30;
							 
							ptEduBButton[row] = new JRadioButton(ptEduB[row]);
							ptEduBButton[row].setActionCommand( ptEduBButton[row].getText() );
							ptEduBButton[row].addActionListener(this);
							ptEduBButton[row].setFont(new Font("Tahoma", Font.PLAIN, 13));
							ptEduBButton[row].setBounds(x+((row-5)*130),100,120,20);
							ptEduBButton[row].setBackground(new Color(207,229,233));
							if(checkAge()==3){
								ptEduBButton[row].setEnabled(true);
							}else{
								ptEduBButton[row].setEnabled(false);
							}
							Panel43.add(ptEduBButton[row]);
							groupEduB.add(ptEduBButton[row]);
						}
					}
					//
					ptEdulevel=new JLabel("ระดับการศึกษา (กรณีเด็กวัยเรียน) ",JLabel.LEFT);
					ptEdulevel.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptEdulevel.setBounds(5,140,250,20);				
					Panel43.add(ptEdulevel);
									 
					Border textfield_b = new SoftBevelBorder(BevelBorder.LOWERED);
					TextField_EdulevelB = new JTextField();
					TextField_EdulevelB.setFont(new Font("Tahoma", Font.PLAIN, 13));
					TextField_EdulevelB.setBorder(textfield_b);
					TextField_EdulevelB.setBounds(200,140,50,20);
					TextField_EdulevelB.setText("");
					TextField_EdulevelB.addActionListener(this);
					Panel43.add(TextField_EdulevelB);
					//
					ptExam1=new JLabel("1.การตรวจในช่องปาก ",JLabel.LEFT);
					ptExam1.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam1.setBounds(5,180,200,20);
					Panel43.add(ptExam1);
					
					columnNamesExam1 = new Vector<String>();
					
					for(int c=0;c<16;c++){
						columnNamesExam1.add("");
					}
					
					Vector<Vector<String>> dataExam1 = new Vector<Vector<String>>();
					final Vector<String> dataExam = new Vector<String>();
					final Vector<String> dataExam2 = new Vector<String>();
					
					String sql_432 = "select * from dhis where  hn= '"+oUserInfo.GetPtHN()+"' and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
					Connection conn43;
					PreparedStatement stmt43,stmt433;
					try {
						conn43 = new DBmanager().getConnMySql(); 
						stmt43 = conn43.prepareStatement(sql_432);
						 
						ResultSet rs43 = stmt43.executeQuery();
					 
						while (rs43.next()) {
							if(rs43.getString(6) ==null){
								//ptTypeBButton[4].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("1")){
								ptTypeBButton[0].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("2")){
								ptTypeBButton[1].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("3")){
								ptTypeBButton[2].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("4")){
								ptTypeBButton[3].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("5")){
								ptTypeBButton[4].setSelected(true);
							}
							if(rs43.getString(7) ==null || rs43.getString(7).trim().equals("0")){
								 
							}
							else if(rs43.getString(7).trim().equals("1")){
								ptEduBButton[0].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("2")){
								ptEduBButton[1].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("3")){
								ptEduBButton[2].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("4")){
								ptEduBButton[3].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("5")){
								ptEduBButton[4].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("6")){
								ptEduBButton[5].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("7")){
								ptEduBButton[6].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("8")){
								ptEduBButton[7].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("9")){
								ptEduBButton[8].setSelected(true);
							}
					
							if(rs43.getString(8) ==null || rs43.getString(8).trim().equals("0")){
								TextField_EdulevelB.setText("");
							}
							else  if(rs43.getString(8) !=null){
								TextField_EdulevelB.setText(rs43.getString(8).trim());
							}
								
								
							for(int r=9;r<25;r++){
								//System.out.println(rs43.getString(r));
								if(rs43.getString(r) ==null){
									dataExam.add("");
								}							
								else if(rs43.getString(r) !=null){
									dataExam.add(rs43.getString(r).trim());
									//System.out.println(rs43.getString(r).trim());
								}							 
							}
							dataExam1.add(dataExam); 					 					 
						}
						stmt43.close(); 
						stmt433 = conn43.prepareStatement(sql_432);
						ResultSet rs433 = stmt433.executeQuery();
						while (rs433.next()) {						
							for(int r=25;r<41;r++){
								if(rs433.getString(r) ==null){
									dataExam.add("");
								}
								else if(rs433.getString(r) !=null){
									dataExam2.add(rs433.getString(r).trim());
									//System.out.println(rs433.getString(r).trim());
								} 
							}
							dataExam1.add(dataExam2); 					 					 
						}
						stmt433.close();					 
						conn43.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DefaultTableModel modelExam1 = new DefaultTableModel(dataExam1, columnNamesExam1){
						public Class getColumnClass(int column){
							return getValueAt(0, column).getClass();
						}
					};
					tableExam1 = new JTable(modelExam1){
						public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
							JLabel c = (JLabel) super.prepareRenderer(renderer, row, column);
							c.setBackground(Color.WHITE);
							c.setForeground(Color.BLACK);
							c.setHorizontalAlignment(JLabel.CENTER);
					        c.setFont(new Font("Tahoma", Font.PLAIN, 13));
					        return c;
						}
					};
					TableColumnModel columnModelExam1 = tableExam1.getColumnModel();
				    for(int cl=0;cl<16;cl++){
				    	columnModelExam1.getColumn(cl).setPreferredWidth(35);
					}
				    tableExam1.setPreferredScrollableViewportSize(new Dimension(560, 70));
					tableExam1.setFillsViewportHeight(true);
					tableExam1.setEnabled(true);
					tableExam1.setRowHeight(35);
					tableExam1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					tableExam1.setBounds(40,260,560,70);
					tableExam1.addMouseListener(this);
					Panel43.add(tableExam1);
					//
					String [] tooth_dUP={"55","54","53","52","51","61","62","63","64","65"};
					String [] tooth_dLOW={"85","84","83","82","81","71","72","73","74","75"};
					int l_tooth_dUP=tooth_dUP.length;
					int l_tooth_dLOW=tooth_dLOW.length;
					
					 JLabel[] ltooth_dUP=new JLabel[l_tooth_dUP];
					 for(int row_tooth_dUP=0;row_tooth_dUP<l_tooth_dUP;row_tooth_dUP++){
						 int x_tooth_dUP=155;
						 ltooth_dUP[row_tooth_dUP] = new JLabel(tooth_dUP[row_tooth_dUP],JLabel.CENTER);		 
						 ltooth_dUP[row_tooth_dUP].setBounds(x_tooth_dUP+(row_tooth_dUP*35),225,20,20);
						 Panel43.add(ltooth_dUP[row_tooth_dUP]);
					 }
					  
					 JLabel[] ltooth_dLOW=new JLabel[l_tooth_dLOW];
					 for(int row_tooth_dLOW=0;row_tooth_dLOW<l_tooth_dLOW;row_tooth_dLOW++){
						 int x_tooth_dLOW=155;
						 ltooth_dLOW[row_tooth_dLOW] = new JLabel(tooth_dLOW[row_tooth_dLOW],JLabel.CENTER);		 
						 ltooth_dLOW[row_tooth_dLOW].setBounds(x_tooth_dLOW+(row_tooth_dLOW*35),350,20,20);
						 Panel43.add(ltooth_dLOW[row_tooth_dLOW]);
					 }
					 String [] tooth_pUP={"18","17","16","15","14","13","12","11","21","22","23","24","25","26","27","28"};
					 String [] tooth_pLOW={"48","47","46","45","44","43","42","41","31","32","33","34","35","36","37","38"};
					 int l_tooth_pUP=tooth_pUP.length;
					 int l_tooth_pLOW=tooth_pLOW.length;
					 
					 JLabel[] ltooth_pUP=new JLabel[l_tooth_pUP];
					 for(int row_tooth_pUP=0;row_tooth_pUP<l_tooth_pUP;row_tooth_pUP++){
						 int x_tooth_pUP=50;
						 ltooth_pUP[row_tooth_pUP] = new JLabel(tooth_pUP[row_tooth_pUP],JLabel.CENTER);		 
						 ltooth_pUP[row_tooth_pUP].setBounds(x_tooth_pUP+(row_tooth_pUP*35),240,20,20);
						 Panel43.add(ltooth_pUP[row_tooth_pUP]);
					 }
					 JLabel[] ltooth_pLOW=new JLabel[l_tooth_pLOW];
					 for(int row_tooth_pLOW=0;row_tooth_pLOW<l_tooth_pLOW;row_tooth_pLOW++){
						 int x_tooth_pLOW=50;
						 ltooth_pLOW[row_tooth_pLOW] = new JLabel(tooth_pLOW[row_tooth_pLOW],JLabel.CENTER);		 
						 ltooth_pLOW[row_tooth_pLOW].setBounds(x_tooth_pLOW+(row_tooth_pLOW*35),335,20,20);
						 Panel43.add(ltooth_pLOW[row_tooth_pLOW]);
						 
					 }
					//end sec1
					 
					 JLabel ptExam201,ptExam301,ptExam401,ptExam31,ptExam32,ptExam33,ptExam41,ptExam42,ptExam43,provider;
						ptExam201=new JLabel("2.สภาวะปริทันต์ ",JLabel.LEFT);
						ptExam201.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam201.setBounds(15,380,200,20);
						Panel43.add(ptExam201);
						
						columnNamesExam2 = new Vector<String>();			
						columnNamesExam2.add("");
						columnNamesExam2.add("");
						columnNamesExam2.add("");
						Vector<Vector<String>> dataExam201 = new Vector<Vector<String>>();
						final Vector<String> dataExam301 = new Vector<String>();
						final Vector<String> dataExam401 = new Vector<String>();
						String [] pLabel_UP={"ฟันหลังบนขวา","ฟันหน้าบน","ฟันหลังบนซ้าย"};
						String [] pLabel_LOW={"ฟันหลังล่างขวา","ฟันหน้าล่าง","ฟันหลังล่างซ้าย"};
						int l_pLabel_UP=pLabel_UP.length;
						int l_pLabel_LOW=pLabel_LOW.length;
						JLabel[] p_UP=new JLabel[l_pLabel_UP];
						 for(int row_p_UP=0;row_p_UP<l_pLabel_UP;row_p_UP++){
							 int x_UP=50;
							 p_UP[row_p_UP] = new JLabel(pLabel_UP[row_p_UP],JLabel.CENTER);		 
							 p_UP[row_p_UP].setBounds(x_UP+(row_p_UP*140),410,100,20);
							 Panel43.add(p_UP[row_p_UP]);
						 }
						  
						 JLabel[] p_LOW=new JLabel[l_pLabel_LOW];
						 for(int row_p_LOW=0;row_p_LOW<l_pLabel_LOW;row_p_LOW++){
							 int x_tooth_dLOW=50;
							 p_LOW[row_p_LOW] = new JLabel(pLabel_LOW[row_p_LOW],JLabel.CENTER);		 
							 p_LOW[row_p_LOW].setBounds(x_tooth_dLOW+(row_p_LOW*140),520,100,20);
							 Panel43.add(p_LOW[row_p_LOW]);
						 }
						
						
						ptExam3=new JLabel("3.การสบฟัน",JLabel.LEFT);
						ptExam3.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam3.setBounds(15,550,180,20);
						Panel43.add(ptExam3);
						
						ptExam31=new JLabel("1.จำนวนคู่สบฟันแท้กับฟันแท้ ",JLabel.LEFT);
						ptExam31.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam31.setBounds(25,570,350,20);				
						Panel43.add(ptExam31);
						
						TextField_occ1 = new JTextField();
						TextField_occ1.setBounds(400,570,50,20);
						TextField_occ1.setText("0");
						TextField_occ1.addActionListener(this);
						Panel43.add(TextField_occ1);
						
						ptExam32=new JLabel("2.จำนวนคู่สบฟันแท้กับฟันเทียม (เฉพาะกลุ่มที่มีอายุ >= 60 ปี)",JLabel.LEFT);
						ptExam32.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam32.setBounds(25,590,350,20);					
						Panel43.add(ptExam32);
						
						TextField_occ2 = new JTextField();
						TextField_occ2.setBounds(400,590,50,20);
						TextField_occ2.addActionListener(this);
						TextField_occ2.setEnabled(false);
						TextField_occ2.setText("0");
						if(checkAge()==4){
							TextField_occ2.setEnabled(true);
							TextField_occ2.setText("");					 
						}
						Panel43.add(TextField_occ2);
						
						ptExam33=new JLabel("3.จำนวนคู่สบฟันเทียมกับฟันเทียม (เฉพาะกลุ่มที่มีอายุ >= 60 ปี)",JLabel.LEFT);
						ptExam33.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam33.setBounds(25,610,350,20);					
						Panel43.add(ptExam33);
						
						TextField_occ3 = new JTextField();
						TextField_occ3.setBounds(400,610,50,20);
						TextField_occ3.addActionListener(this);
						TextField_occ3.setEnabled(false);
						TextField_occ3.setText("0");
						if(checkAge()==4){
							TextField_occ3.setEnabled(true);
							TextField_occ3.setText("");
						}
						Panel43.add(TextField_occ3);
						
						ptExam4=new JLabel("4.บริการที่ควรได้รับ ",JLabel.LEFT);
						ptExam4.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam4.setBounds(15,635,180,20);					
						Panel43.add(ptExam4);
						
						ptExam41=new JLabel("1.การทาฟูลออไรด์",JLabel.LEFT);
						ptExam41.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam41.setBounds(25,660,100,20);					
						Panel43.add(ptExam41);
						
						FNButton=new JRadioButton [2];
						groupFN = new ButtonGroup();
						for(int row=0;row<2;row++){
							int x=140;
							if(row==1){
								FNButton[row] = new JRadioButton("ไม่จำเป็นf");						  
							 }else{
								 FNButton[row] = new JRadioButton("จำเป็นf");
							 }				 
							FNButton[row].setActionCommand( FNButton[row].getText() );
							FNButton[row].addActionListener(this);				
							FNButton[row].setBounds(x+(row*100),660,100,20);
							FNButton[row].setBackground(new Color(207,229,233));					
							Panel43.add(FNButton[row]);
							groupFN.add(FNButton[row]);				 
						}
						
						ptExam42=new JLabel("2.การขูดหินน้ำลาย   ",JLabel.LEFT);
						ptExam42.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam42.setBounds(25,690,250,20);				
						Panel43.add(ptExam42);
						
						SNButton=new JRadioButton [2];
						groupSN = new ButtonGroup();
						for(int row=0;row<2;row++){
							int x=140;
							if(row==1){
								SNButton[row] = new JRadioButton("ไม่จำเป็นs");					  
							 }else{
								 SNButton[row] = new JRadioButton("จำเป็นs");
							 }			
							SNButton[row].setActionCommand( SNButton[row].getText() );
							SNButton[row].addActionListener(this);					 				
							SNButton[row].setBounds(x+(row*100),690,100,20);
							SNButton[row].setBackground(new Color(207,229,233));										
							Panel43.add(SNButton[row]);
							groupSN.add(SNButton[row]);				 
						}
						
						ptExam43=new JLabel("3.การใส่ฟันเทียม ",JLabel.LEFT);
						ptExam43.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam43.setBounds(25,720,250,20);					
						Panel43.add(ptExam43);
						
						String [] prosNeed={"ต้องใส่ฟันบนและล่าง","ต้องใส่ฟันบน","ต้องใส่ฟันล่าง","ไม่ต้องใส่ฟันเทียม"};
						int l_prosNeed = prosNeed.length;			
						ProsNButton=new JRadioButton [l_prosNeed];
						groupProsN = new ButtonGroup();
						for(int row=0;row<l_prosNeed;row++){
							int x=140;
							if(row==3){
								ProsNButton[row] = new JRadioButton(prosNeed[row]);						  
							 }else{
								ProsNButton[row] = new JRadioButton(prosNeed[row]);
							 }					 
							ProsNButton[row].setActionCommand( ProsNButton[row].getText() );
							ProsNButton[row].addActionListener(this);					 				
							ProsNButton[row].setBounds(x+(row*120),720,120,20);
							ProsNButton[row].setBackground(new Color(207,229,233));										
							Panel43.add(ProsNButton[row]);
							groupProsN.add(ProsNButton[row]);				 
						}
						provider=new JLabel("ผู้ตรวจ  ",JLabel.LEFT);
						provider.setFont(new Font("Tahoma", Font.PLAIN, 13));
						provider.setBounds(25,750,300,20);					
						Panel43.add(provider);
						
						TextField_provider = new JTextField();
						TextField_provider.setBounds(150,750,50,20);
						TextField_provider.setText("");
						TextField_provider.addActionListener(this);
						Panel43.add(TextField_provider);
						
						String sql_43 = "select perio,occpetope,occpetopr,occprtopr,fluneed,sneed,prosneed,provider  from dhis where  hn= '"+oUserInfo.GetPtHN()+"'  and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
						Connection conn431;
						PreparedStatement stmt431;
						try {
							conn431 = new DBmanager().getConnMySql();	 
							stmt431 = conn431.prepareStatement(sql_43);	
							ResultSet rs43 = stmt431.executeQuery();
							while (rs43.next()) {
								if(rs43.getString(1) ==null){
									String [] dex4={"","",""};
									for(int r=0;r<dex4.length;r++){
										dataExam301.add(dex4[r]);
									}			           
						            dataExam201.add(dataExam301); 
						            String [] dex5={"","",""};
									for(int r=0;r<dex5.length;r++){
										dataExam401.add(dex5[r]);
									}
						            dataExam201.add(dataExam401); 
								}
								else if(rs43.getString(1) !=null ){
									if( rs43.getString(1).equals("")) {
										String [] dex4={"","",""};
										for(int r=0;r<dex4.length;r++){
											dataExam301.add(dex4[r]);
										}			           
							            dataExam201.add(dataExam301); 
							            String [] dex5={"","",""};
										for(int r=0;r<dex5.length;r++){
											dataExam401.add(dex5[r]);
										}
							            dataExam201.add(dataExam401); 
									}else {
										String perio_text=rs43.getString(1).trim(); 	
										char[] perioArray = perio_text.toCharArray();
										for(int r=0;r<3;r++){
											dataExam301.add(String.valueOf(perioArray[r]));
										}
										dataExam201.add(dataExam301);
										for(int r=3;r<6;r++){
											dataExam401.add(String.valueOf(perioArray[r]));
										}
										dataExam201.add(dataExam401);
									}
									 
								}
							
								if(rs43.getString(2) ==null){							
								}
								else if(rs43.getString(2) !=null){
									TextField_occ1.setText(rs43.getString(2).trim());
								}						
								if(rs43.getString(3) ==null){							
								}
								else if(rs43.getString(3) !=null){
									TextField_occ2.setText(rs43.getString(3).trim());
								}
								if(rs43.getString(4) ==null){							
								}
								else if(rs43.getString(4) !=null){
									TextField_occ3.setText(rs43.getString(4).trim());
								}
								if(rs43.getString(5) ==null){						
								}
								else if(rs43.getString(5) !=null){
									if(rs43.getString(5).trim().equals("1")){
										FNButton[0].setSelected(true);
									}
									else if(rs43.getString(5).trim().equals("2")){
										FNButton[1].setSelected(true);
									}						 
								}						
								if(rs43.getString(6) ==null){							
								}
								else if(rs43.getString(6) !=null){
									if(rs43.getString(6).trim().equals("1")){
										SNButton[0].setSelected(true);
									}
									else if(rs43.getString(6).trim().equals("2")){
										SNButton[1].setSelected(true);
									}						 
								}						
								if(rs43.getString(7) ==null){						 
								}
								else if(rs43.getString(7) !=null){
									if(rs43.getString(7).trim().equals("1")){
										ProsNButton[0].setSelected(true);
									}
									else if(rs43.getString(7).trim().equals("2")){
										ProsNButton[1].setSelected(true);
									}
									else if(rs43.getString(7).trim().equals("3")){
										ProsNButton[2].setSelected(true);
									}
									else if(rs43.getString(7).trim().equals("4")){
										ProsNButton[3].setSelected(true);
									}							 
								}						
								if(rs43.getString(8) ==null){
									TextField_provider.setText("047");
								}
								else if(rs43.getString(8) !=null){
									TextField_provider.setText(rs43.getString(8).trim());
								}						 					 
							}		 
							stmt431.close(); 
							conn431.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						DefaultTableModel modelExam201 = new DefaultTableModel(dataExam201, columnNamesExam2){
							public Class getColumnClass(int column){
								return getValueAt(0, column).getClass();
							}
						};
						
						tableExam2 = new JTable(modelExam201){
							public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
								JLabel c = (JLabel) super.prepareRenderer(renderer, row, column);
								c.setBackground(Color.WHITE);
								c.setForeground(Color.BLACK);
								c.setHorizontalAlignment(JLabel.CENTER);
						        c.setFont(new Font("Tahoma", Font.PLAIN, 13));
						        return c;
							}
						};
						TableColumnModel columnModelExam2 = tableExam2.getColumnModel();
						for(int cl2=0;cl2<3;cl2++){
					    	columnModelExam2.getColumn(cl2).setPreferredWidth(35);
						}
					    tableExam2.setPreferredScrollableViewportSize(new Dimension(560, 70));
						tableExam2.setFillsViewportHeight(true);
						tableExam2.setEnabled(true);
						tableExam2.setRowHeight(35);
						tableExam2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						tableExam2.setBounds(40,440,420,70);
						tableExam2.addMouseListener(this);
						mid2.add(tableExam2);
						
					
					scrollPane43 = new JScrollPane(Panel43);
					//scrollPane43.setPreferredSize(new Dimension(500,500));
					//scrollPane43.setViewportView(Panel43);
					mid2.add(scrollPane43, BorderLayout.CENTER);
					
					mid2.revalidate();
					mid2.repaint();	
					MiddleSection.revalidate();
					MiddleSection.repaint();
					//right
					right1.removeAll();
					ButtonSaveExam3 = new JButton("Save");		 
					ButtonSaveExam3.setBounds(5,10,80,30);
					ButtonSaveExam3.addActionListener(this);
					ButtonPrintExam1 = new JButton("Print");		 
					ButtonPrintExam1.setBounds(5,50,80,30);
					ButtonPrintExam1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							PrintOPD(formcode,"2",page,form_clinic_name);
							
						}
					});
					
					
					Button432 = new JButton("To 43");		 
					Button432.setBounds(5,90,80,30);
					Button432.addActionListener(this);
					if(oUserInfo.GetPtVN().equals("")){
						right1.add(ButtonSaveExam3);
						right1.add(ButtonPrintExam1);
						right1.add(Button432); 
					}else{
						 
					}
					right1.revalidate();
					right1.repaint();
					RightSection.revalidate();
					RightSection.repaint();
				}
				
			}
		}
		else if(formcode.equals("00113")){
			exam_status=0;
			if(oUserInfo.GetPtHN().length() !=7){
				JOptionPane.showMessageDialog(this,"Please select Patient","Patient Error",JOptionPane.ERROR_MESSAGE);

			}else{
				checkExam(oUserInfo.GetPtHN());
				//right Section
				
				if(oUserInfo.GetPtHN().length() !=7){
					JOptionPane.showMessageDialog(this,"Please select Patient","Patient Error",JOptionPane.ERROR_MESSAGE);

				}else{
					mid2.removeAll();
					checkExam(oUserInfo.GetPtHN());
					
					Panel43 = new JPanel();
					Panel43.setPreferredSize(new Dimension((screen.width*2)/10, 800));
					Panel43.setLayout(null);
					
					JLabel ptType,ptEdu,ptEdulevel,ptExam1,ptExam2,ptExam3,ptExam4;
					String [] ptTypeB={"หญิงตั้งครรภ์","เด็กก่อนวัยเรียน","เด็กวัยเรียน","ผู้สูงอายุ","กลุ่มอื่นๆ"};
					String [] ptEduB={"ศพด.","ประถม-รัฐบาล","ประถม-เทศบาล","ประถม-ท้องถิ่น","ประถม-เอกชน","มัธยม-รัฐบาล","มัธยม-เทศบาล","มัธยม-ท้องถิ่น","มัธยม-เอกชน","ไม่เลือก"};
					String [] ptEdulevelB={"ศพด.1-3","ประถม1-6","มัธยม1-6"};
					
					ptType=new JLabel("ประเภท ",JLabel.LEFT);
					ptType.setBounds(5,1,150,20);
					ptType.setFont(new Font("Tahoma", Font.PLAIN, 13));
					Panel43.add(ptType);
					int l_ptTypeB=ptTypeB.length;
					ptTypeBButton=new JRadioButton [l_ptTypeB];
					groupTypeB = new ButtonGroup();
					for(int row=0;row<l_ptTypeB;row++){
						int x=30;
						ptTypeBButton[row] = new JRadioButton(ptTypeB[row]);
						ptTypeBButton[row].setBounds(x+(row*130),20,120,20);
						ptTypeBButton[row].setBackground(new Color(207,229,233));
						ptTypeBButton[row].setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptTypeBButton[row].setActionCommand( ptTypeBButton[row].getText() );
						ptTypeBButton[row].addActionListener(this); 
						Panel43.add(ptTypeBButton[row]);
						groupTypeB.add(ptTypeBButton[row]);
					}
					//
					ptEdu=new JLabel("สถานศึกษา (กรณีเด็กวัยเรียน) ",JLabel.LEFT);
					ptEdu.setBounds(5,45,200,20);
					ptEdu.setFont(new Font("Tahoma", Font.PLAIN, 13));
					Panel43.add(ptEdu);
					
					int l_ptEduB=ptEduB.length;
					ptEduBButton=new JRadioButton [l_ptEduB];
					groupEduB = new ButtonGroup();
					for(int row=0;row<10;row++){
						if(row<5){
						int x=30;
						 
						ptEduBButton[row] = new JRadioButton(ptEduB[row]);
						ptEduBButton[row].setActionCommand( ptEduBButton[row].getText() );
						ptEduBButton[row].addActionListener(this);
						ptEduBButton[row].setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptEduBButton[row].setBounds(x+(row*130),70,120,20);
						ptEduBButton[row].setBackground(new Color(207,229,233));
						if(checkAge()==3){
							ptEduBButton[row].setEnabled(true);
						}else{
							ptEduBButton[row].setEnabled(false);
						}
						Panel43.add(ptEduBButton[row]);
						groupEduB.add(ptEduBButton[row]);
						}else if(row>4 && row<10){
							int x=30;
							 
							ptEduBButton[row] = new JRadioButton(ptEduB[row]);
							ptEduBButton[row].setActionCommand( ptEduBButton[row].getText() );
							ptEduBButton[row].addActionListener(this);
							ptEduBButton[row].setFont(new Font("Tahoma", Font.PLAIN, 13));
							ptEduBButton[row].setBounds(x+((row-5)*130),100,120,20);
							ptEduBButton[row].setBackground(new Color(207,229,233));
							if(checkAge()==3){
								ptEduBButton[row].setEnabled(true);
							}else{
								ptEduBButton[row].setEnabled(false);
							}
							Panel43.add(ptEduBButton[row]);
							groupEduB.add(ptEduBButton[row]);
						}
					}
					//
					ptEdulevel=new JLabel("ระดับการศึกษา (กรณีเด็กวัยเรียน) ",JLabel.LEFT);
					ptEdulevel.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptEdulevel.setBounds(5,140,250,20);				
					Panel43.add(ptEdulevel);
									 
					Border textfield_b = new SoftBevelBorder(BevelBorder.LOWERED);
					TextField_EdulevelB = new JTextField();
					TextField_EdulevelB.setFont(new Font("Tahoma", Font.PLAIN, 13));
					TextField_EdulevelB.setBorder(textfield_b);
					TextField_EdulevelB.setBounds(200,140,50,20);
					TextField_EdulevelB.setText("");
					TextField_EdulevelB.addActionListener(this);
					Panel43.add(TextField_EdulevelB);
					//
					ptExam1=new JLabel("1.การตรวจในช่องปาก ",JLabel.LEFT);
					ptExam1.setFont(new Font("Tahoma", Font.PLAIN, 13));
					ptExam1.setBounds(5,180,200,20);
					Panel43.add(ptExam1);
					
					columnNamesExam1 = new Vector<String>();
					
					for(int c=0;c<16;c++){
						columnNamesExam1.add("");
					}
					
					Vector<Vector<String>> dataExam1 = new Vector<Vector<String>>();
					final Vector<String> dataExam = new Vector<String>();
					final Vector<String> dataExam2 = new Vector<String>();
					
					String sql_432 = "select * from dhis where  hn= '"+oUserInfo.GetPtHN()+"' and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
					Connection conn43;
					PreparedStatement stmt43,stmt433;
					try {
						conn43 = new DBmanager().getConnMySql(); 
						stmt43 = conn43.prepareStatement(sql_432);
						 
						ResultSet rs43 = stmt43.executeQuery();
					 
						while (rs43.next()) {
							if(rs43.getString(6) ==null){
								//ptTypeBButton[4].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("1")){
								ptTypeBButton[0].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("2")){
								ptTypeBButton[1].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("3")){
								ptTypeBButton[2].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("4")){
								ptTypeBButton[3].setSelected(true);
							}
							else if(rs43.getString(6).trim().equals("5")){
								ptTypeBButton[4].setSelected(true);
							}
							if(rs43.getString(7) ==null || rs43.getString(7).trim().equals("0")){
								 
							}
							else if(rs43.getString(7).trim().equals("1")){
								ptEduBButton[0].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("2")){
								ptEduBButton[1].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("3")){
								ptEduBButton[2].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("4")){
								ptEduBButton[3].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("5")){
								ptEduBButton[4].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("6")){
								ptEduBButton[5].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("7")){
								ptEduBButton[6].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("8")){
								ptEduBButton[7].setSelected(true);
							}
							else if(rs43.getString(7).trim().equals("9")){
								ptEduBButton[8].setSelected(true);
							}
					
							if(rs43.getString(8) ==null || rs43.getString(8).trim().equals("0")){
								TextField_EdulevelB.setText("");
							}
							else  if(rs43.getString(8) !=null){
								TextField_EdulevelB.setText(rs43.getString(8).trim());
							}
								
								
							for(int r=9;r<25;r++){
								//System.out.println(rs43.getString(r));
								if(rs43.getString(r) ==null){
									dataExam.add("");
								}							
								else if(rs43.getString(r) !=null){
									dataExam.add(rs43.getString(r).trim());
									//System.out.println(rs43.getString(r).trim());
								}							 
							}
							dataExam1.add(dataExam); 					 					 
						}
						stmt43.close(); 
						stmt433 = conn43.prepareStatement(sql_432);
						ResultSet rs433 = stmt433.executeQuery();
						while (rs433.next()) {						
							for(int r=25;r<41;r++){
								if(rs433.getString(r) ==null){
									dataExam.add("");
								}
								else if(rs433.getString(r) !=null){
									dataExam2.add(rs433.getString(r).trim());
									//System.out.println(rs433.getString(r).trim());
								} 
							}
							dataExam1.add(dataExam2); 					 					 
						}
						stmt433.close();					 
						conn43.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DefaultTableModel modelExam1 = new DefaultTableModel(dataExam1, columnNamesExam1){
						public Class getColumnClass(int column){
							return getValueAt(0, column).getClass();
						}
					};
					tableExam1 = new JTable(modelExam1){
						public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
							JLabel c = (JLabel) super.prepareRenderer(renderer, row, column);
							c.setBackground(Color.WHITE);
							c.setForeground(Color.BLACK);
							c.setHorizontalAlignment(JLabel.CENTER);
					        c.setFont(new Font("Tahoma", Font.PLAIN, 13));
					        return c;
						}
					};
					TableColumnModel columnModelExam1 = tableExam1.getColumnModel();
				    for(int cl=0;cl<16;cl++){
				    	columnModelExam1.getColumn(cl).setPreferredWidth(35);
					}
				    tableExam1.setPreferredScrollableViewportSize(new Dimension(560, 70));
					tableExam1.setFillsViewportHeight(true);
					tableExam1.setEnabled(true);
					tableExam1.setRowHeight(35);
					tableExam1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					tableExam1.setBounds(40,260,560,70);
					tableExam1.addMouseListener(this);
					Panel43.add(tableExam1);
					//
					String [] tooth_dUP={"55","54","53","52","51","61","62","63","64","65"};
					String [] tooth_dLOW={"85","84","83","82","81","71","72","73","74","75"};
					int l_tooth_dUP=tooth_dUP.length;
					int l_tooth_dLOW=tooth_dLOW.length;
					
					 JLabel[] ltooth_dUP=new JLabel[l_tooth_dUP];
					 for(int row_tooth_dUP=0;row_tooth_dUP<l_tooth_dUP;row_tooth_dUP++){
						 int x_tooth_dUP=155;
						 ltooth_dUP[row_tooth_dUP] = new JLabel(tooth_dUP[row_tooth_dUP],JLabel.CENTER);		 
						 ltooth_dUP[row_tooth_dUP].setBounds(x_tooth_dUP+(row_tooth_dUP*35),225,20,20);
						 Panel43.add(ltooth_dUP[row_tooth_dUP]);
					 }
					  
					 JLabel[] ltooth_dLOW=new JLabel[l_tooth_dLOW];
					 for(int row_tooth_dLOW=0;row_tooth_dLOW<l_tooth_dLOW;row_tooth_dLOW++){
						 int x_tooth_dLOW=155;
						 ltooth_dLOW[row_tooth_dLOW] = new JLabel(tooth_dLOW[row_tooth_dLOW],JLabel.CENTER);		 
						 ltooth_dLOW[row_tooth_dLOW].setBounds(x_tooth_dLOW+(row_tooth_dLOW*35),350,20,20);
						 Panel43.add(ltooth_dLOW[row_tooth_dLOW]);
					 }
					 String [] tooth_pUP={"18","17","16","15","14","13","12","11","21","22","23","24","25","26","27","28"};
					 String [] tooth_pLOW={"48","47","46","45","44","43","42","41","31","32","33","34","35","36","37","38"};
					 int l_tooth_pUP=tooth_pUP.length;
					 int l_tooth_pLOW=tooth_pLOW.length;
					 
					 JLabel[] ltooth_pUP=new JLabel[l_tooth_pUP];
					 for(int row_tooth_pUP=0;row_tooth_pUP<l_tooth_pUP;row_tooth_pUP++){
						 int x_tooth_pUP=50;
						 ltooth_pUP[row_tooth_pUP] = new JLabel(tooth_pUP[row_tooth_pUP],JLabel.CENTER);		 
						 ltooth_pUP[row_tooth_pUP].setBounds(x_tooth_pUP+(row_tooth_pUP*35),240,20,20);
						 Panel43.add(ltooth_pUP[row_tooth_pUP]);
					 }
					 JLabel[] ltooth_pLOW=new JLabel[l_tooth_pLOW];
					 for(int row_tooth_pLOW=0;row_tooth_pLOW<l_tooth_pLOW;row_tooth_pLOW++){
						 int x_tooth_pLOW=50;
						 ltooth_pLOW[row_tooth_pLOW] = new JLabel(tooth_pLOW[row_tooth_pLOW],JLabel.CENTER);		 
						 ltooth_pLOW[row_tooth_pLOW].setBounds(x_tooth_pLOW+(row_tooth_pLOW*35),335,20,20);
						 Panel43.add(ltooth_pLOW[row_tooth_pLOW]);
						 
					 }
					//end sec1
					 
					 JLabel ptExam201,ptExam301,ptExam401,ptExam31,ptExam32,ptExam33,ptExam41,ptExam42,ptExam43,provider;
						ptExam201=new JLabel("2.สภาวะปริทันต์ ",JLabel.LEFT);
						ptExam201.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam201.setBounds(15,380,200,20);
						Panel43.add(ptExam201);
						
						columnNamesExam2 = new Vector<String>();			
						columnNamesExam2.add("");
						columnNamesExam2.add("");
						columnNamesExam2.add("");
						Vector<Vector<String>> dataExam201 = new Vector<Vector<String>>();
						final Vector<String> dataExam301 = new Vector<String>();
						final Vector<String> dataExam401 = new Vector<String>();
						String [] pLabel_UP={"ฟันหลังบนขวา","ฟันหน้าบน","ฟันหลังบนซ้าย"};
						String [] pLabel_LOW={"ฟันหลังล่างขวา","ฟันหน้าล่าง","ฟันหลังล่างซ้าย"};
						int l_pLabel_UP=pLabel_UP.length;
						int l_pLabel_LOW=pLabel_LOW.length;
						JLabel[] p_UP=new JLabel[l_pLabel_UP];
						 for(int row_p_UP=0;row_p_UP<l_pLabel_UP;row_p_UP++){
							 int x_UP=50;
							 p_UP[row_p_UP] = new JLabel(pLabel_UP[row_p_UP],JLabel.CENTER);		 
							 p_UP[row_p_UP].setBounds(x_UP+(row_p_UP*140),410,100,20);
							 Panel43.add(p_UP[row_p_UP]);
						 }
						  
						 JLabel[] p_LOW=new JLabel[l_pLabel_LOW];
						 for(int row_p_LOW=0;row_p_LOW<l_pLabel_LOW;row_p_LOW++){
							 int x_tooth_dLOW=50;
							 p_LOW[row_p_LOW] = new JLabel(pLabel_LOW[row_p_LOW],JLabel.CENTER);		 
							 p_LOW[row_p_LOW].setBounds(x_tooth_dLOW+(row_p_LOW*140),520,100,20);
							 Panel43.add(p_LOW[row_p_LOW]);
						 }
						
						
						ptExam3=new JLabel("3.การสบฟัน",JLabel.LEFT);
						ptExam3.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam3.setBounds(15,550,180,20);
						Panel43.add(ptExam3);
						
						ptExam31=new JLabel("1.จำนวนคู่สบฟันแท้กับฟันแท้ ",JLabel.LEFT);
						ptExam31.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam31.setBounds(25,570,350,20);				
						Panel43.add(ptExam31);
						
						TextField_occ1 = new JTextField();
						TextField_occ1.setBounds(400,570,50,20);
						TextField_occ1.setText("0");
						TextField_occ1.addActionListener(this);
						Panel43.add(TextField_occ1);
						
						ptExam32=new JLabel("2.จำนวนคู่สบฟันแท้กับฟันเทียม (เฉพาะกลุ่มที่มีอายุ >= 60 ปี)",JLabel.LEFT);
						ptExam32.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam32.setBounds(25,590,350,20);					
						Panel43.add(ptExam32);
						
						TextField_occ2 = new JTextField();
						TextField_occ2.setBounds(400,590,50,20);
						TextField_occ2.addActionListener(this);
						TextField_occ2.setEnabled(false);
						TextField_occ2.setText("0");
						if(checkAge()==4){
							TextField_occ2.setEnabled(true);
							TextField_occ2.setText("");					 
						}
						Panel43.add(TextField_occ2);
						
						ptExam33=new JLabel("3.จำนวนคู่สบฟันเทียมกับฟันเทียม (เฉพาะกลุ่มที่มีอายุ >= 60 ปี)",JLabel.LEFT);
						ptExam33.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam33.setBounds(25,610,350,20);					
						Panel43.add(ptExam33);
						
						TextField_occ3 = new JTextField();
						TextField_occ3.setBounds(400,610,50,20);
						TextField_occ3.addActionListener(this);
						TextField_occ3.setEnabled(false);
						TextField_occ3.setText("0");
						if(checkAge()==4){
							TextField_occ3.setEnabled(true);
							TextField_occ3.setText("");
						}
						Panel43.add(TextField_occ3);
						
						ptExam4=new JLabel("4.บริการที่ควรได้รับ ",JLabel.LEFT);
						ptExam4.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam4.setBounds(15,635,180,20);					
						Panel43.add(ptExam4);
						
						ptExam41=new JLabel("1.การทาฟูลออไรด์",JLabel.LEFT);
						ptExam41.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam41.setBounds(25,660,100,20);					
						Panel43.add(ptExam41);
						
						FNButton=new JRadioButton [2];
						groupFN = new ButtonGroup();
						for(int row=0;row<2;row++){
							int x=140;
							if(row==1){
								FNButton[row] = new JRadioButton("ไม่จำเป็นf");						  
							 }else{
								 FNButton[row] = new JRadioButton("จำเป็นf");
							 }				 
							FNButton[row].setActionCommand( FNButton[row].getText() );
							FNButton[row].addActionListener(this);				
							FNButton[row].setBounds(x+(row*100),660,100,20);
							FNButton[row].setBackground(new Color(207,229,233));					
							Panel43.add(FNButton[row]);
							groupFN.add(FNButton[row]);				 
						}
						
						ptExam42=new JLabel("2.การขูดหินน้ำลาย   ",JLabel.LEFT);
						ptExam42.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam42.setBounds(25,690,250,20);				
						Panel43.add(ptExam42);
						
						SNButton=new JRadioButton [2];
						groupSN = new ButtonGroup();
						for(int row=0;row<2;row++){
							int x=140;
							if(row==1){
								SNButton[row] = new JRadioButton("ไม่จำเป็นs");					  
							 }else{
								 SNButton[row] = new JRadioButton("จำเป็นs");
							 }			
							SNButton[row].setActionCommand( SNButton[row].getText() );
							SNButton[row].addActionListener(this);					 				
							SNButton[row].setBounds(x+(row*100),690,100,20);
							SNButton[row].setBackground(new Color(207,229,233));										
							Panel43.add(SNButton[row]);
							groupSN.add(SNButton[row]);				 
						}
						
						ptExam43=new JLabel("3.การใส่ฟันเทียม ",JLabel.LEFT);
						ptExam43.setFont(new Font("Tahoma", Font.PLAIN, 13));
						ptExam43.setBounds(25,720,250,20);					
						Panel43.add(ptExam43);
						
						String [] prosNeed={"ต้องใส่ฟันบนและล่าง","ต้องใส่ฟันบน","ต้องใส่ฟันล่าง","ไม่ต้องใส่ฟันเทียม"};
						int l_prosNeed = prosNeed.length;			
						ProsNButton=new JRadioButton [l_prosNeed];
						groupProsN = new ButtonGroup();
						for(int row=0;row<l_prosNeed;row++){
							int x=140;
							if(row==3){
								ProsNButton[row] = new JRadioButton(prosNeed[row]);						  
							 }else{
								ProsNButton[row] = new JRadioButton(prosNeed[row]);
							 }					 
							ProsNButton[row].setActionCommand( ProsNButton[row].getText() );
							ProsNButton[row].addActionListener(this);					 				
							ProsNButton[row].setBounds(x+(row*120),720,120,20);
							ProsNButton[row].setBackground(new Color(207,229,233));										
							Panel43.add(ProsNButton[row]);
							groupProsN.add(ProsNButton[row]);				 
						}
						provider=new JLabel("ผู้ตรวจ  ",JLabel.LEFT);
						provider.setFont(new Font("Tahoma", Font.PLAIN, 13));
						provider.setBounds(25,750,300,20);					
						Panel43.add(provider);
						
						TextField_provider = new JTextField();
						TextField_provider.setBounds(150,750,50,20);
						TextField_provider.setText("");
						TextField_provider.addActionListener(this);
						Panel43.add(TextField_provider);
						
						String sql_43 = "select perio,occpetope,occpetopr,occprtopr,fluneed,sneed,prosneed,provider  from dhis where  hn= '"+oUserInfo.GetPtHN()+"'  and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"'";
						Connection conn431;
						PreparedStatement stmt431;
						try {
							conn431 = new DBmanager().getConnMySql();	 
							stmt431 = conn431.prepareStatement(sql_43);	
							ResultSet rs43 = stmt431.executeQuery();
							while (rs43.next()) {
								
								if(rs43.getString(1) ==null){
									String [] dex4={"","",""};
									for(int r=0;r<dex4.length;r++){
										dataExam301.add(dex4[r]);
									}			           
						            dataExam201.add(dataExam301); 
						            String [] dex5={"","",""};
									for(int r=0;r<dex5.length;r++){
										dataExam401.add(dex5[r]);
									}
						            dataExam201.add(dataExam401); 
								}
								else if(rs43.getString(1) !=null){
									if(rs43.getString(1).equals("")) {
										String [] dex4={"","",""};
										for(int r=0;r<dex4.length;r++){
											dataExam301.add(dex4[r]);
										}			           
							            dataExam201.add(dataExam301); 
							            String [] dex5={"","",""};
										for(int r=0;r<dex5.length;r++){
											dataExam401.add(dex5[r]);
										}
							            dataExam201.add(dataExam401); 
									}else {
										String perio_text=rs43.getString(1).trim(); 	
										char[] perioArray = perio_text.toCharArray();
										for(int r=0;r<3;r++){
											dataExam301.add(String.valueOf(perioArray[r]));
										}
										dataExam201.add(dataExam301);
										for(int r=3;r<6;r++){
											dataExam401.add(String.valueOf(perioArray[r]));
										}
										dataExam201.add(dataExam401);
									}
									 
								}
								if(rs43.getString(2) ==null){							
								}
								else if(rs43.getString(2) !=null){
									TextField_occ1.setText(rs43.getString(2).trim());
								}						
								if(rs43.getString(3) ==null){							
								}
								else if(rs43.getString(3) !=null){
									TextField_occ2.setText(rs43.getString(3).trim());
								}
								if(rs43.getString(4) ==null){							
								}
								else if(rs43.getString(4) !=null){
									TextField_occ3.setText(rs43.getString(4).trim());
								}
								if(rs43.getString(5) ==null){						
								}
								else if(rs43.getString(5) !=null){
									if(rs43.getString(5).trim().equals("1")){
										FNButton[0].setSelected(true);
									}
									else if(rs43.getString(5).trim().equals("2")){
										FNButton[1].setSelected(true);
									}						 
								}						
								if(rs43.getString(6) ==null){							
								}
								else if(rs43.getString(6) !=null){
									if(rs43.getString(6).trim().equals("1")){
										SNButton[0].setSelected(true);
									}
									else if(rs43.getString(6).trim().equals("2")){
										SNButton[1].setSelected(true);
									}						 
								}						
								if(rs43.getString(7) ==null){						 
								}
								else if(rs43.getString(7) !=null){
									if(rs43.getString(7).trim().equals("1")){
										ProsNButton[0].setSelected(true);
									}
									else if(rs43.getString(7).trim().equals("2")){
										ProsNButton[1].setSelected(true);
									}
									else if(rs43.getString(7).trim().equals("3")){
										ProsNButton[2].setSelected(true);
									}
									else if(rs43.getString(7).trim().equals("4")){
										ProsNButton[3].setSelected(true);
									}							 
								}						
								if(rs43.getString(8) ==null){
									TextField_provider.setText("047");
								}
								else if(rs43.getString(8) !=null){
									TextField_provider.setText(rs43.getString(8).trim());
								}						 					 
							}		 
							stmt431.close(); 
							conn431.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						DefaultTableModel modelExam201 = new DefaultTableModel(dataExam201, columnNamesExam2){
							public Class getColumnClass(int column){
								return getValueAt(0, column).getClass();
							}
						};
						
						tableExam2 = new JTable(modelExam201){
							public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
								JLabel c = (JLabel) super.prepareRenderer(renderer, row, column);
								c.setBackground(Color.WHITE);
								c.setForeground(Color.BLACK);
								c.setHorizontalAlignment(JLabel.CENTER);
						        c.setFont(new Font("Tahoma", Font.PLAIN, 13));
						        return c;
							}
						};
						TableColumnModel columnModelExam2 = tableExam2.getColumnModel();
						for(int cl2=0;cl2<3;cl2++){
					    	columnModelExam2.getColumn(cl2).setPreferredWidth(35);
						}
					    tableExam2.setPreferredScrollableViewportSize(new Dimension(560, 70));
						tableExam2.setFillsViewportHeight(true);
						tableExam2.setEnabled(true);
						tableExam2.setRowHeight(35);
						tableExam2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						tableExam2.setBounds(40,440,420,70);
						tableExam2.addMouseListener(this);
						mid2.add(tableExam2);
						
					
					scrollPane43 = new JScrollPane(Panel43);
					//scrollPane43.setPreferredSize(new Dimension(500,500));
					//scrollPane43.setViewportView(Panel43);
					mid2.add(scrollPane43, BorderLayout.CENTER);
					
					mid2.revalidate();
					mid2.repaint();	
					MiddleSection.revalidate();
					MiddleSection.repaint();
					//right
					right1.removeAll();
					ButtonSaveExam3 = new JButton("Save");		 
					ButtonSaveExam3.setBounds(5,10,80,30);
					ButtonSaveExam3.addActionListener(this);
					ButtonPrintExam1 = new JButton("Print");		 
					ButtonPrintExam1.setBounds(5,50,80,30);
					ButtonPrintExam1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							PrintOPD(formcode,"2",page,form_clinic_name);
							
						}
					});
					
					Button432 = new JButton("To 43");		 
					Button432.setBounds(5,90,80,30);
					Button432.addActionListener(this);
					if(oUserInfo.GetPtVN().equals("")){
						right1.add(ButtonSaveExam3);
						right1.add(ButtonPrintExam1);
						right1.add(Button432); 
					}else{
						 
					}
					right1.revalidate();
					right1.repaint();
					RightSection.revalidate();
					RightSection.repaint();
				}
			
			}
		}
		
	}
	public void checkExam(String hn_check_in){
		String hn_check=hn_check_in;
		//int status_insert=0;
		int suc=0;
		String year43=Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()).trim().substring(0, 4);
		String date_stat=year43+"-10-01";
		String date_inti="";
		 
	 /////////////////////////////////////////////////////////////change point time to check 
		
		DateTime dt21 = new DateTime(date_stat);
		DateTime dt20 = new DateTime(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()).trim());
		//System.out.println("setting.."+dt2);
		if(dt20.isBefore(dt21)) {
			System.out.println("setting..thist"+dt21);
			
			date_inti=(Integer.parseInt(year43)-1)+"-10-01";	
			
		}else {
			System.out.println("setting..last"+dt21);
			date_inti=year43+"-10-01";	
		}
		DateTime dt2 = new DateTime(date_inti);
		Connection con6;
		PreparedStatement stmt6;
		ResultSet rs6 ;
		String sql_getHN="select  visitdate from dhis where hn=? order by visitdate desc LIMIT 1 ";
		try {
			con6 = new DBmanager().getConnMySql();
			stmt6 = con6.prepareStatement(sql_getHN);
			stmt6.setString(1, hn_check.trim()); 
			rs6 = stmt6.executeQuery();
			String old_visitdate="";
			 
			while( rs6.next() ){
				if(rs6.getString(1) !=null){
					old_visitdate=rs6.getString(1);
					//in
					DateTime dt1 = new DateTime(old_visitdate);
					 
					if(dt1.isBefore(dt2)){
						exam_status=1;
						// status_insert=1;
						 //System.out.println("1.."+dt2);
						//JOptionPane.showMessageDialog(this,"ลงข้อมูลแล้นวันที่ "+dt1,"Exam Note",JOptionPane.ERROR_MESSAGE);

					}
					//System.out.println("2.."+dt1);	
				} 
				suc=1;  	 
			}
			stmt6.close();
			con6.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(suc==0) {
			exam_status=1;
		}
		if(exam_status==1) {
			JOptionPane.showMessageDialog(this,"กรุณาพิมพ์แบบตรวจ 1 และ แบบตรวจ 2","Exam Note",JOptionPane.ERROR_MESSAGE);

		}
		/**
		if(suc==0){
			//status_insert=1;
		}else {
			
		}
		if((status_insert==0) ){
	    	exam_status=0;
	    	status_insert=0;
	    }
	    else if(status_insert==1){
			exam_status=1;
			status_insert=0;		
			JOptionPane.showMessageDialog(this,"กรุณาพิมพ์แบบตรวจ 1 และ แบบตรวจ 2","Exam Note",JOptionPane.ERROR_MESSAGE);
		}
		*/
		System.out.println("2.."+exam_status+"--"+hn_check);
		
	}
	public int checkAge(){
		 int result=1;
		 int age_check=0;
		 if(oUserInfo.GetPtAge().equals("")){
			 age_check = 0;
		 }else{
			 String cut_age=oUserInfo.GetPtAge().substring(0, oUserInfo.GetPtAge().indexOf("ปี")).trim();
			 age_check = Integer.parseInt(cut_age); 
		 }
		 if(age_check>=60){
				result=4 ;
			}
			else if(age_check<=20){
				result=3;
			}
		 return result;
		 
	}
	public void PrintOPD(String form_code,String typeofprint,String page,String form_clinic_name) {
		String h_print=typeofprint.trim();
		String confFile="report/"+form_code.trim()+".jrxml";
    	String filename=Setup.ConverttoScanDate(Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()))+"1100"+page+"0001"+oUserInfo.GetPtHN().trim();

    	int h_page=0;
		if(h_print.equals("1")){
    		h_page=430;
    	}else if(h_print.equals("2")){
    		h_page=840;
    	}
		InputStream is;
		JasperReportBuilder report = DynamicReports.report(); 

		report.setTemplate(template()).setPageFormat(590, h_page, PageOrientation.PORTRAIT);
		if(( form_code.equals("00115"))  ){
			 
			//report.setTemplate(template()).setPageFormat(590, 840, PageOrientation.PORTRAIT);
			try {
				is = this.getClass().getClassLoader().getResourceAsStream(confFile );
				report.setTemplateDesign(is);
				report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
				report.setParameter("line1","โรงพยาบาลอุตรดิตถ์  ");
				report.setParameter("line2",form_clinic_name );
				report.setParameter("line3", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge()+" HN:"+oUserInfo.GetPtHN()+" VN:"+oUserInfo.GetPtVN()+" เลขที่บัตรประชาชน: "+oUserInfo.GetPtCID());
				report.setParameter("line4","วันที่ "+Setup.ConvertDateTimePrint(day)+" น. "+" "+oUserInfo.GetRightCode());
				report.setParameter("line5","***การแพ้ยา***"+oUserInfo.GetMemo());
				report.setParameter("barcode",filename);
				report.setParameter("line6",oUserInfo.GetMemo1());
				report.setParameter("line7",form_code.trim()+".jrxml");
				
				report.setDataSource(new JREmptyDataSource());
				
				// System.out.println("NAME... "+name+"--"+hn+"-- vn "+vn);
				//report.print(true);
				report.show(false);
			} catch (DRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			
			exam_status=0;
		}
		else if(((form_code.equals("00112") || form_code.equals("00113") || form_code.equals("00116"))  )){
			try {
				is = this.getClass().getClassLoader().getResourceAsStream(confFile );
				report.setTemplateDesign(is);
				report.setPageMargin(DynamicReports.margin().setLeft(50).setRight(10).setTop(10).setBottom(10));
				report.setParameter("line1","โรงพยาบาลอุตรดิตถ์  ");
				report.setParameter("line2",form_clinic_name );
				report.setParameter("line3", "ชื่อ " +oUserInfo.GetPtName()+" อายุ "+oUserInfo.GetPtAge()+" HN:"+oUserInfo.GetPtHN()+" VN:"+oUserInfo.GetPtVN()+" เลขที่บัตรประชาชน: "+oUserInfo.GetPtCID());
				report.setParameter("line4","วันที่ "+Setup.ConvertDateTimePrint(day)+" น. "+" "+oUserInfo.GetRightCode());
				report.setParameter("line5","***การแพ้ยา***"+oUserInfo.GetMemo());
				report.setParameter("barcode",filename);
				report.setParameter("line6",oUserInfo.GetMemo1());
				report.setParameter("line7",form_code.trim()+".jrxml");
				
				report.setDataSource(new JREmptyDataSource());
				
				 System.out.println("NAME... "+oUserInfo.GetPtName()+"--"+oUserInfo.GetPtHN()+"-- vn "+oUserInfo.GetPtVN());
				//report.print(true);
				report.show(false);
			} catch (DRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(exam_status==1) {
				Connection con6;
				PreparedStatement stmt6;
				ResultSet rs6 ;
				String sql_addHN = "insert into  dhis ( visitdate,hn,d_update) values ('"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"','"+oUserInfo.GetPtHN()+"','"+Setup.GetDateNow()+"')";
				try {
					con6 = new DBmanager().getConnMySql();
					stmt6 = con6.prepareStatement(sql_addHN);
					int rs_save = stmt6.executeUpdate();
					stmt6.close();
					con6.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
			exam_status=0;

		 
		}
		
		
	}
	public void clearValue(){
		 
        oUserInfo.setPtVN("");  
        oUserInfo.setPtName("");        
        oUserInfo.setPtHN("");        
        oUserInfo.setRightCode("");        
        oUserInfo.setPtCliniccode("");         
        oUserInfo.setPtAge("");
        oUserInfo.setPtLabel("");       
        oUserInfo.setPtCID("");         
        oUserInfo.setMemo("");
        
         
	}
	public void clearPanel() {
		 mid2.removeAll();
			mid2.revalidate();
			mid2.repaint();	
			MiddleSection.revalidate();
			MiddleSection.repaint();
			//right
			right1.removeAll();
			right1.revalidate();
			right1.repaint();
			RightSection.revalidate();
			RightSection.repaint();
	}
	public void getDataTx(){
		tableTx.setModel(fetchDataTx());
		TableColumnModel columnModelTx = tableTx.getColumnModel();
		columnModelTx.getColumn(0).setPreferredWidth(30);
		columnModelTx.getColumn(1).setPreferredWidth(100);
		columnModelTx.getColumn(2).setPreferredWidth(80);
		columnModelTx.getColumn(3).setPreferredWidth(80);
		columnModelTx.getColumn(4).setPreferredWidth(80);
		columnModelTx.getColumn(5).setPreferredWidth(50);
		((DefaultTableModel)tableTx.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataTx(){

		Vector<Vector<String>> dataDataTx = new Vector<Vector<String>>();
		String sql_datatx = "select  visitdatetime,toothno,diag,treatment,doctor,typeplace from dental_activity where hn='"+oUserInfo.GetPtHN()+"' and hn !='' order by visitdatetime";
		
		//String sql_formopd = "select form_code,form_name,page  from formopd where type='1' and clinic= '"+clinic+"'  order by orderpage";
		Connection conn;
		PreparedStatement stmt;
		try {
			conn = new DBmanager().getConnMySql(); 
	        stmt = conn.prepareStatement(sql_datatx);	         
	        ResultSet rs = stmt.executeQuery();
	        int p=1;
	        
	        while (rs.next()) {
	        	final Vector<String> vstringDataTx = new Vector<String>();
	        	vstringDataTx.add(" "+Integer.toString(p));
	        	vstringDataTx.add(rs.getString(1).trim().substring(0, 10));	           
	        	vstringDataTx.add(rs.getString(2).trim());
	        	vstringDataTx.add(rs.getString(3).trim());
	        	vstringDataTx.add(rs.getString(4).trim());
	        	vstringDataTx.add(rs.getString(5).trim());
	        	 
		           
	        	dataDataTx.add(vstringDataTx); 
	            p++;
	        }
	        stmt.close();
	    	conn.close();
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		return new DefaultTableModel(dataDataTx, columnNamesTx);
	}
	public void getDataClearTx(){
		tableTx.setModel(fetchDataClearTx());
		TableColumnModel columnModelTx = tableTx.getColumnModel();
		columnModelTx.getColumn(0).setPreferredWidth(30);
		columnModelTx.getColumn(1).setPreferredWidth(100);
		columnModelTx.getColumn(2).setPreferredWidth(80);
		columnModelTx.getColumn(3).setPreferredWidth(80);
		columnModelTx.getColumn(4).setPreferredWidth(80);
		columnModelTx.getColumn(5).setPreferredWidth(50);
		((DefaultTableModel)tableTx.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataClearTx(){

		Vector<Vector<String>> dataDataTx = new Vector<Vector<String>>();
	
		return new DefaultTableModel(dataDataTx, columnNamesTx);
	}
	public void clearDataPt43_1(){
		groupTypeB.clearSelection();
		groupEduB.clearSelection();
		TextField_EdulevelB.setText("");
		tableExam1.setModel(fetchClearData43_1());
		TableColumnModel columnModelExam1 = tableExam1.getColumnModel();
		 
		((DefaultTableModel)tableExam1.getModel()).fireTableDataChanged();
		 
	}
	public void clearDataPt43_2(){
		TextField_occ1.setText("");
		TextField_occ2.setText("");
		TextField_occ3.setText("");
		TextField_provider.setText("");
		groupFN.clearSelection();
		groupSN.clearSelection();
		groupProsN.clearSelection();
		tableExam2.setModel(fetchClearData43_2());
		TableColumnModel columnModelExam2 = tableExam2.getColumnModel();
		 
		((DefaultTableModel)tableExam2.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchClearData43_1(){
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		return new DefaultTableModel(data, columnNamesExam1);
	}
	public  DefaultTableModel fetchClearData43_2(){
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		return new DefaultTableModel(data, columnNamesExam2);
	}
	public boolean checkValueTable(){
		
		String [] data_input1={"0","1","2","3","4","5","6","7","8","9","10","a","b","c","d","e","f","g","h","A","B","C","D","E","F","G","H"};
		String [] data_input2={"0","1","2","3","4","5","6","7","8","9","10"};
		
		int rowCount_check = tableExam1.getRowCount();
	    int colCount_check = tableExam1.getColumnCount();
	    for (int i = 0; i < rowCount_check; i++) {
	    	for (int j = 0; j < colCount_check; j++){
	    		
	    		if(Arrays.asList(data_input1).contains(tableExam1.getValueAt(i, j).toString())){
	    			
	    		}else{
	    			return false;
 	    		}  	    		
	    	}    	    	
	    }
	    
	    
	    	for (int j = 0; j < 3; j++){
	    		if(Arrays.asList(data_input2).contains(tableExam1.getValueAt(0, j).toString())){  			
	    		}else{
	    			return false;
 	    		}  	    		 	     	    	
	    }
	     	for (int j = 0; j < 3; j++){
	    		if(Arrays.asList(data_input2).contains(tableExam1.getValueAt(1, j).toString())){  			
	    		}else{
	    			return false;
 	    		}  	    		 	     	    	
	    }
	     	for (int j = 13; j < 16; j++){
	    		if(Arrays.asList(data_input2).contains(tableExam1.getValueAt(0, j).toString())){  			
	    		}else{
	    			return false;
 	    		}  	    		 	     	    	
	    }
	     	for (int j = 13; j < 16; j++){
	    		if(Arrays.asList(data_input2).contains(tableExam1.getValueAt(1, j).toString())){  			
	    		}else{
	    			return false;
 	    		}  	    		 	     	    	
	    }
			
		return true;
		
	}
	public boolean checkValueTableGum(){
	String [] data_guminput={"1","2","3","4","5","9"};
	int rowCount_check = tableExam2.getRowCount();
    int colCount_check = tableExam2.getColumnCount();
    for (int i = 0; i < rowCount_check; i++) {
    	for (int j = 0; j < colCount_check; j++){
    		
    		if(Arrays.asList(data_guminput).contains(tableExam2.getValueAt(i, j).toString())){
    			
    		}else{
    			return false;
	    		}  	    		
    	}    	    	
    }
	return true;
	}
}
