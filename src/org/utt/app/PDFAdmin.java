package org.utt.app;

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
import java.io.IOException;
import java.net.URI;
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

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.utt.app.InApp;
import org.utt.app.OPDFrame;
import org.utt.app.dao.DBmanager;
import org.utt.app.opd.MainHDFSOPDPanel;
import org.utt.app.opd.ObjectOPD;
import org.utt.app.util.DateLabelFormatter;
import org.utt.app.util.Setup;


public class PDFAdmin extends JPanel{

	Dimension screen;
	JTabbedPane tabbedPane;
	JSplitPane split,splitR;
	JPanel leftPanel,topMainPanel,midPanel;
	
	Vector<String> columnNames;
	JTable table;
	Vector<Vector<String>> data;
	JPanel bottomPanel;
	JButton ButtonRefresh;
	JScrollPane scrollPane;
	JRadioButton PtStatus1,PtStatus2;
	ButtonGroup PtGroup = new ButtonGroup();
	JPanel topLeftPanel,mainLeftPanel,topMainLeftPanel,pt_info,jPanel ;
	JLabel Label_Date,Label_VN,Label_HN,Label_CID,Label_NAME,Label_Memo,Label_Extra,Label_Extra1;
	JTextField TextField_VN,TextField_HN,TextField_CID;
	JDatePickerImpl picker;
	JTextArea textArea;
	String day="",month="",dateSearch="";
	String ptStatus=" and vnpres.diagoutdatetime is null";
	MainHDFSOPDPanel mainHDFSOPDPanel;
	/**
	 * 
	 */
	public PDFAdmin() {

		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-5, screen.height-100));
		split = new JSplitPane(split.HORIZONTAL_SPLIT);
		split.setDividerLocation((screen.width*2)/10);
		splitR = new JSplitPane(split.VERTICAL_SPLIT);
		splitR.setDividerLocation(140);
		
		setTop();
		setLeft();
		setRight();
		 
		split.setRightComponent(splitR);
		split.setLeftComponent(leftPanel);
		splitR.setTopComponent(topMainPanel);
		splitR.setBottomComponent(midPanel);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);

	    tabbedPane.addTab("<html>O<br>P<br>D<br> </html>", null, split, "OPD");
		
				
		
		add(tabbedPane, BorderLayout.CENTER);
	}

	public void setLeft(){
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension((screen.width*2)/10, screen.height-175));
		leftPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		leftPanel.setLayout(new BorderLayout(0, 0));
		
		topLeftPanel = new JPanel();
		topLeftPanel.setLayout(new BorderLayout(0, 0));
		topLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		leftPanel.add(topLeftPanel, BorderLayout.NORTH);
		
		Label_Date= new JLabel("วันที่ "+Setup.ShowThaiDate(dateSearch),JLabel.CENTER);
		Label_Date.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Label_Date.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		topLeftPanel.add(Label_Date, BorderLayout.NORTH);
		
		mainLeftPanel = new JPanel();
		mainLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, screen.height-405));
		mainLeftPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mainLeftPanel.setLayout(new BorderLayout(0, 0));
		leftPanel.add(mainLeftPanel, BorderLayout.CENTER);
		
		topMainLeftPanel = new JPanel();
		mainLeftPanel.add(topMainLeftPanel, BorderLayout.NORTH);
		topMainLeftPanel.setBackground(Setup.getColor());
		topMainLeftPanel.setLayout(null);
		topMainLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		
		JLabel LabelListdata = new JLabel("List of File",JLabel.CENTER);
		LabelListdata.setFont(new Font("Tahoma", Font.PLAIN, 13));
		LabelListdata.setBounds(5,5,100,20);
		topMainLeftPanel.add(LabelListdata);
		
		PtStatus1 = new JRadioButton("All");
		PtStatus1.setBackground(Setup.getColor());
		PtStatus1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus="";
			}
		});
		PtGroup.add(PtStatus1);
		PtStatus1.setBounds(100, 5, 40, 25);
		topMainLeftPanel.add(PtStatus1);
		
		PtStatus2 = new JRadioButton("New");
		PtStatus2.setBackground(Setup.getColor());
		PtStatus2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus=" and vnpres.diagoutdatetime is null";
			}
		});
		PtGroup.add(PtStatus2);
		PtStatus2.setSelected(true);
		PtStatus2.setBounds(145, 5, 50, 25);
		topMainLeftPanel.add(PtStatus2);
		
		columnNames = new Vector<String>();
  		columnNames.add("ที่");
		columnNames.add("  VN ");
		columnNames.add("   รายชื่อผู้รับบริการ  ");
		for (int n = 3; n < 6; n++) {	
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
		        
		        String select_vn=table.getValueAt(row,1).toString().trim();

			}
		});
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		table.setRowHeight(25);		
		table.setFillsViewportHeight(true);
 
		scrollPane = new JScrollPane(table);
		//getData();
		
		mainLeftPanel.add(scrollPane, BorderLayout.CENTER);
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Setup.getColor());
		mainLeftPanel.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(null);
		 
		bottomPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		ButtonRefresh = new JButton("Refresh");
		ButtonRefresh.setBackground(Setup.getColor());
		ButtonRefresh.setForeground(UIManager.getColor("Button.darkShadow"));

		ButtonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
				clearValue();
			}
		});
		ButtonRefresh.setBounds((((screen.width*2)/10)/2-45), 2, 90, 25);
		bottomPanel.add(ButtonRefresh);
	}
	public void setTop(){
		topMainPanel = new JPanel();
		topMainPanel.setLayout(new BorderLayout(0, 0));
		topMainPanel.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), 140));
		topMainPanel.setBackground(Setup.getColor());  
			
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
				//clearValue();

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
				

				
				 
			}
		});
		
		Label_CID = new JLabel("ID");
		Label_CID.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_CID.setBounds(510, 12, 20, 14);
		pt_info.add(Label_CID);
		
		TextField_CID = new JTextField();
		TextField_CID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TextField_CID.setBounds(530, 10, 150, 20);
		TextField_CID.setText("");
		pt_info.add(TextField_CID);
		TextField_CID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 

			}
		});
		
		Label_NAME = new JLabel(" ชื่อ  ");
		Label_NAME.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_NAME.setBounds(4, 30, 970, 30);
		pt_info.add(Label_NAME);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setForeground(new Color(255, 0, 0));
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textArea.setBackground(Setup.getColor());
		textArea.setBounds(70, 70, 900, 40);
		pt_info.add(textArea);
		
		Label_Memo = new JLabel("การแพ้ยา ");
		Label_Memo.setForeground(new Color(255, 0, 0));
		Label_Memo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Memo.setBackground(Setup.getColor());
		Label_Memo.setBounds(10, 65, 55, 30);
		pt_info.add(Label_Memo);
		
		Label_Extra = new JLabel("ข้อมูลเพิ่มเติม");
		Label_Extra.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Setup.SetUnderline(Label_Extra);
		Label_Extra.setBounds(10, 110, 80, 30);
		Label_Extra.setBackground(Setup.getColor());
		pt_info.add(Label_Extra);
		
		Label_Extra1 = new JLabel("");
		Label_Extra1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Setup.SetUnderline(Label_Extra1);
		Label_Extra1.setBounds(100, 110, 400, 30);
		Label_Extra1.setBackground(Setup.getColor());
		pt_info.add(Label_Extra1);
	}
	public void setRight(){
		midPanel = new JPanel();
		midPanel.setLayout(new BorderLayout(0, 0));
		midPanel.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), screen.height-320));
		//mainPanel.add(midPanel, BorderLayout.CENTER);
		
		tabbedPane = new JTabbedPane();
        //tabbedPane.setBackground(new Color(232, 248, 245  ));
        midPanel.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addTab("<html><b>EMR</b></html> ",new ImageIcon(getClass().getClassLoader().getResource("images/running.png")) ,mainHDFSOPDPanel);
        //tabbedPane.addTab("<html><b>Old EMR</b></html> ",new ImageIcon(InApp.class.getResource("/org/utt/app/gfx/box.png")) ,mainOldHDFSOPDPanel);

	}
	public void getData(String hn){
		table.setModel(fetchData(hn));
		TableColumnModel columnModel = table.getColumnModel();		
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(220);
		columnModel.getColumn(2).setPreferredWidth(0);
		columnModel.getColumn(2).setMinWidth(0);
		columnModel.getColumn(2).setMaxWidth(0);
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
		
	}
	public  DefaultTableModel fetchData(String hn){
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		String folderhn=hn.substring(0, 2).trim();
		String path1="/user/hadoop/"+folderhn+"/"+hn;
		 
		//String filename="255511290001014700005HN.pdf";
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://172.17.71.2:9000");
		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "C:\\winutils");
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
    	        		 
    	        		String pppp=pp.substring( pp.length()-23,pp.length()-19);
    	        		String ss=pp.substring( pp.length()-19,pp.length()-17);
    	        		String ss1=pp.substring( pp.length()-17,pp.length()-15);
    	        		String ss2=pp.substring( pp.length()-15,pp.length()-13);
    	        		if(ppp.substring(0,2).equals("25") && (ss1.equals("00") || ss1.equals("01") || ss1.equals("02") || ss1.equals("03"))) {
    	        			//System.out.println(ppp+"- "+pppp+"-"+ss+"-"+ss1);
            	        	List<ArrayList<String>> namesAndNumbers = new ArrayList<ArrayList<String>>();
            				namesAndNumbers.add(new ArrayList<String>(Arrays.asList(ppp, pp)));
            				Collections.sort(namesAndNumbers, new Comparator<ArrayList<String>>() {    
            				        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            				            return o1.get(0).compareTo(o2.get(0));
            				        }               
            				});
            				//System.out.println(namesAndNumbers);
            				final Vector<String> vstring = new Vector<String>();
            				vstring.add(" "+num );
            				String pp1=pp.substring(pp.length()-31,pp.length()-6);
            				String year_fn=pp1.substring(0, 4);	             
            	            String month_fn=pp1.substring(4, 6);            
            	            String date_fn=pp1.substring(6, 8);
            	            String clinic_fn=pp1.substring(8, 12);
            	            String page_fn=pp1.substring(12, 14);
            				vstring.add(" "+date_fn+" "+Setup.getMonthShortThaiName(month_fn)+" "+year_fn+"   |"+clinic_fn);
            				vstring.add(pp.substring(pp.length()-31,pp.length()-6));	           				
            				 
            				data.add(vstring);
            				num++;
    	        		}	        		 
    	    	         
    	        	}    		
    	        	 
    	        }
    	        //
    	        else if(pp.length()==74) {
    	        	String ppp=pp.substring( pp.length()-27,pp.length()-19);
    	        	if(ppp.equals("25000101")) {
    	        		 
    	        	}
    	        	else{
    	        		 
    	        		String pppp=pp.substring( pp.length()-19,pp.length()-17);
    	        		if(ppp.substring(0,2).equals("25")) {
    	        			//System.out.println(ppp+"- "+pppp);
            	        	List<ArrayList<String>> namesAndNumbers = new ArrayList<ArrayList<String>>();
            				namesAndNumbers.add(new ArrayList<String>(Arrays.asList(ppp, pp)));
            				Collections.sort(namesAndNumbers, new Comparator<ArrayList<String>>() {    
            				        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            				            return o1.get(0).compareTo(o2.get(0));
            				        }               
            				});
            				//System.out.println(namesAndNumbers);
            				final Vector<String> vstring = new Vector<String>();
            				vstring.add(" "+num );
            				String pp1=pp.substring(pp.length()-27,pp.length()-6);
            				String year_fn=pp1.substring(0, 4);	             
            	            String month_fn=pp1.substring(4, 6);            
            	            String date_fn=pp1.substring(6, 8);
            	            String clinic_fn=pp1.substring(8, 12);
            	            String page_fn=pp1.substring(12, 14);
            				vstring.add(" "+date_fn+" "+Setup.getMonthShortThaiName(month_fn)+" "+year_fn+"   |"+clinic_fn);
            				vstring.add(pp.substring(pp.length()-27,pp.length()-6));	
            				
            				 
            				data.add(vstring);
            				num++;
    	        		}
    	    	         
    	        	}    		
    	        	 
    	        }
    	    }
				
			fs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null,"ไม่พบ file ของ HN:"+hn,"File Error",JOptionPane.ERROR_MESSAGE);
		}
	
		return new DefaultTableModel(data, columnNames);
	}


	public void clearValue(){

        //mainOldHDFSOPDPanel.clear();
       // medOPDPanel.clear();		
	}

}
