package org.utt.app.opd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Vector;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.utt.app.InApp;
import org.utt.app.PharFrame;
import org.utt.app.opd.ObjectOPD;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.DateLabelFormatter;
import org.utt.app.util.Setup;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPrintPage;

public class PharOPDPanel extends JPanel  implements  Observer{
	ObjectOPD oUserInfo;
	Dimension screen;

	JPanel leftPanel,mainPanel,topLeftPanel,topMainPanel,midMainPanel,mid2,mid1,midPanel;
	JLabel Label_Date,Label_Info;
	JRadioButton PtStatus1,PtStatus2,PtStatus3,PtStatus4,PtStatus5;
	String ptStatus="";
	ButtonGroup PtGroup = new ButtonGroup();
	
	JPanel pt_info,jPanel;
	JDatePickerImpl picker;
	String day="",month="",dateSearch="";
	
	Vector<String> columnNames;
	JScrollPane scrollPane,scrollPaneImg;
	JTable table;
	Vector<Vector<String>> data;
	JPanel bottomPanel;
	JButton ButtonRefresh;
	
	JLabel Label_VN;
	JTextField TextField_VN;
	JLabel Label_HN;
	JTextField TextField_HN;
	JLabel Label_CID;
	JTextField TextField_CID;
	JLabel Label_NAME;
	JLabel Label_Memo,Label_Extra,Label_Extra1;
	JTextArea textArea;
	
	JLabel Label_IMG;
	String fn="";
	/**
	 * Create the panel.
	 */
	public PharOPDPanel(ObjectOPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width, screen.height-175));
		
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension((screen.width*2)/10, screen.height-175));
		leftPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));
		
		 
		
		topLeftPanel = new JPanel();
		topLeftPanel.setLayout(null);
		topLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, 90));
		leftPanel.add(topLeftPanel, BorderLayout.NORTH);
		
		Label_Date= new JLabel("",JLabel.CENTER);
		
		JLabel LabelListdata = new JLabel("รายชื่อผู้รับบริการ",JLabel.CENTER);
		LabelListdata.setFont(new Font("Tahoma", Font.PLAIN, 13));
		LabelListdata.setBounds(80,60,100,20);
		topLeftPanel.add(LabelListdata);
		
		PtStatus1 = new JRadioButton("All");
		PtStatus1.setBackground(Setup.getColor());
		PtStatus1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus="";
			}
		});
		PtGroup.add(PtStatus1);
		PtStatus1.setSelected(true);
		PtStatus1.setBounds(5, 35, 50, 25);
		topLeftPanel.add(PtStatus1);
		
		PtStatus2 = new JRadioButton("UCS");
		PtStatus2.setBackground(Setup.getColor());
		PtStatus2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus=" and vnpres.rightcode not in('2009','2010','2019') and  vnpres.rightcode not like '6%' ";
			}
		});
		PtGroup.add(PtStatus2);
		//PtStatus2.setSelected(true);
		PtStatus2.setBounds(55, 35, 50, 25);
		topLeftPanel.add(PtStatus2);
		
		PtStatus3 = new JRadioButton("OFC");
		PtStatus3.setBackground(Setup.getColor());
		PtStatus3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus="  and vnpres.rightcode in ('2009','2010') ";
				 
			}
		});
		PtGroup.add(PtStatus3);
		 
		PtStatus3.setBounds(105, 35, 50, 25);
		topLeftPanel.add(PtStatus3);
		
		PtStatus4 = new JRadioButton("LGO");
		PtStatus4.setBackground(Setup.getColor());
		PtStatus4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus="  and vnpres.rightcode = '2019' ";
			}
		});
		PtGroup.add(PtStatus4);
		//PtStatus4.setSelected(true);
		PtStatus4.setBounds(160, 35, 50, 25);
		topLeftPanel.add(PtStatus4);
		
		PtStatus5 = new JRadioButton("SSS");
		PtStatus5.setBackground(Setup.getColor());
		PtStatus5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus=" and vnpres.rightcode like '6%' ";
			}
		});
		PtGroup.add(PtStatus5);
		//PtStatus5.setSelected(true);
		PtStatus5.setBounds(210, 35, 50, 25);
		topLeftPanel.add(PtStatus5);
		
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(screen.width-(screen.width*2)/10, screen.height-175));
		mainPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		topMainPanel = new JPanel();
		topMainPanel.setLayout(new BorderLayout(0, 0));
		topMainPanel.setBackground(Setup.getColor());
		topMainPanel.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), 140));
		mainPanel.add(topMainPanel, BorderLayout.NORTH);
		
		midPanel = new JPanel();
		midPanel.setLayout(new BorderLayout(0, 0));
		midPanel.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), screen.height-320));
		mainPanel.add(midPanel, BorderLayout.CENTER);
		
		pt_info = new JPanel();		 
		pt_info.setLayout(null);
		pt_info.setBackground(Setup.getColor());
		topMainPanel.add(pt_info, BorderLayout.CENTER);
		
		JLabel Label_Date = new JLabel("วันที่");
		Label_Date.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Date.setBounds(5,12,30,20);
		pt_info.add(Label_Date);
		
		UtilDateModel model = new UtilDateModel();
		model.setSelected(true);
		Properties p = new Properties();
		p.put("text.today", "");
		p.put("text.month", "");
		p.put("text.year", "");
		JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
		picker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		picker.setBackground(Setup.getColor());
		picker.getJFormattedTextField().setBackground(Setup.getColor());
		picker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(picker.getModel().getDay()<10){
					day="0"+picker.getModel().getDay();
				}else{
					day=""+picker.getModel().getDay();
				}
				if(picker.getModel().getMonth()+1<10){
					month="0"+(picker.getModel().getMonth()+1);
				}else{
					month=""+(picker.getModel().getMonth()+1);
				}
				dateSearch=picker.getModel().getYear()+"-"+month+"-"+day;
				oUserInfo.setPtVisitdate(dateSearch.trim());
				getClearData();
			}
		});
		picker.setShowYearButtons(true);
		picker.setTextEditable(false);
		 
		jPanel = new JPanel();
		jPanel.setBounds(30, 4, 220, 30);
		jPanel.setBackground(Setup.getColor());
		jPanel.add((JComponent)picker);
		pt_info.add(jPanel);
		
		if(picker.getModel().getDay()<10){
			day="0"+picker.getModel().getDay();
		}else{
			day=""+picker.getModel().getDay();
		}
		if(picker.getModel().getMonth()+1<10){
			month="0"+(picker.getModel().getMonth()+1);
		}else{
			month=""+(picker.getModel().getMonth()+1);
		}			
		dateSearch=picker.getModel().getYear()+"-"+month+"-"+day;
		oUserInfo.setPtVisitdate(dateSearch.trim());
		Label_VN = new JLabel("VN");
		Label_VN.setFont(new Font("Tahoma", Font.PLAIN, 13));
		pt_info.add(Label_VN);
		Label_VN.setBounds(270, 12, 20, 14);
		
		TextField_VN = new JTextField();
		//TextField_VN.setEditable(false);
		pt_info.add(TextField_VN);
		TextField_VN.setBounds(290, 10, 80, 20);
		TextField_VN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Label_IMG.setIcon(null);
				//clearValue();
				if(TextField_VN.getText().trim().length()==2){
					oUserInfo.setPtVN("0"+TextField_VN.getText());
				}else if(TextField_VN.getText().trim().length()==1){
					oUserInfo.setPtVN("00"+TextField_VN.getText());
					
				}else{
					oUserInfo.setPtVN(TextField_VN.getText().trim());
				}
				
				//System.out.println("a  "+TextField_VN.getText().trim()+"--"+oUserInfo.GetPtVN());
				oUserInfo.setPtHN("");
				oUserInfo.setPtCID("");
				
				oUserInfo.setPtName("");
				oUserInfo.setRightCode("");        
		        oUserInfo.setPtCliniccode("");         
		        oUserInfo.setPtAge("");
		        oUserInfo.setPtLabel("");              
		        oUserInfo.setMemo("");
		        oUserInfo.setMemo1("");
				searchPtvn(oUserInfo.GetPtVN());
				Label_IMG.setIcon(null);
				getClearData();
				
				String date_in=oUserInfo.GetPtVisitdate().trim();
		        String y=date_in.substring(0, 4);
		        String m=date_in.substring(5,7);
		        String d=date_in.substring(8);

		    	//Label_Info.setText(d+":"+m+":"+y+"  รหัสห้องตรวจ:  "+c+"  หน้าที่ : "+p);
		        //Label_Info.setText("Hn "+hn+" . "+oUserInfo.GetPtVisitdate()+"-"+clinic_pt+"**"+y+"-"+m+"-"+d);
		        //System.out.println("hn..."+oUserInfo.GetPtHN());
		        
		        //////////////////////////////////////
		        
		    	String res=getFn(oUserInfo.GetPtHN(),y+m+d).trim();
		    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
		    	if(res.substring(0, res.indexOf(":")).equals("1")){
		    		//oUserInfo.setFN(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
		    		showFilePDF(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
		    		//System.out.println("1111"+res.substring(0, res.indexOf(":")));
		    	}
		    	else if(res.substring(0, res.indexOf(":")).equals("0")){
					JOptionPane.showMessageDialog(null,"ไม่พบ file ของ HN:"+oUserInfo.GetPtHN(),"File Error",JOptionPane.ERROR_MESSAGE);
					
		    	}
		    	else{
		    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
		    		String text=res.substring(res.indexOf(":")+1);
		    		String[] parts = text.split("-");
		    		//String fn_[] = new String[npage];
		    		 

		    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกไฟล์", "FN", JOptionPane.QUESTION_MESSAGE,
		    	        null, parts, "");
		    		//oUserInfo.setFN(selected);
		    		showFilePDF(selected);
		    		//System.out.println(selected);
		    	}
		    	/*
		    	String fn1=res.substring(res.indexOf(":")+1,(res.indexOf("-")));
		    	if(fn1.indexOf("]")>0) {
					fn=fn1.trim().substring(fn1.indexOf("]")+1);
					
				}else {
				
				}
				*/
		    	//System.out.println(fn);
		    	////////////////////////////////
			}
		});
		
		Label_HN = new JLabel("HN");
		Label_HN.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_HN.setBounds(390, 12, 20, 14);
		pt_info.add(Label_HN);
		
		TextField_HN = new JTextField();
		//TextField_HN.setEditable(false);
		TextField_HN.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TextField_HN.setText("");
		TextField_HN.setBounds(410, 10, 80, 20);
		pt_info.add(TextField_HN);
		TextField_HN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Label_IMG.setIcon(null);
				//clearValue();
				if(!Setup.checkEmptyTextField(TextField_HN) && TextField_HN.getText().trim().length()==7){
					
					oUserInfo.setPtHN(TextField_HN.getText().trim());
					//System.out.println("bb  "+TextField_HN.getText().trim()+"--"+oUserInfo.GetPtHN());
				}
				 
				
				oUserInfo.setPtVN("");
				oUserInfo.setPtCID("");
				
				oUserInfo.setPtName("");
				oUserInfo.setRightCode("");        
		        oUserInfo.setPtCliniccode("");         
		        oUserInfo.setPtAge("");
		        oUserInfo.setPtLabel("");              
		        oUserInfo.setMemo("");
		        oUserInfo.setMemo1("");
				searchPthn(oUserInfo.GetPtHN());
				Label_IMG.setIcon(null);
				getClearData();
				
				String date_in=oUserInfo.GetPtVisitdate().trim();
		        String y=date_in.substring(0, 4);
		        String m=date_in.substring(5,7);
		        String d=date_in.substring(8);

		    	//Label_Info.setText(d+":"+m+":"+y+"  รหัสห้องตรวจ:  "+c+"  หน้าที่ : "+p);
		        //Label_Info.setText("Hn "+hn+" . "+oUserInfo.GetPtVisitdate()+"-"+clinic_pt+"**"+y+"-"+m+"-"+d);
		        //System.out.println(fn);
		        
		    	String res=getFn(oUserInfo.GetPtHN(),y+m+d).trim();
		    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
		    	if(res.substring(0, res.indexOf(":")).equals("1")){
		    		//oUserInfo.setFN(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
		    		showFilePDF(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
		    		//System.out.println("1111"+res.substring(0, res.indexOf(":")));
		    	}
		    	else if(res.substring(0, res.indexOf(":")).equals("0")){
					JOptionPane.showMessageDialog(null,"ไม่พบ file ของ HN:"+oUserInfo.GetPtHN(),"File Error",JOptionPane.ERROR_MESSAGE);
					
		    	}
		    	else{
		    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
		    		String text=res.substring(res.indexOf(":")+1);
		    		String[] parts = text.split("-");
		    		//String fn_[] = new String[npage];
		    		 

		    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกไฟล์", "FN", JOptionPane.QUESTION_MESSAGE,
		    	        null, parts, "");
		    		//oUserInfo.setFN(selected);
		    		showFilePDF(selected);
		    		//System.out.println(selected);
		    	}
				
			}
		});
		
		Label_CID = new JLabel("ID");
		Label_CID.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_CID.setBounds(510, 12, 20, 14);
		pt_info.add(Label_CID);
		
		TextField_CID = new JTextField();
		TextField_CID.setEditable(false);
		 
		 
		TextField_CID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TextField_CID.setBounds(530, 10, 150, 20);
		pt_info.add(TextField_CID);
		TextField_CID.setColumns(10);
		
		Label_NAME = new JLabel(" ชื่อ  ");
		Label_NAME.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_NAME.setBounds(4, 30, (screen.width-((screen.width*2)/10)-40), 30);
		pt_info.add(Label_NAME);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setForeground(new Color(255, 0, 0));
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textArea.setBackground(Setup.getColor());
		//textArea.setBackground(new Color(0, 0, 0));
		textArea.setBounds(70, 70, (screen.width-((screen.width*2)/10)-200), 40);
		pt_info.add(textArea);
		
		Label_Memo = new JLabel("การแพ้ยา ");
		Label_Memo.setForeground(new Color(255, 0, 0));
		Label_Memo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Memo.setBounds(10, 65, 55, 30);
		pt_info.add(Label_Memo);
		
		Label_Extra = new JLabel("ข้อมูลเพิ่มเติม");
		//Label_Extra.setForeground(new Color(255, 0, 0));
		Label_Extra.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Setup.SetUnderline(Label_Extra);
		Label_Extra.setBounds(10, 110, 150, 30);
		pt_info.add(Label_Extra);
		
		Label_Extra1 = new JLabel("");
		//Label_Extra.setForeground(new Color(255, 0, 0));
		Label_Extra1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Setup.SetUnderline(Label_Extra1);
		Label_Extra1.setBounds(150, 110, 400, 30);
		pt_info.add(Label_Extra1);
		
		setLeftData();
		setMainData();
		add(mainPanel, BorderLayout.CENTER);
	}

	public void update(Observable oObservable, Object oObject) {
		oUserInfo = ((ObjectOPD)oObservable); // cast
		Label_Date.setText("วันที่ "+Setup.ShowThaiDate(oUserInfo.GetPtVisitdate()));
		TextField_HN.setText(oUserInfo.GetPtHN());
		TextField_CID.setText(oUserInfo.GetPtCID());
		TextField_VN.setText(oUserInfo.GetPtVN());
		Label_NAME.setText(oUserInfo.GetPtLabel());
		
		textArea.setText(oUserInfo.GetMemo());
		Label_Extra1.setText(oUserInfo.GetMemo1());
	
	}
	public void setLeftData(){
		 
		Label_Date.setText("วันที่ "+Setup.ShowThaiDate(oUserInfo.GetPtVisitdate()));
		Label_Date.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Label_Date.setBounds(5,5,(screen.width*2)/10,30);
		topLeftPanel.add(Label_Date);
		
		columnNames = new Vector<String>();
  		columnNames.add("ที่");
		columnNames.add("  VN ");
		columnNames.add("   รายชื่อผู้รับบริการ  ");
		for (int n = 3; n < 10; n++) {	
			columnNames.add(" ");
		}
	
		data = new Vector<Vector<String>>();
		 
		DefaultTableModel model = new DefaultTableModel(data, columnNames){
			public Class getColumnClass(int column){
				return getValueAt(0, column).getClass();
			}
		};
		table = new JTable(model){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
				Component c = super.prepareRenderer(renderer, row, column);	
				JLabel jc = (JLabel) c;
				
				if (!isRowSelected(row)){
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					 
					String type = (String)getModel().getValueAt(modelRow, 5);
					 
					String status=type.trim();					  
					if ("1".equals(status)){
						c.setBackground(new Color(206,203,208));					 
		 			}
					 
				}
				
				return c;
			}
		};
	
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
		        int col = table.columnAtPoint(e.getPoint());
		        
		        clearValue();
		        Label_IMG.setIcon(null);
		        String select_vn=table.getValueAt(row,1).toString().trim();
		        oUserInfo.setPtVN(select_vn);
		        String name=table.getValueAt(row,2).toString().trim();
		        oUserInfo.setPtName(name);
		        String hn=table.getValueAt(row,3).toString().trim();
		        oUserInfo.setPtHN(hn);
		        String clinic_pt=table.getValueAt(row,4).toString().trim();
		        oUserInfo.setPtCliniccode(clinic_pt);
		        String inscl="";
		        //System.out.println(select_vn+"*"+hn+"*"+name+"*"+clinic_pt);
		        searchVN(select_vn,hn,clinic_pt);
		        
		        oUserInfo.setPtLabel(" ชื่อ   "+oUserInfo.GetPtName()+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
		       
		        //if(oUserInfo.GetMemo().indexOf("Z515")>=0){
		        //	JOptionPane.showMessageDialog(null, "Z515" ,"Message", JOptionPane.OK_OPTION);
		        //}
		        String date_in=oUserInfo.GetPtVisitdate().trim();
		        String y=date_in.substring(0, 4);
		        String m=date_in.substring(5,7);
		        String d=date_in.substring(8);

		    	//Label_Info.setText(d+":"+m+":"+y+"  รหัสห้องตรวจ:  "+c+"  หน้าที่ : "+p);
		        //Label_Info.setText("Hn "+hn+" . "+oUserInfo.GetPtVisitdate()+"-"+clinic_pt+"**"+y+"-"+m+"-"+d);
		        //System.out.println(fn);
		        
		    	String res=getFn(hn,y+m+d).trim();
		    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
		    	if(res.substring(0, res.indexOf(":")).equals("1")){
		    		//oUserInfo.setFN(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
		    		showFilePDF(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
		    		//System.out.println("1111"+res.substring(0, res.indexOf(":")));
		    	}
		    	else if(res.substring(0, res.indexOf(":")).equals("0")){
					JOptionPane.showMessageDialog(null,"ไม่พบ file ของ HN:"+hn,"File Error",JOptionPane.ERROR_MESSAGE);
					
		    	}
		    	else{
		    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
		    		String text=res.substring(res.indexOf(":")+1);
		    		String[] parts = text.split("-");
		    		//String fn_[] = new String[npage];
		    		 

		    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกไฟล์", "FN", JOptionPane.QUESTION_MESSAGE,
		    	        null, parts, "");
		    		//oUserInfo.setFN(selected);
		    		//System.out.println("666666666....."+selected);
		    		//System.out.println("666888888888....."+selected.toString().trim().substring(selected.indexOf("[1115]")+1));
		    		showFilePDF(selected);
		    		//System.out.println(selected);
		    	}

				
		     

			}
		});
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		table.setRowHeight(25);		
		table.setFillsViewportHeight(true);
 
		scrollPane = new JScrollPane(table);
		getClearData();
		
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		
		bottomPanel = new JPanel();
		leftPanel.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(null);
		 
		bottomPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		ButtonRefresh = new JButton("Refresh");
		ButtonRefresh.setForeground(UIManager.getColor("Button.darkShadow"));

		ButtonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Label_Date.setText("วันที่ "+Setup.ShowThaiDate(oUserInfo.GetPtVisitdate()));
				clearValue();
				getData();
				 
			}
		});
		ButtonRefresh.setBounds((((screen.width*2)/10)/2-45), 2, 90, 25);
		bottomPanel.add(ButtonRefresh);
	}
	public void setMainData(){
		mid2 = new JPanel();
		mid2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//mid2.setBackground(new Color(206,203,208));
		mid2.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10-480), screen.height-215));		 
		mid2.setLayout(new BorderLayout(0, 0));
		
		Label_IMG = new JLabel("");
		Label_IMG.setHorizontalAlignment(SwingConstants.CENTER);
		//Label_IMG.setBounds(1,1,800,1000);

		scrollPaneImg = new JScrollPane(Label_IMG);
		mid2.add(scrollPaneImg, BorderLayout.CENTER);
		midPanel.add(mid2, BorderLayout.CENTER);
		
		mid1 = new JPanel();
		mid1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u0E23\u0E32\u0E22\u0E25\u0E30\u0E40\u0E2D\u0E35\u0E22\u0E14\u0E02\u0E2D\u0E07\u0E44\u0E1F\u0E25\u0E4C", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		mid1.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10-480), 40));
		midPanel.add(mid1, BorderLayout.NORTH);
		mid1.setLayout(null);
		
		Label_Info = new JLabel(" ");
		Label_Info.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Info.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Info.setBounds(20, 18, 300, 15);
		mid1.add(Label_Info);
		
		JButton ButtonPrint = new JButton("Print");
		ButtonPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//ClearFile("c:\\utthphar\\");
				printOPD(fn);
				//ClearFile("c:\\utthphar\\");
			}
		});
		ButtonPrint.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/printer.png")));
		ButtonPrint.setBounds(300, 12, 100, 25);
		mid1.add(ButtonPrint);
	}
	
	public void searchPtvn(String vn) {
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and vnpres.clinic='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length==0){
			sql_user="";
		}
		else if(parts.length>1){
			sql_user1="and vnpres.clinic in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
			 
		}
		if(!Setup.checkEmptyTextField(TextField_VN) && TextField_VN.getText().trim().length()<6){
			int p=0;
			Connection conn;
			PreparedStatement stmt,stmt1;
			ResultSet rs,rs1;
			//String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnpres.vn=?  "+sql_user+" "; 
			
			String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnpres.vn=?  "; 
			Vector<Vector<String>> data = new Vector<Vector<String>>();
			try {
				conn=new DBmanager().getConnMSSql();
				stmt = conn.prepareStatement(query);
				stmt.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())); 
				stmt.setString(2, vn.trim());
				rs = stmt.executeQuery();
				 
				while (rs.next()) {
					oUserInfo.setPtHN(rs.getString(2).trim());
					String res=getClinic(vn,Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())).trim();
			    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
			    	if(res.substring(0, res.indexOf(":")).equals("1")){
			    		searchVN(vn,(rs.getString(2)).trim(),res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    		oUserInfo.setPtCliniccode(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    	}
			    	else if(res.substring(0, res.indexOf(":")).equals("0")){
			    		
			    	}
			    	else{
			    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
			    		String text=res.substring(res.indexOf(":")+1);
			    		String[] parts1 = text.split("-");
			    		//String fn_[] = new String[npage];
			    		 

			    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกห้องตรวจ", "Clinic", JOptionPane.QUESTION_MESSAGE,
			    	        null, parts1, "");
			    		searchVN(vn,(rs.getString(2)).trim(),selected);
			    		oUserInfo.setPtCliniccode(selected);
			    		//System.out.println(selected);
			    	}
			    	String name=getName(rs.getString(2).trim());
			    	oUserInfo.setPtName(name);

				    String inscl="";
				        
				    oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
				    p++;  
				}
			
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p==0) {
				clearValue();
				JOptionPane.showMessageDialog(null,"ไม่พบ vn ในห้องตรวจนี้","VN Error",JOptionPane.ERROR_MESSAGE);
				
			}
		}
		else if(!Setup.checkEmptyTextField(TextField_VN) && TextField_VN.getText().trim().length()==21){
			String clinic_in=TextField_VN.getText().trim().substring(8,12);
			String hn_in=TextField_VN.getText().trim().substring(14);
			String visitdate_in=Setup.DateInDBMSSQLno(TextField_VN.getText().trim().substring(0,8));
			System.out.println(clinic_in+"-"+hn_in+"-"+visitdate_in);
			oUserInfo.setPtHN(hn_in);
			System.out.println("hn22222..."+oUserInfo.GetPtHN());
			
			int p=0;
			Connection conn;
			PreparedStatement stmt,stmt1;
			ResultSet rs,rs1;
			String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnmst.hn=?  "; 
			Vector<Vector<String>> data = new Vector<Vector<String>>();
			try {
				conn=new DBmanager().getConnMSSql();
				stmt = conn.prepareStatement(query);
				stmt.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())); 
				stmt.setString(2,hn_in.trim());
				rs = stmt.executeQuery();
				 
				while (rs.next()) {
					oUserInfo.setPtVN(rs.getString(1).trim());
					String res=getClinic(rs.getString(1).trim(),Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())).trim();
			    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
			    	if(res.substring(0, res.indexOf(":")).equals("1")){
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    		oUserInfo.setPtCliniccode(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    	}
			    	else if(res.substring(0, res.indexOf(":")).equals("0")){
			    		
			    	}
			    	else{
			    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
			    		String text=res.substring(res.indexOf(":")+1);
			    		String[] parts1 = text.split("-");
			    		//String fn_[] = new String[npage];
			    		 

			    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกห้องตรวจ", "Clinic", JOptionPane.QUESTION_MESSAGE,
			    	        null, parts1, "");
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),selected);
			    		oUserInfo.setPtCliniccode(selected);
			    		//System.out.println(selected);
			    	}
			    	String name=getName(rs.getString(2).trim());
			    	oUserInfo.setPtName(name);

				    String inscl="";
				        
				    oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
				    p++;  
				}
			
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p==0) {
				clearValue();
				JOptionPane.showMessageDialog(null,"ไม่พบ vn ในห้องตรวจนี้","VN Error",JOptionPane.ERROR_MESSAGE);
				
			}
		}
		else if(!Setup.checkEmptyTextField(TextField_VN) && TextField_VN.getText().trim().length()==25){
			String clinic_in=TextField_VN.getText().trim().substring(8,12);
			String hn_in=TextField_VN.getText().trim().substring(18);
			String visitdate_in=Setup.DateInDBMSSQLno(TextField_VN.getText().trim().substring(0,8));
			
			oUserInfo.setPtHN(hn_in);
			
			int p=0;
			Connection conn;
			PreparedStatement stmt,stmt1;
			ResultSet rs,rs1;
			String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnmat.hn=?  and vnpres.clinic=? "; 
			Vector<Vector<String>> data = new Vector<Vector<String>>();
			try {
				conn=new DBmanager().getConnMSSql();
				stmt = conn.prepareStatement(query);
				stmt.setString(1, visitdate_in); 
				stmt.setString(2,hn_in.trim());
				stmt.setString(3,clinic_in.trim());
				rs = stmt.executeQuery();
				 
				while (rs.next()) {
					oUserInfo.setPtVN(rs.getString(1).trim());
					String res=getClinic(rs.getString(1).trim(),Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())).trim();
			    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
			    	if(res.substring(0, res.indexOf(":")).equals("1")){
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    		oUserInfo.setPtCliniccode(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    	}
			    	else if(res.substring(0, res.indexOf(":")).equals("0")){
			    		
			    	}
			    	else{
			    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
			    		String text=res.substring(res.indexOf(":")+1);
			    		String[] parts1 = text.split("-");
			    		//String fn_[] = new String[npage];
			    		 

			    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกห้องตรวจ", "Clinic", JOptionPane.QUESTION_MESSAGE,
			    	        null, parts1, "");
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),selected);
			    		oUserInfo.setPtCliniccode(selected);
			    		//System.out.println(selected);
			    	}
			    	String name=getName(rs.getString(2).trim());
			    	oUserInfo.setPtName(name);

				    String inscl="";
				        
				    oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
				    p++;  
				}
			
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p==0) {
				clearValue();
				JOptionPane.showMessageDialog(null,"ไม่พบ vn ในห้องตรวจนี้","VN Error",JOptionPane.ERROR_MESSAGE);
				
			}
		}
		
	}
	public void searchPthn(String hn_in){
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and vnpres.clinic='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length==0){
			sql_user="";
		}
		else if(parts.length>1){
			sql_user1="and vnpres.clinic in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
			 
		}
		if(!Setup.checkEmptyTextField(TextField_HN) && TextField_HN.getText().trim().length()==7){
			
			int p=0;
			Connection conn;
			PreparedStatement stmt,stmt1;
			ResultSet rs,rs1;
			//String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnmst.hn=?  "+sql_user+" "; 
			
			String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?  and vnmst.hn=? "; 
			Vector<Vector<String>> data = new Vector<Vector<String>>();
			try {
				conn=new DBmanager().getConnMSSql();
				stmt = conn.prepareStatement(query);
				stmt.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())); 
				stmt.setString(2, hn_in.trim());
				rs = stmt.executeQuery();
				 
				while (rs.next()) {
					oUserInfo.setPtVN(rs.getString(1).trim());
					String res=getClinic(rs.getString(1).trim(),Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())).trim();
			    	//System.out.println("res  --- "+res+"--"+res.substring(0, res.indexOf(":")));
			    	if(res.substring(0, res.indexOf(":")).equals("1")){
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    		oUserInfo.setPtCliniccode(res.substring(res.indexOf(":")+1,(res.indexOf("-"))));
			    	}
			    	else if(res.substring(0, res.indexOf(":")).equals("0")){
			    		
			    	}
			    	else{
			    		//int npage=Integer.parseInt(res.substring(0, res.indexOf(":")));
			    		String text=res.substring(res.indexOf(":")+1);
			    		String[] parts1 = text.split("-");
			    		//String fn_[] = new String[npage];
			    		 

			    		String selected=(String)JOptionPane.showInputDialog(null, "กรุณาเลือกห้องตรวจ", "Clinic", JOptionPane.QUESTION_MESSAGE,
			    	        null, parts1, "");
			    		searchVN(rs.getString(1).trim(),(rs.getString(2)).trim(),selected);
			    		oUserInfo.setPtCliniccode(selected);
			    		//System.out.println(selected);
			    	}
			    	String name=getName(rs.getString(2).trim());
			    	oUserInfo.setPtName(name);

				    String inscl="";
				        
				    oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+"   [ NHSO   :  "+ inscl+" ]");
				    p++;  
				}
			
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p==0) {
				clearValue();
				JOptionPane.showMessageDialog(null,"ไม่พบ vn ในห้องตรวจนี้","VN Error",JOptionPane.ERROR_MESSAGE);
				
			}
		}
	}
	public void searchVN(String vn,String hn,String clinic){
		Connection conn;
		PreparedStatement stmt,stmt1,stmt2,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		
		String query_db="select  BirthDateTime from patient_info where hn='"+hn.trim()+"'" ; 
		String query_right="select  rightcode from vnpres where visitdate=? and clinic='"+clinic+"'   and vn='"+vn.trim()+"'" ; 
		String query_ref="select  ref from patient_ref where  reftype='01'  and hn='"+hn.trim()+"'" ; 
		
		conn=new DBmanager().getConnMSSql();
		 
		try {
			stmt = conn.prepareStatement(query_db);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String  right="";
				if(rs.getString(1) !=null){
					Date birthday=rs.getDate(1);
					String age_pt=Setup.AgeInAll(birthday.toString()).trim();
					oUserInfo.setPtAge(age_pt);
				}		
				
			}
			stmt.close();
			//
			stmt1 = conn.prepareStatement(query_right);
			stmt1.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate()));
			rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				String  right="";				
				if(rs1.getString(1) !=null){
					for(int i=0;i<InApp.rightname.length;i++){
						if(rs1.getString(1).trim().equals(InApp.rightname[i][0])){
							right=InApp.rightname[i][1];
							break;
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					oUserInfo.setRightCode(right+"("+rs1.getString(1).trim()+")");
				}
							
			}
			stmt1.close();
			//
			stmt2 = conn.prepareStatement(query_ref);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {			
				if(rs2.getString(1) !=null){
					oUserInfo.setPtCID(rs2.getString(1).trim());
				}
							
			}
			stmt2.close();
			//
			String sql_allergy="select memo from view_MEDICINE_ALLERGIC where hn=? and suffix='8'";
			stmt3 = conn.prepareStatement(sql_allergy);
			stmt3.setString(1, hn);
			rs3 = stmt3.executeQuery();
			String memo1="",memo2="";
			//String memo="";
			while (rs3.next()) {
				if(rs3.getString(1)!=null){
					 memo1=rs3.getString(1).trim();
				}
			}
			rs3.close();
			stmt3.close();
			
			for(int i=0;i<PharFrame.hn_z515.length;i++){
				if(hn.equals(PharFrame.hn_z515[i])){
					memo2="Z515";
					break;
				}
				//System.out.println(i+1+". "+labunitname[i][0]);
			}
			
			 
			oUserInfo.setMemo(memo1);
			oUserInfo.setMemo1(memo2);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void clearValue(){
		 
        oUserInfo.setPtVN("");  
       // oUserInfo.setFN("");
        oUserInfo.setPtName("");        
        oUserInfo.setPtHN("");        
        oUserInfo.setRightCode("");        
        oUserInfo.setPtCliniccode("");         
        oUserInfo.setPtAge("");
        oUserInfo.setPtLabel("");       
        oUserInfo.setPtCID("");         
        oUserInfo.setMemo("");
        oUserInfo.setMemo1("");
        Label_IMG.setIcon(null);
        fn="";
        //oUserInfo.setFormReport("");
        //mainHDFSOPDPanel.clear();
        //mainOldHDFSOPDPanel.clear();
       // medOPDPanel.clear();
         
	}
	public String getClinic(String vn_in,String fndate){
		String clinic="";
		int p=0;
		try{
	    	String sql_clinic="select  clinic from vnpres where vn=? and visitdate= '"+fndate+"'  order by clinic";
			Connection conn=new DBmanager().getConnMSSql();
			PreparedStatement stmt = conn.prepareStatement(sql_clinic);
			stmt.setString(1, vn_in);
			 
			ResultSet rs = stmt.executeQuery();
			 
			while (rs.next()) {
				if(rs.getString(1)!=null){
					 
					clinic+=rs.getString(1).trim()+"-";
				}
				p++;
			}
	    	stmt.close();
	    	conn.close();
	    	 
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p+":"+clinic;
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
				String age_pt=Setup.AgeInAll(birthday.toString()).trim();
				if(rs.getString(4)==null || rs.getString(4).equals("1") || rs.getString(4).equals("2") || rs.getString(4).equals("3") || rs.getString(4).equals("127") || rs.getString(4).equals("128")){
					sex_pt=rs.getString(2).trim();
					mar_pt=rs.getString(3).trim();				 
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
					/**
					for(int i=0;i<oUserInfo.getInitName().length;i++){
						if(rs.getString(4).trim().equals(oUserInfo.getInitName()[i][0])){
							initname=oUserInfo.getInitName()[i][1];
							break;
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					*/
					for(int i=0;i<InApp.initname_indb.length;i++){
						if(rs.getString(4).trim().equals(InApp.initname_indb[i][0])){
							initname=InApp.initname_indb[i][1];
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
	public void getClearData(){
		table.setModel(fetchDataOPDClear());
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(40 );
		columnModel.getColumn(2).setPreferredWidth((screen.width*2)/10-80);
		
		for (int n = 2; n < 9; n++) {	
			columnModel.getColumn(n+1).setPreferredWidth(0);
			columnModel.getColumn(n+1).setMinWidth(0);
			columnModel.getColumn(n+1).setMaxWidth(0);
		}
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataOPDClear(){
		return new DefaultTableModel(data, columnNames);
	}
	public void getData(){
		table.setModel(fetchDataOPD());
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(40 );
		columnModel.getColumn(2).setPreferredWidth((screen.width*2)/10-80);
		
		for (int n = 2; n < 9; n++) {	
			columnModel.getColumn(n+1).setPreferredWidth(0);
			columnModel.getColumn(n+1).setMinWidth(0);
			columnModel.getColumn(n+1).setMaxWidth(0);
		}
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataOPD(){
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and vnpres.clinic='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length==0){
			sql_user="";
		}
		else if(parts.length>1){
			sql_user1="and vnpres.clinic in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
			 
		}

		Connection conn;
		PreparedStatement stmt,stmt1;
		ResultSet rs,rs1;
		//String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?   "+ptStatus+" "+sql_user+"  order by case IsNumeric(vnmst.vn)  when 1 then Replicate('0', 100 - Len(vnmst.vn)) + vnmst.vn else vnmst.vn end "; 
		
		String query="select  vnmst.vn, vnmst.hn, vnpres.diagoutdatetime ,vnpres.clinic from vnmst,vnpres where vnmst.vn=vnpres.vn and vnmst.visitdate=vnpres.visitdate and vnmst.visitdate=?   "+ptStatus+"   order by case IsNumeric(vnmst.vn)  when 1 then Replicate('0', 100 - Len(vnmst.vn)) + vnmst.vn else vnmst.vn end "; 
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		try {
			conn=new DBmanager().getConnMSSql();
			stmt = conn.prepareStatement(query);
			stmt.setString(1, Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())); 
			//stmt.setString(2, "1100");
			rs = stmt.executeQuery();
			int p=1;
			while (rs.next()) {
			
				String clinic_pt="";
				if(rs.getString(4)!=null){
					clinic_pt=rs.getString(4);
				}
				String pt_status="0";
				if(rs.getString(3)!=null){
					pt_status="1";
				}
				final Vector<String> vstring = new Vector<String>();
				vstring.add(" "+Integer.toString(p) );
				//vstring.add(rs.getString(1)+": "+initname+" "+rs.getString(2).substring(1)+" "+rs.getString(3).substring(1));
				vstring.add(rs.getString(1));
				vstring.add(getName(rs.getString(2).trim()));
				vstring.add(rs.getString(2).trim());
				vstring.add(clinic_pt);
				vstring.add(pt_status);
				 
				data.add(vstring);
				p++;
			}
		
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultTableModel(data, columnNames);
	}
	public String getFn(String hn_in,String fndate){
		//System.out.println(hn_in+"--"+fndate);;
		String fn="";
		int p=0;
		String folderhn=hn_in.substring(0, 2).trim();
		String path1="/user/hadoop/"+folderhn+"/"+hn_in;
		 
		//String filename="255511290001014700005HN.pdf";
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://172.17.71.2:9000");
		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "/");
		FileSystem fs;
		try {
			fs = FileSystem.get(URI.create("hdfs://172.17.71.2:9000"), conf);
			Path newFolderPath= new Path(path1);
			FileStatus[] fileStatus = fs.listStatus(newFolderPath);
			int num=1;
			for(FileStatus status : fileStatus){
				String pp=status.getPath().toString().trim();
				//System.out.println(pp);
    	        if(pp.length()==78) {
    	        	String ppp=pp.substring( pp.length()-31,pp.length()-23);
    	        	if(ppp.equals("25000101")) {	 
    	        	}
    	        	else{
    	        		String pp1=pp.substring(pp.length()-31,pp.length()-6);
    	        		if(pp1.substring(0,8).equals(fndate.trim())) {
    	        			fn+="["+pp1.substring(8,12)+" ("+pp1.substring(12,14)+")]"+pp1+"-";
    	        			p++;
    	        		}
            		 
    	    	         
    	        	}    		
    	        	 
    	        }
    	        //
    	        else if(pp.length()==74) {
    	        	String ppp=pp.substring( pp.length()-27,pp.length()-19);
    	        	if(ppp.equals("25000101")) {
    	        		 
    	        	}
    	        	else{
    	        		String pp1=pp.substring(pp.length()-27,pp.length()-6);
    	        		if(pp1.substring(0,8).equals(fndate.trim())) {
    	        			fn+="["+pp1.substring(8,12)+" ("+pp1.substring(12,14)+")]"+pp1+"-";
    	        			p++;
    	        		}
    	    	         
    	        	}    		
    	        	 
    	        }
    	    }
				
			fs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//System.out.println("no..................");
			JOptionPane.showMessageDialog(null,"ไม่พบ file ของ HN:"+hn_in,"File Error",JOptionPane.ERROR_MESSAGE);
		}
	
		return p+":"+fn;
	}
	public  void showFilePDF(String filename_in){
		String filename="";
		if(filename_in.indexOf("]")>0) {
			filename=filename_in.trim().substring(filename_in.indexOf("]")+1);
			fn=filename;
		}else {
			filename=filename_in.trim();
			fn=filename;
		}
		//System.out.println("fff.."+filename);
		if(filename.length()==21 || filename.length()==25){
			ImageIcon icon=null;			
			icon = new ImageIcon(previewPDFDocumentInImage(filename));
			Label_IMG.setIcon(icon);
		}else{
			JOptionPane.showMessageDialog(null,"filename error:"+filename ,"Infomation",JOptionPane.WARNING_MESSAGE); 			
			Label_Info.setText("");
			clearValue();
			Label_IMG.setIcon(null);
		}
		TextField_VN.requestFocusInWindow();
		mid2.repaint();
		mid2.revalidate();
	}
	public  Image previewPDFDocumentInImage(String filescanname){
		 
		Image img=null;
		ByteBuffer buf = null;
		String hn=filescanname.substring(filescanname.length()-7).trim();
		String folderhn=hn.substring(0, 2).trim();
		String path1="/user/hadoop/"+folderhn+"/"+hn;
		 
		//String filename="255511290001014700005HN.pdf";
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://172.17.71.2:9000");
		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "/");
		FileSystem fs;
		PDFFile pdffile;
		try {
			fs = FileSystem.get(URI.create("hdfs://172.17.71.2:9000"), conf);
			Path newFolderPath= new Path(path1);
			 Path path = new Path(newFolderPath+"/"+filescanname+"HN.pdf");
			 
			    if (!fs.exists(path)) {
			      //System.out.println("File " + oUserInfo.GetFN()+"HN.pdf" + " does not exists");
			 
			}
			    
			FSDataInputStream in = fs.open(path);
			byte[] b= IOUtils.toByteArray(in);
			buf = ByteBuffer.wrap(b);
			
			pdffile = new PDFFile(buf);
			PDFPage page = pdffile.getPage(1);
			//get the width and height for the doc at the default zoom
	    	Rectangle rect = new Rectangle(0, 0, (int)page.getBBox().getWidth(), (int)page.getBBox().getHeight());
	    	//img = page.getImage(rect.width, rect.height, //width &amp; height
	        img = page.getImage(800, 1200, //width &amp; height
	                rect, // clip rect
	                null, // null for the ImageObserver
	                true, // fill background with white
	                true) // block until drawing is done
	        ;
			fs.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"ไม่พบ file ของ HN:"+hn,"File Error",JOptionPane.ERROR_MESSAGE);			
			//e.printStackTrace();
			img=null;
		}
		return img;
    	 

 	}

	public void printOPD(String fn){
		ByteBuffer buf = null;
		String hn=fn.substring(fn.length()-7).trim();
		String folderhn=hn.substring(0, 2).trim();
		String path1="/user/hadoop/"+folderhn+"/"+hn;		 
		//String filename="255511290001014700005HN.pdf";
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://172.17.71.2:9000");
		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "/");
		FileSystem fs;
		
		
		PDFFile pdffile;
		try {
			fs = FileSystem.get(URI.create("hdfs://172.17.71.2:9000"), conf);
			Path newFolderPath= new Path(path1);
			 Path path = new Path(newFolderPath+"/"+fn+"HN.pdf");
			    if (!fs.exists(path)) {
			      System.out.println("File " +newFolderPath+"/"+fn+"HN.pdf" + " does not exists");
			      return;
			}
			    FSDataInputStream in = fs.open(path);
			    int contentLength = 2000000;
			    byte[] b = new byte[contentLength];
			    int numBytes = 0;
			
			    int offset = 0;
		           int read = 0;
		           while (read >= 0) {
		               read = in.read(b, offset, contentLength - offset);
		               if (read > 0) {
		                   offset += read;
		               }
		           }
			    buf = ByteBuffer.allocate(contentLength);
		        buf.put(b);
		         
		        
			pdffile = new PDFFile(buf);
			PDFPage page = pdffile.getPage(1);
			
			PrinterJob pjob = PrinterJob.getPrinterJob(); 
	    	Book book = new Book();
	    	Paper paper;
	    	PageFormat pf;
	    	pf = new PageFormat();
			paper = new Paper();
			
			paper.setSize(12.0*72,25.0*72);
			paper.setImageableArea(10,10 ,paper.getHeight()-10,paper.getWidth() -10);
		
			pf.setPaper(paper);
	    	 
	        PDFPrintPage pages = new PDFPrintPage(pdffile);
	        book.append(pages, pf, pdffile.getNumPages());
	    
	        pjob.setPageable(book);
	        
	       new PrintThread(pages, pjob).start();
	        
	        
	        
	       // if (pjob.printDialog()) {
	        //   new PrintThread(pages, pjob).start();
	       //}
	       fs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	class PrintThread extends Thread {

        PDFPrintPage ptPages;
        PrinterJob ptPjob;

        public PrintThread(PDFPrintPage pages, PrinterJob pjob) {
            ptPages = pages;
            ptPjob = pjob;
            setName(getClass().getName());
        }
        public void run() {
            try {
                ptPages.show(ptPjob);
                ptPjob.print();
            } catch (PrinterException pe) {          
            }
            ptPages.hide();
        }
    }
	public void copyFile(String fn) {
		String hn=fn.substring(fn.length()-7).trim();
		String folderhn=hn.substring(0, 2).trim();
		String path1="/user/hadoop/"+folderhn+"/"+hn;		 
		//String filename="255511290001014700005HN.pdf";
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://172.17.71.2:9000");
		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "/");
		FileSystem fs;
		
		try {
			fs = FileSystem.get(URI.create("hdfs://172.17.71.2:9000"), conf);
			Path newFolderPath= new Path(path1);
			 Path path = new Path(newFolderPath+"/"+fn+"HN.pdf");
			    if (!fs.exists(path)) {
			      System.out.println("File " +newFolderPath+"/"+fn+"HN.pdf" + " does not exists");
			      return;
			}
			    Path localFilePath = new Path("c://utthphar//"+fn+".pdf");
			
				fs.copyToLocalFile(false,path,localFilePath, true);
			    System.out.println("File " +newFolderPath+"/"+fn+"HN.pdf" + " ");		
		
	       fs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void checkFolder(String folder){
		File file = new File(folder);
		if (!file.exists()) {
			if (file.mkdir()) {
				
			}else {
				
			}
		}
	}
	public void ClearFile(String file_in){
		
		File folder = new File(file_in);
		File[] listOfFiles = folder.listFiles();
        //System.out.println(listOfFiles.length);
        String file_name_input[]=new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
        	if (listOfFiles[i].isFile()) {  		
        		listOfFiles[i].delete();
        	}
        	else if(listOfFiles[i].isDirectory()){
        		String[]entries = listOfFiles[i].list();
        		for(String s: entries){			
        		    File currentFile = new File(listOfFiles[i].getPath(),s);
        		    currentFile.delete();
        		}
        		listOfFiles[i].delete();
        	}
        }
	}
}
