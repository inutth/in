package org.utt.app.opd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Properties;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.utt.app.InApp;
import org.utt.app.opd.ObjectOPD;
import org.utt.app.util.ComboItem;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.DateLabelFormatter;
import org.utt.app.util.Setup;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;


public class DentalReport extends JPanel{
	Dimension screen;
	String [][] txname=null ;
	String [][] reportM=null ;
	String [][] reportS=null ;
	String [][] icdcm=null ;
	String [][] doctorname=null ;
	String [][] categoryname=null ;
	String [][] subcategoryname=null ;
	
	JDatePicker picker1,picker2;
	
	JPanel LeftSection,MiddleSection,left1,left2;
	JButton buttonReport;
	JPanel mid1,mid2,jPanel1,jPanel2;
	JButton ButtonSearch;
	JComboBox cb1;
	JTable table,tableR;
	Vector<Vector<String>> data,dataReport;
	Vector<String> columnNames,columnNamesReport;
	JScrollPane scrollPane,scrollPaneR;
	
	String day1="",month1="",dateSearch1="",dateSearch2="",day2="",month2="";
	JButton btnNewButton;
	JLabel labelReport;
	JLabel labelTime;
	JButton ButtonCount,ButtonExcel;

	/**
	 * Create the panel.
	 */
	public DentalReport() {
		super();

		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width, screen.height-160));
		
		//countData();
				LeftSection = new JPanel();
				LeftSection.setPreferredSize(new Dimension(250, screen.height-160));
				//add(LeftSection, BorderLayout.WEST);
				LeftSection.setLayout(new BorderLayout(0, 0));
				
				left1 = new JPanel();
				left1.setPreferredSize(new Dimension(250, 80));
				LeftSection.add(left1, BorderLayout.NORTH);
				left1.setLayout(null);
				
				left2 = new JPanel();
				LeftSection.add(left2, BorderLayout.CENTER);
				left2.setLayout(null);
				
				buttonReport = new JButton("รายงานทันตกรรม");
				buttonReport.setBounds(20, 5, 113, 23);
				left2.add(buttonReport);
				
				btnNewButton = new JButton("New button");
				btnNewButton.setBounds(138, 5, 91, 23);
				left2.add(btnNewButton);
				btnNewButton.setEnabled(false);
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//insertData();
						//checkData();
					}
				});
				buttonReport.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//report1();
					}
				});
				
				
				jPanel1 = new JPanel();
				UtilDateModel model1 = new UtilDateModel();
				model1.setSelected(true);
				Properties p1 = new Properties();
				p1.put("text.today", "");
				p1.put("text.month", "");
				p1.put("text.year", "");
				JDatePanelImpl datePanel1 = new JDatePanelImpl(model1,p1);
				picker1 = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
				picker1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(picker1.getModel().getDay()<10){
							day1="0"+picker1.getModel().getDay();
						}else{
							day1=""+picker1.getModel().getDay();
						}
						if(picker1.getModel().getMonth()+1<10){
							month1="0"+(picker1.getModel().getMonth()+1);
						}else{
							month1=""+(picker1.getModel().getMonth()+1);
						}
						dateSearch1=picker1.getModel().getYear()+"-"+month1+"-"+day1;
						
					}
				});
				picker1.setShowYearButtons(true);
				picker1.setTextEditable(false);
				 
				jPanel1 = new JPanel();
				jPanel1.setBounds(35, 1, 220, 30);
				jPanel1.add((JComponent)picker1);
				
				jPanel2 = new JPanel();
				UtilDateModel model2 = new UtilDateModel();
				model2.setSelected(true);
				Properties p2 = new Properties();
				p2.put("text.today", "");
				p2.put("text.month", "");
				p2.put("text.year", "");
				JDatePanelImpl datePanel2 = new JDatePanelImpl(model2,p2);
				picker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
				picker2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(picker2.getModel().getDay()<10){
							day2="0"+picker2.getModel().getDay();
						}else{
							day2=""+picker2.getModel().getDay();
						}
						if(picker2.getModel().getMonth()+1<10){
							month2="0"+(picker2.getModel().getMonth()+1);
						}else{
							month2=""+(picker2.getModel().getMonth()+1);
						}
						dateSearch2=picker2.getModel().getYear()+"-"+month2+"-"+day2;
						
					}
				});
				picker2.setShowYearButtons(true);
				picker2.setTextEditable(false);
				 
				jPanel2 = new JPanel();
				jPanel2.setBounds(300, 1, 220, 30);
				jPanel2.add((JComponent)picker2);
				
				MiddleSection = new JPanel();
				MiddleSection.setBorder(new TitledBorder(null, "Dental Report", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				MiddleSection.setPreferredSize(new Dimension(screen.width, screen.height-160));
				//MiddleSection.setPreferredSize(new Dimension(screen.width-200, screen.height-160));
				
				add(MiddleSection, BorderLayout.CENTER);
				MiddleSection.setLayout(new BorderLayout(0, 0));
				
				mid1 = new JPanel();
				mid1.setPreferredSize(new Dimension(screen.width, 50));
				MiddleSection.add(mid1, BorderLayout.NORTH);
				mid1.setLayout(null);
				
				mid1.add(jPanel1);
				mid1.add(jPanel2);
				
				ButtonCount = new JButton("Count");
				ButtonCount.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getDataReport();
					}
				});
				ButtonCount.setBounds(730, 4, 89, 23);
				mid1.add(ButtonCount);
				
				ButtonExcel = new JButton("To Excel");
				ButtonExcel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getDataToExcel();
					}
				});
				ButtonExcel.setBounds(830, 4, 89, 23);
				mid1.add(ButtonExcel);
				
				JLabel label = new JLabel("เริ่ม");
				label.setFont(new Font("Tahoma", Font.PLAIN, 13));
				label.setBounds(10, 8, 30, 20);
				mid1.add(label);
				
				JLabel label_1 = new JLabel("สิ้นสุด");
				label_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
				label_1.setBounds(260, 8, 40, 20);
				mid1.add(label_1);
				
				cb1 = new JComboBox();
				cb1.setBounds(530, 5, 194, 22);	
				init();
				mid1.add(cb1);
				mid2 = new JPanel();
				mid2.setPreferredSize(new Dimension(screen.width, screen.height-210));
				MiddleSection.add(mid2, BorderLayout.CENTER);
				mid2.setLayout(null);
				
				labelReport = new JLabel("รายงานทันตกรรม");
				labelReport.setFont(new Font("Tahoma", Font.BOLD, 13));
				labelReport.setBounds(76, 8, 232, 25);
				mid2.add(labelReport);
				
				labelTime = new JLabel("ช่วงเวลา");
				labelTime.setFont(new Font("Tahoma", Font.PLAIN, 13));
				labelTime.setBounds(76, 28, 650, 25);
				mid2.add(labelTime);
				
		 
				
				//
				columnNamesReport = new Vector<String>();
		  		columnNamesReport.add(" กิจกรรม");
		  		columnNamesReport.add(" ทันตกรรมในเวลา");
				columnNamesReport.add(" ทันตกรรมนอกเวลา ");
				columnNamesReport.add(" ทันตกรรม ANC ");
				columnNamesReport.add(" ทันตกรรมสุขภาพเด็กดี ");
				columnNamesReport.add(" ทันตกรรมเคลื่อนที่ ");
				columnNamesReport.add(" ทันตกรรม อต.1 ");
				columnNamesReport.add(" ทันตกรรม อต.2 ");
				columnNamesReport.add("    รวม ");
				dataReport = new Vector<Vector<String>>();
				
				DefaultTableModel modelR = new DefaultTableModel(dataReport, columnNamesReport){
					public Class getColumnClass(int column){
						return getValueAt(0, column).getClass();
					}
				};
				tableR = new JTable(modelR){
					public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
						Component c = super.prepareRenderer(renderer, row, column);			
						if (!isRowSelected(row)){
							c.setBackground(getBackground());
							int modelRow = convertRowIndexToModel(row);
							 
							String type = (String)getModel().getValueAt(modelRow, 0);
							 
							String status=type.trim().substring(0, 1);	
							//System.out.println(status+"--"+type);
							if ("[".equals(status)){
								c.setBackground(new Color(206,203,208));					 
				 			}
						}
						return c;
					}
				};
				tableR.setRowHeight(25);		
				tableR.setFillsViewportHeight(true);
		 
				scrollPaneR = new JScrollPane(tableR);
				scrollPaneR.setBounds(5, 50, screen.width-50, screen.height-300);
				mid2.add(scrollPaneR);
	}

	public void init(){
		int p=0,p1=0;
		PreparedStatement stmt31,stmt32;
		ResultSet rs31,rs32,rs33,rs34;
		String query="select code,name,report1,report2,categoryname,subcategoryname from dentalcode ";
		String q_doctor="select provider,name,lname from provider where providertype in ('02','06')";
		try {
			Connection conn = new DBmanager().getConnMySql();
			stmt31 = conn.prepareStatement(query);
			rs31 = stmt31.executeQuery();
			while (rs31.next()) {
				p++;
			}
			rs31.close();
			txname = new String [p][2];
			reportM = new String [p][2];
			reportS = new String [p][2];
			categoryname = new String [p][2];
			subcategoryname = new String [p][2];
			rs32 = stmt31.executeQuery();
			int pp=0;
			while (rs32.next()) {
				String name="";
				if(rs32.getString(2)!=null){
					name=rs32.getString(2);
				}
				txname[pp][0]=rs32.getString(1);
				txname[pp][1]=name;
				String namereportM="";
				if(rs32.getString(3)!=null){
					 if(rs32.getString(3).trim().equals("") || rs32.getString(3).trim().equals("9")){
						 
					 }else{
						 //namereportM=rs32.getString(3).trim().substring(0,2);
						 namereportM=rs32.getString(3).trim();
					 }
				} 
				reportM[pp][0]=rs32.getString(1);
				reportM[pp][1]=namereportM;
				String namereportS="";
				if(rs32.getString(4)!=null){
					 if(rs32.getString(4).trim().equals("") || rs32.getString(4).trim().equals("9")){
						 
					 }else{
						 //namereportM=rs32.getString(3).trim().substring(0,2);
						 namereportS=rs32.getString(4).trim();
					 }
				} 
				reportS[pp][0]=rs32.getString(1);
				reportS[pp][1]=namereportS;
				String cat_name="";
				if(rs32.getString(5)!=null){
					cat_name=rs32.getString(5);
				}
				categoryname[pp][0]=rs32.getString(1);
				categoryname[pp][1]=cat_name;
				String subcat_name="";
				if(rs32.getString(6)!=null){
					subcat_name=rs32.getString(6);
				}
				subcategoryname[pp][0]=rs32.getString(1);
				subcategoryname[pp][1]=subcat_name;
				pp++;
			}
			rs32.close();
			stmt31.close();
			
			stmt32 = conn.prepareStatement(q_doctor);
			rs33 = stmt32.executeQuery();
			while (rs33.next()) {
				p1++;
			}
			rs33.close();
			doctorname = new String [p1][2];
			rs34 = stmt32.executeQuery();
			int pp1=0;
			cb1.addItem(new ComboItem("ทั้งหมด","000"));
			while (rs34.next()) {
				
				//System.out.println(pp1+"."+rs34.getString(1).trim()+" "+rs34.getString(2).trim()+" "+rs34.getString(3).trim());
				cb1.addItem(new ComboItem(rs34.getString(2).trim()+" "+rs34.getString(3).trim(),rs34.getString(1).trim()));
				pp1++;
			}
			stmt32.close();
			conn.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<txname.length;i++){
			
			//System.out.println(txname[i][0]+"::"+txname[i][1]);
		}
		for(int i=0;i<reportM.length;i++){
			
			//System.out.println(reportM[i][0]+"::"+reportM[i][1]);
		}
		 
	}
	public void getDataReport(){
		tableR.setModel(fetchDataOPDReport());
		TableColumnModel columnModelR = tableR.getColumnModel();
		columnModelR.getColumn(0).setPreferredWidth(150);
		columnModelR.getColumn(1).setPreferredWidth(80 );
		columnModelR.getColumn(2).setPreferredWidth(80);
		columnModelR.getColumn(3).setPreferredWidth(80 );
		columnModelR.getColumn(4).setPreferredWidth(80 );
		columnModelR.getColumn(5).setPreferredWidth(80 );
		columnModelR.getColumn(6).setPreferredWidth(80 );
		columnModelR.getColumn(7).setPreferredWidth(80 );
		columnModelR.getColumn(8).setPreferredWidth(80 );
		
		((DefaultTableModel)tableR.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataOPDReport(){
		
		String doctor_search="";
		if(((ComboItem)cb1.getSelectedItem()).getValue().equals("000")){
			
		}else{
			doctor_search=" and dentaltreat.doctor='"+((ComboItem)cb1.getSelectedItem()).getValue().trim()+"' ";
		}
		//System.out.println(doctor_search);
		labelReport.setText("รายงานทันตกรรม ::  "+((ComboItem)cb1.getSelectedItem()).getKey().trim());
		int [] m1={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m2={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m3={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m4={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m5={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m6={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m7={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m8={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		String [] r1={"1.งานทันตวินิจฉัย","2.งานทันตรังสี","3.งานเวชศาสตร์ช่องปาก","4.งานทันตกรรมหัตถการ","5.งานรักษาคลองรากฟัน","6.งานปริทันต์","7.งานทันตกรรมป้องกัน","8.งานทันตกรรมเด็ก","9.งานทันตกรรมประดิษฐ์","10.งานทันตศัลยกรรม","11.งานศัลยกรรมช่องปาก","12.งานทันตกรรมจัดฟัน","13.งานทันตกรรมบดเคี้ยว","14.งานทันตกรรมเบ็ดเตล็ด"};
		String [] r01={"1.1 ตรวจ1","1.2 ตรวจ2","1.3 ตรวจผู้ป่วยเบาหวาน"};
		String [] r02={"2.1 งานทันตรังสี1","2.2 งานทันตรังสี2"};
		String [] r03={"3.1 เวชศาสตร์ช่องปาก1","3.2 เวชศาสตร์ช่องปาก2","3.3 เวชศาสตร์ช่องปาก3","3.4 เวชศาสตร์ช่องปาก4","3.5 เวชศาสตร์ช่องปาก5","3.6 เวชศาสตร์ช่องปาก6"};
		String [] r04={"4.1 ทันตกรรมหัตถการ1","4.2 ทันตกรรมหัตถการ2","4.3 ทันตกรรมหัตถการ3","4.4 ทันตกรรมหัตถการ4"};
		String [] r05={"5.1 รักษาคลองรากฟัน1","5.2 รักษาคลองรากฟัน2","5.3 รักษาคลองรากฟัน3","5.4 รักษาคลองรากฟัน4","5.5 รักษาคลองรากฟัน5","5.6 รักษาคลองรากฟัน6","5.7 รักษาคลองรากฟัน7","5.8 รักษาคลองรากฟัน8"};
		String [] r06={"6.1 ปริทันต์ 1","6.2 ปริทันต์ 2","6.3 ปริทันต์ 3","6.4 ปริทันต์ 4","6.5 ปริทันต์ 5","6.6 ปริทันต์ 6","6.7 ปริทันต์ 7","6.8 ปริทันต์ 8"};
		String [] r07={"7.1 ทันตกรรมป้องกัน1","7.2 ทันตกรรมป้องกัน2","7.3 ทันตกรรมป้องกัน3","7.4 ทันตกรรมป้องกัน4"};
		String [] r08={"8.1 ทันตกรรมเด็ก1","8.2 ทันตกรรมเด็ก2","8.3 ทันตกรรมเด็ก3","8.4 ทันตกรรมเด็ก4","8.5 ทันตกรรมเด็ก5","8.6 ทันตกรรมเด็ก6","8.7 ทันตกรรมเด็ก7"};
		String [] r09={"9.1 ทันตกรรมประดิษฐ์1","9.2 ทันตกรรมประดิษฐ์2","9.3 ทันตกรรมประดิษฐ์3","9.4 ทันตกรรมประดิษฐ์4","9.5 ทันตกรรมประดิษฐ์5","9.6 ทันตกรรมประดิษฐ์6","9.7 ทันตกรรมประดิษฐ์7","9.8 ทันตกรรมประดิษฐ์8","9.9 ทันตกรรมประดิษฐ์9"};
		String [] r10={"10.1 ทันตศัลยกรรม1","10.2 ทันตศัลยกรรม2"};
		String [] r11={"11.1 ศัลยกรรมช่องปาก1","11.2 ศัลยกรรมช่องปาก2","11.3 ศัลยกรรมช่องปาก3","11.4 ศัลยกรรมช่องปาก4","11.5 ศัลยกรรมช่องปาก5","11.6 ศัลยกรรมช่องปาก6","11.7 ศัลยกรรมช่องปาก7"};
		String [] r12={"12.1 ทันตกรรมจัดฟัน1","12.2 ทันตกรรมจัดฟัน2","12.3 ทันตกรรมจัดฟัน3","12.4 ทันตกรรมจัดฟัน4","12.5 ทันตกรรมจัดฟัน5","12.6 ทันตกรรมจัดฟัน6","12.7 ทันตกรรมจัดฟัน7","12.8 ทันตกรรมจัดฟัน8","12.9 ทันตกรรมจัดฟัน9"};
		String [] r13={"13.1 ทันตกรรมบดเคี้ยว1","13.2 ทันตกรรมบดเคี้ยว2","13.3 ทันตกรรมบดเคี้ยว3","13.4 ทันตกรรมบดเคี้ยว4","13.5 ทันตกรรมบดเคี้ยว5","13.6 ทันตกรรมบดเคี้ยว6"};
		String [] r14={"14.1 ทันตกรรมเบ็ดเตล็ด"};
		
		String [] r042={"อุดฟัน Class III Amalgam","อุดฟัน Class V Amalgam","อุดฟัน Class I Toothlike material","อุดฟัน Class III Toothlike material","อุดฟัน Class V Toothlike material"
				,"อุดฟันชั่วคราว","อุดฟัน Class I Compound Aamalgam","อุดฟัน Class II Aamalgam","อุดฟัน Class IV Toothlike","อุดฟัน Class I Compound Toothlike","อุดฟัน Class II Toothlike","อุดฟัน  2 ด้าน"};
		int [] m101={0,0,0};
		int [] m102={0,0};
		int [] m103={0,0,0,0,0,0};
		int [] m104={0,0,0,0};
		int [] m105={0,0,0,0,0,0,0,0};
		int [] m106={0,0,0,0,0,0,0,0};
		int [] m107={0,0,0,0};
		int [] m108={0,0,0,0,0,0,0};
		int [] m109={0,0,0,0,0,0,0,0,0};
		int [] m110={0,0};
		int [] m111={0,0,0,0,0,0,0};
		int [] m112={0,0,0,0,0,0,0,0,0};
		int [] m113={0,0,0,0,0,0};
		int [] m114={0};
		
		int [] m1042={0,0,0,0,0,0,0,0,0,0,0,0};
		
		int [] m201={0,0,0};
		int [] m202={0,0};
		int [] m203={0,0,0,0,0,0};
		int [] m204={0,0,0,0};
		int [] m205={0,0,0,0,0,0,0,0};
		int [] m206={0,0,0,0,0,0,0,0};
		int [] m207={0,0,0,0};
		int [] m208={0,0,0,0,0,0,0};
		int [] m209={0,0,0,0,0,0,0,0,0};
		int [] m210={0,0};
		int [] m211={0,0,0,0,0,0,0};
		int [] m212={0,0,0,0,0,0,0,0,0};
		int [] m213={0,0,0,0,0,0};
		int [] m214={0};
		
		int [] m301={0,0,0};
		int [] m302={0,0};
		int [] m303={0,0,0,0,0,0};
		int [] m304={0,0,0,0};
		int [] m305={0,0,0,0,0,0,0,0};
		int [] m306={0,0,0,0,0,0,0,0};
		int [] m307={0,0,0,0};
		int [] m308={0,0,0,0,0,0,0};
		int [] m309={0,0,0,0,0,0,0,0,0};
		int [] m310={0,0};
		int [] m311={0,0,0,0,0,0,0};
		int [] m312={0,0,0,0,0,0,0,0,0};
		int [] m313={0,0,0,0,0,0};
		int [] m314={0};
		
		int [] m401={0,0,0};
		int [] m402={0,0};
		int [] m403={0,0,0,0,0,0};
		int [] m404={0,0,0,0};
		int [] m405={0,0,0,0,0,0,0,0};
		int [] m406={0,0,0,0,0,0,0,0};
		int [] m407={0,0,0,0};
		int [] m408={0,0,0,0,0,0,0};
		int [] m409={0,0,0,0,0,0,0,0,0};
		int [] m410={0,0};
		int [] m411={0,0,0,0,0,0,0};
		int [] m412={0,0,0,0,0,0,0,0,0};
		int [] m413={0,0,0,0,0,0};
		int [] m414={0};
		
		int [] m501={0,0,0};
		int [] m502={0,0};
		int [] m503={0,0,0,0,0,0};
		int [] m504={0,0,0,0};
		int [] m505={0,0,0,0,0,0,0,0};
		int [] m506={0,0,0,0,0,0,0,0};
		int [] m507={0,0,0,0};
		int [] m508={0,0,0,0,0,0,0};
		int [] m509={0,0,0,0,0,0,0,0,0};
		int [] m510={0,0};
		int [] m511={0,0,0,0,0,0,0};
		int [] m512={0,0,0,0,0,0,0,0,0};
		int [] m513={0,0,0,0,0,0};
		int [] m514={0};
		
		int [] m601={0,0,0};
		int [] m602={0,0};
		int [] m603={0,0,0,0,0,0};
		int [] m604={0,0,0,0};
		int [] m605={0,0,0,0,0,0,0,0};
		int [] m606={0,0,0,0,0,0,0,0};
		int [] m607={0,0,0,0};
		int [] m608={0,0,0,0,0,0,0};
		int [] m609={0,0,0,0,0,0,0,0,0};
		int [] m610={0,0};
		int [] m611={0,0,0,0,0,0,0};
		int [] m612={0,0,0,0,0,0,0,0,0};
		int [] m613={0,0,0,0,0,0};
		int [] m614={0};
		
		int [] m701={0,0,0};
		int [] m702={0,0};
		int [] m703={0,0,0,0,0,0};
		int [] m704={0,0,0,0};
		int [] m705={0,0,0,0,0,0,0,0};
		int [] m706={0,0,0,0,0,0,0,0};
		int [] m707={0,0,0,0};
		int [] m708={0,0,0,0,0,0,0};
		int [] m709={0,0,0,0,0,0,0,0,0};
		int [] m710={0,0};
		int [] m711={0,0,0,0,0,0,0};
		int [] m712={0,0,0,0,0,0,0,0,0};
		int [] m713={0,0,0,0,0,0};
		int [] m714={0};
		 	
		int [] m801={0,0,0};
		int [] m802={0,0};
		int [] m803={0,0,0,0,0,0};
		int [] m804={0,0,0,0};
		int [] m805={0,0,0,0,0,0,0,0};
		int [] m806={0,0,0,0,0,0,0,0};
		int [] m807={0,0,0,0};
		int [] m808={0,0,0,0,0,0,0};
		int [] m809={0,0,0,0,0,0,0,0,0};
		int [] m810={0,0};
		int [] m811={0,0,0,0,0,0,0};
		int [] m812={0,0,0,0,0,0,0,0,0};
		int [] m813={0,0,0,0,0,0};
		int [] m814={0};
		Vector<Vector<String>> dataReport = new Vector<Vector<String>>();
		String b=Setup.DateInDBMSSQL(dateSearch1.trim());
		String f=Setup.DateInDBMSSQL(dateSearch2.trim());
		 
		labelTime.setText("ช่วงเวลา   วันที่   "+Setup.ShowThaiDate1(b)+" ถึง  วันที่  "+Setup.ShowThaiDate1(f) +" :: งานทันตกรรมเคลื่อนที่นับรวม ");
		//System.out.println(dateSearch1.trim()+" and"+dateSearch2.trim());
		String sql="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and ((vnpres.suffix='1' and vnpres.clinic='1100') or (vnpres.suffix='2' and vnpres.clinic='1100') or (vnpres.suffix='3' and vnpres.clinic='1100')) and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
		try {
			Connection conn =  new DBmanager().getConnMSSql();
		
			Connection conn1 =  new DBmanager().getConnMySql();
			//1100
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			int p=1;
			while (rs.next()){
				String report1="",report2="",report3="",report4="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report1=reportM[i][1].trim();
					}	 
				}
				for(int i=0;i<reportS.length;i++){
					if(rs.getString(1).trim().toUpperCase() .equals(reportS[i][0].trim().toUpperCase() )){
						report4=reportS[i][1].trim();
					}	 
				}
				if(report1.length()==4){
					 
					report2=report1.substring(0, 2);
					report3=report1.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report2.equals("01")){
						m1[0]++;
						if(report3.equals("01")){
							m101[0]++;
						}
						else if(report3.equals("02")){
							m101[1]++;
						}
						else if(report3.equals("03")){
							m101[2]++;
						}
					}
					else if(report2.equals("02")){
						m1[1]++;
						if(report3.equals("01")){
							m102[0]++;
						}
						else if(report3.equals("02")){
							m102[1]++;
						}
					}
					else if(report2.equals("03")){
						m1[2]++;
						if(report3.equals("01")){
							m103[0]++;
						}
						else if(report3.equals("02")){
							m103[1]++;
						}
						else if(report3.equals("03")){
							m103[2]++;
						}
						else if(report3.equals("04")){
							m103[3]++;
						}
						else if(report3.equals("05")){
							m103[4]++;
						}
						else if(report3.equals("06")){
							m103[5]++;
						}
					}
					else if(report2.equals("04")){
						m1[3]++;
						if(report3.equals("01")){
							m104[0]++;
						}
						else if(report3.equals("02")){
							m104[1]++;
						}
						else if(report3.equals("03")){
							m104[2]++;
						}
						else if(report3.equals("04")){
							m104[3]++;
						}
						///
						if(report4.equals("01")){
							m1042[0]++;
						}
						else if(report4.equals("02")){
							m1042[1]++;
						}
						else if(report4.equals("03")){
							m1042[2]++;
						}
						else if(report4.equals("04")){
							m1042[3]++;
						}
						else if(report4.equals("05")){
							m1042[4]++;
						}
						else if(report4.equals("06")){
							m1042[5]++;
						}
						else if(report4.equals("07")){
							m1042[6]++;
						}
						else if(report4.equals("08")){
							m1042[7]++;
						}
						else if(report4.equals("09")){
							m1042[8]++;
						}
						else if(report4.equals("10")){
							m1042[9]++;
						}
						else if(report4.equals("11")){
							m1042[10]++;
						}
						else if(report4.equals("12")){
							m1042[11]++;
						}
						for(int t=0;t<m1042.length;t++){
							//System.out.println(t+". WW "+m1042[t]);
						}
					
					}
					else if(report2.equals("05")){
						m1[4]++;
						if(report3.equals("01")){
							m105[0]++;
						}
						else if(report3.equals("02")){
							m105[1]++;
						}
						else if(report3.equals("03")){
							m105[2]++;
						}
						else if(report3.equals("04")){
							m105[3]++;
						}
						else if(report3.equals("05")){
							m105[4]++;
						}
						else if(report3.equals("06")){
							m105[5]++;
						}
						else if(report3.equals("07")){
							m105[6]++;
						}
						else if(report3.equals("08")){
							m105[7]++;
						}
					}
					else if(report2.equals("06")){
						m1[5]++;
						if(report3.equals("01")){
							m106[0]++;
						}
						else if(report3.equals("02")){
							m106[1]++;
						}
						else if(report3.equals("03")){
							m106[2]++;
						}
						else if(report3.equals("04")){
							m106[3]++;
						}
						else if(report3.equals("05")){
							m106[4]++;
						}
						else if(report3.equals("06")){
							m106[5]++;
						}
						else if(report3.equals("07")){
							m106[6]++;
						}
						else if(report3.equals("08")){
							m106[7]++;
						}
					}
					else if(report2.equals("07")){
						m1[6]++;
						if(report3.equals("01")){
							m107[0]++;
						}
						else if(report3.equals("02")){
							m107[1]++;
						}
						else if(report3.equals("03")){
							m107[2]++;
						}
						else if(report3.equals("04")){
							m107[3]++;
						}
						 
					}
					else if(report2.equals("08")){
						m1[7]++;
						if(report3.equals("01")){
							m108[0]++;
						}
						else if(report3.equals("02")){
							m108[1]++;
						}
						else if(report3.equals("03")){
							m108[2]++;
						}
						else if(report3.equals("04")){
							m108[3]++;
						}
						else if(report3.equals("05")){
							m108[4]++;
						}
						else if(report3.equals("06")){
							m108[5]++;
						}
						else if(report3.equals("07")){
							m108[6]++;
						}
						 
					}
					else if(report2.equals("09")){
						m1[8]++;
						if(report3.equals("01")){
							m109[0]++;
						}
						else if(report3.equals("02")){
							m109[1]++;
						}
						else if(report3.equals("03")){
							m109[2]++;
						}
						else if(report3.equals("04")){
							m109[3]++;
						}
						else if(report3.equals("05")){
							m109[4]++;
						}
						else if(report3.equals("06")){
							m109[5]++;
						}
						else if(report3.equals("07")){
							m109[6]++;
						}
						else if(report3.equals("08")){
							m109[7]++;
						}
						else if(report3.equals("09")){
							m109[8]++;
						}
					}
					else if(report2.equals("10")){
						m1[9]++;
						if(report3.equals("01")){
							m110[0]++;
						}
						else if(report3.equals("02")){
							m110[1]++;
						}
					}
					else if(report2.equals("11")){
						m1[10]++;
						if(report3.equals("01")){
							m111[0]++;
						}
						else if(report3.equals("02")){
							m111[1]++;
						}
						else if(report3.equals("03")){
							m111[2]++;
						}
						else if(report3.equals("04")){
							m111[3]++;
						}
						else if(report3.equals("05")){
							m111[4]++;
						}
						else if(report3.equals("06")){
							m111[5]++;
						}
						else if(report3.equals("07")){
							m111[6]++;
						}
					}
					else if(report2.equals("12")){
						m1[11]++;
						if(report3.equals("01")){
							m112[0]++;
						}
						else if(report3.equals("02")){
							m112[1]++;
						}
						else if(report3.equals("03")){
							m112[2]++;
						}
						else if(report3.equals("04")){
							m112[3]++;
						}
						else if(report3.equals("05")){
							m112[4]++;
						}
						else if(report3.equals("06")){
							m112[5]++;
						}
						else if(report3.equals("07")){
							m112[6]++;
						}
						else if(report3.equals("08")){
							m112[7]++;
						}
						else if(report3.equals("09")){
							m112[8]++;
						}
					}
					else if(report2.equals("13")){
						m1[12]++;
						if(report3.equals("01")){
							m113[0]++;
						}
						else if(report3.equals("02")){
							m113[1]++;
						}
						else if(report3.equals("03")){
							m113[2]++;
						}
						else if(report3.equals("04")){
							m113[3]++;
						}
						else if(report3.equals("05")){
							m113[4]++;
						}
						else if(report3.equals("06")){
							m113[5]++;
						}
						 
					}
					else if(report2.equals("14")){
						m1[13]++;
						if(report3.equals("01")){
							m114[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt.close();
			//end 1100
			
			//1101
			String sql1101="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and ((vnpres.suffix='1' and vnpres.clinic='1101') or (vnpres.suffix='2' and vnpres.clinic='1101') or (vnpres.suffix='3' and vnpres.clinic='1101'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt1 = conn.prepareStatement(sql1101);
			ResultSet rs1 = stmt1.executeQuery();
			int p1=1;
			while (rs1.next()){
				String report12="",report22="",report32="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs1.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report12=reportM[i][1].trim();
					}	 
				}
				if(report12.length()==4){
					 
					report22=report12.substring(0, 2);
					report32=report12.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report22.equals("01")){
						m2[0]++;
						if(report32.equals("01")){
							m201[0]++;
						}
						else if(report32.equals("02")){
							m201[1]++;
						}
						else if(report32.equals("03")){
							m201[2]++;
						}
					}
					else if(report22.equals("02")){
						m2[1]++;
						if(report32.equals("01")){
							m202[0]++;
						}
						else if(report32.equals("02")){
							m202[1]++;
						}
					}
					else if(report22.equals("03")){
						m2[2]++;
						if(report32.equals("01")){
							m203[0]++;
						}
						else if(report32.equals("02")){
							m203[1]++;
						}
						else if(report32.equals("03")){
							m203[2]++;
						}
						else if(report32.equals("04")){
							m203[3]++;
						}
						else if(report32.equals("05")){
							m203[4]++;
						}
						else if(report32.equals("06")){
							m203[5]++;
						}
					}
					else if(report22.equals("04")){
						m2[3]++;
						if(report32.equals("01")){
							m204[0]++;
						}
						else if(report32.equals("02")){
							m204[1]++;
						}
						else if(report32.equals("03")){
							m204[2]++;
						}
						else if(report32.equals("04")){
							m204[3]++;
						}
					}
					else if(report22.equals("05")){
						m2[4]++;
						if(report32.equals("01")){
							m205[0]++;
						}
						else if(report32.equals("02")){
							m205[1]++;
						}
						else if(report32.equals("03")){
							m205[2]++;
						}
						else if(report32.equals("04")){
							m205[3]++;
						}
						else if(report32.equals("05")){
							m205[4]++;
						}
						else if(report32.equals("06")){
							m205[5]++;
						}
						else if(report32.equals("07")){
							m205[6]++;
						}
						else if(report32.equals("08")){
							m205[7]++;
						}
					}
					else if(report22.equals("06")){
						m2[5]++;
						if(report32.equals("01")){
							m206[0]++;
						}
						else if(report32.equals("02")){
							m206[1]++;
						}
						else if(report32.equals("03")){
							m206[2]++;
						}
						else if(report32.equals("04")){
							m206[3]++;
						}
						else if(report32.equals("05")){
							m206[4]++;
						}
						else if(report32.equals("06")){
							m206[5]++;
						}
						else if(report32.equals("07")){
							m206[6]++;
						}
						else if(report32.equals("08")){
							m206[7]++;
						}
					}
					else if(report22.equals("07")){
						m2[6]++;
						if(report32.equals("01")){
							m207[0]++;
						}
						else if(report32.equals("02")){
							m207[1]++;
						}
						else if(report32.equals("03")){
							m207[2]++;
						}
						else if(report32.equals("04")){
							m207[3]++;
						}
						 
					}
					else if(report22.equals("08")){
						m2[7]++;
						if(report32.equals("01")){
							m208[0]++;
						}
						else if(report32.equals("02")){
							m208[1]++;
						}
						else if(report32.equals("03")){
							m208[2]++;
						}
						else if(report32.equals("04")){
							m208[3]++;
						}
						else if(report32.equals("05")){
							m208[4]++;
						}
						else if(report32.equals("06")){
							m208[5]++;
						}
						else if(report32.equals("07")){
							m208[6]++;
						}
						 
					}
					else if(report22.equals("09")){
						m2[8]++;
						if(report32.equals("01")){
							m209[0]++;
						}
						else if(report32.equals("02")){
							m209[1]++;
						}
						else if(report32.equals("03")){
							m209[2]++;
						}
						else if(report32.equals("04")){
							m209[3]++;
						}
						else if(report32.equals("05")){
							m209[4]++;
						}
						else if(report32.equals("06")){
							m209[5]++;
						}
						else if(report32.equals("07")){
							m209[6]++;
						}
						else if(report32.equals("08")){
							m209[7]++;
						}
						else if(report32.equals("09")){
							m209[8]++;
						}
					}
					else if(report22.equals("10")){
						m2[9]++;
						if(report32.equals("01")){
							m210[0]++;
						}
						else if(report32.equals("02")){
							m210[1]++;
						}
					}
					else if(report22.equals("11")){
						m2[10]++;
						if(report32.equals("01")){
							m211[0]++;
						}
						else if(report32.equals("02")){
							m211[1]++;
						}
						else if(report32.equals("03")){
							m211[2]++;
						}
						else if(report32.equals("04")){
							m211[3]++;
						}
						else if(report32.equals("05")){
							m211[4]++;
						}
						else if(report32.equals("06")){
							m211[5]++;
						}
						else if(report32.equals("07")){
							m211[6]++;
						}
					}
					else if(report22.equals("12")){
						m2[11]++;
						if(report32.equals("01")){
							m212[0]++;
						}
						else if(report32.equals("02")){
							m212[1]++;
						}
						else if(report32.equals("03")){
							m212[2]++;
						}
						else if(report32.equals("04")){
							m212[3]++;
						}
						else if(report32.equals("05")){
							m212[4]++;
						}
						else if(report32.equals("06")){
							m212[5]++;
						}
						else if(report32.equals("07")){
							m212[6]++;
						}
						else if(report32.equals("08")){
							m212[7]++;
						}
						else if(report32.equals("09")){
							m212[8]++;
						}
					}
					else if(report22.equals("13")){
						m2[12]++;
						if(report32.equals("01")){
							m213[0]++;
						}
						else if(report32.equals("02")){
							m213[1]++;
						}
						else if(report32.equals("03")){
							m213[2]++;
						}
						else if(report32.equals("04")){
							m213[3]++;
						}
						else if(report32.equals("05")){
							m213[4]++;
						}
						else if(report32.equals("06")){
							m213[5]++;
						}
						 
					}
					else if(report22.equals("14")){
						m2[13]++;
						if(report32.equals("01")){
							m214[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt1.close();
			//end 1101
			
			//1102
			
			String sql1102="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and ((vnpres.suffix='1' and vnpres.clinic='1102') or (vnpres.suffix='2' and vnpres.clinic='1102') or (vnpres.suffix='3' and vnpres.clinic='1102'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt2 = conn.prepareStatement(sql1102);
			ResultSet rs2 = stmt2.executeQuery();
			int p2=1;
			while (rs2.next()){
				String report13="",report23="",report33="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs2.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report13=reportM[i][1].trim();
					}	 
				}
				if(report13.length()==4){
					 
					report23=report13.substring(0, 2);
					report33=report13.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report23.equals("01")){
						m3[0]++;
						if(report33.equals("01")){
							m301[0]++;
						}
						else if(report33.equals("02")){
							m301[1]++;
						}
						else if(report33.equals("03")){
							m301[2]++;
						}
					}
					else if(report23.equals("02")){
						m3[1]++;
						if(report33.equals("01")){
							m302[0]++;
						}
						else if(report33.equals("02")){
							m302[1]++;
						}
					}
					else if(report23.equals("03")){
						m2[2]++;
						if(report33.equals("01")){
							m303[0]++;
						}
						else if(report33.equals("02")){
							m303[1]++;
						}
						else if(report33.equals("03")){
							m303[2]++;
						}
						else if(report33.equals("04")){
							m303[3]++;
						}
						else if(report33.equals("05")){
							m303[4]++;
						}
						else if(report33.equals("06")){
							m303[5]++;
						}
					}
					else if(report23.equals("04")){
						m3[3]++;
						if(report33.equals("01")){
							m304[0]++;
						}
						else if(report33.equals("02")){
							m304[1]++;
						}
						else if(report33.equals("03")){
							m304[2]++;
						}
						else if(report33.equals("04")){
							m304[3]++;
						}
					}
					else if(report23.equals("05")){
						m3[4]++;
						if(report33.equals("01")){
							m305[0]++;
						}
						else if(report33.equals("02")){
							m305[1]++;
						}
						else if(report33.equals("03")){
							m305[2]++;
						}
						else if(report33.equals("04")){
							m305[3]++;
						}
						else if(report33.equals("05")){
							m305[4]++;
						}
						else if(report33.equals("06")){
							m305[5]++;
						}
						else if(report33.equals("07")){
							m305[6]++;
						}
						else if(report33.equals("08")){
							m305[7]++;
						}
					}
					else if(report23.equals("06")){
						m3[5]++;
						if(report33.equals("01")){
							m306[0]++;
						}
						else if(report33.equals("02")){
							m306[1]++;
						}
						else if(report33.equals("03")){
							m306[2]++;
						}
						else if(report33.equals("04")){
							m306[3]++;
						}
						else if(report33.equals("05")){
							m306[4]++;
						}
						else if(report33.equals("06")){
							m306[5]++;
						}
						else if(report33.equals("07")){
							m306[6]++;
						}
						else if(report33.equals("08")){
							m306[7]++;
						}
					}
					else if(report23.equals("07")){
						m3[6]++;
						if(report33.equals("01")){
							m307[0]++;
						}
						else if(report33.equals("02")){
							m307[1]++;
						}
						else if(report33.equals("03")){
							m307[2]++;
						}
						else if(report33.equals("04")){
							m307[3]++;
						}
						 
					}
					else if(report23.equals("08")){
						m3[7]++;
						if(report33.equals("01")){
							m308[0]++;
						}
						else if(report33.equals("02")){
							m308[1]++;
						}
						else if(report33.equals("03")){
							m308[2]++;
						}
						else if(report33.equals("04")){
							m308[3]++;
						}
						else if(report33.equals("05")){
							m308[4]++;
						}
						else if(report33.equals("06")){
							m308[5]++;
						}
						else if(report33.equals("07")){
							m308[6]++;
						}
						 
					}
					else if(report23.equals("09")){
						m3[8]++;
						if(report33.equals("01")){
							m309[0]++;
						}
						else if(report33.equals("02")){
							m309[1]++;
						}
						else if(report33.equals("03")){
							m309[2]++;
						}
						else if(report33.equals("04")){
							m309[3]++;
						}
						else if(report33.equals("05")){
							m309[4]++;
						}
						else if(report33.equals("06")){
							m309[5]++;
						}
						else if(report33.equals("07")){
							m309[6]++;
						}
						else if(report33.equals("08")){
							m309[7]++;
						}
						else if(report33.equals("09")){
							m309[8]++;
						}
					}
					else if(report23.equals("10")){
						m3[9]++;
						if(report33.equals("01")){
							m310[0]++;
						}
						else if(report33.equals("02")){
							m310[1]++;
						}
					}
					else if(report23.equals("11")){
						m3[10]++;
						if(report33.equals("01")){
							m311[0]++;
						}
						else if(report33.equals("02")){
							m311[1]++;
						}
						else if(report33.equals("03")){
							m311[2]++;
						}
						else if(report33.equals("04")){
							m311[3]++;
						}
						else if(report33.equals("05")){
							m311[4]++;
						}
						else if(report33.equals("06")){
							m311[5]++;
						}
						else if(report33.equals("07")){
							m311[6]++;
						}
					}
					else if(report23.equals("12")){
						m3[11]++;
						if(report33.equals("01")){
							m312[0]++;
						}
						else if(report33.equals("02")){
							m312[1]++;
						}
						else if(report33.equals("03")){
							m312[2]++;
						}
						else if(report33.equals("04")){
							m312[3]++;
						}
						else if(report33.equals("05")){
							m312[4]++;
						}
						else if(report33.equals("06")){
							m312[5]++;
						}
						else if(report33.equals("07")){
							m312[6]++;
						}
						else if(report33.equals("08")){
							m312[7]++;
						}
						else if(report33.equals("09")){
							m312[8]++;
						}
					}
					else if(report23.equals("13")){
						m3[12]++;
						if(report33.equals("01")){
							m313[0]++;
						}
						else if(report33.equals("02")){
							m313[1]++;
						}
						else if(report33.equals("03")){
							m313[2]++;
						}
						else if(report33.equals("04")){
							m313[3]++;
						}
						else if(report33.equals("05")){
							m313[4]++;
						}
						else if(report33.equals("06")){
							m313[5]++;
						}
						 
					}
					else if(report23.equals("14")){
						m3[13]++;
						if(report33.equals("01")){
							m314[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt2.close();
		//end 1102
	//1103
			
			String sql1103="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and  ((vnpres.suffix='1' and vnpres.clinic='1103') or (vnpres.suffix='2' and vnpres.clinic='1103') or (vnpres.suffix='3' and vnpres.clinic='1103'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt3 = conn.prepareStatement(sql1103);
			ResultSet rs3 = stmt3.executeQuery();
			int p3=1;
			while (rs3.next()){
				String report14="",report24="",report34="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs3.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report14=reportM[i][1].trim();
					}	 
				}
				if(report14.length()==4){
					 
					report24=report14.substring(0, 2);
					report34=report14.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report24.equals("01")){
						m4[0]++;
						if(report34.equals("01")){
							m401[0]++;
						}
						else if(report34.equals("02")){
							m401[1]++;
						}
						else if(report34.equals("03")){
							m401[2]++;
						}
					}
					else if(report24.equals("02")){
						m4[1]++;
						if(report34.equals("01")){
							m402[0]++;
						}
						else if(report34.equals("02")){
							m402[1]++;
						}
					}
					else if(report24.equals("03")){
						m4[2]++;
						if(report34.equals("01")){
							m403[0]++;
						}
						else if(report34.equals("02")){
							m403[1]++;
						}
						else if(report34.equals("03")){
							m403[2]++;
						}
						else if(report34.equals("04")){
							m403[3]++;
						}
						else if(report34.equals("05")){
							m403[4]++;
						}
						else if(report34.equals("06")){
							m403[5]++;
						}
					}
					else if(report24.equals("04")){
						m4[3]++;
						if(report34.equals("01")){
							m404[0]++;
						}
						else if(report34.equals("02")){
							m404[1]++;
						}
						else if(report34.equals("03")){
							m404[2]++;
						}
						else if(report34.equals("04")){
							m404[3]++;
						}
					}
					else if(report24.equals("05")){
						m4[4]++;
						if(report34.equals("01")){
							m405[0]++;
						}
						else if(report34.equals("02")){
							m405[1]++;
						}
						else if(report34.equals("03")){
							m405[2]++;
						}
						else if(report34.equals("04")){
							m405[3]++;
						}
						else if(report34.equals("05")){
							m405[4]++;
						}
						else if(report34.equals("06")){
							m405[5]++;
						}
						else if(report34.equals("07")){
							m405[6]++;
						}
						else if(report34.equals("08")){
							m405[7]++;
						}
					}
					else if(report24.equals("06")){
						m4[5]++;
						if(report34.equals("01")){
							m406[0]++;
						}
						else if(report34.equals("02")){
							m406[1]++;
						}
						else if(report34.equals("03")){
							m406[2]++;
						}
						else if(report34.equals("04")){
							m406[3]++;
						}
						else if(report34.equals("05")){
							m406[4]++;
						}
						else if(report34.equals("06")){
							m406[5]++;
						}
						else if(report34.equals("07")){
							m406[6]++;
						}
						else if(report34.equals("08")){
							m406[7]++;
						}
					}
					else if(report24.equals("07")){
						m4[6]++;
						if(report34.equals("01")){
							m407[0]++;
						}
						else if(report34.equals("02")){
							m407[1]++;
						}
						else if(report34.equals("03")){
							m407[2]++;
						}
						else if(report34.equals("04")){
							m407[3]++;
						}
						 
					}
					else if(report24.equals("08")){
						m4[7]++;
						if(report34.equals("01")){
							m408[0]++;
						}
						else if(report34.equals("02")){
							m408[1]++;
						}
						else if(report34.equals("03")){
							m408[2]++;
						}
						else if(report34.equals("04")){
							m408[3]++;
						}
						else if(report34.equals("05")){
							m408[4]++;
						}
						else if(report34.equals("06")){
							m408[5]++;
						}
						else if(report34.equals("07")){
							m408[6]++;
						}
						 
					}
					else if(report24.equals("09")){
						m4[8]++;
						if(report34.equals("01")){
							m409[0]++;
						}
						else if(report34.equals("02")){
							m409[1]++;
						}
						else if(report34.equals("03")){
							m409[2]++;
						}
						else if(report34.equals("04")){
							m409[3]++;
						}
						else if(report34.equals("05")){
							m409[4]++;
						}
						else if(report34.equals("06")){
							m409[5]++;
						}
						else if(report34.equals("07")){
							m409[6]++;
						}
						else if(report34.equals("08")){
							m409[7]++;
						}
						else if(report34.equals("09")){
							m409[8]++;
						}
					}
					else if(report24.equals("10")){
						m4[9]++;
						if(report34.equals("01")){
							m410[0]++;
						}
						else if(report34.equals("02")){
							m410[1]++;
						}
					}
					else if(report24.equals("11")){
						m4[10]++;
						if(report34.equals("01")){
							m411[0]++;
						}
						else if(report34.equals("02")){
							m411[1]++;
						}
						else if(report34.equals("03")){
							m411[2]++;
						}
						else if(report34.equals("04")){
							m411[3]++;
						}
						else if(report34.equals("05")){
							m411[4]++;
						}
						else if(report34.equals("06")){
							m411[5]++;
						}
						else if(report34.equals("07")){
							m411[6]++;
						}
					}
					else if(report24.equals("12")){
						m4[11]++;
						if(report34.equals("01")){
							m412[0]++;
						}
						else if(report34.equals("02")){
							m412[1]++;
						}
						else if(report34.equals("03")){
							m412[2]++;
						}
						else if(report34.equals("04")){
							m412[3]++;
						}
						else if(report34.equals("05")){
							m412[4]++;
						}
						else if(report34.equals("06")){
							m412[5]++;
						}
						else if(report34.equals("07")){
							m412[6]++;
						}
						else if(report34.equals("08")){
							m412[7]++;
						}
						else if(report34.equals("09")){
							m412[8]++;
						}
					}
					else if(report24.equals("13")){
						m4[12]++;
						if(report34.equals("01")){
							m413[0]++;
						}
						else if(report34.equals("02")){
							m413[1]++;
						}
						else if(report34.equals("03")){
							m413[2]++;
						}
						else if(report34.equals("04")){
							m413[3]++;
						}
						else if(report34.equals("05")){
							m413[4]++;
						}
						else if(report34.equals("06")){
							m413[5]++;
						}
						 
					}
					else if(report24.equals("14")){
						m4[13]++;
						if(report34.equals("01")){
							m414[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt3.close();
		//end 1103
				//1104
			
			String sql1104="select treatment from dental_activity where visitdatetime between '"+b+"' and '"+f+"'  ";
			
			PreparedStatement stmt4 = conn1.prepareStatement(sql1104);
			ResultSet rs4 = stmt4.executeQuery();
		 
			while (rs4.next()){
				String report15="",report25="",report35="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs4.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report15=reportM[i][1].trim();
					}	 
				}
				if(report15.length()==4){
					 
					report25=report15.substring(0, 2);
					report35=report15.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report25.equals("01")){
						m5[0]++;
						if(report35.equals("01")){
							m501[0]++;
						}
						else if(report35.equals("02")){
							m501[1]++;
						}
						else if(report35.equals("03")){
							m501[2]++;
						}
					}
					else if(report25.equals("02")){
						m5[1]++;
						if(report35.equals("01")){
							m502[0]++;
						}
						else if(report35.equals("02")){
							m502[1]++;
						}
					}
					else if(report25.equals("03")){
						m5[2]++;
						if(report35.equals("01")){
							m503[0]++;
						}
						else if(report35.equals("02")){
							m503[1]++;
						}
						else if(report35.equals("03")){
							m503[2]++;
						}
						else if(report35.equals("04")){
							m503[3]++;
						}
						else if(report35.equals("05")){
							m503[4]++;
						}
						else if(report35.equals("06")){
							m503[5]++;
						}
					}
					else if(report25.equals("04")){
						m5[3]++;
						if(report35.equals("01")){
							m504[0]++;
						}
						else if(report35.equals("02")){
							m504[1]++;
						}
						else if(report35.equals("03")){
							m504[2]++;
						}
						else if(report35.equals("04")){
							m504[3]++;
						}
					}
					else if(report25.equals("05")){
						m5[4]++;
						if(report35.equals("01")){
							m505[0]++;
						}
						else if(report35.equals("02")){
							m505[1]++;
						}
						else if(report35.equals("03")){
							m505[2]++;
						}
						else if(report35.equals("04")){
							m505[3]++;
						}
						else if(report35.equals("05")){
							m505[4]++;
						}
						else if(report35.equals("06")){
							m505[5]++;
						}
						else if(report35.equals("07")){
							m505[6]++;
						}
						else if(report35.equals("08")){
							m505[7]++;
						}
					}
					else if(report25.equals("06")){
						m5[5]++;
						if(report35.equals("01")){
							m506[0]++;
						}
						else if(report35.equals("02")){
							m506[1]++;
						}
						else if(report35.equals("03")){
							m506[2]++;
						}
						else if(report35.equals("04")){
							m506[3]++;
						}
						else if(report35.equals("05")){
							m506[4]++;
						}
						else if(report35.equals("06")){
							m506[5]++;
						}
						else if(report35.equals("07")){
							m506[6]++;
						}
						else if(report35.equals("08")){
							m506[7]++;
						}
					}
					else if(report25.equals("07")){
						m5[6]++;
						if(report35.equals("01")){
							m507[0]++;
						}
						else if(report35.equals("02")){
							m507[1]++;
						}
						else if(report35.equals("03")){
							m507[2]++;
						}
						else if(report35.equals("04")){
							m507[3]++;
						}
						 
					}
					else if(report25.equals("08")){
						m5[7]++;
						if(report35.equals("01")){
							m508[0]++;
						}
						else if(report35.equals("02")){
							m508[1]++;
						}
						else if(report35.equals("03")){
							m508[2]++;
						}
						else if(report35.equals("04")){
							m508[3]++;
						}
						else if(report35.equals("05")){
							m508[4]++;
						}
						else if(report35.equals("06")){
							m508[5]++;
						}
						else if(report35.equals("07")){
							m508[6]++;
						}
						 
					}
					else if(report25.equals("09")){
						m5[8]++;
						if(report35.equals("01")){
							m509[0]++;
						}
						else if(report35.equals("02")){
							m509[1]++;
						}
						else if(report35.equals("03")){
							m509[2]++;
						}
						else if(report35.equals("04")){
							m509[3]++;
						}
						else if(report35.equals("05")){
							m509[4]++;
						}
						else if(report35.equals("06")){
							m509[5]++;
						}
						else if(report35.equals("07")){
							m509[6]++;
						}
						else if(report35.equals("08")){
							m509[7]++;
						}
						else if(report35.equals("09")){
							m509[8]++;
						}
					}
					else if(report25.equals("10")){
						m5[9]++;
						if(report35.equals("01")){
							m510[0]++;
						}
						else if(report35.equals("02")){
							m510[1]++;
						}
					}
					else if(report25.equals("11")){
						m5[10]++;
						if(report35.equals("01")){
							m511[0]++;
						}
						else if(report35.equals("02")){
							m511[1]++;
						}
						else if(report35.equals("03")){
							m511[2]++;
						}
						else if(report35.equals("04")){
							m511[3]++;
						}
						else if(report35.equals("05")){
							m511[4]++;
						}
						else if(report35.equals("06")){
							m511[5]++;
						}
						else if(report35.equals("07")){
							m511[6]++;
						}
					}
					else if(report25.equals("12")){
						m5[11]++;
						if(report35.equals("01")){
							m512[0]++;
						}
						else if(report35.equals("02")){
							m512[1]++;
						}
						else if(report35.equals("03")){
							m512[2]++;
						}
						else if(report35.equals("04")){
							m512[3]++;
						}
						else if(report35.equals("05")){
							m512[4]++;
						}
						else if(report35.equals("06")){
							m512[5]++;
						}
						else if(report35.equals("07")){
							m512[6]++;
						}
						else if(report35.equals("08")){
							m512[7]++;
						}
						else if(report35.equals("09")){
							m512[8]++;
						}
					}
					else if(report25.equals("13")){
						m5[12]++;
						if(report35.equals("01")){
							m513[0]++;
						}
						else if(report35.equals("02")){
							m513[1]++;
						}
						else if(report35.equals("03")){
							m513[2]++;
						}
						else if(report35.equals("04")){
							m513[3]++;
						}
						else if(report35.equals("05")){
							m513[4]++;
						}
						else if(report35.equals("06")){
							m513[5]++;
						}
						 
					}
					else if(report25.equals("14")){
						m5[13]++;
						if(report35.equals("01")){
							m514[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt4.close();
		//end 1104
			//1105
			String sql1105="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and  ((vnpres.suffix='1' and vnpres.clinic='1105') or (vnpres.suffix='2' and vnpres.clinic='1105') or (vnpres.suffix='3' and vnpres.clinic='1105'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt5 = conn.prepareStatement(sql1105);
			ResultSet rs5 = stmt5.executeQuery();
			int p5=1;
			while (rs5.next()){
				String report16="",report26="",report36="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs5.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report16=reportM[i][1].trim();
					}	 
				}
				if(report16.length()==4){
					 
					report26=report16.substring(0, 2);
					report36=report16.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report26.equals("01")){
						m6[0]++;
						if(report36.equals("01")){
							m601[0]++;
						}
						else if(report36.equals("02")){
							m601[1]++;
						}
						else if(report36.equals("03")){
							m601[2]++;
						}
					}
					else if(report26.equals("02")){
						m6[1]++;
						if(report36.equals("01")){
							m602[0]++;
						}
						else if(report36.equals("02")){
							m602[1]++;
						}
					}
					else if(report26.equals("03")){
						m6[2]++;
						if(report36.equals("01")){
							m603[0]++;
						}
						else if(report36.equals("02")){
							m603[1]++;
						}
						else if(report36.equals("03")){
							m603[2]++;
						}
						else if(report36.equals("04")){
							m603[3]++;
						}
						else if(report36.equals("05")){
							m603[4]++;
						}
						else if(report36.equals("06")){
							m603[5]++;
						}
					}
					else if(report26.equals("04")){
						m6[3]++;
						if(report36.equals("01")){
							m604[0]++;
						}
						else if(report36.equals("02")){
							m604[1]++;
						}
						else if(report36.equals("03")){
							m604[2]++;
						}
						else if(report36.equals("04")){
							m604[3]++;
						}
					}
					else if(report26.equals("05")){
						m6[4]++;
						if(report36.equals("01")){
							m605[0]++;
						}
						else if(report36.equals("02")){
							m605[1]++;
						}
						else if(report36.equals("03")){
							m605[2]++;
						}
						else if(report36.equals("04")){
							m605[3]++;
						}
						else if(report36.equals("05")){
							m605[4]++;
						}
						else if(report36.equals("06")){
							m605[5]++;
						}
						else if(report36.equals("07")){
							m605[6]++;
						}
						else if(report36.equals("08")){
							m605[7]++;
						}
					}
					else if(report26.equals("06")){
						m6[5]++;
						if(report36.equals("01")){
							m606[0]++;
						}
						else if(report36.equals("02")){
							m606[1]++;
						}
						else if(report36.equals("03")){
							m606[2]++;
						}
						else if(report36.equals("04")){
							m606[3]++;
						}
						else if(report36.equals("05")){
							m606[4]++;
						}
						else if(report36.equals("06")){
							m606[5]++;
						}
						else if(report36.equals("07")){
							m606[6]++;
						}
						else if(report36.equals("08")){
							m606[7]++;
						}
					}
					else if(report26.equals("07")){
						m6[6]++;
						if(report36.equals("01")){
							m607[0]++;
						}
						else if(report36.equals("02")){
							m607[1]++;
						}
						else if(report36.equals("03")){
							m607[2]++;
						}
						else if(report36.equals("04")){
							m607[3]++;
						}
						 
					}
					else if(report26.equals("08")){
						m6[7]++;
						if(report36.equals("01")){
							m608[0]++;
						}
						else if(report36.equals("02")){
							m608[1]++;
						}
						else if(report36.equals("03")){
							m608[2]++;
						}
						else if(report36.equals("04")){
							m608[3]++;
						}
						else if(report36.equals("05")){
							m608[4]++;
						}
						else if(report36.equals("06")){
							m608[5]++;
						}
						else if(report36.equals("07")){
							m608[6]++;
						}
						 
					}
					else if(report26.equals("09")){
						m6[8]++;
						if(report36.equals("01")){
							m609[0]++;
						}
						else if(report36.equals("02")){
							m609[1]++;
						}
						else if(report36.equals("03")){
							m609[2]++;
						}
						else if(report36.equals("04")){
							m609[3]++;
						}
						else if(report36.equals("05")){
							m609[4]++;
						}
						else if(report36.equals("06")){
							m609[5]++;
						}
						else if(report36.equals("07")){
							m609[6]++;
						}
						else if(report36.equals("08")){
							m609[7]++;
						}
						else if(report36.equals("09")){
							m609[8]++;
						}
					}
					else if(report26.equals("10")){
						m6[9]++;
						if(report36.equals("01")){
							m610[0]++;
						}
						else if(report36.equals("02")){
							m610[1]++;
						}
					}
					else if(report26.equals("11")){
						m6[10]++;
						if(report36.equals("01")){
							m611[0]++;
						}
						else if(report36.equals("02")){
							m611[1]++;
						}
						else if(report36.equals("03")){
							m611[2]++;
						}
						else if(report36.equals("04")){
							m611[3]++;
						}
						else if(report36.equals("05")){
							m611[4]++;
						}
						else if(report36.equals("06")){
							m611[5]++;
						}
						else if(report36.equals("07")){
							m611[6]++;
						}
					}
					else if(report26.equals("12")){
						m6[11]++;
						if(report36.equals("01")){
							m612[0]++;
						}
						else if(report36.equals("02")){
							m612[1]++;
						}
						else if(report36.equals("03")){
							m612[2]++;
						}
						else if(report36.equals("04")){
							m612[3]++;
						}
						else if(report36.equals("05")){
							m612[4]++;
						}
						else if(report36.equals("06")){
							m612[5]++;
						}
						else if(report36.equals("07")){
							m612[6]++;
						}
						else if(report36.equals("08")){
							m612[7]++;
						}
						else if(report36.equals("09")){
							m612[8]++;
						}
					}
					else if(report26.equals("13")){
						m6[12]++;
						if(report36.equals("01")){
							m613[0]++;
						}
						else if(report36.equals("02")){
							m613[1]++;
						}
						else if(report36.equals("03")){
							m613[2]++;
						}
						else if(report36.equals("04")){
							m613[3]++;
						}
						else if(report36.equals("05")){
							m613[4]++;
						}
						else if(report36.equals("06")){
							m613[5]++;
						}
						 
					}
					else if(report26.equals("14")){
						m6[13]++;
						if(report36.equals("01")){
							m614[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt5.close();
		//end 1105
			//1106
			//1106
			String sql1106="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and  ((vnpres.suffix='1' and vnpres.clinic='1106') or (vnpres.suffix='2' and vnpres.clinic='1106') or (vnpres.suffix='3' and vnpres.clinic='1106'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt6 = conn.prepareStatement(sql1106);
			ResultSet rs6 = stmt6.executeQuery();
			int p6=1;
			while (rs6.next()){
				String report17="",report27="",report37="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs6.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report17=reportM[i][1].trim();
					}	 
				}
				if(report17.length()==4){
					 
					report27=report17.substring(0, 2);
					report37=report17.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report27.equals("01")){
						m7[0]++;
						if(report37.equals("01")){
							m701[0]++;
						}
						else if(report37.equals("02")){
							m701[1]++;
						}
						else if(report37.equals("03")){
							m701[2]++;
						}
					}
					else if(report27.equals("02")){
						m7[1]++;
						if(report37.equals("01")){
							m702[0]++;
						}
						else if(report37.equals("02")){
							m702[1]++;
						}
					}
					else if(report27.equals("03")){
						m7[2]++;
						if(report37.equals("01")){
							m703[0]++;
						}
						else if(report37.equals("02")){
							m703[1]++;
						}
						else if(report37.equals("03")){
							m703[2]++;
						}
						else if(report37.equals("04")){
							m703[3]++;
						}
						else if(report37.equals("05")){
							m703[4]++;
						}
						else if(report37.equals("06")){
							m703[5]++;
						}
					}
					else if(report27.equals("04")){
						m7[3]++;
						if(report37.equals("01")){
							m704[0]++;
						}
						else if(report37.equals("02")){
							m704[1]++;
						}
						else if(report37.equals("03")){
							m704[2]++;
						}
						else if(report37.equals("04")){
							m704[3]++;
						}
					}
					else if(report27.equals("05")){
						m7[4]++;
						if(report37.equals("01")){
							m705[0]++;
						}
						else if(report37.equals("02")){
							m705[1]++;
						}
						else if(report37.equals("03")){
							m705[2]++;
						}
						else if(report37.equals("04")){
							m705[3]++;
						}
						else if(report37.equals("05")){
							m705[4]++;
						}
						else if(report37.equals("06")){
							m705[5]++;
						}
						else if(report37.equals("07")){
							m705[6]++;
						}
						else if(report37.equals("08")){
							m705[7]++;
						}
					}
					else if(report27.equals("06")){
						m7[5]++;
						if(report37.equals("01")){
							m706[0]++;
						}
						else if(report37.equals("02")){
							m706[1]++;
						}
						else if(report37.equals("03")){
							m706[2]++;
						}
						else if(report37.equals("04")){
							m706[3]++;
						}
						else if(report37.equals("05")){
							m706[4]++;
						}
						else if(report37.equals("06")){
							m706[5]++;
						}
						else if(report37.equals("07")){
							m706[6]++;
						}
						else if(report37.equals("08")){
							m706[7]++;
						}
					}
					else if(report27.equals("07")){
						m7[6]++;
						if(report37.equals("01")){
							m707[0]++;
						}
						else if(report37.equals("02")){
							m707[1]++;
						}
						else if(report37.equals("03")){
							m707[2]++;
						}
						else if(report37.equals("04")){
							m707[3]++;
						}
						 
					}
					else if(report27.equals("08")){
						m7[7]++;
						if(report37.equals("01")){
							m708[0]++;
						}
						else if(report37.equals("02")){
							m708[1]++;
						}
						else if(report37.equals("03")){
							m708[2]++;
						}
						else if(report37.equals("04")){
							m708[3]++;
						}
						else if(report37.equals("05")){
							m708[4]++;
						}
						else if(report37.equals("06")){
							m708[5]++;
						}
						else if(report37.equals("07")){
							m708[6]++;
						}
						 
					}
					else if(report27.equals("09")){
						m7[8]++;
						if(report37.equals("01")){
							m709[0]++;
						}
						else if(report37.equals("02")){
							m709[1]++;
						}
						else if(report37.equals("03")){
							m709[2]++;
						}
						else if(report37.equals("04")){
							m709[3]++;
						}
						else if(report37.equals("05")){
							m709[4]++;
						}
						else if(report37.equals("06")){
							m709[5]++;
						}
						else if(report37.equals("07")){
							m709[6]++;
						}
						else if(report37.equals("08")){
							m709[7]++;
						}
						else if(report37.equals("09")){
							m709[8]++;
						}
					}
					else if(report27.equals("10")){
						m7[9]++;
						if(report37.equals("01")){
							m710[0]++;
						}
						else if(report37.equals("02")){
							m710[1]++;
						}
					}
					else if(report27.equals("11")){
						m7[10]++;
						if(report37.equals("01")){
							m711[0]++;
						}
						else if(report37.equals("02")){
							m711[1]++;
						}
						else if(report37.equals("03")){
							m711[2]++;
						}
						else if(report37.equals("04")){
							m711[3]++;
						}
						else if(report37.equals("05")){
							m711[4]++;
						}
						else if(report37.equals("06")){
							m711[5]++;
						}
						else if(report37.equals("07")){
							m711[6]++;
						}
					}
					else if(report27.equals("12")){
						m7[11]++;
						if(report37.equals("01")){
							m712[0]++;
						}
						else if(report37.equals("02")){
							m712[1]++;
						}
						else if(report37.equals("03")){
							m712[2]++;
						}
						else if(report37.equals("04")){
							m712[3]++;
						}
						else if(report37.equals("05")){
							m712[4]++;
						}
						else if(report37.equals("06")){
							m712[5]++;
						}
						else if(report37.equals("07")){
							m712[6]++;
						}
						else if(report37.equals("08")){
							m712[7]++;
						}
						else if(report37.equals("09")){
							m712[8]++;
						}
					}
					else if(report27.equals("13")){
						m7[12]++;
						if(report37.equals("01")){
							m713[0]++;
						}
						else if(report37.equals("02")){
							m713[1]++;
						}
						else if(report37.equals("03")){
							m713[2]++;
						}
						else if(report37.equals("04")){
							m713[3]++;
						}
						else if(report37.equals("05")){
							m713[4]++;
						}
						else if(report37.equals("06")){
							m713[5]++;
						}
						 
					}
					else if(report27.equals("14")){
						m7[13]++;
						if(report37.equals("01")){
							m714[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt6.close();
		//end 1106
		
			conn.close();
			conn1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<m8.length;i++){
			m8[i]=m1[i]+m2[i]+m3[i]+m4[i]+m5[i]+m6[i]+m7[i]+m8[i];
		}
	    for(int i=0;i<m801.length;i++){
	    	m801[i]=m101[i]+m201[i]+m301[i]+m401[i]+m501[i]+m601[i]+m701[i];
	    }
	    for(int i=0;i<m802.length;i++){
	    	m802[i]=m102[i]+m202[i]+m302[i]+m402[i]+m502[i]+m602[i]+m702[i];
	    }
	    for(int i=0;i<m803.length;i++){
	    	m803[i]=m103[i]+m203[i]+m303[i]+m403[i]+m503[i]+m603[i]+m703[i];
	    }
	    for(int i=0;i<m804.length;i++){
	    	m804[i]=m104[i]+m204[i]+m304[i]+m404[i]+m504[i]+m604[i]+m704[i];
	    }
	    for(int i=0;i<m805.length;i++){
	    	m805[i]=m105[i]+m205[i]+m305[i]+m405[i]+m505[i]+m605[i]+m705[i];
	    }
	    for(int i=0;i<m806.length;i++){
	    	m806[i]=m106[i]+m206[i]+m306[i]+m406[i]+m506[i]+m606[i]+m706[i];
	    }
	    for(int i=0;i<m807.length;i++){
	    	m807[i]=m107[i]+m207[i]+m307[i]+m407[i]+m507[i]+m607[i]+m707[i];
	    }
	    for(int i=0;i<m808.length;i++){
	    	m808[i]=m108[i]+m208[i]+m308[i]+m408[i]+m508[i]+m608[i]+m708[i];
	    }
	    for(int i=0;i<m809.length;i++){
	    	m809[i]=m109[i]+m209[i]+m309[i]+m409[i]+m509[i]+m609[i]+m709[i];
	    }
	    for(int i=0;i<m810.length;i++){
	    	m810[i]=m110[i]+m210[i]+m310[i]+m410[i]+m510[i]+m610[i]+m710[i];
	    }
	    for(int i=0;i<m811.length;i++){
	    	m811[i]=m111[i]+m211[i]+m311[i]+m411[i]+m511[i]+m611[i]+m711[i];
	    }
	    for(int i=0;i<m812.length;i++){
	    	m812[i]=m112[i]+m212[i]+m312[i]+m412[i]+m512[i]+m612[i]+m712[i];
	    }
	    for(int i=0;i<m813.length;i++){
	    	m813[i]=m113[i]+m213[i]+m313[i]+m413[i]+m513[i]+m613[i]+m713[i];
	    }
	    for(int i=0;i<m814.length;i++){
	    	m814[i]=m114[i]+m214[i]+m314[i]+m414[i]+m514[i]+m614[i]+m714[i];
	    }
		/**
	   m801[0]=m101[0]+m201[0]+m301[0]+m401[0]+m501[0]+m601[0]+m701[0];
	   m802[0]=m102[0]+m202[0]+m302[0]+m402[0]+m502[0]+m602[0]+m702[0];
	   m803[0]=m103[0]+m203[0]+m303[0]+m403[0]+m503[0]+m603[0]+m703[0];
	   m804[0]=m104[0]+m204[0]+m304[0]+m404[0]+m504[0]+m604[0]+m704[0];
	   m805[0]=m105[0]+m205[0]+m305[0]+m405[0]+m505[0]+m605[0]+m705[0];
	   m806[0]=m106[0]+m206[0]+m306[0]+m406[0]+m506[0]+m606[0]+m706[0];
	   m807[0]=m107[0]+m207[0]+m307[0]+m407[0]+m507[0]+m607[0]+m707[0];
	   m808[0]=m108[0]+m208[0]+m308[0]+m408[0]+m508[0]+m608[0]+m708[0];
	   m809[0]=m109[0]+m209[0]+m309[0]+m409[0]+m509[0]+m609[0]+m709[0];
	   m810[0]=m110[0]+m210[0]+m310[0]+m410[0]+m510[0]+m610[0]+m710[0];
	   m811[0]=m111[0]+m211[0]+m311[0]+m411[0]+m511[0]+m611[0]+m711[0];
	   m812[0]=m112[0]+m212[0]+m312[0]+m412[0]+m512[0]+m612[0]+m712[0];
	   m813[0]=m113[0]+m213[0]+m313[0]+m413[0]+m513[0]+m613[0]+m713[0];
	   m814[0]=m114[0]+m214[0]+m314[0]+m414[0]+m514[0]+m614[0]+m714[0];
	   */
		/**
		for(int i=0;i<14;i++){
			final Vector<String> vstring = new Vector<String>();
			vstring.add(" "+r1[i]);
			vstring.add(""+m1[i]);
			vstring.add("");
			vstring.add("");
			vstring.add("");
			vstring.add("");
			vstring.add("");
			vstring.add("");
			 
			vstring.add("");
			//System.out.println(m[i]);
			dataReport.add(vstring);
		}
		 */
		for(int i=0;i<r1.length;i++){
			final Vector<String> vstring = new Vector<String>();
			vstring.add("[ "+r1[i]+" ]");
			vstring.add("  "+m1[i]);
			vstring.add("  "+m2[i]);
			vstring.add("  "+m3[i]);
			vstring.add("  "+m4[i]);
			vstring.add("  "+m5[i]);
			vstring.add("  "+m6[i]);
			vstring.add("  "+m7[i]);		 
			vstring.add("  "+m8[i]);
			//System.out.println(r1[i]);
			//System.out.println(r1[i]+","+m1[i]+","+m2[i]+","+m3[i]+","+m4[i]+","+m5[i]+","+m6[i]+","+m7[i]+","+m8[i]);
			dataReport.add(vstring);
			if(i==0){
				for(int j=0;j<r01.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r01[j]);
					vstring1.add("     "+m101[j]);
					vstring1.add("     "+m201[j]);
					vstring1.add("     "+m301[j]);
					vstring1.add("     "+m401[j]);
					vstring1.add("     "+m501[j]);
					vstring1.add("     "+m601[j]);
					vstring1.add("     "+m701[j]);		 
					vstring1.add("     "+m801[j]);
					//System.out.println(m[i]);
					//System.out.println(r01[j]+","+m101[j]+","+m201[j]+","+m301[j]+","+m401[j]+","+m501[j]+","+m601[j]+","+m701[j]+","+m801[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==1){
				for(int j=0;j<r02.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r02[j]);
					vstring1.add("     "+m102[j]);
					vstring1.add("     "+m202[j]);
					vstring1.add("     "+m302[j]);
					vstring1.add("     "+m402[j]);
					vstring1.add("     "+m502[j]);
					vstring1.add("     "+m602[j]);
					vstring1.add("     "+m702[j]);		 
					vstring1.add("     "+m802[j]);
					//System.out.println(m[i]);
					//System.out.println(r02[j]+","+m102[j]+","+m202[j]+","+m302[j]+","+m402[j]+","+m502[j]+","+m602[j]+","+m702[j]+","+m802[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==2){
				for(int j=0;j<r03.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r03[j]);
					vstring1.add("     "+m103[j]);
					vstring1.add("     "+m203[j]);
					vstring1.add("     "+m303[j]);
					vstring1.add("     "+m403[j]);
					vstring1.add("     "+m503[j]);
					vstring1.add("     "+m603[j]);
					vstring1.add("     "+m703[j]);		 
					vstring1.add("     "+m803[j]);
					//System.out.println(m[i]);
					//System.out.println(r03[j]+","+m103[j]+","+m203[j]+","+m303[j]+","+m403[j]+","+m503[j]+","+m603[j]+","+m703[j]+","+m803[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==3){
				for(int j=0;j<r04.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r04[j]);
					vstring1.add("     "+m104[j]);
					vstring1.add("     "+m204[j]);
					vstring1.add("     "+m304[j]);
					vstring1.add("     "+m404[j]);
					vstring1.add("     "+m504[j]);
					vstring1.add("     "+m604[j]);
					vstring1.add("     "+m704[j]);		 
					vstring1.add("     "+m804[j]);
					//System.out.println(m[i]);
					//System.out.println(r04[j]+","+m104[j]+","+m204[j]+","+m304[j]+","+m404[j]+","+m504[j]+","+m604[j]+","+m704[j]+","+m804[j]);
					dataReport.add(vstring1);
				}
			}if(i==4){
				for(int j=0;j<r05.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r05[j]);
					vstring1.add("     "+m105[j]);
					vstring1.add("     "+m205[j]);
					vstring1.add("     "+m305[j]);
					vstring1.add("     "+m405[j]);
					vstring1.add("     "+m505[j]);
					vstring1.add("     "+m605[j]);
					vstring1.add("     "+m705[j]);		 
					vstring1.add("     "+m805[j]);
					//System.out.println(m[i]);
					//System.out.println(r05[j]+","+m105[j]+","+m205[j]+","+m305[j]+","+m405[j]+","+m505[j]+","+m605[j]+","+m705[j]+","+m805[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==5){
				for(int j=0;j<r06.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r06[j]);
					vstring1.add("     "+m106[j]);
					vstring1.add("     "+m206[j]);
					vstring1.add("     "+m306[j]);
					vstring1.add("     "+m406[j]);
					vstring1.add("     "+m506[j]);
					vstring1.add("     "+m606[j]);
					vstring1.add("     "+m706[j]);		 
					vstring1.add("     "+m806[j]);
					//System.out.println(m[i]);
					//System.out.println(r06[j]+","+m106[j]+","+m206[j]+","+m306[j]+","+m406[j]+","+m506[j]+","+m606[j]+","+m706[j]+","+m806[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==6){
				for(int j=0;j<r07.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r07[j]);
					vstring1.add("     "+m107[j]);
					vstring1.add("     "+m207[j]);
					vstring1.add("     "+m307[j]);
					vstring1.add("     "+m407[j]);
					vstring1.add("     "+m507[j]);
					vstring1.add("     "+m607[j]);
					vstring1.add("     "+m707[j]);		 
					vstring1.add("     "+m807[j]);
					//System.out.println(m[i]);
					//System.out.println(r07[j]+","+m107[j]+","+m207[j]+","+m307[j]+","+m407[j]+","+m507[j]+","+m607[j]+","+m707[j]+","+m807[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==7){
				for(int j=0;j<r08.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r08[j]);
					vstring1.add("     "+m108[j]);
					vstring1.add("     "+m208[j]);
					vstring1.add("     "+m308[j]);
					vstring1.add("     "+m408[j]);
					vstring1.add("     "+m508[j]);
					vstring1.add("     "+m608[j]);
					vstring1.add("     "+m708[j]);		 
					vstring1.add("     "+m808[j]);
					//System.out.println(m[i]);
					//System.out.println(r08[j]+","+m108[j]+","+m208[j]+","+m308[j]+","+m408[j]+","+m508[j]+","+m608[j]+","+m708[j]+","+m808[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==8){
				for(int j=0;j<r09.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r09[j]);
					vstring1.add("     "+m109[j]);
					vstring1.add("     "+m209[j]);
					vstring1.add("     "+m309[j]);
					vstring1.add("     "+m409[j]);
					vstring1.add("     "+m509[j]);
					vstring1.add("     "+m609[j]);
					vstring1.add("     "+m709[j]);	 
					vstring1.add("     "+m809[j]);
					//System.out.println(m[i]);
					//System.out.println(r09[j]+","+m109[j]+","+m209[j]+","+m309[j]+","+m409[j]+","+m509[j]+","+m609[j]+","+m709[j]+","+m809[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==9){
				for(int j=0;j<r10.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r10[j]);
					vstring1.add("     "+m110[j]);
					vstring1.add("     "+m210[j]);
					vstring1.add("     "+m310[j]);
					vstring1.add("     "+m410[j]);
					vstring1.add("     "+m510[j]);
					vstring1.add("     "+m610[j]);
					vstring1.add("     "+m710[j]);		 
					vstring1.add("     "+m810[j]);
					//System.out.println(m[i]);
					//System.out.println(r10[j]+","+m110[j]+","+m210[j]+","+m310[j]+","+m410[j]+","+m510[j]+","+m610[j]+","+m710[j]+","+m810[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==10){
				for(int j=0;j<r11.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r11[j]);
					vstring1.add("     "+m111[j]);
					vstring1.add("     "+m211[j]);
					vstring1.add("     "+m311[j]);
					vstring1.add("     "+m411[j]);
					vstring1.add("     "+m511[j]);
					vstring1.add("     "+m611[j]);
					vstring1.add("     "+m711[j]);		 
					vstring1.add("     "+m811[j]);
					//System.out.println(m[i]);
					//System.out.println(r11[j]+","+m111[j]+","+m211[j]+","+m311[j]+","+m411[j]+","+m511[j]+","+m611[j]+","+m711[j]+","+m811[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==11){
				for(int j=0;j<r12.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r12[j]);
					vstring1.add("     "+m112[j]);
					vstring1.add("     "+m212[j]);
					vstring1.add("     "+m312[j]);
					vstring1.add("     "+m412[j]);
					vstring1.add("     "+m512[j]);
					vstring1.add("     "+m612[j]);
					vstring1.add("     "+m712[j]);		 
					vstring1.add("     "+m812[j]);
					//System.out.println(m[i]);
					//System.out.println(r12[j]+","+m112[j]+","+m212[j]+","+m312[j]+","+m412[j]+","+m512[j]+","+m612[j]+","+m712[j]+","+m812[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==12){
				for(int j=0;j<r13.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r13[j]);
					vstring1.add("     "+m113[j]);
					vstring1.add("     "+m213[j]);
					vstring1.add("     "+m313[j]);
					vstring1.add("     "+m413[j]);
					vstring1.add("     "+m513[j]);
					vstring1.add("     "+m613[j]);
					vstring1.add("     "+m713[j]);		 
					vstring1.add("     "+m813[j]);
					//System.out.println(m[i]);
					//System.out.println(r13[j]+","+m113[j]+","+m213[j]+","+m313[j]+","+m413[j]+","+m513[j]+","+m613[j]+","+m713[j]+","+m813[j]);
					dataReport.add(vstring1);
				}
			}
			if(i==13){
				for(int j=0;j<r14.length;j++){
					final Vector<String> vstring1 = new Vector<String>();
					vstring1.add(" "+r14[j]);
					vstring1.add("     "+m114[j]);
					vstring1.add("     "+m214[j]);
					vstring1.add("     "+m314[j]);
					vstring1.add("     "+m414[j]);
					vstring1.add("     "+m514[j]);
					vstring1.add("     "+m614[j]);
					vstring1.add("     "+m714[j]);		 
					vstring1.add("     "+m814[j]);
					//System.out.println(r14[j]+","+m114[j]+","+m214[j]+","+m314[j]+","+m414[j]+","+m514[j]+","+m614[j]+","+m714[j]+","+m814[j]);
					dataReport.add(vstring1);
				}
			}
			 
		}
		for(int t=0;t<m1042.length;t++){
			//System.out.println(t+". WW "+m1042[t]);
		}
	
		
		return new DefaultTableModel(dataReport, columnNamesReport);
	}
	public void getDataToExcel(){
		int [] m1={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m2={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m3={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m4={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m5={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m6={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m7={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int [] m8={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		String [] r1={"1.งานทันตวินิจฉัย","2.งานทันตรังสี","3.งานเวชศาสตร์ช่องปาก","4.งานทันตกรรมหัตถการ","5.งานรักษาคลองรากฟัน","6.งานปริทันต์","7.งานทันตกรรมป้องกัน","8.งานทันตกรรมเด็ก","9.งานทันตกรรมประดิษฐ์","10.งานทันตศัลยกรรม","11.งานศัลยกรรมช่องปาก","12.งานทันตกรรมจัดฟัน","13.งานทันตกรรมบดเคี้ยว","14.งานทันตกรรมเบ็ดเตล็ด"};
		String [] r01={"1.1 ตรวจ1","1.2 ตรวจ2","1.3 ตรวจผู้ป่วยเบาหวาน"};
		String [] r02={"2.1 งานทันตรังสี1","2.2 งานทันตรังสี2"};
		String [] r03={"3.1 เวชศาสตร์ช่องปาก1","3.2 เวชศาสตร์ช่องปาก2","3.3 เวชศาสตร์ช่องปาก3","3.4 เวชศาสตร์ช่องปาก4","3.5 เวชศาสตร์ช่องปาก5","3.6 เวชศาสตร์ช่องปาก6"};
		String [] r04={"4.1 ทันตกรรมหัตถการ1","4.2 ทันตกรรมหัตถการ2","4.3 ทันตกรรมหัตถการ3","4.4 ทันตกรรมหัตถการ4"};
		String [] r05={"5.1 รักษาคลองรากฟัน1","5.2 รักษาคลองรากฟัน2","5.3 รักษาคลองรากฟัน3","5.4 รักษาคลองรากฟัน4","5.5 รักษาคลองรากฟัน5","5.6 รักษาคลองรากฟัน6","5.7 รักษาคลองรากฟัน7","5.8 รักษาคลองรากฟัน8"};
		String [] r06={"6.1 ปริทันต์ 1","6.2 ปริทันต์ 2","6.3 ปริทันต์ 3","6.4 ปริทันต์ 4","6.5 ปริทันต์ 5","6.6 ปริทันต์ 6","6.7 ปริทันต์ 7","6.8 ปริทันต์ 8"};
		String [] r07={"7.1 ทันตกรรมป้องกัน1","7.2 ทันตกรรมป้องกัน2","7.3 ทันตกรรมป้องกัน3","7.4 ทันตกรรมป้องกัน4"};
		String [] r08={"8.1 ทันตกรรมเด็ก1","8.2 ทันตกรรมเด็ก2","8.3 ทันตกรรมเด็ก3","8.4 ทันตกรรมเด็ก4","8.5 ทันตกรรมเด็ก5","8.6 ทันตกรรมเด็ก6","8.7 ทันตกรรมเด็ก7"};
		String [] r09={"9.1 ทันตกรรมประดิษฐ์1","9.2 ทันตกรรมประดิษฐ์2","9.3 ทันตกรรมประดิษฐ์3","9.4 ทันตกรรมประดิษฐ์4","9.5 ทันตกรรมประดิษฐ์5","9.6 ทันตกรรมประดิษฐ์6","9.7 ทันตกรรมประดิษฐ์7","9.8 ทันตกรรมประดิษฐ์8","9.9 ทันตกรรมประดิษฐ์9"};
		String [] r10={"10.1 ทันตศัลยกรรม1","10.2 ทันตศัลยกรรม2"};
		String [] r11={"11.1 ศัลยกรรมช่องปาก1","11.2 ศัลยกรรมช่องปาก2","11.3 ศัลยกรรมช่องปาก3","11.4 ศัลยกรรมช่องปาก4","11.5 ศัลยกรรมช่องปาก5","11.6 ศัลยกรรมช่องปาก6","11.7 ศัลยกรรมช่องปาก7"};
		String [] r12={"12.1 ทันตกรรมจัดฟัน1","12.2 ทันตกรรมจัดฟัน2","12.3 ทันตกรรมจัดฟัน3","12.4 ทันตกรรมจัดฟัน4","12.5 ทันตกรรมจัดฟัน5","12.6 ทันตกรรมจัดฟัน6","12.7 ทันตกรรมจัดฟัน7","12.8 ทันตกรรมจัดฟัน8","12.9 ทันตกรรมจัดฟัน9"};
		String [] r13={"13.1 ทันตกรรมบดเคี้ยว1","13.2 ทันตกรรมบดเคี้ยว2","13.3 ทันตกรรมบดเคี้ยว3","13.4 ทันตกรรมบดเคี้ยว4","13.5 ทันตกรรมบดเคี้ยว5","13.6 ทันตกรรมบดเคี้ยว6"};
		String [] r14={"14.1 ทันตกรรมเบ็ดเตล็ด"};
		
		String [] r042={"อุดฟัน Class III Amalgam","อุดฟัน Class V Amalgam","อุดฟัน Class I Toothlike material","อุดฟัน Class III Toothlike material","อุดฟัน Class V Toothlike material"
				,"อุดฟันชั่วคราว","อุดฟัน Class I Compound Aamalgam","อุดฟัน Class II Aamalgam","อุดฟัน Class IV Toothlike","อุดฟัน Class I Compound Toothlike","อุดฟัน Class II Toothlike","อุดฟัน  2 ด้าน"};
		int [] m101={0,0,0};
		int [] m102={0,0};
		int [] m103={0,0,0,0,0,0};
		int [] m104={0,0,0,0};
		int [] m105={0,0,0,0,0,0,0,0};
		int [] m106={0,0,0,0,0,0,0,0};
		int [] m107={0,0,0,0};
		int [] m108={0,0,0,0,0,0,0};
		int [] m109={0,0,0,0,0,0,0,0,0};
		int [] m110={0,0};
		int [] m111={0,0,0,0,0,0,0};
		int [] m112={0,0,0,0,0,0,0,0,0};
		int [] m113={0,0,0,0,0,0};
		int [] m114={0};
		
		int [] m1042={0,0,0,0,0,0,0,0,0,0,0,0};
		
		int [] m201={0,0,0};
		int [] m202={0,0};
		int [] m203={0,0,0,0,0,0};
		int [] m204={0,0,0,0};
		int [] m205={0,0,0,0,0,0,0,0};
		int [] m206={0,0,0,0,0,0,0,0};
		int [] m207={0,0,0,0};
		int [] m208={0,0,0,0,0,0,0};
		int [] m209={0,0,0,0,0,0,0,0,0};
		int [] m210={0,0};
		int [] m211={0,0,0,0,0,0,0};
		int [] m212={0,0,0,0,0,0,0,0,0};
		int [] m213={0,0,0,0,0,0};
		int [] m214={0};
		
		int [] m301={0,0,0};
		int [] m302={0,0};
		int [] m303={0,0,0,0,0,0};
		int [] m304={0,0,0,0};
		int [] m305={0,0,0,0,0,0,0,0};
		int [] m306={0,0,0,0,0,0,0,0};
		int [] m307={0,0,0,0};
		int [] m308={0,0,0,0,0,0,0};
		int [] m309={0,0,0,0,0,0,0,0,0};
		int [] m310={0,0};
		int [] m311={0,0,0,0,0,0,0};
		int [] m312={0,0,0,0,0,0,0,0,0};
		int [] m313={0,0,0,0,0,0};
		int [] m314={0};
		
		int [] m401={0,0,0};
		int [] m402={0,0};
		int [] m403={0,0,0,0,0,0};
		int [] m404={0,0,0,0};
		int [] m405={0,0,0,0,0,0,0,0};
		int [] m406={0,0,0,0,0,0,0,0};
		int [] m407={0,0,0,0};
		int [] m408={0,0,0,0,0,0,0};
		int [] m409={0,0,0,0,0,0,0,0,0};
		int [] m410={0,0};
		int [] m411={0,0,0,0,0,0,0};
		int [] m412={0,0,0,0,0,0,0,0,0};
		int [] m413={0,0,0,0,0,0};
		int [] m414={0};
		
		int [] m501={0,0,0};
		int [] m502={0,0};
		int [] m503={0,0,0,0,0,0};
		int [] m504={0,0,0,0};
		int [] m505={0,0,0,0,0,0,0,0};
		int [] m506={0,0,0,0,0,0,0,0};
		int [] m507={0,0,0,0};
		int [] m508={0,0,0,0,0,0,0};
		int [] m509={0,0,0,0,0,0,0,0,0};
		int [] m510={0,0};
		int [] m511={0,0,0,0,0,0,0};
		int [] m512={0,0,0,0,0,0,0,0,0};
		int [] m513={0,0,0,0,0,0};
		int [] m514={0};
		
		int [] m601={0,0,0};
		int [] m602={0,0};
		int [] m603={0,0,0,0,0,0};
		int [] m604={0,0,0,0};
		int [] m605={0,0,0,0,0,0,0,0};
		int [] m606={0,0,0,0,0,0,0,0};
		int [] m607={0,0,0,0};
		int [] m608={0,0,0,0,0,0,0};
		int [] m609={0,0,0,0,0,0,0,0,0};
		int [] m610={0,0};
		int [] m611={0,0,0,0,0,0,0};
		int [] m612={0,0,0,0,0,0,0,0,0};
		int [] m613={0,0,0,0,0,0};
		int [] m614={0};
		
		int [] m701={0,0,0};
		int [] m702={0,0};
		int [] m703={0,0,0,0,0,0};
		int [] m704={0,0,0,0};
		int [] m705={0,0,0,0,0,0,0,0};
		int [] m706={0,0,0,0,0,0,0,0};
		int [] m707={0,0,0,0};
		int [] m708={0,0,0,0,0,0,0};
		int [] m709={0,0,0,0,0,0,0,0,0};
		int [] m710={0,0};
		int [] m711={0,0,0,0,0,0,0};
		int [] m712={0,0,0,0,0,0,0,0,0};
		int [] m713={0,0,0,0,0,0};
		int [] m714={0};
		 	
		int [] m801={0,0,0};
		int [] m802={0,0};
		int [] m803={0,0,0,0,0,0};
		int [] m804={0,0,0,0};
		int [] m805={0,0,0,0,0,0,0,0};
		int [] m806={0,0,0,0,0,0,0,0};
		int [] m807={0,0,0,0};
		int [] m808={0,0,0,0,0,0,0};
		int [] m809={0,0,0,0,0,0,0,0,0};
		int [] m810={0,0};
		int [] m811={0,0,0,0,0,0,0};
		int [] m812={0,0,0,0,0,0,0,0,0};
		int [] m813={0,0,0,0,0,0};
		int [] m814={0};
		
		String b=Setup.DateInDBMSSQL(dateSearch1.trim());
		String f=Setup.DateInDBMSSQL(dateSearch2.trim());
		
		String doctor_search="";
		if(((ComboItem)cb1.getSelectedItem()).getValue().equals("000")){
			
		}else{
			doctor_search=" and dentaltreat.doctor='"+((ComboItem)cb1.getSelectedItem()).getValue().trim()+"' ";
		}
		 
		
		/*
		Connection con=new DBmanager().getConnMySql(InApp.UrlCust6);;
		String sql = "select name, address from person_table";
		String sql="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and ((vnpres.suffix='1' and vnpres.clinic='1100') or (vnpres.suffix='2' and vnpres.clinic='1100') or (vnpres.suffix='3' and vnpres.clinic='1100')) and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
		
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ResultSet resultSet = ps.executeQuery();    

			int row = 1;
			while(resultSet.next()) {
			    String name = resultSet.getString("name");
			    String address = resultSet.getString("address");

			    Row dataRow = personSheet.createRow(row);

			    Cell dataNameCell = dataRow.createCell(0);
			    dataNameCell.setCellValue(name);

			    Cell dataAddressCell = dataRow.createCell(1);
			    dataAddressCell.setCellValue(address);

			    row = row + 1;
			    
			    
			}
			ps.close();
			con.close();
			*/
			String sql="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and ((vnpres.suffix='1' and vnpres.clinic='1100') or (vnpres.suffix='2' and vnpres.clinic='1100') or (vnpres.suffix='3' and vnpres.clinic='1100')) and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
		
			Connection conn =  new DBmanager().getConnMSSql();
			
			Connection conn1 =  new DBmanager().getConnMySql();
			//1100
			try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			int p=1;
			
			while (rs.next()){
				String report1="",report2="",report3="",report4="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report1=reportM[i][1].trim();
					}	 
				}
				for(int i=0;i<reportS.length;i++){
					if(rs.getString(1).trim().toUpperCase() .equals(reportS[i][0].trim().toUpperCase() )){
						report4=reportS[i][1].trim();
					}	 
				}
				if(report1.length()==4){
					 
					report2=report1.substring(0, 2);
					report3=report1.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report2.equals("01")){
						m1[0]++;
						if(report3.equals("01")){
							m101[0]++;
						}
						else if(report3.equals("02")){
							m101[1]++;
						}
						else if(report3.equals("03")){
							m101[2]++;
						}
					}
					else if(report2.equals("02")){
						m1[1]++;
						if(report3.equals("01")){
							m102[0]++;
						}
						else if(report3.equals("02")){
							m102[1]++;
						}
					}
					else if(report2.equals("03")){
						m1[2]++;
						if(report3.equals("01")){
							m103[0]++;
						}
						else if(report3.equals("02")){
							m103[1]++;
						}
						else if(report3.equals("03")){
							m103[2]++;
						}
						else if(report3.equals("04")){
							m103[3]++;
						}
						else if(report3.equals("05")){
							m103[4]++;
						}
						else if(report3.equals("06")){
							m103[5]++;
						}
					}
					else if(report2.equals("04")){
						m1[3]++;
						if(report3.equals("01")){
							m104[0]++;
						}
						else if(report3.equals("02")){
							m104[1]++;
						}
						else if(report3.equals("03")){
							m104[2]++;
						}
						else if(report3.equals("04")){
							m104[3]++;
						}
						///
						if(report4.equals("01")){
							m1042[0]++;
						}
						else if(report4.equals("02")){
							m1042[1]++;
						}
						else if(report4.equals("03")){
							m1042[2]++;
						}
						else if(report4.equals("04")){
							m1042[3]++;
						}
						else if(report4.equals("05")){
							m1042[4]++;
						}
						else if(report4.equals("06")){
							m1042[5]++;
						}
						else if(report4.equals("07")){
							m1042[6]++;
						}
						else if(report4.equals("08")){
							m1042[7]++;
						}
						else if(report4.equals("09")){
							m1042[8]++;
						}
						else if(report4.equals("10")){
							m1042[9]++;
						}
						else if(report4.equals("11")){
							m1042[10]++;
						}
						else if(report4.equals("12")){
							m1042[11]++;
						}
						for(int t=0;t<m1042.length;t++){
							//System.out.println(t+". WW "+m1042[t]);
						}
					
					}
					else if(report2.equals("05")){
						m1[4]++;
						if(report3.equals("01")){
							m105[0]++;
						}
						else if(report3.equals("02")){
							m105[1]++;
						}
						else if(report3.equals("03")){
							m105[2]++;
						}
						else if(report3.equals("04")){
							m105[3]++;
						}
						else if(report3.equals("05")){
							m105[4]++;
						}
						else if(report3.equals("06")){
							m105[5]++;
						}
						else if(report3.equals("07")){
							m105[6]++;
						}
						else if(report3.equals("08")){
							m105[7]++;
						}
					}
					else if(report2.equals("06")){
						m1[5]++;
						if(report3.equals("01")){
							m106[0]++;
						}
						else if(report3.equals("02")){
							m106[1]++;
						}
						else if(report3.equals("03")){
							m106[2]++;
						}
						else if(report3.equals("04")){
							m106[3]++;
						}
						else if(report3.equals("05")){
							m106[4]++;
						}
						else if(report3.equals("06")){
							m106[5]++;
						}
						else if(report3.equals("07")){
							m106[6]++;
						}
						else if(report3.equals("08")){
							m106[7]++;
						}
					}
					else if(report2.equals("07")){
						m1[6]++;
						if(report3.equals("01")){
							m107[0]++;
						}
						else if(report3.equals("02")){
							m107[1]++;
						}
						else if(report3.equals("03")){
							m107[2]++;
						}
						else if(report3.equals("04")){
							m107[3]++;
						}
						 
					}
					else if(report2.equals("08")){
						m1[7]++;
						if(report3.equals("01")){
							m108[0]++;
						}
						else if(report3.equals("02")){
							m108[1]++;
						}
						else if(report3.equals("03")){
							m108[2]++;
						}
						else if(report3.equals("04")){
							m108[3]++;
						}
						else if(report3.equals("05")){
							m108[4]++;
						}
						else if(report3.equals("06")){
							m108[5]++;
						}
						else if(report3.equals("07")){
							m108[6]++;
						}
						 
					}
					else if(report2.equals("09")){
						m1[8]++;
						if(report3.equals("01")){
							m109[0]++;
						}
						else if(report3.equals("02")){
							m109[1]++;
						}
						else if(report3.equals("03")){
							m109[2]++;
						}
						else if(report3.equals("04")){
							m109[3]++;
						}
						else if(report3.equals("05")){
							m109[4]++;
						}
						else if(report3.equals("06")){
							m109[5]++;
						}
						else if(report3.equals("07")){
							m109[6]++;
						}
						else if(report3.equals("08")){
							m109[7]++;
						}
						else if(report3.equals("09")){
							m109[8]++;
						}
					}
					else if(report2.equals("10")){
						m1[9]++;
						if(report3.equals("01")){
							m110[0]++;
						}
						else if(report3.equals("02")){
							m110[1]++;
						}
					}
					else if(report2.equals("11")){
						m1[10]++;
						if(report3.equals("01")){
							m111[0]++;
						}
						else if(report3.equals("02")){
							m111[1]++;
						}
						else if(report3.equals("03")){
							m111[2]++;
						}
						else if(report3.equals("04")){
							m111[3]++;
						}
						else if(report3.equals("05")){
							m111[4]++;
						}
						else if(report3.equals("06")){
							m111[5]++;
						}
						else if(report3.equals("07")){
							m111[6]++;
						}
					}
					else if(report2.equals("12")){
						m1[11]++;
						if(report3.equals("01")){
							m112[0]++;
						}
						else if(report3.equals("02")){
							m112[1]++;
						}
						else if(report3.equals("03")){
							m112[2]++;
						}
						else if(report3.equals("04")){
							m112[3]++;
						}
						else if(report3.equals("05")){
							m112[4]++;
						}
						else if(report3.equals("06")){
							m112[5]++;
						}
						else if(report3.equals("07")){
							m112[6]++;
						}
						else if(report3.equals("08")){
							m112[7]++;
						}
						else if(report3.equals("09")){
							m112[8]++;
						}
					}
					else if(report2.equals("13")){
						m1[12]++;
						if(report3.equals("01")){
							m113[0]++;
						}
						else if(report3.equals("02")){
							m113[1]++;
						}
						else if(report3.equals("03")){
							m113[2]++;
						}
						else if(report3.equals("04")){
							m113[3]++;
						}
						else if(report3.equals("05")){
							m113[4]++;
						}
						else if(report3.equals("06")){
							m113[5]++;
						}
						 
					}
					else if(report2.equals("14")){
						m1[13]++;
						if(report3.equals("01")){
							m114[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt.close();
			//1101
			String sql1101="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and ((vnpres.suffix='1' and vnpres.clinic='1101') or (vnpres.suffix='2' and vnpres.clinic='1101') or (vnpres.suffix='3' and vnpres.clinic='1101'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt1 = conn.prepareStatement(sql1101);
			ResultSet rs1 = stmt1.executeQuery();
			int p1=1;
			while (rs1.next()){
				String report12="",report22="",report32="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs1.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report12=reportM[i][1].trim();
					}	 
				}
				if(report12.length()==4){
					 
					report22=report12.substring(0, 2);
					report32=report12.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report22.equals("01")){
						m2[0]++;
						if(report32.equals("01")){
							m201[0]++;
						}
						else if(report32.equals("02")){
							m201[1]++;
						}
						else if(report32.equals("03")){
							m201[2]++;
						}
					}
					else if(report22.equals("02")){
						m2[1]++;
						if(report32.equals("01")){
							m202[0]++;
						}
						else if(report32.equals("02")){
							m202[1]++;
						}
					}
					else if(report22.equals("03")){
						m2[2]++;
						if(report32.equals("01")){
							m203[0]++;
						}
						else if(report32.equals("02")){
							m203[1]++;
						}
						else if(report32.equals("03")){
							m203[2]++;
						}
						else if(report32.equals("04")){
							m203[3]++;
						}
						else if(report32.equals("05")){
							m203[4]++;
						}
						else if(report32.equals("06")){
							m203[5]++;
						}
					}
					else if(report22.equals("04")){
						m2[3]++;
						if(report32.equals("01")){
							m204[0]++;
						}
						else if(report32.equals("02")){
							m204[1]++;
						}
						else if(report32.equals("03")){
							m204[2]++;
						}
						else if(report32.equals("04")){
							m204[3]++;
						}
					}
					else if(report22.equals("05")){
						m2[4]++;
						if(report32.equals("01")){
							m205[0]++;
						}
						else if(report32.equals("02")){
							m205[1]++;
						}
						else if(report32.equals("03")){
							m205[2]++;
						}
						else if(report32.equals("04")){
							m205[3]++;
						}
						else if(report32.equals("05")){
							m205[4]++;
						}
						else if(report32.equals("06")){
							m205[5]++;
						}
						else if(report32.equals("07")){
							m205[6]++;
						}
						else if(report32.equals("08")){
							m205[7]++;
						}
					}
					else if(report22.equals("06")){
						m2[5]++;
						if(report32.equals("01")){
							m206[0]++;
						}
						else if(report32.equals("02")){
							m206[1]++;
						}
						else if(report32.equals("03")){
							m206[2]++;
						}
						else if(report32.equals("04")){
							m206[3]++;
						}
						else if(report32.equals("05")){
							m206[4]++;
						}
						else if(report32.equals("06")){
							m206[5]++;
						}
						else if(report32.equals("07")){
							m206[6]++;
						}
						else if(report32.equals("08")){
							m206[7]++;
						}
					}
					else if(report22.equals("07")){
						m2[6]++;
						if(report32.equals("01")){
							m207[0]++;
						}
						else if(report32.equals("02")){
							m207[1]++;
						}
						else if(report32.equals("03")){
							m207[2]++;
						}
						else if(report32.equals("04")){
							m207[3]++;
						}
						 
					}
					else if(report22.equals("08")){
						m2[7]++;
						if(report32.equals("01")){
							m208[0]++;
						}
						else if(report32.equals("02")){
							m208[1]++;
						}
						else if(report32.equals("03")){
							m208[2]++;
						}
						else if(report32.equals("04")){
							m208[3]++;
						}
						else if(report32.equals("05")){
							m208[4]++;
						}
						else if(report32.equals("06")){
							m208[5]++;
						}
						else if(report32.equals("07")){
							m208[6]++;
						}
						 
					}
					else if(report22.equals("09")){
						m2[8]++;
						if(report32.equals("01")){
							m209[0]++;
						}
						else if(report32.equals("02")){
							m209[1]++;
						}
						else if(report32.equals("03")){
							m209[2]++;
						}
						else if(report32.equals("04")){
							m209[3]++;
						}
						else if(report32.equals("05")){
							m209[4]++;
						}
						else if(report32.equals("06")){
							m209[5]++;
						}
						else if(report32.equals("07")){
							m209[6]++;
						}
						else if(report32.equals("08")){
							m209[7]++;
						}
						else if(report32.equals("09")){
							m209[8]++;
						}
					}
					else if(report22.equals("10")){
						m2[9]++;
						if(report32.equals("01")){
							m210[0]++;
						}
						else if(report32.equals("02")){
							m210[1]++;
						}
					}
					else if(report22.equals("11")){
						m2[10]++;
						if(report32.equals("01")){
							m211[0]++;
						}
						else if(report32.equals("02")){
							m211[1]++;
						}
						else if(report32.equals("03")){
							m211[2]++;
						}
						else if(report32.equals("04")){
							m211[3]++;
						}
						else if(report32.equals("05")){
							m211[4]++;
						}
						else if(report32.equals("06")){
							m211[5]++;
						}
						else if(report32.equals("07")){
							m211[6]++;
						}
					}
					else if(report22.equals("12")){
						m2[11]++;
						if(report32.equals("01")){
							m212[0]++;
						}
						else if(report32.equals("02")){
							m212[1]++;
						}
						else if(report32.equals("03")){
							m212[2]++;
						}
						else if(report32.equals("04")){
							m212[3]++;
						}
						else if(report32.equals("05")){
							m212[4]++;
						}
						else if(report32.equals("06")){
							m212[5]++;
						}
						else if(report32.equals("07")){
							m212[6]++;
						}
						else if(report32.equals("08")){
							m212[7]++;
						}
						else if(report32.equals("09")){
							m212[8]++;
						}
					}
					else if(report22.equals("13")){
						m2[12]++;
						if(report32.equals("01")){
							m213[0]++;
						}
						else if(report32.equals("02")){
							m213[1]++;
						}
						else if(report32.equals("03")){
							m213[2]++;
						}
						else if(report32.equals("04")){
							m213[3]++;
						}
						else if(report32.equals("05")){
							m213[4]++;
						}
						else if(report32.equals("06")){
							m213[5]++;
						}
						 
					}
					else if(report22.equals("14")){
						m2[13]++;
						if(report32.equals("01")){
							m214[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt1.close();
			//end 1101
			
			//1102
			
			String sql1102="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and ((vnpres.suffix='1' and vnpres.clinic='1102') or (vnpres.suffix='2' and vnpres.clinic='1102') or (vnpres.suffix='3' and vnpres.clinic='1102'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt2 = conn.prepareStatement(sql1102);
			ResultSet rs2 = stmt2.executeQuery();
			int p2=1;
			while (rs2.next()){
				String report13="",report23="",report33="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs2.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report13=reportM[i][1].trim();
					}	 
				}
				if(report13.length()==4){
					 
					report23=report13.substring(0, 2);
					report33=report13.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report23.equals("01")){
						m3[0]++;
						if(report33.equals("01")){
							m301[0]++;
						}
						else if(report33.equals("02")){
							m301[1]++;
						}
						else if(report33.equals("03")){
							m301[2]++;
						}
					}
					else if(report23.equals("02")){
						m3[1]++;
						if(report33.equals("01")){
							m302[0]++;
						}
						else if(report33.equals("02")){
							m302[1]++;
						}
					}
					else if(report23.equals("03")){
						m2[2]++;
						if(report33.equals("01")){
							m303[0]++;
						}
						else if(report33.equals("02")){
							m303[1]++;
						}
						else if(report33.equals("03")){
							m303[2]++;
						}
						else if(report33.equals("04")){
							m303[3]++;
						}
						else if(report33.equals("05")){
							m303[4]++;
						}
						else if(report33.equals("06")){
							m303[5]++;
						}
					}
					else if(report23.equals("04")){
						m3[3]++;
						if(report33.equals("01")){
							m304[0]++;
						}
						else if(report33.equals("02")){
							m304[1]++;
						}
						else if(report33.equals("03")){
							m304[2]++;
						}
						else if(report33.equals("04")){
							m304[3]++;
						}
					}
					else if(report23.equals("05")){
						m3[4]++;
						if(report33.equals("01")){
							m305[0]++;
						}
						else if(report33.equals("02")){
							m305[1]++;
						}
						else if(report33.equals("03")){
							m305[2]++;
						}
						else if(report33.equals("04")){
							m305[3]++;
						}
						else if(report33.equals("05")){
							m305[4]++;
						}
						else if(report33.equals("06")){
							m305[5]++;
						}
						else if(report33.equals("07")){
							m305[6]++;
						}
						else if(report33.equals("08")){
							m305[7]++;
						}
					}
					else if(report23.equals("06")){
						m3[5]++;
						if(report33.equals("01")){
							m306[0]++;
						}
						else if(report33.equals("02")){
							m306[1]++;
						}
						else if(report33.equals("03")){
							m306[2]++;
						}
						else if(report33.equals("04")){
							m306[3]++;
						}
						else if(report33.equals("05")){
							m306[4]++;
						}
						else if(report33.equals("06")){
							m306[5]++;
						}
						else if(report33.equals("07")){
							m306[6]++;
						}
						else if(report33.equals("08")){
							m306[7]++;
						}
					}
					else if(report23.equals("07")){
						m3[6]++;
						if(report33.equals("01")){
							m307[0]++;
						}
						else if(report33.equals("02")){
							m307[1]++;
						}
						else if(report33.equals("03")){
							m307[2]++;
						}
						else if(report33.equals("04")){
							m307[3]++;
						}
						 
					}
					else if(report23.equals("08")){
						m3[7]++;
						if(report33.equals("01")){
							m308[0]++;
						}
						else if(report33.equals("02")){
							m308[1]++;
						}
						else if(report33.equals("03")){
							m308[2]++;
						}
						else if(report33.equals("04")){
							m308[3]++;
						}
						else if(report33.equals("05")){
							m308[4]++;
						}
						else if(report33.equals("06")){
							m308[5]++;
						}
						else if(report33.equals("07")){
							m308[6]++;
						}
						 
					}
					else if(report23.equals("09")){
						m3[8]++;
						if(report33.equals("01")){
							m309[0]++;
						}
						else if(report33.equals("02")){
							m309[1]++;
						}
						else if(report33.equals("03")){
							m309[2]++;
						}
						else if(report33.equals("04")){
							m309[3]++;
						}
						else if(report33.equals("05")){
							m309[4]++;
						}
						else if(report33.equals("06")){
							m309[5]++;
						}
						else if(report33.equals("07")){
							m309[6]++;
						}
						else if(report33.equals("08")){
							m309[7]++;
						}
						else if(report33.equals("09")){
							m309[8]++;
						}
					}
					else if(report23.equals("10")){
						m3[9]++;
						if(report33.equals("01")){
							m310[0]++;
						}
						else if(report33.equals("02")){
							m310[1]++;
						}
					}
					else if(report23.equals("11")){
						m3[10]++;
						if(report33.equals("01")){
							m311[0]++;
						}
						else if(report33.equals("02")){
							m311[1]++;
						}
						else if(report33.equals("03")){
							m311[2]++;
						}
						else if(report33.equals("04")){
							m311[3]++;
						}
						else if(report33.equals("05")){
							m311[4]++;
						}
						else if(report33.equals("06")){
							m311[5]++;
						}
						else if(report33.equals("07")){
							m311[6]++;
						}
					}
					else if(report23.equals("12")){
						m3[11]++;
						if(report33.equals("01")){
							m312[0]++;
						}
						else if(report33.equals("02")){
							m312[1]++;
						}
						else if(report33.equals("03")){
							m312[2]++;
						}
						else if(report33.equals("04")){
							m312[3]++;
						}
						else if(report33.equals("05")){
							m312[4]++;
						}
						else if(report33.equals("06")){
							m312[5]++;
						}
						else if(report33.equals("07")){
							m312[6]++;
						}
						else if(report33.equals("08")){
							m312[7]++;
						}
						else if(report33.equals("09")){
							m312[8]++;
						}
					}
					else if(report23.equals("13")){
						m3[12]++;
						if(report33.equals("01")){
							m313[0]++;
						}
						else if(report33.equals("02")){
							m313[1]++;
						}
						else if(report33.equals("03")){
							m313[2]++;
						}
						else if(report33.equals("04")){
							m313[3]++;
						}
						else if(report33.equals("05")){
							m313[4]++;
						}
						else if(report33.equals("06")){
							m313[5]++;
						}
						 
					}
					else if(report23.equals("14")){
						m3[13]++;
						if(report33.equals("01")){
							m314[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt2.close();
		//end 1102
	//1103
			
			String sql1103="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and  ((vnpres.suffix='1' and vnpres.clinic='1103') or (vnpres.suffix='2' and vnpres.clinic='1103') or (vnpres.suffix='3' and vnpres.clinic='1103'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt3 = conn.prepareStatement(sql1103);
			ResultSet rs3 = stmt3.executeQuery();
			int p3=1;
			while (rs3.next()){
				String report14="",report24="",report34="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs3.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report14=reportM[i][1].trim();
					}	 
				}
				if(report14.length()==4){
					 
					report24=report14.substring(0, 2);
					report34=report14.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report24.equals("01")){
						m4[0]++;
						if(report34.equals("01")){
							m401[0]++;
						}
						else if(report34.equals("02")){
							m401[1]++;
						}
						else if(report34.equals("03")){
							m401[2]++;
						}
					}
					else if(report24.equals("02")){
						m4[1]++;
						if(report34.equals("01")){
							m402[0]++;
						}
						else if(report34.equals("02")){
							m402[1]++;
						}
					}
					else if(report24.equals("03")){
						m4[2]++;
						if(report34.equals("01")){
							m403[0]++;
						}
						else if(report34.equals("02")){
							m403[1]++;
						}
						else if(report34.equals("03")){
							m403[2]++;
						}
						else if(report34.equals("04")){
							m403[3]++;
						}
						else if(report34.equals("05")){
							m403[4]++;
						}
						else if(report34.equals("06")){
							m403[5]++;
						}
					}
					else if(report24.equals("04")){
						m4[3]++;
						if(report34.equals("01")){
							m404[0]++;
						}
						else if(report34.equals("02")){
							m404[1]++;
						}
						else if(report34.equals("03")){
							m404[2]++;
						}
						else if(report34.equals("04")){
							m404[3]++;
						}
					}
					else if(report24.equals("05")){
						m4[4]++;
						if(report34.equals("01")){
							m405[0]++;
						}
						else if(report34.equals("02")){
							m405[1]++;
						}
						else if(report34.equals("03")){
							m405[2]++;
						}
						else if(report34.equals("04")){
							m405[3]++;
						}
						else if(report34.equals("05")){
							m405[4]++;
						}
						else if(report34.equals("06")){
							m405[5]++;
						}
						else if(report34.equals("07")){
							m405[6]++;
						}
						else if(report34.equals("08")){
							m405[7]++;
						}
					}
					else if(report24.equals("06")){
						m4[5]++;
						if(report34.equals("01")){
							m406[0]++;
						}
						else if(report34.equals("02")){
							m406[1]++;
						}
						else if(report34.equals("03")){
							m406[2]++;
						}
						else if(report34.equals("04")){
							m406[3]++;
						}
						else if(report34.equals("05")){
							m406[4]++;
						}
						else if(report34.equals("06")){
							m406[5]++;
						}
						else if(report34.equals("07")){
							m406[6]++;
						}
						else if(report34.equals("08")){
							m406[7]++;
						}
					}
					else if(report24.equals("07")){
						m4[6]++;
						if(report34.equals("01")){
							m407[0]++;
						}
						else if(report34.equals("02")){
							m407[1]++;
						}
						else if(report34.equals("03")){
							m407[2]++;
						}
						else if(report34.equals("04")){
							m407[3]++;
						}
						 
					}
					else if(report24.equals("08")){
						m4[7]++;
						if(report34.equals("01")){
							m408[0]++;
						}
						else if(report34.equals("02")){
							m408[1]++;
						}
						else if(report34.equals("03")){
							m408[2]++;
						}
						else if(report34.equals("04")){
							m408[3]++;
						}
						else if(report34.equals("05")){
							m408[4]++;
						}
						else if(report34.equals("06")){
							m408[5]++;
						}
						else if(report34.equals("07")){
							m408[6]++;
						}
						 
					}
					else if(report24.equals("09")){
						m4[8]++;
						if(report34.equals("01")){
							m409[0]++;
						}
						else if(report34.equals("02")){
							m409[1]++;
						}
						else if(report34.equals("03")){
							m409[2]++;
						}
						else if(report34.equals("04")){
							m409[3]++;
						}
						else if(report34.equals("05")){
							m409[4]++;
						}
						else if(report34.equals("06")){
							m409[5]++;
						}
						else if(report34.equals("07")){
							m409[6]++;
						}
						else if(report34.equals("08")){
							m409[7]++;
						}
						else if(report34.equals("09")){
							m409[8]++;
						}
					}
					else if(report24.equals("10")){
						m4[9]++;
						if(report34.equals("01")){
							m410[0]++;
						}
						else if(report34.equals("02")){
							m410[1]++;
						}
					}
					else if(report24.equals("11")){
						m4[10]++;
						if(report34.equals("01")){
							m411[0]++;
						}
						else if(report34.equals("02")){
							m411[1]++;
						}
						else if(report34.equals("03")){
							m411[2]++;
						}
						else if(report34.equals("04")){
							m411[3]++;
						}
						else if(report34.equals("05")){
							m411[4]++;
						}
						else if(report34.equals("06")){
							m411[5]++;
						}
						else if(report34.equals("07")){
							m411[6]++;
						}
					}
					else if(report24.equals("12")){
						m4[11]++;
						if(report34.equals("01")){
							m412[0]++;
						}
						else if(report34.equals("02")){
							m412[1]++;
						}
						else if(report34.equals("03")){
							m412[2]++;
						}
						else if(report34.equals("04")){
							m412[3]++;
						}
						else if(report34.equals("05")){
							m412[4]++;
						}
						else if(report34.equals("06")){
							m412[5]++;
						}
						else if(report34.equals("07")){
							m412[6]++;
						}
						else if(report34.equals("08")){
							m412[7]++;
						}
						else if(report34.equals("09")){
							m412[8]++;
						}
					}
					else if(report24.equals("13")){
						m4[12]++;
						if(report34.equals("01")){
							m413[0]++;
						}
						else if(report34.equals("02")){
							m413[1]++;
						}
						else if(report34.equals("03")){
							m413[2]++;
						}
						else if(report34.equals("04")){
							m413[3]++;
						}
						else if(report34.equals("05")){
							m413[4]++;
						}
						else if(report34.equals("06")){
							m413[5]++;
						}
						 
					}
					else if(report24.equals("14")){
						m4[13]++;
						if(report34.equals("01")){
							m414[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt3.close();
		//end 1103
				//1104
			
			String sql1104="select treatment from dental_activity where visitdatetime between '"+b+"' and '"+f+"'  ";
			
			PreparedStatement stmt4 = conn1.prepareStatement(sql1104);
			ResultSet rs4 = stmt4.executeQuery();
		 
			while (rs4.next()){
				String report15="",report25="",report35="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs4.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report15=reportM[i][1].trim();
					}	 
				}
				if(report15.length()==4){
					 
					report25=report15.substring(0, 2);
					report35=report15.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report25.equals("01")){
						m5[0]++;
						if(report35.equals("01")){
							m501[0]++;
						}
						else if(report35.equals("02")){
							m501[1]++;
						}
						else if(report35.equals("03")){
							m501[2]++;
						}
					}
					else if(report25.equals("02")){
						m5[1]++;
						if(report35.equals("01")){
							m502[0]++;
						}
						else if(report35.equals("02")){
							m502[1]++;
						}
					}
					else if(report25.equals("03")){
						m5[2]++;
						if(report35.equals("01")){
							m503[0]++;
						}
						else if(report35.equals("02")){
							m503[1]++;
						}
						else if(report35.equals("03")){
							m503[2]++;
						}
						else if(report35.equals("04")){
							m503[3]++;
						}
						else if(report35.equals("05")){
							m503[4]++;
						}
						else if(report35.equals("06")){
							m503[5]++;
						}
					}
					else if(report25.equals("04")){
						m5[3]++;
						if(report35.equals("01")){
							m504[0]++;
						}
						else if(report35.equals("02")){
							m504[1]++;
						}
						else if(report35.equals("03")){
							m504[2]++;
						}
						else if(report35.equals("04")){
							m504[3]++;
						}
					}
					else if(report25.equals("05")){
						m5[4]++;
						if(report35.equals("01")){
							m505[0]++;
						}
						else if(report35.equals("02")){
							m505[1]++;
						}
						else if(report35.equals("03")){
							m505[2]++;
						}
						else if(report35.equals("04")){
							m505[3]++;
						}
						else if(report35.equals("05")){
							m505[4]++;
						}
						else if(report35.equals("06")){
							m505[5]++;
						}
						else if(report35.equals("07")){
							m505[6]++;
						}
						else if(report35.equals("08")){
							m505[7]++;
						}
					}
					else if(report25.equals("06")){
						m5[5]++;
						if(report35.equals("01")){
							m506[0]++;
						}
						else if(report35.equals("02")){
							m506[1]++;
						}
						else if(report35.equals("03")){
							m506[2]++;
						}
						else if(report35.equals("04")){
							m506[3]++;
						}
						else if(report35.equals("05")){
							m506[4]++;
						}
						else if(report35.equals("06")){
							m506[5]++;
						}
						else if(report35.equals("07")){
							m506[6]++;
						}
						else if(report35.equals("08")){
							m506[7]++;
						}
					}
					else if(report25.equals("07")){
						m5[6]++;
						if(report35.equals("01")){
							m507[0]++;
						}
						else if(report35.equals("02")){
							m507[1]++;
						}
						else if(report35.equals("03")){
							m507[2]++;
						}
						else if(report35.equals("04")){
							m507[3]++;
						}
						 
					}
					else if(report25.equals("08")){
						m5[7]++;
						if(report35.equals("01")){
							m508[0]++;
						}
						else if(report35.equals("02")){
							m508[1]++;
						}
						else if(report35.equals("03")){
							m508[2]++;
						}
						else if(report35.equals("04")){
							m508[3]++;
						}
						else if(report35.equals("05")){
							m508[4]++;
						}
						else if(report35.equals("06")){
							m508[5]++;
						}
						else if(report35.equals("07")){
							m508[6]++;
						}
						 
					}
					else if(report25.equals("09")){
						m5[8]++;
						if(report35.equals("01")){
							m509[0]++;
						}
						else if(report35.equals("02")){
							m509[1]++;
						}
						else if(report35.equals("03")){
							m509[2]++;
						}
						else if(report35.equals("04")){
							m509[3]++;
						}
						else if(report35.equals("05")){
							m509[4]++;
						}
						else if(report35.equals("06")){
							m509[5]++;
						}
						else if(report35.equals("07")){
							m509[6]++;
						}
						else if(report35.equals("08")){
							m509[7]++;
						}
						else if(report35.equals("09")){
							m509[8]++;
						}
					}
					else if(report25.equals("10")){
						m5[9]++;
						if(report35.equals("01")){
							m510[0]++;
						}
						else if(report35.equals("02")){
							m510[1]++;
						}
					}
					else if(report25.equals("11")){
						m5[10]++;
						if(report35.equals("01")){
							m511[0]++;
						}
						else if(report35.equals("02")){
							m511[1]++;
						}
						else if(report35.equals("03")){
							m511[2]++;
						}
						else if(report35.equals("04")){
							m511[3]++;
						}
						else if(report35.equals("05")){
							m511[4]++;
						}
						else if(report35.equals("06")){
							m511[5]++;
						}
						else if(report35.equals("07")){
							m511[6]++;
						}
					}
					else if(report25.equals("12")){
						m5[11]++;
						if(report35.equals("01")){
							m512[0]++;
						}
						else if(report35.equals("02")){
							m512[1]++;
						}
						else if(report35.equals("03")){
							m512[2]++;
						}
						else if(report35.equals("04")){
							m512[3]++;
						}
						else if(report35.equals("05")){
							m512[4]++;
						}
						else if(report35.equals("06")){
							m512[5]++;
						}
						else if(report35.equals("07")){
							m512[6]++;
						}
						else if(report35.equals("08")){
							m512[7]++;
						}
						else if(report35.equals("09")){
							m512[8]++;
						}
					}
					else if(report25.equals("13")){
						m5[12]++;
						if(report35.equals("01")){
							m513[0]++;
						}
						else if(report35.equals("02")){
							m513[1]++;
						}
						else if(report35.equals("03")){
							m513[2]++;
						}
						else if(report35.equals("04")){
							m513[3]++;
						}
						else if(report35.equals("05")){
							m513[4]++;
						}
						else if(report35.equals("06")){
							m513[5]++;
						}
						 
					}
					else if(report25.equals("14")){
						m5[13]++;
						if(report35.equals("01")){
							m514[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt4.close();
		//end 1104
			//1105
			String sql1105="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and  ((vnpres.suffix='1' and vnpres.clinic='1105') or (vnpres.suffix='2' and vnpres.clinic='1105') or (vnpres.suffix='3' and vnpres.clinic='1105'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt5 = conn.prepareStatement(sql1105);
			ResultSet rs5 = stmt5.executeQuery();
			int p5=1;
			while (rs5.next()){
				String report16="",report26="",report36="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs5.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report16=reportM[i][1].trim();
					}	 
				}
				if(report16.length()==4){
					 
					report26=report16.substring(0, 2);
					report36=report16.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report26.equals("01")){
						m6[0]++;
						if(report36.equals("01")){
							m601[0]++;
						}
						else if(report36.equals("02")){
							m601[1]++;
						}
						else if(report36.equals("03")){
							m601[2]++;
						}
					}
					else if(report26.equals("02")){
						m6[1]++;
						if(report36.equals("01")){
							m602[0]++;
						}
						else if(report36.equals("02")){
							m602[1]++;
						}
					}
					else if(report26.equals("03")){
						m6[2]++;
						if(report36.equals("01")){
							m603[0]++;
						}
						else if(report36.equals("02")){
							m603[1]++;
						}
						else if(report36.equals("03")){
							m603[2]++;
						}
						else if(report36.equals("04")){
							m603[3]++;
						}
						else if(report36.equals("05")){
							m603[4]++;
						}
						else if(report36.equals("06")){
							m603[5]++;
						}
					}
					else if(report26.equals("04")){
						m6[3]++;
						if(report36.equals("01")){
							m604[0]++;
						}
						else if(report36.equals("02")){
							m604[1]++;
						}
						else if(report36.equals("03")){
							m604[2]++;
						}
						else if(report36.equals("04")){
							m604[3]++;
						}
					}
					else if(report26.equals("05")){
						m6[4]++;
						if(report36.equals("01")){
							m605[0]++;
						}
						else if(report36.equals("02")){
							m605[1]++;
						}
						else if(report36.equals("03")){
							m605[2]++;
						}
						else if(report36.equals("04")){
							m605[3]++;
						}
						else if(report36.equals("05")){
							m605[4]++;
						}
						else if(report36.equals("06")){
							m605[5]++;
						}
						else if(report36.equals("07")){
							m605[6]++;
						}
						else if(report36.equals("08")){
							m605[7]++;
						}
					}
					else if(report26.equals("06")){
						m6[5]++;
						if(report36.equals("01")){
							m606[0]++;
						}
						else if(report36.equals("02")){
							m606[1]++;
						}
						else if(report36.equals("03")){
							m606[2]++;
						}
						else if(report36.equals("04")){
							m606[3]++;
						}
						else if(report36.equals("05")){
							m606[4]++;
						}
						else if(report36.equals("06")){
							m606[5]++;
						}
						else if(report36.equals("07")){
							m606[6]++;
						}
						else if(report36.equals("08")){
							m606[7]++;
						}
					}
					else if(report26.equals("07")){
						m6[6]++;
						if(report36.equals("01")){
							m607[0]++;
						}
						else if(report36.equals("02")){
							m607[1]++;
						}
						else if(report36.equals("03")){
							m607[2]++;
						}
						else if(report36.equals("04")){
							m607[3]++;
						}
						 
					}
					else if(report26.equals("08")){
						m6[7]++;
						if(report36.equals("01")){
							m608[0]++;
						}
						else if(report36.equals("02")){
							m608[1]++;
						}
						else if(report36.equals("03")){
							m608[2]++;
						}
						else if(report36.equals("04")){
							m608[3]++;
						}
						else if(report36.equals("05")){
							m608[4]++;
						}
						else if(report36.equals("06")){
							m608[5]++;
						}
						else if(report36.equals("07")){
							m608[6]++;
						}
						 
					}
					else if(report26.equals("09")){
						m6[8]++;
						if(report36.equals("01")){
							m609[0]++;
						}
						else if(report36.equals("02")){
							m609[1]++;
						}
						else if(report36.equals("03")){
							m609[2]++;
						}
						else if(report36.equals("04")){
							m609[3]++;
						}
						else if(report36.equals("05")){
							m609[4]++;
						}
						else if(report36.equals("06")){
							m609[5]++;
						}
						else if(report36.equals("07")){
							m609[6]++;
						}
						else if(report36.equals("08")){
							m609[7]++;
						}
						else if(report36.equals("09")){
							m609[8]++;
						}
					}
					else if(report26.equals("10")){
						m6[9]++;
						if(report36.equals("01")){
							m610[0]++;
						}
						else if(report36.equals("02")){
							m610[1]++;
						}
					}
					else if(report26.equals("11")){
						m6[10]++;
						if(report36.equals("01")){
							m611[0]++;
						}
						else if(report36.equals("02")){
							m611[1]++;
						}
						else if(report36.equals("03")){
							m611[2]++;
						}
						else if(report36.equals("04")){
							m611[3]++;
						}
						else if(report36.equals("05")){
							m611[4]++;
						}
						else if(report36.equals("06")){
							m611[5]++;
						}
						else if(report36.equals("07")){
							m611[6]++;
						}
					}
					else if(report26.equals("12")){
						m6[11]++;
						if(report36.equals("01")){
							m612[0]++;
						}
						else if(report36.equals("02")){
							m612[1]++;
						}
						else if(report36.equals("03")){
							m612[2]++;
						}
						else if(report36.equals("04")){
							m612[3]++;
						}
						else if(report36.equals("05")){
							m612[4]++;
						}
						else if(report36.equals("06")){
							m612[5]++;
						}
						else if(report36.equals("07")){
							m612[6]++;
						}
						else if(report36.equals("08")){
							m612[7]++;
						}
						else if(report36.equals("09")){
							m612[8]++;
						}
					}
					else if(report26.equals("13")){
						m6[12]++;
						if(report36.equals("01")){
							m613[0]++;
						}
						else if(report36.equals("02")){
							m613[1]++;
						}
						else if(report36.equals("03")){
							m613[2]++;
						}
						else if(report36.equals("04")){
							m613[3]++;
						}
						else if(report36.equals("05")){
							m613[4]++;
						}
						else if(report36.equals("06")){
							m613[5]++;
						}
						 
					}
					else if(report26.equals("14")){
						m6[13]++;
						if(report36.equals("01")){
							m614[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt5.close();
		//end 1105
			//1106
			//1106
			String sql1106="select dentaltreat.treatmentcode,dentaltreat.doctor,dentaltreat.vn,dentaltreat.visitdate from dentaltreat,vnpres where dentaltreat.vn=vnpres.vn and dentaltreat.visitdate=vnpres.visitdate and  ((vnpres.suffix='1' and vnpres.clinic='1106') or (vnpres.suffix='2' and vnpres.clinic='1106') or (vnpres.suffix='3' and vnpres.clinic='1106'))  and dentaltreat.visitdate between '"+b+"' and '"+f+"'"+doctor_search;
			
			PreparedStatement stmt6 = conn.prepareStatement(sql1106);
			ResultSet rs6 = stmt6.executeQuery();
			int p6=1;
			while (rs6.next()){
				String report17="",report27="",report37="";
		
				for(int i=0;i<reportM.length;i++){
					if(rs6.getString(1).trim().toUpperCase() .equals(reportM[i][0].trim().toUpperCase() )){
						report17=reportM[i][1].trim();
					}	 
				}
				if(report17.length()==4){
					 
					report27=report17.substring(0, 2);
					report37=report17.substring(2);
					//System.out.println(report1+"**"+report3);
					if(report27.equals("01")){
						m7[0]++;
						if(report37.equals("01")){
							m701[0]++;
						}
						else if(report37.equals("02")){
							m701[1]++;
						}
						else if(report37.equals("03")){
							m701[2]++;
						}
					}
					else if(report27.equals("02")){
						m7[1]++;
						if(report37.equals("01")){
							m702[0]++;
						}
						else if(report37.equals("02")){
							m702[1]++;
						}
					}
					else if(report27.equals("03")){
						m7[2]++;
						if(report37.equals("01")){
							m703[0]++;
						}
						else if(report37.equals("02")){
							m703[1]++;
						}
						else if(report37.equals("03")){
							m703[2]++;
						}
						else if(report37.equals("04")){
							m703[3]++;
						}
						else if(report37.equals("05")){
							m703[4]++;
						}
						else if(report37.equals("06")){
							m703[5]++;
						}
					}
					else if(report27.equals("04")){
						m7[3]++;
						if(report37.equals("01")){
							m704[0]++;
						}
						else if(report37.equals("02")){
							m704[1]++;
						}
						else if(report37.equals("03")){
							m704[2]++;
						}
						else if(report37.equals("04")){
							m704[3]++;
						}
					}
					else if(report27.equals("05")){
						m7[4]++;
						if(report37.equals("01")){
							m705[0]++;
						}
						else if(report37.equals("02")){
							m705[1]++;
						}
						else if(report37.equals("03")){
							m705[2]++;
						}
						else if(report37.equals("04")){
							m705[3]++;
						}
						else if(report37.equals("05")){
							m705[4]++;
						}
						else if(report37.equals("06")){
							m705[5]++;
						}
						else if(report37.equals("07")){
							m705[6]++;
						}
						else if(report37.equals("08")){
							m705[7]++;
						}
					}
					else if(report27.equals("06")){
						m7[5]++;
						if(report37.equals("01")){
							m706[0]++;
						}
						else if(report37.equals("02")){
							m706[1]++;
						}
						else if(report37.equals("03")){
							m706[2]++;
						}
						else if(report37.equals("04")){
							m706[3]++;
						}
						else if(report37.equals("05")){
							m706[4]++;
						}
						else if(report37.equals("06")){
							m706[5]++;
						}
						else if(report37.equals("07")){
							m706[6]++;
						}
						else if(report37.equals("08")){
							m706[7]++;
						}
					}
					else if(report27.equals("07")){
						m7[6]++;
						if(report37.equals("01")){
							m707[0]++;
						}
						else if(report37.equals("02")){
							m707[1]++;
						}
						else if(report37.equals("03")){
							m707[2]++;
						}
						else if(report37.equals("04")){
							m707[3]++;
						}
						 
					}
					else if(report27.equals("08")){
						m7[7]++;
						if(report37.equals("01")){
							m708[0]++;
						}
						else if(report37.equals("02")){
							m708[1]++;
						}
						else if(report37.equals("03")){
							m708[2]++;
						}
						else if(report37.equals("04")){
							m708[3]++;
						}
						else if(report37.equals("05")){
							m708[4]++;
						}
						else if(report37.equals("06")){
							m708[5]++;
						}
						else if(report37.equals("07")){
							m708[6]++;
						}
						 
					}
					else if(report27.equals("09")){
						m7[8]++;
						if(report37.equals("01")){
							m709[0]++;
						}
						else if(report37.equals("02")){
							m709[1]++;
						}
						else if(report37.equals("03")){
							m709[2]++;
						}
						else if(report37.equals("04")){
							m709[3]++;
						}
						else if(report37.equals("05")){
							m709[4]++;
						}
						else if(report37.equals("06")){
							m709[5]++;
						}
						else if(report37.equals("07")){
							m709[6]++;
						}
						else if(report37.equals("08")){
							m709[7]++;
						}
						else if(report37.equals("09")){
							m709[8]++;
						}
					}
					else if(report27.equals("10")){
						m7[9]++;
						if(report37.equals("01")){
							m710[0]++;
						}
						else if(report37.equals("02")){
							m710[1]++;
						}
					}
					else if(report27.equals("11")){
						m7[10]++;
						if(report37.equals("01")){
							m711[0]++;
						}
						else if(report37.equals("02")){
							m711[1]++;
						}
						else if(report37.equals("03")){
							m711[2]++;
						}
						else if(report37.equals("04")){
							m711[3]++;
						}
						else if(report37.equals("05")){
							m711[4]++;
						}
						else if(report37.equals("06")){
							m711[5]++;
						}
						else if(report37.equals("07")){
							m711[6]++;
						}
					}
					else if(report27.equals("12")){
						m7[11]++;
						if(report37.equals("01")){
							m712[0]++;
						}
						else if(report37.equals("02")){
							m712[1]++;
						}
						else if(report37.equals("03")){
							m712[2]++;
						}
						else if(report37.equals("04")){
							m712[3]++;
						}
						else if(report37.equals("05")){
							m712[4]++;
						}
						else if(report37.equals("06")){
							m712[5]++;
						}
						else if(report37.equals("07")){
							m712[6]++;
						}
						else if(report37.equals("08")){
							m712[7]++;
						}
						else if(report37.equals("09")){
							m712[8]++;
						}
					}
					else if(report27.equals("13")){
						m7[12]++;
						if(report37.equals("01")){
							m713[0]++;
						}
						else if(report37.equals("02")){
							m713[1]++;
						}
						else if(report37.equals("03")){
							m713[2]++;
						}
						else if(report37.equals("04")){
							m713[3]++;
						}
						else if(report37.equals("05")){
							m713[4]++;
						}
						else if(report37.equals("06")){
							m713[5]++;
						}
						 
					}
					else if(report27.equals("14")){
						m7[13]++;
						if(report37.equals("01")){
							m714[0]++;
						}
					}
				
				
				}
				 //System.out.println(i+1+". "+depart[i][0]);
				//System.out.println("Result: "+p+"::"+rs.getString(2)+"**"+report2+":"+rs.getString(1)+"::"+cat+"**"+subcat);
				
			}
			stmt6.close();
		//end 1106
		
		
			conn.close();
			conn1.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			for(int i=0;i<m8.length;i++){
				m8[i]=m1[i]+m2[i]+m3[i]+m4[i]+m5[i]+m6[i]+m7[i]+m8[i];
			}
		    for(int i=0;i<m801.length;i++){
		    	m801[i]=m101[i]+m201[i]+m301[i]+m401[i]+m501[i]+m601[i]+m701[i];
		    }
		    for(int i=0;i<m802.length;i++){
		    	m802[i]=m102[i]+m202[i]+m302[i]+m402[i]+m502[i]+m602[i]+m702[i];
		    }
		    for(int i=0;i<m803.length;i++){
		    	m803[i]=m103[i]+m203[i]+m303[i]+m403[i]+m503[i]+m603[i]+m703[i];
		    }
		    for(int i=0;i<m804.length;i++){
		    	m804[i]=m104[i]+m204[i]+m304[i]+m404[i]+m504[i]+m604[i]+m704[i];
		    }
		    for(int i=0;i<m805.length;i++){
		    	m805[i]=m105[i]+m205[i]+m305[i]+m405[i]+m505[i]+m605[i]+m705[i];
		    }
		    for(int i=0;i<m806.length;i++){
		    	m806[i]=m106[i]+m206[i]+m306[i]+m406[i]+m506[i]+m606[i]+m706[i];
		    }
		    for(int i=0;i<m807.length;i++){
		    	m807[i]=m107[i]+m207[i]+m307[i]+m407[i]+m507[i]+m607[i]+m707[i];
		    }
		    for(int i=0;i<m808.length;i++){
		    	m808[i]=m108[i]+m208[i]+m308[i]+m408[i]+m508[i]+m608[i]+m708[i];
		    }
		    for(int i=0;i<m809.length;i++){
		    	m809[i]=m109[i]+m209[i]+m309[i]+m409[i]+m509[i]+m609[i]+m709[i];
		    }
		    for(int i=0;i<m810.length;i++){
		    	m810[i]=m110[i]+m210[i]+m310[i]+m410[i]+m510[i]+m610[i]+m710[i];
		    }
		    for(int i=0;i<m811.length;i++){
		    	m811[i]=m111[i]+m211[i]+m311[i]+m411[i]+m511[i]+m611[i]+m711[i];
		    }
		    for(int i=0;i<m812.length;i++){
		    	m812[i]=m112[i]+m212[i]+m312[i]+m412[i]+m512[i]+m612[i]+m712[i];
		    }
		    for(int i=0;i<m813.length;i++){
		    	m813[i]=m113[i]+m213[i]+m313[i]+m413[i]+m513[i]+m613[i]+m713[i];
		    }
		    for(int i=0;i<m814.length;i++){
		    	m814[i]=m114[i]+m214[i]+m314[i]+m414[i]+m514[i]+m614[i]+m714[i];
		    }
		  
		    Workbook wb = new HSSFWorkbook();
			Sheet personSheet = wb.createSheet("All");
			Row headerRow = personSheet.createRow(0);
			Cell nameHeaderCell = headerRow.createCell(0);
			nameHeaderCell.setCellValue(("กิจกรรม"));
			Cell addressHeaderCell = headerRow.createCell(1);
			addressHeaderCell.setCellValue(("ทันตกรรมในเวลา"));
			Cell addressHeaderCell1 = headerRow.createCell(2);
			addressHeaderCell1.setCellValue(("ทันตกรรมนอกเวลา"));
			Cell addressHeaderCell2 = headerRow.createCell(3);
			addressHeaderCell2.setCellValue(("ทันตกรรม ANC"));
			Cell addressHeaderCell3 = headerRow.createCell(4);
			addressHeaderCell3.setCellValue(("ทันตกรรมสุขภาพเด็กดี"));
			Cell addressHeaderCell4 = headerRow.createCell(5);
			addressHeaderCell4.setCellValue(("ทันตกรรมเคลื่อนที่"));
			Cell addressHeaderCell5 = headerRow.createCell(6);
			addressHeaderCell5.setCellValue(("ทันตกรรม อต.1"));
			Cell addressHeaderCell6 = headerRow.createCell(7);
			addressHeaderCell6.setCellValue(("ทันตกรรม อต.2"));
			Cell addressHeaderCell7 = headerRow.createCell(8);
			addressHeaderCell7.setCellValue(("รวม"));
			
		    int row=1;
			for(int i=0;i<r1.length;i++){
				 
				//System.out.println(r1[i]);
				//System.out.println(r1[i]+","+m1[i]+","+m2[i]+","+m3[i]+","+m4[i]+","+m5[i]+","+m6[i]+","+m7[i]+","+m8[i]);
			
				if(i==0){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r01.length;j++){
						
						 
						//System.out.println(m[i]);
						//System.out.println(r01[j]+","+m101[j]+","+m201[j]+","+m301[j]+","+m401[j]+","+m501[j]+","+m601[j]+","+m701[j]+","+m801[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r01[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m101[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m201[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m301[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m401[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m501[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m601[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m701[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m801[j]));
					    
						row = row + 1;
					}
				}
				if(i==1){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r02.length;j++){
					
						//System.out.println(m[i]);
						//System.out.println(r02[j]+","+m102[j]+","+m202[j]+","+m302[j]+","+m402[j]+","+m502[j]+","+m602[j]+","+m702[j]+","+m802[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r02[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m102[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m202[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m302[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m402[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m502[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m602[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m702[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m802[j]));
						row = row + 1;
					}
				}
				if(i==2){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r03.length;j++){
				
						//System.out.println(m[i]);
						//System.out.println(r03[j]+","+m103[j]+","+m203[j]+","+m303[j]+","+m403[j]+","+m503[j]+","+m603[j]+","+m703[j]+","+m803[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r03[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m103[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m203[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m303[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m403[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m503[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m603[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m703[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m803[j]));
						row = row + 1;
					}
				}
				if(i==3){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r04.length;j++){
					
						//System.out.println(m[i]);
						//System.out.println(r04[j]+","+m104[j]+","+m204[j]+","+m304[j]+","+m404[j]+","+m504[j]+","+m604[j]+","+m704[j]+","+m804[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r04[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m104[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m204[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m304[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m404[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m504[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m604[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m704[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m804[j]));
						row = row + 1;
					}
				}if(i==4){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r05.length;j++){
					
						//System.out.println(m[i]);
						//System.out.println(r05[j]+","+m105[j]+","+m205[j]+","+m305[j]+","+m405[j]+","+m505[j]+","+m605[j]+","+m705[j]+","+m805[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r05[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m105[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m205[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m305[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m405[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m505[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m605[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m705[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m805[j]));
						row = row + 1;
					}
				}
				if(i==5){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r06.length;j++){
					
						//System.out.println(m[i]);
						//System.out.println(r06[j]+","+m106[j]+","+m206[j]+","+m306[j]+","+m406[j]+","+m506[j]+","+m606[j]+","+m706[j]+","+m806[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r06[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m106[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m206[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m306[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m406[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m506[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m606[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m706[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m806[j]));
						row = row + 1;
					}
				}
				if(i==6){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r07.length;j++){
				
						//System.out.println(m[i]);
						//System.out.println(r07[j]+","+m107[j]+","+m207[j]+","+m307[j]+","+m407[j]+","+m507[j]+","+m607[j]+","+m707[j]+","+m807[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r07[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m107[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m207[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m307[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m407[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m507[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m607[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m707[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m807[j]));
						row = row + 1;
					}
				}
				if(i==7){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r08.length;j++){
				
						//System.out.println(m[i]);
						//System.out.println(r08[j]+","+m108[j]+","+m208[j]+","+m308[j]+","+m408[j]+","+m508[j]+","+m608[j]+","+m708[j]+","+m808[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r08[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m108[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m208[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m308[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m408[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m508[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m608[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m708[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m808[j]));
						row = row + 1;
					}
				}
				if(i==8){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r09.length;j++){
						
						//System.out.println(m[i]);
						//System.out.println(r09[j]+","+m109[j]+","+m209[j]+","+m309[j]+","+m409[j]+","+m509[j]+","+m609[j]+","+m709[j]+","+m809[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r09[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m109[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m209[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m309[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m409[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m509[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m609[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m709[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m809[j]));
						row = row + 1;
					}
				}
				if(i==9){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r10.length;j++){
					
						//System.out.println(m[i]);
						//System.out.println(r10[j]+","+m110[j]+","+m210[j]+","+m310[j]+","+m410[j]+","+m510[j]+","+m610[j]+","+m710[j]+","+m810[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r10[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m110[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m210[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m310[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m410[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m510[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m610[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m710[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m810[j]));
						row = row + 1;
					}
				}
				if(i==10){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r11.length;j++){
						
						//System.out.println(m[i]);
						//System.out.println(r11[j]+","+m111[j]+","+m211[j]+","+m311[j]+","+m411[j]+","+m511[j]+","+m611[j]+","+m711[j]+","+m811[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r11[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m111[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m211[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m311[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m411[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m511[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m611[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m711[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m811[j]));
						row = row + 1;
					}
				}
				if(i==11){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r12.length;j++){
						
						//System.out.println(m[i]);
						//System.out.println(r12[j]+","+m112[j]+","+m212[j]+","+m312[j]+","+m412[j]+","+m512[j]+","+m612[j]+","+m712[j]+","+m812[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r12[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m112[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m212[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m312[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m412[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m512[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m612[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m712[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m812[j]));
						row = row + 1;
					}
				}
				if(i==12){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r13.length;j++){
						
						//System.out.println(m[i]);
						///System.out.println(r13[j]+","+m113[j]+","+m213[j]+","+m313[j]+","+m413[j]+","+m513[j]+","+m613[j]+","+m713[j]+","+m813[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r13[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m113[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m213[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m313[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m413[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m513[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m613[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m713[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m813[j]));
						row = row + 1;
					}
				}
				if(i==13){
					Row dataRow = personSheet.createRow(row);
				    Cell dataNameCell = dataRow.createCell(0);
				    dataNameCell.setCellValue((r1[i]));

				    Cell dataAddressCell11 = dataRow.createCell(1);
				    dataAddressCell11.setCellValue((m1[i]));
				    Cell dataAddressCell12 = dataRow.createCell(2);
				    dataAddressCell12.setCellValue((m2[i]));
				    Cell dataAddressCell13 = dataRow.createCell(3);
				    dataAddressCell13.setCellValue((m3[i]));
				    Cell dataAddressCell14 = dataRow.createCell(4);
				    dataAddressCell14.setCellValue((m4[i]));
				    Cell dataAddressCell15 = dataRow.createCell(5);
				    dataAddressCell15.setCellValue((m5[i]));
				    Cell dataAddressCell16 = dataRow.createCell(6);
				    dataAddressCell16.setCellValue((m6[i]));
				    Cell dataAddressCell17 = dataRow.createCell(7);
				    dataAddressCell17.setCellValue((m7[i]));
				    Cell dataAddressCell18 = dataRow.createCell(8);
				    dataAddressCell18.setCellValue((m8[i]));
					row = row + 1;
					for(int j=0;j<r14.length;j++){
						
						//System.out.println(r14[j]+","+m114[j]+","+m214[j]+","+m314[j]+","+m414[j]+","+m514[j]+","+m614[j]+","+m714[j]+","+m814[j]);
						Row dataRow1 = personSheet.createRow(row); 
						Cell dataNameCell1 = dataRow1.createCell(0);
					    dataNameCell1.setCellValue((r14[j]));

					    Cell dataAddressCell1 = dataRow1.createCell(1);
					    dataAddressCell1.setCellValue((m114[j]));
					    Cell dataAddressCell2 = dataRow1.createCell(2);
					    dataAddressCell2.setCellValue((m214[j]));
					    Cell dataAddressCell3 = dataRow1.createCell(3);
					    dataAddressCell3.setCellValue((m314[j]));
					    Cell dataAddressCell4 = dataRow1.createCell(4);
					    dataAddressCell4.setCellValue((m414[j]));
					    Cell dataAddressCell5 = dataRow1.createCell(5);
					    dataAddressCell5.setCellValue((m514[j]));
					    Cell dataAddressCell6 = dataRow1.createCell(6);
					    dataAddressCell6.setCellValue((m614[j]));
					    Cell dataAddressCell7 = dataRow1.createCell(7);
					    dataAddressCell7.setCellValue((m714[j]));
					    Cell dataAddressCell8 = dataRow1.createCell(8);
					    dataAddressCell8.setCellValue((m814[j]));
						row = row + 1;
					}
				}
				 
				 
			}

		
		String outputDirPath = "D:/Dental_report.xls";
		FileOutputStream fileOut;
		JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
        	try {
    			
    			fileOut = new FileOutputStream(chooser.getSelectedFile()+".xls");
    			wb.write(fileOut);
    			fileOut.close();
    			
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
		 
		 
		
		
	}

}

